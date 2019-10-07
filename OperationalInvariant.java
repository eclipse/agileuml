import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/** OperationalInvariant represents action invariants:  ev & Cond => AX(Effect) 
*/

class OperationalInvariant extends Invariant
{
  Expression condition;
  Expression effect;
  boolean eventEffect = false;

  String sensorComponent;
    /* The component that is source of this event */

  public OperationalInvariant(String e, Expression c,
                              Expression ef, String comp)
  { eventName = e;
    condition = c;
    effect = ef;
    sensorComponent = comp;
  }

  public void addPrefix(String dsname)
  { eventName = dsname + eventName;
    sensorComponent = dsname + sensorComponent;
  }

  public String getComponentName()
  { return sensorComponent; }

  public void setEventEffect()
  { eventEffect = true; }

  public boolean hasSensorEvent(Vector sms)
  { Statemachine sens = (Statemachine) VectorUtil.lookup(sensorComponent,sms); 
    if (sens == null) 
    { System.err.println("No component for: " + sensorComponent + 
                         " Something is wrong!"); 
      return false; 
    } 
    if (sens.cType == DCFDArea.SENSOR) 
    { return true; } 
    return false;
  } 

  public Object clone()
  { Expression newCond = (Expression) condition.clone();
    Expression newEffect = (Expression) effect.clone();
    String newEventName = new String(eventName);
    String newCompName = new String(sensorComponent);

    OperationalInvariant inv =
       new OperationalInvariant(newEventName,
                       newCond,newEffect,
                       newCompName);
    inv.modality = modality;   /* Should be */
    inv.setSystem(isSystem());
    inv.setCritical(isCritical());

    if (actionForm != null)
    { inv.setActionForm((Expression) actionForm.clone());
    }
    return inv; 
  }

  public boolean isTwin(String var, OperationalInvariant inv, 
                        Vector cnames) 
  { // asks if I am of form  P & var = val => a = x where inv is of form
    // P & var = val' => a = y (same P). 
    if (equals(inv)) { return false; } 

    Expression succ1 = succedent(); 
    Expression succ2 = inv.succedent(); 
    Vector vars1 = succ1.variablesUsedIn(cnames); 
    Vector vars2 = succ2.variablesUsedIn(cnames); 
    if (vars1.equals(vars2)) { } 
    else 
    { return false; } 
    Expression ante1 = antecedent(); 
    Expression ante2 = inv.antecedent(); 
    Maplet mm1 = ante1.findSubexp(var); 
    Maplet mm2 = ante2.findSubexp(var); 
    Expression vare1 = (Expression) mm1.source; 
    Expression vare2 = (Expression) mm2.source; 
    if (vare1.equals(vare2)) { return false; } 
    return mm1.dest.equals(mm2.dest); 
  }  

  public void display()
  { System.out.println(toString()); }

  public void display(PrintWriter out)
  { out.println(toString()); }

  public void smvDisplay()
  { System.out.println(" AG(" + eventName + " & " + condition.toString() +
                       " -> AX(" + effect.toString() + "))");
  } // and eventEffect case  

  public String toString()
  { String effectString;
    if (eventEffect) { effectString = effect.toString(); }
    else { effectString = "AX(" + effect.toString() + ")"; }

    String res = eventName + " & " + condition.toString() +
           "  =>  " + effectString;
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  }

  public String toB()
  { String effectString;
    if (eventEffect) { effectString = effect.toB(); }
    else { effectString = "AX(" + effect.toB() + ")"; }

    String res = eventName + " & " + condition.toB() +
           "  =>  " + effectString;
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  }

  public String toJava()
  { String effectString;
    if (eventEffect) { effectString = effect.toJava(); }
    else { effectString = "AX(" + effect.toJava() + ")"; }

    String res = eventName + " & " + condition.toJava() +
           "  =>  " + effectString;
    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  }

  public String toSmvString()
  { return eventName + " & " + condition.toString() +
             "  ->  AX(" + effect.toString() + ")";
  }
   // and eventEffect case 


  public void saveData(PrintWriter out)
  { out.println("Operational Invariant:");
    out.println(eventName);
    out.println(condition.toString());
    out.println(effect.toString()); }

  public Expression antecedent()
  { return condition; }

  public Expression succedent()
  { return effect; }

  public boolean hasLHSVariable(String s)
  { return condition.hasVariable(s); }

  public Vector lhsVariables(Vector vars)
  { return condition.variablesUsedIn(vars); }

  public Vector allLhsVariables(Vector vars)
  { Vector res = lhsVariables(vars);
    res.add(sensorComponent);
    return res;
  }

  public Vector rhsVariables(final Vector vars)
  { return effect.variablesUsedIn(vars); }

