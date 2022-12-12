import java.util.*; 
import java.io.*; 

/* package: UseCase */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class Include extends ModelElement
{ UseCase includingCase; 
  UseCase addition; 

  public Include(UseCase base, UseCase subusecase)
  { super(""); 
    includingCase = base; 
    addition = subusecase; 
  } 

  public UseCase getInclusion()
  { return addition; } 

  public static String displayInclusions(List incls)
  { String res = ""; 
    for (int i = 0; i < incls.size(); i++) 
    { Include ex = (Include) incls.get(i); 
      res = res + ex.includingCase.getName() + " --includes--> " + 
                  ex.addition.getName() + "\n"; 
    } 
    return res; 
  } 

  public UseCase insertIntoBase()
  { UseCase result = includingCase; // (UseCase) includingCase.clone(); 
    Vector extcons = addition.getPostconditions(); 
    Vector extstats = addition.getCode().getStatements(); 

    for (int i = 0; i < extcons.size(); i++) 
    { Constraint extpost = (Constraint) extcons.get(i); 
      result.addPostcondition(extpost); 
      result.addStatement((Statement) extstats.get(i)); 
    } 
    // result.setName(includingCase.getName()); 
    return result; 
  } 
  // also add extension preconditions to result preconditions

  public static UseCase insertIntoBase(UseCase uc, UseCase inc)
  { Vector extcons = inc.getPostconditions(); 
    Vector extstats = inc.getCode().getStatements(); 

    for (int i = 0; i < extcons.size(); i++) 
    { Constraint extpost = (Constraint) extcons.get(i); 
      uc.addPostcondition(extpost); 
      uc.addStatement((Statement) extstats.get(i)); 
    } 
    // result.setName(includingCase.getName()); 
    return uc; 
  } 

  public void generateJava(java.io.PrintWriter out) { } 
}