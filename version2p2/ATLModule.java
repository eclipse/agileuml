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
/* Module: ATL */ 

public class ATLModule
{ String name; 
  Vector elements; // MatchedRule
  java.util.Map interp = null; 
  Vector attributes = new Vector(); // of Attribute
  Vector operations = new Vector(); // of BehaviouralFeature

  public ATLModule(String nme)
  { name = nme; } 

  public String getName()
  { return name; } 

  public void addElement(ModuleElement me) 
  { elements.add(me); }

  public void setElements(Vector elems) 
  { elements = elems; }

  public void addElements(Vector elems) 
  { elements.addAll(elems); }

  public void addAttribute(Attribute att)
  { attributes.add(att); } 

  public void addOperation(BehaviouralFeature bf)
  { operations.add(bf); } 

  public void setInterpretation(java.util.Map intp)
  { interp = intp; } 

  public String toString()
  { String res = "module " + name + ";\n"; 
    res = res + "create OUT : T from IN : S;\n";

    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      String attname = att.getName(); 
      Entity ent = att.getEntity(); 
      Type t = att.getType();
      Expression e = att.getInitialExpression();  
      res = res + "  helper "; 
      if (ent != null) 
      { res = res + "context " + ent; } 
      res = res + " def : " + attname + " : " + t + " = " + e + ";\n"; 
    } 

    for (int i = 0; i < operations.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(i); 
      String attname = bf.getName(); 
      Entity ent = bf.getEntity(); 
      Type t = bf.getResultType();
      Expression e = bf.getPost();  
      res = res + "  helper "; 
      if (ent != null) 
      { res = res + "context " + ent; } 
      res = res + " def : " + bf.getSignature() + " : " + t + " = " + e + ";\n"; 
    } 

    for (int i = 0; i < elements.size(); i++) 
    { ModuleElement me = (ModuleElement) elements.get(i); 
      res = res + me + "\n"; 
    } 

