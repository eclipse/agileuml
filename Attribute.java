
import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Class Diagram */ 

public class Attribute extends ModelElement
{ private Type type; 
  private Type elementType; 
  private int kind;  // SEN, ACT, INTERNAL, DERIVED
  private String initialValue = ""; // default is none
  private Expression initialExpression = null; 
  private boolean unique = false; // true if a primary key
  private boolean frozen = false; // a constant 
  private boolean instanceScope = true; // "static"
  private int visibility = PRIVATE; // PRIVATE, PUBLIC, PROTECTED, PACKAGE
  private Entity entity = null; // owner
  private boolean sorted = false;  // for collection-valued attributes 

  private Vector navigation = new Vector();  // for attributes derived as composition
  private int lower = 1;  
  private int upper = 1; // 0 represents * 
  private String role1 = null;  // for associations represented as properties
  private int card1 = ModelElement.MANY; 
 
  public Attribute(String nme, Type t, int k)
  { super(nme);
    type = t;
    kind = k;
    
    if (t != null && t.getDefault() != null) 
    { initialValue = t.getDefault(); 
      initialExpression = t.getDefaultValueExpression(); 
    } 
    // elementType = t.elementType ? 

    if (type != null) 
    { String tname = type.getName(); 
      if ("Set".equals(tname)) 
      { upper = 0; lower = 0; } 
      else if ("Sequence".equals(tname))
      { upper = 0; lower = 0; } 
    } 
  }

  public Attribute(String nme, Type t) 
  { this(nme,t,ModelElement.INTERNAL); } 

  public Attribute(BasicExpression e)
  { super(e.getData()); 
    type = e.getType(); 
    elementType = e.getElementType(); 
    entity = e.getEntity(); 
    int c2 = e.getMultiplicity(); 
    if (c2 == ModelElement.ONE)
    { upper = 1; 
      lower = 1; 
    } 
    else 
    { upper = 0; 
      lower = 0; 
    } 
    kind = INTERNAL; 
  } 
    
  
  public Attribute(Association ast) 
  { super(ast.getRole2()); 
    int c2 = ast.getCard2(); 
    card1 = ast.getCard1(); 
    Entity e2 = ast.getEntity2(); 
    elementType = new Type(e2); 
    role1 = ast.getRole1(); 

    if (ast.isAggregation())
    { addStereotype("aggregation"); } 
    
    if (c2 == ModelElement.ONE) 
    { type = new Type(e2); 
      upper = 1; 
      lower = 1; 
      if (card1 == ModelElement.ONE) 
      { unique = true; } 
      else if (card1 == ModelElement.ZEROONE) 
      { unique = true; } 
    } 
    else if (ast.isOrdered())
    { type = new Type("Sequence", null); 
      type.setElementType(elementType); 
      upper = 0; 
      lower = 0; 
    } 
    else 
    { type = new Type("Set", null); 
      type.setElementType(elementType); 
      upper = 0; 
      lower = 0; 
    } 
    kind = INTERNAL; 
    entity = ast.getEntity1(); 
    if (c2 == ModelElement.ZEROONE)
    { upper = 1; } 

    if (ast.isQualified())
    { Type etype = (Type) type.clone(); 
	  type = new Type("Map", null); 
	  type.setKeyType(new Type("String", null));
	  type.setElementType(etype); 
    } 

    setStereotypes(ast.getStereotypes());  // eg., target, source, aggregation, addOnly
  }  


  public Attribute(Vector path) 
  { super(ModelElement.composeNames(path)); 
    navigation = path; 
    type = Type.composedType(path); 
    upper = Type.composeMultiplicities(path,"upper"); 
    lower = Type.composeMultiplicities(path,"lower"); 

    card1 = ModelElement.composeCard1(path); 
    unique = ModelElement.composeUnique(path); 
    String agg = ModelElement.composeAggregation(path); 
    if ("aggregation".equals(agg))
    { addStereotype("aggregation"); } 
    String st = ModelElement.composeSourceTarget(path); 
    if ("source".equals(st))
    { addStereotype("source"); } 
    else if ("target".equals(st))
    { addStereotype("target"); } 

    elementType = type.getElementType(); 
    kind = INTERNAL; 

    if (path.size() > 0) 
    { ModelElement me = (ModelElement) path.get(0); 
      // setStereotypes(me.getStereotypes());
      if (me instanceof Attribute) 
      { entity = ((Attribute) me).entity; }  
    } 
  } // and set the entity and name. Set it as aggregation if all 
    // path elements are aggregations. Likewise for unique. 

  public boolean isNumeric()
  { return type != null && type.isNumericType(); } 

  public boolean isString()
  { return type != null && type.isStringType(); } 

  public boolean isCollection()
  { return type != null && type.isCollectionType(); } 

  public boolean isEntityCollection()
  { return type != null && type.isCollectionType() && 
           elementType != null && elementType.isEntity(); 
  } 

  public boolean isSet()
  { return type != null && type.isSetType(); } 

  public boolean isSequence()
  { return type != null && type.isSequenceType(); } 

  public boolean isBoolean()
  { return type != null && type.getName().equals("boolean"); } 

  public boolean equalByNameAndOwner(Attribute att) 
  { if (att.getName().equals(name) && 
        att.getOwner() == entity && entity != null) 
    { return true; } 
    return false; 
  } 

  public boolean equalToReverseDirection(Attribute att) 
  { if (att.getName().equals(role1 + "") && 
        elementType != null && elementType.isEntity() && 
        att.getOwner() == elementType.getEntity()) 
    { return true; } 
    else if (att.getName().equals(role1 + "") && 
        type != null && type.isEntity() && 
        att.getOwner() == type.getEntity()) 
    { return true; } 
    return false; 
  } 

  public Attribute objectReference()
  { // path omitting the final feature

    if (navigation.size() <= 1) 
    { Attribute res = new Attribute("self",new Type(entity), ModelElement.INTERNAL); 
      return res; 
    } 

    Vector pathprefix = new Vector(); 
    pathprefix.addAll(navigation); 
    pathprefix.remove(navigation.get(navigation.size()-1)); 
    return new Attribute(pathprefix); 
  } 

