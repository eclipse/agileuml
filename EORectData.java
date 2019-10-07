/*
      * Classname : EORectData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing the rounded rectangle for 
      * the execution occurrence (in a sequence diagram), getting the coordinates etc.
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

class EORectData extends RectForm
{ ExecutionInstance ei; 

  EORectData(int xx, int yy, Color cc, int rectcount)
  { super("",cc,xx,yy); 
    namex = xx + 5; 
    namey = yy + 15;
    width = 40; 
    height = 30; 
  }

  public void setEI(ExecutionInstance x)
  { ei = x; } 

  public Object clone() 
  { EORectData eo = new EORectData(sourcex,sourcey,color,0); 
    eo.setEI(ei); 
    return eo; 
  } 

  void drawData(Graphics2D g)
  { draw(g); } 

  void drawData(Graphics g)
  { draw(g); } 

  private void adjustWidth(Graphics g) 
  {  // compare size of name and width of rectangle
    FontMetrics fm = g.getFontMetrics(); 
    if (fm.stringWidth(label) + (namex - sourcex) >= width) 
    { width = fm.stringWidth(label) + (namex - sourcex) + 5; }
    if (20 + (namey - sourcey) >= height)
    { height = 20 + (namey - sourcey) + 5; }
  }  

  private void draw(Graphics g) 
  { g.setFont(new Font("Serif", Font.BOLD, 18));
    adjustWidth(g);
    g.setColor(Color.black);
    g.drawRect(sourcex, sourcey,
                    width - 1, height - 1);
    g.setColor(Color.gray); 
    g.fillRect(sourcex+1,sourcey+1,width-2,height-2); 
    g.setColor(Color.black);
    if (ei != null)
    { g.drawString("" + ei,namex,namey + 10); } 
  }

  public void extendTo(int x, int y)
  { height = y - sourcey; 
    width = x - sourcex; 
  } 

  public void changePosition(int oldx, int oldy, int x, int y)
  { height = y - sourcey; 
    width = x - sourcex; 
  } 

  public boolean isUnderEnd(int x, int y)
  { return false; } 

  public boolean isUnderStart(int x, int y)
  { return false; } 

  public boolean isUnder(int x, int y)
  { return (x >= sourcex && x <= sourcex + width &&
            y >= sourcey && y <= sourcey + height); 
  } 

}




    

