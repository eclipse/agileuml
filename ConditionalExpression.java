/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: OCL */ 

import java.util.Vector;
import java.io.*;  


public class ConditionalExpression extends Expression
{ Expression test;
  Expression ifExp;
  Expression elseExp;

  public ConditionalExpression(Expression t, Expression ie, Expression ee)
  { test = t; ifExp = ie; elseExp = ee; }

  /* public Object clone()
  { ConditionalExpression clne = new ConditionalExpression(test, ifExp, elseExp); 
    clne.setType(type); 
    clne.setElementType(elementType); 
    return clne;  
  } */ 

  public void setTest(Expression teste)
  { test = teste; } 

  public void setIf(Expression ife)
  { ifExp = ife; } 

  public void setElse(Expression elsee)
  { elseExp = elsee; } 

  public Expression getTest()
  { return test; } 

  public Expression getIfExp()
  { return ifExp; } 

  public Expression getElseExp()
  { return elseExp; } 

  public Expression getIf()
  { return ifExp; } 

  public Expression getElse()
  { return elseExp; } 

  public Vector getParameters() 
  { return new Vector(); } 

  public String queryForm(java.util.Map env, boolean local)
  { String tqf = test.queryForm(env, local);
    String lqf = ifExp.queryForm(env, local);
    String rqf = elseExp.queryForm(env, local);
    return "((" + tqf + ") ? (" + lqf + ") : (" + rqf + "))";
  }
    
  public String queryFormJava6(java.util.Map env, boolean local)
  { String tqf = test.queryFormJava6(env, local);
    String lqf = ifExp.queryFormJava6(env, local);
    String rqf = elseExp.queryFormJava6(env, local);
    return "((" + tqf + ") ? (" + lqf + ") : (" + rqf + "))";
  }

  public String queryFormJava7(java.util.Map env, boolean local)
  { String tqf = test.queryFormJava7(env, local);
    String lqf = ifExp.queryFormJava7(env, local);
    String rqf = elseExp.queryFormJava7(env, local);
    return "((" + tqf + ") ? (" + lqf + ") : (" + rqf + "))";
  }

  public String queryFormCSharp(java.util.Map env, boolean local)
  { String tqf = test.queryFormCSharp(env, local);
    String lqf = ifExp.queryFormCSharp(env, local);
    String rqf = elseExp.queryFormCSharp(env, local);
    return "((" + tqf + ") ? (" + lqf + ") : (" + rqf + "))";
  }

  public String queryFormCPP(java.util.Map env, boolean local)
  { String tqf = test.queryFormCPP(env, local);
    String lqf = ifExp.queryFormCPP(env, local);
    String rqf = elseExp.queryFormCPP(env, local);
    return "((" + tqf + ") ? (" + lqf + ") : (" + rqf + "))";
  }

  public String updateForm(java.util.Map env, boolean local)
  { String tqf = test.queryForm(env, local);
    String lqf = ifExp.updateForm(env, local);
    if ("true".equals(test + ""))
    { return lqf; }
	
    String rqf = elseExp.updateForm(env, local);
    return "    if (" + tqf + ") { " + lqf + " }\n" +
           "    else { " + rqf + " }";
  }

