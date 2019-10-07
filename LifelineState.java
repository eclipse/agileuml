/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class LifelineState extends BasicState
{ UMLObject executesOn = null;  // a vector generally
  Expression startTime = null;
  Expression endTime = null;

  LifelineState(String nme)
  { super(nme);  }

  LifelineState(String nme, UMLObject ox)
  { this(nme);
    executesOn = ox;
  }

  public void setStartTime(Expression st)
  { startTime = st; }

  public void setEndTime(Expression st)
  { endTime = st; }

  public String display_ls()
  { return "state " + getInvariant() + " on " + executesOn + 
           " from " + startTime + " to " + endTime; 
  } 

  public String toString()
  { return "[" + getInvariant() + "]"; } 

  public Expression generateRAL()
  { Expression invariant = getInvariant(); 
    Expression e1 = new BinaryExpression("@",invariant,startTime);
    Expression e2 = new BinaryExpression("@",invariant,endTime);
    return new BinaryExpression("&",e1,e2);
  }

  /* public Expression generateRTL()
  { String i = trigger.label;
    BasicExpression iexp = new BasicExpression(i);
    BasicExpression obj = 
        new BasicExpression("" + executesOn);
    BasicExpression call =
        new BasicExpression("" + operation);
    call.setObjectRef(obj);
    Vector pars = new Vector();
    pars.add(call);
    pars.add(iexp);
    BasicExpression actTime =         
      new BasicExpression("/|\");
    actTime.setParameters(pars);
    BasicExpression finTime =         
      new BasicExpression("\|/");
    actTime.setParameters(pars);
    return new BinaryExpression("<=",actTime,finTime);
  }  */

  public BasicState my_initial()
  { return this; } 

}
