/*
 * Classname : Statemachine
 * 
 * Version information : 1
 *
 * Date :
 * 
 * Description : This class describes the logical class for the 
 * statechart module - is a concurrent statechart diagram.

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
import java.awt.*;


class Statemachine extends Named 
{ State initial_state = null;
  Vector events = new Vector();
  Vector transitions = new Vector();
  Vector states = new Vector();
  private String attribute = null; 
    // Actuators only may have one, integer, attribute
  private int maxval = 0; // attribute: 0..maxval
  private int multiplicity = 1; 
    // Numbers > 1 indicate Multiple instances of component needed. 

  int cType;  // DCFDArea.SENSOR, ACTUATOR, CONTROLLER

  SmBuilder myTemplate;  
  StatechartArea gui; 
  private Vector variables = new Vector(); // label, or system vars

  // categories of a name in relation to this statemachine: 
  public static int NONE = -1; 
  public static int STATEMACHINE = 0; 
  public static int EVENT = 1; 
  public static int ATTRIBUTE = 2; 
  public static int STATE = 3; 

  // Category of what model element it is for
  public static int CLASS = 1; 
  public static int METHOD = 2; 

  private int describes = 0; 
  private ModelElement modelElement = null; 
  private Vector attributes = new Vector();  // of Attribute

  /* Invariant:  
       initial_state != null -> states.get(0) == initial_state 
     Also -- no duplicates in  events, transitions or states. 
  */ 

  public Statemachine(State ini, Vector evs, Vector trs,
                      Vector sts)
  { super("generated statemachine"); 
    initial_state = ini;
    events = evs;
    transitions = trs;
    states = sts; 
  }

  public Statemachine(String id, StatechartArea sa)
  { super(id);
    gui = sa; 
    System.out.println("-- MODULE " + id + " created");
    variables.add(id); 
  }

  public String getName()
  { if (modelElement != null) { return modelElement.getName(); } 
    return super.getName(); 
  } 

  public int getCategory(String nme) 
  { if (nme == null) 
    { return NONE; } 
    
    if (label.equals(nme)) { return STATEMACHINE; } 

    if (VectorUtil.lookup(nme,events) != null) 
    { return EVENT; } 

    if (nme.equals(attribute)) { return ATTRIBUTE; } 

    if (VectorUtil.lookup(nme,states) != null) 
    { return STATE; } 
    
    return NONE; 
  } 

  public StatechartArea getGui()
  { return gui; } 

  public Vector getAttributes()
  { return attributes; } 

  public void setAttributes(Vector natts)
  { attributes = natts; } 

  public void setcType(int type) 
  { cType = type; 
    if (gui != null && type == DCFDArea.ACTUATOR) 
    { gui.enableExtraMenus(); } 
  }   
    /* DCFDArea.SENSOR, DCFDArea.ACTUATOR or DCFDArea.CONTROLLER */ 

  public void setModelElement(ModelElement me)
  { modelElement = me; 
    if (me != null && (me instanceof Entity))
    { describes = CLASS; 
      gui.disableExtraMenus(); 
    } 
    else if (me != null && (me instanceof BehaviouralFeature))
    { describes = METHOD;
      gui.enableExtraMenus(); 
    } 
  } 

  public ModelElement getModelElement()
  { return modelElement; } 

  public void prefixEvents(String pre) 
  { for (int i = 0; i < events.size(); i++) 
    { Event ee = (Event) events.get(i); 
      ee.addPrefix(pre); 
    } // and the line visuals of the GUI
  }

  public void setEvents(Entity ent)
  { // all setatt, setrole, addrole, remrole events
    Vector res = new Vector(); 
    res.addAll(ent.getDefinedEvents());         // for triggers 
    gui.setInputEvents(res);
    res = new Vector(); 
    res.addAll(ent.getSupplierEvents()); // for generations
    gui.setOutputEvents(res);  
    setModelElement(ent);
  }  

  public void setEvents(BehaviouralFeature bf)
  { // all setatt, setrole, addrole, remrole events
    Vector res = new Vector(); 
    String bfname = bf.getName(); 
    
    res.add(new Event(bfname + "_step"));         // for triggers 
    gui.setInputEvents(res);
    setModelElement(bf);
  }  

  public void addState(State st) 
  { states.add(st); } 

  public void prefixTransitions(String pre) 
  { for (int i = 0; i < transitions.size(); i++) 
    { Transition tr = (Transition) transitions.get(i); 
      tr.addPrefix(pre); 
    } 
  } 

  public boolean isOperationStateMachine()
  { if (modelElement instanceof BehaviouralFeature &&
         modelElement != null)
    { return true; }
    return false;
  }

  public boolean isEntityStateMachine()
  { if (modelElement instanceof Entity &&
         modelElement != null)
    { return true; }
    return false;
  }

  public int getCType()
  { return cType; } 

  public boolean hasNumericStates()
  { boolean res = true; 
    for (int i = 0; i < states.size(); i++) 
    { State st = (State) states.get(i); 
      if (Expression.isNumber(st.label)) { } 
      else 
      { return false; } 
    } 
    return res; 
  }  // If one state is a number, they all should be, and be a range a..b

  public void setAttribute(String nme) 
  { attribute = nme; } 

  public void deleteAttribute()
  { attribute = null; 
    maxval = 0; 
  } 

  public Vector getAllAttributes()
  { Entity e = null; 
    Vector res = new Vector(); 
    res.addAll(attributes); 
    if (modelElement == null) 
    { return res; } 

    if (modelElement instanceof Entity)
    { e = (Entity) modelElement; 
      res.addAll(e.getAttributes()); 
    } 
    else 
    { e = ((BehaviouralFeature) modelElement).getEntity();
      res.addAll(e.getAttributes()); 
      res.addAll(((BehaviouralFeature) modelElement).getParameters()); 
    }
    return res; 
  }  

  public void addAttribute(Attribute att) 
  { attributes.add(att); } 

  public void addAttribute(String nme, String typ, String ini)
  { // if attribute with that name already exists, return an error
    Attribute att = new Attribute(nme,new Type(typ,null),ModelElement.INTERNAL);
    Entity e = null; 
    Vector pars = new Vector(); 

    if (modelElement instanceof Entity)
    { e = (Entity) modelElement; 
      att.setEntity(e);
    } 
    else 
    { e = ((BehaviouralFeature) modelElement).getEntity();
      pars = ((BehaviouralFeature) modelElement).getParameters(); 
      att.setEntity(e);
    } 

    Vector ents = e.getSuppliers(); 
    ents.add(e); 

    if (ini != null && !ini.equals(""))
    { Compiler comp = new Compiler();
      comp.lexicalanalysis(ini); 
      Expression initExp = comp.parse();
      if (initExp == null) 
      { System.err.println("Invalid initialisation expression: " + ini); } 
      else      // can use expressions in the method parameters and the class
      { initExp.typeCheck(new Vector(),ents,pars); 
        Expression ie2 = initExp.simplify(); 
        String iqf = ie2.queryForm(new java.util.HashMap(),true);
        System.out.println("Initialisation: " + iqf);
        att.setInitialValue(iqf); 
        att.setInitialExpression(ie2); 
      }
    }
    attributes.add(att); 
  } 

  public String getAttribute()
  { return attribute; } 

  public Vector getTransitions()
  { return transitions; } 

  public void setVariables(Vector vars)
  { variables = vars; } 

  public void checkRefinement()
  { if (modelElement instanceof Entity)
    { Entity e = (Entity) modelElement; 
      e.findRefinement(); 
    } 
  } 

  public void setMaxval(int val) 
  { maxval = val; } 

  public int getMaxval() 
  { return maxval; } 

  public Vector getRange(String comp) 
  { if (comp.equals(label))
    { // get the set of values corresponding to my states
      return Named.getNames(states); 
    } 
    else if (comp.equals(attribute))
    { return Named.namesOfNumberRange(0,maxval); } 
    else 
    { return new Vector(); } 
  } 

  public Vector allVariables()  // All the variables connected to this SM
  { Vector res = new Vector(); 

    if (multiplicity > 1) 
    { for (int i = 1; i <= multiplicity; i++) 
      { res.add(label + i + "." + label); } 
    } 
    else 
    { res.add(label); } 

    if (attribute != null) 
    { 
      if (multiplicity > 1) 
      { for (int i = 1; i <= multiplicity; i++) 
        { res.add(label + i + "." + attribute); }
      } 
      else 
      { res.add(attribute); } 
    }
    return res; 
  } 

  public Vector getInstances()
  { Vector res = new Vector();
    if (multiplicity <= 1)
    { res.add(label);
      return res;
    }

    for (int i = 1; i <= multiplicity; i++)
    { res.add(label + i); }
    return res;
  }

  public Vector getInstanceVars()
  { Vector res = new Vector();
    if (multiplicity <= 1)
    { res.add(label);
      return res;
    }

    for (int i = 1; i <= multiplicity; i++)
    { res.add(label + i + "." + label); }
    return res;
  }

  public Vector myVariables(String instance)  // ac1, ac2, etc. 
  { Vector res = new Vector();

    if (multiplicity > 1)
    { res.add(instance + "." + label); }
    else  // instance.equals(label)
    { res.add(instance); }

    if (attribute != null)
    {
      if (multiplicity > 1)
      { res.add(instance + "." + attribute); }
      else // instance.equals(label)
      { res.add(attribute); }
    }
    return res;
  }

  public int getMultiplicity()
  { return multiplicity; } 

  public void setMultiplicity(int m) 
  { multiplicity = m; } // must be >= 1. 

  public void setTemplate(SmBuilder sm) 
  { myTemplate = sm; } 

  public void closeGui() 
  { gui.setVisible(false); 
    gui.controller.setVisible(false); 
  } 

  /* The name of the module is returned. */
  // public String get_label()
  // { 
    // return label;
  //}

  public void setStates(Vector sts) 
  { states = sts; }

  public Vector getStates() 
  { return states; } 

  public void setEvents(Vector evs)
  { events = evs; } 

  public void addEvent(Event e) 
  { events.add(e); } 

  public void setTransitions(Vector trans) 
  { transitions = trans; } 

  public void removeTransitions(Vector trans)
  { transitions.removeAll(trans); 
    gui.removeVisualTransitions(trans); 
  } 

  public Vector getEvents() 
  { return events; } 

  public void add_state(State st)
  { // states.add(st);
    //Identify and set the initial state
    if (st.initial == true)
    { if (initial_state != null) 
      { initial_state.initial = false; } 
      initial_state = st;
      states.remove(st);
      states.add(0,st);
    }
    else 
    { states.add(st); } 
  }

  public void deleteTransition(Transition t)
  { transitions.remove(t); 
    t.source.removeTransitionFrom(t); 
    t.target.removeTransitionTo(t); 
    setTemplate(null); 
  }

  public void deleteState(State s)
  { states.remove(s); 
    if (initial_state == s)
    { initial_state = null; }

    for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.elementAt(i); 
      t.deleteState(s); 
    }
    setTemplate(null); 
  }

  public void removeState(State s) 
  { states.remove(s); 
    gui.removeVisualState(s); 
  } 

  public void removeState2(State s) 
  { states.remove(s); 
    gui.removeVisualState(s); 
    Vector ins = s.trans_to; 
    Vector outs = s.trans_from; 
    transitions.removeAll(ins); 
    transitions.removeAll(outs);
    gui.removeVisualTransitions(ins); 
    gui.removeVisualTransitions(outs); 
  } 

  public void removeState3(State s) 
  { states.remove(s); 
    gui.removeVisualState(s); 
    Vector outs = s.trans_from; 
    transitions.removeAll(outs);
    gui.removeVisualTransitions(outs); 
  } 

  public void moveEndpoint(Transition t, State s1, State s2)
  { RoundRectData rd1 = gui.getVisual(s1); 
    RoundRectData rd2 = gui.getVisual(s2); 
    if (rd1 != null && rd2 != null)
    { int x1 = rd1.sourcex; 
      int x2 = rd2.sourcex; 
      int y1 = rd1.sourcey; 
      int y2 = rd2.sourcey; 
      gui.moveTransitionTarget(t,x2 - x1,y2 - y1);
    }
    else 
    { System.err.println("Error: no visuals for " + s1 + " " + s2); }
  } 

  public void moveStartpoint(Transition t, State s1, State s2)
  { RoundRectData rd1 = gui.getVisual(s1); 
    RoundRectData rd2 = gui.getVisual(s2); 
    if (rd1 != null && rd2 != null)
    { int x1 = rd1.sourcex; 
      int x2 = rd2.sourcex; 
      int y1 = rd1.sourcey; 
      int y2 = rd2.sourcey; 
      gui.moveTransitionSource(t,x2 - x1,y2 - y1);
    }
    else 
    { System.err.println("Error: no visuals for " + s1 + " " + s2); }
  } 

  public void setInitial(State st) 
  { if (initial_state != null) 
    { initial_state.initial = false; }  
    initial_state = st; 
    st.initial = true;
    states.remove(st); 
    states.add(0,st);  
  } 

  public State getInitial() 
  { return initial_state; } 

  public void unsetInitial(State st) 
  { if (initial_state == st) 
    { initial_state = null; } 
    st.initial = false; 
  } 

  public State getState(int i)
  { return (State) states.elementAt(i); } 

  public State getState(Expression e)
  { if (e instanceof BasicExpression)
    { Object obj = VectorUtil.lookup(((BasicExpression) e).data,states);
      if (obj != null)
      { return (State) obj; } 
    }
    return null; 
  }

  public Vector getStateNames() 
  { Vector res = new Vector(); 
    for (int i = 0; i < states.size(); i++) 
    { State s = (State) states.elementAt(i); 
      res.add(s.label); 
    } 
    return res; 
  }  

  /** Gets all invariants of states of the statemachine */ 
  public Vector generatedInvariants() 
  { Vector res = new Vector(); 
    for (int i = 0; i < states.size(); i++) 
    { State st = (State) states.get(i); 
      Vector sinvs = st.generatedInvariants(); 
      res.addAll(sinvs); 
    } 
    return res; 
  } 

  /** Checks if val is the target of any transition of ev */ 
  public boolean targetOf(Expression val, String ev) 
  { String targ = val.toString(); 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition t = (Transition) transitions.get(i);
      // System.out.println(t);  
      if (ev.equals(t.event.toString()))
      { if (targ.equals(t.target.toString()))
        { // System.out.println("Found target " + val); 
          return true; 
        } 
      } 
    } 
    // System.out.println("Not found target " + val); 
    return false; 
  }  

  public Vector generateOpspecs(String prefix)
  { Vector res = new Vector();
    for (int i = 0; i < events.size(); i++)
    { Event ev = (Event) events.get(i);
      SkipOperationSpec op =
        new SkipOperationSpec(ev.label,label);
      op.setPrefix(prefix); 
      res.add(op); 
    }
    return res;
  }


  public Transition getTransition(String eventName, String source)
  { for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.elementAt(i);
      if (t.event != null && t.source != null)
      { if (eventName.equals(t.event.label) && source.equals(t.source.label))
        { return t; }
      }
      else 
      { System.out.println("Transition " + t + 
                           " has undefined event or source!"); } 
    }
    return null; 
  }


  public void add_trans(Transition trans)
  { if (transitions.contains(trans)) { }
    else  
    { transitions.add(trans); }
  }

  public void add_event(Event ev)
  { if (events.contains(ev)) { } 
    else  
    { events.add(ev); }
  }

  public boolean contractPath(State s)
  { boolean res = false;
    if (s.trans_from.size() == 1 && s.trans_to.size() == 1)
    { Transition trinto = (Transition) s.trans_to.get(0);
      Transition trfrom = (Transition) s.trans_from.get(0);
      if (trfrom.event == null ||
           isOperationStateMachine())
      { res = true;
        trinto.target = trfrom.target;
        if (trfrom.generations == null ||
             trfrom.generations.size() == 0) { }
        else if (trinto.generations == null ||
             trinto.generations.size() == 0)
        { trinto.generations = trfrom.generations; }
        else
        { Expression t1gen = (Expression) trinto.generations.get(0);
          Expression t2gen = (Expression) trfrom.generations.get(0);
          Expression sand = 
            (new BinaryExpression("&",t1gen,t2gen)).simplify();
          trinto.generations = new Vector();
          trinto.generations.add(sand);
          states.remove(s);
          transitions.remove(trfrom);
        }
      }
    }
    return res;
  }

  public boolean isSingleState() 
  { return states.size() == 1; } 

  public static String findComponentOf(String eventName, 
                                       Vector components)
  { for (int i = 0; i < components.size(); i++)
    { Statemachine sm = (Statemachine) components.elementAt(i);
      if (sm.hasEvent(eventName)) 
      { return sm.label; } 
    } 
    System.err.println("Component not found for event " +
                       eventName); 
    return ""; 
  } 

  public static Statemachine createVirtualSensor(OperationalInvariant inv)
  { Vector states = new Vector(); 
    String vsname = "vs" + inv.sensorComponent; 
    String event = inv.eventName; 
    State bs = new BasicState(vsname + "state"); 
    bs.initial = true; 
    Vector trans = new Vector(); 
    Vector events = new Vector(); 
    states.add(bs); 
    Event ev = new Event(event); 
    events.add(ev); 
    Transition tr = new Transition(event,bs,bs,ev); 
    trans.add(tr); 
    Statemachine vs = new Statemachine(bs,events,trans,states); 
    vs.label = vsname; 
    vs.cType = DCFDArea.SENSOR; 
    return vs; 
  } 

  public boolean checkTrans(Transition newt) 
  { boolean res = true; 

    if (newt.event == null) 
    { System.out.println("Error -- no valid event for transition " + newt + 
                         "\n" + "You must create the event first."); 
      return false; 
    } 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition t = (Transition) transitions.get(i); 
      if (t.source == newt.source)
      { if (t.event != null &&
            t.event.equals(newt.event))
        /* Violates consistency */ 
        { System.err.println("Transition " + newt + " has same event as existing transition from source state \n"); 
          System.err.println("You must ensure they have disjoint conditions."); 
          res = false; 
        }
        else if (t.target.equals(newt.target)) 
        { System.err.println("Transition " + newt + " has same target as existing transition from source state \n"); 
          System.err.println("You must ensure they have disjoint conditions.");
          res = false; 
        }
      }
    }
    return res;
  }

  public Transition find_match(Event e,State s)
  { for (int i = 0; i < transitions.size(); i++)
    { Transition ta = (Transition) transitions.elementAt(i);
      if ((ta.source == s) &&
          ta.event.label.equals(e.label))
      { return ta; }
    }
    return null;
  }

  public void displayStatemachine()
  { System.out.println("Initial state is: " + initial_state);

    if (attribute == null) 
    { System.out.println("There is no attribute"); } 
    else 
    { System.out.println("Attribute is: " + attribute); } 

    for (int i = 0; i < states.size(); i++)
    { ((State) states.elementAt(i)).display(); }

    for (int i = 0; i < transitions.size(); i++)
    { ((Transition) transitions.elementAt(i)).display_transition(); }
  }

  public Vector allTransitionsFor(Event e) 
  { Vector res = new Vector(); 
    for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.elementAt(i); 
      if (t.event != null && t.event.equals(e))
      { res.add(t); } 
    } 
    return res; 
  } 

  public static Vector targetStates(Vector tt) 
  { Vector res = new Vector(); 
    for (int i = 0; i < tt.size(); i++) 
    { Transition t = (Transition) tt.elementAt(i); 
      if (t.target != null) 
      { res.add(t.target); } 
    } 
    return res; 
  } 

  public void dispStates()
  { // should save to a logfile. 
    int listSize = states.size();
    File file = new File("output/tmp");
    try
    { PrintWriter out = new PrintWriter(
                          new BufferedWriter(
                            new FileWriter(file)));
      if (listSize > 0)
      {
        for (int i = 0; i < listSize; i++)
        { System.out.println("-------------------------------------------"); 
          out.println("-------------------------------------------");
          State st = (State) states.get(i);
          System.out.println("State ===> " + st.label);
          out.println("State ===> " + st.label);
          Vector sens = st.getSensorSettings(); 
          System.out.println("Sensor settings are: " + sens); 
          out.println("Sensor settings are: " + sens);
          Maplet mm = st.getProperties(); 
          if (mm != null)
          { 
            Vector subs = (Vector) mm.source; 
            Vector invs = (Vector) mm.dest; 
            System.out.println("Actuator settings are: " + subs); 
            out.println("Actuator settings are: " + subs);
            // Invariant.displayAll(invs); 
            // Invariant.displayAll(invs,out);
            System.out.println("Invariants: " + invs); 
            out.println("Invariants: " + invs);  
          } 
        }
      }
      else
      {
        System.err.println("There are no states");
      }
      out.close();
    }
    catch (IOException e)
    { System.err.println("Error saving states: check output/tmp exists"); }

    new TextDisplay("States","output/tmp");
  }

  

  public void dispTrans()
  {
    int listSize = transitions.size();

    if (listSize > 0)
      {
        for (int i = 0; i < listSize; i++)
          {
            Transition tran = (Transition) transitions.get(i);
            //System.out.println("Transition ===> " + tran.label);
            tran.dispSrcTarg();
            if (tran.event != null) 
            { System.out.println("Event ===> " + tran.event.label); } 
          }
      }
    else
      {
        System.out.println("There are no transitions");
      }
  }

  /* Methods for controller synthesis process: */ 
  public Vector getActionList()
  { Vector res = new Vector(); 
               /* maps state to action needed to reach it from
                   each other state */
    for (int i = 0; i < states.size(); i++)
    { State s = (State) states.elementAt(i);
      CaseStatement cs = buildCases(s);
      Maplet mm = new Maplet(s,cs);
      res.add(mm); 
    }
    return res; 
  }

   private CaseStatement buildCases(State s)
   { CaseStatement res = new CaseStatement();
     boolean found = false;

     for (int j = 0; j < states.size(); j++)
     { State from = (State) states.elementAt(j);
       if (from != s) 
       { 
         found = false;
         for (int k = 0; k < transitions.size(); k++)
         { Transition tran = (Transition) transitions.elementAt(k);
            if (tran.source == from && tran.target == s)
            { InvocationStatement is = new InvocationStatement(tran.event);
              res.addCase(from,is);
              found = true; 
            } 
         } // Should only look at tran.isControllable() transitions. 
       } 
     } 
     return res; 
  }

  public Vector getMultActionList()
  { Vector res = new Vector();
               /* maps state to action needed to reach it from
                   each other state */
    for (int i = 0; i < states.size(); i++)
    { State s = (State) states.elementAt(i);
      CaseStatement cs = buildMultipleCases(s);
      Maplet mm = new Maplet(s,cs);
      res.add(mm);
    }
    return res;
  }

   private CaseStatement buildMultipleCases(State s)
   { CaseStatement res = new CaseStatement();
     boolean found = false;

     for (int j = 0; j < states.size(); j++)
     { State from = (State) states.elementAt(j);
       if (from != s) 
       { 
         found = false;
         for (int k = 0; k < transitions.size(); k++)
         { Transition tran = (Transition) transitions.elementAt(k);
            if (tran.source == from && tran.target == s)
            { InvocationStatement is =
                new InvocationStatement(tran.event.label,"oo",null);
              res.addCase(from,is);
              found = true; 
            } 
         }
         // if (!found)
         // { ErrorStatement es = new ErrorStatement();
         //   res.addCase(from,es); 
         // }
         } 
       } 
       return res; 
     }


  /* For generating Outer Level: */ 
  public void displayOuterLevelTest(Vector promoted, String prefix)
  { String oldVar = "old_" + label;
    String newVar = "new_" + label;
    int n = events.size();
    int m = states.size(); 
    String pref = ""; 
    if (prefix != null && !prefix.equals(""))
    { pref = prefix + "_"; } 
  
    if (m > 1)  
    /* For each event get all transitions for that event */
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.elementAt(i);
        e.displayOuterLevelTest(transitions,
                                oldVar,newVar,promoted,pref); 
        if (i < n-1) 
        { System.out.println("  ELSE"); } }

        for (int j = 0; j < n; j++)
        { System.out.print("  END"); }
        /* System.out.println("\n"); */ 
    } 
    else 
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.get(i);
        System.out.println("    IF " + label + "_" + e.label + " = TRUE"); 
        System.out.println("    THEN " + pref + e.label); 
        if (i < n - 1) 
        System.out.println("    ELSE"); 
      }
      for (int j = 0; j < n; j++)
      { System.out.print("    END"); }
        // System.out.println("; \n");  
    } 
  }

  public void displayOuterLevelTest(Vector promoted, PrintWriter out, 
                                    String prefix)
  { String oldVar = "old_" + label;
    String newVar = "new_" + label;
    int n = events.size();
    int m = states.size();
    String pref = "";
    if (prefix != null && !prefix.equals(""))
    { pref = prefix + "_"; }

    if (m > 1)
    /* For each event get all transitions for that event */
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.elementAt(i);
        e.displayOuterLevelTest(transitions,
                                oldVar,newVar,promoted,out,pref);
        if (i < n-1)
        { out.println("  ELSE"); } 
      }

      for (int j = 0; j < n; j++)
      { out.print("  END"); }
    }
    else
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.get(i);
        out.println("    IF " + label + "_" + e.label + " = TRUE");
        out.println("    THEN " + pref + e.label);
        if (i < n - 1)
        out.println("    ELSE"); 
      }
      for (int j = 0; j < n; j++)
      { out.print("    END"); }
    }
  }


  public void displayJavaOuterLevelTest(Vector promoted)
  { String oldVar = "M" + label + "." + label;
    String newVar = "new_" + label;
    int n = events.size();
    int m = states.size(); 
  
    if (m > 1)  
    /* For each event get all transitions for that event */
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.elementAt(i);
        e.displayJavaOuterLevelTest(transitions,
                                    oldVar,newVar,promoted); 
        if (i < n-1) 
        { System.out.println("    else"); } 
      }

      // for (int j = 0; j < n; j++)
      // { System.out.print("  }"); }
    } 
    else 
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.get(i);
        System.out.println("    if (" + label + "_" + e.label + ")"); 
        System.out.println("    { controller." + e.label + "(); }"); 
        if (i < n - 1) 
        System.out.println("    else"); 
      }
      // for (int j = 0; j < n; j++)
      // { System.out.print("  }"); } 
    } 
  }

  public void displayJavaOuterLevelTest(Vector promoted, PrintWriter out)
  { String oldVar = "M" + label + "." + label;
    String newVar = "new_" + label;
    int n = events.size();
    int m = states.size();
 
    if (m > 1)
    /* For each event get all transitions for that event */
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.elementAt(i);
        e.displayJavaOuterLevelTest(transitions,
                                    oldVar,newVar,promoted,out);
        if (i < n-1)
        { out.println("    else"); }      }

    }
    else
    { for (int i = 0; i < n; i++)
      { Event e = (Event) events.get(i);
        out.println("    if (" + label + "_" + e.label + ")");
        out.println("    { controller." + e.label + "(); }");
        if (i < n - 1)
        out.println("    else");      }
    }
  }


  public void genSystemType()
  { System.out.print("{ ");
    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.elementAt(i);
      System.out.print(st.label);
      if (i < states.size()-1)
      { System.out.print(", "); } 
    }
    System.out.print(" }"); 
  }


  public void genSystemType(PrintWriter out)
  { out.print("{ ");
    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.elementAt(i);
      out.print(st.label);
      if (i < states.size()-1)
      { out.print(", "); } 
    }
    out.print(" }"); 
  }

  // If the states are all numbers, also skip. 
  public void genJavaSystemType()
  { if (myTemplate != null && myTemplate instanceof AttributeBuilder) 
    { return; } 
    if (hasNumericStates())
    { return; } 

    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.elementAt(i);
      System.out.println("  public static final int " + 
                            st.label + " = " + i + ";");
    } 
  } 

  public void genJavaSystemType(PrintWriter out)
  { if (myTemplate != null && myTemplate instanceof AttributeBuilder) 
    { return; } 
    if (hasNumericStates())
    { return; } 

    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.elementAt(i);
      out.println("  public static final int " + st.label + " = " + i + ";");
    } 
  } 

  private String bStateType() 
  { if (myTemplate != null && myTemplate instanceof AttributeBuilder)
    { return "0.." + (states.size() - 1); } 
    else if (hasNumericStates())
    { return "0.." + (states.size() - 1); }  // Assumption this is the set
    else 
    { return "State_" + label; } 
  } 

  private String bVariables()
  { String res = label; 
    if (attribute != null)
    { return res + ", " + attribute; }
    else
    { return res; }
  } 

  private String bInvariant() 
  { String res = label + ": " + bStateType(); 
    if (attribute != null)
    { return res + " &\n  " + attribute + " : 0.." + maxval; }
    return res; 
  } 

  private String bInitialisation() 
  { String res = label + " := " + initial_state.label; 
    if (attribute != null)
    { return res + " || " + attribute + " := 0"; }
    return res; 
  } 
    

  /* For generating B Machine: */ 
  public void displayBMachine()
  { System.out.println("MACHINE M" + label);
    System.out.println("SEES SystemTypes");
    System.out.println("VARIABLES " + bVariables());
    System.out.println("INVARIANT " + bInvariant()); 

    if (initial_state == null) 
    { System.out.println("/* No initial state is set! */ "); } 
    else 
    { System.out.println("INITIALISATION " + bInitialisation()); } 
    displayMachineOps(); 
    if (attribute != null) 
    { System.out.println("  " + attribute + "x <-- get" + attribute + " = ");
      System.out.println("    " + attribute + "x := " + attribute + ";");
      System.out.println();
      System.out.println("  set" + attribute + "(" + attribute + "x) = ");
      System.out.println("    PRE " + attribute + "x: 0.." + maxval); 
      System.out.println("    THEN " + attribute + " := " + attribute + "x");
      System.out.println("    END;"); 
      System.out.println();
    } 
    System.out.println("  " + label + "x <-- get" + label + " = "); 
    System.out.println("    " + label + "x := " + label); 
    System.out.println("END"); 
  }

  public void displayBMachine(PrintWriter out)
  { out.println("MACHINE M" + label);
    out.println("SEES SystemTypes");
    out.println("VARIABLES " + bVariables());
    out.println("INVARIANT " + bInvariant()); 
    if (initial_state == null)
    { out.println("/* No initial state is set! */"); }
    else
    { out.println("INITIALISATION " + bInitialisation()); } 
    displayMachineOps(out);
    if (attribute != null)
    { out.println("  " + attribute + "x <-- get" + attribute + " = ");
      out.println("    " + attribute + "x := " + attribute + ";");
      out.println();
      out.println("  set" + attribute + "(" + attribute + "x) = ");
      out.println("    PRE " + attribute + "x: 0.." + maxval);
      out.println("    THEN " + attribute + " := " + attribute + "x");
      out.println("    END;");
      out.println();
    }
    out.println("  " + label + "x <-- get" + label + " = ");
    out.println("    " + label + "x := " + label);
    out.println("END"); 
  }  

  public String bStateImport() 
  { String res = ""; 
    if (myTemplate != null && (myTemplate instanceof AttributeBuilder))
    { res = label + "_Nvar(" + (states.size() - 1) + ")"; } 
    else if (hasNumericStates())
    { res = label + "_Nvar(" + (states.size() - 1) + ")"; }
    else 
    { res = label + "_Vvar(State_" + label + ")"; } 
 
    if (attribute != null) 
    { return res + ", " + attribute + "_Nvar(" + maxval + ")"; } 
    return res;  
  } 

  public String bImpInvariant()
  { String res = ""; 
    if (myTemplate != null && (myTemplate instanceof AttributeBuilder))
    { res = label + " = " + label + "_Nvar"; } 
    else if (hasNumericStates())
    { res = label + " = " + label + "_Nvar"; }
    else 
    { res = label + " = " + label + "_Vvar"; } 

    if (attribute != null)
    { return res + " &\n    " + attribute + " = " + attribute + "_Nvar"; }
    return res;
  } 

  public String bImpInitialisation()
  { String res = ""; 
    if (myTemplate != null && (myTemplate instanceof AttributeBuilder))
    { res = label + "_STO_NVAR(" + initial_state.label + ")"; }
    else if (hasNumericStates())
    { res = label + "_STO_NVAR(" + initial_state.label + ")"; }
    else
    { res = label + "_STO_VAR(" + initial_state.label + ")"; }

    if (attribute != null)
    { return res + ";\n    " + attribute + "_STO_NVAR(0)"; }
    return res;
  }


  public void displayBImp()
  { System.out.println("IMPLEMENTATION I" + label);
    System.out.println("REFINES M" + label);
    System.out.println("SEES SystemTypes");
    System.out.println("IMPORTS " + bStateImport()); 
    System.out.println("INVARIANT " + bImpInvariant()); 
    if (initial_state == null)  
    { System.out.println("/* No initial state set! */"); }
    else 
    { System.out.println("INITIALISATION " + bImpInitialisation()); } 
    displayMachineOpImps();  // NVAR if Attribute
    if (attribute != null)
    { System.out.println("  " + attribute + "x <-- get" + attribute + " = ");
      System.out.println("    " + attribute + "x <-- " + attribute + "_VAL_NVAR;");
      System.out.println();
      System.out.println("  set" + attribute + "(" + attribute + "x) = ");
      System.out.println("    " + attribute + "_STO_NVAR(" + attribute + "x);");
    }
    System.out.println();  
    System.out.println("  " + label + "x <-- get" + label + " = "); 
    System.out.println("    " + label + "x <-- " + label + "_VAL_VAR"); 
    System.out.println("END\n"); 
  }  // And an NVar for attribute if it exists. 

  public void displayBImp(PrintWriter out)
  { out.println("IMPLEMENTATION I" + label);
    out.println("REFINES M" + label);
    out.println("SEES SystemTypes");
    out.println("IMPORTS " + bStateImport()); 
    out.println("INVARIANT " + bImpInvariant()); 
    if (initial_state == null)
    { out.println("/* No initial state set! */"); }
    else
    { out.println("INITIALISATION " + bImpInitialisation()); } 
    displayMachineOpImps(out);
    if (attribute != null)
    { out.println("  " + attribute + "x <-- get" + attribute + " = ");
      out.println("    " + attribute + "x <-- " + attribute + "_VAL_NVAR;");
      out.println();
      out.println("  set" + attribute + "(" + attribute + "x) = ");
      out.println("    " + attribute + "_STO_NVAR(" + attribute + "x);");
    }
    out.println();
    out.println("  " + label + "x <-- get" + label + " = ");
    out.println("    " + label + "x <-- " + label + "_VAL_VAR");
    out.println("END\n");
  }  // And an NVar for attribute if it exists. 

  private Vector generateMultipleBM()
  { String paramType = label.toUpperCase() + "_OBJ";
    String insts = label + "s";

    Vector res = new Vector();
    res.add("MACHINE M" + label + "(" + paramType + ")");
    res.add("SEES SystemTypes");
    res.add("CONSTRAINTS card(" + paramType + ") = " + multiplicity); 
    res.add("VARIABLES " + label + ", " + insts);
    res.add("INVARIANT");
    res.add("  " + insts + " <: " + paramType + " &");
    if (attribute != null) 
    { res.add("  " + attribute + ": " + insts + " --> 0.." + maxval +
              " &");
    } 
    res.add("  " + label + ": " + insts + " --> State_" + label);
    res.add("INITIALISATION " + bMultInit());
    return res; 
  } 

  private String bMultInit()
  { String insts = label + "s";
    String res = insts + " := {} || " + label + " := {}"; 
    if (attribute != null) 
    { return res + " || " + attribute + " := {}"; } 
    return res; 
  } 

  private Vector generateMultGetOp()
  { Vector res = new Vector(); 
    String insts = label + "s";
    String paramType = label.toUpperCase() +
                       "_OBJ";

    if (initial_state == null)
    { System.err.println("No initial state set for " + label); }
    else
    { res.add("  oo <-- new_" + label + " = ");
      res.add("    PRE not(" + insts + " = " +
              paramType + ")");
      res.add("    THEN ");
      res.add("      ANY oox WHERE oox: " + paramType +
              " - " + insts);
      res.add("      THEN ");
      res.add("        " + insts + " := " + insts +
              " \\/ { oox } ||");
      res.add("        " + label + "(oox) := " +
              initial_state.label + " ||");
      if (attribute != null) 
      { res.add("        " + attribute + "(oox) := 0 ||"); } 
      res.add("        oo := oox");
      res.add("      END");
      res.add("    END;");
      res.add(""); 
    } 

    res.add("  " + label + "x <-- get" + label + "(oo) = ");
    res.add("    PRE oo: " + insts); 
    res.add("    THEN " + label + "x := " + label + "(oo)");
    res.add("    END"); 
    res.add("END");
    return res;
  }
  
  private Vector generateMultImpGetOp()
  { Vector res = new Vector();
    int n = label.length();
    String lib = ("" + label.charAt(0)).toUpperCase() +
                 label.substring(1,n);
    if (initial_state == null)
    { System.err.println("No initial state for " + label); }
    else 
    { res.add("  oo <-- new_" + label + " = ");
      res.add("    VAR bb, oox ");
      res.add("    IN ");
      res.add("      bb,oox <-- " + lib + "_CRE_FNC_OBJ;");
      res.add("      IF bb = TRUE");
      res.add("      THEN");
      res.add("        oo := oox;");
      if (attribute != null) 
      { res.add("        " + lib + "_STO_FNC_OBJ(oox,2,0); "); } 
      res.add("        " + lib + "_STO_FNC_OBJ(oox,1," +
              initial_state.label + ")");
      res.add("      END");
      res.add("    END;");
      res.add("   "); 
    }
    res.add("  " + label + "x <-- get" + label + "(oo) = ");
    res.add("    " + lib + "_VAL_FNC_OBJ(oox,1)");
    res.add("END");
    return res;
  }

  private String bmultipleImport(String lib) 
  { if (attribute == null) 
    { return lib + "_fnc_obj(State_" + label + ",1," + multiplicity + ")"; }
    else 
    { return lib + "_fnc_obj(State_" + label + " \\/ NAT,2," + 
                             multiplicity + ")"; 
    } 
  } 

  public Vector generateMultipleImpBM()
  { String paramType = label.toUpperCase() + "_OBJ";
    Vector res = new Vector();
    int n = label.length();
    String lib = ("" + label.charAt(0)).toUpperCase() +
                 label.substring(1,n);
 
    res.add("IMPLEMENTATION I" + label);
    res.add("REFINES M" + label);
    res.add("SEES SystemTypes, Bool_TYPE");
    res.add("IMPORTS " + bmultipleImport(lib)); 
    res.add("PROPERTIES " + paramType + " = " +
            lib + "_FNCOBJ");
    res.add("INVARIANT " + label + "s = " +
            lib + "_fnctok &");
    res.add("  !xx.(xx: " + label + "s  =>");
    res.add("         1: dom(" + lib + "_fncstruct(xx)) &");
    res.add("         " + lib + "_fncstruct(xx)(1) = " + 
            label + "(xx))");
    if (attribute != null) 
    { res.add("  & "); 
      res.add("  !xx.(xx: " + label + "s  =>");
      res.add("         2: dom(" + lib + "_fncstruct(xx)) &");
      res.add("         " + lib + "_fncstruct(xx)(2) = " +
              attribute + "(xx))");
    } 
    return res;
  }  

  // It can never be an AttributeBuilder
  public void displayMultipleBM()
  { Vector text = generateMultipleBM();
    VectorUtil.printLines(text); 
    System.out.println(); 
    displayMultMachineOps();
    System.out.println(); 
    text = generateMultGetOp(); 
    // text.add("END"); 
    VectorUtil.printLines(text); 
  }

  public void displayMultipleBM(PrintWriter out)
  { Vector text = generateMultipleBM(); 
    VectorUtil.printLines(text,out);
    out.println(); 
    displayMultMachineOps(out);
    out.println(); 
    text = generateMultGetOp(); 
    VectorUtil.printLines(text,out); 
  }

  public void displayMultipleBImp()
  { Vector text = generateMultipleImpBM(); 
    VectorUtil.printLines(text);
    System.out.println();
    displayMultMachineOpImps();
    System.out.println();
    text = generateMultImpGetOp(); 
    // text.add("END"); 
    VectorUtil.printLines(text);
  }

  public void displayMultipleBImp(PrintWriter out)
  { Vector text = generateMultipleImpBM();
    VectorUtil.printLines(text,out);
    out.println();
    displayMultMachineOpImps(out);
    out.println();
    text = generateMultImpGetOp();
    VectorUtil.printLines(text,out);
  }


  private void displayMachineOpImps()
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop; 

    if (n == 0) { return; }
    System.out.println("OPERATIONS");
    for (int i = 0; i < n; i++)
    { Event e = (Event) events.get(i);
      bop = buildBOperation(e);
      boperations.add(bop); 
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.get(j); 
      bop.displayImp(label);  /* displayImp() */ 
      System.out.println("; \n"); 
    }
  }

  private void displayMachineOpImps(PrintWriter out)
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop;

    if (n == 0) { return; }
    out.println("OPERATIONS");
    for (int i = 0; i < n; i++)
    { Event e = (Event) events.get(i);
      bop = buildBOperation(e);
      boperations.add(bop);
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.get(j);
      bop.displayImp(label,out);  /* displayImp() */
      out.println("; \n");
    }
  }

  private void displayMachineOps()
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop; 

    if (n == 0) { return; }
    System.out.println("OPERATIONS");
    for (int i = 0; i < n; i++)
    {  Event e = (Event) events.elementAt(i);
       bop = buildBOperation(e);
       boperations.add(bop); 
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.elementAt(j); 
      bop.display(); 
      System.out.println("; \n"); 
    }
  }

  private void displayMachineOps(PrintWriter out)
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop;

    if (n == 0) { return; }
    out.println("OPERATIONS");
    for (int i = 0; i < n; i++)
    {  Event e = (Event) events.elementAt(i);
       bop = buildBOperation(e);
       boperations.add(bop);
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.elementAt(j);
      bop.display(out);
      out.println("; \n");
    }
  }


  private void displayMultMachineOps() 
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop;

    if (n == 0) {  }
    else 
    { System.out.println("OPERATIONS");
      for (int i = 0; i < n; i++)
      {  Event e = (Event) events.elementAt(i);
         bop = buildMultipleBOperation(e); 
         boperations.add(bop); 
      }

      for (int j = 0; j < n; j++)
      { bop = (BOperation) boperations.elementAt(j);
        bop.display();  
        System.out.println("; \n"); 
      }
    }

    if (attribute != null)
    { System.out.println("  " + attribute + "x <-- get" + attribute + "(oo) = ");
      System.out.println("    PRE oo: " + label + "s"); 
      System.out.println("    THEN " + attribute + "x := " + attribute + "(oo)");
      System.out.println("    END;"); 
      System.out.println();
      System.out.println("  set" + attribute + "(oo," + attribute + "x) = ");
      System.out.println("    PRE " + attribute + "x: 0.." + maxval + 
                                  " & oo: " + label + "s");
      System.out.println("    THEN " + attribute + "(oo) := " + attribute + "x");
      System.out.println("    END;");
      System.out.println();
    }
  } // And version with PrintWriter


  private void displayMultMachineOps(PrintWriter out) 
  { int n = events.size();
    Vector boperations = new Vector();
    BOperation bop;

    if (n == 0) { }
    else 
    { out.println("OPERATIONS");
      for (int i = 0; i < n; i++)
      {  Event e = (Event) events.elementAt(i);
         bop = buildMultipleBOperation(e); 
         boperations.add(bop); 
      }

      for (int j = 0; j < n; j++)
      { bop = (BOperation) boperations.elementAt(j);
        bop.display(out);  
        out.println("; \n"); 
      }
    } 

    if (attribute != null)
    { out.println("  " + attribute + "x <-- get" + attribute + "(oo) = ");
      out.println("    PRE oo: " + label + "s");
      out.println("    THEN " + attribute + "x := " + attribute + "(oo)");
      out.println("    END;");
      out.println();
      out.println("  set" + attribute + "(oo," + attribute + "x) = ");
      out.println("    PRE " + attribute + "x: 0.." + maxval +
                                  "& oo: " + label + "s");
      out.println("    THEN " + attribute + "(oo) := " + attribute + "x");
      out.println("    END;");
      out.println();
    }
  } 

  private void displayMultMachineOpImps()
  { int n = events.size();
    int m = label.length(); 
    Vector boperations = new Vector();
    String lib = label.substring(0,1).toUpperCase() +
                 label.substring(1,m);
    BOperation bop;

    if (n == 0) {  }
    else 
    { System.out.println("OPERATIONS");
      for (int i = 0; i < n; i++)
      {  Event e = (Event) events.elementAt(i);
         bop = buildMultImpBOperation(e); 
         boperations.add(bop); 
      }

      for (int j = 0; j < n; j++)
      { bop = (BOperation) boperations.elementAt(j);
        bop.displayMultImp(label);
        System.out.println("; \n"); 
      }
    } 

    if (attribute != null)
    { System.out.println("  " + attribute + "x <-- get" + attribute + "(oo) = ");
      System.out.println("    " + attribute + "x <-- " + lib + "_VAL_FNC_OBJ(oo,2);");
      System.out.println();
      System.out.println("  set" + attribute + "(oo," + attribute + "x) = ");
      System.out.println("    " + lib + "_STO_FNC_OBJ(oo,2," + attribute + "x);");
      System.out.println();
    } 
  } // And version with PrintWriter

  private void displayMultMachineOpImps(PrintWriter out)
  { int n = events.size();
    Vector boperations = new Vector();
    String lib = label.substring(0,1).toUpperCase() +
                 label.substring(1,n);
    BOperation bop;

    if (n == 0) {  }
    else
    { out.println("OPERATIONS");
      for (int i = 0; i < n; i++)
      {  Event e = (Event) events.elementAt(i);
         bop = buildMultImpBOperation(e);
         boperations.add(bop);
      }

      for (int j = 0; j < n; j++)
      { bop = (BOperation) boperations.elementAt(j);
        bop.displayMultImp(label,out); // out
        out.println("; \n");
      }
    }

    if (attribute != null)
    { out.println("  " + attribute + "x <-- get" + attribute + "(oo) = ");
      out.println("    " + attribute + "x <-- " + lib + "_VAL_FNC_OBJ(oo,2);");
      out.println();
      out.println("  set" + attribute + "(oo," + attribute + "x) = ");
      out.println("    " + lib + "_STO_FNC_OBJ(oo,2," + attribute + "x);");
      out.println();
    }
  } 

  private BOperation buildBOperation(Event e)
  { String pre = "";
    IfStatement is = new IfStatement();
    String sensorE = label + "_" + e.label; 
    BasicExpression be = new BasicExpression(label); 

    for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.elementAt(i);
      
      if (t.event.label.equals(e.label))
      { Expression tguard = t.guard; 
        BasicExpression be2 = new BasicExpression(t.source.label);
        BasicExpression be3 = new BasicExpression(t.target.label);
        Expression iftest = new BinaryExpression("=",be,be2); 
        Expression condition = 
          Expression.simplify("&",iftest,tguard,null); 
        if (pre.equals(""))
        { pre = "(" + condition.toB() + ")"; }
        else 
        { pre = pre + " or (" + condition.toB() + ")"; }
        
        if (t.generations != null && t.generations.size() > 0)
        { SequenceStatement ss = t.genToSequenceStatement(); 
          ss.addStatement(new AssignStatement(be,be3)); 
          is.addCase(condition,ss); 
        } 
        else 
        { is.addCase(condition,new AssignStatement(be,be3)); }
      } 
    }  

    if (cType == DCFDArea.SENSOR) 
    { return new BOperation(sensorE,pre,is); } 
    else 
    { return new BOperation(e.label,pre,is); } 
  } 

  private BOperation buildMultipleBOperation(Event e)
  { String insts = label + "s";
    String pre1 = "(oo: " + insts + ")";
    String pre = ""; 
    IfStatement is = new IfStatement();
    String sensorE = label + "_" + e.label + "(oo)";
    BasicExpression be = new BasicExpression(label);
    BasicExpression beref = new BasicExpression("oo"); 
    be.objectRef = beref; 

    for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.elementAt(i);

      if (t.event.label.equals(e.label))
      { Expression tguard = t.guard;
        BasicExpression be2 = new BasicExpression(t.source.label);
        BasicExpression be3 = new BasicExpression(t.target.label);
        Expression iftest = new BinaryExpression("=",be,be2);
        Expression condition =
          Expression.simplify("&",iftest,tguard,null);
        if (pre.equals(""))
        { pre = "((" + condition.toB() + ")"; }
        else
        { pre = pre + " or (" + condition.toB() + ")"; }
        if (t.generations != null && t.generations.size() > 0)
        { SequenceStatement ss = t.genToSequenceStatement();
          ss.addStatement(new AssignStatement(be,be3));
          is.addCase(iftest,ss); 
        }
        else
        { is.addCase(iftest,new AssignStatement(be,be3)); }
      }
    }   /* And the condition */

    String finalpre = pre1; 
    if (pre.equals("")) { } 
    else 
    { finalpre = finalpre + " & " + pre + ")"; }  

    if (cType == DCFDArea.SENSOR)
    { return new BOperation(sensorE,finalpre,is); }
    else
    { return new BOperation(e.label + "(oo)",finalpre,is); } 
  }

  
  private BOperation buildMultImpBOperation(Event e)
  { int n = label.length();
    BasicExpression oovar = // holds value of label(oo)
      new BasicExpression(label + "x");
    // Vector vars = new Vector(); 
    // vars.add("oox");
    String lib = label.substring(0,1).toUpperCase() +
                 label.substring(1,n);
    IfStatement is = new IfStatement();
    String sensorE = label + "_" + e.label + "(oo)";
    
    for (int i = 0; i < transitions.size(); i++)
    { Transition t = (Transition) transitions.get(i);
      if (e.equals(t.getEvent())) // equal by label
      { String call = lib + "_STO_FNC_OBJ(oo,1," +
                      t.getTarget() + ")";
        InvocationStatement callstat = 
          new InvocationStatement(new Event(call));
        BasicExpression besrc =
          new BasicExpression("" + t.getSource());
        BinaryExpression iftest =
          new BinaryExpression("=",oovar,besrc);
        is.addCase(iftest,callstat);
      }
    }

    // InvocationStatement ini =
    //   new InvocationStatement(lib + 
    //                             "_VAL_FNC_OBJ(oo,1)",null,"oox");
    // SequenceStatement ss = new SequenceStatement();
    // ss.addStatement(ini);
    // ss.addStatement(is);
    // VarStatement vs =
    //   new VarStatement(vars,is);

    if (cType == DCFDArea.SENSOR)
    { return new BOperation(sensorE,"",is); }
    else
    { return new BOperation(e.label + "(oo)","",is); }
  }



  /* Converts static to action invariants: */ 
  public Vector stepBack(State v1, BinaryExpression be,
                         Expression assump, Expression conc)
  { Vector res = new Vector();
    for (int i = 0; i < states.size(); i++)
    { State from = (State) states.elementAt(i);
      for (int j = 0; j < transitions.size(); j++)
      { Transition t = (Transition) transitions.elementAt(j);
        if (t.source == from && t.target == v1)
        { Expression newE =
                  new BinaryExpression("=",
                    new BasicExpression(label),
                    new BasicExpression(from.label));
          OperationalInvariant oi =
                    opInvFromSafety(t.event.label,newE,be,
                                    assump,conc,label);
          res.add(oi); 
        } 
      } 
    }
    return res; 
  }

  /** from att = v1 (be) get
      ttick & att = v1+1 */
  public Vector stepBackAtt(String v1,
                  BinaryExpression be,
                  Expression assump,
                  Expression conc)
  { Vector res = new Vector();
    if (Expression.isNumber(v1))
    { int val = Integer.parseInt(v1);
      if (val > 0)
      { val++;
        String event = label + "tick";
        BinaryExpression newE =
          new BinaryExpression("=",
            new BasicExpression(attribute),
            new BasicExpression(""+val));
        OperationalInvariant opinv =
          opInvFromSafety(event,newE,be,
                          assump,conc,label);
        res.add(opinv);
      }
    }
    return res;
  }


  public static OperationalInvariant opInvFromSafety(String e,
                 Expression newE, Expression oldE,
                 Expression assump, Expression conc, 
                 String comp)
  { Expression cond;
    if (oldE == assump)
    { cond = newE; }
    else
    { cond = assump.substitute(oldE,newE); }
    return new OperationalInvariant(e,cond,conc,comp); }

  public boolean hasEvent(String e) 
  { if (e == null) { return false; } 
    for (int i = 0; i < events.size(); i++)
    { Event ee = (Event) events.elementAt(i); 
      if (e.equals(ee.label))
      { return true; } 
    }
    return false; 
  } 

  public boolean hasAttribute(String nme) 
  { if (attribute != null && attribute.equals(nme))
    { return true; } 
    return false; 
  } 

  public boolean hasState(String s) 
  { if (s == null) { return false; } 
    for (int i = 0; i < states.size(); i++)
    { State ee = (State) states.elementAt(i);
      if (s.equals(ee.label))
      { return true; }
    }
    return false; 
  }


  public Vector getSourceNames(String eventName) 
  { Vector res = new Vector(); 
    for (int i = 0; i < transitions.size(); i++) 
    { Transition t = (Transition) transitions.elementAt(i); 
      if (eventName.equals(t.event.label)) 
      { res.add(t.source.label); } 
    } 
    return res; 
  } 


  private void displayJavaAttributes() 
  { String stat = ""; 
    if (multiplicity <= 1) 
    { stat = "static "; } 

    System.out.print("{ public " + stat + "int " + label);
    if (initial_state == null)
    { System.out.println("; // No initial state is set!"); }
    else
    { System.out.println(" = " + initial_state.label + ";"); } 

    if (attribute != null) 
    { System.out.println("  public " + stat + "int " + attribute + " = 0;"); 
      System.out.println(); 
      System.out.println("  public " + stat + "void set" + attribute + "(int " +
                         attribute + "x)");
      System.out.println("  { " + attribute + " = " + attribute + "x; ");
      System.out.println("    System.out.println(\"" + attribute + 
                              " set to \" + " + attribute + "x); "); 
      System.out.println("  }");
    } 
  }
  // If multiplicity > 1, neither are static. 

  public void displayJavaClass()
  { String fini = ""; 
    if (multiplicity <= 1) 
    { fini = "final "; } 

    System.out.println(fini + "class M" + label);
    System.out.println("implements SystemTypes");
    displayJavaAttributes();     
    displayJavaOps();
    System.out.println("}"); 
  }

  public void displayJavaOps()
  { int n = events.size();
    Vector boperations = new Vector();
    String stat = "";  
    BOperation bop;

    if (multiplicity <= 1) 
    { stat = "static "; } 

    if (n == 0) { return; }
    System.out.println(" ");
    for (int i = 0; i < n; i++)
    {  Event e = (Event) events.elementAt(i);
       bop = buildBOperation(e);
       boperations.add(bop); 
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.elementAt(j);
      bop.displayJava(stat);
      System.out.println(""); 
    }
  }

  private void displayJavaAttributes(PrintWriter out)
  { String stat = "";
    if (multiplicity <= 1)
    { stat = "static "; }

    out.print("{ public " + stat + "int " + label);
    if (initial_state == null)
    { out.println("; // No initial state is set!"); }
    else
    { out.println(" = " + initial_state.label + ";"); }

    if (attribute != null)
    { out.println("  public " + stat + "int " + attribute + " = 0;"); 
      out.println(); 
      out.println("  public " + stat + "void set" + attribute + "(int " + 
                  attribute + "x)"); 
      out.println("  { " + attribute + " = " + attribute + "x; }"); 
      out.println();
    }
  }
  // If multiplicity > 1, neither are static.


  public void displayJavaClass(PrintWriter out)
  { String fini = ""; 
    if (multiplicity <= 1) 
    { fini = "final "; } 

    out.println(fini + "class M" + label);
    out.println("implements SystemTypes");
    displayJavaAttributes(out); 
    displayJavaOps(out);
    out.println("}"); 
  }

  public void displayJavaOps(PrintWriter out)
  { int n = events.size();
    String stat = ""; 
    Vector boperations = new Vector();
    BOperation bop;

    if (multiplicity <= 1) 
    { stat = "static "; } 

    if (n == 0) { return; }
    out.println(" ");
    for (int i = 0; i < n; i++)
    { Event e = (Event) events.elementAt(i);
      bop = buildBOperation(e);
      boperations.add(bop); 
    }

    for (int j = 0; j < n; j++)
    { bop = (BOperation) boperations.elementAt(j);
      bop.displayJava(out,stat);
      out.println(""); 
    }
  }

  /* Also need to insert prefix before dnames[i] in the controller for 
     each subsystem. */ 
  public void genHzOps(Vector dspecs, int finished)
  { Vector dnames = getRelevantSpecs(dspecs);
    int nOfSubsystems = dnames.size();

    for (int i = 0; i < events.size(); i++)
    { Event ev = (Event) events.get(i);
      System.out.print(ev.label + " = ");
      if (nOfSubsystems == 0)
      { System.out.println("skip;"); }
      else if (nOfSubsystems == 1)
      { System.out.println(dnames.get(0) +  "_" + ev.label + ";"); }
      else // I am shared between several subsystems
      { System.out.println();
        System.out.println("  BEGIN"); 
        for (int j = 0; j < nOfSubsystems; j++) 
        { String dname = (String) dnames.get(j);
          System.out.print("    " + dname + "_" + dname + ev.label);
          if (j < nOfSubsystems - 1)
          { System.out.println(" ||"); }
        }
        System.out.println();
        System.out.print("  END"); 
        if ((finished == 1) && (i == events.size() - 1)) {} 
        else { System.out.println(";"); } 
      }
      System.out.println();
    }
  }

