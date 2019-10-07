import java.util.Vector; 
import java.io.*; 
// import expression.*; 

/** Invariant is superclass of all forms of RSDS invariant. */ 
/* package: OCL */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

abstract class Invariant 
{ private boolean system = true; 
  private boolean critical = false; 
  private boolean behavioural = true; 
  private boolean ordered = false; 
  private Expression orderedBy = null; 

  Expression actionForm;
  String eventName;  /* For Operational and some Temporal invariants only */ 
  String ownerText; 

  /* Possible modalities of invariants. Equal to Expression codes
     if occur in both. */ 
  public static final int SENSEN = 0; 
  public static final int SENACT = 1; 
  public static final int SENACTACT = 2; 
  public static final int ACTACT = 3; 
  public static final int HASEVENT = 4; 
  public static final int OTHER = 5; 
  public static final int ERROR = 10;
  /* For operational invariants, sensor event is ignored in this 
     modality assignment. */ 

  public int modality = OTHER; 

  public abstract void display(); 
  
  public abstract void display(PrintWriter out);

  public void setSystem(boolean b) 
  { system = b; } 

  public void setCritical(boolean b) 
  { critical = b; } 

  public void setBehavioural(boolean b)
  { behavioural = b; }

  public void setOrdered(boolean b)
  { ordered = b; }

  public void setOrderedBy(Expression ordby)
  { orderedBy = ordby; }

  public void setEventName(String e)
  { eventName = e; } 

  public void setOwnerText(String t)
  { ownerText = t; } 

  public boolean isSystem() 
  { return system; } 

  public boolean isCritical() 
  { return critical; } 

  public boolean isBehavioural()
  { return behavioural; } 

  public boolean isOrdered()
  { return ordered; } 

  public Expression getOrderedBy()
  { return orderedBy; } 

  public String getEventName() 
  { return eventName; } 

  public boolean hasSensorEvent(Vector sms)
  { return false; } // overriden in OperationalInvariant

  public boolean inconsistentWith(Invariant inv, Vector vars, 
                                  Vector sms)
  { if (inv instanceof SafetyInvariant) 
    { return inconsistentWith((SafetyInvariant) inv,vars,sms); } 
    else if (inv instanceof OperationalInvariant)
    { return inconsistentWith((OperationalInvariant) inv,vars,sms); } 
    else if (inv instanceof TemporalInvariant)
    { return inconsistentWith((TemporalInvariant) inv,vars,sms); } 
    return false; 
  } 

  protected abstract boolean inconsistentWith(SafetyInvariant inv, 
                                              Vector vars, 
                                              Vector sms); 

  protected abstract boolean inconsistentWith(OperationalInvariant inv,
                                              Vector vars, Vector sms); 

  protected abstract boolean inconsistentWith(TemporalInvariant inv, 
                                              Vector vars, Vector sms); 

  public void smvDisplay()
  { System.out.println("  AG(" + toSmvString() + ")"); } 

  public void smvDisplay(Vector assumps)
  { System.out.print("  AG(");  
    if (assumps.size() == 0) 
    { System.out.println(toSmvString() + 
                         ")"); } 
    else 
    { System.out.println(); 
      for (int i = 0; i < assumps.size(); i++) 
      { Invariant assump = (Invariant) assumps.get(i);
        System.out.print("    AG(" + assump.toSmvString() + ")");
        if (i < assumps.size() - 1) 
        { System.out.println(" &"); } 
        else 
        { System.out.println("  ->"); } 
      }
      System.out.println("        (" + toSmvString() + "))"); 
    }
  }

  public void smvDisplay(PrintWriter out)
  { out.println("  AG(" + toSmvString() + ")"); }

  public void smvDisplay(Vector assumps, PrintWriter out)
  { out.print("  AG(");  
    if (assumps.size() == 0) 
    { out.println(toSmvString() + ")"); }
    else 
    { out.println(); 
      for (int i = 0; i < assumps.size(); i++) 
      { Invariant assump = (Invariant) assumps.get(i);
        out.print("    AG(" + assump.toSmvString() + ")");
        if (i < assumps.size() - 1) 
        { out.println(" &"); } 
        else 
        { out.println("  ->"); } 
      }
      out.println("        (" + toSmvString() + "))");
    }
  }

  public abstract String toString(); 

  public abstract String toJava(); 

  public abstract String toSmvString(); 

