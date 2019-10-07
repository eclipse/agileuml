import java.util.Vector; 
import java.util.Set;
import java.util.HashSet; 
 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* Package: QVT */ 

public class Relation extends QVTRule
{ boolean isTopLevel = true;
  Pattern when = null;
  Pattern where = null;
  Vector variable = new Vector(); // of Attribute

  // derived features: 
  Entity traceEntity = null;
  BehaviouralFeature nontopOp = null;


  public Relation(String nme, RelationalTransformation t)
  { super(nme, t); }

  public Object clone()
  { Relation res = new Relation(name, transformation); 
    res.isTopLevel = isTopLevel; 
    res.isAbstract = isAbstract; 
    
    if (when != null) 
    { res.when = (Pattern) when.clone(); }  
    if (where != null) 
    { res.where = (Pattern) where.clone(); } 

    // clone the variables 
    res.variable.addAll(variable); 

    for (int k = 0; k < domain.size(); k++) 
    { Domain d = (Domain) domain.get(k); 
      if (d instanceof RelationDomain) 
      { RelationDomain rd = (RelationDomain) d; 
        RelationDomain rd1 = (RelationDomain) rd.clone(); 
        rd1.rule = res; 
        res.addDomain(rd1); 
      } 
      else if (d instanceof PrimitiveDomain)
      { PrimitiveDomain pd = (PrimitiveDomain) d; 
        PrimitiveDomain pd1 = (PrimitiveDomain) pd.clone(); 
        pd1.rule = res; 
        res.addDomain(pd1); 
      } 
    } 

    
    return res; 
  } 

  public Vector allLeafRelations(Vector rules) 
  { Vector res = new Vector(); 

    if (isConcrete())
    { res.add(this); 
      return res; 
    } 

    for (int i = 0; i < rules.size(); i++) 
    { Relation rr = (Relation) rules.get(i); 
      if (rr.soverrides != null && name.equals(rr.soverrides))
      { res.addAll(rr.allLeafRelations(rules)); } 
    } 
    return res; 
  } 

  public Relation flattenedRelation(Vector rules)
  { if (soverrides == null) 
    { return this; } 

    Relation superrule = (Relation) NamedElement.findByName(soverrides,rules);

    if (superrule == null) 
    { System.err.println("!! ERROR: rule " + soverrides + " not defined"); 
      return this; 
    } 

    Relation flatsuper = superrule.flattenedRelation(rules); 
    if (flatsuper == null) 
    { System.err.println("!! ERROR: ill-defined overrides"); 
      return this; 
    } 

    return flatsuper.overrideBy(this); 
  } 

  public Relation overrideBy(Relation r) 
  { // must have same top/non-top
    
    if (isTopLevel && r.isTopLevel) { } 
    else if (!isTopLevel && !r.isTopLevel) { } 
    else 
    { System.err.println("!! ERROR: cannot override non-top relation by top or vice-versa: " + this + " " + r); 
      return null; 
    } 

    Vector newdomains = new Vector(); 

    for (int k = 0; k < domain.size(); k++) 
    { Domain d = (Domain) domain.get(k); 
      String dname = d.getRootName(); 
      Domain rdomain = r.getDomain(dname); 

      if (rdomain == null) 
      { newdomains.add(d); } 
      else 
      { Domain newdomain = d.overrideBy(rdomain); 
        if (newdomain != null) 
        { newdomains.add(newdomain); }  
      } 
    } 

    for (int v = 0; v < r.domain.size(); v++) 
    { Domain rd = (Domain) r.domain.get(v); 
      String rdname = rd.getRootName(); 
      Domain mydomain = getDomain(rdname); 

      if (mydomain == null) 
      { newdomains.add(rd); } 
    } 
  
    Relation res = new Relation(r.name, transformation); 
    // replaces *this* and r in the transformation
    res.isTopLevel = isTopLevel; 
    res.isAbstract = r.isAbstract; 

    Pattern newwhen = null; 
    if (when != null)
    { if (r.when != null) 
      { newwhen = when.overrideBy(r.when); 
        res.when = newwhen; 
      } 
      else 
      { res.when = when; }  // clone it
    } 
    else 
    { res.when = r.when; } 
    
    Pattern newwhere = null; 
    if (where != null)
    { if (r.where != null) 
      { newwhere = where.overrideBy(r.where); 
        res.where = newwhere; 
      } 
      else 
      { res.where = where; }  // clone it
    } 
    else 
    { res.where = r.where; } 

    for (int w = 0; w < newdomains.size(); w++) 
    { Domain newd = (Domain) newdomains.get(w); 
      newd.rule = res; 
      res.addDomain(newd); 
    }
    return res; 
  } 
 
  public void expandWhenCalls(Vector abstractrels, java.util.Map leafrels, java.util.Map guards)
  { // replace R(pars) for R : abstractrels by R1(pars) or ... or Rn(pars) 
    // where (R1,...,Rn) = leafrels(R)
    if (when == null) 
    { return; } 

    when = when.expandWhenCalls(abstractrels,leafrels,guards); 
  } 

  public void expandWhereCalls(Vector abstractrels, java.util.Map leafrels, 
                               java.util.Map guards)
  { // replace R(pars) for R : abstractrels by if guardR1(pars) then R1(pars) else ...  
    // where (R1,...,Rn) = leafrels(R)
    if (where == null) 
    { return; } 

    where = where.expandWhereCalls(abstractrels,leafrels,guards); 
  } 

  public Vector retypeParameters(Vector oldparameters)
  { Vector res = new Vector(); 
    for (int i = 0; i < domain.size(); i++) 
    { Domain d = (Domain) domain.get(i); 
      Attribute vd = d.rootVariable; 
      Expression oldpar = (Expression) oldparameters.get(i); 
      Type oldtype = oldpar.getType(); 
      Type newtype = vd.getType(); 
      if (oldtype != null && newtype != null) 
      { if (Type.isSubType(newtype,oldtype) && !Type.isSubType(oldtype,newtype))
        { Expression newpar = new BinaryExpression("->oclAsType",oldpar,new BasicExpression(newtype)); 
          res.add(newpar); 
        }
        else 
        { res.add(oldpar); } 
      } 
      else 
      { res.add(oldpar); } 
    }  
    return res; 
  }       

    
  public Domain getDomain(String nme) 
  { for (int k = 0; k < domain.size(); k++) 
    { Domain d = (Domain) domain.get(k); 
      Attribute root = d.rootVariable; 
      if (root != null && 
          root.getName().equals(nme))
      { return d; } 
    } 
    return null; 
  } 


