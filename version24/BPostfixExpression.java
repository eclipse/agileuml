import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BPostfixExpression extends BExpression
{ private String operator;
  private BExpression argument;

  public BPostfixExpression(String op, BExpression arg)
  { operator = op;
    argument = arg;
  }

  public boolean setValued()
  { if (operator.equals("~"))
    { return true; }
    return false;   //  
  }

  public Vector rd()
  { return argument.rd(); } 

  public BExpression simplify()
  { BExpression b1 = argument.simplify();
    if (b1 instanceof BPostfixExpression)
    { BPostfixExpression pf = (BPostfixExpression) b1; 
      if (pf.operator.equals("~"))
      { return pf.argument; } 
    } 
    return new BPostfixExpression(operator,b1); 
  } 

  public String toString() 
  { return "(" + argument + ")" + operator; }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    return new BPostfixExpression(operator,
                                argument.substituteEq(oldE,newE)); 
  }
}