  public abstract String toB(); 

  public Vector anteReadFrame()
  { Expression ante = antecedent(); 
    if (ante != null) 
    { return ante.allReadFrame(); }
    return null; 
  } 

  public Vector readFrame()
  { Expression ante = antecedent(); 
    Expression succ = succedent(); 
    if (ante != null && succ != null) 
    { return VectorUtil.union(ante.allReadFrame(),succ.readFrame()); }
    return null; 
  } 

  public Vector wr(Vector assocs)
  { // Expression ante = antecedent(); 
    Expression succ = succedent(); 
    if (succ != null) 
    { return succ.wr(assocs); }
    return null; 
  } 

  public Invariant toSmv(Vector cnames)
  { Expression ante = antecedent(); 
    Expression succ = succedent(); 
    Expression newante = ante.toSmv(cnames); 
    Expression newsucc = succ.toSmv(cnames); 
    Invariant inv = (Invariant) clone(); 
    inv.replaceAntecedent(newante); 
    inv.replaceSuccedent(newsucc); 
    inv.setSystem(isSystem()); 
    inv.setCritical(isCritical()); 
    return inv; 
  } 

  public abstract Expression antecedent(); 

  public abstract Expression succedent(); 

  public abstract Expression filterSuccedent(Vector vg);

  public abstract void replaceSuccedent(Expression e);

  public abstract void replaceAntecedent(Expression e); 
 
  public abstract Object clone(); 

  public boolean equals(Object o) 
  { if (o instanceof Invariant) 
    { Invariant inv = (Invariant) o; 
      return (toString().equals(inv.toString())); } 
    else 
    { return false; } 
  } 

  static public OperationalInvariant opInvFromSafety(String e,
                 Expression newE, Expression oldE,
                 Expression assump, Expression conc, String comp)
  { Expression cond;
    if (oldE == assump)
    { cond = newE; }
    else 
    { cond = assump.substitute(oldE,newE); }
    return new OperationalInvariant(e,cond,conc,comp); }

  abstract public void createActionForm(Vector v);

  public void setActionForm(final Expression e)
  { actionForm = e; }

  public Expression getActionForm()
  { return actionForm; }

  abstract public boolean hasLHSVariable(String s);

  public boolean hasVariable(String var) 
  { boolean res = antecedent().hasVariable(var); 
    if (res) { return true; } 
    res = succedent().hasVariable(var); 
    return res; 
  } 

  abstract public Vector lhsVariables(final Vector vars);

  abstract public Vector rhsVariables(final Vector vars);