public void genHzOps(Vector dspecs, int finished, PrintWriter out)
{ Vector dnames = getRelevantSpecs(dspecs);
  int nOfSubsystems = dnames.size();
  for (int i = 0; i < events.size(); i++)
  { Event ev = (Event) events.get(i);
    out.print(ev.label + " = ");
    if (nOfSubsystems == 0)
    { out.println("skip;"); }
    else if (nOfSubsystems == 1)
    { out.println(dnames.get(0) +  "_" + ev.label + ";"); }
    else
    { out.println();
      out.println("  BEGIN");
      for (int j = 0; j < nOfSubsystems; j++)
      { String dname = (String) dnames.get(j);
        out.print("    " + dname + "_" + dname + ev.label);
        if (j < nOfSubsystems - 1)
        { out.println(" ||"); }
      }
      out.println();
      out.print("  END");
      if ((finished == 1) && (i == events.size() - 1)) {}
      else { out.println(";"); }
    }
    out.println();
  }
}

// Needs to rename the events called if the sensors were shared 
public void genJavaHzOps(Vector dspecs)
{ Vector dnames = getRelevantSpecs(dspecs);
  int nOfSubsystems = dnames.size();
  for (int i = 0; i < events.size(); i++)
  { Event ev = (Event) events.get(i);
    System.out.print("  public void " + ev.label + "() ");
    if (nOfSubsystems == 0)
    { System.out.println("{}"); }
    else if (nOfSubsystems == 1)
    { String dname = (String) dnames.get(0); 
      System.out.println("{ " + dname.toLowerCase() +  "." + ev.label + "(); }"); 
    } // Not ev.label if I was renamed in dname 
    else 
    { System.out.println();
      System.out.println("  {"); 
      for (int j = 0; j < nOfSubsystems; j++) 
      { String dname = (String) dnames.get(j);
        System.out.print("    " + dname.toLowerCase() + "." + dname + 
                         ev.label + "();");
        if (j < nOfSubsystems - 1)
        { System.out.println(""); }
      }
      System.out.println();
      System.out.println("  }"); 
    }
    System.out.println();
  }
}

 public void genJavaHzOps(Vector dspecs, PrintWriter out)
 { Vector dnames = getRelevantSpecs(dspecs);
   int nOfSubsystems = dnames.size();
   for (int i = 0; i < events.size(); i++)
   { Event ev = (Event) events.get(i);
     out.print("  public void " + ev.label + "() ");
     if (nOfSubsystems == 0)
     { out.println("{}"); }
     else if (nOfSubsystems == 1)
     { String dname = (String) dnames.get(0);
       out.println("{ " + dname.toLowerCase() +  "." + ev.label + "(); }"); 
     } // Not ev.label if I was renamed in dname 
     else
     { out.println();
       out.println("  {");
       for (int j = 0; j < nOfSubsystems; j++)
       { String dname = (String) dnames.get(j);
         out.print("    " + dname.toLowerCase() + "." + dname +
                         ev.label + "();");
         if (j < nOfSubsystems - 1)
         { out.println(""); }
      }
      out.println();
      out.println("  }");
    }
    out.println();
  }
}

  public Object clone()
  { State istat = initial_state;
    String att; 
    Vector evs = (Vector) events.clone();
    Vector trans = (Vector) transitions.clone();
    Vector stats = (Vector) states.clone();
    if (attribute != null) 
    { att = new String(attribute); } 
    else 
    { att = null; } 
      // Also gets renamed by prefix
    int mult = multiplicity;
    int ctyp = cType;
    SmBuilder builder = myTemplate;
    Statemachine res = 
      new Statemachine(istat,evs,trans,stats); 
    res.label = label; // also change
    res.setAttribute(att); // rename
    res.setMultiplicity(mult);
    res.setMaxval(maxval); 
    res.myTemplate = builder; // name?
    res.setcType(ctyp);
    return res;
  }

  public static Statemachine copyStatemachine(Statemachine sc, String dname)
  { String dlabel = dname + sc.label;
    StatechartArea oldsa = sc.gui;
    Vector oldvis = oldsa.visuals; 
    Vector newvis = new Vector(); 
    Vector neweventlist = 
      (Vector) oldsa.eventlist.clone();
    Statemachine newsc =
      (Statemachine) sc.clone();
    newsc.label = dlabel;
    String att = newsc.getAttribute();
    if (att != null)
    { newsc.setAttribute(dname + att); }
    // rename sa.eventlist and
    // newsc.events with this prefix,
    // should also rename events of transitions

    // also need to rename in B
    StateWin sw = new StateWin(dlabel,null);  // title?? 
    sw.setTitle(dlabel); 
    StatechartArea sa = sw.getDrawArea();

    Vector newtransitions = new Vector(); 
    Vector finaleventlist = Named.prefixAll(dname,neweventlist); 
    System.out.println("New event list: " + finaleventlist); 
    Vector newevents = new Vector(); 

    for (int j = 0; j < oldvis.size(); j++) 
    { VisualData vd = (VisualData) oldvis.get(j); 
      if (vd instanceof LineData) 
      { LineData vd1 = (LineData) vd; 
        LineData ld = (LineData) vd1.clone(); 
        Transition tr = ld.transition; 
        Transition newtr = (Transition) tr.clone(); 
        ld.label = dname + tr.event.label;  // Line name always == event name
        ld.setTransition(newtr); 
        Event newev = (Event) VectorUtil.lookup(ld.label,finaleventlist); 
        newtr.setEvent(newev); 
        
        newvis.add(ld); 
        newtransitions.add(newtr); 
        if (newevents.contains(newev)) { } 
        else 
        { newevents.add(newev); } 
      } 
      else 
      { newvis.add(vd); }  // States are shared between the copies. 
    } 
    sa.setVisuals(newvis);
    sa.setModule(newsc);
    newsc.gui = sa;
    newsc.setcType(sc.cType); 
    newsc.setTransitions(newtransitions); 
    newsc.setEvents(newevents); 
    sa.setEvents(newevents); 
    sw.setSize(300,300);
    sw.setVisible(true);
    return newsc;
  }



  /** Finds all the dspecs which need to be notified of 
      events of this state machine. */ 
  private Vector getRelevantSpecs(Vector dspecs)
  { Vector res = new Vector();
    for (int i = 0; i < dspecs.size(); i++)
    { DCFDSpec ds = (DCFDSpec) dspecs.get(i);
      Statemachine sm = (Statemachine) VectorUtil.lookup(label,ds.sensors);
      /* Won't work if we clone and rename 'em! */
      if (sm == null) 
      { sm = (Statemachine) VectorUtil.lookup(ds.label + label,ds.sensors); } 

      if (sm != null)
      { res.add(ds.label); }  // In this case also prefix the event called. 
    }
    return res; 
  }

  public static String genHzIncludeList(Vector dspecs)
  { String res = "";
    if (dspecs.size() > 0)
    { res = "INCLUDES ";
      for (int i = 0; i < dspecs.size(); i++)
      { DCFDSpec ds = (DCFDSpec) dspecs.get(i);
        res = res + ds.label;
        if (i < dspecs.size() - 1)
        { res = res + ", "; }
      }  
    }
    return res;
  }

