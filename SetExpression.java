import java.util.Vector;
import java.io.*; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: OCL */ 

public class SetExpression extends Expression
{ private Vector elements = new Vector(); // Expression
  boolean ordered = false; // true for sequences

  public SetExpression() { }

  public SetExpression(boolean b) 
  { ordered = b; }

  public SetExpression(Vector v)
  { if (v == null || v.size() == 0 ||
        (v.get(0) instanceof Expression))
    { elements = v; }
    else
    { for (int i = 0; i < v.size(); i++)
      { String ss = v.get(i) + "";
        elements.add(new BasicExpression(ss));
      }
    }
  } // For a map, the elements are BinaryExpressions representing pairs "," key value
  

  public SetExpression(Vector v,boolean ord)
  { this(v); 
    ordered = ord; 
  }

  public static SetExpression newMapSetExpression()
  { SetExpression res = new SetExpression(); 
    res.setType(new Type("Map", null)); 
    return res; 
  } 

  public Expression getExpression(int i) 
  { if (i < elements.size())
    { return (Expression) elements.get(i); } 
    return null; 
  } 

  public int size()
  { return elements.size(); } 

  public Expression definedness()
  { Expression res = new BasicExpression(true);   // conjunction of definedness of elements
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res = Expression.simplifyAnd(res,elem.definedness());  
    } 
    return res; 
  } 

  public Expression determinate()
  { Expression res = new BasicExpression(true);  // conjunction of definedness of elements
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res = Expression.simplifyAnd(res,elem.determinate());  
    } 
    return res; 
  } 

  public void setPre() 
  { for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      elem.setPre();  
    } 
  } 

  public Expression checkConversions(Type propType, Type propElemType, java.util.Map interp) 
  { Vector argres = new Vector();
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      argres.add(elem.checkConversions(propType, propElemType, interp));  
    } 
    return new SetExpression(argres,ordered); 
  }  

  public Expression addPreForms(String var)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression ne = elem.addPreForms(var); 
      newelems.add(ne); 
    } 
    SetExpression result = new SetExpression(newelems,ordered);
	result.setType(type); 
	result.setElementType(elementType); 
	return result;  
  } 

  public Expression removePrestate()
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression ne = elem.removePrestate(); 
      newelems.add(ne); 
    } 
    Expression res = new SetExpression(newelems,ordered);
    res.setType(type); 
    res.setElementType(elementType); 
    return res;  
  } 

  public void findClones(java.util.Map clones, String rule, String op)
  { return; } 

  public boolean isEmpty()
  { return elements.size() == 0; }

  public boolean isSingleton()
  { return elements.size() == 1; }

  public boolean isOrdered()
  { return ordered; }

  public boolean isOrderedB()
  { return ordered; }
  
  public boolean isMap()
  { return type != null && "Map".equals(type.getName()); }

  public void setOrdered(boolean ord)
  { ordered = ord; } 

  public Expression getElement(int i)
  { if (i < 0 || i >= elements.size())
    { return new BasicExpression("Invalid"); } 
    return (Expression) elements.get(i); 
  }

  public Vector getElements()
  { return elements; } 

  public Expression getLastElement()
  { int i = elements.size(); 
    if (i == 0) { return new BasicExpression("Invalid"); } 
    else 
    { return (Expression) elements.get(i-1); } 
  } 

  public Expression getFirstElement()
  { int i = elements.size(); 
    if (i == 0) { return new BasicExpression("Invalid"); } 
    else 
    { return (Expression) elements.get(0); } 
  } 

  public void addElement(Expression e)
  { elements.add(e); }

  public void addElement(int i, Expression e)
  { elements.add(i,e); }

  public void addMapElement(Expression lhs, Expression rhs)
  { Expression maplet = new BinaryExpression("|->", lhs, rhs); 
    elements.add(maplet); 
  } 

  public String toString()
  { String res;

    if (isMap())
	{ res = "Map{"; }
    else if (ordered) 
    { res = "Sequence{"; } 
    else 
    { res = "Set{"; }

    for (int i = 0; i < elements.size(); i++)
    { res = res + elements.get(i);
      if (i < elements.size() - 1)
      { res = res + ","; }
    }
    res = res + "}";
    return res;
  }

  public Expression invertEq(BasicExpression left) 
  { // inverts  left = this   as  elements(0) = left[1] & ... 
    Expression res = new BasicExpression(true); 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BasicExpression ind = new BasicExpression(i+1); 
      BasicExpression leftcopy = (BasicExpression) left.clone(); 
      leftcopy.setArrayIndex(ind); 
      BinaryExpression eqi = new BinaryExpression("=", e, leftcopy);
      res = Expression.simplifyAnd(res,eqi); 
    } 
    return res; 
  }  

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("collectionexpression_");
    out.println(res + " : CollectionExpression"); 
    out.println(res + ".expId = \"" + res + "\""); 
    out.println(res + ".isOrdered = " + ordered); 

    for (int i = 0; i < elements.size(); i++)
    { Expression expr = (Expression) elements.get(i);
      String exprid = expr.saveModelData(out); 
      out.println(exprid + " : " + res + ".elements"); 
    }

    if (type != null) 
    { String tname = type.getUMLModelName(out); 
      out.println(res + ".type = " + tname); 
    } 

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(res + ".elementType = " + etname); 
    } 

    out.println(res + ".needsBracket = " + needsBracket); 
    out.println(res + ".umlKind = " + umlkind); 
    // out.println(res + ".prestate = " + prestate); 
        
    return res;
  }

  public String toOcl(java.util.Map env, boolean local)
  { String res;
    if (isMap())
	{ res = "Map{"; }
    else if (ordered) 
    { res = "Sequence{"; } 
    else 
    { res = "Set{"; }

    for (int i = 0; i < elements.size(); i++)
    { res = res + ((Expression) elements.get(i)).toOcl(env,local);
      if (i < elements.size() - 1)
      { res = res + ","; }
    }
    res = res + "}";
    return res;
  }

  public String toZ3()   // use List
  { String res = "nil";

    for (int i = elements.size() - 1; 0 <= i; i--)
    { res = "(insert " + ((Expression) elements.get(i)).toZ3() + " " + res + ")"; }

    return res;
  }
  

  public String toSQL() // invalid
  { return "/* Invalid for SQL */"; } 

  public boolean isMultiple()
  { return true; } 

  public boolean isPrimitive()
  { return false; } 

  public Expression skolemize(Expression sourceVar, java.util.Map env)
  { return this; } 

  /* TODO: add operations for MAPS. */ 
  
  public String queryForm(java.util.Map env, boolean local)
  { if (isMap())
    { String result = "(new HashMap())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.queryForm(env,local) + "," + 
		                           value.queryForm(env,local) + ")";
      }
	  return result; 
	}
	
    String res = "(new SystemTypes.Set())"; 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = res + ".add(" + wrap(elementType, e.queryForm(env,local)) + ")";
    }
    res = res + ".getElements()"; 
    return res; 
  }  // different for sequences?

  public String queryFormJava6(java.util.Map env, boolean local)
  { if (isMap())
    { String result = "(new HashMap())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.queryFormJava6(env,local) + "," + 
		                           value.queryFormJava6(env,local) + ")";
      }
	  return result; 
	}
	
	String res = "(new HashSet())"; 
    if (ordered) 
    { res = "(new ArrayList())"; } 

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (ordered) 
      { res = "Set.addSequence(" + res + ", " + e.queryFormJava6(env,local) + ")"; } 
      else 
      { res = "Set.addSet(" + res + ", " + e.queryFormJava6(env,local) + ")"; } 
    }

    return res; 
  }  // different for sequences?

  public String queryFormJava7(java.util.Map env, boolean local)
  { 
    if (isMap())
    { String mtype = type.getJava7(elementType); 
      String result = "(new " + mtype + "())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "Ocl.includingMap(" + result + "," + key.queryFormJava7(env,local) + "," + 
		                           value.queryFormJava7(env,local) + ")";
      }
	  return result; 
	}
	
	if (type == null)
    { if (ordered)
      { type = new Type("Sequence",null); }
      else
      { type = new Type("Set",null); } 
    } 
    String jType = type.getJava7(elementType); 

    String res = "(new " + jType + "())"; 
    
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (e != null) 
      { String wexp = wrap(elementType, e.queryFormJava7(env,local)); 
        if (ordered) 
        { res = "Ocl.addSequence(" + res + ", " + wexp + ")"; } 
        else 
        { res = "Ocl.addSet(" + res + ", " + wexp + ")"; }
      }  
    }

    return res; 
  }  // different for sequences?

  public String queryFormCSharp(java.util.Map env, boolean local)
  { if (isMap())
    { String result = "(new Hashtable())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "System.includingMap(" + result + "," + key.queryFormCSharp(env,local) + "," + 
		                              value.queryFormCSharp(env,local) + ")";
      }
	  return result; 
    }
  
    String res = "(new ArrayList())"; 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = "SystemTypes.addSet(" + res + "," + 
                Expression.wrapCSharp(elementType, e.queryFormCSharp(env,local)) + ")";
    }
    return res; 
  }   

  public String queryFormCPP(java.util.Map env, boolean local)
  { Type et = getElementType(); 
    String cet = "void*"; 
    if (et != null) 
    { cet = et.getCPP(et.getElementType()); } 

    if (isMap())
    { String result = "(new map<string," + cet + ">())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "UmlRsdsLib<" + cet + ">::includingMap(" + result + "," + key.queryFormCPP(env,local) + "," + 
		                           value.queryFormCPP(env,local) + ")";
      }
	  return result; 
	}
	
	String collkind = "Set"; 
    String res = "(new set<" + cet + ">())";
    if (ordered) 
    { res = "(new vector<" + cet + ">())"; 
      collkind = "Sequence"; 
    } 
 
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res = "UmlRsdsLib<" + cet + ">::add" + collkind + 
               "(" + res + "," + e.queryFormCPP(env,local) + ")";
    }
    return res; 
  }  

  public BExpression bqueryForm(java.util.Map env)
  { Vector elems = new Vector();
    if (elements.size() == 1)
    { Expression elem = (Expression) elements.get(0);
      BExpression belem = elem.bqueryForm(env);
      if ((belem instanceof BSetExpression) || belem.setValued())
      { return belem; }
      else
      { BSetExpression bsete = new BSetExpression();
        bsete.addElement(belem);
        bsete.setOrdered(ordered); 
        return bsete;
      }
    }
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.bqueryForm(env);
      elems.add(be);
    }
    
    return new BSetExpression(elems,ordered);
  } // maps?

  public BExpression bqueryForm()
  { Vector elems = new Vector();
    if (elements.size() == 1)
    { Expression elem = (Expression) elements.get(0);
      BExpression belem = elem.bqueryForm();
      if ((belem instanceof BSetExpression) || belem.setValued())
      { return belem; }
      else
      { BSetExpression bsete = new BSetExpression();
        bsete.addElement(belem);
        bsete.setOrdered(ordered); 
        return bsete;
      }
    }
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.bqueryForm();
      elems.add(be);
    }
    
    return new BSetExpression(elems,ordered);
  } // maps? 

  public int minModality()
  { int mm = 9;
    if (elements.size() == 0)
    { return 2; } // sensor

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(0);
      int mmx = e.minModality();
      if (mmx < mm)
      { mm = mmx; }
    }
    return mm;
  }

  public int maxModality()
  { int mm = 0;
    if (elements.size() == 0)
    { return 2; } // sensor

    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(0);
      int mmx = e.maxModality();
      if (mmx > mm)
      { mm = mmx; }
    }
    return mm;
  }

  public Vector metavariables()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.metavariables();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector allPreTerms()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.allPreTerms();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector allPreTerms(String var)
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.allPreTerms(var);
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector innermostEntities()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.innermostEntities();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }

  public Vector getBaseEntityUses()
  { Vector pres = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Vector epres = e.getBaseEntityUses();
      pres = VectorUtil.union(pres,epres);
    }
    return pres;
  }


  public String updateForm(String language, java.util.Map env, String op, String val, Expression var, boolean local)
  { // update   this = var  to this, val is
    // query form of var. 
    String res = "";

   if (isOrdered())
   { // if (i < var.size) { elemi := var[i] }
     for (int i = 0; i < elements.size(); i++)
     { Expression elem = (Expression) elements.get(i);
       BasicExpression vari = (BasicExpression) var.clone();
       vari.setArrayIndex(new BasicExpression(i+1));
       BinaryExpression seti = new BinaryExpression("=", elem, vari );
       UnaryExpression varsize = new UnaryExpression("->size", var );
       BinaryExpression se = new BinaryExpression(">", varsize, new BasicExpression(i));
       res = res + "  if (" + se.queryForm(language,env,local) + ") { " + seti.updateForm(language,env,local) + " }\n";
    }
   }  
   else 
   { // if (i < var.size) { elemi := (var -  Set{elem1, ..., elemi-1})->any() }
     for (int i = 0; i < elements.size(); i++)
     { Expression elem = (Expression) elements.get(i);
       BasicExpression vari = (BasicExpression) var.clone();
       SetExpression prev = subrange(1,i-1);
       BinaryExpression subt = new BinaryExpression("-", vari, prev);
       UnaryExpression varelem = new UnaryExpression("->any", subt); 
       BinaryExpression seti = new BinaryExpression("=", elem, varelem );
       UnaryExpression varsize = new UnaryExpression("->size", var );
       BinaryExpression se = new BinaryExpression(">", varsize, new BasicExpression(i));
       res = res + "  if (" + se.queryForm(language,env,local) + ") { " +  seti.updateForm(language,env,local) + " }\n";
     }
   }  
   return res;
 } // For maps???

  public SetExpression subrange(int i, int j)
  { SetExpression res = new SetExpression();
     for (int k = i-1; k < elements.size() && k < j; k++)
     { Expression e = (Expression) elements.get(k);
       res.addElement(e);
     }
     res.setOrdered(isOrdered());
     return res;
  }

  public String updateForm(java.util.Map env,boolean local)
  { return "{} // no update form for: " + this; } 

  public String updateForm(java.util.Map env, String val2)
  { return "{} // no update form for: " + this; }

  public BExpression binvariantForm(java.util.Map env, boolean local)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      BExpression be = e.binvariantForm(env,local);
      elems.add(be);
    }
    return new BSetExpression(elems,ordered);
  }

  public BExpression binvariantForm(java.util.Map env,
                                    BExpression v)
  { return new BBasicExpression("/* not valid */"); }  // ???

  public BStatement bupdateForm(java.util.Map env,boolean local)
  { return new BBasicStatement("skip"); /* not valid */ }

  public int typeCheck(final Vector sms)
  { return SENSOR; }

  public boolean typeCheck(final Vector types, final Vector entities,
                           final Vector contexts, final Vector env)
  { boolean res = true;
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      e.typeCheck(types,entities,contexts,env);
      Entity eent = e.getEntity(); 
      if (entity == null)
      { entity = eent; } 
      else if (eent != null && Entity.isAncestor(eent,entity))
      { entity = eent; } // most general entity of the elements
    }
    // deduce element type and type itself, and the entity??

    if (isMap())
    { type = new Type("Map", null); } 
    else if (ordered)
    { type = new Type("Sequence",null); }
    else
    { type = new Type("Set",null); } 

    elementType = Type.determineType(elements); 
    type.setElementType(elementType); 
    if (elementType == null) 
    { System.out.println("Warning: cannot determine element type of " + this);
      elementType = new Type("OclAny", null); 
    } 

    umlkind = VALUE; // ???
    multiplicity = ModelElement.MANY; 
    return res && (entity != null || elementType != null);
  }

  public Entity findEntity()
  { if (entity != null) 
    { return entity; } 
    
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Entity eent = e.getEntity(); 
      if (entity == null)
      { entity = eent; 
        System.out.println(">> Warning!!: No entity for: " + e); 
      } 
      else if (eent != null && Entity.isAncestor(eent,entity))
      { entity = eent; } // most general entity of the elements
    }

    return entity; 
  } 


  public Vector allEntitiesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res = VectorUtil.union(res,val.allEntitiesUsedIn());
    }
    return res;
  }

  public Vector allAttributesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res = VectorUtil.union(res,val.allAttributesUsedIn());
    }
    return res;
  }

  public boolean relevantOccurrence(String op, Entity ent, String f,
                                    String val)
  { return false; }


  public Vector getVariableUses()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      res.addAll(val.getVariableUses());
    }
    return res;
  }

  public Vector allFeaturesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allFeaturesUsedIn());
    }
    return res;
  }

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allOperationsUsedIn());
    }
    return res;
  }

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.equivalentsUsedIn());
    }
    return res;
  }

  public Vector allValuesUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.allValuesUsedIn());
    }
    return res;
  }

  public Vector allBinarySubexpressions() { return new Vector(); }

  public DataDependency rhsDataDependency()
  { return new DataDependency(); }  // ???

  public DataDependency getDataItems()
  { return new DataDependency(); }  // ???

  public Expression substitute(Expression old,
                               Expression n)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Expression be = e.substitute(old,n);
      elems.add(be);
    }
    SetExpression result = new SetExpression(elems,ordered);
	if (isMap())
	{ result.setType(type); }
	return result; 
  }

  public Expression substituteEq(String old,
                                 Expression n)
  { Vector elems = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      Expression be = e.substituteEq(old,n);
      elems.add(be);
    }
    SetExpression result = new SetExpression(elems,ordered);
	if (isMap())
	{ result.setType(type); }
	return result; 
  }

  public boolean isOrExpression() { return false; }

  public Expression createActionForm(final Vector v)
  { return this; }

  public String toJava()
  { if (isMap())
    { String result = "(new HashMap())"; 
	  for (int i = 0; i < elements.size(); i++)
      { BinaryExpression e = (BinaryExpression) elements.get(i);
	    Expression key = e.getLeft(); 
		Expression value = e.getRight(); 
        result = "Set.includingMap(" + result + "," + key.toJava() + "," + 
		                           value.toJava() + ")";
      }
	  return result; 
	}
	
    String res = "(new SystemTypes.Set())";
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      String val = e.toJava();
      res = res + ".add(" + wrap(elementType, val) + ")";
    }
    return res + ".getElements()";
  }  // ordered? Maps? 

  public String toB() { return ""; }

  public Expression toSmv(Vector cnames) { return null; }

  public String toImp(final Vector comps)
  { return ""; }

  public String toJavaImp(final Vector comps)
  { return toJava(); }

  public Expression buildJavaForm(final Vector comps)
  { return new BasicExpression(toJava()); }

  public boolean hasVariable(final String s)
  { for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      if (e.hasVariable(s))
      { return true; }
    }
    return false;
  }

  public Vector variablesUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.variablesUsedIn(s));
    }
    return res;
  }

  public Vector getUses(String feature)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.getUses(feature));
    }
    return res;
  }

  public Vector componentsUsedIn(final Vector s)
  { Vector res = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression e = (Expression) elements.get(i);
      res.addAll(e.componentsUsedIn(s));
    }
    return res;
  }

  Maplet findSubexp(final String var)
  { return null; } // new Maplet(null,this) ???

  public Expression simplify(final Vector vars)
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = val.simplify(vars);
      newvals.add(newval);
    }
	SetExpression result = new SetExpression(newvals,ordered);
	if (isMap())
	{ result.setType(type); }
	return result; 
  } // could eliminate duplicates

  public Expression simplify()
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = val.simplify();
      newvals.add(newval);
    }
	SetExpression result = new SetExpression(newvals,ordered);
	if (isMap())
	{ result.setType(type); }
	return result; 
  } // could eliminate duplicates for ordered == false

  public Expression filter(final Vector vars)
  { return null; } // ???

  public Object clone()
  { Vector newvals = new Vector();
    for (int i = 0; i < elements.size(); i++)
    { Expression val = (Expression) elements.get(i);
      Expression newval = (Expression) val.clone();
      newvals.add(newval);
    }
    SetExpression res = new SetExpression(newvals,ordered);
    res.type = type; 
    res.elementType = elementType; 
    res.ordered = ordered; 
	res.formalParameter = formalParameter; 
	// if (isMap())
	// { res.setType(type); }
    return res; 
  }

  public Vector splitAnd(final Vector comps)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Vector splitOr(final Vector comps)
  { Vector res = new Vector();
    res.add(clone());
    return res;
  }

  public Expression expandMultiples(final Vector sms)
  { return this; }

  public Expression removeExpression(final Expression e)
  { if (e.equals(this))
    { return null; }
    else
    { return this; }
  }

  public boolean implies(final Expression e)
  { return equals(e); } // or a subformula?

  public boolean consistentWith(final Expression e)
  { return equals(e); } // <: consis with =, etc.

  public boolean selfConsistent(final Vector vars)
  { return true; }

  public boolean subformulaOf(final Expression e)
  { if (equals(e)) { return true; }
    if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e;
      return subformulaOf(be.left) ||
             subformulaOf(be.right);
    }
    return false;
  }

  public Expression computeNegation4succ(final Vector as)
  { return null; }  // should never be used

  public Vector computeNegation4ante(final Vector as)
  { return new Vector(); }  // should never be used

  public boolean conflictsWith(Expression e)
  { return false; } 

  public Expression invert()
  { return this; } 

  public Expression dereference(BasicExpression ref)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.dereference(ref); 
      newelems.add(newelem); 
    } 
    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered; 
	res.type = type; 
    return res;  
  } 

  public Expression addReference(BasicExpression ref, Type t)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.addReference(ref,t); 
      newelems.add(newelem); 
    } 
    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
	res.type = type;  
    return res;  
  } 

  public Expression replaceReference(BasicExpression ref, Type t)
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      Expression newelem = elem.replaceReference(ref,t); 
      newelems.add(newelem); 
    } 
    SetExpression res = new SetExpression(newelems);
    res.ordered = ordered;
	res.type = type;  
    return res;  
  } 

  public Vector innermostVariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i); 
      res.addAll(elem.innermostVariables()); 
    } 
    return res;  
  } 

  public Expression featureSetting(String var, String k, Vector l)
  { return null; } 

  public int syntacticComplexity() 
  { int res = 0;
    for (int i = 0; i < elements.size(); i++) 
    { Expression elem = (Expression) elements.get(i);  
      res = res + elem.syntacticComplexity();
    } 
    return res + 1; 
  }  

  public int cyclomaticComplexity()
  { return 0; } 

  public void changedEntityName(String oldN, String newN)
  { } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    String arg = "";
    for (int x = 0; x < elements.size(); x++)
    { Expression elem = (Expression) elements.get(x);
      String txt = elem.cg(cgs);
      arg = arg + txt;
      if (x < elements.size() - 1)
      { arg = arg + ","; }
    }
    args.add(arg);
    CGRule r = cgs.matchedSetExpressionRule(this,etext);

    if (r != null)
    { System.out.println(">> Found set expression rule " + r + " for: " + etext); 
      String res = r.applyRule(args);
      if (needsBracket) 
      { return "(" + res + ")"; } 
      else 
      { return res; }
    }
    return etext;
  }

}

