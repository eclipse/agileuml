import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
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
  { String res = "Abstract transformation: \n"; 
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      // Entity s = em.realsrc; 
      // Entity t = em.realtrg; 
      // res = res + "  " + s + " |--> " + t + "\n"; 
      res = res + em + "\n";  
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

  public void removeInvalidMatchings()
  { // remove att -> tatt.f where f is in sources of its owner's attribute matching

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
      { em.copyAttributeMappingsToSubclasses(srcsubs,entitymatches,thesaurus,mymap,entities); } 
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

  public void checkEntityMapCompleteness(Vector targets) 
  { // for every concrete target entity t, warn if no mapping to t or any 
    // superclass of t, if there is a superclass mapping s -> tsup, prompt  
    // for a mapping condition { tcond } s --> t 

   for (int i = 0; i < targets.size(); i++) 
   { Entity t = (Entity) targets.get(i); 
     if (t.isConcrete() && t.isTarget())
     { EntityMatching emt = findEntityMatchingByTarget(t,entitymatches);     
       if (emt == null) 
       { EntityMatching supemt = findSuperclassMatchingByTarget(t,entitymatches); 
         if (supemt == null) 
         { System.out.println(">>> Neither " + t + 
                              " or any superclass is in the range of the mapping"); 
         } 
         else 
         { System.out.println(">>> Concrete class " + t + " not mapped to, but superclass mapping " + 
                              supemt + " is defined."); 
           System.out.println(">>> Suggest conditional mappings { Cond } " + 
                              supemt.realsrc + " --> " + t);
          } 
        } 
      }  
    } 
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
      { found.add(em.realsrc); } 
    }
    res.removeAll(found); 
    return res; 
  }   

  public static Vector unusedTargetEntities(Vector entities, Vector ems) 
  { Vector res = new Vector(); 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ex = (Entity) entities.get(i); 
      if (ex.isTarget())
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

  public Vector analyseCorrelationPatterns(Vector entities)
  { Vector res = new Vector(); 

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
 
    } 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      res.addAll(em.analyseCorrelationPatterns(entitymatches));  
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
 
        // if (em.isSpurious(entitymatches))
        // { System.out.println(">>> match " + em.realsrc + " |--> " + em.realtrg + 
        //                          " seems to be spurious, suggest deletion"); 
        // } 
        
        if (Entity.haveCommonSuperclass(tsources))
        { CorrelationPattern p = new CorrelationPattern("Entity merging (horizontal)", 
                                     "Entity " + em.realtrg + " is target of " + 
                                                     tsources.size() + " exclusive sources"); 
          p.addTargetEntity(t); 
          p.addSourceEntities(tsources); 

          if (res.contains(p)) { } else { res.add(p); }
          // add flag attribute if not already there:
          Entity rtrg = em.realtrg; 
          String flagname = rtrg.getName().toLowerCase() + "Flag";
          if (rtrg.hasDefinedAttribute(flagname)) { } 
          else   
          { Attribute tflag = new Attribute(flagname, 
                                          new Type("String", null),
                                          ModelElement.INTERNAL);
            tflag.setElementType(new Type("String",null)); 
 
            rtrg.addAttribute(tflag); 
            for (int z = 0; z < tsources.size(); z++) 
            { Entity tsrc = (Entity) tsources.get(z); 
              EntityMatching emz = ModelMatching.getEntityMatching(tsrc,t,entitymatches); 
              if (emz != null && emz.realsrc != null) 
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
        { CorrelationPattern p = new CorrelationPattern("Entity merging (vertical)", 
                                     "Entity " + em.realtrg + " is target of " + 
                                                     tsources.size() + " non-exclusive sources"); 
          p.addTargetEntity(t); 
          p.addSourceEntities(tsources); 

          if (res.contains(p)) { } else { res.add(p); }
        }   
      } // risk of conflict in data, for some feature merges
        // target should have id attribute, mapped to by id in each source
        // apart from this and *-mult features, the target features of each mapping 
        // must be disjoint. 
    } 

    return res; 
  }  

  public void removeSpuriousMatchings() 
  { for (int i = 0; i < entitymatches.size(); i++) 
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
                                 " seems to be spurious, suggest deletion"); 
        } 
      } 
    } 
  } 

  public int size() 
  { return mymap.size(); } 

  public double nameSimilarity()
  { double res = 0; 
    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      Entity s = em.src; 
      Entity t = em.trg; 
      double sim = ModelElement.similarity(s.getName().toLowerCase(), 
                                           t.getName().toLowerCase()); 
      res = res + sim; 
    } 
    return res; 
  }       

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

  public String qvtTransformation(Vector econvs, Vector seconvs, Vector esconvs, 
                                  Vector benums, Vector ebools)
  { String res = "transformation tau(source: MM1, target: MM2)\n" + 
                 "{ \n"; 

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

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      res = res + em.qvtrule1(entitymatches) + "\n"; 
    } // must include mapping to any primary key of em.realtarget

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.qvtrule2(entitymatches) + "\n"; }  
    } // omits mapping to primary keys of em.realtarget

    return res + "\n}\n"; 
  } 

  public String umlrsdsTransformation(Vector econvs, Vector esconvs, Vector seconvs, 
                                      Vector beconvs, Vector ebconvs)
  { String res = "  usecase app {\n"; 

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

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.umlrsdsrule1() + "\n"; }  
    } 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.umlrsdsrule2(entitymatches) + "\n"; }  
    } 

    return res + "\n  }\n"; 
  } 

  public String qvtoTransformation(Vector econvs, Vector esconvs, Vector seconvs, 
                                   Vector benums, Vector ebools, Vector thesaurus)
  { String res = "transformation tau(in : src, out : trg);\n\n"; 

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
      if (em.isConcrete())
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
      if (em.isConcrete())
      { res = res + em.qvtorule2(entitymatches) + "\n"; }  
    } 

    return res; 
  } 

  public String atlTransformation(Vector types)
  { String res = ""; 

   /* for (int i = 0; i < types.size(); i++) 
    { Type et = (Type) types.get(i); 
      if (et.isEnumeration())
      { res = res + "  " + et.enumQueryOpsATL() + "\n\n"; } 
    }     */ 

    for (int i = 0; i < entitymatches.size(); i++) 
    { EntityMatching em = (EntityMatching) entitymatches.get(i); 
      if (em.isConcrete())
      { res = res + em.atlrule(entitymatches) + "\n"; }  
    } 

    return res + "\n\n"; 
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

  public static EntityMatching findEntityMatchingByTarget(Entity t, Vector ems)
  { // some matching that can map elements to type t. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realtrg == t) 
      { return em; } 
    }  

    return null;      
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

  public static EntityMatching getEntityMatching(Entity s, Vector ems)
  { // the exact matching that maps elements of type s. 

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (em.realsrc == s) 
      { return em; } 
    }  

    return null;      
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

  public EntityMatching getEntityMatching(Entity s)
  { return getEntityMatching(s,entitymatches); } 

  public Vector getAttributeMapping(Entity ent)
  { EntityMatching em = findEntityMatching(ent, entitymatches);
    if (em != null) 
    { return em.attributeMappings; } 
    return null; 
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

  public static boolean compatibleType(Attribute satt, Attribute tatt, Vector ems)
  { // true if satt could be matched to tatt, given ems
    Type setype = satt.getElementType(); 
    Type tetype = tatt.getElementType(); 
    
    if (setype != null && tetype != null && 
        setype.isEntity() && tetype.isEntity())
    { Entity e1 = setype.getEntity(); 
      Entity e2 = tetype.getEntity(); 
        // and e1 maps to e2 or a subclass of it 
      Entity e1img = ModelMatching.lookupRealMatch(e1,ems); 
      if (e1img == e2) 
      { return true; } 
      if (Entity.isAncestor(e2,e1img))
      { return true; } 
    } 
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
      Entity e1img = ModelMatching.lookupRealMatch(e1,ems); 
      if (e1img == e2) 
      { return true; } 
      if (Entity.isAncestor(e2,e1img))
      { return true; } 
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
        unused.isDirect() && 
        "String".equals(targetType.getName()))
    { BasicExpression sep = new BasicExpression("\"~\"");
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
}




