/*
      * Classname : LSRectData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing the rounded rectangle for 
      * the state (in a sequence diagram), getting the coordinates etc.
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

class LSRectData extends RoundRectData
{
  LSRectData(int xx, int yy, Color cc, int rectcount)
  { super(xx,yy,Color.white,rectcount); 
    namex = xx + 5; 
    namey = yy + 15;
    width = 40; 
    height = 30; 
  }

  public Object clone() 
  { return new LSRectData(sourcex,sourcey,color,0); } 

  void drawData(Graphics2D g)
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
  { g.setColor(Color.black);
    g.setFont(new Font("Serif", Font.BOLD, 18));
    adjustWidth(g);
    g.drawRoundRect(sourcex, sourcey,
                    width - 1, height - 1,10,10);
    if (state != null && (state instanceof BasicState))
    { Maplet mm = state.getProperties(); 
      if (mm != null)
      { Vector invs = (Vector) mm.dest; 
        if (invs != null && invs.size() > 0)
        { g.drawString("" + invs,namex,namey + 15); } 
      } 
    } 
  }

  public void extendTo(int x, int y)
  { height = y - sourcey; 
    width = x - sourcex; 
  } 
}




    

