import java.util.Vector; 
import java.io.*; 
import java.util.StringTokenizer; 
import javax.swing.JOptionPane; 
import java.util.Set; 
import java.util.HashSet; 


/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Class Diagram
*/

public class BehaviouralFeature extends ModelElement
{ private Vector parameters = new Vector(); // Attribute
  private Vector typeParameters = new Vector(); // Type

  private boolean query = true;
  private Type resultType;
  private Type elementType; // Of the result

  private Expression pre;   // For OCL operation specs
  private Expression bpre;  // For preconditions of B ops
  private Expression post; 

  protected Entity entity;  // the owner of the operation, null for global op
  private UseCase useCase;  // the use case that owns the operation, if any

  private boolean instanceScope = true; 
  private boolean ordered = false;  // true for ops on sequences
  private boolean sorted = false;   // true if op returns a sorted set

  private Statemachine sm = null; 
  private Statement activity = null; 
  private String text = "";  // The text of the operation

  private String comments = null;  

  private boolean derived = false; 
  private boolean bx = false; 

  private Vector readfr = null; 
  private Vector writefr = null; 
  private Expression defcond = null; 
  private Expression detcond = null; 
  private Vector myMutants = new Vector(); 


  public BehaviouralFeature(String nme, Vector pars,
                            boolean readstatus,
                            Type res)
  { super(nme);
    parameters = pars;
    if (pars == null) 
    { parameters = new Vector(); } 

    query = readstatus;
    if (query) 
    { addStereotype("query"); }
 
    resultType = res;
    if (res != null && Type.isCollectionType(res)) { } 
    else 
    { elementType = res; }  // primitive, String, entity type
  }

  public BehaviouralFeature(String nme, Vector pars,
                            Statement stat)
  { super(nme);
    parameters = pars;
    if (pars == null) 
    { parameters = new Vector(); } 

    query = false;
    activity = stat; 
  }

  public BehaviouralFeature(String nme, Vector pars,
                            Vector stats)
  { super(nme);
    parameters = pars;
    if (pars == null) 
    { parameters = new Vector(); } 

    

    query = false;
    if (stats == null || stats.size() == 0) 
    { activity = new InvocationStatement("skip");
      return; 
    }

    Vector actualstatements = new Vector();
    for (int i = 0; i < stats.size(); i++) 
    { if (stats.get(i) instanceof Statement)
      { actualstatements.add(stats.get(i)); } 
    } 

    if (actualstatements.size() == 1)
    { activity = (Statement) actualstatements.get(0); } 
    else 
    { activity = new SequenceStatement(actualstatements); }  
  }

  public BehaviouralFeature(String nme) 
  { // creates update operation
    super(nme); 
    query = false; 
    resultType = null; 
    elementType = null; 
  } 

  public void setType(Type rt)
  { resultType = rt; }

  public void setReturnType(Type rt)
  { 
    resultType = rt;
    if (rt != null && Type.isCollectionType(rt)) { } 
    else 
    { elementType = rt; }  // primitive, String, entity type
  } 

  public void setComments(String comms)
  { comments = comms; } 

  public Expression makeLambdaExpression(Expression inst) 
  { // lambda pars : ParTypes in inst.name(pars)

    BasicExpression be = new BasicExpression(this); 
    be.setObjectRef(inst); 

    return UnaryExpression.newLambdaUnaryExpression(be,this); 
  } 

  public void jsClassFromConstructor(Entity ent, Entity cclass, Vector inits, Vector entities) 
  { // For each statement  self.att := expr  in activity
    // make att an attribute of ent, with initialisation expr
    // move operations from cclass to ent if they are used
    // in activity. 

    Type etype = new Type(ent); 
    String ename = ent.getName(); 
    BasicExpression res = 
      BasicExpression.newVariableBasicExpression(
                                 "_res_", etype); 

    BehaviouralFeature initialiseEnt = 
      new BehaviouralFeature("initialise" + ename); 
    initialiseEnt.setActivity((Statement) activity.clone()); 
    initialiseEnt.setOwner(ent); 
    initialiseEnt.setParameters(parameters); 
    ent.addOperation(initialiseEnt); 

    if (activity != null) 
    { Vector asgns = 
        Statement.getAssignments(activity);
      Vector calls = 
        Statement.getOperationCalls(activity); 

      System.out.println("^^^^^^^ Operation calls: " + calls); 
      System.out.println(); 

      Vector pars = new Vector(); 
      Vector exprs = new Vector();  
      for (int i = 0; i < asgns.size(); i++) 
      { AssignStatement asgn = 
          (AssignStatement) asgns.get(i); 
        Expression lhs = asgn.getLeft(); 
        Expression rhs = asgn.getRight(); 

        // if parameters[ind] equals lhs.data, 
        // use inits[ind] for initialisation
        // else use rhs. 

        if (lhs instanceof BasicExpression) 
        { BasicExpression be = (BasicExpression) lhs; 
          if ("self".equals(be.objectRef + ""))
          { Expression init = rhs;
            if (parameters != null && 
                ModelElement.lookupByName(
                    be.data, parameters) != null)
            { int indx = 
                ModelElement.indexByName(be.data, parameters); 
              if (0 <= indx && indx < inits.size())
              { init = (Expression) inits.get(indx); }  
              Attribute att = new Attribute(be,rhs,init); 
              ent.addAttribute(att); 
              pars.add(att); 
              exprs.add(new BasicExpression(att));
              Attribute par = 
                (Attribute) ModelElement.lookupByName(
                                  be.data, parameters); 
              par.setType(att.getType());  
            }
            else   
            { Expression newinit = init; 
              if (init instanceof UnaryExpression)
              { String ff = 
                  ((UnaryExpression) init).functionOfLambda(); 
                // System.out.println("++++ Function call: " + init + " " + ff); 
                if (ff != null) 
                { BehaviouralFeature oldop = 
                      cclass.getOperation(ff); 
                  if (oldop != null) 
                  { oldop.setOwner(ent); 
                    cclass.removeOperation(oldop); 
                    ent.addOperation(oldop); 
                  } 
                }
              }
              Attribute attf = new Attribute(be,rhs,newinit); 
              ent.addAttribute(attf);
                
            } // a method in fact. 
            be.setObjectRef(res);  
          }
          else if (rhs instanceof BasicExpression)
          { BasicExpression rhsbe = (BasicExpression) rhs; 
            if (rhsbe.isCallBasicExpression())
            { String fnme = rhsbe.getData(); 
              if (fnme != null) 
              { BehaviouralFeature oldop = 
                      cclass.getOperation(fnme); 
                if (oldop != null) 
                { oldop.setOwner(ent); 
                  cclass.removeOperation(oldop); 
                  ent.addOperation(oldop); 
                } 
              }
            }
          }
          else if (rhs instanceof UnaryExpression)
          { String ff = 
                  ((UnaryExpression) rhs).functionOfLambda(); 
            // System.out.println("++++ Function call: " + rhs + " " + ff); 
            if (ff != null) 
            { BehaviouralFeature oldop = 
                      cclass.getOperation(ff); 
              if (oldop != null) 
              { oldop.setOwner(ent); 
                cclass.removeOperation(oldop); 
                ent.addOperation(oldop); 
              } 
            }
          }       
        } 
      }

      // and there may be calls self.SupEnt(pars) 
      // of the superclass. 
    
    /* 
      if (ent.hasOperation("new" + ename)) { } 
      else 
      { BehaviouralFeature bf = 
           newStaticConstructor(ent,pars,exprs);
        ent.addOperation(bf); 
      } */  

      SequenceStatement code = new SequenceStatement(); 
 
      CreationStatement cs = new CreationStatement("_res_", etype); 
      code.addStatement(cs); 

      BasicExpression createCall = 
         new BasicExpression("create" + ename); 
      createCall.setUmlKind(Expression.UPDATEOP); 
      createCall.setParameters(new Vector()); 
      createCall.setIsEvent(); 
      createCall.setType(etype); 
      createCall.setStatic(true); 
      // createCall.entity = e; 

      AssignStatement cassgn = 
        new AssignStatement(res,createCall); 
      code.addStatement(cassgn);
      Statement newactivity = 
         activity.substituteEq("self", res);  
      code.addStatement(newactivity); 
      code.addStatement(new ReturnStatement(res));
      activity = code; 

      for (int h = 0; h < calls.size(); h++) 
      { InvocationStatement opcall = 
          (InvocationStatement) calls.get(h);
        if (opcall.callExp instanceof BasicExpression)
        { BasicExpression called = 
            (BasicExpression) opcall.callExp;
          String calledname = called.getData(); 
          if (calledname.startsWith("initialise"))
          { String superclassName = 
               calledname.substring(10);
            System.out.println("^^^^^ Superclass: " + superclassName);  
            Entity superEnt = 
              (Entity) ModelElement.lookupByName(
                           superclassName, entities); 
            if (superEnt != null) 
            { ent.setSuperclass(superEnt); 
              superEnt.addSubclass(ent); 
            } 
          }   
        }  
      }  
    }   
  } 

  public void addBeforeActivityEnd(Statement st)
  { if (activity != null)
    { Statement.addBeforeEnd(activity,st); } 
  } 

  public void addBeforeActivityEnd(Entity ent, Attribute att, 
                                   Expression val)
  { if (activity != null)
    { Type etype = new Type(ent); 
      BasicExpression res = 
        BasicExpression.newVariableBasicExpression(
                                 "_res_", etype);
      BasicExpression lhs = new BasicExpression(att); 

      if (att.isStatic())
      { lhs.setObjectRef(new BasicExpression(ent));
        lhs.setStatic(true); 
      }
      else 
      { lhs.setObjectRef(res); } 
  
      Statement.addBeforeEnd(activity,
        new AssignStatement(lhs,val));
    } 
  } 

  public void setBx(boolean b)
  { bx = b; } 

  public boolean isNoRecursion()
  { return hasStereotype("noRecursion"); } 

  public void setTypeParameters(String generics, Vector entities, Vector types)
  { String gens = generics.trim(); 
    if (gens.length() == 0) 
    { return; } 

    StringTokenizer stok = new StringTokenizer(gens, "<>, "); 
    Vector pars = new Vector(); 
    while (stok.hasMoreTokens())
    { String par = stok.nextToken(); 
      Type tt = Type.getTypeFor(par,types,entities); 
      if (tt != null) 
      { pars.add(tt); }
      else 
      { Entity entpar = new Entity(par); 
        tt = new Type(entpar); 
        pars.add(tt); 
      } 
      System.out.println(">> Added type parameter " + tt); 
    } 

    typeParameters = pars;  
  } 

  public void setTypeParameters(Vector pars)
  { typeParameters = pars; }

  public boolean isGeneric()
  { return typeParameters != null && 
           typeParameters.size() > 0; 
  } 

  public String getCompleteName()
  { String nme = getName();

    if (typeParameters.size() > 0)
    { String tp = ""; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { tp = tp + 
           ((ModelElement) typeParameters.get(i)).getName();
        if (i < typeParameters.size()-1)
        { tp = tp + ","; } 
      } 
      nme = nme + "<" + tp + ">"; 
    }
 
    return nme; 
  } 

  public int getArity()
  { if (parameters == null) 
    { return 0; } 
    return parameters.size(); 
  } 

  public Object clone()
  { Vector newpars = new Vector(); 
    if (parameters == null) { } 
    else 
    { for (int i = 0; i < parameters.size(); i++) 
      { Attribute par = (Attribute) parameters.get(i); 
        Attribute newpar = (Attribute) par.clone(); 
        newpars.add(newpar); 
      } 
    } 
    BehaviouralFeature res = new BehaviouralFeature(getName(),
                                  newpars, query, resultType); 
    res.setElementType(elementType); 
    res.setStatechart(sm); 
    res.setActivity(activity); 
    res.setEntity(entity); 
    res.setInstanceScope(instanceScope); 
    res.setDerived(derived); 
    res.setOrdered(ordered); 
    res.setSorted(sorted); 
    res.setPre(pre);    // I assume these 
    res.setPost(post);  // should be copied, or cloned? 
    res.setBx(bx); 
    if (typeParameters != null) 
    { res.typeParameters = new Vector(); 
      res.typeParameters.addAll(typeParameters); 
    } // clone them. 

    return res; 
  } // and copy the use case, readfr, etc? 

  public BehaviouralFeature interfaceOperation()
  { BehaviouralFeature res = (BehaviouralFeature) clone(); 
    res.activity = null; 
    res.setAbstract(true); 
    return res; 
  } 

  public BehaviouralFeature addContainerReference(
                               BasicExpression ref, 
                               String var, Vector excl)
  { BehaviouralFeature res = new BehaviouralFeature(getName(), parameters, query, resultType); 
    res.setElementType(elementType); 
    res.setStatechart(sm); 
    res.setEntity(entity); 
    res.setInstanceScope(instanceScope); 
    res.setDerived(derived); 
    res.setOrdered(ordered); 
    res.setSorted(sorted); 
    res.setBx(bx); 

    Vector newexcl = new Vector(); 
    newexcl.addAll(excl); 
    newexcl.add(getName()); 

    if (parameters != null) 
    { for (int i = 0; i < parameters.size(); i++) 
      { Attribute att = (Attribute) parameters.get(i); 
        newexcl.add(att.getName()); 
      } 
    } 

    if (typeParameters != null) 
    { res.typeParameters = new Vector(); 
      res.typeParameters.addAll(typeParameters); 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        newexcl.add(tp.getName()); 
      } 
    } 

    Expression newpre = null;  
    if (pre != null)
    { newpre = pre.addContainerReference(ref,var,newexcl); } 
    else 
    { newpre = new BasicExpression(true); } 
    Expression newpost = null;
    if (post != null)
    { newpost = post.addContainerReference(ref,var,newexcl); }
    else 
    { newpost = new BasicExpression(true); }  
    Statement newact = null;
    if (activity != null) {
      newact = activity.addContainerReference(ref,var,newexcl); 
    } 
	
    res.setActivity(newact); 
    res.setPre(newpre);     
    res.setPost(newpost);   

