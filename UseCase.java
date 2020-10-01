import java.util.Vector; 
import java.io.*; 
import javax.swing.*;

/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Use Case */ 

public class UseCase extends ModelElement
{ // String name;
  private Vector parameters = new Vector(); // Attribute
  private Type resultType = null; 
  private Type elementType = null; 

  String description = ""; 
  Entity ent = null;
  Vector orderedPostconditions = new Vector(); // of ConstraintOrGroup
  Vector preconditions = new Vector(); // Assumptions to be checked on initial data
  Vector constraints = new Vector(); // local invariants during the use case
  Vector ownedAttribute = new Vector(); // of Attribute, static 
  Vector ownedOperations = new Vector(); // of BehavouralFeature, static

  SequenceStatement classifierBehaviour = new SequenceStatement();
  Statement activity = null; 

  Vector extendsList = new Vector(); // of String. Resolve for:
  Vector extend = new Vector(); // Extend objects pointing to extension use cases 
    // should be called "extensions", it is not the "extend" in the UML metamodel
  Vector extensionOf = new Vector(); // The UseCases which this extends

  Vector includesList = new Vector(); // String. to resolve:
  Vector include = new Vector(); // Include objects of this use case
  Vector includedIn = new Vector(); // The UseCases which include this
  
  boolean generic = false; 
  boolean derived = false; 
  UseCase superclass = null; 
  boolean isAbstract = false; 
  boolean incremental = false; 
  boolean bx = false; 

  Entity classifier = null;  // this use case, as an Entity
  BehaviouralFeature operation = null; // this use case, as an operation
  Vector deltaconstraints = new Vector(); 
  BacktrackingSpecification bs = null; 

  public UseCase(String nme, Entity e)
  { super(nme);
    ent = e;
    String cname = capitalise(nme); 
    classifier = new Entity(cname); 
    classifier.addStereotype("derived"); 
    operation = new BehaviouralFeature(nme); 
    operation.addStereotype("derived"); 
    operation.setResultType(new Type("void",null)); 
    operation.addStereotype("auxiliary"); 
    operation.setPrecondition(new BasicExpression(true)); 
    operation.setPostcondition(new BasicExpression(true)); 
    classifier.addOperation(operation); 
  }

  public UseCase(String nme)
  { super(nme);
    ent = null; 
    String cname = capitalise(nme); 
    classifier = new Entity(cname); 
    classifier.addStereotype("derived"); 
    BehaviouralFeature bf = new BehaviouralFeature(nme); 
    bf.addStereotype("derived"); 
    bf.setResultType(new Type("void",null)); 
    classifier.addOperation(bf); 
    operation = bf; 
    operation.addStereotype("auxiliary"); 
    operation.setPrecondition(new BasicExpression(true)); 
    operation.setPostcondition(new BasicExpression(true)); 
  }

  public void setResultType(Type et)
  { if ("void".equals(et + ""))
    { resultType = null;
	  operation.setResultType(null); 
	  return;
    } 
	
	resultType = et; 
    if (et != null && Type.isBasicType(et)) 
    { setElementType(et); } 
    operation.setResultType(et); 
  } 

  public void setElementType(Type et)
  { elementType = et; 
    operation.setElementType(et); 
  } 

  public Type getResultType()
  { return resultType; }

  public Type getElementType()
  { return elementType; }

  public Attribute getResultParameter()
  { if (resultType != null) 
    { Attribute res = new Attribute("result", resultType, ModelElement.INTERNAL); 
      res.setElementType(elementType); 
      return res; 
    }
    return null; 
  } 

  public Entity getClassifier() 
  { return classifier; } 

  public Statement getActivity()
  { return activity; } 

  public void setActivity(Statement act)
  { activity = act; } 

  public void setDescription(String comm)
  { description = comm; } 

  public void setName(String nme)
  { super.setName(nme); 
    if (classifier != null) 
    { String ename = capitalise(nme); 
      classifier.setName(ename); 
    } 
  } 
  
  public void setEntity(Entity e)
  { ent = e; }  
  // Associated entity: use case only updates features of ent. 
  
  public Entity getEntity() 
  { return ent; }

  public int ruleCount() 
  { return orderedPostconditions.size(); } 

  public int operationsCount() 
  { return ownedOperations.size(); } 

  public void addParameter(String nme, Type typ) 
  { Attribute par = new Attribute(nme, typ, ModelElement.INTERNAL); 
    par.setElementType(typ.getElementType()); 
    parameters.add(par); 
  } 

