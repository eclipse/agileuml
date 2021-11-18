/******************************
* Copyright (c) 2003--2021 Kevin Lano
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

  public String toString()
  { return src + " |--> " + trg; }

  public ValueMatching invert()
  { return new ValueMatching(trg,src); }
} 
