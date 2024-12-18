/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*; 

abstract class AbstractOpSpec
{ String prefix = "";

  public void setPrefix(String pre) 
  { prefix = pre; } 

  public void display()
  { display(new PrintWriter(System.out)); }  

  public void displayImp()
  { displayImp(new PrintWriter(System.out)); }  

  public void displayJava()
  { displayJava(new PrintWriter(System.out)); }  

  abstract public void display(PrintWriter out); 

  abstract public void displayImp(PrintWriter out); 

  abstract public void displayJava(PrintWriter out); 

  public void displayJavaImp()
  { displayJava(); } 

  public void displayJavaImp(PrintWriter out)
  { displayJava(out); }

  public void simplify() { }  // default implementation 
}

