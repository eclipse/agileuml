/*
      * Classname : ActorRectData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for 
      * drawing actors in use case diagrams

  package: GUI
      */

/******************************
* Copyright (c) 2003-2025 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

class ActorRectData extends RectForm
{ private boolean features = false; 
  
  ActorRectData(int xx, int yy, Color cc, int rectcount)
  { super(" ",cc,xx,yy); 
    label = new String("Actor" + rectcount); 

    namex = xx + 5; 
    namey = yy + 15;
    width = 50; 
    height = 50; 
    features = false;
  }

  public Object clone() 
  { int cType = UCDArea.SENSOR; // default.
    ActorRectData rd = 
      new ActorRectData(sourcex,sourcey,color,0); 
    rd.setName(label,namex,namey); 
    rd.setSize(width,height); 
    rd.setModelElement(modelElement); 
    return rd; 
  } 

  public VisualData getCDVisual()
  { ActorRectData rd = (ActorRectData) clone(); 
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
    int w1 = fm.stringWidth(label); 
    int w2 = fm.stringWidth("  <<actor>>"); 
    int w = w2; 
    if (w1 > w2) 
    { w = w1; }

    if (w + (namex - sourcex) >= width) 
    { width = w + (namex - sourcex) + 5; }
    if (20 + (namey - sourcey) >= height)
    { height = 20 + (namey - sourcey) + 5; }
  }  

  void drawData(Graphics2D g) 
  { g.setFont(new Font("Serif", Font.BOLD, 18));
    adjustWidth(g);
    g.setColor(Color.black);
    g.drawOval(sourcex, sourcey,width - 1, height - 1);
    g.drawRect(sourcex, sourcey,width - 1, height - 1);     
    g.drawString("  <<actor>>",namex,namey);
    g.drawString(label,namex,namey+20); 
 }


  void drawData(Graphics g) // and <<active>>, <<interface>>
  { adjustWidth(g); 
    g.setColor(Color.black);
    g.drawOval(sourcex, sourcey,width - 1, height - 1);
    g.drawRect(sourcex, sourcey,width - 1, height - 1);     
    g.drawString("  <<actor>>",namex,namey);
    g.drawString(label,namex,namey+20); 
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

  public boolean isNearEnd(int x, int y) 
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
  { return; }

  private void drawEntityFeatures(Graphics g,FontMetrics fm,int widest)
  { return; }

  private void drawTypeFeatures(Graphics g,FontMetrics fm,int widest)
  { return; }

  private void drawRequirementFeatures(Graphics g,FontMetrics fm,int widest)
  { return; }

}