  public String cg(CGSpec cgs)
  { String atext = this + "";
    Vector args = new Vector();
    args.add(getName());
    args.add(type.cg(cgs));
    Vector eargs = new Vector(); 
    eargs.add(this); 
    eargs.add(type); 
    // only one Attribute rule?
    // maybe for static/frozen
    CGRule r = cgs.matchedAttributeRule(this,atext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return atext;
  }

  public String cgReference(CGSpec cgs)
  { String atext = this + "";
    Vector args = new Vector();
    args.add(getName());
    args.add(type.cg(cgs));
    Vector eargs = new Vector(); 
    eargs.add(this); 
    eargs.add(type); 

    // only one Reference rule?

    CGRule r = cgs.matchedReferenceRule(this,atext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return atext;
  }

  public String cgParameter(CGSpec cgs, Vector partail)
  { String atext = this + "";
    Vector args = new Vector();
    args.add(getName());
    args.add(type.cg(cgs));
    if (partail.size() == 0) 
    { args.add(""); } 
    else 
    { Attribute p = (Attribute) partail.get(0); 
      Vector newtail = new Vector(); 
      newtail.addAll(partail); 
      newtail.remove(0); 
      args.add(p.cgParameter(cgs,newtail));
    }  
    CGRule r = cgs.matchedParameterRule(this,partail,atext);
    if (r != null)
    { return r.applyRule(args); }
    return atext;
  } // but omit initialisations for parameters

  public Type getReverseType()
  { if (entity != null) 
    { Type et = new Type(entity); 
      if (card1 == ModelElement.ONE)
      { return et; } 
      Type colltype = new Type("Set", null); 
      colltype.setElementType(et); 
      return colltype; 
    } 
    return null; 
  } 

  public Entity getClassType()
  { if (type != null) 
    { if (type.isEntity())
      { return type.getEntity(); } 
    } 

    if (elementType != null) 
    { if (elementType.isEntity())
      { return elementType.getEntity(); } 
    } 

    return null; 
  } 

  public String getRole1()
  { return role1; } 

  public boolean hasOpposite()
  { return role1 != null && role1.length() > 0; } 

  public Attribute getReverseReference()
  { if (role1 != null && role1.length() > 0) 
    { Type tr = getReverseType(); 
      Attribute res = new Attribute(role1,tr,ModelElement.INTERNAL); 
      res.setElementType(tr.getElementType()); 
      return res; 
    } 
    return null; 
  } 

  public boolean isAggregation()
  { return hasStereotype("aggregation"); } 

  public boolean isBidirectionalassociation()
  { return (role1 != null && role1.length() > 0); } 

  public Expression makeInverseCallExpression()
  { // E1.allInstances()->select( e1x � e1.att->includes(self)) for *-mult att 

    BasicExpression srcexp = new BasicExpression(this);
    srcexp.setUmlKind(Expression.ATTRIBUTE); 

    if (role1 != null && role1.length() > 0) 
    { BasicExpression res = new BasicExpression(role1); 
      return res; 
    } 

    if (entity == null) 
    { return new UnaryExpression("->inverse",srcexp); } 
    
    BasicExpression einstances = new BasicExpression(entity); 
    BasicExpression allinst = new BasicExpression("allInstances"); 
    allinst.setUmlKind(Expression.FUNCTION); 
    allinst.setObjectRef(einstances); 
    String var = Identifier.nextIdentifier("var$"); 
    BasicExpression vare = new BasicExpression(var); 
    vare.setType(new Type(entity)); 
    vare.setElementType(new Type(entity)); 
    BinaryExpression rng = new BinaryExpression(":", vare, allinst); 
    srcexp.setObjectRef(vare); 
    BasicExpression selfexp = new BasicExpression("self"); 
    selfexp.setType(elementType); 
    selfexp.setElementType(elementType); 
    BinaryExpression test = new BinaryExpression("->includes", srcexp, selfexp); 
    if (upper == 1 && lower == 1) 
    { test = new BinaryExpression("=", srcexp, selfexp); } 
    BinaryExpression selexp = new BinaryExpression("|", rng, test); 
    return selexp; 
  }  

      
  public boolean isComposed()
  { return (navigation != null && navigation.size() > 1); } 

  public static boolean isMultipleValued(Vector path)
  { int u = Type.composeMultiplicities(path,"upper"); 
    if (u != 1) 
    { return true; } 
    return false; 
  } 

  public boolean isMultiValued()
  { return upper != 1; } 

  public boolean isMandatory()
  { return lower > 0; } 

  public int upperBound()
  { if (upper == 0) 
    { return Integer.MAX_VALUE; } 
    return upper; 
  } 

  public boolean endsWith(Attribute att)
  { int n = navigation.size(); 
    if (n < 2)
    { return false; } 
    Attribute last = (Attribute) navigation.get(n-1); 
    if (last.getName().equals(att.getName()) && 
        (last.getType() + "").equals(att.getType() + ""))
    { return true; } 
    return false; 
  } 

  public boolean startsWith(Attribute att)
  { int n = navigation.size(); 
    if (n < 2)
    { return false; } 
    Attribute first = (Attribute) navigation.get(0); 
    if (first.getName().equals(att.getName()) && 
        (first.getType() + "").equals(att.getType() + ""))
    { return true; } 
    return false; 
  } 

  public Attribute first()
  { int n = navigation.size(); 
    if (n < 1)
    { return this; } 
    Attribute first = (Attribute) navigation.get(0);
    return first;  
  } 

  public Attribute last()
  { int n = navigation.size(); 
    if (n < 1)
    { return this; } 
    Attribute last = (Attribute) navigation.get(n-1);
    return last;  
  } 

  public Entity intermediateEntity()
  { if (navigation.size() < 2) 
    { return null; } 
    Attribute a1 = (Attribute) navigation.get(0); 
    Type t1 = a1.getElementType(); 
    if (t1 != null && t1.isEntity())
    { return t1.getEntity(); } 
    return null; 
  } 


  public boolean isCyclic()
  { // owner is ancestor/descendent of elementType or equal to it.
    Type t = getElementType(); 
    if (t != null && t.isEntity())
    { Entity elemt = t.getEntity(); 
      if (elemt == entity || Entity.isAncestor(elemt,entity) || 
          Entity.isAncestor(entity,elemt))
      { return true; } 
    }
    return false; 
  } 

  public boolean isOrdered()
  { return Type.isSequenceType(type); } 
 
  public boolean isDirect()
  { return navigation.size() <= 1; } 

  public boolean isConcreteChain()
  { // all element types are concrete classes if they are entity types
    if (navigation.size() == 0) 
    { if (elementType == null) 
      { if (type.isEntity())
        { return type.getEntity().isConcrete(); } 
        return false; 
      } 
      else 
      { if (elementType.isEntity())
        { return elementType.getEntity().isConcrete(); } 
      } 
      return false; 
    } 
    else 
    { for (int i = 0; i < navigation.size(); i++) 
      { Attribute x = (Attribute) navigation.get(i); 
        if (x.elementType == null) 
        { if (x.type.isEntity())
          { return x.type.getEntity().isConcrete(); } 
          return false; 
        } 
        else 
        { if (x.elementType.isEntity())
          { return x.elementType.getEntity().isConcrete(); } 
        } 
        return false; 
      } 
    }
    return false; 
  } 
   
  public Vector intermediateEntities()
  { // all entity element types
    Vector res = new Vector(); 

    if (navigation.size() == 0) 
    { if (elementType == null) 
      { if (type.isEntity())
        { res.add(type.getEntity()); } 
        return res; 
      } 
      else 
      { if (elementType.isEntity())
        { res.add(elementType.getEntity()); } 
      } 
      return res; 
    } 
    else 
    { for (int i = 0; i < navigation.size() - 1; i++) 
      { Attribute x = (Attribute) navigation.get(i); 
        if (x.elementType == null) 
        { if (x.type.isEntity())
          { res.add(x.type.getEntity()); } 
        } 
        else 
        { if (x.elementType.isEntity())
          { res.add(x.elementType.getEntity()); } 
        }  
      } 
    }
    return res; 
  } 

  public void replaceIntermediateEntity(Entity orig, Entity newe)
  { // all entity element types

    if (navigation.size() == 0) 
    { if (elementType == null) 
      { if (type.isEntity() && type.getEntity() == orig)
        { type = new Type(newe); 
          elementType = type; 
        } 
        return; 
      } 
      else 
      { if (elementType.isEntity() && elementType.getEntity() == orig)
        { type = new Type(newe); 
          elementType = type;
        } 
      } 
      return; 
    } 
    else 
    { for (int i = 0; i < navigation.size() - 1; i++) 
      { Attribute x = (Attribute) navigation.get(i); 
        if (x.elementType == null) 
        { if (x.type.isEntity() && x.type.getEntity() == orig)
          { x.type = new Type(newe); 
            x.elementType = x.type; 
          } 
        } 
        else 
        { if (x.elementType.isEntity() && x.elementType.getEntity() == orig)
          { x.type = new Type(newe); 
            x.elementType = x.type; 
          } 
        }  
      } 
    }
  } 

  public Expression atlComposedExpression(String svar, Attribute trg, Vector ems) 
  { Expression res = null; 

    Entity sent = type.getEntity(); 
    if (sent == null && elementType != null) 
    { sent = elementType.getEntity(); } 

    if (navigation.size() <= 1) 
    { res = new BasicExpression(svar + "." + this); } 
    else 
    { res = new BasicExpression(svar);
      res.multiplicity = ModelElement.ONE; 
 
      for (int i = 0; i < navigation.size(); i++) 
      { Attribute att = (Attribute) navigation.get(i); 
        if (res.multiplicity == ModelElement.ONE) 
        { BasicExpression r = new BasicExpression(att); 
          r.setObjectRef(res); 
          res = r; 
          // res.multiplicity = ModelElement.ONE;
        } 
        else if (res.multiplicity != ModelElement.ONE && att.upper == 1)  
        { Expression ce = new BinaryExpression("->collect",res,new BasicExpression(att)); 
          res = ce; 
          res.multiplicity = ModelElement.MANY; 
        } 
        else if (res.multiplicity != ModelElement.ONE && att.upper != 1)  
        { Expression ce = new BinaryExpression("->collect",res,new BasicExpression(att)); 
          res = new UnaryExpression("->flatten",ce); 
          res.multiplicity = ModelElement.MANY; 
        } 
      } 
    } 

    if (trg.type.isEntityType())
    { Entity tent = trg.type.getEntity(); 
      EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
      if (em != null && em.isSecondary()) 
      { String trgvarname = em.realtrg.getName().toLowerCase() + "_x"; 
        return new BasicExpression("thisModule.resolveTemp(" + res + ", '" + trgvarname + "')"); 
      }
    } 
    else if (Type.isEntityCollection(trg.type))
    { Entity tent = trg.elementType.getEntity(); 
      EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
      if (em != null && em.isSecondary()) 
      { String trgvarname = em.realtrg.getName().toLowerCase() + "_x"; 
        return new BasicExpression("thisModule.resolveTemp(" + res + ", '" + trgvarname + "')");  
      }
    } 
    return res;  
  } 

  public Expression etlComposedExpression(String svar, Attribute trg, Vector ems) 
  { Entity sent = type.getEntity(); 
    if (sent == null && elementType != null) 
    { sent = elementType.getEntity(); } 

    if (navigation.size() <= 1) 
    { if (trg.type.isEntityType())
      { Entity tent = trg.type.getEntity(); 
        EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
        if (em != null) 
        { return new BasicExpression(svar + "." + this + ".equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
        }  
        return new BasicExpression(svar + "." + this + ".equivalent()"); 
      } 
      else if (Type.isEntityCollection(trg.type))
      { Entity tent = trg.elementType.getEntity(); 
        EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
        if (em != null) 
        { return new BasicExpression(svar + "." + this + ".equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
        }
        return new BasicExpression(svar + "." + this + ".equivalent()"); 
      } 
      else 
      { return new BasicExpression(svar + "." + this); }
    }  
    else 
    { Expression res = new BasicExpression(svar);
      res.multiplicity = ModelElement.ONE; 
 
      for (int i = 0; i < navigation.size(); i++) 
      { Attribute att = (Attribute) navigation.get(i); 
        if (res.multiplicity == ModelElement.ONE) 
        { BasicExpression r = new BasicExpression(att); 
          r.setObjectRef(res); 
          res = r; 
          // res.multiplicity = ModelElement.ONE;
        } 
        else if (res.multiplicity != ModelElement.ONE && att.upper == 1)  
        { // Expression ce = new BinaryExpression("->collect",res,new BasicExpression(att)); 
          // res = ce; 
          // res.multiplicity = ModelElement.MANY; 
          BasicExpression r = new BasicExpression(att); 
          r.setObjectRef(res); 
          res = r; 
        }  
        else if (res.multiplicity != ModelElement.ONE && att.upper != 1)  
        { Expression ce = new BinaryExpression("->collect",res,new BasicExpression(att)); 
          res = new UnaryExpression("->flatten",ce); 
          res.multiplicity = ModelElement.MANY; 
        } 
      }
 
      if (trg.type.isEntityType())
      { Entity tent = trg.type.getEntity(); 
        EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
        if (em != null) 
        { return new BasicExpression(res + ".equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
        }
        return new BasicExpression(res + ".equivalent()");
      } 
      else if (Type.isEntityCollection(trg.type))
      { Entity tent = trg.elementType.getEntity(); 
        EntityMatching em = ModelMatching.findEntityMatchingFor(sent,tent,ems);
        if (em != null) 
        { return new BasicExpression(res + ".equivalent('" + em.realsrc + "2" + 
                                                                          em.realtrg + "')"); 
        }
        return new BasicExpression(res + ".equivalent()"); 
      } 
      else 
      { return res; }  
    } 
  } 

  public int steps()
  { int res = navigation.size(); 
    if (res == 0) 
    { return 1; } 
    return res; 
  } 

  public boolean isForbiddenInverse(Attribute ast)
  { Vector v2 = ast.getNavigation(); 
    Attribute q = ast; 

    if (v2.size() > 0) 
    { q = (Attribute) v2.get(0); }

    if ((role1 + "").equals(q.getName()) && 
        getName().equals("" + q.role1) && 
        q.upper == 1) 
    { return true; } 

    return false; 
  } 
 
  public Attribute getFinalFeature()
  { int pathsize = navigation.size();
    if (pathsize == 0) 
    { return this; } 
    return (Attribute) navigation.get(pathsize-1);
  } 

  public boolean equals(Object x)
  { if (x instanceof Attribute)
    { Attribute att = (Attribute) x; 
      if (att.getName().equals(getName()) && 
          (att.getType() + "").equals(getType() + "") && 
          att.entity == entity)
      { return true; } 
    } 
    return false; 
  } 

  public boolean isManyValued()
  { return upper == 0; } 

  public boolean isMultipleValued()
  { if (upper != 1) 
    { return true; } 
    if (lower != 1)
    { return true; }
    return false;
  } 

  public boolean isSingleValued()
  { return lower == 1 && upper == 1; } 

  public void setEntity(Entity e)
  { entity = e; } 

  public void setType(Type t)
  { type = t; 
    if (t != null && t.getDefault() != null) 
    { initialValue = t.getDefault(); } 
  } 

  public void setNavigation(Vector p) 
  { navigation = p; } 

  public boolean isMultiple()
  { if (type == null) 
    { return false; } 
    return type.isCollectionType();
  } 

  public boolean isEntityInstance()
  { if (type == null) 
    { return false; } 
    return type.isEntity(); 
  } 
  
  public boolean isEntity()
  { return isEntityInstance(); }

  public boolean isSorted()
  { return sorted; } 

  public boolean isComposition()
  { return hasStereotype("aggregation"); } 

  public void setElementType(Type et)
  { elementType = et; }  // type.setElementType(et) also 

  public void setSorted(boolean srt)
  { sorted = srt; } 

  public Object clone()
  { Attribute res = new Attribute(getName(),type,kind);
    res.setInitialValue(initialValue); 
    res.setInitialExpression(initialExpression); 
    res.setUnique(unique); 
    res.setFrozen(frozen); 
    res.setInstanceScope(instanceScope); // "static"
    res.setVisibility(visibility);
    res.setElementType(elementType);  
    // res.setEntity(entity); ??
    res.sorted = sorted; 
    return res; 
  }

  public String getJavaType()
  { if (type != null)
    { return type.getJava(); }
    return "int";
  }

  public Type getElementType()
  { return elementType; } 

  public Vector getNavigation()
  { return navigation; } 

  public int getLower()
  { return lower; } 

  public int getUpper()
  { return upper; } 

  public int getCard1()
  { return card1; } 

  public static String parList(Vector pars)
  { String res = ""; 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute par = (Attribute) pars.get(i); 
      res = res + " " + par.getName() + " " + par.getType();
    } 
    return res; 
  } 
 
  public String underscoreName()
  { if (navigation.size() <= 1) 
    { return getName(); } 
    String res = "";
    for (int i = 0; i < navigation.size(); i++)
    { Attribute p = (Attribute) navigation.get(i);
      res = res + p.getName(); 
      if (i < navigation.size()-1)
      { res = res + "_"; } 
    } 
    return res; 
  } 

  public boolean isSource()
  { return stereotypes.contains("source"); } 

  public boolean isTarget()
  { return stereotypes.contains("target"); } 

  public Attribute mergeAttribute(Attribute att)
  { // checks they can be merged in a common superclass
    Attribute res = null;
    Type newt = type.mergeType(att.type); // not for collection types
    if (newt == null) { return null; } 
    if (kind == att.kind)
    { res = new Attribute(getName(),newt,kind);
      if (initialValue.equals(att.initialValue))
      { res.setInitialValue(initialValue); 
        if (initialExpression != null) 
        { res.setInitialExpression(initialExpression); } 
        else 
        { res.setInitialExpression(att.initialExpression); } 
      } 
      if (unique == att.unique)
      { res.setUnique(unique); } 
      if (frozen == att.frozen)
      { res.setFrozen(frozen); } 
      if (instanceScope == att.instanceScope)
      { res.setInstanceScope(instanceScope); } 
      if (visibility == PUBLIC || att.visibility == PUBLIC)
      { res.setVisibility(PUBLIC); } 
      else 
      { res.setVisibility(PROTECTED); } 
      return res; 
    }
    return null; 
  }
 
    

  public void setInitialValue(String val)
  { initialValue = val; } 

  public void setInitialExpression(Expression e)
  { initialExpression = e; } 

  public void setUnique(boolean uniq)
  { unique = uniq; 
    if (uniq)
    { card1 = ModelElement.ZEROONE; }  
  } 

  public boolean isOutput()
  { return hasStereotype("output"); }
  
  public boolean isInputAttribute()
  { return hasStereotype("input"); }

  public boolean isSummary()
  { return kind == ModelElement.SUMMARY; } 

  public boolean isPassword()
  { return kind == ModelElement.PASSWORD; } 

  public boolean isHidden()
  { return kind == ModelElement.HIDDEN; } 

  
  public void setIdentity(boolean uniq)
  { unique = uniq; 
    if (uniq) 
    { card1 = ModelElement.ZEROONE; }  
  } 

  public boolean isIdentity()
  { return unique; } 

  public boolean isIdentityFeature()
  { return card1 == ModelElement.ZEROONE; } 

  public boolean isPrimaryAttribute()
  { // The first identity attribute in its owner's attributes

    if (isIdentity()) { } 
    else 
    { return false; } 

    Entity own = getOwner(); 
    if (own == null) 
    { System.err.println("!! Warning: " + this + " has no owner"); 
      return true; 
    } 

    Vector atts = own.getAttributes(); 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      if (att.isIdentity())
      { if (att == this) 
        { return true; } 
        return false; 
      } 
    } 
    return false; 
  } 

  public void setFrozen(boolean fr)
  { frozen = fr; 
    if (fr) 
    { addStereotype("readOnly"); }
    else 
    { removeStereotype("readOnly"); }
  }  
  
  public boolean isInteger()
  { return isInt() || isLong(); }

  public boolean isInt()
  { return type != null && type.getName().equals("int"); }

  public boolean isLong()
  { return type != null && type.getName().equals("long"); }

  public boolean isDouble()
  { return type != null && type.getName().equals("double"); }

  public boolean isEnumeration()
  { return type != null && type.isEnumerated(); } 

  public boolean isEnumerated()
  { return type != null && type.isEnumerated(); } 

  public boolean isSmallEnumeration()
  { return type != null && type.isEnumerated() && (type.getValues().size() <= 4); } 

  public boolean isLargeEnumeration()
  { return type != null && type.isEnumerated() && (type.getValues().size() > 4); } 

  public boolean isFinal()
  { return frozen && initialExpression != null; } 

  public void setInstanceScope(boolean inst)
  { instanceScope = inst; } 

  public void setStatic(boolean stat)
  { instanceScope = !stat; } 

  public boolean isStatic()
  { return instanceScope == false; } 

  public boolean isDerived()
  { return kind == ModelElement.DERIVED; } 

  public void setDerived(boolean deriv)
  { if (deriv) 
    { kind = ModelElement.DERIVED; } 
	else 
	{ kind = ModelElement.INTERNAL; } 
  } 
  
  public void setVisibility(int visib)
  { visibility = visib; } 

  public Entity getEntity()
  { return entity; } 

  public Entity getOwner()
  { return entity; } 

  public Type getType()
  { return type; }

  public int getKind()
  { return kind; } 

  public String getInitialValue()
  { return initialValue; } 

  public String getInitialValueSMV()
  { if ("false".equals(initialValue))
    { return "FALSE"; } 
    if ("true".equals(initialValue))
    { return "TRUE"; }
    if ("\"\"".equals(initialValue))
    { return "empty"; } 
    return initialValue; 
  }  

  public Expression getInitialExpression()
  { return initialExpression; } 

  public int getVisibility()
  { return visibility; } 

  public String getDefaultValue()
  { if (initialValue != null && !initialValue.equals(""))
    { return initialValue; } 
    return type.getDefault();
  } 

  public int syntacticComplexity()
  { // att : T = init
    int result = 3 + type.complexity(); 
    if (initialExpression != null) 
    { result += initialExpression.syntacticComplexity(); } 
    return result; 
  } 

  public boolean isSensor()  // or internal
  { return kind == ModelElement.SEN ||
           kind == ModelElement.INTERNAL;
  }

  public boolean isInput()  
  { return kind == ModelElement.SEN ||
           kind == ModelElement.INTERNAL;
  }

  public boolean isUnique()
  { return unique; } 

  public boolean isFrozen()
  { return frozen; } 

  public boolean isInstanceScope()
  { return instanceScope; } 

  public boolean isClassScope()
  { return !instanceScope; } 

  public boolean isUpdatable()
  { return !frozen && kind != ModelElement.DERIVED; } 

  public boolean isAssignable()
  { return !frozen && kind != ModelElement.SEN; } 

  public boolean needsControllerOp()
  { return !frozen &&
           (kind == ModelElement.SEN || kind == ModelElement.INTERNAL); }
  // DERIVED are handled locally? 

  public void saveEMF(PrintWriter out)
  { out.println("  attr " + getType() + " " + getName() + ";"); } 

  public void saveKM3(PrintWriter out)
  { if (isStatic())
    { out.println("    static attribute " + getName() + " : " + getType() + ";"); }
    else if (isIdentity())
    { out.println("    attribute " + getName() + " identity : " + getType() + ";"); } 
    else if (isDerived())
    { out.println("    attribute " + getName() + " derived : " + getType() + ";"); }
	else 
    { out.println("    attribute " + getName() + " : " + getType() + ";"); } 
  } 

  public String getKM3()
  { if (isStatic())
    { return "    static attribute " + getName() + " : " + getType() + ";"; } 
    else if (isIdentity())
    { return "    attribute " + getName() + " identity : " + getType() + ";"; }
    else if (isDerived())
    { return "    attribute " + getName() + " derived : " + getType() + ";"; }
    else  
    { return "    attribute " + getName() + " : " + getType() + ";"; } 
  } 

  public void saveEcore(PrintWriter out)
  { String res = "  <eStructuralFeatures xsi:type=\"ecore:EAttribute\" "; 
    res = res + " name=\"" + getName() + "\""; 
    if (type.isEnumerated())
    { res = res + " eType=\"#//" + type.getName() + "\""; } 
    else 
    { res = res + " eType=\"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//" + type.eType() + "\""; }

    if (initialValue != null && !initialValue.equals(""))
    { res = res + " defaultValueLiteral=\"" + initialValue + "\" "; } 
    
    res = res + "/>"; 
    out.println(res); 
  } 

  public String methodDeclaration()
  { String res = ""; 
    res = "    " + getType().getJava() + " " + getName(); 
    String initval = getInitialValue(); 
    if (initval != null) 
    { res = res + " = " + initval; } 
    else 
    { res = res + " = " + getType().getDefault(); } 
    res = res + ";\n";
    return res;  
  } 
    
  public void generateJava(PrintWriter out)
  { if (visibility == PRIVATE)
    { out.print("  private "); }
    else if (visibility == PUBLIC)
    { out.print("  public "); } 
    else if (visibility == PROTECTED)
    { out.print("  protected "); }
    else 
    { out.print(" "); } 
    
    if (!instanceScope) { out.print("static "); } 
    if (isFinal()) { out.print("final "); } 
    type.generateJava(out);
    // if (isFinal()) 
    if (initialValue != null) 
    { out.print(getName() + " = " + initialValue + ";"); } 
    else 
    { out.print(getName() + ";"); }
    if (kind == ModelElement.INTERNAL)
    { out.println(" // internal"); }
    else if (kind == ModelElement.SEN)
    { out.println(" // sensor"); }     // can be used for volatile data in C
    else if (kind == ModelElement.ACT)
    { out.println(" // actuator"); }
    else if (kind == ModelElement.DERIVED)
    { out.println(" // derived"); } 
  }

  public String methodDeclarationJava6()
  { String res = ""; 
    res = "    " + getType().getJava6() + " " + getName(); 
    Expression initval = initialExpression; 
    if (initval != null) 
    { res = res + " = " + initval.queryFormJava6(new java.util.HashMap(), true); } 
    else 
    { res = res + " = " + getType().getDefaultJava6(); } 
    res = res + ";\n";
    return res;  
  } 

  public String methodDeclarationJava7()
  { String res = ""; 
    res = "    " + getType().getJava7(elementType) + " " + getName(); 
    Expression initval = initialExpression; 
    if (initval != null) 
    { res = res + " = " + initval.queryFormJava7(new java.util.HashMap(), true); } 
    else 
    { res = res + " = " + getType().getDefaultJava7(); } 
    res = res + ";\n";
    return res;  
  } 


  public void generateJava6(PrintWriter out)
  { if (visibility == PRIVATE)
    { out.print("  private "); }
    else if (visibility == PUBLIC)
    { out.print("  public "); } 
    else if (visibility == PROTECTED)
    { out.print("  protected "); }
    else 
    { out.print(" "); } 
    
    if (!instanceScope) { out.print("static "); } 
    if (isFinal()) { out.print("final "); } 
    type.generateJava6(out);
    if (isFinal() && initialValue != null) 
    { out.print(getName() + " = " + initialValue + ";"); } 
    else 
    { out.print(getName() + ";"); }
    if (kind == ModelElement.INTERNAL)
    { out.println(" // internal"); }
    else if (kind == ModelElement.SEN)
    { out.println(" // sensor"); }
    else if (kind == ModelElement.ACT)
    { out.println(" // actuator"); }
    else if (kind == ModelElement.DERIVED)
    { out.println(" // derived"); } 
  }

  public void generateJava7(PrintWriter out)
  { if (visibility == PRIVATE)
    { out.print("  private "); }
    else if (visibility == PUBLIC)
    { out.print("  public "); } 
    else if (visibility == PROTECTED)
    { out.print("  protected "); }
    else 
    { out.print(" "); } 
    
    if (!instanceScope) { out.print("static "); } 
    if (isFinal()) { out.print("final "); } 
    type.generateJava7(out, elementType);
    if (isFinal() && initialValue != null) 
    { out.print(getName() + " = " + initialValue + ";"); } 
    else 
    { out.print(getName() + ";"); }
    if (kind == ModelElement.INTERNAL)
    { out.println(" // internal"); }
    else if (kind == ModelElement.SEN)
    { out.println(" // sensor"); }
    else if (kind == ModelElement.ACT)
    { out.println(" // actuator"); }
    else if (kind == ModelElement.DERIVED)
    { out.println(" // derived"); } 
  }

  public String methodDeclarationCSharp()
  { String res = ""; 
    res = "    " + getType().getCSharp() + " " + getName(); 
    String initval = getInitialValue(); 
    if (initval != null) 
    { res = res + " = " + initval; } 
    else 
    { res = res + " = " + getType().getDefaultCSharp(); } 
    res = res + ";\n";
    return res;  
  } 

  public void generateCSharp(PrintWriter out)
  { if (visibility == PRIVATE)
    { out.print("  private "); }
    else if (visibility == PUBLIC)
    { out.print("  public "); } 
    else if (visibility == PROTECTED)
    { out.print("  protected "); }
    else 
    { out.print(" "); } 
    
    if (!instanceScope) { out.print("static "); } 
    if (isFinal()) { out.print("const "); } 
    type.generateCSharp(out);
    if (isFinal() && initialValue != null) 
    { out.print(getName() + " = " + initialValue + ";"); } 
    else 
    { out.print(getName() + ";"); }
    if (kind == ModelElement.INTERNAL)
    { out.println(" // internal"); }
    else if (kind == ModelElement.SEN)
    { out.println(" // sensor"); }
    else if (kind == ModelElement.ACT)
    { out.println(" // actuator"); }
    else if (kind == ModelElement.DERIVED)
    { out.println(" // derived"); } 
  }

  public String methodDeclarationCPP()
  { String res = ""; 
    res = "    " + getType().getCPP(getElementType()) + " " + getName(); 
    String initval = getInitialValue(); 
    if (initval != null) 
    { res = res + " = " + initval; } 
    else 
    { res = res + " = " + getType().getDefaultCPP(getElementType()); } 
    res = res + ";\n";
    return res;  
  } // initialExpression & convert to C++, etc, also for C#


  public void staticAttributeDefinition(PrintWriter out, String ename) 
  { String res = "    " + getType().getCPP(getElementType()) + " " + ename + "::" + getName(); 
    String initval = getInitialValue(); // initialExpression is better 
    if (initval != null) 
    { res = res + " = " + initval; } 
    else 
    { res = res + " = " + getType().getDefaultCPP(getElementType()); } 
    res = res + ";\n";
    out.println(res); 
  } 
 

  public void generateCPP(PrintWriter out)
  { /* if (visibility == PRIVATE)
    { out.print("  private "); }
    else if (visibility == PUBLIC)
    { out.print("  public "); } 
    else if (visibility == PROTECTED)
    { out.print("  protected "); }
    else 
    { out.print(" "); } */ 
    
    out.print("  "); 

    if (!instanceScope) { out.print("static "); } 

    if (isFinal()) { out.print("const "); } 
    type.generateCPP(out,elementType);

    if (isFinal() && initialValue != null) 
    { out.print(getName() + " = " + initialValue + ";"); } 
    else 
    { out.print(getName() + ";"); }

    if (kind == ModelElement.DERIVED)
    { out.println(" // derived"); } 
    else 
    { out.println(); } 
  }

  public void generateInterfaceJava(PrintWriter out)
  { if (visibility == PUBLIC && !instanceScope && isFinal()) 
    {  }
    else 
    { out.print("  // "); } 
 
    type.generateJava(out);
    if (isFinal()) 
    { out.println(getName() + " = " + initialValue + ";"); } 
    else 
    { out.println(getName() + ";"); }
  }

  public void generateInterfaceJava6(PrintWriter out)
  { if (visibility == PUBLIC && !instanceScope && isFinal()) 
    {  }
    else 
    { out.print("  // "); } 
 
    type.generateJava6(out);
    if (isFinal()) 
    { out.println(getName() + " = " + initialValue + ";"); } 
    else 
    { out.println(getName() + ";"); }
  }

  public void generateInterfaceJava7(PrintWriter out)
  { if (visibility == PUBLIC && !instanceScope && isFinal()) 
    {  }
    else 
    { out.print("  // "); } 
 
    type.generateJava7(out, elementType);
    if (isFinal()) 
    { out.println(getName() + " = " + initialValue + ";"); } 
    else 
    { out.println(getName() + ";"); }
  }

  public void generateInterfaceCSharp(PrintWriter out)
  { if (visibility == PUBLIC && !instanceScope && isFinal()) 
    {  }
    else 
    { out.print("  // "); } 
 
    type.generateCSharp(out);
    if (isFinal()) 
    { out.println(getName() + " = " + initialValue + ";"); } 
    else 
    { out.println(getName() + ";"); }
  }

  public void generateMethodJava(PrintWriter out)
  { out.print("  "); 
    
    type.generateJava(out);
    out.print(getName());
    if (initialValue != null && !initialValue.equals(""))
    { out.println(" = " + initialValue + ";"); }
    else 
    { out.println(";"); } 
  } 

  public String saveModelData(PrintWriter out)
  { String nme = getName(); 
    Entity e = getEntity(); 
    String entnme = e + ""; 

    if (e == null) 
    { entnme = Identifier.nextIdentifier("attribute_"); } 

    String cname = nme + "_" + entnme; 
    String tname = type.getUMLModelName(out); 

    out.println(cname + " : Property"); 
    out.println(cname + ".name = \"" + nme + "\""); 
    if (e != null) 
    { out.println(cname + " : " + entnme + ".ownedAttribute"); } 
    out.println(cname + ".type = " + tname); 

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(cname + ".elementType = " + etname); 
    } 
    else 
    { out.println(cname + ".elementType = " + tname); }

    if (isMultiple())
    { out.println(cname + ".lower = 0"); 
      out.println(cname + ".upper = -1");
    }  
    else 
    { out.println(cname + ".lower = 1"); 
      out.println(cname + ".upper = 1"); 
    } 

    if (!instanceScope) { out.println(cname + ".isStatic = true"); } 
    if (isFinal()) { out.println(cname + ".isReadOnly = true"); } 
    if (unique) { out.println(cname + ".isUnique = true"); } 

    if (initialExpression != null) 
    { String ini = initialExpression.saveModelData(out); 
      out.println(cname + ".initialValue = " + ini); 
    } 

    return cname; 
  } // initial value, derived

  public Statement generateMethodJava()
  { if (initialExpression == null) 
    { return null; } 
    AssignStatement as = 
      new AssignStatement(new BasicExpression(getName()),initialExpression);
    as.setType(type); 
    return as; 
  }  


  public void generateMethodB(PrintWriter out)
  { String nme = getName(); 
    out.println("VAR " + nme + "\n    IN\n    ");     
    if (initialExpression != null && !initialExpression.equals(""))
    { java.util.Map env0 = new java.util.HashMap(); 
      // if (entity != null) 
      // { env0.put(entity.getName(),"this"); 
      out.println(nme + " := " + 
              initialExpression.binvariantForm(env0,true) + 
              ";");
      out.print("    "); 
    }
  } 


  // public void generateB(BComponent res)
  // { res.addAttribute(this); }
 
  public String constructorParameter()
  { if (frozen && initialExpression == null)  // for any frozen att, surely?
    { return type.getJava() + " " + getName() + "x"; }
    else
    { return null; }
  }

  public String constructorParameterJava6()
  { if (frozen && initialExpression == null)  // for any frozen att, surely?
    { return type.getJava6() + " " + getName() + "x"; }
    else
    { return null; }
  }

  public String constructorParameterJava7()
  { if (frozen && initialExpression == null)  // for any frozen att, surely?
    { return type.getJava7(elementType) + " " + getName() + "x"; }
    else
    { return null; }
  }

  public String constructorParameterCSharp()
  { if (frozen && initialExpression == null)  // for any frozen att, surely?
    { return type.getCSharp() + " " + getName() + "x"; }
    else
    { return null; }
  }

  public String constructorParameterCPP()
  { if (frozen && initialExpression == null)  // for any frozen att, surely?
    { return type.getCPP(elementType) + " " + getName() + "x"; }
    else
    { return null; }
  }

  public String initialiser(Vector entities, Vector types)
  { String nme = getName();
    if (frozen && initialExpression == null)   
    { return "this." + nme + " = " + nme + "x;"; }
    if (isFinal()) { return ""; }

    if (initialExpression != null) 
    { java.util.Map env = new java.util.HashMap();
      if (entity != null) 
      { env.put(entity.getName(), "this"); } 
      return "this." + nme + " = " + initialExpression.queryForm(env,true) + ";"; 
    } 

    if (initialValue != null && !initialValue.equals(""))
    { return "this." + nme + " = " + initialValue + ";"; } 

    String def = type.getDefault();
    if (def == null) { return ""; }
    return nme + " = " + def + ";";
  } 

  public String initialiserJava6()
  { String nme = getName();
    if (frozen && initialExpression == null)   
    { return "this." + nme + " = " + nme + "x;"; }

    if (isFinal()) { return ""; }

    if (initialExpression != null)
    { java.util.Map env = new java.util.HashMap();
      if (entity != null) 
      { env.put(entity.getName(), "this"); } 
      return "this." + nme + " = " + 
          initialExpression.queryFormJava6(env, true) + ";"; 
    } 

    // if (initialValue != null && !initialValue.equals(""))
    // { return "this." + nme + " = " + initialValue + ";"; } 

    String def = type.getDefaultJava6();
    if (def == null) { return ""; }
    return nme + " = " + def + ";";
  } 

  public String initialiserJava7()
  { String nme = getName();
    if (frozen && initialExpression == null)   
    { return "this." + nme + " = " + nme + "x;"; }
    if (isFinal()) { return ""; }

    if (initialExpression != null) 
    { java.util.Map env = new java.util.HashMap();
      if (entity != null) 
      { env.put(entity.getName(), "this"); } 
      return "this." + nme + " = " + initialExpression.queryFormJava7(env,true) + ";"; 
    } 


    // if (initialValue != null && !initialValue.equals(""))
    // { return "this." + nme + " = " + initialValue + ";"; } 
    String def = type.getDefaultJava7();
    if (def == null) { return ""; }
    return "this." + nme + " = " + def + ";";
  } 

  public String initialiserCSharp()
  { String nme = getName();
    if (frozen && initialExpression == null)   
    { return "this." + nme + " = " + nme + "x;"; }
    if (isFinal()) { return ""; }

    if (initialExpression != null) 
    { java.util.Map env = new java.util.HashMap();
      if (entity != null) 
      { env.put(entity.getName(), "this"); } 
      return "this." + nme + " = " + initialExpression.queryFormCSharp(env,true) + ";"; 
    } 

    // if (initialValue != null && !initialValue.equals(""))
    // { return "this." + nme + " = " + initialValue + ";"; } 
    String def = type.getDefaultCSharp();
    if (def == null) { return ""; }
    return nme + " = " + def + ";";
  } 

  public String initialiserCPP()
  { String nme = getName();
    if (frozen && initialExpression == null)   
    { return nme + " = " + nme + "x;"; }
    if (isFinal()) { return ""; }
    // if (initialValue != null && !initialValue.equals(""))
    // { return "" + nme + " = " + initialValue + ";"; } 
    if (initialExpression != null) 
    { java.util.Map env = new java.util.HashMap();
      if (entity != null) 
      { env.put(entity.getName(), "this"); } 
      return "this." + nme + " = " + initialExpression.queryFormCPP(env,true) + ";"; 
    } 
    String def = type.getDefaultCPP(elementType);
    if (def == null) { return ""; }
    return nme + " = " + def + ";";
  } 


  // setatt operation for the entity:
  public String setOperation(Entity ent, Vector cons,
                             Vector entities, Vector types) 
  { // setatt(type attx) 
    // if ent != entity, creates subclass ent extension op for att

    if (frozen) { return ""; }
    String nme = getName();
    if (type == null || ent == null || entity == null) // error
    { System.err.println("ERROR: null type or entity in attribute " + nme); 
      return ""; 
    } 

    Vector v = type.getValues();
    String val = nme + "_x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    par.setElementType(elementType); 

    Vector v1 = new Vector();
    v1.add(par);
    String t = type.getJava(); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getName(); }
    // else 
    // { t = "int"; } 

    if (ent.isInterface())
    { return " void set" + nme + "(" + t + " _x);\n"; } 


    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    String qual = " "; 
    String code = ""; 
    String sync = ""; 

    if (entity.isSequential()) { sync = " synchronized"; } 
    
    if (ent != entity && !entity.isInterface()) 
    { code = "super.set" + nme + "(" + val + ");"; }
    else if (!instanceScope)
    { qual = " static "; 
      code = nme + " = " + val + ";";
    }
    else 
    { code = nme + " = " + val + ";"; } // controller sets static atts, once only 
 
    String opheader; 
    opheader = "public" + sync + qual + "void set" + nme + "(" + t +
             " " + val + ") { " + code; 

    if (!instanceScope)
    { opheader = opheader + " }\n\n" + 
        "public" + sync + " void localSet" + nme + "(" + t +
             " " + val + ") { "; 
    }
       
    BasicExpression attxbe = new BasicExpression(val); 
      
    Vector contexts = new Vector(); 
    contexts.add(ent); 
        
    for (int j = 0; j < cons.size(); j++)   // may be constraints of subclass ent
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set of " + cc + " is: " + cnew);
       
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx); 
        if (typed)
        { String update = cnew.updateOperation(ent,nme,true);  
          opheader = opheader + "\n" + 
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    }
    return opheader + "  }\n"; 
  }  // actuators -- include a message?  Should not be local?

