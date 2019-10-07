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
/* package: TransformationSynthesis
*/ 


public class AttributeMatching
{ Attribute src; 
  Attribute trg; 
  String srcname = ""; 
  String trgname = ""; 
  Expression srcvalue = null; // in cases where a value is mapped to the target
  EntityMatching dependsOn;  // in cases where src : E or Collection(E) and E maps to F
  Attribute elementVariable = null; // for  expr -> trg
  Vector auxVariables = new Vector();     // for  expr -> trg


  public AttributeMatching(Attribute source, Attribute target)
  { src = source; 
    trg = target; 
    srcname = src.getName(); 
    trgname = trg.getName(); 
  } 

  public AttributeMatching(Expression source, Attribute target)
  { src = new Attribute("" + source, source.getType(), 
                        ModelElement.INTERNAL); 
    src.setElementType(source.getElementType()); 

    srcvalue = source; 
    trg = target; 
    srcname = srcvalue + ""; 
    trgname = trg.getName(); 
  } 

  public AttributeMatching(Expression source, Attribute target, Attribute var, 
                           Vector auxvars) 
  { src = new Attribute("" + source, source.getType(), ModelElement.INTERNAL); 
    src.setType(source.getType()); 
    src.setElementType(source.getElementType()); 

    srcvalue = source; 
    trg = target; 
    elementVariable = var; 
    srcname = srcvalue + ""; 
    trgname = trg.getName();
    auxVariables = auxvars;  
  } 

  public Attribute getSource()
  { return src; } 

  public Attribute getTarget()
  { return trg; } 

  public String toString()
  { return "    " + srcname + " |--> " + trgname; } 

  public boolean unusedInSource(Attribute v)
  { String nme = v.getName(); 

    if (nme.equals(srcname)) 
    { return false; } 

    if (src.isComposed())
    { Vector path = src.getNavigation(); 
      for (int i = 0; i < path.size(); i++) 
      { Attribute p = (Attribute) path.get(i); 
        if (p.getName().equals(nme))
        { return false; } 
      } 
      return true; 
    } 

    if (srcvalue != null) 
    { if (srcvalue.hasVariable(nme))
      { return false; }
    }  

    return true; 
  } 

  public double similarity(Map mm, Vector entities, Vector thesaurus) 
  { // NMS similarity between src and trg
    double namesim = ModelElement.similarity(srcname, trgname); 
    double namesemsim = Entity.nmsSimilarity(srcname, trgname, thesaurus); 
    double nsim = (namesim + namesemsim - namesim*namesemsim); 
    double tsim = Attribute.partialTypeMatch(src,trg,mm,entities);
    return nsim*tsim;   
  } 

  public Vector enumConversions()
  { // Conversions from one enum type to a different one 
    Vector res = new Vector(); 
    Type stype = src.getType(); 
    Type ttype = trg.getType(); 
    if (stype != null && stype.isEnumeration() && 
        ttype != null && ttype.isEnumeration())
    { Maplet mm = new Maplet(stype,ttype); 
      res.add(mm); 
    } 
    return res; 
  } 

  public Vector boolEnumConversions(Vector names)
  { // Conversions from boolean type to enum
 
    Vector res = new Vector(); 
    Type stype = src.getType(); 
    Type ttype = trg.getType(); 
    if (stype != null && stype.isBoolean() && 
        ttype != null && ttype.isEnumeration())
    { res.add(ttype); 
      names.add(src.getName()); 
    } 
    return res; 
  } 

  public Vector enumBoolConversions(Vector names)
  { // Conversions from boolean type to enum
 
    Vector res = new Vector(); 
    Type stype = src.getType(); 
    Type ttype = trg.getType(); 
    if (ttype != null && ttype.isBoolean() && 
        stype != null && stype.isEnumeration())
    { res.add(stype); 
      names.add(trg.getName()); 
    } 
    return res; 
  } 

  public Vector stringEnumConversions()
  { // Conversions from String type to enum
 
    Vector res = new Vector(); 
    Type stype = src.getType(); 
    Type ttype = trg.getType(); 
    if (stype != null && stype.isString() && 
        ttype != null && ttype.isEnumeration())
    { res.add(ttype); } 
    return res; 
  } 

  public Vector enumStringConversions()
  { // Conversions from enum type to String
 
    Vector res = new Vector(); 
    Type stype = src.getType(); 
    Type ttype = trg.getType(); 
    if (stype != null && stype.isEnumeration() && 
        ttype != null && ttype.isString())
    { res.add(stype); } 
    return res; 
  } 

  public boolean isBidirectionalassociationMatching()
  { return src.isBidirectionalassociation() && 
           trg.isBidirectionalassociation(); 
  } 

  public void addAuxVariable(Attribute x)
  { auxVariables.add(x); } 

  public Vector inverts(Entity srcent, Entity trgent, Vector ems)
  { Vector res = new Vector(); 
    if (isStringAssignment())
    { // assume auxVariables.size() == 2
      BasicExpression delim = new BasicExpression("\"~\""); 
      delim.setType(new Type("String",null)); 
      delim.setElementType(new Type("String",null));
 
      BasicExpression etrg = new BasicExpression(trg); 
      BinaryExpression be1 = new BinaryExpression("->before", etrg, delim); 
      BinaryExpression be2 = new BinaryExpression("->after", etrg, delim); 

      be1.setType(new Type("String",null)); 
      be1.setElementType(new Type("String",null)); 
      be2.setType(new Type("String",null)); 
      be2.setElementType(new Type("String",null)); 

      Attribute v1 = (Attribute) auxVariables.get(0); 
      Attribute v2 = (Attribute) auxVariables.get(1); 
      res.add(new AttributeMatching(be1,v1)); 
      res.add(new AttributeMatching(be2,v2)); 
      return res; 
    } // In general could be several of these. 
    else if (isExpressionAssignment())
    { if (srcvalue instanceof SetExpression) 
      { SetExpression sse = (SetExpression) srcvalue; 
        if (sse.isOrdered() && sse.size() >= 2 && trg.isOrdered())
        { // Sequence{r1,...,rn} --> trg inverts to trg->at(1) --> r1, ...,
          // trg->at(n) --> rn
          Expression v1 = sse.getExpression(0); 
          Expression v2 = sse.getExpression(1); 
          Attribute a1 = new Attribute(v1 + "",v1.getType(),
                                       ModelElement.INTERNAL); 
          Attribute a2 = new Attribute(v2 + "",v2.getType(),
                                       ModelElement.INTERNAL);
          BasicExpression ind1 = new BasicExpression(1); 
          BasicExpression ind2 = new BasicExpression(2);
          BasicExpression trge = new BasicExpression(trg); 
          trge.setUmlKind(Expression.ATTRIBUTE); 

          BinaryExpression at1 = new BinaryExpression("->at",trge,ind1); 
          at1.setType(trg.getElementType()); 
          at1.setElementType(trg.getElementType()); 

          BinaryExpression at2 = new BinaryExpression("->at",trge,ind2); 
          at2.setType(trg.getElementType()); 
          at2.setElementType(trg.getElementType()); 

          AttributeMatching am1 = new AttributeMatching(at1,a1); 
          AttributeMatching am2 = new AttributeMatching(at2,a2);                     
          res.add(am1); 
          res.add(am2); 
          return res; 
        }
      }            // no elementVariable needed? 

      // if r1->including(r2) --> trg with trg ordered then invert to 
      // trg->front() --> r1, trg->last() --> r2
      if (srcvalue instanceof BinaryExpression) 
      { BinaryExpression sbe = (BinaryExpression) srcvalue; 
        if (sbe.operator.equals("->including") && 
            sbe.left instanceof BasicExpression && 
            sbe.right instanceof BasicExpression && trg.isOrdered()) 
        { BasicExpression lft = (BasicExpression) sbe.left; 
          BasicExpression rgt = (BasicExpression) sbe.right; 
          Attribute a1 = new Attribute(lft.data,lft.getType(),
                                       ModelElement.INTERNAL); 
          Attribute a2 = new Attribute(rgt.data,rgt.getType(),
                                       ModelElement.INTERNAL); 
          Expression trge = new BasicExpression(trg); 
          UnaryExpression at1 = new UnaryExpression("->front",trge); 
          UnaryExpression at2 = new UnaryExpression("->last",trge); 
          at1.setType(trg.getType()); 
          at1.setElementType(trg.getElementType()); 
          at2.setType(trg.getElementType()); 
          at2.setElementType(trg.getElementType()); // can't be a nested sequence? 

          res.add(new AttributeMatching(at1,a1)); 
          res.add(new AttributeMatching(at2,a2)); 
          return res; 
        }
      }   // no element variable needed? 
      
      // if r1->union(r2) --> trg with trg or r1 being sets then invert to 
      // trg --> r1, trg --> r2
      if (srcvalue instanceof BinaryExpression) 
      { BinaryExpression sbe = (BinaryExpression) srcvalue; 
        if (sbe.operator.equals("->union") && 
            sbe.left instanceof BasicExpression && 
            sbe.right instanceof BasicExpression && 
            !(sbe.left.isOrdered() && trg.isOrdered())) 
        { BasicExpression lft = (BasicExpression) sbe.left; 
          BasicExpression rgt = (BasicExpression) sbe.right; 
          Attribute a1 = new Attribute(lft.data,lft.getType(),
                                       ModelElement.INTERNAL); 
          a1.setElementType(lft.getElementType()); 

          Attribute a2 = new Attribute(rgt.data,rgt.getType(),
                                       ModelElement.INTERNAL); 
          a2.setElementType(rgt.getElementType()); 

          res.add(new AttributeMatching(trg,a1)); 
          res.add(new AttributeMatching(trg,a2)); 
          return res; 
        }
      }  

    // if r1->select(p) --> r2 invert to 
    // A'->unionAll(r2)->includes(self) --> p, for B' --> B
      if (srcvalue instanceof BinaryExpression) 
      { BinaryExpression sbe = (BinaryExpression) srcvalue; 
        if (sbe.operator.equals("->select") && 
            sbe.left instanceof BasicExpression && 
            sbe.right instanceof BasicExpression && 
            sbe.right.isBooleanValued()) 
        { BasicExpression lft = (BasicExpression) sbe.left; 
          BasicExpression rgt = (BasicExpression) sbe.right; 
          Type etval = srcvalue.getElementType(); 
          if (etval != null && etval.isEntity() && trg.getElementType() != null && 
              trg.getElementType().isEntity())
          { Entity b = etval.getEntity(); 
            Entity b1 = trg.getElementType().getEntity(); 
            BasicExpression selfexp = new BasicExpression("self"); 
            selfexp.setType(new Type(b1)); 
            selfexp.setElementType(new Type(b1)); 
            
            Attribute p = new Attribute(rgt.data,rgt.getType(),
                                        ModelElement.INTERNAL); 
            p.setElementType(rgt.getElementType()); 
            BasicExpression realtrgs = new BasicExpression(trgent); 
            BasicExpression r2exp = new BasicExpression(trg); 

            BinaryExpression allr2 = new BinaryExpression("->unionAll",realtrgs,r2exp); 
            BinaryExpression newleft = new BinaryExpression("->includes",allr2,selfexp); 

            EntityMatching bb1 = ModelMatching.getRealEntityMatching(b1,b,ems); 
            if (bb1 != null) 
            { bb1.addAttributeMatch(new AttributeMatching(newleft,p)); } 
            // and it is an expression match 
            return null; 
          }
        }
      }  
    } 
          
    AttributeMatching invam = invert(); 
    res.add(invam); 
    return res; 
  } 
      
