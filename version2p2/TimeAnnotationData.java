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

public class TimeAnnotationData extends VisualData
{  // coordinates of start and end points of the line
  int xstart, xend; 
  int ystart, yend;

  TimeAnnotation ta; 

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

  public TimeAnnotationData(int xs, int ys, Color c, int linecount) 
  { super(new String("ta" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xs;  
    yend = ys; 
    linetype = 1; // dashed
    
    // This places the name on top of the line
    namex = (xstart - 5); 
    namey = ystart;  
  }

  public TimeAnnotationData(int xs, int ys, int xe, int ye, int linecount, int type) 
  { super(new String("ta" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xe;   
    yend = ys; 
    linetype = type;
    
    // This places the name on top of the line
    namex = (xstart - 5); 
    namey = ystart;  
  }

  public void setStartSelected()
  { selected = STARTSELECTED; } 

  public void setMidSelected()
  { selected = MIDSELECTED; } 

  public void setEndSelected() 
  { selected = ENDSELECTED; } 

  public Object clone() 
  { String cnt = label.substring(2,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) 
    { count = 0; } 

    TimeAnnotationData ld = 
      new TimeAnnotationData(xstart, ystart, xend, 
                   yend, count, linetype); 
    ld.setTimeAnnotation(ta);
    return ld; 
  } 

  public void setTimeAnnotation(TimeAnnotation t) 
  { ta = t; } 

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
                           (xend - xstart));
  }

  /* Identifies the components that it is under */
  boolean isUnder(int x, int y) 
  { boolean res = false; 
    if (x < xstart && x < xend) { return false; }  // before start
    if (x > xend && x > xstart) { return false; }  // after end
    
    res = ((int) Math.sqrt((y - ystart)*(y - ystart)) < 5);
    return res;  
  }

  // boolean isUnder(int x1, int y1, int x2, int y2)
  // { return (x1 <= xstart && xstart <= x2 && y1 <= ystart && y2 >= ystart); } 

  boolean isUnderStart(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xstart)*(x - xstart) + 
                       (y - ystart)*(y - ystart)) < 5); 
    if (res) { selected = STARTSELECTED; } 
    return res; 
  } 

  boolean isUnderEnd(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xend)*(x - xend) + 
                       (y - ystart)*(y - ystart)) < 5); 
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

  void drawData(Graphics2D g) 
  { g.setColor(Color.black); 
    if (linetype == 1)
    { g.setStroke(dashed); }
    g.drawLine(xstart,ystart,xend,ystart);
    if (ta != null)
    { if (ta.condition != null)
      { String lab = "" + ta.condition;  
        g.drawString("[" + lab + "]",namex,namey);
      }
      String nm = ""; 
      if ("<<forall>>".equals(ta.stereotype))
      { nm = " <<forall>>"; } 
      g.drawString(ta.getTime() + nm,xend,ystart); 
    } 
    g.setStroke(stroke); 
  }

  void drawData(Graphics g) 
  { g.setColor(Color.black); 
    g.drawLine(xstart,ystart,xend,ystart);
    if (ta != null)
    { if (ta.condition != null)
      { String lab = "" + ta.condition;  
        g.drawString("[" + lab + "]",namex,namey);
      }
      String nm = ""; 
      if ("<<forall>>".equals(ta.stereotype))
      { nm = " <<forall>>"; } 
      g.drawString(ta.getTime() + nm,xend,ystart); 
    } 
  }




  public void setx1(int x1)
  { xstart = x1;
    namex = (xstart - 5);
  }

  public void sety1(int y1)
  { ystart = y1;
    namey = ystart;
  }

  public void setx2(int x2)
  { xend = x2; }

  public void sety2(int y2)
  { yend = y2;
    ystart = y2;
    namey = ystart; 
  }

  public void changePosition(int oldx, int oldy, int x, int y)   
  { if (selected == STARTSELECTED)
    { xstart = x; 
      ystart = y;
      yend = y;  
    } 
    else if (selected == ENDSELECTED) 
    { xend = x;
      yend = y;
      ystart = y;  
    } 
      
    namex = (xstart - 5);
    namey = ystart;
  } 

  public void shrink(int factor) 
  { super.shrink(factor); 
    xstart = xstart/factor; 
    ystart = ystart/factor; 
    xend = xend/factor; 
    yend = yend/factor;   
    namex = (xstart - 5);
    namey = ystart;
  } 

  public void moveUp(int amount)
  { super.moveUp(amount);
    ystart -= amount;
    namey = ystart;
    yend -= amount;
  }

  public void moveDown(int amount)
  { super.moveDown(amount);
    ystart += amount;
    yend += amount;
    namey = ystart;
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
    namex = xstart - 5;
    xend += amount;
  }

}


