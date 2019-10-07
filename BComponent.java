import java.util.Vector; 
import java.util.List; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BComponent extends ModelElement
{ private static final Vector basicTypes; 
  static { basicTypes = new Vector(); 
           basicTypes.add("int"); 
           basicTypes.add("double");
           basicTypes.add("boolean"); 
         } 

  private Vector seesList = new Vector(); // String
  private Vector usesList = new Vector(); // String
  private Vector includesList = 
                            new Vector(); // String
  private Vector promotes = new Vector(); // String
  private Vector types = new Vector(); // BTypeDec
  private Vector constants = new Vector(); // String
  private Vector properties = new Vector(); // BExpression
  private Vector variables = new Vector(); // String
  private Vector attributes = new Vector(); // Attribute
  private Vector roles = new Vector(); // Association
  private Vector constraints = new Vector(); // Constraint
  private Vector invariants = new Vector(); // BExpression
  private Vector initialisation = new Vector(); // BStatement
  private Vector operations = new Vector(); // BOp
  
  private BExpression objs; // represents cs
  private BExpression cobj; // represents C_OBJ

  public BComponent(String nme, Entity superclass)
  { super(nme);
    
    String objset = nme.toLowerCase() + "s";
    objs = new BBasicExpression(objset);
    cobj = new BBasicExpression(nme + "_OBJ");
    if (superclass == null) 
    { BExpression inv0 =
        new BBinaryExpression("<:",objs,cobj);
      inv0.setCategory(BExpression.ROOT); 
      invariants.add(0,inv0);  // cs <: C_OBJ
    } 
    
    variables.add(objset);
    BExpression empty = 
                  new BSetExpression(new Vector());
    initialisation.add(
                    new BAssignStatement(objs,empty));
  }  // probably addSees("Bool_TYPE"), INT, STRING

  public BComponent(Entity e,Vector entities,Vector types, Vector cons)
  { this(e.getName(),e.getSuperclass());
    addSees("SystemTypes"); 
    Vector atts = e.getAttributes();
    Vector asocs = e.getAssociations();
    Vector subs = e.getSubclasses();
    Vector invs = e.getInvariants(); 
    Vector interfaces = e.getInterfaces(); 
    constraints = invs; 
    Vector ops = e.getOperations(); 
    
    System.out.println("*** CREATING B Component: " + e + 
                       " Local invariants are:\n" + 
                       invs + " global are:\n" + cons); 
    // cons should actually be invs

    for (int i = 0; i < interfaces.size(); i++) 
    { Entity intf = (Entity) interfaces.get(i); 
      BExpression iobj = new BBasicExpression(intf.getName() + "_OBJ"); 
      BExpression inv0 =
        new BBinaryExpression("<:",cobj,iobj);
      inv0.setCategory(BExpression.ROOT); 
      invariants.add(0,inv0);  // C_OBJ <: I_OBJ
      addSees(intf.getName()); 
    } 

    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i);
      addAttribute(att,e,entities,types,cons);
    }

    Vector allinvfeats = Constraint.allFeaturesUsedIn(invs); 
    Vector oldatts = e.allInheritedAttributes(); 
    for (int j = 0; j < oldatts.size(); j++) 
    { Attribute oldatt = (Attribute) oldatts.get(j); 
      if (allinvfeats.contains(oldatt.getName()))
      { addsetOperation(oldatt,e,entities,types,cons); } 
    } // likewise for associations


    for (int j = 0; j < asocs.size(); j++)
    { Association ast = (Association) asocs.get(j);
      String role2 = ast.getRole2();
      addRole(role2,ast,e,entities,types,cons);
    }

    /* if (e.isAssociationClass())
    { Association astcl = e.getLinkedAssociation(); 
      Entity e1 = astcl.getEntity1(); 
      Entity e2 = astcl.getEntity2(); 
      String r1nme = e1.getName().toLowerCase(); 
      String r2nme = e2.getName().toLowerCase(); 
      Association virtual1 = 
        new Association(e,e1,ModelElement.MANY,ModelElement.ONE,null,r1nme); 
      Association virtual2 = 
        new Association(e,e2,ModelElement.MANY,ModelElement.ONE,null,r2nme); 
      addRole(r1nme,virtual1,e,entities,types,cons);
      addRole(r2nme,virtual2,e,entities,types,cons);   
    }  */ // add roles from it to end classes

  
    Vector oldasts = e.allInheritedAssociations(); 
    for (int j = 0; j < oldasts.size(); j++) 
    { Association oldast = (Association) oldasts.get(j); 
      if (allinvfeats.contains(oldast.getRole2()))
      { addsetOperation(oldast,e,entities,types,cons); } 
    } 



    for (int q = 0; q < ops.size(); q++)
    { BehaviouralFeature op = (BehaviouralFeature) ops.get(q); 
      BOp bop = op.getBOperationCode(e,entities,types); 
      if (bop != null)
      { operations.add(bop); }
      if (op.isQuery())
      { constants.add(op.getName() + "_" + e); 
        properties.add(op.buildBConstantDefinition(e,entities,types)); 
      } 
    }



    if (subs.size() > 0)
    { for (int k = 0; k < subs.size(); k++)
      { Entity sb = (Entity) subs.get(k);
        BComponent bs = new BComponent(sb,entities,types,sb.getInvariants());
        union(bs,e,sb);
        addSubclassAxiom(sb);
        usesList.remove(sb.getName()); 
      }
    }

    for (int p = 0; p < invs.size(); p++) 
    { Constraint inv = (Constraint) invs.get(p); 
      if (inv.getEvent() != null) { continue; } 
      BExpression binv = inv.binvariant(); 
      binv.setCategory(BExpression.LOGICAL); 
      invariants.add(binv); 
    } 
    
    if (e.hasStereotype("target")) { } 
    else if (e.getSuperclass() == null)
    { buildConstructor(e); }

    usesList.remove(getName()); 
  } // if e abstract, no constructor, and union
    // of subclass sets equals it.

  public BComponent(String nme,Vector interfaces)  // for an interface
  { super(nme);
    cobj = new BBasicExpression(nme + "_OBJ");
    for (int i = 0; i < interfaces.size(); i++) 
    { Entity intf = (Entity) interfaces.get(i); 
      BExpression iobj = new BBasicExpression(intf.getName() + "_OBJ"); 
      BExpression inv0 =
        new BBinaryExpression("<:",cobj,iobj);
      inv0.setCategory(BExpression.ROOT); 
      invariants.add(inv0);  // C_OBJ <: I_OBJ
      addSees(intf.getName()); 
    } 
  }  // probably addSees("Bool_TYPE"), INT, STRING

  // For interfaces: 
  public BComponent(Vector entities, Vector types, Vector cons, Entity e)
  { this(e.getName(),e.getInterfaces());
    addSees("SystemTypes"); 
    Vector atts = e.getAttributes();
    Vector invs = e.getInvariants(); 
    constraints = invs; 
    Vector ops = e.getOperations(); 

    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i);
      if (att.isClassScope() && att.isFrozen())
      { addInterfaceAttribute(att,e,new Vector(),types,cons); }
    }

    for (int p = 0; p < invs.size(); p++) 
    { Constraint inv = (Constraint) invs.get(p); 
      if (inv.getEvent() != null) { continue; } 
      BExpression binv = inv.binvariant(); 
      binv.setCategory(BExpression.LOGICAL); 
      invariants.add(binv); 
    } 

    for (int q = 0; q < ops.size(); q++)
    { BehaviouralFeature op = (BehaviouralFeature) ops.get(q); 
      BOp bop = op.getBOperationCode(e,entities,types); 
      if (bop != null)
      { operations.add(bop); } 
      if (op.isQuery())
      { constants.add(op.getName() + "_" + e);  
        properties.add(op.buildBConstantDefinition(e,entities,types));
      }  
    }
    
    usesList.remove(getName()); 
  } // if e abstract, no constructor, and union
    // of subclass sets equals it.


  public void clearVariables()
  { variables = new Vector(); 
    invariants = new Vector(); 
    initialisation = new Vector(); 
  }

  public Vector getSees()
  { return seesList; } 

  public Vector getIncludes()
  { return includesList; } 

  public Vector getUses()
  { return usesList; } 

  public void removeSees(List names)
  { seesList.removeAll(names); } 

  public void addSees(String s)
  { seesList.add(s); }  // just strings at present

  public void unionSees(List l)
  { seesList.addAll(l); } 

  public void addIncludes(String i)
  { includesList.add(i); } 

  public void removeUses(List names)
  { usesList.removeAll(names); } 

  public void addInvariant(BExpression inv)
  { invariants.add(inv); } 

  public void addInvariants(Vector invs)
  { invariants.addAll(invs); } 

  public void addOperation(BOp op)
  { operations.add(op); } 

  public void addAttribute(Attribute att, Entity ent,
                           Vector entities,Vector types, Vector invs)
  { attributes.add(att);
    String attnme = att.getName();
    variables.add(attnme);
    // add invariant  att: cs --> T
    Type t = att.getType();
    String bt = t.generateB();

    String newSeen = null;
    if (bt.equals("INT"))
    { newSeen = "Int_TYPE"; }
    else if (bt.equals("BOOL"))
    { newSeen = "Bool_TYPE"; }
    else if (bt.equals("STRING"))
    { newSeen = "String_TYPE"; }
    if (newSeen != null & !seesList.contains(newSeen))
    { seesList.add(newSeen); }
 
    BExpression ran = new BBasicExpression(bt);
    BExpression attbe = 
      new BBasicExpression(attnme);
    BExpression attxbe = 
      new BBasicExpression(attnme + "xx"); 
    BExpression fun; 
    if (att.isUnique())
    { fun = new BBinaryExpression(">->",objs,ran); } 
    else if (att.isClassScope())
    { fun = ran; } // also modify Set and Get
    else 
    { fun = new BBinaryExpression("-->",objs,ran); }
    BExpression atttyping = new BBinaryExpression(":",attbe,fun); 
    atttyping.setCategory(BExpression.FEATURE); 
    invariants.add(atttyping);

    // add  att := {}  to init:
    BExpression empty = 
                  new BSetExpression(new Vector());
    if (att.isInstanceScope())
    { initialisation.add(
                    new BAssignStatement(attbe,empty));
    } // else get default value of type
    else
    { String defval = att.getDefaultValue();
      BExpression bdef = new BBasicExpression(defval);
      initialisation.add(
                    new BAssignStatement(attbe,bdef));
    }

    // add setatt and getatt operations:
    String cx = getName().toLowerCase() + "x";
    BOp getatt;
    if (att.isInstanceScope())
    { getatt =
        BOp.buildGetOperation(attnme,cx,objs);
    }
    else 
    { getatt = BOp.buildStaticGetOperation(attnme); }

    operations.add(getatt);

    if (ent.hasStereotype("target")) { return; } 

    if (att.isUpdatable())
    { BOp setatt;
      BOp updatt; 
      BOp setAllatt; 
      if (att.isInstanceScope())
      { setatt =
          BOp.buildSetOperation(attnme,cx,objs,ran,att.isUnique(),null,0,ModelElement.ONE,
                                ent,entities,types,invs); 
        setatt.setSignature("set" + attnme); // ???
        /* BExpression possiblePre = Constraint.conjoinAllSelected(attnme,
                                                                getName(),
                                                                constraints);
        BExpression preset = 
          possiblePre.substituteEq(attnme + "(" + cx + ")",attxbe); 
        System.out.println("POSIBLE PRE: " + preset); 
        setatt.addPre(preset);
        */ 
       
        updatt = 
          BOp.buildUpdateOperation(attnme,objs,null,ran,att.isUnique());
        setAllatt =
          BOp.buildSetAllOperation(attnme,cx,objs,ran);
        operations.add(setatt); 
        operations.add(updatt);
        operations.add(setAllatt); 
      }
      else 
      { setatt = BOp.buildStaticSetOperation(attnme,cx,objs,ran,att.isUnique(),                                                    ModelElement.ONE,ent,
                                             entities,types,invs); 
        if (setatt != null) 
        { operations.add(setatt); }
      }
    } 
  }

  public void addsetOperation(Attribute att, Entity ent,
                              Vector entities,Vector types, Vector invs)
  { String attnme = att.getName();
    Type t = att.getType();
    String bt = t.generateB();
 
    BExpression ran = new BBasicExpression(bt);
    BExpression attbe = 
      new BBasicExpression(attnme);
    BExpression attxbe = 
      new BBasicExpression(attnme + "xx"); 

    String cx = getName().toLowerCase() + "x";  // e.getName()...

    if (att.isUpdatable())
    { BOp setatt;
      BOp updatt; 
      BOp setAllatt; 
      if (att.isInstanceScope())
      { setatt =
          BOp.buildSetOperation(attnme,cx,objs,ran,att.isUnique(),null,0,ModelElement.ONE,
                                ent,entities,types,invs);        
        System.out.println("Invs are: " + invs); 
        System.out.println("For " + attnme + " " + cx + " " + ent); 
        System.out.println(setatt); 
        setatt.setSignature("set" + attnme); 
        // updatt = 
        //  BOp.buildUpdateOperation(attnme,objs,ran,att.isUnique());
        // setAllatt =
        //   BOp.buildSetAllOperation(attnme,cx,objs,ran);
        operations.add(setatt); 
        // operations.add(updatt);
        // operations.add(setAllatt); 
      }
    }
  }

  public void addsetOperation(Association ast, Entity ent,
                              Vector entities,Vector types, Vector invs)
  { String attnme = ast.getRole2();
    String role1 = ast.getRole1(); 
    boolean isseq = ast.isOrdered(); 
    Entity ent2 = ast.getEntity2(); // must USE unless in same inherit family
    String e2name = ent2.getName();
    String e2s = e2name.toLowerCase() + "s"; 
    // role: objs --> ent2s or FIN(ent2s)
    int card1 = ast.getCard1(); 
    int card2 = ast.getCard2(); 
    BExpression targ;
    BExpression elemtype; 
    if (card2 == ONE)
    { targ = new BBasicExpression(e2s); 
      elemtype = targ; 
    }
    else 
    { elemtype = new BBasicExpression(e2s); 
      if (isseq)
      { targ = new BUnaryExpression("seq",elemtype); } 
      else 
      { targ = new BUnaryExpression("FIN",elemtype); }
    }
 
    BExpression attbe = 
      new BBasicExpression(attnme);
    BExpression attxbe = 
      new BBasicExpression(attnme + "xx"); 


    String cx = getName().toLowerCase() + "x";  // e.getName()...
    BExpression cxbe = new BBasicExpression(cx); 

    BExpression possiblePre = Constraint.conjoinAllSelected(attnme,getName(),
                                                            invs);

    if (!ast.isFrozen())
    { BOp setrole = 
          BOp.buildSetOperation(attnme,cx,objs,targ,ast.isInjective(),role1,card1,card2,
                                ent,entities,types,invs);
      System.out.println("Invs are: " + invs); 
      System.out.println("For " + attnme + " " + cx + " " + ent); 
      System.out.println(setrole); 
      setrole.setSignature("set" + attnme); 
        // updatt = 
        //  BOp.buildUpdateOperation(attnme,objs,ran,att.isUnique());
        // setAllatt =
        //   BOp.buildSetAllOperation(attnme,cx,objs,ran);
      operations.add(setrole); 
        // operations.add(updatt);
        // operations.add(setAllatt); 
    }
    if (card2 != ONE)
    { if (!ast.isFrozen())
      { BOp addop = BOp.buildAddOperation(attnme,cx,objs,elemtype,role1,card1,card2,isseq,
                                          ent,entities,types,invs); 
        BExpression rolecx = new BApplyExpression(attnme,cxbe); 
        BSetExpression setrolexbe = new BSetExpression(); 
        setrolexbe.addElement(attxbe); 
        setrolexbe.setOrdered(isseq); 
        BExpression sub;
        if (isseq)
        { sub = new BBinaryExpression("^",rolecx,setrolexbe); } 
        else 
        { sub = new BBinaryExpression("\\/",rolecx,setrolexbe); } 
        BExpression preset2 = possiblePre.substituteEq(attnme + "(" + cx + ")",sub);
        System.out.println("POSIBLE PRE: " + preset2);
        addop.addPre(preset2);  

        addop.setSignature("add" + attnme); 
        operations.add(addop);
       
        if (isseq) 
        { BOp setindop = BOp.buildSetIndOp(attnme,cx,objs,elemtype,ent); 
          operations.add(setindop); 
        } 

        if (!ast.isAddOnly())
        { BOp delop = BOp.buildDelOperation(attnme,cx,objs,elemtype,
                                            role1,card1,ent,entities,types,invs);
          BExpression sub2 = new BBinaryExpression("-",rolecx,setrolexbe); 
          BExpression preset3 = 
            possiblePre.substituteEq(attnme + "(" + cx + ")",sub);
          System.out.println("POSIBLE PRE: " + preset3);
          delop.addPre(preset3);
          delop.setSignature("remove" + attnme); 
          operations.add(delop); 
        }
      }
    } // also use contraints, and these ops for the Controller
  } // plus cardinality preconditions


  public void addInterfaceAttribute(Attribute att, Entity ent,
                                    Vector entities,Vector types, Vector invs)
  { attributes.add(att);
    String attnme = att.getName();
    variables.add(attnme);
    // add invariant  att: cs --> T
    Type t = att.getType();
    String bt = t.generateB();

    String newSeen = null;
    if (bt.equals("INT"))
    { newSeen = "Int_TYPE"; }
    else if (bt.equals("BOOL"))
    { newSeen = "Bool_TYPE"; }
    else if (bt.equals("STRING"))
    { newSeen = "String_TYPE"; }
    if (newSeen != null & !seesList.contains(newSeen))
    { seesList.add(newSeen); }
 

    BExpression ran = new BBasicExpression(bt);
    BExpression attbe = 
      new BBasicExpression(attnme);
    BExpression attxbe = 
      new BBasicExpression(attnme + "xx"); 
    BExpression fun; 
    if (att.isUnique())
    { fun = new BBinaryExpression(">->",objs,ran); } 
    else if (att.isClassScope())
    { fun = ran; } // also modify Set and Get
    else 
    { fun = new BBinaryExpression("-->",objs,ran); }
    BExpression atttyping = new BBinaryExpression(":",attbe,fun); 
    atttyping.setCategory(BExpression.FEATURE); 
    invariants.add(atttyping);

    // add  att := {}  to init:
    BExpression empty = 
                  new BSetExpression(new Vector());
    if (att.isInstanceScope())
    { initialisation.add(
                    new BAssignStatement(attbe,empty));
    } // else get default value of type
    else
    { String defval = att.getDefaultValue();
      BExpression bdef = new BBasicExpression(defval);
      initialisation.add(
                    new BAssignStatement(attbe,bdef));
    }

    // add setatt and getatt operations:
    BOp getatt = BOp.buildStaticGetOperation(attnme);

    operations.add(getatt);
  }

  public void addRole(String role, Association ast, Entity ent,
                      Vector entities,Vector types, Vector invs)
  { variables.add(role);
    roles.add(ast);
    String role1 = ast.getRole1(); 
    int card1 = ast.getCard1(); 
    int card2 = ast.getCard2();
    boolean isseq = ast.isOrdered(); 
    boolean isqual = ast.isQualified(); 

    Entity ent2 = ast.getEntity2(); // must USE unless in same inherit family
    String e2name = ent2.getName(); 
    String e2top = ent2.getTopSuperclass() + "";
    if (usesList.contains(e2top)) { }
    else { usesList.add(e2top); } // Beware of cycles
    String e2s = e2name.toLowerCase() + "s"; 
    // role: objs --> ent2s or FIN(ent2s)
    BExpression targ;
    BExpression elemtype; 
    if (card2 == ONE)
    { targ = new BBasicExpression(e2s); 
      elemtype = targ; 
      if (isqual)
      { BExpression stype = new BBasicExpression("STRING"); 
        targ = new BBinaryExpression("+->",stype,targ);
        targ.setBrackets(true); 
      }
    }
    else 
    { elemtype = new BBasicExpression(e2s); 
      if (isseq)
      { targ = new BUnaryExpression("seq",elemtype); } 
      else if (isqual)
      { BExpression stype = new BBasicExpression("STRING"); 
        targ = new BBinaryExpression("+->",stype,
                     new BUnaryExpression("FIN",elemtype));
        targ.setBrackets(true); 
      } 
      else 
      { targ = new BUnaryExpression("FIN",elemtype); }
    }

    BExpression rolebe = 
      new BBasicExpression(role);
    BExpression rolexbe = 
      new BBasicExpression(role + "xx");

    BExpression fun; 
    if (ast.isInjective() && !isqual)
    { fun = new BBinaryExpression(">->",objs,targ); } 
    else 
    { fun = new BBinaryExpression("-->",objs,targ); }
    BExpression roletyping = new BBinaryExpression(":",rolebe,fun); 
    roletyping.setCategory(BExpression.FEATURE); 
    invariants.add(roletyping);

    // add  role := {}  to init:
    BExpression empty = 
                  new BSetExpression(new Vector());
    initialisation.add(
                    new BAssignStatement(rolebe,empty));

    String cx = getName().toLowerCase() + "x";
    BExpression cxbe = new BBasicExpression(cx);

    if (card2 == ZEROONE && !isqual)
    { BExpression rhs = new BApplyExpression(role,cxbe);
      BExpression cardleq =
        new BBinaryExpression("<=", 
            new BUnaryExpression("card",rhs),
              new BBasicExpression("1"));
      BExpression lhs = new BBinaryExpression(":",cxbe,objs); 
      BExpression cardinv =
        new BQuantifierExpression("forall",cx,
               new BBinaryExpression("=>",lhs,cardleq));
      cardinv.setCategory(BExpression.LOGICAL); 
      invariants.add(cardinv);
    }

    if (role1 != null && role1.length() > 0)  
    { // add appropriate inverse constraint to invariants
      // !(ax,bx).(ax : as & bx : bs => (bx : br(ax) <=> ax : ar(bx)) )
      BExpression e2objs = new BBasicExpression(e2s); 
      String e2x = e2name.toLowerCase() + "x"; 
      BExpression e2xbe = new BBasicExpression(e2x); 

      BExpression role1app = new BApplyExpression(role1,e2xbe);
      BExpression role2app = new BApplyExpression(role,cxbe);
      BExpression end1exp; 
      BExpression end2exp; 
      if (card1 == ONE)
      { end1exp = new BBinaryExpression("=",cxbe,role1app); }
      else 
      { end1exp = new BBinaryExpression(":",cxbe,role1app); }
      if (card2 == ONE)
      { end2exp = new BBinaryExpression("=",e2xbe,role2app); }
      else 
      { end2exp = new BBinaryExpression(":",e2xbe,role2app); }
   
      BExpression equivends =
        new BBinaryExpression("=>",end1exp,end2exp);  
      equivends.setBrackets(true); 

      BExpression lhs2 = new BBinaryExpression(":",e2xbe,e2objs); 
      BExpression invinv1 =
        new BQuantifierExpression("forall",e2x,
               new BBinaryExpression("=>",lhs2,equivends));
      BExpression lhs1 = new BBinaryExpression(":",cxbe,objs); 
      BExpression invinv2 =
        new BQuantifierExpression("forall",cx,
               new BBinaryExpression("=>",lhs1,invinv1));
      invinv2.setCategory(BExpression.LOGICAL);       
      invariants.add(invinv2);
    }      


    BOp getrole =
          BOp.buildGetOperation(role,cx,objs);
    operations.add(getrole);

    if (ent.hasStereotype("target")) { return; } 

    if (isqual)
    { operations.add(BOp.buildGetQualOperation(role,cx,objs)); 
      BOp setqop = BOp.buildSetQualOp(role,cx,objs,elemtype,ent);
      operations.add(setqop); 
      if (card2 != ONE)
      { BOp qadd = BOp.buildAddQualOperation(role,cx,objs,elemtype,card2,isseq,
                                             ent,entities,types,invs);
        operations.add(qadd); 
        BOp qrem = BOp.buildDelQualOperation(role,cx,objs,elemtype,
                                             ent,entities,types,invs);
        operations.add(qrem); 
      } 
      return; 
    } 

    BExpression possiblePre = Constraint.conjoinAllSelected(role,getName(),
                                                            constraints);

    if (!ast.isFrozen())
    { BOp setrole = 
          BOp.buildSetOperation(role,cx,objs,targ,ast.isInjective(),role1,card1,card2,
                                ent,entities,types,invs);
      // BExpression preset = 
      //     possiblePre.substituteEq(role + "(" + cx + ")",rolexbe);
      // System.out.println("POSIBLE PRE: " + preset);
      // setrole.addPre(preset);  
      setrole.setSignature("set" + role); 
      
      BOp updrole = 
          BOp.buildUpdateOperation(role,objs,ast,targ,ast.isInjective()); 
      operations.add(setrole);
      operations.add(updrole); 
    }

    if (card2 != ONE)
    { if (!ast.isFrozen())
      { BOp addop = BOp.buildAddOperation(role,cx,objs,elemtype,role1,card1,card2,isseq,
                                          ent,entities,types,invs); 
        BExpression rolecx = new BApplyExpression(role,cxbe); 
        BSetExpression setrolexbe = new BSetExpression(); 
        setrolexbe.addElement(rolexbe); 
        setrolexbe.setOrdered(isseq); 
        BExpression sub;
        if (isseq)
        { sub = new BBinaryExpression("^",rolecx,setrolexbe); } 
        else 
        { sub = new BBinaryExpression("\\/",rolecx,setrolexbe); } 
        BExpression preset2 = possiblePre.substituteEq(role + "(" + cx + ")",sub);
        System.out.println("POSIBLE PRE: " + preset2);
        addop.addPre(preset2);  

        addop.setSignature("add" + role); 
        operations.add(addop);
       
        if (isseq) 
        { BOp setindop = BOp.buildSetIndOp(role,cx,objs,elemtype,ent); 
          operations.add(setindop); 
        } 

        if (!ast.isAddOnly())
        { BOp delop = 
            BOp.buildDelOperation(role,cx,objs,elemtype,role1,card1,ent,entities,types,invs);
          BExpression sub2 = new BBinaryExpression("-",rolecx,setrolexbe); 
          BExpression preset3 = possiblePre.substituteEq(role + "(" + cx + ")",sub);
          System.out.println("POSIBLE PRE: " + preset3);
          delop.addPre(preset3);
          delop.setSignature("remove" + role); 
          operations.add(delop); 
        }
      }
    } // also use contraints, and these ops for the Controller
  } // plus cardinality preconditions
  // if (card2 == ZEROONE)
  // { card(role2x) <= 1  } etc

  public void addSetDefinition(String nme, Vector vals)
  { if (basicTypes.contains(nme)) 
    { return; } 
    BTypeDef td = new BTypeDef(nme,vals);
    types.add(td);
  }

  public void buildConstructor(Entity e)
  { buildConstructor(e,"oo",null,new Vector(),new Vector()); }

  private void buildConstructor(Entity e, String res, BExpression pre0, 
                                Vector params0, Vector assigns) 
  { System.out.println("Building constructor for " + e + 
                       " with invariants " + e.getInvariants()); 
    Vector eatts = e.getAttributes(); 
    String nme = e.getName(); 
    String objset = nme.toLowerCase() + "s";
    BExpression eobjs = new BBasicExpression(objset);
    java.util.Map env = new java.util.HashMap(); 

    BExpression localInvPre = Constraint.conjoinAll(e.getInvariants()); 
                                                  // constraints?    
    Vector oldatts = e.allInheritedAttributes(); 
    Vector oldasts = e.allInheritedAssociations(); 
    for (int i = 0; i < oldatts.size(); i++) 
    { Attribute oldatt = (Attribute) oldatts.get(i); 
      String oldattnme = oldatt.getName(); 
      // localInvPre = localInvPre.substituteEq(oldattnme,
      //                             new BBasicExpression(oldattnme + "x"));
    }
    for (int i = 0; i < oldasts.size(); i++) 
    { Association oldast = (Association) oldasts.get(i); 
      String oldastnme = oldast.getRole2(); 
      // localInvPre = localInvPre.substituteEq(oldastnme,
      //                             new BSetExpression());
    } // unless the association is initialised ...


    int natts = eatts.size();
    String[] attparams = new String[natts]; 
    Vector params = (Vector) params0.clone(); 
    BExpression[] attbes = new BExpression[natts];
    BExpression[] attpres = new BExpression[natts];
    BExpression pre = 
      new BBinaryExpression("/=",eobjs,cobj);
    if (pre0 != null)
    { pre = pre0; } 

    Vector attassigns = (Vector) assigns.clone(); 
    String oox = getName().toLowerCase() + "x";
    BExpression ooxbe = new BBasicExpression(oox);
    // objs := objs \/ { ooxbe }
    Vector newelem = new Vector();
    newelem.add(ooxbe);
    BAssignStatement create = 
      new BAssignStatement(eobjs,
        new BBinaryExpression("\\/",eobjs,
          new BSetExpression(newelem)));
    attassigns.add(create);
    BExpression resbe = new BBasicExpression(res);

    for (int i = 0; i < natts; i++)
    { Attribute att = (Attribute) eatts.get(i);
      String attini = att.getInitialValue(); 
      Expression attiniexp = att.getInitialExpression(); 

      if (att.isInstanceScope())
      { Type typ = att.getType();
        String attname = att.getName();
        String attx = attname + "x";
        attparams[i] = attx; 
        params.add(attparams[i]); 
        attbes[i] = new BBasicExpression(attparams[i]);
        attpres[i] = 
          new BBinaryExpression(":",attbes[i],
              new BBasicExpression(typ.generateB()));
        pre = new BBinaryExpression("&",pre,attpres[i]);
        BExpression lhs = 
          new BApplyExpression(attname,ooxbe);
        if (attiniexp == null || attini.equals(""))
        { attassigns.add( 
            new BAssignStatement(lhs,attbes[i]));
          // localInvPre = localInvPre.substituteEq(attname,attbes[i]);
        } 
        else 
        { BExpression attinibe = attiniexp.binvariantForm(env,true); 
          attassigns.add(new BAssignStatement(lhs,attinibe));
          // localInvPre = localInvPre.substituteEq(attname,attinibe);
        }
 
        if (att.isUnique()) // attx /: ran(att)
        { BExpression uniqpre =
            new BBinaryExpression("/:",attbes[i],
                  new BUnaryExpression("ran",new BBasicExpression(attname)));
          pre = new BBinaryExpression("&",pre,uniqpre);
        }
      }
    }

    Vector eroles = e.getAssociations(); 
    for (int j = 0; j < eroles.size(); j++)
    { Association ast = (Association) eroles.get(j);
      String role = ast.getRole2();
      BExpression lhs2 = 
         new BApplyExpression(role,ooxbe);
      int card2 = ast.getCard2();
      if (card2 >= 1)
      { String rlex = role + "x"; // input param
        params.add(rlex);
        BExpression rlexbe = new BBasicExpression(rlex);
        String e2name = ast.getEntity2().getName();
        String e2s = e2name.toLowerCase() + "s"; 
        BExpression e2be = new BBasicExpression(e2s);
        BExpression rlepre; 
        if (card2 == ONE)
        { rlepre =
            new BBinaryExpression(":",rlexbe,e2be);
          rlexbe.setMultiplicity(ModelElement.ONE); 
        } 
        else // {n}
        { BExpression carde =
            new BBinaryExpression("=",
              new BUnaryExpression("card",rlexbe),
                new BBasicExpression("" + card2));
          rlepre =
            new BBinaryExpression("&",carde,
              new BBinaryExpression("<:",rlexbe,e2be));
          rlexbe.setMultiplicity(ModelElement.MANY); 
        }
        pre = new BBinaryExpression("&",pre,rlepre);
        attassigns.add( 
          new BAssignStatement(lhs2,rlexbe));
        String role1 = ast.getRole1(); 
        if (role1 != null && role1.length() > 0)
        { int card1 = ast.getCard1(); 
          BExpression invrhs =
              BasicExpression.computeInverseBExpressionSet(ast,role1,role,card1,card2,ooxbe,rlexbe); 
          BExpression invlhs = new BBasicExpression(role1); 
          BStatement invstat = new BAssignStatement(invlhs,invrhs); 
          attassigns.add(invstat); 
        } 
        // localInvPre = localInvPre.substituteEq(role,rlexbe); 
      }
      else if (ast.isFrozen())
      { String rlex = role + "x"; // input param
        params.add(rlex);
        BExpression rlexbe = new BBasicExpression(rlex);
        rlexbe.setMultiplicity(ModelElement.ONE); 
        String e2name = ast.getEntity2().getName();
        String e2s = e2name.toLowerCase() + "s"; 
        BExpression e2be = new BBasicExpression(e2s);
        BExpression rlepre; 
        rlepre = new BBinaryExpression("<:",rlexbe,e2be);
        pre = new BBinaryExpression("&",pre,rlepre);
        attassigns.add( 
          new BAssignStatement(lhs2,rlexbe));
        // localInvPre = localInvPre.substituteEq(role,rlexbe); 
      }
      else // MANY, not frozen
      { BExpression rhs = new BSetExpression(new Vector(),ast.isOrdered()); 
        attassigns.add(
          new BAssignStatement(lhs2,rhs)); 
        // localInvPre = localInvPre.substituteEq(role,rhs); 
      }
    }
    Vector allassigns = new Vector(); 
    allassigns.addAll(attassigns); 
    allassigns.add(
      new BAssignStatement(resbe,ooxbe));
    // The WHERE clause:
    BExpression where =
      new BBinaryExpression(":",ooxbe,
        new BBinaryExpression("-",cobj,eobjs));
    Vector anyvars = new Vector();
    anyvars.add(oox);
    BStatement anystat =
      new BAnyStatement(anyvars,where,
            new BParallelStatement(allassigns));
    // System.out.println("Local invariant precondition: " + localInvPre); 
    // BExpression simpre = localInvPre.simplify(); 
    /* if (simpre == null || simpre.toString().equals("true")) { } 
    else 
    { simpre.setBrackets(true); 
      pre = new BBinaryExpression("&",pre,simpre); 
    } */ 
    BOp newC = 
      new BOp("new_" + nme,res,
              params,pre,
              anystat);

    Vector subs = e.getSubclasses(); 
    for (int h = 0; h < subs.size(); h++) 
    { Entity esub = (Entity) subs.get(h); 
      buildConstructor(esub,res,pre,params,attassigns); 
    } 
      
    operations.add(0,newC); // at position 0
  }

  public void generateJava(java.io.PrintWriter out)  // not used
  { out.println("public abstract class " + getName() + " { }"); } 

  public String toString()
  { String res = "MACHINE " + getName() + "\n";
    int sc = seesList.size();
    int uc = usesList.size();
    int ic = includesList.size();
    int pc = promotes.size();
    int tc = types.size();
    int conssize = constants.size(); 
    int propsize = properties.size(); 
    int vc = variables.size();
    int invc = invariants.size();
    int initc = initialisation.size();
    int opc = operations.size();
    if (sc > 0)
    { res = res + "SEES "; 
      for (int i = 0; i < sc; i++)
      { res = res + seesList.get(i); 
        if (i < sc - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (uc > 0)
    { res = res + "USES "; 
      for (int i = 0; i < uc; i++)
      { res = res + usesList.get(i); 
        if (i < uc - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (ic > 0)
    { res = res + "INCLUDES "; 
      for (int i = 0; i < ic; i++)
      { res = res + includesList.get(i); 
        if (i < ic - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (pc > 0)
    { res = res + "PROMOTES "; 
      for (int i = 0; i < pc; i++)
      { res = res + promotes.get(i); 
        if (i < pc - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (tc > 0)
    { res = res + "SETS "; 
      for (int i = 0; i < tc; i++)
      { res = res + types.get(i); 
        if (i < tc - 1)
        { res = res + "; "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (conssize > 0)
    { res = res + "CONSTANTS "; 
      for (int i = 0; i < conssize; i++) 
      { res = res + constants.get(i); 
        if (i < conssize - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    } 
    if (propsize > 0)
    { res = res + "PROPERTIES "; 
      for (int i = 0; i < propsize; i++) 
      { res = res + properties.get(i); 
        if (i < propsize - 1)
        { res = res + " &\n  "; }
        else 
        { res = res + "\n"; }
      }
    } 
    if (vc > 0)
    { res = res + "VARIABLES "; 
      for (int i = 0; i < vc; i++)
      { res = res + variables.get(i); 
        if (i < vc - 1)
        { res = res + ", "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (invc > 0)
    { res = res + "INVARIANT "; 
      for (int i = 0; i < invc; i++)
      { res = res + "(" + invariants.get(i) + ")"; 
        if (i < invc - 1)
        { res = res + " & \n  "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (initc > 0)
    { res = res + "INITIALISATION "; 
      for (int i = 0; i < initc; i++)
      { res = res + initialisation.get(i); 
        if (i < initc - 1)
        { res = res + " || \n  "; }
        else 
        { res = res + "\n"; }
      }
    }
    if (opc > 0)
    { res = res + "OPERATIONS\n  "; 
      for (int i = 0; i < opc; i++)
      { res = res + operations.get(i); 
        if (i < opc - 1)
        { res = res + "; \n\n  "; }
        else 
        { res = res + "\n"; }
      }
    }
    return res + "END";
  }

  public void display(PrintWriter out)
  { out.println(toString()); }

  public void union(BComponent b,Entity sup,Entity sub)
  { seesList = VectorUtil.union(seesList,b.getSees());
    usesList = VectorUtil.union(usesList,b.usesList);
    includesList = VectorUtil.union(includesList,
                                    b.includesList);
    promotes = VectorUtil.union(promotes,b.promotes);
    types = VectorUtil.union(types,b.types);
    constants = VectorUtil.union(constants,b.constants);
    properties = VectorUtil.union(properties,b.properties);  
    variables = VectorUtil.union(variables,b.variables);
    attributes = VectorUtil.union(attributes,b.attributes);
    roles = VectorUtil.union(roles,b.roles);
    Vector rootinvs = new Vector(); 
    Vector subclassinvs = new Vector(); 
    Vector featureinvs = new Vector(); 
    Vector logicalinvs = new Vector();
    Vector otherinvs = new Vector();  
    for (int i = 0; i < invariants.size(); i++) 
    { BExpression be = (BExpression) invariants.get(i); 
      int categ = be.getCategory(); 
      if (categ == BExpression.ROOT) { rootinvs.add(be); }
      else if (categ == BExpression.SUBCLASS) { subclassinvs.add(be); }
      else if (categ == BExpression.FEATURE) { featureinvs.add(be); }
      else if (categ == BExpression.LOGICAL) { logicalinvs.add(be); }
      else { otherinvs.add(be); }
    } 
    for (int i = 0; i < b.invariants.size(); i++) 
    { BExpression be = (BExpression) b.invariants.get(i); 
      int categ = be.getCategory(); 
      if (categ == BExpression.ROOT) { rootinvs.add(be); }
      else if (categ == BExpression.SUBCLASS) { subclassinvs.add(be); }
      else if (categ == BExpression.FEATURE) { featureinvs.add(be); }
      else if (categ == BExpression.LOGICAL) { logicalinvs.add(be); }
      else { otherinvs.add(be); }
    } 
    invariants.clear(); 
    invariants.addAll(rootinvs); invariants.addAll(subclassinvs); 
    invariants.addAll(featureinvs); invariants.addAll(logicalinvs); 
    invariants.addAll(otherinvs); 

    // invariants = VectorUtil.union(invariants,b.invariants);
    initialisation = VectorUtil.union(initialisation,
                                      b.initialisation);
    for (int i = 0; i < b.operations.size(); i++)
    { BOp op = (BOp) b.operations.get(i);
      String sig = op.getSignature();
      BOp supop = getBySignature(sig);
      if (supop == null || sup == null)
      { operations.add(op); }
      else
      { BOp newop = mergeOperations(supop,op,sup,sub);
        operations.remove(supop);
        operations.add(newop);
      }
    }
    Vector unuseduses = new Vector(); 
    unuseduses.add(b.getName()); 
    unuseduses.add(sub + ""); 
    removeUses(unuseduses); // surely? 
    removeSees(unuseduses); 
  }

  private BOp getBySignature(String sig)
  { for (int i = 0; i < operations.size(); i++)
    { BOp op = (BOp) operations.get(i);
      if (sig != null && sig.equals(op.getSignature()))
      { return op; }
    }
    return null;
  }

  private BOp mergeOperations(BOp superop, BOp subop,Entity sup, Entity sub)
  { // two operations with same signature. merge is:
    // PRE superop.pre
    // THEN IF cx : ds THEN subop.code[cx/dx] ELSE superop.code END END
    BOp res;
    String cx = sup.getName().toLowerCase() + "x";
    String dx = sub.getName().toLowerCase() + "x";
    String ds = sub.getName().toLowerCase() + "s";

    BExpression cxbe = new BBasicExpression(cx);
    BExpression dsbe = new BBasicExpression(ds);
    BExpression insubclass = new BBinaryExpression(":",cxbe,dsbe);

    BStatement supercode = superop.getCode();
    BStatement subcode = subop.getCode();
    System.out.println("Substituting in " + subop + " " + dx + " " + cxbe);     
    BStatement sc = subcode.substituteEq(dx,cxbe);
    BStatement bif = new BIfStatement(insubclass,sc,supercode);
    res = new BOp(superop.getName(),superop.getResult(),
                  superop.getParameters(),superop.getPre(),bif);
    res.setSignature(superop.getSignature());
    return res;
  }


  private void addSubclassAxiom(Entity e)
  { // Adds es <: sups AFTER sups definition

    Entity sup = e.getSuperclass();
    if (sup == null) { return; }
    String sups = sup.getName().toLowerCase() + "s";
    String es = e.getName().toLowerCase() + "s";
    usesList.remove(e.getName());  // no need to USE it as available anyway
    BBasicExpression bsups =
      new BBasicExpression(sups);
    BBasicExpression bes =
      new BBasicExpression(es);
    BBinaryExpression inv =
      new BBinaryExpression("<:",bes,bsups);
    inv.setCategory(BExpression.SUBCLASS); 
    for (int i = 0; i < invariants.size(); i++) 
    { BExpression inve = (BExpression) invariants.get(i); 
      if (inve instanceof BBinaryExpression) 
      { BBinaryExpression binv = (BBinaryExpression) inve; 
        if (sups.equals(binv.getLeft() + ""))
        { invariants.add(i+1,inv);
          return; 
        } 
      } 
    } 
    invariants.add(inv); 
  }
}

