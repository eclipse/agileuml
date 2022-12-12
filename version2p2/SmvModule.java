import java.util.Vector;
import java.io.*; 
import javax.swing.JFileChooser; 
import javax.swing.JFrame; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* Package: SMV */

public class SmvModule extends Named
{ Vector attributes = new Vector();
  Vector parameters = new Vector(); // of String
  // Vector defs = new Vector();
  Vector defines = new Vector(); // TransitionDefinition
  Vector updates = new Vector(); // SmvCaseStatement
  Vector initialisations = new Vector(); // of String or SmvCaseStatement
  Vector assumptions = new Vector(); 
  Vector specs = new Vector();  // of Invariant
  String eventVar = "C.event";
  String stateVar;

  public SmvModule(String nme)
  { super(nme);
    // stateVar = nme; 
    stateVar = "C." + nme + "id"; 
  }

  public Object clone()
  { return new SmvModule(label); }   // more detail later

  public SmvModule(Statemachine sm)
  { super(sm.label); 
    if (sm.cType == DCFDArea.SENSOR)
    { stateVar = sm.label;
      eventVar = "C.con_event";
      createFromSensor(sm);
    }
    else if (sm.cType == DCFDArea.CONTROLLER)
    { stateVar = "global_state";
      eventVar = "con_event";
      createFromController(sm); 
    }
  }

  public SmvModule(Statemachine sm, Vector invs)
  { super("main"); 
    Vector sts = sm.states;
    Vector trans = sm.transitions;
    eventVar = sm.label + "_event";
    stateVar = sm.label;
    String stateType = buildStateType(sm,sts);
    String eventType = buildEventType(sm.events);
    addAttribute(stateVar, stateType);
    addAttribute(eventVar, eventType); 
    State ini = sm.initial_state;
    if (ini != null) 
    { addInitUpdate(sm.label, ini.label); } 
    
    specs = invs;  /* Properties to test */

    SmvCaseStatement cs = 
      new SmvCaseStatement();
    for (int i = 0; i < trans.size(); i++)
    { Transition tr = 
        (Transition) trans.get(i); 
      Event ev = tr.event;
      State src = tr.source;
      State targ = tr.target;
      if (ev != null && src != null &&
          targ != null && !src.isExcluded() &&
          !targ.isExcluded())
      { String tlabel = sm.label + "T" + i;
        addDefines(tlabel, src.label, ev.label, targ);
        cs.addCase(tlabel, targ.label);     
      }
    }
    cs.addCase("TRUE", sm.label);
    addNextUpdate(sm.label,cs);
  }


  public void addParameter(String p)
  { parameters.add(p); }

  private void addParameters(Vector comps)
  { for (int i = 0; i < comps.size(); i++)
    { String comp = (String) comps.get(i);
      int found = comp.indexOf("."); 
      if (found > 0) // It is an  instance.property  name
      { comp = comp.substring(0,found); } 

      if (parameters.contains("M" + comp)) { }
      else
      { parameters.add("M" + comp); }
    }
  }

  public void addAttribute(String att,
                           String type)
  { attributes.add(new Maplet(att,type)); }

  private void addDefines(String nme,
                          String src, String ev, 
                          State trg)
  { TransitionDefinition td = 
      new TransitionDefinition(nme,src,ev,trg);
    defines.add(td); 
  }

  public void addInitUpdate(String att,
                            String stat)
  { initialisations.add(new Maplet(att,stat)); }

  public void addInitUpdate(String att,
                            SmvCaseStatement stat)
  { initialisations.add(new Maplet(att,stat)); }

  public void addNextUpdate(String att,                        
                            SmvCaseStatement stat)
  { updates.add(new Maplet(att,stat)); }

  public void addSpec(Invariant inv) 
  { specs.add(inv); }  /* All temporal */

  public void addAssumption(Invariant inv) 
  { assumptions.add(inv); }  /* All environmental */

