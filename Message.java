import java.io.*;
import java.util.Vector; 
import java.util.StringTokenizer; 

/* Package: Interactions */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class Message extends InteractionElement implements Serializable
{ BehaviouralFeature operation;
  UMLObject source;            
  UMLObject target;             
  Expression sendtime; 
  Expression receivetime; 
  TimeAnnotation ta1; 
  TimeAnnotation ta2; 

  public Message(String id)
  {  super(id); 
     System.out.println("-- Message created");
  }

  public Message(String lab, UMLObject src, 
                              UMLObject targ, BehaviouralFeature ev)
   {  super(lab);
      source = src;
      target = targ;
      operation = ev;
  }

  public void setStereotype(boolean isForall)
  { if (isForall) 
    { setStereotype("<<forall>>"); } 
    else
    { setStereotype(""); } 
  } 

  public String displayMessage()
  { return source + " --- " + label + ": " + operation + " --> " + target; } 

  public String toString()
  { return "" + operation + " " + stereotype + " [" + label + "]"; } 

  public void setOperation(BehaviouralFeature e) 
  { operation = e; } 
 
  public BehaviouralFeature getOperation() 
  { return operation; } 
  
  public void setSource(UMLObject src)
  {
    source = src;
  }

  public UMLObject getSource() 
  { return source; } 

  public void setTarget(UMLObject targ)
  { target = targ;  }

  public UMLObject getTarget() 
  { return target; } 

  public void deleteLifeline(UMLObject s) 
  { if (s == source) 
    { source = null; 
      System.out.println("There is no source for " + label); 
    } 
    if (s == target) 
    { target = null; 
      System.out.println("There is no target for " + label); 
    } 
  } 
      
  public Expression generateRAL()
  { // produces axioms such as <--(target.operation,i) <= -->(target.operation,i)
    if (target == null)
    { return new BasicExpression("true"); } 
    String i = getName() + "_i"; 
    BasicExpression iexp = new BasicExpression(i); 
    BasicExpression m = new BasicExpression(target.label); 
    BasicExpression call = new BasicExpression("" + operation); 
    call.setObjectRef(m); 
    Vector pars = new Vector(); 
    pars.add(call); 
    pars.add(iexp); 
    BasicExpression sendtime = new BasicExpression("<--"); 
    sendtime.setParameters(pars);
    BasicExpression rectime = new BasicExpression("-->"); 
    rectime.setParameters(pars);
    return new BinaryExpression("<=",sendtime,rectime); 
  } 
  
  public Expression getSendEvent()
  { if (target == null)
    { return new BasicExpression("true"); } 
    String i = getName() + "_i"; 
    BasicExpression iexp = new BasicExpression(i); 
    BasicExpression m = new BasicExpression(target.label); 
    BasicExpression call = new BasicExpression("" + operation); 
    call.setObjectRef(m); 
    Vector pars = new Vector(); 
    pars.add(call); 
    pars.add(iexp); 
    BasicExpression sendtime = new BasicExpression("<--"); 
    sendtime.setParameters(pars);
    return sendtime; 
  }     

  public Expression getReceiveEvent()
  { // produces <--(target.operation,i) 
    if (target == null)
    { return new BasicExpression("true"); } 
    String i = getName() + "_i"; 
    BasicExpression iexp = new BasicExpression(i); 
    BasicExpression m = new BasicExpression(target.label); 
    BasicExpression call = new BasicExpression("" + operation); 
    call.setObjectRef(m); 
    Vector pars = new Vector(); 
    pars.add(call); 
    pars.add(iexp); 
    BasicExpression rectime = new BasicExpression("-->"); 
    rectime.setParameters(pars);
    return rectime; 
  } 
  
  public void dispSrcTarg()
  { System.out.println(); 
    System.out.println("MESSAGE ==> " + label);
    if (source == null)
      {
	System.out.println("There is no source for " + label);
      }
    else
      {
	System.out.println("---source ==>" + source.label);
	
      }
    if (target == null)
      {
	System.out.println("There is no target for " + label);
      }
    else
      {	
	System.out.println("---target ==>" + target.label);
      }
  }

  public void display_transition()
  {  System.out.println(source.label + " -- " + 
                        label + " : " + operation.getName() + " -> " + target.label); 
  }

  public Object clone() 
  { Message res = new Message(label,source,target,
                                            operation); 
    return res; 
  } 


}