  public String updateFormJava6(java.util.Map env, boolean local)
  { String tqf = test.queryFormJava6(env, local);
    String lqf = ifExp.updateFormJava6(env, local);
    if ("true".equals(test + ""))
    { return lqf; }

    String rqf = elseExp.updateFormJava6(env, local);
    return "  if (" + tqf + ") { " + lqf + " }\n" +
           "  else { " + rqf + " }";
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { String tqf = test.queryFormJava7(env, local);
    String lqf = ifExp.updateFormJava7(env, local);
    if ("true".equals(test + ""))
    { return lqf; }
    
    String rqf = elseExp.updateFormJava7(env, local);
    return "  if (" + tqf + ") { " + lqf + " }\n" +
           "  else { " + rqf + " }";
  }

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String tqf = test.queryFormCSharp(env, local);
    String lqf = ifExp.updateFormCSharp(env, local);
    String rqf = elseExp.updateFormCSharp(env, local);
    return "  if (" + tqf + ") { " + lqf + " }\n" +
           "  else { " + rqf + " }";
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { String tqf = test.queryFormCPP(env, local);
    String lqf = ifExp.updateFormCPP(env, local);
    String rqf = elseExp.updateFormCPP(env, local);
    return "  if (" + tqf + ") { " + lqf + " }\n" +
           "  else { " + rqf + " }";
  }

  public Statement generateDesign(java.util.Map env, boolean local)
  { Statement ifstat = ifExp.generateDesign(env,local); 
    Statement elsestat = elseExp.generateDesign(env,local); 
    return new ConditionalStatement(test, ifstat, elsestat); 
  } 

  public int syntacticComplexity()
  { int res = test.syntacticComplexity(); 
    res = res + ifExp.syntacticComplexity(); 
    res = res + elseExp.syntacticComplexity(); 
    return res + 1; 
  } 

public void findClones(java.util.Map clones, String rule, String op)
{ if (this.syntacticComplexity() < UCDArea.CLONE_LIMIT) 
  { return; }
  String val = this + ""; 
  Vector used = (Vector) clones.get(val);
  if (used == null)
  { used = new Vector(); }
  if (rule != null)
  { used.add(rule); }
  else if (op != null)
  { used.add(op); }
  clones.put(val,used);
  test.findClones(clones,rule,op); 
  ifExp.findClones(clones,rule,op);
  elseExp.findClones(clones,rule,op);
}

public void findClones(java.util.Map clones,
                       java.util.Map cloneDefs,
                       String rule, String op)
{ if (this.syntacticComplexity() < UCDArea.CLONE_LIMIT) 
  { return; }
  String val = this + ""; 
  Vector used = (Vector) clones.get(val);
  if (used == null)
  { used = new Vector(); }
  if (rule != null)
  { used.add(rule); }
  else if (op != null)
  { used.add(op); }
  clones.put(val,used);
  cloneDefs.put(val, this); 
  test.findClones(clones,cloneDefs, rule,op); 
  ifExp.findClones(clones,cloneDefs, rule,op);
  elseExp.findClones(clones,cloneDefs, rule,op);
}

  public void findMagicNumbers(java.util.Map mgns, String rule, String op)
  { test.findMagicNumbers(mgns,rule,op);
    ifExp.findMagicNumbers(mgns,rule,op);
    elseExp.findMagicNumbers(mgns,rule,op);
  } 

public Vector mutants()
{ Vector res = new Vector(); 
  res.add(this); 
  ConditionalExpression mutant = (ConditionalExpression) clone(); 
  mutant.ifExp = elseExp; 
  mutant.elseExp = ifExp; 
  res.add(mutant); 
  return res; 
} // Together with mutants of if and else. 

public Vector singleMutants()
{ Vector res = new Vector(); 
  // res.add(this); 
  ConditionalExpression mutant = (ConditionalExpression) clone(); 
  mutant.ifExp = elseExp; 
  mutant.elseExp = ifExp; 
  res.add(mutant); 
  Vector ifMutants = ifExp.singleMutants(); 
  Vector elseMutants = elseExp.singleMutants(); 

  for (int i = 0; i < ifMutants.size(); i++) 
  { Expression mif = (Expression) ifMutants.get(i); 
    ConditionalExpression mut = (ConditionalExpression) clone(); 
    mut.ifExp = mif; 
    mut.elseExp = elseExp; 
    res.add(mut); 
  } 

  for (int i = 0; i < elseMutants.size(); i++) 
  { Expression melse = (Expression) elseMutants.get(i); 
    ConditionalExpression mut = (ConditionalExpression) clone(); 
    mut.ifExp = ifExp; 
    mut.elseExp = melse; 
    res.add(mut); 
  } 

  return res; 
}

  public int cyclomaticComplexity()
  { // 1 + p branches from test + p1 from if + p2 from else

    int tc = test.cyclomaticComplexity(); 
    int res = ifExp.cyclomaticComplexity(); 
    res = res + tc + 1 + elseExp.cyclomaticComplexity(); 
    return res; 
  } 

  public void changedEntityName(String oldN, String newN)
  { test.changedEntityName(oldN,newN); 
    ifExp.changedEntityName(oldN,newN); 
    elseExp.changedEntityName(oldN,newN); 
  } 

  public Expression addPreForms(String v)
  { Expression t1 = test.addPreForms(v); 
    Expression l1 = ifExp.addPreForms(v); 
    Expression r1 = elseExp.addPreForms(v); 
    Expression res = new ConditionalExpression(t1,l1,r1); 
    res.needsBracket = needsBracket; 
    return res; 
  } 

  public Expression removePrestate()
  { Expression t1 = test.removePrestate(); 
    Expression l1 = ifExp.removePrestate(); 
    Expression r1 = elseExp.removePrestate(); 
    Expression res = new ConditionalExpression(t1,l1,r1); 
    res.needsBracket = needsBracket; 
    res.type = type; 
    res.elementType = elementType; 
    return res; 
  } 

  public Vector allPreTerms()
  { Vector res = test.allPreTerms(); 
    Vector lpterms = ifExp.allPreTerms(); 
    Vector rpterms = elseExp.allPreTerms(); 
    res = VectorUtil.union(res,lpterms); 
    return VectorUtil.union(res,rpterms); 
  } 

  public Vector allPreTerms(String v)
  { Vector res = test.allPreTerms(v); 
    Vector lpterms = ifExp.allPreTerms(v); 
    Vector rpterms = elseExp.allPreTerms(v); 
    res = VectorUtil.union(res,lpterms); 
    return VectorUtil.union(res,rpterms); 
  } 

  public Vector innermostEntities()
  { Vector res = test.innermostEntities(); 
    Vector r1 = ifExp.innermostEntities(); 
    res = VectorUtil.union(res,r1); 
    return VectorUtil.union(res,elseExp.innermostEntities()); 
  } 

  public Vector innermostVariables()
  { Vector res = test.innermostVariables(); 
    Vector r1 = ifExp.innermostVariables();
    res = VectorUtil.union(res,r1);  
    return VectorUtil.union(res,elseExp.innermostVariables()); 
  } 

  public Vector allEntitiesUsedIn()
  { Vector res = test.allEntitiesUsedIn(); 
    Vector r1 = ifExp.allEntitiesUsedIn(); 
    res = VectorUtil.union(res,r1); 
    return VectorUtil.union(res,elseExp.allEntitiesUsedIn()); 
  } 

  public Vector allAttributesUsedIn()
  { Vector res = test.allAttributesUsedIn(); 
    Vector r1 = ifExp.allAttributesUsedIn(); 
    res = VectorUtil.union(res,r1); 
    return VectorUtil.union(res,elseExp.allAttributesUsedIn()); 
  } 

  public Expression featureSetting(String var, String feat, Vector remainder)
  { // Treat as (test => ifExp) & (not(test) => elseExp)
    return null; 
  } 

  public Expression featureSetting2(String var, String feat, Vector remainder)
  { // can't return anything because two cases
    return null; 
  } 

  public Vector computeNegation4ante(final Vector sms)
  { // not(if E then E1 else E2 endif) is 
    // E & not(E1), not(E) & not(E2)

    Vector res = new Vector();
    res.add(new BinaryExpression("&",test,new UnaryExpression("not", ifExp)));
    res.add(new BinaryExpression("&",new UnaryExpression("not", test), 
                                     new UnaryExpression("not", elseExp))); 
    return res;
  }

  public Expression computeNegation4succ(final Vector sms)
  { return new ConditionalExpression(test, new UnaryExpression("not", ifExp), 
                                           new UnaryExpression("not", elseExp)); 
  }
  
  public boolean conflictsWith(Expression e)
  { if (ifExp.conflictsWith(e) && elseExp.conflictsWith(e))
    { return true; } 
    return false; 
  } // and other cases

  public boolean subformulaOf(final Expression e)
  { if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e;
      if (be.operator.equals("&"))
      { return (subformulaOf(be.left) || subformulaOf(be.right)); }
      else if (be.operator.equals("#") || be.operator.equals("->exists") ||
              be.operator.equals("#1") || be.operator.equals("->exists1") ||
              be.operator.equals("->forAll") || be.operator.equals("!"))
      { return subformulaOf(be.right); }  // and if be.operator.equals("=>") ???
      else { return false; }
    }
    else 
    { return false; }
  }

