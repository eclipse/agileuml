/*
      * Classname : ProvidedInterfaceLineData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing 
      * lines for provided interfaces

  package: Architecture
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
import javax.swing.*;
import java.awt.geom.*;
import java.util.Vector;

public class ProvidedInterfaceLineData extends LineData
{  // coordinates of start and end points of the line

  public ProvidedInterfaceLineData(int xs, int ys, int xe, 
                         int ye, int linecount, int type) 
  { super(xs,ys,xe,ye,linecount,type); } 

  public Object clone() 
  { String cnt = label.substring(1,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) 
    { count = 0; } 

    ProvidedInterfaceLineData ld = 
      new ProvidedInterfaceLineData(xstart, ystart, xend, 
                   yend, count, linetype); 
    ld.setModelElement(modelElement); 
    ld.setWaypoints(waypoints); 
    return ld; 
  } 

  void drawData(Graphics2D g) 
  { if (LineLength() > 5) 
    { g.setColor(Color.black); 
      // if (modelElement instanceof Generalisation &&
      //     ((Generalisation) modelElement).isRealization())
      // { g.setStroke(dashed); }

      if (waypoints.size() == 0)
      { g.drawLine(xstart,ystart,xend,yend); }
      else 
      { Point p1 = (Point) waypoints.get(0); 
        g.drawLine(xstart,ystart,p1.x,p1.y); 
        for (int i = 1; i < waypoints.size(); i++) 
        { Point p2 = (Point) waypoints.get(i);
          g.drawLine(p1.x,p1.y,p2.x,p2.y); 
          p1 = p2; 
        }
        g.drawLine(p1.x,p1.y,xend,yend); 
      }
      
      // g.drawLine(xstart,ystart,xend,yend);
      g.setStroke(stroke);
      // g.draw(new Line2D.Double(arrow1x,arrow1y,
      //        (double) xend,(double) yend));
      // g.draw(new Line2D.Double(arrow2x,arrow2y,
      //        (double) xend,(double) yend)); 
      drawInterfaceEnd(g);   
    }
  }

  private void drawInterfaceEnd(Graphics2D g)
  { g.setColor(Color.black);
    // g.draw(new Line2D.Double(arrow1x,arrow1y,arrow2x,arrow2y));
    int xx = (int) (xend - (arrow2x - arrow1x)/2); 
    int yy = (int) ((arrow2y + arrow1y)/2);
    if (yend <= ystart)
    { yy = yend - 5; } 
    if (xend <= xstart)
    { xx = xend - 10; }
    if (xend > xstart + 5 && yend > ystart + 5) 
    { xx = xx + 5; 
      yy = yy + 5; 
    }  
    g.drawOval(xx, yy, 10, 10);
    if (modelElement != null && 
        modelElement instanceof Entity)
    { g.drawString(modelElement.getName(), xx, yy-10); }  
    // g.draw(p);
  }

  void drawData(Graphics g) 
  { if (LineLength() > 5)
    { g.setColor(Color.black);
      // if (linetype == 1)
      // { g.setStroke(dashed); }
      if (waypoints.size() == 0)
      { g.drawLine(xstart,ystart,xend,yend); }
      else 
      { Point p1 = (Point) waypoints.get(0); 
        g.drawLine(xstart,ystart,p1.x,p1.y); 
        for (int i = 1; i < waypoints.size(); i++) 
        { Point p2 = (Point) waypoints.get(i);
          g.drawLine(p1.x,p1.y,p2.x,p2.y); 
          p1 = p2; 
        }
        g.drawLine(p1.x,p1.y,xend,yend); 
      }

      // g.drawLine(xstart,ystart,xend,yend);
      // g.setStroke(stroke);
      g.drawLine((int) arrow1x,(int) arrow1y,xend,yend);
      g.drawLine((int) arrow2x,(int) arrow2y,xend,yend);
      g.drawLine((int) arrow1x, (int) arrow1y, (int) arrow2x, (int) arrow2y); 
    }
  } 
}


