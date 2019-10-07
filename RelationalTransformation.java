import java.util.Vector; 
import java.io.*;
import java.awt.print.*; 
import javax.swing.*;
import javax.swing.event.*;


/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public class RelationalTransformation extends NamedElement
{ Vector modelParameter = new Vector(); // of TypedModel
  Vector rule = new Vector(); // of QVTRule
  Vector functions = new Vector(); // of BehaviouralFeature


  public RelationalTransformation(String n)
  { super(n); }

  public int nops() 
  { return functions.size(); } 

  public int nrules()
  { return rule.size(); } 

  public void addParameter(TypedModel tm)
  { modelParameter.add(tm); }

  public void addRule(QVTRule r)
  { rule.add(r); }

  public void addFunction(BehaviouralFeature bf)
  { functions.add(bf); }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { for (int i = 0; i < functions.size(); i++)
    { BehaviouralFeature bf = (BehaviouralFeature) functions.get(i);
      bf.typeCheck(types,entities); 
    }

    for (int i = 0; i < rule.size(); i++)
    { QVTRule r = (QVTRule) rule.get(i);
      r.typeCheck(types,entities,contexts,env); 
    } 
    return true; 
  } 

  public boolean checkOrdering() 
  { boolean res = true; 
    for (int i = 0; i < rule.size(); i++)
    { Relation r = (Relation) rule.get(i);

      System.out.println(); 

      /* Ordering constraint 1: Ri called in Rj.when => i < j */ 
      if (r.isTopLevel())
      { Vector rwhencalls = r.whenCalls(rule);
        for (int k = 0; k < rwhencalls.size(); k++) 
        { BasicExpression wcall = (BasicExpression) rwhencalls.get(k); 
          int j = NamedElement.indexByName(wcall.data, rule); 
          if (j < i) { } 
          else 
          { System.err.println("!! Dependency error 1: rule " + wcall + 
                               " does not precede " + r.getName()); 
            JOptionPane.showMessageDialog(null,"!! Dependency error 1: rule " + wcall + 
                               " does not precede " + r.getName()); 
            res = false; 
          } 
        }   
      } 
    }

    /* Ordering constraint 2: if S called in Ri.where* and Rm called in S.when*, 
       then m < i */ 
    for (int i = 0; i < rule.size(); i++)
    { Relation ri = (Relation) rule.get(i);

      System.out.println(); 

      Vector rwcalls = ri.recursiveWhereCalls(rule,new Vector()); 
      System.out.println(">>> Recursive where calls of " + ri.getName() + " are: " + rwcalls);

      for (int j = 0; j < rwcalls.size(); j++) 
      { BasicExpression s = (BasicExpression) rwcalls.get(j); 
        
        Relation rs = (Relation) NamedElement.findByName(s.data,rule); 
        if (rs != null) 
        { Vector swcalls = rs.recursiveWhenCalls(rule,new Vector()); 
          System.out.println(">>> Recursive when calls of " + rs.getName() + " are: " + swcalls); 
          for (int k = 0; k < swcalls.size(); k++) 
          { BasicExpression wcall = (BasicExpression) swcalls.get(k); 
            int m = NamedElement.indexByName(wcall.data, rule); 
            if (m < i) { } 
            else 
            { System.err.println("!! Dependency error 2: rule " + wcall + 
                               " does not precede " + ri.getName()); 
              JOptionPane.showMessageDialog(null,"!! Dependency error 2: rule " + wcall + 
                               " does not precede " + ri.getName()); 
              res = false;
            }  
          } 
        } 
        else 
        { System.out.println(">>> Rule " + s.data + " not found in transformation!"); }    
      } 
    }

    /* Ordering constraint 3: if S called in Ri.where* and S called in Rj.when* then 
       i < j */ 
    for (int i = 0; i < rule.size(); i++)
    { Relation ri = (Relation) rule.get(i);

      System.out.println(); 

      Vector riwherecalls = ri.recursiveWhereCalls(rule,new Vector()); 

      for (int j = 0; j < rule.size(); j++) 
      { Relation rj = (Relation) rule.get(j); 
        
        Vector rjwhencalls = rj.recursiveWhenCalls(rule,new Vector()); 
        System.out.println(">>> Recursive when calls of " + rj.getName() + " are: " + rjwhencalls); 

        for (int k = 0; k < riwherecalls.size(); k++) 
        { BasicExpression wcall = (BasicExpression) riwherecalls.get(k); 
          // if wcall in rjwhencalls

          Expression me = ModelElement.lookupExpressionByData(wcall.data, rjwhencalls); 
          if (me != null) 
          { if (i < j) { } 
            else 
            { System.err.println("!! Dependency error 3: rule " + ri.getName() + 
                               " does not precede " + rj.getName()); 
              JOptionPane.showMessageDialog(null,"!! Dependency error 3: rule " + ri.getName() + 
                               " does not precede " + rj.getName()); 
              res = false;
            }  
          } 
        }   
      } 
    }

    return res; 
  } 

  public RelationalTransformation expandOverrides(Vector entities) 
  { Vector newrelations = new Vector(); 
    Vector abstractrelations = new Vector(); 
    java.util.Map leafrels = new java.util.HashMap(); 
    java.util.Map guardconditions = new java.util.HashMap(); 

    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i); 
      if (r instanceof Relation)
      { Relation rr = (Relation) r; 
        if (rr.isConcrete() && rr.overrides())
        { Relation newr = rr.flattenedRelation(rule); 
          System.out.println("******** New relation: "); 
          System.out.println(newr); 
          if (newr != null) 
          { newrelations.add(newr); 
            Vector gc = newr.toGuardCondition(new Vector(),entities);
            System.out.println(">>> Guard condition of " + newr.getName() + " is: " + gc);  
            guardconditions.put(newr.getName(),gc); 
          }  
        } 
        else if (rr.isConcrete()) 
        { newrelations.add(rr);             
          Vector gc = rr.toGuardCondition(new Vector(),entities);
          System.out.println(">>> Guard condition of " + rr.getName() + " is: " + gc);  
          guardconditions.put(rr.getName(),gc); 
        }
        else 
        { Vector leafs = rr.allLeafRelations(rule); 
          abstractrelations.add(rr); 
          leafrels.put(rr,leafs); 
          System.out.println(">>> All leaf relations of " + rr.getName() + " are: " + leafs); 
        }  
      }  
    } 

    for (int j = 0; j < newrelations.size(); j++) 
    { Relation newr = (Relation) newrelations.get(j); 
      newr.expandWhenCalls(abstractrelations,leafrels,guardconditions); 
      newr.expandWhereCalls(abstractrelations,leafrels,guardconditions); 
    } 

    RelationalTransformation newtrans = new RelationalTransformation(name); 
    newtrans.functions = functions; 
    newtrans.modelParameter = modelParameter; 
    newtrans.rule = newrelations; 
    return newtrans; 
  } 

        

  public void addTraceEntities(Vector entities, Vector assocs)
  { for (int i = 0; i < rule.size(); i++)
    { QVTRule r = (QVTRule) rule.get(i);
      r.addTraceEntity(entities,assocs); 
    } 
  } // for all rules, not only top ones. 

  public Vector toUseCase(Vector entities, Vector types)
  { String nme = getName(); 
    UseCase pres = new UseCase(nme + "$Pres", null); 
    UseCase res = new UseCase(nme + "$Con",null);
    UseCase cleanup = new UseCase(nme + "$Cleanup",null);

    Vector ucs = new Vector(); 
    ucs.add(pres); 
    ucs.add(res); 
    ucs.add(cleanup); 

    // The functions are added to pres and res
 
    for (int i = 0; i < functions.size(); i++)
    { BehaviouralFeature bf = (BehaviouralFeature) functions.get(i);
      res.addOperation(bf);  
    } 

    for (int i = 0; i < rule.size(); i++)
    { Relation r = (Relation) rule.get(i);
      r.getNontopOp(pres,res,entities,types); 
      // Vector bnd = new Vector(); 
      // Vector guard = r.toGuardCondition(bnd,entities); 
      // System.out.println("*** Guard condition of " + r.getName() + " is " + guard); 
    } // only needed if there is a where that calls it

    Vector ruleclones = new Vector(); 

    for (int i = 0; i < rule.size(); i++)
    { Relation r = (Relation) rule.get(i);
      Relation r1 = (Relation) r.clone(); 
      ruleclones.add(r1); 
    } 

    for (int i = 0; i < rule.size(); i++)
    { QVTRule r = (QVTRule) rule.get(i);
      // Constraint c = r.toInverseConstraint(entities,types);
      // if (c != null)
      // { res.addPostcondition(c); }
      pres.addPostconditions(r.toPreservationConstraints(pres,entities,types)); 
    }
      
    for (int i = 0; i < ruleclones.size(); i++)
    { QVTRule r1 = (QVTRule) ruleclones.get(i); 
      Vector c = r1.toConstraints(res,entities,types);
      if (c != null)
      { res.addPostconditions(c); }
    }

    Vector tents = new Vector(); 
    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i); 
      tents = VectorUtil.union(tents, r.targetEntities()); 
    } 

    for (int i = 0; i < tents.size(); i++)
    { Entity e = (Entity) tents.get(i);
      Constraint cc = toCleanupConstraint(e,entities); 
      if (cc != null) 
      { cleanup.addPostcondition(cc); }  
    }
    
    return ucs;
  }

  public Constraint toCleanupConstraint(Entity e, Vector entities)
  { // not(R$trace->exists( tr ! tr.var = self )) and ... and not(...) => self->isDeleted()
    // for each R with target attribute  var : e

    String ename = e.getName(); 
    Expression ante = new BasicExpression(true); 
    Vector allArgs = new Vector(); 

    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i); 

      if (r.isAbstract()) { continue; } 

      Entity trent = (Entity) ModelElement.lookupByName(r.getName() + "$trace", entities); 
      if (trent == null) 
      { return null; } 

      Vector targs = r.targetAttributes(); 
      for (int j = 0; j < targs.size(); j++) 
      { Attribute tatt = (Attribute) targs.get(j); 
        if ((tatt.getType() + "").equals(ename))
        { Expression checktr = r.checkTraceEmpty(trent,tatt); 
          // UnaryExpression anter = new UnaryExpression("not", checktr); 
          ante = Expression.simplifyAnd(ante,checktr); 
          allArgs.add(tatt); 
        } 
      } 
    } 

    if (allArgs.size() == 0) { return null; } 

    Attribute att = (Attribute) allArgs.get(0); 
        
    Expression delrd = Relation.deleteDomainExp(att); 
    Constraint cc = new Constraint(ante,delrd);
    cc.setOwner(e); 
    return cc; 
  }
       
  public String toString()
  { String res = "transformation " + getName() + "\n" + 
      "{ "; 
    
    for (int i = 0; i < functions.size(); i++) 
    { BehaviouralFeature f = (BehaviouralFeature) functions.get(i);      
      res = res + "query " + f.getSignature() + " : " + f.getResultType() + "\n" + "{ " + f.getPost() + " }\n\n"; 
    }

    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i);      
      res = res + r + "\n\n"; 
    } 
    return res + "}"; 
  } 

  public int complexity(PrintWriter out)
  { int res = 0; 
    for (int i = 0; i < functions.size(); i++) 
    { BehaviouralFeature f = (BehaviouralFeature) functions.get(i);      
      int fc = f.syntacticComplexity(); 
      out.println("**** operation " + f.getName() + " has complexity " + fc); 
      if (fc > 100) 
      { out.println("**** EHS flaw, excessively large helper"); }  
      res = res + fc; 
    } 

    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i);      
      int rc = r.complexity(); 
      out.println("**** rule " + r.getName() + " has complexity " + rc); 
      if (rc > 100) 
      { out.println("**** ERS flaw, excessively large rule"); }  

      res = res + rc; 
    } 

    return res; 
  } 

  public int cyclomaticComplexity()
  { int res = 0; 
    for (int i = 0; i < functions.size(); i++) 
    { BehaviouralFeature f = (BehaviouralFeature) functions.get(i);      
      if (f.cyclomaticComplexity() > 10) { res++; }  
    } 

    for (int i = 0; i < rule.size(); i++) 
    { QVTRule r = (QVTRule) rule.get(i);      
      if (r.cyclomaticComplexity() > 10) { res++; }  
    } 

    return res; 
  } 

  public int enr() { return rule.size(); } 

  public int eno() { return functions.size(); } 

  public int uex()
  { int res = 0; 
    for (int i = 0; i < rule.size(); i++) 
    { Relation r = (Relation) rule.get(i);      
      if (r.isTopLevel) 
      { res++; }  
    } 
    return (res*(res-1))/2; 
  } 

  public int epl(PrintWriter out)
  { int res = 0; 
    for (int i = 0; i < rule.size(); i++) 
    { Relation r = (Relation) rule.get(i);      
      int p = r.epl(); 
      if (p > 10) 
      { out.println("*** EPL flaw: " + p + " parameters for rule " + r.getName()); 
        res++; 
      }  
    } 
    return res; 
  }

  public Map getCallGraph()
  { Map res = new Map(); 
    int efo = 0; 

    // operations, pre and post also
    for (int i = 0; i < functions.size(); i++) 
    { BehaviouralFeature rr = (BehaviouralFeature) functions.get(i); 
      rr.getCallGraph(res); 
    } 

    for (int i = 0; i < rule.size(); i++) 
    { Relation r = (Relation) rule.get(i);      
      r.getCallGraph(res);  
    } 

    return res; 
  }  

}
