import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class StatechartData
{ Vector events;
  Vector rects;
  Vector states;
  Vector lines; 
  int multiplicity; 
  int maxval; 
  String attribute; 
  String[] transnames; 
  String[] transgens; 
  String[] transguards; 
  String[] attributes; 

  StatechartData(Vector evs, Vector rcts, 
                 Vector sts, Vector lns, 
                 int[] mults, String att,
                 String[] tns, String[] tgens, String[] tgds, String[] atts)
  { events = evs;
    rects = rcts;
    states = sts;
    lines = lns; 
    attribute = att; 
    multiplicity = mults[0]; 
    maxval = mults[1]; 
    transnames = tns; 
    transgens = tgens; 
    transguards = tgds; 
    attributes = atts; 
  }
}