public Vector missingOpspecs(Vector opevents, String prefix)
{ Vector res = new Vector();
  for (int i = 0; i < events.size(); i++)
  { Event ev = (Event) events.get(i);
    if (opevents.contains(ev.label))
    {}
    else 
    { System.out.println("No operation for event " + ev.label +
        " -- creating a default action");
      SkipOperationSpec op =
        new SkipOperationSpec(ev.label,label);
      op.setPrefix(prefix); 
      res.add(op); 
    }
  }
  return res;
}



  public OrState toOrState()
  { OrState res = new OrState(label,initial_state,transitions,states);
    return res; 
  }

  // Also save the attribute if it exists, and multiplicity, entry actions
  public void saveData(String fileName, Vector vis)
  { int n = events.size();
    int m = states.size();
    int an = attributes.size(); 

    int count = 0;
    int[] datax = new int[m];
    int[] datay = new int[m];
    String[] datan = new String[m];
    String[] data = new String[n];
    String[] dataentry = new String[m]; 
    int[] mults = { multiplicity, maxval }; 

    int p = transitions.size();
    int[] dataxstart = new int[p];
    int[] dataxend = new int[p];
    int[] dataystart = new int[p];
    int[] datayend = new int[p];
    String[] transnames = new String[p];
    String[] transgens = new String[p]; 
    String[] transguards = new String[p]; 
    String[] attnames = new String[an]; 
    int countt = 0;

    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      attnames[i] = att.getName(); 
    } 

    try 
    { ObjectOutputStream out = 
        new ObjectOutputStream(
          new FileOutputStream(fileName));
      for (int i = 0; i < n; i++)
      { data[i] = ((Event) events.get(i)).label; }

      for (int i = 0; i < vis.size(); i++)
      { VisualData vd = (VisualData) vis.get(i);
        if (vd instanceof RoundRectData &&
            count < m)
        { RoundRectData rd = (RoundRectData) vd;
          datax[count] = rd.getx();
          datay[count] = rd.gety();
          datan[count] = rd.state.label; 
          dataentry[count] = "" + rd.state.getEntryAction();
          count++; 
        }
        else if (vd instanceof LineData &&
                 countt < p)
        { LineData ld = (LineData) vd;
          dataxstart[countt] = ld.xstart;
          dataxend[countt] = ld.xend;
          dataystart[countt] = ld.ystart;
          datayend[countt] = ld.yend;
          if (ld.transition.event != null) 
          { transnames[countt] = ld.transition.event.label; 
            Vector gens = ld.transition.getGenerations();
            if (gens == null || gens.size() == 0)
            { transgens[countt] = ""; } 
            else 
            { transgens[countt] = "" + gens.get(0); } 
            transguards[countt] = "" + ld.transition.guard; 
          }
          else 
          { transnames[countt] = ""; 
            transgens[countt] = ""; 
            transguards[countt] = ""; 
          }  
          countt++; 
        }
      }

      out.writeObject(data);
      out.writeObject(datax);
      out.writeObject(datay);
      out.writeObject(datan);
      out.writeObject(dataxstart);
      out.writeObject(dataystart);
      out.writeObject(dataxend);
      out.writeObject(datayend);
      out.writeObject(transnames);
      out.writeObject(transgens); 
      out.writeObject(transguards); 

      out.writeObject(mults); 
      out.writeObject(attribute);
      out.writeObject(attnames);
      out.close(); 
      System.out.println("Written data"); 
    }
    catch (IOException e) 
    { System.err.println("Error in writing"); }
  }

  public static StatechartData retrieveData(String fileName, int cType)
  { String[] data;
    int[] datax;
    int[] datay;
    String[] datan;
    String[] dataentry; 

    int[] dataxstart;
    int[] dataxend;
    int[] dataystart;
    int[] datayend;
    String[] transnames; 
    String[] transgens; 
    String[] transguards; 
    String[] attnames; 
    // and attribute, multiplicity. 

    StatechartData sd;

    FileInputStream fs;
    ObjectInputStream in;
    Vector rects = new Vector(); 
    Vector newstates = new Vector();
    Vector newevents = new Vector();
    Vector newlines = new Vector();  

    try 
    { fs = new FileInputStream(fileName); 
      in = new ObjectInputStream(fs);
 
      data = (String[]) in.readObject();

      datax = (int[]) in.readObject();
      datay = (int[]) in.readObject();
      datan = (String[]) in.readObject();
      int count = datax.length;

      for (int i = 0; i < data.length; i++)  
      { newevents.add(new Event(data[i])); }  
          
      for (int i = 0; i < count; i++)
      { BasicState state = 
          new BasicState(datan[i]);
        RoundRectData rd = 
          new RoundRectData(datax[i],datay[i],Color.black,i);
        rd.setState(state);
        rd.setName(datan[i]); 
        rects.add(rd);
        newstates.add(state); 
      }

      dataxstart = (int[]) in.readObject();
      dataystart = (int[]) in.readObject();
      dataxend = (int[]) in.readObject();
      datayend = (int[]) in.readObject();
      transnames = (String[]) in.readObject();
      transgens = (String[]) in.readObject(); 
      transguards = (String[]) in.readObject(); 

      int[] mults = (int[]) in.readObject(); 
      String att = (String) in.readObject(); 
      attnames = (String[]) in.readObject(); 

      int countt = transnames.length;
      for (int j = 0; j < countt; j++)
      { String tr = transnames[j];
        LineData ld = 
          new LineData(dataxstart[j],
                       dataystart[j],
                       dataxend[j],
                       datayend[j],j, 
                       StatechartArea.SOLID); /* 1-cType DASHED or SOLID */
        ld.setName(tr);
        newlines.add(ld); 
      }

      sd = new StatechartData(newevents, rects, 
                              newstates, newlines, mults, att,
                              transnames, transgens, transguards, attnames);
      System.out.println(newevents); 
      System.out.println(rects); 
      System.out.println(newstates); 
      System.out.println(att + " " + mults[0] + " " + mults[1]); 

      return sd;
    }
    catch (IOException e) 
    { System.err.println("Error reading data"); }
    catch (ClassNotFoundException f)
    { System.err.println("Class not found"); }
    return null;
  } 


 public String saveData()
 { if (myTemplate == null)
   { saveData(label + ".srs", gui.visuals); 
     return "Custom " + label + ".srs"; //  + 
            // attribute + " " + maxval + " " + multiplicity; 
   }
   else
   { return myTemplate.saveData() + " " + attribute + " " + maxval + " " +
            multiplicity;
   }
 }
 

