/**
      * Classname : RequirementsArea
      * 
      * Version information : 1
      *
      * Date : 2016
      * 
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
import java.util.StringTokenizer; 
import java.io.*; 
import java.awt.print.*; 


class RequirementsArea extends JPanel 
    implements MouseListener, MouseMotionListener, KeyListener, Printable
{
    //these indicate which option the user has chosen from the menu
    public static final int INERT = -1; 
    public static final int SLINES = 0;
    public static final int POINTS = 1;
    public static final int EVENTS = 2;
    public static final int DLINES = 3;
    public static final int EDIT = 4; 
    public static final int LSTATE = 5; 
    public static final int EXECOCC = 6; 
    public static final int TANN = 7; 
    public static final int DANN = 8; 

    public static final int SOLID = 0; 
    public static final int DASHED = 1; 

    public static final int EDITING = 0; 
    public static final int DELETING = 1; 
    public static final int MOVING = 2; 
    public static final int GLUEMOVE = 3; 
    public static final int RESIZING = 4; 

    //Solid line -- could be made smaller for thinner line
    final static BasicStroke stroke = new BasicStroke(2.0f);
    
    RequirementsWin controller;
    UCDArea parent; 
    
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
  private RequirementEditDialog reqDialog; 
  private MessageEditDialog transDialog;   
  private StateEdtDialog sDialog; 
  private ExecutionEditDialog eDialog; 
  private TAEditDialog taDialog; 
  private DurationEditDialog durationDialog; 
  private EntityCreateDialog entDialog; 
  private ScenarioEditDialog scenarioDialog; 	

  Vector visuals = new Vector();  //holds all the points of the elements drawn
  Vector waypoints = new Vector(); 

  Vector lifelines = new Vector();   // UMLObject
  Vector messages = new Vector();   // Message
  Vector executionInstances = new Vector();  // ExecutionInstance
  Vector lifelineStates = new Vector();  // LifelineState
  Vector timeAnnotations = new Vector();  // TimeAnnotation
  Vector durationAnnotations = new Vector(); // DurationAnnotation

  private Vector types = new Vector(); 
  private Vector entities = new Vector();  
  private Vector requirements = new Vector();  // Requirement
  private Vector usecases = new Vector(); 

  private UMLObject selectedObject = null; 
  private VisualData selectedVisual = null; 
  private Message selectedMessage = null; 
  private LifelineState selectedState = null; 
  private TimeAnnotation selectedTimeAnnotation = null; 
  private ExecutionInstance selectedExecution = null;  

  private Requirement selectedRequirement = null; 

  private Vector glueset = new Vector(); // of GlueSet

  // These variables keep a count of all the items created 
  // They will be replaced with labels at a later stage
  private int rectcount = 0;
  private int linecount = 0;


 public RequirementsArea(RequirementsWin controller, UCDArea par, String s, Vector es)
 { this.controller = controller;
   parent = par; 
   entities = new Vector(); 
   entities.addAll(es); 
    
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

  public void shrink(double factor) 
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

  
  public void setVisuals(Vector vis) 
  { visuals = vis; } 

  public Vector getScenarios()
  { Vector res = new Vector(); 
    for (int i = 0; i < requirements.size(); i++) 
    { Requirement req = (Requirement) requirements.get(i); 
      res.addAll(req.getScenarios()); 
    }
    return res; 
  }

  public void disp_States()
  { for (int i = 0; i < requirements.size(); i++) 
    { System.out.println(requirements.get(i)); }
  }

  public void disp_Trans()
  { for (int i = 0; i < messages.size(); i++) 
    { Message mm = (Message) messages.get(i); 
      System.out.println(mm.displayMessage());
    }
  }

  public void dispLStates()
  { for (int i = 0; i < lifelineStates.size(); i++) 
    { LifelineState ei = (LifelineState) lifelineStates.get(i); 
      System.out.println(ei.display_ls()); 
    }
  } 

  public void dispExecutions()
  { for (int i = 0; i < executionInstances.size(); i++) 
    { ExecutionInstance ei = (ExecutionInstance) executionInstances.get(i); 
      System.out.println(ei.display_ei()); 
    }
  } 

  public void dispTimeAnnotations()
  { for (int i = 0; i < timeAnnotations.size(); i++) 
    { TimeAnnotation ta = (TimeAnnotation) timeAnnotations.get(i); 
      System.out.println(ta.display_ta()); 
    }
  } 

  public void dispDurationAnnotations()
  { for (int i = 0; i < durationAnnotations.size(); i++) 
    { DurationAnnotation da = (DurationAnnotation) durationAnnotations.get(i); 
      System.out.println(da.display_da()); 
    }
  } 

  // and time and duration annotations

  private LifelineData findLifeline(int x1, int x2, int y1, int y2) 
  { int xs = x1; 
    int xe = x2; 

    if (x1 <= x2) {} 
    else 
    { xs = x2; 
      xe = x1; 
    }

    for (int i=0; i < visuals.size(); i++)
    { if (visuals.elementAt(i) instanceof LifelineData)
      {	LifelineData rd = (LifelineData) visuals.elementAt(i);
	if (rd.isUnder(xs,xe,y1,y2))
	{ return rd; }
      } 
    } 
    return null; 
  } 

  private LifelineData findLifelines(int x1, int x2, int y) 
  { int xs = x1; 
    int xe = x2; 

    if (x1 <= x2) {} 
    else 
    { xs = x2; 
      xe = x1; 
    }

    for (int i=0; i < visuals.size(); i++)
    { if (visuals.elementAt(i) instanceof LifelineData)
      {	LifelineData rd = (LifelineData) visuals.elementAt(i);
	if (rd.xstart >= xs && rd.xstart <= xe && 
            y >= rd.ystart && y <= rd.yend)
	{ return rd; }
      } 
    } 
    return null; 
  } 

  public void find_src_targ(LineData line, Message strans)
  {
    for (int i=0; i < visuals.size(); i++)
      {
	if (visuals.elementAt(i) instanceof LifelineData)
	  {
	    LifelineData rd = (LifelineData) visuals.elementAt(i);
	    if (rd.isUnder(line.xstart,line.ystart))
	    {
		strans.setSource(rd.object);
		UMLObject st = strans.source;
		System.out.println(" source ==> " + st.label); 
	    }
	    if (rd.isUnder(line.xend, line.yend))
	    {
		strans.setTarget(rd.object);
		UMLObject ta = strans.target;
		System.out.println(" target ==> " + ta.label); 
          }
	  }
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

  public void findSelected(int x, int y)
  { boolean found = false; 

    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.elementAt(i);
      if (vd.isUnder(x,y) || vd.isUnderStart(x,y) || vd.isUnderEnd(x,y))
      { selectedVisual = vd;
        found = true; 
        /* if (vd instanceof LifelineData)
        { selectedObject = ((LifelineData) vd).object;
         
          System.out.println("Selected object: " + selectedObject); 
        }
        else */ 
        if (vd instanceof RectData)
        { selectedRequirement = (Requirement) ((RectData) vd).modelElement;
         
          System.out.println("Selected requirement: " + selectedRequirement); 
        }
       /* else if (vd instanceof LineData) 
        { selectedMessage = 
                    ((LineData) vd).message;
          
          System.out.println("Selected message: " + 
                                  selectedMessage); 
        }
        else if (vd instanceof TimeAnnotationData)
        { selectedTimeAnnotation = ((TimeAnnotationData) vd).ta; 
          System.out.println("Selected time annotation: " + 
                                  selectedTimeAnnotation); 
        } */  
        return; 
      } 
    } 
    if (!found)
    { System.out.println("No selection -- click on lifeline " + 
                         "or on name of message to select"); 
    } 
  }  // and for others

  private void resetSelected()
  { selectedVisual = null;
    // selectedObject = null;
    selectedRequirement = null;
    // selectedMessage = null; 
    // selectedTimeAnnotation = null; 
    glueset.clear(); 
  } 

  private void editSelected()
  { // if (selectedObject != null)
    // { editObject(selectedObject); } 
    if (selectedRequirement != null) 
    { editRequirement(selectedRequirement); } 
    // else if (selectedTimeAnnotation != null) 
    // { editTimeAnnotation(selectedTimeAnnotation); } 
    else 
    { System.out.println("No selection"); } 
    mode = INERT; 
    setAppropriateCursor(INERT); 
  } 

  private void deleteSelected() 
  { visuals.remove(selectedVisual); }

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


  public void saveDataToFile(String f) 
  { File file = new File("output/requirements.txt");
    Vector saved = new Vector(); 

    try
    { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
      for (int i = 0; i < visuals.size(); i++) 
      { VisualData vd = (VisualData) visuals.get(i); 
        if (vd instanceof RectData) 
        { RectData rd = (RectData) vd; 
          Requirement req = (Requirement) rd.modelElement; 
          out.println("Requirement:"); 
          out.println(rd.getx() + " " + rd.gety()); 
          out.println(req.saveData()); 
          out.println(); 
          out.println(); 
          out.println(); 
        } 
        else if (vd instanceof ReqLineData)
        { ReqLineData rld = (ReqLineData) vd; 
          out.println("SubgoalRelation:"); 
          out.println(rld.xstart + " " + rld.ystart + " " + rld.xend + " " + rld.yend); 
          out.println(); 
          out.println(); 
          out.println(); 
        } 
      } 
      out.close(); 
    } catch (Exception e) { } 
  } 
 

  public void loadDataFromFile(String f) 
  { BufferedReader br = null;
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File file = new File("output/requirements.txt");  /* default */ 

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException e)
    { System.out.println("File not found: " + file);
      return; 
    }

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("Reading failed.");
        return; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else if (s.equals("Requirement:"))
      { parseRequirement(br); } 
      else if (s.equals("SubgoalRelation:"))
      { parseRelation(br); }
      else if (s.equals("Scenario:"))
      { parseScenario(br); }
    }  
    try { br.close(); } catch(IOException e) { }
 }

  private void parseRequirement(BufferedReader br)
  { String line1 = "";  // x y 
    String line2 = "";  // name id
    String line3 = "";  // text
    String line4 = "";  // kind
    String line5 = "";  // scope
    Vector line1vals = new Vector();
    Vector line2vals = new Vector();

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    try { line2 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    StringTokenizer st2 =
      new StringTokenizer(line2);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    while (st2.hasMoreTokens())
    { line2vals.add(st2.nextToken()); }

    try { line3 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    try { line4 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    try { line5 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    if (line1vals.size() < 2) { return; } 

    String xs = (String) line1vals.get(0); 
    String ys = (String) line1vals.get(1); 

    int x = Integer.parseInt(xs); 
    int y = Integer.parseInt(ys); 

    if (line2vals.size() < 2) { return; } 

    String nme = (String) line2vals.get(0); 
    String id = (String) line2vals.get(1); 
    String txt = line3.trim(); 
    String kind = line4.trim(); 
    String scope = line5.trim(); 

    Requirement req = new Requirement(nme, id, txt, kind); 
    if ("true".equals(scope))
    { req.setScope("local"); } 

    RectData rd = new RectData(x,y,
                            getForeground(),
                            0,
                            rectcount);
    rectcount++;
    requirements.add(req); 
    visuals.addElement(rd); 
    rd.setModelElement(req);  // why not? 
    rd.setLabel(nme); 
    repaint();
  } 

  private void parseRelation(BufferedReader br)
  { String line1 = "";  // x1 y1 x2 y2 
    Vector line1vals = new Vector();

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading requirement details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    String x1s = (String) line1vals.get(0); 
    String y1s = (String) line1vals.get(1); 

    int x1 = Integer.parseInt(x1s); 
    int y1 = Integer.parseInt(y1s); 

    String x2s = (String) line1vals.get(2); 
    String y2s = (String) line1vals.get(3); 

    int x2 = Integer.parseInt(x2s); 
    int y2 = Integer.parseInt(y2s); 

    LineData dline = 
        new ReqLineData(x1,y1,x2,y2,linecount,SOLID);  // dashed for realizations
    visuals.addElement(dline);
    repaint();
  } 

  private void parseScenario(BufferedReader br)
  { String line1 = "";  // name requirement 
    Vector line1vals = new Vector();
    String line2 = "";  // informalDescription
    String line3 = "";  // semiformalDescription
    String line4 = "";  // constraint 

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading scenario details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    String nme = (String) line1vals.get(0); 
    String req = (String) line1vals.get(1); 

    try { line2 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading scenario details failed"); }

    try { line3 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading scenario details failed"); }

    try { line4 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading scenario details failed"); }

    Scenario sc = new Scenario(nme); 
    sc.setText(line2); 

    Compiler2 c2 = new Compiler2(); 
    c2.nospacelexicalanalysis(line3); 
    Sbvrse sbv = c2.parseSbvrse(); 
    sc.setSbvrse(sbv); 

    Compiler2 c3 = new Compiler2(); 
    c3.nospacelexicalanalysis(line4); 
    Expression con = c3.parse(); 
    Constraint fc = Constraint.getConstraint(con); 
    sc.setConstraint(fc); 

    Requirement r = (Requirement) ModelElement.lookupByName(req, requirements); 
    if (r != null) 
    { r.addScenario(sc); } 
  } 


  public void synthesiseUseCases()
  { types = parent.getTypes(); 
    entities = parent.getEntities(); 

    for (int i = 0; i < requirements.size(); i++) 
    { Requirement rq = (Requirement) requirements.get(i); 
      if (rq.isGlobal() && rq.hasScenarios())
      { UseCase uc = rq.generateUseCase(types,entities); 
        System.out.println("Created use case: " + uc.getName()); 
        System.out.println(uc.display()); 
        usecases.add(uc); 
      } 
    } 
    parent.addUseCases(usecases); 
  } 

  public boolean editMessage(Message t)
  { boolean res = true; 
    LineData ld = (LineData) selectedVisual; 
 
    if (transDialog == null) 
    { transDialog = new MessageEditDialog(controller); 
      transDialog.pack();

      transDialog.setLocationRelativeTo(controller); 
    }

    Vector evs = new Vector(); 

    if (t.target != null)
    { evs.addAll(t.target.getClassifier().getOperations()); 
      System.out.println(evs); 
    } 

    String st = ""; 
    String rt = ""; 
    if (t.ta1 != null) 
    { st = t.ta1 + ""; } 
    if (t.ta2 != null) 
    { rt = t.ta2 + ""; } 
    
    transDialog.setOldFields(evs,st,rt,t.isForall()); 
    transDialog.setVisible(true); 

    String t1 = transDialog.getT1(); 
    String t2 = transDialog.getT2(); 

    if (t1 != null && !t1.trim().equals(""))
    { TimeAnnotationData tad1 = 
        new TimeAnnotationData(ld.xstart,t.y1,Color.black,rectcount); 
      rectcount++;
      visuals.addElement(tad1);
      tad1.setx2(ld.xstart - 30); 
      TimeAnnotation tan1 = new TimeAnnotation(tad1.label);
      tan1.sety1(t.y1); 
      tan1.sety2(t.y1); 
      tan1.onObjects = t.source; 
      tan1.setTime(new BasicExpression(t1)); 
      // tan1.setx1(t.x1); 
      // tan1.setx2(t.x1 - 30); 
      timeAnnotations.add(tan1); 
      tad1.setTimeAnnotation(tan1);
      t.ta1 = tan1; 
      repaint(); 
    }

    if (t2 != null && !t2.trim().equals(""))
    { TimeAnnotationData tad2 = 
        new TimeAnnotationData(ld.xend,t.y2,Color.black,rectcount); 
      rectcount++;
      visuals.addElement(tad2);
      tad2.setx2(ld.xend + 30); 
      TimeAnnotation tan2 = new TimeAnnotation(tad2.label);
      tan2.sety1(t.y2); 
      tan2.sety2(t.y2); 
      tan2.onObjects = t.target; 
      tan2.setTime(new BasicExpression(t2)); 
      // tan1.setx1(t.x1); 
      // tan1.setx2(t.x1 - 30); 
      timeAnnotations.add(tan2); 
      tad2.setTimeAnnotation(tan2);
      t.ta2 = tan2; 
      repaint(); 
    }

    t.setStereotype(transDialog.getQuantifier()); 
    
    // parse the start and end times
    String txt = transDialog.getName();     
    if (txt != null && !txt.trim().equals(""))
    { String ttxt = txt.trim();    
      System.out.println("The name entered is valid.");
      BehaviouralFeature e = 
        (BehaviouralFeature) ModelElement.lookupByName(ttxt,evs); 
      if (e != null) 
      { t.setOperation(e); } 
      else 
      { System.out.println("No event with name " + ttxt); } 
      ld.setName(ttxt);   // ??
      return res; 
    }
    else
    { System.out.println("Null text"); 
      return false; 
    } 
  }

  public boolean editRequirement(Requirement r)
  { if (reqDialog == null)
    { reqDialog = new RequirementEditDialog(controller); 
      reqDialog.pack();
      reqDialog.setLocationRelativeTo(this);
    } 
    reqDialog.setOldFields(r.getName(), r.getText(), r.getKind(), r.getScope()); 
    reqDialog.setVisible(true); 

    String nme = reqDialog.getName(); 
         // JOptionPane.showInputDialog("Enter requirement name:");
    String txt = reqDialog.getText(); 
         // JOptionPane.showInputDialog("Enter requirement text:");
    String kind = reqDialog.getKind(); 
    String scope = reqDialog.getScope(); 
    r.setName(nme); 
    r.setText(txt); 
    r.setKind(kind);
    r.setScope(scope);  
    repaint(); 
    return true; 
  } 


  public boolean editExecution(ExecutionInstance t)
  { boolean res = true; 
    EORectData ld = (EORectData) selectedVisual; 
    int y0 = ld.sourcey; 

    if (eDialog == null) 
    { eDialog = new ExecutionEditDialog(controller); 
      eDialog.pack();

      eDialog.setLocationRelativeTo(controller); 
    }

    Vector evs = new Vector(); 
    evs.addAll(messages); 
    for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof LineData) 
      { LineData messd = (LineData) vd; 
        if (messd.message != null && messd.yend > y0)
        { evs.remove(messd.message); } 
        if (messd.message != null && messd.message.target != t.executesOn)
        { evs.remove(messd.message); }    
      } 
    } 

    // if (t.executesOn != null)
    // { evs.addAll(t.executesOn.getClassifier().getOperations()); 
    //   System.out.println(evs); 
    // } 

    eDialog.setOldFields(evs,"" + t.startTime,"" + t.endTime,"" + t.trigger); 
    eDialog.setVisible(true); 

    String txt = eDialog.getName();     
    if (txt != null && !txt.trim().equals(""))
    { String ttxt = txt.trim();    
      System.out.println("The name entered is valid.");
      Message e = 
        (Message) VectorUtil.lookup(ttxt,evs); 
      if (e != null) 
      { t.setMessage(e); } 
      else 
      { System.out.println("No event with name " + ttxt); } 
      ld.setName(ttxt);   // ??
      return res; 
    }
    else
    { System.out.println("Null text"); 
      return false; 
    } 
  }


  public void editState(State ss) 
  { if (sDialog == null)
    { sDialog = new StateEdtDialog(controller); 
      sDialog.pack();

      sDialog.setLocationRelativeTo(controller); 
    } 

    sDialog.setOldFields(ss.label,false); 
    sDialog.setVisible(true);

    // String txt = stateDialog.getName();
    // boolean ini = stateDialog.getInit(); 
    String inv = sDialog.getInv(); 
    // String entry = stateDialog.getEntry(); 

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
  } 

  public void editTimeAnnotation(TimeAnnotation ta) 
  { if (taDialog == null)
    { taDialog = new TAEditDialog(controller); 
      taDialog.pack();

      taDialog.setLocationRelativeTo(controller); 
    } 

    taDialog.setOldFields(ta.label,false); 
    taDialog.setVisible(true);

    boolean ini = taDialog.getInit();  // stereotype 
    String inv = taDialog.getInv(); 
    String nme = taDialog.getName(); 

    if (inv != null)
    { Compiler comp = new Compiler(); 
      comp.lexicalanalysis(inv);
      Expression pinv = comp.parse(); 
      if (pinv != null) 
      { ta.setCondition(pinv); } 
    } 

    if (nme != null)
    { Compiler comp = new Compiler(); 
      comp.lexicalanalysis(nme);
      Expression tanme = comp.parse(); 
      if (tanme != null) 
      { ta.setTime(tanme); } 
    } 

    if (ini)
    { ta.setStereotype("<<forall>>"); } 
    else 
    { ta.setStereotype("<<exists>>"); } 

  } 
  
  public void editScenario(Scenario sc) 
  { if (scenarioDialog == null)
    { scenarioDialog = new ScenarioEditDialog(controller); 
      scenarioDialog.pack();
      scenarioDialog.setLocationRelativeTo(this);
    } 

    scenarioDialog.setOldFields(sc.getName(), sc.informalDescription, 
                                sc.semiformalDescription + "", 
                                sc.formalDescription + ""); 
    scenarioDialog.setVisible(true); 

    String snme = scenarioDialog.getName(); 
    // String reqnme = JOptionPane.showInputDialog("Enter requirement name:");
    String text = scenarioDialog.getText(); 
    String text1 = scenarioDialog.getText1(); 
    String cons = scenarioDialog.getCons(); 
      
    sc.setText(text); 
    if (text1 != null && text1.trim().length() > 0)
    { Compiler2 comp2 = new Compiler2(); 
      comp2.nospacelexicalanalysis(text1); 
      Sbvrse ss = comp2.parseSbvrse(); 
      sc.setSbvrse(ss); 
    } 

    if (cons != null && cons.trim().length() > 0)
    { Compiler2 c3 = new Compiler2(); 
      c3.nospacelexicalanalysis(cons); 
      Expression con = c3.parse(); 
      Constraint fc = Constraint.getConstraint(con); 
      sc.setConstraint(fc); 
    } 
  } 

  public void editDurationAnnotation(DurationAnnotation da) 
  { if (durationDialog == null)
    { durationDialog = new DurationEditDialog(controller); 
      durationDialog.pack();

      durationDialog.setLocationRelativeTo(controller); 
    } 

    Vector sources = new Vector(); 
    Vector targets = new Vector(); 

    for (int i = 0; i < timeAnnotations.size(); i++)
    { TimeAnnotation ta = (TimeAnnotation) timeAnnotations.get(i); 
      if (ta.y1 <= da.y1 + 5 && da.y1 <= ta.y1 + 5) 
      { sources.add(ta); } 
      else if (ta.y1 <= da.y2 + 5 && da.y2 <= ta.y1 + 5)
      { targets.add(ta); } 
    } 

    durationDialog.setOldFields(sources,targets,da.lower + "",da.lower + ""); 
    durationDialog.setVisible(true);

    String t1 = durationDialog.getT1();   // lower
    String t2 = durationDialog.getT2();  // upper

    if (t1 != null)
    { Compiler comp = new Compiler(); 
      comp.lexicalanalysis(t1);
      Expression low = comp.parse(); 
      if (low != null) 
      { da.setLower(low); } 
    } 

    if (t2 != null)
    { Compiler comp = new Compiler(); 
      comp.lexicalanalysis(t2);
      Expression upp = comp.parse(); 
      if (upp != null) 
      { da.setUpper(upp); } 
    } 

    String ta1 = durationDialog.getTA1(); 
    String ta2 = durationDialog.getTA2(); 
    TimeAnnotation tastart = (TimeAnnotation) VectorUtil.lookup(ta1,sources);
    TimeAnnotation taend = (TimeAnnotation) VectorUtil.lookup(ta2,targets);
    if (tastart != null)
    { da.startTime = tastart; 
      da.y1 = tastart.y1; 
    }
    if (taend != null)
    { da.endTime = taend; 
      da.y2 = taend.y1; 
    }
    if (selectedVisual instanceof DurationAnnotationData) 
    { DurationAnnotationData dad = (DurationAnnotationData) selectedVisual; 
      if (dad != null && dad.da == da) 
      { dad.ystart = da.y1; 
        dad.yend = da.y2; 
      } 
    } 
    // and the DurationAnnotationData ystart, yend
  } 

  private void insertSorted(int y, int i, Vector tt, Vector events)
  { if (i >= events.size())
    { events.add(tt); } 
    else 
    { Vector vv = (Vector) events.get(i); 
      Integer y1 = (Integer) vv.get(1); 
      if (y <= y1.intValue())
      { events.add(i,tt); } 
      else 
      { insertSorted(y,i+1,tt,events); } 
    } 
  } 
    

  public void generateRAL()
  { for (int i = 0; i < lifelines.size(); i++) 
    { UMLObject ll = (UMLObject) lifelines.get(i);
      Vector events = new Vector(); 
 
      for (int j = 0; j < visuals.size(); j++) 
      { VisualData vd = (VisualData) visuals.get(j); 
        if (vd instanceof LineData)
        { LineData md = (LineData) vd; 
          Message m = md.message; 
          if (m.source == ll)
          { Vector tt = new Vector(); 
            tt.add(m.getSendEvent()); 
            tt.add(new Integer(m.y1)); 
            insertSorted(m.y1,0,tt,events); 
          } 
          if (m.target == ll)
          { Vector tt = new Vector(); 
            tt.add(m.getReceiveEvent()); 
            tt.add(new Integer(m.y2)); 
            insertSorted(m.y2,0,tt,events); 
          }
        }
        else if (vd instanceof EORectData)
        { ExecutionInstance ex = ((EORectData) vd).ei; 
          if (ex.executesOn == ll)
          { Vector tt = new Vector(); 
            tt.add(ex.getStartEvent()); 
            tt.add(new Integer(ex.y1)); 
            insertSorted(ex.y1,0,tt,events); 
            Vector ss = new Vector(); 
            ss.add(ex.getEndEvent()); 
            ss.add(new Integer(ex.y2)); 
            insertSorted(ex.y2,0,ss,events); 
          }
        } 
        else if (vd instanceof TimeAnnotationData)
        { TimeAnnotation tad = ((TimeAnnotationData) vd).ta; 
          if (tad.onObjects == ll)
          { Vector yy = new Vector(); 
            yy.add(tad.getTime()); 
            yy.add(new Integer(tad.y1)); 
            insertSorted(tad.y1,0,yy,events); 
          } 
        }         
      }
      for (int p = 0; p < events.size(); p++) 
      { Vector pp = (Vector) events.get(p); 
        if (p+1 < events.size())
        { Vector pp2 = (Vector) events.get(p+1); 
          Integer i1 = (Integer) pp.get(1); 
          Integer i2 = (Integer) pp2.get(1); 
          if (i1.intValue() < i2.intValue())
          { System.out.println(pp.get(0) + " < " + pp2.get(0)); } 
          else if (i1.intValue() == i2.intValue()) 
          { System.out.println(pp.get(0) + " = " + pp2.get(0)); } 
          else 
          { System.out.println(pp.get(0) + " <= " + pp2.get(0)); } 
        } 
      }  
    } 

    for (int i = 0; i < messages.size(); i++) 
    { Message m = (Message) messages.get(i); 
      System.out.println(m.generateRAL()); 
    } 

    for (int i = 0; i < executionInstances.size(); i++) 
    { ExecutionInstance ei = (ExecutionInstance) executionInstances.get(i); 
      System.out.println(ei.generateRAL()); 
    } 

    for (int i = 0; i < lifelineStates.size(); i++) 
    { LifelineState ls = (LifelineState) lifelineStates.get(i); 
      System.out.println(ls.generateRAL()); 
    } 

    for (int k = 0; k < timeAnnotations.size(); k++) 
    { TimeAnnotation ta = (TimeAnnotation) timeAnnotations.get(k); 
      System.out.println(ta.generateRAL()); 
    } 

    for (int k = 0; k < durationAnnotations.size(); k++) 
    { DurationAnnotation da = (DurationAnnotation) durationAnnotations.get(k); 
      System.out.println(da.generateRAL()); 
    } 
  }

  public void setDrawMode(int mode) 
  { // oldMode = this.mode; 
    switch (mode) {
    case SLINES:
    case DLINES:
    case POINTS:
      setAppropriateCursor(mode); 
      this.mode = mode;
      break; 
    case LSTATE:
      setAppropriateCursor(POINTS);
      this.mode = mode;
      break;  
    case TANN:
      setAppropriateCursor(POINTS);
      this.mode = mode;
      break;  
    case DANN:
      setAppropriateCursor(POINTS);
      this.mode = mode;
      break;  
    case EXECOCC:
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
      {	  // The text is valid - it is not null 
	  // and has passed the other test as well.
	System.out.println("The event entered is valid.");
        Event newevent = new Event(ttxt); 
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
    else if (c == 'd') { moveAllUp(20); }
    else if (c == 'l') { moveAllRight(20); }
    else if (c == 'r') { moveAllLeft(20); } 
    else if (firstpress) 
    { System.out.println("Adding waypoint at " + x2 + " " + y2); 
      waypoints.add(new Point(x2,y2)); 
    } 
    System.out.println(e);
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
  { } 

  public void mousePressed(MouseEvent me)
  { int x = me.getX(); 
    int y = me.getY(); 
    x1 = x; 
    y1 = y; 
    x2 = x;  // ??
    y2 = y;  // ??
   
    boolean is_bigger = false;
    System.out.println("Mouse pressed at " + x + " " + y); 

    switch (mode) {
    case SLINES:
      System.out.println("Creating subgoal relation");
      waypoints.clear(); 
      firstpress = true;	
      break;
    case DLINES:
      System.out.println("This is DLINES");
      firstpress = true;	
      break;
    case POINTS:
      System.out.println("Creating requirement");
      // is_bigger = changed(x,y,50,50);
      RectData rd = new RectData(x,y,
                            getForeground(),
                            0,
                            rectcount);
      rectcount++;
      if (reqDialog == null)
      { reqDialog = new RequirementEditDialog(controller); 
        reqDialog.pack();
        reqDialog.setLocationRelativeTo(this);
      } 
      reqDialog.setVisible(true); 

      String nme = reqDialog.getName(); 
         // JOptionPane.showInputDialog("Enter requirement name:");
      String txt = reqDialog.getText(); 
         // JOptionPane.showInputDialog("Enter requirement text:");
      String kind = reqDialog.getKind(); 
      String scope = reqDialog.getScope(); 
      Requirement req = new Requirement(nme,nme,txt,kind);
      req.setScope(scope); 
      requirements.add(req); 
      visuals.addElement(rd); 
      rd.setModelElement(req);  // why not? 
      rd.setLabel(nme); 
      x1 = x;
      y1 = y;
      repaint();
      mode = INERT;
      break;
    case TANN:
      System.out.println("Creating scenario");
      if (scenarioDialog == null)
      { scenarioDialog = new ScenarioEditDialog(controller); 
        scenarioDialog.pack();
        scenarioDialog.setLocationRelativeTo(this);
      } 
      scenarioDialog.setVisible(true); 

      String snme = scenarioDialog.getName(); 
      String reqnme = JOptionPane.showInputDialog("Enter requirement name:");
      String text = scenarioDialog.getText(); 
      String text1 = scenarioDialog.getText1(); 
      String cons = scenarioDialog.getCons(); 
      
      Requirement r = (Requirement) ModelElement.lookupByName(reqnme, requirements);
      Scenario scen = new Scenario(snme); 
      scen.setText(text); 
      Compiler2 comp2 = new Compiler2(); 
      comp2.nospacelexicalanalysis(text1); 
      Sbvrse ss = comp2.parseSbvrse(); 
      scen.setSbvrse(ss); 

      if (r != null) 
      { r.addScenario(scen); } 
      else 
      { System.err.println("No requirement with name: " + reqnme); } 

      x1 = x;
      y1 = y;
      repaint();
      mode = INERT;
      repaint();
      break;
    case DANN:
      repaint();
      break;

    case LSTATE:
      break;
    case EXECOCC:
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
      LineData dline = 
        new ReqLineData(x1,y1,x,y,linecount,SOLID);  // dashed for realizations
      // Flow flw2 = new Flow("f" + linecount); 
      // find_src_targ(dline,flw2); 
      // Generalisation gen = defineGeneralisation(flw2); 
      // if (gen != null) 
      // { linecount++;
      //   dline.setFlow(flw2); 
      visuals.addElement(dline);
      //   generalisations.add(gen); 
      //   dline.setModelElement(gen); 
      //   dline.setWaypoints((Vector) ((Vector) waypoints).clone()); 
      // } 
      firstpress = false;
      mode = INERT; 
      break;
    case DLINES:  
      mode = INERT; 
      repaint(); 
      break; 
    case LSTATE:
      mode = INERT; 
      // selectedVisual = null; 
      repaint(); 
      break; 
    case EXECOCC:
      mode = INERT; 
      // selectedVisual = null; 
      repaint(); 
      break; 
    case POINTS:
      mode = INERT; 
      // selectedVisual = null; 
      repaint(); 
      break; 
    case TANN:
      mode = INERT; 
      // selectedVisual = null; 
      repaint(); 
      break; 
    case DANN:
      mode = INERT; 
      // selectedVisual = null; 
      repaint(); 
      break; 

    case EDIT: 
      // updateModule(); 
      // resetSelected(); 
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
    x2 = x;
    y2 = y;	
    break;
  case DLINES:  
    x2 = x;
    y2 = y;	
    break;
  case POINTS:
    break;
  case TANN:  
    break;   
  case DANN:  
    break;   
  case LSTATE:
    break; 
  case EXECOCC:
    break; 
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
      { if (waypoints.size() == 0)
        { g2.drawLine(x1,y1, x2, y2); }
        else 
        { Point p1 = (Point) waypoints.get(0); 
          g2.drawLine(x1,y1,p1.x,p1.y); 
          for (int i = 1; i < waypoints.size(); i++) 
          { Point p2 = (Point) waypoints.get(i);
            g2.drawLine(p1.x,p1.y,p2.x,p2.y); 
            p1 = p2; 
          }
          g2.drawLine(p1.x,p1.y,x2,y2); 
        } 
      }
    } 
  }
  
  public void paintComponent(Graphics g) 
  { super.paintComponent(g);  //clear the panel
    Graphics2D g2 = (Graphics2D) g;
    drawShapes(g2); 
  } 


  public void synthesiseB() 
  { } 

  private String bPromotes()
  { return ""; 
  } 
 
  private void test()  // synthB() 
  {   } 

  private void synthBMult()
  {   }


  public void testImp()
  { 
  }

  public void testImp(PrintWriter out)
  { 
  }


  public void synthMultImp()
  {   }

  public void synthMultImp(PrintWriter out)
  {  }


  private String javaInherits()
  { return ""; } 

  public void synthesiseJava()
  { } 

  public void synthesiseBCode()
  { } 

  public void synthesiseSmv() 
  {   } 

  private void outputJavaCode(PrintWriter out)
  {   }

  private void outputBCode(PrintWriter out)
  { }

  // Print multiple B code if module.getMultiplicity() > 1
  private void outputBSingleInstance(PrintWriter out) 
  {   }

  private void outputBMultipleInstances(PrintWriter out)
  {   }



  private void setAppropriateCursor(int mode) 
  { if (mode == EDIT) 
    { if (editMode == DELETING) 
      { setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); } 
      else if (editMode == MOVING || editMode == GLUEMOVE || editMode == 
RESIZING) 
      { setCursor(new Cursor(Cursor.MOVE_CURSOR)); } 
      else if (editMode == EDITING) 
      { setCursor(new Cursor(Cursor.HAND_CURSOR)); } 
    } 
    else 
    { setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); } 
  } 


}

	

 







