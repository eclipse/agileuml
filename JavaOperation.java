/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class JavaOperation
{ String opname;
  String inpars = "";
  String outpars = "";
  String defn = "";

  JavaOperation(String nme)
  { opname = nme; }

  public void setinpars(String prs)
  { inpars = prs; }

  public void setoutpars(String prs)
  { outpars = prs; }

  public void setdefn(String dfn)
  { defn = dfn; }

  public String toString()
  { String res = "public " + outpars + " " + opname +
        "(" + inpars + ")\n" +
        "  { " + defn + " }";
    return res;
  }
}