  public AttributeMatching invert()
  { return new AttributeMatching(trg,src); } 
  // ignores any expression/value match

  boolean isDirect() // either source or target is a direct attribute
  { if (src.getNavigation().size() <= 1) 
    { return true; } 
    if (trg.getNavigation().size() <= 1) 
    { return true; } 
    return false; 
  } 

  boolean isDirectSource() // source is a direct attribute
  { if (src.getNavigation().size() <= 1) 
    { return true; } 
    return false; 
  } 

  boolean isDirectTarget() // target is a direct attribute
  { if (trg.getNavigation().size() <= 1) 
    { return true; } 
    return false; 
  } 

  boolean isValueAssignment() 
  { return srcvalue != null && elementVariable == null; } 

  boolean isExpressionAssignment()
  { return srcvalue != null && elementVariable != null; } 

  boolean isStringAssignment() 
  { return srcvalue != null && auxVariables.size() > 1; } 
  // But could occur even if srcvalue is not a string. 

  Expression addReference(BasicExpression ref, Type t)
  { if (srcvalue != null) 
    { return srcvalue.addReference(ref,t); } 
    return null; 
  } 

  public boolean equals(Object x) 
  { if (x instanceof AttributeMatching)
    { AttributeMatching am = (AttributeMatching) x; 
      if ((src + "").equals(am.src + "") && 
          (trg + "").equals(am.trg + ""))
      { return true; } 
    } 
    return false; 
  } 

  public Vector getAuxVariables()
  { return auxVariables; } 

  public Expression getExpression()
  { return srcvalue; } 

  public Attribute getElementVariable()
  { return elementVariable; } 

  static Expression dataConversion(String language, Expression sexp, Type stype, Type ttype)
  { // Convert sexp : stype to ttype
    if (("" + stype).equals("" + ttype)) 
    { return sexp; } 

    String sname = stype.getName(); 
    String tname = ttype.getName(); 
    Type selemt = stype.getElementType(); 
    Type telemt = ttype.getElementType(); 

    if (("" + selemt).equals("" + telemt))
    { if (sname.equals("Sequence") && tname.equals("Set"))
      { UnaryExpression res = new UnaryExpression("->asSet", sexp); 
        return res; 
      } 
      else if (sname.equals("Set") && tname.equals("Sequence"))
      { UnaryExpression res = new UnaryExpression("->asSequence", sexp); 
        return res; 
      }
      else if (sname.equals("Set") && !ttype.isCollectionType())
      { UnaryExpression res = new UnaryExpression("->any", sexp); 
        return res; 
      }  
      else if (sname.equals("Sequence") && !ttype.isCollectionType())
      { UnaryExpression res = new UnaryExpression("->any", sexp); 
        return res; 
      }  
      else if (tname.equals("Set") && !stype.isCollectionType())
      { SetExpression res = new SetExpression();
        res.addElement(sexp);  
        return res; 
      }  
      else if (tname.equals("Sequence") && !stype.isCollectionType())
      { SetExpression res = new SetExpression();
        res.setOrdered(true); 
        res.addElement(sexp);  
        return res; 
      }  
    } 

    String prefix = ""; 
    if ("ATL".equals(language))
    { prefix = "thisModule."; } 
    else if ("UML-RSDS".equals(language))
    { prefix = "App."; } 
    
    
    if (stype.isEnumeration() && ttype.isEnumeration())
    { String query = prefix + "convert" + stype.getName() + "_" + ttype.getName(); 
      BasicExpression res = new BasicExpression(query); 
      res.setIsEvent(); 
      res.setUmlKind(Expression.QUERY); 
      res.addParameter(sexp); 
      return res; 
    } 

    
    if (stype.isEnumeration() && tname.equals("String"))
    { String query = prefix + sname + "2String";  
      BasicExpression res = new BasicExpression(query); 
      res.setIsEvent(); 
      res.setUmlKind(Expression.QUERY); 
      res.addParameter(sexp); 
      return res; 
    } 
    else if (ttype.isEnumeration() && sname.equals("String"))
    { String query = prefix + "String2" + tname;  
      BasicExpression res = new BasicExpression(query); 
      res.setIsEvent(); 
      res.setUmlKind(Expression.QUERY); 
      res.addParameter(sexp); 
      return res; 
    } 
    
    if (stype.isEnumeration() && tname.equals("boolean"))
    { String query = prefix + sname + "2boolean";  
      BasicExpression res = new BasicExpression(query); 
      res.setIsEvent(); 
      res.setUmlKind(Expression.QUERY); 
      res.addParameter(sexp); 
      return res; 
    } 
    else if (ttype.isEnumeration() && sname.equals("boolean"))
    { String query = prefix + "boolean2" + tname;  
      BasicExpression res = new BasicExpression(query); 
      res.setIsEvent(); 
      res.setUmlKind(Expression.QUERY); 
      res.addParameter(sexp); 
      return res; 
    } 

    if (stype.isCollectionType() && !ttype.isCollectionType())
    { UnaryExpression res = new UnaryExpression("->any", sexp); 
      return res; 
    } 
    else if (ttype.isCollectionType() && !stype.isCollectionType())
    { UnaryExpression res = new UnaryExpression("->as" + sname, sexp); 
      return res; 
    } 
 
    return new BinaryExpression("->oclAsType", sexp, new BasicExpression(ttype)); 
  } 

