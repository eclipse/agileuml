import java.io.*; 
import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class Event extends Named implements Serializable
{ 
   Vector my_transitions = new Vector(); 

   public Event(String lab) 
   { super(lab); }

   public Object clone() 
   { return new Event(label); } 

   public int set_transitions(Vector trans)
   {int count = 0;
    for (int i = 0; i < trans.size(); i++)
    { if (label.equals(((Transition) trans.elementAt(i)).event.label))
       { count++;
          my_transitions.addElement((Transition) trans.elementAt(i)); }
    }
    return count;
   } 

   public String toString() 
   { return label; } 

   public boolean equals(Object obj) 
   { if (obj instanceof Event) 
     { if (label.equals(((Event) obj).label))
       { return true; } 
       else 
       { return false; } 
     } 
     else 
     { return false; } 
   } 

   public void display_event()
   { System.out.println("Transitions for event " + label); 
     for (int i = 0; i < my_transitions.size(); i++) 
     { ((Transition) my_transitions.elementAt(i)).display_transition(); } 
   } 

  /* For outer level generation: */ 
  public void displayOuterLevelTest(Vector trans,
                                    String oldV,
                                    String newV,
                                    Vector promoted, String pref)
  { String res = ""; 
    String eventName; 
   
    if (promoted == null || promoted.contains(this))
    { eventName = label; } 
    else 
    { eventName = "fdc_" + label; } 

    for (int i = 0; i < trans.size(); i++)
    { Transition t = (Transition) trans.elementAt(i);
      if (t.event != null && t.event.label.equals(label))
      { if (res.equals("")) 
        { res = "  IF (" + 
                oldV + " = " + t.source.label + " & " + 
                newV + " = " + t.target.label + ")"; }  
        else 
        { res = res + " or (" + 
                oldV + " = " + t.source.label + " & " + 
                newV + " = " + t.target.label + ")"; }
      }
    }
    System.out.println(res);
    System.out.println("  THEN " + pref + eventName); 
  }


  public void displayOuterLevelTest(Vector trans,
                                    String oldV,
                                    String newV,
                                    Vector promoted, 
                                    PrintWriter out, String pref)
  { String res = "";
    String eventName;
  
    if (promoted == null || promoted.contains(this))
    { eventName = label; }
    else
    { eventName = "fdc_" + label; }

    for (int i = 0; i < trans.size(); i++)
    { Transition t = (Transition) trans.elementAt(i);
      if (t.event != null && t.event.label.equals(label))
      { if (res.equals(""))
        { res = "  IF (" +
                oldV + " = " + t.source.label + " & " +
                newV + " = " + t.target.label + ")"; }
        else
        { res = res + " or (" +
                oldV + " = " + t.source.label + " & " +
                newV + " = " + t.target.label + ")"; }
      }
    }
    out.println(res);
    out.println("  THEN " + pref + eventName); 
  }

  public void displayJavaOuterLevelTest(Vector trans,
                              String oldV, String newV,
                              Vector promoted)
  { String res = ""; 
    String eventName; 
   
    if (promoted == null || promoted.contains(this))
    { eventName = "controller." + label + "();"; } 
    else 
    { eventName = "fdc." + label + "();"; } 

    for (int i = 0; i < trans.size(); i++)
    { Transition t = (Transition) trans.elementAt(i);
      if (t.event != null && t.event.label.equals(label))
      { if (res.equals("")) 
        { res = "    if ((" + 
                oldV + " == " + t.source.label + " && " + 
                newV + " == " + t.target.label + ")"; }  
        else 
        { res = res + " || (" + 
                oldV + " == " + t.source.label + " && " + 
                newV + " == " + t.target.label + ")"; }
      }
    }
    System.out.println(res + ")");
    System.out.println("    { " + eventName + " }"); }

  public void displayJavaOuterLevelTest(Vector trans,
                              String oldV, String newV,
                              Vector promoted, 
                              PrintWriter out)
  { String res = "";
    String eventName;

    if (promoted == null || promoted.contains(this))
    { eventName = "controller." + label + "();"; }
    else
    { eventName = "fdc." + label + "();"; }

    for (int i = 0; i < trans.size(); i++)
    { Transition t = (Transition) trans.elementAt(i);
      if (t.event != null && t.event.label.equals(label))
      { if (res.equals(""))
        { res = "    if ((" +
                oldV + " == " + t.source.label + " && " +
                newV + " == " + t.target.label + ")"; }
        else
        { res = res + " || (" +
                oldV + " == " + t.source.label + " && " +
                newV + " == " + t.target.label + ")"; }
      }
    }
    out.println(res + ")");
    out.println("    { " + eventName + " }"); }


  public Vector getBPDRelevantControllers(Vector dspecs)
  { Vector res = new Vector();
    for (int i = 0; i < dspecs.size(); i++)
    { DCFDSpec ds = (DCFDSpec) dspecs.get(i);
      String ff = ds.findComponentOf(label);
      if (ff.equals(""))  { /* ds ignores label */ }
      else 
      { res.add(ds.label); } 
    }
    return res; 
  }

}

