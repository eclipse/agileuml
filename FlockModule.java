import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class FlockModule // extends ModelElement // extends Classifier
{ Vector typeMappings = new Vector(); // of TypeMappingConstruct
  Vector rules = new Vector(); // of MigrateRule
  Vector operations = new Vector(); // of BehaviouralFeature
  String name = ""; 

  public FlockModule(String nme)
  { name = nme; }

  public void addTypeMapping(TypeMappingConstruct tr)
  { typeMappings.add(tr); } 

  public void addDelete(TypeMappingConstruct tr)
  { typeMappings.add(0,tr); } 

  public void addRule(MigrateRule tr)
  { rules.add(tr); } 

  public void add(Object r)
  { if (r instanceof FlockDeletion)
    { typeMappings.add(0,r); } 
    else if (r instanceof FlockRetyping) 
    { typeMappings.add(r); } 
    else if (r instanceof MigrateRule)
    { rules.add(r); } 
    else if (r instanceof BehaviouralFeature) 
    { operations.add(r); } 
  } 
  
  public void setTypeMappings(Vector v)
  { typeMappings = v; } 

  public String toString()
  { String res = "module " + name + ";\n"; 
    for (int i = 0; i < typeMappings.size(); i++) 
    { TypeMappingConstruct tm = (TypeMappingConstruct) typeMappings.get(i); 
      res = res + tm + "\n"; 
    } 
    for (int i = 0; i < rules.size(); i++) 
    { MigrateRule tm = (MigrateRule) rules.get(i); 
      res = res + tm + "\n"; 
    } 
    for (int i = 0; i < operations.size(); i++) 
    { BehaviouralFeature tm = (BehaviouralFeature) operations.get(i); 
      res = res + tm.display() + "\n"; 
    } 
    return res; 
  } 

  public Vector migrateRulesFor(String e)
  { Vector res = new Vector();
    for (int i = 0; i < rules.size(); i++)
    { MigrateRule trule = (MigrateRule) rules.get(i); 
      if (e.equals(trule.originalType))
      { res.add(trule); }
   }
   return res;
  }


  // In metamodel entities are IN$E, OUT$F, etc. 
  // In flock.txt they are E. F
  // Interp has  IN$E |-> OUT$F 
  public java.util.Map buildInterpretation(Vector sents, Vector tents)
  { java.util.Map res = new java.util.HashMap();
    for (int i = 0; i < sents.size(); i++)
    { Entity se = (Entity) sents.get(i);
      String sname = se.getName();
      String bname = ModelElement.baseName(sname);
      Entity te = (Entity) ModelElement.lookupByName("OUT$" + bname, tents);
      Vector interp = new Vector();
      if (te != null)
      { interp.add(te); }
      res.put(sname, interp);
    }
    for (int i = 0; i < typeMappings.size(); i++)
    { TypeMappingConstruct trule = (TypeMappingConstruct) typeMappings.get(i); 
      if (trule instanceof FlockRetyping)
      { String src = trule.originalType;
        String trg = ((FlockRetyping) trule).evolvedType;
        Expression g = trule.guard;
        Entity srce = (Entity) ModelElement.lookupByName("IN$" + src, sents);
        Entity trge = (Entity) ModelElement.lookupByName("OUT$" + trg, tents);
        Vector mapped = (Vector) res.get("IN$" + src); 
        if ("true".equals(g + ""))
        { mapped.clear(); }  // assume no other retyping for srce
        if (mapped != null && trge != null) 
        { mapped.add(trge); } 
      }
    }
    return res;
  }

  public Vector deletionRulesFor(String e)
  { Vector res = new Vector();
    for (int i = 0; i < typeMappings.size(); i++)
    { TypeMappingConstruct trule = (TypeMappingConstruct) typeMappings.get(i); 
      if (trule instanceof FlockDeletion)
      { if (e.equals(trule.originalType))
         { res.add(trule); }
      }
   }
   return res;
  }
   
  public Vector retypingRulesFor(String e)
  { Vector res = new Vector();
    for (int i = 0; i < typeMappings.size(); i++)
    { TypeMappingConstruct trule = (TypeMappingConstruct) typeMappings.get(i); 
      if (trule instanceof FlockRetyping)
      { if (e.equals(trule.originalType))
         { res.add(trule); }
      }
   }
   return res;
  }

  public FlockRetyping defaultRule(Entity e, Entity newe)
  { String ename = e.getName();
    String bname = ModelElement.baseName(ename); 
    Vector rrules = retypingRulesFor(bname);
    // Negate disjunction of rrules.guard
    String enew = newe.getName();
    String bnew = ModelElement.baseName(enew); 
    Expression andguard = new BasicExpression("true");

    for (int i = 0; i < rrules.size(); i++)
    { FlockRetyping rr = (FlockRetyping) rrules.get(i);
      if ("true".equals(rr.guard + ""))  // There is no default
      { andguard = new BasicExpression("false"); 
        return new FlockRetyping(bname,andguard,bnew); 
      } 
      Expression nguard = Expression.negate(rr.guard);
      andguard = Expression.simplify("&",andguard, nguard, new Vector());
    }
    FlockRetyping res = new FlockRetyping(bname,andguard,bnew);
    return res;
  }

  public void removeDeletions()
  { // For each deletion, conjoin negation of its guard to each retyping and 
    // migration rule on its type. 

    Vector deletions = new Vector(); 

    for (int i = 0; i < typeMappings.size(); i++)
    { TypeMappingConstruct trule = (TypeMappingConstruct) typeMappings.get(i); 
      if (trule instanceof FlockDeletion)
      { deletions.add(trule); } 
    } 

    for (int i = 0; i < deletions.size(); i++) 
    { FlockDeletion trule = (FlockDeletion) deletions.get(i); 
      String dtype = trule.originalType; 
      Expression dguard = Expression.negate(trule.guard); 
      for (int j = 0; j < typeMappings.size(); j++)
      { if (typeMappings.get(j) instanceof FlockRetyping) 
        { FlockRetyping rr = (FlockRetyping) typeMappings.get(j);
          if (rr.originalType.equals(dtype))
          { rr.addGuard(dguard); }  
        } 
      } 
      for (int k = 0; k < rules.size(); k++)
      { MigrateRule mrule = (MigrateRule) rules.get(k); 
        if (dtype.equals(mrule.originalType))
        { mrule.addGuard(dguard); }
      }
    } 
  }         


  public UseCase toUseCase(Vector sourceETs, Vector targetETs)
  { UseCase res = new UseCase(name,null);
    Vector phase2postconditions = new Vector(); 

    removeDeletions(); 
    Vector ignoring = new Vector(); 
    ignoring.add("$id"); 

    for (int i = 0; i < sourceETs.size(); i++)
    { Entity se = (Entity) sourceETs.get(i);
      String ename = se.getName();
      String bname = ModelElement.baseName(ename); 
      Vector phase2 = new Vector();
      /* Vector drules = deletionRulesFor(bname);
      for (int j = 0; j < drules.size(); j++)
      { FlockDeletion fd = (FlockDeletion) drules.get(j);
        Constraint con = fd.toConstraint(se);
        res.addPostcondition(con);
      } */  
      Vector rtrules = retypingRulesFor(bname);
      for (int j = 0; j < rtrules.size(); j++)
      { FlockRetyping rt = (FlockRetyping) rtrules.get(j);
        Entity te = (Entity) ModelElement.lookupByName("OUT$" + rt.evolvedType,targetETs);
        if (te == null)
        { System.err.println("No target entity " + rt.evolvedType + " for retyping");
          continue;
        }
        Constraint con = rt.toConstraint1(se,te);
        Constraint con2 = rt.toConstraint2(se,te,ignoring);
        if (con2 != null) 
        { if (con2.isTrivial()) { } 
          else 
          { phase2.add(con2); }
        }  
        res.addPostcondition(con);
      }  // Not fixed-point implementation 
      Entity se1 = (Entity) ModelElement.lookupByName("OUT$" + bname,targetETs);
      
      FlockRetyping def = null; 
      if (se1 != null && se1.isConcrete())
      { def = defaultRule(se,se1);
        Constraint dcon = def.toConstraint1(se,se1);
        Constraint dcon2 = def.toConstraint2(se,se1,ignoring);
        if (dcon2 != null) 
        { if (dcon2.isTrivial()) { } 
          else 
          { phase2.add(dcon2); }
        }  
        if (dcon.isTrivial()) { } 
        else 
        { res.addPostcondition(dcon); } 
      }
      Vector mrules = migrateRulesFor(bname);
      // System.out.println(">>>>> MIGRATE RULES: " + mrules); 

      if (mrules.size() == 0)
      { phase2postconditions.addAll(phase2); }
      else 
      { for (int k = 0; k < mrules.size(); k++)
        { MigrateRule mr = (MigrateRule) mrules.get(k);
          Vector mrcons = mr.toConstraint2(se,rtrules,targetETs,k,def);
          phase2postconditions.addAll(mrcons);
        }
      } 
    }  
    res.addPostconditions(phase2postconditions); 
    return res;
  }

}