  public Vector analyseCorrelationPatterns(Entity s, Entity t, Entity realsrc, Entity realtrg, 
                                           EntityMatching ematch, Vector ems) 
  { Vector res = new Vector(); 

    if (src.isComposition() && !trg.isComposition()) 
    { CorrelationPattern p = new CorrelationPattern("Remove aggregation", 
                                                    "Aggregation " + realsrc + "::" + src + 
                                                    " maps to non-aggregation " + 
                                                    realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 
      if (res.contains(p)) { } 
      else { res.add(p); }  
    } 

    if (!src.isComposition() && trg.isComposition()) 
    { CorrelationPattern p = new CorrelationPattern("Introduce aggregation", 
                                                    "Non-aggregation " + realsrc + "::" + src + 
                                                    " maps to aggregation " + 
                                                    realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 
      p.setWarning(true);     // aggregation properties are not ensured in source model but are 
                              // required by the target. 
      if (res.contains(p)) { } 
      else { res.add(p); }  

    } 

    if (trg.isComposed())
    { Vector trgents = trg.intermediateEntities(); 
      for (int k = 0; k < trgents.size(); k++) 
      { Entity trgent = (Entity) trgents.get(k); 
        if (trgent.isAbstract())
        { System.out.println(">!! Warning: intermediate target class " + trgent + " in " + trg + 
                             " is abstract - replace it by a concrete class from");
          Vector trgactualleaves = trgent.getActualLeafSubclasses(); 
          System.out.println(">>! " + trgactualleaves); // preferably one not in range of ems
          Vector trgunused = ModelMatching.unusedTargetEntities(trgactualleaves,ems); 
          if (trgunused.size() > 0) 
          { System.out.println(">>! Recommend: " + trgunused + "\n"); }  
          String ans = 
              JOptionPane.showInputDialog("Replace " + trgent + " by? (y/n):");
          if (ans != null && "y".equals(ans)) 
          { Entity conc = (Entity) ModelElement.lookupByName(ans,trgactualleaves); 
            if (conc != null) 
            { trg.replaceIntermediateEntity(trgent,conc); }
          } 
        }  
      } 
    }  

    if (!src.isUnique() && trg.isUnique()) 
    { CorrelationPattern p = new CorrelationPattern("Introduce identity feature", 
                                                    "Non-identity " + realsrc + "::" + src + 
                                                    " maps to identity " + 
                                                    realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 
      p.setWarning(true);     // identity property not ensured in source model but  
                              // required by the target. 
      if (res.contains(p)) { } 
      else { res.add(p); }  

    } 

    if (src.isUnique() && !trg.isUnique()) 
    { CorrelationPattern p = new CorrelationPattern("Remove identity feature", 
                                                    "Identity " + realsrc + "::" + src + 
                                                    " maps to non-identity " + 
                                                    realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 

      if (res.contains(p)) { } 
      else { res.add(p); }  

    } 

    if (src.getNavigation().size() <= 1 && 
        trg.getNavigation().size() > 1) 
    { CorrelationPattern p = new CorrelationPattern("Introduce indirection", 
                                                    "Direct feature " + realsrc + "::" + src + 
                                                    " maps to composed feature " + 
                                                    realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 
      if (res.contains(p)) { } 
      else { res.add(p); }  
    } 


    if (src.getNavigation().size() > 1 && 
        trg.getNavigation().size() <= 1) 
    { CorrelationPattern p = new CorrelationPattern("Remove indirection", 
                                                    "Composed feature " + realsrc + "::" + src + 
                                                    " maps to direct feature " + realtrg + "::" + trg); 
      p.addSourceEntity(realsrc); 
      p.addTargetEntity(realtrg); 
      p.addSourceFeature(src); 
      p.addTargetFeature(trg); 
      if (res.contains(p)) { } else { res.add(p); }  
    } 

    if (src.isComposed()) 
    { Attribute asrc = src.getFinalFeature(); 
      Attribute atrg = null; 
      if (trg.isComposed())
      { atrg = trg.getFinalFeature(); } 
      else 
      { atrg = trg; } 
      Entity own1 = asrc.getOwner(); 
      Entity own2 = atrg.getOwner(); 
      // System.out.println("OWNER of " + asrc + " is " + own1); 
      // System.out.println("OWNER of " + atrg + " is " + own2); 
      Entity srcimg = ModelMatching.lookupRealMatch(own1,ems); 
      // System.out.println("IMAGE of " + own1 + " is " + srcimg); 
      if (srcimg != null && srcimg != own2)
      { CorrelationPattern mq; 
        if (Entity.isAncestor(own2,srcimg))
        { mq = new CorrelationPattern("Pull up feature", 
                                      "Feature " + own1 + "::" + asrc + " is moved to superclass " + 
                                      own2 + "::" + atrg + " of " + srcimg); 
        } 
        else if (Entity.isAncestor(srcimg,own2))
        { mq = new CorrelationPattern("Push down feature", 
                                      "Feature " + own1 + "::" + asrc + " is moved to subclass " + 
                                      own2 + "::" + atrg + " of " + srcimg);
          mq.setWarning(true);  
        } 
        else 
        { mq = new CorrelationPattern("Feature moved", 
                                      "Feature " + own1 + "::" + asrc + " is moved to " + 
                                      own2 + "::" + atrg); 
        } 
        mq.addSourceEntity(own1); 
        mq.addTargetEntity(own2); 
        mq.addTargetEntity(srcimg); 
        mq.addSourceFeature(asrc); 
        mq.addTargetFeature(atrg); 
        if (res.contains(mq)) { } else { res.add(mq); }  
      } 
    } 
    else if (trg.isComposed()) 
    { Attribute atrg = trg.getFinalFeature(); 
      Attribute asrc = null; 
      if (src.isComposed())
      { asrc = src.getFinalFeature(); } 
      else 
      { asrc = src; } 
      Entity own1 = asrc.getOwner(); 
      Entity own2 = atrg.getOwner(); 
      // System.out.println("OWNER of " + asrc + " is " + own1); 
      // System.out.println("OWNER of " + atrg + " is " + own2); 
      Entity srcimg = ModelMatching.lookupRealMatch(own1,ems); 
      // System.out.println("IMAGE of " + own1 + " is " + srcimg); 
      if (srcimg != null && srcimg != own2)
      { CorrelationPattern mq; 
        if (Entity.isAncestor(own2,srcimg))
        { mq = new CorrelationPattern("Pull up feature", 
                                      "Feature " + own1 + "::" + asrc + " is moved to superclass " + 
                                      own2 + "::" + atrg + " of " + srcimg); 
        } 
        else if (Entity.isAncestor(srcimg,own2))
        { mq = new CorrelationPattern("Push down feature", 
                                      "Feature " + own1 + "::" + asrc + " is moved to subclass " + 
                                      own2 + "::" + atrg + " of " + srcimg); 
          mq.setWarning(true); 
        }
        else 
        { mq = new CorrelationPattern("Feature moved", 
                                      "Feature " + own1 + "::" + asrc + " is moved to " + 
                                      own2 + "::" + atrg);
        }  
        mq.addSourceEntity(own1); 
        mq.addTargetEntity(own2); 
        mq.addTargetEntity(srcimg); 
        mq.addSourceFeature(asrc); 
        mq.addTargetFeature(atrg); 
        if (res.contains(mq)) { } else { res.add(mq); } 
      } 
    } 
    else // both direct, can still be moved
    { Entity own1 = src.getOwner(); 
      Entity own2 = trg.getOwner(); 
      // System.out.println("OWNER of " + asrc + " is " + own1); 
      // System.out.println("OWNER of " + atrg + " is " + own2); 
      Entity srcimg = ModelMatching.lookupRealMatch(own1,ems); 
      // System.out.println("IMAGE of " + own1 + " is " + srcimg); 
      if (srcimg != null && srcimg != own2)
      { CorrelationPattern mq; 
        if (Entity.isAncestor(own2,srcimg))
        { mq = new CorrelationPattern("Pull up feature", 
                                      "Feature " + own1 + "::" + src + " is moved to superclass " + 
                                      own2 + "::" + trg + " of " + srcimg); 
        } 
        else if (Entity.isAncestor(srcimg,own2))
        { mq = new CorrelationPattern("Push down feature", 
                                      "Feature " + own1 + "::" + src + " is moved to subclass " + 
                                      own2 + "::" + trg + " of " + srcimg); 
          mq.setWarning(true); 
        }
        else 
        { mq = new CorrelationPattern("Feature moved", 
                                      "Feature " + own1 + "::" + src + " is moved to " + 
                                      own2 + "::" + trg);
        }  
        mq.addSourceEntity(own1); 
        mq.addTargetEntity(own2); 
        mq.addTargetEntity(srcimg); 
        mq.addSourceFeature(src); 
        mq.addTargetFeature(trg); 
        if (res.contains(mq)) { } else { res.add(mq); } 
      } 
    } 


    Type srct = src.getType(); 
    Type trgt = trg.getType();
    Type imgt = Type.getImageType(srct,ems); 
 
    if (("" + imgt).equals("" + trgt)) { } 
    else if (Type.isRecursiveSubtype(imgt,trgt))
    { CorrelationPattern q = new CorrelationPattern("Widen type", 
                                   "Feature " + realsrc + "::" + src +
                                   " type widened from " + imgt + " to " + trgt + " in " +  
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      if (res.contains(q)) { } else { res.add(q); }  
    }  
    else if (Type.isRecursiveSubtype(trgt,imgt))
    { CorrelationPattern q = new CorrelationPattern("Narrow type", 
                                   "Feature " + realsrc + "::" + src +
                                   " type narrowed from " + imgt + " to " + trgt + " in " +  
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    }  

    int mult1 = srct.typeMultiplicity(); 
    int mult2 = trgt.typeMultiplicity(); 

    if (mult1 == ModelElement.ONE && mult2 == ModelElement.MANY) 
    { CorrelationPattern q = new CorrelationPattern("Widen multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " widened from ONE to MANY multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.MANY && mult2 == ModelElement.ONE) 
    { CorrelationPattern q = new CorrelationPattern("Narrow multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " narrowed from MANY to ONE multiplicity in " + 
                                   realtrg + "::" + trg); 
      // Suggest a source splitting if possible. 
      Entity realtrgsup = realtrg.getSuperclass(); 
      if (realtrgsup != null) 
      { Vector siblings = new Vector(); 
        siblings.addAll(realtrgsup.getSubclasses());
        siblings.remove(realtrg);
        if (siblings.size() == 1) 
        { UnaryExpression srcsize = new UnaryExpression("->size",new BasicExpression(src)); 
          Expression guard0 = new BinaryExpression("=",srcsize,new BasicExpression(1));
          Expression guard1 = new BinaryExpression("/=",srcsize,new BasicExpression(1)); 
          ematch.addCondition(guard0); 
          Entity othere = (Entity) siblings.get(0); 
          EntityMatching newothermatch = new EntityMatching(realsrc,othere); 
          newothermatch.addCondition(guard1); 
          newothermatch.copyApplicableAttributeMappings(ematch.attributeMappings); 
          System.out.println(">>> Suggest new Entity match: " + newothermatch); 
          String ans = 
              JOptionPane.showInputDialog("Add to entity matches?: (y/n) ");
          if (ans != null && "y".equals(ans))
          { ems.add(newothermatch); } 
        } 
      }    
        
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.ONE && src.getLower() == 0 && mult2 == ModelElement.ONE
             && trg.getLower() == 1) 
    { CorrelationPattern q = new CorrelationPattern("Narrow multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " narrowed from 0..1 to ONE multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.MANY && src.getUpper() == 1 && 
             mult2 == ModelElement.MANY && trg.getUpper() != 1) 
    { CorrelationPattern q = new CorrelationPattern("Widen multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " widened from 0..1 to * multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.MANY && mult2 == ModelElement.MANY && 
             trg.getUpper() == 1 && src.getUpper() != 1) 
    { CorrelationPattern q = new CorrelationPattern("Narrow multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " narrowed from * to 0..1 multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.ONE && mult2 == ModelElement.MANY && trg.getUpper() == 1) 
    { CorrelationPattern q = new CorrelationPattern("Widen multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " widened from 1 to 0..1 multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 
    else if (mult1 == ModelElement.MANY && src.getUpper() == 1 && mult2 == ModelElement.ONE) 
    { CorrelationPattern q = new CorrelationPattern("Narrow multiplicity", 
                                   "Feature " + realsrc + "::" + src +
                                   " narrowed from 0..1 to 1 multiplicity in " + 
                                   realtrg + "::" + trg); 
      q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 

     
    return res; 
  } // also feature merging; stereotype changes


  public EntityMatching isObjectMatch(Vector ems) 
  { Vector spath = new Vector(); 
    spath.addAll(src.getNavigation()); 
    if (spath.size() == 0) 
    { spath.add(src); } 
    Vector tpath = new Vector(); 
    tpath.addAll(trg.getNavigation()); 
    if (tpath.size() == 0) 
    { tpath.add(trg); } 

    // Attribute s1 = (Attribute) spath.get(0); 
    // Attribute t1 = (Attribute) tpath.get(0); 

    

    Type tsrc = src.getElementType(); 
    Type ttrg = trg.getElementType(); 

    // System.out.println(">_>_ " + src + " (" + tsrc + ") " + trg + " (" + ttrg + 
    //                    ")"); 

    if (Type.isEntityType(tsrc) && Type.isEntityType(ttrg))
    { Entity e1 = tsrc.getEntity(); 
      Entity e2 = ttrg.getEntity();

      EntityMatching emx = ModelMatching.findEntityMatching(e1,ems); 
      if (emx != null && emx.realtrg != null) 
      { if (e2.getName().equals(emx.realtrg.getName()) || 
            Entity.isAncestor(e2,emx.realtrg))
        { return emx; } 
      } // to map from e1 to e2, emx.realsrc must be e1 or an ancestor of e1,
        // emx.realtrg must be e2 or a descendent of e2
    }  
    return null; 
  } 

  String whenClause(EntityMatching emx, String srcroot, String trgroot, Map whens) 
  { // isObjectMatching is true
    Vector spath = new Vector(); 
    spath.addAll(src.getNavigation()); 
    if (spath.size() == 0) 
    { spath.add(src); } 
    Vector tpath = new Vector(); 
    tpath.addAll(trg.getNavigation()); 
    if (tpath.size() == 0) 
    { tpath.add(trg); } 

    // Attribute s1 = (Attribute) spath.get(0); 
    // Attribute t1 = (Attribute) tpath.get(0); 
    // Type tsrc = src.getElementType(); 
    // Type ttrg = trg.getElementType(); 
    String nsrc = emx.src.getName(); 
    String ntrg = emx.trg.getName(); 
    String srcentx = nsrc; 
    if (nsrc.endsWith("$"))
    { srcentx = nsrc.substring(0,nsrc.length()-1); } 
    if (emx.realsrc != null) 
    { srcentx = emx.realsrc.getName(); }
 
    String trgentx = ntrg; 
    if (ntrg.endsWith("$"))
    { trgentx = ntrg.substring(0,ntrg.length()-1); }  
    if (emx.realtrg != null) 
    { trgentx = emx.realtrg.getName(); } 

    String d1 = dataname(srcroot,spath); 
    String d2 = dataname(trgroot,tpath); 

    BasicExpression be1 = new BasicExpression(d1); 
    be1.setType(src.getType()); 
    if (emx.realsrc != null) 
    { be1.setElementType(new Type(emx.realsrc)); }  
    be1.variable = src; 

    BasicExpression be2 = new BasicExpression(d2); 
    be2.setType(trg.getType()); 
    if (emx.realtrg != null) 
    { be2.setElementType(new Type(emx.realtrg)); } 
    be2.variable = trg; 

    whens.set(be2,be1); 
    return srcentx + "2" + trgentx + "(" + d1 + "," + d2 + ")"; 
  } 

  String whenClause(String trgroot, Vector ems, Map whens) 
  { // For expr --> trg, need E2F(elementVariable,d2) 
    if (elementVariable == null) 
    { return ""; } 

    Vector tpath = new Vector(); 
    tpath.addAll(trg.getNavigation()); 
    if (tpath.size() == 0) 
    { tpath.add(trg); } 

    Type vtype = elementVariable.getElementType(); 
    if (vtype != null && vtype.isEntity())
    { Entity vent = vtype.getEntity(); 
      // System.out.println(">>> Entity of " + elementVariable + " is " + vent); 

      Type ttype = trg.getElementType(); 
      if (ttype != null && ttype.isEntity())
      { Entity tent = ttype.getEntity(); 
        EntityMatching emx = ModelMatching.findEntityMatching(vent,ems); 
        // System.out.println(">>> Entity match of " + elementVariable + " is " + emx); 
        if (emx != null) 
        { String nsrc = emx.src.getName(); 
          String ntrg = emx.trg.getName(); 
          String srcentx = nsrc; 
          if (nsrc.endsWith("$"))
          { srcentx = nsrc.substring(0,nsrc.length()-1); }  
          if (emx.realsrc != null) 
          { srcentx = emx.realsrc.getName(); }
 
          String trgentx = ntrg; 
          if (ntrg.endsWith("$"))
          { trgentx = ntrg.substring(0,ntrg.length()-1); }  
          if (emx.realtrg != null) 
          { trgentx = emx.realtrg.getName(); } 

          String d2 = dataname(trgroot,tpath); 

          // BasicExpression be2 = new BasicExpression(d2); 
          // be2.setType(trg.getType()); 
          // be2.setElementType(new Type(emx.realtrg)); 
          // be2.variable = trg; 

          // whens.set(be2,be1); 
          return srcentx + "2" + trgentx + "(" + elementVariable + "," + d2 + ")"; 
        } 
      } 
    } 
    return ""; 
  } 

  String sourcedataname(String prefix)
  { Vector path = new Vector(); 
    path.addAll(src.getNavigation()); 
    if (path.size() == 0) 
    { path.add(src); }
    return dataname(prefix,path); 
  } 

  static String dataname(String prefix, Attribute att)
  { Vector path = new Vector(); 
    path.addAll(att.getNavigation()); 
    if (path.size() == 0) 
    { path.add(att); }
    return dataname(prefix,path); 
  } 

  static String dataname(String prefix, Vector path)
  { // nme = prefix + "_" + nme$value for non-objects, non-collections
    // nme = prefix + "_" + nme$x for objects, collections

    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType();
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
 
      if (t != null && Type.isCollectionOrEntity(t))
      { Type telem = t.getElementType(); 
        if ((telem != null && telem.isEntity()) || t.isEntity())
        { return fullname + "$x"; }
      }  
      return fullname + "$value";
    }  
    else if (path.size() > 1) 
    { Attribute p = (Attribute) path.get(0); 
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
      Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
 
      return dataname(fullname, pathtail); 
    }      

    // String pname = src.getName(); 
    // String fullname = prefix + "_" + pname; 
    // return fullname + "$value";
    return prefix;  
  } 

  String sourceequation(String prefix, ObjectTemplateExp sexp)
  { // nme = nme$value for non-objects, non-collections
    Vector path = new Vector(); 
    path.addAll(src.getNavigation()); 
    if (path.size() == 0) 
    { path.add(src); }
    return sourceequation(prefix,path,sexp); 
  } 

  String sourceequation(String prefix, Vector path, ObjectTemplateExp objTE)
  { // nme = nme$value for non-objects, non-collections

    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType();
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
 
      if (t != null && Type.isCollectionOrEntity(t))
      { Type telem = t.getElementType(); 
        if ((telem != null && telem.isEntity()) || t.isEntity())
        { Entity tent; 
          if (telem == null) 
          { tent = t.getEntity(); } 
          else 
          { tent = telem.getEntity(); } 

          if (tent == null) 
          { System.err.println("!! ERROR: no entity type for " + p); 
            return ""; 
          } 

          String obj = fullname + "$x";
          Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 
          objroot.setElementType(new Type(tent)); 

          // String defn = pname + " = " + obj + " : " + tent; 
 
          // Vector exps = (Vector) objectTemplates.get(defn);
          // if (exps == null) 
          // { exps = new Vector(); }
          
          // objectTemplates.set(defn,exps);
          // return "";
          objTE.addPTI(p,new ObjectTemplateExp(objroot,tent));     
          return pname + " = " + obj + " : " + tent + " { }";
        }  
      } 
      BasicExpression valueExp = new BasicExpression(fullname + "$value"); 
      valueExp.setType(p.getType()); 
      valueExp.setElementType(p.getElementType()); 
      valueExp.variable = src; // to record the actual thing. 

      objTE.addPTI(p,valueExp);     
      return pname + " = " + fullname + "$value ";
    }  
    else if (path.size() > 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType();
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
 
      // (Type.isCollectionOrEntity(t))
      Entity tent = null; 
      String obj = fullname + "$x"; 

      Type telem = t.getElementType(); 
      if (telem != null && telem.isEntity())
      { tent = telem.getEntity(); } 
      else if (t.isEntity())
      { tent = t.getEntity(); } 

      if (tent != null)  
      { Vector pathtail = new Vector(); 
        pathtail.addAll(path); 
        pathtail.remove(0); 

        String body = "";         
        Object subtemplate = objTE.getPTI(p); 
        if (subtemplate instanceof ObjectTemplateExp)
        { ObjectTemplateExp subTE = (ObjectTemplateExp) subtemplate; 
          body = sourceequation(prefix + "_" + pname, pathtail, subTE); 

        // String defn = pname + " = " + obj + " : " + tent; 
        // Vector exps = (Vector) objectTemplates.get(defn);
        // if (exps == null) 
        // { exps = new Vector(); }
        // exps.add(body); 
        // objectTemplates.set(defn,exps);   

          return pname + " = " + obj + " : " + tent + " { " + body + " }";
        } 
        else 
        { Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 
          objroot.setElementType(new Type(tent)); 

          ObjectTemplateExp newSubTE = new ObjectTemplateExp(objroot,tent); 
          objTE.addPTI(p,newSubTE);     
          body = sourceequation(prefix + "_" + pname, pathtail, newSubTE); 
          return pname + " = " + obj + " : " + tent + " { " + body + " }";
        }   
      }  

      BasicExpression valueExp = new BasicExpression(fullname + "$value"); 
      valueExp.setType(p.getType()); 
      valueExp.setElementType(p.getElementType()); 
      valueExp.variable = src; 

      objTE.addPTI(p,valueExp);     
      return pname + " = " + fullname + "$value";
    }      

    // String pname = src.getName(); 
    // String fullname = prefix + "_" + pname; 
    // return pname + " = " + fullname + "$value"; 
    return ""; 
  } 

  String targetequation(String prefix,String srcdata,ObjectTemplateExp tExps)
  { // nme = nme$value for non-objects, non-collections


    Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0) 
    { path.add(trg); }
    return targetequation(prefix,path,srcdata,tExps); 
  } 

  String targetequationUMLRSDS(String tvar, Vector bound)
  { Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0)
    { path.add(trg); } 
    return targetequationUMLRSDS(tvar,path,bound); 
  } 

  String targetequation(String prefix, Vector path, String srcdata, ObjectTemplateExp tExp)
  { // nme = srcdata at the end of the path

    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType();
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
 
      if (Type.isCollectionOrEntity(t))
      { Entity tent = null; 
        Type telem = t.getElementType(); 
        if (telem != null && telem.isEntity())
        { tent = telem.getEntity(); } 
        else if (t.isEntity())
        { tent = t.getEntity(); } 

        if (tent != null) 
        { // String defn = pname + " = " + fullname + "$x : " + tent;  
          // Vector exps = (Vector) tExps.get(defn);
          // if (exps == null) 
          // { exps = new Vector(); }
          
          // tExps.set(defn,exps);
          // return "";    

          String obj = fullname + "$x";
          Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 
          objroot.setElementType(new Type(tent)); 

          tExp.addPTI(p,new ObjectTemplateExp(objroot,tent));     
          return pname + " = " + fullname + "$x : " + tent + " { }"; 
        } 
      }
      BasicExpression valueExp = new BasicExpression(srcdata); 
      valueExp.setType(p.getType()); 
      valueExp.setElementType(p.getElementType()); 
      valueExp.variable = src; 

      Expression valueExpC = valueExp; 
      if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("String".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if (p.getType().isEnumeration() && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if (src.getType().isBoolean() && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if (p.getType().isBoolean() && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 

      tExp.addPTI(p,valueExpC);     
      return pname + " = " + valueExpC; 
    }  
    else if (path.size() > 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType();
      String pname = p.getName(); 
      String fullname = prefix + "_" + pname; 
 
      // (Type.isCollectionOrEntity(t))
      Type telem = t.getElementType(); 
      Entity tent = telem.getEntity(); 
      if (tent == null) 
      { tent = t.getEntity(); } 

      Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      String body = ""; 
      // String defn = pname + " = " + fullname + "$x : " + tent; 

      Object subtemplate = tExp.getPTI(p); 
      if (subtemplate instanceof ObjectTemplateExp)
      { ObjectTemplateExp subTE = (ObjectTemplateExp) subtemplate; 
        body = targetequation(prefix + "_" + pname, pathtail, srcdata, subTE); 
      } 
      else 
      { String obj = fullname + "$x"; 

        Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 

          if (tent == null) 
          { System.err.println("!! ERROR: no entity type for " + p); 
            return ""; 
          } 

        objroot.setElementType(new Type(tent)); 

        ObjectTemplateExp newSubTE = new ObjectTemplateExp(objroot,tent); 
        tExp.addPTI(p,newSubTE);     
        body = targetequation(prefix + "_" + pname, pathtail, srcdata, newSubTE); 
        return pname + " = " + obj + " : " + tent + " { " + body + " }";
      }   
      // return pname + " = " + fullname + "$x : " + tent + " { " + body + " }"; 
    }      

    if (path.size() > 0) 
    { Attribute p = (Attribute) path.get(0); 
      String pname = p.getName(); 
      BasicExpression valueExp = new BasicExpression(srcdata); 
      valueExp.setType(p.getType()); 
      valueExp.setElementType(p.getElementType()); 
      valueExp.variable = src; 

      Expression valueExpC = valueExp; 
      if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("String".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      if (p.getType().isEnumeration() && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("boolean".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 

      tExp.addPTI(p,valueExpC);     
      return pname + " = " + valueExpC;
    } 
    return "";  
  } // and boolean -> enum data conversions


  String targetequationUMLRSDS(String tvar, Vector path, Vector bound) 
  { Type ttarg = trg.getType(); 
    Type tsrc = src.getType(); 

    String res = ""; 
    if (ttarg == null) 
    { System.err.println("ERROR: null type for " + trg); 
      return res; 
    } 
    if (tsrc == null) 
    { System.err.println("ERROR: null type for " + src); 
      return res; 
    } 

    int multsrc = tsrc.typeMultiplicity(); 
    int multtrg = ttarg.typeMultiplicity(); 
 
    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType(); 

      if (t != null && Type.isEntityCollection(t))
      { // convert the target objects
       
        Type telem = t.getElementType();

        Entity tent = telem.getEntity(); 
        String tename = tent.getName(); 
        Type selem = tsrc.getElementType(); // must also be an entity

        if (selem.isEntity())
        { Entity sentity = selem.getEntity(); 
          String sename = sentity.getName(); 
          String sId = sename.toLowerCase() + "Id";
          String srclookup = tename + "[" + src + "." + sId + "]";  // src is a feature 

          if (multsrc != ModelElement.ONE && multtrg == ModelElement.ONE)
          { res = tvar + "." + trg + " = " + srclookup + "->any()"; } 
          else if (multsrc == ModelElement.ONE && multtrg != ModelElement.ONE)
          { res = srclookup + " : " + tvar + "." + trg; }  
          else if ("Sequence".equals(t.getName()) && "Set".equals(src.getType().getName()))
          { res = tvar + "." + trg + " = " + srclookup + "->asSequence()"; } 
          else if ("Set".equals(t.getName()) && "Sequence".equals(src.getType().getName()))
          { res = tvar + "." + trg + " = " + srclookup + "->asSet()"; } 
          else 
          { res = tvar + "." + trg + " = " + srclookup; }
        } 
        else // mapping non-entity to entity, may not be valid.  
        { res = tvar + "." + trg + " = " + src; }  
      } 
      else if (t != null && t.isEntity())
      { // convert the target objects

        Entity tent = t.getEntity(); 
        String tename = tent.getName(); 
      
        Entity sentity = tsrc.getEntity(); 
        if (sentity == null) 
        { Type tsrcelem = src.getElementType(); 
          sentity = tsrcelem.getEntity(); 
        } 

        if (sentity == null) 
        { System.err.println("ERROR: not entity type/element type: " + tsrc); 
          return ""; 
        } // Maybe allow strings to be mapped to instances of an entity with a String-primary key

        String sename = sentity.getName(); 
        String sId = sename.toLowerCase() + "Id"; 
        String srclookup = tename + "[" + src + "." + sId + "]";  // src is a feature 

        if (multsrc != ModelElement.ONE && multtrg == ModelElement.ONE)
        { res = tvar + "." + trg + " = " + srclookup + "->any()"; } 
        else if (multsrc == ModelElement.ONE && multtrg != ModelElement.ONE)
        { res = srclookup + " : " + tvar + "." + trg; }  
        else 
        { res = tvar + "." + trg + " = " + srclookup; }         
      } 
      else // p is of a value type 
      { BasicExpression sexp = new BasicExpression(src); 
        Expression texp = AttributeMatching.dataConversion("UML-RSDS",sexp,tsrc,ttarg); 
          
        if (multsrc != ModelElement.ONE && multtrg == ModelElement.ONE)
        { res = tvar + "." + trg + " = " + src + "->any()"; } 
        else if (multsrc == ModelElement.ONE && multtrg != ModelElement.ONE)
        { res = src + " : " + tvar + "." + trg; }  
        else 
        { res = tvar + "." + trg + " = " + texp; } 
      } 
    } 
    return res; 
  } 
 
  String composedTargetEquation(String tvar, Vector created)
  { Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0)
    { path.add(trg); } 
    Expression srcexp = new BasicExpression(src); 

    boolean alreadycreated = false; 
    boolean multiple = false; 

    Vector preattp = VectorUtil.largestInitialSegment(path,created);
    Attribute preatt = null; 
    String prex = "";
    String tvar1 = tvar;  
    Vector pathrem = new Vector(); 
    Entity preattD = null; 
 
    if (preattp != null && preattp.size() > 0) 
    { preatt = new Attribute(preattp); 
      prex = preatt.getName(); // ModelElement.composeNames(preatt); 
      tvar1 = tvar + "." + prex; 
      pathrem.addAll(path); 
      pathrem.removeAll(preattp); 
      Type tpreatt = preatt.getElementType(); 
      if (tpreatt != null && tpreatt.isEntity())
      { preattD = tpreatt.getEntity(); } 
      // System.out.println(">>> Already created " + preatt + " : " + preattD + " followed by " + pathrem); 
      alreadycreated = true; 
      multiple = preatt.isManyValued(); 
    } 

    Type t = trg.getType(); 
    Entity e = null; 
    if (Type.isEntityType(t))
    { e = t.getEntity(); } 
    else if (Type.isEntityCollection(t))
    { Type telem = t.getElementType(); 
      e = telem.getEntity(); 
    } 

    if (e != null)  
    { String ename = e.getName();  
      String sname = "";  
      String bname = ""; 
      Entity b = null; 
      // String shortename = ename.substring(0,ename.length()-1);  // target feature element type 

      Type stype = src.getElementType(); 

      if (Type.isEntityType(stype))
      { b = stype.getEntity(); 
        bname = b.getName(); 
        // sname = bname.substring(0,bname.length()-1);  // source feature element type
      }  
      String bId = bname.toLowerCase() + "Id"; 
      String evar = ename.toLowerCase() + "$x"; 
      String evardec = ""; 

      if (Type.isCollectionType(src.getType()) && b != null)
      { evardec = " & " + evar + " : " + ename + "[" + src + "." + bId + "] => \n"; } 
      else if (b != null) 
      { evardec = " & " + evar + " = " + ename + "[" + src + "." + bId + "] => \n"; }
      else if (Type.isCollectionType(src.getType()))
      { evardec = " & " + evar + " : " + src + " => \n"; } 
      else  
      { evardec = " & " + evar + " = " + src + " => \n"; }

      if (alreadycreated)
      { if (multiple) 
        { if (srcexp.isMultipleValued() && 
              !Attribute.isMultipleValued(pathrem))
          { // ignore the existing elements in tvar1 and create new ones for the new srcexp. 

            String cteq = composedTargetEquation(srcexp,"_y",evar,pathrem);
            if (cteq.length() > 0) 
            { return evardec + "      " + preattD + "->exists( _y | " + 
                               " _y : " + tvar1 + " & " + cteq + " )"; 
            } 
            return "";                
          } 
          else 
          { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
            if (cteq0.length() > 0) 
            { return evardec + "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; } 
            return "";
          }  
        } 
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return evardec + "      " + cteq; } 
          return "";                
        } 
      } 
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return evardec + "      " + cteq1; } 
      return "";  
    } 
    else // target is of a value type 
    if (Type.isCollectionType(src.getType()))
    { String evar = Identifier.nextIdentifier("var$"); 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " & " + evar + " : " + src + " =>\n" + 
                   "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; 
          } 
          return ""; 
        } 
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return " & " + evar + " : " + src + " =>\n" + 
                   "      " + cteq; 
          } 
          return "";
        } 
      } 
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " & " + evar + " : " + src + " =>\n" + 
                "      " + cteq1; 
      } 
      return "";  
    } 
    else 
    { String evar = src + ""; 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " =>\n" + 
                   "       " + tvar1 + "->forAll( _y | " + cteq0 + " )";
          } 
          return ""; 
        }
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0)
          { return " =>\n" + 
               "       " + cteq;
          } 
          return ""; 
        }
      }  
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " =>\n" + "       " + cteq1; } 
      return "";  
    } 
    // return tvar + "." + trg + " = " + src;  
  } 

