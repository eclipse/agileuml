import java.util.Vector; 
import java.util.List; 
import java.io.*;

/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: OCL */

abstract class Expression 
{ Expression javaForm;  
  Attribute formalParameter = null; 
  // The formal parameter, if any, 
  // for which this is an actual parameter.  

  /* The modality of the expression, ie, if it 
     consists of purely sensor, purely actuator 
     or other combination of variables: */ 
  public static final int SENSOR = 0;
  public static final int ACTUATOR = 1;
  public static final int MIXED = 2;
  public static final int HASEVENT = 4; 
  public static final int ERROR = 10; 

  public int modality = ModelElement.NONE; // in uml version use SEN, ACT, etc

  public static final int UNKNOWN = -1; 
  public static final int VALUE = 0; 
  public static final int ATTRIBUTE = 1; 
  public static final int ROLE = 2; 
  public static final int VARIABLE = 3; 
  public static final int CONSTANT = 4; 
  public static final int FUNCTION = 5; 
  public static final int QUERY = 6;    // query operation
  public static final int UPDATEOP = 7; 
  public static final int CLASSID = 8;  // name of a class
  public static final int TYPE = 9; 

  protected int umlkind = UNKNOWN; 
  protected Type type = null; 
  protected Type elementType = null;  // type of elements if a collection
  protected Entity entity = null;  // entity of the elementType if a collection, value 
                                   // or variable. For a feature, the owning entity. 
  protected int multiplicity = ModelElement.ONE; 
                                   // multiplicity of the feature, if a feature
  protected boolean needsBracket = false; 
  protected boolean isStatic = false; 
  protected boolean isArray = false; // For Ref vbls
  protected boolean isSorted = false; 

  protected Type sizeofType = null; 

  protected static Vector comparitors = new Vector(); 
  static 
  { comparitors.add("="); 
    comparitors.add("<"); 
    comparitors.add("<="); 
    comparitors.add(">");
    comparitors.add(">=");
    comparitors.add("!=");  // now it is /= 
    comparitors.add("/="); 
    comparitors.add("<>"); 
  } 

  protected static Vector oclops = new Vector(); 
  static 
  { oclops.add("->select"); 
    oclops.add("->forAll"); 
    oclops.add("->exists"); 
    oclops.add("->reject");
    oclops.add("->collect");
    oclops.add("->intersect"); 
    oclops.add("->union"); 
  } // and ->intersectAll, etc. Used anywhere? 

  public static Vector operators = new Vector(); 
  static 
  { operators.add("<=>"); 
    operators.add("=>"); 
    operators.add("#"); 
    operators.add("or"); 
    operators.add("xor"); 
    operators.add("&"); 
  }

  public static Vector alloperators = new Vector(); 
  static 
  { alloperators.add("=>"); 
    alloperators.add("or"); 
    alloperators.add("&"); 
    alloperators.add("="); 
    alloperators.add("<>="); 
    alloperators.add("<"); 
    alloperators.add("<="); 
    alloperators.add(">");
    alloperators.add(">=");
    alloperators.add("!=");  // now it is /= or <>
    alloperators.add("/="); 
    alloperators.add("<>"); 
    alloperators.add("mod"); 
    alloperators.add("div"); 
    alloperators.add("+"); 
    alloperators.add("-"); 
    alloperators.add("/"); 
    alloperators.add("*"); 
    alloperators.add("\\/"); 
    alloperators.add("/\\"); 
    alloperators.add("^"); 
    alloperators.add(":"); 
    alloperators.add("/:"); 
    alloperators.add("<:"); 
    alloperators.add("/<:"); 
  }  // and? 

  public static java.util.Map oppriority = new java.util.HashMap(); 
  { oppriority.put("<=>",new Integer(0)); 
    oppriority.put("=>",new Integer(1)); 
    oppriority.put("#",new Integer(2)); 
    oppriority.put("or",new Integer(3)); 
    oppriority.put("xor",new Integer(3)); 
    oppriority.put("&",new Integer(4)); 
  }  // and? 

  // For extensions:        
  public static java.util.Map extensionoperators = new java.util.HashMap(); 
  public static java.util.Map extensionopjava = new java.util.HashMap(); 
  public static java.util.Map extensionopcsharp = new java.util.HashMap(); 
  public static java.util.Map extensionopcpp = new java.util.HashMap(); 


  public static String negateOp(String op)
  { if (op.equals("=")) { return "/="; }
    if (op.equals("<=")) { return ">"; }
    if (op.equals("<")) { return ">="; }
    if (op.equals(">=")) { return "<"; }
    if (op.equals(">")) { return "<="; }
    if (op.equals("/=")) { return "="; }
    if (op.equals("<>")) { return "="; }
    if (op.equals(":")) { return "/:"; }
    if (op.equals("/:")) { return ":"; }
    if (op.equals("<:")) { return "/<:"; }
    if (op.equals("/<:")) { return "<:"; }
    if (op.equals("!=")) { return "="; } 
    if (op.equals("->includes")) { return "->excludes"; } 
    if (op.equals("->excludes")) { return "->includes"; } 
    // if (op.equals("->exists")) { return "->forAll"; } 
    // if (op.equals("->forAll")) { return "->exists"; }
    // if (op.equals("#")) { return "!"; } 
    // if (op.equals("!")) { return "#"; } 
 
    return op;
  }  // Not correct for ->exists, ->forAll, #, !

  public static String invertOp(String op)
  { if (op.equals("->exists")) { return "->forAll"; } 
    if (op.equals("->forAll")) { return "->exists"; }
    if (op.equals("#")) { return "!"; } 
    if (op.equals("!")) { return "#"; } 
 
    return op;
  } // so that not(s->exists( x | P )) is s->forAll(x | not(P)), etc 

  public static String getCInputType(Type t) 
  { String tname = t.getName(); 
    if ("String".equals(tname)) 
    { return "char"; } 
    return tname; 
  } 

  public int getUMLKind()
  { return umlkind; } 

  public int arity()
  { return 0; } 

  public boolean isNotLocalVariable() 
  { if (umlkind == VARIABLE) 
    { return false; } 
    return true; 
  } 

  public static boolean isComparator(String opx)
  { return comparitors.contains(opx); } 

  public void setStatic(boolean s)
  { isStatic = s; } 

  public void setArray(boolean s)
  { isArray = s; } 

  public boolean isStatic()
  { return isStatic; } 

  public boolean isArray()
  { return isArray; } 

  public Expression firstConjunct()
  { return this; } 

  public Expression removeFirstConjunct()
  { return this; 
    // return new BasicExpression("true"); 
  } 

  public Type getType() { return type; }

  public void setsizeofType(Type t)
  { sizeofType = t; } 

  public Type getsizeofType()
  { return sizeofType; } 

  public boolean isMap() 
  { if (type != null && type.getName().equals("Map"))
    { return true; } 
    return false; 
  } 

  public boolean isEmptyCollection() 
  { if ("Set{}".equals(this + "") || 
        "Sequence{}".equals(this + ""))
    { return true; } 
    return false; 
  } 

  public int getKind() { return umlkind; } 

  public int getMultiplicity()  { return multiplicity; } 

  public void setUmlKind(int k)
  { umlkind = k; } 

  public void setUmlkind(int k)
  { umlkind = k; } 

  public void setEntity(Entity e)
  { entity = e; } 

  public Type getElementType() { return elementType; } 

  public void setType(Type t)
  { type = t; 
    if (type != null && type.isEntity)
    { entity = type.entity; }
  }  // No, not for features (attributes, roles, operations) 

  public void setMultiplicity(int m)
  { multiplicity = m; } 

  public void setElementType(Type t)
  { elementType = t; }

  public Entity getEntity() { return entity; } 

  public abstract void setPre(); 

  public abstract String toAST(); 

  public static String formattedString(String f)
  { /* s written with {v:fmt} to format v element */ 

    String res = "";
    boolean inElement = false;
    boolean inFormat = false;
    String var = "";   
    String fmt = "";   
    for (int i = 0; i < f.length(); i++) 
    { char c = f.charAt(i); 
      if (inElement) 
      { if (':' == c) 
        { fmt = ""; 
          inFormat = true; 
        } 
        else if ('}' == c)
        { if ("".equals(fmt))
          { fmt = "s"; } 
          res = res + "\" + String.format(\"%" + fmt + "\", " + var + ") + \""; 
          inElement = false;
          inFormat = false;  
          var = ""; 
          fmt = ""; 
        } 
        else if (inFormat) 
        { fmt = fmt + c; } // skip the format
        else 
        { var = var + c; } // var name character
      } 
      else if ('{' == c) 
      { inElement = true; } 
      else 
      { res = res + c; } // literal character
    }  

    return res + ""; 
  } 



  public Vector mutants()
  { Vector res = new Vector(); 
    res.add(this); 
    return res; 
  }  // default implementation. 

  public Vector singleMutants()
  { Vector res = new Vector(); 
    // res.add(this); 
    return res; 
  }  // default implementation. 

  public boolean isEnumerated()
  { return type != null && type.isEnumerated(); } 

  public boolean isVariable()
  { return (umlkind == VARIABLE); } 

  public boolean isFeature()
  { return (umlkind == ROLE || umlkind == ATTRIBUTE || umlkind == QUERY ||
            umlkind == UPDATEOP); 
  } 

  public boolean isSingleValued()
  { if (type == null) { return false; } 

    String tname = type.getName(); 
    if ("Set".equals(tname)) { return false; } 
    if ("Sequence".equals(tname)) { return false; } 
    return true; 
    // return multiplicity == ModelElement.ONE; }
  }  

  public boolean isMultipleValued()
  { if (type == null) { return false; } 

    String tname = type.getName(); 
    if ("Set".equals(tname)) { return true; } 
    if ("Sequence".equals(tname)) { return true; } 
    return false; 
    // return multiplicity != ModelElement.ONE;
  } 

  public int upperBound()
  { if (isMultipleValued())
    { return 1000000000; } 
    return 1; 
  } 

  public int lowerBound()
  { if (isMultipleValued())
    { return 0; } 
    return 1; 
  } 

  public abstract Vector getParameters(); 

