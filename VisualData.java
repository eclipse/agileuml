/*
      * Classname : VisualData
      *  
      * Version information : 1
      *
      * Date
      * 
      * Description : It is an abstract class for all the elements that are 
      * drawn in the panel: classes and associations.

  package: Class Diagram Editor
  Subclasses: LineData, OvalData, RectForm 
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

abstract class VisualData extends Named 
{ protected ModelElement modelElement; 
  protected Color color; //the colour of the element
  protected int namex;
  protected int namey; 

  public VisualData(String name,Color c) 
  { super(name); 
    color = c;
  } 

  public void setModelElement(ModelElement e)
  { modelElement = e; }

  public ModelElement getModelElement()
  { return modelElement; }

  public void setColour(Color c)
  { color = c; } 

  // public Object clone() 
  // { VisualData vd = new VisualData(label); 
  //   vd.color = color; 
  //   vd.namex = namex; 
  //   vd.namey = namey; 
  //   return vd; 
  // } 

	//to get the x coordinate
  abstract int getx();

	//to get the y coordinate
  abstract int gety();

	//draws the new element 
  abstract void drawData(Graphics2D g);

  public void drawCDData(Graphics2D g)
  { VisualData vd = getCDVisual(); 
    vd.drawData(g); 
  } 

  abstract void drawData(Graphics g); 

  public VisualData getCDVisual()
  { return this; }   // default definition.  

  abstract boolean isUnder(int x, int y);

  abstract boolean isUnderStart(int x, int y); 

  abstract boolean isUnderEnd(int x, int y); 

  public void setName(String n) 
  { label = n; } 

  abstract public void changePosition(int oldx, int oldy, int x, int y);  

  public void shrink(double factor)
  { namex = (int) (namex/factor); 
    namey = (int) (namey/factor); 
  }  

  public void moveUp(int amount)
  { namey -= amount; }

  public void moveDown(int amount) 
  { namey += amount; } 

  public void moveLeft(int amount)
  { namex -= amount; } 

  public void moveRight(int amount) 
  { namex += amount; } 
}



