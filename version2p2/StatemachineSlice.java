import java.util.*;

/* package: Statemachine */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class StatemachineSlice
{ Statemachine sm;
  State targetState;
  Vector targetVbls = new Vector();
  java.util.Map stateVbls = new HashMap();
  java.util.Map reach = new java.util.HashMap();

  public StatemachineSlice(Statemachine smx, State s,
                      Vector vbls)
  { sm = smx;
    targetState = s;
    targetVbls = vbls;
    Vector sts = sm.states;

    for (int i = 0; i < sts.size(); i++)
    { State st = (State) sts.get(i);
      stateVbls.put(st,new Vector());
    }
    stateVbls.put(s,vbls);
  }

  public void computeReachability()
  { boolean changed = false;
    Vector trs = sm.transitions;
    Vector sts = sm.states;
    
    reach = new java.util.HashMap();
    for (int i = 0; i < sts.size(); i++)
    { State s = (State) sts.get(i);
      Vector v = new Vector();
      v.add(s);
      reach.put(s,v);
    }

    do 
    { changed = false;
      for (int i = 0; i < trs.size(); i++)
      { Transition tr = (Transition) trs.get(i);
        Vector treach = (Vector) reach.get(tr.target);
        Vector sreach = (Vector) reach.get(tr.source);
        if (sreach.containsAll(treach) && 
            sreach.contains(tr.target)) 
        { }
        else 
        { if (sreach.contains(tr.target)) { }
          else 
          { sreach.add(tr.target);
            changed = true;
          }
          for (int j = 0; j < treach.size(); j++)
          { State ss = (State) treach.get(j);
            if (sreach.contains(ss)) { }
            else 
            { sreach.add(ss);
              changed = true;
            }
          }
        }
      }
      System.out.println("Reachability relation: " + reach);
    } while (changed == true);
  }

  public void removeUnreachableStates()
  { Vector sts = sm.states;
    Vector deleted = new Vector();
    
    for (int i = 0; i < sts.size(); i++)
    { State s = (State) sts.get(i);
      Vector v = (Vector) reach.get(s);
      if (v.contains(targetState)) { }
      else 
      { deleted.add(s); }
    }
     
    for (int i = 0; i < deleted.size(); i++)
    { State s = (State) deleted.get(i);
      sm.removeState(s);
      System.out.println("Removing state: " + s);
    }
  }

  public void removeUnreachableStates2(State init)
  { Vector sts = sm.states;
    Vector deleted = new Vector();
    Vector initReach = (Vector) reach.get(init); 

    for (int i = 0; i < sts.size(); i++)
    { State s = (State) sts.get(i);
      if (initReach == null || initReach.contains(s)) { }
      else 
      { deleted.add(s); }
    }
     
    for (int i = 0; i < deleted.size(); i++)
    { State s = (State) deleted.get(i);
      sm.removeState2(s);
      System.out.println("Removing state: " + s);
    }
  }


  public void computeSlice()
  { boolean changed = false;
    Vector allvars = new Vector(); 

    Vector trs = sm.transitions;
    Vector atts = sm.getAllAttributes(); 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String nme = att.getName(); 
      allvars.add(nme); 
    } 
    sm.setupOutgoingIncoming(); 
    computeReachability(); 
    // removeUnreachableStates(); 
    // sm.setupOutgoingIncoming(); 

    do 
    { changed = false;
      for (int i = 0; i < trs.size(); i++)
      { Transition tr = (Transition) trs.get(i);
        Vector tvbls = (Vector) stateVbls.get(tr.target);
        Vector svbls = (Vector) stateVbls.get(tr.source);
        Vector dds = tr.dataDependents(allvars,tvbls,reach,targetState);
        if (svbls.containsAll(dds)) { }
        else 
        { for (int j = 0; j < dds.size(); j++)
          { String var = (String) dds.get(j);
            if (svbls.contains(var)) { }
            else 
            { svbls.add(var);
              changed = true;
            }
          }
        }
      }
      System.out.println("Data dependency: " + stateVbls);
    } while (changed == true);

    transitionsSlice(sm,allvars); 
    mergeTransitions(); 
    // sm.setupOutgoingIncoming(); 
    rmergeStates(); 
    // mergeStates(); 
  } 

  public void transitionsSlice(Statemachine sm, Vector allvars)
  { Vector deleted = new Vector(); 
    Vector trs = sm.transitions;
    for (int i = 0; i < trs.size(); i++)
    { Transition tr = (Transition) trs.get(i);
      Vector tvbls = (Vector) stateVbls.get(tr.target);
      tr.slice(allvars,tvbls);
      // if (tr.isSelfSkipTransition())
      // { deleted.add(tr); } 
    }
    // sm.removeTransitions(deleted); 
  }

  public boolean mergeTransitions()
  { Vector sts = sm.states;
    Vector trans = sm.transitions;
    sm.setupOutgoingIncoming();
    Vector deleted = new Vector();
    boolean res = false;

    for (int i = 0; i < sts.size(); i++)
    { State st = (State) sts.get(i);
      Vector outt = st.trans_from;
      for (int j = 0; j < outt.size(); j++)
      { Transition t1 = (Transition) outt.get(j);
        for (int k = j+1; k < outt.size(); k++)
        { Transition t2 = (Transition) outt.get(k);
          if (t1.mergeTransition(t2))
          { System.out.println("Merging " + t1 + " and " + t2); 
            deleted.add(t2); 
            res = true;
          }
        }
        outt.removeAll(deleted);
        sm.removeTransitions(deleted); 
        deleted = new Vector();
      }
    }
    return res; 
  }

  public void rmergeStates()
  { Vector sts = sm.states;
    Vector trans = sm.transitions;
    sm.setupOutgoingIncoming();
    
    boolean running = true;
    while (running)
    { running = rmergeSearch(sts,trans); }
  }

  public boolean rmergeSearch(Vector states, Vector trans)
  { for (int i = 0; i < states.size(); i++)
    { State s1 = (State) states.get(i);
      for (int j = 0; j < states.size(); j++)
      { State s2 = (State) states.get(j);
        if (s1 != s2 && sameOutgoing(s1,s2))
        { rmergeStates(s1,s2);
          // recalculateInOut();
          return true;
        }
      }
    }
    return false;
  }

  private boolean sameOutgoing(State s1, State s2)
  { boolean res = true;
    Vector outgoing1 = s1.trans_from; 
    for (int i = 0; i < outgoing1.size(); i++)
    { Transition t1 = (Transition) outgoing1.get(i);
      if (hasOutgoingTransition(s2,s1,t1)) {}
      else
      { return false; }
    }
    
    Vector outgoing2 = s2.trans_from; 
    for (int j = 0; j < outgoing2.size(); j++)
    { Transition t2 = (Transition) outgoing2.get(j);
      if (hasOutgoingTransition(s1,s2,t2)) {}
      else
      { return false; }
    }
    System.out.println("States " + s1 + " " + s2 + " match -- merging them"); 
    return res;
  }

      
  private boolean hasOutgoingTransition(State s, State sother, Transition t)
  { Vector outgoing = s.trans_from; 
    for (int i = 0; i < outgoing.size(); i++)
    { Transition tr = (Transition) outgoing.get(i);
      if ((tr.target == t.target || tr.target == s && t.target == sother ||
           tr.target == sother && t.target == s) &&
      (tr.event + "").equals(t.event + "") &&
      (tr.guard + "").equals(t.guard + "") &&
      (tr.generations + "").equals(t.generations + ""))
      { // System.out.println("Transitions " + t + " " + tr + " match"); 
        return true; 
      }
    }
    return false;
  }

  private void rmergeStates(State s1, State s2)
  { // s2 incoming becomes union of the
    // 2 incoming sets, retarget the s1
    // incoming to s2, delete s1 and its 
    // outgoing transitions.
    // Assumes: s1 /= s2

    if (s1.isInitial())
    { sm.setInitial(s2); } 
    
    for (int i = 0; i < s1.trans_to.size(); i++)
    { Transition t1 = (Transition) s1.trans_to.get(i);
      t1.target = s2;
      sm.moveEndpoint(t1,s1,s2); 
      // ld.xend = ld.xend + (s2.x - s1.x);
      // ld.yend = ld.yend + (s2.y - s1.y);
    }
    // sm.deleteState(s1);
    sm.removeState3(s1); 
  }

  /* 
  public void mergeStates()
  { Vector sts = sm.states;
    sm.setupOutgoingIncoming();

    for (int i = 0; i < sts.size(); i++)
    { State st1 = (State) sts.get(i);
      for (int j = i+1; j < sts.size(); j++)
      { State st2  = (State) sts.get(j);
        if (st1.canMerge(st2))
        { sm.removeState(st2);
          Vector dtrans = st1.mergeState(st2); 
          System.out.println("State " + st2 + " merged into " + st1);
          sm.removeTransitions(dtrans);
          System.out.println("Removing transitions: " + dtrans);
          sm.setupOutgoingIncoming();
          j--;   // a bad hack
        }
      }
    }

    sts = new Vector(); 
    sts.addAll(sm.states);
    sm.setupOutgoingIncoming();

    for (int i = 0; i < sts.size(); i++)
    { State ss = (State) sts.get(i); 
      sm.contractPath(ss); 
    } 
  } */ 

  public boolean mergeStateGroup()
  { java.util.Date date1 = new java.util.Date(); 
    long time1 = date1.getTime(); 

    Vector trs = sm.transitions;
    Vector actlesstrs = new Vector();
    Vector sgroups = new Vector();
    Vector lastgroups = new Vector(); 

    sm.setupOutgoingIncoming();

    for (int i = 0; i < trs.size(); i++)
    { Transition tr = (Transition) trs.get(i);
      if (tr.isActionless())
      { actlesstrs.add(tr);
        if (tr.source != tr.target)
        { Vector sg = new Vector();
          sg.add(tr.source);
          sg.add(tr.target);
          if (VectorUtil.containsEqualVector(sg,lastgroups)) {}
          else
          { lastgroups.add(sg); }
        } 
      }
    }
    System.out.println("Actionless transitions: " + actlesstrs); 

    lastgroups = validateStateGroups(lastgroups,actlesstrs);
    if (lastgroups.size() == 0)
    { System.out.println("No candidates for state merging exist");
      return false;
    }
    // System.out.println("Groups: " + sgroups); 
    int s1 = 1; // sgroups.size(); 
    Vector bestg = new Vector(); 

    Vector newgroups = genNewGroups(lastgroups);
    newgroups = validateStateGroups(newgroups,actlesstrs);

    int largest = 0; 
    sgroups.addAll(lastgroups); 
    sgroups.addAll(newgroups); 
    for (int l = 0; l < sgroups.size(); l++) 
    { Vector gp = (Vector) sgroups.get(l); 
      if (gp.size() > largest) 
      { largest = gp.size(); 
        bestg = gp; 
      } 
    } 
    int s2 = largest; // VectorUtil.maxSize(newgrps); // sgroups.size(); 

    while (s2 > s1)
    { s1 = s2; 
      sgroups = genNewGroups(lastgroups,newgroups);
      lastgroups = new Vector(); 
      lastgroups.addAll(newgroups); 
      newgroups = validateStateGroups(sgroups,actlesstrs);
      // System.out.println("New groups: " + newgrps);
      // sgroups = VectorUtil.addAll(sgroups,newgrps);
      /* for (int k = 0; k < newgrps.size(); k++) 
      { Vector vv = (Vector) newgrps.get(k); 
        if (sgroups.contains(vv)) { } 
        else 
        { sgroups.add(vv); } 
      } */ 
      // sgroups = new Vector(); 
   
      largest = 0; 
      for (int l = 0; l < sgroups.size(); l++) 
      { Vector gp = (Vector) sgroups.get(l); 
        if (gp.size() > largest) 
        { largest = gp.size(); 
          bestg = gp; 
        } 
      } 
      s2 = largest; // VectorUtil.maxSize(newgrps); // sgroups.size(); 
      System.out.println("Max size = " + s2); 
      if (s2 >= 9) { break; } 
    }

    Vector bestgroup = new Vector();

    /* Vector subcoll = VectorUtil.allSubvectors(sgroups); 
    int best = 0;
    for (int i = 0; i < subcoll.size(); i++)
    { Vector gg = (Vector) subcoll.get(i);
      if (checkGroupGroup(gg))
      { int score = scoreGroupGroup(gg);
        if (score > best)
        { best = score;
          bestgroup = gg;
        }
      }
    } */

    bestgroup.add(bestg); 

    System.out.println("Best group group is: " + bestgroup);

    mergeGroupGroup(bestgroup); 

    java.util.Date date2 = new java.util.Date(); 
    long time2 = date2.getTime(); 

    System.out.println("TIME taken = " + (time2 - time1)); 

    return bestg.size() > 0; 
  } 

  private Vector validateStateGroups(Vector sgs, Vector altrs)
  { Vector res = new Vector();
    for (int i = 0; i < sgs.size(); i++)
    { Vector grp = (Vector) sgs.get(i);
      if (validGroup(grp,altrs))
      { // System.out.println("Valid group: " + grp); 
        res.add(grp);
      }
      // else 
      // { System.out.println("Invalid group: " + grp); } 
    }
    return res;
  }

  private boolean validGroup(Vector grp, Vector alts)
  { boolean res = true;
    Vector outevents = new Vector();
    Vector gevents = new Vector();

    for (int i = 0; i < grp.size(); i++)
    { State s = (State) grp.get(i);
      Vector vin = s.incomingTransitions();
      for (int j = 0; j < vin.size(); j++)
      { Transition tt = (Transition) vin.get(j);
        if (grp.contains(tt.source))
        { if (alts.contains(tt)) 
          { gevents.add(tt.event + ""); }
          else 
          { return false; }
        }
      }
      Vector vout = s.outgoingTransitions();
      for (int j = 0; j < vout.size(); j++)
      { Transition tt = (Transition) vout.get(j);
        if (grp.contains(tt.target))
        { if (alts.contains(tt)) 
          { gevents.add(tt.event + ""); }
          else 
          { return false; }
        }
        else
        { outevents.add(tt.event + ""); }
      }
    }
    outevents.retainAll(gevents);
    if (outevents.size() == 0)
    { System.out.println("Possible merge group " + grp); }
    else
    { return false; }
    return res;
  }
 
  private Vector genNewGroups(Vector sgroups)
  { Vector res = new Vector();
    for (int i = 0; i < sgroups.size(); i++)
    { Vector sg = (Vector) sgroups.get(i);
      for (int j = i+1; j < sgroups.size(); j++)
      { Vector sg1 = (Vector) sgroups.get(j);
        Vector intersect = new Vector();
        intersect.addAll(sg);
        intersect.retainAll(sg1);
        if (intersect.size() == 0) { }
        else
        { // Vector ng = new Vector();
          // ng.addAll(sg);
          // ng.addAll(sg1);
          Vector ng = VectorUtil.union(sg,sg1); 
          if (VectorUtil.containsEqualVector(ng,res)) { } 
          else 
          { res.add(ng); } 
        }
      }
    }
    return res;
  }

  private Vector genNewGroups(Vector oldgroups, Vector lastgroups)
  { Vector res = new Vector();
    for (int i = 0; i < oldgroups.size(); i++)
    { Vector sg = (Vector) oldgroups.get(i);
      for (int j = 0; j < lastgroups.size(); j++)
      { Vector sg1 = (Vector) lastgroups.get(j);
        Vector intersect = new Vector();
        intersect.addAll(sg);
        intersect.retainAll(sg1);
        if (intersect.size() == 0) { }
        else
        { // Vector ng = new Vector();
          // ng.addAll(sg);
          // ng.addAll(sg1);
          Vector ng = VectorUtil.union(sg,sg1); 
          if (VectorUtil.containsEqualVector(ng,res)) { } 
          else 
          { res.add(ng); } 
        }
      }
    }
    return res;
  }


  boolean checkGroupGroup(Vector v)
  { if (v.size() == 0) { return false; }
    if (v.size() == 1) { return true; }
    for (int i = 0; i < v.size(); i++)
    { Vector g1 = (Vector) v.get(i);
      for (int j = i+1; j < v.size(); j++)
      { Vector g2 = (Vector) v.get(j);
        Vector inters = new Vector();
        inters.addAll(g1);
        inters.retainAll(g2);
        if (inters.size() > 0) { return false; }
      }
    }
    return true;
  }

  int scoreGroupGroup(Vector v)
  { int res = 0;
    for (int i = 0; i < v.size(); i++)
    { Vector g1 = (Vector) v.get(i);
      res = res + g1.size() - 1;
    }
    return res;
  }

  void mergeGroupGroup(Vector v)
  { for (int i = 0; i < v.size(); i++) 
    { Vector grp = (Vector) v.get(i); 
      if (grp.size() < 2) { } 
      else 
      { mergeGroup(grp); 
        sm.setupOutgoingIncoming(); 
      } 
    } 
  } 

  void mergeGroup(Vector grp)
  { State st0 = (State) grp.get(0);   // best to take the most central one
    for (int i = 1; i < grp.size(); i++) 
    { State st1 = (State) grp.get(i); 
      mergeStates(st1,st0); 
    } 
  } 

  private void mergeStates(State s1, State s2)
  { // s2 incoming becomes union of the
    // 2 incoming sets, likewise s2.outgoing, 
    // retarget the s1
    // incoming to s2, resource s1 outgoing from s2, 
    // delete s1. 
    // Assumes: s1 /= s2
    
    if (s1.isInitial())
    { sm.setInitial(s2); } 

    for (int i = 0; i < s1.trans_to.size(); i++)
    { Transition t1 = (Transition) s1.trans_to.get(i);
      t1.target = s2;
      sm.moveEndpoint(t1,s1,s2); 
      // ld.xend = ld.xend + (s2.x - s1.x);
      // ld.yend = ld.yend + (s2.y - s1.y);
    }
    for (int i = 0; i < s1.trans_from.size(); i++)
    { Transition t1 = (Transition) s1.trans_from.get(i);
      t1.source = s2;   // remove duplicated transitions, merge where possible
      sm.moveStartpoint(t1,s1,s2); 
      // ld.xstart = ld.xstart + (s2.x - s1.x);
      // ld.ystart = ld.ystart + (s2.y - s1.y);
    }
    sm.removeState(s1); 
  }

}
