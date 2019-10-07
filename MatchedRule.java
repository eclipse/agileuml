import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: ATL */ 

public class MatchedRule extends Rule
{ boolean isLazy = false; 
  boolean isUnique = false; 
  InPattern inPattern = null; 
  boolean isCalled = false; 
  Vector parameters = new Vector(); // of Attribute
  Vector using = new Vector(); // of Attribute
    

  public MatchedRule(boolean isL, boolean isU)
  { isLazy = isL; 
    isUnique = isU; 
    inPattern = new InPattern(); // default for called rules
  } 

  public boolean isLazy() 
  { return isLazy; } 

  public boolean isCalled() 
  { return isCalled; } 

  public void setInPattern(InPattern inp)
  { if (inp != null) 
    { inPattern = inp; }
  }  

  public void setIsCalled(boolean c)
  { isCalled = c; } 

  public void addParameter(Attribute p)
  { parameters.add(p); } 

  public String getSignature()
  { String res = "("; 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getName() + " : " + att.getType(); 
      if (i < parameters.size() - 1) 
      { res = res + ", "; } 
    } 
    return res + ")"; 
  } 

  public void setParameters(Vector pars)
  { parameters = pars; } 

  public void setUsing(Vector usin)
  { using = usin; 
    if (using == null) 
    { using = new Vector(); } 
  } 

  public String toString()
  { String res = "rule " + name; 
    
    if (isLazy) 
    { res = "lazy " + res; } 
    if (isUnique) 
    { res = "unique " + res; } 
    if (isCalled) 
    { res = res + getSignature(); } 

    res = res + " {\n"; 
    if (isCalled) 
    { res = res + "  "; } 
    else 
    { res = res + "  " + inPattern + "\n"; }  
    
    if (using != null && using.size() > 0) 
    { res = res + "  using {\n"; 
      for (int i = 0; i < using.size(); i++) 
      { Attribute usv = (Attribute) using.get(i); 
        Type tt = usv.getType(); 
        Expression def = usv.getInitialExpression(); 
        res = res + "    " + usv.getName() + " : " + tt + " = " + def + ";\n"; 
      } 
      res = res + "}\n"; 
    } 

    res = res + "  " + outPattern + "\n"; 
    
    if (actionBlock != null) 
    { res = res + "  do { " + actionBlock + " }\n"; }  
    res = res + "}"; 
    return res; 
  } 

  public boolean equals(Object x) 
  { if (x instanceof MatchedRule) 
    { MatchedRule xx = (MatchedRule) x; 
      if (toString().equals(xx + ""))
      { return true; } 
    } 
    return false; 
  } 


  public boolean typeCheck(Vector types, Vector entities)
  { Vector env = inPattern.allVariables(); 
    Vector contexts = new Vector(); 

    env.addAll(parameters); 

    if (using != null && using.size() > 0) 
    { for (int i = 0; i < using.size(); i++) 
      { Attribute usv = (Attribute) using.get(i); 
        Expression def = usv.getInitialExpression(); 
        def.typeCheck(types,entities,contexts,env); 
        env.add(usv); 
      } 
    } 
    
    env.addAll(outPattern.allVariables()); // and a copy of the parameters & using variables
    outPattern.typeCheck(types,entities,contexts,env);

    if (actionBlock != null) 
    { actionBlock.typeCheck(types,entities,contexts,env); } 

    return true; 
  }  

  private Expression usingToExpression() 
  { Expression res = new BasicExpression(true); 

    if (using == null) 
    { return res; } 

    for (int i = 0; i < using.size(); i++) 
    { Attribute uv = (Attribute) using.get(i); 
      Expression uve = new BasicExpression(uv); 
      Expression uvini = uv.getInitialExpression(); 
      Expression eq = new BinaryExpression("=", uve, uvini); 
      res = Expression.simplify("&", res, eq, null); 
    } 
    return res; 
  } 


  public void toOperation(Vector types, Vector ents, java.util.Map interp, UseCase uc)
  { // In the case of lazy or called rules
    // inPattern must be non-empty or at least one parameter. 

    Expression ante = inPattern.toExpression(uc); 
    if (ante == null) 
    { ante = new BasicExpression(true); } 
    
    Vector env = inPattern.allVariables(); 
    env.addAll(parameters); 

    Vector contexts = new Vector(); 
    ante.typeCheck(types,ents,contexts,env);

    Entity owner = inPattern.firstEntity(); 
    /* Can be null in the case of a called rule */ 
    // if (owner == null) 
    // { Attribute p1 = (Attribute) parameters.get(0); 
    //   owner = p1.getType().getEntity(); 
    // } 
    Expression succ = outPattern.toExpression(types,ents,env,interp,uc); 
    Vector env2 = outPattern.allVariables(); 

    Attribute inatt = (Attribute) env.get(0);
    String mainvar = inatt.getName(); 
    BasicExpression inexp = new BasicExpression(mainvar);

    ante = ante.dereference(inexp); 
    succ = succ.dereference(inexp);  

    Attribute outatt; 
    if (env2.size() == 0)
    { Attribute r = new Attribute("result", new Type("boolean", null), 
                                  ModelElement.INTERNAL); 
      env2.add(r); 
      outatt = r; 
    } 
    else 
    { outatt = (Attribute) env2.get(0); } 

    String resultvar = outatt.getName(); 
    BasicExpression outexp = new BasicExpression(outatt);

    // also replaceModuleReferences in the actionBlock
    // add var uv : T ; uv <- init for each using variable. 

    if (actionBlock != null && isCalled == false) 
    { String opname = name + "ActionBlockop"; 
      Vector pars = new Vector(); 
      pars.addAll(env); 
      pars.remove(0);  // becomes self
      pars = VectorUtil.union(pars,env2); 
      pars.remove(inatt); 

      if (using != null)
      { pars.addAll(using); } 

      Statement dcode = actionBlock.replaceModuleReferences(uc); 
      Statement code = dcode.dereference(inexp); 
      BehaviouralFeature op = new BehaviouralFeature(opname,pars,false,null);  
      // op.setDerived(true); 
      op.addStereotype("explicit"); 
      op.setActivity(code); 
      op.setPost(new BasicExpression(true));   
      owner.addOperation(op); 
      op.setEntity(owner); 
      String opcall = "self." + opname + "(";
      Vector parnames = new Vector();  
      for (int i = 1; i < env.size(); i++) 
      { String par = ((Attribute) env.get(i)).getName();
        if (parnames.contains(par) || mainvar.equals(par)) { } 
        else 
        { parnames.add(par); } 
      }
      for (int j = 0; j < env2.size(); j++) 
      { String par = ((Attribute) env2.get(j)).getName();
        if (parnames.contains(par) || mainvar.equals(par)) { } 
        else 
        { parnames.add(par); } 
      }  
      for (int k = 0; k < parnames.size(); k++) 
      { opcall = opcall + parnames.get(k); 
        if (k < parnames.size() - 1)
        { opcall = opcall + ","; } 
      }    
      opcall = opcall + ")"; 
      BasicExpression invokeop = 
        new BasicExpression(opcall,0); 
      Expression efo = invokeop.checkIfSetExpression(); 
      efo.setEntity(owner); // ??
      if (efo instanceof BasicExpression) 
      { BasicExpression befo = (BasicExpression) efo; 
        if (befo.objectRef != null) 
        { befo.objectRef.setEntity(owner); } 
      } 
      succ = Expression.simplify("&",succ,efo,null); 
    } 
    else if (actionBlock != null && isCalled == true) 
    { String opname = name + "ActionBlockop"; 
      Vector pars = new Vector(); 
      pars.addAll(env); 
      pars = VectorUtil.union(pars,env2); 

      Vector pars1 = new Vector(); 
      Vector parnames = new Vector();  

      if (using != null)
      { pars.addAll(using); } 

      for (int i = 0; i < pars.size(); i++) 
      { Attribute pp = (Attribute) pars.get(i); 
        String par = pp.getName();
        if ("result".equals(par)) { } 
        else 
        { parnames.add(par); 
          pars1.add(pp); 
        }  
      }

      BehaviouralFeature op = new BehaviouralFeature(opname,pars1,false,null);  
      // op.setDerived(true); 
      op.addStereotype("explicit"); 
      Statement stat = actionBlock.replaceModuleReferences(uc); 
      op.setActivity(stat); 
      op.setPost(new BasicExpression(true));
      op.setStatic(true);    
      uc.addOperation(op); 
      op.setUseCase(uc); 
      String opcall = (uc.getClassifier().getName()) + "." + opname + "(";


      for (int k = 0; k < parnames.size(); k++) 
      { opcall = opcall + parnames.get(k); 
        if (k < parnames.size() - 1)
        { opcall = opcall + ","; } 
      }    

      opcall = opcall + ")"; 
      BasicExpression invokeop = 
        new BasicExpression(opcall,0); 
      Expression efo = invokeop.checkIfSetExpression(); 
      // efo.setEntity(owner); // ??
      if (efo instanceof BasicExpression) 
      { BasicExpression befo = (BasicExpression) efo; 
        if (befo.objectRef != null) 
        { befo.objectRef.setEntity(uc.getClassifier()); } 
      } 

      if (outPattern.size() == 0) 
      { succ = new BinaryExpression("=", new BasicExpression(outatt), efo); } 
      else 
      { succ = Expression.simplify("&",succ,efo,null); }  
    } 

    String opnme = name; 
    Vector pars0 = new Vector(); 
    pars0.addAll(env); 
    if (isCalled == false) 
    { pars0.remove(0); }  
    BehaviouralFeature op0 = new BehaviouralFeature(opnme,pars0,false,outatt.getType());  
    // op0.setDerived(true); 
    op0.addStereotype("explicit"); 
    op0.setPre(ante); 

    BinaryExpression setresult = new BinaryExpression("=",new BasicExpression("result"),outexp); 
    if ((outexp + "").equals("result")) { } 
    else 
    { succ = Expression.simplify("&",succ,setresult,null); }

    Expression usingexp = usingToExpression(); 
    Expression uex1 = usingexp.replaceModuleReferences(uc); 

    Expression ued = uex1.dereference(inexp); 
    succ = Expression.simplify("=>", ued, succ, null); 
                             

    for (int i = 0; i < env2.size(); i++) 
    { Attribute att = (Attribute) env2.get(i);
      String nme = att.getName(); 
      Type typ = att.getType();  
      ModelElement met = ModelElement.lookupByName(att.getName(), env); 
      if (met == null && typ.isEntity())  // att is created by out pattern
      { succ = new BinaryExpression("#", new BinaryExpression(":",
                                      new BasicExpression(nme), new BasicExpression(typ)),
                                      succ);
      } // else, postfix each att.f by @pre in succ
      // else 
      // { succ = succ.addPreForms(att.getName()); } 
    }
    op0.setPost(succ);  // result = par0 

    if (isUnique) 
    { op0.setCached(true); } 

    if (owner != null) 
    { owner.addOperation(op0); 
      op0.setEntity(owner); 
    } 
    else 
    { uc.addOperation(op0); 
      op0.setStatic(true); 
      op0.setUseCase(uc); 
    } 
  } 

  public Constraint toConstraint(Vector types, Vector ents, java.util.Map interp, UseCase uc)
  { Expression ante = inPattern.toExpression(uc); 
    if (ante == null) 
    { ante = new BasicExpression(true); } 

    for (int g = 0; g < using.size(); g++) 
    { Attribute var = (Attribute) using.get(g); 
      BasicExpression varbe = new BasicExpression(var); 
      varbe.setType(var.getType()); 
      BinaryExpression vareq = new BinaryExpression("=", varbe, var.getInitialExpression()); 
      ante = Expression.simplify("&",ante,vareq,null);  
    } 

    Vector env = inPattern.allVariables(); 
    Entity owner = inPattern.firstEntity(); 
    Expression succ = outPattern.toExpression(types,ents,env,interp,uc); 
    Vector env2 = outPattern.allVariables(); 

    Attribute inatt = (Attribute) env.get(0);
    String mainvar = inatt.getName(); 
    BasicExpression inexp = new BasicExpression(mainvar);
    ante = ante.dereference(inexp); 
    succ = succ.dereference(inexp);  

    if (actionBlock != null) // If a rule is split, put this in the 2nd rule. 
    { String opname = name + "ActionBlockop"; 
      Vector pars = new Vector(); 
      pars.addAll(env); 
      pars.remove(0); 
      pars = VectorUtil.union(pars,env2); 
      if (using != null)
      { pars.addAll(using); } 
      pars.remove(inatt); 

      Statement dcode = actionBlock.replaceModuleReferences(uc); 
      Statement code = dcode.dereference(inexp); 
      code.setEntity(owner); // self in code is an instance of owner
      BehaviouralFeature op = new BehaviouralFeature(opname,pars,false,null);  
      // op.setDerived(true); 
      op.addStereotype("explicit"); 
      op.setActivity(code); 
      owner.addOperation(op); 
      op.setPost(new BasicExpression(true));   
      op.setEntity(owner); 
      String opcall = "self." + opname + "(";
      Vector parnames = new Vector();  

      for (int i = 1; i < env.size(); i++) 
      { String par = ((Attribute) env.get(i)).getName();
        if (parnames.contains(par) || mainvar.equals(par)) { } 
        else 
        { parnames.add(par); } 
      }

      for (int j = 0; j < env2.size(); j++) 
      { String par = ((Attribute) env2.get(j)).getName();
        if (parnames.contains(par) || mainvar.equals(par)) { } 
        else 
        { parnames.add(par); } 
      }  

      if (using != null)
      { for (int k = 0; k < using.size(); k++) 
        { Attribute usev = (Attribute) using.get(k); 
          String vname = usev.getName();
           
          if (parnames.contains(vname) || mainvar.equals(vname)) { } 
          else 
          { parnames.add(vname); }
        }  
      }  

      for (int k = 0; k < parnames.size(); k++) 
      { opcall = opcall + parnames.get(k); 
        if (k < parnames.size() - 1)
        { opcall = opcall + ","; } 
      }    
      opcall = opcall + ")"; 
      BasicExpression invokeop = 
        new BasicExpression(opcall,0); 
      Expression efo = invokeop.checkIfSetExpression(); 
      efo.setEntity(owner); // ??
      if (efo instanceof BasicExpression) 
      { BasicExpression befo = (BasicExpression) efo; 
        if (befo.objectRef != null) 
        { befo.objectRef.setEntity(owner); } 
      } 
      succ = Expression.simplify("&",succ,efo,null); 
    } 

    for (int i = 0; i < env2.size(); i++) 
    { Attribute att = (Attribute) env2.get(i);
      String nme = att.getName(); 
      String typ = att.getType() + "";  
      ModelElement met = ModelElement.lookupByName(att.getName(), env); 
      if (met == null)  // att is created by out pattern
      { succ = new BinaryExpression("#", new BinaryExpression(":",
                                        new BasicExpression(nme), new BasicExpression(typ)),
                                      succ);
      } // else, postfix each att.f by @pre in succ
      // else 
      // { succ = succ.addPreForms(att.getName()); } 
    }

    Expression usingexp = usingToExpression(); 
    Expression uex1 = usingexp.replaceModuleReferences(uc); 
    Expression ued = uex1.dereference(inexp); 
    succ = Expression.simplify("=>", ued, succ, null); 

    Constraint con = new Constraint(ante,succ); 
    con.setOwner(owner); 
    return con; 
  } 

  public boolean hasOutputType(Vector datas)
  { for (int i = 0; i < datas.size(); i++) 
    { String typ = (String) datas.get(i); 
      if (outPattern.hasType(typ)) 
      { return true; } 
    } 
    return false; 
  } 

  public boolean calledByResolveTemp(Expression e, String ovar)
  { if (outPattern.hasType(e.getType() + "") && outPattern.hasVariable(ovar)) 
    { return true; } 
    return false; 
  } 

  public MatchedRule slice()
  { MatchedRule newrule = new MatchedRule(isLazy,isUnique); 
    newrule.setName(name + "$1"); 
    newrule.setInPattern(inPattern); 
    OutPattern op = outPattern.slice(); 
    newrule.setOutPattern(op); 
    newrule.setUsing(using); 
    newrule.setActionBlock(actionBlock); 
    actionBlock = null; // put into newrule
    return newrule; 
  } // using?

  public int complexity() 
  { int r1 = inPattern.complexity(); 
    int r2 = outPattern.complexity(); 
    if (using != null) 
    { for (int i = 0; i < using.size(); i++) 
      { Attribute usev = (Attribute) using.get(i); 
        r1 = r1 + 3 + usev.getType().complexity() + usev.getInitialExpression().syntacticComplexity(); 
      } 
    } 

    int r3 = 0; 
    if (actionBlock != null) 
    { r3 = actionBlock.syntacticComplexity(); } 
    int result = r1 + r2 + r3; 
    System.out.println("*** Complexity of rule " + name + " is " + result); 
    return result; 
  } // and the using expressions

  public int cyclomaticComplexity() 
  { int r1 = inPattern.cyclomaticComplexity(); 
    int r2 = outPattern.cyclomaticComplexity(); 
    int r3 = 0; 
    if (actionBlock != null) 
    { r3 = actionBlock.cyclomaticComplexity(); } 
    int result = r1 + r2 + r3; 
    // System.out.println("*** Cyclomatic complexity of rule " + name + " is " + result); 
    return result; 
  } // and the using expressions

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 

    if (inPattern != null) 
    { res.addAll(inPattern.operationsUsedIn()); } 

    if (using != null) 
    { for (int i = 0; i < using.size(); i++) 
      { Attribute usev = (Attribute) using.get(i); 
        res.addAll(usev.getInitialExpression().allOperationsUsedIn()); 
      } 
    } 

    if (outPattern != null) 
    { res.addAll(outPattern.operationsUsedIn()); }

    if (actionBlock != null) 
    { res.addAll(actionBlock.allOperationsUsedIn()); } 
 
    return res; 
  } // and using

  public Vector resolveTempsUsedIn()
  { Vector res = new Vector(); 

    if (inPattern != null) 
    { res.addAll(inPattern.getUses("resolveTemp")); } 

    if (using != null) 
    { for (int i = 0; i < using.size(); i++) 
      { Attribute usev = (Attribute) using.get(i); 
        res.addAll(usev.getInitialExpression().getUses("resolveTemp")); 
      } 
    } 

    if (outPattern != null) 
    { res.addAll(outPattern.getUses("resolveTemp")); }

    // if (actionBlock != null) 
    // { res.addAll(actionBlock.getUses("resolveTemp")); } 
 
    return res; 
  } // and using

  public int epl() 
  { int r = parameters.size() + inPattern.size() + outPattern.size(); 
    if (using != null) 
    { r = r + using.size(); } 
 
    System.out.println("*** EPL of rule " + name + " is: " + r); 
    return r; 
  } // plus variables introduced in the code

} 

  