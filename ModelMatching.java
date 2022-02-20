import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 

public class ModelMatching implements SystemTypes
{ Map mymap; 
  Vector entitymatches = new Vector(); // of EntityMatching
  Vector typematches = new Vector(); // of TypeMatching
  Vector helpers = new Vector(); // of BehaviouralFeature

  final static double INTLONG = 0.8; // matching weight of int mapping to a long, etc
  final static double LONGINT = 0.6; 
  final static double ENUMSTRING = 0.3; 
  final static double STRINGENUM = 0.3; 
  final static double BOOLENUM = 0.3;  // boolean matching to an enum

  final static double OPTONE = 0.8; 
  final static double ONEOPT = 0.8; 
  final static double SETSEQUENCE = 0.75; 
  final static double SEQUENCESET = 0.75; 
  final static double SEQUENCEONE = 0.5; 
  final static double SETONE = 0.5; 
  final static double ONESEQUENCE = 0.5; 
  final static double ONESET = 0.5; 
  // These are also the same for OPTSET, OPTSEQUENCE

  final static double SUPERSUB = 0.6; // f : Superclass maps to f' : Subclass 
  final static double SUBSUPER = 0.8; 

  final static double NAMEWEIGHT = 0.25; // weighting of name-similarity in entity-similarity
  final static double NMSWEIGHT = 0.25; // weighting of name-semantics-similarity

  public ModelMatching() { } 

  public ModelMatching(Map m, Vector entities) 
  { mymap = m; 
    Vector elems = m.elements; 
    for (int i = 0; i < elems.size(); i++) 
    { Maplet mm = (Maplet) elems.get(i); 
      EntityMatching em = new EntityMatching((Entity) mm.source, (Entity) mm.dest, entities); 
      entitymatches.add(em); 
    } 
  } 

  public ModelMatching(Map m) 
  { mymap = m; 
    Vector elems = m.elements; 
    for (int i = 0; i < elems.size(); i++) 
    { Maplet mm = (Maplet) elems.get(i); 
      EntityMatching em = new EntityMatching((Entity) mm.source, (Entity) mm.dest); 
      entitymatches.add(em); 
    } 
  } 