  public static Vector variablesUsedInAllLhs(Vector invs,
                                           Vector cnames)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      Vector vars = inv.lhsVariables(cnames);
      res = VectorUtil.union(res,vars);
    }
    return res;
  }

  public Vector lhsComponents(final Vector sms)
  { Vector lhs = antecedent().componentsUsedIn(sms); 
    return Named.getNames(lhs); 
  } 

  public Vector rhsComponents(final Vector sms)
  { Vector rhs = succedent().componentsUsedIn(sms);
    return Named.getNames(rhs);
  }

  public Vector getDependencies(final Vector sms)  
  /* For operational, also include component 
     the event comes from */ 
  { Vector lefts = lhsComponents(sms);  // just component names, not atts
    Vector rights = rhsComponents(sms); // or instances. 
    Vector deps = new Vector();

    for (int i = 0; i < lefts.size(); i++)
    { String ll = (String) lefts.elementAt(i);
      for (int j = 0; j < rights.size(); j++)
      { String rr = (String) rights.elementAt(j);
        deps.add(new Maplet(ll,rr)); 
      } 
    }
    return deps; 
  }

  public Maplet getClump(Vector sms)  
  /* For operational, also include component 
     the event comes from */ 
  { /* Assumes only one variable on RHS */ 
    Vector lefts = lhsComponents(sms);
    Vector rights = rhsComponents(sms);
    Maplet clump = new Maplet(lefts,rights);
    return clump; 
  }

  public Vector normalise(Vector components) 
  { Vector res = new Vector(); 
    Expression succ = succedent(); 
    Expression ante = antecedent(); 
    Vector succs = succ.splitAnd(components); 
    Vector antes = ante.splitOr(components); 

    if (antes == null || antes.size() == 0) 
    { System.out.println("Vacuous (false) antecedent " + ante + 
                         " Generating no normalised forms!"); 
      return res; 
    }

    for (int i = 0; i < succs.size(); i++) 
    { Expression e = (Expression) succs.elementAt(i);
      for (int j = 0; j < antes.size(); j++) 
      { Expression ea = (Expression) antes.get(j);  
        Invariant inv2 = (Invariant) clone(); 
        // inv2.replaceSuccedent(e);
        // inv2.replaceAntecedent(ea);  
        inv2.refactor(ea,e); 
        res.add(inv2); 
      } 
    }

    Vector finalRes = new Vector(); 

    for (int j = 0; j < res.size(); j++) 
    { Invariant inv = (Invariant) res.get(j); 
      Vector minvs = inv.expandMultiples(components); 
      finalRes.addAll(minvs); 
    } 
    return finalRes; 
  } 

  private Vector expandMultiples(Vector sms)
  { Vector res = new Vector(); 
    Expression newleft =
      antecedent().expandMultiples(sms);
    Expression newright =
      succedent().expandMultiples(sms);
    Vector rights = newright.splitAnd(sms);
    for (int i = 0; i < rights.size(); i++)
    { Expression re = (Expression) rights.get(i);
      Invariant inv2 = (Invariant) clone();
      inv2.replaceAntecedent(newleft);
      inv2.replaceSuccedent(re);
      res.add(inv2);
    }
    return res;
  }

  public boolean typeCheck(final Vector types, final Vector entities,
                           final Vector contexts, final Vector env)
  { Expression ante = antecedent();
    Expression succ = succedent();
    boolean ta = ante.typeCheck(types,entities,contexts,env); 
    boolean tb = succ.typeCheck(types,entities,contexts,env);
    int antem = ante.maxModality(); 
    int succm = succ.minModality(); 
    System.out.println("Modalities are: " + antem + " => " + succm); 
    if (succm == ModelElement.SEN)  // must be !behavioural
    { behavioural = false; } 
    else if (succm < antem) 
    { behavioural = false; }  // ACT => SEN, etc.
    else 
    { behavioural = succ.isUpdateable(); } 
    System.out.println(this + " is behavioural: " + behavioural);  
    return ta && tb; 
  } 

  public boolean typeCheck(Vector sms)
  { Expression ante = antecedent();
    Expression succ = succedent();
    int ta = ante.typeCheck(sms);
    int ts = succ.typeCheck(sms);
    System.out.print("Modality is: "); 

    if (ta == Expression.ERROR || 
        ts == Expression.ERROR)
    { modality = ERROR;
      System.out.println("ERROR");
      System.out.println(" for invariant " + toString());  
      return false; 
    }
    if (ta == Expression.HASEVENT || 
        ts == Expression.HASEVENT) 
    { modality = HASEVENT; 
      System.out.println("This must be a temporal invariant" + 
                         " with an event in it: \n" + toString()); 
      if (this instanceof TemporalInvariant) 
      { System.out.println("HASEVENT"); 
        modality = HASEVENT; 
      } 
      else 
      { System.out.println("ERROR");
        modality = ERROR; 
        return false; 
      } 
    } 
    else if (ta == Expression.SENSOR && 
             ts == Expression.ACTUATOR)
    { modality = SENACT;
      System.out.println("SENACT"); 
    }
    else if (ta == Expression.SENSOR &&
             ts == Expression.SENSOR)
    { modality = SENSEN; 
      System.out.println("SENSEN"); 
    }
    else if (ta == Expression.MIXED &&
             ts == Expression.ACTUATOR)
    { modality = SENACTACT; 
      System.out.println("SENACTACT"); 
    }
    else if (hasSensorEvent(sms) && 
             ta == Expression.ACTUATOR &&
             ts == Expression.ACTUATOR)
    { modality = SENACTACT;
      System.out.println("SENACTACT");
    }
    else if (ta == Expression.ACTUATOR &&
             ts == Expression.ACTUATOR)
    { modality = ACTACT; 
      System.out.println("ACTACT"); 
    }
    else 
    { modality = OTHER; 
      System.out.println("OTHER"); 
      System.out.println("Non-standard invariant! " + toString()); 
    }
    System.out.println(" for invariant " + toString());  
    return true; 
  }

  public boolean convertableToOp() 
  { return ((modality == SENACT || 
             modality == SENACTACT) && 
            isSystem()); 
  } 

  public boolean actactInv()
  { return ((modality == ACTACT || 
             modality == SENACTACT) &&
            isSystem()); 
  } 

  abstract public void checkSelfConsistent(Vector componentNames); 

  public abstract Invariant derive(SafetyInvariant inv, Vector componentNames); 

  public Invariant filterForPhase(String var, String val, Vector vars)
  { Expression ante = antecedent().substituteEq(var,new BasicExpression(val)); 
    Expression e1 = ante.simplify(vars); 
    if (e1.equalsString("false"))  /* invariant not relevant for phase */ 
    { return null; } 
    else 
    { Invariant inv = (Invariant) clone(); 
      inv.replaceAntecedent(e1); 
      return inv; 
    } 
  } // Ignore events on var itself. 

  public Invariant simplify(Vector comps)
  { Invariant res = (Invariant) clone();
    Expression newante = antecedent().simplify(comps);
    Expression newsucc = succedent().simplify(comps);
    res.replaceAntecedent(newante);
    res.replaceSuccedent(newsucc); 
    /* res.typeCheck(comps);    Need simpleTypeCheck() */ 
    return res; }

  public boolean isTrivial()
  { Expression ante = antecedent(); 
    Expression succ = succedent(); 
    if (ante.implies(succ))
    { System.out.println("Trivial:: " + this); 
      return true; 
    } 
    // also if ante is inconsistent
    System.out.println("Not trivial:: " + this); 
    return false; 
  } 

  public Invariant substituteEq(String oldvar, Expression newval)
  { Invariant res = (Invariant) clone(); 
    Expression newante = antecedent().substituteEq(oldvar,newval);
    Expression newsucc = succedent().substituteEq(oldvar,newval);
    res.replaceAntecedent(newante);
    res.replaceSuccedent(newsucc); 
    return res; 
  }

  abstract public void saveData(PrintWriter out); 

  public static Vector deriveStableState(Vector snames, 
                                         String[] vals,
                                         Vector invs)
  { Vector res = invs;
    
    // for (int ind = 0; ind < vals.length; ind++) 
    // { System.out.print(vals[ind] + " "); }
    // System.out.println("\n-----------------");

    for (int i = 0; i < snames.size(); i++)
    { String var = (String) snames.get(i);
      BasicExpression val =
        new BasicExpression(vals[i]);
      Vector newinvs = substituteEqAll(var,val,res);
      res = (Vector) newinvs.clone();
    }
    // displayAll(res); 
    // System.out.println(); 

    return res; 
  }

  public static Vector substituteEqAll(String var, 
                Expression val,
                final Vector invs)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      Invariant inv2 = inv.substituteEq(var,val); 
      res.add(inv2); 
    }
    return res;
  }

  public static Vector simplifyAll(Vector invs, 
                                   final Vector vars)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      Invariant inv2 = inv.simplify(vars); 
      res.add(inv2); 
    }
    return res;
  }

  public static void displayAll(final Vector invs) 
  { System.out.println("Invariants: "); 
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      inv.display(); 
    }
  }

  public static void displayAll(final Vector invs, PrintWriter out)
  { out.println("Invariants: "); 
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      inv.display(out);
    }
  }


  public static Vector getStaticInvs(final Vector invs)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i); 
      if (inv instanceof SafetyInvariant)
      { res.add(inv); }
    }
    return res;
  }

  public static Vector getStaticParts(final Vector invs) 
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      SafetyInvariant sinv = new SafetyInvariant(inv.antecedent(), 
                                                 inv.succedent()); 
      sinv.setActionForm(inv.getActionForm()); 
      res.add(sinv); 
    } 
    return res; 
  } 

  public static Vector getStaticSystemInvs(final Vector invs)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i); 
      if ((inv instanceof SafetyInvariant) &&
          inv.isSystem())
      { res.add(inv); }
    }
    return res;
  }

  public static Vector getConvertableToOpInvs(Vector invs)
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      if ((inv instanceof SafetyInvariant) &&
          inv.convertableToOp())
      { res.add(inv); }
    }
    return res;
  }

  public static boolean isSatisfiable(final Vector invs)
  { boolean res = true;
    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      Expression ante =
        inv.antecedent();
      Expression succ =
        inv.succedent();
    
      if (succ.equalsString("false") &&
          ante.equalsString("true"))
      { res = false;
        return false; 
      }
    }
    return res;
  }


  public static Invariant evaluateInvAt(Invariant inv,
                                        Vector mapping, Vector allvars)
  { for (int i = 0; i < mapping.size(); i++)
    { Maplet setting = (Maplet) mapping.get(i);
      String var = (String) setting.source;
      Object obj = setting.dest;
      String val = obj.toString(); 
      BasicExpression be =
        new BasicExpression(val);
      Invariant inv1 = inv.substituteEq(var,be);
      inv = inv1.simplify(allvars);
    }
    return inv;
  }


  private static Maplet deriveSettings(Vector vars, 
                                       final Vector invs)
  { Vector res = new Vector();
    Vector removals = new Vector();
    Vector substitutions = new Vector();

    for (int i = 0; i < invs.size(); i++)
    { Invariant inv = (Invariant) invs.get(i);
      Expression ante =
          inv.antecedent();
      Expression succ =
          inv.succedent();
    
      if (ante.equalsString("false"))
      { removals.add(inv); }
      else if (succ.equalsString("true"))
      { removals.add(inv); }
      else if (ante.equalsString("true"))
      { Vector vuse = 
          succ.variablesUsedIn(vars);
        if (vuse.size() == 1)
        { String var = (String) vuse.get(0);
          Expression val = 
            succ.getSettingFor(var);
          removals.add(inv);
          substitutions.add(new Maplet(var,val));
        }
      }
    }

    Vector temp = (Vector) invs.clone();
    temp.removeAll(removals);

    for (int j = 0; j < substitutions.size(); j++)
    { Maplet mm = (Maplet) substitutions.get(j);
      String vv = (String) mm.source;
      Expression vl = (Expression) mm.dest;
      res = substituteEqAll(vv,vl,temp);
      temp = (Vector) res.clone();
    }
    return new Maplet(substitutions,temp);
  }

  public static Maplet deriveStateInvs(final Vector invars, 
                                       Vector vars)
  { Vector temp = simplifyAll(invars,vars);
    Vector invs = new Vector();
    Vector substitutions = new Vector();
    Vector newinvs = new Vector();

    Maplet mm = deriveSettings(vars,temp);
    Vector subs = (Vector) mm.source;
    newinvs = (Vector) mm.dest;

    
    while (subs.size() > 0)
    { temp = simplifyAll(newinvs,vars);
      Vector newsubs = 
        VectorUtil.union(subs,substitutions); 
      substitutions = (Vector) newsubs.clone();

      // System.out.println("Current substitutions are: " + subs);
      // displayAll(temp);
      // System.out.println();

      Maplet mm2 = deriveSettings(vars,temp);
      subs = (Vector) mm2.source;
      newinvs = (Vector) mm2.dest;
    }
    invs = (Vector) newinvs.clone();
    return new Maplet(substitutions,
                      invs);
  }

  public Statement convert2code(Vector vars)
  { Expression ante = antecedent();
    Expression succ = succedent();
    Expression rhs = succ.createActionForm(vars);  // must be sms. 
    Statement is = 
      new InvocationStatement(rhs.toString(),null,null);
    /* Need to get target (2nd arg) for Java */
    if (ante.equalsString("true"))
    { return is; }
    else
    { IfStatement ifs = new IfStatement();
      ifs.addCase(ante, is);
      return ifs;
    }
  } 

  private void refactor(Expression left,
                        Expression right)
  { if (right instanceof BinaryExpression)
    { BinaryExpression ber = (BinaryExpression) right;
      if (ber.operator.equals("=>"))
      { Expression newleft =
          Expression.simplifyAnd(left,ber.left);
        Expression newright = ber.right;
        refactor(newleft,newright);
      }
      else
      { replaceAntecedent(left);
        replaceSuccedent(right);
      }
    }
    else
    { replaceAntecedent(left);
      replaceSuccedent(right);
    }
  } // for TemporalInvriant don't re-arrange at all.

}


