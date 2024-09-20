import java.util.Vector;
import java.io.*; 
import javax.swing.*;


/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: OCL */ 

public class SetExpression extends Expression
{ private Vector elements = new Vector(); // Expression
  boolean ordered = false; // true for sequences

  public SetExpression() { }

  public SetExpression(boolean b) 
  { ordered = b;
    if (b) 
    { type = new Type("Sequence", null); }
    else 
    { type = new Type("Set", null); } 
  } // what about maps? 

  public SetExpression(Vector v)
  { if (v == null || v.size() == 0 ||
        (v.get(0) instanceof Expression))
    { elements = v; }
    else
    { for (int i = 0; i < v.size(); i++)
      { String ss = v.get(i) + "";
        elements.add(new BasicExpression(ss));
      }
    }
    elementType = Type.determineType(elements); 
    // type.setElementType(elementType); 
  } // For a map, the elements are BinaryExpressions representing pairs "," key value
  

  public SetExpression(Vector v,boolean ord)
  { this(v); 
    ordered = ord; 
    if (ordered) 
    { type = new Type("Sequence", null); }
    else 
    { type = new Type("Set", null); } 
    elementType = Type.determineType(v); 
    type.setElementType(elementType); 
  }

  public static SetExpression newMapSetExpression()
  { SetExpression res = new SetExpression(); 
    res.setType(new Type("Map", null)); 
    return res; 
  } 

  public static SetExpression newRefSetExpression()
  { SetExpression res = new SetExpression(); 
    res.setType(new Type("Ref", null)); 
    return res; 
  } 

  public static SetExpression newRefSetExpression(Expression elem)
  { SetExpression res = new SetExpression(); 
    res.setType(new Type("Ref", null));
    res.addElement(elem);  
    return res; 
  } 

  public SetExpression(Vector elems, Type typ)
  { elements = elems; 
    type = (Type) typ.clone(); 
    if (Type.isSequenceType(type))
    { ordered = true; } 

    elementType = Type.determineType(elements); 
    type.setElementType(elementType);
  } 

  public static SetExpression mergeSetExpressions(
                                SetExpression left, 
                                SetExpression right)
  { // ->union of two literal collections, maps

    Type typ = left.getType(); 
    Vector elems1 = left.getElements(); 
    Vector elems2 = right.getElements(); 
    Vector newelems = new Vector(); 
    newelems.addAll(elems1); 

    if (Type.isSequenceType(typ))
    { newelems.addAll(elems2); 
      SetExpression res = new SetExpression(newelems,typ); 
      return res; 
    } 
   
    if (Type.isSetType(typ))
    { for (int i = 0; i < elems2.size(); i++) 
      { Expression e2 = (Expression) elems2.get(i); 
        if (VectorUtil.containsEqualString(
                               e2 + "", newelems)) 
        { } 
        else 
        { newelems.add(e2); } 
      }
      SetExpression res = new SetExpression(newelems,typ); 
      return res; 
    }

    // Else - maps

    System.out.println("*** Merging maps " + left + " and " + right); 

    Vector mapelems = new Vector(); 
    for (int i = 0; i < elems1.size(); i++) 
    { BinaryExpression maplet1 = 
          (BinaryExpression) elems1.get(i); 
      Expression key1 = maplet1.getLeft();

      System.out.println("*** KEY 1: " + key1); 

      boolean foundkey1 = false;  
      for (int j = 0; j < elems2.size(); j++) 
      { BinaryExpression maplet2 = 
          (BinaryExpression) elems2.get(j); 
        Expression key2 = maplet2.getLeft();
 
        if ((key1 + "").equals(key2 + ""))
        { // maplet2 overrides maplet1
          foundkey1 = true; 
          break; 
        } // don't include maplet1 in mapelems
      }

      if (!foundkey1) 
      { mapelems.add(maplet1); } 
    }
    mapelems.addAll(elems2); 

    SetExpression res = new SetExpression(mapelems,typ); 
    return res;
  }
     
  public Vector getParameters() 
  { return new Vector(); } 

  public Expression getExpression(int i) 
  { if (i < elements.size())
    { return (Expression) elements.get(i); } 
    return null; 
  } 

  public static boolean isRefSetExpression(Expression expr)
  { if (expr.type == null) 
    { return false; } 
    if ("Ref".equals(expr.type.getName()))
    { return true; } 
    return false; 
  } 

  public int size()
  { return elements.size(); } 

