import java.util.Vector; 

/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Activities */ 

public class CallOperationAction extends ActivityAction
{ BehaviouralFeature operation; 

  public CallOperationAction(String nme)
  { super(nme); } 

  public Statement toStatement()
  { return new InvocationStatement(operation); } 

  public void generateJava(java.io.PrintWriter out) { }

  public Vector getParameters()
  { return new Vector(); } 

  public void addParameter(Attribute att)
  { } 

  public Type getType()
  { return null; } 

  public void setType(Type t)
  { }  
} 