  String composedTargetEquationExpr(Expression srcexp, String tvar, Vector created)
  { Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0)
    { path.add(trg); } 

    boolean alreadycreated = false; 
    boolean multiple = false; 

    Vector preattp = VectorUtil.largestInitialSegment(path,created);
    Attribute preatt = null; 
    String prex = "";
    String tvar1 = tvar;  
    Vector pathrem = new Vector(); 
    Entity preattD = null; 
 
    if (preattp != null && preattp.size() > 0) 
    { preatt = new Attribute(preattp); 
      Type tpreatt = preatt.getType(); 

      prex = preatt.getName(); // ModelElement.composeNames(preatt); 
      tvar1 = tvar + "." + prex; 
      pathrem.addAll(path); 
      pathrem.removeAll(preattp); 
      // System.out.println(">>> Already created " + preatt + " " + tvar1 + " " + pathrem); 
      alreadycreated = true; 
      multiple = preatt.isManyValued(); 
      if (tpreatt != null && tpreatt.isEntity())
      { preattD = tpreatt.getEntity(); } 
    } 

    Type t = trg.getType(); 
    Entity e = null; 
    if (Type.isEntityType(t))
    { e = t.getEntity(); } 
    else if (Type.isEntityCollection(t))
    { Type telem = t.getElementType(); 
      e = telem.getEntity(); 
    } 