/* ********************************************** */

/** SafetyInvariant represents static (single state) invariants. */
 
class SafetyInvariant extends Invariant
{ Expression assumption;
  Expression conclusion; 

  public SafetyInvariant(Expression a, Expression c)
  { assumption = a;
    conclusion = c; 
  }

  public Object clone() 
  { Expression newA = (Expression) assumption.clone(); 
    Expression newC = (Expression) conclusion.clone(); 
    Invariant inv = new SafetyInvariant(newA,newC); 
    inv.modality = modality; 
    inv.setSystem(isSystem()); 
    inv.setCritical(isCritical()); 

    if (actionForm != null)
    { inv.setActionForm((Expression) actionForm.clone());
    }

    return inv; 
  } 

  public void display()
  { System.out.println(toString()); }

  public void display(PrintWriter out)
  { out.println(toString()); }

  public String toString() 
  { String res = assumption.toString() + "  =>  " + conclusion.toString();
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  } 

  public String toB()
  { String res = assumption.toB() + "  =>  " + conclusion.toB();
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  } 

  public String toJava()
  { String res = assumption.toJava() + "  =>  " + conclusion.toJava();
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  }


  public String toSmvString() 
  { return assumption.toString() + "  ->  " + conclusion.toString(); } 

  public void saveData(PrintWriter out)
  { out.println("Safety Invariant:");
    out.println(assumption); 
    out.println(conclusion); }

