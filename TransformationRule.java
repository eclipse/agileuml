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
/* Package: ETL  */ 

public class TransformationRule
{ String name;
  boolean lazy = false;
  boolean primary = false;
  boolean isAbstract = false; 

  Attribute source;
  Expression guard;
  Vector targets = new Vector(); // of Attribute
  Statement body;
  TransformationRule superclass; 

  public TransformationRule(String n, boolean l, boolean p)
  { name = n; 
    lazy = l; 
    primary = p;
    guard = new BasicExpression(true); 
  }

  public String getName()
  { return name; } 

  public boolean isAbstract() 
  { return isAbstract; } 

  public void setLazy(boolean b)
  { lazy = b; } 

  public void setPrimary(boolean b)
  { primary = b; } 

  public void setGuard(Expression e)
  { guard = e; }

  public void setSource(Attribute s)
  { source = s; }

  public void setBody(Statement s)
  { body = s; }

  public void addTarget(Attribute trg)
  { targets.add(trg); }

  public void addTargets(Vector trgs)
  { targets.addAll(trgs); }

  public Attribute getSource()
  { return source; }

  public Expression applicationCondition(Expression selfvar)
  { Type typ = source.getType(); 
    if (typ != null) 
    { if (typ.isEntity())
      { Entity src = typ.getEntity(); 
        Expression test = new BinaryExpression(":", selfvar, new BasicExpression(src)); 
        return Expression.simplifyAnd(test, guard); 
      } 
      else 
      { Expression test = new BinaryExpression(":", selfvar, new BasicExpression(typ)); 
        return Expression.simplifyAnd(test, guard); 
      } 
    } 
    return new BasicExpression(true); 
  } 

  public int syntacticComplexity()
  { int res = 0; 
    Expression cond = applicationCondition(new BasicExpression(source)); 
    res = cond.syntacticComplexity(); 

    for (int i = 0; i < targets.size(); i++) 
    { Attribute tt = (Attribute) targets.get(i); 
      BinaryExpression texp = new BinaryExpression(tt); 
      // System.out.println(texp + " complexity = " + texp.syntacticComplexity()); 
      res = res + texp.syntacticComplexity(); 
    } 

    if (body != null) 
    {       
      // System.out.println(body + " complexity = " + body.syntacticComplexity()); 
      res = res + body.syntacticComplexity(); 
    } 

    System.out.println("*** Syntactic complexity of rule " + name + " is " + res); 
    return res; 
  } 

  public int cyclomaticComplexity()
  { int res = 0; 
    Expression cond = applicationCondition(new BasicExpression(source)); 
    res = cond.cyclomaticComplexity(); 

    for (int i = 0; i < targets.size(); i++) 
    { Attribute tt = (Attribute) targets.get(i); 
      BinaryExpression texp = new BinaryExpression(tt); 
      res = res + texp.cyclomaticComplexity(); 
    } 

    if (body != null) 
    { res = res + body.cyclomaticComplexity(); } 

    System.out.println("*** Cyclomatic complexity of rule " + name + " is " + res); 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env) 
  { Vector myenv = new Vector(); 
    myenv.add(source); 
    myenv.addAll(env); 

    if (guard != null) 
    { guard.typeCheck(types,entities,contexts,myenv); } 

    myenv.addAll(targets); 
    if (body != null) 
    { return body.typeCheck(types,entities,contexts,myenv); } 
    return true;  
  } 
  

  public int epl()
  { int res = targets.size() + 1; 
    if (body != null) 
    { res = res + body.epl(); }
    return res; 
  }  
  // includes var statements in the code. 

  public Expression invocation(Expression selfvar)
  { Entity src = source.getType().getEntity(); 
    String opcall = name + "()"; 
    BasicExpression invoke = new BasicExpression(opcall,0); 
    BasicExpression opcallf = (BasicExpression) invoke.checkIfSetExpression();
    BinaryExpression obj = 
       new BinaryExpression("->oclAsType", selfvar, new BasicExpression(src)); 

    opcallf.setUmlKind(Expression.UPDATEOP); 
    opcallf.setObjectRef(obj); 
    return opcallf; 
  } 

