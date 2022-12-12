/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* Package: class diagram */

import java.io.*;
import java.util.Vector; 

abstract public class NamedElement 
{
  String name;

  public NamedElement(String id)
  { name = id; } 

  public String getName()
  { return name; } 

  // public abstract Object clone(); 

  public static int indexByName(String nme, Vector mes)
  { for (int i = 0; i < mes.size(); i++) 
    { NamedElement me = (NamedElement) mes.get(i); 
      if (me.getName().equals(nme))
      { return i; } 
    } 
    return -1; 
  } 

  public static NamedElement findByName(String nme, Vector mes)
  { for (int i = 0; i < mes.size(); i++) 
    { NamedElement me = (NamedElement) mes.get(i); 
      if (me.getName().equals(nme))
      { return me; } 
    } 
    return null; 
  } 

}