    return res; 
  } // and copy the use case, readfr, etc? 

  public static BehaviouralFeature newConstructor(String ename, 
                                       Entity ent, Vector pars)
  { BehaviouralFeature bf = new BehaviouralFeature("new" + ename); 
    bf.setParameters(pars); 
    Entity e = new Entity(ename); 
 
    Type etype = new Type(e); 
    bf.setType(etype); 
    bf.setPostcondition(new BasicExpression(true)); 
    SequenceStatement code = new SequenceStatement();

    BasicExpression res = 
      BasicExpression.newVariableBasicExpression(
                          "res", etype); 
 
    CreationStatement cs = 
        new CreationStatement("res", etype);
    // cs.setInstanceType(etype);  
    code.addStatement(cs); 

    BasicExpression createCall = 
        new BasicExpression("create" + ename); 
    createCall.setUmlKind(Expression.UPDATEOP); 
    createCall.setParameters(new Vector()); 
    createCall.setIsEvent(); 
    createCall.setType(etype); 
    createCall.setStatic(true); 
    // createCall.entity = e; 

    AssignStatement assgn = new AssignStatement(res,createCall); 
    code.addStatement(assgn); 

    BasicExpression initialiseCall = 
       new BasicExpression("initialise"); 
    initialiseCall.setUmlKind(Expression.UPDATEOP);
    initialiseCall.setIsEvent(); 
    Vector parNames = bf.getParameterExpressions(); 
    // System.out.println(">>=== " + pars + " " + parNames); 
 
    initialiseCall.setParameters(parNames); 
    initialiseCall.setObjectRef(res); 
    InvocationStatement callInit = 
        new InvocationStatement(initialiseCall);
    callInit.setParameters(parNames);  
    code.addStatement(callInit); 

    // Add each instance _initialiseInstance() operation
    // res._initialiseInstance()
    if (ent != null) 
    { Vector allops = ent.getOperations(); 
      for (int i = 0; i < allops.size(); i++) 
      { BehaviouralFeature op = (BehaviouralFeature) allops.get(i); 
        String opname = op.getName(); 
        if (opname.startsWith("_initialiseInstance"))
        { BasicExpression initialiseInstanceCall = 
                             new BasicExpression(opname); 
          initialiseInstanceCall.setUmlKind(Expression.UPDATEOP);
          initialiseInstanceCall.setIsEvent(); 
          initialiseInstanceCall.setParameters(new Vector()); 
          initialiseInstanceCall.setObjectRef(res); 
          InvocationStatement callInstInit = 
            new InvocationStatement(initialiseInstanceCall);
          callInstInit.setParameters(new Vector());  
          code.addStatement(callInstInit); 
        } 
      } 
    }
	
    ReturnStatement rs = new ReturnStatement(res); 
    code.addStatement(rs); 

    code.setBrackets(true); 

    bf.setActivity(code); 
    bf.setStatic(true); 

    return bf; 
  } 

  public static BehaviouralFeature newConstructor(String ename, 
                                       Entity ent, Vector pars,
                                       Vector gpars)
  { BehaviouralFeature bf = new BehaviouralFeature("new" + ename); 
    bf.setParameters(pars); 
    Entity e = new Entity(ename); 
    if (gpars != null) 
    { e.setTypeParameters(gpars); }
 
    Type etype = new Type(e); 
    bf.setType(etype); 

    System.out.println(">> Generic parameters of " + bf + " are " + gpars); 

    System.out.println(">> Return type of " + bf + " is " + e.getCompleteName()); 

    bf.setPostcondition(new BasicExpression(true)); 
    SequenceStatement code = new SequenceStatement();

    BasicExpression res = 
      BasicExpression.newVariableBasicExpression(
                          "res", etype); 
 
    CreationStatement cs = 
        new CreationStatement("res", etype);
    cs.setInstanceType(etype);  
    code.addStatement(cs); 

    BasicExpression createCall = 
        new BasicExpression("create" + ename); 
    createCall.setUmlKind(Expression.UPDATEOP); 
    createCall.setParameters(new Vector()); 
    createCall.setIsEvent(); 
    createCall.setType(etype); 
    createCall.setStatic(true); 
    // createCall.entity = e; 

    AssignStatement assgn = new AssignStatement(res,createCall); 
    code.addStatement(assgn); 

    BasicExpression initialiseCall = 
       new BasicExpression("initialise"); 
    initialiseCall.setUmlKind(Expression.UPDATEOP);
    initialiseCall.setIsEvent(); 
    Vector parNames = bf.getParameterExpressions(); 
    // System.out.println(">>=== " + pars + " " + parNames); 
 
    initialiseCall.setParameters(parNames); 
    initialiseCall.setObjectRef(res); 
    InvocationStatement callInit = 
        new InvocationStatement(initialiseCall);
    callInit.setParameters(parNames);  
    code.addStatement(callInit); 

    // Add each instance _initialiseInstance() operation
    // res._initialiseInstance()
    Vector allops = ent.getOperations(); 
    for (int i = 0; i < allops.size(); i++) 
    { BehaviouralFeature op = (BehaviouralFeature) allops.get(i); 
      String opname = op.getName(); 
      if (opname.startsWith("_initialiseInstance"))
      { BasicExpression initialiseInstanceCall = 
                             new BasicExpression(opname); 
        initialiseInstanceCall.setUmlKind(Expression.UPDATEOP);
        initialiseInstanceCall.setIsEvent(); 
        initialiseInstanceCall.setParameters(new Vector()); 
        initialiseInstanceCall.setObjectRef(res); 
        InvocationStatement callInstInit = 
          new InvocationStatement(initialiseInstanceCall);
        callInstInit.setParameters(new Vector());  
        code.addStatement(callInstInit); 
      } 
    } 

    ReturnStatement rs = new ReturnStatement(res); 
    code.addStatement(rs); 

    code.setBrackets(true); 

    bf.setActivity(code); 
    bf.setStatic(true); 

    return bf; 
  } 

  public static BehaviouralFeature newStaticConstructor(String ename, Vector pars)
  { BehaviouralFeature bf = new BehaviouralFeature("new" + ename); 
    bf.setParameters(pars); 
    Entity e = new Entity(ename); 
    Type etype = new Type(e); 
    bf.setType(etype); 
    bf.setPostcondition(new BasicExpression(true)); 
    SequenceStatement code = new SequenceStatement();

    BasicExpression res = BasicExpression.newVariableBasicExpression("result", etype); 
 
    CreationStatement cs = new CreationStatement("result", etype); 
    code.addStatement(cs); 

    BasicExpression createCall = new BasicExpression("create" + ename); 
    createCall.setUmlKind(Expression.UPDATEOP); 
    createCall.setParameters(new Vector()); 
    createCall.setIsEvent(); 
    createCall.setType(etype); 
    createCall.setStatic(true); 
    // createCall.entity = e; 

    AssignStatement assgn = new AssignStatement(res,createCall); 
    code.addStatement(assgn); 

    for (int i = 0; i < pars.size(); i++) 
    { Attribute attr = (Attribute) pars.get(i); 
      BasicExpression lhs = new BasicExpression(attr); 
      lhs.setObjectRef(res); 
      BasicExpression rhs = new BasicExpression(attr); 
      AssignStatement assgnpar = 
        new AssignStatement(lhs,rhs); 
      code.addStatement(assgnpar); 
    } 

    ReturnStatement rs = new ReturnStatement(res); 
    code.addStatement(rs); 

    code.setBrackets(true); 

    bf.setActivity(code); 
    bf.setStatic(true); 

    return bf; 
  } 

  public static BehaviouralFeature newStaticConstructor(Entity ent)
  { String ename = ent.getName(); 
    BehaviouralFeature bf = new BehaviouralFeature("new" + ename); 
    bf.setParameters(new Vector()); 
    Type etype = new Type(ent); 
    bf.setType(etype); 
    bf.setPostcondition(new BasicExpression(true)); 
    SequenceStatement code = new SequenceStatement();

    BasicExpression res = BasicExpression.newVariableBasicExpression("result", etype); 
 
    CreationStatement cs = new CreationStatement("result", etype); 
    code.addStatement(cs); 

    BasicExpression createCall = new BasicExpression("create" + ename); 
    createCall.setUmlKind(Expression.UPDATEOP); 
    createCall.setParameters(new Vector()); 
    createCall.setIsEvent(); 
    createCall.setType(etype); 
    createCall.setStatic(true); 
    // createCall.entity = e; 

    AssignStatement assgn = new AssignStatement(res,createCall); 
    code.addStatement(assgn); 

    Vector pars = ent.getAttributes(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute attr = (Attribute) pars.get(i); 
      BasicExpression lhs = new BasicExpression(attr); 
      lhs.setObjectRef(res); 
      Expression rhs = attr.getInitialisation();
      if (rhs != null) 
      { AssignStatement assgnpar = 
          new AssignStatement(lhs,rhs); 
        code.addStatement(assgnpar);
      }  
    } 

    BasicExpression initialiseCall = 
      new BasicExpression("initialise"); 
    initialiseCall.setUmlKind(Expression.UPDATEOP);
    initialiseCall.setIsEvent(); 
    // Vector parNames = bf.getParameterExpressions(); 
    // System.out.println(">>=== " + pars + " " + parNames); 
 
    initialiseCall.setParameters(new Vector()); 
    initialiseCall.setObjectRef(res); 
    InvocationStatement callInit = 
       new InvocationStatement(initialiseCall);
    // callInit.setParameters(parNames);  
    code.addStatement(callInit); 

    ReturnStatement rs = new ReturnStatement(res); 
    code.addStatement(rs); 

    code.setBrackets(true); 

    bf.setActivity(code); 
    bf.setStatic(true); 

    return bf; 
  } 

  public static BehaviouralFeature newStaticConstructor(Entity ent, Vector pars, Vector exprs)
  { String ename = ent.getName(); 
    BehaviouralFeature bf = new BehaviouralFeature("new" + ename); 
    bf.setParameters(pars); 
    Type etype = new Type(ent); 
    bf.setType(etype); 
    bf.setPostcondition(new BasicExpression(true)); 
    SequenceStatement code = new SequenceStatement();

    BasicExpression res = BasicExpression.newVariableBasicExpression("result", etype); 
 
    CreationStatement cs = new CreationStatement("result", etype); 
    code.addStatement(cs); 

    BasicExpression createCall = new BasicExpression("create" + ename); 
    createCall.setUmlKind(Expression.UPDATEOP); 
    createCall.setParameters(new Vector()); 
    createCall.setIsEvent(); 
    createCall.setType(etype); 
    createCall.setStatic(true); 
    // createCall.entity = e; 

    AssignStatement assgn = new AssignStatement(res,createCall); 
    code.addStatement(assgn); 

    // Vector pars = ent.getAttributes(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute attr = (Attribute) pars.get(i); 
      BasicExpression lhs = new BasicExpression(attr); 
      lhs.setObjectRef(res); 
      Expression rhs = (Expression) exprs.get(i);
      if (rhs != null) 
      { AssignStatement assgnpar = 
          new AssignStatement(lhs,rhs); 
        code.addStatement(assgnpar);
      }  
    } 

    // BasicExpression initialiseCall = 
    //   new BasicExpression("initialise"); 
    // initialiseCall.setUmlKind(Expression.UPDATEOP);
    // initialiseCall.setIsEvent(); 
    // Vector parNames = bf.getParameterExpressions(); 
    // System.out.println(">>=== " + pars + " " + parNames); 
 
    // initialiseCall.setParameters(new Vector()); 
    // initialiseCall.setObjectRef(res); 
    // InvocationStatement callInit = 
    //    new InvocationStatement(initialiseCall);
    // callInit.setParameters(parNames);  
    // code.addStatement(callInit); 

    ReturnStatement rs = new ReturnStatement(res); 
    code.addStatement(rs); 

    code.setBrackets(true); 

    bf.setActivity(code); 
    bf.setStatic(true); 

    return bf; 
  } 

  public static BehaviouralFeature fromAttribute(Attribute att)
  { BehaviouralFeature res = 
      new BehaviouralFeature(att.getName()); 
    res.parameters = att.getParameters(); 
    res.resultType = att.getInnerElementType(); 

    System.out.println("==== Operation from attribute: " + res.display()); 

    return res; 
  } 

  public UseCase toUseCase()
  { // usecase name : resultType 
    // { parameter ... assumptions ... postconditions }

    UseCase res = new UseCase(getName()); 
    res.addParameters(parameters); 
    res.setResultType(resultType); 
    res.setElementType(elementType); 
    if (pre != null) 
    { res.addPrecondition(pre); } 
    if (post != null) 
    { res.addPostcondition(post); } 
    res.setActivity(activity); 
    res.setTypeParameters(typeParameters); 

    return res; 
  } // type parameters become those of the linked class

  public boolean isMutatable()
  { if (isDerived())
    { return false; } 
    if (parameters == null || parameters.size() == 0) 
    { return false; } 
    if (resultType == null || "void".equals(resultType + ""))
    { return false; } 
    return isQuery(); 
  } // At least one input parameter, and a result parameter

  public Vector getMutants()
  { return myMutants; } 

  public Vector getMutants(Vector allops)
  { Vector res = new Vector(); 
    for (int i = 0; i < myMutants.size(); i++) 
    { BehaviouralFeature bf = (BehaviouralFeature) myMutants.get(i); 
      String nme = bf.getName(); 
      if (ModelElement.lookupByName(nme,allops) != null)
      { res.add(bf); }
    } 
    return res; 
  } // only mutants that actually are in the class
 

  public Vector formMutantOperations(Vector posts) 
  { if (myMutants.size() > 0) 
    { return myMutants; } // cached result. 

    Vector res = new Vector();
    String nme = getName(); 
 
    for (int i = 0; i < posts.size(); i++) 
    { Expression postx = (Expression) posts.get(i); 
      BehaviouralFeature bfclone = (BehaviouralFeature) this.clone(); 
      bfclone.setName(nme + "_mutant_" + i); 
      bfclone.setPost(postx); 
      bfclone.setDerived(true); 
      res.add(bfclone); 
    } 

    if ("true".equals(post + "") && activity != null) 
    { Vector statmuts = activity.singleMutants();
      res = new Vector();  
      for (int i = 0; i < statmuts.size(); i++) 
      { Statement statmut = (Statement) statmuts.get(i); 
        BehaviouralFeature bfclone = (BehaviouralFeature) this.clone(); 
        bfclone.setName(nme + "_mutant_" + i); 
        bfclone.setActivity(statmut); 
        bfclone.setDerived(true); 
        res.add(bfclone); 
      }
    }  

    JOptionPane.showMessageDialog(null, "Mutant versions of " + getName() + " are: " + res, 
                "", JOptionPane.INFORMATION_MESSAGE);  

    myMutants = new Vector(); 
    myMutants.addAll(res); 

    return res; 
  } 

  public Vector formMutantCalls(String ename, Vector mutants, Vector tests, Vector alltests, Vector testcalls) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCode(tst);  
           // parameterInitialisationCode(tvals); 

        String javaType = resultType.getJava(); 

        String call = javaType + " " + nme + "_result = _self." + this.toString() + ";\n";  
        call = call + 
            "    System.out.println(\"Test " + j + " of " + nme + " on \" + _self + \" result = \" + " + nme + "_result);\n";
 
        // Should use "  try {\n"

        String testcode = 
          "  public static void " + nme + "_mutation_tests_" + j + "(" + ename + " _self, int[] _counts, int[] _totals)\n" + 
          "  {\n" + 
          "    " + code + "\n" + 
          "    try {\n  " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          testcode = testcode +  
            "    try {\n" + 
            "      " + javaType + " " + mname + "_result = " + mutantcall + ";\n" +    
            "      if ((\"\" + " + nme + "_result).equals(\"\" + " + mname + "_result)) { _totals[" + j + "]++; } else\n" + 
            "      { System.out.println(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\");\n" +
            "        _counts[" + j + "]++; \n" +   
            "        _totals[" + j + "]++;\n" + 
            "      }\n" + 
            "    } catch (Throwable _e) {\n" +
            "        _counts[" + j + "]++; \n" +   
            "        _totals[" + j + "]++;\n" + 
            "      }\n"; 
        } 
        testcode = testcode + "    } catch (Throwable _e) { }\n }\n\n"; 

 /* Should be: 
             "  } catch (Throwable _e) { _e.printStackTrace(); }\n"; */ 
 
        res.add(testcode); 

        String testcall = "    MutationTest." + nme + "_mutation_tests_" + j + "(_self,_counts,_totals);\n"; 
        testcalls.add(testcall); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsCSharp(String ename, Vector mutants, Vector tests, Vector alltests, Vector testcalls) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodeCSharp(tst);  
           // parameterInitialisationCode(tvals); 

        String javaType = resultType.getCSharp(); 

        String call = javaType + " " + nme + "_result = _self." + this.toString() + ";\n";  
        if (this.isStatic()) 
        { call = javaType + " " + nme + "_result = " + ename + "." + this.toString() + ";\n"; } 
        call = call + 
            "    Console.WriteLine(\"Test " + j + " of " + nme + " on \" + _self + \" result = \" + " + nme + "_result);\n";
 
        // Should use "  try {\n"

        String testcode = 
          "  public static void " + nme + "_mutation_tests_" + j + "(" + ename + " _self, int[] _counts, int[] _totals)\n" + 
          "  {\n" + 
          "    " + code + "\n" + 
          "    try {\n  " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          if (this.isStatic()) 
          { mutantcall = ename + "." + m.toString(); } 

          testcode = testcode +  
            "    try {\n" + 
            "      " + javaType + " " + mname + "_result = " + mutantcall + ";\n" +    
            "      if ((\"\" + " + nme + "_result).Equals(\"\" + " + mname + "_result)) { _totals[" + j + "]++; } else\n" + 
            "      { Console.WriteLine(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\");\n" +
            "        _counts[" + j + "]++; \n" +   
            "        _totals[" + j + "]++;\n" + 
            "      }\n" + 
            "    } catch { }\n"; 
        } 
        testcode = testcode + "    } catch { }\n }\n\n"; 
 
        res.add(testcode); 

        String testcall = "    MutationTest." + nme + "_mutation_tests_" + j + "(_self,_counts,_totals);\n"; 
        testcalls.add(testcall); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsCPP(String ename, Vector mutants, Vector tests, Vector alltests, Vector testcalls) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodeCPP(tst);  
           // parameterInitialisationCode(tvals); 

        String javaType = resultType.getCPP(); 

        String call = javaType + " " + nme + "_result = _self->" + this.toString() + ";\n";  
        if (this.isStatic()) 
        { call = javaType + " " + nme + "_result = " + ename + "::" + this.toString() + ";\n"; } 
        call = call + 
            "    cout << \"Test \" << " + j + " << \" of " + nme + " on \" << _self << \" result = \" << " + nme + "_result << endl;\n";
 
        // Should use "  try {\n"

        String testcode = 
          "  static void " + nme + "_mutation_tests_" + j + "(" + ename + "* _self, int _counts[], int _totals[])\n" + 
          "  {\n" + 
          "    " + code + "\n" + 
          "    try {\n  " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self->" + m.toString(); 
          if (this.isStatic()) 
          { mutantcall = ename + "::" + m.toString(); } 

          testcode = testcode +  
            "    try {\n" + 
            "      " + javaType + " " + mname + "_result = " + mutantcall + ";\n" +    
            "      if (" + nme + "_result == " + mname + "_result) { _totals[" + j + "]++; } else\n" + 
            "      { cout << \"Test \" << " + j + " << \" of \" << " + tsts + " << \" detects " + nme + " mutant \" << " + i + " << \" of \" << " + muts + " << endl;\n" +
            "        _counts[" + j + "]++; \n" +   
            "        _totals[" + j + "]++;\n" + 
            "      }\n" + 
            "    } catch (...) { }\n"; 
        } 
        testcode = testcode + "    } catch (...) { }\n }\n\n"; 
 
        res.add(testcode); 

        String testcall = "    MutationTest::" + nme + "_mutation_tests_" + j + "(_self,_counts,_totals);\n"; 
        testcalls.add(testcall); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsJava6(Vector mutants, Vector tests, Vector alltests) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodeJava6(tst);  
           // parameterInitialisationCode(tvals); 

        String java6Type = resultType.getJava6(); 

        String call = java6Type + " " + nme + "_result = _self." + this.toString() + ";\n";  
        call = call + 
            "    System.out.println(\"Test " + j + " of " + nme + " on \" + _self + \" result = \" + " + nme + "_result);\n";
 
        String testcode = "  try {\n" + 
                          "    " + code + "\n" + 
                          "    " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          testcode = testcode +  
            "    " + java6Type + " " + mname + "_result = " + mutantcall + ";\n" +    
            "    if ((\"\" + " + nme + "_result).equals(\"\" + " + mname + "_result)) { _totals[" + j + "]++; } else\n" + 
            "    { System.out.println(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\");\n" +
            "      _counts[" + j + "]++; \n" +   
            "      _totals[" + j + "]++;\n" + 
            "    }\n";  
        } 
        testcode = testcode +
             "  } catch (Throwable _e) { _e.printStackTrace(); }\n"; 
        res.add(testcode); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsJava7(Vector mutants, Vector tests, Vector alltests) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodeJava7(tst);  
           // parameterInitialisationCode(tvals); 

        String java7Type = resultType.getJava7(); 

        String call = java7Type + " " + nme + "_result = _self." + this.toString() + ";\n";  
        call = call + 
            "    System.out.println(\"Test " + j + " of " + nme + " on \" + _self + \" result = \" + " + nme + "_result);\n";
 
        String testcode = "  try {\n" + 
                          "    " + code + "\n" + 
                          "    " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          testcode = testcode +  
            "    " + java7Type + " " + mname + "_result = " + mutantcall + ";\n" +    
            "    if ((\"\" + " + nme + "_result).equals(\"\" + " + mname + "_result)) { _totals[" + j + "]++; } else\n" + 
            "    { System.out.println(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\");\n" +
            "      _counts[" + j + "]++; \n" +   
            "      _totals[" + j + "]++;\n" + 
            "    }\n";  
        } 
        testcode = testcode +
             "  } catch (Throwable _e) { _e.printStackTrace(); }\n"; 
        res.add(testcode); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsJava8(Vector mutants, Vector tests, Vector alltests) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodeJava8(tst);  
           // parameterInitialisationCode(tvals); 

        String java8Type = resultType.getJava8(); 

        String call = java8Type + " " + nme + "_result = _self." + this.toString() + ";\n";  
        call = call + 
            "    System.out.println(\"Test " + j + " of " + nme + " on \" + _self + \" result = \" + " + nme + "_result);\n";
 
        String testcode = "  try {\n" + 
                          "    " + code + "\n" + 
                          "    " + call + "\n";  
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          testcode = testcode +  
            "    " + java8Type + " " + mname + "_result = " + mutantcall + ";\n" +    
            "    if ((\"\" + " + nme + "_result).equals(\"\" + " + mname + "_result)) { _totals[" + j + "]++; } else\n" + 
            "    { System.out.println(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\");\n" +
            "      _counts[" + j + "]++; \n" +   
            "      _totals[" + j + "]++;\n" + 
            "    }\n";  
        } 
        testcode = testcode +
             "  } catch (Throwable _e) { _e.printStackTrace(); }\n"; 
        res.add(testcode); 
      }  
    }  
    return res; 
  } 

  public Vector formMutantCallsPython(
    String ename, Vector mutants, Vector tests, 
    Vector alltests, Vector testcalls) 
  { // for each m : mutants, t : tests, set up a call to 
    // m(t.values)
 
    String nme = getName();         
    Vector res = new Vector();

    if (resultType != null && !("void".equals(resultType + "")))
    { int tsts = alltests.size(); 
      int muts = mutants.size(); 

      for (int j = 0; j < tsts; j++) 
      { Vector tst = (Vector) alltests.get(j);
        // String tvals = (String) tst.get(1); 
        String code = parInitCodePython(tst);  
           // parameterInitialisationCode(tvals); 

        // String javaType = resultType.getCSharp(); 

        String call = "      " + nme + "_result = _self." + this.toString() + "\n";  
        if (this.isStatic()) 
        { call = "      " + nme + "_result = app." + ename + "." + this.toString() + "\n"; } 
        call = call + 
            "      print(\"Test " + j + " of " + nme + " on \" + str(_self) + \" result = \" + str(" + nme + "_result))\n";
 
        // Should use "  try {\n"

        String testcode = 
          "  def " + nme + "_mutation_tests_" + j + "(_self, _counts, _totals) :\n" + 
          "    pass\n" + 
          code + "\n" + 
          "    try :\n" + 
          call +    
          "    except:\n" +   
          "      print(\"Unable to execute " + nme + "\")\n" + 
          "      return\n\n"; 
     
        for (int i = 0; i < muts; i++) 
        { BehaviouralFeature m = (BehaviouralFeature) mutants.get(i); 
          String mname = m.getName(); 
          String mutantcall = "_self." + m.toString(); 
          if (this.isStatic()) 
          { mutantcall = "app." + ename + "." + m.toString(); } 

          testcode = testcode +  
            "    try :\n" + 
            "      " + mname + "_result = " + mutantcall + ";\n" +    
            "      if str(" + nme + "_result) == str(" + mname + "_result) :\n" + 
            "         _totals[" + j + "] = _totals[" + j + "] + 1\n" + 
            "      else :\n" + 
            "        print(\"Test " + j + " of " + tsts + " detects " + nme + " mutant " + i + " of " + muts + "\")\n" +
            "        _counts[" + j + "] = _counts[" + j + "] + 1\n" +   
            "        _totals[" + j + "] = _totals[" + j + "] + 1\n" + 
            "    except :\n" + 
            "      pass\n\n"; 
        } 
        
        res.add(testcode); 

        String testcall = "    MutationTest." + nme + "_mutation_tests_" + j + "(_self,_counts,_totals)\n"; 
        testcalls.add(testcall); 
      }  
    }  
    return res; 
  } 


  public String parametersString()
  { String res = "";
    if (parameters == null) 
    { return res; } 
 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getName() + " : " + att.getType(); 
      if (i < parameters.size()-1)
      { res = res + ","; } 
    } 
    return res; 
  } 
    

  public boolean hasParameter(String nme) 
  { Attribute att = (Attribute) ModelElement.lookupByName(nme,parameters); 
    if (att != null) 
    { return true; } 
    return false; 
  } 

  public String parameterInitialisationCode(String strs) 
  { String res = ""; 
    int st = strs.indexOf("parameters"); 
    if (st >= 0) 
    { String data = strs.substring(st,strs.length()); 
      res = data.substring(11,data.length());
      int eqind = res.indexOf("="); 
      if (eqind >= 0) 
      { String par = res.substring(0,eqind).trim(); 
        String val = res.substring(eqind+2,res.length()).trim(); 

        int next = val.indexOf("parameters"); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { res = "    " + att.getType() + " " + par + " = "; } 
        else // it is a feature of the object
        { res = "    _self." + par + " = "; }

        String value = val; 
        if (att != null && att.isEntity())
        { Type atttype = att.getType(); 
          Entity attent = atttype.getEntity(); 
          String attentname = attent.getName(); 
          String es = attentname.toLowerCase() + "s"; 
          value = "(" + attentname + ") Controller.inst()." + es + ".get(0)"; 
        }
  
        if (next < 0) 
        { return res + value + ";"; } 
        String remainder = val.substring(next,val.length()); 
        
        if (att != null && att.isEntity()) { } 
        else 
        { value = val.substring(0,next).trim(); } 
 
        return res + value + ";\n" + 
                parameterInitialisationCode(remainder); 
      }  
      else 
      { eqind = res.indexOf(":");
        // else look for ":" in the case of collection pars
        // val : parameters.par

        if (eqind >= 0) 
        { String val = res.substring(0,eqind).trim(); 
          String par = res.substring(eqind+12,res.length()).trim(); 

          int rnext = val.indexOf(":"); 

        // if a parameter, give it a declaration
          Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
          if (att != null) 
          { res = "    " + att.getType() + " " + par + " = new " + att.getType().getJava() + "();\n" + 
                  "    " + par + ".add(" + val + ")"; } 
          else // it is a feature of the object
          { res = "    _self." + par + ".add(" + val + ")"; }

          if (rnext < 0) 
          { return res + ";"; } 
          String remainder = val.substring(rnext,val.length()); 
          String value = val.substring(0,rnext).trim(); 
          return res + value + ";\n" + 
                parameterInitialisationCode(remainder);
        } 
      } 
    } 
    return res; 
  } 

  public String parInitCode(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + " " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getJava() + " " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodeCSharp(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + " " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getCSharp() + " " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodeCPP(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + "* " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getCPP() + " " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self->" + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodeJava6(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + " " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getJava6() + " " + par + " " + rem + "\n"; }
        }   
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodeJava7(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + " " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getJava7() + " " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodeJava8(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = (Attribute) ModelElement.lookupByName(par,parameters); 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + leafent.getName() + " " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + atttype.getJava8() + " " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 

  public String parInitCodePython(Vector v) 
  { String res = ""; 
    for (int i = 0; i < v.size(); i++) 
    { String assign = (String) v.get(i); 
      int eqindex = assign.indexOf("="); 
      if (eqindex >= 0) 
      { String par = assign.substring(0,eqindex).trim(); 
        String rem = 
            assign.substring(eqindex,assign.length()); 

        // if a parameter, give it a declaration
        Attribute att = 
          (Attribute) ModelElement.lookupByName(par,parameters);
 
        if (att != null) 
        { Type atttype = att.getType(); 

          if (atttype.isAbstractEntity())
          { Entity absent = atttype.getEntity(); 
            Entity leafent = absent.firstLeafSubclass(); 
            res = res + "    " + par + " " + rem + "\n";
          } 
          else 
          { res = res + "    " + par + " " + rem + "\n"; }  
        } 
        else // it is a feature of the object
        { res = res + "    _self." + par + " " + rem + "\n"; }
      } 
    } 
    return res; 
  } 


  public void defineParameters(Entity inPars, Entity outPars)
  { // parameters = new Vector();
    if (inPars != null)  
    { parameters.addAll(inPars.getAttributes()); } 

    if (outPars != null) 
    { Vector outparams = outPars.getAttributes(); 
      if (outparams.size() > 0)
      { Attribute res = (Attribute) outparams.get(0); 
        resultType = res.getType(); 
      } 
    } 
  }

  public void addParameters(Vector atts)
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute par = (Attribute) atts.get(i); 
      if (hasParameter(par.getName())) { } 
      else 
      { parameters.add(par); }
    } 
  } 
 
    
  public void addParameter(Attribute att)
  { if (att == null) 
    { return; } 

    if (hasParameter(att.getName())) { } 
    else 
    { parameters.add(att); } 
  } 

  public void addFirstParameter(Attribute att)
  { if (att == null) 
    { return; } 

    if (hasParameter(att.getName())) { } 
    else 
    { parameters.add(0,att); } 
  } 

  public void addTypeParameter(Type t)
  { if (typeParameters.contains(t)) { } 
    else
    { typeParameters.add(t); } 
  } 

  public void removeParameters(Vector atts)
  { parameters.removeAll(atts); } 
 

  public Vector getTypeParameters()
  { return typeParameters; } 

  public void setStatechart(Statemachine s)
  { sm = s; } 

  public void setActivity(Statement st)
  { activity = st; } 

  public void setActivity(Vector stats)
  { if (stats == null || stats.size() == 0) 
    { activity = new InvocationStatement("skip"); }
    else if (stats.size() == 1) 
    { activity = (Statement) stats.get(0); } 
    else 
    { activity = new SequenceStatement(stats); } 
  }  

  public boolean hasResult()
  { if (resultType != null && 
        !("void".equals(resultType + "")))
    { return true; } 
    return false; 
  } 

  public boolean hasResultType()
  { return hasResult(); } 

  public void setResultType(Type t)
  { resultType = t; } 
  
  public boolean hasReturnVariable()
  { if (resultType == null) 
    { return false; }
    if ("void".equals(resultType + ""))
    { return false; }
    return true; 
  }

  public boolean hasReturnValue()
  { if (resultType == null) 
    { return false; }
    if ("void".equals(resultType + ""))
    { return false; }
    return true; 
  }

  public Type getFunctionType()
  { Type res; 
    if (resultType != null)
    { res = resultType; } 
    else 
    { res = new Type("void", null); }
    if (parameters == null || parameters.size() == 0)
    { Type ftype = new Type("Function", null); 
      ftype.setElementType(res); 
      ftype.setKeyType(res); 
      return ftype; 
    } 
 
    for (int i = parameters.size()-1; i >= 0; i--) 
    { Attribute par = (Attribute) parameters.get(i); 
      Type ptype = par.getType(); 
      Type ftype = new Type("Function", null); 
      ftype.setElementType(res); 
      ftype.setKeyType(ptype);
      res = ftype; 
    } 

    return res; 
  } 

  public static Type buildFunctionType(Vector pars)
  { Type res = new Type("OclAny", null); 
    if (pars == null || pars.size() == 0)
    { Type ftype = new Type("Function", null); 
      ftype.setElementType(res); 
      ftype.setKeyType(new Type("void", null)); 
      return ftype; 
    } 
 
    for (int i = pars.size()-1; i >= 0; i--) 
    { Attribute par = (Attribute) pars.get(i); 
      Type ptype = par.getType(); 
      Type ftype = new Type("Function", null); 
      ftype.setElementType(res); 
      ftype.setKeyType(ptype);
      res = ftype; 
    } 

    return res; 
  } 

  public boolean isZeroArgument()
  { return parameters.size() == 0; } 

  public Statement extendBehaviour()
  { System.out.println(">>> Operation activity = " + activity); 

    if (activity == null)
    { return new SequenceStatement(); } 

      // Vector newparams = new Vector(); 
      // newparams.addAll(parameters); 

    java.util.Map env = new java.util.HashMap(); 

    Statement stat = activity.generateDesign(env,false); 
    
    if (hasResult()) 
    { if (Statement.hasResultDeclaration(stat))
      { return stat; } 

      Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
      att.setElementType(elementType); 
        // newparams.add(att);

      System.out.println(">>> Enhanced activity = " + stat);   Vector contexts = new Vector(); 
      // newparams.addAll(ownedAttribute); 

      Statement newstat = new SequenceStatement(); 
        // ReturnStatement returnstat = null; 
      CreationStatement cres = new CreationStatement(resultType + "", "result");
      cres.setInstanceType(resultType);
      if (elementType != null) 
      { cres.setElementType(elementType); }    
      ((SequenceStatement) newstat).addStatement(cres);
	     // returnstat = new ReturnStatement(new BasicExpression(att));   
  
      ((SequenceStatement) newstat).addStatement(stat); 
        
        // if (returnstat != null) 
	   // { ((SequenceStatement) newstat).addStatement(returnstat); }
      
      JOptionPane.showMessageDialog(null, "Result declaration:  result : " + resultType + "(" + elementType + ")", 
                                      "", JOptionPane.INFORMATION_MESSAGE);  
         
      // newstat.typeCheck(types,entities,contexts,newparams); 
      return newstat; 
    } 
    return new SequenceStatement();
  } 


  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    args.add(getName());
    Vector eargs = new Vector();
    eargs.add(this);

    String typePar = ""; 
    if (typeParameters != null && typeParameters.size() > 0) 
    { Type tp = (Type) typeParameters.get(0); 
      typePar = tp.getName(); 
      args.add(typePar); 
      eargs.add(tp); 
    } 

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

    if (resultType != null) 
    { args.add(resultType.cg(cgs)); 
      eargs.add(resultType);
    } 
    else 
    { Type rt = new Type("void",null); 
      args.add(rt.cg(cgs)); 
      eargs.add(rt); 
    } 

    if (pre != null) 
    { args.add(pre.cg(cgs)); 
      eargs.add(pre); 
    } 
    else 
    { BasicExpression pr = new BasicExpression(true); 
      args.add(pr.cg(cgs));
      eargs.add(pr);  
    } 

    if (post != null) 
    { args.add(post.cg(cgs)); 
      eargs.add(post); 
    } 
    else 
    { BasicExpression pst = new BasicExpression(true); 
      args.add(pst.cg(cgs)); 
      eargs.add(pst); 
    } 

    if (activity != null)
    { if (hasResult())
      { Statement newact = extendBehaviour(); 
        String actres = newact.cg(cgs); 
        args.add(actres);
        eargs.add(newact);
      } 
      else 
      { String actres = activity.cg(cgs); 
      // System.out.println(">>> Activity result: " + actres); 
        args.add(actres);
        eargs.add(activity);
      }  
    }
    else 
    { Statement nullstat = new SequenceStatement(); 
      args.add(nullstat.cg(cgs));
      eargs.add(nullstat);  
    } 
    // only one Operation rule?
    // maybe for static/cached

    CGRule r = cgs.matchedOperationRule(this,etext);
    if (r != null)
    { System.out.println(">>> Matched operation rule: " + r + " with args " + args); 
      return r.applyRule(args,eargs,cgs); 
    }

    System.out.println("!!! No matching rule for operation " + getName()); 

    return etext;
  }
  
  
  public Vector testCases(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 

    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  

    // We are only interested in those that are *read* 
    // by the operation - its readFrame. 
    // And not frozen attributes. 
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i);

      if (att.isFrozen()) { continue; } 
 
      Vector testassignments = 
        att.testCases("parameters", lowerBounds, 
                      upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesCSharp(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 

    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  

    // We are only interested in those that are *read* 
    // by the operation - its readFrame. 
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 

      if (att.isFrozen()) { continue; } 

      Vector testassignments = 
        att.testCasesCSharp(
          "parameters", lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesCPP(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 

    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  

    // We are only interested in those that are *read* 
    // by the operation - its readFrame. 
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 

      if (att.isFrozen()) { continue; } 

      Vector testassignments = 
        att.testCasesCPP(
          "parameters", lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesPython(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 

    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  

    // We are only interested in those that are *read* 
    // by the operation - its readFrame. 
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 

      if (att.isFrozen()) { continue; } 

      Vector testassignments = 
        att.testCasesPython(
          "parameters", lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesJava6(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 
    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      // System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      // System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      // System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 

      if (att.isFrozen()) { continue; } 

      Vector testassignments = 
         att.testCasesJava6("parameters", 
               lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesJava7(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 
    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      // System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      // System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      // System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i); 
    
      if (att.isFrozen()) { continue; } 

      Vector testassignments = 
         att.testCasesJava7("parameters", 
                lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  public Vector testCasesJava8(Vector opTests)
  { Vector allattributes = getParameters();
    Vector preattributes = new Vector(); 
    preattributes.addAll(allattributes); 
    if (entity != null) 
    { preattributes.addAll(entity.getLocalFeatures()); }  
	
    String nme = getName();  
    Vector res = new Vector(); 
    Vector allOpTests = new Vector(); 

    if (preattributes == null || preattributes.size() == 0) 
    { res.add("-- no test for operation " + nme + "\n"); 
      return res; 
    }

    java.util.Map upperBounds = new java.util.HashMap(); 
    java.util.Map lowerBounds = new java.util.HashMap(); 
	
    Vector test0 = new Vector(); 
    test0.add(nme); 
    test0.add("-- tests for operation " + nme + "\n");
    res.add(test0);  
	
    Expression pre = precondition(); 
    if (pre != null) 
    { Vector bounds = new Vector(); 
      java.util.Map aBounds = new java.util.HashMap(); 
      pre.getParameterBounds(preattributes,bounds,aBounds);
	
    
      identifyUpperBounds(allattributes,aBounds,upperBounds); 
      identifyLowerBounds(allattributes,aBounds,lowerBounds); 
      // System.out.println(".>> Parameter bounds for operation " + nme + " : " + aBounds);  
      // System.out.println(".>> Upper bounds map for operation " + nme + " : " + upperBounds);  
      // System.out.println(".>> Lower bounds map for operation " + nme + " : " + lowerBounds);  
    }
	
    for (int i = 0; i < allattributes.size(); i++) 
    { Vector newres = new Vector(); 
      Vector attoptests = new Vector(); 
      Attribute att = (Attribute) allattributes.get(i);

      if (att.isFrozen()) { continue; } 
 
      Vector testassignments = 
         att.testCasesJava8("parameters", 
           lowerBounds, upperBounds, attoptests); 
      allOpTests.add(attoptests); 
	  
      for (int j = 0; j < res.size(); j++) 
      { Vector oldtest = (Vector) res.get(j); 
        String tst = (String) oldtest.get(1); 
        for (int k = 0; k < testassignments.size(); k++) 
        { String kstr = (String) testassignments.get(k); 
          if (kstr.length() > 0) 
          { String newtst = tst + "\n" + kstr; 
            Vector optest = new Vector(); 
            optest.add(nme); 
            optest.add(newtst); 
            newres.add(optest); 
          } 
        } 
      } 
      res.clear(); 
      res.addAll(newres); 
    } 

    opTests.addAll(flattenTests(allOpTests));
    // System.out.println(">>> Flattened tests = " + opTests);  
        
    return res; 
  }

  private Vector flattenTests(Vector opTests)
  { Vector newOpTests = new Vector(); 
    if (opTests.size() > 0)
    { Vector ptests = (Vector) opTests.get(0); 
      for (int j = 0; j < ptests.size(); j++) 
      { String ptest = (String) ptests.get(j); 
        Vector tailopTests = new Vector(); 
        tailopTests.addAll(opTests); 
        tailopTests.remove(0); 
        Vector tailtests = flattenTests(tailopTests); 
        for (int k = 0; k < tailtests.size(); k++) 
        { Vector tailtest = (Vector) tailtests.get(k);
          Vector newtest = new Vector(); 
          newtest.add(ptest); 
          newtest.addAll(tailtest);
          newOpTests.add(newtest);  
        } 
      } 
      return newOpTests; 
    } 
    else 
    { newOpTests.add(new Vector()); 
      return newOpTests; 
    } 
  } 

  private void identifyUpperBounds(Vector atts, java.util.Map boundConstraints, java.util.Map upperBounds)
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      String aname = att.getName(); 
      Vector bpreds = (Vector) boundConstraints.get(aname); 
      if (bpreds != null) 
      { for (int j = 0; j < bpreds.size(); j++) 
        { BinaryExpression pred = (BinaryExpression) bpreds.get(j); 
          Expression ubound = pred.getUpperBound(aname); 
          if (ubound != null)
          { upperBounds.put(aname,ubound); }
         }
       } 
     }
  } 
  
  private void identifyLowerBounds(Vector atts, java.util.Map boundConstraints, java.util.Map lowerBounds)
  { for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
	  String aname = att.getName(); 
	  Vector bpreds = (Vector) boundConstraints.get(aname); 
	  if (bpreds != null) 
	  { for (int j = 0; j < bpreds.size(); j++) 
	    { BinaryExpression pred = (BinaryExpression) bpreds.get(j); 
	      Expression lbound = pred.getLowerBound(aname); 
		  if (lbound != null)
		  { lowerBounds.put(aname,lbound); }
		}
	  } 
	}
  } 

  public Vector getReadFrame()
  { if (readfr != null) { return readfr; }   // to avoid recursion
    readfr = new Vector(); 
    Vector res = new Vector(); 
    // subtract each params name:
    if (post != null) 
    { res.addAll(post.readFrame()); }
    if (activity != null) 
    { res.addAll(activity.readFrame()); } 
  
    for (int p = 0; p < parameters.size(); p++) 
    { String par = "" + parameters.get(p); 
      res.remove(par); 
    } 
    // System.out.println("Invocation " + this + " READ FRAME= " + res); 
    readfr = res; 
    return res; 
  }         

  public Vector getWriteFrame(Vector assocs)
  { if (writefr != null) { return writefr; }   // to avoid recursion
    writefr = new Vector(); 
    Vector res = new Vector(); 
    if (post != null) 
    { res.addAll(post.wr(assocs)); }  
    if (activity != null) 
    { res.addAll(activity.writeFrame()); } 

    // subtract each params name:
    for (int p = 0; p < parameters.size(); p++) 
    { String par = "" + parameters.get(p); 
      res.remove(par); 
    } 

    // System.out.println("Invocation " + this + " WRITE FRAME= " + res); 
    writefr = res; 
    return res; 
  }         

  public Statemachine getSm()
  { return sm; } 

  public Statement getActivity()
  { return activity; } 

  public boolean isClassScope()
  { return !instanceScope; } 

  public boolean isCached() 
  { if (hasStereotype("cached"))
    { return true; } 
    return false; 
  } 

  public void setCached(boolean b)
  { if (b) 
    { addStereotype("cached"); } 
    else 
    { removeStereotype("cached"); } 
  } 

  public void setInstanceScope(boolean b)
  { if (b)
    { removeStereotype("static"); 
      instanceScope = true; 
    } 
    else 
    { addStereotype("static");   // ??
      instanceScope = false;
    } 
  } 

  public void setStatic(boolean b)
  { if (b)
    { addStereotype("static"); 
      instanceScope = false; 
    } 
    else 
    { removeStereotype("static");   // ??
      instanceScope = true;
    } 
  } 

  public void setQuery(boolean q)
  { query = q; 
    if (query) { addStereotype("query"); } 
    else { removeStereotype("query"); } 
  } 

  public void setVarArgStereotype(Vector pars)
  { if (pars.size() > 0) 
    { Attribute va = (Attribute) pars.get(pars.size()-1); 
      if ("par_varg_sq".equals(va.getName()) && 
          va.isSequence())
      { addStereotype("vararg"); } 
    } 
  } 

  public void setVarArgStereotype()
  { setVarArgStereotype(parameters); } 


  public boolean isVarArg()
  { return hasStereotype("vararg"); } 

  public void setDerived(boolean d) 
  { derived = d; } 

  public boolean isDerived()
  { return derived; } 

  public BehaviouralFeature mergeOperation(Entity e, BehaviouralFeature eop)
  { // Assume that both are instanceScope

    BehaviouralFeature res = null; 
    Type newt = null; 
    boolean isquery = false; 
    Vector newpars = new Vector();
    Expression newpost = null;
    Expression newpre = null; 

    if (resultType == null) 
    { if (eop.resultType == null) {}
      else 
      { newt = eop.resultType; } 
    } 
    else 
    { if (eop.resultType == null) 
      { newt = resultType; }
      else 
      { newt = resultType.mergeType(eop.resultType);
        if (newt == null) 
        { return null; }
      } 
    } 
    // parameters also must match
    if (parameters.size() > eop.parameters.size())
    { newpars = parameters; } 
    else 
    { parameters = eop.parameters; } 
    // take the union of them. 

    if (query && eop.query)
    { isquery = true; }

    if ((pre + "").equals(eop.pre + ""))
    { newpre = pre; }
    else
    { newpre = Expression.simplify("&",pre,eop.pre,null); }

    if ((post + "").equals(eop.post + ""))
    { newpost = post; }
    else
    { newpost = Expression.simplify("or",post,eop.post,null); } 

    res = 
      new BehaviouralFeature(getName(),newpars,isquery,newt); 
    res.setEntity(e); 
    res.setPre(newpre); 
    res.setPost(newpost); 
    return res; 
  } // also set elementType 

  public void setEntity(Entity ent)
  { entity = ent; }

  public Entity getEntity()
  { return entity; } 

  public void setOwner(Entity ent)
  { entity = ent; }

  public Entity getOwner()
  { return entity; } 

  public void setUseCase(UseCase uc)
  { useCase = uc; } 

  public UseCase getUseCase()
  { return useCase; } 

  public String getEntityName()
  { if (entity == null) { return ""; } 
    return entity.getName(); 
  } 

  public void setPre(Expression p)
  { pre = p; }

  public void addPrecondition(Object obj)
  { if (obj != null) 
    { Expression pp = new BasicExpression("\"" + obj + "\""); 
      pre = Expression.simplifyAnd(pre,pp); 
    } 
  } 


  public void setPost(Expression p)
  { post = p; }

  public void addPostcondition(Object obj)
  { if (obj != null) 
    { Expression pp = new BasicExpression("\"" + obj + "\""); 
      post = Expression.simplifyAnd(post,pp); 
    } 
  } 

  public void addActivity(Object obj)
  { Statement newstat = new ImplicitInvocationStatement("\"" + obj + "\"");
 
    if (activity == null) 
    { activity = newstat; } 
    else if (activity instanceof SequenceStatement) 
    { ((SequenceStatement) activity).addStatement(newstat); } 
    else 
    { SequenceStatement res = new SequenceStatement(); 
      res.addStatement(activity); 
      res.addStatement(newstat); 
      activity = res;
    } 
  } 



  public void setPrecondition(Expression p)
  { pre = p; }

  public Expression precondition()
  { return pre; }

  public Expression postcondition()
  { return post; } 
  
  public void setPostcondition(Expression p)
  { post = p; }

  public void setOrdered(boolean ord)
  { ordered = ord; } 

  public void setSorted(boolean ord)
  { sorted = ord; } 

  public void setText(String line) 
  { text = line; } 

  public void clearText()
  { text = ""; } 

  public boolean hasText()
  { return text != null && text.length() > 0; } 

  public void addBPre(Expression e)
  { if (bpre == null) 
    { bpre = e; } 
    else 
    { bpre = Expression.simplifyAnd(bpre,e); } 
  } 

  public void addPost(Expression e)
  { if (post == null) 
    { post = e; } 
    else 
    { post = Expression.simplifyAnd(post,e); } 
  } 

  public boolean isDefinedinSuperclass()
  { if (entity == null) 
    { return false; } 
    Entity sup = entity.getSuperclass(); 
    if (sup == null) 
    { return false; } 
    BehaviouralFeature sop = sup.getDefinedOperation(name); 
    if (sop == null) 
    { return false; } 
    return true; 
  } 
    
  public boolean isQuery()
  { return query; }  // invariants/postconditions for this op should have 
                     // result in succ as only updated feature

  public boolean isUpdate()
  { return query == false; }

  public boolean isGenerator()
  { return hasStereotype("generator"); } 

  public static boolean isStatic0(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.isStatic()) { }  
      else 
      { return false; } 
      if (bf.parameters != null && 
          bf.parameters.size() == 0)
      { return true; } 
      return false; 
    }
    return false; 
  }  

  public static boolean isStaticN(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.isStatic()) { }  
      else 
      { return false; } 
      if (bf.parameters != null && 
          bf.parameters.size() > 0)
      { return true; } 
      return false; 
    }
    return false; 
  }  

  public static boolean isQuery0(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.query) { }  
      else 
      { return false; } 
      if (bf.parameters != null && 
          bf.parameters.size() == 0)
      { return true; } 
      return false; 
    }
    return false; 
  }  

  public static boolean isQueryN(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.query) { }  
      else 
      { return false; } 
      if (bf.parameters != null && 
          bf.parameters.size() > 0)
      { return true; } 
      return false; 
    }
    return false; 
  }  

  public static boolean isUpdate0(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.query) 
      { return false; }  

      if (bf.parameters != null && 
          bf.parameters.size() == 0)
      { return true; }
    } 
    return false; 
  } 

  public static boolean isUpdateN(ModelElement mm)
  { if (mm instanceof BehaviouralFeature)
    { BehaviouralFeature bf = (BehaviouralFeature) mm; 
      if (bf.query) 
      { return false; } 

      if (bf.parameters != null && 
          bf.parameters.size() > 0)
      { return true; }
    }  
    return false; 
  } 

  public void setAbstract(boolean ab)
  { if (ab) 
    { addStereotype("abstract"); } 
    else 
    { removeStereotype("abstract"); } 
  } 

  public boolean isAbstract()
  { return hasStereotype("abstract"); } 

  public boolean isSequential()
  { return hasStereotype("sequential"); } 

  public boolean isStatic()
  { return hasStereotype("static"); } 

  public boolean isUnsafe()
  { return hasStereotype("unsafe"); } 

  public boolean isVirtual(Entity ent) 
  { // if same op is in ent & ent superclass or ent subclass
    if (ent.isInterface() || ent.isAbstract())
    { return true; } 

    if (ent.isActive() && "run".equals(name))
    { return true; } 

    String sig = getSignature(); 
    if (ent != null) 
    { Entity esup = ent.getSuperclass(); 
      if (esup != null) 
      { BehaviouralFeature bf = esup.getOperationBySignature(sig); 
        if (bf != null) 
        { return true; } 
      } 

      for (int j = 0; j < ent.getInterfaces().size(); j++)
      { Entity intf = (Entity) ent.getInterfaces().get(j); 
        BehaviouralFeature bfintf = 
          intf.getOperationBySignature(sig); 
        if (bfintf != null) 
        { return true; } 
      }    

      Vector subs = ent.getSubclasses(); 
      for (int i = 0; i < subs.size(); i++) 
      { Entity sub = (Entity) subs.get(i); 
        BehaviouralFeature bfsub = 
          sub.getOperationBySignature(sig); 
        if (bfsub != null) 
        { return true; } 
      }       
    } 
    return false; 
  } 

  public boolean isFinal()
  { return hasStereotype("final"); } 

  public boolean isOrdered()
  { return ordered; } 

  public boolean isSorted()
  { return sorted; } 

  public boolean isSingleValued()
  { if (resultType != null && !("".equals(resultType)) && 
        !("void".equals(resultType)))
    { if (Type.isCollectionType(resultType))
      { return false; } 
      return true; 
    } 
    return false; 
  } 

  public Vector getParameters()
  { return parameters; }

  public Attribute getParameter(int i) 
  { return (Attribute) parameters.get(i); } 

  public void setParameters(Vector pars)
  { parameters = pars; } 

  public void setParameters(java.util.Map vartypes)
  { parameters = new Vector(); 
    java.util.Set keys = vartypes.keySet(); 
    Vector keyvect = new Vector(); 
    keyvect.addAll(keys); 
    for (int i = 0; i < keyvect.size(); i++) 
    { String key = (String) keyvect.get(i);
      if (vartypes.get(key) instanceof Attribute)
      { parameters.add(vartypes.get(key)); } 
      else if (vartypes.get(key) instanceof Type) 
      { Type vtype = (Type) vartypes.get(key); 
        if (key != null && vtype != null) 
        { Attribute att = new Attribute(key,vtype,ModelElement.INTERNAL); 
          parameters.add(att);
        }  
      } // and avoid duplicates
    } 
  } 

  public void setParameters(java.util.Map vartypes, java.util.Map velemtypes)
  { parameters = new Vector(); 
    java.util.Set keys = vartypes.keySet(); 
    Vector keyvect = new Vector(); 
    keyvect.addAll(keys); 
    for (int i = 0; i < keyvect.size(); i++) 
    { String key = (String) keyvect.get(i); 
      Type vtype = (Type) vartypes.get(key);
      Type vetype = (Type) velemtypes.get(key);  
      if (key != null && vtype != null) 
      { Attribute att = new Attribute(key,vtype,ModelElement.INTERNAL); 
        att.setElementType(vetype); 
        parameters.add(att); 
      } // and avoid duplicates
    } 
  } 

  public Vector parameterEntities()
  { Vector res = new Vector(); 
    if (parameters == null) 
    { return res; } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      Type typ = par.getType(); 
      if (typ != null && typ.isEntity())
      { res.add(typ.getEntity()); } 
    } 

    return res; 
  } 

  public boolean usesOnlyParameterAttributes(Vector attrs)
  { // Each attr : attrs has name starting with a parameter name
    // and not with self. 
    
    for (int i = 0; i < attrs.size(); i++) 
    { Attribute attr = (Attribute) attrs.get(i); 
      String nme = attr.getName();

      if (nme.startsWith("self."))
      { return false; } 
 
      boolean found = false; 
      for (int j = 0; j < parameters.size(); j++) 
      { Attribute par = (Attribute) parameters.get(j); 
        if (nme.startsWith(par.getName() + ".")) 
        { found = true; 
          break; 
        }
      }

      if (!found) 
      { return false; } 
    }

    return true;   
  }

  public void setElementType(Type et)
  { elementType = et; } 
  // and set the elementType of the resultType. 

  public Type getResultType()
  { if (resultType == null) { return null; } 
    return (Type) resultType.clone(); 
  }

  public Type getReturnType()
  { if (resultType == null) 
    { return new Type("void", null); } 
    return (Type) resultType.clone(); 
  }

  public Attribute getResultParameter()
  { if (resultType == null) 
    { return null; }

    if (resultType.getName().equals("void")) 
    { return null; }
 
    Attribute resultVar = 
      new Attribute("result", resultType, ModelElement.INTERNAL); 
    resultVar.setElementType(elementType); 
    return resultVar; 
  }

  public Type getType()
  { if (resultType == null) 
    { return new Type("void",null); } 
    return (Type) resultType.clone(); 
  }

  public Type getElementType()
  { if (elementType == null) { return null; } 
    return (Type) elementType.clone(); 
  } 

  public Expression getPre() 
  { return pre; } 

  public Expression getPost()
  { return post; } 

  public Expression definedness(Vector arguments) 
  { // pre & (pre => def(post))
    Expression res; 
    if (defcond != null) 
    { return substituteParameters(defcond, arguments); } 
    else if (post != null)  
    { if (pre != null) 
      { defcond = (Expression) pre.clone();  
        Expression defpost = post.definedness(); 
        Expression imp = new BinaryExpression("=>", defcond, defpost); 
        imp.setBrackets(true); 
        res = new BinaryExpression("&", defcond, imp);
      } 
      else 
      { defcond = new BasicExpression(true); 
        res = post.definedness();  
      }
    }   
    else 
    { res = new BasicExpression(true); }
    defcond = res; 
 
    // JOptionPane.showMessageDialog(null, "Definedness obligation for " + getName() + " is:\n" + res,
    //   "Internal consistency condition", JOptionPane.INFORMATION_MESSAGE); 
    System.out.println("***> Definedness obligation for " + getName() + " is:\n" + res); 
    return substituteParameters(res, arguments);  
  } 

  public Expression determinate(Vector arguments) 
  { // (pre => det(post))
    if (detcond != null) 
    { return substituteParameters(detcond, arguments); } 
    else 
    { detcond = new BasicExpression(true); 
      Expression imp = null; 
      if (post != null) 
      { Expression detpost = post.determinate(); 
        if (pre != null) 
        { imp = new BinaryExpression("=>", pre, detpost); 
          imp.setBrackets(true);
        } 
        else 
        { imp = detpost; } 
      } 
      else 
      { imp = new BasicExpression(true); } 
 
      // JOptionPane.showMessageDialog(null, "Determinacy obligation for " + getName() + " is:\n" + imp, 
      // "Internal consistency condition", JOptionPane.INFORMATION_MESSAGE); 
      System.out.println("***> Determinacy obligation for " + getName() + " is:\n" + imp); 
      detcond = imp; 
      return substituteParameters(imp, arguments);
    }  
  } 

  public boolean parametersMatch(Vector pars)
  { if (pars == null || parameters == null) 
    { return false; } 

    System.out.println(">>> For operation " + name + " actual parameters are " + pars + " formal parameters are " + parameters); 

    if (pars.size() == parameters.size()) 
    { return true; } 

    return false; 
  } // and check the types

  public boolean parametersSupset(Vector pars)
  { if (pars == null || parameters == null) 
    { return false; } 

    if (pars.size() < parameters.size()) 
    { return true; } 

    return false; 
  } // and check the types

  public boolean parameterMatch(Vector pars)
  { if (pars == null || parameters == null) 
    { return false; } 
    if (pars.size() != parameters.size()) 
    { return false; } 
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      Expression arg = (Expression) pars.get(i); 
      if (par.getType() == null ||
          Type.isVacuousType(par.getType())) { } 
      else if (par.getType().equals(arg.getType())) { } 
      else if (Type.isSubType(arg.getType(), par.getType())) { }  // = or a subtype
      else 
      { return false; } 
    } 

    return false; 
  } // and check the types

  public void setFormalParameters(Vector pars)
  { if (pars == null || parameters == null) 
    { return; }
 
    Vector extrapars = new Vector(); 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      Type pt = par.getType(); 

      if (i < pars.size())
      { Expression arg = (Expression) pars.get(i); 
        arg.formalParameter = par;
      } 
      else 
      { Expression nullInit = 
          Type.nullInitialValueExpression(pt); 
        nullInit.formalParameter = par; 
        extrapars.add(nullInit); 
      } 
    } 

    pars.addAll(extrapars); 
  } // Used in code generation, eg., for Swift. 

  public Expression substituteParameters(Expression e, Vector arguments) 
  { if (e == null) 
    { return new BasicExpression(true); }
    Expression res = (Expression) e.clone(); 
    if (parameters == null) { return res; } 
    for (int i = 0; i < parameters.size(); i++) 
    { if (i < arguments.size()) 
      { Expression arg = (Expression) arguments.get(i); 
        Attribute par = (Attribute) parameters.get(i); 
        res = res.substituteEq(par.getName(), arg); 
      } 
    } 
    return res; 
  } 

  public Expression substituteParametersPre(Vector arguments) 
  { if (pre == null) 
    { return new BasicExpression(true); }
    Expression res = (Expression) pre.clone(); 
    if (parameters == null) { return res; } 
    for (int i = 0; i < parameters.size(); i++) 
    { if (i < arguments.size()) 
      { Expression arg = (Expression) arguments.get(i); 
        Attribute par = (Attribute) parameters.get(i); 
        res = res.substituteEq(par.getName(), arg); 
      } 
    } 
    return res; 
  } 

  public Expression substituteParametersPost(Vector arguments) 
  { if (post == null) 
    { return new BasicExpression(true); } 
    Expression res = (Expression) post.clone(); 
    for (int i = 0; i < parameters.size(); i++) 
    { if (i < arguments.size()) 
      { Expression arg = (Expression) arguments.get(i); 
        Attribute par = (Attribute) parameters.get(i); 
        res = res.substituteEq(par.getName(), arg); 
      } 
    } 
    return res; 
  } 

  public void substituteEq(String oldName, Expression newExpr)
  { if (pre != null) 
    { Expression newpre = pre.substituteEq(oldName, newExpr); 
      this.setPre(newpre); 
    }

    if (post != null) 
    { Expression newpost = 
           post.substituteEq(oldName, newExpr); 
      this.setPost(newpost); 
    } 

    if (activity != null) 
    { Statement newact = 
           activity.substituteEq(oldName, newExpr); 
      this.setActivity(newact); 
    }
  } 

  public void removeEntityParameter(Entity ent)
  { if (parameters == null) 
    { return; } 

    Vector removed = new Vector(); 
    
    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 

      Type typ = par.getType(); 
      if (typ != null && typ.isEntity() && 
          ent == typ.getEntity())
      { removed.add(par);
        BasicExpression selfexpr = 
          new BasicExpression("self"); 
        if (entity != null) 
        { selfexpr.setType(new Type(entity)); }
        selfexpr.setUmlKind(Expression.VARIABLE); 

        this.substituteEq(par.getName(), selfexpr); 
      }
 
      parameters.removeAll(removed); 
      return; 
    } 
  } 

  public void checkParameterNames()
  { if (parameters == null) 
    { return; }
 
    Vector pnames = new Vector(); 

    int UVA = 0; 
    Vector unusedVars = new Vector(); 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      String pname = par.getName(); 

      if (pnames.contains(pname))
      { System.err.println("!! ERROR: duplicated parameter name: " + pname + " in operation " + getName()); } 
      else 
      { pnames.add(pname); } 

      if (Character.isLowerCase(pname.charAt(0))) { } 
      else 
      { System.err.println("! Warning: parameter names should start with a lower case letter: " + pname); } 

      if (Entity.strictEntityName(pname)) { } 
      else 
      { System.err.println("! Warning: parameter names should be alphanumeric: " + pname); } 

      if (activity == null && post != null)
      { Vector puses = post.getUses(pname); 
        if (puses.size() == 0) 
        { System.err.println("!! Code smell (UVA): parameter " + pname + " is unused in operation " + getName() + " postcondition.");
          UVA++; 
          unusedVars.add(pname); 
        } 
      } 

      if (activity != null)
      { Vector actuses = activity.getUses(pname); 
        if (actuses.size() == 0) 
        { System.err.println("!! Code smell (UVA): parameter " + pname + " is unused in operation " + getName() + " activity.");
          UVA++; 
          unusedVars.add(pname); 
        } 
      } 
    } 

    if (UVA > 0) 
    { System.out.println("!!! UVA (parameters) = " + UVA + " for operation " + name); 
      System.out.println("!! Unused parameters: " + unusedVars); 
      System.out.println(); 
    } 
  }


  public void splitIntoSegments()
  { if (activity == null) 
    { return; } 

    String nme = getName(); // not considering generics

    if (activity instanceof SequenceStatement)
    { SequenceStatement code = (SequenceStatement) activity; 
      Vector segments = code.segments();
 
      System.out.println(">>> Segments: " + segments); 

      // A segment is a viable operation if it writes only 
      // attributes or its own local variables (not 
      // parameters). Its parameters are all variables of 
      // this which are read in the segment.

      // Create new operation bf and set 
      // residue to self.bf(...)
      // Otherwise, add segment code to unprocessed and 
      // try again with the preceding segment + unprocessed 

      Vector residue = new Vector(); 
      Vector unprocessed = new Vector(); 

      for (int i = segments.size() - 1; i >= 1; i--) 
      { Vector seg = (Vector) segments.get(i);
        Vector completeseg = new Vector(); 
        completeseg.addAll(seg); 
        completeseg.addAll(unprocessed);  

        SequenceStatement sseg = 
              new SequenceStatement(completeseg); 
        Vector wrs = sseg.writeFrame(); 
        // Vector rds = sseg.readFrame();

        Vector unused = new Vector(); 
        Vector actuses = sseg.getVariableUses(unused);
        actuses = ModelElement.removeExpressionByName("skip", actuses); 

        System.out.println(">%%> Variables used in " + completeseg + " are: " + actuses); 
        System.out.println(">%%> Variables written in " + completeseg + " are: " + wrs); 
        System.out.println(">%%> Declared and unused variables of " + completeseg + " are " + unused);  

        // if wrs disjoint from actuses it is ok. Parameters
        // are actuses. 

        Vector rems = 
          ModelElement.removeExpressionsByName(wrs, actuses);
 
        if (rems.size() == actuses.size())
        { System.out.println(">%%> Valid segment. Creating new operation for it."); 
          unprocessed = new Vector(); 

          Vector pars = new Vector(); 
          Vector pnames = new Vector(); 
          for (int j = 0; j < actuses.size(); j++)
          { Expression use = (Expression) actuses.get(j);
            if (pnames.contains(use + "")) { } 
            else 
            { Attribute att = new Attribute(use); 
              pars.add(att);
              pnames.add(use + ""); 
            }  
          } 
 
          String newopname = Identifier.nextIdentifier(nme + "_segment_"); 
          BehaviouralFeature bf = 
            new BehaviouralFeature(
              newopname, pars, query, resultType); 
          bf.setEntity(entity); 
          bf.setPost(new BasicExpression(true));
          bf.setComments("Derived from " + nme + " by Split operation refactoring"); 

          if (residue.size() > 0) 
          { sseg.addStatements(residue); } 
          residue = new Vector();  
          bf.setActivity(sseg); 
          entity.addOperation(bf); 
          if (resultType == null || 
              "void".equals(resultType + ""))
          { 
            InvocationStatement callbf = 
                new InvocationStatement("self", bf); 
            residue.add(callbf);
          } 
          else 
          { BasicExpression selfbe = 
              new BasicExpression(entity, "self"); 
            BasicExpression val = 
              BasicExpression.newCallBasicExpression(
                                         bf,selfbe,pars); 
            ReturnStatement ret = 
                new ReturnStatement(val); 
            residue.add(ret); 
          }  
        }
        else 
        { System.out.println(">%%> Invalid segment. Cannot split operation."); 
          Vector oldunprocessed = new Vector(); 
          oldunprocessed.addAll(completeseg); 
          unprocessed = oldunprocessed; 
          // residue = new Vector();  
        }
      } 

      Vector seg = (Vector) segments.get(0); 
      if (unprocessed.size() == 0 && residue.size() > 0) 
      { seg.addAll(residue);
        setActivity(new SequenceStatement(seg)); 
      }
      else if (residue.size() > 0)
      { seg.addAll(unprocessed); 
        seg.addAll(residue); 
        setActivity(new SequenceStatement(seg)); 
      }
    } 
  } 

  public static Vector allAttributesUsedIn(Entity ent, Vector ops)
  { Vector res = new Vector(); 
    
    for (int i = 0; i < ops.size(); i++) 
    { String op = (String) ops.get(i); 
      BehaviouralFeature bf = ent.getOperation(op); 
      if (bf != null) 
      { Vector attrs = bf.allAttributesUsedIn(); 
        res = VectorUtil.union(res,attrs); 
      } 
    } 
    return res; 
  } 

  public Vector allAttributesUsedIn()
  { Vector res = new Vector(); 
    if (activity != null) 
    { res = activity.allAttributesUsedIn(); } 
    else if (post != null) 
    { res = post.allAttributesUsedIn(); } 
    return res; 
  } 

  public boolean checkVariableUse()
  { Vector unused = new Vector(); 
  
    if (activity != null)
    { Vector actuses = activity.getVariableUses(unused);
      actuses = ModelElement.removeExpressionByName("skip", actuses); 
 
      System.out.println(">>> Parameters or non-local variables " + actuses + " are used in " + getName() + " activity."); 

      Vector attrs = activity.allAttributesUsedIn(); 

      System.out.println(">>> Attributes " + attrs + " are used in " + getName() + " activity."); 
      System.out.println(); 
 
      if (unused.size() > 0) 
      { System.out.println("!! Parameters or non-local variables " + unused + " are declared but not used in " + getName() + " activity."); 
        System.out.println("!!! UVA (local variables) = " + unused.size() + " for operation " + name); 
        return true; 
      } 

      System.out.println(); 
    } 

    // Should be subset of parameters + visible data features 
    // getDeclaredVariables() -- any extra in here are UVA. 
    return false; 
  }



  private void typeCheckParameters(Vector pars, Vector types, Vector entities)
  { for (int i = 0; i < pars.size(); i++) 
    { Attribute par = (Attribute) pars.get(i); 
      Type partype = par.getType(); 
      Type newtype = Type.typeCheck(partype,types,entities);   
      par.setType(newtype); 
      System.out.println(">> parameter " + par.getName() + 
                         " has type " + newtype); 
    } 
  } 

  private void typeInferenceParameters(Vector pars, Vector types, Vector entities)
  { for (int i = 0; i < pars.size(); i++) 
    { Attribute par = (Attribute) pars.get(i); 
      Type partype = par.getType();
      if (partype != null && 
          partype.isAliasType())
      { partype = partype.getActualType(); }  
      Type newtype = Type.typeCheck(partype,types,entities);   
      par.setType(newtype); 
      par.setElementType(newtype.getElementType()); 
      System.out.println(">> parameter " + par.getName() + 
                         " has type " + newtype); 
    } 
  } 

  public Vector allTypeParameterEntities(Entity ent)
  { Vector localEntities = new Vector();
 
    if (typeParameters != null) 
    { for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        if (tp.isEntity())
        { localEntities.add(tp.getEntity()); } 
      } 
    } 

    if (ent != null) 
    { Vector etpars = ent.getTypeParameters(); 
      if (etpars != null)
      { for (int i = 0; i < etpars.size(); i++) 
        { Type tp = (Type) etpars.get(i); 
          if (tp.isEntity())
          { localEntities.add(tp.getEntity()); } 
        }
      } 
    } 
    
    return localEntities; 
  } 

  public Vector allTypeParameterEntities()
  { Vector localEntities = allTypeParameterEntities(entity);     

    System.out.println(">>> All local entities for " + name + " are " + localEntities); 

    return localEntities; 
  } 

  public boolean typeCheck(Vector types, Vector entities)
  { // System.err.println("ERROR -- calling wrong type-check for " + name); 

    Vector localEntities = allTypeParameterEntities(); 
     
    localEntities.addAll(entities); 

    Vector contexts = new Vector(); 
    if (entity != null && instanceScope) 
    { contexts.add(entity); }
    else if (entity != null && !instanceScope)
    { localEntities.add(0,entity); } 
 
    Vector env = new Vector();
    typeCheckParameters(parameters,types,localEntities);  
    env.addAll(parameters);

    if (resultType != null && !("void".equals(resultType + "")))
    { Attribute resultVar = getResultParameter(); 
      if (resultVar != null) 
      { env.add(resultVar); }  
    } 
 
    if (pre != null) 
    { pre.typeCheck(types,localEntities,contexts,env); } 

    boolean res = false; 

    if (post != null) 
    { res = post.typeCheck(
              types,localEntities,contexts,env); 
    }
 
    if (activity != null) 
    { System.out.println(">>> Type-checking activity " + activity); 
      res = activity.typeCheck(
              types,localEntities,contexts,env);
    } 

    // System.out.println(">>> Parameters = " + parameters); 

    return res;  
  } // and the activity? 
  // could deduce type and element type of result. 

  public boolean typeInference(Vector types, Vector entities, 
                               java.util.Map vartypes)
  { java.util.Map localvartypes = new java.util.HashMap(); 
    localvartypes.putAll(vartypes); 

    Vector localEntities = allTypeParameterEntities(); 
     
    localEntities.addAll(entities); 

    Vector contexts = new Vector(); 
    if (entity != null && instanceScope) 
    { contexts.add(entity); }
    else if (entity != null && !instanceScope)
    { localEntities.add(0,entity); } 
 
    Vector env = new Vector();
    typeInferenceParameters(parameters,types,localEntities);  
    env.addAll(parameters);

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      localvartypes.put(par.getName(), par.getType()); 
    } 

    if (resultType != null && !("void".equals(resultType + "")))
    { Attribute resultVar = getResultParameter(); 
      if (resultVar != null) 
      { env.add(resultVar); 
        localvartypes.put("result", resultType); 
      }  
    } 
 
    if (pre != null) 
    { pre.typeCheck(types,localEntities,contexts,env); } 

    boolean res = false; 

    if (post != null) 
    { res = post.typeCheck(
              types,localEntities,contexts,env); 
    }
 
    if (activity != null) 
    { System.out.println(">>> Type inference for activity " + activity); 
      res = activity.typeInference(
              types,localEntities,contexts,env,localvartypes);
    } 

    System.out.println(">>> Typed variables = " + 
                       localvartypes); 

    Type rtype = (Type) localvartypes.get("result"); 
    if ((resultType == null || 
         "OclAny".equals(resultType + "")) && 
        rtype != null) 
    { resultType = rtype; } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i); 
      Type newpartype = 
         (Type) localvartypes.get(par.getName());
      if (Type.isVacuousType(par.getType()) && 
          newpartype != null)
      { par.setType(newpartype); } 
    } 

    return res;  
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { Vector contexts1 = new Vector(); 
    if (entity != null && instanceScope) 
    { if (contexts.contains(entity)) { } 
      else 
      { contexts1.add(entity); }
    } 
    contexts1.addAll(contexts); 
 

    // System.err.println("Type-check for " + name + " WITH " + env1 + " " + contexts1); 

    Vector localEntities = allTypeParameterEntities();
     
    localEntities.addAll(entities); 
    if (entity != null && !instanceScope)
    { localEntities.add(0,entity); } 
    // The static context. 

    typeCheckParameters(parameters,types,localEntities);  

    Vector env1 = new Vector(); 
    env1.addAll(parameters);
    env1.addAll(env); 
 
    if (pre != null) 
    { pre.typeCheck(types,localEntities,contexts1,env1); } 

    boolean res = false; 

    if (post != null) 
    { res = post.typeCheck(
              types,localEntities,contexts1,env1); 
    }
 
    if (activity != null) 
    { res = activity.typeCheck(
              types,localEntities,contexts1,env1); 
    }
 
    return res;  
  } 

  public static Vector reconstructParameters(
                         String pars, Vector types, Vector entities)
  { return reconstructParameters(null,
                                 pars," ,:",types,entities); 
  } 

  public static Vector reconstructParameters(Entity ent, 
                         String pars, Vector types, Vector entities)
  { return reconstructParameters(ent,
                                 pars," ,:",types,entities); 
  } 

  public static Vector reconstructParameters(Entity ent, 
                          String pars, String seps, 
                          Vector types, Vector entities)
  { Vector parvals = new Vector(); 
    Vector res = new Vector(); 
    StringTokenizer st = new StringTokenizer(pars,seps); 
    while (st.hasMoreTokens())
    { parvals.add(st.nextToken()); } 

    Vector localEntities = new Vector(); 
    if (ent != null) 
    { localEntities.addAll(ent.typeParameterEntities()); } 
    localEntities.addAll(entities); 
    
    for (int i = 0; i < parvals.size() - 1; i = i + 2) 
    { String var = (String) parvals.get(i); 
      String typ = (String) parvals.get(i+1);     // could be Set(T), etc

      Type elemType = null; 

      if (typ != null) 
      { int bind = typ.indexOf("("); 
        if (bind > 0)  // a set, sequence or map type 
        { int cind = typ.indexOf(","); 
          if (cind > 0) 
          { String xparam = typ.substring(cind+1, typ.length() - 1); 
            elemType = Type.getTypeFor(xparam,types,localEntities); 
          } 
          else 
          { String tparam = typ.substring(bind+1,typ.length() - 1); 
            elemType = Type.getTypeFor(tparam,types,localEntities);
          }            
        } 
      }      

      Type t = Type.getTypeFor(typ,types,localEntities);  

      if (t == null) // must be standard type or an entity
      { System.err.println("!! ERROR: Invalid type name: " + typ);
       JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + typ,
                                     "Type error", JOptionPane.ERROR_MESSAGE); 
      }
      else 
      { Attribute att = new Attribute(var,t,ModelElement.INTERNAL); 
        att.setElementType(elemType); 
        t.setElementType(elemType); 
        res.add(att); 
        System.out.println(">>> Parameter " + var + " type " + t); 
      } 
    }
    return res; 
  }

  public String getParList()
  { String res = ""; 

    if (parameters == null) 
    { return res; } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getName() + " " + att.getType() + " "; 
    }
    return res; 
  }   

  public String getSignature()
  { String res = getName(); 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
 
    res = res + genPars + "(";

    if (parameters == null) 
    { return res + ")"; } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getName() + ": " + att.getType(); 
      if (i < parameters.size() - 1) 
      { res = res + ","; } 
    }
    return res + ")"; 
  }   

  public String getTypeSignature()
  { String res = getName(); 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
 
    res = res + genPars + "(";

    if (parameters == null) 
    { return res + ")"; } 

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute att = (Attribute) parameters.get(i); 
      res = res + att.getType(); 
      if (i < parameters.size() - 1) 
      { res = res + ","; } 
    }
    return res + ")"; 
  }   
    
  public Vector getParameterNames()
  { Vector res = new Vector();
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i);
      res.add(att.getName());
    }
    return res;
  }

  public Vector getParameterExpressions()
  { Vector res = new Vector();
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i);
      Expression expr = new BasicExpression(att);
      expr.formalParameter = att; 
      res.add(expr); 
    }
    return res;
  }

  public String getJavaParameterDec()
  { String res = ""; 
    // System.out.println("PARAMETERS = " + parameters); 

    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("!! ERROR: null type for parameter " + att); 
        JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + att,
                                      "Type error", JOptionPane.ERROR_MESSAGE); 
        typ = new Type("void",null); 
      } 
      res = res + typ.getJava() + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getJava6ParameterDec()
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("!! ERROR: null type for parameter " + att); 
        JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + att,
                                      "Type error", JOptionPane.ERROR_MESSAGE); 
        typ = new Type("void",null); 
      } 
      res = res + typ.getJava6() + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getJava7ParameterDec()
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("!! ERROR: null type for parameter " + att); 
        // JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + att,  
        // "Type error", JOptionPane.ERROR_MESSAGE); 
        typ = new Type("void",null); 
      } 
      res = res + typ.getJava7(att.getElementType()) + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  public String getCSharpParameterDec()  
  { String res = ""; 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i); 
      String attnme = att.getName(); 
      Type typ = att.getType(); 
      if (typ == null) 
      { System.err.println("!! ERROR: null type for parameter " + att); 
        JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + att,
                                      "Type error", JOptionPane.ERROR_MESSAGE); 
        typ = new Type("void",null); 
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
      { System.err.println("!! ERROR: null type for parameter " + att); 
        JOptionPane.showMessageDialog(null, "ERROR: Invalid type for parameter: " + att,
                                      "Type error", JOptionPane.ERROR_MESSAGE); 
        typ = new Type("void",null); 
      } 
      res = res + typ.getCPP(att.getElementType()) + " " + attnme; 
      if (i < parameters.size() - 1)
      { res = res + ","; } 
    }
    return res; 
  }      

  private String parcats()
  { // Assumes all parameters are primitive/strings
    String res = "";
    if (parameters.size() == 1)
    { Attribute p = (Attribute) parameters.get(0);
      return Expression.wrap(p.getType(), p.getName());
    }
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String par1 = par.getName(); 
      if (i < parameters.size() - 1) 
      { res = res + par1 + " + \", \" + "; } 
      else 
      { res = res + par1; } 
    }
    return res;
  } // works for Java

  private String parcatsJava6()
  { // Assumes all parameters are primitive/strings
    String res = "";
    if (parameters.size() == 1)
    { Attribute p = (Attribute) parameters.get(0);
      return Expression.wrapJava6(p.getType(), p.getName());
    }
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String par1 = par.getName(); 
      if (i < parameters.size() - 1) 
      { res = res + par1 + " + \", \" + "; } 
      else 
      { res = res + par1; } 
    }
    return res;
  } // works for Java6

  private String parcatsCSharp()
  { // Assumes all parameters are primitive/strings
    String res = "";
    if (parameters.size() >= 1)
    { Attribute p = (Attribute) parameters.get(0);
      res = res + p.getName();
    }
    for (int i = 1; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String par1 = par.getName(); 
      res = res + " + \", \" + " + par1;
    }
    return res;
  } 

  public BExpression getBParameterDec()
  { BExpression res = null;
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i);
      BExpression attbe = new BBasicExpression(att.getName()); 
      BExpression typebe = 
        new BBasicExpression(att.getType().generateB(att.getElementType())); 
      BExpression conj = new BBinaryExpression(":",attbe,typebe); 
      if (res == null) 
      { res = conj; } 
      else 
      { res = new BBinaryExpression("&",res,conj); } 
    }
    return res;
  }

  public BExpression getBCall()
  { // call of query op, assumed instance scope

    String opname = getName();
    if (entity == null) 
    { opname = opname + "_"; } 
    else 
    { opname = opname + "_" + entity.getName(); }
    String res = opname; 
 
    String ex = entity.getName().toLowerCase() + "x"; 
    Vector epars = new Vector(); 
    epars.add(new BBasicExpression(ex)); 

    if (parameters.size() == 0)
    { return new BBasicExpression(opname,epars); } 

    Vector pars = new Vector(); 
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getName();
      pars.add(new BBasicExpression(dec)); 
    } 
    return new BApplyExpression(new BBasicExpression(opname,epars),pars); 
  }      

  public BExpression getBFunctionDec()
  { // opname_E : es --> (parT --> resultT)
    BExpression res = null;
    for (int i = 0; i < parameters.size(); i++)
    { Attribute att = (Attribute) parameters.get(i);
      BExpression typebe = 
        new BBasicExpression(att.getType().generateB(att.getElementType())); 
      if (res == null) 
      { res = typebe; } 
      else 
      { res = new BBinaryExpression("*",res,typebe); } 
    } // what if empty? 
    
    BExpression ftype; 
    if (resultType != null)  // for Set/Sequence need elementType
    { String rT = resultType.generateB(elementType); 
      if (res == null) 
      { ftype = new BBasicExpression(rT); } 
      else 
      { ftype = new BBinaryExpression("-->",res,
                      new BBasicExpression(rT));
        ftype.setBrackets(true); 
      } 
    }
    else 
    { System.err.println("ERROR: Undefined return type of operation " + this); 
      ftype = null; 
      return null; 
    } 

    String opname = getName();
    if (entity == null)   // also if static
    { opname = opname + "_";
      return new BBinaryExpression(":",new BBasicExpression(opname),ftype);
    } 
    else 
    { opname = opname + "_" + entity.getName(); 
      String ename = entity.getName(); 
      String es = ename.toLowerCase() + "s"; 
      BExpression esbe = new BBasicExpression(es); 
      return new BBinaryExpression(":",new BBasicExpression(opname),
                   new BBinaryExpression("-->",esbe,ftype)); 
    }
  }

  private String parameterList()
  { String res = "";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }
    return res;
  }

  
  public String toString()
  { String res = getName(); 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 

    res = res + genPars + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }
    res = res + ")";
    return res;
  }

  public String display()   // what about guards?
  { String res = ""; 
    if (isSequential())
    { res = res + "synchronized "; } 
    if (isAbstract())
    { res = res + "abstract "; } 
    if (isFinal())
    { res = res + "final "; } 
    if (instanceScope) { } 
    else 
    { res = res + "static "; } 
    if (isQuery())
    { res = res + "query ";  } 

    res = res + getSignature(); 

    if (resultType != null)
    { res = res + ": " + resultType; } 
    // if ("Set".equals(resultType + "") || "Sequence".equals(resultType + ""))
    // { res = res + "(" + elementType + ")"; } 
    res = res + "\n"; 
    if (pre != null)
    { res = res + "pre: " + pre + "\n"; } 
    if (post != null) 
    { res = res + "post: " + post + "\n"; }
    if (activity != null)
    { res = res + "activity: " + activity + "\n"; }
    if (hasText())
    { res = res + "text:\n"; 
      res = res + text + "\n"; 
    }     
    return res; 
  } 

  public String displayForUC()   // what about guards?
  { String res = ""; 

    res = res + getSignature(); 

    if (resultType != null)
    { res = res + " : " + resultType; } 
    res = res + "\n\r"; 
    if (pre != null)
    { res = res + "pre: " + pre + "\n\r"; }
    else 
    { res = res + "pre: true\n\r"; }
    
    if (post != null) 
    { res = res + "post: " + post + "\n\r"; }
    else 
    { res = res + "post: true\n\r"; }
    return res; 
  } 

  public Vector equivalentsUsedIn() 
  { Vector res = new Vector(); 
    if (pre != null) 
    { res.addAll(pre.equivalentsUsedIn()); } 
    if (post != null) 
    { res.addAll(post.equivalentsUsedIn()); } 
    if (activity != null) 
    { res.addAll(activity.equivalentsUsedIn()); } 
    return res; 
  } 

  public Map energyAnalysis(Vector redUses, Vector amberUses)
  { // Scan the postcondition/activity for energy expensive
    // expressions/code

    Map res = new Map(); 
    res.set("red", 0); 
    res.set("amber", 0); 

    java.util.Map clones = new java.util.HashMap(); 
    java.util.Map defs = new java.util.HashMap(); 

    if (post != null) 
    { res = post.energyUse(res, redUses, amberUses);

      // System.out.println(res); 
      // System.out.println(redUses); 
      // System.out.println(amberUses); 
 
      post.findClones(clones, defs, null, name); 
      if (clones.size() > 0)
      { java.util.Set actualClones = new java.util.HashSet(); 

        for (Object key : clones.keySet())
        { java.util.List occs = 
            (java.util.List) clones.get(key); 
          if (occs.size() > 1)
          { actualClones.add(key); } 
        } 

        if (actualClones.size() > 0)
        { redUses.add("!!! Cloned expressions could be repeated evaluations: " + actualClones + "\n" + 
             ">>> Use Extract Local Variable refactoring"); 
          int rscore = (int) res.get("red");
          rscore = rscore + actualClones.size();
          res.set("red", rscore);
        } 
      } // In a postcondition they usually are repeats.
    } 

    clones = new java.util.HashMap(); 
    defs = new java.util.HashMap();

    if (activity != null) 
    { res = activity.energyUse(res, redUses, amberUses);

      // System.out.println(res); 
      // System.out.println(redUses); 
      // System.out.println(amberUses); 
 
      activity.findClones(clones, defs, null, name); 
      if (clones.size() > 0)
      { java.util.Set actualClones = new java.util.HashSet(); 

        for (Object key : clones.keySet())
        { java.util.List occs = 
            (java.util.List) clones.get(key); 
          if (occs.size() > 1)
          { actualClones.add(key); } 
        } 

        if (actualClones.size() > 0)
        { amberUses.add("!!! Cloned expressions could be repeated evaluations: " + actualClones + "\n" + 
             ">>> Use Extract Local Variable refactoring"); 
          int ascore = (int) res.get("amber");
          ascore = ascore + actualClones.size();
          res.set("amber", ascore);
        } 
      } // In a postcondition they usually are repeats.
    } 

    return res; 
  } 


  public int displayMeasures(PrintWriter out)   
  { String res = ""; 
    String nme = getName(); 
    if (entity != null) 
    { nme = entity.getName() + "::" + nme; } 
    else if (useCase != null) 
    { nme = useCase.getName() + "::" + nme; } 

    int pars = parameters.size(); 

    if (resultType != null)
    { pars++; } 

    out.println("*** Number of parameters of operation " + nme + " = " + pars); 
    if (pars > TestParameters.numberOfParametersLimit) 
    { System.err.println("!!! Code smell (EPL): too many parameters (" + pars + ") for " + nme); 
      System.err.println(">>> Recommend refactoring by introducing value object for parameters or splitting operation into parts"); 
    }  

    int complexity = 0; 
    int cyc = 0; 

    if (pre != null)
    { complexity = complexity + pre.syntacticComplexity(); } 
    if (post != null) 
    { complexity = complexity + post.syntacticComplexity();
      cyc = post.cyclomaticComplexity(); 
    }

    out.println("*** Postcondition cyclomatic complexity = " + cyc); 
    
    if (activity != null)
    { cyc = activity.cyclomaticComplexity(); 
      out.println("*** Activity cyclomatic complexity = " + cyc); 

      int acomp = activity.syntacticComplexity(); 
      out.println("*** Activity syntactic complexity = " + acomp); 
      if (acomp > TestParameters.operationSizeLimit) 
      { System.err.println("!!! Code smell (EOS): too high activity complexity (" + acomp + ") for " + nme); 
        System.err.println(">>> Recommend refactoring by splitting operation"); 
      }  
      else if (acomp > TestParameters.operationSizeWarning) 
      { System.err.println("*** Warning: high activity complexity (" + acomp + ") for " + nme); }  
      complexity = complexity + acomp; 
    }
    
    out.println("*** Total complexity of operation " + nme + " = " + complexity); 
    out.println(); 
    if (cyc > TestParameters.cyclomaticComplexityLimit) 
    { System.err.println("!!! Code smell (CC): high cyclomatic complexity (" + cyc + ") for " + nme);
      System.err.println(">>> Recommend refactoring by splitting operation"); 
    }  

    if (complexity > TestParameters.operationSizeLimit) 
    { System.err.println("!!! Code smell (EHS): too high complexity (" + complexity + ") for " + nme); 
      System.err.println(">>> Recommend refactoring by splitting operation"); 
    }  
    else if (complexity > TestParameters.operationSizeWarning) 
    { System.err.println("*** Warning: high complexity (" + complexity + ") for " + nme); }  


    return complexity; 
  } 

  public java.util.Map dataDependencies()
  { java.util.Map res = new java.util.HashMap(); 

    if (activity != null && 
        entity != null)
    { String nme = getName(); 
      Vector allvars = new Vector();
      for (int i = 0; i < parameters.size(); i++) 
      { Attribute parx = (Attribute) parameters.get(i); 
        allvars.add(parx.getName()); 
      }  

      Vector attrs = entity.getAttributes(); 
      for (int i = 0; i < attrs.size(); i++) 
      { Attribute attx = (Attribute) attrs.get(i); 
        allvars.add(attx.getName()); 
      }  

      Vector opvars = activity.getVariableUses(); 

      Map localdeps = new Map();
      Map dataLineages = new Map();  

      Vector postvars = new Vector(); 
      postvars.add("result"); 
      Vector deps = activity.dataDependents(
                       allvars,postvars);
      System.out.println();  
      System.out.println(">***> result is derived from:\n  " + deps);

      // res.put("result", deps); 

 
      for (int i = 0; i < attrs.size(); i++) 
      { Attribute attx = (Attribute) attrs.get(i); 
        String aname = attx.getName(); 
        Vector avs = new Vector(); 
        avs.add(aname); 
        Vector adeps = 
            activity.dataDependents(allvars, avs, localdeps, 
                                    dataLineages);
        System.out.println(">***> " + aname + " is derived from " + adeps);
        res.put(aname, adeps); 
        /* for (int j = 0; j < adeps.size(); j++)
        { String adepsource = "" + adeps.get(j); 
          if (aname.equals(adepsource)) { } 
          else 
          { localdeps.add_pair(adepsource, aname); } 
        } */ 
      }  

      Vector seenvars = new Vector();
      seenvars.add("skip"); 
 
      for (int i = 0; i < opvars.size(); i++) 
      { String vname = "" + opvars.get(i);
        if (seenvars.contains(vname)) 
        { continue; }  
        Vector vvs = new Vector(); 
        vvs.add(vname); 
        Vector vdeps = 
            activity.dataDependents(allvars, vvs, localdeps, 
                                    dataLineages);
        System.out.println(">***> " + vname + " is derived from " + vdeps);
        System.out.println();  
        seenvars.add(vname); 
        /* for (int j = 0; j < vdeps.size(); j++)
        { String vdepsource = "" + vdeps.get(j); 
          if (vname.equals(vdepsource)) { } 
          else 
          { localdeps.add_pair(vdepsource, vname); } 
        } */ 
      }  

      System.out.println();
      System.out.println(">***> Data dependencies for " + nme + " are:\n" + localdeps);

      System.out.println();
      System.out.println(">***> Data lineages for " + nme + " are:\n" + dataLineages);

      System.out.println();   
    } 

    return res; 
  } 

  public int syntacticComplexity()   
  { 
    int complexity = 0; 
    
    if (pre != null)
    { complexity = complexity + pre.syntacticComplexity(); } 
    if (post != null) 
    { complexity = complexity + post.syntacticComplexity(); }
    
    if (activity != null)
    { complexity = complexity + activity.syntacticComplexity(); }
    
    return complexity; 
  } 

  public int cyclomaticComplexity()   
  { String res = ""; 
    String nme = getName(); 
    if (entity != null) 
    { nme = entity.getName() + "::" + nme; } 
    else if (useCase != null) 
    { nme = useCase.getName() + "::" + nme; } 

    int pars = parameters.size(); 

    if (resultType != null)
    { pars++; } 

    System.out.println("*** Number of parameters of operation " + nme + " = " + pars); 
    if (pars > 10) 
    { System.err.println("!!! Code smell (EPL): too many parameters (" + pars + ") for " + nme); }  

    int complexity = 0; 
    int cyc = 0; 

    if (pre != null)
    { complexity = complexity + pre.syntacticComplexity(); } 
    if (post != null) 
    { complexity = complexity + post.syntacticComplexity();
      cyc = post.cyclomaticComplexity(); 
    }

    System.out.println("*** Postcondition cyclomatic complexity = " + cyc); 
    
    if (activity != null)
    { cyc = activity.cyclomaticComplexity(); 
      complexity = complexity + cyc; 
      System.out.println("*** Activity cyclomatic complexity = " + cyc); 
    }
    
    System.out.println("*** Total complexity of operation " + nme + " = " + complexity); 
    System.out.println(); 
    if (cyc > 10) 
    { System.err.println("!!! Code smell (CC): high cyclomatic complexity (" + cyc + ") for " + nme); }  
    if (complexity > 100) 
    { System.err.println("!!! Code smell (EHS): too high complexity (" + complexity + ") for " + nme); }  

    return cyc; 
  } 

  public int epl() 
  { return parameters.size(); }  // plus variables introduced in post or activity. 

  public int cc()   
  { int res = 0; 

    if (post != null) 
    { res = post.cyclomaticComplexity(); }

    if (activity != null)
    { res = res + activity.cyclomaticComplexity(); }
    
    return res; 
  } 

  public void findClones(java.util.Map clones)
  { String nme = getName(); 
    if (entity != null) 
    { nme = entity.getName() + "::" + nme; } 
    else if (useCase != null) 
    { nme = useCase.getName() + "::" + nme; } 

    if (pre != null) 
    { pre.findClones(clones,null, nme); } 
    if (post != null) 
    { post.findClones(clones,null, nme); } 
    if (activity != null) 
    { activity.findClones(clones, null, nme); } 
  } 

  public void findClones(java.util.Map clones, 
                         java.util.Map cloneDefinitions)
  { String nme = getName(); 
    if (entity != null) 
    { nme = entity.getName() + "::" + nme; } 
    else if (useCase != null) 
    { nme = useCase.getName() + "::" + nme; } 

    if (pre != null) 
    { pre.findClones(clones, cloneDefinitions, null, nme); } 
    if (post != null) 
    { post.findClones(clones, cloneDefinitions, null, nme); } 
    if (activity != null) 
    { activity.findClones(clones, cloneDefinitions, 
                          null, nme); 
    } 
  } 

  public void findMagicNumbers(java.util.Map mgns)
  { String nme = getName(); 
    if (entity != null) 
    { nme = entity.getName() + "::" + nme; } 
    else if (useCase != null) 
    { nme = useCase.getName() + "::" + nme; } 

    if (pre != null) 
    { pre.findMagicNumbers(mgns, nme, nme); } 
    if (post != null) 
    { post.findMagicNumbers(mgns, nme, nme); } 
    if (activity != null) 
    { activity.findMagicNumbers(mgns, null, nme); } 
  } 

  public void generateMamba(PrintWriter out, CGSpec cgs)
  { String nme = getName();

    String rt = "null"; 
    if (resultType != null)
    { rt = resultType.getCSharp(); } 

    String fdef = this.cg(cgs); 
    String actualDef = CGSpec.replaceActualNewlines(fdef); 
 
    out.println("        {"); 
    out.println("          \"Name\": \"" + nme + "\","); 
    out.println("          \"Description\": \"\",");
    out.println("          \"Syntax\": \"\",");
    out.println("          \"Text\": \"" + actualDef + "\",");     
    out.println("          \"BaseInfo\": \"\",");
    out.println("          \"IsStatic\": " + isStatic() + ",");
    out.println("          \"IsInherited\": false,");
    out.println("          \"IsExternal\": false,");
    out.println("          \"ReturnDataType\": \"" + rt + "\",");
    out.println("          \"Parameters\": [],");
    out.println("          \"RuleType\": 0,");
    out.println("          \"EventName\": \"\",");
    out.println("          \"ApplyToAttribute\": \"\"");
    out.print("        }"); 
  } 

  public void generateJava(PrintWriter out)
  { out.print(toString()); } 

  public void generateCSharp(PrintWriter out)
  { out.print(toString()); } 

  public String genQueryCode(Entity ent, Vector cons)
  { String res = " public "; 
    if (isSequential())
    { res = res + "synchronized "; } 
    if (isAbstract())
    { res = res + "abstract "; } // so comment out code
    if (isClassScope() || isStatic())
    { res = res + "static "; } 

    if (resultType == null || "void".equals(resultType + "")) 
    { JOptionPane.showMessageDialog(null, 
        "ERROR: null result type for query operation: " + this,        
        "Semantic error", JOptionPane.ERROR_MESSAGE);
      return ""; 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
 
    res = res + genPars + resultType.getJava() +
                 " " + getName() + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getType().getJava() +
                   " " + par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }
	
    if (isAbstract())
    { res = res + ");\n\n"; 
      return res; 
    } 
    
    res = res + ")\n";
    res = res + "  { " + resultType.getJava() + " " +
          " result = " + resultType.getDefault() +
          ";\n";
    
    for (int j = 0; j < cons.size(); j++)
    { Constraint con = (Constraint) cons.get(j);
      if (con.getEvent() != null &&
          con.getEvent().equals(this))
      res = res + "    " +
          con.queryOperation(ent) + "\n";
    }
    
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String genQueryCodeJava6(Entity ent, Vector cons)
  { String res = " public "; 
    if (isSequential())
    { res = res + "synchronized "; } 
    if (isAbstract())
    { res = res + "abstract "; } // so comment out code
    if (isClassScope() || isStatic())
    { res = res + "static "; } 

    if (resultType == null || "void".equals(resultType + "")) 
    { JOptionPane.showMessageDialog(null, "ERROR: null result type for: " + this,
                                    "Type error", JOptionPane.ERROR_MESSAGE);
      return ""; 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava6(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
 

    res = res + genPars + resultType.getJava6() +
                 " " + getName() + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getType().getJava6() +
                   " " + par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }
    
    if (isAbstract())
    { res = res + ");\n\n"; 
      return res; 
    } 
    res = res + ")\n";
    res = res + "  { " + resultType.getJava6() + " " +
          " result = " + resultType.getDefaultJava6() +
          ";\n";

    for (int j = 0; j < cons.size(); j++)
    { Constraint con = (Constraint) cons.get(j);
      if (con.getEvent() != null &&
          con.getEvent().equals(this))
      res = res + "    " +
          con.queryOperationJava6(ent) + "\n";  // Java6 version
    }
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String genQueryCodeJava7(Entity ent, Vector cons)
  { String res = "public "; 
    if (isSequential())
    { res = res + "synchronized "; } 
    if (isAbstract())
    { res = res + "abstract "; } // so comment out code
    if (isClassScope() || isStatic())
    { res = res + "static "; } 

    if (resultType == null || "void".equals(resultType + "")) 
    { JOptionPane.showMessageDialog(null, 
        "ERROR: null result type of query operation: " + this,
        "Type error", JOptionPane.ERROR_MESSAGE);
      return ""; 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava7(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
 


    res = res + genPars + resultType.getJava7(elementType) +
                 " " + getName() + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getType().getJava7(par.getElementType()) +
                   " " + par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }
	
    if (isAbstract())
    { res = res + ");\n\n"; 
      return res; 
    } 
	
    res = res + ")\n";
    res = res + "  { " + resultType.getJava7(elementType) + " " +
      " result = " + resultType.getDefaultJava7() + ";\n";

    for (int j = 0; j < cons.size(); j++)
    { Constraint con = (Constraint) cons.get(j);
      if (con.getEvent() != null &&
          con.getEvent().equals(this))
      res = res + "    " +
        con.queryOperationJava7(ent) + "\n"; // Java6 version
    }
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String genQueryCodeCSharp(Entity ent, Vector cons)
  { String res = " public "; 
    if (isUnsafe())
    { res = res + "unsafe "; } 
    // if (isSequential())
    // { res = res + "synchronized "; } 
    if (isAbstract())
    { res = res + "abstract "; } // so comment out code
    if (isClassScope() || isStatic())
    { res = res + "static "; } 

    if (resultType == null || "void".equals(resultType + "")) 
    { JOptionPane.showMessageDialog(null, "ERROR: null result type for: " + this,
                                    "Type error", JOptionPane.ERROR_MESSAGE);
      return ""; 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getCSharp(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
 
    res = res + resultType.getCSharp() +
                " " + getName() + genPars + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getType().getCSharp() +
                   " " + par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }

    if (isAbstract())
    { res = res + ");\n\n"; 
      return res; 
    } 

    res = res + ")\n";
    res = res + "  { " + resultType.getCSharp() + " " +
          " result = " + resultType.getDefaultCSharp() +
          ";\n";
    for (int j = 0; j < cons.size(); j++)
    { Constraint con = (Constraint) cons.get(j);
      if (con.getEvent() != null &&
          con.getEvent().equals(this))
      res = res + "    " +
          con.queryOperationCSharp(ent) + "\n";  // CSharp version
    }
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String genQueryCodeCPP(Entity ent, Vector cons)
  { String res = ""; 
    // " public "; 
    // if (isSequential())
    // { res = res + "synchronized "; } 
    // if (isAbstract())
    // { res = res + "abstract "; } // so comment out code
    // if (isClassScope() || isStatic())
    // { res = res + "static "; } 

    if (resultType == null || "void".equals(resultType + "")) 
    { JOptionPane.showMessageDialog(null, "ERROR: null result type for: " + this,
                                    "Type error", JOptionPane.ERROR_MESSAGE);
      return ""; 
    } 

    String ename = ""; 
    String eTypePars = ""; 
    Vector etpars = new Vector(); 
    String gpars = ent.typeParameterTextCPP(); 

    if (ent == null) 
    { ent = entity; } 
 
    Vector context = new Vector(); 
    if (ent != null) 
    { ename = ent.getName(); 
      context.add(ent); 
      if (ent.hasTypeParameters())
      { etpars.addAll(ent.getTypeParameters()); 
        for (int i = 0; i < etpars.size(); i++) 
        { Type etp = (Type) etpars.get(i); 
          eTypePars = eTypePars + etp.getName(); 
          if (i < etpars.size() - 1)
          { eTypePars = eTypePars + ", "; } 
        } 
      }
    } 

    String opGenPars = ""; 
    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { etpars.addAll(typeParameters); 
      opGenPars = "template<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        opGenPars = opGenPars + " class " + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { opGenPars = opGenPars + ", "; } 
      } 
      opGenPars = opGenPars + "> "; 
    }

    if (etpars.size() > 0)
    { genPars = "template<"; 
      for (int i = 0; i < etpars.size(); i++) 
      { Type tp = (Type) etpars.get(i); 
        genPars = genPars + " class " + tp.getName(); 
        if (i < etpars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 

    String exname = ename; 
    if (eTypePars.length() > 0)
    { exname = ename + "<" + eTypePars + ">"; } 

    String header = ""; 
    if (etpars.size() > 0)
    { header = "  " + genPars + "\n"; }  

    // if (entity != null) 
    // { ename = entity.getName() + "::"; } 
    // else if (ent != null) 
    // { ename = ent.getName() + "::"; } 
    
    res = res + header; 
    res = res + resultType.getCPP(getElementType()) +
                 " " + exname + "::" + getName() + "(";
    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String dec = par.getType().getCPP(par.getElementType()) +
                   " " + par.getName();
      if (i < parameters.size() - 1)
      { dec = dec + ", "; }
      res = res + dec;
    }

    if (isAbstract())
    { res = res + ");\n\n"; 
      return res; 
    } 
    res = res + ")\n";
    res = res + "  { " + resultType.getCPP(getElementType()) + " " +
          " result = " + resultType.getDefaultCPP(elementType) +
          ";\n";

    for (int j = 0; j < cons.size(); j++)
    { Constraint con = (Constraint) cons.get(j);
      if (con.getEvent() != null &&
          con.getEvent().equals(this))
      res = res + "    " +
          con.queryOperationCPP(ent) + "\n";  // CPP version
    }
    res = res + "    return result;\n";
    res = res + "  }\n\n";
    return res;
  }

  public String getParameterisedName() 
  { String res = getName(); 
    if (typeParameters != null && typeParameters.size() > 0) 
    { res = res + "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        res = res + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { res = res + ","; } 
      } 
      res = res + ">"; 
    } 
    return res; 
  } 

  public void saveData(PrintWriter out)
  { if (derived) { return; } 
    out.println("Operation:");
    out.println(getParameterisedName());
    if (useCase != null) 
    { out.println("null " + useCase.getName()); } 
    else 
    { out.println(entity); }  

    if (resultType == null)
    { out.println("void"); }
    else
    { out.println(resultType); } 
    /* String rtname = resultType.getName(); 
      if (rtname.equals("Set") || rtname.equals("Sequence"))
      { out.println(rtname + "(" + elementType + ")"); } 
      else 
      { out.println(rtname); }
    } */ 

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pnme = par.getName();
      // String tnme = par.getType().getName();
      // String elemt = ""; 
      // if ("Set".equals(tnme) || "Sequence".equals(tnme))
      // { if (par.getElementType() != null) 
      //   { elemt = "(" + par.getElementType() + ")"; } 
      // } 
      out.print(pnme + " " + par.getType() + " ");
    }
    out.println(); 
    
    String ss = "";  
    for (int i = 0; i < stereotypes.size(); i++)
    { ss = ss + " " + stereotypes.get(i); } 
    out.println(ss);
    out.println(pre);
    out.println(post); 
    out.println(""); 
    out.println(""); 
  }  // multiple pre, post, and stereotypes, and the operation text

  public void saveKM3(PrintWriter out)
  { if (derived) { return; } 

    if (isStatic())
    { out.print("    static operation "); } 
    else 
    { out.print("    operation "); } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 

    out.print(getName() + genPars + "(");

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pnme = par.getName();
      out.print(pnme + " : " + par.getType() + " ");
      if (i < parameters.size() - 1)
      { out.print(", "); } 
    }
    out.print(") : "); 

    if (resultType == null)
    { out.println("void"); }
    else
    { out.println(resultType); } 

    if (pre != null) 
    { out.println("    pre: " + pre); } 
    else 
    { out.println("    pre: true"); } 
     
    if (activity != null) 
    { out.println("    post: " + post);
      out.println("    activity: " + activity + ";"); 
    } 
    else 
    { out.println("    post: " + post + ";"); } 
      
    out.println(""); 
  } 

  public String getKM3()
  { // if (derived) { return ""; } 
    String res = ""; 

    if (isQuery())
    { res = " query "; } 
    else 
    { res = " operation "; } 

    if (isStatic())
    { res = "    static" + res; } 
    else 
    { res = "   " + res; }

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
	
    res = res + getName() + genPars + "(";

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pnme = par.getName();
      res = res + pnme + " : " + par.getType() + " ";
      if (i < parameters.size() - 1)
      { res = res + ", "; } 
    }
    res = res + ") : "; 

    if (resultType == null)
    { res = res + "void\n"; }
    else
    { res = res + resultType + "\n"; } 

    if (pre != null) 
    { res = res + "    pre: " + pre + "\n"; } 
    else 
    { res = res + "    pre: true\n"; } 
     
    if (activity != null) 
    { res = res + "    post: " + post + "\n";
      res = res + "    activity: " + activity + ";\n"; 
    } 
    else 
    { res = res + "    post: " + post + ";\n"; } 
    res = res + "\n";
    return res;  
  } 

  public String toAST()
  { if (derived) { return ""; } 
    String result = "(OclOperation "; 
    String res = ""; 

    if (isQuery())
    { res = " query "; } 
    else 
    { res = " operation "; } 

    if (isStatic())
    { res = " static" + res; } 
    
    String tpars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { Type tp = (Type) typeParameters.get(0); 
      tpars = " < " + tp.getName() + " > "; 
    }
	
    res = res + getName() + tpars + " ( ";

    if (parameters != null && parameters.size() > 0)
    { res = res + " (OclParameterList "; 
      for (int i = 0; i < parameters.size(); i++)
      { Attribute par = (Attribute) parameters.get(i);
        String pnme = par.toASTParameter();
        res = res + pnme; 
        if (i < parameters.size() - 1)
        { res = res + " , "; } 
      }
      res = res + " ) "; 
    } 
    res = res + " ) : "; 

    if (resultType == null)
    { res = res + "void "; }
    else
    { res = res + resultType.toAST() + " "; } 

    if (pre != null) 
    { res = res + "pre: " + pre.toAST() + " "; } 
    else 
    { res = res + "pre: true "; } 
     
    if (activity != null) 
    { res = res + "post: " + post.toAST() + " ";
      res = res + "activity: " + activity.toAST() + " "; 
    } 
    else 
    { res = res + "post: " + post.toAST() + " "; } 
    return result + res + ")";  
  } 


  /* SaveTextModel */ 
  public String saveModelData(PrintWriter out, Entity ent, Vector entities, Vector types)
  { if (stereotypes.contains("auxiliary"))
    { return ""; } // but derived ops are usually recorded. 

    String nme = getName();
    String opid = Identifier.nextIdentifier("operation_"); 
    out.println(opid + " : Operation"); 
    out.println(opid + ".name = \"" + nme + "\""); 
    Vector cntxt = new Vector(); 
    Vector env = new Vector(); 
    env.addAll(parameters); 

    if (useCase != null) 
    { out.println(opid + ".useCase = " + useCase.getName()); } 
    else if (entity != null) 
    { out.println(opid + ".owner = " + entity); 
      // out.println(opid + " : " + entity + ".ownedOperation"); 
      cntxt.add(entity); 
    }  

    if (isQuery())
    { out.println(opid + ".isQuery = true"); } 
    else 
    { out.println(opid + ".isQuery = false"); } 

    if (isAbstract())
    { out.println(opid + ".isAbstract = true"); } 
    else 
    { out.println(opid + ".isAbstract = false"); } 

    if (isStatic())
    { out.println(opid + ".isStatic = true"); } 
    else 
    { out.println(opid + ".isStatic = false"); } 

    if (isCached())
    { out.println(opid + ".isCached = true"); } 
    else 
    { out.println(opid + ".isCached = false"); } 

    if (resultType == null)
    { out.println(opid + ".type = void");
      out.println(opid + ".elementType = void"); 
    }
    else
    { String rtname = resultType.getName();
      String typeid = resultType.getUMLModelName(out); 
      out.println(opid + ".type = " + typeid);  
      if (rtname.equals("Set") || rtname.equals("Sequence") ||
          rtname.equals("Ref") || 
          rtname.equals("Map") || rtname.equals("Function") )
      { if (elementType == null) 
        { System.err.println("Warning!: No element type for " + this); 
          out.println(opid + ".elementType = void"); 
        } 
        else 
        { String elemtid = elementType.getUMLModelName(out); 
          out.println(opid + ".elementType = " + elemtid);
        }  
      } 
      else  
      { out.println(opid + ".elementType = " + typeid); } 
    } 

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pid = par.saveModelData(out); 
      // String pnme = par.getName();
      out.println(pid + " : " + opid + ".parameters");
    }

    // And typeParameters
    
    for (int i = 0; i < stereotypes.size(); i++)
    { out.println("\"" + stereotypes.get(i) + "\" : " + opid + ".stereotypes"); }
 
    String preid; 
    if (pre != null) 
    { preid = pre.saveModelData(out); } 
    else 
    { preid = "true"; } 
    String postid; 
    if (post != null) 
    { postid = post.saveModelData(out); } 
    else 
    { postid = "true"; }  
    out.println(opid + ".precondition = " + preid);
    out.println(opid + ".postcondition = " + postid);

    System.out.println(">> Operation " + getName() + " activity is: " + activity); 

    Vector localentities = new Vector(); 
    if (typeParameters != null && typeParameters.size() > 0)
    { for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        if (tp.isEntity())
        { localentities.add(tp.getEntity()); } 
      } 
    } 
    localentities.addAll(entities); 

    if (activity == null) 
    { Statement des = generateDesign(ent, localentities, types); 
      des.typeCheck(types,localentities,cntxt,env); 
      String desid = des.saveModelData(out); 
      out.println(opid + ".activity = " + desid); 
      System.out.println(">> Operation " + getName() + " activity is: " + des); 
    } 
    else // if (activity != null) 
    { Statement newstat = activity; 
      if (hasResult() && 
          !Statement.hasResultDeclaration(activity))
      { // Add a  var result : resultType 
        Attribute att = new Attribute("result",resultType,ModelElement.INTERNAL); 
        att.setElementType(elementType); 
        newstat = new SequenceStatement(); 
        CreationStatement cres = new CreationStatement(resultType + "", "result");
        cres.setInstanceType(resultType);
        cres.setElementType(elementType);   
        ((SequenceStatement) newstat).addStatement(cres);
        ((SequenceStatement) newstat).addStatement(activity); 
      } 
      String actid = newstat.saveModelData(out); 
      out.println(opid + ".activity = " + actid); 
    }  
   
    return opid;  
  }  // multiple pre, post, and stereotypes, and the operation text

  public String saveAsUSEData()
  { if (derived) { return ""; } 

    String res = getName() + "(";

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pnme = par.getName();
      String tnme = par.getType().getUMLName();
      String elemt = ""; 
      if ("Set".equals(tnme) || "Sequence".equals(tnme))
      { if (par.getElementType() != null) 
        { elemt = "(" + par.getElementType().getUMLName() + ")"; } 
      } 
      res = res + pnme + " : " + tnme + elemt;
      if (i < parameters.size() - 1) { res = res + ", "; } 
    }
    res = res + ") "; 

    if (resultType != null)
    { String rtname = resultType.getName(); 
      if (rtname.equals("Set") || rtname.equals("Sequence"))
      { if (elementType != null) 
        { res = res + " : " + resultType.getUMLName() + "(" + elementType.getUMLName() + ")"; } 
        else 
        { res = res + " : " + resultType.getUMLName() + "()"; } 
      } 
      else 
      { res = res + " : " + resultType.getUMLName(); }
    } 
    res = res + "\n"; 
    
    java.util.Map env = new java.util.HashMap(); 

    if (pre == null) 
    { pre = new BasicExpression("true"); }  
    
    if (post == null) 
    { post = new BasicExpression("true"); } 

    res = res + "pre pre1:\n  " + pre.toOcl(env,true) + "\n";
    res = res + "post post1:\n   " + post.toOcl(env,true); 
    res = res + "\n\n"; 
    return res; 
  }  // multiple pre, post, and stereotypes

  public String saveAsPlantUML()
  { if (derived) { return ""; } 
   
    String res = ""; 
   
    if (instanceScope) 
    { if (isAbstract()) 
      { res = res + "{abstract} "; }
    }  
    else 
    { res = res + "{static} "; } 

    res = res + getName() + "(";

    for (int i = 0; i < parameters.size(); i++)
    { Attribute par = (Attribute) parameters.get(i);
      String pnme = par.getName();
      String tnme = par.getType().getUMLName();
      String elemt = ""; 
      if ("Set".equals(tnme) || "Sequence".equals(tnme))
      { if (par.getElementType() != null) 
        { elemt = "(" + par.getElementType().getUMLName() + ")"; } 
      } 
      res = res + pnme + " : " + tnme + elemt;
      if (i < parameters.size() - 1) { res = res + ", "; } 
    }
    res = res + ") "; 

    if (resultType != null)
    { String rtname = resultType.getName(); 
      if (rtname.equals("Set") || rtname.equals("Sequence"))
      { if (elementType != null) 
        { res = res + " : " + resultType.getUMLName() + "(" + elementType.getUMLName() + ")"; } 
        else 
        { res = res + " : " + resultType.getUMLName() + "()"; } 
      } 
      else 
      { res = res + " : " + resultType.getUMLName(); }
    } 
    res = res + "\n"; 
    
  /*  java.util.Map env = new java.util.HashMap(); 

    if (pre == null) 
    { pre = new BasicExpression("true"); }  
    
    if (post == null) 
    { post = new BasicExpression("true"); } 

    res = res + "pre pre1:\n  " + pre.toOcl(env,true) + "\n";
    res = res + "post post1:\n   " + post.toOcl(env,true); 
    res = res + "\n\n"; */ 

    return res; 
  }  // multiple pre, post, and stereotypes

  public static Expression wpc(Expression pst, String op, String feat, 
                               String val)
  { // calculates [opfeat(val)]pst 
    Expression res = pst; 
    BasicExpression valbe = new BasicExpression(val);  
    if (op.equals("set"))
    { // System.out.println(valbe + " multiplicity is: " + valbe.isMultiple()); 
      res = pst.substituteEq(feat,valbe); 
    } 
    else if (op.equals("add"))
    { SetExpression se = new SetExpression(); 
      se.addElement(valbe); 
      Expression featbe = new BasicExpression(feat); 
      res = pst.substituteEq(feat,new BinaryExpression("\\/",featbe,se)); 
    }
    else if (op.equals("remove"))
    { SetExpression se = new SetExpression(); 
      se.addElement(valbe); 
      Expression featbe = new BasicExpression(feat); 
      res = pst.substituteEq(feat,new BinaryExpression("-",featbe,se)); 
    }
    else if (op.equals("union"))
    { Expression featbe = new BasicExpression(feat); 
      res = pst.substituteEq(feat,new BinaryExpression("\\/",featbe,valbe)); 
    }
    else if (op.equals("subtract"))
    { Expression featbe = new BasicExpression(feat); 
      res = pst.substituteEq(feat,new BinaryExpression("-",featbe,valbe)); 
    }

    return res; 
  }

  /*
  public static Expression wpc(Expression post, Vector uses, Expression exbe,
                               String op, String feat,
                               BasicExpression valbe)
  { if (uses.size() == 0) { return post; }
    Expression nextpost = post; 
  
    if (op.equals("set"))
    { nextpost = post.substituteEq(feat,valbe); } 
    else if (op.equals("add")) // replace feat by feat \/ { valbe } for add, etc
    { Expression featbe = new BasicExpression(feat); 
      SetExpression setexp = new SetExpression(); 
      setexp.addElement(valbe); 
      nextpost = post.substituteEq(feat,new BinaryExpression("\\/",featbe,setexp)); 
    }
    else if (op.equals("remove"))  // replace feat by feat - { valbe } 
    { Expression featbe = new BasicExpression(feat); 
      SetExpression setexp = new SetExpression(); 
      setexp.addElement(valbe); 
      nextpost = post.substituteEq(feat,new BinaryExpression("-",featbe,setexp)); 
    } // do these ONCE only
    return wpc0(nextpost,uses,exbe,op,feat,valbe); 
  } */   

  // Not checked, may need to be updated
  public static Expression wpc(Expression post, Vector uses, Expression exbe,
                               String op, String feat, boolean ord, 
                               BasicExpression valbe)
  { if (uses.size() == 0) { return post; }
    Expression pre = post; 
    BasicExpression use = (BasicExpression) uses.get(0); 

    if (use.objectRef == null || use.objectRef.umlkind == Expression.CLASSID)
    { if (op.equals("set"))
      { pre = post.substitute(use,valbe); }  // use is just feat 
      else if (op.equals("add")) // replace feat by feat \/ { valbe } for add, etc
      { Expression featbe = (BasicExpression) use.clone();
                          // new BasicExpression(feat);
        SetExpression setexp = new SetExpression(); 
        setexp.addElement(valbe); 
        Expression repl; 
        if (ord)
        { setexp.setOrdered(true); 
          repl = new BinaryExpression("^",featbe,setexp);
        } 
        else 
        { repl = new BinaryExpression("\\/",featbe,setexp); }
          
        repl.setBrackets(true); 
        pre = post.substitute(use,repl); 
      }
      else if (op.equals("remove"))  // replace feat by feat - { valbe } 
      { Expression featbe = new BasicExpression(feat); 
        SetExpression setexp = new SetExpression(); 
        setexp.addElement(valbe); 
        Expression repl = new BinaryExpression("-",featbe,setexp); 
        repl.setBrackets(true); 
        pre = post.substitute(use,repl); 
      } // do these ONCE only
      else if (op.equals("union"))
      { Expression featbe = (BasicExpression) use.clone();
                          // new BasicExpression(feat);
        Expression repl;
        if (ord)
        { repl = new BinaryExpression("^",featbe,valbe); }
        else 
        { repl = new BinaryExpression("\\/",featbe,valbe); }
        repl.setBrackets(true); 
        pre = post.substitute(use,repl); 
      }
      else if (op.equals("subtract"))  // replace feat by feat - valbe 
      { Expression featbe = new BasicExpression(feat); 
        Expression repl = new BinaryExpression("-",featbe,valbe); 
        repl.setBrackets(true); 
        pre = post.substitute(use,repl); 
      } // do these ONCE only

      // Expression selfeq = 
      //   new BinaryExpression("=",new BasicExpression("self"),
      //                        exbe); 
      // pre = Expression.simplifyImp(selfeq,pre);       
      uses.remove(0);
      return wpc(pre,uses,exbe,op,feat,ord,valbe);
    }

    if (use.objectRef.umlkind == Expression.VARIABLE)
    { pre = pre.substitute(use,valbe);
      pre = pre.substituteEq(use.objectRef.toString(),exbe);  
      // Expression neq = new BinaryExpression("/=",exbe,use.objectRef); 
      // Expression pre2 = new BinaryExpression("&",neq,pre); 
      // pre = new BinaryExpression("or",pre1,pre2);
      // pre.setBrackets(true);  
      // Only if set? 
    }
    else if (!use.objectRef.isMultiple())
    { Expression pre0 = pre.substitute(use,valbe); // also in above case?
      BinaryExpression eq =
        new BinaryExpression("=",exbe,use.objectRef);
      pre = Expression.simplify("=>",eq,pre0,new Vector());
     // Expression neq = new BinaryExpression("/=",exbe,use.objectRef); 
     // Expression pre2 = new BinaryExpression("&",neq,pre); 
     // pre = new BinaryExpression("or",pre1,pre2);  
     // pre.setBrackets(true); 
    }      
    else if (use.objectRef.isMultiple())  // objs.f
    { if (op.equals("set"))
      { if (ord && use.objectRef.isOrdered())
        { // just objs.(f <+ { ex |-> value })
          BasicExpression fbe = 
            new BasicExpression(feat);
          Expression maplet = new BinaryExpression("|->",exbe,valbe); 
        
          SetExpression se = new SetExpression();
          se.addElement(maplet);
          BinaryExpression oplus = new BinaryExpression("<+",fbe,se);
          oplus.setBrackets(true);  
          oplus.objectRef = use.objectRef;
          pre = pre.substitute(use,oplus);   
        }  
        else
        { SetExpression se = new SetExpression();
          se.addElement(exbe);
          Expression newobjs =
              new BinaryExpression("-",use.objectRef,se);
          newobjs.setBrackets(true); 
          BasicExpression fbe = 
              new BasicExpression(feat);
          fbe.setObjectRef(newobjs);
          if (use.umlkind == Expression.ATTRIBUTE ||
              use.multiplicity == ModelElement.ONE)
          { // replace objs.f by (objs - {ex}).f \/ {value}
            SetExpression vse = new SetExpression();
            vse.addElement(valbe);
            Expression repl = 
              new BinaryExpression("\\/",fbe,vse);
            repl.setBrackets(true); 
            pre = pre.substitute(use,repl);   
            // and implied by exbe: objs
          }
          else if (use.umlkind == Expression.ROLE &&
                   use.multiplicity != ModelElement.ONE) 
          { Expression repl = 
              new BinaryExpression("\\/",fbe,valbe);
            repl.setBrackets(true); 
            pre = pre.substitute(use,repl); 
          }
        }
        Expression exin = new BinaryExpression(":",exbe,use.objectRef); // : ran ?
        pre = Expression.simplifyImp(exin,pre);   
        uses.remove(0);
        return wpc(pre,uses,exbe,op,feat,ord,valbe);
      }
      else if (op.equals("add"))
      { if (ord && use.objectRef.isOrdered())
        { // just objs.(f <+ { ex |-> f(ex) ^ [value] })
          BasicExpression fbe1 = 
            new BasicExpression(feat);
          BasicExpression fbe = 
            new BasicExpression(feat);
  
          BasicExpression exbe1 = (BasicExpression) exbe.clone(); 
          exbe1.objectRef = fbe1;  // f(ex)
          SetExpression sq = new SetExpression(); 
          sq.setOrdered(true); 
          sq.addElement(valbe);  // [val]
          Expression cat = new BinaryExpression("^",exbe1,sq); 
  
          Expression maplet = new BinaryExpression("|->",exbe,cat); 
        
          SetExpression se = new SetExpression();
          se.addElement(maplet);
          BinaryExpression oplus = new BinaryExpression("<+",fbe,se);
          oplus.setBrackets(true);  
          oplus.objectRef = use.objectRef;
          pre = pre.substitute(use,oplus);   
        }  
        else 
        { SetExpression vse = new SetExpression();
          vse.addElement(valbe);
          Expression repl = 
            new BinaryExpression("\\/",use,vse);
          repl.setBrackets(true); 
          pre = pre.substitute(use,repl);   
        }
      } // exbe: objs for pre
      else if (op.equals("union"))
      { Expression repl = 
          new BinaryExpression("\\/",use,valbe);
        repl.setBrackets(true); 
        pre = pre.substitute(use,repl);   
      } // exbe: objs for pre
      else if (op.equals("remove"))
      { SetExpression se = new SetExpression();
        se.addElement(exbe);
        Expression newobjs = // (objs - {ex}).f
          new BinaryExpression("-",use.objectRef,se);
        newobjs.setBrackets(true); 
        BasicExpression fbe = 
          new BasicExpression(feat);
        fbe.setObjectRef(newobjs);
        SetExpression vse = new SetExpression();
        vse.addElement(valbe);    // { value }
        BasicExpression fbe2 = new BasicExpression(feat);
        fbe2.setObjectRef(exbe);  // ex.f
        Expression minus = 
          new BinaryExpression("-",fbe2,vse);
        minus.setBrackets(true); 
        Expression repl = 
          new BinaryExpression("\\/",fbe,minus);
        repl.setBrackets(true); 
        pre = pre.substitute(use,repl);   
      } 
      else if (op.equals("subtract"))
      { SetExpression se = new SetExpression();
        se.addElement(exbe);
        Expression newobjs = // (objs - {ex}).f
          new BinaryExpression("-",use.objectRef,se);
        newobjs.setBrackets(true); 
        BasicExpression fbe = 
          new BasicExpression(feat);
        fbe.setObjectRef(newobjs);
        BasicExpression fbe2 = new BasicExpression(feat);
        fbe2.setObjectRef(exbe);  // ex.f
        Expression minus = 
          new BinaryExpression("-",fbe2,valbe);
        minus.setBrackets(true); 
        Expression repl = 
          new BinaryExpression("\\/",fbe,minus);
        repl.setBrackets(true); 
        pre = pre.substitute(use,repl);   
      }  // union, subtract?
      
      Expression inexp = new BinaryExpression(":",exbe,use.objectRef); 
      pre = new BinaryExpression("=>",inexp,pre);
    } // or exbe /: objs & this
    uses.remove(0); 
    return wpc(pre,uses,exbe,op,feat,ord,valbe); 
  }

  public Statement generateDesign(Entity ent, Vector entities, Vector types)
  { Statement res = new SequenceStatement(); 
    Vector localEntities = allTypeParameterEntities(ent); 
    localEntities.addAll(entities); 

    String name = getName();
    String ename = ""; 
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent);  
      ename = ent.getName(); 
    } 

    String ex = ename.toLowerCase() + "x";    // very messy to do this 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getJavaParameterDec(); 

    // String header = 

    if ("run".equals(name) && ent != null && ent.isActive())
    { if (ent.isInterface())
      { return res; } 

      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,localEntities,context,new Vector()); 
        return rc; 
      } // should be rc.updateForm(env0,true)
      else if (activity != null) 
      { activity.typeCheck(types,localEntities,context,new Vector()); 
        return activity; 
      } 

      return res; 
    }  


    if (sm != null) 
    { Statement cde = sm.getMethodJava(); // sequential code
      return cde; 
    } 
    else if (activity != null) 
    { Vector env1 = new Vector(); 
      env1.addAll(parameters); 
      activity.typeCheck(types, 
                         localEntities,context,env1);

      System.out.println(">>> Activity of " + name + " is: " + activity); 
 
      return activity; 
    } 

    
    Vector atts = new Vector(); 
    ReturnStatement rets = null; 
  
    String resT = "void";
    if (post == null) 
    { return res; }   // or use the SM if one exists
	// if ("true".equals(post + ""))
	// { return res; }

    if (resultType != null && 
        !("void".equals(resultType.getName())))
    { resT = resultType + ""; 
      Attribute r = new Attribute("result", resultType, ModelElement.INTERNAL); 
      r.setElementType(elementType); 
      atts.add(r); 
      BasicExpression rbe = new BasicExpression(r); 
      CreationStatement cs = 
          new CreationStatement(resT, "result"); 
      cs.setType(resultType); 
      cs.setElementType(elementType); 
      // Expression init = resultType.getDefaultValueExpression(elementType); 
      // AssignStatement initialise = new AssignStatement(rbe, init); 
      ((SequenceStatement) res).addStatement(cs); 
      // ((SequenceStatement) res).addStatement(initialise); 
      rets = new ReturnStatement(rbe); 
    }

    if (ent != null && ent.isInterface())
    { return res; } 

    atts.addAll(parameters); 

    if (query && isNoRecursion())
    { Vector cases = Expression.caselist(post); 
	
      System.out.println(">>> Caselist = " + cases); 
	  
      Statement qstat = 
        designQueryList(cases, resT, env0, 
                        types, localEntities, atts);
      
      System.out.println(">>> qstat for no recursion = " + qstat); 
	  
      qstat.setBrackets(true); 
      WhileStatement ws = 
         new WhileStatement(new BasicExpression(true), qstat); 
      return ws; 
    } 
    else if (query)
    { Vector cases = Expression.caselist(post); 
	
      System.out.println(">>> Caselist = " + cases); 
	  
      Statement qstat = 
        designQueryList(cases, resT, env0, 
                        types, localEntities, atts);
      
      System.out.println(">>> qstat = " + qstat); 
	  
      ((SequenceStatement) res).addStatement(qstat); 
      ((SequenceStatement) res).addStatement(rets); 
      return res; 
    } 
    else 
    { Statement stat; 
      if (bx) 
      { stat = post.statLC(env0,true); } 
      else 
      { stat = post.generateDesign(env0, true); } 
 
      ((SequenceStatement) res).addStatement(stat); 
      if (resultType != null && !("void".equals(resultType.getName()))) 
      { ((SequenceStatement) res).addStatement(rets); }  

      return res; 
    } // if no return 

    // return res; 

    // { return buildQueryOp(ent,name,resultType,resT,entities,types); }
    // else 
    // { return buildUpdateOp(ent,name,resultType,resT,entities,types); }
  } // ignore return type for update ops for now. 


  // For the entity operation: ent == entity normally 
  public String getOperationCode(Entity ent, Vector entities, Vector types)
  { String name = getName();
    String ename = ""; 
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent);  
      ename = ent.getName(); 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
 

    String ex = ename.toLowerCase() + "x";    // very messy to do this 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getJavaParameterDec(); 

    // String header = 

    String textcode = ""; 
    if (hasText())
    { textcode = text + "\n"; } 
  

    if ("run".equals(name) && ent != null && ent.isActive())
    { if (ent.isInterface())
      { return "  public void run();\n"; } 

      String res = "  public void run()\n  { "; 
      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,entities,context,new Vector()); 
        return res + rc.toStringJava() + "  }\n\n"; 
      } // should be rc.updateForm(env0,true)
      res = res + textcode; 
      return res + " }\n\n"; 
    }  


    if (sm != null) 
    { Statement cde = sm.getMethodJava(); // sequential code
      if (cde != null) 
      { Vector localatts = new Vector(); 
        localatts.addAll(sm.getAttributes()); 
        String res = ""; 
        Type tp = getResultType(); 
        String ts = "void";
        String statc = ""; 

        if (tp != null) 
        { ts = tp.getJava(); } 

        if (isClassScope() || isStatic()) 
        { statc = "static "; } 

        res = "  public " + statc + genPars + ts + " " + name + "(" + pars + ")\n  { "; 

        if (comments != null) 
        { res = res + "/* " + comments + " */\n  "; } 

        if (ent.isInterface())
        { return "  public " + genPars + ts + " " + name + "(" + pars + ");\n"; } 
 
        if (tp != null && !"void".equals(ts))
        { res = res + ts + " result;\n"; }
        localatts.addAll(parameters);   
        cde.typeCheck(types,entities,context,localatts); 
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + " " + ex + " = this;\n  "; }   
        return res + cde.updateForm(env0,true,types,entities,localatts) + "\n  }";
      }   // cde.updateForm(env0,true)
    } 
    else if (activity != null) 
    { Vector localatts = new Vector(); 
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { env0.put(ename,ex); } //  or "this" if not static ??
      String res = ""; 

      Type tp = getResultType(); 
      String ts = "void";

      String header = ""; 

      if (tp != null) 
      { ts = tp.getJava(); } 

      if (ent != null && ent.isInterface())
      { return "  public " + genPars + ts + " " + name + "(" + pars + ");\n"; } 

      if (isSequential())
      { header = header + "synchronized "; } 
      if (isAbstract())
      { header = header + "abstract "; } 
      if (isFinal())
      { header = header + "final "; } 
      if (isClassScope() || isStatic())
      { header = header + "static "; } 

      res = "  public " + header + genPars + ts + " " + name + "(" + pars + ")\n  {  "; 

      if (comments != null) 
      { res = res + "/* " + comments + " */\n  "; } 

      if (tp != null && !"void".equals(ts))
      { res = res + ts + " result;\n"; }
      localatts.addAll(parameters);   

      Vector preterms = activity.allPreTerms(); // But some should be within the activity
      if (preterms.size() > 0) 
      { Statement newpost = (Statement) activity.clone(); 
        // System.out.println("PRE terms: " + preterms); 
        Vector processed = new Vector(); 
        String newdecs = ""; 
        for (int i = 0; i < preterms.size(); i++)
        { BasicExpression preterm = (BasicExpression) preterms.get(i);
          if (processed.contains(preterm)) { continue; }  
          // check if the preterm is valid and skip if not. 
          Type typ = preterm.getType();  
            // but actual type may be list if multiple
          Type actualtyp; 
          String newdec = ""; 
          String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
          String pretermqf = preterm.queryForm(env0,true); 
             // classqueryForm; you want to remove the @pre

          BasicExpression prebe = new BasicExpression(pre_var); 

          if (preterm.isMultiple())
          { if (preterm.isOrdered() || 
                preterm.isSequenceValued())
            { actualtyp = new Type("Sequence",null); } 
            else 
            { actualtyp = new Type("Set",null); } 
            actualtyp.setElementType(preterm.getElementType()); 
            actualtyp.setSorted(preterm.isSorted()); 

            if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
            { pretermqf = "Controller.inst()." + pretermqf.toLowerCase() + "s"; } 
            newdec = actualtyp.getJava() + " " + pre_var + " = new Vector();\n" + 
                 "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
          } // Use this strategy also below and in Statement.java 
          else 
          { actualtyp = typ;
            newdec = actualtyp.getJava() + " " + pre_var + " = " + pretermqf + ";\n";
          } 
          newdecs = newdecs + "    " + newdec; 
          prebe.type = actualtyp; 
          prebe.elementType = preterm.elementType; 
          // System.out.println("$$$ PRE variable " + prebe + " type= " + actualtyp + 
          //                     " elemtype= " + prebe.elementType + " FOR " + preterm); 
          
          Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
          preatt.setElementType(preterm.elementType); 
          localatts.add(preatt); 
          newpost = newpost.substituteEq("" + preterm,prebe); 
          processed.add(preterm); 
        }  // substitute(preterm,prebe) more appropriate 

        newpost.typeCheck(types,entities,context,localatts);
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + " " + ex + " = this;\n  "; }   
        return res + newdecs + newpost.updateForm(env0,false,types,entities,localatts) + "\n  }\n";   
      }     // updateForm(, true)? 
      activity.typeCheck(types,entities,context,localatts);
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { res = res + ename + " " + ex + " = this;\n  "; }   
      return res + activity.updateForm(env0,false,types,entities,localatts) + "\n  }\n";   
    } 
      
    String resT = "void";
    if (post == null) 
    { return ""; }   // or use the SM if one exists
    if (resultType != null)
    { resT = resultType.getJava(); }

    if (ent != null && ent.isInterface())
    { return "  public " + genPars + resT + " " + name + "(" + pars + ");\n"; } 

    if (query)
    { return buildQueryOp(ent,name,resultType,resT,entities,types); }
    else 
    { return buildUpdateOp(ent,name,resultType,resT,entities,types); }
  } // ignore return type for update ops for now. 

  public String getOperationCodeJava6(Entity ent, Vector entities, Vector types)
  { String name = getName();
    Vector context = new Vector(); 
    String ename = ""; 
    if (ent != null) 
    { context.add(ent); 
      ename = ent.getName(); 
    } 
    String ex = ename.toLowerCase() + "x"; 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getJava6ParameterDec(); 

    String textcode = ""; 
    if (hasText())
    { textcode = text + "\n"; } 
    

    if ("run".equals(name) && ent != null && ent.isActive())
    { if (ent.isInterface())
      { return "  public void run();\n"; } 

      String res = "  public void run()\n  { "; 
      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,entities,context,new Vector()); 
        return res + rc.updateFormJava6(env0,true) + "  }\n\n"; 
      } 
      res = res + textcode; 
      return res + " }\n\n"; 
    }  


    if (sm != null) 
    { Statement cde = sm.getSequentialCode(); // sequential code
      if (cde != null) 
      { Vector localatts = new Vector(); 
        localatts.addAll(sm.getAttributes()); 
        String res = ""; 
        Type tp = getResultType(); 

        String ts = "void";
        if (tp != null) 
        { ts = tp.getJava6(); } 

        String statc = ""; 
        if (isClassScope() || isStatic()) 
        { statc = "static "; } 


        res = "  public " + statc + ts + " " + name + "(" + pars + ")\n{  "; 

        if (comments != null) 
        { res = res + "/* " + comments + " */\n  "; } 

        if (ent != null && ent.isInterface())
        { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 
 
        if (tp != null && !"void".equals(ts))
        { res = res + ts + " result;\n"; }
        localatts.addAll(parameters);   
        cde.typeCheck(types,entities,context,localatts); 
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + " " + ex + " = this;\n  "; }   
        return res + cde.updateFormJava6(env0,true) + "\n  }";
      }   
    } 
    else if (activity != null) 
    { Vector localatts = new Vector(); 
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { env0.put(ename,ex); } //  or "this" if not static ??
      String res = ""; 

      Type tp = getResultType(); 
      String ts = "void";

      String header = ""; 

      if (tp != null) 
      { ts = tp.getJava6(); } 

      if (ent != null && ent.isInterface())
      { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 

      if (isSequential())
      { header = header + "synchronized "; } 
      if (isAbstract())
      { header = header + "abstract "; } 
      if (isFinal())
      { header = header + "final "; } 
      if (isClassScope() || isStatic())
      { header = header + "static "; } 

      res = "  public " + header + ts + " " + name + "(" + pars + ")\n  {  "; 

      if (comments != null) 
      { res = res + "/* " + comments + " */\n  "; } 

      if (tp != null && !("void".equals(ts)))
      { res = res + ts + " result;\n"; }

      localatts.addAll(parameters);   
      Vector preterms = activity.allPreTerms(); 
      if (preterms.size() > 0) 
      { Statement newpost = (Statement) activity.clone(); 
        // System.out.println("Pre terms: " + preterms); 
        Vector processed = new Vector(); 
        String newdecs = ""; 
        for (int i = 0; i < preterms.size(); i++)
        { BasicExpression preterm = (BasicExpression) preterms.get(i);
          if (processed.contains(preterm)) { continue; }  
          Type typ = preterm.getType();  // but actual type may be list if multiple
          Type actualtyp; 
          String newdec = ""; 
          String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
          String pretermqf = preterm.classqueryFormJava6(env0,true); 
          BasicExpression prebe = new BasicExpression(pre_var); 

          if (preterm.isMultiple())
          { if (preterm.isOrdered() || 
                preterm.isSequenceValued())
            { actualtyp = new Type("Sequence",null); 
              newdec =  "ArrayList " + pre_var + " = new ArrayList();\n" + 
                     "    " + pre_var + ".addAll(" + pretermqf + ");\n";
            } 
            else 
            { actualtyp = new Type("Set",null); 
              newdec =  "HashSet " + pre_var + " = new HashSet();\n" + 
                     "    " + pre_var + ".addAll(" + pretermqf + ");\n";
            }  
            actualtyp.setElementType(preterm.getElementType());
            actualtyp.setSorted(preterm.isSorted()); 
          } 
          else 
          { actualtyp = typ;
            newdec = actualtyp.getJava6() + " " + pre_var + " = " + pretermqf + ";\n";
          } 
          newdecs = newdecs + "    " + newdec; 
          prebe.type = actualtyp; 
          prebe.elementType = preterm.elementType; 
          // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
          //                    " elemtype= " + prebe.elementType); 
          
          Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
          preatt.setElementType(preterm.elementType); 
          localatts.add(preatt); 
          newpost = newpost.substituteEq("" + preterm,prebe); 
          processed.add(preterm); 
        }  // substitute(preterm,prebe) more appropriate 

        newpost.typeCheck(types,entities,context,localatts);
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + " " + ex + " = this;\n  "; }   
        return res + newdecs + newpost.updateFormJava6(env0,false) + "\n  }\n";   
      }     // updateForm(, true)? 
      activity.typeCheck(types,entities,context,localatts);
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { res = res + ename + " " + ex + " = this;\n  "; }   
      return res + activity.updateFormJava6(env0,false) + "\n  }\n";   
    } 
      
    String resT = "void";
    if (post == null) 
    { return ""; }   // or use the SM if one exists
    if (resultType != null)
    { resT = resultType.getJava6(); }

    if (ent != null && ent.isInterface())
    { return "  public " + resT + " " + name + "(" + pars + ");\n"; } 

    if (query)
    { return buildQueryOpJava6(ent,name,resultType,resT,entities,types); }
    else 
    { return buildUpdateOpJava6(ent,name,resultType,resT,entities,types); }
  } // ignore return type for update ops for now. 

  public String getOperationCodeJava7(Entity ent, Vector entities, Vector types)
  { String name = getName();
    Vector context = new Vector(); 
    String ename = ""; 
    String completeename = "";

    if (ent != null) 
    { context.add(ent); 
      ename = ent.getName(); 
      completeename = ent.getCompleteName(); 
    } 

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 

 	
    String ex = ename.toLowerCase() + "x"; 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getJava7ParameterDec(); 

    String textcode = ""; 
    if (hasText())
    { textcode = text + "\n"; } 
 

    if ("run".equals(name) && ent != null && ent.isActive())
    { if (ent.isInterface())
      { return "  public void run();\n"; } 

      String res = "  public void run()\n  { "; 
      res = res + ename + " " + ex + " = this;\n  ";
      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,entities,context,new Vector()); 
        return res + rc.updateFormJava7(env0,true) + "  }\n\n"; 
      } 
      else if (activity != null) 
      { Vector latts = new Vector(); 
        activity.typeCheck(types,entities,context,latts);
          
        return res + 
          activity.updateFormJava7(env0,false) + "\n  }\n\n";   
      } 
      res = res + textcode; 
      return res + "  }\n\n"; 
    }  


    if (sm != null) 
    { Statement cde = sm.getSequentialCode(); // sequential code
      if (cde != null) 
      { Vector localatts = new Vector(); 
        localatts.addAll(sm.getAttributes()); 
        String res = ""; 
        Type tp = getResultType(); 

        String ts = "void";
        if (tp != null) 
        { ts = tp.getJava7(elementType); } 

        String statc = ""; 
        if (isClassScope() || isStatic()) 
        { statc = "static "; } 


        res = "  public " + statc + ts + " " + name + "(" + pars + ")\n{  "; 

        if (comments != null) 
        { res = res + "/* " + comments + " */\n  "; } 
    
        if (ent != null && ent.isInterface())
        { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 
 
        if (tp != null && !"void".equals(ts))
        { res = res + ts + " result;\n"; }
        localatts.addAll(parameters);   
        cde.typeCheck(types,entities,context,localatts); 
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + " " + ex + " = this;\n  "; }   
        return res + cde.updateFormJava7(env0,true) + "\n  }";
      }   
    } 
    else if (activity != null) 
    { Vector localatts = new Vector(); 
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { env0.put(ename,ex); } //  or "this" if not static ??
      String res = ""; 

      Type tp = getResultType(); 
      String ts = "void";

      String header = ""; 

      if (tp != null) 
      { ts = tp.getJava7(elementType); } 

      if (ent != null && ent.isInterface())
      { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 

      if (isSequential())
      { header = header + "synchronized "; } 
      if (isAbstract())
      { header = header + "abstract "; } 
      if (isFinal())
      { header = header + "final "; } 
      if (isClassScope() || isStatic())
      { header = header + "static "; } 

      res = "  public " + header + ts + " " + name + "(" + pars + ")\n  { "; 

      if (comments != null) 
      { res = res + "/* " + comments + " */\n  "; } 

      if (tp != null & !"void".equals(ts))
      { res = res + ts + " result;\n"; }
	  
      localatts.addAll(parameters);   
      Vector preterms = activity.allPreTerms(); 
      if (preterms.size() > 0) 
      { Statement newpost = (Statement) activity.clone(); 
        // System.out.println(">>> Pre terms: " + preterms); 
        Vector processed = new Vector(); 
        String newdecs = ""; 
        for (int i = 0; i < preterms.size(); i++)
        { BasicExpression preterm = (BasicExpression) preterms.get(i);
          if (processed.contains(preterm)) { continue; }  
          Type typ = preterm.getType();  // but actual type may be list if multiple
          Type actualtyp; 
          String newdec = ""; 
          String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
          String pretermqf = preterm.classqueryFormJava7(env0,true); 
          BasicExpression prebe = new BasicExpression(pre_var); 

          if (preterm.isMultiple())
          { if (preterm.isOrdered() || 
                preterm.isSequenceValued())
            { actualtyp = new Type("Sequence",null); 
              actualtyp.setElementType(preterm.getElementType());
              String jType = actualtyp.getJava7(preterm.getElementType()); 
              newdec =  "    " + jType + " " + pre_var + " = new " + jType + "();\n" + 
                        "    " + pre_var + ".addAll(" + pretermqf + ");\n";
            } 
            else 
            { actualtyp = new Type("Set",null); 
              actualtyp.setElementType(preterm.getElementType());
              
              String jType = actualtyp.getJava7(preterm.getElementType()); 
              newdec = "    " + jType + " " + pre_var + " = new " + jType + "();\n" + 
                       "    " + pre_var + ".addAll(" + pretermqf + ");\n";
            } 
 
            actualtyp.setSorted(preterm.isSorted()); 
            prebe.setSorted(preterm.isSorted()); 
            System.out.println(">> Type of " + preterm + " = " + preterm.type + " { " + preterm.isSorted() + " }");
          } 
          else 
          { actualtyp = typ;
            newdec = actualtyp.getJava7(preterm.elementType) + " " + pre_var + " = " + pretermqf + ";\n";
          } 
          newdecs = newdecs + "    " + newdec; 
          prebe.type = actualtyp; 
          prebe.elementType = preterm.elementType; 
          System.out.println(">> Pre variable " + prebe + " type= " + actualtyp + 
                             " elemtype= " + prebe.elementType); 
          
          Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
          preatt.setElementType(preterm.elementType); 
          localatts.add(preatt); 
          newpost = newpost.substituteEq("" + preterm,prebe); 
          processed.add(preterm); 
        }  // substitute(preterm,prebe) more appropriate 

        newpost.typeCheck(types,entities,context,localatts);
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + "    " + completeename + " " + ex + " = this;\n  "; }   
        return res + newdecs + newpost.updateFormJava7(env0,false) + "\n  }\n";   
      }     // updateForm(, true)? 

      activity.typeCheck(types,entities,context,localatts);

      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { res = res + "    " + completeename + " " + ex + " = this;\n  "; }   
      return res + activity.updateFormJava7(env0,false) + "\n  }\n";   
    } 

    // System.out.println("JAVA 7 OPERATION CODE: "); 
      
    String resT = "void";
    if (post == null) 
    { return ""; }   // or use the SM if one exists
    if (resultType != null)
    { resT = resultType.getJava7(elementType); }

    if (ent != null && ent.isInterface())
    { return "  public " + resT + " " + name + "(" + pars + ");\n"; } 

    if (query)
    { return buildQueryOpJava7(ent,name,resultType,resT,entities,types); }
    else 
    { return buildUpdateOpJava7(ent,name,resultType,resT,entities,types); }
  } // ignore return type for update ops for now. 

  public String getOperationCodeCSharp(Entity ent, Vector entities, Vector types)
  { String name = getName();
    Vector context = new Vector(); 
    String ename = ""; 
    String epars = ""; 

    if (ent != null) 
    { context.add(ent);  
      ename = ent.getName();
      epars = ent.typeParameterTextCSharp(); 
    }  

    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getCSharp(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 


    String ex = ename.toLowerCase() + "x"; 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getCSharpParameterDec(); 

    String textcode = ""; 
    if (hasText())
    { textcode = text + "\n"; } 

    /* if ("run".equals(name) && ent.isActive())
    { if (ent != null && ent.isInterface())
      { return "  public void run();\n"; } 

      String res = "  public void run()\n  { "; 
      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,entities,context,new Vector()); 
        return res + rc.toStringJava() + "  }\n\n"; 
      } 
      return res + " }\n\n"; 
    }  */ 


    if (sm != null) 
    { Statement cde = sm.getSequentialCode(); // sequential code
      if (cde != null) 
      { Vector localatts = new Vector(); 
        localatts.addAll(sm.getAttributes()); 
        String res = ""; 
        Type tp = getResultType(); 
        String ts = "void";
        if (tp != null) 
        { ts = tp.getCSharp(); } 

        String statc = ""; 
        if (ent == null || isClassScope() || isStatic()) 
        { statc = "static "; } 

        res = "  public " + statc + ts + " " + name + genPars + "(" + pars + ")\n{  "; 

        if (comments != null) 
        { res = res + "/* " + comments + " */\n  "; } 

        if (ent != null && ent.isInterface())
        { return "  public " + ts + " " + name + genPars + "(" + pars + ");\n"; } 
 
        if (tp != null && !("void".equals(ts)))
        { res = res + ts + " result;\n"; }
        localatts.addAll(parameters);   
        cde.typeCheck(types,entities,context,localatts); 
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + epars + " " + ex + " = this;\n  "; }   
        return res + cde.updateFormCSharp(env0,true) + "\n  }";
      }   
    } 
    else 
    if (activity != null) 
    { Vector localatts = new Vector(); 
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { env0.put(ename,ex); } //  or "this" if not static ??
      String res = ""; 

      Type tp = getResultType(); 
      String ts = "void";

      String header = ""; 

      if (tp != null) 
      { ts = tp.getCSharp(); } 

      if (ent != null && ent.isInterface())
      { return "  public " + ts + " " + name + genPars + "(" + pars + ");\n"; } 

      // if (isSequential())
      // { header = header + "synchronized "; }
      if (isUnsafe())
      { header = header + "unsafe "; } 
      if (isAbstract())
      { header = header + "abstract "; } 
      if (isFinal())
      { header = header + "sealed "; } 
      if (isClassScope() || isStatic())
      { header = header + "static "; } 

      res = "  public " + header + ts + " " + name + genPars + "(" + pars + ")\n  {  "; 

      if (comments != null) 
      { res = res + "/* " + comments + " */\n  "; } 

      if (Statement.hasResultDeclaration(activity))
      {  } 
      else if (tp != null && !("void".equals(ts)))
      { res = res + ts + " result;\n"; }


      localatts.addAll(parameters);   
      Vector preterms = activity.allPreTerms(); 
      if (preterms.size() > 0) 
      { Statement newpost = (Statement) activity.clone(); 
        // System.out.println("Pre terms: " + preterms); 
        Vector processed = new Vector(); 
        String newdecs = ""; 
        for (int i = 0; i < preterms.size(); i++)
        { BasicExpression preterm = (BasicExpression) preterms.get(i);
          if (processed.contains(preterm)) { continue; }  
          Type typ = preterm.getType();  // but actual type may be list if multiple
          Type actualtyp; 
          String newdec = ""; 
          String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
          String pretermqf = preterm.classqueryFormCSharp(env0,true); 
          BasicExpression prebe = new BasicExpression(pre_var); 

          if (preterm.isMultiple())
          { if (preterm.isOrdered() || preterm.isSequenceValued())
            { actualtyp = new Type("Sequence",null); } 
            else 
            { actualtyp = new Type("Set",null); } 
            actualtyp.setElementType(preterm.getElementType()); 
            newdec = actualtyp.getCSharp() + " " + pre_var + " = new ArrayList();\n" + 
                     "    " + pre_var + ".AddRange(" + pretermqf + ");\n"; 
          } 
          else 
          { actualtyp = typ;
            newdec = actualtyp.getCSharp() + " " + pre_var + " = " + pretermqf + ";\n";
          } 
          newdecs = newdecs + "    " + newdec; 
          prebe.type = actualtyp; 
          prebe.elementType = preterm.elementType; 
          // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
          //                    " elemtype= " + prebe.elementType); 
          
          Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
          preatt.setElementType(preterm.elementType); 
          localatts.add(preatt); 
          newpost = newpost.substituteEq("" + preterm,prebe); 
          processed.add(preterm); 
        }  // substitute(preterm,prebe) more appropriate 

        newpost.typeCheck(types,entities,context,localatts);
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + epars + " " + ex + " = this;\n  "; }   
        return res + newdecs + newpost.updateFormCSharp(env0,false) + "\n  }\n";   
      }     // updateForm(, true)? 
      activity.typeCheck(types,entities,context,localatts);
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { res = res + ename + epars + " " + ex + " = this;\n  "; }   
      return res + activity.updateFormCSharp(env0,false) + "\n  }\n";   
    } 
      
    String resT = "void";
    if (post == null) 
    { return ""; }   // or use the SM if one exists
    if (resultType != null)
    { resT = resultType.getCSharp(); }

    if (ent != null && ent.isInterface())
    { return "  public " + resT + " " + name + genPars + "(" + pars + ");\n"; } 

    if (query)
    { return buildQueryOpCSharp(ent,name,resultType,resT,entities,types); }
    else 
    { return buildUpdateOpCSharp(ent,name,resultType,resT,entities,types); }
  } // ignore return type for update ops for now. 

  // Operation for entity:
  public String getOperationCodeCPP(Entity ent, Vector entities, Vector types, Vector decs)
  { String name = getName();
    Vector context = new Vector();

    String gpars = ent.typeParameterTextCPP(); 

    String ename = "";  
    String eTypePars = ""; 
    Vector etpars = new Vector(); 
 
    if (ent != null) 
    { ename = ent.getName(); 
      context.add(ent); 
      if (ent.hasTypeParameters())
      { etpars.addAll(ent.getTypeParameters()); 
        for (int i = 0; i < etpars.size(); i++) 
        { Type etp = (Type) etpars.get(i); 
          eTypePars = eTypePars + etp.getName(); 
          if (i < etpars.size() - 1)
          { eTypePars = eTypePars + ", "; } 
        } 
      }
    } 

    String ex = ename.toLowerCase() + "x"; 
    java.util.Map env0 = new java.util.HashMap(); 
    String pars = getCPPParameterDec(); 

    String textcode = ""; 
    if (hasText())
    { textcode = text + "\n"; } 

    String opGenPars = ""; 
    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { etpars.addAll(typeParameters); 
      opGenPars = "  template<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        opGenPars = opGenPars + " class " + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { opGenPars = opGenPars + ", "; } 
      } 
      opGenPars = opGenPars + " >\n"; 
    }

    if (etpars.size() > 0)
    { genPars = "template<"; 
      for (int i = 0; i < etpars.size(); i++) 
      { Type tp = (Type) etpars.get(i); 
        genPars = genPars + " class " + tp.getName(); 
        if (i < etpars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 

    String exname = ename; 
    if (eTypePars.length() > 0)
    { exname = ename + "<" + eTypePars + ">"; } 

    String header = ""; 
    if (etpars.size() > 0)
    { header = "  " + genPars + "\n"; }  
    

    /* if ("run".equals(name) && ent.isActive())
    { if (ent.isInterface())
      { return "  public void run();\n"; } 

      String res = "  public void run()\n  { "; 
      if (sm != null) 
      { Statement rc = sm.runCode(); 
        // add run_state : T to ent, and the type. 
        rc.typeCheck(types,entities,context,new Vector()); 
        return res + rc.toStringJava() + "  }\n\n"; 
      } 
      return res + " }\n\n"; 
    }  */ 

    String isstatic = "";  
    if (isClassScope() || isStatic())
    { isstatic = "static "; } 
    else if (isVirtual(ent))
    { isstatic = "virtual "; } 

    if (sm != null) 
    { Statement cde = sm.getSequentialCode(); // sequential code
      if (cde != null) 
      { Vector localatts = new Vector(); 
        localatts.addAll(sm.getAttributes()); 
        String res = ""; 
        Type tp = getResultType(); 
        String ts = "void";
        if (tp != null) 
        { ts = tp.getCPP(elementType); } 
        res = header + "  " + ts + " " + exname + "::" + name + "(" + pars + ")\n  {  "; 
        decs.add(opGenPars + "  " + isstatic + ts + " " + name + "(" + pars + ");\n"); 

        if (comments != null) 
        { res = res + "/* " + comments + " */\n  "; } 

        // if (ent.isInterface())
        // { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 
 
        // if (Statement.hasResultDeclaration(activity))
        // {  } 
        // else 
        if (tp != null && !("void".equals(ts)))
        { res = res + ts + " result;\n"; }
        localatts.addAll(parameters);   
        cde.typeCheck(types,entities,context,localatts); 
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + "* " + ex + " = this;\n  "; }   
        return res + cde.updateFormCPP(env0,true) + "\n  }";
      }   
    } 
    else
    if (activity != null) 
    { Vector localatts = new Vector(); 
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { env0.put(ename,ex); } //  or "this" if not static ??
      String res = ""; 

      Type tp = getResultType(); 
      String ts = "void";

      // String header = ""; 

      Type elemT = getElementType(); 
      if (tp != null) 
      { ts = tp.getCPP(elemT); } 

      String cet = "void*"; 
      if (elemT != null) 
      { cet = elemT.getCPP(elemT.getElementType()); } 

      // if (ent.isInterface())
      // { return "  public " + ts + " " + name + "(" + pars + ");\n"; } 

      // if (isSequential())
      // { header = header + "synchronized "; } 
      // if (isAbstract())
      // { header = header + "abstract "; } 
      // if (isFinal())
      // { header = header + "sealed "; }


      res = header + "  " + ts + " " + exname + "::" + name + "(" + pars + ")\n  {  "; 
      decs.add(opGenPars + "  " + isstatic + ts + " " + name + "(" + pars + ");\n"); 

      if (comments != null) 
      { res = res + "/* " + comments + " */\n  "; } 

      if (Statement.hasResultDeclaration(activity))
      { } 
      else if (tp != null && !("void".equals(ts)))
      { res = res + ts + " result;\n"; }
      localatts.addAll(parameters);   
      Vector preterms = activity.allPreTerms(); 
      if (preterms.size() > 0) 
      { Statement newpost = (Statement) activity.clone(); 
        // System.out.println("Pre terms: " + preterms); 
        Vector processed = new Vector(); 
        String newdecs = ""; 
        for (int i = 0; i < preterms.size(); i++)
        { BasicExpression preterm = (BasicExpression) preterms.get(i);
          if (processed.contains(preterm)) { continue; }  
          Type typ = preterm.getType();  // but actual type may be list if multiple
          Type actualtyp; 
          String newdec = ""; 
          String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
          String pretermqf = preterm.classqueryFormCPP(env0,true); 
          BasicExpression prebe = new BasicExpression(pre_var); 

          if (preterm.isMultiple())
          { Type pelemT = preterm.getElementType(); 
            String pcet = "void*"; 
            if (pelemT != null) 
            { pcet = pelemT.getCPP(pelemT.getElementType()); }
 
            if (preterm.isOrdered() || preterm.isSequenceValued())
            { actualtyp = new Type("Sequence",null);  
              newdec = "vector<" + pcet + ">* " + pre_var + " = new vector<" + pcet + ">();\n" + 
                     "    " + pre_var + "->insert(" + pre_var + "->end(), " + 
                                                      pretermqf + "->begin(), " + 
                                                      pretermqf + "->end());\n";
            }  
            else 
            { actualtyp = new Type("Set",null);  
              newdec = "std::set<" + pcet + ">* " + pre_var + " = new std::set<" + pcet + ">();\n" + 
                     "    " + pre_var + "->insert(" + pretermqf + "->begin(), " + 
                                                      pretermqf + "->end());\n";
            }  
            actualtyp.setElementType(pelemT); 
          } 
          else 
          { actualtyp = typ;
            newdec = actualtyp.getCPP() + " " + pre_var + " = " + pretermqf + ";\n";
          } 
          newdecs = newdecs + "    " + newdec; 
          prebe.type = actualtyp; 
          prebe.elementType = preterm.elementType; 
          // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
          //                    " elemtype= " + prebe.elementType); 
          
          Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
          preatt.setElementType(preterm.elementType); 
          localatts.add(preatt); 
          newpost = newpost.substituteEq("" + preterm,prebe); 
          processed.add(preterm); 
        }  // substitute(preterm,prebe) more appropriate 

        newpost.typeCheck(types,entities,context,localatts);
        if (ent == null || isClassScope() || isStatic()) { } 
        else 
        { res = res + ename + "* " + ex + " = this;\n  "; }   
        return res + newdecs + newpost.updateFormCPP(env0,false) + "\n  }\n";   
      }     // updateForm(, true)? 
      activity.typeCheck(types,entities,context,localatts);
      if (ent == null || isClassScope() || isStatic()) { } 
      else 
      { res = res + ename + "* " + ex + " = this;\n  "; }   
      return res + activity.updateFormCPP(env0,false) + "\n  }\n";   
    } 
      
    String resT = "void";
    if (post == null) 
    { return ""; }   // or use the SM if one exists
    if (resultType != null)
    { resT = resultType.getCPP(getElementType()); }

    // if (ent.isInterface())
    // { return "  public " + resT + " " + name + "(" + pars + ");\n"; } 

    if (query)
    { return buildQueryOpCPP(ent,name,resultType,resT,entities,types,decs); }
    else 
    { return buildUpdateOpCPP(ent,name,resultType,resT,entities,types,decs); }
  } // ignore return type for update ops for now. 

  public String typeParameterList()
  { String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
    return genPars; 
  } 

  public String typeParameterListJava6()
  { String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava6(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
    return genPars; 
  } 

  public String typeParameterListJava7()
  { String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = " <"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getJava7(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 
    return genPars; 
  } 

  public String buildQueryOp(Entity ent, String opname,
                             Type t, String resT, 
                             Vector entities, Vector types)
  { if (resultType == null || "void".equals(resT)) 
    { System.err.println("ERROR: No result type for " + opname); 
      return ""; 
    }
 
    String genPars = typeParameterList(); 
    
    /* Scope scope = post.resultScope();
    BinaryExpression rscope = null; 
    if (scope == null) // no range, must assign result
    { System.err.println("WARNING: No scope for result of " + opname); }
    else 
    { rscope = scope.resultScope; }  */ 
 
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } // But for static should only find static features

    String preheader = ""; 
    
    String javaPars = getJavaParameterDec(); 
    String header = "  public "; 
    if (isSequential())
    { header = header + "synchronized "; } 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 

    String statc = ""; 

    if (isClassScope() || isStatic())
    { header = header + "static "; 
      statc = "static"; 
    } 

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n   "; } 

    java.util.Map env0 = new java.util.HashMap();
    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 
    // This should not carry over to inner static scopes, eg., in a ->select

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL);
    resultatt.setElementType(elementType);  
    atts.add(resultatt); 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.preconditionExpression();
        preheader = "    if (" + npre.queryForm(env0,true) +
                    ") { return result; } \n  "; 
      }
    } 

    // Expression q0 = post.removeExpression(rscope);
    // Expression q = null;
    // if (q0 != null)
    // { q = q0.substituteEq("result",rxbe); } 

    header = header + "  " + resT + " result = " + resultType.getDefault() + ";\n";
    header = header + preheader; 

    String querycases = buildQueryCases(post,"",resT,env0,types,entities,atts); 
	
	System.out.println(">> Query cases for " + post + " are: " + querycases); 
	System.out.println(); 

    if (ent != null && isCached() && parameters.size() == 0 && instanceScope) 
    { ent.addAux("  private static java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      String wresult = Expression.wrap(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(this);\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrap("_cached_result", resultType) + "; \n" + 
             "    return result; \n" + 
             "  }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(this, " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    if (ent != null && isCached() && parameters.size() >= 1) 
    { ent.addAux("  private " + statc + " java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      // Attribute par = (Attribute) parameters.get(0);  
      // String par1 = par.getName();
      String wpar1 = parcats(); // Expression.wrap(par.getType(), par1);  
      String wresult = Expression.wrap(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(" + wpar1 + ");\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrap("_cached_result", resultType) + "; \n" + 
             "    return result; \n" + 
             "  }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(" + wpar1 + ", " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           
 

    return  header + " \n" + querycases + "    return result;\n  }\n"; 
      
    /* if (isEqScope(rscope))
    { rscope.typeCheck(types,entities,atts);  // not resultx though 
      Expression test = null; 
      // if (q instanceof BinaryExpression)
      // { BinaryExpression qbe = (BinaryExpression) q; 
      //   if (qbe.operator.equals("=>"))
      //   { test = qbe.left; }
      // }
      String body = Association.genEventCode(env0,test,rscope,true); 
      return header + "  " + body + "\n" + 
             "    return result;\n  }";
    }  
    if (isInScope(rscope))
    { header = header + " " + resT + " result;\n";
      header = header + preheader; 
      header = header + "    Vector " + rs + " = " +
               getRange(rscope,env0) + ";\n";
      header = header + "    for (int i = 0; i < " +
               rs + ".size(); i++)\n";
      header = header + "    { " + resT + " " + rx +
               " = (" + resT + ") " + rs + ".get(i);\n";
      Expression newpost =
        new BinaryExpression("=",
                    new BasicExpression("result"),rxbe);
      newpost.typeCheck(types,entities,context,atts); 
      if (q != null) 
      { q.typeCheck(types,entities,context,atts); } 
      String body = Association.genEventCode(env0,q,
                                             newpost,true);
      return header + "  " + body + " }\n" + 
             "    return result;\n  }";
    }
    if (isSubScope(rscope))
    { Type elemT = getElementType(rscope);
      if (elemT == null) { return ""; } 
      String jelemT = elemT.getJava(); 
      header = header + "  " + resT + " result = new Vector();\n";
      header = header + preheader; 
      header = header + "    Vector " + rs + " = " +
               getRange(rscope,env0) + ";\n";
      header = header + "    for (int i = 0; i < " +
               rs + ".size(); i++)\n";
      header = header + "    { " + jelemT + " " + rx +
               " = (" + jelemT + ") " + rs + ".get(i);\n";
      Expression newpost =
        new BinaryExpression(":",rxbe,new BasicExpression("result"));
       
      newpost.typeCheck(types,entities,context,atts); 
      if (q != null) 
      { q.typeCheck(types,entities,context,atts); } 
      String body = Association.genEventCode(env0,q,
                                             newpost,true);
      return header + "  " + body + " }\n" + 
             "    return result;\n  }";
    }
    return ""; */ 
  }

  public String buildQueryOpJava6(Entity ent, String opname,
                             Type t, String resT, 
                             Vector entities, Vector types)
  { if (resultType == null || "void".equals(resT)) 
    { System.err.println("ERROR: No result type for " + opname); 
      return ""; 
    }
    
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } // But for static should only find static features

    String preheader = ""; 
    
    // String rx = "resultx";
    // Expression rxbe = new BasicExpression(rx);
    String javaPars = getJava6ParameterDec(); 
    String header = "  public "; 
    if (isSequential())
    { header = header + "synchronized "; } 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 

    String statc = ""; 

    if (isClassScope() || isStatic())
    { header = header + "static "; 
      statc = "static"; 
    } 

    String genPars = typeParameterListJava6();  

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }
 

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n   "; } 

    java.util.Map env0 = new java.util.HashMap();
    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL);
    resultatt.setElementType(elementType);  
    // Attribute resultxatt = new Attribute(rx,t,ModelElement.INTERNAL); 
    // resultxatt.setElementType(elementType);  
    atts.add(resultatt); 
    // atts.add(resultxatt); 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.preconditionExpression();
        preheader = "    if (" + npre.queryFormJava6(env0,true) +
                    ") { return result; } \n  "; 
      }
    } 

    // Expression q0 = post.removeExpression(rscope);
    // Expression q = null;
    // if (q0 != null)
    // { q = q0.substituteEq("result",rxbe); } 

    header = header + "  " + resT + " result = " + resultType.getDefaultJava6() + ";\n";
    header = header + preheader; 

    String querycases = buildQueryCasesJava6(post,"",resT,env0,types,entities,atts); 
      
    if (ent != null && isCached() && parameters.size() == 0 && instanceScope) 
    { ent.addAux("  private static java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      String wresult = Expression.wrapJava6(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(this);\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrapJava6("_cached_result", resultType) + "; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(this, " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    if (ent != null && isCached() && parameters.size() >= 1) 
    { ent.addAux("  private " + statc + " java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      // Attribute par = (Attribute) parameters.get(0);  
      // String par1 = par.getName();
      String wpar1 = parcatsJava6(); 
         // Expression.wrap(par.getType(), par1);  
      String wresult = Expression.wrapJava6(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(" + wpar1 + ");\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrapJava6("_cached_result", resultType) + "; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(" + wpar1 + ", " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           
 
    return header + "  \n" + querycases + "  \n" + 
           "    return result;\n  }\n";       
  }

  public String buildQueryOpJava7(Entity ent, String opname,
                             Type t, String resT, 
                             Vector entities, Vector types)
  { 
    if (resultType == null || "void".equals(resultType + "")) 
    { System.err.println("ERROR: No result type for " + opname); 
      return ""; 
    }
    
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } // But for static should only find static features

    String preheader = ""; 
    
    // String rx = "resultx";
    // Expression rxbe = new BasicExpression(rx);
    String javaPars = getJava7ParameterDec(); 
    String header = "  public "; 
    if (isSequential())
    { header = header + "synchronized "; } 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 

    String statc = ""; 

    if (isClassScope() || isStatic())
    { header = header + "static "; 
      statc = "static"; 
    } 

    String genPars = typeParameterListJava7(); 

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n   "; } 

    java.util.Map env0 = new java.util.HashMap();
    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL);
    resultatt.setElementType(elementType);  
    // Attribute resultxatt = new Attribute(rx,t,ModelElement.INTERNAL); 
    // resultxatt.setElementType(elementType);  
    atts.add(resultatt); 
    // atts.add(resultxatt); 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.preconditionExpression();
        preheader = "    if (" + npre.queryFormJava7(env0,true) +
                    ") { return result; } \n  "; 
      }
    } 

    header = header + "  " + resT + " result = " + resultType.getDefaultJava7() + ";\n";
    header = header + preheader; 

    String querycases = buildQueryCasesJava7(post,"",resT,env0,types,entities,atts); 
      
    if (ent != null && isCached() && parameters.size() == 0 && instanceScope) 
    { ent.addAux("  private static java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      String wresult = Expression.wrap(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(this);\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrap("_cached_result", resultType) + "; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(this, " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    if (ent != null && isCached() && parameters.size() >= 1) 
    { ent.addAux("  private " + statc + " java.util.Map " + opname + "_cache = new java.util.HashMap();\n");
      // Attribute par = (Attribute) parameters.get(0);  
      // String par1 = par.getName();
      String wpar1 = parcats(); // Expression.wrap(par.getType(), par1);  
      String wresult = Expression.wrap(resultType, "result"); 
      return header + 
             "  Object _cached_result = " + opname + "_cache.get(" + wpar1 + ");\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = " + Expression.unwrap("_cached_result", resultType) + "; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache.put(" + wpar1 + ", " + wresult + ");\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           
 
    return header + "  \n" + querycases + "  \n" + 
           "    return result;\n  }\n";       
  }

  public String typeParameterListCSharp()
  { String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        genPars = genPars + tp.getCSharp(); 
        if (i < typeParameters.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
    return genPars; 
  } 

  public String buildQueryOpCSharp(Entity ent, String opname,
                             Type t, String resT, 
                             Vector entities, Vector types)
  { if (resultType == null || "void".equals(resultType + "")) 
    { System.err.println("ERROR: No result type for " + opname); 
      return ""; 
    }
    
    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } // But for static should only find static features

    String preheader = ""; 
    
    // String rx = "resultx";
    // Expression rxbe = new BasicExpression(rx);
    String javaPars = getCSharpParameterDec(); 
    String header = "  public "; 
    // if (isSequential())
    // { header = header + "synchronized "; }

    if (isUnsafe())
    { header = header + "unsafe "; } 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "sealed "; } 
    String statc = ""; 

    if (isClassScope() || isStatic())
    { header = header + "static "; 
      statc = "static"; 
    } 

    String genPars = typeParameterListCSharp(); 

    if (isAbstract())
    { header = header + resT + " " +
               opname + genPars + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + resT + " " +
             opname + genPars + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n   "; } 

    java.util.Map env0 = new java.util.HashMap();
    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL);
    resultatt.setElementType(elementType);  
    // Attribute resultxatt = new Attribute(rx,t,ModelElement.INTERNAL); 
    // resultxatt.setElementType(elementType);  
    atts.add(resultatt); 
    // atts.add(resultxatt); 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.preconditionExpression();
        preheader = "    if (" + npre.queryFormCSharp(env0,true) +
                    ") { return result; } \n  "; 
      }
    } 

    // Expression q0 = post.removeExpression(rscope);
    // Expression q = null;
    // if (q0 != null)
    // { q = q0.substituteEq("result",rxbe); } 

    header = header + "  " + resT + " result = " + resultType.getDefaultCSharp() + ";\n";
    header = header + preheader; 

    String querycases = buildQueryCasesCSharp(post,"",resT,env0,types,entities,atts); 
      
    if (ent != null && isCached() && parameters.size() == 0 && instanceScope) 
    { ent.addAux("  private static Hashtable " + opname + "_cache = new Hashtable();\n");
      String wresult = "result"; // Expression.wrap(resultType, "result"); 
      String tcsh = resultType.getCSharp(); 
      return header + 
             "  object _cached_result = " + opname + "_cache[this];\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = (" + tcsh + ") _cached_result; }\n" +
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache[this] = " + wresult + ";\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    if (ent != null && isCached() && parameters.size() >= 1) 
    { ent.addAux("  private " + statc + " Hashtable " + opname + "_cache = new Hashtable();\n");
      // Attribute par = (Attribute) parameters.get(0);  
      // String par1 = par.getName();
      String wpar1 = parcatsCSharp(); // par1; // Expression.wrap(par.getType(), par1);  
      String wresult = "result"; // Expression.wrap(resultType, "result"); 
      String tcsh = resultType.getCSharp(); 
      return header + 
             "  object _cached_result = " + opname + "_cache[" + wpar1 + "];\n" + 
             "  if (_cached_result != null)\n" + 
             "  { result = (" + tcsh + ") _cached_result; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache[" + wpar1 + "] = " + wresult + ";\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    return header + "  \n  " + querycases + " \n" + 
           "    return result;\n  }\n";       
  }

  public String buildQueryOpCPP(Entity ent, String opname,
                             Type t, String resT, 
                             Vector entities, Vector types, Vector decs)
  { if (resultType == null || "void".equals(resultType + "")) 
    { System.err.println("ERROR: No result type for " + opname); 
      return ""; 
    }
    
    String gpars = ent.typeParameterTextCPP(); 

    String ename = ""; 
    String eTypePars = ""; 
    Vector etpars = new Vector(); 
 
    Vector context = new Vector(); 
    if (ent != null) 
    { ename = ent.getName(); 
      context.add(ent); 
      if (ent.hasTypeParameters())
      { etpars.addAll(ent.getTypeParameters()); 
        for (int i = 0; i < etpars.size(); i++) 
        { Type etp = (Type) etpars.get(i); 
          eTypePars = eTypePars + etp.getName(); 
          if (i < etpars.size() - 1)
          { eTypePars = eTypePars + ", "; } 
        } 
      }
    } 

    String preheader = ""; 
    
    // String rx = "resultx";
    // Expression rxbe = new BasicExpression(rx);
    String javaPars = getCPPParameterDec(); 
    String header = "  "; 
    // if (isSequential())
    // { header = header + "synchronized "; } 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    // if (isFinal())
    // { header = header + "sealed "; } 

    String isstatic = ""; 
    if (isClassScope() || isStatic())
    { isstatic = "static "; } 
    else if (isVirtual(ent))
    { isstatic = "virtual "; } 

    // if (isAbstract())
    // { header = header + resT + " " +
    //                 opname + "(" + javaPars + ");\n\n";
    //   return header; 
    // }

    String opGenPars = ""; 
    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { etpars.addAll(typeParameters); 
      opGenPars = "  template<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        opGenPars = opGenPars + " class " + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { opGenPars = opGenPars + ", "; } 
      } 
      opGenPars = opGenPars + ">\n"; 
    }

    if (etpars.size() > 0)
    { genPars = "template<"; 
      for (int i = 0; i < etpars.size(); i++) 
      { Type tp = (Type) etpars.get(i); 
        genPars = genPars + " class " + tp.getName(); 
        if (i < etpars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 

    String exname = ename; 
    if (eTypePars.length() > 0)
    { exname = ename + "<" + eTypePars + ">"; } 


    if (etpars.size() > 0)
    { header = "  " + genPars + "\n"; }  
    header = header + "  " + resT + " " + exname + "::" + 
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n   "; } 

    String opdec = "";     
    if (typeParameters.size() > 0)
    { opdec = opGenPars; } 
    opdec = opdec + 
            "  " + isstatic + resT + " " + opname + "(" + javaPars + ");\n"; 

    decs.add(opdec); 

    java.util.Map env0 = new java.util.HashMap();
    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ename,"this"); } 

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL);
    resultatt.setElementType(elementType);  
    // Attribute resultxatt = new Attribute(rx,t,ModelElement.INTERNAL); 
    // resultxatt.setElementType(elementType);  
    atts.add(resultatt); 
    // atts.add(resultxatt); 

    String et = "void*"; 
    if (elementType != null) 
    { et = elementType.getCPP(elementType.getElementType()); } 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.preconditionExpression();
        preheader = "    if (" + npre.queryFormCPP(env0,true) +
                    ") { return result; } \n  "; 
      }
    } 

    // Expression q0 = post.removeExpression(rscope);
    // Expression q = null;
    // if (q0 != null)
    // { q = q0.substituteEq("result",rxbe); } 

    header = header + "  " + resT + " result = " + resultType.getDefaultCPP(elementType) + ";\n";
    header = header + preheader; 


    String querycases = buildQueryCasesCPP(post,"",resT,env0,types,entities,atts); 

    if (ent != null && isCached() && parameters.size() == 0 && instanceScope) 
    { String etype = "void*"; 
      if (elementType != null) 
      { etype = elementType.getCPP(); } 
      String tcsh = resultType.getCPP(etype); 

      ent.addAux("  static map<" + ename + "*," + tcsh + "> " + opname + "_cache;\n");
      ent.addPrivateAux("  map<" + ename + "*," + tcsh + "> " + ename + "::" + opname + "_cache;\n");

      String wresult = "result"; // Expression.wrap(resultType, "result"); 
      return header + 
             "  map<" + ename + "*," + tcsh + ">::iterator _cached_pos = " + opname + "_cache.find(this);\n" + 
             "  if (_cached_pos != " + opname + "_cache.end())\n" + 
             "  { result = " + opname + "_cache[this]; }\n" +
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    (" + opname + "_cache*)[this] = " + wresult + ";\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    /* General caching for more than 1 parameter is not possible in C++ */ 

    if (ent != null && isCached() && parameters.size() == 1 && instanceScope) 
    { String etype = "void*"; 
      if (elementType != null) 
      { etype = elementType.getCPP(elementType.getElementType()); } 
      String tcsh = resultType.getCPP(etype); 
      Attribute par = (Attribute) parameters.get(0);  
      String par1 = par.getName();
      String ptyp = par.getType().getCPP();  // must be primitive or String
      // String wpar1 = parcatsCSharp();   
      ent.addAux("  map<" + ptyp + "," + tcsh + "> " + opname + "_cache;\n");
      String wresult = "result";  
      return header + 
             "  map<" + ptyp + "," + tcsh + ">::iterator _cached_pos = " + opname + "_cache.find(" + par1 + ");\n" + 
             "  if (_cached_pos != " + opname + "_cache.end())\n" + 
             "  { result = " + opname + "_cache[" + par1 + "]; }\n" + 
             "  else \n" + 
             "  { " + querycases + "\n" + 
             "    " + opname + "_cache[" + par1 + "] = " + wresult + ";\n" + 
             "  }\n" + 
             "  return result;\n" + 
             " }\n"; 
    }           

    return header + querycases + " \n" + 
           "    return result;\n  }\n";       

  }

  public Statement designQueryList(Vector postconds, 
                                   String resT,
                                   java.util.Map env0, 
                                   Vector types, 
                                   Vector entities, 
                                   Vector atts)
  { if (postconds.size() == 0)
    { return new SequenceStatement(); } 

    Expression postcond = (Expression) postconds.get(0); 
    Statement fst = designBasicCase(postcond,resT,
                                    env0,types,entities,atts);
    if (postconds.size() == 1) 
    { return fst; } 

    Vector ptail = new Vector(); 
    ptail.addAll(postconds); 
    ptail.remove(0); 
 
    if ((postcond instanceof BinaryExpression) &&  
        "=>".equals(((BinaryExpression) postcond).operator))
    { Expression next = (Expression) postconds.get(1); 

      if ((next instanceof BinaryExpression) &&
          "=>".equals(((BinaryExpression) next).operator)) 
      { Statement elsepart = 
           designQueryList(ptail,resT,       
                           env0,types,entities,atts);
        if (fst instanceof ConditionalStatement)  
        { ((ConditionalStatement) fst).setElse(elsepart);
           // Statement.combineIfStatements(fst,elsepart);
          return fst;
        } 
        else 
        { SequenceStatement res = new SequenceStatement(); 
          res.addStatement(fst); res.addStatement(elsepart); 
          return res;
        }   
      } 
      else 
      { Statement stat = 
           designQueryList(ptail,resT,
                           env0,types,entities,atts); 
        SequenceStatement res = new SequenceStatement(); 
        res.addStatement(fst); res.addStatement(stat); 
        return res; 
      } 
    }      
    else 
    { Statement stat = 
         designQueryList(ptail,resT,
                         env0,types,entities,atts); 
      SequenceStatement res = new SequenceStatement(); 
      res.addStatement(fst); 
      res.addStatement(stat); 
      return res;
    }  // sequencing of fst and ptail
  } 

  /* if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond;
      if ("&".equals(pst.operator))
      { Statement fst = designQueryCases(pst.left, resT, env0, types, entities, atts);
        Statement scnd = designQueryCases(pst.right, resT, env0, types, entities, atts);
        if ((pst.left instanceof BinaryExpression) && "=>".equals(((BinaryExpression) pst.left).operator))
        { IfStatement res = new IfStatement(fst, scnd); 
          return res;
        }
        else
        { SequenceStatement ss = new SequenceStatement();
          ss.addStatement(fst);
          ss.addStatement(scnd);
          return ss;
        }
      }
      else if ("=>".equals(pst.operator))
      { Statement body = designQueryCases(pst.right, resT, env0, types, entities, atts);
        IfStatement res = new IfStatement(pst.left, body);
        return res;
      }
    }
    return designBasicCase(postcond, resT, env0, types, entities, atts); 
  } */

  private Statement designBasicCase(Expression pst,
                  String resT, java.util.Map env0,
                  Vector types, Vector entities, Vector atts)
  { if (pst instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) pst; 
      if ("=>".equals(be.operator))
      { Expression test = be.left; 

        Constraint virtualCon = new Constraint(test,be.right); 

        Vector pars = new Vector(); 
        pars.addAll(getParameters()); 
        Vector lvars = new Vector(); 
        virtualCon.secondaryVariables(lvars,pars); 

        Vector allvars = new Vector(); 
        Vector allpreds = new Vector(); 
        Vector qvars1 = new Vector(); 
        Vector lvars1 = new Vector(); 
        Vector pars1 = ModelElement.getNames(pars); 
        Vector v0 = new Vector(); 
        BasicExpression betrue = new BasicExpression(true); 
        v0.add(betrue); 
        v0.add(betrue.clone()); 
        Vector splitante = 
           test.splitToCond0Cond1Pred(v0,pars1,qvars1,
                                 lvars1,allvars,allpreds); 
        // System.out.println(">>> Quantified local = " + qvars1 + " Let local = " + lvars1 + " All: " + allvars); 
       
        Expression ante1 = (Expression) splitante.get(0); 
        Expression ante2 = (Expression) splitante.get(1); 

        // System.out.println(">>> Variable quantifiers: " + ante1); 
        // System.out.println(">>> Assumptions: " + ante2);

        Statement ifpart = 
           // new ImplicitInvocationStatement(be.right); 
           designBasicCase(be.right, resT, env0, 
                           types, entities, atts); 

        if (qvars1.size() > 0 || lvars1.size() > 0)   
                                 // allvars.size() > 0) 
        { Statement forloop = 
              virtualCon.q2LoopsPred(
                        allvars,qvars1,lvars1,ifpart); 
          return forloop; 
        } 

        Statement cs = new ConditionalStatement(test, ifpart);
        System.out.println(); 
        System.out.println(">-->> code for branch " + be); 
        System.out.println(">-->> is: " + cs);
        return cs; 
      } // But may be let definitions and local variables in test. 
      else if ("=".equals(be.operator))
      { Expression beleft = be.left; 
        if (isNoRecursion() &&  
            "result".equals(be.left + ""))
        { 
          if (be.right.isSelfCall(this))
          { // recursive call to operation, replace by 
            // parameter assignements and continue
            ContinueStatement ctn = new ContinueStatement(); 
            Statement assgns = 
                         parameterAssignments(be.right); 
            if (assgns == null) 
            { return ctn; } 
            else 
            { SequenceStatement ss = new SequenceStatement(); 
              ss.addStatements((SequenceStatement) assgns); 
              ss.addStatement(ctn);
              ss.setBrackets(true);  
              return ss; 
            } 
          } 
          else 
          { Statement retr = 
                new ReturnStatement(be.right); 
            return retr;  
          } 
        } 
        else if (env0.containsValue(be.left + "") || 
                 "result".equals(be.left + "") ||
                 ModelElement.getNames(
                       parameters).contains(be.left + ""))
        { return new AssignStatement(be.left, be.right); } 
          // or attribute of ent
        else if (entity != null && 
                 entity.hasFeature(be.left + "")) 
        { return new AssignStatement(be.left, be.right); }
        else // declare it
        { Type t = be.left.getType(); 
          /* JOptionPane.showMessageDialog(null, 
            "Declaring new local variable  " + be.left + " : " + t + "   in:\n" + this,               
            "Implicit variable declaration", 
            JOptionPane.INFORMATION_MESSAGE); */  
          if (t == null) 
          { t = new Type("OclAny", null); }
          CreationStatement cs = 
            new CreationStatement(t.getJava(), be.left + ""); 
          cs.setInstanceType(t); 
          cs.setElementType(t.getElementType()); 
          cs.setFrozen(true); // it must be a constant
          AssignStatement ast = new AssignStatement(be.left, be.right);
          SequenceStatement sst = new SequenceStatement(); 
          sst.addStatement(cs); 
          sst.addStatement(ast); 
          return sst;  
        } 
      } 
    }
    return pst.generateDesign(env0, true);
  }

  // assume it is a conjunction of implications
  private String buildQueryList(Vector postconds, String header,String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { if (postconds.size() == 0)
    { return ""; } 

    Expression postcond = (Expression) postconds.get(0); 
    String fst = buildQueryCases(postcond,header,resT,
                                     env0,types,entities,atts) + " ";
    if (postconds.size() == 1) 
    { return fst; } 
    Vector ptail = new Vector(); 
    ptail.addAll(postconds); 
    ptail.remove(0); 
 
    if ((postcond instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) postcond).operator))
    { fst = fst + " else\n    ";  
      Expression next = (Expression) postconds.get(1); 

      if ((next instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) next).operator)) 
      { return buildQueryList(ptail,fst,resT,
                                           env0,types,entities,atts) + "   "; 
      } 
      else 
      { fst = fst + "  { "; 
            return buildQueryList(ptail,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
      } 
    }      
    else 
    { return buildQueryList(ptail,fst,resT,
                                           env0,types,entities,atts) + "   ";
    }  // sequencing of fst and ptail
  } 

  // assume it is a conjunction of implications
  private String buildQueryCases(Expression postcond, String header, String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond; 
      if ("&".equals(pst.operator))  // and left right both have => as top operator
      { String fst = buildQueryCases(pst.left,header,resT,
                                     env0,types,entities,atts) + " "; 
        if ((pst.left instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) pst.left).operator))
        { fst = fst + " else\n    ";  
          if ((pst.right instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) pst.right).operator)) 
          { return buildQueryCases(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
          } 
          else 
          { fst = fst + "  { "; 
            return buildQueryCases(pst.right,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
          } 
        } 
        else 
        { return buildQueryCases(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
        }
      } 

      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 

      if ("=>".equals(pst.operator))
      { Expression test = pst.left; 
        test.typeCheck(types,entities,context,atts); 
        String qf = test.queryForm(env0,true); 
        // System.out.println(">>-->> " + test + " QUERY FORM= " + qf); 

        Constraint virtualCon = new Constraint(test,pst.right); 

        Vector pars = new Vector(); 
        pars.addAll(getParameters()); 
        Vector lvars = new Vector(); 
        virtualCon.secondaryVariables(lvars,pars); 

        Vector allvars = new Vector(); 
        Vector allpreds = new Vector(); 

        Vector qvars1 = new Vector(); 
        Vector lvars1 = new Vector(); 
        Vector pars1 = ModelElement.getNames(pars); 
        Vector v0 = new Vector(); 
        BasicExpression betrue = new BasicExpression(true); 
        v0.add(betrue); 
        v0.add(betrue.clone()); 
        Vector splitante = test.splitToCond0Cond1Pred(v0,pars1,qvars1,lvars1,allvars,allpreds); 
        System.out.println(); 
        System.out.println(">>> Operation " + getName() + " case parameters = " + 
                            pars1 + " Quantified local = " + 
                            qvars1 + " Let local = " + lvars1 + " All: " + allvars); 
       
        Expression ante1 = (Expression) splitante.get(0); 
        Expression ante2 = (Expression) splitante.get(1); 

        // System.out.println(">>> Operation " + getName() + " case variable quantifiers: " + ante1); 
        // System.out.println(">>> Operation " + getName() + " case assumptions: " + ante2);
        // System.out.println(); 

        if (qvars1.size() > 0 || lvars1.size() > 0) 
        { Statement ifpart = new ImplicitInvocationStatement(pst.right);
             // designBasicCase(pst.right, resT, env0, types, entities, atts); 
          Statement forloop = virtualCon.q2LoopsPred(allvars,qvars1,lvars1,ifpart); 
          // System.out.println(">>> Actual code= " + forloop); 
          String res = header + forloop.updateForm(env0,true,types,entities,atts); 
          // System.out.println(">-->> code for branch " + pst); 
          // System.out.println(">-->> is: " + res);
          return res; 
        } 
        
        header = header + "  if (" + qf + ") \n  { "; 
        String body = buildQueryCases(pst.right,header,resT,
                                      env0,types,entities,atts); 
        String res = body + " \n  }"; 
        // System.out.println(">-->> code for branch " + pst); 
        // System.out.println(">-->> is: " + res);
        return res; 
      } 

      if ("or".equals(pst.operator))
      { return buildQueryCases(pst.left,header,resT,env0,types,entities,atts); } 
      else 
      { return header + basicQueryCase(pst,resT,env0,types,entities,atts); } 
    }
	else if (postcond instanceof ConditionalExpression)
	{ return header + basicQueryCase(postcond,resT,env0,types,entities,atts); }
    return header; 
  } 

  private String buildQueryCasesJava6(Expression postcond, String header,String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond; 
      if ("&".equals(pst.operator))
      { String fst = buildQueryCasesJava6(pst.left,header,resT,
                                     env0,types,entities,atts) + " "; 
        if ((pst.left instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) pst.left).operator))
        { fst = fst + " else\n    ";  
          if ((pst.right instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) pst.right).operator)) 
          { return buildQueryCasesJava6(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
          } 
          else 
          { fst = fst + "  { "; 
            return buildQueryCasesJava6(pst.right,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
          } 
        } 
        else 
        { return buildQueryCasesJava6(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
        }
      } 
      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 

      if ("=>".equals(pst.operator))
      { Expression test = pst.left; 
        test.typeCheck(types,entities,context,atts); 
        String qf = test.queryFormJava6(env0,true); 
        // System.out.println(test + " QUERY FORM= " + qf); 

        header = header + "  if (" + qf + ") \n  { "; 
        String body = buildQueryCasesJava6(pst.right,header,resT,
                                      env0,types,entities,atts); 
        return body + " \n  }"; 
      } 
      if ("or".equals(pst.operator))
      { return buildQueryCasesJava6(pst.left,header,resT,env0,types,entities,atts); } 
      else 
      { return header + basicQueryCaseJava6(pst,resT,env0,types,entities,atts); } 
    }
    else if (postcond instanceof ConditionalExpression)
    { return header + basicQueryCaseJava6(postcond,resT,env0,types,entities,atts); }
    return header; 
  } 

  private String buildQueryCasesJava7(Expression postcond, String header,String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { // System.out.println("JAVA7 QUERY CASES"); 
    if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond; 
      if ("&".equals(pst.operator))
      { String fst = buildQueryCasesJava7(pst.left,header,resT,
                                     env0,types,entities,atts) + " "; 
        if ((pst.left instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) pst.left).operator))
        { fst = fst + " else\n    ";  
          if ((pst.right instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) pst.right).operator)) 
          { return buildQueryCasesJava7(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
          } 
          else 
          { fst = fst + "  { "; 
            return buildQueryCasesJava7(pst.right,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
          } 
        } 
        else 
        { return buildQueryCasesJava7(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
        }
      } 
      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 

      if ("=>".equals(pst.operator))
      { Expression test = pst.left; 
        test.typeCheck(types,entities,context,atts); 
        String qf = test.queryFormJava7(env0,true); 
        // System.out.println(test + " QUERY FORM= " + qf); 

        header = header + "  if (" + qf + ") \n  { "; 
        String body = buildQueryCasesJava7(pst.right,header,resT,
                                      env0,types,entities,atts); 
        return body + " \n  }"; 
      } 
      if ("or".equals(pst.operator))
      { return buildQueryCasesJava7(pst.left,header,resT,env0,types,entities,atts); } 
      else 
      { return header + basicQueryCaseJava7(pst,resT,env0,types,entities,atts); } 
    }
    else if (postcond instanceof ConditionalExpression)
    { return header + basicQueryCaseJava7(postcond,resT,env0,types,entities,atts); }
    return header; 
  } 

  private String buildQueryCasesCSharp(Expression postcond, String header, String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond; 
      if ("&".equals(pst.operator))
      { String fst = buildQueryCasesCSharp(pst.left,header,resT,
                                     env0,types,entities,atts) + " "; 
        if ((pst.left instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) pst.left).operator))
        { fst = fst + " else\n    ";  
          if ((pst.right instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) pst.right).operator)) 
          { return buildQueryCasesCSharp(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
          } 
          else 
          { fst = fst + "  { "; 
            return buildQueryCasesCSharp(pst.right,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
          } 
        } 
        else 
        { return buildQueryCasesCSharp(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
        }
      } 

      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 

      if ("=>".equals(pst.operator))
      { Expression test = pst.left; 
        test.typeCheck(types,entities,context,atts); 
        String qf = test.queryFormCSharp(env0,true); 
        // System.out.println(test + " QUERY FORM= " + qf); 

        header = header + "  if (" + qf + ") \n  { "; 
        String body = buildQueryCasesCSharp(pst.right,header,resT,
                                      env0,types,entities,atts); 
        return body + " \n  }"; 
      } 
      if ("or".equals(pst.operator))
      { return buildQueryCasesCSharp(pst.left,header,resT,env0,types,entities,atts); } 
      else 
      { return header + basicQueryCaseCSharp(pst,resT,env0,types,entities,atts); } 
    }
    else if (postcond instanceof ConditionalExpression)
    { return header + basicQueryCaseCSharp(postcond,resT,env0,types,entities,atts); }
    return header; 
  } 

  private String buildQueryCasesCPP(Expression postcond, String header,String resT,
                                 java.util.Map env0, 
                                 Vector types, Vector entities, Vector atts)
  { if (postcond instanceof BinaryExpression)
    { BinaryExpression pst = (BinaryExpression) postcond; 
      if ("&".equals(pst.operator))
      { String fst = buildQueryCasesCPP(pst.left,header,resT,
                                     env0,types,entities,atts) + " "; 
        if ((pst.left instanceof BinaryExpression) &&  
            "=>".equals(((BinaryExpression) pst.left).operator))
        { fst = fst + " else\n    ";  
          if ((pst.right instanceof BinaryExpression) &&
              "=>".equals(((BinaryExpression) pst.right).operator)) 
          { return buildQueryCasesCPP(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
          } 
          else 
          { fst = fst + "  { "; 
            return buildQueryCasesCPP(pst.right,fst,resT,
                                           env0,types,entities,atts) + "\n   } "; 
          } 
        } 
        else 
        { return buildQueryCasesCPP(pst.right,fst,resT,
                                           env0,types,entities,atts) + "   "; 
        }      
      } 

      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 

      if ("=>".equals(pst.operator))
      { Expression test = pst.left; 
        test.typeCheck(types,entities,context,atts); 
        String qf = test.queryFormCPP(env0,true); 
        // System.out.println(test + " QUERY FORM= " + qf); 

        header = header + "  if (" + qf + ") \n  { "; 
        String body = buildQueryCasesCPP(pst.right,header,resT,
                                         env0,types,entities,atts); 
        return body + " \n  }"; 
      } 

      if ("or".equals(pst.operator))
      { return buildQueryCasesCPP(pst.left,header,resT,env0,types,entities,atts); } 
      else 
      { return header + basicQueryCaseCPP(pst,resT,env0,types,entities,atts); } 
    }
    else if (postcond instanceof ConditionalExpression)
    { return header + basicQueryCaseCPP(postcond,resT,env0,types,entities,atts); }

    return header; 
  } 



  private String basicQueryCase(Expression pst,String resT, java.util.Map env0,
                                Vector types, Vector entities, Vector atts)
  { // Scope scope = pst.resultScope(); 
   
    // if (scope == null) 
     if (pst instanceof BinaryExpression) 
     { BinaryExpression be = (BinaryExpression) pst; 
       if ("=".equals(be.operator))
       { Expression beleft = be.left; 
         if (env0.containsValue(be.left + "") || "result".equals(be.left + "") || 
             ModelElement.getNames(parameters).contains(be.left + ""))
         { return "  " + pst.updateForm(env0,true) + "\n"; } // or attribute of ent
         else if (entity != null && entity.hasFeature(be.left + "")) 
         { return "  " + pst.updateForm(env0,true) + "\n"; }
         else // declare it
         { Type t = be.right.getType(); 
           System.out.println(">>> Declaring new local variable " + be.left + 
                              " in:\n" + this); 
            // JOptionPane.showMessageDialog(null, 
            //   "Declaring new local variable " + be.left + " in:\n" + this,               
            //   "Implicit variable declaration", JOptionPane.INFORMATION_MESSAGE); 
           return "  final " + t.getJava() + " " + pst.updateForm(env0,true) + " \n  "; 
         } 
       }
       else if ("&".equals(be.operator))
       { String fst = basicQueryCase(be.left, resT, env0, types, entities, atts); 
         return fst + basicQueryCase(be.right, resT, env0, types, entities, atts); 
       }  
     } 
     return "  " + pst.updateForm(env0,true) + "\n  ";
     
   /* BinaryExpression rscope = scope.resultScope; 
    String op = scope.scopeKind; 
    String rx = "resultx";
    Expression rxbe = new BasicExpression(rx);
    String rs = "results";

    Expression q0 = pst.removeExpression(rscope);
    Expression q = null;
    if (q0 != null)
    { q = q0.substituteEq("result",rxbe); } 
    // What is the point of this??? 

    Vector context = new Vector(); 
    if (entity != null) 
    { context.add(entity); } 

    if (op.equals("eq"))
    { rscope.typeCheck(types,entities,context,atts);  // not resultx though 
      Expression test = null; 

      // if (q instanceof BinaryExpression)
      // { BinaryExpression qbe = (BinaryExpression) q; 
      //   if (qbe.operator.equals("=>"))
      //   { test = qbe.left; }
      // }
      String body = Association.genEventCode(env0,test,rscope,true); 
      return "  " + body + "\n";  
            // + "    return result;\n  ";
    }  
    if (op.equals("in"))
    { String res = "    Vector " + rs + " = new Vector();\n" +
                   "    " + rs + ".addAll(" + getRange(rscope,env0) + ");\n";
      res = res + "    for (int i = 0; i < " + rs + ".size(); i++)\n";
      res = res + "    { " + resT + " " + rx + " = (" + resT + ") " + rs + ".get(i);\n";
      Expression newpost =
        new BinaryExpression("=", new BasicExpression("result"), rxbe);
      newpost.typeCheck(types,entities,context,atts); 
      if (q != null) 
      { q.typeCheck(types,entities,context,atts); } 
      String body = Association.genEventCode(env0,q,
                                             newpost,true);
      return res + "  " + body + " }\n" + 
             "    return result;\n  ";
    }
    if (op.equals("subset"))
    { Type elemT = getElementType(rscope);
      if (elemT == null) { return ""; } 
      String jelemT = elemT.getJava(); 
      header = header + "  " + resT + " result = new Vector();\n";
      header = header + preheader; 
      header = header + "    Vector " + rs + " = " +
               getRange(rscope,env0) + ";\n";
      header = header + "    for (int i = 0; i < " +
               rs + ".size(); i++)\n";
      header = header + "    { " + jelemT + " " + rx +
               " = (" + jelemT + ") " + rs + ".get(i);\n";
      Expression newpost =
        new BinaryExpression(":",rxbe,new BasicExpression("result"));
       
      newpost.typeCheck(types,entities,context,atts); 
      if (q != null) 
      { q.typeCheck(types,entities,context,atts); } 
      String body = Association.genEventCode(env0,q,
                                             newpost,true);
      return header + "  " + body + " }\n" + 
             "    return result;\n  }";
    }  
    if (op.equals("array"))
    { BasicExpression arr = (BasicExpression) scope.arrayExp.clone();
      arr.arrayIndex = null; 
      Expression teste = rscope.substituteEq("result",rxbe); 
      arr.typeCheck(types,entities,context,atts); 
      teste.typeCheck(types,entities,context,atts); 
      String e = arr.queryForm(env0,true); 
      String test = teste.queryForm(env0,true); 
      String loop = 
        "  result = 0;\n" + 
        "  for (int resultx = 1; resultx <= " + e + ".size(); resultx++)\n" + 
        "  { if (" + test + ") \n" + 
        "    { result = resultx; \n" + 
        "      return result; \n" +  
        "    } \n" + 
        "  }\n"; 
      return "  " + loop + "  return result;\n";
    }  
    return ""; */ 
  }

  private String basicQueryCaseJava6(Expression pst,String resT, java.util.Map env0,
                                Vector types, Vector entities, Vector atts)
  { Scope scope = pst.resultScope(); 
   
      if (pst instanceof BinaryExpression) 
      { BinaryExpression be = (BinaryExpression) pst; 
        if ("=".equals(be.operator))
        { Expression beleft = be.left; 
          if (env0.containsValue(be.left + "") || "result".equals(be.left + "") ||
              ModelElement.getNames(parameters).contains(be.left + ""))
          { return "  " + pst.updateFormJava6(env0,true) + "\n"; } // or attribute of ent
          else if (entity != null && entity.hasFeature(be.left + "")) 
          { return "  " + pst.updateFormJava6(env0,true) + "\n"; }
          else // declare it
          { Type t = be.right.getType(); 
            /* JOptionPane.showMessageDialog(null, 
              "Declaring new local variable " + be.left + " in:\n" + this,               
              "Implicit variable declaration", JOptionPane.INFORMATION_MESSAGE); */ 

            return "  final " + t.getJava6() + " " + pst.updateFormJava6(env0,true) + " \n  "; 
          } 
        } 
      } 
      return "  " + pst.updateFormJava6(env0,true);
  }

  private String basicQueryCaseJava7(Expression pst,String resT, java.util.Map env0,
                                Vector types, Vector entities, Vector atts)
  { if (pst instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) pst; 
      if ("=".equals(be.operator))
      { Expression beleft = be.left; 
        if (env0.containsValue(be.left + "") || 
            "result".equals(be.left + "") || 
            ModelElement.getNames(parameters).contains(be.left + ""))
        { return "  " + pst.updateFormJava7(env0,true) + "\n"; } // or attribute of ent
        else if (entity != null && entity.hasFeature(be.left + "")) 
        { return "  " + pst.updateFormJava7(env0,true) + "\n"; }
        else // declare it
        { Type t = be.right.getType(); 
          /* JOptionPane.showMessageDialog(null, 
            "Declaring new local variable " + be.left + " : " + t + " in:\n" + this,               
            "Implicit variable declaration", JOptionPane.INFORMATION_MESSAGE); */ 

          return "  final " + t.getJava7(t.getElementType()) + " " + pst.updateFormJava7(env0,true) + " \n  "; 
        } 
      } 
    } 
    return "  " + pst.updateFormJava7(env0,true);
  }

  private String basicQueryCaseCSharp(Expression pst,String resT, java.util.Map env0,
                                Vector types, Vector entities, Vector atts)
  { if (pst instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) pst; 
      if ("=".equals(be.operator))
      { Expression beleft = be.left; 
        if (env0.containsValue(be.left + "") || "result".equals(be.left + "") || 
            ModelElement.getNames(parameters).contains(be.left + ""))
        { return "  " + pst.updateFormCSharp(env0,true); } // or attribute of ent
        else if (entity != null && entity.hasFeature(be.left + "")) 
        { return "  " + pst.updateFormCSharp(env0,true); }
        else // declare it
        { Type t = be.left.getType(); 
          /* JOptionPane.showMessageDialog(null, 
            "Declaring new local variable " + be.left + " in:\n" + this,               
            "Implicit variable declaration", JOptionPane.INFORMATION_MESSAGE); */ 

          return "  " + t.getCSharp() + " " + pst.updateFormCSharp(env0,true) + " \n  "; 
        } 
      } 
    } 
    return "  " + pst.updateFormCSharp(env0,true); 
  }

  private String basicQueryCaseCPP(Expression pst,String resT, java.util.Map env0,
                                   Vector types, Vector entities, Vector atts)
  { if (pst instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) pst; 
      if ("=".equals(be.operator))
      { Expression beleft = be.left; 
        if (env0.containsValue(be.left + "") || "result".equals(be.left + "") || 
            ModelElement.getNames(parameters).contains(be.left + ""))
        { return "  " + pst.updateFormCPP(env0,true); } // or attribute of ent
        else if (entity != null && entity.hasFeature(be.left + "")) 
        { return "  " + pst.updateFormCPP(env0,true); }
        else // declare it
        { Type t = be.left.getType(); 
          /* JOptionPane.showMessageDialog(null, 
            "Declaring new local variable " + be.left + " in:\n" + this,               
            "Implicit variable declaration", JOptionPane.INFORMATION_MESSAGE); */ 
 
          return "  " + t.getCPP(t.getElementType()) + " " + pst.updateFormCPP(env0,true) + " \n  "; 
        } 
      } 
    } 
    return "  " + pst.updateFormCPP(env0,true);
  } 


  // Controller version of the operation -- only for instance operations. Should be 
  // for static also? 

  public String getGlobalOperationCode(Entity ent,Vector entities, Vector types,
                                       Vector constraints)
  { String name = getName();
    String resT = "void";
    if (resultType != null)
    { resT = resultType.getJava(); }
    if (post == null)
    { return ""; }
    if (query) 
    { return buildGlobalQueryOp(ent,name,resultType,resT,
                                entities,types); 
    }
    else  // including for abstract ones
    { return buildGlobalUpdateOp(ent,name,resultType,resT,
                                 entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 

  public String getGlobalOperationCodeJava6(Entity ent,Vector entities, Vector types,
                                       Vector constraints)
  { String name = getName();
    String resT = "void";
    if (resultType != null)
    { resT = resultType.getJava6(); }
    if (post == null)
    { return ""; }
    if (query) 
    { return buildGlobalQueryOpJava6(ent,name,resultType,resT,
                                entities,types); 
    }
    else  // including for abstract ones
    { return buildGlobalUpdateOpJava6(ent,name,resultType,resT,
                                 entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 

  public String getGlobalOperationCodeJava7(Entity ent,Vector entities, Vector types,
                                       Vector constraints)
  { String name = getName();
    String resT = "void";
    if (resultType != null)
    { resT = resultType.getJava7(elementType); } // may need the wrapper type down below. 
    if (post == null)
    { return ""; }
    if (query) 
    { return buildGlobalQueryOpJava7(ent,name,resultType,resT,
                                entities,types); 
    }
    else  // including for abstract ones
    { return buildGlobalUpdateOpJava7(ent,name,resultType,resT,
                                 entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 


  // Controller version of the operation
  public String getGlobalOperationCodeCSharp(Entity ent,Vector entities, Vector types,
                                       Vector constraints)
  { // if (!instanceScope) { return ""; } 

    if (isUnsafe()) { return ""; } 

    String name = getName();
    String resT = "void";
    if (resultType != null)
    { resT = resultType.getCSharp(); }
    if (post == null)
    { return ""; }
    if (query) 
    { return buildGlobalQueryOpCSharp(ent,name,resultType,resT,
                                entities,types); 
    }
    else  // including for abstract ones
    { return buildGlobalUpdateOpCSharp(ent,name,resultType,resT,
                                 entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 

  // Controller version of the operation
  public String getGlobalOperationCodeCPP(Entity ent,Vector entities, Vector types,
                                       Vector constraints, Vector declarations)
  { if (!instanceScope) { return ""; } 

    String name = getName();
    String resT = "void";
    if (resultType != null)
    { resT = resultType.getCPP(getElementType()); }
    if (post == null)
    { return ""; }
    if (query) 
    { return buildGlobalQueryOpCPP(ent,name,resultType,resT,declarations, 
                                entities,types); 
    }
    else  // including for abstract ones
    { return buildGlobalUpdateOpCPP(ent,name,resultType,resT,declarations, 
                                 entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 


  public String buildUpdateOp(Entity ent, String opname,
                              Type t, String resT, 
                              Vector entities, Vector types)
  { String preheader = ""; 
    String javaPars = getJavaParameterDec(); 
    
    // System.out.println(">>> Building update op for " + opname + " : " + t + " >> " + resT); 
	
    String header = "  public "; 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    if (isClassScope() || isStatic())
    { header = header + "static "; } 

    String genPars = typeParameterList(); 

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n  "; } 

    java.util.Map env0 = new java.util.HashMap();

    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 

    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "  " + resT + " result = " + resultType.getDefault() + ";\n"; 
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.computeNegation4succ();
        Expression npre1 = npre.removePrestate();  
        preheader = "  //  if (" + npre1.queryForm(env0,true) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 

    Expression newpost = (Expression) post.clone(); 
    Vector preterms = post.allPreTerms(); 
    // System.out.println("Pre terms: " + preterms); 
    // substitute these by new variables pre_var in post, copy types/multiplicities
    // of preterm variables. Have initial assignments pre_var = var.queryForm();
    // for each. Can optimise by ignoring vars not on any lhs

    Vector processed = new Vector(); 

    for (int i = 0; i < preterms.size(); i++)
    { BasicExpression preterm = (BasicExpression) preterms.get(i); 
      if (processed.contains(preterm)) { continue; } 

      Type typ = preterm.getType();  // but actual type may be list if multiple
      Type actualtyp; 
      String newdec = ""; 
      String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
      String pretermqf = preterm.queryForm(env0,true); 
      BasicExpression prebe = new BasicExpression(pre_var); 
      
      if (preterm.isMultiple())
      { if (preterm.isOrdered() || preterm.isSequenceValued())
        { actualtyp = new Type("Sequence",null); } 
        else 
        { actualtyp = new Type("Set",null); } 
        actualtyp.setElementType(preterm.getElementType()); 
 
        if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
        { pretermqf = "Controller.inst()." + pretermqf.toLowerCase() + "s"; } 
        newdec = actualtyp.getJava() + " " + pre_var + " = new Vector();\n" + 
                 "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
      } 
      else 
      { actualtyp = typ;
        newdec = actualtyp.getJava() + " " + pre_var + " = " + pretermqf + ";\n";
      } 
      newdecs = newdecs + "    " + newdec; 
      prebe.type = actualtyp; 
      prebe.elementType = preterm.elementType; 
      Attribute preatt = new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
      // System.out.println("****** PRE variable " + prebe + " type= " + actualtyp + 
      //                    " elemtype= " + prebe.elementType + " VALUE= " + pretermqf); 
      preatt.setElementType(preterm.elementType); 
      atts.add(preatt); 
      newpost = // newpost.substitute(preterm,prebe); 
                newpost.substituteEq("" + preterm,prebe); 
      processed.add(preterm); 
    }  // substitute(preterm,prebe) more appropriate

    newpost.typeCheck(types,entities,context,atts);  
    String body = Association.genEventCode(env0,null,newpost,false);
    if (returning.length() > 0)
    { body = body + "\n  return" + returning + ";\n"; }

    String res = header + newdecs + preheader + body + "\n" + "  }";
    return res; 
  }

  public String buildUpdateOpJava6(Entity ent, String opname,
                              Type t, String resT, 
                              Vector entities, Vector types)
  { String preheader = ""; 
    String javaPars = getJava6ParameterDec(); 
    
    String header = "  public "; 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    if (isClassScope() || isStatic())
    { header = header + "static "; } 

    String genPars = typeParameterListJava6(); 

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n  "; } 

    java.util.Map env0 = new java.util.HashMap();

    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 

    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "  " + resT + " result = " + resultType.getDefaultJava6() + ";\n"; 
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.computeNegation4succ(); 
	    Expression npre1 = npre.removePrestate();  
        
        preheader = "  //  if (" + npre1.queryFormJava6(env0,true) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 

    Expression newpost = (Expression) post.clone(); 
    Vector preterms = post.allPreTerms(); 
    // System.out.println("Pre terms: " + preterms); 
    // substitute these by new variables pre_var in post, copy types/multiplicities
    // of preterm variables. Have initial assignments pre_var = var.queryForm();
    // for each. Can optimise by ignoring vars not on any lhs

    Vector processed = new Vector(); 

    for (int i = 0; i < preterms.size(); i++)
    { BasicExpression preterm = (BasicExpression) preterms.get(i); 
      if (processed.contains(preterm)) { continue; } 

      Type typ = preterm.getType();  // but actual type may be list if multiple
      Type actualtyp; 
      String newdec = ""; 
      String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
      String pretermqf = preterm.queryFormJava6(env0,true); 
      BasicExpression prebe = new BasicExpression(pre_var); 
      
      // System.out.println(">>> Preterm " + preterm + " type = " + typ); 
      // System.out.println(">>> isOrdered: " + preterm.isOrdered() + " isMultiple: " + preterm.isMultiple() + " isSequenceValued: " + preterm.isSequenceValued()); 

      if (preterm.isMultiple())
      { if (preterm.isOrdered() || preterm.isSequenceValued())
        { actualtyp = new Type("Sequence", null); } 
        else 
        { actualtyp = new Type("Set",null); } 
        actualtyp.setElementType(preterm.getElementType()); 

        

        if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
        { pretermqf = "Controller.inst()." + pretermqf.toLowerCase() + "s"; } 
        String j6type = actualtyp.getJava6(); 
        newdec = j6type + " " + pre_var + " = new " + j6type + "();\n" + 
                 "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
      } 
      else 
      { actualtyp = typ;
        newdec = actualtyp.getJava6() + " " + pre_var + " = " + pretermqf + ";\n";
      } 
      newdecs = newdecs + "    " + newdec; 
      prebe.type = actualtyp; 
      prebe.elementType = preterm.elementType; 
      Attribute preatt = new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
      // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
      //                    " elemtype= " + prebe.elementType); 
      preatt.setElementType(preterm.elementType); 
      atts.add(preatt); 
      newpost = newpost.substituteEq("" + preterm,prebe); 
      processed.add(preterm); 
    }  // substitute(preterm,prebe) more appropriate

    newpost.typeCheck(types,entities,context,atts);  
    String body = Association.genEventCodeJava6(env0,null,newpost,false);
    if (returning.length() > 0)
    { body = body + "\n  return" + returning + ";\n"; }

    String res = header + newdecs + preheader + body + "\n" + "  }";
    return res; 
  }

  public String buildUpdateOpJava7(Entity ent, String opname,
                              Type t, String resT, 
                              Vector entities, Vector types)
  { String preheader = ""; 
    String javaPars = getJava7ParameterDec(); 
    
    String header = "  public "; 
    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    if (isClassScope() || isStatic())
    { header = header + "static "; } 

    String genPars = typeParameterListJava7(); 

    if (isAbstract())
    { header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + genPars + resT + " " +
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n  "; } 

    java.util.Map env0 = new java.util.HashMap();

    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 

    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "  " + resT + " result = " + resultType.getDefaultJava7() + ";\n"; 
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.computeNegation4succ();
        Expression npre1 = npre.removePrestate();  
         
        preheader = "  //  if (" + npre1.queryFormJava7(env0,true) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 

    Expression newpost = (Expression) post.clone(); 
    Vector preterms = post.allPreTerms(); 
    // System.out.println("Pre terms: " + preterms); 
    // substitute these by new variables pre_var in post, copy types/multiplicities
    // of preterm variables. Have initial assignments pre_var = var.queryForm();
    // for each. Can optimise by ignoring vars not on any lhs

    Vector processed = new Vector(); 

    for (int i = 0; i < preterms.size(); i++)
    { BasicExpression preterm = (BasicExpression) preterms.get(i); 
      if (processed.contains(preterm)) { continue; } 

      Type typ = preterm.getType();  // but actual type may be list if multiple
      Type actualtyp; 
      String newdec = ""; 
      String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
      String pretermqf = preterm.queryFormJava7(env0,true); 
      BasicExpression prebe = new BasicExpression(pre_var);
      prebe.setUmlKind(Expression.VARIABLE);  
      
      if (preterm.isMultiple())
      { if (preterm.isOrdered() || preterm.isSequenceValued())
        { actualtyp = new Type("Sequence", null); } 
        else 
        { actualtyp = new Type("Set",null); } 
        actualtyp.setElementType(preterm.getElementType()); 

        if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
        { pretermqf = "Controller.inst()." + pretermqf.toLowerCase() + "s"; } 

        actualtyp.setSorted(preterm.isSorted()); 
        prebe.setSorted(preterm.isSorted()); 
        // System.out.println(">> Type of " + preterm + " = " + preterm.type + " { " + preterm.isSorted() + " }");

        String j7type = 
              actualtyp.getJava7(preterm.elementType); 
        newdec = j7type + " " + pre_var + " = new " + j7type + "();\n" + 
                 "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
      } 
      else 
      { actualtyp = typ;
        newdec = actualtyp.getJava7(preterm.elementType) + " " + pre_var + " = " + pretermqf + ";\n";
      } 

      newdecs = newdecs + "    " + newdec; 
      prebe.type = actualtyp; 
      prebe.elementType = preterm.elementType; 
      Attribute preatt = new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
      // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
      //                    " elemtype= " + prebe.elementType); 
      preatt.setElementType(preterm.elementType); 
      atts.add(preatt); 
      newpost = newpost.substituteEq("" + preterm,prebe); 
      processed.add(preterm); 
    }  // substitute(preterm,prebe) more appropriate

    newpost.typeCheck(types,entities,context,atts);  
    String body = Association.genEventCodeJava7(env0,null,newpost,false);
    if (returning.length() > 0)
    { body = body + "\n  return" + returning + ";\n"; }

    String res = header + newdecs + preheader + body + "\n" + "  }";
    return res; 
  }


  public String buildUpdateOpCSharp(Entity ent, String opname,
                              Type t, String resT, 
                              Vector entities, Vector types)
  { String preheader = ""; 
    String javaPars = getCSharpParameterDec(); 
    
    String header = "  public "; 

    if (isUnsafe())
    { header = header + "unsafe "; } 

    if (isAbstract())
    { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "sealed "; } 
    if (isClassScope() || isStatic())
    { header = header + "static "; } 

    String genPars = typeParameterListCSharp(); 

    if (isAbstract())
    { header = header + resT + " " +
                    opname + genPars + "(" + javaPars + ");\n\n";
      return header; 
    }

    header = header + resT + " " +
                    opname + genPars + 
                    "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n  "; } 

    java.util.Map env0 = new java.util.HashMap();

    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 

    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "  " + resT + " result = " + resultType.getDefaultCSharp() + ";\n"; 
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.computeNegation4succ();
	    Expression npre1 = npre.removePrestate();  
         
        preheader = "  //  if (" + npre1.queryFormCSharp(env0,true) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 

    Expression newpost = (Expression) post.clone(); 
    Vector preterms = post.allPreTerms(); 
    // System.out.println("Pre terms: " + preterms); 
    // substitute these by new variables pre_var in post, copy types/multiplicities
    // of preterm variables. Have initial assignments pre_var = var.queryForm();
    // for each. Can optimise by ignoring vars not on any lhs

    Vector processed = new Vector(); 

    for (int i = 0; i < preterms.size(); i++)
    { BasicExpression preterm = (BasicExpression) preterms.get(i); 
      if (processed.contains(preterm)) { continue; } 

      Type typ = preterm.getType();  // but actual type may be list if multiple
      Type actualtyp; 
      String newdec = ""; 
      String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
      String pretermqf = preterm.queryFormCSharp(env0,true); 
      BasicExpression prebe = new BasicExpression(pre_var); 
      
      if (preterm.isMultiple())
      { if (preterm.isOrdered() || preterm.isSequenceValued())
        { actualtyp = new Type("Sequence",null); } 
        else
        { actualtyp = new Type("Set",null); }
        actualtyp.setElementType(preterm.getElementType()); 
 
        if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
        { pretermqf = "Controller.inst().get" + pretermqf.toLowerCase() + "_s()"; } 
        newdec = actualtyp.getCSharp() + " " + pre_var + " = new ArrayList();\n" + 
                 "    " + pre_var + ".AddRange(" + pretermqf + ");\n"; 
      } 
      else 
      { actualtyp = typ;
        newdec = actualtyp.getCSharp() + " " + pre_var + " = " + pretermqf + ";\n";
      } 
      newdecs = newdecs + "    " + newdec; 
      prebe.type = actualtyp; 
      prebe.elementType = preterm.elementType; 
      Attribute preatt = new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
      // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
      //                    " elemtype= " + prebe.elementType); 
      preatt.setElementType(preterm.elementType); 
      atts.add(preatt); 
      newpost = newpost.substituteEq("" + preterm,prebe); 
      processed.add(preterm); 
    }  // substitute(preterm,prebe) more appropriate

    newpost.typeCheck(types,entities,context,atts);  
    String body = Association.genEventCodeCSharp(env0,null,newpost,false);
    if (returning.length() > 0)
    { body = body + "\n  return" + returning + ";\n"; }

    String res = header + newdecs + preheader + body + "\n" + "  }";
    return res; 
  }

  public String buildUpdateOpCPP(Entity ent, String opname,
                              Type t, String resT, 
                              Vector entities, Vector types, Vector decs)
  { String preheader = ""; 
    String javaPars = getCPPParameterDec(); 
    
    String ename = ""; 
    String eTypePars = ""; 
    Vector etpars = new Vector(); 
 
    String gpars = ent.typeParameterTextCPP(); 

    Vector context = new Vector(); 
    if (ent != null) 
    { ename = ent.getName(); 
      context.add(ent); 
      if (ent.hasTypeParameters())
      { etpars.addAll(ent.getTypeParameters()); 
        for (int i = 0; i < etpars.size(); i++) 
        { Type etp = (Type) etpars.get(i); 
          eTypePars = eTypePars + etp.getName(); 
          if (i < etpars.size() - 1)
          { eTypePars = eTypePars + ", "; } 
        } 
      }
    } 

    String header = ""; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    // if (isFinal())
    // { header = header + "sealed "; } 

    String isstatic = ""; 
    if (isClassScope() || isStatic())
    { isstatic = "static "; } 
    else if (isVirtual(ent))
    { isstatic = "virtual "; } 

    // if (isAbstract())
    // { header = header + resT + " " +
    //                 opname + "(" + javaPars + ");\n\n";
    //   return header; 
    // }

    String opGenPars = ""; 
    String genPars = ""; 
    if (typeParameters != null && typeParameters.size() > 0)
    { etpars.addAll(typeParameters); 
      opGenPars = "  template<"; 
      for (int i = 0; i < typeParameters.size(); i++) 
      { Type tp = (Type) typeParameters.get(i); 
        opGenPars = opGenPars + " class " + tp.getName(); 
        if (i < typeParameters.size() - 1) 
        { opGenPars = opGenPars + ", "; } 
      } 
      opGenPars = opGenPars + ">\n"; 
    }

    if (etpars.size() > 0)
    { genPars = "template<"; 
      for (int i = 0; i < etpars.size(); i++) 
      { Type tp = (Type) etpars.get(i); 
        genPars = genPars + " class " + tp.getName(); 
        if (i < etpars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + "> "; 
    } 

    String exname = ename; 
    if (eTypePars.length() > 0)
    { exname = ename + "<" + eTypePars + ">"; } 


    if (etpars.size() > 0)
    { header = "  " + genPars + "\n"; }  
    header = header + "  " + resT + " " + exname + "::" + 
                    opname + "(" + javaPars + ")\n  { ";

    if (comments != null) 
    { header = header + "/* " + comments + " */\n  "; } 

    decs.add(opGenPars + "  " + isstatic + resT + " " + opname + "(" + javaPars + ");\n"); 
 
    java.util.Map env0 = new java.util.HashMap();

    if (ent == null || isClassScope() || isStatic()) { } 
    else 
    { env0.put(ent.getName(),"this"); } 

    Vector atts = new Vector(); 
    atts.addAll(parameters); 

    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String et = "void*"; 
    if (elementType != null) 
    { et = elementType.getCPP(elementType.getElementType()); } 

    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "  " + resT + " result = " + resultType.getDefaultCPP(elementType) + ";\n"; 
      returning = " result"; 
    }

    if (pre != null) 
    { pre.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre)) { } 
      else 
      { Expression npre = pre.computeNegation4succ();
	    Expression npre1 = npre.removePrestate();  
         
        preheader = "  //  if (" + npre1.queryFormCPP(env0,true) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 

    Expression newpost = (Expression) post.clone(); 
    Vector preterms = post.allPreTerms(); 
    // System.out.println("Pre terms: " + preterms); 
    // substitute these by new variables pre_var in post, copy types/multiplicities
    // of preterm variables. Have initial assignments pre_var = var.queryForm();
    // for each. Can optimise by ignoring vars not on any lhs

    Vector processed = new Vector(); 

    for (int i = 0; i < preterms.size(); i++)
    { BasicExpression preterm = (BasicExpression) preterms.get(i); 
      if (processed.contains(preterm)) { continue; } 

      Type typ = preterm.getType();  // but actual type may be list if multiple
      Type actualtyp; 
      String newdec = ""; 
      String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
      String pretermqf = preterm.queryFormCPP(env0,true); 
      BasicExpression prebe = new BasicExpression(pre_var); 
      
      Type preET = preterm.getElementType(); 
      String pcet = "void*"; 
      if (preET != null) 
      { pcet = preET.getCPP(preET.getElementType()); } 

      if (preterm.isMultiple())
      { if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
        { pretermqf = "Controller::inst->get" + pretermqf.toLowerCase() + "_s()"; } 
        if (preterm.isOrdered() || preterm.isSequenceValued())
        { actualtyp = new Type("Sequence",null); 
          newdec =  "  vector<" + pcet + ">* " + pre_var + " = new vector<" + pcet + ">();\n" + 
                 "    " + pre_var + "->insert(" + pre_var + "->end(), " + 
                                              pretermqf + "->begin(), " + 
                                              pretermqf + "->end());\n";
        } 
        else 
        { actualtyp = new Type("Set",null); 
          newdec =  "  std::set<" + pcet + ">* " + pre_var + " = new std::set<" + pcet + ">();\n" + 
                 "    " + pre_var + "->insert(" + pretermqf + "->begin(), " + 
                                              pretermqf + "->end());\n";
        }  
        actualtyp.setElementType(preET); 
      } 
      else 
      { actualtyp = typ;
        newdec = actualtyp.getCPP() + " " + pre_var + " = " + pretermqf + ";\n";
      } 
      newdecs = newdecs + "    " + newdec; 
      prebe.type = actualtyp; 
      prebe.elementType = preterm.elementType; 
      Attribute preatt = new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
      // System.out.println("Pre variable " + prebe + " type= " + actualtyp + 
      //                    " elemtype= " + prebe.elementType); 
      preatt.setElementType(preterm.elementType); 
      atts.add(preatt); 
      newpost = newpost.substituteEq("" + preterm,prebe); 
      processed.add(preterm); 
    }  // substitute(preterm,prebe) more appropriate

    newpost.typeCheck(types,entities,context,atts);  
    String body = Association.genEventCodeCPP(env0,null,newpost,false);
    if (returning.length() > 0)
    { body = body + "\n  return" + returning + ";\n"; }

    String res = header + newdecs + preheader + body + "\n" + "  }";
    return res; 
  }


  public String buildGlobalUpdateOp(Entity ent, String opname,
                                    Type t, String resT, 
                                    Vector entities, Vector types,
                                    Vector constraints)
  { String preheader = ""; 
    if (ent == null) { return ""; } 

    String javaPars = getJavaParameterDec(); 
    String parlist = parameterList(); 
    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 

    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (!instanceScope)
    { if (resT == null || resT.equals("void")) 
      { return "  public static void " + opname + "(" + javaPars + ")\n" + 
               "  { " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
      } 
      return " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "List " + ex + "s"; 
    } 
    else 
    { java2Pars = "List " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    String retT = resT; 
    if (resT == null)
    { retT = "void"; } 

    String header2 = header + " List All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    header = header + retT + " " +
                    opname + "(" + javaPars + ")\n  { ";
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + "\n    List result = new Vector();\n  "; 
      retT = "void"; 
    }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefault() + ";\n  "; 
      header2 = header2 + "\n    List result = new Vector();\n  ";
      returning = " result"; 
    }

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "  //  if (!(" + pre1.queryForm(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    String body = "  " + ex + "." + call + ";"; 
    String endbody = ""; 
    header2 = header2 + "  for (int _i = 0; _i < " + exs + ".size(); _i++)\n" +
              "    { " + ename + " " + ex + " = (" + ename + ") " + exs + ".get(_i);\n"; 

    if (parlist.length() > 0)
    { parlist = "," + parlist; } 

    String newitem = opname + "(" + ex + parlist + ")"; 

    if ("List".equals(resT))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; } // NOT addAll
    else if (resT == null || resT.equals("void")) 
    { header2 = header2 + "      " + newitem + ";\n"; }
    else if (resultType.isPrimitive())
    { newitem = Expression.wrap(resultType,newitem); 
      header2 = header2 + "      result.add(" + newitem + ");\n"; 
    }
    else 
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    if (resT == null || resT.equals("void")) 
    { }
    else
    { body = " result = " + body; 
      endbody = "    return result;"; 
    }

    for (int i = 0; i < constraints.size(); i++)
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname, this);
      if (ccnew != null) 
      { Vector contx = new Vector(); 
        if (ccnew.getOwner() != null) 
        { contx.add(ccnew.getOwner()); } 
        boolean ok = ccnew.typeCheck(types,entities,contx); 
        if (ok)
        { String upd = ccnew.globalUpdateOp(ent,false); 
          body = body + "\n    " + upd;
        } 
      } 
    }  

    // if (isDefinedinSuperclass())
    // { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 
    String gop = header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; 
    if (derived) 
    { return gop; } 
    return gop + header2;
  }

  public String buildGlobalUpdateOpJava6(Entity ent, String opname,
                                    Type t, String resT, 
                                    Vector entities, Vector types,
                                    Vector constraints)
  { String preheader = ""; 
    String javaPars = getJava6ParameterDec(); 
    String parlist = parameterList(); 

    if (ent == null) { return ""; } 
    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    String retT = resT; 
    if (resT == null)
    { retT = "void"; } 

    if (!instanceScope)
    { if (resT == null || resT.equals("void")) 
      { return "  public static void " + opname + "(" + javaPars + ")\n" + 
               "  { " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
      } 
      return " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "Collection " + ex + "s"; 
    } 
    else 
    { java2Pars = "Collection " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    String header2 = header + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    header = header + retT + " " +
                    opname + "(" + javaPars + ")\n  { ";
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + "\n    ArrayList result = new ArrayList();\n  "; }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultJava6() + ";\n  "; 
      header2 = header2 + "\n    ArrayList result = new ArrayList();\n  ";
      returning = " result"; 
    }

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "  //  if (!(" + pre1.queryFormJava6(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    String body = "  " + ex + "." + call + ";"; 
    String endbody = ""; 
    header2 = header2 + "  for (Object _i : " + exs + ")\n" +
              "    { " + ename + " " + ex + " = (" + ename + ") _i;\n"; 

    if (parlist.length() > 0)
    { parlist = "," + parlist; } 

    String newitem = opname + "(" + ex + parlist + ")"; 

    if (resultType != null && Type.isMapType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    else if (resultType != null && 
             Type.isCollectionType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; } // NOT addAll
    else if (resT == null || resT.equals("void")) 
    { header2 = header2 + "      " + newitem + ";\n"; }
    else if (resultType.isPrimitive())
    { newitem = Expression.wrap(resultType,newitem); 
      header2 = header2 + "      result.add(" + newitem + ");\n"; 
    }
    else 
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    if (resT == null || resT.equals("void")) 
    { }
    else
    { body = " result = " + body; 
      endbody = "    return result;"; 
    }

    for (int i = 0; i < constraints.size(); i++)
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname, this);
      if (ccnew != null) 
      { Vector contx = new Vector(); 
        if (ccnew.getOwner() != null) 
        { contx.add(ccnew.getOwner()); } 
        boolean ok = ccnew.typeCheck(types,entities,contx); 
        if (ok)
        { String upd = ccnew.globalUpdateOpJava6(ent,false); 
          body = body + "\n    " + upd;
        } 
      } 
    }  

    // if (isDefinedinSuperclass())
    // { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 
    String gop = header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; 
    if (derived) 
    { return gop; } 
    return gop + header2;
  }

  public String buildGlobalUpdateOpJava7(Entity ent, String opname,
                                    Type t, String resT, 
                                    Vector entities, Vector types,
                                    Vector constraints)
  { String preheader = ""; 
    String javaPars = getJava7ParameterDec(); 
    String parlist = parameterList(); 

    if (ent == null) { return ""; } 
    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (!instanceScope)
    { if (resT == null || resT.equals("void")) 
      { return "  public static void " + opname + "(" + javaPars + ")\n" + 
               "  { " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
      } 
      return " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    Type allrestype = new Type("Sequence", null); 
    allrestype.setElementType(resultType); 
    String alltype = allrestype.getJava7(resultType); 

    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "Collection<" + ename + "> " + ex + "s"; 
    } 
    else 
    { java2Pars = "Collection<" + ename + "> " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    String retT = resT; 
    String header2 = header; 
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { "; 
      retT = "void"; 
    } 
    else 
    { header2 = header2 + " " + alltype + " All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    } 

    header = header + retT + " " +
                    opname + "(" + javaPars + ")\n  { ";
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + "\n    ArrayList result = new ArrayList();\n  "; }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultJava7() + ";\n  "; 
      header2 = header2 + "\n    " + alltype + " result = new " + alltype + "();\n  ";
      returning = " result"; 
    } // wrapper types needed for primitive. 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "  //  if (!(" + pre1.queryFormJava7(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    String body = "  " + ex + "." + call + ";"; 
    String endbody = ""; 
    header2 = header2 + "  for (Object _i : " + exs + ")\n" +
              "    { " + ename + " " + ex + " = (" + ename + ") _i;\n"; 

    if (parlist.length() > 0)
    { parlist = "," + parlist; } 

    String newitem = opname + "(" + ex + parlist + ")"; 

    if (resultType != null && Type.isMapType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    else if (resultType != null && 
             Type.isCollectionType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; } // NOT addAll
    else if (resT == null || resT.equals("void")) 
    { header2 = header2 + "      " + newitem + ";\n"; }
    else if (resultType.isPrimitive())
    { newitem = Expression.wrap(resultType,newitem); 
      header2 = header2 + "      result.add(" + newitem + ");\n"; 
    }
    else 
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    if (resT == null || resT.equals("void")) 
    { }
    else
    { body = " result = " + body; 
      endbody = "    return result;"; 
    }

    for (int i = 0; i < constraints.size(); i++)
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname, this);
      if (ccnew != null) 
      { Vector contx = new Vector(); 
        if (ccnew.getOwner() != null) 
        { contx.add(ccnew.getOwner()); } 
        boolean ok = ccnew.typeCheck(types,entities,contx); 
        if (ok)
        { String upd = ccnew.globalUpdateOpJava7(ent,false); 
          body = body + "\n    " + upd;
        } 
      } 
    }  

    // if (isDefinedinSuperclass())
    // { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 
    String gop = header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; 
    if (derived) 
    { return gop; } 
    return gop + header2;
  }

  public String buildGlobalUpdateOpCSharp(Entity ent, String opname,
                                    Type t, String resT, 
                                    Vector entities, Vector types,
                                    Vector constraints)
  { String preheader = ""; 
    String javaPars = getCSharpParameterDec(); 
    String parlist = parameterList(); 

    if (ent == null) { return ""; } 
    
    String genPars = csharpGenericParameters(ent); 

    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "sealed "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(ArrayList exs, pars)
    String java2Pars = "";  // For All version of op 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    String retT = resT; 
    if (resT == null)
    { retT = "void"; } 

    if (!instanceScope)
    { if (resT == null || resT.equals("void")) 
      { return "  public static void " + opname + genPars + "(" + javaPars + ")\n" + 
               "  { " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
      } 
      return " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "ArrayList " + ex + "s"; 
    } 
    else 
    { java2Pars = "ArrayList " + exs + "," + javaPars; 
      javaPars = ename + genPars + " " + ex + "," + javaPars; 
    } 

    String header2 = header + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    header = header + retT + " " +
                    opname + "(" + javaPars + ")\n  { ";
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + "\n    ArrayList result = new ArrayList();\n  "; }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultCSharp() + ";\n  "; 
      header2 = header2 + "\n    ArrayList result = new ArrayList();\n  ";
      returning = " result"; 
    }

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "  //  if (!(" + pre1.queryFormCSharp(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    String body = "  " + ex + "." + call + ";"; 
    String endbody = ""; 
    header2 = header2 + "  for (int _i = 0; _i < " + exs + ".Count; _i++)\n" +
              "    { " + ename + genPars + " " + ex + " = (" + ename + genPars + ") " + exs + "[_i];\n"; 

    if (parlist.length() > 0)
    { parlist = "," + parlist; } 

    String newitem = opname + "(" + ex + parlist + ")"; 

    if (resultType != null && Type.isMapType(resultType))
    { header2 = header2 + "      result.Add(" + newitem + ");\n"; }
    else if (resultType != null && Type.isCollectionType(resultType))
    { header2 = header2 + "      result.AddRange(" + newitem + ");\n"; }
    else if (resT == null || resT.equals("void")) 
    { header2 = header2 + "      " + newitem + ";\n"; }
    else if (resultType.isPrimitive())
    { // newitem = Expression.wrap(resultType,newitem); 
      header2 = header2 + "      result.Add(" + newitem + ");\n"; 
    }
    else 
    { header2 = header2 + "      result.Add(" + newitem + ");\n"; }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    if (resT == null || resT.equals("void")) 
    { }
    else
    { body = " result = " + body; 
      endbody = "    return result;"; 
    }

    for (int i = 0; i < constraints.size(); i++)
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname, this);
      if (ccnew != null) 
      { Vector contx = new Vector(); 
        if (ccnew.getOwner() != null) 
        { contx.add(ccnew.getOwner()); } 
        boolean ok = ccnew.typeCheck(types,entities,contx); 
        if (ok)
        { String upd = ccnew.globalUpdateOpCSharp(ent,false); 
          body = body + "\n    " + upd;
        } 
      } 
    }  

    // if (isDefinedinSuperclass())
    // { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 

    String gop = header + newdecs + preheader + body + "\n " + endbody + "  }\n\n";  
    if (derived) 
    { return gop; } 
    return gop + header2;
  }  // if derived, omit the All global one. 

  public String buildGlobalUpdateOpCPP(Entity ent, String opname,
                                    Type t, String resT, Vector declarations,  
                                    Vector entities, Vector types,
                                    Vector constraints)
  { String preheader = ""; 
    String javaPars = getCPPParameterDec(); 

    if (ent == null) { return ""; } 
    
    String gpars = ent.typeParameterTextCPP(); 

    String header = "  "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    // if (isFinal())
    // { header = header + "sealed "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(ArrayList exs, pars)
    String java2Pars = "";  // For All version of op 
    String java3Pars = ""; 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 
    
    if (javaPars.equals(""))
    { javaPars = ename + gpars + "* " + ex;
      java2Pars = "vector<" + ename + gpars + "*>* " + exs; 
      java3Pars = "std::set<" + ename + gpars + "*>* " + exs; 
    } 
    else 
    { java2Pars = "vector<" + ename + gpars + "*>* " + exs + ", " + javaPars; 
      javaPars = ename + gpars + "* " + ex + ", " + javaPars; 
      java3Pars = "std::set<" + ename + gpars + "*>* " + exs + ", " + javaPars; 
    } 

    String et = "void*"; 
    if (elementType != null) 
    { et = elementType.getCPP(elementType.getElementType()); } 

    String allrest = "vector<void*>*";
    if ("void".equals(resultType + ""))
    { allrest = "vector<void*>*"; } 
    else if (resultType != null)
    { allrest = "vector<" + resT + ">*"; } 

    String header2 = header + " " + allrest + " Controller::All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    String header3 = header + " " + allrest + " Controller::All" + ename + 
                     opname + "(" + java3Pars + ")\n  { ";

    if (derived) { } 
    else 
    { declarations.add("  " + allrest + " All" + ename + opname + "(" + java2Pars + ");"); 
      declarations.add("  " + allrest + " All" + ename + opname + "(" + java3Pars + ");");
    } 

    String retT = resT; 
    if (resT == null)
    { retT = "void"; } 
    
    header = header + retT + " Controller::" +
                             opname + "(" + javaPars + ")\n  { ";
    declarations.add(retT + " " + opname + "(" + javaPars + ");"); 

    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);

    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    resultatt.setElementType(elementType); 
    atts.add(resultatt);

    String returning = "";
    if (resultType == null || resT.equals("void")) 
    { header2 = header2 + "\n    vector<void*>* result = new vector<void*>();\n  "; 
      header3 = header3 + "\n    vector<void*>* result = new vector<void*>();\n  ";
    }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultCPP(elementType) + ";\n  "; 
      header2 = header2 + "\n    vector<" + resT + ">* result = new vector<" + resT + ">();\n  ";
      header3 = header3 + "\n    vector<" + resT + ">* result = new vector<" + resT + ">();\n  ";
      returning = " result"; 
    }

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "  //  if (!(" + pre1.queryFormCPP(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    String body = "  " + ex + "->" + call + ";"; 
    String endbody = ""; 
    header2 = header2 + "  for (int _i = 0; _i < " + exs + "->size(); _i++)\n" +
              "    { " + ename + gpars + "* " + ex + " = (*" + exs + ")[_i];\n"; 
    header3 = header3 + "  for (std::set<" + ename + gpars + "*>::iterator _i = " + exs + "->begin(); _i != " + exs + "->end(); _i++)\n" +
              "    { " + ename + gpars + "* " + ex + " = *_i;\n"; 

    String parlist = parameterList(); 
    if (parlist.length() > 0)
    { parlist = "," + parlist; } 

    String newitem = opname + "(" + ex + parlist + ")"; 

    if (resultType != null && Type.isMapType(resultType))
    { header2 = header2 + "      result->push_back(" + newitem + ");\n"; 
      header3 = header3 + "      result->push_back(" + newitem + ");\n";
    }
    else if (resultType != null && 
             Type.isCollectionType(resultType))
    { header2 = header2 + "      result->insert(result->end(), " + newitem + "->begin(), " + 
                                                newitem + "->end());\n"; 
      header3 = header3 + "      result->insert(result->end(), " + newitem + "->begin(), " + 
                                                newitem + "->end());\n"; 
    }
    else if (resultType == null || resT.equals("void")) 
    { header2 = header2 + "      " + newitem + ";\n"; 
      header3 = header3 + "      " + newitem + ";\n";
    }
    else 
    { header2 = header2 + "      result->push_back(" + newitem + ");\n"; 
      header3 = header3 + "      result->push_back(" + newitem + ");\n"; 
    }

    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 
    header3 = header3 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    if (resultType == null || resT.equals("void")) 
    { }
    else
    { body = " result = " + body; 
      endbody = "    return result;"; 
    }

    for (int i = 0; i < constraints.size(); i++)
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname, this);
      if (ccnew != null) 
      { Vector contx = new Vector(); 
        if (ccnew.getOwner() != null) 
        { contx.add(ccnew.getOwner()); } 
        boolean ok = ccnew.typeCheck(types,entities,contx); 
        if (ok)
        { String upd = ccnew.globalUpdateOpCPP(ent,false); 
          body = body + "\n    " + upd;
        } 
      } 
    }  

    // if (isDefinedinSuperclass())
    // { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 
    if (derived) 
    { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n"; } 
    else 
    { return header + newdecs + preheader + body + "\n " + endbody + "  }\n\n" + header2 + header3; } 
  }


  public String buildGlobalQueryOp(Entity ent, String opname,
                                   Type t, String resT, 
                                   Vector entities, Vector types)
  { // if (isDefinedinSuperclass())
    // { return ""; } 
    if (ent == null) { return ""; } 

    String preheader = ""; 
    String javaPars = getJavaParameterDec(); 
    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 
    String parlist = parameterList(); 
    
    if (!instanceScope) 
    { return ""; // " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
                 // " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 

    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "List " + exs; 
    } 
    else 
    { java2Pars = "List " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    String header2 = header + " List All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    atts.add(resultatt);
    String returning = "";
    String retT = resT; 
    if (resT == null || resT.equals("void")) 
    { retT = "void"; }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefault() + ";\n  "; 
      header2 = header2 + "\n    List result = new Vector();\n  ";
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "    if (!(" + pre1.queryForm(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    }   

    String newdecs = ""; 
    String call = toString();
     
    header2 = header2 + "  for (int _i = 0; _i < " + exs + ".size(); _i++)\n" +
      "    { " + ename + " " + ex + " = (" + ename + ") " + exs + ".get(_i);\n"; 

    // if (parlist.length() > 0)
    // { parlist = "," + parlist; } 

    String newitem = ex + "." + opname + "(" + parlist + ")"; 

    if ("List".equals(resT))
    { header2 = header2 + 
        "      if (" + newitem + " != null)\n" + 
        "      { result.addAll(" + newitem + "); }\n"; 
    } // NOT addAll
    else 
    { // header2 = header2 + 
      //          "      if (" + newitem + " != null) \n"; 
      if (t != null && t.isPrimitive())
      { newitem = Expression.wrap(resultType,newitem); }
      header2 = header2 + 
                "      result.add(" + newitem + ");\n"; 
    }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    return header2;
  }

  public String buildGlobalQueryOpJava6(Entity ent, String opname,
                                   Type t, String resT, 
                                   Vector entities, Vector types)
  { if (derived)
    { return ""; }
    if (ent == null) { return ""; } 
 
    String preheader = ""; 
    String javaPars = getJava6ParameterDec(); 
    String parlist = parameterList(); 

    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 

    if (!instanceScope) 
    { return ""; // " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             // " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "Collection " + exs; 
    } 
    else 
    { java2Pars = "Collection " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 



    String header2 = header + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    atts.add(resultatt);
    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultJava6() + ";\n  "; 
      header2 = header2 + "\n    ArrayList result = new ArrayList();\n  ";
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "    if (!(" + pre1.queryFormJava6(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    header2 = header2 + "  for (Object _i : " + exs + ")\n" +
              "    { " + ename + " " + ex + " = (" + ename + ") _i;\n"; 

    // if (parlist.length() > 0)
    // { parlist = "," + parlist; } 

    String newitem = ex + "." + opname + "(" + parlist + ")"; 

    if (resultType != null && 
        Type.isMapType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    else if (resultType != null && 
             Type.isCollectionType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; } // NOT addAll
    else 
    { if (t != null && t.isPrimitive())
      { newitem = Expression.wrap(resultType,newitem); }
      header2 = header2 + "      result.add(" + newitem + ");\n"; 
    }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    return header2;
  }

  public String buildGlobalQueryOpJava7(Entity ent, String opname,
                                   Type t, String resT, 
                                   Vector entities, Vector types)
  { if (derived)
    { return ""; }
    if (ent == null) { return ""; } 
 
    String preheader = ""; 
    String javaPars = getJava7ParameterDec(); 
    String parlist = parameterList(); 

    
    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "final "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 

    if (!instanceScope) 
    { return ""; // " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             // " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "Collection<" + ename + "> " + exs; 
    } 
    else 
    { java2Pars = "Collection<" + ename + "> " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    Type allrestype = new Type("Sequence", null); 
    allrestype.setElementType(resultType); 
    String alltype = allrestype.getJava7(resultType); 

    String header2 = header; 
    if (resT == null || resT.equals("void")) 
    { header2 = header2 + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    } // not valid case
    else 
    { header2 = header2 + " " + alltype + " All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    } 

    
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    atts.add(resultatt);
    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { header = header + "\n   " + resT + " result = " + resultType.getDefaultJava7() + ";\n  "; 
      header2 = header2 + "\n    " + alltype + " result = new " + alltype + "();\n  ";
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "    if (!(" + pre1.queryFormJava7(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    header2 = header2 + "  for (Object _i : " + exs + ")\n" +
              "    { " + ename + " " + ex + " = (" + ename + ") _i;\n"; 

    // if (parlist.length() > 0)
    // { parlist = "," + parlist; } 

    String newitem = ex + "." + opname + "(" + parlist + ")"; 

    if (resultType != null && 
        Type.isMapType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }
    else if (resultType != null && 
        Type.isCollectionType(resultType))
    { header2 = header2 + "      result.add(" + newitem + ");\n"; }  // NOT addAll
    else 
    { if (t != null && t.isPrimitive())
      { newitem = Expression.wrap(resultType,newitem); }
      header2 = header2 + "      result.add(" + newitem + ");\n"; 
    }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    return header2;
  }

  public String csharpGenericParameters(Entity ent)
  { String genPars = ""; 
    Vector alltypePars = new Vector(); 
    alltypePars.addAll(ent.getTypeParameters()); 
    if (typeParameters != null)
    { alltypePars.addAll(typeParameters); } 
    if (alltypePars.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < alltypePars.size(); i++) 
      { Type tp = (Type) alltypePars.get(i); 
        genPars = genPars + tp.getCSharp(); 
        if (i < alltypePars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 
    return genPars; 
  } 

  public String buildGlobalQueryOpCSharp(Entity ent, String opname,
                                   Type t, String resT, 
                                   Vector entities, Vector types)
  { // if (isDefinedinSuperclass())
    // { return ""; } 
    if (ent == null || derived) { return ""; } 

    String preheader = ""; 
    String javaPars = getCSharpParameterDec(); 
    
    String genPars = ""; 
    Vector alltypePars = new Vector(); 
    alltypePars.addAll(ent.getTypeParameters()); 
    if (typeParameters != null)
    { alltypePars.addAll(typeParameters); } 
    if (alltypePars.size() > 0)
    { genPars = "<"; 
      for (int i = 0; i < alltypePars.size(); i++) 
      { Type tp = (Type) alltypePars.get(i); 
        genPars = genPars + tp.getCSharp(); 
        if (i < alltypePars.size() - 1) 
        { genPars = genPars + ", "; } 
      } 
      genPars = genPars + ">"; 
    } 

    String header = "  public "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    if (isFinal())
    { header = header + "sealed "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 
    String parlist = parameterList(); 

    if (!instanceScope) 
    { return ""; // " public static " + resT + " " + opname + "(" + javaPars + ")\n" + 
             // " { return " + ename + "." + opname + "(" + parlist + "); }\n\n"; 
    } 
    
    if (javaPars.equals(""))
    { javaPars = ename + " " + ex;
      java2Pars = "ArrayList " + exs; 
    } 
    else 
    { java2Pars = "ArrayList " + exs + "," + javaPars; 
      javaPars = ename + " " + ex + "," + javaPars; 
    } 

    String header2 = header + " ArrayList All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    atts.add(resultatt);
    String returning = "";
    if (resT == null || resT.equals("void")) { }
    else
    { // header = header + "\n   " + resT + " result = " + resultType.getDefaultCSharp() + ";\n  "; 
      header2 = header2 + "\n    ArrayList result = new ArrayList();\n  ";
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "    if (!(" + pre1.queryFormCSharp(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    header2 = header2 + "  for (int _i = 0; _i < " + exs + ".Count; _i++)\n" +
              "    { " + ename + genPars + " " + ex + " = (" + ename + genPars + ") " + exs + "[_i];\n"; 

    // if (parlist.length() > 0)
    // { parlist = "," + parlist; } 

    String newitem = ex + "." + opname + "(" + parlist + ")"; 

    if (resultType != null && 
        Type.isMapType(resultType))
    { header2 = header2 + "      result.Add(" + newitem + ");\n"; }
    else if ("ArrayList".equals(resT))
    { header2 = header2 + "      result.AddRange(" + newitem + ");\n"; }
    else 
    { // if (t.isPrimitive())
      // { newitem = Expression.wrap(resultType,newitem); }
      header2 = header2 + "      result.Add(" + newitem + ");\n"; 
    }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    return header2;
  }

  public String buildGlobalQueryOpCPP(Entity ent, String opname,
                                   Type t, String resT, Vector declarations, 
                                   Vector entities, Vector types)
  { if (derived) { return ""; } 
    if (ent == null) { return ""; } 

    // if (isDefinedinSuperclass())
    // { return ""; } 
    String preheader = ""; 
    String javaPars = getCPPParameterDec(); 
    
    String header = "  "; 
    // if (isAbstract())
    // { header = header + "abstract "; } 
    // if (isFinal())
    // { header = header + "sealed "; } 
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String exs = ex + "s";  // For All version of op: op(List exs, pars)
    String java2Pars = "";  // For All version of op 
    String java3Pars = "";  // For All version of op 
    
    if (javaPars.equals(""))
    { javaPars = ename + "* " + ex;
      java2Pars = "vector<" + ename + "*>* " + exs; 
      java3Pars = "std::set<" + ename + "*>* " + exs; 
    } 
    else 
    { java2Pars = "vector<" + ename + "*>* " + exs + "," + javaPars; 
      java3Pars = "std::set<" + ename + "*>* " + exs + "," + javaPars; 
      javaPars = ename + "* " + ex + "," + javaPars; 
    } 

    String restype = "";
    if (resultType == null || resT.equals("void")) 
    { restype = "vector<void*>"; }
    else
    { restype = "vector<" + resT + ">"; }

    String et = "void*"; 
    if (elementType != null) 
    { et = elementType.getCPP(elementType.getElementType()); } 

    String header2 = header + " " + restype + "* Controller::All" + ename + 
                     opname + "(" + java2Pars + ")\n  { ";
    String header3 = header + " " + restype + "* Controller::All" + ename + 
                     opname + "(" + java3Pars + ")\n  { ";
    // and add header to the declarations
    declarations.add(restype + "* All" + ename + opname + "(" + java2Pars + ");"); 
    declarations.add(restype + "* All" + ename + opname + "(" + java3Pars + ");"); 
    
    java.util.Map env0 = new java.util.HashMap();
    env0.put(ename,ex);
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    atts.add(resultatt);

    String returning = "";
    if (resultType == null || resT.equals("void")) 
    { header2 = header2 + "\n    vector<void*>* result = new vector<void*>();\n  "; 
      header3 = header3 + "\n    vector<void*>* result = new vector<void*>();\n  "; 
    }
    else
    { header2 = header2 + "\n    vector<" + resT + ">* result = new vector<" + resT + ">();\n  ";
      header3 = header3 + "\n    vector<" + resT + ">* result = new vector<" + resT + ">();\n  ";
      returning = " result"; 
    }

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (pre != null) 
    { Expression pre1 = pre.substituteEq("self",new BasicExpression(ex)); 
      pre1.typeCheck(types,entities,context,atts); 
      if ("true".equals("" + pre1)) { } 
      else 
      { preheader = "    if (!(" + pre1.queryFormCPP(env0,false) + 
                    ")) { return" + returning + "; } \n  "; 
      }
    } 

    String newdecs = ""; 
    String call = toString();
     
    header2 = header2 + "  for (int _i = 0; _i < " + exs + "->size(); _i++)\n" +
              "    { " + ename + "* " + ex + " = (*" + exs + ")[_i];\n"; 
    header3 = header3 + "  for (std::set<" + ename + "*>::iterator _i = " + exs + "->begin(); _i != " + exs + "->end(); _i++)\n" +
              "    { " + ename + "* " + ex + " = *_i;\n"; 

    String parlist = parameterList(); 
    // if (parlist.length() > 0)
    // { parlist = "," + parlist; } 

    String newitem = ex + "->" + opname + "(" + parlist + ")"; 

    if (resultType != null)
    /*  && Type.isCollectionType(resultType))
    { header2 = header2 + "      result->insert(result->end(), " + newitem + "->begin(), " +
                                                newitem + "->end());\n"; 
      header3 = header3 + "      result->insert(result->end(), " + newitem + "->begin(), " +
                                                newitem + "->end());\n"; 
    }
    else */ 
    { header2 = header2 + "      result->push_back(" + newitem + ");\n"; 
      header3 = header3 + "      result->push_back(" + newitem + ");\n"; 
    }
    
    header2 = header2 + "    }\n" + "    return result; \n" + "  }\n\n"; 
    header3 = header3 + "    }\n" + "    return result; \n" + "  }\n\n"; 

    return header2 + header3;
  }

  // Use the statemachine?
  public BOp getBOperationCode(Entity ent,Vector entities, Vector types)
  { String name = getName();
    String resT = null; 
    if (post == null) 
    { return null; } 
    if (resultType != null)
    { resT = resultType.generateB(); }
    if (query)
    { BOp op = buildBQueryOp(ent,name,resultType,resT,entities,types);
      if (op != null)
      { op.setSignature(getSignature()); }
      return op;
    }
    else
    { BOp op = buildBUpdateOp(ent,name,resultType,resT,entities,types);
      if (op != null)
      { op.setSignature(getSignature()); }
      return op;
    }
  } // no pre?

  public BOp getGlobalBOperationCode(Entity ent,Vector entities, Vector types,
                                     Vector constraints)
  { String name = getName();
    String resT = null; 
    if (resultType != null)
    { resT = resultType.generateB(); }
    if (post == null || query || isAbstract())
    { return null; }
    else 
    { return buildGlobalBUpdateOp(ent,name,resultType,resT,
                                  entities,types,constraints); 
    }
  } // ignore return type for update ops for now. 

  public BOp buildBQueryOp(Entity ent, String opname,
                                  Type t, String resT, 
                                  Vector entities, Vector types)
  { String rx = "resultx";
    Expression rxbe = new BasicExpression(rx);
    BExpression rxbbe = new BBasicExpression(rx); 
    boolean norange = false; 
    

    Scope scope = post.resultScope();
    BinaryExpression rscope = null; 
    if (scope == null) // no range, must assign result
    { System.err.println("ERROR: No scope for result of " + opname); 
      // ANY resultx WHERE resultx: resultType & Post[resultx/result]
      // THEN result := resultx END
      // return null;
      Expression resTbe = new BasicExpression(resT);
      rscope = new BinaryExpression(":",rxbe,resTbe);  
      norange = true; 
      // post = new BinaryExpression("&",post,rscope); 
    }
    else 
    { rscope = scope.resultScope; } 
    
    BExpression returnbe = new BBasicExpression("result");
    java.util.Map env0 = new java.util.HashMap();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    if (instanceScope) 
    { env0.put(ename,exbe); }
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 

    Expression presb = new BasicExpression("true"); 
    if (pre != null) 
    { presb = pre.substituteEq("self",new BasicExpression(ex)); } 

    BExpression pre0 = getBParameterDec(); 
    BExpression pre1 = 
      BBinaryExpression.simplify("&",pre0,presb.binvariantForm(env0,false),true); 
    BExpression inst = new BBinaryExpression(":",exbe,esbe); 
    if (instanceScope)
    { pre1 = BBinaryExpression.simplify("&",inst,pre1,false); }
    // Only for instance scope ops 
    Vector atts = (Vector) parameters.clone(); 
    Attribute resultatt = new Attribute("result",t,ModelElement.INTERNAL); 
    Attribute resultxatt = new Attribute(rx,t,ModelElement.INTERNAL); 
    resultatt.setEntity(ent); // seems like a good idea
    resultxatt.setEntity(ent); 
    atts.add(resultatt); 
    atts.add(resultxatt);   

    Vector pars = getParameterNames();
    if (instanceScope) 
    { pars.add(0,ex); } 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    if (isInScope(rscope) || isSubScope(rscope) || 
        (scope != null && scope.scopeKind.equals("array")))
    { Expression newpost = post.substituteEq("result",rxbe); 
   
      newpost.typeCheck(types,entities,context,atts);
      BExpression pred = newpost.binvariantForm(env0,false);
      if (norange)
      { pred = new BBinaryExpression("&",
                 new BBinaryExpression(":",rxbbe,new BBasicExpression(resT)),
                 pred); 
      } 
      else if (scope != null && scope.scopeKind.equals("array"))
      { BasicExpression arr = (BasicExpression) scope.arrayExp.clone();
        arr.arrayIndex = null; 
        BExpression barr = arr.binvariantForm(env0,false); 
        pred = new BBinaryExpression("&",
                 new BBinaryExpression(":",rxbbe,new BUnaryExpression("dom",barr)),
                 pred); 
      } 
      Vector var = new Vector();
      var.add(rx);
      BStatement assign = new BAssignStatement(returnbe,rxbbe); 
      BStatement body = new BAnyStatement(var,pred,assign);
      return new BOp(opname,"result",pars,pre1,body);  
    }
    if (isEqScope(rscope))
    { /* Expression returnval = returnValue(rscope);
      returnval.typeCheck(types,entities,atts); 
      BStatement body =
        new BAssignStatement(returnbe,returnval.binvariantForm(env0,false));
      body.setWriteFrame("result");

      if (post instanceof BinaryExpression)
      { BinaryExpression postbe = (BinaryExpression) post;
        if (postbe.operator.equals("=>"))
        { Expression cond = postbe.left;
          cond.typeCheck(types,entities,atts);
          BStatement code = new BIfStatement(cond.binvariantForm(env0,false),body,
                                             null);
          return new BOp(opname,"result",pars,pre1,code);
        }
      }  // if a conjunction, use separateUpdates(statements)
      */ 
      BStatement body = buildBUpdateCode(rscope,env0,types,entities,atts); 
      return new BOp(opname,"result",pars,pre1,body); 
    }  
    return null;  // result := (opname_ename(ex))(pars)
  }

  private BStatement buildBQueryCases(Expression pst, java.util.Map env,
                                      Vector types, Vector entities, Vector atts)
  { Vector context = new Vector(); 
    if (entity != null) 
    { context.add(entity); } 

    if (pst instanceof BinaryExpression)
    { BinaryExpression bepst = (BinaryExpression) pst; 
      String op = bepst.operator; 

      if (op.equals("="))
      { BExpression returnbe = new BBasicExpression("result");
        Scope scope = bepst.resultScope(); 
        Expression returnval = returnValue(scope.resultScope);
        returnval.typeCheck(types,entities,context,atts); 
        BStatement body =
          new BAssignStatement(returnbe,returnval.binvariantForm(env,false));
        body.setWriteFrame("result");
        return body; 
      } 
      if (op.equals("=>"))
      { BStatement body = buildBUpdateCode(bepst.right, env, types, entities, atts); 
        Expression cond = bepst.left;
        cond.typeCheck(types,entities,context,atts);
        BStatement code = new BIfStatement(cond.binvariantForm(env,false),body,
                                           null);
        code.setWriteFrame("result"); 
        return code; 
      } 
      if (op.equals("&"))
      { Vector res = new Vector(); 
        BStatement code1 = buildBQueryCases(bepst.left, env,types,entities,atts); 
        BStatement code2 = buildBQueryCases(bepst.right, env,types,entities,atts); 
        if ((code1 instanceof BIfStatement) && (code2 instanceof BIfStatement))
        { BIfStatement if1 = (BIfStatement) code1; 
          BIfStatement if2 = (BIfStatement) code2; 
          if1.extendWithCase(if2); 
          return if1;
        }  
        res.add(code1); 
        res.add(code2);
        return BStatement.separateUpdates(res);
      } 
    } // for or, a choice 
    return pst.bupdateForm(env,false); 
  } 
          

  public BExpression buildBConstantDefinition(Entity ent, 
                                              Vector entities, Vector types)
  { // (opname_E: es --> (parT --> resT)) &
    // !ex.(ex : es => !x.(x: parT & pre => post[opname_E(ex,x)/result]))
    
    BExpression fdec = getBFunctionDec(); 
    Expression fcall = new BasicExpression("" + getBCall()); 
    java.util.Map env0 = new java.util.HashMap();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    env0.put(ename,exbe);
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 


    BExpression pre0 = getBParameterDec(); 
    BExpression pre1; 
    if (pre == null) 
    { pre1 = pre0; } 
    else 
    { pre1 = 
        BBinaryExpression.simplify("&",pre0,pre.binvariantForm(env0,false),true); 
    } 
    BExpression inst = new BBinaryExpression(":",exbe,esbe); 
    pre1 = BBinaryExpression.simplify("&",inst,pre1,false);
    // Only for instance scope ops 
    Vector atts = (Vector) parameters.clone(); 

    Vector pars = getParameterNames();
    pars.add(0,ex); 

    Expression newpost = post.substituteEq("result",fcall); 

    Vector context = new Vector(); 
    if (ent != null) 
    { context.add(ent); } 

    newpost.typeCheck(types,entities,context,atts);
    BExpression pred = newpost.binvariantForm(env0,false);
    pred.setBrackets(true); 
    
    BExpression fdef = 
      new BQuantifierExpression("forall",pars,
            new BBinaryExpression("=>",pre1,pred)); 
    return new BBinaryExpression("&",fdec,fdef); 
  }


  private BStatement buildBUpdateCode(Expression rscope, java.util.Map env,
                                      Vector types, Vector entities, Vector atts)
  { if (rscope instanceof BinaryExpression)
    { return buildBUpdateCode((BinaryExpression) rscope,env,types,entities,atts); } 
    return null; 
  } 

  private BStatement buildBUpdateCode(BinaryExpression rscope, java.util.Map env,
                                      Vector types, Vector entities, Vector atts)
  { String op = rscope.operator; 
    BExpression returnbe = new BBasicExpression("result");

    Vector context = new Vector(); 
    if (entity != null) 
    { context.add(entity); } 

    if (op.equals("="))
    { Expression returnval = returnValue(rscope);
      returnval.typeCheck(types,entities,context,atts); 
      BStatement body =
        new BAssignStatement(returnbe,returnval.binvariantForm(env,false));
      body.setWriteFrame("result");
      return body; 
    } 
    if (op.equals("=>"))
    { BStatement body = buildBUpdateCode(rscope.right, env, types, entities, atts); 
      Expression cond = rscope.left;
      cond.typeCheck(types,entities,context,atts);
      BStatement code = new BIfStatement(cond.binvariantForm(env,false),body,
                                         null);
      code.setWriteFrame("result"); 
      return code; 
    } 
    if (op.equals("&"))
    { Vector res = new Vector(); 
      BStatement code1 = buildBUpdateCode(rscope.left, env,types,entities,atts); 
      BStatement code2 = buildBUpdateCode(rscope.right, env,types,entities,atts); 
      if ((code1 instanceof BIfStatement) && (code2 instanceof BIfStatement))
      { BIfStatement if1 = (BIfStatement) code1; 
        BIfStatement if2 = (BIfStatement) code2; 
        if1.extendWithCase(if2); 
        return if1;
      }  
      // res.add(code1); 
      // res.add(code2);
      // return BStatement.separateUpdates(res); 
    } // for or, a choice 
    return null; 
  } 
          
    

  public BOp buildBUpdateOp(Entity ent, String opname,
                            Type t, String resT, 
                            Vector entities, Vector types)
  { java.util.Map env0 = new java.util.HashMap();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    if (instanceScope) 
    { env0.put(ename,exbe); }
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

  public BOp explicitBUpdateOp(Entity ent, String opname,
                            Type t, String resT, 
                            Vector entities, Vector types,java.util.Map env0,
                            BExpression exbe, BExpression esbe, BExpression pre1)
  { BStatement stat = post.bOpupdateForm(env0,true); 
    Vector pars = getParameterNames();
    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    if (instanceScope) 
    { pars.add(0,ex); } 
    return new BOp(opname,null,pars,pre1,stat); 
  }  

  public BOp buildGlobalBUpdateOp(Entity ent, String opname,
                            Type t, String resT, 
                            Vector entities, Vector types, Vector constraints)
  { java.util.Map env0 = new java.util.HashMap();
    if (ent == null) 
    { return null; } 

    String ename = ent.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    env0.put(ename,exbe);
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 

    Vector context = new Vector(); 
    context.add(ent); 

    // type check pre? 
    Expression presb;
    if (pre == null) 
    { presb = new BasicExpression("true"); } 
    else 
    { presb = pre.substituteEq("self", new BasicExpression(ex)); } 

    BExpression pre0 = getBParameterDec(); 
    BExpression pre1 = 
      BBinaryExpression.simplify("&",pre0,presb.binvariantForm(env0,false),true); 
    BExpression inst = new BBinaryExpression(":",exbe,esbe); 
    pre1 = BBinaryExpression.simplify("&",inst,pre1,false);
    Vector atts = (Vector) parameters.clone(); 
    // are these used? 

    Vector pars = getParameterNames();
    pars.add(0,ex); 
    int count = 0; 

    BParallelStatement body = new BParallelStatement();
     
    BOperationCall call = new BOperationCall(opname,pars);
    body.addStatement(call);
    
    for (int i = 0; i < constraints.size(); i++) 
    { Constraint cc = (Constraint) constraints.get(i); 
      Constraint ccnew = cc.matchesOp(opname,this); 
      if (ccnew != null)
      { boolean ok = ccnew.typeCheck(types,entities,context); 
        if (ok)
        { BStatement upd = ccnew.bupdateOperation(ent,false); 
          body.addStatement(upd);
          count++;  
        } 
      } 
    } // B does not allow 2 ops of same machine to be called in 
      // parallel!
    
    if (count == 0)   
    { return new BOp(opname + "_" + ename,null,pars,pre1,call); }
    return new BOp(opname + "_" + ename,null,pars,pre1,body); 
    // This operation must ensure the preconditions of ops that it calls. 
  }

  private Vector usedInPreForm(Vector features, Vector preForms)
  { Vector res = new Vector(); 
    for (int i = 0; i < preForms.size(); i++) 
    { BasicExpression pf = (BasicExpression) preForms.get(i); 
      if (features.contains(pf.data))
      { BasicExpression var = (BasicExpression) pf.clone(); 
        var.setPrestate(false); 
        res.add(var);
      } 
    } 
    return res; 
  } 

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 
    if (pre == null) { } 
    else 
    { res.addAll(pre.allOperationsUsedIn()); } 

    if (activity != null)
    { res.addAll(activity.allOperationsUsedIn()); 
      return res; 
    } 
    
    if (post == null) 
    { return res; } 
    
    res.addAll(post.allOperationsUsedIn()); 
    return res; 
  } 

  public int efo() 
  { Vector clls = operationsUsedIn(); 
    java.util.Set st = new HashSet(); 
    for (int i = 0; i < clls.size(); i++) 
    { st.add(clls.get(i)); } 
    return st.size(); 
  } 

  public void getCallGraph(Map res)
  { Vector bfcalls = operationsUsedIn(); 
    for (int j = 0; j < bfcalls.size(); j++) 
    { res.add_pair(name, bfcalls.get(j)); } 
  } 

  public int analyseEFO(Map m) 
  { // count the number of different targets of calls from the map
    java.util.Set calls = new HashSet(); 
    for (int i = 0; i < m.elements.size(); i++) 
    { Maplet mm = (Maplet) m.get(i); 
      calls.add(mm.getTarget() + ""); 
    } 
    int res = calls.size(); 

    System.out.println("*** Operation " + getName() + " has fan-out = " + res); 
    return res; 
  } 

  private static boolean isInScope(BinaryExpression be)
  { if (be.operator.equals(":")) { return true; }
    if (be.operator.equals("&"))
    { return isInScope((BinaryExpression) be.left) &&
             isInScope((BinaryExpression) be.right);
    }
    // if (be.operator.equals("#") || be.operator.equals("#1"))
    // { return isInScope((BinaryExpression) be.right); } 
    return false;
  }

  private static boolean isEqScope(BinaryExpression be)
  { if (be.operator.equals("=")) { return true; }
    if (be.operator.equals("&"))
    { return (be.left instanceof BinaryExpression) &&
             (be.right instanceof BinaryExpression) && 
             isEqScope((BinaryExpression) be.left) &&
             isEqScope((BinaryExpression) be.right);
    }
    if (be.operator.equals("=>"))
    { return (be.right instanceof BinaryExpression) &&
             isEqScope((BinaryExpression) be.right); 
    }
    // if (be.operator.equals("#") || be.operator.equals("#1"))
    // { return isEqScope((BinaryExpression) be.right); } 
    return false;
  }  // if => then true if isEqScope right? 

  private static boolean isSubScope(BinaryExpression be)
  { if (be.operator.equals("<:")) { return true; }
    if (be.operator.equals("&"))
    { return isSubScope((BinaryExpression) be.left) &&
             isSubScope((BinaryExpression) be.right);
    }
    // if (be.operator.equals("#") || be.operator.equals("#1"))
    // { return isSubScope((BinaryExpression) be.right); } 
    return false;
  }

  private static Expression returnValue(BinaryExpression be)
  { if (be.operator.equals("=") && "result".equals(be.left + ""))  // check result is on left
    { return be.right; }
    else if (be.operator.equals("&"))
    { if (be.left instanceof BinaryExpression)
      { Expression res = returnValue((BinaryExpression) be.left);
        if (res != null)
        { return res; }
        else if (be.right instanceof BinaryExpression)
        { return returnValue((BinaryExpression) be.right); }
        else
        { return null; }
      }
      else if (be.right instanceof BinaryExpression)
      { return returnValue((BinaryExpression) be.right); }
    }
    else if (be.operator.equals("=>") &&
             (be.right instanceof BinaryExpression))
    { return returnValue((BinaryExpression) be.right); } 
    else if (be.operator.equals("#") || be.operator.equals("#1"))
    { return returnValue((BinaryExpression) be.right); } 
    return null;
  }

  private static BExpression getBRange(BinaryExpression be,
                                       java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.binvariantForm(env,false); }
    if (be.operator.equals("&")) // take intersection
    { BExpression lqf = getBRange((BinaryExpression) be.left,
                                  env);
      BExpression rqf = getBRange((BinaryExpression) be.right,
                                  env);
      if (("" + lqf).equals("" + rqf)) { return lqf; } 
      return new BBinaryExpression("/\\",lqf,rqf);
    }
    return null;
  }

  private static String getRange(BinaryExpression be,
                                 java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.queryForm(env,true); }
    if (be.operator.equals("&")) // take intersection
    { String lqf = getRange((BinaryExpression) be.left,
                                  env);
      String rqf = getRange((BinaryExpression) be.right,
                                  env);

      if (lqf == null) { return rqf; } 
      if (rqf == null) { return lqf; } 
      if (("" + lqf).equals(rqf)) { return lqf; } 
      return "Set.intersection(" + lqf + "," + rqf + ")";
    }
    return null;
  }

  private static String getRangeJava6(BinaryExpression be,
                                 java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.queryFormJava6(env,true); }
    if (be.operator.equals("&")) // take intersection
    { String lqf = getRangeJava6((BinaryExpression) be.left,
                                  env);
      String rqf = getRangeJava6((BinaryExpression) be.right,
                                  env);

      if (lqf == null) { return rqf; } 
      if (rqf == null) { return lqf; } 
      if (("" + lqf).equals(rqf)) { return lqf; } 
      return "Set.intersection(" + lqf + "," + rqf + ")";
    }
    return null;
  }

  private static String getRangeJava7(BinaryExpression be,
                                 java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.queryFormJava7(env,true); }
    if (be.operator.equals("&")) // take intersection
    { String lqf = getRangeJava7((BinaryExpression) be.left,
                                  env);
      String rqf = getRangeJava7((BinaryExpression) be.right,
                                  env);

      if (lqf == null) { return rqf; } 
      if (rqf == null) { return lqf; } 
      if (("" + lqf).equals(rqf)) { return lqf; } 
      return "Ocl.intersection(" + lqf + "," + rqf + ")";
    }
    return null;
  }

  private static String getRangeCSharp(BinaryExpression be,
                                 java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.queryFormCSharp(env,true); }
    if (be.operator.equals("&")) // take intersection
    { String lqf = getRangeCSharp((BinaryExpression) be.left,
                                  env);
      String rqf = getRangeCSharp((BinaryExpression) be.right,
                                  env);

      if (lqf == null) { return rqf; } 
      if (rqf == null) { return lqf; } 
      if (("" + lqf).equals(rqf)) { return lqf; } 
      return "SystemTypes.intersection(" + lqf + "," + rqf + ")";
    }
    return null;
  }

  private static String getRangeCPP(BinaryExpression be,
                                 java.util.Map env)
  { if (be.operator.equals(":") || be.operator.equals("<:"))
    { return be.right.queryFormCPP(env,true); }
    if (be.operator.equals("&")) // take intersection
    { String lqf = getRangeCPP((BinaryExpression) be.left,
                                  env);
      String rqf = getRangeCPP((BinaryExpression) be.right,
                                  env);

      if (lqf == null) { return rqf; } 
      if (rqf == null) { return lqf; } 
      if (("" + lqf).equals(rqf)) { return lqf; } 
      String lcet = be.left.elementType.getCPP(); 
      return "UmlRsdsLib<" + lcet + ">::intersection(" + lqf + "," + rqf + ")";
    }
    return null;
  }

  private static Type getElementType(BinaryExpression be)
  { if (be.operator.equals("<:"))
    { return be.right.elementType; }
    if (be.operator.equals("&")) // take intersection
    { return getElementType((BinaryExpression) be.left); }
    return null;
  }

  public Statement parameterAssignments(Expression call)
  { // vars := exprs for corresponding parameters of call
    // A parallel assignment. Serialise by storing old 
    // values of LHS variables before overwriting them.

    if (parameters == null || parameters.size() == 0) 
    { return null; } 

    Vector exprs = call.getParameters(); 
    if (exprs == null) 
    { return null; } 

    Vector stats = new Vector(); 

    Vector previousVars = new Vector(); 
    Vector declared = new Vector(); // already have var old_v

    for (int i = 0; i < parameters.size(); i++) 
    { Attribute par = (Attribute) parameters.get(i);
      
      if (i < exprs.size()) 
      { BasicExpression parexpr = 
            new BasicExpression(par); 
        Expression expr = (Expression) exprs.get(i);
        Expression newexpr = expr; 
 
        if (("" + parexpr).equals(expr + "")) { } 
        else 
        { 
          Vector vuses = expr.getVariableUses();
          Vector previousVarOccurs = 
            VectorUtil.commonElementsByString(
                              previousVars, vuses); 

          if (previousVarOccurs.size() > 0)
          { // for each occ : previousVarOccurs, add
            // var old_occ if occ not already in declared
            // Substitute old_occ for occ in expr

            for (int j = 0; j < previousVarOccurs.size(); j++)
            { Attribute occ = 
                  (Attribute) previousVarOccurs.get(j);
              String oname = occ.getName(); 

              Attribute old_occ = (Attribute) occ.clone(); 
              old_occ.setName("old_" + oname); 

              if (declared.contains(occ)) { } 
              else 
              { CreationStatement cs = 
                   new CreationStatement(old_occ,occ); 
                stats.add(0,cs); 
                declared.add(occ); 
              } 

              newexpr = newexpr.substituteEq(oname, 
                            new BasicExpression(old_occ)); 
            } 
          }

          AssignStatement asgn = 
            new AssignStatement(parexpr,newexpr);       
          stats.add(asgn);

          /* JOptionPane.showInputDialog("## Assignment " + asgn + " variable uses " + vuses + " previous " + previousVars + " clash " + previousVarOccurs); */  
        }  
      }

      previousVars.add(par);  
    } // if expr contains an earlier lhs variable v
      // insert a var v_pre : vType := v declaration at start 
      // and use v_pre in expr instead of v. 

    if (stats.size() == 0) 
    { return null; } 

    return new SequenceStatement(stats); 
  } 


  // Check completeness: if post updates v but not w when w data depends on v

  public void removeUnusedStatements()
  { if (activity == null) { return; } 

    Statement newact = 
       Statement.removeUnusedStatements(activity); 
    activity = newact; 
  } 

  public void unfoldOperationCall(String nme, Statement defn)
  { if (activity == null) { return; } 

    Statement newact = 
       Statement.unfoldCall(activity, nme, defn); 
    activity = newact; 
  } 

  public Statement replaceRecursionByIteration(Statement act)
  { // if there are simple calls to itself, replace by 
    // continue/break in a loop. 

    // ... ; self.nme() ; *** 
    // is  while true do (...)

    // ... ; self.nme(exprs) ; ***
    // is  while true do (... ; pars := exprs)

    // ... ; if E then self.nme() else skip ; ***
    // is   while true do (... ; if E then continue else 
    //                     skip) ; ***

    // ... ; if E then self.nme(exprs) else skip ; ***
    // is   while true do (... ; 
    //         if E then (pars := exprs ; continue) else 
    //                     skip) ; ***
    // 

    Statement oldact = act; 
    if (oldact == null) 
    { oldact = activity; } 
    if (oldact == null) 
    { return act; } 

    String nme = getName(); 

    Statement loopBdy =             
       Statement.replaceSelfCallsByContinue(
                                     this,nme,oldact);

    WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBdy); 

    System.out.println(">>> Restructured code: " + ws); 
    System.out.println(); 

    return ws;
  }

  public Statement selfCalls2Loops(Statement act)
  { // if there are simple calls to itself, replace by 
    // continue/break in a loop. 

    // ... ; self.nme() ; *** 
    // is  while true do (...)

    // ... ; self.nme(exprs) ; ***
    // is  while true do (... ; pars := exprs)

    // ... ; if E then self.nme() else skip ; ***
    // is   while true do (... ; if E then continue else 
    //                     skip) ; ***

    // ... ; if E then self.nme(exprs) else skip ; ***
    // is   while true do (... ; 
    //         if E then (pars := exprs ; continue) else 
    //                     skip) ; ***
    // 

    Statement oldact = act; 
    if (oldact == null) 
    { oldact = activity; } 
    if (oldact == null) 
    { return act; } 

    String nme = getName(); 

    Vector contexts = new Vector(); 
    Vector remainders = new Vector(); 

    Vector opcalls = Statement.getOperationCallsContexts(
                              nme,oldact,contexts,remainders); 

    // System.out.println(opcalls); 
    // System.out.println(contexts); 
    // System.out.println(remainders); 

    int selfcalls = 0; 
    Vector branches = new Vector(); 
    Vector rems = new Vector(); 
    Vector assigns = new Vector(); 

    for (int i = 0; i < opcalls.size(); i++) 
    { Statement opcall = 
          (Statement) opcalls.get(i); 
      Vector cntx = (Vector) contexts.get(i); 
      Vector rem = (Vector) remainders.get(i); 
      Expression expr = null; 
      if (opcall instanceof InvocationStatement)
      { expr = ((InvocationStatement) opcall).getCallExp(); } 
      else if (opcall instanceof ReturnStatement)
      { expr = ((ReturnStatement) opcall).getReturnValue(); }  

      // if ((expr + "").startsWith("self." + nme + "("))

      if (expr != null && expr.isSelfCall(nme))
      { selfcalls++; 
        branches.add(cntx); 
        rems.add(rem); 
        // System.out.println(cntx);
        Statement parAssigns = parameterAssignments(expr);
        assigns.add(parAssigns);  
      } 
    } 

    System.out.println(">>> There are " + selfcalls + 
                       " calls of self." + nme + "() in " + 
                       oldact); 
    System.out.println(">>> Branches to calls: " + branches); 
    System.out.println(">>> Remainders: " + rems); 
    System.out.println(">>> Assignments: " + assigns); 

    if (selfcalls <= 0) 
    { return act; } 

    if (selfcalls == 1) 
    { int br = 0; 

      // simple case. The call is the last item in 
      // branches[br]. Either a direct call or conditional.

      SequenceStatement loopBody = new SequenceStatement(); 

      Vector branch = (Vector) branches.get(br); 
      int blen = branch.size(); 
      for (int i = 0; i < blen - 1; i++) 
      { Statement sx = (Statement) branch.get(i); 
        loopBody.addStatement(sx); 
      } 
      
      Statement callAssigns = (Statement) assigns.get(br); 
       

      Vector remainder = (Vector) rems.get(br); 
      Object selfcall = branch.get(blen-1); 

      if (selfcall instanceof Statement)      
      { if (callAssigns != null && 
            callAssigns instanceof SequenceStatement) 
        { loopBody.addStatements(
                     (SequenceStatement) callAssigns); 
        }

        WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBody); 

        System.out.println(">A> Restructured code: " + ws); 
        System.out.println(); 

        return ws;
      } 
      else if (selfcall instanceof Vector)
      { // conditional cases
        Vector selfcallv = (Vector) selfcall; 
        if (selfcallv.size() == 4 && 
            "if".equals(selfcallv.get(0) + ""))      
        { Expression tst = (Expression) selfcallv.get(1);
          Vector sts = (Vector) selfcallv.get(2); 
          Statement cde = 
            Statement.replaceSelfCallByContinue(
                                      nme,sts,callAssigns);
          Statement elsePart = (Statement) selfcallv.get(3); 
  
          Statement newelse = 
            SequenceStatement.combineSequenceStatements(
                            elsePart,new BreakStatement()); 
          ConditionalStatement cs = 
            new ConditionalStatement(tst,
                cde, 
                newelse); 
          loopBody.addStatement(cs); 

          WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBody); 
          SequenceStatement res = new SequenceStatement(); 
          res.addStatement(ws); 
          res.addStatements(remainder); 

          System.out.println(">0> Restructured code: " + res); 
          System.out.println(); 

          return res;
        } 
        else if (selfcallv.size() == 4 && 
                 "else".equals(selfcallv.get(0) + ""))      
        { Expression tst = (Expression) selfcallv.get(1);
          Statement ifpart = (Statement) selfcallv.get(2); 

          Vector sts = (Vector) selfcallv.get(3); 
          Statement cde = 
            Statement.replaceSelfCallByContinue(
                                  nme,sts,callAssigns);  
          Statement newif = 
            SequenceStatement.combineSequenceStatements(
                            ifpart,new BreakStatement()); 
          ConditionalStatement cs = 
            new ConditionalStatement(tst, 
                newif, cde); 
          loopBody.addStatement(cs);
 
          WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBody); 
          SequenceStatement res = new SequenceStatement(); 
          res.addStatement(ws); 
          res.addStatements(remainder); 

          System.out.println(">1> Restructured code: " + res); 
          System.out.println(); 

          return res;
        } 
      } 
    }
    else if (selfcalls == 2) 
    { int br = 0; 

      // if-else case. The calls are last item in 
      // branches[0] == branches[1]. 
      // A conditional that is last item of the branch.

      SequenceStatement loopBody = new SequenceStatement(); 

      Vector branch = (Vector) branches.get(br); 
      int blen = branch.size(); 
      for (int i = 0; i < blen - 1; i++) 
      { Statement sx = (Statement) branch.get(i); 
        loopBody.addStatement(sx); 
      } 
      
      Statement callAssigns1 = (Statement) assigns.get(0); 
      Statement callAssigns2 = (Statement) assigns.get(1); 
       
      Vector remainder = (Vector) rems.get(br); 
      Object selfcall = branch.get(blen-1); 
                        // vector representing conditional

      if (selfcall instanceof Vector && 
          (((Vector) selfcall).get(0) + "").equals("ifelse"))
      { // conditional cases
        Vector selfcallv = (Vector) selfcall; 
        if (selfcallv.size() == 4 && 
            "ifelse".equals(selfcallv.get(0) + ""))      
        { Expression tst = (Expression) selfcallv.get(1);
          Vector sts1 = (Vector) selfcallv.get(2); 
          Vector sts2 = (Vector) selfcallv.get(3); 
          Statement cde1 = 
            Statement.replaceSelfCallByContinue(
                                      nme,sts1,callAssigns1);
          Statement cde2 = 
            Statement.replaceSelfCallByContinue(
                                      nme,sts2,callAssigns2);
          
          ConditionalStatement cs = 
            new ConditionalStatement(tst,
                cde1, 
                cde2); 
          loopBody.addStatement(cs); 

          WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBody); 
          SequenceStatement res = new SequenceStatement(); 
          res.addStatement(ws); 
          res.addStatements(remainder); 

          System.out.println(">2> Restructured code: " + res); 
          System.out.println(); 

          return res;
        } 
      }
      else // unrelated branches
      { // replace self.nme(exprs) by pars := exprs; continue 

        Statement loopBdy =             
          Statement.replaceSelfCallsByContinue(
                                        this,nme,oldact);

        WhileStatement ws = new WhileStatement(
                                 new BasicExpression(true),
                                 loopBdy); 

        System.out.println(">3> Restructured code: " + ws); 
        System.out.println(); 

        return ws;
      } 
    } 

    return act;  
  } 

  public void hoistLocalDeclarations()
  { // Find all the local declarations. 
    // Give warnings if 
    //   (i) multiple declarations with same variable name
    //   (ii) declaration with same name as a parameter
    // Apart from case (ii) replace declarations by assigns
    // in the code and put all declarations at the start 

    if (activity == null) 
    { return; } 

    Vector localdecs = 
      Statement.getLocalDeclarations(activity); 
    
    Vector initialDecs = new Vector(); 
    Vector varnames = new Vector(); 
    
    for (int i = 0; i < localdecs.size(); i++) 
    { CreationStatement cs = (CreationStatement) localdecs.get(i); 
      String vname = cs.assignsTo;

      ModelElement par = 
          ModelElement.lookupByName(vname, parameters); 

      if (par != null) 
      { System.err.println("!! Code Smell (MDV): " + vname + " is both a parameter and a local variable!"); 
        continue; 
      }
  
      if (varnames.contains(vname))
      { System.err.println("!! Code Smell (MDV): multiple declarations in same scope for variable " + vname); 
        continue;
      } 

      System.out.println(">>> Local declaration " + cs); 

      varnames.add(vname); 
      initialDecs.add(cs.defaultVersion()); 
    } 

    if (varnames.size() == 0) 
    { return; } 

    Statement newactivity = 
      Statement.replaceLocalDeclarations(activity,varnames); 
    SequenceStatement ss = new SequenceStatement(initialDecs);
    ss.addStatement(newactivity); 
   
    System.out.println(">>> New activity: " + ss); 
    activity = ss; 
  } 

  public void reduceCodeNesting()
  { // Replace each conditional  
    // if cond then stats1 else stats2 
    // by 
    // if cond then stats1 else skip; stats2
    // where all code paths in stats1 end with a return. 

    if (activity == null) 
    { return; } 

    Statement newactivity = 
      Statement.replaceElseBySequence(activity); 
       
    System.out.println(">>> New activity: " + newactivity); 
    activity = newactivity; 
  } 
}