  private void addAttributeCode(String att,
                 Statemachine ac, 
                 int index, int maxval,
                 Statemachine cont,
                 SmvModule contsmv,
                 Vector cnames)
  { String attType = "0.." + maxval;
    addAttribute(att, attType);
    String init = buildAttInitial(att,cont);
    addInitUpdate(att,init);
    SmvCaseStatement cs = new SmvCaseStatement();
    if (index == 0) // Not a multiple component
    { variableUpdates(att, ac, contsmv.defines,
                      cnames, cs);
    } 
    else 
    { variableUpdates(label + "." + att, ac, contsmv.defines,
                      cnames, cs);
    } 
    cs.addCase("TRUE",att);
    addNextUpdate(att,cs);
  }

  
  private String buildAttInitial(String att,
                   Statemachine cont)
  { State cinit = cont.initial_state;
    Maplet cprop = cinit.getProperties();
    if (cprop != null)
    { Vector subs = (Vector) cprop.source;
      Expression val =
        (Expression) VectorUtil.mapletValue(att,subs);
      if (val != null)
      { return val.toString(); }
    }
    return "0";
  } // or "FALSE"


  public void display()
  { System.out.print("MODULE " + label);
    if (parameters.size() == 0)
    { System.out.println(); }
    else
    { System.out.print("("); 
      System.out.print(parameterList());
      System.out.println(")"); 
    }

    if (attributes.size() > 0)
    { System.out.println("VAR");
      for (int i = 0; i < attributes.size(); i++)
      { Maplet mm = (Maplet) attributes.get(i);
        System.out.println("  " +
          (String) mm.source + " : " +
          (String) mm.dest + "; ");
      }
    }

    if (defines.size() > 0)
    { System.out.println("DEFINE"); 
      for (int i = 0; i < defines.size(); i++)
      { TransitionDefinition td = 
          (TransitionDefinition) defines.get(i);
        td.display(eventVar,stateVar);
      }
    }
  
    if (initialisations.size() > 0 ||
        updates.size() > 0)
    { System.out.println("ASSIGN"); 
      for (int i = 0; 
           i < initialisations.size(); i++)
      { Maplet mm3 =
          (Maplet) initialisations.get(i);
        System.out.println("  init(" + 
          mm3.source + ") := " +
          mm3.dest + ";\n");   // no need for ; if an SmvCaseStatement
      }

      for (int i = 0; 
           i < updates.size(); i++)
      { Maplet mm4 =
          (Maplet) updates.get(i);
        System.out.println("  next(" + 
          (String) mm4.source + ") := ");
        ((SmvCaseStatement) mm4.dest).display();
        System.out.println("\n"); 
      }
    }

    if (specs.size() > 0)
    { for (int g = 0; g < specs.size(); g++)
      { System.out.println("SPEC"); 
        Invariant inv = (Invariant) specs.get(g);
        inv.smvDisplay(assumptions); 
      }
    }
  }

  public void display(PrintWriter out)
  { out.print("MODULE " + label);
    if (parameters.size() == 0)
    { out.println(); }
    else
    { out.print("("); 
      out.print(parameterList());
      out.println(")"); 
    }

    if (attributes.size() > 0)
    { out.println("VAR");
      for (int i = 0; i < attributes.size(); i++)
      { Maplet mm = (Maplet) attributes.get(i);
        out.println("  " +
          (String) mm.source + " : " +
          (String) mm.dest + "; ");
      }
    }

    if (defines.size() > 0)
    { out.println("DEFINE"); 
      for (int i = 0; i < defines.size(); i++)
      { TransitionDefinition td =
          (TransitionDefinition) defines.get(i);
        td.display(eventVar,stateVar,out);
      }
    }

    if (initialisations.size() > 0 ||
        updates.size() > 0)
    { out.println("ASSIGN"); 
      for (int i = 0; 
           i < initialisations.size(); i++)
      { Maplet mm3 =
          (Maplet) initialisations.get(i);
        out.println("  init(" + 
          (String) mm3.source + ") := " +
          mm3.dest + ";\n"); 
      }

      for (int i = 0; 
           i < updates.size(); i++)
      { Maplet mm4 =
          (Maplet) updates.get(i);
        out.println("  next(" + 
          (String) mm4.source + ") := ");
        ((SmvCaseStatement) mm4.dest).display(out);
        out.println("\n"); 
      }
    }

    if (specs.size() > 0)
    { for (int g = 0; g < specs.size(); g++)
      { out.println("SPEC"); 
        Invariant inv = (Invariant) specs.get(g);
        inv.smvDisplay(assumptions, out); 
      }
    }
  }


  private String parameterList()
  { String res = "";
    int n = parameters.size();

    for (int i = 0; i < n; i++)
    { String param = (String) parameters.get(i);
      res = res + param;
      if (i < n-1) 
      { res = res + ", "; }
    }
    return res;
  }