  public Object clone()
  { UseCase uc = new UseCase(getName(), ent); // copies the classifier

    for (int i = 0; i < preconditions.size(); i++) 
    { Constraint pns = 
        (Constraint) ((Constraint) preconditions.get(i)).clone(); 
      uc.addPrecondition(pns); 
    } 

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { Constraint cns = 
        (Constraint) ((Constraint) orderedPostconditions.get(i)).clone(); 
      uc.addPostcondition(cns); 
    } 

    for (int i = 0; i < constraints.size(); i++) 
    { Constraint cns = 
        (Constraint) ((Constraint) constraints.get(i)).clone(); 
      uc.addInvariant(cns); 
    } 

    uc.setCode((SequenceStatement) classifierBehaviour.clone());
    Vector newparams = new Vector(); 
    if (parameters == null) 
    { newparams = parameters; } 
    else 
    { newparams.addAll(parameters); } 
    uc.setParameters(newparams); 
    uc.generic = generic; 
    uc.derived = derived; 
    uc.setResultType(resultType); 
    uc.setElementType(elementType); 
    uc.setSuperclass(superclass); 
    uc.setActivity(activity);
    uc.isAbstract = isAbstract; 
    for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = 
        (Attribute) ((Attribute) ownedAttribute.get(i)).clone(); 
      uc.addAttribute(att); 
    } 
    uc.ownedOperations = ownedOperations;  // clone them 
    return uc; 
  } // but don't copy the extends list, classifier, or includes? I think you should.

  public String getDescription()
  { return description; } 

  public void setBacktrackingSpecification(BacktrackingSpecification back)
  { bs = back; } 

  public BacktrackingSpecification getBacktrackingSpecification()
  { return bs; } 

  public boolean isBacktracking() 
  { return bs != null; } 

  public void setDerived(boolean d)
  { derived = d; } 

  public void setIncremental(boolean d)
  { incremental = d; } 

  public void setBx(boolean d)
  { bx = d; } 

  public boolean isIncremental() 
  { return incremental; } 

  public boolean isBx() 
  { return bx; } 

  public boolean isDependent()
  { return extensionOf.size() > 0 || includedIn.size() > 0; } 

  public boolean isIndependent()
  { return extensionOf.size() == 0 && includedIn.size() == 0; } 

  public boolean isPrivate()
  { return hasStereotype("private"); } 

  public boolean isPublic()
  { if (isPrivate()) 
    { return false; } 
    return true; 
  } 


  public void setParameters(Vector pars)
  { parameters = pars; 
    operation.setParameters(pars); 
  } 

  public Vector getParameters()
  { return parameters; } 

  public String getParameterNames()
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getName() + " "; 
    } 
    return res; 
  } 

  public boolean isGeneric()
  { return generic; } 

  public boolean isDerived()
  { return derived; } 

  public boolean isAbstract()
  { return isAbstract; } 

  public void setCode(SequenceStatement stat)
  { classifierBehaviour = stat; } 

  public void resetCode()
  { classifierBehaviour = new SequenceStatement(); }
  
  public SequenceStatement getCode()
  { return classifierBehaviour; } 

  public void setSuperclass(UseCase ss)
  { superclass = ss; 
    if (ss != null) 
    { ss.isAbstract = true; }  
  } 

  public int getSize()
  { return orderedPostconditions.size(); } 

  public int getCodeSize()
  { return classifierBehaviour.getSize(); } 

  public Vector getPostconditions()
  { return orderedPostconditions; } 

  public ConstraintOrGroup getPostcondition(int i) 
  { if (i >= 1 && i <= orderedPostconditions.size())
    { int j = i-1; 
      return (ConstraintOrGroup) orderedPostconditions.get(j); 
    } 
    return null; 
  } 
 
  public Vector getInvariants()
  { return constraints; } 

  public void addPostcondition(ConstraintOrGroup post)
  { if (post == null) { return; } 
    if (post.isTrivial()) { return; } 
    orderedPostconditions.add(post); 
    post.setId(orderedPostconditions.size()); 
  }

  public void addPostconditions(Vector posts)
  { for (int i = 0; i < posts.size(); i++) 
    { ConstraintOrGroup post = (ConstraintOrGroup) posts.get(i); 
      // orderedPostconditions.add(post); 
      // post.setId(orderedPostconditions.size());
      addPostcondition(post); 
    }  
  }

  // public void addPostcondition(ConstraintGroup post)
  // { orderedPostconditions.add(post); }  // set Id? 

  public void renumber()
  { for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      con.setId(i); 
    } 
  } 

  public void addPrecondition(Constraint pre)
  { if (pre != null) 
    { preconditions.add(pre); }
  }  

  public void addInvariant(Constraint inv)
  { if (inv != null) 
    { constraints.add(inv); }  // its usecase is set to this
  } 

  public void addPreconditions(Vector cons)
  { for (int i = 0; i < cons.size(); i++)
    { Constraint p = (Constraint) cons.get(i); 
      if (p == null || preconditions.contains(p)) { } 
      else 
      { preconditions.add(p); } 
    } 
  } 

  public void addInvariants(Vector cons)
  { for (int i = 0; i < cons.size(); i++)
    { Constraint p = (Constraint) cons.get(i); 
      if (p == null || constraints.contains(p)) { } 
      else 
      { constraints.add(p); } 
    } 
  } 

  public void setAttributes(Vector atts)
  { ownedAttribute.clear(); 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute x = (Attribute) atts.get(i); 
      x.setInstanceScope(false); 
      x.setVisibility(PUBLIC);
    }       
    ownedAttribute.addAll(atts); 
    classifier.setAttributes(atts); 
  } // must all be set to PUBLIC class scope. 

  public void addAttribute(String nme, Type typ) 
  { Attribute par = new Attribute(nme, typ, ModelElement.INTERNAL); 
    par.setElementType(typ.getElementType()); 
    addAttribute(par); 
  } 

  public void addAttribute(Attribute att)
  { removeAttribute(att.getName()); 
    ownedAttribute.add(att); 
    classifier.addAttribute(att); 
    att.setInstanceScope(false); 
    att.setVisibility(PUBLIC); 
  }  // Normally static, with owner = null

  public void removeAttribute(Attribute att)
  { ownedAttribute.remove(att); 
    classifier.removeAttribute(att.getName()); 
  }  // Normally static, with owner = null

  public void removeAttribute(String nme)
  { Vector removed = new Vector(); 
    for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      if (nme.equals(att.getName()))
      { removed.add(att); } 
    } 
    ownedAttribute.removeAll(removed); 
    classifier.removeAtts(removed); 
  } 

  public boolean hasAttribute(String nme) 
  { Attribute att = (Attribute) ModelElement.lookupByName(nme, ownedAttribute); 
    if (att == null) 
    { return false; } 
    return true; 
  } 

  public Vector getOwnedAttribute()
  { return ownedAttribute; } 

  public void addAttributes(Vector atts) 
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      if (att == null || hasAttribute(att.getName()))
      { } 
      else 
      { ownedAttribute.add(att); 
        classifier.addAttribute(att); 
        att.setInstanceScope(false); 
      } 
    } 
  } 

  public void addOperation(BehaviouralFeature op)
  { if (op == null || hasOperation(op.getName())) { } 
    else 
    { op.setInstanceScope(false); 
      ownedOperations.add(op); 
      classifier.addOperation(op); 
    }  // Static. with owner/entity = null
  } 

  public void addOperations(Vector ops)
  { for (int i = 0; i < ops.size(); i++) 
    { BehaviouralFeature op = (BehaviouralFeature) ops.get(i); 
      addOperation(op); 
    } 
  } 

  public void removeOperation(BehaviouralFeature op)
  { ownedOperations.remove(op); 
    classifier.removeOperation(op); 
  }  // Static. with owner/entity = null

  public void removeOperations(String nme)
  { Vector removed = new Vector(); 
    for (int i = 0; i < ownedOperations.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) ownedOperations.get(i); 
      if (bf.getName().equals(nme))
      { removed.add(bf); } 
    } 
    ownedOperations.removeAll(removed);  
    classifier.removeOperations(nme); 
  }  // Static. with owner/entity = null

  public void replaceOperation(BehaviouralFeature op)
  { if (op == null) 
    { return; } 
     
    if (hasOperation(op.getName())) 
    { removeOperations(op.getName()); } 
     
    op.setInstanceScope(false); 
    ownedOperations.add(op); 
    classifier.addOperation(op); 
    // Static. with owner/entity = null
  } 

  public Vector getOperations()
  { return ownedOperations; } 

  public boolean hasOperation(String nme) 
  { BehaviouralFeature op = (BehaviouralFeature) ModelElement.lookupByName(nme, ownedOperations); 
    if (op == null) 
    { return false; } 
    return true; 
  } 

  public void removeConstraints(Vector cons)
  { constraints.removeAll(cons); 
    preconditions.removeAll(cons); 
    orderedPostconditions.removeAll(cons); 
    classifierBehaviour = new SequenceStatement();
    for (int i = 0; i < cons.size(); i++) 
    { Constraint con = (Constraint) cons.get(i); 
      Entity e = con.getOwner(); 
      if (e != null) 
      { e.removeDerivedOperations(); } 
    } 
  } // and remove within groups. 

  public void resetDesign(Vector cons)
  { classifierBehaviour = new SequenceStatement();
    for (int i = 0; i < cons.size(); i++) 
    { Constraint con = (Constraint) cons.get(i); 
      Entity e = con.getOwner(); 
      if (e != null) 
      { e.removeDerivedOperations(); } 
    } 
  } // and remove within groups. 

  public void resetDesign()
  { classifierBehaviour = new SequenceStatement();
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Entity e = con.getOwner(); 
      if (e != null) 
      { e.removeDerivedOperations(); } 
    } 
  } // and remove within groups - for all owner entities of the group. 

  public void clearPostconditions()
  { orderedPostconditions.clear(); } 

  public Vector allPreTerms()
  { Vector res = new Vector(); 
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cns = 
        (ConstraintOrGroup) orderedPostconditions.get(i); 
      res = VectorUtil.union(res,cns.allPreTerms()); 
    } 

    for (int i = 0; i < constraints.size(); i++) 
    { Constraint cns = 
        (Constraint) constraints.get(i); 
      res = VectorUtil.union(res,cns.allPreTerms());  
    } // should not use @pre in invariants. 

    return res; 
  } 

  public Vector getPreconditions()
  { return preconditions; } 

  public void addExtends(String ext) 
  { if (extendsList.contains(ext)) { } 
    else 
    { extendsList.add(ext); } 
  } 

  public void addIncludes(String ext) 
  { if (includesList.contains(ext)) { } 
    else 
    { includesList.add(ext); } 
  } 

  public void addExtensionOf(UseCase base)
  { extensionOf.add(base); } 

  public void removeExtensionOf(UseCase base)
  { extensionOf.remove(base); } 

  public void addIncludedIn(UseCase base)
  { includedIn.add(base); } 

  public void removeIncludedIn(UseCase base)
  { includedIn.remove(base); } 

  public Vector cwr(Vector assocs)
  { Vector res = new Vector(); 
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cns = 
        (ConstraintOrGroup) orderedPostconditions.get(i); 
      res = VectorUtil.union(res,cns.cwr(assocs)); 
    } 
    return res; 
  } 

  public Vector wr(Vector assocs)
  { Vector res = new Vector(); 
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cns = 
        (ConstraintOrGroup) orderedPostconditions.get(i); 
      res = VectorUtil.union(res,cns.wr(assocs)); 
    } 
    return res; 
  } 

  public Vector readFrame()
  { Vector res = new Vector(); 
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cns = 
        (ConstraintOrGroup) orderedPostconditions.get(i); 
      res = VectorUtil.union(res,cns.readFrame()); 
    } 
    return res; 
  } 

  public Entity findEntity(Vector entities, Vector assocs)
  { Vector readents = new Vector(); 
    Vector wrents = new Vector(); 
    classDependencies(entities,assocs,readents,wrents); 
    if (wrents.size() > 0) 
    { ent = (Entity) wrents.get(0); 
      return ent; 
    } 
    return null; 
  } 

  public void classDependencies(Vector entities, Vector assocs, Vector readents, Vector writtenents)
  { // Computes which entities are read, and which are written by the use case
    Vector wrf = wr(assocs); 
    Vector rdf = readFrame(); 

    for (int i = 0; i < entities.size(); i++) 
    { Entity e = (Entity) entities.get(i); 
      String ename = e.getName(); 
      if (wrf.contains(ename))
      { writtenents.add(e); } 
      else 
      { for (int j = 0; j < wrf.size(); j++) 
        { String v = (String) wrf.get(j); 
          if (v.startsWith(ename))
          { if (writtenents.contains(e)) { } 
            else 
            { writtenents.add(e); }
          }  
        } 
      } 

      if (rdf.contains(ename))
      { readents.add(e); } 
      else 
      { for (int j = 0; j < rdf.size(); j++) 
        { String v = (String) rdf.get(j); 
          if (v.startsWith(ename))
          { if (readents.contains(e)) { } 
            else 
            { readents.add(e); }
          }  
        } 
      } 
    } 
    System.out.println(""); 
    System.out.println("Use case " + this + " reads entities: " + readents); 
    System.out.println("Use case " + this + " writes entities: " + writtenents); 
    System.out.println("Associated entity: " + ent + " should be the only written entity.");
    System.out.println();  
  } 
    
  public void addStatement(Statement stat)
  { classifierBehaviour.addStatement(stat); } 

  public void addExtension(Extend ext)
  { // ext.extension /: uc1.extend.extension for any other uc1, and 
    // ext.extension /: uc1.include.addition for any other uc1
    extend.add(ext);
  } 

  public Vector extensionUseCases()
  { Vector res = new Vector(); 
    for (int i = 0; i < extend.size(); i++) 
    { Extend ext = (Extend) extend.get(i); 
      res.add(ext.extension); 
    } 
    return res; 
  } 

  public void addInclude(Include ext)
  { // ext.addition /: uc1.extend.extension for any other uc1
    include.add(ext); 
  } 

  public void removeInclude(String nme)
  { Vector removed = new Vector(); 
    for (int i = 0; i < include.size(); i++) 
    { Include inc = (Include) include.get(i); 
      if (inc.getName().equals(nme))
      { removed.add(inc); } 
    } 
    include.removeAll(removed); 
  } 

  public void removeExtend(String nme)
  { Vector removed = new Vector(); 
    for (int i = 0; i < extend.size(); i++) 
    { Extend enc = (Extend) extend.get(i); 
      if (enc.getName().equals(nme))
      { removed.add(enc); } 
    } 
    extend.removeAll(removed); 
  } 

  public void setExtend(Vector exts)
  { extend = exts; } 

  public void setInclude(Vector incs)
  { include = incs; } 

  public Vector getExtend()
  { return extend; } 

  public Vector getInclude()
  { return include; } 

  public boolean hasExtension(UseCase uc1)
  { for (int i = 0; i < extend.size(); i++) 
    { Extend ext = (Extend) extend.get(i); 
      if (uc1 == ext.getExtension())
      { return true; } 
    } 
    return false; 
  } 

  public boolean hasInclusion(UseCase uc1)
  { for (int i = 0; i < include.size(); i++) 
    { Include inc = (Include) include.get(i); 
      if (uc1 == inc.getInclusion())
      { return true; } 
    } 
    return false; 
  } 

  public void resolveExtendsIncludes(Vector useCases, UCDArea ucdArea)
  { for (int i = 0; i < includesList.size(); i++) 
    { String nme = (String) includesList.get(i); 
	  UseCase ucinc = (UseCase) ModelElement.lookupByName(nme,useCases); 

      if (ucinc == null) 
      { System.err.println("Invalid use case name: " + nme); } 
      else 
	  { Include ee = new Include(this,ucinc); 
        addInclude(ee);  
	    ucinc.addIncludedIn(this); 
        ucdArea.drawDependency(this, ucinc, "<<include>>"); 
      }
	} 
	
    for (int j = 0; j < extendsList.size(); j++) 
	{ String nme = (String) extendsList.get(j); 
	  UseCase ucext = (UseCase) ModelElement.lookupByName(nme,useCases); 

      if (ucext == null) 
      { System.err.println("Invalid use case name: " + nme); } 
      else
	  { Extend ee = new Extend(this,ucext); 
        addExtension(ee);  
	    ucext.addExtensionOf(this); 
        // Draw dashed line from ucext to uc
        ucdArea.drawDependency(ucext, this, "<<extend>>"); 
      }
    }
  } 

  public void analyseConstraints(Vector types, Vector entities, Vector assocs) 
  // calculates constraintKind and qvars, lvars
  { System.out.println("Analysing use case: " + getName() + "\n"); 
    JOptionPane.showMessageDialog(null, "Analysing use case " + getName(), 
                             "Use case analysis",
                             JOptionPane.INFORMATION_MESSAGE);  

    if (incremental) 
    { addIncrementalConstraints(types,entities); 
      Vector newposts = new Vector(); 
      newposts.addAll(deltaconstraints); 
      newposts.addAll(orderedPostconditions); 
      orderedPostconditions.clear(); 
      orderedPostconditions.addAll(newposts); 
    } 

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      if (con instanceof Constraint)
      { Constraint inv = (Constraint) con; 
        Vector rf = inv.internalReadFrame(); 
        Vector wf = inv.wr(assocs); 
        int constraintType = 0; 
        Entity owner = inv.getOwner(); 

        Expression defined = inv.definedness(); 
        System.out.println(""); 
        System.out.println("DEFINEDNESS of constraint " + i + " = " + defined); 
        System.out.println("SHOULD BE LOGICALLY VALID"); 
        System.out.println(""); 
       
        if (owner != null)
        { if (inv.getispreOwner())
          { rf.add(owner + "@pre"); } 
          else 
          { rf.add(owner + ""); } 
        } 

        System.out.println(""); 
        System.out.println("WRITE FRAME of constraint " + i + " = " + wf); 
        System.out.println("READ FRAME of constraint " + i + " = " + rf); 
        System.out.println(""); 

        Vector intersect = new Vector(); 
        intersect.addAll(rf); 
        intersect.retainAll(wf); 
     
        if (intersect.size() > 0)  // type 2 or 3 constraint
        { constraintType = 2; 
          inv.setConstraintKind(2); 
          if (wf.contains(owner + "") && rf.contains(owner + ""))
          { constraintType = 3; 
            inv.setConstraintKind(3); 
            System.out.println("Type 3 constraint: writes and reads " + 
                               intersect); 
            JOptionPane.showMessageDialog(null, "Constraint " + inv + "\n is of type 3", 
                           "Constraint analysis",
                           JOptionPane.INFORMATION_MESSAGE);   
            System.out.println("Fixpoint iteration needed. Proof using a variant\n"  + 
                               "needed for confluence & termination"); 
          } 
          else 
          { Vector anterf = inv.anteReadFrame(); 
            Vector intersect2 = new Vector(); 
            intersect2.addAll(anterf); 
            intersect2.retainAll(wf); 
            if (intersect2.size() > 0)
            { constraintType = 3; 
              inv.setConstraintKind(3); 
              System.out.println("Type 3 constraint: writes and reads " + 
                             intersect); 
              JOptionPane.showMessageDialog(null, "Constraint " + inv + "\n is of type 3", 
                             "Constraint analysis",
                             JOptionPane.INFORMATION_MESSAGE);   
              System.out.println("Fixpoint iteration needed. Proof using a variant\n"  + 
                                 "needed for confluence & termination"); 
            }
            else 
            { System.out.println("Type 2 constraint: writes and reads " + 
                               intersect); 
              JOptionPane.showMessageDialog(null, "Constraint " + inv + "\n is of type 2", 
                           "Constraint analysis",
                           JOptionPane.INFORMATION_MESSAGE);  
              System.out.println("Fixpoint iteration needed. Proof using a variant\n"  + 
                                 "needed for confluence & termination"); 
            }   
          }
        }
        else 
        { System.out.println("Type 1 constraint"); 
          // JOptionPane.showMessageDialog(null, "Constraint " + inv + "\n is of type 1", 
          //                    "Constraint analysis",
          //                    JOptionPane.INFORMATION_MESSAGE);  
          System.out.println(">-> Implementation by bounded loop.\n"  + 
                             ">-> Syntactic check for confluence & correctness."); 
          constraintType = 1; 
          inv.setConstraintKind(1); 
        } 

        int oldConstraintType = constraintType; 

        if (owner == null) 
        { constraintType = 0;
          inv.setConstraintKind(0);   
          System.out.println(">-> Owner is null; bounded loop will be used."); 
          if (oldConstraintType > 1)
          { System.out.println("But a bounded loop may not be sufficient!"); }  
        } 
          

        Vector lvars = new Vector(); 
        Vector qvars = inv.secondaryVariables(lvars,parameters);  
        System.out.println(">-> LET variables = " + lvars); 
        System.out.println(">-> QUANTIFIED variables = " + qvars); 
      }
    }

    Vector res = allPreTerms(); 
    System.out.println(">-> All pre-terms: " + res); 
  }   // no analysis of groups? 
   


  public void analyseDependencies(Vector assocs)
  { Vector possibleGroups = new Vector(); 

    System.out.println("\n"); 
          
    Vector newPostconds = new Vector(); 
    
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup ci = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Vector rdi = ci.internalReadFrame(); // or readFrame? plus the owner
      Vector wri = ci.wr(assocs); 
      for (int j = i+1; j < orderedPostconditions.size(); j++) 
      { ConstraintOrGroup cj = (ConstraintOrGroup) orderedPostconditions.get(j); 
        Vector wrj = cj.wr(assocs); 
        // wrj /\ rdi = {} and wrj /\ wri = {} should be true
        Vector inter1 = new Vector(); 
        inter1.addAll(rdi); 
        inter1.retainAll(wrj); 
        if (inter1.size() > 0)
        { System.out.println("Warning: constraint " + j + " writes data read in " +
                             "constraint " + i + ": "); 
          System.out.println(inter1); 
          JOptionPane.showMessageDialog(null, "Warning: constraint " + j + " writes data\n" + 
                             inter1 + " read in constraint " + i, 
                             "Possible invalid ordering of postconditions",
                             JOptionPane.WARNING_MESSAGE);  
          Vector rdj = cj.internalReadFrame(); // plus the owner 
          Vector inter2 = new Vector(); 
          inter2.addAll(rdj); 
          inter2.retainAll(wri); 
          if (inter2.size() > 0)
          { System.out.println("The constraints are mutually dependent: " + inter2);
            System.out.println("They should be in a constraint group."); 
            JOptionPane.showMessageDialog(null, "The constraints are mutually dependent: " + inter2 + 
                             "\n" + 
                             "I will put them in a constraint group.",
                             "Mutually dependent postconditions",
                             JOptionPane.WARNING_MESSAGE);  
            Vector grp = new Vector(); 
            if (ci instanceof Constraint) 
            { grp.add(ci); } 
            else 
            { grp.addAll(((ConstraintGroup) ci).elements); }  
            if (cj instanceof Constraint) 
            { grp.add(cj); } 
            else 
            { grp.addAll(((ConstraintGroup) cj).elements); }  
            ConstraintGroup cg = new ConstraintGroup(grp); 
            cg.findGroupType(assocs); 
            possibleGroups.add(cg); 
          } 
          else 
          { System.out.println("Try swapping their order"); 
            /* JOptionPane.showMessageDialog(null, "Try swapping their order", 
                             "Possible incorrect ordering of postconditions",
                             JOptionPane.WARNING_MESSAGE);  */
          } 
        }
        else 
        { System.out.println("Constraint " + i + " and " + j + " in correct order"); 
          // JOptionPane.showMessageDialog(null, "Constraints " + i + " and " + 
          //                           j + " are in correct order",
          //                           "Valid ordering of postconditions",
          //                           JOptionPane.INFORMATION_MESSAGE);  
        } 
        Vector inter3 = new Vector();
        inter3.addAll(wri); 
        inter3.retainAll(wrj); 
        if (inter3.size() > 0)
        { System.out.println("Possible error:\n" + 
                             "constraint " + j + " writes to same data as " + i); 
          System.out.println(inter3); 
          System.out.println("The later constraint may invalidate the earlier");
          JOptionPane.showMessageDialog(null, "Possible error:\n" + 
                                    "Constraint " + j + 
                                    " writes to same data " + inter3 + " as " + i + "\n" + 
                                    "The later constraint may invalidate the earlier",
                                    "Conflict between postconditions", JOptionPane.WARNING_MESSAGE);  
      
        } 
      } 
    } 
    System.out.println("Possible groups: " + possibleGroups); 
    System.out.println("\n"); 

    if (possibleGroups.size() == 0) { return; } 

    boolean somemerge = true; 
    while (somemerge)
    { somemerge = false; 
      Vector refinedGroups = new Vector(); 
      Vector merged = new Vector(); 

      for (int h = 0; h < possibleGroups.size(); h++) 
      { ConstraintGroup cg1 = (ConstraintGroup) possibleGroups.get(h); 
        for (int g = h+1; g < possibleGroups.size(); g++) 
        { ConstraintGroup cg2 = (ConstraintGroup) possibleGroups.get(g); 
          if (cg1.intersects(cg2))
          { ConstraintGroup cg3 = cg1.union(cg2); 
            if (refinedGroups.contains(cg3)) { } 
            else 
            { refinedGroups.add(cg3); }  
            merged.add(cg1); 
            merged.add(cg2);  
            somemerge = true; 
          } 
        }
      } 

      possibleGroups.removeAll(merged); 
      possibleGroups.addAll(refinedGroups); 
      System.out.println("Possible groups: " + possibleGroups.size() + " " + somemerge); 
    } 

    for (int j = 0; j < orderedPostconditions.size(); j++)
    { Constraint cc = (Constraint) orderedPostconditions.get(j); 
      ConstraintGroup cg = ConstraintGroup.findContainingGroup(possibleGroups,cc); 
      if (cg == null)
      { newPostconds.add(cc); } 
      else 
      { if (newPostconds.contains(cg)) { } 
        else 
        { newPostconds.add(cg); } 
      } 
    } 
    System.out.println("Possible new postconds: " + newPostconds); 

    orderedPostconditions.clear(); 
    orderedPostconditions.addAll(newPostconds); 
    classifierBehaviour = new SequenceStatement(); 
  } 

  public void mapToDesign(Vector types, Vector entities, Vector assocs)
  { // assume analyseDependencies has already been done, and derived = false

    Vector env = new Vector(); 
    env.addAll(parameters); 
    env.addAll(ownedAttribute); // and result

    if (bs != null) 
    { bs.generateDesign1(types,entities); } 

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cc = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Statement stat; 
      int tt = cc.getConstraintKind(assocs);  // assumes already computed
      if (tt == 0)
      { stat = cc.mapToDesign0(this); } 
      else if (tt == 1)  // Must be single constraint
      { stat = cc.mapToDesign1(this,types,entities,env); } 
      else if (tt == 2) 
      { if (cc instanceof Constraint && 
                 ((Constraint) cc).isPureDeletionConstraint(assocs))
        { stat = cc.mapToDesign1(this,types,entities,env); }
        else if (cc.hasAntecedent())
        { System.out.println("Type 2 constraint: " + cc); 
          System.out.println("Optimise by omitting test of not(succedent)?"); 
          String optimise = 
            JOptionPane.showInputDialog("Type 2. Optimise: omit not(succedent) check?: (y/n) ");
          stat = cc.mapToDesign2(this,optimise,types,entities);
        }
        else 
        { stat = cc.mapToDesign2(this,"n",types,entities); }
      }  
      else  
      { if (cc instanceof Constraint && 
                 ((Constraint) cc).isPureDeletionConstraint(assocs))
        { stat = cc.mapToDesign1(this,types,entities,env); }
        else if (cc.hasAntecedent())
        { System.out.println("Type 3 constraint: " + cc); 
          System.out.println("Optimise by omitting test of not(succedent)?"); 
          String optimise = 
            JOptionPane.showInputDialog("Type 3. Optimise: omit not(succedent) check?: (y/n) ");
          stat = cc.mapToDesign3(this,optimise,types,entities);
        }
        else 
        { stat = cc.mapToDesign3(this,"n",types,entities); }
      }

      Entity owner = null;
      Vector owns = new Vector();  
      Vector contexts = new Vector(); 

      if (cc instanceof ConstraintGroup)
      { ConstraintGroup cg = (ConstraintGroup) cc; 
        owns = cg.getOwners(); 
        if (owns.size() > 0)
        { owner = (Entity) owns.get(0); }
        contexts.addAll(owns); 
      } 
      else 
      { Constraint ccon = (Constraint) cc;   
        owner = ccon.getOwner(); 
        if (owner != null) 
        { contexts.add(owner); }  
      }
      contexts.add(classifier); 

      // System.out.println("Constraint code: " + stat); 
      if (stat == null) 
      { stat = new InvocationStatement("skip",null,null); } 
      
      if (owner != null) 
      { stat.setEntity(owner); }    

        // owner.setActivity(stat); 
      Vector newparms = new Vector();
      newparms.addAll(parameters); 
      newparms.addAll(ownedAttribute); // and result

      // System.out.println("Type-checking " + stat + " with " + newparms); 
      stat.typeCheck(types,entities,contexts,newparms);
      // System.out.println("Required parameters for code = " + newparms);  
      addStatement(stat); 
    } 
  }   

  public void identifyInputsOutputs(Vector assocs)
  { // assume analyseDependencies has already been done, and derived = false
  
    Vector newparms = new Vector();
    newparms.addAll(parameters); 
  
    Vector inputs = new Vector(); // written and read
	Vector outputs = new Vector(); // only written

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cc = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Vector ccrd = cc.readFrame(); 
	  Vector ccwr = cc.wr(assocs); 
	  DataDependency dd = cc.getDataFlows();
        System.out.println("Data flows for " + cc + " are: " + dd); 
       // DataDependency dd = cc.rhsDataDependency(); 
	  System.out.println(">> read in constraint " + i + " = " + ccrd); 
	  System.out.println(">> written in constraint " + i + " = " + ccwr);
	  System.out.println(">> Data-dependencies: " + dd);  
	}

        // owner.setActivity(stat); 
     newparms.addAll(ownedAttribute); // and result
     System.out.println(">>> Parameters = " + newparms); 
      // System.out.println("Type-checking " + stat + " with " + newparms); 
  }   

  public UseCase instantiate(Vector parvals, Vector types, Vector entities,
                             Vector assocs)
  { if (parvals.size() == 0) 
    { return this; } 
    // parvals.size() <= parameters.size() assumed 

    UseCase result = new UseCase(getName(),ent);
    result.setResultType(resultType); 
    result.setElementType(elementType); 
    result.setSuperclass(superclass); 

    Vector newparams = new Vector(); 
    int n = parvals.size(); 

    for (int i = n; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      newparams.add(par); 
    } 
    result.setParameters(newparams);

    Vector allparams = new Vector(); 
    allparams.addAll(newparams); 
    allparams.addAll(ownedAttribute); 

    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,INTERNAL); 
      resatt.setElementType(elementType); 
      allparams.add(resatt); 
    } 

    for (int j = 0; j < orderedPostconditions.size(); j++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(j); 
      ConstraintOrGroup con2 = (ConstraintOrGroup) con.clone(); 
      for (int i = 0; i < parvals.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
          
        con2 = con2.substituteEq(par + "",(Expression) parvals.get(i));  
      } 
      Entity owner = con.getOwner(); 
      con2.setOwner(owner);
      // con2.setownerisPre(con.getispreOwner()); 

      Vector contexts = new Vector(); 
      if (owner != null) 
      { contexts.add(owner); } 
      contexts.add(classifier); 

      con2.typeCheck(types,entities,contexts,allparams); 

      if (con2 instanceof Constraint)
      { Constraint ccon2 = (Constraint) con2; 
        Vector rf = ccon2.internalReadFrame(); 
        Vector wf = ccon2.wr(assocs); 
        if (owner != null)
        { if (ccon2.getispreOwner())
          { rf.add(owner + "@pre"); } 
          else 
          { rf.add(owner + ""); } 
        } 

        System.out.println("");  
        System.out.println("WRITE FRAME = " + wf); 
        System.out.println("READ FRAME = " + rf); 
        System.out.println(""); 
 
        int constraintType;      
        Vector intersect = new Vector(); 
        intersect.addAll(rf); 
        intersect.retainAll(wf); 
        if (intersect.size() > 0)  // type 2 or 3 constraint
        { System.out.println("Type 2 or 3 constraint: writes and reads " + 
                             intersect); 
          constraintType = 2; 
          // ccon2.setConstraintKind(2); 
          if (wf.contains(owner + "") && rf.contains(owner + ""))
          { constraintType = 3; 
            // ccon2.setConstraintKind(3); 
          } 
          else 
          { Vector anterf = ccon2.anteReadFrame(); 
            Vector intersect2 = new Vector(); 
            intersect2.addAll(anterf); 
            intersect2.retainAll(wf); 
            if (intersect2.size() > 0)
            { constraintType = 3; 
              // ccon2.setConstraintKind(3); 
            } 
          } 
        }
        else 
        { System.out.println("Type 1 constraint"); 
          constraintType = 1; 
          // ccon2.setConstraintKind(1); 
        } 
   
        result.addPostcondition(ccon2); 
        ccon2.setUseCase(result); 
      } 
      else 
      { result.addPostcondition((ConstraintGroup) con2); } 
    } 


    for (int j = 0; j < preconditions.size(); j++) 
    { Constraint con = (Constraint) preconditions.get(j); 
      Constraint con2 = (Constraint) con.clone(); 
      for (int i = 0; i < parvals.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
        con2 = (Constraint) con2.substituteEq(par + "",(Expression) parvals.get(i)); 
      } 
      Entity owner = con.getOwner(); 
      con2.setOwner(owner);
      Vector contexts = new Vector(); 
      if (owner != null) 
      { contexts.add(owner); } 
      contexts.add(classifier); 

      con2.typeCheck(types,entities,contexts,allparams); 
      result.addPrecondition(con2); 
      con2.setUseCase(result); 
    } 

    if (activity != null) 
    { Statement act2 = (Statement) activity.clone(); 

      for (int i = 0; i < parvals.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
          
        act2 = act2.substituteEq(par + "",(Expression) parvals.get(i));  
      } 
      result.setActivity(act2); 
    } 

    result.setExtend(extend); 
    result.setInclude(include); 
    // result.setParameters(newparams); 
    // result.setResultType(resultType); 
    // result.setSuperclass(superclass); 
    return result; 
  } 
 
  public int insertAfter(Constraint extcons, int k, Vector assocs)
  { // inserts extcons into the first position after k
    // where it is correct wrt wr, rd
    int n = orderedPostconditions.size(); 
    int j = n; // default: add at end

    Vector wrext = extcons.wr(assocs); 
    Vector rdext = extcons.internalReadFrame(); 

    for (int i = n-1; i >= k; i--) 
    { Constraint cn = (Constraint) orderedPostconditions.get(i); 
      if ((cn + "").equals(extcons + "")) { return i; } 

      Vector rdbase = cn.internalReadFrame(); 
      Vector wrbase = cn.wr(assocs); 
      // if (wrext /\ rdbase /= {}) move ext before cn:
      Vector inter1 = new Vector(); 
      inter1.addAll(rdbase); 
      inter1.retainAll(wrext); 
      if (inter1.size() > 0)
      { System.out.println(extcons + " MUST PRECEDE " + cn); 
        j = i;
      }
      // if (wrbase /\ rdext /= {}) ext must be after cn: 
      Vector inter2 = new Vector(); 
      inter2.addAll(wrbase); 
      inter2.retainAll(rdext); 
      if (inter2.size() > 0)
      { System.out.println(cn + " MUST PRECEDE " + extcons); 
        return i+1;
      } 
    }  
    return j; 
  } 

  public void insertPostconditionAt(Constraint p, int pos)
  { if (orderedPostconditions.contains(p)) { return; } 

    if (0 <= pos && pos < orderedPostconditions.size())
    { orderedPostconditions.add(pos,p); }
    else 
    { orderedPostconditions.add(p); } 
    p.setUseCase(this); 
  }  

  public void insertCodeAt(Statement p, int pos)
  { classifierBehaviour.addStatement(pos,p); } 
  // Is this ok at the end? 


  public UseCase invert(Vector types, Vector entities)
  { UseCase reverse = new UseCase(getName() + "Reverse",ent); 
    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      ConstraintOrGroup newcon = con.invert();
      if (newcon == null) { continue; } 

      Vector contexts = new Vector();  
      Entity nowner = newcon.getOwner(); 
      if (nowner != null) 
      { contexts.add(nowner); } 
      Vector newparams = new Vector(); 
      newparams.addAll(parameters); 
      newparams.addAll(ownedAttribute);
 
      newcon.typeCheck(types,entities,contexts,newparams); // to identify variables
      reverse.addPostcondition(newcon); 
      // if (con.constraintType == 1) { reverse.addInvariant(con); } 
      newcon.setUseCase(reverse); 
    }
    reverse.setInclude(include); 
    reverse.setExtend(extend);
    reverse.setParameters(parameters); 
    reverse.setSuperclass(superclass);  
    reverse.setResultType(resultType);  
    reverse.setElementType(elementType); 
    reverse.ownedAttribute = ownedAttribute; 
    reverse.ownedOperations = ownedOperations; 
    // reverse.setActivity(activity); 
    return reverse; 
  } 

  public void addIncrementalConstraints(Vector types, Vector entities) 
  { // For each  c : orderedPostconditions, create a Delta-version. Put these at start of uc. 
    int ucn = orderedPostconditions.size(); 

    for (int i = 0; i < ucn; i++) 
    { ConstraintOrGroup cg = (ConstraintOrGroup) orderedPostconditions.get(i); 
      if (cg instanceof Constraint) 
      { Constraint con = (Constraint) cg; 

        Constraint cdelta0 = con.deltaForm0(); 
        if (cdelta0 != null) 
        { deltaconstraints.add(cdelta0);
          cdelta0.setId(con.id + ucn + 1000);  
          Vector newparams = new Vector(); 
          newparams.addAll(parameters); 
          newparams.addAll(ownedAttribute);
          Vector contexts = new Vector();  
          Entity nowner = cdelta0.getOwner(); 
          if (nowner != null) 
          { contexts.add(nowner); } 
 
          cdelta0.typeCheck(types,entities,contexts,newparams);
        } 

        Constraint cdelta = con.deltaForm(); 
        if (cdelta != null) 
        { deltaconstraints.add(cdelta);
          cdelta.setId(con.id + ucn);  
          Vector newparams = new Vector(); 
          newparams.addAll(parameters); 
          newparams.addAll(ownedAttribute);
          Vector contexts = new Vector();  
          Entity nowner = cdelta.getOwner(); 
          if (nowner != null) 
          { contexts.add(nowner); } 
 
          cdelta.typeCheck(types,entities,contexts,newparams);
        } 
      } 
      else 
      { System.out.println("NOTE: Cannot produce delta-form for " + cg); } 
   } 
   System.out.println("Delta constraints are: " + deltaconstraints); 
  } 

  public void typeCheck(Vector types, Vector entities)
  { Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    newparams.addAll(ownedAttribute); // static attributes
	if (resultType != null)
	{ Attribute resultPar = getResultParameter(); 
	  newparams.add(resultPar); 
	}
    Vector context0 = new Vector(); 

    for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      Expression exp = att.getInitialExpression(); 
      if (exp != null) 
      { exp.typeCheck(types,entities,context0,newparams); } 
    } 
    // and operations

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Vector contexts = new Vector();  
      Entity nowner = con.getOwner(); 
      if (nowner != null) 
      { contexts.add(nowner); } 
      contexts.add(classifier); 
      con.typeCheck(types,entities,contexts,newparams); 
    } 

    // and the classifierBehaviour and activity

    Vector context = new Vector(); 
    context.add(classifier); 

    if (activity != null) 
    { activity.typeCheck(types, entities, context, newparams); } 
  } 
  
  public String toString()
  { return getName(); } 


  public String display()
  { int lowcount = 0; 
    int highcount = 0; 

    String res = "\n" + 
                "Use Case, name: " + getName() + "\n"; 
    res = res + "Parameters: " + parameters + "\n"; 
    res = res + "Result type: " + resultType + "\n"; 
    res = res + "Superclass: " + superclass + "\n"; 
    if (extend.size() > 0)
    { res = res + Extend.displayExtensions(extend); } 
    if (include.size() > 0)
    { res = res + Include.displayInclusions(include); } 
    res = res + "Description: " + description + "\n"; 
    res = res + "isIncremental: " + incremental + "\n"; 
    res = res + "isDerived: " + derived + "\n";
    res = res + "isGeneric: " + generic + "\n";  
    res = res + "Attributes: \n"; 

    int attsSize = ownedAttribute.size(); 
    for (int i = 0; i < attsSize; i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      res = res + "  " + att.getName() + " : " + att.getType(); 
      if (att.getInitialValue() != null) 
      { res = res + " = " + att.getInitialValue(); } 
      res = res + ";\n\n"; 
    } 

    System.out.println("*** Number of attributes = " + attsSize); 

    res = res + "Operations: \n"; 

    int opsSize = ownedOperations.size(); 
    for (int i = 0; i < opsSize; i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) ownedOperations.get(i); 
      res = res + "  " + bf.display(); 
      res = res + "\n\n"; 
    } 

    System.out.println("*** Number of operations = " + opsSize); 


    for (int i = 0; i < preconditions.size(); i++) 
    { Constraint con = (Constraint) preconditions.get(i); 
      res = res + "Precondition " + i + " is:\n  " + con + "\n\n"; 
    } 

    int rulesSize = orderedPostconditions.size(); 
    int totalSize = 0; 

    for (int i = 0; i < rulesSize; i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      res = res + "Postcondition " + i + " is:\n  " + con + "\n\n"; 
      int consize = con.syntacticComplexity(); 
      if (consize > 100) 
      { System.err.println("*** Warning: Constraint " + i + " too large: " + consize); 
        highcount++; 
      } 
      else if (consize > 50) 
      { System.err.println("*** Warning: Constraint " + i + " is large: " + consize); 
        lowcount++; 
      } 

      
      totalSize = totalSize + consize;       
      res = res + "complexity = " + consize + "\n\n"; 
    } 

    System.out.println(); 

    System.out.println("*** Number of rules = " + rulesSize); 

    System.out.println("*** Total rules complexity = " + totalSize); 

    System.out.println("*** " + highcount + " rules over complexity 100"); 
    System.out.println("*** " + lowcount + " other rules over complexity 50"); 
    System.out.println(); 

    for (int i = 0; i < constraints.size(); i++) 
    { Constraint con = (Constraint) constraints.get(i); 
      res = res + " Invariant " + i + " is: " + con + "\n\n"; 
    } 

    res = res + "Behaviour code: \n" + classifierBehaviour + "\n"; 
    res = res + "Activity: \n" + activity + "\n";  
    return res; 
  } 


  public int displayMeasures(PrintWriter out, java.util.Map clones)
  { out.println("----------------------------------------------\n" + 
                "*** Use Case, name: " + getName() + "\n"); 
    out.println("*** Number of parameters: " + parameters.size() + "\n"); 

    int attsSize = ownedAttribute.size(); 

    out.println("*** Number of attributes = " + attsSize + "\n"); 

    int totalSize = 0; 
    int highcount = 0; 
    int lowcount = 0; 

    int opsSize = ownedOperations.size(); 
    for (int i = 0; i < opsSize; i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) ownedOperations.get(i); 
      bf.findClones(clones); 
      int opsize = bf.displayMeasures(out); 
      totalSize = totalSize + opsize; 

      if (opsize > 100) 
      { System.out.println("*** Operation " + bf.getName() + " too large: " + opsize); 
        highcount++; 
      } 
      else if (opsize > 50) 
      { System.out.println("*** Operation " + bf.getName() + " is large: " + opsize); 
        lowcount++; 
      } 
    } 

    out.println("*** Number of operations = " + opsSize + "\n"); 
    if (opsSize > 10) 
    { System.err.println("*** Bad smell: too many operations (" + opsSize + ") in " + getName()); }  

    int rulesSize = orderedPostconditions.size(); 
    out.println("*** Number of rules = " + rulesSize + "\n"); 
    if (rulesSize > 10) 
    { System.err.println("*** Bad smell: too many rules (" + rulesSize + ") in " + getName()); }  

    for (int i = 0; i < rulesSize; i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      int consize = con.syntacticComplexity(); 
      out.println("*** Postcondition " + i + " size = " + consize + "\n"); 
      if (consize > 100) 
      { System.err.println("*** Bad smell: rule too large (" + consize + ") for " + getName() + i); 
        highcount++; 
      }
      else if (consize > 50) 
      { System.err.println("*** Warning: Rule is large (" + consize + ") for " + getName() + i); 
        lowcount++; 
      }
  
      totalSize = totalSize + consize;       
      int concc = con.cyclomaticComplexity(); 
      out.println("*** Postcondition " + i + " cyclomatic complexity = " + concc + "\n"); 
      if (concc > 10) 
      { System.err.println("*** Bad smell: rule cyclomatic complexity too high (" + concc + ") for " + getName() + i); }  

      con.findClones(clones); 

      Vector opuses = con.operationsUsedIn(); 
      if (opuses.size() > 0) 
      { out.println("*** Postcondition " + i + " uses operations: " + opuses); 
        if (opuses.size() > 10) 
        { System.err.println("*** Bad smell: rule uses too many operations (" + opuses.size() + ") for " + getName() + i); }  
        out.println(); 
      } 
    } 



    if (activity != null) 
    { totalSize = totalSize + activity.syntacticComplexity(); } 

   /*  java.util.Iterator keys = clones.keySet().iterator();
    while (keys.hasNext())
    { Object k = keys.next();
      Vector clonedIn = (Vector) clones.get(k); 
      if (clonedIn.size() > 1)
      { out.println(k + " is cloned in: " + clonedIn); 
        System.err.println("Bad smell: Cloned expression in " + getName()); 
      } 
    } */  

    out.println("*** Total transformation rules + owned operations complexity = " + totalSize + "\n");  
    out.println("*** " + highcount + " rules/operations over 100 complexity"); 
    out.println("*** " + lowcount + " other rules/operations over 50 complexity"); 

    if (totalSize > 1000) 
    { System.err.println("*** Bad smell: transformation too large (" + totalSize + ") " + getName()); }  
    return totalSize; 
  } 

  public Map getCallGraph()
  { Map res = new Map(); 
    String nme = getName(); 

    int opsSize = ownedOperations.size(); 
    for (int i = 0; i < opsSize; i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) ownedOperations.get(i); 
      Vector bfcalls = bf.operationsUsedIn(); 
      for (int j = 0; j < bfcalls.size(); j++) 
      { res.add_pair(nme + "::" + bf.getName(), bfcalls.get(j)); } 
    } 

    int rulesSize = orderedPostconditions.size(); 

    for (int i = 0; i < rulesSize; i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Vector opuses = con.operationsUsedIn(); 
      for (int j = 0; j < opuses.size(); j++) 
      { res.add_pair(nme + "_" + i, opuses.get(j)); }  
    } 
    return res; 
  } 

  public void generateJava(PrintWriter out) { }  

  public String getQueryCode(String language, Vector types, Vector entities)
  { String res = ""; 
    for (int i = 0; i < preconditions.size(); i++) 
    { Constraint p = (Constraint) preconditions.get(i); 
      String check = p.mapToCheckOp(language,this, i, types, entities); 
      if (check != null) 
      { res = res + check; }  
    } 
    return res; 
  } 

  public String getInvariantCheckCode(String language, Vector types, Vector entities)
  { String res = ""; 
    for (int i = 0; i < constraints.size(); i++) 
    { Constraint p = (Constraint) constraints.get(i); 
      String check = p.mapToCheckOp(language,this, i, types, entities); 
      if (check != null) 
      { res = res + check; }  
    } 
    return res; 
  } 

  public String getJavaParameterDec()  // from BehaviouralFeature
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType();
      if (typ == null) 
      { System.err.println("ERROR: No type for parameter " + att); 
        continue; 
      }  
      res = res + typ.getJava() + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getJava6ParameterDec()  // from BehaviouralFeature
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("ERROR: No type for parameter " + att); 
        continue; 
      }  
      res = res + typ.getJava6() + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getJava7ParameterDec()  // from BehaviouralFeature
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("ERROR: No type for parameter " + att); 
        continue; 
      }  
      
      res = res + typ.getJava7(att.getElementType()) + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getCSharpParameterDec()  // from BehaviouralFeature
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("ERROR: No type for parameter " + att); 
        continue; 
      }  
      
      res = res + typ.getCSharp() + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getCPPParameterDec()  
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("ERROR: No type for parameter " + att); 
        continue; 
      }  
            res = res + typ.getCPP(att.getElementType()) + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