  // setatt operation for the entity:
  public String setOperationJava6(Entity ent, Vector cons,
                             Vector entities, Vector types) 
  { // setatt(type attx) 
    // if ent != entity, creates subclass ent extension op for att

    if (frozen) { return ""; }
    String nme = getName();
    if (type == null || ent == null || entity == null) // error
    { System.err.println("ERROR: null type or entity in attribute " + nme); 
      return ""; 
    } 

    Vector v = type.getValues();
    String val = nme + "_x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    par.setElementType(elementType); 

    Vector v1 = new Vector();
    v1.add(par);
    String t = type.getJava6(); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getName(); }
    // else 
    // { t = "int"; } 

    if (ent.isInterface())
    { return " void set" + nme + "(" + t + " _x);\n"; } 


    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    String qual = " "; 
    String code = ""; 
    String sync = ""; 

    if (entity.isSequential()) { sync = " synchronized"; } 
    
    if (ent != entity && !entity.isInterface()) 
    { code = "super.set" + nme + "(" + val + ");"; }
    else if (!instanceScope)
    { qual = " static "; 
      code = nme + " = " + val + ";";
    }
    else 
    { code = nme + " = " + val + ";"; } // controller sets static atts, once only 
 
    String opheader; 
    opheader = "public" + sync + qual + "void set" + nme + "(" + t +
             " " + val + ") { " + code; 

    if (!instanceScope)
    { opheader = opheader + " }\n\n" + 
        "public" + sync + " void localSet" + nme + "(" + t +
             " " + val + ") { "; 
    }
       
    BasicExpression attxbe = new BasicExpression(val); 
      
    Vector contexts = new Vector(); 
    contexts.add(ent); 
        