  public Expression definedness()
  { Expression res = new BasicExpression(true);   // conjunction of definedness of elements
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res = Expression.simplifyAnd(res,elem.definedness());  
    } 
    return res; 
  } 

  public Expression determinate()
  { Expression res = new BasicExpression(true);  // conjunction of definedness of elements
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res = Expression.simplifyAnd(res,elem.determinate());  
    } 
    return res; 
  } 

  public void setPre() 
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      elem.setPre();  
    } 
  } 

  public Expression checkConversions(Type propType, Type propElemType, java.util.Map interp) 
  { Vector argres = new Vector();
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      argres.add(elem.checkConversions(propType, propElemType, interp));  
    } 
    return new SetExpression(argres,ordered); 
  }  

  public Expression addPreForms(String var)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression ne = elem.addPreForms(var); 
      newelems.add(ne); 
    } 
    SetExpression result = new SetExpression(newelems,ordered);
	result.setType(type); 
	result.setElementType(elementType); 
	return result;  
  } 

  public Expression removePrestate()
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression ne = elem.removePrestate(); 
      newelems.add(ne); 
    } 
    Expression res = new SetExpression(newelems,ordered);
    res.setType(type); 
    res.setElementType(elementType); 
    return res;  
  } 

  public void findClones(java.util.Map clones, String rule, String op)
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      elem.findClones(clones, rule, op);  
    }
  } 

  public void findClones(java.util.Map clones, 
                         java.util.Map cloneDefs, 
                         String rule, String op)
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      elem.findClones(clones, cloneDefs, rule, op);  
    }
  } 

  public void findMagicNumbers(java.util.Map mgns, String rule, String op)
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      elem.findMagicNumbers(mgns,rule,op);      
    } 
  } 

  public boolean isEmpty()
  { return elements.size() == 0; }

  public boolean isSingleton()
  { return elements.size() == 1; }

  public boolean isOrdered()
  { return ordered; }

  public boolean isOrderedB()
  { return ordered; }
  
  public boolean isMap()
  { return type != null && "Map".equals(type.getName()); }

  public boolean isSortedMap()
  { return type != null && "Map".equals(type.getName()) && 
           type.isSorted(); 
  }

  public void setOrdered(boolean ord)
  { ordered = ord; } 

  public Expression getElement(int i)
  { if (i < 0 || i >= elements.size())
    { return new BasicExpression("Invalid"); } 
    return (Expression) elements.get(i); 
  }

  public Vector getElements()
  { return elements; } 

  public Expression getLastElement()
  { int i = elements.size(); 
    if (i == 0) 
    { return new BasicExpression("Invalid"); } 
    else 
    { return (Expression) elements.get(i-1); } 
  } 

  public Expression getFirstElement()
  { int i = elements.size(); 
    if (i == 0) 
    { return new BasicExpression("Invalid"); } 
    else 
    { return (Expression) elements.get(0); } 
  } 

  public void addElement(Expression e)
  { elements.add(e); }

  public void addElements(Vector es)
  { elements.addAll(es); }

  public void addElement(int i, Expression e)
  { elements.add(i,e); }

  public void addMapElement(Expression lhs, Expression rhs)
  { Expression maplet = new BinaryExpression("|->", lhs, rhs); 
    elements.add(maplet); 
  } 

  public String toString()
  { String res;

    if (type != null && "Ref".equals(type.getName()))
    { if (elementType != null && elements.size() > 0) 
      { res = "Ref(" + elementType + "){"; }
      else 
      { res = "Ref{"; }
    }  
    else if (isSortedMap())
    { res = "SortedMap{"; }
    else if (isMap())
    { res = "Map{"; }
    else if (ordered) 
    { if (isSorted) 
      { res = "SortedSequence{"; } 
      else 
      { res = "Sequence{"; }
    }  
    else if (isSorted)
    { res = "SortedSet{"; }
    else
    { res = "Set{"; }

    for (int i = 0; i < elements.size(); i++)
    { res = res + elements.get(i);
      if (i < elements.size() - 1)
      { res = res + ","; }
    }
    res = res + "}";
    return res;
  }

  public String toAST()
  { String res = "(OclCollectionExpression ";

    if (type != null && "Ref".equals(type.getName()))
    { res = "Ref { "; } 
    else if (isMap())
    { res = res + " Map { "; }
    else if (ordered) 
    { res = res + " Sequence { "; } 
    else 
    { res = res + " Set { "; }

    if (elements.size() == 0)
    { res = res + " } )"; 
      return res; 
    } 

    res = res + " (OclElementList "; 

    for (int i = 0; i < elements.size(); i++)
    { Expression elem = (Expression) elements.get(i); 
      res = res + elem.toAST();
      if (i < elements.size() - 1)
      { res = res + " , "; }
    }
    res = res + " ) } )";
    return res;
  }

  public Expression invertEq(BasicExpression left) 
  { // inverts  left = this   as  elements(0) = left[1] & ... 
    Expression res = new BasicExpression(true); 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BasicExpression ind = new BasicExpression(i+1); 
      BasicExpression leftcopy = (BasicExpression) left.clone(); 
      leftcopy.setArrayIndex(ind); 
      BinaryExpression eqi = new BinaryExpression("=", e, leftcopy);
      res = Expression.simplifyAnd(res,eqi); 
    } 
    return res; 
  }  

  // saveAsTextModel:
  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("collectionexpression_");
    out.println(res + " : CollectionExpression"); 
    out.println(res + ".expId = \"" + res + "\""); 
    out.println(res + ".isOrdered = " + ordered); 

    for (int i = 0; i < elements.size(); i++)
    { Expression expr = (Expression) elements.get(i);
      String exprid = expr.saveModelData(out); 
      out.println(exprid + " : " + res + ".elements"); 
    }

    if (type != null) 
    { String tname = type.getUMLModelName(out); 
      out.println(res + ".type = " + tname); 
      out.println(res + ".isSorted = " + type.isSorted()); 
    } 
    // else 
    // { out.println(res + ".type = Sequence"); } 

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(res + ".elementType = " + etname); 
    } 
    else 
    { out.println(res + ".elementType = OclAny"); } 

    out.println(res + ".needsBracket = " + needsBracket); 
    out.println(res + ".umlKind = " + umlkind); 
    // out.println(res + ".prestate = " + prestate); 
        
    return res;
  }

  public String toOcl(java.util.Map env, boolean local)
  { String res;

    if (isSortedMap())
    { res = "SortedMap{"; }
    else if (isMap())
    { res = "Map{"; }
    else if (ordered) 
    { res = "Sequence{"; } 
    else if (isSorted) 
    { res = "SortedSet{"; } 
    else 
    { res = "Set{"; }

    for (int i = 0; i < elements.size(); i++)
    { res = res + ((Expression) elements.get(i)).toOcl(env,local);
      if (i < elements.size() - 1)
      { res = res + ","; }
    }

    res = res + "}";

    return res;
  }

  public String toZ3()   // use List
  { String res = "nil";

    for (int i = elements.size() - 1; 0 <= i; i--)
    { res = "(insert " + ((Expression) elements.get(i)).toZ3() + " " + res + ")"; }

    return res;
  }
  

  public String toSQL() // invalid
  { return "/* Invalid for SQL */"; } 

  public boolean isMultiple()
  { return true; } 

  public boolean isPrimitive()
  { return false; } 

  public Vector allReadBasicExpressionData()
  { Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++)
    { res = VectorUtil.union(res, ((Expression) elements.get(i)).allReadBasicExpressionData());
    }

    return res;
  }

  public Vector allReadFrame()
  { Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++)
    { res = VectorUtil.union(res, ((Expression) elements.get(i)).allReadFrame());
    }

    return res;
  }

  public Vector readFrame()
  { Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++)
    { res = VectorUtil.union(res, ((Expression) elements.get(i)).readFrame());
    }

    return res;
  }

  public Expression skolemize(Expression sourceVar, java.util.Map env)
  { return this; } 

  /* TODO: add operations for MAPS. */ 
  
  public String queryForm(java.util.Map env, boolean local)
  { if (type != null && "Ref".equals(type.getName()))
    { Type et = getElementType();
      String cset = "Object"; 
      if (et != null) 
      { cset = et.getJava(); }
      String refsze = "1";  
      if (elements.size() > 0) 
      { Expression refsize = (Expression) elements.get(0); 
        refsze = refsize.queryForm(env,local); 
      } 
      return "new " + cset + "[" + refsze + "]"; 
    } 

    if (isMap())
    { String result = "(new HashMap())"; 
      for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.queryForm(env,local) + "," + 
		                           value.queryForm(env,local) + ")";
      }
      return result; 
    }
	
    String res = "(new SystemTypes.Set())"; 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = res + ".add(" + wrap(elementType, e.queryForm(env,local)) + ")";
    }
    res = res + ".getElements()"; 
    return res; 
  }  // different for sequences?

  public String queryFormJava6(java.util.Map env, boolean local)
  { if (type != null && "Ref".equals(type.getName()))
    { Type et = getElementType();
      String cset = "Object"; 
      if (et != null) 
      { cset = et.getJava6(); }
      String refsze = "1";  
      if (elements.size() > 0) 
      { Expression refsize = (Expression) elements.get(0); 
        refsze = refsize.queryFormJava6(env,local); 
      } 
      return "new " + cset + "[" + refsze + "]"; 
    } 

    if (isMap())
    { String result = "(new HashMap())"; 
      for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.queryFormJava6(env,local) + "," + 
		                           value.queryFormJava6(env,local) + ")";
      }
      return result; 
    }
	
    String res = "(new HashSet())"; 
    if (ordered) 
    { res = "(new ArrayList())"; } 

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (ordered) 
      { res = "Set.addSequence(" + res + ", " + e.queryFormJava6(env,local) + ")"; } 
      else 
      { res = "Set.addSet(" + res + ", " + e.queryFormJava6(env,local) + ")"; } 
    }

    return res; 
  }  // different for sequences?

  public String queryFormJava7(java.util.Map env, boolean local)
  { if (type != null && "Ref".equals(type.getName()))
    { Type et = getElementType();
      String cset = "Object"; 
      if (et != null) 
      { cset = et.getJava7(); }
      String refsze = "1";  
      if (elements.size() > 0) 
      { Expression refsize = (Expression) elements.get(0); 
        refsze = refsize.queryFormJava7(env,local); 
      } 
      return "new " + cset + "[" + refsze + "]"; 
    } 

    if (isSortedMap())
    { String mtype = type.getJava7(elementType); 
      String result = "new TreeMap()";
 
      if (elements.size() > 0)
      { BinaryExpression elem1 = 
          (BinaryExpression) elements.get(0); 
        result = 
          "Ocl.singletonSortedMap(" + 
              elem1.getLeft().queryFormJava7(env,local) +
              "," + 
              elem1.getRight().queryFormJava7(env,local) +                  
              ")"; 
      } 

      for (int i = 1; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "Ocl.includingMap(" + result + "," + key.queryFormJava7(env,local) + "," + 
		                           value.queryFormJava7(env,local) + ")";
      }

      return result; 
    }
    
    if (isMap())
    { String mtype = type.getJava7(elementType); 
      String result = "(new " + mtype + "())"; 
      if (elements.size() > 0)
      { BinaryExpression elem1 = 
          (BinaryExpression) elements.get(0); 
        result = 
          "Ocl.singletonMap(" + 
              elem1.getLeft().queryFormJava7(env,local) +
              "," + 
              elem1.getRight().queryFormJava7(env,local) +                  
              ")"; 
      } 
      
      for (int i = 1; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "Ocl.includingMap(" + result + "," + key.queryFormJava7(env,local) + "," + 
		                           value.queryFormJava7(env,local) + ")";
      }
      return result; 
    }
	
    if (type == null)
    { if (ordered)
      { type = new Type("Sequence",null); }
      else
      { type = new Type("Set",null); } 

      if (isSorted) 
      { type.setSorted(true); } 
    } 

    String jType = type.getJava7(elementType); 

    String res = "(new " + jType + "())"; 
    
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (e != null) 
      { String wexp = 
           wrap(elementType, e.queryFormJava7(env,local)); 
        if (ordered) 
        { res = "Ocl.addSequence(" + res + ", " + wexp + ")"; } 
        else 
        { res = "Ocl.addSet(" + res + ", " + wexp + ")"; }
      }  
    }

    return res; 
  }  // different for sequences?

  public String queryFormCSharp(java.util.Map env, boolean local)
  { System.out.println(">>> Query form of " + this + " " + 
                       type + " " + elementType); 

    if (type != null && "Ref".equals(type.getName()))
    { Type et = getElementType();
      String cset = "object"; 
      if (et != null) 
      { cset = et.getCSharp(); }
      String refsze = "1";  
      if (elements.size() > 0) 
      { Expression refsize = (Expression) elements.get(0); 
        refsze = refsize.queryFormCSharp(env,local); 
      } 
      return " stackalloc " + cset + "[" + refsze + "]"; 
    } 

    if (isMap())
    { String result = "(new Hashtable())"; 
      for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "SystemTypes.includingMap(" + result + "," + key.queryFormCSharp(env,local) + "," + 
		                              value.queryFormCSharp(env,local) + ")";
      }
      return result; 
    }
  
    String res = "(new ArrayList())"; 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = "SystemTypes.addSet(" + res + "," + 
                Expression.wrapCSharp(elementType, 
                            e.queryFormCSharp(env,local)) + ")";
    }

    return res; 
  }   

  public String queryFormCPP(java.util.Map env, boolean local)
  { Type et = getElementType(); 
    String cet = "void*"; 
    if (et != null) 
    { cet = et.getCPP(et.getElementType()); } 

    if (type != null && "Ref".equals(type.getName()))
    { String refsze = "1";  
      if (elements.size() > 0) 
      { Expression refsize = (Expression) elements.get(0); 
        refsze = refsize.queryFormCPP(env,local); 
      } 
      return " new " + cet + "[" + refsze + "]"; 
    } 

    if (isMap())
    { Type lkeyt = type.getKeyType();
      String lkeytype = Type.getCPPtype(lkeyt);  
        
      String result = "(new map<" + lkeytype + ", " + cet + ">())"; 

      for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "UmlRsdsOcl<" + lkeytype + ", " + cet + ", " + cet + ">::includingMap(" + 
          result + "," + key.queryFormCPP(env,local) + "," + 
          value.queryFormCPP(env,local) + ")";
      }
      return result; 
    }

    String collkind = "Set"; 
    String res = "(new set<" + cet + ">())";
    if (ordered) 
    { res = "(new vector<" + cet + ">())"; 
      collkind = "Sequence"; 
    } 
 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = "UmlRsdsLib<" + cet + ">::add" + collkind + 
               "(" + res + "," + e.queryFormCPP(env,local) + ")";
    }
    return res; 
  }  

  public String toCSequence(java.util.Map env, boolean local)
  { String cet = "void*"; 
    
    String res = "(new vector<" + cet + ">())"; 
       
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = "addCSequence" + 
               "(" + res + "," + e.queryFormCPP(env,local) + ")";
    }
    return res; 
  }  

  public BExpression bqueryForm(java.util.Map env)
  { Vector elems = new Vector();
    if (elements.size() == 1)
    { Expression elem = (Expression) elements.get(0);
      BExpression belem = elem.bqueryForm(env);
      if ((belem instanceof BSetExpression) || belem.setValued())
      { return belem; }
      else
      { BSetExpression bsete = new BSetExpression();
        bsete.addElement(belem);
        bsete.setOrdered(ordered); 
        return bsete;
      }
    }
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.bqueryForm(env);
      elems.add(be);
    }
    
    return new BSetExpression(elems,ordered);
  } // maps?

  public BExpression bqueryForm()
  { Vector elems = new Vector();
    if (elements.size() == 1)
    { Expression elem = (Expression) elements.get(0);
      BExpression belem = elem.bqueryForm();
      if ((belem instanceof BSetExpression) || belem.setValued())
      { return belem; }
      else
      { BSetExpression bsete = new BSetExpression();
        bsete.addElement(belem);
        bsete.setOrdered(ordered); 
        return bsete;
      }
    }
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.bqueryForm();
      elems.add(be);
    }
    
    return new BSetExpression(elems,ordered);
  } // maps? 

  public int minModality()
  { int mm = 9;
    if (elements.size() == 0)
    { return 2; } // sensor

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(0);
      int mmx = e.minModality();
      if (mmx < mm)
      { mm = mmx; }
    }
    return mm;
  }

  public int maxModality()
  { int mm = 0;
    if (elements.size() == 0)
    { return 2; } // sensor

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(0);
      int mmx = e.maxModality();
      if (mmx > mm)
      { mm = mmx; }
    }
    return mm;
  }

  public Vector metavariables()
  { Vector pres = new Vector();

    if (type != null && "Ref".equals(type.getName()) && 
        elementType != null)
    { pres.add("_1"); }

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.metavariables();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector allPreTerms()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.allPreTerms();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector allPreTerms(String var)
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.allPreTerms(var);
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector innermostEntities()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.innermostEntities();
      pres = VectorUtil.union(pres,epres);
    }
    return pres; 
  }

  public Vector getBaseEntityUses()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.getBaseEntityUses();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }


  public String updateForm(String language, java.util.Map env, String op, String val, Expression var, boolean local)
  { // update   this = var  to this, val is
    // query form of var. 
    // Assume op is "="

    if ("=".equals(op)) { } 
    else  
    { System.err.println("!! Cannot generate code for " + 
                         this + " " + op + " " + val); 
    } 

    String res = "";

    if (isOrdered())
    { // (var _x : var.type := var; 
      //  elem1 := var[1]; ...; elemn := var[n])

      String varx = Identifier.nextIdentifier("_var"); 
      BasicExpression vx = 
        BasicExpression.newVariableBasicExpression(varx,
                                   var.getType()); 
      CreationStatement cs = 
        CreationStatement.newCreationStatement(varx,
                                   var.getType(), var); 

      SequenceStatement ss = new SequenceStatement(); 
      ss.addStatement(cs);  
      ss.setBrackets(true); 

      for (int i = 0; i < elements.size(); i++)
      { Expression elem = (Expression) elements.get(i);
        // BasicExpression vari = (BasicExpression) var.clone();
        // vari.setArrayIndex(new BasicExpression(i+1));
        
        BinaryExpression vari = 
          new BinaryExpression("->at", vx, 
                               new BasicExpression(i+1));
        vari.setType(var.getElementType()); 
 
        AssignStatement seti = 
            new AssignStatement(elem, vari); 
        ss.addStatement(seti); 
      } 
        
      // JOptionPane.showMessageDialog(null, "Sequence assignment " + this + " := " + var + " code is " + ss); 

      res = ss.updateForm(language,env,local); 
    }  
    else 
    { // if (i < var.size) { elemi := (var -  Set{elem1, ..., elemi-1})->any() }
      for (int i = 0; i < elements.size(); i++)
      { Expression elem = (Expression) elements.get(i);
        BasicExpression vari = (BasicExpression) var.clone();
        SetExpression prev = subrange(1,i-1);
        BinaryExpression subt = new BinaryExpression("-", vari, prev);
        UnaryExpression varelem = new UnaryExpression("->any", subt); 
        BinaryExpression seti = new BinaryExpression("=", elem, varelem );
        UnaryExpression varsize = new UnaryExpression("->size", var );
        BinaryExpression se = new BinaryExpression(">", varsize, new BasicExpression(i));
        res = res + "  if (" + se.queryForm(language,env,local) + ") { " +  seti.updateForm(language,env,local) + " }\n";
      }
    }  
    return res;
  } // For maps???

  public SetExpression subrange(int i, int j)
  { SetExpression res = new SetExpression();
     for (int k = i-1; k < elements.size() && k < j; k++)
     { Expression e = (Expression) elements.get(k);
       res.addElement(e);
     }
     res.setOrdered(isOrdered());
     return res;
  }

  public String updateForm(java.util.Map env,boolean local)
  { return "{} // no update form for: " + this; } 

  public String updateForm(java.util.Map env, String val2)
  { return "{} // no update form for: " + this; }

  public BExpression binvariantForm(java.util.Map env, boolean local)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.binvariantForm(env,local);
      elems.add(be);
    }
    return new BSetExpression(elems,ordered);
  }

  public BExpression binvariantForm(java.util.Map env,
                                    BExpression v)
  { return new BBasicExpression("/* not valid */"); }  // ???

  public BStatement bupdateForm(java.util.Map env,boolean local)
  { return new BBasicStatement("skip"); /* not valid */ }

  public int typeCheck(final Vector sms)
  { return SENSOR; }

  public boolean typeInference(final Vector typs, 
                                        final Vector ents,
                   final Vector contexts, final Vector env, 
                   java.util.Map vartypes)
  { return typeCheck(typs,ents,contexts,env); } 

  public boolean typeCheck(final Vector types, 
                           final Vector entities,
                 final Vector contexts, final Vector env)
  { boolean res = true;

    if (type != null && "Ref".equals(type.getName()))
    { if (elements.size() == 1)
      { Expression e = (Expression) elements.get(0);
        e.typeCheck(types,entities,contexts,env);

        if (elementType == null) 
        { elementType = new Type("OclAny", null); } 

        System.out.println(">>> Reference type Ref(" + elementType + ") size " + e + " of type " + elementType); 
      } 
      else 
      { System.out.println(">>> Reference type Ref(" + elementType + ") size 1 of type " + elementType); 
      } 
      return res; 
    } 

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      e.typeCheck(types,entities,contexts,env);
      Entity eent = e.getEntity(); 
      if (entity == null)
      { entity = eent; } 
      else if (eent != null && Entity.isAncestor(eent,entity))
      { entity = eent; } // most general entity of the elements
    }
    // deduce element type and type itself, and the entity??

    if (isSortedMap())
    { type = new Type("Map", null); 
      type.setSorted(true); 
    } 
    else if (isMap())
    { type = new Type("Map", null); } 
    else if (ordered)
    { type = new Type("Sequence",null); }
    else if (isSorted)
    { type = new Type("Set",null); 
      type.setSorted(true); 
    }
    else
    { type = new Type("Set",null); } 

    elementType = Type.determineType(elements); 
    if (isMap())
    { elementType = Type.determineMapElementType(elements);
      type.keyType = Type.determineMapKeyType(elements); 
    } 
    // and the type.keyType

    if (elementType == null) 
    { System.out.println("! Warning: cannot determine element type of " + this);
      elementType = new Type("OclAny", null); 
    } 
    type.setElementType(elementType); 
    
    umlkind = VALUE; // ???
    multiplicity = ModelElement.MANY; 
    return res && (entity != null || elementType != null);
  }

  public Entity findEntity()
  { if (entity != null) 
    { return entity; } 
    
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Entity eent = e.getEntity(); 
      if (entity == null)
      { entity = eent; 
        System.out.println(">> Warning!!: No entity for: " + e); 
      } 
      else if (eent != null && Entity.isAncestor(eent,entity))
      { entity = eent; } // most general entity of the elements
    }

    return entity; 
  } 


  public Vector allEntitiesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res = VectorUtil.union(res,val.allEntitiesUsedIn());
    }
    return res;
  }

  public Vector allAttributesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res = VectorUtil.union(res,val.allAttributesUsedIn());
    }
    return res;
  }

  public Vector allVariableNames()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res = VectorUtil.union(res,val.allVariableNames());
    }
    return res;
  }

  public boolean relevantOccurrence(String op, Entity ent, String f,
                                    String val)
  { return false; }


  public Vector getVariableUses()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res.addAll(val.getVariableUses());
    }
    return res;
  }

  public Vector allFeaturesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allFeaturesUsedIn());
    }
    return res;
  }

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allOperationsUsedIn());
    }
    return res;
  }

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.equivalentsUsedIn());
    }
    return res;
  }

  public Vector allValuesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allValuesUsedIn());
    }
    return res;
  }

  public Vector allBinarySubexpressions() { return new Vector(); }

  public DataDependency rhsDataDependency()
  { return new DataDependency(); }  // ???

  public DataDependency getDataItems()
  { return new DataDependency(); }  // ???

  public Expression substitute(Expression old,
                               Expression n)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Expression be = e.substitute(old,n);
      elems.add(be);
    }

    SetExpression result = new SetExpression(elems,ordered);
    result.setSorted(isSorted); 

    if (isMap() || isSorted)
    { result.setType(type); }

    return result; 
  } // And for Ref. 

  public Expression substituteEq(String old,
                                 Expression n)
  { if (old.equals(this + ""))
    { return n; } 

    Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Expression be = e.substituteEq(old,n);
      elems.add(be);
    }

    SetExpression result = new SetExpression(elems,ordered);
    result.setSorted(isSorted); 

    if (isMap() || isSorted)
    { result.setType(type); }

    return result; 
  } // And for Ref. 

  public Expression removeSlicedParameters(BehaviouralFeature op, Vector fpars)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Expression be = e.removeSlicedParameters(op,fpars);
      elems.add(be);
    }
    SetExpression result = new SetExpression(elems,ordered);

    // if (isMap())

    result.setType(type); 
    result.setElementType(elementType); 

    return result; 
  } // And for Ref. 


  public boolean isOrExpression() { return false; }

  public Expression createActionForm(final Vector v)
  { return this; }

  public String toJava()
  { if (isMap())
    { String result = "(new HashMap())"; 
      for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
        Expression key = e.getLeft(); 
        Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.toJava() + "," + 
                           value.toJava() + ")";
       }
       return result; 
     }
	
    String res = "(new SystemTypes.Set())";
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      String val = e.toJava();
      res = res + ".add(" + wrap(elementType, val) + ")";
    }
    return res + ".getElements()";
  }  // ordered? Maps? Ref? 

  public String toB() { return ""; }

  public Expression toSmv(Vector cnames) { return null; }

  public String toImp(final Vector comps)
  { return ""; }

  public String toJavaImp(final Vector comps)
  { return toJava(); }

  public Expression buildJavaForm(final Vector comps)
  { return new BasicExpression(toJava()); }

  public boolean hasVariable(final String s)
  { for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (e.hasVariable(s))
      { return true; }
    }
    return false;
  }

  public Vector variablesUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.variablesUsedIn(s));
    }
    return res;
  }

  public Vector getUses(String feature)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.getUses(feature));
    }
    return res;
  }

  public Vector componentsUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.componentsUsedIn(s));
    }
    return res;
  }

  Maplet findSubexp(final String var)
  { return null; } // new Maplet(null,this) ???

  public Expression simplify(final Vector vars)
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = val.simplify(vars);
      newvals.add(newval);
    }

    SetExpression result = new SetExpression(newvals,ordered);
    result.setSorted(isSorted); 

    if (isMap() || isSorted)
    { result.setType(type); }
    return result; 
  } // could eliminate duplicates

  public Expression simplify()
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = val.simplify();
      newvals.add(newval);
    }

    SetExpression result = new SetExpression(newvals,ordered);
    result.setSorted(isSorted); 

    if (isMap() || isSorted)
    { result.setType(type); }

    return result; 
  } // could eliminate duplicates for ordered == false

  public Expression filter(final Vector vars)
  { return null; } // ???

  public Object clone()
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = (Expression) val.clone();
      newvals.add(newval);
    }

    SetExpression res = new SetExpression(newvals,ordered);
    res.setSorted(isSorted); 

    res.type = type; 
    res.elementType = elementType; 
    res.ordered = ordered; 
    res.formalParameter = formalParameter;
    res.refactorELV = refactorELV; 
 
	// if (isMap())
	// { res.setType(type); }
    return res; 
  }

  public Vector splitAnd(final Vector comps)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Vector splitOr(final Vector comps)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Expression expandMultiples(final Vector sms)
  { return this; }

  public Expression removeExpression(final Expression e)
  { if (e.equals(this))
    { return null; }
    else
    { return this; }
  }

  public boolean implies(final Expression e)
  { return equals(e); } // or a subformula?

  public boolean consistentWith(final Expression e)
  { return equals(e); } // <: consis with =, etc.

  public boolean selfConsistent(final Vector vars)
  { return true; }

  public boolean subformulaOf(final Expression e)
  { if (equals(e)) { return true; }
    if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e;
      return subformulaOf(be.left) ||
             subformulaOf(be.right);
    }
    return false;
  }

  public Expression computeNegation4succ(final Vector as)
  { return null; }  // should never be used

  public Vector computeNegation4ante(final Vector as)
  { return new Vector(); }  // should never be used

  public boolean conflictsWith(Expression e)
  { return false; } 

  public Expression invert()
  { return this; } 

  public Expression dereference(BasicExpression ref)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.dereference(ref); 
      newelems.add(newelem); 
    } 

    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
    res.isSorted = isSorted;  
    res.type = type; 
    return res;  
  } 

  public Expression addReference(BasicExpression ref, Type t)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.addReference(ref,t); 
      newelems.add(newelem); 
    } 
    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
    res.isSorted = isSorted;  
    res.type = type;  
    return res;  
  } 

  public Expression addContainerReference(
           BasicExpression ref, String var, Vector excls)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = 
            elem.addContainerReference(ref,var,excls); 
      newelems.add(newelem); 
    } 

    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
    res.isSorted = isSorted;  
    res.type = type;  
    return res;  
  } 

  public Expression replaceReference(BasicExpression ref, Type t)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.replaceReference(ref,t); 
      newelems.add(newelem); 
    } 
    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
    res.isSorted = isSorted;  
    res.type = type;  
    return res;  
  } 

  public Vector innermostVariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res.addAll(elem.innermostVariables()); 
    } 
    return res;  
  } 

  public Expression featureSetting(String var, String k, Vector l)
  { return null; } 

  public Map energyUse(Map res, Vector rUses, Vector aUses) 
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i);  
      elem.energyUse(res, rUses, aUses);
    } 

    return res; 
  }  

  public Expression simplifyOCL() 
  { Vector elems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i);  
      elems.add(elem.simplifyOCL());
    } 

    SetExpression res = (SetExpression) clone(); 
    res.elements = elems; 

    return res; 
  }  

  public java.util.Map collectionOperatorUses(int level, 
                             java.util.Map res, 
                             Vector vars)
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i);  
      elem.collectionOperatorUses(level, res, vars);
    } 

    return res; 
  }  


  public int syntacticComplexity() 
  { int res = 0;
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i);  
      res = res + elem.syntacticComplexity();
    } 
    return res + 1; 
  }  

  public int cyclomaticComplexity()
  { return 0; } 

  public void changedEntityName(String oldN, String newN)
  { } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    Vector eargs = new Vector(); 

    String arg = "";
    Vector earg = new Vector(); 

    for (int x = 0; x < elements.size(); x++)
    { Expression elem = (Expression) elements.get(x);
      String txt = elem.cg(cgs);
      arg = arg + txt;
      earg.add(elem); 
      if (x < elements.size() - 1)
      { arg = arg + ","; }
    }

    if (type != null && type.isRef() && elementType != null)
    { args.add(elementType.cg(cgs));
      eargs.add(elementType); 
    }
 
    args.add(arg);
    eargs.add(earg); 

    CGRule r = cgs.matchedSetExpressionRule(this,etext);

    System.out.println(">> Found set expression rule " + r + " for: " + etext); 
      
    if (r != null)
    { String res = r.applyRule(args,eargs,cgs);
      if (needsBracket) 
      { return "(" + res + ")"; } 
      else 
      { return res; }
    }
    return etext;
  }

}

