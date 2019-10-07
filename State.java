/*
      * Classname : State
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the logical class for the 
      * states in the statechart diagram.

   package: Statemachine
*/

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*;
import java.util.Vector;


abstract class State extends Named implements Serializable
{
  //if state is intial then = TRUE
  boolean initial;
  boolean excluded = false; 
  public static final int LOOPSTATE = 1; 
  public static final int IFSTATE = 2;  // at least 2 outgoing transitions
  public static final int FINALSTATE = 3; // no outgoing transitions 
  public int stateKind = 0; 

  private Maplet properties; 
  private Vector sensorSettings = new Vector(); // sen |-> val for controller
  private Expression entryAction = null; 

  //The list of transitions from and to the state
  Vector trans_from = new Vector();  // of Transition
  Vector trans_to = new Vector();    // of Transition

  public State(String id)
  {  
    super(id); 
    System.out.println("-- STATE created");
  }
  
  public String get_label()
  {
    return label;
  }

  public Expression getEntryAction()
  { return entryAction; } 

  public boolean isInitial() 
  { return initial; } 

  public void setProperties(Maplet p) 
  { properties = p; } 

  public void setStateKind(int kind) 
  { stateKind = kind; } 

  public void setEntryAction(Expression e)
  { entryAction = e; } 

  public Maplet getProperties() 
  { return properties; } 

  public void setExcluded(boolean b) 
  { excluded = b; } 

  public boolean isExcluded() 
  { return excluded; } 

  public void clearOutgoingTransitions()
  { trans_from.clear(); } 

  public void clearIncomingTransitions()
  { trans_to.clear(); } 

  public void addOutgoingTransition(Transition tr) 
  { trans_from.add(tr); } 

  public void addIncomingTransition(Transition tr) 
  { trans_to.add(tr); } 

  public void removeTransitionFrom(Transition t)
  { trans_from.remove(t); } 

  public void removeTransitionTo(Transition t)
  { trans_to.remove(t); } 

  public Vector outgoingTransitions()
  { return trans_from; } 

  public Vector incomingTransitions()
  { return trans_to; } 

  public void addInvariant(Expression inv)
  { Vector invs; 
    if (properties == null) 
    { invs = new Vector(); 
      properties = new Maplet(new Vector(),invs); 
    }
    else 
    { invs = (Vector) properties.dest; } 
    invs.add(inv); 
  }  

  public Expression getInvariant()
  { Vector invs = new Vector(); 
    if (properties == null) 
    { return null; }
    else 
    { invs = (Vector) properties.dest; } 
    if (invs == null || invs.size() == 0)
    { return null; } 
    
    return (Expression) invs.get(0);  
  }  // really conjunction of them
    
  public void setSensorSettings(Vector sNames, String[] sVals)
  { sensorSettings = new Vector();
    int n = sNames.size(); 
    for (int i = 0; i < n; i++) 
    { Maplet mm = new Maplet(sNames.get(i),sVals[i]); 
      sensorSettings.add(mm); 
    } 
    System.out.println(sensorSettings);
  } 

  public Vector getSensorSettings() 
  { return sensorSettings; } 

  public Vector generatedInvariants()
  { Vector res = new Vector();
    Expression ante =
      VectorUtil.mapletsToExpression(sensorSettings);
    Maplet mm = getProperties();
    if (mm == null) 
    { return res; } 

    Vector actSettings = (Vector) mm.source;
    if (actSettings == null) 
    { return res; } 

    for (int i = 0; i < actSettings.size(); i++)
    { Maplet setting = (Maplet) actSettings.get(i);
      String act = (String) setting.source;
      Expression val = (Expression) setting.dest;
      Expression succ =
        new BinaryExpression("=",
          new BasicExpression(act),val);
      Invariant inv = new SafetyInvariant(ante,succ);
      res.add(inv);
    }
    Vector invs = (Vector) mm.dest;
    for (int j = 0; j < invs.size(); j++)
    { Invariant inv2 = (Invariant) invs.get(j);
      Expression ante2 = inv2.antecedent();
      Expression succ2 = inv2.succedent();
      if (succ2.equalsString("false") || ante2.equalsString("false") ||
          succ2.equalsString("true") || ante2.equalsString("true")) { } 
      else 
      { Expression ante3 =
          Expression.simplify("&",ante,ante2,null);
        Invariant inv3 =
          new SafetyInvariant(ante3,succ2);
        res.add(inv3);
      } 
    }
    return res;
  }

