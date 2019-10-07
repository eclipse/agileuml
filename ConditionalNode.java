import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ConditionalNode extends StructuredActivityNode
{ Vector clause = new Vector(); // of Clause

  public ConditionalNode(String nme)
  { super(nme); } 

  public Statement toStatement()
  { return null; } 

  public void generateJava(java.io.PrintWriter out) { } 
} 