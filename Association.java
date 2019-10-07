import java.util.Vector; 
import java.io.*; 
import java.util.List; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Class Diagram  */ 

/* Major change: May 2016 - addr(obj,val) for 0..1 association end now enforces val : obj.r */  

public class Association extends ModelElement
{ private Entity entity1;
  private Entity entity2;
  private Entity linkedClass = null; // for association classes only
  private int card1 = MANY; // MANY, ONE, ZEROONE or >= 2
  private int card2 = MANY;
  private String role1; // one of these is usually null
  private String role2;
  private boolean ordered = false; // role2 is a sequence
  private boolean sortedAsc = false; // a sorted set
  private boolean sortedDesc = false; // not implemented yet

  // also ADDONLY, FROZEN options
  private boolean addOnly = false; // also represented as stereotypes 
  private boolean frozen = false; 
  private boolean aggregation = false; 
  private Vector constraints = new Vector(); // those that involve this association
  private Attribute qualifier = null; // non-null for qualified associations
  private boolean instanceScope = true; 
 
  public Association(Entity e1, Entity e2, int c1,
                     int c2, String r1, String r2)
  { super(e1.getName() + "_" + e2.getName());
    entity1 = e1;
    entity2 = e2;
    card1 = c1;
    card2 = c2;
    role1 = r1;
    role2 = r2;
  }

  public Association(Entity e1, Attribute att) 
  { super(e1.getName() + "_" + att.getName()); 
    entity1 = e1; 
    entity2 = att.getType().getEntity(); 
    card1 = MANY; 
    card2 = ONE; 
    role1 = ""; 
    role2 = att.getName(); 
  } 

  public Object clone()
  { Association res = new Association(entity1, entity2, card1, card2, role1, role2);
    res.setAddOnly(addOnly);
    res.setFrozen(frozen);
    res.setOrdered(ordered); 
    res.sortedAsc = sortedAsc; 
    res.aggregation = aggregation; 
    res.setQualifier(qualifier); 
    res.setInstanceScope(instanceScope);
    return res; 
  }

  public Association(Entity e1, Entity e2, String c1,
                     String c2, String r1, String r2)
  { super(e1.getName() + "_" + e2.getName());
    entity1 = e1;
    entity2 = e2;

    card2 = ONE; // default in Ecore

    // c1 is lower bound on role2, c2 is upper bound

    if ("-1".equals(c2))
    { card2 = MANY; } 
    else if ("1".equals(c2)) 
    { if ("1".equals(c1))
      { card2 = ONE; } 
      else 
      { card2 = ZEROONE; } 
    } 
    else 
    { card2 = MANY; } // a..b with b > 1
    card1 = MANY;  // default - depends on the opposite

    role1 = r1;
    role2 = r2;
  }

  public Association generateSubAssociation(Entity e1, Entity e2)
  { // assume e1 is ancestor of entity1, e2 of entity2
    int c1 = card1; 
    int c2 = card2; 
    if (card1 == ONE)
    { c1 = ZEROONE; }
    if (card2 == ONE) 
    { c2 = ZEROONE; } 

    Association res = new Association(e1,e2,c1,c2,role1,role2);
    return res; 
  } // copy ordered, sorted

  public Association generateInverseAssociation()
  { Association inv = new Association(entity2,entity1,card2,card1,role2,role1); 
    if (isSource()) 
    { inv.setSource(true); }
    else if (isTarget())
    { inv.setTarget(true); } 
    return inv; 
  } // and other stereotypes?  

  public boolean isInverseAssociation(Association ast) 
  { if (entity1 == ast.entity2 && entity2 == ast.entity1 && 
        (role1 + "").equals(ast.role2) && role2.equals(ast.role1 + ""))
    { return true; } 
    return false; 
  } 

  public void updateAssociation(int c1, int c2, String r1, String r2, 
                                Vector stereos)
  { card1 = c1; 
    card2 = c2;     
    role1 = r1;
    role2 = r2;
    setStereotypes(stereos); 
    if (stereos.contains("ordered"))
    { setOrdered(true); } 
    else 
    { setOrdered(false); } 
  }

  public void setName(String nme)
  { name = nme; } 

  public void setEntity1(Entity e1)
  { entity1 = e1; } 

  public void setEntity2(Entity e2)
  { entity2 = e2; } 

  public void setCard1(String lower, String upper)
  { if ("-1".equals(upper))
    { card1 = MANY; } 
    else if ("1".equals(upper)) 
    { if ("1".equals(lower))
      { card1 = ONE; } 
      else 
      { card1 = ZEROONE; } 
    } 
  } 

  
  public boolean getAggregation()
  { return aggregation; } 

  public void setAggregation(boolean a)
  { aggregation = a; 
    if (a) 
    { if (hasStereotype("aggregation")) { } 
      else 
      { addStereotype("aggregation"); } 
    }
    else 
    { removeStereotype("aggregation"); } 
  }  

  public void setSource(boolean a)
  { if (a) 
    { if (hasStereotype("source")) { } 
      else 
      { addStereotype("source"); } 
    }
    else 
    { removeStereotype("source"); } 
  }  

  public void setTarget(boolean a)
  { if (a) 
    { if (hasStereotype("target")) { } 
      else 
      { addStereotype("target"); } 
    }
    else 
    { removeStereotype("target"); } 
  }  

  public void setQualifier(Attribute q)
  { qualifier = q;
    if (q != null) 
    { addStereotype("qualifier"); 
      addStereotype(q.getName() + ""); 
    } 
  } 

  public void unsetQualifier()
  { if (qualifier != null) 
    { String qnme = qualifier.getName(); 
      qualifier = null; 
      removeStereotype("qualifier");
      removeStereotype(qnme); 
    }  
  } 

  public void setLinkedClass(Entity e) 
  { linkedClass = e; 
    addStereotype("associationClass"); 
    addStereotype(e.getName() + ""); 
  } 

  public void unsetLinkedClass()
  { if (linkedClass != null) 
    { String lc = linkedClass.getName(); 
      linkedClass = null; 
      removeStereotype("associationClass"); 
      removeStereotype(lc); 
    } 
  } 

  public void setSorted(boolean srt)
  { sortedAsc = srt; } 

  public void makeE1Optional()
  { if (card1 == ZEROONE) { } 
    else if (card1 == ONE)
    { card1 = ZEROONE; } 
    else 
    { card1 = MANY; } 
  } 

  public void makeE2Optional()
  { if (card2 == ZEROONE) { } 
    else if (card2 == ONE)
    { card2 = ZEROONE; } 
    else 
    { card2 = MANY; } 
  } 

  public Entity getEntity1()
  { return entity1; } 

  public Entity getEntity2()
  { return entity2; }

  public String getRole1()
  { return role1; }

  public String getRole2()
  { return role2; }

  public int getCard1()
  { return card1; }

  public int getCard2()
  { return card2; }

  public boolean isAggregation()
  { return aggregation; } 

  public boolean isClassScope()
  { return !instanceScope; }

  public boolean isSingleValued()
  { return card2 == ONE; } 

  public void addConstraint(Constraint con)
  { constraints.add(con); } 

  public void setInstanceScope(boolean iscope)
  { instanceScope = iscope; } 

  public boolean getInstanceScope()
  { return instanceScope; } 

  public boolean isOrdered()
  { return ordered; } 

  public boolean isSorted()
  { return sortedAsc; } 

  public boolean isInjective()
  { return card1 == ZEROONE && card2 == ONE; } 

  public boolean isManyMany()
  { return card1 == MANY && card2 == MANY; } 

  public boolean isManyOne()  // either way round
  { return card1 == MANY && card2 == ONE ||
           card1 == ONE && card2 == MANY; 
  }

  public boolean isPersistent()
  { return hasStereotype("persistent"); } 

  public boolean isExplicit()
  { return hasStereotype("explicit"); }   // default

  public boolean isImplicit()
  { return hasStereotype("implicit"); }  

  public boolean isSource()
  { return hasStereotype("source"); }

  public boolean isTarget()
  { return hasStereotype("target"); }  

  public boolean isDerived()
  { return hasStereotype("derived"); }

  public boolean isQualified()
  { return qualifier != null; } 

  public Attribute getQualifier()
  { return qualifier; } 

  public Vector getConstraints()
  { return constraints; } 

  public void setRole1(String r1) 
  { role1 = r1; } 

  public void setRole2(String r2) 
  { role2 = r2; } 

  public void setCard1(int c1)
  { card1 = c1; } 

  public void setCard2(int c2)
  { card2 = c2; } 

  public Type getType2()
  { Type e2type = new Type(entity2);
    if (card2 == ONE)
    { return e2type; } 
    Type res;
    if (ordered)
    { res = new Type("Sequence", null); }
    else  
    { res = new Type("Set",null); } 
    res.setElementType(e2type); 
    return res;  
  } 

  public void setOrdered(boolean ord) 
  { ordered = ord; 
    if (ordered == true) 
    { addStereotype("ordered"); }
    else 
    { removeStereotype("ordered"); }
  } 

  public void setSorted(String direction) 
  { if (direction.equals("<"))
    { sortedAsc = true; 
      addStereotype("sorted<");
    }
    else if (direction.equals(">")) 
    { sortedDesc = true; 
      addStereotype("sorted>"); 
    } 
  } 

  public void setAddOnly(boolean ao) 
  { addOnly = ao;
    if (addOnly == true)
    { addStereotype("addOnly"); } 
    else 
    { removeStereotype("addOnly"); }
  }  

  public void setFrozen(boolean froz) 
  { frozen = froz;
    if (frozen == true)
    { addStereotype("readOnly"); }
    else 
    { removeStereotype("readOnly"); } 
  }  

  public boolean isFrozen()
  { return frozen; }

  public boolean isAddOnly()
  { return addOnly; }

  public String subsets()
  { String res = ""; 
    // System.out.println(stereotypes); 

    if (stereotypes.size() > 0)
    { String stereo = (String) stereotypes.get(0); 
      if (stereo.length() > 6 && "subsets".equals(stereo.substring(0,7)))
      { return stereo.substring(7,stereo.length()); } 
    } 
    return res; 
  } 

  public Association getOpposite()
  { return entity2.getDefinedRole(role1); } 

  // for write frames: 
  public static String getOppositeEnd(String rle, Entity e, Vector assocs)
  { for (int i = 0; i < assocs.size(); i++)
    { Association a = (Association) assocs.get(i);
      if (rle.equals(a.getRole1()) && e == a.getEntity2())
      { String rle2 = a.getRole2();
        if (rle2 != null && rle2.length() > 0)
        { return a.getEntity1() + "::" + rle2; }
      }
      if (rle.equals(a.getRole2()) && e == a.getEntity1())
      { String rle1 = a.getRole1();
        if (rle1 != null && rle1.length() > 0)
        { return a.getEntity2() + "::" + rle1; }
      }
    }
    return null;
  }

  public static Vector getManyOneAssociations(Vector asts)
  { Vector res = new Vector(); 
    for (int i = 0; i < asts.size(); i++) 
    { Association ast = (Association) asts.get(i); 
      // if (ast.isManyOne())
      if (ast.getCard1() == ONE && ast.getCard2() == MANY)
      { res.add(ast); } 
      else if (ast.getCard2() == ONE && ast.getCard1() == MANY)
      { res.add(ast); }
    }
    return res; 
  } 
   
  public static Vector getManyManyAssociations(Vector asts)
  { Vector res = new Vector(); 
    for (int i = 0; i < asts.size(); i++) 
    { Association ast = (Association) asts.get(i); 
      // if (ast.isManyMany())
      if (ast.getCard1() == MANY && ast.getCard2() == MANY)
      { res.add(ast); } 
      // else if (ast.getCard2() == ONE && ast.getCard1() == MANY)
      // { res.add(ast); }  // also consider ZEROONE? 
    }
    return res; 
  }    

  public boolean hasEnd(Entity e)
  { return ((e == entity1) || (e == entity2)); } 

  public Vector hasNamedEnds(Entity e)
  { Vector res = new Vector(); 
    if (e == entity1 && role1 != null && role1.length() > 0)
    { res.add(role1); } 
    if (e == entity2)
    { if (res.contains(role2)) { } 
      else 
      { res.add(role2); }
    } 
    return res; 
  }  

//  public boolean isLinkedTo(Association ast)
//  { return (hasEnd(ast.getEntity1()) || hasEnd(ast.getEntity2())); } 
  
  public boolean isLinkedTo(Association ast)
  { return entity1.equals(ast.getEntity1()) ||
           entity1.equals(ast.getEntity2()) ||
           entity2.equals(ast.getEntity1()) ||
           entity2.equals(ast.getEntity2());
  }

  public boolean linkedToAny(List rels)
  { for (int i = 0; i < rels.size(); i++)
    { Association ast = (Association) rels.get(i);
      if (isLinkedTo(ast))
      { return true; }
    }
    return false;
  }

  public static Vector getEndpoints(Vector rels)
  { Vector res = new Vector(); 
    for (int i = 0; i < rels.size(); i++)
    { Association ast = (Association) rels.get(i); 
      res.add(ast.getEntity1()); 
      res.add(ast.getEntity2()); 
    }
    return res; 
  }
    
