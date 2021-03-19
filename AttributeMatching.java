import java.io.*; 
import java.util.Collections; 
import javax.swing.JOptionPane; 
import java.util.Vector; 
import java.util.List; 


/******************************
* Copyright (c) 2003--2021 Kevin Lano
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
  boolean unionSemantics = false;     // src' should be added to trg, not assigned

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

  public AttributeMatching(Expression source, Expression target)
  { src = new Attribute("" + source, source.getType(), 
                        ModelElement.INTERNAL); 
    src.setElementType(source.getElementType());
 

    srcvalue = source; 
    trg = new Attribute("" + target, target.getType(), 
                        ModelElement.INTERNAL);
    trg.setElementType(target.getElementType()); 
 
    srcname = srcvalue + ""; 
    trgname = trg.getName(); 
  } 

  public void setElementVariable(Attribute var) 
  { elementVariable = var; } 

  public void addAuxVariable(Attribute var) 
  { if (auxVariables.contains(var)) { } 
    else 
    { auxVariables.add(var); }
  }  

  public void setExpressionMatch(Expression source, Attribute target, Attribute var) 
  { src = new Attribute("" + source, source.getType(), ModelElement.INTERNAL); 
    src.setType(source.getType()); 
    src.setElementType(source.getElementType()); 

    srcvalue = source; 
    trg = target; 
    elementVariable = var; 
    srcname = srcvalue + ""; 
    trgname = trg.getName();
  } 

  public Attribute getSource()
  { return src; } 
  
  public Expression getSourceExpression()
  { if (srcvalue != null) 
    { return srcvalue; }
    return new BasicExpression(src); 
  }

  public Attribute getTarget()
  { return trg; } 

  public String toString()
  { return "    " + srcname + " |--> " + trgname; } 

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

  boolean is1MultiplicityTarget() // target not a collection
  { if (trg.isCollection()) 
    { return false; } 
    return true; 
  } 

  boolean isIdentityTarget() // target is an identity
  { if (trg.isIdentity()) 
    { return true; } 
    Attribute fatt = trg.getFinalFeature();
    if (fatt != null && fatt.isIdentity())
    { return true; } 
    return false; 
  } 

  boolean notReferenceTarget() // target is not a reference
  { if (trg.isReference())
    { return false; } 
    return true; 
  } 

  boolean isComposedTarget() // target is a composed attribute
  { if (trg.getNavigation().size() > 1) 
    { return true; } 
    return false; 
  } 

  boolean isExpressionMapping()
  { return srcvalue != null; } 

  boolean isValueAssignment() 
  { return srcvalue != null && elementVariable == null; } 

  boolean isExpressionAssignment()
  { return srcvalue != null && elementVariable != null; } 

  boolean isStringAssignment() 
  { return srcvalue != null && auxVariables.size() > 1; } 
  // But could occur even if srcvalue is not a string. 

  boolean isStringMapping() 
  { return srcvalue != null && auxVariables.size() > 1 && 
           srcvalue.getType().isString(); 
  } 

  boolean isValueTyped()
  { // target and source are of a value type
    Type st = src.getType(); 
    Type tt = trg.getType(); 
    if (Type.isValueType(st) && Type.isValueType(tt))
    { return true; } 
    return false; 
  } 

  void displayMappingKind()
  { if (isExpressionMapping() && !(srcvalue.getType().isString()))
    { System.out.println("Expression mapping: " + this); } 

    if (isStringAssignment() && srcvalue.getType().isString())
    { System.out.println("String mapping: " + this); } 

    if (isValueAssignment())
    { System.out.println("Value mapping: " + this); } 
  } 

  boolean isPossibleFeatureMerge()
  { // of form s->before(sep) or s->after(sep), etc
    if (srcvalue != null && 
        (srcvalue instanceof BinaryExpression))
    { BinaryExpression be = (BinaryExpression) srcvalue; 
      if ("->before".equals(be.operator) || "->after".equals(be.operator))
      { return true; } 
      if ("->front".equals(be.operator) || "->last".equals(be.operator))
      { return true; } 
      if ("->first".equals(be.operator) || "->tail".equals(be.operator))
      { return true; } 
    } 
    return false; 
  } 
 
  Vector findMergeFamily(Vector ams, Vector seen)
  { // of form s->before(sep) or s->after(sep), etc
    Vector res = new Vector(); 
    res.add(this); 

    if (srcvalue != null && 
        (srcvalue instanceof BinaryExpression))
    { BinaryExpression be = (BinaryExpression) srcvalue; 
      if ("->before".equals(be.operator))
      { Expression sep = be.getRight(); 
        for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof BinaryExpression)
          { BinaryExpression be2 = (BinaryExpression) amx.srcvalue; 
            if ("->after".equals(be2.getOperator()) && 
                (sep + "").equals(be2.getRight() + "") && 
                (be.getLeft() + "").equals(be2.getLeft() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      } 
      else if ("->after".equals(be.operator))
      { Expression sep = be.getRight(); 
        for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof BinaryExpression)
          { BinaryExpression be2 = (BinaryExpression) amx.srcvalue; 
            if ("->before".equals(be2.getOperator()) && 
                (sep + "").equals(be2.getRight() + "") && 
                (be.getLeft() + "").equals(be2.getLeft() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      }
    }
    return res; 
  } 

  Vector findMergeFamilySequence1(Vector ams, Vector seen)
  { // of form s->front() or s->last(), etc
    Vector res = new Vector(); 
    res.add(this); 

    if (srcvalue != null && 
        (srcvalue instanceof UnaryExpression))
    { UnaryExpression be = (UnaryExpression) srcvalue; 
      if ("->front".equals(be.operator))
      { for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof UnaryExpression)
          { UnaryExpression be2 = (UnaryExpression) amx.srcvalue; 
            if ("->last".equals(be2.getOperator()) && 
                (be.getArgument() + "").equals(be2.getArgument() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      } 
      else if ("->last".equals(be.operator))
      { for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof UnaryExpression)
          { UnaryExpression be2 = (UnaryExpression) amx.srcvalue; 
            if ("->front".equals(be2.getOperator()) && 
                (be.getArgument() + "").equals(be2.getArgument() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      }
    }
    return res; 
  } 

  Vector findMergeFamilySequence2(Vector ams, Vector seen)
  { // of form s->tail() or s->first(), etc
    Vector res = new Vector(); 
    res.add(this); 

    if (srcvalue != null && 
        (srcvalue instanceof UnaryExpression))
    { UnaryExpression be = (UnaryExpression) srcvalue; 
      if ("->first".equals(be.operator))
      { for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof UnaryExpression)
          { UnaryExpression be2 = (UnaryExpression) amx.srcvalue; 
            if ("->tail".equals(be2.getOperator()) && 
                (be.getArgument() + "").equals(be2.getArgument() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      } 
      else if ("->tail".equals(be.operator))
      { for (int i = 0; i < ams.size(); i++) 
        { AttributeMatching amx = (AttributeMatching) ams.get(i); 
          if (amx.isExpressionAssignment() && !(seen.contains(amx)) && 
              amx.srcvalue instanceof UnaryExpression)
          { UnaryExpression be2 = (UnaryExpression) amx.srcvalue; 
            if ("->first".equals(be2.getOperator()) && 
                (be.getArgument() + "").equals(be2.getArgument() + ""))
            { res.add(amx); 
              return res; 
            } 
          } 
        } 
      }
    }
    return res; 
  } 

  public boolean usedAsIntermediateClass(Entity e)
  { // E is the entity element type of some p : trg.path
    if (e == null) 
    { return false; } 

    String nme = e.getName(); 

    if (trg.isComposed())
    { Vector path = trg.getNavigation(); 
      for (int i = 0; i < path.size() - 1; i++) 
      { Attribute p = (Attribute) path.get(i); 
        if ((p.getElementType() + "").equals(nme))
        { return true; } 
      } 
    } 
    return false; 
  } 
    

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

  public boolean usedInSource(Attribute v)
  { if (v == src || v.equalByNameAndOwner(src) || v.equalToReverseDirection(src)) 
    { return true; } 

    if (src.isComposed())
    { Vector path = src.getNavigation(); 
      for (int i = 0; i < path.size(); i++) 
      { Attribute p = (Attribute) path.get(i); 
        if (v == p || v.equalByNameAndOwner(p))
        { return true; } 
      } 
      return false; 
    } 

    String nme = v.getName(); 

    if (srcvalue != null) 
    { if (srcvalue.hasVariable(nme))
      { return true; }
    }  

    return false; 
  } // or src is the reverse

  public double similarity(Map mm, Vector entities, Vector thesaurus) 
  { // NMS similarity between src and trg
    double namesim = ModelElement.similarity(srcname, trgname); 
    double namesemsim = Entity.nmsSimilarity(srcname, trgname, thesaurus); 
    double nsim = (namesim + 2*namesemsim - namesim*namesemsim); 
    double tsim = Attribute.partialTypeMatch(src,trg,mm,entities);
    return nsim*tsim;   
  } // should be namesemsim not 2*namesemsim

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

  public Vector inverts(Entity srcent, Entity trgent, Vector ems)
  { Vector res = new Vector(); 
    if (isStringAssignment())
    { // assume auxVariables.size() == 2
      // v1 + " ~ " + v2 --> v  inverts as 
      // v->before(" ~ ") --> v1, v->after(" ~ ") --> v2

      Expression delim = new BasicExpression("\" ~ \""); 

      if (srcvalue instanceof BinaryExpression)
      { BinaryExpression sbe = (BinaryExpression) srcvalue; 
        if ("+".equals(sbe.operator))
        { Expression lbe = sbe.getLeft(); 
          Expression rbe = sbe.getRight(); 
          // Attribute var1 = (Attribute) auxVariables.get(0); 
          // Attribute var2 = (Attribute) auxVariables.get(1); 
          if (rbe instanceof BinaryExpression && 
              "+".equals(((BinaryExpression) rbe).operator))  
          { delim = (BasicExpression) ((BinaryExpression) rbe).getLeft();
            System.out.println(">> Delimiter = " + delim); 
          } 
          else if (lbe instanceof BinaryExpression && 
                   "+".equals(((BinaryExpression) lbe).operator))
          { // (var1 + sep) + var2
            delim = ((BinaryExpression) lbe).getRight();
            System.out.println(">> Delimiter = " + delim); 
          } 
        } 
      } 
 

      delim.setType(new Type("String",null)); 
      delim.setElementType(new Type("String",null));
 
      BasicExpression etrg = new BasicExpression(trg); 
      etrg.setUmlKind(Expression.ATTRIBUTE); 
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
    { if ("self".equals(srcvalue + ""))
      { return null; } // inverted to condition instead 

      if (srcvalue instanceof SetExpression) 
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
          a1.setElementType(v1.getElementType()); 
          a2.setElementType(v2.getElementType()); 

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
          a1.setElementType(lft.getElementType()); 
          a2.setElementType(rgt.getElementType()); 

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
            // selfexp.setUmlkind(Expression.VARIABLE); 

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

      if (srcvalue instanceof BinaryExpression) 
      { BinaryExpression sbe = (BinaryExpression) srcvalue; 
        // elems->closure(next) --> parts  for ordered parts inverts as 
        // parts --> elems, next computation of parts/container --> next

        // System.out.println("INVERSE OF: " + srcvalue + " --> " + trg); 
        // System.out.println(trg.isOrdered() + " " + trg.hasOpposite() + " " +  
        //                    ((BasicExpression) sbe.right).variable + " " + 
        //                    ((BasicExpression) sbe.right).isZeroOneRole()); 

        if (sbe.operator.equals("->closure") && trg.isOrdered() &&  
            sbe.left instanceof BasicExpression && trg.hasOpposite() && 
            sbe.right instanceof BasicExpression && 
            ((BasicExpression) sbe.right).variable != null && 
            ((BasicExpression) sbe.right).isZeroOneRole()) 
        { BasicExpression lft = (BasicExpression) sbe.left; 
          BasicExpression rgt = (BasicExpression) sbe.right;
          String role1 = trg.getRole1(); // container 
          Attribute newrhs1 = new Attribute(sbe.left + "", sbe.left.getType(), 
                                            ModelElement.INTERNAL); // elems
          newrhs1.setElementType(sbe.left.getElementType()); 
          AttributeMatching newam = new AttributeMatching(trg,newrhs1);
          res.add(newam);   

          Type targetref = src.getElementType(); 
          if (targetref != null && targetref.isEntity())
          { Entity trgref = targetref.getEntity(); 
            BasicExpression ownerparts = new BasicExpression(trg);
            BasicExpression ownr = new BasicExpression(role1);
            ownerparts.setUmlKind(Expression.ATTRIBUTE); 
            ownr.setUmlKind(Expression.ATTRIBUTE); 
            ownerparts.setObjectRef(ownr);
            BasicExpression selfexp = new BasicExpression("self");
            // should have umlkind = VARIABLE

            selfexp.setType(new Type(trgref));
            selfexp.setElementType(new Type(trgref));
            BinaryExpression selfindex = new BinaryExpression("->indexOf", ownerparts, selfexp);
            UnaryExpression ownerpartssize = new UnaryExpression("->size",ownerparts);
            BinaryExpression test = new BinaryExpression("<", selfindex, ownerpartssize);
            BinaryExpression nextindex = new BinaryExpression("+",selfindex,new BasicExpression(1));
            BinaryExpression ownerpartsat = new BinaryExpression("->at", ownerparts, nextindex);
            SetExpression emptyset = new SetExpression();
            SetExpression nextset = new SetExpression();
            nextset.addElement(ownerpartsat);
            ConditionalExpression newlhs = new ConditionalExpression(test,nextset,emptyset);
            Type sourceref = trg.getElementType(); 
            if (sourceref != null && sourceref.isEntity())
            { Entity srcref = sourceref.getEntity(); 
              Type restype = new Type("Set", null);
              restype.setElementType(new Type(srcref)); // not trgref
              ownerparts.setType(restype);
              ownerparts.setElementType(new Type(srcref));

              newlhs.setType(restype);
              newlhs.setElementType(new Type(srcref));
              newlhs.setBrackets(true); 

              AttributeMatching newatm =  
                    new AttributeMatching(newlhs,rgt.variable);
              Attribute varnew = new Attribute(Identifier.nextIdentifier("var$"),
                                          newlhs.getElementType(),
                                          ModelElement.INTERNAL);
              varnew.setElementType(newlhs.getElementType()); 
              newatm.setElementVariable(varnew); 

              EntityMatching bb1 = ModelMatching.getRealEntityMatching(srcref,trgref,ems); 
              if (bb1 != null) 
              { bb1.addAttributeMatch(newatm); } 
            } 
          } 

          return res; 
        } 
		else if (sbe.operator.equals("->unionAll") && sbe.right instanceof BasicExpression &&
                 sbe.left instanceof BinaryExpression &&
                 ((BinaryExpression) sbe.left).operator.equals("->closure"))
        { BinaryExpression clsre = (BinaryExpression) sbe.left;
          BasicExpression f = (BasicExpression) sbe.right;
          Expression g = clsre.right;
          BasicExpression r = new BasicExpression(trg);
          Type Rtype = r.getElementType();
          BasicExpression Fclass = new BasicExpression(trgent);
          Type Ftype = new Type(trgent);
          String avalue = Identifier.nextIdentifier("var$");
          BasicExpression ax = new BasicExpression(avalue);
          ax.setType(Ftype);
          ax.setElementType(Ftype);
          BinaryExpression aInFclass = new BinaryExpression(":", ax, Fclass);
          BasicExpression ar = new BasicExpression(trg);
          ar.setObjectRef(ax);
          BinaryExpression aAncestor = new BinaryExpression("->includesAll", r, ar);
          BinaryExpression aStrict = new BinaryExpression("/=", r, ar);
          BinaryExpression aStrictAncestor = new BinaryExpression("&",aAncestor,aStrict);
          BinaryExpression ancestors = new BinaryExpression("|", aInFclass, aStrictAncestor);
          // ancestors->selectMaximals(r.size) |--> g
          // r - ancestors->unionAll(r) |--> f
          UnaryExpression rsize = new UnaryExpression("->size", r);
          BinaryExpression lhsgmap = new BinaryExpression("->selectMaximals", ancestors, rsize);
		  Type FSetType = new Type("Set",null); 
		  FSetType.setElementType(Ftype); 
		  lhsgmap.setType(FSetType); 
		  lhsgmap.setElementType(Ftype); 
          AttributeMatching gmatch = new AttributeMatching(lhsgmap,g);
          Attribute gmatchvar = new Attribute(Identifier.nextIdentifier("var$"), Ftype, ModelElement.INTERNAL);
          gmatchvar.setElementType(Ftype);
          gmatch.setElementVariable(gmatchvar);
          res.add(gmatch);
          BinaryExpression ancestorsUnionAllr = new BinaryExpression("->unionAll", ancestors, r);
          BinaryExpression fmatchlhs = new BinaryExpression("-", r, ancestorsUnionAllr);
		  Type FRefSetType = new Type("Set",null); 
		  FRefSetType.setElementType(Rtype); 
		  fmatchlhs.setType(FRefSetType); 
		  fmatchlhs.setElementType(Rtype);
          AttributeMatching fmatch = new AttributeMatching(fmatchlhs,f); 
          Attribute fmatchvar = new Attribute(Identifier.nextIdentifier("var$"), Rtype, ModelElement.INTERNAL);
          fmatchvar.setElementType(Rtype);
          fmatch.setElementVariable(fmatchvar);
          res.add(fmatch);
          return res;
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

  public static Vector invertCondition(Expression cond, Entity realsrc, Entity realtrg) 
  { Vector res = new Vector(); 
    if (cond instanceof BinaryExpression)
    { BinaryExpression condbe = (BinaryExpression) cond; 
      
      if (condbe.operator.equals("="))
      { // feature = value inverts as value --> feature
        if ((condbe.left.umlkind == Expression.VARIABLE ||
             condbe.left.umlkind == Expression.ATTRIBUTE) &&
            condbe.left instanceof BasicExpression && 
            ((BasicExpression) condbe.left).variable != null &&  
            condbe.right.umlkind != Expression.VARIABLE &&
            condbe.right.umlkind != Expression.ATTRIBUTE)
        { Attribute trgfeature = ((BasicExpression) condbe.left).variable; 
          Attribute var = null; 

          // System.out.println(">> Target feature is " + trgfeature + 
          //                    " " + trgfeature.getType()); 
          Expression newleft = null; 
          if ((condbe.right + "").equals("self"))
          { newleft = new BasicExpression("self"); 
            newleft.setType(new Type(realtrg)); 
            newleft.setElementType(new Type(realtrg)); 
            var = new Attribute("self",
                                        new Type(realtrg),
                                        ModelElement.INTERNAL);
            var.setElementType(new Type(realtrg));
          }  
          else 
          { newleft = condbe.right; 
            var = new Attribute(Identifier.nextIdentifier("var$"),
                                        condbe.right.getType(),
                                        ModelElement.INTERNAL);
            var.setElementType(condbe.right.getElementType());
          } 
          AttributeMatching newam = new AttributeMatching(newleft,trgfeature); 
          newam.setElementVariable(var); 
          res.add(newam);
        } 
        else if ((condbe.right.umlkind == Expression.VARIABLE ||
                  condbe.right.umlkind == Expression.ATTRIBUTE) &&
                 condbe.right instanceof BasicExpression && 
                 ((BasicExpression) condbe.right).variable != null &&  
                 condbe.left.umlkind != Expression.VARIABLE &&
                 condbe.left.umlkind != Expression.ATTRIBUTE)
        { Attribute trgfeature = ((BasicExpression) condbe.right).variable; 
          Attribute var = null; 

          // System.out.println(">> Target feature is " + trgfeature + 
          //                    " " + trgfeature.getType());  
          // trgfeature.setUmlKind(Expression.ATTRIBUTE); 
          Expression newleft = null; 
          if ((condbe.left + "").equals("self"))
          { newleft = new BasicExpression("self"); 
            newleft.setType(new Type(realtrg)); 
            newleft.setElementType(new Type(realtrg)); 
            var = new Attribute("self",
                                        new Type(realtrg),
                                        ModelElement.INTERNAL);
            var.setElementType(new Type(realtrg));
          }  
          else 
          { newleft = condbe.left; 
            var = new Attribute(Identifier.nextIdentifier("var$"),
                                        condbe.left.getType(),
                                        ModelElement.INTERNAL);
            var.setElementType(condbe.left.getElementType());
          } 
          AttributeMatching newam = new AttributeMatching(newleft,trgfeature); 
          newam.setElementVariable(var); 
          res.add(newam);
        } 
      }
      else if (condbe.operator.equals("&"))
      { Expression cond1 = condbe.left; 
        Expression cond2 = condbe.right; 
        res.addAll(invertCondition(cond1,realsrc,realtrg)); 
        res.addAll(invertCondition(cond2,realsrc,realtrg)); 
      } 
    } 
    return res; 
  }   // cases of f->includes(self)  
        
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

    // In the case of entity types, (selemt.entity, telemt.entity) : mymap 

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
 
    if (stype.isCollectionType())
    { return sexp; } 

    if (ttype.isCollectionType())
    { return sexp; } 

    return new BinaryExpression("->oclAsType", sexp, new BasicExpression(ttype)); 
  } 

  public Vector analyseCorrelationPatterns(Entity s, Entity t, Entity realsrc, Entity realtrg, 
                                           EntityMatching ematch, Vector ems, ModelMatching modmatch,
                                           Vector entities) 
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
              JOptionPane.showInputDialog("Replace " + trgent + " by? (name/null):");
          if (ans != null && !("null".equals(ans))) 
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
      { optionalToTotal(realtrgsup, realsrc, realtrg, 
                        ematch, entities, 
                        modmatch, res); 
      }    
	  else
	  { multiplicityReduction(realsrc, realtrg); }
        
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
      
	  // Suggest a source splitting if possible. 
      Entity realtrgsup = realtrg.getSuperclass(); 
      if (realtrgsup != null) 
      { optionalToTotal(realtrgsup, realsrc, realtrg, 
                        ematch, entities, 
                        modmatch, res); 
      }
	  
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
	  multiplicityReduction(realsrc, realtrg); 

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
      
	  // Suggest a source splitting if possible. 
      Entity realtrgsup = realtrg.getSuperclass(); 
      if (realtrgsup != null) 
      { optionalToTotal(realtrgsup, realsrc, realtrg, 
                        ematch, entities, 
                        modmatch, res); 
      }
	  
	  q.addSourceEntity(realsrc); 
      q.addTargetEntity(realtrg); 
      q.addSourceFeature(src); 
      q.addTargetFeature(trg); 
      q.setWarning(true); 
      if (res.contains(q)) { } else { res.add(q); }  
    } 

     
    return res; 
  } // also feature merging; stereotype changes

  private void optionalToTotal(Entity realtrgsup, Entity realsrc, Entity realtrg, 
                               EntityMatching ematch, Vector entities, 
                               ModelMatching modmatch, Vector res) 
  { Vector siblings = new Vector(); 
    siblings.addAll(realtrgsup.getSubclasses());
    siblings.remove(realtrg);
    if (siblings.size() == 1) 
    { UnaryExpression srcsize = new UnaryExpression("->size",new BasicExpression(src)); 
      Expression guard0 = new BinaryExpression("=",srcsize,new BasicExpression(1));
      Expression guard1 = new BinaryExpression("/=",srcsize,new BasicExpression(1)); 
      Entity othere = (Entity) siblings.get(0); 
      EntityMatching newothermatch = new EntityMatching(realsrc,othere); 
      newothermatch.addCondition(guard1); 
      newothermatch.copyApplicableAttributeMappings(ematch.attributeMappings); 
      System.out.println(">>> Suggest splitting based on cases of " + src + 
                             " size (1 or not)"); 
      String ans = 
              JOptionPane.showInputDialog("Add " + newothermatch + " to entity matches?: (y/n) ");
      if (ans != null && "y".equals(ans))
      { // ems.add(newothermatch);
        modmatch.addEntityMatch(newothermatch,entities);  
        ematch.addCondition(guard0); 
        CorrelationPattern qq = new CorrelationPattern("Conditioned class splitting", 
                                          "Class " + realsrc + " split to " + realtrg + ", " + 
                                          othere); 
        qq.addSourceEntity(realsrc); 
        qq.addTargetEntity(realtrg); 
        qq.addTargetEntity(othere); 
        if (res.contains(qq)) { } 
        else 
        { res.add(qq); } 
      } 
    } 
  } 
  
  public void multiplicityReduction(Entity realsrc, Entity realtrg)
  { // src is *, trg is not
    Type elemT = src.getElementType();  
    if (elemT != null && elemT.isNumeric())
	{ System.out.println(">>> Suggest replacing " + src + " |--> " + trg); 
	  System.out.println("with reduction such as " + src + "->sum()  |-->  " + trg); 
    }
	else if (elemT != null && elemT.isString())
	{ System.out.println(">>> Suggest replacing " + src + " |--> " + trg); 
	  System.out.println("with reduction such as " + src + "->sum()  |-->  " + trg);
    }
	else if (src.isOrdered())
	{ System.out.println(">>> Suggest replacing " + src + " |--> " + trg); 
	  System.out.println("with reduction such as " + src + "->first()  |-->  " + trg); 
	  System.out.println("or " + src + "->last()  |-->  " + trg); 
	}
	else 
	{ System.out.println(">>> Suggest replacing " + src + " |--> " + trg); 
	  System.out.println("with reduction such as " + src + "->any()  |-->  " + trg);
	  System.out.println("or instance replication { x : " + src + " } " + realsrc + " |--> " + realtrg); 
	  System.out.println("                                          x |-->  " + trg);
    } 
	System.out.println(); 
  }

  public boolean isEquality() 
  { // types are the same 
    Type tsrc = src.getType(); 
    Type ttrg = trg.getType();
    if ((tsrc + "").equals(ttrg + "")) 
    { return true; } 
    return false; 
  } 

  public EntityMatching isObjectMatch(Vector ems, Vector possibles) 
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
    //                     ")"); 

    if (Type.isEntityType(tsrc) && Type.isEntityType(ttrg))
    { Entity e1 = tsrc.getEntity(); 
      Entity e2 = ttrg.getEntity();

      EntityMatching emx = ModelMatching.findEntityMatchingFor(e1,e2,ems); 
      if (emx != null)
      { return emx; } 
        // to map from e1 to e2, emx.realsrc must be e1 or an ancestor of e1,
        // emx.realtrg must be e2 or a descendent of e2
      else 
      { Vector e1leafs = e1.getActualLeafSubclasses(); 
        Vector e2leafs = e2.getActualLeafSubclasses(); 
        for (int i = 0; i < e1leafs.size(); i++) 
        { Entity e1sub = (Entity) e1leafs.get(i); 
          for (int j = 0; j < e2leafs.size(); j++) 
          { Entity e2sub = (Entity) e2leafs.get(j); 
            EntityMatching emy = ModelMatching.getRealEntityMatching(e1sub,e2sub,ems); 
            if (emy != null) 
            { possibles.add(emy); }
          }
        }
      }
    }  
    return null; 
  } 

  String whenClause(EntityMatching emx, String srcroot, String trgroot, Map whens) 
  { // isObjectMatching is true
    if (emx == null) 
    { return ""; }
	
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
    String nsrc = emx.src + ""; 
    String ntrg = emx.trg + ""; 
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

  String whenClause(EntityMatching emx, String srcvar, String srcroot, String trgroot, Map whens) 
  { // isObjectMatching is true
    if (emx == null) 
    { return ""; }
	
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
    String nsrc = emx.src + ""; 
    String ntrg = emx.trg + ""; 
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

    // String d1 = dataname(srcroot,spath); 
    String d2 = dataname(trgroot,tpath); 

    /* BasicExpression be1 = new BasicExpression(d1); 
    be1.setType(src.getType()); 
    if (emx.realsrc != null) 
    { be1.setElementType(new Type(emx.realsrc)); }  
    be1.variable = src; 

    BasicExpression be2 = new BasicExpression(d2); 
    be2.setType(trg.getType()); 
    if (emx.realtrg != null) 
    { be2.setElementType(new Type(emx.realtrg)); } 
    be2.variable = trg; */ 

    // whens.set(be2,be1); 
    return srcentx + "2" + trgentx + "(" + srcvar + "," + d2 + ")"; 
  } 

  String whenClause(Vector possibles, String srcroot, String trgroot, Map whens) 
  { // isObjectMatching is true, with a list of matches. 
    String res = ""; 

    Vector spath = new Vector(); 
    spath.addAll(src.getNavigation()); 
    if (spath.size() == 0) 
    { spath.add(src); } 
    Vector tpath = new Vector(); 
    tpath.addAll(trg.getNavigation()); 
    if (tpath.size() == 0) 
    { tpath.add(trg); }
 
    String d1 = dataname(srcroot,spath); 
    String d2 = dataname(trgroot,tpath); 

    BasicExpression be1 = new BasicExpression(d1); 
    be1.setType(src.getType()); 
    be1.variable = src; 

    BasicExpression be2 = new BasicExpression(d2); 
    be2.setType(trg.getType()); 
    be2.variable = trg; 

    whens.set(be2,be1); 

    // Attribute s1 = (Attribute) spath.get(0); 
    // Attribute t1 = (Attribute) tpath.get(0); 
    // Type tsrc = src.getElementType(); 
    // Type ttrg = trg.getElementType(); 
    for (int i = 0; i < possibles.size(); i++) 
    { EntityMatching emx = (EntityMatching) possibles.get(i); 
      String nsrc = emx.src.getName(); 
      String ntrg = emx.trg.getName(); 
      String srcentx = nsrc; 
      if (nsrc.endsWith("$"))
      { srcentx = nsrc.substring(0,nsrc.length()-1); } 
      if (emx.realsrc != null) 
      { srcentx = emx.realsrc.getName(); }

      if (emx.realsrc != null) 
      { be1.setElementType(new Type(emx.realsrc)); }  
      if (emx.realtrg != null) 
      { be2.setElementType(new Type(emx.realtrg)); } 
    
      String trgentx = ntrg; 
      if (ntrg.endsWith("$"))
      { trgentx = ntrg.substring(0,ntrg.length()-1); }  
      if (emx.realtrg != null) 
      { trgentx = emx.realtrg.getName(); } 

      res = res + srcentx + "2" + trgentx + "(" + d1 + "," + d2 + ")"; 
      if (i < possibles.size() - 1)
      { res = res + " or "; } 
    } 
    return res; 
  } 

  String whenClause(String trgroot, String srcobj, Vector ems, Map whens) 
  { // For expr --> trg, need E2F(elementVariable,d2) 

    // System.out.println(">>> Element variable of " + this + " is>> " + elementVariable); 
		
    if (elementVariable == null) 
    { return "true"; } 

    Vector tpath = new Vector(); 
    tpath.addAll(trg.getNavigation()); 
    if (tpath.size() == 0) 
    { tpath.add(trg); } 

    Type vtype = elementVariable.getType(); 
	
    if (vtype != null && vtype.isEntity())
    { Entity vent = vtype.getEntity(); 
      // System.out.println(">>> Entity element type of " + elementVariable + " is " + vent); 

      Type ttype = trg.getElementType(); 
      if (ttype != null && ttype.isEntity())
      { Entity tent = ttype.getEntity();
         
        EntityMatching emx = ModelMatching.findEntityMatchingFor(vent,tent,ems); 

        // System.out.println(">>> Entity match of " + elementVariable + " to " + tent + " is " + emx);
 
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
          if ("self".equals(elementVariable + ""))
          { return srcentx + "2" + trgentx + "(" + srcobj + "," + d2 + ")"; } 
          return srcentx + "2" + trgentx + "(" + elementVariable + "," + d2 + ")"; 
        } 
      } 
    } 
    return "true"; 
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

  String targetequationbx(BasicExpression srcx, BasicExpression trgx, 
                          String prefix,String srcdata,ObjectTemplateExp tExps,Vector whereclause)
  { // trgx.nme = srcx.nme and srcx.nme = trgx.nme for non-objects, non-collections


    Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0) 
    { path.add(trg); }
    return targetequationbx(srcx,trgx,prefix,path,srcdata,tExps,whereclause); 
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

      if (src == null || src.getType() == null) 
      { System.err.println("!! ERROR: null type in " + src); } 
      else if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
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

        if (tent == null) 
        { System.err.println("!! ERROR: no entity type for " + p); 
          return ""; 
        } 

        Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 
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

      if (src == null || src.getType() == null) 
      { System.err.println("!! ERROR: null type in " + src); } 
      else if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("String".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      if (p.getType().isEnumeration() && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("boolean".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("boolean".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 

      tExp.addPTI(p,valueExpC);     
      return pname + " = " + valueExpC;
    } 
    return "";  
  } // and boolean -> enum data conversions

  String targetequationbx(BasicExpression srcx, BasicExpression trgx, 
                          String prefix, Vector path, String srcdata, 
						  ObjectTemplateExp tExp, Vector whereclause)
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
      BasicExpression valueExp = new BasicExpression(src);
      valueExp.setObjectRef(srcx);  
      valueExp.setType(src.getType()); 
      valueExp.setElementType(src.getElementType()); 
      valueExp.variable = src; 

      BasicExpression tvalueExp = new BasicExpression(trg);
      tvalueExp.setObjectRef(trgx);  
      tvalueExp.setType(trg.getType()); 
      tvalueExp.setElementType(trg.getElementType()); 
      tvalueExp.variable = trg; 
      

      Expression valueExpC = valueExp; 

      if (src == null || src.getType() == null) 
      { System.err.println("!! ERROR: null type in " + src); } 
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

      Expression tvalueExpC = tvalueExp; 

      if (trg == null || trg.getType() == null) 
      { System.err.println("!! ERROR: null type in " + trg); } 
      if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
      { tvalueExpC = AttributeMatching.dataConversion("QVTR", tvalueExp, p.getType(), src.getType()); } 
      else if ("String".equals(src.getType() + "") && p.getType().isEnumeration())
      { tvalueExpC = AttributeMatching.dataConversion("QVTR", tvalueExp, p.getType(), src.getType()); } 
      else if (p.getType().isEnumeration() && src.getType().isEnumeration())
      { tvalueExpC = AttributeMatching.dataConversion("QVTR", tvalueExp, p.getType(), src.getType()); } 
      else if (src.getType().isBoolean() && p.getType().isEnumeration())
      { tvalueExpC = AttributeMatching.dataConversion("QVTR", tvalueExp, p.getType(), src.getType()); } 
      else if (p.getType().isBoolean() && src.getType().isEnumeration())
      { tvalueExpC = AttributeMatching.dataConversion("QVTR", tvalueExp, p.getType(), src.getType()); } 

      // tExp.addPTI(p,valueExpC);     
	  
      whereclause.add(tvalueExp + " = " + valueExpC + " and " + valueExp + " = " + tvalueExpC); 
      return tvalueExp + " = " + valueExpC + " and " + valueExp + " = " + tvalueExpC; 
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
        body = targetequationbx(srcx,trgx,prefix + "_" + pname, pathtail, srcdata, subTE,whereclause); 
      } 
      else 
      { String obj = fullname + "$x"; 

        if (tent == null) 
        { System.err.println("!! ERROR: no entity type for " + p); 
          return ""; 
        } 

        Attribute objroot = new Attribute(obj,new Type(tent),ModelElement.INTERNAL); 
        objroot.setElementType(new Type(tent)); 

        ObjectTemplateExp newSubTE = new ObjectTemplateExp(objroot,tent); 
        tExp.addPTI(p,newSubTE);     
        body = targetequationbx(srcx,trgx,prefix + "_" + pname, pathtail, srcdata, newSubTE,whereclause); 
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

      if (src == null || src.getType() == null) 
      { System.err.println("!! ERROR: null type in " + src); } 
      else if ("String".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("String".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      if (p.getType().isEnumeration() && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("boolean".equals(src.getType() + "") && p.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 
      else if ("boolean".equals(p.getType() + "") && src.getType().isEnumeration())
      { valueExpC = AttributeMatching.dataConversion("QVTR",valueExp, src.getType(), p.getType()); } 

      tExp.addPTI(p,valueExpC);     
      return pname + " = " + valueExpC;
    } 
    return "";  
  } // and boolean -> enum data conversions

  String targetequationUMLRSDS(String tvar, Vector path, Vector bound) 
  { Type ttarg = trg.getType(); 
    Type tsrc = src.getType(); 
	
	// We assume that there is never a need to lookup intermediate objects via primary key; they are always accessed through 
	// the reference pointing to them. Eg., activityx.sourceEvent in the Gantt2CPM example. 
	// Thus lookups always use the unmodified source key attribute. But creation of intermediate objects assigns a new target
	// key value based on the source instance key & on the accessing reference name, to ensure uniqueness.

    String res = ""; 
    if (ttarg == null) 
    { System.err.println("!! ERROR: " + trg + " has no type"); 
      return res; 
    } 
	
    if (tsrc == null) 
    { System.err.println("!! ERROR: " + src + " has no type"); 
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
          
          Attribute srcpk = sentity.getPrincipalPK(); 
          if (srcpk != null) 
          { sId = srcpk + ""; }
		   
          String srclookup = tename + "@pre[" + src + "." + sId + "]";  // src is a feature 

          
          if (multsrc != ModelElement.ONE && multtrg == ModelElement.ONE)
          { srclookup = tename + "@pre[" + src + "->collect(" + sId + ")]";
            res = tvar + "." + trg + " = " + srclookup + "->any()"; 
          } 
          else if (multsrc == ModelElement.ONE && multtrg != ModelElement.ONE)
          { res = srclookup + " : " + tvar + "." + trg; }  
          else if (multsrc != ModelElement.ONE && multtrg != ModelElement.ONE && unionSemantics)
          { res = srclookup + " <: " + tvar + "." + trg; }
          else if (src.getType() != null && 
                   "Sequence".equals(t.getName()) && "Set".equals(src.getType().getName()))
          { res = tvar + "." + trg + " = " + srclookup + "->asSequence()"; } 
          else if (src.getType() != null && 
                   "Set".equals(t.getName()) && "Sequence".equals(src.getType().getName()))
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
        { System.err.println("!! ERROR: source is not of entity type/element type: " + tsrc); 
          return ""; 
        } // Maybe allow strings to be mapped to instances of an entity with a String-primary key

        String sename = sentity.getName(); 
        String sId = sename.toLowerCase() + "Id"; 

        Attribute srcpk = sentity.getPrincipalPK(); 
        if (srcpk != null) 
        { sId = srcpk + ""; } 

        String srclookup = tename + "@pre[" + src + "." + sId + "]";  // src is a feature 
        
        if (multsrc != ModelElement.ONE && multtrg == ModelElement.ONE)
        { srclookup = tename + "@pre[" + src + "->collect(" + sId + ")]";
          res = tvar + "." + trg + " = " + srclookup + "->any()"; 
        } 
        else if (multsrc != ModelElement.ONE && multtrg != ModelElement.ONE && unionSemantics)
        { res = srclookup + " <: " + tvar + "." + trg; }
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
 
  // For UML-RSDS: 
  String composedTargetEquation(Entity realsrc, String tvar, Vector created)
  { Vector path = new Vector(); 
    path.addAll(trg.getNavigation()); 
    if (path.size() == 0)
    { path.add(trg); } 
    Expression srcexp = new BasicExpression(src); 

	// We assume that there is never a need to lookup intermediate objects via primary key; they are always accessed through 
	// the reference pointing to them. Eg., activityx.sourceEvent in the Gantt2CPM example. 
	// Thus lookups always use the unmodified source key attribute. But creation of intermediate objects assigns a new target
	// key value based on the source instance key & on the accessing reference name, to ensure uniqueness.

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
      // System.out.println("PATH=" + pathrem + " " + preattp); 
      // pathrem.removeAll(preattp); 
      // System.out.println("PATHREM=" + pathrem); 
      for (int xx = 0; xx < preattp.size(); xx++)
      { String xxs = ((Attribute) preattp.get(xx)).getName(); 
        pathrem = VectorUtil.removeByName(pathrem,xxs); 
      } // Does this make sense?

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
      Attribute bpk = null; 

      if (Type.isEntityType(stype))
      { b = stype.getEntity(); 
        bname = b.getName(); 
        // sname = bname.substring(0,bname.length()-1);  // source feature element type
        bpk = b.getPrincipalPK(); 
      }  
      String bId = bname.toLowerCase() + "Id"; 
      if (bpk != null) 
      { bId = bpk.getName(); } 
      String evar = ename.toLowerCase() + "$x"; 
      String evardec = ""; 

      if (Type.isCollectionType(src.getType()) && b != null)
      { evardec = " & " + evar + " : " + ename + "@pre[" + src + "->collect(" + bId + ")] => \n"; } 
      else if (b != null) 
      { evardec = " & " + evar + " = " + ename + "@pre[" + src + "." + bId + "] => \n"; }
      else if (Type.isCollectionType(src.getType()))
      { evardec = " & " + evar + " : " + src + " => \n"; } 
      else  
      { evardec = " & " + evar + " = " + src + " => \n"; }

      if (alreadycreated)
      { // System.out.println("* source: " + srcexp.isMultipleValued() + " * " + preatt + " " + 
        //                    multiple + " * " + pathrem + " " + 
        //                    Attribute.isMultipleValued(pathrem)); 
      
        if (multiple) 
        { if (srcexp.isMultipleValued() && 
              !Attribute.isMultipleValued(pathrem))
          { // ignore the existing elements in tvar1 and create new ones for the new srcexp. 

            String cteq = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem);
            if (cteq.length() > 0) 
            { return evardec + "      " + preattD + "->exists( _y | " + 
                               " _y : " + tvar1 + " & " + cteq + " )"; 
            } 
            return "";                
          } 
          else 
          { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
            if (cteq0.length() > 0) 
            { return evardec + "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; } 
            return "";
          }  
        } 
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return evardec + "      " + cteq; } 
          return "";                
        } 
      } 
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return evardec + "      " + cteq1; } 
      return "";  
    } 
    else // target is of a value type 
    if (Type.isCollectionType(src.getType()))
    { String evar = Identifier.nextIdentifier("var$"); 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " & " + evar + " : " + src + " =>\n" + 
                   "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; 
          } 
          return ""; 
        } 
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return " & " + evar + " : " + src + " =>\n" + 
                   "      " + cteq; 
          } 
          return "";
        } 
      } 
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " & " + evar + " : " + src + " =>\n" + 
                "      " + cteq1; 
      } 
      return "";  
    } 
    else 
    { String evar = src + ""; 
      if (alreadycreated)
      { // System.out.println("Valuetype * source: " + srcexp.isMultipleValued() + " * " + preatt + " " + 
        //                    multiple + " * " + pathrem + " " + 
        //                    Attribute.isMultipleValued(pathrem)); 
		
        if (multiple) 
        { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " =>\n" + 
                   "       " + tvar1 + "->forAll( _y | " + cteq0 + " )";
          } 
          return ""; 
        }
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0)
          { return " =>\n" + 
               "       " + cteq;
          } 
          return ""; 
        }
      }  
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " =>\n" + "       " + cteq1; } 
      return "";  
    } 
    // return tvar + "." + trg + " = " + src;  
  } 

  String composedTargetEquationExpr(Entity realsrc, Expression srcexp, String tvar, Vector created)
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
      // pathrem.removeAll(preattp); 
      for (int xx = 0; xx < preattp.size(); xx++)
      { String xxs = ((Attribute) preattp.get(xx)).getName(); 
        pathrem = VectorUtil.removeByName(pathrem,xxs); 
      } 
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
      Attribute bpk = null; 

      if (Type.isEntityType(stype))
      { b = stype.getEntity(); 
        bname = b.getName(); 
         // source expression element type
        bpk = b.getPrincipalPK(); 
      }  
      String bId = bname.toLowerCase() + "Id"; 
      if (bpk != null) 
      { bId = bpk.getName(); } 
      String evar = ename.toLowerCase() + "$x"; 
      String evardec = ""; 

      if (Type.isCollectionType(srctype) && b != null)
      { evardec = " & " + evar + " : " + ename + "@pre[(" + srcexp + ")->collect(" + bId + ")] => \n"; } 
      else if (b != null) 
      { evardec = " & " + evar + " = " + ename + "@pre[(" + srcexp + ")." + bId + "] => \n"; }
      else if (Type.isCollectionType(srctype))  
      { evardec = " & " + evar + " : " + srcexp + " => \n"; }
      else 
      { evardec = " & " + evar + " = " + srcexp + " => \n"; }
      
      
      if (alreadycreated)
      { if (multiple) 
        { if (srcexp.isMultipleValued() && 
              !Attribute.isMultipleValued(pathrem))
          { // ignore the existing elements in tvar1 and create new ones for the new srcexp. 

            String cteq = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem);
            if (cteq.length() > 0) 
            { return evardec + "      " + preattD + "->exists( _y | " + 
                               " _y : " + tvar1 + " & " + cteq + " )"; 
            } 
            return "";                
          } 
          else 
          { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
            if (cteq0.length() > 0) 
            { return evardec + "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; } 
            return "";
          }  
        } 
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return evardec + "      " + cteq; } 
          return "";                
        } 
      } 
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return evardec + "      " + cteq1; } 
      return "";  
    } 
    else // target is of a value type 
    if (Type.isCollectionType(srctype))
    { String evar = Identifier.nextIdentifier("var$"); 
      if (alreadycreated)
      { if (multiple) 
        { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " & " + evar + " : " + srcexp + " =>\n" + 
                   "      " + tvar1 + "->forAll( _y | " + cteq0 + " )"; 
          } 
          return ""; 
        } 
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0) 
          { return " & " + evar + " : " + srcexp + " =>\n" + 
                   "      " + cteq; 
          } 
          return "";
        } 
      } 
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
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
        { String cteq0 = composedTargetEquation(realsrc,srcexp,"_y",evar,pathrem); 
          if (cteq0.length() > 0) 
          { return " =>\n" + 
                   "       " + tvar1 + "->forAll( _y | " + cteq0 + " )";
          } 
          return ""; 
        }
        else 
        { String cteq = composedTargetEquation(realsrc,srcexp,tvar1,evar,pathrem);
          if (cteq.length() > 0)
          { return " =>\n" + 
               "       " + cteq;
          } 
          return ""; 
        }
      }  
      String cteq1 = composedTargetEquation(realsrc,srcexp,tvar,evar,path);
      if (cteq1.length() > 0)
      { return " =>\n" + "       " + cteq1; } 
      return "";  
    } 
    // return tvar + "." + trg + " = " + src;  
  } 

  public String composedTargetEquation(Entity realsrc, Expression srcexp, String tvar, String evar, Vector path) 
  { // System.out.println("CTEq: source: " + srcexp + " tvar " + tvar + " evar " + 
    //                        evar + " path: " + path); 
      
    if (path.size() == 1) 
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
      else if (t.isBoolean() && tsrc.isEnumeration())
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
      String code = composedTargetEquation(realsrc,srcexp,ex,evar,ptail);
      if (code.length() > 0) 
      { code = " & " + code; } 
 
      if (Type.isCollectionType(t))
      { code = ex + " : " + tvar + "." + p.getName() + code; } 
      else 
      { code = tvar + "." + p.getName() + " = " + ex + code; }
	  
      String srcid = realsrc.getName().toLowerCase() + "Id";  
      String pksettings = e.primaryKeySettings(ex, srcid + " + \"~" + p.getName() + "\"");  
      return ename + "->exists( " + ex + " | " + pksettings + code + " ) "; 
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
      for (int xx = 0; xx < preattp.size(); xx++)
      { String xxs = ((Attribute) preattp.get(xx)).getName(); 
        pathrem = VectorUtil.removeByName(pathrem,xxs); 
      } 
      // pathrem.removeAll(preattp); 
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

          EntityMatching em = ModelMatching.findEntityMatchingFor(s,tent,ems);
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
          
          EntityMatching em = ModelMatching.findEntityMatchingFor(s,tent,ems); 
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

    if (src.getUpper() == 1 && src.getLower() == 0) 
    { String testnull = "if (" + svar + " = null) then {} else { " + 
                        res + " } endif"; 
      return testnull; 
    }     

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
	  if (t != null && t.isConcrete()) { } 
	  else 
	  { System.err.println("!! Cannot instanciate owner " + t + " of " + p); 
	    t = previous.getElementType().getEntity(); 
	  }
	  
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
        if (selem.isEntity() && pent != null)
        { EntityMatching emx = ModelMatching.findEntityMatchingFor(selem.getEntity(), pent, ems); 
          String desttype = pent.getName(); 
          if (emx != null) 
          { // if (pent != null) 
            // { desttype = pent.getName(); }  
            // else 
            // { desttype = emx.realtrg.getName(); } 
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
          EntityMatching em = ModelMatching.findEntityMatchingFor(s,pent,ems); 
          // System.out.println("Entity matching for " + s + " is " + em); 

          if (em != null) 
          { String res = pname + " " + pop + " " + svar + "." + 
                 "resolveoneIn(" + em.realsrc + "::" + em.realsrc + "2" + em.realtrg + ", " +
                 pent.getName() + ");"; 
            result = tvar + " " + operator + " object " + t + " { " + res + " };";
          }
          else 
          { System.err.println(">>> no entity matching for " + s + " Incomplete mapping."); 
            String res = pname + " " + pop + " " + svar + "." + "resolveone();"; 
            result = tvar + " " + operator + " object " + t + " { " + res + " };";
          } 
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
	  
	  if (t != null && t.isConcrete()) { }
	  else 
	  { System.err.println("!! Cannot create instance of owner " + t + " of " + p); 
	    t = previous.getElementType().getEntity(); 
	  }

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



  String atldirecttarget(String svar, Vector ems) 
  { Expression sexp = // new BasicExpression(svar + "." + src);
                      src.atlComposedExpression(svar,trg,ems); 
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

  Binding atldirectbinding(String svar, Vector ems) 
  { Expression sexp = // new BasicExpression(svar + "." + src);
                      src.atlComposedExpression(svar,trg,ems); 
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
    return bres; 
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

    if (srcvalue != null)
    { expr = srcvalue.addReference(srcexvar, new Type(realsrc));
      expr.setType(srcvalue.getType()); 
      expr.setElementType(srcvalue.getElementType()); 
    } 
    else 
    { expr = new BasicExpression(src); 
      ((BasicExpression) expr).setObjectRef(srcexvar); 
    } 

    
    if (preset != null && (preset instanceof Attribute)) 
    { // System.out.println(">>> Additional mapping " + expr + " ?-> " + trg); 
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
	if (d != null && d.isConcrete()) { } 
	else 
	{ if (pathtail.size() > 0) 
	  { Attribute tailhead = (Attribute) pathtail.get(0); 
	    System.err.println("!! Cannot instantiate entity " + d + " try " + tailhead.getOwner()); 
		d = tailhead.getOwner(); 
	  }
	  else 
	  { System.err.println("!! Error: Cannot instantiate entity " + d); }
	} 
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

  Statement etldirecttarget(String svar, BasicExpression directtarget, Vector ems) 
  { Expression sexp = // new BasicExpression(svar + "." + src);
                      src.etlComposedExpression(svar,trg,ems); 
    Expression vexp = sexp;
    Type stype = src.getType(); 
    Type ttype = trg.getType();   
    if (stype != null && ttype != null && 
        "String".equals(ttype.getName()) && stype.isEnumeration())
    { vexp = new BasicExpression("(" + sexp + ")." + stype.getName() + "2String()"); }  
    else if (stype != null && ttype != null && 
             "String".equals(stype.getName()) && ttype.isEnumeration())
    { vexp = new BasicExpression("(" + sexp + ").String2" + ttype.getName() + "()"); } 
    else if (stype != null && ttype != null && 
             stype.isEnumeration() && ttype.isEnumeration())
    { vexp = new BasicExpression("(" + sexp + ").convert" + stype.getName() + "_" + 
                                 ttype.getName() + "()"); 
    }
    else if (stype != null && ttype != null && 
        "boolean".equals(ttype.getName()) && stype.isEnumeration())
    { vexp = new BasicExpression("(" + sexp + ")." + stype.getName() + "2boolean" + trg.getName() + 
                                 "()"); } 
    else if (stype != null && ttype != null && 
             "boolean".equals(stype.getName()) && ttype.isEnumeration())
    { vexp = new BasicExpression("(" + sexp + ").boolean2" + 
                                 ttype.getName() + src.getName() + "()"); 
    } 

    AssignStatement res = new AssignStatement(directtarget, vexp); 
    if (src.getUpper() == 1 && src.getLower() == 0) 
    { BasicExpression testnull = new BasicExpression("isDefined"); 
      testnull.setParameters(new Vector()); 
      testnull.setObjectRef(new BasicExpression(svar + "." + src)); 
      return new ConditionalStatement(testnull, res); 
    }     
    return res; 
  } 

  Statement etlcomposedtarget(Vector newclauses, Vector newrules, Vector statements, 
                              String srcvar, String trgvar, Entity realsrc,
                              Vector created, java.util.Map implementedBy, Vector ems) 
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

    if (srcvalue != null)
    { expr = srcvalue.addReference(srcexvar, new Type(realsrc)); 
      expr.setType(srcvalue.getType()); 
      expr.setElementType(srcvalue.getElementType()); 
    } 
    else 
    { expr = new BasicExpression(src); 
      ((BasicExpression) expr).setObjectRef(srcexvar); 
    } 

    
    if (preset != null && (preset instanceof Attribute)) 
    { // System.out.println(">>> Additional mapping " + expr + " ?-> " + trg); 
      Attribute trgref = trg.objectReference(); 
      Attribute f = trg.getFinalFeature(); 
        
      if (trgref.isMultiValued())
      { // System.out.println(f + " upper bound is " + f.upperBound()); 
        // System.out.println(expr + " upper bound is " + expr.upperBound()); 
        
        if (f.upperBound() >= expr.upperBound())
        { // convert expr by using equivalent. 
          Expression xf = new BasicExpression("_x." + f); 
          Expression expretl = expr.etlEquivalent(trg,ems); 
          AssignStatement body = new AssignStatement(xf, expretl);
          Expression test = new BasicExpression("_x in " + trgvar + "." + trgref);  
          WhileStatement ws = new WhileStatement(test,body); 
          ws.setLoopKind(Statement.FOR); 
          statements.add(ws); 
        } 
        else 
        { Binding res = etlTargetMap(trgvar,preatt,path,expr,newclauses,newrules,implementedBy,ems);
          if (res == null) 
          { return null; }

          AssignStatement astat = new AssignStatement(res); 
 
          if (src.getUpper() == 1 && src.getLower() == 0) 
          { BasicExpression testnull = new BasicExpression("isDefined"); 
            testnull.setParameters(new Vector()); 
            testnull.setObjectRef(expr); 
            return new ConditionalStatement(testnull, astat); 
          }     
          return astat; 
        } 
      }  
      else if (f.upperBound() >= expr.upperBound()) 
      { Expression expretl = expr.etlEquivalent(trg,ems); 
        AssignStatement as1 = new AssignStatement(trgvar + "." + trg, expretl); 
        if (src.getUpper() == 1 && src.getLower() == 0) 
        { BasicExpression testnull = new BasicExpression("isDefined"); 
          testnull.setParameters(new Vector()); 
          testnull.setObjectRef(expr); 
          statements.add(new ConditionalStatement(testnull, as1)); 
        }     
        statements.add(as1); 
      }
      else 
      { Expression expretl = expr.etlEquivalent(trg,ems); 
        AssignStatement as2 = new AssignStatement(trgvar + "." + trg, 
                                     new UnaryExpression("->any", expretl)); 
        statements.add(as2); 
      }
 
      return null; 
    }      
    else 
    { Binding res = etlTargetMap(trgvar,preatt,path,expr,newclauses,newrules,implementedBy,ems);
      if (res == null) { return null; } 
      AssignStatement ast2 = new AssignStatement(res); 
      if (src.getUpper() == 1 && src.getLower() == 0) 
      { BasicExpression testnull = new BasicExpression("isDefined"); 
        testnull.setParameters(new Vector()); 
        testnull.setObjectRef(expr); 
        return new ConditionalStatement(testnull, ast2); 
      }     
      return ast2; 

    } 
  } 

Binding etlTargetMap(String trgvar, Attribute preatt, Vector path, Expression sexpr, 
                     Vector newclauses, Vector newrules, java.util.Map implementedBy, 
                     Vector ems)
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
  { // Expression expretl = sexpr.etlEquivalent(trg,ems); 
    Binding bres = new Binding(trgvar + "." + p1x, sexpr); 
    return bres; 
    // return trgvar + "." + p1x + " := " + sexpr; 
  }


  if (p1type.isEntity())
  { d = p1type.getEntity(); 
	if (d != null && d.isConcrete()) { } 
	else if (pathtail.size() > 0)
    { Attribute tailhead = (Attribute) pathtail.get(0); 
      System.err.println("!!! Cannot instantiate entity " + d + " try " + tailhead.getOwner()); 
      d = tailhead.getOwner(); 
    }
    else 
	{ System.err.println("!!! Error: Cannot instantiate " + d); } 
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
    { Binding bres1 = new Binding(trgvar + "." + p1x,sexpr); 
      return bres1; 
      // Do conversions using equivalent. 
    }
    sx = Identifier.nextIdentifier("var_"); 
         // srctype.getName().toLowerCase() + "$x"; 
    srcvar = new Attribute(sx,srctype,ModelElement.INTERNAL); 
    srcvar.setElementType(srctype); 
  } 

  if (path.size() > 1) 
  { if (p1.isSingleValued())
    { Binding sexp = etlTargetMap(dx,nextatt,pathtail,sexpr,
                                  newclauses,newrules,implementedBy,ems);
      // String newclause = "  " + dx + " : " + d.getName() + "\n" +
      //     "    ( " + sexp + " )";
      // newclauses.add(newclause);
      Binding bres2 = new Binding(trgvar + "." + p1x, new BasicExpression(dxvar)); 

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
        if (src.getLower() == 0 && src.getUpper() == 1) 
        { BasicExpression isdefined = new BasicExpression("isDefined"); 
          isdefined.setParameters(new Vector()); 
          isdefined.setObjectRef(sexpr); 
          ope.setCondition(isdefined); 
        } 
        newclauses.add(ope);
        ope.addBinding(sexp);
        implementedBy.put(preattname, ope);
        return bres2;  
      } 
    } 
    else // p1 not ONE
    { if (sexpr.isMultipleValued() && srcent != null)
      { Binding sexp2 = etlTargetMap(dx,nextatt,pathtail,new BasicExpression(srcvar),
                                     newclauses,newrules,implementedBy,ems);
        String newmap = "Map" + srcent + "2" + d + "_" + ModelElement.underscoredNames(pathtail);
        // String newrule = "  unique lazy rule " + newmap +"\n" +
        //   "  { from " + sx + " : MM1!" + srcent + "\n" +
        //   "    to " + dx + " : MM2!" + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        TransformationRule mr = new TransformationRule(newmap,true,false);
        mr.setSource(srcvar); 
          // OutPattern op = new OutPattern(); 
        OutPatternElement ope = new OutPatternElement(dxvar);
        ope.addBinding(sexp2); 
          // implementedBy.put(preattname, ope);
        implementedBy.put(preattname, preatt); 
          // op.setElement(ope);
        mr.addClause(ope);
        if (newrules.contains(mr)) { } 
        else 
        { newrules.add(mr); } 
         

        BinaryExpression crange = new BinaryExpression(":",new BasicExpression(sx),sexpr); 
        BasicExpression mapcall = new BasicExpression(sx + ".equivalent('" + newmap + "')"); 
        BinaryExpression collexp = new BinaryExpression("|C", crange, mapcall); 
        Binding bres3 = new Binding(trgvar + "." + p1x, collexp); 

        return bres3; 
        // return p1x + " <- " + 
        //   sexpr + "->collect( " + sx + " | thisModule." + newmap + "(" + sx + "))";
      }
      else if (sexpr.isMultipleValued())
      { Binding sexp2 = etlTargetMap(dx,nextatt,pathtail,new BasicExpression(srcvar),
                                     newclauses,newrules,implementedBy,ems);
        String newmap = "Map" + srctype + "2" + d + "_" + ModelElement.underscoredNames(pathtail);
        // String newrule = "    rule " + newmap +"(" + sx + " : " + srctype + ")\n" +
        //   "  { to " + dx + " : " + d + "\n" +
        //   "    ( " + sexp2 + " )\n" +
        //   "  }";
        // newrules.add(newrule);  

        // OutPatternElement ope = null; 
        // MatchedRule mr = new MatchedRule(false,false);
          Type rettype = new Type(d); 
          BehaviouralFeature bf = new BehaviouralFeature(newmap,new Vector(),false,rettype); 
          Vector stats = new Vector(); 
          CreationStatement decvar = new CreationStatement(d + "", dx);
          decvar.setInitialValue("new MM2!" + d); 
          stats.add(decvar);
          Binding sexp3 = sexp2.substitute(sx,new BasicExpression("self")); 
          stats.add(new AssignStatement(sexp3));
       
          stats.add(new ReturnStatement(new BasicExpression(dx)));  
          bf.setActivity(new SequenceStatement(stats)); 

          if (srcent != null) 
          { bf.setOwner(srcent); } 
          else 
          { bf.setOwner(new Entity(srctype + "")); } 
          // mr.addParameter(srcvar); 
          // OutPattern op = new OutPattern(); 
          // ope = new OutPatternElement(dxvar); 
          // op.setElement(ope); 
          // mr.setOutPattern(op);

          if (newrules.contains(bf)) { } 
          else 
          { newrules.add(bf); } 
 
          // ope.addBinding(sexp2); 
          implementedBy.put(preattname,preatt); 
        // An operation in ETL 
        
        BinaryExpression crange = new BinaryExpression(":",new BasicExpression(sx),sexpr); 
        BasicExpression mapcall = new BasicExpression(sx + "." + newmap + "()"); 
        BinaryExpression collexp = new BinaryExpression("|C", crange, mapcall); 
        Binding bres4 = new Binding(trgvar + "." + p1x,collexp); 
        return bres4; 
        // return p1x + " <- " + 
        //   sexpr + "->collect( " + sx + " | thisModule." + newmap + "(" + sx + "))";
      }
      else // if (srcent != null) // sexpr single-valued, object
      { // Binding sexp2 = atlTargetMap(nextatt,pathtail,new BasicExpression(srcvar),
        //                              newclauses,newrules,implementedBy);
        // String newmap = "Map" + srcent + "2" + d;
        Binding sexp = etlTargetMap(dx,nextatt,pathtail,sexpr,newclauses,
                                    newrules,implementedBy,ems);
        // String newclause = "  " + dx + " : " + d.getName() + "\n" +
        //     "    ( " + sexp + " )";
        // newclauses.add(newclause);
        Binding bres2 = new Binding(trgvar + "." + p1x, new BasicExpression(dxvar)); 
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
      }
    }
  }
  else // path.size() == 1
  { if (p1.isSingleValued() && sexpr.isSingleValued())
    { Expression expretl = sexpr.etlEquivalent(p1,ems); 
      Binding res7 = new Binding(trgvar + "." + p1x, expretl); 
      return res7; 
    }
    else if (p1.isSingleValued())
    { Expression expretl = sexpr.etlEquivalent(p1,ems); 
      UnaryExpression anyexp = new UnaryExpression("->any", expretl); 
      Binding bres8 = new Binding(trgvar + "." + p1x, anyexp); 
      return bres8; 
      // return p1x + " <- (" + sexpr + ")->any()";
    }
    else
    { Expression expretl = sexpr.etlEquivalent(p1,ems); 
      Binding res9 = new Binding(trgvar + "." + p1x, expretl); 
      return res9; 
      // return p1x + " <- " + sexpr; 
    }
  }
}


  public void checkModel(ModelSpecification mod, 
                         Vector srcobjs, Vector trgobjs, Vector attmaps, Vector removed, Vector added, Vector queries)
  { // For this feature mapping f |--> g, checks for each 
    // srcobjs ex : E and corresponding trgobjs fx : F, that ex.f = fx.g in 
    // the model mod. For set-valued g, could be ok if f1 |--> g, f2 |--> g and union of f1, f2 in model is g
	
	Type selemt = src.getElementType(); 
	Type telemt = trg.getElementType(); 
	String sent = ""; 
	String tent = ""; 
	if (selemt != null && selemt.isEntityType())
	{ sent = selemt.getEntity().getName(); }
	if (telemt != null && telemt.isEntityType())
	{ tent = telemt.getEntity().getName(); }
	
     boolean valid = true;
 
     double[] xs = new double[srcobjs.size()]; 
     double[] ys = new double[trgobjs.size()]; 
     Vector xlist = new Vector(); 
     java.util.Map ymap = new java.util.HashMap(); 
	
     String[] xstrs = new String[srcobjs.size()]; 
     String[] ystrs = new String[trgobjs.size()]; 
	
     Vector[] xvect = new Vector[srcobjs.size()]; 
     Vector[] yvect = new Vector[trgobjs.size()]; 
	 
     boolean[] xbools = new boolean[srcobjs.size()]; 
     boolean[] ybools = new boolean[trgobjs.size()]; 

     for (int i = 0; i < srcobjs.size() && i < trgobjs.size(); i++) 
     { ObjectSpecification sobj = (ObjectSpecification) srcobjs.get(i); 
       ObjectSpecification tobj = (ObjectSpecification) trgobjs.get(i); 

       if (src.isEnumeration() && trg.isEnumeration())
       { String senumval = sobj.getEnumerationValue(src,mod); 
         String tenumval = tobj.getEnumerationValue(trg,mod);
         xstrs[i] = senumval; 
         ystrs[i] = tenumval;  
         if (senumval != null && tenumval != null && senumval.equals(tenumval))
         { System.out.println(">> objects " + sobj + " and"); 
           System.out.println(tobj + " satisfy matching " + this); 
         } 
         else 
         { System.out.println("!! objects " + sobj + " (" + senumval + ") and"); 
           System.out.println(tobj + " (" + tenumval + ") fail to satisfy matching " + this); 
           valid = false; 
           removed.add(this); 
         }
       }
       else if (src.isNumeric() && trg.isNumeric())
       { double srcval = sobj.getNumericValue(src,mod); 
         xs[i] = srcval;
         Double xv = new Double(srcval);  
         xlist.add(xv); 
         double trgval = tobj.getNumericValue(trg,mod);
         ys[i] = trgval; 
         ymap.put(xv,new Double(trgval)); 
		 
         if (srcval == trgval) 
         { System.out.println(">> objects " + sobj + " and"); 
           System.out.println(tobj + " satisfy matching " + this); 
         } 
         else 
         { System.out.println("!! objects " + sobj + " and"); 
           System.out.println(tobj + " fail to satisfy matching " + this); 
           valid = false; 
           removed.add(this); 
         }
         System.out.println("----------------------"); 
         System.out.println();  
       } 
       else if (src.isBoolean() && trg.isBoolean())
       { boolean srcval = sobj.getBoolean(src.getName()); 
         xbools[i] = srcval;
         boolean trgval = tobj.getBoolean(trg.getName());
         ybools[i] = trgval; 
         
         if (srcval == trgval) 
         { System.out.println(">> objects " + sobj + " and"); 
           System.out.println(tobj + " satisfy matching " + this); 
         } 
         else 
         { System.out.println("!! objects " + sobj + " and"); 
           System.out.println(tobj + " fail to satisfy matching " + this); 
           valid = false;
           removed.add(this);  
         }
         System.out.println("----------------------"); 
         System.out.println();  
       } 
       else if (src.isString() && trg.isString())
       { String srcstr = sobj.getStringValue(src,mod); 
         String trgstr = tobj.getStringValue(trg,mod);
         xstrs[i] = srcstr; 
         ystrs[i] = trgstr; 
		
         if (srcstr.equals(trgstr))
         { System.out.println(">> objects " + sobj + " and"); 
           System.out.println(tobj + " satisfy matching " + this); 
         } 
         else 
         { System.out.println("!! objects " + sobj + " and"); 
           System.out.println(tobj + " fail to satisfy matching " + this); 
           valid = false; 
           removed.add(this); 
         }
         System.out.println("----------------------");
         System.out.println();    
	  }
	  else if (src.isEntity() && trg.isEntity())
	  { ObjectSpecification srcobj = sobj.getReferredObject(src,mod); 
	    ObjectSpecification trgobj = tobj.getReferredObject(trg,mod);
	    if (srcobj == null) 
	    { System.out.println("?? Incomplete model: Referred object " + src + " of " + sobj + " is undefined."); }
	    if (trgobj == null)
	    { System.out.println("?? Incomplete model: Referred object " + trg + " of " + tobj + " is undefined."); }
	    if (trgobj != null && srcobj != null)	 
	    { if (mod.correspondence.getAll(srcobj) != null && mod.correspondence.getAll(srcobj).contains(trgobj))
          { System.out.println(">> objects " + srcobj + " and"); 
            System.out.println(trgobj + " correspond."); 
            System.out.println(">> objects " + sobj + " and"); 
            System.out.println(tobj + " satisfy matching " + this); 
          } 
          else 
          { System.out.println(">> objects " + srcobj + " and"); 
            System.out.println(trgobj + " do not correspond."); 
            System.out.println("!! objects " + sobj + " " + tobj + " fail to satisfy matching " + this); 
            valid = false; 
            removed.add(this); 
          }
        } 
        else 
        { valid = false; 
          removed.add(this); 
        }
        System.out.println("----------------------");
        System.out.println();
      }
      else if (src.isCollection() && trg.isCollection())
      { Vector srcvect = sobj.getCollectionValue(src,mod); 
        Vector trgvect = tobj.getCollectionValue(trg,mod); 
		
	   xvect[i] = srcvect; 
	   yvect[i] = trgvect; 
		
        if (src.isSequence() && trg.isSequence())
        { if (srcvect.equals(trgvect))
          { System.out.println(">> objects " + sobj + " and"); 
            System.out.println(tobj + " satisfy matching " + this); 
          } 
          else if (mod.correspondingObjectSequences(sent,tent,srcvect,trgvect))
          { System.out.println(">> objects " + sobj + " and"); 
            System.out.println(tobj + " satisfy matching " + this); 
          } 
          else 
          { System.out.println("!! objects " + sobj + " and"); 
            System.out.println(tobj + " fail to satisfy matching " + this); 
	        valid = false;
			// removed.add(this); 
          }  
          System.out.println("----------------------");
          System.out.println();    
	    }
	    else if (src.isCollection() && trg.isCollection()) 
	    { // System.out.println(srcvect + "  " + trgvect); 
           if (srcvect.containsAll(trgvect) && trgvect.containsAll(srcvect))
           { System.out.println(">> objects " + sobj + " and"); 
             System.out.println(tobj + " satisfy matching " + this); 
           } 
           else if (mod.correspondingObjectSets(srcvect,trgvect))
           { System.out.println(">> objects " + sobj + " and"); 
             System.out.println(tobj + " satisfy matching " + this); 
           } 
           else 
           { System.out.println("!! Collections " + srcvect); 
             System.out.println(trgvect + " Do not correspond"); 
             System.out.println(">> Sizes are " + srcvect.size() + " and " + trgvect.size()); 
             System.out.println("!! objects " + sobj + " and"); 
             System.out.println(tobj + " fail to satisfy matching " + this); 
             valid = false;
			  // removed.add(this); 
           }                   
           System.out.println("----------------------");
           System.out.println();    
         }
       } 
     } 

	if (!valid && src.isEnumeration() && trg.isEnumeration())
	{ if (AuxMath.isConstant(ystrs))
       { System.out.println(">>> " + trg + " is constant.");
         System.out.println(ystrs[0] + " |--> " + trg);
         added.add(new AttributeMatching(new BasicExpression(ystrs[0]), trg)); 
         System.out.println(); 
       }    
	
       if (AuxMath.isFunctional(xstrs,ystrs)) { } 
	  else
	  { System.out.println(">>> " + trg + " is not a function of " + src); 
	     return; 
        }
	   
	   // Propose a function
	   if (xstrs.length > 1)
	   { System.out.println(">>> Suggesting a function f_" + src + "_" + trg); 
           int qs = queries.size(); 
           Vector pars = new Vector(); 
           pars.add(src); 
           Type returntype = trg.getType(); 
           BehaviouralFeature bf = new BehaviouralFeature("f_" + src + "_" + trg + "_" + qs, pars, true, returntype);
           Expression post = new BasicExpression(trg);  
	     for (int i = 0; i < xstrs.length; i++)
	     { System.out.println("  " + xstrs[i] + " |--> " + ystrs[i]); 
            BinaryExpression test = 
              new BinaryExpression("=", new BasicExpression(src), new BasicExpression(xstrs[i])); 
             post = new ConditionalExpression(test,
                      new BasicExpression(ystrs[i]),post); 
           }
           bf.setPostcondition(post); 
           queries.add(bf); 
	     System.out.println(">>> for " + src + " |--> " + trg); 
		 added.add(new AttributeMatching(new BasicExpression("f_" + src + "_" + trg + "_" + qs + "(" + src + ")"), trg)); 
         System.out.println(); 
       }    

	}
     else if (!valid && src.isNumeric() && trg.isNumeric())
	{ // check if the x and y are constant, or 
       // linearly or quadratically correlated. 
       // If so, derive the x to y function
	   removed.add(this); 
	   
	   if (AuxMath.isFunctional(xs,ys)) { } 
	   else
	   { System.out.println(">>> " + trg + " is not a function of " + src); 
	     return; 
	   }

       if (AuxMath.isConstant(ys))
       { System.out.println(">>> " + trg + " is constant.");
         System.out.println(ys[0] + " |--> " + trg);
		 added.add(new AttributeMatching(new BasicExpression(ys[0]), trg)); 
         System.out.println(); 
       }    
       else 
	   { double corr = AuxMath.linearCorrelation(xs,ys); 
	     if (corr > 0.95)
	     { System.out.println(">>> The " + src + " and " + trg + 
	            " values are linearly related with correlation " + corr); 
	       double slope = AuxMath.linearSlope(); 
	       double offset = AuxMath.linearOffset(); 
	       System.out.println(slope + "*" + src + " + " + offset + " |--> " + trg);
           added.add(new AttributeMatching(new BasicExpression(slope + "*" + src + " + " + offset), trg)); 
           System.out.println(); 
         } 
         else 
         { // sort the ys by increasing xs
            List sortedxs = new Vector(); 
            sortedxs.addAll(xlist); 
            Collections.sort(sortedxs); 
            System.out.println(sortedxs); 
            List ylist = new Vector();
            for (int g = 0; g < sortedxs.size(); g++) 
            { ylist.add(ymap.get(sortedxs.get(g))); }  
            System.out.println(ylist); 
            
            double[] ys2 = new double[ylist.size()]; 
            for (int k = 0; k < ylist.size(); k++) 
            { ys2[k] = 
                ((Double) ylist.get(k)).doubleValue(); 
            } 

            AuxMath.slopes(ys2); 
            boolean quad = AuxMath.quadraticRelationship(xs,ys2,
                                            src.getName(),trg.getName()); 
            System.out.println(">>> Quadratic " + quad); 

            if (!quad)
            { double isexp = AuxMath.isExponential(xs,ys2); 
              System.out.println(">>> Exponential: " + isexp); 
	          if (isexp > 0.9) 
              { AuxMath.exponentialRelationship(src.getName(),trg.getName()); }
            } 
          }  
        }
		
	   return; 
      }

	  if (!valid && src.isBoolean() && trg.isBoolean())
	  { removed.add(this); 
	  
	    if (AuxMath.isConstant(ybools))
         { System.out.println(">>> " + trg + " is constant.");
           System.out.println(">>> Corrected mapping is: ");
           System.out.println(ybools[0] + " |--> " + trg);
           added.add(new AttributeMatching(new BasicExpression(ybools[0]),trg)); 
		   System.out.println(); 
         }
		 else if (AuxMath.isNegation(xbools,ybools))
		 { System.out.println(">>> " + trg + " is negation of " + src + ".");
           System.out.println(">>> Corrected mapping is: ");
           System.out.println("not(" + src + ") |--> " + trg);
           added.add(new AttributeMatching(new UnaryExpression("not", new BasicExpression(src)),trg)); 
		   System.out.println(); 
         }
		 
	    return; 
      } 
	  
	  
	  if (!valid && src.isString() && trg.isString())
	  { // System.out.println(xstrs[0]); 
	    // System.out.println(ystrs[0]); 
		removed.add(this); 
		
	    if (AuxMath.isConstant(ystrs))
         { System.out.println(">>> " + trg + " is constant.");
           System.out.println(">>> Corrected mapping is: ");
           System.out.println("\"" + ystrs[0] + "\" |--> " + trg);
           added.add(new AttributeMatching(new BasicExpression("\"" + ystrs[0] + "\""),trg)); 
		   System.out.println(); 
         }
	   
	    boolean prefixed = AuxMath.isPrefixed(xstrs,ystrs); 
	    if (prefixed) 
	    { System.out.println(">>> Target is " + src + " + a prefix "); 
           String pref = AuxMath.commonPrefix(xstrs,ystrs); 
           if (pref != null) 
           { System.out.println(">>> Corrected mapping is: ");
             System.out.println("\"" + pref + "\" + " + src + " |--> " + trg); 
             added.add(new AttributeMatching(new BasicExpression("\"" + pref + "\" + " + src),trg)); 
		     System.out.println(); 
           }
         }
 
	    boolean suffixed = AuxMath.isSuffixed(xstrs,ystrs); 
	    if (suffixed) 
	    { System.out.println(">>> Target is " + src + " + a suffix "); 
           String pref = AuxMath.commonSuffix(xstrs,ystrs); 
           if (pref != null) 
           { System.out.println(">>> Corrected mapping is: ");
             System.out.println(src + " + \"" + pref + "\" |--> " + trg); 
             added.add(new AttributeMatching(new BasicExpression(src + " + \"" + pref + "\""),trg)); 
		     System.out.println(); 
           }
         }

	    boolean uppercased = AuxMath.isUpperCased(xstrs,ystrs); 
	    if (uppercased) 
	    { System.out.println(">>> Target is " + src + " uppercased ");
           System.out.println(">>> Corrected mapping is: ");
           
           System.out.println(src + "->toUpperCase() |--> " + trg); 
           BasicExpression srcexp = new BasicExpression(src);  

		   added.add(new AttributeMatching(new UnaryExpression("->toUpperCase",srcexp),trg)); 
		     
           System.out.println(); 
         }

         boolean lowercased = AuxMath.isLowerCased(xstrs,ystrs); 
         if (lowercased) 
	     { System.out.println(">>> Target is " + src + " lowercased "); 
           System.out.println(src + "->toLowerCase() |--> " + trg); 
		   BasicExpression srcexp = new BasicExpression(src);  
		   added.add(new AttributeMatching(new UnaryExpression("->toLowerCase",srcexp),trg)); 
         }

        boolean reversed = AuxMath.isReversed(xstrs,ystrs); 
	   if (reversed) 
	   { System.out.println(">>> Target is " + src + " reversed "); 
          System.out.println(src + "->reverse() |--> " + trg);
          BasicExpression srcexp = new BasicExpression(src);  
	     added.add(new AttributeMatching(new UnaryExpression("->reverse",srcexp),trg)); 
        }
		
	   return; 
     }
	 
	 if (!valid && src.isSequence() && trg.isSequence())
      { 
	   if (AuxMath.isConstantSequence(yvect))
        { System.out.println(">>> " + trg + " is constant.");
          System.out.println(">>> Corrected mapping is: ");
          System.out.println(yvect[0] + " |--> " + trg);
          removed.add(this); 
	     added.add(new AttributeMatching(new SetExpression(yvect[0],true), trg)); 
	     System.out.println(); 
        }
		
		
	   boolean prefixed = AuxMath.isPrefixedSequence(xvect, yvect, sent, tent, mod); 
	   if (prefixed)
	   { System.out.println(">>> Target is " + src + " + a prefix "); 
         Vector pref = AuxMath.commonSequencePrefix(xvect,yvect); 
         if (pref != null) 
         { System.out.println(">>> Corrected mapping is: ");
           System.out.println(pref + "^" + src + " |--> " + trg); 
           removed.add(this);
           BinaryExpression cat = new BinaryExpression("^", new SetExpression(pref,true), new BasicExpression(src));
           added.add(new AttributeMatching(cat, trg)); 
		    
           System.out.println(); 
         }
       }

	   boolean suffixed = AuxMath.isSuffixedSequence(xvect, yvect, sent, tent, mod); 
	   if (suffixed)
	   { System.out.println(">>> Target is " + src + " + a suffix "); 
          Vector pref = AuxMath.commonSequenceSuffix(xvect,yvect); 
          if (pref != null) 
          { System.out.println(">>> Corrected mapping is: ");
            System.out.println(src + "^" + pref + " |--> " + trg); 
            removed.add(this);
            BinaryExpression cat = new BinaryExpression("^", new BasicExpression(src), new SetExpression(pref,true));
            added.add(new AttributeMatching(cat, trg)); 
            System.out.println(); 
          }
        }
		
	   return; 
	 }
	  
     if (!valid && src.isCollection() && trg.isCollection())
     { 
	   if (AuxMath.isConstantSet(yvect))
        { System.out.println(">>> " + trg + " is constant.");
          System.out.println(">>> Corrected mapping is: ");
          System.out.println(yvect[0] + " |--> " + trg);
          removed.add(this); 
	      added.add(new AttributeMatching(new SetExpression(yvect[0]), trg)); // SetExpression constructor 
          System.out.println(); 
        }
		
		
      boolean subsetted = AuxMath.isSubsetSet(xvect, yvect, mod); 
	  if (subsetted)
	  { System.out.println(">>> Target " + trg + " is " + src + " + some elements -- possibly another feature or a constant set");  
        Vector sadded = AuxMath.commonSubsetSet(xvect,yvect,mod); 
        if (sadded != null) 
        { System.out.println(">>> Corrected mapping is: ");
          System.out.println(src + "->union(" + sadded + ") |--> " + trg);
          BinaryExpression unionexp = new BinaryExpression("->union", new BasicExpression(src), new SetExpression(sadded));  
          added.add(new AttributeMatching(unionexp, trg)); 
          removed.add(this); 
          System.out.println();      
        } // or union of another feature
        else 
        { for (int k = 0; k < attmaps.size(); k++) 
          { AttributeMatching kmap = (AttributeMatching) attmaps.get(k); 
            if (kmap.trg == trg && kmap.src != src)
            { System.out.println(">>> Suggest " + src + "->union(" + kmap.src + ") |--> " + trg); }
		  }
		  System.out.println(); 
		}
      }
      else 
      { boolean supsetted = AuxMath.isSupsetSet(xvect, yvect, mod); 
        if (supsetted)
        { System.out.println(">>> Target " + trg + " is selection of " + src);  
            // Vector sadded = AuxMath.commonSubsetSet(xvect,yvect,sent,tent,mod); 
            // if (sadded != null) 
			
          if (src.getElementType() != null && src.getElementType().isAbstractEntity())
          { // Possible conditions P are x->oclIsTypeOf(S) for concrete subtypes of the elementtype, 
            // or x.f = val for booleans, enums, Strings 
            Entity srcelement = src.getElementType().getEntity(); 
            Vector leafsubs = srcelement.getActualLeafSubclasses(); 
            Vector selectionconditions = mod.validateSelectionConditions(leafsubs,xvect,yvect,src);   
            if (selectionconditions.size() > 0)
            { System.out.println(">>> Possible corrected mapping is: ");
              System.out.println(selectionconditions.get(0) + " |--> " + trg); 
              added.add(new AttributeMatching((Expression) selectionconditions.get(0), trg)); 
              removed.add(this); 
              System.out.println(); 
            }
            else 
            { System.out.println(">>> Retaining mapping, but it needs to be refined."); }
           } 
           else if (src.getElementType() != null && src.getElementType().isEntity())
           { Entity srcref = src.getElementType().getEntity(); 
             Vector discriminators = srcref.getDiscriminatorAttributes();
             System.out.println(">>> Discriminator attributes of " + srcref + " are: " + discriminators); 

             Vector selectionConds = mod.validateDiscriminatorConditions(discriminators,xvect,yvect,src); 
             if (selectionConds.size() > 0)
             { System.out.println(">>> Possible alternative source expressions are: " + selectionConds);
			   
               System.out.println(selectionConds.get(0) + " |--> " + trg); 
               added.add(new AttributeMatching((Expression) selectionConds.get(0), trg)); 
               removed.add(this); 
               System.out.println(); 
             } 
           }
           else 
           { System.out.println(">>> Retaining mapping, but it needs to be refined."); }
             System.out.println(); 
           }
	      else 
           { System.out.println(">>> " + trg + " seems unrelated to " + src);
             System.out.println(">>> Try an alternative target feature of type " + trg.getType());
             removed.add(this); 
             System.out.println();   
           }
         }        
       }
   }    
    
   public String targetCSTLExpressionFor(java.util.Map srcfeatureMap)
   { // Add var`f for any source composition src of the form att.f, where att |-> var in the featureMap
   
     String var = (String) srcfeatureMap.get(src + ""); 
     String res = "\"\""; 
     if (var != null) 
     { res = var; } // can be null. 

     if (srcvalue != null) 
     { res = srcvalue.cstlQueryForm(srcfeatureMap); } 
     // but can't convert general expressions to CSTL

     Vector tpath = trg.getNavigation(); 
     int tpathsize = tpath.size(); 
     if (tpathsize > 1) // must be size 2 in most cases
     { // create a nested target element
       Attribute attf = (Attribute) tpath.get(tpathsize-2); 
       Type eintermediate = attf.getElementType(); 
       if (eintermediate != null && eintermediate.isEntity())
       { Entity eint = eintermediate.getEntity(); 
         return eint.getName() + "[" + res + "]"; 
       } // actually, accumulate features of eint. 
     } 

     Vector path = src.getNavigation(); 
     if (path.size() <= 1) 
     { return res; } // The variable _i corresponding to src

     Attribute fsrc = (Attribute) path.get(0); 
     var = (String) srcfeatureMap.get(fsrc + ""); 

     String metafeatures = ""; 
     for (int i = 1; i < path.size(); i++) 
     { Attribute f = (Attribute) path.get(i); 
       metafeatures = metafeatures + "`" + f.getName();
     } 
     return var + metafeatures;   
   } // can't have both a source expression and source path. 
} 
