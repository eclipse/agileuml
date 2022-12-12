import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class PreEntity
{ Entity e;
  Vector attnames = new Vector();
  Vector tnames = new Vector();
  int[] attmodes;
  Vector attfroz = new Vector(); 
  Vector attuniq = new Vector(); 
  Vector attstatic = new Vector(); 

  public PreEntity(Entity ent, Vector anames,
                   Vector tnms, int[] amodes,
                   Vector afroz, Vector auniq, 
                   Vector astatic)
  { e = ent;
    attnames = anames;
    tnames = tnms;
    attmodes = amodes;
    attfroz = afroz; 
    attuniq = auniq; 
    attstatic = astatic; 
  }
}

