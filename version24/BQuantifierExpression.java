/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 

public class BQuantifierExpression extends BExpression
{ private String quantifier;
  private String var = null;
  private Vector pars = null; 
  private BExpression argument;

  public BQuantifierExpression(String q, String v,
                               BExpression pred)
  { quantifier = q;
    var = v;
    argument = pred;
  }

  public BQuantifierExpression(String q, Vector v,
                               BExpression pred)
  { quantifier = q;
    var = null; 
    pars = v; 
    argument = pred;
  }

  public Vector rd()
  { Vector res = argument.rd(); 
    Vector vars = new Vector(); 
    vars.add(var); 
    res.removeAll(vars); 
    return res; 
  } 

  public String toString()
  { String res = "";
    if (quantifier.equals("forall"))
    { res = "!"; }
    else 
    { res = "#"; }
    if (var != null)
    { res = res + var + ".(" + argument + ")"; }
    else if (pars != null) 
    { res = res + "("; 
      for (int i = 0; i < pars.size(); i++) 
      { res = res + pars.get(i); 
        if (i < pars.size() - 1)
        { res = res + ","; } 
      } 
      res = res + ").(" + argument + ")"; 
    } 
    return res;
  }

  public boolean setValued()
  { return false; } 

  public BExpression simplify()
  { BExpression newarg = argument.simplify();
    if ((newarg + "").equals("true") || 
        (newarg + "").equals("false"))
    { return newarg; } 
    if (var != null) 
    { return new BQuantifierExpression(quantifier,
                                       var,newarg);
    } 
    return new BQuantifierExpression(quantifier,pars,newarg);  
  }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    BExpression newArg = argument.substituteEq(oldE,newE); 
    if (var != null && oldE.equals(var)) // var doesn't occur in newArg, assume ...
    { return newArg; } 
    if (var != null) 
    { return new BQuantifierExpression(quantifier,var,newArg); }
    return new BQuantifierExpression(quantifier,pars,newArg);  
  } 

}

