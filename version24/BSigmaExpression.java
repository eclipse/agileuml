import java.util.Vector; 

/* Package: B AMN */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class BSigmaExpression extends BExpression
{ private String var;
  private BExpression pred;
  private BExpression value;
  // SIGMA(var).(pred | value)

  public BSigmaExpression(String v, BExpression p,
                          BExpression val)
  { var = v;
    pred = p;
    value = val;
  }

  public boolean setValued()
  { return false; } 

  public String toString()
  { return "SIGMA(" + var + ").(" + pred + " | " +
           value + ")";
  }

  public Vector rd()
  { Vector res = pred.rd(); 
    res = VectorUtil.union(res,value.rd()); 
    res.remove(var); 
    return res; 
  } 

  public BExpression simplify()
  { BExpression p2 = pred.simplify(); 
    BExpression v2 = value.simplify(); 
    return new BSigmaExpression(var,p2,v2); 
  } 

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (var.equals(oldE)) { return this; } 
    // also variable capture ...
    if (oldE.equals(toString()))
    { return newE; } 
    
    BExpression npred = pred.substituteEq(oldE,newE); 
    BExpression nval = value.substituteEq(oldE,newE); 
    return new BSigmaExpression(var,npred,nval); 
  } 
}

