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

public class Pattern
{ Vector bindsTo = new Vector(); // of Attribute
  Vector predicate = new Vector(); // of Expression

  public Pattern() {}

  public Pattern(Vector preds)
  { predicate = preds; } 

  public Object clone()
  { Pattern res = new Pattern(); 
    res.bindsTo = bindsTo; 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression e = (Expression) predicate.get(i); 
      Expression e1 = (Expression) e.clone(); 
      res.addPredicate(e1); 
    } 
    return res; 
  } 

  public Pattern overrideBy(Pattern p)
  { Pattern res = new Pattern(); 
    res.bindsTo = VectorUtil.union(bindsTo,p.bindsTo); 
    // remove duplicates

    for (int i = 0; i < predicate.size(); i++) 
    { Expression e = (Expression) predicate.get(i); 
      Expression e1 = (Expression) e.clone(); 
      res.addPredicate(e1); 
    } 

    for (int i = 0; i < p.predicate.size(); i++) 
    { Expression e = (Expression) p.predicate.get(i); 
      Expression e1 = (Expression) e.clone(); 
      if (res.predicate.contains(e1)) { } 
      else 
      { res.addPredicate(e1); }  
    } 
    return res; 
  } 

  public void addBindsTo(Attribute b)
  { bindsTo.add(b); }

  public void addPredicate(Expression p)
  { if (p instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) p; 
      if ("&".equals(be.operator))
      { addPredicate(be.left); 
        addPredicate(be.right); 
      }
      else 
      { predicate.add(be); } 
    } 
    else 
    { predicate.add(p); } 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector context, Vector env)
  { boolean res = true; 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      res = p.typeCheck(types,entities,context,env); 
    } 
    return res; 
  }  

  public Vector rd()
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      Vector rd = p.readFrame();
      res = VectorUtil.union(res,rd);  
    } 
    return res; 
  }  // ignore v.f = e in where clause unless v is a target variable 

  public Vector allReadFrame()
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      Vector rd = p.allReadFrame();
      res = VectorUtil.union(res,rd);  
    } 
    return res; 
  }  

  public Vector wr(Vector assocs)
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      Vector v = p.wr(assocs);
      res = VectorUtil.union(res,v);  
    } 
    return res; 
  }  

  public String toString()
  { String res = ""; 
    for (int i = 0; i < predicate.size(); i++) 
    { res = res + predicate.get(i); 
      if (i < predicate.size() - 1)
      { res = res + " & "; } 
    } 
    return res; 
  }  

  public int complexity()
  { int res = 0; 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      res = res + p.syntacticComplexity(); 
    } 
    return res; 
  }  

  public int cyclomaticComplexity()
  { int res = 0; 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      res = res + p.cyclomaticComplexity(); 
    } 
    return res; 
  }  

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      res.addAll(p.allOperationsUsedIn()); 
    } 

    Vector cleanedops = new Vector(); 
    for (int j = 0; j < res.size(); j++) 
    { String str = res.get(j) + ""; 
      if (str.startsWith("null::"))
      { cleanedops.add(str.substring(6,str.length())); } 
      else 
      { cleanedops.add(str); } 
    } 
    return cleanedops; 
  }  

  public Vector variablesUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++) 
    { Expression p = (Expression) predicate.get(i); 
      res.addAll(p.getVariableUses()); 
    } 
    return res; 
  }  

  public int epl() { return 0; } 
  // but should count let and ->iterate variables 


  public Expression toGuardCondition(Vector bound, Expression contextObj, Expression post)
  { return null; }

  public Expression toSourceExpression(Vector bound, Expression contextObj)
  { return null; }

  public Expression toTargetExpression(Vector bound, Expression contextObj)
  { return null; }

  public Expression toSourceExpressionOp(Vector bound, Expression contextObj)
  { return null; }

  public Expression toTargetExpressionOp(Vector bound, Expression contextObj)
  { return null; }

  public Vector allRuleCalls(Vector rules)
  { Vector res = new Vector(); 
    for (int i = 0; i < predicate.size(); i++)
    { Expression conj = (Expression) predicate.get(i);
      Expression e = conj;
      if (conj != null && conj.isRuleCall(rules) && (conj instanceof BasicExpression))
      { BasicExpression be = (BasicExpression) conj; 
        res.add(be); 
      } 
      else if (conj != null && (conj instanceof BinaryExpression) && ((BinaryExpression) conj).isRuleCallDisjunction(rules))
      { Vector disjs = ((BinaryExpression) conj).allDisjuncts(); 
        res.addAll(disjs); 
      } 
      else if (conj != null && (conj instanceof BinaryExpression) && 
               ((BinaryExpression) conj).operator.equals("or"))
      { Vector disjs = ((BinaryExpression) conj).allDisjuncts();
        for (int j = 0; j < disjs.size(); j++) 
        { Expression dj = (Expression) disjs.get(j); 
          Pattern pj = new Pattern(); 
          pj.addPredicate(dj); 
          Vector pjcalls = pj.allRuleCalls(rules); 
          res = VectorUtil.union(res,pjcalls);
        } 
      } 
      else if (conj != null && (conj instanceof BinaryExpression) && 
               ((BinaryExpression) conj).operator.equals("!"))
      { BinaryExpression beforall = (BinaryExpression) conj; 
        Pattern qpatt = new Pattern(); 
        qpatt.addPredicate(beforall.getRight()); 
        Vector ncalls = qpatt.allRuleCalls(rules); 
        res.addAll(ncalls); 
      }
      else if (conj instanceof ConditionalExpression)
      { ConditionalExpression cexp = (ConditionalExpression) conj; 
        Expression ifpart = cexp.getIfExp(); 
        Expression elsepart = cexp.getElseExp(); 
        Pattern ifpatt = new Pattern(); 
        ifpatt.addPredicate(ifpart); 
        Pattern epatt = new Pattern(); 
        epatt.addPredicate(elsepart); 
        Vector q1 = ifpatt.allRuleCalls(rules);
        Vector q2 = epatt.allRuleCalls(rules);
        res.addAll(q1); 
        res.addAll(q2); 
      } 
    } 
    return res; 
  } // and negation, conditionals, ->forAll

  public Pattern expandWhenCalls(Vector oldrules, java.util.Map leafrules, java.util.Map guards)
  { Vector newpredicate = new Vector(); 

    for (int i = 0; i < predicate.size(); i++) 
    { Expression conj = (Expression) predicate.get(i); 

      if (conj != null && conj.isRuleCall(oldrules) && (conj instanceof BasicExpression))
      { BasicExpression be = (BasicExpression) conj; 
        Relation r = (Relation) be.getCalledRule(oldrules); 
        if (r != null) 
        { Vector leafs = (Vector) leafrules.get(r); 
          Expression res = new BasicExpression(false); 

          for (int k = 0; k < leafs.size(); k++) 
          { Relation rk = (Relation) leafs.get(k); 
            BasicExpression rkcall = new BasicExpression(rk.getName()); 
            rkcall.umlkind = Expression.UPDATEOP;
            rkcall.type = new Type("boolean",null);  
            rkcall.setParameters(be.getParameters()); 
            rkcall.elementType = new Type("boolean",null);  

            Vector gd = (Vector) guards.get(rk.getName()); 

            // System.out.println(">>> Guard of " + rk.getName() + " is " + gd); 

            if (gd != null && gd.size() > 0)
            { Expression gcall = Expression.simplifyAnd((Expression) gd.get(0),rkcall);
              gcall.setBrackets(true); 
              res = Expression.simplifyOr(res,gcall); 
            } 
            else 
            { res = Expression.simplifyOr(res,rkcall); }  
          } 
          res.setBrackets(true); 
          res.needsBracket = true; 
          newpredicate.add(res); 
        } 
        else 
        { newpredicate.add(conj); } 
      } 
      else 
      { newpredicate.add(conj); } 
    } 
    return new Pattern(newpredicate);       
  } 

  public Pattern expandWhereCalls(Vector oldrules, java.util.Map leafrules, java.util.Map guards)
  { Vector newpredicate = new Vector(); 

    for (int i = 0; i < predicate.size(); i++) 
    { Expression conj = (Expression) predicate.get(i); 

      if (conj != null && conj.isRuleCall(oldrules) && (conj instanceof BasicExpression))
      { BasicExpression be = (BasicExpression) conj; 
        Relation r = (Relation) be.getCalledRule(oldrules); 
        if (r != null) 
        { Vector leafs = (Vector) leafrules.get(r); 
          Expression res = new BasicExpression(true); 

          for (int k = 0; k < leafs.size(); k++) 
          { Relation rk = (Relation) leafs.get(k); 
            BasicExpression rkcall = new BasicExpression(rk.getName()); 
            rkcall.umlkind = Expression.UPDATEOP;
            rkcall.isEvent = true; 
            // rkcall.entity = uc.classifier; 
            rkcall.type = new Type("boolean",null);  
            Vector newpars = rk.retypeParameters(be.getParameters()); 
            rkcall.setParameters(newpars);
            // No - need to substitute and cast them possibly
 
            rkcall.elementType = new Type("boolean",null);  

            Vector gd = (Vector) guards.get(rk.getName()); 

            // System.out.println(">>> Guard of " + rk.getName() + " is " + gd); 

            if (gd != null && gd.size() > 0)
            { res = new ConditionalExpression((Expression) gd.get(0), rkcall, res); } 
            else 
            { res = rkcall; }  
          } 
          res.setBrackets(true); 
          res.needsBracket = true; 
          newpredicate.add(res); 
        } 
        else 
        { newpredicate.add(conj); } 
      } 
      else 
      { newpredicate.add(conj); } 
    } 
    return new Pattern(newpredicate);       
  } 

  public Vector whenToExp(Vector bound, Vector rules, Vector entities)
  { // R(s,t) is r$trace : s.traces$R$p1 & t = r$trace.p2
    // The s, t are bound at this point. 
    // An or of trace tests leads to multiple constraints, one for each disjunct. 

    Vector res = new Vector(); 

    Expression cond = new BasicExpression(true);
    res.add(cond); 

    for (int i = 0; i < predicate.size(); i++)
    { Expression conj = (Expression) predicate.get(i);
      Expression e = conj;
      if (conj != null && conj.isRuleCall(rules) && (conj instanceof BasicExpression))
      { BasicExpression be = (BasicExpression) conj; 
        e = be.testTraceRelationWhen(entities,bound); 

        // System.out.println("RULE TEST: " + e); 

        Vector pars = be.getParameters(); 
        for (int p = 0; p < pars.size(); p++) 
        { Expression par = (Expression) pars.get(p); 
          if (par instanceof BasicExpression) 
          { BasicExpression pbe = (BasicExpression) par; 
            if (bound.contains(pbe.data)) { } 
            else 
            { bound.add(pbe.data); } 
          } 
        } 
        
        Vector newres = new Vector(); 

        for (int j = 0; j < res.size(); j++) 
        { Expression ante = (Expression) res.get(j); 
          Expression newante = Expression.simplify("&", ante, e, null); 
          newres.add(newante); 
        } 
        res.clear(); 
        res.addAll(newres); 
      }   
      else if (conj != null && (conj instanceof BinaryExpression) && ((BinaryExpression) conj).isRuleCallDisjunction(rules))
      { Vector disjs = ((BinaryExpression) conj).allDisjuncts(); 
 
        Vector newres = new Vector(); 
                 
        for (int k = 0; k < disjs.size(); k++) 
        { BasicExpression disj = (BasicExpression) disjs.get(k); 
          Expression callk = disj.testTraceRelationWhen(entities,bound); 
          // System.out.println("*** All disjuncts = " + callk); 

          for (int j = 0; j < res.size(); j++) 
          { Expression ante = (Expression) res.get(j); 
            Expression newante = Expression.simplify("&", ante, callk, null); 
            newres.add(newante); 
          } 
        }
        res.clear(); 
        res.addAll(newres);  
      }  // also case of a negated call, in a when. 
      else if (conj != null && 
               (conj instanceof BinaryExpression) && ((BinaryExpression) conj).operator.equals("or"))
      { Vector disjs = ((BinaryExpression) conj).allDisjuncts(); 
 
        Vector newres = new Vector(); 
        Vector alldisjuncts = new Vector(); 
                 
        for (int k = 0; k < disjs.size(); k++) 
        { Expression disj = (Expression) disjs.get(k); 
          Pattern pd = new Pattern(); 
          pd.addPredicate(disj); 
          Vector pdcases = pd.whenToExp(bound,rules,entities); 
          alldisjuncts.addAll(pdcases); 

          for (int j = 0; j < res.size(); j++) 
          { Expression ante = (Expression) res.get(j); 
            for (int m = 0; m < alldisjuncts.size(); m++) 
            { Expression disjm = (Expression) alldisjuncts.get(m); 
              Expression newante = Expression.simplify("&", ante, disjm, null); 
              newres.add(newante);
            }  
          } 
        }
        res.clear(); 
        res.addAll(newres);  
      }  // also case of a negated call, in a when. 
      else 
      { Vector newres = new Vector(); 

        for (int j = 0; j < res.size(); j++) 
        { Expression ante = (Expression) res.get(j); 
          Expression newante = Expression.simplify("&", ante, e, null); 
          newres.add(newante); 
        } 
        res.clear(); 
        res.addAll(newres); 
      } 
    }

    // System.out.println("WHEN predicate for " + predicate + " IS " + res); 
    System.out.println(); 

    return res;
  }  // also add parameters of call to bound variables with predicate p:T if not in bound

  public Vector checkWhen(Vector bound, Vector rules, Vector entities)
  { // R(s,t) is r$trace : R$trace # s = r$trace.p1 & t = r$trace.p2
    // The s, t are bound at this point. 
    // An or of trace tests leads to multiple constraints, one for each disjunct. 

    Vector res = new Vector(); 

    Expression cond = new BasicExpression(true);
    res.add(cond); 

    for (int i = 0; i < predicate.size(); i++)
    { Expression conj = (Expression) predicate.get(i);
      Expression e = conj;
      if (conj != null && conj.isRuleCall(rules) && (conj instanceof BasicExpression))
      { BasicExpression be = (BasicExpression) conj; 
        e = be.checkWhen(entities,bound); 

        // System.out.println("RULE TEST: " + e); 

        Vector pars = be.getParameters(); 
        for (int p = 0; p < pars.size(); p++) 
        { Expression par = (Expression) pars.get(p); 
          if (par instanceof BasicExpression) 
          { BasicExpression pbe = (BasicExpression) par; 
            if (bound.contains(pbe.data)) { } 
            else 
            { bound.add(pbe.data); } 
          } 
        } 
        
        Vector newres = new Vector(); 

        for (int j = 0; j < res.size(); j++) 
        { Expression ante = (Expression) res.get(j); 
          Expression newante = Expression.simplify("&", ante, e, null); 
          newres.add(newante); 
        } 
        res.clear(); 
        res.addAll(newres); 
      }   
      else if (conj != null && (conj instanceof BinaryExpression) && ((BinaryExpression) conj).isRuleCallDisjunction(rules))
      { Vector disjs = ((BinaryExpression) conj).allDisjuncts(); 
 
        Vector newres = new Vector(); 
                 
        for (int k = 0; k < disjs.size(); k++) 
        { BasicExpression disj = (BasicExpression) disjs.get(k); 
          Expression callk = disj.checkWhen(entities,bound); 
          // System.out.println("*** All disjuncts = " + callk); 

          for (int j = 0; j < res.size(); j++) 
          { Expression ante = (Expression) res.get(j); 
            Expression newante = Expression.simplify("&", ante, callk, null); 
            newres.add(newante); 
          } 
        }
        res.clear(); 
        res.addAll(newres);  
      }  // also case of negated call
      else 
      { Vector newres = new Vector(); 

        for (int j = 0; j < res.size(); j++) 
        { Expression ante = (Expression) res.get(j); 
          Expression newante = Expression.simplify("&", ante, e, null); 
          newres.add(newante); 
        } 
        res.clear(); 
        res.addAll(newres); 
      } 
    }

    // System.out.println("WHEN predicate for " + predicate + " IS " + res); 
    System.out.println(); 

    return res;
  }  // also add parameters of call to bound variables with predicate p:T if not in bound

  public Expression whereToExp(String calltarget, UseCase uc, Vector bound, 
                               Vector targetvariables, Vector rules)
  { // invokes ops for non-top relations
    // Ignore predicates whose write frame does not involve target variables 

    Expression res = new BasicExpression(true);
    for (int i = 0; i < predicate.size(); i++)
    { Expression conj = (Expression) predicate.get(i);
      Expression e = conj;
      if (conj != null && conj.isRuleCall(rules) && 
          (conj instanceof BasicExpression))
      { e = ((BasicExpression) conj).invokeNontopRule(calltarget,uc,rules); }
      else if (conj instanceof ConditionalExpression)
      { ConditionalExpression cexp = (ConditionalExpression) conj; 
        Expression tst = cexp.getTest(); 
        Expression ifpart = cexp.getIfExp(); 
        Expression elsepart = cexp.getElseExp(); 
        Pattern ifpatt = new Pattern(); 
        ifpatt.addPredicate(ifpart); 
        Pattern epatt = new Pattern(); 
        epatt.addPredicate(elsepart); 
        Expression q1 = ifpatt.whereToExp(calltarget,uc,bound,targetvariables,rules);
        Expression q2 = epatt.whereToExp(calltarget,uc,bound,targetvariables,rules);

        e = new ConditionalExpression(tst, q1, q2);   
      } 
      else if (conj instanceof BinaryExpression) 
      { BinaryExpression be = (BinaryExpression) conj; 
        /* if ("<=>".equals(be.operator))
        { // only include the direction which updates the target domains
          Vector varsleft = be.left.variablesUsedIn(targetvariables); 
          Vector varsright = be.right.variablesUsedIn(targetvariables); 
          if (varsleft.size() == 0) 
          { e = new BinaryExpression("=>", be.left, be.right);
            e.setBrackets(true); 
          } 
          else if (varsright.size() == 0) 
          { e = new BinaryExpression("=>", be.right, be.left); 
            e.setBrackets(true); 
          } 
          else 
          { System.err.println("ERROR: cannot update target variables on both sides of <=>: " + be); } 
        }
        else */  
        if (be.operator.equals("!"))
        { Pattern qpatt = new Pattern(); 
          qpatt.addPredicate(be.getRight()); 
          Expression qbody = qpatt.whereToExp(calltarget,uc,bound,targetvariables,rules);
          e = new BinaryExpression("!", be.getLeft(), qbody);   
        } 
        else if ("=".equals(be.operator))
        { // Only include it if it updates a feature of a target variable
          if (be.left instanceof BasicExpression)
          { Vector vuses = ((BasicExpression) be.left).getAssignedVariableUses(); // just the updated variable. 

            System.out.println(">>>> Updated variables of assignment " + be + " LHS: " + vuses); 
            System.out.println(">>>> Only assignments to target variables: " + targetvariables + 
                               " are effective"); 

            e = null; 
            for (int k = 0; k < vuses.size(); k++) 
            { Expression use = (Expression) vuses.get(k); 
              if (targetvariables.contains(use + ""))
              { e = conj; 
                System.out.println(">>>> " + be + " is effective"); 
                break; 
              } 
            }
          } 
          else 
          { System.err.println("!!! Cannot assign to expression: " + be.left + 
                               " in " + be); 
          } 
        } 
        else 
        { e = conj; } 
      }  
          
      res = Expression.simplify("&", res, e, null);
    }
    return res;
  }

  // wr and rd frames
}
