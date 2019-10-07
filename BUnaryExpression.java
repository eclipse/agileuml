import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BUnaryExpression extends BExpression
{ private String operator;
  private BExpression argument;

  public BUnaryExpression(String op, BExpression arg)
  { operator = op;
    argument = arg;
  }

  public boolean setValued()
  { if (operator.equals("union") ||
        operator.equals("ran") || operator.equals("sort") ||
        operator.equals("reverse") ||
        operator.equals("conc"))
    { return true; }
    if (argument.setValued())
    { if (operator.equals("front") || operator.equals("tail"))
      { return true; } 
    } 
    return false;   //  
  }

  public Vector rd()
  { return argument.rd(); } 

  public BExpression simplify()
  { BExpression b1 = argument.simplify();
    if (operator.equals("union") && 
        b1 instanceof BSetExpression)
    { BSetExpression bs = (BSetExpression) b1;
      if (bs.isSingleton())
      { BExpression elem = bs.getElement(0);
        return elem;
      }
      if (bs.isEmpty())
      { return new BSetExpression(new Vector()); }
    }
    return new BUnaryExpression(operator,b1); 
  } // and simplifications of last, first, front, tail, reverse, if a literal
    // sequence

  public String toString() 
  { return operator + "(" + argument + ")"; }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    return new BUnaryExpression(operator,
                                argument.substituteEq(oldE,newE)); 
  }
}

