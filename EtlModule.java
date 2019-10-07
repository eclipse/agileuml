import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package ETL */ 

public class EtlModule
{ String name;
  Vector pre = new Vector();  // of NamedBlock
  Vector post = new Vector(); 
  Vector transformationRules = new Vector();  // of TransformationRule
  Vector operations = new Vector();  // of BehaviouralFeature
  Vector attributes = new Vector();  // of Attribute

  public EtlModule(String nme)
  { name = nme; }

  public int nops()
  { return operations.size(); } 

  public int nrules()
  { return transformationRules.size(); } 

  public void addPre(NamedBlock p)
  { pre.add(p); }

  public void addPost(NamedBlock p)
  { post.add(p); }

  public void addRule(TransformationRule r)
  { // if (r.isPrimary())
    // { transformationRules.add(0,r); }
    // else 
    { transformationRules.add(r); }
  }

  public void addOperation(BehaviouralFeature bf)
  { operations.add(bf); } 

  public void addAttribute(Attribute att)
  { attributes.add(att); } 

  public void add(Object ob)
  { if (ob instanceof TransformationRule)
    { addRule((TransformationRule) ob); } 
    else if (ob instanceof NamedBlock)
    { NamedBlock nb = (NamedBlock) ob; 
      if (nb.isPre())
      { addPre(nb); } 
      else 
      { addPost(nb); } 
    } 
    else if (ob instanceof BehaviouralFeature)
    { addOperation((BehaviouralFeature) ob); } 
  } 