  public void addVariable(Attribute att)
  { variable.add(att); }

  public void addVariables(Vector atts)
  { variable.addAll(atts); }

  public void setWhen(Pattern w)
  { when = w; }

  public void addWhen(Expression e)
  { when = new Pattern(); 
    when.addPredicate(e); 
  } // divide it up into a sequence of conjuncts

  public void setWhere(Pattern w)
  { where = w; }

  public void addWhere(Expression e)
  { where = new Pattern(); 
    where.addPredicate(e); 
  } // divide it up into a sequence of conjuncts

  public void setIsTop(boolean t)
  { isTopLevel = t; }

  public boolean isTopLevel()
  { return isTopLevel; } 

  public Vector whenCalls(Vector rules) 
  { if (when == null) 
    { return new Vector(); } 
    return when.allRuleCalls(rules); 
  } 

  public Vector recursiveWhenCalls(Vector rules, Vector seen) 
  { Vector res = new Vector(); 
    if (when == null) 
    { return res; }
    Vector seen2 = new Vector(); 
    seen2.addAll(seen); 
    seen2.add(name); 
     
    Vector wcalls = when.allRuleCalls(rules);
    for (int i = 0; i < wcalls.size(); i++) 
    { BasicExpression wc = (BasicExpression) wcalls.get(i); 
      res.add(wc); 
      String wcd = wc.data; 
      if (wcd.equals(name)) 
      { System.err.println("!!! ERROR: calling relation " + name + " recursively"); } 
      else if (seen.contains(wcd)) 
      { System.err.println("!!! ERROR: calling cycle of relations with " + wcd); } 
      else 
      { Relation r = (Relation) NamedElement.findByName(wcd,rules); 
        if (r != null) 
        { Vector res2 = r.recursiveWhenCalls(rules,seen2); 
          res.addAll(res2); 
        } 
      } 
    }
    return res;  
  } 

 
  public Vector whereCalls(Vector rules) 
  { if (where == null) 
    { return new Vector(); } 
    return where.allRuleCalls(rules);
  } 

  public Vector recursiveWhereCalls(Vector rules, Vector seen) 
  { Vector res = new Vector(); 
    if (where == null) 
    { return res; }

    Vector seen2 = new Vector(); 
    seen2.addAll(seen); 
    seen2.add(name); 
     
    Vector wcalls = where.allRuleCalls(rules);
    for (int i = 0; i < wcalls.size(); i++) 
    { BasicExpression wc = (BasicExpression) wcalls.get(i); 
      res.add(wc); 
      String wcd = wc.data; 
      if (wcd.equals(name)) 
      { System.err.println("!!! ERROR: calling relation " + name + " recursively"); } 
      else if (seen.contains(wcd)) 
      { System.err.println("!!! ERROR: calling cycle of relations with " + wcd); } 
      else 
      { Relation r = (Relation) NamedElement.findByName(wcd,rules); 
        if (r != null) 
        { Vector res2 = r.recursiveWhereCalls(rules,seen2); 
          res.addAll(res2); 
        } 
      } 
    }
    return res;  
  } 

  public String toString()
  { String res = "relation";

    if (isTopLevel)
    { res = "top " + res; }
    if (isAbstract)
    { res = "abstract " + res; }

    res = res + " " + name + "\n";

    if (soverrides != null) 
    { res = res + " overrides " + soverrides + " "; } 

    res = res + "{\n";

    for (int i = 0; i < variable.size(); i++)
    { Attribute att = (Attribute) variable.get(i);
      res = res + "  " + att.getName() + " : " + att.getType(); 
      Expression ini = att.getInitialExpression(); 
      if (ini != null) 
      { res = res + " = " + ini + ";\n"; } 
      else 
      { res = res + ";\n"; }
    }

    res = res + "\n";

    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      res = res + "  " + d + "\n";
    }

    res = res + " " + "\n";

    if (when != null)
    { res = res + "  when\n" + 
                  "  { " + when + " }\n"; 
    }

