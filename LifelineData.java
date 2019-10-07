/*
      * Classname : LineData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing 
      * lines for lifelines
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
import java.awt.geom.*;
import java.util.Vector;

public class LifelineData extends VisualData
{  // coordinates of start and end points of the line
  int xstart, xend; 
  int ystart, yend;

  UMLObject object; 

  double length; //length of the line
  int leftright = 0; 
  // The line type -- solid or dashed
  int linetype;
  
  // Solid line stroke
  final static BasicStroke stroke = 
                        new BasicStroke(2.0f);
  
  // For producing dashed lines
  final static float dash1[] = {10.0f};
  final static BasicStroke dashed = 
      new BasicStroke(1.0f, 
                      BasicStroke.CAP_BUTT, 
                      BasicStroke.JOIN_MITER, 
                      10.0f, dash1, 0.0f);
  
  public static final int NONESELECTED = 0; 
  public static final int MIDSELECTED = 1; 
  public static final int STARTSELECTED = 2;
  public static final int ENDSELECTED = 3; 

  int selected = NONESELECTED;  

  public LifelineData(int xs, int ys, Color c, int linecount) 
  { super(new String("l" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xs;  // lifelines are vertical 
    yend = ys + 10; 
    linetype = 1; // dashed
    
    // This places the name on top of the line
    namex = (xstart - 5); 
    namey = (ystart - 5);  
  }

  public LifelineData(int xs, int ys, int xe, int ye, int linecount, int type) 
  { super(new String("l" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xs;  // lifelines are vertical 
    yend = ye; 
    linetype = type;
    
    // This places the name on top of the line
    namex = (xstart - 5); 
    namey = (ystart - 5);  
  }

  public void setStartSelected()
  { selected = STARTSELECTED; } 

  public void setMidSelected()
  { selected = MIDSELECTED; } 

  public void setEndSelected() 
  { selected = ENDSELECTED; } 

  public Object clone() 
  { String cnt = label.substring(1,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) 
    { count = 0; } 

    LifelineData ld = 
      new LifelineData(xstart, ystart, xend, 
                   yend, count, linetype); 
    ld.setObject(object);
    ld.setModelElement(modelElement); 
    return ld; 
  } 

  public void setObject(UMLObject t) 
  { object = t; } 

  // returns x position of first point of the line
  int getx()
  { return xstart; }
  
  // returns y position of the first point of line
  int gety()
  { return ystart; }

  int getx2()
  { return xend; }
  
  // returns y position of the last point of line
  int gety2()
  { return yend; }

  int LineLength() 
  { return (int) Math.sqrt((xend - xstart)*
                           (xend - xstart) + 
			  (yend - ystart)*(yend - ystart));
  }

  /* Identifies the components that it is under */
  boolean isUnder(int x, int y) 
  { boolean res = false; 
    if (y < ystart) { return false; }  // before start
    if (y > yend) { return false; }    // after end
    
    res = ((int) Math.sqrt((x - xstart)*(x - xstart)) < 10);
    return res;  
  }

  boolean isUnder(int x1, int y1, int x2, int y2)
  { return (x1 <= xstart && xstart <= x2 && y1 >= ystart && y2 <= yend); } 

  boolean isUnderStart(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xstart)*(x - xstart) + 
                       (y - ystart)*(y - ystart)) < 10); 
    if (res) { selected = STARTSELECTED; } 
    return res; 
  } 

  boolean isUnderEnd(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xstart)*(x - xstart) + 
                       (y - yend)*(y - yend)) < 10); 
    if (res) { selected = ENDSELECTED; } 
    return res; 
  } 

  void SetName(String s, int x, int y) 
  { label = s; 
    namex = x; 
    namey = y;
  }

  boolean nearlyEqual(int a, int b)
  {  return ((a <= b+5 && b <= a) || 
             (b <= a+5 && a <= b)); 
  } 

  /* private void adjustWidth(Graphics g) 
  {  // compare size of name and width of rectangle
    FontMetrics fm = g.getFontMetrics(); 
    if (fm.stringWidth(label) + (namex - sourcex) >= width) 
    { width = fm.stringWidth(label) + (namex - sourcex) + 5; }
    if (20 + (namey - sourcey) >= height)
    { height = 20 + (namey - sourcey) + 5; }
  }  */ 

  void drawData(Graphics2D g) 
  { if (LineLength() < 5) { return; }  
    g.setFont(new Font("Serif", Font.BOLD, 18));
    // adjustWidth(g);
    g.setColor(Color.black); 
    if (linetype == 1)
    { g.setStroke(dashed); }
    g.drawLine(xstart,ystart,xstart,yend);
    g.setStroke(stroke);
    if (object != null)
    { label = "" + object; } 
    g.drawString(label,namex,namey);
    FontMetrics fm = g.getFontMetrics();
    int xoffset = fm.stringWidth(label); 
    g.drawRect(namex-5,namey-10,xoffset+10,10);
    if (object != null && object.getTerminates())
    { g.drawLine(xstart - 10, yend - 7, xstart + 10, yend + 7); 
      g.drawLine(xstart - 10, yend + 7, xstart + 10, yend - 7); 
    } 
 }

  void drawData(Graphics g) 
  { if (LineLength() < 5) { return; }  
    g.setColor(Color.black); 
    // if (linetype == 1)
    // { g.setStroke(dashed); }
    g.drawLine(xstart,ystart,xstart,yend);
    // g.setStroke(stroke);
    g.drawString(label,namex,namey);
    FontMetrics fm = g.getFontMetrics();
    int xoffset = fm.stringWidth(label); 
    g.drawRect(namex-5,namey-10,xoffset+10,10);
    if (object != null && object.getTerminates())
    { g.drawLine(xstart - 10, yend - 7, xstart + 10, yend + 7); 
      g.drawLine(xstart - 10, yend + 7, xstart + 10, yend - 7); 
    } 

  }




  public void setx1(int x1)
  { xstart = x1;
    namex = (xstart - 5);
  }

  public void sety1(int y1)
  { ystart = y1;
    namey = (ystart - 5);
  }

  public void setx2(int x2)
  { xend = x2;
    xstart = x2;
  }

  public void sety2(int y2)
  { yend = y2;  }

  public void changePosition(int oldx, int oldy, int x, int y)   
  { if (selected == STARTSELECTED)
    { xstart = x;
      xend = x; 
      ystart = y; 
    } 
    else if (selected == ENDSELECTED) 
    { xend = x;
      xstart = x; 
      yend = y; 
    } 
    else if (selected == MIDSELECTED)
    { int ymid = (ystart + yend)/2; 
      xstart = x;
      ystart = ystart + y - ymid;
      xend = x;
      yend = yend + y - ymid;
    }  
    namex = (xstart - 5);
    namey = (ystart - 5);
  } 

  public void shrink(int factor) 
  { super.shrink(factor); 
    xstart = xstart/factor; 
    ystart = ystart/factor; 
    xend = xend/factor; 
    yend = yend/factor;   
    namex = (xstart - 5);
    namey = (ystart - 5);
  } 

  public void moveUp(int amount)
  { super.moveUp(amount);
    ystart -= amount;
    namey = (ystart - 5);
    yend -= amount;
  }

  public void moveDown(int amount)
  { super.moveDown(amount);
    ystart += amount;
    yend += amount;
    namey = (ystart - 5);
  }

  public void moveLeft(int amount)
  { super.moveLeft(amount);
    xstart -= amount;
    namex = (xstart - 5);
    xend -= amount;
  }

  public void moveRight(int amount)
  { super.moveRight(amount);
    xstart += amount;
    namex = (xstart - 5);
    xend += amount;
  }

}


