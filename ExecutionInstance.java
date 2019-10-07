import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class ExecutionInstance extends InteractionElement
{ Entity classifier = null;
  UMLObject executesOn = null;
  BehaviouralFeature operation = null;
  Message trigger = null;
  Expression startTime = null;
  Expression endTime = null;

  ExecutionInstance(String nme)
  { super(nme);  }

  ExecutionInstance(String nme, UMLObject ox)
  { this(nme);
    executesOn = ox;
    classifier = ox.classifier;
  }

  public void setMessage(Message m)
  { trigger = m;
    setLabel(m.label); 
    operation = m.getOperation();
  }

  public void setStartTime(Expression st)
  { startTime = st; }

  public void setEndTime(Expression st)
  { endTime = st; }

  public Object clone()
  { ExecutionInstance res = new ExecutionInstance(label,executesOn); 
    if (trigger != null)
    { res.setMessage(trigger); }
    res.setStartTime(startTime); 
    res.setEndTime(endTime); 
    return res; 
  }  

  public String display_ei()
  { return "execution " + label + " of " + trigger + 
           " from " + startTime + " to " + endTime + " on " + executesOn; 
  } 

  public String toString()
  { if (trigger != null) 
    { return trigger.operation + " [" + label + "]"; }
    return "[" + label + "]"; 
  } 

  public Expression generateRAL()
  { if (trigger == null) 
    { return new BasicExpression("true"); } 
    String i = trigger.label;
    BasicExpression iexp = new BasicExpression(i + "_i");
    BasicExpression obj = 
        new BasicExpression(executesOn.label);
    BasicExpression call =
        new BasicExpression("" + operation);
    call.setObjectRef(obj);
    Vector pars = new Vector();
    pars.add(call);
    pars.add(iexp);
    BasicExpression actTime =         
      new BasicExpression("/|\\");
    actTime.setParameters(pars);
    BasicExpression finTime =         
      new BasicExpression("\\|/");
    finTime.setParameters(pars);
    return new BinaryExpression("<=",actTime,finTime);
  }

  public Expression getStartEvent()
  { if (trigger == null) 
    { return new BasicExpression("true"); } 
    String i = trigger.label;
    BasicExpression iexp = new BasicExpression(i + "_i");
    BasicExpression obj = 
        new BasicExpression(executesOn.label);
    BasicExpression call =
        new BasicExpression("" + operation);
    call.setObjectRef(obj);
    Vector pars = new Vector();
    pars.add(call);
    pars.add(iexp);
    BasicExpression actTime =         
      new BasicExpression("/|\\");
    actTime.setParameters(pars);
    return actTime; 
  } 

  public Expression getEndEvent()
  { if (trigger == null) 
    { return new BasicExpression("true"); } 
    String i = trigger.label;
    BasicExpression iexp = new BasicExpression(i + "_i");
    BasicExpression obj = 
        new BasicExpression(executesOn.label);
    BasicExpression call =
        new BasicExpression("" + operation);
    call.setObjectRef(obj);
    Vector pars = new Vector();
    pars.add(call);
    pars.add(iexp);
    BasicExpression finTime =         
      new BasicExpression("\\|/");
    finTime.setParameters(pars);
    return finTime;
  }

}