  public String toString()
  { String res = "";

    if (lazy)
    { res = res + "@lazy\n"; }
    if (primary)
    { res = res + "@primary\n"; }
    if (isAbstract)
    { res = res + "@abstract\n"; }

    res = "rule " + name + "\n" +
       "  transform " + source.getName() + " : " + source.getType() + "\n" +
       "  to ";
    for (int i = 0; i < targets.size(); i++)
    { Attribute trg = (Attribute) targets.get(i);
      res = res + trg.getName() + " : " + trg.getType();
      if (i < targets.size() - 1)
      { res = res + ", "; }
      res = res + "\n    ";
    }

    if (guard != null) 
    { res = res + "  { guard: " + guard + "\n  " +
         body + "  }\n";
    } 
    else 
    { res = res + "  {\n  " +
         body + "  }\n";
    } 
    
    return res;
  }

  public boolean isPrimary()
  { return primary; }

  public boolean isLazy()
  { return lazy; }

  public boolean hasOutputType(Entity trg)
  { boolean res = false;
    for (int i = 0; i < targets.size(); i++)
    { Attribute att = (Attribute) targets.get(i);
      Type t = att.getType();
      if (t.isEntity())
      { Entity et = t.getEntity(); 
        if (et == trg || Entity.isAncestor(trg,et))
        { return true; }
      }
    }
    return res;
  }

  public Constraint toConstraint(Vector entities, Vector types)
  { // guard => T1->exists( t1 | ... Tn->exists( tn | AddToTrace & self.$name(t1,...,tn) ) ... )

    String srcname = source.getName();
    Type srct = source.getType();
    Entity srcent = srct.getEntity();

    String opcall = "self.$" + name + "(";
   
    Entity trent = (Entity) ModelElement.lookupByName("$Trace", entities);
    Entity outent = (Entity) ModelElement.lookupByName("$OUT", entities);
    Entity inent = (Entity) ModelElement.lookupByName("$IN", entities); 

    if (trent == null) { return null; } 

    if (outent == null) { return null; } 

    if (inent == null) { return null; } 

    Type trentt = new Type(trent);
    Type outentt = new Type(outent);
    Type intype = new Type(inent); 

    Expression trentexp = new BasicExpression(trent);
    trentexp.setUmlKind(Expression.CLASSID);
    // trentexp.setType(new Type("Set", null));
    trentexp.setElementType(trentt);
    // trentexp.setEntity(tent);
    Expression tracevar = new BasicExpression("$trace");
    tracevar.setUmlKind(Expression.ROLE);
    tracevar.setElementType(trentt);
    tracevar.setType(new Type("Sequence", null));
    tracevar.setMultiplicity(ModelElement.MANY);

    // trent, outent needed
    // trent = (Entity) ModelElement.lookupByName("$Trace", entities);
    // outent = (Entity) ModelElement.lookupByName("$OUT", entities);

    Vector opparams = new Vector();

    Expression add2targets = new BasicExpression(true);

    for (int i = 0; i < targets.size(); i++)
    { Attribute trg = (Attribute) targets.get(i);
      String tname = trg.getName();
      opcall = opcall + tname;
      if (i < targets.size() - 1)
      { opcall = opcall + ","; }
      Expression tvar = new BasicExpression(trg);
      tvar.setUmlKind(Expression.VARIABLE);
      tvar.setType(trg.getType());
      tvar.setElementType(trg.getType());

      opparams.add(tvar);
      Expression trvar = new BasicExpression("$" + tname);
      trvar.setUmlKind(Expression.VARIABLE);
      trvar.setType(trentt);
      trvar.setElementType(trentt);
      Expression intent = new BinaryExpression(":", trvar, trentexp);
      Expression trintrace = new BinaryExpression(":", trvar, tracevar);
      BasicExpression targetf = new BasicExpression("target");
      targetf.setUmlKind(Expression.ROLE);
      targetf.setMultiplicity(ModelElement.ONE);
      targetf.setElementType(outentt);
      targetf.setType(outentt);
      targetf.setObjectRef(trvar);
      Expression targeteq = new BinaryExpression("=", targetf, tvar);
      BinaryExpression conj = new BinaryExpression("&", trintrace, targeteq);
      Expression add2target = new BinaryExpression("#", intent, conj);
      add2target.setBrackets(true);
      add2targets = Expression.simplifyAnd(add2targets,add2target);
    }
    opcall = opcall + ")";
    BasicExpression invoke = new BasicExpression(opcall,0); 
    Expression opcallf = invoke.checkIfSetExpression();
    opcallf.setUmlKind(Expression.UPDATEOP); 
    add2targets = Expression.simplifyAnd(add2targets, opcallf);

    for (int j = targets.size() - 1; j >= 0; j--)
    { Attribute trg = (Attribute) targets.get(j);
      String tname = trg.getName();  
      Type ttype = trg.getType();
      Expression tvar = (Expression) opparams.get(j);
      Expression texp = new BasicExpression(ttype + "");
      texp.setUmlKind(Expression.CLASSID);
      texp.setType(new Type("Set", null));
      texp.setElementType(ttype);
      Expression intent = new BinaryExpression(":", tvar, texp);
      add2targets = new BinaryExpression("#", intent, add2targets);
    }

    Expression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(srcent);
    selfexp.setType(srct);
    selfexp.setElementType(srct);

    if (body == null) 
    { body = new SequenceStatement(); } 
    
    Statement opcode = body.substituteEq(srcname, selfexp);
      opcode.setEntity(srcent); 

      BehaviouralFeature bf = new BehaviouralFeature("$" + name);
      bf.setParameters(targets);
      bf.setEntity(srcent);
      srcent.addOperation(bf);
      Vector env = new Vector(); 
      env.addAll(targets); 
      opcode.typeCheck(types,entities,env); 
      bf.setActivity(opcode); 
    
    Expression newguard = guard.substituteEq(srcname, selfexp);

    Constraint con = new Constraint(newguard,add2targets);
    con.setOwner(srcent);  
    return con; 
  } 

