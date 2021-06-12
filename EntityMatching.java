import java.util.Vector; 
import java.io.*; 
import java.util.Collections; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
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
  Expression postcondition = null; 
  Vector replicationConditions = new Vector(); // of Expression

  ObjectTemplateExp sourceTemplate; 
  ObjectTemplateExp targetTemplate; 

  boolean isSecondary = false;  // for ATL, another entity mapping is the primary mapping
  String isSecondaryTo = "";    // The name of the primary mapping

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
    if (src == null) 
    { System.err.println("!!! Null source in entity match to " + target); 
      return; 
    }
    if (trg == null) 
    { System.err.println("!!! Null target in entity match from " + source); 
      return; 
    }
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

  public Object clone()
  { EntityMatching res = new EntityMatching(realsrc, realtrg); 
    res.setCondition(condition); 
    res.addAttributeMappings(attributeMappings); 
    res.postcondition = postcondition; 
    return res; 
  } 

  public void addAttributeMatching(AttributeMatching am)
  { if (attributeMappings.contains(am)) { } 
    else 
    { attributeMappings.add(am); } 
  } 
  
  public void addReplicationCondition(Expression r)
  { if (replicationConditions.contains(r)) { } 
    else 
    { replicationConditions.add(r); } 
  } 

  public boolean hasEmptyCondition()
  { if (condition == null) 
    { return true; }
	if ("true".equals(condition + ""))
	{ return true; }
	return false; 
  }

  public boolean hasSameCondition(EntityMatching em)
  { if (hasEmptyCondition() && em.hasEmptyCondition()) 
    { return true; }
	else if (("" + condition).equals(em.condition + ""))
	{ return true; }
	return false; 
  }

  public boolean hasReplicationCondition()
  { if (replicationConditions.size() > 0) 
    { return true; }
	return false; 
  }

  public void setCondition(Expression e)
  { condition = e; } 

  public void setPostcondition(Expression e)
  { postcondition = e; } 

  public void addCondition(Expression e)
  { condition = Expression.simplify("&",condition,e,null); } 

  public static void addCondition(Expression e, Vector ematches)
  { for (int i = 0; i < ematches.size(); i++) 
    { EntityMatching em = (EntityMatching) ematches.get(i); 
	  em.addCondition(e); 
	} 
  } 

  public static void addToConditionIfNull(Expression e, Vector ematches)
  { for (int i = 0; i < ematches.size(); i++) 
    { EntityMatching em = (EntityMatching) ematches.get(i); 
	  if (em.condition == null) 
	  { em.addCondition(e); }  
	} 
  } 

  public void mergeWith(EntityMatching emx)
  { attributeMappings = VectorUtil.union(attributeMappings, emx.attributeMappings); 
    condition = Expression.simplifyAnd(condition,emx.condition); 
  } 

  public Expression getCondition()
  { return condition; } 

  public boolean isPrimary()
  { return !isSecondary; } 

  public boolean isSecondary()
  { return isSecondary; } 

  public boolean unconditional()
  { if (condition == null) 
    { return true; }
    if ("true".equals(condition + ""))
    { return true; } 
    return false; 
  } 

  public boolean hasAssignmentOf(String value) 
  { // Some e --> t has e as "value"
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i);
      if ((am.src + "").equals("\"" + value + "\""))
      { return true; } 
      if ((am.srcvalue + "").equals("\"" + value + "\""))
      { return true; } 
    } 
    return false; 
  }     

  public boolean inheritanceRelatedTo(EntityMatching otherem)
  { // realsrc is ancestor, equal to or descendent of otherem.realsrc

    if (realsrc == otherem.realsrc) { return true; }  
    else if (Entity.isAncestor(realsrc, otherem.realsrc)) { return true; } 
    else if (Entity.isAncestor(otherem.realsrc, realsrc)) { return true; }
    return false; 
  }

  public EntityMatching reverse()
  { EntityMatching inv = new EntityMatching(realtrg,realsrc); 
    return inv; 
  } 

  public void invert(Vector ems)
  { EntityMatching inv = ModelMatching.getRealEntityMatching(realtrg,realsrc,ems);
    if (inv == null) 
    { return; }  

    Vector invams = new Vector(); 
    if (condition != null)
    { Vector amconds = AttributeMatching.invertCondition(condition,realsrc,realtrg); 
      invams.addAll(amconds); 
    } 

    Vector seen = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i);
      if (am.isPossibleFeatureMerge() && !(seen.contains(am)))
      { Vector otherams = am.findMergeFamily(attributeMappings,seen); 
        if (otherams.size() > 1)
        { AttributeMatching aminv = inverseMerge(otherams); 
          invams.add(aminv); 
          seen.addAll(otherams); 
        } 
      } 

      if (am.isStringAssignment() && !(seen.contains(am)))
      { Vector res = am.inverts(realsrc,realtrg,ems); 
        if (res != null) 
        { invams.addAll(res); }
        seen.add(am); 
      }   
      else if (am.isExpressionAssignment() && !(seen.contains(am)))
      { Vector res = am.inverts(realsrc,realtrg,ems); 
        if (res != null) 
        { invams.addAll(res); }
        seen.add(am); 
      }   
      else if (am.isValueAssignment() && !(seen.contains(am)))
      { BasicExpression trgfeature = new BasicExpression(am.trg); 
        trgfeature.setUmlKind(Expression.ATTRIBUTE); 
        Expression e = new BinaryExpression("=",trgfeature,am.srcvalue); 
        e.setType(new Type("boolean",null)); 
        inv.addCondition(e); 
        seen.add(am); 
      } 
      else if (!seen.contains(am)) 
      { invams.add(am.invert()); 
        seen.add(am); 
      }  
    } 
    inv.src = trg; 
    inv.trg = src; 
    inv.srcname = trg.getName(); 
    inv.trgname = src.getName(); 
    inv.attributeMappings.addAll(invams);
    return;  
  } 

  public AttributeMatching inverseMerge(Vector ams)
  { // x->before(s) --> t1 and x->after(s) --> t2 invert to t1 + s + t2 --> x

    AttributeMatching am1 = (AttributeMatching) ams.get(0); 
    AttributeMatching am2 = (AttributeMatching) ams.get(1);

    Expression t1 = new BasicExpression(am1.trg); 
    t1.setUmlKind(Expression.ATTRIBUTE); 
    Expression t2 = new BasicExpression(am2.trg);
    t2.setUmlKind(Expression.ATTRIBUTE); 
    Expression sep = ((BinaryExpression) am1.srcvalue).getRight(); 
    BinaryExpression news1 = new BinaryExpression("+", t1, sep); 
    BinaryExpression news = new BinaryExpression("+", news1, t2); 
    news.setType(new Type("String",null)); 
    news.setElementType(new Type("String", null)); 
    news.setMultiplicity(ModelElement.ONE);
 
    Expression x = ((BinaryExpression) am1.srcvalue).getLeft();
    AttributeMatching res = new AttributeMatching(news,x); 
    res.setElementVariable(am1.trg);
    res.addAuxVariable(am1.trg); 
    res.addAuxVariable(am2.trg); 
    return res; 
  } // must be a String matching
 
 
        

  public void setAttributeMatches(Vector ams) 
  { for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      addMapping(am); 
    }
  }  

  public void setAttributeMatches(Vector srcatts, Vector trgatts) 
  { for (int i = 0; i < srcatts.size(); i++) 
    { Attribute att1 = (Attribute) srcatts.get(i); 
      Attribute att2 = (Attribute) trgatts.get(i); 
      AttributeMatching am = new AttributeMatching(att1,att2); 
      if (am.isDirect())
      { attributeMappings.add(am); }  
    } 
  } 

  public void addMapping(AttributeMatching am) 
  { if (attributeMappings.contains(am)) { } 
    else 
    { attributeMappings.add(am); } 
  } 

  public void replaceAttributeMatching(AttributeMatching am) 
  { // remove any to the same target

    Vector removed = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching amx = (AttributeMatching) attributeMappings.get(i); 
      if ((amx.trg + "").equals(am.trg + ""))
      { removed.add(amx); } 
    } 
    attributeMappings.removeAll(removed); 
    attributeMappings.add(am); 
  } 

  public void addAttributeMatch(AttributeMatching am)
  { addMapping(am); } 

  public void addAttributeMapping(AttributeMatching am)
  { addMapping(am); } 

  public void addAttributeMappings(Vector ams) 
  { for (int i = 0; i < ams.size(); i++) 
    { AttributeMatching am = (AttributeMatching) ams.get(i); 
      addMapping(am); 
    }
  }  

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
      res = VectorUtil.union(res,am.boolEnumConversions(names)); 
    } 
    return res; 
  } 

  public Vector enumBoolConversions(Vector names)
  { Vector res = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      res = VectorUtil.union(res,am.enumBoolConversions(names)); 
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

  AttributeMatching findAttributeMappingByTarget(String tname) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.trg.getName().equals(tname))
      { return am; }  
    } 
    return null; 
  } // but there could be a match for x in a subclass

  AttributeMatching findAttributeMappingByFirstTarget(String tname) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.trg != null && am.trg instanceof Attribute) 
      { Attribute tatt = (Attribute) am.trg; 
        Vector path = tatt.getNavigation(); 
        if (path != null && path.size() <= 1)
        { if ((tatt + "").equals(tname))
          { return am; }  
        }  
        else if (path != null) 
        { Attribute ftrg = (Attribute) path.get(0); 
          if ((ftrg + "").equals(tname))
          { return am; }
        }  
      }   
    } 
    return null; 
  } // but there could be a match for x in a subclass

  public boolean usedInSource(Attribute x) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.usedInSource(x))
      { return true; }   
    }

    if (condition != null && usedInCondition(x))
    { return true; } 
    return false;  
  } 

  public boolean usedInCondition(Attribute x)
  { Vector xs = new Vector(); 
    xs.add(x + ""); 
    Vector varsusedin = condition.variablesUsedIn(xs);
    if (varsusedin.size() > 0)
    { return true; } 
    return false; 
  } 
 

  public boolean usedAsIntermediateClass(Entity e) 
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.usedAsIntermediateClass(e))
      { return true; } 
    }
    return false; 
  } 

  public boolean isUnusedSource(Attribute x)
  { if (condition != null && usedInCondition(x))
    { return false; } 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.unusedInSource(x))
      { } 
      else 
      { return false; }  
    }
    return true; 
  } 

  public boolean isUnusedTargetByName(Attribute x)
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.trg.getName().equals(x.getName()))
      { return false; }  

      Vector path = am.trg.getNavigation(); 
      String initialname = ""; 
      for (int j = 0; j < path.size(); j++) 
      { Attribute tp = (Attribute) path.get(j); 
        initialname = initialname + tp.getName(); 
        if (initialname.equals(x.getName()))
        { return false; } 
        if (j < path.size() - 1) 
        { initialname = initialname + "."; } 
      } 
    } 
    return true; 
  } 

  public boolean isUnusedTarget(Attribute x)
  { for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (x == am.trg || x.equalByNameAndOwner(am.trg) || x.equalToReverseDirection(am.trg))
      { return false; }  

      Vector path = am.trg.getNavigation(); 
      String initialname = ""; 
      for (int j = 0; j < path.size(); j++) 
      { Attribute tp = (Attribute) path.get(j); 
        if (x == tp || x.equalByNameAndOwner(tp))
        { return false; } 
      } 
    } 
    return true; 
  } 

  public Vector bestTargetMatches(Attribute satt, Vector ems, Vector thesaurus)
  { // Find closest NMS matches for satt in attributes of trg

    Vector locals1 = trg.getAttributes();
    Vector best = new Vector(); 
	
    for (int i = 0; i < locals1.size(); i++) 
    { Attribute tatt = (Attribute) locals1.get(i); 
      double bestmatch = 0;
      Attribute smatched = null; 
      if (ModelMatching.compatibleType(satt,tatt,ems) || ModelMatching.compatibleBaseTypes(satt,tatt,ems) ||
          ModelMatching.compatibleElementType(satt,tatt,ems))
      { double tknss = 
            Entity.nmsSimilarity(tatt.getName(), satt.getName(), thesaurus); 
        System.out.println(">>> NMS similarity of " + tatt + " and " + satt + " = " + tknss); 
        if (tknss > bestmatch) 
        { bestmatch = tknss; 
          smatched = tatt; 
          best = new Vector(); 
          best.add(smatched);  
        } 
        else if (tknss == bestmatch)
        { best.add(tatt); }
	 }
    }
    return best; 
  } 
   
  public void checkTargetFeatureCompleteness(Vector ems, Vector thesaurus)
  { Vector locals1 = trg.getAttributes();
    Vector locals3 = src.getAttributes();
    Vector added = new Vector(); 

    for (int i = 0; i < locals1.size(); i++) 
    { Attribute tatt = (Attribute) locals1.get(i); 
      if (ModelMatching.isUnusedTarget(tatt,ems))
      { System.err.println("!! Unused target feature " + realtrg + "::" + tatt); 
        double bestmatch = 0;
        Attribute smatched = null; 
 
        for (int j = 0; j < locals3.size(); j++) 
        { Attribute satt = (Attribute) locals3.get(j); 
          double tknss = 
            Entity.nmsSimilarity(tatt.getName(), satt.getName(), thesaurus); 
          System.out.println(">>> NMS similarity of " + tatt + " and " + satt + 
                                       " = " + tknss); 
          if (tknss > bestmatch) 
          { bestmatch = tknss; 
            smatched = satt; 
          } 
        }

        Attribute tmatched = null; 
        AttributeMatching amt = null; 

        for (int j = 0; j < attributeMappings.size(); j++) 
        { AttributeMatching am = (AttributeMatching) attributeMappings.get(j);
          Attribute targatt = am.trg;  
          double tknss = 
            Entity.nmsSimilarity(tatt.getName(), targatt.getName(), thesaurus); 
          System.out.println(">>> NMS similarity of " + tatt + " and " + targatt + 
                                       " = " + tknss); 
          if (tknss > bestmatch) 
          { bestmatch = tknss; 
            tmatched = targatt; 
            amt = am; 
          } 
        }

        if (tmatched != null) // feature splitting
        { System.out.println(">>> best match is target " + tmatched); 
          if (ModelMatching.compatibleType(amt.src,tatt,ems))
          { AttributeMatching newam = ModelMatching.defineFeatureSplitting(amt,tatt); 
            if (newam != null) 
            { added.add(newam); 
              System.out.println(">>> Adding " + newam);
            } 
            else 
            { added.add(new AttributeMatching(amt.src,tatt)); 
              System.out.println(">>> Adding " + amt.src + " |--> " + tatt);
            }
          }  
        } 
        else if (smatched != null) 
        { System.out.println(">>> best match is source " + smatched);
          if (ModelMatching.compatibleType(smatched,tatt,ems))
          { System.out.println(">>> Adding " + smatched + " |--> " + tatt);
            added.add(new AttributeMatching(smatched,tatt));
          }  
        } 
        else 
        { System.out.println(">>> no match for " + tatt); }       
      } 
    } 
    addAttributeMappings(added); 
  } 

  public Vector unusedSupplierBooleans(Attribute sref, Vector ems)
  { // boolean attributes of sref's supplier class, which are not in any mapping 

    Vector res = new Vector(); 
    Type stype = sref.getElementType(); 
    if (stype == null) 
    { return res; } 
    if (stype.isEntity())
    { Entity supplier = stype.getEntity(); 
      Vector entmatches = ModelMatching.getEntityMatchings(supplier,ems); 
      for (int k = 0; k < entmatches.size(); k++) 
      { EntityMatching em = (EntityMatching) entmatches.get(k); 
        Vector sbools = supplier.getLocalBooleanFeatures(); 
        for (int i = 0; i < sbools.size(); i++) 
        { Attribute att = (Attribute) sbools.get(i); 
          if (em.isUnusedSource(att) && isUnusedSource(att))
          { if (res.contains(att)) { } 
            else 
            { res.add(att); }
          } 
        }  
      } 
    } 
    return res; 
  } 

  public Vector unusedSourceBooleans()
  { Vector sbools = realsrc.getLocalBooleanFeatures(); 
    System.out.println(">>> Boolean source features: " + sbools); 
    Vector res = new Vector();

    for (int i = 0; i < sbools.size(); i++) 
    { Attribute sbool = (Attribute) sbools.get(i); 
      if (isUnusedSource(sbool))
      { if (res.contains(sbool)) { } 
        else 
        { res.add(sbool); } 
      } 
    }  
    return res; 
  } 

  public Vector unusedSourceEnumerations()
  { Vector sbools = realsrc.getLocalEnumerationFeatures(); 
    System.out.println(">>> Enumeration source features: " + sbools); 
    Vector res = new Vector();

    for (int i = 0; i < sbools.size(); i++) 
    { Attribute sbool = (Attribute) sbools.get(i); 
      if (isUnusedSource(sbool))
      { if (res.contains(sbool)) { } 
        else 
        { res.add(sbool); }
      }  
    }  
    return res; 
  } 

  public Vector unusedSourceStrings()
  { Vector sbools = realsrc.getLocalStringFeatures(); 
    // System.out.println(">>> String source features: " + sbools); 
    Vector res = new Vector();

    for (int i = 0; i < sbools.size(); i++) 
    { Attribute sbool = (Attribute) sbools.get(i); 
      if (isUnusedSource(sbool))
      { if (res.contains(sbool)) { } 
        else 
        { res.add(sbool); }
      }  
    }  
    return res; 
  } 

  public Vector unusedSourceOptionals()
  { Vector sbools = realsrc.getLocalOptionalFeatures(); 
    // System.out.println(">>> Optional source features: " + sbools); 
    Vector res = new Vector();

    for (int i = 0; i < sbools.size(); i++) 
    { Attribute sbool = (Attribute) sbools.get(i); 
      if (isUnusedSource(sbool))
      { if (res.contains(sbool)) { } 
        else 
        { res.add(sbool); }
      }  
    }  
    return res; 
  } 

  public Vector allSourceOptionals()
  { Vector sbools = realsrc.getLocalOptionalFeatures();
    return sbools; 
  }  

  public Vector unusedSourceConditions(Vector featuresets)
  { Vector locals1 = realsrc.getLocalFeatures();
    Vector locals2 = realsrc.getNonLocalFeatures(); 

    Vector res1 = new Vector(); 
    Vector res2 = new Vector(); 

    for (int i = 0; i < locals1.size(); i++) 
    { Attribute sbool = (Attribute) locals1.get(i); 
      if (isUnusedSource(sbool))
      { res1.add(sbool); } 
    }  
 
    for (int i = 0; i < locals2.size(); i++) 
    { Attribute sbool = (Attribute) locals2.get(i); 
      if (isUnusedSource(sbool))
      { res2.add(sbool); } 
    }  

    System.out.println(">>> Unused local source features: " + res1); 
    System.out.println(">>> Unused non-local source features: " + res2); 

    BasicExpression selfexp = new BasicExpression("self"); 
    selfexp.setType(new Type(realsrc)); 
    selfexp.setElementType(new Type(realsrc)); 

    Vector res = new Vector();
    for (int i = 0; i < res1.size(); i++) 
    { Attribute ix = (Attribute) res1.get(i); 
      Type tix = ix.getType(); 
      Type tixe = ix.getElementType(); 
      if (tix != null && tix.isEntityType() && tix.getEntity() == realsrc)
      { BasicExpression eix = new BasicExpression(ix); 
        eix.setUmlKind(Expression.ATTRIBUTE); 
        BinaryExpression cond = new BinaryExpression("=",selfexp,eix); 
        res.add(cond); 
        featuresets.add(ix); 
      } 
      else if (tixe != null && tixe.isEntityType() && tixe.getEntity() == realsrc)
      { BasicExpression eix = new BasicExpression(ix); 
        eix.setUmlKind(Expression.ATTRIBUTE); 
        BinaryExpression cond = new BinaryExpression("->includes",eix,selfexp); 
        res.add(cond); 
        featuresets.add(ix); 
      } 
    }

    for (int i = 0; i < res2.size(); i++) 
    { Attribute ix = (Attribute) res2.get(i); 
      Type tix = ix.getType(); 
      Type tixe = ix.getElementType(); 
      if (tix != null && tix.isEntityType() && tix.getEntity() == realsrc)
      { BasicExpression eix = new BasicExpression(ix); 
        eix.setUmlKind(Expression.ATTRIBUTE); 
        BinaryExpression cond = new BinaryExpression("=",selfexp,eix); 
        res.add(cond); 
        featuresets.add(ix); 
      } 
      else if (tixe != null && tixe.isEntityType() && tixe.getEntity() == realsrc)
      { BasicExpression eix = new BasicExpression(ix); 
        eix.setUmlKind(Expression.ATTRIBUTE); 
        BinaryExpression cond = new BinaryExpression("->includes",eix,selfexp); 
        res.add(cond); 
        featuresets.add(ix); 
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
      Vector entmatches = ModelMatching.getEntityMatchings(supplier,ems); 
      for (int i = 0; i < entmatches.size(); i++) 
      { EntityMatching em = (EntityMatching) entmatches.get(i); 
        Vector srefs = em.realtrg.getLocalReferenceFeatures(); 
        for (int j = 0; j < srefs.size(); j++) 
        { Attribute att = (Attribute) srefs.get(j); 
          if (em.isUnusedTarget(att) && ModelMatching.compatibleReverse(sref,att,ems))
          { if (res.contains(att)) { } 
            else 
            { res.add(att); }
          } 
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
        { if (res.contains(att)) { } 
          else 
          { res.add(att); } 
        } 
      } 
    } 
    return res; 
  } 

  public Vector unusedMandatoryTargetReferences(Vector ems) 
  { // reference 1 features of realtrg, unused as target features

    Vector res = new Vector(); 
    Vector trefs = realtrg.getLocalReferenceFeatures(); 
    for (int i = 0; i < trefs.size(); i++) 
    { Attribute att = (Attribute) trefs.get(i); 
      if (att.isMandatory() && ModelMatching.isUnusedTarget(att,ems))
      { if (res.contains(att)) { } 
        else 
        { res.add(att); } 
      } 
    } 
    return res; 
  } 

  public boolean isUnused(Attribute x, Vector ems)
  { // assume path size <= 2

    if (condition != null) 
    { if (usedInCondition(x)) 
      { return false; } 
    } 

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
    // Better to check along the path by name & owner


  public Vector findCompatibleMappings(Attribute x, Vector ems) 
  { Vector res = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (am.isValueAssignment()) { } 
      else if (am.isExpressionAssignment()) { } 
      else if (am.isStringAssignment()) { } 
      else if (am.src.getName().equals(x.getName())) {}
      else if ((am.src.getType() + "").equals(x.getType() + "")) 
      { if (res.contains(am)) { } 
        else 
        { res.add(am); }
      }   
      else if (am.src.getElementType() != null && 
               (am.src.getElementType() + "").equals(
                                        x.getElementType() + "")) 
      { if (res.contains(am)) { } 
        else 
        { res.add(am); }
      }   
      else if (am.src.getElementType() != null && x.getElementType() != null && 
               am.src.getElementType().isEntity() && x.getElementType().isEntity())
      { Entity e1 = am.src.getElementType().getEntity(); 
        Entity e2 = x.getElementType().getEntity(); 
        // and they map to the same entity
        Entity e1img = ModelMatching.lookupRealMatch(e1,ems); 
        Entity e2img = ModelMatching.lookupRealMatch(e2,ems); 
        if (e1img == e2img) 
        { if (res.contains(am)) { } 
          else 
          { res.add(am); } 
        } 
      } 
      else if ((am.trg.getType() + "").equals(x.getType() + ""))
      { if (res.contains(am)) { } 
        else 
        { res.add(am); }
      }  
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
  } // also use NMS

  public static Vector findClosestNamed(Attribute x, Vector atts)
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
  } // also NMS

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
  } // also NMS

  public AttributeMatching findSuperclassMapping(Attribute x, Vector ems) 
  { Entity sup = src.getSuperclass(); 
    if (sup != null) 
    { Vector supmatches = ModelMatching.getMatchings(sup,ems); 
      for (int xx = 0; xx < supmatches.size(); xx++) 
      { EntityMatching supmatch = (EntityMatching) supmatches.get(xx); 
        Vector ams = supmatch.getAttributeMatchings();  
        // System.out.println(supmatch.realsrc + " >> " + supmatch.realtrg + " has mappings " + ams); 
        for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching am = (AttributeMatching) ams.get(i); 
          if (am.src.getName().equals(x.getName()))
          { return am; }  
        }

        AttributeMatching sm = supmatch.findSuperclassMapping(x,ems);
        if (sm != null) 
        { return sm; }   
      } 
      return null;  
    } 
    return null; 
  } 

  public AttributeMatching findConsistentSuperclassMapping(Attribute x, Vector ems) 
  { Entity sup = src.getSuperclass(); 
    if (sup != null) 
    { Vector supmatches = ModelMatching.getMatchings(sup,ems); 
      for (int j = 0; j < supmatches.size(); j++) 
      { EntityMatching supmatch = (EntityMatching) supmatches.get(j); 
        if (supmatch.realtrg == realtrg || 
            Entity.isAncestor(supmatch.realtrg,realtrg)) 
        { Vector ams = supmatch.getAttributeMatchings();  
          // System.out.println(supmatch.realsrc + " >> " + supmatch.realtrg + " has mappings " + ams); 
          for (int i = 0; i < ams.size(); i++) 
          { AttributeMatching am = (AttributeMatching) ams.get(i); 
            if (am.src.getName().equals(x.getName()))
            { return am; }  
          }
        } 

        AttributeMatching sm = supmatch.findConsistentSuperclassMapping(x,ems);
        if (sm != null) 
        { return sm; }   
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
                  JOptionPane.showInputDialog("Remove " + realsrc + " mapping " + am + 
                                              " that may conflict with " + 
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
  } // could also merge them

    
  boolean isConcrete()
  { return src.isConcrete(); } 

  boolean isConcreteTarget()
  { return trg.isConcrete(); } 

  public String getName()
  { return realsrc.getName() + "2" + realtrg.getName(); } 

  public String toString()
  { String res = ""; 
    if (condition == null) 
    { res = realsrc.getName() + " |--> " + realtrg.getName(); } 
    else 
    { res = "{ " + condition + " } " + 
            realsrc.getName() + " |--> " + realtrg.getName(); 
    } 

    if (postcondition != null) 
    { res = res + " { " + postcondition + " }"; } 
    res = res + "\n"; 
     
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
    addAttributeMappings(added); 
  } 


  public void createSubclassMatchings(Vector srcsubs, Vector ems)
  { Vector added = new Vector(); 
    for (int i = 0; i < srcsubs.size(); i++) 
    { Entity srcsub = (Entity) srcsubs.get(i); 
      EntityMatching e2tent = ModelMatching.findEntityMatchingFor(srcsub,
                                                             realtrg,ems);
      if (e2tent == null) 
      { EntityMatching emnew = new EntityMatching(srcsub,realtrg); 
        added.add(emnew); 
      }  
    }
    ems.addAll(added); 
  }  
   
  public void copyAttributeMappingsToSubclasses(Vector subs, Vector ems, 
                                                Vector thesaurus, ModelMatching modmatch, 
                                                Vector entities)
  { Vector added = new Vector(); 
    Map mymap = modmatch.mymap; 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      Vector ematches = ModelMatching.getEntityMatchings(sub,ems); 

      for (int j = 0; j < ematches.size(); j++) 
      { EntityMatching em = (EntityMatching) ematches.get(j); 
        if (realtrg == em.realtrg || Entity.isAncestor(realtrg,em.realtrg)) 
        { System.out.println(">> Copying attribute matchings from " + 
              realsrc + " |--> " + realtrg + " down to " + em.realsrc + " |--> " + em.realtrg); 
          em.copyAttributeMappings(attributeMappings,thesaurus,modmatch,entities); 
        }
        else 
        { System.err.println(">> Target " + em.realtrg + " of " + em.realsrc + 
                             " is not subclass of " + realtrg); 
          
          // option to change this mapping, em, or to create a new mapping.
          String ans0 = JOptionPane.showInputDialog("Change " + realsrc + " |--> " + 
                                          realtrg + " target to " + em.realtrg + 
                                          " or superclass?: (y/n) ");
          if (ans0 != null && "y".equals(ans0))
          { if (em.realtrg.getSuperclass() != null && realsrc.isAbstract())
            { realtrg = em.realtrg.getSuperclass(); 
              // May invalidate attribute mappings
              revalidateAttributeMappings(); 
            } 
            else if (em.realtrg.isConcrete())
            { realtrg = em.realtrg; 
              revalidateAttributeMappings(); 
            } 
          }     
          else if (realtrg.isConcrete())
          { String ans = 
              JOptionPane.showInputDialog("Create additional map " + sub + " |--> " + 
                                          realtrg + " (class splitting vertical of " + sub + 
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

      if (ematches.size() == 0) 
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

    // ems.addAll(added); 
    modmatch.addEntityMatchings(added,entities); 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      EntityMatching em = ModelMatching.getEntityMatching(sub,ems); 
      if (em != null && em.realsrc != null) 
      { Vector subsubs = em.realsrc.getSubclasses(); 
        if (subsubs.size() > 0) 
        { em.copyAttributeMappingsToSubclasses(subsubs,ems,thesaurus,modmatch,entities); } 
      } 
    }     
  } // and recursively

  public void copyAttributeMappingToSubclasses(AttributeMatching amnew, Vector subs, Vector ems, 
                                               Vector thesaurus, ModelMatching modmatch, 
                                               Vector entities)
  { Vector added = new Vector(); 
    Vector ams = new Vector(); 
    ams.add(amnew); 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      EntityMatching em = ModelMatching.getEntityMatching(sub,ems); 
      if (em != null) 
      { if (realtrg == em.realtrg || Entity.isAncestor(realtrg,em.realtrg)) 
        { System.out.println(">> Copying " + amnew + " from " + 
              realsrc + " |--> " + realtrg + " down to " + em.realsrc + " |--> " + em.realtrg); 
          em.copyAttributeMappings(ams,thesaurus,modmatch,entities); 
        }
        else 
        { System.err.println(">> Target " + em.realtrg + " of " + em.realsrc + 
                             " is not subclass of " + realtrg); 
          
          // option to change this mapping, em, or to create a new mapping.
          System.out.println(">> Copying applicable attribute matchings from " + 
              realsrc + " |--> " + realtrg + " to " + em.realsrc + " |--> " + em.realtrg); 
          em.copyApplicableAttributeMappings(ams); 
        }  
      }
      else 
      { System.err.println(">> No mapping for subclass " + sub + " of " + realsrc); 
        if (realtrg.isConcrete())
        { System.err.println(">> Creating mapping to " + realtrg + 
                             " for subclass " + sub + " of " + realsrc); 
          EntityMatching newem = new EntityMatching(sub,realtrg); 
          newem.addAttributeMappings(attributeMappings);
          newem.addAttributeMappings(ams);  
          added.add(newem); 
        }
      }  
    }  

    modmatch.addEntityMatchings(added,entities); 

    for (int i = 0; i < subs.size(); i++) 
    { Entity sub = (Entity) subs.get(i);
      EntityMatching em = ModelMatching.getEntityMatching(sub,ems); 
      if (em != null && em.realsrc != null) 
      { Vector subsubs = em.realsrc.getSubclasses(); 
        if (subsubs.size() > 0) 
        { em.copyAttributeMappingToSubclasses(amnew,subsubs,ems,thesaurus,modmatch,entities); } 
      } 
    }     
  } // and recursively

  public void copyAttributeMappings(Vector ams, Vector thesaurus, 
                                    ModelMatching modmatch, Vector entities)
  { Vector removed = new Vector(); 
    Vector added = new Vector(); 
    Map mymap = modmatch.mymap; 

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

          if (simnew > simold)
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
    addAttributeMappings(added); 
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

            if (amx.src.getName().equals(am.src.getName())) { } 
            else 
            { String ans = 
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
    addAttributeMappings(added); 
  }       

  public void revalidateAttributeMappings()
  { Vector removed = new Vector(); 
    Vector added = new Vector(); 

    // ams are the mappings from the superclass of src. 

    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      Attribute amsrc = am.src; 
      Attribute amtrg = am.trg;
   
      if (realtrg.isDefinedDataFeature(am.trg)) 
      { System.out.println(">--- " + realtrg + " has feature " + am.trg); } 
      else 
      { removed.add(am); } 
    } 

    attributeMappings.removeAll(removed); 
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


  public Vector analyseCorrelationPatterns(Vector ems, ModelMatching modmatch, 
                                           Vector entities, Vector types)
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

      CorrelationPattern p = new CorrelationPattern(splitkind + " class splitting", 
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
          else 
          { String response = 
              JOptionPane.showInputDialog("Enter new LHS expression for map " + 
                                          am1.src + " |--> " + am1.trg +
                                            "?: (expression/null) ");
            if (response != null && !"null".equals(response))
            { promptForAttributeMapping(response,entities,types, 
                                         am1,removed0,added0);
            }  
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

    // are there any unused source attributes?      
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
              else 
              { String response = 
                  JOptionPane.showInputDialog("Enter new LHS expression for map " + 
                                              amx.src + " |--> " + amx.trg +
                                              "?: (expression/null) ");
                if (response != null && !"null".equals(response))
                { promptForAttributeMapping(response,entities,types, 
                                            amx,removed0,added0);
                } 
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
      if (isUnusedSource(amsrc) && 
          (!realsrc.isShared() || amsrc.isSource()) && 
          isUnused(amsrc,ems))
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
                if (added.contains(newam)) { } 
                else 
                { added.add(newam); } 
              }
              else 
              { String response = 
                  JOptionPane.showInputDialog("Enter new LHS expression for map " + 
                                              amx.src + " |--> " + amx.trg +
                                              "?: (expression/null) ");
                if (response != null && !"null".equals(response))
                { promptForAttributeMapping(response,entities,types, 
                                         amx,removed0,added0);
                }  
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
    addAttributeMappings(added0); 
    attributeMappings.removeAll(removed); 
    addAttributeMappings(added); 

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
                addAttributeMapping(amnew); 
                CorrelationPattern introSelect = new CorrelationPattern("Introduce select", 
                   "Select of unused boolean " + sel + " mapped to " + fref); 
                if (res.contains(introSelect)) { } 
                else 
                { res.add(introSelect); } 
              } 
            }  // also try unused supplier enumerations 
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
              CorrelationPattern reversedad = new CorrelationPattern("Reversed association direction", 
                "Reversed source association " + rev + " maps to target " + fref); 
              if (res.contains(reversedad)) { } 
              else 
              { res.add(reversedad); } 
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
                                              realsrc,realtrg,this,ems,modmatch,entities); 
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


  private void promptForAttributeMapping(String response, Vector entities, Vector types, 
                                         AttributeMatching am1, Vector removed0, Vector added0) 
  { Compiler2 cc = new Compiler2(); 
    cc.nospacelexicalanalysis(response); 
    Expression newexp = cc.parseExpression(); 

    if (newexp != null) 
    { Vector contexts = new Vector(); 
      contexts.add(realsrc); 
      newexp.typeCheck(types,entities,contexts,new Vector());
      Vector auxvars = newexp.allAttributesUsedIn(); 

      // System.out.println(">>>> attributes used in " + newexp + " are: " + auxvars); 

      Attribute var = null; 
      if (auxvars.size() > 0) 
      { var = (Attribute) auxvars.get(0); } 

      AttributeMatching newam; 
      if ((newexp instanceof BasicExpression) && (var + "").equals(newexp + ""))
      { newam = new AttributeMatching(var, am1.trg); } 
      else 
      { newam = new AttributeMatching(newexp, am1.trg, var, auxvars); } 
 
      removed0.add(am1);  
      added0.add(newam);
    }
  } 

  public void removeDuplicateMappings()
  { Vector res = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++) 
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      if (res.contains(am)) { } 
      else 
      { res.add(am); } 
    } 
    attributeMappings.clear(); 
    attributeMappings.addAll(res); 
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
        if (em.realsrc == srcsup && Entity.isDescendant(realtrg,em.realtrg)) 
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

    // Should include attribute mappings that are not in any superclass rule & of 1-mult and of 
    // value type 

    if (condition != null) 
    { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
      res = res + "    when { " + cexp + " }\n"; 
    } 
    res = res +  "  }\n"; 
    return res; 
  } 

  String qvtrule1bx(Vector ems) 
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
    BasicExpression trgv = new BasicExpression(trgvar); 

    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realtrg != null)
    { trgent = realtrg.getName(); 
      trgv.setType(new Type(realtrg)); 
      trgv.setElementType(new Type(realtrg)); 
    } 

    Vector localams = new Vector(); 
    localams.addAll(attributeMappings); 
	
    String overrides = ""; 
    String res = " top relation " + srcent + "2" + trgent; 
    if (src.isConcrete())
    { res = " " + res; }  
    else 
    { res = "  abstract" + res; }   

    Entity srcsup = realsrc.getSuperclass(); 
    if (srcsup != null) 
    { EntityMatching em = ModelMatching.getAncestorMatching(srcsup,realtrg,ems); 
      if (em != null) 
      { String ssname = em.realsrc.getName(); 
        String stname = em.realtrg.getName(); 
        overrides = " overrides " + ssname + "2" + stname + " ";  
	  localams.removeAll(em.attributeMappings); 
      }
    }     
    res = res + overrides + "\n"; 

    Attribute srcroot = new Attribute(srcvar, new Type(realsrc), ModelElement.INTERNAL); 
    Attribute trgroot = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 

    ObjectTemplateExp sourceObjectTemplate = new ObjectTemplateExp(srcroot,realsrc); 
    ObjectTemplateExp targetObjectTemplate = new ObjectTemplateExp(trgroot,realtrg); 

    Vector whereclause = new Vector(); 

    for (int i = 0; i < localams.size(); i++)
    { AttributeMatching am = (AttributeMatching) localams.get(i); 

      if (am.isComposedTarget()) { continue; } 

      if (am.isStringMapping())
      { Vector reverseassignments = am.inverts(realsrc,realtrg,ems); 
        Vector auxvars = am.getAuxVariables(); 
        // Expression val = (Expression) am.srcvalue.clone(); 
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector(); 
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
          // String avdata = AttributeMatching.dataname(srcvar,avpath);
          am.sourceequation(srcvar,avpath, 
                            sourceObjectTemplate);
          // val = val.substituteEq(av + "", new BasicExpression(avdata));
          // System.out.println("STRING ASSIGNMENT " + am + " inverted is: "); 
        } 
        Expression srcexpr = am.srcvalue.addReference(new BasicExpression(srcroot), 
                                                      new Type(realsrc));
        whereclause.add(trgv + "." + am.trg + " = " + srcexpr);

        /* Vector trgpath = new Vector(); 
        if (am.trg.getNavigation().size() == 0) 
        { trgpath.add(am.trg); } 
        else 
        { trgpath.addAll(am.trg.getNavigation()); } 
                 am.targetequation(trgvar,trgpath,val+"",targetObjectTemplate);  
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector();
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
           
          am.sourceequation(srcvar,avpath, 
                            sourceObjectTemplate);
          String srcdata = am.sourcedataname(srcvar); 
          Vector trgpath = new Vector(); 
          if (am.trg.getNavigation().size() == 0) 
          { trgpath.add(am.trg); } 
          else 
          { trgpath.addAll(am.trg.getNavigation()); } 
               
          am.targetequation(trgvar,trgpath,val+"", 
                            targetObjectTemplate);  
        am.targetequationbx(srcv,trgv,trgvar,srcdata,
                           targetObjectTemplate,whereclause); */ 

        for (int j = 0; j < reverseassignments.size(); j++) 
        { AttributeMatching invam = (AttributeMatching) reverseassignments.get(j); 
		  // System.out.println(">>> Inverse: " + invam); 
		  
          whereclause.add(trgv + "." + invam.trg + " = " + srcv + "." + invam.src); 
        }  
      } 
      else if (am.isExpressionAssignment()) 
      { } 
      else if (am.isValueAssignment())
      { Expression sval = 
          am.srcvalue.addReference(new BasicExpression(srcroot), 
                                   new Type(realsrc));
        String trgeq = trgvar + "." + am.trg + " = " + sval;
        // targetObjectTemplate.addPTI(am.trg,sval);
        Vector trgpath = new Vector();
		 
		if (am.srcvalue.isEmptyCollection())
		{ whereclause.add(trgeq); }
		else 
        { if (am.trg.getNavigation().size() == 0) 
          { trgpath.add(am.trg); } 
          else 
          { trgpath.addAll(am.trg.getNavigation()); } 
          am.targetequation(trgvar,trgpath,sval + "",targetObjectTemplate);
		}   
		// Inverse entity mapping has the condition { trgeq }
      }    
      else if (am.isDirect() && am.notReferenceTarget())
      { String srceq = "";
        String trgeq = "";  
        String srcdata = am.sourcedataname(srcvar); 
        
		Vector possibles = new Vector(); 
        EntityMatching emx = am.isObjectMatch(ems,possibles); 
        if (emx == null && possibles.size() == 0)
        { srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  

		  /* if (am.isEquality())
		  { whereclause.add(trgvar + "." + am.trg + " = " + srcdata); 
		    whereclause.add(srcvar + "." + am.src + " = " + trgvar + "." + am.trg); 
          } */ 

        } 
      } 
    } 

    if (condition != null) 
    { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
      sourceObjectTemplate.addWhere(cexp); 
    } 

    res = res +  "  { checkonly domain source " + sourceObjectTemplate + ";\n"; 
    res = res +  "    enforce domain target " + targetObjectTemplate + ";\n"; 

    // Should include attribute mappings that are not in any superclass rule & of 1-mult and of 
    // value type 
	

	
	if (whereclause.size() > 0)
	{ res = res + "    where {\n"; 
	  for (int i = 0; i < whereclause.size(); i++) 
	  { res = res + "      " + whereclause.get(i); 
	    if (i < whereclause.size() - 1)
		{ res = res + " and\n"; } 
		else 
		{ res = res + "\n"; }; 
      }
	  res = res + "    }\n";
	} 
    res = res +  "  }\n"; 
    return res; 
  } 
  
  String qvtrule1InstanceReplication(Vector ems) 
  { // if (src == trg) { return ""; } 
  
    Expression repl = (Expression) replicationConditions.get(0); // assume just one at the moment
	
	if (repl instanceof BinaryExpression) { } 
	else 
	{ return ""; }
	
	BinaryExpression repExpr = (BinaryExpression) repl; 
	BasicExpression repVar = (BasicExpression) repExpr.getLeft();  // should be a BasicExpression of String or Entity type. 
    Expression repExprR = repExpr.getRight();  // should be a BasicExpression of String or Entity type. 

    Type repType = repVar.getType(); 
    System.out.println(">>> Replication constraint in phase 1; condition = " + repExpr + " variable type = " + repType); 


    String srcvar = srcname.toLowerCase() + "x"; 
    BasicExpression srcv = new BasicExpression(srcvar); 
    String srcent = srcname.substring(0,srcname.length()-1); 

    Expression repExpr1 = repExprR.addReference(srcv, new Type(realsrc));

    if (realsrc != null)
    { srcent = realsrc.getName(); 
      srcv.setType(new Type(realsrc)); 
      srcv.setElementType(new Type(realsrc)); 
    } 

    String trgvar = trgname.toLowerCase() + "x"; 
    BasicExpression trgv = new BasicExpression(trgvar); 

    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realtrg != null)
    { trgent = realtrg.getName(); 
      trgv.setType(new Type(realtrg)); 
      trgv.setElementType(new Type(realtrg)); 
    } 

    Vector localams = new Vector(); 
    localams.addAll(attributeMappings); 
	
    String overrides = ""; 
    String res = " top relation " + srcent + "2" + trgent + repVar; 
    if (src.isConcrete())
    { res = " " + res; }  
    else 
    { res = "  abstract" + res; }   

    Entity srcsup = realsrc.getSuperclass(); 
    if (srcsup != null) 
    { EntityMatching em = ModelMatching.getAncestorMatching(srcsup,realtrg,ems); 
      if (em != null) 
      { String ssname = em.realsrc.getName(); 
        String stname = em.realtrg.getName(); 
        overrides = " overrides " + ssname + "2" + stname + ",  " + srcent + "2" + trgent;  
        localams.removeAll(em.attributeMappings); 
      }
      else 
      { overrides = " overrides " + srcent + "2" + trgent; }   
    }     
    else 
    { overrides = " overrides " + srcent + "2" + trgent; }

    res = res + overrides + "\n"; 

    Attribute srcroot = new Attribute(srcvar, new Type(realsrc), ModelElement.INTERNAL); 
    Attribute trgroot = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 

    ObjectTemplateExp sourceObjectTemplate = new ObjectTemplateExp(srcroot,realsrc); 
    ObjectTemplateExp targetObjectTemplate = new ObjectTemplateExp(trgroot,realtrg); 

    Vector whereclause = new Vector(); 
	Vector whenclauses = new Vector(); 
    Map whens = new Map(); 

    for (int i = 0; i < localams.size(); i++)
    { AttributeMatching am = (AttributeMatching) localams.get(i); 
	
	  System.out.println(">> Processing attribute mapping: " + am); 
		  
	  if ((am.src + "").equals(repVar + "") || (am.srcvalue + "").equals(repVar + "")) 
	  { String srceq = "";
        String trgeq = "";  
        String srcdata = am.sourcedataname(srcvar); 
        
		System.out.println(">> Replication assignment: " + am); 
		  
		Vector possibles = new Vector(); 
        EntityMatching emx = am.isObjectMatch(ems,possibles); 

		System.out.println(">> Entity matching: " + emx + " " + possibles); 

        if (emx != null)
        { // srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  
          System.out.println(">> Target equation: " + trgeq); 
		  /* if (am.isEquality())
		  { whereclause.add(trgvar + "." + am.trg + " = " + srcdata); 
		    whereclause.add(srcvar + "." + am.src + " = " + trgvar + "." + am.trg); 
          } */ 
          am.dependsOn = emx; 
          String whenc = am.whenClause(emx,repVar + "",srcvar,trgvar,whens); 
          whenclauses.add(whenc);
          // srceq = am.sourceequation(srcvar,sourceObjectTemplate);
		  // clausecount++;  
        } 
		else if (possibles.size() > 0)
		{ String whencs = am.whenClause(possibles,srcvar,trgvar,whens); 
		  whenclauses.add("(" + whencs + ")");
          // srceq = am.sourceequation(srcvar,sourceObjectTemplate);
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  
		  // clausecount++;  
        }
		else 
		{ String whenc = am.whenClause(trgvar,repVar + "",ems,whens); 
          if (whenc != null && whenc.trim().length() > 0 && !("true".equals(whenc)))
          { whenclauses.add(whenc); }
		}  
	  }
      else if (am.isComposedTarget()) 
	  { continue; } 
      else if (am.isStringMapping())
      { Vector reverseassignments = am.inverts(realsrc,realtrg,ems); 
        Vector auxvars = am.getAuxVariables(); 
        // Expression val = (Expression) am.srcvalue.clone(); 
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector(); 
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
          // String avdata = AttributeMatching.dataname(srcvar,avpath);
          am.sourceequation(srcvar,avpath, 
                            sourceObjectTemplate);
          // val = val.substituteEq(av + "", new BasicExpression(avdata));
          // System.out.println("STRING ASSIGNMENT " + am + " inverted is: "); 
        } 
        Expression srcexpr = am.srcvalue.addReference(new BasicExpression(srcroot), 
                                                      new Type(realsrc));
        whereclause.add(trgv + "." + am.trg + " = " + srcexpr);

        /* Vector trgpath = new Vector(); 
        if (am.trg.getNavigation().size() == 0) 
        { trgpath.add(am.trg); } 
        else 
        { trgpath.addAll(am.trg.getNavigation()); } 
                 am.targetequation(trgvar,trgpath,val+"",targetObjectTemplate);  
        for (int j = 0; j < auxvars.size(); j++) 
        { Attribute av = (Attribute) auxvars.get(j); 
          Vector avpath = new Vector();
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
           
          am.sourceequation(srcvar,avpath, 
                            sourceObjectTemplate);
          String srcdata = am.sourcedataname(srcvar); 
          Vector trgpath = new Vector(); 
          if (am.trg.getNavigation().size() == 0) 
          { trgpath.add(am.trg); } 
          else 
          { trgpath.addAll(am.trg.getNavigation()); } 
               
          am.targetequation(trgvar,trgpath,val+"", 
                            targetObjectTemplate);  
        am.targetequationbx(srcv,trgv,trgvar,srcdata,
                           targetObjectTemplate,whereclause); */ 

        for (int j = 0; j < reverseassignments.size(); j++) 
        { AttributeMatching invam = (AttributeMatching) reverseassignments.get(j); 
		  // System.out.println(">>> Inverse: " + invam); 
		  
          whereclause.add(trgv + "." + invam.trg + " = " + srcv + "." + invam.src); 
        }  
      } 
      else if (am.isExpressionAssignment()) 
      { System.out.println(">>> Expression assignment: " + am); 
      } 
      else if (am.isValueAssignment())
      { Expression sval = 
          am.srcvalue.addReference(new BasicExpression(srcroot), 
                                   new Type(realsrc));
        String trgeq = trgvar + "." + am.trg + " = " + sval;
        // targetObjectTemplate.addPTI(am.trg,sval);
		System.out.println(">>> Value assignment: " + am + " from " + sval + " target equation: " + trgeq);
		
		if (am.srcvalue.isEmptyCollection())
		{ whereclause.add(trgeq); }
		else 
        { Vector trgpath = new Vector(); 
          if (am.trg.getNavigation().size() == 0) 
          { trgpath.add(am.trg); } 
          else 
          { trgpath.addAll(am.trg.getNavigation()); } 
          am.targetequation(trgvar,trgpath,sval + "",targetObjectTemplate);  
		// Inverse entity mapping has the condition { trgeq }
		} 
      }    
      else if (am.isDirect())
      { String srceq = "";
        String trgeq = "";  
        String srcdata = am.sourcedataname(srcvar); 
		
		System.out.println(">>> Direct assignment: " + am + " from " + srcdata); 
        
		Vector possibles = new Vector(); 
        EntityMatching emx = am.isObjectMatch(ems,possibles); 
        if (emx != null)
        { srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  

		  /* if (am.isEquality())
		  { whereclause.add(trgvar + "." + am.trg + " = " + srcdata); 
		    whereclause.add(srcvar + "." + am.src + " = " + trgvar + "." + am.trg); 
          } */ 
          am.dependsOn = emx; 
          String whenc = am.whenClause(emx,srcvar,trgvar,whens); 
          whenclauses.add(whenc);
          // srceq = am.sourceequation(srcvar,sourceObjectTemplate);
		  // clausecount++;  
        } 
		else if (possibles.size() > 0)
		{ String whencs = am.whenClause(possibles,srcvar,trgvar,whens); 
		  whenclauses.add("(" + whencs + ")");
          srceq = am.sourceequation(srcvar,sourceObjectTemplate);
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  
		  // clausecount++;  
        }
		else 
		{ srceq = am.sourceequation(srcvar,sourceObjectTemplate);
          trgeq = am.targetequationbx(srcv,trgv,trgvar,srcdata,targetObjectTemplate,whereclause);                  
		}
      } 
    } 

    // if (condition != null) 
    // { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
    //   sourceObjectTemplate.addWhere(cexp); 
    // } 

    res = res +  "  { checkonly domain source " + repVar + " : " + repType + "{ };\n" + 
	             "    checkonly domain source " + sourceObjectTemplate + " { " + repExpr1 + "->includes(" + repVar + ") };\n"; 
    res = res +  "    enforce domain target " + targetObjectTemplate + ";\n"; 

    // Should include attribute mappings that are not in any superclass rule & of 1-mult and of 
    // value type 
	
    String whenclause = ""; 
 
    boolean previous = false; 
    for (int k = 0; k < whenclauses.size(); k++) 
    { String w = (String) whenclauses.get(k); 
      if ("true".equals(w)) { } 
      else if ("".equals(w)) { } 
      else 
      { if (previous) 
        { whenclause = whenclause + " and\n          " + w; } 
        else  
        { whenclause = whenclause + w; 
          previous = true; 
        }
      }   
	}
	  
    if (whenclause.length() > 0) 
	{ res = res +  "    when {\n          " + whenclause;
      res = res + " }\n"; 
	} 
	
	if (whereclause.size() > 0)
	{ res = res + "    where {\n"; 
	  for (int i = 0; i < whereclause.size(); i++) 
	  { res = res + "      " + whereclause.get(i); 
	    if (i < whereclause.size() - 1)
		{ res = res + " and\n"; } 
		else 
		{ res = res + "\n"; }; 
      }
	  res = res + "    }\n";
	} 
    res = res +  "  }\n"; 
    return res; 
  } 

  public String abstractReplicationRule(Vector ems) 
  { String srcvar = srcname.toLowerCase() + "x"; 
    BasicExpression srcv = new BasicExpression(srcvar); 
    String srcent = srcname.substring(0,srcname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); 
      srcv.setType(new Type(realsrc)); 
      srcv.setElementType(new Type(realsrc)); 
    } 

    String trgvar = trgname.toLowerCase() + "x"; 
    BasicExpression trgv = new BasicExpression(trgvar); 

    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realtrg != null)
    { trgent = realtrg.getName(); 
      trgv.setType(new Type(realtrg)); 
      trgv.setElementType(new Type(realtrg)); 
    } 
	
    String overrides = ""; 
    String res = " top relation " + srcent + "2" + trgent; 
    if (src.isConcrete())
    { res = " " + res; }  
    else 
    { res = "  abstract" + res; }   

    Entity srcsup = realsrc.getSuperclass(); 
    if (srcsup != null) 
    { EntityMatching em = ModelMatching.getAncestorMatching(srcsup,realtrg,ems); 
      if (em != null) 
      { String ssname = em.realsrc.getName(); 
        String stname = em.realtrg.getName(); 
        overrides = " overrides " + ssname + "2" + stname + " ";  
      }
    }     
    res = res + overrides + "\n"; 

    Attribute srcroot = new Attribute(srcvar, new Type(realsrc), ModelElement.INTERNAL); 
    Attribute trgroot = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 


    res = res + "    checkonly domain source " + srcvar + " : " + realsrc + " {};\n"; 
    res = res + "    enforce domain target " + trgvar + " : " + realtrg + " {};\n"; 

    res = res +  "  }\n\n"; 
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
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
          // String avdata = AttributeMatching.dataname(srcvar,avpath); 
          am.sourceequation(srcvar,avpath,sourceObjectTemplate);
        } 
      } 
      else if (am.isExpressionAssignment())
      { Expression expr = am.getExpression(); 
        Expression instantiatedexpr = expr.addReference(new BasicExpression(srcroot), 
                                                        new Type(realsrc));
        if ("self".equals(expr + ""))
        { } 
        else if (expr.isMultiple())
        { Expression inst = new BasicExpression(am.getElementVariable());  
          Expression test = new BinaryExpression("->includes", instantiatedexpr, inst); 
          sourceObjectTemplate.addWhere(test); 
          // and add a domain am.elementVariable : E1 { }; 
          auxdomains = auxdomains + 
            "  domain source " + inst + " : " + expr.getElementType() + " {};\n"; 
        } 
        else 
        { Expression inst = new BasicExpression(am.getElementVariable());  
          Expression test = new BinaryExpression("=", inst, instantiatedexpr); 
          sourceObjectTemplate.addWhere(test); 
          // and add a domain am.elementVariable : E1 { }; 
          auxdomains = auxdomains + 
            "  domain source " + inst + " : " + inst.getType() + " {};\n"; 
        } 

        String whenc = am.whenClause(trgvar,instantiatedexpr + "",ems,whens); 
        if (whenc != null && whenc.trim().length() > 0 && !("true".equals(whenc)))
        { whenclauses.add(whenc); } 
      } 
      else if (am.isValueAssignment())
      { } 
      else if (am.isDirect())
      { String srceq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        
		Vector possibles = new Vector(); 
        
        EntityMatching emx = am.isObjectMatch(ems,possibles); 
        if (emx != null)
        { am.dependsOn = emx; 
          String whenc = am.whenClause(emx,srcvar,trgvar,whens); 
          whenclauses.add(whenc);
          srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
        } 
		else if (possibles.size() > 0)
		{ String whencs = am.whenClause(possibles,srcvar,trgvar,whens); 
		  whenclauses.add("(" + whencs + ")");
          srceq = am.sourceequation(srcvar,sourceObjectTemplate); 
        }
        else 
        { srceq = am.sourceequation(srcvar,sourceObjectTemplate); } 
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
          Vector avnavigation = av.getNavigation(); 
          if (avnavigation.size() == 0)  
          { avpath.add(av); } 
          else 
          { avpath.addAll(avnavigation); } 
          String avdata = AttributeMatching.dataname(srcvar,avpath);
          val = val.substituteEq(av + "", new BasicExpression(avdata));
          // System.out.println("TARGET NAME " + am.trg + " " + av + " " + val); 
        } 
        // targetObjectTemplate.addPTI(am.trg,val);
        Vector trgpath = new Vector(); 
        if (am.trg.getNavigation().size() == 0) 
        { trgpath.add(am.trg); } 
        else 
        { trgpath.addAll(am.trg.getNavigation()); } 
        am.targetequation(trgvar,trgpath,val+"",targetObjectTemplate);  
      } 
      else if (am.isExpressionAssignment()) 
      { if (am.srcvalue.isMultiple())
        { am.targetequation(trgvar,"",targetObjectTemplate); }
        else 
        { am.targetequation(trgvar, "" + am.getElementVariable(), 
                            targetObjectTemplate);
        }   
      }  
      else if (am.isValueAssignment())
      { Expression sval = am.srcvalue.addReference(new BasicExpression(srcroot), 
                                                   new Type(realsrc));
        String trgeq = am.trg + " = " + sval;
        // targetObjectTemplate.addPTI(am.trg,sval);
        Vector trgpath = new Vector(); 
        if (am.trg.getNavigation().size() == 0) 
        { trgpath.add(am.trg); } 
        else 
        { trgpath.addAll(am.trg.getNavigation()); } 
        am.targetequation(trgvar,trgpath,sval + "",targetObjectTemplate);  
      }   
      else if (am.isDirect())
      { String srceq = ""; 
        String trgeq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        trgeq = am.targetequation(trgvar,srcdata,targetObjectTemplate);                  
      }   
    } 

    if (postcondition != null) 
    { Expression post = postcondition.addReference(new BasicExpression(trgvar),new Type(realtrg)); 
      targetObjectTemplate.addWhere(post); 
    } 

    String res = "  top relation Map" + srcent + "2" + trgent + "\n"; 
    res = res +  "  { " + auxdomains + " checkonly domain source \n    " + sourceObjectTemplate + ";\n";
           // srcvar + " : " + srcent + " { " + srcbody + " };\n"; 
    res = res +  "    enforce domain target \n    " + targetObjectTemplate + ";\n"; 
          // trgvar + " : " + trgent + " { " + trgbody + " };\n"; 
    res = res +  "    when {\n          ";
 
    boolean previous = false; 
    for (int k = 0; k < whenclauses.size(); k++) 
    { String w = (String) whenclauses.get(k); 
      if ("true".equals(w)) { } 
      else if ("".equals(w)) { } 
      else 
      { if (previous) 
        { res = res + " and\n          " + w; } 
        else  
        { res = res + w; 
          previous = true; 
        }
      }  
    } 
    res = res + " }\n"; 
    res = res +  "  }\n"; 

    sourceTemplate = sourceObjectTemplate; 
    targetTemplate = targetObjectTemplate; 

    return res; 
  } // and when for the objects that are referenced. 

  String qvtrule2bx(Vector ems) 
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
	
	int clausecount = 0; 
 
    Vector whenclauses = new Vector(); 
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
    
    Vector ams1 = (Vector) Ocl.sortedBy(attributeMappings, srcnames); 
    Map whens = new Map(); 

    for (int i = 0; i < ams1.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams1.get(i); 

      if (am.isStringMapping()) { }
      else if (am.isExpressionAssignment())
      { Expression expr = am.getExpression(); 
        Expression instantiatedexpr = expr.addReference(new BasicExpression(srcroot), 
                                                        new Type(realsrc));
        Attribute elemvar = am.getElementVariable(); 
        System.out.println(">>> Element variable of " + am + " is>> " + elemvar); 
		  
        if ("self".equals(expr + ""))
        { } 
        else if (expr.isMultiple())
        { BasicExpression inst = new BasicExpression(elemvar);
		  // inst.setData(elemvar.getName() + "$x");   
          Expression test = new BinaryExpression("->includes", instantiatedexpr, inst); 
          sourceObjectTemplate.addWhere(test); 
          // and add a domain am.elementVariable : E1 { }; 
          auxdomains = auxdomains + 
            "  domain source " + inst + " : " + expr.getElementType() + " {};\n";
        } 
        else 
        { BasicExpression inst = new BasicExpression(elemvar);  
          inst.setData(elemvar.getName() + "$x");   
          Expression test = new BinaryExpression("=", inst, instantiatedexpr); 
          sourceObjectTemplate.addWhere(test); 
          // and add a domain am.elementVariable : E1 { }; 
          auxdomains = auxdomains + 
            "  domain source " + inst + " : " + elemvar.getType() + " {};\n"; 
        } 

        String whenc = am.whenClause(trgvar,instantiatedexpr + "",ems,whens);
		
		System.out.println(">>> linking when clause of " + am + " is>> " + whenc); 
		 
        if (whenc != null && whenc.trim().length() > 0 && !("true".equals(whenc)))
        { whenclauses.add(whenc); } 
		clausecount++; 
      } 
      else if (am.isValueAssignment())
      { } 
      else if (am.isDirect())
      { String srceq = ""; 
        String srcdata = am.sourcedataname(srcvar); 
        
		Vector possibles = new Vector(); 
        
        EntityMatching emx = am.isObjectMatch(ems,possibles); 
        if (emx != null)
        { am.dependsOn = emx; 
          String whenc = am.whenClause(emx,srcvar,trgvar,whens); 
          whenclauses.add(whenc);
          srceq = am.sourceequation(srcvar,sourceObjectTemplate);
		  clausecount++;  
        } 
		else if (possibles.size() > 0)
		{ String whencs = am.whenClause(possibles,srcvar,trgvar,whens); 
		  whenclauses.add("(" + whencs + ")");
          srceq = am.sourceequation(srcvar,sourceObjectTemplate);
		  clausecount++;  
        }
      } 
    } 

    if (clausecount == 0)
	{ return ""; }
	
    if (condition != null) 
    { Expression cexp = condition.addReference(srcv, new Type(realsrc)); 
      sourceObjectTemplate.addWhere(cexp); 
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


      if (am.isStringMapping()) { } 
	  else if (am.isExpressionAssignment()) 
      { if (am.srcvalue.isMultiple())
        { am.targetequation(trgvar,"",targetObjectTemplate); }
        else 
        { am.targetequation(trgvar, "" + am.getElementVariable() + "$x", 
                            targetObjectTemplate);
        }   
      }  
      else if (am.isDirect())
      { Vector possibles = new Vector(); 
        EntityMatching emx = am.isObjectMatch(ems,possibles); 
        if (emx != null)
        { String srceq = ""; 
          String trgeq = ""; 
          String srcdata = am.sourcedataname(srcvar); 
          trgeq = am.targetequation(trgvar,srcdata,targetObjectTemplate);
        }                   
      }   
    } 

    if (postcondition != null) 
    { Expression post = postcondition.addReference(new BasicExpression(trgvar),new Type(realtrg)); 
      targetObjectTemplate.addWhere(post); 
    } 

    String res = "  top relation Map" + srcent + "2" + trgent + "\n"; 
    res = res +  "  { " + auxdomains + " checkonly domain source \n    " + sourceObjectTemplate + ";\n";
           // srcvar + " : " + srcent + " { " + srcbody + " };\n"; 
    res = res +  "    enforce domain target \n    " + targetObjectTemplate + ";\n"; 
          // trgvar + " : " + trgent + " { " + trgbody + " };\n"; 
    res = res +  "    when {\n          ";
 
    boolean previous = false; 
    for (int k = 0; k < whenclauses.size(); k++) 
    { String w = (String) whenclauses.get(k); 
      if ("true".equals(w)) { } 
      else if ("".equals(w)) { } 
      else 
      { if (previous) 
        { res = res + " and\n          " + w; } 
        else  
        { res = res + w; 
          previous = true; 
        }
      }  
    } 
    res = res + " }\n"; 
    res = res +  "  }\n"; 

    sourceTemplate = sourceObjectTemplate; 
    targetTemplate = targetObjectTemplate; 

    return res; 
  } // and when for the objects that are referenced. 


  /* First phase rules for UML-RSDS: */ 

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
    
    Attribute srcpk = realsrc.getPrincipalPK(); 
    if (srcpk != null) 
    { srcid = srcpk.getName(); } 
	
    Attribute trgpk = realtrg.getPrincipalPK(); 
    if (trgpk != null) 
    { trgid = trgpk.getName(); } 

    Expression srcexp = new BasicExpression(srcid);
    String srcval = srcid; 
 
    // If there is an attribute mapping to trgid, use that: 
    AttributeMatching amx = findAttributeMappingByTarget(trgid); 

    if (amx != null) 
    { srcexp = amx.getSourceExpression(); 
      srcval = srcexp + ""; 
    } 

    Vector found = new Vector(); 
    found.add(trgid); 
    String setSuperPKs = superclassKeyAssignments(realtrg,trgvar,found,srcexp,srcval); 

    String res = srcent + "::\n"; 
    if (condition != null) 
    { res = res + "  " + condition + " =>\n  "; } 
    res = res +  "  " + trgent + "->exists( " + trgvar + " | "; 
    res = res + trgvar + "." + trgid + " = " + srcexp + " " + setSuperPKs + " )\n"; 
    res = res +  "\n"; 
    return res; 
  } 

  String umlrsdsrule1InstanceReplication() 
  { if (src == trg) { return ""; } 
  
    Expression repl = (Expression) replicationConditions.get(0); // assume just one at the moment
	
	if (repl instanceof BinaryExpression) { } 
	else 
	{ return ""; }
	
	BinaryExpression repExpr = (BinaryExpression) repl; 
	Expression repVar = repExpr.getLeft();  // should be a BasicExpression of String or Entity type. 

    Type repType = repVar.getType(); 
    System.out.println(">>> Replication constraint in phase 1; condition = " + repExpr + " variable type = " + repType); 

    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    String srcid = srcent.toLowerCase() + "Id"; 

    Attribute srcpk = realsrc.getPrincipalPK(); 
    if (srcpk != null) 
    { srcid = srcpk.getName(); } 
	
	if (repType != null && repType.isEntity())
	{ Entity repEnt = repType.getEntity(); 
	  String repPk = repEnt.getName().toLowerCase() + "Id"; 
      srcid = srcid + " + \"~for~\" + " + repVar + "." + repPk;
	} 
	else if (repType != null)
	{ srcid = srcid + " + \"~for~\" + " + repVar; }
	
	System.out.println(">>> srcid = " + srcid); 
	 
    String srcvar = srcname.toLowerCase() + "x"; 
    String trgvar = trgname.toLowerCase() + "x"; 

    String trgid = trgent.toLowerCase() + "Id"; 
    
	
    Attribute trgpk = realtrg.getPrincipalPK(); 
    if (trgpk != null) 
    { trgid = trgpk.getName(); } 

    Expression srcexp = new BasicExpression(srcid);
    String srcval = srcid; 

	// System.out.println(">>> trgid = " + trgid); 
 
    // If there is an attribute mapping to trgid, use that: 
    AttributeMatching amx = findAttributeMappingByTarget(trgid); 

	// System.out.println(">>> amx = " + amx); 

    if (amx != null) 
    { srcexp = amx.getSourceExpression(); 
      srcval = srcexp + ""; 
    } 

	// System.out.println(">>> srcexp = " + srcexp); 

    String targetbody = ""; 
    String remainder = ""; 

    Vector created = new Vector(); 

    Vector auxattmaps = new Vector(); 
    auxattmaps.addAll(attributeMappings); 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < auxattmaps.size(); i++)
    { AttributeMatching am = (AttributeMatching) auxattmaps.get(i); 
      trgnames.add(am.trg.getName()); 
    } 

    if (amx != null) 
    { Vector amxs = new Vector(); 
      amxs.add(amx); 
      auxattmaps.removeAll(amxs); 
    } 

    Vector ams2 = (Vector) Ocl.sortedBy(auxattmaps, trgnames); 
    // But: ids should always precede non-ids. 

    Vector processed = new Vector(); 

    // First, handle the direct targets: 

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i);
      if (am.isDirectTarget())
      { if (am.isStringAssignment())
        { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
          targetbody = targetbody + " & " + trgeq;
          processed.add(am);  
        } 
        else if (am.isExpressionAssignment())
        { Type srcvaltype = am.srcvalue.getElementType();
          if (srcvaltype != null && srcvaltype.isEntityType()) 
          { String e1Id = srcvaltype.getName().toLowerCase() + "Id";  
            Attribute e1pk = srcvaltype.getEntity().getPrincipalPK(); 
            if (e1pk != null) 
            { e1Id = e1pk.getName(); } 
    
            Expression srcids = null; 
            if (am.srcvalue.isMultipleValued())
            { am.srcvalue.setBrackets(true); 
              srcids = new BinaryExpression("->collect", am.srcvalue, 
                                          new BasicExpression(e1Id)); 
            } 
            else 
            { srcids = new BasicExpression(e1Id); 
            ((BasicExpression) srcids).setObjectRef(am.srcvalue); 
            } 
            String trgeq = trgvar + "." + am.trg + " = " + am.trg.getElementType() + 
                                                   "@pre[" + srcids + "]"; 
            targetbody = targetbody + " & " + trgeq;
            processed.add(am);
          } 
          else 
          { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
            targetbody = targetbody + " & " + trgeq; 
            processed.add(am);
          } 
          updateCreated(am.trg.getNavigation(),created); 
        } 
        else if (am.isValueAssignment())
        { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
          targetbody = targetbody + " & " + trgeq;
          processed.add(am); 
        } 
        else // if (am.isDirectTarget())
        { String srceq = ""; 
          String trgeq = ""; 
          String srcdata = am.sourcedataname(srcvar); 
          Vector bound = new Vector(); 
          bound.add(trgvar); 
          trgeq = am.targetequationUMLRSDS(trgvar,bound); 
          targetbody = targetbody + " & " + trgeq; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am); 
        }
      } 
    } 

    ams2.removeAll(processed);  
    // Only indirect targets are left. Process primary key & 
    // 1-multiplicity references before other forms of target. 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      // Creating an intermediate single-valued reference. 
      // Must be done before any assignment to its features. 

      if (!am.isDirectTarget() && am.isIdentityTarget()) 
      { if (am.isExpressionAssignment())
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        }
        else 
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        } 
      }
    } 

    ams2.removeAll(processed); 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      // Creating an intermediate single-valued reference. 
      // Must be done before any assignment to its features. 

      if (!am.isDirectTarget() && am.is1MultiplicityTarget()) 
      { if (am.isExpressionAssignment())
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        }
        else 
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        } 
      }
    } 

    ams2.removeAll(processed); 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      if (!am.isDirectTarget() && am.isExpressionAssignment() && !am.is1MultiplicityTarget())
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
        remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
        updateCreated(am.trg.getNavigation(),created); 
      } 
      else if (!am.isDirectTarget() && am.isDirect() && !am.is1MultiplicityTarget()) // target is composite, source is not
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
        remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
        updateCreated(am.trg.getNavigation(),created); 
      } 
    } // But amalgamate the equations where there is a common target path
    
    Vector found = new Vector(); 
    found.add(trgid); 
    String setSuperPKs = superclassKeyAssignments(realtrg,trgvar,found,srcexp,srcval); 

    String res = srcent + "::\n"; 
    if (repExpr != null) 
    { res = res + "  " + repExpr + " =>\n  "; } 
    res = res +  "  " + trgent + "->exists( " + trgvar + " | "; 
    res = res + trgvar + "." + trgid + " = " + srcexp + " " + setSuperPKs + targetbody + " )\n"; 
    res = res +  "\n\n" + remainder; 
    return res; 
  } 

  private String superclassKeyAssignments(Entity etarg, String trgvar, Vector found, Expression srcexp, String srcval)
  { if (etarg == null) { return ""; } 
   
    Entity sup = etarg.getSuperclass(); 
    if (sup == null) { return ""; } 

    Attribute pk = sup.getPrincipalPrimaryKey(); 
    if (pk == null) { return ""; } 

    String supkey = pk.getName(); 
    if (found.contains(supkey)) { }
    else 
    { found.add(supkey); 
      String res = ""; 
      if (srcexp.isNumeric() && pk.isString())
      { res = " & " + trgvar + "." + supkey + " = " + srcexp + "\"\""; } 
      else // assume both String
      { res = " & " + trgvar + "." + supkey + " = " + srcexp; } 
 
      return res + superclassKeyAssignments(sup,trgvar,found,srcexp,srcval);
    } 
    return superclassKeyAssignments(sup,trgvar,found,srcexp,srcval);  
  } 

  String umlrsdsrule2(Vector ems) 
  { // if (src == trg) { return ""; } 

    System.out.println(">>> Creating UML-RSDS for rule " + this); 

    Vector auxattmaps = new Vector(); 
    auxattmaps.addAll(attributeMappings); 

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

    Attribute srcpk = realsrc.getPrincipalPK(); 
    if (srcpk != null) 
    { srcid = srcpk.getName(); } 

    Attribute trgpk = realtrg.getPrincipalPK(); 
    if (trgpk != null) 
    { trgid = trgpk.getName(); } 

    String srcexp = srcid; 
    // If there is an attribute mapping to trgid, use that: 
    AttributeMatching amx = findAttributeMappingByTarget(trgid); 

    if (amx != null) 
    { srcexp = amx.getSourceExpression() + ""; 
      Vector amxs = new Vector(); 
      amxs.add(amx); 
      auxattmaps.removeAll(amxs); 
    } 

    String targetbody = ""; 
    String remainder = ""; 

    Vector created = new Vector(); 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < auxattmaps.size(); i++)
    { AttributeMatching am = (AttributeMatching) auxattmaps.get(i); 
      trgnames.add(am.trg.getName()); 
    } 

    Vector ams2 = (Vector) Ocl.sortedBy(auxattmaps, trgnames); 
    // But: ids should always precede non-ids. 

    Vector processed = new Vector(); 

    // First, handle the direct targets: 

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i);

      System.out.println(">>> Mapping " + am + " to UML-RSDS"); 

      if (am.isDirectTarget())
      { if (am.isStringAssignment())
        { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
          targetbody = targetbody + " & " + trgeq;
          processed.add(am);  
        } 
        else if (am.isExpressionAssignment())
        { Type srcvaltype = am.srcvalue.getElementType();
          if (srcvaltype != null && srcvaltype.isEntityType()) 
          { String e1Id = srcvaltype.getName().toLowerCase() + "Id";  
            Attribute e1pk = srcvaltype.getEntity().getPrincipalPK(); 
            if (e1pk != null) 
            { e1Id = e1pk.getName(); } 
    
            Expression srcids = null; 
            if (am.srcvalue.isMultipleValued())
            { am.srcvalue.setBrackets(true); 
              srcids = new BinaryExpression("->collect", am.srcvalue, 
                                          new BasicExpression(e1Id)); 
            } 
            else 
            { srcids = new BasicExpression(e1Id); 
            ((BasicExpression) srcids).setObjectRef(am.srcvalue); 
            } 
            String trgeq = trgvar + "." + am.trg + " = " + am.trg.getElementType() + 
                                                   "@pre[" + srcids + "]"; 
            targetbody = targetbody + " & " + trgeq;
            processed.add(am);
          } 
          else 
          { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
            targetbody = targetbody + " & " + trgeq; 
            processed.add(am);
          } 
          updateCreated(am.trg.getNavigation(),created); 
        } 
        else if (am.isValueAssignment())
        { String trgeq = trgvar + "." + am.trg + " = " + am.srcvalue; 
          targetbody = targetbody + " & " + trgeq;
          processed.add(am); 
        } 
        else // if (am.isDirectTarget())
        { String srceq = ""; 
          String trgeq = ""; 
          String srcdata = am.sourcedataname(srcvar); 
          Vector bound = new Vector(); 
          bound.add(trgvar); 
          trgeq = am.targetequationUMLRSDS(trgvar,bound); 
          targetbody = targetbody + " & " + trgeq; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am); 
        }
      } 
    } 

    System.out.println(">>> After all direct mappings: " + targetbody); 


    ams2.removeAll(processed);  
    // Only indirect targets are left. Process primary key & 
    // 1-multiplicity references before other forms of target. 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      // Creating an intermediate single-valued reference. 
      // Must be done before any assignment to its features. 

      if (!am.isDirectTarget() && am.isIdentityTarget()) 
      { if (am.isExpressionAssignment())
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        }
        else 
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        } 
      }
    } 

    ams2.removeAll(processed); 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      // Creating an intermediate single-valued reference. 
      // Must be done before any assignment to its features. 

      if (!am.isDirectTarget() && am.is1MultiplicityTarget()) 
      { if (am.isExpressionAssignment())
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        }
        else 
        { remainder = remainder + srcent + "::\n"; 
          remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
          remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
          updateCreated(am.trg.getNavigation(),created);
          processed.add(am);  
        } 
      }
    } 

    ams2.removeAll(processed); 

    for (int j = 0; j < ams2.size(); j++) 
    { AttributeMatching am = (AttributeMatching) ams2.get(j); 
      String trgvar1 = trgent.toLowerCase() + "$" + j; 

      if (!am.isDirectTarget() && am.isExpressionAssignment() && !am.is1MultiplicityTarget())
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
        remainder = remainder + "  " + 
                        am.composedTargetEquationExpr(realsrc,am.srcvalue,trgvar1,created) + "\n\n"; 
        updateCreated(am.trg.getNavigation(),created); 
      } 
      else if (!am.isDirectTarget() && am.isDirect() && !am.is1MultiplicityTarget()) // target is composite, source is not
      { remainder = remainder + srcent + "::\n"; 
        remainder = remainder + "  " + trgvar1 + " = " + trgent + "@pre[" + srcexp + "] "; 
        remainder = remainder + "  " + am.composedTargetEquation(realsrc,trgvar1,created) + "\n\n"; 
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
      res = res + trgvar + "." + trgid + " = " + srcexp + targetbody + " )\n";
    }  
    res = res +  "\n\n" + remainder; 

    String trgvar2 = trgent.toLowerCase() + "$xx"; 

    if (postcondition != null) 
    { Expression post = postcondition.addReference(new BasicExpression(trgvar2),new Type(realtrg)); 
      String postconstraint = srcent + "::\n"; 
      postconstraint = postconstraint + "  " + trgvar2 + " = " + trgent + "@pre[" + srcexp + "] "; 
      postconstraint = postconstraint + "  => (" + post + ")\n\n"; 
      res = res + "\n\n" + postconstraint;
    } 

    System.out.println(">>> Overall 2nd phase constraint is: " + res); 

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

    return "  src.objects()[" + srcent + "]->map " + srcent + "2" + trgent + "();\n"; 
  } 

  public String qvtomain2()
  { if (attributeMappings.size() == 0) { return ""; }
 
    String srcent = srcname.substring(0,srcname.length()-1); 
    String trgent = trgname.substring(0,trgname.length()-1); 

    if (realsrc != null)
    { srcent = realsrc.getName(); } 
    if (realtrg != null) 
    { trgent = realtrg.getName(); } 

    return "  src.objects()[" + srcent + "]->map map" + srcent + "2" + trgent + "();\n"; 
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
    Vector subclasses = realsrc.getSubclasses();
    int subcount = 0; 
    

    if (subclasses.size() == 0) 
    { return ""; } // no need for it. 
 
    for (int i = 0; i < subclasses.size(); i++) 
    { Entity sub = (Entity) subclasses.get(i); 
      String subname = sub.getName(); 
      String srcsub = subname; 
      if (subname.endsWith("$"))
      { srcsub = subname.substring(0,subname.length()-1); } 
      
      Vector submatches = ModelMatching.findSpecialisedMatchingsBySourceTarget(sub,realtrg,ems); 
      for (int j = 0; j < submatches.size(); j++)  
      { EntityMatching submatch = (EntityMatching) submatches.get(j); 
        Entity tsub = submatch.realtrg; 
        String tsubname = tsub.getName(); 
        String trgsub = tsubname; 
        srcsub = submatch.realsrc.getName(); 
        // if (tsubname.endsWith("$"))
        // { trgsub = tsubname.substring(0,tsubname.length()-1); } 
      
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

    BasicExpression tvar = new BasicExpression("result"); 
    tvar.setType(new Type(realtrg)); 
    tvar.setElementType(new Type(realtrg)); 
       
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
        if (am.isDirectTarget())
        { targetbody = targetbody + "\n  " + 
                       "result." + am.trg + " := " + val + ";";
        } 
        else 
        { String trgeq = am.targetequationQVTO(tvar,val,ems,created); 
          updateCreated(am.trg.getNavigation(), created); 
          targetbody = targetbody + "\n  " + trgeq; 
        }   
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression srcinst = am.srcvalue.addReference(new BasicExpression("self"),
                                                      new Type(realsrc));
        Type srctype = am.srcvalue.getType(); 
		Type trgtype = am.trg.getType(); 
		
        String trgeq = ""; 
        if (srctype != null && srctype.isValueType())
        { trgeq = "result." + am.trg + " := " + srcinst + ";"; } 
        else if (srctype != null && srctype.isEntity() && trgtype != null && trgtype.isEntity())
        { Entity s = srctype.getEntity(); 
		  Entity tent = trgtype.getEntity(); 
		  EntityMatching emx = ModelMatching.findEntityMatchingFor(s,tent,ems);
          // System.out.println("Entity matching for " + s + " is " + em); 

          if (emx != null) 
          { trgeq = "result." + am.trg + " := (" + srcinst +  
                 ").resolveoneIn(" + emx.realsrc + "::" + 
                 emx.realsrc + "2" + emx.realtrg + "," + tent.getName() + ")";
          } 
		  else 
		  { trgeq = "result." + am.trg + " := (" + srcinst + ").resolve();"; }
		}
        else if (srctype != null && Type.isEntityCollection(srctype))
        { Type selt = srctype.getElementType(); 
		
		  Entity s = selt.getEntity(); 
		  Entity tent = trgtype.getEntityOrElementEntity(); 
		  EntityMatching emx = ModelMatching.findEntityMatchingFor(s,tent,ems);
		  
		  if (emx != null)
          { trgeq = "result." + am.trg + " := (" + srcinst +   
                 ").resolveIn(" + emx.realsrc + "::" + 
                 emx.realsrc + "2" + emx.realtrg + "," + tent.getName() + ")";
          } 
		  else 
		  { trgeq = "result." + am.trg + " := (" + srcinst + ").resolve();"; }
        } 
		else 
        { trgeq = "result." + am.trg + " := (" + srcinst + ").resolve();"; } 
 
        updateCreated(am.trg.getNavigation(), created); 
        targetbody = targetbody + "\n  " + trgeq; 
      }
      else if (am.isValueAssignment())
      { Expression sval = am.srcvalue.addReference(new BasicExpression("self"), 
                                                   new Type(realsrc));
        if (am.isDirectTarget())
        { targetbody = targetbody + "\n  " + 
                       "result." + am.trg + " := " + sval + ";";
        } 
        else 
        { String trgeq = am.targetequationQVTO(tvar,sval,ems,created); 
          updateCreated(am.trg.getNavigation(), created); 
          targetbody = targetbody + "\n  " + trgeq; 
        }   
      }
      else if (am.isDirect())
      { String srceq = ""; 
        String trgeq = ""; 
        // String srcdata = am.sourcedataname(srcvar); 
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

  String atlrule(Vector ems, Vector secondaryRules) 
  { // if (src == trg) { return ""; } 
    // Only produce rules for primary rules. Secondary rules are put into 
    // primary rule as a new OutPattern. 

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

      if (am.isStringAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        trgeq = trgeq + am.trg + " <- " + newval; 
        targetbody.add(trgeq); 
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        if ("self".equals(am.srcvalue + ""))
        { Type tet = am.trg.getElementType(); 
          if (tet != null && tet.isEntityType())
          { Entity tent = tet.getEntity(); 
            String tvar = tent.getName().toLowerCase() + "_x"; 
            EntityMatching em = ModelMatching.findEntityMatchingFor(realsrc,tent,ems);
            if (em != null && em.isSecondary()) 
            { tvar = em.trg.getName().toLowerCase() + "_x"; } 
            else  
            { tvar = "thisModule.resolveTemp(" + srcvar + ", '" + tvar + "')"; } 
            newval = new BasicExpression(tvar); 
          }
        }  
        
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
      { Expression sval = am.srcvalue.addReference(new BasicExpression(srcvar), 
                                                   new Type(realsrc));
        trgeq = trgeq + am.trg + " <- " + sval; 
        targetbody.add(trgeq); 
      } 
      else if (am.isDirectTarget())
      { trgeq = trgeq + am.atldirecttarget(srcvar,ems);  
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
    { res = res + " ( " + condition.addReference(new BasicExpression(srcvar), 
                                                 new Type(realsrc)) + " )\n"; }
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

    for (int i = 0; i < secondaryRules.size(); i++) 
    { EntityMatching emsub = (EntityMatching) secondaryRules.get(i); 
      emsub.atlruleSubordinate(ems,allnewrules,allnewclauses,allnewdo); 
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
  } 


  String atlruleSubordinate(Vector ems, Vector allnewrules, 
                            Vector allnewclauses, Vector allnewdo) 
  { // if (src == trg) { return ""; } 
    // Secondary rules are put into 
    // primary rule as a new OutPatternElement. 

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

    Attribute trgatt = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 
    OutPatternElement ope = new OutPatternElement(trgatt); 
    allnewclauses.add(ope);  // primary clause
        
    java.util.Map implementedBy = new java.util.HashMap(); 
    // for each path in created, either an ATL OutPatternElement or an Attribute

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 
      String trgeq = "  "; 

      if (am.isStringAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        // trgeq = trgeq + am.trg + " <- " + newval; 
        // targetbody.add(trgeq); 
        Binding bres2 = new Binding(am.trg + "", newval);
        ope.addBinding(bres2);  
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        
        // trgeq = trgeq + am.trg + " <- " + newval; 
        // targetbody.add(trgeq);
        Binding bres2 = new Binding(am.trg + "", newval);
        ope.addBinding(bres2);  
        Vector cpath = am.trg.getNavigation();
        if (cpath.size() == 0) 
        { cpath.add(am.trg); }    
        updateCreated(cpath,created);
        Attribute cpathatt = new Attribute(cpath);  
        implementedBy.put(cpathatt.getName(), am.trg); 
      } 
      else if (am.isValueAssignment() && am.isDirectTarget())
      { Expression sval = am.srcvalue.addReference(new BasicExpression(srcvar), 
                                                   new Type(realsrc));
        // trgeq = trgeq + am.trg + " <- " + sval; 
        Binding bres2 = new Binding(am.trg + "", sval);
        ope.addBinding(bres2);  
        // targetbody.add(trgeq); 
      } 
      else if (am.isDirectTarget())
      { // trgeq = trgeq + am.atldirecttarget(srcvar,ems);  
        // targetbody.add(trgeq); 

        Binding bres2 = am.atldirectbinding(srcvar,ems);
        ope.addBinding(bres2);  
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
        { // trgeq = trgeq + cbind; 
          // targetbody.add(trgeq); 
          ope.addBinding(cbind);  
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
    { res = res + " ( " + condition.addReference(new BasicExpression(srcvar), 
                                                 new Type(realsrc)) + " )\n"; }
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
  } 


  TransformationRule etlrule(Vector ems, Vector auxrules) 
  { // if (src == trg) { return ""; } 

    EntityMatching extending = null; 
    if (realsrc != null && realsrc.getSuperclass() != null) 
    { EntityMatching supermatch = 
        ModelMatching.findSuperclassMatchingBySourceTarget(realsrc.getSuperclass(),realtrg,ems); 
      if (supermatch != null) 
      { extending = supermatch; 
        // System.out.println(">> Super-rule of " + getName() + " is " + extending.getName()); 
      } 
    } 

    Vector allnewrules = new Vector(); // lazy rules or operations
    Vector allnewclauses = new Vector(); // additional outputs for this rule
    Vector allnewdo = new Vector(); // additional code

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

    Attribute trgatt = new Attribute(trgvar, new Type(realtrg), ModelElement.INTERNAL); 

    Vector targetbody = new Vector(); 
    String remainder = ""; 

    Vector created = new Vector(); 

    Vector trgnames = new Vector(); 
    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
      trgnames.add(am.trg.getName()); 
    } 
    Vector ams2 = (Vector) Ocl.sortedBy(attributeMappings, trgnames); 
    // remove all matchings in extending. 
    if (extending != null) 
    { ams2.removeAll(extending.attributeMappings); } 

    java.util.Map implementedBy = new java.util.HashMap(); 
    // for each path in created, either an ATL OutPatternElement or an Attribute

    for (int i = 0; i < ams2.size(); i++)
    { AttributeMatching am = (AttributeMatching) ams2.get(i); 
      String trgeq = "  "; 
      Statement assign; 
      BasicExpression directtarget = new BasicExpression(am.trg); 
      directtarget.setObjectRef(new BasicExpression(trgatt)); 

      if (am.isStringAssignment())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        // trgeq = trgeq + am.trg + " <- " + newval; 
        assign = new AssignStatement(directtarget, newval);  
        targetbody.add(assign); 
      } 
      else if (am.isExpressionAssignment() && am.isDirectTarget())
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        newval.setType(am.srcvalue.getType());
        newval.setElementType(am.srcvalue.getElementType());  
        
        Expression targetval = newval.etlEquivalent(am.trg,ems); 
        
        // trgeq = trgeq + am.trg + " <- " + newval; 

        assign = new AssignStatement(directtarget, targetval); 
        targetbody.add(assign);

        Vector cpath = am.trg.getNavigation();
        if (cpath.size() == 0) 
        { cpath.add(am.trg); }    
        updateCreated(cpath,created);
        Attribute cpathatt = new Attribute(cpath);  
        implementedBy.put(cpathatt.getName(), am.trg); 
      } 
      else if (am.isValueAssignment() && am.isDirectTarget())
      { // trgeq = trgeq + am.trg + " <- " + am.srcvalue; 
        Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        newval.setType(am.srcvalue.getType());
        newval.setElementType(am.srcvalue.getElementType());  
        
        assign = new AssignStatement(directtarget, newval); 
        targetbody.add(assign);
      } 
      else if (am.isDirectTarget())
      { // trgeq = trgeq + am.atldirecttarget(srcvar,ems);
        assign = am.etldirecttarget(srcvar, directtarget,ems);   
        targetbody.add(assign);
 
        Vector cpath = am.trg.getNavigation();   
        if (cpath.size() == 0) 
        { cpath.add(am.trg); }    
        updateCreated(cpath,created); 
        Attribute cpathatt = new Attribute(cpath);  
        implementedBy.put(cpathatt.getName(), am.trg); 
      }
      else if (am.isExpressionAssignment()) // composed target
      { Expression newval = am.srcvalue.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
        newval.setType(am.srcvalue.getType());
        newval.setElementType(am.srcvalue.getElementType());  
        
        // System.out.println(">> Instantiated source expression is: " + newval); 
        // Expression targetval = newval.etlEquivalent(am.trg,ems); 
        
        // trgeq = trgeq + am.trg + " <- " + newval; 

        // assign = new AssignStatement(directtarget, targetval); 
        // targetbody.add(assign);

        Vector newclauses = new Vector(); // of OutPatternElement
        Vector newrules = new Vector();   // of MatchedRule
        Vector newdo = new Vector();      // of Statement
        Statement cbind = 
                  am.etlcomposedtarget(newclauses,newrules,newdo,srcvar,trgvar,
                                       realsrc,created,implementedBy,ems); 
        if (cbind != null) 
        { targetbody.add(cbind); } 
        allnewrules.addAll(newrules); 
        allnewclauses.addAll(newclauses);
        targetbody.addAll(newdo);  
        
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
        Vector newdo = new Vector();      // of Statement
        Statement cbind = 
                  am.etlcomposedtarget(newclauses,newrules,newdo,srcvar,trgvar,
                                       realsrc,created,implementedBy,ems); 
        if (cbind != null) 
        { targetbody.add(cbind); } 
        allnewrules.addAll(newrules); 
        allnewclauses.addAll(newclauses);
        targetbody.addAll(newdo);  
        updateCreated(am.trg.getNavigation(),created); 
      }  

      // else case of composed target
    } 

    TransformationRule res = new TransformationRule(srcent + "2" + trgent,false,false);
    Attribute srcatt = new Attribute(srcvar, new Type(realsrc), ModelElement.INTERNAL); 
    res.setSource(srcatt);  

    if (realsrc != null && realsrc.isAbstract())
    { res.setAbstract(true); } 
    
    if (extending != null) 
    { res.setExtends(new TransformationRule(extending)); } 

    if (condition != null) 
    { Expression cond = condition.addReference(new BasicExpression(srcvar),
                                                     new Type(realsrc)); 
      res.setGuard(cond);
    }
    
    res.addTarget(trgatt); 
 

    for (int i = 0; i < allnewclauses.size(); i++) 
    { OutPatternElement newclause = (OutPatternElement) allnewclauses.get(i); 
      res.addClause(newclause); 
    } 
    res.addBody(new SequenceStatement(targetbody)); 

    auxrules.addAll(allnewrules); 

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
  
  public void checkModelConditions(ModelSpecification mod, ModelMatching mm, Vector ems, Vector rem, Vector queries)
  { // For objects  restrictedsources |-> trgobjects 
    // checks that the condition of rule is valid, or if
    // no condition, creates a valid condition. 

    String srcname = realsrc.getName(); 
    String trgname = realtrg.getName(); 
	
    Vector srcobjects = mod.getObjects(srcname);
    Vector restrictedsources = new Vector();  
    Vector trgobjects = mod.getCorrespondingObjects(srcobjects,restrictedsources,trgname); 
	
	// If there is a condition Cond, check that all restrictedsources actually satisfy the condition and no other source 
	// fails the condition.
	
	Vector othersources = new Vector(); 
	othersources.addAll(srcobjects);  // the extent of realsrc in mod
	othersources.removeAll(restrictedsources);  // elements of realsrc which do not map to trgname

    // pairs x : restrictedsources and corresponding y : trgobjects in the corresponding targets via trgname
	// are the extent of rule(s)  srcname |--> trgname.  There may be several such rules but we treat them the same here. 
	
	Vector otherrules = ModelMatching.getEntityMatchings(realsrc,ems); 
	Vector thislist = new Vector(); 
	thislist.add(this); 
	otherrules.removeAll(thislist); 
	
	System.out.println(">> Other rules from " + srcname + " are: " + otherrules); 
	System.out.println(); 
	
     boolean conditionError = false; 
	
     if (condition != null) 
     { boolean condAreMapped = mod.checkConditionInModel(condition,restrictedsources); 
       if (condAreMapped) { }
       else 
       { System.err.println("!! Some instances of " + restrictedsources + " are mapped to " + trgname + " but fail the " + 
                            srcname + " |--> " + trgname + " mapping condition " + condition); 
	    // But they may be mapped by another rule  srcname |--> trgname with a different condition
        if (otherrules.size() > 0) 
        { System.out.println(">> There are " + otherrules.size() + " other rule(s) from " + srcname); }
          conditionError = true; 
        } 
	  
        Expression notcond = Expression.negate(condition); 
        boolean othersAreNotMapped = mod.checkConditionInModel(notcond,othersources); 

        if (othersAreNotMapped) 
        { System.out.println(">>> " + condition + " is a valid condition for rule " + this);
          System.out.println(">>> adding its negation to other rules from " + srcname); 
          addToConditionIfNull(notcond,otherrules); 
		  conditionError = false; 
        }
        else 
        { System.err.println("!! Some instances of " + othersources + " are not mapped to " + trgname + " but satisfy the " + 
                             srcname + " |--> " + trgname + " mapping condition " + condition);
          conditionError = true; 
	    } 
      
	
        if (conditionError)
        { String yn = 
            JOptionPane.showInputDialog("Remove invalid condition " + condition + " of mapping " + 
		                                srcname + " |--> " + trgname + " (y or n)?:");
 
          if (yn != null && yn.equals("y"))
          { condition = null; }  
        }          
	  }
	  
	  
      if (srcobjects.size() > 0 && 
          trgobjects.size() == 0 && 
          (condition == null || "true".equals(condition + "")))
      { System.out.println(">>>> No source " + srcname + " object has a corresponding " + trgname);  
        System.out.println(">>>> object: suggest to delete the entity mapping " + srcname + " |--> " + trgname);
        System.out.println();
        condition = new BasicExpression(false);   
        rem.add(this); 
      } 
      else if (trgobjects.size() < srcobjects.size() && 
                (condition == null || "true".equals(condition + "")))
      { System.out.println(">>>> Not every source " + srcname + " object has a corresponding " + trgname);  
        System.out.println("object: a condition is needed in the entity mapping " + srcname + " |--> " + trgname);
        System.out.println();  

        System.out.println(">>>> Mapped source objects are: " + restrictedsources);  
        System.out.println(">>>> Non-mapped source objects are: " + othersources);  
	  
        Vector allatts = realsrc.allDefinedAttributes();
        Vector allrefs = realsrc.getLocalReferenceFeatures(); 
        allatts.addAll(allrefs); 
	   
        for (int i = 0; i < allatts.size(); i++)
        { Attribute att = (Attribute) allatts.get(i); 
          String attnme = att.getName(); 
		  System.out.println(">>> Identifying tests based on attribute " + att); 
            
          if (att.isBoolean())
          { System.out.println(">>> Possible source boolean condition: " + attnme + " or not(" + attnme + ")"); 
            Expression possibleCond = new BasicExpression(att);
            Expression negationCond = new UnaryExpression("not", possibleCond);
            negationCond.setType(att.getType());  
	         
            boolean condValid = mod.checkConditionInModel(possibleCond,restrictedsources);
            boolean notcondValid = mod.checkConditionInModel(negationCond,othersources);
            if (condValid && notcondValid)
            { System.out.println(">>> Condition " + att + " is valid on all mapped sources & false on all others. Adding to mapping"); 
              addCondition(possibleCond);
              addCondition(negationCond,otherrules);  
              System.out.println(); 
            }
            else if (condValid)
            { addCondition(possibleCond); } 
            else 
            { condValid = mod.checkConditionInModel(negationCond,restrictedsources);
              notcondValid = mod.checkConditionInModel(possibleCond,othersources);
              if (condValid && notcondValid)
              { System.out.println(">>> Condition not(" + att + ") is valid on all mapped sources & false on others. Adding to mapping"); 
	           addCondition(negationCond); 
	           addCondition(possibleCond,otherrules);
                System.out.println();   
              }
	         else if (condValid)
              { addCondition(negationCond); }
           }
        }
        else if (att.isEnumeration())	  
        { Type enumt = att.getType();
 
          if (enumt != null) 
          { Vector enumvals = enumt.getValues(); 
            boolean found = false; 
            Vector passedAttValues = ObjectSpecification.getAllValuesOf(att,restrictedsources,mod); 
            System.out.println(">>> Values of " + att + " on source objects are: " + passedAttValues); 

            if (passedAttValues.containsAll(enumvals)) { } 
            else 
            { Expression expr = Expression.disjoinCases(att,passedAttValues); 
              System.out.println(">> Identified discriminator condition for " + att + ": " + expr);
              if (expr != null) 
              { expr.setBrackets(true); 
                addCondition(expr);
                System.out.println();
              }  
            }
          }
        }
        else if (att.isString())
        { /* System.out.println(">>> Possible source string condition is: " + att + " = \"" + trgname + "\"");
          // No, take the value of one of the restrictedsources.att
          BasicExpression strexpr = new BasicExpression("\"" + trgname + "\""); 
          strexpr.setType(new Type("String", null)); 
          strexpr.setElementType(new Type("String", null)); 
		  
          Expression possibleCond = new BinaryExpression("=", new BasicExpression(att), strexpr);
          Expression negCond = new BinaryExpression("/=", new BasicExpression(att), strexpr); 
          possibleCond.setType(new Type("boolean", null)); 
          negCond.setType(new Type("boolean", null));
          boolean condValid = mod.checkConditionInModel(possibleCond,restrictedsources);
          boolean notcondValid = mod.checkConditionInModel(negCond,othersources);
		  
          if (condValid && notcondValid)
          { System.out.println(">>> Condition " + att + " = \"" + trgname + "\" is valid on source instances & false on others. Adding to mapping"); 
            addCondition(possibleCond);
            System.out.println();  
          } 
          else // check that all the sources have a constant value for att. 
		  */
		  
          if (mod.isConstant(att,restrictedsources))
          { System.out.println(">>> Attribute " + att + " is constant on these source objects");
            String val = ((ObjectSpecification) restrictedsources.get(0)).getString(attnme); 
	        BasicExpression valbe = new BasicExpression("\"" + val + "\""); 
			valbe.setType(new Type("String", null)); 
			valbe.setElementType(new Type("String", null));
			valbe.setUmlKind(Expression.VALUE); 
			
            Expression possibleCond = new BinaryExpression("=", new BasicExpression(att), valbe); 
            // negCond = new BinaryExpression("/=", new BasicExpression(att), valbe);
            boolean condValid = mod.checkConditionInModel(possibleCond,restrictedsources);
            if (condValid)
            { addCondition(possibleCond); 
              System.out.println(">> Adding the condition " + possibleCond); 
              System.out.println(); 
            } 
          }
          else 
          { Vector attvalues = ObjectSpecification.getStringValues(att,restrictedsources,mod); 
            String csuff = AuxMath.longestCommonSuffix(attvalues); 
            if (csuff != null && csuff.length() > 0) 
            { System.out.println(">>> The source objects have a common " + attnme + " suffix = " + csuff); 
              Expression suffexpr = new BasicExpression("\"" + csuff + "\""); 
              suffexpr.setType(new Type("String", null)); 
              suffexpr.setElementType(new Type("String", null)); 
              suffexpr.setUmlKind(Expression.VALUE); 

              Expression possibleCond = new BinaryExpression("->hasSuffix", new BasicExpression(att), suffexpr); 
              // Expression negCond = new UnaryExpression("not", possibleCond);
              boolean condValid = mod.checkConditionInModel(possibleCond,restrictedsources);
              if (condValid)
              { possibleCond.setType(new Type("boolean", null));
                addCondition(possibleCond);   
                System.out.println(">> Adding the condition " + possibleCond); 
                System.out.println(); 
              }
            } 
            else 
            { String cpref = AuxMath.longestCommonPrefix(attvalues); 
              if (cpref != null && cpref.length() > 0) 
              { System.out.println(">>> The source objects have a common " + attnme + " prefix = " + cpref); 
                Expression prefexpr = new BasicExpression("\"" + cpref + "\""); 
                prefexpr.setType(new Type("String", null)); 
                prefexpr.setElementType(new Type("String", null)); 
                prefexpr.setUmlKind(Expression.VALUE); 
                Expression possibleCond = new BinaryExpression("->hasPrefix", new BasicExpression(att), prefexpr); 
                 // Expression negCond = new UnaryExpression("not", possibleCond);
                 boolean condValid = mod.checkConditionInModel(possibleCond,restrictedsources);
                 if (condValid)
                 { possibleCond.setType(new Type("boolean", null));
                   addCondition(possibleCond);   
                   System.out.println(">> Adding the condition " + possibleCond); 
                   System.out.println(); 
                 }
               } 
             } 
           } 
         } // other conditions: att->hasSuffix(ss) if the values have a common suffix, different from any suffix of other 
		  // sets, or ->hasPrefix, or ->size, etc. Conditions for numbers being all equal. 
       else if (att.isEntity())
       { Type enttype = att.getType(); 
         Entity attent = enttype.getEntity(); 
         if (attent != null && attent.isAbstract())
         { // try att->oclIsTypeOf(sub) each subclass of attent 
           Vector esubs = attent.getActualLeafSubclasses(); 
			/* for (int h = 0; h < esubs.size(); h++) 
			{ Entity esub = (Entity) esubs.get(h); 
			  String subname = esub.getName(); 
 		      BinaryExpression possCond = 
			    new BinaryExpression("->oclIsTypeOf", new BasicExpression(att), new BasicExpression(subname)); 
			  boolean cValid = mod.checkConditionInModel(possCond,restrictedsources);
              if (cValid)
			  { System.out.println(">> Condition " + att + "->oclIsTypeOf(" + subname + ") is valid on " + restrictedsources); 
			    addCondition(possCond); 
			  }
			} */ 
              
           Vector classesOfSources = mod.getClassNames(restrictedsources,att);
           if (classesOfSources.size() < esubs.size())
           { System.out.println(">>> Only subclasses " + classesOfSources + " of " + attent + " are permitted for " + att + 
			                     " for this mapping"); 
             Expression membershipCond = Expression.classMembershipPredicate(att,classesOfSources);
			 membershipCond.setBrackets(true);  
             addCondition(membershipCond); 	
             System.out.println(">> Adding the condition " + membershipCond); 
             System.out.println(); 
           } 
         } // and ->oclIsKindOf for abstract subclasses. 
       }
       else if (att.isCollection())
       { Expression possCond = new UnaryExpression("->isEmpty", new BasicExpression(att)); 
         possCond.setType(new Type("boolean", null)); 
		  
         System.out.println(">>> Possible source condition is: " + possCond);
		  
         boolean cvalid = mod.checkConditionInModel(possCond,restrictedsources); 
         if (cvalid) 
         { System.out.println(">>> Condition " + att + "->isEmpty() is valid. Adding to mapping"); 
           addCondition(possCond); 
         }

         Expression uepossCond3 = new UnaryExpression("->size", new BasicExpression(att));
         Expression bcpossCond3 = new BasicExpression(1); 
           
         if (att.isMany())
         { Expression uepossCond4 = new UnaryExpression("->size", new BasicExpression(att));
           Expression bcpossCond4 = new BasicExpression(1); 
           Expression possCond4 = new BinaryExpression(">", uepossCond3, bcpossCond3);  
           possCond4.setType(new Type("boolean", null)); 
           System.out.println(">>> Possible source condition is: " + possCond4);
		  
           boolean cvalid4 = mod.checkConditionInModel(possCond4,restrictedsources); 
           if (cvalid4) 
           { System.out.println(">>> Condition " + att + "->size() > 1  is valid. Adding to mapping"); 
		     possCond4.setBrackets(true); 
             addCondition(possCond4); 
             System.out.println(); 
           }
         }

         Expression possCond2 = new UnaryExpression("->notEmpty", new BasicExpression(att)); 
         possCond2.setType(new Type("boolean", null)); 
         System.out.println(">>> Possible source condition is: " + possCond2);
		  
         boolean cvalid2 = mod.checkConditionInModel(possCond2,restrictedsources); 
         if (cvalid2) 
         { System.out.println(">>> Condition " + att + "->notEmpty() is valid. Adding to mapping"); 
           addCondition(possCond2); 
         }

         if (att.isMany())
		 { Expression possCond3 = new BinaryExpression("=", uepossCond3, bcpossCond3);  
           possCond3.setType(new Type("boolean", null)); 
           System.out.println(">>> Possible source condition is: " + possCond3);
		  
           boolean cvalid3 = mod.checkConditionInModel(possCond3,restrictedsources); 
           if (cvalid3) 
           { System.out.println(">>> Condition " + att + "->size() = 1  is valid. Adding to mapping"); 
		     possCond3.setBrackets(true); 
             addCondition(possCond3); 
             System.out.println(); 
            }
		  }
        }
	  }   
    } 
    System.out.println(); 
     // and class specialisations if realsrc is abstract
  } 	

      /*   
              for (int j = 0; j < enumvals.size() && !found; j++) 
            { String enumval = (String) enumvals.get(j); 
              System.out.println(">>> Possible source enum condition is: " + att + " = " + enumval);
              BasicExpression enumValbe = new BasicExpression(enumval); // no quotes?? 
              enumValbe.setType(enumt);  
              Expression possCond = new BinaryExpression("=", new BasicExpression(att), enumValbe); 
              Expression negCond = new BinaryExpression("/=", new BasicExpression(att), enumValbe); 
              boolean condValid = mod.checkConditionInModel(possCond,restrictedsources);
              boolean condnotValid = mod.checkConditionInModel(negCond,othersources);
              if (condValid && condnotValid)
              { System.out.println(">>> Condition " + att + " = " + enumval + " is valid on all sources & false on others. Adding to mapping"); 
                addCondition(possCond);
                System.out.println(); 
                found = true;  
              }
            } 
            
            if (!found)
            { Vector passed = new Vector(); 
              Vector rejected = new Vector(); 
              if (mod.disjointValueSetsOfObjects(att,restrictedsources,othersources,passed,rejected))
              { Expression expr = Expression.disjoinCases(att,passed); 
                System.out.println(">> Identified discriminator condition: " + att + " : " + passed + " ie: " + expr);
                addCondition(expr);
                System.out.println();  
              }
            }
          }
        }  */ 
		
  public void recheckModel(ModelSpecification mod, ModelMatching mm, Vector ems, Vector rem, Vector queries)
  { // For each feature mapping f |--> g, checks for each 
    // ex : E and corresponding fx : F, that ex.f = fx.g in 
    // the model. 
    String srcname = realsrc.getName(); 
    String trgname = realtrg.getName(); 
	
    Vector srcobjects = mod.getObjects(srcname);
    Vector restrictedsources = new Vector();  
    Vector trgobjects = mod.getCorrespondingObjects(srcobjects,restrictedsources,trgname); 
	
	// If there is a condition Cond, check that all restrictedsources actually satisfy the condition and no other source 
	// fails the condition.
	
	Vector othersources = new Vector(); 
	othersources.addAll(srcobjects);  // the extent of realsrc in mod
	othersources.removeAll(restrictedsources);  // elements of realsrc which do not map to trgname

  
    Vector removed = new Vector(); 
    Vector added = new Vector(); 

    for (int i = 0; i < attributeMappings.size(); i++)
    { AttributeMatching am = (AttributeMatching) attributeMappings.get(i);
      System.out.println(">> Checking mapping " + am); 
      System.out.println(">> Types of " + am + " are " + am.src.getType() + " |--> " + am.trg.getType()); 
	  
      if (am.isExpressionMapping()) { } 
      else if (am.isDirectTarget())
      { Attribute satt = am.src; 
        Attribute tatt = am.trg; 
        if (satt.isEnumeration() && tatt.isEnumeration())
        { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); }
        else if (satt.isNumeric() && tatt.isNumeric())
        { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); }
        else if (satt.isBoolean() && tatt.isBoolean())
        { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); }
        else if (satt.isString() && tatt.isString())
	    { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); }
        else if (satt.isEntity() && tatt.isEntity())
        { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); } 
        else if (satt.isCollection() && tatt.isCollection())
        { am.checkModel(mod,restrictedsources,trgobjects,attributeMappings,removed,added,queries); }
        else if (tatt.isCollection())
        { System.out.println(">>> mapping of non-collection to collection: " + am); 
          am.unionSemantics = true; 
        }
        else 
        { System.out.println(">>> Mis-matching types in " + am); 
          // removed.add(am); 
        }
		// Other possibilities: enum to enum, enum/String, enum/boolean either way round
		// Entity to Collection, Collection to Entity. Others are invalid and should be removed. 
      } 
    } 
    attributeMappings.removeAll(removed); 
    attributeMappings.addAll(added); 
  }


   public void combineMatching(EntityMatching em)
   { // replace mappings of this by those of em and add other 
     // mappings of em. Combine the conditions

     Vector removed = new Vector(); 

     for (int i = 0; i < em.attributeMappings.size(); i++) 
     { AttributeMatching newam = (AttributeMatching) em.attributeMappings.get(i); 
       for (int j = 0; j < attributeMappings.size(); j++) 
       { AttributeMatching oldam = (AttributeMatching) attributeMappings.get(j); 
         if ((oldam.trg + "").equals(newam.trg + ""))
         { removed.add(oldam); } 
       }
     } 
     attributeMappings.removeAll(removed); 
     attributeMappings.addAll(em.attributeMappings); 
   } 
   
   
   public CGRule convert2CSTL()
   { // E |--> F { pi |--> qi } becomes
     // E:: 
	 //   _1 ... _n |--> F[_1,...,_n]
	 CGRule res = null; 
	 String lhs = ""; 
	 String rhs = "" + realtrg.getName() + "["; 
	 Vector variables = new Vector(); 
	 Vector conds = new Vector(); 
	 
	 java.util.Map srcfeatureMap = new java.util.HashMap(); 
	 // correspondence of _i variables and am.src variables
	 
	 Vector srcattributes = realsrc.allDefinedFeatures(); 
	 for (int i = 0; i < srcattributes.size(); i++)
	 { String satt = (String) srcattributes.get(i); 
	   String var = "_" + (i+1); 
	   lhs = lhs + var + " "; 
	   variables.add(var); 
	   srcfeatureMap.put(satt,var); 
	 } 
	 
	 System.out.println(">>> Canonical order of " + realsrc + " features is: " + srcfeatureMap); 

	 Vector trgattributes = realtrg.allDefinedFeatures(); 

	 System.out.println(">>> Canonical order of " + realtrg + " features is: " + trgattributes); 

      for (int i = 0; i < trgattributes.size(); i++) 
      { String tatt = (String) trgattributes.get(i); 
        if (tatt != null) 
        { AttributeMatching am = findAttributeMappingByFirstTarget(tatt); 
          System.out.println(">> found mapping " + am + " for " + tatt);
          if (am != null) 
          { rhs = rhs + am.targetCSTLExpressionFor(srcfeatureMap); }
          else 
          { rhs = rhs + "\"\""; }
          if (i < trgattributes.size() - 1)
          { rhs = rhs + ","; }
        }  
      }  
	   
	 /* for (int i = 0; i < attributeMappings.size(); i++)
	 { AttributeMatching am = (AttributeMatching) attributeMappings.get(i); 
	   String var = "_" + (i+1); 
	   rhs = rhs + am.targetCSTLExpressionFor(var,srcfeatureMap); 
	   if (i < attributeMappings.size() - 1)
	   { rhs = rhs + ","; } 
	 } */ 
	 
    rhs = rhs + "]";
	 
    if (condition != null) 
    { String cond = condition.cstlConditionForm(srcfeatureMap); 
      conds.add(cond); 
    }
	  
    res = new CGRule(lhs,rhs,variables,conds); 
    return res; 
  }  
}