public void saveEvents(String fileName)
{ int n = events.size();
  String[] data = new String[n];
  try 
  { ObjectOutputStream out = 
      new ObjectOutputStream(
        new FileOutputStream(fileName));
    for (int i = 0; i < n; i++)
    { data[i] = ((Event) events.get(i)).label; }
    out.writeObject(data);
    out.close(); 
    System.out.println("Written data"); }
  catch (IOException e) 
  { System.out.println("Error in writing"); }
}

public Vector retrieveEventlist(String fileName)
{ String[] data;
  int[] data2; 
  FileInputStream fs;
  ObjectInputStream in;
  Vector res = new Vector(); 
  try 
  { fs = new FileInputStream(fileName); 
    in = new ObjectInputStream(fs); 
    data = (String[]) in.readObject();
    data2 = (int[]) in.readObject(); 
    for (int i = 0; i < data.length; i++)  
    { res.add(data[i]); }   /* add(new Event(data[i])) */
    
    for (int j = 0; j < data2.length; j++)
    { System.out.println(data2[j]); }  
  }
  catch (IOException e) 
  { System.out.println("Error reading data"); }
  catch (ClassNotFoundException f)
  { System.out.println("Class not found"); }
  return res; 
}

  public String genBPhaseCIncludes(Vector dspecs)
  { String res = "INCLUDES M" + label; 
    int phases = dspecs.size();
    if (phases == 0)   /* Should never happen */
    { return res; }
    else 
    { for (int i = 0; i < phases; i++)
      { DCFDSpec ds = (DCFDSpec) dspecs.get(i); 
        res = res + ", " + ds.label; }
    }
    return res;
  }

  public void genBPhaseCOps()
  { for (int i = 0; i < events.size(); i++)
    { Event ev = (Event) events.get(i);
      System.out.println("  " + ev.label + " = " 
                              + label + "_" + ev.label + ";"); 
      System.out.println(); }
  }

  public void genOtherBPhaseCOps(Statemachine sm, Vector dspecs)
  { for (int i = 0; i < events.size(); i++)
    { Event ev = (Event) events.get(i);
      Vector rels = ev.getBPDRelevantControllers(dspecs);
      if (rels.size() == 0)  /* no phase interested in ev */
      { System.out.println("  " + ev.label + " = skip;"); }
      else 
      { System.out.println("  " + ev.label + " ="); 
        sm.displayBPCOtherop(ev.label,rels); 
      }
    }
  }

  public void displayBPCOtherop(String ev, Vector rels)
  { for (int i = 0; i < states.size(); i++)
    { State ss = (State) states.get(i);
      System.out.print("    IF " + label + " = " + ss.label +
                       " THEN "); 
      if (rels.contains("C" + label + ss.label))
      { System.out.println("C" + label + ss.label + "_" + ev); }
      else 
      { System.out.println("skip"); }
      if (i < states.size() - 1) 
      { System.out.println("    ELSE"); }
    }
    System.out.print("    ");
    for (int j = 0; j < states.size(); j++)
    { System.out.print("END  "); }
  }

  public boolean controllerCompleteness(Vector actnames, PrintWriter out)
  { boolean res = true;
    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.get(i);
      boolean stc = st.stateCompleteness(actnames);
      if (stc) { }
      else 
      { System.err.println("Incomplete state: " + st); 
        out.println("Incomplete state: " + st);
        res = false;
      }
    }
    return res;
  }


  public boolean controllerConsistency(PrintWriter out)
  { boolean res = true;
    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.get(i);
      boolean stc = st.stateConsistency();
      if (stc) { }
      else 
      { System.err.println("Inconsistent state: " + st); 
        out.println("Inconsistent state: " + st);
        res = false;
      }
    }
    return res;
  }

  public boolean opInvCheck(OperationalInvariant inv,
                            State st,
                            Vector allvars)
  { Expression ante = inv.antecedent();
    Expression succ = inv.succedent();
    String ev = inv.getEventName(); 
    boolean anteTrue = st.satisfies(ante,allvars);
    if (anteTrue)
    { for (int i = 0; i < transitions.size(); i++)
      { Transition tr = (Transition) transitions.get(i);
        if (tr.event.equals(ev) &&
            tr.source == st)
        { boolean succTrue =
            tr.target.satisfies(succ,allvars);
          if (!succTrue)
          { return false; }
        }
      }
      return true; // all successors satisfy succ
    }
    return true; // ante false
  }

  public void verifyInvariant(TemporalInvariant inv) 
  { boolean res = temporalInvCheck(inv,initial_state,variables); 
    if (res) 
    { System.out.println("Invariant " + inv + " is satisfied at initial state"); } 
    else 
    { System.out.println("Invariant " + inv + " is false at initial state"); } 
  } 

  public boolean temporalInvCheck(TemporalInvariant inv, State st, 
                                  Vector allvars)
  { Expression ante = inv.antecedent();
    Expression succ = inv.succedent();
    Vector ops = inv.getModalOp(); 
    boolean anteTrue = st.satisfies(ante,allvars);
    if (anteTrue)
    { boolean succTrue = 
        satisfies(succ,st,ops,new Vector(),allvars); 
      if (!succTrue)
      { System.out.println("Succedent " + succ + " not satisfied at " + st);
        return false; 
      }
      return true; 
    } 
    return true; // ante false
  }

  public boolean satisfies(Expression e,
                           State st,
                           Vector modalOps,
                           Vector path,
                           Vector allvars)
  { if (modalOps.size() == 0)
    { return st.satisfies(e,allvars); }
    String op = (String) modalOps.get(0);
    Vector newOps = (Vector) modalOps.clone();
    newOps.remove(0);
    Vector newpath = (Vector) path.clone();

    if (op.equals("AX"))
    { for (int i = 0; i < transitions.size(); i++)
      { Transition tr = (Transition) transitions.get(i);
        if (tr.source == st)
        { newpath.add(st);
          boolean succTrue =
            satisfies(e,tr.target,newOps,newpath,
                      allvars);
          if (!succTrue)
          { System.out.println("AX false at: " + tr.target); 
            return false; 
          }
        }
      }
      return true; // all successors satisfy succ
    }
    else if (op.equals("EX"))
    { return verifyEX(e,st,newOps,newpath,allvars); }
    else if (op.equals("EF"))
    { return verifyEF(e,st,newOps,newpath,allvars); }
    else if (op.equals("AF"))
    { return verifyAF(e,st,newOps,newpath,allvars); }
    else if (op.equals("AG"))
    { return verifyAG(e,st,newOps,newpath,allvars); }
    else if (op.equals("EG"))
    { return verifyEG(e,st,newOps,newpath,allvars); }
    return false; // unknown
  }

  private boolean verifyEX(Expression e, State st,
                           Vector newOps, Vector newpath,
                           Vector allvars)
  { for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.source == st)
      { newpath.add(st);
        boolean succTrue =
          satisfies(e,tr.target,newOps,newpath,
                    allvars);
        if (succTrue)
        { System.out.println("EX verified at: " + tr.target); 
          return true; 
        }
      }
    }
    return false; // all successors fail succ
  } 

