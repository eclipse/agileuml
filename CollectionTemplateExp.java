import java.util.Vector;

/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public class CollectionTemplateExp extends TemplateExp
{ Type referredCollectionType;
  // Vector member = new Vector(); // Expression or TemplateExp
  Attribute member = null; 
  Attribute rest = null;

  public CollectionTemplateExp(Attribute bind, Expression wh, Type typ)
  { super(bind,wh);
    referredCollectionType = typ; 
  }

  /* public void addMember(Expression e)
  { members.add(e); }

  public void addMember(TemplateExp e)
  { members.add(e); } */ 

  public void setMember(Attribute m) 
  { member = m; } 

  public void setRest(Attribute r)
  { rest = r; }

  public void setConstraint(Expression w)
  { where = w; } 

  public Object clone()
  { Expression wclone = null;
    if (where != null)
    { wclone = (Expression) where.clone(); }

    CollectionTemplateExp ref = new CollectionTemplateExp(bindsTo,wclone,referredCollectionType);
    /* for (int j = 0; j < members.size(); j++)
    { Object m = members.get(j);
      if (m instanceof Expression)
      { ref.addMember((Expression) m.clone()); } 
      else
      { ref.addMember((TemplateExp) m.clone()); }
    } */ 

    if (member != null) 
    { ref.setMember((Attribute) member.clone()); } 

    if (rest != null)
    { ref.setRest((Attribute) rest.clone()); }

    return ref;
  }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { /* for (int j = 0; j < members.size(); j++)
    { Object m = members.get(j);
      if (m instanceof Expression)
      { ((Expression) m).typeCheck(types,entities,contexts,env); }
      else 
      { ((TemplateExp) m).typeCheck(types,entities,contexts,env); }
    } */ 

    Vector newenv = new Vector(); 
    newenv.addAll(env); 
    if (member != null) 
    { newenv.add(member); } 
    if (rest != null)  
    { newenv.add(rest); } 
 
    if (where != null)
    { where.typeCheck(types,entities,contexts,newenv); }

    return true;
  }

  public Expression toSourceExpression(Vector bound, Expression contextObj)
  { BasicExpression obj = new BasicExpression(bindsTo);
    // obj.setUmlKind(Expression.ROLE); 
    // obj.setEntity(traceent); 

    Vector elems = new Vector();
    Expression res = new BasicExpression(true); 
     // new BinaryExpression(bindsTo); 

  /*
    if ("Set".equals(referredCollectionType.getName()))
    { // m : obj for each m

      for (int j = 0; j < members.size(); j++)
      { Object m = members.get(j);
        if ("_".equals(m + "")) { continue; }
        if (m instanceof Expression)
        { BinaryExpression inset = new BinaryExpression(":", (Expression) m, obj);
          res = Expression.simplify("&",res,inset,null);
          elems.add(m);
        } // add to bounds if a variable
        else 
        { TemplateExp tm = (TemplateExp) m;
          Attribute x = tm.bindsTo;
          Expression xexp = new BasicExpression(x);
           bound.add(x.getName());
          BinaryExpression inset = new BinaryExpression(":", xexp, obj);
          res = Expression.simplify("&",res,inset,null);
          elems.add(xexp); 

          if (tm.isEmpty()) {}
          else
          { Expression andcond = tm.toSourceExpression(bound,xexp);
            res = new BinaryExpression("&", res, andcond);
          } 
        }
      }
    }  */ 


    if ("Set".equals(referredCollectionType.getName()))
    { if (member != null)
      { if ("_".equals(member.getName())) {}
        else
        { bound.add(member.getName()); 
          BasicExpression rexp = new BasicExpression(member);
        // rexp.setUmlKind(Expression.ROLE);
        // rexp.setEntity(traceent); 
 
          elems.add(rexp); 
          BinaryExpression be = new BinaryExpression(":", rexp, obj);
          res = Expression.simplify("&",res,be,null);
        }
      }

      if (rest != null)
      { if ("_".equals(rest.getName())) {}
        else
        { bound.add(rest.getName()); 
          BasicExpression rexp = new BasicExpression(rest);
        // rexp.setUmlKind(Expression.ROLE); 
        // rexp.setEntity(traceent); 

          SetExpression known = new SetExpression(elems);
          BinaryExpression be = new BinaryExpression("-", obj, known);
          BinaryExpression eqrest = new BinaryExpression("=", rexp, be);            
          res = Expression.simplify("&",res,eqrest,null);
        }
      }
    }
    else if ("Sequence".equals(referredCollectionType.getName()))
    { if (member != null)
      { if ("_".equals(member.getName())) {}
        else
        { bound.add(member.getName()); 
          BasicExpression rexp = new BasicExpression(member);
        // rexp.setUmlKind(Expression.ROLE);
        // rexp.setEntity(traceent); 
 
          elems.add(rexp); 
          UnaryExpression objsize = new UnaryExpression("->size",obj); 
          BinaryExpression nonempty = 
              new BinaryExpression(">", objsize, new BasicExpression(0));

          BinaryExpression be = new BinaryExpression("->at",obj,new BasicExpression(1));
          BinaryExpression setbe = new BinaryExpression("=", rexp, be); 
          res = Expression.simplify("&",res,nonempty,null);
          res = Expression.simplify("&",res,setbe,null);
        }
      }
      if (rest != null)
      { if ("_".equals(rest.getName())) {}
        else
        { bound.add(rest.getName()); 
          BasicExpression rexp = new BasicExpression(rest);
        // rexp.setUmlKind(Expression.ROLE); 
        // rexp.setEntity(traceent); 

          UnaryExpression be = new UnaryExpression("->tail", obj);
          BinaryExpression eqrest = new BinaryExpression("=", rexp, be);            
          res = Expression.simplify("&",res,eqrest,null);
        }
      }
    } 

    if (where != null)
    { res = Expression.simplify("&", res, where, null); }

    return res; 
  }

  public Expression toGuardCondition(Vector bound, 
                            Expression contextObj, Expression post, Entity tr)
  { BasicExpression obj = new BasicExpression(bindsTo);
    obj.setUmlKind(Expression.ROLE); 
    obj.setEntity(tr); 

    Vector elems = new Vector();
    Expression res = new BinaryExpression(bindsTo); 

    if ("Set".equals(referredCollectionType.getName()))
    { if (member != null)
      { if ("_".equals(member.getName())) {}
        else
        { bound.add(member.getName()); 
          BasicExpression rexp = new BasicExpression(member);
          rexp.setUmlKind(Expression.ROLE); 
          rexp.setEntity(tr); 

          elems.add(rexp); 
          BinaryExpression be = new BinaryExpression(":", rexp, obj);
          res = Expression.simplify("&",res,be,null);
        }
      }

      if (rest != null)
      { if ("_".equals(rest.getName())) {}
        else
        { bound.add(rest.getName()); 
          BasicExpression rexp = new BasicExpression(rest);
          rexp.setUmlKind(Expression.ROLE); 
          rexp.setEntity(tr); 
          SetExpression known = new SetExpression(elems);
          BinaryExpression be = new BinaryExpression("-", obj, known);
          BinaryExpression eqrest = new BinaryExpression("=", rexp, be);            
          res = Expression.simplify("&",res,eqrest,null);
        }
      }
    } 
    else if ("Sequence".equals(referredCollectionType.getName()))
    { if (member != null)
      { if ("_".equals(member.getName())) {}
        else
        { bound.add(member.getName()); 
          BasicExpression rexp = new BasicExpression(member);
        // rexp.setUmlKind(Expression.ROLE);
        // rexp.setEntity(traceent); 
 
          elems.add(rexp); 
          UnaryExpression objsize = new UnaryExpression("->size",obj); 
          BinaryExpression nonempty = 
              new BinaryExpression(">", objsize, new BasicExpression(0));

          BinaryExpression be = new BinaryExpression("->at",obj,new BasicExpression(1));
          BinaryExpression setbe = new BinaryExpression("=", rexp, be); 
          res = Expression.simplify("&",res,nonempty,null);
          res = Expression.simplify("&",res,setbe,null);
        }
      }
      if (rest != null)
      { if ("_".equals(rest.getName())) {}
        else
        { bound.add(rest.getName()); 
          BasicExpression rexp = new BasicExpression(rest);
        // rexp.setUmlKind(Expression.ROLE); 
        // rexp.setEntity(traceent); 

          UnaryExpression be = new UnaryExpression("->tail", obj);
          BinaryExpression eqrest = new BinaryExpression("=", rexp, be);            
          res = Expression.simplify("&",res,eqrest,null);
        }
      }
    } 


    if (where != null)
    { res = Expression.simplify("&", res, where, null); }

    return res; 
  }

  public Expression toTargetExpression(Vector bound, Expression contextObj, Expression setting)
  { return new BasicExpression(true); } 

  public Expression toUndoExpression(Vector bound, Expression contextObj, Expression setting)
  { return new BasicExpression(true); } 

  public Expression toTargetExpressionOp(Vector bound, Expression contextObj, Expression setting)
  { return new BasicExpression(true); } 

  public Vector getObjectTemplateAttributes(Vector vars)
  { Vector res = new Vector(); 
    if (member != null && vars.contains(member.getName()))
    { res.add(member.getName()); } 
    if (rest != null && vars.contains(rest.getName()))
    { res.add(rest.getName()); } 
    return res; 
  } 
 

  public Vector objectTemplateAttributes()
  { Vector res = new Vector(); 
    if (member != null && !(member.getName().equals("_")))
    { res.add(member); } 
    if (rest != null && !(rest.getName().equals("_")))
    { res.add(rest); } 
    return res; 
  } 
  

  public int complexity()
  { int res = 0;

    if (member != null && !member.getName().equals("_"))
    { res++; } 

    if (rest != null && !rest.getName().equals("_"))
    { res++; } 

    if (where != null) 
    { res = res + where.syntacticComplexity(); } 
    return res; 
  } 

  public int epl()
  { int res = 0;

    if (member != null && !member.getName().equals("_"))
    { res++; } 

    if (rest != null && !rest.getName().equals("_"))
    { res++; } 

  /*  for (int j = 0; j < members.size(); j++)
    { Object m = members.get(j);
      if (m instanceof Expression)
      { Expression me = (Expression) m; 
      }
      else 
      { TemplateExp te = (TemplateExp) m; 
        res += te.epl(); 
      }
    } */ 

    return res;
  }

  public Vector operationsUsedIn()
  { if (where != null) 
    { return where.allOperationsUsedIn(); } 
    return new Vector(); 
  } 

  public String toString()
  { String res = bindsTo + " : " + referredCollectionType + " { ";
    
    res = res + member + " ++ " + rest + " }"; 
    if (where != null) 
    { res = res + " { " + where + " }"; } 
    return res;
  } // and { where }

}

