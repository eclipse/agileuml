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

public class Extend extends ModelElement
{ UseCase extendedCase; 
  UseCase extension; 
  // extension points are usually *implicitly* calculated
  Vector extensionLocation = new Vector(); 

  public Extend(UseCase base, UseCase exten)
  { super("<<extend>>"); 
    extendedCase = base; 
    extension = exten; 
  } 

  public UseCase getExtension()
  { return extension; } 

  public static String displayExtensions(List exts)
  { String res = ""; 
    for (int i = 0; i < exts.size(); i++) 
    { Extend ex = (Extend) exts.get(i); 
      res = res + ex.extension.getName() + " --extends--> " + 
                  ex.extendedCase.getName() + "\n"; 
    } 
    return res; 
  } 

  public void addExtensionLocation(ExtensionPoint extp)
  { extensionLocation.add(extp); } 

  public UseCase insertIntoBase(Vector assocs)
  { UseCase result = (UseCase) extendedCase.clone(); 
    Vector extcons = extension.getPostconditions(); 
    // Vector extstats = extension.getCode().getStatements(); 

    int k = 0; 
    for (int i = 0; i < extcons.size(); i++) 
    { Constraint extpost = (Constraint) extcons.get(i); 
      k = result.insertAfter(extpost,k,assocs); 
      result.insertPostconditionAt(extpost,k); 
      // result.insertCodeAt((Statement) extstats.get(i),k); 
    } 
    result.setName(extendedCase.getName() + "_" + extension.getName()); 
    return result; 
  } 
  // also add extension preconditions to result preconditions, and invariants 
  // to invariants, and take union of attributes and operations 

  public static UseCase insertIntoBase(UseCase bse, UseCase ext, Vector assocs)
  { Vector extcons = ext.getPostconditions(); 
    // Vector extstats = ext.getCode().getStatements(); 

    int k = 0; 
    for (int i = 0; i < extcons.size(); i++) 
    { if (extcons.get(i) instanceof Constraint) 
      { Constraint extpost = (Constraint) extcons.get(i);      
        Constraint con = (Constraint) extpost.clone(); 
        k = bse.insertAfter(con,k,assocs); 
        bse.insertPostconditionAt(con,k); 
        con.setUseCase(bse); 
      // bse.insertCodeAt((Statement) extstats.get(i),k); 
      } // else - if ConstraintGroup? 
    } 
    // result.setName(extendedCase.getName() + "_" + extension.getName()); 
    bse.resetCode(); 

    Vector extpres = ext.getPreconditions(); 
    Vector extinvs = ext.getInvariants();
    Vector extatts = ext.getOwnedAttribute(); 
    Vector ops = ext.getOperations();  
    bse.addPreconditions(extpres); 
    bse.addInvariants(extinvs);   // Only those which are not affected by bse original preconditions
    bse.addAttributes(extatts); 
    bse.addOperations(ops); 
    bse.renumber(); 
    return bse; 
  } 

  public void generateJava(java.io.PrintWriter out) { } 
}