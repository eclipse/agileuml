/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class InteractionElement extends Named
{ int y1; 
  int y2; 
  String stereotype = ""; 

  InteractionElement(String nme)
  { super(nme); } 

  public void sety1(int yy1)
  { y1 = yy1; } 

  public void sety2(int yy2)
  { y2 = yy2; } 

  public void setStereotype(String s)
  { stereotype = s; }

  public boolean isForall()
  { return "<<forall>>".equals(stereotype); }

  public Object clone()
  { InteractionElement res = new InteractionElement(label); 
    res.sety1(y1); 
    res.sety2(y2); 
    res.setStereotype(stereotype); 
    return res; 
  } 
} 