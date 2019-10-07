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

public class BSetComprehension extends BExpression
{ private String variable;
  private Vector variables = new Vector(); 
  private BExpression range;
  private BExpression predicate;

  public BSetComprehension(String var, String ran,
                           BExpression pred)
  { variable = var;
    range = new BBasicExpression(ran);
    predicate = pred;
  }

  public BSetComprehension(String var, BExpression ran,
                           BExpression pred)
  { variable = var;
    range = ran;
    predicate = pred;
  }

  public BSetComprehension(Vector vars, BExpression pred)
  { variables = vars;
    predicate = pred;
    variable = null;
  }

  public BSetComprehension(Vector vars, BExpression ran, BExpression pred)
  { variables = vars;
    variable = null;
    predicate = pred;
    range = ran;
  }

  public Vector rd()
  { Vector res = predicate.rd(); 
    if (range != null) 
    { res = VectorUtil.union(res,range.rd()); } 
    Vector vars = new Vector(); 
    vars.addAll(variables); 
    vars.add(variable); 
    res.removeAll(vars); 
    return res; 
  } 


  public boolean setValued()
  { return true; } 

  public BExpression simplify()
  { BExpression sp = predicate.simplify();
    if ((sp + "").equals("false"))
    { return new BSetExpression(); } 

    if (variable != null)
    { return new BSetComprehension(variable,range,sp); }
    else 
    { return new BSetComprehension(variables,sp); }  
  }  // pred being false yields empty set, etc. 

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    return this;
  } 

  public String toString() 
  { if (variable != null)
    { return "{ " + variable + " | " + variable + " : " + range + " & " +
             predicate + " }";
    }
    else
    { String res = "{ ";
      for (int i = 0; i < variables.size(); i++)
      { String var = (String) variables.get(i);
        res = res + var;
        if (i < variables.size() - 1)
        { res = res + ","; }
      }
      res = res + " | " + predicate + " }";
      return res;
    }
  }
}
