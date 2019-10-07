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

public class ConstraintGroup extends ConstraintOrGroup
{ Vector elements = new Vector(); 
  int groupType = 2; // default. It is type 3 if any constraint writes to LHS
                     // entities or features of any constraints in the group. 

  public ConstraintGroup(Vector v) 
  { elements = v; } 

  public Object clone()
  { Vector newelements = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      Constraint ccnew = (Constraint) cc.clone(); 
      newelements.add(ccnew); 
    } 
    ConstraintGroup res = new ConstraintGroup(newelements); 
    res.groupType = groupType; 
    return res; 
  } 

  public boolean isTrivial()
  { boolean res = true; 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      if (cc.isTrivial()) { } 
      else 
      { return false; }  
    } 
    return res; 
  } 


  public ConstraintOrGroup substituteEq(String var, Expression exp)
  { Vector newelements = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      Constraint ccnew = (Constraint) cc.substituteEq(var,exp); 
      newelements.add(ccnew); 
      ccnew.setOwner(cc.getOwner()); 
    } 
    ConstraintGroup res = new ConstraintGroup(newelements); 
    // res.findGroupType(); 
    return res; 
  } 
  
  public int getConstraintKind(Vector assocs)
  { groupType = findGroupType(assocs);
    return groupType; 
  } 

  public boolean hasAntecedent()
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      if (cc.hasAntecedent())
      { return true; } 
    } 
    return false; 
  } 

  public Vector applyCIForm()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      res.addAll(cc.applyCIForm()); 
    } 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts)
  { // Vector newcontext = getOwners();
    // newcontext.addAll(contexts); 
 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      Vector newcontext = new Vector(); 
      if (cc.getOwner() != null)
      { newcontext.add(cc.getOwner()); }
      cc.typeCheck(types,entities,newcontext);
    } 
    return true; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector vars)
  { // Vector newcontext = getOwners();
    // newcontext.addAll(contexts); 
 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      Vector newcontext = new Vector(); 
      if (cc.getOwner() != null)
      { newcontext.add(cc.getOwner()); }
      cc.typeCheck(types,entities,newcontext,vars);
    } 
    return true; 
  } 

  public boolean intersects(ConstraintGroup g)
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      if (g.elements.contains(cc))
      { return true; } 
    } 
    return false; 
  } 

  public ConstraintGroup union(ConstraintGroup g)
  { int newType = 2; 
    if (groupType == 3 || g.groupType == 3)
    { newType = 3; } 

    Vector elems = new Vector(); 
    elems.addAll(elements); 
    for (int i = 0; i < g.elements.size(); i++) 
    { Constraint cc = (Constraint) g.elements.get(i); 
      if (elems.contains(cc)) { } 
      else 
      { elems.add(cc); } 
    } 
    
    ConstraintGroup res = new ConstraintGroup(elems); 
    res.groupType = newType; 
    return res; 
  } 
    
  public static ConstraintGroup findContainingGroup(Vector cgs, Constraint cc)
  { ConstraintGroup res = null; 
    for (int i = 0; i < cgs.size(); i++) 
    { ConstraintGroup cg = (ConstraintGroup) cgs.get(i); 
      if (cg.elements.contains(cc))
      { return cg; } 
    } 
    return res; 
  } 

  public String saveData(PrintWriter out)
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint c = (Constraint) elements.get(i); 
      res = res + c.saveData(out); 
    }
    return res;  
  } 

  public void saveKM3(PrintWriter out)
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint c = (Constraint) elements.get(i); 
      c.saveKM3(out); 
    }
  } 

  public String getKM3()
  { String res = ""; 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint c = (Constraint) elements.get(i); 
      res = res + c.getKM3() + "\n"; 
    }
    return res; 
  } 

  public Entity getOwner()
  { Vector owns = getOwners(); 
    if (owns.size() > 0)
    { return (Entity) owns.get(0); }
    return null; 
  } 

  public void setOwner(Entity e) { } 
 
  public Vector getOwners()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      Entity own = cc.getOwner(); 
      if (own != null) 
      { res.add(own); } 
    } 
    return res; 
  } 

  public Vector wr(Vector assocs)
  { Vector written = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cst = (Constraint) elements.get(i); 
      written.addAll(cst.wr(assocs)); 
    } 
    return written; 
  } 

  public Vector readFrame()
  { return rd(); } 

  public Vector rd()
  { Vector read = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cst = (Constraint) elements.get(i); 
      read.addAll(cst.readFrame()); 
    } 
    return read; 
  } 

  public Vector internalReadFrame()
  { Vector read = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cst = (Constraint) elements.get(i); 
      read.addAll(cst.internalReadFrame()); 
    } 
    return read; 
  } 

  public Vector anteReadFrame()
  { Vector read = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cst = (Constraint) elements.get(i); 
      read.addAll(cst.anteReadFrame()); 
    } 
    return read; 
  } 


  public int findGroupType(Vector assocs)
  { Vector written = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cst = (Constraint) elements.get(i); 
      written.addAll(cst.wr(assocs)); 
    } 

    for (int i = 0; i < elements.size(); i++) 
    { Constraint cn2 = (Constraint) elements.get(i); 
      Vector rd2 = cn2.anteReadFrame(); 
      Vector inter = new Vector(); 
      inter.addAll(written); 
      inter.retainAll(rd2); 
      if (inter.size() > 0)
      { System.out.println("Type 3 group: " + inter + " written and read"); 
        groupType = 3; 
        return 3; 
      } 
    }
    System.out.println("Type 2 group: " + written + 
                       " not in any antecedent"); 
    groupType = 2; 
    return 2; 
  }  

  public Statement mapToDesign0(UseCase usecase)
  { SequenceStatement ss = new SequenceStatement(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint c1 = (Constraint) elements.get(i); 
      Statement stat = c1.mapToDesign0(usecase);
      ss.addStatement(stat); 
    } 
    return ss;  
  } // should never occur

  public Statement mapToDesign1(UseCase usecase, Vector types, Vector entities, Vector env)
  { SequenceStatement ss = new SequenceStatement(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint c1 = (Constraint) elements.get(i); 
      Statement stat = c1.mapToDesign1(usecase,types,entities,env);
      ss.addStatement(stat); 
    } 
    return ss; 
  } // should never occur


  public Statement mapToDesign2(UseCase usecase, String optimise, Vector types, Vector entities)
  { if (elements.size() == 0)
    { return new SequenceStatement(); } 

    Constraint c1 = (Constraint) elements.get(0); 
    SequenceStatement s1 = 
      (SequenceStatement) c1.mapToDesign2(usecase,optimise,null,types,entities); 
     
    Statement st = s1; 
    for (int i = 1; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i);
      st = cn.mapToDesign2(usecase,optimise,s1,types,entities); 
    }
    return st;   
  } 

  public Statement mapToDesign3(UseCase usecase, String opt, Vector types, Vector entities)
  { if (elements.size() == 0)
    { return new SequenceStatement(); } 

    Constraint c1 = (Constraint) elements.get(0);
    Vector innerss = new Vector();  
    SequenceStatement s1 = 
      (SequenceStatement) c1.mapToDesign3(usecase,opt,innerss,types,entities); 
     
    for (int i = 1; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i);
      Statement st = cn.mapToDesign3(usecase,opt,innerss,types,entities); 
    }
    return s1;   
  } 

  public String toString()
  { return elements + ""; }

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("constraintgroup_");
    out.println(res + " : ConstraintGroup");  
    for (int i = 0; i < elements.size(); i++) 
    { Constraint con = (Constraint) elements.get(i); 
      String elemid = con.saveModelData(out);
      out.println(elemid + " : " + res + ".elements");  
    }
    return res; 
  } // not quite correct. 

  public boolean equals(Object o)
  { if (o instanceof ConstraintGroup)
    { ConstraintGroup cg2 = (ConstraintGroup) o; 
      for (int i = 0; i < elements.size(); i++) 
      { if (cg2.elements.contains((Constraint) elements.get(i))) { } 
        else 
        { return false; } 
      } 
      for (int j = 0; j < cg2.elements.size(); j++) 
      { if (elements.contains((Constraint) cg2.elements.get(j))) { } 
        else 
        { return false; } 
      } 
      return true; 
    } 
    return false; 
  } 

  public int syntacticComplexity()
  { int res = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i); 
      res = res + cn.syntacticComplexity(); 
    } 
    return res; 
  }  

  public int cyclomaticComplexity()
  { int res = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i); 
      res = res + cn.cyclomaticComplexity(); 
    } 
    return res; 
  }  

  public void findClones(java.util.Map clones)
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i); 
      cn.findClones(clones);     
    } 
  }  

  public Vector allPreTerms()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      res = VectorUtil.union(res, cc.allPreTerms()); 
    } 
    return res; 
  } 

  public ConstraintOrGroup invert()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      res.add(cc.invert());  
    } 
    return new ConstraintGroup(res); 
  }  

  public void setUseCase(UseCase uc)
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      cc.setUseCase(uc);  
    } 
  } 

  public void changedEntityName(String oldN, String newN)
  { for (int i = 0; i < elements.size(); i++) 
    { Constraint cn = (Constraint) elements.get(i); 
      cn.changedEntityName(oldN, newN); 
    } 
  } 

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Constraint cc = (Constraint) elements.get(i); 
      res = VectorUtil.union(res, cc.operationsUsedIn()); 
    } 
    return res; 
  } 
 

} 