    if (where != null)
    { res = res + "  where\n" + 
                  "  { " + where + " }\n";
    }
    res = res + "}\n\n";
    return res; 
  }

  // rd, wr frames

  public int complexity()
  { int res = 0;
    for (int i = 0; i < variable.size(); i++)
    { Attribute att = (Attribute) variable.get(i);
      res = res + 2 + att.getType().complexity();
      Expression ini = att.getInitialExpression(); 
      if (ini != null) 
      { res = res + ini.syntacticComplexity(); } 
    }

    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      res = res + d.complexity();
    }

    if (when != null)
    { res = res + when.complexity(); }

    if (where != null)
    { res = res + where.complexity(); }

    System.out.println("*** Complexity for rule " + name + " is " + res); 
    
    return res; 
  }

  public int cyclomaticComplexity()
  { int res = 0;
    
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      res = res + d.cyclomaticComplexity();
    }

    if (when != null)
    { res = res + when.cyclomaticComplexity(); }

    if (where != null)
    { res = res + where.cyclomaticComplexity(); }

    System.out.println("*** Cyclomatic complexity for rule " + name + " is " + res); 
    
    return res; 
  }

  public int epl()
  { int res = variable.size(); 
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      res = res + d.epl();
    }
    if (when != null)
    { res = res + when.epl(); }
    if (where != null)
    { res = res + where.epl(); }

    System.out.println("*** EPL for rule " + name + " is " + res); 

    return res; 
  }     

  public Map getCallGraph(Map res)
  { int efo = 0; 
    Set calls = new HashSet(); 
    Vector ops = new Vector(); 

    // assume no calls *within* the domains or variable initialisations

    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      ops.addAll(d.operationsUsedIn());
    }

    if (when != null)
    { ops.addAll(when.operationsUsedIn()); }

    if (where != null)
    { ops.addAll(where.operationsUsedIn()); }

    for (int i = 0; i < ops.size(); i++) 
    { String opname = ops.get(i) + ""; 
      if (calls.contains(opname)) { } 
      else
      { res.add_pair(name, opname); 
        calls.add(opname);
      }  
    } 

    if (soverrides != null) 
    { res.add_pair(soverrides, name); } 
  
    efo = calls.size(); 
    System.out.println("*** EFO for rule " + name + " is " + efo); 
    
    return res; 
  }     



  public void addTraceEntity(Vector entities, Vector assocs)
  { Entity e = getTraceEntity(assocs); 
    if (e != null) 
    { entities.add(e); } 
  } 

  public Entity getTraceEntity(Vector assocs)
  { String ename = getName() + "$trace";
    Vector atts = new Vector(); 

    Entity res = new Entity(ename);
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d.rootVariable != null)
      { Attribute att = d.rootVariable; 
        BasicExpression dini = new BasicExpression("null"); 
        dini.umlkind = Expression.VALUE;
        dini.type = att.getType(); 
        dini.elementType = att.getElementType();  
        if (dini.type.isEntity())
        { att.setInitialExpression(dini);
          att.setInitialValue("null"); 
        }  
        atts.add(att); 
        // res.addAttribute(att); 

        if (dini.type.isEntity())
        { Entity ent2 = dini.type.getEntity(); 
          Association ast = new Association(res,ent2,Association.MANY,Association.ONE,
                                            "traces$" + getName() + "$" + att.getName(),att.getName()); 
          assocs.add(ast);
          res.addAssociation(ast);  
          if (att.isSource())
          { ast.setSource(true); } 
          Association invast = ast.generateInverseAssociation(); 
          ent2.addAssociation(invast);
        } 
        else 
        { res.addAttribute(att); } 
      }
    } // actually assocs. - addRole? 

    // and, add variables that are used in the *when* clause: 
    Vector whenvars = new Vector(); 
    if (when != null) 
    { whenvars = when.variablesUsedIn(); } 
    // should be subset of the domain root variables and object template variables

    // System.out.println("*** Variables used in when clause: " + whenvars); 

    // Get corresponding attributes from the domains: 

    Vector wdatts = new Vector(); 
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d instanceof RelationDomain)
      { Vector datts = ((RelationDomain) d).objectTemplateAttributes(); 
        wdatts = VectorUtil.union(wdatts,datts);
      }  
    } 

    wdatts.removeAll(atts); 

    // System.out.println("*** Additional attributes for " + getName() + " : " + wdatts); 
    
    for (int k = 0; k < wdatts.size(); k++)
    { Attribute watt = (Attribute) wdatts.get(k); 
      BasicExpression wdini = new BasicExpression("null"); 
      wdini.umlkind = Expression.VALUE;
      wdini.type = watt.getType(); 
      wdini.elementType = watt.getElementType();  
      // res.addAttribute(watt); 

      if (wdini.type.isEntity())
      { Entity ent2 = wdini.type.getEntity(); 
        watt.setInitialExpression(wdini);
        watt.setInitialValue("null");  
        Association ast = new Association(res,ent2,Association.MANY,Association.ONE,
                                          "traces$" + getName() + "$" + watt.getName(),watt.getName()); 
        assocs.add(ast);
        res.addAssociation(ast);  
        Association invast = ast.generateInverseAssociation(); 
        ent2.addAssociation(invast);
      } 
    } // actually assocs. - addRole? 

  /*  for (int f = 0; f < whenvars.size(); f++) 
    { BasicExpression wv = (BasicExpression) whenvars.get(f); 
      String wvar = wv.innermostData(); 
      Attribute tv = res.getAttribute(wvar); 
      Association tva = res.getRole(wvar); 
      if (tv == null && tva == null) 
      { System.out.println("!! Warning: variable " + wvar + 
                           " used in when clause of " + getName() + 
                           " but not declared in any domain"); 
      } // doesn't consider inheritance
    } */ 

    traceEntity = res;
    return res;
  }

  
  public Vector targetEntities()
  { Vector res = new Vector(); 
  
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d.isEnforceable)
      { Vector tatts = d.objectTemplateAttributes();
        for (int k = 0; k < tatts.size(); k++) 
        { Attribute tatt = (Attribute) tatts.get(k); 
          Type ttype = tatt.getType(); 
          if (ttype != null && ttype.isEntity())
          { Entity tent = ttype.getEntity(); 
            if (res.contains(tent)) { } 
            else 
            { res.add(tent); } 
          } 
        } 
      } 
    } 
    return res; 
  } 

  public Vector targetAttributes()
  { Vector res = new Vector(); 
  
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d.isEnforceable)
      { Vector tatts = d.objectTemplateAttributes();
        res = VectorUtil.union(res,tatts); 
      } 
    } 
    return res; 
  } 

  public Vector targetVariables() 
  { Vector tatts = targetAttributes(); 
    Vector tvars = new Vector(); 
    for (int i = 0; i < tatts.size(); i++) 
    { Attribute tatt = (Attribute) tatts.get(i); 
      tvars.add(tatt.getName()); 
    }
    return tvars; 
  } 

  public Vector sourceAttributes()
  { Vector res = new Vector(); 
  
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d.isEnforceable) { } 
      else 
      { Vector tatts = d.objectTemplateAttributes();
        res = VectorUtil.union(res,tatts); 
      } 
    } 
    return res; 
  } 

  public Vector sourceVariables() 
  { Vector tatts = sourceAttributes(); 
    Vector tvars = new Vector(); 
    for (int i = 0; i < tatts.size(); i++) 
    { Attribute tatt = (Attribute) tatts.get(i); 
      tvars.add(tatt.getName()); 
    }
    return tvars; 
  } 

  public Vector allAttributes()
  { Vector res = new Vector(); 
  
    Vector rootvariables = new Vector(); 
    Vector othervariables = new Vector(); 

    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      rootvariables.add(d.rootVariable); 
      Vector tatts = d.objectTemplateAttributes();
      othervariables = VectorUtil.union(othervariables,tatts); 
    } 
    res.addAll(rootvariables); 
    othervariables.removeAll(rootvariables);
    res.addAll(othervariables);  
    return res; 
  } 

  public Vector allVariables() 
  { Vector tatts = allAttributes(); 
    Vector tvars = new Vector(); 
    for (int i = 0; i < tatts.size(); i++) 
    { Attribute tatt = (Attribute) tatts.get(i); 
      tvars.add(tatt.getName()); 
    }
    return tvars; 
  } 
  
  public Expression assertTraceRelation(Vector entities)
  { // R$trace->exists( r$x | r$x.p1 = p1 & ... & r$xx.pn = pn)

    // String var = name.toLowerCase() + "$x";
    String var = Identifier.nextIdentifier("tr$");

    String traceop = name + "$trace";
    BasicExpression varexp = new BasicExpression(var);
    varexp.umlkind = Expression.VARIABLE; 

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    varexp.type = new Type(ent); 
    varexp.elementType = new Type(ent);
    varexp.entity = ent;  
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 

    Vector fparams = ent.allProperties();
    // must be at least 1

    // Attribute pivot = (Attribute) fparams.get(0);
    // Attribute selfv = new Attribute("self", pivot.getType(), ModelElement.INTERNAL);  
    // BasicExpression fexp = new BasicExpression(pivot);
    // fexp.objectRef = varexp;
    // pred = new BinaryExpression("=", fexp, new BasicExpression(selfv));
    
    for (int i = 0; i < fparams.size(); i++)
    { Attribute fp = (Attribute) fparams.get(i);
      BasicExpression fpexp = new BasicExpression(fp);
      fpexp.objectRef = varexp;
      fpexp.umlkind = Expression.ROLE; 
      BasicExpression pval = new BasicExpression(fp);
      BinaryExpression eq = new BinaryExpression("=", fpexp, pval);
      pred = Expression.simplifyAnd(pred, eq); 
    }
    return new BinaryExpression("#", dran, pred);
  }

  public Expression simpleCheckTrace(Vector entities)
  { // r$x : R$trace & p1 = r$x.p1 & ... & pn = r$xx.pn

    // String var = name.toLowerCase() + "$x";
    String var = Identifier.nextIdentifier("tr$");

    String traceop = name + "$trace";
    BasicExpression varexp = new BasicExpression(var);

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    eexp.setPrestate(true); 
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 

    Vector fparams = ent.allProperties();
    // must be at least 1

    // Attribute pivot = (Attribute) fparams.get(0);
    // Attribute selfv = new Attribute("self", pivot.getType(), ModelElement.INTERNAL);  
    // BasicExpression fexp = new BasicExpression(pivot);
    // fexp.objectRef = varexp;
    // pred = new BinaryExpression("=", fexp, new BasicExpression(selfv));
    
    for (int i = 0; i < fparams.size(); i++)
    { Attribute fp = (Attribute) fparams.get(i);
      BasicExpression fpexp = new BasicExpression(fp);
      fpexp.objectRef = varexp;
      BasicExpression pval = new BasicExpression(fp);
      BinaryExpression eq = new BinaryExpression("=", pval, fpexp);
      pred = Expression.simplifyAnd(pred, eq); 
    }
    return new BinaryExpression("&", dran, pred);
  }

  public Expression assertNotTraceRelation(Vector entities) 
  { Vector fparams = sourceAttributes();
    return assertNotTraceRelation(fparams,entities); 
  } 

  public Expression assertNotTraceRelation(Vector fparams, Vector entities)
  { // not(R$trace@pre->exists( r$x | r$x.p1 = p1 & ... & r$x.pn = pn)) for source domains only
    // not(p1.traces$R$p1->exists( r$x | r$x.p2 = p2 & ... & r$x.pn = pn)) for 1st object source p1

    String var = Identifier.nextIdentifier("tr$");
    // String var = name.toLowerCase() + "$x";
    String traceop = name + "$trace";
    BasicExpression varexp = new BasicExpression(var);

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    eexp.setPrestate(true); 

    // 431   String qf = eexp.queryForm(null,true); 
    // System.out.println("***** " + qf); 

    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 


    // must be at least 1, assume at least one source object variable

    if (fparams.size() == 0) 
    { return pred; } 

    int ind = 0; 

    Attribute pivot = (Attribute) fparams.get(0);
    // Assumes that this is a reference, ie., type is an entity type. 
    // Attribute selfv = new Attribute("self", pivot.getType(), ModelElement.INTERNAL);  
    Type t = pivot.getType(); 

    while (!(t.isEntity() && pivot.isSource()))
    { ind++; 
      if (ind > fparams.size())
      { System.err.println("!!!! Error: no checkonly object domain for " + this); 
        return pred; 
      } 
      pivot = (Attribute) fparams.get(ind);  
      t = pivot.getType(); 
    }     

    BasicExpression fexp = new BasicExpression(pivot);
    // fexp.objectRef = varexp;
    // pred = new BinaryExpression("=", fexp, new BasicExpression(selfv));
    Entity pivotEntity = t.getEntity(); 
    Association ast = pivotEntity.getRole("traces$" + name + "$" + pivot.getName()); 

    if (ast == null) { return pred; } 
    
    eexp = new BasicExpression(ast); 
    eexp.setPrestate(true); 
    eexp.setObjectRef(fexp); 
    dran = new BinaryExpression(":", varexp, eexp); 
    

    for (int i = 0; i < fparams.size(); i++)
    { if (i != ind) 
      { Attribute fp = (Attribute) fparams.get(i);
        if (fp.isSource())
        { BasicExpression fpexp = new BasicExpression(fp);
          fpexp.setPrestate(true); 
          fpexp.objectRef = varexp;
          BasicExpression pval = new BasicExpression(fp);
          BinaryExpression eq = new BinaryExpression("=", fpexp, pval);
          pred = Expression.simplifyAnd(pred, eq);
        } 
      }  
    }
    BinaryExpression existstrace = new BinaryExpression("#", dran, pred);
    return new UnaryExpression("not", existstrace); 
  }

  public Expression assertOpTraceRelation(Vector entities)
  { // R$trace->exists( r$x | r$x.p1 = p1 & ... & r$xx.pn = pn)

    String var = name.toLowerCase() + "$x";
    String traceop = name + "$trace";
    BasicExpression varexp = new BasicExpression(var);
    varexp.umlkind = Expression.VARIABLE; 

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 
    varexp.entity = ent; 
    varexp.elementType = new Type(ent); 
    varexp.type = new Type(ent); 

    Vector fparams = ent.allProperties();
    // must be at least 1

    
    for (int i = 0; i < fparams.size(); i++)
    { Attribute fp = (Attribute) fparams.get(i);
      BasicExpression fpexp = new BasicExpression(fp);
      fpexp.objectRef = varexp;
      fpexp.umlkind = Expression.ROLE; 
      BasicExpression pval = new BasicExpression(fp);
      BinaryExpression eq = new BinaryExpression("=", fpexp, pval);
      pred = Expression.simplifyAnd(pred, eq); 
    }
    Expression res = new BinaryExpression("#", dran, pred);
    return res; 
  }

  public Expression assertOpTraceRelation1(Vector entities)
  { // R$trace->exists1( r$x | r$x.p1 = p1 & ... & r$xx.pn = pn)

    String var = name.toLowerCase() + "$x";
    String traceop = name + "$trace";
    BasicExpression varexp = new BasicExpression(var);
    varexp.umlkind = Expression.VARIABLE; 

    Entity ent = (Entity) ModelElement.lookupByName(traceop, entities);
    // or of the use case? Or relation doms?
    Expression pred = new BasicExpression(true);
    if (ent == null)
    { return pred; }

    BasicExpression eexp = new BasicExpression(ent);
    BinaryExpression dran = new BinaryExpression(":", varexp, eexp); 
    varexp.entity = ent; 
    varexp.elementType = new Type(ent); 
    varexp.type = new Type(ent); 

    Vector fparams = ent.allProperties();
    // must be at least 1

    
    for (int i = 0; i < fparams.size(); i++)
    { Attribute fp = (Attribute) fparams.get(i);
      BasicExpression fpexp = new BasicExpression(fp);
      fpexp.objectRef = varexp;
      BasicExpression pval = new BasicExpression(fp);
      BinaryExpression eq = new BinaryExpression("=", fpexp, pval);
      pred = Expression.simplifyAnd(pred, eq); 
    }
    Expression res = new BinaryExpression("#1", dran, pred);
    return res; 
  }

  public BehaviouralFeature getNontopOp(UseCase pres, UseCase transuc, Vector entities, Vector types)
  { if (isTopLevel)
    { return null; }
    // Every relation needs a callable version, because even top relations can be called 

    String traceent = name + "$trace";
    Entity traceEntity = (Entity) ModelElement.lookupByName(traceent, entities);

    String conopname = name.toLowerCase() + "$con";
    String presopname = name.toLowerCase() + "$pres";

    Vector bound = new Vector();
    Vector pars = new Vector();
    for (int j = 0; j < domain.size(); j++)
    { Domain d = (Domain) domain.get(j);
      if (d.rootVariable != null)
      { pars.add(d.rootVariable); 
        bound.add(d.rootVariable.getName());
      }
    } // the parameters of the op

    Expression precond = assertNotTraceRelation(pars,entities); 

    Vector ante = toSourceExpressionOp(bound,entities);
    Expression succ = toTargetExpressionOp("Tau$Con",transuc,bound,entities);
    // Expression assertR = assertOpTraceRelation1(entities); 
    // Expression newsucc = Expression.simplify("&", succ, assertR, null); 

    Expression newsucc = succ; 
    Expression post = new BasicExpression(true); 

    for (int i = 0; i < ante.size(); i++) 
    { Expression aexp = (Expression) ante.get(i); 
      Expression posti = new BinaryExpression("=>", aexp, newsucc);
      posti.setBrackets(true); 
      post = Expression.simplifyAnd(post,posti); 
    } 

    /* Version for Pres constraints: */

    Expression traceguard = simpleCheckTrace(entities); 
    Vector allobjectvars = allAttributes(); // domain roots and their template roots 
    BasicExpression becall = new BasicExpression(name); 
    Vector params = new Vector(); 
    Vector presbound = new Vector(); 
    for (int i = 0; i < allobjectvars.size(); i++) 
    { Attribute att = (Attribute) allobjectvars.get(i); 
      params.add(new BasicExpression(att)); 
      presbound.add(att.getName()); 
    } 

    
    becall.setParameters(params); 
    // Expression precond2 = becall.testTraceRelationWhen(entities,presbound); 
 
    Expression postpres = new BasicExpression(true); 
    BehaviouralFeature tracebf = new BehaviouralFeature(presopname); 
    Expression pressucc = toTargetExpressionOpPres("Tau$Pres",pres,presbound,entities);
    for (int i = 0; i < ante.size(); i++) 
    { Expression aexp = (Expression) ante.get(i); 
      Expression caexp = Expression.simplifyAnd(traceguard,aexp); 
      Expression postipres = new BinaryExpression("=>", caexp, pressucc);
      postipres.setBrackets(true); 
      postpres = Expression.simplifyAnd(postpres,postipres); 
    } 
    tracebf.setParameters(pars); 
    tracebf.setQuery(true);   // it cannot create traces or other objects
    tracebf.setStatic(true);  
    tracebf.setUseCase(pres);
    tracebf.setPost(postpres);
    tracebf.setPre(new BasicExpression(true));
    tracebf.setType(new Type("boolean",null)); 
    pres.addOperation(tracebf); 


    /* Version for Con_tau */ 

    BehaviouralFeature bf = new BehaviouralFeature(conopname);
    // bf.setUseCase(transuc);
    bf.setParameters(pars); 
    bf.setQuery(true); 
    // bf.setEntity(traceEntity);
    // bf.setPost(post);
    bf.setPre(precond);   // actually (not(precond) => true) should be a post conjunct
    nontopOp = bf;
    transuc.addOperation(bf);  
    bf.setType(new Type("boolean",null)); 
    bf.setUseCase(transuc);

    // Constraint opact = new Constraint(ante,newsucc); 
    // opact.typeCheck(types,entities,new Vector()); 
    // opact.setUseCase(transuc); 
    // opact.analyseConstraint(new Vector());
    // Statement stat = opact.mapToDesign0(transuc);
 
    bf.setPost(post);
    // bf.setActivity(stat);   
    return bf;
  }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { Vector env2 = new Vector(); 
    env2.addAll(env); 
    env2.addAll(variable); 
    
    for (int x = 0; x < variable.size(); x++) 
    { Attribute att = (Attribute) variable.get(x); 
      Expression ini = att.getInitialExpression(); 
      if (ini != null) 
      { ini.typeCheck(types,entities,contexts,env2); } 
    }


    for (int i = 0; i < domain.size(); i++) 
    { Domain d = (Domain) domain.get(i); 
      d.typeCheck(types,entities,contexts,env2); 
    }
    if (when != null)
    { when.typeCheck(types,entities,contexts,env2); }
    if (where != null)
    { where.typeCheck(types,entities,contexts,env2); }
    return true; 
  }  
   
  public Vector toSourceExpression(Vector bound, Vector entities)
  { // Returns list of antecedents, one for each disjunct of source. 

    Vector res = new Vector(); 
    Vector wnexp = new Vector(); 

    if (when != null) 
    { wnexp = when.whenToExp(bound, transformation.rule, entities); } 
    else 
    { wnexp.add(new BasicExpression(true)); }  

    Expression ante = new BasicExpression(true);
    for (int x = 0; x < variable.size(); x++) 
    { Attribute att = (Attribute) variable.get(x); 
      Expression ini = att.getInitialExpression(); 
      if (ini != null) 
      { Expression setatt = new BinaryExpression("=", new BasicExpression(att), ini); 
        ante = Expression.simplify("&", ante, setatt, null); 
      }
    } 

    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isCheckable)
      { Expression ex = d.toSourceExpression(bound); 
        ante = Expression.simplify("&", ante, ex, null);
      }
    } 

    for (int j = 0; j < wnexp.size(); j++) 
    { Expression wdisj = (Expression) wnexp.get(j); 
      res.add(Expression.simplify("&", ante, wdisj, null));
    } 
      
    return res;
  }

  /*  public Vector toSourceExpression(Vector bound, Vector entities)
  { // for top-level rules only

        
    Expression res = new BasicExpression(true);
    Vector whencases = new Vector(); 
    whencases.add(res); 

    if (when != null) 
    { whencases = when.whenToExp(bound, transformation.rule, entities); } 

    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isCheckable)
      { Expression ex = d.toSourceExpression(bound);
        Vector newlist = new Vector(); 
 
        for (int j = 0; j < whencases.size(); j++) 
        { Expression ante = (Expression) whencases.get(j); 
          Expression newres = Expression.simplify("&", ante, ex, null);
          newlist.add(newres); 
        } 
        whencases.clear(); 
        whencases.addAll(newlist); 
      }
    } 

    return whencases;
  } */ 

  public Vector toGuardCondition(Vector bound, Vector entities)
  { // for invoked rules only
    // b : T for each bound variable, and E->exists( e ... ) for other object variables
        
    Expression res = new BasicExpression(true);
    Vector whencases = new Vector(); 
    whencases.add(res); 

    if (when != null) 
    { whencases = when.checkWhen(bound, transformation.rule, entities); } 

    Vector rootvariables = new Vector(); 

    Vector newlist = new Vector(); 
    for (int j = 0; j < whencases.size(); j++) 
    { Expression ante = (Expression) whencases.get(j);
      Expression ex = ante;  
      for (int i = domain.size() - 1; i >= 0; i--)
      { Domain d = (Domain) domain.get(i);
        if (d.isCheckable)
        { ex = d.toGuardCondition(rootvariables,ex); } 
      }
      newlist.add(ex); 
    } 

    return newlist;
  }

