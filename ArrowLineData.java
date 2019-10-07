/*
      * Classname : LineData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing 
      * lines for the 
      * associatios
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

public class ArrowLineData extends VisualData
{  // coordinates of start and end points of the line
  int xstart, xend; 
  int ystart, yend;

  Transition transition; 
  Flow flow;   /* One of these is null usually */ 
  
  //coordinates for the arrow head
  double arrow1x, arrow1y; 
  double arrow2x, arrow2y; 

  // coordinates for roles and cards
  double card1x, card1y; 
  double card2x, card2y; 
  double role1x, role1y; 
  double role2x, role2y; 

  double length; //length of the line
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

  public ArrowLineData(int xs, int ys, int xe, 
                  int ye, int linecount, int type) 
  { super(new String("t" + linecount),Color.black); 

    xstart = xs; 
    ystart = ys; 
    xend = xe; 
    yend = ye; 
    linetype = type;
    
    // This places the name in the middle of the line
    namex = (xstart + xend)/2; 
    namey = (ystart + yend)/2;  
    setArrows(); 
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

    LineData ld = 
      new LineData(xstart, ystart, xend, 
                   yend, count, linetype); 
    ld.setTransition(transition);
    ld.setFlow(flow); 
    ld.setModelElement(modelElement); 
    return ld; 
  } 

  public void setTransition(Transition t) 
  { transition = t; } 

  public void setFlow(Flow fl) 
  { flow = fl; } 

  // returns x position of first point of the line
  int getx()
  { return xstart; }
  
  // returns y position of the first point of line
  int gety()
  { return ystart; }

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

    res = ((int) Math.sqrt((x - midx)*(x - midx) +
                           (y - midy)*(y - midy)) < 10);
    if (res) { selected = MIDSELECTED; } 
    return res;  
  }

  boolean isUnderStart(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xstart)*(x - xstart) + 
                       (y - ystart)*(y - ystart)) < 10); 
    if (res) { selected = STARTSELECTED; } 
    return res; 
  } 

  boolean isUnderEnd(int x, int y) 
  { boolean res = 
      ((int) Math.sqrt((x - xend)*(x - xend) + 
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

  void setArrows() 
  { // The vector from start to end of line
    double xvect = xend - xstart;
    double yvect = yend - ystart;

    // The length of the line drawn
    length = Math.sqrt((xvect*xvect)+ (yvect*yvect));
    double xdir = xvect/length ;
    double ydir = yvect/length;
    arrow1x = xend - (5*xdir) - (5*ydir);
    arrow1y = yend - (5*ydir) + (5*xdir);

    arrow2x = xend - (5*xdir) + (5*ydir);
    arrow2y = yend - (5*ydir) - (5*xdir);

    role2x = xend - (10*xdir) - (8*ydir);
    role2y = yend - (8*ydir) + (10*xdir);
    card2x = xend - (10*xdir) + (8*ydir);
    card2y = yend - (8*ydir) - (10*xdir);
  } 
  // Base the roles and cards on these positions as well? 

  void drawData(Graphics2D g) 
  { if (LineLength() > 5) 
    { g.setColor(Color.black); 
      if (linetype == 1)
      { g.setStroke(dashed); }
      g.drawLine(xstart,ystart,xend,yend);
      g.setStroke(stroke);
      g.draw(new Line2D.Double(arrow1x,arrow1y,
             (double) xend,(double) yend));
      g.draw(new Line2D.Double(arrow2x,arrow2y,
             (double) xend,(double) yend)); 
       
      if (modelElement != null) 
      { Association ast = (Association) modelElement;
        String role1 = ast.getRole1();
        String role2 = ast.getRole2();
        String c1 = 
          ModelElement.convertCard(ast.getCard1()); 
        String c2 =
          ModelElement.convertCard(ast.getCard2());
        if (role1 != null)
        { g.drawString(role1,xstart+5,ystart-5); }
        if (role2 != null)
        { g.drawString(role2,(int) role2x,(int) role2y); }
        g.drawString(c1,xstart+5,ystart+10); 
        g.drawString(c2,(int) card2x,(int) card2y);
      }
    }
  }

  void drawData(Graphics g) 
  { if (LineLength() > 5)
    { g.setColor(Color.black);
      // if (linetype == 1)
      // { g.setStroke(dashed); }
      g.drawLine(xstart,ystart,xend,yend);
      // g.setStroke(stroke);
      g.drawLine((int) arrow1x,(int) arrow1y,xend,yend);
      g.drawLine((int) arrow2x,(int) arrow2y,xend,yend);
      
      if (modelElement != null)
      { Association ast = (Association) modelElement;
        String role1 = ast.getRole1();
        String role2 = ast.getRole2();
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
      }
    }
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
    namex = (xstart + xend)/2;
    namey = (ystart + yend)/2;
    setArrows(); 
  } 

  public void shrink(int factor) 
  { super.shrink(factor); 
    xstart = xstart/factor; 
    ystart = ystart/factor; 
    xend = xend/factor; 
    yend = yend/factor;   
    setArrows(); 
  } 

  public void moveUp(int amount)
  { super.moveUp(amount);
    ystart -= amount;
    yend -= amount;
    setArrows();
  }

  public void moveDown(int amount)
  { super.moveDown(amount);
    ystart += amount;
    yend += amount;
    setArrows();
  }

  public void moveLeft(int amount)
  { super.moveLeft(amount);
    xstart -= amount;
    xend -= amount;
    setArrows();
  }

  public void moveRight(int amount)
  { super.moveRight(amount);
    xstart += amount;
    xend += amount;
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