  public static String getMatcherOperator(Expression mexpr)
  { if (mexpr == null) 
    { return "="; } 
    if (mexpr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) mexpr;
      Vector prs = be.getParameters(); 
 
      if ("is".equals(be.data) && prs != null && 
          prs.size() > 0)
      { return getMatcherOperator(
                  (Expression) prs.get(0)); 
      } 
      else if ("not".equals(be.data) && prs != null && 
          prs.size() > 0)
      { return "/="; } 
    } 
    return "="; 
  } 

  public static Expression getMatcherArgument(Expression mexpr)
  { if (mexpr == null) 
    { return new BasicExpression("null"); }
 
    if (mexpr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) mexpr; 
      if ("is".equals(be.data) && be.getParameters() != null && 
          be.getParameters().size() > 0)
      { return getMatcherArgument(
                  (Expression) be.getParameters().get(0)); 
      } 
      else if ("not".equals(be.data) && be.getParameters() != null && 
          be.getParameters().size() > 0)
      { return (Expression) be.getParameters().get(0); } 
      else 
      { return be; }
    } 

    return mexpr; 
  } 


  public void getParameterBounds(Vector pars, Vector bounds, java.util.Map attBounds)
  { } 

  public static void identifyUpperBounds(Vector atts, java.util.Map boundConstraints, java.util.Map upperBounds)
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String aname = att.getName(); 
      Vector bpreds = (Vector) boundConstraints.get(aname); 
      if (bpreds != null) 
      { for (int j = 0; j < bpreds.size(); j++) 
        { BinaryExpression pred = (BinaryExpression) bpreds.get(j); 
          Expression ubound = pred.getUpperBound(aname); 
          if (ubound != null)
          { upperBounds.put(aname,ubound); }
         }
       } 
     }
  } 
  
  public static void identifyLowerBounds(Vector atts, java.util.Map boundConstraints, java.util.Map lowerBounds)
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String aname = att.getName(); 
      Vector bpreds = (Vector) boundConstraints.get(aname); 
      if (bpreds != null) 
      { for (int j = 0; j < bpreds.size(); j++) 
        { BinaryExpression pred = (BinaryExpression) bpreds.get(j); 
          Expression lbound = pred.getLowerBound(aname); 
          if (lbound != null)
          { lowerBounds.put(aname,lbound); }
        }
	 } 
     }
  } 

  public static Vector parametersFromTypeMap(java.util.Map vartypes)
  { Vector pars = new Vector(); 
    java.util.Set keys = vartypes.keySet(); 
    Vector keyvect = new Vector(); 
    keyvect.addAll(keys); 
    for (int i = 0; i < keyvect.size(); i++) 
    { String key = (String) keyvect.get(i); 
      Type vtype = (Type) vartypes.get(key); 
      if (key != null && vtype != null) 
      { BasicExpression att = 
          BasicExpression.newVariableBasicExpression(
                                           key,vtype); 
        pars.add(att); 
      } // and avoid duplicates
    } 
    return pars; 
  } 

  public static Type deduceType(String opr, 
                                Expression e1, Expression e2)
  { Type t1 = e1.getType(); 
    Type t2 = e2.getType(); 
    Type res = new Type("OclAny", null); 

    if ("?".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; 
        if (Type.isDefinedType(t2)) { }
        else  
        { e2.setType(res); } 
      }
      else if (Type.isDefinedType(t2))
      { res = t2; 
        e1.setType(res); 
      } 
      return res; 
    } 

    if ("*".equals(opr) || "/".equals(opr) || 
        "->pow".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; 
        if (Type.isDefinedType(t2)) { } 
        else  
        { e2.setType(res); } 
      }
      else if (Type.isDefinedType(t2))
      { res = t2; 
        e1.setType(res); 
      } 
      else 
      { res = new Type("double", null); 
        e1.setType(res); 
        e2.setType(res); 
      } 
      return res; 
    } 

    if ("mod".equals(opr) || "div".equals(opr) || 
        "->gcd".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; 
        if (Type.isDefinedType(t2)) { } 
        else  
        { e2.setType(new Type("int", null)); } 
      }
      else if (Type.isDefinedType(t2)) // Should be int. 
      { res = t2; 
        e1.setType(res); 
      } 
      else 
      { res = new Type("int", null); 
        e1.setType(res); 
        e2.setType(res); 
      } 
      return res; 
    } 

    if ("or".equals(opr) || "xor".equals(opr) || 
        "&".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; 
        if (Type.isDefinedType(t2)) { } 
        else  
        { e2.setType(new Type("boolean", null)); } 
      }
      else if (Type.isDefinedType(t2)) // Should be boolean. 
      { res = t2; 
        e1.setType(res); 
      } 
      else 
      { res = new Type("boolean", null); 
        e1.setType(res); 
        e2.setType(res); 
      } 
      return res; 
    } 

    if ("bitwiseOr".equals(opr) || "bitwiseXor".equals(opr) || 
        "bitwiseAnd".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; 
        if (Type.isDefinedType(t2)) { } 
        else  
        { e2.setType(new Type("int", null)); } 
      }
      else if (Type.isDefinedType(t2)) // Should be int. 
      { res = t2; 
        e1.setType(res); 
      } 
      else 
      { res = new Type("int", null); 
        e1.setType(res); 
        e2.setType(res); 
      } 
      return res; 
    } 

  
    return res; 
  } 

  public static Type deduceType(String opr, 
                                Expression e1)
  { Type t1 = e1.getType(); 
    Type res = new Type("OclAny", null); 

    if ("+".equals(opr) || "-".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; } 
      else 
      { res = new Type("double", null); 
        e1.setType(res); 
      } 
      return res; 
    } 

    if ("->reverse".equals(opr))
    { if (Type.isDefinedType(t1)) 
      { res = t1; } 
      else 
      { res = new Type("Sequence", null); 
        e1.setType(res); 
      } 
      return res; 
    } 

    return res; 
  } 

  public static Expression convertToApply(Expression expr,
                                          Vector pars)
  { Expression res = expr; 

    if (pars.size() == 0)
    { res = new BinaryExpression("->apply", res, new BasicExpression("null")); 
      return res; 
    } 

    for (int i = 0; i < pars.size(); i++) 
    { Type elemType = res.getElementType(); 
      Expression par = (Expression) pars.get(i); 
      res = new BinaryExpression("->apply",res,par);
      res.type = elemType;  
    } 
    return res; 
  } 

  public static Expression convertToGenerator(Expression expr,
                                              Vector pars)
  { // OclIterator.newOclIterator_Function(
    //      lambda i : int in expr(pars^[i])) 
   
    BasicExpression pos =
        BasicExpression.newVariableBasicExpression(
                                         "_position_"); 
    pos.setType(new Type("int", null));
    Attribute acc = 
      new Attribute("_position_", new Type("int", null), 
                    ModelElement.INTERNAL); 
 
    Vector newpars = new Vector(); 
    newpars.addAll(pars); 
    newpars.add(pos); 

    Expression res = expr; 

    if (res instanceof BasicExpression) 
    { BasicExpression resbe = (BasicExpression) res; 
      if (resbe.getParameters() == null || 
          resbe.getParameters().size() == 0) 
      { resbe.setParameters(newpars); } 

      if (resbe.objectRef == null)
      { resbe.objectRef = 
          BasicExpression.newVariableBasicExpression("self"); 
      } 
    } 
    else 
    { for (int i = 0; i < newpars.size(); i++) 
      { Expression par = (Expression) newpars.get(i); 
        res = new BinaryExpression("->apply",res,par);  
      } 
    } 

    UnaryExpression lambda = 
      new UnaryExpression("lambda", res); 
    lambda.setAccumulator(acc); 
    Type ftype = new Type("Function", null);
    ftype.setKeyType(new Type("int", null)); 
    ftype.setElementType(new Type("OclAny", null)); 
    lambda.setType(ftype); 
    
    return 
      BasicExpression.newStaticCallBasicExpression(
        "newOclIterator_Function", "OclIterator", lambda); 
  } 

  public static Expression combineBySum(Vector atts)
  { Expression res = null; 
    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i); 
      BasicExpression be = new BasicExpression(att);
      be.setUmlKind(Expression.ATTRIBUTE);  
      res = concatenateStrings(res,be); 
    } 
    return res; 
  } 

  public static Expression disjoinCases(Attribute att, Vector values)
  { Expression res = null; 
    Vector seen = new Vector(); 

    for (int i = 0; i < values.size(); i++)
    { Object val = values.get(i);
      if (seen.contains(val))
      { continue; } 
 
      BasicExpression be = new BasicExpression(val + "");
      be.setUmlKind(Expression.VALUE); 
      be.setType(att.getType()); 
      BasicExpression attbe = new BasicExpression(att); 
      attbe.setUmlKind(Expression.ATTRIBUTE);
      BinaryExpression eq = new BinaryExpression("=", attbe, be);   

      if (res == null) 
      { res = eq; } 
      else
      { res = new BinaryExpression("or", res, eq); }
      res.setType(new Type("boolean",null));
 
      seen.add(val);  
    } 
    return res; 
  } 

  public static Expression concatenateStrings(Expression lhs, Expression rhs)
  { if (lhs == null) 
    { return rhs; } 
    Expression expr = new BinaryExpression("+", lhs, 
      new BasicExpression("\" ~ \"")); 
    Expression expr1 = new BinaryExpression("+", expr, rhs); 
    expr1.setType(new Type("String", null)); 
    expr1.setElementType(new Type("String", null)); 
    return expr1; 
  } 

  public static Expression classMembershipPredicate(Attribute att, Vector classNames)
  { if (classNames.size() == 0) 
    { return new BasicExpression(false); } 

    String c1 = (String) classNames.get(0); 
    BinaryExpression possCond = 
			    new BinaryExpression("->oclIsTypeOf", new BasicExpression(att), new BasicExpression(c1));
   
    for (int i = 1; i < classNames.size(); i++) 
    { String ci = (String) classNames.get(i); 
      BinaryExpression condi = 
			    new BinaryExpression("->oclIsTypeOf", new BasicExpression(att), new BasicExpression(ci));
      possCond = new BinaryExpression("or", possCond, condi); 
    } 
    possCond.setBrackets(true); 
    possCond.setType(new Type("boolean",null)); 
    return possCond; 
  } 
 
			   
			  

  public static boolean isSimpleEntity(Expression ee) 
  { if (ee == null) 
    { return false; } 

    if (ee.umlkind == Expression.CLASSID && (ee instanceof BasicExpression) &&
        ((BasicExpression) ee).arrayIndex == null)
    { return true; } 
    return false; 
  }  

  public static boolean isEntity(Expression ee) 
  { if (ee == null) 
    { return false; } 

    Type te = ee.getElementType(); 
    if (Type.isEntityType(te) && (te + "").equals(ee + ""))
    { return true; } 
    
    return false; 
  } 

  public boolean isEntity()
  { if (Type.isEntityType(type))
    { return true; } 
    
    return false; 
  } 

  public boolean isBooleanValued()
  { if (type == null) 
    { return false; } 
    String nme = type.getName(); 
    if (nme.equals("boolean"))
    { return true; } 
    return false; 
  } 

  public boolean isBoolean()
  { if (type == null) 
    { return false; } 
    String nme = type.getName(); 
    if (nme.equals("boolean"))
    { return true; } 
    return false; 
  } 

  public static void addOperator(String op, Vector types,
                                 String typ) 
  { Type t = (Type) ModelElement.lookupByName(typ,types); 
    if (t != null) 
    { extensionoperators.put(op,t); } 
  } 

  public static void addOperator(String op, 
                                 Type typ) 
  { extensionoperators.put(op,typ); } 

  public static void addOperatorJava(String op, String jcode)
  { extensionopjava.put(op,jcode); } 

  public static void addOperatorCSharp(String op, String cscode)
  { extensionopcsharp.put(op,cscode); } 

  public static void addOperatorCPP(String op, String cppcode)
  { extensionopcpp.put(op,cppcode); } 

  public static Type getOperatorType(String op) 
  { return (Type) extensionoperators.get(op); } 

  public static String getOperatorJava(String op) 
  { return (String) extensionopjava.get(op); } 

  public static String getOperatorCSharp(String op) 
  { return (String) extensionopcsharp.get(op); } 

  public static String getOperatorCPP(String op) 
  { return (String) extensionopcpp.get(op); } 

  public static void saveOperators(PrintWriter out)
  { java.util.Iterator keys = extensionoperators.keySet().iterator();
    while (keys.hasNext())
    { Object k = keys.next();
      String opname = (String) k;  
      String typ = extensionoperators.get(k) + ""; 
      out.println("Operator:"); 
      out.println(opname + " " + typ); 

      String opjava = (String) extensionopjava.get(k);
      if (opjava != null) 
      { out.println(opjava); }
      else 
      { out.println(); }   

      String opcsharp = (String) extensionopcsharp.get(k);
      if (opcsharp != null) 
      { out.println(opcsharp); }
      else 
      { out.println(); }
   
      String opcpp = (String) extensionopcpp.get(k);
      if (opcpp != null) 
      { out.println(opcpp); }
      else 
      { out.println(); }   

      out.println();
      out.println(); 
      out.println(); 
    } 
  } 

  public Expression etlEquivalent(Attribute trg, Vector ems) 
  { if (type == null) 
    { return this; } 
    if (trg == null) 
    { return this; } 

    Entity sent = type.getEntity(); 
    if (sent == null && elementType != null) 
    { sent = elementType.getEntity(); } 

    if (trg.getType() != null && trg.getType().isEntityType())
    { Entity tent = trg.getType().getEntity(); 
      EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
      if (em != null) 
      { return new BasicExpression("(" + this + ").equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
      }  
      return new BasicExpression("(" + this + ").equivalent()"); 
    } 
    else if (Type.isEntityCollection(trg.getType()))
    { Entity tent = trg.getElementType().getEntity(); 
      EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
      if (em != null) 
      { return new BasicExpression("(" + this + ").equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
      }
      return new BasicExpression("(" + this + ").equivalent()"); 
    } 
    else 
    { return this; }
  } 


  public abstract Expression definedness(); 

  public abstract Expression determinate(); 

  public abstract Expression removePrestate(); 


  public static Statement iterationLoop(Expression var, Expression range, Statement body)
  { BinaryExpression test = new BinaryExpression(":", var, range);
    WhileStatement ws = new WhileStatement(test, body);
    ws.setLoopKind(Statement.FOR);
    ws.setLoopRange(var,range);
    return ws;
  }

  public String findEntityVariable(java.util.Map env)
  { if (entity == null)
    { return ""; } 
    String nme = entity + ""; 
    String var = (String) env.get(nme);
    if (var == null)  // because nme is superclass of ent: dom(env)
    { var = entity.searchForSubclassJava(env);  
      if (var == null)
      { var = entity.searchForSuperclassJava(env); 
        if (var != null) 
        { var = "((" + nme + ") " + var + ")"; }  
        else 
        { System.err.println("** Specification error: no variable of " + entity); 
          var = nme.toLowerCase() + "x"; 
        } 
      }
      return var; 
    }
    return var; 
  } 
 
  static Vector caselist(Expression e)
  { if (e == null) { return new Vector(); }

    if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e;
      if ("&".equals(be.operator))
      { Vector v1 = caselist(be.left);
        Vector v2 = caselist(be.right);
        Vector res = new Vector();
        res.addAll(v1); res.addAll(v2);
        return res;
      }
    }
    Vector v = new Vector();
    v.add(e);
    return v;
  }

  public String saveModelData(PrintWriter out) { return "_"; } 

  public String unwrap(String se)
  { if (type == null)
    { return se; }  // should never happen
    String tname = type.getName();
    if (tname.equals("double"))
    { return "((Double) " + se + ").doubleValue()"; } 
    if (tname.equals("boolean"))
    { return "((Boolean) " + se + ").booleanValue()"; } 
    if (tname.equals("int"))
    { return "((Integer) " + se + ").intValue()"; }
    if (tname.equals("long"))
    { return "((Long) " + se + ").longValue()"; }
    if (type.isEnumerated())
    { return "((Integer) " + se + ").intValue()"; } // for enumerated
    return se;   // can't unwrap -- for strings or objects
  }

  public static String unwrap(String se, Type typ)
  { if (typ == null)
    { return se; }  // should never happen
    String tname = typ.getName();
    if (tname.equals("double"))
    { return "((Double) " + se + ").doubleValue()"; } 
    if (tname.equals("boolean"))
    { return "((Boolean) " + se + ").booleanValue()"; } 
    if (tname.equals("int"))
    { return "((Integer) " + se + ").intValue()"; }
    if (tname.equals("long"))
    { return "((Long) " + se + ").longValue()"; }
    if (typ.isEnumerated())
    { return "((Integer) " + se + ").intValue()"; } // for enumerated
    if (tname.equals("Set") || tname.equals("Sequence"))
    { return se; }  // "(Vector) " + se
    if (tname.equals("OclAny"))
    { return se; } 

    return "(" + tname + ") " + se;   // for strings or objects
  }

  public String unwrapJava6(String se)
  { return Expression.unwrapJava6(se,type); } 

  public static String unwrapJava6(String se, Type typ)
  { if (typ == null)
    { return se; }  // should never happen
    String tname = typ.getName();
    if (tname.equals("double"))
    { return "((Double) " + se + ").doubleValue()"; } 
    if (tname.equals("boolean"))
    { return "((Boolean) " + se + ").booleanValue()"; } 
    if (tname.equals("int"))
    { return "((Integer) " + se + ").intValue()"; }
    if (tname.equals("long"))
    { return "((Long) " + se + ").longValue()"; }
    if (typ.isEnumerated())
    { return "((" + typ.getName() + ") " + se + ")"; } 

    if (tname.equals("Set") || tname.equals("Sequence"))
    { return se; }  // "(Vector) " + se
    if (tname.equals("OclAny"))
    { return se; } 

    return "(" + tname + ") " + se;   // for strings or objects
  }

  public String unwrapJava7(String se)
  { return se; } 

  public static String convertCSharp(String se, Type t)
  { if (t == null)
    { return se; }  // should never happen
    String tname = t.getName();
    if (tname.equals("double"))
    { return "double.Parse(" + se + ")"; } 
    if (tname.equals("boolean"))
    { return "bool.Parse(" + se + ")"; } 
    if (tname.equals("int"))
    { return "int.Parse(" + se + ")"; }
    if (tname.equals("long"))
    { return "Int64.convert(" + se + ")"; }
    if (t.isEnumerated())
    { return "int.Parse(" + se + ")"; } // for enumerated
    return se;   // can't unwrap -- for strings or objects
  }



  public String unwrapCSharp(String se)
  { if (type == null)
    { return se; }  // should never happen
    String tname = type.getName();
    if (tname.equals("double"))
    { return "((double) " + se + ")"; } 
    if (tname.equals("boolean"))
    { return "((bool) " + se + ")"; } 
    if (tname.equals("int"))
    { return "((int) " + se + ")"; }
    if (tname.equals("long"))
    { return "((long) " + se + ")"; }
    if (type.isEnumerated())
    { return "((int) " + se + ")"; } // for enumerated
    return se;   // can't unwrap -- for strings or objects
  }

  public static String unwrapSwift(String se, Type t)
  { if (t == null)
    { return se; }  // should never happen
    String tname = t.getName();
    if (tname.equals("double"))
    { return "Double(" + se + ")"; } 
    if (tname.equals("boolean"))
    { return "Bool(" + se + ")"; } 
    if (tname.equals("int"))
    { return "Int(" + se + ")"; }
    if (tname.equals("long"))
    { return "Int(" + se + ")"; }
    if (t.isEnumerated())
    { return tname + "(rawValue: " + se + ")"; } 
    if (t.isEntity())
    { return tname + ".getByPK" + tname + "(index: " + se + ")"; } 

    return se;   // no unwrap -- for strings, OclAny
  }

  public static String unwrapSwiftOptional(String se, Type t)
  { if (t == null)
    { return se; }  // should never happen
    String tname = t.getName();
    if (tname.equals("double"))
    { return "Double(" + se + "!)"; } 
    if (tname.equals("boolean"))
    { return "Bool(" + se + "!)"; } 
    if (tname.equals("int"))
    { return "Int(" + se + "!)"; }
    if (tname.equals("long"))
    { return "Int(" + se + "!)"; }
    if (t.isEnumerated())
    { return tname + "(rawValue: " + se + "!)"; } 
    if (t.isEntity())
    { return tname + ".getByPK" + tname + "(index: " + se + "!)"; } 

    return se;   // no unwrap -- for strings
  }

  public abstract Expression skolemize(Expression sourceVar, java.util.Map env); 

  public Vector topLevelSplit(String d)
  { // divides d into substrings using . at top level only
    int dlen = d.length();
    Vector res = new Vector();
    StringBuffer sb = new StringBuffer();
    int bcount = 0;
    int sqbcount = 0;
    int scount = 0;
    boolean instring = false;

    char prev = ' '; 

    for (int i = 0; i < dlen; i++)
    { char c = d.charAt(i);
      if (c == '.')
      { if (bcount == 0 && sqbcount == 0 && scount == 0 &&
            !instring)
        { res.add(sb.toString());
          sb = new StringBuffer();
        }
        else 
        { sb.append(c); } 
      }
      else if (c == '(') { bcount++; sb.append(c); }
      else if (c == ')') { bcount--; sb.append(c); }
      else if (c == '[') { sqbcount++; sb.append(c); }
      else if (c == ']') { sqbcount--; sb.append(c); }
      else if (c == '{') { scount++; sb.append(c); }
      else if (c == '}') { scount--; sb.append(c); }
      else if (c == '"' && prev != '\\') 
      { if (instring) { instring = false; } else { instring = true; }
        sb.append(c); 
      }
      else { sb.append(c); }
      prev = c; 
    }
    res.add(sb.toString());
    return res;
  }

  public static Expression removeExistentials(Expression e, Vector vscopes, 
                                              Vector vars)
  { if (e instanceof BinaryExpression)
    { BinaryExpression eb = (BinaryExpression) e; 
      if (eb.operator.equals("#") || eb.operator.equals("#1"))
      { BinaryExpression sleft = (BinaryExpression) eb.left; 
        BasicExpression var = (BasicExpression) sleft.left;
        vars.add(var + "");  
        vscopes.add(sleft); 
        Expression entB = eb.right;
        return removeExistentials(entB,vscopes,vars); 
      } 
      else if (eb.operator.equals("&")) // E1->exists( e1 | expr1 & E2->exists( e2 | expr2 ))
      { Expression res = removeExistentials(eb.right, vscopes, vars); 
        return simplifyAnd(eb.left, res); 
      } 
      return e; 
    } 
    
    return e; 
  } 

  public static Expression conjoin(Vector exps)
  { if (exps.size() == 0) { return new BasicExpression(true); } 
    if (exps.size() == 1) { return (Expression) exps.get(0); } 
    Vector tail = new Vector(); 
    Expression head = (Expression) exps.get(0);
    tail.addAll(exps); 
    tail.remove(head);  
    return simplifyAnd(head,conjoin(tail)); 
  } 
        
  public String updateForm(String language, java.util.Map env, boolean local)
  { if ("Java4".equals(language))
    { return updateForm(env,local); } 
    else if ("Java6".equals(language))
    { return updateFormJava6(env,local); } 
    else if ("Java7".equals(language))
    { return updateFormJava7(env,local); } 
    else if ("CSharp".equals(language))
    { return updateFormCSharp(env,local); } 
    else 
    { return updateFormCPP(env,local); } 
  } 

  public String updateForm(String language, java.util.Map env, 
                                    String op, String val, Expression var, boolean local)
  { return ""; }  

  public abstract void findClones(java.util.Map clones, String rule, String op); 

  public abstract void findClones(java.util.Map clones, 
                                  java.util.Map cloneDefs,
                                  String rule, String op); 

  public abstract void findMagicNumbers(java.util.Map mgns, String rule, String op); 

  public abstract String updateForm(java.util.Map env, boolean local); 

  public Statement generateDesign(java.util.Map env, boolean local)
  { return new SequenceStatement(); } 

  public Statement statLC(java.util.Map env, boolean local)
  { return generateDesign(env,local); } 

  public boolean isExecutable()
  { return false; } 

  public boolean isTestable()
  { return true; } 

  public String updateFormCSharp(java.util.Map env, boolean local)
  { return updateForm(env,local); }  

  public String updateFormJava6(java.util.Map env, boolean local)
  { return updateForm(env,local); }  

  public String updateFormJava7(java.util.Map env, boolean local)
  { return updateForm(env,local); }  

  public String updateFormCPP(java.util.Map env, boolean local)
  { return updateForm(env,local); }  

  public static String javaOpOf(String op)
  { if (op.equals("&") || op.equals("and")) { return "&&"; }
    if (op.equals("or")) { return "||"; }
    if (op.equals("=")) { return "=="; }
    if (op.equals("/=")) { return "!="; }
    if (op.equals("mod")) { return "%"; }
    if (op.equals("div")) { return "/"; }
    return op;
  }

  public Vector writeFrame()
  { return new Vector(); }   // default

  public Scope resultScope()
  { return null; }

  public String updatedData()
  { return null; } // default

  public boolean isUpdateable() { return false; }   // default
 
  public static boolean toBoolean(String s)
  { if (s == null) { return false; }
    if (s.equals("true")) { return true; }
    return false; 
  } 

  public boolean isOrdered()
  { return false; } // default

  public boolean isSorted()
  { return isSorted; } // default

  public void setSorted(boolean s)
  { isSorted = s; } 

  public boolean isOrderedB()
  { return false; } // default

  public abstract Vector allEntitiesUsedIn(); 

  public abstract Vector innermostEntities(); // cases of SetExpression etc needed

  public abstract Vector innermostVariables(); 
  // cases of SetExpression etc needed

  public abstract Vector allPreTerms();

  public abstract Vector metavariables();

  public abstract Vector allPreTerms(String var);

  public abstract Expression addPreForms(String var); 

  public Vector getBaseEntityUses()
  { return new Vector(); } // default
 

  public abstract Vector getVariableUses(); 

  public Vector allFeatureUses(Vector features)
  { Vector res = new Vector(); 
    for (int i = 0; i < features.size(); i++) 
    { res.addAll(getUses((String) features.get(i))); } 
    return res; 
  } 

  public abstract Vector getUses(String feature); 

  public abstract Vector allFeaturesUsedIn(); 

  public abstract Vector allAttributesUsedIn();
  
  public abstract Vector allOperationsUsedIn(); 

  public boolean isSelfCall(BehaviouralFeature bf) 
  { return false; } 

  public boolean isSelfCall(String nme) 
  { return false; } 

  public abstract Vector equivalentsUsedIn(); 

  public abstract Vector allValuesUsedIn(); 

  public abstract Vector allBinarySubexpressions(); 

  public Vector updateVariables() { return new Vector(); } 

  public Vector allConjuncts()
  { return new Vector(); }

  public Vector allDisjuncts()
  { Vector res = new Vector();
    res.add(this); 
    return res; 
  }

  public Vector getConjuncts()
  { Vector res = new Vector();
    res.add(this); 
    return res; 
  }

  public static Expression formDisjunction(Vector exprs) 
  { Expression res = new BasicExpression(false); 
    for (int i = 0; i < exprs.size(); i++) 
    { Expression expr = (Expression) exprs.get(i); 
      res = simplifyOr(res,expr); 
    } 
    res.setType(new Type("boolean", null)); 
    return res; 
  } 

  public static Expression formConjunction(Vector exprs) 
  { Expression res = new BasicExpression(true); 
    for (int i = 0; i < exprs.size(); i++) 
    { Expression expr = (Expression) exprs.get(i); 
      res = simplifyAnd(res,expr); 
    } 
    res.setType(new Type("boolean", null)); 
    return res; 
  } 


  public Expression preconditionExpression()  // for Java
  { Vector conjs = computeNegation4ante();
    Expression disj;
    if (conjs.size() == 0)
    { return new BasicExpression(false); }  // ???
    disj = (Expression) conjs.get(0);
    for (int i = 1; i < conjs.size(); i++)
    { Expression c = (Expression) conjs.get(i);
      disj = Expression.simplify("or",disj,c,false);
    }
    return disj;
  }

  public abstract boolean relevantOccurrence(String op, 
                                             Entity ent, String val, String f); 

  public abstract DataDependency getDataItems(); 

  public abstract DataDependency rhsDataDependency(); 

  public abstract Expression addReference(BasicExpression ref, Type t);

  public abstract Expression 
     addContainerReference(BasicExpression ref, String var,
                           Vector exclusions);

  public abstract Expression replaceReference(BasicExpression ref, Type t);

  public abstract Expression dereference(BasicExpression ref);

  public abstract Expression invert(); 

  public static Expression convertToString(Expression expr)
  { if (expr == null) 
    { return new BasicExpression("null"); } 
    if (expr.isString())
    { return expr; } 
    Expression res = new BasicExpression("\"" + expr + "\"");
    res.setType(new Type("String", null)); 
    res.setElementType(new Type("String", null));
    return res;  
  } 

  public static String cppStringOf(String expr, Type t)
  { if (t == null) 
    { return expr; } 
    String tname = t.getName(); 
    if (tname.equals("String"))
    { return expr; } 
    if (t.isNumeric() || t.isEnumeration() || t.isBoolean())
    { return "std::to_string(" + expr + ")"; } 
    if (t.isCollection())
    { Type elemT = t.getElementType(); 
      if (elemT != null) 
	  { String cet = elemT.getCPP(); 
        return "UmlRsdsLib<" + cet + ">::collectionToString(" + expr + ")";
	  } 
	  else 
	  { return "UmlRsdsLib<void*>::collectionToString(" + expr + ")"; } 
    } 
    return "(" + expr + ")->toString()";
  } 

  public String wrap(String qf)
  { return wrap(type,qf); }

  public static String wrap(Type t, String qf)
  { if (t != null)
    { if (t.isEntity()) 
      { return qf; } 
      String tname = t.getName();
      if (tname.equals("Set") || tname.equals("Sequence") ||
          tname.equals("String") || tname.equals("Map") ||
          tname.equals("Function") || 
          tname.equals("OclType") ||
          tname.equals("OclDate") || 
          tname.equals("OclFile") || 
          tname.equals("OclIterator") || 
          tname.equals("OclRandom") || 
          tname.equals("OclDatasource") || 
          tname.equals("OclProcess") || 
          tname.equals("OclAny"))
      { return qf; }
      if (tname.equals("boolean"))
      { return "new Boolean(" + qf + ")"; }
      else if (tname.equals("double"))
      { return "new Double(" + qf + ")"; }
      else if (tname.equals("long"))
      { return "new Long(" + qf + ")"; }
      else // int or enumeration
      { return "new Integer(" + qf + ")"; }
    }
    else // assume already an object
    { return qf; }
  }

  public String wrapJava6(String qf)
  { return Expression.wrapJava6(type, qf); } 

  public static String wrapJava6(Type t, String qf)
  { if (t != null)
    { if (t.isEntity()) 
      { return qf; } 
      String tname = t.getName();
      if (tname.equals("Set") || tname.equals("Sequence") ||
          tname.equals("String") || tname.equals("Map") ||
          tname.equals("Function") || 
          tname.equals("OclType") ||
          tname.equals("OclDate") || 
          tname.equals("OclFile") || 
          tname.equals("OclIterator") || 
          tname.equals("OclRandom") || 
          tname.equals("OclDatasource") || 
          tname.equals("OclProcess") || 
          tname.equals("OclAny"))
      { return qf; }
      if (tname.equals("boolean"))
      { return "new Boolean(" + qf + ")"; }
      else if (tname.equals("double"))
      { return "new Double(" + qf + ")"; }
      else if (tname.equals("long"))
      { return "new Long(" + qf + ")"; }
      else if (t.isEnumeration())
      { return qf; }
      else // int 
      { return "new Integer(" + qf + ")"; }
    }
    else // assume already an object
    { return qf; }
  }

  public String wrapCSharp(String qf)
  { return wrapCSharp(type,qf); }

  public static String wrapCSharp(Type t, String qf)
  { if (t != null)
    { if (t.isEntity()) { return qf; } 
      String tname = t.getName();
      if (tname.equals("Set") || tname.equals("Sequence") || 
          tname.equals("String") || tname.equals("Map") ||
          tname.equals("Function") || 
          tname.equals("OclType") || 
          tname.equals("OclFile") || 
          tname.equals("OclIterator") || 
          tname.equals("OclDate") ||
          tname.equals("OclRandom") || 
          tname.equals("OclDatasource") || 
          tname.equals("OclProcess") || 
          tname.equals("OclAny"))
      { return qf; }
      if (tname.equals("boolean"))
      { return "((bool) " + qf + ")"; }
      else if (tname.equals("double"))
      { return "((double) " + qf + ")"; }
      else if (tname.equals("long"))
      { return "((long) " + qf + ")"; }
      else
      { return "((int) " + qf + ")"; }
    }
    else // already an object
    { return qf; }
  }

  public String makeSet(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 

    return "(new Set()).add(" + obj + ").getElements()";
  }

  public String makeSetJava6(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 

    return "Set.addSet(new HashSet(), " + obj + ")";
  }

  public String makeSequenceJava6(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 

    return "Set.addSequence(new ArrayList(), " + obj + ")";
  }  // I assume? 

  public String makeSetJava7(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 
    Type seqtype = new Type("Set",null); 
    seqtype.setElementType(type); 
    String jtype = seqtype.getJava7(type); 

    return "Ocl.addSet(new " + jtype + "(), " + obj + ")";
  }

  public String makeSortedSetJava7(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 
    Type seqtype = new Type("Set",null); 
    seqtype.setElementType(type);
    seqtype.setSorted(true);  
    String jtype = seqtype.getJava7(type); 

    return "Ocl.addSet(new " + jtype + "(), " + obj + ")";
  }

  public String makeSequenceJava7(String qf)
  { String obj = qf; 
    // if (isPrimitive())
    // { obj = wrap(qf); } 
    // else 
    // { obj = qf; } 
    Type seqtype = new Type("Sequence",null); 
    seqtype.setElementType(type); 
    String jtype = seqtype.getJava7(type); 

    return "Ocl.addSequence(new " + jtype + "(), " + obj + ")";
  }  // I assume? 

  public String makeSetCSharp(String qf)
  { String obj = qf; 

    return "SystemTypes.makeSet(" + obj + ")";
  }

  public String makeSetCPP(String qf)
  { String obj = qf; 

    String cet = "void*"; 
    if (elementType != null) 
    { cet = elementType.getCPP(); } 
    
    return "UmlRsdsLib<" + cet + ">::makeSet(" + obj + ")";
  }

  public String makeSequenceCPP(String qf)
  { String obj = qf; 

    String cet = "void*"; 
    if (elementType != null) 
    { cet = elementType.getCPP(); } 
    
    return "UmlRsdsLib<" + cet + ">::makeSequence(" + obj + ")";
  }

  public abstract boolean isPrimitive(); 

  public boolean isAssignable()
  { return false; } // default

  public Expression checkConversions(Type typ, Type elemTyp, java.util.Map interp) 
  { return this; }   // default 

  public Expression replaceModuleReferences(UseCase uc) 
  { return this; }   // default 


  public abstract int typeCheck(final Vector sms);

  public boolean typeCheck(final Vector typs, final Vector ents,
                                    final Vector env)
  { Vector contexts = new Vector(); 
    if (entity != null)
    { contexts.add(entity); }  // For features, only 
    return typeCheck(typs,ents,contexts,env); 
  }  
     

  public abstract boolean typeCheck(final Vector typs, final Vector ents,
                                    final Vector contexts, final Vector env); 

  public abstract boolean typeInference(final Vector typs, 
                                        final Vector ents,
                   final Vector contexts, final Vector env, 
                   java.util.Map vartypes); 

  public abstract int maxModality();

  public abstract int minModality(); 

  public boolean isNumeric()
  { if (type != null) 
    { return type.getName().equals("int") || 
        type.getName().equals("double") || 
        type.getName().equals("long"); 
    } 
    return false; 
  }  

  public boolean isInteger()
  { if (type != null) 
    { return type.getName().equals("int") || 
        type.getName().equals("long"); 
    } 
    return false; 
  }  

  public boolean isInt()
  { if (type != null) 
    { return type.getName().equals("int"); }
 
    return false; 
  }  

  public static boolean isNumber(Object ob) 
  { try
    { double nn = Double.parseDouble("" + ob); 
      return true; 
    } 
    catch (Exception e) 
    { try 
      { long ll = Long.parseLong("" + ob); 
        return true; 
      } 
      catch (Exception ee)
      { return false; }
    }  
  } 

  public static double convertNumber(Object ob) 
  { try
    { double nn = Double.parseDouble("" + ob); 
      return nn; 
    } 
    catch (Exception e) 
    { try 
      { long ll = Long.parseLong("" + ob); 
        return ll; 
      } 
      catch (Exception ee)
      { return 0; }
    }  
  } 

  public static String removeUnderscores(String ss)
  { String res = ""; 
    for (int i = 0; i < ss.length(); i++) 
    { if ('_' == ss.charAt(i)) { } 
      else 
      { res = res + ss.charAt(i); } 
    } 
    return res; 
  } 

  public static boolean isInteger(Object ob) 
  { if (ob == null) 
    { return false; } 

    if ((ob + "").startsWith("_"))
    { return false; } 

    String cleanValue = removeUnderscores(ob + ""); 

    if (cleanValue.startsWith("0b"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Integer bx = Integer.parseInt(cleanValue,2); 
        if (bx == null) 
        { return false; } 
        int bn = bx.intValue(); 
        return true; 
      } 
      catch (Exception e) 
      { return false; } 
    } 

    if (cleanValue.startsWith("0o"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Integer bx = Integer.parseInt(cleanValue,8); 
        if (bx == null) 
        { return false; } 
        int bn = bx.intValue(); 
        return true; 
      } 
      catch (Exception e) 
      { return false; } 
    } 

    try
    { Integer intx = Integer.decode(cleanValue); 
      if (intx == null) 
      { return false; } 
      int nn = intx.intValue(); 
      return true; 
    } 
    catch (Exception e) 
    { return false; } 
  } 

  public static boolean isDecimalInteger(String val)
  { try
    { Integer intx = Integer.decode(val); 
      if (intx == null) 
      { return false; } 
      int nn = intx.intValue(); 
      return true; 
    } 
    catch (Exception e) 
    { return false; } 
  } 


  public static boolean isLong(Object ob) 
  { if (ob == null) 
    { return false; } 

    if ((ob + "").startsWith("_"))
    { return false; } 

    String cleanValue = removeUnderscores(ob + ""); 

    if (cleanValue.startsWith("0b"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Long bx = Long.parseLong(cleanValue,2); 
        if (bx == null) 
        { return false; } 
        long bn = bx.longValue(); 
        return true; 
      } 
      catch (Exception e) 
      { return false; } 
    } 

    try
    { Long longx = Long.decode(cleanValue); 
      if (longx == null) 
      { return false; } 
      long nn = longx.longValue(); 
      return true; 
    } 
    catch (Exception e) 
    { if (cleanValue.endsWith("L") || 
          cleanValue.endsWith("l"))
      { String sfront = 
          cleanValue.substring(0,cleanValue.length()-1);
        // System.out.println(sfront);  
        try
        { Long lx = Long.decode(sfront); 
          long mm = lx.longValue(); 
          // System.out.println(mm); 
          return true; 
        } catch (Exception _e) { return false; }
      } 
      return false; 
    }  
  } 

  public static int convertInteger(Object ob) 
  { if (ob == null) 
    { return 0; } 

    if ((ob + "").startsWith("_"))
    { return 0; } 

    String cleanValue = removeUnderscores(ob + ""); 

    if (cleanValue.startsWith("0b"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Integer bx = Integer.parseInt(cleanValue,2); 
        if (bx == null) 
        { return 0; } 
        int bn = bx.intValue(); 
        return bn; 
      } 
      catch (Exception e) 
      { return 0; } 
    } 

    if (cleanValue.startsWith("0o"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Integer bx = Integer.parseInt(cleanValue,8); 
        if (bx == null) 
        { return 0; } 
        int bn = bx.intValue(); 
        return bn; 
      } 
      catch (Exception e) 
      { return 0; } 
    } 

    try
    { Integer intx = Integer.decode(cleanValue); 
      if (intx == null) 
      { return 0; } 
      int nn = intx.intValue(); 
      return nn; 
    } 
    catch (Exception e) 
    { return 0; } 
  } 

  public static long convertLong(Object ob) 
  { if (ob == null) 
    { return 0; } 

    if ((ob + "").startsWith("_"))
    { return 0; } 

    String cleanValue = removeUnderscores(ob + ""); 

    if (cleanValue.startsWith("0b"))
    { cleanValue = cleanValue.substring(2); 
      try 
      { Long bx = Long.parseLong(cleanValue,2); 
        if (bx == null) 
        { return 0; } 
        long bn = bx.longValue(); 
        return bn; 
      } 
      catch (Exception e) 
      { return 0; } 
    } 

    try
    { Long longx = Long.decode(cleanValue); 
      if (longx == null) 
      { return 0; } 
      long nn = longx.longValue(); 
      return nn; 
    } 
    catch (Exception e) 
    { String slong = cleanValue; 
      if (slong.endsWith("L") || slong.endsWith("l"))
      { String sfront = slong.substring(0,slong.length()-1);
        try
        { Long lx = Long.decode(sfront); 
          long mm = lx.longValue(); 
          return mm; 
        } catch (Exception _e) { return 0; }
      } 
      return 0; 
    }  
  } 

  public static boolean isDouble(Object ob) 
  { try
    { double nn = Double.parseDouble("" + ob); 
      return true; 
    } 
    catch (Exception e) 
    { return false; } 
  } 

  public static boolean isValue(Object ob)
  { if (isNumber(ob)) { return true; } 
    if (ob instanceof String) 
    { if (isString((String) ob)) { return true; } 
      if (isBoolean((String) ob)) { return true; }
    }  
    return false; 
  } // and values of enum types? 

  public boolean isValue()
  { return umlkind == VALUE; } 

  public static boolean isConstant(String s)
  { // it is all capitals
    if (s == null || s.length() == 0) { return false; } 
    return (s.toUpperCase().equals(s)); 
  } 

  public static boolean isString(String data)
  { int len = data.length();
    if (len > 1 &&
        data.charAt(0) == '\"' &&
        data.charAt(len-1) == '\"')
    { return true; }
    else if (len > 1 &&
        data.charAt(0) == '\'' &&
        data.charAt(len-1) == '\'')
    { return true; }
    return false;
  }
  
  public boolean hasBasicType()
  { return Type.isBasicType(type); } 

  public boolean isString()
  { return type != null && type.isString(); }

  public boolean isCollection()
  { return type != null && type.isCollection(); }

  public boolean isNumericCollection()
  { return type != null && type.isCollection() && 
           elementType != null && elementType.isNumeric(); 
  }


  public boolean isObject()
  { return type != null && type.isEntityType(); }


  public boolean isSetValued()
  { return type != null && type.isSet(); }

  public boolean isSequenceValued()
  { return type != null && type.isSequence(); }

  public boolean isSet()
  { return type != null && type.isSet(); }

  public boolean isSequence()
  { return type != null && type.isSequence(); }

  public boolean isStringSequence()
  { return type != null && type.isStringSequence(); } 

  public boolean isIntSequence()
  { return type != null && type.isIntSequence(); } 

  public boolean isLongSequence()
  { return type != null && type.isLongSequence(); } 

  public boolean hasSetType()
  { return type != null && type.isSet(); }

  public boolean hasSequenceType()
  { return type != null && type.isSequence(); }

  public boolean isClassEntityType()
  { return type != null && type.isClassEntityType(); }

  public boolean isFunctionType()
  { return type != null && type.isFunctionType(); }

  public boolean hasFunctionType()
  { return type != null && type.isFunctionType(); }

  public boolean isRef()
  { return type != null && type.isRef(); }

  public boolean isOclIterator()
  { return type != null && 
      "OclIterator".equals(type.getName());
  }

  public boolean isOclDate()
  { return type != null && 
      "OclDate".equals(type.getName());
  }

  public String getOclType()
  { 
    if (Expression.isBoolean("" + this))
    { return "Boolean"; }

    if ("null".equals("" + this) || 
        "self".equals("" + this) ||
        "super".equals("" + this))
    { return "Object"; }  
    
    if (type == null) 
    { return null; }
 
    if (Type.isIntegerType(type))
    { return "Integer"; } 
    if (type.isRealType())
    { return "Real"; } 
    if (type.isStringType())
    { return "String"; } 
    return null; 
  } 

  public static boolean isBoolean(String data)
  { return data.equals("true") || data.equals("false"); }

  public static boolean isSet(String data)
  { int len = data.length();
    // if (len > 1 && data.charAt(0) == '{' && 
    //     data.charAt(len-1) == '}')
    // { return true; }
    if (len > 4 && data.substring(0,4).equals("Set{") && 
        data.charAt(len-1) == '}')
    { return true; }
    return false;
  }

  // a more precise check, isSetValue, is needed. 

  public static boolean isSequence(String data)
  { int len = data.length();
    if (len > 9 && data.substring(0,9).equals("Sequence{") &&
        data.charAt(len-1) == '}')
    { return true; }
    return false;
  }

  // a more precise check, isSequenceValue, is needed. 

  public static boolean isMap(String data)
  { int len = data.length();
    if (data.startsWith("Map{") &&
        data.charAt(len-1) == '}')
    { return true; }
    return false;
  }

  // a more precise check, isMapValue, is needed. 

  public boolean isCall(String data)
  { int n = data.length();
    int i = data.indexOf('('); 
    if (n > 0 &&
        data.charAt(n-1) == ')' &&
        i > 0)
    { return true; }
    return false; 
  }


  public static boolean isFunction(String d)
  { if (d.equals("sqr") || d.equals("sqrt") || 
        d.equals("any") || d.equals("cbrt") || 
        d.equals("sin") || d.equals("cos") || 
        d.equals("tan") || d.equals("exp") ||
        d.equals("sinh") || d.equals("cosh") || 
        d.equals("tanh") || d.equals("log10") ||
        d.equals("log") || d.equals("floor") || 
        d.equals("round") || d.equals("ceil") || 
        d.equals("atan") || d.equals("acos") || 
        d.equals("asin") || d.equals("oclAsType") || 
        d.equals("abs") || d.equals("max") || 
        d.equals("subcollections") || d.equals("Prd") || 
        d.equals("size") || d.equals("toLowerCase") || 
        d.equals("pow") || d.equals("gcd") || d.equals("Sum") ||
        d.equals("replace") || d.equals("replaceAll") ||
        d.equals("replaceAllMatches") ||
        d.equals("replaceFirstMatch") || 
        // d.equals("toBoolean") || d.equals("toInteger") || 
        // d.equals("toReal") || d.equals("toLong") ||
        // d.equals("compareTo") ||  
        d.equals("toUpperCase") || d.equals("closure") || 
        d.equals("asSet") || d.equals("asSequence") ||
        d.equals("min") || d.equals("sum") || 
        d.equals("reverse") || d.equals("allInstances") || 
        d.equals("sort") || d.equals("prd") || 
        d.equals("last") || d.equals("insertAt") ||
        d.equals("insertInto") ||  
        d.equals("excludingSubrange") || 
        d.equals("setAt") ||
        d.equals("first") || d.equals("tail") || 
        d.equals("front") || d.equals("oclIsUndefined") || 
        d.equals("subrange") || d.equals("indexOf") || 
        d.equals("count") ||
        d.equals("characters") || d.equals("isDeleted") || 
        d.equals("oclIsKindOf") || d.equals("oclIsTypeOf"))
    { return true; }  
    return false;
  } // oclAsType should be BinaryExpression??? flatten??

  public static boolean isOclValue(Expression expr)
  { if ("self".equals(expr + ""))
    { return true; } 
    if ("super".equals(expr + ""))
    { return true; } 
    if ("null".equals(expr + ""))
    { return true; } 
    if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      return be.umlkind == VALUE; 
    } 
    return false; 
  } 

  public static boolean isOclIdentifier(Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.umlkind == VALUE || 
          be.data.equals("self") || 
          be.data.equals("super") || 
          be.data.equals("null")) 
      { return false; }  
      if (be.arrayIndex == null && be.objectRef == null && 
          be.getParameters() == null && 
          Compiler2.isSimpleIdentifier(be.data)) 
      { return true; }  
    } 
    return false; 
  } 

  public static boolean isOclFieldAccess(Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.umlkind == VALUE) 
      { return false; }  
      if (be.arrayIndex == null && be.objectRef != null && 
          be.getParameters() == null && 
          Compiler2.isSimpleIdentifier(be.data)) 
      { return true; }  
    } 
    return false; 
  } 

  public static boolean isOclOperationCall0(Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.umlkind == VALUE) 
      { return false; }  
      if (be.arrayIndex == null && 
          be.getParameters() != null && 
          be.getParameters().size() == 0) 
      { return true; }  
    } 
    return false; 
  } 

  public static boolean isOclOperationCallN(Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.umlkind == VALUE) 
      { return false; }  
      if (be.arrayIndex == null && 
          be.getParameters() != null && 
          be.getParameters().size() > 0) 
      { return true; }  
    } 
    return false; 
  } 

  public static boolean isOclArrayAccess(Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.umlkind == VALUE) 
      { return false; }  
      if (be.arrayIndex != null) 
      { return true; }  
    } 
    return false; 
  } 

  public static boolean isOclUnaryPrefix(Expression expr)
  { if (expr instanceof UnaryExpression)
    { UnaryExpression ue = (UnaryExpression) expr; 
      String op = ue.getOperator(); 
      if ("not".equals(op) || "-".equals(op))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isOclUnaryPostfix(Expression expr)
  { if (expr instanceof UnaryExpression)
    { UnaryExpression ue = (UnaryExpression) expr; 
      String op = ue.getOperator(); 
      if ("not".equals(op) || "-".equals(op) || 
          "lambda".equals(op))
      { return false; } 
      return true; 
    } 
    return false; 
  } 

  public static boolean isOclUnaryLambda(Expression expr)
  { if (expr instanceof UnaryExpression)
    { UnaryExpression ue = (UnaryExpression) expr; 
      String op = ue.getOperator(); 
      if ("lambda".equals(op))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isOclBinaryInfix(Expression expr)
  { if (expr instanceof BinaryExpression)
    { BinaryExpression ue = (BinaryExpression) expr; 
      String op = ue.getOperator(); 
      if ("+".equals(op) || "-".equals(op) || 
          "*".equals(op) || "&".equals(op) || 
          "^".equals(op) || "<>=".equals(op) || 
          "/".equals(op) || "div".equals(op) || 
          "mod".equals(op) || "and".equals(op) || 
          "or".equals(op) || "xor".equals(op) || 
          "<".equals(op) || "<=".equals(op) || 
          "=".equals(op) || "/=".equals(op) ||
          "=>".equals(op) || 
          ">".equals(op) || ">=".equals(op))
      { return true; } 
      if (op.startsWith("->"))
      { return false; } 
    } 
    return false; 
  } 

  public static boolean isOclIteratorOperator(String op)
  { if (op.startsWith("|") || op.startsWith("#") || 
          op.equals("!") )
    { return true; }
    
    if ("->collect".equals(op) || 
        "->isUnique".equals(op) || 
        "->reject".equals(op) || 
        "->select".equals(op) || 
        "->selectMaximals".equals(op) || 
        "->selectMinimals".equals(op) || 
        "->forAll".equals(op) || 
        "->exists".equals(op) || 
        "->exists1".equals(op)) 
    { return true; } 

    return false; 
  } 

  public static boolean isOclDistributedIteratorOperator(
                                                String op)
  { if ("->unionAll".equals(op) ||
        "->intersectAll".equals(op) || 
        "->concatenateAll".equals(op))
    { return true; } 

    if ("|unionAll".equals(op) ||
        "|intersectAll".equals(op) || 
        "|concatenateAll".equals(op))
    { return true; } 
    
    return false; 
  } 


  public static boolean isOclIndexOperation(String attr, 
                                            Expression expr)
  { if (expr instanceof BasicExpression)
    { BasicExpression bexpr = (BasicExpression) expr;
      Expression obj = bexpr.getObjectRef(); 

      if (attr.equals(obj + "")) { } 
      else 
      { return false; } 
 
      if (bexpr.data.equals("setAt") || 
          bexpr.data.equals("insertAt") ||
          bexpr.data.equals("insertInto") ||
          bexpr.data.equals("excludingSubrange") ||
          bexpr.data.equals("subrange")) 
      { return true; }
      return false; 
    }

    if (expr instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) expr; 
      String op = be.getOperator(); 

      if (attr.equals(be.getLeft() + "")) { } 
      else 
      { return false; } 
      
    
      if ("->at".equals(op) || 
          "->indexOf".equals(op) || 
          "->lastIndexOf".equals(op) || 
          "->excludingAt".equals(op) || 
          "->append".equals(op) || 
          "->prepend".equals(op)) 
       { return true; } 
       return false; 
     } 

     if (expr instanceof UnaryExpression)
     { UnaryExpression be = (UnaryExpression) expr; 
       String op = be.getOperator(); 

       if (attr.equals(be.getArgument() + "")) { } 
       else 
       { return false; } 
      
    
       if ("->first".equals(op) || 
           "->last".equals(op)) 
       { return true; } 
       return false;  
     }

     return false; 
   } 

  public static boolean isOclIteratorExpression(Expression expr)
  { if (expr instanceof BinaryExpression)
    { BinaryExpression ue = (BinaryExpression) expr; 
      String op = ue.getOperator(); 
      if (op.startsWith("|") || op.startsWith("#") || 
          op.equals("!") )
      { return true; } 
    } 
    return false; 
  } // They must have an iterator variable. 

  public static boolean isOclBinaryArrow(Expression expr)
  { if (expr instanceof BinaryExpression)
    { BinaryExpression ue = (BinaryExpression) expr; 
      String op = ue.getOperator(); 
      if (op.startsWith("->"))
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isEmptyCollectionExpression(Expression expr)
  { if (expr instanceof SetExpression)
    { SetExpression ue = (SetExpression) expr; 
      if (ue.isEmpty()) 
      { return true; } 
    } 
    return false; 
  } 

  public static boolean isNonEmptySetExpression(Expression expr)
  { if (expr instanceof SetExpression)
    { SetExpression ue = (SetExpression) expr; 
      if (ue.isEmpty()) 
      { return false; }
      if (ue.isOrdered())
      { return false; } 
      return true;  
    } 
    return false; 
  } 

  public static boolean isNonEmptySequenceExpression(Expression expr)
  { if (expr instanceof SetExpression)
    { SetExpression ue = (SetExpression) expr; 
      if (ue.isEmpty()) 
      { return false; }
      if (ue.isOrdered())
      { return true; }   
    } 
    return false; 
  } 


  public boolean isRuleCall(Vector rules)
  { return false; } 

  public abstract boolean isMultiple(); 

  public abstract boolean isOrExpression(); 

  public void setBrackets(boolean brack)
  { needsBracket = brack; } 

  public Expression completeness(Entity ent)
  { Expression res = new BasicExpression("true",0);
    return res;
  }

  public static String evaluateString(String op, String le, String re) 
  { if ("+".equals(op) || "-".equals(op) || "*".equals(op) || "/".equals(op))
    { int li; 
      int ri;
      long ll; 
      long rl;  
      double ld; 
      double rd; 
      try
      { li = Integer.parseInt(le); 
        try 
        { ri = Integer.parseInt(re); 
          return "" + evaluateInteger(op,li,ri);
        }
        catch (Exception e1) 
        { try 
          { rl = Long.parseLong(re); 
            return "" + evaluateLong(op,li,rl); 
          } 
          catch (Exception e2)
          { try 
            { rd = Double.parseDouble(re); 
              return "" + evaluateDouble(op,li,rd); 
            } 
            catch (Exception e3)
            { return le + " " + op + " " + re; } 
          } 
        }  
      } catch (Exception e) { } 
      
      try 
      { ll = Long.parseLong(le); 
        rl = Long.parseLong(re); 
        return "" + evaluateLong(op,ll,rl); 
      } 
      catch (Exception e4)
      { try 
        { rd = Double.parseDouble(re); 
          ld = Double.parseDouble(le); 
          return "" + evaluateDouble(op,ld,rd); 
        } 
        catch (Exception e5)
        { return le + " " + op + " " + re; } 
      }
    } 
    return le + " " + op + " " + re;
  }       

  public static int evaluateInteger(String op, int li, int ri)
  { if ("+".equals(op))
    { return li + ri; } 
    if ("-".equals(op))
    { return li - ri; } 
    if ("*".equals(op))
    { return li * ri; } 
    if ("/".equals(op) || "div".equals(op))
    { return li / ri; } 
    if ("mod".equals(op))
    { return li % ri; } 
    return 0; 
  } 

  public static long evaluateLong(String op, long li, long ri)
  { if ("+".equals(op))
    { return li + ri; } 
    if ("-".equals(op))
    { return li - ri; } 
    if ("*".equals(op))
    { return li * ri; } 
    if ("/".equals(op) || "div".equals(op))
    { return li / ri; } 
    if ("mod".equals(op))
    { return li % ri; } 
    return 0; 
  } 

  public static double evaluateDouble(String op, double li, double ri)
  { if ("+".equals(op))
    { return li + ri; } 
    if ("-".equals(op))
    { return li - ri; } 
    if ("*".equals(op))
    { return li * ri; } 
    if ("/".equals(op) || "div".equals(op))
    { return li / ri; } 
    return 0; 
  } 

  public String cstlConditionForm(java.util.Map srcvarmap)
  { return this + ""; }
  
  public String cstlQueryForm(java.util.Map srcvarmap)
  { return this + ""; }
  
  abstract public Expression createActionForm(final Vector v);

  public String classqueryForm(java.util.Map env, boolean local)
  { return queryForm(env,local); }   // default  

  public String classqueryFormJava6(java.util.Map env, boolean local)
  { return queryFormJava6(env,local); }   // default  

  public String classqueryFormJava7(java.util.Map env, boolean local)
  { return queryFormJava7(env,local); }   // default  

  public String classqueryFormCSharp(java.util.Map env, boolean local)
  { return queryFormCSharp(env,local); }   // default  

  public String classqueryFormCPP(java.util.Map env, boolean local)
  { return queryFormCPP(env,local); }   // default  

  abstract public String queryForm(java.util.Map env, boolean local); 

  public String declarationQueryForm(java.util.Map env, boolean local)
  { if (this instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) this; 
      if (":".equals(be.operator))
      { Expression typeexpr = be.right; 
        String typeqf = typeexpr.queryForm(env,local); 
        if (typeexpr.elementType != null) 
        { typeqf = typeexpr.elementType.getJava(); } 

        return typeqf + " " + be.left; 
      } 
    } 
    return queryForm(env,local); 
  } 

  public String declarationQueryFormCSharp(java.util.Map env, boolean local)
  { if (this instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) this; 
      if (":".equals(be.operator))
      { Expression typeexpr = be.right; 
        String typeqf = typeexpr.queryFormCSharp(env,local); 
        if (typeexpr.elementType != null) 
        { typeqf = typeexpr.elementType.getCSharp(); } 
        return typeqf + " " + be.left; 
      } 
    } 
    return queryFormCSharp(env,local); 
  } 

  public String declarationQueryFormCPP(java.util.Map env, boolean local)
  { if (this instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) this; 
      if (":".equals(be.operator))
      { Expression typeexpr = be.right; 
        String typeqf = typeexpr.queryFormCPP(env,local); 
        if (typeexpr.elementType != null) 
        { typeqf = typeexpr.elementType.getCPP(); } 
        return typeqf + " " + be.left; 
      } 
    } 
    return queryFormCPP(env,local); 
  } 

  public String throwQueryForm(java.util.Map env, boolean local)
  { if (this instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) this; 
      if (be.data.startsWith("new"))
      { String createdException = be.data.substring(3); 
        String jexception = (String) Type.exceptions2java.get(createdException); 
        if (jexception != null)
        { return "new " + jexception + "()"; }
        else 
        { return "new " + createdException + "()"; }  
      } 
    } 
    return queryForm(env,local); 
  } 

  public String throwQueryFormCSharp(java.util.Map env, boolean local)
  { if (this instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) this; 
      if (be.data.startsWith("new"))
      { String createdException = be.data.substring(3); 
        String jexception = (String) Type.exceptions2csharp.get(createdException); 
        if (jexception != null)
        { return "new " + jexception + "()"; }
        else 
        { return "new " + createdException + "()"; }  
      } 
    } 
    return queryFormCSharp(env,local); 
  } 

  public String throwQueryFormCPP(java.util.Map env, boolean local)
  { if (this instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) this; 
      if (be.data.startsWith("new"))
      { String createdException = be.data.substring(3); 
        String jexception = (String) Type.exceptions2cpp.get(createdException); 
        if (jexception != null)
        { return jexception + "()"; }
        else 
        { return createdException + "()"; }  
      } 
    } 
    return queryFormCPP(env,local); 
  } 

  public String queryForm(String language, java.util.Map env, boolean local)
  { if ("Java4".equals(language))
    { return queryForm(env,local); } 
    else if ("Java6".equals(language))
    { return queryFormJava6(env,local); } 
    else if ("Java7".equals(language))
    { return queryFormJava7(env,local); } 
    else if ("CSharp".equals(language))
    { return queryFormCSharp(env,local); } 
    else 
    { return queryFormCPP(env,local); } 
  } 

  public abstract String queryFormCSharp(java.util.Map env, boolean local); 
  // { return queryForm(env,local); }   // default 

  public abstract String queryFormJava6(java.util.Map env, boolean local); 
  // { return queryForm(env,local); }   // default 

  public abstract String queryFormJava7(java.util.Map env, boolean local); 

  public abstract String queryFormCPP(java.util.Map env, boolean local);
  // { return queryForm(env,local); }   // default 

  abstract public BExpression bqueryForm(java.util.Map env); 

  abstract public BExpression bqueryForm(); 

  abstract public BStatement bupdateForm(java.util.Map env, boolean local);

  public BStatement bOpupdateForm(java.util.Map env, boolean local)
  { return bupdateForm(env,local); } 


  public BExpression bInvariantForm(java.util.Map env,boolean local)
  { Vector uses = getVariableUses();
    Vector pars = new Vector(); // parameters from the context
    Vector variables = Constraint.variableTypeAssign(uses,pars); 
    BExpression pred = binvariantForm(env,local);   // add quantifiers for vbls
    return Constraint.addVariableQuantifiers(variables,pred);
  }

  abstract public BExpression binvariantForm(java.util.Map env,boolean local); 

  abstract public String toString();  // RSDS version of expression

  abstract public String toSQL(); 

  abstract public String toJava();   // Java4 version of expression

  public String toJava6()   // Java6 version of expression
  { java.util.Map env = new java.util.HashMap(); 
    return queryFormJava6(env,true); 
  } 

  public String toJava7()   // Java7 version of expression
  { java.util.Map env = new java.util.HashMap(); 
    return queryFormJava7(env,true); 
  } 

  public String toCSharp()   // C# version of expression
  { java.util.Map env = new java.util.HashMap(); 
    return queryFormCSharp(env,true); 
  } 

  public String toCPP()   // C++ version of expression
  { java.util.Map env = new java.util.HashMap(); 
    return queryFormCPP(env,true); 
  } 


  abstract public String toB();  

  // abstract public String toOcl(); 

  public String toOcl(java.util.Map env, boolean local) 
  { return toString(); } 

  abstract public String toZ3();  

  abstract public Expression toSmv(Vector cnames);   // SMV version 

  abstract public String toImp(final Vector comps);  // B IMPLEMENTATION version

  abstract public String toJavaImp(final Vector comps); 

  public abstract Expression buildJavaForm(final Vector comps);

  public void setJavaForm(Expression e) 
  { javaForm = e; } 

  public static Expression substitute(final Expression e, 
                                      final Expression oldE,
                                      final Expression newE)
  { if (e == null) { return null; } 
    return e.substitute(oldE,newE); 
  } 

  abstract public Expression substitute(final Expression oldE,
                                        final Expression newE); 

  abstract public Expression substituteEq(final String var,
                                          final Expression val); 

  abstract Expression removeSlicedParameters(
             BehaviouralFeature op, Vector fpars); 
  
  abstract public boolean hasVariable(final String s); 

  // public boolean hasComponent(final Statemachine sm)

  public Vector cwr(Vector assocs)
  { return wr(assocs); } 

  public Vector wr(Vector assocs)
  { return new Vector(); } 

  public Vector readFrame()
  { return new Vector(); } 

  public Vector allReadFrame()
  { return new Vector(); } 

  public Vector internalReadFrame()
  { return readFrame(); } 

  public Vector allReadData()
  { Vector vs = getVariableUses(); 
    Vector atts = allAttributesUsedIn();
    Vector res = new Vector(); 
    for (int i = 0; i < vs.size(); i++)
    { String var = "" + vs.get(i); 
      res.add(var); 
    }     
    for (int i = 0; i < atts.size(); i++)
    { String var = "" + atts.get(i); 
      res.add(var); 
    }
    return res; 
  } 

  public abstract Vector allReadBasicExpressionData();  

  public abstract Vector allVariableNames();  

  public boolean confluenceCheck(Vector iterated, Vector created)
  { return true; } 

  abstract public Vector variablesUsedIn(final Vector varNames);

  abstract public Vector componentsUsedIn(final Vector sms); 

  abstract Maplet findSubexp(final String var); 

  abstract public Expression simplify(final Vector vars); 

  abstract public Expression simplify(); 

  protected static Expression pruneDuplicates(Vector cnames, 
                                              Expression e1,
                                              Expression e2)
  { Vector vars1 =
      e1.variablesUsedIn(cnames);
    Vector vars2 =
      e2.variablesUsedIn(cnames);
    Vector inter =
      VectorUtil.intersection(vars1,vars2); 
    if (inter.size() == 0)
    { return simplify("&",e1,e2,null); }
    else  /* inter.size() == 1 */
    { String var = (String) inter.get(0);
      Maplet mm1 = e1.findSubexp(var);
      Maplet mm2 = e2.findSubexp(var);
      Expression sub1 = (Expression) mm1.source;
      Expression sub2 = (Expression) mm2.source;
      Expression sub = resolve(var,sub1,sub2);
      Expression rem = 
          simplify("&", (Expression) mm1.dest, 
                        (Expression) mm2.dest, null);
      return simplify("&",sub,rem,null); 
    }
  }     

  private static Expression resolve(String var,
                                    Expression e1,
                                    Expression e2)
  { if (e1 == null || e2 == null)
    { return null; }  /* Should never happen */
    else 
    { if (e1.subformulaOf(e2))
      { System.err.println("!! Warning: Two copies of formula " +
                    e1.toString() + " being merged"); 
        return e2;  // e2 not e1, surely?  
      }
      else 
      { System.err.println("!! Inconsistent formulae " +
                    e1.toString() + " and " + 
                    e2.toString() + " being removed"); 
        return new BasicExpression(false); 
      }
    }
  }

  static public Expression simplify(final String op, 
                            final Expression e1, 
                            final Expression e2,
                            final Vector vars)
  { if (e1 == null)  { return e2; }
    if (e2 == null)  { return e1; }
    if (op.equals("+")) { return simplifyPlus(e1,e2); } 
    if (op.equals("-")) { return simplifyMinus(e1,e2); } 
    if (op.equals("*")) { return simplifyMult(e1,e2); } 
    if (op.equals("/")) { return simplifyDivide(e1,e2); } 
    if (op.equals("#&")) { return simplifyExistsAnd(e1,e2); } 
    if (op.equals("&")) { return simplifyAnd(e1,e2); } 
    if (op.equals("or")) { return simplifyOr(e1,e2); }
    if (op.equals("=>")) { return simplifyImp(e1,e2); } 
    if (op.equals("=")) { return simplifyEq(e1,e2,vars); }
    // if (op.equals("->apply")) { return simplifyApply(e1,e2,vars); }
    if (op.equals("!=") || op.equals("/=")) 
    { return simplifyNeq(e1,e2,vars); } 
    if (op.equals(":")) 
    { return simplifyIn(e1,e2,vars); }
    if (comparitors.contains(op)) 
    { return simplifyIneq(op,e1,e2); } 
    return new BinaryExpression(op,e1,e2);
  }

  // should extend to deal with evaluation of +, *, -, /, :, etc
  static public Expression simplify(final String op, 
                            final Expression e1, 
                            final Expression e2, boolean needsBrackets)
  { if (e1 == null)  { return e2; }
    if (e2 == null)  { return e1; }
    Expression res; 
    if (op.equals("&")) { res = simplifyAnd(e1,e2); } 
    else if (op.equals("or")) { res = simplifyOr(e1,e2); }
    else if (op.equals("=>")) { res = simplifyImp(e1,e2); } 
    else if (op.equals("=")) { res = simplifyEq(e1,e2); }
    else if (op.equals("!=") || op.equals("/=")) { res = simplifyNeq(e1,e2); } 
    else if (op.equals(":")) { res = simplifyIn(e1,e2); } 
    else if (op.equals("->apply")) { res = simplifyApply(e1,e2); } 
    else if (comparitors.contains(op)) { res = simplifyIneq(op,e1,e2); } 
    else { res = new BinaryExpression(op,e1,e2); } 
    res.setBrackets(needsBrackets); 
    return res; 
  }


  public static Expression simplifyPlus(Expression e1, Expression e2) 
  { if (e1 == null) { return e2; } 
    if (e2 == null) { return e1; } 

    if (isInteger("" + e1) && isInteger("" + e2))
    { int v1 = convertInteger("" + e1); 
      int v2 = convertInteger("" + e2); 
      return new BasicExpression(v1 + v2); 
    }
    else if (isNumber("" + e1) && isNumber("" + e2))
    { double v1 = convertNumber("" + e1); 
      double v2 = convertNumber("" + e2); 
      return new BasicExpression(v1 + v2); 
    } 
    return new BinaryExpression("+", e1, e2); 
  }  

  public static Expression simplifyMinus(Expression e1, Expression e2) 
  { if (e1 == null) { return e2; } 
    if (e2 == null) { return e1; } 

    if (isInteger("" + e1) && isInteger("" + e2))
    { int v1 = convertInteger("" + e1); 
      int v2 = convertInteger("" + e2); 
      return new BasicExpression(v1 - v2); 
    }
    else if (isNumber("" + e1) && isNumber("" + e2))
    { double v1 = convertNumber("" + e1); 
      double v2 = convertNumber("" + e2); 
      return new BasicExpression(v1 - v2); 
    } 
    return new BinaryExpression("-", e1, e2); 
  }  

  public static Expression simplifyMult(Expression e1, Expression e2) 
  { if (e1 == null) { return e2; } 
    if (e2 == null) { return e1; } 

    if (isInteger("" + e1) && isInteger("" + e2))
    { int v1 = convertInteger("" + e1); 
      int v2 = convertInteger("" + e2); 
      return new BasicExpression(v1 * v2); 
    }
    else if (isNumber("" + e1) && isNumber("" + e2))
    { double v1 = convertNumber("" + e1); 
      double v2 = convertNumber("" + e2); 
      return new BasicExpression(v1 * v2); 
    } 
    return new BinaryExpression("*", e1, e2); 
  }  

  public static Expression simplifyDivide(Expression e1, Expression e2) 
  { if (e1 == null) { return e2; } 
    if (e2 == null) { return e1; } 

    if (isInteger("" + e1) && isInteger("" + e2))
    { int v1 = convertInteger("" + e1); 
      int v2 = convertInteger("" + e2);
      // case of v2 = 0 
      return new BasicExpression(v1 / v2); 
    }
    else if (isNumber("" + e1) && isNumber("" + e2))
    { double v1 = convertNumber("" + e1); 
      double v2 = convertNumber("" + e2); 
      // case of v2 = 0 
      return new BasicExpression(v1 / v2); 
    } 
    return new BinaryExpression("/", e1, e2); 
  }  

  public static List simplifyAnd(final List e1s, final List e2s) 
  { if (e1s == null) { return e2s; } 
    if (e2s == null) { return e1s; } 
    if (e1s.size() == 0) { return e2s; } 
    if (e2s.size() == 0) { return e1s; } 

    Expression e1 = (Expression) e1s.get(0); 
    Expression e2 = (Expression) e2s.get(0); 
    Expression e = simplifyAnd(e1,e2); 
    Vector res = new Vector(); 
    res.add(e); 
    return res; 
  }  

  public static Expression simplifyAnd(final Expression e1,
                                       final Expression e2)
  { if (e1 == null) { return e2; } 
    if (e2 == null) { return e1; } 
    if (e1.equalsString("false"))
    { return new BasicExpression(false); } 
    if (e2.equalsString("false"))
    { return new BasicExpression(false); } 
    if (e1.equalsString("true")) 
    { return e2; } 
    if (e2.equalsString("true"))
    { return e1; }
    if (e1.equals(e2))
    { return e1; }
    if (e1 instanceof BinaryExpression) 
    { BinaryExpression be1 = (BinaryExpression) e1; 
      if (be1.operator.equals("&"))
      { if ((e2 + "").equals(be1.left + ""))
        { return e1; } 
        else if ((e2 + "").equals(be1.right + ""))
        { return e1; } 
      } 
    }  
    if (e2 instanceof BinaryExpression) 
    { BinaryExpression be2 = (BinaryExpression) e2; 
      if (be2.operator.equals("&"))
      { if ((e1 + "").equals(be2.left + ""))
        { return e2; } 
        else if ((e1 + "").equals(be2.right + ""))
        { return e2; } 
      } 
    }  
    if (e1.conflictsWith(e2))
    { return new BasicExpression("false"); } 
    return new BinaryExpression("&",e1,e2);  
  }   // if (e1.subformulaOf(e2)) { return e2; } 


  public static boolean isLambdaApplication(Expression pred)
  { if (pred instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) pred; 
      if (be.getOperator().equals("->apply") && 
          be.getLeft() instanceof UnaryExpression) 
      { UnaryExpression f = (UnaryExpression) be.getLeft(); 
        if (f.getOperator().equals("lambda") && 
            f.getAccumulator() != null) 
        { return true; } 
      } 
    } 
    return false; 
  } 

  public static Expression simplifyApply(Expression be) 
  { if (be instanceof BinaryExpression) 
    { BinaryExpression fapp = (BinaryExpression) be; 
      return Expression.simplifyApply(fapp.getLeft(), fapp.getRight()); 
    } 
    return be; 
  } 

  public static Expression simplifyApply(Expression func, Expression arg)
  { // (lambda var : T in lbody)->apply(arg)  is 
    // lbody[arg/var]

    if (func instanceof UnaryExpression) 
    { UnaryExpression f = (UnaryExpression) func; 
      if (f.getOperator().equals("lambda") && 
          f.getAccumulator() != null) 
      { Attribute var = f.getAccumulator(); 
        String vname = var.getName(); 
        Expression lbody = f.getArgument(); 
        return lbody.substituteEq(vname,arg); 
      } 
    } 
    return new BinaryExpression("->apply", func, arg); 
  }  

  public static Expression simplifyApply(Expression func, Expression arg1, Expression arg2)
  { // (lambda var1 : T1 in 
    //   (lambda var2 : T2 in lbody))->apply(arg1)->apply(arg2)  
    // is 
    // lbody[arg1/var1,arg2/var2]

    if (func instanceof UnaryExpression) 
    { UnaryExpression f = (UnaryExpression) func; 
      if (f.getOperator().equals("lambda") && 
          f.getAccumulator() != null) 
      { Attribute var = f.getAccumulator(); 
        String vname = var.getName(); 
        Expression lbody = f.getArgument(); 
        Expression res = lbody.substituteEq(vname,arg1);
        return Expression.simplifyApply(res,arg2);  
      } 
    } 
    return new BinaryExpression("->apply", func, arg1); 
  }  

  public static Expression simplifyApply2(Expression func, Expression arg1, Expression arg2)
  { // (lambda var1 : T1 in 
    //   (lambda var2 : T2 in lbody))->apply(arg1)->apply(arg2)  
    // is 
    // lbody[arg1/var1,arg2/var2]

    Vector pars = new Vector(); 
    pars.add(arg1); 
    pars.add(arg2); 

    if (func instanceof BasicExpression)
    { // set its parameters to be arg1,arg2
      BasicExpression fbe = (BasicExpression) func; 
      BasicExpression res = 
        BasicExpression.newCallBasicExpression(fbe.data,
                                fbe.objectRef,pars); 
      return res; 
    } 

    if (func instanceof UnaryExpression) 
    { UnaryExpression f = (UnaryExpression) func; 
      if (f.getOperator().equals("lambda") && 
          f.getAccumulator() != null) 
      { Attribute var = f.getAccumulator(); 
        String vname = var.getName(); 
        Expression lbody = f.getArgument(); 
        Expression res = lbody.substituteEq(vname,arg1);
        return Expression.simplifyApply(res,arg2);  
      } 
    } 
    return new BinaryExpression("->apply", func, arg1); 
  }  

  public static Expression simplifyExistsAnd(final Expression e1,
                                       final Expression e2)
  { if (e1 instanceof BinaryExpression) 
    { BinaryExpression be1 = (BinaryExpression) e1; 
      if ("#".equals(be1.operator))
      { Expression res1 = simplifyExistsAnd(be1.right, e2); 
        return new BinaryExpression("#", be1.left, res1); 
      }
      else if ("#LC".equals(be1.operator))
      { Expression res1 = simplifyExistsAnd(be1.right, e2); 
        return new BinaryExpression("#LC", be1.left, res1); 
      }
      else if ("&".equals(be1.operator))
      { Expression res1 = simplifyExistsAnd(be1.right, e2); 
        return simplifyExistsAnd(be1.left, res1); 
      }   
    } 
    return simplifyAnd(e1,e2); 
  }
  
  public static Expression simplifyOr(final Expression e1,
                                      final Expression e2)
  { if (e1.equalsString("true") || e2.equalsString("true"))
    { return new BasicExpression(true); }
    if (e1.equalsString("false"))
    { return e2; }
    if (e2.equalsString("false"))
    { return e1; }
    if (e1.equals(e2))
    { return e1; }
    return new BinaryExpression("or",e1,e2); 
  }  // if (e1.subformulaOf(e2)) { return e1; }


  public static Expression simplifyImp(final Expression ante,
                                       final Expression succ)
  { if (ante.equalsString("true"))
    { return succ; }
    if (succ.equalsString("true"))
    { return new BasicExpression(true); }
    if (ante.equals(succ))
    { return new BasicExpression(true); }
    if (succ instanceof BinaryExpression)
    { BinaryExpression sbe = (BinaryExpression) succ;
      if (sbe.operator.equals("=>"))
      { Expression newante = simplifyAnd(ante,sbe.left); 
         // = new BinaryExpression("&",ante,sbe.left);
        return new BinaryExpression("=>",newante,sbe.right);
      }
    }
    return new BinaryExpression("=>",ante,succ);
  }

  private static Expression simplifyEq(final Expression e1,
                                       final Expression e2, 
                                       final Vector vars)
  { if (e1.equals(e2))
    { return new BasicExpression(true); }
                   /* if different states of same component, false */
                   /*for (int i = 0; i < comps.size(); i++) 
                     { Statemachine sm = (Statemachine) comps.elementAt(i); 
                       boolean b1 = sm.hasState(e1); 
                       if (b1) 
                       { boolean b2 = sm.hasState(e2); 
                         if (b2) { return false; } 
                       } 
                     } */ 
    /* Or, if vars is list of all variables: 
    else 
    { boolean b1 = vars.contains(e1.toString()); 
      if (!b1)
      { boolean b2 = vars.contains(e2.toString()); 
        if (!b2) { return new BasicExpression("false"); } 
      } 
    } */ 
    return new BinaryExpression("=",e1,e2); 
  }  // new version: 
  // else if (e1.getKind() == VALUE && e2.getKind() == VALUE)
  // { return false; } 

  private static Expression simplifyNeq(final Expression e1,
                                        final Expression e2,
                                        final Vector vars)
  { if (e1.equals(e2))
    { return new BasicExpression(false); }
    /* else 
    { boolean b1 = vars.contains(e1.toString());
      if (!b1)
      { boolean b2 = vars.contains(e2.toString());
        if (!b2) // they are both primitive values, not equal
        { return new BasicExpression("true"); }
      }
    } */ 
    return new BinaryExpression("!=",e1,e2);
  }

  private static Expression simplifyEq(final Expression e1,
                                       final Expression e2)
  { if (e1.equals(e2))
    { return new BasicExpression(true); }
    // else if (e1.getKind() == VALUE && e2.getKind() == VALUE)
    // { return new BasicExpression("false"); } 
                   
    return new BinaryExpression("=",e1,e2); 
  }  

  private static Expression simplifyNeq(final Expression e1,
                                        final Expression e2)
  { if (e1.equals(e2))
    { return new BasicExpression(false); }
    // else if (e1.getKind() == VALUE && e2.getKind() == VALUE)
    // { return new BasicExpression("true"); } 

    return new BinaryExpression("!=",e1,e2);
  }
  
  public static Expression negate(Expression e)
  { if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e; 
      String op = be.operator; 
      if (op.equals("&"))
      { Expression nleft = negate(be.left); 
        Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("or", nleft, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("or"))
      { Expression nleft = negate(be.left); 
        Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("&", nleft, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("->exists"))
      { Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("->forAll", be.left, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("->forAll"))
      { Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("->exists", be.left, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("#"))
      { Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("!", be.left, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("!"))
      { Expression nright = negate(be.right); 
        Expression res = new BinaryExpression("#", be.left, nright); 
        res.setBrackets(true); 
        return res; 
      } 
      else if (op.equals("->includesAll")) 
      { return new BinaryExpression("/<:",be.right,be.left); } 


      String nop = negateOp(be.operator); 
      if (!(nop.equals(be.operator))) 
      { return new BinaryExpression(nop,be.left,be.right); } 
      return new UnaryExpression("not",e); 
    } 
    else if (e instanceof UnaryExpression) 
    { UnaryExpression ue = (UnaryExpression) e; 
      if (ue.operator.equals("not"))
      { return ue.argument; } 
    } 
    else if (e instanceof BasicExpression)
    { if ("true".equals(e + ""))
      { return new BasicExpression(false); } 
      if ("false".equals(e + ""))
      { return new BasicExpression(true); } 
    }

    return new UnaryExpression("not",e); 
  } // and other cases

  private static Expression simplifyIn(Expression le,
                                Expression re,
                                Vector vars)
  { if (re instanceof SetExpression)
    { SetExpression rse = (SetExpression) re;
      if (rse.isEmpty()) 
      { return new BasicExpression(false); }
      if (rse.isSingleton())
      { Expression relem = rse.getElement(0);
        return simplify("=",le,relem,vars);
      }
    } // x : a \/ b  as  x : a or x : b etc?
    return new BinaryExpression(":",le,re);
  }

  private static Expression simplifyIn(Expression le,
                                Expression re)
  { if (re instanceof SetExpression)
    { SetExpression rse = (SetExpression) re;
      if (rse.isEmpty()) 
      { return new BasicExpression(false); }
      if (rse.isSingleton())
      { Expression relem = rse.getElement(0);
        return simplify("=",le,relem,false);
      }
    } // x : a \/ b  as  x : a or x : b etc?
    return new BinaryExpression(":",le,re);
  }


  private static Expression simplifyIneq(String op, Expression left, 
                                         Expression right)
  { String lval = left.toString(); 
    String rval = right.toString(); 
    if (op.equals("<") || op.equals(">"))
    { if (lval.equals(rval)) 
      { return new BasicExpression(false); } 
    }
    else if (op.equals("<=") || op.equals(">=") || op.equals("<:"))
    { if (lval.equals(rval))
      { return new BasicExpression(true); } 
    } 
    try 
    { int x = Integer.parseInt(lval); 
      int y = Integer.parseInt(rval); 
      boolean val = VectorUtil.test(op,x,y);  
      return new BasicExpression("" + val); 
    } // and for long ints. 
    catch (Exception e) 
    { return new BinaryExpression(op,left,right); } 
  } 

  public boolean equals(Object obj) 
  { if (obj instanceof Expression && obj != null) 
    { return toString().equals(obj.toString()); } 
    else 
    { return false; } 
  }

  public abstract Expression filter(final Vector vars);

  public abstract Object clone(); // Not needed? 

  public void display() 
  { System.out.println(toString()); } 

  public Expression excel2Ocl(Entity target, Vector entities, Vector qvars, Vector antes)
  { return this; }  

  public Vector splitToCond0Cond1(Vector conds, Vector pars, Vector qvars1, Vector lvars1, Vector allvars)
  { return conds; }  // all formulae in antecedent should be binary

  public Vector splitToCond0Cond1Pred(Vector conds, Vector pars, 
                            Vector qvars1, Vector lvars1, Vector allvars, Vector allpreds)
  // { return conds; }    
  { Expression cond0 = (Expression) conds.get(0); 
    Expression cond1 = (Expression) conds.get(1); 
    Vector res = new Vector(); 
    // System.out.println("Split conds on: " + this);

    Expression c1; 
    if (cond1 == null) 
    { c1 = this; } 
    else 
    { c1 = new BinaryExpression("&",cond1,this); }  
    res.add(cond0); 
    res.add(c1); 
    allvars.add(this); 
    allpreds.add(this); 
    // System.out.println("Added condition: " + this); 
    
    return res; 
  } 


  public Vector splitToTCondPost(Vector tvars, Vector conds)
  { return conds; }  // all formulae in antecedent should be binary

  public Vector variableRanges(java.util.Map m, Vector pars)
  { return new Vector(); } 

  public Vector letDefinitions(java.util.Map m, Vector pars)
  { return new Vector(); } 

  static protected Expression filterEquality(final Expression l,
                                    final Expression r,
                                    final Expression e,
                                    final Vector vars)
  { boolean found = false;
    for (int i = 0; i < vars.size(); i++)
    { String var = (String) vars.elementAt(i);
      if (l.equalsString(var) ||  r.equalsString(var))
      { found = true;
        return e; 
      } 
    }
    return null;  
  }

  public boolean equalsString(final String s)
  { return s.equals(toString()); }

  abstract public Vector splitAnd(final Vector comps); 

  abstract public Vector splitOr(final Vector comps); 

  abstract public Expression expandMultiples(final Vector sms);

  abstract public Expression removeExpression(final Expression e);

  public Expression removeExpressions(final Vector el)
  { Expression res = (Expression) clone();
    for (int i = 0; i < el.size(); i++)
    { Expression e = (Expression) el.elementAt(i); 
      Expression res2 = res.removeExpression(e);
      if (res2 == null)
      { return null; }
      else 
      { res = res2; } 
    }
    return res; 
  }

 public static Expression removeTypePredicate(Expression cond, 
                                              String var, String typ)
 { if (cond instanceof BinaryExpression)
   { BinaryExpression bex = (BinaryExpression) cond; 
     if (bex.operator.equals(":") && var.equals(bex.left + "") && 
         typ.equals(bex.right + ""))
     { return new BasicExpression("true"); } 
   
     Expression c1 = removeTypePredicate(bex.left,var,typ); 
     Expression c2 = removeTypePredicate(bex.right,var,typ); 
     return simplify(bex.operator,c1,c2,false); 
   } 
   return cond; 
  } 

 
 public abstract boolean implies(final Expression e);

 public abstract boolean consistentWith(final Expression e);

 public abstract boolean selfConsistent(final Vector vars); 

 public boolean selfConsistent()
 { return true; }   // default 

 public abstract boolean subformulaOf(final Expression e);

 public abstract boolean conflictsWith(final Expression e); 

  public boolean conflictsWith(String op, Expression el,
                             Expression er)
  { return false; } 

public static boolean conflictsOp(String op1, String op2)
{ if (op1.equals("="))
  { return (op2.equals(">") || op2.equals("<") ||
            op2.equals("/=") || op2.equals("/<:"));
  }
  if (op1.equals("<"))
  { return (op2.equals(">") || op2.equals(">=") ||
            op2.equals("="));
  }
  if (op1.equals(">"))
  { return (op2.equals("<") || op2.equals("<=") ||
            op2.equals("="));
  }
  if (op1.equals("/="))
  { return op2.equals("="); }
  if (op1.equals("<:"))
  { return op2.equals("/<:"); }
  if (op1.equals(":"))
  { return op2.equals("/:"); }
  if (op1.equals("->includes"))
  { return op2.equals("->excludes"); }
  if (op1.equals("/<:"))
  { return (op2.equals("=") || op2.equals("<:")); }
  if (op1.equals("/:"))
  { return op2.equals(":"); }
  if (op1.equals("->excludes"))
  { return op2.equals("->includes"); }
  if (op1.equals("<="))
  { return op2.equals(">"); } 
  if (op1.equals(">="))
  { return op2.equals("<"); }
  return false;
}

/* I don't understand the purpose of this: */ 
public static boolean conflictsReverseOp(String op1, String op2)
{ if (op1.equals("="))
  { return (op2.equals(">") || op2.equals("<") ||
            op2.equals("/=") || op2.equals("/<:"));
  }
  if (op1.equals("<"))
  { return (op2.equals("<") || op2.equals("<=") ||
            op2.equals("="));
  }
  if (op1.equals(">"))
  { return (op2.equals(">") || op2.equals(">=") ||
            op2.equals("="));
  }
  if (op1.equals("/="))
  { return op2.equals("="); }
  if (op1.equals("/<:"))
  { return op2.equals("="); }
  if (op1.equals("<="))
  { return op2.equals("<"); } 
  if (op1.equals(">="))
  { return op2.equals(">"); }
  return false;
}




 public boolean conflictsWith(final Expression e, 
                              final Vector vars)
  { for (int i = 0; i < vars.size(); i++)
    { String var = (String) vars.elementAt(i);
      Expression myval = getSettingFor(var);
      Expression eval = e.getSettingFor(var);

      // System.out.println("1st exp sets " + var + " = " +
      //                   myval + " in " + toString()); 
      // System.out.println("2nd exp sets " + var + " = " + 
      //                   eval + " in " + e); 

      if (myval == null || eval == null)  
      {  }  /* ok */
      else if (myval.equals(eval))  { }
      else { return true; }  /* conflict */
    }
   return false; 
 }

  public static Expression evaluateExpAt(Expression e,
      Vector mapping, Vector allvars)
  { Expression e2 = (Expression) e.clone(); 
    BasicExpression be; 

    for (int i = 0; i < mapping.size(); i++)
    { Maplet setting = (Maplet) mapping.get(i);
      String var = (String) setting.source;
      Object val = setting.dest;
      if (val instanceof String) 
      { be = new BasicExpression((String) val); } 
      else 
      { be = (BasicExpression) val; } 
      Expression e1 = e2.substituteEq(var,be);
      e2 = e1.simplify(allvars);
    }
    return e2;
  }


 protected Expression getSettingFor(final String var)
 { Maplet mm = findSubexp(var);
   if (mm == null)  { return null; }
   else 
   { Expression ee = (Expression) mm.source;
     if (ee != null &&
         (ee instanceof BinaryExpression) && 
         ((BinaryExpression) ee).operator.equals("="))
    { BinaryExpression be = (BinaryExpression) ee;
      if (be.left.equalsString(var))
      { return  be.right; }
      else 
      { return be.left; } 
    }
    else 
    { return null; } 
   }
 }     

  abstract public Expression computeNegation4succ(final Vector actuators); 

  abstract public Vector computeNegation4ante(final Vector actuators);

  public Expression computeNegation4succ()
  { return null; }  // default  

  public Vector computeNegation4ante()
  { return new Vector(); }  // default

  public Expression satisfiedAt(State st,
                             Vector snames, 
                             String[] vals,
                             Vector cnames)
  { /* Tests if self satisfied at state 
       described by st and vals/snames */
    Expression temp;
    Expression res = (Expression) clone();

    for (int i = 0; i < snames.size(); i++)
    { String sen = (String) snames.get(i);
      String val = vals[i];
      temp = res.substituteEq(sen, 
                 new BasicExpression(val));
      res = (Expression) temp.clone(); 
    }

    Maplet mm = st.getProperties();
    if (mm != null && mm.source != null)
    { Vector subs = (Vector) mm.source;
      for (int j = 0; j < subs.size(); j++)
      { Maplet sub = (Maplet) subs.get(j);
        String act = (String) sub.source;
        Expression actval = 
              (Expression) sub.dest;
        temp =
              res.substituteEq(act,actval);
        res = (Expression) temp.clone();
      }
    }
    return res.simplify(cnames); 
  }

  public UpdateFeatureInfo updateFeature(String f)
  { return null; } // default

  public Expression updateExpression() { return null; } // default

  public Expression updateReference() { return null; }  // default

  /* What is this doing??: */ 
  public static Entity findTarget(Expression succ, String src)
  { if (succ instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) succ;
      Entity e1 = be.left.getEntity();
      Entity e2 = be.right.getEntity();
      if (e1 != null)
      { String e1name = e1.getName();
        if (e1name.equals(src)) { }
        else if (be.operator.equals("="))
        { return e1; }
      }
      if (e2 != null)
      { String e2name = e2.getName();
        if (e2name.equals(src)) { }
        else
        { return e2; }
      }
      return null;
    }
    return succ.getEntity();
  } // but could be more implicit: x : objs.att

  // Is this used? What does it do? 
  public static Entity getTargetEntity(Expression e)
  { if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e; 
      Expression l = be.left; 
      Expression r = be.right; 
      Entity e1 = l.getEntity(); 
      Entity e2 = r.getEntity(); 
      if (be.operator.equals("=")) 
      { if (e1 == null) 
        { return e2; } 
        return e1; 
      } 
      if (be.operator.equals(":"))  // and /:, <:, /<:, ->includes, ->includesAll  ??
      { if (e2 == null)
        { return e1; } 
        return e2; 
      } 
    } 
    return null; 
  } 

  public static String objectType(Expression typ, java.util.Map env, boolean local)
  { if (typ != null)
    { if (typ.umlkind == CLASSID)
      { return typ.entity.objectType(); }  
      return "" + typ.binvariantForm(env,local);  
    } 
    return typ + ""; 
  }

  // Strange operation: 
  public static Vector ancestorAddStatements(Expression typ, BExpression var)
  { Vector res = new Vector(); 
    if (typ != null)
    { if (typ.umlkind == CLASSID)
      { if (typ.entity != null && typ.entity.hasSuperclass())
        { Entity sup = typ.entity.getSuperclass(); 
          return sup.ancestorAddStatements(var);
        }
      }  
      return res;  
    } 
    return res; 
  }

  public static Vector commonConjSubformula(Expression l,
                                            Expression r)
  { Vector res = new Vector(); // comm, lrem, rrem
    if (l.equals(r)) // also l equals r.reverse() for =
    { res.add(l);
      res.add(null);
      res.add(null);
      return res;
    }
    if (l instanceof BinaryExpression)
    { BinaryExpression bel = (BinaryExpression) l;
      if (bel.operator.equals("&"))
      { if (bel.left.equals(r))
        { res.add(bel.left);
          res.add(bel.right);
          res.add(null);
          return res;
        }
        if (bel.right.equals(r))
        { res.add(bel.right);
          res.add(bel.left);
          res.add(null);
          return res;
        }
        Vector res1 = commonConjSubformula(bel.left,r);
        Vector res2 = commonConjSubformula(bel.right,r);
        res = tripleConj(res1,res2);
        if (res.get(0) != null)
        { return res; }
      }
    }
    if (r instanceof BinaryExpression)
    { BinaryExpression ber = (BinaryExpression) r;
      if (ber.operator.equals("&"))
      { if (ber.left.equals(l))
        { res.add(ber.left);
          res.add(null);
          res.add(ber.right);
          return res;
        }
        if (ber.right.equals(l))
        { res.add(ber.right);
          res.add(null);
          res.add(ber.left);
          return res;
        }
        Vector res1 = commonConjSubformula(l,ber.left);
        Vector res2 = commonConjSubformula(l,ber.right);
        res = tripleConj(res1,res2);
        if (res.get(0) != null)
        { return res; }
      }
    }
    res.add(null);
    res.add(l);
    res.add(r);
    return res;
  }
          
  private static Vector tripleConj(Vector v1, Vector v2)
  { System.out.println("Called with [[" + v1 + "]]  [[" + v2); 

    Vector res = new Vector();
    int n = v1.size();
    Expression comm1 = (Expression) v1.get(0);
    Expression comm2 = (Expression) v2.get(0);
    ((Expression) v2.get(1)).removeExpression(comm1);
    ((Expression) v2.get(2)).removeExpression(comm1);
    ((Expression) v1.get(1)).removeExpression(comm2);
    ((Expression) v1.get(2)).removeExpression(comm2);
    for (int i = 0; i < n; i++)
    { Expression e1 = (Expression) v1.get(i);
      if (i < v2.size())
      { Expression e2 = (Expression) v2.get(i);
        res.add(simplify("&",e1,e2,new Vector()));
      } 
      else 
      { res.add(e1); } // hack
    }
    return res;
  }

  public Expression matchCondition(String op, String f,
                     Expression exbe)
  { // default:
    return new BasicExpression("false"); 
  }

  public static Attribute hasPrimaryKey(Expression right)
  { Type tr = right.getElementType(); 
    if (tr.entity != null) 
    { Entity e = tr.entity; 
      Vector keys = e.allDefinedUniqueAttributes(); 
        // search for equation lft.key = value in right
      if (keys.size() > 0)
      { return (Attribute) keys.get(0); } 
    } 
    return null; 
  } 

  public abstract Expression featureSetting(String var, String feat, Vector r); 

  public Expression featureSetting2(String var, String feat, Vector r)
  { r.add(this); 
    return null; 
  }  // default

  public Expression featureAdding2(String var, Vector r)
  { r.add(this); 
    return null; 
  }  // default

  public static boolean nakedUses(String f, Vector uses)
  { boolean res = true; 
    for (int i = 0; i < uses.size(); i++) 
    { Expression use = (Expression) uses.get(i); 
      if (use instanceof BasicExpression) 
      { BasicExpression be = (BasicExpression) use; 
        if (be.nakedUse(f)) { } 
        else 
        { return false; } 
      } 
      else 
      { return false; } 
    } 
    return res; 
  }  

  public abstract int syntacticComplexity(); 

  public abstract int cyclomaticComplexity(); 

  public abstract Map energyUse(Map uses, 
                                Vector rUses, Vector oUses); 

  public abstract java.util.Map collectionOperatorUses(int level, 
                             java.util.Map res); 

  public Expression isExistsForall(Vector foralls, Expression tracest)
  { return null; }

  public Expression isForall(Vector foralls, Expression tracest)
  { return null; }

  public abstract void changedEntityName(String oldN, String newN); 

  abstract public String cg(CGSpec cgs); 

  public String cgParameter(CGSpec cgs, Vector partail)
  { String atext = this + "";
    Vector args = new Vector();
    Vector eargs = new Vector();
    args.add(cg(cgs));
    eargs.add(this); 
    if (partail.size() == 0) 
    { args.add("");
      eargs.add(null); 
    } 
    else 
    { Expression p = (Expression) partail.get(0); 
      Vector newtail = new Vector(); 
      newtail.addAll(partail); 
      newtail.remove(0); 
      args.add(p.cgParameter(cgs,newtail));
      eargs.add(newtail); 
    }  
    CGRule r = cgs.matchedParameterArgumentRule(this,partail,atext);
    if (r != null)
    { String res = r.applyRule(args,eargs,cgs); 
      System.out.println(">>> Applied parameter rule " + r + " to " + this + " giving " + res); 
      return res; 
    }
    return cg(cgs);
  } // but omit initialisations for parameters

  public static void main(String[] args) 
  { if (Expression.isLong("10000000000000L"))
    { long xx = Expression.convertLong("10000000000000L"); 
      System.out.println(xx); 
    }
  }   
} 

