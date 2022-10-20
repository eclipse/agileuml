/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 


public class ValueMatching
{ Expression src;
  Expression trg;

  public ValueMatching(Expression s, Expression t)
  { src = s;
    trg = t;
  }

  public ValueMatching(String s, String t)
  { src = new BasicExpression(s);
    trg = new BasicExpression(t);
  }

  public String toString()
  { return src + " |--> " + trg; }

  public boolean isIdentityMapping() 
  { String ss = (src + "").trim(); 
    String tt = (trg + "").trim(); 
    if ("_1".equals(ss) && "_1".equals(tt))
    { return true; } 
    if ("_0".equals(ss) && "_0".equals(tt))
    { return true; } 
    if (ss.startsWith("\"") && ss.endsWith("\"") && 
        tt.startsWith("\"") && tt.endsWith("\"") && 
        ss.substring(1,ss.length()-1).trim().equals(
             tt.substring(1,tt.length()-1).trim()))
    { System.out.println(">>> Identity mapping: " + ss + " |--> " + tt); 
      return true;
    } 
    return false; 
  } 

  public ValueMatching invert()
  { return new ValueMatching(trg,src); }

  public boolean equals(Object obj)
  { if (obj instanceof ValueMatching)
    { ValueMatching vm = (ValueMatching) obj; 
      if (src.equals(vm.src) && trg.equals(vm.trg))
      { return true; } 
    } 
    return false; 
  } 
} 
