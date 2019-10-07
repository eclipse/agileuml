import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BTypeDef extends ModelElement
{ private Vector values; // null for given sets

  public BTypeDef(String name, Vector vals)
  { super(name); 
    values = vals;
  }

  public void generateJava(java.io.PrintWriter out) 
  { out.print(getName()); }  // not used. 

  public String toString()
  { String res = getName();
    if (values == null)
    { return res; }  // but not for int, String, boolean, double
    int vc = values.size();
    if (vc == 0)
    { return res; }
    res = res + " = {";
    for (int i = 0; i < vc; i++)
    { res = res + values.get(i);
      if (i < vc - 1)
      { res = res + ", "; }
    }
    return res + "}";
  }
}

