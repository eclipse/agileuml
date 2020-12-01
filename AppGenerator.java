import java.util.Vector; 
import java.io.*; 

/* Package: Mobile */ 
/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public abstract class AppGenerator
{ public static UseCase isSinglePageApp(Vector usecases)
  { // True if there is only one independent use case. 

    UseCase res = null; 
    for (int i = 0; i < usecases.size(); i++) 
    { if (usecases.get(i) instanceof OperationDescription)
      { return null; } 
      UseCase uc = (UseCase) usecases.get(i); 
      if (uc.isDependent()) { } 
      else if (uc.isPublic())
      { if (res != null) 
        { return null; } 
        else 
        { res = uc; } 
      } 
    } 
    return res; 
  } 

  public abstract void modelFacade(String packageName, Vector usecases, CGSpec cgs, 
    Vector entities, Vector clouds, Vector types, 
    int remoteCalls, boolean needsMaps, PrintWriter out); 

  public abstract void singlePageApp(UseCase uc, String appName, String image, CGSpec cgs, Vector types, Vector entities, PrintWriter out); 

  public abstract void listViewController(Entity e, PrintWriter out); 

  // public static void generateInternetAccessor(String packagename, PrintWriter out); 
  
}
