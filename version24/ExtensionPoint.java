import java.util.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ExtensionPoint
{ UseCase useCase;  // points are within this
  int location = 0; 
  int position = 0; 

  public static final int BEFORE = 0; 
  public static final int AFTER = 1; 
  public static final int WITHIN = 2; 
 
  public ExtensionPoint(UseCase base, int loc, int pos)
  { 
    useCase = base; 
    location = loc;
    position = pos;  
  } 
}