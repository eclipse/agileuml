/**
      * Classname : StatechartArea
      * 
      * Version information : 1
      *
      * Date : 
      * 
      * Description: This class describes the area that all the painting for 
      * the statecharts will be performed and deals with painting them 
      * depending on the users actions with the mouse. (Detecting events)
*/
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 
import java.awt.print.*; 


class StatechartArea extends JPanel 
    implements MouseListener, MouseMotionListener, KeyListener, Printable
{
    //these indicate which option the user has chosen from the menu
    public static final int INERT = -1; 
    public static final int SLINES = 0;
    public static final int POINTS = 1;
    public static final int EVENTS = 2;
    public static final int DLINES = 3;
    public static final int EDIT = 4; 
    public static final int ORSTATE = 5; 


    public static final int SOLID = 0; 
    public static final int DASHED = 1; 

    public static final int EDITING = 0; 
    public static final int DELETING = 1; 
    public static final int MOVING = 2; 
    public static final int GLUEMOVE = 3; 
    public static final int RESIZING = 4; 

    //Solid line -- could be made smaller for thinner line
    final static BasicStroke stroke = new BasicStroke(2.0f);
    
    StateWin controller;
    
    //Get the screen size for the size of the scroll bar view
    private Dimension screenSize = 
      Toolkit.getDefaultToolkit().getScreenSize();   
    private Dimension preferredSize = screenSize; 
 
  //Record mode e.g. Lines, Points etc.
  int mode = INERT;
  int editMode = EDITING; 

  private int x1, y1;  // x1,y1 store where the mouse has been pressed
  private int x2, y2;  //end of line coordinates
  private boolean firstpress = false;
    //True when the user draws a transition or drags mouse
    
  // True if the user pressed, dragged or released the mouse outside of
  // the rectangle; false otherwise.
  private boolean pressOut = false;   

  //Dialog definitions
  private EvtNameDialog nameDialog;
  private StateEdtDialog stateDialog; 
  private TransEditDialog transDialog;   
	
  Vector visuals = new Vector();  //holds all the points of the elements drawn
  Vector eventlist = new Vector();  //holds all the events entered	 
  Vector inputevents = new Vector(); // events that can trigger transitions
  Vector outputevents = new Vector(); // events that can be in generations

  private State selectedState = null; 
  private VisualData selectedVisual = null; 
  private Transition selectedTransition = null; 
  private Vector glueset = new Vector(); // of GlueSet

  // These variables keep a count of all the items created 
  // They will be replaced with labels at a later stage
  private int rectcount = 0;
  private int linecount = 0;

  //Each statechart consists of a model 
  Statemachine module; 

  //pointer to the statehandle to get number of frames created
  // WinHandling windHand;

 public StatechartArea(StateWin controller, String s)
 { this.controller = controller;
    
    // windHand = new WinHandling();
    
    //numArea is the number of Statechart window open 
    // (number of concurrent modules)
    module = new Statemachine(s, this);
    
    Border raisedBevel = BorderFactory.createRaisedBevelBorder();
    Border loweredBevel = BorderFactory.createLoweredBevelBorder();
    Border compound = 
      BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
    setBorder(compound);
    setBackground(Color.white);
    addMouseListener(this);
    addMouseMotionListener(this);	  
    addKeyListener(this);
  }

  public String getName()
  { if (module == null) 
    { return "nullmodule"; } 
    return module.getName();
  } 

  public void createCopy(String s, StatechartArea instance) 
  { for (int i = 0; i < eventlist.size(); i++) 
    { Event e = (Event) eventlist.get(i); 
      Event e1 = new Event(s + "." + e.label); 
      instance.addEvent(e1); 
      instance.module.addEvent(e1); 
    } 
    
    for (int i = 0; i < inputevents.size(); i++) 
    { Event e = (Event) inputevents.get(i); 
      Event e1 = new Event(s + "." + e.label); 
      instance.addInputEvent(e1); 
      instance.module.addEvent(e1); 
    }

    for (int i=0; i < visuals.size(); i++)
    {
      if (visuals.elementAt(i) instanceof RoundRectData)
      {
	RoundRectData rd = (RoundRectData) visuals.elementAt(i);
	
    // Vector sts = module.getStates();
    // for (int j = 0; j < sts.size(); j++) 
        State st = rd.state; 
        BasicState st1 = new BasicState(s + "." + st.label);
        if (st.isInitial())
        { // st1.setInitial(true); 
          instance.module.setInitial(st1); 
        } 
        else 
        { instance.module.addState(st1); } 

        RoundRectData rd1 = (RoundRectData) rd.clone(); 
        rd1.setName(s + "." + st.label); 
        rd1.setState(st1); 
        instance.addVisual(rd1); 
        // instance.module.addState(st1);  
      } 
    } 

    for (int i=0; i < visuals.size(); i++)
    {
      if (visuals.elementAt(i) instanceof LineData)
      {
	LineData ld = (LineData) visuals.elementAt(i);
	
        Transition tr = ld.transition;
        Event ev = tr.event; 
        State src = tr.source; 
        State trg = tr.target; 
        Expression grd = tr.guard; 
        Vector effects = tr.generations; 

        BasicState st1 = (BasicState) VectorUtil.lookup(s + "." + src.label,
                                                        instance.module.states);
       
        BasicState st2 = (BasicState) VectorUtil.lookup(s + "." + trg.label,
                                                        instance.module.states);
        Event ev1 = 
          (Event) VectorUtil.lookup(s + "." + ev.label,instance.module.events); 

        Transition tr1 = new Transition(s + "." + tr.label, st1, st2, ev1); 

        Expression grd1 = null; 
        if (grd == null) 
        { tr1.guard = new BasicExpression("true"); }  
        else if (module.getModelElement() instanceof Entity) 
        { grd1 = (Expression) grd.clone(); 
          Vector atts = ((Entity) module.getModelElement()).getAttributes();
          for (int k = 0; k < atts.size(); k++) 
          { Attribute att = (Attribute) atts.get(k); 
            grd1 = grd1.substituteEq(att + "", 
                                    new BasicExpression(s + "." + att));  
          }
          tr1.guard = grd1;   
        } 

        Vector gens1 = new Vector(); 
        if (effects == null || effects.size() == 0) 
        { tr1.generations = gens1; } 
        else if (module.getModelElement() instanceof Entity) 
        { Expression gen1 = (Expression) ((Expression) effects.get(0)).clone(); 
          Vector atts = ((Entity) module.getModelElement()).getAttributes();
          for (int k = 0; k < atts.size(); k++) 
          { Attribute att = (Attribute) atts.get(k); 
            gen1 = gen1.substituteEq(att + "", 
                                    new BasicExpression(s + "." + att));  
          }
          gens1.add(gen1); 
          tr1.generations = gens1;   
        } 

        LineData ld1 = (LineData) ld.clone(); 
        ld1.setName(s + "." + tr.label); 
        ld1.setTransition(tr1); 
        instance.addVisual(ld1); 
        instance.module.add_trans(tr1);  
      } 
    } 

      /* if (sd.states.size() > 0) 
      { module.setInitial((State) sd.states.get(0)); } 
      setVisuals(sd.rects); 
      for (int i = 0; i < sd.lines.size(); i++)
      { LineData ld = (LineData) sd.lines.get(i);
        addLine(ld,sd.transguards[i],sd.transgens[i]); 
        // Transition tr = new Transition(ld.label); 
        // tr.setGuard(sd.transguards[i]); 
        // tr.setGenerations(sd.transgens[i],sd.events); 
        // module.add_trans(tr); 
        // ld.setTransition(tr);
        // find_src_targ(ld,tr); 
        // Event e = (Event) VectorUtil.lookup(ld.label,eventlist); 
        // if (e != null) 
        // { tr.setEvent(e); } 
        // else 
        // { System.err.println("No event with name " + ld.label); } 
      }  */ 

    Vector oldatts = module.getAllAttributes(); 
      // put renamed copies as variables of instance.module: 
    for (int h = 0; h < oldatts.size(); h++) 
    { Attribute att = (Attribute) oldatts.get(h); 
      Attribute newatt = (Attribute) att.clone(); 
      newatt.setName(s + "." + att.getName()); 
      instance.module.addAttribute(newatt); 
    } 
  } 
  
  public Dimension getPreferredSize() {
    return preferredSize;
  }

  public int getMaxX() // maximum x-coord of any shape 
  { int max = 0; 
    for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      int x = vd.getx(); 
      if (x > max)
      { max = x; } 
    } 
    return max; 
  } 

  public int getMinX() // min x-coord of any shape 
  { int min = 1000;
    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      int x = vd.getx();
      if (x < min)
      { min = x; }
    }
    return min;
  }

  public int getMaxY() // maximum y-coord of any shape 
  { int max = 0;
    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      int y = vd.gety();
      if (y > max)
      { max = y; }
    }
    return max;
  }

  public int getMinY() // min y-coord of any shape 
  { int min = 1000;
    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      int y = vd.gety();
      if (y < min)
      { min = y; }
    }
    return min;
  }

  public void shrink(int factor) 
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      vd.shrink(factor); 
    } 
  } 

  public void moveAllDown(int amount) 
  { for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      vd.moveDown(amount);
    }
  }

  public void moveAllUp(int amount)
  { for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      vd.moveUp(amount);
    }
  }

  public void moveAllLeft(int amount)
  { for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      vd.moveLeft(amount);
    }
  }

  public void moveAllRight(int amount)
  { for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      vd.moveRight(amount);
    }
  }

  public Vector getVisuals() 
  { return visuals; } 

  public void addVisual(VisualData vd)
  { visuals.add(vd); } 

  public int getCType() 
  { return module.getCType(); } 

  public void enableExtraMenus()
  { controller.enableExtraMenus(); } 
  
  public void disableExtraMenus()
  { controller.disableExtraMenus(); } 
  
  public void setVisuals(Vector vis) 
  { visuals = vis; } 

  public void setEvents(Vector evs) 
  { eventlist = evs; 
    module.setEvents(evs); 
  } 

  public void setInputEvents(Vector evs)
  { inputevents = evs;
    module.setEvents(evs); 
  } 

  public void addInputEvent(Event e)
  { inputevents.add(e); } 

  public void setOutputEvents(Vector evs)
  { outputevents = evs; } 

  public void setMultiplicity(int m) 
  { module.setMultiplicity(m); 
    repaint(); 
  } 

  public void setAttribute(String att)
  { String nme = att.trim(); 
    if (nme.equals("") || module.label.equals(nme))
    { System.err.println("Invalid name: " + nme); } 
    else 
    { module.setAttribute(nme); } 
    repaint(); 
  }

  public void addAttribute(String nme, String typ, String init)
  { module.addAttribute(nme,typ,init); } 

  public void setMaxval(String mx)
  { int mm = 0; 
    try { mm = Integer.parseInt(mx); } 
    catch (Exception e)
    { System.err.println("Not a valid integer!");
      return; 
    } 
    if (mm > 0)
    { module.setMaxval(mm); } 
    else 
    { System.err.println("Max value must be > 0"); } 
    repaint();
  }

  public void setModule(Statemachine sm) 
  { module = sm; } 

  public void disp_States()
  {
    module.dispStates();
  }

  public void disp_Trans()
  {
    module.dispTrans();
  }

  public void dispEvents() 
  { for (int i = 0; i< eventlist.size(); i++)
    { System.out.println(eventlist.elementAt(i)); }
    System.out.println("Input events: " + inputevents); 
    System.out.println("Output events: " + outputevents);  
    
    System.out.println("Module events: " + module.events); 

    // for (int j = 0; j < module.events.size(); j++) 
    // { System.out.println(module.events.get(j)); } 
  } 

  public void dispAttributes()
  { Vector atts = module.getAttributes(); 
    for (int i = 0; i < atts.size(); i++) 
    { System.out.println(atts.get(i)); } 
  } 

  public void find_src_targ(LineData line, Transition strans)
  { boolean valid = false; 
    boolean sourceFound = false; 
    boolean targetFound = false; 
 
    for (int i=0; i < visuals.size(); i++)
    {
	if (visuals.elementAt(i) instanceof RoundRectData)
	{
	  RoundRectData rd = (RoundRectData) visuals.elementAt(i);
	  if (rd.isUnder(line.xstart,line.ystart))
	  {
	    strans.setSource(rd.state);
	    State st = strans.source;
	    System.out.println(" source ==> " + st.label);
            sourceFound = true;  
          }
	  if (rd.isUnder(line.xend, line.yend))
	  {
	    strans.setTarget(rd.state);
	    State ta = strans.target;
	    System.out.println(" target ==> " + ta.label); 
            targetFound = true; 
          }
        }    
    }

    if (sourceFound && targetFound) { valid = true; }
    else 
    { System.err.println("ERROR! Invalid transition, no source or target!"); 
      valid = false; 
    }  
  }

  private void findGlueSet()
  { if (selectedVisual instanceof RoundRectData)
    { RoundRectData rd = (RoundRectData) selectedVisual; 
      int x = rd.getx();
      int y = rd.gety();
      for (int i = 0; i < visuals.size(); i++)
      { VisualData vd = (VisualData) visuals.get(i);
        if (vd instanceof LineData)
        { LineData ld = (LineData) vd;
          if (rd.isUnder(ld.getx(),ld.gety()))
          { ld.setStartSelected();
            if (rd.isUnder(ld.xend,ld.yend))
            { ld.setMidSelected();
              glueset.add(
                new GluedVisual(ld,
                      rd.width/2,
                      rd.height/2));
            }
            else
            { glueset.add(
                new GluedVisual(ld, ld.getx() - x, 
                                ld.gety() - y));
            }
          }
          else if (rd.isUnder(ld.xend,ld.yend))
          { glueset.add(
              new GluedVisual(ld, ld.xend - x, 
                              ld.yend - y));
            ld.setEndSelected(); 
          }
        }
      }
    }
  }

  public void setEditMode(int emode)
  { editMode = emode; 
    setAppropriateCursor(mode); 
  } 

  public void removeVisualState(State s)
  { Vector deleted = new Vector(); 
    for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof LineData)
      { if (s == ((LineData) vd).transition.source ||
            s == ((LineData) vd).transition.target) 
        { deleted.add(vd); } 
      } 
    }
    visuals.removeAll(deleted); 

    for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof RoundRectData)
      { if (((RoundRectData) vd).state == s) 
        { visuals.remove(vd); 
          return; 
        } 
      } 
    } 
  } 

  public void removeVisualTransitions(Vector trans)
  { Vector removed = new Vector(); 
    for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof LineData)
      { if (trans.contains(((LineData) vd).transition)) 
        { removed.add(vd); } 
      } 
    } 

    visuals.removeAll(removed); 
  } 

  public void moveTransitionTarget(Transition t, int newx, int newy)
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof LineData)
      { LineData ld = (LineData) vd; 
        if (t == ld.transition) 
        { ld.setEndpoint(ld.xend + newx, ld.yend + newy);  
          return; 
        } 
      } 
    } 
  } 

  public void moveTransitionSource(Transition t, int newx, int newy)
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof LineData)
      { LineData ld = (LineData) vd; 
        if (t == ld.transition) 
        { ld.setStartpoint(ld.xstart + newx, ld.ystart + newy);  
          return; 
        } 
      } 
    } 
  } 


  public RoundRectData getVisual(State s)
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof RoundRectData)
      { RoundRectData res = (RoundRectData) vd; 
        if (res.state == s)
        { return res; } 
      } 
    } 
    return null; 
  } 

  public void findSelected(int x, int y)
  { boolean found = false; 

    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.elementAt(i);
      if (vd.isUnder(x,y) || vd.isUnderStart(x,y) || vd.isUnderEnd(x,y))
      { selectedVisual = vd;
        found = true; 
        if (vd instanceof RoundRectData)
        { selectedState = ((RoundRectData) vd).state;
         
          System.out.println("Selected state: " + selectedState.label); 
        }
        else if (vd instanceof LineData) 
        { selectedTransition = 
                    ((LineData) vd).transition;
          
          System.out.println("Selected transition: " + 
                                  selectedTransition.label); 
        }
        return; 
      } 
    } 
    if (!found)
    { System.out.println("No selection -- click on state " + 
                         "or on name of transition to select"); 
    } 
  }

  private void resetSelected()
  { selectedVisual = null;
    selectedState = null;
    selectedTransition = null; 
    glueset.clear(); 
  } 

  private void editSelected()
  { if (selectedState != null)
    { editState(selectedState); } 
    else if (selectedTransition != null) 
    { editTrans(selectedTransition); } 
    else 
    { System.out.println("No selection"); } 
    mode = INERT; 
    setAppropriateCursor(INERT); 
  } 

  private void deleteSelected() 
  { visuals.remove(selectedVisual);
    if (selectedState != null)
    { module.deleteState(selectedState); }
    else if (selectedTransition != null)
    { module.deleteTransition(selectedTransition); } 
  }

  private void moveSelected(int oldx, int oldy, int x, int y) 
  { if (selectedVisual != null) 
    { selectedVisual.changePosition(oldx,oldy,x,y); } 
  } 

  private void resizeSelected(int x, int y) 
  { if (selectedVisual != null) 
    { if (selectedVisual instanceof RectForm)
      { RectForm rf = (RectForm) selectedVisual; 
        rf.extendTo(x,y); 
      } 
    } 
  } 

  private void moveGlueSet(int x, int y)
  { moveSelected(0,0,x,y);
    for (int i = 0; i < glueset.size(); i++)
    { GluedVisual gv = (GluedVisual) glueset.get(i);
      LineData ld = gv.line;
      ld.changePosition(0,0,x+gv.xdisp, y+gv.ydisp);
    }
  }

  private void tofrontSelected()
  { if (selectedVisual != null)
    { visuals.remove(selectedVisual);
      visuals.add(selectedVisual); 
    }
  }  // Not actually used. 

  // public void saveToFile(String ofile)
  // { /* StatechartData data = 
  //    new StatechartData(module,visuals,eventlist);

  //  try 
  //  { ObjectOutputStream out =
  //        new ObjectOutputStream(new FileOutputStream(ofile));
  //    out.writeObject(data);
  //    out.close(); }
  //   catch (IOException e) {} */ 

  //  try
  //  { PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ofile))); 
  //    outputBCode(out); 
  //    out.close(); } 
  //  catch (IOException e) {} } 

  public void saveJavaToFile(File file) 
  { try
    { PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file))); 
      outputJavaCode(out); 
      out.close(); 
    } 
    catch (IOException e) {} 
  }
    
  public void saveBToFile(File file) 
  { try
    { PrintWriter out = 
        new PrintWriter(new BufferedWriter(new FileWriter(file))); 
      outputBCode(out); 
      out.close(); 
    } 
    catch (IOException e) {} 
  }

  public OrState asRegion(String nme)
  { Vector sts = module.getStates(); 
    Vector trs = module.getTransitions(); 
    State ini = module.getInitial(); 
    OrState res = new OrState(nme, ini, trs, sts);
    res.setAttributes(module.getAttributes()); 
    res.setEvents(module.getEvents()); 
    return res;  
  } 

  public void createProduct(String f1, String f2)
  { StatechartArea sc1 = new StatechartArea(controller,f1); 
    sc1.loadDataFromFile(f1); 
    StatechartArea sc2 = new StatechartArea(controller,f2); 
    sc2.loadDataFromFile(f2); 
    OrState region1 = sc1.asRegion(f1); 
    OrState region2 = sc2.asRegion(f2); 
    AndState ast = new AndState(f1 + f2, region1, region2); 
    module = ast.flatten();
    module.gui = this;  
    int scale = region2.substates.size(); 
    createProductStates(sc1.visuals,sc2.visuals,module.states,scale); 
    createProductTransitions(sc1.visuals,sc2.visuals,module.transitions,scale); 
  } 


  public void saveDataToFile(String file) 
  { module.saveData(file,visuals); } 

  public void loadDataFromFile(String file) 
  { StatechartData sd = Statemachine.retrieveData(file,module.cType);
    if (sd != null)
    { setEvents(sd.events);
      module.setStates(sd.states);
      if (sd.states.size() > 0) 
      { module.setInitial((State) sd.states.get(0)); } 
      setVisuals(sd.rects); 
      for (int i = 0; i < sd.lines.size(); i++)
      { LineData ld = (LineData) sd.lines.get(i);
        addLine(ld,sd.transguards[i],sd.transgens[i]); 
        // Transition tr = new Transition(ld.label); 
        // tr.setGuard(sd.transguards[i]); 
        // tr.setGenerations(sd.transgens[i],sd.events); 
        // module.add_trans(tr); 
        // ld.setTransition(tr);
        // find_src_targ(ld,tr); 
        // Event e = (Event) VectorUtil.lookup(ld.label,eventlist); 
        // if (e != null) 
        // { tr.setEvent(e); } 
        // else 
        // { System.err.println("No event with name " + ld.label); } 
      }
      module.setMultiplicity(sd.multiplicity); 
      if (sd.attribute != null) 
      { module.setAttribute(sd.attribute); 
        module.setMaxval(sd.maxval); 
      } 
      if (sd.attributes != null) 
      { Type intType = new Type("int",null); 
        for (int k = 0; k < sd.attributes.length; k++) 
        { Attribute attk = new Attribute(sd.attributes[k], 
                                         intType, ModelElement.INTERNAL); 
          module.addAttribute(attk); 
        } 
      }  
    } 
    repaint(); 
  } // No matching of events to transitions or lines? 

  // public void loadFromFile(File ifile)
  // { StatechartData data; 
  //  FileInputStream fs; 

  //  try 
  //  { fs = new FileInputStream(ifile); } 
  //  catch (IOException e)
  //  { System.out.println("Invalid load file");
  //    return; }

  //  try
  //  { ObjectInputStream in =
  //      new ObjectInputStream(fs);
  //    data = 
  //       (StatechartData) in.readObject(); }
  //  catch (IOException e)
  //  { System.out.println("Corrupted load file");
  //    return; }
  //  catch (ClassNotFoundException e)
  //  { System.out.println("Invalid data format"); 
  //    return; }

  //  /* visuals = data.visualobjects; 
  //  module = data.statechart; */ 
  //  eventlist = data.eventlist; } 

  public void editState(State ss) 
  { if (stateDialog == null)
    { stateDialog = new StateEdtDialog(controller); 
      stateDialog.pack();

      stateDialog.setLocationRelativeTo(controller); 
    } 

    stateDialog.setOldFields(ss.label,ss.initial); 
    stateDialog.setVisible(true);

    String txt = stateDialog.getName();
    boolean ini = stateDialog.getInit(); 
    String inv = stateDialog.getInv(); 
    String entry = stateDialog.getEntry(); 

    if (txt != null) 
    { String ttxt = txt.trim(); 
      if (ttxt.length() > 1)
      { System.out.println("The name entered is valid.");
        ss.setLabel(ttxt); 
        module.setTemplate(null);  // No longer a standard component
        ss.initial = ini; 
        if (ini) 
        { module.setInitial(ss); } 
        else 
        { module.unsetInitial(ss); } 
        ((RoundRectData) selectedVisual).setName(ttxt); 
      } 
      else 
      { System.err.println("Invalid name -- too short: " + ttxt); } 
    } 
    else
    { System.err.println("Null text"); } 

    if (inv != null)
    { Maplet props = ss.getProperties(); 
      if (props == null) 
      { props = new Maplet(null,new Vector()); }
      Vector invs = (Vector) props.dest; 
      Compiler comp = new Compiler(); 
      comp.lexicalanalysis(inv);
      Expression pinv = comp.parse(); 
      if (pinv != null) 
      { invs.add(pinv); } 
      ss.setProperties(props); 
    } 

    if (entry != null) 
    { Compiler cc = new Compiler(); 
      cc.lexicalanalysis(entry); 
      Expression ente = cc.parse(); 
      ss.setEntryAction(ente); 
    } 
  } 

  public boolean editTrans(Transition t)
  { boolean res = true; 
    LineData ld = (LineData) selectedVisual; 
 
    if (transDialog == null) 
    { transDialog = new TransEditDialog(controller); 
      transDialog.pack();

      transDialog.setLocationRelativeTo(controller); }

    if (t.event == null) 
    { transDialog.setOldFields(inputevents,"" + t.guard,
                               t.genToString()); 
    } 
    else 
    { transDialog.setOldFields(inputevents,
        "" + t.guard,t.genToString()); 
    } 

    transDialog.setVisible(true); 

    String txt = transDialog.getName(); 
    
    if (txt != null && !txt.trim().equals(""))
    { String ttxt = txt.trim();    
      System.out.println("The name entered is valid.");
      Event e = (Event) VectorUtil.lookup(ttxt,inputevents); 
      if (e != null) 
      { t.setEvent(e); } 
      else 
      { System.out.println("No event with name " + ttxt); } 
      ld.setName(ttxt); 
      module.setTemplate(null);  // is custom now. 
      String gens = transDialog.getGens(); 
      System.out.println("Generations: " + gens); 
      int multip = module.getMultiplicity(); 
      t.setGenerations(gens,outputevents); // eventlist must also
                                        // contain events of all
                                        // reciever modules.
      String gd = transDialog.getGuard(); 
      if (gd == null) 
      { t.setGuard("true"); } 
      else 
      { t.setGuard(gd); }
      // boolean ii = transDialog.getInput(); 
      // ld.setInput(ii); 
      return res; 
    }
    else
    { System.out.println("Null text"); /* Cancel pressed */
      return false; 
    } 
  }
     /* For gens should really be eventlist of its receivers */

  public void setDrawMode(int mode) 
  { // oldMode = this.mode; 
    switch (mode) {
    case SLINES:
    case DLINES:
    case POINTS:
      setAppropriateCursor(mode); 
      this.mode = mode;
      break; 
    case ORSTATE:
      setAppropriateCursor(POINTS);
      this.mode = mode;
      break;  
    case EDIT: 
      this.mode = mode;
      break;
    case EVENTS:
      setAppropriateCursor(mode); 
      if (nameDialog == null)
	{
	  //Create the new dialog box
	  nameDialog = new EvtNameDialog(controller); 
	  nameDialog.pack();

	  //set the location and make it visible
	  nameDialog.setLocationRelativeTo(controller);
	}
		
      //Make the dialogue box visible (already been created)
      nameDialog.setVisible(true); // this shows the dialog box
	
      //Get the new text (event name) entered from the textfield 
      String txt = nameDialog.getValidatedText();
      String ttxt = txt.trim(); 

      if (ttxt != null) 
	{
	  // The text is valid - it is not null 
	  // and has passed the other test as well.
	  System.out.println("The event entered is valid.");
        Event newevent = new Event(ttxt); 
	  eventlist.addElement(newevent); 
	  module.add_event(newevent); 
      }
      break;
    case INERT: 
      this.mode = mode;
      break; 
    default:
      throw new IllegalArgumentException();
    }
  }

  public void keyPressed(KeyEvent e)
  { char c = e.getKeyChar();
    if (c == 'u') { moveAllDown(20); }
    if (c == 'd') { moveAllUp(20); }
    if (c == 'l') { moveAllRight(20); }
    if (c == 'r') { moveAllLeft(20); } 
    // System.out.println(e);
    repaint(); 
  }

  public void keyReleased(KeyEvent e) 
  { } 

  public void keyTyped(KeyEvent e) 
  { } 
  
  public void mouseClicked(MouseEvent me)
  { requestFocus(); } 

  public void mouseEntered(MouseEvent me)
  { /* System.out.println("Mouse entered"); */ } 

  public void mouseExited(MouseEvent me)
  { /* System.out.println("Mouse exited"); */ } 

    //This procedure returns true if the element drawn is larger than the 
    //view and the scrollbars have to be adjusted.
    public boolean changed(int x, int y, int W, int H)
    {
	int this_width = (x + W + 10);
	if (this_width > preferredSize.width)
	    {
		preferredSize.width = this_width;
		return true;
	    }
	
	int this_height = (y + H + 10);
	if (this_height > preferredSize.height)
	    {
		preferredSize.height = this_height; 
		return true;
	    }
	return false;
    }
    

  public void addEvent(Event e) 
  { eventlist.addElement(e); 
    module.add_event(e); 
  } 

  private void createProductStates(Vector vis1, Vector vis2, Vector sts, int scale)
  { for (int i = 0; i < vis1.size(); i++) 
    { VisualData vd1 = (VisualData) vis1.get(i); 
      if (vd1 instanceof RoundRectData) 
      { RoundRectData rd1 = (RoundRectData) vd1; 
        for (int j = 0; j < vis2.size(); j++) 
        { VisualData vd2 = (VisualData) vis2.get(j); 
          if (vd2 instanceof RoundRectData) 
          { RoundRectData rd2 = (RoundRectData) vd2; 
            if (rd1.state != null && rd2.state != null) 
            { String lab = rd1.state.label + "," + rd2.state.label;
              State st = (State) VectorUtil.lookup(lab,sts); 
              if (st != null) 
              { RoundRectData rd = 
                  new RoundRectData(rd1.sourcex*scale + rd2.sourcex,
                                    rd1.sourcey*scale + rd2.sourcey, Color.black, rectcount); 
                rectcount++; 
                visuals.add(rd); 
                rd.setState(st);
                rd.label = lab;  
              }
            } 
          }
        }
      }
    }
  }

  private void createProductTransitions(Vector vis1, Vector vis2, Vector trs, int scale)
  { for (int i = 0; i < vis1.size(); i++) 
    { VisualData vd1 = (VisualData) vis1.get(i); 
      if (vd1 instanceof LineData) 
      { LineData ld1 = (LineData) vd1; 
        Transition tr1 = ld1.transition; 
        RoundRectData srcrd = 
          (RoundRectData) VectorUtil.lookup(tr1.source.label,vis1); 
        RoundRectData trgrd = 
          (RoundRectData) VectorUtil.lookup(tr1.target.label,vis1); 
        int delta1x = ld1.xstart - srcrd.sourcex; 
        int delta1y = ld1.ystart - srcrd.sourcey; 
        int delta2x = ld1.xend - trgrd.sourcex; 
        int delta2y = ld1.yend - trgrd.sourcey; 

        for (int j = 0; j < vis2.size(); j++) 
        { VisualData vd2 = (VisualData) vis2.get(j); 
          if (vd2 instanceof RoundRectData) 
          { RoundRectData rd2 = (RoundRectData) vd2; 
            // New line data for tr1,rd2.state
            LineData ld2 = 
              new LineData(srcrd.sourcex*scale + delta1x + rd2.sourcex, 
                           srcrd.sourcey*scale + delta1y + rd2.sourcey, 
                           trgrd.sourcex*scale + delta2x + rd2.sourcex, 
                           trgrd.sourcey*scale + delta2y + rd2.sourcey,
                           linecount,0);
            String lab = tr1.label + "," + rd2.state.label;
            Transition tr = (Transition) VectorUtil.lookup(lab,trs); 
            if (tr != null) 
            { ld2.transition = tr; } 
            linecount++; 
            visuals.add(ld2); 
            ld2.label = lab;  
          }
        }
      }
    }

    for (int i = 0; i < vis2.size(); i++) 
    { VisualData vd2 = (VisualData) vis2.get(i); 
      if (vd2 instanceof LineData) 
      { LineData ld2 = (LineData) vd2; 
        Transition tr2 = ld2.transition; 
        RoundRectData srcrd = 
          (RoundRectData) VectorUtil.lookup(tr2.source.label,vis2); 
        RoundRectData trgrd = 
          (RoundRectData) VectorUtil.lookup(tr2.target.label,vis2); 
        int delta1x = ld2.xstart - srcrd.sourcex; 
        int delta1y = ld2.ystart - srcrd.sourcey; 
        int delta2x = ld2.xend - trgrd.sourcex; 
        int delta2y = ld2.yend - trgrd.sourcey; 

        for (int j = 0; j < vis1.size(); j++) 
        { VisualData vd1 = (VisualData) vis1.get(j); 
          if (vd1 instanceof RoundRectData) 
          { RoundRectData rd1 = (RoundRectData) vd1; 
            // New line data for tr1,rd2.state
            LineData ld1 = 
              new LineData(rd1.sourcex*scale + delta1x + srcrd.sourcex, 
                           rd1.sourcey*scale + delta1y + srcrd.sourcey, 
                           rd1.sourcex*scale + delta2x + trgrd.sourcex, 
                           rd1.sourcey*scale + delta2y + trgrd.sourcey,
                           linecount,0);
            String lab = rd1.state.label + "," + tr2.label;
            Transition tr = (Transition) VectorUtil.lookup(lab,trs); 
            if (tr != null) 
            { ld1.transition = tr; } 
            linecount++; 
            visuals.add(ld1); 
            ld1.label = lab;  
          }
        }
      }
    }

  }
                        
 
  public void addRect(RoundRectData rd) 
  { rectcount++; 
    visuals.addElement(rd); 
    BasicState new_state = new BasicState(rd.label); 
    module.add_state(new_state); 
    rd.setState(new_state); 
  } 

  public void addInitialRect(RoundRectData rd) 
  { rectcount++; 
    visuals.addElement(rd); 
    BasicState new_state = new BasicState(rd.label); 
    module.add_state(new_state); 
    rd.setState(new_state); 
    new_state.initial = true; 
    module.setInitial(new_state); 
  } 

  public void addLine(LineData ld) 
  { visuals.addElement(ld); 
    Transition trans = new Transition("t" + linecount);
    linecount++;  
    ld.setTransition(trans);
    module.add_trans(trans); 
    find_src_targ(ld,trans); 
    Event e = (Event) VectorUtil.lookup(ld.label,eventlist); 
    if (e != null) 
    { trans.setEvent(e); } 
    else 
    { System.err.println("No event with name " + ld.label); } 
  }

  public void addLine(LineData ld, String guard, String gen) 
  { visuals.addElement(ld); 
    Transition trans = new Transition("t" + linecount);
    linecount++;  
    ld.setTransition(trans);
    module.add_trans(trans); 
    find_src_targ(ld,trans); 
    Event e = (Event) VectorUtil.lookup(ld.label,eventlist); 
    if (e != null) 
    { trans.setEvent(e); } 
    else 
    { System.err.println("No event with name " + ld.label); } 
    trans.setGuard(guard); 
    trans.setGenerations(gen,eventlist); 
   }

  public void createVisuals(Vector sensors) 
  { int spacing = 125;  
    int offset = 10; 
    int totalstates = module.states.size(); 

    // if (totalstates > 16) 
    // { return; } 

    Layout layout = Layout.getLayout(totalstates,sensors); 

    try 
    { for (int i = 0; i < totalstates; i++) 
      { State st = 
          (State) module.states.get(i);
        int x = layout.getX(i);
        int y = layout.getY(i);
        RoundRectData rd = 
          new RoundRectData(x, y, getForeground(), rectcount);
        rectcount++;
        rd.setName(st.label);
        rd.setState(st);
        visuals.add(rd); 
      }
    } 
    catch (Exception eee) 
    { eee.printStackTrace(); 
      System.err.println("State space too large: " + totalstates + 
                         " for visual presentation, but");
      System.err.println("you can still view the states using the StateList "); 
      System.err.println("option."); 
      return; 
    } 
    
    repaint();
 
    drawTransitions(module.transitions); 
    repaint(); 
  }

  private void drawTransitions(Vector trans) 
  { Vector lines = new Vector(); 
 
    for (int i = 0; i < trans.size(); i++)
    { Transition tr = 
        (Transition) trans.get(i);
      State src = tr.source;
      src.addOutgoingTransition(tr); // to optimise property verification.
      State targ = tr.target;
      RoundRectData rd1 =
        (RoundRectData) VectorUtil.lookup(src.label,visuals);
      RoundRectData rd2 =
        (RoundRectData) VectorUtil.lookup(targ.label,visuals);
      LineData ld = 
        LineData.drawBetween(rd1, rd2, linecount);
      linecount++;
      ld.setTransition(tr); 
      ld.setName(tr.event.label); 
      lines.add(ld);
    }
    visuals.addAll(lines);
  }

  public void mousePressed(MouseEvent me)
  { 
    int x = me.getX(); 
    int y = me.getY(); 
    x1 = x; 
    y1 = y; 
    x2 = x;  // ??
    y2 = y;  // ??
   
    boolean is_bigger = false;
    System.out.println("Mouse pressed at " + x + " " + y); 

    switch (mode) {
    case SLINES:
      System.out.println("This is LINES");
      // x1 = x;
      // y1 = y;    // Start line  
      firstpress = true;	
      break;
    case DLINES:
      System.out.println("This is LINES");
      // x1 = x;
      // y1 = y;    // Start line  
      firstpress = true;	
      break;
    case POINTS:
      System.out.println("This is POINTS");
      is_bigger = changed(x,y,50,50);
      RoundRectData rd = new RoundRectData(x,y,getForeground(),rectcount); 
      rectcount++;
      visuals.addElement(rd);
      BasicState new_state = new BasicState(rd.label);
      module.add_state(new_state);
      rd.setState(new_state);
      // x1 = x;
      // y1 = y;
      if (is_bigger)
	  {
	      //Update client's preferred size because 
	      //the area taken up by the graphics has
	      //gotten larger or smaller (got cleared).
	      setPreferredSize(preferredSize);
	      
	      //Let the scroll pane know to update itself
	      //and its scrollbars.
	      revalidate();  		  
	  }
      mode = INERT; 
      repaint();
      break;
    case ORSTATE:
      System.out.println("This is ORSTATE");
      is_bigger = changed(x,y,50,50);
      RoundRectData rdd = new RoundRectData(x,y,rectcount); 
      rectcount++;
      visuals.addElement(rdd);
      OrState orstate = new OrState(rdd.label,null,new Vector(), new Vector());
      module.add_state(orstate);
      rdd.setState(orstate);
      // x1 = x;
      // y1 = y;
      if (is_bigger)
	  {
	      //Update client's preferred size because 
	      //the area taken up by the graphics has
	      //gotten larger or smaller (got cleared).
	      setPreferredSize(preferredSize);
	      
	      //Let the scroll pane know to update itself
	      //and its scrollbars.
	      revalidate();  		  
	  }
      mode = INERT; 
      repaint();
      break;
    case EDIT: 
      findSelected(x,y);

      if (editMode == EDITING) 
      { editSelected();
        resetSelected();
        mode = INERT; // and change cursor
        setAppropriateCursor(INERT); 
      } 
      else if (editMode == DELETING) 
      { deleteSelected(); 
        resetSelected(); 
        mode = INERT; // change cursor
        setAppropriateCursor(INERT); 
      } 
      else if (editMode == GLUEMOVE)
      { findGlueSet(); } 
      else if (editMode == RESIZING)
      { System.out.println("Drag cursor to resize state"); } 

      repaint(); 
      break;
    default:
      System.out.println("This is default");
      break;
    }
  } 

  public void mouseReleased(MouseEvent e)
  { 
    int x = e.getX();
    int y = e.getY();
    System.out.println("Mouse released at " + x + " " + y); 
    switch (mode) {
    case SLINES:  
      LineData sline = new LineData(x1,y1,x,y,linecount,SOLID);     
      linecount++;
      visuals.addElement(sline);
      Transition strans = new Transition(sline.label);
      sline.setTransition(strans); 
      find_src_targ(sline, strans);
            
      firstpress = false;
      selectedVisual = sline;
      boolean doCreate = editTrans(strans);
      selectedVisual = null;
      if (doCreate) 
      { module.checkTrans(strans); 
        module.add_trans(strans);
      }
      else 
      { visuals.remove(sline); } 
      mode = INERT; 
      repaint(); 
      break;
    case DLINES:  
      LineData dline = new LineData(x1,y1,x,y,linecount,DASHED);     
      linecount++;
      visuals.addElement(dline); 
      Transition dtrans = new Transition(dline.label);
      dline.setTransition(dtrans); 
      
      find_src_targ(dline, dtrans);
      //check for the source and target later and add the values     
      firstpress = false;
      selectedVisual = dline;
      boolean doCreate2 = editTrans(dtrans);
      selectedVisual = null;

      if (doCreate2) 
      { module.checkTrans(dtrans); 
        module.add_trans(dtrans); 
      }
      else 
      { visuals.remove(dline); } 
      mode = INERT; 
      repaint(); 
      break; 
    case POINTS:
      break; 
    case EDIT: 
      updateModule(); 
      resetSelected(); 
      mode = INERT; 
      setAppropriateCursor(INERT); 
      break; 
    default:
      break;
    }
    repaint(); 
  } 

  public void mouseDragged(MouseEvent e)
  { /* System.out.println("Mouse dragged"); */  
  int x = e.getX();
  int y = e.getY();
  switch (mode) {
  case SLINES:  
    // Graphics gc_sline = this.getGraphics(); 
    // Graphics2D g2_sline = (Graphics2D) gc_sline;	
    // prevx = x2;
    // prevy = y2;
    x2 = x;
    y2 = y;	
    //g2.setColor(Color.white);
    //g2.drawLine(x1, y1, prevx, prevy);
    //g2.setColor(Color.black);
    //g2.drawLine(x1,y1, x2, y2);			
    break;
  case DLINES:  
    // Graphics gc_dline = this.getGraphics(); 
    // Graphics2D g2_dline = (Graphics2D) gc_dline;	
    // prevx = x2;
    // prevy = y2;
    x2 = x;
    y2 = y;	
    break;
  case POINTS:
  case EVENTS: 
    break; 
  case EDIT: 
    if (editMode == MOVING) 
    { moveSelected(0,0,x,y); }
    else if (editMode == GLUEMOVE) 
    { moveGlueSet(x,y); }  
    else if (editMode == RESIZING)
    { resizeSelected(x,y); } 
    break; 
  default:
    break;
  }
  repaint();
} 

  public void mouseMoved(MouseEvent e)
  { //System.out.println("Mouse moved at " + e.getX() + " " + e.getY()); 
  }
 

  public int print(Graphics g, PageFormat pf, int pi) 
  throws PrinterException
  { if (pi >= 1) { return Printable.NO_SUCH_PAGE; } 
    drawShapes((Graphics2D) g); 
    return Printable.PAGE_EXISTS; 
  } 


  private void drawShapes(Graphics2D g2)
  { g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON );
    g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                         RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    g2.setRenderingHint( RenderingHints.KEY_RENDERING,
                         RenderingHints.VALUE_RENDER_QUALITY );

    /* System.out.println("the paintComponent entered"); */

    //Create a thicker line for drawing objects
    g2.setStroke(stroke);

    //The number of elements in the drawing area
    int numstates = visuals.size();
    String att = module.getAttribute(); 

    if (att != null) 
    { int mv = module.getMaxval(); 
      g2.drawString(att + " : 0.." + mv + " := 0" + 
                    "        set" + att + "(" + att + "x)/" +
                    att + " := " + att + "x", 10, 20); 
    } 

    Vector atts = module.getAttributes(); 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute a = (Attribute) atts.get(i); 
      String init = ""; 
      if (a.getInitialExpression() != null)
      { init = " = " + a.getInitialExpression(); } 
   
      g2.drawString(a.getName() + " : " + a.getType() + init, 10, 15 + 15*i); 
    } 

    //Paint the previous visuals

    for (int i=0; i < numstates; i++)
    {
      //get the coordinates   
      VisualData vd = (VisualData) visuals.elementAt(i);
      //draw the data elements
      vd.drawData(g2);
    } 

    //Draws the line when the user drags and then stops but has not
    // released the mouse yet 
    if ((mode == DLINES) || (mode == SLINES))
    {
      //The mouse has not been released yet 
      if (firstpress == true)
      {
        g2.drawLine(x1,y1, x2, y2);
      }
    } 
  }
  
  public void paintComponent(Graphics g) 
  { super.paintComponent(g);  //clear the panel
    Graphics2D g2 = (Graphics2D) g;
    drawShapes(g2); 
  } 

  private void updateModule()
  { if (editMode == MOVING && selectedVisual != null)
    { if (selectedTransition != null)
      { find_src_targ((LineData) selectedVisual,selectedTransition); } 
      else if (selectedState != null) 
      { for (int i = 0; i < visuals.size(); i++) 
        { VisualData vd = (VisualData) visuals.elementAt(i); 
          if (vd instanceof LineData) 
          { LineData ld = (LineData) vd; 
            Transition t = ld.transition; 
            find_src_targ(ld,t); 
          } 
        } 
      }
    }
  }

  public void synthesiseB() 
  { int multip = module.getMultiplicity(); 
    if (multip == 1) 
    { System.out.println("Single component, multiplicity 1"); 
      test(); 
    } 
    else if (multip > 1) 
    { System.out.println("Multiple component, multiplicity " + multip);
      synthBMult(); 
    } 
    else 
    { System.err.println("Erroneous multiplicity: " + multip); } 
  } 

  private String bPromotes()
  { String res = "get" + module.label; 
    String att = module.getAttribute(); 
    if (att != null) 
    { res = res + ", get" + att + ", set" + att; } 
    return res; 
  } 
 
  private void test()  // synthB() 
  { if (module.cType == DCFDArea.ACTUATOR) 
    { System.out.println("MACHINE Actuator" + module.label); 
      System.out.println("SEES SystemTypes"); 
      System.out.println("INCLUDES M" + module.label); 
      System.out.println("PROMOTES " + bPromotes()); 
      System.out.println("OPERATIONS"); 

      Vector tt = module.getActionList(); 
      for (int i = 0; i < tt.size(); i++) 
      { Maplet mm = (Maplet) tt.elementAt(i); 
        System.out.println(module.label + "Set" + ((Named) mm.source).label + " = "); 
        ((CaseStatement) mm.dest).display(module.label); 
        if (i < tt.size() - 1) 
        { System.out.println(";"); }
        System.out.println(" "); 
      } 
      System.out.println("END\n\n"); } 

    module.displayBMachine(); 

    System.out.println("\n\n"); 

    testImp();  // synthBImp()

    System.out.println("\n\n"); 

    module.displayBImp(); 
  } 

  private void synthBMult()
  { String cName = module.label; 
    String obParam = cName.toUpperCase() + "_OBJ"; 
    String insts = cName + "s"; 

    if (module.cType == DCFDArea.ACTUATOR)
    { System.out.println("MACHINE Actuator" + cName + "(" + 
                         obParam + ")");
      System.out.println("SEES SystemTypes");
      System.out.println("INCLUDES M" + cName + "(" + 
                         obParam + ")");
      System.out.println("PROMOTES " + bPromotes());
      System.out.println("CONSTRAINTS card(" + obParam + ") = " + 
                         module.getMultiplicity()); 
      System.out.println("OPERATIONS");

      Vector tt = module.getMultActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.elementAt(i);
        System.out.println(cName + "Set" + 
                           ((Named) mm.source).label + "(oo) = ");
        System.out.println("  PRE oo: " + insts); 
        System.out.println("  THEN"); 
        ((CaseStatement) mm.dest).display(cName);
        System.out.println(); 
        System.out.print("  END"); 
        if (i < tt.size() - 1)
        { System.out.println(";"); } 
        System.out.println(" ");
      }
      System.out.println("END\n\n"); }

    module.displayMultipleBM();

    System.out.println("\n\n");

    synthMultImp();

    System.out.println("\n\n");

    module.displayMultipleBImp(); 
  }


  public void testImp()
  { if (module.cType == DCFDArea.ACTUATOR)
    { System.out.println("IMPLEMENTATION Actuator" + module.label + "_1");
      System.out.println("REFINES Actuator" + module.label);
      System.out.println("SEES SystemTypes");
      System.out.println("IMPORTS M" + module.label);
      System.out.println("PROMOTES " + bPromotes()); 
      System.out.println("OPERATIONS");
    
      Vector tt = module.getActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.get(i);
        String vv = module.label + "x"; 
        System.out.println(module.label + "Set" + 
                           ((Named) mm.source).label + " = ");
        System.out.println("  VAR " + vv);
        System.out.println("  IN " + vv +
                           " <-- get" + module.label + ";");
        ((CaseStatement) mm.dest).display(vv); 
        System.out.println(); 
        System.out.print("  END"); 
        if (i < tt.size() - 1)
        { System.out.println(";"); }
        System.out.println(" ");
      }
      System.out.println("END\n\n"); 
    }
  }

  public void testImp(PrintWriter out)
  { if (module.cType == DCFDArea.ACTUATOR)
    { out.println("IMPLEMENTATION Actuator" + module.label + "_1");
      out.println("REFINES Actuator" + module.label);
      out.println("SEES SystemTypes");
      out.println("IMPORTS M" + module.label);
      out.println("PROMOTES " + bPromotes()); 
      out.println("OPERATIONS");
    
      Vector tt = module.getActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.get(i);
        String vv = module.label + "x"; 
        out.println(module.label + "Set" + 
                           ((Named) mm.source).label + " = ");
        out.println("  VAR " + vv);
        out.println("  IN " + vv +
                           " <-- get" + module.label + ";");
        ((CaseStatement) mm.dest).display(vv,out); 
        out.println(); 
        out.print("  END"); 
        if (i < tt.size() - 1)
        { out.println(";"); }
        out.println(" ");
      }
      out.println("END\n\n"); 
    }
  }


  public void synthMultImp()
  { String cName = module.label;
    String obParam = cName.toUpperCase() + "_OBJ";

    if (module.cType == DCFDArea.ACTUATOR)
    { System.out.println("IMPLEMENTATION Actuator" + cName + "_1");
      System.out.println("REFINES Actuator" + cName);
      System.out.println("SEES SystemTypes");
      System.out.println("IMPORTS M" + module.label + "(" + obParam + ")");
      System.out.println("PROMOTES " + bPromotes());
      System.out.println("OPERATIONS");

      Vector tt = module.getMultActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.get(i);
        String vv = cName + "x";
        System.out.println(cName + "Set" +
                           ((Named) mm.source).label + "(oo) = ");
        System.out.println("  VAR " + vv);
        System.out.println("  IN " + vv +
                           " <-- get" + cName + "(oo);");
        ((CaseStatement) mm.dest).display(vv);
        System.out.println();
        System.out.print("  END");
        if (i < tt.size() - 1)
        { System.out.println(";"); }
        System.out.println(" ");
      }
      System.out.println("END\n\n");
    }
  }

  public void synthMultImp(PrintWriter out)
  { String cName = module.label;
    String obParam = cName.toUpperCase() + "_OBJ";

    if (module.cType == DCFDArea.ACTUATOR)
    { out.println("IMPLEMENTATION Actuator" + cName + "_1");
      out.println("REFINES Actuator" + cName);
      out.println("SEES SystemTypes");
      out.println("IMPORTS M" + module.label + "(" + obParam + ")");
      out.println("PROMOTES " + bPromotes());
      out.println("OPERATIONS");

      Vector tt = module.getMultActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.get(i);
        String vv = cName + "x";
        out.println(cName + "Set" +
                           ((Named) mm.source).label + "(oo) = ");
        out.println("  VAR " + vv);
        out.println("  IN " + vv +
                           " <-- get" + cName + "(oo);");
        ((CaseStatement) mm.dest).display(vv,out);
        out.println();
        out.print("  END");
        if (i < tt.size() - 1)
        { out.println(";"); }
        out.println(" ");
      }
      out.println("END\n\n");
    }
  }


  private String javaInherits()
  { if (module.getMultiplicity() <= 1) 
    { return "implements SystemTypes"; } 
    else 
    { return "extends M" + module.label + " implements SystemTypes"; } 
  } 

  /* public void synthesiseJava()
  { int mult = module.getMultiplicity(); 
    String fini = ""; 
    String stat = "";
    if (module.getMultiplicity() <= 1)
    { stat = "static ";
      fini = "final "; 
     }

    if (module.cType == DCFDArea.ACTUATOR)
    { System.out.println("final class Actuator" + module.label);
      System.out.println(javaInherits());
      System.out.println("{");

      Vector tt = module.getActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.elementAt(i);
        System.out.println("public " + stat + "void " + module.label + 
                           "Set" + ((Named) mm.source).label + "() ");
        System.out.println("{"); 
        ((CaseStatement) mm.dest).displayJava(module.label);
        System.out.println("}\n"); 
      } 
      System.out.println(" ");
      System.out.println("}\n\n"); }

    module.displayJavaClass(); 
  } */ 

  public void synthesiseJava()
  { module.synthesiseJava(); } 

  public void synthesiseBCode()
  { module.synthesiseB(); } 

  public void synthesiseSmv() 
  { SmvModule mod = 
      new SmvModule(module, new Vector()); 
    mod.display(); 
  } 

  private void outputJavaCode(PrintWriter out)
  { String stat = "";
    if (module.getMultiplicity() <= 1)
    { stat = "static "; }

    if (module.cType == DCFDArea.ACTUATOR)
    { out.println("final class Actuator" + module.label);
      out.println(javaInherits());
      out.println("{");

      Vector tt = module.getActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.elementAt(i);
        Named tsource = (Named) mm.source; 
        out.println("public " + stat + "void " + module.label + "Set" + 
                    tsource.label + "() ");
        out.println("{");
        ((CaseStatement) mm.dest).displayJava(module.label, out);
        out.println("}\n");
      }
      out.println(" ");
      out.println("}\n\n"); 
    }

    module.displayJavaClass(out); 
  }

  private void outputBCode(PrintWriter out)
  { int multip = module.getMultiplicity();
    if (multip == 1)
    { System.out.println("Single component, multiplicity 1");
      outputBSingleInstance(out);
    }
    else if (multip > 1)
    { System.out.println("Multiple component, multiplicity " + multip);
      outputBMultipleInstances(out);
    }
    else
    { System.err.println("Erroneous multiplicity: " + multip); }
  }

  // Print multiple B code if module.getMultiplicity() > 1
  private void outputBSingleInstance(PrintWriter out) 
  { if (module.cType == DCFDArea.ACTUATOR)
    { out.println("MACHINE Actuator" + module.label);
      out.println("SEES SystemTypes");
      out.println("INCLUDES M" + module.label);      
      out.println("OPERATIONS");

      Vector tt = module.getActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.elementAt(i);
        out.println(module.label + "Set" + ((Named) mm.source).label + " = ");
        ((CaseStatement) mm.dest).display(module.label,out);
        if (i < tt.size() - 1)
        { out.println(";"); } /* and ; */
        out.println(" ");
      }
      out.println("END\n\n"); 
    }

    module.displayBMachine(out); 
    out.println("\n\n");

    testImp(out);  // synthBImp()

    out.println("\n\n");

    module.displayBImp(out);
  }

  private void outputBMultipleInstances(PrintWriter out)
  { String cName = module.label;
    String obParam = cName.toUpperCase() + "_OBJ";
    String insts = cName + "s";

    if (module.cType == DCFDArea.ACTUATOR)
    { out.println("MACHINE Actuator" + cName + "(" +
                         obParam + ")");
      out.println("SEES SystemTypes");
      out.println("INCLUDES M" + cName + "(" +
                         obParam + ")");
      out.println("PROMOTES " + bPromotes());
      out.println("CONSTRAINTS card(" + obParam + ") = " +
                         module.getMultiplicity());
      out.println("OPERATIONS");

      Vector tt = module.getMultActionList();
      for (int i = 0; i < tt.size(); i++)
      { Maplet mm = (Maplet) tt.elementAt(i);
        out.println(cName + "Set" +
                           ((Named) mm.source).label + "(oo) = ");
        out.println("  PRE oo: " + insts);
        out.println("  THEN");
        ((CaseStatement) mm.dest).display(cName,out);
        out.println();
        out.print("  END");
        if (i < tt.size() - 1)
        { out.println(";"); }
        out.println(" ");
      }
      out.println("END\n\n"); 
    }

    module.displayMultipleBM(out);
    out.println("\n\n");

    synthMultImp(out);

    out.println("\n\n");

    module.displayMultipleBImp(out);
  }



  private void setAppropriateCursor(int mode) 
  { if (mode == EDIT) 
    { if (editMode == DELETING) 
      { setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); } 
      else if (editMode == MOVING || editMode == GLUEMOVE || editMode == RESIZING) 
      { setCursor(new Cursor(Cursor.MOVE_CURSOR)); } 
      else if (editMode == EDITING) 
      { setCursor(new Cursor(Cursor.HAND_CURSOR)); } 
    } 
    else 
    { setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); } 
  } 

  public void synthesiseConcurrentJava()
  { Statement code = module.methodStepCode(); 
    code.displayJava(null); 
  } 

  public void synthesiseSimulationJava()
  { Statement code = module.methodGeneralSequentialCode(); 
    code.displayJava(null); 
  } 

  public void checkStructure()
  { module.checkStructure(null); } 

  public void slice(String st, String att)
  { Vector sts = module.states; 
    State s = (State) VectorUtil.lookup(st,sts); 
    Vector v = new Vector(); 
    v.add(att); 
    StatemachineSlice ss = new StatemachineSlice(module,s,v);
    ss.computeSlice(); 
  }  

  public void inputEventSlice(String ev)
  { Vector trans = module.transitions; 
    Vector removed = new Vector(); 
    Vector removedVisuals = new Vector(); 

    java.util.Date date1 = new java.util.Date(); 
    long time1 = date1.getTime(); 

    for (int i = 0; i < trans.size(); i++) 
    { Transition tr = (Transition) trans.get(i); 
      Event ee = tr.getEvent(); 
      if (ee != null && ee.label.equals(ev))
      { removed.add(tr); } 
    } 
    System.out.println("Transitions removed: " + removed); 

    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.elementAt(i);
      if ((vd instanceof LineData) &&
          removed.contains(((LineData) vd).transition))
      { removedVisuals.add(vd); }
    }

    for (int j = 0; j < removed.size(); j++) 
    { Transition tt = (Transition) removed.get(j); 
      module.deleteTransition(tt); 
    } 

    for (int j = 0; j < removedVisuals.size(); j++) 
    { VisualData tt = (VisualData) removedVisuals.get(j); 
      visuals.remove(tt); 
    } 

    StatemachineSlice sslice = new StatemachineSlice(module,null,null); 
    module.setupOutgoingIncoming(); 
    sslice.computeReachability(); 
    sslice.removeUnreachableStates2(module.initial_state); 
    
    java.util.Date date2 = new java.util.Date(); 
    long time2 = date2.getTime(); 
    System.out.println("Time = " + (time2 - time1)); 
  } 

  public void outputEventSlice(Vector events)
  { Vector trs = module.transitions;
    for (int i = 0; i < trs.size(); i++)
    { Transition tt = (Transition) trs.get(i);
      tt.outputEventSlice(events);
    }
    tryStateMerge(); 
  }

  public void tryStateMerge()
  { boolean running = true; 
    while (running) 
    { StatemachineSlice ss = new StatemachineSlice(module,null,null); 
      running = ss.mergeStateGroup(); 
    } 
  } 
}

	

 
