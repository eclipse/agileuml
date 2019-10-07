/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class LoopNode extends StructuredActivityNode
{ boolean isTestedFirst = false; // but actually it is
  ExecutableNode setupPart;      // not used
  ExecutableNode test; 
  ExecutableNode bodyPart; 

  public LoopNode(String nme, boolean itf)
  { super(nme); 
    isTestedFirst = itf; 
  } 

  public Statement toStatement()
  { Statement bodycode = bodyPart.toStatement(); 
    Statement result = new WhileStatement(null, bodycode); 
    return result; 
  } 

  public void generateJava(java.io.PrintWriter out) { } 

  public static void main(String[] args)
  { System.out.println(Double.parseDouble("NaN")); } 
} 