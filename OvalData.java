/*
      * Classname : OvalData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing the oval required for 
      * the controllers in the DCFD diagram.
      
        Package: GUI
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

class OvalData extends RectForm
{ Statemachine component; 
  
  public OvalData(int xx, int yy, Color cc, int ovalcount)
  { super(new String("c" + ovalcount),xx,yy); 
    //this can be used to change the filling colour
    color = cc; 
    // sourcex = xx;
    // sourcey = yy;

    namex = xx + 20;  
    namey = yy + 20;
    width = 100; 
    height = 40; 
  }

  public Object clone() 
  { OvalData res = new OvalData(sourcex,sourcey,color,0); 
    res.label = label; 
    return res; 
  }  

  public void setComponent(Statemachine sm) 
  { component = sm; } 

  public void setName(String ss, int xpos, int ypos)
  { 
    label = ss; 
    namex = xpos; 
    namey = ypos; 
  } 

  /* public VisualData getCDVisual()
  { String cnt = label.substring(1,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) { } 
    RectData rd = new RectData(sourcex,sourcey,color,DCFDArea.CONTROLLER,count); 
    rd.setName(label,namex,namey); 
    rd.setSize(width,height); 
    return rd; 
  } */

  public void horizontalSplit()
  { sourcex = sourcex - 55; 
    sourcey = sourcey - 50; 
    namex = namex - 50; 
    namey = namey - 45; 
    width = 200; 
    height = 160; 
  } 

  public void makeSmall() 
  { height = 20; 
    width = 50; 
  } 

  void drawData(Graphics2D g) 
  { 
    // System.out.println("drawData for Oval entered");
    g.setFont(new Font("Serif", Font.BOLD, 18));
    UseCase me = (UseCase) modelElement; 
    if (me.isAbstract())
    { Font newff = new Font("Serif", Font.ITALIC, 18);
      g.setFont(newff);  
    } 
    
    FontMetrics fm = g.getFontMetrics(); 
    //compare the size of the name and the width of the rectangle
    if (fm.stringWidth(label) + (namex - sourcex) >= width) 
      { 
	width = fm.stringWidth(label) + (namex - sourcex) + 5; 
      }
    if (20 + (namey - sourcey) >= height)
      { 
	height = 20 + (namey - sourcey) + 5; 
      }
    
    g.setColor(Color.black);
    g.drawOval(sourcex, sourcey,width - 1, height - 1);
    //g.setColor(Color.blue);
    //g.fillRect(sourcex + 1, sourcey + 1, width - 2, height - 2);
    //g.setColor(Color.black);
    g.drawString(label,namex,namey);
  }

  void drawData(Graphics g)
  {
    g.setFont(new Font("Serif", Font.BOLD, 18));
    UseCase me = (UseCase) modelElement; 
    if (me.isAbstract())
    { Font newff = new Font("Serif", Font.ITALIC, 18);
      g.setFont(newff);  
    } 
    FontMetrics fm = g.getFontMetrics();
    //compare the size of the name and the width of the rectangle
    if (fm.stringWidth(label) + (namex - sourcex) >= width)
      {
        width = fm.stringWidth(label) + (namex - sourcex) + 5;
      }
    if (20 + (namey - sourcey) >= height)
      {
        height = 20 + (namey - sourcey) + 5;
      }

    g.setColor(Color.black);
    g.drawOval(sourcex, sourcey,width - 1, height - 1);
    g.drawString(label,namex,namey);
  }


  boolean isUnder(int xx, int yy)
  { int endx = sourcex + width; 
  int endy = sourcey + height; 

  if (xx <= endx && sourcex <= xx &&
      yy <= endy && sourcey <= yy)
    {
      // System.out.println("Oval: " + sourcex + " " + sourcey + " to " + endx + " " + endy);
      // System.out.println("is under " + xx + " " + yy);  
      return true;
   }
  else 
    {
      // System.out.println("Oval: " + sourcex + " " + sourcey + " to " + endx + " " + endy);
      // System.out.println("is not under " + xx + " " + yy); 
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
    namex = sourcex + 10; 
    namey = sourcey + 15; } 

}

