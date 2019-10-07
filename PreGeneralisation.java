import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class PreGeneralisation
{ String e1name;
  String e2name;
  int xs, ys, xe, ye;
  Vector waypoints = new Vector(); 

  public PreGeneralisation()
  { e1name = ""; e2name = ""; } 

  public PreGeneralisation(String e1, String e2,
                           int x1, int y1, int x2,
                           int y2, Vector wps)
  { e1name = e1;
    e2name = e2;
    xs = x1;
    ys = y1;
    xe = x2;
    ye = y2;
    waypoints = wps; 
  }
}