  protected boolean inconsistentWith(SafetyInvariant inv,
                                     Vector vars, Vector sms)
  { String ev = getEventName();
    String var = getComponentName();
    Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();

    Maplet mm1 = ante.findSubexp(var);
    Maplet mm2 = ante2.findSubexp(var);
    Expression cond1 = (Expression) mm1.dest;  /* ante : var = x & cond1 */
    // System.out.println("Cond1 is: " + cond1); 
    Expression cond2 = (Expression) mm2.dest;  /* ante2: var = y & cond2 */
    // System.out.println("Cond2 is: " + cond2); 

    if ((cond1 == null || cond1.modality == Expression.SENSOR) &&
        (cond2 == null || cond2.modality == Expression.SENSOR))
    { if (succ.conflictsWith(succ2,vars))
      { if (cond1 != null && cond2 != null &&
            cond1.conflictsWith(cond2,vars))
        { return false; }
        else
        { Expression val = ante2.getSettingFor(var);
          // System.out.println("val is: " + val); 
          if (val == null) /* var not in ante2 */
          { return true; }
          else  /*  var = val  in ante2 */
          { Statemachine sm =
              (Statemachine) VectorUtil.lookup(var,sms);
            return sm.targetOf(val,ev);
          } /* var = val compat. with ev */
        }
      }
    }
    return false;
  }


  protected boolean inconsistentWith(OperationalInvariant inv,
                                     Vector vars, Vector sms)
  { String ev2 = inv.getEventName();
    String var2 = inv.getComponentName();
    Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();

    if (eventName.equals(ev2))
    { if (succ.conflictsWith(succ2,vars))
      { if (ante.conflictsWith(ante2,vars))
        { return false; }
        else
        { return true; }
      }
    }
    return false;
  }

  protected boolean inconsistentWith(TemporalInvariant inv,
                                     Vector vars, Vector sms)
  { return inv.inconsistentWith(this,vars,sms); }

  public void createActionForm(final Vector sms)
  { actionForm = effect.createActionForm(sms); }

  public Expression filterSuccedent(final Vector vg)
  { return effect.filter(vg); }

  public void replaceSuccedent(final Expression e)
  { effect = e; }   /* Should recalculate modality */

  public void replaceAntecedent(final Expression e)
  { condition = e; }   /* Should recalculate modality */

  public Vector getDependencies(final Vector vars)
  { Vector lefts = lhsComponents(vars);
    Vector rights = rhsComponents(vars);
    /* Here is where you can define clumps lefts |-> rights */
    Vector deps = new Vector();

    if (sensorComponent != null)
    { lefts.add(sensorComponent); }

    for (int i = 0; i < lefts.size(); i++)
    { String ll = (String) lefts.elementAt(i);
      for (int j = 0; j < rights.size(); j++)
      { String rr = (String) rights.elementAt(j);
        deps.add(new Maplet(ll,rr)); 
      } 
    }
    return deps; 
  }

  public boolean hasVariable(final String var)
  { boolean res = condition.hasVariable(var);
    if (res) { return true; }
    res = effect.hasVariable(var);
    if (res) { return true; }
    return var.equals(sensorComponent);
  }

  public void checkSelfConsistent(final Vector vars)
  { boolean b = condition.selfConsistent(vars);
    if (!b)
    { System.out.println("Error in condition of invariant " + toString());
      modality = ERROR;
      return; 
    }

    if (modality == SENSEN)
    { System.out.println("Error -- action invariant should not have sensor " +
                         "in effect! " + toString());
    } 


    Vector lefts = condition.variablesUsedIn(vars);
    Vector rights = effect.variablesUsedIn(vars);
    Vector inter = VectorUtil.intersection(lefts,rights);
    if (inter.size() > 0)
    { System.out.println("Warning: same variable on LHS and RHS of: " + 
                         toString()); 
      // modality = ERROR;  
    }
  }
  /* Also check that event is able to occur from source state indicated 
     in condition. */

  public Invariant derive(SafetyInvariant inv, Vector componentNames)
  { /* Assume both are SENACT or SENACTACT modality */
    Expression invante = inv.antecedent();
    Vector vars = effect.variablesUsedIn(componentNames);
    if (vars.size() == 1)
    { String var = (String) vars.elementAt(0);   /* an actuator */
      Maplet mm = invante.findSubexp(var);
      if (mm != null && mm.source != null && mm.dest == null)   /* var occurs in invante */
      { Expression val = effect.getSettingFor(var);  /* var = val */
        Expression invval = ((Expression) mm.source).getSettingFor(var);
        if (val != null && val.equals(invval))   /* match between this and inv */
        { Expression e = (Expression) condition.clone();
          OperationalInvariant res = 
            new OperationalInvariant(eventName,e,inv.succedent(),sensorComponent);
          // res.createActionForm(componentNames); 
          return res;
        }
          /* And res.simpleTypeCheck(componentNames); */
      }
    }
    return null;
  }
  /* Only valid if mm.dest == null, really */

  public Invariant filterForPhase(String var, String val, Vector vars) 
  { if (sensorComponent.equals(var))
    { return null; } 
    else 
    { return super.filterForPhase(var,val,vars); } 
  } 

}