  public boolean hasLHSVariable(String s)
  { return assumption.hasVariable(s); }

  public Vector lhsVariables(Vector vars)
  { return assumption.variablesUsedIn(vars); }

  public Vector rhsVariables(Vector vars)
  { return conclusion.variablesUsedIn(vars); }

  public void createActionForm(Vector componentNames)
  { System.err.println("Safety Invariant: action form generated"); 
    actionForm = conclusion.createActionForm(componentNames); 
  } 

  public Expression filterSuccedent(Vector vg) 
  { return conclusion.filter(vg); }
  
  public void replaceSuccedent(Expression e)
  { conclusion = e; }   /* Recalculate modality */ 

  public void replaceAntecedent(Expression e) 
  { assumption = e; }   /* Recalculate modality */ 

  public Expression antecedent() 
  { return assumption; } 
  
  public Expression succedent() 
  { return conclusion; } 

  protected boolean inconsistentWith(SafetyInvariant inv,
                                     Vector vars, Vector sms)
  { Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();

    if (succ.conflictsWith(succ2,vars))
    { if (ante.conflictsWith(ante2,vars))
      { return false; }
      return true;
    }
    return false;
  }

  protected boolean inconsistentWith(OperationalInvariant inv,
                                     Vector vars, Vector sms)
  { String ev = inv.getEventName();
    String var = inv.getComponentName();
    Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();

    Maplet mm1 = ante.findSubexp(var);
    Maplet mm2 = ante2.findSubexp(var);
    Expression cond1 = (Expression) mm1.dest;  /* ante : var = x & cond1 */
    System.out.println("Cond1 is: " + cond1); 
    Expression cond2 = (Expression) mm2.dest;  /* ante2: var = y & cond2 */
    System.out.println("Cond2 is: " + cond2); 

    if ((cond1 == null || cond1.modality == Expression.SENSOR) && 
        (cond2 == null || cond2.modality == Expression.SENSOR)) 
    { if (succ.conflictsWith(succ2,vars))
      { if (cond1 != null && cond2 != null &&
            cond1.conflictsWith(cond2,vars))
        { return false; }
        else 
        { Expression val = ante.getSettingFor(var);
          System.out.println("val is: " + val); 
          if (val == null) /* var not in ante */
          { return true; }
          else  /*  var = val  in ante */
          { Statemachine sm =
              (Statemachine) VectorUtil.lookup(var,sms);
            return sm.targetOf(val,ev);
          } /* var = val compat. with ev */
        } 
      }
    }
    return false;
  }