    for (int j = 0; j < cons.size(); j++)   // may be constraints of subclass ent
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set of " + cc + " is: " + cnew);
       
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx); 
        if (typed)
        { String update = cnew.updateOperationJava6(ent,nme,true);  
          opheader = opheader + "\n" + 
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    }
    return opheader + "  }\n"; 
  }  // actuators -- include a message?  Should not be local?

  public String setOperationJava7(Entity ent, Vector cons,
                             Vector entities, Vector types) 
  { // setatt(type attx) 
    // if ent != entity, creates subclass ent extension op for att

    if (frozen) { return ""; }
    String nme = getName();
    if (type == null || ent == null || entity == null) // error
    { System.err.println("ERROR: null type or entity in attribute " + nme); 
      return ""; 
    } 

    Vector v = type.getValues();
    String val = nme + "_x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    par.setElementType(elementType); 

    Vector v1 = new Vector();
    v1.add(par);
    String t = type.getJava7(elementType); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getName(); }
    // else 
    // { t = "int"; } 

    if (ent.isInterface())
    { return " void set" + nme + "(" + t + " _x);\n"; } 


    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    String qual = " "; 
    String code = ""; 
    String sync = ""; 

    if (entity.isSequential()) { sync = " synchronized"; } 
    
    if (ent != entity && !entity.isInterface()) 
    { code = "super.set" + nme + "(" + val + ");"; }
    else if (!instanceScope)
    { qual = " static "; 
      code = nme + " = " + val + ";";
    }
    else 
    { code = nme + " = " + val + ";"; } // controller sets static atts, once only 
 
    String opheader; 
    opheader = "public" + sync + qual + "void set" + nme + "(" + t +
             " " + val + ") { " + code; 

    if (!instanceScope)
    { opheader = opheader + " }\n\n" + 
        "public" + sync + " void localSet" + nme + "(" + t +
             " " + val + ") { "; 
    }
       
    BasicExpression attxbe = new BasicExpression(val); 
      
    Vector contexts = new Vector(); 
    contexts.add(ent); 
        
    for (int j = 0; j < cons.size(); j++)   // may be constraints of subclass ent
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set of " + cc + " is: " + cnew);
       
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx); 
        if (typed)
        { String update = cnew.updateOperationJava7(ent,nme,true);  
          opheader = opheader + "\n" + 
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    }
    return opheader + "  }\n"; 
  }  // actuators -- include a message?  Should not be local?

  public String setOperationCSharp(Entity ent, Vector cons,
                             Vector entities, Vector types) 
  { // setatt(type attx) 
    // if ent != entity, creates subclass ent extension op for att

    if (frozen) { return ""; }
    String nme = getName();
    Vector v = type.getValues();
    String val = nme + "_x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    par.setElementType(elementType); 

    Vector v1 = new Vector();
    v1.add(par);
    String t = type.getCSharp(); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getCSharp(); }
    // else 
    // { t = "int"; } 

    if (ent.isInterface())
    { return " void set" + nme + "(" + t + " _x);\n"; } 


    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    String qual = " "; 
    String code = ""; 
    String sync = ""; 

    // if (entity.isSequential()) { sync = " synchronized"; } 
    
    if (ent != entity && !entity.isInterface()) 
    { code = "base.set" + nme + "(" + val + ");"; }
    else if (!instanceScope)
    { qual = " static "; 
      code = nme + " = " + val + ";";
    }
    else 
    { code = nme + " = " + val + ";"; } // controller sets static atts, once only 
 
    String opheader; 
    opheader = "public" + sync + qual + "void set" + nme + "(" + t +
             " " + val + ") { " + code; 

    if (!instanceScope)
    { opheader = opheader + " }\n\n" + 
        "public" + sync + " void localSet" + nme + "(" + t +
             " " + val + ") { "; 
    }
       
    BasicExpression attxbe = new BasicExpression(val); 
      
    Vector contexts = new Vector(); 
    contexts.add(ent); 
        
    for (int j = 0; j < cons.size(); j++)   // may be constraints of subclass ent
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      // System.out.println("Match set of " + cc + " is: " + cnew);
       
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx); 
        if (typed)
        { String update = cnew.updateOperationCSharp(ent,nme,true);  
          opheader = opheader + "\n" + 
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    }
    return opheader + "  }\n"; 
  }  // actuators -- include a message?  Should not be local?

  public String setOperationCPP(Entity ent, Vector cons,
                             Vector entities, Vector types) 
  { // setatt(type attx) 
    // if ent != entity, creates subclass ent extension op for att

    if (frozen) { return ""; }
    String nme = getName();
    Vector v = type.getValues();
    String val = nme + "_x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    par.setElementType(elementType); 

    Vector v1 = new Vector();
    v1.add(par);
    String t = type.getCPP(elementType); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getCSharp(); }
    // else 
    // { t = "int"; } 

    // if (ent.isInterface())
    // { return " void set" + nme + "(" + t + " _x);\n"; } 


    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    String qual = " "; 
    String code = ""; 
    String sync = ""; 

    // if (entity.isSequential()) { sync = " synchronized"; } 
    
    // if (ent != entity && !entity.isInterface()) 
    // { code = "super.set" + nme + "(" + val + ");"; }
    // else 
    if (!instanceScope)
    { qual = " static "; 
      code = nme + " = " + val + ";";
    }
    else 
    { code = nme + " = " + val + ";"; } // controller sets static atts, once only 
 
    String opheader; 
    opheader = "  " + qual + "void set" + nme + "(" + t + " " + val + ") { " + code; 

    if (!instanceScope)
    { opheader = opheader + " }\n\n" + 
        "  " + " void localSet" + nme + "(" + t + " " + val + ") { "; 
    }
       
    BasicExpression attxbe = new BasicExpression(val); 
      
    Vector contexts = new Vector(); 
    contexts.add(ent); 
        
    for (int j = 0; j < cons.size(); j++)   // may be constraints of subclass ent
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set of " + cc + " is: " + cnew);
       
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx); 
        if (typed)
        { String update = cnew.updateOperationCPP(ent,nme,true);    
          opheader = opheader + "\n" + 
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    }
    return opheader + "  }\n"; 
  }  // actuators -- include a message?  Should not be local?


  public String addremOperation(Entity ent)
  { String res = "";
    if (!isMultiple()) { return res; }
    if (elementType == null) { return res; }

    String nme = getName();
    String attx = nme + "_x";
    String et = elementType.getJava();
    String wattx = Expression.wrap(elementType,attx); 

    if (ent.isInterface())
    { return "  void add" + nme + "(" + et + " " + attx + ");\n\n" +
             "  void remove" + nme + "(" + et + " " + attx + ");\n\n";
     }
    else if (instanceScope)
    { return "  public void add" + nme + "(" + et + " " + attx + ")\n" + 
             "  { " + nme + ".add(" + wattx + "); }\n\n" +
          "  public void remove" + nme + "(" + et + " " + attx + ")\n" + 
          "  { " + nme + ".remove(" + wattx + "); }\n\n";
    }
    else // static
    { return "  public static void add" + nme + "(" + et + " " + attx + ")\n" + 
             "  { " + nme + ".add(" + wattx + "); }\n\n" +
          "  public static void remove" + nme + "(" + et + " " + attx + ")\n" + 
          "  { " + nme + ".remove(" + wattx + "); }\n\n";
    }
  }

  public Vector sqlSetOperations(Entity ent, Vector cons,
                                 Vector entities, Vector types) 
  { Vector res = new Vector(); 
    if (frozen) { return res; }
    String nme = getName();
    Vector v = type.getValues();
    String val = nme + "x"; 
    Attribute par = new Attribute(val,type,ModelElement.INTERNAL);
    Vector v1 = new Vector();
    v1.add(par);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
 
    String opheader; 
    String ename = ent.getName(); 
    String eId = ename.toLowerCase() + "Id"; // or name of the prim. key of E
    String t = "int " + eId;  
    opheader = "  private void set" + ename + nme + "(" + type.getName() + " " +
                                               nme + "x, " + t + ")\n  { ";
                     
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set: " + cnew); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }

        boolean typed = cnew.typeCheck(types,entities,cntxs); 
        if (typed)
        { Vector update = cnew.sqlOperation(ent,nme,true);
          String condpart = (String) update.get(0); 
          String wheredef = (String) update.get(1);   
          if (condpart.equals("") || condpart.equals("true"))
          { opheader = opheader + " dbi.maintain" + nme + j + "(" + eId + ");\n"; }
          else 
          { opheader = opheader + 
                       " if (" + condpart + ")\n" + 
                       "  { dbi.maintain" + nme + j + "(" + eId + "); }\n"; 
          }  
          System.out.println("Operation: " + opheader + " Code: " + wheredef);  
          res.add("  public synchronized void maintain" + nme + j + 
                  "(int " + eId + ")\n" +
                  "  { try {\n" + 
                  "          PreparedStatement s = connection.prepareStatement(\"" + 
                  wheredef + "\");\n" + 
                  "          s.setInt(1," + eId + ");\n" + 
                  "          s.executeUpdate();\n" + 
                  "          connection.commit(); \n" + 
                  "        } catch (Exception e) { e.printStackTrace(); }\n" + 
                  "  }\n\n"); 
        } 
      }
    }
    opheader = opheader + "  }\n\n"; 
    res.add(0,opheader); 
    return res; 
  }  // actuators -- include a message?  Should not be local?

  public String interfaceSetOperation(Entity ent) 
  { if (frozen) { return ""; }
    if (kind != ModelElement.SEN) 
    { return ""; } 

    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String edec = ename + " " + ex + ","; 
    Vector v = type.getValues();
    String val = nme + "x"; 
 
    String opheader; 
    String t = type.getJava(); 
    // if (v == null) // int or double, String or boolean
    // { t = type.getName(); }
    // else 
    // { t = "int"; } 
    opheader = " public void set" + nme + "(";

    if (instanceScope)
    { opheader = opheader + edec; }
    opheader = opheader + t + " " + val + ");\n  "; 
    return opheader;
  }

  public String setAllOperation(String ename)
  { // public static void setAllatt(List es, T val)
    // { update e.att for e in es }
    if (frozen || isMultiple()) { return ""; } 
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String update = "Controller.inst().set" + nme + "(" + ex + ",val);";
    String typ = type.getJava();
    // String tname = type.getJava();
    // if (tname.equals("boolean") ||
    //     tname.equals("String") ||
    //     tname.equals("double"))
    // { typ = tname; }
    // else // enum or int
    // { typ = "int"; }

    String es = ename.toLowerCase() + "s";
    String res = "  public static void setAll" + nme;
    res = res + "(List " + es + "," + typ + " val)\n";
    res = res + "  { for (int i = 0; i < " + es +
          ".size(); i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(i);\n";
    res = res + "      " + update + " } }\n";
    return res;
  }

  public String setAllOperationJava6(String ename)
  { // public static void setAllatt(Collection es, T val)
    // { update e.att for e in es }
    if (frozen || isMultiple()) { return ""; } 
    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String update = "Controller.inst().set" + nme + "(" + ex + ",val);";
    String typ = type.getJava6();
    // String tname = type.getJava6();
    // if (tname.equals("boolean") ||
    //     tname.equals("String") ||
    //     tname.equals("double"))
    // { typ = tname; }
    // else // enum or int
    // { typ = "int"; }

    String es = ename.toLowerCase() + "_s";
    String res = "  public static void setAll" + nme;
    res = res + "(Collection " + es + "," + typ + " val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + update + " } }\n";
    return res;
  }

  public String setAllOperationJava7(String ename)
  { // public static void setAllatt(Collection es, T val)
    // { update e.att for e in es }
    if (frozen || isMultiple()) { return ""; } 
    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String update = "Controller.inst().set" + nme + "(" + ex + ",val);";
    String typ = type.getJava7(elementType);

    String es = ename.toLowerCase() + "_s";
    String res = "  public static void setAll" + nme;
    res = res + "(Collection<" + ename + "> " + es + "," + typ + " val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + update + " } }\n";
    return res;
  }

  public String setAllOperationCSharp(String ename)
  { // public static void setAllatt(ArrayList es, T val)
    // { update e.att for e in es }
    if (frozen || isMultiple()) { return ""; } 
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String update = "Controller.inst().set" + nme + "(" + ex + ",val);";
    String typ = type.getCSharp();
    // String tname = type.getCSharp();
    // if (tname.equals("bool") ||
    //     tname.equals("string") ||
    //     tname.equals("double"))
    // { typ = tname; }
    // else // enum or int
    // { typ = "int"; }

    String es = ename.toLowerCase() + "_s";
    String res = "  public static void setAll" + nme;
    res = res + "(ArrayList " + es + "," + typ + " val)\n";
    res = res + "  { for (int i = 0; i < " + es +
          ".Count; i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[i];\n";
    res = res + "      " + update + " } }\n";
    return res;
  }

  public String setAllOperationCPP(String ename, Vector declarations)
  { // static void setAllatt(vector<ename*>* es, T val)
    // static void setAllatt(set<ename*>* es, T val)
    // { update e.att for e in es }
    // Declarations are in class E, coding in Controller.cpp

    if (frozen || isMultiple()) { return ""; } 
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String update = "Controller::inst->set" + nme + "(" + ex + ",val);";
    String argtyp1 = "vector<" + ename + "*>*";
    String argtyp2 = "set<" + ename + "*>*"; 
    String tname = type.getCPP(elementType);

    String es = ename.toLowerCase() + "s";
    String declaration = "  static void setAll" + nme;
    declaration = declaration + "(" + argtyp1 + " " + es + "," + tname + " val);\n";

    String res = "  void " + ename + "::setAll" + nme;
    res = res + "(" + argtyp1 + " " + es + "," + tname + " val)\n";
    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      " + update + " }\n";
    res = res + "  }\n\n"; 

    declaration = declaration + "  static void setAll" + nme;
    declaration = declaration + "(" + argtyp2 + " " + es + "," + tname + " val);\n";

    res = res + "  void " + ename + "::setAll" + nme;
    res = res + "(" + argtyp2 + " " + es + "," + tname + " val)\n";
    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      " + update + " }\n";
    res = res + "  }\n\n";

    declarations.add(declaration); 
    return res;
  }


  public String setAllInterfaceOperation(String ename)
  { // void setAllatt(List es, T val);
    return ""; 
  } 

  /*     if (frozen) { return ""; } 
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String typ;
    String tname = type.getName();
    if (tname.equals("boolean") ||
        tname.equals("String") ||
        tname.equals("double"))
    { typ = tname; }
    else // enum or int
    { typ = "int"; }

    String es = ename.toLowerCase() + "s";
    String res = "void setAll" + nme;
    res = res + "(List " + es + "," + typ + " val);\n";
    return res;
  }
    */ 

  public String getOperation(Entity ent)
  { String nme = getName();
    String tn = type.getJava();
    String qual = " ";
    if (!instanceScope)
    { qual = " static "; } 

    if (ent.isInterface())
    { return "  " + tn + " get" + nme + "();\n"; } 

    return "  public" + qual + tn + " get" + nme + "() { " +
           "return " + nme + "; }";
  }

  public String getOperationJava6(Entity ent)
  { String nme = getName();
    String tn = type.getJava6();
    String qual = " ";
    if (!instanceScope)
    { qual = " static "; } 

    if (ent.isInterface())
    { return "  " + tn + " get" + nme + "();\n"; } 

    return "  public" + qual + tn + " get" + nme + "() { " +
           "return " + nme + "; }";
  }

  public String getOperationJava7(Entity ent)
  { String nme = getName();
    String tn = type.getJava7(elementType);
    String qual = " ";
    if (!instanceScope)
    { qual = " static "; } 

    if (ent.isInterface())
    { return "  " + tn + " get" + nme + "();\n"; } 

    return "  public" + qual + tn + " get" + nme + "() { " +
           "return " + nme + "; }";
  }

  public String getOperationCSharp(Entity ent)
  { String nme = getName();
    String tn = type.getCSharp();
    String qual = " ";
    if (!instanceScope)
    { qual = " static "; } 

    if (ent.isInterface())
    { return "  " + tn + " get" + nme + "();\n"; } 

    return "  public" + qual + tn + " get" + nme + "() { " +
           "return " + nme + "; }";
  }

  public String getOperationCPP(Entity ent)
  { String nme = getName();
    String tn = type.getCPP(elementType);
    String qual = " ";
    if (!instanceScope)
    { qual = " static "; } 

    return "  " + qual + tn + " get" + nme + "() { " +
           "return " + nme + "; }";
  }

  public String getAllOperation(Entity ent, String ename)
  { // public static List getAllatt(List es)
    // { return list of e.att for e in es }  es unordered
    if (isMultiple()) { return ""; } 
    if (ent.isInterface())
    { return ""; } // " List getAll" + nme + "(List " + es + ");\n"; } 

    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    if (isStatic())
    { elem = ename + "." + nme; } 
    String item = elem;
    String tname = type.getJava();
    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if (tname.equals("int"))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "s";

    String res = "  public static List getAll" + nme;
    res = res + "(List " + es + ")\n";
    res = res + "  { List result = new Vector();\n";
    if (isStatic())
    { res = res + "   if (" + es + ".size() > 0)\n" + 
            "   { result.add(" + item + "); }\n"; 
    } 
    else  
    { res = res + "    for (int i = 0; i < " + es + ".size(); i++)\n";
      res = res + "    { " + ename + " " + ex + " = (" +
            ename + ") " + es + ".get(i);\n";
      res = res + "      if (result.contains(" + item + ")) { }\n"; 
      res = res + "      else { result.add(" + item + "); } }\n";
    } 
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationJava6(Entity ent, String ename)
  { // public static HashSet getAllatt(Collection es)
    // { return list of e.att for e in es }  and for es unordered
    if (isMultiple()) { return ""; } 
    if (ent.isInterface())
    { return ""; } 
    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    if (isStatic())
    { elem = ename + "." + nme; } 
    String item = elem;
    String tname = type.getJava6();
    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if ("int".equals(tname))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "_s";


    String res = "  public static HashSet getAll" + nme;
    res = res + "(Collection " + es + ")\n";
    res = res + "  { HashSet result = new HashSet();\n";
    if (isStatic())
    { res = res + "   if (" + es + ".size() > 0)\n" + 
            "   { result.add(" + item + "); }\n"; 
    } 
    else  
    { res = res + "    for (Object _o : " + es + ")\n";
      res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
      res = res + "      result.add(" + item + "); }\n";
    } 
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationJava7(Entity ent, String ename)
  { // public static HashSet getAllatt(Collection es)
    // { return list of e.att for e in es }  and for es unordered
    if (isMultiple()) { return ""; } 
    if (ent.isInterface())
    { return ""; } 
    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    if (isStatic())
    { elem = ename + "." + nme; } 
    String item = elem;
    String wtype = type.typeWrapperJava7(); 
    String tname = type.getJava7(elementType);
    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if ("int".equals(tname))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "_s";


    String res = "  public static HashSet<" + wtype + "> getAll" + nme;
    res = res + "(Collection<" + ename + "> " + es + ")\n";
    res = res + "  { HashSet<" + wtype + "> result = new HashSet<" + wtype + ">();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      result.add(" + item + "); }\n";
    res = res + "    return result; }";
    return res;
  } // if attribute is collection-valued, the collections are added as elements of result. 


  public String getAllOperationCSharp(Entity ent, String ename)
  { // public static ArrayList getAllatt(ArrayList es)
    // { return list of e.att for e in es }  es unordered
    if (isMultiple()) { return ""; } 
    if (ent.isInterface())
    { return ""; } // " ArrayList getAll" + nme + "(ArrayList " + es + ");\n"; } 
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    if (isStatic())
    { elem = ename + "." + nme; } 
    String item = elem;
    String tname = type.getCSharp();
    
    String es = ename.toLowerCase() + "_s";


    String res = "  public static ArrayList getAll" + nme;
    res = res + "(ArrayList " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    if (isStatic())
    { res = res + "   if (" + es + ".Count > 0)\n" + 
            "   { result.Add(" + elem + "); }\n"; 
    } 
    else  
    { res = res + "    for (int _i = 0; _i < " + es + ".Count; _i++)\n";
      res = res + "    { " + ename + " " + ex + " = (" + ename + ") " + es + "[_i];\n";
      res = res + "      if (result.Contains(" + item + ")) { }\n"; 
      res = res + "      else { result.Add(" + item + "); } }\n";
    } 
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationCPP(Entity ent, String ename)
  { // public static set<type>* getAllatt(set<ent*>* es)
    // { return list of e.att for e in es }  es unordered
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + "->get" + nme + "()";
    String tname = type.getCPP(elementType);
    String returntype = "set<" + tname + ">*"; 
    String argtype = "set<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";

    String res = "  static " + returntype + " getAll" + nme;
    res = res + "(" + argtype + " " + es + ")\n";
    res = res + "  { " + returntype + " result = new set<" + tname + ">();\n";
    res = res + "    set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      result->insert(" + elem + "); }\n";
    res = res + "    return result; }\n\n";
    
    returntype = "vector<" + tname + ">*"; 
    argtype = "vector<" + ename + "*>*"; 

    res = res + "  static " + returntype + " getAll" + nme;
    res = res + "(" + argtype + " " + es + ")\n";
    res = res + "  { " + returntype + " result = new vector<" + tname + ">();\n";
    res = res + "    vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      result->push_back(" + elem + "); }\n";
    res = res + "    return result; }";
    return res;
  }  // duplicates are preserved here - different behaviour to Java/C#


  // Only needed if some association to E is ONE or ordered:
  public String getAllOrderedOperation(Entity ent, String ename)
  { // public static List getAllatt(List es)
    // { return list of e.att for e in es }  es ordered
    if (ent.isInterface())
    { return " "; } // List getAllOrdered" + nme + "(List " + es + ");\n"; } 
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    String item = elem;
    if (isStatic())
    { item = ename + "." + nme; } 
    String tname = type.getJava();
    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if ("int".equals(tname))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "s";


    String res = "  public static List getAllOrdered" + nme;
    res = res + "(List " + es + ")\n";
    res = res + "  { List result = new Vector();\n";
    res = res + "    for (int i = 0; i < " + es +
          ".size(); i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(i);\n";
    res = res + "      result.add(" + item + "); } \n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationJava6(Entity ent, String ename)
  { // public static List getAllatt(List es)
    // { return list of e.att for e in es }  es ordered
    if (ent.isInterface())
    { return " "; } // List getAllOrdered" + nme + "(List " + es + ");\n"; } 
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    String item = elem;
    if (isStatic())
    { item = ename + "." + nme; } 
    String tname = type.getJava6();
    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if ("int".equals(tname))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "_s";


    String res = "  public static ArrayList getAllOrdered" + nme;
    res = res + "(Collection " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      result.add(" + item + "); } \n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationJava7(Entity ent, String ename)
  { // public static List getAllatt(List es)
    // { return list of e.att for e in es }  es ordered
    if (ent.isInterface())
    { return " "; } // List getAllOrdered" + nme + "(List " + es + ");\n"; } 
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "_x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    String item = elem;
    if (isStatic())
    { item = ename + "." + nme; } 
    String tname = type.getJava7(elementType);
    String wtype = type.typeWrapperJava7(); 

    if (tname.equals("boolean"))
    { item = "new Boolean(" + elem + ")"; }
    else if (tname.equals("String"))
    { item = elem; }
    else if (tname.equals("double"))
    { item = "new Double(" + elem + ")"; }
    else if (tname.equals("long"))
    { item = "new Long(" + elem + ")"; }
    else if ("int".equals(tname))
    { item = "new Integer(" + elem + ")"; }

    String es = ename.toLowerCase() + "_s";


    String res = "  public static ArrayList<" + wtype + "> getAllOrdered" + nme;
    res = res + "(Collection<" + ename + "> " + es + ")\n";
    res = res + "  { ArrayList<" + wtype + "> result = new ArrayList<" + wtype + ">();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      result.add(" + item + "); } \n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationCSharp(Entity ent, String ename)
  { // public static ArrayList getAllatt(ArrayList es)
    // { return list of e.att for e in es }  es ordered
    if (ent.isInterface())
    { return " "; } // ArrayList getAllOrdered" + nme + "(ArrayList " + es + ");\n"; } 
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + ".get" + nme + "()";
    String item = elem;
    if (isStatic())
    { item = ename + "." + nme; } 
    String tname = type.getCSharp();

    String es = ename.toLowerCase() + "_s";


    String res = "  public static ArrayList getAllOrdered" + nme;
    res = res + "(ArrayList " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    res = res + "    for (int i = 0; i < " + es +
          ".Count; i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[i];\n";
    res = res + "      result.Add(" + item + "); } \n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationCPP(Entity ent, String ename)
  { // public static vector<type> getAllatt(vector<ename*>* es)
    // { return list of e.att for e in es }  es ordered
    if (isMultiple()) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String elem = ex + "->get" + nme + "()";
    String argtype = "vector<" + ename + "*>*";
    String tname = type.getCPP(elementType);
    String restype = "vector<" + tname + ">*"; 
 
    String es = ename.toLowerCase() + "s";

    String res = "  static " + restype + " getAllOrdered" + nme;
    res = res + "(" + argtype + " " + es + ")\n";
    res = res + "  { " + restype + " result = new vector<" + tname + ">();\n";
    res = res + "    vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      result->push_back(" + elem + "); }\n";
    res = res + "    return result; }";
    return res;
  }

  // setatt operation for Controller:
  public Vector senOperationsCode(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (frozen) { return res; }  // ??
    String opheader = ""; 
    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String es = ename.toLowerCase() + "s"; 
    Vector vals = type.getValues();
    String attx = nme + "_x"; 
    BasicExpression attxbe = new BasicExpression(attx); 

    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Attribute apar = new Attribute(attx,type,ModelElement.INTERNAL);
    apar.setElementType(elementType); 

    Vector v1 = new Vector();
    if (instanceScope) 
    { v1.add(epar); }
    v1.add(apar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    String t = type.getJava(); 

    // if (vals == null)
    // { t = type.getName(); } 
    // else 
    // { t = "int"; } 

    if (unique || ent.uniqueConstraint(nme))   // instanceScope assumed
    { String indexmap = ename.toLowerCase() + nme + "index";
      String wattx = Expression.wrap(type,attx); 
      String oldatt = Expression.wrap(type,ex + ".get" + nme + "()"); 
      opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { if (" + indexmap + 
             ".get(" + wattx + ") != null) { return; }\n  " +
             indexmap + ".remove(" + oldatt + ");\n  " +  
             ex + ".set" + nme + "(" + attx + ");\n  " +
             indexmap + ".put(" + wattx + "," + ex + ");\n  "; 
    } // should be for any key managed by ent, including superclass atts. 
    else if (instanceScope) 
    { opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
           ex + ".set" + nme + "(" + attx + ");\n  "; 
    } 
    else 
    { opheader = "public void set" + nme + "(" + t +
             " " + attx + ") \n  { " +
             ename + ".set" + nme + "(" + attx + ");\n  " + 
             "for (int i = 0; i < " + es + ".size(); i++)\n" + 
             "  { " + ename + " " + ex + " = (" + ename + ") " + es + ".get(i);\n" +
             "    set" + nme + "(" + ex + "," + attx + "); } }\n\n";
      opheader = opheader + 
             "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
             ex + ".localSet" + nme + "(" + attx + ");\n  ";  
    } // very peculiar, why do you want to do this?

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx);
        if (typed)
        { String update = cnew.globalUpdateOp(ent,false);
          opheader = opheader +
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    } 
    res.add(opheader + "  }\n\n");
    return res; 
  }

  public Vector addremOperationsCode(Entity ent)
  { Vector res = new Vector();
    if (!isMultiple()) { return res; }
    if (elementType == null) { return res; }

    String opheader = "";
    String ename = ent.getName();
    String ex = ename.toLowerCase() + "x";
    String nme = getName();
    String attx = nme + "_x";
    String et = elementType.getJava();

    if (instanceScope)
    { opheader = "  public void add" + nme + "(" + ename + " " + ex + ", " + et + " " + attx + ")\n" +
         "  { " + ex + ".add" + nme + "(" + attx + "); }\n\n";
       res.add(opheader);
       String removeop = "  public void remove" + nme + "(" + ename + " " + ex + ", " + et + " " + attx + ")\n" +
         "  { " + ex + ".remove" + nme + "(" + attx + "); }\n\n";
       res.add(removeop);
    }
   else
    { opheader = "  public void add" + nme + "(" + et + " " + attx + ")\n" +
         "  { " + ename + ".add" + nme + "(" + attx + "); }\n\n";
       res.add(opheader);
       String removeop = "  public void remove" + nme + "(" + et + " " + attx + ")\n" +
         "  { " + ename + ".remove" + nme + "(" + attx + "); }\n\n";
       res.add(removeop);
    }
    return res;
  }  


  // setatt operation for Controller:
  public Vector senOperationsCodeJava6(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (frozen) { return res; }  // ??
    String opheader = ""; 
    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String es = ename.toLowerCase() + "s"; 
    Vector vals = type.getValues();
    String attx = nme + "_x"; 
    BasicExpression attxbe = new BasicExpression(attx); 

    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Attribute apar = new Attribute(attx,type,ModelElement.INTERNAL);
    apar.setElementType(elementType); 

    Vector v1 = new Vector();
    if (instanceScope) 
    { v1.add(epar); }
    v1.add(apar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    String t = type.getJava6(); 

    // if (vals == null)
    // { t = type.getName(); } 
    // else 
    // { t = "int"; } 

    if (unique || ent.uniqueConstraint(nme))   // instanceScope assumed
    { String indexmap = ename.toLowerCase() + nme + "index";
      String wattx = Expression.wrap(type,attx); 
      String oldatt = Expression.wrap(type,ex + ".get" + nme + "()"); 
      opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { if (" + indexmap + 
             ".get(" + wattx + ") != null) { return; }\n  " +
             indexmap + ".remove(" + oldatt + ");\n  " +  
             ex + ".set" + nme + "(" + attx + ");\n  " +
             indexmap + ".put(" + wattx + "," + ex + ");\n  "; 
    } 
    else if (instanceScope) 
    { opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
           ex + ".set" + nme + "(" + attx + ");\n  "; 
    } 
    else 
    { opheader = "public void set" + nme + "(" + t +
             " " + attx + ") \n  { " +
             ename + ".set" + nme + "(" + attx + ");\n  " + 
             "for (int i = 0; i < " + es + ".size(); i++)\n" + 
             "  { " + ename + " " + ex + " = (" + ename + ") " + es + ".get(i);\n" +
             "    set" + nme + "(" + ex + "," + attx + "); } }\n\n";
      opheader = opheader + 
             "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
             ex + ".localSet" + nme + "(" + attx + ");\n  ";  
    } // very peculiar, why do you want to do this?

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx);
        if (typed)
        { String update = cnew.globalUpdateOpJava6(ent,false);
          opheader = opheader +
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    } 
    res.add(opheader + "  }\n\n");
    return res; 
  }

  // setatt operation for Controller:
  public Vector senOperationsCodeJava7(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (frozen) { return res; }  // ??
    String opheader = ""; 
    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String es = ename.toLowerCase() + "s"; 
    Vector vals = type.getValues();
    String attx = nme + "_x"; 
    BasicExpression attxbe = new BasicExpression(attx); 

    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Attribute apar = new Attribute(attx,type,ModelElement.INTERNAL);
    apar.setElementType(elementType); 

    Vector v1 = new Vector();
    if (instanceScope) 
    { v1.add(epar); }
    v1.add(apar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    String t = type.getJava7(elementType); 

    // if (vals == null)
    // { t = type.getName(); } 
    // else 
    // { t = "int"; } 

    if (unique || ent.uniqueConstraint(nme))   // instanceScope assumed
    { String indexmap = ename.toLowerCase() + nme + "index";
      String wattx = Expression.wrap(type,attx); 
      String oldatt = Expression.wrap(type,ex + ".get" + nme + "()"); 
      opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { if (" + indexmap + 
             ".get(" + wattx + ") != null) { return; }\n  " +
             indexmap + ".remove(" + oldatt + ");\n  " +  
             ex + ".set" + nme + "(" + attx + ");\n  " +
             indexmap + ".put(" + wattx + "," + ex + ");\n  "; 
    } 
    else if (instanceScope) 
    { opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
           ex + ".set" + nme + "(" + attx + ");\n  "; 
    } 
    else 
    { opheader = "public void set" + nme + "(" + t +
             " " + attx + ") \n  { " +
             ename + ".set" + nme + "(" + attx + ");\n  " + 
             "for (int _i = 0; _i < " + es + ".size(); _i++)\n" + 
             "  { " + ename + " " + ex + " = (" + ename + ") " + es + ".get(_i);\n" +
             "    set" + nme + "(" + ex + "," + attx + "); } }\n\n";
      opheader = opheader + 
             "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
             ex + ".localSet" + nme + "(" + attx + ");\n  ";  
    } // very peculiar, why do you want to do this?

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx);
        if (typed)
        { String update = cnew.globalUpdateOpJava7(ent,false);
          opheader = opheader +
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    } 
    res.add(opheader + "  }\n\n");
    return res; 
  }

  public Vector senOperationsCodeCSharp(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (frozen) { return res; }  // ??
    String opheader = ""; 
    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String es = ename.toLowerCase() + "_s"; 
    Vector vals = type.getValues();
    String attx = nme + "_x"; 
    BasicExpression attxbe = new BasicExpression(attx); 

    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Attribute apar = new Attribute(attx,type,ModelElement.INTERNAL);
    Vector v1 = new Vector();
    if (instanceScope) 
    { v1.add(epar); }
    v1.add(apar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    String t = type.getCSharp(); 

    // if (vals == null)
    // { t = type.getCSharp(); } 
    // else 
    // { t = "int"; } 

    if (unique || ent.uniqueConstraint(nme))   // instanceScope assumed
    { String indexmap = ename.toLowerCase() + nme + "index";
      String wattx = attx; 
      String oldatt = ex + ".get" + nme + "()"; 
      opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { if (" + indexmap + 
             "[" + wattx + "] != null) { return; }\n  " +
             indexmap + ".Remove(" + oldatt + ");\n  " +  
             ex + ".set" + nme + "(" + attx + ");\n  " +
             indexmap + "[" + wattx + "] = " + ex + ";\n  "; 
    } 
    else if (instanceScope) 
    { opheader = "public void set" + nme + "(" + ename + " " +
             ex + ", " + t + " " + attx + ") \n  { " +
           ex + ".set" + nme + "(" + attx + ");\n  "; 
    } 
    else 
    { opheader = "public void set" + nme + "(" + t +
             " " + attx + ") \n  { " +
             ename + ".set" + nme + "(" + attx + ");\n  " + 
             "for (int i = 0; i < " + es + ".Count; i++)\n" + 
             "  { " + ename + " " + ex + " = (" + ename + ") " + es + "[i];\n" +
             "    set" + nme + "(" + ex + "," + attx + "); } }\n\n";
      opheader = opheader + 
             "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
             ex + ".localSet" + nme + "(" + attx + ");\n  ";  
    } // very peculiar, why do you want to do this?

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx);
        if (typed)
        { String update = cnew.globalUpdateOpCSharp(ent,false);
          opheader = opheader +
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    } 
    res.add(opheader + "  }\n\n");
    return res; 
  }

  public Vector senOperationsCodeCPP(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (frozen) { return res; }  // ??
    String opheader = ""; 
    String nme = getName();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String es = ename.toLowerCase() + "_s"; 
    Vector vals = type.getValues();
    String attx = nme + "_x"; 
    BasicExpression attxbe = new BasicExpression(attx); 

    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Attribute apar = new Attribute(attx,type,ModelElement.INTERNAL);
    apar.setElementType(elementType); 

    Vector v1 = new Vector();
    if (instanceScope) 
    { v1.add(epar); }
    v1.add(apar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    String t = type.getCPP(elementType); 

    // if (vals == null)
    // { t = type.getCSharp(); } 
    // else 
    // { t = "int"; } 

    if (unique || ent.uniqueConstraint(nme))   // instanceScope assumed
    { String indexmap = ename.toLowerCase() + nme + "index";
      String wattx = attx; 
      String oldatt = ex + "->get" + nme + "()"; 
      opheader = "  void set" + nme + "(" + ename + "* " +
             ex + ", " + t +
             " " + attx + ") \n  { if (" + indexmap + ".find(" + wattx + ") != " + 
                                           indexmap + ".end()) { return; }\n  " +
             indexmap + ".erase(" + oldatt + ");\n  " +  
             ex + "->set" + nme + "(" + attx + ");\n  " +
             indexmap + "[" + wattx + "] = " + ex + ";\n  "; 
    } 
    else if (instanceScope) 
    { opheader = "  void set" + nme + "(" + ename + "* " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
           ex + "->set" + nme + "(" + attx + ");\n  "; 
    } 
    else 
    { opheader = "  void set" + nme + "(" + t +
             " " + attx + ") \n  { " +
             ename + "::set" + nme + "(" + attx + ");\n  " + 
             "for (int _i = 0; _i < " + es + "->size(); _i++)\n" + 
             "  { " + ename + "* " + ex + " = " + es + "->at(_i);\n" +
             "    set" + nme + "(" + ex + "," + attx + "); } }\n\n";
      opheader = opheader + 
             "  void set" + nme + "(" + ename + "* " +
             ex + ", " + t +
             " " + attx + ") \n  { " +
             ex + "->localSet" + nme + "(" + attx + ");\n  ";  
    } // very peculiar, why do you want to do this?

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector contx = new Vector(); 
        if (cnew.getOwner() != null) 
        { contx.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,contx);
        if (typed)
        { String update = cnew.globalUpdateOpCPP(ent,false);
          opheader = opheader +
                     update + "\n";
        } 
      }
      else if (cc.allFeaturesUsedIn().contains(nme) && cc.getEvent() == null)
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }
    } 
    res.add(opheader + "  }\n\n");
    return res; 
  }

  // For controller:
  public Vector senBOperationsCode(Vector cons, Entity ent, 
                                   Vector entities, Vector types)
  { Vector res = new Vector();
    if (frozen)
    { return res; } // no updates permitted
    String nme = getName();
    String ename = ent.getName();
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex);
    java.util.Map env = new java.util.HashMap(); 
    env.put(ename,exbe); 

    Vector pars = new Vector(); 
    pars.add(ex); 
    BExpression pre = new BBinaryExpression(":",exbe,esbe); 
    // Vector vals = type.getValues();
    // if (vals == null) // its an int, String or boolean
    // {
      String attx = nme + "x";
      String btype = type.generateB();
      pars.add(attx); 
      BParallelStatement stat = new BParallelStatement(); 
      BExpression attbe = new BBasicExpression(attx);
      BExpression btbe = new BBasicExpression(btype);
      pre = new BBinaryExpression("&",pre,new BBinaryExpression(":",attbe,btbe)); 
      if (unique)   // instanceScope assumed
      { BSetExpression bs1 = new BSetExpression();
        bs1.addElement(exbe); 
        BExpression bmin = new BBinaryExpression("-",esbe,bs1); 
        BExpression bapp = new BApplySetExpression(nme,bmin); 
        BExpression compar = new BBinaryExpression("/:",attbe,bapp); 
        pre = new BBinaryExpression("&",pre,compar); 
      } 

      Vector callpars = new Vector(); 
      if (instanceScope)
      { callpars.add(exbe); }
      callpars.add(attbe); 
      BStatement opcall = new BOperationCall("set" + nme,callpars); 
      opcall.setWriteFrame(nme); 
      stat.addStatement(opcall);
      Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
      Vector v1 = new Vector();
      if (instanceScope) 
      { v1.add(epar); }
      Attribute attpar = new Attribute(attx,type,ModelElement.INTERNAL);
      v1.add(attpar); 
      BehaviouralFeature event =
        new BehaviouralFeature("set" + nme,v1,false,null);

      Vector contexts = new Vector(); 
      contexts.add(ent); 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.bmatches("set",nme,ent,attx,event);
        System.out.println("senBOperationsCode, new constraint: " + cnew); 

        if (cnew != null)
        { Constraint c2 = cnew.normalise(); 
          System.out.println("senBOperationsCode, normalised constraint: " + c2); 

          Vector cntxs = new Vector(); 
          if (c2.getOwner() != null) 
          { cntxs.add(c2.getOwner()); } 

          boolean typed = c2.typeCheck(types,entities,cntxs);
          System.out.println("Typed?: " + typed); 
          if (typed)
          { BStatement update;
            if (instanceScope)
            { System.out.println("instance scope"); 
              update = c2.synthesiseBCode(ent,nme,false); 
            }
            else 
            { update = c2.staticSynthesiseBCode(ent,nme,false); }  
          // bupdateOperation(ent,false);
            stat.addStatement(update);
          } 
        }
        else if (cc.getEvent() == null &&
                 cc.allFeaturesUsedIn().contains(nme))
        { BExpression inv = // cc.binvariantForm(env,false);
                            cc.bprecondition(env);  
          inv.setBrackets(true); 
          BExpression inv1 = inv.substituteEq(nme + "(" + ex + ")", attbe);  
          pre = new BBinaryExpression("&",pre,inv1); 
        } // must ensure that not both an invariant and its contrapositive are used!
 
      }
      BStatement newbody = BStatement.separateUpdates(stat.getStatements()); 
      BOp op = new BOp("set_" + nme,null,pars,pre,newbody);  
      res.add(op); 
      

      return res;
   /* }
    for (int i = 0; i < vals.size(); i++)
    { String val = (String) vals.get(i);
      BExpression valbe = new BBasicExpression(val); 
      BParallelStatement stat = new BParallelStatement();
      Vector callpars = new Vector(); 
      callpars.add(exbe); 
      callpars.add(valbe); 
      BStatement opcall = new BOperationCall("set" + nme,callpars); 
      stat.addStatement(opcall);
      Attribute epar = new Attribute(ex,new Type(ent),INTERNAL); 
      Vector v1 = new Vector();
      if (instanceScope) 
      { v1.add(epar); }
      BehaviouralFeature event =
        new BehaviouralFeature("set" + nme + val,v1,false,null);
          Vector contexts = new Vector(); 
    contexts.add(ent); 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.bmatches("set",nme,ent,val,event);
        if (cnew != null)
        { boolean typed = cnew.typeCheck(types,entities,contexts);
          if (typed)
          { BStatement update = cc.bupdateOperation(ent,false);
            stat.addStatement(update);
          } 
        } 
      }
      BOp op = new BOp(nme + val,null,pars,pre,stat);  
      res.add(op); 
    }
    return res; */
  }

  public String getUniqueCheckCode(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
    String ex = ename.toLowerCase() + "x";

    if (unique || ent.uniqueConstraint(nme))
    { String arg = Expression.wrap(type,nme + "x"); 
      res = "    " + ex + " = (" + ename + ") " + ename.toLowerCase() + nme + "index.get(" + 
            arg + ");\n"; 
      res = res + "    if (" + ex + " != null) { return " + ex + "; }\n"; 
    }
    return res; 
  }

  public String getUniqueCheckCodeCSharp(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
      
    if (unique || ent.uniqueConstraint(nme))
    { String arg = nme + "x"; 
      res = "    if (" + ename.toLowerCase() + nme + "index[" + 
            arg + "] != null) { return null; }\n"; 
    }
    return res; 
  }

  public String getUniqueCheckCodeCPP(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
      
    if (unique || ent.uniqueConstraint(nme))
    { String arg = nme + "x"; 
      String indexmap = ename.toLowerCase() + nme + "index"; 

      res = "    if (" + indexmap + ".find(" + arg + ") != " + 
                         indexmap + ".end()) { return 0; }\n"; 
    }
    return res; 
  }


  public String getUniqueUpdateCode(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
      
    if (unique || ent.uniqueConstraint(nme))
    { String arg = Expression.wrap(type,nme + "x");
      res = "    " + ename.toLowerCase() + nme + "index.put(" + arg + "," + 
            ename.toLowerCase() + "x);\n"; 
    }
    return res; 
  }

  public String getUniqueUpdateCodeCSharp(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
      
    if (unique || ent.uniqueConstraint(nme))
    { String arg = nme + "x";
      res = "    " + ename.toLowerCase() + nme + "index[" + arg + "] = " + 
            ename.toLowerCase() + "x;\n"; 
    }
    return res; 
  }

  public String getUniqueUpdateCodeCPP(Entity ent, String ename)
  { String res = ""; 
    String nme = getName(); 
      
    if (unique || ent.uniqueConstraint(nme))
    { String arg = nme + "x";
      res = "    " + ename.toLowerCase() + nme + "index[" + arg + "] = " + 
            ename.toLowerCase() + "x;\n"; 
    }
    return res; 
  }

  public String getCreateCode(Entity ent, String ex)
  { // setatt(ex,initval) -- initval is attx for frozen
    if (frozen) { return ""; }  // passed into constructor instead

    String nme = getName();
    String ini = null;
    String op;
    // if (instanceScope)
    { op = "    set" + nme + "(" + ex + ","; }
    // else 
    // { op = "    set" + nme + "("; }
    if (unique || ent.uniqueConstraint(nme))
    { ini = nme + "x"; } // but can't call set anyway.
    /* else if (initialValue != null &&
             !initialValue.equals(""))
    { ini = initialValue; }
    else
    { ini = type.getDefault(); } */ 
    if (ini == null) { return ""; }
    return op + ini + ");\n";
  } // order so derived are last?

  public String getCreateCodeJava6(Entity ent, String ex)
  { // setatt(ex,initval) -- initval is attx for frozen
    if (frozen) { return ""; }  // passed into constructor instead

    String nme = getName();
    String ini;
    String op;
    // if (instanceScope)
    { op = "    set" + nme + "(" + ex + ","; }
    // else 
    // { op = "    set" + nme + "("; }
    if (unique || ent.uniqueConstraint(nme))
    { ini = nme + "x"; } // but can't call set anyway.
    else if (initialExpression != null) 
    { ini = initialExpression.queryFormJava6(new java.util.HashMap(), true); } 
    else
    { ini = type.getDefaultJava6(); }
    if (ini == null) { return ""; }
    return op + ini + ");\n";
  } // order so derived are last?

  public String getCreateCodeJava7(Entity ent, String ex)
  { // setatt(ex,initval) -- initval is attx for frozen
    if (frozen) { return ""; }  // passed into constructor instead

    String nme = getName();
    String ini;
    String op;
    // if (instanceScope)
    { op = "    set" + nme + "(" + ex + ","; }
    // else 
    // { op = "    set" + nme + "("; }
    if (unique || ent.uniqueConstraint(nme))
    { ini = nme + "x"; } // but can't call set anyway.
    // else if (initialValue != null &&
    //          !initialValue.equals(""))
    // { ini = initialValue; }
    else
    { ini = type.getDefaultJava7(); }
    if (ini == null) { return ""; }
    return op + ini + ");\n";
  } // order so derived are last?

  public String getCreateCodeCSharp(Entity ent, String ex)
  { // setatt(ex,initval) -- initval is attx for frozen
    if (frozen) { return ""; }  // passed into constructor instead

    String nme = getName();
    String ini;
    String op;
    // if (instanceScope)
    { op = "    set" + nme + "(" + ex + ","; }
    // else 
    // { op = "    set" + nme + "("; }
    if (unique || ent.uniqueConstraint(nme))
    { ini = nme + "x"; } // but can't call set anyway.
    else if (initialExpression != null) 
    { ini = initialExpression.queryFormCSharp(new java.util.HashMap(), true); } 
    else
    { ini = type.getDefaultCSharp(); }
    if (ini == null) { return ""; }
    return op + ini + ");\n";
  } // order so derived are last?

  public String getCreateCodeCPP(Entity ent, String ex)
  { // setatt(ex,initval) -- initval is attx for frozen
    if (frozen) { return ""; }  // passed into constructor instead

    String nme = getName();
    String ini;
    String op;
    // if (instanceScope)
    { op = "    set" + nme + "(" + ex + ","; }
    // else 
    // { op = "    set" + nme + "("; }
    if (unique || ent.uniqueConstraint(nme))
    { ini = nme + "x"; } // but can't call set anyway.
    else if (initialExpression != null) 
    { ini = initialExpression.queryFormCPP(new java.util.HashMap(), true); } 
    else 
    { ini = type.getDefaultCPP(elementType); }
    if (ini == null) { return ""; }
    return op + ini + ");\n";
  } // order so derived are last?

  public String toXml()
  { return "    <feature xmi:type=\"UML:Attribute\" " +
           "name=\"" + getName() + "\" type=\"" +
           getType().getName() + "\"/>\n";
  }

  public String extractCode()
  { String att = getName();
    return "    String " + att +
           " = req.getParameter(\"" + att + "\");\n";
  }

// for strings, booleans and ints, not enum (radio butt)
  public String getHtmlGen()
  { String nme = getName();
    String lbl = nme + "Label";
    String fld = name + "Field";
    String res = 
      "    HtmlText " + lbl + " = " +
      "new HtmlText(\"" + nme + "\",\"strong\");\n" + 
      "    form.add(" + lbl + ");\n" +
      "    HtmlInput " + fld + " = new HtmlInput();\n" + 
      "    " + fld +
      ".setAttribute(\"type\",\"text\");\n" +
      "    " + fld + ".setAttribute(\"name\",\"" + nme +
      "\");\n" +
      "    form.add(" + fld + ");\n" +
      "    form.add(para);\n";
    return res;
  }

  public String getFormInput()
  { String nme = getName();
    String label = nme; 
    String res = ""; 

    if (type != null && type.isEnumerated())
    { Vector vals = type.getValues(); 
      res = "<p><strong>" + label + "</strong>\n" + 
            "<select name=\"" + nme + "\">\n"; 
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        res = res + "  <option value=\"" + val + "\">" + val + "</option>\n"; 
      } 
      res = res + "</select>\n"; 
      return res; 
    } 

    if (type != null && type.isEntity())
    { Entity etype = type.getEntity(); 
      Attribute pk = etype.getPrincipalPrimaryKey(); 
      if (pk != null)
      { label = nme + " (" + pk.getName() + ")"; } 
    } 

    res = 
      "<p><strong>" + label + "</strong>\n" +
      "<input type = \"text\" name = \"" + nme + "\"/></p>\n";
    return res;
  }

  public String getBeanForm()
  { String res = "";
    String attname = getName();
    String tname = type.getName();
    if (tname.equals("int") || tname.equals("long"))
    { res = "i" + attname; }
    else if (tname.equals("double"))
    { res = "d" + attname; } 
    else if (type.isEnumerated())
    { res = "e" + attname; } 
    else 
    { res = attname; } 
    return res; 
  } // case of long? 
    

  public String getServletCheckCode()
  { String res = "";
    String attname = getName();
    String tname = type.getName();
    if (tname.equals("int"))
    { String iatt = "i" + attname;
      res = "    int " + iatt + " = 0;\n";
      res = res + "    try { " + iatt + 
            " = Integer.parseInt(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errorPage.addMessage(" + 
            attname + " + \" is not an integer\"); }\n";
      return res;
    }
    else if (tname.equals("long"))
    { String iatt = "i" + attname;
      res = "    int " + iatt + " = 0;\n";
      res = res + "    try { " + iatt + 
            " = Long.parseLong(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errorPage.addMessage(" + 
            attname + " + \" is not a long integer\"); }\n";
      return res;
    }
    else if (tname.equals("double"))
    { String datt = "d" + attname;
      res = "    double " + datt + " = 0;\n";
      res = res + "    try { " + datt + 
            " = Double.parseDouble(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errorPage.addMessage(" + 
            attname + " + \" is not a double\"); }\n";
      return res;
    }
    else if (type.isEnumerated())
    { String eatt = "e" + attname; 
      Vector vals = type.getValues(); 
      
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 

        res = res + "    if (" + attname + ".equals(\"" + val + "\"))\n" + 
                    "    { " + eatt + " = " + tname + "." + val + "; } else\n"; 
      } 
      res = res + "    { errorPage.addMessage(\"" + attname + " is not in type " + type + "\"); }\n"; 
    } 
    else if (type.isEntity())
    { res = res + 
            "    instance_" + attname + " = model.get" + tname + "ByPK(" + attname + ");\n" +  
	       "    if (instance_" + attname + " == null)\n" + 
	       "    { errorPage.addMessage(\"" + attname + " must be a valid " + tname + " id\"); }\n"; 
    } 
    else if (type.isCollection())
    { res = res + 
            "    String[] split_" + attname + " = " + attname + ".split(\" \");\n" + 
            "    for (int _i = 0; _i < split_" + attname + ".length; _i++)\n" + 
            "    { s" + attname + ".add(split_" + attname + "[_i]); }\n" + 
	       "    if (s" + attname + ".size() == 0)\n" + 
	       "    { errorPage.addMessage(\"" + attname + " must have one or more values\"); }\n"; 
     } 
    // enumerations 

    if (hasStereotype("email"))
    { String atindex = attname + "_index";
      res = "    int " + atindex + " = " + attname + 
            ".indexOf('@');\n";
      res = res + "    if (" + atindex +
            " <= 0 || " + atindex + " >= " +
            attname + ".length() - 1)\n" +
            "    { errorPage.addMessage(\"" + 
            "not an email address:\" + " + attname + 
            "); }\n";
      return res;
    }

    if (hasStereotype("nonempty"))
    { res = res + "    if (" +
            attname + ".length() == 0)\n" +
            "    { errorPage.addMessage(\"" + 
            "empty data: \" + " + attname + 
            "); }\n";
      return res;
    }        

    return res;
  }

  public String getBeanCheckCode()
  { String res = "";
    String attname = getName();
    String tname = type.getName();
    
    if (tname.equals("int"))
    { String iatt = "i" + attname;
      res = "    try { " + iatt + 
            " = Integer.parseInt(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errors.add(\"" + 
            attname + " is not an integer\"); }\n";
      return res;
    }
    else if (tname.equals("long"))
    { String iatt = "i" + attname;
      res = "    try { " + iatt + 
            " = Long.parseLong(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errors.add(\"" + 
            attname + " is not a long integer\"); }\n";
      return res;
    }
    else if (tname.equals("double"))
    { String datt = "d" + attname;
      res = "    try { " + datt + 
            " = Double.parseDouble(" + attname + "); }\n";
      res = res + "    catch (Exception e)\n" +
            "    { errors.add(\"" + 
            attname + " is not a double\"); }\n";
      return res;
    }
    else if (type.isEnumerated())
    { String eatt = "e" + attname; 
      Vector vals = type.getValues(); 
      
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 

        res = res + "    if (" + attname + ".equals(\"" + val + "\"))\n" + 
                    "    { " + eatt + " = " + tname + "." + val + "; } else\n"; 
      } 
      res = res + "    { errors.add(\"" + attname + " is not in type " + type + "\"); }\n"; 
    } 
    else if (type.isEntity())
    { res = res + 
            "    instance_" + attname + " = model.get" + tname + "ByPK(" + attname + ");\n" +  
	       "    if (instance_" + attname + " == null)\n" + 
	       "    { errors.add(\"" + attname + " must be a valid " + tname + " id\"); }\n"; 
    } 
    else if (type.isCollection())
    { res = res + 
            "    String[] split_" + attname + " = " + attname + ".split(\" \");\n" + 
            "    for (int _i = 0; _i < split_" + attname + ".length; _i++)\n" + 
            "    { s" + attname + ".add(split_" + attname + "[_i]); }\n" + 
	       "    if (s" + attname + ".size() == 0)\n" + 
	       "    { errors.add(\"" + attname + " must have one or more values\"); }\n"; 
     } 

    if (hasStereotype("email"))
    { String atindex = attname + "_index";
      res = "    int " + atindex + " = " + attname + 
            ".indexOf('@');\n";
      res = res + "    if (" + atindex +
            " <= 0 || " + atindex + " >= " +
            attname + ".length() - 1)\n" +
            "    { errors.add(\"" + 
            "not an email address: " + attname + 
            "\"); }\n";
      return res;
    }

    if (hasStereotype("nonempty"))
    { res = res + "    if (" +
            attname + ".length() == 0)\n" +
            "    { errors.add(\"" + 
            "empty data: " + attname + 
            "\"); }\n";
      return res;
    }        
    return res;
  }

  public String getIOSCheckCode()
  { String res = "";
    String attname = getName();
    String tname = type.getName();

    /* if (type.isEntity())
    { res = res + 
            "    let instance_" + attname + " = " + tname + ".getByPK" + tname + "(" + attname + ")\n" +  
	       "    if instance_" + attname + " == nil\n" + 
	       "    { errors.append(\"" + attname + " must be a valid " + tname + " id\") }\n"; 
    } */ 


    if (hasStereotype("email"))
    { String atindex = attname + "_index";
      res = "    int " + atindex + " = " + 
            "Ocl.indexOf(str: " + attname + ", ch: \"@\")\n";
      res = res + "    if (" + atindex +
            " <= 1 || " + atindex + " >= " +
            attname + ".count)\n" +
            "    { errors.append(\"" + 
            "not an email address: " + attname + 
            "\") }\n";
      return res;
    }

    if (hasStereotype("nonempty"))
    { res = res + "    if " +
            attname + ".count == 0\n" +
            "    { errors.append(\"" + 
            "empty data: " + attname + 
            "\") }\n";
      return res;
    }        
    return res;
  }

  public String jdbcExtractOp(String resultSet)
  { String res = resultSet + ".get";
    String tname = type.getName();
    if (tname.equals("int"))
    { res = res + "Int(\""; }
    else if (tname.equals("double"))
    { res = res + "Double(\""; }
    else // strings, booleans, enums
    { res = res + "String(\""; }
    res = res + getName() + "\")";
    return res;
  }

  public String rawDecoder(String m)
  { String res = "";
    String attname = getName();
    String tname = type.getName();
    if (tname.equals("int"))
    { res = "(int) ((Long) " + m + ".get(\"" + attname + "\")).longValue();"; }
    else if (tname.equals("long"))
    { res = "((Long) " + m + ".get(\"" + attname + "\")).longValue();"; }
    else if (tname.equals("double"))
    { res = "((Double) " + m + ".get(\"" + attname + "\")).doubleValue();"; } 
    else if (type.isBoolean())
    { res = "((Boolean) " + m + ".get(\"" + attname + "\")).booleanValue();"; } 
    else if (type.isString())
    { res = "(String) " + m + ".get(\"" + attname + "\");"; } 
    else 
    { res = m + ".get(\"" + attname + "\");"; } 

    return res; 
  } // TODO: case of lists and maps? 

public String dbType()
{ String tname = type.getName();
  if ("int".equals(tname))
  { return "integer"; }
  else if ("String".equals(tname))
  { return "VARCHAR(50)"; }
  else if ("boolean".equals(tname))
  { return "VARCHAR(5)"; }
  else 
  { return tname; }
}

public String androidExtractOp(String ent)
{ String allcaps = name.toUpperCase();
 
  String tname = type.getName();
  if ("String".equals(tname))
  { return "cursor.getString(" + ent + "_COL_" + allcaps + ")"; }
  if ("int".equals(tname))
  { return "cursor.getInt(" + ent + "_COL_" + allcaps + ")"; }
  if ("long".equals(tname))
  { return "cursor.getLong(" + ent + "_COL_" + allcaps + ")"; }
  if ("double".equals(tname))
  { return "cursor.getDouble(" + ent + "_COL_" + allcaps + ")"; }
  else
  { return "cursor.getString(" + ent + "_COL_" + allcaps + ")"; }
}

public String iosExtractOp(String ent, int i)
{ String allcaps = name.toUpperCase();
  String nme = getName(); 
 
  String tname = type.getName();
  
  if ("int".equals(tname))
  { return "      guard let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_int(queryStatement, " + i + ")\n" + 
           "      else { return res }\n" +  
           "      let " + nme + " = Int(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else if ("long".equals(tname))
  { return "      guard let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_int64(queryStatement, " + i + ")\n" + 
           "      else { return res }\n" +  
           "      let " + nme + " = Int(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else if ("double".equals(tname))
  { return "      guard let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_double(queryStatement, " + i + ")\n" + 
           "      else { return res }\n" +  
           "      let " + nme + " = Double(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else // ("String".equals(tname))
  { return "      guard let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_text(queryStatement, " + i + ")\n" + 
           "      else { return res }\n" +  
           "      let " + nme + " = String(cString: queryResult" + ent + "_COL" + allcaps + ")"; 
  }
}

public String iosDbiExtractOp(String ent, int i)
{ String allcaps = name.toUpperCase();
  String nme = getName(); 
 
  String tname = type.getName();
  
  if ("int".equals(tname))
  { return "      let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_int(queryStatement, " + i + ")\n" + 
           "      let " + nme + " = Int(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else if ("long".equals(tname))
  { return "      let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_int64(queryStatement, " + i + ")\n" + 
           "      let " + nme + " = Int(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else if ("double".equals(tname))
  { return "      let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_double(queryStatement, " + i + ")\n" + 
           "      let " + nme + " = Double(queryResult" + ent + "_COL" + allcaps + ")";
  }
  else // ("String".equals(tname))
  { return "      guard let queryResult" + ent + "_COL" + allcaps + " = sqlite3_column_text(queryStatement, " + i + ")\n" + 
           "      else { return res }\n" +  
           "      let " + nme + " = String(cString: queryResult" + ent + "_COL" + allcaps + ")"; 
  }
}

/*   public String androidExtractOp(String resultSet)
  { String res = resultSet + ".get";
    String tname = type.getName();
    if (tname.equals("int"))
    { res = res + "Int(\""; }
    else if (tname.equals("double"))
    { res = res + "Double(\""; }
    else // strings, booleans, enums
    { res = res + "String(\""; }
    res = res + getName() + "\")";
    return res;
  } */ 

  public String ejbBeanGet()
  { String nme = getName(); 
    String tname = type.getJava(); 
    String fl = nme.substring(0,1); 
    String rem = nme.substring(1,nme.length()); 
    return "  public abstract " + tname + " get" + fl.toUpperCase() + 
           rem + "();"; 
  } 

  public String ejbBeanSet()
  { String nme = getName(); 
    String tname = type.getJava(); 
    String fl = nme.substring(0,1); 
    String rem = nme.substring(1,nme.length()); 
    return "  public abstract void set" + fl.toUpperCase() + 
           rem + "(" + tname + " " + nme.toLowerCase() + "x);"; 
  }

  public String guidec()
  { String res = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("int") || tname.equals("String") ||
         tname.equals("double"))
    { res = "  JLabel " + nme + "Label;\n" + 
               "  JTextField " + nme + "Field;\n";
    }
    else if (tname.equals("boolean"))
    { res = "  JCheckBox " + nme + "Box, not" + nme + "Box;\n" +
        "  JPanel " + nme + "Panel;\n" +
        "  ButtonGroup " + nme + "Group;\n";
    }
    return res;
  }

  public String guidef()
  { String res = "";
    String addcode = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("int") || tname.equals("String") ||
         tname.equals("double"))
    { res = "  " + nme + "Label = new JLabel(\"" + nme + ":\");\n" + 
               "  " + nme + "Field = new JTextField();\n";
      addcode = addcode + "  add(" + nme + "Label);\n" +
              "  add(" + nme + "Field);\n";
    }
    else if (tname.equals("boolean"))
    { res = "  " + nme + "Box = new JCheckBox(\"" + nme + "\");\n" +
        "  not" + nme + "Box = new JCheckBox(\"not" + nme + "\");\n" +
        "  " + nme + "Panel = new JPanel();\n" +
        "  " + nme + "Group = new ButtonGroup();\n" +
        "  " + nme + "Panel.add(" + nme + "Box);\n" +
        "  " + nme + "Panel.add(not" + nme + "Box);\n" +
        "  " + nme + "Panel.setBorder(BorderFactory.createTitledBorder(\"" + nme + "\"));\n" +
        "  " + nme + "Group.add(" + nme + "Box);\n" +
        "  " + nme + "Group.add(not" + nme + "Box);\n";
      addcode = addcode + "  add(" + nme + "Panel);\n";
    }
    return res + addcode;
  }

  public String guiattinit()
  { String res = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("int") || tname.equals("String") ||
         tname.equals("double"))
    { res = "  " + nme + "Field.setText(" + nme + "x + \"\");\n";  }
    else if (tname.equals("boolean"))
    { res = "  if (" + nme + "x) { " + nme + 
            "Box.setSelected(true); } else { not" +             
            nme + "Box.setSelected(true); }\n";
    }
    return res;
  }

  public String guifieldpar1()
  { String res = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("int") || tname.equals("String") ||
         tname.equals("double"))
    { res = "dialogPanel." + nme + "Field.getText()";  }
    else if (tname.equals("boolean"))
    { res = "dialogPanel." + nme + "Box.isSelected()"; }
    return res;
  }

  public String guifieldpar2()
  { String res = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("int") || tname.equals("String") ||
         tname.equals("double"))
    { res = "null";  }
    else if (tname.equals("boolean"))
    { res = "false"; }
    return res;
  }

  public String guiconversioncode()
  { String res = "";
    if (type == null) { return res; }

    String nme = getName();
    Vector vals = type.getValues();
    String tname = type.getName();

    if (tname.equals("String") || tname.equals("boolean"))
    { res = "new" + nme + " = " + nme + "x;\n";  }
    else if (tname.equals("int"))
    { res = "try { new" + nme + " = Integer.parseInt(" + nme + "x); }\n" +
         "      catch(Exception e) { } \n";
    }
    else if (tname.equals("double"))
    { res = "try { new" + nme + " = Double.parseDouble(" + nme + "x); }\n" +
         "      catch(Exception e) { } \n";
    }
    return res;
  }

  public double similarityDist(Attribute att, String tename, Map mm, Vector entities) 
  { String ts = type + ""; 
    String ats = att.getType() + ""; 

    int mysteps = steps(); 
    int tsteps = att.steps(); 
    double stepsfactor = 1.0/(Math.abs(tsteps - mysteps) + 1); 

    double tsim = similarity(att,tename,mm,entities); 
    return tsim + stepsfactor; 
  } 

  public static double asim(Attribute satt, Attribute tatt, Map mm, Vector entities) 
  { if (exactTypeMatch(satt,tatt,mm))
    { return 1; } 
    return partialTypeMatch(satt,tatt,mm,entities); 
  } 

  public static boolean exactTypeMatch(Attribute satt, Attribute tatt, Map mm)
  { Type t1 = satt.getType(); 
    Type t2 = tatt.getType(); 
    Type elemt1 = satt.getElementType(); 
    Type elemt2 = tatt.getElementType(); 

    if (tatt.isUnique())
    { if (satt.isUnique())
      { return true; } // both must be strings
      else 
      { return false; } // can't assign a non-key to a key
    } 
    
    if (t1.isEntity()) 
    { if (t2.isEntity())
      { Entity e1 = t1.getEntity(); 
        Entity e2 = t2.getEntity(); 
        Entity e1mapped = (Entity) mm.get(e1); 
        if (e1mapped == null) 
        { return false; } 
        else if (e1mapped.getName().equals(e2.getName()))
        { return true; }
        return false; 
      } 
      return false; 
    } 
    else if (elemt1 != null && elemt1.isEntity()) // ignore difference of sequences and sets of entities
    { if (elemt2 != null && elemt2.isEntity())
      { Entity e1 = elemt1.getEntity(); 
        Entity e2 = elemt2.getEntity(); 
        Entity e1mapped = (Entity) mm.get(e1); 
        if (e1mapped == null) 
        { return false; } 
        else if (e1mapped.getName().equals(e2.getName()))
        { return true; }
        return false; 
      } 
      return false; 
    } 
    
    if ((t1 + "").equals(t2 + "")) // e1 is basic
    { return true; } 
    return false; 
  } 

  public static boolean exactTypeMatchRel(Attribute satt, Attribute tatt, Map mm)
  { Type t1 = satt.getType(); 
    Type t2 = tatt.getType(); 
    Type elemt1 = satt.getElementType(); 
    Type elemt2 = tatt.getElementType(); 

    if (tatt.isUnique())
    { if (satt.isUnique())
      { return true; } // both must be strings
      else 
      { return false; } // can't assign a non-key to a key
    } 
    
    if (t1.isEntity()) 
    { if (t2.isEntity())
      { Entity e1 = t1.getEntity(); 
        Entity e2 = t2.getEntity(); 
        Vector e1mapped = mm.getAll(e1); 
        if (e1mapped.size() == 0) 
        { return false; } 
        else 
        { Vector targetnames = ModelElement.getNames(e1mapped); 
          if (targetnames.contains(e2.getName()))
          { return true; }
          return false;
        }  
      } 
      return false; 
    } 
    else if (elemt1 != null && elemt1.isEntity()) // ignore difference of sequences and sets of entities
    { if (elemt2 != null && elemt2.isEntity())
      { Entity e1 = elemt1.getEntity(); 
        Entity e2 = elemt2.getEntity(); 
        Vector e1mapped = mm.getAll(e1); 
        if (e1mapped.size() == 0) 
        { return false; } 
        else 
        { Vector targetnames = ModelElement.getNames(e1mapped); 
          if (targetnames.contains(e2.getName()))
          { return true; }
          return false; 
        }
      } 
      return false; 
    } 
    
    if ((t1 + "").equals(t2 + "")) // e1 is basic
    { return true; } 
    return false; 
  } 


  public static double partialTypeMatch(Attribute satt, Attribute tatt, Map mm, Vector entities)
  { Type t1 = satt.getType(); 
    Type t2 = tatt.getType(); 
    Type elemt1 = satt.getElementType(); 
    Type elemt2 = tatt.getElementType(); 
    

    if (t1.isEntity()) 
    { Entity e1 = t1.getEntity(); 
      Entity e1mapped = (Entity) mm.get(e1); 
      if (e1mapped == null) 
      { return 0; } 
        
      if (t2.isEntity())
      { Entity e2 = t2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
         
        return satt.similarity(tatt,e1mapped.getName(),mm,entities);  
      } 
      else if (elemt2 != null && elemt2.isEntity())
      { Entity e2 = elemt2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
        return satt.similarity(tatt,e1mapped.getName(),mm,entities); 
      }
      return 0; 
    } 
    else if (elemt1 != null && elemt1.isEntity()) // ignore difference of sequences and sets of entities
    { Entity e1 = elemt1.getEntity(); 
      Entity e1mapped = (Entity) mm.get(e1); 
      if (e1mapped == null) 
      { return 0; } 
        
      if (t2.isEntity())
      { Entity e2 = t2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
         
        return satt.similarity(tatt,e1mapped.getName(),mm,entities);  
      } 
      else if (elemt2 != null && elemt2.isEntity())
      { Entity e2 = elemt2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
        // else if (e1mapped.getName().equals(e2.getName()))
        // { return 1; }
        return satt.similarity(tatt,e1mapped.getName(),mm,entities); 
      } 
      return 0; 
    } 
    
    if ((t1 + "").equals(t2 + "")) // e1 is basic
    { return 1; } 
    return satt.similarity(tatt,elemt1 + "",mm,entities); 
  } 

  public static double partialTypeMatchRel(Attribute satt, Attribute tatt, Map mm, Vector entities)
  { Type t1 = satt.getType(); 
    Type t2 = tatt.getType(); 
    Type elemt1 = satt.getElementType(); 
    Type elemt2 = tatt.getElementType(); 
    

    if (t1.isEntity()) 
    { Entity e1 = t1.getEntity(); 
      Vector e1mapped = mm.getAll(e1); 
      if (e1mapped.size() == 0) 
      { return 0; } 
        
      if (t2.isEntity())
      { Entity e2 = t2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
         
        return satt.maxSimilarity(tatt,e1mapped,mm,entities);  
      } 
      else if (elemt2 != null && elemt2.isEntity())
      { Entity e2 = elemt2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
        return satt.maxSimilarity(tatt,e1mapped,mm,entities); 
      }
      return 0; 
    } 
    else if (elemt1 != null && elemt1.isEntity()) // ignore difference of sequences and sets of entities
    { Entity e1 = elemt1.getEntity(); 
      Vector e1mapped = mm.getAll(e1); 
      if (e1mapped.size() == 0) 
      { return 0; } 
        
      if (t2.isEntity())
      { Entity e2 = t2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
         
        return satt.maxSimilarity(tatt,e1mapped,mm,entities);  
      } 
      else if (elemt2 != null && elemt2.isEntity())
      { Entity e2 = elemt2.getEntity(); 
        // System.out.println("--->> Trying partial type match of " + t1 + "(" + e1mapped + ") " + 
        //                t2 + "(" + elemt2 + ")"); 
        // else if (e1mapped.getName().equals(e2.getName()))
        // { return 1; }
        return satt.maxSimilarity(tatt,e1mapped,mm,entities); 
      } 
      return 0; 
    } 
    
    if ((t1 + "").equals(t2 + "")) // e1 is basic
    { return 1; } 
    return satt.similarity(tatt,elemt1 + "",mm,entities); 
  } 

  public double findExactTypeMatchSameName(Vector eatts, String sstr, 
                                           Vector matched, Vector ematched)
  { String attname = getName(); 

    for (int j = 0; j < eatts.size(); j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else if (isSource() && eatt.isSource()) { } 
      else if (eatt.isUnique() && !(this.isUnique())) { } // can't match non-key to key
      { String estr = eatt.getType() + ""; 

        if (sstr.equals(estr))
        { if (attname.equals(eattname))
          { matched.add(this); 
            ematched.add(eatt); 
            return 1; 
          } 
        } 
      } 
    } 
    return 0; 
  } 
 

  public double findExactTypeMatch(Vector eatts, String sstr, Vector matched, Vector ematched,
                                   Vector thesaurus)
  { double bestsim = 0; 
    Attribute best = null; 
    String attname = getName(); 

    for (int j = 0; j < eatts.size(); j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else if (isSource() && eatt.isSource()) { } 
      else if (eatt.isUnique() && !(this.isUnique())) { } // can't match non-key to key
      { String estr = eatt.getType() + ""; 

        if (sstr.equals(estr))
        { double sim = ModelElement.similarity(attname,eattname); 
          double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
          double csim = sim + 2*nmssim - (sim*nmssim); 

          // System.out.println(">> Exact type match of " + this + " to " + 
          //                    eattname + " == " + csim); 

          if (csim > bestsim) 
          { best = eatt; 
            bestsim = csim; 
          }
            // else if (sim == bestsim && eatt.steps() < best.steps())
            // { best = eatt; } // prefer direct attributes
        } 
      } 
    } 

    if (bestsim > 0 && !ematched.contains(best)) 
    { matched.add(this); 
      ematched.add(best); 
      return 1; 
    } 
    return 0; 
  } 

  public double findPartialTypeMatch(Vector eatts, String testring, 
                                     Map mm, Vector entities, Vector matched, Vector ematched, 
                                     Vector thesaurus)
  { double maxscore = 0;
    double bestsim = 0; 
    Attribute maxmatch = null;
    int esize = eatts.size();   
    String attname = getName(); 
          
    for (int j = 0; j < esize; j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else 
      { double ascore = similarity(eatt,testring,mm,entities); 

        // System.out.println(">> partial type match of " + this + " to " + 
        //                      eattname + " == " + ascore); 

        if (ascore > maxscore) 
        { maxscore = ascore; 
          maxmatch = eatt; 
          bestsim = ModelElement.similarity(attname,eattname); 
          double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
          bestsim = (bestsim + 2*nmssim) - (bestsim*nmssim); 
        } 
        else if (ascore == maxscore) 
        { double sim = ModelElement.similarity(attname,eattname); 
          double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
          double csim = sim + 2*nmssim - (sim*nmssim); 
          if (csim > bestsim) 
          { maxmatch = eatt; 
            bestsim = csim; 
          } 
                // else if (sim == bestsim && eatt.steps() < maxmatch.steps())
                // { maxmatch = eatt; } 
        } 
      } 
    } 
    if (maxscore > 0 && !ematched.contains(maxmatch))
    { matched.add(this); 
      ematched.add(maxmatch); 
      return maxscore; 
    }  
    return 0; 
  }

  public double findPartialBasicMatch(Vector eatts, Map mm, Vector entities, 
                                      Vector matched, Vector ematched, Vector thesaurus)
  { double bestsim = 0; 
    double maxscore = 0;
    Attribute maxmatch = null;
    int esize = eatts.size(); 
    String attname = getName(); 
  
    for (int j = 0; j < esize; j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else 
      { String testring = getElementType() + ""; 
        double ascore = similarity(eatt,testring,mm,entities); 
            // if (ascore > 0) 
            // { System.out.println("PARTIAL match of " + att + " " + testring + " " + eatt + " " + ascore); } 

        if (ascore > maxscore) 
        { maxscore = ascore; 
          maxmatch = eatt; 
          bestsim = ModelElement.similarity(attname,eattname); 
          double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
          bestsim = (bestsim + 2*nmssim) - (bestsim*nmssim); 
        } 
        else if (ascore == maxscore)
        { double sim = ModelElement.similarity(attname,eattname); 
          double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
          double csim = sim + 2*nmssim - (sim*nmssim); 
          if (csim > bestsim) 
          { maxmatch = eatt; 
            bestsim = csim; 
          }
              // else if (sim == bestsim && eatt.steps() < maxmatch.steps())
              // { maxmatch = eatt; } 
        } 
      } 
    } 
    if (maxscore > 0 && !ematched.contains(maxmatch))
    { matched.add(this); 
      ematched.add(maxmatch); 
      return maxscore; 
    }  
    return 0; 
  } 

  public double findNMSMatch(Vector eatts, Vector matched, Vector ematched, Vector attmaps,
                             Map mm, Vector entities, Vector thesaurus)
  { double bestsim = 0; 
    Attribute best = null; 
    String attname = getName(); 

    for (int j = 0; j < eatts.size(); j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else if (isSource() && eatt.isSource()) { } 
      else if (isTarget() && eatt.isTarget()) { } 
      else if (eatt.isUnique() && !(this.isUnique())) { } // can't match non-key to key
      { double nmssim = Entity.nmsSimilarity(attname,eattname,thesaurus); 
        double tsim = Type.typeSimilarity(type,eatt.getType(),mm,entities);
        // System.out.println(">> type-similarity of " + this + " " + eatt + " is " + tsim); 
        if (nmssim > bestsim && tsim > 0) 
        { best = eatt; 
          bestsim = nmssim; 
        } 
      } 
    } 

    if (bestsim > 0 && !ematched.contains(best)) 
    { matched.add(this); 
      if (attname.equals(best.getName()))
      { ematched.add(best); }  
      AttributeMatching am = new AttributeMatching(this,best); 
      attmaps.add(am); 
      return bestsim; 
    } 
    return 0; 
  } 

  public double findTypeMatch(Vector eatts, Vector matched, Vector ematched,
                              Vector entities, Map mm)
  { double bestsim = 0; 
    Attribute best = null; 
    String attname = getName(); 

    for (int j = 0; j < eatts.size(); j++) 
    { Attribute eatt = (Attribute) eatts.get(j); 
      String eattname = eatt.getName(); 

      if (ematched.contains(eatt)) { } 
      else if (isSource() && eatt.isSource()) { } 
      else if (isTarget() && eatt.isTarget()) { } 
      else if (eatt.isUnique() && !(this.isUnique())) { } // can't match non-key to key
      { double tsim = Type.typeSimilarity(type,eatt.getType(),mm,entities);
        // System.out.println(">__> type-similarity of " + this + " " + eatt + " is " + tsim); 

        if (tsim > bestsim) 
        { best = eatt; 
          bestsim = tsim; 
        } 
      } 
    } 

    if (bestsim > 0 && !ematched.contains(best)) 
    { matched.add(this); 
      ematched.add(best); 
      return bestsim; 
    } 
    return 0; 
  } 

  public double maxSimilarity(Attribute att, Vector targetEntities, Map mm, Vector entities)
  { double res = 0; 
  
    for (int i = 0; i < targetEntities.size(); i++) 
	{ Entity tent = (Entity) targetEntities.get(i); 
	  String tname = tent.getName(); 
	  double sim = similarity(att,tname,mm,entities); 
	  if (sim > res) 
	  { res = sim; }
	}
	return res; 
  }
  
  public double similarity(Attribute att, String tename, Map mm, Vector entities) 
  { String ts = type + ""; 
    String ats = att.getType() + ""; 

    // Also check that both are unique or that neither are unique. Match = 0 otherwise. 

    if (ts.equals(ats)) // only shared classes have the same name in both metamodels
    { return 1.0; } 

    if (type == null) { return 0; } 
    if (att.getType() == null) { return 0; } 

    Type typetatt = att.getType(); 

    String tn1 = type.getName(); 
    String tn2 = typetatt.getName(); 
  
    String etn1 = elementType + ""; 
    if (elementType == null) 
    { etn1 = tn1; } 
    String etn2 = att.getElementType() + "";
    if (att.getElementType() == null) 
    { etn2 = tn2; } 
 
    if ("null".equals(tename)) { } 
    else 
    { etn1 = tename; } 

    // System.out.println("Checking similarity of " + tn1 + "(" + etn1 + ")" + upper + " " +
    //                    tn2 + "(" + etn2 + ")" + att.getUpper()); 

    if ("int".equals(tn1) && "long".equals(tn2))
    { return ModelMatching.INTLONG; } 
    else if ("long".equals(tn1) && "int".equals(tn2))
    { return ModelMatching.LONGINT; } 

    if (type.isEnumerated() && "String".equals(tn2))
    { return ModelMatching.ENUMSTRING; } 
    else if ("String".equals(tn1) && typetatt.isEnumerated())
    { return ModelMatching.STRINGENUM; }     

    if (type.isEnumerated() && typetatt.isEnumerated())
    { return ModelElement.similarity(getName(),att.getName())*Type.enumSimilarity(type,typetatt); } 

    if (type.isEnumerated() && Type.isNumericType(tn2))
    { return 0; } 

    if (typetatt.isEnumerated() && Type.isNumericType(tn1))
    { return 0; } 

    if (typetatt.isEnumerated() && "boolean".equals(tn1))
    { return ModelMatching.BOOLENUM*typetatt.enumBooleanSimilarity(getName()); } 

    if (type.isEnumerated() && "boolean".equals(tn2))
    { return ModelMatching.BOOLENUM*type.enumBooleanSimilarity(att.getName()); } 

    if (etn1.equals(tn2) && upper == 1)  // 0..1 T matches to 1..1 T
    { return ModelMatching.OPTONE; } 
    else if (etn2.equals(tn1) && att.getUpper() == 1) 
    { return ModelMatching.ONEOPT; } 

    if (tn1.equals("Set") && tn2.equals("Sequence") || 
        tn2.equals("Set") && tn1.equals("Sequence"))
    { if (etn1.equals(etn2))
      { return ModelMatching.SETSEQUENCE; } 
    } // both collections, of different kinds but same element types
    else if (tn1.equals("Set") && etn1.equals(etn2))
    { return ModelMatching.SETONE; } // one is collection of same type as the other single element
    else if (tn1.equals("Sequence") && etn1.equals(etn2))
    { return ModelMatching.SEQUENCEONE; } 
    else if (tn2.equals("Set") && etn1.equals(etn2))
    { // System.out.println("<> Trying to match " + tn1 + "(" + etn1 + ") with"); 
      // System.out.println("<> " + tn2 + "(" + etn2 + ")"); 
      return ModelMatching.ONESET; 
    } 
    else if (tn2.equals("Sequence") && etn1.equals(etn2))
    { return ModelMatching.ONESEQUENCE; }
    // also case where att is a role of type 
    // a superclass or subclass of this.type.entity
    else if (Type.isSubType(type,typetatt,mm,entities))
    { return ModelMatching.SUBSUPER; } 
    else if (Type.isSubType(typetatt,type,mm,entities))
    { return ModelMatching.SUPERSUB; }     
 
    return Type.typeSimilarity(type,typetatt,mm,entities); 
  } // int does not match to enum, but boolean can match to an enum of size 2


  public Vector testCases(String x, java.util.Map lowerBnds, java.util.Map upperBnds)
  { Vector res = new Vector(); 
    if (type == null) 
    { return res; } 

    String nme = x + "." + getName(); 
    String t = type.getName(); 
    Vector vs = type.getValues(); 

    String nmx = getName(); 
    Expression lbnd = (Expression) lowerBnds.get(nmx); 
    Expression ubnd = (Expression) upperBnds.get(nmx); 

    if ("int".equals(t))
    { res.add(nme + " = 0"); 
      res.add(nme + " = -1");
      res.add(nme + " = 1"); 

      if (ubnd != null && lbnd != null)
      { try
        { double ud = Double.parseDouble(ubnd + ""); 
          double ld = Double.parseDouble(lbnd + ""); 
          int midd = (int) Math.floor((ud + ld)/2); 
          res.add(nme + " = " + midd);
        } catch (Exception _e) { } 
      }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(nme + " = " + upperval); }
      }  
      else 
      { res.add(nme + " = 2147483647"); } // Integer.MAX_VALUE);

      if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(nme + " = " + lowerval); }
      }  
      else 
      { res.add(nme + " = -2147483648"); } // Integer.MIN_VALUE);
    } 
    else if ("long".equals(t))
    { res.add(nme + " = 0"); 
      res.add(nme + " = -1");
      res.add(nme + " = 1"); 
	  
	  if (ubnd != null && lbnd != null)
	  { try
	    { double ud = Double.parseDouble(ubnd + ""); 
	      double ld = Double.parseDouble(lbnd + ""); 
		  long midd = (long) Math.floor((ud + ld)/2); 
		  res.add(nme + " = " + midd);
		} catch (Exception _e) { } 
	  }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(nme + " = " + upperval); }
      }  
      else 
      { res.add(nme + " = " + Long.MAX_VALUE); } 

      if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(nme + " = " + lowerval); }
      }  
      else 
      { res.add(nme + " = " + Long.MIN_VALUE); } 
    } 
    else if ("double".equals(t))
    { res.add(nme + " = 0"); 
      res.add(nme + " = -1");
      res.add(nme + " = 1"); 
	  
	  if (ubnd != null && lbnd != null)
	  { try
	    { double ud = Double.parseDouble(ubnd + ""); 
	      double ld = Double.parseDouble(lbnd + ""); 
		  double midd = (ud + ld)/2; 
		  res.add(nme + " = " + midd);
		} catch (Exception _e) { } 
	  }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(nme + " = " + upperval); }
      }  
      else 
      { res.add(nme + " = " + Double.MAX_VALUE); } 
	  
	  if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(nme + " = " + lowerval); }
      }  
      else 
      { res.add(nme + " = " + Double.MIN_VALUE); }
      
    } 
    else if ("boolean".equals(t))
    { res.add(nme + " = true"); 
      res.add(nme + " = false");
    }
    else if ("String".equals(t))
    { res.add(nme + " = \"\""); 
      res.add(nme + " = \" abc_XZ \"");
      res.add(nme + " = \"#�$* &~@':\"");
    }
    else if (vs != null && vs.size() > 0) 
    { for (int j = 0; j < vs.size(); j++)   
      { String v0 = (String) vs.get(j); 
        res.add(nme + " = " + v0); 
      } 
    }
    else if (type.isEntity())
    { String obj = t.toLowerCase() + "x_0"; 
        // Identifier.nextIdentifier(t.toLowerCase()); 
      String decl = nme + " = " + obj; 
      res.add(decl); 
    }  
    else if (type.isCollection() && elementType != null)
    { Type elemT = getElementType(); 
      Vector testVals = elemT.testValues(); 
      res.add(""); 
	  
	  // Singletons: 
      for (int p = 0; p < testVals.size() && p < 3; p++) 
      { String tv = (String) testVals.get(p); 
        res.add(tv + " : " + nme); 
      }
	  
	  // Triples: 
      for (int p = 0; p+2 < testVals.size() && p < 3; p++) 
      { String tv = (String) testVals.get(p); 
        String tv1 = (String) testVals.get(p+1);
		String tv2 = (String) testVals.get(p+2);  
        res.add(tv + " : " + nme + "\n" + tv1 + " : " + nme + "\n" + tv2 + " : " + nme); 
      }
    } 
 
    return res;  
  } 

  public Vector testValues(String x, java.util.Map lowerBnds, java.util.Map upperBnds)
  { Vector res = new Vector(); 
    if (type == null) 
    { return res; } 

    String nme = x + "." + getName(); 
    String t = type.getName(); 
    Vector vs = type.getValues(); 

    String nmx = getName(); 
    Expression lbnd = (Expression) lowerBnds.get(nmx); 
    Expression ubnd = (Expression) upperBnds.get(nmx); 

    if ("int".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 

      if (ubnd != null && lbnd != null)
      { try
        { double ud = Double.parseDouble(ubnd + ""); 
          double ld = Double.parseDouble(lbnd + ""); 
          int midd = (int) Math.floor((ud + ld)/2); 
          res.add("" + midd);
        } catch (Exception _e) { } 
      }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(upperval); }
      }  
      else 
      { res.add("2147483647"); } // Integer.MAX_VALUE);

      if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(lowerval); }
      }  
      else 
      { res.add("-2147483648"); } // Integer.MIN_VALUE);
    } 
    else if ("long".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 
	  
	  if (ubnd != null && lbnd != null)
	  { try
	    { double ud = Double.parseDouble(ubnd + ""); 
	      double ld = Double.parseDouble(lbnd + ""); 
		  long midd = (long) Math.floor((ud + ld)/2); 
		  res.add("" + midd);
		} catch (Exception _e) { } 
	  }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(upperval); }
      }  
      else 
      { res.add(Long.MAX_VALUE + "L"); } 

      if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(lowerval); }
      }  
      else 
      { res.add(Long.MIN_VALUE + "L"); } 
    } 
    else if ("double".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 
	  
	  if (ubnd != null && lbnd != null)
	  { try
	    { double ud = Double.parseDouble(ubnd + ""); 
           double ld = Double.parseDouble(lbnd + ""); 
           double midd = (ud + ld)/2; 
           res.add("" + midd);
	    } catch (Exception _e) { } 
	  }

      if (ubnd != null) 
      { String upperval = ubnd + ""; 
        if ("0".equals(upperval) || "1".equals(upperval) || "-1".equals(upperval)) { } 
        else 
        { res.add(upperval); }
      }  
      else 
      { res.add("" + Double.MAX_VALUE); } 
	  
	  if (lbnd != null) 
      { String lowerval = lbnd + ""; 
        if ("0".equals(lowerval) || "1".equals(lowerval) || "-1".equals(lowerval)) { } 
        else 
        { res.add(lowerval); }
      }  
      else 
      { res.add("" + Double.MIN_VALUE); }
      
    } 
    else if ("boolean".equals(t))
    { res.add("true"); 
      res.add("false");
    }
    else if ("String".equals(t))
    { res.add("\"\""); 
      res.add("\" abc_XZ \"");
      res.add("\"#�$* &~@':\"");
    }
    else if (vs != null && vs.size() > 0) 
    { for (int j = 0; j < vs.size(); j++)   
      { String v0 = (String) vs.get(j); 
        res.add(v0); 
      } 
    }
    else if (type.isEntity())
    { String obj = t.toLowerCase() + "x_0"; 
        // Identifier.nextIdentifier(t.toLowerCase()); 
      String decl = obj; 
      res.add(decl); 
    }  
    /* else if (type.isCollection() && elementType != null)
    { Type elemT = getElementType(); 
      Vector testVals = elemT.testValues(); 
      res.add(""); 
      for (int p = 0; p < testVals.size(); p++) 
      { String tv = (String) testVals.get(p); 
        res.add(tv + " : " + nme); 
      }
    } */ 
 
    return res;  
  } 