  public String evaluateInv(Invariant inv, Vector allvars)
  { Invariant inv2;
    Invariant inv1 =
      Invariant.evaluateInvAt(inv,sensorSettings,allvars);
    Maplet mm = getProperties();
    if (mm != null)
    { Vector subs = (Vector) mm.source;
      inv2 = Invariant.evaluateInvAt(inv1,subs,allvars);
    }
    else
    { inv2 = inv1; }
    Expression ante = inv2.antecedent();
    Expression succ = inv2.succedent();
    System.out.println("At state " + label +
      " property is simplified to: " + inv2);
    if (succ.equalsString("true") ||
        ante.equalsString("false"))
    { return label + " satisfies " + inv; }
    else if (succ.equalsString("false"))
    { return label + " does not satisfy " + inv; }
    else
    { return label + " cannot determine truth of " +
             inv;
    }
  }

  public boolean satisfies(Expression e, Vector allvars)
  { Expression e2;
    Expression e1 =
      Expression.evaluateExpAt(e,sensorSettings,
                               allvars);
    Maplet mm = getProperties();
    if (mm != null)
    { Vector subs = (Vector) mm.source;
      e2 = Expression.evaluateExpAt(e1,subs,allvars);
    }
    else
    { e2 = e1; }
    System.out.println("At state " + label +
      " property " + e + " is simplified to: " + e2);
    if (e2.equalsString("true"))
    { return true; }
    else
    { return false; }
  }

  private static Statement convert2code(Vector subs, 
                                 Vector invs,
                                 Vector vars)
  { SequenceStatement ss = new SequenceStatement();
    for (int i = 0; i < subs.size(); i++)
    { Maplet mm = (Maplet) subs.get(i);
      Expression var = 
        new BasicExpression((String) mm.source);
      Expression val = 
        new BasicExpression((String) mm.dest);
      AssignStatement as = new AssignStatement(var,val);
      ss.addStatement(as); 
    }

    for (int j = 0; j < invs.size(); j++)
    { Invariant inv = (Invariant) invs.get(j);
      Statement stat = inv.convert2code(vars);   // sms
      ss.addStatement(stat); 
    }
    return ss;
  }

  abstract public Statemachine flatten(); /*  { return null; } */ 

  abstract public BasicState my_initial(); /*  { return null; } */ 

  public boolean stateCompleteness(Vector actnames)
  { boolean res = false;
    Maplet mm = getProperties();
    if (mm == null)
    { return false; }  // no settings
    if (mm.source == null)
    { return false; }
    Vector subs = (Vector) mm.source;
    Vector subacts = new Vector();
    
    for (int i = 0; i < subs.size(); i++)
    { Maplet sub = (Maplet) subs.get(i);
      subacts.add(sub.source);
    }
    return VectorUtil.subset(actnames,subacts);
  }

  public boolean stateConsistency()
  { Maplet mm = getProperties();
    if (mm == null)
    { return true; }
    if (mm.source == null)
    { return true; }
    Vector subs = (Vector) mm.source;
    boolean conflict =
      VectorUtil.hasConflictingMappings(subs);
    return !conflict;
  }

