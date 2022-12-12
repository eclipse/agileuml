import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class VarAnalInfo extends Named
{ Vector allvars = new Vector();
  Vector locals = new Vector();
  Vector foreign = new Vector();

  VarAnalInfo(String nme, Vector allvs)
  { super(nme);
    allvars = allvs; 
  }

  public void setLocals(Vector locs)
  { locals = locs; }

  public Vector getLocals()
  { return (Vector) locals.clone(); }

  public void addLocal(String var)
  { locals.add(var); }

  public Object clone()
  { VarAnalInfo res = new VarAnalInfo(label,allvars); 
    res.setLocals(locals); 
    res.foreign = (Vector) foreign.clone(); 
    return res; 
  } 
}