  public static List reorderFromSource(String src,
                                       List assocs)
  { // places those r : assocs that have entity1 or 2
    // with name src first in the list, then those
    // r linked to these, etc. Puts groups of linked
    // rels together even if not connected to src
    List oldrels = (List) ((Vector) assocs).clone();
    List res = new Vector();
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i);
      Entity e1 = ast.getEntity1();
      Entity e2 = ast.getEntity2();
      if (e1.getName().equals(src) ||
          e2.getName().equals(src)) 
      { res.add(ast); }
    }
    // res is now all rels linked direct to src
    oldrels.removeAll(res); 
    List newrels; 
    
    do
    { newrels = new Vector();
      for (int j = 0; j < oldrels.size(); j++)
      { Association ast = (Association) oldrels.get(j);
        if (ast.linkedToAny(res))
        { newrels.add(ast); }
      }
      oldrels.removeAll(newrels);
      res.addAll(newrels);
    } while (newrels.size() > 0);
    // res is all rels connected to src

    while (oldrels.size() > 0)
    { Object r = oldrels.get(0); 
      oldrels.remove(0); 
      Vector v = new Vector(); 
      v.add(r); 
      res.addAll(findConnected(v,oldrels)); 
    } 
    return res;
  }

  private static List findConnected(List rels, List oldrels)
  { // finds group of all r:oldrels connected to any x:rels
    List res = (List) ((Vector) rels).clone(); 
    List newrels; 
    
    do
    { newrels = new Vector();
      for (int j = 0; j < oldrels.size(); j++)
      { Association ast = (Association) oldrels.get(j);
        if (ast.linkedToAny(res))
        { newrels.add(ast); }
      }
      oldrels.removeAll(newrels);
      res.addAll(newrels);
    } while (newrels.size() > 0);
    // res is all rels connected to src
    return res;
  }

  public static void main(String[] args)
  { Entity e1 = new Entity("e1");
    Entity e2 = new Entity("e2");
    Entity e3 = new Entity("e3");
    Entity e4 = new Entity("e4");
    Entity e5 = new Entity("e5");
    Entity e6 = new Entity("e6");
    Entity e7 = new Entity("e7");
   
    String r = "";
    Association a1 = new Association(e1,e2,0,0,r,r);
    Association a2 = new Association(e3,e4,0,0,r,r);
    Association a3 = new Association(e4,e5,0,0,r,r);
    Association a4 = new Association(e4,e6,0,0,r,r);
    Association a5 = new Association(e1,e1,0,0,r,r);
    Association a6 = new Association(e7,e7,0,0,r,r); 
    Vector rels = new Vector();
    rels.add(a1);
    rels.add(a2);
    rels.add(a3);
    rels.add(a4);
    rels.add(a5); 
    rels.add(a6); 
    System.out.println(reorderFromSource("e3",rels));

    String stereo = "subsetsrr"; 
    if (stereo.length() > 6 && "subsets".equals(stereo.substring(0,7)))
    { System.out.println(stereo.substring(7,stereo.length())); } 
  }

  public void generateJava(PrintWriter out)
  { String qual = ""; 
    String initialiser = " = new Vector()"; 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }
    if (role2 != null)  // attribute of entity1
    { if (entity1.isAbstract())
      { out.print("  protected " + qual); } 
      else 
      { out.print("  private " + qual); } 

      if (qualifier != null)
      { out.println("java.util.Map " + role2 + " = new java.util.HashMap();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " + role2 + ";"); }
      else
      { out.println("List " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateJava6(PrintWriter out)
  { String qual = ""; 

    String initialiser = ""; 
    if (ordered)
    { initialiser = " = new ArrayList()"; } 
    else 
    { initialiser = " = new HashSet()"; } 
 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }

    if (role2 != null)  // attribute of entity1
    { if (entity1.isAbstract())
      { out.print("  protected " + qual); } 
      else 
      { out.print("  private " + qual); } 

      if (qualifier != null)
      { out.println("java.util.Map " + role2 + " = new java.util.HashMap();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " + role2 + ";"); }
      else if (ordered)
      { out.println("ArrayList " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
      else 
      { out.println("HashSet " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateJava7(PrintWriter out)
  { String qual = ""; 
    String e2name = entity2.getName(); 

    String initialiser = ""; 
    String reltype = e2name; 
    if (ordered)
    { initialiser = " = new ArrayList<" + e2name + ">()"; 
      reltype = "ArrayList<" + e2name + ">"; 
    } 
    else if (sortedAsc)
    { initialiser = " = new TreeSet<" + e2name + ">()";  
      reltype = "TreeSet<" + e2name + ">"; 
    } 
    else 
    { initialiser = " = new HashSet<" + e2name + ">()";
      reltype = "HashSet<" + e2name + ">"; 
    } 
 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }

    if (role2 != null)  // attribute of entity1
    { if (entity1.isAbstract())
      { out.print("  protected " + qual); } 
      else 
      { out.print("  private " + qual); } 

      // Type t2 = getType2(); 
      // String j2type = t2.getJava7(t2.getElementType()); 

      if (qualifier != null)
      { out.println("java.util.Map<String, " + reltype + "> " + role2 + 
                            " = new java.util.HashMap<String, " + reltype + ">();"); 
      } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " + role2 + ";"); }
      else 
      { out.println(reltype + " " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateCSharp(PrintWriter out)
  { String qual = ""; 
    String initialiser = " = new ArrayList()"; 
    if (frozen) 
    { qual = "const ";
      initialiser = ""; 
    }
    if (role2 != null)  // attribute of entity1
    { if (entity1.isAbstract())
      { out.print("  protected " + qual); } 
      else 
      { out.print("  private " + qual); } 

      if (qualifier != null)
      { out.println("Hashtable " + role2 + " = new Hashtable();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " + role2 + ";"); }
      else
      { out.println("ArrayList " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateCPP(PrintWriter out)
  { String qual = ""; 
    // String initialiser = " = new Vector()"; 
    if (frozen) 
    { qual = "const ";
      // initialiser = ""; 
    }

    String e2name = entity2.getName() + "*"; 
    out.print("  " + qual); 

    if (role2 != null)  // attribute of entity1
    { if (qualifier != null)
      { if (card2 == ONE) 
        { out.println("map<string," + e2name + ">* " + role2 + ";"); }
        else if (ordered) 
        { out.println("map<string, vector<" + e2name + ">*>* " + role2 + ";"); }
        else 
        { out.println("map<string, set<" + e2name + ">*>* " + role2 + ";"); }
      } 
      else if (card2 == ONE)
      { out.println(e2name + " " + role2 + ";"); }
      else if (ordered) 
      { out.println("vector<" + e2name + ">* " + role2 + ";"); }
      else 
      { out.println("set<" + e2name + ">* " + role2 + ";"); }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateInterfaceJava(PrintWriter out)
  { out.print(" // "); 
    String qual = ""; 
    String initialiser = " = new ArrayList()"; 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }
    if (role2 != null)  // attribute of entity1
    { out.print("  protected " + qual);  
      if (qualifier != null)
      { out.println("java.util.Map " + role2 + " = new java.util.HashMap();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " +
                    role2 + ";");
      }
      else
      { out.println("List " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateInterfaceJava6(PrintWriter out)
  { out.print(" // "); 
    String qual = ""; 
    String initialiser = ""; 

    if (ordered) 
    { initialiser = " = new ArrayList()"; } 
    else 
    { initialiser = " = new HashSet()"; } 
 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }

    if (role2 != null)  // attribute of entity1
    { out.print("  protected " + qual);  
      if (qualifier != null)
      { out.println("java.util.Map " + role2 + " = new java.util.HashMap();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " +
                    role2 + ";");
      }
      else if (ordered)
      { out.println("ArrayList " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
      else 
      { out.println("HashSet " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?
 
  public void generateInterfaceJava7(PrintWriter out)
  { out.print(" // "); 
String qual = ""; 
    String e2name = entity2.getName(); 

    String initialiser = ""; 
    String reltype = e2name; 
    if (ordered)
    { initialiser = " = new ArrayList<" + e2name + ">()"; 
      reltype = "ArrayList<" + e2name + ">"; 
    } 
    else if (sortedAsc)
    { initialiser = " = new TreeSet<" + e2name + ">()";  
      reltype = "TreeSet<" + e2name + ">"; 
    } 
    else 
    { initialiser = " = new HashSet<" + e2name + ">()";
      reltype = "HashSet<" + e2name + ">"; 
    } 
 
    if (frozen) 
    { qual = "final ";
      initialiser = ""; 
    }

    if (role2 != null)  // attribute of entity1
    { out.print("  protected " + qual);  
      
      // Type t2 = getType2(); 
      // String j2type = t2.getJava7(t2.getElementType()); 

      if (qualifier != null)
      { out.println("java.util.Map<String, " + reltype + "> " + role2 + 
                            " = new java.util.HashMap<String, " + reltype + ">();"); 
      } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " + role2 + ";"); }
      else 
      { out.println(reltype + " " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?

  public void generateInterfaceCSharp(PrintWriter out)
  { out.print(" // "); 
    String qual = ""; 
    String initialiser = " = new ArrayList()"; 
    if (frozen) 
    { qual = "const ";
      initialiser = ""; 
    }
    if (role2 != null)  // attribute of entity1
    { out.print("  protected " + qual);  
      if (qualifier != null)
      { out.println("Hashtable " + role2 + " = new Hashtable();"); } 
      else if (card2 == ONE)
      { out.println(entity2.getName() + " " +
                    role2 + ";");
      }
      else
      { out.println("ArrayList " +
                    role2 + initialiser + "; // of " +
                    entity2.getName());
      }
    }
  }  // not valid for a..b a > 0. Should be array then?
 
  public String constructorParameter()
  { if (role2 != null && qualifier == null)
    { if (card2 == ONE)
      { return entity2.getName() + " " + role2; }  // and for a..b, a>0
      else if (frozen)
      { return "List " + role2; } 
    }
    return null; 
  }

  public String constructorParameterJava6()
  { if (role2 != null && qualifier == null)
    { if (card2 == ONE)
      { return entity2.getName() + " " + role2; }  // and for a..b, a>0
      else if (frozen)
      { if (ordered) { return "ArrayList " + role2; }
        else { return "HashSet " + role2; } 
      }  
    }
    return null; 
  }

  public String constructorParameterJava7()
  { if (role2 != null && qualifier == null)
    { String e2name = entity2.getName(); 
      if (card2 == ONE)
      { return e2name + " " + role2; }  // and for a..b, a>0
      else if (frozen)
      { if (ordered) { return "ArrayList<" + e2name + "> " + role2; }
        else if (sortedAsc) 
        { return "TreeSet<" + e2name + "> " + role2; }
        else  
        { return "HashSet<" + e2name + "> " + role2; } 
      }  
    }
    return null; 
  }

  public String constructorParameterCSharp()
  { if (role2 != null && qualifier == null)
    { if (card2 == ONE)
      { return entity2.getName() + " " + role2; }  // and for a..b, a>0
      else if (frozen)
      { return "ArrayList " + role2; } 
    }
    return null; 
  }

  public String constructorParameterCPP()
  { String e2name = entity2.getName() + "*"; 

    if (role2 != null && qualifier == null)
    { if (card2 == ONE)
      { return e2name + " " + role2 + "_x"; }  // and for a..b, a>0
      else if (frozen)
      { if (ordered) { return "vector<" + e2name + ">* " + role2 + "_x"; } 
        else 
        { return "set<" + e2name + ">* " + role2 + "_x"; } 
      } 
    }
    return null; 
  }

  public String destructorCodeCPP()
  { if (card2 != ONE) 
    { return "delete " + role2 + ";"; } 
    return null; 
  } 

  public String initialiser()
  { if (role2 != null && qualifier == null)
    { if (card2 == ONE || frozen)
      { return "this." + role2 + " = " + role2 + ";"; } // for a..b a>0
    }
    return null;
  }

  public String initialiserCPP()
  { if (role2 != null && qualifier == null)
    { if (card2 == ONE || frozen)
      { return "" + role2 + " = " + role2 + "_x;"; } // for a..b a>0
    }
    String e2name = entity2.getName() + "*"; 
    String role2type = e2name; 
    if (ordered)
    { role2type = "vector<" + role2type + ">"; } 
    else 
    { role2type = "set<" + role2type + ">"; } 
    
    if (qualifier != null) // for card2 != ONE
    { role2type = "map<string, " + role2type + "*>"; } 
    
    if (card2 == ONE)
    { if (qualifier == null) 
      { return "" + role2 + " = new " + entity2.getName() + "();"; } 
      else 
      { return "" + role2 + " = new map<string, " + e2name + ">();"; } 
    } 
    else 
    { return "" + role2 + " = new " + role2type + "();"; }  
  }



  // and static versions -- ops for the class itself
  public String setOperation(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (qualifier != null) 
    { return qualifiedSetOperation(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "_xx"; 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    Type e2type = new Type(entity2);
    if (card2 == ONE)
    { valt = e2type; } 
    else if (ordered)
    { valt = new Type("Sequence",null); 
      valt.setElementType(e2type); 
    } 
    else 
    { valt = new Type("Set",null); 
      valt.setElementType(e2type); 
    } 

    String sync = ""; 
    if (ent.isSequential()) { sync = "synchronized "; } 

    Attribute par = new Attribute(val,valt,INTERNAL); 
    par.setElementType(new Type(entity2)); 

    Vector pars = new Vector(); 
    pars.add(par); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,pars,false,null);

    String code = ""; 
    String assign = ""; 
    String addassign = ""; 
    String remassign = ""; 

    if (ent != entity1 && !entity1.isInterface())
    { assign = "super.set" + nme + "(" + val + ");";
      addassign = "super.add" + nme + "(" + val + ");";
      remassign = "super.remove" + nme + "(" + val + ");";
    } 
    else 
    { assign = nme + " = " + val + ";"; 
      if (ordered)
      { addassign = nme + ".add(" + val + ");"; }
      else
      { addassign = "if (" + nme + ".contains(" + val + ")) {} else { " + 
                             nme + ".add(" + val + "); }"; 
      }
      remassign = "Vector _removed" + nme + val + " = new Vector();\n" + 
                  "  _removed" + nme + val + ".add(" + val + ");\n" + 
                  "  " + nme + ".removeAll(_removed" + nme + val + ");";
    } 
    // But if not ordered, should be:  if (nme.contains(val)) { } else { nme.add(val); } 

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      // System.out.println("Match set: " + cnew + " Pars are: " + pars); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 

        boolean typed = cnew.typeCheck(types,entities,cntxs,pars); 
        if (typed)
        { String update = cnew.updateOperation(ent,nme,true);  
          code = code + update + "\n";
        } 
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,valbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }

    // if (isInjective())    // for controller
    // { String role2map = entity1.getName().toLowerCase() + role2 +
    //                     "index";
    //   String injtest = "    if (" + role2map + ".get(" +
    //                    role2 + "xx) != null) { return; }\n";
    if (card2 == ONE)
    { return "public " + sync + "void set" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + assign + "\n" + code + "  }";
    }
    else  // but not a set/sequence type in pars? 
    { String addcode = ""; 
      String removecode = ""; 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,pars,false,null);
      BehaviouralFeature removeevent =
        new BehaviouralFeature("remove" + nme,pars,false,null);

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("add",nme,ent,val,addevent);
        // must type check new constraint. 
        System.out.println("Match add: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperation(ent,nme,true);  
            addcode = addcode + update + "\n";
          } 
        }
      }


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,val,removeevent);
        // must type check new constraint. 
        System.out.println("Match remove: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperation(ent,nme,true);  
            removecode = removecode + update + "\n";
          } 
        }
      }

      String setindop = ""; 

      if (ordered)
      { setindop = " public " + sync + "void set" + role2 + "(int ind_x, " + 
                                 entity2.getName() + " " + val + 
                   ") { if (ind_x >= 0 && ind_x < " + role2 + ".size()) { " + role2 + ".set(ind_x, " + val + "); } }\n\n"; 
      }

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(List " + val +
             ") { if (" + val + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { if (" + role2 + ".size() > 0) { return; } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        } 
        else 
        { return "public " + sync + "void set" + role2 + "(List " + val +
             ") { if (" + val + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { if (" + role2 + ".size() > 0) { " + role2 + ".clear(); } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             entity2.getName() + " " + val +
             ") { " + remassign + "\n  " + removecode + "  }";
        }
      }
      else // card2 == MANY
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(List " +
             val + ") { " + assign + "\n" + 
             code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        }
        else 
        { return "public " + sync + "void set" + role2 + "(List " +
             val + ") { " + assign + "\n  " + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             entity2.getName() + " " + val +
             ") { " + remassign +
             "\n  " + removecode + "  }";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  public String setOperationJava6(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (qualifier != null) 
    { return qualifiedSetOperationJava6(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "_xx"; 
    String role2x = role2 + "_xx"; 
    String e2name = entity2.getName(); 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    String inpartype = ""; 

    Type e2type = new Type(entity2);

    if (card2 == ONE)
    { valt = e2type; } 
    else if (ordered)
    { valt = new Type("Sequence",null); 
      valt.setElementType(e2type); 
      inpartype = "ArrayList"; 
    } 
    else 
    { valt = new Type("Set",null); 
      valt.setElementType(e2type); 
      inpartype = "HashSet"; 
    } 

    String sync = ""; 
    if (ent.isSequential()) { sync = "synchronized "; } 

    Attribute par = new Attribute(val,valt,INTERNAL); 
    par.setElementType(new Type(entity2)); 

    Vector pars = new Vector(); 
    pars.add(par); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,pars,false,null);

    String code = ""; 
    String assign = ""; 
    String addassign = ""; 
    String remassign = ""; 

    if (ent != entity1 && !entity1.isInterface())
    { assign = "super.set" + nme + "(" + val + ");";
      addassign = "super.add" + nme + "(" + val + ");";
      remassign = "super.remove" + nme + "(" + val + ");";
    } 
    else 
    { assign = nme + " = " + val + ";"; 
      addassign = nme + ".add(" + val + ");"; 
      remassign = "ArrayList _removed" + nme + val + " = new ArrayList();\n" + 
                  "  _removed" + nme + val + ".add(" + val + ");\n" + 
                  "  " + nme + ".removeAll(_removed" + nme + val + ");";
      // remassign = nme + ".remove(" + val + ");";
    } // Should remove all

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set: " + cnew + " Pars are: " + pars); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 

        boolean typed = cnew.typeCheck(types,entities,cntxs,pars); 
        if (typed)
        { String update = cnew.updateOperationJava6(ent,nme,true);  
          code = code + update + "\n";
        } 
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,valbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }

    // if (isInjective())    // for controller
    // { String role2map = entity1.getName().toLowerCase() + role2 +
    //                     "index";
    //   String injtest = "    if (" + role2map + ".get(" +
    //                    role2 + "xx) != null) { return; }\n";
    if (card2 == ONE)
    { return "public " + sync + "void set" + role2 + "(" +
             e2name + " " + role2x + ") { " + assign + "\n" + code + "  }";
    }
    else  // but not a set/sequence type in pars? 
    { String addcode = ""; 
      String removecode = ""; 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,pars,false,null);
      BehaviouralFeature removeevent =
        new BehaviouralFeature("remove" + nme,pars,false,null);

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("add",nme,ent,val,addevent);
        // must type check new constraint. 
        // System.out.println("Match add: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationJava6(ent,nme,true);  
            addcode = addcode + update + "\n";
          } 
        }
      }


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,val,removeevent);
        // must type check new constraint. 
        System.out.println("Match remove: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationJava6(ent,nme,true);  
            removecode = removecode + update + "\n";
          } 
        }
      }

      String setindop = ""; 

      if (ordered)
      { setindop = " public " + sync + "void set" + role2 + "(int ind_x, " + 
                                 e2name + " " + role2x + 
                   ") { if (ind_x >= 0 && ind_x < " + role2 + ".size()) { " + role2 + ".set(ind_x, " + role2x + "); } }\n\n"; 
      }

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x +
             ") { if (" + role2x + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { if (" + role2 + ".size() > 0) { return; } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        } 
        else 
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x +
             ") { if (" + role2x + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { if (" + role2 + ".size() > 0) { " + role2 + ".clear(); } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             e2name + " " + role2x +
             ") { " + remassign + "\n  " + removecode + "  }";
        }
      }
      else
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x + ") { " + assign + "\n" + 
             code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        }
        else 
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " + 
             role2x + ") { " + assign + "\n  " + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             e2name + " " + role2x +
             ") { " + remassign +
             "\n  " + removecode + "  }";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  public String setOperationJava7(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (qualifier != null) 
    { return qualifiedSetOperationJava7(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "_xx"; 
    String role2x = role2 + "_xx"; 
    String e2name = entity2.getName(); 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    String inpartype = ""; 

    Type e2type = new Type(entity2);

    if (card2 == ONE)
    { valt = e2type; } 
    else if (ordered)
    { valt = new Type("Sequence",null); 
      valt.setElementType(e2type); 
      inpartype = "ArrayList<" + e2name + ">"; 
    } 
    else 
    { valt = new Type("Set",null); 
      valt.setElementType(e2type); 
      if (sortedAsc) 
      { inpartype = "TreeSet<" + e2name + ">"; 
        valt.setSorted(true); 
      } 
      else 
      { inpartype = "HashSet<" + e2name + ">"; } 
    } 

    String sync = ""; 
    if (ent.isSequential()) { sync = "synchronized "; } 

    Attribute par = new Attribute(val,valt,INTERNAL); 
    par.setElementType(new Type(entity2)); 

    Vector pars = new Vector(); 
    pars.add(par); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,pars,false,null);

    String code = ""; 
    String assign = ""; 
    String addassign = ""; 
    String remassign = ""; 

    if (ent != entity1 && !entity1.isInterface())
    { assign = "super.set" + nme + "(" + val + ");";
      addassign = "super.add" + nme + "(" + val + ");";
      remassign = "super.remove" + nme + "(" + val + ");";
    } 
    else 
    { assign = nme + " = " + val + ";"; 
      addassign = nme + ".add(" + val + ");"; 
      // remassign = nme + ".remove(" + val + ");";
      remassign = "ArrayList<" + e2name + "> _removed" + nme + val + " = new ArrayList<" + e2name + ">();\n" + 
                  "  _removed" + nme + val + ".add(" + val + ");\n" + 
                  "  " + nme + ".removeAll(_removed" + nme + val + ");";
    } // Should remove all

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set: " + cnew + " Pars are: " + pars); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 

        boolean typed = cnew.typeCheck(types,entities,cntxs,pars); 
        if (typed)
        { String update = cnew.updateOperationJava7(ent,nme,true);  
          code = code + update + "\n";
        } 
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,valbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }

    // if (isInjective())    // for controller
    // { String role2map = entity1.getName().toLowerCase() + role2 +
    //                     "index";
    //   String injtest = "    if (" + role2map + ".get(" +
    //                    role2 + "xx) != null) { return; }\n";
    if (card2 == ONE)
    { return "public " + sync + "void set" + role2 + "(" +
             e2name + " " + role2x + ") { " + assign + "\n" + code + "  }";
    }
    else  // but not a set/sequence type in pars? 
    { String addcode = ""; 
      String removecode = ""; 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,pars,false,null);
      BehaviouralFeature removeevent =
        new BehaviouralFeature("remove" + nme,pars,false,null);

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("add",nme,ent,val,addevent);
        // must type check new constraint. 
        // System.out.println("Match add: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationJava7(ent,nme,true);  
            addcode = addcode + update + "\n";
          } 
        }
      }


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,val,removeevent);
        // must type check new constraint. 
        System.out.println("Match remove: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationJava7(ent,nme,true);  
            removecode = removecode + update + "\n";
          } 
        }
      }

      String setindop = ""; 

      if (ordered)
      { setindop = " public " + sync + "void set" + role2 + "(int ind_x, " + 
                                 e2name + " " + role2x + 
          ") { if (ind_x >= 0 && ind_x < " + role2 + ".size()) { " + role2 + ".set(ind_x, " + role2x + "); } }\n\n"; 
      } // check the index is correct

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x +
             ") { if (" + role2x + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { if (" + role2 + ".size() > 0) { return; } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        } // overwrite the existing element?
        else 
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x +
             ") { if (" + role2x + ".size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { if (" + role2 + ".size() > 0) { " + role2 + ".clear(); } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             e2name + " " + role2x +
             ") { " + remassign + "\n  " + removecode + "  }";
        } // for add, overwrite 
      }
      else
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " +
             role2x + ") { " + assign + "\n" + 
             code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        }
        else 
        { return "public " + sync + "void set" + role2 + "(" + inpartype + " " + 
             role2x + ") { " + assign + "\n  " + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             e2name + " " + role2x + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             e2name + " " + role2x +
             ") { " + remassign +
             "\n  " + removecode + "  }";
        }
      }
    }
  }  

  // and static versions -- ops for the class itself
  public String setOperationCSharp(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (qualifier != null) 
    { return qualifiedSetOperationCSharp(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "_xx"; 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); } 
    valt.setElementType(new Type(entity2)); 

    String sync = ""; 
    if (ent.isSequential()) { sync = "synchronized "; } 

    Attribute par = new Attribute(val,valt,INTERNAL); 
    par.setElementType(new Type(entity2)); 

    Vector pars = new Vector(); 
    pars.add(par); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,pars,false,null);

    String code = ""; 
    String assign = ""; 
    String addassign = ""; 
    String remassign = ""; 

    if (ent != entity1 && !entity1.isInterface())
    { assign = "base.set" + nme + "(" + val + ");";
      addassign = "base.add" + nme + "(" + val + ");";
      remassign = "base.remove" + nme + "(" + val + ");";
    } 
    else 
    { assign = nme + " = " + val + ";"; 
      addassign = nme + ".Add(" + val + ");"; 
      remassign = nme + " = SystemTypes.subtract(" + nme + ", " + val + ");";
    } // Remove all copies

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      // System.out.println("Match set: " + cnew + " Pars are: " + pars); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 

        boolean typed = cnew.typeCheck(types,entities,cntxs,pars); 
        if (typed)
        { String update = cnew.updateOperationCSharp(ent,nme,true);  
          code = code + update + "\n";
        } 
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,valbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }

    // if (isInjective())    // for controller
    // { String role2map = entity1.getName().toLowerCase() + role2 +
    //                     "index";
    //   String injtest = "    if (" + role2map + ".get(" +
    //                    role2 + "xx) != null) { return; }\n";
    if (card2 == ONE)
    { return "public " + sync + "void set" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + assign + "\n" + code + "  }";
    }
    else  // but not a set/sequence type in pars? 
    { String addcode = ""; 
      String removecode = ""; 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,pars,false,null);
      BehaviouralFeature removeevent =
        new BehaviouralFeature("remove" + nme,pars,false,null);

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("add",nme,ent,val,addevent);
        // must type check new constraint. 
        System.out.println("Match add: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationCSharp(ent,nme,true);  
            addcode = addcode + update + "\n";
          } 
        }
      }


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,val,removeevent);
        // must type check new constraint. 
        System.out.println("Match remove: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationCSharp(ent,nme,true);  
            removecode = removecode + update + "\n";
          } 
        }
      }

      String setindop = ""; 

      if (ordered)
      { setindop = " public " + sync + "void set" + role2 + "(int ind_x," + 
                                 entity2.getName() + " " + val + 
                   ") { if (ind_x >= 0 && ind_x < " + role2 + ".size()) { " + role2 + "[ind_x] = " + val + "; } }\n\n"; 
      }

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(ArrayList " +
             val +
             ") { if (" + val + ".Count > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { if (" + role2 + ".Count > 0) { return; } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        } 
        else 
        { return "public " + sync + "void set" + role2 + "(ArrayList " +
             val +
             ") { if (" + val + ".Count > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { if (" + role2 + ".Count > 0) { " + role2 + ".Clear(); } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             entity2.getName() + " " + val +
             ") { " + remassign + "\n  " + removecode + "  }";
        }
      }
      else
      { if (addOnly)
        { return "public " + sync + "void set" + role2 + "(ArrayList " +
             val + ") { " + assign + "\n" + 
             code + "  }" + "\n \n " + setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        }
        else 
        { return "public " + sync + "void set" + role2 + "(ArrayList " +
             val + ") { " + assign + "\n  " + code + "  }" + "\n \n " +
             setindop + 
             " public " + sync + "void add" + role2 + "(" +
             entity2.getName() + " " + val + 
             ") { " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             " public " + sync + "void remove" + role2 + "(" +
             entity2.getName() + " " + val + ") { " + remassign +
             "\n  " + removecode + "  }";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  // and static versions -- ops for the class itself
  public String setOperationCPP(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (qualifier != null) 
    { return qualifiedSetOperationCPP(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "xx"; 
    BasicExpression valbe = new BasicExpression(val); 
    String e2name = entity2.getName() + "*"; 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); } 
    valt.setElementType(new Type(entity2)); 

    // String sync = ""; 
    // if (ent.isSequential()) { sync = "synchronized "; } 

    Attribute par = new Attribute(val,valt,INTERNAL); 
    par.setElementType(new Type(entity2)); // in C++ it is a pointer

    Vector pars = new Vector(); 
    pars.add(par); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,pars,false,null);

    String code = ""; 
    String assign = ""; 
    String addassign = ""; 
    String remassign = ""; 
    String clearop = "  void setEmpty" + nme + "()\n" + 
                     "  { " + nme + "->clear(); }\n"; 


    if (card2 == ONE)
    { assign = nme + " = " + val + ";"; } 
    else if (ordered)     // NB: erase only removes the *first* copy of val. 
    { assign = nme + "->clear();\n" + 
               "      " + nme + "->insert(" + nme + "->end(), " + 
                                              val + "->begin()," + val + "->end());"; 
      addassign = nme + "->push_back(" + val + ");"; 
      remassign = "  vector<" + e2name + ">::iterator _pos = find(" + nme + "->begin(), " + 
                                          nme + "->end(), " + val + ");\n" + 
                  "  while (_pos != " + nme + "->end())\n" + 
                  "  { " + nme + "->erase(_pos);\n" + 
                  "    _pos = find(" + nme + "->begin(), " + nme + "->end(), " + val + ");\n" +
                  "  }"; 
    } 
    else 
    { assign = nme + "->clear();\n" + 
               "      " + nme + "->insert(" + val + "->begin()," + val + "->end());"; 
      addassign = nme + "->insert(" + val + ");"; 
      remassign = nme + "->erase(" + val + ");";
    } 

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,val,event);
      // must type check new constraint. 
      System.out.println("Match set: " + cnew + " Pars are: " + pars); 
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 

        boolean typed = cnew.typeCheck(types,entities,cntxs,pars); 
        if (typed)
        { String update = cnew.updateOperationCPP(ent,nme,true);  
          code = code + update + "\n";
        } 
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,valbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }

    // if (isInjective())    // for controller
    // { String role2map = entity1.getName().toLowerCase() + role2 +
    //                     "index";
    //   String injtest = "    if (" + role2map + ".get(" +
    //                    role2 + "xx) != null) { return; }\n";
    if (card2 == ONE)
    { return "  " + "void set" + role2 + "(" + e2name + " " + role2 + 
             "xx) { " + assign + "\n" + code + "  }";
    }
    else  // but not a set/sequence type in pars? 
    { String addcode = ""; 
      String removecode = ""; 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,pars,false,null);
      BehaviouralFeature removeevent =
        new BehaviouralFeature("remove" + nme,pars,false,null);

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("add",nme,ent,val,addevent);
        // must type check new constraint. 
        System.out.println("Match add: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationCPP(ent,nme,true);  
            addcode = addcode + update + "\n";
          } 
        }
      }


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,val,removeevent);
        // must type check new constraint. 
        System.out.println("Match remove: " + cnew); 
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs); 
          if (typed)
          { String update = cnew.updateOperationCPP(ent,nme,true);  
            removecode = removecode + update + "\n";
          } 
        }
      }

      String setindop = ""; 
      String argtype = e2name; 

      if (ordered)
      { setindop = "  void set" + role2 + "(int ind_x," + e2name + " " + role2 + "xx)\n" + 
          "  { if (ind_x >= 0 && ind_x < " + role2 + "->size()) { (*" + role2 + ")[ind_x] = " + role2 + "xx; } }\n\n"; 
        argtype = "vector<" + e2name + ">*"; 
      }
      else 
      { argtype = "set<" + e2name + ">*"; } 

      if (card2 == ZEROONE)
      { if (addOnly)
        { return " " + "void set" + role2 + "(" + argtype + " " +
             role2 +
             "xx) { if (" + role2 + "xx->size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " + setindop + 
             "  " + "void add" + role2 + "(" +
             e2name + " " + role2 + 
             "xx) { if (" + role2 + "->size() > 0) { return; } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        } 
        else 
        { return "  " + "void set" + role2 + "(" + argtype + " " + role2 +
             "xx) { if (" + role2 + "xx->size() > 1) { return; } \n" +
             "    " + assign + "\n" + code + "  }" + "\n \n " +
             setindop + 
             "  " + "void add" + role2 + "(" +
             e2name + " " + role2 + 
             "xx) { if (" + role2 + "->size() > 0) { " + role2 + "->clear(); } \n" +
             "    " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             "  " + "void remove" + role2 + "(" +
             e2name + " " + role2 +
             "xx) { " + remassign + "\n  " + removecode + "  }\n\n" + clearop;
        }
      }
      else
      { if (addOnly)        
        { return "  " + "void set" + role2 + "(" + argtype + " " + 
             role2 + "xx) { " + assign + "\n" + 
             code + "  }" + "\n \n " + setindop + 
             "  " + "void add" + role2 + "(" + e2name + " " + role2 + 
             "xx) { " + addassign +
             "\n  " + addcode + "  }" + "\n \n ";
        }
        else 
        { return "  " + "void set" + role2 + "(" + argtype + " " + 
             role2 + "xx) { " + assign + "\n  " + code + "  }" + "\n \n " +
             setindop + 
             "  "  + "void add" + role2 + "(" + e2name + " " + role2 + 
             "xx) { " + addassign +
             "\n  " + addcode + "  }" + "\n \n " + 
             "  " + "void remove" + role2 + "(" + e2name + " " + role2 +
             "xx) { " + remassign +
             "\n  " + removecode + "  }\n\n" + clearop;
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.

  public String setInterfaceOperation(Entity ent)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    // if (qualifier != null) 
    // { return qualifiedSetOperation(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "xx"; 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); } 
    valt.setElementType(new Type(entity2)); 

    if (card2 == ONE)
    { return "  void set" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);";
    }
    else  // but not a set/sequence type in pars? 
    { String setindop = ""; 

      if (ordered)
      { setindop = " void set" + role2 + "(int ind_x," + 
                                 entity2.getName() + " " + role2 + 
                   "xx);\n\n"; 
      }

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "  void set" + role2 + "(List " +
             role2 +
             "xx);\n \n " + setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);" + "\n \n ";
        } 
        else 
        { return "  void set" + role2 + "(List " +
             role2 +
             "xx);" + "\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);" + "\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "xx);";
        }
      }
      else
      { if (addOnly)
        { return "  void set" + role2 + "(List " +
             role2 + "xx);\n \n " +  setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);\n \n ";
        }
        else 
        { return "  void set" + role2 + "(List " +
             role2 + "xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "xx);";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  public String setInterfaceOperationJava6(Entity ent)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    // if (qualifier != null) 
    // { return qualifiedSetOperation(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "xx"; 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); } 
    valt.setElementType(new Type(entity2)); 

    if (card2 == ONE)
    { return "  void set" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);";
    }
    else  // but not a set/sequence type in pars? 
    { String setindop = ""; 
      String inpartype = ""; 

      if (ordered)
      { setindop = " void set" + role2 + "(int ind_x," + 
                                 entity2.getName() + " " + role2 + 
                   "xx);\n\n"; 
        inpartype = "ArrayList"; 
      }
      else 
      { inpartype = "HashSet"; } 

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "  void set" + role2 + "(" + inpartype + " " + role2 +
             "_xx);\n \n " +  setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + "_xx);" + "\n \n ";
        } 
        else 
        { return "  void set" + role2 + "(" + inpartype + " " + role2 +
             "_xx);" + "\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "_xx);" + "\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "_xx);";
        }
      }
      else
      { if (addOnly)
        { return "  void set" + role2 + "(" + inpartype + " " + role2 + "_xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + "_xx);\n \n ";
        }
        else 
        { return "  void set" + role2 + "(" + inpartype + " " + role2 + "_xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "_xx);\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "_xx);";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  public String setInterfaceOperationJava7(Entity ent)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    // if (qualifier != null) 
    // { return qualifiedSetOperation(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "xx"; 
    BasicExpression valbe = new BasicExpression(val); 
    String e2name = entity2.getName(); 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); 
      if (sortedAsc)
      { valt.setSorted(true); } 
    } 
    valt.setElementType(new Type(entity2)); 

    if (card2 == ONE)
    { return "  void set" + role2 + "(" + e2name + " " + role2 + "xx);"; }
    else  // but not a set/sequence type in pars? 
    { String setindop = ""; 
      String inpartype = ""; 

      if (ordered)
      { setindop = " void set" + role2 + "(int ind_x," + e2name + " " + role2 + 
                   "xx);\n\n"; 
        inpartype = "ArrayList<" + e2name + ">"; 
      }
      else if (sortedAsc) 
      { inpartype = "TreeSet<" + e2name + ">"; } 
      else  
      { inpartype = "HashSet<" + e2name + ">"; } 

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "  void set" + role2 + "(" + inpartype + " " + role2 +
             "_xx);\n \n " +  setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + "_xx);" + "\n \n ";
        } 
        else 
        { return "  void set" + role2 + "(" + inpartype + " " + role2 +
             "_xx);" + "\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "_xx);" + "\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "_xx);";
        }
      }
      else
      { if (addOnly)
        { return "  void set" + role2 + "(" + inpartype + " " + role2 + "_xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             e2name + " " + role2 + "_xx);\n \n ";
        }
        else 
        { return "  void set" + role2 + "(" + inpartype + " " + role2 + "_xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             e2name + " " + role2 + 
             "_xx);\n \n " + 
             "  void remove" + role2 + "(" +
             e2name + " " + role2 +
             "_xx);";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 

  public String setInterfaceOperationCSharp(Entity ent)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    // if (qualifier != null) 
    // { return qualifiedSetOperation(ent,cons,entities,types); } 

    String nme = role2; 
    String val = nme + "xx"; 
    BasicExpression valbe = new BasicExpression(val); 

    Type valt; 
    if (card2 == ONE)
    { valt = new Type(entity2); } 
    else if (ordered)
    { valt = new Type("Sequence",null); } 
    else 
    { valt = new Type("Set",null); } 
    valt.setElementType(new Type(entity2)); 

    if (card2 == ONE)
    { return "  void set" + role2 + "(" + entity2.getName() + " " + role2 + "xx);"; }
    else  // but not a set/sequence type in pars? 
    { String setindop = ""; 

      if (ordered)
      { setindop = " void set" + role2 + "(int ind_x," + 
                                 entity2.getName() + " " + role2 + "xx);\n\n"; 
      }

      if (card2 == ZEROONE)
      { if (addOnly)
        { return "  void set" + role2 + "(ArrayList " + role2 + "xx);\n \n " + setindop + 
             "  void add" + role2 + "(" + entity2.getName() + " " + role2 + 
             "xx);" + "\n \n ";
        } 
        else 
        { return "  void set" + role2 + "(ArrayList " +
             role2 + "xx);" + "\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);" + "\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "xx);";
        }
      }
      else
      { if (addOnly)
        { return "  void set" + role2 + "(ArrayList " + role2 + "xx);\n \n " + setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + "xx);\n \n ";
        }
        else 
        { return "  void set" + role2 + "(ArrayList " + role2 + "xx);\n \n " +
             setindop + 
             "  void add" + role2 + "(" +
             entity2.getName() + " " + role2 + 
             "xx);\n \n " + 
             "  void remove" + role2 + "(" +
             entity2.getName() + " " + role2 +
             "xx);";
        }
      }
    }
  }  // none of these if frozen, add, union if "addOnly". 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 


  /* Note that qualified associations from an interface are not permitted */ 

  public String qualifiedSetOperation(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { String argtype = ""; 
    String e2name = entity2.getName(); 

    if (card2 == MANY || card2 == ZEROONE)
    { argtype = "List"; } 
    else 
    { argtype = e2name; } 
    
    String setindop = 
      " public void set" + role2 + 
        "(String _ind, " + argtype + " " + role2 + "xx) { " + 
            role2 + ".put(_ind, " + role2 + "xx); }\n\n";

    String rem1op = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { Vector _removedKeys = new Vector();\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     if (" + role2 + "xx.equals(" + role2 + ".get(_k)))\n" + 
      "     { _removedKeys.add(_k); }\n" + 
      "   }\n" + 
      "   for (int _i = 0; _i < _removedKeys.size(); _i++)\n" + 
      "   { " + role2 + ".remove(_removedKeys.get(_i)); }\n" +  
      " }\n\n";  
    
    if (card2 == ONE) 
    { return setindop + rem1op; } 

    String test = 
      "   if (_old" + role2 + " == null)\n" + 
      "   { _old" + role2 + " = new Vector();\n" + 
      "    " + role2 + ".put(_ind, _old" + role2 + ");\n" + 
      "   }\n"; 

    if (ordered) { } 
    else  
    { test = test + "   if (_old" + role2 + ".contains(" + role2 + "xx)) { return; }\n"; }
 
    String addindop = 
      " public void add" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { List _old" + role2 + " = (List) " + role2 + ".get(_ind);\n" + test + 
      "   _old" + role2 + ".add(" + role2 + "xx); \n" + 
      " }\n\n"; 
    
    if (addOnly) 
    { return setindop + addindop; } 

    String remtest = 
      "   if (_old" + role2 + " == null) { return; }\n";
 
    String remindop = 
      " public void remove" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { List _old" + role2 + " = (List) " + role2 + ".get(_ind);\n" + remtest + 
      "   List _remove" + role2 + " = new Vector();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   _old" + role2 + ".removeAll(_remove" + role2 + "); \n" + 
      " }\n\n";

    String remop = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { List _remove" + role2 + " = new Vector();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     ((List) " + role2 + ".get(_k)).removeAll(_remove" + role2 + ");\n" + 
      "    }\n" + 
      " }\n\n";  
    
    return setindop + addindop + remindop + remop;  
  }  // and the general remove. removeAll is better than remove. 

  public String qualifiedSetOperationJava6(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { String argtype = ""; 
    String e2name = entity2.getName(); 

    if (card2 == MANY || card2 == ZEROONE)
    { argtype = "Collection"; } 
    else 
    { argtype = e2name; } 
    
    String setindop = 
      " public void set" + role2 + 
        "(String _ind, " + argtype + " " + role2 + "xx) { " + 
            role2 + ".put(_ind," + role2 + "xx); }\n\n";

    String rem1op = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _removedKeys = new ArrayList();\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     if (" + role2 + "xx.equals(" + role2 + ".get(_k)))\n" + 
      "     { _removedKeys.add(_k); }\n" + 
      "   }\n" + 
      "   for (int _i = 0; _i < _removedKeys.size(); _i++)\n" + 
      "   { " + role2 + ".remove(_removedKeys.get(_i)); }\n" +  
      " }\n\n";  
    
    if (card2 == ONE) 
    { return setindop + rem1op; } 

    String rantype = ""; 
    if (ordered) 
    { rantype = "ArrayList"; } 
    else 
    { rantype = "HashSet"; } 

    String test = 
      "   if (_old" + role2 + " == null)\n" + 
      "   { _old" + role2 + " = new " + rantype + "();\n" + 
      "    " + role2 + ".put(_ind, _old" + role2 + ");\n" + 
      "   }\n"; 

 
    String addindop = 
      " public void add" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { Collection _old" + role2 + " = (Collection) " + role2 + ".get(_ind);\n" + test + 
      "   _old" + role2 + ".add(" + role2 + "xx); \n" + 
      " }\n\n"; 
    
    if (addOnly) 
    { return setindop + addindop; } 

    String remtest = 
      "   if (_old" + role2 + " == null) { return; }\n";
 
    String remindop = 
      " public void remove" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { Collection _old" + role2 + " = (Collection) " + role2 + ".get(_ind);\n" + remtest + 
      "   ArrayList _remove" + role2 + " = new ArrayList();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   _old" + role2 + ".removeAll(_remove" + role2 + "); \n" + 
      " }\n\n";

    String remop = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _remove" + role2 + " = new ArrayList();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     ((Collection) " + role2 + ".get(_k)).removeAll(_remove" + role2 + ");\n" + 
      "    }\n" + 
      " }\n\n";  
    
    return setindop + addindop + remindop + remop;  
  }  // and the general remove. removeAll is better than remove. 

  public String qualifiedSetOperationJava7(Entity ent, Vector cons,
                             Vector entities, Vector types)
  { String argtype = ""; 
    String e2name = entity2.getName(); 

    if (card2 == MANY || card2 == ZEROONE)
    { if (ordered) 
      { argtype = "ArrayList<" + e2name + ">"; } 
      else if (sortedAsc)
      { argtype = "TreeSet<" + e2name + ">"; } 
      else 
      { argtype = "HashSet<" + e2name + ">"; } 
    }  
    else 
    { argtype = e2name; } 
    
    String setindop = 
      " public void set" + role2 + 
        "(String _ind, " + argtype + " " + role2 + "xx) { " + 
            role2 + ".put(_ind," + role2 + "xx); }\n\n";

    String rem1op = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList<String> _removedKeys = new ArrayList();\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     if (" + role2 + "xx.equals(" + role2 + ".get(_k)))\n" + 
      "     { _removedKeys.add((String) _k); }\n" + 
      "   }\n" + 
      "   for (int _i = 0; _i < _removedKeys.size(); _i++)\n" + 
      "   { " + role2 + ".remove(_removedKeys.get(_i)); }\n" +  
      " }\n\n";  
    
    if (card2 == ONE) 
    { return setindop + rem1op; } 

    String rantype = argtype; 

    String test = 
      "   if (_old" + role2 + " == null)\n" + 
      "   { _old" + role2 + " = new " + rantype + "();\n" + 
      "    " + role2 + ".put(_ind, _old" + role2 + ");\n" + 
      "   }\n"; 

    // if (ordered) 
    // { test = test + "   if (_old" + role2 + ".contains(" + role2 + "xx)) { return; }\n"; }
 
    String addindop = 
      " public void add" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { " + argtype + " _old" + role2 + " = (" + argtype + ") " + role2 + ".get(_ind);\n" + test + 
      "   _old" + role2 + ".add(" + role2 + "xx); \n" + 
      " }\n\n"; 
    
    if (addOnly) 
    { return setindop + addindop; } 

    String remtest = 
      "   if (_old" + role2 + " == null) { return; }\n";
 
    String remindop = 
      " public void remove" + role2 + 
        "(String _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { " + argtype + " _old" + role2 + " = (" + argtype + ") " + role2 + ".get(_ind);\n" + remtest + 
      "   ArrayList<" + e2name + "> _remove" + role2 + " = new ArrayList<" + e2name + ">();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   _old" + role2 + ".removeAll(_remove" + role2 + "); \n" + 
      " }\n\n";

    String remop = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList<" + e2name + "> _remove" + role2 + " = new ArrayList<" + e2name + ">();\n" + 
      "   _remove" + role2 + ".add(" + role2 + "xx);\n" + 
      "   java.util.Iterator _keys = " + role2 + ".keySet().iterator();\n" + 
      "   while (_keys.hasNext())\n" + 
      "   { Object _k = _keys.next();\n" +  
      "     ((Collection) " + role2 + ".get(_k)).removeAll(_remove" + role2 + ");\n" + 
      "    }\n" + 
      " }\n\n";  
    
    return setindop + addindop + remindop + remop;  
  }  // and the general remove. removeAll is better than remove. 

  public String qualifiedSetOperationCSharp(Entity ent, Vector cons,
                                            Vector entities, Vector types)
  { String argtype = ""; 
    String e2name = entity2.getName(); 

    if (card2 == MANY || card2 == ZEROONE)
    { argtype = "ArrayList"; } 
    else 
    { argtype = e2name; } 
    
    String setindop = 
      " public void set" + role2 + 
        "(string _ind, " + argtype + " " + role2 + "xx) { " + 
            role2 + "[_ind] = " + role2 + "xx; }\n\n";

    String rem1op = 
      " public void remove" + role2 + "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _removedKeys = new ArrayList();\n" + 
      "   foreach (DictionaryEntry _de in " + role2 + ")\n" + 
      "   { string _k = (string) _de.Key;\n" +  
      "     if (" + role2 + "xx.Equals(" + role2 + "[_k]))\n" + 
      "     { _removedKeys.Add(_k); }\n" + 
      "   }\n" + 
      "   for (int _i = 0; _i < _removedKeys.Count; _i++)\n" + 
      "   { " + role2 + ".Remove(_removedKeys[_i]); }\n" +  
      " }\n\n";  
    
    if (card2 == ONE) 
    { return setindop + rem1op; } 

    String test = 
      "   if (_old" + role2 + " == null)\n" + 
      "   { _old" + role2 + " = new ArrayList();\n" + 
      "    " + role2 + "[_ind] = _old" + role2 + ";\n" + 
      "   }\n"; 

    if (ordered) { } 
    else 
    { test = test + "   if (_old" + role2 + ".Contains(" + role2 + "xx)) { return; }\n"; }
 
    String addindop = 
      " public void add" + role2 + 
        "(string _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _old" + role2 + " = (ArrayList) " + role2 + "[_ind];\n" + test + 
      "   _old" + role2 + ".Add(" + role2 + "xx); \n" + 
      " }\n\n"; 
    
    if (addOnly) 
    { return setindop + addindop; } 

    String remtest = 
      "   if (_old" + role2 + " == null) { return; }\n";
 
    String remindop = 
      " public void remove" + role2 + 
        "(string _ind, " + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _old" + role2 + " = (ArrayList) " + role2 + "[_ind];\n" + remtest + 
      "   ArrayList _remove" + role2 + " = new ArrayList();\n" + 
      "   _remove" + role2 + ".Add(" + role2 + "xx);\n" + 
      "   _old" + role2 + " = SystemTypes.subtract(_old" + role2 + ", _remove" + role2 + "); \n" + 
      " }\n\n";

    String remop = 
      " public void remove" + role2 + 
        "(" + e2name + " " + role2 + "xx)\n" + 
      " { ArrayList _remove" + role2 + " = new ArrayList();\n" + 
      "   _remove" + role2 + ".Add(" + role2 + "xx);\n" + 
      "   foreach ( DictionaryEntry _de in " + role2 + ")\n" + 
      "   { string _k = (string) _de.Key;\n" +  
      "     " + role2 + "[_k] = SystemTypes.subtract(" + role2 + "[_k], _remove" + role2 + ");\n" + 
      "    }\n" + 
      " }\n\n";  
    
    return setindop + addindop + remindop + remop;  
  }  // and the general remove. removeAll is better than remove. 

  public String qualifiedSetOperationCPP(Entity ent, Vector cons,
                                            Vector entities, Vector types)
  { String argtype = ""; 
    String argtype0 = ""; 
    String e2name = entity2.getName(); 

    if (card2 == MANY || card2 == ZEROONE)
    { if (ordered) 
      { argtype0 = "vector<" + e2name + "*>"; }
      else 
      { argtype0 = "set<" + e2name + "*>"; } 
    }  
    else 
    { argtype0 = e2name; } 
    argtype = argtype0 + "*"; 

    String setindop = 
      " void set" + role2 + 
        "(string _ind, " + argtype + " " + role2 + "xx) { (*" + 
            role2 + ")[_ind] = " + role2 + "xx; }\n\n";

    String rem1op = 
      " void remove" + role2 + "(" + e2name + "* " + role2 + "xx)\n" + 
      " { vector<string>* _removedKeys = new vector<string>();\n" + 
      "   for (map<string,argtype>::iterator _pos = " + role2 + "->begin(); " + 
               "_pos != " + role2 + "->end(); _pos++)\n" + 
      "   { if (" + role2 + "xx == _pos->second)\n" + 
      "     { _removedKeys->push_back(pos->first); }\n" + 
      "   }\n" + 
      "   for (int _i = 0; _i < _removedKeys->size(); _i++)\n" + 
      "   { " + role2 + "->erase((*_removedKeys)[_i]); }\n" +  
      " }\n\n";  
    
    if (card2 == ONE) 
    { return setindop + rem1op; } 

    String test =
      "   if (_old" + role2 + " == " + role2 + "->end())\n" + 
      "   { (*" + role2 + ")[_ind] = new " + argtype0 + "(); }\n"; 

    String insertop = "insert"; 
    if (ordered) 
    { insertop = "push_back"; } 
 
    String addindop = 
      " void add" + role2 + 
        "(string _ind, " + e2name + "* " + role2 + "xx)\n" + 
      " { map<string," + argtype + ">::iterator _old" + role2 + " = " + 
                           role2 + "->find(_ind);\n" + test + 
      "   (*" + role2 + ")[_ind]->" + insertop + "(" + role2 + "xx); \n" + 
      " }\n\n"; 
    
    if (addOnly) 
    { return setindop + addindop; } 

    String remtest = 
      "   if (_old" + role2 + " == " + role2 + "->end()) { return; }\n";
 
    String remindop = 
      " void remove" + role2 + 
        "(string _ind, " + e2name + "* " + role2 + "xx)\n" + 
      " { map<string," + argtype + ">::iterator _old" + role2 + " = " + role2 + "->find(_ind);\n" + remtest + 
      "   (_old" + role2 + "->second)->erase(" + role2 + "xx); \n" + 
      " }\n\n";

    String remop = 
      " void remove" + role2 + 
        "(" + e2name + "* " + role2 + "xx)\n" + 
      " { for (map<string," + argtype + ">::iterator _pos = " + role2 + "->begin(); " + 
               "_pos != " + role2 + "->end(); ++_pos)\n" + 
      "   { (_pos->second)->erase(" + role2 + "xx); }\n" + 
      " }\n\n";  
    
    return setindop + addindop + remindop + remop;  
  }  // and the general remove. removeAll is better than remove. 


  // for controller interface:
  public String interfaceSetOperation(Entity ent)
  { if (role2 == null) { return ""; }
    if (frozen) { return ""; } 
    if (ent.isInterface()) { return ""; } 
    if (isDerived() || isImplicit()) { return ""; } 
     
    String nme = role2; 
    String val = nme + "xx"; 
    String ename = ent.getName(); 
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x"; 
    String edec = ename + " " + ex + ", "; 

    if (qualifier != null)
    { if (card2 == ONE) 
      { return " public void set" + role2 + "(" + edec +  
             " String _ind, " + e2name + " " + val + ");\n" + 
             " public void remove" + role2 + "(" + edec + e2name + " " + role2 +
                  "x);\n";
      }
      else
      { if (addOnly)
        { return " public void set" + role2 + "(" + edec + 
                   "String _ind, List " + role2 + "xx);\n  " +
             " public void add" + role2 + "(" + edec + 
             "String _ind, " + e2name + " " + role2 + "xx);\n  ";
        }
        else 
        { return " public void set" + role2 + "(" + edec + "String _ind, " +
                  "List " + role2 + "xx);\n  " +
             " public void add" + role2 + "(" + edec + 
                  "String _ind, " + e2name + " " + role2 + "xx);\n  " + 
             " public void remove" + role2 + "(" + edec + 
                  "String _ind, " + e2name + " " + role2 + "xx);\n  " +
             " public void remove" + role2 + "(" + edec + e2name + " " + role2 +
                  "x);\n";
        }
      }   // and the more general remove without _ind. 
    }

    if (card2 == ONE)
    { return " public void set" + role2 + "(" + edec +  
             e2name + " " + val + ");\n";
    }
    else
    { if (addOnly)
      { return " public void set" + role2 + "(" + edec + "List " +
             role2 + "xx);\n  " +
             " public void add" + role2 + "(" + edec + 
             e2name + " " + role2 + "xx);\n  ";
      }
      else 
      { return " public void set" + role2 + "(" + edec + "List " +
             role2 + "xx);\n  " +
             " public void add" + role2 + "(" + edec + 
             e2name + " " + role2 + "xx);\n  " + 
             " public void remove" + role2 + "(" + edec + 
             e2name + " " + role2 + "xx);\n  ";
      }
    }
  }  // none of these if frozen, add, union if "addOnly". Ops for sequences? 
     // Also generate reaction code.
     // PRE for ZEROONE: if (role2.size() > 0) { return; } 


  /* Qualified case is not included for B */ 

  public Vector senBOperationsCode(Vector cons, Entity ent, 
                                   Vector entities, Vector types)
  { Vector res = new Vector();
    String nme = role2;
    if (role2 == null || frozen)
    { return res; } 

    String ename = ent.getName(); // must be entity1
    String e2name = entity2.getName(); 
    String es = ename.toLowerCase() + "s";
    BExpression esbe = new BBasicExpression(es); 
    String ex = ename.toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex);

    java.util.Map env = new java.util.HashMap(); 
    env.put(ename,exbe); 

    Vector pars = new Vector(); 
    pars.add(ex); 
    BExpression pre = new BBinaryExpression(":",exbe,esbe); 
    String attx = nme + "xx";
    
    String e2s = e2name.toLowerCase() + "s";
    pars.add(attx); 
    BParallelStatement stat = new BParallelStatement(); 
    BExpression attbe = new BBasicExpression(attx);
    
    BExpression btbe;
    Type type; 
    BExpression elembtbe = new BBasicExpression(e2s);
    Type elemtype = new Type(entity2);
    if (card2 == ONE) // just setrole2(ex,attx)
    { btbe = elembtbe;
      type = elemtype; 
    }
    else 
    { if (ordered)
      { btbe = new BUnaryExpression("seq",new BBasicExpression(e2s)); 
        type = new Type("Sequence",null); 
      } 
      else 
      { btbe = new BUnaryExpression("FIN",new BBasicExpression(e2s)); 
        type = new Type("Set",null);
      } 
      type.setElementType(elemtype); 
    } 
    BExpression setpre = 
      new BBinaryExpression("&",pre,new BBinaryExpression(":",attbe,btbe)); 
    Vector callpars = new Vector();
    callpars.add(exbe); 
    callpars.add(attbe); 

    BExpression lhs = new BApplyExpression(nme,exbe);
    Vector vals = new Vector();
    vals.add(attbe);
    BSetExpression nmese = new BSetExpression(vals,ordered);
    BExpression addrhs;
    if (ordered)
    { addrhs = new BBinaryExpression("^",lhs,nmese); } 
    else 
    { addrhs = new BBinaryExpression("\\/",lhs,nmese); }
    addrhs.setBrackets(true); 
    BExpression remrhs = new BBinaryExpression("-",lhs,nmese); 
    remrhs.setBrackets(true); 


    BStatement opcall = new BOperationCall("set" + nme,callpars); 
    opcall.setWriteFrame(nme); 
    stat.addStatement(opcall);
    Attribute epar = new Attribute(ex,new Type(ent),ModelElement.INTERNAL); 
    Vector v1 = new Vector();
    Attribute attpar = new Attribute(attx,type,ModelElement.INTERNAL);
    attpar.setElementType(elemtype); 
    v1.add(attpar); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);

    if (ordered) 
    { event.setOrdered(true); } 

    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.bmatches("set",nme,ent,attx,event);
      if (cnew != null)
      { Constraint cnew2 = cnew.normalise(); 
        System.out.println("New constraint for set: " + cnew2); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew2.typeCheck(types,entities,cntxs);
        if (typed)
        { BStatement update = cnew2.synthesiseBCode(ent,nme,false); 
          // bupdateOperation(ent,false);
          stat.addStatement(update);
        } 
      } 
      else if (cc.getEvent() == null &&
               cc.involvesFeature(nme))
      { env = new java.util.HashMap(); 
        env.put(ename,exbe);  
        BExpression inv = // cc.binvariantForm(env,false);
                          cc.bprecondition(env);  
        inv.setBrackets(true); 
        BExpression inv1 = inv.substituteEq(nme + "(" + ex + ")", attbe);  
        setpre = new BBinaryExpression("&",setpre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!
 
    }  // separate updates?: 
    BOp op = new BOp("set_" + nme,null,pars,setpre,stat);  
    res.add(op); 

    if (card2 != ONE) // gen add and remove
    { BStatement addopcall = new BOperationCall("add" + nme,callpars); 
      addopcall.setWriteFrame(nme); 
      BParallelStatement addstat = new BParallelStatement(); 
      addstat.addStatement(addopcall); 
      Vector v2 = new Vector();
      Attribute addpar = new Attribute(attx,elemtype,ModelElement.INTERNAL);
      v2.add(epar); 
      v2.add(addpar); 
      BehaviouralFeature addevent =
        new BehaviouralFeature("add" + nme,v2,false,null);

      if (ordered) 
      { addevent.setOrdered(true); 
        String e2x = e2name.toLowerCase() + "x";
        BExpression e2xbe = new BBasicExpression(e2x); 
        Vector indpars = new Vector(); 
        indpars.add(ex); 
        indpars.add("ind"); 
        indpars.add(e2x);  
        BBasicExpression indbe = new BBasicExpression("ind"); 
        BExpression indpre = new BBinaryExpression(":",exbe,esbe); 
        BBinaryExpression pre2 =
          new BBinaryExpression(":",e2xbe,elembtbe);
        indpre = 
          new BBinaryExpression("&",indpre,pre2);  

        BExpression indom = new BBinaryExpression(":",indbe,
                                  new BUnaryExpression("dom",lhs)); 


        indpre = new BBinaryExpression("&",indpre,indom); 
        BStatement setindcall = new BOperationCall("set" + nme,indpars); 
      
        BOp setind = new BOp("set_" + nme,null,indpars,indpre,setindcall); 
        res.add(setind); 
      } // add for sequences

      BExpression addpre = 
        new BBinaryExpression("&",pre,new BBinaryExpression(":",attbe,elembtbe)); 


      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.bmatches("add",nme,ent,attx,addevent);
        if (cnew != null)
        { Constraint cnew2 = cnew.normalise(); 
          System.out.println("New constraint for add: " + cnew2); 
          Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
 
          boolean typed = cnew2.typeCheck(types,entities,cntxs);
          if (typed)
          { BStatement update = cnew2.synthesiseBCode(ent,nme,false); 
          // bupdateOperation(ent,false);
            addstat.addStatement(update);
          } 
        } 
        else if (cc.getEvent() == null &&
                 cc.involvesFeature(nme))
        { env = new java.util.HashMap(); 
          env.put(ename,exbe);  
          BExpression inv = // cc.binvariantForm(env,false);
                            cc.bprecondition(env);  
          inv.setBrackets(true); 
          BExpression inv1 = inv.substituteEq(nme + "(" + ex + ")", addrhs);  
          addpre = new BBinaryExpression("&",addpre,inv1); 
        } // must ensure that not both an invariant and its contrapositive are used!

      }
      // separate updates in addstat!

   
      BOp addop = new BOp("add_" + nme,null,pars,addpre,addstat);  
      res.add(addop); 
      
      if (!addOnly)
      { BStatement remopcall = new BOperationCall("remove" + nme,callpars); 
        remopcall.setWriteFrame(nme); 
        BParallelStatement remstat = new BParallelStatement(); 
        remstat.addStatement(remopcall); 
        BehaviouralFeature remevent =
          new BehaviouralFeature("remove" + nme,v2,false,null);

        BExpression rempre = 
          new BBinaryExpression("&",pre,new BBinaryExpression(":",attbe,elembtbe)); 


        if (ordered) 
        { remevent.setOrdered(true); } 

        for (int j = 0; j < cons.size(); j++)
        { Constraint cc = (Constraint) cons.get(j);
          Constraint cnew = cc.bmatches("remove",nme,ent,attx,remevent);
          if (cnew != null)
          { Constraint cnew2 = cnew.normalise(); 
            System.out.println("New constraint for remove: " + cnew2); 
            Vector cntxs = new Vector(); 
            if (cnew.getOwner() != null) 
            { cntxs.add(cnew.getOwner()); }
 
            boolean typed = cnew2.typeCheck(types,entities,cntxs);
            if (typed)
            { BStatement update = cnew2.synthesiseBCode(ent,nme,false); 
              remstat.addStatement(update);
            } 
          } 
          else if (cc.getEvent() == null &&
                   cc.involvesFeature(nme))
          { env = new java.util.HashMap(); 
            env.put(ename,exbe);  
            BExpression inv = // cc.binvariantForm(env,false);
                              cc.bprecondition(env);  
            inv.setBrackets(true); 
            BExpression inv1 = inv.substituteEq(nme + "(" + ex + ")", remrhs);  
            rempre = new BBinaryExpression("&",rempre,inv1); 
          } //

        }
        BOp remop = new BOp("remove_" + nme,null,pars,rempre,remstat);  
        res.add(remop);
      }
    }  
    return res;
  }  // conjoin preconditions from the event for each

  public String setAllOperation(String ename)
  { // public static void setAllrole(List es, T val)
    // { update e.role for e in es }
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name; 
    if (card2 != ONE)
    { typ = "List"; }

    String es = ename.toLowerCase() + "s";
    String res = "public static void setAll" + role2;
    res = res + "(List " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es +
          ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      Controller.inst().set" + role2 + "(" + ex + "," + qual + " _val); } }";
    if (ordered)
    { res = res + "\n\n" + 
            "  public static void setAll" + role2;
      res = res + "(List " + es + ", int _ind," + e2name + " _val)\n";
      res = res + "  { for (int _i = 0; _i < " + es +
            ".size(); _i++)\n";
      res = res + "    { " + ename + " " + ex + " = (" +
            ename + ") " + es + ".get(_i);\n";
      res = res + "      Controller.inst().set" + role2 + 
            "(" + ex + ",_ind,_val); } }";
    }

    return res;
  }

  public String setAllOperationJava6(String ename)
  { // public static void setAllrole(List es, T val)
    // { update e.role for e in es }
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String e2name = entity2.getName();
    String typ = e2name; 
    if (card2 != ONE)
    { if (ordered) 
      { typ = "ArrayList"; }
      else 
      { typ = "HashSet"; } 
    } 

    String es = ename.toLowerCase() + "_s";
    String res = "public static void setAll" + role2;
    res = res + "(Collection " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().set" + role2 + "(" + ex + "," + qual + " _val); } }";
    if (ordered)
    { res = res + "\n\n" + 
            "  public static void setAll" + role2;
      res = res + "(Collection " + es + ", int _ind," + e2name + " _val)\n";
      res = res + "  { for (Object _o : " + es + ")\n";
      res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
      res = res + "      Controller.inst().set" + role2 + 
            "(" + ex + ",_ind,_val); } }";
    }

    return res;
  }

  public String setAllOperationJava7(String ename)
  { // public static void setAllrole(List es, T val)
    // { update e.role for e in es }
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String e2name = entity2.getName();
    String typ = e2name; 
    if (card2 != ONE)
    { if (ordered) 
      { typ = "ArrayList<" + e2name + ">"; }
      else if (sortedAsc)
      { typ = "TreeSet<" + e2name + ">"; } 
      else 
      { typ = "HashSet<" + e2name + ">"; } 
    } 

    String es = ename.toLowerCase() + "_s";
    String res = "public static void setAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().set" + role2 + "(" + ex + ", " + qual + " _val); } }";
    if (ordered)
    { res = res + "\n\n" + 
            "  public static void setAll" + role2;
      res = res + "(Collection<" + ename + "> " + es + ", int _ind," + e2name + " _val)\n";
      res = res + "  { for (Object _o : " + es + ")\n";
      res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
      res = res + "      Controller.inst().set" + role2 + 
            "(" + ex + ", _ind, _val); } }";
    }

    return res;
  }

  public String setAllOperationCSharp(String ename)
  { // public static void setAllrole(ArrayList es, T val)
    // { update e.role for e in es }
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String e2name = entity2.getName();
    String typ = e2name; 
    if (card2 != ONE)
    { typ = "ArrayList"; }

    String es = ename.toLowerCase() + "_s";
    String res = "public static void setAll" + role2;
    res = res + "(ArrayList " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es +
          ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      Controller.inst().set" + role2 + "(" + ex + "," + qual + " _val); } }";
    if (ordered)
    { res = res + "\n\n" + 
            "  public static void setAll" + role2;
      res = res + "(ArrayList " + es + ", int _ind," + e2name + " _val)\n";
      res = res + "  { for (int _i = 0; _i < " + es +
            ".Count; _i++)\n";
      res = res + "    { " + ename + " " + ex + " = (" +
            ename + ") " + es + "[_i];\n";
      res = res + "      Controller.inst().set" + role2 + 
            "(" + ex + ", _ind, _val); } }";
    }

    return res;
  }

  public String setAllOperationCPP(String ename, Vector declarations)
  { // static void setAllrole(set<ename*>* es, T val)
    // static void setAllrole(vector<ename*>* es, T val)
    // { update e.role for e in es }
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name + "*"; 
    if (card2 != ONE)
    { if (ordered) 
      { typ = "vector<" + e2name + "*>*"; }
      else 
      { typ = "set<" + e2name + "*>*"; }
    } 
    String argtyp1 = "set<" + ename + "*>*"; 
    String argtyp2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  void " + ename + "::setAll" + role2;
    res = res + "(" + argtyp1 + " " + es + ", " + qualt + typ + " _val)\n";

    String declaration = "  static void setAll" + role2;
    declaration = declaration + "(" + argtyp1 + " " + es + ", " + qualt + typ + " _val);\n";

    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->set" + role2 + "(" + ex + ", " + qual + " _val); } }\n\n";

    declaration = declaration + "  static void setAll" + role2;
    declaration = declaration + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val);\n";

    res = res + "  void " + ename + "::setAll" + role2;
    res = res + "(" + argtyp2 + " " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->set" + role2 + "(" + ex + ", " + qual + " _val); } }\n\n";

    if (ordered)
    { declaration = declaration + 
            "  static void setAll" + role2 + "(" + argtyp1 + " " + es + 
                                             ", int _ind, " + e2name + "* _val);\n";
      res = res + "\n\n" + 
            "  void " + ename + "::setAll" + role2;
      res = res + "(" + argtyp1 + " " + es + ", int _ind, " + e2name + "* _val)\n";
      res = res + "  { set<" + ename + "*>::iterator _pos;\n";
      res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
      res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
      res = res + "      Controller::inst->set" + role2 + "(" + ex + ", _ind, _val); } }";
    } // and vector es version

    declarations.add(declaration); 
    return res;
  }

  public String addAllOperation(String ename)
  { // public static void addAllrole(List es, T val)
    // { add val to e.role for e in es } 

    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "s";
    String res = "public static void addAll" + role2;
    res = res + "(List " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      Controller.inst().add" + role2 + "(" + ex +
                ", " + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String addAllOperationJava6(String ename)
  { // public static void addAllrole(List es, T val)
    // { add val to e.role for e in es } 

    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void addAll" + role2;
    res = res + "(Collection " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().add" + role2 + "(" + ex +
                ", " + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String addAllOperationJava7(String ename)
  { // public static void addAllrole(List es, T val)
    // { add val to e.role for e in es } 

    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void addAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().add" + role2 + "(" + ex +
                ", " + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller


  public String addAllOperationCSharp(String ename)
  { // public static void addAllrole(ArrayList es, T val)
    // { add val to e.role for e in es } 

    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void addAll" + role2;
    res = res + "(ArrayList " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      Controller.inst().add" + role2 + "(" + ex +
                ", " + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String addAllOperationCPP(String ename, Vector declarations)
  { // static void addAllrole(set<ename*>* es, T val)
    // static void addAllrole(vector<ename*>* es, T val)
    // { update e.role for e in es }

    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name + "*"; 
    String argtyp1 = "set<" + ename + "*>*"; 
    String argtyp2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  void " + ename + "::addAll" + role2;
    res = res + "(" + argtyp1 + " " + es + ", " + qualt + typ + " _val)\n";

    String declaration = "  static void addAll" + role2;
    declaration = declaration + "(" + argtyp1 + " " + es + ", " + qualt + typ + " _val);\n";

    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->add" + role2 + "(" + ex + ", " + qual + " _val); } }\n\n";

    declaration = declaration + "  static void addAll" + role2;
    declaration = declaration + "(" + argtyp2 + " " + es + ", " + qualt + typ + " _val);\n";

    res = res + "  void " + ename + "::addAll" + role2;
    res = res + "(" + argtyp2 + " " + es + ", " + qualt + typ + " _val)\n";
    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->add" + role2 + "(" + ex + ", " + qual + " _val); } }\n\n";

    declarations.add(declaration); 
    return res;
  }


  public String removeAllOperation(String ename)
  { // public static void addAllrole(List es, T val)
    // { remove val from e.role for e in es } 

    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "s";
    String res = "public static void removeAll" + role2;
    res = res + "(List " + es + "," + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es +
          ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      Controller.inst().remove" + role2 + "(" + ex +
                "," + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String removeAllOperationJava6(String ename)
  { // public static void addAllrole(List es, T val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void removeAll" + role2;
    res = res + "(Collection " + es + "," + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().remove" + role2 + "(" + ex +
                "," + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String removeAllOperationJava7(String ename)
  { // public static void addAllrole(List es, T val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "_x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void removeAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + "," + qualt + typ + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().remove" + role2 + "(" + ex +
                "," + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String removeAllOperationCSharp(String ename)
  { // public static void addAllrole(ArrayList es, T val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void removeAll" + role2;
    res = res + "(ArrayList " + es + "," + qualt + typ + " _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      Controller.inst().remove" + role2 + "(" + ex +
                "," + qual + " _val); } }\n";
    return res;
  }  // have to append prefix of particular controller

  public String removeAllOperationCPP(String ename, Vector declarations)
  { // static void removeAllrole(set<ename*>* es, T val)
    // static void removeAllrole(vector<ename*>* es, T val)
    // { update e.role for e in es }
    if (frozen || addOnly || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name + "*"; 
    String argtyp1 = "set<" + ename + "*>*"; 
    String argtyp2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  void " + ename + "::removeAll" + role2;
    res = res + "(" + argtyp1 + " " + es + "," + qualt + typ + " _val)\n";

    String declaration = "  static void removeAll" + role2;
    declaration = declaration + "(" + argtyp1 + " " + es + "," + qualt + typ + " _val);\n";

    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->remove" + role2 + "(" + ex + "," + qual + " _val); } }\n\n";
    res = res + "  void " + ename + "::removeAll" + role2;
    res = res + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val)\n";
    declaration = declaration + "  static void removeAll" + role2;
    declaration = declaration + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val);\n";
    
    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->remove" + role2 + "(" + ex + "," + qual + " _val); } }\n\n";

    declarations.add(declaration); 
    return res;
  }


  public String unionAllOperation(String ename)
  { // public static void unionAllrole(List es, List val)
    // { add val to e.role for e in es } 
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "s";
    String res = "public static void unionAll" + role2;
    res = res + "(List " + es + "," + qualt + " List _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      Controller.inst().union" + role2 + "(" + ex + "," + qual + " _val); } }\n";
    return res;
  }

  public String unionAllOperationJava6(String ename)
  { // public static void unionAllrole(List es, List val)
    // { add val to e.role for e in es } 
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ptype = ""; 

    // if (ordered) 
    // { ptype = "ArrayList"; } 
    // else 
    // { ptype = "HashSet"; } 
    ptype = "Collection"; 

    String ex = ename.toLowerCase() + "_x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void unionAll" + role2;
    res = res + "(Collection " + es + "," + qualt + " " + ptype + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().union" + role2 + "(" + ex + "," + qual + " _val); } }\n";
    return res;
  }

  public String unionAllOperationJava7(String ename)
  { // public static void unionAllrole(List es, List val)
    // { add val to e.role for e in es } 
    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String e2name = entity2.getName(); 

    String ptype = "Collection<" + e2name + ">"; 

    /* if (ordered) 
    { ptype = "ArrayList<" + e2name + ">"; } 
    else 
    { ptype = "Set<" + e2name + ">"; } */ 

    String ex = ename.toLowerCase() + "_x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void unionAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + "," + qualt + " " + ptype + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().union" + role2 + "(" + ex + "," + qual + " _val); } }\n";
    return res;
  }

  public String unionAllOperationCSharp(String ename)
  { // public static void unionAllrole(ArrayList es, ArrayList val)
    // { add val to e.role for e in es } 
    if (frozen) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void unionAll" + role2;
    res = res + "(ArrayList " + es + "," + qualt + " ArrayList _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") " + es + "[_i];\n";
    res = res + "      Controller.inst().union" + role2 + "(" + ex + "," + qual + " _val); } }\n";
    return res;
  }

  public String unionAllOperationCPP(String ename, Vector declarations)
  { // static void unionAllrole(set<ename*>* es, T val)
    // static void unionAllrole(vector<ename*>* es, T val)
    // { update e.role for e in es }
    if (frozen || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name + "*"; 
    if (card2 != ONE)
    { if (ordered) 
      { typ = "vector<" + e2name + "*>*"; }
      else 
      { typ = "set<" + e2name + "*>*"; }
    } // but actually need 2 versions as val could be any collection, independent of role2's type

    String argtyp1 = "set<" + ename + "*>*"; 
    String argtyp2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  void " + ename + "::unionAll" + role2;
    res = res + "(" + argtyp1 + " " + es + "," + qualt + typ + " _val)\n";

    String declaration = "  static void unionAll" + role2;
    declaration = declaration + "(" + argtyp1 + " " + es + "," + qualt + typ + " _val);\n";

    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->union" + role2 + "(" + ex + "," + qual + " _val); } }\n\n";
    res = res + "  void " + ename + "::unionAll" + role2;
    res = res + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val)\n";
    declaration = declaration + "  static void unionAll" + role2;
    declaration = declaration + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val);\n";

    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->union" + role2 + "(" + ex + "," + qual + " _val); } }\n\n";

    declarations.add(declaration); 
    return res;
  }

  public String subtractAllOperation(String ename)
  { // public static void subtractAllrole(List es, List val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "s";
    String res = "public static void subtractAll" + role2;
    res = res + "(List " + es + ", " + qualt + " List _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es +
          ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      Controller.inst().subtract" + role2 +
                "(" + ex + ", " + qual + " _val); } }\n";
    return res;
  }

  public String subtractAllOperationJava6(String ename)
  { // public static void subtractAllrole(List es, List val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String ptype = "Collection"; 
    /* if (ordered) 
    { ptype = "ArrayList"; } 
    else 
    { ptype = "HashSet"; } */ 

    String ex = ename.toLowerCase() + "_x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void subtractAll" + role2;
    res = res + "(Collection " + es + ", " + qualt + " " + ptype + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().subtract" + role2 +
                "(" + ex + ", " + qual + " _val); } }\n";
    return res;
  }

  public String subtractAllOperationJava7(String ename)
  { // public static void subtractAllrole(List es, List val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "String _arg, "; 
      qual = "_arg, "; 
    } 

    String e2name = entity2.getName(); 

    String ptype = "Collection<" + e2name + ">"; 
    /* if (ordered) 
    { ptype = "ArrayList"; } 
    else 
    { ptype = "HashSet"; } */ 

    String ex = ename.toLowerCase() + "_x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void subtractAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + ", " + qualt + " " + ptype + " _val)\n";
    res = res + "  { for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      Controller.inst().subtract" + role2 +
                "(" + ex + ", " + qual + " _val); } }\n";
    return res;
  }

  public String subtractAllOperationCSharp(String ename)
  { // public static void subtractAllrole(ArrayList es, ArrayList val)
    // { remove val from e.role for e in es } 
    if (frozen || addOnly) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    // String typ = entity2.getName();
    String es = ename.toLowerCase() + "_s";
    String res = "public static void subtractAll" + role2;
    res = res + "(ArrayList " + es + ", " + qualt + " ArrayList _val)\n";
    res = res + "  { for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      Controller.inst().subtract" + role2 +
                "(" + ex + ", " + qual + " _val); } }\n";
    return res;
  }

  public String subtractAllOperationCPP(String ename, Vector declarations)
  { // static void subtractAllrole(set<ename*>* es, T val)
    // static void subtractAllrole(vector<ename*>* es, T val)
    // { update e.role for e in es }
    if (frozen || addOnly || card2 == ONE) { return ""; } 
    String qualt = ""; 
    String qual = ""; 
    if (qualifier != null) 
    { qualt = "string _arg, "; 
      qual = "_arg, "; 
    } 

    String ex = ename.toLowerCase() + "x";
    String e2name = entity2.getName();
    String typ = e2name + "*"; 
    if (card2 != ONE)
    { if (ordered) 
      { typ = "vector<" + e2name + "*>*"; }
      else 
      { typ = "set<" + e2name + "*>*"; }
    } 
    String argtyp1 = "set<" + ename + "*>*"; 
    String argtyp2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  void " + ename + "::subtractAll" + role2;
    res = res + "(" + argtyp1 + " " + es + ", " + qualt + typ + " _val)\n";

    String declaration = "  static void subtractAll" + role2;
    declaration = declaration + "(" + argtyp1 + " " + es + "," + qualt + typ + " _val);\n";

    res = res + "  { set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->subtract" + role2 + "(" + ex + ", " + qual + " _val); } }\n\n";
    res = res + "  void " + ename + "::subtractAll" + role2;
    res = res + "(" + argtyp2 + " " + es + "," + qualt + typ + " _val)\n";
    declaration = declaration + "  static void subtractAll" + role2;
    declaration = declaration + "(" + argtyp2 + " " + es + ", " + qualt + typ + " _val);\n";


    res = res + "  { vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      Controller::inst->subtract" + role2 + "(" + ex + "," + qual + " _val); } }\n\n";

    declarations.add(declaration); 
    return res;
  }

  public String getOperation()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "public " + e2name + " get" +
             role2 + "(String _ind) { return (" + e2name + ") " + 
                                        role2 + ".get(_ind); }\n";
      } 
      else 
      { return "public List get" +
             role2 + "(String _ind) { return (List) " + role2 + ".get(_ind); }\n";
      } 
    }
    else if (card2 == ONE)
    { return "public " + e2name + " get" +
             role2 + "() { return " + role2 + "; }";
    }
    return "public List get" +
           role2 + "() { return (Vector) ((Vector) " + role2 + ").clone(); }";
  } 

  public String getOperationJava6()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "public " + e2name + " get" +
             role2 + "(String _ind) { return (" + e2name + ") " + 
                                        role2 + ".get(_ind); }\n";
      } 
      else if (ordered)
      { return "public ArrayList get" +
             role2 + "(String _ind) { return (ArrayList) " + role2 + ".get(_ind); }\n";
      } 
      else 
      { return "public HashSet get" +
             role2 + "(String _ind) { return (HashSet) " + role2 + ".get(_ind); }\n";
      } 
    }
    else if (card2 == ONE)
    { return "public " + e2name + " get" +
             role2 + "() { return " + role2 + "; }";
    }
    else if (ordered) 
    { return "public ArrayList get" +
           role2 + "() { return (ArrayList) ((ArrayList) " + role2 + ").clone(); }"; 
    } 
    else 
    { return "public HashSet get" +
           role2 + "() { return (HashSet) ((HashSet) " + role2 + ").clone(); }"; 
    } 
  } 

  public String getOperationJava7()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "public " + e2name + " get" +
             role2 + "(String _ind) { return (" + e2name + ") " + 
                                        role2 + ".get(_ind); }\n";
      } 
      else if (ordered)
      { return "public ArrayList<" + e2name + "> get" +
             role2 + "(String _ind) { return (ArrayList<" + e2name + ">) " + role2 + ".get(_ind); }\n";
      } 
      else if (sortedAsc) 
      { return "public TreeSet<" + e2name + "> get" +
             role2 + "(String _ind) { return (TreeSet<" + e2name + ">) " + role2 + ".get(_ind); }\n";
      } 
      else 
      { return "public HashSet<" + e2name + "> get" +
             role2 + "(String _ind) { return (HashSet<" + e2name + ">) " + role2 + ".get(_ind); }\n";
      } 
    }
    else if (card2 == ONE)
    { return "public " + e2name + " get" +
             role2 + "() { return " + role2 + "; }";
    }
    else if (ordered) 
    { return "public ArrayList<" + e2name + "> get" +
           role2 + "() { return (ArrayList<" + e2name + ">) ((ArrayList<" + e2name + ">) " + role2 + ").clone(); }"; 
    } 
    else if (sortedAsc)  
    { return "public TreeSet<" + e2name + "> get" +
           role2 + "() { return (TreeSet<" + e2name + ">) ((TreeSet<" + e2name + ">) " + role2 + ").clone(); }"; 
    } 
    else 
    { return "public HashSet<" + e2name + "> get" +
           role2 + "() { return (HashSet<" + e2name + ">) ((HashSet<" + e2name + ">) " + role2 + ").clone(); }"; 
    } 
  } 

  public String getOperationCSharp()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "public " + e2name + " get" +
             role2 + "(string _ind) { return (" + e2name + ") " + 
                                        role2 + "[_ind]; }\n";
      } 
      else 
      { return "public ArrayList get" +
             role2 + "(string _ind) { return (ArrayList) " + role2 + "[_ind]; }\n";
      } 
    }
    else if (card2 == ONE)
    { return "public " + e2name + " get" +
             role2 + "() { return " + role2 + "; }";
    }
    return "public ArrayList get" +
           role2 + "()\n" + 
           " { ArrayList res = new ArrayList();\n" + 
           "   res.AddRange(" + role2 + ");\n" + 
           "   return res; }";
  } 

  public String getOperationCPP()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + "*"; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "  " + e2name + " get" +
             role2 + "(string _ind) { return (*" + 
                                        role2 + ")[_ind]; }\n";
      } 
      else if (ordered) 
      { return "  vector<" + e2name + ">* get" +
             role2 + "(string _ind) { return (*" + role2 + ")[_ind]; }\n";
      }
      else 
      { return "  set<" + e2name + ">* get" +
             role2 + "(string _ind) { return (*" + role2 + ")[_ind]; }\n";
      } 
    }
    else if (card2 == ONE)
    { return "  " + e2name + " get" +
             role2 + "() { return " + role2 + "; }";
    }
    else if (ordered)
    { return "  vector<" + e2name + ">* get" +
           role2 + "() { return " + role2 + "; }";
    } 
    else 
    { return "  set<" + e2name + ">* get" +
           role2 + "() { return " + role2 + "; }";
    } 
  } // not cloned, but copied when set, instead. 

  public String getInterfaceOperation()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "  " + e2name + " get" +
             role2 + "(String _ind);\n";
      } 
      else 
      { return "  List get" +
             role2 + "(String _ind);\n";
      } 
    }
    else if (card2 == ONE)
    { return "  " + e2name + " get" +
             role2 + "();";
    }
    return "  List get" +
           role2 + "();";
  } 

  public String getInterfaceOperationJava6()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "  " + e2name + " get" + role2 + "(String _ind);\n"; } 
      else if (ordered) 
      { return "  ArrayList get" + role2 + "(String _ind);\n"; } 
      else 
      { return " HashSet get" + role2 + "(String _ind);\n"; }
    }
    else if (card2 == ONE)
    { return "  " + e2name + " get" + role2 + "();"; }
    else if (ordered) 
    { return "  ArrayList get" + role2 + "();"; } 
    else 
    { return "  HashSet get" + role2 + "();"; }
  } 

  public String getInterfaceOperationJava7()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "  " + e2name + " get" + role2 + "(String _ind);\n"; } 
      else if (ordered) 
      { return "  ArrayList<" + e2name + "> get" + role2 + "(String _ind);\n"; } 
      else if (sortedAsc)
      { return " TreeSet<" + e2name + "> get" + role2 + "(String _ind);\n"; }
      else 
      { return " HashSet<" + e2name + "> get" + role2 + "(String _ind);\n"; }
    }
    else if (card2 == ONE)
    { return "  " + e2name + " get" + role2 + "();"; }
    else if (ordered) 
    { return "  ArrayList<" + e2name + "> get" + role2 + "();"; } 
    else if (sortedAsc)
    { return "  TreeSet<" + e2name + "> get" + role2 + "();"; }
    else 
    { return "  HashSet<" + e2name + "> get" + role2 + "();"; }
  } 


  public String getInterfaceOperationCSharp()
  { if (role2 == null) { return null; }
    String e2name = entity2.getName() + ""; 

    if (qualifier != null) 
    { if (card2 == ONE) 
      { return "  " + e2name + " get" +
             role2 + "(string _ind);\n";
      } 
      else 
      { return "  ArrayList get" +
             role2 + "(string _ind);\n";
      } 
    }
    else if (card2 == ONE)
    { return "  " + e2name + " get" +
             role2 + "();";
    }
    return "  ArrayList get" +
           role2 + "();";
  } 

  public String getAllOperation(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is unordered
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String op;
    String body;
    if (card2 == ONE)
    { body = "if (result.contains(" + ex + ".get" + role2 + "())) {}\n      " + 
             "else { result.add(" + ex + ".get" + role2 + "()); }\n"; 
    }
    else 
    { body = "result = Set.union(result, " + ex + ".get" + role2 + "());"; }
    
    String es = ename.toLowerCase() + "s";
    String res = "public static List getAll" + role2;
    res = res + "(List " + es + ")\n";
    res = res + "  { List result = new Vector();\n";
    res = res + "    for (int _i = 0; _i < " + es + ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationJava6(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is unordered
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "_x";
    String op;
    String body;
    if (card2 == ONE)
    { body = "result.add(" + ex + ".get" + role2 + "());"; }
    else 
    { body = "result.addAll(" + ex + ".get" + role2 + "());"; }
    
    String es = ename.toLowerCase() + "_s";
    String res = "public static HashSet getAll" + role2;
    res = res + "(Collection " + es + ")\n";
    res = res + "  { HashSet result = new HashSet();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationJava7(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is unordered
    if (qualifier != null) { return ""; } 

    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "_x";
    String op;
    String body;
    if (card2 == ONE)
    { body = "result.add(" + ex + ".get" + role2 + "());"; }
    else 
    { body = "result.addAll(" + ex + ".get" + role2 + "());"; }
    
    String es = ename.toLowerCase() + "_s";
    String res = "public static HashSet<" + e2name + "> getAll" + role2;
    res = res + "(Collection<" + ename + "> " + es + ")\n";
    res = res + "  { HashSet<" + e2name + "> result = new HashSet<" + e2name + ">();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationCSharp(String ename)
  { // public static ArrayList getAllrole(ArrayList es)
    // { return list of e.role for e in es }  es is unordered
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String op;
    String body;
    if (card2 == ONE)
    { body = "if (result.Contains(" + ex + ".get" + role2 + "())) {}\n      " + 
             "else { result.Add(" + ex + ".get" + role2 + "()); }\n"; 
    }
    else 
    { body = "result = SystemTypes.union(result, " + ex + ".get" + role2 + "());"; }
    
    String es = ename.toLowerCase() + "_s";
    String res = "public static ArrayList getAll" + role2;
    res = res + "(ArrayList " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    res = res + "    for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOperationCPP(String ename)
  { // public static set<e2name*>* getAllrole(set<ename*>* es)
    // { return list of e.role for e in es }  es is unordered
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String op;
    String body1;
    String body2; 
    if (card2 == ONE)
    { body1 = "  result->insert(" + ex + "->get" + role2 + "());\n"; 
      body2 = "  result->push_back(" + ex + "->get" + role2 + "());\n"; 
    }
    else 
    { body1 = 
        "  result->insert(" + ex + "->get" + role2 + "()->begin(), " + ex + "->get" + role2 + "()->end());"; 
      body2 = 
        "  result->insert(result->end(), " + ex + "->get" + role2 + "()->begin(), " + ex + "->get" + role2 + "()->end());";
    }
    
    String e2name = entity2.getName(); 
    String returntype1 = "set<" + e2name + "*>*"; 
    String argtype1 = "set<" + ename + "*>*"; 
    String returntype2 = "vector<" + e2name + "*>*"; 
    String argtype2 = "vector<" + ename + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  static " + returntype1 + " getAll" + role2;
    res = res + "(" + argtype1 + " " + es + ")\n";
    res = res + "  { " + returntype1 + " result = new set<" + e2name + "*>();\n";
    res = res + "    set<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      " + body1 + " }\n";
    res = res + "    return result; }\n\n";
    if (isOrdered()) // result is a vector for vector arguments
    { res = res + 
                "  static " + returntype2 + " getAll" + role2; 
      res = res + "(" + argtype2 + " " + es + ")\n" + 
                "  { return getAllOrdered" + role2 + "(" + es + "); }\n\n"; 
    } // No - the result is always a set. 
    else 
    { res = res + "  static " + returntype1 + " getAll" + role2;
      res = res + "(" + argtype2 + " " + es + ")\n";
      res = res + "  { " + returntype1 + " result = new set<" + e2name + "*>();\n";
      res = res + "    vector<" + ename + "*>::iterator _pos;\n";
      res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
      res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
      res = res + "      " + body1 + " }\n";
      res = res + "    return result; }\n\n";
    } 
    return res;
  }

  public String getAllOrderedOperation(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is ordered 
    // Only needed if some association to E is ONE or ordered at E end
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String op;
    String body;
    if (ordered)
    { if (card2 == ONE)
      { op = "add"; }
      else
      { op = "addAll"; }
      body = "result." + op + "(" + ex + ".get" + role2 + "());";
    }
    else // No - the result is always a sequence. sq->collect(role2)->flatten()
    { if (card2 == ONE)
      { body = "if (result.contains(" + ex + ".get" + role2 + "())) {}\n      " + 
               "else { result.add(" + ex + ".get" + role2 + "()); }\n"; 
      }
      else 
      { body = "result = Set.union(result, " + ex + ".get" + role2 + "());"; }
    }
    String es = ename.toLowerCase() + "s";
    String res = "public static List getAllOrdered" + role2;
    res = res + "(List " + es + ")\n";
    res = res + "  { List result = new Vector();\n";
    res = res + "    for (int _i = 0; _i < " + es +
          ".size(); _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + ".get(_i);\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationJava6(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is ordered 
    // Only needed if some association to E is ONE or ordered at E end
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "_x";
    String op;
    String body;
    if (card2 == ONE)
    { op = "add"; }
    else
    { op = "addAll"; }
    body = "result." + op + "(" + ex + ".get" + role2 + "());";
    
    String es = ename.toLowerCase() + "_s";
    String res = "public static ArrayList getAllOrdered" + role2;
    res = res + "(Collection " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationJava7(String ename)
  { // public static List getAllrole(List es)
    // { return list of e.role for e in es }  es is ordered 
    // Only needed if some association to E is ONE or ordered at E end
    if (qualifier != null) { return ""; } 

    
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "_x";
    String op;
    String body;
    if (card2 == ONE)
    { op = "add"; }
    else
    { op = "addAll"; }
    body = "result." + op + "(" + ex + ".get" + role2 + "());";
    
    String es = ename.toLowerCase() + "_s";
    String res = "public static ArrayList<" + e2name + "> getAllOrdered" + role2;
    res = res + "(Collection<" + ename + "> " + es + ")\n";
    res = res + "  { ArrayList<" + e2name + "> result = new ArrayList<" + e2name + ">();\n";
    res = res + "    for (Object _o : " + es + ")\n";
    res = res + "    { " + ename + " " + ex + " = (" + ename + ") _o;\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationCSharp(String ename)
  { // public static ArrayList getAllrole(ArrayList es)
    // { return list of e.role for e in es }  es is ordered 
    // Only needed if some association to E is ONE or ordered at E end
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String op;
    String body;
    if (ordered)
    { if (card2 == ONE)
      { op = "Add"; }
      else
      { op = "AddRange"; }
      body = "result." + op + "(" + ex + ".get" + role2 + "());";
    }
    else 
    { if (card2 == ONE)
      { body = "if (result.Contains(" + ex + ".get" + role2 + "())) {}\n      " + 
               "else { result.Add(" + ex + ".get" + role2 + "()); }\n"; 
      }
      else 
      { body = "result = SystemTypes.union(result," + ex + ".get" + role2 + "());"; }
    }
    String es = ename.toLowerCase() + "_s";
    String res = "public static ArrayList getAllOrdered" + role2;
    res = res + "(ArrayList " + es + ")\n";
    res = res + "  { ArrayList result = new ArrayList();\n";
    res = res + "    for (int _i = 0; _i < " + es + ".Count; _i++)\n";
    res = res + "    { " + ename + " " + ex + " = (" +
          ename + ") " + es + "[_i];\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }

  public String getAllOrderedOperationCPP(String ename)
  { // static vector<e2name*>* getAllrole(vector<ename*>* es)
    // { return list of e.role for e in es }  es is ordered 
    // Only needed if some association to E is ONE or ordered at E end
    if (qualifier != null) { return ""; } 

    String ex = ename.toLowerCase() + "x";
    String val = ex + "->get" + role2 + "()"; 
    String op;
    String body;
    if (card2 == ONE)
    { op = "push_back(" + val + ")"; }
    else
    { op = "insert(result->end(), " + val + "->begin(), " + val + "->end())"; }
    body = "result->" + op + ";";

    String e2name = entity2.getName(); 
    String restype = "vector<" + e2name + "*>*"; 

    String es = ename.toLowerCase() + "s";
    String res = "  static " + restype + " getAllOrdered" + role2;
    res = res + "(vector<" + ename + "*>* " + es + ")\n";
    res = res + "  { " + restype + " result = new vector<" + e2name + "*>();\n";
    res = res + "    vector<" + ename + "*>::iterator _pos;\n";
    res = res + "    for (_pos = " + es + "->begin(); _pos != " + es + "->end(); ++_pos)\n";
    res = res + "    { " + ename + "* " + ex + " = *_pos;\n";
    res = res + "      " + body + " }\n";
    res = res + "    return result; }";
    return res;
  }



  public static String genEventCode(Vector rels,
                  java.util.Map env,
                  Expression cond, int index,
                  Expression succ, boolean local) 
  { if (rels.size() == 0)
    { return tab(index) + genEventCode(env,cond,succ,local); } 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateEventCode(rels,env,
                                   cond,index,succ,local);
    }
  }

  public static String genEventCodeJava6(Vector rels,
                  java.util.Map env,
                  Expression cond, int index,
                  Expression succ, boolean local) 
  { if (rels.size() == 0)
    { return tab(index) + genEventCodeJava6(env,cond,succ,local); } 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateEventCodeJava6(rels,env,
                                   cond,index,succ,local);
    }
  }

  public static String genEventCodeJava7(Vector rels,
                  java.util.Map env,
                  Expression cond, int index,
                  Expression succ, boolean local) 
  { if (rels.size() == 0)
    { return tab(index) + genEventCodeJava7(env,cond,succ,local); } 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateEventCodeJava7(rels,env,
                                   cond,index,succ,local);
    }
  }

  public static String genEventCodeCSharp(Vector rels,
                  java.util.Map env,
                  Expression cond, int index,
                  Expression succ, boolean local) 
  { if (rels.size() == 0)
    { return tab(index) + genEventCodeCSharp(env,cond,succ,local); } 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateEventCodeCSharp(rels,env,
                                   cond,index,succ,local);
    }
  }

  public static String genEventCodeCPP(Vector rels,
                  java.util.Map env,
                  Expression cond, int index,
                  Expression succ, boolean local) 
  { if (rels.size() == 0)
    { return tab(index) + genEventCodeCPP(env,cond,succ,local); } 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateEventCodeCPP(rels,env,
                                   cond,index,succ,local);
    }
  }  // needs to be implemented. 

  public String generateEventCode(Vector rels, 
                  java.util.Map env, 
                  Expression cond, int index,
                  Expression succ,boolean local) 
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String res = "";
    String var1;
    String var2;
    String lvar = "i_" + index;
      
    var1 = (String) env.get(e1name);
    var2 = (String) env.get(e2name);
    // System.out.println(var1 + " ++++ " + var2); 
    if (var1 != null && var2 != null)
    { if (card2 != ONE)
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().contains(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCode(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      else  
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().equals(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCode(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      return res; 
    }
    else if (var1 != null) // Entity1 already seen
    { var2 = e2name.toLowerCase() + "x";
      if (card2 != ONE) 
      { res = tab(index) + "for (int " + lvar + " = 0; " +
              lvar + " < " + var1 + ".get" +
              role2 + "().size(); " +
              lvar + "++)\n" + 
              tab(index) + 
              "{ " + e2name + " " + var2 + " = (" +
              e2name + ") " + var1 + ".get" +
              role2 + "().get(" + lvar + ");\n"; 
        env.put(e2name,var2);
        String text = genEventCode(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      else 
      { res = tab(index) + e2name + " " + var2 + " = " +
              var1 + ".get" + role2 + "();\n";
        env.put(e2name,var2);
        String text = genEventCode(rels,env, 
                            cond,index,succ,local);
        return res + text;
      }
    }
    else if (var2 != null) // Entity2 already seen
    { String op;
      if (card2 != ONE) 
      { op = "contains"; }
      else 
      { op = "equals"; }
      var1 = e1name.toLowerCase() + "x";

      res = tab(index) + "for (int " + lvar + " = 0; " + lvar +
            " < " + e1s + ".size(); " + lvar + 
            "++) \n" + tab(index) + 
            "{ " + e1name + " " + var1 + " = (" +
            e1name + ") " + e1s + ".get(" + lvar +
            "); \n" +
            tab(index + 1) + 
            "if (" + var1 + ".get" + role2 +
            "() != null && " + var1 + ".get" + 
            role2 + "()." + op + "(" + var2 + 
            ")) \n" + 
            tab(index + 1) + 
            "{ \n";   
      env.put(e1name,var1);
      String text = genEventCode(rels,env, 
                              cond,index+2,succ,local); 
      return res + text + "\n" + tab(index+1) + "}\n" + 
             tab(index) + "}";
    }
    else // var1 == null && var2 == null
    { var1 = e1name.toLowerCase() + "x";
      res = tab(index) + "for (int " + lvar + " = 0; " + lvar +
            " < " + e1s + ".size(); " + lvar + 
            "++) \n" + 
            tab(index) + 
            "{ " + e1name + " " + var1 + " = (" +
            e1name + ") " + e1s + ".get(" + lvar +
            "); \n";
      env.put(e1name,var1);
      String text = genEventCode(rels,env, 
                              cond,index+1,succ,local);
      return res + text + "\n" + tab(index) + "}";
    }
  }

  public String generateEventCodeJava6(Vector rels, 
                  java.util.Map env, 
                  Expression cond, int index,
                  Expression succ,boolean local) 
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String res = "";
    String var1;
    String var2;
    String lvar = "i_" + index;
      
    var1 = (String) env.get(e1name);
    var2 = (String) env.get(e2name);
    // System.out.println(var1 + " ++++ " + var2); 
    if (var1 != null && var2 != null)
    { if (card2 != ONE)
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().contains(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeJava6(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      else  
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().equals(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeJava6(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      return res; 
    }
    else if (var1 != null) // Entity1 already seen
    { var2 = e2name.toLowerCase() + "x";
      if (card2 != ONE) 
      { res = tab(index) + "for (Object " + lvar + " : " + var1 + ".get" + role2 + "())\n" +
              tab(index) + 
              "{ " + e2name + " " + var2 + " = (" + e2name + ") " + lvar + ";\n"; 
        env.put(e2name,var2);
        String text = genEventCodeJava6(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      else 
      { res = tab(index) + e2name + " " + var2 + " = " +
              var1 + ".get" + role2 + "();\n";
        env.put(e2name,var2);
        String text = genEventCodeJava6(rels,env, 
                            cond,index,succ,local);
        return res + text;
      }
    }
    else if (var2 != null) // Entity2 already seen
    { String op;
      if (card2 != ONE) 
      { op = "contains"; }
      else 
      { op = "equals"; }
      var1 = e1name.toLowerCase() + "x";

      res = tab(index) + "for (Object " + lvar + " : " + e1s + ") \n" + tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n" +
            tab(index + 1) + 
            "if (" + var1 + ".get" + role2 +
            "() != null && " + var1 + ".get" + 
            role2 + "()." + op + "(" + var2 + 
            ")) \n" + 
            tab(index + 1) + 
            "{ \n";   
      env.put(e1name,var1);
      String text = genEventCodeJava6(rels,env, 
                              cond,index+2,succ,local); 
      return res + text + "\n" + tab(index+1) + "}\n" + 
             tab(index) + "}";
    }
    else // var1 == null && var2 == null
    { var1 = e1name.toLowerCase() + "x";
      res = tab(index) + "for (Object " + lvar + " : " + e1s + ") \n" + 
            tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n";
      env.put(e1name,var1);
      String text = genEventCodeJava6(rels,env, 
                              cond,index+1,succ,local);
      return res + text + "\n" + tab(index) + "}";
    }
  }

  public String generateEventCodeJava7(Vector rels, 
                  java.util.Map env, 
                  Expression cond, int index,
                  Expression succ,boolean local) 
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String res = "";
    String var1;
    String var2;
    String lvar = "i_" + index;
      
    var1 = (String) env.get(e1name);
    var2 = (String) env.get(e2name);
    // System.out.println(var1 + " ++++ " + var2); 
    if (var1 != null && var2 != null)
    { if (card2 != ONE)
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().contains(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeJava7(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      else  
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().equals(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeJava7(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      return res; 
    }
    else if (var1 != null) // Entity1 already seen
    { var2 = e2name.toLowerCase() + "x";
      if (card2 != ONE) 
      { res = tab(index) + "for (Object " + lvar + " : " + var1 + ".get" + role2 + "())\n" +
              tab(index) + 
              "{ " + e2name + " " + var2 + " = (" + e2name + ") " + lvar + ";\n"; 
        env.put(e2name,var2);
        String text = genEventCodeJava7(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      else 
      { res = tab(index) + e2name + " " + var2 + " = " +
              var1 + ".get" + role2 + "();\n";
        env.put(e2name,var2);
        String text = genEventCodeJava7(rels,env, 
                            cond,index,succ,local);
        return res + text;
      }
    }
    else if (var2 != null) // Entity2 already seen
    { String op;
      if (card2 != ONE) 
      { op = "contains"; }
      else 
      { op = "equals"; }
      var1 = e1name.toLowerCase() + "x";

      res = tab(index) + "for (Object " + lvar + " : " + e1s + ") \n" + tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n" +
            tab(index + 1) + 
            "if (" + var1 + ".get" + role2 +
            "() != null && " + var1 + ".get" + 
            role2 + "()." + op + "(" + var2 + 
            ")) \n" + 
            tab(index + 1) + 
            "{ \n";   
      env.put(e1name,var1);
      String text = genEventCodeJava7(rels,env, 
                              cond,index+2,succ,local); 
      return res + text + "\n" + tab(index+1) + "}\n" + 
             tab(index) + "}";
    }
    else // var1 == null && var2 == null
    { var1 = e1name.toLowerCase() + "x";
      res = tab(index) + "for (Object " + lvar + " : " + e1s + ") \n" + 
            tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n";
      env.put(e1name,var1);
      String text = genEventCodeJava7(rels,env, 
                              cond,index+1,succ,local);
      return res + text + "\n" + tab(index) + "}";
    }
  }

  public String generateEventCodeCSharp(Vector rels, 
                  java.util.Map env, 
                  Expression cond, int index,
                  Expression succ,boolean local) 
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String res = "";
    String var1;
    String var2;
    String lvar = "i" + index;
      
    var1 = (String) env.get(e1name);
    var2 = (String) env.get(e2name);
    // System.out.println(var1 + " ++++ " + var2); 
    if (var1 != null && var2 != null)
    { if (card2 != ONE)
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().Contains(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeCSharp(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      else  
      { res = tab(index) + "if (" + var1 + ".get" + role2 + "().Equals(" +
              var2 + "))\n" + tab(index) + "{ " + 
              genEventCodeCSharp(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      return res; 
    }
    else if (var1 != null) // Entity1 already seen
    { var2 = e2name.toLowerCase() + "x";
      if (card2 != ONE) 
      { res = tab(index) + "foreach (object " + lvar + " in " + var1 + ".get" + role2 + "())\n" +
              tab(index) + 
              "{ " + e2name + " " + var2 + " = (" + e2name + ") " + lvar + ";\n"; 
        env.put(e2name,var2);
        String text = genEventCodeCSharp(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      else 
      { res = tab(index) + e2name + " " + var2 + " = " +
              var1 + ".get" + role2 + "();\n";
        env.put(e2name,var2);
        String text = genEventCodeCSharp(rels,env, 
                            cond,index,succ,local);
        return res + text;
      }
    }
    else if (var2 != null) // Entity2 already seen
    { String op;
      if (card2 != ONE) 
      { op = "Contains"; }
      else 
      { op = "Equals"; }
      var1 = e1name.toLowerCase() + "x";

      res = tab(index) + "foreach (object " + lvar + " in " + e1s + ") \n" + tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n" +
            tab(index + 1) + 
            "if (" + var1 + ".get" + role2 +
            "() != null && " + var1 + ".get" + 
            role2 + "()." + op + "(" + var2 + 
            ")) \n" + 
            tab(index + 1) + 
            "{ \n";   
      env.put(e1name,var1);
      String text = genEventCodeCSharp(rels,env, 
                              cond,index+2,succ,local); 
      return res + text + "\n" + tab(index+1) + "}\n" + 
             tab(index) + "}";
    }
    else // var1 == null && var2 == null
    { var1 = e1name.toLowerCase() + "x";
      res = tab(index) + "foreach (object " + lvar + " in " + e1s + ") \n" + 
            tab(index) + 
            "{ " + e1name + " " + var1 + " = (" + e1name + ") " + lvar + "; \n";
      env.put(e1name,var1);
      String text = genEventCodeCSharp(rels,env, 
                              cond,index+1,succ,local);
      return res + text + "\n" + tab(index) + "}";
    }
  }

  public String generateEventCodeCPP(Vector rels, 
                  java.util.Map env, 
                  Expression cond, int index,
                  Expression succ,boolean local) 
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String res = "";
    String var1;
    String var2;
    String lvar = "i_" + index;
      
    var1 = (String) env.get(e1name);
    var2 = (String) env.get(e2name);
    // System.out.println(var1 + " ++++ " + var2); 
    if (var1 != null && var2 != null)
    { if (card2 != ONE)
      { res = tab(index) + "if (UmlRsdsLib<" + e2name + "*>::isIn(" + var2 + ", " + var1 + "->get" + role2 + "())\n" + tab(index) + "{ " + 
              genEventCodeCPP(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      else  
      { res = tab(index) + "if (" + var1 + "->get" + role2 + "() == " +
              var2 + ")\n" + tab(index) + "{ " + 
              genEventCodeCPP(rels,env, 
                          cond,index+1,succ,local) + "\n" + tab(index) + "}";
      }
      return res; 
    }
    else if (var1 != null) // Entity1 already seen
    { var2 = e2name.toLowerCase() + "x";
      if (card2 != ONE && ordered) 
      { res = tab(index) + "for (int " + lvar + " = 0; " +
              lvar + " < " + var1 + "->get" + role2 + "()->size(); " +
              lvar + "++)\n" + tab(index) + 
              "{ " + e2name + "* " + var2 + " = (" +
              e2name + "*) " + var1 + "->get" + role2 + "()->at(" + lvar + ");\n"; 
        env.put(e2name,var2);
        String text = genEventCodeCPP(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      if (card2 != ONE && !ordered) 
      { res = tab(index) + "for (set<" + e2name + "*>::iterator " + lvar + " = " + 
              var1 + "->get" + role2 + "()->begin(); " +
              lvar + " != " + var1 + "->get" + role2 + "()->end();" +
              lvar + "++)\n" + tab(index) + 
              "{ " + e2name + "* " + var2 + " = (" + e2name + "*) *" + lvar + ";\n"; 
        env.put(e2name,var2);
        String text = genEventCodeCPP(rels,env, 
                            cond,index+1,succ,local);
        return res + text + "\n" + tab(index) + "}";
      }
      else 
      { res = tab(index) + e2name + "* " + var2 + " = " +
              var1 + "->get" + role2 + "();\n";
        env.put(e2name,var2);
        String text = genEventCodeCPP(rels,env, 
                            cond,index,succ,local);
        return res + text;
      }
    }
    else if (var2 != null) // Entity2 already seen
    { String test;
      if (card2 != ONE) 
      { test = "UmlRsdsLib<" + e2name + "*>::isIn(" + var2 + ", " + var1 + "->get" + role2 + "())"; }
      else 
      { test = var2 + " == " + var1 + "->get" + role2 + "()"; }
      var1 = e1name.toLowerCase() + "x";

      res = tab(index) + "for (int " + lvar + " = 0; " + lvar +
            " < " + e1s + "->size(); " + lvar + 
            "++) \n" + tab(index) + 
            "{ " + e1name + "* " + var1 + " = (" +
            e1name + "*) " + e1s + "->at(" + lvar +
            "); \n" +
            tab(index + 1) + 
            "if (" + var1 + "->get" + role2 +
            "() != 0 && " + test + ")\n" + 
            tab(index + 1) + 
            "{ \n";   
      env.put(e1name,var1);
      String text = genEventCodeCPP(rels,env, 
                              cond,index+2,succ,local); 
      return res + text + "\n" + tab(index+1) + "}\n" + 
             tab(index) + "}";
    }
    else // var1 == null && var2 == null
    { var1 = e1name.toLowerCase() + "x";
      res = tab(index) + "for (int " + lvar + " = 0; " + lvar +
            " < " + e1s + "->size(); " + lvar + 
            "++) \n" + 
            tab(index) + 
            "{ " + e1name + "* " + var1 + " = (" +
            e1name + "*) " + e1s + "->at(" + lvar +
            "); \n";
      env.put(e1name,var1);
      String text = genEventCodeCPP(rels,env, 
                              cond,index+1,succ,local);
      return res + text + "\n" + tab(index) + "}";
    }
  }

  public static String genEventCode(java.util.Map env,
                  Expression cond, Expression succ, 
                  boolean local)
  { String test = ""; 
    if (cond != null) 
    { String qf = cond.queryForm(env,local); 
      if (qf.equals("true")) { } 
      else 
      { test = "if (" + qf + ") { "; }
    }
    String code = ""; 
    code = succ.updateForm(env,local); 
    
    if (test.equals("")) 
    { return code; } 
    return test + code + " }"; 
  }

  public static String genEventCodeJava6(java.util.Map env,
                  Expression cond, Expression succ, 
                  boolean local)
  { String test = ""; 
    if (cond != null) 
    { String qf = cond.queryFormJava6(env,local); 
      if (qf.equals("true")) { } 
      else 
      { test = "if (" + qf + ") { "; }
    }
    String code = ""; 
    code = succ.updateFormJava6(env,local); 
    
    if (test.equals("")) 
    { return code; } 
    return test + code + " }"; 
  }

  public static String genEventCodeJava7(java.util.Map env,
                  Expression cond, Expression succ, 
                  boolean local)
  { String test = ""; 
    if (cond != null) 
    { String qf = cond.queryFormJava7(env,local); 
      if (qf.equals("true")) { } 
      else 
      { test = "if (" + qf + ") { "; }
    }
    String code = ""; 
    code = succ.updateFormJava7(env,local); 
    
    if (test.equals("")) 
    { return code; } 
    return test + code + " }"; 
  }

  public static String genEventCodeCSharp(java.util.Map env,
                  Expression cond, Expression succ, 
                  boolean local)
  { String test = ""; 
    if (cond != null) 
    { String qf = cond.queryFormCSharp(env,local); 
      if (qf.equals("true")) { } 
      else 
      { test = "if (" + qf + ") { "; }
    }
    String code = ""; 
    code = succ.updateFormCSharp(env,local); 
    
    if (test.equals("")) 
    { return code; } 
    return test + code + " }"; 
  }

  public static String genEventCodeCPP(java.util.Map env,
                  Expression cond, Expression succ, 
                  boolean local)
  { String test = ""; 
    if (cond != null) 
    { String qf = cond.queryFormCPP(env,local); 
      if (qf.equals("true")) { } 
      else 
      { test = "if (" + qf + ") { "; }
    }
    String code = ""; 
    code = succ.updateFormCPP(env,local); 
    
    if (test.equals("")) 
    { return code; } 
    return test + code + " }"; 
  }




  public static BStatement genBEventCode(Vector rels,
                  java.util.Map env,
                  Expression cond, Expression rhs,
                  boolean local)
  { if (rels.size() == 0)
    { return genBEventCode(env,cond,rhs,local); }
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateBEventCode(rels,env,cond,rhs,local);
    }
  }

  public BStatement generateBEventCode(Vector rels,
                  java.util.Map env,
                  Expression cond, Expression rhs, boolean local)
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    BExpression set1 = (BExpression) env.get(e1name);
    BExpression set2 = (BExpression) env.get(e2name);

    if (set1 != null && set2 != null) // also restrict to objs in this rel
    { return genBEventCode(rels,env,cond,rhs,local); }
    else if (set1 != null)
    { BExpression fapp = 
        new BApplySetExpression(role2,set1);
      fapp.setMultiplicity(card2); 
      if (card2 != ONE)
      { BExpression uni = new BUnaryExpression("union",fapp); 
        if (ordered)
        { env.put(e2name,new BUnaryExpression("ran",uni)); } 
        else 
        { env.put(e2name,uni); }  // all multiple
      }
      else 
      { env.put(e2name,fapp); }
      return genBEventCode(rels,env,cond,rhs,local);
    }
    else if (set2 != null)
    { String e1x = e1name.toLowerCase() + "x";
      BExpression e1var = new BBasicExpression(e1x);
      BExpression exp1 = 
          new BApplyExpression(role2,e1var);  
      exp1.setMultiplicity(card2); 
      BExpression pred;
      if (card2 != ONE) 
      { if (ordered)
        { exp1 = new BUnaryExpression("ran",exp1); } 
        if (set2 instanceof BSetExpression && 
            ((BSetExpression) set2).isSingleton())
        { // Just elem2 : exp1
          BExpression elem2 = ((BSetExpression) set2).getElement(0); 
          pred = new BBinaryExpression(":",elem2,exp1); 
        }
        else 
        { BExpression exp2 =
            new BBinaryExpression("/\\",exp1,set2);
          BExpression exp3 = 
            new BSetExpression(new Vector());
          pred = new BBinaryExpression("/=",exp2,exp3);
        }
      }
      else 
      { pred = new BBinaryExpression(":",exp1,set2); }
      env.put(e1name,
        new BSetComprehension(e1x,e1s,pred));
      return genBEventCode(rels,env,cond,rhs,local);
    }
    else // both set1 and set2 null
    { set1 = new BBasicExpression(e1s);
      set1.setMultiplicity(MANY); 
      env.put(e1name,set1); 
      return genBEventCode(rels,env,cond,rhs,local);
    }

    // return new BBasicStatement("skip"); 
  }

  public static BStatement genBEventCode(java.util.Map env,
                         Expression cond,
                         Expression rhs,boolean local)
  { BExpression test;
    BStatement code = rhs.bupdateForm(env,local);
    if (cond != null)
    { test = cond.binvariantForm(env,false);  // bqueryForm as env has SETS
      System.out.println("*** Binv form of " + cond + " *env* " + local + 
                         " ** " + env +
                         " *is* " + test); 

      BExpression tsimp = test.simplify();
      if ("TRUE".equals("" + tsimp) || "true".equals("" + tsimp))
      { return code; } 
      else
      { BStatement ifs = new BIfStatement(tsimp,code,null);
        ifs.setWriteFrame(code.getWriteFrame()); 
        return ifs;
      }
    }
    return code; 
  }

  public static BExpression genBInvariantCode(Vector rels, Vector needed,
                  java.util.Map env,
                  Expression cond, Expression rhs)
  { if (rels.size() == 0)
    { return genBInvariantCode(env,cond,rhs); }
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateBInvariantCode(rels,needed,env,cond,rhs);
    }
  }

  public BExpression generateBInvariantCode(Vector rels, Vector needed,
                  java.util.Map env,
                  Expression cond, Expression rhs)
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String e2s = e2name.toLowerCase() + "s";
    BExpression e1be = new BBasicExpression(e1s);
    BExpression e2be = new BBasicExpression(e2s);
    BExpression var1 = (BExpression) env.get(e1name);
    BExpression var2 = (BExpression) env.get(e2name);

    System.out.println("BINVCODE: " + var1 + " " + var2 + " **** " + cond + " => "                            + rhs); 

    if (var1 != null && var2 != null)
    { BExpression role2be = 
          new BApplyExpression(role2,var1);
      BExpression ante; 
      BExpression arg = 
          genBInvariantCode(rels,needed,env,cond,rhs);

      if (card2 != ONE)
      { if (ordered)
        { role2be = new BUnaryExpression("ran",role2be); }
        ante = new BBinaryExpression("=>",
                   new BBinaryExpression(":",var2,role2be),arg);
      }
      else 
      { ante = new BBinaryExpression("=>",
                   new BBinaryExpression("=",var2,role2be),arg);
      }
      ante.setBrackets(true); 
      return ante; 
    }
    else if (var1 != null)
    { // if (needed.contains(entity2))
      // {
        String svar2 = e2name.toLowerCase() + "x";
        var2 = new BBasicExpression(svar2);
        env.put(e2name,var2);
        BExpression range = 
          new BBinaryExpression(":",var2,e2be);
        BExpression ante;
        BExpression role2be = 
          new BApplyExpression(role2,var1);
        if (card2 != ONE)
        { if (ordered)
          { role2be = new BUnaryExpression("ran",role2be); }
          ante = new BBinaryExpression("&",range,
                   new BBinaryExpression(":",var2,role2be));
        }
        else 
        { ante = new BBinaryExpression("&",range,
                   new BBinaryExpression("=",var2,role2be));
        }
        BExpression arg = 
          genBInvariantCode(rels,needed,env,cond,rhs);
        return new BQuantifierExpression("forall",svar2,
                 new BBinaryExpression("=>",ante,arg));
      // }
      // else 
      // { return genBInvariantCode(rels,needed,env,cond,rhs); }
    }
    else if (var2 != null)
    { if (needed.contains(entity1))
      { String svar1 = e1name.toLowerCase() + "x";
        var1 = new BBasicExpression(svar1);
        env.put(e1name,var1);
        BExpression role2be = 
            new BApplyExpression(role2,var1);
        BExpression range = 
          new BBinaryExpression(":",var1,e1be);
        BExpression ante;
        role2be.setMultiplicity(card2);
        if (card2 != ONE)
        { if (ordered)
          { role2be = new BUnaryExpression("ran",role2be); }
          BExpression exp2 =
            new BBinaryExpression(":",var2,role2be);
          ante = new BBinaryExpression("&",range,exp2);
        }
        else 
        { ante = new BBinaryExpression("&",range,
                   new BBinaryExpression("=",var2,role2be));
        }
        BExpression arg = 
          genBInvariantCode(rels,needed,env,cond,rhs);
        return new BQuantifierExpression("forall",svar1,
                 new BBinaryExpression("=>",ante,arg));
      }
      else 
      { return genBInvariantCode(rels,needed,env,cond,rhs); }  
    }   // both null -- generate var1, var2 with var1: e1be, etc
    else 
    { String svar1 = e1name.toLowerCase() + "x";
      var1 = new BBasicExpression(svar1);
      BExpression range = 
          new BBinaryExpression(":",var1,e1be);
      env.put(e1name,var1);
      String svar2 = e2name.toLowerCase() + "x";
      var2 = new BBasicExpression(svar2);
      BExpression range2 = 
          new BBinaryExpression(":",var2,e2be);
      env.put(e2name,var2);
      BExpression arg = 
        genBInvariantCode(rels,needed,env,cond,rhs);
      return new BQuantifierExpression("forall",svar1,
               new BBinaryExpression("=>",range,
                 new BQuantifierExpression("forall",svar2,
                   new BBinaryExpression("=>",range2,arg))));
    }
    // return new BBasicExpression("false /* can't construct code */"); 
  }

  public static BExpression genBInvariantCode(
                              java.util.Map env,
                              Expression cond,
                              Expression rhs)
  { BExpression test = null;
    if (cond != null)
    { test = cond.binvariantForm(env,false); } 
    BExpression succ = rhs.binvariantForm(env,false);
    if (cond == null || cond.equalsString("true")) 
    { return succ; }
    BExpression res = new BBinaryExpression("=>",test,succ);
    BExpression rsimp = res.simplify(); 
    rsimp.setBrackets(true); 
    return rsimp; 
  } // must have brackets

  public static BExpression genBUpdateSet(Vector rels,
           String target, java.util.Map env,
           Expression cond)
  { if (rels.size() == 0)
    { return genBUpdateSet(env,cond); }
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateBUpdateSet(rels,target,env,
                                    cond);
    }
  }

  public BExpression generateBUpdateSet(Vector rels,
                       String target, java.util.Map env, 
                       Expression cond)
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String e2s = e2name.toLowerCase() + "s";
    BExpression e1be = new BBasicExpression(e1s);
    BExpression e2be = new BBasicExpression(e2s);
    BExpression var1 = (BExpression) env.get(e1name);
    BExpression var2 = (BExpression) env.get(e2name);

    if (var1 != null && var2 != null)
    { return genBUpdateSet(rels,target,env,cond); } // and restrict to rel
    else if (var1 != null)
    { String svar2 = e2name.toLowerCase() + "x";
      var2 = new BBasicExpression(svar2);
      env.put(e2name,var2);
      BExpression range = 
        new BBinaryExpression(":",var2,e2be);
      BExpression ante;
      BExpression role2be = 
        new BApplyExpression(role2,var1);
      role2be.setMultiplicity(card2);
      if (card2 != ONE)
      { ante = new BBinaryExpression("&",range,
                 new BBinaryExpression(":",var2,role2be));
      }
      else 
      { ante = new BBinaryExpression("&",range,
                 new BBinaryExpression("=",var2,role2be));
      }
      BExpression arg = 
        genBUpdateSet(rels,target,env,cond);
      if (target.equals(e2name))
      { return new BBinaryExpression("&",ante,arg); } 
      else 
      { return new BQuantifierExpression("exists",svar2,
                 new BBinaryExpression("&",ante,arg));
      } // and put unquantified version in newenv for
    }   // entity2. Add quantifiers for other non-src Es
    else if (var2 != null)
    { String svar1 = e1name.toLowerCase() + "x";
      var1 = new BBasicExpression(svar1);
      env.put(e1name,var1);
      BExpression role2be = 
          new BApplyExpression(role2,var1);
      role2be.setMultiplicity(card2); 
      BExpression range = 
        new BBinaryExpression(":",var1,e1be);
      BExpression ante;
      if (card2 != ONE)
      { BExpression exp2 =
          new BBinaryExpression(":",var2,role2be);
        ante = new BBinaryExpression("&",range,exp2);
      }
      else 
      { ante = 
          new BBinaryExpression("&",range,
            new BBinaryExpression("=",var2,role2be));
      }
      BExpression arg = 
        genBUpdateSet(rels,target,env,cond);
      if (e1name.equals(target))
      { return new BBinaryExpression("&",ante,arg); }
      else 
      { return new BQuantifierExpression("exists",svar1,
                 new BBinaryExpression("&",ante,arg));
      }
    }
    else // both null
    { String svar1 = e1name.toLowerCase() + "x";
      var1 = new BBasicExpression(svar1);
      env.put(e1name,var1);
      BExpression arg = 
        genBUpdateSet(rels,target,env,cond);
      BExpression range = 
        new BBinaryExpression(":",var1,e1be);
      
      if (e1name.equals(target))
      { return new BBinaryExpression("&",range,arg); }
      else 
      { return new BQuantifierExpression("exists",svar1,
                 new BBinaryExpression("&",range,arg));
      }
    } 
    // return new BBasicExpression("false");  /* can't construct code */
  }

  public static BExpression genBUpdateSet(
                                java.util.Map env,
                                Expression cond)
  { BExpression test = null;
    if (cond != null)
    { test = cond.binvariantForm(env,false); } 
    return test.simplify(); 
  } 

  public static BExpression genBUpdateExp(Vector rels,
           String target, java.util.Map env,
           Expression cond, Expression ue, 
           BExpression tfeatbe)
  { if (rels.size() == 0)
    { return genBUpdateExp(env,cond,ue,tfeatbe); }
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateBUpdateExp(rels,target,env,
                                    cond,ue,tfeatbe);
    }
  }

  public BExpression generateBUpdateExp(Vector rels,
                       String target, java.util.Map env, 
                       Expression cond, Expression ue, 
                       BExpression tfeatbe)
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String e2s = e2name.toLowerCase() + "s";
    BExpression e1be = new BBasicExpression(e1s);
    BExpression e2be = new BBasicExpression(e2s);
    BExpression var1 = (BExpression) env.get(e1name);
    BExpression var2 = (BExpression) env.get(e2name);

    if (var1 != null && var2 != null)
    { return genBUpdateExp(rels,target,env,cond,ue,tfeatbe); }
    else if (var1 != null)
    { String svar2 = e2name.toLowerCase() + "x";
      var2 = new BBasicExpression(svar2);
      env.put(e2name,var2);
      BExpression range = 
        new BBinaryExpression(":",var2,e2be);
      BExpression ante;
      BExpression role2be = 
        new BApplyExpression(role2,var1);
      role2be.setMultiplicity(card2); 
      if (card2 != ONE)
      { ante = new BBinaryExpression("&",range,
                 new BBinaryExpression(":",var2,role2be));
      }
      else 
      { ante = new BBinaryExpression("&",range,
                 new BBinaryExpression("=",var2,role2be));
      }
      BExpression arg = 
        genBUpdateExp(rels,target,env,cond,ue,tfeatbe);
      if (target.equals(e2name))
      { return new BBinaryExpression("&",ante,arg); } 
      else 
      { return new BQuantifierExpression("exists",svar2,
                 new BBinaryExpression("&",ante,arg));
      } 
    }   
    else if (var2 != null)
    { String svar1 = e1name.toLowerCase() + "x";
      var1 = new BBasicExpression(svar1);
      env.put(e1name,var1);
      BExpression role2be = 
          new BApplyExpression(role2,var1);
      role2be.setMultiplicity(card2); 
      BExpression range = 
        new BBinaryExpression(":",var1,e1be);
      BExpression ante;
      if (card2 != ONE)
      { BExpression exp2 =
          new BBinaryExpression(":",var2,role2be);
        ante = new BBinaryExpression("&",range,exp2);
      }
      else 
      { ante = 
          new BBinaryExpression("&",range,
            new BBinaryExpression("=",var2,role2be));
      }
      BExpression arg = 
        genBUpdateExp(rels,target,env,cond,ue,tfeatbe);
      if (target.equals(e1name))
      { return new BBinaryExpression("&",ante,arg); } 
      else 
      { return new BQuantifierExpression("exists",svar1,
                 new BBinaryExpression("&",ante,arg));
      } 
    }
    else // both null
    { String svar1 = e1name.toLowerCase() + "x";
      var1 = new BBasicExpression(svar1);
      env.put(e1name,var1);
      BExpression range = 
        new BBinaryExpression(":",var1,e1be);
      BExpression arg = 
        genBUpdateExp(rels,target,env,cond,ue,tfeatbe);
      if (target.equals(e1name))
      { return new BBinaryExpression("&",range,arg); } 
      else 
      { return new BQuantifierExpression("exists",svar1,
                 new BBinaryExpression("&",range,arg));
      } 
    }
    // return new BBasicExpression("false");  /* can't construct code */
  }

  public static BExpression genBUpdateExp(
                                java.util.Map env,
                                Expression cond, Expression ue,
                                BExpression tfeatbe)
  { BExpression test = null;
    BExpression updateExp = ue.binvariantForm(env,false); 
    BExpression ups = updateExp.simplify(); 

    BExpression eqpred = 
      new BBinaryExpression("=",tfeatbe,BExpression.unSet(ups)); 
    if (cond != null)
    { test = cond.binvariantForm(env,false); 
      return (new BBinaryExpression("&",eqpred,test)).simplify(); 
    } 
    return eqpred; 
  } 
                          // simplify it


  public String saveData()
  { // saveData(getName() + ".srs");
    String res = ""; 
    for (int p = 0; p < stereotypes.size(); p++) 
    { res = res + stereotypes.get(p) + " "; }  
    return res; // getName() + ".srs";
  }

  public String saveAsUSEData()
  { // saveData(getName() + ".srs");
    String res = "association " + getName() + " between\n"; 
    res = res + "  " + entity1.getName() + " [" + convertCard(card1) + "] role "; 
    if (role1 != null) { res = res + role1; } 
    res = res + "\n"; 
    res = res + "  " + entity2.getName() + " [" + convertCard(card2) + "] role " + role2 + "\n" + 
          "end\n\n"; 
    return res;
  }

  public String saveAsZ3Data()
  { String res = "(declare-fun " + role2 + " (" + entity1.getName() + ")";
    if (card2 == ONE) 
    { res = res + " " + entity2.getName() + ")\n"; } 
    else 
    { res = res + " (List " + entity2.getName() + "))\n"; }  // check 
    return res;
  }

  public void saveModelData(PrintWriter out)
  { String nme = getName(); 
    // String entnme = getEntity() + ""; 
    // String cname = nme + "_" + entnme; 
    // String tname = type.getUMLName(); 

    out.println(nme + " : Association"); 
    out.println(nme + ".name = \"" + nme + "\""); 

    // out.println(cname + " : " + entnme + ".ownedAttribute"); 
    // out.println(cname + ".type = " + tname); 

    String end1 = nme + "_end1"; 
    String end2 = nme + "_end2";  
    
    if (role1 != null && role1.length() > 0)
    { out.println(end1 + " : Property"); 
      out.println(end1 + ".name = \"" + role1 + "\""); 
      out.println(end1 + " : " + entity2 + ".ownedAttribute"); 
      out.println(end1 + " : " + nme + ".memberEnd"); 
      if (card1 == ONE)
      { out.println(end1 + ".lower = 1");
        out.println(end1 + ".upper = 1"); 
        out.println(end1 + ".type = " + entity1); 
      } 
      else 
      { out.println(end1 + ".lower = 0"); 
        String tid = Identifier.nextIdentifier(""); 
        out.println("SetType" + tid + " : CollectionType"); 
        out.println("SetType" + tid + ".name = \"Set\""); 
        out.println("SetType" + tid + ".elementType = " + entity1); 
        out.println("SetType" + tid + ".typeId = \"" + tid + "\""); 
        out.println(end1 + ".type = SetType" + tid); 
        if (card1 == ZEROONE)
        { out.println(end1 + ".upper = 1"); } 
        else if (card1 == MANY) 
        { out.println(end1 + ".upper = -1"); }
      }  
    } 
    
    out.println(end2 + " : Property"); 
    out.println(end2 + ".name = \"" + role2 + "\""); 
    out.println(end2 + " : " + entity1 + ".ownedAttribute"); 
    out.println(end2 + " : " + nme + ".memberEnd"); 

    if (card2 == ONE)
    { out.println(end2 + ".lower = 1");
      out.println(end2 + ".upper = 1"); 
      out.println(end2 + ".type = " + entity2); 
    } 
    else 
    { out.println(end2 + ".lower = 0"); 
      String tid = Identifier.nextIdentifier(""); 
      String colType = "SetType" + tid; 
      String colRoot = "Set"; 
      if (ordered) 
      { colType = "SequenceType" + tid; 
        colRoot = "Sequence"; 
      } 
      out.println(colType + " : CollectionType"); 
      out.println(colType + ".name = \"" + colRoot + "\""); 
      out.println(colType + ".elementType = " + entity2); 
      out.println(colType + ".typeId = \"" + tid + "\""); 
      out.println(end2 + ".type = " + colType); 
    } 
    if (card2 == ZEROONE)
    { out.println(end2 + ".upper = 1"); } 
    else if (card2 == MANY)  
    { out.println(end2 + ".upper = -1"); } 

    if (aggregation)
    { out.println(nme + ".aggregation = true"); } 
    if (linkedClass != null) 
    { out.println(linkedClass.getName() + " : " + nme + ".linkedClass"); } 
    if (qualifier != null) 
    { String qname = qualifier.saveModelData(out); 
      out.println(qname + " : " + end2 + ".qualifier"); 
    } 
  } // no representation of qualifiers or sorted associations

  public void saveEMF(PrintWriter out)
  { String mult = "*"; 
    if (card2 == ZEROONE)
    { mult = "0..1"; } 
    else if (card2 == ONE)
    { mult = "1"; } 

    String qual = "ref";     
    if (aggregation)
    { qual = "val"; } 

    out.println("  " + qual + " " + entity2 + "[" + mult + "] " + role2 + ";");  
  } 

  public void saveKM3(PrintWriter out)
  { String mult = "[*]"; 
    if (card2 == ZEROONE)
    { mult = "[0-1]"; } 
    else if (card2 == ONE)
    { mult = ""; } 

    String ord = ""; 
    if (card2 != ONE && ordered)
    { ord = "ordered "; } 

    String agg = "";     
    if (aggregation)
    { agg = "container "; } 

    String opp = ""; 
    if (role1 != null && role1.length() > 0)
    { opp = " oppositeOf " + role1; } 

    out.println("    reference " + role2 + mult + " " + ord + agg + ": " + entity2 + opp + ";");  
  } 

  public void saveSimpleKM3(PrintWriter out)
  { 
    out.println(entity1 + " referenceTo " + entity2);  
  } 

  public String getKM3()
  { String mult = "[*]"; 
    if (card2 == ZEROONE)
    { mult = "[0-1]"; } 
    else if (card2 == ONE)
    { mult = ""; } 

    String ord = ""; 
    if (card2 != ONE && ordered)
    { ord = "ordered "; } 

    String agg = "";     
    if (aggregation)
    { agg = "container "; } 

    String opp = ""; 
    if (role1 != null && role1.length() > 0)
    { opp = " oppositeOf " + role1; } 

    return "    reference " + role2 + mult + " " + ord + agg + ": " + entity2 + opp + ";";  
  } 

  public void saveEcore(PrintWriter out)
  { String res = "  <eStructuralFeatures xsi:type=\"ecore:EReference\" "; 
    res = res + "name=\"" + role2 + "\""; 

    if (card2 == ONE) 
    { res = res + " lowerBound = \"1\""; } 
    else if (card2 == ZEROONE) 
    { res = res + " lowerBound = \"0\""; }
 
    String upperBound = "-1"; 
    if (card2 == ZEROONE || card2 == ONE)
    { upperBound = "1"; } 
    res = res + " upperBound=\"" + upperBound + "\""; 
    
    res = res + " eType=\"#//" + entity2 + "\""; 

    if (role1 != null && role1.length() > 0)
    { res = res + " eOpposite=\"#//" + entity2 + "/" + role1 + "\""; } 

    if (aggregation)
    { res = res + " containment=\"true\""; } 

    out.println(res + "/>");  
  } // ordering? 

  public String xmiSaveModel(String domain) 
  { String res = ""; 
    String e1name = entity1.getName(); 
    String e2name = entity2.getName(); 
    String ex = e1name.toLowerCase() + "x_"; 
    String e2s = e2name.toLowerCase() + "s"; 
    Vector allAttributes = new Vector(); 
    allAttributes.addAll(entity2.getAttributes()); 
    allAttributes.addAll(entity2.allInheritedAttributes()); 


    if (card2 == ONE)
    { res = res + "    out.print(\" <" + role2 + " xsi:type=\\\"" + domain + ":" + e2name + " \");\n"; 

      String e2x = ex + ".get" + role2 + "()"; 

      for (int j = 0; j < allAttributes.size(); j++)
      { Attribute att = (Attribute) allAttributes.get(j);
        if (att.isMultiple()) 
        { continue; } 
        String attname = att.getName();
        res = res + "    out.print(\" " + attname + "=\\\"\"" + e2x + ".get" + attname + "() + \"\\\");\n";
      }
      res = res +  "    out.println(\" />\");\n";   
    }
    else      
    { String r = e1name.toLowerCase() + "_" + role2; 
      res = 
         res + "    List " + r + " = " + ex + ".get" + role2 + "();\n" + 
               "    for (int _j = 0; _j < " + r + ".size(); _j++)\n" +
               "    { out.print(\" <" + role2 + " xsi:type=\\\"" + domain + ":" + e2name + " />\");\n" + 
               "    }\n";   
    }
    return res;  
  } 

  public void saveData(String fileName)
  { String[] atts = { entity1.getName(), 
                      entity2.getName(), 
                      ""+card1, ""+card2, 
                      role1, role2 }; 
    try
    { ObjectOutputStream out =
        new ObjectOutputStream(
          new FileOutputStream(fileName));
      out.writeObject(atts); 
      out.close();
      System.out.println("Written data");
    }
    catch (IOException e)
    { System.err.println("Error in writing " + getName()); }
  }

  public String getKillCode(String ex, String e1s)
  { // ex : Entity2, e1s = Entity1.getName().toLowerCase() + "s"
     
    String e1name = entity1.getName();
    String deletedE1 = "_1removed" + role2 + e1name;
    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 

    String e1x = e1name.toLowerCase() + "x";
    String qrange = e1s; 
    if (role1 != null && role1.length() > 0)
    { qrange = ex + ".get" + role1 + "()"; } // More efficient, if role1 exists

    String qrangename = "_1qrange" + role2 + e1name; 

    String res = 
      "    Vector " + qrangename + " = new Vector();\n";
    if (role1 != null && role1.length() > 0 && card1 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 
   
    res = res + 
      "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + 
      "    { " + e1name + " " + e1x + " = (" + e1name + ") " + qrangename + ".get(_i);\n";

    if (card2 == ONE && qualifier == null) // killE1 each e1x connected to ex
    { res = res +
        "      if (" + e1x + " != null && " + ex + ".equals(" + e1x + ".get" + role2 + "()))\n" + 
        "      { _1removed" + role2 + e1name + ".add(" + e1x + ");\n" + 
        "        " + e1x + ".set" + role2 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || isDerived() || addOnly) { } 
    else // remove ex from role2
    { if (qualifier == null)
      { res = res +
        "      if (" + e1x + " != null && " + e1x + ".get" + role2 + "().contains(" + ex + "))\n" + 
        "      { remove" + role2 + "(" + e1x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "      remove" + role2 + "(" + e1x + "," + ex + "); \n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getKillCodeJava6(String ex, String e1s)
  { // ex : Entity2, e1s = Entity1.getName().toLowerCase() + "s"
     
    String e1name = entity1.getName();
    String deletedE1 = "_1removed" + role2 + e1name;

    String e1x = e1name.toLowerCase() + "x";
    String qrange = e1s; 
    if (role1 != null && role1.length() > 0)
    { qrange = ex + ".get" + role1 + "()"; } // More efficient, if role1 exists

    String qrangename = "_1qrange" + role2 + e1name; 

    String res = 
      "    ArrayList " + qrangename + " = new ArrayList();\n";
    if (role1 != null && role1.length() > 0 && card1 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 
   
    res = res + 
      "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + 
      "    { " + e1name + " " + e1x + " = (" + e1name + ") " + qrangename + ".get(_i);\n";

    if (card2 == ONE && qualifier == null) // killE1 each e1x connected to ex
    { res = res +
        "      if (" + e1x + " != null && " + ex + ".equals(" + e1x + ".get" + role2 + "()))\n" + 
        "      { _1removed" + role2 + e1name + ".add(" + e1x + ");\n" + 
        "        " + e1x + ".set" + role2 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly || isDerived()) { } 
    else // remove ex from role2
    { if (qualifier == null)
      { res = res +
        "      if (" + e1x + ".get" + role2 + "().contains(" + ex + "))\n" + 
        "      { remove" + role2 + "(" + e1x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "      remove" + role2 + "(" + e1x + "," + ex + "); \n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getKillCodeJava7(String ex, String e1s)
  { // ex : Entity2, e1s = Entity1.getName().toLowerCase() + "s"
     
    String e1name = entity1.getName();
    String deletedE1 = "_1removed" + role2 + e1name;

    String e1x = e1name.toLowerCase() + "x";
    String qrange = e1s; 
    if (role1 != null && role1.length() > 0)
    { qrange = ex + ".get" + role1 + "()"; } // More efficient, if role1 exists

    String qrangename = "_1qrange" + role2 + e1name; 

    String res = 
      "    ArrayList<" + e1name + "> " + qrangename + " = new ArrayList<" + e1name + ">();\n";
    if (role1 != null && role1.length() > 0 && card1 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 
   
    res = res + 
      "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + 
      "    { " + e1name + " " + e1x + " = (" + e1name + ") " + qrangename + ".get(_i);\n";

    if (card2 == ONE && qualifier == null) // killE1 each e1x connected to ex
    { res = res +
        "      if (" + e1x + " != null && " + ex + ".equals(" + e1x + ".get" + role2 + "()))\n" + 
        "      { _1removed" + role2 + e1name + ".add(" + e1x + ");\n" + 
        "        " + e1x + ".set" + role2 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly || isDerived()) { } 
    else // remove ex from role2
    { if (qualifier == null)
      { res = res +
        "      if (" + e1x + ".get" + role2 + "().contains(" + ex + "))\n" + 
        "      { remove" + role2 + "(" + e1x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "      remove" + role2 + "(" + e1x + "," + ex + "); \n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getKillCodeCSharp(String ex, String e1s)
  { // ex : Entity2, e1s = Entity1.getName().toLowerCase() + "s"
     
    String e1name = entity1.getName();
    String deletedE1 = "_1removed" + role2 + e1name;

    String e1x = e1name.toLowerCase() + "x";
    String qrange = e1s; 
    if (role1 != null && role1.length() > 0)
    { qrange = ex + ".get" + role1 + "()"; } // More efficient, if role1 exists

    String qrangename = "_1qrange" + role2 + e1name; 

    String res = 
      "    ArrayList " + qrangename + " = new ArrayList();\n";
    if (role1 != null && role1.length() > 0 && card1 == ONE)
    { res = res + "    " + qrangename + ".Add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".AddRange(" + qrange + ");\n"; } 
   
    res = res + 
      "    for (int _i = 0; _i < " + qrangename + ".Count; _i++)\n";
    res = res + 
      "    { " + e1name + " " + e1x + " = (" + e1name + ") " + qrangename + "[_i];\n";

    if (card2 == ONE && qualifier == null) // killE1 each e1x connected to ex
    { res = res +
        "      if (" + e1x + " != null && " + ex + ".Equals(" + e1x + ".get" + role2 + "()))\n" + 
        "      { _1removed" + role2 + e1name + ".Add(" + e1x + ");\n" + 
        "        " + e1x + ".set" + role2 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role2
    { if (qualifier == null)
      { res = res +
        "      if (" + e1x + ".get" + role2 + "().Contains(" + ex + "))\n" + 
        "      { remove" + role2 + "(" + e1x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "      remove" + role2 + "(" + e1x + "," + ex + "); \n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getKillCodeCPP(String ex, String e1s)
  { // ex : Entity2, e1s = Entity1.getName().toLowerCase() + "s"
     
    String e1name = entity1.getName();
    String deletedE1 = "_1removed" + role2 + e1name;

    String e1x = e1name.toLowerCase() + "x";
    String qrange = e1s; 
    if (role1 != null && role1.length() > 0)
    { qrange = ex + "->get" + role1 + "()"; } // More efficient, if role1 exists

    String qrangename = "_1qrange" + role2 + e1name; 

    String res = 
      "    vector<" + e1name + "*> " + qrangename + ";\n";
    if (role1 != null && role1.length() > 0 && card1 == ONE)
    { res = res + "    " + qrangename + ".push_back(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".insert(" + qrangename + ".end(), " + 
                                                 qrange + "->begin(), " + 
                                                 qrange + "->end());\n";
    } 
   
    res = res + 
      "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + 
      "    { " + e1name + "* " + e1x + " = " + qrangename + "[_i];\n";

    if (card2 == ONE && qualifier == null) // killE1 each e1x connected to ex
    { res = res +
        "      if (" + e1x + " != 0 && " + ex + " == " + e1x + "->get" + role2 + "())\n" + 
        "      { " + deletedE1 + ".push_back(" + e1x + ");\n" + 
        "        " + e1x + "->set" + role2 + "(0);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role2
    { if (qualifier == null)
      { String finder = ""; 
        if (ordered)
        { finder = "find(" + e1x + "->get" + role2 + "()->begin(), " + 
                             e1x + "->get" + role2 + "()->end(), " + ex + ")";
        } 
        else
        { finder = e1x + "->get" + role2 + "()->find(" + ex + ")"; } 
 
        res = res +
        "      if (" + finder + " != " + 
                       e1x + "->get" + role2 + "()->end())\n" + 
        "      { remove" + role2 + "(" + e1x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "      remove" + role2 + "(" + e1x + "," + ex + "); \n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getDualKillCode(String ex, String e2s)
  { // ex : Entity1, role1 not null

    String e2name = entity2.getName();
    String deletedE2 = "_2removed" + role1 + e2name;
    // String e1name = entity1.getName(); 
    // String e1ref = e1name; 
    // if (entity1.isInterface())
    // { e1ref = e1name + "." + e1name + "Ops"; } 

    String e2x = e2name.toLowerCase() + "x";
    String qrange = e2s; 
    if (role2 != null && role2.length() > 0)
    { qrange = ex + ".get" + role2 + "()"; } // More efficient, if role2 exists

    String qrangename = "_2qrange" + role1 + e2name; 

    String res = 
      "    Vector " + qrangename + " = new Vector();\n";
    if (role2 != null && role2.length() > 0 && card2 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 

    res = res + "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + "    { " + e2name + " " + e2x + " = (" + e2name + ") " + 
                qrangename + ".get(_i);\n";
    if (card1 == ONE && qualifier == null) // killE2 each e2x connected to ex
    { res = res +
        "      if (" + e2x + " != null && " + ex + ".equals(" + e2x + ".get" + role1 + "()))\n" + 
        "      { _2removed" + role1 + e2name + ".add(" + e2x + ");\n" + 
        "        " + e2x + ".set" + role1 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role1
    { if (qualifier == null) 
      { res = res +
          "      if (" + e2x + " != null && " + e2x + ".get" + role1 + "().contains(" + ex + "))\n" +
          "      { " + "remove" + role1 + "(" + e2x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "remove" + role1 + "(" + e2x + "," + ex + ");\n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getDualKillCodeJava6(String ex, String e2s)
  { // ex : Entity1, role1 not null

    String e2name = entity2.getName();
    String deletedE2 = "_2removed" + role1 + e2name;

    String e2x = e2name.toLowerCase() + "x";
    String qrange = e2s; 
    if (role2 != null && role2.length() > 0)
    { qrange = ex + ".get" + role2 + "()"; } // More efficient, if role2 exists

    String qrangename = "_2qrange" + role1 + e2name; 

    String res = 
      "    ArrayList " + qrangename + " = new ArrayList();\n";
    if (role2 != null && role2.length() > 0 && card2 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 

    res = res + "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + "    { " + e2name + " " + e2x + " = (" + e2name + ") " + 
                qrangename + ".get(_i);\n";
    if (card1 == ONE && qualifier == null) // killE2 each e2x connected to ex
    { res = res +
        "      if (" + e2x + " != null && " + ex + ".equals(" + e2x + ".get" + role1 + "()))\n" + 
        "      { _2removed" + role1 + e2name + ".add(" + e2x + ");\n" + 
        "        " + e2x + ".set" + role1 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role1
    { if (qualifier == null) 
      { res = res +
          "      if (" + e2x + ".get" + role1 + "().contains(" + ex + "))\n" +
          "      { " + "remove" + role1 + "(" + e2x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "remove" + role1 + "(" + e2x + "," + ex + ");\n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getDualKillCodeJava7(String ex, String e2s)
  { // ex : Entity1, role1 not null

    String e2name = entity2.getName();
    String deletedE2 = "_2removed" + role1 + e2name;

    String e2x = e2name.toLowerCase() + "x";
    String qrange = e2s; 
    if (role2 != null && role2.length() > 0)
    { qrange = ex + ".get" + role2 + "()"; } // More efficient, if role2 exists

    String qrangename = "_2qrange" + role1 + e2name; 

    String res = 
      "    ArrayList<" + e2name + "> " + qrangename + " = new ArrayList<" + e2name + ">();\n";
    if (role2 != null && role2.length() > 0 && card2 == ONE)
    { res = res + "    " + qrangename + ".add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".addAll(" + qrange + ");\n"; } 

    res = res + "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + "    { " + e2name + " " + e2x + " = (" + e2name + ") " + 
                qrangename + ".get(_i);\n";
    if (card1 == ONE && qualifier == null) // killE2 each e2x connected to ex
    { res = res +
        "      if (" + e2x + " != null && " + ex + ".equals(" + e2x + ".get" + role1 + "()))\n" + 
        "      { _2removed" + role1 + e2name + ".add(" + e2x + ");\n" + 
        "        " + e2x + ".set" + role1 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role1
    { if (qualifier == null) 
      { res = res +
          "      if (" + e2x + ".get" + role1 + "().contains(" + ex + "))\n" +
          "      { " + "remove" + role1 + "(" + e2x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "remove" + role1 + "(" + e2x + "," + ex + ");\n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  } // qualifier is redundant as cannot be qualifier at role1 end. 

  public String getDualKillCodeCSharp(String ex, String e2s)
  { // ex : Entity1, role1 not null

    String e2name = entity2.getName();
    String deletedE2 = "_2removed" + role1 + e2name;

    String e2x = e2name.toLowerCase() + "x";
    String qrange = e2s; 
    if (role2 != null && role2.length() > 0)
    { qrange = ex + ".get" + role2 + "()"; } // More efficient, if role2 exists

    String qrangename = "_2qrange" + role1 + e2name; 

    String res = 
      "    ArrayList " + qrangename + " = new ArrayList();\n";
    if (role2 != null && role2.length() > 0 && card2 == ONE)
    { res = res + "    " + qrangename + ".Add(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".AddRange(" + qrange + ");\n"; } 

    res = res + "    for (int _i = 0; _i < " + qrangename + ".Count; _i++)\n";
    res = res + "    { " + e2name + " " + e2x + " = (" + e2name + ") " + 
                qrangename + "[_i];\n";
    if (card1 == ONE && qualifier == null) // killE2 each e2x connected to ex
    { res = res +
        "      if (" + e2x + " != null && " + ex + ".Equals(" + e2x + ".get" + role1 + "()))\n" + 
        "      { _2removed" + role1 + e2name + ".Add(" + e2x + ");\n" + 
        "        " + e2x + ".set" + role1 + "(null);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role1
    { if (qualifier == null) 
      { res = res +
          "      if (" + e2x + ".get" + role1 + "().Contains(" + ex + "))\n" +
          "      { " + "remove" + role1 + "(" + e2x + "," + ex + "); }\n";
      } 
      else 
      { res = res +
          "remove" + role1 + "(" + e2x + "," + ex + ");\n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }

  public String getDualKillCodeCPP(String ex, String e2s)
  { // ex : Entity1, role1 not null

    String e2name = entity2.getName();
    String deletedE2 = "_2removed" + role1 + e2name;

    String e2x = e2name.toLowerCase() + "x";
    String qrange = e2s; 
    if (role2 != null && role2.length() > 0)
    { qrange = ex + "->get" + role2 + "()"; } // More efficient, if role2 exists

    String qrangename = "_2qrange" + role1 + e2name; 

    String res = 
      "    vector<" + e2name + "*> " + qrangename + ";\n";
    if (role2 != null && role2.length() > 0 && card2 == ONE)
    { res = res + "    " + qrangename + ".push_back(" + qrange + ");\n"; } 
    else 
    { res = res + "    " + qrangename + ".insert(" + qrangename + ".end(), " + 
                                                 qrange + "->begin(), " + 
                                                 qrange + "->end());\n"; 
    } 

    res = res + "    for (int _i = 0; _i < " + qrangename + ".size(); _i++)\n";
    res = res + "    { " + e2name + "* " + e2x + " = " + qrangename + "[_i];\n";
    if (card1 == ONE && qualifier == null) // killE2 each e2x connected to ex
    { res = res +
        "      if (" + e2x + " != 0 && " + ex + " == " + e2x + "->get" + role1 + "())\n" + 
        "      { " + deletedE2 + ".push_back(" + e2x + ");\n" + 
        "        " + e2x + "->set" + role1 + "(0);\n" + 
        "      }\n";
    }
    else if (frozen || addOnly) { } 
    else // remove ex from role1
    { if (qualifier == null) 
      { res = res +
          "      if (" + e2x + "->get" + role1 + "()->find(" + ex + ") != " + 
                         e2x + "->get" + role1 + "()->end())\n" +
          "      { " + "remove" + role1 + "(" + e2x + "," + ex + "); }\n";
      }  // Assumes role1 not ordered. 
      else 
      { res = res +
          "remove" + role1 + "(" + e2x + "," + ex + ");\n";
      } 
    }
    // if (aggregation)
    // { res = "  removed" + role2 + entity2.getName() + ".addAll(" + 
    //         ex + ".get" + role2 + "());\n" + res; 
    // } 
    res = res + "    }\n";
    return res;
  }


  // For Controller: 
  // For 0..1 card associations need PRE for add: if (ex.getf().size() > 0) 
  //                                              { return; } 
  // NO -- replace the existing element. 

  public Vector addremOperationsCode(Vector cons,
                                     Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerOperations(ent); } 

    String nme = role2; 
    String opheader = "";
    String ename = ent.getName();
    String e1name = entity1.getName(); 
    String e1s = e1name.toLowerCase() + "s"; 
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String e2xx = e2name.toLowerCase() + "_xx";
    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 

    String attx = nme + "xx";
    String addtest = ""; 
    if (ordered) 
    { addtest = ""; } // duplicates allowed
    else 
    { addtest = "if (" + ex + ".get" + nme + 
             "().contains(" + attx + ")) { return; }\n  "; 
    } // put if (ex.getr().size() > 0) { return; } for card2 == ZEROONE?

    String reminverse = ""; 
    String reminverse1 = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { reminverse = "    " + attx + ".set" + role1 + "(null);\n"; 
        reminverse1 = "    " + e2xx + ".set" + role1 + "(null);\n";
      } // delete it
      else 
      { reminverse = "    " + attx + ".remove" + role1 + "(" + ex + ");\n"; 
        reminverse1 = "    " + e2xx + ".remove" + role1 + "(" + ex + ");\n";
      } 
    }

    if (card2 == ZEROONE) 
    { addtest = addtest + 
        "    if (" + ex + ".get" + nme + "().size() > 0)\n" +  
        "    { " + e2name + " " + e2xx + " = (" + e2name + ") " + ex + ".get" + nme + "().get(0);\n" +
        "      " + ex + ".remove" + nme + "(" + e2xx + ");\n  " + 
        reminverse1 +  
        "    }\n  "; 
    } 

   
    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar = new Attribute(attx,new Type(entity2),INTERNAL);
    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("add" + nme,v1,false,null);
    String addclearup = ""; 
    if (card1 == ONE || card1 == ZEROONE) // remove all existing exx |-> attx pairs
    { if (role1 != null && role1.length() > 0)
      { if (card1 == ONE) 
        { addclearup = 
            "  if (" + attx + ".get" + role1 + "() != null) { " + attx + ".get" + 
            role1 + "().remove" + role2 + "(" + attx + "); }\n";  
        } 
        else // Need special case for interface e1
        { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + attx + 
          ".get" + role1 + "()," + attx + ");\n";
        } 
      }
      else // Not for interface e1 
      { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + e1s + 
                "," + attx + ");\n";
      } 
    }  
    String addinverse = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { addinverse = "  " + attx + ".set" + role1 + "(" + ex + ");\n"; } 
      else // 0..1 or * at card1
      { addinverse = "  " + attx + ".add" + role1 + "(" + ex + ");\n"; } 
    }
    opheader = "  public void add" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " + addtest + addclearup + addinverse + 
             "    " + ex + ".add" + nme + "(" + attx + ");\n  ";

    if (linkedClass != null) 
    { String acName = linkedClass.getName(); 
      // String acx = acName.toLowerCase() + "__x"; 
      if (linkedClass.getLinkedAssociation() == this) // this = the original association
      { opheader = opheader + "   create" + acName + "(" + ex + "," + attx + ");\n"; } 
      else // this = the inverse association
      { opheader = opheader + "   create" + acName + "(" + attx + "," + ex + ");\n"; } 
    } 
    
    String genr = subsets(); 
    if (genr.equals("")) { } 
    else 
    { opheader = opheader + "   add" + genr + "(" + ex + "," + attx + ");\n"; } 
    // More generally, could be a list of these
    
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      // cc.typeCheck(types,entities,contexts);
        
      Constraint cnew = cc.matches("add",nme,ent,attx,event);

      System.out.println("Match of " + cc + " with add" + nme + " ==> " + cnew); 
 
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,cntxs);
        if (typed)
        { String update = cnew.globalUpdateOp(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
    }
    res.add(opheader + " }\n\n");

    if (!addOnly)
    {  

      BehaviouralFeature event2 =
        new BehaviouralFeature("remove" + nme,v1,false,null);
      opheader = "  public void remove" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " +
             ex + ".remove" + nme + "(" + attx + ");\n  " + reminverse;

      if (linkedClass != null) 
      { String acName = linkedClass.getName(); 
        String acx = acName.toLowerCase() + "__x"; 
        String acs = acName.toLowerCase() + "s"; 
        opheader = opheader + "   for (int _p = 0; _p < " + acs + ".size(); _p++)\n" + 
                   "   { " + acName + " " + acx + " = (" + acName + ") " + acs + ".get(_p);\n" + 
                   "     if (" + ex + " == " + acx + ".get" + e1name.toLowerCase() + "() && " + 
                                 attx + " == " + acx + ".get" + e2name.toLowerCase() + "())\n" + 
                   "     { kill" + acName + "(" + acx + "); }\n" + 
                   "   }\n"; 
      } 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,attx,event2);
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs);
          if (typed)
          { String update = cnew.globalUpdateOp(ent,false);
            opheader = opheader +
                       update + "\n";
          }
        }
      }
      res.add(opheader + "  }\n\n");
    }

    // And subtract, union: 
    String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", List " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.size(); _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " + 
             role2 + "x.get(_i);\n" + 
             "      add" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
    String subtractop = " public void subtract" + role2 + "(" + ename + " " + ex +
             ", List " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.size(); _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " +
             role2 + "x.get(_i);\n" +
             "      remove" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" +
             "     } } \n\n";

    res.add(unionop); 
    if (!addOnly)
    { res.add(subtractop); } 
    return res;  
  } 

  // For Controller: 
  // For 0..1 card associations need PRE for add: if (ex.getf().size() > 0) 
  //                                              { return; } 
  public Vector addremOperationsCodeJava6(Vector cons,
                                     Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerOperationsJava6(ent); } 

    String nme = role2; 
    String opheader = "";
    String ename = ent.getName();
    String e1name = entity1.getName(); 
    String e1s = e1name.toLowerCase() + "s"; 
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String e2xx = e2name.toLowerCase() + "_xx";

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 

    String attx = nme + "xx";
    String addtest = ""; 
    if (ordered) 
    { addtest = ""; } // duplicates allowed
    else 
    { addtest = "if (" + ex + ".get" + nme + 
             "().contains(" + attx + ")) { return; }\n  "; 
    } // put if (ex.getr().size() > 0) { return; } for card2 == ZEROONE?
    // not actually needed for Java6. 

    String reminverse = ""; 
    String reminverse1 = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { reminverse = "    " + attx + ".set" + role1 + "(null);\n"; 
        reminverse1 = "    " + e2xx + ".set" + role1 + "(null);\n";
      } // delete it
      else 
      { reminverse = "    " + attx + ".remove" + role1 + "(" + ex + ");\n"; 
        reminverse1 = "    " + e2xx + ".remove" + role1 + "(" + ex + ");\n";
      } 
    }

    if (card2 == ZEROONE) 
    { addtest = addtest + 
        "    if (" + ex + ".get" + nme + "().size() > 0)\n" +  
        "    { " + e2name + " " + e2xx + " = (" + e2name + ") Set.any(" + ex + ".get" + nme + "());\n" +
        "      " + ex + ".remove" + nme + "(" + e2xx + ");\n  " + 
        reminverse1 +  
        "    }\n  "; 
    } 

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar = new Attribute(attx,new Type(entity2),INTERNAL);
    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("add" + nme,v1,false,null);
    String addclearup = ""; 
    if (card1 == ONE || card1 == ZEROONE) // remove all existing exx |-> attx pairs
    { if (role1 != null && role1.length() > 0)
      { if (card1 == ONE) 
        { addclearup = 
            "  if (" + attx + ".get" + role1 + "() != null) { " + attx + ".get" + 
            role1 + "().remove" + role2 + "(" + attx + "); }\n";  
        } 
        else 
        { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + attx + 
          ".get" + role1 + "()," + attx + ");\n";
        } 
      }
      else 
      { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + e1s + 
                "," + attx + ");\n";
      } 
    }  
    String addinverse = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { addinverse = "  " + attx + ".set" + role1 + "(" + ex + ");\n"; } 
      else // 0..1 or * at card1
      { addinverse = "  " + attx + ".add" + role1 + "(" + ex + ");\n"; } 
    }
    opheader = "  public void add" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " + addtest + addclearup + addinverse + 
             "    " + ex + ".add" + nme + "(" + attx + ");\n  ";

    if (linkedClass != null) 
    { String acName = linkedClass.getName(); 
      // String acx = acName.toLowerCase() + "__x"; 
      if (linkedClass.getLinkedAssociation() == this) // this = the original association
      { opheader = opheader + "   create" + acName + "(" + ex + "," + attx + ");\n"; } 
      else // this = the inverse association
      { opheader = opheader + "   create" + acName + "(" + attx + "," + ex + ");\n"; } 
    } 
    
    String genr = subsets(); 
    if (genr.equals("")) { } 
    else 
    { opheader = opheader + "   add" + genr + "(" + ex + "," + attx + ");\n"; } 
    // More generally, could be a list of these
    
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      // cc.typeCheck(types,entities,contexts);
        
      Constraint cnew = cc.matches("add",nme,ent,attx,event);

      System.out.println("Match of " + cc + " with add" + nme + " ==> " + cnew); 
 
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,cntxs);
        if (typed)
        { String update = cnew.globalUpdateOpJava6(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
    }
    res.add(opheader + "  }\n\n");

    if (!addOnly)
    { BehaviouralFeature event2 =
        new BehaviouralFeature("remove" + nme,v1,false,null);
      opheader = "  public void remove" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " +
             ex + ".remove" + nme + "(" + attx + ");\n  " + reminverse;

      if (linkedClass != null) 
      { String acName = linkedClass.getName(); 
        String acx = acName.toLowerCase() + "__x"; 
        String acs = acName.toLowerCase() + "s"; 
        opheader = opheader + "   for (int _p = 0; _p < " + acs + ".size(); _p++)\n" + 
                   "   { " + acName + " " + acx + " = (" + acName + ") " + acs + ".get(_p);\n" + 
                   "     if (" + ex + " == " + acx + ".get" + e1name.toLowerCase() + "() && " + 
                                 attx + " == " + acx + ".get" + e2name.toLowerCase() + "())\n" + 
                   "     { kill" + acName + "(" + acx + "); }\n" + 
                   "   }\n"; 
      } 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,attx,event2);
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs);
          if (typed)
          { String update = cnew.globalUpdateOpJava6(ent,false);
            opheader = opheader +
                       update + "\n";
          }
        }
      }
      res.add(opheader + "  }\n\n");
    }

    // And subtract, union: 
    String ptype = "Collection"; 
    // if (ordered) 
    // { ptype = "ArrayList"; } 
    // else 
    // { ptype = "HashSet"; } 

    String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", " + ptype + " " + role2 + "x)\n" +
             "  { for (Object _o : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _o;\n" + 
             "      add" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
    String subtractop = " public void subtract" + role2 + "(" + ename + " " + ex +
             ", " + ptype + " " + role2 + "x)\n" +
             "  { for (Object _o : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _o;\n" +
             "      remove" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" +
             "     } } \n\n";

    res.add(unionop); 
    if (!addOnly)
    { res.add(subtractop); } 
    return res;  
  } 

  public Vector addremOperationsCodeJava7(Vector cons,
                                     Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerOperationsJava7(ent); } 

    String nme = role2; 
    String opheader = "";
    String ename = ent.getName();
    String e1name = entity1.getName(); 
    String e1s = e1name.toLowerCase() + "s"; 
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String e2xx = e2name.toLowerCase() + "_xx";
    String attx = nme + "xx";

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 

    String addtest = ""; 
    if (ordered) 
    { addtest = ""; } // duplicates allowed
    else 
    { addtest = "if (" + ex + ".get" + nme + 
             "().contains(" + attx + ")) { return; }\n  "; 
    } // put if (ex.getr().size() > 0) { return; } for card2 == ZEROONE?

    String reminverse = ""; 
    String reminverse1 = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { reminverse = "    " + attx + ".set" + role1 + "(null);\n"; 
        reminverse1 = "    " + e2xx + ".set" + role1 + "(null);\n";
      } // delete it
      else 
      { reminverse = "    " + attx + ".remove" + role1 + "(" + ex + ");\n"; 
        reminverse1 = "    " + e2xx + ".remove" + role1 + "(" + ex + ");\n";
      } 
    }

    if (card2 == ZEROONE) 
    { addtest = addtest + 
        "    if (" + ex + ".get" + nme + "().size() > 0)\n" +  
        "    { " + e2name + " " + e2xx + " = (" + e2name + ") Ocl.any(" + ex + ".get" + nme + "());\n" +
        "      " + ex + ".remove" + nme + "(" + e2xx + ");\n  " + 
        reminverse1 +  
        "    }\n  "; 
    } 

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar = new Attribute(attx,new Type(entity2),INTERNAL);
    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("add" + nme,v1,false,null);
    String addclearup = ""; 
    if (card1 == ONE || card1 == ZEROONE) // remove all existing exx |-> attx pairs
    { if (role1 != null && role1.length() > 0)
      { if (card1 == ONE) 
        { addclearup = 
            "  if (" + attx + ".get" + role1 + "() != null) { " + attx + ".get" + 
            role1 + "().remove" + role2 + "(" + attx + "); }\n";  
        } 
        else 
        { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + attx + 
          ".get" + role1 + "()," + attx + ");\n";
        } 
      }
      else 
      { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + e1s + 
                "," + attx + ");\n";
      } 
    }  
    String addinverse = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { addinverse = "  " + attx + ".set" + role1 + "(" + ex + ");\n"; } 
      else // 0..1 or * at card1
      { addinverse = "  " + attx + ".add" + role1 + "(" + ex + ");\n"; } 
    }
    opheader = "  public void add" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " + addtest + addclearup + addinverse + 
             "    " + ex + ".add" + nme + "(" + attx + ");\n  ";

    if (linkedClass != null) 
    { String acName = linkedClass.getName(); 
      // String acx = acName.toLowerCase() + "__x"; 
      if (linkedClass.getLinkedAssociation() == this) // this = the original association
      { opheader = opheader + "   create" + acName + "(" + ex + ", " + attx + ");\n"; } 
      else // this = the inverse association
      { opheader = opheader + "   create" + acName + "(" + attx + ", " + ex + ");\n"; } 
    } 
    
    String genr = subsets(); 
    if (genr.equals("")) { } 
    else 
    { opheader = opheader + "   add" + genr + "(" + ex + ", " + attx + ");\n"; } 
    // More generally, could be a list of these
    
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      // cc.typeCheck(types,entities,contexts);
        
      Constraint cnew = cc.matches("add",nme,ent,attx,event);

      System.out.println("Match of " + cc + " with add" + nme + " ==> " + cnew); 
 
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,cntxs);
        if (typed)
        { String update = cnew.globalUpdateOpJava7(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
    }
    res.add(opheader + "  }\n\n");

    if (!addOnly)
    { 
      BehaviouralFeature event2 =
        new BehaviouralFeature("remove" + nme,v1,false,null);
      opheader = "  public void remove" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " +
             ex + ".remove" + nme + "(" + attx + ");\n  " + reminverse;

      if (linkedClass != null) 
      { String acName = linkedClass.getName(); 
        String acx = acName.toLowerCase() + "__x"; 
        String acs = acName.toLowerCase() + "s"; 
        opheader = opheader + "   for (int _p = 0; _p < " + acs + ".size(); _p++)\n" + 
                   "   { " + acName + " " + acx + " = (" + acName + ") " + acs + ".get(_p);\n" + 
                   "     if (" + ex + " == " + acx + ".get" + e1name.toLowerCase() + "() && " + 
                                 attx + " == " + acx + ".get" + e2name.toLowerCase() + "())\n" + 
                   "     { kill" + acName + "(" + acx + "); }\n" + 
                   "   }\n"; 
      } 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,attx,event2);
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs);
          if (typed)
          { String update = cnew.globalUpdateOpJava7(ent,false);
            opheader = opheader +
                       update + "\n";
          }
        }
      }
      res.add(opheader + "  }\n\n");
    }

    // And subtract, union: 
    String ptype = "Collection<" + e2name + ">"; 
    // if (ordered) 
    // { ptype = "ArrayList"; } 
    // else 
    // { ptype = "HashSet"; } 

    String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", " + ptype + " " + role2 + "x)\n" +
             "  { for (Object _o : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _o;\n" + 
             "      add" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
    String subtractop = " public void subtract" + role2 + "(" + ename + " " + ex +
             ", " + ptype + " " + role2 + "x)\n" +
             "  { for (Object _o : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _o;\n" +
             "      remove" + role2 + "(" + ex + ", " + e2x + role2 + ");\n" +
             "     } } \n\n";

    res.add(unionop); 
    if (!addOnly)
    { res.add(subtractop); } 
    return res;  
  } 

  public Vector addremOperationsCodeCSharp(Vector cons,
                                     Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerOperationsCSharp(ent); } 

    String nme = role2; 
    String opheader = "";
    String ename = ent.getName();
    String e1name = entity1.getName(); 
    String e1s = e1name.toLowerCase() + "_s"; 
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "Ops"; } 


    String addtest = ""; 
    if (ordered) 
    { addtest = ""; } // duplicates allowed
    else 
    { addtest = "if (" + ex + ".get" + nme + 
             "().Contains(" + attx + ")) { return; }\n  "; 
    } // put if (ex.getr().size() > 0) { return; } for card2 == ZEROONE?
    if (card2 == ZEROONE) 
    { addtest = addtest + "  if (" + ex + ".get" + nme + "().Count > 0) { return; }\n  "; } 
    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar = new Attribute(attx,new Type(entity2),INTERNAL);
    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("add" + nme,v1,false,null);
    String addclearup = ""; 
    if (card1 == ONE || card1 == ZEROONE) // remove all existing exx |-> attx pairs
    { if (role1 != null && role1.length() > 0)
      { if (card1 == ONE) 
        { addclearup = 
            "  if (" + attx + ".get" + role1 + "() != null) { " + attx + ".get" + 
            role1 + "().remove" + role2 + "(" + attx + "); }\n";  
        } 
        else 
        { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + attx + 
          ".get" + role1 + "()," + attx + ");\n";
        } 
      }
      else 
      { addclearup = "  " + e1ref + ".removeAll" + role2 + "(" + e1s + 
                "," + attx + ");\n";
      } 
    }  
    String addinverse = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { addinverse = "  " + attx + ".set" + role1 + "(" + ex + ");\n"; } 
      else // 0..1 or * at card1
      { addinverse = "  " + attx + ".add" + role1 + "(" + ex + ");\n"; } 
    }
    opheader = "  public void add" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " + addtest + addclearup + addinverse + 
             "    " + ex + ".add" + nme + "(" + attx + ");\n  ";
    if (linkedClass != null) 
    { String acName = linkedClass.getName(); 
      // String acx = acName.toLowerCase() + "__x"; 
      if (linkedClass.getLinkedAssociation() == this) // this = the original association
      { opheader = opheader + "   create" + acName + "(" + ex + "," + attx + ");\n"; } 
      else // this = the inverse association
      { opheader = opheader + "   create" + acName + "(" + attx + "," + ex + ");\n"; } 
    } 
    
    String genr = subsets(); 
    if (genr.equals("")) { } 
    else 
    { opheader = opheader + "   add" + genr + "(" + ex + "," + attx + ");\n"; } 
    // More generally, could be a list of these
    
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      // cc.typeCheck(types,entities,contexts);
        
      Constraint cnew = cc.matches("add",nme,ent,attx,event);

      System.out.println("Match of " + cc + " with add" + nme + " ==> " + cnew); 
 
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,cntxs);
        if (typed)
        { String update = cnew.globalUpdateOpCSharp(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
    }
    res.add(opheader + "  }\n\n");

    if (!addOnly)
    { String reminverse = ""; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == ONE)
        { reminverse = "  " + attx + ".set" + role1 + "(null);\n"; } // delete it
        else 
        { reminverse = "  " + attx + ".remove" + role1 + "(" + ex + ");\n"; } 
      } 

      BehaviouralFeature event2 =
        new BehaviouralFeature("remove" + nme,v1,false,null);
      opheader = "  public void remove" + nme + "(" + ename + " " +
             ex + ", " + e2name +
             " " + attx + ") \n  { " +
             ex + ".remove" + nme + "(" + attx + ");\n  " + reminverse;

      if (linkedClass != null) 
      { String acName = linkedClass.getName(); 
        String acx = acName.toLowerCase() + "__x"; 
        String acs = acName.toLowerCase() + "_s"; 
        opheader = opheader + "   for (int _p = 0; _p < " + acs + ".Count; _p++)\n" + 
                   "   { " + acName + " " + acx + " = (" + acName + ") " + acs + "[_p];\n" + 
                   "     if (" + ex + " == " + acx + ".get" + e1name.toLowerCase() + "() && " + 
                                 attx + " == " + acx + ".get" + e2name.toLowerCase() + "())\n" + 
                   "     { kill" + acName + "(" + acx + "); }\n" + 
                   "   }\n"; 
      } 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,attx,event2);
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs);
          if (typed)
          { String update = cnew.globalUpdateOpCSharp(ent,false);
            opheader = opheader +
                       update + "\n";
          }
        }
      }
      res.add(opheader + "  }\n\n");
    }

    // And subtract, union: 
    String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", ArrayList " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.Count; _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " + 
             role2 + "x[_i];\n" + 
             "      add" + role2 + "(" + ex + "," + e2x + role2 + ");\n" + 
             "     } } \n\n";   
    String subtractop = " public void subtract" + role2 + "(" + ename + " " + ex +
             ", ArrayList " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.Count; _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " +
             role2 + "x[_i];\n" +
             "      remove" + role2 + "(" + ex + "," + e2x + role2 + ");\n" +
             "     } } \n\n";

    res.add(unionop); 
    if (!addOnly)
    { res.add(subtractop); } 
    return res;  
  } 

  public Vector addremOperationsCodeCPP(Vector cons,
                                     Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerOperationsCPP(ent); } 

    String nme = role2; 
    String opheader = "";
    String ename = ent.getName();
    String e1name = entity1.getName(); 
    String e1s = e1name.toLowerCase() + "_s"; // Controller::inst->e1s
    String e2name = entity2.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    String addtest = ""; 
    if (ordered) 
    { addtest = ""; } // duplicates allowed
    else 
    { addtest = "if (" + ex + "->get" + nme + 
             "()->find(" + attx + ") != " + ex + "->get" + nme + "()->end()) { return; }\n  "; 
    } // put if (ex.getr().size() > 0) { return; } for card2 == ZEROONE?
    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar = new Attribute(attx,new Type(entity2),INTERNAL);
    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("add" + nme,v1,false,null);
    String addclearup = ""; 
    if (card1 == ONE || card1 == ZEROONE) // remove all existing exx |-> attx pairs
    { if (role1 != null && role1.length() > 0)
      { if (card1 == ONE) 
        { addclearup = 
            "  if (" + attx + "->get" + role1 + "() != 0) { " + attx + "->get" + 
            role1 + "()->remove" + role2 + "(" + attx + "); }\n";  
        } 
        else 
        { addclearup = "  " + e1name + "::removeAll" + role2 + "(" + attx + 
          "->get" + role1 + "()," + attx + ");\n";
        } 
      }
      else 
      { addclearup = "  " + e1name + "::removeAll" + role2 + "(" + e1s + 
                "," + attx + ");\n";
      } 
    }  
    String addinverse = ""; 
    if (role1 != null && role1.length() > 0)
    { if (card1 == ONE)
      { addinverse = "  " + attx + "->set" + role1 + "(" + ex + ");\n"; } 
      else // 0..1 or * at card1
      { addinverse = "  " + attx + "->add" + role1 + "(" + ex + ");\n"; } 
    }
    opheader = " void add" + nme + "(" + ename + "* " +
             ex + ", " + e2name +
             "* " + attx + ") \n  { " + addtest + addclearup + addinverse + 
             "    " + ex + "->add" + nme + "(" + attx + ");\n  ";

    if (linkedClass != null) 
    { String acName = linkedClass.getName(); 
      // String acx = acName.toLowerCase() + "__x"; 
      if (linkedClass.getLinkedAssociation() == this) // this = the original association
      { opheader = opheader + "   create" + acName + "(" + ex + "," + attx + ");\n"; } 
      else // this = the inverse association
      { opheader = opheader + "   create" + acName + "(" + attx + "," + ex + ");\n"; } 
    } 
    
    String genr = subsets(); 
    if (genr.equals("")) { } 
    else 
    { opheader = opheader + "   add" + genr + "(" + ex + "," + attx + ");\n"; } 
    // More generally, could be a list of these
    
    Vector contexts = new Vector(); 
    contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      // cc.typeCheck(types,entities,contexts);
        
      Constraint cnew = cc.matches("add",nme,ent,attx,event);

      // System.out.println("Match of " + cc + " with add" + nme + " ==> " + cnew); 
 
      // if (cc.matches(nme,val))
      if (cnew != null)
      { Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); } 
        boolean typed = cnew.typeCheck(types,entities,cntxs);
        if (typed)
        { String update = cnew.globalUpdateOpCPP(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
    }
    res.add(opheader + "  }\n\n");

    if (!addOnly)
    { String reminverse = ""; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == ONE)
        { reminverse = "  " + attx + "->set" + role1 + "(0);\n"; } // delete it
        else 
        { reminverse = "  " + attx + "->remove" + role1 + "(" + ex + ");\n"; } 
      } 

      BehaviouralFeature event2 =
        new BehaviouralFeature("remove" + nme,v1,false,null);
      opheader = "  void remove" + nme + "(" + ename + "* " +
             ex + ", " + e2name +
             "* " + attx + ") \n  { " +
             ex + "->remove" + nme + "(" + attx + ");\n  " + reminverse;

      if (linkedClass != null) 
      { String acName = linkedClass.getName(); 
        String acx = acName.toLowerCase() + "__x"; 
        String acs = acName.toLowerCase() + "_s"; 
        opheader = opheader + "   for (int _p = 0; _p < " + acs + "->size(); _p++)\n" + 
                   "   { " + acName + "* " + acx + " = (*" + acs + ")[_p];\n" + 
                   "     if (" + ex + " == " + acx + "->get" + e1name.toLowerCase() + "() && " + 
                                 attx + " == " + acx + "->get" + e2name.toLowerCase() + "())\n" + 
                   "     { kill" + acName + "(" + acx + "); }\n" + 
                   "   }\n"; 
      } 

      for (int j = 0; j < cons.size(); j++)
      { Constraint cc = (Constraint) cons.get(j);
        Constraint cnew = cc.matches("remove",nme,ent,attx,event2);
        if (cnew != null)
        { Vector cntxs = new Vector(); 
          if (cnew.getOwner() != null) 
          { cntxs.add(cnew.getOwner()); } 
          boolean typed = cnew.typeCheck(types,entities,cntxs);
          if (typed)
          { String update = cnew.globalUpdateOpCPP(ent,false);
            opheader = opheader +
                       update + "\n";
          }
        }
      }
      res.add(opheader + "  }\n\n");
    }

    // And subtract, union: 
    String unionop; 
    String subtractop; 
    // if (ordered)
    { unionop = "  void union" + role2 + "(" + ename + "* " + ex +
             ", vector<" + e2name + "*>* " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x->size(); _i++)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = (*" + 
             role2 + "x)[_i];\n" + 
             "      add" + role2 + "(" + ex + "," + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      subtractop = "  void subtract" + role2 + "(" + ename + "* " + ex +
             ", vector<" + e2name + "*>* " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x->size(); _i++)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = (*" +
             role2 + "x)[_i];\n" +
             "      remove" + role2 + "(" + ex + "," + e2x + role2 + ");\n" +
             "     } } \n\n";
    } 
    // else 
    { unionop = unionop + 
             "  void union" + role2 + "(" + ename + "* " + ex +
             ", set<" + e2name + "*>* " + role2 + "x)\n" +
             "  { set<" + e2name + "*>::iterator _pos; \n" + 
             "    for (_pos = " + role2 + "x->begin() ; _pos != " + role2 + "x->end(); ++_pos)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = *_pos;\n" + 
             "      add" + role2 + "(" + ex + "," + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      subtractop = subtractop + 
             "  void subtract" + role2 + "(" + ename + "* " + ex +
             ", set<" + e2name + "*>* " + role2 + "x)\n" +
             "  { set<" + e2name + "*>::iterator _pos; \n" + 
             "    for (_pos = " + role2 + "x->begin() ; _pos != " + role2 + "x->end(); ++_pos)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = *_pos;\n" + 
             "      remove" + role2 + "(" + ex + "," + e2x + role2 + ");\n" + 
             "     } } \n\n";
    } 

    res.add(unionop); 
    if (!addOnly)
    { res.add(subtractop); } 
    return res;  
  } 

  public Vector qualifierControllerOperations(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String e2x = e2name.toLowerCase() + "xx"; 
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
      return res; 
    }
    else
    { /* res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, List " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); */ 
      res.add(" public void add" + role2 + "(" + edec + 
                   ", String _ind, " + e2name + " " + val + ")\n  " + 
              " { " + ex + ".add" + role2 + "(_ind," + val + "); }\n");
      String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", String _ind, List " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.size(); _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " + 
             role2 + "x.get(_i);\n" + 
             "      add" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      res.add(unionop); 
      if (addOnly)
      { return res; } 
      else 
      { res.add(" public void remove" + role2 + "(" + edec + 
                  ", String _ind, " + e2name + " " + val + ")\n  " +
                " { " + ex + ".remove" + role2 + "(_ind," + val + "); }\n"); 
        res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
        String subtractop = 
             " public void subtract" + role2 + "(" + ename + " " + ex +
             ", String _ind, List " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.size(); _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " +
             role2 + "x.get(_i);\n" +
             "      remove" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" +
             "     } } \n\n";

        res.add(subtractop); 
      }   // and the more general remove without _ind. 
    }
    
    return res; 
  } 

  public Vector qualifierControllerOperationsJava6(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String e2x = e2name.toLowerCase() + "xx"; 
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
      return res; 
    }
    else
    { /* res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, Collection " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); */ 
      res.add(" public void add" + role2 + "(" + edec + 
                   ", String _ind, " + e2name + " " + val + ")\n  " + 
              " { " + ex + ".add" + role2 + "(_ind," + val + "); }\n");
      String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", String _ind, Collection " + role2 + "x)\n" +
             "  { for (Object _i : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _i;\n" + 
             "      add" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      res.add(unionop); 
      if (addOnly)
      { return res; } 
      else 
      { res.add(" public void remove" + role2 + "(" + edec + 
                  ", String _ind, " + e2name + " " + val + ")\n  " +
                " { " + ex + ".remove" + role2 + "(_ind," + val + "); }\n"); 
        res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
        String subtractop = 
             " public void subtract" + role2 + "(" + ename + " " + ex +
             ", String _ind, Collection " + role2 + "x)\n" +
             "  { for (Object _i : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _i;\n" +
             "      remove" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" +
             "     } } \n\n";

        res.add(subtractop); 
      }   // and the more general remove without _ind. 
    }
    
    return res; 
  } 

  public Vector qualifierControllerOperationsJava7(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String e2x = e2name.toLowerCase() + "xx"; 
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
      return res; 
    }
    else
    { /* res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, Collection " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); */ 
      res.add(" public void add" + role2 + "(" + edec + 
                   ", String _ind, " + e2name + " " + val + ")\n  " + 
              " { " + ex + ".add" + role2 + "(_ind," + val + "); }\n");
      String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", String _ind, Collection<" + e2name + "> " + role2 + "x)\n" +
             "  { for (Object _i : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _i;\n" + 
             "      add" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      res.add(unionop); 
      if (addOnly)
      { return res; } 
      else 
      { res.add(" public void remove" + role2 + "(" + edec + 
                  ", String _ind, " + e2name + " " + val + ")\n  " +
                " { " + ex + ".remove" + role2 + "(_ind," + val + "); }\n"); 
        res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
        String subtractop = 
             " public void subtract" + role2 + "(" + ename + " " + ex +
             ", String _ind, Collection<" + e2name + "> " + role2 + "x)\n" +
             "  { for (Object _i : " + role2 + "x)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") _i;\n" +
             "      remove" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" +
             "     } } \n\n";

        res.add(subtractop); 
      }   // and the more general remove without _ind. 
    }
    
    return res; 
  } 

  public Vector qualifierControllerOperationsCSharp(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String e2x = e2name.toLowerCase() + "xx"; 
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", string _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
      return res; 
    }
    else
    { /* res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, ArrayList " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); */ 
      res.add(" public void add" + role2 + "(" + edec + 
                   ", string _ind, " + e2name + " " + val + ")\n  " + 
              " { " + ex + ".add" + role2 + "(_ind," + val + "); }\n");
      String unionop = " public void union" + role2 + "(" + ename + " " + ex +
             ", string _ind, ArrayList " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.Count; _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " + 
             role2 + "x[_i];\n" + 
             "      add" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      res.add(unionop); 
      if (addOnly)
      { return res; } 
      else 
      { res.add(" public void remove" + role2 + "(" + edec + 
                  ", string _ind, " + e2name + " " + val + ")\n  " +
                " { " + ex + ".remove" + role2 + "(_ind," + val + "); }\n"); 
        res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
        String subtractop = 
             " public void subtract" + role2 + "(" + ename + " " + ex +
             ", string _ind, ArrayList " + role2 + "x)\n" +
             "  { for (int _i = 0; _i < " + role2 + "x.Count; _i++)\n" +
             "    { " + e2name + " " + e2x + role2 + " = (" + e2name + ") " +
             role2 + "x[_i];\n" +
             "      remove" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" +
             "     } } \n\n";

        res.add(subtractop); 
      }   // and the more general remove without _ind. 
    }
    
    return res; 
  } 

  public Vector qualifierControllerOperationsCPP(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String e2x = e2name.toLowerCase() + "xx"; 
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + "* " + ex; 
    String val = "_" + role2 + "xx"; 
 
    String argtype0 = ""; 
    if (card2 == ONE) 
    { argtype0 = e2name; } 
    else if (ordered)  // actually need both, just in case. 
    { argtype0 = "vector<" + e2name + "*>"; } 
    else 
    { argtype0 = "set<" + e2name + "*>"; } 
    String argtype = argtype0 + "*"; 

    if (card2 == ONE) 
    { res.add(" void set" + role2 + "(" + edec +  
                   ", string _ind, " + e2name + "* " + val + ")\n" + 
              " { " + ex + "->set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" void remove" + role2 + "(" + edec + 
                  ", " + e2name + "* " + val + ")\n" + 
                " { " + ex + "->remove" + role2 + "(" + val + "); }\n"); 
      return res; 
    }
    else
    { /* res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, ArrayList " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); */ 
      res.add(" void add" + role2 + "(" + edec + 
                   ", string _ind, " + e2name + "* " + val + ")\n  " + 
              " { " + ex + "->add" + role2 + "(_ind," + val + "); }\n");
      String unionop = " void union" + role2 + "(" + edec +
             ", string _ind, " + argtype + " " + role2 + "x)\n" +
             "  { for (" + argtype0 + "::iterator _pos = " + role2 + "x->begin(); _pos != " + 
                       role2 + "x->end(); _pos++)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = *_pos;\n" + 
             "      add" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" + 
             "     } } \n\n";   
      res.add(unionop); 
      if (addOnly)
      { return res; } 
      else 
      { res.add(" void remove" + role2 + "(" + edec + 
                  ", string _ind, " + e2name + "* " + val + ")\n  " +
                " { " + ex + "->remove" + role2 + "(_ind," + val + "); }\n"); 
        res.add(" void remove" + role2 + "(" + edec + 
                  ", " + e2name + "* " + val + ")\n" + 
                " { " + ex + "->remove" + role2 + "(" + val + "); }\n"); 
        String subtractop = 
             "  void subtract" + role2 + "(" + edec +
             ", string _ind, " + argtype + " " + role2 + "x)\n" +
             "  { for (" + argtype0 + "::iterator _pos = " + role2 + "x->begin(); _pos != " + 
                       role2 + "x->end(); _pos++)\n" +
             "    { " + e2name + "* " + e2x + role2 + " = *_pos;\n" +
             "      remove" + role2 + "(" + ex + ", _ind, " + e2x + role2 + ");\n" +
             "     } } \n\n";

        res.add(subtractop); 
      }   // and the more general remove without _ind. 
    }
    
    return res; 
  } 


  // for the controller
  public Vector setOperationsCode(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    String nme = role2; 
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerSetOperation(ent); } 

    if (linkedClass != null && card2 != ONE) 
    { return associationClassControllerSetOperation(ent); } 

    String opheader = "";
    String ename = ent.getName();
    String e2name = entity2.getName(); 
    String e1name = entity1.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    BasicExpression attxbe = new BasicExpression(attx); 
    String oldrole2xx = "_old" + role2 + "xx";
    String e1s = (entity1 + "").toLowerCase() + "s"; 
    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 
        
    String unlink1 = "";     // Unlink ex.getrole2() - attx elements from ex
    String setinverse = "";  // set role1 if it exists
    String settest = "";      

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar;
    String t2; 
    String assocclassextra = ""; 

    if (card2 == ONE)
    { apar = new Attribute(attx,new Type(entity2),INTERNAL);
      settest = "  if (" + ex + ".get" + role2 + "() == " + attx + ") { return; }\n";

      if (linkedClass != null) 
      { String lcname = linkedClass.getName(); 
        String e1role = e1name.toLowerCase(); 
        String e2role = e2name.toLowerCase(); 
        String ls = lcname.toLowerCase() + "s"; 
        String lx = lcname.toLowerCase() + "__x"; 
        assocclassextra = "  for (int _l = 0; _l < " + ls + ".size(); _l++)\n" + 
                          "    { " + lcname + " " + lx + " = (" + lcname + ") " + ls + ".get(_l);\n" +
                          "      if (" + lx + ".get" + e1role + "() == " + ex + ")\n" + 
                          "      { " + lx + ".set" + e2role + "(" + attx + "); return; }\n" + 
                          "    }\n" +
                          "    create" + lcname + "(" + ex + ", " + attx + ");\n";  
      }
 
      t2 = e2name; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == MANY)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + "); } \n" + 
                       "    " + attx + ".add" + role1 + "(" + ex + ");\n";
        }
        else if (card1 == ZEROONE)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + ");\n" + 
                       "      for (int _p = 0; _p < " + attx + ".get" + role1 + "().size(); _p++)\n" + 
                       "      { " + e1name + " _p_xx = (" + e1name + ") " + attx + ".get" + 
                                      role1 + "().get(_p);\n" + 
                       "        _p_xx.set" + role2 + "(null);\n" + 
                       "      }\n" + 
                       "    }\n" + 
                       "    Vector _exvect = new Vector();\n" + 
                       "    _exvect.add(" + ex + ");\n" + 
                       "    " + attx + ".set" + role1 + "(_exvect);\n";
        }
        else // 1-1 association
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + ex + ".get" + role2 + "().set" + role1 + "(null); }\n" +
                       "    if (" + attx + ".get" + role1 + "() != null)\n" + 
                       "    { " + attx + ".get" + role1 + "().set" + role2 + "(null); }\n" + 
                       "    " + attx + ".set" + role1 + "(" + ex + ");\n"; 
        }   
      } // if role1 un-named, still need to do, for ZEROONE role1: 
        // for (e1xx : e1s) { if (e1xx.getrole2() == attx) { e1xx.setrole2(null); } } 
      else if (card1 == ZEROONE || card1 == ONE) 
      { setinverse = "    for (int _q = 0; _q < " + e1s + ".size(); _q++)\n" + 
                     "    { " + e1name + " _q_E1x = (" + e1name + ") " + e1s + ".get(_q);\n" + 
                     "      if (_q_E1x.get" + role2 + "() == " + attx + ")\n" + 
                     "      { _q_E1x.set" + role2 + "(null); }\n" + 
                     "    }\n"; 
      }   
    }
    else           // card2 == MANY or ZEROONE
    { Type partype; 
      if (ordered)
      { partype = new Type("Sequence",null);
        partype.setElementType(new Type(entity2));  
        apar = new Attribute(attx,partype,INTERNAL); 
        t2 = "List";
      }
      else 
      { partype = new Type("Set",null);
        partype.setElementType(new Type(entity2));  
        apar = new Attribute(attx,partype,INTERNAL); 
        t2 = "List";
      }
      apar.setElementType(new Type(entity2)); 

      unlink1 = "  List " + oldrole2xx + " = " + ex + ".get" + role2 + "();\n"; 
      String role1set = ""; 
      String e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ",_xx);"; 
     
      if (card2 == ZEROONE)
      { settest = "  if (" + attx + ".size() > 1) { return; }\n"; } 
 
      // if (card1 == ONE || card1 == ZEROONE)
      // { 
      if (role1 != null && role1.length() > 0) 
      { 
        if (card1 == ONE)
        { unlink1 = unlink1 + 
              "    for (int _j = 0; _j < " + oldrole2xx + ".size(); _j++)\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + ".get(_j);\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.set" + role1 + "(null); }\n" + 
              "    }\n"; 
          role1set = "    _xx.set" + role1 + "(" + ex + ");\n"; 
          e1removexx = "if (_xx.get" + role1 + "() != null) { _xx.get" + role1 + "().remove" + nme + "(_xx); } ";  
        }
        else if (card1 == ZEROONE)
        { unlink1 = unlink1 + 
              "    for (int _j = 0; _j < " + oldrole2xx + ".size(); _j++)\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + ".get(_j);\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
              "    }\n"; 

          role1set = "    Vector _xxNewValue = new Vector();\n" + 
                       "    _xxNewValue.add(" + ex + ");\n" + 
                       "    _xx.set" + role1 + "(_xxNewValue);\n"; 
          e1removexx = e1ref + ".removeAll" + nme + "(_xx.get" + role1 + "(),_xx);";
        } // Note case for interface e1
        else  // MANY at card1 
        { unlink1 = unlink1 + 
               "    for (int _j = 0; _j < " + oldrole2xx + ".size(); _j++)\n" + 
               "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + ".get(_j);\n" + 
               "      if (" + attx + ".contains(_yy)) { }\n" + 
               "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
               "    }\n";
          e1removexx = "_xx.add" + role1 + "(" + ex + ");";
          role1set = "";   
        }  

      } 
      else if (card1 == ZEROONE || card1 == ONE)  // No role1
      { e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ", _xx);"; }
      // Not for interface e1

      setinverse = 
          "  for (int _i = 0; _i < " + attx + ".size(); _i++)\n" + 
          "  { " + entity2 + " _xx = (" + entity2 + ") " + attx + ".get(_i);\n" + 
          "    if (" + oldrole2xx + ".contains(_xx)) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     

     if (card1 == MANY && (role1 == null || role1.length() == 0))
     { unlink1 = ""; 
       setinverse = ""; 
     }  // Do nothing to inverse in this case
    }

    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t2 + " " + attx + ") \n  { " + settest + unlink1 + setinverse + 
             "    " + ex + ".set" + nme + "(" + attx + ");\n  ";

      Vector contexts = new Vector(); 
      contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { System.out.println("Matched: new cons is " + cnew + " Pars " + v1); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }
        boolean typed = cnew.typeCheck(types,entities,cntxs,v1);
        if (typed)
        { String update = cnew.globalUpdateOp(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }
    res.add(opheader + "  " + assocclassextra + "  }\n\n");

    if (ordered) // setrole2(ename ex, int ind, e2name e2x) 
                 // { ex.setrole2(ind,e2x); } 
    { opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", int _ind, " + e2name + " " + e2x + ") \n" + 
             "  { " + ex + ".set" + nme + "(_ind," + e2x + "); }\n  ";
      res.add(opheader); 
    } 
    return res; 
  }

  // for the controller
  public Vector setOperationsCodeJava6(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    String nme = role2; 
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerSetOperationJava6(ent); } 

    if (linkedClass != null && card2 != ONE) 
    { return associationClassControllerSetOperation(ent); } // Java6? 

    String opheader = "";
    String ename = ent.getName();
    String e2name = entity2.getName(); 
    String e1name = entity1.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    BasicExpression attxbe = new BasicExpression(attx); 
    String oldrole2xx = "_old" + role2 + "xx";
    String e1s = (entity1 + "").toLowerCase() + "s"; 

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 
        
    String unlink1 = "";     // Unlink ex.getrole2() - attx elements from ex
    String setinverse = "";  // set role1 if it exists
    String settest = "";      

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar;
    String t2; 
    String assocclassextra = ""; 

    if (card2 == ONE)
    { apar = new Attribute(attx,new Type(entity2),INTERNAL);
      settest = "  if (" + ex + ".get" + role2 + "() == " + attx + ") { return; }\n";

      if (linkedClass != null) 
      { String lcname = linkedClass.getName(); 
        String e1role = e1name.toLowerCase(); 
        String e2role = e2name.toLowerCase(); 
        String ls = lcname.toLowerCase() + "s"; 
        String lx = lcname.toLowerCase() + "__x"; 
        assocclassextra = "  for (int _l = 0; _l < " + ls + ".size(); _l++)\n" + 
                          "    { " + lcname + " " + lx + " = (" + lcname + ") " + ls + ".get(_l);\n" +
                          "      if (" + lx + ".get" + e1role + "() == " + ex + ")\n" + 
                          "      { " + lx + ".set" + e2role + "(" + attx + "); return; }\n" + 
                          "    }\n" +
                          "    create" + lcname + "(" + ex + ", " + attx + ");\n";  
      }
 
      t2 = e2name; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == MANY)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + "); } \n" + 
                       "    " + attx + ".add" + role1 + "(" + ex + ");\n";
        }
        else if (card1 == ZEROONE)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + ");\n" + 
                       "      for (Object _p : " + attx + ".get" + role1 + "())\n" + 
                       "      { " + e1name + " _p_xx = (" + e1name + ") _p;\n" + 
                       "        _p_xx.set" + role2 + "(null);\n" + 
                       "      }\n" + 
                       "    }\n" + 
                       "    HashSet _exvect = new HashSet();\n" + 
                       "    _exvect.add(" + ex + ");\n" + 
                       "    " + attx + ".set" + role1 + "(_exvect);\n";
        }
        else // 1-1 association
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + ex + ".get" + role2 + "().set" + role1 + "(null); }\n" +
                       "    if (" + attx + ".get" + role1 + "() != null)\n" + 
                       "    { " + attx + ".get" + role1 + "().set" + role2 + "(null); }\n" + 
                       "    " + attx + ".set" + role1 + "(" + ex + ");\n"; 
        }   
      } // if role1 un-named, still need to do, for ZEROONE role1: 
        // for (e1xx : e1s) { if (e1xx.getrole2() == attx) { e1xx.setrole2(null); } } 
      else if (card1 == ZEROONE || card1 == ONE) 
      { setinverse = "    for (Object _q : " + e1s + ")\n" + 
                     "    { " + e1name + " _q_E1x = (" + e1name + ") _q;\n" + 
                     "      if (_q_E1x.get" + role2 + "() == " + attx + ")\n" + 
                     "      { _q_E1x.set" + role2 + "(null); }\n" + 
                     "    }\n"; 
      }   
    }
    else           // card2 == MANY or ZEROONE
    { settest = "  if (" + ex + ".get" + role2 + "().equals(" + attx + ")) { return; }\n";  
      Type partype; 
      if (ordered)
      { partype = new Type("Sequence",null); 
        partype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx, partype, INTERNAL); 
        t2 = "ArrayList";
      }
      else 
      { partype = new Type("Set",null); 
        partype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx, partype, INTERNAL); 
        t2 = "HashSet";
      }
      apar.setElementType(new Type(entity2)); 

      unlink1 = "  " + t2 + " " + oldrole2xx + " = " + ex + ".get" + role2 + "();\n"; 
      String role1set = ""; 
      String e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ",_xx);"; 
     
      if (card2 == ZEROONE)
      { settest = "  if (" + attx + ".size() > 1) { return; }\n"; } 
 
      // if (card1 == ONE || card1 == ZEROONE)
      // { 
      if (role1 != null && role1.length() > 0) 
      { 
        if (card1 == ONE)
        { unlink1 = unlink1 + 
              "    for (Object _o : " + oldrole2xx + ")\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.set" + role1 + "(null); }\n" + 
              "    }\n"; 
          role1set = "    _xx.set" + role1 + "(" + ex + ");\n"; 
          e1removexx = "if (_xx.get" + role1 + "() != null) { _xx.get" + role1 + "().remove" + nme + "(_xx); } ";  
        }
        else if (card1 == ZEROONE)
        { unlink1 = unlink1 + 
              "    for (Object _o : " + oldrole2xx + ")\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
              "    }\n"; 

          role1set = "    HashSet _xxNewValue = new HashSet();\n" + 
                       "    _xxNewValue.add(" + ex + ");\n" + 
                       "    _xx.set" + role1 + "(_xxNewValue);\n"; 
          e1removexx = e1ref + ".removeAll" + nme + "(_xx.get" + role1 + "(),_xx);";
        } 
        else  // MANY at card1 
        { unlink1 = unlink1 + 
               "    for (Object _o : " + oldrole2xx + ")\n" + 
               "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
               "      if (" + attx + ".contains(_yy)) { }\n" + 
               "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
               "    }\n";
          e1removexx = "_xx.add" + role1 + "(" + ex + ");";
          role1set = "";   
        }  

      } 
      else if (card1 == ZEROONE || card1 == ONE)  // No role1
      { e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ", _xx);"; }

      setinverse = 
          "  for (Object _o : " + attx + ")\n" + 
          "  { " + entity2 + " _xx = (" + entity2 + ") _o;\n" + 
          "    if (" + oldrole2xx + ".contains(_xx)) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     

     if (card1 == MANY && (role1 == null || role1.length() == 0))
     { unlink1 = ""; 
       setinverse = ""; 
     }  // Do nothing to inverse in this case
    }

    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t2 + " " + attx + ") \n  { " + settest + unlink1 + setinverse + 
             "    " + ex + ".set" + nme + "(" + attx + ");\n  ";

      Vector contexts = new Vector(); 
      contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { System.out.println("Matched: new cons is " + cnew + " Pars " + v1); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }
        boolean typed = cnew.typeCheck(types,entities,cntxs,v1);
        if (typed)
        { String update = cnew.globalUpdateOpJava6(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }
    res.add(opheader + "  " + assocclassextra + "  }\n\n");

    if (ordered) // setrole2(ename ex, int ind, e2name e2x) 
                 // { ex.setrole2(ind,e2x); } 
    { opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", int _ind, " + e2name + " " + e2x + ") \n" + 
             "  { " + ex + ".set" + nme + "(_ind," + e2x + "); }\n  ";
      res.add(opheader); 
    } 
    return res; 
  }

  // for the controller
  public Vector setOperationsCodeJava7(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    String nme = role2; 
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerSetOperationJava7(ent); } 

    if (linkedClass != null && card2 != ONE) 
    { return associationClassControllerSetOperation(ent); } // Java7? 

    String opheader = "";
    String ename = ent.getName();
    String e2name = entity2.getName(); 
    String e1name = entity1.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    BasicExpression attxbe = new BasicExpression(attx); 
    String oldrole2xx = "_old" + role2 + "xx";
    String e1s = (entity1 + "").toLowerCase() + "s"; 

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "." + e1name + "Ops"; } 
        
    String unlink1 = "";     // Unlink ex.getrole2() - attx elements from ex
    String setinverse = "";  // set role1 if it exists
    String settest = "";      

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar;
    String t2; 
    String assocclassextra = ""; 

    if (card2 == ONE)
    { apar = new Attribute(attx,new Type(entity2),INTERNAL);
      settest = "  if (" + ex + ".get" + role2 + "() == " + attx + ") { return; }\n";

      if (linkedClass != null) 
      { String lcname = linkedClass.getName(); 
        String e1role = e1name.toLowerCase(); 
        String e2role = e2name.toLowerCase(); 
        String ls = lcname.toLowerCase() + "s"; 
        String lx = lcname.toLowerCase() + "__x"; 
        assocclassextra = "  for (int _l = 0; _l < " + ls + ".size(); _l++)\n" + 
                          "    { " + lcname + " " + lx + " = (" + lcname + ") " + ls + ".get(_l);\n" +
                          "      if (" + lx + ".get" + e1role + "() == " + ex + ")\n" + 
                          "      { " + lx + ".set" + e2role + "(" + attx + "); return; }\n" + 
                          "    }\n" +
                          "    create" + lcname + "(" + ex + ", " + attx + ");\n";  
      }
 
      t2 = e2name; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == MANY)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + "); } \n" + 
                       "    " + attx + ".add" + role1 + "(" + ex + ");\n";
        }
        else if (card1 == ZEROONE)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + ");\n" + 
                       "      for (Object _p : " + attx + ".get" + role1 + "())\n" + 
                       "      { " + e1name + " _p_xx = (" + e1name + ") _p;\n" + 
                       "        _p_xx.set" + role2 + "(null);\n" + 
                       "      }\n" + 
                       "    }\n" + 
                       "    HashSet<" + e1name + "> _exvect = new HashSet<" + e1name + ">();\n" + 
                       "    _exvect.add(" + ex + ");\n" + 
                       "    " + attx + ".set" + role1 + "(_exvect);\n";
        }
        else // 1-1 association
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + ex + ".get" + role2 + "().set" + role1 + "(null); }\n" +
                       "    if (" + attx + ".get" + role1 + "() != null)\n" + 
                       "    { " + attx + ".get" + role1 + "().set" + role2 + "(null); }\n" + 
                       "    " + attx + ".set" + role1 + "(" + ex + ");\n"; 
        }   
      } // if role1 un-named, still need to do, for ZEROONE role1: 
        // for (e1xx : e1s) { if (e1xx.getrole2() == attx) { e1xx.setrole2(null); } } 
      else if (card1 == ZEROONE || card1 == ONE) 
      { setinverse = "    for (Object _q : " + e1s + ")\n" + 
                     "    { " + e1name + " _q_E1x = (" + e1name + ") _q;\n" + 
                     "      if (_q_E1x.get" + role2 + "() == " + attx + ")\n" + 
                     "      { _q_E1x.set" + role2 + "(null); }\n" + 
                     "    }\n"; 
      }   
    }
    else           // card2 == MANY or ZEROONE
    { Type partype; 
      if (ordered)
      { partype = new Type("Sequence",null); 
        partype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx, partype, INTERNAL); 
        t2 = "ArrayList<" + e2name + ">";
      }
      else 
      { partype = new Type("Set",null); 
        partype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx, partype, INTERNAL); 
        if (sortedAsc) 
        { t2 = "TreeSet<" + e2name + ">"; 
          partype.setSorted(true); 
        } 
        else 
        { t2 = "HashSet<" + e2name + ">"; } 
      }
      apar.setElementType(new Type(entity2)); 

      unlink1 = "  " + t2 + " " + oldrole2xx + " = " + ex + ".get" + role2 + "();\n"; 
      String role1set = ""; 
      String e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ",_xx);"; 
     
      if (card2 == ZEROONE)
      { settest = "  if (" + attx + ".size() > 1) { return; }\n"; } 
 
      // if (card1 == ONE || card1 == ZEROONE)
      // { 
      if (role1 != null && role1.length() > 0) 
      { 
        if (card1 == ONE)
        { unlink1 = unlink1 + 
              "    for (Object _o : " + oldrole2xx + ")\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.set" + role1 + "(null); }\n" + 
              "    }\n"; 
          role1set = "    _xx.set" + role1 + "(" + ex + ");\n"; 
          e1removexx = "if (_xx.get" + role1 + "() != null) { _xx.get" + role1 + "().remove" + nme + "(_xx); } ";  
        }
        else if (card1 == ZEROONE)
        { unlink1 = unlink1 + 
              "    for (Object _o : " + oldrole2xx + ")\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
              "      if (" + attx + ".contains(_yy)) { }\n" + 
              "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
              "    }\n"; 

          role1set = "    HashSet<" + e1name + "> _xxNewValue = new HashSet<" + e1name + ">();\n" + 
                       "    _xxNewValue.add(" + ex + ");\n" + 
                       "    _xx.set" + role1 + "(_xxNewValue);\n"; 
          e1removexx = e1ref + ".removeAll" + nme + "(_xx.get" + role1 + "(),_xx);";
        } 
        else  // MANY at card1 
        { unlink1 = unlink1 + 
               "    for (Object _o : " + oldrole2xx + ")\n" + 
               "    { " + entity2 + " _yy = (" + entity2 + ") _o;\n" + 
               "      if (" + attx + ".contains(_yy)) { }\n" + 
               "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
               "    }\n";
          e1removexx = "_xx.add" + role1 + "(" + ex + ");";
          role1set = "";   
        }  

      } 
      else if (card1 == ZEROONE || card1 == ONE)  // No role1
      { e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ", _xx);"; }

      setinverse = 
          "  for (Object _o : " + attx + ")\n" + 
          "  { " + entity2 + " _xx = (" + entity2 + ") _o;\n" + 
          "    if (" + oldrole2xx + ".contains(_xx)) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     

     if (card1 == MANY && (role1 == null || role1.length() == 0))
     { unlink1 = ""; 
       setinverse = ""; 
     }  // Do nothing to inverse in this case
    }

    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t2 + " " + attx + ") \n  { " + settest + unlink1 + setinverse + 
             "    " + ex + ".set" + nme + "(" + attx + ");\n  ";

      Vector contexts = new Vector(); 
      contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { System.out.println("Matched: new cons is " + cnew + " Pars " + v1); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }
        boolean typed = cnew.typeCheck(types,entities,cntxs,v1);
        if (typed)
        { String update = cnew.globalUpdateOpJava7(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }
    res.add(opheader + "  " + assocclassextra + "  }\n\n");

    if (ordered) // setrole2(ename ex, int ind, e2name e2x) 
                 // { ex.setrole2(ind,e2x); } 
    { opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", int _ind, " + e2name + " " + e2x + ") \n" + 
             "  { " + ex + ".set" + nme + "(_ind," + e2x + "); }\n  ";
      res.add(opheader); 
    } 
    return res; 
  }

  public Vector setOperationsCodeCSharp(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    String nme = role2; 
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerSetOperationCSharp(ent); } 

    if (linkedClass != null && card2 != ONE) 
    { return associationClassControllerSetOperationCSharp(ent); } 

    String opheader = "";
    String ename = ent.getName();
    String e2name = entity2.getName(); 
    String e1name = entity1.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    BasicExpression attxbe = new BasicExpression(attx); 
    String oldrole2xx = "_old" + role2 + "xx";
    String e1s = (entity1 + "").toLowerCase() + "_s"; 

    String e1ref = e1name; 
    if (entity1.isInterface())
    { e1ref = e1name + "Ops"; } 
        
    String unlink1 = "";     // Unlink ex.getrole2() - attx elements from ex
    String setinverse = "";  // set role1 if it exists
    String settest = "";      

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar;
    String t2; 
    String assocclassextra = ""; 

    if (card2 == ONE)
    { apar = new Attribute(attx,new Type(entity2),INTERNAL);
      settest = "  if (" + ex + ".get" + role2 + "() == " + attx + ") { return; }\n";

      if (linkedClass != null) 
      { String lcname = linkedClass.getName(); 
        String e1role = e1name.toLowerCase(); 
        String e2role = e2name.toLowerCase(); 
        String ls = lcname.toLowerCase() + "_s"; 
        String lx = lcname.toLowerCase() + "__x"; 
        assocclassextra = "  for (int _l = 0; _l < " + ls + ".Count; _l++)\n" + 
                          "    { " + lcname + " " + lx + " = (" + lcname + ") " + ls + "[_l];\n" +
                          "      if (" + lx + ".get" + e1role + "() == " + ex + ")\n" + 
                          "      { " + lx + ".set" + e2role + "(" + attx + "); return; }\n" + 
                          "    }\n" +
                          "    create" + lcname + "(" + ex + ", " + attx + ");\n";  
      }
 
      t2 = e2name; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == MANY)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + "); } \n" + 
                       "    " + attx + ".add" + role1 + "(" + ex + ");\n";
        }
        else if (card1 == ZEROONE)
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + e2name + " old_value = " + ex + ".get" + role2 + "();\n" + 
                       "      old_value.remove" + role1 + "(" + ex + ");\n" + 
                       "      for (int _p = 0; _p < " + attx + ".get" + role1 + "().Count; _p++)\n" + 
                       "      { " + e1name + " _p_xx = (" + e1name + ") " + attx + ".get" + 
                                      role1 + "()[_p];\n" + 
                       "        _p_xx.set" + role2 + "(null);\n" + 
                       "      }\n" + 
                       "    }\n" + 
                       "    ArrayList _exvect = new ArrayList();\n" + 
                       "    _exvect.Add(" + ex + ");\n" + 
                       "    " + attx + ".set" + role1 + "(_exvect);\n";
        }
        else // 1-1 association
        { setinverse = "    if (" + ex + ".get" + role2 + "() != null)\n" + 
                       "    { " + ex + ".get" + role2 + "().set" + role1 + "(null); }\n" +
                       "    if (" + attx + ".get" + role1 + "() != null)\n" + 
                       "    { " + attx + ".get" + role1 + "().set" + role2 + "(null); }\n" + 
                       "    " + attx + ".set" + role1 + "(" + ex + ");\n"; 
        }   
      } // if role1 un-named, still need to do, for ZEROONE role1: 
        // for (e1xx : e1s) { if (e1xx.getrole2() == attx) { e1xx.setrole2(null); } } 
      else if (card1 == ZEROONE || card1 == ONE)
      { setinverse = "    for (int _q = 0; _q < " + e1s + ".Count; _q++)\n" + 
                     "    { " + e1name + " _q_E1x = (" + e1name + ") " + e1s + "[_q];\n" + 
                     "      if (_q_E1x.get" + role2 + "() == " + attx + ")\n" + 
                     "      { _q_E1x.set" + role2 + "(null); }\n" + 
                     "    }\n"; 
      }   
    }
    else           // card2 == MANY or ZEROONE
    { Type coltype; 
      if (ordered)
      { coltype = new Type("Sequence",null); } 
      else 
      { coltype = new Type("Set",null); } 
      coltype.setElementType(new Type(entity2)); 
      apar = new Attribute(attx, coltype, INTERNAL); 
      t2 = "ArrayList";
      apar.setElementType(new Type(entity2)); 

      unlink1 = "  ArrayList " + oldrole2xx + " = " + ex + ".get" + role2 + "();\n"; 
      String role1set = ""; 
      String e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ", _xx);"; 
     
      if (card2 == ZEROONE)
      { settest = "  if (" + attx + ".Count > 1) { return; }\n"; } 
 
      // if (card1 == ONE || card1 == ZEROONE)
      // { 
      if (role1 != null && role1.length() > 0) 
      { 
        if (card1 == ONE)
        { unlink1 = unlink1 + 
              "    for (int _j = 0; _j < " + oldrole2xx + ".Count; _j++)\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + "[_j];\n" + 
              "      if (" + attx + ".Contains(_yy)) { }\n" + 
              "      else { _yy.set" + role1 + "(null); }\n" + 
              "    }\n"; 
          role1set = "    _xx.set" + role1 + "(" + ex + ");\n"; 
          e1removexx = "if (_xx.get" + role1 + "() != null) { _xx.get" + role1 + "().remove" + nme + "(_xx); } ";  
        }
        else if (card1 == ZEROONE)
        { unlink1 = unlink1 + 
              "    for (int _j = 0; _j < " + oldrole2xx + ".Count; _j++)\n" + 
              "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + "[_j];\n" + 
              "      if (" + attx + ".Contains(_yy)) { }\n" + 
              "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
              "    }\n"; 

          role1set = "    ArrayList _xxNewValue = new ArrayList();\n" + 
                       "    _xxNewValue.Add(" + ex + ");\n" + 
                       "    _xx.set" + role1 + "(_xxNewValue);\n"; 
          e1removexx = e1ref + ".removeAll" + nme + "(_xx.get" + role1 + "(), _xx);";
        } 
        else  // MANY at card1 
        { unlink1 = unlink1 + 
               "    for (int _j = 0; _j < " + oldrole2xx + ".Count; _j++)\n" + 
               "    { " + entity2 + " _yy = (" + entity2 + ") " + oldrole2xx + "[_j];\n" + 
               "      if (" + attx + ".Contains(_yy)) { }\n" + 
               "      else { _yy.remove" + role1 + "(" + ex + "); }\n" + 
               "    }\n";
          e1removexx = "_xx.add" + role1 + "(" + ex + ");";
          role1set = "";   
        }  

      } 
      else if (card1 == ZEROONE || card1 == ONE)  // No role1
      { e1removexx = e1ref + ".removeAll" + nme + "(" + e1s + ", _xx);"; }

      setinverse = 
          "  for (int _i = 0; _i < " + attx + ".Count; _i++)\n" + 
          "  { " + entity2 + " _xx = (" + entity2 + ") " + attx + "[_i];\n" + 
          "    if (" + oldrole2xx + ".Contains(_xx)) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     

     if (card1 == MANY && (role1 == null || role1.length() == 0))
     { unlink1 = ""; 
       setinverse = ""; 
     }  // Do nothing to inverse in this case
    }

    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", " + t2 + " " + attx + ") \n  { " + settest + unlink1 + setinverse + 
             "    " + ex + ".set" + nme + "(" + attx + ");\n  ";

      Vector contexts = new Vector(); 
      contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { System.out.println("Matched: new cons is " + cnew + " Pars " + v1); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }
        boolean typed = cnew.typeCheck(types,entities,cntxs,v1);
        if (typed)
        { String update = cnew.globalUpdateOpCSharp(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }
    res.add(opheader + "  " + assocclassextra + "  }\n\n");

    if (ordered) // setrole2(ename ex, int ind, e2name e2x) 
                 // { ex.setrole2(ind,e2x); } 
    { opheader = "  public void set" + nme + "(" + ename + " " +
             ex + ", int _ind, " + e2name + " " + e2x + ") \n" + 
             "  { " + ex + ".set" + nme + "(_ind," + e2x + "); }\n  ";
      res.add(opheader); 
    } 
    return res; 
  }

  // for the controller. Only vectors considered
  public Vector setOperationsCodeCPP(Vector cons,
                                  Entity ent,Vector entities,Vector types)
  { Vector res = new Vector();
    String nme = role2; 
    if (role2 == null || frozen)
    { return res; } 
    if (qualifier != null) 
    { return qualifierControllerSetOperationCPP(ent); } 

    if (linkedClass != null && card2 != ONE) 
    { return associationClassControllerSetOperationCPP(ent); } 

    String opheader = "";
    String ename = ent.getName();
    String e2name = entity2.getName(); 
    String e1name = entity1.getName(); 
    String ex = ename.toLowerCase() + "x";
    String e2x = e2name.toLowerCase() + "x";
    String attx = nme + "xx";
    BasicExpression attxbe = new BasicExpression(attx); 
    String oldrole2xx = "_old" + role2 + "xx";
    String e1s = (entity1 + "").toLowerCase() + "_s";  // Controller::inst->e1s
        
    String unlink1 = "";     // Unlink ex.getrole2() - attx elements from ex
    String setinverse = "";  // set role1 if it exists
    String settest = "";      

    Attribute epar = new Attribute(ex,new Type(ent),INTERNAL);
    Attribute apar;
    String t2; 
    String assocclassextra = ""; 

    if (card2 == ONE)
    { apar = new Attribute(attx,new Type(entity2),INTERNAL);
      settest = "  if (" + ex + "->get" + role2 + "() == " + attx + ") { return; }\n";

      if (linkedClass != null) 
      { String lcname = linkedClass.getName(); 
        String e1role = e1name.toLowerCase(); 
        String e2role = e2name.toLowerCase(); 
        String ls = lcname.toLowerCase() + "_s";  // Controller::getls()
        String lx = lcname.toLowerCase() + "__x"; 
        assocclassextra = "  for (int _l = 0; _l < " + ls + "->size(); _l++)\n" + 
                          "    { " + lcname + "* " + lx + " = (" + lcname + ") " + ls + "->at(_l);\n" +
                          "      if (" + lx + "->get" + e1role + "() == " + ex + ")\n" + 
                          "      { " + lx + "->set" + e2role + "(" + attx + "); return; }\n" + 
                          "    }\n" +
                          "    create" + lcname + "(" + ex + ", " + attx + ");\n";  
      } 
 
      t2 = e2name + "*"; 
      if (role1 != null && role1.length() > 0)
      { if (card1 == MANY)
        { setinverse = "    if (" + ex + "->get" + role2 + "() != 0)\n" + 
                       "    { " + e2name + "* old_value = " + ex + "->get" + role2 + "();\n" + 
                       "      old_value->remove" + role1 + "(" + ex + "); } \n" + 
                       "    " + attx + "->add" + role1 + "(" + ex + ");\n";
        }
        else if (card1 == ZEROONE) // Assume its a set
        { setinverse = "    if (" + ex + "->get" + role2 + "() != 0)\n" + 
                       "    { " + e2name + "* _old_value = " + ex + "->get" + role2 + "();\n" + 
                       "      _old_value->remove" + role1 + "(" + ex + ");\n" + 
                       "      set<" + e1name + "*>* _old_role1 = " + attx + "->get" + role1 + "();\n" +
                       "      for (set<" + e1name + "*>::iterator _p = _old_role1->begin(); _p != _old_role1->end(), ++_p)\n" + 
                       "      { " + e1name + "* _p_xx = *_p;\n" + 
                       "        _p_xx->set" + role2 + "(0);\n" + 
                       "      }\n" + 
                       "    }\n" + 
                       "    set<" + e1name + "*>* _exvect = new set<" + e1name + "*>();\n" + 
                       "    _exvect->insert(" + ex + ");\n" + 
                       "    " + attx + "->set" + role1 + "(_exvect);\n";
        }
        else // 1-1 association
        { setinverse = "    if (" + ex + "->get" + role2 + "() != 0)\n" + 
                       "    { " + ex + "->get" + role2 + "()->set" + role1 + "(0); }\n" +
                       "    if (" + attx + "->get" + role1 + "() != 0)\n" + 
                       "    { " + attx + "->get" + role1 + "()->set" + role2 + "(0); }\n" + 
                       "    " + attx + "->set" + role1 + "(" + ex + ");\n"; 
        }   
      } // if role1 un-named, still need to do, for ZEROONE role1: 
        // for (e1xx : e1s) { if (e1xx.getrole2() == attx) { e1xx.setrole2(null); } } 
      else if (card1 == ZEROONE || card1 == ONE) 
      { setinverse = "    for (int _q = 0; _q < " + e1s + "->size(); _q++)\n" + 
                     "    { " + e1name + "* _q_E1x = (*" + e1s + ")[_q];\n" + 
                     "      if (_q_E1x->get" + role2 + "() == " + attx + ")\n" + 
                     "      { _q_E1x->set" + role2 + "(0); }\n" + 
                     "    }\n"; 
      }   
    }
    else           // card2 == MANY or ZEROONE
    { String forheader = 
              "    for (set<" + e2name + "*>::iterator _j = " + oldrole2xx + 
                    "->begin(); _j != " + oldrole2xx + "->end(); ++_j)\n" + 
              "    { " + entity2 + "* _yy = *_j;\n"; 
      Type coltype; 
      if (ordered)
      { coltype = new Type("Sequence",null); 
        coltype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx,coltype,INTERNAL); 
        t2 = "vector<" + e2name + "*>*";
        forheader =  
              "    for (int _j = 0; _j < " + oldrole2xx + "->size(); _j++)\n" + 
              "    { " + entity2 + "* _yy = (*" + oldrole2xx + ")[_j];\n"; 
      }
      else // Not considered here
      { coltype = new Type("Set",null); 
        coltype.setElementType(new Type(entity2)); 
        apar = new Attribute(attx,new Type("Set",null),INTERNAL); 
        t2 = "set<" + e2name + "*>*";
      }
      apar.setElementType(new Type(entity2)); 

      unlink1 = "  " + t2 + " " + oldrole2xx + " = " + ex + "->get" + role2 + "();\n"; 
      String role1set = ""; 
      String e1removexx = entity1 + "::removeAll" + nme + "(" + e1s + ",_xx);"; 
     
      if (card2 == ZEROONE)
      { settest = "  if (" + attx + "->size() > 1) { return; }\n"; } 
 
      // if (card1 == ONE || card1 == ZEROONE)
      // { 
      if (role1 != null && role1.length() > 0) 
      { 
        if (card1 == ONE)
        { unlink1 = unlink1 + forheader +
              "      if (UmlRsdsLib<" + e2name + "*>::isIn(_yy, " + attx + ")) { }\n" + 
              "      else { _yy->set" + role1 + "(0); }\n" + 
              "    }\n"; 
          role1set = "    _xx->set" + role1 + "(" + ex + ");\n"; 
          e1removexx = "if (_xx->get" + role1 + "() != 0) { _xx->get" + role1 + "()->remove" + nme + "(_xx); } ";  
        }
        else if (card1 == ZEROONE)
        { unlink1 = unlink1 + forheader + 
              "      if (UmlRsdsLib<" + e2name + "*>::isIn(_yy, " + attx + ")) { }\n" + 
              "      else { _yy->remove" + role1 + "(" + ex + "); }\n" + 
              "    }\n"; 

          role1set = "    set<" + e1name + "*>* _xxNewValue = new set<" + e1name + "*>();\n" + 
                       "    _xxNewValue->insert(" + ex + ");\n" + 
                       "    _xx->set" + role1 + "(_xxNewValue);\n"; 
          e1removexx = entity1 + "::removeAll" + nme + "(_xx->get" + role1 + "(),_xx);";
        } 
        else  // MANY at card1 
        { unlink1 = unlink1 + forheader + 
               "      if (UmlRsdsLib<" + e2name + "*>::isIn(_yy, " + attx + ")) { }\n" + 
               "      else { _yy->remove" + role1 + "(" + ex + "); }\n" + 
               "    }\n";
          e1removexx = "_xx->add" + role1 + "(" + ex + ");";
          role1set = "";   
        }  

      } 
      else if (card1 == ZEROONE || card1 == ONE)  // No role1
      { e1removexx = entity1 + "::removeAll" + nme + "(" + e1s + ", _xx);"; }

      if (ordered)
      { setinverse = 
          "  for (int _i = 0; _i < " + attx + "->size(); _i++)\n" + 
          "  { " + entity2 + "* _xx = (*" + attx + ")[_i];\n" + 
          "    if (UmlRsdsLib<" + e2name + "*>::isIn(_xx, " + oldrole2xx + ")) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     
      } 
      else 
      { setinverse = 
          "  for (set<" + e2name + "*>::iterator _i = " + attx + "->begin(); _i != " + 
                  attx + "->end(); ++_i)\n" + 
          "  { " + entity2 + "* _xx = *_i;\n" + 
          "    if (UmlRsdsLib<" + e2name + "*>::isIn(_xx, " + oldrole2xx + ")) { }\n" + 
          "    else { " + e1removexx + " }\n" +
          role1set + "  }\n";     
      } 


     if (card1 == MANY && (role1 == null || role1.length() == 0))
     { unlink1 = ""; 
       setinverse = ""; 
     }  // Do nothing to inverse in this case
    }

    Vector v1 = new Vector();
    v1.add(epar);
    v1.add(apar);
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,v1,false,null);
    opheader = "  void set" + nme + "(" + ename + "* " +
             ex + ", " + t2 + " " + attx + ") \n  { " + settest + unlink1 + setinverse + 
             "    " + ex + "->set" + nme + "(" + attx + ");\n  ";

      Vector contexts = new Vector(); 
      contexts.add(ent); 

    for (int j = 0; j < cons.size(); j++)
    { Constraint cc = (Constraint) cons.get(j);
      Constraint cnew = cc.matches("set",nme,ent,attx,event);
      // if (cc.matches(nme,val))
      if (cnew != null)
      { System.out.println("Matched: new cons is " + cnew + " Pars " + v1); 
        Vector cntxs = new Vector(); 
        if (cnew.getOwner() != null) 
        { cntxs.add(cnew.getOwner()); }
        boolean typed = cnew.typeCheck(types,entities,cntxs,v1);
        if (typed)
        { String update = cnew.globalUpdateOpCPP(ent,false);
          opheader = opheader +
                     update + "\n";
        }
      }
      else if (cc.getEvent() == null && cc.involvesFeature(nme))
      { Constraint cpre = (Constraint) cc.substituteEq(nme,attxbe); 
        System.out.println("Possible precond for set" + nme + ": " + cpre); 
      }

    }
    res.add(opheader + "  " + assocclassextra + "  }\n\n");

    if (ordered) // setrole2(ename ex, int ind, e2name e2x) 
                 // { ex.setrole2(ind,e2x); } 
    { opheader = "  void set" + nme + "(" + ename + "* " +
             ex + ", int _ind, " + e2name + "* " + e2x + ") \n" + 
             "  { " + ex + "->set" + nme + "(_ind," + e2x + "); }\n  ";
      res.add(opheader); 
    } 
    return res; 
  }


  public Vector qualifierControllerSetOperation(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
    }
    else 
    { res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, List " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    return res; 
  } 

  public Vector qualifierControllerSetOperationJava6(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
    }
    else if (ordered) 
    { res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, ArrayList " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    else
    { res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, HashSet " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    return res; 
  } 

  public Vector qualifierControllerSetOperationJava7(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 

    String tname; 
    if (ordered) 
    { tname = "ArrayList<" + e2name + ">"; } 
    else if (sortedAsc)
    { tname = "TreeSet<" + e2name + ">"; } 
    else
    { tname = "HashSet<" + e2name + ">"; }  
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", String _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
    }
    else 
    { res.add(" public void set" + role2 + "(" + edec + 
                   ", String _ind, " + tname + " " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    return res; 
  } 

  public Vector qualifierControllerSetOperationCSharp(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" public void set" + role2 + "(" + edec +  
                   ", string _ind, " + e2name + " " + val + ")\n" + 
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" public void remove" + role2 + "(" + edec + 
                  ", " + e2name + " " + val + ")\n" + 
                " { " + ex + ".remove" + role2 + "(" + val + "); }\n"); 
    }
    else 
    { res.add(" public void set" + role2 + "(" + edec + 
                   ", string _ind, ArrayList " + val + ")\n  " +
              " { " + ex + ".set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    return res; 
  } 

  public Vector qualifierControllerSetOperationCPP(Entity ent)
  { Vector res = new Vector();
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + "* " + ex; 
    String val = "_" + role2 + "xx"; 
 
    
    if (card2 == ONE) 
    { res.add(" void set" + role2 + "(" + edec +  
                   ", string _ind, " + e2name + "* " + val + ")\n" + 
              " { " + ex + "->set" + role2 + "(_ind, " + val + "); } \n");
      res.add(" void remove" + role2 + "(" + edec + 
                  ", " + e2name + "* " + val + ")\n" + 
                " { " + ex + "->remove" + role2 + "(" + val + "); }\n"); 
    }
    else if (ordered)
    { res.add(" void set" + role2 + "(" + edec + 
                   ", string _ind, vector<" + e2name + "*>* " + val + ")\n  " +
              " { " + ex + "->set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    else 
    { res.add(" void set" + role2 + "(" + edec + 
                   ", string _ind, set<" + e2name + "*>* " + val + ")\n  " +
              " { " + ex + "->set" + role2 + "(_ind, " + val + "); }\n"); 
    } 
    return res; 
  } 


  public Vector associationClassControllerSetOperation(Entity ent)
  { Vector res = new Vector(); 
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
    String oldrole2 = "old_" + role2;  
    String exx = "__" + role2 + "x"; 
    
    if (card2 == ONE) { return res; }  // no valid operation

    String setop = 
      "  public void set" + role2 + "(" + edec + ", List " + val + ")\n";
    setop = setop + 
      "  { List " + oldrole2 + " = new Vector();\n" + 
      "    " + oldrole2 + ".addAll(" + ex + ".get" + role2 + "());\n" + 
      "    for (int _i = 0; _i < " + val + ".size(); _i++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + val + ".get(_i);\n" + 
      "      if (" + oldrole2 + ".contains(" + exx + ")) {}\n" + 
      "      else { add" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "    for (int _j = 0; _j < " + oldrole2 + ".size(); _j++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + oldrole2 + ".get(_j);\n" + 
      "      if (" + val + ".contains(" + exx + ")) {}\n" + 
      "      else { remove" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "  }\n"; 
    res.add(setop); 
    return res; 
  } 

  public Vector associationClassControllerSetOperationJava6(Entity ent)
  { Vector res = new Vector(); 
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
    String oldrole2 = "old_" + role2;  
    String exx = "__" + role2 + "x"; 
    
    if (card2 == ONE) { return res; }  // no valid operation

    String setop = 
      "  public void set" + role2 + "(" + edec + ", Collection " + val + ")\n";
    setop = setop + 
      "  { List " + oldrole2 + " = new ArrayList();\n" + 
      "    " + oldrole2 + ".addAll(" + ex + ".get" + role2 + "());\n" + 
      "    for (int _i = 0; _i < " + val + ".size(); _i++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + val + ".get(_i);\n" + 
      "      if (" + oldrole2 + ".contains(" + exx + ")) {}\n" + 
      "      else { add" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "    for (int _j = 0; _j < " + oldrole2 + ".size(); _j++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + oldrole2 + ".get(_j);\n" + 
      "      if (" + val + ".contains(" + exx + ")) {}\n" + 
      "      else { remove" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "  }\n"; 
    res.add(setop); 
    return res; 
  } 
   
  public Vector associationClassControllerSetOperationCSharp(Entity ent)
  { Vector res = new Vector(); 
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + " " + ex; 
    String val = "_" + role2 + "xx"; 
    String oldrole2 = "old_" + role2;  
    String exx = "__" + role2 + "x"; 
    
    if (card2 == ONE) { return res; }  // no valid operation

    String setop = 
      "  public void set" + role2 + "(" + edec + ", ArrayList " + val + ")\n";
    setop = setop + 
      "  { ArrayList " + oldrole2 + " = new ArrayList();\n" + 
      "    " + oldrole2 + ".AddRange(" + ex + ".get" + role2 + "());\n" + 
      "    for (int _i = 0; _i < " + val + ".Count; _i++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + val + "[_i];\n" + 
      "      if (" + oldrole2 + ".Contains(" + exx + ")) {}\n" + 
      "      else { add" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "    for (int _j = 0; _j < " + oldrole2 + ".Count; _j++)\n" + 
      "    { " + e2name + " " + exx + " = (" + e2name + ") " + oldrole2 + "[_j];\n" + 
      "      if (" + val + ".Contains(" + exx + ")) {}\n" + 
      "      else { remove" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "  }\n"; 
    res.add(setop); 
    return res; 
  } 

  public Vector associationClassControllerSetOperationCPP(Entity ent)
  { Vector res = new Vector(); 
    String ename = ent.getName(); 
    String e2name = entity2.getName();
    String ex = "_" + ename.toLowerCase() + "x";  
    String edec = ename + "* " + ex; 
    String val = "_" + role2 + "xx"; 
    String oldrole2 = "old_" + role2;  
    String exx = "__" + role2 + "x"; 
    
    if (card2 == ONE) { return res; }  // no valid operation

    String argtype = ""; 
    String insertop = ""; 
    if (ordered) 
    { argtype = "vector<" + e2name + "*>"; 
      insertop = "insert(" + oldrole2 + "->end(), "; 
    } 
    else 
    { argtype = "set<" + e2name + "*>"; 
      insertop = "insert("; 
    } 

    String setop = 
      "   void set" + role2 + "(" + edec + ", " + argtype + "* " + val + ")\n";
    setop = setop + 
      "  { " + argtype + "* " + oldrole2 + " = new " + argtype + "();\n" + 
      "    " + oldrole2 + "->" + insertop + ex + "->get" + role2 + "()->begin(), " + 
                                           ex + "->get" + role2 + "()->end());\n" + 
      "    for (" + argtype + "::iterator _pos = " + val + "->begin(); _pos != " + val + "->end(); _pos++)\n" + 
      "    { " + e2name + "* " + exx + " = *_pos;\n" + 
      "      if (UmlRsdsLib<" + e2name + "*>::isIn(" + exx + ", " + oldrole2 + ")) {}\n" + 
      "      else { add" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "    for (" + argtype + "::iterator _pos = " + oldrole2 + "->begin(); _pos != " + oldrole2 + "->end(); ++_pos)\n" + 
      "    { " + e2name + "* " + exx + " = *_pos;\n" + 
      "      if (UmlRsdsLib<" + e2name + "*>::isIn(" + exx + ", " + val + ")) {}\n" + 
      "      else { remove" + role2 + "(" + ex + ", " + exx + "); }\n" + 
      "    }\n" + 
      "  }\n"; 
    res.add(setop); 
    return res; 
  } 


  public String getCreateCode(String ex)
  { // setrole2(ex,initval) -- initval is nmex for ONE
    if (qualifier != null) { return ""; } 
    String nme = role2;
    String ini;
    String op = "    set" + nme + "(" + ex + ", ";
    if (card2 == ONE || frozen) 
    { ini = nme + "x"; }
    else 
    { ini = "new Vector()"; }
    return op + ini + ");\n"; 
  } // order so multi are last?

  public String getCreateCodeJava6(String ex)
  { // setrole2(ex,initval) -- initval is nmex for ONE
    if (qualifier != null) { return ""; } 
    String nme = role2;
    String ini;
    String op = "    set" + nme + "(" + ex + ", ";
    if (card2 == ONE || frozen) 
    { ini = nme + "x"; }
    else 
    { if (ordered)
      { ini = "new ArrayList()"; }
      else 
      { ini = "new HashSet()"; } 
    } 
    return op + ini + ");\n"; 
  } // order so multi are last?

  public String getCreateCodeJava7(String ex)
  { // setrole2(ex,initval) -- initval is nmex for ONE
    if (qualifier != null) { return ""; }
    String e2name = entity2.getName();  
    String nme = role2;
    String ini;
    String op = "    set" + nme + "(" + ex + ", ";
    if (card2 == ONE || frozen) 
    { ini = nme + "x"; }
    else 
    { if (ordered)
      { ini = "new ArrayList<" + e2name + ">()"; }
      else if (sortedAsc)
      { ini = "new TreeSet<" + e2name + ">()"; } 
      else 
      { ini = "new HashSet<" + e2name + ">()"; } 
    } 
    return op + ini + ");\n"; 
  } // order so multi are last?

  public String getCreateCodeCSharp(String ex)
  { // setrole2(ex,initval) -- initval is nmex for ONE
    if (qualifier != null) { return ""; } 
    String nme = role2;
    String ini;
    String op = "    set" + nme + "(" + ex + ", ";
    if (card2 == ONE || frozen) 
    { ini = nme + "x"; }
    else 
    { ini = "new ArrayList()"; }
    return op + ini + ");\n"; 
  } // order so multi are last?

  public String getCreateCodeCPP(String ex)
  { // setrole2(ex,initval) -- initval is nmex for ONE
    if (qualifier != null) { return ""; } 
    String nme = role2;
    String e2name = entity2.getName();

    String ini;
    String op = "    set" + nme + "(" + ex + ", ";
    if (card2 == ONE || frozen) 
    { ini = nme + "x"; }
    else if (ordered)
    { ini = "new vector<" + e2name + "*>()"; }
    else 
    { ini = "new set<" + e2name + "*>()"; }

    return op + ini + ");\n"; 
  } // order so multi are last?

  public String genSQLWhere(Vector rels)
  { if (rels.size() == 0)
    { return ""; } // cond.toSQL() 
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return generateSQLWhere(rels);
    }
  }

  // assume no 1-1 associations:
  public String generateSQLWhere(Vector rels)
  { String where = genSQLWhere(rels);
    if (isExplicit() && isPersistent())
    { String e1name = entity1.getName();
      String e2name = entity2.getName();
      if (card1 == ONE && card2 != ONE)
      { // Foreign key from entity2 to entity1
        Vector akeys = entity1.getUniqueAttributes();
        for (int i = 0; i < akeys.size(); i++)
        { Attribute akey = (Attribute) akeys.get(i);
          String akeynme = akey.getName();
          if (entity2.hasAttribute(akeynme))
          { String res = 
              e1name + "." + akeynme + " = " +
              e2name + "." + akeynme;
            if (where.equals("")) { where = res; }
            else 
            { where = where + " AND " + res; }
          }
        }
      }
      else if (card2 == ONE && card1 != ONE)
      { // Foreign key from entity1 to entity2
        Vector akeys = entity2.getUniqueAttributes();
        for (int i = 0; i < akeys.size(); i++)
        { Attribute akey = (Attribute) akeys.get(i);
          String akeynme = akey.getName();
          if (entity1.hasAttribute(akeynme))
          { String res = 
              e1name + "." + akeynme + " = " +
              e2name + "." + akeynme;
            if (where.equals("")) { where = res; }
            else 
            { where = where + " AND " + res; }
          }
        }
      }
    }
    return where;
  }

  public static String genSQLCode(Vector rels, String eId,
                                  Expression cond, Expression succ)
  { if (rels.size() == 0)
    { return " SET " + succ.toSQL() + " WHERE " + eId + " = ?"; }  
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.generateSQLCode(rels,eId,cond,succ);
    }
  }

  // assume no 1-1 associations:
  public String generateSQLCode(Vector rels, String eId,
                                Expression cond, Expression succ)
  { String where = genSQLCode(rels,eId,cond,succ);
    if (isExplicit() && isPersistent())
    { String e1name = entity1.getName();
      String e2name = entity2.getName();
      if (card1 == ONE && card2 != ONE)
      { // Foreign key from entity2 to entity1
        Vector akeys = entity1.getUniqueAttributes();
        for (int i = 0; i < akeys.size(); i++)
        { Attribute akey = (Attribute) akeys.get(i);
          String akeynme = akey.getName();
          if (entity2.hasAttribute(akeynme))
          { String res = 
              e1name + "." + akeynme + " = " +
              e2name + "." + akeynme;
            if (where.equals("")) { where = res; }
            else 
            { where = where + " AND " + res; }
          }
        }
      }
      else if (card2 == ONE && card1 != ONE)
      { // Foreign key from entity1 to entity2
        Vector akeys = entity2.getUniqueAttributes();
        for (int i = 0; i < akeys.size(); i++)
        { Attribute akey = (Attribute) akeys.get(i);
          String akeynme = akey.getName();
          if (entity1.hasAttribute(akeynme))
          { String res = 
              e1name + "." + akeynme + " = " +
              e2name + "." + akeynme;
            if (where.equals("")) { where = res; }
            else 
            { where = where + " AND " + res; }
          }
        }
      }
    }
    return where;
  }

  public Association mergeAssociation(Entity e, Association ast)
  { // ast is association from same entity as this
    int acard1 = ast.getCard1(); 
    int acard2 = ast.getCard2(); 
    Entity ent2 = ast.getEntity2(); 
    boolean ord = false; 

    Entity target = Entity.commonSuperclass(entity2,ent2); 
    if (target == null) 
    { return null; } 
    int ncard1 = mergeMultiplicities(card1,acard1); 
    int ncard2 = mergeMultiplicities(card2,acard2); 
    if (ordered && ast.isOrdered())
    { ord = true; } 

    Association res = new Association(e,target,ncard1,ncard2,role1,role2); 
    res.setOrdered(ord); 
    return res; 
  }


  public void introduceForeignKey()
  { // if association is MANY-ONE or ONE-MANY, add primary key of ONE side 
    // as new attribute of MANY side. Should only be done for explicit persistent
    // rels. 

    if (isPersistent() && isExplicit()) { } 
    else 
    { System.err.println("Warning: should only apply this transformation to \n" + 
                         "persistent and explicit associations. "); 
    } 

    if (card1 == ONE && card2 == MANY) 
    { Vector akeys = entity1.getUniqueAttributes(); 
      if (akeys.size() > 0)
      { Attribute akey = (Attribute) akeys.get(0); 
        Attribute bakey = (Attribute) akey.clone(); 
        bakey.setUnique(false); 
        entity2.addAttribute(bakey); 
      } 
      else 
      { System.err.println("No primary key in " + entity1); } 
    }
    else if (card2 == ONE && card1 == MANY) 
    { Vector akeys = entity2.getUniqueAttributes(); 
      if (akeys.size() > 0)
      { Attribute akey = (Attribute) akeys.get(0); 
        Attribute bakey = (Attribute) akey.clone(); 
        bakey.setUnique(false); 
        entity1.addAttribute(bakey); 
      } 
      else 
      { System.err.println("No primary key in " + entity2); } 
    }
    else
    { System.err.println("Not a ONE-MANY association"); } 
  }

  public void removeManyManyAssociation(UCDArea ucdArea)
  { // if association is MANY-MANY, add primary key of both sides as attributes 
    // of new intermediate entity, and ONE-MANY associations to it. 
    // Should only be done for explicit persistent
    // rels. 

    if (isPersistent() && isExplicit()) { } 
    else 
    { System.err.println("Warning: should only apply this transformation to \n" + 
                         "persistent and explicit associations. "); 
    } 

    if (card1 == MANY && card2 == MANY) 
    { Entity e = new Entity(getName() + "Table"); 
      Vector akeys = entity1.getUniqueAttributes();
      Vector bkeys = entity2.getUniqueAttributes();  
      if (akeys.size() > 0)
      { Attribute akey = (Attribute) akeys.get(0); 
        Attribute ekey = (Attribute) akey.clone(); 
        ekey.setUnique(false); 
        e.addAttribute(ekey); 
      } 
      else 
      { System.err.println("No primary key in " + entity1); }
      if (bkeys.size() > 0)
      { Attribute bkey = (Attribute) bkeys.get(0); 
        Attribute ekey2 = (Attribute) bkey.clone(); 
        ekey2.setUnique(false); 
        e.addAttribute(ekey2); 
      } 
      else 
      { System.err.println("No primary key in " + entity2); }  
      String ename = e.getName().toLowerCase(); 
      Association ast1 = new Association(entity1,e,ONE,MANY,role1,ename + "r"); 
      Association ast2 = new Association(e,entity2,MANY,ONE,"",role2);
      entity1.addAssociation(ast1); 
      e.addAssociation(ast2); 
      entity1.removeAssociation(this); 
      ucdArea.addBetween(this,ast1,ast2,e); 
    }
    else
    { System.err.println("Not a MANY-MANY association"); } 
  }

  public static String getOclCode(Vector rels,
                  java.util.Map env,
                  Expression cond, Expression rhs,
                  boolean local)
  { if (rels.size() == 0)
    { return getOcl(env,cond,rhs,local); }
    else 
    { Association ast = (Association) rels.get(0);
      rels.remove(0);
      return ast.getOcl(rels,env,cond,rhs,local);
    }
  }

  public String getOcl(Vector rels,
                  java.util.Map env,
                  Expression cond, Expression rhs, boolean local)
  { String e1name = entity1.getName();
    String e2name = entity2.getName();
    String e1s = e1name.toLowerCase() + "s";
    String var1 = (String) env.get(e1name);
    String var2 = (String) env.get(e2name);

    if (var1 != null && var2 != null) // also restrict to objs in this rel
    { return getOcl(rels,env,cond,rhs,local); }
    else if (var1 != null)
    { String arg; 

      if (card2 != ONE)
      { var2 = e2name.toLowerCase() + "x"; 
        env.put(e2name,var2); 
        arg = getOclCode(rels,env,cond,rhs,local);
        return var1 + "." + role2 + "->forAll(" + var2 + " |\n  " + arg + ")"; } 
      else 
      { var2 = var1 + "." + role2; 
        env.put(e2name,var2); 
        return getOclCode(rels,env,cond,rhs,local); 
      }
    }
    else if (var2 != null)
    { var1 = e1name.toLowerCase() + "x";
      env.put(e1name,var1); 
      String arg = getOclCode(rels,env,cond,rhs,local); 
      if (card2 != ONE) 
      { return e1name + ".allInstances()->forAll(" + var1 + "|\n" + 
               "  " + var1 + "." + role2 + ".includes(" + var2 + ") implies \n" + 
               "    " + arg + ")"; 
      }
      else
      { return e1name + ".allInstances()->forAll(" + var1 + "|\n" + 
               "  " + var1 + "." + role2 + " = " + var2 + " implies \n" + 
               "    " + arg + ")"; 
      } 
    }
    else // both set1 and set2 null
    { var1 = e1name.toLowerCase() + "x";
      env.put(e1name,var1);
      rels.add(0,this); // very dodgy 
      String arg = getOclCode(rels,env,cond,rhs,local);
      return e1name + ".allInstances()->forAll(" + var1 + " | " + arg + ")"; 
    }
  }

  public static String getOcl(java.util.Map env,
                         Expression cond,
                         Expression rhs,boolean local)
  { String r = rhs.toOcl(env,local);
    if (cond != null)
    { String l = cond.toOcl(env,local); 
      
      if ("true".equals(l))
      { return r; } 
      else
      { return l + " implies " + r; }
    } 
    return r; 
  }

  public boolean isClosurable() // for x-* self-associations only
  { if (card2 == ONE) { return false; } 
    if (entity1 == entity2) 
    { return true; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { return true; } 
    else if (Entity.isAncestor(entity2,entity1))
    { return true; } 
    else 
    { return false; }
  }  

  public Entity closureEntity() // for x-* self-associations only
  { if (card2 == ONE) { return null; } 
    if (entity1 == entity2) 
    { return entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { return entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { return entity2; } 
    else 
    { return null; }
  }  

  public String generateClosureOperation() // for x-* self-associations only
  { if (card2 == ONE) { return ""; } 
    Entity sup; 
    if (entity1 == entity2) 
    { sup = entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { sup = entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { sup = entity2; } 
    else { return ""; } 

    String supname = sup.getName();  
    String e1name = entity1.getName(); 
    String e2name = entity2.getName();  // a subclass or superclass of entity1 
    String e1x = e1name.toLowerCase() + "x";  
    String e2x = e2name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String rolex = role2 + "x"; 
    String res = "  public static List closure" + e1name + role2 + "(List _r)\n" +
                 "  { Vector _path = new Vector();\n" +  
                 "    for (int _i = 0; _i < _r.size(); _i++)\n" +  
                 "    { " + e1name + " " + e1x + " = (" + e1name + ") _r.get(_i);\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  

      "  private static void closure" + e1name + role2 + "(" + e1name + " " + e1x + 
                                                ", List _path)\n" + 
      "  { if (_path.contains(" + e1x + ")) { return; }\n" + 
      "    _path.add(" + e1x + ");\n" +  
      "    List " + rolex + " = " + e1x + ".get" + role2 + "();\n" +  
      "    for (int _i = 0; _i < " + rolex + ".size(); _i++)\n" +  
      "    { if (" + rolex + ".get(_i) instanceof " + e1name + ")\n" + 
      "      { " + e1name + " " + e1xx + " = (" + e1name + ") " + 
                                                rolex + ".get(_i);\n" +  
      "        closure" + e1name + role2 + "(" + e1xx + ", _path);\n" +
      "      }\n" + 
      "      else { _path.add(" + rolex + ".get(_i)); }\n" +   
      "    }\n" + 
      "  }\n"; 
    return res; 
  }

  public String generateClosureOperationJava6() // for x-* self-associations only
  { if (card2 == ONE) { return ""; } 
    Entity sup; 
    if (entity1 == entity2) 
    { sup = entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { sup = entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { sup = entity2; } 
    else { return ""; } 

    // these are operations of Set:
    String e1name = entity1.getName(); 
    String e1x = e1name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String rolex = role2 + "x"; 
    String rtype = ""; 
    if (ordered) 
    { rtype = "ArrayList"; } 
    else 
    { rtype = "HashSet"; } 

    String res = "  public static " + rtype + " closure" + e1name + role2 + "(Collection _r)\n" +
                 "  { " + rtype + " _path = new " + rtype + "();\n" +  
                 "    for (Object _i : _r)\n" +  
                 "    { " + e1name + " " + e1x + " = (" + e1name + ") _i;\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +  
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  

      "  private static void closure" + e1name + role2 + "(" + e1name + " " + e1x + 
                                                ", " + rtype + " _path)\n" + 
      "  { if (_path.contains(" + e1x + ")) { return; }\n" + 
      "    _path.add(" + e1x + ");\n" +  
      "    Collection " + rolex + " = " + e1x + ".get" + role2 + "();\n" +  
      "    for (Object _i : " + rolex + ")\n" +  
      "    { if (_i instanceof " + e1name + ")\n" + 
      "      { " + e1name + " " + e1xx + " = (" + e1name + ") _i;\n" +  
      "        closure" + e1name + role2 + "(" + e1xx + ", _path);\n" +
      "      } else { _path.add(_i); }\n" +   
      "    }\n" + 
      "  }\n"; 
    return res; 
  }

  public String generateClosureOperationJava7() // for x-* self-associations only
  { // if (entity1 != entity2) { return ""; } 
    if (card2 == ONE) { return ""; } 
    Entity sup; 
    if (entity1 == entity2) 
    { sup = entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { sup = entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { sup = entity2; } 
    else { return ""; } 

    String supname = sup.getName();  

    String e1name = entity1.getName(); 
    String e2name = entity2.getName(); // these should be equal
    String e1x = e1name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String rolex = role2 + "x"; 
    String rtype = ""; 
    if (ordered) 
    { rtype = "ArrayList<" + supname + ">"; } 
    else if (sortedAsc)
    { rtype = "TreeSet<" + supname + ">"; } 
    else 
    { rtype = "HashSet<" + supname + ">"; } 

    String res = "  public static " + rtype + " closure" + e1name + role2 + "(Collection<" + e1name + "> _r)\n" +
                 "  { " + rtype + " _path = new " + rtype + "();\n" +  
                 "    for (Object _i : _r)\n" +  
                 "    { " + e1name + " " + e1x + " = (" + e1name + ") _i;\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +  
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  

      "  private static void closure" + e1name + role2 + "(" + e1name + " " + e1x + 
                                                ", " + rtype + " _path)\n" + 
      "  { if (_path.contains(" + e1x + ")) { return; }\n" + 
      "    _path.add(" + e1x + ");\n" +  
      "    " + rtype + " " + rolex + " = " + e1x + ".get" + role2 + "();\n" +  
      "    for (Object _i : " + rolex + ")\n" +  
      "    { if (_i instanceof " + e1name + ")\n" + 
      "      { " + e1name + " " + e1xx + " = (" + e1name + ") _i;\n" +  
      "        closure" + e1name + role2 + "(" + e1xx + ", _path);\n" +  
      "      } else { _path.add((" + supname + ") _i); }\n" + 
      "    }\n" + 
      "  }\n"; 
    return res; 
  }

  public String generateClosureOperationCSharp() // for x-* self-associations only
  { if (card2 == ONE) { return ""; } 
    Entity sup; 
    if (entity1 == entity2) 
    { sup = entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { sup = entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { sup = entity2; } 
    else { return ""; } 

    // these are operations of Set:
    String e1name = entity1.getName(); 
    String e1x = e1name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String rolex = role2 + "x"; 
    String res = "  public static ArrayList closure" + e1name + role2 + "(ArrayList _r)\n" +
                 "  { ArrayList _path = new ArrayList();\n" +  
                 "    for (int _i = 0; _i < _r.Count; _i++)\n" +  
                 "    { " + e1name + " " + e1x + " = (" + e1name + ") _r[_i];\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +  
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  

      "  private static void closure" + e1name + role2 + "(" + e1name + " " + e1x + 
                                                ", ArrayList _path)\n" + 
      "  { if (_path.Contains(" + e1x + ")) { return; }\n" + 
      "    _path.Add(" + e1x + ");\n" +  
      "    ArrayList " + rolex + " = " + e1x + ".get" + role2 + "();\n" +  
      "    for (int _i = 0; _i < " + rolex + ".Count; _i++)\n" +  
      "    { if (" + rolex + "[_i] is " + e1name + ")\n" + 
      "      { " + e1name + " " + e1xx + " = (" + e1name + ") " + 
                                                rolex + "[_i];\n" +  
      "        closure" + e1name + role2 + "(" + e1xx + ", _path);\n" +
      "      }\n" + 
      "      else { _path.Add(" + rolex + "[_i]); }\n" +   
      "    }\n" + 
      "  }\n"; 
    return res; 
  }

  public String generateClosureOperationCPP() // for x-* self-associations only
  { if (card2 == ONE) { return ""; } 
    Entity sup; 
    if (entity1 == entity2) 
    { sup = entity1; }
    else if (Entity.isAncestor(entity1,entity2)) 
    { sup = entity1; } 
    else if (Entity.isAncestor(entity2,entity1))
    { sup = entity2; } 
    else { return ""; } 

    String supname = sup.getName();  

    // these are operations of Set:
    String e1name = entity1.getName(); 
    String e2name = entity2.getName(); 
    String e1x = e1name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String e1s = "Controller::inst->" + e1name.toLowerCase() + "_s"; 
    String rolex = role2 + "x"; 
    String res = ""; 
    if (ordered) 
    { res =      "  static vector<" + supname + "*>* closure" + e1name + role2 + 
                                                    "(vector<" + e1name + "*>* _r)\n" +
                 "  { vector<" + supname + "*>* _path = new vector<" + supname + "*>();\n" +  
                 "    for (vector<" + e1name + "*>::iterator _i = _r->begin(); _i != _r->end(); _i++)\n" +  
                 "    { " + e1name + "* " + e1x + " = (*_i);\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +  
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  
      "  static void closure" + e1name + role2 + "(" + e1name + "* " + e1x + 
                                                ", vector<" + supname + "*>* _path)\n" + 
      "  { if (UmlRsdsLib<" + supname + "*>::isIn(" + e1x + ", _path)) { return; }\n" + 
      "    _path->push_back(" + e1x + ");\n" +  
      "    vector<" + e2name + "*>* " + rolex + " = " + e1x + "->get" + role2 + "();\n" +  
      "    for (vector<" + e2name + "*>::iterator _i = " + rolex + "->begin(); _i != " 
                                                              + rolex + "->end(); _i++)\n" +  
      "    { " + e2name + "* " + e1xx + " = (*_i);\n" +  
      "      if (UmlRsdsLib<" + e1name + "*>::isIn(" + e1xx + ", " + e1s + "))\n" + 
      "      { closure" + e1name + role2 + "(" + e1xx + ", _path); }\n" +
      "      else { _path->push_back((" + supname + "*) " + e1xx + "); }\n" +   
      "    }\n" + 
      "  }\n"; 
    } 
    else 
    { res = "  static set<" + supname + "*>* closure" + e1name + role2 + 
                                                    "(set<" + e1name + "*>* _r)\n" +
            "  { set<" + supname + "*>* _path = new set<" + supname + "*>();\n" +  
            "    for (set<" + e1name + "*>::iterator _i = _r->begin(); _i != _r->end(); _i++)\n" +  
            "    { " + e1name + "* " + e1x + " = (*_i);\n" + 
            "      closure" + e1name + role2 + "(" + e1x + ", _path);\n" +  
            "    }\n" +  
            "    return _path;\n" +  
            "  }\n\n" +        
      "  static void closure" + e1name + role2 + "(" + e1name + "* " + e1x + 
                                                ", set<" + supname + "*>* _path)\n" + 
      "  { if (UmlRsdsLib<" + supname + "*>::isIn(" + e1x + ", _path)) { return; }\n" + 
      "    _path->insert(" + e1x + ");\n" +  
      "    set<" + e2name + "*>* " + rolex + " = " + e1x + "->get" + role2 + "();\n" +  
      "    for (set<" + e2name + "*>::iterator _i = " + rolex + "->begin(); _i != " 
                                                              + rolex + "->end(); _i++)\n" +  
      "    { " + e2name + "* " + e1xx + " = (*_i);\n" +  
      "      if (UmlRsdsLib<" + e1name + "*>::isIn(" + e1xx + ", " + e1s + "))\n" + 
      "      { closure" + e1name + role2 + "(" + e1xx + ", _path); }\n" +
      "      else { _path->insert((" + supname + "*) " + e1xx + "); }\n" +   
      "    }\n" + 
      "  }\n"; 
    } 
    return res; 
  }

  public String generateClosure1Operation() // for x-1 self-associations only
  { if (entity1 != entity2) { return ""; } 
    if (card2 != ONE) { return ""; } 
    // these are operations of Set:
    String e1name = entity1.getName(); 
    String e1x = e1name.toLowerCase() + "x";  
    String e1xx = e1x + "x"; 
    String rolex = role2 + "x"; 
    String res = "  public static Vector closure" + e1name + role2 + "(Vector _r)\n" +
                 "  { Vector _path = new Vector();\n" +  
                 "    for (int _i = 0; _i < _r.size(); _i++)\n" +  
                 "    { " + e1name + " " + e1x + " = (" + e1name + ") _r.get(_i);\n" + 
                 "      closure" + e1name + role2 + "(" + e1x + ",_path);\n" +  
                 "    }\n" +  
                 "    return _path;\n" +  
                 "  }\n\n" +  

      "  private static void closure" + e1name + role2 + "(" + e1name + " " + e1x + 
                                                ", Vector _path)\n" + 
      "  { if (_path.contains(" + e1x + ")) { return; }\n" + 
      "    _path.add(" + e1x + ");\n" +  
      "   " + e1name + " " + e1xx + " = (" + e1name + ")" + e1x + ".get" + role2 + "();\n" +  
      "      closure" + role2 + "(" + e1xx + ",_path);\n" +  
      "    }\n" + 
      "  }\n"; 
    return res; 
  }  // for other languages? 


  public String ejbBeanGet()
  { String res = "  public abstract ";
    if (card2 == MANY)
    { res = res + "Collection"; }
    else 
    { res = res + entity2.getName(); }
    res = res + " get";
    String fl = role2.substring(0,1);
    String rem = role2.substring(1,role2.length());
    res = res + fl.toUpperCase() +
          rem + "();\n";
    return res;
  }

  public String ejbBeanSet()
  { String res = "  public abstract void set";
    String fl = role2.substring(0,1);
    String rem = role2.substring(1,role2.length());
    res = res + fl.toUpperCase() +
          rem + "(";
    if (card2 == MANY)
    { res = res + "Collection"; }
    else 
    { res = res + entity2.getName(); }
    res = res + " x);\n";
    return res;
  }

  public static Vector reachableFrom(Vector ents, Vector asts)
  { Vector reachable = new Vector(); 
    for (int i = 0; i < asts.size(); i++) 
    { Association ast = (Association) asts.get(i); 
      if (ents.contains(ast.getEntity1()))
      { reachable.add(ast.getEntity2()); } 
    } 
    return reachable; 
  } 

  public String checkCompletenessOp()
  { String res = ""; 
    
    if (role1 == null) { return res; } 
    if (role1.equals("")) { return res; } 

    String e1name = entity1.getName(); 
    String e2name = entity2.getName(); 
    String e1s = e1name.toLowerCase() + "s"; 
    String e2s = e2name.toLowerCase() + "s"; 
    String e1x = role1 + "_" + e1name.toLowerCase() + "x1"; 
    String e2x = role2 + "_" + e2name.toLowerCase() + "x2"; 
 

    if (card1 != ONE && card2 != ONE) 
    { res = "  for (int _i = 0; _i < " + e1s + ".size(); _i++)\n" + 
            "  { " + e1name + " " + e1x + " = (" + e1name + ") " + e1s + ".get(_i);\n" + 
            "    for (int _j = 0; _j < " + e2s + ".size(); _j++)\n" + 
            "    { " + e2name + " " + e2x + " = (" + e2name + ") " + e2s + ".get(_j);\n" + 
            "      if (" + e1x + ".get" + role2 + "().contains(" + e2x + "))\n" + 
            "      { if (" + e2x + ".get" + role1 + "().contains(" + e1x + ")) { }\n" + 
            "        else { " + e2x + ".add" + role1 + "(" + e1x + "); }\n" + 
            "      }\n" + 
            "      else if (" + e2x + ".get" + role1 + "().contains(" + e1x + "))\n" + 
            "      { " + e1x + ".add" + role2 + "(" + e2x + "); } \n" + 
            "    }\n" + 
            "  }\n"; 
    } 
    else if (card2 == ONE && card1 != ONE) 
    { res = "  for (int _i = 0; _i < " + e1s + ".size(); _i++)\n" + 
            "  { " + e1name + " " + e1x + " = (" + e1name + ") " + e1s + ".get(_i);\n" + 
            "    for (int _j = 0; _j < " + e2s + ".size(); _j++)\n" + 
            "    { " + e2name + " " + e2x + " = (" + e2name + ") " + e2s + ".get(_j);\n" + 
            "      if (" + e1x + ".get" + role2 + "() == " + e2x + ")\n" + 
            "      { if (" + e2x + ".get" + role1 + "().contains(" + e1x + ")) { }\n" + 
            "        else { " + e2x + ".add" + role1 + "(" + e1x + "); }\n" + 
            "      }\n" + 
            "      else if (" + e2x + ".get" + role1 + "().contains(" + e1x + "))\n" + 
            "      { " + e1x + ".set" + role2 + "(" + e2x + "); } \n" + 
            "    }\n" + 
            "  }\n"; 
    } 
    else if (card1 == ONE && card2 != ONE) 
    { res = "  for (int _i = 0; _i < " + e1s + ".size(); _i++)\n" + 
            "  { " + e1name + " " + e1x + " = (" + e1name + ") " + e1s + ".get(_i);\n" + 
            "    for (int _j = 0; _j < " + e2s + ".size(); _j++)\n" + 
            "    { " + e2name + " " + e2x + " = (" + e2name + ") " + e2s + ".get(_j);\n" + 
            "      if (" + e1x + ".get" + role2 + "().contains(" + e2x + "))\n" + 
            "      { if (" + e2x + ".get" + role1 + "() == " + e1x + ") { }\n" + 
            "        else { " + e2x + ".set" + role1 + "(" + e1x + "); }\n" + 
            "      }\n" + 
            "      else if (" + e2x + ".get" + role1 + "() == " + e1x + ")\n" + 
            "      { " + e1x + ".add" + role2 + "(" + e2x + "); } \n" + 
            "    }\n" + 
            "  }\n"; 
    } 
    else if (card1 == ONE && card2 == ONE) 
    { res = "  for (int _i = 0; _i < " + e1s + ".size(); _i++)\n" + 
            "  { " + e1name + " " + e1x + " = (" + e1name + ") " + e1s + ".get(_i);\n" + 
            "    for (int _j = 0; _j < " + e2s + ".size(); _j++)\n" + 
            "    { " + e2name + " " + e2x + " = (" + e2name + ") " + e2s + ".get(_j);\n" + 
            "      if (" + e1x + ".get" + role2 + "() == " + e2x + ")\n" + 
            "      { if (" + e2x + ".get" + role1 + "() == " + e1x + ") { }\n" + 
            "        else { " + e2x + ".set" + role1 + "(" + e1x + "); }\n" + 
            "      }\n" + 
            "      else if (" + e2x + ".get" + role1 + "() == " + e1x + ")\n" + 
            "      { " + e1x + ".set" + role2 + "(" + e2x + "); } \n" + 
            "    }\n" + 
            "  }\n"; 
    } 
   
    return res; 
  }    // and for CSharp and C++

  /* Generation of design, static operation code for managing the association: */ 

public BehaviouralFeature designAddOperation()
{ // static operation of entity1 addE1_role2
  String e1name = entity1.getName();
  String e2name = entity2.getName();
  String e1x = e1name.toLowerCase() + "_x";
  String e2x = e2name.toLowerCase() + "_x";
  BasicExpression ax = new BasicExpression(e1x, 0);
  BasicExpression bx = new BasicExpression(e2x, 0);
  Type e1type = new Type(entity1);
  Type e2type = new Type(entity2);
  ax.setType(e1type);
  bx.setType(e2type);
  Attribute p1 = new Attribute(e1x, e1type, ModelElement.INTERNAL);
  Attribute p2 = new Attribute(e2x, e2type, ModelElement.INTERNAL);
  BehaviouralFeature bf = new BehaviouralFeature("add" + e1name + "_" + role2);
  bf.setStatic(true);
  bf.setQuery(false);
  bf.addParameter(p1);
  bf.addParameter(p2);
  SequenceStatement code = new SequenceStatement();
  code.addStatement(addtest(ax,bx));
  code.addStatement(addcleanup(ax,bx));
  code.addStatement(addinverse(ax,bx));
  BasicExpression axrole2 = new BasicExpression(this);
  axrole2.setObjectRef(ax);
  BinaryExpression rhs = new BinaryExpression("->including", axrole2, bx);
  AssignStatement addrole2 = new AssignStatement(axrole2,rhs);
  code.addStatement(addrole2);
  bf.setActivity(code);
  bf.setOwner(entity1);
  return bf;
}

public BehaviouralFeature designRemoveOperation()
{ // static operation of entity1 removeE1_role2
  String e1name = entity1.getName();
  String e2name = entity2.getName();
  String e1x = e1name.toLowerCase() + "_x";
  String e2x = e2name.toLowerCase() + "_x";
  BasicExpression ax = new BasicExpression(e1x, 0);
  BasicExpression bx = new BasicExpression(e2x, 0);
  Type e1type = new Type(entity1);
  Type e2type = new Type(entity2);
  ax.setType(e1type);
  bx.setType(e2type);
  Attribute p1 = new Attribute(e1x, e1type, ModelElement.INTERNAL);
  Attribute p2 = new Attribute(e2x, e2type, ModelElement.INTERNAL);
  BehaviouralFeature bf = new BehaviouralFeature("remove" + e1name + "_" + role2);
  bf.setStatic(true);
  bf.setQuery(false);
  bf.addParameter(p1);
  bf.addParameter(p2);
  bf.setOwner(entity1);
  SequenceStatement code = new SequenceStatement();
   code.addStatement(reminverse(ax,bx));
  BasicExpression axrole2 = new BasicExpression(this);
  axrole2.setObjectRef(ax);
  BinaryExpression rhs = new BinaryExpression("->excluding", axrole2, bx);
  AssignStatement addrole2 = new AssignStatement(axrole2,rhs);
  code.addStatement(addrole2);
  bf.setActivity(code);
  return bf;
}


public BehaviouralFeature designSetOperation()
{ // static operation of entity1 setE1_role2
  String e1name = entity1.getName();
  String e2name = entity2.getName();
  String e1x = e1name.toLowerCase() + "_x";
  String e2x = e2name.toLowerCase() + "_x";
  BasicExpression ax = new BasicExpression(e1x, 0);
  BasicExpression bx = new BasicExpression(e2x, 0);
  Type e1type = new Type(entity1);
  // Type e2type = new Type(entity2);
  ax.setType(e1type);
  ax.setElementType(e1type);
  Type e2set = getType2(); 
  bx.setType(e2set);
  bx.setElementType(new Type(entity2)); 
  Attribute p1 = new Attribute(e1x, e1type, ModelElement.INTERNAL);
  Attribute p2 = new Attribute(e2x, e2set, ModelElement.INTERNAL);
  p1.setElementType(new Type(entity1)); 
  p2.setElementType(new Type(entity2)); 
  BehaviouralFeature bf = new BehaviouralFeature("assign" + e1name + "_" + role2);
  bf.setStatic(true);
  bf.setQuery(false);
  bf.addParameter(p1);
  bf.addParameter(p2);
  bf.setOwner(entity1);
  // SequenceStatement code = new SequenceStatement();
  // code.addStatement(setcode(ax,bx));
  // BasicExpression axrole2 = new BasicExpression(this);
  // axrole2.setObjectRef(ax);
  // AssignStatement setrole2 = new AssignStatement(axrole2, bx);
  // code.addStatement(setrole2);
  bf.setActivity(setcode(ax,bx));
  return bf;
}


public Statement addtest(BasicExpression ax, BasicExpression bx)
{ SequenceStatement res = new SequenceStatement();
  String e2name = entity2.getName();
  String bxx = e2name.toLowerCase() + "_xx";
  BasicExpression bxxe = new BasicExpression(bxx,0);
  BasicExpression axbr = new BasicExpression(this);
  axbr.setObjectRef(ax);
  if (ordered) {}
  else
  { BinaryExpression test = new BinaryExpression("->includes", axbr, bx);
    ConditionalStatement cs = new ConditionalStatement(test, new ReturnStatement());
    res.addStatement(cs);
  }
  if (card2 == ZEROONE)
  { BasicExpression axbrsize = new BasicExpression("size", 0);
    axbrsize.setObjectRef(axbr);
    BinaryExpression nonempt = new BinaryExpression(">", axbrsize, new BasicExpression(0));
    UnaryExpression axbrany = new UnaryExpression("->any", axbr);
    AssignStatement asst = new AssignStatement(bxxe, axbrany);
    asst.setType(new Type(entity2));
    BinaryExpression removebxx = new BinaryExpression("->excluding", axbr, bxxe);
    AssignStatement rembxx = new AssignStatement(axbr, removebxx);
    SequenceStatement clearbr = new SequenceStatement();
    clearbr.addStatement(asst);
    clearbr.addStatement(rembxx);
    if (role1 != null && role1.length() > 0)
    { BasicExpression bxxar = new BasicExpression(role1,0);
      bxxar.setObjectRef(bxxe);
      BinaryExpression removeax = new BinaryExpression("->excluding", bxxar, ax);
      AssignStatement remax = new AssignStatement(bxxar, removeax);
      clearbr.addStatement(remax);
    }
    ConditionalStatement clear01 = new ConditionalStatement(nonempt, clearbr);
    res.addStatement(clear01);
  }
  return res;
}

public Statement reminverse(BasicExpression ax, BasicExpression bx)
{ SequenceStatement res = new SequenceStatement();
  if (role1 != null && role1.length() > 0)
  { BasicExpression bxxar = new BasicExpression(role1,0);
    bxxar.setObjectRef(bx);
    if (card1 == ONE)
    { BasicExpression nullbe = new BasicExpression("null", 0);
      AssignStatement setnull = new AssignStatement(bxxar, nullbe);
      return setnull;
    }
    else // MANY role1
    { BinaryExpression removeax = new BinaryExpression("->excluding", bxxar, ax);
      AssignStatement remax = new AssignStatement(bxxar, removeax);
      return remax;
    }
  }
  return res;
}

public Statement addinverse(BasicExpression ax, BasicExpression bx)
{ SequenceStatement res = new SequenceStatement();
  if (role1 != null && role1.length() > 0)
  { BasicExpression bxxar = new BasicExpression(role1,0);
    bxxar.setObjectRef(bx);
    if (card1 == ONE)
    { AssignStatement setax = new AssignStatement(bxxar, ax);
      return setax;
    }
    else // MANY role1
    { BinaryExpression addax = new BinaryExpression("->including", bxxar, ax);
      AssignStatement remax = new AssignStatement(bxxar, addax);
      return remax;
    }
  }
  return res;
}

public Statement addcleanup(BasicExpression ax, BasicExpression bx)
{ SequenceStatement res = new SequenceStatement();
  String e1name = entity1.getName();
  String axx = e1name.toLowerCase() + "_xx";
  BasicExpression axxe = new BasicExpression(axx,0);
  Type voidtype = new Type("void", null); 

  if (role1 != null && role1.length() > 0) 
  { BasicExpression bxar = new BasicExpression(role1,0);
    bxar.setObjectRef(bx);
    if (card1 == ONE)
    { BasicExpression nullexp = new BasicExpression("null", 0);
      nullexp.setType(voidtype);
      nullexp.setElementType(voidtype);
      BinaryExpression nulltest = new BinaryExpression("/=", bxar, nullexp);
      BasicExpression bxarbr = new BasicExpression(this);
      bxarbr.setObjectRef(bxar);
      BinaryExpression exclbx = new BinaryExpression("->excluding", bxarbr, bx);
      AssignStatement rembx = new AssignStatement(bxarbr, exclbx);
      ConditionalStatement str = new ConditionalStatement(nulltest, rembx); 
      return str;
    }
    else if (card1 == ZEROONE)
    { BasicExpression bxarsize = new BasicExpression("size", 0);
      bxarsize.setObjectRef(bxar);
      BinaryExpression nonempt = new BinaryExpression(">", bxarsize, new BasicExpression(0));
      UnaryExpression bxarany = new UnaryExpression("->any", bxar);
      AssignStatement asst = new AssignStatement(axxe, bxarany);
      asst.setType(new Type(entity1));
      BinaryExpression removeaxx = new BinaryExpression("->excluding", bxar, axxe);
      AssignStatement remax = new AssignStatement(bxar, removeaxx);
      SequenceStatement clearbr = new SequenceStatement();
      clearbr.addStatement(asst);
      clearbr.addStatement(remax);
      BasicExpression axxbr = new BasicExpression(this);
      axxbr.setObjectRef(axxe);
      BinaryExpression removebx = new BinaryExpression("->excluding", axxbr, bx);
      AssignStatement rembx = new AssignStatement(axxbr, removebx);
      clearbr.addStatement(rembx);
      ConditionalStatement clear01 = new ConditionalStatement(nonempt, clearbr);
      res.addStatement(clear01);
    } 
  }
  else if (card1 == ZEROONE)
  { BasicExpression ainsts = new BasicExpression(entity1);
    BinaryExpression tst = new BinaryExpression(":", axxe, ainsts);
    BasicExpression axxbr = new BasicExpression(this);
    axxbr.setObjectRef(axxe);
    BinaryExpression includesbx = new BinaryExpression("->includes", axxbr, bx);
    BinaryExpression removebx = new BinaryExpression("->excluding", axxbr, bx);
    AssignStatement rembx = new AssignStatement(axxbr, removebx);
    ConditionalStatement bdy = new ConditionalStatement(includesbx, rembx);
    WhileStatement ws = new WhileStatement(tst, bdy);
    ws.setLoopKind(Statement.FOR);
    ws.setLoopRange(axxe, ainsts);
    return ws;
  }
  return res;
}

public Statement setcode(BasicExpression ax, BasicExpression bx)
{ BasicExpression axbr = new BasicExpression(this);
  axbr.setObjectRef(ax);
  axbr.setType(getType2()); 
  axbr.setElementType(new Type(entity2)); 

  String oldrem = role2 + "_old_removed";
  String newadd = role2 + "_new_added";

  BasicExpression oldr = new BasicExpression(oldrem, 0);
  oldr.setType(getType2()); 
  oldr.setElementType(new Type(entity2)); 
  BasicExpression newa = new BasicExpression(newadd, 0);
  newa.setType(getType2()); 
  newa.setElementType(new Type(entity2)); 
  String bxx = role2 + "_xx";
  BasicExpression bxxe = new BasicExpression(bxx,0);
  bxxe.setType(new Type(entity2)); 
  bxxe.setElementType(new Type(entity2)); 
  String bxx2 = role2 + "_xxx";
  BasicExpression bxxe2 = new BasicExpression(bxx2,0);
  bxxe2.setType(new Type(entity2)); 
  bxxe2.setElementType(new Type(entity2)); 
  BasicExpression nulle = new BasicExpression("null", 0);  
    
  if (card2 != ONE)
  { BinaryExpression subtractbrbx = new BinaryExpression("-", axbr, bx);
    BinaryExpression subtractbxbr = new BinaryExpression("-", bx, axbr);
    subtractbrbx.setType(getType2()); 
    subtractbxbr.setType(getType2()); 
    subtractbrbx.setElementType(new Type(entity2)); 
    subtractbxbr.setElementType(new Type(entity2)); 
    AssignStatement orem = new AssignStatement(oldr, subtractbrbx);
    AssignStatement nadd = new AssignStatement(newa, subtractbxbr);
   // types for these
    orem.setType(getType2()); 
    orem.setElementType(new Type(entity2)); 
    nadd.setType(getType2()); 
    nadd.setElementType(new Type(entity2)); 

    SequenceStatement ss = new SequenceStatement();
    ss.addStatement(orem);
    ss.addStatement(nadd);
    BinaryExpression tst1 = new BinaryExpression(":", bxxe, oldr);
    BinaryExpression tst2 = new BinaryExpression(":", bxxe2, newa);
    tst1.setType(new Type("boolean", null)); 
    tst2.setType(new Type("boolean", null)); 
    tst1.setElementType(new Type("boolean", null)); 
    tst2.setElementType(new Type("boolean", null)); 
   
    // BasicExpression aa = new BasicExpression(entity1);
    BasicExpression rembr = new BasicExpression("remove" + entity1.getName() + "_" + role2, 0);
    BasicExpression addbr = new BasicExpression("add" + entity1.getName() + "_" + role2, 0);
    rembr.setType(new Type("void", null)); 
    addbr.setType(new Type("void", null)); 
    rembr.setElementType(new Type("void", null));
    addbr.setElementType(new Type("void", null));
    Vector pars = new Vector();
    pars.add(ax); pars.add(bxxe);
    rembr.setParameters(pars);
    Vector pars2 = new Vector(); 
    pars2.add(ax); pars2.add(bxxe2); 
    addbr.setParameters(pars2);
    InvocationStatement remcall = new InvocationStatement(rembr); // static
    InvocationStatement addcall = new InvocationStatement(addbr);
    WhileStatement ws1 = new WhileStatement(tst1, remcall);
    ws1.setLoopKind(Statement.FOR);
    ws1.setLoopRange(bxxe, oldr);
    ss.addStatement(ws1);
    WhileStatement ws2 = new WhileStatement(tst2, addcall);
    ws2.setLoopKind(Statement.FOR);
    ws2.setLoopRange(bxxe2, newa);
    ss.addStatement(ws2);
    System.out.println(ss); 
    return ss;
  }
  else if (card2 == ONE && card1 != ONE) // && (role1 != null && role1.length() > 0)
  { BinaryExpression neq1 = new BinaryExpression("/=", axbr, bx);
    BinaryExpression neq2 = new BinaryExpression("/=", axbr, nulle);
    BinaryExpression and12 = new BinaryExpression("&", neq1, neq2);
    SequenceStatement remaddss = new SequenceStatement();
    // BasicExpression bb = new BasicExpression(entity2);
    BasicExpression remar = new BasicExpression("remove" + entity2.getName() + "_" + role1, 0);
    BasicExpression addar = new BasicExpression("add" + entity2.getName() + "_" + role1, 0);
    // remar.setObjectRef(bb);
    // addar.setObjectRef(bb);
    Vector pars1 = new Vector();
    pars1.add(axbr); pars1.add(ax);
    Vector pars2 = new Vector();
    pars2.add(bx); pars2.add(ax);
    remar.setParameters(pars1);
    addar.setParameters(pars2);
    remaddss.addStatement(new InvocationStatement(remar));
    remaddss.addStatement(new InvocationStatement(addar));
    ConditionalStatement cs = new ConditionalStatement(and12, remaddss);
    return cs;
  }
  else // card2 == ONE && card1 == ONE
  { BasicExpression bxar = new BasicExpression(role1, 0);
    bxar.setObjectRef(bx);
    BasicExpression bxarbr = new BasicExpression(this);
    bxarbr.setObjectRef(bxar);
    BinaryExpression cond1 = new BinaryExpression("/=", bxar, nulle);
    AssignStatement as1 = new AssignStatement(bxarbr, nulle);
    ConditionalStatement cs1 = new ConditionalStatement(cond1, as1);
    BasicExpression axbrar = new BasicExpression(role1, 0);
    axbrar.setObjectRef(axbr);
    BinaryExpression cond2 = new BinaryExpression("/=", axbr, nulle);
    AssignStatement as2 = new AssignStatement(axbrar, nulle);
    ConditionalStatement cs2 = new ConditionalStatement(cond2, as2);
    SequenceStatement res = new SequenceStatement();
    res.addStatement(cs1); 
    res.addStatement(cs2);
    AssignStatement fas = new AssignStatement(axbr, bx);
    AssignStatement ras = new AssignStatement(bxar, ax);
    res.addStatement(fas);
    res.addStatement(ras);
    return res;
  }
}

public Statement delete1Op(Entity e1)
{ // entity1 == e1, the entity
  // with instance being deleted

  String e1name = entity1.getName();
  String e1lower = e1name.toLowerCase();
  Type e1type = new Type(entity1);
  Type e2type = new Type(entity2);
  String e2name = entity2.getName();
  String e2lower = e2name.toLowerCase();
  Type voidtype = new Type("void", null);
  BasicExpression ent2 = new BasicExpression(entity2);
  BasicExpression nullexp = new BasicExpression("null", 0);
  nullexp.setType(voidtype);
  nullexp.setElementType(voidtype);

  SequenceStatement res = new SequenceStatement();

  BasicExpression e2xrole1 = new BasicExpression(role1, 0);
  BasicExpression e2x = new BasicExpression(e2lower + "_xx", 0);
  e2x.setType(e2type);
  e2x.setElementType(e2type);
  e2xrole1.setObjectRef(e2x);
  BasicExpression e1x = new BasicExpression(e1lower + "_x", 0);
  e1x.setType(e1type);
  e1x.setElementType(e1type);
  BasicExpression e2insts = new BasicExpression(entity2); 
  BinaryExpression tst = new BinaryExpression(":", e2x, e2insts);
    
  if (card1 == ONE)
  { e2xrole1.setType(e1type);
    e2xrole1.setElementType(e1type);
    
    String e2_r1_deleted = e2lower + "_" + role1 + "_deleted1";
    BasicExpression e2r1del = new BasicExpression(e2_r1_deleted, 0);
    Type settype = new Type("Set", null);
    settype.setElementType(e2type);
    e2r1del.setType(settype);
    e2r1del.setElementType(e2type);
    BinaryExpression cnd = new BinaryExpression("=", e2xrole1, e1x); 
    SequenceStatement ss = new SequenceStatement();
    CreationStatement cree1del = new CreationStatement("Set", e2_r1_deleted);
    cree1del.setType(settype);
    cree1del.setElementType(e2type);

    System.out.println("SET TYPE IS " + settype); 

    AssignStatement setnull = new AssignStatement(e2xrole1, nullexp);
    ss.addStatement(setnull);
    BinaryExpression addel = new BinaryExpression("->including", e2r1del, e2x);
    addel.setType(settype);
    addel.setElementType(e2type);
    AssignStatement adddel = new AssignStatement(e2r1del, addel);
    ss.addStatement(adddel);
    ConditionalStatement cs = new ConditionalStatement(cnd, ss);
    WhileStatement ws = new WhileStatement(tst, cs);
    ws.setLoopKind(Statement.FOR);
    ws.setLoopRange(e2x, e2insts);

    res.addStatement(cree1del);
    res.addStatement(ws);
    BasicExpression bx = new BasicExpression(e2lower + "_xx", 0);
    bx.setType(e2type);
    bx.setElementType(e2type);
    BasicExpression kille2x = new BasicExpression("kill" + e2name, 0);
    kille2x.setObjectRef(ent2);
    kille2x.addParameter(bx);
    kille2x.umlkind = Expression.UPDATEOP;
    kille2x.setIsEvent();
    InvocationStatement killbx = new InvocationStatement(kille2x);
    BinaryExpression tst2 = new BinaryExpression(":", bx, e2r1del); 
    WhileStatement ws2 = new WhileStatement(tst2, killbx);
    ws2.setLoopKind(Statement.FOR);
    ws2.setLoopRange(bx, e2r1del);
 
    res.addStatement(ws2);
    BasicExpression freeex = new BasicExpression("free", 0);
    freeex.addParameter(e2r1del);
    freeex.setIsEvent(); 
    freeex.umlkind = Expression.UPDATEOP;
    InvocationStatement destroyex = new InvocationStatement(freeex);
    res.addStatement(destroyex); 
 
    return res;
  } // also free(e2x);
  else
  { BinaryExpression excle1x = new BinaryExpression("->excluding", e2xrole1, e1x);
    AssignStatement reme1x = new AssignStatement(e2xrole1, excle1x);
    WhileStatement ws3 = new WhileStatement(tst, reme1x);
    ws3.setLoopKind(Statement.FOR);
    ws3.setLoopRange(e2x, e2insts);
    return ws3;
  } // also free(e1x)
}

public Statement deleteAggregationOp(Entity e1, BasicExpression ex)
{ // entity1 == e1, the entity
  // with instance being deleted.
  // All linked E2 instances are
  // also deleted. ex : E1

  String e2name = entity2.getName();
  String e2lower = e2name.toLowerCase();
  Type e2type = new Type(entity2);
  BasicExpression ent2 = new  BasicExpression(entity2);
  BasicExpression nullexp = new BasicExpression("null", 0);
  Type voidtype = new Type("void", null);
  nullexp.setType(voidtype);
  nullexp.setElementType(voidtype);
  SequenceStatement res = new SequenceStatement();

  BasicExpression e1xrole2 = new BasicExpression(this);
  e1xrole2.setObjectRef(ex);
 
  if (card2 == ONE)
  { BasicExpression kille2x1 = new BasicExpression("kill" + e2name, 0);
    kille2x1.umlkind = Expression.UPDATEOP;
    kille2x1.setIsEvent();   
    kille2x1.setObjectRef(ent2);
    kille2x1.addParameter(e1xrole2);
    InvocationStatement killbx1 = new InvocationStatement(kille2x1);
    res.addStatement(killbx1);
  }    
  else
  { BasicExpression bx = new BasicExpression(e2lower + "_xx", 0);
    bx.setType(e2type);
    bx.setElementType(e2type);
    BasicExpression kille2x = new BasicExpression("kill" + e2name, 0);
    kille2x.addParameter(bx);    
    kille2x.umlkind = Expression.UPDATEOP;
    kille2x.setIsEvent();   
    kille2x.setObjectRef(ent2);
    InvocationStatement killbx = new InvocationStatement(kille2x);
    BinaryExpression tst2 = new BinaryExpression(":", bx, e1xrole2); 
    WhileStatement ws2 = new WhileStatement(tst2, killbx);
    ws2.setLoopKind(Statement.FOR);
    ws2.setLoopRange(bx, e1xrole2);
    res.addStatement(ws2);
  }
  return res;
}

public Statement delete2Op(Entity e2)
{ // entity2 == e2, the entity
  // with instance being deleted

  String e1name = entity1.getName();
  String e1lower = e1name.toLowerCase();
  String e2name = entity2.getName();
  String e2lower = e2name.toLowerCase();
  Type e1type = new Type(entity1);
  BasicExpression ent1 = new BasicExpression(entity1);
  Type voidtype = new Type("void", null);
  BasicExpression nullexp = new BasicExpression("null", 0);
  nullexp.setType(voidtype);
  nullexp.setElementType(voidtype);
  SequenceStatement res = new SequenceStatement();

  BasicExpression e1xrole2 = new BasicExpression(this);
  BasicExpression e1x = new BasicExpression(e1lower + "_xx", 0);
  e1x.setType(e1type); 
  e1x.setElementType(e1type); 

  e1xrole2.setObjectRef(e1x);
  BasicExpression e2x = new BasicExpression(e2lower + "_x", 0);
  BasicExpression e1insts = new BasicExpression(entity1); 

  BinaryExpression tst = new BinaryExpression(":", e1x, e1insts);
    
  if (card2 == ONE)
  { String e1_r2_deleted = e1lower + "_" + role2 + "_deleted";
    BasicExpression e1r2del = new BasicExpression(e1_r2_deleted, 0);
    Type settype = new Type("Set", null);
    settype.setElementType(e1type);
    e1r2del.setType(settype);
    e1r2del.setElementType(e1type);
    
    BinaryExpression cnd = new BinaryExpression("=", e1xrole2, e2x); 
    SequenceStatement ss = new SequenceStatement();
    CreationStatement cree1del = new CreationStatement("Set", e1_r2_deleted);
    cree1del.setType(settype);
    cree1del.setElementType(e1type);
    res.addStatement(cree1del);

    // System.out.println("SET TYPE IS " + settype); 

    AssignStatement setnull = new AssignStatement(e1xrole2, nullexp);
    ss.addStatement(setnull);
    BinaryExpression addel = new BinaryExpression("->including", e1r2del, e1x);
    addel.setType(settype);
    addel.setElementType(e1type);
    AssignStatement adddel = new AssignStatement(e1r2del, addel);
    ss.addStatement(adddel);
    ConditionalStatement cs = new ConditionalStatement(cnd, ss);
    WhileStatement ws = new WhileStatement(tst, cs);
    ws.setLoopKind(Statement.FOR);
    ws.setLoopRange(e1x, e1insts);
 
    res.addStatement(ws);
    BasicExpression ax = new BasicExpression(e1lower + "_xx", 0);
    ax.setType(e1type); 
    ax.setElementType(e1type); 
    BasicExpression kille1x = new BasicExpression("kill" + e1name, 0);
    kille1x.setObjectRef(ent1);
    kille1x.addParameter(ax);
    kille1x.setIsEvent(); 
    kille1x.umlkind = Expression.UPDATEOP;
    
    InvocationStatement killax = new InvocationStatement(kille1x);
    BinaryExpression tst2 = new BinaryExpression(":", ax, e1r2del); 
    WhileStatement ws2 = new WhileStatement(tst2, killax);
    ws2.setLoopKind(Statement.FOR);
    ws2.setLoopRange(ax, e1r2del);
 
    res.addStatement(ws2);  
    BasicExpression freeex = new BasicExpression("free", 0);
    freeex.addParameter(e1r2del);
    freeex.setIsEvent(); 
    freeex.umlkind = Expression.UPDATEOP;
    InvocationStatement destroyex = new InvocationStatement(freeex);
    res.addStatement(destroyex); 
 
    return res;
  } // also free(e2x);
  else 
  { BinaryExpression excle2x = new BinaryExpression("->excluding", e1xrole2, e2x);
    AssignStatement reme2x = new AssignStatement(e1xrole2, excle2x);
    WhileStatement ws3 = new WhileStatement(tst, reme2x);
    ws3.setLoopKind(Statement.FOR);
    ws3.setLoopRange(e1x, e1insts);
    return ws3;
  } // also free(e2x)
}



}