  public String toString()
  { String res = "module " + name + "\n"; 
    for (int i = 0; i < pre.size(); i++)
    { res = res + pre.get(i); }

    for (int j = 0; j < operations.size(); j++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(j); 
      Entity context = bf.getEntity(); 
      res = res + "operation " + context + " " + bf.getSignature() + "\n" +  
            "{ " + bf.getActivity() + " }\n"; 
    } 
 
    for (int i = 0; i < transformationRules.size(); i++)
    { res = res + transformationRules.get(i); }

    for (int i = 0; i < post.size(); i++)
    { res = res + post.get(i); }

    return res + "\n";
  }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env) 
  { for (int i = 0; i < pre.size(); i++)
    { NamedBlock pp = (NamedBlock) pre.get(i);
      pp.typeCheck(types,entities,contexts,env); 
    } // global variables can be added to env here. 

    attributes.addAll(env); 

    for (int j = 0; j < operations.size(); j++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(j); 
      // Entity context = bf.getEntity(); 
      bf.typeCheck(types,entities); 
    } 
 
    for (int i = 0; i < transformationRules.size(); i++)
    { TransformationRule tr = (TransformationRule) transformationRules.get(i);
      tr.typeCheck(types,entities,contexts,env); 
    }

    for (int i = 0; i < post.size(); i++)
    { NamedBlock pst = (NamedBlock) post.get(i); 
      pst.typeCheck(types,entities,contexts,env); 
    }

    return true; 
  } 

  public Vector rulesOnInput(Entity e)
  { Vector res = new Vector();
    for (int i = 0; i < transformationRules.size(); i++)
    { TransformationRule r = (TransformationRule) transformationRules.get(i);
      Attribute att = r.getSource();
      Type atype = att.getType(); 
      if (atype != null && atype.isEntity())
      { Entity src = atype.getEntity(); 
        if (e == src || Entity.isAncestor(e,src) ||
            Entity.isAncestor(src,e)) 
        { res.add(r); }
      } 
    }
    return res;
  }

  public Vector rulesMapping(Entity src, Entity trg)
  { // Rules that can map input x : src to a y : trg
    Vector pres = new Vector();
    Vector res = new Vector();
    Vector srcrules = rulesOnInput(src);
    for (int i = 0; i < srcrules.size(); i++)
    { TransformationRule r = (TransformationRule) srcrules.get(i);
      if (r.hasOutputType(trg))
      { if (r.isPrimary())
        { pres.add(r); }
        else 
        { res.add(r); }
      }
    } // add primary ones first
    pres.addAll(res);
    return pres;
  }

  public UseCase toUseCase(Vector entities, Vector types)
  { Entity trent = (Entity) ModelElement.lookupByName("$Trace", entities);
    Entity outent = (Entity) ModelElement.lookupByName("$OUT", entities);
    Entity inent = (Entity) ModelElement.lookupByName("$IN", entities); 

    if (trent == null) 
    { return null; } 

    if (outent == null) 
    { return null; }
 
    if (inent == null) 
    { return null; } 

    Type trentt = new Type(trent);
    Type outentt = new Type(outent);
    Type intype = new Type(inent); 


    UseCase uc = new UseCase(name,null); 
    for (int i = 0; i < pre.size(); i++) 
    { NamedBlock nb = (NamedBlock) pre.get(i); 
      Constraint cons = nb.toConstraint(entities, types,uc); 
      uc.addPostcondition(cons); 
    } 

    // the operations belong to entities, not to the use case. 

    for (int j = 0; j < attributes.size(); j++) 
    { Attribute att = (Attribute) attributes.get(j); 
      uc.addAttribute(att); 
    } 

    for (int i = 0; i < transformationRules.size(); i++)
    { TransformationRule tr = (TransformationRule) transformationRules.get(i);
      if (tr.isLazy()) 
      { tr.toOperation(entities, types); } 
      else if (tr.isAbstract()) { } 
      else  
      { tr.toOperation(entities, types); 
        Constraint con = tr.toConstraint(entities, types);
        uc.addPostcondition(con); 
      }
    } 

    defineEquivalentOp(inent, intype, trent, outentt); 

    for (int i = 0; i < post.size(); i++)
    { NamedBlock pb = (NamedBlock) post.get(i);
      Constraint cons = pb.toConstraint(entities, types,uc); 
      uc.addPostcondition(cons); 
    }
    return uc; 
  } 

  private void defineEquivalentOp(Entity inent, Type intype, Entity trent, Type outtype)
  { // adds operation for equivalent/equivalents to $IN
    BehaviouralFeature bf = new BehaviouralFeature("equivalent");
    Association tr = inent.getRole("$trace"); 

    if (tr == null) { return; } 

    bf.setType(outtype); 
    // bf.addParameter(inp); 
    inent.addOperation(bf); 
    bf.setEntity(inent);

    Expression selfvar = new BasicExpression(inent, "self"); 

    BasicExpression trexp = new BasicExpression(tr); 
    UnaryExpression trne = new UnaryExpression("->notEmpty", trexp); 
    Association targ = trent.getRole("target"); 
    if (targ == null) 
    { return; } 

    BasicExpression targexp = new BasicExpression(targ); 
    targexp.setObjectRef(trexp);  // $trace.target
    UnaryExpression anytarget = new UnaryExpression("->any", targexp);     
    
    BasicExpression nulle = new BasicExpression("null"); 
    nulle.setType(outtype); 
    nulle.setElementType(outtype); 

    BasicExpression res = new BasicExpression(outtype, "result");
    ConditionalExpression ce = new ConditionalExpression(trne, anytarget, nulle);   

    // loop through all the concrete rules, calling their operations as alternative cases

    ConditionalExpression ce0 = ce; 

    for (int i = 0; i < transformationRules.size(); i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      if (rr.isAbstract()) { } 
      else 
      { Expression appcond = rr.applicationCondition(selfvar); 
        Expression invokerr = rr.invocation(selfvar); 
        ConditionalExpression cerr = new ConditionalExpression(appcond, invokerr, nulle); 
        ce.setElse(cerr); 
        ce = cerr; 
      } 
    } 
    
    BinaryExpression reseq = new BinaryExpression("=", res, ce0); 
    bf.setPost(reseq); 
  } 

  public int syntacticComplexity()
  { int res = 0; 

    for (int j = 0; j < pre.size(); j++) 
    { NamedBlock pp = (NamedBlock) pre.get(j); 
      res = res + pp.syntacticComplexity(); 
    } 

    for (int p = 0; p < operations.size(); p++) 
    { BehaviouralFeature op = (BehaviouralFeature) operations.get(p); 
      int opc = op.syntacticComplexity();
      System.out.println("*** Syntactic complexity of operation " + op.getName() + " is " + opc); 
      res += opc;  
    } 

    for (int i = 0; i < transformationRules.size(); i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      res = res + rr.syntacticComplexity(); 
    } 

    for (int k = 0; k < post.size(); k++) 
    { NamedBlock pp = (NamedBlock) post.get(k); 
      res = res + pp.syntacticComplexity(); 
    } 

    return res; 
  }   

  public int cyclomaticComplexity()
  { int res = 0; 

    for (int j = 0; j < pre.size(); j++) 
    { NamedBlock pp = (NamedBlock) pre.get(j); 
      if (pp.cyclomaticComplexity() > 10)
      { res++; }  
    } 

    for (int p = 0; p < operations.size(); p++) 
    { BehaviouralFeature op = (BehaviouralFeature) operations.get(p); 
      int opc = op.cyclomaticComplexity();
      System.out.println("*** Cyclomatic complexity of operation " + op.getName() + " is " + opc); 
      if (opc > 10) { res++; }   
    } 

    for (int i = 0; i < transformationRules.size(); i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      int rc = rr.cyclomaticComplexity();
      if (rc > 10) { res++; }  
    } 

    for (int k = 0; k < post.size(); k++) 
    { NamedBlock pp = (NamedBlock) post.get(k); 
      if (pp.cyclomaticComplexity() > 10) { res++; }  
    } 

    return res; 
  }   

  public Map getCallGraph()
  { Map res = new Map(); 
    int efo = 0; 

    // operations, pre and post also
    for (int i = 0; i < operations.size(); i++) 
    { BehaviouralFeature rr = (BehaviouralFeature) operations.get(i); 
      Map resrr = new Map(); 
      rr.getCallGraph(resrr); 

      Vector addedmaps = new Vector(); 
      Vector removedmaps = new Vector(); 
      Vector equivs = rr.equivalentsUsedIn();
      if (equivs.size() > 0) 
      { // System.out.println("Equivalent expressions used in " + rr.name + " are: " + equivs);
        for (int j = 0; j < equivs.size(); j++) 
        { Expression ex = (Expression) equivs.get(j); 
          Type extype = ex.getType(); 
          if (extype != null)
          { Entity exent = extype.getEntity();
            if (exent == null && ex.getElementType() != null) 
            { exent = ex.getElementType().getEntity(); } 
 
            Vector callsrules = rulesOnInput(exent); 
            // System.out.println(ex + ".equivalent() can call rules " + callsrules); 
 
            for (int m = 0; m < resrr.size(); m++)
            { Maplet mm = (Maplet) resrr.get(m); 
              // System.out.println(">>> " + mm.getTarget()); 

              if (mm.getTarget() != null && (mm.getTarget() + "").endsWith("equivalent"))
              { removedmaps.add(mm); 
                for (int g = 0; g < callsrules.size(); g++) 
                { TransformationRule cr = (TransformationRule) callsrules.get(g); 
                  Maplet newmm = new Maplet(mm.getSource(), cr.getName()); 
                  addedmaps.add(newmm); 
                }
              } 
            }
          } 
        } 
      }  
      resrr.removeAll(removedmaps); 
      resrr.addAll(addedmaps);

      int rrefo = rr.analyseEFO(resrr); 
      if (rrefo > 5) { efo++; } 

      res.addAll(resrr);               
    } 
 
    for (int i = 0; i < transformationRules.size(); i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      Map resrr = new Map(); 
      rr.getCallGraph(resrr); 

      Vector addedmaps = new Vector(); 
      Vector removedmaps = new Vector(); 
      Vector equivs = rr.equivalentsUsedIn();
      if (equivs.size() > 0) 
      { // System.out.println("Equivalent expressions used in " + rr.name + " are: " + equivs);
        for (int j = 0; j < equivs.size(); j++) 
        { Expression ex = (Expression) equivs.get(j); 
          Type extype = ex.getType(); 
          if (extype != null)
          { Entity exent = extype.getEntity();
            if (exent == null && ex.getElementType() != null) 
            { exent = ex.getElementType().getEntity(); } 

            Vector callsrules = rulesOnInput(exent); 
            // System.out.println(ex + ".equivalent() can call rules " + callsrules); 
 
            for (int m = 0; m < resrr.size(); m++)
            { Maplet mm = (Maplet) resrr.get(m); 
              // System.out.println(">>> " + mm.getTarget()); 

              if (mm.getTarget() != null && (mm.getTarget() + "").endsWith("equivalent"))
              { removedmaps.add(mm); 
                for (int g = 0; g < callsrules.size(); g++) 
                { TransformationRule cr = (TransformationRule) callsrules.get(g); 
                  Maplet newmm = new Maplet(mm.getSource(), cr.getName()); 
                  addedmaps.add(newmm); 
                }
              } 
            }
          } 
        } 
      }  
      resrr.removeAll(removedmaps); 
      resrr.addAll(addedmaps);

      int rrefo = rr.analyseEFO(resrr); 
      if (rrefo > 5) { efo++; } 

      res.addAll(resrr);               
    } 

    System.out.println("*** EFO for module = " + efo); 

    return res; 
  } 

    
  public int uex()
  { // number of concrete non-lazy rules
    int r = transformationRules.size(); 
    int count = 0; 
    for (int i = 0; i < r; i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      if (rr.isLazy()) { } 
      else 
      { count++; } 
    } 
    
    return (count*(count-1))/2; 
  } 
    
  public int enr()
  { // number of rules, including pre/post blocks

    int r = transformationRules.size(); 
    return r + pre.size() + post.size(); 
  } 

  public int eno()
  { // number of operations

    int r = operations.size(); 
    return r; 
  } 

  public int epl()
  { // count 1 for each rule or operation over the limit of 10
    int r = 0; 

    for (int i = 0; i < operations.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) operations.get(i); 
      int bfepl = bf.epl();
      if (bfepl > 10)
      { System.err.println("*** Too many parameters in " + bf.getName() + " : " + bfepl); 
        r++; 
      } 
    } 
   
    for (int i = 0; i < transformationRules.size(); i++) 
    { TransformationRule rr = (TransformationRule) transformationRules.get(i); 
      int rrepl = rr.epl();
      if (rrepl > 10)
      { System.err.println("*** Too many parameters in " + rr.getName() + " : " + rrepl); 
        r++; 
      } 
    } 
    return r; 
  } 

}

