import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class TransitionDefinition extends Named
{ String sourceName;
  String eventName;
  State target;
  Expression condition = null;
  Expression effect = null;
  static int count = 0;

  TransitionDefinition(String nme, 
                       String src, 
                       String ev,
                       State targ)
  { super(nme);
    count++;
    sourceName = src;
    eventName = ev;
    target = targ; 
  }

  TransitionDefinition(String ev, 
                       Expression cond,
                       Expression eff)
  { super("Trans" + count);
    count++; 
    eventName = ev;
    sourceName = null;
    target = null;
    condition = cond;
    effect = eff;
  }

  public Object clone()  // minimal def. 
  { return new TransitionDefinition(label,sourceName,eventName,target); } 

  public void setCondition(Expression cond)
  { condition = cond; }

  public void setEffect(Expression act)
  { effect = act; }

  public String toString(String eVar, 
                         String sVar)
  { String res = "  " + label +
                 " := " + eVar + 
                 " = " + eventName;
    if (sourceName == null) 
    { } // Don't print in this case! 
    else 
    { res = res + " & " + sVar + " = " + 
            sourceName; }
    if (condition == null ||
        condition.equalsString("true"))
    { return res + ";"; }
    else 
    { return res + " & " +
             condition.toString() + ";"; 
    } 
    // return res + ";"; 
  }

  public void display(String eVar, String sVar)
  { if (sourceName == null)
    { } 
    else 
    { System.out.println(toString(eVar,sVar)); }
  }

  public void display(String eVar, 
                      String sVar, PrintWriter out)
  { if (sourceName == null)
    { } 
    else 
    { out.println(toString(eVar,sVar)); }
  }
}













