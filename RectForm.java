/*
      * Classname : RectForm
      * 
      * Version information : 1
      *
      * Date
      * 
      * Description : General rectangular forms. 
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
import java.util.Vector;

abstract class RectForm extends VisualData
{ protected int sourcex, sourcey; 
  protected int width, height; 

  RectForm(String s, Color c, int xx, int yy) 
  { super(s,c); 
    sourcex = xx; 
    sourcey = yy; 
  } 

  RectForm(String s, int xx, int yy)
  { super(s,Color.black);
    sourcex = xx;
    sourcey = yy;
  }

  int getx()
  { return sourcex; }

  int gety()
  { return sourcey; }

  public void shrink(double factor)
  { sourcex = (int) (sourcex/factor); 
    sourcey = (int) (sourcey/factor); 
    width = (int) (width/factor); 
    height = (int) (height/factor); 
    super.shrink(factor); 
  } 

  public void moveUp(int amount) 
  { super.moveUp(amount); 
    sourcey -= amount; 
  } 

  public void moveDown(int amount)
  { super.moveDown(amount); 
    sourcey += amount; 
  }

  public void moveLeft(int amount)
  { super.moveLeft(amount); 
    sourcex -= amount; 
  }

  public void moveRight(int amount)
  { super.moveRight(amount); 
    sourcex += amount; 
  }

  public void extendTo(int x, int y)
  { int w = x - sourcex; 
    int h = y - sourcey; 
    width = w; 
    height = h; 
  } 

  public boolean isInside(RectForm rf)
  { if (sourcex >= rf.sourcex && 
        sourcey >= rf.sourcey &&
        sourcex + width <= rf.sourcex + rf.width && 
        sourcey + height <= rf.sourcey + rf.height)
    { return true; }
    return false;
  }  
}