    Type srctype = srcexp.getType(); 

    if (e != null)  
    { String ename = e.getName();  
      String sname = "";  
      String bname = ""; 
      Entity b = null; 

      Type stype = srcexp.getElementType(); 
      if (Type.isEntityType(stype))
      { b = stype.getEntity(); 
        bname = b.getName(); 
         // source expression element type
      }  
      String bId = bname.toLowerCase() + "Id"; 
      String evar = ename.toLowerCase() + "$x"; 
      String evardec = ""; 

      if (Type.isCollectionType(srctype) && b != null)
      { evardec = " & " + evar + " : " + ename + "[(" + srcexp + ")->collect(" + bId + ")] => \n"; } 
      else if (b != null) 
      { evardec = " & " + evar + " = " + ename + "[(" + srcexp + ")." + bId + "] => \n"; }
      else if (Type.isCollectionType(srctype))  
      { evardec = " & " + evar + " : " + srcexp + " => \n"; }
      else 
      { evardec = " & " + evar + " = " + srcexp + " => \n"; }
      
      
      if (alreadycreated)
      { if (multiple) 
        { if (srcexp.isMultipleValued() && 
              !Attribute.isMultipleValued(pathrem))
          { // ignore the existing elements in tvar1 and create new ones for the new srcexp. 

            String cteq = composedTargetEquation(srcexp,"_y",evar,pathrem);
            if (cteq.length() > 0) 
            { return evardec + "      " + preattD + "->exists( _y | " + 
                               " _y : " + tvar1 + " & " + cteq + " )"; 
            } 
            return "";                
          } 
          else 
          { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
            if (cteq0.length() > 0) 
            { return evardec + "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; } 
            return "";
          }  
        } 
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return evardec + "      " + cteq; } 
          return "";                
        } 
      } 
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return evardec + "      " + cteq1; } 
      return "";  
    } 
    else // target is of a value type 
    if (Type.isCollectionType(srctype))
    { String evar = Identifier.nextIdentifier("var$"); 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " & " + evar + " : " + srcexp + " =>\n" + 
                   "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; 
          } 
          return ""; 
        } 
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return " & " + evar + " : " + srcexp + " =>\n" + 
                   "      " + cteq; 
          } 
          return "";
        } 
      } 
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " & " + evar + " : " + srcexp + " =>\n" + 
                "      " + cteq1; 
      } 
      return "";  
    } 
    else 
    { String evar = src + ""; 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " =>\n" + 
                   "       " + tvar1 + "->forAll( _y | " + cteq0 + " )";
          } 
          return ""; 
        }
        else 
        { String cteq = composedTargetEquation(srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0)
          { return " =>\n" + 
               "       " + cteq;
          } 
          return ""; 
        }
      }  
      String cteq1 = composedTargetEquation(srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " =>\n" + "       " + cteq1; } 
      return "";  
    } 
    // return tvar + "." + trg + " = " + src;  
  } 

  public String composedTargetEquation(Expression srcexp, String tvar, String evar, Vector path) 
  { if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType(); 
      Type tsrc = srcexp.getType(); 
        
      if (t == null) 
      { System.err.println("!! ERROR: null type for " + p); 
        return ""; 
      } 

      if (tsrc == null) 
      { System.err.println("!! ERROR: null type for " + srcexp); 
        return ""; 
      } 

      if (Type.isCollectionType(t))
      { return evar + " : " + tvar + "." + p.getName(); } 
      else if (t.isEnumeration() && "String".equals(tsrc.getName()))
      { BasicExpression sexp = new BasicExpression(evar); 
        Expression texp = AttributeMatching.dataConversion("UML-RSDS",sexp,tsrc,t); 
        return tvar + "." + p.getName() + " = " + texp;
      } 
      else if (tsrc.isEnumeration() && "String".equals(t.getName()))
      { BasicExpression sexp = new BasicExpression(evar); 
        Expression texp = AttributeMatching.dataConversion("UML-RSDS",sexp,tsrc,t); 
        return tvar + "." + p.getName() + " = " + texp;
      } 
      else if (t.isEnumeration() && tsrc.isEnumeration())
      { BasicExpression sexp = new BasicExpression(evar); 
        Expression texp = AttributeMatching.dataConversion("UML-RSDS",sexp,tsrc,t); 
        return tvar + "." + p.getName() + " = " + texp;
      } 
      else if (tsrc.isBoolean() && t.isEnumeration())
      { BasicExpression sexp = new BasicExpression(evar); 
        Expression texp = AttributeMatching.dataConversion("UML-RSDS",sexp,tsrc,t); 
        return tvar + "." + p.getName() + " = " + texp;
      } 
      else 
      { return tvar + "." + p.getName() + " = " + evar; } 
    } 
    else if (path.size() > 0)
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType(); 
      Entity e = null; 
      if (Type.isEntityType(t))
      { e = t.getEntity(); } 
      else if (Type.isEntityCollection(t))
      { Type elemT = t.getElementType(); 
        e = elemT.getEntity(); 
      } 
      String ename = e.getName(); 
      String ex = ename.toLowerCase() + "$x"; 
      Vector ptail = new Vector(); 
      ptail.addAll(path); 
      ptail.remove(0); 
      String code = composedTargetEquation(srcexp,ex,evar,ptail);
      if (code.length() > 0) 
      { code = " & " + code; } 
 
      if (Type.isCollectionType(t))
      { code = ex + " : " + tvar + "." + p.getName() + code; } 
      else 
      { code = tvar + "." + p.getName() + " = " + ex + code; } 
      return ename + "->exists( " + ex + " | " + code + " ) "; 
    } 
    return ""; 
  } // push down the evar : ename[src.bId] declaration as locally as possible. 



  String targetequationQVTO(Expression tvar, Expression srcdata, Vector ems, Vector created)
  { Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0)
    { path.add(trg); } 

    Vector preattp = VectorUtil.largestInitialSegment(path,created); 
    if (preattp != null && preattp.size() > 0) 
    { Attribute preatt = new Attribute(preattp); 
      String prex = preatt.getName(); // ModelElement.composeNames(preatt); 
      String tvar1 = tvar + "." + prex; 
      Vector pathrem = new Vector(); 
      pathrem.addAll(path); 
      pathrem.removeAll(preattp); 
      // System.out.println(">>> Already created " + preatt + " " + tvar1 + " " + pathrem); 
      if (preatt.isManyValued())
      { if (srcdata.isMultipleValued() && 
            !Attribute.isMultipleValued(pathrem))
        { // BasicExpression x = new BasicExpression("_x"); 
          // x.setType(srcdata.getElementType()); 
          // Entity d = preatt.getElementType().getEntity(); 
          // Vector pathremtail = new Vector(); 
          // pathremtail.addAll(pathrem); 
          // Attribute nextp = (Attribute) pathrem.get(0); 
          // pathremtail.remove(0); 
          Expression tvarexp = new BasicExpression(tvar1); 
          tvarexp.setType(preatt.getType()); 
          tvarexp.setElementType(preatt.getElementType()); 
          String body = qvtoObjectFor(preatt,pathrem,srcdata,tvarexp,
                                      ems,created); 
          return body; 
          // tvar1 + " += (" + srcdata + ")->xcollect( _x | " + body + " );";
        }  
        BasicExpression y = new BasicExpression("_y"); 
        y.setType(preatt.getElementType()); 
        String body = targetequationQVTO(y,srcdata,pathrem,ems,created); 
        return tvar1 + "->forEach(_y) { " + body + " };"; 
      } 
      BasicExpression p = new BasicExpression(preatt); 
      p.setObjectRef(tvar); 
      return targetequationQVTO(p,srcdata,pathrem,ems,created); 
      // tvar = result.preatt if ONE mult, path is remainder of path
    } 

    return targetequationQVTO(tvar,srcdata,path,ems,created); 
  } 

  String targetequationQVTO(Expression tvar, Expression svar, Vector path, Vector ems, Vector created) 
  { Type ttarg = trg.getType(); 
    Type tsrc = src.getType(); 
    int smult = tsrc.typeMultiplicity(); 
    int tmult = ttarg.typeMultiplicity(); 

    String res = ""; 
 
    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Type t = p.getType(); 
      if (t != null) 
      { tmult = t.typeMultiplicity(); } 

      if (t != null && Type.isEntityCollection(t))
      { // convert the target objects using resolve
        // unless the target entity element type is shared between source & target models. 
        // Also take account of the multiplicities. 
        Type telem = t.getElementType();

        Entity tent = telem.getEntity();
        if (tent != null && !tent.isSourceEntity() && !tent.isTargetEntity())
        { res = tvar + "." + p + " := " + svar; } 
        else if (tent != null)  
        { String tename = tent.getName(); 
          Type selem = tsrc.getElementType(); // must also be an entity
          Entity s = tsrc.getEntity(); 
          if (selem != null && selem.isEntity())
          { s = selem.getEntity(); } 

          EntityMatching em = ModelMatching.findEntityMatching(s,ems);
          // System.out.println("Entity matching for " + s + " is " + em); 

          if (em != null && smult == ModelElement.ONE) 
          { res = tvar + "." + p + " := " + svar +  
                 ".resolveoneIn(" + em.realsrc + "::" + 
                 em.realsrc + "2" + em.realtrg + "," + tename + ")";
          } 
          else if (em != null && smult != ModelElement.ONE)
          { res = tvar + "." + p + " := " + svar + 
                 ".resolveIn(" + em.realsrc + "::" + 
                 em.realsrc + "2" + em.realtrg + "," + tename + ")";
          } 
          else 
          { res = tvar + "." + p + " := " + svar; }  
        } 
      } 
      else if (t != null && t.isEntity())
      { // convert the target objects using resolveone

        Entity tent = t.getEntity(); 
      
        if (tent != null && !tent.isSourceEntity() && !tent.isTargetEntity())
        { res = tvar + "." + p + " := " + svar; } 
        else if (tent != null)  
        { String tename = tent.getName(); 
          Type selem = tsrc.getElementType(); // must also be an entity
          Entity s = tsrc.getEntity(); 
          if (selem != null && selem.isEntity())
          { s = selem.getEntity(); } 
          
          EntityMatching em = ModelMatching.findEntityMatching(s,ems); 
          // System.out.println("Entity matching for " + s + " is " + em); 

          if (em != null && smult == ModelElement.ONE) 
          { res = tvar + "." + p + " := " + svar + 
                 ".resolveoneIn(" + em.realsrc + "::" + 
                 em.realsrc + "2" + em.realtrg + "," + tename + ")";
          } 
          else if (em != null && smult != ModelElement.ONE)
          { res = tvar + "." + p + " := " + svar + 
                 ".resolveIn(" + em.realsrc + "::" + 
                 em.realsrc + "2" + em.realtrg + "," + tename + ")";
          } 
          else 
          { res = tvar + "." + p + " := " + svar; }  
        } 
      } 
      else if (svar instanceof BasicExpression) // it is a value or collection of values. 
      { BasicExpression srcexp = (BasicExpression) svar; 
                                 // new BasicExpression(svar + "." + src);
        Expression sconv = AttributeMatching.dataConversion("QVTO",srcexp, src.getType(), t);  
        res = tvar + "." + p + " := " + sconv + ";"; 
        // created.add(trg.getNavigation()); 
        return res; 
      }

      if (tmult == ModelElement.ONE && smult != ModelElement.ONE) 
      { res = res + "->any();"; } 
      else if (tmult != ModelElement.ONE && smult == ModelElement.ONE)
      { res = res + "->as" + t.getName() + "();"; } 
      else if (tmult != ModelElement.ONE && smult != ModelElement.ONE && 
               "Sequence".equals(t.getName()) && "Set".equals(src.getType().getName()))
      { res = res + "->asSequence();"; } 
      else if (tmult != ModelElement.ONE && smult != ModelElement.ONE && 
               "Set".equals(t.getName()) && "Sequence".equals(src.getType().getName()))
      { res = res + "->asSet();"; } 
      else 
      { res = res + ";"; } 

      // { res = AttributeMatching.dataConversion(new BasicExpression(res), tsrc, ttarg) + ";"; }  
      // other data conversions. 
      // created.add(trg.getNavigation()); 
    } 
    else if (path.size() > 1) 
    { Attribute p = (Attribute) path.get(0); 
      Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      // BasicExpression srcexp = new BasicExpression(src); 
      // srcexp.setObjectRef(svar); // has same multiplicity and types as src
      BasicExpression trgexp = new BasicExpression(p); 
      trgexp.setObjectRef(tvar); 
      res = qvtoObjectFor(p, pathtail, svar, trgexp, ems, created); 
      // created.add(trg.getNavigation()); 
    }  
      // tvar + "." + trg + " := " + svar + "." + src + ";"; } 
    return res; 
  } 

  private String qvtoObjectFor(Attribute previous, Vector path, 
                               Expression svar, Expression tvar, 
                               Vector ems, Vector created)
  { String result = ""; 
    Type tsrc = src.getType(); 
    Type ttarg = trg.getType(); 

    boolean smult = svar.isMultiple(); 
    boolean tmult = tvar.isMultiple(); 
    String operator = ":="; 
    if (tmult) 
    { operator = "+="; } 

    if (path.size() == 1) 
    { Attribute p = (Attribute) path.get(0); 
      Entity t = p.getOwner(); 
      String pname = p.getName(); 
      Type ptype = p.getType(); 

      String pop = ":="; 
      Entity pent = null; 

      if (p.isManyValued())
      { pop = "+=";
        Type pelemt = p.getElementType(); 
        if (pelemt != null && pelemt.isEntity())
        { pent = pelemt.getEntity(); }
      } 
      else 
      { if (ptype.isEntity())
        { pent = ptype.getEntity(); } 
      } 

      Type selem = svar.getElementType(); // must also be an entity if ptype is
      if (selem == null) 
      { selem = svar.getType(); } 
        
      if (previous != null && previous.isManyValued() && src.isManyValued())
      { String xresolve = "_x.resolveone()"; 
        if (selem.isEntity())
        { EntityMatching emx = ModelMatching.findEntityMatching(selem.getEntity(), ems); 
          String desttype = ""; 
          if (emx != null) 
          { if (pent != null) 
            { desttype = pent.getName(); }  
            else 
            { desttype = emx.realtrg.getName(); } 
            xresolve = "_x.resolveoneIn(" + 
                       emx.realsrc + "::" + emx.realsrc + "2" + emx.realtrg + 
                       ", " + desttype + ")";
          }  
        } 
        else // source is of a value type 
        { xresolve = "_x"; } 
 
        if (ptype.getName().equals("Sequence"))
        { result = 
            tvar + " += " + svar + "->xcollect( _x | object " + 
                                        t + " { " + pname + " := Sequence{ " + xresolve + " }; } );"; 
        } 
        else if (ptype.getName().equals("Set"))
        { result = tvar + " += " + svar + "->xcollect( _x | object " + t + 
                                          " { " + pname + " := Set{ " + xresolve + " }; } );"; 
        } 
        else 
        { result = tvar + " += " + svar + "->xcollect( _x | object " + 
                                            t + " { " + pname + " := " + xresolve + "; } );"; 
        }
      } 
      else // dataconversions as for targetequationQVTO
      { 
        if (pent != null && selem != null && selem.isEntity()) 
        { Entity s = selem.getEntity(); 
          EntityMatching em = ModelMatching.findEntityMatching(s,ems); 
          // System.out.println("Entity matching for " + s + " is " + em); 

          String res = pname + " " + pop + " " + svar + "." + 
                 "resolveoneIn(" + em.realsrc + "::" + em.realsrc + "2" + em.realtrg + ", " +
                 pent.getName() + ");"; 
          result = tvar + " " + operator + " object " + t + " { " + res + " };"; 
        } 
        else 
        { result = tvar + " " + operator + " object " + t + 
                        " { " + pname + " " + pop + " " + svar + "; };"; 
        } 
      } 
    } 
    else if (path.size() > 1) 
    { Attribute p = (Attribute) path.get(0); 
      Entity t = p.getOwner();
      String pname = p.getName(); 
      Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 

      BasicExpression xexp = new BasicExpression("_x"); 
      xexp.setType(svar.getElementType());  
      BasicExpression pexp = new BasicExpression(p); 

      if (previous != null && previous.isManyValued() && src.isManyValued())
      { String rem1 = qvtoObjectFor(null,pathtail,xexp,pexp,ems,created); 
        result = tvar + " += " + svar + "->xcollect( _x | object " + t + " { " + rem1 + " });"; 
      } 
      else   
      { String rem = qvtoObjectFor(p,pathtail,svar,pexp,ems,created); 
        result = tvar + " " + operator + " object " + t + " { " + rem + " };"; 
      } 
    } 
    return result; 
  } 



  String atldirecttarget(String svar) 
  { Expression sexp = // new BasicExpression(svar + "." + src);
                      src.atlComposedExpression(svar); 
    Expression vexp = sexp;
    Type stype = src.getType(); 
    Type ttype = trg.getType();   
    if (stype != null && ttype != null && 
        "String".equals(ttype.getName()) && stype.isEnumeration())
    { vexp = AttributeMatching.dataConversion("ATL", sexp, stype, ttype); } 
    else if (stype != null && ttype != null && 
             "String".equals(stype.getName()) && ttype.isEnumeration())
    { vexp = AttributeMatching.dataConversion("ATL", sexp, stype, ttype); } 
    // or both are enums, or one boolean and the other enum
    else if (stype != null && ttype != null && 
             stype.isEnumeration() && ttype.isEnumeration())
    { vexp = AttributeMatching.dataConversion("ATL", sexp, stype, ttype); }
    else if (stype != null && ttype != null && 
        "boolean".equals(ttype.getName()) && stype.isEnumeration())
    { vexp = new BasicExpression("thisModule." + stype.getName() + "2boolean" + trg.getName() + 
                                 "(" + sexp + ")"); } 
    else if (stype != null && ttype != null && 
             "boolean".equals(stype.getName()) && ttype.isEnumeration())
    { vexp = new BasicExpression("thisModule.boolean2" + ttype.getName() + src.getName() + 
                                 "(" + sexp + ")"); 
    } 
    
    Binding bres = new Binding(trg + "", vexp); 
    return trg + " <- " + vexp;
  } 

  Binding atlcomposedtarget(Vector newclauses, Vector newrules, Vector newdo, 
                            String srcvar, String trgvar, Entity realsrc,
                            Vector created, java.util.Map implementedBy) 
  { Vector path = trg.getNavigation(); 
    if (path.size() == 0) 
    { path.add(trg); } 

    Attribute preatt = null; 
    Vector preattp = VectorUtil.largestInitialSegment(path,created); 
    Object preset = null; 

    if (preattp != null && preattp.size() > 0) 
    { preatt = new Attribute(preattp); 
      String prex = preatt.getName(); // ModelElement.composeNames(preatt); 
      // String tvar1 = tvar + "." + prex; 
      Vector pathrem = new Vector(); 
      pathrem.addAll(path); 
      pathrem.removeAll(preattp); 
      preset = implementedBy.get(prex); 
      // System.out.println(">>> Already created " + preattp + " " + prex + " " + pathrem); 
      // System.out.println(">>> Implemented by " + preset); 
    } 
    // else if (path.size() > 0) 
    // { preatt = (Attribute) path.get(0); } 

    BasicExpression srcexvar = new BasicExpression(srcvar); 
    Expression expr; 

    if (isExpressionAssignment())
    { expr = srcvalue.addReference(srcexvar, new Type(realsrc)); } 
    else 
    { expr = new BasicExpression(src); 
      ((BasicExpression) expr).setObjectRef(srcexvar); 
    } 

    
    if (preset != null && (preset instanceof Attribute)) 
    { // System.out.println(">>> Additional mapping " + expr + " ¦-> " + trg); 
      Attribute trgref = trg.objectReference(); 
      Attribute f = trg.getFinalFeature(); 
        
      if (trgref.isMultiValued())
      { // System.out.println(f + " upper bound is " + f.upperBound()); 
        // System.out.println(expr + " upper bound is " + expr.upperBound()); 
        
        if (f.upperBound() >= expr.upperBound())
        { newdo.add("for (_x in " + trgvar + "." + trgref + 
                    ") { _x." + f + " <- " + expr + "; }"); 
        } 
        else 
        { Binding res = atlTargetMap(preatt,path,expr,newclauses,newrules,implementedBy);
          return res; 
        } 
      }  
      else if (f.upperBound() >= expr.upperBound()) 
      { newdo.add(trgvar + "." + trg + " <- " + expr + ";"); }
      else 
      { newdo.add(trgvar + "." + trg + " <- " + expr + "->any();"); }
 
      return null; 
    }      
    else 
    { Binding res = atlTargetMap(preatt,path,expr,newclauses,newrules,implementedBy);
 

      // System.out.println(">>> Composed assignment: " + this);
      // System.out.println(">>> New rule clauses: " + newclauses); 
      // System.out.println(">>> New rules: " + newrules); 
      return res; 
    } 
  } 

