import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ObjectExpression extends Expression
{ private String ename; 
  private Vector values = new Vector(); 

  public ObjectExpression(String e, Vector vals)
  { ename = e; 
    values = vals; 
  } 

  public boolean isPrimitive() { return false; } 
  
  public boolean isMultiple() { return false; } 

  // public Expression computeNegation4succ(final Vector actuators)
  // { // not valid to negate it
    // return new BasicExpression("false"); 
  // } 

  // public Vector computeNegation4ante(final Vector actuators)
  // { return new Vector(); } 

  public String queryForm(java.util.Map env,boolean local)
  { String res = "lookup" + ename + "(";
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res = res + e.queryForm(env,local);
      if (i < values.size() - 1)
      { res = res + ", "; }
    }
    return res + ")";
  }

  public Vector allEntitiesUsedIn()
  { Vector res = new Vector();
    res.add(entity);
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      res = VectorUtil.union(res,val.allEntitiesUsedIn());
    }
    return res;
  }

  public Vector getVariableUses()
  { Vector res = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      res.addAll(val.getVariableUses());
    }
    return res;
  }

  public Vector getUses(String feature)
  { Vector res = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      res.addAll(val.getUses(feature));
    }
    return res;
  }

  public Vector allFeaturesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res.addAll(e.allFeaturesUsedIn());
    }
    return res;
  }


  public boolean relevantOccurrence(String op, Entity ent, String val,
                                    String feat)
  { return false; } // ???


  public Vector allBinarySubexpressions() { return new Vector(); }

  public String toString()
  { String res = "(" + ename + ")"; 
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res = res + e;
      if (i < values.size() - 1)
      { res = res + ","; }
    }
    return res;
  }

  public String updateForm(java.util.Map env,boolean local)
  { return "{} // not defined "; } 

  public String updateForm(java.util.Map env, String val2)
  { String ex = ename.toLowerCase() + "xx";
    String newexp = ename + " " + ex + " = new " +
                    ename + "(";
    for (int i = 0; i < values.size(); i++)
    { String val = (String) values.get(i);
      newexp = newexp + val;
      if (i < values.size() - 1)
      { newexp = newexp + ","; }
    }
    newexp = newexp + ")";
    return "{ " + newexp + "; if (" + val2 +
           ".contains(" + ex + ")) { } else " +
           val2 + ".add(" + ex + "); }";
  }

  public int typeCheck(final Vector sms)
  { return SENSOR; }

  public boolean typeCheck(final Vector types, final Vector entities,
                           final Vector env)
  { boolean res = true;
    entity = (Entity) ModelElement.lookupByName(ename,entities); 
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res = res && e.typeCheck(types,entities,env);
    }
    return res && entity != null;
  }

  public BExpression binvariantForm(java.util.Map env)
  { return new BBasicExpression("/* not valid */"); } 

  public BExpression binvariantForm(java.util.Map env,
                                    BExpression v)
  { // #ex.(ex: es & f(ex) = vals & ex : v)
    Vector vals = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      BExpression e = val.binvariantForm(env);
      vals.add(e);
    }
    BExpression pred = entity.getBEqualityPred(vals);
    String es = ename.toLowerCase() + "s";
    String ex = ename.toLowerCase() + "x";
    BExpression ran = new BBasicExpression(es);
    BExpression exbe = new BBasicExpression(ex);
    pred =
      new BBinaryExpression("&",
        new BBinaryExpression(":",exbe,ran),pred);
    return new BQuantifierExpression("exists",ex,
             new BBinaryExpression("&",pred,
               new BBinaryExpression(":",exbe,v)));
  }

  public BStatement bupdateForm(java.util.Map env)
  { return new BBasicStatement("skip"); /* not valid */ }

  public BExpression bqueryForm(java.util.Map env)
  { return new BBasicExpression("/* not valid */"); }

  public boolean isOrExpression() { return false; }

  public Expression createActionForm(final Vector v)
  { return this; }

  public String toJava()
  { String res = "new " + ename + "(";
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      String val = e.toJava();
      res = res + val;
      if (i < values.size() - 1)
      { res = res + ","; }
    }
    return res + ")";
  }

  public String toB() { return ""; }

  public Expression toSmv(Vector cnames) { return null; }

  public String toImp(final Vector comps)
  { return ""; }

  public String toJavaImp(final Vector comps)
  { return toJava(); }

  public Expression buildJavaForm(final Vector comps)
  { return new BasicExpression(toJava()); }

  public Expression substitute(final Expression var,
                               final Expression val)
  { Vector newvals = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      Expression newval = e.substitute(var,val);
      newvals.add(newval);
    }
    return new ObjectExpression(ename,newvals);
  }

  public Expression substituteEq(final String var,
                                 final Expression val)
  { Vector newvals = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      Expression newval = e.substituteEq(var,val);
      newvals.add(newval);
    }
    return new ObjectExpression(ename,newvals);
  }

  public boolean hasVariable(final String s)
  { for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      if (e.hasVariable(s))
      { return true; }
    }
    return false;
  }

  public Vector variablesUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res.addAll(e.variablesUsedIn(s));
    }
    return res;
  }

  public Vector componentsUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression e = (Expression) values.get(i);
      res.addAll(e.componentsUsedIn(s));
    }
    return res;
  }

  Maplet findSubexp(final String var)
  { return null; } // new Maplet(null,this) ???

  public Expression simplify(final Vector vars)
  { Vector newvals = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      Expression newval = val.simplify(vars);
      newvals.add(newval);
    }
    return new ObjectExpression(ename,newvals);
  }

  public Expression filter(final Vector vars)
  { return null; } // ???

  public Object clone()
  { Vector newvals = new Vector();
    for (int i = 0; i < values.size(); i++)
    { Expression val = (Expression) values.get(i);
      Expression newval = (Expression) val.clone();
      newvals.add(newval);
    }
    return new ObjectExpression(ename,newvals);
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

  public DataDependency rhsDataDependency()
  { return new DataDependency(); }  // ???

  public DataDependency getDataItems()
  { return new DataDependency(); }  // ???
} 