// To optimise, just consider outgoing transitions of
// state

  private boolean verifyEF(Expression e, State st,
                           Vector newOps, Vector newpath,
                           Vector allvars)
  { if (satisfies(e,st,newOps,newpath,allvars))
    { System.out.println("EF " + e + " verified at state " + st); 
      return true; 
    }
    if (newpath.contains(st))
    { System.out.println("EF " + e + " counterexample path: " + newpath); 
      return false; 
    }  // no state with e found yet
    for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.source == st)
      { Vector path2 = (Vector) newpath.clone();
        path2.add(st);
        boolean succTrue =
          verifyEF(e,tr.target,newOps,path2,
                   allvars);
        if (succTrue)
        { return true; }
      }
    }
    return false; // all successors fail e
  }

  private boolean verifyAF(Expression e, State st,
                           Vector newOps, Vector newpath,
                           Vector allvars)
  { if (satisfies(e,st,newOps,newpath,allvars))
    { return true; }
    if (newpath.contains(st))
    { System.out.println("AF " + e + " counterexample path: " + newpath);
      return false; 
    }  // no state with e found yet
    for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.source == st)
      { Vector path2 = (Vector) newpath.clone();
        path2.add(st);
        boolean succTrue =
          verifyAF(e,tr.target,newOps,path2,
                   allvars);
        if (!succTrue)
        { return false; }
      }
    }
    System.out.println("AF " + e + " verified at state " + st);
    return true; // all successors satisfy AF
  }

  private boolean verifyAG(Expression e, State st,
                           Vector newOps, Vector newpath,
                           Vector allvars)
  { if (satisfies(e,st,newOps,newpath,allvars)) { }
    else
    { System.out.println("AG " + e + " counterexample: " + st); 
      return false; 
    }
    if (newpath.contains(st))
    { return true; }  // checked all states on that path
    for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.source == st)
      { Vector path2 = (Vector) newpath.clone();
        path2.add(st);
        boolean succTrue =
          verifyAG(e,tr.target,newOps,path2,
                   allvars);
        if (!succTrue)
        { return false; }
      }
    }
    return true; // all successors satisfy AG
  }

  private boolean verifyEG(Expression e, State st,
                           Vector newOps, Vector newpath,
                           Vector allvars)
  { if (satisfies(e,st,newOps,newpath,allvars)) { }
    else
    { System.err.println("EG " + e + " counterexample: " + st);
      return false;
    }
    if (newpath.contains(st))
    { return true; }  // finished this path
    for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.source == st)
      { Vector path2 = (Vector) newpath.clone();
        path2.add(st);
        boolean succTrue =
          verifyEG(e,tr.target,newOps,path2,
                   allvars);
        if (succTrue)
        { return true; }
      }
    }
    return false; // all successors fail EG
  }

  public void synthesiseJava()
  { File file = new File("output/tmp");
    Type tp = null; 

    try
    { PrintWriter out = new PrintWriter(
                          new BufferedWriter(
                            new FileWriter(file)));
      if (modelElement != null && 
          (modelElement instanceof BehaviouralFeature))
      { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
        // declaration of method and local variables
        String nme = bf.getName(); 
        String pars = bf.getJavaParameterDec(); 
        tp = bf.getResultType(); 
        String ts = "void";
        if (tp != null) 
        { ts = tp.getJava(); } 
        out.print("public " + ts + " " + nme + "(" + pars + ")\n{  "); 
        if (tp != null)
        { out.println(ts + " result;\n"); } 
      } 
      else 
      { System.err.println("Not a statechart for a method!"); 
        return; 
      } 

      for (int i = 0; i < attributes.size(); i++)
      { Attribute att = (Attribute) attributes.get(i);
        att.generateMethodJava(out);
      }
      // and entry action of initial state if any
      Expression tent = initial_state.getEntryAction(); 
      if (tent != null) 
      { Statement entrystat = Transition.generationToJava(tent); 
        out.println("  " + entrystat.toStringJava()); 
      } 

      loopStates(out,tp); 
      
      // if (tp != null) 
      // { out.println("    return result;"); } // and in the body
      out.println("}");
      out.close();  
    } catch (Exception e) 
    { System.err.println("Error in processing code"); 
      e.printStackTrace(); 
      return;
    } 
    
    new TextDisplay("Java code of operation","output/tmp");
  } 


  public Vector loopStates(PrintWriter out, Type tp)
  { // identifies those states with paths back to themselves
    Vector res = new Vector(); 
    Vector seenstates = new Vector(); 
    Vector seentransitions = new Vector(); 


    if (transitions.size() == 0)
    { return res; } 

    if (initial_state == null) 
    { System.err.println("Method statechart must have an initial state!"); 
      return res; 
    } 
    seenstates.add(initial_state);  
    java.util.Map looppaths = new java.util.HashMap(); 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition tr = (Transition) transitions.get(i); 
      if (tr.source == initial_state) 
      { Vector loops = getLoops(tr,seentransitions,seenstates,
                                new Vector(),new Vector(),looppaths); 
        res = VectorUtil.union(loops,res);
        seenstates.clear();  
        seenstates.add(initial_state);  
      }
    } 
    System.out.println(looppaths); 
    for (int i = 0; i < res.size(); i++) 
    { State st = (State) res.get(i); 
      st.setStateKind(State.LOOPSTATE); 
    } 
    Statement code = synthesiseCode(res,looppaths,tp); 
    code.displayJava(null,out); 
    gui.repaint(); 
    return res; 
  } 

  public Statement getMethodJava()
  { Type tp = null; 
    SequenceStatement res = new SequenceStatement(); 

    if (modelElement != null && 
        (modelElement instanceof BehaviouralFeature))
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
        // declaration of method and local variables
      String nme = bf.getName(); 
      String pars = bf.getJavaParameterDec(); 
      tp = bf.getResultType(); 
      String ts = "void";
      if (tp != null) 
      { ts = tp.getJava(); } 
        System.out.print("public " + ts + " " + nme + "(" + pars + ")\n{  "); 
        if (tp != null)
        { System.out.println(ts + " result;\n"); } 
      } 
      else 
      { System.err.println("Not a statechart for a method!"); 
        return null; 
      } 

      for (int i = 0; i < attributes.size(); i++)
      { Attribute att = (Attribute) attributes.get(i);
        Statement assign = att.generateMethodJava();
        if (assign != null) 
        { res.addStatement(assign); } 
      }
      // and entry action of initial state if any
      Expression tent = initial_state.getEntryAction(); 
      if (tent != null) 
      { Statement entrystat = Transition.generationToJava(tent); 
        res.addStatement(entrystat); 
      } 

      Statement body = methodBodyCode(tp); 
      res.addStatement(body); 
      gui.repaint(); 

      return res;       
  } 

  public Statement methodBodyCode(Type tp)
  { // identifies those states with paths back to themselves
    SequenceStatement res = new SequenceStatement(); 
    Vector newloops = new Vector(); 
    Vector seenstates = new Vector(); 
    Vector seentransitions = new Vector(); 


    if (transitions.size() == 0)
    { return res; } 

    if (initial_state == null) 
    { System.err.println("Method statechart must have an initial state!"); 
      return res; 
    } 
    seenstates.add(initial_state);  
    java.util.Map looppaths = new java.util.HashMap(); 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition tr = (Transition) transitions.get(i); 
      if (tr.source == initial_state) 
      { Vector loops = getLoops(tr,seentransitions,seenstates,
                                new Vector(),new Vector(),looppaths); 
        newloops = VectorUtil.union(loops,newloops);
        seenstates.clear();  
        seenstates.add(initial_state);  
      }
    } 
    // System.out.println(looppaths); 
    for (int i = 0; i < newloops.size(); i++) 
    { State st = (State) newloops.get(i); 
      st.setStateKind(State.LOOPSTATE); 
    } 
    Statement code = synthesiseCode(newloops,looppaths,tp); 
    if (code != null) { return code; } 
    gui.repaint(); 
    return res; 
  } 


  public void synthesiseB()
  { File file = new File("output/tmp");
    Type tp = null; 

    try
    { PrintWriter out = new PrintWriter(
                          new BufferedWriter(
                            new FileWriter(file)));
      if (modelElement != null && 
          (modelElement instanceof BehaviouralFeature))
      { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
        // declaration of method and local variables
        String nme = bf.getName(); 
        String pars = bf.getJavaParameterDec(); 
        tp = bf.getResultType(); 
        String ts = "";
        if (tp != null) 
        { ts = "result <-- "; } 
        out.println("  " + ts + " " + bf + " = \n   BEGIN"); 
        // BExpression pre = bf.getBParameterDec(); 
        // out.println("PRE " + pre + "\n  THEN\n    "); 
      } 
      else 
      { System.err.println("Not a statechart for a method!"); 
        return; 
      } 

      
      for (int i = 0; i < attributes.size(); i++)
      { Attribute att = (Attribute) attributes.get(i);
        att.generateMethodB(out);
      }
      // and entry action of initial state if any
      Expression tent = initial_state.getEntryAction(); 
      if (tent != null) 
      { Statement entrystat = Transition.generationToJava(tent); 
        entrystat.displayImp("",out); 
      } 

      bloopStates(out,tp); 
      
      // if (tp != null) 
      // { out.println("    return result;"); } // and in the body
      for (int i = 0; i < attributes.size(); i++)
      { out.println("    END"); }

      out.println("  END;\n");
      out.close();  
    } catch (Exception e) 
    { System.err.println("Error in processing code"); 
      e.printStackTrace(); 
      return;
    } 
    
    new TextDisplay("B code of operation","output/tmp");
  } 


  public Vector bloopStates(PrintWriter out, Type tp)
  { // identifies those states with paths back to themselves
    Vector res = new Vector(); 
    Vector seenstates = new Vector(); 
    Vector seentransitions = new Vector(); 


    if (transitions.size() == 0)
    { return res; } 

    if (initial_state == null) 
    { System.err.println("Method statechart must have an initial state!"); 
      return res; 
    } 
    seenstates.add(initial_state);  
    java.util.Map looppaths = new java.util.HashMap(); 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition tr = (Transition) transitions.get(i); 
      if (tr.source == initial_state) 
      { Vector loops = getLoops(tr,seentransitions,seenstates,
                                new Vector(),new Vector(),looppaths); 
        res = VectorUtil.union(loops,res);
        seenstates.clear();  
        seenstates.add(initial_state);  
      }
    } 
    System.out.println(looppaths); 
    for (int i = 0; i < res.size(); i++) 
    { State st = (State) res.get(i); 
      st.setStateKind(State.LOOPSTATE); 
      Expression linv = st.getInvariant(); 
      if (linv == null) 
      { System.err.println("State " + st + " needs a loop invariant"); } 
    } 
    Statement code = synthesiseCode(res,looppaths,tp); 
    code.displayImp("",out); 
    gui.repaint(); 
    return res; 
  } 


  private Vector getLoops(Transition tr, Vector seentrans, Vector seenstates, 
                          Vector loops, Vector transpath, java.util.Map looppaths)
  { System.out.println(tr.source + "--" + tr + "-->" + tr.target); 
    
    if (seentrans.contains(tr))
    { return loops; } 
    if (seenstates.contains(tr.target))
    { Vector looppath = getPathBetween(tr,transpath);
      System.out.println("Loop " + looppath + " to " + tr.target); 
      loops.add(tr.target);
      Vector mypaths = (Vector) looppaths.get(tr.target); 
      if (mypaths == null) 
      { mypaths = new Vector(); 
        mypaths.add(looppath); 
        looppaths.put(tr.target,mypaths); 
      } 
      else 
      { mypaths.add(looppath); } 
    }
    else 
    { seenstates.add(tr.target); 
      transpath.add(tr); 
    } 
    Vector path = (Vector) seenstates.clone(); // path so far
    Vector newtranspath = (Vector) transpath.clone(); 

    seentrans.add(tr); 
    for (int i = 0; i < transitions.size(); i++) 
    { Transition tr1 = (Transition) transitions.get(i); 
      if (tr1.source == tr.target) 
      { getLoops(tr1,seentrans,path,loops,newtranspath,looppaths);
        path.clear(); 
        newtranspath.clear(); 
        path.addAll(seenstates);
        // newtranspath.addAll(transpath);  
      }
    } 
    return loops; 
  }    

  private Vector getPathBetween(Transition tran, Vector path)
  { Vector res = new Vector(); 
    for (int i = 0; i < path.size(); i++)
    { Transition tr = (Transition) path.get(i); 
      if (tr.source == tran.target)
      { res.add(tr); } 
      else if (res.size() > 0)
      { res.add(tr); } 
    }
    res.add(tran); 
    return res; 
  } 

  public void setupOutgoingIncoming()
  { // Setup trans_out of states: 
    for (int i = 0; i < states.size(); i++)
    { State s = (State) states.get(i); 
      s.clearOutgoingTransitions(); 
      s.clearIncomingTransitions(); 
    } 
    for (int j = 0; j < transitions.size(); j++) 
    { Transition tr = (Transition) transitions.get(j); 
      if (tr.source != null) 
      { tr.source.addOutgoingTransition(tr); } 
      if (tr.target != null) 
      { tr.target.addIncomingTransition(tr); }  
    }
  }

  public Statement synthesiseCode(Vector loopstates, java.util.Map looppaths,
                                  Type tp)
  { if (initial_state == null)
    { System.err.println("Initial state must exist!"); 
      return null; 
    } 

    // Setup trans_out of states: 
    for (int i = 0; i < states.size(); i++)
    { State s = (State) states.get(i); 
      s.clearOutgoingTransitions(); 
    } 
    for (int j = 0; j < transitions.size(); j++) 
    { Transition tr = (Transition) transitions.get(j); 
      tr.source.addOutgoingTransition(tr); 
    }
    java.util.Map containedIn = new java.util.HashMap(); 
      // st1 |-> st2  in this means st1 is directly contained in loop/if st2

    // create assignment statements for all attributes: 
    Statement ss = buildCode(initial_state,null,containedIn,loopstates,looppaths,tp);
    if (modelElement instanceof Entity)
    { ss.setEntity((Entity) modelElement); }  
    else 
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
      ss.setEntity(bf.getEntity()); 
    } 
    return ss; 
  }

  public Statement buildCode(State st, State scope, java.util.Map containedIn,
                             Vector loopstates, java.util.Map looppaths, Type tp)
  { Vector outtrs = st.outgoingTransitions(); 
    containedIn.put(st,scope); 

    if (outtrs.size() == 0)
    { // termination of the operation
      st.setStateKind(State.FINALSTATE); 
      if (tp == null) 
      { return new ReturnStatement(null); }
      return new ReturnStatement(new BasicExpression("result"));  
    }
    if (outtrs.size() >= 1)        
    { Statement stat; 
      if (loopstates.contains(st))
      { stat = buildLoop(st,outtrs,scope,containedIn,loopstates,looppaths,tp);
        return stat;
      } 
      else if (outtrs.size() > 1)
      { st.setStateKind(State.IFSTATE); 
        return buildIf(st,outtrs,scope,containedIn,loopstates,looppaths,tp);
      } 
      // else, it is a simple step: 
      else 
      { Transition tr = (Transition) outtrs.get(0); 
        stat = tr.transitionToCode(); 
        State next = tr.target; 
        Statement rest = buildCode(next,scope,containedIn,loopstates,looppaths,tp); 
        SequenceStatement res = new SequenceStatement(); 
        if (stat != null)
        { res.addStatement(stat); } 
        res.addStatement(rest); 
        return res;
      } 
    }
    return null; 
  }           

  private Statement buildLoop(State st, Vector outtrs, State scope,
                              java.util.Map containedIn, Vector loops, 
                              java.util.Map looppaths,Type tp)
  { Vector bodypaths = (Vector) looppaths.get(st); 
    if (bodypaths == null || bodypaths.size() == 0)
    { return new WhileStatement(new BasicExpression("false"),
                                new ErrorStatement());
    }
    Expression test = makeLoopTest(bodypaths); 
    IfStatement body = new IfStatement(); 

    Vector otherbranches = (Vector) outtrs.clone(); 

    for (int i = 0; i < bodypaths.size(); i++) 
    { Vector vv = (Vector) bodypaths.get(i); 
      if (vv == null || vv.size() == 0) { } 
      else 
      { Transition tr = (Transition) vv.get(0); 
        otherbranches.remove(tr);
        Vector path = (Vector) vv.clone(); 
        Statement pathcode = pathToCode(path,st,containedIn,loops,
                                        looppaths); 
        IfCase branch = new IfCase(tr.getGuard(),pathcode); 
        body.addCase(branch); 
      } 
    }
    WhileStatement thisloop = new WhileStatement(test,body);
    Expression inv = st.getInvariant(); 
    thisloop.setInvariant(inv); 
    // and deal with the otherbranches - they are in scope

    Statement afterloop = null;
    if (otherbranches.size() > 1)
    { afterloop = new IfStatement(); } 
     
    for (int j = 0; j < otherbranches.size(); j++) 
    { Transition tr = (Transition) otherbranches.get(j); 
      Statement stat = tr.transitionToCode(); 
      State next = tr.target; 
      Statement rest = buildCode(next,scope,containedIn,loops,looppaths,tp); 
      SequenceStatement pathcode = new SequenceStatement(); 
      if (stat != null) 
      { pathcode.addStatement(stat); } 
      pathcode.addStatement(rest); 

      if (afterloop != null) // an If
      { IfCase branch = new IfCase(tr.getGuard(),pathcode); 
        ((IfStatement) afterloop).addCase(branch); 
      }
      else // no more iterations
      { afterloop = pathcode; 
        break; 
      }  
    }

    
    if (afterloop == null) 
    { return thisloop; }
    else 
    { SequenceStatement res = new SequenceStatement(); 
      res.addStatement(thisloop); 
      res.addStatement(afterloop);  
      return res;
    } 
  }   

  private Statement buildIf(State st, Vector outtrs, State scope,
                            java.util.Map containedIn, Vector loops, 
                            java.util.Map looppaths, Type tp)
  { IfStatement res = new IfStatement(); 
    for (int j = 0; j < outtrs.size(); j++) 
    { Transition tr = (Transition) outtrs.get(j); 
      Statement stat = tr.transitionToCode(); 
      State next = tr.target; 
      containedIn.put(next,st); 
      Statement rest = buildCode(next,st,containedIn,loops,looppaths,tp); 
      SequenceStatement pathcode = new SequenceStatement(); 
      if (stat != null) 
      { pathcode.addStatement(stat); } 
      pathcode.addStatement(rest); 

      IfCase branch = new IfCase(tr.getGuard(),pathcode); 
      res.addCase(branch);    
    }

    if (modelElement instanceof Entity)
    { res.setEntity((Entity) modelElement); }  
    else 
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
      res.setEntity(bf.getEntity()); 
    } 

    return res; 
  } 
  
  private static Expression makeLoopTest(Vector paths)
  { Expression test = new BasicExpression("false"); 
    for (int i = 0; i < paths.size(); i++) 
    { Vector path = (Vector) paths.get(i); 
      if (path != null && path.size() > 0)
      { Transition tr = (Transition) path.get(0); 
        Expression gd = tr.getGuard(); 
        test = (new BinaryExpression("or",test,gd)).simplify(); 
      }           // convert to Java
    } 
    return test; 
  }  

  // assumes paths are all linear, no more tests:

  private static Statement pathToCode(Vector path, State scope, 
                                      java.util.Map containedIn,
                                      Vector loopstates, 
                                      java.util.Map looppaths)
  { SequenceStatement res = new SequenceStatement(); 
    Transition tr = (Transition) path.get(0); 
    Statement stat0 = tr.transitionToCode(); 
    State next = tr.target; 
    containedIn.put(next,scope); 
    if (path.size() == 1) 
    { if (stat0 != null)
      { return stat0; } 
      else 
      { return res; } 
    } 
    else 
    { path.remove(0); 
      Statement rest = pathToCode(path,scope,containedIn,loopstates,
                                  looppaths);
       
      if (stat0 != null)
      { res.addStatement(stat0); } 
      res.addStatement(rest); 
      return res; 
    } 
  }   
        
  public Statement methodStepCode()
  { // computes individual steps of method defined by the statechart
    System.out.println("Computes body of thread run_step method"); 

    IfStatement res = new IfStatement(); 
    String nme = modelElement.getName(); 
   
    Type intType = new Type("int",null); 

    String statename = nme + "_state"; 
    BasicExpression var = new BasicExpression(statename); 
    var.setType(intType); 
    var.setUmlKind(Expression.VARIABLE); 

    for (int i = 0; i < transitions.size(); i++) 
    { Transition tr = (Transition) transitions.get(i); 
      Statement stat = tr.transitionToCode(); 

      BasicExpression srcexp = new BasicExpression(tr.source.label); 
      srcexp.setType(intType); 
      srcexp.setUmlKind(Expression.VALUE); 

      Expression test = new BinaryExpression("=",var,srcexp); 
      test = (new BinaryExpression("&",tr.getGuard(),test)).simplify();

      BasicExpression trgexp = new BasicExpression(tr.target.label); 
      trgexp.setType(intType); 
      trgexp.setUmlKind(Expression.VALUE); 
 
      AssignStatement as = new AssignStatement(var,trgexp);

      Expression initexp = tr.target.getEntryAction(); 
      Statement initstat = Transition.generationToJava(initexp); 

      // Statement code; 

      // if (initstat == null)
      // { code = as; } 
      // else 
      // { code = new SequenceStatement(); 
      //   code.addStatement(initstat); 
      //   code.addStatement(as); 
      // } 

      IfCase step; 

      if (stat == null) 
      { step = new IfCase(test,as); } 
      else 
      { SequenceStatement ss = new SequenceStatement(); 
        ss.addStatement(stat); 
        ss.addStatement(as); 
        step = new IfCase(test,ss); 
      } 
      res.addCase(step); 
    } 

    if (modelElement instanceof Entity)
    { res.setEntity((Entity) modelElement); }  
    else 
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
      res.setEntity(bf.getEntity()); 
    } 

    return res; 
  } 

  public Statement runCode()
  { Statement body = new InvocationStatement(new Event("run_step()")); 
    String nme = modelElement.getName(); 
   
    String statename = nme + "_state"; 
    BasicExpression var = new BasicExpression(statename);
    Expression cond = new BasicExpression("true"); 
 
    Vector terms = getTerminalStates(); 
    for (int i = 0; i < terms.size(); i++) 
    { State st = (State) terms.get(i); 
      BinaryExpression neq = new BinaryExpression("!=",var,
                               new BasicExpression(st.label)); 
      cond = (new BinaryExpression("&",cond,neq)).simplify(); 
    } 
    WhileStatement loop = new WhileStatement(cond,body); 
    AssignStatement as = 
      new AssignStatement(var,new BasicExpression(initial_state.label));
    // and any initialisations of attributes
    Expression initexp = initial_state.getEntryAction(); 
    Statement initstat = Transition.generationToJava(initexp); 

    SequenceStatement ss = new SequenceStatement(); 
    ss.addStatement(as); 
    if (initstat != null) 
    { ss.addStatement(initstat); } 
    ss.addStatement(loop); 

    if (modelElement instanceof Entity)
    { ss.setEntity((Entity) modelElement); }  
    else 
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
      ss.setEntity(bf.getEntity()); 
    } 

    return ss; 
  } // add run_state and its type to the class
 
  public Statement getSequentialCode()
  { Statement body = methodStepCode(); 
    String nme = modelElement.getName(); 
   
    String statename = nme + "_state"; 
    BasicExpression var = new BasicExpression(statename);
    Type intType = new Type("int",null); 
    var.setType(intType); 
    var.setUmlKind(Expression.VARIABLE); 

    Expression cond = new BasicExpression("true"); 
     
    Vector terms = getTerminalStates(); 
    for (int i = 0; i < terms.size(); i++) 
    { State st = (State) terms.get(i); 
      BasicExpression stvalue = new BasicExpression(st.label); 
      stvalue.setType(intType); 
      stvalue.setUmlKind(Expression.VALUE); 
      BinaryExpression neq = new BinaryExpression("!=",var,stvalue); 
      cond = (new BinaryExpression("&",cond,neq)).simplify(); 
    } 

    CreationStatement cstat = new CreationStatement("int",statename); 

    WhileStatement loop = new WhileStatement(cond,body); 

 
    BasicExpression initvalue = new BasicExpression(initial_state.label); 
    initvalue.setType(intType); 
    initvalue.setUmlKind(Expression.VALUE);  
    AssignStatement as = new AssignStatement(var,initvalue);
    // and any initialisations of attributes
    Expression initexp = initial_state.getEntryAction(); 
    Statement initstat = Transition.generationToJava(initexp); 

    SequenceStatement ss = new SequenceStatement(); 
    for (int p = 0; p < states.size(); p++) 
    { State sta = (State) states.get(p); 
      String stname = sta.label; 
      BasicExpression stexp = new BasicExpression(stname); 
      stexp.setType(intType); 
      stexp.setUmlKind(Expression.VARIABLE); 
      BasicExpression stval = new BasicExpression("" + p); 
      stval.setType(intType); 
      stval.setUmlKind(Expression.VALUE); 
      AssignStatement stdec = new AssignStatement(stexp,stval); 
      stdec.setType(intType); 
      ss.addStatement(stdec); 
    } 
    ss.addStatement(cstat); 
    ss.addStatement(as); 

    if (initstat != null) 
    { ss.addStatement(initstat); } 
    ss.addStatement(loop); 

    return ss; 
  }             

  public Statement methodGeneralSequentialCode()
  { Statement body = methodStepCode(); 
    String nme = modelElement.getName(); 
   
    String statename = nme + "_state"; 
    BasicExpression var = new BasicExpression(statename);
    Expression cond = new BasicExpression("true"); 
 
    Vector terms = getTerminalStates(); 
    for (int i = 0; i < terms.size(); i++) 
    { State st = (State) terms.get(i); 
      BinaryExpression neq = new BinaryExpression("!=",var,
                               new BasicExpression(st.label)); 
      cond = (new BinaryExpression("&",cond,neq)).simplify(); 
    } 
    WhileStatement loop = new WhileStatement(cond,body); 
    AssignStatement as = 
      new AssignStatement(var,new BasicExpression(initial_state.label));
    // and any initialisations of attributes
    Expression initexp = initial_state.getEntryAction(); 
    Statement initstat = Transition.generationToJava(initexp); 

    SequenceStatement ss = new SequenceStatement(); 
    ss.addStatement(as); 
    if (initstat != null) 
    { ss.addStatement(initstat); } 
    ss.addStatement(loop); 

    File file = new File("output/tmp");
    Type tp = null; 

    try
    { PrintWriter out = new PrintWriter(
                          new BufferedWriter(
                            new FileWriter(file)));
      if (modelElement != null && 
          (modelElement instanceof BehaviouralFeature))
      { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
        // declaration of method and local variables
        String pars = bf.getJavaParameterDec(); 
        tp = bf.getResultType(); 
        String ts = "void";
        if (tp != null) 
        { ts = tp.getJava(); } 
        out.println("public " + ts + " " + nme + "(" + pars + ")\n{\n"); 
        displayMethodLocalDeclarations(out); 
        ss.displayJava(null,out); 
      }
      else 
      { System.err.println("Not statechart of an operation"); 
        return ss; 
      } 
    } 
    catch (Exception e) 
    { System.err.println("Error in processing code"); 
      e.printStackTrace(); 
      return ss;
    } 
    
    new TextDisplay("Java code of operation","output/tmp");
    return ss; 
  }             

  public Vector getTerminalStates()
  { // assumes analysis already done
    Vector res = new Vector(); 
    for (int i = 0; i < states.size(); i++) 
    { State st = (State) states.get(i); 
      if (st.stateKind == State.FINALSTATE)
      { res.add(st); } 
    } 
    return res; 
  } 

  public void displayMethodLocalDeclarations(PrintWriter out)
  { for (int i = 0; i < attributes.size(); i++)
    { Attribute att = (Attribute) attributes.get(i);
      att.generateMethodJava(out);
    }
  } 

  public Statement checkStructure(Type tp)
  { if (initial_state == null)
    { System.err.println("Initial state must exist!"); 
      return null; 
    } 

    boolean ok = true; 
    java.util.Map reachability = new java.util.HashMap(); 

    // Setup trans_out of states: 
    for (int i = 0; i < states.size(); i++)
    { State s = (State) states.get(i); 
      s.clearOutgoingTransitions(); 
      s.clearIncomingTransitions(); 
      reachability.put(s,new Vector()); 
    } 

    for (int j = 0; j < transitions.size(); j++) 
    { Transition tr = (Transition) transitions.get(j); 
      if (tr.source != null) 
      { tr.source.addOutgoingTransition(tr); } 
      if (tr.target != null) 
      { tr.target.addIncomingTransition(tr); }  
    }

    Vector allloops = new Vector(); 
    Vector loops = new Vector(); 
    java.util.Map loopdefs = new java.util.HashMap(); 

    Vector sloops = checkForSelfLoops();
    System.out.println("Self loops: " + sloops);
    // allloops.addAll(sloops); 

    Vector ploops = setUpPossibleLoops();
    boolean changed = true;
    while (changed)
    { changed = extendPossibleLoops(ploops,allloops); }

    System.out.println(allloops); 

    for (int i = 0; i < allloops.size(); i++)
    { PossibleLoop pl = (PossibleLoop) allloops.get(i);
      pl.calculateStates();
      boolean plok = pl.checkLoop();
      ok = ok && plok; 
    }

    for (int i = 0; i < sloops.size(); i++)
    { State ss = (State) sloops.get(i);
      loops.add(ss);
      Vector sstates = new Vector();
      sstates.add(ss);
      loopdefs.put(ss,sstates);
    }

    for (int i = 0; i < allloops.size(); i++)
    { PossibleLoop pl = (PossibleLoop) allloops.get(i);
      State lhead = pl.getLoophead();
      Vector sts = pl.getStates();
      if (loops.contains(lhead))
      { Vector v = (Vector) loopdefs.get(lhead);

      // if (v == null) { v = new Vector(); } 

        for (int j = 0; j < sts.size(); j++)
        { State ss = (State) sts.get(j);
          if (v.contains(ss)) { }
          else
          { v.add(ss); }
        }
        // loopdefs.put(lhead,v); 
      }
      else
      { loops.add(lhead); 
        loopdefs.put(lhead,sts);
      }
    }

    for (int p = 0; p < loops.size(); p++) 
    { State st = (State) loops.get(p); 
      if (st != null) 
      { st.setStateKind(State.LOOPSTATE); }  
      else 
      { ok = false; } 
    } 

    if (ok == false) 
    { System.out.println("No code can be produced for this state machine"); 
      return null; 
    } 
    
    Statement res = fcode(initial_state,null,loops,loopdefs,tp);
    if (res == null) 
    { System.out.println("No code can be produced for this state machine");
      return null; 
    } 

    if (modelElement instanceof Entity)
    { res.setEntity((Entity) modelElement); }  
    else 
    { BehaviouralFeature bf = (BehaviouralFeature) modelElement; 
      res.setEntity(bf.getEntity()); 
    } 
    res.displayJava(null); 

    return null;  
  } 

  private void buildReachability(State st, Vector seentrans, 
                                 java.util.Map reachability)
  { if (st == null) { return; } 

    Vector outt = st.outgoingTransitions();
    if (outt.size() == 0)
    { st.setStateKind(State.FINALSTATE);
      return;
    }
    for (int i = 0; i < outt.size(); i++)
    { Transition tr = (Transition) outt.get(i);
      if (seentrans.contains(tr)) { return; }
      else
      { extendReachability(st,tr.target,reachability);
        seentrans.add(tr);
        buildReachability(tr.target,seentrans,reachability);
      }
    }
  }

  private void extendReachability(State src, State trg, java.util.Map reachability)
  { if (trg == null) { return; } 
    if (src == null) { return; } 

    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.get(i);
      Vector sr = (Vector) reachability.get(st);
      if (sr == null)
      { sr = new Vector(); 
        reachability.put(st,sr); 
      } 

      if (st == src)
      { sr.add(trg); }
      else if (sr.contains(src))
      { sr.add(trg); }
    }
  }

  private Vector identifyLoops(java.util.Map reachability, java.util.Map loopdefs)
  { Vector res = new Vector();
    for (int i = 0; i < states.size(); i++)
    { State st = (State) states.get(i);
      Vector rs = (Vector) reachability.get(st);
      if (rs.contains(st))
      { res.add(st);
        st.setStateKind(State.LOOPSTATE);
        Vector stloop = new Vector();
        for (int j = 0; j < rs.size(); j++)
        { State os = (State) rs.get(j);
          Vector ors = (Vector) reachability.get(os);
          if (ors.contains(st)) 
          { if (stloop.contains(os)) { }
            else
            { stloop.add(os); }
          }
        }
        loopdefs.put(st,stloop);
      }
    }
    return res;
  }

  private boolean checkLoops(Vector loops, java.util.Map loopdefs)
  { boolean res = true;
    for (int i = 0; i < loops.size(); i++)
    { State st = (State) loops.get(i);
      Vector stloop = (Vector) loopdefs.get(st);
      for (int k = 0; k < stloop.size(); k++)
      { State st1 = (State) stloop.get(k);
        Vector outt = st1.outgoingTransitions();
        for (int j = 0; j < outt.size(); j++)
        { Transition tr = (Transition) outt.get(j);
          if (stloop.contains(tr.target))  { }  // ok
          else if (st1 == st || tr.target == st) { } // ok  
          else
          { res = false;
            System.out.println("Multiple exit loop!: " + st + " state: " + st1 + " in loop: " + stloop + " to outside loop: " + tr.target);
          }
        }
        Vector inct = st1.incomingTransitions();
        for (int j = 0; j < inct.size(); j++)
        { Transition tr = (Transition) inct.get(j);
          if (stloop.contains(tr.source))  { }  // ok
          else if (st1 == st || tr.source == st) { } // ok  
          else
          { res = false;
            System.out.println("Multiple-entry loop!: " + st + " state: " + st1 + " in: " + stloop + " from outside loop: " + tr.source);
          }
        }
      }
    }
    return res;
  }

  private static Expression makeLoopTest(State st,
      Vector outt, Vector loopstates, Vector inloop, 
      Vector rem)
  { Expression test = new BasicExpression("false");
    for (int i = 0; i < outt.size(); i++)
    { Transition tr = (Transition) outt.get(i);
      if (tr.target == st || loopstates.contains(tr.target))
      { Expression gd = tr.getGuard();
        test = (new BinaryExpression("or",test,gd)).simplify();
        inloop.add(tr);
      }
      else
      { rem.add(tr); }  // an exit from loop
    }
    return test;
  }

  public Statement fcode(State st, State contloop,
      Vector loops, java.util.Map loopdefs, Type tp)
  { Vector outt = st.outgoingTransitions();
    if (outt.size() == 0)
    { st.setStateKind(State.FINALSTATE); 
      if (tp == null)
      { return new ReturnStatement(null); }
      return new ReturnStatement(new BasicExpression("result"));
    }
    else if (loops.contains(st))
    { Vector stloop = (Vector) loopdefs.get(st);
      Vector rem = new Vector();
      Vector inloop = new Vector();
      Expression test =
        makeLoopTest(st,outt,stloop,inloop,rem);
      Statement body =
        ffcode(inloop,st,loops,loopdefs,tp);
      WhileStatement wl = new WhileStatement(test,body);
      Expression inv = st.getInvariant();
      wl.setInvariant(inv);
      Statement afterloop =
        ffcode(rem,contloop,loops,loopdefs,tp);
      SequenceStatement ss = new SequenceStatement();
      ss.addStatement(wl);
      ss.addStatement(afterloop);
      return ss;
    }
    else
    { return ffcode(outt,contloop,loops,loopdefs,tp); }
  }

  public Statement ffcode(Vector outt, State contloop,
      Vector loops, java.util.Map loopdefs, Type tp)
  { Vector conds = new Vector();
    Vector stats = new Vector();

    for (int i = 0; i < outt.size(); i++)
    { Transition tr = (Transition) outt.get(i);
      Expression gd = tr.getGuard();
      Statement stat = tr.transitionToCode();
      if (tr.target == contloop)   // end of loop
      { stats.add(stat); }
      else 
      { Statement stat1 = 
             fcode(tr.target,contloop,loops,
                       loopdefs,tp);
        SequenceStatement ss = new SequenceStatement();
        ss.addStatement(stat);
        ss.addStatement(stat1);
        stats.add(ss);
      }
      conds.add(gd);
    }

    if (stats.size() == 0)
    { return new IfStatement(); } 

    if (stats.size() == 1 && "true".equals("" + conds.get(0)))
    { return (Statement) stats.get(0); }
    else
    { return Statement.buildIf(conds,stats); }
  }

  public Vector checkForSelfLoops()
  { Vector res = new Vector();
    for (int i = 0; i < transitions.size(); i++)
    { Transition tr = (Transition) transitions.get(i);
      if (tr.target == tr.source)
      { if (res.contains(tr.source)) {}
        else 
        { res.add(tr.source); 
          tr.source.setStateKind(State.LOOPSTATE);
        }
      }
    }
    return res;
  }

  private Vector setUpPossibleLoops()
  { Vector ploops = new Vector();
    for (int i = 0; i < states.size(); i++)
    { State ss = (State) states.get(i);
      Vector outt = ss.outgoingTransitions();
      for (int j = 0; j < outt.size(); j++)
      { Transition tr = (Transition) outt.get(j);
        if (tr.source != tr.target)
        { ploops.add(new PossibleLoop(ss,tr)); }
      }
    }
    return ploops;
  }

  private boolean extendPossibleLoops(Vector ploops, Vector aloops)
  { boolean changed = false;
    
    for (int i = 0; i < ploops.size(); i++)
    { PossibleLoop pl = (PossibleLoop) ploops.get(i);
      State st = pl.endState();
      Vector outt = st.outgoingTransitions();
      for (int j = 0; j < outt.size(); j++)
      { Transition tr = (Transition) outt.get(j);
        boolean modified = pl.extend(tr);
        changed = changed || modified;
      }
    }

    Vector loops = new Vector();
    for (int i = 0; i < ploops.size(); i++)
    { PossibleLoop pl = (PossibleLoop) ploops.get(i);
      if (pl.isLoop())
      { loops.add(pl); }
    }
    ploops.removeAll(loops);
    aloops.addAll(loops); 
    System.out.println("Loops: " + loops);
    return changed;
  }



}