Binding atlTargetMap(Attribute preatt, Vector path, Expression sexpr, 
                     Vector newclauses, Vector newrules, java.util.Map implementedBy)
{ if (path.size() == 0)
  { return null; }
  Attribute p1 = (Attribute) path.get(0);
  String p1x = p1.getName();

  String preattname = ""; 
  if (preatt != null) 
  { preattname = preatt.getName(); } 
  else 
  { preatt = p1; 
    preattname = p1x; 
  } 

  Vector pathtail = new Vector();
  pathtail.addAll(path);
  pathtail.remove(0);
  Type p1type = p1.getElementType();
  Entity d = null;
  String dx = "";
  Attribute dxvar = null; 

  Attribute nextatt = null; 
  if (preatt != null) 
  { Vector prepath = preatt.getNavigation(); 
    Vector route = new Vector(); 
    route.addAll(prepath); 
    route.add(p1); 
    nextatt = new Attribute(route); 
  } 

  if (p1type == null) 
  { Binding bres = new Binding(p1x, sexpr); 
    return bres; 
    // return p1x + " <- " + sexpr; 
  }


  if (p1type.isEntity())
  { d = p1type.getEntity(); 
    dx = p1x + "_" + d.getName().toLowerCase() + "_x";
    dxvar = new Attribute(dx, new Type(d), ModelElement.INTERNAL); 
    dxvar.setElementType(new Type(d)); 
  }
  
  Entity srcent = null;
  String sx = "";
  Attribute srcvar = null;

  Type srctype = sexpr.getElementType();

  if (Type.isEntityType(srctype))
  { srcent = srctype.getEntity();
    sx = srcent.getName().toLowerCase() + "_x";
    srcvar = new Attribute(sx,new Type(srcent),ModelElement.INTERNAL);
    srcvar.setElementType(new Type(srcent));
  }
  else if (srctype != null) // eg, for String  
  { // sx = srctype.getName().toLowerCase() + "$x"; 
    sx = Identifier.nextIdentifier("var_"); 
    srcvar = new Attribute(sx,srctype,ModelElement.INTERNAL); 
    srcvar.setElementType(srctype); 
  } 
  else // for int, long, boolean, etc
  { // System.err.println("!! Null element type in " + sexpr); 
    srctype = sexpr.getType(); 
    if (srctype == null) 
    { Binding bres1 = new Binding(p1x,sexpr); 
      return bres1; 
      // return p1x + " <- " + sexpr; 
    }
    sx = Identifier.nextIdentifier("var_"); 
         // srctype.getName().toLowerCase() + "$x"; 
    srcvar = new Attribute(sx,srctype,ModelElement.INTERNAL); 
    srcvar.setElementType(srctype); 
  } 

  if (path.size() > 1) 
  { if (p1.isSingleValued())
    { Binding sexp = atlTargetMap(nextatt,pathtail,sexpr,newclauses,newrules,implementedBy);
      // String newclause = "  " + dx + " : " + d.getName() + "\n" +
      //     "    ( " + sexp + " )";
      // newclauses.add(newclause);
      Binding bres2 = new Binding(p1x, new BasicExpression(dxvar)); 

      OutPatternElement ope = null; 
      if (implementedBy.get(preattname) != null && 
          implementedBy.get(preattname) instanceof OutPatternElement)
      { ope = (OutPatternElement) implementedBy.get(preattname); 
        ope.addBinding(sexp);
        // implementedBy.put(preattname, ope);
        return null;  
      } 
      else 
      { ope = new OutPatternElement(dxvar); 
        newclauses.add(ope);
        ope.addBinding(sexp);
        implementedBy.put(preattname, ope);
        return bres2;  
      } 
      // return p1x + " <- " + dx;
    } 
    else // p1 not ONE
    { /* if (sexpr.isMultipleValued() && srcent != null && 
          Attribute.isMultipleValued(pathtail))        // tail is many 
      { Binding sexp = atlTargetMap(nextatt,pathtail,sexpr,newclauses,newrules,implementedBy);
        // String newclause = "  " + dx + " : " + d.getName() + "\n" +
        //     "    ( " + sexp + " )";
        // newclauses.add(newclause);
        Binding bres2 = new Binding(p1x, new BasicExpression(dxvar)); 

        OutPatternElement ope = null; 
        if (implementedBy.get(preattname) != null && 
            implementedBy.get(preattname) instanceof OutPatternElement)
        { ope = (OutPatternElement) implementedBy.get(preattname); 
          ope.addBinding(sexp);
          // implementedBy.put(preattname, ope);
          return null;  
        } 
        else 
        { ope = new OutPatternElement(dxvar); 
          ope.addBinding(sexp); 
          newclauses.add(ope);
          implementedBy.put(preattname, ope);
          return bres2;  
        } 
      } 
      else */ 
      if (sexpr.isMultipleValued() && srcent != null)
      { Binding sexp2 = atlTargetMap(nextatt,pathtail,new BasicExpression(srcvar),
                                     newclauses,newrules,implementedBy);
        String newmap = "Map" + srcent + "2" + d + "_" + ModelElement.underscoredNames(pathtail);
        // String newrule = "  unique lazy rule " + newmap +"\n" +
        //   "  { from " + sx + " : MM1!" + srcent + "\n" +
        //   "    to " + dx + " : MM2!" + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        MatchedRule mr = new MatchedRule(true,true);
          mr.setName(newmap);  
          InPattern ip = new InPattern(); 
          InPatternElement ipe = new InPatternElement(srcvar,null); 
          ip.addElement(ipe); 
          OutPattern op = new OutPattern(); 
          OutPatternElement ope = new OutPatternElement(dxvar);
          ope.addBinding(sexp2); 
          // implementedBy.put(preattname, ope);
          implementedBy.put(preattname, preatt); 
          op.setElement(ope);
          mr.setInPattern(ip);  
          mr.setOutPattern(op);
          if (newrules.contains(mr)) { } 
          else 
          { newrules.add(mr); } 
         

        BinaryExpression crange = new BinaryExpression(":",new BasicExpression(sx),sexpr); 
        BasicExpression mapcall = new BasicExpression("thisModule." + newmap + "(" + sx + ")"); 
        BinaryExpression collexp = new BinaryExpression("|C", crange, mapcall); 
        Binding bres3 = new Binding(p1x,collexp); 

        return bres3; 
        // return p1x + " <- " + 
        //   sexpr + "->collect( " + sx + " | thisModule." + newmap + "(" + sx + "))";
      }
      else if (sexpr.isMultipleValued())
      { Binding sexp2 = atlTargetMap(nextatt,pathtail,new BasicExpression(srcvar),
                                    newclauses,newrules,implementedBy);
        String newmap = "Map" + srctype + "2" + d + "_" + ModelElement.underscoredNames(pathtail);
        // String newrule = "    rule " + newmap +"(" + sx + " : " + srctype + ")\n" +
        //   "  { to " + dx + " : " + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        OutPatternElement ope = null; 
        MatchedRule mr = new MatchedRule(false,false);
          mr.setName(newmap);  
          mr.setIsCalled(true); 
          mr.addParameter(srcvar); 
          OutPattern op = new OutPattern(); 
          ope = new OutPatternElement(dxvar); 
          op.setElement(ope); 
          mr.setOutPattern(op);

          if (newrules.contains(mr)) { } 
          else 
          { newrules.add(mr); } 
 
          ope.addBinding(sexp2); 
          implementedBy.put(preattname,preatt); 
         
        
        BinaryExpression crange = new BinaryExpression(":",new BasicExpression(sx),sexpr); 
        BasicExpression mapcall = new BasicExpression("thisModule." + newmap + "(" + sx + ")"); 
        BinaryExpression collexp = new BinaryExpression("|C", crange, mapcall); 
        Binding bres4 = new Binding(p1x,collexp); 
        return bres4; 
        // return p1x + " <- " + 
        //   sexpr + "->collect( " + sx + " | thisModule." + newmap + "(" + sx + "))";
      }
      else // if (srcent != null) // sexpr single-valued, object
      { // Binding sexp2 = atlTargetMap(nextatt,pathtail,new BasicExpression(srcvar),
        //                              newclauses,newrules,implementedBy);
        // String newmap = "Map" + srcent + "2" + d;
        Binding sexp = atlTargetMap(nextatt,pathtail,sexpr,newclauses,newrules,implementedBy);
        // String newclause = "  " + dx + " : " + d.getName() + "\n" +
        //     "    ( " + sexp + " )";
        // newclauses.add(newclause);
        Binding bres2 = new Binding(p1x, new BasicExpression(dxvar)); 
        // String newrule = "  unique lazy rule " + newmap +"\n" +
        //   "  { from " + sx + " : MM1!" + srcent + "\n" +
        //   "    to " + dx + " : MM2!" + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        OutPatternElement ope = null; 
        if (implementedBy.get(preattname) != null && 
            implementedBy.get(preattname) instanceof OutPatternElement)
        { ope = (OutPatternElement) implementedBy.get(preattname);           
          ope.addBinding(sexp); 
          // implementedBy.put(preattname,ope); 
          return null; 
        } 
        else 
        { ope = new OutPatternElement(dxvar); 
          // MatchedRule mr = new MatchedRule(true,true);
          // mr.setName(newmap);  
          // InPattern ip = new InPattern(); 
          // InPatternElement ipe = new InPatternElement(srcvar,null); 
          // ip.addElement(ipe); 
          // OutPattern op = new OutPattern(); 
          // ope = new OutPatternElement(dxvar); 
          // op.setElement(ope); 
          // mr.setInPattern(ip); 
          // mr.setOutPattern(op);
          // newrules.add(mr); 
          ope.addBinding(sexp); 
          newclauses.add(ope); 
          implementedBy.put(preattname,ope); 
          return bres2; 
        } 

       /* SetExpression setexp = new SetExpression(); 
          BasicExpression mapcall = 
            new BasicExpression("thisModule." + newmap + "(" + sexpr + ")"); 
          setexp.addElement(mapcall); 
          Binding bres5 = new Binding(p1x,setexp); 
          return bres5; */ 

        // return p1x + " <- Set{ thisModule." + newmap + "(" + sexpr + ") }";
      }
      /* else 
      { Binding sexp2 = atlTargetMap(nextatt,pathtail,new BasicExpression(srcvar),
                                    newclauses,newrules,implementedBy);
        String newmap = "Map" + srctype + "2" + d;
        // String newrule = "    rule " + newmap +"(" + sx + " : " + srctype + ")\n" +
        //   "  { to " + dx + " : " + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        OutPatternElement ope = null; 
        if (implementedBy.get(preattname) != null && 
            implementedBy.get(preattname) instanceof OutPatternElement)
        { ope = (OutPatternElement) implementedBy.get(preattname); 
          ope.addBinding(sexp2); 
          // implementedBy.put(preattname,ope); 
          return null; 
        } 
        else 
        { MatchedRule mr = new MatchedRule(false,false);
          mr.setName(newmap);  
          mr.setIsCalled(true); 
          OutPattern op = new OutPattern(); 
          ope = new OutPatternElement(dxvar); 
          op.setElement(ope); 
          mr.setOutPattern(op);
          newrules.add(mr); 
          ope.addBinding(sexp2); 
          implementedBy.put(preattname,ope); 
        }  

        SetExpression setexp = new SetExpression(); 
        BasicExpression mapcall = 
          new BasicExpression("thisModule." + newmap + "(" + sexpr + ")"); 
        setexp.addElement(mapcall); 
        Binding bres6 = new Binding(p1x,setexp); 
        return bres6; 
        // return p1x + " <- Set{ thisModule." + newmap + "(" + sexpr + ") }";
      } */ 
    }
  }
  else // path.size() == 1
  { if (p1.isSingleValued() && sexpr.isSingleValued())
    { Binding res7 = new Binding(p1x, sexpr); 
      return res7; 
      // return p1x + " <- " + sexpr; 
    }
    else if (p1.isSingleValued())
    { UnaryExpression anyexp = new UnaryExpression("->any", sexpr); 
      Binding bres8 = new Binding(p1x, anyexp); 
      return bres8; 
      // return p1x + " <- (" + sexpr + ")->any()";
    }
    else
    { Binding res9 = new Binding(p1x, sexpr); 
      return res9; 
      // return p1x + " <- " + sexpr; 
    }
  }
}


} 
