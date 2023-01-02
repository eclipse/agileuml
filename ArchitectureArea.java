/**
      * Classname : ArchitectureArea
      * 
      * Version information : 1
      *
      * Date : 2022
      * 
*/
/******************************
* Copyright (c) 2003--2022 Kevin Lano
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


class ArchitectureArea extends JPanel 
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
    
    // ArchitectureWin controller;
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
  private Vector components = new Vector();  // ArchComponent
  private Vector usecases = new Vector(); 

  private UMLObject selectedObject = null; 
  private VisualData selectedVisual = null; 
  private Message selectedMessage = null; 
  private LifelineState selectedState = null; 
  private TimeAnnotation selectedTimeAnnotation = null; 
  private ExecutionInstance selectedExecution = null;  

  private ModelElement selectedInterface = null; 
  private ArchComponent selectedComponent = null; 

  private Vector glueset = new Vector(); // of GlueSet

  // These variables keep a count of all the items created 
  // They will be replaced with labels at a later stage
  private int rectcount = 0;
  private int linecount = 0;


 public ArchitectureArea(Object controller, 
                         UCDArea par, String s, Vector es)
 { // this.controller = controller;
   parent = par; 
   entities = new Vector(); 
   if (es != null) 
   { entities.addAll(es); } 
    
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

  public void disp_States()
  { for (int i = 0; i < components.size(); i++) 
    { System.out.println(components.get(i)); }
  }

  public void disp_Trans()
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof ProvidedInterfaceLineData)
      { System.out.println(">> Provided interface: " + vd.getModelElement()); } 
      else if (vd instanceof RequiredInterfaceLineData)
      { System.out.println(">> Required interface: " + vd.getModelElement()); } 
    }
  }


  // and time and duration annotations


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

  public void updateModel()
  { // Scan the visuals, adding/removing component interface
    // and connections based on the visual layout. 

    for (int i=0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i); 

      if (vd instanceof RectData)
      { // component
        RectData rd = (RectData) vd;
        ArchComponent comp = 
          (ArchComponent) rd.getModelElement();
        if (comp == null) 
        { continue; }  
        comp.clearInterfaces(); 
        for (int j = 0; j < visuals.size(); j++) 
        { VisualData ld = (VisualData) visuals.get(j); 
          if (ld instanceof ProvidedInterfaceLineData) 
          { ProvidedInterfaceLineData line = 
              (ProvidedInterfaceLineData) ld; 
            if (rd.isUnder(line.xstart,line.ystart))
            { comp.addProvidedInterface((Entity) 
                                line.getModelElement()); 
            } 
          }
          else if (ld instanceof RequiredInterfaceLineData) 
          { RequiredInterfaceLineData line = 
              (RequiredInterfaceLineData) ld; 
            if (rd.isUnder(line.xstart,line.ystart))
            { comp.addRequiredInterface((Entity) 
                                line.getModelElement()); 
            } 
          } 
        } 
      }
      else if (vd instanceof LineData) 
      { LineData ld = (LineData) vd; 
        if (ld instanceof ProvidedInterfaceLineData) 
        { findConnection((ProvidedInterfaceLineData) ld, 
                         ld.xend, ld.yend); 
        } 
        else if (ld instanceof RequiredInterfaceLineData) 
        { findConnection((RequiredInterfaceLineData) ld, 
                         ld.xend, ld.yend); 
        }  
      } 
    }
  } 

  public ArchComponent find_src(ProvidedInterfaceLineData line)
  { ModelElement ent = line.getModelElement();
    String ename = ent.getName(); 
 
    for (int i=0; i < visuals.size(); i++)
    {
      if (visuals.elementAt(i) instanceof RectData)
      {
        RectData rd = (RectData) visuals.elementAt(i);
        if (rd.isUnder(line.xstart,line.ystart) && 
            rd.modelElement instanceof ArchComponent)
        {
	     System.out.println(ename + " is a provided interface of ==> " + rd.label);
          ArchComponent comp = 
                (ArchComponent) rd.modelElement;
          comp.addProvidedInterface((Entity) ent); 
          return comp;   
        }
      }
    }
    return null; 
  }

  public ArchComponent find_src(RequiredInterfaceLineData line)
  { ModelElement ent = line.getModelElement();
    String ename = ent.getName(); 
 
    for (int i=0; i < visuals.size(); i++)
    {
      if (visuals.elementAt(i) instanceof RectData)
      {
        RectData rd = (RectData) visuals.elementAt(i);
        if (rd.isUnder(line.xstart,line.ystart) && 
            rd.modelElement instanceof ArchComponent)
        {
	     System.out.println(ename + " is a required interface of ==> " + rd.label);
          ArchComponent comp = 
                (ArchComponent) rd.modelElement;
          comp.addRequiredInterface((Entity) ent); 
          return comp;   
        }
      }
    }
    return null; 
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
        if (vd instanceof RectData)
        { selectedComponent = 
            (ArchComponent) ((RectData) vd).modelElement;
         
          System.out.println(">>> Selected component: " + selectedComponent); 
        }
        else if (vd instanceof LineData) 
        { selectedInterface = 
                    ((LineData) vd).modelElement;
          
          System.out.println("Selected interface: " + 
                                  selectedInterface); 
        }
        return; 
      } 
    } 
    if (!found)
    { System.out.println("!! No selection -- click on end of a line or in a component area to select"); 
    } 
  }  // and for others

  public void findConnection(VisualData self, int x, int y)
  { boolean found = false; 

    ModelElement me = self.getModelElement(); 

    for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.elementAt(i);
      if (vd instanceof LineData && vd.isNearEnd(x,y) && 
          vd != self)
      { // selectedVisual = vd;
        ModelElement connectedInterface = 
                    ((LineData) vd).modelElement;
          
        if (self instanceof ProvidedInterfaceLineData && 
            vd instanceof RequiredInterfaceLineData)
        { System.out.println(
              ">>> Assembly connection between " + me + 
              " and: " + 
                             connectedInterface);
          if (connectedInterface != null && 
              me != null && 
              connectedInterface.getName().equals(
                me.getName()))
          { System.out.println(">>> Valid connection"); } 
          else  
          { System.out.println(">>> All operations of " + 
               connectedInterface + " must be present in: " + 
               me);
            boolean isImp = 
              ((Entity) me).checkIfDefactoImplementation(
                                (Entity) connectedInterface); 
            if (isImp) 
            { System.out.println(">>> This is a valid assembly connection."); } 
            else 
            { System.out.println("!! This is not a valid assembly connection!"); } 
          } 
          found = true; 
          return; 
        }
        else if (self instanceof RequiredInterfaceLineData && 
            vd instanceof ProvidedInterfaceLineData)
        { System.out.println(
              ">>> Assembly connection between " + me + 
              " and: " + 
                             connectedInterface); 
          if (connectedInterface != null && 
              me != null && 
              connectedInterface.getName().equals(
                me.getName()))
          { System.out.println(">>> Valid connection"); } 
          else  
          { System.out.println(">>> All operations of " + 
               me + " must be present in: " + 
               connectedInterface);
            boolean isImp = 
     ((Entity) connectedInterface).checkIfDefactoImplementation(
                                                 (Entity) me);
            if (isImp) 
            { System.out.println(">>> This is a valid assembly connection."); } 
            else 
            { System.out.println("!! This is not a valid assembly connection!"); }
          } 
          found = true; 
          return; 
        }
      }  
    } 
    if (!found)
    { System.out.println(">> No connection found for " + me); } 
  }  // and for others

  private void resetSelected()
  { selectedVisual = null;
    // selectedObject = null;
    selectedComponent = null;
    // selectedMessage = null; 
    // selectedTimeAnnotation = null; 
    glueset.clear(); 
  } 

  private void editSelected()
  { // if (selectedObject != null)
    // { editObject(selectedObject); } 
    if (selectedComponent != null) 
    { editComponent(selectedComponent); } 
    else if (selectedInterface != null) 
    { editInterface(selectedInterface); } 
    else 
    { System.out.println("!! No selection"); } 
    mode = INERT; 
    setAppropriateCursor(INERT); 
    repaint(); 
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


  public void saveModelDataToFile(String f) 
  { File file = new File("output/requirementsmodel.txt");
    Vector saved = new Vector(); 

    try
    { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
      for (int i = 0; i < visuals.size(); i++) 
      { VisualData vd = (VisualData) visuals.get(i); 
        if (vd instanceof RectData) 
        { RectData rd = (RectData) vd; 
          ArchComponent req = (ArchComponent) rd.modelElement; 
          req.saveModelData(out); 
        } 
        else if (vd instanceof ReqLineData)
        { ReqLineData rld = (ReqLineData) vd; 
          // out.println("SubgoalRelation:");
          RectData rstart = findRectDataAt(rld.xstart,rld.ystart); 
          RectData rend = findRectDataAt(rld.xend,rld.yend);
          if (rstart != null && rend != null && rstart.modelElement != null && rend.modelElement != null)  
          { out.println(rstart.modelElement.getName() + " : " + rend.modelElement.getName() + ".subgoals"); 
            out.println(); 
          } 
        } 
      } 
      out.close(); 
    } catch (Exception e) { } 
  } 

  public void saveDataToFile(String f) 
  { File file = new File("output/architecture.txt");
    Vector saved = new Vector(); 
    try
    { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
      for (int i = 0; i < visuals.size(); i++) 
      { VisualData vd = (VisualData) visuals.get(i); 
        if (vd instanceof RectData) 
        { RectData rd = (RectData) vd; 
          ArchComponent req = (ArchComponent) rd.modelElement; 
          out.println("Component:"); 
          out.println(rd.getx() + " " + rd.gety()); 
          out.println(req.saveData()); 
          out.println(); 
          out.println(); 
          out.println(); 
        } 
        else if (vd instanceof ProvidedInterfaceLineData)
        { ProvidedInterfaceLineData rld = (ProvidedInterfaceLineData) vd; 
          out.println("ProvidedInterface:"); 
          out.println(rld.xstart + " " + rld.ystart + " " + rld.xend + " " + rld.yend); 
          ModelElement elem = rld.getModelElement(); 
          out.println(elem.getName()); 
          out.println(); 
          out.println(); 
        } 
        else if (vd instanceof RequiredInterfaceLineData)
        { RequiredInterfaceLineData rld = (RequiredInterfaceLineData) vd; 
          out.println("RequiredInterface:"); 
          out.println(rld.xstart + " " + rld.ystart + " " + rld.xend + " " + rld.yend); 
          ModelElement elem = rld.getModelElement(); 
          out.println(elem.getName()); 
          out.println(); 
          out.println(); 
        } 
      } 
      out.close(); 
    } catch (Exception e) { } 
  } 
 
  public RectData findRectDataAt(int xx, int yy) 
  { for (int i = 0; i < visuals.size(); i++) 
    { VisualData vd = (VisualData) visuals.get(i); 
      if (vd instanceof RectData) 
      { RectData rd = (RectData) vd; 
        if (rd.isUnder(xx,yy))
        { return rd; }
      } 
    } 
    return null;
  } 

  public void loadDataFromFile(String f) 
  { BufferedReader br = null;
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File file = new File("output/architecture.txt");

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException e)
    { System.out.println("!! File not found: " + file);
      return; 
    }

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("!! Reading failed.");
        return; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else if (s.equals("Component:"))
      { parseComponent(br); } 
      else if (s.equals("ProvidedInterface:"))
      { parseProvidedInterface(br); }
      else if (s.equals("RequiredInterface:"))
      { parseRequiredInterface(br); }
    }  
    try { br.close(); } catch(IOException e) { }
 }

  private void parseComponent(BufferedReader br)
  { String line1 = "";  // x y 
    String line2 = "";  // name id
    String line3 = "";  // text
    String line4 = "";  // kind
    String line5 = "";  // scope
    Vector line1vals = new Vector();
    Vector line2vals = new Vector();

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading component details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    try { line2 = br.readLine(); }
    catch (IOException e)
    { System.err.println("Reading component details failed"); }

    StringTokenizer st2 =
      new StringTokenizer(line2);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    while (st2.hasMoreTokens())
    { line2vals.add(st2.nextToken()); }

    if (line1vals.size() < 2) { return; } 

    String xs = (String) line1vals.get(0); 
    String ys = (String) line1vals.get(1); 

    int x = Integer.parseInt(xs); 
    int y = Integer.parseInt(ys); 

    if (line2vals.size() < 1) { return; } 

    String nme = (String) line2vals.get(0); 
    // String id = (String) line2vals.get(1); 
    // String txt = line3.trim(); 
    // String kind = line4.trim(); 
    // String scope = line5.trim(); 

    ArchComponent req = new ArchComponent(nme); 
    // if ("true".equals(scope))
    // { req.setScope("local"); } 

    RectData rd = new RectData(x,y,
                            getForeground(),
                            0,
                            rectcount);
    rectcount++;
    components.add(req); 
    visuals.addElement(rd); 
    rd.setModelElement(req);  // why not? 
    rd.setLabel(nme); 
    repaint();
  } 

  private void parseProvidedInterface(BufferedReader br)
  { String line1 = "";  // x1 y1 x2 y2 
    Vector line1vals = new Vector();
    String line2 = "";  // Name 
    Vector line2vals = new Vector();

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("!! Reading interface details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    if (line1vals.size() < 4) 
    { System.err.println("!! Incomplete interface details"); 
      return; 
    }

    String x1s = (String) line1vals.get(0); 
    String y1s = (String) line1vals.get(1); 

    int x1 = Integer.parseInt(x1s); 
    int y1 = Integer.parseInt(y1s); 

    String x2s = (String) line1vals.get(2); 
    String y2s = (String) line1vals.get(3); 

    int x2 = Integer.parseInt(x2s); 
    int y2 = Integer.parseInt(y2s); 

    ProvidedInterfaceLineData dline = 
      new ProvidedInterfaceLineData(
        x1,y1,x2,y2,linecount,SOLID); 
    visuals.addElement(dline);

    try { line2 = br.readLine(); }
    catch (IOException e)
    { System.err.println("!! Reading interface details failed"); }

    StringTokenizer st2 =
      new StringTokenizer(line2);

    while (st2.hasMoreTokens())
    { line2vals.add(st2.nextToken()); }

    if (line2vals.size() < 1) { return; } 

    String nme = (String) line2vals.get(0); 

    Entity ent = (Entity) ModelElement.lookupByName(nme, entities); 
    if (ent == null) 
    { ent = new Entity(nme); 
      ent.setInterface(true); 
      entities.add(ent); 
    } 
    findConnection(dline,x2,y2); 
    dline.setModelElement(ent); 
    find_src(dline); 
    
    repaint();
  } 

  private void parseRequiredInterface(BufferedReader br)
  { String line1 = "";  // x1 y1 x2 y2 
    Vector line1vals = new Vector();
    String line2 = "";  // Name 
    Vector line2vals = new Vector();

    try { line1 = br.readLine(); }
    catch (IOException e)
    { System.err.println("!! Reading interface details failed"); }

    StringTokenizer st1 =
      new StringTokenizer(line1);

    while (st1.hasMoreTokens())
    { line1vals.add(st1.nextToken()); }

    if (line1vals.size() < 4) 
    { System.err.println("!! Incomplete interface details"); 
      return; 
    }

    String x1s = (String) line1vals.get(0); 
    String y1s = (String) line1vals.get(1); 

    int x1 = Integer.parseInt(x1s); 
    int y1 = Integer.parseInt(y1s); 

    String x2s = (String) line1vals.get(2); 
    String y2s = (String) line1vals.get(3); 

    int x2 = Integer.parseInt(x2s); 
    int y2 = Integer.parseInt(y2s); 

    RequiredInterfaceLineData dline = 
      new RequiredInterfaceLineData(
          x1,y1,x2,y2,linecount,SOLID);
    try { line2 = br.readLine(); }
    catch (IOException e)
    { System.err.println("!! Reading interface details failed"); }

    StringTokenizer st2 =
      new StringTokenizer(line2);

    while (st2.hasMoreTokens())
    { line2vals.add(st2.nextToken()); }

    if (line2vals.size() < 1) { return; } 

    String nme = (String) line2vals.get(0); 

    Entity ent = (Entity) ModelElement.lookupByName(nme, entities); 
    if (ent == null) 
    { ent = new Entity(nme); 
      ent.setInterface(true); 
      entities.add(ent); 
    } 
    dline.setModelElement(ent); 
    visuals.addElement(dline);
    find_src(dline); 
    findConnection(dline,x2,y2); 
    repaint();
  } 



  public void synthesiseJava()
  { for (int i = 0; i < components.size(); i++) 
    { ArchComponent comp = (ArchComponent) components.get(i); 
      String compName = comp.getName();
      String lcname = compName.toLowerCase(); 
      String interfacesDefinitions = ""; 
      Vector pinterfaces = comp.getProvidedInterfaces();
      for (int j = 0; j < pinterfaces.size(); j++) 
      { Entity pint = (Entity) pinterfaces.get(j); 
        interfacesDefinitions = 
          interfacesDefinitions + 
          "/* Interface specification: */\n" + 
          pint.getKM3() + "\n\n"; 
      } 

      Vector rinterfaces = comp.getRequiredInterfaces();
      for (int j = 0; j < rinterfaces.size(); j++) 
      { Entity rint = (Entity) rinterfaces.get(j); 
        interfacesDefinitions = 
          interfacesDefinitions + 
          "/* Interface specification: */\n" + 
          rint.getKM3() + "\n\n"; 
      } 

      String res = "package " + lcname + ";\n\n" + 
                   interfacesDefinitions + 
                   "class " + compName; 

      if (pinterfaces.size() > 0) 
      { res = res + " implements "; }  
      for (int j = 0; j < pinterfaces.size(); j++) 
      { Entity pint = (Entity) pinterfaces.get(j); 
        res = res + pint.getName(); 
        if (j < pinterfaces.size() - 1) 
        { res = res + ", "; } 
      } 

      res = res + "\n{\n"; 

      String constr = "  public " + compName + "("; 
      String assignments = ""; 

      for (int j = 0; j < rinterfaces.size(); j++) 
      { Entity rint = (Entity) rinterfaces.get(j); 
        String rname = rint.getName();
        String rx = rname.toLowerCase() + "_x"; 
        res = res + "  " + rname + " " + rx + ";\n";
        constr = constr + rname + " " + rx; 
        if (j < rinterfaces.size()-1)
        { constr = constr + ", "; }  
        assignments = assignments + "    this." + rx + " = " + rx + ";\n";   
      } 

      constr = constr + ") {\n" + assignments + "  }\n"; 
      res = res + "\n" + constr + "}\n"; 
      System.out.println(res); 
      System.out.println(); 
    } 
  } 


  public boolean editComponent(ArchComponent r)
  { 
    String nme = 
      JOptionPane.showInputDialog("Enter component name:");
    r.setName(nme); 
    repaint(); 
    return true; 
  } 

  public boolean editInterface(ModelElement r)
  { 
    String nme = 
      JOptionPane.showInputDialog("Enter interface name:");
    r.setName(nme); 
    repaint(); 
    return true; 
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
      System.out.println(">> Creating connector");
      waypoints.clear(); 
      firstpress = true;	
      break;
    case DLINES:
      System.out.println("This is DLINES");
      firstpress = true;	
      break;
    case POINTS:
      System.out.println(">> Creating component");
      // is_bigger = changed(x,y,50,50);
      RectData rd = new RectData(x,y,
                            getForeground(),
                            0,
                            rectcount);
      rectcount++;

      String nme = 
        JOptionPane.showInputDialog("Enter component name:");
      ArchComponent req = new ArchComponent(nme);
      components.add(req); 
      visuals.addElement(rd); 
      rd.setModelElement(req);  // why not? 
      rd.setLabel(nme); 
      x1 = x;
      y1 = y;
      repaint();
      mode = INERT;
      break;
   /* case TANN:
      System.out.println(">> Creating provided interface");
      if (scenarioDialog == null)
      { scenarioDialog = new ScenarioEditDialog(controller); 
        scenarioDialog.pack();
        scenarioDialog.setLocationRelativeTo(this);
      } 
      scenarioDialog.setVisible(true); 

      String snme = scenarioDialog.getName(); 
      String reqnme = JOptionPane.showInputDialog(">> Enter component name:");
      String text = scenarioDialog.getText(); 
      String text1 = scenarioDialog.getText1(); 
      String cons = scenarioDialog.getCons(); 
      
      ArchComponent r = 
        (ArchComponent) ModelElement.lookupByName(
                                       reqnme, components);
      Scenario scen = new Scenario(snme); 
      scen.setText(text); 
      Compiler2 comp2 = new Compiler2(); 
      comp2.nospacelexicalanalysis(text1); 
      // Sbvrse ss = comp2.parseSbvrse(); 
      // scen.setSbvrse(ss); 

      // if (r != null) 
      // { r.addScenario(scen); } 
      // else 
      // { System.err.println(">> No component with name: " + reqnme); } 

      x1 = x;
      y1 = y;
      repaint();
      mode = INERT;
      repaint();
      break; */ 
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
      { System.out.println(">> Drag cursor to resize element"); } 

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
    System.out.println(">> Mouse released at " + x + " " + y); 
    
    switch (mode) {
    case SLINES:  
      ProvidedInterfaceLineData sline = 
        new ProvidedInterfaceLineData(
                        x1,y1,x,y,linecount,SOLID); 
      // Flow flw2 = new Flow("f" + linecount); 
      // find_src_targ(dline,flw2); 
      // Generalisation gen = defineGeneralisation(flw2); 
      // if (gen != null) 
      // { linecount++;
      //   dline.setFlow(flw2); 
      visuals.addElement(sline);
      String jname = 
        JOptionPane.showInputDialog("Enter provided interface name:");
      Entity pint = 
        (Entity) ModelElement.lookupByName(jname,entities); 
      if (pint == null) 
      { System.err.println("! Interface " + jname + " does not exist, it will be added to the class diagram."); 
        pint = new Entity(jname); 
        pint.setInterface(true); 
        int xx = (entities.size() + 1)*100; 
        int yy = 200; 
        if (parent != null) 
        { parent.addEntity(pint,xx,yy); } 
        entities.add(pint); 
      } 
      else if (pint.isInterface()) { } 
      else 
      { System.err.println("!! This is not an interface and cannot be used as one."); } 

      sline.setModelElement(pint); 
      //   generalisations.add(gen); 
      //   dline.setModelElement(gen); 
      //   dline.setWaypoints((Vector) ((Vector) waypoints).clone()); 
      // } 
      find_src(sline); 
      findConnection(sline,x,y); 

      firstpress = false;
      mode = INERT; 
      break;
    case DLINES:  
      RequiredInterfaceLineData dline = 
        new RequiredInterfaceLineData(
                        x1,y1,x,y,linecount,SOLID); 
      // Flow flw2 = new Flow("f" + linecount); 
      // find_src_targ(dline,flw2); 
      // Generalisation gen = defineGeneralisation(flw2); 
      // if (gen != null) 
      // { linecount++;
      //   dline.setFlow(flw2); 
      visuals.addElement(dline);
      String iname = 
        JOptionPane.showInputDialog("Enter required interface name:");
      Entity rint = 
        (Entity) ModelElement.lookupByName(iname,entities); 
      if (rint == null) 
      { System.err.println("! Interface " + iname + " does not exist, it will be added to the class diagram."); 
        rint = new Entity(iname); 
        rint.setInterface(true); 
        int xx = (entities.size() + 1)*100; 
        int yy = 200; 
        if (parent != null) 
        { parent.addEntity(rint,xx,yy); } 
        entities.add(rint); 
      } 
      else if (rint.isInterface()) { } 
      else 
      { System.err.println("!! This is not an interface and cannot be used as one."); } 

      dline.setModelElement(rint); 
      //   generalisations.add(gen); 
      //   dline.setModelElement(gen); 
      //   dline.setWaypoints((Vector) ((Vector) waypoints).clone()); 
      // } 
      find_src(dline); 
      findConnection(dline,x,y); 
    
      firstpress = false;
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
      updateModel();  
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

	

 







