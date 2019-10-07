/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Utilities */ 

public class Identifier
{ private static int count = 0;

  public static String nextIdentifier(String pfx)
  { String res = pfx + count;
    count++;
    return res;
  }
}