  public boolean selfConsistent(final Vector vars) 
  { boolean c1 = ifExp.selfConsistent(vars); 
    boolean c2 = elseExp.selfConsistent(vars); 
    if (c1 && c2) { return true; } 
    return false;  
  } 

  public boolean consistentWith(Expression e)
  { if (equals(e))  { return true; }
    return (ifExp.consistentWith(e) ||
            elseExp.consistentWith(e)); 
  } 

  public boolean implies(final Expression e)
  { if (equals(e)) { return true; }
    if (e.equalsString("true")) { return true; } 
    boolean r1 = ifExp.implies(e); 
    boolean r2 = elseExp.implies(e); 
    return r1 && r2; 
  } 

  public Expression removeExpression(final Expression e)
  { /* Assumes e is of form  v = x */
    if (e == null)
    { return this; }
    if (("" + this).equals("" + e))
    { return null; } 
    Expression ifr = ifExp.removeExpression(e);
    Expression elser = elseExp.removeExpression(e); 
    return new ConditionalExpression(test,ifr,elser); 
  } 

  public Expression expandMultiples(Vector sms)
  { Expression newleft =
        ifExp.expandMultiples(sms);
    Expression newright =
        elseExp.expandMultiples(sms);
    return new ConditionalExpression(test,newleft,newright);
  }

