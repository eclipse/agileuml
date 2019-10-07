import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: class diagram */

public class PreAssociation
{ String e1name;
  String e2name;
  int card1;
  int card2;
  int xs, xe;
  int ys, ye;
  String role2;
  String role1;
  Vector stereotypes; 
  Vector wpoints; 

  public PreAssociation()
  { e1name = ""; e2name = ""; 
    role1 = ""; role2 = ""; 
    stereotypes = new Vector(); 
    card1 = ModelElement.MANY; card2 = ModelElement.MANY; 
  } 

  public PreAssociation(String e1n, String e2n, int c1,
                        int c2, int x1, int y1, int x2,
                        int y2, String r2, String r1, Vector sts, Vector wps)
  { e1name = e1n;
    e2name = e2n;
    card1 = c1;
    card2 = c2;
    xs = x1;
    ys = y1;
    xe = x2;
    ye = y2;
    role2 = r2;
    role1 = r1; 
    stereotypes = sts; 
    wpoints = wps; 
  }

  public boolean isDualTo(PreAssociation pinv)
  { if (pinv.role1 != null && pinv.role1.equals(role2) && 
        role1 != null && role1.equals(pinv.role2) && 
        e1name.equals(pinv.e2name) && e2name.equals(pinv.e1name))
    { return true; }
    return false; 
  } 

  public PreAssociation combineWith(PreAssociation pinv)
  { PreAssociation res = new PreAssociation(e1name, e2name, 
                               card1, card2, xs, ys, xe, ye, 
                               role2, role1, stereotypes, wpoints); 
    if (card1 == ModelElement.MANY) 
    { res.card1 = pinv.card2; } 
    if (card2 == ModelElement.MANY) 
    { res.card2 = pinv.card1; } 
    return res; 
  } 

}

