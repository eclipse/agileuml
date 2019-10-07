import java.util.Map;
import java.util.HashMap;
import java.util.Vector; 

/* Package: OCL */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class FilterExpression extends Expression
{ Expression objectRef = null;
  Expression filter = null;
  static int count = 0;
  static Map functionDefinitions = new HashMap();
    // String --> FilterExpression
  int myIndex;

  FilterExpression(Expression ref, Expression test)
  { objectRef = ref;
    filter = test;
    count++;
    myIndex = count;
    functionDefinitions.put("" + myIndex, this);
  }

  public boolean isPrimitive() { return false; }

  public boolean isOrdered()
  { if (objectRef == null)
    { return false; }
    return objectRef.isOrdered(); 
  }

  public String toString()
  { String res = objectRef + " | ( " + filter + " )"; 
    if (needsBracket)
    { res = "( " + res + " )"; }
    return res;
  }

  public int minModality() { return modality; } 

  public int maxModality() { return modality; } 

  public String createFilterMethod()
  { // method to compute the filter
    String ename = entity.getName();
    String elc = ename.toLowerCase();
    String elx = elc + "s";
    String var = elc + "xx";
    java.util.Map env = new java.util.HashMap();
    env.put(ename,var);
    String test = filter.queryForm(env,false);
    String res = "  public static List " + elc +
                 "_filter_" + myIndex + "(List " +
                 elx + ")\n";
    res = res + "  { List result = new ArrayList();\n";
    res = res + "    for (int i = 0; i < " + elx + 
          ".size(); i++)\n";
    res = res + "    { " + ename + " " + var + " = (" +
          ename + ") " + elx + ".get(i);\n"; 
    res = res + "      if (" + test + ")\n";
    res = res + "      { result.add(" + var + "); }\n";
    res = res + "    }\n";
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String toB()
  { Entity e = objectRef.getEntity();
    String xx = e.getName().toLowerCase() + "xxx";
    String res = "{ " + xx + " | " + xx + " : " + 
                 objectRef.toB() + " & ";
    java.util.Map env = new java.util.HashMap();
    env.put(objectRef.getEntity() + "",xx);
    BExpression pred = filter.bqueryForm(env); 
    return res + pred + " }";
  }

  public String toJava()
  { String pre = objectRef.toJava();
    Entity e = objectRef.getEntity();
    String ename = e.getName();
    String fname = ename.toLowerCase() + "_filter_" +
                   myIndex;
    return ename + "." + fname + "(" + pre + ")";
  }

  public Expression toSmv(Vector cs) 
  { return this; } 

  public Expression buildJavaForm(final Vector comps)
  { return new BasicExpression(toJava()); }

  public String toJavaImp(final Vector comps)
  { return toJava(); }

  public String toImp(final Vector comps)
  { return toJava(); }

  public Expression createActionForm(final Vector comps)
  { return this; }

  public boolean isOrExpression() { return false; }

  public boolean typeCheck(final Vector types,
                   final Vector entities,
                   final Vector contexts, final Vector env)
  { boolean res =
      objectRef.typeCheck(types,entities,contexts,env);
    modality = ModelElement.NONE;
    type = objectRef.getType();
    elementType = objectRef.getElementType();
    entity = objectRef.getEntity();
    umlkind = VALUE; // ??
    multiplicity = ModelElement.MANY;
    // Add entity as first element in contexts copy
    boolean res2 = filter.typeCheck(types,entities,contexts,env);
    // filter must have boolean type
    System.out.println("Types of " + this + " are " +
                       type + " " + filter.getType());
    return res && res2;
  }

  public int typeCheck(final Vector sms)
  { return ERROR; } 

  public String queryForm(java.util.Map env, boolean local)
  { String pre = objectRef.queryForm(env,local);
    Entity e = objectRef.getEntity();
    String ename = e.getName();
    String fname = ename.toLowerCase() + "_filter_" +
                   myIndex;
    return ename + "." + fname + "(" + pre + ")";
  }

  public String updateForm(java.util.Map env, boolean local)
  { return "{} // no sensible update form"; }

  public String updateForm(java.util.Map env,
                           String op, String v2,
                           boolean local)
  { return "{} // no sensible update form"; }

  public BExpression bqueryForm(java.util.Map env)
  { Entity e = objectRef.getEntity();
    String xx = e.getName().toLowerCase() + "xxx";
    BExpression objs = objectRef.bqueryForm(env);
    env.put(objectRef.getEntity() + "",xx);
    BExpression pred = filter.bqueryForm(env); 
    return new BSetComprehension(xx,objs,pred);
  }

  public BStatement bupdateForm(java.util.Map env,
                                String op, 
                                BExpression val2,
                                boolean local)
  { return new BBasicStatement("skip"); } 

  public BStatement bupdateForm(java.util.Map env,
                                boolean local)
  { return new BBasicStatement("skip"); } 

  public BExpression binvariantForm(java.util.Map env, boolean local)
  { Entity e = objectRef.getEntity();
    String xx = e.getName().toLowerCase() + "xxx";
    BExpression objs = objectRef.binvariantForm(env,local);
    env.put(objectRef.getEntity() + "",xx);
    BExpression pred = filter.binvariantForm(env,local);
    Vector vars = new Vector();
    vars.add(xx); 
    return new BSetComprehension(xx,objs,pred);
  }

  public boolean isMultiple()
  { return true; }

  public Expression substitute(Expression oldE,
                               Expression newE)
  { if (oldE == this)
    { return newE; }
    Expression newObj = objectRef.substitute(oldE,newE);
    Expression newF = filter.substitute(oldE,newE);
    return new FilterExpression(newObj,newF);
  }

  public Expression substituteEq(final String oldV,
                      final Expression newV)
  { if (toString().equals(oldV))
    { return (Expression) newV.clone(); }
    Expression newObj =
      objectRef.substituteEq(oldV,newV);
    Expression newF =
      filter.substituteEq(oldV,newV);
    return new FilterExpression(newObj,newF);
  } // but if not changed, just return this

  public Expression simplify()
  { return this; }

  public Expression simplify(final Vector vars)
  { return this; } 

  public boolean hasVariable(final String v)
  { return objectRef.hasVariable(v) ||
           filter.hasVariable(v);
  }

  public Vector getVariableUses()
  { Vector res = objectRef.getVariableUses();
    return VectorUtil.union(res,
                        filter.getVariableUses());
  }

  public Vector getUses(String f)
  { Vector res = objectRef.getUses(f);
    res.addAll(filter.getUses(f)); // no duplicates
    return res;
  }

  public Vector allFeaturesUsedIn()
  { Vector res = objectRef.allFeaturesUsedIn();
    res.addAll(filter.allFeaturesUsedIn());
    return res;
  }

  public Vector allBinarySubexpressions()
  { return new Vector(); }  // ??

  public Maplet findSubexp(final String var)
  { return new Maplet(null,this); } // ??

  public Vector componentsUsedIn(final Vector sms)
  { return new Vector(); }  // ??

  public Vector variablesUsedIn(final Vector vars)
  { return new Vector(); }  // ??

  public boolean selfConsistent()
  { return true; }

  public Object clone()
  { Expression nor = (Expression) objectRef.clone();
    Expression fltr = (Expression) filter.clone();
    FilterExpression res = new FilterExpression(nor,fltr);
    res.myIndex = myIndex;
    res.modality = modality;
    res.type = type;
    res.umlkind = umlkind;
    res.elementType = elementType;
    res.entity = entity;
    return res;
  }

  public Vector splitAnd(final Vector cs)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Vector splitOr(final Vector cs)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Expression removeExpression(final Expression e)
  { if (equals(e)) { return null; }
    return this;
  }
  
  public boolean implies(final Expression e)
  { return false; }  // should never be called 

  public boolean consistentWith(final Expression e)
  { return false; }

  public Expression computeNegation4succ(final Vector as)
  { return null; }


  public Vector computeNegation4ante(final Vector as)
  { return new Vector(); }

  public boolean subformulaOf(final Expression e)
  { return equals(e); } 

  public boolean selfConsistent(final Vector v) 
  { return true; } 

  public Expression expandMultiples(final Vector sms)
  { return this; } 

  public Expression filter(final Vector vars)
  { return null; } // ??

  public DataDependency rhsDataDependency()
  { DataDependency res = new DataDependency(); 
    res.union(objectRef.getDataItems()); // all features are sources of data here
    res.union(filter.getDataItems()); 
    return res; 
  } 

  public DataDependency getDataItems()
  { return rhsDataDependency(); } 

  public boolean relevantOccurrence(String op, Entity ent, String val, String f)
  { return objectRef.relevantOccurrence(op,ent,val,f) ||
           filter.relevantOccurrence(op,ent,val,f); 
  } 

  public Vector allPreTerms()
  { return new Vector(); } // ??

  public Vector innermostEntities()
  { return objectRef.innermostEntities(); } 

  public Vector allEntitiesUsedIn()
  { return objectRef.allEntitiesUsedIn(); } 

  public int syntacticComplexity()
  { return objectRef.syntacticComplexity(); } // plus the filter? 

  public Expression featureSetting(String v, String e, Vector rem)
  { return null; } 

  public boolean conflictsWith(Expression e)
  { return false; } 

  public String toZ3()
  { return ""; }  // No equivalent

  public String toSQL()
  { return ""; }  // No equivalent

  public BExpression bqueryForm()
  { return binvariantForm(new java.util.HashMap(), false); } 

  public Expression invert() { return this; } 

  public Expression dereference(BasicExpression be) 
  { return this; } 

  public Expression addReference(BasicExpression e, Type t) 
  { return this; } 
}