  public Vector splitOr(final Vector components)
  { Vector res = new Vector();
    Vector lefts = ifExp.splitOr(components);
    Vector rights = elseExp.splitOr(components);
    for (int i = 0; i < lefts.size(); i++)
    { Expression e1 = (Expression) lefts.get(i);
      Expression e = new BinaryExpression("&",test,e1);
      res.add(e); 
    }
    for (int i = 0; i < rights.size(); i++)
    { Expression e2 = (Expression) rights.get(i);
      Expression e = new BinaryExpression("&",new UnaryExpression("not",test),e2);
      res.add(e); 
    }
    return res; 
  } 
 
  public Vector splitAnd(Vector sms) 
  { Vector res = new Vector(); 
    res.add(this); 
    return res;  
  } 

  public Object clone()
  { ConditionalExpression be = new ConditionalExpression(test,
                          (Expression) ifExp.clone(),
                          (Expression) elseExp.clone()); 
    be.type = type;
    be.entity = entity;
    be.elementType = elementType;
    be.multiplicity = multiplicity;
    be.umlkind = umlkind; 
    be.setBrackets(needsBracket); 
    be.formalParameter = formalParameter; 
    return be; 
  } // is this sufficient? 

  public Expression filter(final Vector vars)
  { Expression e1 = ifExp.filter(vars);
    Expression e2 = elseExp.filter(vars);
    return new ConditionalExpression(test,e1,e2); 
  }

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    Vector eargs = new Vector(); 

    if ("true".equals(test + ""))
    { return ifExp.cg(cgs); }
    else if ("false".equals(test + ""))
    { return elseExp.cg(cgs); }
	
    args.add(test.cg(cgs));
    args.add(ifExp.cg(cgs));
    args.add(elseExp.cg(cgs));
    eargs.add(test);
    eargs.add(ifExp);
    eargs.add(elseExp);

    CGRule r = 
      cgs.matchedConditionalExpressionRule(this,etext);

