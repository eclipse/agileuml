/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package ATL */ 

public abstract class Rule extends ModuleElement
{ Statement actionBlock = null; 
  OutPattern outPattern = null;
  // Matched rules must have an in pattern and out pattern, action block is optional. 
  // Called rules must have an out pattern or an action block, possibly both.  

  public Rule() 
  { outPattern = new OutPattern(); } 

  public void setActionBlock(Statement stat)
  { actionBlock = stat; } 

  public void setOutPattern(OutPattern outp)
  { if (outp != null) 
    { outPattern = outp; }
  }  
} 