  public Vector mergeState(State s)
  { // make transitions between this and s into self
    // transitions of this, also for self trans of s:
    for (int i = 0; i < trans_from.size(); i++)
    { Transition tr = (Transition) trans_from.get(i);
      if (tr.target == s)
      { tr.target = this; }
    }
    for (int i = 0; i < trans_to.size(); i++)
    { Transition tr = (Transition) trans_to.get(i);
      if (tr.source == s)
      { tr.source = this; }
    }
    for (int i = 0; i < s.trans_from.size(); i++)
    { Transition tr = (Transition) s.trans_from.get(i);
      if (tr.target == s)
      { tr.target = this; 
        tr.source = this;
      }
    }
    // delete any other transition from s.
    Vector deleted = new Vector();
    for (int i = 0; i < s.trans_from.size(); i++)
    { Transition tr = (Transition) s.trans_from.get(i);
      if (tr.target != this && tr.target != s)
      { deleted.add(tr); }
    }
    for (int i = 0; i < s.trans_to.size(); i++)
    { Transition tr = (Transition) s.trans_to.get(i);
      if (tr.source != this && tr.source != s)
      { deleted.add(tr); }
    }

    return deleted;
  }

  public boolean canMerge(State s)
  { boolean res = true;
    
    // all transitions between this and s have no actions:
    
    for (int i = 0; i < trans_from.size(); i++)
    { Transition tr = (Transition) trans_from.get(i);
      if (tr.target == s)
      { if (tr.generations == null || tr.generations.size() == 0) { }
        else { return false; }
      }
    }
    for (int i = 0; i < trans_to.size(); i++)
    { Transition tr = (Transition) trans_to.get(i);
      if (tr.source == s)
      { if (tr.generations == null || tr.generations.size() == 0) { }
       else { return false; }
     }
    }
    for (int i = 0; i < s.trans_from.size(); i++)
    { Transition tr = (Transition) s.trans_from.get(i);
      if (tr.target == s)
      { if (tr.generations == null || tr.generations.size() == 0) { }
        else { return false; }
      }
    }
   // all other transitions from s must be identical to one
   // from this:
    for (int i = 0; i < s.trans_from.size(); i++)
    { Transition tr = (Transition) s.trans_from.get(i);
      if (tr.target != this && tr.target != s)
      { if (tr.similarToAny(trans_from)) { }
        else { return false; }
      }
    }
   // all other transitions to s must be identical to one
   // to this:
    for (int i = 0; i < s.trans_to.size(); i++)
    { Transition tr = (Transition) s.trans_to.get(i);
      if (tr.source != this && tr.source != s)
      { if (tr.similarToAny(trans_to)) { }
        else { return false; }
      }
    }
    return res;
  }   

}

class BasicState extends State
{
   BasicState(String name)
   {  super(name); }

   public Object clone()
   { return new BasicState(label); } 

   public Statemachine flatten()
   { Vector evs = new Vector(0);
     Vector sts = new Vector(1);
     Vector trs = new Vector(0);
     sts.addElement(this);
     Statemachine res = new Statemachine(this,evs,trs,sts);
     return res; 
   }

  public BasicState my_initial()
  {  return this; }
}

abstract class CompositeState extends State
{ 
  CompositeState(String name)
  { super(name); } 

  public abstract Statemachine flatten0(); 
} 


class OrState extends CompositeState
{  State init;
   /** internal_transitions include inter-level transitions between 
       different substates of this state, but not those internal to 
       any substates */ 
   Vector internal_transitions;
   Vector substates;
   Vector attributes; 
   Vector events; 

   OrState(String name, State ini, Vector itrans, Vector sstates)
   { super(name);
      init = ini;
      internal_transitions = itrans;
      substates = sstates; 
   }

  public void setAttributes(Vector atts)
  { attributes = atts; } 

  public void setEvents(Vector evs)
  { events = evs; } 

  public Object clone()
  { OrState res = new OrState(label,init,internal_transitions,substates);
    res.setAttributes(attributes); 
    res.setEvents(events); 
    return res; 
  } 

  public void addSubstate(State s)
  { substates.add(s); } 

  public void addTransition(Transition t)
  { internal_transitions.add(t); } 

