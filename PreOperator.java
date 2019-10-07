import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class PreOperator
{ String name; 
  String type; 
  String javacode;
  String csharpcode; 

  public PreOperator(String line1, String line2, String line3)
  { int i = line1.indexOf(' '); 
    name = line1.substring(0,i).trim();
    type = line1.substring(i,line1.length()).trim();  
    javacode = line2; 
    csharpcode = line3; 
  } 
} 