public void generateCUIcode(PrintWriter out)
{ String nme = getName();
  out.println("        if (strcmp(\"" + nme + "\",uc) == 0)"); 
  out.println("        {");
  if (parameters.size() == 0)
  { out.println("          " + nme + "(); }");
    return;
  }
  String instring = "";
  String callstring = "";
  for (int j = 0; j < parameters.size(); j++)
  { Attribute p = (Attribute) parameters.get(j);
    Type t = p.getType();
    String pn = p.getName();
    String pref = "*" + pn;
    if ("String".equals(t.getName()))
    { pref = pn; }
    String ct = Expression.getCInputType(t);
    out.println("          " + ct + "* " + pn + " = (" + ct + "*) malloc(sizeof(" + ct + "));");
    instring = instring + "," + pn;
    callstring = callstring + pref;
    if (j < parameters.size()-1)
    { callstring = callstring + ","; }
  }
 
  out.println("          err = sscanf(cmd,format,uc" + instring + ");"); 
  out.println("          if (err == EOF)"); 
  out.println("          { printf(\"Invalid arguments for %s\\n\", res[j]); }");  
  out.println("          else { " + nme + "(" + callstring + "); }"); 
  out.println("        }");
}
 
  public String genOperation(Vector entities, Vector types)
  { if (derived || isAbstract) 
    { return ""; } 

    Vector pars = new Vector(); 
    pars.add(parameters); 

    String nme = getName(); 
    Vector contexts = new Vector(); 
    contexts.add(classifier); 
    java.util.Map env = new java.util.HashMap(); 

    if (classifierBehaviour == null) 
    { System.err.println("!! >>> No design exists for this use case " + nme);
      if (activity != null) 
      { return "  public void " + nme + "(" + getJavaParameterDec() + ") \n" + 
               "  { " + activity.updateForm(env,false,types,entities,pars) + " }\n"; 
      }
      return ""; 
    } 

    String typ = ""; 
    String ini = ""; 
    String ret = ""; 

    if (resultType == null) 
    { typ = "void"; } 
    else 
    { typ = resultType.getJava(); 
      ini = "    " + typ + " result = " + resultType.getDefault() + ";\n"; 
      ret = "    return result;\n"; 
    } 

  /*  for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      ini = ini + "    " + att.getType().getJava() + " " + att.getName(); 
      Expression initval = att.getInitialExpression(); 
      if (initval != null) 
      { ini = ini + " = " + initval.queryForm(env,false); } 
      else 
      { ini = ini + " = " + att.getType().getDefault(); } 
      ini = ini + ";\n"; 
    }  */ 
 
    String res = ""; 

    if (constraints != null && constraints.size() > 0) 
    { res = "  public void invcheck_" + nme + "(" + getJavaParameterDec() + ")\n";
      res = res + "  { " + getInvariantCheckCode("Java4", types, entities) + "\n";
      res = res + "  }\n";
    } 

    res = res + "\n  public " + typ + " " + nme + "(" + getJavaParameterDec() + ") \n"; 
    res = res + "  { \n" + ini + 
                // "    loadModel(\"in.txt\"); \n" + 
                // "    Controller.inst().checkCompleteness();\n" + 
                getQueryCode("Java4", types, entities);  


    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,ModelElement.INTERNAL); 
      resatt.setElementType(elementType); 
      newparams.add(resatt); 
    }

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      // System.err.println("PPAAMEMTER TYPE" + att.getType() + " " + att.getElementType()); 
    } 


    newparams.addAll(ownedAttribute); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = ""; 
    if (activity == null) 
    { if (incremental)
      { code = classifierBehaviour.deltaUpdateForm(env,false); } 
      else 
      { code = classifierBehaviour.updateForm(env,false,types,entities,newparams); } 
    } 

    // assume these are just sequentially composed, if no explicit activity:
    if (activity == null) 
    { for (int i = 0; i < include.size(); i++) 
      { Include inc = (Include) include.get(i); 
        UseCase subcase = inc.getInclusion(); 
        code = code + subcase.internalCode(entities,types); 
      }
    }  
    else // (activity != null) -- but subordinate use cases should not load, store models
    { code = code + "\n  " + activity.updateForm(env,false,types,entities,newparams); } 

    res = res + "\n  " + code + "\n" + 
                // "    Controller.inst().saveModel(\"out.txt\");\n" + 
                // "    Controller.inst().saveXSI(\"xsi.txt\");\n" + 
                ret + 
                "  }\n\n";

  /*  for (int i = 0; i < ownedOperations.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) ownedOperations.get(i); 
      Entity ent = bf.getEntity(); 
      res = res + "  " + bf.getOperationCode(ent, entities, types) + "\n\n"; 
    } */ 

    return res; 
  } // should have any parameters remaining after instantiation

  public String genOperationJava6(Vector entities, Vector types)
  { if (derived || isAbstract) 
    { return ""; } 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector contexts = new Vector(); 
    String nme = getName(); 
    String typ = ""; 
    String ini = ""; 
    String ret = ""; 

    if (resultType == null) 
    { typ = "void"; } 
    else 
    { typ = resultType.getJava6(); 
      ini = "    " + typ + " result = " + resultType.getDefaultJava6() + ";\n"; 
      ret = "    return result;\n"; 
    } 

    /* for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      ini = ini + "    " + att.methodDeclarationJava6();
    }  */ 

    String res = "  public " + typ + " " + nme + "(" + getJava6ParameterDec() + ") \n"; 
    res = res + "  { \n" + ini + 
                // "    loadModel(\"in.txt\"); \n" + 
                // "    Controller.inst().checkCompleteness();\n" + 
                getQueryCode("Java6", types, entities);  

    java.util.Map env = new java.util.HashMap(); 

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,ModelElement.INTERNAL); 
      resatt.setElementType(elementType); 
      newparams.add(resatt); 
    }
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = ""; 
    if (activity == null) 
    { code = classifierBehaviour.updateFormJava6(env,false); } 

    // assume these are just sequentially composed, if no explicit activity:
    if (activity == null) 
    { for (int i = 0; i < include.size(); i++) 
      { Include inc = (Include) include.get(i); 
        UseCase subcase = inc.getInclusion(); 
        code = code + subcase.internalCodeJava6(entities,types); 
      }
    }  
    else // (activity != null) 
    { code = code + "\n  " + activity.updateFormJava6(env,false); } 

    res = res + "\n  " + code + "\n" + 
                // "    Controller.inst().saveModel(\"out.txt\");\n" + 
                // "    Controller.inst().saveXSI(\"xsi.txt\");\n" + 
                ret + 
                "  }\n\n";
    return res; 
  } // should have any parameters remaining after instantiation

  public String genOperationJava7(Vector entities, Vector types)
  { if (derived || isAbstract) 
    { return ""; } 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector contexts = new Vector(); 
    String nme = getName(); 
    String typ = ""; 
    String ini = ""; 
    String ret = ""; 

    if (resultType == null) 
    { typ = "void"; } 
    else 
    { typ = resultType.getJava7(elementType); 
      ini = "    " + typ + " result = " + resultType.getDefaultJava7() + ";\n"; 
      ret = "    return result;\n"; 
    } 

    /* for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      ini = ini + "    " + att.methodDeclarationJava6();
    }  */ 

    String res = "  public " + typ + " " + nme + "(" + getJava7ParameterDec() + ") \n"; 
    res = res + "  { \n" + ini + 
                // "    loadModel(\"in.txt\"); \n" + 
                // "    Controller.inst().checkCompleteness();\n" + 
                getQueryCode("Java7", types, entities);  

    java.util.Map env = new java.util.HashMap(); 

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,ModelElement.INTERNAL); 
      resatt.setElementType(elementType); 
      newparams.add(resatt); 
    }
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = ""; 
    if (activity == null) 
    { code = classifierBehaviour.updateFormJava7(env,false); } 

    // assume these are just sequentially composed, if no explicit activity:
    if (activity == null) 
    { for (int i = 0; i < include.size(); i++) 
      { Include inc = (Include) include.get(i); 
        UseCase subcase = inc.getInclusion(); 
        code = code + subcase.internalCodeJava7(entities,types); 
      }
    }  
    else // (activity != null) 
    { code = code + "\n  " + activity.updateFormJava7(env,false); } 

    res = res + "\n  " + code + "\n" + 
                // "    Controller.inst().saveModel(\"out.txt\");\n" + 
                // "    Controller.inst().saveXSI(\"xsi.txt\");\n" + 
                ret + 
                "  }\n\n";
    return res; 
  } // should have any parameters remaining after instantiation

  public String genOperationCSharp(Vector entities, Vector types)
  { if (derived || isAbstract) 
    { return ""; } 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector contexts = new Vector(); 
    String nme = getName(); 
    String typ = ""; 
    String ini = ""; 
    String ret = ""; 

    if (resultType == null) 
    { typ = "void"; } 
    else 
    { typ = resultType.getCSharp(); 
      ini = "    " + typ + " result = " + resultType.getDefaultCSharp() + ";\n"; 
      ret = "    return result;\n"; 
    } 

    /* for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      ini = ini + "    " + att.methodDeclarationCSharp();
    }  */ 

    String res = "  public " + typ + " " + nme + "(" + getCSharpParameterDec() + ") \n"; 
    res = res + "  { \n" + ini + 
                // "    loadModel(\"in.txt\"); \n" + 
                // "    Controller.inst().checkCompleteness();\n" + 
                getQueryCode("CSharp", types, entities);  

    java.util.Map env = new java.util.HashMap(); 

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,ModelElement.INTERNAL); 
      resatt.setElementType(elementType); 
      newparams.add(resatt); 
    }
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = ""; 
    if (activity == null) 
    { code = classifierBehaviour.updateFormCSharp(env,false); } 

    // assume these are just sequentially composed, if no explicit activity:
    if (activity == null) 
    { for (int i = 0; i < include.size(); i++) 
      { Include inc = (Include) include.get(i); 
        UseCase subcase = inc.getInclusion(); 
        code = code + subcase.internalCodeCSharp(entities,types); 
      }
    }  
    else // (activity != null) 
    { code = code + "\n  " + activity.updateFormCSharp(env,false); } 

    res = res + "\n  " + code + "\n" + 
                // "    Controller.inst().saveModel(\"out.txt\");\n" + 
                // "    Controller.inst().saveXSI(\"xsi.txt\");\n" + 
                ret + 
                "  }\n\n";
    return res; 
  } // should have any parameters remaining after instantiation

  public String genOperationCPP(Vector entities, Vector types, Vector mainopcodes)
  { if (derived || isAbstract) 
    { return ""; } 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector contexts = new Vector(); 
    String nme = getName(); 
    String typ = ""; 
    String ini = ""; 
    String ret = ""; 

    if (resultType == null) 
    { typ = "void"; } 
    else 
    { typ = resultType.getCSharp(); 
      ini = "    " + typ + " result = " + resultType.getDefaultCPP(elementType.getCPP()) + ";\n"; 
      ret = "    return result;\n"; 
    } 

   /*  for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute att = (Attribute) ownedAttribute.get(i); 
      ini = ini + "    " + att.methodDeclarationCPP();
    } */  


    String pardec = getCPPParameterDec(); 
    String res2 = "  " + typ + " " + nme + "(" + pardec + "); \n";
    String res = "  " + typ + " Controller::" + nme + "(" + pardec + ") \n" + 
                  "  { \n" + ini + 
                // "    loadModel(\"in.txt\"); \n" + 
                // "    Controller.inst().checkCompleteness();\n" + 
                getQueryCode("CPP", types, entities);  

    java.util.Map env = new java.util.HashMap(); 

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute resatt = new Attribute("result",resultType,ModelElement.INTERNAL); 
      resatt.setElementType(elementType); 
      newparams.add(resatt); 
    }
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = ""; 
    if (activity == null) 
    { code = classifierBehaviour.updateFormCPP(env,false); } 

    // assume these are just sequentially composed, if no explicit activity:
    if (activity == null) 
    { for (int i = 0; i < include.size(); i++) 
      { Include inc = (Include) include.get(i); 
        UseCase subcase = inc.getInclusion(); 
        code = code + subcase.internalCodeCPP(entities,types); 
      }
    }  
    else // (activity != null) 
    { code = code + "\n  " + activity.updateFormCPP(env,false); } 

    res = res + "\n  " + code + "\n" + 
                // "    Controller::inst->saveModel(\"out.txt\");\n" + 
                // "    Controller::inst->saveXSI(\"xsi.txt\");\n" + 
                ret + 
                "  }\n\n";
    mainopcodes.add(res); 
    return res2; 
  } // should have any parameters remaining after instantiation


  public String internalCode(Vector entities, Vector types)
  { java.util.Map env = new java.util.HashMap(); 
    Vector contexts = new Vector(); 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
      newparams.add(att);
    }
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = classifierBehaviour.updateForm(env,false,types,entities,newparams);
    return code; 
  } 

  public String internalCodeJava6(Vector entities, Vector types)
  { java.util.Map env = new java.util.HashMap(); 
    Vector contexts = new Vector(); 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
      newparams.add(att);
    } 
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = classifierBehaviour.updateFormJava6(env,false);
    return code; 
  } 

  public String internalCodeJava7(Vector entities, Vector types)
  { java.util.Map env = new java.util.HashMap(); 
    Vector contexts = new Vector(); 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
      newparams.add(att);
    } 
    newparams.addAll(ownedAttribute); 
    contexts.add(classifier); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = classifierBehaviour.updateFormJava7(env,false);
    return code; 
  } 

  public String internalCodeCSharp(Vector entities, Vector types)
  { java.util.Map env = new java.util.HashMap(); 
    Vector contexts = new Vector(); 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
      newparams.add(att);
    }
    newparams.addAll(ownedAttribute); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = classifierBehaviour.updateFormCSharp(env,false);
    return code; 
  } 

  public String internalCodeCPP(Vector entities, Vector types)
  { java.util.Map env = new java.util.HashMap(); 
    Vector contexts = new Vector(); 

    if (classifierBehaviour == null) 
    { System.err.println("No design exists for this use case");
      return ""; 
    }

    Vector newparams = new Vector(); 
    newparams.addAll(parameters); 
    if (resultType != null) 
    { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
      newparams.add(att);
    }
    newparams.addAll(ownedAttribute); 

    classifierBehaviour.typeCheck(types,entities,contexts,newparams); 
    String code = classifierBehaviour.updateFormCPP(env,false);
    return code; 
  } 
  
  public void saveData(PrintWriter out, Vector saved)
  { if (derived) { return; } 

    String extlist = ""; 
    String inclist = ""; 

    // and extensions, inclusions
    for (int j = 0; j < extend.size(); j++) 
    { Extend ee = (Extend) extend.get(j); 
      UseCase ext = ee.extension; 
      extlist = extlist + ext.getName() + " "; 

      if (saved.contains(ext.getName())) { } 
      else 
      { // saved.add(ext.getName()); 
        ext.saveData(out,saved); 
      }  
    }       
    out.println();
    out.println();
  
    for (int j = 0; j < include.size(); j++) 
    { Include ee = (Include) include.get(j); 
       
      UseCase inc = ee.addition; 
      inclist = inclist + inc.getName() + " "; 

      if (saved.contains(inc.getName())) { } 
      else 
      { inc.saveData(out,saved); }  
    }       
    out.println();
    out.println();

    String nme = getName();

    if (saved.contains(nme)) // already printed
    { return; } 
    
    out.println("GeneralUseCase:");
    out.print(nme + " ");
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      out.print(par.getName() + " " + par.getType() + " "); 
    }  // and result
    if (resultType != null) 
    { out.print("result " + resultType); } 
    out.println(); 
    out.println(extlist); 
    out.println(inclist); 

    saved.add(nme); 

    for (int i = 0; i < ownedAttribute.size(); i++) 
    { Attribute par = (Attribute) ownedAttribute.get(i); 
      out.print(par.getName() + " " + par.getType() + " "); 
    }  // missing initial values 
    out.println();  

    String stereos = incremental + "";
    for (int h = 0; h < stereotypes.size(); h++) 
    { stereos = stereos + " " + stereotypes.get(h); } 
 
    out.println(stereos);
    out.println();  
    out.println();  


    for (int i = 0; i < ownedOperations.size(); i++) 
    { BehaviouralFeature op = (BehaviouralFeature) ownedOperations.get(i); 
      op.saveData(out);
      // out.println(nme); 
      out.println();  
    } 


    for (int i = 0; i < preconditions.size(); i++) 
    { Constraint con = (Constraint) preconditions.get(i); 
      con.saveAssertion(out);
      // out.println(nme); 
      out.println();  
    } 

    for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
      con.saveData(out);
      // out.println(nme); 
      out.println();  
    } 

    for (int i = 0; i < constraints.size(); i++) 
    { Constraint con = (Constraint) constraints.get(i); 
      con.saveInvariant(out);
      // out.println(nme); 
      out.println();  
    } 


    // if (activity != null) 
    // { activity.saveData(out); }

  }  // and the activity

  public void saveInterfaceDescription(PrintWriter out)
  { String nme = getName(); 
    String res = nme + " ";
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      Type t = par.getType(); 
      res = res + t.getName() + " "; 
    } 
    res = res + " : "; 
    if (resultType != null) 
    { res = res + resultType.getName(); } 
    else 
    { res = res + "void"; } 
    out.println(res); 
  }  

 


  public void saveModelData(PrintWriter out, Vector saved, Vector entities, Vector types)
  { // if (derived) { return; } 

    Vector extlist = new Vector(); 
    Vector inclist = new Vector(); 

    // and extensions, inclusions
    for (int j = 0; j < extend.size(); j++) 
    { Extend ee = (Extend) extend.get(j); 
      UseCase ext = ee.extension; 
      extlist.add(ext.getName()); 

      if (saved.contains(ext.getName())) { } 
      else 
      { // saved.add(ext.getName()); 
        ext.saveModelData(out,saved,entities,types); 
      }  
    }       
    out.println();
    out.println();
  
    for (int j = 0; j < include.size(); j++) 
    { Include ee = (Include) include.get(j); 
       
      UseCase inc = ee.addition; 
      inclist.add(inc.getName()); 

      if (saved.contains(inc.getName())) { } 
      else 
      { inc.saveModelData(out,saved,entities,types); }  
    }       
    out.println();
    out.println();

    String nme = getName();

    if (saved.contains(nme)) { }     
    else 
    { out.println(nme + " : UseCase");
      out.println(nme + ".name = \"" + nme + "\"");
      out.println(nme + ".typeId = \"" + nme + "\"");
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
        String parid = par.saveModelData(out);
        out.println(parid + " : " + nme + ".parameters");  
      }  // and result

      if (resultType != null) 
      { String rtname = resultType.getUMLModelName(out); 
        out.println(nme + ".resultType = " + rtname); 
      } 
      else 
      { out.println(nme + ".resultType = void"); } 

      out.println();

      if (generic) 
      { out.println(nme + ".isGeneric = true"); } 
      if (derived) 
      { out.println(nme + ".isDerived = true"); } 
    
      for (int i = 0; i < extlist.size(); i++) 
      { out.println(extlist.get(i) + " : " + nme + ".extensions"); }  
     
      for (int i = 0; i < inclist.size(); i++) 
      { out.println(inclist.get(i) + " : " + nme + ".inclusions"); }  
    
      saved.add(nme); 

      if (classifier != null && classifier.notEmpty())
      { classifier.asTextModel(out); 
        classifier.asTextModel2(out,entities,types); 
      } 
      else 
      { for (int i = 0; i < ownedAttribute.size(); i++) 
        { Attribute par = (Attribute) ownedAttribute.get(i); 
          par.saveModelData(out); 
        }  // missing initial values 
        out.println();  
        out.println();  
        out.println();  

        for (int i = 0; i < ownedOperations.size(); i++) 
        { BehaviouralFeature op = (BehaviouralFeature) ownedOperations.get(i); 
          String opid = op.saveModelData(out,classifier,entities,types);
          out.println(opid + " : " + nme + ".ownedOperation");  
        } 
      } 


      for (int i = 0; i < preconditions.size(); i++) 
      { Constraint con = (Constraint) preconditions.get(i); 
        String preid = con.saveModelData(out);
        out.println(preid + " : " + nme + ".assumptions");  
      }  

      for (int i = 0; i < orderedPostconditions.size(); i++) 
      { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
        String conid = con.saveModelData(out);
        out.println(conid + " : " + nme + ".orderedPostconditions"); 
        out.println();  
      } 

      for (int i = 0; i < constraints.size(); i++) 
      { Constraint con = (Constraint) constraints.get(i); 
        String invid = con.saveModelData(out);
        out.println(invid + " : " + nme + ".constraint");  
      }  

      if (classifierBehaviour != null) 
      { java.util.Map env = new java.util.HashMap(); 

        Statement stat; 
        if (bx) 
        { stat = classifierBehaviour.statLC(env,false); } 
        else 
        { stat = classifierBehaviour.generateDesign(env,false); } 
 
        Vector newparams = new Vector(); 
        newparams.addAll(parameters); 
        if (resultType != null) 
        { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
          att.setElementType(elementType); 
          newparams.add(att);
        }
        Vector contexts = new Vector(); 
        newparams.addAll(ownedAttribute); 

        Statement newstat = stat; 

        if (activity != null) 
        { newstat = new SequenceStatement(); 
          ((SequenceStatement) newstat).addStatement(stat); 
          Statement actstat = activity.generateDesign(env,false); 
          ((SequenceStatement) newstat).addStatement(actstat); 
        }
      
        newstat.typeCheck(types,entities,contexts,newparams); 
        System.out.println(newstat); 
        String behaviour2id = newstat.saveModelData(out);
        out.println(nme + ".classifierBehaviour = " + behaviour2id); 
      }

    }  
  }  


  public Statement implementBehaviour(Vector types, Vector entities)
  { System.out.println(">>> Activity = " + classifierBehaviour); 

    if (classifierBehaviour != null) 
    { java.util.Map env = new java.util.HashMap(); 

      Statement stat; 
      if (bx) 
      { stat = classifierBehaviour.statLC(env,false); } 
      else 
      { stat = classifierBehaviour.generateDesign(env,false); } 

      System.out.println(">>> Activity = " + stat); 
 
      Vector newparams = new Vector(); 
      newparams.addAll(parameters); 
      if (resultType != null) 
      { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
        att.setElementType(elementType); 
        newparams.add(att);
      }
      Vector contexts = new Vector(); 
      newparams.addAll(ownedAttribute); 

      Statement newstat = stat; 

      if (activity != null) 
      { newstat = new SequenceStatement(); 
        ReturnStatement returnstat = null; 
        if (resultType != null && !("void".equals(resultType + "")))
        { Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
          att.setElementType(elementType);
	     CreationStatement cres = new CreationStatement(resultType + "", "result");
	     cres.setInstanceType(resultType);
	     cres.setElementType(elementType);   
	     ((SequenceStatement) newstat).addStatement(cres);
	     returnstat = new ReturnStatement(new BasicExpression(att));   
        }
        ((SequenceStatement) newstat).addStatement(stat); 
        Statement actstat = activity.generateDesign(env,false); 
        ((SequenceStatement) newstat).addStatement(actstat); 
	   if (returnstat != null) 
	   { ((SequenceStatement) newstat).addStatement(returnstat); }
      }
      else if (resultType != null && !("void".equals(resultType + "")))
      { newstat = new SequenceStatement(); 
        Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
        att.setElementType(elementType);
	   CreationStatement cres = new CreationStatement(resultType + "", "result");
	   cres.setInstanceType(resultType);
	   cres.setElementType(elementType);   
	   ((SequenceStatement) newstat).addStatement(cres);
        ((SequenceStatement) newstat).addStatement(stat); 
	   ReturnStatement returnstat = null; 
         returnstat = new ReturnStatement(new BasicExpression(att));   
	   ((SequenceStatement) newstat).addStatement(returnstat);
      }
        
      
      newstat.typeCheck(types,entities,contexts,newparams); 
      return newstat; 
    } 
    return new SequenceStatement(); 
  } 

  public void saveKM3(PrintWriter out, Vector saved)
  { // if (derived) { return; } 

    Vector extlist = new Vector(); 
    Vector inclist = new Vector(); 

    // and extensions, inclusions
    for (int j = 0; j < extend.size(); j++) 
    { Extend ee = (Extend) extend.get(j); 
      UseCase ext = ee.extension; 
      extlist.add(ext.getName()); 
    }       
  
    for (int j = 0; j < include.size(); j++) 
    { Include ee = (Include) include.get(j); 
       
      UseCase inc = ee.addition; 
      inclist.add(inc.getName()); 

      if (saved.contains(inc.getName())) { } 
      else 
      { inc.saveKM3(out,saved); }  
    }       
    out.println();
    out.println();

    String nme = getName();
	
	String retType = "void"; 
	if (resultType != null)
	{ retType = resultType + ""; }

    if (saved.contains(nme)) { }     
    else 
    { out.println("  usecase " + nme + " : " + retType + " {");
         
      for (int i = 0; i < inclist.size(); i++) 
      { out.println("    includes " + inclist.get(i) + ";"); }  
    
      for (int i = 0; i < extlist.size(); i++) 
      { out.println("    extendedBy " + extlist.get(i) + ";"); }  

      saved.add(nme); 
    
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
        out.println("    parameter " + par.getName() + " : " + par.getType() + ";");  
      }  // and result

      out.println();
      
      for (int i = 0; i < stereotypes.size(); i++) 
      { String stereo = (String) stereotypes.get(i); 
        out.println("  stereotype " + stereo + ";"); 
      }

      out.println();
      
	  
      for (int i = 0; i < ownedAttribute.size(); i++) 
      { Attribute par = (Attribute) ownedAttribute.get(i); 
        out.println("    attribute " + par.getName() + " : " + par.getType() + ";");  
      }  // and result

      out.println();
      

      for (int i = 0; i < preconditions.size(); i++) 
      { Constraint con = (Constraint) preconditions.get(i); 
        Expression ante = con.antecedent(); 
        Expression succ = con.succedent(); 
        if (ante == null || "true".equals(ante + ""))
        { out.println("  precondition " + succ + ";"); } 
        else 
        { out.println("  precondition " + ante + " => " + succ + ";"); }  
      }  

      out.println(); 
	  
      for (int i = 0; i < orderedPostconditions.size(); i++) 
      { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
        con.saveKM3(out);
        out.println();  
      } 
	  
	  if (activity != null) 
	  { out.println("  activity: ");  
	    out.println("    " + activity + ";"); 
	  }

      out.println("  }"); 
    } 
  }  

  public String getKM3()
  { Vector saved = new Vector(); 
    return getKM3(saved); 
  } 

  public String getKM3(Vector saved)
  { // if (derived) { return; } 
    String res = ""; 

    Vector extlist = new Vector(); 
    Vector inclist = new Vector(); 

    // and extensions, inclusions
    for (int j = 0; j < extend.size(); j++) 
    { Extend ee = (Extend) extend.get(j); 
      UseCase ext = ee.extension; 
      extlist.add(ext.getName()); 
    }       
  
    for (int j = 0; j < include.size(); j++) 
    { Include ee = (Include) include.get(j); 
       
      UseCase inc = ee.addition; 
      inclist.add(inc.getName()); 

      if (saved.contains(inc.getName())) { } 
      else 
      { res = res + inc.getKM3(saved) + "\n"; }  
    }       
    res = res + "\n" + "\n";
    

    String nme = getName();
	String retType = "void"; 
	if (resultType != null)
	{ retType = resultType + ""; }

    if (saved.contains(nme)) { }     
    else 
    { res = res + "  usecase " + nme + " : " + retType + " {\n";
         
      for (int i = 0; i < inclist.size(); i++) 
      { res = res + "    includes " + inclist.get(i) + ";\n"; }  
    
      for (int i = 0; i < extlist.size(); i++) 
      { res = res + "    extendedBy " + extlist.get(i) + ";\n"; }  

      saved.add(nme); 
    
      res = res + "\n"; 
	  
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
        res = res + "    parameter " + par.getName() + " : " + par.getType() + ";\n";  
      }  // and result

      res = res + "\n";

      for (int i = 0; i < stereotypes.size(); i++) 
      { String stereo = (String) stereotypes.get(i); 
        res = res + "  stereotype " + stereo + ";\n\r"; 
      }

      res = res + "\n";

      for (int i = 0; i < ownedAttribute.size(); i++) 
      { Attribute par = (Attribute) ownedAttribute.get(i); 
        res = res + "    attribute " + par.getName() + " : " + par.getType() + ";\n";  
      }  // and result

      /* for (int i = 0; i < ownedAttribute.size(); i++) 
      { Attribute par = (Attribute) ownedAttribute.get(i); 
        par.saveModelData(out); 
      }  
      out.println();  
      out.println();  
      out.println();  

      for (int i = 0; i < ownedOperations.size(); i++) 
      { BehaviouralFeature op = (BehaviouralFeature) ownedOperations.get(i); 
        String opid = op.saveModelData(out,classifier,entities,types);
        out.println(opid + " : " + nme + ".ownedOperation");  
      } 

       */ 
	   
      for (int i = 0; i < preconditions.size(); i++) 
      { Constraint con = (Constraint) preconditions.get(i); 
        // String preid = con.saveModelData(out);
        Expression ante = con.antecedent(); 
        Expression succ = con.succedent(); 
        if (ante == null || "true".equals(ante + ""))
        { res = res + " precondition " + succ + ";\n"; } 
        else 
        { res = res + " precondition " + ante + " => " + succ + ";\n"; } 
      }  
	  
	  res = res + "\n"; 

      for (int i = 0; i < orderedPostconditions.size(); i++) 
      { ConstraintOrGroup con = (ConstraintOrGroup) orderedPostconditions.get(i); 
        res = res + con.getKM3() + "\n";
      } 

      if (activity != null) 
      { res = res + "  activity:\n ";  
        res = res + "    " + activity + ";\n"; 
      }

      /* for (int i = 0; i < constraints.size(); i++) 
      { Constraint con = (Constraint) constraints.get(i); 
        String invid = con.saveModelData(out);
        out.println(invid + " : " + nme + ".constraint");  
      }  */ 
      res = res + "  }\n"; 
    } 
    return res; 
  }  


  public void applyPhasedConstruction(Vector types, Vector entities)
  { Vector newposts = new Vector(); 
    boolean applied = false; 

    for (int i = 0; i < orderedPostconditions.size(); i++)
    { ConstraintOrGroup cn = (ConstraintOrGroup) orderedPostconditions.get(i); 
      Vector cis = cn.applyCIForm(); 
      if (cis.size() == 0) 
      { newposts.add(cn); } 
      else 
      { newposts.addAll(cis); 
        applied = true; 
      } 
    } 

    if (applied == true) 
    { System.out.println("Applied Phased Construction, new posts: "); 
      System.out.println(newposts); 
      orderedPostconditions.clear(); 
      orderedPostconditions.addAll(newposts); // and renumber and re-type check
      typeCheck(types,entities); 
      resetIds(); 
    } 
  }     

  private void resetIds()  //  renumber()
  { for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cn = (ConstraintOrGroup) orderedPostconditions.get(i); 
      cn.setId(i); 
    } 
  } 

   public void generateControllerBeanAttributes(PrintWriter out)
   { String nme = getName();
     
     for (int i = 0; i < parameters.size(); i++)
     { Attribute att = (Attribute) parameters.get(i);
       String par = att.getName();
       Type typ = att.getType();         
       String tname = typ.getJava();
       out.println("  " + tname + " " + par + ";\n\r");
        /* if ("String".equals(tname)) {}
        else if ("int".equals(tname) || "long".equals(tname) || "double".equals(tname))
        { out.println("  " + tname + "  _" + nme + "_" + par + ";\n\r"); } */ 
      } 
      if (resultType != null)
      { // String rtyp = resultType.getJava();
        out.println("  String result;\n\r");
      }
    }  
 

   public void generateControllerBeanOps(PrintWriter out)
   { // set and get ops for attributes,
     // and the op itself

      String nme = getName();
      // String parsetname = (nme.charAt(0) + "").toUpperCase() + nme.substring(1,nme.length());
      String opcallpars = "";
      for (int i = 0; i < parameters.size(); i++)
      { Attribute att = (Attribute) parameters.get(i);
        String par = att.getName();
        Type typ = att.getType();         
        String tname = typ.getJava();
        // String parname = nme + "_" + par;
        opcallpars = opcallpars + par;
        if (i < parameters.size()-1)
        { opcallpars = opcallpars + ","; }

        out.println("  public void set" + par + "(String _s)\n\r");
        out.println("  {\n\r"); 
        String convertcode = "    " + par + " = _s;";  
        if ("String".equals(tname)) {}
        else if ("int".equals(tname))
        { convertcode = "    try { " + par + " = Integer.parseInt(_s); } catch (Exception _e) { return; }";
        }
        else if ("long".equals(tname))
        { convertcode = "    try { " + par + " = Long.parseLong(_s); } catch (Exception _e) { return; }";
        }
        else if ("double".equals(tname))
         { convertcode = "    try { " + par + " = Double.parseDouble(_s); } catch (Exception _e) { return; }";
        }
        else if ("boolean".equals(tname))
        { convertcode = "    if (\"true\".equals(_s) { " + par + " = true; } else { " + par + " = false; }"; 
        }
        out.println(convertcode + "\n\r");
        out.println("  }\n\r\n\r");
      }
      out.println("  public void " + nme + "()\n\r");
      if (resultType == null)
      { out.println("  { cont." + nme + "(" + opcallpars + "); }\n\r\n\r"); } 
      else // (resultType != null)
      { // String rtyp = resultType.getJava();
        out.println("  { result = \"\" + cont." + nme + "(" + opcallpars + "); }\n\r\n\r");
        out.println("  public String getResult() { return result; }\n\r");
      }
    } 

   public void generateWebServiceOp(PrintWriter out)
   { String nme = getName();
     String opcallpars = "";
     String opcalldec = "";
     for (int i = 0; i < parameters.size(); i++)
     { Attribute att = (Attribute) parameters.get(i);
       String par = att.getName();
       Type typ = att.getType();         
       String tname = typ.getJava();
       opcallpars = opcallpars + par;
       opcalldec = opcalldec + tname + " " + par; 
       if (i < parameters.size()-1)
       { opcallpars = opcallpars + ","; 
         opcalldec = opcalldec + ",";
       }
      }
      out.println("  @WebMethod( operationName = \"" + nme + "\" )\n\r");
      out.println("  public  String " + nme + "(" + opcalldec + ")\n\r");
      if (resultType != null)
      { out.println("  { return \"\" + cont." + nme + "(" + opcallpars + "); }\n\r\n\r"); } 
      else // (resultType == null)
      { out.println("  {  cont." + nme + "(" + opcallpars + "); }\n\r\n\r");
      }
    } 
  

  public String jspUpdateDeclarations()
  { // String bean = ename.toLowerCase();
    String beanclass = "beans.ControllerBean";
    return "<jsp:useBean id=\"bean\" scope=\"session\" \n " + 
           "class=\"" + beanclass + "\"/>";
  }

  public String jspParamTransfers(Vector atts)
  { String bean = "bean";
    String ucname = getName(); 
    String res = "";
    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i);
      String nme = att.getName();
      res = res +
        "<jsp:setProperty name=\"" + bean +
        "\"  property=\"" + nme + 
        "\"  param=\"" + nme + "\"/>\n\r";
    }
    return res;
  }

  
  public String jspUpdateText(String op,
                              Vector atts)
  { String bean = "bean";
    String dec = jspUpdateDeclarations();
    String sets = jspParamTransfers(atts);
    String showresult = ""; 
    if (resultType != null) 
    { // String op1 = (op.charAt(0) + "").toUpperCase() + op.substring(1,op.length()); 
      showresult = "<strong> Result = </strong> <%= bean.getResult() %>\n\r"; 
    } 
    String res = dec + "\n\r" + 
      sets + "\n\r" +
      "<html>\n\r" +
      "<head><title>" + op + "</title></head>\n\r" +
      "<body>\n\r" +
      "<h1>" + op + "</h1>\n\r" +
      "<% " + bean + "." + op + "(); %>\n\r" +
      "<h2>" + op + " performed</h2>\n\r" +
      showresult + "\n\r" +
      "<hr>\n\r\n\r" +
      "</body>\n\r</html>\n\r";
    return res;
  }

  /* public String jspQueryDeclarations(String ename)
  { String bean = ename.toLowerCase();
    String beanclass = "beans." + ename + "Bean";
    String res = "<%@ page import = \"java.util.*\" %>\n\r" +
      "<%@ page import = \"beans.*\" %>\n\r" +
      "<jsp:useBean id=\"" + bean +
           "\" scope=\"session\" \n\r " + 
           "class=\"" + beanclass + "\"/>";
      return res;
  } 

  public String jspQueryText(String op,
                             String ename, Vector atts, Entity ent)
  { String bean = ename.toLowerCase();
    String dec = jspQueryDeclarations(ename);
    String sets = jspParamTransfers(ename, atts);
    Entity ent2 = ent; 
    String action = getStereotype(0); 
    if (action.equals("get"))
    { String role = getStereotype(1); 
      Association ast = ent.getRole(role); 
      if (ast != null)
      { ent2 = ast.getEntity2(); }
    }
    String e2name = ent2.getName(); 
    String e2bean = e2name.toLowerCase(); 

    String res = dec + "\n\r" + sets + "\n\r" +
      "<html>\n\r" +
      "<head><title>" + op + " Results</title></head>\n\r" +
      "<body>\n\r" +
      "<h1>" + op + " Results</h1>\n\r" +
      "<% Iterator " + bean + "s = " + bean + "." + op +
      "(); %>\n\r" +
      "<table border=\"1\">\n\r" +
      ent2.getTableHeader() + "\n\r" +
      "<% while (" + bean + "s.hasNext())\n\r" +
      "{ " + e2name + "VO " + e2bean + "VO = (" + 
      e2name + "VO) " + bean + "s.next(); %>\n\r" +
      ent2.getTableRow() + "\n\r" +
      "<% } %>\n\r</table>\n\r\n\r<hr>\n\r\n\r" +
      "<%@ include file=\"commands.html\" %>\n\r" +
      "</body>\n\r</html>\n\r";
    return res;
  } */ 

  public String getJsp(String appname)
  { String action = getName();
    // String ename = entity.getName();
    Vector pars = getParameters();
    return jspUpdateText(action,pars); 
    // return jspQueryText(action,ename,pars,entity);
  }

  public String getInputPage(String appname)
  { String codebase = "http://127.0.0.1:8080/" + appname + "/servlets/";
    String op = getName();
    // String action = getStereotype(0);
    String jsp = codebase + op + ".jsp";
    String method = "GET";
    // if (action.equals("create") || action.equals("delete") ||
    //     action.equals("edit") || action.equals("add") || action.equals("set") ||
    //     action.equals("remove"))
    // { method = "POST"; }
    String res = "<html>\n\r" +
      "<head><title>" + op + " Form</title></head>\n\r" +
      "<body>\n\r" +
      "<h1>" + op + " Form</h1>\n\r" +
      "<form action = \"" + jsp + "\" method = \"" + method + "\" >\n\r";
    Vector pars = getParameters();
    for (int i = 0; i < pars.size(); i++)
    { Attribute att = (Attribute) pars.get(i);
      res = res + att.getFormInput() + "\n\r";
    }
    res = res + "<input type=\"submit\" value = \"" + 
          op + "\"/>\n\r</form>\n\r</body>\n\r</html>";
    return res;
  }

  public void changedEntityName(String oldN, String newN)
  { for (int i = 0; i < orderedPostconditions.size(); i++) 
    { ConstraintOrGroup cn = (ConstraintOrGroup) orderedPostconditions.get(i); 
      cn.changedEntityName(oldN,newN); 
    } 
    resetCode(); 
    System.err.println("Reset design of " + getName()); 
  } // and invariants, assumptions


  /* 
  public BOp buildBUpdateOp(Entity ent, String opname,
                            Type t, String resT, 
                            Vector entities, Vector types)
  { java.util.Map env0 = new java.util.HashMap();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    env0.put(ename,exbe);
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 

    Vector pars = getParameterNames();
    if (instanceScope) 
    { pars.add(0,ex); } 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    Expression presb; 
    if (pre == null)
    { presb = new BasicExpression("true"); } 
    else 
    { presb = pre.substituteEq("self", new BasicExpression(ex)); } 

    BExpression pre0 = getBParameterDec(); 
    BExpression pre1 = 
      BBinaryExpression.simplify("&",pre0,presb.binvariantForm(env0,false),true); 

    if ("true".equals(post + ""))
    { if (activity != null)
      { Vector localatts = (Vector) parameters.clone(); 
        activity.typeCheck(types,entities,context,localatts);
        // replace "self" by ex? 
        BStatement bactivity = activity.bupdateForm(env0,true); 
        return new BOp(opname,null,pars,pre1,bactivity);  
      } 
    } 

    // type check pre? 
    BExpression inst = new BBinaryExpression(":",exbe,esbe); 
    if (instanceScope)
    { pre1 = BBinaryExpression.simplify("&",inst,pre1,false); }
    Vector atts = (Vector) parameters.clone(); 
    post.typeCheck(types,entities,context,atts);
    if (stereotypes.contains("explicit"))
    { return explicitBUpdateOp(ent,opname,t,resT,entities,types,env0,exbe,esbe,pre1); } 

    Vector updates = post.updateVariables(); // take account of ent,
                                             // ie, like updateFeature
                                             // Assume just identifiers 4 now
    // check that all update vars are features of ent: 
    Vector preterms = post.allPreTerms(); 
    Vector entfeatures = ent.allDefinedFeatures(); // allFeatures?
    Vector pres = usedInPreForm(entfeatures,preterms); 
    updates = VectorUtil.union(updates,pres); 
    if (updates.size() == 0)
    { System.err.println("ERROR: Cannot determine write frame of " + this); 
      updates = post.allFeatureUses(entfeatures);
      System.err.println("ERROR: Assuming write frame is: " + updates); 
    } 

    BExpression dec = null;
    BParallelStatement body = new BParallelStatement();
    Vector vars = new Vector(); 

    Expression newpost = (Expression) post.clone();

    for (int i = 0; i < updates.size(); i++)
    { BasicExpression var = (BasicExpression) updates.get(i);
      if (entfeatures.contains(var.data)) 
      { BExpression oldbe = new BBasicExpression(var.data); 
        Type typ = var.getType();
        String new_var = "new_" + var.data;
        if (vars.contains(new_var)) { continue; } 
        vars.add(new_var); 
        Attribute att = new Attribute(new_var,typ,ModelElement.INTERNAL);
        att.setEntity(ent); // ?

        atts.add(att); 
        String btyp = typ.generateB(var);
        // If a set type, should be FIN(elementType.generateB())

        BExpression varbe = new BBasicExpression(new_var);
        BExpression typebe =
          new BBinaryExpression("-->",esbe,new BBasicExpression(btyp));
      
        BExpression new_dec = new BBinaryExpression(":",varbe,typebe);
        Expression newbe = new BasicExpression(new_var); 
        newbe.entity = var.entity; 
        newbe.type = var.type; 
        newbe.elementType = var.elementType; 
        newbe.umlkind = var.umlkind; 
        newbe.multiplicity = var.multiplicity; 
        dec = BBinaryExpression.simplify("&",dec,new_dec,true); 
        newpost = newpost.substituteEq(var.data,newbe);
        // should only substitute for var itself, not var@pre
        // and assign var := new_var;
        BAssignStatement stat = new BAssignStatement(oldbe,varbe); 
        body.addStatement(stat); 
      }
      else 
      { System.err.println("ERROR: " + var.data + " is not a feature of " + ent); }
    }

    BExpression pred = newpost.binvariantForm(env0,false);      
    pred = BBinaryExpression.simplify("&",dec,pred,false);
    BStatement code; 
    if (vars.size() > 0)
    { code = new BAnyStatement(vars,pred,body); }
    else
    { code = body; }  // skip
    return new BOp(opname,null,pars,pre1,code);  
  }
    */ 

  public String cg(CGSpec cgs, Vector types, Vector entities)
  { // _1(_2) : 3 { precondition: _4; activity: _5; } 
  
    String etext = this + "";
    Vector args = new Vector();
    args.add(getName());
    Vector eargs = new Vector();
    eargs.add(this);
    String pars = "";
	
    if (parameters == null) {} 
    else if (parameters.size() == 0) {} 
    else 
    { Attribute p = (Attribute) parameters.get(0);
      Vector partail = new Vector(); 
      partail.addAll(parameters); 
      partail.remove(0); 
      pars = p.cgParameter(cgs,partail);
    }
    args.add(pars);
    eargs.add(parameters); 



 /*   if (parameters == null) {} 
    else if (parameters.size() == 0) {} 
    else 
    { Attribute p = (Attribute) parameters.get(0);
      Vector partail = new Vector(); 
      partail.addAll(parameters); 
      partail.remove(0); 
      pars = p.cgParameter(cgs,partail);
    }
    args.add(pars); */ 

    if (resultType != null) 
    { args.add(resultType.cg(cgs)); 
      eargs.add(resultType); 
    } 
    else 
    { Type rt = new Type("void",null); 
      args.add(rt.cg(cgs)); 
      eargs.add(rt); 
    } 

   /* 
    if (pre != null) 
    { args.add(pre.cg(cgs)); } 
    else 
    { BasicExpression pr = new BasicExpression(true); 
      args.add(pr.cg(cgs)); 
    } 

    if (post != null) 
    { args.add(post.cg(cgs)); } 
    else 
    { BasicExpression pst = new BasicExpression(true); 
      args.add(pst.cg(cgs)); 
    } */ 

    if (classifierBehaviour != null)
    { Statement impl = implementBehaviour(types,entities); 
      args.add(impl.cg(cgs));
      eargs.add(impl);  
    }
    else 
    { Statement nullstat = new SequenceStatement(); 
      args.add(nullstat.cg(cgs));
      eargs.add(nullstat);  
    } 
    // only one Use Case rule?
    // maybe for static/cached

    CGRule r = cgs.matchedUsecaseRule(this,etext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext;
  }

  public String cgActivity(CGSpec cgs, Vector types, Vector entities)
  { java.util.Map env = new java.util.HashMap(); 
  
    System.out.println(">>> " + getName() + " classifierBehaviour= " + classifierBehaviour + 
	                   " activity= " + activity); 

    if (classifierBehaviour != null && !Statement.isEmpty(classifierBehaviour))
    { // Statement impl = implementBehaviour(types,entities);
      Statement stat = classifierBehaviour.generateDesign(env,false);
      System.out.println(">>> Use case implementation is: " + stat);  
      if (stat != null) 
      { return stat.cg(cgs); }  
    }
	else if (activity != null) 
	{ Statement stat1 = activity.generateDesign(env,false);
      System.out.println(">>> Use case activity implementation is: " + stat1);  
      if (stat1 != null) 
      { return stat1.cg(cgs); }  
    }
    Statement nullstat = new SequenceStatement(); 
    return "";    
  }

  public String getAndroidValueObject() 
  { return getAndroidValueObject("com.example.app"); } 

 
  public String getAndroidValueObject(String pge)
  { String res = "package " + pge + ";\n\n";
    String nme = getName();  
    res = res + "public class " + nme + "VO\n" + 
          "{ \n"; 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getJava8(); 
      if (tname.equals("boolean"))
      { tname = "String"; } 
      res = res + " private " + tname + " " + attnme + ";\n"; 
    }
 
    res = res + "\n" +
          "  public " + nme + "VO() {}\n\n"; 
		  
    if (parameters.size() > 0)
    { res = res + "  public " + nme + "VO(";
      boolean previous = false;

      for (int i = 0; i < parameters.size(); i++)
      { Attribute att = (Attribute) parameters.get(i);
        String tname = att.getType().getJava8(); 
        if (tname.equals("boolean"))
        { tname = "String"; } 

        String par = tname + " " + att.getName() + "x";
        if (previous)
        { res = res + "," + par; }
        else        
        { res = res + par;
          previous = true;
        }
      }
      res = res + ")\n  { "; 

      for (int i = 0; i < parameters.size(); i++) 
      { Attribute att = (Attribute) parameters.get(i); 
        String attnme = att.getName(); 
        res = res + "   " + attnme + " = " + attnme + "x;\n"; 
      }
      res = res + "  }\n\n"; 
    } 
	
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getJava8(); 
      if (tname.equals("boolean"))
      { tname = "String"; } 

      res = res + "  public " + tname + " get" + attnme + "()\n  { " + 
            "return " + attnme + "; }\n\n"; 
    } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getJava8(); 
      if (tname.equals("boolean"))
      { tname = "String"; } 

      res = res + "  public void set" + attnme + "(" + tname + " _x)\n  { " + 
            attnme + " = _x; }\n\n"; 
    } 

    return res + "}\n\n"; 
  } 

  public String generateAndroidBean(String packageName, 
       Vector entities, Vector types, CGSpec cgs)
  { String ename = getName(); 
    String res = "package " + packageName + ";\n\n" + 
      "import java.util.ArrayList;\n\n" + 
      "import java.util.List;\n\n" + 
      "import android.content.Context;\n\n" + 
      "public class " + ename + "Bean\n{ ModelFacade model = null;\n\n";
	  
    Attribute resultAttribute = getResultParameter(); 

    Vector attributes = new Vector(); 
    attributes.addAll(parameters); 
 
    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      String attnme = att.getName();
      Type atttype = att.getType();  
      String tname = atttype.getName(); 
      res = res + "  private String " + attnme + " = \"\";\n";
      if (tname.equals("int") || tname.equals("long"))
      { res = res + "  private int i" + attnme + " = 0;\n"; } 
      else if (tname.equals("double"))
      { res = res + "  private double d" + attnme + " = 0;\n"; } 
      else if (att.isEnumeration())
      { Vector vals = atttype.getValues(); 
	   res = res + "  private " + tname + " e" + attnme + " = " + tname + "." + vals.get(0) + ";\n"; 
      }  
      else if (att.isEntity())
      { res = res + "  private " + tname + " instance_" + attnme + " = null;\n"; 
      }  
      else if (att.isCollection()) // assume a collection of strings
      { res = res + "  private ArrayList<String> s" + attnme + " = new ArrayList<String>();\n"; } 

       // booleans are treated as strings. 
    } 
    res = res + "  private List errors = new ArrayList();\n\n" +
          "  public " + ename + "Bean(Context _c) { model = ModelFacade.getInstance(_c); }\n\n"; 
    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      String attnme = att.getName(); 
      res = res + "  public void set" + attnme + "(String " + attnme + "x)\n  { " + 
            attnme + " = " + attnme + "x; }\n\n"; 
    } 

    res = res + "  public void resetData()\n  { "; 
    for (int i = 0; i < attributes.size(); i++) 
    { Attribute att = (Attribute) attributes.get(i); 
      String attname = att.getName(); 
      res = res + attname + " = \"\";\n    "; 
    } 
    res = res + "}\n\n";     

    res = res + "  public boolean is" + ename + "error()\n" + 
            "  { errors.clear(); \n";

    String parstring = "";  
    for (int k = 0; k < parameters.size(); k++) 
    { Attribute att = (Attribute) parameters.get(k);
      String attnme = att.getName();  
      Type atype = att.getType(); 
      String tname = atype.getName(); 
      String check = att.getBeanCheckCode(); 
      res = res + check; 

      if (tname.equals("int") || tname.equals("long"))
      { parstring = parstring + "i" + attnme; } 
      else if (tname.equals("double"))
      { parstring = parstring + "d" + attnme; }
      else if (att.isEnumerated())
      { parstring = parstring + "e" + attnme; } 
      else if (att.isEntity())
      { parstring = parstring + "instance_" + attnme; } 
      else if (att.isCollection())
      { parstring = parstring + "s" + attnme; } 
      else 
      { parstring = parstring + attnme; }  
      if (k < parameters.size() - 1) 
      { parstring = parstring + ","; } 
    } 

    Vector tests = getPreconditionCheckTests(cgs,parameters); 
    for (int p = 0; p < tests.size(); p++)
    { String test = (String) tests.get(p); 
      res = res + 
            "    if (" + test + ") { }\n" + 
            "    else { errors.add(\"Precondition: " + test + " failed\"); }\n";
    }
    res = res + "    return errors.size() > 0;\n  }\n\n";

    res = res + "  public String errors() { return errors.toString(); }\n\n"; 

    if (resultAttribute == null)
    { res = res + "  public void " + ename + "()\n" +  "  { "; 
      res = res + "model." + ename + "(" + parstring + ");" + " }\n\n";
    } 
	else 
	{ Type t = resultAttribute.getType(); 
	  String jType = t.getJava7(); 
	  res = res + "  public " + jType + " " + ename + "()\n" +  "  { "; 
      res = res + "return model." + ename + "(" + parstring + ");" + " }\n\n";
    }  
    return res + "}\n"; 
  }

  public String generateIOSValidationBean(String packageName, 
       CGSpec cgs, Vector entities, Vector types)
  { String ename = getName(); 
    String res = 
	  // "package " + packageName + ";\n\n" + 
      "import Foundation\n" + 
      "import Glibc\n\n" + 
      "class " + ename + "ValidationBean\n{ \n";
	  
    Attribute resultAttribute = getResultParameter(); 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 
 
    String parlist = ""; 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String attnme = att.getName();
      Type atttype = att.getType();  
      parlist = parlist + attnme + " : " + atttype.getSwift(); 
      if (i < atts.size() - 1) 
      { parlist = parlist + ", "; } 
    } 

    res = res + "  var errorlist : [String] = [String]()\n\n"; 

    res = res + "  init()\n  { }\n\n"; 
          
    res = res + "  func resetData()\n  { errorlist = [String]() }\n\n"; 

    res = res + "  func is" + ename + "error(" + parlist + ") -> Bool\n" + 
            "  { resetData() \n";

    String parstring = "";  
    for (int k = 0; k < parameters.size(); k++) 
    { Attribute att = (Attribute) parameters.get(k);
      String attnme = att.getName();  
      Type atype = att.getType(); 
      String tname = atype.getName(); 
      String check = att.getIOSCheckCode(); 
      res = res + check; 
	}

    Vector tests = getIOSPreconditionCheckTests(cgs,parameters); 
    for (int p = 0; p < tests.size(); p++)
    { String test = (String) tests.get(p); 
      res = res + 
            "    if " + test + " { }\n" + 
            "    else { errorlist.append(\"Precondition " + (p+1) + " failed\") }\n";
    }
    res = res + "    return errorlist.count > 0\n  }\n\n";

    res = res + "  func errors() -> String\n" + 
                "  { var res : [String] = [String]()\n" + 
                "    for (_,x) in errorlist.enumerated()\n" + 
                "    { res = res + x + \" \"; } \n" + 
                "    return res\n" + 
                "  }\n\n"; 

    return res + "}\n"; 
  }

  public void androidTabItem(PrintWriter out)
  { String fullop = getName();
    String titleop = Named.capitalise(fullop);
    out.println("  <item android:id=\"@+id/" + fullop + "\"");
    out.println("    android:title=\"" + titleop + "\"");
    out.println("    android:showAsAction=\"always\" />"); 
  }

  public Vector getPreconditionCheckTests(CGSpec cgs, Vector params)
  { // only include invariants which have all features in params
    Vector parnames = ModelElement.getNames(params); 
    Vector res = new Vector(); 
    if (preconditions.size() == 0) { return res; } 
    Vector newinvs = new Vector();
    Vector oldinvs = new Vector(); 
 
    for (int i = 0; i < preconditions.size(); i++)
    { Constraint c = (Constraint) preconditions.get(i); 
      Vector cfeats = c.allFeaturesUsedIn(); 
      if (parnames.containsAll(cfeats))
      { oldinvs.add((Constraint) c.clone()); } 
    }
     
    java.util.Map env = new java.util.HashMap(); 
    if (classifier != null) 
    { env.put(classifier.getName(),"this"); } 

    for (int i = 0; i < params.size(); i++)
    { Attribute att = (Attribute) params.get(i);
      String attname = att.getName();  
      Type t = att.getType(); 
      String tname = t.getName();
      Expression newE; 
      if (tname.equals("int") || tname.equals("long"))
      { newE = new BasicExpression("i" + attname);
        newE.setUmlKind(Expression.VARIABLE); 
        newE.setType(t); 
        newinvs = Constraint.substituteEqAll(attname,newE,oldinvs); 
        oldinvs = (Vector) ((Vector) newinvs).clone(); 
      }
      else if (tname.equals("double"))
      { newE = new BasicExpression("d" + attname);
        newE.setUmlKind(Expression.VARIABLE); 
        newE.setType(t);
        newinvs = Constraint.substituteEqAll(attname,newE,oldinvs); 
        oldinvs = (Vector) ((Vector) newinvs).clone(); 
      }    
      else if (t.isEnumerated())
      { newE = new BasicExpression("e" + attname);
        newE.setUmlKind(Expression.VARIABLE); 
        newE.setType(t); 
        newinvs = Constraint.substituteEqAll(attname,newE,oldinvs); 
        oldinvs = (Vector) ((Vector) newinvs).clone(); 
      } 
    }
    
    for (int j = 0; j < oldinvs.size(); j++) 
    { Constraint con = (Constraint) oldinvs.get(j); 
      String contest = // con.cg(cgs); 
                       con.queryForm(env,true); 
      res.add(contest); 
    } 
    return res; 
  } 

  public Vector getIOSPreconditionCheckTests(CGSpec cgs, Vector params)
  { // only include invariants which have all features in params
    Vector parnames = ModelElement.getNames(params); 
    Vector res = new Vector(); 
    if (preconditions.size() == 0) { return res; } 
    Vector newinvs = new Vector();
    Vector oldinvs = new Vector(); 
 
    for (int i = 0; i < preconditions.size(); i++)
    { Constraint c = (Constraint) preconditions.get(i); 
      Vector cfeats = c.allFeaturesUsedIn(); 
      if (parnames.containsAll(cfeats))
      { oldinvs.add((Constraint) c.clone()); } 
    }
     
    java.util.Map env = new java.util.HashMap(); 
    if (classifier != null) 
    { env.put(classifier.getName(),"this"); } 
    
    for (int j = 0; j < oldinvs.size(); j++) 
    { Constraint con = (Constraint) oldinvs.get(j); 
      String contest = con.cg(cgs); 
      res.add(contest); 
    } 
    return res; 
  } // type check them again

  public static boolean hasLargeEnumerationParameter(Vector pars) 
  { for (int i = 0; i < pars.size(); i++)
    { Attribute att = (Attribute) pars.get(i);
      if (att.isLargeEnumeration() || att.isEntity())
      { return true; } 
    } 
    return false; 
  } 

  public static String spinnerListenerOperations(String op, Vector pars) 
  { String selectcode = "";
    String deselectcode = ""; 
 
    for (int i = 0; i < pars.size(); i++)
    { Attribute att = (Attribute) pars.get(i);
      String attnme = att.getName(); 

      if (att.isLargeEnumeration())
      { selectcode = selectcode + 
           "    if (_parent == " + op + attnme + "Spinner)\n\r" +
           "    { " + op + attnme + "Data = " + op + attnme + "ListItems[_position]; }\n\r"; 
        deselectcode = deselectcode + 
           "    " + op + attnme + "Data = " + op + attnme + "ListItems[0];\n\r"; 
      } 
      else if (att.isEntity())
      { selectcode = selectcode + 
           "    if (_parent == " + op + attnme + "Spinner)\n\r" +
           "    { " + op + attnme + "Data = " + op + attnme + "ListItems.get(_position); }\n\r"; 
        deselectcode = deselectcode + 
           "    " + op + attnme + "Data = \"\";\n\r"; 
      } 
    } 
 
    if ("".equals(selectcode)) { return ""; }

    return "  public void onItemSelected(AdapterView<?> _parent, View _v, int _position, long _id)\n\r" +   
           "  { " + selectcode + " }\n\r\n\r" + 
        
           "  public void onNothingSelected(AdapterView<?> _parent)\n\r" + 
           "  { " + deselectcode + " }\n\r"; 
  } 

  public static Vector spinnerListenerOps(String op, Vector pars, Vector extensions) 
  { String selectcode = "";
    String deselectcode = ""; 
 
    for (int i = 0; i < pars.size(); i++)
    { Attribute att = (Attribute) pars.get(i);
      String attnme = att.getName(); 

      if (att.isLargeEnumeration())
      { selectcode = selectcode + 
           "    if (_parent == " + op + attnme + "Spinner)\n\r" +
           "    { " + op + attnme + "Data = " + op + attnme + "ListItems[_position]; }\n\r"; 
        deselectcode = deselectcode + 
           "    " + op + attnme + "Data = " + op + attnme + "ListItems[0];\n\r"; 
      } 
      else if (att.isEntity())
      { selectcode = selectcode + 
           "    if (_parent == " + op + attnme + "Spinner)\n\r" +
           "    { " + op + attnme + "Data = " + op + attnme + "ListItems.get(_position); }\n\r"; 
        deselectcode = deselectcode + 
           "    " + op + attnme + "Data = \"\";\n\r"; 
      } 
    } 


    for (int j = 0; j < extensions.size(); j++) 
    { UseCase ext = (UseCase) extensions.get(j);
      String extop = ext.getName();  
      Vector extpars = ext.getParameters(); 

      for (int k = 0; k < extpars.size(); k++)
      { Attribute extatt = (Attribute) extpars.get(k);
        String attnme = extatt.getName(); 

        if (extatt.isLargeEnumeration())
        { selectcode = selectcode + 
            "    if (_parent == " + extop + attnme + "Spinner)\n\r" +
            "    { " + extop + attnme + "Data = " + extop + attnme + "ListItems[_position]; }\n\r"; 
          deselectcode = deselectcode + 
            "    " + extop + attnme + "Data = " + extop + attnme + "ListItems[0];\n\r"; 
        } 
        else if (extatt.isEntity())
        { selectcode = selectcode + 
             "    if (_parent == " + extop + attnme + "Spinner)\n\r" +
             "    { " + extop + attnme + "Data = " + extop + attnme + "ListItems.get(_position); }\n\r"; 
          deselectcode = deselectcode + 
             "    " + extop + attnme + "Data = \"\";\n\r"; 
        } 
      }
    }  

    Vector res = new Vector(); 
 
    if ("".equals(selectcode)) 
    { return res; }

    res.add("  public void onItemSelected(AdapterView<?> _parent, View _v, int _position, long _id)\n\r" +   
           "  { " + selectcode + " }\n\r");  
        
    res.add("  public void onNothingSelected(AdapterView<?> _parent)\n\r" + 
           "  { " + deselectcode + " }\n\r");
    return res;  
  } 

  public String getSwiftUIValueObject(String pge, Vector types, Vector entities, Vector useCases, CGSpec cgs)
  { String res = "import Foundation\n";
    res = res + "import Glibc\n\n"; 
	
    String ename = getName();
    String ucname = getName(); 
  
    res = res + "class " + ename + "VO\n" + 
          "{ \n"; 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getSwift(); 
      String dflt = att.getType().getSwiftDefaultValue();
      if (att.isEntity())
      { tname = "String"; 
        dflt = "\"\""; 
      } 
      res = res + "  var " + attnme + " : " + tname + " = " + dflt + "\n"; 
    } // But entity instances are represented by their key values, a string. 

    res = res + "  static var defaultInstance : " + ename + "VO? = nil\n"; 
    res = res + "  var errorlist : [String] = [String]()\n\n"; 
    if (resultType != null && "WebDisplay".equals(resultType.getName()))
	{ res = res + "  var result : WebDisplay = WebDisplay()\n\n"; }
	else if (resultType != null && "ImageDisplay".equals(resultType.getName()))
	{ res = res + "  var result : ImageDisplay = ImageDisplay()\n\n"; }
	else if (resultType != null && "GraphDisplay".equals(resultType.getName()))
	{ res = res + "  var result : GraphDisplay = GraphDisplay()\n\n"; }
	else if (resultType != null)
    { res = res + "  var result : " + resultType.getSwift() + " = " + resultType.getSwiftDefaultValue() + "\n\n"; }

    res = res + "\n" +
          "  init() {}\n\n"; 

    res = res + "\n" +
          "  static func default" + ename + "VO() -> " + ename + "VO\n" + 
          "  { if defaultInstance == nil \n" + 
          "    { defaultInstance = " + ename + "VO() }\n" + 
          "    return defaultInstance!\n" + 
          "  }\n\n"; 

    String stringtext = "\"\""; 
		  
    if (parameters.size() > 0)
    { res = res + "  init(";
		  
      boolean previous = false;
	
      for (int i = 0; i < parameters.size(); i++)
      { Attribute att = (Attribute) parameters.get(i);
        String tname = att.getType().getSwift();
        if (att.isEntity())
        { tname = "String"; }
		
        String attname = att.getName(); 
 
        String label = "\"" + attname + "= \" + "; 
        if (att.isNumeric())
        { label = label + "String(" + attname + ")"; }
		else if (att.isEnumerated())
        { label = label + attname + ".rawValue"; }
        else 
        { label = label + attname; }
		 
      
        String par = attname + "x" + " : " + tname;
        if (previous)
        { res = res + "," + par; 
          stringtext = stringtext + " + \", \" + " + label; 
        }
        else        
        { res = res + par;
          stringtext = stringtext + " + " + label; 
          previous = true;
        }
      }
      res = res + ")  {\n"; 
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute att = (Attribute) parameters.get(i); 
        String attnme = att.getName(); 
        res = res + "    " + attnme + " = " + attnme + "x\n"; 
      }
      res = res + "  }\n\n"; 
    } 
	
	 
    res = res + "  func toString() -> String\n" + 
                "  { return " + stringtext + " }\n\n"; 
 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getSwift(); 
	  
      res = res + "  func get" + attnme + "() -> " + tname + "\n";  
      if (att.isEntity())
	  { Type atype = att.getType(); 
	    Entity enttype = atype.getEntity(); 
	    String refname = enttype.getName(); 
	    res = res + "  { return " + refname + "." + refname + "_index[" + attnme + "]! }\n\n";
	  } 
	  else 
	  { res = res + "  { return " + attnme + " }\n\n"; } 
    } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      String tname = att.getType().getSwift(); 

      res = res + "  func set" + attnme + "(_x : " + tname + ")\n"; 
      if (att.isEntity())
      { Type atype = att.getType(); 
        Entity enttype = atype.getEntity(); 
        Attribute primkey = enttype.getPrincipalPrimaryKey(); 
        res = res + "  { " + attnme + " = _x." + primkey.getName() + " }\n\n";
	  } 
	  else 
	  { res = res + "  { " + attnme + " = _x }\n\n"; } 
     }

	if (resultType != null)
	{ res = res + "  func setresult(_x : " + resultType.getSwift() + ")\n" + 
	              "  { result = _x }\n\n"; 
	}
	
	/* For SwiftUI this also holds the validation operation: */ 
	 
          
    res = res + "  func resetData()\n  { errorlist = [String]() }\n\n"; 

    Vector contexts = new Vector(); 
    contexts.add(this); 
    // typeCheckInvariants(types,entities); 

    res = res + "  func is" + ucname + "error() -> Bool\n" + 
                "  { resetData() \n";
    

    Vector tests = getIOSPreconditionCheckTests(cgs,parameters); 
    for (int p = 0; p < tests.size(); p++)
    { String test = (String) tests.get(p); 
      res = res + 
            "    if " + test + " { }\n" + 
            "    else { errorlist.append(\"" + ename + " invariant " + (p+1) + " failed\") }\n";
    }
    res = res + "    if errorlist.count > 0\n" + 
                "    { return true }\n" + 
                "    return false\n" + 
                "  }\n\n"; 


    res = res + "  func errors() -> String\n" + 
           "  { var res : String = \"\"\n" +
           "    for (_,x) in errorlist.enumerated()\n" + 
           "    { res = res + x + \", \" }\n" +  
           "    return res\n" + 
           "  }\n\n"; 
				

    return res + "}\n\n"; 
  } 


  public Vector testCases()
  { Vector allattributes = getParameters();
    String nme = getName();  
    Vector res = new Vector();
 
    if (allattributes == null || allattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }
    res.add("-- test for operation " + nme + "\n"); 
	
    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector bounds = new Vector(); 
    java.util.Map aBounds = new java.util.HashMap(); 
      
    for (int i = 0; i < preconditions.size(); i++) 
    { Constraint con = (Constraint) preconditions.get(i);
      Expression pre = con.succedent();  
      pre.getParameterBounds(allattributes,bounds,aBounds);
   
      Expression.identifyUpperBounds(allattributes,aBounds,upperBounds); 
      Expression.identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);
    }

    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 

      Vector testassignments = att.testCases("parameters", lowerBounds, upperBounds);
 
      for (int j = 0; j < res.size(); j++) 
      { String tst = (String) res.get(j); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            newres.add(newtst); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 
    return res; 
 }
 
   
}
