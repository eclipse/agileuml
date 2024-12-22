import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Activities */ 

public class MergeNode extends ControlNode
{ public MergeNode(String nme) 
  { super(nme); }

  public void generateJava(PrintWriter pw)
  { } 

  public Vector getParameters()
  { return new Vector(); } 

  public void addParameter(Attribute att)
  { } 

  public Type getType()
  { return null; } 

  public void setType(Type t)
  { } 
}  