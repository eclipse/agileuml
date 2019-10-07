import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public class ObjectTemplateExp extends TemplateExp
{ Entity referredClass;
  Vector part = new Vector(); // PropertyTemplateItem

  public ObjectTemplateExp(Attribute att, Expression ex, Entity e)
  { super(att,ex); 
    referredClass = e; 
  }

  public ObjectTemplateExp(Attribute att, Entity e)
  { super(att,null); 
    referredClass = e; 
  }

  public Object clone()
  { Expression wclone = null; 
    if (where != null) 
    { wclone = (Expression) where.clone(); } 
    ObjectTemplateExp res = new ObjectTemplateExp(bindsTo,wclone,referredClass);
    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      res.addpart((PropertyTemplateItem) pti.clone()); 
    } 
    return res; 
  } 

  public ObjectTemplateExp overrideBy(ObjectTemplateExp t)
  { // assume bindsTo.name.equals(t.bindsTo.name)
    ObjectTemplateExp res = null; 
    Entity resent = null; 

    Attribute tbinds = t.bindsTo; 
    Type mytype = bindsTo.getType(); 
    Type ttype = tbinds.getType(); 
    if (mytype != null && ttype != null && mytype.isEntity() && ttype.isEntity())
    { Entity myent = mytype.getEntity(); 
      Entity tent = ttype.getEntity(); 
      if (Entity.isAncestor(myent,tent))
      { resent = tent; } 
      else if (myent == tent)
      { resent = myent; } 
      else 
      { System.err.println("!! ERROR: cannot override " + this + " by " + t + " Incorrect types"); 
        return null; 
      } 
    } 
    else 
    { System.err.println("!! ERROR: cannot override " + this + " by " + t + " Not entity types");
      return null; 
    } 

    Vector resparts = new Vector(); 

    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      PropertyTemplateItem titem = t.getItem(pti.referredProperty.getName()); 
      if (titem == null) 
      { resparts.add(pti); } 
      else 
      { PropertyTemplateItem overriddenpti = pti.overrideBy(titem); 
        if (overriddenpti != null) 
        { resparts.add(overriddenpti); }  
      }       
    } 

    for (int q = 0; q < t.part.size(); q++) 
    { PropertyTemplateItem tpart = (PropertyTemplateItem) t.part.get(q); 
      PropertyTemplateItem mypart = getItem(tpart.referredProperty.getName()); 
      if (mypart == null) 
      { resparts.add(tpart); } 
    } 
  
    Expression reswhere = null; 
    if (where != null) 
    { if (t.where != null) 
      { reswhere = Expression.simplifyAnd(where,t.where); } 
      else 
      { reswhere = (Expression) where.clone(); } 
    } 
    else 
    { reswhere = t.where; } 
    
    res = new ObjectTemplateExp(tbinds,reswhere,resent); 
    res.part = resparts; 
    
    return res; 
  }

  public void setSource()
  { bindsTo.addStereotype("source"); 
    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      pti.setSource(); 
    } 
  }     

  public void addpart(PropertyTemplateItem p)
  { part.add(p); }

  public void setpart(Vector parts)
  { part = parts; 
    if (parts == null) 
    { part = new Vector(); }        
  } 

  public void addPTI(Attribute att, Object v)
  { for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      if (pti.referredProperty.getName().equals(att.getName()))
      { return; } 
    }       
    PropertyTemplateItem newpti = new PropertyTemplateItem(att);
    if (v instanceof ObjectTemplateExp)
    { newpti.setTemplate((ObjectTemplateExp) v); } 
    else if (v instanceof Expression) 
    { newpti.setValue((Expression) v); }  
    part.add(newpti); 
  } 

  public Object getPTI(Attribute att) 
  { for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      if (pti.referredProperty.getName().equals(att.getName()))
      { if (pti.template != null) 
        { return pti.template; }
        else 
        { return pti.value; } 
      }  
    }   
    return null; 
  }     
      
  public PropertyTemplateItem getItem(String nme) 
  { for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem pti = (PropertyTemplateItem) part.get(i); 
      if (pti.referredProperty.getName().equals(nme))
      { return pti; }  
    }   
    return null; 
  }     

  public Entity getEntity()
  { return referredClass; }

  public void setConstraint(Expression w)
  { where = w; } 

  public void addWhere(Expression w)
  { if (where == null) 
    { where = w; }
    else 
    { where = Expression.simplify("&", where, w, new Vector()); } 
  }  

  public Vector getObjectTemplateAttributes(Vector vars)
  { Vector res = new Vector(); 
    
    String dname = bindsTo.getName(); 
    for (int i = 0; i < vars.size(); i++) 
    { String v = vars.get(i) + ""; 
      if (dname.equals(v))
      { res.add(bindsTo); } 
    } 
    

    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res.addAll(p.getObjectTemplateAttributes(vars)); 
    } 
    return res; 
  } 

  public Vector objectTemplateAttributes()
  { Vector res = new Vector(); 
    
    res.add(bindsTo);  
    
    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res.addAll(p.objectTemplateAttributes()); 
    } 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { Vector context = new Vector(); 
    if (referredClass != null) 
    { context.add(referredClass); } 
    context.addAll(contexts); 
    env.add(bindsTo);  

    for (int i = 0; i < part.size(); i++) 
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.typeCheck(types,entities,context,env); 
    } 

    if (where != null) 
    { where.typeCheck(types,entities,context,env); } 

    return true; 
  } 

  public Expression toGuardCondition(Vector bound, Expression contextObj, Expression post)
  { Expression res = new BasicExpression(true);
 
    String bind = bindsTo.getName(); 
        
    boolean alreadybound = bound.contains(bind); 
    System.out.println("BOUND = " + bound + " BINDS TO= " + bindsTo); 
    
    res = Expression.simplify("&",where,post,null);

    for (int i = part.size()-1; 0 <= i; i--)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      res = p.toGuardCondition(bound, contextObj, res);
    }

    Expression btexp = new BasicExpression(bindsTo);
    // Expression scope = new BasicExpression(referredClass);
    // Expression decl = new BinaryExpression(":", btexp, scope); 
    // res = Expression.simplify("&",decl,res,null);


    Expression scope = new BasicExpression(referredClass);
    Expression decl = new BinaryExpression(":", btexp, scope); 
    Expression pred = Expression.simplifyAnd(decl,res); 

    if (alreadybound) 
    { return pred; } 
    else 
    { return new BinaryExpression("#", decl, res); } 
  } /* For complete guard, do not add the quantifiers on any object variable */ 

  public Expression toSourceExpression(Vector bound, Expression contextObj)
  { Expression res = new BasicExpression(true);
 
    String bind = bindsTo.getName(); 
    if (bound.contains(bind)) { } 
    else 
    { bound.add(bindsTo.getName()); }  // Surely? 

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      Expression pexp = p.toSourceExpression(bound, contextObj);
      res = Expression.simplify("&",res,pexp,null);
    }

    res = Expression.simplify("&",res,where,null);
    // Expression btexp = new BasicExpression(bindsTo);
    // Expression scope = new BasicExpression(referredClass);
    // Expression decl = new BinaryExpression(":", btexp, scope); 
    // res = Expression.simplify("&",decl,res,null);
    return res;
  }

  public Expression toUMLRSDSantecedent()
  { Vector bnd = new Vector(); 
    return toUMLRSDSantecedent(bnd, null); 
  }     

  public Expression toUMLRSDSantecedent(Vector bound, Expression contextObj)
  { Expression res = new BasicExpression(true);
 
    String bind = bindsTo.getName(); 
    if (bound.contains(bind)) { } 
    else 
    { bound.add(bindsTo.getName()); }  // Surely? 

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      if (p.isManyValued())
      { Expression pexp = p.toSourceExpression0(bound, contextObj);
        res = Expression.simplify("&",res,pexp,null);
      } 
    }

    // res = Expression.simplify("&",res,where,null);
    // Expression btexp = new BasicExpression(bindsTo);
    // Expression scope = new BasicExpression(referredClass);
    // Expression decl = new BinaryExpression(":", btexp, scope); 
    // res = Expression.simplify("&",decl,res,null);
    return res;
  }

  public boolean isEmpty()
  { return part.size() == 0; }
  
  public Expression toTargetExpression(Vector bound, Expression contextObj, Expression setting)
  { Expression res = setting; 
    Expression btexp = new BasicExpression(bindsTo);
    boolean alreadybound = bound.contains(bindsTo + ""); 
    // System.out.println("BOUND = " + bound + " " + bindsTo); 

    if (alreadybound) { } 
    else 
    { bound.add(bindsTo + ""); } 

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      Expression pexp = p.toTargetExpression(bound, btexp,null);
      res = Expression.simplify("#&",res,pexp,null);
    }

    res = Expression.simplify("#&",res,where,null);

    if (alreadybound)    
    { return res; } 

    Expression scope = new BasicExpression(referredClass);
    Expression decl = new BinaryExpression(":", btexp, scope); 
    res = new BinaryExpression("#", decl, res);

    // System.out.println("TARGET ACTION for " + this + " IS " + res); 

    return res;
  }

  public Expression toTargetExpressionOp(Vector bound, Expression contextObj, Expression setting)
  { Expression res = setting; 
    Expression btexp = new BasicExpression(bindsTo);
    boolean alreadybound = bound.contains(bindsTo + ""); 
    // System.out.println("BOUND = " + bound + " " + bindsTo); 

    if (alreadybound) { } 
    else 
    { bound.add(bindsTo + ""); } 

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      Expression pexp = p.toTargetExpressionOp(bound, btexp, null);
      res = Expression.simplify("#&",res,pexp,null);
    }
    res = Expression.simplify("#&",res,where,null);
    if (alreadybound)
    { return res; } 
    Expression scope = new BasicExpression(referredClass);
    Expression decl = new BinaryExpression(":", btexp, scope); 
    res = new BinaryExpression("#", decl, res);

    // System.out.println("TARGET ACTION for " + this + " IS " + res); 
    return res;
  }


  public String toString()
  { String res = bindsTo + " : " + referredClass + " { ";
    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p;
      if (i < part.size() - 1)
      { res = res + ", "; }
    }
    res = res + " }"; 
    if (where != null) 
    { res = res + " { " + where + " }"; } 
    return res;
  } // and { where }

  public String toQVTO(Attribute src, Map whens)
  { Vector bound = new Vector(); 
    // ignore the first target - this will be result variable 

    String res = ""; 
    Expression contxt = new BasicExpression("result"); 
    contxt.setType(new Type(referredClass)); 
    contxt.setElementType(new Type(referredClass)); 

    // Expression scope = new BasicExpression(referredClass);

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p.toQVTO(contxt,src,bound,whens);
      // if (i < part.size() - 1)
      // { res = res + " "; }
    }
    return res; 
  }     

  public String toQVTO(Expression contextObject, Attribute src, Vector bound, Map whens)
  { String res = " object " + referredClass + " { ";

    // Expression scope = new BasicExpression(referredClass);

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p.toQVTO(contextObject,src,bound,whens);
      // if (i < part.size() - 1)
      // { res = res + "; "; }
    }
    res = res + " }"; 
    // if (where != null) 
    // { res = res + " { " + where + " }"; } 
    return res;
  } // and { where }

  public String toQVTOnoobject(Expression contextObject, Attribute src, Vector bound, Map whens)
  { String res = "  "; 
    Expression scope = new BasicExpression(referredClass);

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p.toQVTO(contextObject,src,bound,whens);
      // if (i < part.size() - 1)
      // { res = res + "; "; }
    }
    return res;
  } // and { where }

  public Expression toUMLRSDS(Map whens)
  { Expression res = new BasicExpression(true); 
    
    Vector bound = new Vector(); 
    Expression btexp = new BasicExpression(bindsTo); 
    bound.add(bindsTo + "");

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      Expression pexp = p.toUMLRSDS(bound, btexp, null, whens);
      res = Expression.simplify("#&",res,pexp,null);
    }

    return res; 
  } 

  public Expression toUMLRSDS(Vector bound, Expression contextObj, Expression setting, Map whens)
  { Expression res = setting; 
    Expression btexp = new BasicExpression(bindsTo);
    boolean alreadybound = bound.contains(bindsTo + ""); 
    // System.out.println("BOUND = " + bound + " " + bindsTo); 

    if (alreadybound) { } 
    else 
    { bound.add(bindsTo + ""); } 

    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      p.setEntity(referredClass);
      Expression pexp = p.toUMLRSDS(bound, btexp, null, whens);
      res = Expression.simplify("#&",res,pexp,null);
    }

    res = Expression.simplify("#&",res,where,null);

    if (alreadybound)    
    { return res; } 

    Expression scope = new BasicExpression(referredClass);
    Expression decl = new BinaryExpression(":", btexp, scope); 
    res = new BinaryExpression("#", decl, res);

    // System.out.println("TARGET ACTION for " + this + " IS " + res); 

    return res;
  }

  public Expression toUMLRSDSroot(String contextObj)
  { // Expression res = setting; 
    BasicExpression btexp = new BasicExpression(bindsTo);

    BasicExpression scope = new BasicExpression(referredClass);
   
    BasicExpression cid = new BasicExpression(contextObj.toLowerCase() + "Id"); 
    scope.setArrayIndex(cid); 
    Expression decl = new BinaryExpression("=", btexp, scope); 
    return decl;
  }


  public int complexity() 
  { int res = 3; 
    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p.complexity();
    }
    if (where != null) 
    { res = res + where.syntacticComplexity(); } 
    return res;
  } 

  public int epl()
  { int res = 1; 
    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res = res + p.epl();
    }
    return res; 
  } // counts the PTI variables?     

  public Vector operationsUsedIn() 
  { Vector res = new Vector(); 
    for (int i = 0; i < part.size(); i++)
    { PropertyTemplateItem p = (PropertyTemplateItem) part.get(i);
      res.addAll(p.operationsUsedIn());
    }
    if (where != null) 
    { res.addAll(where.allOperationsUsedIn()); } 
    return res;
  } 

}


