/*
      * Classname : ACLineData
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : Contains methods for drawing 
      * lines for the 
      * association classes
      */
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* Package: GUI */ 

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Vector;

public class ACLineData extends LineData
{  // coordinates of start and end points of the line
  
  RectData myclass; 
  
  final static BasicStroke solid = stroke; 
  
  public ACLineData(int xs, int ys, int xe, 
                    int ye, int linecount, int type, RectData rd) 
  { super(xs,ys,xe,ye,linecount,type); 
    myclass = rd; 
  }

  public Object clone() 
  { String cnt = label.substring(1,label.length()-1); 
    int count = 0; 
    try { count = Integer.parseInt(cnt); } 
    catch (Exception e) 
    { count = 0; } 

    ACLineData ld = 
      new ACLineData(xstart, ystart, xend, 
                     yend, count, linetype, myclass); 
    ld.setTransition(transition);
    ld.setFlow(flow); 
    ld.setModelElement(modelElement); 
    return ld; 
  } 

  private void drawClass(Graphics2D g)
  { // draw dashed line down from midpoint, to a class
    int midx = (xstart + xend)/2; 
    int midy = (ystart + yend)/2; 
    g.setStroke(dashed); 
    g.drawLine(midx,midy,midx,midy+25); 
    g.setStroke(solid); 
    if (myclass != null)
    { myclass.changePosition(0,0,midx - 10, midy+25); 
      myclass.drawData(g); 
    }
  } 

  private void drawClass(Graphics g)
  { // draw dashed line down from midpoint, to a class
    int midx = (xstart + xend)/2; 
    int midy = (ystart + yend)/2; 
    // g.setStroke(dashed); 
    g.drawLine(midx,midy,midx,midy+25); 
    // g.setStroke(solid); 
    if (myclass != null)
    { myclass.changePosition(0,0,midx - 10, midy+25); 
      myclass.drawData(g); 
    }
  }

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
        boolean ord = ast.isOrdered();
        if (ord) { role2 = role2 + " { ordered }"; }
        if (ast.isFrozen())
        { role2 = role2 + " { frozen }"; } 
        else if (ast.isAddOnly())
        { role2 = role2 + " { addOnly }"; } // and derived
        if (ast.isDerived())
        { role2 = "/" + role2; } 
        FontMetrics fm = g.getFontMetrics();
        int xoffset = fm.stringWidth(role2); 
        String c1 =
          ModelElement.convertCard(ast.getCard1()); 
        String c2 =
          ModelElement.convertCard(ast.getCard2());
        if (role1 != null)
        { g.drawString(role1,xstart+5,ystart-5); }
        if (role2 != null)
        { g.drawString(role2,(int) role2x - leftright*xoffset,(int) role2y); }
        g.drawString(c1,(int) card1x,(int) card1y); 
        g.drawString(c2,(int) card2x,(int) card2y);
        g.drawString(ast.getName(), (int) (xend + xstart)/2, 
                                    (int) (yend + ystart)/2);
        if (myclass != null)
        { drawClass(g); } 
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
        if (myclass != null)
        { drawClass(g); } 
      }
    }
  } 
}


