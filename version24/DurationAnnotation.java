/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


class DurationAnnotation extends InteractionElement
{ TimeAnnotation startTime = null;  
  TimeAnnotation endTime = null;  
  Expression lower = null;
  Expression upper = null;

  DurationAnnotation(String nme,TimeAnnotation t1,
             TimeAnnotation t2)
  { super(nme);  
    startTime = t1;
    endTime = t2;
  }

  DurationAnnotation(String nme)
  { super(nme); } 

  public void setLower(Expression lb)
  { lower = lb; }

  public void setUpper(Expression ub)
  { upper = ub; }

  public Expression generateRAL()
  { if (endTime == null || startTime == null) 
    { return new BasicExpression("true"); } 

    Expression diff = 
      new BinaryExpression("-",endTime.time,startTime.time);
    Expression e1 = new BinaryExpression("<=",diff,upper);
    Expression e2 = new BinaryExpression("<=",lower,diff);
    return new BinaryExpression("&",e1,e2);
  }

  public Expression generateRTL()
  { return generateRAL(); }

  public String toString()
  { return "{" + lower + ".." + upper + "}"; }

  public String display_da()
  { return startTime + " to " + endTime + 
       " with bounds " + toString();
  }

  public Object clone()
  { DurationAnnotation da = new DurationAnnotation(label,startTime,endTime); 
    da.setLower(lower); 
    da.setUpper(upper); 
    return da; 
  } 
}
