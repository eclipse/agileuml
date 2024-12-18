/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class UpdateFeatureInfo
{ // holds information about what feature is allowed to change in succ
  String feature;  // feature updated
  Expression updateExp; // value updating feature
  Expression updateRef; // objectRef of updated thing
  Expression newexp;    // r = l  if r has the update feature


  public UpdateFeatureInfo(String f, Expression ue,
                           Expression ur, Expression ne)
  { feature = f;
    updateExp = ue;
    updateRef = ur;
    newexp = ne;
  }

  public String toString()
  { return "[" + feature + "," + updateExp + "," + updateRef + "," +
           newexp + "]";
  } 
}