public String androidEntryField(String op, String previous, String ent)
{ String nme = getName();
  String label = Named.capitalise(nme);
  String attfield = op + nme;
  String attlabel = op + nme + "Label";
  String hint = ent + " " + nme; 

  String res1 = "  <TextView\n\r" +
    "    android:id=\"@+id/" + attlabel + "\"\n\r" +
    "    android:layout_width=\"wrap_content\"\n\r" +
    "    android:layout_height=\"wrap_content\"\n\r" +
    "    android:hint=\"" + hint + "\"\n\r" +
    "    android:textStyle=\"bold\"\n\r" +
    "    android:text=\"" + label + ":\"\n\r"; 
  if (previous != null) 
  { res1 = res1 + "    android:layout_below=\"@id/" + previous + "\"\n\r"; } 
  res1 = res1 + 
    "    android:layout_alignBaseline=\"@+id/" + attfield + "\"\n\r" +
    "    android:layout_alignParentLeft=\"true\"/>\n\r";

  String res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n\r" +
    "    android:layout_width=\"match_parent\"\n\r" +
    "    android:layout_height=\"wrap_content\"\n\r" +
    "    android:layout_toRightOf=\"@id/" + attlabel + "\"/>\n\r";
  return res1 + res2;
}
  
public String androidEntryFieldName(String op)
{ String nme = getName();
  String attlabel = op + nme + "Label";
  return attlabel; 
} 