  protected boolean inconsistentWith(TemporalInvariant inv,
                                     Vector vars, Vector sms)
  { return inv.inconsistentWith(this,vars,sms); } 

  public Invariant derive(SafetyInvariant inv, Vector componentNames) 
  { /* Assume both are SENACT or SENACTACT modality */ 
    Expression invante = inv.antecedent(); 
    Vector vars = conclusion.variablesUsedIn(componentNames); 
    if (vars.size() == 1) 
    { String var = (String) vars.elementAt(0);   /* an actuator */ 
      Maplet mm = invante.findSubexp(var); 
      if (mm != null && mm.source != null)   /* var occurs in invante */ 
      { Expression val = conclusion.getSettingFor(var);  /* var = val */ 
        Expression invval = ((Expression) mm.source).getSettingFor(var); 
        if (val.equals(invval))   /* match between this and inv */ 
        { Expression e = Expression.simplify("&",assumption,(Expression) mm.dest, componentNames); 
          SafetyInvariant res = new SafetyInvariant(e,inv.succedent()); 
          return res; } 
          /* res.simpleTypeCheck(componentNames); */
      } 
    } 
    return null; 
  } 

  private static Vector generateConverses(Expression ante,
                            Expression succ, Vector actuators)
  { Vector res = new Vector();
    Expression newsucc = ante.computeNegation4succ(actuators);
    System.out.println("Negation of " + ante + " is: " + newsucc); 
    Vector newantes = succ.computeNegation4ante(actuators);
    for (int i = 0; i < newantes.size(); i++)
    { Expression e = (Expression) newantes.elementAt(i);
      SafetyInvariant si = new SafetyInvariant(e,newsucc);
      res.add(si); }
    return res; }  /* Type check each si */ 

