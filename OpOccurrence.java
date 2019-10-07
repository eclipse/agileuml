/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class OpOccurrence
{ String op; 
  int location;
  int bcount; 

  public OpOccurrence(String o, int l, int b)
  { op = o; location = l; bcount = b; } 

  public String toString()
  { return "Op: " + op + " " + location + " " + bcount; }

  public int priority() 
  { if ("<=>".equals(op)) { return 0; }
    if ("=>".equals(op)) { return 1; }
    if ("#".equals(op)) { return 2; }
    if ("or".equals(op)) { return 3; }
    if ("&".equals(op)) { return 4; }
    return -1; 
  }
} 