  public Statemachine flatten0()
  { Statemachine res = 
      new Statemachine(init,events,internal_transitions,substates);
    res.setAttributes(attributes); 
    return res; 
  }


  public Statemachine flatten()
  { return flatten0(); } 

/* 
  { Vector flattened_substates = new Vector(substates.size());
    Vector allstates = new Vector(substates.size());
    Vector alltrans = new Vector(internal_transitions.size());
    Vector allevents = new Vector(internal_transitions.size());
    BasicState myinit = init.my_initial(); 
    Statemachine res;

    for (int i = 0; i < substates.size(); i++)
    { State st = (State) substates.elementAt(i);
       Statemachine sms = st.flatten();
       flattened_substates.addElement(sms); 
       allstates = VectorUtil.vector_append(allstates,sms.states);
       alltrans = VectorUtil.vector_append(alltrans,sms.transitions); 
       allevents = VectorUtil.vector_merge(allevents,sms.events); 
    }

    for (int i = 0; i < internal_transitions.size(); i++)
    { Transition t = (Transition) internal_transitions.elementAt(i);
        State src = t.source;
        State trg = t.target;
        // If src, trg basic, copy t to result: 
        if ((src instanceof BasicState) && 
             (trg instanceof BasicState))
        { alltrans.addElement(t); }
        // If src is OR, trg basic, copy t for each 
        // state of flattened source:
        if ((src instanceof OrState) &&
             (trg instanceof BasicState))
        { Statemachine sm; 
          if (substates.contains(src))
          { int j = substates.indexOf(src);
            sm = (Statemachine) flattened_substates.elementAt(j);
          }
          else 
          { sm = src.flatten(); }
          for (int k = 0; k < sm.states.size(); k++)
          { Transition tk = (Transition) t.clone();
            tk.source = (State) sm.states.elementAt(k);
            alltrans.addElement(tk); 
          }
        }
        // Source basic, target OR: replace t by transition
        // to trg.my_initial(): 
        if ((src instanceof BasicState) &&
             (trg instanceof OrState))
        { Transition tnew = (Transition) t.clone();
            tnew.target = trg.my_initial(); 
        }
        // Otherwise do both things: 
        if ((src instanceof OrState) &&
            (trg instanceof OrState))
        { int j = substates.indexOf(src);
          Statemachine sm = (Statemachine) flattened_substates.elementAt(j);
          for (int k = 0; k < sm.states.size(); k++)
          { Transition tk = (Transition) t.clone();
             tk.source = (State) sm.states.elementAt(k);
             tk.target = trg.my_initial();
             alltrans.addElement(tk); 
          }
        } 
    }
    res = new Statemachine(myinit,allevents,alltrans,allstates);
    res.setAttributes(attributes); 
    return res; 
  } */ 

   public BasicState my_initial()
   {  return init.my_initial(); }
}


class AndState extends CompositeState
{ OrState left;
  CompositeState right;

  public AndState(String name, OrState lft, CompositeState rht)
  { super(name);
    left = lft;
    right = rht; 
  }

  public Object clone()
  { return new AndState(label,left,right); } 

  public BasicState compound(State s1, State s2)
  { String nn = s1.label + "," + s2.label;
     BasicState res = new BasicState(nn);
     return res; 
  }

  public Transition compound(Maplet mm)
  { Transition t1 = (Transition) mm.source;
     Transition t2 = (Transition) mm.dest;
     BasicState src = compound(t1.source,t2.source);
     BasicState trg = compound(t1.target,t2.target);
     String nme = t1.label + "," + t2.label;
     Transition res = new Transition(nme,src,trg,t1.event);
     return res; 
  }

  public Map get_sync_trans(Vector ltrans, Vector rtrans)
  { Map res = new Map();
     for (int i = 0; i < ltrans.size(); i++)
     { Transition t1 = (Transition) ltrans.elementAt(i);
        for (int j = 0; j < rtrans.size(); j++)
        {
           Transition t2 = (Transition) rtrans.elementAt(j);
           if (t1.event.label.equals(t2.event.label))
           { res.add_pair(t1,t2); } 
        } 
     }
     return res;
  }