  private Vector generateAllConverses(final String actvar, 
                                      final Vector actuators) 
  { Vector res = new Vector(); 
    Maplet mm = assumption.findSubexp(actvar);
    if (mm != null && mm.source != null)
    { Vector converses = 
        generateConverses((Expression) mm.source, conclusion, actuators);
      for (int i = 0; i < converses.size(); i++)
      { SafetyInvariant si = (SafetyInvariant) converses.elementAt(i);
        SafetyInvariant sinew = si.addToAntecedent((Expression) mm.dest);
        res.add(sinew); 
      } 
    }
    return res; 
  }
 
  public Vector expandWithConverses(final Vector actuators, 
                                    final Vector components)
  { /* Assume it is ACTACT or SENACTACT &
       1 formula on RHS */
    Vector res = new Vector();
    Vector actuatorNames = VectorUtil.getNames(actuators); 
    Vector actvars = assumption.variablesUsedIn(actuatorNames);
    // System.out.println("Variables used in " + assumption + " are: " + actvars);
 
    // if (actvars.size() > 0)
    // { String actvar = (String) actvars.elementAt(0);
      // Maplet mm = assumption.findSubexp(actvar);
      // if (mm != null && mm.source != null)
      // { Vector converses = generateConverses((Expression) mm.source, conclusion, actuators);
      //  for (int i = 0; i < converses.size(); i++)
      //  { SafetyInvariant si = (SafetyInvariant) converses.elementAt(i);
      //    SafetyInvariant sinew = si.addToAntecedent((Expression) mm.dest);
      //    res.add(sinew); } } }

    for (int i = 0; i < actvars.size(); i++) 
    { String actvar = (String) actvars.get(i); 
      Vector cons = generateAllConverses(actvar,actuators); 
      res = VectorUtil.vector_merge(res,cons); 
    } 
    return res; 
  }  


  private static Vector generateContrapositives(Expression ante,
                                                Expression succ)
  { Vector res = new Vector();
    Expression newsucc = ante.computeNegation4succ();
    System.out.println("Negation of " + ante + " is: " + newsucc); 
    Vector newantes = succ.computeNegation4ante();
    for (int i = 0; i < newantes.size(); i++)
    { Expression e = (Expression) newantes.elementAt(i);
      SafetyInvariant si = new SafetyInvariant(e,newsucc);
      res.add(si);
    }
    return res;
  }  /* Type check each si. ACTSEN etc invs must be !behavioural */


  public static Vector genAllContrapositives(SafetyInvariant inv)
  { Vector res = new Vector(); 
    Expression ante = inv.antecedent(); 
    Expression succ = inv.succedent(); 
    Vector conjs = ante.allConjuncts(); 
    for (int i = 0; i < conjs.size(); i++) 
    { Vector fconj = (Vector) conjs.get(i); 
      Expression factor = (Expression) fconj.get(0);
      Expression rem = (Expression) fconj.get(1); 
      if (factor != null) 
      { Vector contras = generateContrapositives(factor,succ); 
        for (int j = 0; j < contras.size(); j++) 
        { SafetyInvariant contra = (SafetyInvariant) contras.get(j); 
          SafetyInvariant newinv = contra.addToAntecedent(rem); 
          res.add(newinv); 
        } // need to type check and modality assign each of these
      }
    } 
    return res; 
  } 
         
  private SafetyInvariant addToAntecedent(Expression e)
  { if (e == null  ||  e.equalsString("true"))
    { return this; }
    else
    { Expression newante = new BinaryExpression("&",e,assumption);
      return new SafetyInvariant(newante,conclusion); 
    }
  }  /* type check this new invariant */ 