public String androidTableEntryField(String ent, String op)
{ String nme = getName();
  String label = Named.capitalise(nme);
  String attfield = op + ent + nme + "Field";
  String attlabel = op + ent + nme + "Label";
  String hint = ent + " " + nme; 

  String res1 = "  <TextView\n\r" +
    "    android:id=\"@+id/" + attlabel + "\"\n" +
    "    android:hint=\"" + hint + "\"\n" +
    "    android:textStyle=\"bold\"\n" +
    "    android:background=\"#EEFFBB\"\n" + 
    "    android:text=\"" + label + ":\" />\n\r";

  String res2 = ""; 
  // if (isIdentity())
  // { res2 = "  <TextView\n\r" +
  //   "    android:id=\"@id/" + attfield + "\"\n\r" +
  //   "    android:layout_span=\"3\" />\n\r";
  // } 
  // else 
  // android:inputType="textPassword"

 
  if (isSmallEnumeration()) // no more than 4 elements
  { res2 = androidRadioButtonGroup(op + ent + nme); }
  else if (isLargeEnumeration() || isEntity())
  { res2 = androidSpinner(op + ent + nme); }
  else if (isInteger())
  { res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n" +
    "    android:inputType=\"number\"\n" +  
    "    android:layout_span=\"4\" />\n\r";
  } 
  else if (isDouble())
  { res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n" +
    "    android:inputType=\"number|numberDecimal\"\n" +  
    "    android:layout_span=\"4\" />\n\r";
  } 
  else if (isPassword())
  { res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n" +
    "    android:inputType=\"textPassword\"\n" +  
    "    android:layout_span=\"4\" />\n";
  } 
  else if (isCollection())
  { res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n" +
    "    android:inputType=\"text|textMultiLine\"\n" +
	"    android:minLines=\"5\"\n" + 
	"    android:gravity=\"top\"\n" +   
    "    android:layout_span=\"4\" />\n\r"; }
  else  
  { res2 = "  <EditText\n\r" +
    "    android:id=\"@+id/" + attfield + "\"\n" +
    "    android:layout_span=\"4\" />\n\r";
  } 
  return "  <TableRow>\n\r" +
         res1 + res2 + 
         "  </TableRow>\n\r";
}  // email, password kinds also 

  public String androidRadioButtonGroup(String fullop) 
  { Type t = getType(); 
    Vector vals = t.getValues(); 

    String res = 
      "  <RadioGroup\n\r" + 
      "    android:id=\"@+id/" + fullop + "Group\"\n" + 
      "    android:orientation=\"horizontal\"\n" +
      "    android:layout_span=\"4\"\n" +
      "    android:layout_width=\"fill_parent\"\n" +
      "    android:layout_height=\"wrap_content\" >\n\r"; 
    for (int i = 0; i < vals.size(); i++) 
    { String val = (String) vals.get(i);      
      res = res + "    <RadioButton android:id=\"@+id/" + fullop + val + "\"\n\r" +
            "      android:layout_width=\"wrap_content\"\n" +
            "      android:layout_height=\"wrap_content\"\n" +
            "      android:text=\"" + val + "\" />\n\r"; 
     } 
    res = res +         
      "  </RadioGroup>\n\r"; 
    return res; 
  } 

  public String androidSpinner(String fullop) 
  { 
    String res = 
       "  <Spinner\n\r" +
       "    android:id=\"@+id/" + fullop + "Spinner\"\n" + 
       "    android:layout_width=\"fill_parent\"\n" + 
       "    android:layout_height=\"wrap_content\"\n" + 
       "    android:layout_span=\"4\" />\n\r"; 
    return res; 
  } 

  public String extractEnumerationValue(String op, String attdata)
  {  String res = "";
     String nme = getName(); 
     String group = op + nme + "Group";  
     Vector vals = type.getValues(); 
     for (int i = 0; i < vals.size(); i++) 
     { String val = (String) vals.get(i); 
       res = res + "   if (" + group + ".getCheckedRadioButtonId() == R.id." + op + nme + val + ")" + 
       " { " + attdata + " = \"" + val + "\"; }\n\r"; 
       if (i < vals.size() - 1) 
       { res = res + "    else\n\r"; } 
     } 
     return res; 
  } 

  public String setEnumerationValue(String op, String attdata)
  {  String res = "";
     String nme = getName(); 
     String group = op + nme + "Group";  
     Vector vals = type.getValues(); 
     String tname = type.getName();

     for (int i = 0; i < vals.size(); i++) 
     { String val = (String) vals.get(i); 
       res = res + "   if (" + attdata + " == " + tname + "." + val + ")\n" + 
                   "   { " + group + ".check(R.id." + op + nme + val + ");\n"; 
       if (i < vals.size() - 1) 
       { res = res + "    else\n"; } 
     } 
     return res; 
  } 

  public String androidValueList()
  { if (isLargeEnumeration())
    { Vector vals = type.getValues(); 
      String res = "{"; 
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        res = res + "\"" + val + "\""; 
        if (i < vals.size() - 1)
        { res = res + ", "; } 
      } 
      return res + "}";
	} 
	else if (isEntity())
	{ String ename = type.getName(); 
	  return "ModelFacade.getInstance(myContext).all" + ename + "ids()"; 
	} 
	return "{}"; 
  } 


  public String androidSpinnerInitialisation(String op, String nme, String root, String context)
  { String res = "    " + op + nme + "Spinner = (Spinner) " + root + "findViewById(R.id." + op + nme + "Spinner);\n\r"; 
    if (isEntity())
    { res = res + "    " + op + nme + "ListItems = " + androidValueList() + ";\n"; } 
    res = res +  
                 "    ArrayAdapter<String> " + op + nme + "Adapter = new ArrayAdapter<String>(" + context + ", android.R.layout.simple_spinner_item," + op + nme + "ListItems);\n\r" +  
                 "    " + op + nme + "Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);\n\r" + 
                 "    " + op + nme + "Spinner.setAdapter(" + op + nme + "Adapter);\n\r" + 
                 "    " + op + nme + "Spinner.setOnItemSelectedListener(this);\n\r"; 
     return res; 
  } 

