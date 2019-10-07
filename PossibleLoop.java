import java.util.*;

/* Package: Statemachine */
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


class PossibleLoop
{ State s;
  Vector trs = new Vector();
  Vector sts = new Vector();
  State loophead = null;

  PossibleLoop(State ss, Transition tr)
  { // pre: tr.source == ss
    s = ss;
    trs.add(tr);
  }

  public boolean isLoop()
  { if (trs.size() > 0)
    { Transition tr = (Transition) trs.get(trs.size()-1);
      return tr.target == s;
    }
    return false;
  }

  public State endState()
  { if (trs.size() > 0)
    { Transition tr = (Transition) trs.get(trs.size()-1);
      return tr.target;
    }
    return s;
  }

  public State getLoophead()
  { return loophead; } 

  public Vector getStates()
  { return sts; } 

  public boolean extend(Transition tr)
  { if (trs.contains(tr)) 
    { return false; }
    else 
    { trs.add(tr);
      return true;
    }
  }

  public void calculateStates()
  { sts.clear(); 
    sts.add(s);
    for (int i = 0; i < trs.size(); i++)
    { Transition t = (Transition) trs.get(i);
      if (sts.contains(t.target)) { }
      else
      { sts.add(t.target); }
    }
  }

  public boolean checkLoop()
  { loophead = null;
    boolean res = true;
    for (int i = 0; i < sts.size(); i++)
    { State st = (State) sts.get(i);
      Vector outt = st.outgoingTransitions();
      Vector intt = st.incomingTransitions();
      for (int j = 0; j < outt.size(); j++)
      { Transition tt = (Transition) outt.get(j);
        if (sts.contains(tt.target)) { } // add to trs?
        else if (loophead == null)
        { loophead = st; }
        else if (loophead != st) 
        { System.out.println("Error: state " + st + " exits loop " + sts); 
          res = false;
        }
      }
      for (int j = 0; j < intt.size(); j++)
      { Transition tt = (Transition) intt.get(j);
        if (sts.contains(tt.source)) { } // add to trs?
        else if (loophead == null)
        { loophead = st; }
        else if (loophead != st) 
        { System.out.println("Error: state " + st + " enters loop " + sts); 
          res = false;
        }
      }
    }
    return res;
  }

  public String toString()
  { return s + " ==> " + trs; }
}
