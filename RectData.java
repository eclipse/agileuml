/*
      * Classname : RectData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for 
      * drawing the rectangle required for 
      * the classes in the class diagram

  package: GUI
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
import javax.swing.*;
import java.util.Vector;

class RectData extends RectForm
{ private boolean features = false; 
  Statemachine component; 
  
  RectData(int xx, int yy, Color cc, int cType, int rectcount)
  { super(" ",cc,xx,yy); 
    if (cType == UCDArea.SENSOR)
    { label = new String("s" + rectcount); }
    else
    { label = new String("a" + rectcount); }

    namex = xx + 5; 
    namey = yy + 15;
    width = 50; 
    height = 50; 
    features = true;
  }

  public Object clone() 
  { int cType = UCDArea.SENSOR; // default.
    RectData rd = 
      new RectData(sourcex,sourcey,color,cType,0); 
    rd.setName(label,namex,namey); 
    rd.setSize(width,height); 
    rd.setModelElement(modelElement); 
    return rd; 
  } 

  public VisualData getCDVisual()
  { RectData rd = (RectData) clone(); 
    rd.showFeatures(); 
    return rd; 
  } 

  void setName(String ss, int xpos, int ypos)
  { label = ss; 
    namex = xpos; 
    namey = ypos; 
  } 

  public void setSize(int wid, int high)
  { width = wid; 
    height = high; 
  } 

  private void adjustWidth(Graphics g) 
  {  // compare size of name and width of rectangle
    FontMetrics fm = g.getFontMetrics(); 
    if (fm.stringWidth(label) + (namex - sourcex) >= width) 
    { width = fm.stringWidth(label) + (namex - sourcex) + 5; }
    if (20 + (namey - sourcey) >= height)
    { height = 20 + (namey - sourcey) + 5; }
  }  

  void drawData(Graphics2D g) 
  { g.setFont(new Font("Serif", Font.BOLD, 18));
    adjustWidth(g);
    g.setColor(Color.black);
    if (features)
    { drawFeatures(g); }
    g.drawRect(sourcex, sourcey,width - 1, height - 1);
    // if (modelElement != null && modelElement instanceof Type)
    // { g.drawString(label + " <<enumeration>>",namex,namey); }
    
    if (modelElement != null && modelElement instanceof Entity)
    { Entity e = (Entity) modelElement; 
      if (e.isAbstract() || e.isInterface())
      { Font ff = g.getFont(); 
        Font newff = new Font(ff.getName(),Font.ITALIC,ff.getSize());
        g.setFont(newff);  
        g.drawString(label,namex,namey); 
        g.setFont(ff); 
        if (e.isInterface())
        { g.drawString("<<interface>>",namex,namey-5);
          FontMetrics fm = g.getFontMetrics(); 
          if (fm.stringWidth("<<interface>>") + (namex - sourcex) >= width) 
          { width = fm.stringWidth("<<interface>>") + (namex - sourcex) + 5; }
        }
      } 
      else if (e.isActive())
      { g.drawString(label,namex,namey);
        g.drawString("<<active>>",namex,namey-5);
        FontMetrics fm = g.getFontMetrics(); 
        if (fm.stringWidth("<<active>>") + (namex - sourcex) >= width) 
        { width = fm.stringWidth("<<active>>") + (namex - sourcex) + 5; }
      }
      else 
      { g.drawString(label,namex,namey); }
 

      // String ecard = e.getCardinality(); 
      // if (ecard != null)
      // { g.drawString(ecard, sourcex + width - 10, sourcey + 10); }
    }
    else if (modelElement != null && modelElement instanceof Requirement)
    { Requirement req = (Requirement) modelElement; 
      Font ff = g.getFont(); 
      // Font newff = new Font(ff.getName(),Font.ITALIC,ff.getSize());
      // g.setFont(newff);  
      g.drawString(label,namex,namey+5); 
      g.setFont(ff); 
      g.drawString("<<requirement>>",namex,namey-5);
      FontMetrics fm = g.getFontMetrics(); 
      if (fm.stringWidth("<<requirement>>") + (namex - sourcex) >= width) 
      { width = fm.stringWidth("<<requirement>>") + (namex - sourcex) + 5; }
    } 
    else if (modelElement != null && modelElement instanceof Type)
    { Type typ = (Type) modelElement; 
      Font ff = g.getFont(); 
      g.drawString(label,namex,namey+5); 
      g.setFont(ff); 
      g.drawString("<<enumeration>>",namex,namey-5);
      FontMetrics fm = g.getFontMetrics(); 
      if (fm.stringWidth("<<enumeration>>") + (namex - sourcex) >= width) 
      { width = fm.stringWidth("<<enumeration>>") + (namex - sourcex) + 5; }
    } 
    else 
    { g.drawString(label,namex,namey); }

    // for entity draw card
  }

  /* private void drawMultiplicity(Graphics g)
  { if (component != null)
    { int multip = component.getMultiplicity();
      if (multip > 1)
      { g.drawString(""+multip, sourcex + width - 10, sourcey + 10); }
    }
  }  */

  void drawData(Graphics g) // and <<active>>, <<interface>>
  { adjustWidth(g); 
    g.setColor(Color.black);
    if (features)
    { drawFeatures(g); }
    g.drawRect(sourcex, sourcey,width - 1, height - 1);
    if (modelElement != null && modelElement instanceof Type)
    { g.drawString(label + " <<enumeration>>",namex,namey); }
    else 
    { g.drawString(label,namex,namey); } // for entity draw card
  }

  /* Returns true if (xx,yy), which is coordinate
     of the start of a line,
   * are found within rectangle */
  boolean isUnder(int xx, int yy)
  { // get the end points of the rectangle
    int endx = sourcex + width; 
    int endy = sourcey + height; 
  
    // Check that xx,yy is within start & end rectangle
    if (xx <= endx && sourcex <= xx &&
        yy <= endy && sourcey <= yy)
    { return true; }
    else 
    { return false; }
  }

  public boolean isUnderStart(int x, int y) 
  { return false; } 

  public boolean isUnderEnd(int x, int y) 
  { return false; } 

  public void changePosition(int oldx, int oldy, 
                             int x, int y)
  { sourcex = x; 
    sourcey = y; 
    namex = sourcex + 5; 
    namey = sourcey + 15; 
  } 

  private void showFeatures()
  { features = true; }

  // Show all the attributes:
  private void drawFeatures(Graphics g)
  { FontMetrics fm = g.getFontMetrics();
    int widest = width;
    // int ypos = namey;
    if (modelElement == null) { return; }
    if (modelElement instanceof Entity)
    { drawEntityFeatures(g,fm,widest); } 
    else if (modelElement instanceof Type)
    { drawTypeFeatures(g,fm,widest); } 
    else if (modelElement instanceof Requirement)
    { drawRequirementFeatures(g,fm,widest); } 
  }

  private void drawEntityFeatures(Graphics g,FontMetrics fm,int widest)
  { Vector atts =
      ((Entity) modelElement).getAttributes();
    int xpos = namex;
    int y = namey + 15;
    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i);
      String nme = att.getName();
      String init = att.getInitialValue(); 
      boolean uniq = att.isUnique();
      boolean froz = att.isFrozen(); 
      boolean classScope = att.isClassScope();
      int kind = att.getKind();
      String decor = "  ";
      if (kind == ModelElement.SEN) { decor = "? "; }
      else if (kind == ModelElement.ACT) { decor = "! "; }
      else if (kind == ModelElement.DERIVED) { decor = "/ "; } 

      String line1 = decor + nme + ": " + att.getType(); 
      if (init != null && !init.equals(""))
      { line1 = line1 + " = " + init; } 
      if (uniq) 
      { if (froz)
        { line1 = line1 + " { identity, frozen }"; }
        else
        { line1 = line1 + " { identity }"; }
      }
      else if (froz)
      { line1 = line1 + " { frozen }"; }
      g.drawString(line1,xpos,y);
      if (classScope) 
      { g.drawLine(xpos,y+1,xpos + fm.stringWidth(line1),y+1); } 
      int wid1 = fm.stringWidth(line1) + 10;
      if (wid1 > widest)
      { widest = wid1; }
      y += 15;
    }
    width = widest;
    height = y - namey + 10;
    int attendy = y - 10; 
    Vector ops = ((Entity) modelElement).getOperations(); 
    int nops = ops.size(); 
    // if (nops > 0)
    // { g.drawLine(sourcex,height,sourcex + width - 1,height); }
    for (int j = 0; j < nops; j++) 
    { BehaviouralFeature op = (BehaviouralFeature) ops.get(j); 
      boolean cls = op.isClassScope(); 
      // draw  opname(): type
      String brackets = "()"; 
      Vector pps = op.getParameters();
      if (pps.size() > 0) { brackets = "(...)"; }  
      String opname = op.getName() + brackets; 
      Type resT = op.getResultType(); 
      if (resT != null)
      { opname = opname + ": " + resT; } 
      if (op.isAbstract())
      { Font ff = g.getFont(); 
        Font newff = new Font(ff.getName(),Font.ITALIC,ff.getSize());
        g.setFont(newff);  
        g.drawString(opname,xpos,y); 
        g.setFont(ff); 
      }
      else 
      { g.drawString(opname,xpos,y); } 

      if (cls) 
      { g.drawLine(xpos,y+1,xpos + fm.stringWidth(opname),y+1); } 
      
      int wid1 = fm.stringWidth(opname) + 10;
      if (wid1 > widest)
      { widest = wid1; }
      y += 15;
    }      
    width = widest;
    if (nops > 0)
    { g.drawLine(sourcex,attendy,sourcex + width - 1,attendy); }
    height = y - namey + 10;
    g.drawLine(sourcex,namey + 2,
               sourcex + width-1,namey + 2);
  }

  private void drawTypeFeatures(Graphics g,FontMetrics fm,int widest)
  { Vector vals =
      ((Type) modelElement).getValues();
    int xpos = namex;
    int y = namey+15;
    if (vals != null)
    { for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        g.drawString(val,xpos,y); 
        int wid1 = fm.stringWidth(val) + 10;
        if (wid1 > widest)
        { widest = wid1; }
        y += 15;
      }
    } 
    width = widest;
    height = y - namey + 10;
    g.drawLine(sourcex,namey + 2,
               sourcex + width-1,namey + 2);
  }

  private void drawRequirementFeatures(Graphics g,FontMetrics fm,int widest)
  { Vector vals = new Vector();
    Requirement req = (Requirement) modelElement;  
    vals.add("id = " + req.id); 
    vals.add("text = " + req.text); 
    vals.add("kind = " + req.requirementKind); 
    vals.add("scope = " + req.getScope()); 
    int xpos = namex;
    int y = namey+20;
    if (vals != null)
    { for (int i = 0; i < vals.size(); i++) 
      { String val = (String) vals.get(i); 
        g.drawString(val,xpos,y); 
        int wid1 = fm.stringWidth(val) + 10;
        if (wid1 > widest)
        { widest = wid1; }
        y += 15;
      }
    } 
    width = widest;
    height = y - namey + 5;
    g.drawLine(sourcex,namey + 8,
               sourcex + width-1,namey + 8);
  }

}