  public ModelMatching(Vector ems) 
  { mymap = new Map(); 
    entitymatches.clear(); 
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      mymap.addPair(new Maplet(em.realsrc, em.realtrg)); 
      entitymatches.add(em); 
    } 
  } 

  public void addTypeMatch(TypeMatching tm)
  { typematches.add(tm); } 

  public void addEntityMatch(EntityMatching em, Vector entities)
  { Entity s = em.realsrc; 
    Entity t = em.realtrg; 
    String sname = s.getName(); 
    String tname = t.getName(); 
    Entity s$ = (Entity) ModelElement.lookupByName(sname + "$",entities); 
    Entity t$ = (Entity) ModelElement.lookupByName(tname + "$",entities); 
    if (s$ != null && t$ != null) 
    { mymap.add(new Maplet(s$,t$)); 
      System.out.println(">> Added entity matching: >> " + s$ + " --> " + t$);  
    } 
    entitymatches.add(em);
  } 

  public void addEntityMatchings(Vector ems) 
  { entitymatches.addAll(ems); } 

  public void addEntityMatchings(Vector ems, Vector entities) 
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      addEntityMatch(em,entities); 
    } 
  } 

  public void combineMatches(Vector ems)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
	 EntityMatching emx = findExactMatchBySourceTarget(em.realsrc,em.realtrg,entitymatches); 
	 if (emx != null)
	 { emx.combineMatching(em); }
	 else 
	 { entitymatches.add(em); }
    }
  }
  
  public ModelMatching invert()
  { Map invmm = Map.inverse(mymap); 
    ModelMatching inv = new ModelMatching(); 
    Vector inversemaps = new Vector(); 
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      EntityMatching eminv = em.reverse(); 
      inversemaps.add(eminv); 
    } 
    inv.mymap = invmm; 
    inv.entitymatches.addAll(inversemaps); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      em.invert(inversemaps); 
    } 

    return inv; 
  } 

  public String toString()
  { String res = "\n"; 
    for (int i = 0; i < helpers.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) helpers.get(i); 
      res = res + bf.display() + "\n\n"; 
    }
	
    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i); 
      res = res + tm + "\n\n"; 
    }
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      // Entity s = em.realsrc; 
      // Entity t = em.realtrg; 
      // res = res + "  " + s + " |--> " + t + "\n"; 
      res = res + em + "\r\n";  
    }
    return res; 
  }  

  public String toCSTL(CGSpec cg)
  { String res = "\n"; 
    /* for (int i = 0; i < helpers.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) helpers.get(i); 
      res = res + bf.display() + "\n\n"; 
    } */ 

    Vector usedFunctions = new Vector(); 
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = 
        (EntityMatching) entitymatches.get(i); 
      usedFunctions.addAll(em.usesCSTLfunctions()); 
    } 

    System.out.println(">>> Used functions are: " + 
                       usedFunctions); 
	
    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i);
      if (usedFunctions.contains(tm.getName())) 
      { res = res + tm.toCSTL(cg) + "\n\n"; } 
    } // *only* include them if used in some rule. 
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      // Entity s = em.realsrc; 
      // Entity t = em.realtrg; 
      // res = res + "  " + s + " |--> " + t + "\n"; 
      res = res + em.toCSTL(cg,typematches) + "\n\n";  
    }

    return res; 
  }  

  public Vector enumConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching am = (EntityMatching) entitymatches.get(i); 
      res = VectorUtil.union(res,am.enumConversions()); 
    } 
    return res; 
  } 

  // for ATL: 
  public Vector enumConversionFunctions(Vector enumconversions, Vector thesaurus) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Maplet conv = (Maplet) enumconversions.get(i); 
      Type e1 = (Type) conv.source; 
      Type e2 = (Type) conv.dest; 
      String tconv = Type.enumConversionFunction(e1,e2,thesaurus); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector booleanEnumConversionFunctions(Vector enumconversions, Vector enames) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      String srcname = (String) enames.get(i); 
      String tconv = Type.booleanEnumConversionFunction(e1,srcname); 
      res.add(tconv); 
    } 
    return res; 
  } // f : boolean converted to g : T.  

  public Vector enumBooleanConversionFunctions(Vector enumconversions, Vector tnames) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      String trgname = (String) tnames.get(i); 
      String tconv = Type.enumBooleanConversionFunction(e1,trgname); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector stringEnumConversionFunctions(Vector enumconversions) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      String tconv = Type.stringEnumConversionFunction(e1); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector enumStringConversionFunctions(Vector enumconversions) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      String tconv = Type.enumStringConversionFunction(e1); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector boolEnumConversions(Vector names)
  { Vector res = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching am = (EntityMatching) entitymatches.get(i);
      Vector enames = new Vector();  
      res.addAll(am.boolEnumConversions(enames));
      names.addAll(enames);  
    } 
    return res; 
  } 

  public Vector enumBoolConversions(Vector names)
  { Vector res = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching am = (EntityMatching) entitymatches.get(i);
      Vector tnames = new Vector();  
      res.addAll(am.enumBoolConversions(tnames)); 
      names.addAll(tnames); 
    } 
    return res; 
  } 

  public Vector stringEnumConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching am = (EntityMatching) entitymatches.get(i); 
      res = VectorUtil.union(res,am.stringEnumConversions()); 
    } 
    return res; 
  } 

  public Vector enumStringConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching am = (EntityMatching) entitymatches.get(i); 
      res = VectorUtil.union(res,am.enumStringConversions()); 
    } 
    return res; 
  } 

  public Vector enumConversionFunctionsETL(Vector enumconversions, Vector thesaurus) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Maplet conv = (Maplet) enumconversions.get(i); 
      Type e1 = (Type) conv.source; 
      Type e2 = (Type) conv.dest; 
      BehaviouralFeature tconv = Type.enumConversionFunctionETL(e1,e2,thesaurus); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector enumStringConversionFunctionsETL(Vector enumconversions, Vector thesaurus) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      BehaviouralFeature tconv = Type.enumStringConversionFunctionETL(e1); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public Vector stringEnumConversionFunctionsETL(Vector enumconversions, Vector thesaurus) 
  { Vector res = new Vector(); 
 
    for (int i = 0; i < enumconversions.size(); i++) 
    { Type e1 = (Type) enumconversions.get(i); 
      BehaviouralFeature tconv = Type.stringEnumConversionFunctionETL(e1); 
      res.add(tconv); 
    } 
    return res; 
  } 

  public void removeInvalidMatchings()
  { // remove att -> tatt.f where f is in targets of its owner's attribute matching

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      em.removeInvalidMatchings(entitymatches); 
    } 
  }     

  public void copySuperclassMatchings(Vector thesaurus, Vector entities)
  { for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.realsrc == null) { continue; } 

      Entity srcsup = em.realsrc.getSuperclass(); 
      Vector srcsubs = em.realsrc.getSubclasses(); 

      if (srcsup == null && srcsubs.size() > 0)
      { em.copyAttributeMappingsToSubclasses(srcsubs,entitymatches,thesaurus,this,entities); } 
    } 
  } 

  public void checkBidirectionalAssociationConsistency()
  { // For each feature mapping r to rr, where both have reverse directions r1, rr1, 
    // check that r1 is mapped to rr1

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.realsrc == null) { continue; } 
      Vector biassocmatchings = em.bidirectionalassociationMatchings(); 

      // For each one, src -> trg, for each entity mapping emx with 
      // emx.realsrc equal to or subclass of em.realsrc, and emx.realtrg 
      // equal to or subclass of em.realtrg, any emx mapping for src~ must be 
      // to trg~, and any mapping to trg~ must be from src~

      for (int k = 0; k < biassocmatchings.size(); k++) 
      { AttributeMatching bm = (AttributeMatching) biassocmatchings.get(k); 
        Attribute bmsrc = bm.src; 
        Attribute bmtrg = bm.trg; 
        if (bmsrc != null && bmsrc.getElementType() != null && 
            bmsrc.getElementType().isEntity() && 
            bmtrg != null && bmtrg.getElementType() != null && 
            bmtrg.getElementType().isEntity())
        { Entity srcsupplier = bmsrc.getElementType().getEntity(); 
          Entity trgsupplier = bmtrg.getElementType().getEntity(); 
          for (int j = 0; j < entitymatches.size(); j++) 
          { EntityMatching emx = (EntityMatching) entitymatches.get(j); 
            if (emx.realsrc == srcsupplier || Entity.isAncestor(srcsupplier,emx.realsrc))
            { Vector bms = new Vector(); 
              bms.add(bm); 
              emx.checkReverseReferenceConsistency(bms); 
            } 
          } 
        } 
      } 
    } 
  } 



  public boolean isMonotonic() 
  { // for every source entity, s, mm(s) = t, 
    // every subclass of s maps to t or a descendent of t 

    boolean res = true; 
    Vector elems = mymap.elements; 
    for (int i = 0; i < elems.size(); i++) 
    { Maplet mm = (Maplet) elems.get(i); 
      Entity s = (Entity) mm.source; 
      Entity t = (Entity) mm.dest; 
      Vector subs = s.getSubclasses(); 
      for (int j = 0; j < subs.size(); j++) 
      { Entity sub = (Entity) subs.get(j); 
        Entity subt = (Entity) mymap.get(sub); 
        // System.out.println("Subclass " + sub + " maps to " + subt); 
        if (subt == t) { }
        else if (subt == null) { } 
        // else if (Entity.isAncestor(t,subt)) { } 
        else if (Entity.isAncestor(subt,t)) 
        { return false; }  // inheritance wrong way round.  
      } 
    } 
    return res; 
  } 

  public Vector checkEntityMapCompleteness(Vector targets) 
  { // for every concrete target entity t, warn if no mapping to t or any 
    // superclass of t, if there is a superclass mapping s -> tsup, prompt  
    // for a mapping condition { tcond } s --> t 

   Vector unused = new Vector(); 

   for (int i = 0; i < targets.size(); i++) 
   { Entity t = (Entity) targets.get(i); 
     if (t.isConcrete() && t.isTarget())
     { Vector emts = findEntityMatchingsByTarget(t,entitymatches);
       if (emts.size() == 0) 
       { unused.add(t); 
         EntityMatching supemt = findSuperclassMatchingByTarget(t,entitymatches); 
         if (supemt == null) 
         { System.out.println(">>> Neither " + t + 
                              " or any superclass is in the range of the mapping"); 
         } 
         else 
         { System.out.println(">>> Concrete class " + t + " not mapped to, but superclass mapping " + 
                              supemt + " is defined."); 
           System.out.println(">>> Suggest conditional mappings { Cond } " + 
                              supemt.realsrc + " --> " + t);
           // Cond based on unused source features of supemt.realsrc
          } 
        } 
      }  
    } 
    return unused; 
  } 


  public Vector unusedSourceEntities(Vector entities) 
  { Vector res = new Vector(); 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ex = (Entity) entities.get(i); 
      if (ex.isSource())
      { res.add(ex); } 
    } 
   
    Vector found = new Vector(); 
    for (int j = 0; j < entitymatches.size(); j++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(j); 
      if (em.realsrc != null) 
      { found.add(em.realsrc); 
        found.addAll(em.realsrc.getAllSubclasses()); 
      } 
    }
    res.removeAll(found); 
    return res; 
  }   

  public static Vector unusedTargetEntities(Vector entities, Vector ems) 
  { Vector res = new Vector(); 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ex = (Entity) entities.get(i); 
      if (ex.isTarget() && ex.isConcrete())
      { res.add(ex); } 
    } 
   
    Vector found = new Vector(); 
    for (int j = 0; j < ems.size(); j++) 
    { EntityMatching em = (EntityMatching) ems.get(j); 
      if (em.realtrg != null) 
      { found.add(em.realtrg); } 
    }
    res.removeAll(found); 
    return res; 
  }

  public Vector unusedTargetEntities(Vector entities) 
  { return ModelMatching.unusedTargetEntities(entities,entitymatches); }    

  public Vector analyseCorrelationPatterns(Vector entities, Vector types)
  { Vector res = new Vector(); 

    /* 
    for (int i = 0; i < entities.size(); i++) 
    { Entity e = (Entity) entities.get(i); 
      // some source e /: entitymatches.realsrc
      // then it has been deleted from the target metamodel
      // if target e /: entitymatches.realtrg  
      // then it is new in the target

      boolean foundsrc = false; 
      boolean foundtrg = false; 

      for (int j = 0; j < entitymatches.size(); j++) 
      { EntityMatching em = (EntityMatching) entitymatches.get(j); 
        if (em.realsrc != null && em.realsrc.getName().equals(e.getName())) 
        { foundsrc = true; } 
        if (em.realtrg != null && em.realtrg.getName().equals(e.getName()))
        { foundtrg = true; } 
      }  

      if (foundsrc == false && e.isSourceEntity()) 
      { CorrelationPattern p = new CorrelationPattern("Deleted entity", 
                                     "Entity " + e + " has been deleted by the mapping"); 
        p.addSourceEntity(e); 
        if (res.contains(p)) { } else { res.add(p); }  
      } 
      else if (foundtrg == false && e.isTargetEntity()) 
      { CorrelationPattern p = new CorrelationPattern("New entity", 
                                     "Entity " + e + " is new in the target metamodel"); 
        p.addTargetEntity(e); 
        if (res.contains(p)) { } else { res.add(p); }  
      } 
 
    }  */ 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      res.addAll(em.analyseCorrelationPatterns(entitymatches,this,entities,types));  
    }

    return res; 
  } 


  public void addFlagVariables(Vector res) 
  { 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 

      em.removeDuplicateMappings(); 

      Entity t = em.trg; // The $ form of the entity
      Vector tvect = new Vector(); 
      tvect.add(t); 
      Vector tsources = mymap.inverseImage(tvect); 
      Vector emts = ModelMatching.findEntityMatchingsByTarget(em.realtrg, entitymatches); 
      if (emts.size() > 1) 
      { System.out.println(">>> Several classes " + tsources + 
                           " map to " + t); 


        // if (em.isSpurious(entitymatches))
        // { System.out.println(">>> match " + em.realsrc + " |--> " + em.realtrg + 
        //                          " seems to be spurious, suggest deletion"); 
        // } 
        
        if (Entity.haveCommonSuperclass(tsources))
        { CorrelationPattern p = new CorrelationPattern("Class merging (horizontal)", 
                                     "Entity " + em.realtrg + " is target of " + 
                                                     tsources.size() + " exclusive sources"); 
          p.addTargetEntity(t); 
          p.addSourceEntities(tsources); 

          if (res.contains(p)) { } else { res.add(p); }
          // add flag attribute if not already there:
          Entity rtrg = em.realtrg; 
          String flagname = rtrg.getName().toLowerCase() + "Flag";
          if (rtrg.hasDefinedAttribute(flagname)) { } 
          else if (conditionedMatches(emts)) { } 
          else   
          { Attribute tflag = new Attribute(flagname, 
                                          new Type("String", null),
                                          ModelElement.INTERNAL);
            tflag.setElementType(new Type("String",null)); 
 
            rtrg.addAttribute(tflag); 
            for (int z = 0; z < emts.size(); z++) 
            { EntityMatching emz = (EntityMatching) emts.get(z); 
              if (emz.realsrc != null && emz.realsrc.isConcrete()) 
              { BasicExpression srcvalue = new BasicExpression("\"" + emz.realsrc.getName() + "\""); 
                srcvalue.setType(new Type("String",null)); 
                srcvalue.setElementType(new Type("String",null)); 

                AttributeMatching am = new AttributeMatching(srcvalue,tflag); 
                emz.addAttributeMatch(am); 
              }
            } 
          }  
        } 
        else 
        { CorrelationPattern p = new CorrelationPattern("Class merging (vertical)", 
                                     "Entity " + em.realtrg + " is target of " + 
                                                     tsources.size() + " non-exclusive sources"); 
          p.addTargetEntity(t); 
          p.addSourceEntities(tsources); 

          if (res.contains(p)) { } else { res.add(p); }
        }   
      } // risk of conflict in data, for some feature merges
        // target should have id attribute, mapped to by id in each source
        // apart from this and *-mult features, the target features of each mapping 
        // must be disjoint. Or merge the data if they are not disjoint. 
    } 

  }  

  public boolean conditionedMatches(Vector emts)
  { // each E --> F in emts has an assignment "F" --> s for some target variable s

    for (int i = 0; i < emts.size(); i++) 
    { EntityMatching emt = (EntityMatching) emts.get(i); 
      Entity t = emt.realtrg; 
      String tname = t.getName(); 
      if (emt.hasAssignmentOf(tname)) 
      { return true; } 
    } 
    return false; 
  } 

  public void removeSpuriousMatchings() 
  { // E --> F may be spurious if all features of E are already mapped by other entity matchings
    // Or, if no mapped feature f uses E as a type/element type

    Vector spurious = new Vector(); 
    Vector removed = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      Entity s = em.src; // The $ form of the entity
      boolean allused = true;  // all of s's features are used elsewhere

      Vector satts = s.getAttributes(); 

      for (int j = 0; j < satts.size(); j++) 
      { Attribute f = (Attribute) satts.get(j); 
        boolean fused = false; 

        for (int k = 0; k < entitymatches.size(); k++) 
        { EntityMatching em2 = (EntityMatching) entitymatches.get(k); 
          if (i != k && !em.inheritanceRelatedTo(em2)) 
          { if (em2.usedInSource(f)) 
            { fused = true; } 
          } 
        }

        if (fused == true) { } 
        else 
        { allused = false; 
          break; 
        }        
      } 

      if (allused) 
      { System.out.println(">>> match " + em.realsrc + " |--> " + em.realtrg + 
                           " has all source features mapped elsewhere - may be spurious"); 
        spurious.add(em); 
      }  
    }  
    
      

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      Entity t = em.trg; // The $ form of the entity
      Vector tvect = new Vector(); 
      tvect.add(t); 
      Vector tsources = mymap.inverseImage(tvect); 
      if (tsources.size() > 1) 
      { System.out.println(">>> Several classes " + tsources + 
                           " map to " + t); 
 
        
        if (em.isSpurious(entitymatches))
        { System.out.println(">>> match " + em.realsrc + " |--> " + em.realtrg + 
                             " is not used by any feature mappings - may be spurious"); 
          if (spurious.contains(em)) 
          { String yn = 
                JOptionPane.showInputDialog("Remove mapping " + em +
                                            "? (y/n):");

            if (yn != null && "y".equals(yn))
            { removed.add(em); } 
          } 
        } 
      } 
    } 
    entitymatches.removeAll(removed); 
    // and remove from mymap
    for (int i = 0; i < removed.size(); i++) 
    { EntityMatching rem = (EntityMatching) removed.get(i); 
      mymap.remove_pair(rem.src, rem.trg); 
    } 
  } 
  
  public static Vector sourcesMappedTo(Vector entitymaps, Entity ut)
  { Vector res = new Vector(); 
    for (int i = 0; i < entitymaps.size(); i++) 
	{ EntityMatching em = (EntityMatching) entitymaps.get(i); 
	  if (em.realtrg == ut)
	  { res.add(em.realsrc); }
	}
	return res;
  }

  public void checkValidity(Vector unusedtargets, Vector entities, Vector sources,
                            Vector entitymaps, Vector thesaurus)
  { // (i) unused target F1 matches highly with used target F. 
    // Introduce class splitting: horizontal
    // if F, F1 have a common ancestor.
 
    // (ii) unused mandatory target reference r : R of some F in range of entity matching:
    // if (E,R) in entitymatches then add self --> r to the attribute mapping of E -> F, otherwise
    // entity split E to R if permitted and add self --> r. 

    Vector added = new Vector(); // New entity matches

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      Vector unusedmandatorytargets = em.unusedMandatoryTargetReferences(entitymatches); 
      if (unusedmandatorytargets.size() > 0) 
      { System.out.println("!! Warning: invalid matching, unused mandatory target features: " + 
                           unusedmandatorytargets + " of " + em.getName());
 
        for (int j = 0; j < unusedmandatorytargets.size(); j++) 
        { Attribute targ = (Attribute) unusedmandatorytargets.get(j);
          if (targ.getElementType() != null && targ.getElementType().isEntity()) 
          { Entity tent = targ.getElementType().getEntity(); 

            // Is there already a mapping of em.realsrc to tent? 
            EntityMatching e2tent = findEntityMatchingFor(em.realsrc,tent,entitymatches); 
            // or tent is used as intermediate target in em?
            boolean usedasintermediate = em.usedAsIntermediateClass(tent); 

            if (e2tent == null && !usedasintermediate)
            { String yn = 
                JOptionPane.showInputDialog("Add mapping " + em.realsrc + " |--> " + tent +
                                            "? (y/n):");

              if (yn != null && "y".equals(yn))
              { String s$ = em.realsrc.getName() + "$";  
                String t$ = tent.getName() + "$"; 
                Entity newsrc = (Entity) ModelElement.lookupByName(s$,entities); 
                Entity newtrg = (Entity) ModelElement.lookupByName(t$,entities); 
                EntityMatching newemx = null; 

                if (newsrc == null || newtrg == null) 
                { System.err.println("!! ERROR: no entity " + s$ + " or " + t$); 
                  newemx = new EntityMatching(em.realsrc,tent);                
                }
                else 
                { newemx = new EntityMatching(newsrc,newtrg,entities); } 
                // addEntityMatch(newemx,entities);

                added.add(newemx);  
                unusedtargets.remove(tent);
              } 
            } // But don't do this if tent already occurs as intermediate class in an em.realsrc
              // feature mapping target 

            String yn = 
                JOptionPane.showInputDialog("Add feature mapping self |--> " + targ +
                                            "? (y/n):");

            if (yn != null && "y".equals(yn))
            { BasicExpression selfatt = new BasicExpression(em.realsrc,"self"); 
              selfatt.setElementType(new Type(em.realsrc)); 
              Attribute selfvar = new Attribute("self",new Type(em.realsrc),
                                                  ModelElement.INTERNAL); 
              selfvar.setElementType(new Type(em.realsrc));
              AttributeMatching self2mandatory = new AttributeMatching(selfatt,targ); 
              self2mandatory.setElementVariable(selfvar);   
              em.addAttributeMatch(self2mandatory); 
              Vector emsrcsubs = em.realsrc.getSubclasses(); 
              em.copyAttributeMappingToSubclasses(self2mandatory,emsrcsubs,entitymatches,
                                                  thesaurus,this,entities);
 
              // System.out.println(">> Created new feature map self --> " + targ); 
              // Also, add it to all subclasses of em.realsrc               
            } 
          } 
        } 
      } 
    } 

    addEntityMatchings(added,entities); 

    for (int h = 0; h < added.size(); h++) 
    { EntityMatching add = (EntityMatching) added.get(h); 
      add.src.similarity(add.trg,this,entities,false,false,thesaurus);
      Vector srcsubs = add.realsrc.getSubclasses(); 
      if (srcsubs.size() > 0)
      { add.createSubclassMatchings(srcsubs,entitymatches); 
        add.copyAttributeMappingsToSubclasses(srcsubs,entitymatches,thesaurus,this,entities); 
      }
    } // build feature mappings of the new entity matchings


    // For unused target entities, look for closest matching source & propose to add
    // and to class split. 



    for (int i = 0; i < unusedtargets.size(); i++) 
    { Entity ut = (Entity) unusedtargets.get(i); 
      if (ut.isAbstract()) { continue; } 
      Vector recommendedsources = sourcesMappedTo(entitymaps,ut); 

      System.out.println(">>> Recommended source matches for " + ut + " are " + recommendedsources); 

      double bestscore = 0; 
      Entity bestmatch = null; 

      for (int j = 0; j < sources.size(); j++) 
      { Entity ej = (Entity) sources.get(j); 
        String ejname = ej.getName(); 
        Entity ej$ = (Entity) ModelElement.lookupByName(ejname + "$", entities); 
        String utname = ut.getName(); 
        Entity ut$ = (Entity) ModelElement.lookupByName(utname + "$", entities); 
        double dd = 0; 
        if (ej$ != null && ut$ != null) 
        { dd = ej$.similarity(ut$,this,entities,false,false,thesaurus); } 
        else 
        { dd = ej.similarity(ut,this,entities,false,false,thesaurus); }  
        double namesim = ModelElement.similarity(ej.getName().toLowerCase(),
                                                 ut.getName().toLowerCase()); 
        double nmssim = ej.nms$Similarity(ut,thesaurus); 
        double escore = dd + namesim*ModelMatching.NAMEWEIGHT + 
                             nmssim*ModelMatching.NMSWEIGHT; 
        System.out.println(">>> Possible match for target " + ut + " is source " + ej + " with " + 
                           "NSS=" + namesim + " NMS=" + nmssim + " DSS=" + dd); 

        if (escore > bestscore) 
        { bestscore = escore; 
          bestmatch = ej; 
        } 
      }
      
      if (bestmatch != null) 
      { System.out.println(">>> Best match for unused target class " + ut + " is " + bestmatch); 
        String yn = 
          JOptionPane.showInputDialog("Add class mapping " + bestmatch + " |--> " + ut +
                                            "? (y/n):");
        if (yn != null && "n".equals(yn))
        { sources = VectorUtil.union(recommendedsources,sources); 
		  sources.remove(bestmatch); 
		  
          if (sources.size() > 0)
          { String othere = 
              JOptionPane.showInputDialog("Suggest another source from " + 
                                          sources + " (name/null)?");  
            bestmatch = (Entity) ModelElement.lookupByName(othere,sources);
          } 
          else 
          { bestmatch = null; }  
        }  
      } 

      if (bestmatch != null)
      { // EntityMatching newem = new EntityMatching(bestmatch,ut); 
        String s$ = bestmatch.getName() + "$";  
        String t$ = ut.getName() + "$"; 
        Entity newsrc = (Entity) ModelElement.lookupByName(s$,entities); 
        Entity newtrg = (Entity) ModelElement.lookupByName(t$,entities); 
        EntityMatching newem = null; 
        
        if (newsrc == null || newtrg == null) 
        { System.err.println("!! Warning: no entity " + s$ + " or " + t$); 
          newem = new EntityMatching(bestmatch,ut);  
        }
        else 
        { newem = new EntityMatching(newsrc,newtrg,entities); } 
        addEntityMatch(newem,entities); 
        newem.src.similarity(newem.trg,this,entities,false,false,thesaurus);
        // Needs a discriminator if bestmatch now has > 1 target match & no conditions
        // look for unused boolean features of bestmatch & test their name similarity to 
        // ut and other targets. Condition is { bf } bestmatch --> ut if name similarity
        // highest for bt, ut names.

        Vector allems = new Vector(); 
        Vector trgs = isUnconditionalEntitySplitting(bestmatch,allems);
            
        if (trgs.size() > 1)
        { Vector ubools = newem.unusedSourceBooleans();  
          Vector ustrings = newem.unusedSourceStrings();
          Vector unusedoptionals = newem.allSourceOptionals();  
          System.out.println(">>> Unconditional entity splitting of " + 
                             bestmatch + " to: " + trgs); 

          if (ubools.size() > 0) 
          { System.out.println(">>> Unused source boolean features: " + ubools); 
            for (int p = 0; p < ubools.size(); p++) 
            { Attribute ub = (Attribute) ubools.get(p); 
              EntityMatching besttrg = newem; 
              double besttscore = ModelElement.similarity(ub.getName(), ut.getName()); 
              for (int k = 0; k < trgs.size(); k++) 
              { EntityMatching tk = (EntityMatching) trgs.get(k); 
                double tknss = ModelElement.similarity(ub.getName(), tk.realtrg.getName()); 
                if (tknss > besttscore) 
                { besttscore = tknss; 
                  besttrg = tk; 
                }
              }
              System.out.println(">>> Best match for " + ub + " is " + besttrg.realtrg); 
              besttrg.addCondition(new BasicExpression(ub));

              UnaryExpression negub = new UnaryExpression("not", new BasicExpression(ub)); 

              for (int k = 0; k < trgs.size(); k++) 
              { EntityMatching tk = (EntityMatching) trgs.get(k); 
                if (tk != besttrg)
                { tk.addCondition(negub); } 
              }  
            }  
          }
          else if (ustrings.size() > 0) 
          { System.out.println(">>> Unused source string features: " + ustrings); 
            Attribute selector = (Attribute) ustrings.get(0); 
            String yns = 
                JOptionPane.showInputDialog("Add conditions " + selector + " = target entity name" +
                                            "? (y/n):");

            if (yns != null && "y".equals(yns))
            { BasicExpression selbe = new BasicExpression(selector); 
            
              for (int k = 0; k < trgs.size(); k++) 
              { EntityMatching tk = (EntityMatching) trgs.get(k); 
                BasicExpression tbe = new BasicExpression("\"" + tk.realtrg.getName() + "\""); 
                tbe.setType(selector.getType()); 
                tbe.setElementType(selector.getType()); 

                BinaryExpression equalstname = new BinaryExpression("=",selbe,tbe);  
                tk.addCondition(equalstname);  
              }  
            }
            else 
            { String ynn = 
                JOptionPane.showInputDialog("Choose string selector from " + ustrings +
                                            "? (name/null):");

              if (ynn != null && !"null".equals(ynn))
              { Attribute selsel = (Attribute) ModelElement.lookupByName(ynn,ustrings); 
                if (selsel != null) 
                { BasicExpression selbe = new BasicExpression(selsel); 
            
                  for (int k = 0; k < trgs.size(); k++) 
                  { EntityMatching tk = (EntityMatching) trgs.get(k); 
                    BasicExpression tbe = new BasicExpression("\"" + tk.realtrg.getName() + "\""); 
                    tbe.setType(selsel.getType()); 
                    tbe.setElementType(selsel.getType()); 
                    BinaryExpression equalstname = new BinaryExpression("=",selbe,tbe);  
                    tk.addCondition(equalstname);  
                  }  
                } 
              } 
            }
          }
          else if (unusedoptionals.size() > 0) 
          { System.out.println(">>> Optional source features: " + unusedoptionals); 
            for (int pp = 0; pp < unusedoptionals.size(); pp++) 
            { Attribute selector = (Attribute) unusedoptionals.get(pp); 
              String yns = 
                  JOptionPane.showInputDialog("Add condition " + selector + "->isEmpty(), or " + selector + "->notEmpty() " +
                                            "? (y/n):");

              if (yns != null && "y".equals(yns))
              { BasicExpression selbe = new BasicExpression(selector); 

              // Look for a match to selector in tk, if found, use selector->notEmpty().             
                for (int k = 0; k < trgs.size(); k++) 
                { EntityMatching tk = (EntityMatching) trgs.get(k); 
			    // if tk.realtrg has match for selector, set condition 
                // BasicExpression tbe = new BasicExpression("\"" + tk.realtrg.getName() + "\""); 
                // tbe.setType(selector.getType()); 
                // tbe.setElementType(selector.getType()); 
			// Vector amsc = tk.findCompatibleMappings(selector,entitymatches); 
                // AttributeMatching amx = tk.findClosestNamedMapping(selector,amsc); 
                  Vector amsc = tk.bestTargetMatches(selector,entitymatches,thesaurus);  
                  if (amsc != null && amsc.size() > 0) 
			  { System.out.println(">>> " + tk + " has matches " + amsc + " for " + selector);
                    BasicExpression selxbe = new BasicExpression(selector);    
                    UnaryExpression hasOptional = new UnaryExpression("->notEmpty",selbe);  
                    tk.addCondition(hasOptional);
                  } 
                  else 
                  { BasicExpression selxbe = new BasicExpression(selector);    
                    UnaryExpression emptyOptional = new UnaryExpression("->isEmpty",selbe);  
                    tk.addCondition(emptyOptional);
                  } 
                }   
              }  
            }
          }
          else 
          { Vector featuresets = new Vector(); 
            Vector otherconds = newem.unusedSourceConditions(featuresets); 

            Vector matchedconditions = new Vector(); 
            Vector matchedtargets = new Vector(); 

            System.out.println(">>> Possible discriminator conditions are: " + otherconds); 
            for (int k = 0; k < trgs.size(); k++) 
            { EntityMatching tk = (EntityMatching) trgs.get(k); 
              if (matchedtargets.contains(tk.realtrg)) { } 
              else 
              { Expression bestcond = null; 
                Vector bestconds = new Vector(); 

                double besttscore = 0;  

                for (int p = 0; p < featuresets.size(); p++) 
                { Attribute ub = (Attribute) featuresets.get(p); 
                  Expression newcond = (Expression) otherconds.get(p); 
                  if (matchedconditions.contains(newcond + "")) { } 
                  else 
                  { double tknss = // ModelElement.similarity(ub.getName(), tk.realtrg.getName());
                        Entity.nmsSimilarity(ub.getName(), tk.realtrg.getName(), thesaurus); 
                    System.out.println(">>> NMS similarity of " + ub + " and " + tk.realtrg + 
                                       " = " + tknss); 
                    
                    if (tknss > besttscore) 
                    { besttscore = tknss; 
                      bestcond = newcond;   
                      bestconds.clear(); 
                      bestconds.add(newcond); 
                    }
                    else if (tknss == besttscore) 
                    { bestconds.add(newcond); } 
                  } 
                } 
                if (besttscore > 0) 
                {   
                  System.out.println(">>> Best matches for " + tk.realtrg + " are " + bestconds); 
                  tk.addCondition(bestcond);
                  // add negation of bestcond to the other ones

                  matchedconditions.add(bestcond + ""); 
                  matchedtargets.add(tk.realtrg);
                }
                else 
                { System.out.println(">>> No discriminator available for " + tk.realtrg); }  
              }  
            }  
          }  
        } 
      } 
    }  

  } 

  public void checkTargetFeatureCompleteness(Vector entities, Vector sources,
                            Vector thesaurus)
  { // are all target features used in the target of some entitymatch? 
    // if not, search for similar targets that are used, or similar sources. 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      em.checkTargetFeatureCompleteness(entitymatches,thesaurus); 
    } 
  } 

 

  public int size() 
  { return mymap.size(); } 

  public double nameSimilarity()
  { double res = 0; 
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      Entity s = em.src; 
      if (em.realsrc != null) 
      { s = em.realsrc; } 
      Entity t = em.trg; 
      if (em.realtrg != null) 
      { t = em.realtrg; } 
      double sim = ModelElement.similarity(s.getName().toLowerCase(), 
                                           t.getName().toLowerCase()); 
      res = res + sim; 
    } 
    return res; 
  } // better to use realsrc.       

  public double graphSimilarity(Map srcgraph, Map trggraph, Map mm1, Vector entities)
  { double res = 0; 
    if (srcgraph == null) 
    { return res; } 
    if (trggraph == null) 
    { return res; } 

    for (int i = 0; i < srcgraph.elements.size(); i++) 
    { Maplet em = (Maplet) srcgraph.elements.get(i); 
      Entity s = (Entity) em.source; 
      Integer sdist = (Integer) em.dest;
      if (s == null) { continue; } 
      Entity t = (Entity) mm1.get(s); 
      if (t != null) 
      { Integer tdist = (Integer) trggraph.get(t); 
        if (tdist != null && sdist != null) 
        { int sd = sdist.intValue(); 
          int td = tdist.intValue(); 
          if (sd == td) 
          { res++; } 
          else 
          { res = res + 1.0/(1 + Math.abs(sd - td)); } 
        } 
      } 
    } 
    return res; 
  }       

  public static double compositeSimilarity(Map mm, Vector entities) 
  { double res = 0; 
    for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mp = (Maplet) mm.elements.get(i); 
      Entity s = (Entity) mp.source; 
      Entity t = (Entity) mp.dest; 
      res = res + s.compositeSimilarity(t,mm,null,entities); 
    } 
    return res; 
  } 

  public static double compositeSimilarity(Map mm, ModelMatching modm, Vector entities) 
  { double res = 0; 
    for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mp = (Maplet) mm.elements.get(i); 
      Entity s = (Entity) mp.source; 
      Entity t = (Entity) mp.dest; 
      res = res + s.compositeSimilarity(t,mm,modm,entities); 
    } 
    return res; 
  } 

  public static double dssSimilarity(Map mm, ModelMatching modmatch, Vector entities,
                                     Vector thesaurus) 
  { double res = 0; 
    for (int i = 0; i < mm.elements.size(); i++) 
    { Maplet mp = (Maplet) mm.elements.get(i); 
      Entity s = (Entity) mp.source; 
      Entity t = (Entity) mp.dest; 
      res = res + s.similarity(t,modmatch,entities,false,false,thesaurus); 
    } 
    return res; 
  } 


  public void setAttributeMatches(Entity source, Entity target, Vector srcatts, Vector trgatts)
  { for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.src == source && em.trg == target)
      { em.setAttributeMatches(srcatts,trgatts); 
        return; 
      } 
    } 
  } 

  public void addAttributeMatches(Entity source, Entity target, Vector srcatts, Vector trgatts)
  { for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.src == source && em.trg == target)
      { em.setAttributeMatches(srcatts,trgatts); 
        return; 
      } 
    } 
  } 

  public void setAttributeMatches(Entity source, Entity target, Vector attmatches)
  { for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.src == source && em.trg == target)
      { em.setAttributeMatches(attmatches); 
        return; 
      } 
    } 
  } 




  public void setRealAttributeMatches(Entity source, Entity target, Vector srcatts, Vector trgatts)
  { for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.realsrc == source && em.realtrg == target)
      { em.setAttributeMatches(srcatts,trgatts); 
        return; 
      } 
    } 
  } 


  public Vector isUnconditionalEntitySplitting(Entity src, Vector alltargets)
  { Vector targets = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (src == em.realsrc) 
      { alltargets.add(em); 

        if (em.getCondition() == null) 
        { targets.add(em); } 
        else if ("true".equals(em.getCondition() + ""))
        { targets.add(em); } 
      } 
    } 
    
    if (targets.size() > 0 && targets.size() == alltargets.size())
    { System.out.println(">>> All mappings from " + src + " are unconditioned."); } 

    return targets; 
  } 
  
  private String qvtrKeyDeclarations(Vector entities)
  { String res = ""; 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i); 
      res = res + ent.qvtrKeyDeclarations(); 
    }
    return res + "\n\n"; 
  }

  public String qvtTransformation(Vector econvs, Vector seconvs, Vector esconvs, 
                                  Vector benums, Vector ebools, Vector entities)
  { String res = "transformation tau(source: MM1, target: MM2)\n" + 
                 "{ \n"; 
				 
	res = res + qvtrKeyDeclarations(entities); 

    for (int i = 0; i < econvs.size(); i++) 
    { Maplet mt = (Maplet) econvs.get(i); 
      Type t1 = (Type) mt.source; 
      Type t2 = (Type) mt.dest; 
      if (t1.isEnumeration() && t2.isEnumeration())
      { res = res + "  " + Type.enumConversionOpQVT(t1,t2) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < seconvs.size(); i++) 
    { Type et = (Type) seconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.stringEnumQueryOpQVTR() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < esconvs.size(); i++) 
    { Type et = (Type) esconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.enumStringQueryOpQVTR() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < benums.size(); i++) 
    { Type et = (Type) benums.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.booleanEnumOpQVTR(et) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < ebools.size(); i++) 
    { Type et = (Type) ebools.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.enumBooleanOpQVTR(et) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i); 
      res = res + tm.qvtfunction() + "\n\n"; 
    }
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      res = res + em.qvtrule1(entitymatches) + "\n"; 
    } // must include mapping to any primary key of em.realtarget

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget())
      { res = res + em.qvtrule2(entitymatches) + "\n"; }  
    } // omits mapping to primary keys of em.realtarget

    return res + "\n}\n"; 
  } 

  public String qvtBxTransformation(Vector econvs, Vector seconvs, Vector esconvs, 
                                  Vector benums, Vector ebools, Vector entities)
  { String res = "transformation tau(source: MM1, target: MM2)\n" + 
                 "{ \n"; 

    res = res + qvtrKeyDeclarations(entities) + "\n"; 
	
    for (int i = 0; i < econvs.size(); i++) 
    { Maplet mt = (Maplet) econvs.get(i); 
      Type t1 = (Type) mt.source; 
      Type t2 = (Type) mt.dest; 
      if (t1.isEnumeration() && t2.isEnumeration())
      { res = res + "  " + Type.enumConversionOpQVT(t1,t2) + "\n\n" + Type.enumConversionOpQVT(t2,t1) + "\n\n"; } 
    } 

    for (int i = 0; i < seconvs.size(); i++) 
    { Type et = (Type) seconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.stringEnumQueryOpQVTR() + "\n\n" + et.enumStringQueryOpQVTR() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < esconvs.size(); i++) 
    { Type et = (Type) esconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.enumStringQueryOpQVTR() + "\n\n" + et.stringEnumQueryOpQVTR() + "\n\n"; } 
    } 

    for (int i = 0; i < benums.size(); i++) 
    { Type et = (Type) benums.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.booleanEnumOpQVTR(et) + "\n\n" + Type.enumBooleanOpQVTR(et) + "\n\n"; } 
    } 

    for (int i = 0; i < ebools.size(); i++) 
    { Type et = (Type) ebools.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.enumBooleanOpQVTR(et) + "\n\n" + Type.booleanEnumOpQVTR(et); } 
    } 

    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i); 
      res = res + tm.qvtfunction() + "\n\n"; 
    }
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (!em.hasReplicationCondition())
      { res = res + em.qvtrule1bx(entitymatches) + "\n"; } 
    } // must include mapping to any primary key of em.realtarget

    String replicationRules = ""; 
	Vector abstractReplicationRules = new Vector(); 
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcreteTarget() && em.hasReplicationCondition())
      { replicationRules = replicationRules + em.qvtrule1InstanceReplication(entitymatches) + "\n"; 
	    String absRule = em.abstractReplicationRule(entitymatches); 
		if (abstractReplicationRules.contains(absRule)) { } 
		else 
		{ abstractReplicationRules.add(absRule); }  
	  } 
    } // must include mapping to any primary key of em.realtarget

    for (int j = 0; j < abstractReplicationRules.size(); j++)
	{ String absRule = (String) abstractReplicationRules.get(j); 
	  res = res + absRule; 
	}
	
	res = res + replicationRules; 
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget() && 
          !em.hasReplicationCondition())
      { res = res + em.qvtrule2bx(entitymatches) + "\n"; }  
    }  

    return res + "\n}\n"; 
  } 

  public String umlrsdsTransformation(Vector econvs, Vector esconvs, Vector seconvs, 
                                      Vector beconvs, Vector ebconvs)
  { String res = "package tau {\n\n" + 
                 "  usecase tauphase1 : void {\n"; 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && !em.hasReplicationCondition())
      { res = res + em.umlrsdsrule1() + "\n"; }  
    } 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.hasReplicationCondition())
      { res = res + em.umlrsdsrule1InstanceReplication() + "\n"; }  
    } 

    res = res + "}\n\n"; 

    res = res + "  usecase tauphase2 : void {\n"; 

  /* 
    for (int i = 0; i < econvs.size(); i++) 
    { Maplet mt = (Maplet) econvs.get(i); 
      Type t1 = (Type) mt.source; 
      Type t2 = (Type) mt.dest; 
      if (t1.isEnumeration() && t2.isEnumeration())
      { res = res + "  " + Type.enum2enumOp(t1,t2) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < seconvs.size(); i++) 
    { Type et = (Type) seconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.string2EnumOp() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < esconvs.size(); i++) 
    { Type et = (Type) esconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.enum2StringOp() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < beconvs.size(); i++) 
    { Type et = (Type) beconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.booleanEnumOp(et) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < ebconvs.size(); i++) 
    { Type et = (Type) ebconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.enumBooleanOp(et) + "\n\n"; } 
    } // Only include functions that are actually used

   */ 

    for (int i = 0; i < helpers.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) helpers.get(i); 
      res = res + bf.getKM3() + "\n"; 
    } 

    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i); 
      res = res + tm.umlrsdsfunction() + "\n\n"; 
    }
	

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget() && 
          !em.hasReplicationCondition())
      { res = res + em.umlrsdsrule2(entitymatches) + "\n"; }  
    } 

    return res + "\n  }\n\n}\n"; 
  } 

  public String qvtoTransformation(Vector econvs, Vector esconvs, Vector seconvs, 
                                   Vector benums, Vector ebools, Vector thesaurus)
  { String res = "modeltype MM1 uses 'http://mm1.com';\n" + 
                 "modeltype MM2 uses 'http://mm2.com';\n\n\n" + 
                 "transformation tau(in src : MM1, out trg : MM2);\n\n"; 

    for (int i = 0; i < econvs.size(); i++) 
    { Maplet mt = (Maplet) econvs.get(i); 
      Type t1 = (Type) mt.source; 
      Type t2 = (Type) mt.dest; 
      if (t1.isEnumeration() && t2.isEnumeration())
      { res = res + "  " + Type.enumConversionOpQVTO(t1,t2) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < seconvs.size(); i++) 
    { Type et = (Type) seconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.string2EnumOpQVTO() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < esconvs.size(); i++) 
    { Type et = (Type) esconvs.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.enum2StringOpQVTO() + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < benums.size(); i++) 
    { Type et = (Type) benums.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.booleanEnumOpQVTO(et,thesaurus) + "\n\n"; } 
    } // Only include functions that are actually used

    for (int i = 0; i < ebools.size(); i++) 
    { Type et = (Type) ebools.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + Type.enumBooleanOpQVTO(et,thesaurus) + "\n\n"; } 
    } // Only include functions that are actually used

    res = res + "main() {\n"; 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.qvtomain1() + "\n"; }  
    } 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget())
      { res = res + em.qvtomain2() + "\n"; }  
    } 

    res = res + "}\n\n";

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.qvtorule1() + "\n"; }  
      else 
      { res = res + em.qvtodisjunctsrule(entitymatches) + "\n"; }
    } 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget())
      { res = res + em.qvtorule2(entitymatches) + "\n"; }  
    } 

    return res; 
  } 

  public String atlTransformation(Vector types)
  { String res = ""; 

    java.util.Map fromCommonSource = new java.util.HashMap(); 
    // entity name --> Vector of mappings with same entity

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget())
      { Entity emsrc = em.realsrc; 
        String key = emsrc.getName() + "{" + em.getCondition() + "}"; 
        Vector emsrcmappings = (Vector) fromCommonSource.get(key); 
        if (emsrcmappings == null) 
        { emsrcmappings = new Vector(); 
          em.isSecondary = false; 
        }
        else 
        { em.isSecondary = true;
          EntityMatching r1 = (EntityMatching) emsrcmappings.get(0);  
          em.isSecondaryTo = r1.getName(); 
        } 
        emsrcmappings.add(em);  
        fromCommonSource.put(key,emsrcmappings); 
      }   
    } 

    System.out.println(">> ATL rule groups: " + fromCommonSource); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete() && em.isConcreteTarget() && em.isPrimary())
      { Vector secondaryRules = new Vector(); 
        String rkey = em.realsrc.getName() + "{" + em.getCondition() + "}"; 
        secondaryRules.addAll((Vector) fromCommonSource.get(rkey)); 
        secondaryRules.remove(em); 
        res = res + em.atlrule(entitymatches,secondaryRules) + "\n"; 
      }  
    } 

    return res + "\n\n"; 
  } 


  public EtlModule etlTransformation(Vector types, Vector benums, Vector benames, 
                                     Vector ebools, Vector ebnames)
  { EtlModule res = new EtlModule("Autogenerated"); 

    for (int i = 0; i < benums.size(); i++) 
    { Type et = (Type) benums.get(i); 
      String trgname = (String) benames.get(i); 
      if (et.isEnumeration())
      { BehaviouralFeature bf = 
          Type.booleanEnumConversionFunctionETL(et,trgname); 
        if (bf != null) 
        { res.addOperation(bf); }
      }  
    } // Only include functions that are actually used

    for (int i = 0; i < ebools.size(); i++) 
    { Type et = (Type) ebools.get(i); 
      String trgnme = (String) ebnames.get(i); 
      if (et.isEnumeration())
      { BehaviouralFeature bf = Type.enumBooleanConversionFunctionETL(et,trgnme);
        if (bf != null) 
        { res.addOperation(bf); }
      }  
    } // Only include functions that are actually used

    Vector auxrules = new Vector(); 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      // if (em.isConcrete())
      res.addRule(em.etlrule(entitymatches,auxrules));  
    } 

    for (int y = 0; y < auxrules.size(); y++) 
    { Object arx = auxrules.get(y); 
      if (arx instanceof TransformationRule)
      { res.addRule((TransformationRule) arx); } 
      else if (arx instanceof BehaviouralFeature)
      { res.addOperation((BehaviouralFeature) arx); } 
    } 
    return res; 
  } 

  public static Entity lookupMatch(Entity s, Vector ems) 
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.src == s) 
      { return em.trg; } 
    } 
    return null; 
  } 

  public Entity lookupMatch(Entity s) 
  { return lookupMatch(s,entitymatches); } 

  public static EntityMatching getMatching(Entity s, Vector ems)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.src == s) 
      { return em; } 
      else if (em.src != null && em.src.getName().equals(s.getName()))
      { return em; } 
    } 
    return null; 
  } 

  public static Vector getMatchings(Entity s, Vector ems)
  { Vector res = new Vector(); 
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.src == s) 
      { res.add(em); } 
      else if (em.src != null && em.src.getName().equals(s.getName()))
      { res.add(em); } 
    } 
    return res; 
  } 

  public EntityMatching getMatching(Entity s) 
  { return getMatching(s,entitymatches); } 


  public static Entity lookupRealMatch(Entity s, Vector ems) 
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { return em.realtrg; } 
    } 
    return null; 
  } 

  public static Vector lookupRealMatches(Entity s, Vector ems) 
  { Vector res = new Vector(); 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { res.add(em.realtrg); } 
    } 
    return res; 
  } 

  public static EntityMatching findEntityMatching(Entity s, Vector ems)
  { // some matching that can map elements of type s. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { return em; } 
    }  

    Entity srcsup = s.getSuperclass(); 
    if (srcsup != null) 
    { return ModelMatching.findEntityMatching(srcsup,ems); }
    return null;      
  }

  public static Vector findEntityMatchings(Entity s, Vector ems)
  { // closest matchings that can map elements of type s. 
    Vector res = new Vector(); 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { res.add(em); } 
      else   
      { Entity srcsup = s.getSuperclass(); 
        if (srcsup != null) 
        { res.addAll(ModelMatching.findEntityMatchings(srcsup,ems)); }
      } 
    } 
    return null;      
  }

  public static EntityMatching findEntityMatchingFor(Entity s, Entity t, Vector ems)
  { // some matching that can map elements of type s to type t. 
    if (s == null || t == null)
	{ return null; }
	
    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s && Entity.isDescendant(em.realtrg,t)) 
      { return em; } 
    }  

    Entity srcsup = s.getSuperclass(); 
    if (srcsup != null) 
    { return ModelMatching.findEntityMatchingFor(srcsup,t,ems); }
    return null;      
  }

  public static EntityMatching findEntityMatchingByTarget(Entity t, Vector ems)
  { // some matching that can map elements to type t. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realtrg == t) 
      { return em; } 
    }  

    return null;      
  }

  public static Vector findEntityMatchingsByTarget(Entity t, Vector ems)
  { // all matchings that can map elements to type t. 

    Vector res = new Vector(); 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realtrg == t) 
      { res.add(em); } 
    }  

    return res;      
  }

  public static EntityMatching findSuperclassMatchingByTarget(Entity t, Vector ems)
  { // some matching that can map elements to type t. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realtrg == t) 
      { return em; } 
    }  

    Entity trgsup = t.getSuperclass(); 
    if (trgsup != null) 
    { return ModelMatching.findSuperclassMatchingByTarget(trgsup,ems); }
    return null;      
  }

  public static EntityMatching findSuperclassMatchingBySourceTarget(Entity s, Entity t, Vector ems)
  { // some matching with s descendant of realsrc, t of realtrg

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s && Entity.isDescendant(t,em.realtrg)) 
      { return em; } 
    }  

    Entity srcsup = s.getSuperclass(); 
    if (srcsup != null) 
    { return ModelMatching.findSuperclassMatchingBySourceTarget(srcsup,t,ems); }
    return null;      
  }

  public static Vector findSpecialisedMatchingsBySourceTarget(Entity s, Entity t, Vector ems)
  { // all matchings with t = or descendant of realtrg
    Vector res = new Vector(); 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s && Entity.isDescendant(t,em.realtrg)) 
      { res.add(em); } 
    }  

    if (res.size() == 0) 
    { Vector srcsubs = s.getSubclasses(); 
      for (int j = 0; j < srcsubs.size(); j++) 
      { Entity srcsub = (Entity) srcsubs.get(j); 
        res.addAll(findSpecialisedMatchingsBySourceTarget(srcsub,t,ems)); 
      } 
    } 
    return res; 
  }

  public static EntityMatching getEntityMatching(Entity s, Vector ems)
  { // the exact matching that maps elements of type s. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { return em; } 
    }  

    return null;      
  }

  public static Vector getEntityMatchings(Entity s, Vector ems)
  { // all matchings that map elements of type s. 
    Vector res = new Vector(); 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { res.add(em); } 
    }  

    return res;      
  }

  public static EntityMatching getEntityMatching(Entity s, Entity t, Vector ems)
  { // the exact matching that maps elements of type s to t. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.src == s && em.trg == t) 
      { return em; } 
    }  

    return null;      
  }

  public static EntityMatching getRealEntityMatching(Entity s, Entity t, Vector ems)
  { // the exact matching that maps elements of type s to t. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s && em.realtrg == t) 
      { return em; } 
    }  

    return null;      
  }

  public static EntityMatching findExactMatchBySourceTarget(Entity s, Entity t, Vector ems)
  { return getRealEntityMatching(s,t,ems); }
  
  public static EntityMatching getAncestorMatching(Entity srcsup, Entity realtrg, Vector ems)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == srcsup && 
          (realtrg == em.realtrg || 
           Entity.isDescendant(realtrg,em.realtrg))
          )  
      { return em; } 
      else if (srcsup.getSuperclass() != null) 
      { EntityMatching emx = getAncestorMatching(srcsup.getSuperclass(), realtrg, ems); 
        if (emx != null) 
        { return emx; } 
      } 
    }
    return null;  
  }

  public EntityMatching getEntityMatching(Entity s)
  { return getEntityMatching(s,entitymatches); } 

  public Vector getAttributeMapping(Entity ent)
  { EntityMatching em = findEntityMatching(ent, entitymatches);
    if (em != null) 
    { return em.attributeMappings; } 
    return null; 
  }  

  public static boolean isUnusedTargetByName(Attribute att, Vector ems)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.isUnusedTargetByName(att)) { } 
      else 
      { return false; } 
    } 
    return true; 
  } 

  public static boolean isUnusedTarget(Attribute att, Vector ems)
  { for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.isUnusedTarget(att)) { } 
      else 
      { return false; } 
    } 
    return true; 
  } 

  public static Vector findCompatibleSourceAttributes(Attribute tatt, Vector satts, EntityMatching em, Vector ems)
  { // Some attribute of em.realsrc which is type-compatible 
    // with tatt. 
    return findCompatibleSourceAttributes(tatt,satts,em.realsrc,ems); 
  } 

  public static Vector findCompatibleSourceAttributes(Attribute tatt, Vector satts, Entity sent, Vector ems)
  { // Some attribute of sent which is type-compatible 
    // with tatt. 

    Vector res = new Vector(); 
    Vector allsats = new Vector(); 
    allsats.addAll(satts); 
    if (tatt.isEntity())
    { Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL);
      selfatt.setElementType(new Type(sent));  
      allsats.add(selfatt); 
    } 

    for (int i = 0; i < allsats.size(); i++) 
    { Attribute satt = (Attribute) allsats.get(i); 
	
	  // System.out.println("???? testing " + satt + " as a match for " + tatt); 
	  
      if (compatibleType(satt,tatt,ems))
      { System.out.println(">> " + satt + " matches by type to " + tatt); 
        res.add(satt); 
      }
      else if (compatibleElementType(satt,tatt,ems)) 
      { System.out.println(">> " + satt + " : " + satt.getType() + " is compatible to element type of " + tatt + " : " + tatt.getType()); 
        res.add(satt); 
      }  
    } 
    return res; 
  } 

  public static boolean compatibleElementType(Attribute satt, Attribute tatt, Vector ems)
  { // true if satt could be matched to tatt elements, given ems
    Type setype = satt.getType(); 
    Type tetype = tatt.getType(); 
    
    if (!Type.isCollectionType(setype) && Type.isCollectionType(tetype))
    { tetype = tetype.getElementType();
      if (tetype == null) { return false; } 

      Attribute telematt = new Attribute(tatt.getName() + "Element", tetype, ModelElement.INTERNAL); 
      return compatibleType(satt,telematt,ems); 
    }
    return false; 
  } 

  public static boolean compatibleSourceElementType(Attribute satt, Attribute tatt, Vector ems)
  { // true if satt elements could be matched to tatt, given ems
    Type setype = satt.getType(); 
    Type tetype = tatt.getType(); 
    
    if (Type.isCollectionType(setype) && !Type.isCollectionType(tetype))
    { setype = setype.getElementType();
      if (setype == null) { return false; } 

      Attribute selematt = new Attribute(satt.getName() + "Element", setype, ModelElement.INTERNAL); 
      return compatibleType(selematt,tatt,ems); 
    }
    return false; 
  } 

  public static boolean compatibleType(Attribute satt, Attribute tatt, Vector ems)
  { // true if satt could be matched to tatt, given ems
    Type setype = satt.getType(); 
    Type tetype = tatt.getType(); 
    
    if (Type.isCollectionType(setype) && Type.isCollectionType(tetype))
    { setype = setype.getElementType(); 
      tetype = tetype.getElementType(); 
    }
	
    if (setype != null && tetype != null && 
        setype.isEntity() && tetype.isEntity())
    { Entity e1 = setype.getEntity(); 
      Entity e2 = tetype.getEntity(); 
        // and e1 maps to e2 or a subclass/superclass of it 
      Vector e1imgs = ModelMatching.lookupRealMatches(e1,ems); 
      for (int i = 0; i < e1imgs.size(); i++) 
      { Entity e1img = (Entity) e1imgs.get(i); 
        if (e1img == e2) 
        { return true; } 
        if (Entity.isAncestor(e2,e1img) || Entity.isAncestor(e1img,e2))
        { return true; } 
      } 
    } 
    else if ((setype + "").equals("int") && 
             (tetype + "").equals("long")) 
    { return true; } 
    else if ((setype + "").equals(tetype + "")) 
    { return true; }  
    return false; 
  } 

  public static Vector findBaseTypeCompatibleSourceAttributes(Attribute tatt, Vector satts, EntityMatching em, Vector ems)
  { // Some attribute of em.realsrc which is type-compatible 
    // with tatt. 
    return findBaseTypeCompatibleSourceAttributes(tatt, satts, em.realsrc, ems); 
  } 

  public static Vector findBaseTypeCompatibleSourceAttributes(Attribute tatt, Vector satts, Entity sent, Vector ems)
  { // Some attribute of sent which is basetype-compatible 
    // with tatt. 

    Vector res = new Vector(); 
    Vector allsats = new Vector(); 
    allsats.addAll(satts); 
    if (tatt.isEntity())
    { Attribute selfatt = new Attribute("self", new Type(sent), ModelElement.INTERNAL);
      selfatt.setElementType(new Type(sent));  
      allsats.add(selfatt); 
    } 

    for (int i = 0; i < allsats.size(); i++) 
    { Attribute satt = (Attribute) allsats.get(i); 
      if (compatibleBaseTypes(satt,tatt,ems))
      { System.out.println(">> " + satt + " matches by base type to " + tatt); 
        res.add(satt); 
      }
    } 
    return res; 
  } 

  public static boolean compatibleBaseTypes(Attribute satt, Attribute tatt, Vector ems)
  { // true if base type of satt can map to base type of tatt,
    // given ems
    Type setype = satt.getType(); 
    Type tetype = tatt.getType(); 
    
	if (Type.isCollectionType(setype))
	{ setype = setype.getElementType(); }

      if (Type.isCollectionType(tetype))
      { tetype = tetype.getElementType(); }
	
    if (setype != null && tetype != null && 
        setype.isEntity() && tetype.isEntity())
    { Entity e1 = setype.getEntity(); 
      Entity e2 = tetype.getEntity(); 
        // and e1 maps to e2 or a subclass/superclass of it 
      Vector e1imgs = ModelMatching.lookupRealMatches(e1,ems); 
      for (int i = 0; i < e1imgs.size(); i++) 
      { Entity e1img = (Entity) e1imgs.get(i); 
        if (e1img == e2) 
        { return true; } 
        if (Entity.isAncestor(e2,e1img) || Entity.isAncestor(e1img,e2))
        { return true; } 
      } 
    } 
    else if ((setype + "").equals("int") && 
             (tetype + "").equals("long")) 
    { return true; } 
    else if ((setype + "").equals(tetype + "")) 
    { return true; }  
    return false; 
  } 

  public static boolean compatibleReverse(Attribute satt, Attribute tatt, Vector ems)
  { // true if satt could be matched to tatt~, given ems
    Type setype = satt.getElementType(); 
    Entity e2 = tatt.getEntity(); // the entity1 of an association represented as an attribute 
    
    if (setype != null && e2 != null && 
        setype.isEntity())
    { Entity e1 = setype.getEntity(); 
        // and e1 maps to e2 or a subclass of it 
      Vector e1imgs = ModelMatching.lookupRealMatches(e1,ems);
      for (int i = 0; i < e1imgs.size(); i++)  
      { Entity e1img = (Entity) e1imgs.get(i); 
        if (e1img == e2) 
        { return true; } 
        if (Entity.isAncestor(e2,e1img))
        { return true; }
      }  
    } 
    return false; 
  } 

  public static Expression defineCombinedExpression(Attribute unused, Attribute used, 
                                                    Attribute target, Vector auxvariables)
  { Type unusedType = unused.getType();
    Type usedType = used.getType();
    Type targetType = target.getType();
    Expression usedExp = new BasicExpression(used);
    usedExp.setUmlKind(Expression.ATTRIBUTE); 
    Expression unusedExp = new BasicExpression(unused);
    unusedExp.setUmlKind(Expression.ATTRIBUTE); 
    
    if (used.isDirect() &&  
        "String".equals(targetType.getName()))
    { BasicExpression sep = new BasicExpression("\" ~ \"");
      sep.setType(new Type("String", null));
      sep.setElementType(new Type("String", null));
      Expression res = 
        new BinaryExpression("+", new BinaryExpression("+", usedExp, sep), unusedExp);
      res.setType(new Type("String", null));
      res.setElementType(new Type("String", null));
      res.setMultiplicity(ModelElement.ONE); 
      auxvariables.add(used); 
      auxvariables.add(unused); 
      return res;
    }

    if (Type.isEntityCollection(targetType)) { } 
    else 
    { return null; } 

    Expression r = null;
    Attribute var = new Attribute(Identifier.nextIdentifier("var$"),used.getElementType(),
                                  ModelElement.INTERNAL);
    var.setElementType(used.getElementType()); 

    if (target.isManyValued())
    { if (unused.endsWith(used) && unused.first().isCyclic())
      { // Set{self}->closure(f)
        Attribute f = unused.first();
        Entity fent = f.getEntity(); 
        if (fent == null) 
        { return null; } 

        BasicExpression fexp = new BasicExpression(f);
        fexp.setUmlKind(Expression.ATTRIBUTE); 
        Expression selfexp = new BasicExpression("self");
        selfexp.setType(new Type(fent));
        selfexp.setElementType(new Type(fent));
        SetExpression setself = new SetExpression();
        setself.addElement(selfexp);
        BinaryExpression clos = new BinaryExpression("->closure",setself,fexp);
        if (used.isManyValued())
        { r = new BinaryExpression("->unionAll",clos,usedExp);
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        else
        { r = new BinaryExpression("->collect",clos,usedExp); 
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        r.setType(new Type("Set",null)); 
        r.setElementType(used.getElementType()); 
        r.setMultiplicity(ModelElement.MANY); 
        return r;
      }

      if (unused.startsWith(used) && unused.last().isCyclic())
      { // Set{used}->closure(f)
        Attribute f = unused.last();
        BasicExpression fexp = new BasicExpression(f);
        fexp.setUmlKind(Expression.ATTRIBUTE); 
        SetExpression setused = new SetExpression();
        setused.addElement(usedExp);
        if (used.isManyValued())
        { r = new BinaryExpression("->closure",usedExp,fexp);
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        else
        { r = new BinaryExpression("->closure",setused,fexp); 
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        r.setType(new Type("Set",null)); 
        r.setElementType(used.getElementType()); 
        r.setMultiplicity(ModelElement.MANY); 
        return r;
      }

      if (used.startsWith(unused) && used.last().isCyclic())
      { // Set{self}->closure(f)
        Attribute f = used.last();
        BasicExpression fexp = new BasicExpression(f);
        fexp.setUmlKind(Expression.ATTRIBUTE); 
        SetExpression setunused = new SetExpression();
        setunused.addElement(unusedExp);
        if (unused.isManyValued())
        { r = new BinaryExpression("->closure",unusedExp,fexp);
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        else
        { r = new BinaryExpression("->closure",setunused,fexp); 
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        r.setType(new Type("Set",null)); 
        r.setElementType(used.getElementType()); 
        r.setMultiplicity(ModelElement.MANY); 
        return r;
      }

      if (used.endsWith(unused) && used.first().isCyclic())
      { // Set{self}->closure(f)
        Attribute f = used.first();
        BasicExpression fexp = new BasicExpression(f);
        fexp.setUmlKind(Expression.ATTRIBUTE); 
        Expression selfexp = new BasicExpression("self");
        selfexp.setType(new Type(f.getEntity()));
        selfexp.setElementType(new Type(f.getEntity()));
        SetExpression setself = new SetExpression();
        setself.addElement(selfexp);
        BinaryExpression clos = new BinaryExpression("->closure",setself,fexp);
        if (unused.isManyValued())
        { r = new BinaryExpression("->unionAll",clos,unusedExp);
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        else
        { r = new BinaryExpression("->collect",clos,unusedExp); 
          // var.setType(used.getElementType());
          auxvariables.add(var);
        }
        r.setType(new Type("Set",null)); 
        r.setElementType(used.getElementType()); 
        r.setMultiplicity(ModelElement.MANY); 
        return r;
      }

      /* Following is also ok with collections of value types */ 

      if (used.isDirect() && unused.isDirect()) { } 
      else 
      { return null; } 
      
      Type vartype = used.getElementType(); 
      Type t1 = used.getElementType(); 
      Type t2 = unused.getElementType(); 
      if (t1 != null && t2 != null && 
          t1.isEntity() && t2.isEntity())
      { Entity e1 = t1.getEntity(); 
        Entity e2 = t2.getEntity(); 
        Entity e3 = Entity.commonSuperclass(e1,e2); 
        if (e3 != null) 
        { vartype = new Type(e3); } 
      } 
        
      if (used.isManyValued())
      { // var.setType(used.getElementType());
        
        auxvariables.add(var);
        if (unused.isManyValued())
        { r = new BinaryExpression("->union",usedExp,unusedExp); }
        else
        { r = new BinaryExpression("->including",usedExp,unusedExp); }
        r.setType(usedExp.getType()); 
        r.setMultiplicity(ModelElement.MANY); 
      }
      else if (unused.isManyValued())
      { // var.setType(usedType);
        auxvariables.add(var);
        r = new BinaryExpression("->including",unusedExp,usedExp);
        r.setType(unusedExp.getType()); 
        r.setMultiplicity(ModelElement.MANY); 
      }
      else
      { // var.setType(usedType);
        auxvariables.add(var);
        r = new SetExpression();
        ((SetExpression) r).addElement(usedExp);
        ((SetExpression) r).addElement(unusedExp);
        ((SetExpression) r).setOrdered(target.isOrdered());
        r.setType(new Type("Sequence",null)); 
        r.setMultiplicity(ModelElement.MANY); 
      }
      // var.setType(vartype);
      var.setElementType(vartype);  
      if (r != null) 
      { r.setElementType(vartype); } 
        
      // System.out.println("*** Element type of " + r + " is " + vartype + " is multiple-valued = " + 
      //                    r.isMultipleValued()); 
      return r; 
    }

    // var.setType(vartype); 

    if (r != null) 
    { r.setElementType(used.getElementType()); } 

    return r;
  }

  public static Expression defineCombinedExpression(Vector sametargets,  
                                                    Attribute target, Vector auxvariables)
  { Type targetType = target.getType();
    
    if ("String".equals(targetType.getName()))
    { // assume all sources are strings

      BasicExpression sep = new BasicExpression("\"~\"");
      sep.setType(new Type("String", null));
      sep.setElementType(new Type("String", null));
      AttributeMatching am1 = (AttributeMatching) sametargets.get(0); 
      Attribute am1src = am1.getSource(); 
      auxvariables.add(am1src); 

      Expression res = new BasicExpression(am1src); 
      for (int i = 1; i < sametargets.size(); i++) 
      { AttributeMatching ami = (AttributeMatching) sametargets.get(i);
        Attribute amisrc = ami.getSource(); 
        BasicExpression amiexp = new BasicExpression(amisrc);  
        res = 
          new BinaryExpression("+", new BinaryExpression("+", res, sep), amiexp);
        res.setType(new Type("String", null));
        res.setElementType(new Type("String", null));
        res.setMultiplicity(ModelElement.ONE); 
        auxvariables.add(amisrc); 
      } 
      return res;
    }

    if ("boolean".equals(targetType.getName()))
    { // all sources are booleans

      AttributeMatching am1 = (AttributeMatching) sametargets.get(0); 
      Attribute am1src = am1.getSource(); 
      auxvariables.add(am1src); 

      Expression res = new BasicExpression(am1src); 
      for (int i = 1; i < sametargets.size(); i++) 
      { AttributeMatching ami = (AttributeMatching) sametargets.get(i);
        Attribute amisrc = ami.getSource(); 
        BasicExpression amiexp = new BasicExpression(amisrc);  
        res = 
          new BinaryExpression("or", res, amiexp);
        res.setType(new Type("boolean", null));
        res.setElementType(new Type("boolean", null));
        res.setMultiplicity(ModelElement.ONE); 
        auxvariables.add(amisrc); 
      } 
      return res;
    }

    if (Type.isCollectionType(targetType)) { } 
    else 
    { return null; } 

    AttributeMatching am1 = (AttributeMatching) sametargets.get(0); 
    Attribute used = am1.getSource(); 
    BasicExpression usedExp = new BasicExpression(used); 

    auxvariables.add(used); 

    Expression r = null;
    Attribute var = new Attribute(Identifier.nextIdentifier("var$"),used.getElementType(),
                                  ModelElement.INTERNAL);
    var.setElementType(used.getElementType()); 
    auxvariables.add(var);
      
    if (used.isManyValued())
    { r = new BasicExpression(used); } 
    else 
    { r = new SetExpression();
      ((SetExpression) r).addElement(usedExp);
      ((SetExpression) r).setOrdered(target.isOrdered());
    } 
      
    /* Type vartype = used.getElementType(); 
    Type t1 = used.getElementType(); 
      Type t2 = unused.getElementType(); 
      if (t1 != null && t2 != null && 
          t1.isEntity() && t2.isEntity())
      { Entity e1 = t1.getEntity(); 
        Entity e2 = t2.getEntity(); 
        Entity e3 = Entity.commonSuperclass(e1,e2); 
        if (e3 != null) 
        { vartype = new Type(e3); } 
      } */  
    
    for (int i = 1; i < sametargets.size(); i++) 
    { AttributeMatching am2 = (AttributeMatching) sametargets.get(i); 
      Attribute unused = am2.getSource(); 
      Expression unusedExp = new BasicExpression(unused); 
  
      if (unused.isManyValued())
      { r = new BinaryExpression("->union",r,unusedExp); }
      else
      { r = new BinaryExpression("->including",r,unusedExp); }

      r.setType(usedExp.getType()); 
      r.setMultiplicity(ModelElement.MANY); 
      // System.out.println("*** Element type of " + r + " is " + vartype + " is multiple-valued = " + 
      //                    r.isMultipleValued()); 
    }

    // var.setType(vartype); 

    if (r != null) 
    { r.setElementType(used.getElementType()); } 

    return r;
  }

  public static AttributeMatching defineFeatureSplitting(AttributeMatching am, Attribute newtrg)
  { Attribute src = am.src; 
    Attribute oldtrg = am.trg; 

    Type srctype = src.getType();
    Type trgtype = oldtrg.getType();
    Type newtype = newtrg.getType();

    Expression srcExp = new BasicExpression(src);
    srcExp.setUmlKind(Expression.ATTRIBUTE); 
    Expression newExp = new BasicExpression(newtrg);
    newExp.setUmlKind(Expression.ATTRIBUTE); 
    
    if ("String".equals(srctype.getName()) && "String".equals(trgtype.getName()) && 
        "String".equals(newtype.getName()))
    { BasicExpression sep = new BasicExpression("\" ~ \"");
      sep.setType(new Type("String", null));
      sep.setElementType(new Type("String", null));
      Expression src1 = 
        new BinaryExpression("->before", srcExp, sep);
      src1.setType(new Type("String", null));
      src1.setElementType(new Type("String", null));
      src1.setMultiplicity(ModelElement.ONE); 
      Expression src2 = 
        new BinaryExpression("->after", srcExp, sep);
      src2.setType(new Type("String", null));
      src2.setElementType(new Type("String", null));
      src2.setMultiplicity(ModelElement.ONE);
      // am.srcvalue = src1; 
      // am.elementVariable = src;
      Attribute var1 = new Attribute(Identifier.nextIdentifier("var$"),srctype,
                                  ModelElement.INTERNAL);
      var1.setElementType(srctype); 
      am.setExpressionMatch(src1,am.trg,var1);  
      Attribute var2 = 
        new Attribute(Identifier.nextIdentifier("var$"),srctype,
                                   ModelElement.INTERNAL);
      var2.setElementType(srctype); 
      Vector auxvariables = new Vector(); 
      // auxvariables.add(var1); 
      auxvariables.add(var2); 
      AttributeMatching res = new AttributeMatching(src2,newtrg,var2,auxvariables);   
      
      return res;
    }
    return null; 
  }

  public void checkModel(ModelSpecification mod, Vector entities, Vector types)
  { // For each feature mapping f |--> g, checks for each 
    // ex : E and corresponding fx : F, that ex.f = fx.g in 
    // the model. 

    Vector removed = new Vector(); 
    Vector functions = new Vector(); 
    Vector tms = typematches; 

  /*  for (int i = 0; i < entities.size(); i++) 
    { Entity tent = (Entity) entities.get(i); 
      if (tent.isTarget())
      { java.util.HashMap mergings = mod.objectMergings(tent.getName()); 
        if (mergings != null && mergings.size() > 0)
        { System.out.println(">>> Instance merging to " + tent + " in model: " + mergings); 
          mod.checkMergingCondition(tent,entities,mergings); 
        } 
      } 
    }  */ 

    // Are there extra (unexpected) correspondences? 
    Vector extraems = mod.extraEntityMatches(entitymatches); 
    if (extraems.size() > 0)
    { System.out.println(">>> Additional class mappings discovered in the model: "); 
      for (int j = 0; j < extraems.size(); j++) 
      { EntityMatching emx = (EntityMatching) extraems.get(j); 
        System.out.println("Additional mapping: \n" + emx); 
      }
    }

    combineMatches(extraems); // and the mymap. 
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = 
          (EntityMatching) entitymatches.get(i); 
      em.checkModelConditions(mod,this,entitymatches,
                              removed,functions); 
    } // check the conditions for each rule.  

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = 
           (EntityMatching) entitymatches.get(i); 
      em.recheckModel(mod,this,
                entitymatches,removed,functions,tms); 
    } // check feature mappings of each em.  

    helpers.addAll(functions); 
    entitymatches.removeAll(removed); 
	
    removed = new Vector(); 
    Vector added = new Vector(); 

    Vector unusedSources = unusedSourceEntities(entities);

    
    Vector cems = mod.completeClassMappings(entities,
                         unusedSources,entitymatches); 

    mod.extraAttributeMatches(entitymatches,typematches); 

    mod.checkPrimaryKeyAssignments(entitymatches); 

    Vector rem = new Vector(); 
    Vector fcns = new Vector(); 
	
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = 
        (EntityMatching) entitymatches.get(i); 
      System.out.println(">>> Rechecking conditions and mappings for " + em); 
      Vector newems = 
        em.recheckModel(mod,this,entitymatches,rem,fcns,tms);
      if (newems.size() > 0) 
      { removed.add(em); 
        added.addAll(newems); 
      }  
    } 

    mod.defineTargetQueryFunctions(entitymatches,typematches); 

    Vector sourceClasses = new Vector(); 

    for (int i = 0; i < entities.size(); i++) 
    { Entity tent = (Entity) entities.get(i); 
      if (tent.isTarget())
      { Vector unmerged = new Vector(); 
        java.util.HashMap mergings = mod.objectMergings(tent.getName(),sourceClasses, unmerged); 
        if (mergings != null && mergings.size() > 0)
        { System.out.println(">>> Instance merging from classes " + sourceClasses + " to " + tent + " in model: " + mergings); 
          System.out.println(">>> Unmerged source elements are: " + unmerged);  
          EntityMatching sent2tent = mod.checkMergingCondition(tent,entities,entitymatches,mergings,unmerged); 
          mod.checkMergingMappings(tent,sent2tent,sourceClasses,entities,entitymatches,mergings,unmerged);
        }
      } 
    } 

    for (int i = 0; i < entities.size(); i++) 
    { Entity sent = (Entity) entities.get(i); 
      if (sent.isSource())
      { Vector splitsources = new Vector(); 
        Vector splittargets = new Vector(); 
        java.util.HashMap splittings = 
           mod.objectSplittings(sent.getName(),
                          splitsources,splittargets); 
        if (splittings != null && splittings.size() > 0)
        { System.out.println(">>> Instance replication in model: " + splittings); 
          mod.checkSplittingCondition(sent,
                    entities,entitymatches,splittings); 
        } 
      } 
    } 


    System.out.println(">>> Removed: " + removed); 
    System.out.println(">>> Added: " + added); 

    entitymatches.removeAll(removed); 
    entitymatches.addAll(added); 
  } 

  public java.util.Map convert2CSTL()
  { java.util.Map rulecategories = new java.util.HashMap(); 
  
    for (int i = 0; i < typematches.size(); i++) 
    { TypeMatching tm = (TypeMatching) typematches.get(i); 
      String ffile = tm.getName(); 
      File chtml = new File("output/" + ffile + ".cstl"); 
      try
      { PrintWriter chout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(chtml)));
        tm.cstlfunction(chout);
        chout.close(); 
      } catch (Exception _e) { }  
    }


    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i);
      CGRule r = em.convert2CSTL(); 
      Entity sent = em.realsrc; 
      if (sent != null) 
      { String sname = sent.getName(); 
        Vector srules = (Vector) rulecategories.get(sname);
        if (srules == null) 
        { srules = new Vector(); 
          rulecategories.put(sname,srules); 
        }
 
        if (srules.contains(r)) 
        { System.err.println("! Warning: duplicate CSTL rules produced from TL: " + r); 
		  System.err.println("! Only one copy will be retained.");
		} 
        else 
        { srules.add(r); }
      }   
    } 
    return rulecategories; 
  } 


}