  public void checkSelfConsistent(Vector vars)
  { boolean b = assumption.selfConsistent(vars); 
    if (!b) 
    { System.out.println("Error in assumption of invariant " + toString());
      modality = ERROR;  
      return; }  
    Vector lefts = assumption.variablesUsedIn(vars);
    Vector rights = conclusion.variablesUsedIn(vars);
    Vector inter = VectorUtil.intersection(lefts,rights);
    if (inter.size() > 0)
    { System.out.println("Error: same variable on LHS and RHS of: " + toString()
);
      modality = ERROR; }
  }

  // public Invariant filterForPhase(String var, String val)
  // { Expression v2 = assumption.getSettingFor(var); 
  //   if (v2 == null || v2.equalsString(val))
  //   { return this; } 
  //   else 
  //   { return null; } 
  // } 

  public static SafetyInvariant transitiveComp(
                                  SafetyInvariant i1,
                                  SafetyInvariant i2)
  { Expression ante1 = i1.antecedent();
    Expression ante2 =
      (Expression) i2.antecedent().clone();
    Expression succ1 = 
      (Expression) i1.succedent().clone();
    Expression succ2 = i2.succedent();
    // if i2 has event, return null:
    if (i2.getEventName() != null) { return null; }
    Vector comms =
      Expression.commonConjSubformula(succ1,ante2);
    if (comms.get(0) == null) { return null; }
    Expression rem2 = (Expression) comms.get(2);
    Expression ante = 
      Expression.simplify("&",ante1,rem2,new Vector());
    SafetyInvariant res = 
      new SafetyInvariant(ante,succ2);
    res.setEventName(i1.getEventName());
    // its critical if i1 or i2 are?
    // system if i1 or i2 are?
    return res;
  }

  public static SafetyInvariant transitiveComp2(
                                  SafetyInvariant i1,
                                  SafetyInvariant i2)
  { Expression ante1 = i1.antecedent();
    Expression ante2 =
      (Expression) i2.antecedent().clone();
    Expression succ1 = 
      (Expression) i1.succedent().clone();
    Expression succ2 = i2.succedent();
    // if i2 has event, return null:
    if (i2.getEventName() != null) { return null; }

    if (succ1 instanceof BinaryExpression)
    { BinaryExpression bsucc1 = (BinaryExpression) succ1;
      if (bsucc1.operator.equals("=") &&
          bsucc1.left instanceof BasicExpression)
      { BasicExpression var = (BasicExpression) bsucc1.left;
        Expression newante = ante2.substituteEq(var.toString(),
                                                bsucc1.right);
        Expression newsucc = succ2.substituteEq(var.toString(),
                                                bsucc1.right); 
        Expression ante = 
          (new BinaryExpression("&",ante1,newante)).simplify();
        SafetyInvariant res = 
          new SafetyInvariant(ante,newsucc);
        res.setEventName(i1.getEventName());
    // its critical if i1 or i2 are?
    // system if i1 or i2 are?
        return res;
      }  // other cases for l : r, l <: r, l /: r, l /<: r
    }
    return null;
  }

  public static SafetyInvariant transitiveComp3(
                                  SafetyInvariant i1,
                                  SafetyInvariant i2)
  { Expression ante1 = i1.antecedent();
    Expression ante2 =
      (Expression) i2.antecedent().clone();
    Expression succ1 = 
      (Expression) i1.succedent().clone();
    Expression succ2 = i2.succedent();
    // if i2 has event, return null:
    if (i2.getEventName() != null) { return null; }

    if (succ1.implies(ante2))
    { SafetyInvariant res = new SafetyInvariant(ante1,succ2);
      res.setEventName(i1.getEventName());
      return res; 
    }
    return null;
  }

  public Statement toStatement() // tries to implement it as a use case phase
  { BehaviouralFeature bf = 
      new BehaviouralFeature("op", new Vector(), false, null); 
    bf.setPost(succedent()); 
    InvocationStatement invokeop = new InvocationStatement("s.op()",null,null); 
    invokeop.setCallExp(new BasicExpression("s.op()")); 
    Statement stat = new IfStatement(antecedent(),invokeop,
                           new InvocationStatement("skip",null,null));
    return stat; 
  } 
}

