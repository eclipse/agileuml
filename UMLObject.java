/******************************
* Copyright (c) 2003-2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*; 

class UMLObject extends Named
{ Entity classifier = null;
  boolean terminates = false; 

  UMLObject(String nme)
  { super(nme);  }

  UMLObject(String nme, Entity e)
  { this(nme);
    classifier = e;
  }

  public void setTerminates(boolean b)
  { terminates = b; } 

  public boolean getTerminates()
  { return terminates; } 

  public Object clone()
  { UMLObject res = new UMLObject(getName(),classifier); 
    res.setTerminates(terminates); 
    return res; 
  } 

  public String toString()
  { return label + ": " + classifier; } 

  public Entity getClassifier()
  { return classifier; } 

  public void setClassifier(Entity e)
  { classifier = e; } 

  public void saveModelData(PrintWriter out)
  { String name = label; 

    out.println(name + " : UMLObject"); 
    if (classifier != null) 
    { out.println(name + ".classifier = " + classifier.getName()); }
    out.println(name + ".terminates = " + terminates);   
  } 

}
