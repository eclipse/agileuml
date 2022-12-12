import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BidirectionalAssociation extends ModelElement
{ private Association e1e2; 
  private Association e2e1; 

  public BidirectionalAssociation(Entity e1, Entity e2, int c1, int c2, 
                                  String r1, String r2)
  { super(e1.getName() + "_" + e2.getName()); 
    e1e2 = new Association(e1,e2,c1,c2,r1,r2); 
    e2e1 = new Association(e2,e2,c2,c1,r2,r1); 
  }

  public void generateJava(PrintWriter out) { } 

}

