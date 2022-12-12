import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

abstract class SmBuilder
{ StatechartArea  sarea;
  String attribute = null; 
  String maxval = "0"; 
  int multip = 1; 

  SmBuilder() {}

  abstract String getName(); 

  public void build(StatechartArea sa)
  { sarea = sa; 
    if (attribute != null) 
    { sarea.setAttribute(attribute); } 
    sarea.setMaxval(maxval);
    sarea.setMultiplicity(multip); 
  }  

  abstract String saveData(); 

  public void setParameters(Vector params)
  { // The last three parameters represent the attribute, maxval and mult
    if (params.size() >= 3) 
    { int sze = params.size(); 
      String att = (String) params.get(sze-3); 
      if (att == null || att.equals("null")) { } 
      else { attribute = att; } 
      String mxvl = (String) params.get(sze-2); 
      maxval = mxvl; 
      String mult = (String) params.get(sze-1);
      int mm = 1; 
      try { mm = Integer.parseInt(mult); } 
      catch (Exception e)
      { System.err.println("Not a valid integer!: " + mult);
        return; 
      } 
      multip = mm;  
    } 
  }  
}

