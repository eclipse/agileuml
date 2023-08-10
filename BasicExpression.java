import java.util.Vector; 
import java.util.StringTokenizer; 
import javax.swing.JOptionPane; 
import java.io.*; 

/******************************
* Copyright (c) 2003--2023 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
// package: OCL
  
class BasicExpression extends Expression
{ boolean isEvent = false; 
  boolean downcast = false; 

  String data; 
  Expression objectRef = null;   // can be complex expression, but normally a BasicExpression
  Expression arrayIndex = null;  // multiple indexes data[i][j] represented by data[i]->at(j)
  Type arrayType = null; // in e[i] the type of e

  int kind = Statemachine.NONE;
     // Statemachine.STATE, Statemachine.STATEMACHINE, etc
  int mult = 1;  // multiplicity of the owning statemachine
  private boolean prestate = false;  // has a @pre at end of name
  private Vector parameters = null; 
    // new Vector();  // for operation calls
  Attribute variable = null;  // in the case of umlkind == VARIABLE 

  private String atTime = "";  // e@t 

  private boolean alreadyCorrected = false; 
    // Don't correct type twice for array references. 

  BasicExpression(String dd, int i) 
  { data = dd; } 

  BasicExpression(String dd) 
  { if (dd == null) 
    { dd = ""; } 
    int preindex = dd.indexOf('@');
    if (preindex < 0)
    { data = dd;
      prestate = false;
    }
    else
    { data = dd.substring(0,preindex);
      atTime = dd.substring(preindex+1); 
      System.out.println(">> At-time expression: " + data + " at " + atTime); 
      prestate = true;
      /* if (dd.length() > preindex+4)
      { String d2 = dd.substring(preindex+5,dd.length()); 
        BasicExpression e2 = new BasicExpression(d2); 
        BasicExpression selfclone = new BasicExpression(data,0); 
        selfclone.prestate = prestate; 
        // e2.setInnerObjectRef(selfclone);
        data = e2.data; 
        e2.objectRef = selfclone;
        objectRef = selfclone;  
        prestate = e2.prestate; 
      } */ 
    }

  }

  BasicExpression(Entity e) 
  { if (e == null) 
    { data = ""; 
      return; 
    } 
    data = e.getName(); 
    umlkind = CLASSID; 
    elementType = new Type(e); 
    type = new Type("Sequence",null);
    type.setElementType(elementType); 
    multiplicity = ModelElement.MANY; 
    entity = e; 
  } 

  BasicExpression(Entity e, String ex) 
  { if (e == null) 
    { data = ""; 
      return; 
    } 
    data = ex; 
    umlkind = VARIABLE; 
    elementType = new Type(e); 
    type = elementType; 
    entity = e; 
    multiplicity = ModelElement.ONE; 
  } 

  BasicExpression(Type t, String ex) 
  { if (t == null) 
    { data = ""; 
      return; 
    } 
    data = ex; 
    umlkind = VARIABLE; 
    type = t; 
    if (t != null) 
    { multiplicity = t.typeMultiplicity(); 
      elementType = t.getElementType(); 
    } 
  } 

  BasicExpression(String v, Expression e) 
  { if (e == null) 
    { data = v; 
      return; 
    } 
    data = v; 
    umlkind = VARIABLE; 
    type = e.elementType; 
    entity = e.entity; 
    if (type != null) 
    { multiplicity = type.typeMultiplicity(); }  
  } 

  BasicExpression(Attribute att)
  { if (att == null) 
    { data = ""; 
      return; 
    } 
    data = att.getName(); 
    umlkind = VARIABLE; 
    variable = att; 

    if (att.getElementType() != null)
    { elementType = (Type) att.getElementType().clone(); }  
    if (att.getType() != null) 
    { type = (Type) att.getType().clone(); }
	 
    if (type != null && type.getElementType() == null) 
    { type.setElementType(elementType); } 
	
    entity = att.getEntity(); 
    if (type != null) 
    { multiplicity = type.typeMultiplicity(); } 
    else 
    { multiplicity = ModelElement.ONE; }  
  } 

  BasicExpression(BehaviouralFeature bf)
  { if (bf == null) 
    { data = ""; 
      return; 
    } 
    data = bf.getName(); 
    umlkind = UPDATEOP; 
    elementType = bf.getElementType(); 
    type = bf.getType(); 
    if (type != null) 
    { type.setElementType(elementType); } 
    entity = bf.getEntity(); 
    if (type != null) 
    { multiplicity = type.typeMultiplicity(); } 
    else 
    { multiplicity = ModelElement.ONE; }  
    isEvent = true; 
    Vector pars = bf.getParameters();
    parameters = new Vector();  
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      parameters.add(new BasicExpression(att));
    }  
  } 

  BasicExpression(int i)
  { data = i + ""; 
    umlkind = VALUE; 
    elementType = new Type("int",null);  
    type = new Type("int",null); 
    multiplicity = ModelElement.ONE; 
  } 

  BasicExpression(long i)
  { data = i + ""; 
    umlkind = VALUE; 
    elementType = new Type("long",null);  
    type = new Type("long",null); 
    multiplicity = ModelElement.ONE; 
  } 

  BasicExpression(double d)
  { data = d + ""; 
    umlkind = VALUE; 
    elementType = new Type("double",null);  
    type = new Type("double",null); 
    multiplicity = ModelElement.ONE; 
  } 

  BasicExpression(boolean b)
  { data = b + ""; 
    umlkind = VALUE; 
    elementType = new Type("boolean",null);  
    type = new Type("boolean",null); 
    multiplicity = ModelElement.ONE; 
  } 

  BasicExpression(Association ast)
  { if (ast == null) 
    { data = ""; 
      return; 
    } 
    data = ast.getRole2(); 
    umlkind = ROLE; 
    elementType = new Type(ast.getEntity2()); 
    type = ast.getType2();  
    if (type != null) 
    { type.setElementType(elementType); } 
    multiplicity = ast.getCard2(); 
    entity = ast.getEntity1(); 
  } 
 
  BasicExpression(Expression obj, Attribute prop) 
  { if (prop == null) 
    { data = ""; return; } 
    // if (obj == null)
    // { return new BasicExpression(prop); } 

    data = prop.getName(); 
    entity = prop.getEntity(); 
    objectRef = obj; 
    if (entity != null) 
    { if (entity.hasDefinedAttribute(data))
      { Attribute att = entity.getDefinedAttribute(data); 
        umlkind = ATTRIBUTE;
        multiplicity = ModelElement.ONE;  
        prop.setType(att.getType()); 
        prop.setElementType(att.getElementType()); 
      } 
      else if (entity.hasDefinedRole(data))
      { umlkind = ROLE; 
        Association ast = entity.getDefinedRole(data);
        multiplicity = ast.getCard2(); 
        prop.setElementType(new Type(ast.getEntity2())); 
        prop.setType(ast.getType2());  
      } 
      else // should not happen 
      { umlkind = VARIABLE; 
        variable = prop; 
      } 
    } 
    type = prop.getType(); 
    elementType = prop.getElementType(); 
    if (type != null) 
    { type.setElementType(elementType); } 
  } // may need to modify type because of obj

  BasicExpression(UnaryExpression uexp)
  { if (uexp == null) 
    { data = ""; return; } 
    objectRef = uexp.argument; 
    String op = uexp.operator; 
    if (op.startsWith("->"))
    { op = op.substring(2,op.length()); } 
    if (uexp.isMultiple())
    { multiplicity = ModelElement.MANY; } 
    data = op; 
    type = uexp.type; 
    elementType = uexp.elementType; 
  } 

  BasicExpression(Type t)
  { // if (t == null) 
    
    data = t + ""; 
    type = new Type("OclType", null);
    umlkind = TYPE;

    /* 
    if ("Set".equals(data) || "Sequence".equals(data))
    { Type tp = t.getElementType(); 
      if (tp != null)
      { // data = data + "(" + tp.getName() + ")";
        type = new Type("OclType", null);
        umlkind = TYPE;  
      } 
    } 
    else if ("Map".equals(data) || "Function".equals(data))
    { Type tp = t.getElementType(); 
      Type kt = t.getKeyType(); 
      if (kt != null) 
      { data = data + "(" + kt.getName() + ","; } 
      else
      { data = data + "(String,"; } 

      if (tp != null)
      { data = data + tp.getName() + ")"; }
      else 
      { data = data + "OclAny)"; }
 
      type = new Type("OclType", null); 
      umlkind = TYPE;
    }
    else 
    { type = new Type("OclType", null); 
      umlkind = TYPE;
    } */ 

    elementType = type; 
    multiplicity = ModelElement.MANY; 
  } 

  BasicExpression(ObjectSpecification obj) 
  { if (obj == null) 
    { data = "null"; } 
    else 
    { data = obj.getName(); }

    if (obj != null && obj.entity != null) 
    { type = new Type(obj.getEntity());
      elementType = type; 
    } 
    // multiplicity = ModelElement.MANY; 
    umlkind = VALUE; 
  } 

  BasicExpression(ASTTerm t)
  { if (t instanceof ASTBasicTerm)
    { ASTBasicTerm bt = (ASTBasicTerm) t; 
      data = bt.getTag();
      BasicExpression arg = new BasicExpression(bt.getValue());
      arg.umlkind = VALUE;   
      umlkind = QUERY;
      isEvent = true; 
      Vector pars = new Vector(); 
      pars.add(arg); 
      parameters = pars;  
    } 
    else if (t instanceof ASTSymbolTerm)
    { data = ((ASTSymbolTerm) t).getSymbol(); 
      umlkind = VALUE; 
    } 
    else if (t instanceof ASTCompositeTerm)
    { ASTCompositeTerm tree = (ASTCompositeTerm) t; 
      data = tree.getTag(); 
      umlkind = QUERY;
      isEvent = true; 
      Vector trms = tree.getTerms(); 
      Vector pars = new Vector(); 
      for (int i = 0; i < trms.size(); i++)
      { ASTTerm trm = (ASTTerm) trms.get(i); 
        Expression arg = new BasicExpression(trm); 
        pars.add(arg); 
      } 
      parameters = pars; 
    } 
  } 

  public boolean isNotLocalVariable() 
  { if (umlkind == VARIABLE && objectRef == null) 
    { return false; } 
    return true; 
  } 

  public void setVariableType(Type t) 
  { if (variable != null) 
    { variable.setType(t); } 
  } 

  public void setVariableElementType(Type et) 
  { if (variable != null) 
    { variable.setElementType(et); } 
  } 

  public static BasicExpression newASTBasicExpression(ASTTerm t)
  { BasicExpression res = new BasicExpression(""); 
    if (t instanceof ASTBasicTerm)
    { ASTBasicTerm bt = (ASTBasicTerm) t; 
      res.data = bt.getTag();
      BasicExpression arg = new BasicExpression("_1");
      Vector pars = new Vector(); 
      pars.add(arg); 
      res.parameters = pars;  
    } 
    else if (t instanceof ASTSymbolTerm)
    { res.data = "_1";
      // Vector pars = new Vector(); 
      // res.parameters = pars;  
    } 
    else if (t instanceof ASTCompositeTerm)
    { ASTCompositeTerm tree = (ASTCompositeTerm) t; 
      res.data = tree.getTag(); 
      Vector trms = tree.getTerms(); 
      Vector pars = new Vector(); 
      for (int i = 0; i < trms.size(); i++)
      { ASTTerm trm = (ASTTerm) trms.get(i); 
        String val = "_" + (i+1); 
        // if (trm instanceof ASTSymbolTerm)
        // { val = trm.literalForm(); } 
        Expression arg = new BasicExpression(val); 
        pars.add(arg); 
      } 
      res.parameters = pars; 
    } 
    return res; 
  } 



  public static BasicExpression newBasicExpression(Expression obj, String feat) 
  { BasicExpression res = new BasicExpression(feat); 
    res.objectRef = obj; 
    return res; 
  } 

  public static BasicExpression newTypeBasicExpression(String value) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = TYPE; 
    return res; 
  } 


  public static BasicExpression newValueBasicExpression(String value) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VALUE; 
    return res; 
  } 

  public static BasicExpression newValueBasicExpression(String value, String typ) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VALUE;
    if (typ != null)
    { Type tt = Type.getTypeFor(typ); 
      res.type = tt; 
    }  
    return res; 
  } 

  public static BasicExpression newAttributeBasicExpression(String value) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = ATTRIBUTE;
    return res; 
  } 

  public static BasicExpression newAttributeBasicExpression(String value, Expression obj) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = ATTRIBUTE;
    res.objectRef = obj; 
    return res; 
  } 

  public static BasicExpression newAttributeBasicExpression(String value, Type typ) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = ATTRIBUTE;
    res.type = typ; 
    return res; 
  } 

  public static BasicExpression newStaticAttributeBasicExpression(Attribute att) 
  { BasicExpression res = new BasicExpression(att.getName()); 
    res.umlkind = ATTRIBUTE;
    res.type = att.getType(); 
    res.elementType = att.getElementType(); 
    res.isStatic = true; 
    // res.variable = att; 
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(String value) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VARIABLE;
    
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(Attribute att) 
  { BasicExpression res = new BasicExpression(att); 
    res.umlkind = VARIABLE;
    res.variable = att; 
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(String f, Expression obj) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = VARIABLE;
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(String value, String typ) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VARIABLE;
    if (typ != null)
    { Type tt = Type.getTypeFor(typ); 
      res.type = tt; 
      if (tt != null) 
      { res.multiplicity = tt.typeMultiplicity(); 
        res.elementType = tt.getElementType(); 
      } 
    }  
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(
            String value, String typ, 
            Vector types, Vector entities) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VARIABLE;
    if (typ != null)
    { Type tt = Type.getTypeFor(typ,types,entities); 
      res.type = tt; 
      if (tt != null) 
      { res.multiplicity = tt.typeMultiplicity(); 
        res.elementType = tt.getElementType(); 
      } 
    }  
    return res; 
  } 

  public static BasicExpression newVariableBasicExpression(String value, Type typ) 
  { BasicExpression res = new BasicExpression(value); 
    res.umlkind = VARIABLE;
    res.type = typ; 
    if (typ != null) 
    { res.multiplicity = typ.typeMultiplicity(); 
      res.elementType = typ.getElementType();  
    }  
    return res; 
  } 

  public static BasicExpression newFunctionBasicExpression(String f, Expression obj) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = FUNCTION;
    res.parameters = new Vector(); 
    return res; 
  } 

  public static BasicExpression newFunctionBasicExpression(String f, Expression obj, Vector pars) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = FUNCTION;
    res.parameters = pars; 
    return res; 
  } 


  public static BasicExpression newFunctionBasicExpression(String f, Expression obj, Expression par) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = FUNCTION;
    Vector pars = new Vector(); 
    pars.add(par); 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newFunctionBasicExpression(String f, String obj, Vector pars) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(new BasicExpression(obj));  
    res.umlkind = FUNCTION;
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(String f) 
  { BasicExpression res = new BasicExpression(f);
    res.umlkind = UPDATEOP;
    res.isEvent = true; 
    res.parameters = new Vector(); 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(String f, Vector vars) 
  { BasicExpression res = new BasicExpression(f);
    res.umlkind = UPDATEOP;
    res.isEvent = true; 
    res.parameters = vars; 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(String f, Expression obj, Vector pars) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(String f, Expression obj, Expression par) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.parameters = new Vector();
    res.parameters.add(par);  
    res.isEvent = true; 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(String f, Expression obj) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.parameters = new Vector(); 
    res.isEvent = true; 
    return res; 
  } 

  public static BasicExpression newQueryCallBasicExpression(
                                  String f, Expression obj) 
  { BasicExpression res = new BasicExpression(f);
    res.setObjectRef(obj);  
    res.umlkind = QUERY;
    res.parameters = new Vector(); 
    res.isEvent = true; 
    return res; 
  } 

  public static BasicExpression newQueryCallBasicExpression(
                                  BehaviouralFeature bf,
                                  Expression obj,  
                                  Vector pars) 
  { BasicExpression res = new BasicExpression(bf);
    
    res.setObjectRef(obj);  
    res.umlkind = QUERY;
    res.parameters = new Vector(); 
    for (int i = 0; i < pars.size(); i++) 
    { // Assumed to be attributes 
      Attribute par = (Attribute) pars.get(i); 
      res.parameters.add(new BasicExpression(par)); 
    } 
    res.isEvent = true; 
    return res; 
  } 

  public static BasicExpression newCallBasicExpression(
                                  BehaviouralFeature bf,
                                  Expression obj,  
                                  Vector pars) 
  { BasicExpression res = new BasicExpression(bf);
    
    res.setObjectRef(obj);  
    if (bf.isQuery())
    { res.umlkind = QUERY; } 
    else 
    { res.umlkind = UPDATEOP; } 

    res.parameters = new Vector(); 
    for (int i = 0; i < pars.size(); i++) 
    { // Assumed to be attributes 
      Attribute par = (Attribute) pars.get(i); 
      res.parameters.add(new BasicExpression(par)); 
    } 
    res.isEvent = true; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                  BehaviouralFeature bf, Entity ent) 
  { BasicExpression obj = new BasicExpression(ent.getName());
    obj.umlkind = CLASSID;
    BasicExpression res = new BasicExpression(bf);  
    res.setObjectRef(obj);  
    res.entity = ent; 
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    // res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                            String f, String arg, Vector pars) 
  { BasicExpression res = new BasicExpression(f);
    BasicExpression obj = new BasicExpression(arg); 
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                        String f, Expression obj, Vector pars) 
  { BasicExpression res = new BasicExpression(f);
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                      String f, String arg, Expression par) 
  { Vector pars = new Vector(); 
    pars.add(par); 
    BasicExpression res = new BasicExpression(f);
    BasicExpression obj = new BasicExpression(arg); 
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
               String f, Expression obj, Expression par) 
  { Vector pars = new Vector(); 
    pars.add(par); 
    BasicExpression res = new BasicExpression(f);
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                                       String f, String arg) 
  { Vector pars = new Vector(); 
    
    BasicExpression res = new BasicExpression(f);
    BasicExpression obj = new BasicExpression(arg); 
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static BasicExpression newStaticCallBasicExpression(
                                    String f, Expression obj) 
  { Vector pars = new Vector(); 
    
    BasicExpression res = new BasicExpression(f);
    obj.umlkind = CLASSID; 
    res.setObjectRef(obj);  
    res.umlkind = UPDATEOP;
    res.isStatic = true; 
    res.isEvent = true; 
    res.parameters = pars; 
    return res; 
  } 

  public static Expression newIndexedBasicExpression(
                      Expression base, Expression ind)
  { if (base instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) base; 
      if (be.arrayIndex == null) 
      { BasicExpression res = (BasicExpression) be.clone(); 
        res.arrayIndex = ind; 
        return res; 
      } 
      else // multiple indexes
      { return new BinaryExpression("->at", base, ind); } 
    } 
    return new BinaryExpression("->at", base, ind);
  } 

  public static BasicExpression newStaticValueBasicExpression(String v, String cls) 
  { BasicExpression res = new BasicExpression(v); 
    res.umlkind = VALUE;
    res.isStatic = true; 
    res.objectRef = new BasicExpression(cls); 
    res.objectRef.umlkind = CLASSID; 
    return res; 
  } 

  public void addParameterExpressions(Vector pars)
  { parameters = new Vector(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute par = (Attribute) pars.get(i); 
      parameters.add(new BasicExpression(par)); 
    } 
  } 

  public Expression getObjectRef() 
  { return objectRef; } 


  public Expression getArrayIndex() 
  { return arrayIndex; } 

  public Type getArrayType() 
  { return arrayType; } 

  public boolean isCallBasicExpression()
  { return parameters != null && 
      (umlkind == QUERY || umlkind == UPDATEOP); 
  } 

  public Object clone()
  { BasicExpression res = new BasicExpression(data);
    if (javaForm != null) 
    { res.javaForm = javaForm; }  // clone it? 
    if (objectRef != null && (objectRef instanceof BasicExpression)) 
    { res.objectRef = (BasicExpression) ((BasicExpression) objectRef).clone(); }  // clone it?
    else if (objectRef != null) 
    { res.objectRef = (Expression) objectRef.clone(); } 
    res.formalParameter = formalParameter; 
 
    res.isEvent = isEvent; 
    res.modality = modality; // Surely? 
    res.kind = kind; 
    res.type = type; 
    res.umlkind = umlkind; 
    res.elementType = elementType; 
    res.entity = entity; 
    res.mult = mult; 
    res.multiplicity = multiplicity; 

    if (arrayIndex != null) 
    { res.arrayIndex = (Expression) arrayIndex.clone(); }
    res.arrayType = arrayType; 

    if (parameters == null) 
    { res.parameters = null; } 
    else 
    { res.parameters = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { if (parameters.get(i) instanceof Expression)
	    { Expression par = (Expression) parameters.get(i); 
          Expression pclone = (Expression) par.clone(); 
          res.parameters.add(pclone); 
        }
        else 
        { res.parameters.add(parameters.get(i)); }
      } 
    } 

    res.prestate = prestate;
    res.atTime = atTime;  
    res.downcast = downcast;
    res.isStatic = isStatic;  
    return res; 
  }

  public Vector singleMutants()
  { Vector res = new Vector(); 
    if ("true".equals(data))
    { res.add(new BasicExpression(false)); } 
    else if ("false".equals(data))
    { res.add(new BasicExpression(true)); } 
    return res; 
  } 

  public static boolean isMapAccess(Expression expr) 
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.arrayIndex != null && 
          be.arrayType != null && 
          be.arrayType.isMap())
      { return true; } 
    } 
    return false; 
  } 

  public static void updateMapType(Expression lhs, Expression rhs) 
  { if (lhs instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) lhs; 
      if (be.arrayIndex != null && 
          be.arrayType != null && 
          be.arrayType.isMap())
      { Type elemType = rhs.getType(); 
        Type oldElemType = be.arrayType.getElementType(); 
        be.arrayType.elementType = 
          Type.refineType(oldElemType,elemType);
        System.out.println(">> From " + lhs + " := " + rhs + " deduced element type " + be.arrayType.elementType + " for map " + be.data);

        Type indType = be.arrayIndex.getType(); 
        Type oldKeyType = be.arrayType.getKeyType(); 
        be.arrayType.keyType = 
          Type.refineType(oldKeyType,indType);  
        System.out.println(">> From " + lhs + " := " + rhs + " deduced key type " + be.arrayType.keyType + " for map " + be.data);
      } 
    } 
  } 

  public static boolean hasVariable(Expression expr) 
  { if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.variable != null)
      { return true; } 
    } 
    return false; 
  } 

  public static void updateVariableType(Expression lhs, Expression rhs) 
  { if (lhs instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) lhs; 
      if (be.variable != null)
      { Type vType = rhs.getType();

        System.out.println(">>> RHS type: " + vType); 
 
        Type oldVType = be.variable.getType(); 

        System.out.println(">>> old type: " + oldVType); 

        be.variable.setType( 
          Type.refineType(oldVType, vType));
        System.out.println(">> From " + lhs + " := " + rhs + " deduced type " + be.variable.getType() + " for " + be.data);

        Type elemType = rhs.getElementType();

        System.out.println(">>> RHS element type: " + elemType); 
 
        Type oldElemType = be.variable.getElementType(); 

        System.out.println(">>> old element type: " + oldElemType); 

        be.variable.setElementType( 
          Type.refineType(oldElemType,elemType));
        System.out.println(">> From " + lhs + " := " + rhs + " deduced element type " + be.variable.getElementType() + " for " + be.data);
      } 
    } 
  } 

  public Vector decompose()
  { // assuming it represents a navigation path r1.r2.r3.att
    Vector res = new Vector(); 
    if (objectRef == null) 
    { Attribute finalatt = new Attribute(data, type, ModelElement.INTERNAL);
      finalatt.setElementType(elementType);  
      res.add(finalatt); 
      return res; 
    } 

    Vector front = ((BasicExpression) objectRef).decompose(); 
    Attribute finalatt = new Attribute(data, type, ModelElement.INTERNAL);
    finalatt.setElementType(elementType);  
    front.add(finalatt); 
    return front;
  } 


  public BasicExpression invokeOnFirstArg()
  { if (parameters == null || parameters.size() == 0)
    { return this; }  // error
    Expression p1 = (Expression) parameters.get(0);
    Vector newparams = new Vector();
    newparams.addAll(parameters);
    newparams.remove(0);
    BasicExpression res = (BasicExpression) this.clone();
    // String newname = data.toLowerCase() + "$op";
    // res.data = newname;
    res.type = new Type("boolean", null);
    res.elementType = new Type("boolean", null);
    res.setParameters(newparams);
    res.setObjectRef(p1);
    res.umlkind = UPDATEOP; 
    return res;
  }

  public BasicExpression invokeNontopRule(String calltarget, UseCase uc, Vector rules)
  { BasicExpression res = (BasicExpression) this.clone();
    Entity ucclass = uc.getClassifier(); 
    if (ucclass == null) 
    { System.err.println("!! ERROR: null classifier for use case " + uc.getName()); } 
            
    for (int i = 0; i < rules.size(); i++) 
    { Relation rel = (Relation) rules.get(i); 
      String nme = rel.getName(); 
      if (data.equals(nme)) 
      { if (rel.isTopLevel())
        { System.err.println("!!! Error: cannot invoke top relation in where clause " + data); 
          return new BasicExpression(true); 
        } 
        else 
        { String newname = data.toLowerCase() + "$op";
          if (calltarget.length() > 0) 
          { BasicExpression p1 = new BasicExpression(calltarget);
            if (calltarget.endsWith("Pres"))
            { newname = data.toLowerCase() + "$pres"; } 
            else if (calltarget.endsWith("Con"))
            { newname = data.toLowerCase() + "$con"; } 

            p1.umlkind = CLASSID; 
            p1.setType(new Type(ucclass)); 
            p1.setElementType(new Type(ucclass)); 
            res.setObjectRef(p1);
          } 
          res.data = newname;
          res.umlkind = UPDATEOP; 
          res.isEvent = true; 
          res.entity = ucclass; 
          return res;
        } 
      } 
    } 
    System.err.println("!!! Relation " + data + " is not known in this transformation"); 
    return new BasicExpression(true);
  }

  public Expression testTraceRelation(Vector entities)
  { // data->exists( datax | datax.p1 = p1 & ... & datax.pn = pn)
    // 
    // String var = data.toLowerCase() + "$x";

    String var = Identifier.nextIdentifier("trace$");
    String traceop = data + "$trace";
    BasicExpression varexp = new BasicExpression(var);

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }
    BasicExpression eexp = new BasicExpression(ent);
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 

    Vector fparams = ent.allProperties();
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++)
      { Expression pval = (Expression) parameters.get(i);
        if (i < fparams.size())
        { Attribute fp = (Attribute) fparams.get(i);
          BasicExpression fpexp = new BasicExpression(fp);
          fpexp.objectRef = varexp;
          BinaryExpression eq = new BinaryExpression("=", fpexp, pval);
          pred = Expression.simplifyAnd(pred, eq);
        }
      } 
    }
    return new BinaryExpression("#", dran, pred);
  }

  public Expression testTraceRelationWhen(Vector entities, Vector bound)
  { // datax : data$trace & p1 = datax.p1 & ... & pn = datax.pn
    // datax : p1.traces$data$p1 & p2 = datax.p2 & ... & pn = datax.pn


    String var = Identifier.nextIdentifier("trace$");
    // String var = data.toLowerCase() + "$x";
    String traceop = data + "$trace";
    BasicExpression varexp = new BasicExpression(var);

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    varexp.setType(new Type(ent)); 
    BasicExpression eexp = new BasicExpression(ent);
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 

    int ind = 0;  

    Vector fparams = ent.allProperties();
    // System.out.println("*** TEST WHEN for " + this + " " + fparams); 

    if (fparams.size() == 0) 
    { return dran; } 

    if (parameters != null) 
    { Attribute pivot = (Attribute) fparams.get(ind); 
      Type t = pivot.getElementType();

      while (!(t.isEntity() && pivot.isSource()))
      { if (ind < fparams.size())
        { ind = ind + 1; } 
        else 
        { System.err.println("!!! ERROR: No source relation domain in rule " + this); 
          return pred; 
        }  // no source reference properties, should not occur 
        pivot = (Attribute) fparams.get(ind); 
        t = pivot.getElementType(); 
      } 

      // System.out.println("PIVOT for " + this + " is: " + pivot); 

      Entity pivotEntity = t.getEntity(); 
      Association ast = pivotEntity.getRole("traces$" + data + "$" + pivot.getName()); 

      // System.out.println("PIVOT ASSOCIATION = " + ast); 

      if (ast != null) 
      { BasicExpression astexp = new BasicExpression(ast); 
        astexp.setPrestate(true); 
        BasicExpression pivotexp = new BasicExpression(parameters.get(ind) + ""); 
        astexp.setObjectRef(pivotexp); 
        if (bound.contains(pivotexp + "")) { } 
        else 
        { bound.add(pivotexp + ""); }  
        dran = new BinaryExpression(":", varexp, astexp); 
      }         
      else 
      { System.err.println("!!!! ERROR: undefined inverse of " + pivot); } 

      for (int i = 0; i < parameters.size(); i++)
      { Expression pval = (Expression) parameters.get(i);
        if (i < fparams.size() && i != ind)
        { Attribute fp = (Attribute) fparams.get(i);
          BasicExpression fpexp = new BasicExpression(fp);
          fpexp.objectRef = varexp;
          BinaryExpression eq = new BinaryExpression("=", pval, fpexp);
          if (bound.contains(pval + "")) { } 
          else 
          { bound.add(pval + ""); }  
          pred = Expression.simplifyAnd(pred, eq);
        }
      } 
    }
    return new BinaryExpression("&", dran, pred);
  }

    
  public Expression checkWhen(Vector entities, Vector bound)
  { // # (datax : data$trace) (p1 = datax.p1 & ... & pn = datax.pn)

    String var = Identifier.nextIdentifier("trace$");
    // String var = data.toLowerCase() + "$x";
    String traceop = data + "$trace";
    BasicExpression varexp = new BasicExpression(var);
    varexp.setUmlKind(VARIABLE); 
    

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 

    Vector fparams = ent.allProperties();
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++)
      { Expression pval = (Expression) parameters.get(i);
        if (i < fparams.size())
        { Attribute fp = (Attribute) fparams.get(i);
          BasicExpression fpexp = new BasicExpression(fp);
          fpexp.objectRef = varexp;
          BinaryExpression eq = new BinaryExpression("=", pval, fpexp);
          if (bound.contains(pval + "")) { } 
          else 
          { bound.add(pval + ""); }  
          pred = Expression.simplifyAnd(pred, eq);
        }
      } 
    }
    return new BinaryExpression("#", dran, pred);
  }

  public void setIsEvent()
  { isEvent = true; 
    modality = HASEVENT; 
  } 

  public void setData(String d)
  { data = d; } 

  public void setParameters(Vector pars)
  { parameters = pars; } 

  public void setParameter(int ind, Expression par)
  { if (parameters == null) 
    { parameters = new Vector(); }
    if (parameters.size() >= ind)
    { parameters.set(ind-1,par); } 
    else 
    { parameters.add(par); }  
  }  

  public void addParameter(Expression v)
  { if (parameters == null)
    { parameters = new Vector(); } 
    parameters.add(v); 
  } 

  public Vector getParameters()
  { return parameters; } 

  public Expression getParameter(int i) 
  { if (parameters == null) 
    { return null; } 
     
    if (i <= parameters.size() && i >= 1) 
    { return (Expression) parameters.get(i-1); } 
    return null; 
  } 

  public int arity() 
  { if (parameters == null) 
    { return 0; } 
    return parameters.size(); 
  } 

  public String getData()
  { return data; } 

  public boolean getPrestate()
  { return prestate; } 

  public Type getElementType()
  { if (isString(data))
    { return type; } 
    else 
    { return elementType; } 
  } 

  public void setPrestate(boolean p) 
  { prestate = p; } 

  public void setPre()
  { if (umlkind == VALUE || umlkind == CONSTANT || umlkind == VARIABLE ||
        umlkind == UPDATEOP) 
    { return; } 

    if (umlkind == FUNCTION || umlkind == QUERY) 
    { } 
    else 
    { setPrestate(true); } 

    if (objectRef != null) 
    { objectRef.setPre(); } 

    if (arrayIndex != null) 
    { arrayIndex.setPre(); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++)
      { Expression pval = (Expression) parameters.get(i);
        pval.setPre(); 
      } 
    }
  } // parameters and arrayIndex in all cases? 


  public boolean isReduceFunctionCall()  // Sequence input, single value output
  { if (parameters == null || parameters.size() == 0) { return false; } 
    else if (entity != null)  
    { BehaviouralFeature bf = entity.getDefinedOperation(data);  
      if (bf == null) { return false; } 
      Attribute p1 = bf.getParameter(0); 
      if (Type.isSequenceType(p1.getType()) && !Type.isCollectionType(type))
      { return true; } 
    } 
    return false; 
  } // umlkind == QUERYOP

  public Expression convertExcelExpression()  // Sequence-valued E.Att becomes E->collect(Att)
  { if (parameters == null || parameters.size() == 0) { return this; } 
    else if (entity != null) 
    { BasicExpression res = (BasicExpression) clone(); 
      BehaviouralFeature bf = entity.getDefinedOperation(data);  
      if (bf == null) { return this; } 
      // Attribute p1 = bf.getParameter(0); 
            
      Vector newparams = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute par = bf.getParameter(i);
        Expression apar = (Expression) parameters.get(i); 
 
        if (Type.isSequenceType(par.getType()) && apar instanceof BasicExpression)
        { BasicExpression bpar = (BasicExpression) apar; 
          if (bpar.objectRef != null && bpar.objectRef.umlkind == CLASSID && bpar.umlkind == ATTRIBUTE)
          { BasicExpression paratt = (BasicExpression) bpar.clone(); 
            BasicExpression ref = (BasicExpression) bpar.objectRef; 
            paratt.objectRef = null; 
            Expression newpar = new BinaryExpression("->collect", ref, paratt); 
            if (ref.prestate)
            { paratt.prestate = true; } 
            newparams.add(newpar); 
          }
          else 
          { newparams.add(apar); } 
        }  
        else // single value expected
        if (apar instanceof BasicExpression)  
        { BasicExpression bpar = (BasicExpression) apar; 
          if (bpar.objectRef != null && bpar.objectRef.umlkind == CLASSID && bpar.umlkind == ATTRIBUTE)
          { BasicExpression paratt = (BasicExpression) bpar.clone(); 
            BasicExpression ref = (BasicExpression) bpar.objectRef; 
            String evarname = ref.data.toLowerCase() + "x"; 
            paratt.objectRef = new BasicExpression(ref.entity, evarname); 
            if (ref.prestate)
            { paratt.prestate = true; } 
            newparams.add(paratt); 
          }
          else 
          { newparams.add(apar); }
        } 
        else 
        { newparams.add(apar); }  
      } 
      res.parameters = newparams; 
      return res; 
    } 
    return this; 
  } 

  public Expression convertExcelExpression(Entity target, Vector entities, Vector qvars, Vector antes)     { if (parameters == null || parameters.size() == 0) { return this; } 
    else 
    { BasicExpression res = (BasicExpression) clone(); 
      
      
      Vector newparams = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression bpar = par.excel2Ocl(target, entities, qvars, antes); 
        newparams.add(bpar); 
      } 
      res.parameters = newparams; 
      return res; 
    } 
  } 


  public boolean isQualified()
  { if (umlkind == ROLE) 
    { Association ast = entity.getDefinedRole(data); 
      if (ast != null && ast.isQualified())
      { return true; } 
    } 
    return false; 
  } // Or its type is a map ...

  public boolean isZeroOneRole()
  { if (umlkind == ROLE || umlkind == ATTRIBUTE) 
    { Association ast = entity.getDefinedRole(data); 
      if (ast != null && ast.getCard2() == ModelElement.ZEROONE)
      { return true; } 
    } 
    return false; 
  } 

  // public boolean isZeroOne()
  // { return lower == 0 && upper == 1; } 


  public boolean isRuleCall(Vector rules)
  { for (int i = 0; i < rules.size(); i++) 
    { NamedElement ne = (NamedElement) rules.get(i); 
      if (data.equals(ne.getName()))
      { return true; } 
    }
    return false; 
  }  

  public NamedElement getCalledRule(Vector rules)
  { for (int i = 0; i < rules.size(); i++) 
    { NamedElement ne = (NamedElement) rules.get(i); 
      if (data.equals(ne.getName()))
      { return ne; } 
    }
    return null; 
  }  

  public boolean isAssignable()
  { if (umlkind == VARIABLE)
    { return true; } 
    if (umlkind == ROLE || umlkind == ATTRIBUTE)
    { if (entity != null && entity.isFrozen(data))
      { return false; } 
      return true; 
    } 
    return false; 
  } // CLASSID also, in special cases: C = {}. And the ROLE is not frozen

  public boolean isExecutable()
  { if (umlkind == UPDATEOP || umlkind == QUERY)
    { return true; }
    if (("" + this).equals("true"))
    { return true; }  
    return false; 
  } // d.isDeleted not valid syntax.

  public boolean isPrimitive()  // A number, boolean or enumeration value
  { if (umlkind == ROLE || umlkind == CLASSID) { return false; } 
    // if (arrayIndex != null) 
    // { return (elementType != null && elementType.isPrimitive()); } 

    // if (isMultiple()) 
    // { return false; } 

    if (umlkind == FUNCTION) 
    { if (data.equals("sqr") || data.equals("size") || 
          data.equals("floor") || data.equals("ceil") ||
          data.equals("round") || 
          data.equals("exp") || data.equals("pow") ||
          data.equals("sin") || data.equals("cos") || 
          data.equals("tan") ||
          data.equals("asin") || data.equals("acos") || 
          data.equals("atan") ||
          data.equals("sinh") || data.equals("cosh") || 
          data.equals("tanh") ||
          data.equals("log10") || data.equals("cbrt") || 
          data.equals("Prd") || data.equals("prd") || 
          data.equals("log") || data.equals("count") || 
          data.equals("hasPrefix") ||
          data.equals("hasSuffix") || 
          data.equals("isEmpty") || data.equals("notEmpty") ||
          data.equals("isUnique") || 
          data.equals("oclIsKindOf") || 
          data.equals("sqrt") || data.equals("abs") ||
          data.equals("indexOf") || data.equals("isDeleted"))
      { return true; }
      else if (data.equals("Sum"))
      { return Type.primitiveType(type); } 
      else if (data.equals("oclAsType"))
      { String cast = "" + parameters.get(0); 
        if ("int".equals(cast) || "boolean".equals(cast) || "long".equals(cast) || 
            "double".equals(cast))
        { return true; }
        return false; 
      }  
      else if (data.equals("toLowerCase") || data.equals("toUpperCase") || 
               data.equals("flatten") || data.equals("sortedBy") || 
               data.equals("reverse") || data.equals("sort") || data.equals("asSequence") || 
               data.equals("front") || data.equals("tail") || data.equals("insertAt") || 
               data.equals("subrange") ||
               data.equals("setAt") ||
               data.equals("replace") || data.equals("replaceAll") || 
               data.equals("replaceAllMatches") ||
               data.equals("characters") || data.equals("allInstances") || 
               data.equals("closure") || data.equals("asSet") || data.equals("subcollections"))
      { return false; }   // but strings don't need to be wrapped 
      else if (data.equals("max") || data.equals("min") || data.equals("last") ||
               data.equals("sum") || data.equals("first") || data.equals("any"))
      { if (objectRef != null) 
        { Type objstype = objectRef.getElementType(); 
          return Type.primitiveType(objstype);
        } 
        return false;  
      } 
    }  // not prim if objectRef multiple and data : {sqr, sqrt, floor, exp, sqrt,
       // abs, toLower, toUpper

    if (umlkind == VALUE || umlkind == VARIABLE || umlkind == CONSTANT)
    { return Type.primitiveType(type); } 

    if (umlkind == ATTRIBUTE || umlkind == QUERY || umlkind == UPDATEOP)
    { // Look it up

      if (objectRef == null)
      { if (arrayIndex != null) 
        { return Type.primitiveType(elementType); } 
        return Type.primitiveType(type); 
      } 
      else if (entity != null) 
      { if (entity.isStaticFeature(data))
        { if (entity.getName().equals(objectRef + ""))
          { return Type.primitiveType(type); } 
        } 
      } 

      if (arrayIndex != null) 
      { return Type.primitiveType(elementType); } 
      
      if (!objectRef.isMultiple())  
      { return Type.primitiveType(type); } 
   
       // if objectRef not multiple
    }
    return false;
  }

  public Expression addContainerReference(BasicExpression ref, 
                                          String var,
                                          Vector exclusions)
  { // var becomes ref.var unless var : exclusions
    BasicExpression res = (BasicExpression) clone(); 

    if ("self".equals(data) && objectRef == null)
    { return res; }         // I would have thought? 

    if (arrayIndex != null)
    { Expression arInd = 
        arrayIndex.addContainerReference(ref,var,exclusions);
      res.arrayIndex = arInd; 
    }   // and to parameters of calls

    if (parameters != null) 
    { Vector pars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression rpar = 
             par.addContainerReference(ref,var,exclusions); 
        pars.add(rpar);   
      } 
      res.parameters = pars; 
    } 

    if (data.equals(var)) { } 
    else if (objectRef == null) 
    { return res; } 
    else 
    { Expression oref1 = 
           objectRef.addContainerReference(ref,var,exclusions);
      res.setObjectRef(oref1);
      return res; 
    }   

    if (exclusions.contains(data))
    { return res; }
 
    if (objectRef == null) 
    { res.setObjectRef(ref);
      return res; 
    }   
    else 
    { Expression oref1 = 
           objectRef.addContainerReference(ref,var,exclusions);
      res.setObjectRef(oref1);
      return res; 
    }

  } 

  public Expression addReference(BasicExpression ref, Type t)
  { BasicExpression res = (BasicExpression) clone(); 
    // if (umlkind == FUNCTION) 
    // { Expression oref1 = objectRef.addReference(ref,t);
    //   res.setObjectRef(oref1);
    //   return res; 
    // }

    if ("self".equals(data) && objectRef == null)
    { return ref; }         // I would have thought? 

    if (arrayIndex != null)
    { Expression arInd = arrayIndex.addReference(ref,t);
      res.arrayIndex = arInd; 
    }   // and to parameters of calls

    if (parameters != null) 
    { Vector pars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression rpar = par.addReference(ref,t); 
        pars.add(rpar);   
      } 
      res.parameters = pars; 
    } 

    if (umlkind == ATTRIBUTE || umlkind == ROLE || umlkind == FUNCTION ||
        umlkind == CONSTANT || umlkind == QUERY || umlkind == UPDATEOP)
    { if (objectRef == null) //  && (objectRef.isMultiple()))
      { res.setObjectRef(ref);
        return res; 
      }  // only if it is a feature of t. What about variables? 
      else 
      { Expression oref1 = objectRef.addReference(ref,t);
        res.setObjectRef(oref1);
        return res; 
      }
    }
    return res;
  }

  public Expression replaceReference(BasicExpression ref, Type t)
  { BasicExpression res = (BasicExpression) clone(); 
    // if (umlkind == FUNCTION) 
    // { Expression oref1 = objectRef.addReference(ref,t);
    //   res.setObjectRef(oref1);
    //   return res; 
    // }

    if ("self".equals(data) && objectRef == null)
    { return ref; }         // I would have thought? 

    if (arrayIndex != null)
    { Expression arInd = arrayIndex.replaceReference(ref,t);
      res.arrayIndex = arInd; 
    }   // and to parameters of calls

    if (parameters != null) 
    { Vector pars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression rpar = par.replaceReference(ref,t); 
        pars.add(rpar);   
      } 
      res.parameters = pars; 
    } 

    if (umlkind == ATTRIBUTE || umlkind == ROLE || umlkind == FUNCTION ||
        umlkind == CONSTANT || umlkind == QUERY || umlkind == UPDATEOP)
    { if (objectRef == null) //  && (objectRef.isMultiple()))
      { res.setObjectRef(ref);
        return res; 
      }  // only if it is a feature of t. What about variables? 
      else if (entity == t.entity) 
      { res.setObjectRef(ref);
        return res; 
      }
    }
    return res;
  }

  public Expression dereference(BasicExpression ref)
  { // Clone this? 
    BasicExpression res = (BasicExpression) clone(); 
    
    if (arrayIndex != null) 
    { res.arrayIndex = arrayIndex.dereference(ref); } 

    if (parameters != null) 
    { Vector pars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression par1 = par.dereference(ref); 
        pars.add(par1); 
      } 
      res.parameters = pars; 
    } 
    
    if (objectRef == null)
    { if (data.equals("" + ref))
      { res.data = "self"; 
        res.setEntity(entity);
        res.setType(type);
        res.setElementType(elementType);  
        res.setUmlKind(VARIABLE);   
        res.variable = new Attribute("self", res.type, ModelElement.INTERNAL); 
        return res; 
      } 
    } 
    else if (("" + objectRef).equals("" + ref))
    { res.objectRef = null; 
      return res; 
    } // objectRef.data = "self"
    else 
    { res.objectRef = objectRef.dereference(ref); } 

    return res; 
  } // also dereference parameters of operation calls

  public Expression invert()
  { return (Expression) clone(); }     
  // For all of these also update the array ref

  public void findClones(java.util.Map clones, String rule, String op)
  { return; } 

  public void findClones(java.util.Map clones, 
                         java.util.Map cloneDefs, 
                         String rule, String op)
  { return; } 

  public void findMagicNumbers(java.util.Map mgns, String rule, String op)
  { if (umlkind == VALUE)
    { if ("0".equals(data) || "1".equals(data) || 
          "\"\"".equals(data) || "true".equals(data) ||
          "false".equals(data) || 
          "null".equals(data) ||
          "1.0".equals(data) ||
          "0.0".equals(data))
      { return; }

      // Or if type is enumerated, also ok. 
      if (type != null && type.isEnumerated())
      { return; } 

      System.err.println("!! Magic number: " + this + " in " + op + " context: " + rule); 
      Vector occs = (Vector) mgns.get(data); 
      if (occs == null) 
      { occs = new Vector(); } 
      occs.add(this); 
      mgns.put(data, occs);
      return;  
    } 

    if (objectRef != null) 
    { objectRef.findMagicNumbers(mgns, this + "", op); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        par.findMagicNumbers(mgns, this + "", op);
      }
    }  
      
    // and array index

    return; 
  } 

     

  public boolean isOrdered()   // assume isMultiple()
  { if (umlkind == VARIABLE) 
    { if (type != null)
      { return "Sequence".equals(type.getName()); } 
      return false;
    } 

    if (umlkind == CLASSID) 
    { if (arrayIndex == null || arrayIndex.isOrdered())
      { return true; } 
      else 
      { return false; }
    } 

    if (umlkind == FUNCTION)
    { if (data.equals("asSet") || data.equals("closure") || 
          data.equals("subcollections"))
      { return false; } 
      
      if (data.equals("sort") || data.equals("allInstances") || data.equals("asSequence") ||
          data.equals("sortedBy") || data.equals("characters"))
      { return true; } 
      
      if (objectRef == null) 
      { return false; } // invalid anyway

      if (data.equals("any") || data.equals("first") || data.equals("last"))
      { return Type.isSequenceType(elementType); } 

      if (objectRef.isOrdered())
      { if (data.equals("front") || data.equals("reverse") || 
            data.equals("tail") ||
            data.equals("insertInto") ||
            data.equals("insertAt") || data.equals("setAt") ||
            data.equals("subrange"))
        { return true; } 
      } 
      else if ("Integer".equals("" + objectRef) && "subrange".equals(data))
      { return true; } 

      return false; 
    } 

    if (objectRef == null || !objectRef.isMultiple())  // singletons are ordered?
    { if (umlkind == ROLE && entity != null) 
      { Association ast = entity.getDefinedRole(data); 
        if (ast != null) 
        { return ast.isOrdered(); } 
        // else 
        // { return result; } 
      } 
      return false;
    }

    if (objectRef.isOrdered())
    { if (umlkind == ATTRIBUTE)
      { return !Type.isSetType(type); }   // was: true; 

      if (umlkind == ROLE && entity != null)
      { Association ast = entity.getDefinedRole(data); 
        if (ast != null) 
        { return (ast.isOrdered() || ast.isSingleValued()); }  
      }

      if (umlkind == QUERY || umlkind == UPDATEOP) 
      { if (type == null) { return false; } 
        BehaviouralFeature op = entity.getDefinedOperation(data); 
        if (op != null)  
        { if (op.isOrdered() || op.isSingleValued())
          { return true; } 
        }    
      } // the result type of the op is needed in type. 
    }

    return false;  // but it could be a singleton
  }

  public boolean isOrderedB()   // assume isMultiple()
  { if (umlkind == VARIABLE) 
    { if (type != null)
      { return "Sequence".equals(type.getName()); } 
      return false;
    } // or return false if "Set".equals(type.getName())

    if (umlkind == CLASSID) 
    { return false; }  // strange, could be a sequence if the arrayInd is? 

    if (umlkind == FUNCTION)
    { if (data.equals("asSet") || data.equals("closure") || 
          data.equals("subcollections"))
      { return false; } 

      if (data.equals("sort") || data.equals("allInstances") || data.equals("asSequence") ||
          data.equals("sortedBy") || data.equals("characters"))
      { return true; } 

      if (objectRef == null) { return false; } // invalid anyway

      if (objectRef.isOrderedB())
      { if (data.equals("front") || data.equals("reverse") || 
            data.equals("tail") ||
            data.equals("insertAt") ||
            data.equals("insertInto") ||
            data.equals("setAt") || data.equals("subrange"))
        { return true; } 
      } 
      else if ("Integer".equals("" + objectRef) && "subrange".equals(data))
      { return true; } 

      return false; 
    } 

    if (objectRef == null || !objectRef.isMultiple())  // singletons are ordered?
    { if (umlkind == ROLE && entity != null) 
      { Association ast = entity.getDefinedRole(data); 
        if (ast != null) 
        { return ast.isOrdered(); } 
      } 
      return false;
    }

    if (objectRef.isOrderedB())
    { if (umlkind == ATTRIBUTE)
      { return true; } // but could be set-valued

      if (umlkind == ROLE && entity != null)
      { Association ast = entity.getDefinedRole(data); 
        if (ast != null) 
        { return (ast.isOrdered() || ast.isSingleValued()); }  
      }

      if (umlkind == QUERY || umlkind == UPDATEOP) 
      { if (type == null) { return false; } 
        BehaviouralFeature op = entity.getDefinedOperation(data); 
        if (op != null)  
        { if (op.isOrdered() || op.isSingleValued())
          { return true; } 
        }    
      } // the result type of the op is needed in type. 
    }

    if (type != null)
    { return "Sequence".equals(type.getName()); } 
      
    return false;  // but it could be a singleton
  }

  public boolean isSorted()   // assume isMultiple()
  { // if (umlkind == VARIABLE) 
    // { if (type != null)
    //   { return "Sequence".equals(type.getName()); } 
    //   return false;
    // } 

    if (umlkind == FUNCTION)
    { if (data.equals("asSet") || data.equals("closure") || 
          data.equals("subcollections"))
      { return false; } 

      if (data.equals("sort") || data.equals("sortedBy"))
      { return true; } 

      if (objectRef == null) { return false; } // invalid anyway

      if (objectRef.isSorted())
      { if (data.equals("front") || data.equals("reverse") || data.equals("tail") ||
            // data.equals("removeAt") || 
            data.equals("subrange"))
        { return true; } 
      } 
      else if ("Integer".equals("" + objectRef) && "subrange".equals(data))
      { return true; } 

      return false; 
    } 

    if (objectRef == null || !objectRef.isMultiple())  // singletons are ordered?
    { if (umlkind == ROLE && entity != null) 
      { Association ast = entity.getDefinedRole(data); 
        if (ast != null) 
        { return ast.isSorted(); }  
      } 
      return false;
    }

    if (objectRef.isSorted())
    { if (umlkind == ATTRIBUTE && multiplicity == ModelElement.ONE)
      { return true; }  // if single-valued
    }

    if (objectRef == null && (umlkind == QUERY || umlkind == UPDATEOP)) 
    { if (type == null) { return false; } 
      BehaviouralFeature op = entity.getDefinedOperation(data); 
      return op.isSorted(); 
    } // the result type of the op is needed in type. 
    return false;  // but it could be a singleton
  }

  public int maxModality() { return modality; } 

  public int minModality() { return modality; } 

  public Vector allEntitiesUsedIn()
  { Vector res = new Vector();
    if (entity != null && isFeature())
    { res.add(entity); }
    // Perhaps also variables of type entity??? 
   
    if (objectRef != null)
    { res = VectorUtil.union(res,objectRef.allEntitiesUsedIn()); } 

    if (arrayIndex != null) 
    { res = VectorUtil.union(res,arrayIndex.allEntitiesUsedIn()); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = VectorUtil.union(res,par.allEntitiesUsedIn()); 
      } 
    } 

    return res;
  } // and parameters or arrayIndex?

  public Vector metavariables()
  { Vector res = new Vector();
   
    if (objectRef != null)
    { res = objectRef.metavariables(); } 

    for (int i = 0; i < data.length() - 1; i++) 
    { if ('_' == data.charAt(i) && 
          Character.isDigit(data.charAt(i+1)) && 
          i + 2 < data.length() && 
          Character.isDigit(data.charAt(i+2))) 
      { res.add(data.charAt(i) + "" + data.charAt(i+1) + "" + data.charAt(i+2)); }
      else if ('_' == data.charAt(i) && 
               Character.isDigit(data.charAt(i+1))) 
      { res.add(data.charAt(i) + "" + data.charAt(i+1)); }
      else if ('_' == data.charAt(i) && 
               '*' == data.charAt(i+1)) 
      { res.add(data.charAt(i) + "*"); }
      else if ('_' == data.charAt(i) && 
               '+' == data.charAt(i+1)) 
      { res.add(data.charAt(i) + "+"); }
    }  

    if (arrayIndex != null) 
    { res = VectorUtil.union(res,arrayIndex.metavariables()); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = VectorUtil.union(res,par.metavariables()); 
      } 
    } 


    return res;
  } // and parameters or arrayIndex?

  public Vector innermostEntities()
  { Vector res = new Vector(); 
    if (objectRef == null)
    { if (isFeature() && entity != null) 
      { res.add(entity); }  // also the superclasses? 
    }
    else 
    { res = objectRef.innermostEntities(); }

    if (arrayIndex != null) 
    { res = VectorUtil.union(res,arrayIndex.innermostEntities()); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = VectorUtil.union(res,par.innermostEntities()); 
      } 
    } 

    return res;  
  } // parameters, arrayIndex also 

  public String innermostData()
  { if (objectRef == null) 
    { return data; } 
    if (objectRef instanceof BasicExpression)
    { return ((BasicExpression) objectRef).innermostData(); } 
    return "";  
  } 

  public Vector innermostVariables()
  { Vector res = new Vector(); 
    if (umlkind == ATTRIBUTE || umlkind == ROLE || 
        umlkind == UPDATEOP || umlkind == QUERY) 
    { if (objectRef == null) // and feature isn't static 
      { res.add("self"); }
      else if (objectRef.umlkind == VARIABLE)
      { res.add(objectRef + ""); } 
      else 
      { res.addAll(objectRef.innermostVariables()); }
    }
    else if (umlkind == FUNCTION)
    { res.addAll(objectRef.innermostVariables()); } 
    else if (umlkind == VARIABLE) 
    { res.add(data); } 
    
    if (arrayIndex != null) 
    { res = VectorUtil.union(res,arrayIndex.innermostVariables()); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = VectorUtil.union(res,par.innermostVariables()); 
      } 
    } 

    return res; 
  } // parameters, arrayIndex also 

  public Vector allPreTerms()
  { Vector res = new Vector();
    
    if (prestate)
    { res.add(this); }
    if (objectRef != null) 
    { res.addAll(objectRef.allPreTerms()); } 
    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allPreTerms()); } 
     

    if ("Integer".equals(objectRef + "") && "Sum".equals(data))
    { // ignore pre-terms inside the 4th argument
      for (int i = 0; i < parameters.size() - 1; i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allPreTerms()); 
      }
    } 
    else if ("Integer".equals(objectRef + "") && "Prd".equals(data))
    { // ignore pre-terms inside the 4th argument
      for (int i = 0; i < parameters.size() - 1; i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allPreTerms()); 
      }
    } 
    else if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allPreTerms()); 
      } 
    } 
    return res;
  }  // and in object ref?

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    if (prestate && var.equals(objectRef + ""))
    { res.add(this); }
    else if (objectRef != null) 
    { res.addAll(objectRef.allPreTerms(var)); } 

    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allPreTerms(var)); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allPreTerms(var)); 
      } 
    } 
    return res;
  }  // and in object ref?

  public boolean relevantOccurrence(String op, Entity ent, 
                                    String val,
                                    String f)
  { if (data.equals(f)) 
    { if (objectRef == null) 
      { return true; } 
      else 
      { Type elemT = objectRef.elementType; 
        // System.out.println("Element type of " + objectRef + " is: " + 
        //                    elemT); 
        // System.out.println("Operation entity is: " + ent); 
        if (elemT == null) // a variable
        { elemT = objectRef.getType(); 
          if (elemT.entity == null) { return false; }
        }
        Entity et = elemT.getEntity();
        if (Entity.comparable(et,ent)) { return true; } 
        return false; 
      } 
    } 
    if (objectRef == null) { return false; } 
    return objectRef.relevantOccurrence(op,ent,val,f); 
  } // But ignore it if object ref type (an entity) isn't comparable to src
  // and look in arrayIndex or parameters? 

  // Can't have {} inside () or vice-versa

  public Expression checkIfSetExpression() 
  { int n = data.length();
    if (n > 0 &&
        data.charAt(n-1) == '}')
    { System.out.println(">>> Found set expression: " + this); 
      return buildSetExpression(data);
    }
    // return checkIfObjectExpression();
    // return checkIfObjectRef();
    return checkIfCallExpression();  
  } // should start with Set{, Sequence{ or {

  private static SetExpression buildSetExpression(String d)
  { StringTokenizer st =
      new StringTokenizer(d,"{},");
    Vector ids = new Vector();
    while (st.hasMoreTokens())
    { String s = st.nextToken();
      BasicExpression be = new BasicExpression(s);
      ids.add(be.checkIfObjectRef());  // can't allow . inside {} at present
    }

    if (ids.size() > 0 && "Sequence".equals("" + ids.get(0)))
    { ids.remove(0); 
      return new SetExpression(ids,true); 
    }

    if (ids.size() > 0 && "Set".equals("" + ids.get(0)))
    { ids.remove(0); 
      return new SetExpression(ids,false); 
    }

    return new SetExpression(ids,false);
  }

  public BasicExpression checkIfCallExpression() 
  { int n = data.length();
    int i = data.indexOf('('); 
    if (n > 0 &&
        data.charAt(n-1) == ')' &&
        i > 0)
    { // System.out.println("Found call expression: " + this); 
      return buildCallExpression(data);
    }
    return checkIfObjectRef(); 
  }

  private static BasicExpression buildCallExpression(String d)
  { StringTokenizer st =
      new StringTokenizer(d,"(),");
    Vector ids = new Vector();
    while (st.hasMoreTokens())
    { String s = st.nextToken();
      Compiler2 c2 = new Compiler2();
      c2.nospacelexicalanalysis(s);  
      Expression be1 = c2.parse(); 
      // BasicExpression be = new BasicExpression(s);
      // BasicExpression be1 = (BasicExpression) be.checkIfObjectRef(); 
      // be1.setPrestate(be.getPrestate()); 
      if (be1 != null) { ids.add(be1); }   // can't allow . inside {} at present
      else 
      { System.err.println("ERROR: Invalid expression " + s + " in call " + d); } 
    }

    if (ids.size() > 0 && (ids.get(0) instanceof BasicExpression)) { } 
    else 
    { System.err.println("ERROR: Invalid call expression: " + d); 
      return null; 
    } 

    BasicExpression op = (BasicExpression) ids.get(0); 
    ids.remove(0);

    if (isFunction(op.data))
    { op.umlkind = FUNCTION; } 
    else 
    { op.setIsEvent(); } 
 
    op.setParameters(ids);  
    return op;
  }
  // can't have calls within calls: op(op1(x,y),z) 

/*   public Expression checkIfObjectExpression()
  { Vector ids = new Vector();
    StringTokenizer st = 
      new StringTokenizer(data,"(),");
    while (st.hasMoreTokens())
    { ids.add(st.nextToken());
      System.out.println(ids);
    }
    if (ids.size() > 1)  // ObjectExpression  data.charAt(0) == '('
    { return buildObjectExpression(ids); }
    else 
    { return checkIfObjectRef(); }
  }

  public static Expression buildObjectExpression(Vector ids)
  { String ent = (String) ids.get(0);
    Vector res = new Vector();
    ids.remove(0);
    for (int i = 0; i < ids.size(); i++)
    { String e = (String) ids.get(i);
      BasicExpression be = new BasicExpression(e);
      res.add(be.checkIfObjectRef());
    }
    return new ObjectExpression(ent,res);
  }  */ 

  public BasicExpression checkIfObjectRef()
  { Vector ids = new Vector();
    BasicExpression res; 

    /* StringTokenizer st =
      new StringTokenizer(data,".");
 
    while (st.hasMoreTokens())
    { ids.add(st.nextToken()); } */ 

    ids = topLevelSplit(data);     

    // System.out.println("Top level split of " + data + " is " + ids); 
  
    if (ids.size() > 1)
    { try 
      { double dd = Double.parseDouble(ids.get(0) + "." + ids.get(1)); 
        BasicExpression doub = new BasicExpression(dd); 
        ids.remove(0); 
        ids.remove(0);
        if (ids.size() == 0) { return doub; }  
        BasicExpression ref = (BasicExpression) buildObjectRef(ids);
        ref.setObjectRef(doub); 
        return ref; 
      } catch (Exception ee) { } 
      res = buildObjectRef(ids);
      // res.prestate = prestate;  
      // System.out.println("MAIN EXPRESSION IS: " + res); 
      return res; 
    }
    else 
    { res = checkIfArrayRef(data); 
      // res.prestate = prestate;
      // System.out.println("INNER EXPRESSION IS: " + res); 
      return res;  
    }
  }

  private static BasicExpression buildObjectRef(Vector ids)
  { /** Precondition: ids.size() > 0 */
    int n = ids.size();
    if (n == 0) { return null; }
    if (n == 1)
    { return checkIfArrayRef((String) ids.get(0)); }
    String last = (String) ids.get(n - 1);
    BasicExpression ee = new BasicExpression(last,0); 
    BasicExpression res = // new BasicExpression(last);
                          ee.checkIfCallExpression(); 
    Vector rem = new Vector(); 
    rem.addAll(ids); 
    // Vector rem = (Vector) ids.clone();
    rem.remove(n-1);
    Expression obj = buildObjectRef(rem);
    res.objectRef = obj; 
    // System.out.println("Built object ref " + res); 
    return res;
  }

  private static BasicExpression checkIfArrayRef(String d)
  { int n = d.length();
    BasicExpression res; 
    if (n > 0 && d.charAt(n-1) == ']')
    { // System.out.println("Found array expression: " + d); 
      StringTokenizer st = new StringTokenizer(d,"[]");
      Vector ids = new Vector();
      while (st.hasMoreTokens())
      { ids.add(st.nextToken()); }
      String basic = (String) ids.get(0);
      BasicExpression index;
      if (ids.size() > 0)
      { BasicExpression inde = 
          new BasicExpression((String) ids.get(1), 0); 
        index = inde.checkIfObjectRef(); 
      }
      else // syntax error
      { index = null; }
      res = new BasicExpression(basic,0);
      BasicExpression res1 = res.checkIfCallExpression(); 
      res1.arrayIndex = index;
      return res1;
    }
    res = new BasicExpression(d);
     
    // System.out.println("Build array ref: " + res + " from " + d);
    return res;  
  }  // does not deal with qualified associations with asst[i,j]


  public void setObjectRef(Expression e) 
  { objectRef = e; } 

  public void setInnerObjectRef(Expression e) 
  { if (objectRef == null) 
    { objectRef = e; }
    else if (objectRef instanceof BasicExpression)
    { ((BasicExpression) objectRef).setInnerObjectRef(e); } 
  }  

  public void setArrayIndex(Expression ind)
  { arrayIndex = ind; } 

  public String toString() // the RSDS form ref.data
  { String res;
    if (objectRef != null)
    { res = objectRef + "." + data; } 
    else 
    { res = data; } 

    if (prestate)
    { res = res + "@pre"; }

    if (parameters != null)
    { res = res + "("; 
      for (int i = 0; i < parameters.size(); i++)
      { res = res + parameters.get(i); 
        if (i < parameters.size() - 1)
        { res = res + ","; } 
      }
      res = res + ")"; 
    }

    if (arrayIndex != null)
    { res = res + "[" + arrayIndex + "]"; } 

    if (needsBracket) 
    { res = "(" + res + ")"; } 

    return res; 
  }

  public String toAST() 
  { String res = "(OclBasicExpression ";
    if (objectRef != null)
    { res = res + objectRef.toAST() + " . " + data; } 
    else 
    { res = res + data; } 

    if (prestate)
    { res = res + " @pre"; }

    if (parameters != null)
    { res = res + " ( "; 

      if (parameters.size() > 0)
      { res = res + " (ParameterArguments "; } 
 
      for (int i = 0; i < parameters.size(); i++)
      { res = res + ((Expression) parameters.get(i)).toAST(); 
        if (i < parameters.size() - 1)
        { res = res + " , "; } 
      }

      if (parameters.size() > 0)
      { res = res + " ) "; } 
  
      res = res + " ) "; 
    }

    if (arrayIndex != null)
    { res = res + " [ " + arrayIndex.toAST() + " ]"; } 

    return res + " )"; 
  }

  public String toCSTL()
  { String res = data;
 
    if (parameters != null && parameters.size() > 0)
    { res = ""; 
      for (int i = 0; i < parameters.size(); i++)
      { String par = ((BasicExpression) parameters.get(i)).toCSTL(); 
        res = res + par + " "; 
      } 
    } 
    return res; 
  }  

  public static int starIndex(BasicExpression be)
  { if (be.parameters == null) 
    { return 0; } 
    for (int i = 0; i < be.parameters.size(); i++) 
    { if ("_*".equals(be.parameters.get(i) + ""))
      { return i+1; } 
    } 
    return 0; 
  } 

  public boolean isSingleListTerm()
  { // contents of expression are constants, with no "_v"
    // except _*

    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++) 
      { String par = "" + parameters.get(i); 
        if (par.startsWith("_*")) { }
        else if (par.startsWith("_"))
        { return false; } 
      }  
      return true; 
    }
 
    return false; 
  } 

  public boolean isFunctionApplication()
  { if (umlkind == FUNCTION && parameters != null && 
        parameters.size() == 1)
    { String par = "" + parameters.get(0); 
      if ("_*".equals(par))
      { return false; } 
      return true; 
    }
 
    if (parameters != null && parameters.size() == 1) 
    { BasicExpression par = ((BasicExpression) parameters.get(0));
      return par.isFunctionApplication(); 
    }  

    return false; 
  } 

  public String getAppliedFunction()
  { if (umlkind == FUNCTION && parameters != null && 
        parameters.size() == 1)
    { return data; }
 
    if (parameters != null && parameters.size() == 1) 
    { BasicExpression par = ((BasicExpression) parameters.get(0));
      return par.getAppliedFunction(); 
    }  

    return null; 
  } 


  public String toLiteralCSTL()
  { String res = data;
 
    if (umlkind == FUNCTION)
    { if (parameters != null && parameters.size() == 1 && 
          parameters.get(0) instanceof BasicExpression)
      { BasicExpression par = (BasicExpression) parameters.get(0); 
        if (par.umlkind == FUNCTION) 
        { res = par.toLiteralCSTL(); } 
        else  
        { res = par + "`" + data; }  
      }
      return res;  
    } 

    if (parameters != null && parameters.size() > 0)
    { res = ""; 
      for (int i = 0; i < parameters.size(); i++)
      { String par = ((BasicExpression) parameters.get(i)).toLiteralCSTL(); 
        // if (res.length() > 0 && 
        //     par.startsWith("_") && 
        //     ( Character.isDigit(res.charAt(res.length()-1)) ||
        //       Character.isLetter(res.charAt(res.length()-1)) )
        //    )
        // { res = res + " " + par; } 
        // else
 
        if (par.endsWith(" "))
        { res = res + par; } 
        else 
        { res = res + par + " "; } 
      } 
    } 
    return res; 
  }  

  public String toInnerCSTL()
  { String res = data;
 
    if (umlkind == FUNCTION)
    { if (parameters != null && parameters.size() == 1)
      { String par = ((BasicExpression) parameters.get(0)).toInnerCSTL(); 
        res = par; 
      }
      return res;  
    } 

    if (parameters != null && parameters.size() > 0)
    { res = ""; 
      for (int i = 0; i < parameters.size(); i++)
      { String par = ((BasicExpression) parameters.get(i)).toLiteralCSTL(); 
        // if (res.length() > 0 && 
        //     par.startsWith("_") && 
        //     ( Character.isDigit(res.charAt(res.length()-1)) ||
        //       Character.isLetter(res.charAt(res.length()-1)) )
        //    )
        // { res = res + " " + par; } 
        // else
 
        if (par.endsWith(" "))
        { res = res + par; } 
        else 
        { res = res + par + " "; } 
      } 
    } 
    return res; 
  }  // No nesting of metafeatures. 

  public Vector usesCSTLfunctions()
  { Vector res = new Vector();
 
    if (umlkind == FUNCTION)
    { if (parameters != null && parameters.size() == 1)
      { res.add(data);
        Expression par = (Expression) parameters.get(0); 
        if (par instanceof BasicExpression)
        { res.addAll(((BasicExpression) par).usesCSTLfunctions()); }
      } 

      // System.out.println(">>>--- CSTL functions used in " + this + " are: " + res); 
      return res;  
    } 

    if (parameters != null && parameters.size() > 0)
    { for (int i = 0; i < parameters.size(); i++)
      { if (parameters.get(i) instanceof BasicExpression)
       { BasicExpression par = (BasicExpression) parameters.get(i); 
          res.addAll(par.usesCSTLfunctions());
        }    
      } 
    } 

    // System.out.println(">>>--- CSTL functions used in " + this + " are: " + res); 

    return res; 
  }  


  // saveTextModel
  public String saveModelData(PrintWriter out) // the RSDS internal representation
  { String res = Identifier.nextIdentifier("basicexpression_");
    out.println(res + " : BasicExpression"); 
    out.println(res + ".data = \"" + data + "\""); 
    out.println(res + ".expId = \"" + res + "\""); 
    // type and element type, prestate umlKind, needsBracket

    String tname = "void"; 
    if (type != null) 
    { tname = type.getUMLModelName(out); }  
    out.println(res + ".type = " + tname);  

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(res + ".elementType = " + etname); 
    } 
    else if (type != null && type.elementType != null) 
    { String tet = type.elementType.getUMLModelName(out); 
      out.println(res + ".elementType = " + tet); 
    } 
    else if (type != null && 
             type.getElementType() != null)
    { String etname = 
        type.getElementType().getUMLModelName(out); 
      out.println(res + ".elementType = " + etname);
    } 
    else if (type != null && 
             Type.isBasicType(type))
    { out.println(res + ".elementType = " + tname); } 
    else 
    { System.err.println("!!! Warning!: no element type for " + this); 
      out.println(res + ".elementType = " + tname); 
    } 

    if (entity != null) 
    { out.println(entity.getName() + " : " + res + ".context"); } 

    out.println(res + ".needsBracket = " + needsBracket); 

    if (umlkind == QUERY) 
    { out.println(res + ".umlKind = " + UPDATEOP); } 
    else 
    { out.println(res + ".umlKind = " + umlkind); } 

    out.println(res + ".isStatic = " + isStatic); 
 
    out.println(res + ".prestate = " + prestate); 
        

    if (umlkind == ATTRIBUTE) 
    { if (entity != null) 
      { Entity owner = entity.getAttributeOwner(data); 
        out.println(data + "_" + owner.getName() + " : " + res + ".referredProperty");
      } 
    }  // must be the owner of the attribute, not this entity. 

    if (umlkind == ROLE) 
    { if (entity != null) 
      { Association ast = entity.getDefinedRole(data); 
        String aname = ast.getName() + "_end2"; 
        out.println(aname + " : " + res + ".referredProperty"); 
      } 
    }  


    if (objectRef != null)
    { String objid = objectRef.saveModelData(out); 
      out.println(objid + " : " + res + ".objectRef"); 
    } 

    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        String parid = par.saveModelData(out); 
        out.println(parid + " : " + res + ".parameters");  
      }
    }

    if (arrayIndex != null)
    { String arrayid = arrayIndex.saveModelData(out); 
      out.println(arrayid + " : " + res + ".arrayIndex"); 
    } 
    return res; 
  }

  public Expression definedness()
  { Expression res = new BasicExpression(true);

    // for E[ind] for E entity with primary key key, ind : E.key
    if (umlkind == CLASSID)
    { if (arrayIndex != null && entity != null)
      { Attribute pk = entity.getPrincipalKey(); 
        if (pk == null) { return res; } 
        String pkname = pk.getName();
        res = arrayIndex.definedness();  
        BasicExpression edom = new BasicExpression(pkname); 
        edom.setObjectRef(new BasicExpression(data)); 
        Expression indom = new BinaryExpression(":",arrayIndex,edom);
        res = simplify("&",res,indom,null); 
      } 
      return res; 
    }   

    // For s[ind] with s a sequence, ind >= 1 & ind <= s.size:

    if ((umlkind == ROLE || umlkind == ATTRIBUTE || umlkind == VARIABLE)
        && arrayIndex != null)
    { // if not qualified, but is ordered
      if (objectRef != null) 
      { res = objectRef.definedness(); }
      res = simplify("&",res,arrayIndex.definedness(),null);  
      if (isQualified()) 
      { if (multiplicity == ModelElement.MANY)
        { return res; } 
        else 
        { // E2->exists( e2 | this = e2 )
          Type t2 = getElementType(); 
          if (t2 != null && t2.isEntity())
          { Entity e2 = t2.getEntity(); 
            BasicExpression e2x = new BasicExpression(e2.getName().toLowerCase()); 
            BinaryExpression eqe2x = new BinaryExpression("=", this, e2x); 
            return new BinaryExpression("#", 
                         new BinaryExpression(":", e2x, new BasicExpression(e2)), 
                         eqe2x); 
          } 
        } 
        return res; 
      } 
      BasicExpression newdata = new BasicExpression(data);
      newdata.setObjectRef(objectRef); 
      newdata.umlkind = ROLE;  
      UnaryExpression selfsize = new UnaryExpression("->size", newdata); 
      Expression lbnd = new BinaryExpression("<=", new BasicExpression(1), arrayIndex); 
      Expression ubnd = new BinaryExpression("<=", arrayIndex, selfsize); 
      Expression inrange = new BinaryExpression("&",lbnd,ubnd); 
      return simplify("&",res,inrange,null);
    } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression pdef = par.definedness(); 
        res = simplify("&", res, pdef, null); 
      } 
    } 

    if ((umlkind == UPDATEOP || umlkind == QUERY) && entity != null) 
    { BehaviouralFeature bf = entity.getDefinedOperation(data);
      if (bf != null) 
      { Expression spre = bf.definedness(parameters); 
        return simplify("&",res,spre,null);
      }  
    } 

    if (objectRef == null) 
    { return res; }
    res = objectRef.definedness();


    if (umlkind == FUNCTION)
    { Expression zero = new BasicExpression(0);
      if ("sqrt".equals(data))
      { Expression nneg = new BinaryExpression(">=",objectRef,zero);
        return simplify("&",nneg,res,null);
      }
      else if ("log".equals(data) || "log10".equals(data))
      { Expression pos = new BinaryExpression(">",objectRef,zero);
        return simplify("&",pos,res,null);
      }
      else if ("acos".equals(data) || "asin".equals(data))
      { Expression minus1 = new BasicExpression(-1); 
        Expression one = new BasicExpression(1); 
        Expression pos1 = new BinaryExpression(">=",objectRef,minus1);
        Expression pos2 = new BinaryExpression("<=",objectRef,one);
        Expression res1 = simplify("&",pos2,res,null);
        return simplify("&",pos1,res1,null); 
      }
      else if ("last".equals(data) || "first".equals(data) ||
          "front".equals(data) || "tail".equals(data) ||
          "max".equals(data) || "min".equals(data) || "any".equals(data))
      { UnaryExpression orsize = new UnaryExpression("->size",objectRef);
        Expression pos = 
          new BinaryExpression(">",orsize,zero);
        return simplify("&",pos,res,null);
      }
      else if ("toReal".equals(data))
      { BasicExpression checkReal = new BasicExpression("isReal"); 
        checkReal.setObjectRef(objectRef); 
        return simplify("&",checkReal,res,null); 
      } 
      else if ("toInteger".equals(data))
      { BasicExpression checkInt = new BasicExpression("isInteger"); 
        checkInt.setObjectRef(objectRef); 
        return simplify("&",checkInt,res,null); 
      } 
      else if ("oclAsType".equals(data))
      { BasicExpression iko = new BasicExpression("oclIsKindOf"); 
        iko.setObjectRef(objectRef); 
        iko.setParameters(parameters);
        return iko; 
      }
      // Operation call defined if precondition[args/params] is true
    }

    return res;
  }

  public Expression determinate()
  { Expression res = new BasicExpression(true);
    if (objectRef != null) 
    { res = objectRef.determinate(); }
    if (arrayIndex != null) 
    { res = simplify("&",res,arrayIndex.determinate(),null); }

    if ("any".equals(data))
    { Expression one = new BasicExpression(1);
      UnaryExpression andsize = new UnaryExpression("->size",objectRef);
      Expression isone = 
        new BinaryExpression("=",andsize,one);
      return simplify("&",res,isone,null);
    }

    if ("asSequence".equals(data))
    { if ("Set".equals(objectRef.getType().getName()))
      { Expression det1 = new BinaryExpression("<=", new UnaryExpression("->size",objectRef),
                                    new BasicExpression(1)); 
        return simplify("&",res,det1,null);
      } 
      else 
      { return res; } 
    } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression pdef = par.determinate(); 
        res = simplify("&", res, pdef, null); 
      } 
    } 

    if ((umlkind == UPDATEOP || umlkind == QUERY) && entity != null) 
    { BehaviouralFeature bf = entity.getDefinedOperation(data);
      if (bf != null) 
      { Expression spre = bf.determinate(parameters); 
        return simplify("&",res,spre,null);
      }  
    } 

    return res;
  } // operations determinate if postcondition[args/params] is
  // also case of flatten

  public String toSQL()
  { String res = ""; 
    if (umlkind == VALUE || umlkind == CONSTANT)
    { String tname = type.getName();
      if (tname.equals("String") ||
          tname.equals("boolean"))
      { res = "'" + data + "'"; }
      else 
      { res = data; }
      return res; 
    }

    if (umlkind == FUNCTION)
    { String pre = objectRef.toSQL();
      if (data.equals("size"))
      { return "LENGTH(" + pre + ")"; } 
      if (data.equals("abs") || data.equals("floor") ||
          data.equals("sqrt") || data.equals("max") || data.equals("ceil") ||
          data.equals("round") || data.equals("exp") || data.equals("log") || 
          data.equals("min") || data.equals("sum"))  // exp, floor
      { return data.toUpperCase() + "(" + pre + ")"; }
      else if (data.equals("toUpperCase"))
      { return "UCASE(" + pre + ")"; }
      else if (data.equals("toLowerCase"))
      { return "LCASE(" + pre + ")"; } 
      else if (data.equals("sqr"))
      { return "(" + pre + "*" + pre + ")"; }
    }

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { return entity.getName() + "." + data; } 
    }
    return data;
  }

  private String impName() // removes "."
  { if (objectRef != null) 
    { return ((BasicExpression) objectRef).impName() + data; } 
    else 
    { return data; }
  }

  public String toB()
  { String res = ""; 
    if (data.equals("closure"))  // objectRef not null
    { BasicExpression objs = (BasicExpression) objectRef;
      return "(closure(" + objs.data + ")(" + objs.objectRef.toB() + "))";
    }
    else if (data.equals("asSet"))
    { return "ran(" + objectRef.toB() + ")"; }  
    else if (objectRef != null)  
    { res = data + "(" + objectRef.toB() + ")"; }
    else 
    { res = data; } 
    if (data.equals("true")) { return "TRUE"; } 
    if (data.equals("false")) { return "FALSE"; } 
    if (parameters != null) 
    { res = res + "("; 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = res + par.toB(); 
        if (i < parameters.size() - 1) 
        { res = res + ","; } 
      } 
      res = res + ")"; 
    } 
    if (arrayIndex == null) 
    { return res; } 
    return "(" + res + ")(" + arrayIndex.toB() + ")"; 
  } // + between strings also different in B

  public String toJava()   
  { String res = ""; 
    if (objectRef != null) 
    { res = objectRef.toJava() + "." + data; }   // for multiples only
    else                                         
    { res = data; } // "M" + mysm.label + "." + data  surely???
    if (parameters != null) 
    { res = res + "("; 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = res + par; 
        if (i < parameters.size() - 1) 
        { res = res + ","; } 
      } 
      res = res + ")"; 
    }
    if (arrayIndex != null)
    { return res + ".get(" + arrayIndex.toJava() + " - 1)"; }  // not for qualified
    return res;  
 } // if data is a method name also add () at end. 

  public String toZ3()   // not considering parameters. Arrays/lists indexed from 1
  { String res = data;
    if ("Set{}".equals(data) || "{}".equals(data) || "Sequence{}".equals(data))
    { return "nil"; } 
    if (umlkind == FUNCTION) 
    { String pre = objectRef.toZ3(); 
      if (data.equals("sqr") || data.equals("abs") || data.equals("floor") ||
          data.equals("tan") || data.equals("sin") || data.equals("cos") ||
          data.equals("exp") || data.equals("log") || data.equals("round") ||
          data.equals("sqrt") || data.equals("toUpperCase") || data.equals("ceil") || 
          data.equals("toLowerCase"))
      { res = res + "(" + data + " " + pre + ")"; } 
      else if (data.equals("subrange"))  // Integer.subrange is special case
      { // if a String, substring, otherwise subSequence
        if (parameters.size() == 2) 
        { res = "(" + data + " " + pre + " " + parameters.get(0) + " " + parameters.get(1) + ")"; } 
        else if (parameters.size() == 1)
        { res = "(" + data + " " + pre + " " + parameters.get(0) + ")"; } 
      } 
      else if (data.equals("first"))
      { res = "(head " + pre + ")"; } 
      else // min, max, sum, size, any, asSet, last, front, tail
      { res = "(" + data + " " + pre + ")"; } 
      return res; 
    } 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { Attribute pk = entity.getPrincipalKey(); 
        if (pk == null) { return ""; } 
        String pkname = pk.getName();
        String ind = arrayIndex.toZ3();  
        res = "(get" + data + "By" + pkname + " " + ind + ")"; 
      } 
      return res; 
    }   
    if (objectRef != null) 
    { res = "(" + data + " " + objectRef.toZ3() + ")"; } 
    if (arrayIndex != null)
    { res = "(at " + res + " " + arrayIndex.toZ3() + ")"; } 

    if (parameters != null) 
    { String parstring = "("; 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        parstring = parstring + par.toZ3();
        if (i < parameters.size() - 1) { parstring = parstring + ","; }  
      }
      res = "(" + res + " " + parstring + "))";  
    } 

    return res; 
  } 

  public String toOcl(java.util.Map env, boolean local)
  { // System.out.println(this + " umlkind = " + umlkind); 
   
    String res = ""; 
    if (objectRef != null) 
    { res = objectRef.toOcl(env,local); }
    if (umlkind == FUNCTION) 
    { if (data.equals("exp") || data.equals("abs") ||
          data.equals("log") || data.equals("round") ||
          data.equals("ceil") || data.equals("floor") ||
          data.equals("sin") || data.equals("cos") ||
          data.equals("tan") || data.equals("cbrt") || 
          data.equals("sqrt") || data.equals("toUpperCase") ||
          data.equals("toLowerCase"))
      { res = res + "." + data + "()"; } 
      else if (data.equals("subrange") || 
               data.equals("pow") || 
               data.equals("setAt") || 
               data.equals("insertAt") || 
               data.equals("insertInto"))
      { // if a String, substring, otherwise subSequence
        if (parameters != null && parameters.size() > 1)
        { res = res + "." + data + "(" + parameters.get(0) + "," + 
                                         parameters.get(1) + ")"; 
        } 
      } 
      else if (data.equals("indexOf") || data.equals("count") || data.equals("oclAsType") ||
               data.equals("oclIsKindOf"))
      { // if a String, substring, otherwise subSequence
        if (parameters != null && parameters.size() > 0)
        { res = 
            res + "->" + data + "(" + ((Expression) parameters.get(0)).toOcl(env,local) + ")"; 
        } 
      } 
      else // min, max, sum, size, any, asSet, first, last, front, tail
      { res = res + "->" + data + "()"; } 
    } 
    else if (umlkind == VALUE || umlkind == CONSTANT || umlkind == VARIABLE)
    { res = data; } // if CLASS, then data + ".allInstances()"
    else if (umlkind == CLASSID)
    { res = data + ".allInstances()"; 
      if (arrayIndex != null)
      { Attribute pk = entity.getPrincipalKey(); 
        String pkname = pk.getName();
        String ind = arrayIndex.toOcl(env,local);  
        if (arrayIndex.isMultiple())
        { res = res + "->select(" + ind + "->includes(" + pkname + "))"; }
        else 
        { res = res + "->select(" + ind + " = " + pkname + ")->any()"; } 
      } 
    }  
    else 
    { String nme = entity.getName();
      if (objectRef == null)
      { if (umlkind == ATTRIBUTE || umlkind == ROLE)
        { if (entity.isClassScope(data))  // also for ops
          { res = nme + "." + data; }  // and arrayIndex
          else 
          { String var = (String) env.get(nme);
            if (var == null)  // because nme is superclass of ent: dom(env)
            { var = entity.searchForSubclassJava(env); 
              if (var == null)
              { var = entity.searchForSuperclassJava(env); 
                if (var == null) 
                { System.err.println("!! ERROR: No variable in scope of class " + nme); 
                  // var = nme.toLowerCase() + "x";
                  res = data;  
                }
                else 
                { var = "(" + var + "->oclAsType(" + nme + "))"; 
                  res = var + "." + data; 
                }
              } 
              else 
              { res = var + "." + data; } 
            } 
            else 
            { res = var + "." + data; } 
          }
        } 
      } // valid but unecesary for static attributes
      else
      { if (entity.isClassScope(data))  // also for ops
        { res = nme + "." + data; }  // and arrayIndex
        else 
        { String pre = objectRef.toOcl(env,local);
          res = pre + "." + data;
        } 
      }  
    }

    if (parameters != null)
    { res = res + "("; 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = res + par.toOcl(env,local); 
        if (i < parameters.size() - 1) 
        { res = res + ","; } 
      } 
      res = res + ")"; 
    } 

    if (prestate)
    { res = res + "@pre"; }

    if (arrayIndex != null) 
    { res = res + "->at(" + arrayIndex.toOcl(env,local) + ")"; } 

    return res; 
  } // constants and variables?

  public Expression toSmv(Vector cnames) 
  { if (cnames.contains(toString()))
    { if (objectRef == null)
      { return new BasicExpression("M" + data + "." + data); }
      else 
      { return new BasicExpression("M" + toJava()); } 
    }  
    else if (isEvent) 
    { return new BinaryExpression("=",
                   new BasicExpression("C.con_event"), 
                   new BasicExpression(data)); 
    } 
    else
    { return new BasicExpression(data); } 
  } 

  public String toImp(final Vector components) 
  { // System.out.println(data); 
    // System.out.println(components); 

    Statemachine sm = (Statemachine) VectorUtil.lookup(data,components); 
    if (sm != null) 
    { return impName() + "x"; } 
    else 
    { return data; } 
  }

  public String toJavaImp(final Vector components)
  { // System.out.println(data); 
    // System.out.println(components); 

    Statemachine sm = (Statemachine) VectorUtil.lookup(data,components);
    if (sm != null)
    { return impName() + "x"; }
    else
    { return data; }
  }


  public boolean isOrExpression() { return false; } 

  public Expression buildJavaForm(final Vector comps) 
  { int whatis = -1; 
    int mysm = -1; 
    int multip = 0; 
    Statemachine sm = null; 

    for (int i = 0; i < comps.size(); i++) 
    { sm = (Statemachine) comps.get(i); 
      whatis = sm.getCategory(data); 
      if (whatis >= 0)
      { mysm = i; 
        multip = sm.getMultiplicity(); 
        break; 
      } 
    }

    if (whatis == Statemachine.EVENT) 
    { 
      if (sm.cType == DCFDArea.ACTUATOR && multip <= 1) 
      { javaForm = new BasicExpression("M" + sm.label + "." + data + "()"); }
      else if (sm.cType == DCFDArea.ACTUATOR && multip > 1) 
      { javaForm = new BasicExpression(toJava() + "()"); } // toString() 
      else if (sm.cType == DCFDArea.CONTROLLER)
      { javaForm = new BasicExpression(sm.label.toLowerCase() + "." + 
                                       data + "()"); 
      } 
      else // invalid 
      { javaForm = this; } 
    } 
    else if (whatis == Statemachine.ATTRIBUTE || 
             whatis == Statemachine.STATEMACHINE)
    { if (multip <= 1) 
      { javaForm = new BasicExpression("M" + sm.label + "." + data); } 
      else 
      { javaForm = new BasicExpression(toJava()); }  // toString() in fact
    } 
    return javaForm; 
  }  // Must be an event if valid. 


  public int typeCheck(final Vector sms)
  { if (data.equals("true") || data.equals("false"))
    { modality = SENSOR; 
      return SENSOR; 
    } 
    
    String comp = Statemachine.findComponentOf(data, sms); 

    if (comp == null || comp.equals("")) 
    { System.out.println("Error: must have equality expressions " +
                         " or valid event names\n" + 
                         "at base level, not: " + toString());
      modality = ERROR; 
      return ERROR; 
    }
    else 
    { isEvent = true; 
      modality = HASEVENT; 
      return HASEVENT; 
    } 
  } 

  public Expression checkConversions(Type propType, Type propElemType, java.util.Map interp) 
  { // if (umlkind == CLASSID) { return this; } 
    // System.out.println("CONVERTING " + this + " " + type + "(" + elementType + ")"); 


    if ("resolveTemp".equals(data)) 
    { // x.resolveTemp(p) becomes propElemType[p + x.$id] 

      if (parameters != null && parameters.size() > 0) 
      { Expression par1 = (Expression) parameters.get(0); 
        Expression p = (Expression) par1.clone(); 
        BasicExpression b = new BasicExpression(propElemType);
        BasicExpression id = new BasicExpression("$id");
        BasicExpression underscore = new BasicExpression("\"_\"");  
        id.setObjectRef(objectRef);  
        BinaryExpression prefix = new BinaryExpression("+", p, underscore); 
        BinaryExpression ind = new BinaryExpression("+", prefix, id); 
        b.setArrayIndex(ind); 
        return b; 
      } 
    } // for attributes it is entity.getName() + "." + data

    if ("boolean".equals(type + "") || "int".equals(type + "") || "long".equals(type + "") || 
        "double".equals(type + "") || "String".equals(type + ""))
    { return this; } 

    if (objectRef != null) 
    { Expression objs = objectRef.checkConversions(propType,propElemType,interp); 
      objectRef = objs; 
    } 

    if (type != null && type.isEntity())
    { Entity ent = type.getEntity(); 
      if (ent.hasStereotype("source"))
      { Entity tent = (Entity) interp.get(ent + ""); 

        // System.out.println("INTERPRETATION OF " + ent + " IS= " + tent); 

        if (tent == null) // use the expected target type instead
        { if (propType != null && propType.isEntity())
          { tent = propType.getEntity(); } 
        }  
        BasicExpression res = new BasicExpression(tent + ""); 
        res.setType(new Type(tent)); 
        res.setUmlKind(Expression.CLASSID); 
        res.setMultiplicity(ModelElement.ONE); 
        res.setElementType(new Type(tent)); 
        BasicExpression ind = new BasicExpression("$id"); 
        ind.setObjectRef(this); 
        ind.setType(new Type("String",null)); 
        res.setArrayIndex(ind); 
        return res; 
      } 
      else 
      { return this; } // it must already be of target type
    } 
    else if (elementType != null && elementType.isEntity())
    { Entity ent = elementType.getEntity(); 
      if (ent.hasStereotype("source"))
      { Entity tent = (Entity) interp.get(ent + ""); 

        // System.out.println("INTERPRETATION OF " + ent + " IS= " + tent); 

        if (tent == null) 
        { if (propElemType != null && propElemType.isEntity())
          { tent = propElemType.getEntity(); } 
        }  
        BasicExpression res = new BasicExpression(tent + ""); 
        res.setType(type); 
        res.setMultiplicity(multiplicity); 
        res.setElementType(new Type(tent)); 
        res.setUmlKind(Expression.CLASSID); 
        BinaryExpression ind = 
             new BinaryExpression("->collect",this,new BasicExpression("$id"));
        
        ind.setType(type); // Sequence or Set 
        ind.setElementType(new Type("String",null)); 
        res.setArrayIndex(ind); 
        return res; 
      }   // More general to use Tent[this->collect($id)] 
      else 
      { return this; }
    } 
    else 
    { return this; } 
  } 
    
  public Expression replaceModuleReferences(UseCase uc) 
  { Entity e = uc.getClassifier(); 

    BasicExpression res = new BasicExpression(data); 

    res.setType(type); 
    res.setElementType(elementType); 
    res.umlkind = umlkind; 
    
    Expression objs = null; 

    Vector pars = new Vector(); 
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression pp = (Expression) parameters.get(i); 
        Expression newpp = pp.replaceModuleReferences(uc); 
        pars.add(newpp); 
      } 
      res.setParameters(pars); 
    } 
    else 
    { res.parameters = null; } 

    // and arrayIndex

    if (objectRef != null && (objectRef instanceof BasicExpression))
    { String odata = ((BasicExpression) objectRef).data; 

      // if ("thisModule".equals(odata) && "resolveTemp".equals(data)) 
      // { // thisModule.resolveTemp(x,v)  becomes  TargetType[v + x.$id]
      

      if ("thisModule".equals(odata) && !e.hasOperation(data)) 
      { // thisModule.op(pars) becomes pars1.op(parsTail)

        if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) pars.get(0); 
          Expression par1c = (Expression) par1.clone(); 
          par1c.needsBracket = true; 
          res.setObjectRef(par1c); 
          Vector parst = new Vector(); 
          parst.addAll(pars); 
          parst.remove(0); 
          res.setParameters(parst); 
          return res; 
        } 
        else 
        { Expression eref = new BasicExpression(e); 
          res.setObjectRef(eref); 
          return res; 
        } 
      } 
      // for attributes it is e.getName() + "." + data
      else if ("thisModule".equals(odata) && e.hasOperation(data)) 
      { // thisModule.op(pars) becomes e.getName().op(pars)
        Expression eref = new BasicExpression(e); 
        res.setObjectRef(eref); 
        return res; 
      }   
    } 

    if (objectRef != null) 
    { objs = objectRef.replaceModuleReferences(uc); } 
    res.setObjectRef(objs); 
    return res; 

  } 
    

  // for UMLRSDS: also do doubles with E-, E+ syntax.
 
  public boolean typeCheck(final Vector types, final Vector entities,
                           final Vector contexts, final Vector env)
  { boolean res = true;

    modality = ModelElement.NONE;  // default
    // check if a number, boolean or String.
    // if so, umlkind = VALUE and should have no objectRef
    // Also doubles. 
    Vector context = new Vector(); // local context


    if ("null".equals(data))
    { type = new Type("OclAny", null); 
      entity = null; 
      umlkind = VALUE; 
      multiplicity = ModelElement.ONE;
      return true;
    } 

    if ("Math_NaN".equals(data) || 
        "Math_PINFINITY".equals(data) || 
        "Math_NINFINITY".equals(data))
    { type = new Type("double", null); 
      entity = null; 
      umlkind = VALUE; 
      multiplicity = ModelElement.ONE;
      return true;
    } // double values that represent invalid doubles

    if (isInteger(data))
    { type = new Type("int",null);
      elementType = type; 
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.ONE;
      data = "" + Expression.convertInteger(data);  
      // System.out.println("**Type of " + data + " is int");
      return true;
    }

    if (Expression.isLong(data))
    { type = new Type("long",null);
      elementType = type; 
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.ONE;
      data = "" + Expression.convertLong(data); 
      // System.out.println("**Type of " + data + " is long");
      return true;
    }

    if (isDouble(data))
    { type = new Type("double",null);
      elementType = type; 
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.ONE;
      // System.out.println("**Type of " + data + " is double");
      return true;
    }

    if (isString(data))
    { type = new Type("String",null);
      elementType = type; 
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.ONE;
      // System.out.println("**Type of " + data + " is String");
      if (arrayIndex != null) 
      { // System.out.println(">>It has array index " + arrayIndex); 
        arrayIndex.typeCheck(types,entities,contexts,env); 
      }
      return true;
    }

    if (isBoolean(data))
    { type = new Type("boolean",null);
      elementType = type; 
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.ONE;
      // System.out.println("**Type of " + data + " is boolean");
      return true;
    }

    // Expression.isSetValue(data)
    if (isSet(data))  // convert it to a SetExpression
    { type = new Type("Set",null);  // or Sequence if ordered
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.MANY;
      SetExpression se = (SetExpression) buildSetExpression(data); 
      elementType = Type.determineType(se.getElements()); 
      type.setElementType(elementType); 
      // System.out.println("**Type of " + data + " is " + type);
      return true;
    }  // could have an array index. Also deduce elementType

    // Expression.isSequenceValue(data)
    if (isSequence(data))  // convert it to a SetExpression
    { type = new Type("Sequence",null);  // or Sequence if ordered
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.MANY;
      SetExpression se = (SetExpression) buildSetExpression(data); 
      elementType = Type.determineType(se.getElements());
      type.setElementType(elementType);  
      if (arrayIndex != null) 
      { type = elementType; 
        elementType = elementType.getElementType();
        arrayIndex.typeCheck(types,entities,contexts,env);
        if (type != null) 
        { multiplicity = type.typeMultiplicity(); }   
      }      
      // System.out.println("**Type of " + this + " is " + type);
      return true;
    }  // and deduce elementType

    // Expression.isMapValue(data)
    if (isMap(data))  // convert it to a SetExpression
    { type = new Type("Map",null);  // or Sequence if ordered
      entity = null;
      umlkind = VALUE;
      multiplicity = ModelElement.MANY;
      SetExpression se = (SetExpression) buildSetExpression(data); 
      elementType = Type.determineType(se.getElements()); 
      type.setElementType(elementType); 
      // System.out.println("**Type of " + data + " is " + type);
      return true;
    }  // could have an array index. Also deduce elementType

    if ("int".equals(data) || "long".equals(data) || 
        "boolean".equals(data) || "void".equals(data) ||  
        "double".equals(data) || "String".equals(data) ||
        "OclDate".equals(data) || "OclAny".equals(data) || 
        "OclType".equals(data) || "OclFile".equals(data) || 
        "OclRandom".equals(data) ||
        "SQLStatement".equals(data) || 
        "OclDatasource".equals(data) || 
        "OclAttribute".equals(data) || 
        "OclOperation".equals(data) || 
        Type.isOclExceptionType(data) ||  
        "OclProcess".equals(data) || 
        "OclProcessGroup".equals(data))
    { type = new Type("OclType", null); 
      elementType = new Type(data, null); 
      umlkind = TYPE; 
      type.elementType = elementType; 
      multiplicity = ModelElement.MANY; 
      entity = null; 
    } // for use in oclAsType(typ)

    if ("now".equals(data))
    { type = new Type("long", null); 
      elementType = type; 
      umlkind = VARIABLE; 
      variable = new Attribute("now", type, ModelElement.INTERNAL); 
      multiplicity = ModelElement.ONE; 
      System.out.println("***Type of " + this + " is: " + type); 
      return true;  
    } 

    if ("$act".equals(data) || "$fin".equals(data))
    { type = new Type("int", null); 
      elementType = type; 
      umlkind = VALUE; 
      multiplicity = ModelElement.ONE; 
      System.out.println("***Type of " + this + " is: " + type); 
      return true; 
    } 

    if ("self".equals(data))
    { if (contexts.size() == 0)
      { System.err.println("!WARNING!: Invalid occurrence of self, not in instance context"); 
        // JOptionPane.showMessageDialog(null, "ERROR: Invalid occurrence of self, not in instance context", "Semantic error", JOptionPane.ERROR_MESSAGE); 
      }
      else 
      { if (contexts.size() > 1)
        { System.err.println("!WARNING!: Ambiguous occurrence of self, contexts: " + contexts);
        } 
        entity = (Entity) contexts.get(0); // the most local context
        type = new Type(entity); 
        elementType = type; 
        umlkind = VARIABLE; 
        variable = new Attribute("self", type, ModelElement.INTERNAL); 
        multiplicity = ModelElement.ONE; 
        System.out.println("***Type of self (most local context) is: " + type); 
        return true; 
      } 
    } 

    if ("super".equals(data))
    { if (contexts.size() == 0)
      { if (objectRef == null) 
        { System.err.println("!!ERROR!!: Invalid occurrence of super, not instance context"); 
          JOptionPane.showMessageDialog(null, "ERROR!!: Invalid occurrence of super, not in instance context", "Semantic error", JOptionPane.ERROR_MESSAGE);
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity()) 
        { entity = objectRef.elementType.getEntity(); 
          type = new Type(entity.getSuperclass()); 
        } 
        else 
        { System.err.println("!!ERROR!!: Invalid occurrence of super: " + this + " no defined instance context"); }           
      }
      else 
      { if (contexts.size() > 1)
        { System.err.println("WARNING!: Ambiguous occurrence of super, contexts: " + contexts);
        } 

        for (int i = 0; i < contexts.size(); i++) 
        { entity = (Entity) contexts.get(i); 
          if (entity != null && entity.getSuperclass() != null)
          { type = new Type(entity.getSuperclass()); 
            break; 
          } 
        }  
        elementType = type; 
        umlkind = VARIABLE; 
        variable = new Attribute("super", type, ModelElement.INTERNAL); 
        multiplicity = ModelElement.ONE; 
        System.out.println("***Type of super is: " + type); 
        return true; 
      } 
    } 

    if ("equivalent".equals(data))
    { if (contexts.size() == 0)
      { System.err.println("!!ERROR!!: no context for ETL equivalent() operator"); 
        JOptionPane.showMessageDialog(null, "ERROR!!: Invalid occurrence of equivalent, not in instance context", "Semantic error", JOptionPane.ERROR_MESSAGE); 
      }
      else 
      { // entity = (Entity) contexts.get(0); // the most local context
        entity = (Entity) ModelElement.lookupByName("$IN", entities);
        Entity outent = (Entity) ModelElement.lookupByName("$OUT", entities);
        type = new Type(outent); 
        elementType = type; 
        umlkind = UPDATEOP; 
        multiplicity = ModelElement.ONE; 
        return true; 
      } 
    } // Only for formalisation of ETL

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { if (parameters.get(i) instanceof Expression)
        { Expression ep = (Expression) parameters.get(i); 
          ep.typeCheck(types,entities,contexts,env);
        } 
      } 
    }

    if ("newOclDate".equals(data) ||
        "newOclDate_Time".equals(data))
    { type = new Type("OclDate", null); 
      umlkind = UPDATEOP;
      isStatic = true;  
      entity = (Entity) ModelElement.lookupByName("OclDate",entities);  
      multiplicity = ModelElement.ONE; 
      // set the formal parameters
      if (parameters != null && parameters.size() > 0) 
      { Expression par1 = (Expression) parameters.get(0); 
        Attribute fpar1 = new Attribute("t", 
                                new Type("long", null),  
                                ModelElement.INTERNAL); 
        par1.formalParameter = fpar1; 
      }
      return true; 
    } 
    
    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      if (Type.isExceptionType(createdClass))
      { type = new Type(createdClass,null); 
        umlkind = UPDATEOP;
        isStatic = true;  
        entity = (Entity) 
          ModelElement.lookupByName(createdClass,entities); 
        multiplicity = ModelElement.ONE;
        if (parameters != null) 
        { for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
            if (par.isString())
            { Attribute fparmessage = 
                new Attribute("m", new Type("String", null), 
                              ModelElement.INTERNAL); 
              par.formalParameter = fparmessage; 
            }
            else 
            { Attribute fparcause = 
                new Attribute("c", new Type("OclException", null), ModelElement.INTERNAL); 
              par.formalParameter = fparcause; 
            } 
          }
        }  
             
        return true;
      }   
      else if (createdClass.startsWith("OclIterator"))
      { type = new Type("OclIterator",null); 
        umlkind = UPDATEOP;
        isStatic = true;  
        entity = (Entity) 
          ModelElement.lookupByName("OclIterator",entities); 
        multiplicity = ModelElement.ONE;
        if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          if (par1.isString())
          { elementType = new Type("String", null); } 
          else 
          { elementType = par1.getElementType(); }

          if (createdClass.equals("OclIterator_Sequence"))
          { Attribute fparsq = 
                new Attribute("sq", new Type("Sequence", null), ModelElement.INTERNAL); 
            par1.formalParameter = fparsq;
          }
          else if (createdClass.equals("OclIterator_Set"))
          { Attribute fparst = 
                new Attribute("st", new Type("Set", null), ModelElement.INTERNAL); 
            par1.formalParameter = fparst;
          }
          else if (createdClass.startsWith("OclIterator_String"))
          { Attribute fparss = 
                new Attribute("ss", new Type("String", null), ModelElement.INTERNAL); 
            par1.formalParameter = fparss;
          }
        } // and String_String, _Function

        return true;  
      }
        
    } // and the parameters

    if ("length".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("String")) 
      { type = new Type("int", null);
        umlkind = FUNCTION;
        data = "size"; 
        parameters = null;  
        multiplicity = ModelElement.ONE; 
 
        return true;
      }  
    } 

    if ("time".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { type = new Type("long", null);
        umlkind = ATTRIBUTE; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) ModelElement.lookupByName("OclDate",entities);  

        return true;
      }  
    } 

    if ("getSystemTime".equals(data) && "OclDate".equals(objectRef + "")) 
    { type = new Type("long", null);
      umlkind = QUERY;
      isStatic = true;
      entity = (Entity) ModelElement.lookupByName("OclDate",entities);  
  
      multiplicity = ModelElement.ONE; 
      entity = 
       (Entity) ModelElement.lookupByName("OclDate",entities);  
 
      return true;
    } 

    if ("getTime".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        type = new Type("long", null);
        entity = (Entity) ModelElement.lookupByName("OclDate",entities);  
        return true;
      }  
    } 

    if ("setTime".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { type = new Type("void", null); 
        umlkind = UPDATEOP; 
        multiplicity = ModelElement.ONE; 
        entity = 
          (Entity) ModelElement.lookupByName("OclDate",entities);
        // set the formal parameters
        if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          Attribute fpar1 = new Attribute("t", 
                                new Type("long", null),  
                                ModelElement.INTERNAL); 
          par1.formalParameter = fpar1; 
        }
        
        return true;
      }  
    } 

    if ("dateAfter".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { type = new Type("boolean", null); 
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE;
        entity = (Entity) ModelElement.lookupByName("OclDate",entities); 
        // set the formal parameters
        if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          Attribute fpar1 = new Attribute("d", 
                                new Type("OclDate", null),  
                                ModelElement.INTERNAL); 
          par1.formalParameter = fpar1; 
        }
      
        return true;
      }  
    } 

    if ("dateBefore".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { type = new Type("boolean", null); 
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE;
        entity = 
          (Entity) ModelElement.lookupByName("OclDate",entities); 
        if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          Attribute fpar1 = new Attribute("d", 
                                new Type("OclDate", null),  
                                ModelElement.INTERNAL); 
          par1.formalParameter = fpar1; 
        }
        return true;

        // set the formal parameters
        
      }  
    } 

    if ("elements".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclIterator")) 
      { type = new Type("Sequence", null);
        entity = (Entity) 
          ModelElement.lookupByName("OclIterator",entities); 

        umlkind = ATTRIBUTE; 
        multiplicity = ModelElement.MANY; 
 
        return true;
      }  
    } 

    if ("printStackTrace".equals(data) && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env); 
      if (objectRef.type != null && 
          objectRef.type.isOclException()) 
      { type = new Type("void", null); 
        umlkind = UPDATEOP; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName(objectRef.type.getName(),
                                    entities); 
        
        return true;
      }  
    } 

    if (("getCurrent".equals(data) || "read".equals(data) ||
         "readLine".equals(data) || "getName".equals(data) ||
         "getAbsolutePath".equals(data) || 
         "readAll".equals(data) || "getPath".equals(data) ||
         "getParent".equals(data))
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile")) 
      { type = new Type("String", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName("OclFile",entities); 

        return true;
      }  
    } 

    if (("lastModified".equals(data) || "length".equals(data))
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile")) 
      { type = new Type("long", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName("OclFile",entities); 
 
        return true;
      }  
    } 

    if (("hasNext".equals(data) || "canRead".equals(data) ||
         "canWrite".equals(data) || "exists".equals(data) || 
         "isFile".equals(data) || "mkdir".equals(data) ||  
         "isDirectory".equals(data) || 
         "isAbsolute".equals(data))
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile")) 
      { type = new Type("boolean", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        // set the formal parameters
        entity = (Entity) 
          ModelElement.lookupByName("OclFile",entities); 
        
        return true;
      }  
    } 

    if (("isAlive".equals(data) || "isDaemon".equals(data))
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclProcess")) 
      { type = new Type("boolean", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName("OclProcess",entities); 
        return true;
      }  
    } 

    if ("getName".equals(data)
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclProcess")) 
      { type = new Type("String", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName("OclProcess",entities); 

        return true;
      }  // and for OclType
    } 

    if ("currentThread".equals(data)
        && "OclProcess".equals(objectRef + "")) 
    { objectRef.typeCheck(types,entities,contexts,env);
      type = new Type("OclProcess", null);
      umlkind = QUERY; 
      multiplicity = ModelElement.ONE; 
      isStatic = true; 
      entity = (Entity) 
          ModelElement.lookupByName("OclProcess",entities); 
      return true;
    } 

    if (("getPriority".equals(data) || "activeCount".equals(data))
        && objectRef != null) 
    { objectRef.typeCheck(types,entities,contexts,env);
      if (objectRef.type != null && 
          objectRef.type.getName().equals("OclProcess")) 
      { type = new Type("int", null);
        umlkind = QUERY; 
        multiplicity = ModelElement.ONE; 
        entity = (Entity) 
          ModelElement.lookupByName("OclProcess",entities); 

        return true;
      }  
    } 

    if ("randomString".equals(data)
        && objectRef != null && 
        "OclRandom".equals(objectRef + "")) 
    { type = new Type("String", null);
      umlkind = QUERY; 
      multiplicity = ModelElement.ONE; 
      if (parameters != null && parameters.size() > 0) 
      { Expression par1 = (Expression) parameters.get(0); 
        Attribute fpar1 = new Attribute("n", 
                                new Type("int", null),  
                                ModelElement.INTERNAL); 
        par1.formalParameter = fpar1; 
      }
    
      return true;
    } 


    if (arrayIndex != null)
    { boolean res1 = arrayIndex.typeCheck(types,entities,contexts,env);
      arrayType = type; 
      res = res && res1;
    } // might reduce multiplicity to ONE - although not for --* qualified roles 

    if (objectRef != null)
    { if (objectRef instanceof BasicExpression)
      { BasicExpression oref = (BasicExpression) objectRef; 
        if (isSet(oref.data))
        { Expression newset = buildSetExpression(oref.data); 
          objectRef = newset; 
          // what is its element type? 
          res = objectRef.typeCheck(types,entities,contexts,env);
        }
        else if (isSequence(oref.data))  // needed?
        { Expression newset = buildSetExpression(oref.data); 
          objectRef = newset; 
          res = objectRef.typeCheck(types,entities,contexts,env);
        }
        else if (isCall(oref.data))
        { BasicExpression newcall = buildCallExpression(oref.data); 
          newcall.objectRef = oref.objectRef; 
          objectRef = newcall; 
          // contexts = { entity of result type }
          res = objectRef.typeCheck(types,entities,contexts,env);
        }
        else 
        { res = objectRef.typeCheck(types,entities,contexts,env); }
        // Also case of literal maps Map{...}. But not really needed
      }
      else 
      { res = objectRef.typeCheck(types,entities,contexts,env); }

      Entity staticent = 
        (Entity) ModelElement.lookupByName(objectRef + "", entities); 
      // System.out.println("**Type of " + this + " is static operation, of: " + staticent);

      if (staticent != null) 
      { objectRef.umlkind = Expression.CLASSID; 
        // isStatic = true; 

        BehaviouralFeature bf = staticent.getStaticOperation(data,parameters); 

        if (bf != null) 
        { if (bf.parametersMatch(parameters)) { } 
          else 
          { JOptionPane.showMessageDialog(null, "Actual parameters do not match operation formal pars: " + this + " /= " + bf, 
                                    "Type warning", JOptionPane.WARNING_MESSAGE); 
          }  

          bf.setFormalParameters(parameters); 
          System.out.println("** Setting formal parameters of " + this);

          type = bf.getResultType(); 
          elementType = bf.getElementType(); 
          entity = staticent; 
          isStatic = true; 
            
          if (bf.isQuery())
          { umlkind = QUERY; }
          else 
          { umlkind = UPDATEOP; 
            if (type == null || type.equals("") || type.equals("void"))
            { type = new Type("boolean",null); }  
          }
          // System.out.println("**Type of " + this + " is static operation call, of: " + entity);
          return true;  
        } 
        else if (staticent.hasDefinedAttribute(data))  
        { umlkind = ATTRIBUTE;
          multiplicity = ModelElement.ONE;
          Attribute att = staticent.getDefinedAttribute(data);
          if (att != null && att.isStatic())
          { modality = att.getKind(); 
            type = att.getType();
            elementType = att.getElementType(); 
            if (Type.isCollectionType(type))
            { multiplicity = ModelElement.MANY; } 
            entity = staticent;
            isStatic = true; 
            // variable = att; // For precise type-analysis

            arrayType = type; 
            adjustTypeForArrayIndex(att); 
            System.out.println("*** Type of " + data + " is static ATTRIBUTE in entity " +
                                staticent + " type is " + type + "(" + elementType + ") Modality = " + modality); 
            return res;
          } 
        } 
      } 
    
      if (objectRef.elementType != null && objectRef.elementType.isEntity(entities))
      { context.add(0,objectRef.elementType.getEntity(entities)); } 
      else if (objectRef.type != null && objectRef.type.isEntity(entities)) 
      { context.add(0,objectRef.type.getEntity(entities)); } 
    }
    else // objectRef == null
    { context.addAll(contexts); } 

    // System.out.println("Context of " + this + "(event: " + isEvent + ") is " + context);
 
    if (context.size() == 0)
    { if (entity == null) 
      { System.out.println(">> No owning class found for " + this);
        System.out.println();  
        //  + 
        //         " -- it must be a local variable/parameter");
        // System.out.println(">> Or static feature/global use case"); 
        // System.out.println(">> Static features should be prefixed by their class: C.f"); 
      } 
    } 

    // System.out.println();  


    if (isEvent && isFunction(data))
    { isEvent = false; 
      umlkind = FUNCTION; 
    } // this should not arise. 

    if (isEvent && 
        !data.equals("subrange") && 
        !data.equals("indexOf") && 
        !data.equals("setAt") && 
        !data.equals("insertAt") && 
        !data.equals("insertInto") && 
        !(data.equals("replace")) && 
        !(data.equals("replaceFirstMatch")) && 
        !(data.equals("replaceAll")) && 
        !(data.equals("replaceAllMatches")) && 
        !(data.equals("Sum")) && !(data.equals("Prd")) &&  
        !(data.equals("oclIsKindOf")) && 
        !(data.equals("oclIsTypeOf")) && 
        !(data.equals("oclAsType")))
    { // data must be an event of the owning class, the elementType of 
      // the objectRef, or of an ancestor of it. 
      BehaviouralFeature bf; 


      for (int i = 0; i < context.size(); i++) 
      { Entity e = (Entity) context.get(i); 
        bf = e.getDefinedOperation(data,parameters);
  
        if (bf != null) 
        { System.out.println("**Type of " + data + " is operation, of: " + e);
          entity = e;
          if (bf.parametersMatch(parameters)) { } 
          else 
          { JOptionPane.showMessageDialog(null, "Parameters do not match operation pars: " + this + " " + bf, 
                                    "Type warning", JOptionPane.WARNING_MESSAGE);
            continue; 
          }  

          System.out.println("** Setting formal parameters of " + data + " operation: " + parameters);
          System.out.println(); 

          bf.setFormalParameters(parameters); 

          if (bf.isQuery())
          { umlkind = QUERY; 
            type = bf.getResultType(); 
            elementType = bf.getElementType(); 
            // System.out.println("QUERY OPERATION, type: " + type); 
            // System.out.println("QUERY OPERATION, element type: " + elementType); 
          }
          else 
          { umlkind = UPDATEOP; 
            type = bf.getResultType(); 
            elementType = bf.getElementType(); 
            if (type == null || 
                type.equals("") || type.equals("void"))
            { type = new Type("boolean",null); }  
            // System.out.println("UPDATE OPERATION, type: " + type); 
          }  // shouldn't be object ref of anything. Should 
             // only occur as part of action invariant.

          setObjectRefType(); 
          
          adjustTypeForObjectRef(bf); 
          arrayType = type; 

          adjustTypeForArrayIndex(bf); 
          return true; 
        }   // type check the actual parameters, also 
        else if (e.getEventNames().contains(data))
        { umlkind = UPDATEOP;
          entity = e;  
          multiplicity = ModelElement.ONE; 
          type = new Type("boolean",null); 
          setObjectRefType(); 
          // adjustTypeForObjectRef(bf); 
          // adjustTypeForArrayIndex(bf); 
          arrayType = type; 

          return true; 
        } // else, downcast if in a subclass
        else 
        { Entity subent = e.searchForSubclassWithOperation(data); 
          if (subent != null) 
          { downcast = true; 
            entity = subent; 
            bf = subent.getOperation(data); 
            if (bf != null) 
            { type = bf.getResultType(); 
              elementType = bf.getElementType(); 
            
              bf.setFormalParameters(parameters); 

              if (bf.isQuery())
              { umlkind = QUERY; }
              else 
              { umlkind = UPDATEOP; 
                if (type == null || type.equals("") || type.equals("void"))
                { type = new Type("boolean",null); }  
              } 
              setObjectRefType(); 
              adjustTypeForObjectRef(bf); 
              arrayType = type; 
              adjustTypeForArrayIndex(bf); 
              return true;
            } 
          }  
        } 
      } 

      // If it is a function parameter of the current operation:

      Attribute fvar = (Attribute) ModelElement.lookupByName(data,env); 
      if (fvar != null) 
      { type = fvar.getType(); 
        arrayType = type; 

        if (type != null && type.isFunctionType())
        { umlkind = UPDATEOP; 
          elementType = fvar.getElementType();
          System.out.println(">> Function parameter " + data + " of type " + type + "(" + elementType + ")"); 
        } 
        else 
        { JOptionPane.showMessageDialog(null, data + " is not a function parameter or known function in " + this + ".\n" + 
            "Please re-type-check or correct your specification.", 
            "Type warning", JOptionPane.WARNING_MESSAGE);

          if (parameters != null && parameters.size() == 1)
          { // assume it is a function
            type = new Type("Function", null); 
            Expression par1 = (Expression) parameters.get(0); 
            type.keyType = par1.getType();
            type.elementType = new Type("OclAny", null);  
          } // data->apply(par1)
        }
      } // default
      else if (objectRef != null)
      { if (objectRef.type != null) 
        { String ename = objectRef.type.getName(); 
          Entity cent = 
           (Entity) ModelElement.lookupByName(ename, entities); 
          if (cent != null) 
          { BehaviouralFeature op = cent.getOperation(data); 
            if (op != null) 
            { System.out.println(">>> Found operation " + op + " of class " + cent); 
              umlkind = UPDATEOP; 
              type = op.getType();         
              elementType = op.getElementType();
            } 
          }       
        }
        else  
        { System.err.println("!Warning!: Unknown operation " + data + " at call " + this + ".\n");  
          System.out.println(">> " + objectRef + " of type: " + objectRef.type); 
          type = new Type("boolean",null);         
          elementType = new Type("boolean",null);
        } 
        umlkind = UPDATEOP;   
      }          
    }

    if (isFunction(data))
    { umlkind = FUNCTION;
      if (objectRef == null) // error
      { System.err.println("!! TYPE ERROR: OCL operator " + data +
                           " should have an argument: arg->" + data + "(pars)");
        if (parameters != null && parameters.size() > 0)
        { objectRef = (Expression) parameters.get(0); 
          parameters.remove(0); 
        } 
        else 
        { type = null;
          return false;
        } 
      }
      entity = objectRef.entity; // default
      multiplicity = ModelElement.ONE; // default 
      modality = objectRef.modality; 

      if (("Sum".equals(data) || "Prd".equals(data)) && 
          "Integer".equals(objectRef + "") && 
          parameters != null && parameters.size() > 3)
      { Expression par3 = (Expression) parameters.get(2); 
        par3.setType(new Type("int",null)); 
        par3.setElementType(new Type("int",null)); 
        Vector env1 = (Vector) ((Vector) env).clone(); 
        env1.add(new Attribute(par3 + "", 
                       par3.type, ModelElement.INTERNAL)); 
        Expression par4 = (Expression) parameters.get(3); 
        par4.typeCheck(types,entities,contexts,env1); 
        type = par4.getType(); 
        elementType = par4.getElementType(); 
        type.setElementType(elementType); 
        //  System.out.println("*** Type of " + this + " is " + type);
        return true;  
      } 

      if (data.equals("toLong") || data.equals("gcd"))
      { type = new Type("long", null); 
        elementType = type; 
      } 
      else if (data.equals("size") || data.equals("floor") || data.equals("count") ||
          data.equals("toInteger") || 
          data.equals("indexOf") || data.equals("ceil") || data.equals("round"))
      { type = new Type("int",null); 
        elementType = type; 
      }
      else if (data.equals("sort") || 
               data.equals("characters") ||
               data.equals("sortedBy") || 
               data.equals("asSequence") ||
               data.equals("allInstances"))
      { type = new Type("Sequence",null); 
        elementType = objectRef.elementType; 
        type.setElementType(elementType); 
        multiplicity = ModelElement.MANY;
        arrayType = type; 
 
        adjustTypeForArrayIndex(); 
      } 
      else if (data.equals("isDeleted") || data.equals("oclIsKindOf") || data.equals("oclIsTypeOf") ||
               data.equals("isReal") || data.equals("toBoolean") || 
               data.equals("isInteger") || data.equals("isLong") ||
               data.equals("hasPrefix") || data.equals("hasSuffix"))
      { type = new Type("boolean",null);
        elementType = type; 
      } 
      else if (data.equals("oclAsType") && parameters != null && 
               parameters.size() > 0)  // type cast
      { Expression par1 = (Expression) parameters.get(0); 
        type = par1.elementType; 
        elementType = type; 
      } 
      else if (data.equals("subrange"))
      { // 3 cases - Integer.subrange, 
        // str.subrange, col.subrange
        if ("Integer".equals(objectRef + ""))
        { type = new Type("Sequence", null); 
          elementType = new Type("int", null);
          // could be long if the 2nd argument is long
          type.setElementType(elementType);  
          multiplicity = ModelElement.MANY;
          arrayType = type; 
          adjustTypeForArrayIndex(); 
          return res;
        } 
        else if (objectRef.isString())
        { type = new Type("String", null); 
          elementType = new Type("String", null);
          // could be long if the 2nd argument is long
          type.setElementType(elementType);  
          multiplicity = ModelElement.ONE;
          arrayType = type; 
          adjustTypeForArrayIndex(); 
          return res;
        } 
        // Otherwise, assume it is a collection
        type = new Type("Sequence", null); 
        elementType = objectRef.elementType; 
        multiplicity = ModelElement.MANY;
        arrayType = type; 
        adjustTypeForArrayIndex(); 
        return res;
      } 
      else if (data.equals("reverse") || 
               data.equals("tail") || 
               data.equals("front") || 
               data.equals("insertAt") || 
               data.equals("insertInto") || 
               data.equals("setAt"))  
      { type = objectRef.getType(); // Sequence or String
        elementType = objectRef.elementType; 

        if (data.equals("insertAt") || 
            data.equals("setAt"))
        { Expression par2 = (Expression) parameters.get(1);
          Type partyp = par2.getType(); 
          Type newleftET = 
            Type.refineType(elementType,partyp); 
          System.out.println(">> Deduced element type of " + this + " = " + newleftET); 
          elementType = newleftET; 
          type.setElementType(newleftET); 
        } 

        if (type != null && 
            type.isCollectionType())
        { multiplicity = ModelElement.MANY;
          arrayType = type; 
          adjustTypeForArrayIndex(); 
          return res;
        } 
      }
      else if (data.equals("sqrt") || data.equals("exp") || data.equals("pow") ||
               data.equals("sqr") || data.equals("abs") || data.equals("toReal") ||  
               data.equals("sin") || data.equals("cos") || data.equals("cbrt") ||
               data.equals("sinh") || data.equals("cosh") || data.equals("tanh") ||
               data.equals("atan") || data.equals("acos") || data.equals("asin") ||
               data.equals("tan") || data.equals("log") || data.equals("log10"))
      { type = new Type("double",null); 
        elementType = type; 
      } 
      else if (data.equals("toLowerCase") || 
               data.equals("toUpperCase") ||
               data.equals("replace") || 
               data.equals("replaceAll") ||
               data.equals("replaceFirstMatch") || 
               data.equals("replaceAllMatches") )
      { type = new Type("String",null); 
        elementType = type; 
      }
      else if (data.equals("closure") || data.equals("asSet"))
      { type = new Type("Set",null); 
        elementType = objectRef.elementType; 
        type.setElementType(elementType); 
        multiplicity = ModelElement.MANY;
        return res; 
      }  
      else if (data.equals("subcollections"))
      { type = new Type("Set",null); 
        elementType = objectRef.getType();
        type.setElementType(elementType);  
        multiplicity = ModelElement.MANY;
        return res; 
      }  
      else // max,min,sum,prd,last,first,any
      { type = objectRef.getElementType();
        elementType = type; 
        if (data.equals("sum") || data.equals("prd")) 
        { entity = null; } 
 
        if (type == null) // objectRef is multiple but an attribute
        { type = objectRef.getType();
          Entity e = objectRef.getEntity();
          if (type == null && e != null && (objectRef instanceof BasicExpression)) 
          { type = e.getFeatureType(((BasicExpression) objectRef).data); } 
          // System.out.println("Element type of " + data + ": " + e + " " + type);
          if (type == null) // || type.getName().equals("Set"))
          { System.err.println("!!! ERROR: Can't determine element type of " + this); 
            JOptionPane.showMessageDialog(null, "ERROR: Can't determine element type of " + this,
                            "Semantic error", JOptionPane.ERROR_MESSAGE);            
            type = new Type("void",null);
          }  // actually a void or Object type. 
        }  
      }
      // entity = null;
      // System.out.println("**Type of " + this + " is " + type);
      return res;
    }  // toUpper, toLower, sqr, sqrt, exp, abs, floor, could have objectRef 
       // multiple, in which case result is multiple



    // Parameters or local variables of the 
    // current operation/usecase: 

    Attribute paramvar = (Attribute) ModelElement.lookupByName(data,env); 
    if (paramvar != null && objectRef == null) 
    { type = paramvar.getType(); 
      elementType = paramvar.getElementType(); 
      entity = paramvar.getEntity(); 
      if (entity == null && elementType != null) 
      { entity = elementType.getEntity(); } 
      if (entity == null && type != null) 
      { entity = type.getEntity(); } 
      if (elementType == null && type != null) 
      { elementType = type.getElementType(); } 
      arrayType = type; 
      adjustTypeForArrayIndex(paramvar);
      
      // System.out.println(">>> Parameter/local variable: " + this + " type= " + type + " (" + elementType + ")"); 
      // System.out.println();
      variable = paramvar;  
      umlkind = VARIABLE; 
      modality = paramvar.getKind(); 

      return true; 
    } // And adjust type for any array index. 

    for (int j = 0; j < context.size(); j++)
    { Entity ent = (Entity) context.get(j);
      if (ent.hasDefinedAttribute(data))  
      { umlkind = ATTRIBUTE;
        multiplicity = ModelElement.ONE;
        Attribute att = ent.getDefinedAttribute(data);
        if (att == null)   // something very bad has happened
        { System.err.println("!! TYPE ERROR: attribute: " + data + " is not defined in class " + ent.getName()); 
          return false; 
        } 
        modality = att.getKind(); 
        type = att.getType();
        if (att.isStatic())
        { isStatic = true; } 

        elementType = att.getElementType(); 
        if (elementType == null || 
            "OclAny".equals("" + elementType)) 
        { elementType = type.getElementType(); } 

        if (Type.isCollectionType(type))
        { multiplicity = ModelElement.MANY; } 
        entity = ent;  // may not be att.owner, but a subclass of this. 
        if (objectRef == null)
        { res = true; }
        else 
        { setObjectRefType(); } 

        System.out.println(">+>+> Attribute: " + this + " type= " + type + " (" + elementType + ")"); 
        
        /* if (arrayIndex != null) 
        { System.out.println("** Unadjusted type of " + this + " is ATTRIBUTE in entity " +
                            ent + " type is " + type + "(" + elementType + ") Modality = " + modality); 
        } */ 
        
        adjustTypeForObjectRef(att);
        arrayType = type;  

        // if (alreadyCorrected) { } 
        // else 
        // { 
        adjustTypeForArrayIndex(att); 
         
        elementType = Type.correctElementType(
                          type,elementType,types,entities); 
        // att.setElementType(elementType); 
        // variable = att; 
        System.out.println("*>>* Adjusted type of " + this + " is " + type + "(" + elementType + ")");
        System.out.println(); 
          // alreadyCorrected = true; 
        // } 

        return res;
      } // couldn't it have an array ref if objectRef was a sequence?
      else 
      { Entity subent = ent.searchForSubclassWithAttribute(data); 
        if (subent != null) 
        { umlkind = ATTRIBUTE;
          multiplicity = ModelElement.ONE;
          Attribute att = subent.getAttribute(data);
          if (att == null)   // something very bad has happened
          { System.err.println("!! TYPE ERROR: attribute: " + data + " is not defined in class " + subent); 
            JOptionPane.showMessageDialog(null, "Undefined attribute: " + data, "Type error",                                           JOptionPane.ERROR_MESSAGE);  
            return false; 
          } 
          modality = att.getKind();
          downcast = true;  
          if (att.isStatic())
          { isStatic = true; } 

          type = att.getType();
          elementType = att.getElementType(); 
          if (Type.isCollectionType(type))
          { multiplicity = ModelElement.MANY; } 
          entity = subent;
          adjustTypeForObjectRef(att);
          arrayType = type;  
          adjustTypeForArrayIndex(att); 
          // System.out.println("**Type of " + data + " is downcast ATTRIBUTE in entity " +
          //                    subent + " type is " + type); 
          // variable = att; 
          return res;
        }  
      } 

      if (ent.hasDefinedRole(data))
      { umlkind = ROLE; 
        Association ast = ent.getDefinedRole(data); 
        if (ast == null)   // something very bad has happened
        { System.err.println("!! TYPE ERROR: role: " + data + " is not defined in class " + ent.getName()); 
          JOptionPane.showMessageDialog(null, "Undefined role " + data, "Type error",                                         
		                                JOptionPane.ERROR_MESSAGE);  
          return false; 
        } 

        multiplicity = ast.getCard2();
        elementType = new Type(ast.getEntity2()); 
        modality = ModelElement.INTERNAL; // ???
        
        if (ast.isQualified() && arrayIndex == null) // a naked qualified role, it is a map
        { type = ast.getRole2Type(); }
        else if (multiplicity == ModelElement.ONE) 
        { type = new Type(ast.getEntity2()); } 
        else 
        { if (ast.isOrdered())
          { type = new Type("Sequence",null); } 
          else 
          { type = new Type("Set",null); }
          type.setElementType(elementType); 
 
          // type = new Type(ast.getEntity2());
        }   // index must be int type, 
            // and ast is ordered/sorted. Also for any att or role

        adjustTypeForObjectRef();
        arrayType = type; 
 
        if (arrayIndex != null && !ast.isQualified())  
        { adjustTypeForArrayIndex(); } 

        // if (Type.isCollectionType(type))
        // { multiplicity = ModelElement.MANY; } 
        // else 
        // { multiplicity = ModelElement.ONE; } 
        // System.out.println("**Type of " + data + " is ROLE in entity " + ent +
        //                    "\n type is: " + type + " element type: " + 
        //                    elementType + " Modality = " + modality); 
        entity = ent;
        if (objectRef == null)
        { res = true; }
        else 
        { setObjectRefType(); } 

        // variable = attribute for the role? 

        return res;
      }
      else 
      { Entity subent = ent.searchForSubclassWithRole(data); 
        if (subent != null) 
        { umlkind = ROLE;
          Association ast = subent.getRole(data);
          if (ast == null)   // something very bad has happened
          { System.err.println("!! ERROR: Undefined role: " + data); 
            return false; 
          } 
          multiplicity = ast.getCard2();
          modality = ModelElement.INTERNAL;
          downcast = true;  
          elementType = new Type(ast.getEntity2()); 
          if (multiplicity == ModelElement.ONE)
          { type = new Type(ast.getEntity2()); } 
          else 
          { if (ast.isOrdered())
            { type = new Type("Sequence",null); } 
            else
            { type = new Type("Set",null); } 
            type.setElementType(elementType); 
          }
          adjustTypeForObjectRef();
          arrayType = type;  
          if (arrayIndex != null && !ast.isQualified())  
          { adjustTypeForArrayIndex(); } 
          // System.out.println("**Type of " + data + " is downcast ROLE in entity " +
          //                    subent + " type is " + type); 
          entity = subent;

          // variable = attribute for the role? 
          return res;
        } // objectRefTypeAssignment 
      }  // or it could be a query op of the entity
    }
    // prestate == true only for VARIABLE, ATTRIBUTE, ROLE, CONSTANT

    /* Is data in some enumerated type? */ 

    for (int i = 0; i < types.size(); i++)
    { Type t = (Type) types.get(i);
      if (t != null && t.hasValue(data))
      { umlkind = VALUE;
        multiplicity = ModelElement.ONE; 
        System.out.println("*** " + data + " is enumerated literal in type " + t); 
        type = t;
        elementType = type; 
     
        if (objectRef == null) 
        { return true; }
        else if (t.getName().equals(objectRef + "")) 
        { return true; } 
      }
    }   // if T1.value and T2.value may both occur, must be distinguished by the type name

    if (data.equals("Integer"))
    { type = new Type("OclType",null); 
      elementType = new Type("int",null); 
      type.setElementType(elementType); 
      umlkind = TYPE;    
      modality = ModelElement.INTERNAL;
      multiplicity = ModelElement.MANY;
      return true; 
    }  

    Attribute var = 
      (Attribute) ModelElement.lookupByName(data,env); 
    if (var != null) 
    { type = var.getType();
      elementType = var.getElementType(); 
      umlkind = VARIABLE;  // its a parameter or local variable. 
      variable = var; 
      modality = var.getKind(); 
      if (type != null) 
      { String tname = type.getName(); 
        if (tname.equals("Set") || 
            tname.equals("Sequence") || 
            tname.equals("Map"))
        { multiplicity = ModelElement.MANY; } 
        else 
        { multiplicity = ModelElement.ONE; }   // assume
      } 
      else 
      { multiplicity = ModelElement.ONE; }   // assume
      
      arrayType = type; 
      if (arrayIndex != null) 
      { adjustTypeForArrayIndex(var); } 
	  
      if (parameters != null && 
          var.getType().isFunctionType()) // application of a Function(S,T)
      { Type ftype = var.getType(); 
        type = ftype.getElementType(); 
        elementType = type.getElementType(); 
        System.out.println(">>>> TYPE CHECKED: Type of variable expression " + this + " is " + type + " entity: " + entity); 
      }
	  
      entity = var.getEntity(); 
      if (entity == null && elementType != null) 
      { entity = elementType.getEntity(); } 
      if (entity == null && type != null) 
      { entity = type.getEntity(); } 

      return true; 
    } // entity = var.getEntity() ? 

    Entity ee = (Entity) ModelElement.lookupByName(data,entities); 
    if (ee != null) 
    { umlkind = CLASSID; 
      // System.out.println("**umlkind of " + this + " is CLASSID"); 
      modality = ModelElement.INTERNAL; 
      elementType = new Type(ee); 
      if (arrayIndex != null)  // a lookup by primary key
      { if (!arrayIndex.isMultiple()) // (arrayIndex.multiplicity == ModelElement.ONE)
        { multiplicity = ModelElement.ONE; 
          type = new Type(ee);
        } 
        else 
        { multiplicity = ModelElement.MANY; 

          if (arrayIndex.isOrdered())
          { type = new Type("Sequence", null); } 
          else 
          { type = new Type("Set",null); } 
          type.setElementType(elementType); 
        }
      }
      else 
      { multiplicity = ModelElement.MANY; 
        type = new Type("Sequence",null); 
        type.setElementType(elementType); 
      } 
      entity = ee; // ?? 
      elementType = new Type(ee);  // type.setElementType(elementType); ?? 
      return res; 
    } 

    /* if (isConstant(data))
    { umlkind = CONSTANT; 
      // System.out.println("**Type of " + data + " is CONSTANT");
      modality = ModelElement.INTERNAL; 
      multiplicity = ModelElement.ONE;   // as for variables
      return res; 
    } */  

    if (umlkind != UNKNOWN && type != null && elementType != null)    
    { return res; } 

    if (parameters != null && umlkind == UNKNOWN) // must be an operation, external, not a variable
    { umlkind = UPDATEOP; } 
    else if (parameters == null) 
    { umlkind = VARIABLE; }
    modality = ModelElement.INTERNAL; 
    multiplicity = ModelElement.ONE;   // assume 

    if (objectRef != null && entity == null) 
    { Type oret = objectRef.getElementType(); 
      if (oret != null && oret.isEntity())
      { entity = oret.getEntity(); } 
    } 

    if (isEvent && type == null && objectRef == null && 
        context.size() == 0)
    { // A usecase, or a static operation of the local class
 
      BehaviouralFeature bf; 

      for (int i = 0; i < entities.size(); i++) 
      { Entity e = (Entity) entities.get(i); 
        bf = e.getDefinedOperation(data,parameters);
  
        if (bf != null && bf.isStatic()) 
        { System.out.println("**Type of " + data + " is static operation, of class: " + e);
          entity = e;
          if (bf.parametersMatch(parameters)) { } 
          else 
          { JOptionPane.showMessageDialog(null, 
              "Parameters do not match operation pars: " + this + " " + bf, 
              "Type warning", JOptionPane.WARNING_MESSAGE);
            continue; 
          }  

          System.out.println("** Setting formal parameters of " + data + " operation: " + parameters);
          System.out.println(); 

          bf.setFormalParameters(parameters); 

          if (bf.isQuery())
          { umlkind = QUERY; 
            type = bf.getResultType(); 
            elementType = bf.getElementType(); 
            // System.out.println("QUERY OPERATION, type: " + type); 
            // System.out.println("QUERY OPERATION, element type: " + elementType); 
          }
          else 
          { umlkind = UPDATEOP; 
            type = bf.getResultType(); 
            elementType = bf.getElementType(); 
            if (type == null || 
                type.equals("") || type.equals("void"))
            { type = new Type("boolean",null); }  
            // System.out.println("UPDATE OPERATION, type: " + type); 
          }  // shouldn't be object ref of anything. Should 
             // only occur as part of action invariant.

          arrayType = type; 

          adjustTypeForArrayIndex(bf); 
          return true; 
        }   // type check the actual parameters, also 
      }
    } 

    if ("skip".equals(data))
    { } 
    else if (type == null) 
    { System.out.println("***! WARNING: Type of " + this + " is unknown");
      System.out.println("***! Re-run type-checking/correct your specification"); 
    } 
       
    return true;
  }

  private void setObjectRefType()
  { if (entity != null && objectRef != null && objectRef.umlkind == VARIABLE &&
        objectRef.getType() == null)
    { objectRef.setType(new Type(entity)); 
      objectRef.setElementType(new Type(entity)); 
    }
    else if (entity != null && objectRef != null && objectRef.umlkind == CONSTANT &&
             objectRef.getType() == null)
    { objectRef.setType(new Type(entity)); 
      objectRef.setElementType(new Type(entity)); 
    }
  } 

  private void adjustTypeForObjectRef(BehaviouralFeature bf) 
  { if (objectRef != null) 
    { if (bf.isStatic()) 
      { objectRef.multiplicity = ModelElement.ONE; 
        objectRef.umlkind = CLASSID; 
        objectRef.type = new Type(entity); 
      } 
      else if (objectRef.isMultiple())
      { Type resType = bf.getResultType(); 
        Type elemType = bf.getElementType(); 
        // Actual type is Set or Sequence 
        if (objectRef.isOrdered())
        { if (resType != null && Type.isSequenceType(resType))
          { type = new Type("Sequence",null); 
            elementType = elemType; 
            type.setElementType(elemType); 
          } 
          else if (resType == null || resType.typeMultiplicity() == ModelElement.ONE) 
          { elementType = resType; 
            type = new Type("Sequence",null); 
            type.setElementType(elementType); 
          } 
          else // set-valued operation
          { elementType = resType; 
            type = new Type("Set",null); 
            type.setElementType(elementType); 
          } 
        } 
        else // objectRef is a set  
        { if (resType != null && Type.isCollectionType(resType))
          { elementType = elemType; 
            type = new Type("Set",null); 
            type.setElementType(elementType);
          }  
          else 
          { type = new Type("Set",null); 
            elementType = resType; 
            type.setElementType(elementType); 
          } 
        }
      }  
    }
    // System.out.println("Actual type of " + this + " is " + type); 
  }  

  private void adjustTypeForObjectRef(Attribute att) 
  { if (objectRef != null) 
    { if (att.isStatic() && objectRef != null && objectRef.equals(entity + "")) 
      { objectRef.multiplicity = ModelElement.ONE; 
        objectRef.umlkind = CLASSID; 
        objectRef.type = new Type(entity); 
      } 
      else if (objectRef.isMultiple())
      { Type resType = att.getType(); 
        Type elemType = att.getElementType(); 
        // Actual type is Set or Sequence 
        if (objectRef.isOrdered())
        { if (resType != null && Type.isSequenceType(resType))
          { type = new Type("Sequence",null); 
            elementType = elemType; 
            type.setElementType(elemType); 
          } 
          else if (resType == null || resType.typeMultiplicity() == ModelElement.ONE) 
          { elementType = resType; 
            type = new Type("Sequence",null); 
            type.setElementType(elementType); 
          } 
          else // set-valued operation
          { elementType = resType; 
            type = new Type("Set",null); 
            type.setElementType(elementType); 
          } 
        } 
        else // objectRef is a set  
        { if (resType != null && Type.isCollectionType(resType))
          { elementType = elemType; 
            type = new Type("Set",null); 
            type.setElementType(elementType);
          }  
          else 
          { type = new Type("Set",null); 
            elementType = resType; 
            type.setElementType(elementType); 
          } 
        }
      }  
    }
    // System.out.println("Actual type of " + this + " is " + type); 
  }  

  private void adjustTypeForObjectRef() 
  { if (objectRef != null) 
    { if (objectRef.isMultiple())
      { Type resType = type; 
        Type elemType = elementType; 
        // Actual type is Set or Sequence 
        if (objectRef.isOrdered())
        { if (resType != null && Type.isSequenceType(resType))
          { type = new Type("Sequence",null); 
            elementType = elemType; 
            type.setElementType(elemType); 
          } 
          else if (resType == null || resType.typeMultiplicity() == ModelElement.ONE) 
          { elementType = resType; 
            type = new Type("Sequence",null); 
            type.setElementType(elementType); 
          } 
          else // set-valued feature
          { elementType = elemType; 
            type = new Type("Set",null); 
            type.setElementType(elementType); 
          } 
        } 
        else // objectRef is a set  
        { if (resType != null && Type.isCollectionType(resType))
          { elementType = elemType; 
            type = new Type("Set",null); 
            type.setElementType(elementType);
          }  
          else // single-valued feature 
          { type = new Type("Set",null); 
            elementType = resType; 
            type.setElementType(elementType); 
          } 
        }
      }  
    }
    // System.out.println("Actual type of " + this + " is " + type); 
  }  

 

  private void adjustTypeForArrayIndex()
  { // if there is an arrayIndex, make type = elementType, etc

    // System.out.println("+++ Adjusting type " + type + " " + 
    //                    elementType + " " + arrayIndex); 

    if (arrayIndex != null && type != null && 
        "String".equals(type.getName()))
    { elementType = new Type("String", null); 
      multiplicity = ModelElement.ONE; 
    } // access to a string element
    else if (arrayIndex != null) 
    { if (elementType == null) 
      { type = new Type("OclAny", null);
        elementType = new Type("OclAny", null);
        multiplicity = ModelElement.ONE;  // assume
      } // access to sequence element; no element type
      else if (Type.isMapOrCollectionType(type)) 
      { type = elementType; 
        elementType = elementType.getElementType(); 
        if (Type.isCollectionType(type))
        { multiplicity = ModelElement.MANY; } 
        else 
        { multiplicity = ModelElement.ONE; } 
      } // access to element of typed sequence
      // System.out.println("TYPE CHECKED: Type of " + this + " is " + type); 
    } 
  }  

  private void adjustTypeForArrayIndex(BehaviouralFeature bf)
  { // if there is an arrayIndex, make type = elementType, etc

    if (arrayIndex != null && "String".equals(type + ""))
    { elementType = new Type("String", null); 
      multiplicity = ModelElement.ONE; 
    } 
    else if (arrayIndex != null) 
    { if (bf.getElementType() == null) 
      { type = new Type("OclAny", null); 
        elementType = new Type("OclAny", null);
        multiplicity = ModelElement.ONE;
      } 
      else
      { type = bf.getElementType(); 
        elementType = type.getElementType(); 
           
        if (Type.isCollectionType(type))
        { multiplicity = ModelElement.MANY; } 
        else 
        { multiplicity = ModelElement.ONE; } 
      }  
    } 
  }  

  private void adjustTypeForArrayIndex(Attribute var)
  { // if there is an arrayIndex, make type = elementType, etc

    // System.out.println("+++ Adjusting type " + type + " " + 
    //        elementType + " " + arrayIndex + " " + var); 

    if (arrayIndex != null && "String".equals(type + ""))
    { elementType = new Type("String", null); 
      multiplicity = ModelElement.ONE; 
    } 
    else if (arrayIndex != null) 
    { Type elemT = var.getElementType(); 
      if (elemT == null || "OclAny".equals("" + elemT)) 
      // { elemT = type.getElementType(); }
      // if (elemT == null) 
      { type = new Type("OclAny", null);
        elementType = new Type("OclAny", null);
        multiplicity = ModelElement.ONE;  // assume
      } // Sequence access, no type
      else
      { type = (Type) elemT.clone(); 
        elementType = type.getElementType(); 
        
        if (Type.isCollectionType(type))
        { multiplicity = ModelElement.MANY; } 
        else 
        { multiplicity = ModelElement.ONE; } 
      } // Sequence access, defined type
    } 

    // System.out.println("+++ Adjusted type " + type + " " + 
    //                    elementType); 
  }  

  private boolean eventTypeCheck(Vector types, Vector entities, Vector env)
  { // data must be an event of the owning class, the elementType of 
    // the objectRef, or of an ancestor of it. 
      
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression ep = (Expression) parameters.get(i); 
        ep.typeCheck(types,entities,env); 
      } 
    }

    for (int i = 0; i < entities.size(); i++) 
    { Entity e = (Entity) entities.get(i); 
      BehaviouralFeature bf = e.getOperation(data);  
      if (bf != null) 
      { System.out.println("**Type of " + data + " is operation, of: " + e);
        entity = e;
        if (bf.isQuery())
        { umlkind = QUERY; 
          type = bf.getResultType(); 
          System.out.println("query operation, type: " + type); 
        }
        else 
        { umlkind = UPDATEOP; 
          type = new Type("boolean",null); 
          System.out.println("update operation, type: " + type); 
        }  // shouldn't be object ref of anything. Should 
           // only occur as part of action invariant.
        multiplicity = type.typeMultiplicity();  
        return true; 
      } 
      else if (e.getEventNames().contains(data))
      { umlkind = UPDATEOP;
        entity = e;  
        multiplicity = ModelElement.ONE; 
        type = new Type("boolean",null); 
        return true; 
      }
    } 
    return true; 
  }

  private boolean functionTypeCheck(Vector types, Vector entities, Vector env)
  { boolean res = true; 
    umlkind = FUNCTION;
    if (objectRef == null) // error
    { System.out.println("**Error: function " + data +
                           " must have argument");
      type = null;
      return false;
    }
    if (data.equals("size") || data.equals("floor"))
    { type = new Type("int",null); }
    else if (data.equals("sort"))
    { type = new Type("Sequence",null); 
      elementType = objectRef.elementType; 
    } 
    else if (data.equals("isDeleted") || data.equals("oclIsKindOf"))
    { type = new Type("boolean",null); } 
    else if (data.equals("sqr") || data.equals("reverse") || data.equals("abs") ||
             data.equals("tail") || data.equals("front"))  
    { type = objectRef.getType(); 
      if (type.isCollectionType())
      { elementType = objectRef.elementType; 
        multiplicity = ModelElement.MANY;
        modality = objectRef.modality; 
        return res;
      } 
    }
    else if (data.equals("sqrt") || data.equals("exp"))
    { type = new Type("double",null); } 
    else if (data.equals("toLowerCase") || data.equals("toUpperCase"))
    { type = new Type("String",null); }
    else if (data.equals("closure") || data.equals("asSet"))
    { type = new Type("Set",null); 
      elementType = objectRef.elementType; 
      multiplicity = ModelElement.MANY;
      modality = objectRef.modality; 
      return res; 
    }  
    else // max,min,sum,prd,last,first
    { type = objectRef.getElementType(); 
      if (type == null) // objectRef is multiple but an attribute
      { type = objectRef.getType();
        Entity e = objectRef.getEntity();
        if (type == null && e != null) 
        { type = e.getFeatureType(((BasicExpression) objectRef).data); } 
        System.out.println("Element type of " + data + ": " + e + " " + type);
        if (type == null || type.getName().equals("Set"))
        { type = new Type("int",null); }  // hack
      }  
    }
    entity = null;
    multiplicity = ModelElement.ONE; // not for closure though
    modality = objectRef.modality; 
    System.out.println("**Type of " + this + " is " +
                       type);
    return res;
  }

  public Expression skolemize(Expression sourceVar, java.util.Map env)
  { return this; } 

  public String cstlQueryForm(java.util.Map srcfeatureMap)
  { if (parameters != null && parameters.size() > 0) // (umlKind == QUERY || umlKind == UPDATEOP) 
    { Expression par1 = (Expression) parameters.get(0); 
      String expr = par1.cstlQueryForm(srcfeatureMap); 
      return expr + "`" + data; 
    }
    if (isString())
    { if (data.endsWith("\"") && data.startsWith("\""))
      { int dlen = data.length(); 
        return data.substring(1,dlen-2); 
      }
    }
    if (objectRef != null) 
    { String obj = objectRef.cstlQueryForm(srcfeatureMap); 
      return obj + "`" + data;
    }
    String vv = (String) srcfeatureMap.get(data); 
	System.out.println(srcfeatureMap + " " + data); 
    if (vv != null) 
    { return vv; } 
    return data; 
  } 
  
  public String cstlConditionForm(java.util.Map fmap)
  { return cstlQueryForm(fmap); }

  public String classExtentQueryForm(java.util.Map env, boolean local)
  { // umlkind == CLASSID
    String cont = "Controller.inst()"; 

    int preindex = data.indexOf('@');
    if (preindex < 0)
    { prestate = false; }
    else
    { data = data.substring(0,preindex);
      prestate = true; 
    } 

    if (arrayIndex != null) 
    { String ind = arrayIndex.queryForm(env,local);
      if ("OclFile".equals(data))
      { return "OclFile.OclFile_index.get(" + ind + ")"; } 
      if (data.equals("OclType"))
      { return "OclType.getByPKOclType(" + ind + ")"; } 
      return cont + ".get" + data + "ByPK(" + ind + ")"; 
    } 
    return cont + "." + data.toLowerCase() + "s";  
  } 

  public String classExtentQueryFormJava6(java.util.Map env, boolean local)
  { // umlkind == CLASSID
    String cont = "Controller.inst()"; 

    int preindex = data.indexOf('@');
    if (preindex < 0)
    { prestate = false; }
    else
    { data = data.substring(0,preindex);
      prestate = true; 
    } 

    if (arrayIndex != null) 
    { String ind = arrayIndex.queryFormJava6(env,local);
      if ("OclFile".equals(data))
      { return "OclFile.OclFile_index.get(" + ind + ")"; } 
      if (data.equals("OclType"))
      { return "OclType.getByPKOclType(" + ind + ")"; } 
      return cont + ".get" + data + "ByPK(" + ind + ")"; 
    } 
    return cont + "." + data.toLowerCase() + "s";  
  } 

  public String classExtentQueryFormJava7(java.util.Map env, boolean local)
  { // umlkind == CLASSID
    String cont = "Controller.inst()"; 

    int preindex = data.indexOf('@');
    if (preindex < 0)
    { prestate = false; }
    else
    { data = data.substring(0,preindex);
      prestate = true; 
    } 

    if (arrayIndex != null) 
    { String ind = arrayIndex.queryFormJava7(env,local);
      if ("OclFile".equals(data))
      { return "OclFile.OclFile_index.get(" + ind + ")"; } 
      if (data.equals("OclType"))
      { return "OclType.getByPKOclType(" + ind + ")"; } 
      return cont + ".get" + data + "ByPK(" + ind + ")"; 
    } 
    return cont + "." + data.toLowerCase() + "s";  
  } 

  public String classExtentQueryFormCSharp(java.util.Map env, boolean local)
  { // umlkind == CLASSID
    String cont = "Controller.inst()"; 

    int preindex = data.indexOf('@');
    if (preindex < 0)
    { prestate = false; }
    else
    { data = data.substring(0,preindex);
      prestate = true; 
    } 

    if (arrayIndex != null) 
    { String ind = arrayIndex.queryFormCSharp(env,local);
      if (data.equals("OclFile"))
      { return "OclFile.getOclFileByPK(" + ind + ")"; } 
      if (data.equals("OclType"))
      { return "OclType.getOclTypeByPK(" + ind + ")"; } 
        
      return cont + ".get" + data + "ByPK(" + ind + ")"; 
    } 
    return cont + ".get" + data.toLowerCase() + "_s()";  
  } 

  public String classExtentQueryFormCPP(java.util.Map env, boolean local)
  { // umlkind == CLASSID
    String cont = "Controller::inst"; 

    int preindex = data.indexOf('@');
    if (preindex < 0)
    { prestate = false; }
    else
    { data = data.substring(0,preindex);
      prestate = true; 
    } 

    if (arrayIndex != null) 
    { String ind = arrayIndex.queryFormCPP(env,local);
      if (data.equals("OclType"))
      { return "OclType::getOclTypeByPK(" + ind + ")"; } 
      if (data.equals("OclFile"))
      { return "OclFile::getOclFileByPK(" + ind + ")"; } 
      return cont + "->get" + data + "ByPK(" + ind + ")"; 
    } 
    return cont + "->get" + data.toLowerCase() + "_s()";  
  } 

  public String classqueryForm(java.util.Map env, boolean local)
  { if (umlkind == CLASSID)
    { return classExtentQueryForm(env,local); } 
    return queryForm(env,local); 
  } 

  public String classqueryFormJava6(java.util.Map env, boolean local)
  { if (umlkind == CLASSID)
    { return classExtentQueryFormJava6(env,local); } 
    return queryFormJava6(env,local); 
  } 

  public String classqueryFormJava7(java.util.Map env, boolean local)
  { if (umlkind == CLASSID)
    { return classExtentQueryFormJava7(env,local); } 
    return queryFormJava7(env,local); 
  } 

  public String classqueryFormCSharp(java.util.Map env, boolean local)
  { if (umlkind == CLASSID)
    { return classExtentQueryFormCSharp(env,local); } 
    return queryFormCSharp(env,local); 
  } 

  public String classqueryFormCPP(java.util.Map env, boolean local)
  { if (umlkind == CLASSID)
    { return classExtentQueryFormCPP(env,local); } 
    return queryFormCPP(env,local); 
  } 

  public String parametersQueryForm(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + par.classqueryForm(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } 

  public String parametersQueryFormJava6(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + 
                    par.classqueryFormJava6(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } 

  public String parametersQueryFormJava7(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + par.classqueryFormJava7(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } 

  public String parametersQueryFormCSharp(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + par.classqueryFormCSharp(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } 

  public String parametersQueryFormCPP(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + par.classqueryFormCPP(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } 

  public String queryForm(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller.inst()"; 

    if (data.equals("$act") && parameters.size() > 0)
    { return "$act_" + parameters.get(0); }
    else if (data.equals("$fin") && parameters.size() > 0)
    { return "$fin_" + parameters.get(0); }

    if ((data.equals("systemTime") || 
         data.equals("getSystemTime")) && 
        "OclDate".equals(objectRef + ""))
    { return "Set.getTime()"; } 

    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      if ("OclDate".equals(createdClass))
      { return "new Date()"; }
    } 

      
    if (umlkind == VALUE || umlkind == CONSTANT)
    { if (data.equals("Set{}") || data.equals("Sequence{}"))
      { return "new Vector()"; }    // new Set really
      if (data.equals("Map{}"))
      { return "new HashMap()"; }
	 
      if (data.equals("null")) 
      { return "null"; } 

      if (data.equals("Math_NaN")) 
      { return "Double.NaN"; } 
      if (data.equals("Math_PINFINITY")) 
      { return "Double.POSITIVE_INFINITY"; } 
      if (data.equals("Math_NINFINITY")) 
      { return "Double.NEGATIVE_INFINITY"; } 

      if (isSet(data))
      { Expression se = buildSetExpression(data); 
        return se.queryForm(env,local);
      }

      if (isSequence(data))
      { Expression se = buildSetExpression(data); 
        if (arrayIndex != null)
        { String ind = arrayIndex.queryForm(env,local); 
          String indopt = evaluateString("-",ind,"1"); 
          if (se.elementType != null)
          { if (Type.isPrimitiveType(se.elementType))
            { return se.unwrap(se.queryForm(env,local) + ".get(" + indopt + ")"); }
            else 
            { String jtype = se.elementType.getJava(); 
              return "((" + jtype + ") " + se.queryForm(env,local) + ".get(" + indopt + "))"; 
            } 
          }  
          return se.queryForm(env,local) + ".get(" + indopt + ")";
        }
        return se.queryForm(env,local); 
      } 

 	  // Also case of literal maps Map{...}[arrayIndex]

      if (isString(data) && arrayIndex != null)
      { String ind = arrayIndex.queryForm(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        return "\"" + "\" + " + data + ".charAt(" + indopt + ")"; 
      } 
      return data;
    }

    if (umlkind == VARIABLE)
    { if (data.equals("self"))  // but return "self" if it is a genuine variable
      { if (env.containsValue("self"))
        { return data; } 
        else if (type != null && type.isEntity())
        { // System.out.println("^^^^^^^^ Type of self = " + type); 
          String tname = type.getName(); 
          Object obj = env.get(tname); 
          if (obj != null) 
          { return obj + ""; }
          else 
          { return "this"; }
        }
        else 
        { return "this"; }
      } 
      else if (data.equals("now")) 
      { return "Set.getTime()"; } 
      else // entity == null) 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryForm(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (type != null)
        { if (variable != null && "String".equals(variable.getType() + ""))
          { return "(" + data + ".charAt(" + indopt + ") + \"\")"; } 
          if (arrayIndex.type != null && arrayIndex.type.getName().equals("int"))
		  // if (Type.isPrimitiveType(type))
          { return unwrap(data + ".get(" + indopt + ")"); }
		  else if (arrayIndex.type != null && arrayIndex.type.getName().equals("String"))
		  { return unwrap(data + ".get(" + ind + ")"); }
		   
          String jType = type.getJava(); 
          return "((" + jType + ") " + data + ".get(" + indopt + "))"; 
        }         
        return data + ".get(" + indopt + ")";
      } // unwrap ints, doubles, etc, also 
      else if (parameters != null && variable != null && variable.getType().isFunctionType()) // application of a Function(S,T)
      { String pars = ""; 
	    for (int h = 0; h < parameters.size(); h++) 
		{ Expression par = (Expression) parameters.get(h); 
		  pars = pars + par.queryForm(env,local); 
		  if (h < parameters.size()-1) 
		  { pars = pars + ","; } 
		} 
	    return data + ".evaluate(" + pars + ")"; 
      }
      else 
      { return data; } 
    } 

    if (umlkind == CLASSID)
    { 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryForm(env,local);
        if ("OclFile".equals(data))
        { return "OclFile.OclFile_index.get(" + ind + ")"; } 
        if (data.equals("OclType"))
        { return "OclType.getByPKOclType(" + ind + ")"; } 
      
        return cont + ".get" + data + "ByPK(" + ind + ")"; 
      } 
      return data;  // But library classes may map to language-specific name
    }

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return data; } // Invalid function

      
      if (data.equals("$act") && parameters.size() > 0)
      { return "$act_" + parameters.get(0); }

      String pre = objectRef.queryForm(env,local);
      
      if (data.equals("allInstances"))  // objectRef is a CLASSID
      { return cont + "." + pre.toLowerCase() + "s"; } 
	  

      if (objectRef.umlkind == CLASSID)
      { pre = ((BasicExpression) objectRef).classExtentQueryForm(env,local); }

      if (data.equals("asSequence"))  
      { return pre; } 

      if (data.equals("oclIsUndefined"))
      { return "(" + pre + " == null)"; } 
        
      if (data.equals("size"))
      { if (objectRef.type != null && objectRef.type.getName().equals("String"))
        { return pre + ".length()"; } 
        else 
        { return pre + ".size()"; }
      }

      if (("Sum".equals(data) || "Prd".equals(data))
          && "Integer".equals(objectRef + "") && parameters.size() > 3)
      { Expression par1 = (Expression) parameters.get(0);
        Expression par2 = (Expression) parameters.get(1);
        Expression par3 = (Expression) parameters.get(2);
        Expression par4 = (Expression) parameters.get(3);
        Type sumtype = par4.getType();
        String tname = sumtype.getName();
        BasicExpression subrexp = new BasicExpression("subrange");
        subrexp.umlkind = FUNCTION; 
        subrexp.objectRef = objectRef;
        subrexp.addParameter(par1);
        subrexp.addParameter(par2);
        Type seqtype = new Type("Sequence", null); 
        subrexp.setType(seqtype);
        Type inttype = new Type("int", null); 
        subrexp.setElementType(inttype);
        seqtype.setElementType(inttype); 
        if (("" + par3).equals("" + par4))
        { String sqf = subrexp.queryForm(env,local); 
          return "Set." + data.toLowerCase() + tname + "(" + sqf + ")"; 
        } 
        Expression colexp = new BinaryExpression("|C",new BinaryExpression(":", par3, subrexp), par4);
        String colqf = colexp.queryForm(env,local); 
        return "Set." + data.toLowerCase() + tname + "(" + colqf + ")";
      }

      if (data.equals("isDeleted"))  // assume objectRef is single object
      { return "true"; // "false" really
        /* String objstype = objectRef.type.getName();
        if (objectRef.isMultiple())
        { return "Set.intersection(" + pre + 
                                   ",Controller.inst()." + objstype.toLowerCase() + "s).size() == 0"; 
        } 
        else
        { return "!(Controller.inst()." + objstype.toLowerCase() + 
                 "s.contains(" + pre + "))"; 
        } */ 
      } 

      if (data.equals("oclIsKindOf") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        String arg1s = arg1 + "";

        if ("int".equals(arg1s) && Type.isNumericType(objectRef.type))
        { return "Set.isInteger(\"\" + " + pre + ")"; } 
        else if ("long".equals(arg1s) && Type.isNumericType(objectRef.type))
        { return "Set.isLong(\"\" + " + pre + ")"; }
        else if ("double".equals(arg1s) && Type.isNumericType(objectRef.type))
        { return "Set.isReal(\"\" + " + pre + ")"; } 

        return "(" + pre + " instanceof " + arg1s + ")";  // works for external classes also 
        // return "(Controller.inst()." + arg1s.toLowerCase() + "s.contains(" + pre + "))"; 
      } 

      if (data.equals("oclAsType") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        return "((" + arg1 + ") " + pre + ")";  // casting 
      } 

      if (data.equals("sqr"))
      { return "(" + pre + ") * (" + pre + ")"; } 
      else if (data.equals("cbrt"))
      { return "Math.pow(" + pre + ", 1.0/3)"; } 
      else if (data.equals("log10"))
      { return "(Math.log(" + pre + ")/Math.log(10))"; } 
      else if (data.equals("abs") || data.equals("sin") || data.equals("cos") ||
          data.equals("sqrt") || data.equals("exp") || data.equals("log") ||
          data.equals("tan")  || 
          data.equals("atan") || data.equals("acos") || data.equals("asin") ||
          data.equals("cosh") || data.equals("sinh") ||
          data.equals("tanh"))  
          // exp, floor can be applied to sets/sequences
      { return "Math." + data + "(" + pre + ")"; }
      else if (data.equals("round") || data.equals("ceil") || data.equals("floor"))
      { return "((int) Math." + data + "(" + pre + "))"; }   // (long) is better
      else if (data.equals("toUpperCase") || data.equals("toLowerCase"))
      { return pre + "." + data + "()"; } 
      else if (data.equals("sum"))
      { Type sumtype = objectRef.getElementType();
        // Must be int, long, double, String only 
        String tname = sumtype.getName(); 
        return "Set.sum" + tname + "(" + pre + ")"; 
      }
      else if (data.equals("prd"))
      { Type sumtype = objectRef.getElementType();
        // Must be int, long, double only 
        String tname = sumtype.getName(); 
        return "Set.prd" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("reverse") || data.equals("sort") ||
               data.equals("asSet") || data.equals("characters")) 
      { return "Set." + data + "(" + pre + ")"; }   // sortedBy? 
      else if (data.equals("subrange") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        if ("Integer".equals(objectRef + ""))
        { return "Set.integerSubrange(" + par1 + "," + par2 + ")"; } 
        return "Set.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("subrange") && parameters != null && parameters.size() == 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local);
        UnaryExpression ue = new UnaryExpression("->size", objectRef); 
        ue.setType(new Type("int", null)); 
        String par2 = ue.queryForm(env,local); 
        return "Set.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("indexOf") && parameters != null && parameters.size() > 0)  
      // for strings or sequences
      { Expression par1 = (Expression) parameters.get(0); 
        String qpar1 = par1.queryForm(env,local); 
        String wpar1 = par1.wrap(qpar1); 
        return "(" + pre + ".indexOf(" + wpar1 + ") + 1)"; 
      }  // Surely it should be qpar1 not wpar1 here.     
      else if (data.equals("pow") && parameters != null && parameters.size() > 0)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        return "Math.pow(" + pre + ", " + par1 + ")"; 
      }      
      else if (data.equals("closure"))
      { String rel = ((BasicExpression) objectRef).data;
        Expression arg = ((BasicExpression) objectRef).objectRef;
        if (arg != null) 
        { return "Set.closure" + entity.getName() + rel + "(" + arg.queryForm(env,local) + ")"; }   
        return "Set.closure" + entity.getName() + rel + "(" + pre + ")";
      }  // entity may be a subclass of the owner of rel. 
      else if (data.equals("insertAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.insertAt(" + pre + "," + par1 + "," + par2 + ")"; 
        // ".add(" + par1 + ", " + par2 + ")"; 
      }
      else if (data.equals("setAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.setAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replace") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.replace(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAll") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAllMatches") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceFirstMatch") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryForm(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryForm(env,local); 
        return "Set.replaceFirstMatch(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("count") && parameters != null && parameters.size() > 0)  
      // for strings or collections
      { String par1 = 
            ((Expression) parameters.get(0)).queryForm(env,local);
        return "Set.count(" + pre + "," + par1 + ")";  
      }    
      else if (objectRef != null && (objectRef.getType() + "").equals("String"))
      { // last,first,front,tail on strings
        if (data.equals("first") || data.equals("any"))
        { return "(" + pre + ".charAt(0) + \"\")"; } 
        if (data.equals("last"))
        { return "(" + pre + ".charAt(" + pre + ".length()-1) + \"\")"; } 
        if (data.equals("front"))
        { return pre + ".substring(0," + pre + ".length()-1)"; } 
        if (data.equals("tail"))
        { return pre + ".substring(1," + pre + ".length())"; } 
      } 
      else if (data.equals("max") || data.equals("min"))
      { Type compertype = objectRef.getElementType();   
        if (compertype == null)
        { compertype = type; } 
        String ctname = compertype.getName();   // only int, double, String are valid
        if ("int".equals(ctname))
        { return "((Integer) Set." + data + "(" + pre + ")).intValue()"; }
        else if ("double".equals(ctname))
        { return "((Double) Set." + data + "(" + pre + ")).doubleValue()"; }
        else if ("long".equals(ctname))
        { return "((Long) Set." + data + "(" + pre + ")).longValue()"; }
        else 
        { return "((" + ctname + ") Set." + data + "(" + pre + "))"; }
      } 
      else if (data.equals("any") || data.equals("last") || data.equals("first"))
      { Type et = objectRef.elementType; 
        if (et != null) 
        { if ("String".equals("" + et) || et.isEntity())
          { return "((" + et + ") Set." + data + "(" + pre + "))"; }
          else 
          { return unwrap("Set." + data + "(" + pre + ")"); } 
        } 
        else 
        { return "Set." + data + "(" + pre + ")"; }
      }  
      else  // sum, prd, front tail on sequences, subcollections 
          // -- separate versions for numbers & objects?
      { return "Set." + data + "(" + pre + ")"; }
    }  // and s.count(x) in Java

    if (objectRef != null && isEvent && "equivalent".equals(data))  // obj.equivalent()
    { String arg = objectRef.queryForm(env,local);
      ename = entity.getName(); 
      if (objectRef.isMultiple()) 
      { return "(($OUT) $Trace.getAlltarget(" + ename + ".getAll$trace(" + arg + ")).get(0))"; } 
      else 
      { return "(($OUT) $Trace.getAlltarget(" + arg + ".get$trace()).get(0))"; } 
    }  // likewise for equivalents

    if (umlkind == UPDATEOP) 
    { System.err.println("WARNING: Update operations should not be used in query expression:\n " + this); 
      // JOptionPane.showMessageDialog(null, "Update operation in query context: " + this, 
      //   "Poor specification style", JOptionPane.WARNING_MESSAGE);  

      // return this;   // or return its postcondition if not recursive. 
    } 

    if (umlkind == QUERY || umlkind == UPDATEOP) 
    { String res = data + "("; 
      String parString = parametersQueryForm(env,local); 

      if (entity != null) 
      { ename = entity.getName(); } 

      if (entity != null && entity.isExternalApplication())
      { res = ename + "." + cont + "." + res + parString; 
        return res; 
      } 
      else if (entity != null && entity.isClassScope(data))
      { res = ename + "." + res + parString; 
        return res; 
      } 
      else if (objectRef != null)
      { String pre = objectRef.queryForm(env,local);
        if (objectRef.umlkind == CLASSID)
        { pre = ((BasicExpression) objectRef).classExtentQueryForm(env,local); }

        if (objectRef.isMultiple()) // iterate, assume parameters.size() > 0
        { if (parameters == null || parameters.size() == 0) 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + ")"; } 
          else 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + "," + parString; } 
        } 
        else
        { if (downcast)
          { pre = "((" + ename + ") " + pre + ")"; } 
          res = pre + "." + res + parString;
        }
        return res;  
      }
      String var = findEntityVariable(env);
      if (var == null || "".equals(var))
      { return "Controller.inst()." + res + parString; } 
      return var + "." + res + parString; 
    } // may also be an arrayIndex
      

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { // String etype = elementType + ""; 
            String ind = arrayIndex.queryForm(env,local); 
            if (isQualified())
            { return "get" + data + "(" + ind + ")"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrap(data + ".get(" + indopt + ")"); } 
            else 
            { String jtype = type.getJava();
              return "((" + jtype + ") " + data + ".get(" + indopt + "))"; 
            }
          }
          return data; 
        }

        if (entity == null) { return data; } 

        String nme = entity.getName();
        
        if (entity.isClassScope(data))   // entity cannot be an interface
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryForm(env,local); 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   nme + "." + data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrap(nme + "." + data + ".get(" + indopt + ")"); } 
            else 
            { String jtype = type.getJava();
              return "((" + jtype + ") " + nme + "." + data + ".get(" + indopt + "))"; 
            }
          } 
          return nme + "." + data; 
        }  
        
        String var = findEntityVariable(env);

        String res = var + ".get" + data + "()";
        if (var == null || var.length() == 0)
        { res = data; }

        if (arrayIndex != null) 
        { String etype = elementType + ""; 
          String ind = arrayIndex.queryForm(env,local); 
          if (isQualified())
          { return var + ".get" + data + "(" + ind + ")"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (type.getName().equals("String"))
          { return "(\"\" + " + 
                   res + ".charAt(" + indopt + "))";
          } 
          else if (Type.isPrimitiveType(type))
          { return unwrap(res + ".get(" + indopt + ")"); } 
          else 
          { String jtype = type.getJava();
            return "((" + jtype + ") " + res + ".get(" + indopt + "))"; 
          }
        } // not for strings 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String eref = ename; 
      if (entity.isInterface()) { eref = ename + "." + ename + "Ops"; } 

      String res = "";
      String pre = objectRef.queryForm(env,local);
      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { if (refindex.isMultiple())
          { if (refindex.isOrdered())
            { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }
            else 
            { res = eref + ".getAll" + data + "(" + pre + ")"; }  // E[inds].data
          } 
          else 
          { res = pre + ".get" + data + "()"; }  // E[ind].data
        } 
        else if (entity.isClassScope(data))
        { res = pre + ".get" + data + "()"; }   // E.data
        else 
        { pre = cont + "." + ename.toLowerCase() + "s"; 
          res = eref + ".getAll" + data + "(" + pre + ")";
        } 
      } 
      else if (objectRef.isMultiple())
      { if (isOrdered())
        { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }
        else
        { res = eref + ".getAll" + data + "(" + pre + ")"; }
      }
      else
      { if (downcast)
        { pre = "((" + ename + ") " + pre + ")"; } 
        res = pre + ".get" + data + "()";
      }

      if (arrayIndex != null) 
      { String ind = arrayIndex.queryForm(env,local); 

        if (isQualified())
        { return "(" + res + ").get(" + ind + ")"; } 
        String indopt = evaluateString("-",ind,"1"); // not for qualified, Strings

        if (type.getName().equals("String"))
        { return "(\"\" + " + 
                 res + ".charAt(" + indopt + "))";
        } 
        else if (Type.isPrimitiveType(type))
        { return unwrap(res + ".get(" + indopt + ")"); } 
        else 
        { String jtype = type.getJava();
          return "((" + jtype + ") (" + res + ".get(" + indopt + ")))";
        } 
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  } 

  public String queryFormJava6(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller.inst()"; 

    if ((data.equals("systemTime") || 
         data.equals("getSystemTime")) && 
        "OclDate".equals(objectRef + ""))
    { return "Set.getTime()"; } 

    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      if ("OclDate".equals(createdClass))
      { return "new Date()"; }
    } 

    if (type != null && type.isEnumeration() && 
        type.hasValue(data))
    { return type.getName() + "." + data; } 

    if (umlkind == VALUE || umlkind == CONSTANT)
    { if (data.equals("Set{}")) 
      { return "new HashSet()"; }    // new Set really
      if (data.equals("Sequence{}"))
      { return "new ArrayList()"; } 
      if (data.equals("Map{}"))
      { return "new HashMap()"; }
	 
      if (data.equals("null")) 
      { return "null"; } 

      if (data.equals("Math_NaN")) 
      { return "Double.NaN"; } 
      if (data.equals("Math_PINFINITY")) 
      { return "Double.POSITIVE_INFINITY"; } 
      if (data.equals("Math_NINFINITY")) 
      { return "Double.NEGATIVE_INFINITY"; } 

      if (isSet(data))
      { Expression se = buildSetExpression(data); 
        return se.queryFormJava6(env,local);
      }
	  
      if (isSequence(data))
      { Expression se = buildSetExpression(data); 
        if (arrayIndex != null)
        { String ind = arrayIndex.queryFormJava6(env,local); 
          String indopt = evaluateString("-",ind,"1"); 
          if (se.elementType != null)
          { if (Type.isPrimitiveType(se.elementType))
            { return se.unwrapJava6(
                se.queryFormJava6(env,local) + ".get(" + indopt + ")"); 
            }
            else 
            { String jtype = se.elementType.getJava6(); 
              return "((" + jtype + ") " + se.queryFormJava6(env,local) + ".get(" + indopt + "))"; 
            } 
          }  
          return se.queryFormJava6(env,local) + ".get(" + indopt + ")";
        }  // primitive types
        return se.queryFormJava6(env,local); 
      } 

  	  // Also case of literal maps Map{...}[arrayIndex]

      if (isString(data) && arrayIndex != null)
      { String ind = arrayIndex.queryFormJava6(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        return "\"" + "\" + " + data + ".charAt(" + indopt + ")"; 
      } 
      return data;
    }

    if (umlkind == VARIABLE)
    { if (data.equals("self"))  // but return "self" if it is a genuine variable
      { if (env.containsValue("self"))
        { return data; } 
        // else if (entity != null)  // Assume it is the most local instance in scope
        // { String enme = entity.getName(); 
        //   String var = (String) env.get(enme);
        //   if (var == null) { return "this"; } 
        //   return var; 
        // } 
        else 
        { return "this"; }
      } 
      else // entity == null) 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryFormJava6(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (type != null)
        { if (variable != null && "String".equals(variable.getType() + ""))
          { return "(" + data + ".charAt(" + indopt + ") + \"\")"; } 
          
          if (arrayIndex.type != null && arrayIndex.type.getName().equals("int"))
		  // if (Type.isPrimitiveType(type))
          { return unwrapJava6(data + ".get(" + indopt + ")"); }
          else if (arrayIndex.type != null && arrayIndex.type.getName().equals("String"))
          { return unwrapJava6(data + ".get(" + ind + ")"); }
          else if (Type.isPrimitiveType(type))
          { return unwrapJava6(data + ".get(" + indopt + ")"); } 
          String jType = type.getJava6(); 
          return "((" + jType + ") " + data + ".get(" + indopt + "))"; 
        }         
        return data + ".get(" + indopt + ")";
      } // unwrap it, also for primitive types
      else if (parameters != null && variable != null && variable.getType().isFunctionType()) // application of a Function(S,T)
      { String pars = ""; 
        for (int h = 0; h < parameters.size(); h++) 
        { Expression par = (Expression) parameters.get(h); 
          pars = pars + par.queryFormJava6(env,local); 
          if (h < parameters.size()-1) 
          { pars = pars + ","; } 
        } 
        return data + ".evaluate(" + pars + ")"; 
      }
      else 
      { return data; }  
    } 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { String ind = arrayIndex.queryFormJava6(env,local);
        if ("OclFile".equals(data))
        { return "OclFile.OclFile_index.get(" + ind + ")"; } 
        if (data.equals("OclType"))
        { return "OclType.getByPKOclType(" + ind + ")"; } 
      
        return cont + ".get" + data + "ByPK(" + ind + ")"; 
      } 
      return data;  // But library classes may map to language-specific name
    }

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return data; } // Invalid function

      String pre = objectRef.queryFormJava6(env,local);

      if (data.equals("allInstances"))  // objectRef is a CLASSID
      { return cont + "." + pre.toLowerCase() + "s"; } 

      if (objectRef.umlkind == CLASSID)
      { pre = ((BasicExpression) objectRef).classExtentQueryFormJava6(env,local); }
 
      if (data.equals("size"))
      { if (objectRef.type != null && objectRef.type.getName().equals("String"))
        { return pre + ".length()"; } 
        else 
        { return pre + ".size()"; }
      }

      if (("Sum".equals(data) || "Prd".equals(data))
          && "Integer".equals(objectRef + "") && parameters != null && parameters.size() > 3)
      { Expression par1 = (Expression) parameters.get(0);
        Expression par2 = (Expression) parameters.get(1);
        Expression par3 = (Expression) parameters.get(2);
        Expression par4 = (Expression) parameters.get(3);
        Type sumtype = par4.getType();
        String tname = sumtype.getName();
        BasicExpression subrexp = new BasicExpression("subrange");
        subrexp.umlkind = FUNCTION; 
        subrexp.objectRef = objectRef;
        subrexp.addParameter(par1);
        subrexp.addParameter(par2);
        Type seqtype = new Type("Sequence", null); 
        subrexp.setType(seqtype);
        Type inttype = new Type("int", null); 
        subrexp.setElementType(inttype);
        seqtype.setElementType(inttype); 
        if (("" + par3).equals("" + par4))
        { String sqf = subrexp.queryFormJava6(env,local); 
          return "Set." + data.toLowerCase() + tname + "(" + sqf + ")"; 
        } 
        Expression colexp = new BinaryExpression("|C",new BinaryExpression(":", par3, subrexp), par4);
        String colqf = colexp.queryFormJava6(env,local); 
        return "Set." + data.toLowerCase() + tname + "(" + colqf + ")";
      }

      if (data.equals("isDeleted"))  // assume objectRef is single object
      { return "true"; // "false" really
        /* String objstype = objectRef.type.getName();
        if (objectRef.isMultiple())
        { return "Set.intersection(" + pre + 
                    ",Controller.inst()." + objstype.toLowerCase() + "s).size() == 0"; 
        } 
        else
        { return "!(Controller.inst()." + objstype.toLowerCase() + 
                 "s.contains(" + pre + "))"; 
        } */ 
      } 

      if (data.equals("asSequence")) 
      { return "Set.asSequence(" + pre + ")"; } 

      if (data.equals("oclIsUndefined"))
      { return "(" + pre + " == null)"; } 

      if (data.equals("oclIsKindOf") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        String arg1s = arg1 + ""; 
        return "(" + pre + " instanceof " + arg1s + ")";  // works for external classes also 
        // return "(Controller.inst()." + arg1s.toLowerCase() + "s.contains(" + pre + "))"; 
      } 
      if (data.equals("oclAsType") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        return "((" + arg1 + ") " + pre + ")";  // casting 
      } 
      if (data.equals("sqr"))
      { return "(" + pre + ") * (" + pre + ")"; } 
      else if (data.equals("abs") || data.equals("sin") || data.equals("cos") ||
          data.equals("sqrt") || data.equals("exp") || data.equals("log") || data.equals("log10") ||
          data.equals("tan") || data.equals("asin") || data.equals("acos") || data.equals("cbrt") ||
          data.equals("atan") || data.equals("sinh") || data.equals("cosh") || data.equals("tanh"))  
          // exp, floor can be applied to sets/sequences
      { return "Math." + data + "(" + pre + ")"; }
      else if (data.equals("round") || data.equals("ceil") || data.equals("floor"))
      { return "((int) Math." + data + "(" + pre + "))"; } 
      else if (data.equals("toUpperCase") || data.equals("toLowerCase"))
      { return pre + "." + data + "()"; } 
      else if (data.equals("sum"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // must be int, double, long, String
        return "Set.sum" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("prd"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // must be int, double, long
        return "Set.prd" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("reverse") || data.equals("sort") ||
               data.equals("asSet") || data.equals("characters")) 
      { return "Set." + data + "(" + pre + ")"; }  // what about an index? 
      else if (data.equals("subrange") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        if ("Integer".equals(objectRef + ""))
        { return "Set.integerSubrange(" + par1 + "," + par2 + ")"; } 
        return "Set.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("subrange") && parameters != null && parameters.size() == 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local);
        UnaryExpression ue = new UnaryExpression("->size", objectRef); 
        ue.setType(new Type("int", null)); 
        String par2 = ue.queryFormJava6(env,local); 
        return "Set.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("indexOf") && parameters != null && parameters.size() > 0)  
      // for strings or sequences
      { Expression par1 = (Expression) parameters.get(0); 
        String spar1 = par1.queryFormJava6(env,local); 
        String wpar1 = par1.wrap(spar1); 
        return "(" + pre + ".indexOf(" + wpar1 + ") + 1)"; 
      }      
      else if (data.equals("pow") && parameters != null && parameters.size() > 0)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        return "Math.pow(" + pre + ", " + par1 + ")"; 
      }      
      else if (data.equals("closure"))
      { String rel = ((BasicExpression) objectRef).data;
        Expression arg = ((BasicExpression) objectRef).objectRef;
        if (arg != null) 
        { return "Set.closure" + entity.getName() + rel + "(" + arg.queryFormJava6(env,local) + ")"; }   
        return "Set.closure" + entity.getName() + rel + "(" + pre + ")";
      } 
      else if (data.equals("insertAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.insertAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("setAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.setAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replace") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.replace(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAll") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAllMatches") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceFirstMatch") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava6(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava6(env,local); 
        return "Set.replaceFirstMatch(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("count"))  // for strings or collections
      { if (parameters != null && parameters.size() > 0)
        { String par1 = 
            ((Expression) parameters.get(0)).queryFormJava6(env,local);
          return "Set.count(" + pre + "," + par1 + ")";  
        } 
      }   
      else if (objectRef != null && (objectRef.getType() + "").equals("String"))
      { // last,first,front,tail on strings
        if (data.equals("first") || data.equals("any"))
        { return "(" + pre + ".charAt(0) + \"\")"; } 
        if (data.equals("last"))
        { return "(" + pre + ".charAt(" + pre + ".length()-1) + \"\")"; } 
        if (data.equals("front"))
        { return pre + ".substring(0," + pre + ".length()-1)"; } 
        if (data.equals("tail"))
        { return pre + ".substring(1," + pre + ".length())"; } 
      } 
      else if (data.equals("max") || data.equals("min"))
      { Type compertype = objectRef.getElementType();   
        String ctname = compertype.getName();   // only int, double, String are valid
        if ("int".equals(ctname))
        { return "((Integer) Set." + data + "(" + pre + ")).intValue()"; }
        else if ("long".equals(ctname))
        { return "((Long) Set." + data + "(" + pre + ")).longValue()"; }
        else if ("double".equals(ctname))
        { return "((Double) Set." + data + "(" + pre + ")).doubleValue()"; }
        else 
        { return "((" + ctname + ") Set." + data + "(" + pre + "))"; }
      } 
      else if (data.equals("any") || data.equals("last") || data.equals("first"))
      { Type et = objectRef.elementType; 
        if (et != null) 
        { if ("String".equals("" + et) || et.isEntity())
          { return "((" + et + ") Set." + data + "(" + pre + "))"; }
          else 
          { return unwrapJava6("Set." + data + "(" + pre + ")"); } 
        } 
        else 
        { return "Set." + data + "(" + pre + ")"; }
      }  
      else  // sum, prd, front tail on sequences, subcollections 
          // -- separate versions for numbers & objects?
      { return "Set." + data + "(" + pre + ")"; }
    }  // and s.count(x) in Java

    if (umlkind == QUERY || umlkind == UPDATEOP) 
    { String res = data + "("; 
      String parString = parametersQueryFormJava6(env,local); 

      if (entity != null) 
      { ename = entity.getName(); } 

      if (entity != null && entity.isExternalApplication())
      { res = ename + "." + cont + "." + res + parString; 
        return res; 
      } 
      else if (entity != null && entity.isClassScope(data))
      { res = ename + "." + res + parString; 
        return res; 
      } 
      else if (objectRef != null)
      { String pre = objectRef.queryFormJava6(env,local);
        if (objectRef.umlkind == CLASSID)
        { pre = ((BasicExpression) objectRef).classExtentQueryFormJava6(env,local); }

        if (objectRef.isMultiple()) // iterate, assume parameters.size() > 0
        { if (parameters == null || parameters.size() == 0) 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + ")"; } 
          else 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + "," + parString; } 
        } 
        else
        { if (downcast)
          { pre = "((" + ename + ") " + pre + ")"; } 
          res = pre + "." + res + parString;
        }
        return res;  
      }
	  
      String var = findEntityVariable(env);
      if (var != null && var.length() > 0) 
	  { return var + "." + res + parString; } 
	  else 
	  { return "Controller.inst()." + res + parString; }
    } // may also be an arrayIndex
      

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava6(env,local); 
            if (isQualified())
            { return "get" + data + "(" + ind + ")"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrapJava6(data + ".get(" + indopt + ")"); } 
            String jType = type.getJava6(); 
            return "((" + jType + ") " + data + ".get(" + indopt + "))"; 
          } 
          return data; 
        } 

        if (entity == null) { return data; } 

        String nme = entity.getName();

        if (entity.isClassScope(data))   // entity cannot be an interface
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava6(env,local); 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   nme + "." + data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrapJava6(nme + "." + data + ".get(" + indopt + ")"); } 
            else 
            { String jtype = type.getJava6();
              return "((" + jtype + ") " + nme + "." + data + ".get(" + indopt + "))"; 
            }
          } 
          return nme + "." + data; 
        }  
        
        String var = findEntityVariable(env);

        String res = var + ".get" + data + "()";
        if (var == null || var.length() == 0)
        { res = data; }

        if (arrayIndex != null) 
        { String etype = elementType + ""; 
          String ind = arrayIndex.queryFormJava6(env,local); 
          if (isQualified())
          { return var + ".get" + data + "(" + ind + ")"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (type.getName().equals("String"))
          { return "(\"\" + " + 
                   res + ".charAt(" + indopt + "))";
          } 
          else if (Type.isPrimitiveType(type))
          { return unwrapJava6(var + ".get" + data + "().get(" + indopt + ")"); } 
          String jType = type.getJava6(); 
          return "((" + jType + ") " + var + ".get" + data + "().get(" + indopt + "))"; 
        } // not for strings; unwrap primitives 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String eref = ename; 
      if (entity.isInterface()) { eref = ename + "." + ename + "Ops"; } 

      String res = "";
      String pre = objectRef.queryFormJava6(env,local);
      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { if (refindex.isMultiple())
          { if (isOrdered())
            { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }  // E[inds].data
            else 
            { res = eref + ".getAll" + data + "(" + pre + ")"; }  // E[inds].data
          } 
          else 
          { res = pre + ".get" + data + "()"; }  // E[ind].data
        } 
        else if (entity.isClassScope(data))
        { res = pre + ".get" + data + "()"; }   // E.data
        else 
        { pre = cont + "." + ename.toLowerCase() + "s"; 
          res = eref + ".getAll" + data + "(" + pre + ")";
        } 
      } 
      else if (objectRef.isMultiple())
      { if (isOrdered())
        { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }
        else
        { res = eref + ".getAll" + data + "(" + pre + ")"; }
      }
      else
      { if (downcast)
        { pre = "((" + ename + ") " + pre + ")"; } 
        res = pre + ".get" + data + "()";
      }

      if (arrayIndex != null) 
      { String ind = arrayIndex.queryFormJava6(env,local); 
        if (isQualified())
        { return "(" + res + ").get(" + ind + ")"; } 
        String indopt = evaluateString("-",ind,"1"); // not for qualified ones
        if (type.getName().equals("String"))
        { return "(\"\" + " + 
                 res + ".charAt(" + indopt + "))";
        } 
        else if (Type.isPrimitiveType(type))
        { return unwrapJava6(res + ".get(" + indopt + ")"); } 
        String jType = type.getJava6(); 
        return "((" + jType + ") " + res + ".get(" + indopt + "))"; 
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  }


  // should extend to allow class names in constrants, also 
  // action names -- check they are in the class of the objectRef

  public String queryFormJava7(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller.inst()"; 

    if ((data.equals("systemTime") || 
         data.equals("getSystemTime")) && 
        "OclDate".equals(objectRef + ""))
    { return "Ocl.getTime()"; } 

    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      if ("OclDate".equals(createdClass))
      { return "new Date()"; }
    } 

    if (umlkind == VALUE || umlkind == CONSTANT)
    { if (data.equals("{}") || data.equals("Set{}")) 
      { return "new HashSet<Object>()"; }    // new Set really
      if (data.equals("Sequence{}"))
      { return "new ArrayList<Object>()"; } 
      if (data.equals("Map{}"))
      { return "new HashMap<Object,Object>()"; }
	  
      if (data.equals("null")) 
      { return "null"; } 

      if (data.equals("Math_NaN")) 
      { return "Double.NaN"; } 
      if (data.equals("Math_PINFINITY")) 
      { return "Double.POSITIVE_INFINITY"; } 
      if (data.equals("Math_NINFINITY")) 
      { return "Double.NEGATIVE_INFINITY"; } 

      if (isSet(data))
      { Expression se = buildSetExpression(data); 
        return se.queryFormJava7(env,local);
      }

      if (isSequence(data))
      { Expression se = buildSetExpression(data); 
        if (arrayIndex != null)
        { String ind = arrayIndex.queryFormJava7(env,local); 
          String indopt = evaluateString("-",ind,"1"); 
          if (se.elementType != null)
          { if (Type.isPrimitiveType(se.elementType))
            { return se.unwrap(se.queryFormJava7(env,local) + ".get(" + indopt + ")"); }
            else 
            { String jtype = se.elementType.getJava7(se.elementType.getElementType()); 
              return "((" + jtype + ") " + se.queryFormJava7(env,local) + ".get(" + indopt + "))"; 
            } 
          }  
          return se.queryFormJava7(env,local) + ".get(" + indopt + ")";
        }  // primitive types
        return se.queryFormJava7(env,local); 
      } 
	  
      if (isString(data) && arrayIndex != null)
      { String ind = arrayIndex.queryFormJava7(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        return "(\"" + "\" + " + data + ".charAt(" + indopt + "))"; 
      } 
      return data;
    }
	
	// Also case of literal maps Map{...}[arrayIndex]

    if (umlkind == VARIABLE)
    { if (data.equals("self"))  // but return "self" if it is a genuine variable
      { if (env.containsValue("self"))
        { return data; } 
        // else if (entity != null)  // Assume it is the most local instance in scope
        // { String enme = entity.getName(); 
        //   String var = (String) env.get(enme);
        //   if (var == null) { return "this"; } 
        //   return var; 
        // } 
        else 
        { return "this"; }
      } 
      else // entity == null) 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryFormJava7(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (type != null)
        { if (variable != null && "String".equals(variable.getType() + ""))
          { return "(" + data + ".charAt(" + indopt + ") + \"\")"; } 

          if (arrayIndex.type != null && 
              arrayIndex.type.getName().equals("int"))
		  // if (Type.isPrimitiveType(type))
          { return unwrap(data + ".get(" + indopt + ")"); }
          else if (arrayIndex.type != null && arrayIndex.type.getName().equals("String"))
          { return unwrap(data + ".get(\"\" + " + ind + ")"); }
          else if (arrayType != null && arrayType.isMap())
          { return unwrap(data + ".get(" + ind + ")"); }
          else if (Type.isPrimitiveType(type))
          { return unwrap(data + ".get(" + indopt + ")"); } 
          String jType = type.getJava7(elementType); 
          return "((" + jType + ") " + data + ".get(" + indopt + "))"; 
        }         
        return data + ".get(" + indopt + ")";
      } // unwrap it, also for primitive types
      else if (parameters != null && variable != null)
      { String pars = ""; 
        for (int h = 0; h < parameters.size(); h++) 
        { Expression par = (Expression) parameters.get(h); 
          pars = pars + par.queryFormJava7(env,local); 
          if (h < parameters.size()-1) 
          { pars = pars + ","; } 
        }
		
        if (variable.getType() != null && variable.getType().isFunctionType()) // application of a Function(S,T)
        {  
          return data + ".evaluate(" + pars + ")"; 
        }
        else 
        { System.err.println("!! ERROR: function application " + this + " should be expressed as " + data + "->apply(" + pars + ")");
          return "((Evaluation<Object,Object>) " + data + ").evaluate(" + pars + ")"; 
        }

      }
      else 
      { return data; }  
    } 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { String ind = arrayIndex.queryFormJava7(env,local);
        if ("OclFile".equals(data))
        { return "OclFile.OclFile_index.get(" + ind + ")"; } 
        if (data.equals("OclType"))
        { return "OclType.getByPKOclType(" + ind + ")"; } 
      
        return cont + ".get" + data + "ByPK(" + ind + ")"; 
      } 
      return data;  // But library classes may map to language-specific name
    }

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return data; } // Invalid function

      String pre = objectRef.queryFormJava7(env,local);

      if (data.equals("allInstances"))  // objectRef is a CLASSID
      { return cont + "." + pre.toLowerCase() + "s"; } 

      if (objectRef.umlkind == CLASSID)
      { pre = ((BasicExpression) objectRef).classExtentQueryFormJava7(env,local); }
 
      if (data.equals("size"))
      { if (objectRef.type != null && objectRef.type.getName().equals("String"))
        { return pre + ".length()"; } 
        else 
        { return pre + ".size()"; }
      }

      if (("Sum".equals(data) || "Prd".equals(data))
          && "Integer".equals(objectRef + "") && parameters != null && parameters.size() > 3)
      { Expression par1 = (Expression) parameters.get(0);
        Expression par2 = (Expression) parameters.get(1);
        Expression par3 = (Expression) parameters.get(2);
        Expression par4 = (Expression) parameters.get(3);
        Type sumtype = par4.getType();
        String tname = sumtype.getName();
        BasicExpression subrexp = new BasicExpression("subrange");
        subrexp.umlkind = FUNCTION; 
        subrexp.objectRef = objectRef;
        subrexp.addParameter(par1);
        subrexp.addParameter(par2);
        Type seqtype = new Type("Sequence", null); 
        subrexp.setType(seqtype);
        Type inttype = new Type("int", null); 
        subrexp.setElementType(inttype);
        seqtype.setElementType(inttype); 
        if (("" + par3).equals("" + par4))
        { String sqf = subrexp.queryFormJava7(env,local); 
          return "Ocl." + data.toLowerCase() + tname + "(" + sqf + ")"; 
        } 
        Expression colexp = new BinaryExpression("|C",new BinaryExpression(":", par3, subrexp), par4);
        String colqf = colexp.queryFormJava7(env,local); 
        return "Ocl." + data.toLowerCase() + tname + "(" + colqf + ")";
      }

      if (data.equals("isDeleted"))  // assume objectRef is single object
      { return "true"; // "false" really
        /* String objstype = objectRef.type.getName();
        if (objectRef.isMultiple())
        { return "Ocl.intersection(" + pre + 
                                   ",Controller.inst()." + objstype.toLowerCase() + "s).size() == 0"; 
        } 
        else
        { return "!(Controller.inst()." + objstype.toLowerCase() + 
                 "s.contains(" + pre + "))"; 
        } */ 
      } 

      if (data.equals("asSequence")) 
      { return "Ocl.asSequence(" + pre + ")"; } 

      if (data.equals("oclIsUndefined"))
      { return "(" + pre + " == null)"; } 

      if (data.equals("oclIsKindOf") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        String arg1s = arg1 + ""; 
        return "(" + pre + " instanceof " + arg1s + ")";  // works for external classes also 
      }
	   
      if (data.equals("oclAsType") && parameters != null && parameters.size() > 0)  
      { Expression arg1 = (Expression) parameters.get(0); 
        return "((" + arg1 + ") " + pre + ")";  // casting 
      }
	   
      if (data.equals("sqr"))
      { return "(" + pre + ") * (" + pre + ")"; } 
      else if (data.equals("abs") || data.equals("sin") || data.equals("cos") ||
          data.equals("sqrt") || data.equals("exp") || data.equals("log") || data.equals("log10") ||
          data.equals("tan") || data.equals("asin") || data.equals("acos") || data.equals("cbrt") ||
          data.equals("atan") || data.equals("sinh") || data.equals("cosh") || data.equals("tanh"))  
          // exp, floor can be applied to sets/sequences
      { return "Math." + data + "(" + pre + ")"; }
      else if (data.equals("round") || data.equals("ceil") || data.equals("floor"))
      { return "((int) Math." + data + "(" + pre + "))"; } 
      else if (data.equals("toUpperCase") || data.equals("toLowerCase"))
      { return pre + "." + data + "()"; } 
      else if (data.equals("sum"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // must be int, double, long, String
        return "Ocl.sum" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("prd"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // must be int, double, long
        return "Ocl.prd" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("reverse") || data.equals("sort") ||
               data.equals("asSet") || data.equals("characters")) 
      { return "Ocl." + data + "(" + pre + ")"; }  // what about an index? 
      else if (data.equals("subrange") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        if ("Integer".equals(objectRef + ""))
        { return "Ocl.integerSubrange(" + par1 + "," + par2 + ")"; } 
        return "Ocl.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("subrange") && parameters != null && parameters.size() == 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local);
        UnaryExpression ue = new UnaryExpression("->size", objectRef); 
        ue.setType(new Type("int", null)); 
        String par2 = ue.queryFormJava7(env,local); 
        return "Ocl.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("indexOf") && parameters != null && parameters.size() > 0)  
      // for strings or sequences
      { Expression par1 = (Expression) parameters.get(0); 
        String spar1 = par1.queryFormJava7(env,local); 
        String wpar1 = par1.wrap(spar1); 
        return "(" + pre + ".indexOf(" + wpar1 + ") + 1)"; 
      }      
      else if (data.equals("pow") && parameters != null && parameters.size() > 0)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        return "Math.pow(" + pre + ", " + par1 + ")"; 
      }      
      else if (data.equals("closure"))
      { String rel = ((BasicExpression) objectRef).data;
        Expression arg = ((BasicExpression) objectRef).objectRef;
        if (arg != null) 
        { return "Ocl.closure" + entity.getName() + rel + "(" + arg.queryFormJava7(env,local) + ")"; }   
        return "Ocl.closure" + entity.getName() + rel + "(" + pre + ")";
      } 
      else if (data.equals("insertAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.insertAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("setAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.setAt(" + pre + "," + par1 + "," + par2 + ")"; 
        // ".add(" + par1 + ", " + par2 + ")"; 
      } 
      else if (data.equals("replace") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.replace(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAll") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAllMatches") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceFirstMatch") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormJava7(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormJava7(env,local); 
        return "Ocl.replaceFirstMatch(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("count"))  // for strings or collections
      { if (parameters != null && parameters.size() > 0)
        { String par1 = 
            ((Expression) parameters.get(0)).queryFormJava7(env,local);
          return "Ocl.count(" + pre + "," + par1 + ")";  
        } 
      }   
      else if (objectRef != null && (objectRef.getType() + "").equals("String"))
      { // last,first,front,tail on strings
        if (data.equals("first") || data.equals("any"))
        { return "(" + pre + ".charAt(0) + \"\")"; } 
        if (data.equals("last"))
        { return "(" + pre + ".charAt(" + pre + ".length()-1) + \"\")"; } 
        if (data.equals("front"))
        { return pre + ".substring(0," + pre + ".length()-1)"; } 
        if (data.equals("tail"))
        { return pre + ".substring(1," + pre + ".length())"; } 
      } 
      else if (data.equals("max") || data.equals("min"))
      { Type compertype = objectRef.getElementType();   
        String ctname = compertype.getName();   // only int, double, String are valid
        if ("int".equals(ctname))
        { return "((Integer) Ocl." + data + "(" + pre + ")).intValue()"; }
        else if ("long".equals(ctname))
        { return "((Long) Ocl." + data + "(" + pre + ")).longValue()"; }
        else if ("double".equals(ctname))
        { return "((Double) Ocl." + data + "(" + pre + ")).doubleValue()"; }
        else 
        { return "((" + ctname + ") Ocl." + data + "(" + pre + "))"; }
      } 
      else if (data.equals("any") || data.equals("last") || data.equals("first"))
      { Type et = objectRef.elementType; 
        if (et != null) 
        { if ("String".equals("" + et) || et.isEntity())
          { return "((" + et + ") Ocl." + data + "(" + pre + "))"; }
          else 
          { return unwrap("Ocl." + data + "(" + pre + ")"); } 
        } 
        else 
        { return "Ocl." + data + "(" + pre + ")"; }
      }  
      else  // sum, prd, front tail on sequences, subcollections 
          // -- separate versions for numbers & objects?
      { return "Ocl." + data + "(" + pre + ")"; }
    }  // and s.count(x) in Java

    if (umlkind == QUERY || umlkind == UPDATEOP) 
    { String res = data + "("; 
      String parString = parametersQueryFormJava7(env,local); 

      if (entity != null) 
      { ename = entity.getName(); } 

      if (entity != null && entity.isExternalApplication())
      { res = ename + "." + cont + "." + res + parString; 
        return res; 
      } 
      else if (entity != null && entity.isClassScope(data))
      { res = ename + "." + res + parString; 
        return res; 
      } 
      else if (objectRef != null)
      { String pre = objectRef.queryFormJava7(env,local);
        if (objectRef.umlkind == CLASSID)
        { pre = ((BasicExpression) objectRef).classExtentQueryFormJava7(env,local); }

        if (objectRef.isMultiple()) // iterate, assume parameters.size() > 0
        { if (parameters == null || parameters.size() == 0) 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + ")"; } 
          else 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + "," + parString; } 
        } 
        else
        { if (downcast)
          { pre = "((" + ename + ") " + pre + ")"; } 
          res = pre + "." + res + parString;
        }
        return res;  
      }
	  
      String var = findEntityVariable(env);
      if (var != null && var.length() > 0) 
	  { return var + "." + res + parString; } 
	  else 
	  { return "Controller.inst()." + res + parString; } 
    } // may also be an arrayIndex
      

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava7(env,local); 
            if (isQualified())
            { return "get" + data + "(" + ind + ")"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones

            if (arrayIndex.isString())
            { return unwrap(data + ".get(\"\" + " + ind + ")"); }
            else if (arrayType != null && arrayType.isMap())
            { return unwrap(data + ".get(" + ind + ")"); }
            else if (arrayType != null && arrayType.isSequence())
            { return unwrap(data + ".get(" + indopt + ")"); }

            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrap(data + ".get(" + indopt + ")"); } 
            String jType = type.getJava7(elementType); 
            return "((" + jType + ") " + data + ".get(" + indopt + "))"; 
          } 
          return data; 
        } 

        if (entity == null) { return data; } 

        String nme = entity.getName();

        if (entity.isClassScope(data))   // entity cannot be an interface
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava7(env,local); 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (arrayIndex.isString())
            { return unwrap(nme + "." + data + ".get(\"\" + " + ind + ")"); }

            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   nme + "." + data + ".charAt(" + indopt + "))";
            } 
            else if (Type.isPrimitiveType(type))
            { return unwrap(nme + "." + data + ".get(" + indopt + ")"); } 
            else 
            { String jtype = type.getJava7(elementType);
              return "((" + jtype + ") " + nme + "." + data + ".get(" + indopt + "))"; 
            }
          } 
          return nme + "." + data; 
        }  
        
        String var = findEntityVariable(env); // could be null, or ""

        String res = var + ".get" + data + "()";
        if (var == null || var.length() == 0) 
        { res = data; }
		
        if (arrayIndex != null) 
        { String etype = elementType + ""; 
          String ind = arrayIndex.queryFormJava7(env,local); 
          if (isQualified())
          { return var + ".get" + data + "(" + ind + ")"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (arrayType != null && arrayType.isMap())
          { return unwrap(var + ".get" + data + "().get(" + ind + ")"); }
          else if (arrayType != null && arrayType.isSequence())
          { return unwrap(var + ".get" + data + "().get(" + indopt + ")"); }

          if (type.getName().equals("String"))
          { return "(\"\" + " + 
                   res + ".charAt(" + indopt + "))";
          } 
          else if (Type.isPrimitiveType(type))
          { return unwrap(var + ".get" + data + "().get(" + indopt + ")"); } 
          String jType = type.getJava7(elementType); 
          return "((" + jType + ") " + var + ".get" + data + "().get(" + indopt + "))"; 
        } // not for strings; unwrap primitives 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String eref = ename; 
      if (entity.isInterface()) 
      { eref = ename + "." + ename + "Ops"; } 

      String res = "";
      String pre = objectRef.queryFormJava7(env,local);
      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { if (refindex.isMultiple())
          { if (isOrdered())
            { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }  // E[inds].data
            else 
            { res = eref + ".getAll" + data + "(" + pre + ")"; }  // E[inds].data
          } 
          else 
          { res = pre + ".get" + data + "()"; }  // E[ind].data
        } 
        else if (entity.isClassScope(data))
        { res = pre + ".get" + data + "()"; }   // E.data
        else 
        { pre = cont + "." + ename.toLowerCase() + "s"; 
          res = eref + ".getAll" + data + "(" + pre + ")";
        } 
      } 
      else if (objectRef.isMultiple())
      { if (isOrdered())
        { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }
        else
        { res = eref + ".getAll" + data + "(" + pre + ")"; }
      }
      else
      { if (downcast)
        { pre = "((" + ename + ") " + pre + ")"; } 
        res = pre + ".get" + data + "()";
      }

      if (arrayIndex != null) 
      { String ind = arrayIndex.queryFormJava7(env,local); 
        if (isQualified())
        { return "(" + res + ").get(" + ind + ")"; } 
        String indopt = evaluateString("-",ind,"1"); // not for qualified ones
        if (type.getName().equals("String"))
        { return "(\"\" + " + 
                 res + ".charAt(" + indopt + "))";
        } 
        else if (Type.isPrimitiveType(type))
        { return unwrap(res + ".get(" + indopt + ")"); } 
        String jType = type.getJava7(elementType); 
        return "((" + jType + ") " + res + ".get(" + indopt + "))"; 
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  }

  public String leftQueryFormJava7(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller.inst()"; 

    if (umlkind == VARIABLE)
    { if (arrayIndex != null)
      { String ind = arrayIndex.queryFormJava7(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (type != null)
        { if (arrayIndex.type != null && arrayIndex.type.getName().equals("int"))
          { return data + "[" + indopt + "]"; }
          return data + "[" + indopt + "]"; 
        }         
        return data + "[" + indopt + "]";
      } // unwrap it, also for primitive types
      else 
      { return data; }  
    } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava7(env,local); 
            if (isQualified())
            { return data + "[" + ind + "]"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones

            if (arrayIndex.isString())
            { return data + "[" + ind + "]"; }
            else if (arrayType != null && arrayType.isMap())
            { return data + "[" + ind + "]"; }
            else if (arrayType != null && arrayType.isSequence())
            { return data + "[" + indopt + "]"; }
          } 
          return data; 
        } 

        if (entity == null) 
        { return data; } 

        String nme = entity.getName();

        if (entity.isClassScope(data))   // entity cannot be an interface
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava7(env,local); 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (arrayIndex.isString())
            { return nme + "." + data + "[" + ind + "]"; }

            return nme + "." + data + "[" + indopt + "]"; 
          } 
          return nme + "." + data; 
        }  
        
        String var = findEntityVariable(env); // could be null, or ""

        String res = var + "." + data;
        if (var == null || var.length() == 0) 
        { res = data; }
		
        if (arrayIndex != null) 
        { String etype = elementType + ""; 
          String ind = arrayIndex.queryFormJava7(env,local); 
          if (isQualified())
          { return var + "." + data + "[" + ind + "]"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (arrayType != null && arrayType.isMap())
          { return var + "." + data + "[" + ind + "]"; }
          else if (arrayType != null && arrayType.isSequence())
          { return var + "." + data + "[" + indopt + "]"; }

          return var + "." + data + "[" + indopt + "]"; 
        } // not for strings; unwrap primitives 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String eref = ename; 

      String res = "";
      String pre = objectRef.queryFormJava7(env,local);
      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { res = pre + "." + data; }  // E[ind].data
        else if (entity.isClassScope(data))
        { res = pre + "." + data; }   // E.data 
      } 
      else
      { if (downcast)
        { pre = "((" + ename + ") " + pre + ")"; } 
        res = pre + "." + data + "";
      }

      if (arrayIndex != null) 
      { String ind = arrayIndex.queryFormJava7(env,local); 
        if (isQualified())
        { return "(" + res + ")[" + ind + "]"; } 
        String indopt = evaluateString("-",ind,"1"); // not for qualified ones
        return res + "[" + indopt + "]"; 
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  }


  // public String classqueryFormCSharp(java.util.Map env, boolean local)
  // { if (umlkind == CLASSID)
  //   { return classExtentQueryForm(env,local); } 
  //   return queryFormCSharp(env,local); 
  // } 

  /* public String parametersQueryFormCSharp(java.util.Map env, boolean local)
  { String parString = ""; 
    if (parameters != null)
    { for (int i = 0; i < parameters.size(); i++)
      { Expression par = (Expression) parameters.get(i); 
        parString = parString + par.queryFormCSharp(env,local); 
        if (i < parameters.size() - 1)
        { parString = parString + ","; } 
      }
    } 
    parString = parString + ")"; 
    return parString; 
  } */ 

  public String queryFormCSharp(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller.inst()"; 

    String pars = ""; 
    if (parameters != null)
    { for (int h = 0; h < parameters.size(); h++) 
      { Expression par = (Expression) parameters.get(h); 
        pars = pars + par.queryFormCSharp(env,local); 
        if (h < parameters.size()-1) 
        { pars = pars + ","; } 
      } 
    }
   
    if ("getMessage".equals(data) && objectRef != null &&
        Type.isOclExceptionType(objectRef))
    { String rqf = objectRef.queryFormCSharp(env,local); 
      return rqf + ".Message"; 
    }  

    if ("getCause".equals(data) && objectRef != null &&
        Type.isOclExceptionType(objectRef))
    { String rqf = objectRef.queryFormCSharp(env,local); 
      return rqf + ".InnerException"; 
    }  

    if ("getName".equals(data) && objectRef != null &&
        objectRef.getType() != null &&
        "OclType".equals(objectRef.getType().getName()))
    { String rqf = objectRef.queryFormCSharp(env,local); 
      return rqf + ".Name"; 
    }  

    // if ("printStackTrace".equals(data) && objectRef != null &&
    //     Type.isOclExceptionType(objectRef))
    // { String rqf = objectRef.queryFormCSharp(env,local); 
    //   return "Console.WriteLine(" + rqf + ".StackTrace)"; 
    // }  

    if (data.equals("System_in") || 
        data.equals("System_out") ||
        data.equals("System_err"))
    { return "OclFile." + data; } 

    if ((data.equals("systemTime") || 
         "getSystemTime".equals(data)) && 
        "OclDate".equals(objectRef + ""))
    { return "SystemTypes.getTime()"; } 

    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      if ("OclDate".equals(createdClass))
      { return "DateTime.Now"; }
      if (Type.isExceptionType(createdClass))
      { String csharpClass = 
          (String) Type.exceptions2csharp.get(createdClass); 
        return "new " + csharpClass + "(" + pars + ")"; 
      } 
    } 

    if (data.startsWith("new") && objectRef != null) 
    { String entname = data.substring(3);
      System.out.println(">>> Creation operation for " + entname); 
         
      if ((objectRef + "").equals(entname)) 
      { return objectRef + "." + data + "(" + pars + ")"; }
    } 

    if (("time".equals(data) || "getTime".equals(data)) && 
        objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { String rqf = objectRef.queryFormCSharp(env,local); 
        return "SystemTypes.getTime(" + rqf + ")"; 
      }  
    } 

    /* Update form: 
    if ("setTime".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          String par1qf = par1.queryFormCSharp(env,local); 
          String rqf = objectRef.queryFormCSharp(env,local); 
       
          return "  " + rqf + 
            " = DateTimeOffset.FromUnixTimeMilliseconds(" + par1qf + ").DateTime;";
        } 
      }  
    } */ 

    if ("dateAfter".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          String par1qf = par1.queryFormCSharp(env,local); 
          String rqf = objectRef.queryFormCSharp(env,local); 
        
          return rqf + " > " + par1qf;
        } 
      }  
    } 

    if ("dateBefore".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          String par1qf = par1.queryFormCSharp(env,local); 
          String rqf = objectRef.queryFormCSharp(env,local); 
        
          return rqf + " < " + par1qf;
        }
      }  
    } 

/* 
    if (data.startsWith("create") && objectRef == null) 
    { String entname = data.substring(6);
      System.out.println(">>> Creation operation for " + entname); 
         
      // Entity entx = (Entity) ModelElement.lookupByName(entname,entities); 
      if (entity != null && entity.getName().equals(entname)) 
      { return cont + "." + data + "(" + pars + ")"; }
    } 
*/ 

    if (type != null && type.isEnumeration() && 
        type.hasValue(data))
    { return type.getName() + "." + data; } 

    if (umlkind == VALUE || umlkind == CONSTANT)
    { if (data.equals("{}") || data.equals("Set{}") || data.equals("Sequence{}"))
      { return "new ArrayList()"; }    // new Set really
      if (data.equals("Map{}"))
      { return "new Hashtable()"; }
	 
      if (data.equals("null")) 
      { return "null"; } 

      if (data.equals("Math_NaN")) 
      { return "double.NaN"; } 
      if (data.equals("Math_PINFINITY")) 
      { return "double.PositiveInfinity"; } 
      if (data.equals("Math_NINFINITY")) 
      { return "double.NegativeInfinity"; } 

      if (isSet(data))
      { Expression se = buildSetExpression(data); 
        return se.queryFormCSharp(env,local);
      }

      if (isSequence(data))
      { Expression se = buildSetExpression(data); 
        if (arrayIndex != null)
        { String ind = arrayIndex.queryFormCSharp(env,local); 
          String indopt = evaluateString("-",ind,"1"); 
          String cstype = type.getCSharp(); 
          return "((" + cstype + ") " + se.queryFormCSharp(env,local) + "[" + indopt + "])";
        }
        return se.queryFormCSharp(env,local); 
      } 

      if (isString(data) && arrayIndex != null)
      { String ind = arrayIndex.queryFormCSharp(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        return "(\"" + "\" + " + data + "[" + indopt + "])"; 
      } 

      if (type != null && type.isEnumeration())
      { return type.getName() + "." + data; } 

      return data;
    } // literal maps? 

    if (umlkind == VARIABLE)
    { if (data.equals("self"))  // but return "self" if it is a genuine variable
      { if (env.containsValue("self"))
        { return data; } 
        // else if (entity != null)  // Assume it is the most local instance in scope
        // { String enme = entity.getName(); 
        //   String var = (String) env.get(enme);
        //   if (var == null) { return "this"; } 
        //   return var; 
        // } 
        else 
        { return "this"; }
      } 
      else if (data.equals("super"))
      { return "base"; } 
      else // entity == null) 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryFormCSharp(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (variable != null && "String".equals(variable.getType() + ""))
        { return data + ".Substring(" + indopt + ", 1)"; } 
        if (variable != null && variable.getType() != null && "Map".equals(variable.getType().getName()))
        { return data + "[" + ind + "]"; } 
        String cstype = type.getCSharp(); 
        return "((" + cstype + ") " + data + "[" + indopt + "])";
      }
      else if (parameters != null && variable != null && variable.getType().isFunctionType()) // application of a Function(S,T)
      { return data + "(" + pars + ")"; }

      return data;  
    } 

    // if (data.equals("OclFile"))
    // { return "OclFile.getOclFileByPK(" + ind + ")"; } 
      
    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { String ind = arrayIndex.queryFormCSharp(env,local);
        if (data.equals("OclFile"))
        { return "OclFile.getOclFileByPK(" + ind + ")"; } 
        if (data.equals("OclType"))
        { return "OclType.getOclTypeByPK(" + ind + ")"; } 
        return cont + ".get" + data + "ByPK(" + ind + ")"; 
      } 
      return data;  
      // But library classes may map to language-specific name
    }

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return data; } // Invalid function

      String pre = objectRef.queryFormCSharp(env,local);

      if (data.equals("allInstances"))  // objectRef is a CLASSID
      { return cont + ".get" + pre.toLowerCase() + "_s()"; } 

      if (data.equals("oclIsUndefined"))
      { return "(" + pre + " == null)"; } 

      if (objectRef.umlkind == CLASSID)
      { pre = ((BasicExpression) objectRef).classExtentQueryFormCSharp(env,local); }
 
      if (data.equals("size"))
      { if (objectRef.type != null && objectRef.type.getName().equals("String"))
        { return pre + ".Length"; } 
        else 
        { return pre + ".Count"; }
      }
      if (data.equals("isDeleted"))  // assume objectRef is single object
      { return "true"; // "false" really
      } 
              
      if (("Sum".equals(data) || "Prd".equals(data))
          && "Integer".equals(objectRef + "") && parameters != null && parameters.size() > 3)
      { Expression par1 = (Expression) parameters.get(0);
        Expression par2 = (Expression) parameters.get(1);
        Expression par3 = (Expression) parameters.get(2);
        Expression par4 = (Expression) parameters.get(3);
        BasicExpression subrexp = new BasicExpression("subrange");
        subrexp.umlkind = FUNCTION; 
        subrexp.objectRef = objectRef;
        subrexp.addParameter(par1);
        subrexp.addParameter(par2);
        Type seqtype = new Type("Sequence",null); 
        subrexp.setType(seqtype);
        Type inttype = new Type("int",null); 
        subrexp.setElementType(inttype);
        seqtype.setElementType(inttype); 
        Type sumtype = par4.getType();
        String tname = sumtype.getName();
        if (("" + par3).equals("" + par4))
        { String sqf = subrexp.queryFormCSharp(env,local); 
          return "SystemTypes." + data.toLowerCase() + tname + "(" + sqf + ")"; 
        } 
        Expression colexp = new BinaryExpression("|C",new BinaryExpression(":", par3, subrexp), par4);
        String colqf = colexp.queryFormCSharp(env,local); 
        return "SystemTypes." + data.toLowerCase() + tname + "(" + colqf + ")";
      }

      if (data.equals("oclIsKindOf") & parameters != null)  
      { Expression arg1 = (Expression) parameters.get(0); 
        String arg1s = arg1 + ""; 
        return "(" + pre + " is " + arg1s + ")"; 
        // Controller.inst().get" + arg1s.toLowerCase() + "_s().contains(" + pre + "))"; 
      } 
      if (data.equals("oclAsType") && parameters != null)  
      { Expression arg1 = (Expression) parameters.get(0); 
        return "((" + arg1 + ") " + pre + ")";  // casting 
      } 
      if (data.equals("sqr"))
      { return "(" + pre + ") * (" + pre + ")"; } 
      else if (data.equals("abs") || data.equals("sin") || data.equals("cos") ||
          data.equals("sqrt") || data.equals("exp") || data.equals("log") || data.equals("log10") ||
          data.equals("tan") || data.equals("asin") || data.equals("acos") || data.equals("cbrt") ||
          data.equals("atan") || data.equals("sinh") || data.equals("cosh") || data.equals("tanh"))  
          // exp, floor can be applied to sets/sequences
      { String csop = ("" + data.charAt(0)).toUpperCase() + data.substring(1,data.length());  
        return "Math." + csop + "(" + pre + ")"; 
      }   // to uppercase?
      else if (data.equals("round"))
      { return "((int) Math.Round(" + pre + "))"; } 
      else if (data.equals("ceil"))
      { return "((int) Math.Ceiling(" + pre + "))"; } 
      else if (data.equals("floor"))
      { return "((int) Math.Floor(" + pre + "))"; } 
      else if (data.equals("toUpperCase"))
      { return pre + ".ToUpper()"; } 
      else if (data.equals("toLowerCase"))
      { return pre + ".ToLower()"; } 
      else if (data.equals("sum"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // int, double, long, String
        return "SystemTypes.sum" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("prd"))
      { Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getName(); // int, double, long
        return "SystemTypes.prd" + tname + "(" + pre + ")"; 
      } 
      else if (data.equals("reverse") || data.equals("sort") ||
               data.equals("asSet") || data.equals("characters")) 
      { return "SystemTypes." + data + "(" + pre + ")"; } 
      else if (data.equals("subrange") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        if ("Integer".equals(objectRef + ""))
        { return "SystemTypes.integerSubrange(" + par1 + "," + par2 + ")"; } 
        return "SystemTypes.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("subrange") && parameters != null && parameters.size() == 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local);
        UnaryExpression ue = new UnaryExpression("->size", objectRef); 
        ue.setType(new Type("int", null)); 
        String par2 = ue.queryFormCSharp(env,local); 
        return "SystemTypes.subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("indexOf") && parameters != null && parameters.size() > 0)  // for strings or sequences
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        return "(" + pre + ".IndexOf(" + par1 + ") + 1)"; 
      }      
      else if (data.equals("pow") && parameters != null && parameters.size() > 0)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        return "Math.Pow(" + pre + ", " + par1 + ")"; 
      }      
      else if (data.equals("asSequence"))
      { return pre; } 
      else if (data.equals("closure"))
      { String rel = ((BasicExpression) objectRef).data; 
        Expression arg = ((BasicExpression) objectRef).objectRef;
        if (arg != null) 
        { return "SystemTypes.closure" + entity.getName() + rel + "(" + arg.queryFormCSharp(env,local) + ")"; }   
        return "SystemTypes.closure" + entity.getName() + rel + "(" + pre + ")";
      } 
      else if (data.equals("insertAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.insertAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("setAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.setAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replace") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.replace(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAll") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAllMatches") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceFirstMatch") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCSharp(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCSharp(env,local); 
        return "SystemTypes.replaceFirstMatch(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("count"))  // for strings or collections
      { if (parameters != null && parameters.size() > 0)
        { String par1 = 
            ((Expression) parameters.get(0)).queryFormCSharp(env,local);
          return "SystemTypes.count(" + pre + "," + par1 + ")";  
        } 
      }   
      else if (objectRef != null && (objectRef.getType() + "").equals("String"))
      { // last,first,front,tail on strings
        if (data.equals("first") || data.equals("any"))
        { return pre + ".Substring(0,1)"; } 
        if (data.equals("last"))
        { return pre + ".Substring(" + pre + ".Length-1, 1)"; } 
        if (data.equals("front"))
        { return pre + ".Substring(0," + pre + ".Length-1)"; } 
        if (data.equals("tail"))
        { return pre + ".Substring(1," + pre + ".Length-1)"; } 
      } 
      else if (data.equals("max") || data.equals("min"))
      { Type et = objectRef.elementType;  
        if (et != null) 
        { return "((" + et.getCSharp() + ") SystemTypes." + data + "(" + pre + "))"; }
        else 
        { return "SystemTypes." + data + "(" + pre + ")"; }
      }  
      else if (data.equals("any") || data.equals("last") || data.equals("first"))
      { Type et = objectRef.elementType; 
        if (et != null) 
        { return "((" + et.getCSharp() + ") SystemTypes." + data + "(" + pre + "))"; } 
        else 
        { return "SystemTypes." + data + "(" + pre + ")"; }
      }  
      else  // sum, prd, front tail on sequences, subcollections 
          // -- separate versions for numbers & objects?
      { return "SystemTypes." + data + "(" + pre + ")"; }
    }  // and s.count(x) in Java

    if (umlkind == QUERY || umlkind == UPDATEOP) 
    { String res = data + "("; 
      String parString = parametersQueryFormCSharp(env,local); 

      System.out.println(">>> C# query form of: " + this + " " +
                         isStatic() + " " + entity); 

      if (entity == null && objectRef == null) // use case
      { return cont + "." + res + parString; } 

      BehaviouralFeature bf = null; 

      if (entity != null) 
      { ename = entity.getName();
        bf = entity.getDefinedOperation(data); 
      } 

      if (bf == null && (objectRef instanceof BasicExpression) 
          && isStatic() &&
          objectRef.umlkind == CLASSID && 
          ((BasicExpression) objectRef).arrayIndex == null)
      { Type et = objectRef.getElementType(); 
        if (et != null && et.isEntity())
        { bf = et.getEntity().getDefinedOperation(data); } 
        System.out.println(">>> Static call: " + objectRef + " " + et + " " + bf);

        if (bf != null) 
        { res = et.getName() + "." + res + parString; 
          return res;
        }  
      } 

      if (bf != null && bf.isGeneric())
      { String tpars = 
          Type.resolveTypeParametersCSharp( 
             bf.getTypeParameters(), 
             bf.getParameters(), parameters); 
        res = data + tpars + "("; 
      } 

      if (entity != null && entity.isExternalApplication())
      { res = ename + "." + cont + "." + res + parString; 
        return res; 
      } 
      else if (entity != null && entity.isClassScope(data))
      { res = ename + "." + res + parString; 
        return res; 
      } 
      else if (entity != null && data.startsWith("create") &&
               objectRef == null) 
      { if (data.substring(6).equals(ename))
        { return cont + "." + res + parString; } 
      } 
      else if (objectRef != null)
      { String pre = objectRef.queryFormCSharp(env,local);
        if (objectRef.umlkind == CLASSID)
        { pre = ((BasicExpression) objectRef).classExtentQueryFormCSharp(env,local); }

        if (objectRef.isSequenceValued() || 
            objectRef.isSetValued()) 
          // iterate, assume parameters.size() > 0
        { if (parameters == null || parameters.size() == 0) 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + ")"; } 
          else 
          { res = "  " + cont + ".All" + ename + data + "(" + pre + "," + parString; } 
        } 
        else
        { if (downcast)
          { pre = "((" + ename + ") " + pre + ")"; } 
          res = pre + "." + res + parString;
        }
        return res;  
      }

      String var = findEntityVariable(env);
      if (var != null && var.length() > 0) 
      { return var + "." + res + parString; } 
      else 
      { return res + parString; }   
    } // may also be an arrayIndex
      

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { String etype = type.getCSharp(); 
            String ind = arrayIndex.queryFormCSharp(env,local); 
            if (isQualified())
            { return "((" + etype + ") " + data + "[" + ind + "])"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (arrayIndex.isString())
            { return data + "[\"\" + " + ind + "]"; }
            // if (type.getName().equals("String"))
            // { return data + ".Substring(" + indopt + ",1)"; } 
            else if (arrayType != null && arrayType.isMap())
            { return "((" + etype + ") " + data + "[" + ind + "])"; }
            else if (arrayType != null && arrayType.isSequence())
            { return "((" + etype + ") " + data + "[" + indopt + "])"; }
            else if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   data + ".charAt(" + indopt + "))";
            } 
            else 
            { return "((" + type.getCSharp() + ") " + data + "[" + indopt + "])"; }
          }
          return data; 
        } 

        if (entity == null) { return data; } 

        String nme = entity.getName();

        if (entity.isClassScope(data))   // entity cannot be an interface
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormCSharp(env,local); 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return "(\"\" + " + 
                   nme + "." + data + ".Substring(" + indopt + ",1))";
            } 
            else 
            { String jtype = type.getCSharp();
              return "((" + jtype + ") " + nme + "." + data + "[" + indopt + "])"; 
            }
          } 
          return nme + "." + data; 
        }  
        
        String var = findEntityVariable(env);

        String res = var + ".get" + data + "()";
        if (var == null || var.length() == 0) 
        { res = data; }
		
        if (arrayIndex != null) 
        { String etype = type.getCSharp(); 
          String ind = arrayIndex.queryFormCSharp(env,local); 
          if (isQualified())
          { return "((" + etype + ") " + res + "[" + ind + "])"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (type.getName().equals("String"))
          { return data + ".Substring(" + indopt + ", 1)"; } 
          return "((" + type.getCSharp() + ") " + res + "[" + indopt + "])";
        } // not for strings 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String eref = ename; 
      if (entity.isInterface()) { eref = ename + "Ops"; }
 
      String res = "";
      String pre = objectRef.queryFormCSharp(env,local);
      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { if (refindex.isMultiple())
          { if (refindex.isOrdered())
            { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }  // E[inds].data
            else 
            { res = eref + ".getAll" + data + "(" + pre + ")"; }
          }  
          else 
          { res = pre + ".get" + data + "()"; }  // E[ind].data
        } 
        else if (entity.isClassScope(data))
        { res = pre + ".get" + data + "()"; }   // E.data
        else 
        { pre = cont + "." + ename.toLowerCase() + "_s"; 
          res = eref + ".getAll" + data + "(" + pre + ")";
        } 
      } 
      else if (objectRef.isMultiple())
      { if (isOrdered())
        { res = eref + ".getAllOrdered" + data + "(" + pre + ")"; }
        else
        { res = eref + ".getAll" + data + "(" + pre + ")"; }
      }
      else
      { if (downcast)
        { pre = "((" + ename + ") " + pre + ")"; } 
        res = pre + ".get" + data + "()";
      }
      if (arrayIndex != null) 
      { String ind = arrayIndex.queryFormCSharp(env,local); 
        if (isQualified())
        { return res + "[" + ind + "]"; } 
        String indopt = evaluateString("-",ind,"1"); // not for qualified, Strings
        if (type.getName().equals("String"))
        { return res + ".Substring(" + indopt + ", 1)"; } 
        String cstype = type.getCSharp(); 
        return "((" + cstype + ") " + res + "[" + indopt + "])"; 
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  }

  public String queryFormCPP(java.util.Map env, boolean local)
  { // already type-checked. Ignores prestate
    String ename = ""; 
    String cont = "Controller::inst->"; 
    Type elemT = getElementType(); 
    String cetype = "void*";   
    if (elemT != null) 
    { cetype = elemT.getCPP(elemT.getElementType()); }  

    if ("null".equals(data)) 
    { return "NULL"; } 

    if ("getMessage".equals(data) && objectRef != null &&
        Type.isOclExceptionType(objectRef))
    { String rqf = objectRef.queryFormCPP(env,local); 
      return rqf + ".what()"; 
    }  

    if ((data.equals("systemTime") || 
         data.equals("getSystemTime")) && 
        "OclDate".equals(objectRef + ""))
    { return "UmlRsdsLib<long>::getTime()"; } 

    if (("time".equals(data) || "getTime".equals(data)) && 
        objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { String rqf = objectRef.queryFormCPP(env,local); 
        return rqf + "->getTime()"; 
      }  
    } 

    if (data.startsWith("new"))
    { String createdClass = data.substring(3); 
      String pars = ""; 
      if (parameters != null) 
      { for (int h = 0; h < parameters.size(); h++) 
        { Expression par = (Expression) parameters.get(h); 
          pars = pars + par.queryFormCPP(env,local); 
          if (h < parameters.size()-1) 
          { pars = pars + ","; } 
        }
      } 

      if ("OclDate".equals(createdClass))
      { return "OclDate::newOclDate(" + pars + ")"; }

      if (createdClass.startsWith("OclIterator") && 
          parameters != null && 
          parameters.size() > 0)
      { Expression par1 = (Expression) parameters.get(0); 
        Type et = par1.getElementType(); 
        if (et != null) 
        { String cpptype = et.getCPP(et.getElementType()); 
          return "OclIterator<" + cpptype + ">::" + data + "(" + pars + ")"; 
        } 
        else 
        { System.err.println("!! ERROR: No element type for: " + par1); } 
      }  

      if (Type.isExceptionType(createdClass))
      { String cppClass = 
          (String) Type.exceptions2cpp.get(createdClass);
        if (cppClass == null) 
        { cppClass = createdClass; }
 
        return cppClass + "::" + data + "(" + pars + ")"; 
      }            
    } 

    if ("elements".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclIterator")) 
      { return objectRef.queryFormCPP(env,local) + "->getelements()"; }
    } 

    if ("delete".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile"))
      { return objectRef.queryFormCPP(env,local) + 
               "->deleteFile()"; 
      } 
    } 

    if ("printf".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile") && 
          parameters != null && 
          parameters.size() > 1 && 
          parameters.get(1) instanceof SetExpression)
      { Expression format = (Expression) parameters.get(0); 
        Expression sq = (Expression) parameters.get(1);
        String p1 = format.queryFormCPP(env,local);  
        String cseq = 
          ((SetExpression) sq).toCSequence(env,local); 
        return objectRef.queryFormCPP(env,local) + 
               "->printf(" + p1 + "," + cseq + ")"; 
      } 
    } 

    if (umlkind == VALUE || umlkind == CONSTANT)
    { if (data.equals("{}") || data.equals("Set{}"))
      { return "(new set<" + cetype + ">())"; }  
      if (data.equals("Sequence{}"))
      { return "(new vector<" + cetype + ">())"; }  
      if (data.equals("Map{}"))
      { return "(new map<" + cetype + ">())"; } 

      if (data.equals("null")) 
      { return "NULL"; } 

      if (data.equals("Math_NaN")) 
      { return "std::numeric_limits<double>::quiet_NaN()"; } 
      if (data.equals("Math_PINFINITY")) 
      { return "std::numeric_limits<double>::infinity()"; } 
      if (data.equals("Math_NINFINITY")) 
      { return "-(std::numeric_limits<double>::infinity())"; } 

      if (isSet(data))
      { Expression se = buildSetExpression(data); 
        return se.queryFormCPP(env,local);
      }

      if (isSequence(data))
      { Expression se = buildSetExpression(data); 
        if (arrayIndex != null)
        { String ind = arrayIndex.queryFormCPP(env,local); 
          String indopt = evaluateString("-",ind,"1"); 
          return "(" + se.queryFormCPP(env,local) + ")->at(" + indopt + ")";
        }
        return se.queryFormCPP(env,local); 
      } 

      if (isString(data) && arrayIndex != null)
      { String ind = arrayIndex.queryFormCPP(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        return data + ".substr(" + indopt + ", 1)"; 
      } 
      if (type != null && type.isEnumeration())
      { return data; } // "(int) " + type.getName() + "." + data; } 
      return data;
    }

    if (umlkind == VARIABLE)
    { if (data.equals("self"))  // but return "self" if it is a genuine variable
      { if (env.containsValue("self"))
        { return data; } 
        else 
        { return "this"; }
      } 
      else if (data.equals("super"))  
      { if (env.containsValue("super"))
        { return data; } 
        else if (entity != null && entity.getSuperclass() != null)
        { return entity.getSuperclass().getName(); }
        else 
        { System.err.println(">> Warning!: cannot find superclass for " + this); 
          return data; 
        } 
      } 
      else // entity == null) 
      if (arrayIndex != null)
      { String ind = arrayIndex.queryFormCPP(env,local); 
        String indopt = evaluateString("-",ind,"1"); 
        if (variable != null && "String".equals(variable.getType() + ""))
        { return data + ".substr(" + indopt + ", 1)"; } 
        if (variable != null && variable.getType() != null && "Map".equals(variable.getType().getName()))
        { return data + "->at(" + ind + ")"; } 
        return "(" + data + ")->at(" + indopt + ")";
      }
      else if (parameters != null && variable != null && variable.getType().isFunctionType()) // application of a Function(S,T)
      { String pars = ""; 
        for (int h = 0; h < parameters.size(); h++) 
        { Expression par = (Expression) parameters.get(h); 
          pars = pars + par.queryFormCPP(env,local); 
          if (h < parameters.size()-1) 
          { pars = pars + ","; } 
        } 
        return "(*" + data + ")(" + pars + ")"; 
      }
      return data;  
    } 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { String ind = arrayIndex.queryFormCPP(env,local);
        if (data.equals("OclFile"))
        { return "OclFile::getOclFileByPK(" + ind + ")"; } 
        if (data.equals("OclType"))
        { return "OclType::getOclTypeByPK(" + ind + ")"; } 
        return cont + "get" + data + "ByPK(" + ind + ")"; 
      } 
      return data;  // But library classes may map to language-specific name
    }

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return data; } // Invalid function

      String pre = objectRef.queryFormCPP(env,local);

      if (data.equals("allInstances"))  // objectRef is a CLASSID
      { return cont + "get" + pre.toLowerCase() + "_s()"; } 

      if (data.equals("oclIsUndefined"))
      { return "(" + pre + " == NULL)"; } 

      if (objectRef.umlkind == CLASSID)
      { pre = ((BasicExpression) objectRef).classExtentQueryFormCPP(env,local); }
 
      if (data.equals("size"))
      { if (objectRef.type != null && objectRef.type.getName().equals("String"))
        { return pre + ".length()"; } 
        else // must be a collection
        { return pre + "->size()"; }
      }
      if (data.equals("isDeleted"))  // assume objectRef is single object
      { return "true"; // "false" really
      } 
      if (data.equals("oclIsKindOf") && parameters != null)  // Does not work for external classes 
      { Expression arg1 = (Expression) parameters.get(0); 
        String arg1s = arg1 + ""; 
        String rinstances = cont + "get" + arg1s.toLowerCase() + "_s()"; 
        return "UmlRsdsLib<" + arg1s + "*>::isIn(" + pre + ", " + rinstances + ")";
      } 

      if (data.equals("oclAsType") && parameters != null)  
      { Expression arg1 = (Expression) parameters.get(0); 
        return "((" + arg1 + "*) " + pre + ")";  // casting - assuming it is an entity 
      } 

      if (data.equals("sqr"))
      { return "(" + pre + ") * (" + pre + ")"; } 
      else if (data.equals("abs") || data.equals("sin") || data.equals("cos") ||
          data.equals("sqrt") || data.equals("exp") || data.equals("log") ||
          data.equals("tan") || data.equals("atan") || data.equals("acos") ||
          data.equals("asin") || data.equals("cbrt") || data.equals("log10") ||
          data.equals("cosh") || data.equals("sinh") || data.equals("tanh"))  
          // exp, floor can be applied to sets/sequences
      { return data + "(" + pre + ")"; } 
      else if (data.equals("floor") || data.equals("ceil"))
      { return "((int) " + data + "(" + pre + "))"; } 
      else if (data.equals("round"))
      { return "UmlRsdsLib<int>::oclRound(" + pre + ")"; } 
      else if (data.equals("toUpperCase"))
      { return "UmlRsdsLib<string>::toUpperCase(" + pre + ")"; } 
      else if (data.equals("toLowerCase"))
      { return "UmlRsdsLib<string>::toLowerCase(" + pre + ")"; } 
      else if (("Sum".equals(data) || "Prd".equals(data))
          && "Integer".equals(objectRef + "") && parameters != null && parameters.size() > 3)
      { Expression par1 = (Expression) parameters.get(0);
        Expression par2 = (Expression) parameters.get(1);
        Expression par3 = (Expression) parameters.get(2);
        Expression par4 = (Expression) parameters.get(3);
        Type sumtype = par4.getType();
        String tname = sumtype.getName();
        BasicExpression subrexp = new BasicExpression("subrange");
        subrexp.umlkind = FUNCTION; 
        subrexp.objectRef = objectRef;
        subrexp.addParameter(par1);
        subrexp.addParameter(par2);
        Type seqtype = new Type("Sequence", null); 
        subrexp.setType(seqtype);
        Type inttype = new Type("int", null); 
        subrexp.setElementType(inttype);
        seqtype.setElementType(inttype); 
        if (("" + par3).equals("" + par4))
        { String sqf = subrexp.queryFormCPP(env,local); 
          return "UmlRsdsLib<" + tname + ">::" + data.toLowerCase() + "(" + sqf + ")"; 
        } 
        Expression colexp = new BinaryExpression("|C",new BinaryExpression(":", par3, subrexp), par4);
        String colqf = colexp.queryFormCPP(env,local); 
        if (tname.equals("string") && "Sum".equals(data))
        { return "UmlRsdsLib<" + tname + ">::sumString(" + pre + ")"; }  
        return "UmlRsdsLib<" + tname + ">::" + data.toLowerCase() + "(" + colqf + ")";
      }
      else if (data.equals("sum"))
      { // String collname = "Set"; 
        // if (isOrdered()) { collname = "Sequence"; } 
        Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getCPP();  // int, double only
        if (tname.equals("string"))
        { return "UmlRsdsLib<" + tname + ">::sumString(" + pre + ")"; }  
        return "UmlRsdsLib<" + tname + ">::sum(" + pre + ")"; 
      } 
      else if (data.equals("prd"))
      { // String collname = "Set"; 
        // if (isOrdered()) { collname = "Sequence"; } 
        Type sumtype = objectRef.getElementType(); 
        String tname = sumtype.getCPP();  // int, double only
        return "UmlRsdsLib<" + tname + ">::prd(" + pre + ")"; 
      }
      else if (data.equals("reverse") || data.equals("sort") ||
               data.equals("asSet") || data.equals("characters")) 
      { return "UmlRsdsLib<" + cetype + ">::" + data + "(" + pre + ")"; } 
      else if (data.equals("subrange") && parameters != null && parameters.size() >= 2)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        if ("Integer".equals(objectRef + ""))
        { return "UmlRsdsLib<int>::integerSubrange(" + par1 + "," + par2 + ")"; } 
        return "UmlRsdsLib<" + cetype + ">::subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("subrange") && parameters != null && parameters.size() == 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local);
        UnaryExpression ue = new UnaryExpression("->size", objectRef); 
        ue.setType(new Type("int", null)); 
        String par2 = ue.queryFormCPP(env,local); 
        return "UmlRsdsLib<" + cetype + ">::subrange(" + pre + "," + par1 + "," + par2 + ")";
      } 
      else if (data.equals("indexOf") && parameters != null && parameters.size() > 0)  // for strings or sequences
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        Type oreftype = objectRef.getType(); 
        if ("String".equals(oreftype.getName()))
        { return "UmlRsdsLib<string>::indexOf(" + par1 + ", " + pre + ")"; } 
        else // a sequence
        { return "UmlRsdsLib<" + cetype + ">::indexOf(" + par1 + ", " + pre + ")"; }  
      }      
      else if (data.equals("pow") && parameters != null && parameters.size() > 0)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        return "pow(" + pre + ", " + par1 + ")"; 
      }      
      else if (data.equals("closure"))
      { String rel = ((BasicExpression) objectRef).data; 
        Expression arg = ((BasicExpression) objectRef).objectRef;
        if (objectRef.entity != null) 
        { ename = objectRef.entity.getName(); } 
        if (arg != null) 
        { return "UmlRsdsLib<" + cetype + ">::closure" + ename + 
                     rel + "(" + arg.queryFormCPP(env,local) + ")"; 
        }   
        return "UmlRsdsLib<" + cetype + ">::closure" + ename + rel + "(" + pre + ")";  
      } 
      else if (data.equals("insertAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<" + cetype + ">::insertAt(" + pre + "," + par1 + "," + par2 + ")"; 
        // ".add(" + par1 + ", " + par2 + ")"; 
      }
      else if (data.equals("setAt") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<" + cetype + ">::setAt(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAll") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<string>::replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceAllMatches") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<string>::replaceAll(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replaceFirstMatch") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<string>::replaceFirstMatch(" + pre + "," + par1 + "," + par2 + ")"; 
      } 
      else if (data.equals("replace") && parameters != null && parameters.size() > 1)
      { String par1 = ((Expression) parameters.get(0)).queryFormCPP(env,local); 
        String par2 = ((Expression) parameters.get(1)).queryFormCPP(env,local); 
        return "UmlRsdsLib<string>::replace(" + pre + "," + par1 + "," + par2 + ")"; 
      }  
      else if (data.equals("count"))  // for strings or collections
      { if (parameters == null || parameters.size() > 0)
        { String par1 = 
            ((Expression) parameters.get(0)).queryFormCPP(env,local);
          return "UmlRsdsLib<" + cetype + ">::count(" + pre + "," + par1 + ")";  
        } 
      }   
      else if (objectRef != null && (objectRef.getType() + "").equals("String"))
      { // last,first,front,tail on strings
        if (data.equals("first") || data.equals("any"))
        { return pre + ".substr(0,1)"; } 
        if (data.equals("last"))
        { return pre + ".substr(" + pre + ".length()-1, 1)"; } 
        if (data.equals("front"))
        { return pre + ".substr(0," + pre + ".length()-1)"; } 
        if (data.equals("tail"))
        { return pre + ".substr(1," + pre + ".length()-1)"; } 
      } 
      else if (data.equals("max") || data.equals("min"))
      { return "UmlRsdsLib<" + cetype + ">::" + data + "(" + pre + ")"; } 
      else if (data.equals("any") || data.equals("last") || data.equals("first"))
      { return "UmlRsdsLib<" + cetype + ">::" + data + "(" + pre + ")"; }
      else  // sum, prd, front tail on sequences, subcollections 
          // -- separate versions for numbers & objects?
      { return "UmlRsdsLib<" + cetype + ">::" + data + "(" + pre + ")"; }
    }  // and s.count(x) in Java

    if (umlkind == QUERY || umlkind == UPDATEOP) 
    { String res = data + "("; 
      String parString = parametersQueryFormCPP(env,local); 

      if (entity != null) 
      { ename = entity.getName(); } 

      if (entity != null && entity.isExternalApplication())
      { res = ename + "::" + cont + res + parString; 
        return res; 
      } 
      
      if (entity != null && data.startsWith("create") &&
               objectRef == null) 
      { if (data.substring(6).equals(ename))
        { return cont + res + parString; } 
      } 

      BehaviouralFeature bf = null; 

      if (entity != null) 
      { ename = entity.getName();
        bf = entity.getDefinedOperation(data); 
      } 

      if (bf != null && bf.isGeneric())
      { String tpars = 
          Type.resolveTypeParametersCPP( 
             bf.getTypeParameters(), 
             bf.getParameters(), parameters); 
        res = data + tpars + "("; 
      } 


      if (entity != null && entity.isClassScope(data))
      { res = ename + "::" + res + parString; 
        return res; 
      } 
      else if (objectRef != null)
      { String pre = objectRef.queryFormCPP(env,local);

        if ("super".equals(objectRef + ""))
        { res = pre + "::" + res + parString;
          return res; 
        } 

        if (objectRef.umlkind == CLASSID)
        { pre = ((BasicExpression) objectRef).classExtentQueryFormCPP(env,local); }

        if (objectRef.isMultiple()) // iterate, assume parameters.size() > 0
        { if (parameters == null || parameters.size() == 0) 
          { res = "  " + cont + "All" + ename + data + "(" + pre + ")"; } 
          else 
          { res = "  " + cont + "All" + ename + data + "(" + pre + "," + parString; } 
        } 
        else
        { if (downcast)
          { pre = "((" + ename + "*) " + pre + ")"; } 
          res = pre + "->" + res + parString;
        }
        return res;  
      }
	  
      String var = findEntityVariable(env);
      if (var != null && var.length() > 0) 
      { return var + "->" + res + parString; } 
      else 
      { return res + parString; }      
    } // may also be an arrayIndex
      

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null) 
          { String etype = elementType + ""; 
            String ind = arrayIndex.queryFormCPP(env,local); 
            if (isQualified())
            { return "((*" + data + ")[" + ind + "])"; } 
            String indopt = evaluateString("-",ind,"1"); // not for qualified ones
            if (type.getName().equals("String"))
            { return data + ".substr(" + indopt + ", 1)"; } 
            else 
            { return "((*" + data + ")[" + indopt + "])"; }
          }
          return data; 
        } 

        if (entity == null) { return data; } 

        String nme = entity.getName();

        if (entity.isClassScope(data))
        { if (arrayIndex != null) 
          { String etype = type.getCPP(elementType); 
            String ind = arrayIndex.queryFormCPP(env,local); 
         
            String indopt = evaluateString("-",ind,"1"); // not for qualified   
            if (type.getName().equals("String"))
            { return nme + "::" + data + ".substr(" + indopt + ", 1)"; } 
            return "(" + nme + "::" + data + "->at(" + indopt + "))";
          }
          return nme + "::" + data; 
        }  

        
        String var = findEntityVariable(env);

        String res = var + "->get" + data + "()";
        if (var == null || var.length() == 0)
        { res = data; }
		
        if (arrayIndex != null) 
        { String etype = type.getCPP(elementType); 
          String ind = arrayIndex.queryFormCPP(env,local); 
          if (isQualified())
          { return "((*(" + res + "))[" + ind + "])"; } 

          String indopt = evaluateString("-",ind,"1"); // not for qualified   
          if (type.getName().equals("String"))
          { return data + ".substr(" + indopt + ", 1)"; } 
          return "(" + res + "->at(" + indopt + "))";
        } // not for strings 
        return res; 
      } // valid but unecesary for static attributes
    }
    else if (entity != null) // objectRef != null
    { ename = entity.getName();
      String res = "";
      String pre = objectRef.queryFormCPP(env,local);
      Entity eref = objectRef.getEntity();  // Could be subclass of entity of data
      String cast = ""; 

      if (objectRef.umlkind == CLASSID)
      { Expression refindex = ((BasicExpression) objectRef).arrayIndex;
        if (refindex != null)
        { if (refindex.isMultiple())
          { if (objectRef.isOrdered())
            { cast = "(vector<" + ename + "*>*) "; }
            else 
            { cast = "(set<" + ename + "*>*) "; }

            res = ename + "::getAll" + data + "(" + cast + pre + ")";
          }  // E[inds].data
          else 
          { res = pre + "->get" + data + "()"; }  // E[ind].data
        } 
        else if (entity.isClassScope(data))
        { res = pre + "::get" + data + "()"; }   // E.data
        else 
        { pre = cont + "get" + ename.toLowerCase() + "_s()"; 
          res = ename + "::getAll" + data + "(" + pre + ")";
        } 
      } 
      else if ("super".equals(objectRef + ""))
      { res = pre + "::" + res;
        return res; 
      } 
      else if (objectRef.isMultiple())
      { if (objectRef.isOrdered())
        { cast = "(vector<" + ename + "*>*) "; }
        else 
        { cast = "(set<" + ename + "*>*) "; }

        if (isOrdered())
        { res = ename + "::getAllOrdered" + data + "(" + cast + pre + ")"; }
        else
        { res = ename + "::getAll" + data + "(" + cast + pre + ")"; }
      }
      else
      { if (downcast)
        { pre = "((" + ename + "*) " + pre + ")"; }
        if (entity != null && entity.isClassScope(data))
        { res = pre + "::get" + data + "()"; } 
        else 
        { res = pre + "->get" + data + "()"; }
      }

      if (arrayIndex != null) 
      { String ind = arrayIndex.queryFormCPP(env,local); 
        if (isQualified())
        { return "(" + res + "->at(" + ind + "))"; }  
        String indopt = evaluateString("-",ind,"1"); // not for qualified, Strings
        if (type.getName().equals("String"))
        { return res + ".substr(" + indopt + ", 1)"; } 
        return "(" + res + "->at(" + indopt + "))";    
      } 
      return res; 
    } // version with array? obj.r[i] for each obj: objs? 
    return data;
  }
 
  public Statement generateDesign(java.util.Map env, boolean local) 
  { // if multiple objectRef:  for (v : objectRef) { v.data(pars) } 
    // otherwise  objectRef.data(pars)
    // objs.isDeleted  is a call  KillAllE(objs)

    if (isEvent || umlkind == Expression.UPDATEOP) // an operation of entity
    { if (entity != null) 
      { BehaviouralFeature bf = entity.getDefinedOperation(data); 
        if (bf != null && bf.isStatic())
        { InvocationStatement res = new InvocationStatement(this); 
          return res; 
        } 
      } 

      if (objectRef != null && objectRef.isMultiple())
      { String v = Identifier.nextIdentifier("v_"); 
        BasicExpression ve = new BasicExpression(v); 
        ve.setType(objectRef.getElementType()); 
        ve.umlkind = Expression.VARIABLE; 
        BinaryExpression test = new BinaryExpression(":", ve, objectRef); 
        BasicExpression invokee = (BasicExpression) clone(); 
        invokee.setObjectRef(ve); 
        InvocationStatement invoke = new InvocationStatement(invokee); 
        WhileStatement lp = new WhileStatement(test, invoke); 
        lp.setLoopKind(Statement.FOR);
        lp.setLoopRange(ve,objectRef);  
        // lp.setLoopTest(test); 
        return lp; 
      }  
      InvocationStatement res = new InvocationStatement(this); 
      return res; 
    } 
    
    if (umlkind == Expression.FUNCTION && data.equals("isDeleted"))
    { if (objectRef.umlkind == Expression.CLASSID && (objectRef instanceof BasicExpression) && 
          ((BasicExpression) objectRef).arrayIndex == null) 
      { BasicExpression killset = new BasicExpression("allInstances"); 
        killset.setObjectRef(objectRef); 
        killset.setType(objectRef.getType()); 
        killset.setElementType(objectRef.getElementType()); 
        BasicExpression killop = new BasicExpression("killAll" + ((BasicExpression) objectRef).data); 
        killop.umlkind = Expression.UPDATEOP; 
        killop.addParameter(killset); 
        // killop.setStatic(true); 
        return new InvocationStatement(killop); 
      } // Could be abstract class
      String eename = objectRef.type.getName(); 
      Entity ent = null; 
      String all = ""; 
      String opname = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName();
        ent = objectRef.elementType.entity;  
      }
      else 
      { ent = objectRef.type.entity; } 

      if (ent.isAbstract())
      { opname = "killAbstract" + eename; }
      else 
      { opname = "kill" + all + eename; } 
      BasicExpression killop = new BasicExpression(opname); 
      killop.umlkind = Expression.UPDATEOP; 
      killop.addParameter(objectRef); 
      return new InvocationStatement(killop); 
    } 
    else 
    { return new SequenceStatement(); } 
  }  

  public Statement generateDesign(java.util.Map env,
                           String operator,
                           Expression rhs, boolean local)
  { if (operator.equals("="))
    { return generateDesignEq(rhs); }
    else if (operator.equals(":"))
    { return generateDesignIn(rhs); }
    /* else if (operator.equals("/:"))
    { return generateDesignNotIn(rhs); }
    else if (operator.equals("<:"))
    { return generateDesignSubset(rhs); }
    else if (operator.equals("-"))
    { return generateDesignSubtract(rhs); } */ 
    else
    { return new SequenceStatement(); }
  }

public Statement generateDesignEq(Expression rhs)
{ if (objectRef == null)
  { return new AssignStatement(this, rhs); }
  else if (objectRef.isMultiple())
  { BasicExpression lhs = (BasicExpression) clone();
    String v = Identifier.nextIdentifier("v_");
    BasicExpression var = new BasicExpression(v, objectRef);
    lhs.setObjectRef(var);  // will change typing/multiplicity
    AssignStatement update = new AssignStatement(lhs, rhs); 
    Statement lp = Expression.iterationLoop(var, objectRef, update);
    return lp;
  }
  else
  { return new AssignStatement(this, rhs); }
}

  public Statement generateDesignIn(Expression rhs)
  { // for rhs : lhs, same as
  // lhs := lhs->including(rhs)

  if (umlkind == CLASSID && arrayIndex == null)
  { CreationStatement cs = new CreationStatement(data, rhs + "");
    cs.setInstanceType(type);
    cs.setElementType(type);
    return cs;
  }
  else if (objectRef == null)
  { BinaryExpression inclu = new BinaryExpression("->including", this, rhs);
    return new AssignStatement(this, inclu); 
  }
  else if (objectRef.isMultiple())
  { BasicExpression lhs = (BasicExpression) clone();
    String v = Identifier.nextIdentifier("v_");
    BasicExpression var = new BasicExpression(v, objectRef);
    lhs.setObjectRef(var);  // will change typing/multiplicity
    BinaryExpression inclu = new BinaryExpression("->including", lhs, rhs);
    AssignStatement update = new AssignStatement(lhs, inclu); 
    Statement lp = Expression.iterationLoop(var, objectRef, update);
    return lp;
  }
  else if (multiplicity == ModelElement.ZEROONE)
  { Expression sizeiszero = new UnaryExpression("->isEmpty", this); 
    SequenceStatement skip = new SequenceStatement(); 
    SetExpression sett = new SetExpression(); 
    sett.addElement(rhs); 
    AssignStatement setthis = new AssignStatement(this, sett); 
    return new ConditionalStatement(sizeiszero, setthis, skip); 
  } 
  else 
  { BinaryExpression inclu = new BinaryExpression("->including", this, rhs);
    return new AssignStatement(this, inclu);  
  }
}


public Statement generateDesignNotIn(Expression rhs)
{ // for rhs /: lhs, same as
  // lhs := lhs->excluding(rhs)

  if (objectRef == null)
  { BinaryExpression exclu = new BinaryExpression("->excluding", this, rhs);
    return new AssignStatement(this, exclu); }
  else if (objectRef.isMultiple())
  { BasicExpression lhs = (BasicExpression) clone();
    String v = Identifier.nextIdentifier("v_");
    BasicExpression var = new BasicExpression(v, objectRef);
    lhs.setObjectRef(var);  // will change typing/multiplicity
    BinaryExpression exclu = new BinaryExpression("->excluding", lhs, rhs);
    AssignStatement update = new AssignStatement(lhs, exclu); 
    Statement lp = iterationLoop(var, objectRef, update);
    return lp;
  }
  else
  { BinaryExpression exclu = new BinaryExpression("->excluding", this, rhs);
    return new AssignStatement(this, exclu);   }
}

public Statement generateDesignSubset(Expression rhs)
{ // for rhs <: lhs, same as
  // lhs := lhs->union(rhs)

  if (objectRef == null)
  { BinaryExpression inclu = new BinaryExpression("->union", this, rhs);
    return new AssignStatement(this, inclu); }
  else if (objectRef.isMultiple())
  { BasicExpression lhs = (BasicExpression) clone();
    String v = Identifier.nextIdentifier("v_");
    BasicExpression var = new BasicExpression(v, objectRef);
    lhs.setObjectRef(var);  // will change typing/multiplicity
    BinaryExpression inclu = new BinaryExpression("->union", lhs, rhs);
    AssignStatement update = new AssignStatement(lhs, inclu); 
    Statement lp = iterationLoop(var, objectRef, update);
    return lp;
  }
  else
  { BinaryExpression inclu = new BinaryExpression("->union", this, rhs);
    return new AssignStatement(this, inclu);   }
}

public Statement generateDesignSubtract(Expression rhs)
{ // for rhs /<: lhs, same as
  // lhs := lhs - rhs

  if (objectRef == null)
  { BinaryExpression inclu = new BinaryExpression("-", this, rhs);
    return new AssignStatement(this, inclu); }
  else if (objectRef.isMultiple())
  { BasicExpression lhs = (BasicExpression) clone();
    String v = Identifier.nextIdentifier("v_");
    BasicExpression var = new BasicExpression(v, objectRef);
    lhs.setObjectRef(var);  // will change typing/multiplicity
    BinaryExpression inclu = new BinaryExpression("-", lhs, rhs);
    AssignStatement update = new AssignStatement(lhs, inclu); 
    Statement lp = iterationLoop(var, objectRef, update);
    return lp;
  }
  else
  { BinaryExpression inclu = new BinaryExpression("-", this, rhs);
    return new AssignStatement(this, inclu);   }
}



  public String updateForm(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryForm(env,local); 
    String cont = "Controller.inst()"; 

    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("WARNING: No defined entity for operation: " + this); 
        System.err.println("Assuming it is a global operation (use case, library op, etc)");
 
        if (objectRef == null) 
        { return cont + "." + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryForm(env,local);
          return preref + "." + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "." + cont + "." + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "." + data + pars + ";"; } 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "." + data + pars + ";";
      }
      else if (objectRef.umlkind == CLASSID && entity.isExternalApplication())
      { return ename + "." + cont + "." + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && 
               entity.isClassScope(data)) //  || entity.hasStereotype("external")))  // E.op()
      { return ename + "." + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))   // if (env.containsValue("self")) then just "self"
      { return "this." + data + pars + ";"; } 
      else if (local || "super".equals(objectRef + ""))
      { String pre = objectRef.queryForm(env,local);
        return pre + "." + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryForm(env,local);
        if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + "." + ename.toLowerCase() + "s";
          } 
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((" + ename + ") " + var + ".get(" + ind + "))." + data + pars; }
        else 
        { call = cont + "." + 
                 data + "((" + ename + ") " + var + ".get(" + ind + ")"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  List " + var + " = new Vector();\n" + 
               "  " + var + ".addAll(" + pre + ");\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + ".size(); " +
                       ind + "++)\n" +
               "  { "  + call + "; }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryForm(env,local);
        String ss = cont + "." + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryForm(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + ".kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{} /* No update form for: " + this + " */"; } 
      // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }
  // and similarly for B?

  public String deltaUpdateForm(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryForm(env,local); 
    String cont = "Controller.inst()"; 

    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("WARNING: No defined entity for operation: " + this); 
        System.err.println("Assuming it is a global operation (use case, library op, etc)");
 
        if (objectRef == null) 
        { return cont + "." + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryForm(env,local);
          return preref + "." + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "." + cont + "." + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "." + data + pars + ";"; } 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "." + data + pars + ";";
      }
      else if (objectRef.umlkind == CLASSID && entity.isExternalApplication())
      { return ename + "." + cont + "." + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && 
               entity.isClassScope(data)) //  || entity.hasStereotype("external")))  // E.op()
      { return ename + "." + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))
      { return "this." + data + pars + ";"; } 
      else if (local || "super".equals(objectRef + "") ||
               entity.isComponent())
      { String pre = objectRef.queryForm(env,local);
        return pre + "." + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryForm(env,local);
        if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + "." + ename.toLowerCase() + "s";
          } 
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((" + ename + ") " + var + ".get(" + ind + "))." + data + pars; }
        else 
        { call = cont + "." + 
                 data + "((" + ename + ") " + var + ".get(" + ind + ")"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  List " + var + " = new Vector();\n" + 
               "  " + var + ".addAll(_newobjs);\n" +
               "  " + var + ".addAll(_modobjs);\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + ".size(); " +
                       ind + "++)\n" +
               "  { if (" + var + ".get(" + ind + ") instanceof " + ename + ")\n" +
               "    { "  + call + "; }\n" + 
               "  }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryForm(env,local);
        String ss = cont + "." + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryForm(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + ".kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{}"; } // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }

  public String updateFormJava6(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryFormJava6(env,local); 
    String cont = "Controller.inst()"; 

    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("! WARNING: No defined entity for operation: " + this); 
        System.err.println("! Assuming it is a global operation (use case, library op, etc)");
         
        if (objectRef == null) 
        { return cont + "." + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryFormJava6(env,local);
          return preref + "." + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "." + cont + "." + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "." + data + pars + ";"; } 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "." + data + pars + ";";
      }
      else if (objectRef.umlkind == CLASSID && entity.isExternalApplication())
      { return ename + "." + cont + "." + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && entity.isClassScope(data))  // E.op()
      { return ename + "." + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))
      { return "this." + data + pars + ";"; } 
      else if (local || "super".equals(objectRef + "") ||
               entity.isComponent())
      { String pre = objectRef.queryFormJava6(env,local);
        return pre + "." + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryFormJava6(env,local);
        if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + "." + ename.toLowerCase() + "s";
          } 
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((" + ename + ") " + var + ".get(" + ind + "))." + data + pars; }
        else 
        { call = cont + "." + 
                 data + "((" + ename + ") " + var + ".get(" + ind + ")"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  ArrayList " + var + " = new ArrayList();\n" + 
               "  " + var + ".addAll(" + pre + ");\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + ".size(); " +
                       ind + "++)\n" +
               "  { "  + call + "; }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryFormJava6(env,local);
        String ss = cont + "." + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryFormJava6(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("!! ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + ".kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{} /* No update form for: " + this + " */"; } 
      // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }
  // and similarly for B?

  public String updateFormJava7(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryFormJava7(env,local); 
    String cont = "Controller.inst()"; 

    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("!! WARNING: No defined entity for operation: " + this); 
        System.err.println("!! Assuming it is a global operation (use case, library op, etc)");
         
        if (objectRef == null) 
        { return cont + "." + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryFormJava7(env,local);
          return preref + "." + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "." + cont + "." + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "." + data + pars + ";"; } 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "." + data + pars + ";";
      }
      else if (objectRef.umlkind == CLASSID && entity.isExternalApplication())
      { return ename + "." + cont + "." + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && entity.isClassScope(data))  // E.op()
      { return ename + "." + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))
      { return "this." + data + pars + ";"; } 
      else if (local || "super".equals(objectRef + "") ||
               entity.isComponent())
      { String pre = objectRef.queryFormJava7(env,local);
        return pre + "." + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryFormJava7(env,local);
        if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + "." + ename.toLowerCase() + "s";
          } 
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((" + ename + ") " + var + ".get(" + ind + "))." + data + pars; }
        else 
        { call = cont + "." + 
                 data + "((" + ename + ") " + var + ".get(" + ind + ")"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  ArrayList<" + ename + "> " + var + " = new ArrayList<" + ename + ">();\n" + 
               "  " + var + ".addAll(" + pre + ");\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + ".size(); " +
                       ind + "++)\n" +
               "  { "  + call + "; }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryFormJava7(env,local);
        String ss = cont + "." + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryFormJava7(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + ".kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{} /* No update form for: " + this + " */"; } 
      // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }


  public String updateFormCSharp(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryFormCSharp(env,local); 
    String cont = "Controller.inst()"; 

    if ("printStackTrace".equals(data) && objectRef != null &&
        Type.isOclExceptionType(objectRef))
    { String rqf = objectRef.queryFormCSharp(env,local); 
      return "Console.WriteLine(" + rqf + ".StackTrace);";  
    }  

    // Update form: 
    if ("setTime".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclDate")) 
      { if (parameters != null && parameters.size() > 0) 
        { Expression par1 = (Expression) parameters.get(0); 
          String par1qf = par1.queryFormCSharp(env,local); 
          String rqf = objectRef.queryFormCSharp(env,local); 
                 
          return "  " + rqf + 
            " = DateTimeOffset.FromUnixTimeMilliseconds(" + par1qf + ").DateTime;";
        } 
      }  
    } 

    if (data.startsWith("create") && objectRef == null) 
    { String entname = data.substring(6);
      System.out.println(">>> Creation operation for " + entname); 
         
      // Entity entx = (Entity) ModelElement.lookupByName(entname,entities); 
      if (entity != null && entity.getName().equals(entname)) 
      { return cont + "." + data + pars + ";"; }
    } 


    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("!! WARNING: No defined entity for operation: " + this); 
        System.err.println("!! Assuming it is a global operation (use case, library op, etc)");
         
        if (objectRef == null) 
        { return cont + "." + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryFormCSharp(env,local);
          return preref + "." + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "." + cont + "." + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "." + data + pars + ";"; }
 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "." + data + pars + ";";
      }
      else if (objectRef.umlkind == CLASSID && 
               entity.isExternalApplication())
      { return ename + "." + cont + "." + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && 
               entity.isClassScope(data))  // E.op()
      { return ename + "." + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))
      { return "this." + data + pars + ";"; } 
      else if (local || "super".equals(objectRef + "") ||
               entity.isComponent())
      { String pre = objectRef.queryFormCSharp(env,local);  // base?    
        return pre + "." + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryFormCSharp(env,local);
        if (objectRef.umlkind == CLASSID && 
            (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + ".get" + ename.toLowerCase() + "_s()";
          } 
        } 
        else if (objectRef.elementType != null && 
                 objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((" + ename + ") " + var + "[" + ind + "])." + data + pars; }
        else 
        { call = cont + "." + 
                 data + "((" + ename + ") " + var + "[" + ind + "]"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  ArrayList " + var + " = new ArrayList();\n" + 
               "  " + var + ".AddRange(" + pre + ");\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + ".Count; " +
                       ind + "++)\n" +
               "  { "  + call + "; }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryFormCSharp(env,local);
        String ss = cont + "." + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryFormCSharp(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + ".kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{} /* No update form for: " + this + " */"; } 
      // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }
  // and similarly for B?

  public String updateFormCPP(java.util.Map env, boolean local) 
  { String pars = "(" + parametersQueryFormCPP(env,local); 
    String cont = "Controller::inst->"; 

    if ("printStackTrace".equals(data) && objectRef != null &&
        Type.isOclExceptionType(objectRef))
    { String rqf = objectRef.queryFormCPP(env,local); 
      return "cerr << " + rqf + ".what() << endl;";  
    }  

    if (data.startsWith("create") && objectRef == null) 
    { String entname = data.substring(6);
      System.out.println(">>> Creation operation for " + entname); 
         
      // Entity entx = (Entity) ModelElement.lookupByName(entname,entities); 
      if (entity != null && entity.getName().equals(entname)) 
      { return cont + data + pars + ";"; }
    } 

    if ("delete".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile"))
      { return objectRef.queryFormCPP(env,local) + 
               "->deleteFile();"; 
      } 
    } 

    if ("printf".equals(data) && objectRef != null) 
    { if (objectRef.type != null && 
          objectRef.type.getName().equals("OclFile") && 
          parameters != null && 
          parameters.size() > 1 && 
          parameters.get(1) instanceof SetExpression)
      { Expression format = (Expression) parameters.get(0); 
        Expression sq = (Expression) parameters.get(1);
        String p1 = format.queryFormCPP(env,local);  
        String cseq = 
          ((SetExpression) sq).toCSequence(env,local); 
        return objectRef.queryFormCPP(env,local) + 
               "->printf(" + p1 + "," + cseq + ");"; 
      } 
    } 


    if (isEvent) // an operation of entity
    { 
      if (entity == null) 
      { System.err.println("Warning!: No defined entity for: " + this); 
        if (objectRef == null) 
        { return cont + data + pars + ";"; } 
        else 
        { String preref = objectRef.queryFormCPP(env,local);
          return preref + "->" + data + pars + ";"; 
        }
      } 
      
      String ename = entity.getName();
        
      if (objectRef == null)
      { if (entity.isExternalApplication())
        { return ename + "::" + cont + data + pars + ";"; } 
        if (entity.isClassScope(data))
        { return ename + "::" + data + pars + ";"; } 
        if (local) 
        { return data + pars + ";"; } 

        String var = findEntityVariable(env);
        return var + "->" + data + pars + ";";
      }
      else if ("super".equals(objectRef + ""))
      { String pre = objectRef.queryFormCPP(env,local);
        String res = pre + "::" + data + pars + ";";
        return res; 
      } 
      else if (objectRef.umlkind == CLASSID && entity.isExternalApplication())
      { return ename + "::" + cont + data + pars + ";"; } 
      else if (objectRef.umlkind == CLASSID && entity.isClassScope(data))  // E.op()
      { return ename + "::" + data + pars + ";"; } 
      else if ("self".equals(objectRef + ""))
      { return "this->" + data + pars + ";"; } 
      else if (local || 
               entity.isComponent())
      { String pre = objectRef.queryFormCPP(env,local);  // base?    
        return pre + "->" + data + pars + ";"; 
      }
      else if (objectRef.isMultiple())
      { String pre = objectRef.queryFormCPP(env,local);
        if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression))
        { BasicExpression oref = (BasicExpression) objectRef; 
          if (oref.arrayIndex == null) 
          { ename = oref.data; // pre; 
            pre = cont + "get" + ename.toLowerCase() + "_s()";
          } 
        } 
        else if (objectRef.elementType != null && objectRef.elementType.isEntity())
        { Type et = objectRef.elementType; 
          Entity ent = et.getEntity(); 
          if (ent != null)
          { ename = ent.getName(); } 
        } 
        String var = ename.toLowerCase() + data + "x";
        String ind = Identifier.nextIdentifier(var + "_ind");
        String call;
        if (local)
        { call = " ((*" + var + ")[" + ind + "])->" + data + pars; }
        else 
        { call = cont + 
                 data + "((*" + var + ")[" + ind + "]"; 
          if (parameters != null && parameters.size() > 0)
          { call = call + ","; } 
          call = call + 
                 pars.substring(1,pars.length()); 
        }
        return "  vector<" + ename + "*>* " + var + " = new vector<" + ename + "*>();\n" + 
               "  " + var + "->insert(" + var + "->end(), " + pre + "->begin(), " + 
                                      pre + "->end());\n" +
               "  for (int " + ind + " = 0; " + ind + " < " + var + "->size(); " +
                       ind + "++)\n" +
               "  { "  + call + "; }\n";
      }
      else // for !(objectRef.isMultiple())
      { String pre = objectRef.queryFormCPP(env,local);
        String ss = cont + data + "(" + pre; 
        if (parameters != null && parameters.size() > 0)
        { ss = ss + ","; } 
        return ss + pars.substring(1,pars.length()) + ";";  
      }
    }
    else if (umlkind == FUNCTION && data.equals("isDeleted"))
    { String pre = objectRef.queryFormCPP(env,local);
      Type otype = objectRef.type; 
      if (otype == null) 
      { System.err.println("ERROR: no type for " + objectRef); 
        return ""; 
      } 
      String eename = otype.getName(); 
      String all = ""; 
      if (objectRef.isMultiple()) 
      { all = "All";
        eename = objectRef.elementType.getName(); 
      } 
      return cont + "kill" + all + eename + "(" + pre + ");"; 
    } 
    else 
    { return "{} /* Update form for: " + this + " */"; } 
      // Controller.inst()." + data + pars + ";"; } 
      // no sensible update form?; 
  }
  // and similarly for B?

  public String updateForm(java.util.Map env,
                           String operator,
                           String val2, Expression exp2, boolean local)
  { if (operator.equals("="))
    { return updateFormEq(env,val2,exp2,local); }
    else if (operator.equals(":"))
    { return updateFormIn(env,val2,local); }
    else if (operator.equals("/:"))
    { return updateFormNotin(env,val2,exp2,local); }
    else if (operator.equals("<:"))
    { return updateFormSubset(env,val2,local); }
    else if (operator.equals("-"))
    { return updateFormSubtract(env,val2,exp2,local); }
    else
    { return "{} /* invalid update to " + this + " by " + operator + " with " + val2 + " */"; }
  }

  public String updateFormJava6(java.util.Map env,
                           String operator,
                           String val2, Expression exp2, boolean local)
  { if (operator.equals("="))
    { return updateFormEqJava6(env,val2,exp2,local); }
    else if (operator.equals(":"))
    { return updateFormInJava6(env,val2,local); }
    else if (operator.equals("/:"))
    { return updateFormNotinJava6(env,val2,exp2,local); }
    else if (operator.equals("<:"))
    { return updateFormSubsetJava6(env,val2,local); }
    else if (operator.equals("-"))
    { return updateFormSubtractJava6(env,val2,exp2,local); }
    else
    { return "{} /* invalid update to " + this + " by " + operator + " with " + val2 + " */"; }
  }

  public String updateFormJava7(java.util.Map env,
                           String operator,
                           String val2, Expression exp2, boolean local)
  { if (operator.equals("="))
    { return updateFormEqJava7(env,val2,exp2,local); }
    else if (operator.equals(":"))
    { return updateFormInJava7(env,val2,local); }
    else if (operator.equals("/:"))
    { return updateFormNotinJava7(env,val2,exp2,local); }
    else if (operator.equals("<:"))
    { return updateFormSubsetJava7(env,val2,local); }
    else if (operator.equals("-"))
    { return updateFormSubtractJava7(env,val2,exp2,local); }
    else
    { return "{} /* invalid update to " + this + " by " + operator + " with " + val2 + " */"; }
  }

  public String updateFormCSharp(java.util.Map env,
                           String operator,
                           String val2, Expression exp2, boolean local)
  { if (operator.equals("="))
    { return updateFormEqCSharp(env,val2,exp2,local); }
    else if (operator.equals(":"))
    { return updateFormInCSharp(env,val2,local); }
    else if (operator.equals("/:"))
    { return updateFormNotinCSharp(env,val2,exp2,local); }
    else if (operator.equals("<:"))
    { return updateFormSubsetCSharp(env,val2,local); }
    else if (operator.equals("-"))
    { return updateFormSubtractCSharp(env,val2,exp2,local); }
    else
    { return "{} /* invalid update to " + this + " by " + operator + " with " + val2 + " */"; }
  }

  public String updateFormCPP(java.util.Map env,
                           String operator,
                           String val2, Expression exp2, boolean local)
  { if (operator.equals("="))
    { return updateFormEqCPP(env,val2,local); }
    else if (operator.equals(":"))
    { return updateFormInCPP(env,val2,local); }
    else if (operator.equals("/:"))
    { return updateFormNotinCPP(env,val2,exp2,local); }
    else if (operator.equals("<:"))
    { return updateFormSubsetCPP(env,val2,local); }
    else if (operator.equals("-"))
    { return updateFormSubtractCPP(env,val2,exp2,local); }
    else
    { return "{} /* invalid update to " + this + " by " + operator + " with " + val2 + " */"; }
  }

  public static String updateFormEqIndex(String lang, Expression obj, Expression ind,  
                             String val2, Expression var, java.util.Map env, boolean local)
  { if ("Java4".equals(lang))
    { return updateFormEqIndex(obj,ind,val2,var,env,local); } 
    else if ("Java6".equals(lang))
    { return updateFormEqIndexJava6(obj,ind,val2,var,env,local); }
    else if ("Java7".equals(lang))
    { return updateFormEqIndexJava7(obj,ind,val2,var,env,local); }
    else if ("CSharp".equals(lang))
    { return updateFormEqIndexCSharp(obj,ind,val2,var,env,local); }
    else if ("CPP".equals(lang))
    { return updateFormEqIndexCPP(obj,ind,val2,var,env,local); }
    else 
    { return "/* Unsupported language for x->at(y) := z */"; }  
  } 

  public static String updateFormEqIndex(Expression obj, Expression ind,  
                             String val2, Expression var, java.util.Map env, boolean local)
  { // obj[ind] = val2 where obj is complex expression, 
    // either a sequence, ref or map, or 
    // itself an indexed expression
 
    if (ind != null) 
    { String indopt = ind.queryForm(env,local);
      String lexp = obj.queryForm(env,local); 
      String wind = ind.wrap(indopt); 
      String wval = var.wrap(val2); 
 
      if (ind.type != null && "String".equals(ind.type.getName()))
      { return "((Map) " + lexp + ").put(" + wind + ", " + wval + ");"; }  // map[ind] = val2 
      else if (obj.isRef())
      { return lexp + "[" + indopt + " -1] = " + val2 + ";"; }  
      else 
      { return "((Vector) " + lexp + ").set((" + indopt + " -1), " + wval + ");"; }  
    } 
    return "/* Error: null index */"; 
  } 

  public static String updateFormEqIndexJava6(Expression obj, Expression ind,  
                             String val2, Expression var, java.util.Map env, boolean local)
  { // obj[ind] = val2 where obj is complex expression, 
    // either a sequence or map, or 
    // itself an indexed expression
 
    if (ind != null) 
    { String indopt = ind.queryFormJava6(env,local);
      String lexp = obj.queryFormJava6(env,local); 
      String wind = ind.wrapJava6(indopt); 
      String wval = var.wrapJava6(val2); 
 
      if (ind.type != null && "String".equals(ind.type.getName()))
      { return "((HashMap) " + lexp + ").put(" + wind + ", " + wval + ");"; }  // map[ind] = val2 
      else if (obj.isRef())
      { return lexp + "[" + indopt + " -1] = " + val2 + ";"; }  
      else 
      { return "((ArrayList) " + lexp + ").set((" + indopt + " -1), " + wval + ");"; }  
    } 
    return "/* Error: null index */"; 
  } 

  public static String updateFormEqIndexJava7(Expression obj, Expression ind,  
    String val2, Expression var, java.util.Map env, boolean local)
  { // obj[ind] = val2 where obj is complex expression, 
    // either a sequence or map, or 
    // itself an indexed expression

   System.out.println(">>> Update to " + obj + "[" + ind + 
                      "] := " + val2); 
 
   Type objt = obj.getType();
   if (objt == null) 
   { return "/* No type for: " + obj + " */"; } 
 
   Type objet = objt.getElementType(); 
   String j7type = objt.getJava7(objet); 

   if (ind != null) 
   { String indopt = ind.queryFormJava7(env,local);
     String lexp = obj.queryFormJava7(env,local); 
     String wind = ind.wrap(indopt); 
     String wval = var.wrap(val2); 
 
     if (objt.isMapType())
     { return "((" + j7type + ") " + lexp + ").put(" + wind + ", " + wval + ");"; }  // map[ind] = val2 
     else if (obj.isRef())
     { return lexp + "[" + indopt + " -1] = " + val2 + ";"; }  
     else if (obj instanceof BasicExpression) 
     { Vector pars = new Vector(); 
       // setatpars.add(obj); 
       pars.add(ind); 
       pars.add(var); 
       Expression setatexpr = 
         BasicExpression.newFunctionBasicExpression("setAt", 
                                                    obj,pars);                       

       return ((BasicExpression) obj).updateFormEqJava7(env, 
         "Ocl.setAt(" + lexp + ", " + indopt + ", " + wval + ")", 
         setatexpr, local);
      }   
      else 
      { return "(" + lexp + ").set(" + indopt + " -1, " + val2 + ");"; }
    } 

    return "/* Error: null index */"; 
  } 
  // obj.updateFormEqJava7(env, String val2, Expression var, boolean local)

  public static String updateFormEqIndexCSharp(Expression obj, Expression ind,  
                             String val2, Expression var, java.util.Map env, boolean local)
  { // obj[ind] = val2 where obj is complex expression, 
    // either a sequence, reference or map, or 
    // itself an indexed expression
 
    if (ind != null) 
    { String indopt = ind.queryFormCSharp(env,local);
      String lexp = obj.queryFormCSharp(env,local); 
      String wind = ind.wrapCSharp(indopt); 
      String wval = var.wrapCSharp(val2); 
 
      if (ind.type != null && 
          "String".equals(ind.type.getName()))
      { return "((Hashtable) " + lexp + ")[" + wind + "] = " + wval + ";"; }  // map[ind] = val2 
      else if (Type.isReferenceType(obj.type))
      { return lexp + "[" + indopt + "-1] = " + val2 + ";"; } 
      else 
      { return "((ArrayList) " + lexp + ")[" + indopt + " -1] = " + wval + ";"; }  
    } 
    return "/* Error: null index */"; 
  } 

  public static String updateFormEqIndexCPP(Expression obj, Expression ind,  
                             String val2, Expression var, java.util.Map env, boolean local)
  { // (*obj)[ind] = val2 where obj is complex expression, 
    // either a sequence or map, or 
    // itself an indexed expression
    
 
    if (ind != null) 
    { String indopt = ind.queryFormCPP(env,local);
      String lexp = obj.queryFormCPP(env,local); 
      String wind = indopt; 
      String wval = var.queryFormCPP(env,local); 
 
      if (ind.type != null && "String".equals(ind.type.getName()))
      { return "(*" + lexp + ")[" + indopt + "] = " + wval + ";"; }   
      else if (obj.isRef())
      { return "(" + lexp + ")[" + indopt + " -1] = " + wval + ";"; }  
      else 
      { return "(*" + lexp + ")[" + indopt + " -1] = " + wval + ";"; }  
    } 

    return "/* Error: null index in " + obj + "[index] := " + val2 + " */"; 
  } 

  public String updateFormEq(java.util.Map env,
                             String val2, Expression var, boolean local)
  { String cont = "Controller.inst()"; 

    // System.out.println("^^^^^^^^^ Assignment " + this + " " + val2); 
    // System.out.println("^^^^^^^^^ Assignment " + umlkind + " " + objectRef); 

    String datax = data;
    if (objectRef != null) 
    { datax = objectRef.queryFormCPP(env,local) + "." + data; } 

    if ("subrange".equals(data) && parameters != null && 
        objectRef != null && 
        objectRef instanceof BasicExpression)
    { // obj.subrange(a,b) := val2 for strings is same as 
      // obj := obj.subrange(1,a-1) + val2 + obj.subrange(b+1)

      Expression par1 = (Expression) parameters.get(0); 
      Expression par2 = (Expression) parameters.get(1); 

      Vector pars1 = new Vector(); 
      pars1.add(new BasicExpression(1)); 
      pars1.add(new BinaryExpression("-", par1, 
                       new BasicExpression(1))); 

      Expression rng1 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, pars1); 
      rng1.setType(objectRef.getType()); 
      rng1.setElementType(objectRef.getElementType()); 


      Expression rng2 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, 
              new BinaryExpression("+", par2, 
                       new BasicExpression(1))); 
      rng2.setType(objectRef.getType()); 
      rng2.setElementType(objectRef.getElementType()); 

      if (type != null && "String".equals(type.getName()))
      { // objx := objx.subrange(1,par1x-1) + val2 +
        //         objx.subrange(par2x+1)

        Expression newvar = 
          new BinaryExpression("+", rng1, 
            new BinaryExpression("+", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryForm(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEq(
                                 env,newval2,newvar,local); 
      } 
      else if (type != null && 
               "Sequence".equals(type.getName()))
      { // objx := objx.subrange(0,par1x-1) ^ val2 ^
        //         objx.subrange(par2x + 1)

        Expression newvar = 
          new BinaryExpression("->union", rng1, 
            new BinaryExpression("->union", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryForm(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEq(
                                 env,newval2,newvar,local); 
      } 
    } 
                

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't assign to: " + data + " */"; }
    
    if (umlkind == CLASSID && arrayIndex == null) 
    { if (val2.equals("{}") || val2.equals("Set{}") || val2.equals("Sequence{}"))  // delete the class extent
      { String datas = classExtentQueryForm(env,local);  
        return "List _" + data + " = new Vector(); \n" + 
               "  _" + data + ".addAll(" + datas + "); \n" + 
               "  " + cont + ".killAll" + data + "(_" + data + ");\n"; 
      } 
      else 
      { return "{} /* can't assign to: " + data + " */"; }
    } 

    if (umlkind == VARIABLE)
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryForm(env,local); 
        String wind = arrayIndex.wrap(indopt); 
        String wval = var.wrap(val2); 
        if ((arrayIndex.type != null && 
             "String".equals(arrayIndex.type.getName())) ||
            BasicExpression.isMapAccess(this))
        { return datax + ".put(" + wind + ", " + wval + ");"; }  
        // map[index] = val2 
        else 
        { return datax + ".set((" + indopt + " -1), " + wval + ");"; }  
      } 
      return datax + " = " + val2 + ";"; 
    }

    String nme = ""; 
    if (entity != null) 
    { nme = entity.getName();
      if (entity.isBidirectionalRole(data))
      { local = false; }
    }  
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null)
          { String ind = arrayIndex.queryForm(env,local); 
            String wval = var.wrap(val2); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this)
               )
            { return data + ".put(" + ind + ", " + wval + ");"; } // for maps
            return data + ".set((" + ind + " - 1), " + wval + ");"; 
          }
          return data + " = " + val2 + ";";
        } 

        if (entity != null && entity.isClassScope(data))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryForm(env,local); 
            String wval = var.wrap(val2); 
            if ("String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this)
               )
            { return data + ".put(" + indopt + ", " + wval + ");"; } // for maps
            return nme + ".set" + data + "((" + indopt + " -1), " + wval + ");"; 
          } 
          return nme + ".set" + data + "(" + val2 + ");"; 
        }   // and with array index
        
        String varx = findEntityVariable(env); 

        String target = ""; 
        if (varx != null && varx.equals("this")) 
        { System.err.println("!! WARNING: using self with non-local update " + this);
          target = varx + ",";
        } 
        else if (varx != null)  
        { target = varx + ","; } 
        
        // data is a feature of the entity, non-local consequences of the update
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryForm(env,local); 
          if (isQualified() || 
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cont + ".set" + data + "(" + target + ind + ", " + val2 + ");"; } 
          String indopt = evaluateString("-",ind,"1"); // not for qualified
          return cont + ".set" + data + "(" + target + indopt + "," + val2 + ");";
        }
        return cont + ".set" + data + "(" + target + val2 + ");"; 
      } // Really cont + ".set" ... in each case
    }
    else // objectRef != null
    { String pre = objectRef.queryForm(env,local);

      if (entity.isClassScope(data))
      { if (arrayIndex != null) 
        { String ind1 = arrayIndex.queryForm(env,local);
          String indopt = evaluateString("-",ind1,"1");
          return nme + ".set" + data + "(" + indopt + ", " + val2 + ");"; 
        } 
        return nme + ".set" + data + "(" + val2 + ");"; 
      }   

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // C[inds].data = val2
          { return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // C[ind].data = val2 
          { return cont + ".set" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".setAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      if (objectRef.isMultiple()) 
      { // objectRef.umlkind != CLASSID
        String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "." + nme + "Ops"; } 

        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryForm(env,local); 
            if (isQualified())
            { return cref + ".setAll" + data + "(" + pre + "," + ind + "," + 
                                val2 + ");";
            } 
            String indopt = evaluateString("-",ind,"1"); 
            return cref + ".setAll" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
          }
          return cref + ".setAll" + data + "(" + pre + "," + val2 + ");";
        } 
      }
      else // not objectRef.isMultiple()
      { if (local)
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryForm(env,local); 
            if (isQualified() ||   
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return pre + ".set" + data + "(" + ind + "," + 
                                val2 + ");";
            } 
            String indopt = evaluateString("-",ind,"1"); 
            return pre + ".set" + data + "(" + indopt + "," + val2 + ");";
          }
          return pre + ".set" + data + "(" + val2 + ");"; 
        }
        else if (arrayIndex != null) 
        { String ind = arrayIndex.queryForm(env,local);
          if (isQualified() ||
            "String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
          { return cont + ".set" + data + "(" + pre + "," + ind + "," + val2 + ");"; }    
          String indopt = evaluateString("-",ind,"1"); 
          return cont + ".set" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        return cont + ".set" + data + "(" + pre + "," + val2 + ");"; 
      }
    }
    return "{} /* unrecognised update: " + this + " = " + val2 + " */";
  }  // arrayIndex != null: setdata(var,arrayIndex,val) ?

  public String updateFormEqJava6(java.util.Map env,
             String val2, Expression var, boolean local)
  { String cont = "Controller.inst()"; 

    // System.out.println("#### " + this + " := " + val2); 

    if ("subrange".equals(data) && parameters != null && 
        objectRef != null && 
        objectRef instanceof BasicExpression)
    { // obj.subrange(a,b) := val2 for strings is same as 
      // obj := obj.subrange(1,a-1) + val2 + obj.subrange(b+1)

      Expression par1 = (Expression) parameters.get(0); 
      Expression par2 = (Expression) parameters.get(1); 

      Vector pars1 = new Vector(); 
      pars1.add(new BasicExpression(1)); 
      pars1.add(new BinaryExpression("-", par1, 
                       new BasicExpression(1))); 

      Expression rng1 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, pars1); 
      rng1.setType(objectRef.getType()); 
      rng1.setElementType(objectRef.getElementType()); 


      Expression rng2 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, 
              new BinaryExpression("+", par2, 
                       new BasicExpression(1))); 
      rng2.setType(objectRef.getType()); 
      rng2.setElementType(objectRef.getElementType()); 

      if (type != null && "String".equals(type.getName()))
      { // objx := objx.subrange(1,par1x-1) + val2 +
        //         objx.subrange(par2x+1)

        Expression newvar = 
          new BinaryExpression("+", rng1, 
            new BinaryExpression("+", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormJava6(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEqJava6(
                                 env,newval2,newvar,local); 
      } 
      else if (type != null && 
               "Sequence".equals(type.getName()))
      { // objx := objx.subrange(0,par1x-1) ^ val2 ^
        //         objx.subrange(par2x + 1)

        Expression newvar = 
          new BinaryExpression("->union", rng1, 
            new BinaryExpression("->union", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormJava6(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEqJava6(
                                 env,newval2,newvar,local); 
      } 
    } 
                
 
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't assign to: " + data + " */"; }
    
    if (umlkind == CLASSID && arrayIndex == null) 
    { if (val2.equals("{}") || val2.equals("Set{}") || val2.equals("Sequence{}"))  // delete the class extent
      { String datas = classExtentQueryFormJava6(env,local);  
        return "ArrayList _" + data + " = new ArrayList(); \n" + 
               "  _" + data + ".addAll(" + datas + "); \n" + 
               "  " + cont + ".killAll" + data + "(_" + data + ");\n"; 
      } 
      else 
      { return "{} /* can't assign to: " + this + " */"; }
    } 

    if (umlkind == VARIABLE)
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava6(env,local); 
        String wind = arrayIndex.wrapJava6(indopt); 
        String wval = var.wrapJava6(val2); 
        if ("String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
        { return data + ".put(" + wind + ", " + wval + ");"; }  // map[index] = val2 
        else 
        { return data + ".set((" + indopt + " -1), " + wval + ");"; }  
      }
      return data + " = " + val2 + ";"; 
    } // and map case. wrap val2

    String nme = entity.getName();
    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null)
          { String ind = arrayIndex.queryFormJava6(env,local); 
            String wind = arrayIndex.wrapJava6(ind); 
            String wval = var.wrapJava6(val2); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return data + ".put(" + wind + ", " + wval + ");"; } 
            return data + ".set((" + ind + " - 1)," + wval + ");"; 
          }
          return data + " = " + val2 + ";";
        } 

        if (entity != null && entity.isClassScope(data))
        { if (arrayIndex != null) 
          { String indopt = 
              arrayIndex.queryFormJava6(env,local); 
            // String wind = arrayIndex.wrapJava6(ind); 
            String wval = var.wrapJava6(val2); 
            if ("String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this)
               )
            { return data + ".put(" + indopt + ", " + wval + ");"; } // for maps
            return nme + ".set" + data + "((" + indopt + " -1), " + wval + ");"; 
          } 
          return nme + ".set" + data + "(" + val2 + ");"; 
        }   // and with array
        
        String varx = findEntityVariable(env); 

        String target = ""; 
        if (varx.equals("this")) 
        { System.err.println("!! WARNING: using self with non-local update " + this);
          target = varx + ",";
        } 
        else 
        { target = varx + ","; } 
        
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava6(env,local); 
          if (isQualified() ||
            "String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
          { return cont + ".set" + data + "(" + target + ind + ", " + val2 + ");"; } 
          String indopt = evaluateString("-",ind,"1"); // not for qualified
          return cont + ".set" + data + "(" + target + indopt + "," + val2 + ");";
        }
        return cont + ".set" + data + "(" + target + val2 + ");"; 
      } // Really cont + ".set" ... in each case
    }
    else // objectRef != null
    { String pre = objectRef.queryFormJava6(env,local);

      if (entity != null && entity.isClassScope(data))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormJava6(env,local); 
          String wval = var.wrapJava6(val2); 
          if ("String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this)
             )
          { return nme + "." + data + ".put(" + indopt + ", " + wval + ");"; } // for maps
          return nme + ".set" + data + "((" + indopt + " -1), " + val2 + ");"; 
        }
        return nme + ".set" + data + "(" + val2 + ");"; 
      }   // and with array

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // C[inds].data = val2
          { return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // C[ind].data = val2 
          { return cont + ".set" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".setAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      if (objectRef.isMultiple()) 
      { // objectRef.umlkind != CLASSID
        String cref = nme; 
        if (entity.isInterface())
        { cref = nme + "." + nme + "Ops"; } 
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava6(env,local); 
          if (isQualified() || 
            "String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
          { return cref + ".setAll" + data + "(" + pre + "," + ind + "," + 
                                val2 + ");";
          } 
          String indopt = evaluateString("-",ind,"1"); 
          return cref + ".setAll" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; 
      }
      else  // not objectRef.isMultiple()
      { if (local)
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava6(env,local); 
            if (isQualified() ||
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return pre + ".set" + data + "(" + ind + "," + 
                                val2 + ");";
            } 
            String indopt = evaluateString("-",ind,"1"); 
            return pre + ".set" + data + "(" + indopt + "," + val2 + ");";
          }
          return cont + ".set" + data + "(" + pre + ", " + val2 + ");"; 
        }
        else if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava6(env,local);
          if (isQualified())
          { return cont + ".set" + data + "(" + pre + "," + ind + "," + val2 + ");"; }    
          String indopt = evaluateString("-",ind,"1"); 
          return cont + ".set" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        // return cont + ".set" + data + "(" + pre + "," + val2 + ");"; 
      }
      return cont + ".set" + data + "(" + pre + "," + val2 + ");"; 
    }
    return "{} /* unrecognised update: " + this + " = " + val2 + " */";
  }  // arrayIndex != null: setdata(var,arrayIndex,val) ?

  public String updateFormEqJava7(java.util.Map env, String val2, Expression var, boolean local)
  { String cont = "Controller.inst()"; 
    String datax = data;
    if (objectRef != null) 
    { datax = objectRef.queryFormJava7(env,local) + "." + data; } 

    if ("subrange".equals(data) && parameters != null && 
        objectRef != null && 
        objectRef instanceof BasicExpression)
    { // obj.subrange(a,b) := val2 for strings is same as 
      // obj := obj.subrange(1,a-1) + val2 + obj.subrange(b+1)

      Expression par1 = (Expression) parameters.get(0); 
      Expression par2 = (Expression) parameters.get(1); 

      Vector pars1 = new Vector(); 
      pars1.add(new BasicExpression(1)); 
      pars1.add(new BinaryExpression("-", par1, 
                       new BasicExpression(1))); 

      Expression rng1 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, pars1); 
      rng1.setType(objectRef.getType()); 
      rng1.setElementType(objectRef.getElementType()); 


      Expression rng2 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, 
              new BinaryExpression("+", par2, 
                       new BasicExpression(1))); 
      rng2.setType(objectRef.getType()); 
      rng2.setElementType(objectRef.getElementType()); 

    /*  String objx = objectRef.queryFormJava7(env,local);
      String lqf = ((BasicExpression) objectRef).leftQueryFormJava7(env,local);  
      String par1x = par1.queryFormJava7(env,local); 
      String par2x = par2.queryFormJava7(env,local); */ 

      if (type != null && "String".equals(type.getName()))
      { // objx := objx.subrange(1,par1x-1) + val2 +
        //         objx.subrange(par2x+1)

        Expression newvar = 
          new BinaryExpression("+", rng1, 
            new BinaryExpression("+", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormJava7(env,local); 
           
       /* String res = lqf + " = (" + objx + ").substring(0," + par1x + "-1) + " + val2 + " + (" + objx + ").substring(" + par2x + ");\n";
        return res; */ 
       
        return 
          ((BasicExpression) objectRef).updateFormEqJava7(
                                 env,newval2,newvar,local); 
      } 
      else if (type != null && 
               "Sequence".equals(type.getName()))
      { // objx := objx.subrange(0,par1x-1) ^ val2 ^
        //         objx.subrange(par2x + 1)

        Expression newvar = 
          new BinaryExpression("->union", rng1, 
            new BinaryExpression("->union", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormJava7(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEqJava7(
                                 env,newval2,newvar,local); 
      } 
    } 
                

    // System.out.println("#### " + this + " := " + val2); 
 
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't assign to: " + data + " */"; }
    
    if (umlkind == CLASSID && arrayIndex == null) 
    { if (val2.equals("{}") || 
          val2.equals("Set{}") || 
          val2.equals("Sequence{}"))  // delete the class extent
      { String datas = classExtentQueryFormJava7(env,local);  
        return "ArrayList<" + data + "> _" + data + " = new ArrayList<" + data + ">(); \n" + 
               "  _" + data + ".addAll(" + datas + "); \n" + 
               "  " + cont + ".killAll" + data + "(_" + data + ");\n"; 
      } 
      else 
      { return "{} /* can't assign to: " + this + " */"; }
    } 

    if (umlkind == VARIABLE)
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava7(env,local); 
        String wind = arrayIndex.wrap(indopt); 
        String wval = var.wrap(val2); 
        if ("String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
        { return data + ".put(" + wind + ", " + wval + ");"; }  // map[index] = val2 
        else 
        { return data + ".set((" + indopt + " -1), " + wval + ");"; }  
      }

      
      if (type != null && var.type == null)
      { String cstype = type.getJava7(); 
        return datax + " = (" + cstype + ") (" + val2 + ");"; 
      } 
      else if (type != null && var.type != null)
      { if (Type.isSpecialisedOrEqualType(var.type, type))
        { return "  " + datax + " = " + val2 + ";"; } 
        if ("String".equals(type.getName())) 
        { return "  " + datax + " = \"\" + " + val2 + ";"; }
        else if ("String".equals(var.type.getName()) &&
                 type.isNumeric())
        { String cname = Named.capitalise(type.getName()); 
          return "  " + datax + " = Ocl.to" + cname + "(" + val2 + ");"; 
        }
 
        String cstype = type.getJava7(); 
        return "  " + datax + " = (" + cstype + ") (" + val2 + ");"; 
      } 

      return data + " = " + val2 + ";"; 
    }

    // String nme = entity.getName();
    if (entity != null && entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null)
          { String ind = arrayIndex.queryFormJava7(env,local); 
            String wind = arrayIndex.wrap(ind); 
            String wval = var.wrap(val2); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") || 
                BasicExpression.isMapAccess(this))
            { return data + ".put(" + wind + ", " + wval + ");"; } 
            return data + ".set((" + ind + " - 1)," + wval + ");"; 
          }

          if (type != null && var.type == null)
          { String cstype = type.getJava7(); 
            return datax + " = (" + cstype + ") (" + val2 + ");"; 
          } 
          else if (type != null && var.type != null)
          { if (Type.isSpecialisedOrEqualType(var.type, type))
            { return "  " + datax + " = " + val2 + ";"; } 
            if ("String".equals(type.getName())) 
            { return "  " + datax + " = \"\" + " + val2 + ";"; }
            else if ("String".equals(var.type.getName()) &&
                     type.isNumeric())
            { String cname = Named.capitalise(type.getName()); 
              return "  " + datax + " = Ocl.to" + cname + "(" + val2 + ");"; 
            }

            String cstype = type.getJava7(); 
            return "  " + datax + " = (" + cstype + ") (" + val2 + ");"; 
          } 

          return data + " = " + val2 + ";";
        } 

        if (entity != null && entity.isClassScope(data))
        { String nme = entity.getName();
		  if (arrayIndex != null) 
          { String indopt = 
               arrayIndex.queryFormJava7(env,local); 
            // if ("String".equals(arrayIndex.type + "") || 
            //     BasicExpression.isMapAccess(this))
            return nme + ".set" + data + "((" + indopt + " -1), " + val2 + ");"; 
          } 
          return nme + ".set" + data + "(" + val2 + ");"; 
        }   // and with array
        
        String varx = findEntityVariable(env); 

        String target = ""; 
        if (varx.equals("this")) 
        { System.err.println("!! WARNING: using self with non-local update " + this);
          target = varx + ",";
        } 
        else 
        { target = varx + ","; } 
        
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava7(env,local); 
          if (isQualified() || 
              "String".equals(arrayIndex.type + "") || 
              BasicExpression.isMapAccess(this)
              )
          { return cont + ".set" + data + "(" + target + ind + ", " + val2 + ");"; } 
          String indopt = evaluateString("-",ind,"1"); // not for qualified
          return cont + ".set" + data + "(" + target + indopt + "," + val2 + ");";
        }

        if (type != null && var.type == null)
        { String cstype = type.getJava7(); 
          return cont + ".set" + data + "(" + target + " (" + cstype + ") (" + val2 + "));"; 
        } 
        else if (type != null && var.type != null)
        { if (Type.isSpecialisedOrEqualType(var.type, type))
          { return cont + ".set" + data + "(" + target + val2 + ");"; } 
          if ("String".equals(type.getName())) 
          { return cont + ".set" + data + "(" + target + "\"\" + " + val2 + ");"; }
          else if ("String".equals(var.type.getName()) &&
                     type.isNumeric())
          { String cname = Named.capitalise(type.getName()); 
            return cont + ".set" + data + "(" + target + " Ocl.to" + cname + "(" + val2 + "));"; 
          }
          String cstype = type.getJava7(); 
          return cont + ".set" + data + "(" + target + " (" + cstype + ") (" + val2 + "));"; 
        } 

        return cont + ".set" + data + "(" + target + val2 + ");"; 
      } // Really cont + ".set" ... in each case
    }
    else // objectRef != null
    { String pre = objectRef.queryFormJava7(env,local);

      if (entity != null && entity.isClassScope(data))
      { String nme = entity.getName();
        if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormJava7(env,local); 
          return nme + ".set" + data + "((" + indopt + " -1), " + val2 + ");"; 
        } 
        return nme + ".set" + data + "(" + val2 + ");"; 
      }   // and with array

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // C[inds].data = val2
          { return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // C[ind].data = val2 
          { return cont + ".set" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".setAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      if (entity != null && objectRef.isMultiple()) 
      { String nme = entity.getName();
	    String cref = nme; 
        if (entity.isInterface())
        { cref = nme + "." + nme + "Ops"; }
		 
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava7(env,local); 
          if (isQualified())
          { return cref + ".setAll" + data + "(" + pre + "," + ind + "," + 
                                val2 + ");";
          } 
          String indopt = evaluateString("-",ind,"1"); 
          return cref + ".setAll" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        return cref + ".setAll" + data + "(" + pre + ", " + val2 + ");";
      }
      else  // not objectRef.isMultiple()
      { if (local)
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormJava7(env,local); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") || 
                BasicExpression.isMapAccess(this)
               )
            { return pre + ".set" + data + "(" + ind + ", " + 
                                val2 + ");";
            } 
            String indopt = evaluateString("-",ind,"1"); 
            return pre + ".set" + data + "(" + indopt + ", " + val2 + ");";
          }
          return pre + ".set" + data + "(" + val2 + ");"; 
        }
        else if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava7(env,local);
          if (isQualified() ||
              "String".equals(arrayIndex.type + "") || 
              BasicExpression.isMapAccess(this)
             )
          { return cont + ".set" + data + "(" + pre + ", " + ind + ", " + val2 + ");"; }    
          String indopt = evaluateString("-",ind,"1"); 
          return cont + ".set" + data + "(" + pre + ", " + indopt + "," + 
                                val2 + ");";
        }

        if (type != null && var.type == null)
        { String cstype = type.getJava7(); 
          return cont + ".set" + data + "(" + pre + ", (" + cstype + ") (" + val2 + "));"; 
        } 
        else if (type != null && var.type != null)
        { if (Type.isSpecialisedOrEqualType(var.type, type))
          { return "    " + cont + ".set" + data + "(" + pre + ", " + val2 + ");"; } 
          if ("String".equals(type.getName())) 
          { return "    " + cont + ".set" + data + "(" + pre + ", \"\" + " + val2 + ");"; }
          else if ("String".equals(var.type.getName()) &&
                     type.isNumeric())
          { String cname = Named.capitalise(type.getName()); 
            return "    " + cont + ".set" + data + "(" + pre + ",  Ocl.to" + cname + "(" + val2 + "));"; 
          }
          String cstype = type.getJava7(); 
          return cont + ".set" + data + "(" + pre + ", (" + cstype + ") (" + val2 + "));"; 
        } 
 
        return cont + ".set" + data + "(" + pre + ", " + val2 + ");"; 
      }
    }

    return "{} /* unrecognised update: " + this + " = " + val2 + " */";
  }  // arrayIndex != null: setdata(var,arrayIndex,val) ?

  public String updateFormEqCSharp(java.util.Map env,
                             String val2, Expression var, boolean local)
  { String cont = "Controller.inst()"; 

    // System.out.println(">>> Assignment " + this + " = " + val2); 

    String datax = data;

    if (objectRef != null) 
    { datax = objectRef.queryFormCSharp(env,local) + "." + data; } 

    if ("subrange".equals(data) && parameters != null && 
        objectRef != null && 
        objectRef instanceof BasicExpression)
    { // obj.subrange(a,b) := val2 for strings is same as 
      // obj := obj.subrange(1,a-1) + val2 + obj.subrange(b+1)

      Expression par1 = (Expression) parameters.get(0); 
      Expression par2 = (Expression) parameters.get(1); 

      Vector pars1 = new Vector(); 
      pars1.add(new BasicExpression(1)); 
      pars1.add(new BinaryExpression("-", par1, 
                       new BasicExpression(1))); 

      Expression rng1 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, pars1); 
      rng1.setType(objectRef.getType()); 
      rng1.setElementType(objectRef.getElementType()); 


      Expression rng2 = 
         BasicExpression.newFunctionBasicExpression(
           "subrange", objectRef, 
              new BinaryExpression("+", par2, 
                       new BasicExpression(1))); 
      rng2.setType(objectRef.getType()); 
      rng2.setElementType(objectRef.getElementType()); 

    /*  String objx = objectRef.queryFormJava7(env,local);
      String lqf = ((BasicExpression) objectRef).leftQueryFormJava7(env,local);  
      String par1x = par1.queryFormJava7(env,local); 
      String par2x = par2.queryFormJava7(env,local); */ 

      if (type != null && "String".equals(type.getName()))
      { // objx := objx.subrange(1,par1x-1) + val2 +
        //         objx.subrange(par2x+1)

        Expression newvar = 
          new BinaryExpression("+", rng1, 
            new BinaryExpression("+", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormCSharp(env,local); 
           
       /* String res = lqf + " = (" + objx + ").substring(0," + par1x + "-1) + " + val2 + " + (" + objx + ").substring(" + par2x + ");\n";
        return res; */ 
       
        return 
          ((BasicExpression) objectRef).updateFormEqCSharp(
                                 env,newval2,newvar,local); 
      } 
      else if (type != null && 
               "Sequence".equals(type.getName()))
      { // objx := objx.subrange(0,par1x-1) ^ val2 ^
        //         objx.subrange(par2x + 1)

        Expression newvar = 
          new BinaryExpression("->union", rng1, 
            new BinaryExpression("->union", var, rng2)); 
        newvar.setType(objectRef.getType()); 
        newvar.setElementType(objectRef.getElementType()); 

        String newval2 = newvar.queryFormCSharp(env,local); 
                  
        return 
          ((BasicExpression) objectRef).updateFormEqCSharp(
                                 env,newval2,newvar,local); 
      } 
    } 
                


    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "  {} /* can't assign to: " + data + " */"; }
    
    if (umlkind == CLASSID && arrayIndex == null) 
    { if (val2.equals("{}") || val2.equals("Set{}") || val2.equals("Sequence{}"))  // delete the class extent
      { String datas = classExtentQueryFormCSharp(env,local);  
        return "ArrayList _" + data + " = new ArrayList(); \n" + 
               "  _" + data + ".AddRange(" + datas + "); \n" + 
               "  " + cont + ".killAll" + data + "(_" + data + ");\n"; 
      } 
      else 
      { return "  {} /* can't assign to: " + data + " */"; }
    } 

    if (umlkind == VARIABLE) // assume objectRef == null
    { // System.out.println(">>> Variable " + this); 

      if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCSharp(env,local); 
        String wind = arrayIndex.wrapCSharp(indopt); 
        String wval = var.wrapCSharp(val2); 
        if ("String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
        { return datax + "[" + wind + "] = " + wval + ";"; }  // map[index] = val2 
        else 
        { return datax + "[" + indopt + " -1] = " + wval + ";"; } 
      }

      if (type != null && var.type == null)
      { String cstype = type.getCSharp(); 
        return datax + " = (" + cstype + ") (" + val2 + ");"; 
      } 
      else if (type != null && var.type != null)
      { if (Type.isSpecialisedOrEqualType(var.type, type))
        { return "  " + datax + " = " + val2 + ";"; }
        else if ("String".equals(type.getName())) 
        { return "  " + datax + " = \"\" + " + val2 + ";"; }
        else if ("String".equals(var.type.getName()) &&
                 type.isNumeric())
        { String cname = Named.capitalise(type.getName()); 
          return "  " + datax + " = SystemTypes.to" + cname + "(" + val2 + ");"; 
        }

        String cstype = type.getCSharp(); 
        return "  " + datax + " = (" + cstype + ") (" + val2 + ");"; 
      } 

      return "  " + datax + " = " + val2 + ";"; 
    }

    String nme = entity.getName();
    if (entity.isBidirectionalRole(data))
    { local = false; }

    // System.out.println(">> " + this + " objectref = " + objectRef);  
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null)
          { String ind = arrayIndex.queryFormCSharp(env,local); 
            String wind = arrayIndex.wrapCSharp(ind); 
            String wval = var.wrapCSharp(val2); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return data + "[" + wind + "] = " + wval + ";"; } 
            return data + "[" + ind + " - 1] = " + val2 + ";"; 
          }

          if (type != null && var.type == null)
          { String cstype = type.getCSharp(); 
            return data + " = (" + cstype + ") (" + val2 + ");"; 
          } 
          else if (type != null && var.type != null)
          { if (Type.isSpecialisedOrEqualType(var.type, type))
            { return data + " = " + val2 + ";"; } 
            else if ("String".equals(type.getName())) 
            { return "  " + datax + " = \"\" + " + val2 + ";"; }
            else if ("String".equals(var.type.getName()) &&
                     type.isNumeric())
            { String cname = Named.capitalise(type.getName()); 
              return "  " + datax + " = SystemTypes.to" + cname + "(" + val2 + ");"; 
            }   
            String cstype = type.getCSharp(); 
            return data + " = (" + cstype + ") (" + val2 + ");"; 
          } 

          return data + " = " + val2 + ";";
        } 

        if (entity.isClassScope(data))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormCSharp(env,local); 
            return nme + ".set" + data + "((" + indopt + " -1), " + val2 + ");"; 
          }
          return nme + ".set" + data + "(" + val2 + ");"; 
        }   // and with array
        
        String varx = findEntityVariable(env); 

        String target = ""; 
        if (varx.equals("this")) 
        { System.err.println("!! WARNING: using self with non-local update " + this);
          target = varx + ",";
        } 
        else 
        { target = varx + ","; } 
        
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCSharp(env,local); 
          if (isQualified() ||
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cont + ".set" + data + "(" + target + ind + ", " + val2 + ");"; } 
          String indopt = evaluateString("-",ind,"1"); // not for qualified
          return cont + ".set" + data + "(" + target + indopt + "," + val2 + ");";
        }

        if (type != null && var.type == null)
        { String cstype = type.getCSharp(); 
          return cont + ".set" + data + "(" + target + " (" + cstype + ") (" + val2 + "));"; 
        } 
        else if (type != null && var.type != null)
        { if (Type.isSpecialisedOrEqualType(var.type, type))
          { return "    " + cont + ".set" + data + "(" + target + val2 + ");"; } 
          else if ("String".equals(type.getName())) 
          { return "    " + cont + ".set" + data + "(" + target + "\"\" + " + val2 + ");"; }
          else if ("String".equals(var.type.getName()) &&
                   type.isNumeric())
          { String cname = Named.capitalise(type.getName()); 
            return "    " + cont + ".set" + data + "(" + target + "SystemTypes.to" + cname + "(" + val2 + "));"; 
          }
          String cstype = type.getCSharp(); 
          return "    " + cont + ".set" + data + "(" + target + " (" + cstype + ") (" + val2 + "));"; 
        } 

        return cont + ".set" + data + "(" + target + val2 + ");"; 
      } // Really cont + ".set" ... in each case
    }
    else // objectRef != null
    { String pre = objectRef.queryFormCSharp(env,local);

      // System.out.println("**** Controller.inst().set" + data + "(" + pre + ", " + val2 + ")"); 

      if (entity != null && entity.isClassScope(data))
      { if (arrayIndex != null) 
        { String indopt = 
             arrayIndex.queryFormCSharp(env,local); 
          return nme + ".set" + data + "((" + indopt + " -1), " + val2 + ");"; 
        }
        return nme + ".set" + data + "(" + val2 + ");"; 
      }   // and with array

      if (objectRef instanceof UnaryExpression && 
          "!".equals(((UnaryExpression) objectRef).operator))
      { return "(" + pre + ")." + data + " = " + val2 + ";"; } 

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // C[inds].data = val2
          { return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // C[ind].data = val2 
          { return cont + ".set" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "_s";  
        return cref + ".setAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      if (objectRef.isMultiple()) 
      { String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "Ops"; } 
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCSharp(env,local); 
          if (isQualified() ||
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cref + ".setAll" + data + "(" + pre + "," + ind + "," + 
                                val2 + ");";
          } 
          String indopt = evaluateString("-",ind,"1"); 
          return cref + ".setAll" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        return cref + ".setAll" + data + "(" + pre + "," + val2 + ");"; 
      }
      else
      { if (local)
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormCSharp(env,local); 
            if (isQualified() ||
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return pre + ".set" + data + "(" + ind + "," + 
                                val2 + ");";
            } 
            String indopt = evaluateString("-",ind,"1"); 
            return pre + ".set" + data + "(" + indopt + "," + val2 + ");";
          }
          return pre + ".set" + data + "(" + val2 + ");"; 
        }
        else if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCSharp(env,local);
          if (isQualified() ||
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cont + ".set" + data + "(" + pre + "," + ind + "," + val2 + ");"; }    
          String indopt = evaluateString("-",ind,"1"); 
          return cont + ".set" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }

        if (type != null && var.type == null)
        { String cstype = type.getCSharp(); 
          return cont + ".set" + data + "(" + pre + ", (" + cstype + ") (" + val2 + "));"; 
        } 
        else if (type != null && var.type != null)
        { if (Type.isSpecialisedOrEqualType(var.type, type))
          { return "    " + cont + ".set" + data + "(" + pre + ", " + val2 + ");"; } 
          else if ("String".equals(type.getName())) 
          { return "    " + cont + ".set" + data + "(" + pre + ", \"\" + " + val2 + ");"; }
          else if ("String".equals(var.type.getName()) &&
                   type.isNumeric())
          { String cname = Named.capitalise(type.getName()); 
            return "    " + cont + ".set" + data + "(" + pre + ", SystemTypes.to" + cname + "(" + val2 + "));"; 
          }
          String cstype = type.getCSharp(); 
          return cont + ".set" + data + "(" + pre + ", (" + cstype + ") (" + val2 + "));"; 
        } 

        return cont + ".set" + data + "(" + pre + "," + val2 + ");"; 
      }
      // return cont + ".set" + data + "(" + pre + "," + val2 + ");"; 
    }

    return "  {} /* unrecognised update: " + this + " = " + val2 + " */";
  }  // arrayIndex != null: setdata(var,arrayIndex,val) ?

  public String updateFormEqCPP(java.util.Map env,
                             String val2, boolean local)
  { String cont = "Controller::inst->"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't assign to: " + data + " */"; }

    System.out.println("### Assignment: " + this + " := " + val2); 
    System.out.println("### " + this.umlkind + ", " + this.type); 

    String datax = data;
    if (objectRef != null) 
    { datax = objectRef.queryFormCPP(env,local) + "." + data; } 

    String cetype = "void*";     
    Type et = getElementType(); 
    if (et != null)
    { cetype = et.getCPP(et.getElementType()); } 

    if (umlkind == CLASSID && arrayIndex == null) 
    { if (val2.equals("{}") || val2.equals("Set{}") || val2.equals("Sequence{}"))  // delete the class extent
      { String datas = classExtentQueryFormCPP(env,local);  
        return "vector<" + cetype + ">* _" + data + " = new vector<" + cetype + ">(); \n" + 
               "  _" + data + "->insert(_" + data + "->end(), " + datas + "->begin(), " +
               datas + "->end()); \n" + 
               "  " + cont + "killAll" + data + "(_" + data + ");\n"; 
      } 
      else 
      { return "{} /* can't assign to: " + data + " */"; }
    } 

    if (umlkind == VARIABLE)
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCPP(env,local); 
        if ("String".equals(arrayIndex.type + "") ||
            BasicExpression.isMapAccess(this))
        { return "(*" + datax + ")[" + indopt + "] = " + val2 + ";"; }  // map[index] = val2 
        else 
        { return "(*" + datax + ")[" + indopt + " -1] = " + val2 + ";"; }  
      }

      /* if (type != null && var.type == null)
      { String cstype = type.getCPP(); 
        return datax + " = (" + cstype + ") (" + val2 + ");"; 
      } 
      else if (type != null && var.type != null)
      { if (Type.isSpecialisedOrEqualType(var.type, type))
        { return "  " + datax + " = " + val2 + ";"; } 
        String cstype = type.getCPP(); 
        return "  " + datax + " = (" + cstype + ") (" + val2 + ");"; 
      } */ 

      return datax + " = " + val2 + ";"; 
    }

    String nme = entity.getName();
    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (local) 
        { if (arrayIndex != null)
          { String ind = arrayIndex.queryFormCPP(env,local); 
            if (isQualified() || 
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return "(*" + data + ")[" + ind + "] = " + val2 + ";"; } 
            return "(*" + data + ")[" + ind + " - 1] = " + val2 + ";"; 
          }
          return data + " = " + val2 + ";";
        } 

        if (entity != null && entity.isClassScope(data))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormCPP(env,local); 
            return nme + "::set" + data + "((" + indopt + " -1), " + val2 + ");"; 
          }
          return nme + "::set" + data + "(" + val2 + ");"; 
        }   // and with array
        
        String var = findEntityVariable(env); 

        String target = ""; 
        if (var.equals("this")) 
        { System.err.println("WARNING: using self with non-local update " + this);
          target = var + ",";
        } 
        else 
        { target = var + ","; } 
        
        if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCPP(env,local); 
          if (isQualified() ||
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cont + "set" + data + "(" + target + ind + ", " + val2 + ");"; } 
          String indopt = evaluateString("-",ind,"1"); // not for qualified
          return cont + "set" + data + "(" + target + indopt + "," + val2 + ");";
        }
        return cont + "set" + data + "(" + target + val2 + ");"; 
      } // Really cont + ".set" ... in each case
    }
    else // objectRef != null
    { String pre = objectRef.queryFormCPP(env,local);

      if (entity != null && entity.isClassScope(data))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormCPP(env,local); 
          return nme + "::set" + data + "((" + indopt + " -1), " + val2 + ");"; 
        }
        return nme + "::set" + data + "(" + val2 + ");"; 
      }   // and with array

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // C[inds].data = val2
          { return cname + "::setAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // C[ind].data = val2 
          { return cont + "set" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "get" + cname.toLowerCase() + "_s()";  
        return cname + "::setAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      if (objectRef.isMultiple()) 
      { if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCPP(env,local); 
          if (isQualified() || 
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return nme + "::setAll" + data + "(" + pre + "," + ind + "," + 
                              val2 + ");";
          } 
          String indopt = evaluateString("-",ind,"1"); 
          return nme + "::setAll" + data + "(" + pre + "," + indopt + "," + 
                              val2 + ");";
        }
        return nme + "::setAll" + data + "(" + pre + "," + val2 + ");"; 
      }
      else
      { if (local)
        { if (arrayIndex != null) 
          { String ind = arrayIndex.queryFormCPP(env,local); 
            if (isQualified() ||
                "String".equals(arrayIndex.type + "") ||
                BasicExpression.isMapAccess(this))
            { return pre + "->set" + data + "(" + ind + "," + val2 + ");"; } 
            String indopt = evaluateString("-",ind,"1"); 
            return pre + "->set" + data + "(" + indopt + "," + val2 + ");";
          }
          return pre + "->set" + data + "(" + val2 + ");"; 
        }
        else if (arrayIndex != null) 
        { String ind = arrayIndex.queryFormCPP(env,local);
          if (isQualified() || 
              "String".equals(arrayIndex.type + "") ||
              BasicExpression.isMapAccess(this))
          { return cont + "set" + data + "(" + pre + "," + ind + "," + val2 + ");"; }    
          String indopt = evaluateString("-",ind,"1"); 
          return cont + "set" + data + "(" + pre + "," + indopt + "," + 
                                val2 + ");";
        }
        return cont + "set" + data + "(" + pre + "," + val2 + ");"; 
      }
    }
    return "{} /* unrecognised update: " + this + " = " + val2 + " */";
  }  // arrayIndex != null: setdata(var,arrayIndex,val) ?


  private String updateFormIn(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    String nme = ""; 
    if (entity != null)
    { nme = entity.getName(); }
     
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == CLASSID && arrayIndex == null)  // but no arrayIndex
    { if (local && entity.hasFeature(val2)) // or is parameter of the op
      { return val2 + " = new " + data + "();"; }   
      else if (entity.hasStereotype("external"))
      { return data + " " + val2 + " = new " + data + "();"; }
      else 
      { // return data + " " + val2 + " = Controller.inst().create" + data + "();"; 
        return data + " " + val2 + " = new " + data + "();\n    " + 
               cont + ".add" + data + "(" + val2 + ");";
      }  // also check completeness of post in such cases
    }  

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryForm(env,local); 
        return "((List) " + data + ".get(" + indopt + " -1)).add(" + val2 + ");"; 
      }
      return data + ".add(" + val2 + ");"; 
    }   // wrap val2
    // case with arrayIndex:  ((List) data.get(ind - 1)).add(val2)

    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { return data + ".add(" + val2 + ");"; }
        else 
        { return "{} /* can't add to single-valued attribute: " + this + " */"; }
      } 

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 
        String qual = ""; 

        if (isQualified() && arrayIndex != null)
        { qual = arrayIndex.queryForm(env,local) + ", "; 
          if (local) 
          { return var + ".add" + data + "(" + qual + val2 + ");"; } 
          else 
          { return cont + ".add" + data + "(" + var + "," + qual + val2 + ");"; }
        }

        if (local) 
        { return data + ".add(" + val2 + ");"; }  
        // unordered and zero-one roles: check not in first

        return cont + ".add" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be an object
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryForm(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 : C[inds].data
          { return cref + ".addAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 : C[ind].data 
          { return cont + ".add" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".addAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      String qual = ""; 
      if (isQualified() && arrayIndex != null)
      { qual = arrayIndex.queryForm(env,local) + ", "; } 

      if (objectRef.isMultiple())
      { String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "." + nme + "Ops"; } 
        return cref + ".addAll" + data + "(" + pre + "," + qual + val2 + ");"; 
      } // use Set.union if unordered
      else
      { return cont + ".add" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised add: " + val2 + " : " + this + " */";
  }  // Entity: generate unionAll etc ops

  private String updateFormInJava6(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    String nme = ""; 
    if (entity != null)
    { nme = entity.getName(); }
     
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == CLASSID && arrayIndex == null)
    { if (local && entity.hasFeature(val2)) // or is parameter of the op
      { return val2 + " = new " + data + "();"; }   
      else if (entity.hasStereotype("external"))
      { return data + " " + val2 + " = new " + data + "();"; }
      else 
      { // return data + " " + val2 + " = Controller.inst().create" + data + "();"; 
        return data + " " + val2 + " = new " + data + "();\n    " + 
               cont + ".add" + data + "(" + val2 + ");";
      }  // also check completeness of post in such cases
    }  

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava6(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).add(" + val2 + ");"; 
      }
      return data + ".add(" + val2 + ");"; 
    }   // wrap val2

    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { return data + ".add(" + val2 + ");"; }
        else 
        { return "{} /* can't add to single-valued attribute */"; }
      }
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role */"; }

        String var = findEntityVariable(env); 
        String qual = ""; 

        if (isQualified() && arrayIndex != null)
        { qual = arrayIndex.queryFormJava6(env,local) + ", "; 
          if (local) 
          { return var + ".add" + data + "(" + qual + val2 + ");"; } 
          else 
          { return cont + ".add" + data + "(" + var + "," + qual + val2 + ");"; }
        }

        if (local) 
        { return data + ".add(" + val2 + ");"; }  
        // unordered roles: check not in first

        return cont + ".add" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be an object
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormJava6(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 : C[inds].data
          { return cref + ".addAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 : C[ind].data 
          { return cont + ".add" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".addAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      String qual = ""; 
      if (isQualified() && arrayIndex != null)
      { qual = arrayIndex.queryFormJava6(env,local) + ", "; } 

      if (objectRef.isMultiple())
      { String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "." + nme + "Ops"; } 
        return cref + ".addAll" + data + "(" + pre + "," + qual + val2 + ");"; 
      } // use Set.union if unordered
      else
      { return cont + ".add" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised add: " + val2 + " : " + this + " */";
  }  // Entity: generate unionAll etc ops

  private String updateFormInJava7(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    String nme = ""; 
    if (entity != null)
    { nme = entity.getName(); }
     
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == CLASSID && arrayIndex == null)
    { if (local && entity.hasFeature(val2)) // or is parameter of the op
      { return val2 + " = new " + data + "();"; }   
      else if (entity.hasStereotype("external"))
      { return data + " " + val2 + " = new " + data + "();"; }
      else 
      { // return data + " " + val2 + " = Controller.inst().create" + data + "();"; 
        return data + " " + val2 + " = new " + data + "();\n    " + 
               cont + ".add" + data + "(" + val2 + ");";
      }  // also check completeness of post in such cases
    }  

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava7(env,local); 
        String j7type = elementType.getJava7();          
        return "((" + j7type + ") " + data + ".get(" + indopt + " -1)).add(" + val2 + ");"; 
      }
      return data + ".add(" + val2 + ");"; 
    }   // wrap val2

    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { return data + ".add(" + val2 + ");"; }
        else 
        { return "{} /* can't add to single-valued attribute */"; }
      }
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role */"; }

        String var = findEntityVariable(env); 
        String qual = ""; 

        if (isQualified() && arrayIndex != null)
        { qual = arrayIndex.queryFormJava7(env,local) + ", "; 
          if (local) 
          { return var + ".add" + data + "(" + qual + val2 + ");"; } 
          else 
          { return cont + ".add" + data + "(" + var + "," + qual + val2 + ");"; }
        }

        if (local) 
        { return data + ".add(" + val2 + ");"; }  
        // unordered roles: check not in first

        return cont + ".add" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be an object
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormJava7(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 : C[inds].data
          { return cref + ".addAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 : C[ind].data 
          { return cont + ".add" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".addAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      String qual = ""; 
      if (isQualified() && arrayIndex != null)
      { qual = arrayIndex.queryFormJava7(env,local) + ", "; } 

      if (objectRef.isMultiple())
      { String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "." + nme + "Ops"; } 
        return cref + ".addAll" + data + "(" + pre + "," + qual + val2 + ");"; 
      } // use Set.union if unordered
      else
      { return cont + ".add" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised add: " + val2 + " : " + this + " */";
  }  // Entity: generate unionAll etc ops

  private String updateFormInCSharp(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    String nme = ""; 
    if (entity != null)
    { nme = entity.getName(); }
     
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == CLASSID && arrayIndex == null)
    { if (local && entity.hasFeature(val2)) // or is parameter of the op
      { return val2 + " = new " + data + "();"; }   
      else if (entity.hasStereotype("external"))
      { return data + " " + val2 + " = new " + data + "();"; }
      else 
      { // return data + " " + val2 + " = Controller.inst().create" + data + "();"; 
        return data + " " + val2 + " = new " + data + "();\n    " + 
               cont + ".add" + data + "(" + val2 + ");";
      }  // also check completeness of post in such cases
    }  

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCSharp(env,local); 
        return "((ArrayList) " + data + "[" + indopt + " -1]).Add(" + val2 + ");"; 
      }
      return data + ".Add(" + val2 + ");"; 
    }   // wrap val2

    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { return data + ".Add(" + val2 + ");"; }  
        else 
        { return "{} /* can't add to single-valued attribute */"; }
      } 
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 
        String qual = ""; 

        if (isQualified() && arrayIndex != null)
        { qual = arrayIndex.queryFormCSharp(env,local) + ", "; 
          if (local) 
          { return var + ".add" + data + "(" + qual + val2 + ");"; } 
          else 
          { return cont + ".add" + data + "(" + var + "," + qual + val2 + ");"; }
        }

        if (local) 
        { return data + ".Add(" + val2 + ");"; }  
        // unordered roles: check not in first

        return cont + ".add" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be an object
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormCSharp(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "Ops"; } 
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 : C[inds].data
          { return cref + ".addAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 : C[ind].data 
          { return cont + ".add" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "_s";  
        return cref + ".addAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      String qual = ""; 
      if (isQualified() && arrayIndex != null)
      { qual = arrayIndex.queryFormCSharp(env,local) + ", "; } 

      if (objectRef.isMultiple())
      { String cref = nme; 
        if (entity != null && entity.isInterface())
        { cref = nme + "Ops"; } 
        return cref + ".addAll" + data + "(" + pre + "," + qual + val2 + ");"; 
      } // use Set.union if unordered
      else
      { return cont + ".add" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised add: " + val2 + " : " + this + " */";
  }  // Entity: generate unionAll etc ops

  private String updateFormInCPP(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller::inst->"; 

    String nme = ""; 
    if (entity != null)
    { nme = entity.getName(); }
     
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == CLASSID && arrayIndex == null)
    { if (local && entity.hasFeature(val2)) // or is parameter of the op
      { return val2 + " = new " + data + "();"; }   
      else if (entity.hasStereotype("external"))
      { return data + "* " + val2 + " = new " + data + "();"; }
      else 
      { return data + "* " + val2 + " = new " + data + "();\n    " + 
               cont + "add" + data + "(" + val2 + ");";
      }  // also check completeness of post in such cases
    }  

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { String pre = data; 

      if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCPP(env,local); 
        pre = "((*" + data + ")[" + indopt + " -1])"; 
      }

      if (isOrdered() || isSequenceValued()) 
      { return "((vector<void*>) " + pre + ")->push_back(" + val2 + ");"; }   
      else 
      { return "((set<void*>) " + pre + ")->insert(" + val2 + ");"; }   
    }  

    if (entity.isBidirectionalRole(data))
    { local = false; } 
    
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type)) 
        { if (isOrdered() || isSequenceValued()) 
          { return data + "->push_back(" + val2 + ");"; }  
          else 
          { return data + "->insert(" + val2 + ");"; } 
        } 
        else 
        { return "{} /* can't add to single-valued attribute */"; }
      } 

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 
        String qual = ""; 

        if (isQualified() && arrayIndex != null)
        { qual = arrayIndex.queryFormCPP(env,local) + ", "; 
          if (local) 
          { return var + "->add" + data + "(" + qual + val2 + ");"; } 
          else 
          { return cont + "add" + data + "(" + var + "," + qual + val2 + ");"; }
        }

        if (local) 
        { if (isOrdered() || isSequenceValued()) 
          { return data + "->push_back(" + val2 + ");"; }  
          else 
          { return data + "->insert(" + val2 + ");"; } 
        } 
        return cont + "add" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be an object
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormCPP(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 : C[inds].data
          { return cname + "::addAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 : C[ind].data 
          { return cont + "add" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "get" + cname.toLowerCase() + "_s()";  
        return cname + "::addAll" + data + "(" + cobjs + "," + val2 + ");"; 
      } 

      String qual = ""; 
      if (isQualified() && arrayIndex != null)
      { qual = arrayIndex.queryFormCPP(env,local) + ", "; } 

      if (objectRef.isMultiple())
      { return nme + "::addAll" + data + "(" + pre + "," + qual + val2 + ");"; } 
      // use Set.union if unordered -- implement addAll this way (Entity.java)
      else
      { return cont + "add" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised add: " + val2 + " : " + this + " */";
  }  // Entity: generate unionAll etc ops

  private String updateFormNotin(java.util.Map env,
                                 String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't remove from: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryForm(env,local); 
        return "((List) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
      }
      return data + ".remove(" + val2 + ");";
    }
    // wrap val2 if primitive; really removeAll

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".kill" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    String qual = ""; 
    if (isQualified() && arrayIndex != null)
    { qual = arrayIndex.queryForm(env,local) + ", "; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryForm(env,local); 
            return "((List) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
          }
          return data + ".remove(" + val2 + ");"; 
        }
        else 
        { return "{} /* can't remove from single-valued attribute: " + this + " */"; } 
      }

      if (umlkind == ROLE) // && not addOnly
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't remove from ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (local) 
        { return var + ".remove" + data + "(" + qual + val2 + ");"; }  // Remove all copies 
                
        return cont + ".remove" + data + "(" + var + "," + qual + val2 + ");";
      }
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemove(env,dataform,exp2,local,"="); 
      } 
      
      String pre = objectRef.queryForm(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 

        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 /: C[inds].data
          { return cref + ".removeAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 /: C[ind].data  
          { return cont + ".remove" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".removeAll" + data + "(" + cobjs + "," + val2 + ");"; 
      }
 
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".removeAll" + data + "(" + pre + "," + qual + val2 + ");";
      }
      else
      { return cont + ".remove" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised remove: " + this + "->excludes(" + val2 + ") */";
  }

  private String updateFormNotinJava6(java.util.Map env,
                                 String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't remove from: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava6(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
      }
      return data + ".remove(" + val2 + ");"; 
    }
      // wrap val2 if primitive; really removeAll

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".kill" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    String qual = ""; 
    if (isQualified() && arrayIndex != null)
    { qual = arrayIndex.queryFormJava6(env,local) + ", "; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormJava6(env,local); 
            return "((Collection) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
          }
          return data + ".remove(" + val2 + ");"; 
        }
        else 
        { return "{} /* can't remove from single-valued attribute: " + this + " */"; } 
      }

      if (umlkind == ROLE)  // && not addOnly
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't remove from ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (local) 
        { return var + ".remove" + data + "(" + qual + val2 + ");"; }  // Remove all copies 
                
        return cont + ".remove" + data + "(" + var + "," + qual + val2 + ");";
      }
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveJava6(env,dataform,exp2,local,"="); 
      } 
      
      String pre = objectRef.queryFormJava6(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 /: C[inds].data
          { return cref + ".removeAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 /: C[ind].data  
          { return cont + ".remove" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".removeAll" + data + "(" + cobjs + "," + val2 + ");"; 
      }
 
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String cref = ename; 
        if (entity.isInterface())
        { cref = ename + "." + ename + "Ops"; } 
        return cref + ".removeAll" + data + "(" + pre + "," + qual + val2 + ");";
      }
      else
      { return cont + ".remove" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised remove: " + this + "->excludes( " + val2 + ") */";
  }

  private String updateFormNotinJava7(java.util.Map env,
                                 String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't remove from: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava7(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
      }
      return data + ".remove(" + val2 + ");"; 
    }
      // wrap val2 if primitive; really removeAll

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".kill" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    String qual = ""; 
    if (isQualified() && arrayIndex != null)
    { qual = arrayIndex.queryFormJava7(env,local) + ", "; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormJava7(env,local); 
            return "((Collection) " + data + ".get(" + indopt + " -1)).remove(" + val2 + ");"; 
          }
          return data + ".remove(" + val2 + ");"; 
        }
        else 
        { return "{} /* can't remove from single-valued attribute: " + this + " */"; } 
      }

      if (umlkind == ROLE)  // && not addOnly
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't remove from ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (local) 
        { return var + ".remove" + data + "(" + qual + val2 + ");"; }  // Remove all copies 
                
        return cont + ".remove" + data + "(" + var + "," + qual + val2 + ");";
      }
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveJava7(env,dataform,exp2,local,"="); 
      } 
      
      String pre = objectRef.queryFormJava7(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "." + cname + "Ops"; } 
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 /: C[inds].data
          { return cref + ".removeAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 /: C[ind].data  
          { return cont + ".remove" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "s";  
        return cref + ".removeAll" + data + "(" + cobjs + "," + val2 + ");"; 
      }
 
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String cref = ename; 
        if (entity.isInterface())
        { cref = ename + "." + ename + "Ops"; } 
        return cref + ".removeAll" + data + "(" + pre + "," + qual + val2 + ");";
      }
      else
      { return cont + ".remove" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised remove: " + this + "->excludes( " + val2 + ") */";
  }

  private String updateFormNotinCSharp(java.util.Map env,
                                 String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't remove from: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCSharp(env,local); 
        return "((ArrayList) " + data + "[" + indopt + " -1]).Remove(" + val2 + ");"; 
      }
      return data + ".Remove(" + val2 + ");"; 
    }
      // wrap val2 if primitive; really removeAll

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".kill" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    String qual = ""; 
    if (isQualified() && arrayIndex != null)
    { qual = arrayIndex.queryFormCSharp(env,local) + ", "; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormCSharp(env,local); 
            return "((ArrayList) " + data + "[" + indopt + " -1]).Remove(" + val2 + ");"; 
          }
          return data + ".Remove(" + val2 + ");"; 
        }
        else 
        { return "{} /* can't remove from single-valued attribute: " + this + " */"; } 
      }
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't remove from ONE role */"; }

        String var = findEntityVariable(env); 

        if (local) 
        { return var + ".remove" + data + "(" + qual + val2 + ");"; }  // Remove all copies 
                
        return cont + ".remove" + data + "(" + var + "," + qual + val2 + ");";
      }
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveCSharp(env,dataform,exp2,local,"="); 
      } 
      
      String pre = objectRef.queryFormCSharp(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        String cref = cname; 
        if (objref.entity != null && objref.entity.isInterface())
        { cref = cname + "Ops"; } 
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 /: C[inds].data
          { return cref + ".removeAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 /: C[ind].data  
          { return cont + ".remove" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "." + cname.toLowerCase() + "_s";  
        return cref + ".removeAll" + data + "(" + cobjs + "," + val2 + ");"; 
      }
 
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String cref = ename; 
        if (entity.isInterface())
        { cref = ename + "Ops"; } 
        return cref + ".removeAll" + data + "(" + pre + "," + qual + val2 + ");";
      }
      else
      { return cont + ".remove" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised remove: " + this + "->excludes(" + val2 + ") */";
  }

  private String updateFormNotinCPP(java.util.Map env,
                                 String val2, Expression exp2, boolean local)
  { String cont = "Controller::inst->"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't remove from: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCPP(env,local);
        String coll = "set"; 
        if (isOrdered() || isSequenceValued()) 
        { coll = "vector"; } 
        return "(" + data + "->at(" + indopt + " -1))->erase(" + val2 + ");"; 
      }
      return data + "->erase(" + val2 + ");"; 
    }
      // wrap val2 if primitive; really removeAll

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + "kill" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    String qual = ""; 
    if (isQualified() && arrayIndex != null)
    { qual = arrayIndex.queryFormCPP(env,local) + ", "; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormCPP(env,local);
            String coll = "set"; 
            if (isOrdered() || isSequenceValued()) 
            { coll = "vector"; 
              return "(" + data + "->at(" + indopt + " -1)->erase(find(" + data + "->at(" + indopt + " -1)->begin(), " + data + "->at(" + indopt + " -1)->end(), " + val2 + ")));"; 
            } 
            return "(" + data + "->at(" + indopt + " -1))->erase(" + val2 + ");"; 
          }

          if (isOrdered() || isSequenceValued())
          { return 
              data + "->erase(find(" + data + "->begin(), " + data + "->end(), " + val2 + "));"; 
          } 
          return data + "->erase(" + val2 + ");"; 
        }
        else 
        { return "{} /* can't remove from single-valued attribute: " + this + " */"; } 
      }

      if (umlkind == ROLE) // and not addOnly
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't remove from ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (local) 
        { return var + "->remove" + data + "(" + qual + val2 + ");"; }  // Remove all copies 
                
        return cont + "remove" + data + "(" + var + "," + qual + val2 + ");";
      }
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveCPP(env,dataform,exp2,local,"="); 
      } 
      
      String pre = objectRef.queryFormCPP(env,local);

      if (objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression)) 
      { BasicExpression objref = (BasicExpression) objectRef; 
        String cname = objref.data;
        if (objref.arrayIndex != null) 
        { if (objref.arrayIndex.isMultiple())   // val2 /: C[inds].data
          { return cname + "::removeAll" + data + "(" + pre + "," + val2 + ");"; } 
          else    // val2 /: C[ind].data  
          { return cont + "remove" + data + "(" + pre + "," + val2 + ");"; } 
        } 
        String cobjs = cont + "get" + cname.toLowerCase() + "_s()";  
        return cname + "::removeAll" + data + "(" + cobjs + "," + val2 + ");"; 
      }
 
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        return ename + "::removeAll" + data + "(" + pre + "," + qual + val2 + ");";
      }
      else
      { return cont + "remove" + data + "(" + pre + "," + qual + val2 + ");"; }
    }
    return "{} /* unrecognised remove: " + this + "->excludes(" + val2 + ") */";
  }

  private String updateFormRemove(java.util.Map env, BasicExpression dataform,
                                  Expression exp2, boolean local, String op)
  { String cont = "Controller.inst()"; 

    if (objectRef == null)
    { Expression selarg = new BinaryExpression(op,dataform,exp2);
      Expression selexp = 
          new BinaryExpression("->select",this,selarg);
      String rej = selexp.queryForm(env,local);
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { if (entity.isBidirectionalRole(data))
        { local = false; } 

        if (local)
        { return "subtract" + data + "(" + rej + ");"; }
        else
        { String var = findEntityVariable(env); 
          return cont + ".subtract" + data + "(" + var + ", " + rej + ");"; 
        }
      }
      else if (umlkind == CLASSID)  // eg: exp2 /: C.dataform
      { return cont + ".killAll" + data + "(" + rej + ");"; } 
      else 
      { return "{} /* Invalid subtract */"; }
    }
    else
    { if (umlkind == ROLE && multiplicity == ModelElement.ONE || 
          umlkind == ATTRIBUTE )
      { BasicExpression newdf;
        BasicExpression dataexp = (BasicExpression) clone();
        dataexp.objectRef = null;
        if (dataform.objectRef == null)
        { newdf = dataform;
          newdf.objectRef = dataexp;
        }
        else
        { BasicExpression dor = (BasicExpression) dataform.objectRef;
          dor.setInnerObjectRef(dataexp); 
          // dor.objectRef = dataexp;
          newdf = dataform;
        }
        return 
          ((BasicExpression) objectRef).updateFormRemove(env,newdf,exp2,local,op);
      } 
      else if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { String pre = objectRef.queryForm(env,local);
        Expression selarg = new BinaryExpression(op,dataform,exp2);
        Expression selexp = 
          new BinaryExpression("->select",this,selarg);
        String rej = selexp.queryForm(env,local);
        
        if (objectRef.isMultiple())
        { String ename = entity.getName();
          String eref = ename; 
          if (entity.isInterface())
          { eref = ename + "." + ename + "Ops"; } 
        
          return eref + ".subtractAll" + data + "(" + pre + "," + rej + ");";
        }
        else
        { return cont + ".subtract" + data + "(" + pre + "," + rej + ");"; }
      }
      else
      { return "{} /* Invalid subtract */"; }
    }
  }

  private String updateFormRemoveJava6(java.util.Map env, BasicExpression dataform,
                                  Expression exp2, boolean local, String op)
  { String cont = "Controller.inst()"; 

    if (objectRef == null)
    { Expression selarg = new BinaryExpression(op,dataform,exp2);
      Expression selexp = 
          new BinaryExpression("->select",this,selarg);
      String rej = selexp.queryFormJava6(env,local);
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { if (entity.isBidirectionalRole(data))
        { local = false; } 

        if (local)
        { return "subtract" + data + "(" + rej + ");"; }
        else
        { String var = findEntityVariable(env); 
          return cont + ".subtract" + data + "(" + var + ", " + rej + ");"; 
        }
      }
      else if (umlkind == CLASSID)  // eg: exp2 /: C.dataform
      { return cont + ".killAll" + data + "(" + rej + ");"; } 
      else 
      { return "{} /* Invalid subtract */"; }
    }
    else
    { if (umlkind == ROLE && multiplicity == ModelElement.ONE || 
          umlkind == ATTRIBUTE )
      { BasicExpression newdf;
        BasicExpression dataexp = (BasicExpression) clone();
        dataexp.objectRef = null;
        if (dataform.objectRef == null)
        { newdf = dataform;
          newdf.objectRef = dataexp;
        }
        else
        { BasicExpression dor = (BasicExpression) dataform.objectRef;
          dor.setInnerObjectRef(dataexp); 
          // dor.objectRef = dataexp;
          newdf = dataform;
        }
        return 
          ((BasicExpression) objectRef).updateFormRemoveJava6(env,newdf,exp2,local,op);
      } 
      else if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { String pre = objectRef.queryFormJava6(env,local);
        Expression selarg = new BinaryExpression(op,dataform,exp2);
        Expression selexp = 
          new BinaryExpression("->select",this,selarg);
        String rej = selexp.queryFormJava6(env,local);
        
        if (objectRef.isMultiple())
        { String ename = entity.getName();
          String eref = ename; 
          if (entity.isInterface())
          { eref = ename + "." + ename + "Ops"; } 
        
          return eref + ".subtractAll" + data + "(" + pre + "," + rej + ");";
        }
        else
        { return cont + ".subtract" + data + "(" + pre + "," + rej + ");"; }
      }
      else
      { return "{} /* Invalid subtract */"; }
    }
  }

  private String updateFormRemoveJava7(java.util.Map env, BasicExpression dataform,
                                  Expression exp2, boolean local, String op)
  { String cont = "Controller.inst()"; 

    if (objectRef == null)
    { Expression selarg = new BinaryExpression(op,dataform,exp2);
      Expression selexp = 
          new BinaryExpression("->select",this,selarg);
      String rej = selexp.queryFormJava7(env,local);
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { if (entity.isBidirectionalRole(data))
        { local = false; } 

        if (local)
        { return "subtract" + data + "(" + rej + ");"; }
        else
        { String var = findEntityVariable(env); 
          return cont + ".subtract" + data + "(" + var + ", " + rej + ");"; 
        }
      }
      else if (umlkind == CLASSID)  // eg: exp2 /: C.dataform
      { return cont + ".killAll" + data + "(" + rej + ");"; } 
      else 
      { return "{} /* Invalid subtract */"; }
    }
    else
    { if (umlkind == ROLE && multiplicity == ModelElement.ONE || 
          umlkind == ATTRIBUTE )
      { BasicExpression newdf;
        BasicExpression dataexp = (BasicExpression) clone();
        dataexp.objectRef = null;
        if (dataform.objectRef == null)
        { newdf = dataform;
          newdf.objectRef = dataexp;
        }
        else
        { BasicExpression dor = (BasicExpression) dataform.objectRef;
          dor.setInnerObjectRef(dataexp); 
          // dor.objectRef = dataexp;
          newdf = dataform;
        }
        return 
          ((BasicExpression) objectRef).updateFormRemoveJava7(env,newdf,exp2,local,op);
      } 
      else if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { String pre = objectRef.queryFormJava7(env,local);
        Expression selarg = new BinaryExpression(op,dataform,exp2);
        Expression selexp = 
          new BinaryExpression("->select",this,selarg);
        String rej = selexp.queryFormJava7(env,local);
        
        if (objectRef.isMultiple())
        { String ename = entity.getName();
          String eref = ename; 
          if (entity.isInterface())
          { eref = ename + "." + ename + "Ops"; } 
        
          return eref + ".subtractAll" + data + "(" + pre + "," + rej + ");";
        }
        else
        { return cont + ".subtract" + data + "(" + pre + "," + rej + ");"; }
      }
      else
      { return "{} /* Invalid subtract */"; }
    }
  }

  private String updateFormRemoveCSharp(java.util.Map env, BasicExpression dataform,
                                  Expression exp2, boolean local, String op)
  { String cont = "Controller.inst()"; 

    if (objectRef == null)
    { Expression selarg = new BinaryExpression(op,dataform,exp2);
      Expression selexp = 
          new BinaryExpression("->select",this,selarg);
      String rej = selexp.queryFormCSharp(env,local);
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { if (entity.isBidirectionalRole(data))
        { local = false; } 

        if (local)
        { return "subtract" + data + "(" + rej + ");"; }
        else
        { String var = findEntityVariable(env); 
          return cont + ".subtract" + data + "(" + var + ", " + rej + ");"; 
        }
      }
      else if (umlkind == CLASSID)  // eg: exp2 /: C.dataform
      { return cont + ".killAll" + data + "(" + rej + ");"; } 
      else 
      { return "{} /* Invalid subtract */"; }
    }
    else
    { if (umlkind == ROLE && multiplicity == ModelElement.ONE || 
          umlkind == ATTRIBUTE )
      { BasicExpression newdf;
        BasicExpression dataexp = (BasicExpression) clone();
        dataexp.objectRef = null;
        if (dataform.objectRef == null)
        { newdf = dataform;
          newdf.objectRef = dataexp;
        }
        else
        { BasicExpression dor = (BasicExpression) dataform.objectRef;
          dor.setInnerObjectRef(dataexp); 
          // dor.objectRef = dataexp;
          newdf = dataform;
        }
        return 
          ((BasicExpression) objectRef).updateFormRemoveCSharp(env,newdf,exp2,local,op);
      } 
      else if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { String pre = objectRef.queryFormCSharp(env,local);
        Expression selarg = new BinaryExpression(op,dataform,exp2);
        Expression selexp = 
          new BinaryExpression("->select",this,selarg);
        String rej = selexp.queryFormCSharp(env,local);
        
        if (objectRef.isMultiple())
        { String ename = entity.getName();
          String eref = ename; 
          if (entity.isInterface())
          { eref = ename + "Ops"; } 
        
          return eref + ".subtractAll" + data + "(" + pre + "," + rej + ");";
        }
        else
        { return cont + ".subtract" + data + "(" + pre + "," + rej + ");"; }
      }
      else
      { return "{} /* Invalid subtract */"; }
    }
  }

  private String updateFormRemoveCPP(java.util.Map env, BasicExpression dataform,
                                  Expression exp2, boolean local, String op)
  { String cont = "Controller::inst->"; 

    if (objectRef == null)
    { Expression selarg = new BinaryExpression(op,dataform,exp2);
      Expression selexp = 
          new BinaryExpression("->select",this,selarg);
      String rej = selexp.queryFormCPP(env,local);
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { if (entity.isBidirectionalRole(data))
        { local = false; } 

        if (local)
        { return "subtract" + data + "(" + rej + ");"; }
        else
        { String var = findEntityVariable(env); 
          return cont + "subtract" + data + "(" + var + ", " + rej + ");"; 
        }
      }
      else if (umlkind == CLASSID)  // eg: exp2 /: C.dataform
      { return cont + "killAll" + data + "(" + rej + ");"; } 
      else 
      { return "{} /* Invalid subtract */"; }
    }
    else
    { if (umlkind == ROLE && multiplicity == ModelElement.ONE || 
          umlkind == ATTRIBUTE )
      { BasicExpression newdf;
        BasicExpression dataexp = (BasicExpression) clone();
        dataexp.objectRef = null;
        if (dataform.objectRef == null)
        { newdf = dataform;
          newdf.objectRef = dataexp;
        }
        else
        { BasicExpression dor = (BasicExpression) dataform.objectRef;
          dor.setInnerObjectRef(dataexp); 
          // dor.objectRef = dataexp;
          newdf = dataform;
        }
        return 
          ((BasicExpression) objectRef).updateFormRemoveCPP(env,newdf,exp2,local,op);
      } 
      else if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { String pre = objectRef.queryFormCPP(env,local);
        Expression selarg = new BinaryExpression(op,dataform,exp2);
        Expression selexp = 
          new BinaryExpression("->select",this,selarg);
        String rej = selexp.queryFormCPP(env,local);
        
        if (objectRef.isMultiple())
        { String ename = entity.getName();
          String eref = ename; 
        
          return eref + "::subtractAll" + data + "(" + pre + "," + rej + ");";
        }
        else
        { return cont + "subtract" + data + "(" + pre + "," + rej + ");"; }
      }
      else
      { return "{} /* Invalid subtract */"; }
    }
  }

  private String updateFormSubset(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryForm(env,local); 
        return "((List) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
      }
      return data + ".addAll(" + val2 + ");"; 
    }   

    if (umlkind == CLASSID && arrayIndex == null)
    { // System.out.println("Creation operation on " + data); 
      return cont + ".createAll" + data + "(" + val2 + ");"; 
    }

    if (entity.isBidirectionalRole(data))
    { local = false; } 
 
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryForm(env,local); 
            return "((List) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
          }
          return data + ".addAll(" + val2 + ");"; 
        }
        else  
        { return "{} /* can't add to single-valued attribute: " + this + " */"; }
      } // case of index

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or frozen
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryForm(env,local); 
          return cont + ".union" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".addAll(" + val2 + ");"; } 
        
        return cont + ".union" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryForm(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".unionAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".union" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised union: " + val2 + " <: " + this + " */";
  }

  private String updateFormSubsetJava6(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava6(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
      }
      return data + ".addAll(" + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null) 
    { // System.out.println("Creation operation on " + data); 
      return cont + ".createAll" + data + "(" + val2 + ");"; 
    }

    if (entity.isBidirectionalRole(data))
    { local = false; } 
 
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormJava6(env,local); 
            return "((Collection) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
          }
          return data + ".addAll(" + val2 + ");"; 
        }
        else  
        { return "{} /* can't add to single-valued attribute: " + this + " */"; }
      }

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or frozen
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava6(env,local); 
          return cont + ".union" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".addAll(" + val2 + ");"; } 
        
        return cont + ".union" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormJava6(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".unionAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".union" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised union: " + val2 + " <: " + this + " */";
  }

  private String updateFormSubsetJava7(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava7(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
      }
      return data + ".addAll(" + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null) 
    { // System.out.println("Creation operation on " + data); 
      return cont + ".createAll" + data + "(" + val2 + ");"; 
    }

    if (entity.isBidirectionalRole(data))
    { local = false; } 
 
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormJava7(env,local); 
            return "((Collection) " + data + ".get(" + indopt + " -1)).addAll(" + val2 + ");"; 
          }
          return data + ".addAll(" + val2 + ");"; 
        }
        else  
        { return "{} /* can't add to single-valued attribute: " + this + " */"; }
      }

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or frozen
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava7(env,local); 
          return cont + ".union" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".addAll(" + val2 + ");"; } 
        
        return cont + ".union" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormJava7(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".unionAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".union" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised union: " + val2 + " <: " + this + " */";
  }

  private String updateFormSubsetCSharp(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + data + " */"; }

    if (umlkind == VARIABLE)
    { if (type != null && Type.isCollectionType(type))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormCSharp(env,local); 
          return "((ArrayList) " + data + "[" + indopt + " -1]).AddRange(" + val2 + ");"; 
        }
        return data + ".AddRange(" + val2 + ");";
      } 
      else 
      { return "{} /* can't add to single-valued variable: " + this + " */"; }
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null)
    { // System.out.println("Creation operation on " + data); 
      return cont + ".createAll" + data + "(" + val2 + ");"; 
    }

    if (entity.isBidirectionalRole(data))
    { local = false; } 
 
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE)
      { if (type != null && Type.isCollectionType(type))
        { if (arrayIndex != null) 
          { String indopt = arrayIndex.queryFormCSharp(env,local); 
            return "((ArrayList) " + data + "[" + indopt + " -1]).AddRange(" + val2 + ");"; 
          }
          return data + ".AddRange(" + val2 + ");"; 
        }
        else  
        { return "{} /* can't add to single-valued attribute: " + this + " */"; }
      }
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormCSharp(env,local); 
          return cont + ".union" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".AddRange(" + val2 + ");"; } 
        
        return cont + ".union" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormCSharp(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "Ops"; } 
        return eref + ".unionAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".union" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised union: " + val2 + " <: " + this + " */";
  }

  private String updateFormSubsetCPP(java.util.Map env,
                              String val2, boolean local)
  { String cont = "Controller::inst->"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP || prestate)
    { return "{} /* can't add to: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { String pre = data; 
      if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCPP(env,local);
        String coll = "set"; 
        if (isOrdered()) { coll = "vector"; } 
        pre = "(" + data + "->at(" + indopt + " -1));"; 
      }
      if (isOrdered())
      { return pre + "->insert(" + pre + "->end(), " + val2 + "->begin(), " + 
                                val2 + "->end());"; 
      }
      else 
      { return pre + "->insert(" + val2 + "->begin(), " + val2 + "->end());"; }
    } // case of index. 

    if (umlkind == CLASSID && arrayIndex == null)
    { // System.out.println("Creation operation on " + data); 
      return cont + "createAll" + data + "(" + val2 + ");"; 
    }

    if (entity.isBidirectionalRole(data))
    { local = false; } 
 
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { String pre = data; 
        if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormCPP(env,local);
          String coll = "set"; 
          if (isOrdered()) { coll = "vector"; } 
          pre = "(" + data + "->at(" + indopt + " -1));"; 
        }
        if (isOrdered())
        { return pre + "->insert(" + pre + "->end(), " + val2 + "->begin(), " + 
                                val2 + "->end());"; 
        }
        else 
        { return pre + "->insert(" + val2 + "->begin(), " + val2 + "->end());"; }
      } 

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't add to ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormCPP(env,local); 
          return cont + "union" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) 
        { if (isOrdered())
          { return data + "->insert(" + data + "->end(), " + val2 + "->begin(), " + 
                                    val2 + "->end());"; 
          }
          else 
          { return data + "->insert(" + val2 + "->begin(), " + val2 + "->end());"; }
        }   
        return cont + "union" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { String pre = objectRef.queryFormCPP(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        return ename + "::unionAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + "union" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised union: " + val2 + " <: " + this + " */";
  }

  private String updateFormSubtract(java.util.Map env,
                              String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't subtract: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryForm(env,local); 
        return "((List) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
      }
      return data + ".removeAll(" + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".killAll" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryForm(env,local); 
          return "((List) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
        }
        return data + ".removeAll(" + val2 + ");"; 
      } 
    
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or addOnly
        { return "{} /* can't subtract ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryForm(env,local); 
          return cont + ".subtract" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".removeAll(" + val2 + ");"; } 

        return cont + ".subtract" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemove(env,dataform,exp2,local,":"); 
      }
      String pre = objectRef.queryForm(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".subtractAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".subtract" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised subtract: " + this + " - " + val2 + " */";
  }

  private String updateFormSubtractJava6(java.util.Map env,
              String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't subtract: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava6(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
      }
      return data + ".removeAll(" + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".killAll" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormJava6(env,local); 
          return "((Collection) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
        }
        return data + ".removeAll(" + val2 + ");"; 
      } 

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or addOnly
        { return "{} /* can't subtract ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava6(env,local); 
          return cont + ".subtract" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".removeAll(" + val2 + ");"; } 

        return cont + ".subtract" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveJava6(env,dataform,exp2,local,":"); 
      }
      String pre = objectRef.queryFormJava6(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".subtractAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".subtract" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised subtract: " + this + " - " + val2 + " */";
  }

  private String updateFormSubtractJava7(java.util.Map env,
                              String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't subtract: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormJava7(env,local); 
        return "((Collection) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
      }
      return data + ".removeAll(" + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".killAll" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormJava7(env,local); 
          return "((Collection) " + data + ".get(" + indopt + " -1)).removeAll(" + val2 + ");"; 
        }
        return data + ".removeAll(" + val2 + ");"; 
      } 

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE) // or addOnly
        { return "{} /* can't subtract ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormJava7(env,local); 
          return cont + ".subtract" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + ".removeAll(" + val2 + ");"; } 

        return cont + ".subtract" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveJava7(env,dataform,exp2,local,":"); 
      }
      String pre = objectRef.queryFormJava7(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "." + ename + "Ops"; } 
        return eref + ".subtractAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".subtract" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised subtract: " + this + " - " + val2 + " */";
  }

  private String updateFormSubtractCSharp(java.util.Map env,
                              String val2, Expression exp2, boolean local)
  { String cont = "Controller.inst()"; 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't subtract: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCSharp(env,local); 
        return data + "[" + indopt + " -1] = SystemTypes.subtract((ArrayList) " + 
                                           data + "[" + indopt + " -1], " + val2 + ");"; 
      }
      return data + " = SystemTypes.subtract(" + data + ", " + val2 + ");"; 
    }
      // val2 must be a list

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + ".killAll" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormCSharp(env,local); 
          return data + "[" + indopt + " -1] = SystemTypes.subtract((ArrayList) " + data + "[" + indopt + " -1], " + val2 + ");"; 
        }
        return data + " = SystemTypes.subtract(" + data + ", " + val2 + ");"; 
      }      

      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)  // and not addOnly
        { return "{} /* can't subtract ONE role: " + this + " */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormCSharp(env,local); 
          return cont + ".subtract" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) { return data + " = SystemTypes.subtract(" + data + ", " + val2 + ");"; } 

        return cont + ".subtract" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveCSharp(env,dataform,exp2,local,":"); 
      }
      String pre = objectRef.queryFormCSharp(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        String eref = ename; 
        if (entity != null && entity.isInterface())
        { eref = ename + "Ops"; } 
        return eref + ".subtractAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + ".subtract" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised subtract: " + this + " - " + val2 + " */";
  }

  private String updateFormSubtractCPP(java.util.Map env,
                              String val2, Expression exp2, boolean local)
  { String cont = "Controller::inst->"; 
    Type et = getElementType(); 
    String cetype = "void*"; 
    if (et != null) { cetype = et.getCPP(); } 

    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == QUERY ||
        umlkind == FUNCTION || umlkind == UPDATEOP || prestate)
    { return "{} /* can't subtract: " + this + " */"; }

    if (umlkind == VARIABLE && Type.isCollectionType(type))
    { String pre = data; 
      if (arrayIndex != null) 
      { String indopt = arrayIndex.queryFormCPP(env,local); 
        pre = "(" + data + "->at(" + indopt + " -1));"; 
      }
      return pre + " = UmlRsdsLib<" + cetype + ">::subtract(" + pre + ", " + val2 + ");";
    }
    // case of index, and below. 

    if (umlkind == CLASSID && arrayIndex == null)
    { return cont + "killAll" + entity.getName() + "(" + val2 + ");"; }

    if (entity.isBidirectionalRole(data))
    { local = false; } 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE && Type.isCollectionType(type))
      { String pre = data; 
        if (arrayIndex != null) 
        { String indopt = arrayIndex.queryFormCPP(env,local); 
          pre = "(" + data + "->at(" + indopt + " -1));"; 
        }
        return pre + " = UmlRsdsLib<" + cetype + ">::subtract(" + pre + ", " + val2 + ");";
      }
  
      if (umlkind == ROLE)
      { if (multiplicity == ModelElement.ONE)
        { return "{} /* can't subtract ONE role */"; }

        String var = findEntityVariable(env); 

        if (isQualified() && arrayIndex != null) 
        { String ind = arrayIndex.queryFormCPP(env,local); 
          return cont + "subtract" + data + "(" + var + ", " + ind + ", " + val2 + ");"; 
        } 

        if (local) 
        { return data + " = UmlRsdsLib<" + cetype + ">::subtract(" + data + ", " + val2 + ");"; } 

        return cont + "subtract" + data + "(" + var + "," + val2 + ");";
      } // no need to wrap -- val2 must be a list
    }
    else // objectRef != null, ROLE, != ONE assumed
    { if (umlkind == ATTRIBUTE || umlkind == ROLE && multiplicity == ModelElement.ONE)
      { BasicExpression dataform = (BasicExpression) clone(); 
        dataform.objectRef = null; 
        return
         ((BasicExpression) objectRef).updateFormRemoveCPP(env,dataform,exp2,local,":"); 
      }
      String pre = objectRef.queryFormCPP(env,local);
      if (objectRef.isMultiple())
      { String ename = entity.getName();
        return ename + "::subtractAll" + data + "(" + pre + "," + val2 + ");";
      }
      else
      { return cont + "subtract" + data + "(" + pre + "," + val2 + ");"; }
    }
    return "{} /* unrecognised subtract: " + this + " - " + val2 + " */";
  }

  public BExpression bqueryForm(java.util.Map env)
  { // assume expression already type-checked. true |-> TRUE, etc
    BExpression res; 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { BExpression bind = arrayIndex.binvariantForm(env,true); 
        if (entity != null) 
        { Attribute pk = entity.getPrincipalKey();
          BExpression invpk = new BPostfixExpression("~", new BBasicExpression(pk + ""));  
          if (arrayIndex.isMultiple())
          { res = new BApplySetExpression(invpk,bind); } 
          else 
          { res = new BApplyExpression(invpk,bind); }  
          return res; 
        } 
      } 
      String es = data.toLowerCase() + "s"; 
      res = new BBasicExpression(es); 
      res.setMultiplicity(ModelElement.MANY); 
      res.setKind(umlkind); 
      return res; 
    } 

    if (umlkind == VALUE || umlkind == VARIABLE || umlkind == CONSTANT)
    { if (data.equals("self"))
      { BExpression var = findBContextVariable(env,entity); 
        return var; 
      } 
      if (data.equals("true"))
      { res = new BBasicExpression("TRUE"); } 
      else if (data.equals("false"))
      { res = new BBasicExpression("FALSE"); }
      else if (isSequence(data) || isSet(data))
      { Expression se = buildSetExpression(data); 
        BExpression seqf = se.bqueryForm(env); 
        if (arrayIndex != null) 
        { BExpression ind = arrayIndex.binvariantForm(env,true); 
          return new BApplyExpression(seqf,ind); 
        } 
        return seqf; 
      } 
      else if (isString(data))
      { BExpression bexp = new BBasicExpression(data);
        bexp.setKind(umlkind);  
        if (arrayIndex != null)
        { BExpression ind = arrayIndex.binvariantForm(env,true); 
          return new BApplyExpression(bexp,ind); 
        } 
        return bexp; 
      } 
      else if (isMultiple())
      { res = new BBasicExpression(data); } 
      else 
      { Vector vv = new Vector();
        BBasicExpression be = new BBasicExpression(data);
        be.setKind(umlkind); 
        vv.add(be);
        return new BSetExpression(vv);
      }
      res.setKind(umlkind); 
      return res; 
    }  // makeBSet -- WHY????

    if (umlkind == FUNCTION)
    { BExpression pre = objectRef.bqueryForm(env);
      // System.out.println(objectRef + " query form is: " + pre + " " +
      //                   pre.setValued());
      String op;
      if (data.equals("size"))
      { op = "card";
        return new BUnaryExpression(op,pre.simplify());
      }
      else if (data.equals("abs") || data.equals("sqr") ||
          data.equals("sqrt") || data.equals("max") ||
          data.equals("min"))
      { op = data;
        return new BUnaryExpression(op,pre.simplify());
      } // can be sets { xx | yy: pre & xx = f(yy) } 
      else if (data.equals("closure"))
      { // System.out.println(entity + " " + objectRef + " " + objectRef.entity + 
        // env); 
        String rel = ((BasicExpression) objectRef).data; 
          // the relation being closured
        if (((BasicExpression) objectRef).objectRef == null) 
        { BExpression var = findBContextVariable(env,objectRef.entity); 
          return new BUnaryExpression("closure(" + rel + ")",var); 
        } 
        BExpression objs = 
          ((BasicExpression) objectRef).objectRef.bqueryForm(env); 
        return new BUnaryExpression("closure(" + rel + ")",objs); 
      } 
      else if (data.equals("any"))
      { BExpression arg = pre.simplify(); 
        arg.setBrackets(true); 
        return new BApplyExpression(arg,new BBasicExpression("1")); 
      }
      else if (data.equals("subcollections"))
      { return new BUnaryExpression("FIN",pre.simplify()); } 
      else if (data.equals("asSet"))
      { if (objectRef.isOrderedB())
        { return new BUnaryExpression("ran",pre.simplify()); }
        else 
        { return pre.simplify(); } 
      }  
      else if (data.equals("sum")) // Ignore toLower, toUpper
      { BExpression xxbe = new BBasicExpression("x_x");
        BExpression pred =
          new BBinaryExpression(":",xxbe,pre);
        return new BSigmaExpression("x_x",pred,xxbe);
      }
      else if (data.equals("oclIsKindOf"))
      { Expression par1 = (Expression) parameters.get(0); 
        String par1s = par1 + ""; 
        BExpression rightobjs = new BBasicExpression(par1s.toLowerCase() + "s"); 
        return new BBinaryExpression(":", pre, rightobjs);
      } 
      else if (data.equals("oclAsType"))  
      { return pre; } 
      else // last, first, front, tail, sort, reverse
      { return new BUnaryExpression(data,pre); } 
    }

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (umlkind == ATTRIBUTE)
        { Attribute datt = entity.getDefinedAttribute(data); 
          if (datt == null || datt.isClassScope())
          { BExpression bres = new BBasicExpression(data);
            bres.setKind(umlkind); 
          }  // if array?
        } 
        BExpression var = findBContextVariable(env,entity); 
        BApplySetExpression fapp = 
          new BApplySetExpression(data,var);
        fapp.setMultiplicity(multiplicity); 
        
        if (umlkind == ROLE && multiplicity != ModelElement.ONE)
        { BExpression ufapp = new BUnaryExpression("union",fapp);
          if (arrayIndex != null)
          { BExpression ind = arrayIndex.binvariantForm(env,true); 
            return new BApplyExpression(ufapp,ind); 
          }  // binvariantForm for array indexes
          if (entity.getRole(data).isOrdered())
          { return new BUnaryExpression("ran",ufapp); } 
          else  
          { return ufapp; } 
        }   
        return fapp; 
      }
      else if (umlkind == QUERY)
      { Vector bpars = new Vector(); 
        if (parameters != null)
        { for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
            BExpression bpar = par.binvariantForm(env,true); 
            bpars.add(bpar); 
          }
        }
        return new BBasicExpression(data,bpars);  
      } // may have array index
    }
    else 
    { BExpression pre = objectRef.bqueryForm(env);
      BApplySetExpression fapp = new BApplySetExpression(data,pre);
      fapp.setMultiplicity(multiplicity); 
        
      if (umlkind == ROLE && multiplicity != ModelElement.ONE)
      { BExpression ufapp = new BUnaryExpression("union",fapp);
        if (arrayIndex != null)
        { BExpression ind = arrayIndex.binvariantForm(env,true); 
          return new BApplyExpression(ufapp,ind); 
        } // actually { sq(ind) | sq : ufapp } 
        if (entity.getRole(data) != null && entity.getRole(data).isOrdered())
        { return new BUnaryExpression("ran",ufapp); } 
        else  
        { return ufapp; } 
      }   
      if (umlkind == ATTRIBUTE) 
      { Attribute datt = entity.getDefinedAttribute(data); 
        if (datt == null || datt.isClassScope())
        { BExpression bres = new BBasicExpression(data);
          bres.setKind(umlkind); 
        }  // array case?
      } 
      else if (umlkind == QUERY)
      { Vector bpars = new Vector(); 
        if (parameters != null)
        { for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
            BExpression bpar = par.binvariantForm(env,true); 
            bpars.add(bpar); 
          }
        }
        Vector prepars = new Vector(); 
        prepars.add(pre); 
        BBasicExpression bres = 
          new BBasicExpression(data + "_" + entity,prepars); 
        bres.setKind(umlkind); 
        if (bpars.size() > 0)
        { return new BApplyExpression(bres,bpars); } 
        else 
        { return bres; }  
      } // may have array index
   
      if (arrayIndex != null)
      { BExpression ind = arrayIndex.binvariantForm(env,true); 
        return new BApplyExpression(fapp,ind); 
      } 
           
      return fapp; 
    } // same as above if multiple
    Vector vv = new Vector();
    BBasicExpression be = new BBasicExpression(data);
    be.setKind(umlkind); 
    vv.add(be);
    return new BSetExpression(vv);
  }  // query form of CLASS ident is lower case of it + "s". Set valued

  public BExpression bqueryForm()
  { return bqueryForm(null); } 

  public static BExpression findBContextVar(java.util.Map env, Entity e)
  { BasicExpression exp = new BasicExpression(("" + e).toLowerCase() + "x"); 
    return exp.findBContextVariable(env,e); 
  } 

  public BExpression findBContextVariable(java.util.Map env, Entity e)
  { BExpression var; 
    if (env == null || e == null) 
    { var = new BBasicExpression(data);
      var.setKind(umlkind); 
    } 
    else 
    { String nme = e.getName();
      var = (BExpression) env.get(nme);
      if (var == null)  // because nme is superclass of ent: dom(env)
      { var = e.searchForSubclass(env); } 
      if (var == null)
      { var = e.searchForSuperclass(env); 
        if (var == null) 
        { System.err.println("!! Error: no variable for " + nme); 
          var = new BBasicExpression(nme.toLowerCase() + "x"); 
        } 
        else 
        { var = new BBinaryExpression("/\\", var, 
                         new BBasicExpression(nme.toLowerCase() + "s")); 
        } 
      } 
    }
    var.setKind(umlkind); 
    return var; 
  } 

  public BStatement bupdateForm(java.util.Map env,
                            String operator, BExpression val2, boolean local) 
  { if (operator.equals("="))
    { return bEqupdateForm(env,val2,local); }
    else if (operator.equals(":"))
    { return bInupdateForm(env,val2,local); }
    else if (operator.equals("/:"))
    { return bNotinupdateForm(env,val2,local); }
    else if (operator.equals("<:"))
    { return bSubsetupdateForm(env,val2,local); }
    else if (operator.equals("/<:"))
    { return bSubtractupdateForm(env,val2,local); } 
    else
    { return new BBasicStatement("skip"); } 
  }

  public BStatement bupdateForm(java.util.Map env, boolean local) 
  { String feat = ""; 
    String op = ""; 

    System.out.println(">> B UPDATE FORM OF " + this + " " + isEvent); 

    if (isEvent) // an operation of entity -- not allowed if local
    { Vector oppars = new Vector(); 
      
      if (data.charAt(0) == 's' && data.length() > 3) // set
      { feat = data.substring(3,data.length()); 
        op = data.substring(0,3); 
        System.out.println("Feature == " + feat + " op = " + op); 
      } 
      else if (data.charAt(0) == 'a' && data.length() > 3)
      { feat = data.substring(3,data.length()); 
        op = data.substring(0,3); 
      } 
      else if (data.charAt(0) == 'r' && data.length() > 6) // remove
      { feat = data.substring(6,data.length());
        op = data.substring(0,6); 
      }  

      if (parameters != null)
      { for (int i = 0; i < parameters.size(); i++) 
        { Expression e = (Expression) parameters.get(i); 
          BExpression be = e.binvariantForm(env,local); 
          oppars.add(be); 
        }
      }
      Vector pars = new Vector(); 
      String ename = entity.getName();
      if (objectRef == null)
      { BExpression var = (BExpression) env.get(ename);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 
        if (var == null) 
        { System.err.println("!! Error: No variable of " + ename); 
          var = new BBasicExpression(ename.toLowerCase() + "x"); 
          var.setKind(VARIABLE); 
        }        

        BExpression pre1 = var.simplify(); 
        BExpression psimp = BExpression.unmakeSet(pre1); 
        pars.add(psimp); 
        pars.addAll(oppars); 
        
        BStatement res;
        if (psimp.setValued() && op != null &&
            op.equals("set") || op.equals("remove") || op.equals("add"))
        { res = new BOperationCall(op + "All" + feat,pars); }
        else 
        { res = new BOperationCall(data,pars); } 
        res.setWriteFrame(feat); 
        return res; 
      }
      else if ("super".equals("" + objectRef))
      { System.out.println(">> Super entity: " + entity); 
        if (entity != null) //  && entity.getSuperclass() != null)
        { // Entity sup = entity.getSuperclass(); 
          BehaviouralFeature supop = entity.getOperation(data); 
          if (supop != null)
          { Expression suppost = supop.getPost(); 
            return suppost.bupdateForm(env,local); 
          } 
        } 
        return new BBasicStatement("skip");
      } 
      else 
      { BExpression pre = objectRef.bqueryForm(env);   // better to use binvariantForm
        BExpression pre1 = pre.simplify(); 
        BExpression psimp = BExpression.unmakeSet(pre1); 
        pars.add(psimp); 
        pars.addAll(oppars); 
        
        BStatement res;
        if (psimp.setValued() && op != null &&
            op.equals("set") || op.equals("remove") || 
            op.equals("add"))
        { res = new BOperationCall(op + "All" + feat,pars); }
        else 
        { res = new BOperationCall(data,pars); } 
        res.setWriteFrame(feat); 
        return res; 
      }
    }
    else
    { System.out.println("!! No update form for " + this + " " + isEvent); 
      return new BBasicStatement("skip"); 
    } // no sensible update form
  }
  // If objectRef is multiple may need setAll versions.
  // also local versions
  
  public BStatement bEqupdateForm(java.util.Map env,
                                  BExpression s, boolean local)
  { BStatement res; 
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP)
    { System.out.println("Cannot update: " + this); 
      return new BBasicStatement("skip"); 
    }
    if (prestate == true)
    { System.out.println("Cannot update: " + this); 
      return new BBasicStatement("skip"); 
    }
    if (umlkind == VARIABLE)
    { if (data.equals("result"))
      { BExpression be = new BBasicExpression(data); 
        be.setKind(VARIABLE); 
        res = new BAssignStatement(be,s); 
        res.setWriteFrame(data); 
        return res; 
      }
      System.out.println("Cannot update: " + this); 
      return new BBasicStatement("skip");
    }
    BExpression ind = null; 
    if (arrayIndex != null) 
    { ind = arrayIndex.binvariantForm(env,local); }  // really bqueryForm!

    Association ast = entity.getDefinedRole(data); 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { String nme = entity.getName();
        BExpression var = (BExpression) env.get(nme);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 
        if (var == null) // error
        { var = entity.searchForSuperclass(env); 
          if (var == null) 
          { System.err.println("No range of values for " + nme); 
            var = new BBasicExpression(entity.getName().toLowerCase() + "x"); 
          }
          else 
          { var = new BBinaryExpression("/\\",var,
                    new BBasicExpression(nme.toLowerCase() + "s")); 
          } 
        } 
        var.setKind(umlkind); 
        if (local)
        { return constructLocalBOp("set",data,var.simplify(),s,ind,ast); }
        return constructBOp("set",data,var.simplify(),s,ind);
      }
    }
    else
    { String nme = entity.getName();
      BExpression pre = objectRef.binvariantForm(env,local);
      if (umlkind == ATTRIBUTE && entity.isClassScope(data))
      { pre = null; }  // and for add, remove etc
      else 
      { pre = pre.simplify(); } 
      if (local)
      { return constructLocalBOp("set",data,pre,s,ind,ast); }
     
      return constructBOp("set",data,pre,s,ind);
    }
    return new BBasicStatement("skip");
  }

  public BStatement bInupdateForm(java.util.Map env,
                                  BExpression s, boolean local)
  { BStatement res;
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP)
    { return new BBasicStatement("skip"); } 
    if (umlkind == CLASSID)
    { String dvar = data.toLowerCase() + "x";
      String dvars = data.toLowerCase() + "s";
      String tname = data + "_OBJ";
      BExpression dexp = new BBasicExpression(dvar);
      BExpression texp = new BBasicExpression(tname);
      BExpression objs = new BBasicExpression(dvars);
      BExpression mns = new BBinaryExpression("-",texp,objs);
      BExpression exp = new BBinaryExpression(":",dexp,mns);
      // BExpression s1 = BExpression.unSet(s); 
      // String snme = s1 + "";
      // BStatement assgn =
      //   constructLocalBOp("set",snme,null,dexp,null);
      BStatement addst =
         constructLocalBOp("add",dvars,null,dexp,null,null);
      // BParallelStatement stat = new BParallelStatement();
      // stat.addStatement(assgn);
      // stat.addStatement(addst);

    // if (local && entity.hasAttribute(snme))
      Vector vars = new Vector();
      vars.add(dvar);
    // ANY snmex WHERE snmex : data.toUpperCase() -
    //   data.toLowerCase() + "s" THEN snme(datax) := snmex ||
    //      datas := datas \/ {snmex} END
      res = new BAnyStatement(vars,exp,addst);
      return res;
    }
    if (umlkind == VARIABLE)
    { if (data.equals("result"))   // or lhs := lhs \/ rhs in all cases?
      { BSetExpression rhs = new BSetExpression(); 
        rhs.addElement(s); 
        BExpression be = new BBasicExpression(data);
        be.setKind(VARIABLE); 
        res = new BAssignStatement(be,rhs);
        res.setWriteFrame(data); 
        return res; 
      }
      return new BBasicStatement("skip");
    }

    Association ast = entity.getDefinedRole(data); 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { String nme = entity.getName();
        BExpression var = (BExpression) env.get(nme);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 
        if (local)
        { return constructLocalBOp("add",data,var.simplify(),s,null,ast); }
        return constructBOp("add",data,var.simplify(),s,null);
      }
    }
    else
    { BExpression pre = objectRef.binvariantForm(env,local);
      if (local)
      { return constructLocalBOp("add",data,pre.simplify(),s,null,ast); }
      return constructBOp("add",data,pre.simplify(),s,null);
    }
    return new BBasicStatement("skip");
  }

  public BStatement bNotinupdateForm(java.util.Map env,
                                     BExpression s, boolean local)
  { BStatement res;
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP)
    { return new BBasicStatement("skip"); } 
    if (umlkind == VARIABLE)
    { // if (data.equals("result"))
      // { return "result := result - {" + s + "}"; }
      return new BBasicStatement("skip");
    }

    Association ast = entity.getDefinedRole(data); 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { String nme = entity.getName();
        BExpression var = (BExpression) env.get(nme);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 
        if (local)
        { return constructLocalBOp("remove",data,var.simplify(),s,null,ast); } 
        return constructBOp("remove",data,var.simplify(),s,null);
      }
    }
    else
    { BExpression pre = objectRef.binvariantForm(env,local);
      if (local)
      { return constructLocalBOp("remove",data,pre.simplify(),s,null,ast); }
      return constructBOp("remove",data,pre.simplify(),s,null);
    }
    return new BBasicStatement("skip");
  }

  public BStatement bSubsetupdateForm(java.util.Map env,
                                      BExpression s, boolean local)
  { BStatement res;
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP)
    { return new BBasicStatement("skip"); } 
    if (umlkind == VARIABLE)
    { if (data.equals("result"))
      { BExpression be = new BBasicExpression(data); 
        res = new BAssignStatement(be,s);
        res.setWriteFrame(data);
        return res; 
      } 
      return new BBasicStatement("skip");
    }
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { String nme = entity.getName();
        BExpression var = (BExpression) env.get(nme);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 

        if (local)
        { return constructLocalBOp("union",data,var.simplify(),s,null,null); } 
        return constructBOp("union",data,var.simplify(),s,null);
      }
    }
    else
    { BExpression pre = objectRef.binvariantForm(env,local);
      if (local)
      { return constructLocalBOp("union",data,pre.simplify(),s,null,null); } 
      return constructBOp("union",data,pre.simplify(),s,null);
    }
    return new BBasicStatement("skip");
  }

  public BStatement bSubtractupdateForm(java.util.Map env,
                                     BExpression s, boolean local)
  { BStatement res;
    if (umlkind == VALUE || umlkind == CONSTANT || umlkind == FUNCTION ||
        umlkind == QUERY || umlkind == UPDATEOP)
    { return new BBasicStatement("skip"); } 
    if (umlkind == VARIABLE)
    { // if (data.equals("result"))
      // { return "result := result - {" + s + "}"; }
      return new BBasicStatement("skip");
    }
    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { String nme = entity.getName();
        BExpression var = (BExpression) env.get(nme);
        if (var == null)  // because nme is superclass of ent: dom(env)
        { var = entity.searchForSubclass(env); } 
        if (local)
        { return constructLocalBOp("subtract",data,var.simplify(),s,null,null); } 
        return constructBOp("subtract",data,var.simplify(),s,null);
      }
    }
    else
    { BExpression pre = objectRef.binvariantForm(env,local);
      if (local)
      { return constructLocalBOp("subtract",data,pre.simplify(),s,null,null); }
      return constructBOp("subtract",data,pre.simplify(),s,null);
    }
    return new BBasicStatement("skip");
  }

  private BStatement constructBOp(String op, String feat,
                                  BExpression var,
                                  BExpression val, BExpression ind)
  { // System.out.println("Call: " + op + " " + var + " " + val +
    //                   " " + var.setValued() + " " + val.setValued());
    BStatement res; 
    Vector params = new Vector();
    if (var == null)
    { if (ind != null) 
      { params.add(ind); } 
      params.add(val);
      res = new BOperationCall(op + feat,params);
      res.setWriteFrame(feat); 
      return res; 
    }  
    else if (var instanceof BSetExpression)
    { BSetExpression svar = (BSetExpression) var;
      if (svar.isSingleton())
      { BExpression e = svar.getElement(0);
        params.add(e);
        if (ind != null) 
        { params.add(ind); } 
        params.add(val);
        if (e.setValued())
        { res = new BOperationCall(op + "All" + feat,params);
          res.setWriteFrame(feat); 
          return res; 
        } 
        res = new BOperationCall(op + feat,params);
        res.setWriteFrame(feat); 
        return res; 
      }
    }
    params.add(var);
    if (ind != null) 
    { params.add(ind); }     
    params.add(val);
    // res = new BOperationCall(op + "All" + feat,params);
    res = new BOperationCall(op + feat,params);
    res.setWriteFrame(feat); 
    return res; 
  }

  public BStatement constructLocalBOp(String op,String f,
                                  BExpression e,
                                  BExpression v, BExpression ind, Association ast)
  { BStatement res; 
    BExpression var = null;
    if (e != null)
    { var = BExpression.unmakeSet(e); }  
    if (var != null && var.setValued())
    { return buildLocalAllOp(op,f,var,v,ind); } // updates a set of objects at once
    BExpression lhs; 
    if (var != null)
    { lhs = new BApplyExpression(f,var); }
    else 
    { lhs = new BBasicExpression(f); } 

    lhs.setMultiplicity(multiplicity); 
    lhs.setKind(umlkind); 
    if (op.equals("set"))
    { if (ind != null)
      { lhs = new BApplyExpression(lhs,ind); }
      res = new BAssignStatement(lhs,v); 
      res.setWriteFrame(f); 
      System.out.println("WRITE FRAME of " + res + " : " + f); 
      if (ast != null) 
      { String role1 = ast.getRole1(); 
        if (role1 != null && role1.length() > 0)
        { // an inverse is needed
          int card1 = ast.getCard1(); 
          int card2 = ast.getCard2(); 
          // role2 is data
          BExpression invrhs = computeInverseBExpressionSet(ast,role1,data,card1,card2,var,v); 
          BExpression invlhs = new BBasicExpression(role1); 
          BStatement invstat = new BAssignStatement(invlhs,invrhs); 
          invstat.setWriteFrame(role1); 
          BParallelStatement body = new BParallelStatement(); 
          body.addStatement(res); 
          body.addStatement(invstat); 
          return body; 
        }
      } 
      return res;
    }
    if (op.equals("add"))
    { BSetExpression se =
        new BSetExpression();
      if (v instanceof BSetExpression)
      { se = (BSetExpression) v; } 
      else 
      { se.addElement(v); }
      BExpression rhs; 
      if (ast != null && ast.isOrdered())
      { se.setOrdered(true); 
        rhs = new BBinaryExpression("^",lhs,se); 
      } 
      else 
      { rhs = new BBinaryExpression("\\/",lhs,se); } // different for sequences
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      if (ast != null) 
      { String role1 = ast.getRole1(); 
        if (role1 != null && role1.length() > 0)
        { // an inverse is needed
          int card1 = ast.getCard1(); 
          int card2 = ast.getCard2(); 
          // role2 is data
          BStatement invstat = computeInverseBAdd(ast,role1,data,card1,card2,var,v); 
          BParallelStatement body = new BParallelStatement(); 
          body.addStatement(res); 
          body.addStatement(invstat); 
          return body; 
        }
      } 
      return res; 
    }
    if (op.equals("union"))
    { BExpression rhs =
        new BBinaryExpression("\\/",lhs,v);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("subtract"))
    { BExpression rhs =
        new BBinaryExpression("-",lhs,v);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("remove"))
    { BSetExpression se =
        new BSetExpression();
      if (v instanceof BSetExpression)
      { se = (BSetExpression) v; } 
      else 
      { se.addElement(v); }
      BExpression rhs;
      if (ast != null && ast.isOrdered())
      { // { _ii_, _yy_ | #_jj_.(_jj_ : dom(f) & _yy_ = f(_jj_) & _ii_ = card(dom(f) \cap 1.._jj_) ) }
        // where  f = lhs |>> se
        BBinaryExpression rresf = new BBinaryExpression("|>>",lhs,se);
        Vector subparams = new Vector();
        subparams.add("_ii_");
        subparams.add("_yy_");
        BUnaryExpression domf = new BUnaryExpression("dom", rresf);
        BBasicExpression jjbe = new BBasicExpression("_jj_"); 
        BBinaryExpression rge = new BBinaryExpression("..", new BBasicExpression("1"),  jjbe);
        BBinaryExpression intrsec = new BBinaryExpression("/\\", domf, rge);
        BUnaryExpression cardintrsec = new BUnaryExpression("card", intrsec);
        BBasicExpression iibe = new BBasicExpression("_ii_"); 
        BBinaryExpression iieq = new BBinaryExpression("=", iibe, cardintrsec);
        BBinaryExpression jjindom = new BBinaryExpression(":", jjbe, domf);
        BApplyExpression appf = new BApplyExpression(lhs, jjbe);
        BBasicExpression yybe = new BBasicExpression("_yy_"); 
        BBinaryExpression yyeq = new BBinaryExpression("=", yybe, appf);
        BBinaryExpression and1 = new BBinaryExpression("&", jjindom, yyeq);
        BBinaryExpression and2 = new BBinaryExpression("&", and1, iieq);
        BQuantifierExpression exists1 = new BQuantifierExpression("#", "_jj_", and2);
        rhs = new BSetComprehension(subparams, exists1);
      } 
      else 
      { rhs =
          new BBinaryExpression("-",lhs,se); 
      }  // DIFFERENT FOR SEQUENCES
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      if (ast != null) 
      { String role1 = ast.getRole1(); 
        if (role1 != null && role1.length() > 0)
        { // an inverse is needed
          int card1 = ast.getCard1(); 
          int card2 = ast.getCard2(); 
          // role2 is data
          BStatement invstat = computeInverseBDel(ast,role1,data,card1,card2,var,v); 
          BParallelStatement body = new BParallelStatement(); 
          body.addStatement(res); 
          body.addStatement(invstat); 
          return body; 
        }
      } 
      return res; 
    }
    return new BBasicStatement("skip"); 
  } // deal with cases other than set. 

  private BStatement buildLocalAllOp(String op,String f,
                                  BExpression e,
                                  BExpression v, BExpression ind)
  { BStatement res; 
    BExpression lhs = new BBasicExpression(f);
    if (op.equals("set"))  // f := f <+ e * {v}
    { if (ind != null) // f := f <+ { ee,sq | ee: e & sq = f(ee) <+ {ind |-> v}}
      { BExpression maplet = new BBinaryExpression("|->",ind,v);
        BSetExpression sem = new BSetExpression(); 
        sem.addElement(maplet);  
        String ee = Identifier.nextIdentifier("_ee"); 
        BExpression ex = new BBasicExpression(ee); 
        BExpression fapp = new BApplyExpression(f,ex);
        BExpression upd = new BBinaryExpression("<+",fapp,sem);
        String ss = Identifier.nextIdentifier("_xx"); 
        BExpression sq = new BBasicExpression(ss); 
        BExpression updeq = new BBinaryExpression("=",sq,upd); 
        BExpression inexp = new BBinaryExpression(":",ex,e); 
        BExpression pred = new BBinaryExpression("&",inexp,updeq);   
        Vector vars = new Vector(); 
        vars.add(ee); 
        vars.add(ss);  
        BExpression comp = new BSetComprehension(vars,e,pred); // e? 
        BExpression rhs = new BBinaryExpression("<+",lhs,comp);
        return new BAssignStatement(lhs,rhs);  
      }
      BSetExpression se = new BSetExpression();
      se.addElement(v);
      BExpression prd =
        new BBinaryExpression("*",e,se);
      BExpression rhs =
        new BBinaryExpression("<+",lhs,prd);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("add"))  // f := f <+ { ee,xx | ee : e & xx = f(ee) \/ {v} }
    { BSetExpression se = new BSetExpression();
      se.addElement(v);
      Vector vars = new Vector();
      String ee = Identifier.nextIdentifier("_ee"); 
      vars.add(ee);
      String xx = Identifier.nextIdentifier("_xx"); 
      vars.add(xx);
      BExpression be = new BBasicExpression(ee);
      BExpression xe = new BBasicExpression(xx);
      BExpression inexp = new BBinaryExpression(":",be,e); 
      BExpression fapp =
        new BApplyExpression(f,be);
      fapp.setMultiplicity(multiplicity);  // f is data
      BExpression un =
        new BBinaryExpression("\\/",fapp,se);
      BExpression pred =
        new BBinaryExpression("=",xe,un);
      pred = new BBinaryExpression("&",inexp,pred); 
      BExpression sc =
        new BSetComprehension(vars,e,pred);
      BExpression rhs =
        new BBinaryExpression("<+",lhs,sc);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("union"))  // f := f <+ { ee,xx | ee : e & xx = f(ee) \/ v }
    { Vector vars = new Vector();
      String ee = Identifier.nextIdentifier("_ee"); 
      vars.add(ee);
      String xx = Identifier.nextIdentifier("_xx"); 
      vars.add(xx);
      BExpression be = new BBasicExpression(ee);
      BExpression xe = new BBasicExpression(xx);
      BExpression fapp =
        new BApplyExpression(f,be);
      fapp.setMultiplicity(multiplicity); 
      BExpression inexp = new BBinaryExpression(":",be,e); 
      BExpression un =
        new BBinaryExpression("\\/",fapp,v);
      BExpression pred =
        new BBinaryExpression("=",xe,un);
      pred = new BBinaryExpression("&",inexp,pred); 
     
      BExpression sc =
        new BSetComprehension(vars,e,pred);
      BExpression rhs =
        new BBinaryExpression("<+",lhs,sc);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("subtract"))  // f := f <+ { ee,xx | ee : e & xx = f(ee) - v }
    { Vector vars = new Vector();
      String ee = Identifier.nextIdentifier("_ee"); 
      vars.add(ee);
      String xx = Identifier.nextIdentifier("_xx"); 
      vars.add(xx);
      BExpression be = new BBasicExpression(ee);
      BExpression xe = new BBasicExpression(xx);
      BExpression inexp = new BBinaryExpression(":",be,e); 
      BExpression fapp =
        new BApplyExpression(f,be);
      fapp.setMultiplicity(multiplicity);  // f is data
      BExpression un =
        new BBinaryExpression("-",fapp,v);
      BExpression pred =
        new BBinaryExpression("=",xe,un);
      pred = new BBinaryExpression("&",inexp,pred); 
      BExpression sc =
        new BSetComprehension(vars,e,pred);
      BExpression rhs =
        new BBinaryExpression("<+",lhs,sc);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    if (op.equals("remove"))  // f := f <+ { ee,xx | ee : e & xx = f(ee) - {v} }
    { BSetExpression se = new BSetExpression();
      se.addElement(v);
      Vector vars = new Vector();
      String ee = Identifier.nextIdentifier("_ee"); 
      vars.add(ee);
      String xx = Identifier.nextIdentifier("_xx"); 
      vars.add(xx);
      BExpression be = new BBasicExpression(ee);
      BExpression xe = new BBasicExpression(xx);
      BExpression inexp = new BBinaryExpression(":",be,e); 
      BExpression fapp =
        new BApplyExpression(f,be);
      fapp.setMultiplicity(multiplicity);  // f is data
      BExpression un =
        new BBinaryExpression("-",fapp,se);
      BExpression pred =
        new BBinaryExpression("=",xe,un);
      pred = new BBinaryExpression("&",inexp,pred); 
      BExpression sc =
        new BSetComprehension(vars,e,pred);
      BExpression rhs =
        new BBinaryExpression("<+",lhs,sc);
      res = new BAssignStatement(lhs,rhs);
      res.setWriteFrame(f); 
      return res; 
    }
    return new BBasicStatement("skip"); 
  } 

  public static BExpression computeInverseBExpressionSet(Association ast, String role1, String role2, 
                                               int card1, int card2, BExpression var,
                                               BExpression val)
  { BExpression role1be = new BBasicExpression(role1); 
    Entity e1 = ast.getEntity1(); 
    String e1name = e1.getName(); 
    String e1var = e1name.toLowerCase() + "_xx"; 
    Entity e2 = ast.getEntity2(); 
    String e2name = e2.getName(); 
    String e2var = e2name.toLowerCase() + "_xx"; 
    String e2s = e2name.toLowerCase() + "s"; 
    BExpression e2sbe = new BBasicExpression(e2s); 
    Vector e1xset = new Vector(); 
    e1xset.add(var); 
    BExpression e1xsetbe = new BSetExpression(e1xset,false); 

    if (card2 == ModelElement.MANY) 
    { if (card1 == ModelElement.ONE)
      { // role1 := (val <<| role1) \\/ val*{var}
        BExpression rhs1 = new BBinaryExpression("<<|",val,role1be);
        rhs1.setBrackets(true);  
        BExpression rhs2 = new BBinaryExpression("*",val,e1xsetbe); 
        BExpression invrhs = new BBinaryExpression("\\/",rhs1,rhs2); 
        return invrhs; 
      }            
      else  
      { Vector scompvars = new Vector(); 
        scompvars.add(e2var);
        scompvars.add(e1var); 
        BExpression e2varbe = new BBasicExpression(e2var); 
        BExpression e1varbe = new BBasicExpression(e1var); 
        BExpression pred11 = new BBinaryExpression(":",e2varbe,val); 
        BExpression role1app = new BApplyExpression(role1,e2varbe);
        BExpression role1add = new BBinaryExpression("\\/",role1app,e1xsetbe);             
        BExpression pred12 = new BBinaryExpression("=",e1varbe,role1add); 
        BExpression pred1 = new BBinaryExpression("&",pred11,pred12); 
        BExpression comp1 = new BSetComprehension(scompvars,pred1); 
        BExpression role1sub = new BBinaryExpression("-",role1app,e1xsetbe);
        BExpression pred21 = new BBinaryExpression("/:",e2varbe,val);
        BExpression pred22 = new BBinaryExpression(":",e2varbe,e2sbe);
        BExpression pred2 = new BBinaryExpression("&",pred22,pred21);
        BExpression pred23 = new BBinaryExpression("=",e1varbe,role1sub); 
        BExpression comp2 = new BSetComprehension(scompvars,
                                    new BBinaryExpression("&",pred2,pred23)); 
        BExpression invrhs = new BBinaryExpression("\\/", comp1, comp2);        
        return invrhs; 
      } // role1 := { bxx,axx | bxx : val & axx = ar(bxx) - { ax } } \\/ ... 
    }
    else if (card2 == ModelElement.ONE && card1 != ModelElement.ONE)
    { // role1 := { bx |-> ar(bx) \\/ {ax} } \\/ { bb,aa | bb : bs & bb /= bx & aa : ar(bx) - { ax } }
      Vector scompvars = new Vector(); 
      scompvars.add(e2var);
      scompvars.add(e1var); 
      BExpression e2varbe = new BBasicExpression(e2var); 
      BExpression e1varbe = new BBasicExpression(e1var); 
      BExpression pred11 = new BBinaryExpression("=",e2varbe,val); 
      BExpression role1app = new BApplyExpression(role1,e2varbe);
      BExpression role1add = new BBinaryExpression("\\/",role1app,e1xsetbe);             
      BExpression pred12 = new BBinaryExpression("=",e1varbe,role1add); 
      BExpression pred1 = new BBinaryExpression("&",pred11,pred12); 
      BExpression comp1 = new BSetComprehension(scompvars,pred1); 
      BExpression role1sub = new BBinaryExpression("-",role1app,e1xsetbe);
      BExpression pred21 = new BBinaryExpression("/=",e2varbe,val);
      BExpression pred22 = new BBinaryExpression(":",e2varbe,e2sbe);
      BExpression pred2 = new BBinaryExpression("&",pred22,pred21);
      BExpression pred23 = new BBinaryExpression("=",e1varbe,role1sub); 
      BExpression comp2 = new BSetComprehension(scompvars,
                                    new BBinaryExpression("&",pred2,pred23)); 
      BExpression invrhs = new BBinaryExpression("\\/", comp1, comp2);        
      return invrhs; 
    } 
    return null; 
  }  

  public BStatement computeInverseBAdd(Association ast, String role1, String role2, 
                                               int card1, int card2, BExpression var,
                                               BExpression val)
  { BExpression role1be = new BBasicExpression(role1); 
    String e1name = entity.getName(); 
    String e1var = e1name.toLowerCase() + "_xx"; 
    Entity e2 = ast.getEntity2(); 
    String e2name = e2.getName(); 
    String e2var = e2name.toLowerCase() + "_xx"; 
    String e2s = e2name.toLowerCase() + "s"; 
    BExpression e2sbe = new BBasicExpression(e2s); 
    Vector e1xset = new Vector(); 
    e1xset.add(var); 
    BExpression e1xsetbe = new BSetExpression(e1xset,false); 

    if (card1 != ModelElement.ONE) 
    { BExpression invlhs = new BApplyExpression(role1,val);
      Vector invvals = new Vector();
      invvals.add(var);
      BSetExpression role1se = new BSetExpression(invvals,false);
      BExpression invrhs = new BBinaryExpression("\\/",invlhs,role1se); 
      invrhs.setBrackets(true);
      BStatement inversecode = new BAssignStatement(invlhs,invrhs); 
      inversecode.setWriteFrame(role1); 
      return inversecode; 
    } 
    else // role1(nmexbe) := cx
    { BExpression invlhs = new BApplyExpression(role1,val);
      BStatement inversecode = new BAssignStatement(invlhs,var); 
      inversecode.setWriteFrame(role1); 
      return inversecode; 
    } 
  } 


  public BStatement computeInverseBDel(Association ast, String role1, String role2, 
                                               int card1, int card2, BExpression var,
                                               BExpression val)
  { BExpression role1be = new BBasicExpression(role1); 
    String e1name = entity.getName(); 
    String e1var = e1name.toLowerCase() + "_xx"; 
    Entity e2 = ast.getEntity2(); 
    String e2name = e2.getName(); 
    String e2var = e2name.toLowerCase() + "_xx"; 
    String e2s = e2name.toLowerCase() + "s"; 
    BExpression e2sbe = new BBasicExpression(e2s); 
    Vector e1xset = new Vector(); 
    e1xset.add(var); 
    BExpression e1xsetbe = new BSetExpression(e1xset,false); 

    if (card1 != ModelElement.ONE) 
    { BExpression invlhs = new BApplyExpression(role1,val);
      Vector invvals = new Vector();
      invvals.add(var);
      BSetExpression role1se = new BSetExpression(invvals,false);
      BExpression invrhs = new BBinaryExpression("-",invlhs,role1se); 
      invrhs.setBrackets(true);
      BStatement inversecode = new BAssignStatement(invlhs,invrhs); 
      inversecode.setWriteFrame(role1); 
      return inversecode; 
    } 
    else // role1 := role1 domain anti-restriction { nmexbe } 
    { Vector invvals = new Vector();
      invvals.add(val);
      BSetExpression role1se = new BSetExpression(invvals,false);
        
      BExpression invrhs = new BBinaryExpression("<<|",role1se,role1be); 
      BStatement inversecode = new BAssignStatement(role1be,invrhs); 
      inversecode.setWriteFrame(role1); 
      return inversecode; 
    } 
  } 


  public BExpression binvariantForm(java.util.Map env, boolean local)
  { BExpression res; 

    if (umlkind == CLASSID)
    { if (arrayIndex != null)
      { BExpression bind = arrayIndex.binvariantForm(env,local); 
        if (entity != null) 
        { Attribute pk = entity.getPrincipalKey(); 
          BExpression invpk = new BPostfixExpression("~", new BBasicExpression(pk.getName())); 
          if (arrayIndex.isMultiple())
          { res = new BApplySetExpression(invpk,bind); } 
          else 
          { res = new BApplyExpression(invpk,bind); }  
          return res; 
        } 
      } 
      String es = data.toLowerCase() + "s"; 
      res = new BBasicExpression(es); 
      res.setMultiplicity(ModelElement.MANY); 
      return res; 
    } 

    if (umlkind == VALUE)
    { if (data.equals("true"))
      { res = new BBasicExpression("TRUE"); }
      else if (data.equals("false"))
      { res = new BBasicExpression("FALSE"); }
      else if (isSequence(data) || isSet(data))
      { Expression se = buildSetExpression(data); 
        BExpression seqf = se.binvariantForm(env,local); 
        if (arrayIndex != null) 
        { BExpression ind = arrayIndex.binvariantForm(env,local); 
          return new BApplyExpression(seqf,ind); 
        } 
        return seqf; 
      } 
      else if (isString(data))
      { BExpression bexp = new BBasicExpression(data); 
        if (arrayIndex != null)
        { BExpression ind = arrayIndex.binvariantForm(env,local); 
          return new BApplyExpression(bexp,ind); 
        } 
        return bexp; 
      }
      res = new BBasicExpression(data); 
      res.setKind(VALUE); 
      return res; 
    }
     

    if (umlkind == VARIABLE || umlkind == CONSTANT)
    { if (data.equals("self"))
      { BExpression var = findBContextVariable(env,entity); 
        return var; 
      }
      res = new BBasicExpression(data); 
      res.setKind(umlkind); 
      if (objectRef != null)
      { BExpression obj = objectRef.binvariantForm(env,local);
        if (objectRef.isMultiple())
        { BExpression res2 = new BApplySetExpression(res,obj); 
          return res2;
        } 
        else 
        { return new BApplyExpression(res,obj); }  
      } 
      return res; 
    } 

    if (umlkind == FUNCTION)
    { if (objectRef == null) 
      { return null; } // invalid function
      BExpression pre = objectRef.binvariantForm(env,local);
      String op;
      if (data.equals("size")) // also strings
      { op = "card";
        return new BUnaryExpression(op,pre);
      }
      else if (data.equals("sqr"))
      { return new BBinaryExpression("*",pre,pre); } 
      else if (data.equals("Sum"))
      { Expression par1 = (Expression) parameters.get(0); 
        Expression par2 = (Expression) parameters.get(1); 
        Expression par3 = (Expression) parameters.get(2); 
        Expression par4 = (Expression) parameters.get(3);
        BExpression low = par1.binvariantForm(env,local);
        BExpression upp = par2.binvariantForm(env,local);
        BExpression ind = par3.binvariantForm(env,local);
        BExpression exp = par4.binvariantForm(env,local);
        BExpression rang = new BBinaryExpression("..", low, upp); 
        BExpression pred = new BBinaryExpression(":", ind, rang); 
        return new BSigmaExpression(par3 + "", pred, exp); 
      } 
      else if (data.equals("Prd"))
      { Expression par1 = (Expression) parameters.get(0); 
        Expression par2 = (Expression) parameters.get(1); 
        Expression par3 = (Expression) parameters.get(2); 
        Expression par4 = (Expression) parameters.get(3);
        BExpression low = par1.binvariantForm(env,local);
        BExpression upp = par2.binvariantForm(env,local);
        BExpression ind = par3.binvariantForm(env,local);
        BExpression exp = par4.binvariantForm(env,local);
        BExpression rang = new BBinaryExpression("..", low, upp); 
        BExpression pred = new BBinaryExpression(":", ind, rang); 
        return new BPiExpression(par3 + "", pred, exp); 
      } 
      else if (data.equals("abs") || data.equals("log") || data.equals("sin") ||
          data.equals("cos") || data.equals("tan") ||
          data.equals("sqrt") || data.equals("max") || data.equals("floor") ||
          data.equals("min") || data.equals("exp") || data.equals("round") ||
          data.equals("ceil") || data.equals("asin") || data.equals("acos") ||
          data.equals("atan") || data.equals("sinh") || data.equals("cosh") ||
          data.equals("tanh") || data.equals("log10") || data.equals("cbrt"))
      { op = data;
        return new BUnaryExpression(op,pre);
      } // for sets.func  a set comprehension
      else if (data.equals("closure"))
      { if (objectRef instanceof BasicExpression)
        { BasicExpression beref = (BasicExpression) objectRef; 
          String rel = beref.data; 
          // the relation being closured
          BExpression closureexp = new BApplyExpression("closure", new BBasicExpression(rel)); 

          if (beref.objectRef != null) 
          { BExpression objs = 
              beref.objectRef.binvariantForm(env,local); 
            return new BApplyExpression(closureexp,objs); 
          }
          else 
          { BExpression var = findBContextVariable(env,objectRef.entity); 
            return new BApplyExpression(closureexp,var); 
          } 
        } 
        BExpression arg = objectRef.binvariantForm(env,local); 
        return new BUnaryExpression("closure",arg); 
      }
      else if (data.equals("any"))
      { pre.setBrackets(true); 
        return new BApplyExpression(pre,new BBasicExpression("1"));
      } 
      else if (data.equals("subcollections"))
      { return new BUnaryExpression("FIN",pre); }  // But not if objectRef is ordered
      else if (data.equals("asSet"))
      { if (objectRef.isOrderedB())
        { return new BUnaryExpression("ran",pre); }  // Only if objectRef is ordered 
        else 
        { return pre; } 
      }  // asSequence???
      else if (data.equals("oclIsKindOf") && parameters != null && parameters.size() > 0)
      { Expression par1 = (Expression) parameters.get(0); 
        String par1s = par1 + ""; 
        BExpression rightobjs = new BBasicExpression(par1s.toLowerCase() + "s"); 
        return new BBinaryExpression(":", pre, rightobjs);
      } 
      else if (data.equals("oclAsType"))  
      { return pre; } 
      else if (data.equals("sum")) // sum
      { String xx = Identifier.nextIdentifier("_xx"); 
        BExpression xxbe = new BBasicExpression(xx);
        BExpression pred =
          new BBinaryExpression(":",xxbe,pre);
        return new BSigmaExpression(xx,pred,xxbe);
      }
      else if (data.equals("prd")) // product
      { String xx = Identifier.nextIdentifier("_xx"); 
        BExpression xxbe = new BBasicExpression(xx);
        BExpression pred =
          new BBinaryExpression(":",xxbe,pre);
        return new BPiExpression(xx,pred,xxbe);
      }
      else if (data.equals("reverse"))
      { return new BUnaryExpression("rev",pre); } 
      else if (data.equals("indexOf"))
      { if (parameters != null && parameters.size() > 0)
        { BExpression ber = 
            ((Expression) parameters.get(0)).binvariantForm(env,local);
          BSetExpression img = new BSetExpression(); 
          img.addElement(ber); 
          BExpression func = new BPostfixExpression("~",pre); 
          res = new BApplySetExpression(func,img); 
          res = new BUnaryExpression("min",res); 
          return res; 
        } 
      }   
      else if (data.equals("count"))
      { if (parameters != null && parameters.size() > 0)
        { BExpression ber = 
            ((Expression) parameters.get(0)).binvariantForm(env,local);
          BSetExpression img = new BSetExpression(); 
          img.addElement(ber); 
          BExpression func = new BPostfixExpression("~",pre); 
          res = new BApplySetExpression(func,img); 
          res = new BUnaryExpression("card",res); 
          return res; 
        } 
      }   
      else if (data.equals("subrange"))
      { if (parameters != null && parameters.size() > 1)
        { BExpression bstart = 
            ((Expression) parameters.get(0)).binvariantForm(env,local);
          BExpression bend = 
            ((Expression) parameters.get(1)).binvariantForm(env,local);
          bstart.setBrackets(true); 
          bend.setBrackets(true); 
          if ("Integer".equals(objectRef + ""))
          { return new BBinaryExpression("..", bstart, bend); } 

          BBinaryExpression bstartminus1 = 
            new BBinaryExpression("-",bstart,new BBasicExpression("1")); 
          bstartminus1.setBrackets(true); 
          BBinaryExpression trimend = 
            new BBinaryExpression("/|\\",pre,bend); 
          trimend.setBrackets(true); 
          return new BBinaryExpression("\\|/",trimend,bstartminus1);
        }  
      }                
      else if (data.equals("insertAt"))  
      { if (parameters != null && parameters.size() > 1)
        { BExpression bpos = 
            ((Expression) parameters.get(0)).binvariantForm(env,local);
          BExpression obj = 
            ((Expression) parameters.get(1)).binvariantForm(env,local);
          BBinaryExpression bposminus1 = 
            new BBinaryExpression("-",bpos,new BBasicExpression("1")); 
          bposminus1.setBrackets(true); 
          BBinaryExpression trimend = 
            new BBinaryExpression("/|\\",pre,bposminus1); 
          trimend.setBrackets(true);
          BSetExpression objseq = new BSetExpression(); 
          objseq.addElement(obj); 
          objseq.setOrdered(true); 
          BBinaryExpression trimstart = 
            new BBinaryExpression("\\|/",pre,bposminus1); 
          trimstart.setBrackets(true);  
          return new BBinaryExpression("^",trimend,
                        new BBinaryExpression("^",objseq,trimstart));
        }  
      }                

      else // last, first, front, tail - 1st 2 not quite correct for strings
      { return new BUnaryExpression(data,pre); } 
    }

    System.out.println("*** Binvar form: " + this + " (multiple = " + isMultiple() + " ordered = " + isOrderedB() + ")"); 

    if (objectRef == null)
    { if (umlkind == ATTRIBUTE || umlkind == ROLE)
      { if (entity.isClassScope(data))     // local means a local par?
        { if (arrayIndex != null) 
          { return new BApplyExpression(data,
                         arrayIndex.binvariantForm(env,local)); 
          }
          return new BBasicExpression(data); 
        } 
        // String ename = entity.getName(); 
        BExpression var = findBContextVariable(env,entity); 
        res = new BApplyExpression(data,var);   // Assume that var is single-valued
        res.setMultiplicity(multiplicity); 
        if (arrayIndex != null) 
        { res.setBrackets(true); 
          res = new BApplyExpression(res,
                      arrayIndex.binvariantForm(env,local)); 
        }
        res.setKind(umlkind); 
        return res; 
      }
      else if (umlkind == QUERY)
      { Vector bpars = new Vector(); 
        BExpression var = findBContextVariable(env,entity); 
        Vector prepars = new Vector(); 
        prepars.add(var); 
        BBasicExpression bres = new BBasicExpression(data + "_" + entity,prepars); 
        if (parameters != null)
        { for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
            BExpression bpar = par.binvariantForm(env,true); 
            bpars.add(bpar); 
          }
        }
        return new BApplyExpression(bres,bpars);  
      } // may have array index
    }
    else // objectRef != null
    { BExpression obj = objectRef.binvariantForm(env,local);
      if (entity != null && entity.isClassScope(data))     // local means a local par?
      { if (arrayIndex != null) 
        { return new BApplyExpression(data,
                         arrayIndex.binvariantForm(env,local)); 
        }
        return new BBasicExpression(data); 
      } // also for functions
      if (umlkind == QUERY)
      { Vector bpars = new Vector(); 
        if (parameters != null)
        { for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
            BExpression bpar = par.binvariantForm(env,true); 
            bpars.add(bpar); 
          }
        }
        Vector prepars = new Vector(); 
        prepars.add(obj); 
        BBasicExpression bres = new BBasicExpression(data + "_" + entity,prepars); 
        if (bpars.size() > 0)
        { if (objectRef.isMultiple())  // { op(ex)(pars) | ex : obj } 
          { Vector vars = new Vector(); 
            String xx = Identifier.nextIdentifier("xx"); 
            BExpression xxbe = new BBasicExpression(xx);
            Vector prepars1 = new Vector(); 
            prepars1.add(xxbe); 
            BBasicExpression bres1 = 
              new BBasicExpression(data + "_" + entity,prepars1); 
            vars.add(new BApplyExpression(bres1,bpars)); 
            BBinaryExpression pred1 = new BBinaryExpression(":",xxbe,obj);
             
            BExpression comp = new BSetComprehension(vars,pred1); 
            return comp; 
          }  
          else 
          { return new BApplyExpression(bres,bpars); }
        }  
        else 
        { if (objectRef.isMultiple())
          { return new BApplySetExpression(
              new BBasicExpression(data + "_" + entity),obj); 
          } 
          else 
          { return bres; } 
        }  
      } // may have array index. What about obj?
      else if (objectRef.isMultiple())
      { if (isOrderedB())  // seq-role or 1-role/attribute applied to seq objectRef
        { BExpression comp = new BBinaryExpression(";",obj,
                                                   new BBasicExpression(data));
          comp.setBrackets(true); 
          if (umlkind == ATTRIBUTE || multiplicity == ModelElement.ONE)
          { res = comp; } 
          else // seq role applied to seq object Ref
          { res = new BUnaryExpression("conc",comp); }
        } 
        else  // object ref is a set, or role is set-valued 
        { BApplySetExpression app = new BApplySetExpression(data,obj);
          // app.setMultiplicity(ModelElement.MANY); 
          if (umlkind == ATTRIBUTE || multiplicity == ModelElement.ONE)  
          { res = app; }  // object ref is a set
          else if (objectRef.isOrderedB()) // role is a set 
          { BUnaryExpression ranobjref = new BUnaryExpression("ran", obj);
            res = new BApplySetExpression(data,ranobjref);
          }  
          else // role is a sequence, and applied to a set, to produce a set!  
          { BUnaryExpression unionexp = new BUnaryExpression("union", app); 
            res = new BUnaryExpression("ran", unionexp); 
          } 
        }
        if (arrayIndex != null) // the current expression must be a sequence or string
        { res = new BApplyExpression(res,
                      arrayIndex.binvariantForm(env,local)); 
        }
        res.setKind(umlkind); 
        System.out.println("B INVARIANT FORM OF " + this + " IS " + res); 
        return res; 
      } 
      else
      { BApplyExpression app = new BApplyExpression(data,obj);  // not null?
        app.setMultiplicity(multiplicity);
        if (arrayIndex != null) 
        { app = new BApplyExpression(app,
                      arrayIndex.binvariantForm(env,local)); 
        }
        app.setKind(umlkind); 
        return app; 
      }
    }
    res = new BBasicExpression(data);
    res.setKind(umlkind); 
    System.out.println(">> B INVARIANT FORM OF " + this + " IS " + res); 

    return res;  // and setKind in all other cases, as well.
  }

  public boolean isMultiple()
  { // Multiple if objectRef or arrayIndex are multiple, or if 
    // a Set, Sequence or multi-valued role or variable. Otherwise not.
 
    if (arrayIndex == null)  // depends only on data itself
    { if (umlkind == ROLE &&
          multiplicity != ModelElement.ONE)
      { return true; } 

      if (umlkind == CLASSID) 
      { return true; } 

      if (umlkind == VARIABLE) 
      { return Type.isCollectionType(type); } 

      if (objectRef != null)
      { if ("last".equals(data) || "first".equals(data) ||
            "max".equals(data) || "min".equals(data) || "any".equals(data))
        { Type argelemtype = objectRef.getElementType(); 
          return (argelemtype != null && argelemtype.isCollectionType());  
        } 
        if (objectRef.isMultiple()) 
        { if (data.equals("size") || data.equals("Sum") || data.equals("Prd") || 
              data.equals("sum") || data.equals("prd") || 
              data.equals("count") || data.equals("indexOf") || data.equals("isDeleted"))
          { return false; }
          else if (entity != null && entity.isStaticFeature(data) &&
                   objectRef.umlkind == CLASSID && (objectRef instanceof BasicExpression) && 
                   ((BasicExpression) objectRef).arrayIndex == null)
          { return type.isMultiple(); } 
          else 
          { return true; }
        } 
      } 

      if (umlkind == FUNCTION) 
      { if (data.equals("closure") || data.equals("asSet") || data.equals("subcollections") || 
            // data.equals("front") || data.equals("tail") || data.equals("reverse") || 
            data.equals("characters") || data.equals("sort")) 
        { return true; }  // or object ref multiple
        if (data.equals("size") || data.equals("max") || data.equals("min") ||
            data.equals("Sum") || data.equals("Prd") || 
            data.equals("sum") || data.equals("indexOf") ||
            data.equals("count") || data.equals("prd") || data.equals("isDeleted"))
        { return false; } 
        if (data.equals("first") || data.equals("last") || data.equals("any"))
        { return Type.isCollectionType(objectRef.elementType); } 
      } 

     
      if (type == null) 
      { System.out.println("!Warning: null type in: " + data); 
        // JOptionPane.showMessageDialog(null, "Null type in: " + this, 
        // "Error", JOptionPane.ERROR_MESSAGE); 
        return false; 
      }

      if (type.getName().equals("String"))
      { return false; } 

      if (type.getName().equals("Set") ||
          type.getName().equals("Sequence")) 
      { return true; } 

      if (data.equals("front") || data.equals("tail"))
      { return true; }   // Must be a sequence

      if (multiplicity != ModelElement.ONE) 
      { return true; }

      return false; 
    }  
    else // arrayIndex != null  
    { if (isQualified()) 
      { return multiplicity != ModelElement.ONE; } 

      if (Type.isCollectionType(elementType))
      { return true; } 

      if (umlkind == CLASSID)
      { return arrayIndex.isMultiple(); } 

      return false;  
    }    
  } // arrayIndex != null: reduces cardinality to 1 or 0..1, unless it is also
    // multiple



  public Expression createActionForm(final Vector sms)
  { /* actionForm = this; */ 
    buildJavaForm(sms); 
    return this;   // for multiples, is data(objectRef) ?? 
  }

  public Expression substitute(Expression oldE,
                               Expression newE)
  { if (oldE == this)
    { return newE; } 
    BasicExpression res = (BasicExpression) clone(); 
    if (objectRef != null) 
    { Expression newobref = objectRef.substitute(oldE,newE); 
      res.objectRef = newobref; 
    }
    if (arrayIndex != null)
    { Expression newind = arrayIndex.substitute(oldE,newE); 
      res.arrayIndex = newind; 
    }
    if (parameters != null) 
    { Vector newpars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        newpars.add(par.substitute(oldE,newE)); 
      } 
      res.parameters = newpars; 
    }  

    return res; 
  }  

  public Expression substituteEq(final String oldVar, 
                                 final Expression newVal)
  { if (toString().equals(oldVar))
    { return (Expression) newVal.clone(); }

    BasicExpression res = (BasicExpression) clone();

    Expression newind = null; 
    if (arrayIndex != null)
    { newind = arrayIndex.substituteEq(oldVar,newVal); 
      res.arrayIndex = newind; 
    }

    Vector newpars = new Vector();   
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        newpars.add(par.substituteEq(oldVar,newVal)); 
      } 
      res.parameters = newpars; 
    }  

    if (objectRef == null)
    { if (prestate)
      { return res; }
      if (data.equals(oldVar))
      { Expression res1 = (Expression) newVal.clone();
        if (res1 instanceof BasicExpression) 
        { BasicExpression res1be = (BasicExpression) res1; 
          res1be.parameters = newpars; 
          res1be.arrayIndex = newind; 
          return res1be; 
        }  // discards other updates
        return res1; 
      } 
      return res;
    }

    res.objectRef =
      objectRef.substituteEq(oldVar,newVal);
    
    /* New, untested: */ 
    if (data.equals(oldVar))
    { if (newVal instanceof BasicExpression) 
      { BasicExpression newData = (BasicExpression) newVal.clone(); 
        if (newData.objectRef != null) 
        { newData.setInnerObjectRef(res.objectRef); 
          return newData; 
        } 
        else
        { newData.objectRef = res.objectRef; 
          return newData; 
        }  
      } 
    } 
    return res;
  }  // again, not if prestate

  public Expression removeSlicedParameters(
             BehaviouralFeature op, Vector fpars)
  { // op(parameters) becomes op(pars) where
    // pars are the parameters *not* in the range of 
    // the removed formal parameters fpars of op
    // op is original version of 
    // operation before slicing. 

    BasicExpression res = (BasicExpression) clone();

    Expression newind = null; 
    if (arrayIndex != null)
    { newind = arrayIndex.removeSlicedParameters(op,fpars); 
      res.arrayIndex = newind; 
    }

    Vector oldpars = new Vector();   
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        oldpars.add(par.removeSlicedParameters(op,fpars)); 
      } 
      res.parameters = oldpars; 
    }  

    if (objectRef == null)
    { }
    else 
    { res.objectRef =
        objectRef.removeSlicedParameters(op,fpars);
    }

    if (data.equals(op.getName()) && 
        (umlkind == QUERY || umlkind == UPDATEOP))
    { Vector newpars = new Vector(); 
      Vector oppars = op.getParameters(); 
      for (int i = 0; i < oppars.size(); i++) 
      { Attribute att = (Attribute) oppars.get(i); 
        if (fpars.contains(att.getName()))
        { System.out.println("++ Removing parameter " + att); } 
        else 
        { newpars.add(oldpars.get(i)); } 
      } 
      res.parameters = newpars; 
    } 

    return res;
  }  

  public Expression removePrestate()
  { 
    BasicExpression res = (BasicExpression) clone();

    res.prestate = false; 

    if (arrayIndex != null)
    { Expression newind = arrayIndex.removePrestate(); 
      res.arrayIndex = newind; 
    }

    Vector newpars = new Vector();   
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        newpars.add(par.removePrestate()); 
      } 
      res.parameters = newpars; 
    }  

    if (objectRef == null)
    { }  
    else 
    { res.objectRef =
        objectRef.removePrestate();
    } 

    res.setType(type); 
    res.setElementType(elementType); 
       
    return res;
  }  

  public Expression simplify(final Vector vars) 
  { return simplify(); }

  public Expression simplify() 
  { if (umlkind == FUNCTION)
    { if (objectRef == null)
      { return this; }
      Expression ors = objectRef.simplify(); 

      // many cases, eg, 2.sqr as 4, etc

      if (data.equals("last") && 
          (ors instanceof SetExpression))
      { SetExpression se = (SetExpression) ors; 
        return se.getLastElement(); 
      } 
      else if (data.equals("first") && 
               (ors instanceof SetExpression))
      { SetExpression se = (SetExpression) ors; 
        return se.getFirstElement(); 
      }
      else if (data.equals("size") && 
               (ors instanceof SetExpression))
      { SetExpression se = (SetExpression) ors; 
        return new BasicExpression(se.size()); 
      }
      else if (data.equals("subrange") && 
         ors instanceof BasicExpression && 
         ((BasicExpression) ors).objectRef != null &&
         ((BasicExpression) ors).data.equals("subrange"))
      { // e.subrange(a,b).subrange(c,d) is 
        // e.subrange(a+c-1, a+d-1)
        
        BasicExpression objref = (BasicExpression) ors; 
        Expression a = (Expression) objref.parameters.get(0); 
        Expression c = (Expression) parameters.get(0);  
        Expression newA = 
            Expression.simplifyPlus(a, c);
        Expression newAsimp = 
            Expression.simplifyMinus(newA, 
                                 new BasicExpression(1));
        Vector pars = new Vector(); 
        pars.add(newAsimp); 
        BasicExpression res = 
          newFunctionBasicExpression("subrange", 
                                     objref.objectRef,
                                     pars); 
        if (parameters.size() > 1)
        { Expression d = (Expression) parameters.get(1); 
          Expression newB = 
            Expression.simplifyPlus(a, d); 
          Expression newBsimp = 
            Expression.simplifyMinus(newB, new BasicExpression(1));   
          pars.add(newBsimp); 
        } 
        return res; 
      }

      objectRef = ors; 
      return this;  
    }       

    if (arrayIndex == null) { return this; } 

    Expression ai = arrayIndex.simplify(); 
    if (isNumber("" + ai) && isString(data))
    { int i = Integer.parseInt("" + ai); 
      String ss = data.substring(i-1,i);
      BasicExpression res = new BasicExpression("\"" + ss + "\""); 
      return res; 
      // and type check it?
    }  
    if (isNumber("" + ai) && isSequence(data))
    { int i = Integer.parseInt("" + ai); 
      SetExpression se = (SetExpression) buildSetExpression(data); 
      Expression res = se.getElement(i-1); 
      return res; 
      // and type check it?
    }  
    if (objectRef == null)
    { return this; }
    Expression ors = objectRef.simplify(); 
    objectRef = ors; 
    if (isNumber("" + ai) && (ors instanceof SetExpression))
    { int i = Integer.parseInt("" + ai); 
      Expression ob = ((SetExpression) ors).getElement(i-1); 
      BasicExpression res = new BasicExpression(data); 
      res.objectRef = ob;  // and copy the object ref of ors? 
      return res; 
    } 
    arrayIndex = ai; 
    return this; 
  }

  public Vector getBaseEntityUses()
  { Vector res = new Vector();
    if (objectRef == null) 
    { if (umlkind == ROLE || umlkind == QUERY || umlkind == ATTRIBUTE)
      { if (entity != null) 
        { if (entity.isStaticFeature(data)) 
          { System.err.println("!! ERROR: Static feature should have class name objectref: " + this); }  
            // must have class name as objectRef 
          else 
          { res.add(entity); }
        } 
      } 
      else if ("self".equals(data) && entity != null)
      { res.add(entity); } 
    }
    else 
    { res.addAll(objectRef.getBaseEntityUses()); } 
 
    if (arrayIndex != null)
    { res = VectorUtil.union(res,arrayIndex.getBaseEntityUses()); }

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = VectorUtil.union(res,par.getBaseEntityUses()); 
      } 
    } 

    return res;
  } // and recursively also for parameters of operation calls 

  public boolean hasVariable(final String s)
  { return toString().equals(s); }

  /* allVariablesUsedIn */ 
  public Vector getVariableUses()
  { Vector res = new Vector();

    if (("Sum".equals(data) || "Prd".equals(data)) && 
        "Integer".equals(objectRef + "") && parameters != null && parameters.size() > 3)
    { Expression par1 = (Expression) parameters.get(0); 
      Expression par2 = (Expression) parameters.get(1);
      Expression par3 = (Expression) parameters.get(2); 
      Expression par4 = (Expression) parameters.get(3);
      res.addAll(par1.getVariableUses()); 
      res.addAll(par2.getVariableUses()); 
      Vector ss = par4.getVariableUses(); 
      Vector removals = new Vector(); 
      for (int i = 0; i < ss.size(); i++) 
      { Expression e1 = (Expression) ss.get(i); 
        if ((e1 + "").equals(par3 + ""))
        { removals.add(e1); } 
      } 
      ss.removeAll(removals);
      res.addAll(ss);
      return res; 
    }  

    if (umlkind == VARIABLE && parameters == null)  // it is a true variable
    { if (variable != null) 
      { res.add(new BasicExpression(variable)); }  // ignoring arrayIndex
      else 
      { res.add(this); }
    } 

    if (objectRef != null)
    { res.addAll(objectRef.getVariableUses()); }

    if (arrayIndex != null)
    { res = VectorUtil.union(res,arrayIndex.getVariableUses()); }

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.getVariableUses()); 
      } 
    } 

    return res;
  } 

  public Vector getAssignedVariableUses()
  { Vector res = new Vector();

    if (umlkind == VARIABLE && parameters == null)  // it is a true variable
    { if (variable != null) 
      { res.add(new BasicExpression(variable)); }  // ignoring arrayIndex
      else 
      { res.add(this); }
    } 

    if (objectRef != null && (objectRef instanceof BasicExpression))
    { res.addAll(((BasicExpression) objectRef).getAssignedVariableUses()); }

    return res;
  } 

  public Vector getUses(String feature)
  { Vector res = new Vector();
    if (data.equals(feature))
    { res.add(this); }
    else if (objectRef != null)
    { res = objectRef.getUses(feature); }
    if (arrayIndex != null)
    { res.addAll(arrayIndex.getUses(feature)); } 
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.getUses(feature)); 
      } 
    } 

    return res;
  } // and from arrayIndex


  public Vector allFeaturesUsedIn()
  { Vector res = new Vector();
    if (umlkind == ATTRIBUTE || umlkind == ROLE)
    { res.add(data); }
    if (objectRef != null)
    { res.addAll(objectRef.allFeaturesUsedIn()); }
    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allFeaturesUsedIn()); } 
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allFeaturesUsedIn()); 
      } 
    } 
    return res;
  } // and from arrayIndex

  public Vector allAttributesUsedIn()
  { Vector res = new Vector();
    if ("self".equals(this + "")) 
    { return res; } 
    if ("super".equals(this + ""))
    { return res; } 

    if (umlkind == ATTRIBUTE || umlkind == ROLE)
    { Vector path = getAttributePath(); 
      res.add(new Attribute(path)); 
    }
    else if (objectRef != null)
    { res.addAll(objectRef.allAttributesUsedIn()); }

   /*
    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allFeaturesUsedIn()); } 
   */ 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allAttributesUsedIn()); 
      } 
    }

    return res;
  } // and from arrayIndex

  private Vector getAttributePath()
  { Attribute att = new Attribute(this); 
    if (objectRef != null && (objectRef instanceof BasicExpression) && 
        !("self".equals(objectRef + ""))) 
    { Vector path = ((BasicExpression) objectRef).getAttributePath(); 
      path.add(att); 
      return path; 
    } 
    Vector res = new Vector(); 
    res.add(att); 
    return res; 
  } 

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    if (umlkind == UPDATEOP || umlkind == QUERY || isEvent)
    { res.add(entity + "::" + data); }
    if (objectRef != null)
    { res.addAll(objectRef.allOperationsUsedIn()); }
    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allOperationsUsedIn()); } 
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allOperationsUsedIn()); 
      } 
    } 
    return res;
  } 

  public boolean isSelfCall(BehaviouralFeature bf)
  { String nme = bf.getName(); 
    return isSelfCall(nme); 
  } 

  public boolean isSelfCall(String nme)
  { if (data.equals(nme) && 
        "self".equals(objectRef + "") && 
        (umlkind == UPDATEOP || umlkind == QUERY ||
         isEvent)) 
    { return true; } 
    return false;  
  }

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    if ("equivalent".equals(data))
    // { res.add(entity + "::" + data); }
    { res.add(objectRef); } 

    if (objectRef != null)
    { res.addAll(objectRef.equivalentsUsedIn()); }
    if (arrayIndex != null) 
    { res.addAll(arrayIndex.equivalentsUsedIn()); } 
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.equivalentsUsedIn()); 
      } 
    } 
    return res;
  } // and from arrayIndex

  public Vector allValuesUsedIn()
  { Vector res = new Vector(); 
    if (umlkind == VALUE)
    { res.add(data); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res.addAll(par.allValuesUsedIn()); 
      } 
    } 

    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allValuesUsedIn()); } 

    if (objectRef != null) 
    { res.addAll(objectRef.allValuesUsedIn()); } 

    return res; 
  } 

  public Vector allBinarySubexpressions()
  { return new Vector(); } 

  public Vector componentsUsedIn(final Vector sms)
  { Vector res = new Vector();
    int cat = Statemachine.NONE;
    Statemachine sm = null;

    for (int i = 0; i < sms.size(); i++)
    { sm = (Statemachine) sms.get(i);
      cat = sm.getCategory(data);
      if (cat == Statemachine.STATEMACHINE || 
          cat == Statemachine.ATTRIBUTE ||
          cat == Statemachine.EVENT)
      { res.add(sm);
        return res;
      }
    }
    return res;
  }

  public Expression addPreForms(String var)
  { Expression arrayInd = arrayIndex; 
    Expression objRef = objectRef;
    BasicExpression res = (BasicExpression) clone();  

    if (arrayIndex != null) 
    { arrayInd = arrayIndex.addPreForms(var); } 

    if (objectRef != null) 
    { if ((objectRef + "").equals(var))
      { res.prestate = true; } 
      else 
      { objRef = objectRef.addPreForms(var); }  // and add to the parameters, etc
    } 

    if (parameters != null) 
    { Vector newpars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        Expression newpar = par.addPreForms(var);  
        newpars.add(newpar); 
      }
      res.parameters = newpars;  
    } 

    res.objectRef = objRef; 
    res.arrayIndex = arrayInd; 
    return res; 
  } 

  public Vector allReadFrame()
  { return readFrame(); } 

  public Vector readFrame()
  { Vector res; 
    String ename = ""; 

    // System.out.println("||| " + this + " is " + umlkind + " " + entity); 

    if ("equivalent".equals(data) && objectRef != null)
    { res = objectRef.readFrame(); 
      res.add("$IN::$trace"); 
      res.add("$Trace::target"); 
      return res; 
    } 

    if (objectRef != null) 
    { res = objectRef.readFrame();
      Type t = objectRef.elementType; 
      if (t != null && t.entity != null)
      { ename = t.entity.getName() + "::"; }
      else if (entity != null) 
      { ename = entity.getName() + "::"; } 
    } 
    else 
    { res = new Vector(); } 

    if (umlkind == ROLE || umlkind == ATTRIBUTE)
    { if (ename.length() == 0 && entity != null)
      { ename = entity + "::"; }  
      if (prestate) 
      { res.add(ename + data + "@pre"); } 
      else 
      { res.add(ename + data); } 
    } 
    else if (umlkind == CLASSID)
    { if (prestate) 
      { res.add(data + "@pre"); } 
      else 
      { res.add(data); }  

      if (entity != null) 
      { Vector allsubs = entity.getAllSubclasses(); 
        for (int j = 0; j < allsubs.size(); j++) 
        { String subent = ((Entity) allsubs.get(j)).getName(); 
          if (prestate)
          { res.add(subent + "@pre"); }
          else
          { res.add(subent); } 
        } 
      }   
    }  
    else if (umlkind == QUERY || umlkind == UPDATEOP) 
    { if (entity != null) 
      { BehaviouralFeature op = 
            entity.getDefinedOperation(data,parameters); 
        if (op != null) 
        { res.addAll(op.getReadFrame()); }
      }  
    }  

    if (arrayIndex != null) 
    { res.addAll(arrayIndex.readFrame()); } 

    if (parameters != null) 
    { for (int j = 0; j < parameters.size(); j++) 
      { Expression par = (Expression) parameters.get(j); 
        res.addAll(par.allReadFrame()); 
      } 
    } 

    return res; 
  } 

  public Vector wr(Vector assocs)
  { Vector res = new Vector(); 
    String ename = ""; 
     
    if (prestate) { return res; } 

    if ("equivalent".equals(data))
    { res.add("$IN::$trace"); 
      res.add("$Trace::target"); 
      res.add("$Trace"); 
      return res; 
    } 

    if (objectRef != null) 
    { Type t = objectRef.elementType; 
      if (t != null && t.entity != null)
      { ename = t + "::"; }
      else if (entity != null) 
      { ename = entity.getName() + "::"; } 
    }

    if (umlkind == ROLE || umlkind == ATTRIBUTE)
    { if (ename.length() == 0)
      { ename = entity + "::"; }  
      res.add(ename + data); 
      if (umlkind == ROLE && entity != null)  // also add opposite end if there is one
      { String opp = Association.getOppositeEnd(data, entity, assocs); 
        if (opp != null) 
        { res.add(opp); } 
      } 
    }
    else if (umlkind == CLASSID)
    { res.add(data);
      /* if (entity != null) 
      { Vector allsubs = entity.getAllSuperclasses(); 
        for (int j = 0; j < allsubs.size(); j++) 
        { res.add(((Entity) allsubs.get(j)).getName()); }
      }  */  
    } // and all superclasses
    else if (umlkind == UPDATEOP && entity != null) 
    { BehaviouralFeature op = entity.getDefinedOperation(data,parameters); 
      if (op != null) 
      { res.addAll(op.getWriteFrame(assocs)); } 
    }  
 
    return res; 
  } 

  public String updatedData()
  { // The affected item in a[i] := v
    // or a.op(pars)

    String res = null; 
    String objname = ""; 
     
    if (prestate) 
    { return res; } 

    if ("equivalent".equals(data))
    { return res; } 

    if (objectRef != null) 
    { objname = objectRef + ""; }

    if (umlkind == ROLE || umlkind == ATTRIBUTE || 
        umlkind == VARIABLE)
    { return objname + data; }
    else if (umlkind == CLASSID)
    { return objname + data; } 
    else if (umlkind == UPDATEOP || umlkind == QUERY) 
    { // BehaviouralFeature op = entity.getDefinedOperation(data,parameters); 
      // if (op != null) 
      // { res.addAll(op.getWriteFrame(assocs)); } 
      return objname; 
    }  
 
    return res; 
  } 

  public Vector readData()
  { // The index in a[i] := v
    // or pars data in a.op(pars)

    Vector res = new Vector(); 
     
    if (prestate) 
    { return res; } 

    if ("equivalent".equals(data))
    { return res; } 

    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allReadData()); }

    // if (umlkind == ROLE || umlkind == ATTRIBUTE || 
    //     umlkind == VARIABLE || 
    //     umlkind == CLASSID)
    // { return res; }
    // else if (umlkind == UPDATEOP || umlkind == QUERY) 
    // { 
      if (parameters != null) 
      { for (int i = 0; i < parameters.size(); i++) 
        { Expression par = (Expression) parameters.get(i);
          Vector parvars = par.allReadData();  
          res = VectorUtil.union(res,parvars); 
        } 
      }  
    // }  
 
    return res; 
  } 

  public Vector readBasicExpressionData()
  { // All read basic expressions including arguments

    Vector res = new Vector(); 
     
    if (prestate) 
    { return res; } 

    if ("equivalent".equals(data))
    { return res; } 

    if (arrayIndex != null) 
    { res.addAll(arrayIndex.allReadBasicExpressionData()); }

    // if (umlkind == ROLE || umlkind == ATTRIBUTE || 
    //     umlkind == VARIABLE || 
    //     umlkind == CLASSID)
    // { return res; }
    // else if (umlkind == UPDATEOP || umlkind == QUERY) 
    // { 
      if (parameters != null) 
      { for (int i = 0; i < parameters.size(); i++) 
        { Expression par = (Expression) parameters.get(i);
          Vector parvars = par.allReadBasicExpressionData();  
          res = VectorUtil.union(res,parvars); 
        } 
      }  
    // }  
 
    return res; 
  } 

  public Vector allReadBasicExpressionData()
  { // All read basic expressions including arguments

    Vector res = new Vector(); 
    
    if (prestate) 
    { return res; } 

    if ("equivalent".equals(data))
    { return res; } 

    // if (arrayIndex != null) 
    // { res.addAll(
    //     arrayIndex.allReadBasicExpressionData()); }

    if (umlkind == ROLE || umlkind == ATTRIBUTE || 
        umlkind == VARIABLE || 
        umlkind == CLASSID)
    { res.add(this); 
      return res; 
    }
    else if (umlkind == UPDATEOP || umlkind == QUERY) 
    { res.add(this); 
      if (parameters != null) 
      { for (int i = 0; i < parameters.size(); i++) 
        { Expression par = (Expression) parameters.get(i);
          Vector parvars = par.allReadBasicExpressionData();  
          res = VectorUtil.union(res,parvars); 
        } 
      }  
    }  
 
    return res; 
  } 


  public Expression excel2Ocl(Entity target, Vector entities, Vector qvars, Vector antes)
  { if (umlkind == VALUE) 
    { return this; }
    if (umlkind == ATTRIBUTE) 
    { // E.Att becomes ex.att, ex added to qvars if needed. 
      if (entity == null) 
      { return this; }    // error
      if (entity == target) 
      { String tvarname = target.getName().toLowerCase() + "x"; 
        BasicExpression tvar = new BasicExpression(target, tvarname); 
        BasicExpression owexp = (BasicExpression) clone(); 
        owexp.setObjectRef(tvar); 
        owexp.setType(elementType); 
        return owexp; 
      } 
      else 
      { String svarname = entity.getName().toLowerCase() + "x"; 
        BasicExpression svar = new BasicExpression(entity, svarname); 
        BasicExpression owexp = (BasicExpression) clone(); 
        owexp.setObjectRef(svar); 
        if (qvars.contains(svarname)) { } 
        else 
        { qvars.add(svarname); 
          BinaryExpression scop = new BinaryExpression(":", svar, new BasicExpression(entity)); 
          antes.add(scop);
        }  
        owexp.setType(elementType); 
        return owexp; 
      } 
    } 
    else if (isReduceFunctionCall())
    { // F(pars) with pars expected to be sequences. 
      return convertExcelExpression(); 
    } 
    else 
    { return convertExcelExpression(target,entities,qvars,antes); }           
  } 

  public Vector splitToCond0Cond1(Vector conds, Vector v1, Vector v2, Vector v3)
  { Expression cond0 = (Expression) conds.get(0); 
    Expression cond1 = (Expression) conds.get(1); 
    Vector res = new Vector(); 

    Expression c1 = Expression.simplifyAnd(cond1,this); 
    res.add(cond0); 
    res.add(c1);  
    
    return res; 
  }


  // The following turns  x : e & P & l = g & Q into [x,P,l,Q] in allvars
  public Vector splitToCond0Cond1Pred(Vector conds, Vector pars, Vector qvars, Vector lvars, Vector allvars, Vector allpreds)
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
  { Expression cond0 = (Expression) conds.get(0); 
    Expression cond1 = (Expression) conds.get(1); 
    Vector res = new Vector(); 

    Expression c1 = Expression.simplifyAnd(cond1,this); 
    res.add(cond0); 
    res.add(c1);  
    
    return res; 
  }

  public Vector variablesUsedIn(final Vector vars)
  { Vector res = new Vector();
    String s = toString();
    if (vars.contains(s))
    { res.add(s); }  // eg, att, or sig1.sig
    return res;
  }

  public boolean selfConsistent(final Vector vars)
  { return true; }

  public boolean conflictsWith(Expression e)
  { return false; }

  public Maplet findSubexp(final String var)
  { return new Maplet(null,this); } /* ???? */ 
 
  public Expression filter(final Vector vars) 
  { return null; }                  /* ???? */ 

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
  { return this; }  // Case of event? 

  public Expression removeExpression(final Expression e)
  { if (equals(e))
    { return null; }
    else 
    { return this; }
  }

 public boolean implies(final Expression e)
 { if (equalsString("false")) { return true; } 
   if (e.equalsString("true")) { return true; } 
   if (e instanceof BinaryExpression)
   { BinaryExpression be = (BinaryExpression) e;
     if (be.operator.equals("="))  { return false; }
     else if (be.operator.equals("&"))
     { return (implies(be.left) && implies(be.right)); }
     else if (be.operator.equals("or"))
     { return (implies(be.left) || implies(be.right)); } 
     else { return false; } 
   }
   else if (e instanceof BasicExpression)
   { return equals(e); }
   else { return false; }
  }

  public boolean consistentWith(final Expression e)
  { return subformulaOf(e); }  /* Eg, event names */

  public boolean subformulaOf(final Expression e)
  { if (e instanceof BasicExpression) 
    { return equals(e); } 
    else if (e instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) e; 
      if (isEvent) 
      { return subformulaOf(be.left) || subformulaOf(be.right); }
      else 
      { return false; }  // v isn't a subformula of v = val
    } 
    return false; 
  } 

  public Expression computeNegation4succ(final Vector as)
  { if (data.equals("true"))
    { return new BasicExpression("false"); }
    if (data.equals("false"))
    { return new BasicExpression("true"); }
    if (umlkind == Expression.UPDATEOP)
    { return new BasicExpression("false"); }   // unsatisfactory. 
    return new UnaryExpression("not",this); 
  }  /* if name of an event, "or" of others -NO. */

  public Vector computeNegation4ante(final Vector as)
  { Vector res = new Vector();
    if (data.equals("true"))
    { res.add(new BasicExpression("false")); }
    if (data.equals("false"))
    { res.add(new BasicExpression("true")); }
    return res; 
  }

  public Expression computeNegation4succ()
  { if (data.equals("true"))
    { return new BasicExpression("false"); }
    if (data.equals("false"))
    { return new BasicExpression("true"); }
    if (umlkind == Expression.UPDATEOP)
    { return new BasicExpression("false"); }  // unsatisfactory 
    return new UnaryExpression("not",this); 
  }  /* if name of an event, "or" of others -NO. */

  public Vector computeNegation4ante()
  { Vector res = new Vector();
    if (data.equals("true"))
    { res.add(new BasicExpression("false")); }
    if (data.equals("false"))
    { res.add(new BasicExpression("true")); }
    res.add(new UnaryExpression("not",this));   // Added Jan 2016
    return res; 
  }


  public DataDependency getDataItems()
  { DataDependency res = new DataDependency();
    if (umlkind == ATTRIBUTE || umlkind == ROLE || 
        umlkind == VARIABLE || umlkind == CONSTANT)
    { res.addSource(new BasicExpression(data)); }
    if (objectRef != null)
    { DataDependency dd = objectRef.getDataItems();
      res.union(dd);
    }
    if (arrayIndex != null)
    { DataDependency dd2 = arrayIndex.getDataItems();
      res.union(dd2);
    }

    return res;
  }

  public DataDependency rhsDataDependency()  // ????
  { DataDependency res = new DataDependency();
    if (kind == ATTRIBUTE || kind == ROLE)
    { res.addTarget(this); }
    else
    { res.addSource(this); }
    if (objectRef == null)
    { return res; }
    return res.union(objectRef.getDataItems());
  } // and the arrayindex
  // target is the entity of first element of targets
  // that is not the src.

  public Expression matchCondition(String op, String f,
                     Expression exbe)
  { if (objectRef == null)
    { if (data.equals(f))
      { return new BasicExpression("true"); }
      else
      { return new BasicExpression("false"); }
    } // or in arrayRef
    else if (objectRef.multiplicity == ModelElement.ONE)
    { if (data.equals(f))
      { return new BinaryExpression("=",exbe,objectRef); }
      else 
      { return objectRef.matchCondition(op,f,exbe); }
    }
    else 
    { if (data.equals(f))
      { return new BinaryExpression(":",exbe,objectRef); }
      else 
      { return objectRef.matchCondition(op,f,exbe); }
    }
  }

  public Expression featureSetting(String var, String feat, Vector r)
  { return null; } 

  public boolean nakedUse(String f)  // Ie, of form f.features
  { if (data.equals(f))
    { if (objectRef != null) 
      { return false; } 
      return true;  // parameters and object ref also should be naked if use f
    } 
    else if (objectRef == null) 
    { return false; } 
    else if (objectRef instanceof BasicExpression) 
    { return ((BasicExpression) objectRef).nakedUse(f); } 
    else 
    { return false; } 
  } 


 /*  public static void main(String[] args)
  { Compiler2 comp = new Compiler2(); 
    comp.nospacelexicalanalysis("\"abc\"[2]"); 
    Expression e = comp.parse(); 
    System.out.println(e); 
    System.out.println(e.toZ3()); 
    Vector v = new Vector(); 
    System.out.println(e.typeCheck(v,v,v,v)); 
    // System.out.println(((BasicExpression) e).arrayIndex); 
  } */ 

  public static void main(String[] args)
  { // System.out.println(Math.log10(100)); 
    // System.out.println(Math.log(100)/Math.log(10)); 
    BasicExpression expr = new BasicExpression("x@100"); 
    System.out.println(expr); 
    BasicExpression expr1 = new BasicExpression("0xFFFFFFFFFFFFFFFFFFFF"); 
    System.out.println(expr1);
    Vector t = new Vector(); 
    Vector e = new Vector();
    Vector v = new Vector();  
    expr1.typeCheck(t,e,v);  
    System.out.println(">>> Type of " + expr1 + " is: " + expr1.type + " ( " + expr1.elementType + " )"); 

  } 

  public int syntacticComplexity() 
  { int res = 0; 

    if (objectRef != null) 
    { res = objectRef.syntacticComplexity(); }

    if (arrayIndex != null) 
    { res = res + arrayIndex.syntacticComplexity(); } 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        res = res + par.syntacticComplexity(); 
      } 
    } 

    if (umlkind == VALUE) {} 
    else  
    // if (umlkind == ATTRIBUTE || umlkind == CLASSID || umlkind == UPDATEOP || 
    //     umlkind == QUERY || umlkind == ROLE) 
    if (objectRef == null) 
    { return res + 1; } 
    else 
    { return res + 2; }  

    // one for the "." and 1 for the data.

    return res; 
  } // and arrayIndex, parameters

  public int cyclomaticComplexity()
  { return 1; }   // a basic boolean predicate

  public void changedEntityName(String oldN, String newN)
  { if (data.equals(oldN))
    { data = newN; } 
    if (type != null && oldN.equals(type.getName()))
    { type.setName(newN); } 
    if (elementType != null && oldN.equals(elementType.getName()))
    { elementType.setName(newN); }
    if (objectRef != null) 
    { objectRef.changedEntityName(oldN, newN); } 
    if (arrayIndex != null)
    { arrayIndex.changedEntityName(oldN, newN); }  
    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Expression par = (Expression) parameters.get(i); 
        par.changedEntityName(oldN, newN); 
      } 
    } 
  } 

  public String cg(CGSpec cgs)
  { String etext = (this + "").trim();
    Vector args = new Vector();
    Vector eargs = new Vector(); 
    Vector textrules = new Vector(); 

    if (("displayString".equals(data) || 
         "displayint".equals(data) ||
         "displaylong".equals(data) ||
         "displaydouble".equals(data) ||
         "displayboolean".equals(data) || 
         "displaySequence".equals(data) ||
         "displaySet".equals(data) ||
         "displayMap".equals(data)) && 
        parameters != null &&
        parameters.size() == 1)
    { Expression par1 = (Expression) parameters.get(0); 
      args.add(par1.cg(cgs)); 
      eargs.add(par1); 
      CGRule r = cgs.matchedBasicExpressionRule(this,etext,textrules); 
      if (r != null) 
      { System.out.println(">> Matched rule: " + r + " to: " + etext + " with arguments= " + args); 
       String res = r.applyRule(args,eargs,cgs);
       return res;
     }  
   }


    if (umlkind == FUNCTION) 
    { // process as the corresponding unary or binary expression
      if ("allInstances".equals(data))
      { args.add(objectRef + ""); 
        eargs.add(objectRef); 
        CGRule r = cgs.matchedBasicExpressionRule(this,etext,textrules); 
        if (r != null) 
        { // System.out.println(">> Matched rule: " + r + " to: " + etext + " with arguments= " + args); 
         String res = r.applyRule(args,eargs,cgs);
         return res;
       }  
     }
     else if ("size".equals(data) || 
              "reverse".equals(data) || 
              "toLowerCase".equals(data) ||
              "toUpperCase".equals(data) || 
              "first".equals(data) || "last".equals(data) ||
              "front".equals(data) || "tail".equals(data))
     { // args.add(objectRef + ""); 
	   // eargs.add(objectRef); 
       UnaryExpression uexp = new UnaryExpression(this); 
       System.out.println(">> Converted basic expression " + this + 
                            " to UnaryExpression " + uexp); 
        return uexp.cg(cgs); 
      }
      else if (parameters != null && parameters.size() == 0) 
      { UnaryExpression uexp = new UnaryExpression(this); 
        System.out.println(">> Converted basic expression " + this + 
                            " to UnaryExpression " + uexp); 
        return uexp.cg(cgs); 
      }  
      else if (parameters != null && parameters.size() == 2) 
      { // case of x.insertAt(i,y) or s.subrange(x,y)
        if ("Integer".equals(objectRef + "")) { } 
        else 
        { args.add(objectRef.cg(cgs)); }  
        args.add(((Expression) parameters.get(0)).cg(cgs)); 
        args.add(((Expression) parameters.get(1)).cg(cgs)); 
        eargs.add((Expression) parameters.get(0)); 
        eargs.add((Expression) parameters.get(1)); 
        CGRule r = cgs.matchedBasicExpressionRule(this,etext,textrules);
    
        if (r != null)
        { System.out.println(">> Matched rule: " + r + " to: " + etext + " with arguments= " + args); 

          String res = r.applyRule(args,eargs,cgs);
          if (needsBracket) 
          { return "(" + res + ")"; } 
          else 
          { return res; }
        }  
      }  
      else if (parameters != null && parameters.size() == 4 && "Integer".equals(objectRef + "")) 
      { // Integer.Sum(a,b,i,e), Integer.Prd(a,b,i,e)
        args.add(((Expression) parameters.get(0)).cg(cgs)); 
        args.add(((Expression) parameters.get(1)).cg(cgs)); 
        args.add(((Expression) parameters.get(2)).cg(cgs)); 
        args.add(((Expression) parameters.get(3)).cg(cgs)); 
        eargs.add((Expression) parameters.get(0)); 
        eargs.add((Expression) parameters.get(1)); 
        eargs.add((Expression) parameters.get(2)); 
        eargs.add((Expression) parameters.get(3)); 
        CGRule r = cgs.matchedBasicExpressionRule(this,etext,textrules);
    
        if (r != null)
        { // System.out.println(">> Matched rule: " + r + " to: " + etext + " with arguments= " + args); 

          String res = r.applyRule(args,eargs,cgs);
          if (needsBracket) 
          { return "(" + res + ")"; } 
          else 
          { return res; }
        }  
      }        
      else if (parameters != null && parameters.size() == 1) 
      { BinaryExpression bexp = new BinaryExpression(this); 
        // System.out.println(">> Converted basic expression " + this + 
        //                    " to BinaryExpression " + bexp); 
        return bexp.cg(cgs); 
      }
      // case of _1.pow(_2), _1.subrange(_2) etc
    } 


    if (arrayIndex != null) // no parametrs
    { BasicExpression arg = (BasicExpression) clone();
      arg.arrayIndex = null;
      arg.parameters = null;
      arg.elementType = type; 
      if (arrayIndex.type != null && arrayIndex.type.isInteger())
      { arg.type = new Type("Sequence", null); } 
      else 
      { arg.type = new Type("Map", null); } 

      args.add(arg.cg(cgs)); // but need to retype it
      args.add(arrayIndex.cg(cgs));
      eargs.add(arg);
      eargs.add(arrayIndex);
    } // assume parameters == null
    else if (objectRef != null)
    { args.add(objectRef.cg(cgs));
      eargs.add(objectRef);
      BasicExpression dataexp = (BasicExpression) clone();
      dataexp.objectRef = null; // retype it 
      if (parameters != null) 
      { String pars = ""; 
        if (parameters.size() > 0) 
        { Expression p = (Expression) parameters.get(0);
          Vector partail = new Vector(); 
          partail.addAll(parameters); 
          partail.remove(0); 
          pars = p.cgParameter(cgs,partail);
        } 
      // args.add(pars);
      // eargs.add(parameters); 

      /*  String parg = ""; 
        for (int i = 0; i < parameters.size(); i++) 
        { Expression par = (Expression) parameters.get(i); 
          String parstring = par.cg(cgs); 
          parg = parg + parstring; 
          if (i < parameters.size()-1) 
          { parg = parg + ","; } 
          // need special processig for parameters
        } */ 
        dataexp.parameters = null;

        System.out.println(dataexp + " is static: " + dataexp.isStatic); 
        String dexcg = dataexp.cg(cgs); 
        System.out.println(dexcg); 
        System.out.println(); 
 
        args.add(dataexp.cg(cgs));
        args.add(pars); 
        eargs.add(dataexp);
        eargs.add(parameters); 
      } 
      else 
      { args.add(dataexp.cg(cgs)); 
        eargs.add(dataexp);
      } 
    }
    else if (parameters != null) 
    { String parg = ""; 
      // Set the formalParameter of each par to 
      // the corresponding attribute of the operation.

      if (parameters.size() > 0) 
      { Expression p = (Expression) parameters.get(0);
        Vector partail = new Vector(); 
        partail.addAll(parameters); 
        partail.remove(0); 
        parg = p.cgParameter(cgs,partail);
      }
      args.add(data);  
      args.add(parg); 
      BasicExpression dataexp = (BasicExpression) clone();
      dataexp.objectRef = null; 
      dataexp.parameters = null;  // need to retype it
      eargs.add(dataexp);  
      eargs.add(parameters); 
    } 
    else
    { args.add(etext);
      eargs.add(this);
    }
    // and cg of parameters

    textrules = new Vector(); 
    CGRule r = cgs.matchedBasicExpressionRule(this,etext,textrules);
    
    System.out.println(">>> Found basic expression rule: " + r + " for " + this + " args= " + args + 
	                   " eargs= " + eargs); 
    System.out.println(); 

    if (r != null)
    { if (textrules.size() > 0)
      { CGRule tr = (CGRule) textrules.get(0);
        System.out.println(">>> Applying text rule " + tr + " " + tr.variables);  
        if (tr.variables.size() == 2)  // it is text_1(_2)
        { BasicExpression rootexp = (BasicExpression) clone();
          rootexp.parameters = null; 
          String rootstring = rootexp + ""; 
          String prefix = ModelElement.longestCommonPrefix(rootstring,tr.lhs);
          String remstring = 
            rootstring.substring(
                prefix.length(), rootstring.length());
          Vector textargs = new Vector(); 
          textargs.add(remstring);
          String parstring = ""; 
          for (int i = 0; i < parameters.size(); i++) 
          { Expression par = (Expression) parameters.get(i); 
	        parstring = parstring + par.cg(cgs); 
	        if (i < parameters.size() - 1) 
		   { parstring = parstring + ", "; }
		 } 
		 textargs.add(parstring);  
		 return tr.applyRule(textargs); 
	    }  
	    else if (tr.variables.size() == 1 && 
                   tr.lhs.endsWith("_1()"))  // it is text_1()
        { BasicExpression rootexp = (BasicExpression) clone();
	      rootexp.parameters = null; 
	      String rootstring = rootexp + ""; 
	      String prefix = ModelElement.longestCommonPrefix(rootstring,tr.lhs);
	      String remstring = rootstring.substring(prefix.length(), rootstring.length());
	      Vector textargs = new Vector(); 
	      textargs.add(remstring);
	      return tr.applyRule(textargs); 
	    }
        else if (tr.variables.size() == 1 && 
                 tr.lhs.endsWith("(_1)"))  // it is text(_1)
        { Vector textargs = new Vector(); 
		  String parstring = ""; 
		  for (int i = 0; i < parameters.size(); i++) 
		  { Expression par = (Expression) parameters.get(i); 
		    parstring = parstring + par.cg(cgs); 
	        if (i < parameters.size() - 1) 
		    { parstring = parstring + ", "; }
		  } 
	      textargs.add(parstring);  
	      return tr.applyRule(textargs); 
	    }
	  }
      String res = r.applyRule(args,eargs,cgs);
      if (needsBracket) 
      { return "(" + res + ")"; } 
      else 
      { return res; }
    } 
    else 
    { r = cgs.matchedTextRule(etext); 
      if (r != null) 
      { System.out.println(">>> Found basic expression text rule: " + r + " for " + this); 
        return r.applyTextRule(etext); 
      }
    } 
    return etext;
  }

}

