import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class DCFDSpec extends Named
{ Vector sensors = new Vector();  // Statemachine
  Vector baseSensors = new Vector(); 
  Vector actuators = new Vector();  // Statemachine
  Vector baseActuators = new Vector(); 
  Vector vgroup = new Vector();  // corresponds to componentNames
  Vector invariants = new Vector(); // Invariant
  /* Maybe dependencies, transdeps? */
  Vector visuals = new Vector();  // VisualData
  String extending = null;  /* Ancestor, if any */ 
  String prefix = "";  /* Prefix for B operations */

  VarAnalInfo varanal; 
  //  Vector locals = new Vector();  
  /* Set by clump analysis */ 
  // Vector foriegn = new Vector(); 

  static final int FACTFSEN = 0; 
  static final int FACT = 1; 
  static final int ALLACT = 2; 
  static final int ALLACTFSEN = 3; 
  static final int UNKNOWN = 4; 

  int modality = 4; 

  DCFDSpec(String name, Vector vg)
  { super(name); 
    vgroup = vg; 
  }

  public Object clone() 
  { Vector newvg = (Vector) vgroup.clone(); 
    return new DCFDSpec(label,newvg); 
  } 

  public void setSensors(Vector s)
  { sensors = s; 
    baseSensors = s; 
  }

  public Vector getSensors() 
  { return sensors; } 

  public void setPrefix(String s) 
  { prefix = s; } 

  public void setActuators(Vector a)
  { actuators = a; 
    baseActuators = a; 
  }

  public Vector getActuators()
  { return actuators; } 

  public void setExtending(String ext) 
  { extending = ext; } 

  public void setInvariants(Vector invs)
  { invariants = invs; }

  public void setVisuals(Vector vis)
  { visuals = vis; } 

  public void setMode(int mode) 
  { modality = mode; } 

  public String toString() 
  { if (modality == FACTFSEN) 
    { return vgroup.toString() + " (FACTFSEN)"; } 
    if (modality == FACT)
    { return vgroup.toString() + " (FACT)"; }
    if (modality == ALLACT)
    { return vgroup.toString() + " (ALLACT)"; } 
    if (modality == ALLACTFSEN) 
    { return vgroup.toString() + " (ALLACTFSEN)"; } 
    else 
    { return vgroup.toString() + " (UNKNOWN)"; } 
  } 

  public void extractSensors(Vector sens, Vector vis)
  { for (int i = 0; i < sens.size(); i++)
    { Statemachine sm = (Statemachine) sens.elementAt(i);
      for (int j = 0; j < invariants.size(); j++)
      { Invariant inv = (Invariant) invariants.elementAt(j);
        if (inv.hasVariable(sm.label)) // hasComponent
        { sensors.add(sm);
          vgroup.add(sm.label);
          VisualData vd = (VisualData) VectorUtil.lookup(sm.label,vis);
          if (vd != null)  
          { visuals.add(vd); }
          break;   /* go to next sensor */ 
        }
      }
    }
  }

  public void extractActuators(Vector acts, Vector vis)
  { for (int i = 0; i < acts.size(); i++)
    { Statemachine sm = (Statemachine) acts.elementAt(i);
      for (int j = 0; j < invariants.size(); j++)
      { Invariant inv = (Invariant) invariants.elementAt(j);
        if (inv.hasVariable(sm.label)) // hasComponent
        { actuators.add(sm);
          vgroup.add(sm.label);
          VisualData vd = (VisualData) VectorUtil.lookup(sm.label,vis);
          if (vd != null)  
          { visuals.add(vd); }
          break;   /* go to next actuator */ 
        }
      }
    }
  }

  public static int sharingMeasure(Vector dspecs) 
  { int res = 0; 
    for (int i = 0; i < dspecs.size(); i++) 
    { DCFDSpec ds1 = (DCFDSpec) dspecs.elementAt(i); 
      for (int j = i+1; j < dspecs.size(); j++) 
      { DCFDSpec ds2 = (DCFDSpec) dspecs.elementAt(j); 
        res = res + sharingMetric(ds1,ds2); 
      } 
    } 
    return res; 
  } 

  private static int sharingMetric(DCFDSpec ds1, DCFDSpec ds2) 
  { Vector ds1acts = Named.getNames(ds1.actuators); 
    Vector ds2acts = Named.getNames(ds2.actuators); 
    Vector shared = VectorUtil.intersection(ds1acts,ds2acts); 
    return shared.size(); 
  } 


  public String findComponentOf(String eventName)
  { for (int i = 0; i < sensors.size(); i++)
    { Statemachine sm = (Statemachine) sensors.elementAt(i);
      if (sm.hasEvent(eventName)) 
      { return sm.label; } 
    } 
    System.out.println("Sensor component not found for event " + eventName); 
    return ""; 
  } 

  public void setVarInfo(VarAnalInfo vai)
  { varanal = vai; }

  public Vector hierarchicalSplit(Vector cnames)
  { /* Returns coordinator invariants */
    if (varanal == null) 
    { System.err.println("No variable analysis performed yet!"); 
      return null; 
    } 
    if (varanal.locals.size() == 0 || 
        (varanal.locals.size() == 1 && varanal.locals.contains(label)) )
    { System.err.println("No sensor variables in locals of: " + label); 
      return null; 
    } 

    Vector cnames1 = (Vector) cnames.clone();
    cnames1.removeAll(varanal.locals);
    Vector res = new Vector();
    Vector newinvs = new Vector();
    Statemachine mysm = getStatemachine();
    Vector allsms = VectorUtil.vector_append(sensors,actuators);
    allsms.add(mysm);

    
    for (int i = 0; i < invariants.size(); i++)
    { Invariant inv = 
        (Invariant) invariants.get(i);
      if ((inv instanceof OperationalInvariant) &&
          inv.isSystem())
      { OperationalInvariant opinv =
          (OperationalInvariant) inv;
        String component = findComponentOf(opinv.eventName); 
        if (component == null)  // event is "foriegn", must create virtual 
        { Statemachine vs = Statemachine.createVirtualSensor(opinv); 
          sensors.add(vs); 
          varanal.addLocal(vs.label);  // also create visual rep
          opinv.sensorComponent = vs.label; 
          System.out.println("Virtual sensor " + vs.label + " created"); 
        }  
        else if (varanal.locals.contains(component)) { } 
        else // non-local, create virtual sensor
        { Statemachine vs = Statemachine.createVirtualSensor(opinv);
          sensors.add(vs);
          varanal.addLocal(vs.label);  // also create visual rep
          opinv.sensorComponent = vs.label; 
          System.out.println("Virtual sensor " + vs.label + " created"); 
        }
        Expression ante = opinv.antecedent();
        Expression localante =
          ante.filter(varanal.locals);
        if (localante == null) 
        { localante = new BasicExpression("true"); }

        Expression fante = ante.filter(cnames1);
        if (fante == null) 
        { fante = new BasicExpression("true"); }

        String dsevent = label + "_" + opinv.eventName;
        BasicExpression e = new BasicExpression(dsevent);
        e.setIsEvent(); 
        BasicExpression eventExp = new BasicExpression(opinv.eventName); 
        eventExp.setIsEvent();
        Expression cond = Expression.simplifyAnd(eventExp,fante); 

        String sen = findComponentOf(opinv.eventName); 
        OperationalInvariant supinv = 
          new OperationalInvariant(opinv.eventName,fante,e,sen); 
        supinv.typeCheck(allsms); 
        supinv.setEventEffect(); 
        supinv.createActionForm(allsms); 
        Expression eact = supinv.actionForm; 
        String javaOpCall = label.toLowerCase() + "." + 
                            opinv.eventName + "()";
        eact.setJavaForm(new BasicExpression(javaOpCall));
        supinv.modality = Invariant.SENACT;
        res.add(supinv);       
        opinv.replaceAntecedent(localante);
        newinvs.add(opinv);
      }
      else 
      { newinvs.add(inv); } 
    }
    System.out.println("New local invariants of " + label);
    Invariant.displayAll(newinvs);
    invariants = newinvs; 
    filterHier(); 
    System.out.println("Supervisor invariants: ");
    Invariant.displayAll(res);
    return res;
  }

  private void filterHier() 
  { /** Cuts down sensors, etc to be purely local */ 
    Vector newinvs = new Vector();
 
    if (varanal == null)
    { System.err.println("No variable analysis performed yet!");
      return; 
    }

    String act1 = varanal.label;  /* Primary actuator */ 
    Vector locs = varanal.locals; /* All other local components */ 
    locs.add(act1); 

    for (int i = 0; i < invariants.size(); i++)
    { Invariant inv = (Invariant) invariants.get(i);
      Vector lhs = inv.lhsVariables(vgroup);
      Vector rhs = inv.rhsVariables(vgroup);
      if (locs.containsAll(lhs) && locs.containsAll(rhs))
      { newinvs.add(inv); }
    }

    vgroup = locs; 
    invariants = newinvs; 
    sensors = VectorUtil.filterVector(locs,sensors); 
    actuators = VectorUtil.filterVector(locs,actuators); 
    visuals = VectorUtil.filterVector(locs,visuals); 
  } 

  public static Vector copySharedSensors(Vector dspecs)
  { // produces distinct copies of any
    // shared sensors for separate dspecs
    Vector sensorSets = new Vector();
    for (int i = 0; i < dspecs.size(); i++)
    { DCFDSpec ds = (DCFDSpec) dspecs.get(i);
      sensorSets.add(ds.sensors);
    }
    Vector shared = VectorUtil.sharedElements(sensorSets);
    // for each dspec copy each of its 
    // sensors which occur in shared:

    for (int i = 0; i < dspecs.size(); i++)
    { DCFDSpec ds = (DCFDSpec) dspecs.get(i);
      copySharedSensors(ds,shared);
    }
    return dspecs; 
  }

  public static void copySharedSensors(DCFDSpec ds, Vector shared)
  { Vector newsens = new Vector();
    Vector newinvs = new Vector();
    Vector sens = ds.sensors;
    Vector invs = ds.invariants;

    for (int i = 0; i < sens.size(); i++)
    { Statemachine sen = (Statemachine) sens.get(i);
      if (shared.contains(sen))
      { Statemachine newsc =
          Statemachine.copyStatemachine(sen,ds.label);
        // newsc.prefixEvents(ds.label); 
        newsens.add(newsc);
        ds.vgroup.remove(sen.label);
        ds.vgroup.add(newsc.label); // and rename ds.visuals
        RectData vd = 
         (RectData) VectorUtil.lookup(sen.label,ds.visuals); 
        if (vd != null) 
        { ds.visuals.remove(vd); 
          RectData rd = (RectData) vd.clone(); 
          rd.label = newsc.label;
          rd.component = newsc; 
          ds.visuals.add(rd); 
        } 
        
        for (int j = 0; j < invs.size(); j++) 
        { Invariant inv = (Invariant) invs.get(j); 
          if (inv instanceof OperationalInvariant) 
          { OperationalInvariant opinv = 
                (OperationalInvariant) inv; 
            if (opinv.sensorComponent.equals(sen.label))
            { opinv.addPrefix(ds.label); } 
          } 
          Invariant newinv =
            inv.substituteEq(sen.label,new BasicExpression(newsc.label)); 
          newinvs.add(newinv); 
        } 
        invs = (Vector) newinvs.clone(); 
        newinvs = new Vector(); 
      }
      else 
      { newsens.add(sen); }
    }
    ds.sensors = newsens; // also rename in invars.
    ds.invariants = invs; 
  }

  public Statemachine getStatemachine()
  { Vector events = new Vector();
    Vector states = new Vector();
    Vector trans = new Vector();
    Statemachine res;

    for (int i = 0; i < sensors.size(); i++)
    { Statemachine sen = (Statemachine) sensors.get(i);
      Vector evs = sen.getEvents(); 
      for (int j = 0; j < evs.size(); j++) 
      { Event ev = (Event) evs.get(j); 
        events.add(new Event(label + "_" + ev.label)); 
      } 
    }
    res = new Statemachine(null,events,trans,states);
    res.setcType(DCFDArea.CONTROLLER);
    res.label = label;
    return res;
  } 
}








