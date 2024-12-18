import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class PreConstraint
{ Expression cond0; 
  Expression cond; 
  Expression succ; 
  Vector assocs = new Vector(); // String
  Expression orderedBy; 

  public PreConstraint(Expression c0, Expression c, Expression s, 
                       Vector rs, Expression ob)
  { cond0 = c0; 
    cond = c; 
    succ = s; 
    assocs = rs; 
    orderedBy = ob; 
  } 

  public String toString()
  { return cond0 + " & " + cond + " => " + succ + " ON " + assocs + " orderedBy: " + orderedBy; } 
} 
