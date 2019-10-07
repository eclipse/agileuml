import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 



public class EntityMatching implements SystemTypes
{ Entity src; 
  Entity trg; 
  Entity realsrc; 
  Entity realtrg; 

  String srcname = ""; 
  String trgname = ""; 
  Vector attributeMappings = new Vector(); // of AttributeMatching
  Expression condition = null; 

  ObjectTemplateExp sourceTemplate; 
  ObjectTemplateExp targetTemplate; 

  public EntityMatching(Entity asrc, Entity atrg)
  { src = asrc; 
    trg = atrg; 
    srcname = src.getName(); 
    trgname = trg.getName(); 
    realsrc = asrc; 
    realtrg = atrg; 
  } 

  public EntityMatching(Entity source, Entity target, Vector entities) 
  { src = source; 
    trg = target; 
    srcname = src.getName(); 
    trgname = trg.getName(); 

    String srcent = srcname; 
    if (srcname.endsWith("$"))
    { srcent = srcname.substring(0,srcname.length()-1); }
     
    String trgent = trgname; 
    if (trgname.endsWith("$"))
    { trgent = trgname.substring(0,trgname.length()-1); }
 
    realsrc = (Entity) ModelElement.lookupByName(srcent,entities); 
    realtrg = (Entity) ModelElement.lookupByName(trgent,entities); 

    if (realsrc == null) 
    { realsrc = src; } 
    if (realtrg == null) 
    { realtrg = trg; } 
  } // remove the trailing $ if present. 

  public void setCondition(Expression e)
  { condition = e; } 

  public void addCondition(Expression e)
  { condition = Expression.simplify("&",condition,e,null); } 

  public EntityMatching reverse()
  { EntityMatching inv = new EntityMatching(realtrg,realsrc); 
    return inv; 
  } 