  public boolean not_synchd(Transition t, State s, Map strans)
  { for (int i = 0; i < strans.elements.size(); i++)
    { Maplet mm = (Maplet) strans.elements.elementAt(i);
      if (mm.source == t && ((Transition) mm.dest).source == s)
      { return false; } 
    }
    return true;
  }

  public Statemachine flatten0()
  { return flatten(); } 

  public Statemachine flatten()
  { Statemachine fleft = left.flatten0();
     Statemachine fright = right.flatten0();
     int maxsize = fleft.states.size()*fright.states.size();
     Vector allstates = new Vector(maxsize);
     Vector alltrans = new Vector(maxsize);
     Vector allevents = new Vector();
     Vector allatts = new Vector(); 

     // allevents.addAll(fleft.events); 
     // allevents.addAll(fright.events); 
     allatts.addAll(fleft.getAttributes()); 
     allatts.addAll(fright.getAttributes());  

      Map synctrans = get_sync_trans(fleft.transitions,fright.transitions);

      synctrans.print_map();
      for (int i = 0; i < synctrans.elements.size(); i++)
      { Transition tt = compound((Maplet) synctrans.elements.elementAt(i));
         alltrans.addElement(tt); }

      for (int i = 0; i < fleft.states.size(); i++)
      { for (int j = 0; j < fright.states.size(); j++)
         { BasicState s1 = (BasicState)  fleft.states.elementAt(i);
            BasicState s2 = (BasicState)  fright.states.elementAt(j);
            BasicState ts = compound(s1,s2);
            allstates.addElement(ts); } }

       for (int k = 0; k < fleft.transitions.size(); k++)
       { Transition t1 = (Transition) fleft.transitions.elementAt(k);
         if (! synctrans.in_domain(t1))
         { for (int l = 0; l < fright.states.size(); l++)
           {  BasicState s2 = (BasicState) fright.states.elementAt(l);
              BasicState cs = (BasicState) VectorUtil.lookup(t1.source.label + "," + s2.label, allstates);
              BasicState ct = (BasicState) VectorUtil.lookup(t1.target.label + "," + s2.label, allstates);
              Transition nt = new Transition(t1.label + "," + s2.label,cs,ct,t1.event);
              nt.setGenerations(t1.genToString(),null); 

              alltrans.addElement(nt);
              if (allevents.contains(t1.event)) { } 
              else 
              { allevents.add(t1.event); }  
            } 
          } 
        }

       for (int k = 0; k < fright.transitions.size(); k++)
       { Transition t2 = (Transition) fright.transitions.elementAt(k);
         if (! synctrans.in_range(t2))
         { for (int l = 0; l < fleft.states.size(); l++)
           { BasicState s1 = (BasicState) fleft.states.elementAt(l);
             BasicState cs = (BasicState) VectorUtil.lookup(s1.label + "," + t2.source.label, allstates);
             BasicState ct = 
               (BasicState) VectorUtil.lookup(s1.label + "," + t2.target.label, allstates);
             Transition nt = 
               new Transition(s1.label + "," + t2.label,cs,ct,t2.event);
             // Guard
             // Generations
             nt.setGenerations(t2.genToString(),null); 
             
             alltrans.addElement(nt); 
             if (allevents.contains(t2.event)) { } 
             else 
             { allevents.add(t2.event); }
           } 
         } 
       }

       BasicState inita = 
        (BasicState) VectorUtil.lookup(fleft.initial_state.label + "," + fright.initial_state.label,allstates); 

       Statemachine sms = new Statemachine(inita,allevents,alltrans,allstates);
       sms.setAttributes(allatts);
       if (inita != null) 
       { sms.setInitial(inita); } 

       return sms;
   }

  public BasicState my_initial()
  {  BasicState res = compound(left.my_initial(),right.my_initial());
       return res; 
  }
}

