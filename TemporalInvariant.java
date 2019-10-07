import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class TemporalInvariant extends Invariant
{ /* Modality of succedent: */
  private  Vector modalop = new Vector();

  private Expression condition;
  private Expression effect;

  public TemporalInvariant(Expression c, 
                           Expression e, Vector op)
  { condition = c;
    effect = e;
    modalop = op; 
  }

  public Expression getCondition()
  { return condition; } 

  public Expression getEffect() 
  { return effect; } 

  public Vector getModalOp()
  { return modalop; }

  public void setModalOp(Vector val)
  { modalop = val; }

  public boolean expressesGuard()   /* event & P => G;  for FDC */ 
  { boolean res = (modalop.size() == 0);
    res = res && (condition.modality == Expression.HASEVENT) &&
          (effect.modality != Expression.HASEVENT); 
    return res; 
  } 

  public boolean isActionSpec()  /* event & Cond => event';  for CaseTree */ 
  { boolean res = (modalop.size() == 0);
    if (!res) 
    { return false; } 

    res = (condition.modality == Expression.HASEVENT); 
    res = res && (effect instanceof BasicExpression) &&
          ((BasicExpression) effect).isEvent;
    return res; 
  } 

  public boolean isUniversal() 
  { boolean res = (modalop.size() == 0);
    res = res || (modalop.size() == 1 &&
                  ((String) modalop.get(0)).equals("AG") ); 
    return res; 
  } 

  public boolean isScheduleSpec()
  { return ((modality == SENACT || 
             modality == SENACTACT) &&
            modalop.size() == 1 &&
            ((String) modalop.get(0)).equals("AF"));
  }

  protected boolean inconsistentWith(SafetyInvariant inv, 
                                     Vector vars, Vector sms)
  { Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();
  
    if (succ.conflictsWith(succ2,vars))
    { Vector ops1 = getModalOp();
      Vector ops2 = new Vector();
      if (temporalConflict(ops1,ops2))
      { if (ante.conflictsWith(ante2,vars))
        { return false; }
        return true;
      }
    }
    return false;
  }

  protected boolean inconsistentWith(OperationalInvariant inv,
                                     Vector vars, Vector sms)
  { Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();

    if (succ.conflictsWith(succ2,vars))
    { Vector ops1 = getModalOp();
      Vector ops2 = new Vector();
      ops2.add("AX"); // Surely? At least for AX(a = v) ones? 
      if (temporalConflict(ops1,ops2))
      { if (ante.conflictsWith(ante2,vars))
        { return false; }
        return true;
      }
    }
    return false;
  }
  
  protected boolean inconsistentWith(TemporalInvariant inv, 
                                     Vector vars, Vector sms)
  { Expression ante = antecedent();
    Expression ante2 = inv.antecedent();
    Expression succ = succedent();
    Expression succ2 = inv.succedent();
  
    if (succ.conflictsWith(succ2,vars))
    { Vector ops1 = getModalOp();
      Vector ops2 = inv.getModalOp();
      if (temporalConflict(ops1,ops2))
      { if (ante.conflictsWith(ante2,vars))
        { return false; }
        return true;
      }
    }
    return false;
  }


  private boolean temporalConflict(Vector ops1, Vector ops2)
  { int n1 = ops1.size();
    int n2 = ops2.size();
    
    if (n1 == 0 && n2 == 0)
    { return true; }
    if (n1 == 0)
    { return allAG(ops2); }
    if (n2 == 0)
    { return allAG(ops1); }
    /* Both n1 > 0 && n2 > 0 */
    String op1 = (String) ops1.get(0);
    String op2 = (String) ops2.get(0);
    Vector rem1 = (Vector) ops1.clone();
    rem1.remove(0);
    Vector rem2 = (Vector) ops2.clone();
    rem2.remove(0);
    boolean conflict = temporalConflict(rem1, rem2);
    if (conflict)
    { return operatorConflict(op1,op2); }
    return false;
  }
  
  private boolean allAG(Vector ops)
  { for (int i = 0; i < ops.size(); i++)
    { String op = (String) ops.get(i); 
      if (op.equals("AG")) { }
      else 
      { return false; }
    }
    return true;
  }

  private boolean operatorConflict(String op1, String op2)
  { if (op1.charAt(0) == 'E' && op2.equals("AG"))
    { return true; }
    if (op2.charAt(0) == 'E' && op1.equals("AG"))
    { return true; }
    if (op1.equals("AF") && op2.equals("AG"))
    { return true; }
    if (op2.equals("AF") && op1.equals("AG"))
    { return true; }
    if (op1.equals("EX") && op2.equals("AX"))
    { return true; }
    if (op1.equals("AX") && op2.equals("EX"))
    { return true; }
    if ((op1.equals("AX") || op1.equals("AG")) && 
        (op2.equals("AX") || op2.equals("AG")))
    { return true; } 
    return false;
  }  // Also AX conflicts with EG

  public void display()
  { System.out.println(toString()); }

  public void display(PrintWriter out)
  { out.println(toString()); }

  public String toJava() 
  { return toString(); }  // guess
  
  public String toString()
  { String res = condition.toString();
    res = res + "  =>  ";
    for (int i = 0; i < modalop.size(); i++)
    { res = res + (String) modalop.get(i) + "("; }
    res = res + effect.toString();
    for (int i = 0; i < modalop.size(); i++)
    { res = res + ")"; }
 
    if (isCritical())
    { res = res + " /* Critical */"; } 

    if (isSystem())
    { } 
    else 
    { res = res + " /* Environmental */"; } 

    return res;
  }

  public String toB()  // should be within /*  */ 
  { String res = condition.toB();
    res = res + "  =>  ";
    for (int i = 0; i < modalop.size(); i++)
    { res = res + (String) modalop.get(i) + "("; }
    res = res + effect.toB();
    for (int i = 0; i < modalop.size(); i++)
    { res = res + ")"; }

    if (isCritical())
    { res = res + " /* Critical */"; }

    if (isSystem())
    { }
    else
    { res = res + " /* Environmental */"; }

    return res;
  }


  public String toSmvString()
  { String res = condition.toString();
    res = res + "  ->  ";
    for (int i = 0; i < modalop.size(); i++)
    { res = res + (String) modalop.get(i) + "("; }
    res = res + effect.toString();
    for (int i = 0; i < modalop.size(); i++)
    { res = res + ")"; }
    return res;
  }

  public void smvDisplay()
  { System.out.println(toSmvString()); } 

  public void smvDisplay(Vector assumps)
  { System.out.print("  AG(");  
    if (assumps.size() == 0) 
    { System.out.println(toSmvString() + ")"); } 
    else 
    { System.out.println(); 
      for (int i = 0; i < assumps.size(); i++) 
      { Invariant assump = (Invariant) assumps.get(i);
        System.out.print("    AG(" +
                         assump.toSmvString() + ")");
        if (i < assumps.size() - 1) 
        { System.out.println(" &"); } 
        else 
        { System.out.println("  ->"); } 
      }
      System.out.println("        " + toSmvString()
                         + ")"); 
    }
  }

  public void smvDisplay(PrintWriter out)
  { out.println(toSmvString()); } 


  public void smvDisplay(Vector assumps, PrintWriter out)
  { out.print("  AG(");  
    if (assumps.size() == 0) 
    { out.println(toSmvString() + ")"); } 
    else 
    { out.println(); 
      for (int i = 0; i < assumps.size(); i++) 
      { Invariant assump = (Invariant) assumps.get(i);
        out.print("    AG(" +
                  assump.toSmvString() + ")");
        if (i < assumps.size() - 1) 
        { out.println(" &"); } 
        else 
        { out.println("  ->"); } 
      }
      out.println("        " + toSmvString()
                  + ")"); 
    }
  }

  public Expression antecedent()
  { return condition; }

  public Expression succedent()
  { return effect; }

  public Expression filterSuccedent(final Vector vg)
  { return effect.filter(vg); }

  public void replaceSuccedent(final Expression e)
  { effect = e; }

  public void replaceAntecedent(final Expression e)
  { condition = e; }

  public Object clone()
  { Expression newCond = 
      (Expression) condition.clone();
    Expression newEffect = 
      (Expression) effect.clone();
    Vector mops = (Vector) modalop.clone();

    TemporalInvariant inv =
      new TemporalInvariant(newCond,
                            newEffect,
                            mops);
    inv.modality = modality;
    inv.setSystem(isSystem()); 
    inv.setCritical(isCritical()); 

    /* And copy actionForm, eventName */ 
    return inv; }

  public void createActionForm(final Vector cnames)
  { if (modalop.size() > 0)
    { System.err.println("Temporal Invariant -- " +
                         "no action form generated"); }
    else 
    { actionForm = effect.createActionForm(cnames); } 
  } 

  public boolean hasLHSVariable(final String s)
  { return condition.hasVariable(s); }

  public Vector lhsVariables(final Vector vars)
  { return condition.variablesUsedIn(vars); }

  public Vector rhsVariables(final Vector vars)
  { return effect.variablesUsedIn(vars); } 

  public void checkSelfConsistent(final Vector cnames)
  { boolean b = condition.selfConsistent(cnames);
    if (!b)
    { System.err.println("Error in condition of " +
                         "invariant " + toString()); }
  }  /* and for effect? */

  public Invariant derive(SafetyInvariant inv,
                          Vector cnames)
  { if (isUniversal())
    { Expression invante = inv.antecedent();
      Vector vars = effect.variablesUsedIn(cnames);
      if (vars.size() == 1)
      { String var = (String) vars.get(0);
        Maplet mm = invante.findSubexp(var);
        if (mm != null && mm.source != null)
        { Expression myval = 
            effect.getSettingFor(var); // S => AG(var = myval)
          Expression invval = 
            ((Expression) mm.source).getSettingFor(var);
                                 // inv is  var = invval & P => Q
          if (invval.equals(myval))
          { Expression e = 
              Expression.simplify("&",(Expression) mm.dest,
                                condition,null);
            SafetyInvariant res =
              new SafetyInvariant(e,inv.succedent());
            return res;       // S & P => Q
          }
        }
      }
    }
    return null;
  }

  public Invariant derive2(SafetyInvariant inv, 
                           Vector cnames)
  { Expression invsucc = inv.succedent();
    Vector vars = invsucc.variablesUsedIn(cnames);
    if (vars.size() == 1)
    { String var = (String) vars.get(0);
      Maplet mm = condition.findSubexp(var);
      if (mm != null && mm.source != null)
      { Expression val = 
          invsucc.getSettingFor(var);  // inv is P => var = val
        Expression myval =             // var = myval & R => S
          ((Expression) mm.source).getSettingFor(var); 
        if (val.equals(myval))
        { Expression e = 
            Expression.simplify("&",
                              (Expression) mm.dest,
                              inv.antecedent(),null);
          TemporalInvariant res =
            new TemporalInvariant(e,effect,modalop);
          return res;          // P & R => S
        }
      }
    }
    return null;
  }

  public void saveData(PrintWriter out)
  { out.println("Temporal Invariant:");
    out.println(condition);
    out.println(effect);
    for (int i = 0; i < modalop.size(); i++)
    { out.print((String) modalop.get(i) + " "); }
    out.println();
  }

  public Vector getDependencies(final Vector cnames)
  { Vector deps = new Vector();

    if (isUniversal())
    { return super.getDependencies(cnames);
    }
    else 
    { Vector lefts = lhsVariables(cnames);
      Vector rights = rhsVariables(cnames);
      

      for (int i = 0; i < lefts.size(); i++)
      { String ll = (String) lefts.get(i);
        for (int j = 0; j < rights.size(); j++)
        { String rr = (String) rights.get(j);
          deps.add(new Maplet(ll,rr));
          for (int k = j+1; k < rights.size(); k++)
          { String rr2 = (String) rights.get(k);
            deps.add(new Maplet(rr,rr2));
            deps.add(new Maplet(rr2,rr));
          }
        }
      }
    }
    return deps;
  }

  public Vector normalise(Vector components)
  { if (isUniversal())
    { return super.normalise(components); }
    // Do modalities get copied? AG(A & B) should be AG(A) & AG(B)
    Vector res = new Vector();
    Vector antes = condition.splitOr(components);
    
    for (int j = 0; j < antes.size(); j++)
    { Expression ea = (Expression) antes.get(j);
      Invariant inv2 = (Invariant) clone();
      inv2.replaceAntecedent(ea);
      res.add(inv2); 
    }
    return res;
  }

  public static Invariant parseTemporalInv(BufferedReader br)
  { Compiler comp = new Compiler();
    String s;
    Expression eCond;
    Expression eEffect;
    Vector mode = new Vector();

    try { s = br.readLine(); }
    catch(IOException e)
    { System.err.println("Reading temporal invariant condition failed"); 
      return null; }
    comp.lexicalanalysis(s);
    eCond = comp.parse(); 

    try { s = br.readLine(); }
    catch(IOException e)
    { System.err.println("Reading temporal invariant effect failed"); 
      return null; }
    comp.lexicalanalysis(s);
    eEffect = comp.parse();

    try { s = br.readLine(); }
    catch(IOException e)
    { System.err.println("Reading temporal invariant modal op failed"); 
      return null; }
    
    StringTokenizer st = new StringTokenizer(s);
    while (st.hasMoreTokens())
    { String tok = st.nextToken();
      mode.add(tok); }
    
    if (eEffect != null && eCond != null)
    { return new TemporalInvariant(eCond,eEffect,mode); }
    else { return null; }
  }


}







