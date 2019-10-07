/*
      * Classname : LineData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing 
      * lines for the 
      * associations
  package: Class Diagram Editor

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

public class LineData extends VisualData
{  // coordinates of start and end points of the line
  int xstart, xend; 
  int ystart, yend;
  Vector waypoints = new Vector(); // of LinePoint

  Transition transition; 
  Flow flow;   /* One of these is null usually */ 
  Message message = null; 
  
  //coordinates for the arrow head
  double arrow1x, arrow1y; 
  double arrow2x, arrow2y; 

  // coordinates for roles and cards
  double card1x, card1y; 
  double card2x, card2y; 
  double role1x, role1y; 
  double role2x, role2y; 

  // for aggregation symbol:
  double agg1x, agg2x, agg3x, agg1y, agg2y, agg3y;
  // for qualifier box: 
  int q1x, q1y, q2x, q2y, q3x, q3y, q4x, q4y; 

  double length; //length of the line

  int leftright = 0; 
  int downwards = 0; 

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

  public static final int LEFTRIGHT = 0; 
  public static final int RIGHTLEFT = 1; 
  public static final int UPWARDS = 2; 
  public static final int DOWNWARDS = 3; 

  int direction = 0; 

  public LineData(int xs, int ys, int xe, 
                  int ye, int linecount, int type) 
  { super(new String("t" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xe; 
    yend = ye; 
    linetype = type;
    
    // This places the name in the middle of the line
    setName(); 
    setArrows(); 
  }

  public void setStartSelected()
  { selected = STARTSELECTED; } 

  public void setMidSelected()
  { selected = MIDSELECTED; } 

  public void setEndSelected() 
  { selected = ENDSELECTED; } 

  public void addWaypoint(LinePoint p)
  { waypoints.add(p); 
    setName(); 
    setArrows(); 
  } 

  public Vector getWaypoints()
  { return waypoints; } 

  public Object clone() 
  { String cnt = label.substring(1,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) 
    { count = 0; } 

    LineData ld = 
      new LineData(xstart, ystart, xend, 
                   yend, count, linetype); 
    ld.setTransition(transition);
    ld.setFlow(flow); 
    ld.setModelElement(modelElement); 
    ld.setWaypoints(waypoints); 
    return ld; 
  } 

  public void setTransition(Transition t) 
  { transition = t; } 

  public void setFlow(Flow fl) 
  { flow = fl; } 

  public void setMessage(Message m)
  { message = m; } 

  public void setWaypoints(Vector w)
  { waypoints = w;
    setName(); 
    setArrows(); 
  } 

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
    int midx = (xstart + xend)/2; 
    int midy = (ystart + yend)/2; 

    res = ((int) Math.sqrt((x - namex)*(x - namex) +
                           (y - namey)*(y - namey)) < 15);
    if (res) { selected = MIDSELECTED; } 
    return res;  
  }

  boolean isUnderStart(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xstart)*(x - xstart) + 
                       (y - ystart)*(y - ystart)) < 15); 
    if (res) { selected = STARTSELECTED; } 
    return res; 
  } 

  boolean isUnderEnd(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xend)*(x - xend) + 
                       (y - yend)*(y - yend)) < 15); 
    if (res) { selected = ENDSELECTED; } 
    return res; 
  } 

  void SetName(String s, int x, int y) 
  { label = s; 
    namex = x; 
    namey = y;
  }

  void setName()
  { int n = waypoints.size(); 
    if (n == 0)
    { namex = (xstart + xend)/2; 
      namey = (ystart + yend)/2;
    } 
    else if (n % 2 == 0)  // even - put on middle of n/2 segment 
    { LinePoint p1 = (LinePoint) waypoints.get(n/2 - 1); 
      LinePoint p2 = (LinePoint) waypoints.get(n/2); 
      namex = (p1.x + p2.x)/2; 
      namey = (p1.y + p2.y)/2; 
    } 
    else // odd -- use the n/2 point
    { LinePoint p = (LinePoint) waypoints.get(n/2); 
      namex = p.x; 
      namey = p.y; 
    } 
  } 
  
  void setEndpoint(int x, int y)
  { xend = x; 
    yend = y; 
    setName(); 
    setArrows(); 
  } 

  void setStartpoint(int x, int y)
  { xstart = x; 
    ystart = y; 
    setName(); 
    setArrows(); 
  } 

  boolean nearlyEqual(int a, int b)
  {  return ((a <= b+5 && b <= a) || 
             (b <= a+5 && a <= b)); 
  } 

  void setArrows() 
  { // The vector from start to end of line
    int lastx = xstart; 
    int lasty = ystart; 

    if (waypoints.size() > 0)
    { LinePoint lastpoint = (LinePoint) waypoints.get(waypoints.size() - 1); 
      lastx = lastpoint.x; 
      lasty = lastpoint.y; 
    }

    double xvect = xend - lastx;
    double yvect = yend - lasty;

    // The length of the last line segment drawn
    length = Math.sqrt((xvect*xvect)+ (yvect*yvect));
    double xdir = xvect/length ;
    double ydir = yvect/length;

    arrow1x = xend - (5*xdir) - (5*ydir);
    arrow1y = yend - (5*ydir) + (5*xdir);

    arrow2x = xend - (5*xdir) + (5*ydir);
    arrow2y = yend - (5*ydir) - (5*xdir);

    role2x = xend - (10*xdir) - (10*ydir);
    role2y = yend - (10*ydir) + (10*xdir);
    card2x = xend - (10*xdir) + (10*ydir);
    card2y = yend - (10*ydir) - (10*xdir);

    if (xvect < 0) // right-left arrow
    { // card1x = xstart - 3;
      // role1x = xstart - 6; 
      leftright = 0;
    }
    else
    { // card1x = xstart + 5;
      // role1x = xstart + 2; 
      leftright = 1;
    }
    
    /* if (yvect < 0) // upwards arrow
    { card1y = ystart - 5;
      role1y = ystart + 2;
    }
    else
    { card1y = ystart + 7;
      role1y = ystart + 10;
    } */ 


    int firstx = xend; 
    int firsty = yend; 

    if (waypoints.size() > 0)
    { LinePoint firstpoint = (LinePoint) waypoints.get(0); 
      firstx = firstpoint.x; 
      firsty = firstpoint.y; 
    }

    xvect = firstx - xstart;
    yvect = firsty - ystart;

    // The length of the first line segment drawn
    double length1 = Math.sqrt((xvect*xvect)+ (yvect*yvect));
    xdir = xvect/length1 ;
    ydir = yvect/length1;

    // if waypoint - need to calculate vector to first waypoint for these
    agg1x = xstart + (8*xdir) + (8*ydir);
    agg1y = ystart + (8*ydir) - (8*xdir);

    agg2x = xstart + (8*xdir) - (8*ydir);
    agg2y = ystart + (8*ydir) + (8*xdir);
 
    agg3x = xstart + (16*xdir);
    agg3y = ystart + (16*ydir);

    card1x = xstart + (10*xdir) + (10*ydir);
    card1y = ystart + (10*ydir) - (10*xdir);

    role1x = xstart + (10*xdir) - (10*ydir);
    role1y = ystart + (10*ydir) + (10*xdir);

    if (yvect < 0) // upwards arrow
    { downwards = 0; }
    else
    { downwards = 1; } 


    if (xdir > 0 && ydir*ydir < xdir*xdir) // left-right
    { q1x = xstart;
      q1y = ystart + 5;

      q2x = xstart + 15;
      q2y = ystart + 5;
 
      q3x = xstart + 15;
      q3y = ystart - 5;

      q4x = xstart; 
      q4y = ystart - 5;
      direction = LEFTRIGHT; 
    } 
    else if (xdir < 0 && ydir*ydir < xdir*xdir) // right-left 
    { q1x = xstart; 
      q1y = ystart + 5; 

      q2x = xstart - 15; 
      q2y = ystart + 5; 
 
      q3x = xstart - 15; 
      q3y = ystart - 5; 

      q4x = xstart;
      q4y = ystart - 5;  
      direction = RIGHTLEFT; 
    } 
    else if (ydir > 0 && xdir*xdir < ydir*ydir) // downwards
    { q1y = ystart; 
      q2y = ystart + 6; 
      q3y = ystart + 6; 
      q4y = ystart; 
      q1x = xstart + 8; 
      q2x = xstart + 8; 
      q3x = xstart - 8; 
      q4x = xstart - 8; 
      direction = DOWNWARDS; 
    } 
    else 
    { q1y = ystart; 
      q2y = ystart - 6; 
      q3y = ystart - 6; 
      q4y = ystart; 
      q1x = xstart - 8; 
      q2x = xstart - 8; 
      q3x = xstart + 8; 
      q4x = xstart + 8; 
      direction = UPWARDS; 
    }
  }
  // Base the roles and cards on these positions as well? 

  void drawData(Graphics2D g) 
  { // if (LineLength() < 5) { return; }  
    g.setColor(color); 
    if (linetype == 1)
    { g.setStroke(dashed); }
    FontMetrics fm1 = g.getFontMetrics();

    int xstart1 = xstart; 
    int ystart1 = ystart; 

    if (modelElement instanceof Association && 
        ((Association) modelElement).isQualified())
    { String qvar = ((Association) modelElement).getQualifier() + ""; 
      if (direction == LEFTRIGHT)
      { xstart1 = xstart + 15 + fm1.stringWidth(qvar); } 
      else if (direction == RIGHTLEFT)
      { xstart1 = xstart - 15 - fm1.stringWidth(qvar); } 
      else if (direction == UPWARDS)
      { ystart1 = ystart - 6; } 
      else 
      { ystart1 = ystart + 6; } 
    } 
            
    if (waypoints.size() == 0)
    { g.drawLine(xstart1,ystart1,xend,yend); }
    else 
    { LinePoint p1 = (LinePoint) waypoints.get(0); 
      g.drawLine(xstart1,ystart1,p1.x,p1.y); 
      for (int i = 1; i < waypoints.size(); i++) 
      { LinePoint p2 = (LinePoint) waypoints.get(i);
        g.drawLine(p1.x,p1.y,p2.x,p2.y); 
        p1 = p2; 
      }
      g.drawLine(p1.x,p1.y,xend,yend); 
    }

    g.setStroke(stroke);
    g.draw(new Line2D.Double(arrow1x,arrow1y,
           (double) xend,(double) yend));
    g.draw(new Line2D.Double(arrow2x,arrow2y,
           (double) xend,(double) yend)); 
       
    if (modelElement != null) 
    { Association ast = (Association) modelElement;
      String role1 = ast.getRole1();
      String role2 = ast.getRole2();
      boolean ord = ast.isOrdered();
      boolean srt = ast.isSorted();
      boolean agg = ast.isAggregation(); 
      boolean qualified = ast.isQualified(); 

      if (ord) { role2 = role2 + " { ordered }"; }
      else if (srt) { role2 = role2 + " { sorted }"; }

      if (ast.isFrozen())
      { role2 = role2 + " { frozen }"; } 
      else if (ast.isAddOnly())
      { role2 = role2 + " { addOnly }"; } // and derived
      if (ast.isDerived())
      { role2 = "/" + role2; } 
      
      int x1offset = 0; 
      int x2offset = 0; 
      if (role1 != null) 
      { x1offset = fm1.stringWidth(role1); } 
      if (role2 != null) 
      { x2offset = fm1.stringWidth(role2); } 
 
      String c1 =
        ModelElement.convertCard(ast.getCard1()); 
      String c2 =
        ModelElement.convertCard(ast.getCard2());
      if (role1 != null)
      { g.drawString(role1,(int) role1x - downwards*x1offset,(int) role1y); }
      if (role2 != null)
      { g.drawString(role2,(int) role2x - leftright*x2offset,(int) role2y); }
      g.drawString(c1,(int) card1x,(int) card1y); 
      g.drawString(c2,(int) card2x,(int) card2y);
      g.setFont(new Font("Serif", Font.BOLD, 8));
      g.drawString(ast.getName(),namex,namey);
      g.setFont(new Font("Serif", Font.BOLD, 18));
      if (agg) 
      { Polygon p = new Polygon(); 
        p.addPoint(xstart1,ystart); 
        p.addPoint((int) agg1x, (int) agg1y); 
        p.addPoint((int) agg3x,(int) agg3y); 
        p.addPoint((int) agg2x,(int) agg2y);
        g.fillPolygon(p);
      } // fill it as a polygon
      else if (qualified)
      { String qvar = ast.getQualifier() + ""; 
        int qsize = fm1.stringWidth(qvar) + 5; 
        // q2x = q2x + qsize; 
        // q3x = q3x + qsize; 
        if (direction == LEFTRIGHT)
        { g.drawLine(q1x,q1y,q2x + qsize,q2y); 
          g.drawLine(q2x + qsize,q2y,q3x + qsize,q3y); 
          g.drawLine(q3x + qsize,q3y,q4x,q4y);
          g.drawString(qvar,xstart + 5,ystart + 3);
        } 
        else if (direction == RIGHTLEFT)
        { g.drawLine(q1x,q1y,q2x - qsize,q2y); 
          g.drawLine(q2x - qsize,q2y,q3x - qsize,q3y); 
          g.drawLine(q3x - qsize,q3y,q4x,q4y);
          g.drawString(qvar,xstart1,ystart + 3);
        } 
        else if (direction == DOWNWARDS)
        { g.drawLine(q1x + qsize/2,q1y,q2x + qsize/2,q2y); 
          g.drawLine(q2x + qsize/2,q2y,q3x - qsize/2,q3y); 
          g.drawLine(q3x - qsize/2,q3y,q4x - qsize/2,q4y);
          g.drawString(qvar,xstart - qsize/2,ystart);
        } 
        else 
        { g.drawLine(q1x - qsize/2,q1y,q2x - qsize/2,q2y); 
          g.drawLine(q2x - qsize/2,q2y,q3x + qsize/2,q3y); 
          g.drawLine(q3x + qsize/2,q3y,q4x + qsize/2,q4y);
          g.drawString(qvar,xstart - qsize/2,ystart);
        } 
      } 
      return; 
    }
    
    if (transition != null) 
    { Event e = transition.getEvent(); 
      g.drawString("" + e + transition.guardToString() +
                   transition.genToString(),namex,namey); 
      return; 
    } 

    if (message != null)
    { g.drawString("" + message,namex,namey); 
      return; 
    } 

    if (flow != null) 
    { g.drawString("" + flow.label,namex,namey); 
      return; 
    } 
  }

  void drawData(Graphics g) 
  { if (LineLength() > 5)
    { g.setColor(color);
      // if (linetype == 1)
      // { g.setStroke(dashed); }
    if (waypoints.size() == 0)
    { g.drawLine(xstart,ystart,xend,yend); }
    else 
    { LinePoint p1 = (LinePoint) waypoints.get(0); 
      g.drawLine(xstart,ystart,p1.x,p1.y); 
      for (int i = 1; i < waypoints.size(); i++) 
      { LinePoint p2 = (LinePoint) waypoints.get(i);
        g.drawLine(p1.x,p1.y,p2.x,p2.y); 
        p1 = p2; 
      }
      g.drawLine(p1.x,p1.y,xend,yend); 
    }
      // g.setStroke(stroke);
      g.drawLine((int) arrow1x,(int) arrow1y,xend,yend);
      g.drawLine((int) arrow2x,(int) arrow2y,xend,yend);
      
      if (modelElement != null)
      { Association ast = (Association) modelElement;
        String role1 = ast.getRole1();
        String role2 = ast.getRole2();
        if (ast.isOrdered()) 
        { role2 = role2 + " { ordered }"; }
        if (ast.isFrozen())
        { role2 = role2 + " { frozen }"; } 
        else if (ast.isAddOnly())
        { role2 = role2 + " { addOnly }"; } // and derived
        String c1 = 
          ModelElement.convertCard(ast.getCard1()); 
        String c2 =
          ModelElement.convertCard(ast.getCard2());
        if (role1 != null)
        { g.drawString(role1,xstart+5,ystart-5); }
        if (role2 != null)
        { g.drawString(role2,(int) role2x,(int) role2y); }
        g.drawString(c1,xstart+5,ystart+8); 
        g.drawString(c2,(int) card2x,(int) card2y);
        if (ast.isAggregation()) 
        { Polygon p = new Polygon(); 
          p.addPoint(xstart,ystart); 
          p.addPoint((int) agg1x, (int) agg1y); 
          p.addPoint((int) agg3x,(int) agg3y); 
          p.addPoint((int) agg2x,(int) agg2y);
          g.fillPolygon(p);
        }
        else if (ast.isQualified())
        { String qvar = ast.getQualifier() + ""; 
          // FontMetrics fm1 = g.getFontMetrics();
          int qsize = 2*qvar.length(); // fm1.stringWidth(qvar); 
          // q2x = q2x + qsize; 
          // q3x = q3x + qsize; 
          g.drawLine(q1x,q1y,q2x + qsize,q2y); 
          g.drawLine(q2x + qsize,q2y,q3x + qsize,q3y); 
          g.drawLine(q3x + qsize,q3y,q4x,q4y);
          g.drawString(qvar,xstart,ystart + 3); 
        } 
      }
    }

    if (transition != null) 
    { Event e = transition.getEvent(); 
      g.drawString("" + e + transition.guardToString() +
                   transition.genToString(),namex,namey); 
    } 

    if (message != null)
    { g.drawString("" + message,namex,namey); } 
  } 


  public void setx1(int x1)
  { xstart = x1;
    setName(); 
    setArrows(); 
  }

  public void sety1(int y1)
  { ystart = y1;
    setName(); 
    setArrows(); 
  }

  public void setx2(int x2)
  { xend = x2;
    setName(); 
    setArrows(); 
  }

  public void sety2(int y2)
  { yend = y2;
    setName(); 
    setArrows(); 
  }

  public void changePosition(int oldx, int oldy, int x, int y)   
  { if (selected == STARTSELECTED)
    { xstart = x; 
      ystart = y; 
    } 
    else if (selected == ENDSELECTED) 
    { xend = x; 
      yend = y; 
    } 
    else if (selected == MIDSELECTED)
    { int xmid = (xstart + xend)/2; 
      int ymid = (ystart + yend)/2; 
      xstart = xstart + x - xmid;
      ystart = ystart + y - ymid;
      xend = xend + x - xmid;
      yend = yend + y - ymid;
    }  
    setName(); 
    setArrows(); 
  } 

  public void shrink(double factor) 
  { super.shrink(factor); 
    xstart = (int) (xstart/factor); 
    ystart = (int) (ystart/factor); 
    xend = (int) (xend/factor); 
    yend = (int) (yend/factor);   
    setName(); 
    setArrows(); 
  } 

  public void moveUp(int amount)
  { super.moveUp(amount);
    ystart -= amount;
    yend -= amount;
    for (int i = 0; i < waypoints.size(); i++) 
    { LinePoint p = (LinePoint) waypoints.get(i); 
      p.y = p.y - amount; 
    } 
    setName(); 
    setArrows();
  }

  public void moveDown(int amount)
  { super.moveDown(amount);
    ystart += amount;
    yend += amount;
    for (int i = 0; i < waypoints.size(); i++) 
    { LinePoint p = (LinePoint) waypoints.get(i); 
      p.y = p.y + amount; 
    } 
    setName(); 
    setArrows();
  }

  public void moveLeft(int amount)
  { super.moveLeft(amount);
    xstart -= amount;
    xend -= amount;
    for (int i = 0; i < waypoints.size(); i++) 
    { LinePoint p = (LinePoint) waypoints.get(i); 
      p.x = p.x - amount; 
    } 
    setName(); 
    setArrows();
  }

  public void moveRight(int amount)
  { super.moveRight(amount);
    xstart += amount;
    xend += amount;
    for (int i = 0; i < waypoints.size(); i++) 
    { LinePoint p = (LinePoint) waypoints.get(i); 
      p.x = p.x + amount; 
    } 
    setName(); 
    setArrows(); 
  }

  public boolean isInput() 
  { return linetype == UCDArea.DASHED; } 

  public void setInput(boolean ii) 
  { if (ii) 
    { linetype = UCDArea.DASHED; } 
    else 
    { linetype = UCDArea.SOLID; } 
  } 

  public static LineData 
    drawBetween(RectForm rd1, RectForm rd2,
                int i)
  { int x1 = rd1.getx();
    int y1 = rd1.gety();
    int x2 = rd2.getx();
    int y2 = rd2.gety();
    int deltas = 10; 
    int deltae = 5; 

    int startx, starty, endx, endy;

    if (y1 < y2)
    { startx = x1 + deltae;
      starty = y1 + rd1.height - deltas;
      endx = x2 + deltae;
      endy = y2 + deltae; 
    }
    else if (y2 < y1)
    { startx = x1 + rd1.width - deltas;
      starty = y1 + deltae;
      endx = x2 + rd2.width - deltas;
      endy = y2 + rd2.height - deltas; 
    }
    else if (x1 < x2)
    { startx = x1 + rd1.width - deltas;
      starty = y1 + deltae;
      endx = x2 + deltae;
      endy = y2 + deltae; 
    }
    else if (x2 < x1)
    { startx = x1 + deltae;
      starty = y1 + rd1.height - deltas;
      endx = x2 + rd2.width - deltas;
      endy = y2 + rd2.height - deltas; 
    }
    else 
    { startx = x1 + deltae;
      starty = y1 + deltae;
      endx = x2 + rd2.width - deltas;
      endy = y2 + rd2.height - deltas; 
    } 
    return new LineData(startx, starty,
                        endx, endy, i,
                        UCDArea.DASHED);               
  }
}


class LinePoint 
{ int x; 
  int y; 

  public LinePoint(int xx, int yy)
  { x = xx; 
    y = yy; 
  } 
} 