/* 
  public Expression toInverseSuccedent(Vector bound, Vector entities)
  { // for top-level rules only. Not used. 

    Expression res = new BasicExpression(true);

    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isCheckable)
      { Expression ex = d.toTargetExpression(bound); 
        res = Expression.simplify("&", res, ex, null);
      }
    } 

    if (when != null) 
    { Vector wnexps = when.whenToExp(bound, transformation.rule, entities); 
      Expression wexp = (Expression) wnexps.get(0); 
      res = Expression.simplify("#&", res, wexp, null);
    } // and where as well 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp("Tau$Con",bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  } */ 

  public Expression toTargetExpression(UseCase con, Vector bound, Vector entities)
  { // for top-level rules only. For Con constraints

    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toTargetExpression(bound); 
        res = Expression.simplify("#&", res, ex, null);
      }
    }

    // Put the trace relation here. 
    Expression assertR = assertTraceRelation(entities); 
    res = Expression.simplify("#&", res, assertR, null); 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp("Tau$Con", con, bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  } // add the # quantifiers at the outer level. 

/* 
  public Expression toInverseAntecedent(Vector bound)
  { // for top-level rules only
    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toSourceExpression(bound); 
        res = Expression.simplify("&", res, ex, null);
      }
    }
    return res;
  } */ 

  public Vector toSourceExpressionOp(Vector bound, Vector entities)
  { // Returns list of antecedents, one for each disjunct of source. 

    Vector res = new Vector(); 
    Vector wnexp = new Vector(); 

    if (when != null) 
    { wnexp = when.whenToExp(bound, transformation.rule, entities); } 
    else 
    { wnexp.add(new BasicExpression(true)); }  

    Expression ante = new BasicExpression(true);

    for (int x = 0; x < variable.size(); x++) 
    { Attribute att = (Attribute) variable.get(x); 
      Expression ini = att.getInitialExpression(); 
      if (ini != null) 
      { Expression setatt = new BinaryExpression("=", new BasicExpression(att), ini); 
        ante = Expression.simplify("&", ante, setatt, null); 
      }
    } 

    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isCheckable)
      { Expression ex = d.toSourceExpressionOp(bound); 
        ante = Expression.simplify("&", ante, ex, null);
      }
    } 

    for (int j = 0; j < wnexp.size(); j++) 
    { Expression wdisj = (Expression) wnexp.get(j); 
      res.add(Expression.simplify("&", ante, wdisj, null));
    } 
      
    return res;
  }

  public Expression toTargetExpressionPres(Vector bound, UseCase pres, Vector entities)
  { // for Pres constraints. Omits trace creation
 
    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toTargetExpressionOp(bound); 
        res = Expression.simplify("#&", res, ex, null);
      }
    }

    // Put the trace relation here. 
    // Expression assertR = assertOpTraceRelation1(entities); 
    // res = Expression.simplify("&", res, assertR, null); 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp("Tau$Pres", pres, bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  }

  public Expression toTargetExpressionOp(String calltarget, UseCase uc, Vector bound, Vector entities)
  { // for non top-level rules only. 
 
    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toTargetExpressionOp(bound); 
        res = Expression.simplify("#&", res, ex, null);
      }
    }

    // Put the trace relation here. 
    Expression assertR = assertOpTraceRelation(entities); 
    res = Expression.simplify("#&", res, assertR, null); 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp(calltarget, uc, bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  }

  public Expression toTargetExpressionOp1(String calltarget, UseCase uc, Vector bound, Vector entities)
  { // for non top-level rules only. 
 
    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toTargetExpressionOp(bound); 
        res = Expression.simplify("#&", res, ex, null);
      }
    }

    // Put the trace relation here. 
    Expression assertR = assertOpTraceRelation1(entities); 
    res = Expression.simplify("#&", res, assertR, null); 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp(calltarget, uc, bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  }

  public Expression toTargetExpressionOpPres(String calltarget, UseCase uc, 
                                             Vector bound, Vector entities)
  { // for non top-level rules only. 
 
    Expression res = new BasicExpression(true);
    for (int i = 0; i < domain.size(); i++)
    { Domain d = (Domain) domain.get(i);
      if (d.isEnforceable)
      { Expression ex = d.toTargetExpressionOp(bound); 
        res = Expression.simplify("#&", res, ex, null);
      }
    }

    // Put the trace relation here. 
    // Expression assertR = assertOpTraceRelation1(entities); 
    // res = Expression.simplify("#&", res, assertR, null); 

    if (where != null) 
    { Vector tvars = targetVariables(); 
      Expression wexp = where.whereToExp(calltarget, uc, bound, tvars, transformation.rule); 
      res = Expression.simplify("#&", res, wexp, null);
    } 

    return res;
  }

  public Vector toConstraints(UseCase uc, Vector entities, Vector types)
  { // :: whenexp & source domain predicates & not(sources in relation trace) => 
    //                             target domain predicates & add to relation trace

    Vector res = new Vector(); 
    if (isAbstract) 
    { return res; } 

    Vector bound = new Vector();
    if (isTopLevel)
    { Vector ante = toSourceExpression(bound,entities);
      Expression succ = toTargetExpression(uc, bound, entities);
      // Expression fconj = ante.firstConjunct(); 
      // Expression rem = ante.removeFirstConjunct(); 

      // System.out.println(fconj); 

      /* if (fconj != null && (fconj instanceof BinaryExpression))
      { BinaryExpression fcnj = (BinaryExpression) fconj; 
        if (":".equals(fcnj.operator) && fcnj.left instanceof BasicExpression)
        { Expression newante = rem.dereference((BasicExpression) fcnj.left);
          Expression ntrace = assertOpTraceRelation(entities); 
          newante = Expression.simplifyAnd(newante,new UnaryExpression("not", ntrace)); 
          Expression newsucc = succ.dereference((BasicExpression) fcnj.left);
          Expression ctrace = assertTraceRelation(entities); 
          newsucc = Expression.simplify("#&", newsucc, ctrace, null); 
          Constraint res = new Constraint(newante, newsucc); 
          res.setOwner(fcnj.right.entity); 
          return res; 
        } 
      } */ 

      Expression ntrace = assertNotTraceRelation(entities); 
      // Expression createtrace = assertTraceRelation(entities); 
      // Expression newsucc = Expression.simplify("#&", succ, createtrace, null); 

      Expression newsucc = succ; 

      for (int j = 0; j < ante.size(); j++) 
      { Expression antej = (Expression) ante.get(j); 
        Expression newantej = Expression.simplifyAnd(antej,ntrace); 
        
        Constraint con = new Constraint(newantej,newsucc);
        con.setOwner(null); 
        con.typeCheck(types,entities,new Vector()); 
        res.add(con);
      } 
      return res; 
    }
    return null; // make an operation
  }

  public Vector toPreservationConstraints(UseCase uc, Vector entities, Vector types)
  { // R$trace:: 
    //   ante => succ  with all domain variables bound 
    // Split into several constraints if antecedent is a disjunction because of the when clause

    Vector res = new Vector(); 
    if (isAbstract) 
    { return res; } 

    String tracename = getName() + "$trace";
    Entity ent = (Entity) ModelElement.lookupByName(tracename, entities);

    Vector bound = new Vector();
    Vector pars = new Vector();
    bound.addAll(allVariables()); 
  
    Vector ante = toSourceExpressionOp(bound,entities);
    Expression succ = toTargetExpressionPres(bound,uc,entities);  // but no trace needed
    for (int i = 0; i < ante.size(); i++) 
    { Expression antei = (Expression) ante.get(i); 
      Constraint post = new Constraint(antei, succ);
      post.typeCheck(types,entities,new Vector()); 
      post.setOwner(ent);
      post.setUseCase(uc); 
      res.add(post); 
    } 
    return res; 
  }


  public Expression deleteEnforceableDomainsExp()
  { Expression res = new BasicExpression(true); 
    for (int i = 0; i < domain.size(); i++) 
    { Domain rd = (Domain) domain.get(i); 
      if (rd.isEnforceable)
      { Expression delrd = rd.deleteDomainExp(); 
        res = Expression.simplify("&",res,delrd,null); 
      } 
    } 
    return res; 
  } 

  /*      
  public Constraint toInverseConstraint(Vector entities, Vector types)
  { Vector bound = new Vector();
    if (isTopLevel)
    { Expression ante = toInverseAntecedent(bound);
      Expression succ = toInverseSuccedent(bound,entities);
      Expression fconj = ante.firstConjunct(); 
      Expression rem = ante.removeFirstConjunct(); 
      // System.out.println(fconj); 
      if (fconj != null && (fconj instanceof BinaryExpression))
      { BinaryExpression fcnj = (BinaryExpression) fconj; 
        if (":".equals(fcnj.operator) && fcnj.left instanceof BasicExpression)
        { Expression newante = rem.dereference((BasicExpression) fcnj.left);
          Expression newsucc = succ.dereference((BasicExpression) fcnj.left);
          Expression notsucc = new UnaryExpression("not", newsucc); 
          Expression deldoms = deleteEnforceableDomainsExp(); 
          Expression invsucc = deldoms.dereference((BasicExpression) fcnj.left);
          Expression invante = Expression.simplify("&", newante, notsucc, null); 
          Constraint res = new Constraint(invante,invsucc);
          res.setOwner(fcnj.right.entity); 
          return res; 
        } 
      } 
      Expression notsucc = new UnaryExpression("not", succ); 
      Expression deldoms = deleteEnforceableDomainsExp(); 
      Expression invante = Expression.simplify("&", ante, notsucc, null); 
      Constraint con = new Constraint(invante,deldoms);
      return con;
    }
    return null; // make an operation
  }

  public Vector toInverseConstraints(Vector entities, Vector types)
  { // D:: not(R$trace@pre->exists( r$trace : r$trace.d = self )) => self->isDeleted()
    // for each enforce domain d : D

    Vector res = new Vector(); 


    Entity trent = (Entity) ModelElement.lookupByName(name + "$trace", entities); 
    if (trent == null) 
    { return res; } 

    for (int d = 0; d < domain.size(); d++) 
    { Domain rd = (Domain) domain.get(d); 
      if (rd.isEnforceable)
      { Expression checktr = rd.checkTraceEmpty(trent); 
        Expression delrd = rd.deleteDomainExp(); 
        // UnaryExpression ante = new UnaryExpression("not", checktr); 

        Constraint cc = new Constraint(checktr,delrd);
        cc.setOwner(rd.rootVariable.getType().getEntity()); 
        res.add(cc); 
      } 
    }
    return res; 
  }  */ 

  public Expression checkTrace(Entity rtrace, Attribute att)
  { // R$trace@pre->exists( r$trace : r$trace.att = self )
    // traces$R$d->notEmpty()

    Type t = att.getType(); 
    if (t != null && t.isEntity())
    { Entity e = t.getEntity(); 
      Association ast = e.getRole("traces$" + getName() + "$" + att.getName()); 
      if (ast != null) 
      { BasicExpression traces = new BasicExpression(ast);
        traces.setPrestate(true); 
        UnaryExpression isempty = new UnaryExpression("->notEmpty", traces); 
        return isempty; 
      } 
    }  

    String tracename = rtrace.getName(); 
    Type ttype = new Type(rtrace); 
    BasicExpression tracevar = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression traceent = new BasicExpression(rtrace); 
    traceent.setPrestate(true); 

    BasicExpression traced = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression d = new BasicExpression(att); 
    d.setObjectRef(traced); 
    BasicExpression selfvar = new BasicExpression(att.getType(), "self"); 
    BinaryExpression eq = new BinaryExpression("=", d, selfvar); 
    BinaryExpression indom = new BinaryExpression(":", tracevar, traceent); 
    BinaryExpression res = new BinaryExpression("#", indom, eq); 
    return res; 
  } 

  public Expression checkTraceEmpty(Entity rtrace, Attribute att)
  { // not(R$trace@pre->exists( r$trace : r$trace.att = self ))
    // traces$R$d->isEmpty()

    Type t = att.getType(); 
    if (t != null && t.isEntity())
    { Entity e = t.getEntity(); 
      Association ast = e.getRole("traces$" + getName() + "$" + att.getName()); 
      if (ast != null) 
      { BasicExpression traces = new BasicExpression(ast);
        traces.setPrestate(true); 
        UnaryExpression isempty = new UnaryExpression("->isEmpty", traces); 
        return isempty; 
      } 
    }  

    String tracename = rtrace.getName(); 
    Type ttype = new Type(rtrace); 
    BasicExpression tracevar = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression traceent = new BasicExpression(rtrace); 
    traceent.setPrestate(true); 

    BasicExpression traced = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression d = new BasicExpression(att); 
    d.setObjectRef(traced); 
    BasicExpression selfvar = new BasicExpression(att.getType(), "self"); 
    BinaryExpression eq = new BinaryExpression("=", d, selfvar); 
    BinaryExpression indom = new BinaryExpression(":", tracevar, traceent); 
    BinaryExpression res = new BinaryExpression("#", indom, eq); 
    return new UnaryExpression("not", res); 
  } 

  public static Expression deleteDomainExp(Attribute att)
  { BasicExpression arg = new BasicExpression(att); 
    arg.setData("self"); 
    return new UnaryExpression("->isDeleted", arg); 
  } 

}
