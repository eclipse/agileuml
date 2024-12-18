/*
      * Classname : Flow
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the logical classes for the 
      * input/output flow (line) between the components in the DCFD diagram.
*/

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*;
import java.util.Vector;

class Flow extends Named
{
  VisualData source;
  VisualData target;
  // Vector events = new Vector();
 
  Flow(String name) 
  { super(name); }  
  
  Flow(String name, VisualData first, 
       VisualData second)
  { super(name); 
    source = first;
    target = second;
  }

  public Object clone()
  { return new Flow(label,source,target); } 

  public void setSource(RectForm rd) 
  { source = rd; } 

  public void setTarget(RectForm rd) 
  { target = rd; } 
  
  public VisualData getSource()
  {
    return source;
  }

  public VisualData getTarget()
  {
    return target;
  }

  // public void addEvent(Event e) 
  // { events.add(e); } 
 

  public void display_flow()
  {
    System.out.println("Flow from " + source.label + "to " + target.label);
  }
}
