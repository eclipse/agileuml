import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class AbsMorphism 
{ Map statemap;
  Map transmap;
  Map eventmap;
  Statemachine abs; 
  Statemachine conc; 


   AbsMorphism()
   { statemap = new Map();
     transmap = new Map();
     eventmap = new Map();
   }

   public void print_morphism()
   {  System.out.println("Event mapping");
      eventmap.print_map();
      System.out.println("State mapping");
      statemap.print_map();
      System.out.println("Transition mapping");
      transmap.print_map(); 
  }

  public boolean build_map(Statemachine abs, Statemachine conc)
  { this.abs = abs; 
    this.conc = conc; 
    Vector processed = new Vector(conc.states.size());
    Maplet mm = new Maplet(conc.initial_state, abs.initial_state);
    statemap.add_element(mm);
    System.out.println("Mapped " + mm.toString()); 
    if (try_match(conc.initial_state,abs.initial_state,processed,abs,conc))
    { return true; }
    else 
    { statemap.clear();
      eventmap.clear();
      transmap.clear();
      return false;
    }
   }

  public boolean try_match(State currc, State curra, Vector processed, 
                           Statemachine abs, Statemachine conc)
  { if (processed.contains(currc))
    { return true; }
    else 
    { processed.addElement(currc);
      for (int i = 0; i < conc.transitions.size(); i++)
      { Transition t = (Transition) conc.transitions.elementAt(i);
        if (t.source == currc)
        { Transition ta = abs.find_match(t.event,curra);
          if (ta == null)
          { System.out.println("No abstract transition matching " + t); 
            return false;
          }
          else 
          { System.out.println(t.target.label + " -> " + ta.target.label);
            System.out.println(t.label + " -> " + ta.label); 
            if (statemap.in_domain(t.target))
            { State img = (State) statemap.apply(t.target);
              if (img == ta.target)
              { transmap.add_pair(t,ta);
                System.out.println("Mapped " + t + " to " + ta); 
              }
              else 
              { System.out.println("Target of " + ta + 
                                   " is not abstraction of " + t.target); 
                return false;
              } 
            }
            else
            { statemap.add_pair(t.target,ta.target);
              transmap.add_pair(t,ta);
              System.out.println("Mapped " + t + " to " + ta);
            }
            boolean bb = try_match(t.target,ta.target,processed,abs,conc); }
          }
         } 
      }
      return true; /* No transitions from currc, or all succeeded */
   }

  public boolean checkTotality()
  { boolean ok = true; 
    Vector dom = transmap.domain(); 
    Vector trs = conc.getTransitions(); 
    if (dom.containsAll(trs)) {}
    else 
    { ok = false; 
      System.out.println("Some concrete transitions are not given abstractions "); 
    }

    Vector sdom = statemap.domain(); 
    Vector allstates = conc.getStates(); 
    if (sdom.containsAll(allstates)) {}
    else 
    { ok = false; 
      System.out.println("Some concrete states are not given abstractions "); 
    }
    return ok; 
  }

  public boolean checkCompleteness()  // adequacy conditions
  { Vector allabstract = new Vector(); // the abstract states of the map
    for (int i = 0; i < statemap.elements.size(); i++) 
    { Maplet mm = (Maplet) statemap.elements.get(i); 
      State st = (State) mm.dest; 
      if (allabstract.contains(st)) { } 
      else 
      { allabstract.add(st); } 
    } 
    if (allabstract.containsAll(abs.getStates()))
    { System.out.println("State map is complete"); } 
    else 
    { Vector vv = new Vector(); 
      vv.addAll(abs.getStates()); 
      vv.removeAll(allabstract); 
      System.out.println("State map is not complete, states: " + vv + 
                         " have no concrete representation");
      return false;  
    }  
    return true; 
  } 
}