  public void addOpInv(OperationalInvariant inv)
  { String ev = inv.eventName;
    Expression cond = inv.antecedent();
    Expression effect = inv.succedent();
    
    if (cond.equalsString("false")) 
    { return; }

    TransitionDefinition td = 
      new TransitionDefinition(ev,cond,effect);
    defines.add(td); 
  }
  
  private static String buildStateType(Statemachine sm,
                                       Vector sts)
  { if (sm.myTemplate != null && 
        sm.myTemplate instanceof AttributeBuilder) 
    { return "0.." + (sts.size()-1); }
 
    String res = "{ ";
    for (int i = 0; i < sts.size(); i++)
    { State st = (State) sts.get(i);
      if (st.isExcluded()) { }
      else 
      { res = res + st.label;
        if (i < sts.size() - 1)
        { res = res + ", "; }
      }
    }
    return res + " }";
  }

  private String buildEventType(Vector evs)
  { String res = "{ ";
    for (int i = 0; i < evs.size(); i++)
    { Event st = (Event) evs.get(i);
      res = res + st.label;
      if (i < evs.size() - 1)
      { res = res + ", "; }
    }
    return res + " }";
  }

  private void buildDefsUps(Vector trans)
  { SmvCaseStatement cs = 
      new SmvCaseStatement();
    for (int i = 0; i < trans.size(); i++)
    { Transition tr = 
        (Transition) trans.get(i);
      Event ev = tr.event; /* != null */
      State src = tr.source;
      State targ = tr.target;
      if (ev != null && src != null &&
          targ != null && !src.isExcluded() &&
          !targ.isExcluded())
      { String tlabel = label + "T" + i; 
        addDefines(tlabel, src.label, ev.label,
                   targ);
        cs.addCase(tlabel,targ.label);
      }
    }
    cs.addCase("TRUE",label);
    addNextUpdate(label,cs);
  }

  private String buildActInitial(Statemachine cont, 
                                 Statemachine act)
  { State cinit = cont.initial_state;
    Maplet cprop = cinit.getProperties();
    if (cprop != null)
    { Vector subs = (Vector) cprop.source;
      Expression val = 
        (Expression) VectorUtil.mapletValue(label,subs);
      if (val != null)
      { return val.toString(); }
    } 
    return act.initial_state.label; 
  }

  private SmvCaseStatement buildActPhaseInitial(Statemachine cont,
                                                Statemachine act)
  { SmvCaseStatement cs =
      new SmvCaseStatement();
    Vector states = cont.getStates(); 
    for (int i = 0; i < states.size(); i++) 
    { State st = (State) states.get(i);
      Maplet cprop = st.getProperties();
      if (cprop != null)
      { Vector subs = (Vector) cprop.source;
        Expression val =
          (Expression) VectorUtil.mapletValue(label,subs);
        if (val != null)
        { Vector sens = st.getSensorSettings(); 
          Expression ante = VectorUtil.mapletsToSmvExpression(sens); 
          cs.addCase(ante.toString(),val.toString());
        }
      } 
    }
    return cs;  // The initialisation statement of label. 
  }

  private void buildContDefsUps(Vector trans)
  { SmvCaseStatement cs = 
      new SmvCaseStatement();
    for (int i = 0; i < trans.size(); i++)
    { Transition tr = 
        (Transition) trans.get(i);
      Event ev = tr.event; /* != null */
      State src = tr.source;
      State targ = tr.target;
      if (ev != null && src != null &&
          targ != null && !src.isExcluded() &&
          !targ.isExcluded())
      { String tlabel = "CT" + i; 
        addDefines(tlabel, src.label, ev.label, targ); 
        cs.addCase(tlabel,targ.label);
      }
    }
    cs.addCase("TRUE","global_state");
    addNextUpdate("global_state",cs);
  }

  private void createFromSensor(Statemachine sm)
  { Vector sts = sm.states;
    Vector trans = sm.transitions;
    addParameter("C"); 
    String stateType = buildStateType(sm,sts);
    addAttribute(sm.label, stateType);
    State ini = sm.initial_state; 
    addInitUpdate(sm.label, ini.label);
    buildDefsUps(trans);
  }

  private void createFromController(Statemachine sm)
  { Vector sts = sm.states;
    Vector trans = sm.transitions;
    
    String stateType = buildStateType(sm,sts);
    String eventType = buildEventType(sm.events);
    addAttribute("global_state", stateType);
    addAttribute("con_event", eventType);
    State ini = sm.initial_state; 
    addInitUpdate("global_state", ini.label);
    buildContDefsUps(trans);
  }

