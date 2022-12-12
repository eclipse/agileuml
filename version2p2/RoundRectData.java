/*
      * Classname : RoundRectData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing the rounded rectangle for 
      * the state (in a statechart diagram), getting the coordinates etc.
   package: State machine GUI
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

class RoundRectData extends RectForm
{
  //Link to the state - logical representation
  State state;

  RoundRectData(int xx, int yy, Color cc, int rectcount)
  { super(new String("state" + rectcount),xx,yy); 
    //this can be used to change the filling colour - hazard states etc.
    color = cc; 

    namex = xx + 5; 
    namey = yy + 15;
    width = 60; 
    height = 60; 
  }

  RoundRectData(int xx, int yy, int rectcount)
  { super(new String("state" + rectcount),xx,yy); 
   
    namex = xx + 5; 
    namey = yy + 15;
    width = 260; 
    height = 260; 
  }

  public Object clone() 
  { return new RoundRectData(sourcex,sourcey,color,0); } 

  void setName(String ss, int xpos, int ypos)
  { label = ss; 
    namex = xpos; 
    namey = ypos; 
  }

  public void setState(State st)
  {
    state = st;
  }

  void drawData(Graphics2D g) 
  { draw(g); } 

  void drawData(Graphics g) 
  { draw(g); } 

/* Font myFont = new Font("Serif", Font.BOLD, 12);, then use a setFont method on your components like

JButton b = new JButton("Hello World");
b.setFont(myFont); */ 


  private void draw(Graphics g) 
  { FontMetrics fm = g.getFontMetrics(); 
    //compare the size of the name and the width of the rectangle
    if (fm.stringWidth(label) + (namex - sourcex) >= width) 
    { 
      width = fm.stringWidth(label) + (namex - sourcex) + 5; 
    }
    if (20 + (namey - sourcey) >= height)
    { 
      height = 20 + (namey - sourcey) + 5; 
    }
    /*g.setColor(color); 
    g.drawRoundRect(sourcex,sourcey,width,height,5,5); 
    g.drawString(label,namex,namey);*/

    g.setColor(Color.black);
    g.drawRoundRect(sourcex, sourcey,
                       width - 1, height - 1,10,10);
    if (state != null && (state instanceof BasicState))
    { if (state.excluded) 
      { g.setColor(Color.gray); } 
      else if (state.stateKind == State.LOOPSTATE)
      { g.setColor(Color.red); } 
      else if (state.stateKind == State.IFSTATE)
      { g.setColor(Color.blue); }
      else if (state.stateKind == State.FINALSTATE)
      { g.setColor(Color.green); } 
      else 
      { g.setColor(Color.yellow); }
      g.fillRoundRect(sourcex + 1, sourcey + 1,
                         width - 2, height - 2,10,10);
      g.setColor(Color.black);
    }

    g.drawString(label,namex,namey);

    if (state == null) { return; } 

    if (state.initial) 
    { g.drawLine(sourcex - 10, sourcey - 10, sourcex, sourcey); 
      g.drawLine(sourcex - 5, sourcey, sourcex, sourcey); 
      g.drawLine(sourcex, sourcey - 5, sourcex, sourcey); 
    } 

    Maplet mm = state.getProperties(); 
    if (mm != null)
    { Vector invs = (Vector) mm.dest; 
      if (invs != null && invs.size() > 0)
      { g.drawString("" + invs,namex,namey + 15); } 
    } // adjust width, height

    Expression e = state.getEntryAction(); 
    if (e != null) 
    { g.drawString("entry: " + e,namex,namey + 25); } 
 
  }
  
  boolean isUnder(int xx, int yy)
  { 
    int endx = sourcex + width ; 
    int endy = sourcey + height ; 
    
    // 3 is added to make for the area on the border 
    if (xx <= endx+3 && sourcex-3 <= xx &&
        yy <= endy+3 && sourcey-3 <= yy)
      {
	System.out.println("Rect: " + sourcex + " " + sourcey + " to " + endx + " " + endy);
	System.out.println("is under " + xx + " " + yy);  
	return true;
      }
    else
     {    
       System.out.println("Rect: " + sourcex + " " + sourcey + " to " + endx + " " + endy);
       System.out.println("is not under " + xx + " " + yy); 
       return false; 
     }
  }

  public boolean isUnderStart(int x, int y) 
  { return false; } 

  public boolean isUnderEnd(int x, int y) 
  { return false; } 
  
  /* place_text -- extend width if nec. textx, texty ... */ 
  public void changePosition(int oldx, int oldy, int x, int y)
  { sourcex = x; 
    sourcey = y; 
    namex = sourcex + 5; 
    namey = sourcey + 15; 
  } 
}




    