  public void invert(Vector ems)
  { EntityMatching inv = ModelMatching.getRealEntityMatching(realtrg,realsrc,ems);
    if (inv == null) 
    { return; }  

    Vector invams = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i);
      if (am.isStringAssignment())
      { Vector res = am.inverts(realsrc,realtrg,ems); 
        if (res != null) 
        { invams.addAll(res); }
      }   
      else if (am.isExpressionAssignment())
      { Vector res = am.inverts(realsrc,realtrg,ems); 
        if (res != null) 
        { invams.addAll(res); }
      }   
      else if (am.isValueAssignment())
      { BasicExpression trgfeature = new BasicExpression(am.trg); 
        trgfeature.setUmlKind(Expression.ATTRIBUTE); 
        Expression e = new BinaryExpression("=",trgfeature,am.srcvalue); 
        e.setType(new Type("boolean",null)); 
        inv.addCondition(e); 
      } 
      else 
      { invams.add(am.invert()); }  
    } 
    inv.src = trg; 
    inv.trg = src; 
    inv.srcname = trg.getName(); 
    inv.trgname = src.getName(); 
    inv.attributeMappings.addAll(invams);
    return;  
  } 

  public void setAttributeMatches(Vector ams) 
  { attributeMappings.addAll(ams); } 

  public void setAttributeMatches(Vector srcatts, Vector trgatts) 
  { for (int i = 0; i < srcatts.size(); i++) 
    { Attribute att1 = (Attribute) srcatts.get(i); 
      Attribute att2 = (Attribute) trgatts.get(i); 
      AttributeMatching am = new AttributeMatching(att1,att2); 
      if (am.isDirect())
      { attributeMappings.add(am); }  
    } 
  } 

  public void addAttributeMatch(AttributeMatching am)
  { attributeMappings.add(am); } 

  public void addAttributeMapping(AttributeMatching am)
  { attributeMappings.add(am); } 

  public void addAttributeMappings(Vector ams) 
  { attributeMappings.addAll(ams); } 

  public Vector getAttributeMatchings()
  { return attributeMappings; } 

  public Vector enumConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res = VectorUtil.union(res,am.enumConversions()); 
    } 
    return res; 
  } 

  public Vector boolEnumConversions(Vector names)
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res.addAll(am.boolEnumConversions(names)); 
    } 
    return res; 
  } 

  public Vector enumBoolConversions(Vector names)
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res.addAll(am.enumBoolConversions(names)); 
    } 
    return res; 
  } 

  public Vector stringEnumConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res = VectorUtil.union(res,am.stringEnumConversions()); 
    } 
    return res; 
  } 

  public Vector enumStringConversions()
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res = VectorUtil.union(res,am.enumStringConversions()); 
    } 
    return res; 
  } 

  AttributeMatching findAttributeMatch(Attribute x) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.src.getName().equals(x.getName()))
      { return am; }  
    } 
    return null; 
  } // but there could be a match for x in a subclass

  public boolean isUnusedSource(Attribute x)
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.unusedInSource(x))
      { } 
      else 
      { return false; }  
    }
    return true; 
  } 

  public boolean isUnusedTarget(Attribute x)
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.trg.getName().equals(x.getName()))
      { return false; }  
    } 
    return true; 
  } 

  public Vector unusedSupplierBooleans(Attribute sref, Vector ems)
  { // boolean attributes of sref's supplier class, which are not in any mapping 

    Vector res = new Vector(); 
    Type stype = sref.getElementType(); 
    if (stype == null) 
    { return res; } 
    if (stype.isEntity())
    { Entity supplier = stype.getEntity(); 
      EntityMatching em = ModelMatching.getEntityMatching(supplier,ems); 
      if (em != null) 
      { Vector sbools = supplier.getLocalBooleanFeatures(); 
        for (int i = 0; i < sbools.size(); i++) 
        { Attribute att = (Attribute) sbools.get(i); 
          if (em.isUnusedSource(att) && isUnusedSource(att))
          { res.add(att); }
        }  
      } 
    } 
    return res; 
  } 

  public Vector unusedReverseReferences(Attribute sref, Vector ems)
  { // reference features of target of sref's supplier, unused, with reverse compatible with sref
    Vector res = new Vector(); 
    Type stype = sref.getElementType(); 
    if (stype == null) 
    { return res; } 
    if (stype.isEntity())
    { Entity supplier = stype.getEntity(); 
      EntityMatching em = ModelMatching.getEntityMatching(supplier,ems); 
      if (em != null) 
      { Vector srefs = em.realtrg.getLocalReferenceFeatures(); 
        for (int i = 0; i < srefs.size(); i++) 
        { Attribute att = (Attribute) srefs.get(i); 
          if (em.isUnusedTarget(att) && ModelMatching.compatibleReverse(sref,att,ems))
          { res.add(att); }
        }  
      } 
    } 
    return res; 
  } 


  public Vector unusedTargetReferences(Attribute sref, Vector ems) 
  { // reference * features of realtrg, compatible under ems to sref, and unused as targets
    Vector res = new Vector(); 
    Vector trefs = realtrg.getLocalReferenceFeatures(); 
    for (int i = 0; i < trefs.size(); i++) 
    { Attribute att = (Attribute) trefs.get(i); 
      if (att.isMultiValued() && ModelMatching.isUnusedTarget(att,ems))
      { if (ModelMatching.compatibleType(sref,att,ems))
        { res.add(att); } 
      } 
    } 
    return res; 
  } 

  public boolean isUnused(Attribute x, Vector ems)
  { // assume path size <= 2
    Vector path = x.getNavigation(); 
    if (path.size() <= 1) 
    { AttributeMatching am = findAttributeMatch(x); 
      if (am == null) 
      { return true; } 
    } 
    else if (path.size() > 1) 
    { Attribute first = x.first(); 
      Attribute last = x.last(); 
      if (first.getEntity() != null) 
      { EntityMatching fmatch = ModelMatching.getEntityMatching(first.getEntity(),ems);
        if (fmatch == null) 
        { return true; } // but may be a match in a subclass 
        else 
        { AttributeMatching am1 = fmatch.findAttributeMatch(first); 
          if (am1 == null) 
          { return true; } 
          else if (last.getEntity() != null)  
          { EntityMatching lmatch = ModelMatching.getEntityMatching(last.getEntity(),ems);
            if (lmatch == null) { return true; } 
            else  
            { AttributeMatching am2 = lmatch.findAttributeMatch(last); 
              if (am2 == null) 
              { return true; } 
            } 
          } 
        } 
      }  
    } 
    return false; 
  } // or unused if some parts are unused. 

  public Vector findCompatibleMappings(Attribute x, Vector ems) 
  { Vector res = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.isValueAssignment()) { } 
      else if (am.isExpressionAssignment()) { } 
      else if (am.isStringAssignment()) { } 
      else if (am.src.getName().equals(x.getName())) {}
      else if ((am.src.getType() + "").equals(x.getType() + "")) 
      { res.add(am); }  
      else if (am.src.getElementType() != null && 
               (am.src.getElementType() + "").equals(
                                        x.getElementType() + "")) 
      { res.add(am); }  
      else if (am.src.getElementType() != null && x.getElementType() != null && 
               am.src.getElementType().isEntity() && x.getElementType().isEntity())
      { Entity e1 = am.src.getElementType().getEntity(); 
        Entity e2 = x.getElementType().getEntity(); 
        // and they map to the same entity
        Entity e1img = ModelMatching.lookupRealMatch(e1,ems); 
        Entity e2img = ModelMatching.lookupRealMatch(e2,ems); 
        if (e1img == e2img) 
        { res.add(am); } 
      } 
      else if ((am.trg.getType() + "").equals(x.getType() + ""))
      { res.add(am); } 
    } 
    Entity sup = src.getSuperclass(); 
    if (sup != null) 
    { EntityMatching supmatch = ModelMatching.getMatching(sup,ems); 
      if (supmatch != null) 
      { res.addAll(supmatch.findCompatibleMappings(x,ems)); } 
    } 
    return res; 
  } 

  public AttributeMatching findClosestNamedMapping(Attribute x, Vector ams)
  { double bestmatch = 0; 
    double besttargetmatch = 0; 
    String xname = x.getName().toLowerCase(); 

    AttributeMatching best = null; 

    for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      String sname = am.src.getName().toLowerCase(); 
      String tname = am.trg.getName().toLowerCase(); 

      double d1 = ModelElement.similarity(sname,xname); 
      if (d1 > bestmatch)
      { bestmatch = d1; 
        besttargetmatch = ModelElement.similarity(tname,xname); 
        best = am; 
      } 
      else if (d1 > 0 && d1 == bestmatch)
      { double d2 = ModelElement.similarity(tname,xname); 
        if (d2 > besttargetmatch)
        { besttargetmatch = d2; 
          best = am; 
        } 
      } 
    } 
    return best; 
  } 

  public Vector findClosestNamed(Attribute x, Vector atts)
  { double bestmatch = 0; 
    double besttargetmatch = 0; 
    String xname = x.getName().toLowerCase(); 

    Vector best = new Vector(); 

    for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String tname = att.getName().toLowerCase(); 

      double d1 = ModelElement.similarity(tname,xname); 
      if (d1 > bestmatch)
      { bestmatch = d1;
        best.clear();  
        best.add(att); 
      } 
      else if (d1 == bestmatch)
      { best.add(att); } 
    } 
    return best; 
  } 

  public AttributeMatching findClosestNamedPair(Vector atts1, Vector atts2)
  { double bestmatch = 0; 
    Vector bestx = new Vector();
    Vector besty = new Vector();  

    for (int i = 0; i < atts1.size(); i++) 
    { Attribute x = (Attribute) atts1.get(i); 
      String xname = x.getName().toLowerCase(); 

      for (int j = 0; j < atts2.size(); j++) 
      { Attribute y = (Attribute) atts2.get(j); 
        String yname = y.getName().toLowerCase(); 

        double d1 = ModelElement.similarity(yname,xname); 
        if (d1 > bestmatch)
        { bestmatch = d1;
          bestx.clear();  
          bestx.add(x);
          besty.clear(); 
          besty.add(y);  
        } 
        else if (d1 == bestmatch)
        { bestx.add(x); 
          besty.add(y); 
        }
      }  
    } 
    if (bestx.size() == 0) 
    { return null; } 
    AttributeMatching am = new AttributeMatching((Attribute) bestx.get(0), 
                                                 (Attribute) besty.get(0)); 
    return am;  
  } 

  public AttributeMatching findSuperclassMapping(Attribute x, Vector ems) 
  { Entity sup = src.getSuperclass(); 
    if (sup != null) 
    { EntityMatching supmatch = ModelMatching.getMatching(sup,ems); 
      if (supmatch != null) 
      { Vector ams = supmatch.getAttributeMatchings();  
        // System.out.println(supmatch.realsrc + " >> " + supmatch.realtrg + " has mappings " + ams); 
        for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching am = (AttributeMatching) ams.get(i); 
          if (am.src.getName().equals(x.getName()))
          { return am; }  
        }
        return supmatch.findSuperclassMapping(x,ems);  
      } 
      return null;  
    } 
    return null; 
  } 

  public AttributeMatching findConsistentSuperclassMapping(Attribute x, Vector ems) 
  { Entity sup = src.getSuperclass(); 
    if (sup != null) 
    { EntityMatching supmatch = ModelMatching.getMatching(sup,ems); 
      if (supmatch != null && 
          (supmatch.realtrg == realtrg || Entity.isAncestor(supmatch.realtrg,realtrg))) 
      { Vector ams = supmatch.getAttributeMatchings();  
        // System.out.println(supmatch.realsrc + " >> " + supmatch.realtrg + " has mappings " + ams); 
        for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching am = (AttributeMatching) ams.get(i); 
          if (am.src.getName().equals(x.getName()))
          { return am; }  
        }
        return supmatch.findConsistentSuperclassMapping(x,ems);  
      } 
      return null;  
    } 
    return null; 
  } 

  public void removeConflictingMappings(AttributeMatching amx, Vector removed) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.trg.getName().equals(amx.trg.getName()))
      { removed.add(am); }  
    } 
  } 

  public void removeInvalidMatchings(Vector ems) 
  { // remove am where am.trg is some amx.trg in ems
    Vector removed = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      Attribute target = am.trg; 
      if (target.isComposed()) 
      { Attribute ftarget = target.getFinalFeature(); 
        if (ftarget.isManyValued()) { } 
        else 
        { Entity own = ftarget.getEntity(); 
          EntityMatching em = ModelMatching.findEntityMatchingByTarget(own,ems); 
          if (own != null && em != null) 
          { Vector ams = em.getAttributeMatchings(); 
            for (int j = 0; j < ams.size(); j++) 
            { AttributeMatching amx = (AttributeMatching) ams.get(j); 
              if (amx.trg.getName().equals(ftarget.getName()))
              { String yn = 
                  JOptionPane.showInputDialog("Remove " + realsrc + " mapping " + am + " that conflicts with " + 
                                              amx + " (y or n)?:");
 
                if (yn != null && yn.equals("y"))
                { removed.add(am); 
                  System.out.println(">>> removed mapping " + am + 
                                     " to ensure consistency");
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    attributeMappings.removeAll(removed); 
  } 

    
  boolean isConcrete()
  { return src.isConcrete(); } 

  boolean isConcreteTarget()
  { return trg.isConcrete(); } 

  public String toString()
  { String res = ""; 
    if (condition == null) 
    { res = realsrc.getName() + " |--> " + realtrg.getName() + "\n"; } 
    else 
    { res = "{ " + condition + " } " + 
            realsrc.getName() + " |--> " + realtrg.getName() + "\n"; 
    } 
     
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res = res + "    " + am + "\n"; 
    } 
    return res; 
  } 

  public Vector bidirectionalassociationMatchings()
  { // AttributeMatching am where am.src and am.trg are both bidirectional

    Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.isDirectTarget() && am.isDirectSource() && 
          am.isBidirectionalassociationMatching())
      { res.add(am); } 
    } 
    return res; 
  } 

  public void checkReverseReferenceConsistency(Vector bmatchings) 
  { // For each src -> trg in bmatchings, the matchings of src~ and to trg~ 
    // are consistent with src -> trg

    Vector removed = new Vector(); 
    Vector added = new Vector(); 

    for (int i = 0; i < bmatchings.size(); i++) 
    { AttributeMatching bm = (AttributeMatching) bmatchings.get(i); 
      Attribute bsrc = bm.src; 
      Attribute btrg = bm.trg; 
      String srcreverse = bsrc.getRole1(); 
      String trgreverse = btrg.getRole1(); 

      for (int j = 0; j < attributeMappings.size(); j++) 
      { AttributeMatching am = (AttributeMatching) attributeMappings.get(j); 
        if (am.src != null && am.src.getName().equals(srcreverse))
        { if (am.trg != null && am.trg.getName().equals(trgreverse)) { } 
          else 
          { System.out.println(">!! Inconsistency in " + realsrc + " |--> " + realtrg + 
                               ": " + bm.src + " maps to " + bm.trg + 
                               " but " + realsrc + "::" + srcreverse + " maps to " + am.trg); 
            String yn = JOptionPane.showInputDialog("Replace " + realsrc + "::" + 
                                                    srcreverse + " |--> " + am.trg + 
                                                    " by " + srcreverse + " |--> " + 
                                                    trgreverse + "? (y/n):"); 
            if (yn != null && "y".equals(yn))
            { removed.add(am); 
              Attribute revsrc = bsrc.getReverseReference(); 
              Attribute revtrg = btrg.getReverseReference();  
              AttributeMatching amnew = new AttributeMatching(revsrc,revtrg); 
              if (added.contains(amnew))
              { } 
              else 
              { added.add(amnew); }  
            }  
          } 
        } 
        else if (am.trg != null && am.trg.getName().equals(trgreverse))
        { System.out.println(">!! Inconsistency in " + 
                             realsrc + " |--> " + realtrg + ": " + 
                             bm.src + " maps to " + bm.trg + 
                             " but " + realsrc + "::" + am.src + " maps to " + trgreverse); 
          String yn = JOptionPane.showInputDialog("Replace " + realsrc + "::" + 
                                                   am.src + " |--> " + trgreverse + 
                                                   " by " + srcreverse + " |--> " + 
                                                   trgreverse + "? (y/n):"); 
          if (yn != null && "y".equals(yn))
          { removed.add(am); 
            Attribute revsrc = bsrc.getReverseReference(); 
            Attribute revtrg = btrg.getReverseReference();  
            AttributeMatching amnew = new AttributeMatching(revsrc,revtrg); 
            if (added.contains(amnew)) { } 
            else 
            { added.add(amnew); }  
          }   
        } 
      } 
    } 

    attributeMappings.removeAll(removed); 
    attributeMappings.addAll(added); 
  } 



  public void copyAttributeMappingsToSubclasses(Vector subs, Vector ems, 
                                                Vector thesaurus, Map mymap, Vector entities)
  { Vector added = new Vector(); 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      EntityMatching em = ModelMatching.getEntityMatching(sub,ems); 
      if (em != null) 
      { if (realtrg == em.realtrg || Entity.isAncestor(realtrg,em.realtrg)) 
        { System.out.println(">> Copying attribute matchings from " + 
              realsrc + " |--> " + realtrg + " down to " + em.realsrc + " |--> " + em.realtrg); 
          em.copyAttributeMappings(attributeMappings,thesaurus,mymap,entities); 
        }
        else 
        { System.err.println(">> Target " + em.realtrg + " of " + em.realsrc + 
                             " is not subclass of " + realtrg); 
          
          // option to create a new mapping. 
          if (realtrg.isConcrete())
          { String ans = 
              JOptionPane.showInputDialog("Create additional map " + sub + " |--> " + 
                                          realtrg + " (entity splitting vertical of " + sub + 
                                          ")?: (y/n) ");
            if (ans != null && "y".equals(ans))
            { EntityMatching newe = new EntityMatching(sub,realtrg);
              newe.addAttributeMappings(attributeMappings); 
              added.add(newe); 
            }
          }
          else 
          { System.out.println(">> Copying applicable attribute matchings from " + 
              realsrc + " |--> " + realtrg + " to " + em.realsrc + " |--> " + em.realtrg); 
            em.copyApplicableAttributeMappings(attributeMappings); 
          }   
        }  
      }
      else 
      { System.err.println(">> No mapping for subclass " + sub + " of " + realsrc); 
        if (realtrg.isConcrete())
        { System.err.println(">> Creating mapping to " + realtrg + 
                             " for subclass " + sub + " of " + realsrc); 
          EntityMatching newem = new EntityMatching(sub,realtrg); 
          newem.addAttributeMappings(attributeMappings); 
          added.add(newem); 
        }
      }  
    }  

    ems.addAll(added); 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      EntityMatching em = ModelMatching.getEntityMatching(sub,ems); 
      if (em != null && em.realsrc != null) 
      { Vector subsubs = em.realsrc.getSubclasses(); 
        if (subsubs.size() > 0) 
        { em.copyAttributeMappingsToSubclasses(subsubs,ems,thesaurus,mymap,entities); } 
      } 
    }     
  } // and recursively

  public void copyAttributeMappings(Vector ams, Vector thesaurus, Map mymap, Vector entities)
  { Vector removed = new Vector(); 
    Vector added = new Vector(); 

    // ams are the mappings from the superclass of src. 

    for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      Attribute amsrc = am.src; 
      Attribute amtrg = am.trg;

      boolean found = false; 
   
      for (int j = 0; j < attributeMappings.size(); j++) 
      { AttributeMatching amx = (AttributeMatching) attributeMappings.get(j); 
        if (amx.src != null && amx.src.getName().equals(amsrc.getName()))
        { found = true; 

          double simold = amx.similarity(mymap,entities,thesaurus); 
          double simnew = am.similarity(mymap,entities,thesaurus); 

          if (simnew >= simold)
          { removed.add(amx);  // remove a conflicting mapping, but only if new mapping is better

            if (added.contains(am)) { } 
            else if (amx.trg != null && amx.trg.getName().equals(amtrg.getName()))
            { } 
            else 
            { added.add(am);
              System.out.println(">--- Replaced " + amx + " by " + am + 
                               " for " + realsrc + " --> " + realtrg); 
            } 
          }  
        } 
      } 
      if (found) { } 
      else 
      { if (added.contains(am)) { } 
        else 
        { added.add(am);
          System.out.println(">--- added mapping " + am + 
                               " to " + realsrc + " -> " + realtrg); 
        }  
      } 
    } 

    attributeMappings.removeAll(removed); 
    attributeMappings.addAll(added); 
  }       


  public void copyApplicableAttributeMappings(Vector ams)
  { Vector removed = new Vector(); 
    Vector added = new Vector(); 

    // ams are the mappings from the superclass of src. 

    for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      Attribute amsrc = am.src; 
      Attribute amtrg = am.trg;
   
      if (realtrg.isDefinedDataFeature(am.trg)) 
      { System.out.println(">--- " + realtrg + " has feature " + am.trg);

        boolean found = false;   
        for (int j = 0; j < attributeMappings.size(); j++) 
        { AttributeMatching amx = (AttributeMatching) attributeMappings.get(j); 
          if (amx.trg != null && amx.trg.getName().equals(amtrg.getName()))
          { found = true; 

            String ans = 
              JOptionPane.showInputDialog("Replace old mapping " + amx + " by " + am + "?(y/n):");
            if (ans != null && "y".equals(ans)) 
            { 
              if (amx.trg.isMultipleValued()) { } 
              else 
              { removed.add(amx); } // remove a conflicting mapping

              if (added.contains(am)) { } 
              else 
              { added.add(am);
                System.out.println(">--- added mapping " + am + 
                                 " to " + realsrc + " -> " + realtrg); 
              }  // if realtrg has the feature am.trg
            } 
            break; 
          }
        } 

        if (found) { } 
        else // no conflicting feature
        { if (added.contains(am)) { } 
          else 
          { added.add(am); 
            System.out.println(">--- added mapping " + am + 
                                 " to " + realsrc + " -> " + realtrg); 
          }  
        } 
      } 
    } 

    attributeMappings.removeAll(removed); 
    attributeMappings.addAll(added); 
  }       


  public Vector allMappingsToTarget(AttributeMatching am1, Vector removed0) 
  { Vector res = new Vector(); 
    res.add(am1); 

    for (int j = 0; j < attributeMappings.size(); j++) 
    { AttributeMatching am2 = (AttributeMatching) attributeMappings.get(j);
      if (removed0.contains(am2)) // already processed
      { } 
      else if ((am1.trg + "").equals(am2.trg + ""))
      { if (!(am1.src + "").equals(am2.src + "")) // different mapping to same target
        { removed0.add(am2); 
          res.add(am2); 
        } 
      } 
    } 
    return res; 
  } 

  public boolean isSpurious(Vector ems)
  { // there is no feature mapping that needs this realsrc -> realtrg mapping

    for (int i = 0; i < ems.size(); i++) 
    { EntityMatching em = (EntityMatching) ems.get(i); 
      if (isNeededIn(em)) 
      { return false; } 
    } 
    return true; 
  } 

  public boolean isNeededIn(EntityMatching em) 
  { // some feature mapping requires this realsrc --> realtrg
    Vector ams = em.attributeMappings; 
    for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      if (isNeededIn(am))
      { return true; } 
    } 
    return false; 
  } 

  public boolean isNeededIn(AttributeMatching am) 
  { Type etsrc = am.src.getElementType(); 
    Type ettrg = am.trg.getElementType(); 
    if (etsrc != null && etsrc.isEntity() && 
        ettrg != null && ettrg.isEntity())
    { Entity esrc = etsrc.getEntity(); 
      Entity etrg = ettrg.getEntity(); 
      if ((esrc == realsrc || Entity.isAncestor(realsrc,esrc) || 
           Entity.isAncestor(esrc,realsrc)) && 
          (etrg == realtrg || Entity.isAncestor(etrg,realtrg)))
      { System.out.println(">>> Mapping " + realsrc + " |-> " + 
                           realtrg + " is needed in: " + am); 
        return true; 
      } 
    } 
    return false;
  } 


  public Vector analyseCorrelationPatterns(Vector ems)
  { Vector res = new Vector(); 

    if (srcname.equals(trgname)) 
    { CorrelationPattern p = new CorrelationPattern("Copy class", 
        "Class " + realsrc + " is copied into the target metamodel with the same name"); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      if (res.contains(p)) { } else { res.add(p); } 
    } 
    else 
    { CorrelationPattern p = new CorrelationPattern("Rename class", 
        "Class " + realsrc + " is renamed to " + realtrg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      if (res.contains(p)) { } else { res.add(p); }  
    } 

    Vector srcsubs = realsrc.getSubclasses();
    Vector trgsubs = realtrg.getSubclasses();

    Entity srcsup = realsrc.getSuperclass(); 
    Entity trgsup = realtrg.getSuperclass(); 

    // if (srcsup == null && srcsubs.size() > 0)
    // { copyAttributeMappingsToSubclasses(srcsubs,ems); } 

    if (srcsup == null && trgsup != null) 
    { CorrelationPattern p = new CorrelationPattern("Added superclass", 
        "New superclass " + trgsup + " of class " + realtrg + " is added wrt source " + realsrc); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      if (res.contains(p)) { } 
      else { res.add(p); }  
    } 
    else if (srcsup != null && trgsup == null) 
    { Entity tsup = ModelMatching.lookupRealMatch(srcsup,ems); 
      if (tsup == realtrg) 
      { } 
      else 
      { CorrelationPattern p = new CorrelationPattern("Removed superclass", 
          "Superclass " + srcsup + " of class " + realsrc + " is removed from " + realtrg); 
        p.addSourceEntity(realsrc); 
        p.addTargetEntity(realtrg); 
        if (res.contains(p)) { } else { res.add(p); }
      }   
    } 

    Vector tsplits = new Vector(); // entities into which src features are split
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.isValueAssignment()) { } 
      else if (am.isExpressionAssignment()) { } 
      else if (am.isStringAssignment()) { } 
      else if (am.src.getOwner() == realsrc && am.isDirectSource() && !am.isDirectTarget())
      { Attribute amt = am.trg; 
        Attribute ftrg = amt.getFinalFeature(); 
        Entity towner = ftrg.getOwner(); 
        if (tsplits.contains(towner)) { } 
        else 
        { tsplits.add(towner); } 
      }  
    } 

    if (tsplits.size() > 1) 
    { // boolean isHorizontal = Entity.haveCommonSuperclass(tsplits); 
      String splitkind = "Vertical"; 
      // String excl = "non-exclusive"; 
      // if (isHorizontal) 
      // { splitkind = "Horizontal";
      //   excl = "exclusive";
      // } 

      CorrelationPattern p = new CorrelationPattern(splitkind + " entity splitting", 
         "Direct features of " + realsrc + " are split into " + tsplits.size() +  
         " target classes " + tsplits); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntities(tsplits); 
      if (res.contains(p)) { } else { res.add(p); } 
    } // could be horizontal if the targets are all subclasses of one class.                             

    // Vector srcasts = realsrc.allDefinedAssociations(); 
    // Vector srcatts = realsrc.allDefinedAttributes(); 

    // are there any duplicated mappings src1 -> trg, src2 --> trg? 
    Vector added0 = new Vector(); 
    Vector removed0 = new Vector(); 

    for (int i = 0; i < attributeMappings.size();  i++) 
    { AttributeMatching am1 = (AttributeMatching) attributeMappings.get(i); 
      if (removed0.contains(am1))
      { continue; } 

      Vector am1siblings = allMappingsToTarget(am1,removed0); 
      if (am1siblings.size() > 1)  // multiple sources to same target
      { // replace all of them with a new combined mapping
  
        Vector auxvars = new Vector(); 
        Expression expr = 
              ModelMatching.defineCombinedExpression(am1siblings, am1.trg, auxvars); 
        if (expr != null) 
        { String message = "Suggest combination " + expr + " |--> " + am1.trg; 
          String optimise = 
            JOptionPane.showInputDialog("Replace " + realsrc + " map " + am1 + " by " + 
                                            expr + " |--> " + am1.trg +
                                            "?: (y/n) ");
          if (optimise != null && "y".equals(optimise))
          { Attribute var = null; 
            if (auxvars.size() > 0) 
            { var = (Attribute) auxvars.get(0); } 
            AttributeMatching newam = new AttributeMatching(expr, am1.trg, var, auxvars); 
            removed0.add(am1);  
            added0.add(newam);
            message = "Combined mappings to same target: " + expr + " |--> " + am1.trg; 
            
            CorrelationPattern q = 
                new CorrelationPattern("Combine source features: " + am1siblings 
                                        + " of " + realsrc.getName(), message); 
            q.addSourceEntity(realsrc); 
            q.addTargetFeature(am1.trg); 
            if (res.contains(q)) { } else { res.add(q); }
          }  
        }
      } 
    } 


   // Another possilibity is two+ unused source features, and an unused target, all of same type. 
  
    realsrc.defineLocalFeatures(); 
    Vector srcatts = realsrc.getLocalFeatures(); 
    realsrc.defineNonLocalFeatures(); 
    Vector srcasts = realsrc.getNonLocalFeatures(); 

    Vector added = new Vector(); 
    Vector removed = new Vector(); 

    // are there any unused attributes?      
    for (int i = 0; i < srcatts.size(); i++) 
    { Attribute amsrc = (Attribute) srcatts.get(i);
      if (isUnusedSource(amsrc) && (!realsrc.isShared() || amsrc.isSource()))
      { String message = "No other mapped feature has the same type"; 
        AttributeMatching amx = findConsistentSuperclassMapping(amsrc,ems); 
        if (amx != null) 
        { message = "Use superclass match " + amx; 
          added.add(amx); 
          removeConflictingMappings(amx,removed); 
        }
        else 
        { Vector amsc = findCompatibleMappings(amsrc,ems); 
          amx = findClosestNamedMapping(amsrc,amsc); 
          if (amx != null) 
          { Vector auxvars = new Vector(); 
            Expression expr = 
              ModelMatching.defineCombinedExpression(amsrc, amx.src, amx.trg, auxvars); 
            if (expr != null) 
            { message = "Suggest " + expr + " |--> " + amx.trg; 
              String optimise = 
                JOptionPane.showInputDialog("Replace " + realsrc + " map " + amx + " by " + 
                                            expr + " |--> " + amx.trg +
                                            "?: (y/n) ");
              if (optimise != null && "y".equals(optimise))
              { removed.add(amx); 
                Attribute var = null; 
                if (auxvars.size() > 0) 
                { var = (Attribute) auxvars.get(0); } 
                AttributeMatching newam = new AttributeMatching(expr, amx.trg, var, auxvars); 
                added.add(newam);
                message = "Combined feature mapping: " + expr + " |--> " + amx.trg; 
              }  
            }
            else 
            { message = "Suggest combining with " + amx; } 
          } 
        } 

        CorrelationPattern q = 
           new CorrelationPattern("Unused source feature: " + amsrc + " of " + 
                                  realsrc.getName(), message); 
        q.addSourceEntity(realsrc); 
        q.addSourceFeature(amsrc); 
        if (res.contains(q)) { } else { res.add(q); } 
      } 
    } 
     
    for (int i = 0; i < srcasts.size(); i++) 
    { Attribute amsrc = (Attribute) srcasts.get(i);
      if (isUnusedSource(amsrc) && (!realsrc.isShared() || amsrc.isSource()) && isUnused(amsrc,ems))
      { String message = "No other mapped feature has the same type"; 
        AttributeMatching amx = findConsistentSuperclassMapping(amsrc,ems); 
        if (amx != null) 
        { message = "Use superclass match " + amx;
          added.add(amx);
          System.out.println(">--- added superclass match " + amx + " to " + realsrc + " -> " + 
                             realtrg); 
          removeConflictingMappings(amx,removed); 
        }
        else 
        { Vector amxs = findCompatibleMappings(amsrc,ems); 
          amx = findClosestNamedMapping(amsrc,amxs); 
          if (amx != null) 
          { Vector auxvars = new Vector(); 
            Expression expr = 
              ModelMatching.defineCombinedExpression(amsrc, amx.src, amx.trg, auxvars); 
            if (expr != null) 
            { message = "Suggest " + expr + " --> " + amx.trg; 
              String optimise = 
                JOptionPane.showInputDialog("Replace " + realsrc + " map " + amx + 
                                            " by " + 
                                            expr + " |--> " + amx.trg +
                                            "?: (y/n) ");
              if (optimise != null && "y".equals(optimise))
              { removed.add(amx); 
                Attribute var = null; 
                if (auxvars.size() > 0) 
                { var = (Attribute) auxvars.get(0); } 
                AttributeMatching newam = new AttributeMatching(expr, amx.trg, var, auxvars); 
                added.add(newam);
              }
            }
            else 
            { message = "Suggest combining with " + amx; } 
          }  
        }
 
        CorrelationPattern q = 
           new CorrelationPattern("Unused source feature: " + amsrc + 
                                  " of " + realsrc.getName(), message); 
        q.addSourceEntity(realsrc); 
        q.addSourceFeature(amsrc); 
        if (res.contains(q)) { } else { res.add(q); } 
      } 
    } 

    // Also identify unused target features

    attributeMappings.removeAll(removed0); 
    attributeMappings.addAll(added0); 
    attributeMappings.removeAll(removed); 
    attributeMappings.addAll(added); 

    for (int i = 0; i < srcatts.size(); i++) 
    { Attribute amsrc = (Attribute) srcatts.get(i);
      if (amsrc.isMultiValued() && !isUnusedSource(amsrc))
      { if (!realsrc.isShared() || amsrc.isSource())
        { Vector trefs = unusedTargetReferences(amsrc,ems); 
          if (trefs.size() > 0) 
          { System.out.println(">>> Unused targets compatible with " + amsrc + " are: " + 
                               trefs); 
            Vector sbools = unusedSupplierBooleans(amsrc,ems); 
            if (sbools.size() > 0) 
            { System.out.println(">>> Unused supplier booleans " + sbools); 
              System.out.println("Suggest " + realsrc + " |--> " + realtrg + " mappings of form  " + 
                                 amsrc + "->select(" + sbools.get(0) + ")  |-> " + trefs.get(0));
              AttributeMatching bestpair = findClosestNamedPair(trefs,sbools); 
              Attribute fref = bestpair.src; 
              Attribute boolatt = bestpair.trg; 
              String yn = 
                JOptionPane.showInputDialog("Add mapping " + realsrc + "::" + amsrc + 
                                            "->select(" + boolatt + ")  |-> " + fref +
                                            "? (y/n):");

              if (yn != null && "y".equals(yn))
              { BasicExpression boolexp = new BasicExpression(boolatt); 
                BasicExpression srcexp = new BasicExpression(amsrc); 
                srcexp.setUmlKind(Expression.ATTRIBUTE); 

                BinaryExpression sel = new BinaryExpression("->select",srcexp,boolexp); 
                sel.setType(srcexp.getType()); 
                sel.setElementType(srcexp.getElementType()); 
                AttributeMatching amnew = new AttributeMatching(sel,fref); 
                String var = Identifier.nextIdentifier("var_"); 
                Attribute var$ = new Attribute(var,amsrc.getElementType(),ModelElement.INTERNAL); 
                var$.setElementType(amsrc.getElementType()); 
                amnew.elementVariable = var$; 
                amnew.addAuxVariable(amsrc); 
                attributeMappings.add(amnew); 
              } 
            }  
          } 
        }
      } 
    } 

    for (int i = 0; i < srcatts.size(); i++) 
    { Attribute amsrc = (Attribute) srcatts.get(i);
      if (isUnusedSource(amsrc))
      { if (!realsrc.isShared() || amsrc.isSource())
        { Vector trefs = unusedReverseReferences(amsrc,ems); 
          if (trefs.size() > 0) 
          { System.out.println(">>> Unused reverse associations compatible with " + amsrc + " are: " + 
                               trefs); 
            System.out.println("Suggest " + realsrc + " |--> " + realtrg + " mappings of form  " + 
                               amsrc + "~ |-->  " + trefs.get(0));
            String yn = 
                JOptionPane.showInputDialog("Add mapping " + realsrc + "::" + amsrc + 
                                            "~  |--> " + trefs.get(0) +
                                            "? (y/n):");

            if (yn != null && "y".equals(yn))
            { Entity supplier = amsrc.getElementType().getEntity(); 
              EntityMatching emx = ModelMatching.getEntityMatching(supplier,ems); 
            
              Attribute fref = (Attribute) trefs.get(0); 
              BasicExpression srcexp = new BasicExpression(amsrc);
              srcexp.setUmlKind(Expression.ATTRIBUTE); 

              Type entity1type = new Type(amsrc.getEntity()); 
              Expression rev = amsrc.makeInverseCallExpression(); 
              // Expression rev = new UnaryExpression("->inverse",srcexp);  
              rev.setType(amsrc.getReverseType()); 
              rev.setElementType(entity1type); 
              AttributeMatching amnew = new AttributeMatching(rev,fref); 
              String var = Identifier.nextIdentifier("var_"); 
              Attribute var$ = new Attribute(var,entity1type,ModelElement.INTERNAL); 
              var$.setElementType(entity1type); 
              amnew.elementVariable = var$; 
              amnew.addAuxVariable(amsrc); 
              emx.addAttributeMapping(amnew); 
            }  
            // should check it is valid, ie., rev is feature of supplier, fref of emx.realtrg
          } 
        }
      } 
    } 


    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.isValueAssignment()) { } 
      else if (am.isStringAssignment()) { } 
      else if (am.isExpressionAssignment()) { } 
      else if (am.isDirect())
      { Vector patts = am.analyseCorrelationPatterns(src,trg,
                                              realsrc,realtrg,this,ems); 
        res.addAll(patts);
      }  
    } // but avoid duplicates

    /* If all of srcsubs are mapped to m(realsrc) then it is 
       "Merge superclass and subclasses" */ 
    boolean allmerged = true; 
    for (int i = 0; i < srcsubs.size(); i++) 
    { Entity ssub = (Entity) srcsubs.get(i); 
      Entity tsub = (Entity) ModelMatching.lookupRealMatch(ssub,ems); 
      if (tsub == realtrg) { } 
      else 
      { allmerged = false; } 
    } 

    if (srcsubs.size() > 0 && allmerged == true) 
    { CorrelationPattern p = new CorrelationPattern("Merged superclass and subclasses", 
        "All subclasses of class " + realsrc + " are merged into " + realtrg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      /* Attribute tflag = new Attribute(realtrg.getName().toLowerCase() + "Flag", 
                                      new Type("String", null),
                                      ModelElement.INTERNAL); 
      realtrg.addAttribute(tflag); 
      for (int z = 0; z < srcsubs.size(); z++) 
      { Entity realsub = (Entity) srcsubs.get(z); 
        EntityMatching emz = ModelMatching.getEntityMatching(realsub,ems); 
        if (emz != null) 
        { BasicExpression srcvalue = new BasicExpression("\"" + realsub.getName() + "\""); 
          srcvalue.setType(new Type("String",null)); 
          srcvalue.setElementType(new Type("String",null)); 

          AttributeMatching am = new AttributeMatching(srcvalue,tflag); 
          emz.addAttributeMatch(am); 
        }
      }  */ 

      if (res.contains(p)) { } else { res.add(p); }  
    } // create a target entity flag attribute and new settings for this for each subclass mapping. 
    else if (trgsubs.size() < srcsubs.size())
    { CorrelationPattern p = new CorrelationPattern("Removed subclasses", 
        "Some subclasses of class " + realsrc + " are removed from " + realtrg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      if (res.contains(p)) { } else { res.add(p); }  
    } 
    else if (trgsubs.size() > srcsubs.size())
    { CorrelationPattern p = new CorrelationPattern("Added subclasses", 
        "Additional subclasses of class " + realtrg + " are added wrt " + realsrc); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      if (res.contains(p)) { } else { res.add(p); }  
    } 


    return res; 
  } 


  String qvtrule1(Vector ems) 
  { // if (src == trg) { return ""; } 

    String srcvar = srcname.toLowerCase() + "x"; 
    BasicExpression srcv = new BasicExpression(srcvar); 
    String srcent = srcname.substring(0,srcname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); 
      srcv.setType(new Type(realsrc)); 
      srcv.setElementType(new Type(realsrc)); 
    } 

    String trgvar = trgname.toLowerCase() + "x"; 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String overrides = ""; 
    String res = " top relation " + srcent + "2" + trgent; 
    if (src.isConcrete())
    { res = " " + res; }  
    else 
    { res = "  abstract" + res; }   

    Entity srcsup = realsrc.getSuperclass(); 
    if (srcsup != null) 
    { for (int i = 0; i < ems.size(); i++) 
      { EntityMatching em = (EntityMatching) ems.get(i); 
        if (em.realsrc == srcsup) 
        { String ssname = srcsup.getName(); 
          String stname = em.realtrg.getName(); 
          overrides = " overrides " + ssname + "2" + stname + " "; 
          break; 
        } 
      }
    }     
    res = res + overrides + "\n"; 
    res = res +  "  { checkonly domain source " + srcvar + " : " + srcent + " {};\n"; 
    res = res +  "    enforce domain target " + trgvar + " : " + trgent + " {};\n"; 
    if (condition != null) 
    { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
      res = res + "    when { " + cexp + " }\n"; 
    } 
    res = res +  "  }\n"; 
    return res; 
  } 

  String qvtrule2(Vector ems) 
  { // if (src == trg) { return ""; } 

    if (attributeMappings.size() == 0) 
    { return ""; } 

    String srcvar = srcname.toLowerCase() + "x"; 
    BasicExpression srcv = new BasicExpression(srcvar); 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgvar = trgname.toLowerCase() + "x"; 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); 
      srcv.setType(new Type(realsrc)); 
      srcv.setElementType(new Type(realsrc)); 
    } 

    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String srcbody = ""; 
    String trgbody = ""; 

    int scount = 0;
    int tcount = 0; 
 
    Vector whenclauses = new Vector(); 
    if (condition != null) 
    { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
      whenclauses.add(cexp + ""); 
    } 
    whenclauses.add(srcent + "2" + trgent + "(" + srcvar + "," + trgvar + ")"); 

    // Map objTemplates = new Map(); // String -> Vector(String)
    // Map tExps = new Map(); 

    Attribute srcroot = new Attribute(srcvar, new Type(realsrc), ModelElement.INTERNAL); 
    Attribute trgroot = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 

    ObjectTemplateExp sourceObjectTemplate = new ObjectTemplateExp(srcroot,realsrc); 
    ObjectTemplateExp targetObjectTemplate = new ObjectTemplateExp(trgroot,realtrg); 
    String auxdomains = ""; 

    Vector srcnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      srcnames.add(am.src.getName()); 
    } 

    // System.out.println(">>> Source names = " + srcnames); 
    // Collections.sort(srcnames); 
    // System.out.println(">>> Source names = " + srcnames); 
    
    Vector ams1 = (Vector) Ocl.sortedBy(attributeMappings, srcnames); 
    Map whens = new Map(); 

    for (int i = 0; i < ams1.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams1.get(i); 

      if (am.isStringAssignment())
      { Vector auxvars = am.getAuxVariables(); 
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector(); 
          avpath.add(av); 
          // String avdata = AttributeMatching.dataname(srcvar,avpath); 
          am.sourceequation(srcvar,avpath,sourceObjectTemplate);
        } 
      } 
      else if (am.isExpressionAssignment())
      { Expression expr = am.getExpression(); 
        Expression instantiatedexpr = expr.addReference(new BasicExpression(srcroot), 
                                                        new Type(realsrc));
        Expression inst = new BasicExpression(am.getElementVariable());  
        Expression test = new BinaryExpression("->includes", instantiatedexpr, inst); 
        sourceObjectTemplate.addWhere(test); 
        // and add a domain am.elementVariable : E1 { }; 
        auxdomains = auxdomains + 
          "  domain source " + inst + " : " + expr.getElementType() + " {};\n"; 

        /* Vector avpath = new Vector(); 
        if (am.trg.getNavigation().size() == 0)
        { avpath.add(am.trg); } 
        else 
        { avpath.addAll(am.trg.getNavigation()); }  
        String avdata = AttributeMatching.dataname(trgvar,avpath); */ 
        String whenc = am.whenClause(trgvar,ems,whens); 
        whenclauses.add(whenc);
      } 
      else if (am.isValueAssignment())
      { } 
      else if (am.isDirect())
      { String srceq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        
        EntityMatching emx = am.isObjectMatch(ems); 
        if (emx != null)
        { am.dependsOn = emx; 
          String whenc = am.whenClause(emx,srcvar,trgvar,whens); 
          whenclauses.add(whenc);
          srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
        } 
        else 
        { srceq = am.sourceequation(srcvar,sourceObjectTemplate); } 

        if (scount > 0) 
        { if (srceq.length() > 0)
          { srcbody = srcbody + ", " + srceq; 
            scount++; 
          }
        } 
        else if (srceq.length() > 0)
        { srcbody = srceq; 
          scount++; 
        } 
      } 
    } 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      trgnames.add(am.trg.getName()); 
    } 

    // System.out.println(">>> Source template = " + sourceObjectTemplate); 
    // Collections.sort(trgnames); 
    // System.out.println(">>> Source names = " + srcnames); 
    
    Vector ams2 = (Vector) Ocl.sortedBy(attributeMappings, trgnames); 

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 


      if (am.isStringAssignment())
      { Vector auxvars = am.getAuxVariables(); 
        Expression val = (Expression) am.srcvalue.clone(); 
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector(); 
          avpath.add(av); 
          String avdata = AttributeMatching.dataname(srcvar,avpath);
          val = val.substituteEq(av + "", new BasicExpression(avdata));
          // System.out.println("TARGET NAME " + am.trg + " " + av + " " + val); 
        } 
        targetObjectTemplate.addPTI(am.trg,val); 
      } 
      else if (am.isExpressionAssignment()) 
      { am.targetequation(trgvar,"",targetObjectTemplate); } 
      else if (am.isValueAssignment())
      { Expression sval = am.srcvalue.addReference(new BasicExpression(srcroot), 
                                                   new Type(realsrc));
        String trgeq = am.trg + " = " + sval;
        targetObjectTemplate.addPTI(am.trg,sval); 
      }   
      else if (am.isDirect())
      { String srceq = ""; 
        String trgeq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        trgeq = am.targetequation(trgvar,srcdata,targetObjectTemplate); 
                 
        if (tcount > 0)  
        { if (trgeq.length() > 0) 
          { trgbody = trgbody + ", " + trgeq; 
            tcount++;
          }  
        } 
        else if (trgeq.length() > 0) 
        { trgbody = trgeq;
          tcount++; 
        }
      }   
    } 

    // Attribute self = new Attribute("self",new Type(realsrc),ModelElement.INTERNAL); 
    // self.setType(new Type(realsrc)); 
    // self.setElementType(new Type(realsrc)); 
    // System.out.println("QVT-O Target code: " + targetObjectTemplate.toQVTO(self,whens)); 
    // System.out.println("UML-RSDS Target code: " +
    //     realsrc + ":: " +
    //       targetObjectTemplate.toUMLRSDSroot(realsrc.getName()) + " & " + 
    //       sourceObjectTemplate.toUMLRSDSantecedent() + " => \n" + 
    //       "    " + targetObjectTemplate.toUMLRSDS(whens)); 

  /*  String sobjs = ""; 
    Vector srcobjs = objTemplates.elements; 
    for (int h = 0; h < srcobjs.size(); h++) 
    { Maplet maplet = (Maplet) srcobjs.get(h); 
      String sobj = (String) maplet.source; 
      Vector sdefn = (Vector) maplet.dest;
      sobjs = sobjs + " " + sobj + " { "; 
      for (int g = 0; g < sdefn.size(); g++) 
      { String sdef = (String) sdefn.get(g); 
        sobjs = sobjs + sdef; 
        if (g < sdefn.size() - 1) 
        { sobjs = sobjs + ", "; } 
      } 
      sobjs = sobjs + " }"; 
      if (h < srcobjs.size() - 1) 
      { sobjs = sobjs + ", "; } 
    }  */ 

    /* String tobjs = ""; 
    Vector trgobjs = tExps.elements; 
    for (int h = 0; h < trgobjs.size(); h++) 
    { Maplet maplet = (Maplet) trgobjs.get(h); 
      String tobj = (String) maplet.source; 
      Vector tdefn = (Vector) maplet.dest;
      tobjs = tobjs + " " + tobj + " { "; 
      for (int g = 0; g < tdefn.size(); g++) 
      { String tdef = (String) tdefn.get(g); 
        tobjs = tobjs + tdef; 
        if (g < tdefn.size() - 1) 
        { tobjs = tobjs + ", "; } 
      } 
      tobjs = tobjs + " }"; 
      if (h < trgobjs.size() - 1) 
      { tobjs = tobjs + ", "; } 
    }  */ 

   /* if (scount > 0 && sobjs.length() > 0) 
    { srcbody = srcbody + ", " + sobjs; } */ 

    /* if (tcount > 0 && tobjs.length() > 0) 
    { trgbody = trgbody + ", " + tobjs; } */ 

    String res = "  top relation Map" + srcent + "2" + trgent + "\n"; 
    res = res +  "  { " + auxdomains + " checkonly domain source \n    " + sourceObjectTemplate + ";\n";
           // srcvar + " : " + srcent + " { " + srcbody + " };\n"; 
    res = res +  "    enforce domain target \n    " + targetObjectTemplate + ";\n"; 
          // trgvar + " : " + trgent + " { " + trgbody + " };\n"; 
    res = res +  "    when {\n    "; 
    for (int k = 0; k < whenclauses.size(); k++) 
    { String w = (String) whenclauses.get(k); 
      res = res + w; 
      if (k < whenclauses.size() - 1)
      { res = res + " and\n           "; } 
    } 
    res = res + " }\n"; 
    res = res +  "  }\n"; 

    sourceTemplate = sourceObjectTemplate; 
    targetTemplate = targetObjectTemplate; 

    return res; 
  } // and when for the objects that are referenced. 

  String umlrsdsrule1() 
  { if (src == trg) { return ""; } 

    String srcvar = srcname.toLowerCase() + "x"; 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgvar = trgname.toLowerCase() + "x"; 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String srcid = srcent.toLowerCase() + "Id"; 
    String trgid = trgent.toLowerCase() + "Id"; 

    String res = srcent + "::\n"; 
    if (condition != null) 
    { res = res + "  " + condition + " =>\n  "; } 
    res = res +  "  " + trgent + "->exists( " + trgvar + " | "; 
    res = res + trgvar + "." + trgid + " = " + srcid + " )\n"; 
    res = res +  "\n"; 
    return res; 
  } 

  String umlrsdsrule2(Vector ems) 
  { // if (src == trg) { return ""; } 

    String srcent = ""; 
    if (realsrc != null)
    { srcent = realsrc.getName(); }
    else 
    { srcent = srcname.substring(0,srcname.length()-1); } 

    String trgent = ""; 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 
    else 
    { trgent = trgname.substring(0,trgname.length()-1); } 

    String srcvar = srcname.toLowerCase() + "x"; 
    String trgvar = trgname.toLowerCase() + "x"; 


    String srcid = srcent.toLowerCase() + "Id"; 
    String trgid = trgent.toLowerCase() + "Id"; 

    String targetbody = ""; 
    String remainder = ""; 

    Vector created = new Vector(); 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      trgnames.add(am.trg.getName()); 
    } 
    Vector ams2 = (Vector) Ocl.sortedBy(attributeMappings, trgnames); 


    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 
      if (am.isStringAssignment())
      { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
        targetbody = targetbody + " & " + trgeq; 
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Type srcvaltype = am.srcvalue.getElementType();
        if (srcvaltype != null) 
        { String e1Id = srcvaltype.getName().toLowerCase() + "Id";  
          Expression srcids = 
            new BinaryExpression("->collect", am.srcvalue, new BasicExpression(e1Id)); 
          String trgeq = trgvar + "." + am.trg + " = " + am.trg.getElementType() + 
                                                   "[" + srcids + "]"; 
          targetbody = targetbody + " & " + trgeq;
        } 
        else 
        { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
          targetbody = targetbody + " & " + trgeq; 
        } 
        updateCreated(am.trg.getNavigation(),created); 
      } 
      else if (am.isValueAssignment())
      { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
        targetbody = targetbody + " & " + trgeq; 
      } 
      else if (am.isDirectTarget())
      { String srceq = ""; 
        String trgeq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        Vector bound = new Vector(); 
        bound.add(trgvar); 
        trgeq = am.targetequationUMLRSDS(trgvar,bound); 
        targetbody = targetbody + " & " + trgeq; 
        updateCreated(am.trg.getNavigation(),created); 
      }
    } 

    String trgvar1 = trgent.toLowerCase() + "_x"; 
      
    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 

      if (!am.isDirectTarget() && am.isExpressionAssignment())
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "[" + srcid + "] "; 
        remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(am.srcvalue,trgvar1,created) + "\n\n"; 
        updateCreated(am.trg.getNavigation(),created); 
      } 
      else if (!am.isDirectTarget() && am.isDirect()) // target is composite, source is not
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "[" + srcid + "] "; 
        remainder = remainder + "  " + am.composedTargetEquation(trgvar1,created) + "\n\n"; 
        updateCreated(am.trg.getNavigation(),created); 
      } 
    } // But amalgamate the equations where there is a common target path

    String res = srcent + "::\n"; 

    if (condition != null) 
    { res = res + "  " + condition + " =>\n  "; }
 
    if (targetbody.length() == 0) 
    { res = ""; } 
    else if (src == trg) 
    { res = res + "  " + trgvar + " = self" + targetbody + "\n"; } 
    else 
    { res = res +  "  " + trgent + "->exists( " + trgvar + " | "; 
      res = res + trgvar + "." + trgid + " = " + srcid + targetbody + " )\n";
    }  
    res = res +  "\n\n" + remainder; 
    return res; 
  } // use targetTemplate to rationalise the remainder. 

  public String qvtomain1()
  { if (src == trg) { return ""; } 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgent = trgname.substring(0,trgname.length()-1); 
    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    return "  in.objects[" + srcent + "]->map " + srcent + "2" + trgent + "();\n"; 
  } 

  public String qvtomain2()
  { if (attributeMappings.size() == 0) { return ""; }
 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    return "  in.objects[" + srcent + "]->map map" + srcent + "2" + trgent + "();\n"; 
  } 

  public String qvtorule1()
  { if (src == trg) { return ""; } 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String when = ""; 
    if (condition != null) 
    { Expression cexp = condition.addReference(new BasicExpression("self"),
                                               new Type(realsrc));
      when = " when { " + cexp + " } "; 
    } 

    return "mapping " + srcent + "::" + srcent + "2" + trgent + "() : " + trgent + "\n" + 
           "{" + when + "}\n"; 
  } 

  public String qvtodisjunctsrule(Vector ems)
  { // if (src == trg) { return ""; } 
    String srcent = srcname;
    if (srcname.endsWith("$"))
    { srcent = srcname.substring(0,srcname.length()-1); }
 
    String trgent = trgname; 
    if (trgname.endsWith("$"))
    { trgent = trgname.substring(0,trgname.length()-1); } 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String subs = ""; 
    Vector subclasses = src.getSubclasses();
    int subcount = 0; 
    

    if (subclasses.size() == 0) 
    { return ""; } // no need for it. 
 
    for (int i = 0; i < subclasses.size(); i++) 
    { Entity sub = (Entity) subclasses.get(i); 
      String subname = sub.getName(); 
      String srcsub = subname; 
      if (subname.endsWith("$"))
      { srcsub = subname.substring(0,subname.length()-1); } 
      
      Entity tsub = ModelMatching.lookupMatch(sub,ems); 
      if (tsub != null) 
      { String tsubname = tsub.getName(); 
        String trgsub = tsubname; 
        if (tsubname.endsWith("$"))
        { trgsub = tsubname.substring(0,tsubname.length()-1); } 
      
        String subopname = srcsub + "::" + srcsub + "2" + trgsub; 

        if (subcount > 0) 
        { subs = subs + ", " + subopname; } 
        else 
        { subs = subopname; } 
        subcount++; 
      } // else - find all the maximal subclasses of sub with a mapping. 
    } 

    if (subcount == 0)
    { return ""; } 

    String when = ""; 
    if (condition != null && realsrc != null) 
    { Expression cexp = condition.addReference(new BasicExpression("self"),
                                               new Type(realsrc));
      when = " when { " + cexp + " } "; 
    } 
 
    return "mapping " + srcent + "::" + srcent + "2" + trgent + "() : " + trgent + "\n" + 
           "disjuncts " + subs + "\n" + "{" + when + "}\n"; 
  } 

  public String qvtorule2(Vector ems)
  { // if (src == trg && src.hasNoSourceFeature()) { return ""; } 

    if (attributeMappings.size() == 0) { return ""; } // no assignments to be done 

    String srcvar = srcname.toLowerCase() + "x"; 
    String srcent = srcname; 
    if (srcname.endsWith("$"))
    { srcent = srcname.substring(0,srcname.length()-1); }  
    String trgvar = trgname.toLowerCase() + "x"; 
    String trgent = trgname; 
    if (trgname.endsWith("$"))
    { trgent = trgname.substring(0,trgname.length()-1); } 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 
    else 
    { System.err.println("!! ERROR: no real target entity in " + this); } 


    String lookup = "result := self.resolveoneIn(" + srcent + "::" + srcent + "2" + trgent + 
                                                 ", " + trgent + ");"; 
    if (src == trg) 
    { lookup = "result := self;"; } 


    Vector created = new Vector(); 
    // sort the attribute mappings in increasing name order/length

    Vector trgnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      trgnames.add(am.trg.getName()); 
    } 

    // System.out.println(">>> Source template = " + sourceObjectTemplate); 
    // Collections.sort(trgnames); 
    // System.out.println(">>> Source names = " + srcnames); 
    
    Vector ams2 = (Vector) Ocl.sortedBy(attributeMappings, trgnames); 

    String targetbody = ""; 
    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 
      if (am.isStringAssignment())
      { Vector auxvars = am.getAuxVariables(); 
        Expression val = (Expression) am.srcvalue.clone(); 
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          // Vector avpath = new Vector(); 
          // avpath.add(av); 
          String avdata = "self." + av.getName();
          val = val.substituteEq(av + "", new BasicExpression(avdata));
          // System.out.println("TARGET NAME " + am.trg + " " + av + " " + val); 
        } 
        targetbody = targetbody + "\n  " + 
                       "result." + am.trg + " := " + val; 
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression srcinst = am.srcvalue.addReference(new BasicExpression("self"),
                                                      new Type(realsrc));
        String trgeq = "result." + am.trg + " := (" + srcinst + ").resolve();"; 
        updateCreated(am.trg.getNavigation(), created); 
        targetbody = targetbody + "\n  " + trgeq; 
      }
      else if (am.isValueAssignment())
      { Expression sval = am.srcvalue.addReference(new BasicExpression("self"), 
                                                   new Type(realsrc));
        String trgeq = "result." + am.trg + " := " + sval + ";"; 
        targetbody = targetbody + "\n  " + trgeq; 
      }
      else if (am.isDirect())
      { String srceq = ""; 
        String trgeq = ""; 
        // String srcdata = am.sourcedataname(srcvar); 
        BasicExpression tvar = new BasicExpression("result"); 
        tvar.setType(new Type(realtrg)); 
        tvar.setElementType(new Type(realtrg)); 
        Expression svar; 
        if (am.isExpressionAssignment())
        { svar = am.srcvalue.addReference(new BasicExpression("self"),
                                                      new Type(realsrc));
          svar.setType(am.srcvalue.getType()); 
          svar.setElementType(am.srcvalue.getElementType()); 
          svar.setBrackets(true); 
        } 
        else 
        { BasicExpression selfvar = new BasicExpression("self"); 
          selfvar.setType(new Type(realsrc)); 
          selfvar.setElementType(new Type(realsrc));
          svar = new BasicExpression(am.src); 
          ((BasicExpression) svar).setObjectRef(selfvar);  
        } 

        trgeq = am.targetequationQVTO(tvar,svar,ems,created); 
        updateCreated(am.trg.getNavigation(), created); 
        // System.out.println(">>> CREATED: " + created); 
        targetbody = targetbody + "\n  " + trgeq; 
      }
    } 

    // if targetbody.length() == 0 return ""
    String when = ""; 
    if (condition != null) 
    { Expression cexp = condition.addReference(new BasicExpression("self"),
                                               new Type(realsrc));

      when = "  when { " + cexp + " }\n"; 
    } 

    return "mapping " + srcent + "::map" + srcent + "2" + trgent + "() : " + trgent + "\n" + 
           "{ init { " + lookup + " }\n" + when + 
           "  " + targetbody + "\n}\n"; 
  } 

  String atlrule(Vector ems) 
  { // if (src == trg) { return ""; } 

    Vector allnewrules = new Vector(); 
    Vector allnewclauses = new Vector(); 
    Vector allnewdo = new Vector(); 

    String srcent = ""; 
    if (realsrc != null)
    { srcent = realsrc.getName(); }
    else 
    { srcent = srcname.substring(0,srcname.length()-1); } 

    String trgent = ""; 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 
    else 
    { trgent = trgname.substring(0,trgname.length()-1); } 

    String srcvar = srcent.toLowerCase() + "_x"; 
    String trgvar = trgent.toLowerCase() + "_x"; 

    if (srcvar.equals(trgvar)) 
    { trgvar = trgvar + "_x"; } 

    Vector targetbody = new Vector(); 
    String remainder = ""; 

    Vector created = new Vector(); 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      trgnames.add(am.trg.getName()); 
    } 
    Vector ams2 = (Vector) Ocl.sortedBy(attributeMappings, trgnames); 


    java.util.Map implementedBy = new java.util.HashMap(); 
    // for each path in created, either an ATL OutPatternElement or an Attribute

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 
      String trgeq = "  "; 

      if (am.isStringAssignment())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        trgeq = trgeq + am.trg + " <- " + newval; 
        targetbody.add(trgeq); 
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        
        trgeq = trgeq + am.trg + " <- " + newval; 
        targetbody.add(trgeq);
        Vector cpath = am.trg.getNavigation();
        if (cpath.size() == 0) 
        { cpath.add(am.trg); }    
        updateCreated(cpath,created);
        Attribute cpathatt = new Attribute(cpath);  
        implementedBy.put(cpathatt.getName(), am.trg); 
      } 
      else if (am.isValueAssignment() && am.isDirectTarget())
      { trgeq = trgeq + am.trg + " <- " + am.srcvalue; 
        targetbody.add(trgeq); 
      } 
      else if (am.isDirectTarget())
      { trgeq = trgeq + am.atldirecttarget(srcvar);  
        targetbody.add(trgeq); 
        Vector cpath = am.trg.getNavigation();   
        if (cpath.size() == 0) 
        { cpath.add(am.trg); }    
        updateCreated(cpath,created); 
        Attribute cpathatt = new Attribute(cpath);  
        implementedBy.put(cpathatt.getName(), am.trg); 
      }
      else // composed target 
      { Vector newclauses = new Vector(); // of OutPatternElement
        Vector newrules = new Vector();   // of MatchedRule
        Vector newdo = new Vector();      // of String
        Binding cbind = 
                  am.atlcomposedtarget(newclauses,newrules,newdo,srcvar,trgvar,
                                       realsrc,created,implementedBy); 
        if (cbind != null) 
        { trgeq = trgeq + cbind; 
          targetbody.add(trgeq); 
        } 
        allnewrules.addAll(newrules); 
        allnewclauses.addAll(newclauses);
        allnewdo.addAll(newdo);  
        updateCreated(am.trg.getNavigation(),created); 
      }

      // else case of composed target
    } 

    String res = "  rule " + srcent + "2" + trgent + "\n"; 
    res = res +  "  { from " + srcvar + " : MM1!" + srcent; 

    if (condition != null) 
    { res = res + " ( " + condition + " )\n"; }
    else 
    { res = res + "\n"; }

    res = res + "    to " + trgvar + " : MM2!" + trgent + "\n";
 
    if (targetbody.size() == 0) 
    { } 
    else 
    { res = res + "    ( "; 
      for (int y = 0; y < targetbody.size()-1; y++) 
      { res = res + targetbody.get(y) + ",\n      "; }
      res = res + targetbody.get(targetbody.size()-1) + " )"; 
    }   

    for (int i = 0; i < allnewclauses.size(); i++) 
    { OutPatternElement newclause = (OutPatternElement) allnewclauses.get(i); 
      res = res + ",\n    " + newclause; 
    } 

    if (allnewdo.size() > 0) 
    { res = res + "\n  " + 
            "  do {\n    "; 
      for (int d = 0; d < allnewdo.size(); d++) 
      { String ds = (String) allnewdo.get(d); 
        res = res + ds + "\n      "; 
      } 
      res = res + "    }"; 
    } 

    res = res +  "\n  }\n\n"; 
  
    for (int j = 0; j < allnewrules.size(); j++) 
    { MatchedRule newrule = (MatchedRule) allnewrules.get(j); 
      res = res + newrule + "\n\n"; 
    } 

    return res; 
  } // use targetTemplate to rationalise the remainder. 

  private void updateCreated(Vector path, Vector created)
  { // add all initial segments of path to created if they are not in there already 
    Vector partialpath = new Vector(); 
    for (int i = 0; i < path.size(); i++) 
    { partialpath.add(path.get(i)); 
      if (created.contains(partialpath)) { } 
      else
      { Vector newpath = new Vector(); 
        newpath.addAll(partialpath); 
        created.add(newpath); 
      } 
    } 
  } 

}