    System.out.println(">>> Matched conditional expression rule " + r); 

    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext;
  }

  public Vector metavariables()
  { Vector res = test.metavariables(); 
    res = VectorUtil.union(res,ifExp.metavariables()); 
    res = VectorUtil.union(res,elseExp.metavariables()); 
    return res; 
  } 

  public Expression simplify() 
  { Expression lsimp = ifExp.simplify();
    Expression rsimp = elseExp.simplify();
    if ("true".equals("" + test)) { return lsimp; } 
    if ("false".equals("" + test)) { return rsimp; } 
    if (("" + lsimp).equals("" + rsimp)) { return lsimp; } 

    return new ConditionalExpression(test,lsimp,rsimp); 
  }
 
  public Expression simplify(final Vector vars) 
  { Expression lsimp = ifExp.simplify(vars);
    Expression rsimp = elseExp.simplify(vars);
    if ("true".equals("" + test)) { return lsimp; } 
    if ("false".equals("" + test)) { return rsimp; } 
    if (("" + lsimp).equals("" + rsimp)) { return lsimp; } 

    return new ConditionalExpression(test,lsimp,rsimp); 
  }

  public Maplet findSubexp(final String var)
  { return new Maplet(null,this); } 

  public Vector componentsUsedIn(final Vector sms)
  { Vector res1 = ifExp.componentsUsedIn(sms);
    Vector res2 = elseExp.componentsUsedIn(sms);
    return VectorUtil.union(res1,res2);
  }

  public Vector variablesUsedIn(final Vector vars)
  { Vector res = test.variablesUsedIn(vars); 
    Vector res1 = ifExp.variablesUsedIn(vars);
    Vector res2 = elseExp.variablesUsedIn(vars);
    Vector resres = VectorUtil.vector_merge(res,res1);
    return VectorUtil.vector_merge(resres,res2);  
  } // and the test? 

  public boolean hasVariable(final String s)
  { return (test.hasVariable(s) || ifExp.hasVariable(s) || elseExp.hasVariable(s)); }
  // and the test

  public Expression substituteEq(final String oldVar, 
                                 final Expression newVal)
  { Expression newLeft = null;
    if (ifExp != null)
    { newLeft = ifExp.substituteEq(oldVar,newVal); }
    Expression newRight = null;
    if (elseExp != null)
    { newRight = elseExp.substituteEq(oldVar,newVal); }
    Expression newTest = test.substituteEq(oldVar,newVal); 
    Expression result = new ConditionalExpression(newTest,newLeft,newRight);
    if (needsBracket) 
    { result.setBrackets(true); } 
    return result; 
  } 

  public Expression removeSlicedParameters(
             BehaviouralFeature op, Vector fpars)
  { Expression newLeft = null;
    if (ifExp != null)
    { newLeft = ifExp.removeSlicedParameters(op,fpars); }
    Expression newRight = null;
    if (elseExp != null)
    { newRight = elseExp.removeSlicedParameters(op,fpars); }
    Expression newTest = test.removeSlicedParameters(op,fpars); 
    Expression result = 
       new ConditionalExpression(newTest,newLeft,newRight);
    if (needsBracket) 
    { result.setBrackets(true); } 
    return result; 
  } 

  public Expression substitute(final Expression oldE,
                               final Expression newE)
  { Expression newR = ifExp.substitute(oldE,newE);
    Expression newL = elseExp.substitute(oldE,newE);   
    Expression newT = test.substitute(oldE,newE); 
    Expression result = new ConditionalExpression(newT,newL,newR); 
    result.setBrackets(needsBracket); 
    return result; 
  } 

  public Expression buildJavaForm(final Vector comps)
  { return null; } 

  public String toJavaImp(Vector components)
  { String basicString = "(" + test.toJavaImp(components) + ")?(" + 
              ifExp.toJavaImp(components) + "):(" + 
              elseExp.toJavaImp(components) + ")";
    if (needsBracket)
    { return "(" + basicString + ")"; }  // eg, for or inside & 
    else
    { return basicString; }
  }

  public String toImp(final Vector components) 
  {
    return "(" + test.toImp(components) + ")?(" + 
                 ifExp.toImp(components) + "):(" + 
                 elseExp.toImp(components) + ")"; 
  }

  public Expression toSmv(Vector cnames)
  { Expression tsmv = test.toSmv(cnames); 
    BinaryExpression case1 = new BinaryExpression("->", tsmv, ifExp.toSmv(cnames)); 
    BinaryExpression case2 = 
      new BinaryExpression("->", new UnaryExpression("not", tsmv), elseExp.toSmv(cnames));
    case1.setBrackets(true); 
    case2.setBrackets(true); 
    return new BinaryExpression("&", case1, case2);  
  } // but actually written as  test ? if : else in SMV

  public String toZ3()
  { String tz3 = test.toZ3(); 
    return "(ite " + tz3 + " " + ifExp.toZ3() + " " + elseExp.toZ3() + ")"; 
  } 

  public String toB()
  { String tsmv = test.toB(); 
    return "((" + tsmv + " => " + ifExp.toB() + ") & (" + 
             "not(" + tsmv + ") => " + elseExp.toB() + "))";
  } 

  public String toJava()
  { String basicString = "(" + test.toJava() + ")?(" + 
              ifExp.toJava() + "):(" + 
              elseExp.toJava() + ")";
    if (needsBracket)
    { return "(" + basicString + ")"; }  // eg, for or inside & 
    else
    { return basicString; }
  }

  public String toSQL()
  { String ts = test.toSQL(); 
    String ls = ifExp.toSQL();
    String rs = elseExp.toSQL();
    String res = "((" + ts + " AND " + ls + ") OR (NOT(" + ts + ") AND " + rs + "))"; 
    return res;
  }

  public String toString()
  { String res = "if " + test + " then " + ifExp + " else " + elseExp + " endif"; 
    if (needsBracket) 
    { res = "(" + res + ")"; } 
    return res; 
  } 

  public String toAST()
  { String res = "(OclConditionalExpression if " + test.toAST() + " then " + ifExp.toAST() + " else " + elseExp.toAST() + " endif )"; 
    // if (needsBracket) 
    // { res = "(BracketedExpression ( " + res + " ) )"; } 
    return res; 
  } 

  public BExpression binvariantForm(java.util.Map env, boolean local)
  { BExpression tsmv = test.binvariantForm(env,local); 
    BExpression case1 = new BBinaryExpression("=>", tsmv, ifExp.binvariantForm(env,local)); 
    BExpression case2 = new BBinaryExpression("=>", new BUnaryExpression("not", tsmv), 
                   elseExp.binvariantForm(env,local)); 
    case1.setBrackets(true); 
    case2.setBrackets(true); 
    return new BBinaryExpression("&", case1, case2);
  } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression tsmv = test.binvariantForm(env,local); 
    return new BIfStatement(tsmv, ifExp.bupdateForm(env,local),
                   elseExp.bupdateForm(env,local));
  } 

  public BExpression bqueryForm(java.util.Map env)
  { BExpression tsmv = test.binvariantForm(env,true); 
    BExpression case1 = new BBinaryExpression("=>", tsmv, ifExp.binvariantForm(env,true)); 
    BExpression case2 = new BBinaryExpression("=>", new BUnaryExpression("not", tsmv), 
                   elseExp.binvariantForm(env,true)); 
    case1.setBrackets(true); 
    case2.setBrackets(true); 
    return new BBinaryExpression("&", case1, case2); 
  } 

  public BExpression bqueryForm()
  { BExpression tsmv = test.bqueryForm(); 
    BExpression case1 = new BBinaryExpression("=>", tsmv, ifExp.bqueryForm()); 
    BExpression case2 = new BBinaryExpression("=>", new BUnaryExpression("not", tsmv), 
                   elseExp.bqueryForm()); 
    case1.setBrackets(true); 
    case2.setBrackets(true); 
    return new BBinaryExpression("&",case1,case2);
  } 

  public Expression createActionForm(final Vector sms)
  { Expression lact = ifExp.createActionForm(sms);
    Expression ract = elseExp.createActionForm(sms);
    Expression actionForm =
        new BinaryExpression("||",lact,ract);
    javaForm =
        new BinaryExpression(";",lact.javaForm,
                                 ract.javaForm);
    actionForm.javaForm = javaForm;
    return actionForm;
  } // not correct, should be (test => lact) parallel (not(test) => ract)

  public boolean isOrExpression() { return false; } 

  public boolean isMultiple() 
  { return ifExp.isMultiple() && elseExp.isMultiple(); } 

  public int minModality()
  { int ml = ifExp.minModality();
    int mr = elseExp.minModality();
    if (ml > mr && mr > ModelElement.NONE)
    { return mr; }
    return ml; // could be NONE
  }

  public int maxModality()
  { int ml = ifExp.maxModality();
    int mr = elseExp.maxModality();
    if (ml > mr)
    { return ml; }
    return mr;
  }

  public boolean typeCheck(final Vector types,
                           final Vector entities,
                           final Vector contexts, final Vector env)
  { test.typeCheck(types,entities,contexts,env); 
    ifExp.typeCheck(types,entities,contexts,env);
    elseExp.typeCheck(types,entities,contexts,env); 
    if ((ifExp.type + "").equals(elseExp.type + ""))
    { type = ifExp.type; 
      elementType = ifExp.elementType; 
      return true; 
    } 
    else 
    { System.err.println("!! WARNING: types in then, else of " + this + " are different: " + ifExp.type + " /= " + elseExp.type); 
      type = ifExp.type; 
      elementType = ifExp.elementType; 
      return false; 
    }  
  } // if and else should have same type, or be type-compatible. Overall type is 
    // closest common supertype.  

  public int typeCheck(Vector sms)
  { test.typeCheck(sms); 
    int tt = ifExp.typeCheck(sms);
    elseExp.typeCheck(sms); 
    if ((ifExp.type + "").equals(elseExp.type + ""))
    { return tt; } 
    else 
    { System.err.println("WARNING: types in if,else of " + this + " are different"); 
      return ERROR; 
    }  
  } // if and else should have same type. 

  public boolean isPrimitive() 
  { return ifExp.isPrimitive() && elseExp.isPrimitive(); } 

  public Expression invert() 
  { BinaryExpression be1 = new BinaryExpression("=>", test, ifExp); 
    BinaryExpression be2 = new BinaryExpression("=>", new UnaryExpression("not", test), 
                                                elseExp); 
    be1.setBrackets(true); 
    be2.setBrackets(true); 
    Expression invbe1 = be1.invert(); 
    Expression invbe2 = be2.invert(); 
    return new BinaryExpression("&", invbe1, invbe2); 
  } 

  public Expression dereference(BasicExpression ref)
  { ConditionalExpression res = (ConditionalExpression) clone(); 
    res.test = test.dereference(ref); 
    res.ifExp = ifExp.dereference(ref); 
    res.elseExp = elseExp.dereference(ref); 
    return res;  
  } 

  public Expression replaceReference(BasicExpression ref, Type t)
  { ConditionalExpression res = (ConditionalExpression) clone(); 
    Expression tr = test.replaceReference(ref,t); 
    Expression lr = ifExp.replaceReference(ref,t); 
    Expression rr = elseExp.replaceReference(ref,t); 
    res.test = tr; 
    res.ifExp = lr; 
    res.elseExp = rr; 
      
    return res; 
  } 

  public Expression addReference(BasicExpression ref, Type t)
  { ConditionalExpression res = (ConditionalExpression) clone(); 
    Expression tr = test.addReference(ref,t); 
    Expression lr = ifExp.addReference(ref,t); 
    Expression rr = elseExp.addReference(ref,t); 
    res.test = tr; 
    res.ifExp = lr; 
    res.elseExp = rr; 
      
    return res; 
  } 

  public Expression addContainerReference(BasicExpression ref, String var, Vector excls)
  { ConditionalExpression res = (ConditionalExpression) clone(); 
    Expression tr = test.addContainerReference(ref,var,excls); 
    Expression lr = ifExp.addContainerReference(ref,var,excls); 
    Expression rr = elseExp.addContainerReference(ref,var,excls); 
    res.test = tr; 
    res.ifExp = lr; 
    res.elseExp = rr; 
      
    return res; 
  } 

  public DataDependency rhsDataDependency()
  { // if p.f = val then  val, p --> f
    return null; 
  } 

  public DataDependency getDataItems()
  { DataDependency res = ifExp.getDataItems();
    res.union(elseExp.getDataItems());
    return res;
  }

  // returns true if opf(val) can possibly make expression true
  public boolean relevantOccurrence(String op, Entity ent, 
                                    String val,
                                    String f)
  { return false; } 

  public Vector allBinarySubexpressions()
  { // returns new Vector() by default.
    Vector res = new Vector();
    return res; 
  } 

  public Vector allValuesUsedIn()
  { Vector res = test.allValuesUsedIn();
    res = VectorUtil.union(res,ifExp.allValuesUsedIn()); 
    return VectorUtil.union(res,
                            elseExp.allValuesUsedIn());
  }

  public Vector allOperationsUsedIn()
  { Vector res = test.allOperationsUsedIn();
    res = VectorUtil.union(res,ifExp.allOperationsUsedIn()); 
    return VectorUtil.union(res,
                            elseExp.allOperationsUsedIn());
  }

  public Vector equivalentsUsedIn()
  { Vector res = test.equivalentsUsedIn();
    res = VectorUtil.union(res,ifExp.equivalentsUsedIn()); 
    return VectorUtil.union(res,
                            elseExp.equivalentsUsedIn());
  }

  public Vector allFeaturesUsedIn()
  { Vector res = test.allFeaturesUsedIn();
    res = VectorUtil.union(res,ifExp.allFeaturesUsedIn()); 
    return VectorUtil.union(res,
                            elseExp.allFeaturesUsedIn());
  }

  public Vector getUses(String feature)
  { Vector res = new Vector();
    res.addAll(test.getUses(feature));
    res.addAll(ifExp.getUses(feature));
    res.addAll(elseExp.getUses(feature));
    return res;
  }  // VectorUtil.union of the uses? 

  public Vector getVariableUses()
  { Vector res = new Vector();
    res.addAll(test.getVariableUses());
    res.addAll(ifExp.getVariableUses());
    res.addAll(elseExp.getVariableUses());
    return res;
  }  // VectorUtil.union of the uses? 

  public Expression skolemize(Expression sourceVar, java.util.Map env)
  { Expression res;

    Expression tt = test.skolemize(sourceVar,env); 
    Expression nleft = ifExp.skolemize(sourceVar,env); 
    Expression nrigh = elseExp.skolemize(sourceVar,env); 
    res = new ConditionalExpression(tt,nleft,nrigh); 
    res.setBrackets(needsBracket); 
    return res; 
  } 

  public Expression determinate()
  { Expression dand = 
        new BinaryExpression("&",test.determinate(), ifExp.determinate());
    Expression pand = 
        new BinaryExpression("&", dand, elseExp.determinate());

    return pand.simplify();
  }

  public Expression definedness()
  { Expression dand = 
        new BinaryExpression("=>", test, ifExp.definedness());
    Expression pand = 
        new BinaryExpression("=>", new UnaryExpression("not", test), elseExp.definedness());
    Expression tdef = test.definedness(); 
    dand.setBrackets(true); 
    pand.setBrackets(true); 
    Expression and1 = new BinaryExpression("&", dand.simplify(), pand.simplify()); 
    Expression and2 = new BinaryExpression("&", tdef, and1); 

    return and2.simplify();
  }

  public void setPre()
  { test.setPre();
    ifExp.setPre();  
    elseExp.setPre(); 
  } 

  public String saveModelData(PrintWriter out) 
  { String id = Identifier.nextIdentifier(
                    "conditionalexpression_");
    out.println(id + " : ConditionalExpression"); 
    out.println(id + ".expId = \"" + id + "\""); 

    String testId = test.saveModelData(out); 
    String leftId = ifExp.saveModelData(out); 
    String rightId = elseExp.saveModelData(out); 
    out.println(id + ".test = " + testId); 
    out.println(id + ".ifExpr = " + leftId); 
    out.println(id + ".elseExpr = " + rightId); 
    out.println(id + ".ifExp = " + leftId); 
    out.println(id + ".elseExp = " + rightId);

    String tname = "void"; 
    if (type != null) 
    { tname = type.getUMLModelName(out); } 
    out.println(id + ".type = " + tname); 
 
    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(id + ".elementType = " + etname); 
    } 
    else 
    { out.println(id + ".elementType = " + tname); } 

    out.println(id + ".needsBracket = " + needsBracket); 
    out.println(id + ".umlKind = " + umlkind); 
 
    return id; 
  } 


}
 