  public void toOperation(Vector entities, Vector types)
  { // operation name with postcondition 
    // guard => T1->exists( t1 | ... Tn->exists( tn | AddToTrace & self.$name(t1,...,tn) & result = t1 ) ... )

    String srcname = source.getName();
    Type srct = source.getType();
    Entity srcent = srct.getEntity();

    String opcall = "self.$" + name + "(";
   
    Entity trent = (Entity) ModelElement.lookupByName("$Trace", entities);
    Entity outent = (Entity) ModelElement.lookupByName("$OUT", entities);
    Entity inent = (Entity) ModelElement.lookupByName("$IN", entities); 

    Type trentt = new Type(trent);
    Type outentt = new Type(outent);
    Type intype = new Type(inent); 

    Expression trentexp = new BasicExpression(trent);
    trentexp.setUmlKind(Expression.CLASSID);
    // trentexp.setType(new Type("Set", null));
    trentexp.setElementType(trentt);
    // trentexp.setEntity(tent);
    Expression tracevar = new BasicExpression("$trace");
    tracevar.setUmlKind(Expression.ROLE);
    tracevar.setElementType(trentt);
    tracevar.setType(new Type("Sequence", null));
    tracevar.setMultiplicity(ModelElement.MANY);

    // trent, outent needed
    // trent = (Entity) ModelElement.lookupByName("$Trace", entities);
    // outent = (Entity) ModelElement.lookupByName("$OUT", entities);

    Vector opparams = new Vector();

    Expression add2targets = new BasicExpression(true);
    Type resType = null; 
    Expression reseq = new BasicExpression(true); 

    if (targets.size() > 0) 
    { Attribute firsttarg = (Attribute) targets.get(0); 
      resType = firsttarg.getType(); 
      String resultname = firsttarg.getName();  
      BasicExpression resultvar = new BasicExpression(resultname); 
      resultvar.setUmlKind(Expression.VARIABLE);
      resultvar.setType(resType);
      resultvar.setElementType(resType);
      BasicExpression resvar = new BasicExpression("result"); 
      resvar.setUmlKind(Expression.VARIABLE);
      resvar.setType(resType);
      resvar.setElementType(resType);
      reseq = new BinaryExpression("=", resvar, resultvar); 

      for (int i = 0; i < targets.size(); i++)
      { Attribute trg = (Attribute) targets.get(i);
        String tname = trg.getName();
        opcall = opcall + tname;
        if (i < targets.size() - 1)
        { opcall = opcall + ","; }
        Expression tvar = new BasicExpression(trg);
        tvar.setUmlKind(Expression.VARIABLE);
        tvar.setType(trg.getType());
        tvar.setElementType(trg.getType());

        opparams.add(tvar);
        Expression trvar = new BasicExpression("$" + tname);
        trvar.setUmlKind(Expression.VARIABLE);
        trvar.setType(trentt);
        trvar.setElementType(trentt);
        Expression intent = new BinaryExpression(":", trvar, trentexp);
        Expression trintrace = new BinaryExpression(":", trvar, tracevar);
        BasicExpression targetf = new BasicExpression("target");
        targetf.setUmlKind(Expression.ROLE);
        targetf.setMultiplicity(ModelElement.ONE);
        targetf.setElementType(outentt);
        targetf.setType(outentt);
        targetf.setObjectRef(trvar);
        Expression targeteq = new BinaryExpression("=", targetf, tvar);
        BinaryExpression conj = new BinaryExpression("&", trintrace, targeteq);
        Expression add2target = new BinaryExpression("#", intent, conj);
        add2target.setBrackets(true);
        add2targets = Expression.simplifyAnd(add2targets,add2target);
      }
    } 

    opcall = opcall + ")";
    BasicExpression invoke = new BasicExpression(opcall,0); 
    Expression opcallf = invoke.checkIfSetExpression();
    opcallf.setUmlKind(Expression.UPDATEOP); 
    add2targets = Expression.simplifyAnd(add2targets, opcallf);
    add2targets = Expression.simplifyAnd(add2targets, reseq);

    for (int j = targets.size() - 1; j >= 0; j--)
    { Attribute trg = (Attribute) targets.get(j);
      String tname = trg.getName();  
      Type ttype = trg.getType();
      Expression tvar = (Expression) opparams.get(j);
      Expression texp = new BasicExpression(ttype + "");
      texp.setUmlKind(Expression.CLASSID);
      texp.setType(new Type("Set", null));
      texp.setElementType(ttype);
      Expression intent = new BinaryExpression(":", tvar, texp);
      add2targets = new BinaryExpression("#", intent, add2targets);
    }

    Expression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(srcent);
    selfexp.setType(srct);
    selfexp.setElementType(srct);

    if (body == null) 
    { body = new SequenceStatement(); } 

    Statement opcode = body.substituteEq(srcname, selfexp);
    opcode.setEntity(srcent); 

    BehaviouralFeature bf = new BehaviouralFeature("$" + name);
    bf.setParameters(targets);
    bf.setEntity(srcent);
    srcent.addOperation(bf);
    Vector env = new Vector(); 
    env.addAll(targets); 
    opcode.typeCheck(types,entities,env); 
    bf.setActivity(opcode); 

    
    Expression newguard = guard.substituteEq(srcname, selfexp);
    BehaviouralFeature bfop = new BehaviouralFeature(name);
    // bf.setParameters(targets);
    bfop.setEntity(srcent);
    srcent.addOperation(bfop);
    Vector env1 = new Vector(); 
    // env.addAll(targets); 
    // opcode.typeCheck(types,entities,env); 
    bfop.setPre(newguard); 
    bfop.setPost(add2targets); 
    bfop.setType(resType); 
  } 

  public void getCallGraph(Map res)
  { if (body == null) 
    { return; } 
    
    Vector bfcalls = body.allOperationsUsedIn(); 
    for (int j = 0; j < bfcalls.size(); j++) 
    { res.add_pair(name, bfcalls.get(j)); } 
  } 

  public int analyseEFO(Map m) 
  { // count the number of different targets of calls from the map
    Set calls = new HashSet(); 
    for (int i = 0; i < m.elements.size(); i++) 
    { Maplet mm = (Maplet) m.get(i); 
      calls.add(mm.getTarget() + ""); 
    } 
    int res = calls.size(); 

    System.out.println("*** Rule " + getName() + " has fan-out = " + res); 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    if (guard != null) 
    { res.addAll(guard.equivalentsUsedIn()); } 
    if (body != null) 
    { res.addAll(body.equivalentsUsedIn()); }  
    return res; 
  } 
}

