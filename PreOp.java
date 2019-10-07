import java.util.Vector; 
import java.util.StringTokenizer; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package Class Diagram */ 

public class PreOp
{ String name;
  String entname;
  String ucname = null; 
  String params; 
  String resultType;
  String pre;
  String post;
  String stereotypes; 


  public PreOp(String nme, String ename, String ps,
               String res, String spre, String spost, String stereos)
  { name = nme;
    Vector linevals = new Vector(); 
    StringTokenizer st = new StringTokenizer(ename); 
    while (st.hasMoreTokens())
    { linevals.add(st.nextToken()); }
    if (linevals.size() > 1) 
    { entname = (String) linevals.get(0); 
      ucname = (String) linevals.get(1); 
    } 
    else 
    { entname = ename; } 
    params = ps; 
    resultType = res;
    pre = spre;
    post = spost;
    stereotypes = stereos; 
  }
}