  public static SmvModule createControllerModule(Vector entities)
  { SmvModule res = new SmvModule("Controller"); 
    Vector allevents = new Vector(); 
    String eventtype = "{ "; 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i); 
      int ecard = ent.getSmvCardinality(); 
      if (ecard < 0) { } // not valid for Smv
      else 
      { String ename = ent.getName(); 
        res.addAttribute(ename + "id","1.." + ecard); 
        allevents.addAll(ent.smvEventList()); 
      } 
    }
    for (int k = 0; k < allevents.size(); k++) 
    { String ev = (String) allevents.get(k); 
      eventtype = eventtype + ev + ", "; 
      if (k > 0 && k % 10 == 0)
      { eventtype = eventtype + "\n       "; } 
    } 
    res.addAttribute("event",eventtype + "none }"); 
    return res; 
  } // also main module

  public static SmvModule createMainModule(Vector entities)
  { SmvModule res = new SmvModule("main"); 
    // MEi : E(C,i) for each entity
    // C : Controller
    res.addAttribute("C","Controller"); 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i); 
      int ecard = ent.getSmvCardinality(); 
      if (ecard < 0) { } // not valid for Smv
      else 
      { String ename = ent.getName(); 
        for (int j = 1; j <= ecard; j++) 
        { res.addAttribute("M" + ename + j, ename + "(C," + j + ")"); } 
      } // for target entities also add the source they depend on as a parameter
    } 
    return res; 
  } 

   /* need cnames: */
  public void createFromActuator(Statemachine sm,
                            Statemachine cont,
                            SmvModule contsmv,
                            Vector cnames)
  { Vector trans = cont.transitions;
    Vector sts = sm.states;
    stateVar = sm.label;
    String att = sm.getAttribute(); 
   
    addParameter("C"); 
    String stateType = buildStateType(sm,sts);
    addAttribute(sm.label, stateType);
    
    String init = buildActInitial(cont,sm);
    addInitUpdate(sm.label, init);

    SmvCaseStatement cs = 
      new SmvCaseStatement();
    
    actuatorUpdates(sm, contsmv.defines, cnames, cs); 
    cs.addCase("TRUE",sm.label);
    addNextUpdate(sm.label,cs);
    if (att != null)
    { addAttributeCode(att,sm,0,sm.getMaxval(),
                       cont,contsmv,cnames); }
  }

  private void actuatorUpdates(Statemachine ac,
                               Vector cdefines, 
                               Vector cnames,
                               SmvCaseStatement cs)
  { variableUpdates(label,ac,cdefines,cnames,cs); }

  private void variableUpdates(String variable, Statemachine sm, 
                    Vector contdefines, 
                    Vector cnames, SmvCaseStatement cs)
  { Vector cnames1 = (Vector) cnames.clone();
    Vector removals = sm.myVariables(label); 
    System.out.println("Building actuator module for " + label + 
                       " variable " + variable); 
    System.out.println("All variables = " + cnames1); 
    cnames1.removeAll(removals); // all vars of the sm instance
    System.out.println("Other variables = " + cnames1); 

    for (int i = 0; i < contdefines.size(); i++)
    { TransitionDefinition td = 
        (TransitionDefinition) contdefines.get(i);
      State targ = td.target;
      if (targ != null && targ.getProperties() != null)
      { Maplet prop = targ.getProperties();
        Vector subs = (Vector) prop.source;
        Expression eval = 
          (Expression) VectorUtil.mapletValue(variable, subs);
    
        if (eval != null)
        { String val = eval.toString();
          cs.addCase("C." + td.label, val); 
        }
        
        if (prop.dest != null)
        { Vector invs = (Vector) prop.dest;
          for (int j = 0; j < invs.size(); j++)
          { Invariant inv = (Invariant) invs.get(j);
            Expression succ = inv.succedent();
            Vector vars = succ.variablesUsedIn(cnames);
            if (vars.contains(variable))
            { Expression val2 = succ.getSettingFor(variable);
              Expression ante = inv.antecedent();
              Vector others =
                ante.variablesUsedIn(cnames);
              others.remove(variable);
              addParameters(others); // Mv for each.
              Expression anteSmv =
                  ante.toSmv(cnames1);
              String antestr;
              if (anteSmv.equalsString("true"))
              { antestr = ""; }
              else 
              { antestr = " & " + anteSmv; }
              cs.addCase("C." + td.label +
                         antestr,
                         val2.toString());
            }
          }
        } 
      }

      if (td.condition != null && td.effect != null)
      { Expression eff = td.effect;
        Vector cvars = eff.variablesUsedIn(cnames);
        if (cvars.contains(variable))
        { Expression val3 = eff.getSettingFor(variable);
          Expression cond = td.condition;
          Vector others2 = cond.variablesUsedIn(cnames);
          others2.remove(variable);  // I guess. 
          addParameters(others2);
          Expression condsmv = cond.toSmv(cnames1);
          String condstr;
          if (condsmv.equalsString("true"))
          { condstr = ""; }
          else 
          { condstr = " & " + condsmv.toString(); }
          // cs.addCase("C." + td.label + condstr,
          //            val3.toString());
          cs.addCase("C.con_event = " + td.eventName +
                     condstr, val3.toString()); 
        }
      }
    }  
  }

  public static Vector 
    createFromMultipleActuator(Statemachine ac,
                               Statemachine cont, SmvModule contsmv,
                               Vector cnames)  // all variables?
  { Vector res = new Vector();
    String aclab = ac.label;
    int m = ac.getMultiplicity();
    Vector trans = cont.transitions;
    Vector sts = ac.states;
    String stateType = buildStateType(ac,sts);
    

    for (int i = 1; i <= m; i++)
    { String label = aclab + i + "." + aclab;
      SmvModule acmod = new SmvModule(aclab + i);
      // has to change to avoid . ?
      acmod.addParameter("C");
      acmod.addAttribute(aclab,stateType);
      String init = acmod.buildActInitial(cont,ac);
      acmod.addInitUpdate(aclab, init);
      SmvCaseStatement cs = new SmvCaseStatement();
      acmod.variableUpdates(label, ac, contsmv.defines, cnames, cs);
      cs.addCase("TRUE", aclab);
      acmod.addNextUpdate(aclab, cs);
      if (ac.getAttribute() != null)
      { String att = ac.getAttribute(); 
        int maxv = ac.getMaxval(); 
        acmod.addAttributeCode(att,ac,i,maxv,
                               cont,contsmv,cnames); 
      }   
      res.add(acmod);
    }
    return res;
  }

  public static SmvModule createMainModule(SmvModule contsmv,
                            Vector sensormods,
                            Vector actuatormods)
  { SmvModule res = new SmvModule("main");
    res.addAttribute("C", contsmv.label);
    for (int i = 0; i < sensormods.size(); i++)
    { SmvModule sm = (SmvModule) sensormods.get(i);
      res.addAttribute("M" + sm.label,
                       sm.label + "(C)"); 
    }
    for (int i = 0; i < actuatormods.size(); i++)
    { SmvModule am = (SmvModule) actuatormods.get(i);
      res.addAttribute("M" + am.label,
                       am.label + "(" + 
                       am.parameterList() + ")"); 
    }
    return res;
  }

  public static void saveSmvToFile(Vector smvmods, JFrame frame)
  { if (smvmods == null) { return; }

    File startingpoint = new File("output");
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(startingpoint);
    fc.setDialogTitle("Save SMV Modules");
    int returnVal = fc.showSaveDialog(frame);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    { File file = fc.getSelectedFile();
      try
      { PrintWriter out = new PrintWriter(
                            new BufferedWriter(
                              new FileWriter(file)));
        for (int i = 0; i < smvmods.size(); i++)
        { SmvModule mod = (SmvModule) smvmods.get(i);
          mod.display(out);
          out.println();
        }
        out.close();
      }
      catch(IOException e) { }
    }
  }

  public static void main(String[] args)
  { SmvModule mod = new SmvModule("test");
    mod.eventVar = "sw_event";
    BasicState onS = new BasicState("On"); 
    BasicState offS = new BasicState("Off");
    mod.addAttribute("sw", "{ Off, On }");
    mod.addInitUpdate("sw", "Off");
    mod.addDefines("T1", "Off", "swon", onS);
    mod.addDefines("T2", "On", "swoff", offS);
    
    mod.display();
  }

  public void buildModule(int card, Vector atts, Vector asts,
                          Entity superclass)
  { // addAttribute("self","1.." + card); 
    addParameter("C"); // for controller
    addParameter("id"); 
    addAttribute("alive","boolean"); 
    addAliveInit(); 
    addAliveUpdate(); 
    if (superclass != null) 
    { int csup = superclass.getSmvCardinality(); 
      if (csup > 0)
      { addAttribute("super","1.." + csup); 
        addSuperUpdates(superclass.getName()); 
      } 
    } 

    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i); 
      String nme = att.getName(); 
      String smvtype = null; 
      Type typ = att.getType(); 
      smvtype = typ.getSmvType(); 
      if (smvtype == null) 
      { System.err.println("Can only convert boolean and enumerated types to SMV"); } 
      else
      { addAttribute(nme,smvtype); 
        String ini = att.getInitialValueSMV(); 
        if (ini != null && !ini.equals("")) 
        { addInitUpdate(nme,ini); }
        if (att.getKind() == ModelElement.SEN)
        { addSetUpdates(nme,typ); }
        // if (att.getKind() == ModelElement.ACT)
        // { addDefaultSetUpdates(nme,typ); }  next(nme) := nme;
      } 
    }   
    for (int j = 0; j < asts.size(); j++) 
    { Association ast = (Association) asts.get(j); 
      Entity ent2 = ast.getEntity2(); 
      int card2 = ast.getCard2();  // m
      String role2 = ast.getRole2(); 
      // role2 : 0..q or role2 : array 1..q of 0..1
      String cardinality = ent2.getCardinality();  // q 
      if (cardinality == null || cardinality.equals("*"))
      { System.err.println("Cannot convert unbounded cardinality class to SMV"); 
        continue; 
      } 
      int e2card = 0; 
      try { e2card = Integer.parseInt(cardinality); }
      catch (Exception e) 
      { System.err.println("Cannot convert unbounded cardinality class to SMV"); 
        continue; 
      }
      String e2name = ent2.getName(); 

      if (card2 == ModelElement.ONE)
      { addAttribute(role2,"1.." + e2card);
        addInitUpdate(role2,"1");            // arbitrary value 
        addSetUpdates(role2,e2card,e2name); 
      } 
      else if (card2 == ModelElement.ZEROONE) 
      { addAttribute(role2,"0.." + e2card); 
        addInitUpdate(role2,"0"); 
        addZOAddRemove(role2,e2card,e2name); 
      } 
      else 
      { addAttribute(role2,"array 1.." + ModelElement.maxCard(card2,e2card)
                           + " of 0..1");
        for (int k = 1; k <= e2card; k++) 
        { addInitUpdate(role2 + "[" + k + "]","0"); 
          addAddRemUpdate(role2,k,e2name); 
        } 
      }  
      // init for array is all set to 0, by default
    } 
  } 

  public void buildSourceModule(int card, Vector atts, Vector asts,
                          Entity superclass)
  { // addAttribute("self","1.." + card); 
    addParameter("C"); // for controller
    addParameter("id"); 
    if (superclass != null) 
    { int csup = superclass.getSmvCardinality(); 
      if (csup > 0)
      { addAttribute("super","1.." + csup); 
      } 
    } 

    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i); 
      String nme = att.getName(); 
      String smvtype = null; 
      Type typ = att.getType(); 
      smvtype = typ.getSmvType(); 
      if (smvtype == null) 
      { System.err.println("Can only convert boolean and enumerated types to SMV"); } 
      else
      { addAttribute(nme,smvtype); 
      } 
    }   
    for (int j = 0; j < asts.size(); j++) 
    { Association ast = (Association) asts.get(j); 
      Entity ent2 = ast.getEntity2(); 
      int card2 = ast.getCard2();  // m
      String role2 = ast.getRole2(); 
      // role2 : 0..q or role2 : array 1..q of 0..1
      String cardinality = ent2.getCardinality();  // q 
      if (cardinality == null || cardinality.equals("*"))
      { System.err.println("Cannot convert unbounded cardinality class to SMV"); 
        continue; 
      } 
      int e2card = 0; 
      try { e2card = Integer.parseInt(cardinality); }
      catch (Exception e) 
      { System.err.println("Cannot convert unbounded cardinality class to SMV"); 
        continue; 
      }
      String e2name = ent2.getName(); 

      if (card2 == ModelElement.ONE)
      { addAttribute(role2,"1.." + e2card);
      } 
      else if (card2 == ModelElement.ZEROONE) 
      { addAttribute(role2,"0.." + e2card); 
      } 
      else 
      { addAttribute(role2,"array 1.." + ModelElement.maxCard(card2,e2card)
                           + " of 0..1");
      }  
      // init for array is all set to 0, by default
    } 
  } 


  private void addSetUpdates(String att, Type typ)
  { Vector vals = typ.getValues(); 
    String ename = getName(); 
    String eId = "C." + ename + "id"; 

    BasicExpression aliveexp = new BasicExpression("alive"); 
    BinaryExpression isalive = new BinaryExpression("=",aliveexp,
                                     new BasicExpression("TRUE")); 
    SmvCaseStatement cs = new SmvCaseStatement(); 

    // for each val : vals define transition
    // Tattval := C.event = attval & alive = 1 & C.Eid = self
    // and update  next(att) := ... T : val; ...
    if ("boolean".equals(typ + ""))
    { vals = new Vector(); 
      vals.add("FALSE"); // false
      vals.add("TRUE"); // true
    } 
    else if ("String".equals(typ + ""))
    { vals = new Vector(); 
      vals.add("empty"); vals.add("string0"); vals.add("string1"); 
      vals.add("string2"); 
    } 
    else if (vals == null) // numeric type
    { vals = new Vector(); 
      vals.add("0"); 
      vals.add("1");
      vals.add("2"); 
      vals.add("3");   
    }

    for (int i = 0; i < vals.size(); i++) 
    { String val = (String) vals.get(i); 
      String attval = att + val; 
      String tname = "T" + attval; 
      TransitionDefinition tran = new TransitionDefinition(tname,"id",attval,null);
      tran.setCondition(isalive); 
      defines.add(tran);
      cs.addCase(tname,val);  
    }
    cs.addCase("TRUE",att); 
    addNextUpdate(att,cs); 
  }

  private void addAliveInit()
  { initialisations.add(new Maplet("alive","FALSE")); } 

  private void addAliveUpdate()
  { SmvCaseStatement cs = new SmvCaseStatement(); 
    String ename = getName(); 
    String cevent = "create" + ename;
    String kevent = "kill" + ename;  
    TransitionDefinition t1 = 
      new TransitionDefinition("T" + cevent,"id",cevent,null);
    TransitionDefinition t2 = 
      new TransitionDefinition("T" + kevent,"id",kevent,null); 
    defines.add(t1);
    defines.add(t2);
    cs.addCase("T" + cevent,"TRUE");  
    cs.addCase("T" + kevent,"FALSE");  
    cs.addCase("TRUE","alive"); 
    addNextUpdate("alive",cs); 
  }

  private void addSuperUpdates(String sname)
  { SmvCaseStatement cs = new SmvCaseStatement(); 
    String ename = getName(); 
    String cevent = "create" + ename;
    // String kevent = "kill" + ename;  
    // TransitionDefinition t1 = 
    //  new TransitionDefinition("T" + cevent,"self",cevent,null);
    // TransitionDefinition t2 = 
    //  new TransitionDefinition("T" + kevent,"self",kevent,null); 
    // defines.add(t1);
    // defines.add(t2);
    cs.addCase("T" + cevent,"C." + sname + "id");  
    // cs.addCase("T" + kevent,"0");  
    cs.addCase("TRUE","super"); 
    addNextUpdate("super",cs); 
  }


  private void addSetUpdates(String role2, int card, String e2name)
  { String ename = getName(); 
    BasicExpression aliveexp = new BasicExpression("alive"); 
    BinaryExpression isalive = new BinaryExpression("=",aliveexp,
                                     new BasicExpression("TRUE")); 
    SmvCaseStatement cs = new SmvCaseStatement(); 

    // for each val : vals define transition
    // Tsetrole2 := C.event = setrole2 & alive = 1 & C.Eid = self
    // and update  next(att) := ... Tsetrole2 : C.E2id; ...
    String eId = "C." + ename + "id"; 
    String e2Id = "C." + e2name + "id"; 

    String tname = "Tset" + role2; 
    TransitionDefinition tran = 
      new TransitionDefinition(tname,"id","set" + role2,null);
    tran.setCondition(isalive); 
    defines.add(tran);
    cs.addCase(tname,e2Id);  
    cs.addCase("TRUE",role2); 
    addNextUpdate(role2,cs); 
  }

  private void addAddRemUpdate(String role2, int index, String e2name)
  { String ename = getName(); 
    BasicExpression aliveexp = new BasicExpression("alive"); 
    BinaryExpression isalive = new BinaryExpression("=",aliveexp,
                                     new BasicExpression("TRUE")); 
    
    SmvCaseStatement cs = new SmvCaseStatement(); 

    // for each role2[index] define transition
    // Taddrole2index := 
    //     C.event = addrole2 & alive = 1 & C.Eid = self & C.E2id = index
    // Tremrole2index := 
    //     C.event = remrole2 & alive = 1 & C.Eid = self & C.E2id = index
    // and update  next(role2[index]) := ... Taddrole2index : 1;
    //                                       Tremrole2index : 0; ...
    String eId = "C." + ename + "id"; 
    String e2Id = "C." + e2name + "id"; 
    BasicExpression e2id = new BasicExpression(e2Id); 
    BinaryExpression isindex = 
      new BinaryExpression("=",e2id,new BasicExpression("" + index));
    BinaryExpression cond = new BinaryExpression("&",isalive,isindex); 

    String taddname = "Tadd" + role2 + index;
    String tremname = "Trem" + role2 + index;  
    TransitionDefinition addtran = 
      new TransitionDefinition(taddname,"id","add" + role2,null);
    TransitionDefinition remtran = 
      new TransitionDefinition(tremname,"id","rem" + role2,null);
    addtran.setCondition(cond); 
    remtran.setCondition(cond); 
    defines.add(addtran);
    defines.add(remtran);
    cs.addCase(taddname,"1");
    cs.addCase(tremname,"0");   
    cs.addCase("TRUE",role2 + "[" + index + "]"); 
    addNextUpdate(role2 + "[" + index + "]",cs); 
  }

  private void addZOAddRemove(String role2, int e2card, String e2name)
  { String ename = getName(); 
    BasicExpression aliveexp = new BasicExpression("alive"); 
    BinaryExpression isalive = new BinaryExpression("=",aliveexp,
                                     new BasicExpression("TRUE")); 
    
    SmvCaseStatement cs = new SmvCaseStatement(); 

    // define transitions
    // Taddrole2 := 
    //     C.event = addrole2 & alive = 1 & C.Eid = self 
    // Tremrole2 := 
    //     C.event = remrole2 & alive = 1 & C.Eid = self & C.E2id = role2
    // and update  next(role2) := ... Taddrole2 : C.E2id;
    //                                Tremrole2 : 0; ...
    String eId = "C." + ename + "id"; 
    String e2Id = "C." + e2name + "id"; 
    BasicExpression e2id = new BasicExpression(e2Id); 
    BinaryExpression isindex = 
      new BinaryExpression("=",e2id,new BasicExpression(role2));
    BinaryExpression cond = new BinaryExpression("&",isalive,isindex); 

    String taddname = "Tadd" + role2;
    String tremname = "Trem" + role2;  
    TransitionDefinition addtran = 
      new TransitionDefinition(taddname,"id","add" + role2,null);
    TransitionDefinition remtran = 
      new TransitionDefinition(tremname,"id","rem" + role2,null);
    addtran.setCondition(isalive); 
    remtran.setCondition(cond); 
    defines.add(addtran);
    defines.add(remtran);
    cs.addCase(taddname,e2Id);
    cs.addCase(tremname,"0");   
    cs.addCase("TRUE",role2); 
    addNextUpdate(role2,cs); 
  }


  // Controller has form
  // MODULE Controller 
  // VAR ME1 : E(1); ...; MEn : E(n); ...; Eid : 1..n; ...; event : { ... } 

} 


class SmvCaseStatement
{ Vector cases = new Vector();

  public void addCase(String left, String right)
  { cases.add(new Maplet(left, right)); }

  public String toString()
  { String res = "    case"; 
    for (int i = 0; i < cases.size(); i++)
    { Maplet mm = (Maplet) cases.get(i);
      res = res + "      " + (String) mm.source +
                  " : " + (String) mm.dest + ";\n"; 
    }
    res = res + "    esac;\n"; 
    return res; 
  }

  public void display()
  { System.out.println("    case");
    for (int i = 0; i < cases.size(); i++)
    { Maplet mm = (Maplet) cases.get(i);
      System.out.println("      " + (String) mm.source +
                   " : " + (String) mm.dest + ";");
    }
    System.out.println("    esac;"); 
  }

  public void display(PrintWriter out)
  { out.println("    case");
    for (int i = 0; i < cases.size(); i++)
    { Maplet mm = (Maplet) cases.get(i);
      out.println("      " + (String) mm.source +
                   " : " + (String) mm.dest + ";");
    }
    out.println("    esac;"); 
  }

}


