    return res; 
  }  

  public int nops() 
  { return operations.size(); } 

  public int nrules()
  { return elements.size(); } 

  public boolean typeCheck(Vector types, Vector entities) 
  { Vector env = new Vector(); 
  
    for (int j = 0; j < attributes.size(); j++) 
    { Attribute att = (Attribute) attributes.get(j); 
      Vector context = new Vector(); 
      Entity ent = att.getEntity(); 
      if (ent != null) 
      { context.add(ent); } 
      Expression ini = att.getInitialExpression(); 
      ini.typeCheck(types,entities,context,env); 
      env.add(att); 
    } 

    for (int k = 0; k < operations.size(); k++) 
    { BehaviouralFeature op = (BehaviouralFeature) operations.get(k); 
      op.typeCheck(types,entities); 
    } 

    for (int i = 0; i < elements.size(); i++) 
    { ModuleElement me = (ModuleElement) elements.get(i);
      if (me instanceof MatchedRule) 
      { MatchedRule rule = (MatchedRule) me; 
        rule.typeCheck(types,entities); 
      } 
    } 
    return true; 
  } 
     
  public UseCase toUML(Vector types, Vector entities, Vector inits)
  { UseCase res = new UseCase(name,null);
    res.addPostconditions(inits); 

    java.util.Map interp = getInterpretation(); 
 
    for (int j = 0; j < attributes.size(); j++) 
    { Attribute att = (Attribute) attributes.get(j); 
      Entity ent = att.getEntity(); 
      if (ent != null) 
      { Expression ini = att.getInitialExpression(); 
        Expression resexp = new BasicExpression("result"); 
        resexp.setType(att.getType()); 
        resexp.setUmlKind(Expression.VARIABLE); 
        Expression post = new BinaryExpression("=", resexp, ini.replaceModuleReferences(res)); 
        BehaviouralFeature bf = 
          new BehaviouralFeature(att.getName(), new Vector(), true, att.getType()); 
        ent.addOperation(bf); 
        bf.setPost(post); 
        bf.setEntity(ent); 
        bf.setCached(true); // helper attributes act like cached operations in ATL
      }  
      else 
      { res.addAttribute(att); }  

      for (int k = 0; k < operations.size(); k++) 
      { BehaviouralFeature op = (BehaviouralFeature) operations.get(k); 
        Entity oent = op.getEntity(); 
        if (oent == null) 
        { res.addOperation(op); }  
        Statement act = op.getActivity(); 
        if (act != null) 
        { Statement stat = act.replaceModuleReferences(res); 
          op.setActivity(stat); 
        } 
      } 
    } 

    for (int i = 0; i < elements.size(); i++) 
    { ModuleElement me = (ModuleElement) elements.get(i);
      if (me instanceof MatchedRule) 
      { MatchedRule rule = (MatchedRule) me; 
        rule.typeCheck(types,entities); 
        if (rule.isLazy() || rule.isCalled())
        { rule.toOperation(types,entities,interp,res); } 
        else 
        { Constraint con = rule.toConstraint(types,entities,interp,res); 
          res.addPostcondition(con);
        }  
      } 
    } 

    // stat.typeCheck(types,entities,contexts,newparms);
    return res; 
  } 

  public java.util.Map getInterpretation()
  { java.util.Map res = new java.util.HashMap(); 
    // InE -> OutE  for the first input InE and first output OutE of all matched rules

    for (int i = 0; i < elements.size(); i++) 
    { if (elements.get(i) instanceof MatchedRule) 
      { MatchedRule mr = (MatchedRule) elements.get(i); 
        OutPattern opatt = mr.outPattern; 
        InPattern inpatt = mr.inPattern; 
        if (inpatt != null && opatt != null) 
        { String t1 = inpatt.firstType(); 
          if (t1 != null) 
          { Vector opes = opatt.elements; 
            if (opes.size() > 0) 
            { OutPatternElement ope = (OutPatternElement) opes.get(0); 
              Entity tt = ope.getEntity(); 
              res.put(t1,tt);   
            } 
          }
        } 
      } 
    }
    interp = res; 
    return res; 
  }  

  public Vector dataAnalysis()
  { // returns list of rules that must be split
    Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { if (elements.get(i) instanceof MatchedRule) 
      { MatchedRule mr = (MatchedRule) elements.get(i);
        if (mr.isLazy() || mr.isCalled()) { continue; } 
        // mr must be split if it reads a target type that is an output of itself or a 
        // later rule.  
        OutPattern opatt = mr.outPattern; 
        Vector streads = opatt.sourceTypesRead(); 

        Statement stat = mr.actionBlock; 
        if (stat != null) 
        { streads.addAll(stat.readFrame()); } 

        System.out.println("Read frame of rule " + i + " is: " + streads); 

        for (int j = i; j < elements.size(); j++) 
        { MatchedRule mr2 = (MatchedRule) elements.get(j); 
          if (!mr2.isLazy() && !mr2.isCalled() && mr2.hasOutputType(streads))
          { System.err.println(">> Rule " + mr2.getName() + " writes an entity read by earlier rule " + mr); 
            System.err.println(">> " + mr.getName() + " will be split into two rules/constraints"); 

            MatchedRule mr3 = mr.slice(); 
            if (res.contains(mr3)) { } 
            else
            { res.add(mr3); } // only add *once*. 
          } 
        } 
      } 
    } 
    return res; 
  } 

  public int complexity() 
  { int result = 0; 

    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      Expression ini = att.getInitialExpression(); 
      if (ini == null) { continue; } 
      int acomp = att.syntacticComplexity(); 
      System.out.println("*** Syntactic complexity of helper " + att.getName() + " is " + acomp); 
      result += acomp; 
    } 

    for (int j = 0; j < operations.size(); j++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(j); 
      int bfc = bf.syntacticComplexity();
      System.out.println("*** Syntactic complexity of helper " + bf.getName() + " is " + bfc); 
      result += bfc;  
    } 

    for (int i = 0; i < elements.size(); i++) 
    { MatchedRule r = (MatchedRule) elements.get(i); 
      result = result + r.complexity(); 
    } 

    System.out.println("*** Number of rules in transformation " + name + " is: " + elements.size()); 
    int hs = operations.size() + attributes.size(); 
    System.out.println("*** Number of helpers in transformation " + name + " is: " + hs); 

    return result; 
  } 

  public int cyclomaticComplexity() 
  { int result = 0; 

    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      Expression ini = att.getInitialExpression(); 
      if (ini == null) { continue; } 
      int acomp = ini.cyclomaticComplexity(); 
      System.out.println("*** Cyclomatic complexity of helper " + att.getName() + " is " + acomp); 
      if (acomp > 10) 
      { result += 1; } 
    } 

    for (int j = 0; j < operations.size(); j++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(j); 
      int bfc = bf.cyclomaticComplexity();
      System.out.println("*** Cyclomatic complexity of helper " + bf.getName() + " is " + bfc); 
      if (bfc > 10) 
      { result += 1; }        
    } 

    for (int i = 0; i < elements.size(); i++) 
    { MatchedRule r = (MatchedRule) elements.get(i); 
      int rfc = r.cyclomaticComplexity(); 
      System.out.println("*** Cyclomatic complexity of rule " + r.getName() + " is " + rfc); 
      if (rfc > 10) 
      { result = result + 1; }  
    } 

    return result; 
  } 

  public Map getCallGraph()
  { // Include also thisModule.resolveTemp(x,v) 

    Map res = new Map(); 
    String nme = getName(); 

    int opsSize = operations.size(); 
    for (int i = 0; i < opsSize; i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(i); 

      Set calledrules = new HashSet(); 
      Vector bfcalls = bf.operationsUsedIn(); 
      for (int j = 0; j < bfcalls.size(); j++) 
      { res.add_pair(nme + "::" + bf.getName(), bfcalls.get(j)); 
        calledrules.add(bfcalls.get(j) + ""); 
      } 
      System.out.println("*** EFO of operation " + bf.getName() + " is: " + calledrules.size());   
    } 

    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      Expression ini = att.getInitialExpression(); 
      if (ini == null) { continue; } 
      Set calledrules = new HashSet(); 
      Vector attcalls = ini.allOperationsUsedIn(); 
      for (int j = 0; j < attcalls.size(); j++) 
      { res.add_pair(nme + "::" + att.getName(), attcalls.get(j)); 
        calledrules.add(attcalls.get(j) + ""); 
      } 
      System.out.println("*** EFO of helper " + att.getName() + " is: " + calledrules.size());   
    } 

    int rulesSize = elements.size(); 

    for (int i = 0; i < rulesSize; i++) 
    { MatchedRule rr = (MatchedRule) elements.get(i); 
      Set calledrules = new HashSet(); 
      Vector opuses = rr.operationsUsedIn(); 
      for (int j = 0; j < opuses.size(); j++) 
      { res.add_pair(rr.getName(), opuses.get(j)); 
        calledrules.add(opuses.get(j) + ""); 
      }

      Vector rts = rr.resolveTempsUsedIn(); 

      if (rts.size() > 0) 
      { System.out.println("*** ResolveTemps used in rule " + rr.getName() + ": " + rts);   

        for (int k = 0; k < rts.size(); k++) 
        { BasicExpression rtemp = (BasicExpression) rts.get(k); 
          Vector pars = rtemp.getParameters(); 
          if (pars != null && pars.size() > 1) 
          { Expression esrc = (Expression) pars.get(0);
            Expression evar = (Expression) pars.get(1);  
            System.out.println(esrc + " " + esrc.type); 

            for (int j = 0; j < rulesSize; j++) 
            { MatchedRule rx = (MatchedRule) elements.get(j); 
              if (rx.calledByResolveTemp(esrc, evar+"")) 
              { res.remove_pair(rr.getName(), "null::resolveTemp"); 
                res.add_pair(rr.getName(), rx.getName()); 
              } 
            } 
          } 
        } 
      } 

      System.out.println("*** EFO of rule " + rr.getName() + " is: " + (calledrules.size() + rts.size()));   
    } 

    return res; 
  } 

  public int epl() 
  { int res = 0; 
    int rulesSize = elements.size(); 

    for (int i = 0; i < rulesSize; i++) 
    { MatchedRule rr = (MatchedRule) elements.get(i); 
      int repl = rr.epl(); 
      if (repl > 10) 
      { System.err.println("*** Rule " + rr.getName() + " has excessive number of parameters/variables: " + repl); 
        res++; 
      } 
    } 
    return res; 
  } // and the operations

  public int uex() 
  { int res = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { MatchedRule r = (MatchedRule) elements.get(i); 
      if (!r.isLazy() && !r.isCalled())
      { res++; } 
    } 
    return (res*(res-1))/2;
  } 

}