public String swiftUIEntryField(String ent, String op, Vector decs, Vector actions)
{ String nme = getName();
  String label = Named.capitalise(nme);
  String hint = ent + " " + nme; 
  String bean = "bean"; 
  String tname = type.getName(); 
  
    // temporary data of the class attributes or use case parameters

  String res1 = "      HStack {\n" +
                "        Text(\"" + label + ":\").bold()\n" +
                "        Divider()\n" +
                "        TextField(\"" + label + "\", text: $bean." + nme + ")\n" +
                "      }.frame(width: 200, height: 30).border(Color.gray)\n" + 
                "    \n";

  String res2 = ""; // declaration
  String res3 = ""; // actions
  // if (isIdentity())
  // { res2 = "  <TextView\n\r" +
  //   "    android:id=\"@id/" + attfield + "\"\n\r" +
  //   "    android:layout_span=\"3\" />\n\r";
  // } 
  // else 
  // android:inputType="textPassword"

  if (isBoolean())
  { res1 = "      Toggle(\"" + label + "\", isOn: $bean." + nme + ")\n"; } 
  else if (isEnumeration())
  { Vector vals = type.getValues();
    res2 = "  var " + nme + "Values = ["; 
    for (int i = 0; i < vals.size(); i++)
    { String val = (String) vals.get(i); 
      res2 = res2 + "\"" + val + "\""; 
      if (i < vals.size() - 1) 
      { res2 = res2 + ", "; }
    } 
    res2 = res2 + "]\n" + 
       "  @State var selected" + tname + " : Int = 0\n\n"; 
    // decs.add(res2); 
    res1 = 
       "      Picker(\"" + tname + "\", selection: $bean." + nme + ") {\n";  
	for (int i = 0; i < vals.size(); i++)
	{ String val = (String) vals.get(i); 
	  res1 = res1 + "        Text(\"" + val + "\").tag(" + tname + "." + val + ")\n"; 
	} 
	res1 = res1 + "      }.frame(height: 100)\n"; 
	// res3 = "    $bean." + nme + " = " + tname + "(rawValue: selected" + tname + ")!"; 
	// actions.add(res3);               
  } // on button press, $bean.nme = tname(rawValue: selectedtname)!
  else if (isEntity())
  { // Show the list of instances
    String rname = type.getName(); 
    Entity ref = type.getEntity(); 
    Attribute rkey = ref.getPrincipalPrimaryKey(); // must be one
    if (rkey != null) 
    { String pk = rkey.getName(); 
      res1 = 
	    "      Picker(\"" + tname + "\", selection: $bean." + nme + ")\n" + 
	    "      { ForEach(model.current" + rname + "s) { Text($0." + pk + ").tag($0." + pk + ") } }.frame(height: 100)\n";
    }   
  } // In the value object, objects are stored as their string primary key values. 
  else if (isInteger())
  { res1 = "      HStack {\n" +
           "        Text(\"" + label + ":\").bold()\n" +
           "        Divider()\n" +
           "        TextField(\"" + label + "\", value: $bean." + nme + ", formatter: NumberFormatter()).keyboardType(.numberPad)\n" +
           "      }.frame(height: 30).border(Color.gray)\n" + 
           "    \n";
  } 
  else if (isDouble())
  { res1 = "      HStack {\n" +
           "        Text(\"" + label + ":\").bold()\n" +
           "        Divider()\n" +
           "        TextField(\"" + label + "\", value: $bean." + nme + ", formatter: NumberFormatter()).keyboardType(.decimalPad)\n" +
           "      }.frame(height: 30).border(Color.gray)\n" + 
           "    \n";
  } 
  else if (isPassword())
  { res1 = "      HStack {\n" +
           "        Text(\"" + label + ":\").bold()\n" +
           "        Divider()\n" +
           "        SecureField(\"" + label + "\", text: $bean." + nme + ")\n" +
           "      }.frame(height: 30).border(Color.gray)\n" + 
           "    \n";
  } 
  else if (isCollection())
  { res1 = "      HStack {\n" +
           "        Text(\"" + label + ":\").bold()\n" +
           "        Divider()\n" +
           "        TextEditor(text: $bean." + nme + ")\n" +
           "      }.frame(height: 100).border(Color.gray)\n" + 
           "    \n";
  }
  
  return res1;
}  // email, password kinds also 

