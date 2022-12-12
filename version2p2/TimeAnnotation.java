/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class TimeAnnotation extends InteractionElement
{ UMLObject onObjects = null;  // a vector generally
  Expression time = null;
  Expression condition = null;

  TimeAnnotation(String nme)
  { super(nme);  }

  TimeAnnotation(String nme, UMLObject ox)
  { this(nme);
    onObjects = ox;
  }

  public void setTime(Expression st)
  { time = st; }

  public void setCondition(Expression c)
  { condition = c; }

  public Expression getTime()
  { return time; } 

  public String toString()
  { return "" + time + " " + stereotype; }

  public String display_ta()
  { return stereotype + " time " + time + " on " + onObjects +
         " " + condition; }

  public Expression generateRAL()
  { if (condition != null)
    { time.setBrackets(true);
      condition.setBrackets(true); 
      Expression e = new BinaryExpression("@",condition,
        time);
      return e;
    } 
    return new BasicExpression("true");
  }

  public void setOnObject(UMLObject lifeline)
  { onObjects = lifeline; } 

  public Object clone()
  { TimeAnnotation res = new TimeAnnotation(label,onObjects); 
    res.setTime(time); 
    res.setCondition(condition); 
    res.setStereotype(stereotype); 
    return res; 
  } 
}
