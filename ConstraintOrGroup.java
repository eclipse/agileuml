import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: OCL */ 

public abstract class ConstraintOrGroup
{ String name; 
  protected int id = 0; // Primary key within a use case

  public void setName(String nme) 
  { name = nme; } 

  public String getName()
  { return name; }

  public void setId(int idx)
  { id = idx; } 

  public int getId()
  { return id; }

  public abstract boolean isTrivial(); 

  public abstract int getConstraintKind(Vector assocs); 

  public abstract Statement mapToDesign0(UseCase uc); 

  public abstract Statement mapToDesign1(UseCase uc, Vector types, Vector entities, Vector env); 

  public abstract Statement mapToDesign2(UseCase usecase, String opt, Vector types, Vector entities);

  public abstract Statement mapToDesign3(UseCase usecase, String opt, Vector types, Vector entities);

  public abstract String toString();

  public abstract String saveModelData(PrintWriter out);

  public abstract Object clone(); 

  public abstract void setUseCase(UseCase uc); 

  public abstract Vector applyCIForm(); 


  public abstract ConstraintOrGroup invert(); 

  public abstract Entity getOwner(); 

  public abstract void setOwner(Entity e); 

  public abstract boolean typeCheck(Vector types, Vector entities, Vector contexts); 

  public abstract boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector vars); 

  public abstract ConstraintOrGroup substituteEq(String var, Expression exp); 

  public abstract String saveData(PrintWriter out); 

  public abstract void saveKM3(PrintWriter out); 

  public abstract String getKM3(); 

  public abstract boolean hasAntecedent(); 

  public abstract int syntacticComplexity(); 

  public abstract int cyclomaticComplexity(); 

  public abstract void findClones(java.util.Map clones); 

  public abstract Vector allPreTerms(); 

  public abstract Vector readFrame(); 

  public abstract Vector internalReadFrame(); 

  public abstract Vector wr(Vector assocs); 

  public abstract void changedEntityName(String oldN, String newN); 

  public abstract Vector operationsUsedIn(); 
} 