public String swiftUIFormInitialiser()
{ if (type.isEntity()) { } 
  else 
  { return ""; } 

  String attname = getName();
  String bean = "model"; 
  String tname = type.getName(); 
  String res1 = ""; 
  
  Entity ref = type.getEntity(); 
  String ename = ref.getName(); 

  Attribute rkey = ref.getPrincipalPrimaryKey(); // must be one
  if (rkey != null) 
  { String pk = rkey.getName(); 
    res1 = ".onAppear(perform:\n" + 
           "             { let list = model.list" + ename + "()\n" + 
           "               if list.count > 0\n" + 
           "               { bean." + attname + " = list[0]." + pk + " }\n" + 
           "             })\n\n";
  }  
  return res1;
}  // email, password kinds also 

  public String uiKitDeclaration()
  { String nme = getName(); 

    if (isSmallEnumeration())
    { Vector vals = type.getValues(); 
      String defaultValue = (String) vals.get(0); 
      return "  @IBOutlet weak var " + nme + "Control : UISegmentedControl!\n" + 
             "  var " + nme + "Input : String = \"" + defaultValue + "\""; 
    } 
    else if (isCollection())
	{ return "  @IBOutlet weak var " + nme + "Input : UITextView!"; } 
	else
    { return "  @IBOutlet weak var " + nme + "Input : UITextField!"; } 
  } 

  public String uiKitDeclaration(String ucname)
  { String nme = ucname + getName(); 

    if (isSmallEnumeration())
    { Vector vals = type.getValues(); 
      String defaultValue = (String) vals.get(0); 
      return "  @IBOutlet weak var " + nme + "Control : UISegmentedControl!\n" + 
             "  var " + nme + "Input : String = \"" + defaultValue + "\""; 
    } 
    else if (isCollection())
	{ return "  @IBOutlet weak var " + nme + "Input : UITextView!"; } 
    else 
    { return "  @IBOutlet weak var " + nme + "Input : UITextField!"; } 
  } 

  public String uiKitOp()
  { String nme = getName(); 

    if (isSmallEnumeration())
    { Vector vals = type.getValues(); 
      String switchcases = "";
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        switchcases = switchcases + 
                      "      case " + i + ":\n" + 
                      "        " + nme + "Input = \"" + val + "\"\n"; 
      }  
      return "  @IBAction func " + nme + "Control(_ sender : Any)\n" + 
             "  { switch " + nme + "Control.selectedSegmentIndex {\n" + 
             switchcases + 
             "      default: \n" + 
             "        return\n" + 
             "    }\n" + 
             "  }\n\n"; 
    } 
    else 
    { return ""; } 
  } 

  public String uiKitOp(String ucname)
  { String nme = ucname + getName(); 

    if (isSmallEnumeration())
    { Vector vals = type.getValues(); 
      String switchcases = "";
      for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        switchcases = switchcases + 
                      "      case " + i + ":\n" + 
                      "        " + nme + "Input = \"" + val + "\"\n"; 
      }  
      return "  @IBAction func " + nme + "Control(_ sender : Any)\n" + 
             "  { switch " + nme + "Control.selectedSegmentIndex {\n" + 
             switchcases + 
             "      default: \n" + 
             "        return\n" + 
             "    }\n" + 
             "  }\n\n"; 
    } 
    else 
    { return ""; } 
  } 


}
