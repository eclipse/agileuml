import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public abstract class ActivityNode extends ModelElement
{ Vector incoming = new Vector(); // of ActivityEdge
  Vector outgoing = new Vector(); // of ActivityEdge

  public ActivityNode(String nme)
  { super(nme); } 
} 
