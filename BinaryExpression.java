import java.util.Vector; 
import java.io.*; 
import javax.swing.JOptionPane; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
// package: OCL 

class BinaryExpression extends Expression 
{ // Represents binary expressions:   left operator right

  Expression left; 
  Expression right; 
  String operator; 
  Expression objectRef; // just for B

  String iteratorVariable = null; // for ->exists( x, ->forAll( x, etc
  Attribute accumulator = null;      // let and ->iterate(  )
  Expression keyValue = null;   // for map operators ->including(key,right), ->excluding(key,right)

  public BinaryExpression(String op, Expression ll, Expression rr)
  { operator = op; 

    if ("<>".equals(op) || op.equals("!=")) 
    { operator = "/="; } 
    else if ("->one".equals(op)) 
    { operator = "->exists1"; } 
    // else if ("div".equals(op)) 
    // { operator = "/"; } 

    left = ll; 
    right = rr; 
  } // change <> to /=, ->one to ->exists1, etc

  public static BinaryExpression newBinaryExpression(String op, Expression lx, Expression rx)
  { if (lx == null || rx == null) 
    { return null; } 
    return new BinaryExpression(op,lx,rx); 
  } 

  public Object clone()
  { BinaryExpression res = 
      new BinaryExpression(operator,(Expression) left.clone(),(Expression) right.clone()); 
    res.iteratorVariable = iteratorVariable; 
    res.accumulator = accumulator; 
    res.keyValue = keyValue;
    res.type = type; 
    res.elementType = elementType; 
    res.entity = entity; 
    res.umlkind = umlkind; 
    res.multiplicity = multiplicity; 
    res.modality = modality; 
    res.needsBracket = needsBracket;
    res.formalParameter = formalParameter;  
    return res; 
  } 

  /* 
  public Object clone()
  { BinaryExpression be = new BinaryExpression(operator,
                          (Expression) left.clone(),
                          (Expression) right.clone()); 
    if (javaForm != null) 
    { be.javaForm = (Expression) javaForm.clone(); }
    be.modality = modality;  // surely?
    be.type = type;
    be.entity = entity;
    be.elementType = elementType;
    be.multiplicity = multiplicity;
    be.umlkind = umlkind; 
    be.setBrackets(needsBracket); 
    return be; 
  } // is this sufficient? 
   */ 

  public BinaryExpression(Attribute att)
  { left = new BasicExpression(att); 
    right = new BasicExpression(att.getType()); 
    operator = ":"; 
    type = new Type("boolean", null); 
  } 

  public BinaryExpression(BasicExpression be)
  { // obj.op(par) becomes obj->op(par)

    left = ((BasicExpression) be).getObjectRef(); 
    right = (Expression) ((Vector) be.getParameters()).get(0); 
    operator = "->" + be.getData(); 
    type = be.getType();
    elementType = be.getElementType();  
  } 

  public void setLeft(Expression le) { left = le; } 

  public void setRight(Expression re) { right = re; } 

  public void setKeyValue(Expression key) { keyValue = key; } 

  public void setOperator(String op) { operator = op; } 

  public Expression getLeft() { return left; } 

  public Expression getRight() { return right; } 

  public String getOperator() { return operator; } 

  public Expression getKeyValue() { return keyValue; } 

  public BasicExpression getVariable()
  { if (iteratorVariable == null) 
    { return null; }  
    BasicExpression res = new BasicExpression(iteratorVariable); 
    res.umlkind = VARIABLE; 
    res.setType(elementType); // except for any, when it is type
    if ("|A".equals(operator))
    { res.setType(type); } 
    return res; 
  } 

  public Expression definedness()
  { Expression dl = left.definedness();
    Expression dr = right.definedness();
    Expression res = simplify("&",dl,dr,null);  // simplifyAnd(dl,dr); 
      
    if ("/".equals(operator) || 
        "mod".equals(operator) || "div".equals(operator)) 
    { Expression zero = new BasicExpression(0);
      Expression neqz = new BinaryExpression("/=",right,zero);
      return simplify("&",res,neqz,null);
    }
    // and case for ->pow
    if ("->pow".equals(operator))
    { Expression zero = new BasicExpression(0);
      Expression eqz = new BinaryExpression("=",left,zero);
      Expression geqz = new BinaryExpression(">",right,zero); 
      Expression pex = new BinaryExpression("=>",eqz,geqz);
	  pex.setBrackets(true);  
      return simplify("&",res,pex,null);
    }  // left < 0  =>  right->oclIsTypeOf("int")

    if ("->at".equals(operator))
    { if ("String".equals(right.getType() + ""))
      { UnaryExpression kexp = new UnaryExpression("->keys", left); 
        Expression inkeys = new BinaryExpression(":", right, kexp); 
        return simplify("&",res,inkeys,null); 
      } 
      UnaryExpression selfsize = new UnaryExpression("->size", left); 
      Expression lbnd = new BinaryExpression("<=", new BasicExpression(1), right); 
      Expression ubnd = new BinaryExpression("<=", right, selfsize); 
      Expression inrange = new BinaryExpression("&",lbnd,ubnd); 
      return simplify("&",res,inrange,null);
    } 
    return res;
  }

  public Expression determinate()
  { if ("or".equals(operator))
    { return new BasicExpression(false); }
    if ("#".equals(operator) || "#LC".equals(operator) || "#1".equals(operator) || "!".equals(operator) ||
        "|".equals(operator) || "|R".equals(operator) || "|C".equals(operator))
    { Expression leftdet = ((BinaryExpression) left).right.determinate(); 
      Expression rightdet = right.determinate(); 
      return (new BinaryExpression("&",leftdet,rightdet)).simplify(); 
    } 
    if ("->exists".equals(operator) || "=".equals(operator) ||
        "->existsLC".equals(operator) || 
        "->forAll".equals(operator) || "->exists1".equals(operator) ||
        "->select".equals(operator) || "->reject".equals(operator) ||
        "->collect".equals(operator) || "=>".equals(operator) || "->closure".equals(operator) || 
        "->isUnique".equals(operator) || "->unionAll".equals(operator) ||
        "->intersectAll".equals(operator) || "->symmetricDifference".equals(operator) ||
        "->selectMaximals".equals(operator) || "->sortedBy".equals(operator) ||
        "->selectMinimals".equals(operator) || "->at".equals(operator) || 
        "->apply".equals(operator))
    { Expression leftdet = left.determinate(); 
      Expression rightdet = right.determinate(); 
      return (new BinaryExpression("&",leftdet,rightdet)).simplify(); 
    } 

    /* if (":".equals(operator) ||
        "/:".equals(operator) || "<:".equals(operator) ||
        "->includes".equals(operator) || "->excludes".equals(operator) ||
        "->includesAll".equals(operator) || "->excludesAll".equals(operator) ||
        "->including".equals(operator) || "->excluding".equals(operator) ||
        "->count".equals(operator) || "->prepend".equals(operator) ||
        "->indexOf".equals(operator) || "->hasPrefix".equals(operator) ||
        "->hasSuffix".equals(operator) ||
        "->hasMatch".equals(operator) ||
        "->isMatch".equals(operator) ||
 "/<:".equals(operator))
    { return new BasicExpression(true); }
    // provided left, right have no common features */ 

    if ("&".equals(operator) && isCaseExpression())
    { return caseDisjointness(); }
    else // if ("&".equals(operator))
    { Expression dand = 
        new BinaryExpression("&",left.determinate(),right.determinate());
      return dand.simplify();
    }
    // union, intersection, etc? 
    // return new BasicExpression("false",0);
  }

  public Expression firstConjunct()
  { if ("&".equals(operator))
    { return left.firstConjunct(); } 
    return this; 
  } 

  public Expression removeFirstConjunct()
  { if ("&".equals(operator))
    { Expression lrem = left.removeFirstConjunct(); 
      return simplify("&",lrem,right,null); 
    } 
    return new BasicExpression(true); 
  } 

  public void setPre()
  { left.setPre(); 
    right.setPre(); 
  } 

  public Expression removePrestate()
  { Expression dl = left.removePrestate();
    Expression dr = right.removePrestate();
    Expression res = new BinaryExpression(operator,dl,dr); 
    res.setType(type); 
    res.setElementType(elementType); 
	res.needsBracket = needsBracket; 
	res.formalParameter = formalParameter; 
    return res; 
  } 



  public Expression checkConversions(Type propType, Type propElemType, java.util.Map interp) 
  { /* if (type != null && type.isEntity())
    { Entity ent = type.getEntity(); 
      if (ent.hasStereotype("source"))
      { Entity tent = (Entity) interp.get(ent + ""); 

        System.out.println("INTERPRETATION OF " + ent + " IS= " + tent); 

        if (tent == null) // use the expected target type instead
        { if (propType != null && propType.isEntity())
          { tent = propType.getEntity(); } 
        }  */ 

     Expression leftres = null; 
     Expression rightres = null; 

     if ("boolean".equals(type + "") || "int".equals(type + "") || "long".equals(type + "") || 
         "double".equals(type + "") || "String".equals(type + ""))
     { return this; } 

     if (operator.equals("->exists") || operator.equals("->forAll") ||
         "->existsLC".equals(operator) || "#LC".equals(operator) ||
         operator.equals("#") || operator.equals("!") || operator.equals("->exists1") ||
         operator.equals("#1") || operator.equals("and") || operator.equals("or"))
     { return this; } // intended as a boolean value

     if (operator.equals("->select") || operator.equals("->reject") || operator.equals("->collect") ||
         operator.equals("|") || operator.equals("|R") || operator.equals("|C")) 
     { if (elementType != null && elementType.isEntity())
       { Entity ent = elementType.getEntity(); 
         if (ent.hasStereotype("source"))
         { Entity tent = (Entity) interp.get(ent + ""); 
           if (tent == null) 
           { if (propElemType != null && propElemType.isEntity())
             { tent = propElemType.getEntity(); } 
             else 
             { return this; } // no interpretation
           }  
           BasicExpression res = new BasicExpression(tent + ""); 
           res.setType(type); 
           res.setMultiplicity(multiplicity); 
           res.setElementType(new Type(tent)); 
           res.setUmlKind(Expression.CLASSID); 
           BinaryExpression ind = 
             new BinaryExpression("->collect",this,new BasicExpression("$id"));
           ind.setType(type);  
           ind.setElementType(new Type("String",null)); 
           res.setArrayIndex(ind); 
           return res; 
         }
       } 
     }  
 
     if (operator.equals("^") || operator.equals("\\/") || 
         operator.equals("->at") || operator.equals("->apply") || 
         operator.equals("/\\") || operator.equals("->union") ||
         operator.equals("->intersection") || operator.equals("->symmetricDifference")) 
     { leftres = left.checkConversions(propType, propElemType, interp); 
       rightres = right.checkConversions(propType, propElemType, interp); 
       return new BinaryExpression(operator,leftres,rightres); 
     } 
     else if (operator.equals("->append") || operator.equals("->including") ||
              operator.equals("->prepend") || operator.equals("->excluding") ||
              operator.equals("->excludingAt") ||
              operator.equals("->excludingFirst"))
     { leftres = left.checkConversions(propType, propElemType, interp); 
       rightres = right.checkConversions(propElemType, propElemType, interp); 
       return new BinaryExpression(operator,leftres,rightres); 
     } 
     // if ->append, ->prepend, ->including expect a single elem

     if (elementType != null && elementType.isEntity())
     { Entity ent = elementType.getEntity(); 
       if (ent.hasStereotype("source"))
       { Entity tent = (Entity) interp.get(ent + ""); 
         if (tent == null) 
         { if (propElemType != null && propElemType.isEntity())
           { tent = propElemType.getEntity(); } 
           else 
           { return this; } // no interpretation
         }  
         BasicExpression res = new BasicExpression(tent + ""); 
         res.setType(type); 
         res.setMultiplicity(multiplicity); 
         res.setElementType(new Type(tent)); 
         res.setUmlKind(Expression.CLASSID); 
         BinaryExpression ind = 
             new BinaryExpression("->collect",this,new BasicExpression("$id"));
         ind.setType(type);  
         ind.setElementType(new Type("String",null)); 
         res.setArrayIndex(ind); 
         return res; 
       }
     } 
     return this; 
  } 

  public Expression replaceModuleReferences(UseCase uc) 
  { Expression rr = right.replaceModuleReferences(uc); 
    Expression ll = left.replaceModuleReferences(uc); 
    Expression res = new BinaryExpression(operator,ll,rr);
    res.setType(type); 
    res.setElementType(elementType); 
    res.umlkind = umlkind; 
	res.formalParameter = formalParameter; 
    return res;  
  } 

  private boolean isForallExists()
  { if (operator.equals("!") && (right instanceof BinaryExpression)) 
    { BinaryExpression br = (BinaryExpression) right; 
      if (br.operator.equals("#") || "#LC".equals(br.operator) || br.operator.equals("#1"))
      { if (br.right instanceof BinaryExpression) 
        { return true; }
      }  
    } 
    return false; 
  } 

  private boolean isCaseExpression()
  { boolean res = "&".equals(operator);
    BinaryExpression beleft;
    BinaryExpression beright;
    if (res && left instanceof BinaryExpression &&
        right instanceof BinaryExpression)
    { beleft = (BinaryExpression) left;
      beright = (BinaryExpression) right;
    }
    else
    { return false; }

    if ("=>".equals(beleft.operator) && "=>".equals(beright.operator))
    { return res; }
    if ("=>".equals(beleft.operator) && beright.isCaseExpression())
    { return res; }
    if ("=>".equals(beright.operator) && beleft.isCaseExpression())
    { return res; }
    return false;
  }

  private Vector caseConditions()
  { Vector res = new Vector();
    BinaryExpression beleft;
    BinaryExpression beright;
    if (left instanceof BinaryExpression &&
        right instanceof BinaryExpression)
    { beleft = (BinaryExpression) left;
      beright = (BinaryExpression) right;
    }
    else
    { return res; }

    if ("=>".equals(beleft.operator) && "=>".equals(beright.operator))
    { res.add(beleft.left); 
      res.add(beright.left);
      return res; 
    }
    if ("=>".equals(beleft.operator) && beright.isCaseExpression())
    { res.add(beleft.left);
      res.addAll(beright.caseConditions());
      return res;
    }
    if ("=>".equals(beright.operator) && beleft.isCaseExpression())
    { res.addAll(beleft.caseConditions());
      res.add(beright.left);
      return res;
    }
    return res;
  }

  private Vector cases(Vector caselist)
  { Vector res = new Vector();
    BinaryExpression beleft;
    BinaryExpression beright;
    if (left instanceof BinaryExpression &&
        right instanceof BinaryExpression)
    { beleft = (BinaryExpression) left;
      beright = (BinaryExpression) right;
    }
    else
    { return res; }

    if ("=>".equals(beleft.operator) && "=>".equals(beright.operator))
    { res.add(beleft.left); 
      res.add(beright.left);
      caselist.add(beleft); 
      caselist.add(beright); 
      return res; 
    }
    if ("=>".equals(beleft.operator) && beright.isCaseExpression())
    { res.add(beleft.left);
      caselist.add(beleft); 
      res.addAll(beright.cases(caselist));
      return res;
    }
    if ("=>".equals(beright.operator) && beleft.isCaseExpression())
    { res.addAll(beleft.cases(caselist));
      res.add(beright.left);
      caselist.add(beright); 
      return res;
    }
    return res;
  }

  private Expression caseDisjointness()
  { Vector cases = caseConditions();
    Expression res = new BasicExpression(true);
    for (int i = 0; i < cases.size(); i++)
    { Expression e1 = (Expression) cases.get(i);
      for (int j = i + 1; j < cases.size(); j++)
      { Expression e2 = (Expression) cases.get(j);
        Expression neg = e2.computeNegation4succ();
		Expression next = new BinaryExpression("=>",e1,neg); 
		next.setBrackets(true); 
        res = simplify("&",res,next,null);
      }
    }
    return res;
  }

  private Expression caseCompleteness(Entity ent)
  { Vector caselist = new Vector(); 
    Vector caseconds = cases(caselist);
    Expression res = new BasicExpression(true);
    for (int i = 0; i < caselist.size(); i++)
    { BinaryExpression cse = (BinaryExpression) caselist.get(i); 
      Expression e1 = (Expression) caseconds.get(i); 
      Expression ce = cse.completeness(ent);
      Expression next = new BinaryExpression("=>",e1,ce);
	  next.setBrackets(true);  
      res = simplify("&",res,next,null);
    }
    return res;
  } // and disjunction of guards is true


  public Expression completeness(Entity ent)
  { Expression res = new BasicExpression(true);
    if (operator.equals("or"))
    { Expression cleft = left.completeness(ent);
      Expression cright = right.completeness(ent);
      res = simplify("&",cleft,cright,null);
      return res;
    }
    if (operator.equals("&") && isCaseExpression())
    { return caseCompleteness(ent); }  // /\i Ei => completeness(casei)
    else
    { Vector v = updateFeatures();
      Vector feats = ent.allDefinedFeatures();
      if (v.containsAll(feats))  // complete
      { // System.out.println("Expression: " + this + " complete.");
        return res;
      }
      else
      { System.out.println("Expression: " + this + " incomplete: updates " + v + 
                           ", not all defined features: " + feats);
        return new BasicExpression(false);
      }
    } 
  }

  public Expression addPreForms(String v)
  { Expression l1 = left.addPreForms(v); 
    Expression r1 = right.addPreForms(v); 
    Expression res = new BinaryExpression(operator,l1,r1); 
    res.needsBracket = needsBracket; 
	res.formalParameter = formalParameter; 
    return res; 
  } 

  public Vector updateFeatures()
  { Vector uv = updateVariables();
    Vector res = new Vector();
    for (int i = 0; i < uv.size(); i++)
    { Expression e = (Expression) uv.get(i);
      if (e instanceof BasicExpression)
      { res.add(((BasicExpression) e).data); }
    }
    return res;
  }

  public String toZ3()
  { String res = "";
    String op = operator; 
    if (operator.startsWith("->")) 
    { op = operator.substring(2,operator.length()); } 

    String leftz3 = left.toZ3(); 
    String rightz3 = right.toZ3(); 
    String memberEnt = ""; 

    Type membt = right.elementType;
    if (membt != null) 
    { Entity e = membt.getEntity();  
      memberEnt = e + ""; 
    } 
      
    if (operator.equals("!"))  // Should be no # or #1 remaining
    { res = "(forall ((" + ((BinaryExpression) left).left + " " + 
                           ((BinaryExpression) left).right + ")) " + rightz3 + ")"; 
      return res; 
    } 
    if (operator.equals("&"))
    { return "(and " + leftz3 + " " + rightz3 + ")"; } 
    if (operator.equals("->at") || operator.equals("->apply"))
    { return "(" + leftz3 + " " + rightz3 + ")"; } 
    if (operator.equals(":"))
    { return "(member" + memberEnt + " " + leftz3 + " " + rightz3 + ")"; } 
    if (operator.equals("/:"))
    { return "(not (member" + memberEnt + " " + leftz3 + " " + rightz3 + "))"; } 
    if (operator.equals("->includes"))
    { return "(member" + memberEnt + " " + rightz3 + " " + leftz3 + ")"; } 
    if (operator.equals("->excludes"))
    { return "(not (member" + memberEnt + " " + rightz3 + " " + leftz3 + "))"; } 
    if (operator.equals("->including") || operator.equals("->prepend"))
    { return "(insert " + rightz3 + " " + leftz3 + ")"; } 
 
    return "(" + op + " " + leftz3 + " " + rightz3 + ")"; 
  } 

 
  public String toOcl(java.util.Map env, boolean local)
  { String re = right.toOcl(env,local); 
    
    if ("#LC".equals(operator) || operator.equals("#"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->exists(" + lle + " | " + re + ")"; 
    }

    if (operator.equals("#1"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->one(" + lle + " | " + re + ")"; 
    }
 
    if (operator.equals("!"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->forAll(" + lle + " | " + re + ")"; 
    } 

    if (operator.equals("|"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->select(" + lle + " | " + re + ")"; 
    } 

    if (operator.equals("|R"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->reject(" + lle + " | " + re + ")"; 
    } 

    if (operator.equals("|C"))
    { String lle = ((BinaryExpression) left).left.toOcl(env,local); 
      String lri = ((BinaryExpression) left).right.toOcl(env,local);
      return lri + "->collect(" + lle + " | " + re + ")"; 
    } 

    String le = left.toOcl(env,local); 
    if (operator.equals("=>"))
    { return le + " implies " + re; } 
    if (operator.equals("&"))
    { return le + " and " + re; } 
    if (operator.equals(":"))
    { return re + "->includes(" + le + ")"; } 
    if (operator.equals("/:"))
    { return re + "->excludes(" + le + ")"; } 
    if (operator.equals("<:"))
    { return re + "->includesAll(" + le + ")"; } 
    if (operator.equals("/<:"))
    { return "not(" + re + "->includesAll(" + le + "))"; }
    if (operator.equals("\\/") || operator.equals("^"))
    { return le + "->union(" + re + ")"; } 
    if (operator.equals("/\\"))
    { return le + "->intersection(" + re + ")"; } 

    if (operator.equals("->including") && keyValue != null) 
    { return le + "->including(" + keyValue.toOcl(env,local) + ", " + re + ")"; } 
    if (operator.equals("->excluding") && keyValue != null) 
    { return le + "->excluding(" + keyValue.toOcl(env,local) + ", " + re + ")"; } 


    if (operator.startsWith("->"))
    { return le + operator + "(" + re + ")"; } // always brackets on re

    return le + " " + operator + " " + re; 
  } // - on collections is excludesAll in OCL? Plus set < elem, etc

  public String saveModelData(PrintWriter out) 
  { String id = Identifier.nextIdentifier("binaryexpression_");
    out.println(id + " : BinaryExpression"); 
    out.println(id + ".expId = \"" + id + "\""); 

    String op = operator; 
    Expression lft = left; 

    if (operator.equals("#"))
    { op = "->exists"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if ("#LC".equals(operator))
    { op = "->existsLC"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("!"))
    { op = "->forAll"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("#1"))
    { op = "->exists1"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|"))
    { op = "->select"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|R"))
    { op = "->reject"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|C"))
    { op = "->collect"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|unionAll"))
    { op = "->unionAll"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|intersectAll"))
    { op = "->intersectAll"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|concatenateAll"))
    { op = "->concatenateAll"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|selectMaximals"))
    { op = "->selectMaximals"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|selectMinimals"))
    { op = "->selectMinimals"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("|sortedBy"))
    { op = "->sortedBy"; 
      lft = ((BinaryExpression) left).right;  
      out.println(id + ".variable = \"" + ((BinaryExpression) left).left + "\""); 
    } 
    else if (operator.equals("->exists") || operator.equals("->exists1") || 
             "->existsLC".equals(operator) || 
             operator.equals("->forAll") || operator.equals("->select") ||
             operator.equals("->reject") || operator.equals("->collect"))
    { out.println(id + ".variable = \"self\""); } 
    // maybe not a good idea to use self here. 

    if (operator.equals("->including") && keyValue != null) 
    { String varid = keyValue.saveModelData(out); 
      out.println(id + ".variable = " + varid);
    } 
    else if (operator.equals("->excluding") && keyValue != null) 
    { String varid = keyValue.saveModelData(out); 
      out.println(id + ".variable = " + varid);
    } 

    out.println(id + ".operator = \"" + op + "\""); 
    String leftid = lft.saveModelData(out); 
    String rightid = right.saveModelData(out); 
    
    out.println(id + ".left = " + leftid); 
    out.println(id + ".right = " + rightid); 

    String tname = "void"; 
    if (type != null) 
    { tname = type.getUMLModelName(out); } 
    out.println(id + ".type = " + tname); 
 

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(id + ".elementType = " + etname); 
    } 
    else 
    { out.println(id + ".elementType = " + tname); } 

    out.println(id + ".needsBracket = " + needsBracket); 
    out.println(id + ".umlKind = " + umlkind); 
    // out.println(id + ".prestate = " + prestate); 
        
    return id; 
  } 
  
  public Vector mutantOperators(String op)
  { Vector res = new Vector(); 
    if (":".equals(op))
    { res.add("/:"); }
    else if ("/:".equals(op))
    { res.add(":"); }
    else if ("<".equals(op))
    { res.add("<="); }
    else if (">".equals(op))
    { res.add(">="); }
    else if ("->including".equals(op))
    { res.add("->excluding"); }
    else if ("->excluding".equals(op))
    { res.add("->including"); }
    else if ("->includes".equals(op))
    { res.add("->excludes"); }
    else if ("->excludes".equals(op))
    { res.add("->includes"); }
    else if ("->select".equals(op))
    { res.add("->reject"); }
    else if ("->reject".equals(op))
    { res.add("->select"); }
    else if ("->before".equals(op))
    { res.add("->after"); }
    else if ("->after".equals(op))
    { res.add("->before"); }
    else if ("->hasPrefix".equals(op))
    { res.add("->hasSuffix"); }
    else if ("->hasSuffix".equals(op))
    { res.add("->hasPrefix"); }
    else if ("->restrict".equals(op))
    { res.add("->antirestrict"); } 
    else if ("->antirestrict".equals(op))
    { res.add("->restrict"); } 
    else if (left.isNumeric() && right.isNumeric() && op.equals("+"))
    { res.add("-"); 
      res.add("*"); 
    }
    else if (left.isNumeric() && right.isNumeric() && op.equals("-"))
    { res.add("+"); }
	
    res.add(op); 
    return res; 
  } // ->compareTo: swap left & right. 
  
  public Vector mutants()
  { Vector lms = left.mutants(); 
    Vector rms = right.mutants(); 
    Vector res = new Vector(); 
    Vector mutantOps = mutantOperators(operator); 
	
    for (int i = 0; i < lms.size(); i++) 
    { Expression lm = (Expression) lms.get(i); 
      for (int j = 0; j < rms.size(); j++) 
      { Expression rm = (Expression) rms.get(j); 
        for (int k = 0; k < mutantOps.size(); k++) 
        { String mop = (String) mutantOps.get(k); 
          BinaryExpression mutant = (BinaryExpression) this.clone(); 
          mutant.operator = mop; 
          mutant.left = lm; 
          mutant.right = rm;  
          if (VectorUtil.containsEqualString(mutant + "",res)) { } 
          else 
          { res.add(mutant); } 
        } 
	 }
     }
     System.out.println(">>> All mutants of " + this + " are: " + res); 
     return res; 
  }

  public Vector singleMutants()
  { Vector lms = left.singleMutants(); 
    Vector rms = right.singleMutants(); 
    Vector res = new Vector(); 
    Vector mutantOps = mutantOperators(operator); 
	
    for (int i = 0; i < lms.size(); i++) 
    { Expression lm = (Expression) lms.get(i); 
      BinaryExpression mutant = (BinaryExpression) this.clone(); 
      mutant.operator = operator; 
      mutant.left = lm; 
      mutant.right = right;  
      if (VectorUtil.containsEqualString(mutant + "",res)) { } 
      else 
      { res.add(mutant); } 
    } 

    for (int j = 0; j < rms.size(); j++) 
    { Expression rm = (Expression) rms.get(j); 
      BinaryExpression mutant = (BinaryExpression) this.clone(); 
      mutant.operator = operator; 
      mutant.left = left; 
      mutant.right = rm;  
      if (VectorUtil.containsEqualString(mutant + "",res)) { } 
      else 
      { res.add(mutant); } 
    } 

    for (int k = 0; k < mutantOps.size(); k++) 
    { String mop = (String) mutantOps.get(k); 
      BinaryExpression mutant = (BinaryExpression) this.clone(); 
      mutant.operator = mop; 
      mutant.left = left; 
      mutant.right = right;  
      if (VectorUtil.containsEqualString(mutant + "",res)) { } 
      else 
      { res.add(mutant); } 
    } 

    Vector selfs = new Vector(); 
    selfs.add(this); 
    res.removeAll(selfs); 
     
    System.out.println(">>> Single mutants of " + this + " are: " + res); 
    return res; 
  }
 
  public String toString()
  { String basicString = 
             left + " " + operator + " " + right; 
     
    if ("let".equals(operator) && accumulator != null)
    { String res = "let " + accumulator.getName() + " : " + accumulator.getType() + " = " + left + " in (" + right + ")"; 
      if (needsBracket)
      { return "(" + res + ")"; }
      return res; 
    }  

    if (operator.equals("#"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->exists( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if ("#LC".equals(operator))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->existsLC( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("#1"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 
      basicString = rangestring + "->exists1( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    } 
    else if (operator.equals("!"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 
      basicString = rangestring + "->forAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    } 
    else if (operator.equals("|"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->select( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|C"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->collect( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|A"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->any( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|R"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->reject( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|selectMinimals"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->selectMinimals( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|selectMaximals"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->selectMaximals( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|unionAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->unionAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|intersectAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->intersectAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|concatenateAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->concatenateAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|sortedBy"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->sortedBy( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|isUnique"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->isUnique( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("->including") && keyValue != null) 
    { basicString = left + "->including(" + keyValue + ", " + right + ")"; } 
    else if (operator.equals("->excluding") && keyValue != null) 
    { basicString = left + "->excluding(" + keyValue + ", " + right + ")"; } 
    else if (operator.equals("->includesAll") || operator.equals("->collect") || 
             operator.equals("->excludesAll") || operator.equals("->append") ||
             operator.equals("->prepend") || operator.equals("->select") || 
             operator.equals("->reject") || operator.equals("->including") ||
             operator.equals("->excluding") ||
             operator.equals("->excludingAt") ||
             operator.equals("->excludingFirst") ||
             operator.equals("->includes") ||
             operator.equals("->excludes") || operator.equals("->pow") || 
             operator.equals("->sortedBy") || operator.equals("->hasSuffix") ||
             operator.equals("->hasPrefix") || "->isUnique".equals(operator) ||
             operator.equals("->oclAsType") || "->forAll".equals(operator) ||
             "->exists".equals(operator) || "->exists1".equals(operator) || 
             "->existsLC".equals(operator) || "->any".equals(operator) ||
             operator.equals("->selectMaximals") || operator.equals("->oclIsKindOf") || 
             operator.equals("->oclIsTypeOf") || operator.equals("->includesKey") || 
             operator.equals("->excludesKey") || operator.equals("->includesValue") || 
             operator.equals("->excludesValue") || 
             operator.equals("->restrict") ||
             operator.equals("->antirestrict") ||  
             operator.equals("->selectMinimals") || operator.equals("->union") ||
             operator.equals("->intersectAll") || operator.equals("->unionAll") ||
             operator.equals("->count") || operator.equals("->indexOf") || operator.equals("->lastIndexOf") || 
             operator.equals("->equalsIgnoreCase") || operator.equals("->before") || operator.equals("->after") || 
             operator.equals("->hasMatch") || operator.equals("->isMatch") ||
             operator.equals("->split") || operator.equals("->allMatches") || operator.equals("->firstMatch") || 
             operator.equals("->at") || 
             operator.equals("->apply") ||
             operator.equals("->closure") || 
             operator.equals("->intersection") || operator.equals("->symmetricDifference"))   
    { basicString = left + operator + "(" + right + ")"; } 

    if (operator.startsWith("->"))
    { basicString = left + operator + "(" + right + ")"; }
	
    if (needsBracket) 
    { return "( " + basicString + " )"; }  // eg, for or inside &  }
    return basicString; 
  } 

  public String toAST()
  { String leftast = left.toAST(); 
    String rightast = right.toAST(); 
    String basicString = 
            "(OclBinaryExpression " + leftast + " " + operator + " " + rightast + ")"; 
     
    if ("let".equals(operator) && accumulator != null)
    { String res = "(OclBinaryExpression let " + accumulator.getName() + " : " + accumulator.getType().toAST() + " = " + leftast + " in ( " + rightast + " ) )"; 
      // if (needsBracket)
      // { return "(BracketedExpression ( " + res + " ) )"; }
      return res; 
    }  

    if (operator.equals("#"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST();
      String varstring = ((BinaryExpression) left).left.toAST();  

      basicString = "(OclBinaryExpression " + rangestring + " ->exists ( " + varstring + " | " + rightast + " ) )";
    }
    else if ("#LC".equals(operator))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();  

      basicString = "(OclBinaryExpression " + rangestring + " ->existsLC ( " + varstring + " | " + rightast + " ) )";
    }
    else if (operator.equals("#1"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();  

      basicString = "(OclBinaryExpression " + rangestring + " ->exists1 ( " + varstring + " | " + rightast + " ) )";
    } 
    else if (operator.equals("!"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();  

      basicString = "(OclBinaryExpression " + rangestring + " ->forAll ( " + varstring + " | " + rightast + " ) )";
    } 
    else if (operator.equals("|"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();  

      basicString = "(OclBinaryExpression " + rangestring + " ->select ( " + varstring + " | " + rightast + " ) )";
    }
    else if (operator.equals("|C"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();

      basicString = "(OclBinaryExpression " + rangestring + " ->collect ( " + varstring + " | " + rightast + " ) )";
    }
    else if (operator.equals("|A"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 

      String varstring = ((BinaryExpression) left).left.toAST();

      basicString = "(OclBinaryExpression " + rangestring + " ->any ( " + varstring + " | " + rightast + " ) )";
    }
    else if (operator.equals("|R"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = range.toAST(); 
      String varstring = ((BinaryExpression) left).left.toAST();

      basicString = "(OclBinaryExpression " + rangestring + " ->reject ( " + varstring + " | " + rightast + " ) )";
    }
  /*  else if (operator.equals("|selectMinimals"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->selectMinimals( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|selectMaximals"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->selectMaximals( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|unionAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->unionAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|intersectAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->intersectAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|concatenateAll"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->concatenateAll( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|sortedBy"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->sortedBy( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    }
    else if (operator.equals("|isUnique"))
    { Expression range = ((BinaryExpression) left).right; 
      String rangestring = "" + range; 
      if (range.needsBracket)
      { rangestring = "(" + rangestring + ")"; } 

      basicString = rangestring + "->isUnique( " + 
             ((BinaryExpression) left).left + " | " + right + " )";
    } 
    else if (operator.equals("->including") && keyValue != null) 
    { basicString = left + "->including(" + keyValue + ", " + right + ")"; } 
    else if (operator.equals("->excluding") && keyValue != null) 
    { basicString = left + "->excluding(" + keyValue + ", " + right + ")"; } */ 
    else if (operator.equals("->includesAll") || operator.equals("->collect") || 
             operator.equals("->excludesAll") || operator.equals("->append") ||
             operator.equals("->prepend") || operator.equals("->select") || 
             operator.equals("->reject") || operator.equals("->including") ||
             operator.equals("->excluding") ||
             operator.equals("->excludingAt") ||
             operator.equals("->excludingFirst") ||
             operator.equals("->includes") ||
             operator.equals("->excludes") || operator.equals("->pow") || 
             operator.equals("->sortedBy") || operator.equals("->hasSuffix") ||
             operator.equals("->hasPrefix") || "->isUnique".equals(operator) ||
             operator.equals("->oclAsType") || "->forAll".equals(operator) ||
             "->exists".equals(operator) || "->exists1".equals(operator) || 
             "->existsLC".equals(operator) || "->any".equals(operator) ||
             operator.equals("->selectMaximals") || operator.equals("->oclIsKindOf") || 
             operator.equals("->oclIsTypeOf") || operator.equals("->includesKey") || 
             operator.equals("->excludesKey") || operator.equals("->includesValue") || 
             operator.equals("->excludesValue") || 
             operator.equals("->restrict") ||
             operator.equals("->antirestrict") ||  
             operator.equals("->selectMinimals") || operator.equals("->union") ||
             operator.equals("->intersectAll") || operator.equals("->unionAll") ||
             operator.equals("->count") || operator.equals("->indexOf") || operator.equals("->lastIndexOf") || 
             operator.equals("->equalsIgnoreCase") || operator.equals("->before") || operator.equals("->after") || 
             operator.equals("->hasMatch") || operator.equals("->isMatch") ||
             operator.equals("->split") || operator.equals("->allMatches") || operator.equals("->firstMatch") || 
             operator.equals("->at") || 
             operator.equals("->apply") ||
             operator.equals("->closure") || 
             operator.equals("->intersection") || operator.equals("->symmetricDifference"))   
    { basicString = "(OclBinaryExpression " + leftast + " " + operator + " ( " + rightast + " ) )"; } 

    if (operator.startsWith("->"))
    { basicString = "(OclBinaryExpression " + leftast + " " + operator + " ( " + rightast + " ) )"; }
	
    // if (needsBracket) 
    // { return "(BracketedExpression ( " + basicString + " ) )"; }  

    return basicString; 
  } 

  public Expression addReference(BasicExpression ref, Type t)
  { BinaryExpression res = (BinaryExpression) clone(); 
    Expression lr = left.addReference(ref,t); 
    res.left = lr; 
    if ("->select".equals(operator) || "->reject".equals(operator) || 
        "->collect".equals(operator) || "->closure".equals(operator) ||
        "->selectMinimals".equals(operator) || "->selectMaximals".equals(operator) ||
        "->isUnique".equals(operator) || "->sortedBy".equals(operator) || 
        "->unionAll".equals(operator) || "->intersectAll".equals(operator) ||
        "->concatenateAll".equals(operator) || "->existsLC".equals(operator) || 
        "->exists".equals(operator) || "->exists1".equals(operator) || 
        "->forAll".equals(operator))
    { res.right = right; } // assume no objects of type t in the rhs. 
    else 
    { Expression rr = right.addReference(ref,t); 
      res.right = rr;
    }  

    if (keyValue != null) 
    { Expression kv = keyValue.addReference(ref,t); 
      res.keyValue = kv; 
    } 

    return res; 
  } 

  public Expression replaceReference(BasicExpression ref, Type t)
  { BinaryExpression res = (BinaryExpression) clone(); 
    Expression lr = left.replaceReference(ref,t); 
    res.left = lr; 
    if ("->select".equals(operator) || "->reject".equals(operator) || 
        "->collect".equals(operator) || "->closure".equals(operator) ||
        "->selectMinimals".equals(operator) || "->selectMaximals".equals(operator) ||
        "->isUnique".equals(operator) || "->sortedBy".equals(operator) || 
        "->unionAll".equals(operator) || "->existsLC".equals(operator) ||
        "->intersectAll".equals(operator) || "->concatenateAll".equals(operator) ||  
        "->exists".equals(operator) || "->exists1".equals(operator) || 
        "->forAll".equals(operator))
    { res.right = right; } 
    else 
    { Expression rr = right.replaceReference(ref,t); 
      res.right = rr;
    }  

    if (keyValue != null) 
    { Expression kv = keyValue.replaceReference(ref,t); 
      res.keyValue = kv; 
    } 

    return res; 
  } 

  public Expression dereference(BasicExpression ref)
  { BinaryExpression res = (BinaryExpression) clone(); 
    res.left = left.dereference(ref); 
    res.right = right.dereference(ref); 
    if (keyValue != null) 
    { Expression kv = keyValue.dereference(ref); 
      res.keyValue = kv; 
    } 
    return res;  
  } 

  public Expression invert()
  { BinaryExpression res = (BinaryExpression) clone(); 

    if (operator.equals("&"))
    { res.left = left.invert(); 
      res.right = right.invert(); 
      if (res.left.entity != null) 
      { res.entity = res.left.entity; } 
      else 
      { res.entity = res.right.entity; } // for & -- should be the same. 
      return res;
    } // strictly, should swap the order of the arguments

    if (operator.equals("="))  // left should be basic
    { if (right instanceof BasicExpression)
      { // System.out.println("RTYNG TO INVERT " + right.umlkind); 
          
        BasicExpression rightbe = (BasicExpression) right; 
        if (left instanceof BasicExpression && rightbe.umlkind == CLASSID && 
            rightbe.arrayIndex != null && (rightbe.arrayIndex instanceof BasicExpression))
        { BasicExpression lbe = (BasicExpression) left;
          BasicExpression rightbearrayInd = (BasicExpression) rightbe.arrayIndex;  
          // System.out.println("RTYNG TO INVERT " + this); 
          Expression lref = lbe.objectRef;
          Type t2 = lbe.elementType;  
          if (lref != null && lref.umlkind == CLASSID && lref.entity != null && 
              (lref instanceof BasicExpression) &&
              ((BasicExpression) lref).arrayIndex != null && 
              rightbearrayInd.objectRef != null &&
              (rightbearrayInd.objectRef instanceof BasicExpression) &&  
              t2 != null && t2.isEntity())
          { // T1[idS1].f = T2[g.idS2] on S1
            return invertEqClassClassBasic(lbe, rightbe, (BasicExpression) lref, rightbearrayInd,t2); 
          } 
        }
        else if (left instanceof BasicExpression && rightbe.umlkind == CLASSID && 
            rightbe.arrayIndex != null && (rightbe.arrayIndex instanceof BinaryExpression))
        { BasicExpression lbe = (BasicExpression) left;
          BinaryExpression rightbearrayInd = (BinaryExpression) rightbe.arrayIndex;  
          Expression lref = lbe.objectRef;
          Type t2 = lbe.elementType;  
          if (lref != null && lref.umlkind == CLASSID && lref.entity != null && 
              (lref instanceof BasicExpression) &&
              ((BasicExpression) lref).arrayIndex != null && 
              rightbearrayInd.operator.equals("->collect") &&
              (rightbearrayInd.left instanceof BasicExpression) &&  
              t2 != null && t2.isEntity())
          { // T1[idS1].f = T2[g->collect(idS2)] on S1
            return invertEqClassClassCollect(lbe, rightbe, (BasicExpression) lref, rightbearrayInd, t2);
          }  
          else if (lref != null && lref.umlkind == CLASSID && lref.entity != null && 
              (lref instanceof BasicExpression) &&
              ((BasicExpression) lref).arrayIndex != null && 
              rightbearrayInd.operator.equals("->unionAll") &&
              (rightbearrayInd.left instanceof BinaryExpression) &&  
              t2 != null && t2.isEntity())
          { // T1[idS1].f = T2[g->unionAll(idS2)] on S1
            return invertEqClassClassUnionAll(lbe, rightbe, (BasicExpression) lref, rightbearrayInd, t2);
          }  
        }

        if (rightbe.umlkind == CLASSID)   // t.g = T[]
        { return invertEqClass(res,rightbe); } 

        if (right.umlkind == FUNCTION)  // t.g = f( )
        { return invertEqFunction(res,rightbe); } 

        res.left = right.invert(); 
        res.right = left.invert(); 
        res.entity = res.left.entity;  
        return res;
      }
      else if (right instanceof UnaryExpression && 
               (((UnaryExpression) right).argument instanceof BasicExpression)) 
      { UnaryExpression ue = (UnaryExpression) right; 
        return invertEqUnary(ue);  // t.g = bl->op()
      } 
      else if (right instanceof BinaryExpression)   // t.g = l->op(r) 
      { BinaryExpression rbine = (BinaryExpression) right; 
        return invertEqBinary(rbine); 
      } 
      else if (left instanceof BasicExpression && right instanceof SetExpression) 
      { SetExpression sright = (SetExpression) right; 
        return sright.invertEq((BasicExpression) left); 
      }  
      // else if both basic, eg.,  t.g = s.r.f
    } 

    if (isForallExists())
    { // s->forAll( x | E->exists( y | P(x,y) & y : t ) ) inverts to 
      // t->forAll( y | F->exists( x | P~(x,y) & x : s ) ) where F = s.elementType.entity
      BinaryExpression bl = (BinaryExpression) left; // x : s 
      BinaryExpression br = (BinaryExpression) right; // E->exists( y | br.right )
      BinaryExpression succ = (BinaryExpression) br.right; // pxy & y : t  
      Expression x = bl.left; 
      Expression s = bl.right; 
      Expression y = ((BinaryExpression) br.left).left; 
      Vector remainder = new Vector(); 
      Expression t = succ.featureAdding2("" + y, remainder);
      Type selemt = s.getElementType(); 
      if (t != null && selemt != null && selemt.isEntity()) 
      { Expression pxy = (Expression) remainder.get(0);
        Expression pinv = pxy.invert(); 
        Expression newsucc = new BinaryExpression("&", pinv, (Expression) bl.clone());  // pxy~ & x : s
        Entity sent = selemt.getEntity(); 
        Expression sentexp = new BasicExpression(sent); 
        Expression sinv = new BinaryExpression("#", new BinaryExpression(":", x, sentexp), newsucc); 
        BinaryExpression fres = new BinaryExpression("!", new BinaryExpression(":", y, t), sinv); 
        return fres; 
      }  
    } 

        
    /* if ("&".equals(operator))
    { Expression newll = left.invert(); 
      Expression newrr = right.invert(); 
      res.left = newll; 
      res.right = newrr; 
      return res; 
    } */ 

    if ("=>".equals(operator))
    { Expression newrr = right.invert(); 
      res.left = left; 
      res.right = newrr; 
      return res; 
    } 

    Expression nl = left.invert(); 
    Expression nr = right.invert(); 
    res.left = nl; 
    res.right = nr; 
    return res; 
  } 


  private Expression invertEqClassClassBasic(BasicExpression lbe, BasicExpression rightbe, 
                           BasicExpression lref, BasicExpression rightbearrayInd, Type t2)
  { BinaryExpression res = (BinaryExpression) this.clone(); 

    // T1[idS1].f = T2[g.idS2]

    System.out.println("Invert-eq-class-basic: " + this); 
    
    BasicExpression newleft = (BasicExpression)
         ((BasicExpression) rightbearrayInd.objectRef).clone();  // g
    Entity s1ent = newleft.entity;  // S1, owner of g
    Entity t1ent = lref.entity;     // T1, owner of f
    Type s2 = newleft.elementType; 
    Entity s2ent; 
    s2ent = s2.getEntity(); 
            // res is  S1[idT1].g = S2[f.idT2] on T1 
    Attribute idT1 = null;
    Vector t1ids = t1ent.allDefinedUniqueAttributes();
    if (t1ids.size() > 0)
    { idT1 = (Attribute) t1ids.get(0); }
    Entity t2ent; 
    t2ent = t2.getEntity(); 
    Attribute idT2 = null;
    Vector t2ids = t2ent.allDefinedUniqueAttributes();
    if (t2ids.size() > 0)
    { idT2 = (Attribute) t2ids.get(0); }
    BasicExpression newleftoref = new BasicExpression(s1ent);
    BasicExpression newleftorefind = new BasicExpression(idT1);
    newleftoref.arrayIndex = newleftorefind;
    newleft.objectRef = newleftoref;
            // S1[idT1].g 
    res.left = newleft;
    BasicExpression newright = new BasicExpression(s2ent);
    BasicExpression newrightind = new BasicExpression(idT2);
    BasicExpression nrindoref = (BasicExpression) lbe.clone(); // f
    nrindoref.objectRef = null;
    newrightind.objectRef = nrindoref;
    newright.arrayIndex = newrightind;
    res.right = newright;  // S2[f.idT2]
    res.entity = t1ent;    // owner of this constraint. 
    return res;
  }

  private Expression invertEqClassClassCollect(BasicExpression lbe, BasicExpression rightbe, 
                           BasicExpression lref, BinaryExpression rightbearrayInd, Type t2)
  { System.out.println("Invert-eq-class-collect: " + this);    

    // T1[idS1].f = T2[g->collect(idS2)]

    BinaryExpression res = (BinaryExpression) this.clone(); 
    BasicExpression newleft = (BasicExpression)
              ((BasicExpression) rightbearrayInd.left).clone();  // g
    Entity s1ent = newleft.entity;  // S1, owner of g
    Entity t1ent = lref.entity;     // T1, owner of f
    Type s2 = newleft.elementType; 
    Entity s2ent; 
    s2ent = s2.getEntity(); 
            // res is  S1[idT1].g = S2[f->collect(idT2)] on T1 
    Attribute idT1 = null;
    Vector t1ids = t1ent.allDefinedUniqueAttributes();
    if (t1ids.size() > 0)
    { idT1 = (Attribute) t1ids.get(0); }
    Entity t2ent; 
    t2ent = t2.getEntity(); 
    Attribute idT2 = null;
    Vector t2ids = t2ent.allDefinedUniqueAttributes();
    if (t2ids.size() > 0)
    { idT2 = (Attribute) t2ids.get(0); }
    BasicExpression newleftoref = new BasicExpression(s1ent);
    BasicExpression newleftorefind = new BasicExpression(idT1);
    newleftoref.arrayIndex = newleftorefind;
    newleft.objectRef = newleftoref;
            // S1[idT1].g 
    res.left = newleft;
    BasicExpression newright = new BasicExpression(s2ent);
    BasicExpression newrightind = new BasicExpression(idT2);
    BasicExpression nrindoref = (BasicExpression) lbe.clone(); // f
    nrindoref.objectRef = null;
    
    newright.arrayIndex = new BinaryExpression("->collect", nrindoref, newrightind);
    res.right = newright;  // S2[f->collect(idT2)]
    res.entity = t1ent;    // owner of this constraint. 
    return res;
  } 

  private Expression invertEqClassClassUnionAll(BasicExpression lbe, BasicExpression rightbe, 
                           BasicExpression lref, BinaryExpression rightbearrayInd, Type t2)
  { System.out.println("Invert-eq-class-unionAll: " + this); 

    // T1[idS1].f = T2[Set{self}->closure(r)->unionAll(g.idS2)]

    BinaryExpression res = (BinaryExpression) this.clone(); 
    Expression closurer = rightbearrayInd.left; 
    if (closurer instanceof BinaryExpression && 
        ((BinaryExpression) closurer).operator.equals("->closure")) { } 
      // T1[idS1].f = T2[r->unionAll(g.idS2)] inverts to 
      // S1[idT1].r->unionAll(g)  =  S2[f->collect(idT2)]
    else if (closurer instanceof BasicExpression)
    { BasicExpression r = (BasicExpression) closurer; 
      BasicExpression unionarg = (BasicExpression) rightbearrayInd.right; // g.idS2
      BasicExpression g = (BasicExpression) unionarg.objectRef.clone();
      Entity s1ent = r.entity; // S1
      Entity t1ent = lref.entity; // T1 
      Type s2 = g.elementType; 
      Entity s2ent = s2.getEntity();
      Attribute idT1 = null;
      Vector t1ids = t1ent.getUniqueAttributes();
      if (t1ids.size() > 0)
      { idT1 = (Attribute) t1ids.get(0); }
      else 
      { System.err.println("Cannot invert " + this);
        return this; 
      }
      Attribute idT2 = null;
      Entity t2ent = t2.getEntity(); 
      Vector t2ids = t2ent.getUniqueAttributes();
      if (t2ids.size() > 0)
      { idT2 = (Attribute) t2ids.get(0); }
      else 
      { System.err.println("Cannot invert " + this);
        return this; 
      }
      BasicExpression newleftoref = new BasicExpression(s1ent);
      BasicExpression newleftorefind = new BasicExpression(idT1);
      newleftoref.arrayIndex = newleftorefind; // S1[idT1]
      r.objectRef = newleftoref; // S1[idT1].r
      res.left = new BinaryExpression("->unionAll", r, g);
      BasicExpression f = (BasicExpression) lbe.clone();
      f.objectRef = null;
      BinaryExpression newrightind = new BinaryExpression("->collect", f, new BasicExpression(idT2));
      BasicExpression newright = new BasicExpression(s2ent);
      newright.arrayIndex = newrightind; // S2[f->collect(idT2)]
      res.right = newright;
      res.entity = t1ent;
      return res;
    }
    else { return this; } 
  
 
    BinaryExpression beclosurer = (BinaryExpression) closurer; 
    BasicExpression r = (BasicExpression) beclosurer.right; 

    BasicExpression newleft = (BasicExpression)
              ((BasicExpression) rightbearrayInd.right).clone();  // g.idS2
    BasicExpression g = (BasicExpression) newleft.objectRef; 

    Entity s1ent = g.entity;        // S1, owner of g
    Entity t1ent = lref.entity;     // T1, owner of f
    Type s2 = g.elementType; 
    Entity s2ent; 
    s2ent = s2.getEntity(); 
       // res is  S1[idT1].g = S2[f.idT2 - S1[idT1].r->closure(r)->unionAll(g.idS2)] on T1 
    Attribute idT1 = null;
    Vector t1ids = t1ent.allDefinedUniqueAttributes();
    if (t1ids.size() > 0)
    { idT1 = (Attribute) t1ids.get(0); }
    else 
    { System.err.println("Unable to invert: " + this); 
      return this; 
    } 

    Entity t2ent; 
    t2ent = t2.getEntity(); 
    Attribute idT2 = null;
    Vector t2ids = t2ent.allDefinedUniqueAttributes();
    if (t2ids.size() > 0)
    { idT2 = (Attribute) t2ids.get(0); }
    else 
    { System.err.println("Unable to invert: " + this); 
      return this; 
    } 

    BasicExpression newleftoref = new BasicExpression(s1ent);
    BasicExpression newleftorefind = new BasicExpression(idT1);
    newleftoref.arrayIndex = newleftorefind;  // S1[idT1]
    g.objectRef = newleftoref;
            // S1[idT1].g 
    res.left = g;

    BasicExpression newright = new BasicExpression(s2ent);
    BasicExpression rclone = (BasicExpression) r.clone(); 
    BasicExpression f = (BasicExpression) lbe.clone(); // f
    f.objectRef = null;

    BasicExpression idT2be = new BasicExpression(idT2);
    idT2be.setObjectRef(f);            // f.idT2
    rclone.setObjectRef(newleftoref);  // S1[idT1].r
    BinaryExpression newclosure = new BinaryExpression("->closure", rclone, r); 
    BinaryExpression newunionall = 
        new BinaryExpression("->unionAll", newclosure, rightbearrayInd.right); 

    newright.arrayIndex = new BinaryExpression("-", idT2be, newunionall);
    res.right = newright;  // S2[f.idT2 - S1[idT1].r->closure(r)->unionAll(g.idS2)]
    res.entity = t1ent;    // owner of this constraint. 
    return res;
  } 

  private Expression invertEqUnary(UnaryExpression ue)
  { BinaryExpression res = (BinaryExpression) clone(); 
    if ("->reverse".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->reverse",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->log".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->exp",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->exp".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->log",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->sqrt".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->sqr",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->sin".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->asin",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->cos".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->acos",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->tan".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->atan",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->asin".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->sin",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->acos".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->cos",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->atan".equals(ue.operator))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->tan",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->sort".equals(ue.operator) && !(ue.argument.isOrdered()))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->asSet",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->sort".equals(ue.operator) && ue.argument.isOrdered())
    { res.left = ue.argument; 
      res.right = left; 
      return res; 
    } 
    else if ("->asSequence".equals(ue.operator) && !(ue.argument.isOrdered()))
    { res.left = ue.argument; 
      UnaryExpression newr = new UnaryExpression("->asSet",left); 
      res.right = newr; 
      return res; 
    } 
    else if ("->asSequence".equals(ue.operator) && ue.argument.isOrdered())
    { res.left = ue.argument; 
      res.right = left; 
      return res; 
    } 
    res.left = right; 
    res.right = left; 
    return res; 
  } // also ->asSet inverts to ->any on ue.argument not multiple

  private Expression invertEqBinary(BinaryExpression rbine)
  { BinaryExpression res = (BinaryExpression) clone(); 
    if ("+".equals(rbine.operator) && 
            rbine.right.umlkind == VALUE && Type.isNumericType(type.getName())) 
    { // left = rbine.left + VAL, reversed is  rbine.left = left - VALUE
      res.left = rbine.left; 
      res.right = new BinaryExpression("-",left,rbine.right); 
      return res; 
    } 
    if ("-".equals(rbine.operator) && 
        rbine.right.umlkind == VALUE &&
        Type.isNumericType(type.getName())) 
    { // left = rbine.left - VAL, reversed is  rbine.left = left + VALUE
          res.left = rbine.left; 
          res.right = new BinaryExpression("+",left,rbine.right); 
          return res; 
    } 
    if ("/".equals(rbine.operator) && 
            rbine.right.umlkind == VALUE &&
            Type.isNumericType(type.getName())) 
    { // left = rbine.left / VAL, reversed is  rbine.left = left * VALUE
      res.left = rbine.left; 
      res.right = new BinaryExpression("*",left,rbine.right); 
      return res; 
    } 
    if ("*".equals(rbine.operator) && 
        rbine.right.umlkind == VALUE &&
        Type.isNumericType(type.getName())) 
    { // left = rbine.left * VAL, reversed is  rbine.left = left / VALUE
      res.left = rbine.left; 
      res.right = new BinaryExpression("/",left,rbine.right); 
      return res; 
    } 
    else if (rbine.operator.equals("->pow"))
    { res.left = rbine.left; 
      Expression x = rbine.right; 
      BinaryExpression newpow = 
          new BinaryExpression("/", new BasicExpression(1.0), x); 
      res.right = new BinaryExpression("->pow", left, newpow); 
      return res; 
    } 
    else if ((rbine.operator.equals("->including") && left.isOrdered()) ||
              rbine.operator.equals("->append"))
    { // left = rl->including(rr)

      Expression rl = rbine.left; 
      Expression rr = rbine.right; 
      BinaryExpression neweq1 = 
          new BinaryExpression("=", rr, new UnaryExpression("->last", left)); 
      BinaryExpression neweq2 = 
          new BinaryExpression("=", rl, new UnaryExpression("->front", left)); 
      res = new BinaryExpression("&", neweq1, neweq2); 
      return res; 
    } 
    else if (rbine.operator.equals("->prepend"))
    { // left = rl->prepend(rr)

      Expression rl = rbine.left; 
      Expression rr = rbine.right; 
      BinaryExpression neweq1 = 
          new BinaryExpression("=", rr, new UnaryExpression("->first", left)); 
      BinaryExpression neweq2 = 
          new BinaryExpression("=", rl, new UnaryExpression("->tail", left)); 
      res = new BinaryExpression("&", neweq1, neweq2); 
      return res; 
    } 

    return this;  
  } 

  public Expression invertEqClass(BinaryExpression res, BasicExpression rightbe)
  { // t.g = T2[exp]
    if (rightbe.arrayIndex != null) 
    { if (rightbe.arrayIndex instanceof BasicExpression) // T2[s.f.id] 
      { BasicExpression rindex = (BasicExpression) rightbe.arrayIndex; 
        if (rindex.objectRef != null)               
        { res.left = rindex.objectRef;   // s.f = T2[t.g.id]
          String id2 = "";  
          Type s2type = rindex.objectRef.elementType; 
          Type leftelemtype = left.elementType; 
          if (leftelemtype != null && leftelemtype.isEntity())
          { Entity leent = leftelemtype.getEntity(); 
            Vector idatts = leent.allDefinedUniqueAttributes(); 
            if (idatts.size() > 0)
            { id2 = idatts.get(0) + ""; } 
            else 
            { System.err.println("Cannot invert " + this); 
              return this; 
            } 
          }
          BasicExpression newright = new BasicExpression(s2type + ""); 
          if (id2.length() > 0)
          { newright.arrayIndex = new BasicExpression(id2);
            ((BasicExpression) newright.arrayIndex).objectRef = left; 
          }  
          else 
          { newright.arrayIndex = left; } 
          res.right = newright; // t.g = S2[s.f.id2]
          return res; 
        } 
        else // left = T2[rindex] 
        { String id2 = "";  
          Type leftelemtype = left.elementType; 
          if (leftelemtype != null && leftelemtype.isEntity())
          { Entity leent = leftelemtype.getEntity(); 
            Vector idatts = leent.allDefinedUniqueAttributes(); 
            if (idatts.size() > 0)
            { id2 = idatts.get(0) + ""; } 
            else 
            { System.err.println("Cannot invert " + this); 
              return this; 
            }
          }
          BasicExpression newright = new BasicExpression("" + left); 
          if (id2.length() > 0)
          { newright = new BasicExpression(id2);
            ((BasicExpression) newright).objectRef = left; 
          }  
          res.left = rindex; 
          res.right = newright; // rindex = left.id2
          return res; 
        } 
      }
      else if ((rightbe.arrayIndex instanceof BinaryExpression) && 
               "->collect".equals(((BinaryExpression) rightbe.arrayIndex).operator)) 
              // t.g = T2[s.f->collect(id)] 
      { BinaryExpression rindex = (BinaryExpression) rightbe.arrayIndex; 
              
        res.left = rindex.left;   // s.f =
        String id2 = "";  
        Type s2type = rindex.left.elementType; 
        Type leftelemtype = left.elementType; 
        if (leftelemtype != null && leftelemtype.isEntity())
        { Entity leent = leftelemtype.getEntity(); 
          Vector idatts = leent.allDefinedUniqueAttributes(); 
          if (idatts.size() > 0)
          { id2 = idatts.get(0) + ""; } 
          else 
          { System.err.println("Cannot invert " + this); 
            return this; 
          }
        }
        BasicExpression newright = new BasicExpression(s2type + ""); 
        if (id2.length() > 0)
        { BinaryExpression newcollect = new BinaryExpression("->collect", left, 
                                                             new BasicExpression(id2));
          newright.arrayIndex = newcollect; 
        }  
        else 
        { newright.arrayIndex = left; } 
        res.right = newright; // S2[t.g->collect(id2)]

        if (rindex.left instanceof UnaryExpression) 
        { UnaryExpression rindexleft = (UnaryExpression) rindex.left; 
          if (rindexleft.operator.equals("->reverse")) 
          { res.left = rindexleft.argument; 
            res.right = new UnaryExpression("->reverse", newright); 
          } 
        } 

        return res; 
      }
      else if ((rightbe.arrayIndex instanceof BinaryExpression) && 
               "->unionAll".equals(((BinaryExpression) rightbe.arrayIndex).operator)) 
              // t.rr = T2[s.r->unionAll(f.id)] 
      { BinaryExpression rindex = (BinaryExpression) rightbe.arrayIndex; 
              
        Expression f = null; 
        if (rindex.right instanceof BasicExpression) 
        { f = ((BasicExpression) rindex.right).objectRef; } 
        else 
        { f = rindex.right; } // doesn't make sense. 

        res.left = new BinaryExpression("->unionAll", rindex.left, f);   
             // s.r->unionAll(f) = S2[t.rr->collect(id2)] 

        String id2 = "";  
        Type s2type = rindex.left.elementType; 
        Type leftelemtype = left.elementType; 
        if (leftelemtype != null && leftelemtype.isEntity())
        { Entity leent = leftelemtype.getEntity(); 
          Vector idatts = leent.allDefinedUniqueAttributes(); 
          if (idatts.size() > 0)
          { id2 = idatts.get(0) + ""; }
          else 
          { System.err.println("Cannot invert " + this); 
            return this; 
          } 
        }
        BasicExpression newright = new BasicExpression(s2type + ""); 
        if (id2.length() > 0)
        { BinaryExpression newcollect = new BinaryExpression("->collect", left, 
                                                             new BasicExpression(id2));
          newright.arrayIndex = newcollect; 
        }  
        else 
        { newright.arrayIndex = left; } 
        res.right = newright; // S2[t.rr->collect(id2)]
        return res; 
      } 
    }
    res.left = right; 
    res.right = left; 
    return res; 
  } // also cases of s.f->reverse()->collect(id), s.f->sort()->collect(id) etc. 

  public Expression invertEqFunction(BinaryExpression res, BasicExpression rbe)
  { // BasicExpression rbe = (BasicExpression) right; 
    if (rbe.data.equals("sqrt"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("sqr"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
          /* else if (rbe.data.equals("sqr"))
          { res.left = rbe.objectRef; 
            BasicExpression newr = new BasicExpression("sqrt"); 
            newr.umlkind = FUNCTION; 
            newr.objectRef = left; 
            res.right = newr; 
            return res; 
          } */ 
    else if (rbe.data.equals("reverse"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("reverse"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("log"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("exp"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("exp"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("log"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("sin"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("asin"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("cos"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("acos"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("tan"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("atan"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("asin"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("sin"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("acos"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("cos"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("atan"))
    { res.left = rbe.objectRef; 
      BasicExpression newr = new BasicExpression("tan"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      return res; 
    } 
    else if (rbe.data.equals("pow"))
    { res.left = rbe.objectRef; 
      Expression x = (Expression) rbe.getParameters().get(0); 
      BasicExpression newr = new BasicExpression("pow"); 
      newr.umlkind = FUNCTION; 
      newr.objectRef = left; 
      res.right = newr; 
      BinaryExpression newarg = 
           new BinaryExpression("/", new BasicExpression(1.0), x); 
      newr.addParameter(newarg); 
      return res; 
    } 
    res.left = right; 
    res.right = left; 
    return res; 
  } 


  public String toSQL()
  { String ls = left.toSQL();
    String rs = right.toSQL();
    String res;
    if (operator.equals("&"))
    { res = ls + " AND " + rs; }
    else if (operator.equals("or"))
    { res = ls + " OR " + rs; }
    else if (operator.equals("+") && 
             (left.getType().getName().equals("String") ||
              right.getType().getName().equals("String")))
    { res = "CONCAT(" + ls + "," + rs + ")"; } 
    else // MOD as well?  
    { res = ls + " " + operator + " " + rs; }
    if (needsBracket)
    { res = "( " + res + " )"; }
    return res;
  }
     

  public boolean isPrimitive()  
  { // Strings are not counted as primitive

    if (operator.equals("->count") || operator.equals("->indexOf") || operator.equals("->lastIndexOf") || 
        operator.equals("->oclIsKindOf") || operator.equals("->oclIsTypeOf") || 
        operator.equals("->pow") || 
        operator.equals("->hasPrefix") || operator.equals("->hasSuffix") ||
        operator.equals("->exists") || "->existsLC".equals(operator) ||
        operator.equals("->exists1") || operator.equals("->isUnique") || operator.equals("|isUnique") || 
        operator.equals("->forAll") || operator.equals("<:") || operator.equals("=") ||
        operator.equals("/=") || operator.equals("->includes") || 
        operator.equals("->excludes") ||
        operator.equals("!=") || operator.equals("<>") || 
        operator.equals("#") || operator.equals("#LC") || 
        operator.equals("/<:") ||
        operator.equals("!") || operator.equals("#1") || 
        operator.equals("->excludesAll") ||
        operator.equals("->includesAll") ||
        operator.equals("->compareTo") || 
        comparitors.contains(operator))
    { return true; } 

    if (operator.equals("\\/") || operator.equals("/\\") || operator.equals("^") ||
        operator.equals("->collect") || operator.equals("->union") ||
        operator.equals("->sortedBy") || operator.equals("->select") || operator.equals("->closure") || 
        operator.equals("->reject") || operator.equals("|") || operator.equals("|R") ||
        operator.equals("|C") || operator.equals("->symmetricDifference") || 
        operator.equals("->intersection") || operator.equals("->prepend") || 
        operator.equals("->unionAll") || operator.equals("->intersectAll") || 
        operator.equals("|unionAll") || operator.equals("|intersectAll") || operator.equals("|sortedBy") ||
        operator.equals("->selectMaximals") || operator.equals("->selectMinimals") ||
        "|selectMinimals".equals(operator) ||  
        "|selectMaximals".equals(operator) ||
        operator.equals("->append") || 
        operator.equals("->including") || 
        operator.equals("->restrict") ||
        operator.equals("->antirestrict") || 
        operator.equals("->excludingAt") ||
        operator.equals("->excludingFirst") ||
        operator.equals("->excluding")) 
    { return false; } 

    if (operator.equals("->oclAsType"))
    { String typ = right + ""; 
      if ("int".equals(typ) || "long".equals(typ) || "double".equals(typ) || 
          "boolean".equals(typ))
      { return true; } // or it is the name of an enum - but a strange thing to do
      if (right.type != null && right.type.isEnumeration())
      // ModelElement.lookupByName(typ, types) != null) 
      { return true; } 
      return false; 
    } 

    if (operator.equals("->at") || operator.equals("|A") || operator.equals("->any"))
    { return Type.isPrimitiveType(left.elementType); } 

    if (operator.equals("->apply"))
    { return Type.isPrimitiveType(type); } 

    // for +, -, *, /
    return left.isPrimitive() && right.isPrimitive(); 
  } // e->oclAsType(T)  if T is a primitive type

  // Also s->exists(p) and s->forAll(p) are primitive? 

  public Vector allEntitiesUsedIn()
  { Vector les = left.allEntitiesUsedIn();
    Vector res = right.allEntitiesUsedIn();
    return VectorUtil.union(les,res);
  }

  public Vector innermostEntities()
  { Vector res = left.innermostEntities(); 
    return VectorUtil.union(res,right.innermostEntities()); 
  } 

  public Vector innermostVariables()
  { Vector res = left.innermostVariables(); 
    return VectorUtil.union(res,right.innermostVariables()); 
  } 

  public Vector metavariables()
  { if ("|C".equals(operator) || "|R".equals(operator) || "|".equals(operator) ||
        "|A".equals(operator) || "#".equals(operator) || "#1".equals(operator) || 
		"!".equals(operator) || 
		(operator.startsWith("|") && (left instanceof BinaryExpression))  
	   )
    { BinaryExpression beleft = (BinaryExpression) left; 
      Vector res = beleft.right.metavariables(); 
      res = VectorUtil.union(res,beleft.left.metavariables());
      return VectorUtil.union(res,right.metavariables());
    } 
    Vector res = left.metavariables(); 
    return VectorUtil.union(res,right.metavariables()); 
  } 

  public Vector allPreTerms()
  { Vector res = left.allPreTerms(); 
    Vector rpterms = right.allPreTerms(); 

    if ("->forAll".equals(operator) || "->exists".equals(operator) ||
        "->exists1".equals(operator) || "->select".equals(operator) || 
        "->existsLC".equals(operator) || "->any".equals(operator) ||
        "->reject".equals(operator) || "->collect".equals(operator) ||
        "->selectMinimals".equals(operator) || "->selectMaximals".equals(operator) || 
        "->unionAll".equals(operator) || "->intersectAll".equals(operator) ||
        "->concatenateAll".equals(operator))
    { // discard pre-terms of features defined in the left.elementType.entity
      // because these are handled by the inner iteration

      Type letype = left.elementType;
      if (letype == null) 
      { letype = left.type; } 
      Entity lent = left.entity; 
      Vector removals = new Vector(); 

      if (letype != null && letype.isEntity())
      { lent = letype.entity;
        for (int i = 0; i < rpterms.size(); i++) 
        { Expression rpt = (Expression) rpterms.get(i); 
          if (rpt instanceof BasicExpression) 
          { BasicExpression berpt = (BasicExpression) rpt;     
            if (berpt.umlkind == ROLE || berpt.umlkind == ATTRIBUTE) 
            { if (berpt.entity == lent)
              { removals.add(rpt); } 
              else if (lent.hasFeature(berpt.data))
              { removals.add(rpt); } 
            } 
          } 
        } 
      } 
      rpterms.removeAll(removals); 
    } 
    else if ("!".equals(operator) || "#".equals(operator) || "|A".equals(operator) || 
        "#1".equals(operator) || "|".equals(operator) || operator.equals("#LC") ||
        "|R".equals(operator) || "|C".equals(operator) || 
		"|selectMinimals".equals(operator) || "|selectMaximals".equals(operator) || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { // discard pre-terms of features of the left.left
      // because these are invalid

      BinaryExpression lbe = (BinaryExpression) left;
      BasicExpression v = (BasicExpression) lbe.left; 

      Vector removals = new Vector(); 

      for (int i = 0; i < rpterms.size(); i++) 
      { Expression rpt = (Expression) rpterms.get(i); 
        Vector inns = rpt.innermostVariables(); 

        if (inns.contains(v + "")) 
        { removals.add(rpt); } 
      } 
      rpterms.removeAll(removals); 
    } 

    return VectorUtil.union(res,rpterms); 
  } // also discard for ->select, ->reject, ->collect, ->isUnique, ->selectMaximals, 
    // ->selectMinimals, ->unionAll, ->intersectAll


  public Vector allPreTerms(String var)
  { Vector res = left.allPreTerms(var); 
    Vector rpterms = right.allPreTerms(var); 

    if ("->forAll".equals(operator) || "->exists".equals(operator) ||
        "->existsLC".equals(operator) || "->any".equals(operator) ||
        "->exists1".equals(operator) || "->select".equals(operator) || 
        "->reject".equals(operator) || "->collect".equals(operator) ||
        "->selectMinimals".equals(operator) || "->selectMaximals".equals(operator) || 
        "->unionAll".equals(operator) || "->intersectAll".equals(operator) ||
        "->concatenateAll".equals(operator))
    { // discard pre-terms of features of the left.elementType.entity
      Type letype = left.elementType;
      if (letype == null) 
      { letype = left.type; } 
      Entity lent = left.entity; 
      Vector removals = new Vector(); 

      if (letype != null && letype.isEntity())
      { lent = letype.entity;
        for (int i = 0; i < rpterms.size(); i++) 
        { Expression rpt = (Expression) rpterms.get(i); 
          if (rpt instanceof BasicExpression) 
          { BasicExpression berpt = (BasicExpression) rpt;     
            if (berpt.umlkind == ROLE || berpt.umlkind == ATTRIBUTE) 
            { if (berpt.entity == lent)
              { removals.add(rpt); } 
              else if (lent.hasFeature(berpt.data))
              { removals.add(rpt); } 
            } 
          } 
        } 
      } 
      rpterms.removeAll(removals); 
    } 
    else if ("!".equals(operator) || "#".equals(operator) || operator.equals("#LC") ||
        "#1".equals(operator) || "|".equals(operator) || "|A".equals(operator) || 
        "|R".equals(operator) || "|C".equals(operator) ||
		"|selectMinimals".equals(operator) || "|selectMaximals".equals(operator) || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { // discard pre-terms of features of the left.left
      // because these are invalid

      BinaryExpression lbe = (BinaryExpression) left;
      BasicExpression v = (BasicExpression) lbe.left; 

      Vector removals = new Vector(); 

      for (int i = 0; i < rpterms.size(); i++) 
      { Expression rpt = (Expression) rpterms.get(i); 
        Vector inns = rpt.innermostVariables(); 

        if (inns.contains(v + "")) 
        { removals.add(rpt); } 
      } 
      rpterms.removeAll(removals); 
    } 

    return VectorUtil.union(res,rpterms); 
  } // As above, also for ->select, etc. 

  public Vector getBaseEntityUses()  // entities E such that f occurs without an object ref
  { if (operator.equals("->forAll") || operator.equals("->exists") ||
        "->existsLC".equals(operator) || operator.equals("->any") ||
        operator.equals("->exists1") || operator.equals("->sortedBy") ||
        operator.equals("->select") || operator.equals("->reject") || 
        "->isUnique".equals(operator) || operator.equals("->unionAll") || 
        operator.equals("->intersectAll") || 
        operator.equals("->collect") || operator.equals("->selectMaximals") ||
        operator.equals("->selectMinimals"))
    { Vector ss = right.getBaseEntityUses(); 
      Vector removals = new Vector(); 
      Type elemtype = left.getElementType(); 
      if (elemtype != null && elemtype.isEntity())
      { Entity e2 = elemtype.getEntity(); 

        for (int i = 0; i < ss.size(); i++) 
        { Entity e1 = (Entity) ss.get(i); 
          if (Entity.inheritanceRelated(e1,e2)) 
          { removals.add(e1); } 
        } 
        ss.removeAll(removals); 
      } 

      Vector ss1 = left.getBaseEntityUses(); 
      ss = VectorUtil.union(ss,ss1); 

      // System.out.println("Base entities of " + this + " = " + ss); 
      return ss; 
    } 
    
    // forAll, exists, exists1 and select, reject, collect: ignore the 
    // entity being quantified/ranged over. 

    if (operator.equals("->closure")) 
    { return left.getBaseEntityUses(); } 

    if (operator.equals("!") || operator.equals("#") || operator.equals("#LC") ||
        operator.equals("#1") || operator.equals("|") || operator.equals("|R") ||
        operator.equals("|A") || operator.equals("|C") ||
		"|selectMinimals".equals(operator) || "|selectMaximals".equals(operator) || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { Vector ss = right.getBaseEntityUses(); 
      if (left instanceof BinaryExpression)
      { Vector res = ((BinaryExpression) left).right.getBaseEntityUses(); 
        return VectorUtil.union(ss,res); 
      }
      return ss; 
    }  
    
    Vector res = left.getBaseEntityUses();
    res = VectorUtil.union(res,right.getBaseEntityUses());
    // System.out.println("Base entities of " + this + " = " + res); 
    return res;
  } // This is what you need in uml2Cbmm to obtain the parameters of lambda expressions. 
    
  public Vector getVariableUses()
  { if (operator.equals("#") || operator.equals("|") || operator.equals("|A") ||
        operator.equals("|C") || operator.equals("#LC") ||
        operator.equals("|R") || operator.equals("#1") || operator.equals("!") ||
		"|selectMinimals".equals(operator) || "|selectMaximals".equals(operator) || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { Vector ss = right.getVariableUses(); 
      Vector removals = new Vector(); 
      if (left instanceof BinaryExpression)
      { Expression e = ((BinaryExpression) left).left;
        for (int i = 0; i < ss.size(); i++) 
        { Expression e1 = (Expression) ss.get(i); 
          if ((e1 + "").equals(e + ""))
          { removals.add(e1); } 
        } 
        ss.removeAll(removals);
        Expression scope = ((BinaryExpression) left).right; 
        ss = VectorUtil.union(ss,scope.getVariableUses()); 
      } 
      return ss; 
    } 
    

    Vector res = left.getVariableUses();
    res = VectorUtil.union(res,right.getVariableUses());
    return res;
  }

  public Vector getUses(String feature)
  { Vector res = new Vector();
    res.addAll(left.getUses(feature));
    res.addAll(right.getUses(feature));
    return res;
  }  // VectorUtil.union of the uses? 

  public Vector allFeaturesUsedIn()
  { Vector res = left.allFeaturesUsedIn();
    return VectorUtil.union(res,
                            right.allFeaturesUsedIn());
  }

  public Vector allAttributesUsedIn()
  { Vector res = left.allAttributesUsedIn();
    return VectorUtil.union(res,
                            right.allAttributesUsedIn());
  }

  public Vector allOperationsUsedIn()
  { Vector res = left.allOperationsUsedIn();
    return VectorUtil.union(res,
                            right.allOperationsUsedIn());
  }

  public Vector equivalentsUsedIn()
  { Vector res = left.equivalentsUsedIn();
    return VectorUtil.union(res,
                            right.equivalentsUsedIn());
  }

  public Vector allValuesUsedIn()
  { Vector res = left.allValuesUsedIn();
    return VectorUtil.union(res,
                            right.allValuesUsedIn());
  }

public void findClones(java.util.Map clones, String rule, String op)
{ if (this.syntacticComplexity() < 10) { return; }
  String val = this + ""; 
  Vector used = (Vector) clones.get(val);
  if (used == null)
  { used = new Vector(); }
  if (rule != null)
  { used.add(rule); }
  else if (op != null)
  { used.add(op); }
  clones.put(val,used);
  left.findClones(clones,rule,op);
  right.findClones(clones,rule,op);
}

  public Vector variableRanges(java.util.Map ranges, Vector pars)
  { Vector res = new Vector(); 
    if (operator.equals(":") && left.umlkind == VARIABLE)
    { if (ranges.keySet().contains(left + ""))
      { Expression oldright = (Expression) ranges.get(left + ""); 
        System.out.println("->> redefinition of " + left + " : " + oldright + " by " + this); 

        if (Expression.isEntity(oldright) && Expression.isEntity(right))
        { Entity oldent = oldright.getElementType().getEntity(); 
          Entity newent = right.getElementType().getEntity(); 
          if (oldent != null && newent != null && Entity.isAncestor(oldent,newent))
          { ranges.put(left + "", new BasicExpression(newent)); 
            System.out.println("--> Set range of " + left + " to " + right); 
          }  // replace by most-specific type
          else { } 
        } 
        else { } 
      } 
      else if (pars.contains(left + "")) 
      { } // Only the first scope is used, later ones are further conditions
      else 
      { res.add(left + ""); 
        System.out.println("--> Set range of " + left + " to " + right); 
        ranges.put(left + "", right);
      }  
    } 
    else if (operator.equals("&"))
    { res.addAll(left.variableRanges(ranges,pars)); 
      res.addAll(right.variableRanges(ranges,pars)); 
    } 
    
    return res; 
  }

  public Vector letDefinitions(java.util.Map ranges, Vector pars)
  { Vector res = new Vector(); 
    if (operator.equals("=") && left.umlkind == VARIABLE)
    { if (ranges.keySet().contains(left + "") || pars.contains(left + "")) 
      { } // Only the first scope is used, later ones are further conditions
      else 
      { res.add(left + ""); 
        ranges.put(left + "", right); 
      } 
    } 
    else if (operator.equals("&"))
    { res.addAll(left.letDefinitions(ranges,pars)); 
      res.addAll(right.letDefinitions(ranges,pars)); 
    } 
    
    return res; 
  }

  public Expression excel2Ocl(Entity target, Vector entities, Vector qvars, Vector antes)
  { BinaryExpression enew = (BinaryExpression) clone(); 
    enew.left = left.excel2Ocl(target, entities, qvars, antes); 
    enew.right = right.excel2Ocl(target, entities, qvars, antes); 
    return enew; 
  } 
  
  public void getParameterBounds(Vector atts, Vector bounds, java.util.Map attBounds)
  { if ("&".equals(operator))
    { left.getParameterBounds(atts,bounds,attBounds);   
      right.getParameterBounds(atts,bounds,attBounds); 
    }
    else if ("<".equals(operator) || "<=".equals(operator) || ">".equals(operator) || "=".equals(operator) ||
    	     ">=".equals(operator))
    { bounds.add(this);
 
      for (int i = 0; i < atts.size(); i++) 
      { Attribute attr = (Attribute) atts.get(i); 
        if ((left + "").equals(attr + "") || 
            (right + "").equals(attr + ""))
        { Vector abounds = (Vector) attBounds.get(attr + ""); 
          if (abounds == null) 
          { abounds = new Vector(); }
          abounds.add(this); 
          attBounds.put(attr + "", abounds); 
        } 
      } 
    } 
  } 

  public Expression getUpperBound(String attname)
  { if (("<".equals(operator) || 
         "<=".equals(operator) || 
         "=".equals(operator)) && 
         attname.equals(left + "") &&
         (right.umlkind == VALUE || 
          Expression.isValue(right))
        ) 
     { return right; } 
     
     if ((">".equals(operator) || 
         ">=".equals(operator) || 
         "=".equals(operator)) && 
         attname.equals(right + "") &&
         (left.umlkind == VALUE || Expression.isValue(left))
        ) 
     { return left; } 
    
     return null; 
  } 

  public Expression getLowerBound(String attname)
  { if (("<".equals(operator) || 
         "<=".equals(operator) || 
         "=".equals(operator)) && 
         attname.equals(right + "") &&
         (left.umlkind == VALUE || Expression.isValue(left))
        ) 
     { return left; } 
     
     if ((">".equals(operator) || 
          ">=".equals(operator) || 
          "=".equals(operator)) && 
         attname.equals(left + "") &&
         (right.umlkind == VALUE || Expression.isValue(right))
        ) 
     { return right; } 
    
     return null; 
  } 

  
  // The following turns  x : e & P & l = g & Q into [x,P,l,Q] in allvars
  public Vector splitToCond0Cond1(Vector conds, Vector pars, 
                                  Vector qvars, Vector lvars, Vector allvars)
  { Expression cond0 = (Expression) conds.get(0);  // predicates defining variables
    Expression cond1 = (Expression) conds.get(1);  // other predicates
    Vector res = new Vector(); 

    if (operator.equals(":") && left.umlkind == VARIABLE)  // && not in qvars
    { if (qvars.contains(left + "") || lvars.contains(left + "") || pars.contains(left + "")) 
      // condition on existing variable
      { Expression c2 = Expression.simplifyAnd(cond1,this); 
        res.add(cond0); 
        res.add(c2);
        Type t1 = left.getType(); 
        Type t2 = right.getElementType(); 
        left.setType(Type.mostSpecificType(t1,t2)); 
        // System.out.println("DEDUCED TYPE: " + left.getType() + " of " + left); 
        // allvars.add(this); 
      }  
      else 
      { qvars.add(left + ""); 
        allvars.add(left + ""); 
        Expression c0 = Expression.simplifyAnd(cond0,this); 
        res.add(c0); 
        res.add(cond1);
      }  
    } // add to qvars
    else if (operator.equals("=") && left.umlkind == VARIABLE)
    { if (qvars.contains(left + "") || lvars.contains(left + "") || pars.contains(left + "")) 
      // condition on existing variable
      { Expression c2 = Expression.simplifyAnd(cond1,this); 
        res.add(cond0); 
        res.add(c2);
        // allvars.add(this); 
      }
      else 
      { lvars.add(left + ""); 
        allvars.add(left + ""); 
        Expression c0 = Expression.simplifyAnd(cond0,this); 
        res.add(c0); 
        res.add(cond1);
      }  
    } // add to lvars
    else if (operator.equals("&"))
    { Vector res1 = left.splitToCond0Cond1(conds,pars,qvars,lvars,allvars); 
      res = right.splitToCond0Cond1(res1,pars,qvars,lvars,allvars); 
    } 
    else // it is a cond1 formula
    { Expression c1 = Expression.simplifyAnd(cond1,this); 
      res.add(cond0); 
      res.add(c1); 
      // allvars.add(this); 
    } 
    
    return res; 
  } 

  public static Expression findDefinitionOf(Expression key, Vector exps)
  { // finds e where v : e is in exps with v + "" = key + ""
    for (int i = 0; i < exps.size(); i++) 
    { Expression ex = (Expression) exps.get(i); 
      if (ex instanceof BinaryExpression) 
      { BinaryExpression bex = (BinaryExpression) ex; 
        if (bex.operator.equals(":") && (bex.left + "").equals(key + ""))
        { return bex.right; } 
      } 
    } 
    return null; 
  } 

  public static void replaceDefinition(Expression key, Expression newvalue, Vector exps)
  { // replaces e where v : e is in exps with v + "" = key + "" by newvalue

    for (int i = 0; i < exps.size(); i++) 
    { Expression ex = (Expression) exps.get(i); 
      if (ex instanceof BinaryExpression) 
      { BinaryExpression bex = (BinaryExpression) ex; 
        if (bex.operator.equals(":") && (bex.left + "").equals(key + ""))
        { bex.right = newvalue;
          return; 
        } 
      } 
    } 
  } 

  // The following turns  x : e & P & l = g & Q into [x,P,l,Q] in allvars
  public Vector splitToCond0Cond1Pred(Vector conds, Vector pars, 
                                 Vector qvars, Vector lvars, 
                                 Vector allvars, Vector allpreds)
  { Expression cond0 = (Expression) conds.get(0); 
    Expression cond1 = (Expression) conds.get(1); 
    Vector res = new Vector(); 
    // System.out.println("Split conds on: " + this);

    if (operator.equals(":") && left.umlkind == VARIABLE)  // && not in qvars
    { if (qvars.contains(left + "") || lvars.contains(left + "") || pars.contains(left + "")) 
      // condition on existing variable
      { if (Expression.isSimpleEntity(right))
        { // if previous occurrence was also  left : Entity, replace that by left : Specific
          // where Specific is the most specific of the two. 
          Expression vardef = BinaryExpression.findDefinitionOf(left,allpreds); 
          if (vardef != null && Expression.isSimpleEntity(vardef))
          { Entity eold = vardef.getElementType().getEntity(); 
            Entity enew = right.getElementType().getEntity(); 
            if (eold != null && enew != null && Entity.isAncestor(eold,enew))
            { BinaryExpression.replaceDefinition(left,right,allpreds); 
              left.setType(new Type(enew)); 
            } 
            else 
            { } // new condition is redundant
            res.add(cond0); 
            res.add(cond1); 
          } 
          else // is genuine new condition 
          { Expression c2 = Expression.simplifyAnd(cond1,this); 
            res.add(cond0); 
            res.add(c2);
            Type t1 = left.getType(); 
            Type t2 = right.getElementType(); 
            left.setType(Type.mostSpecificType(t1,t2)); 
        // System.out.println("DEDUCED TYPE: " + left.getType() + " of " + left); 
            allvars.add(this);
            allpreds.add(this); 
          }
        } 
        else  
        { Expression c2 = Expression.simplifyAnd(cond1,this); 
          res.add(cond0); 
          res.add(c2);
          Type t1 = left.getType(); 
          Type t2 = right.getElementType(); 
          left.setType(Type.mostSpecificType(t1,t2)); 
        // System.out.println("DEDUCED TYPE: " + left.getType() + " of " + left); 
          allvars.add(this);
          allpreds.add(this); 
        }  
      }  
      else 
      { qvars.add(left + ""); 
        allvars.add(left + ""); 
        Expression c0 = Expression.simplifyAnd(cond0,this); 
        res.add(c0); 
        res.add(cond1);
        allpreds.add(this); 
      }  
    } // add to qvars
    else if (operator.equals("=") && left.umlkind == VARIABLE)
    { if (qvars.contains(left + "") || lvars.contains(left + "") || pars.contains(left + "")) 
      // condition on existing variable
      { Expression c2 = Expression.simplifyAnd(cond1,this); 
        res.add(cond0); 
        res.add(c2);
        allvars.add(this);
        allpreds.add(this);  
      }
      else 
      { lvars.add(left + ""); 
        allvars.add(left + ""); 
        Expression c0 = Expression.simplifyAnd(cond0,this); 
        res.add(c0); 
        res.add(cond1);
        allpreds.add(this); 
      }  
    } // add to lvars
    else if (operator.equals("&"))
    { Vector res1 = left.splitToCond0Cond1Pred(conds,pars,qvars,lvars,allvars,allpreds); 
      res = right.splitToCond0Cond1Pred(res1,pars,qvars,lvars,allvars,allpreds); 
    } 
    else // it is a cond1 formula
    { Expression c1; 
      if (cond1 == null) 
      { c1 = this; } 
      else 
      { c1 = new BinaryExpression("&",cond1,this); }  
      res.add(cond0); 
      res.add(c1); 
      allvars.add(this);
      allpreds.add(this);  
      // System.out.println("Added condition: " + this); 
    } 
    
    return res; 
  } 

  // Assumes all existential quantifiers are first: 

  public Vector splitToTCondPost(Vector tvars, Vector conds)
  { Expression cond0 = (Expression) conds.get(0);  // TCond
    Expression cond1 = (Expression) conds.get(1);  // Post
    Vector res = new Vector(); 

    // This is a TCond conjunct if: it only has a tvar as base of 
    // its basic expressions, or they are values

    if (operator.equals("&"))
    { Vector res1 = left.splitToTCondPost(tvars,conds); 
      res = right.splitToTCondPost(tvars,res1);
      return res;  
    } 

    Vector vleft = left.innermostVariables(); 
    // System.out.println("Innermost variables of " + left + " = " + vleft); 
    Vector vright = right.innermostVariables(); 
    // System.out.println("Innermost variables of " + right + " = " + vright); 
    if (tvars.containsAll(vleft) && tvars.containsAll(vright)) 
    { Expression c0 = Expression.simplifyAnd(cond0,this); 
      res.add(c0); 
      res.add(cond1); 
    } 
    else // it is a post formula
    { Expression c1 = Expression.simplifyAnd(cond1,this); 
      res.add(cond0); 
      res.add(c1); 
    } 
    
    return res; 
  }

  public Vector updateVariables()  // updated variables in the expression
  { Vector res = new Vector(); 
    if (operator.equals("&") || operator.equals("#") || operator.equals("#LC") ||
        operator.equals("#1"))
    { res = left.updateVariables(); 
      return VectorUtil.union(res,right.updateVariables()); 
    } 
    /* else if (operator.equals("->forAll") || */ 
    else if ( operator.equals("!") || operator.equals("=>"))
    { return right.updateVariables(); } 
    else if (operator.equals("="))
    { if (left instanceof BasicExpression)
      { res.add(left); } // and l->at(e) = val also
    } 
    else if (operator.equals(":") || operator.equals("<:") ||
             operator.equals("/:") || operator.equals("/<:"))
    { if (right instanceof BasicExpression)
      { res.add(right); } 
    } // Not quite.  x /: e.r.att can remove an element of r. 
    else if (operator.equals("->includesAll") || operator.equals("->includes") ||
             operator.equals("->excludesAll") || operator.equals("->excludes"))
    { if (left instanceof BasicExpression)
      { res.add(left); } 
    } 
    return res; 
  } 

  public int maxModality()
  { int ml = left.maxModality();
    int mr = right.maxModality();
    if (ml > mr)
    { return ml; }
    return mr;
  }

  public int minModality()
  { int ml = left.minModality();
    int mr = right.minModality();
    if (ml > mr && mr > ModelElement.NONE)
    { return mr; }
    return ml; // could be NONE
  }

  public String toB()
  { String op; 
    if (operator.equals("!=") || operator.equals("<>"))
    { op = "/="; } 
    else 
    { op = operator; } 
    String basicString = left.toB() + " " + op + " " + right.toB(); 
    if (needsBracket)
    { return "(" + basicString + ")"; }  // eg, for or inside & 
    else
    { return basicString; }
  }

  // Should not use this in Statement:
  public String toJava()
  { String javaop; 
    if (operator.equals("="))
    { javaop = "=="; } 
    else if (operator.equals("&") || operator.equals("and"))
    { javaop = "&&"; } 
    else if (operator.equals("or"))
    { javaop = "||"; } 
    else if (operator.equals("/=") || operator.equals("<>"))
    { javaop = "!="; } 
    else if (operator.equals("mod"))
    { javaop = "%"; } 
    else if (operator.equals("div"))
    { javaop = "/"; } 
    else 
    { javaop = operator; } 

    String basicString = left.toJava() + " " + javaop + " " + right.toJava(); 
    if (needsBracket)
    { return "(" + basicString + ")"; }  // eg, for or inside & 
    else
    { return basicString; }
  } 

  public String toJavaImp(Vector components)
  { String javaop;
    if (operator.equals("="))
    { javaop = "=="; }
    else if (operator.equals("&") || operator.equals("and"))
    { javaop = "&&"; }
    else if (operator.equals("or"))
    { javaop = "||"; }
    else if (operator.equals("/=") || operator.equals("<>"))
    { javaop = "!="; } 
    else if (operator.equals("mod"))
    { javaop = "%"; } 
    else if (operator.equals("div"))
    { javaop = "/"; } 
    else
    { javaop = operator; }

    String basicString = left.toJavaImp(components) + " " + javaop + " " + 
                         right.toJavaImp(components);
    if (needsBracket)
    { return "(" + basicString + ")"; }  // eg, for or inside & 
    else
    { return basicString; }
  }


  public Expression toSmv(Vector cnames)
  { String smvop; 
    if (operator.equals("=>"))
    { smvop = "->"; }  
    else if (operator.equals("or"))
    { smvop = "|"; } 
    else 
    { smvop = operator; } 

    return new BinaryExpression(smvop, left.toSmv(cnames),
                                right.toSmv(cnames)); 
  } 

  public String toImp(final Vector components) 
  {
    return left.toImp(components) + " " + operator + " " + 
           right.toImp(components); 
  }

  public boolean isOrExpression()
  { return operator.equals("or"); } 


  public Expression buildJavaForm(final Vector comps)
  { Expression newleft;

    if (operator.equals("="))
    { BasicExpression bel = (BasicExpression) left;
      BasicExpression ber = (BasicExpression) right;
      Statemachine sm = 
        (Statemachine) VectorUtil.lookup(bel.data, comps);
      if (sm != null && sm.getMultiplicity() <= 1)
      { newleft = new BasicExpression("M" + sm.label + "." + sm.label); 
        javaForm = new BinaryExpression("==", newleft, right);
        return javaForm; 
      }
      else
      { Statemachine smr = 
          (Statemachine) VectorUtil.lookup(ber.data, comps);
        if (smr != null && smr.getMultiplicity() <= 1)
        { newleft = new BasicExpression("M" + smr.label + "." + smr.label) ;
          javaForm = new BinaryExpression("==", newleft, left);
          return javaForm; 
        } 
      } 
    }
    else if (operator.equals("&"))
    { Expression jaf1 = left.buildJavaForm(comps);
      Expression jaf2 = right.buildJavaForm(comps);
      javaForm = new BinaryExpression("&&",jaf1,jaf2);
      return javaForm;  
    }
    else if (operator.equals("or"))
    { Expression jaf1 = left.buildJavaForm(comps);
      Expression jaf2 = right.buildJavaForm(comps);
      javaForm = new BinaryExpression("||",jaf1,jaf2);
      return javaForm;
    }
    javaForm = this;   // for multiple instance  ac1.ac, etc. 
    return javaForm; 
  }


  public int typeCheck(Vector sms)
  { if (comparitors.contains(operator))
    { return typeCheckEq(sms); }
    else if (operator.equals("&") || operator.equals("or"))
    { return typeCheckAnd(sms); }
    else 
    { System.out.println("Unexpected operator: " + operator);
      modality = ERROR; 
      return ERROR; 
    }
  }

  public boolean typeCheck(final Vector types,
                           final Vector entities,
                           final Vector contexts, final Vector env)
  { Vector context = new Vector(); 
  
    if (operator.equals(","))
    { left.typeCheck(types,entities,contexts,env); 
      right.typeCheck(types,entities,contexts,env); 
      type = right.type; 
      elementType = right.elementType; 
      multiplicity = right.multiplicity; 
      return true; 
    }

    if (operator.equals("let") && accumulator != null)
    { boolean lrt = left.typeCheck(types,entities,contexts,env); 
      context.addAll(contexts); 

      Vector env1 = new Vector(); 
      env1.addAll(env); 
      env1.add(accumulator); 
      boolean rtc = right.typeCheck(types,entities,context,env1);
      // type = new Type("Function",accumulator.getType(),right.type);
      // elementType = right.type; 
      type = right.type; 
      elementType = right.elementType;  
      System.out.println(">>> Typechecked let expression: " + lrt + " " + rtc + " " + type); 
      return true; 
    }


    if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC") || operator.equals("!"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      boolean lrt = lexp.right.typeCheck(types,entities,contexts,env); 

      // lexp.right must be multiple, also 

      Type et = lexp.right.elementType; 

      if (et == null)
      { System.err.println("!!! TYPE ERROR: null element type in " + lexp.right + " in " + this); 
        // JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
        //   "Type error", JOptionPane.ERROR_MESSAGE);
        et = new Type("OclAny", null); 
      } 

      context.addAll(contexts); 

      Attribute att = 
        new Attribute(lexp.left + "",et,ModelElement.INTERNAL); 
      att.setEntity(et.getEntity()); 
      att.setElementType(et.getElementType()); 
        // FOR (s->subcollections())->exists( x | ... )
      Vector env1 = new Vector(); 
      env1.addAll(env); 
      env1.add(att); 
      lexp.typeCheck(types,entities,contexts,env1); 
      boolean rtc = right.typeCheck(types,entities,context,env1);
      type = new Type("boolean",null); 
      elementType = new Type("boolean", null); 
      return true; 
    } 

    if (operator.equals("->exists") || operator.equals("->exists1") ||
        "->existsLC".equals(operator) || operator.equals("->isUnique") || 
        operator.equals("->forAll"))
    { 
      boolean lrt = left.typeCheck(types,entities,contexts,env); 
      Type et = left.elementType; 
      if (et != null && et.isEntity())
      { context.add(et.getEntity()); } 
      context.addAll(contexts); 
      // left must be multiple

      // Attribute att = 
      //   new Attribute(et.getName().toLowerCase() +
      //     "x",et,ModelElement.INTERNAL); 
      // Vector env1 = new Vector(); 
      // env1.addAll(env); 
      // env1.add(att); 
      // lexp.typeCheck(types,entities,env1); 
      boolean rtc = right.typeCheck(types,entities,context,env);
      type = new Type("boolean",null); 
      elementType = new Type("boolean", null); 
      return true; 
    }
    else if (operator.equals("|") || operator.equals("|R") || "|sortedBy".equals(operator) || 
	         "|selectMinimals".equals(operator) || "|selectMaximals".equals(operator))
    { BinaryExpression lexp = (BinaryExpression) left; 
      boolean lrt = lexp.right.typeCheck(types,entities,contexts,env); 

      // lexp.right must be multiple 

      Type et = lexp.right.elementType;
      if (et == null) 
      { Type tt = lexp.right.getType(); 
        if (tt != null) 
        { et = tt.getElementType(); } 
        System.out.println(">> Type of " + lexp.right + " = " + tt + "(" + et + ")"); 
      }  

      if (et == null)
      { System.err.println("Warning: no element type for " + lexp.right + " in " + this); 
        // JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
        //      "Type error", JOptionPane.ERROR_MESSAGE); 
        et = new Type("OclAny", null); 
      } 

      //  && et.isEntity())
      // { context.add(et.getEntity()); } 
      context.addAll(contexts); 

      Attribute att = 
        new Attribute(lexp.left + "",et,ModelElement.INTERNAL); 
      att.setEntity(et.getEntity()); 
      att.setElementType(et.getElementType()); 
       // FOR (s->subcollections())->exists( x | ... )
      Vector env1 = new Vector(); 
      env1.addAll(env); 
      env1.add(att); 
      lexp.typeCheck(types,entities,contexts,env1); 
      boolean rtc = right.typeCheck(types,entities,context,env1);
      Type stleft = lexp.right.getType(); 
      Type stright = right.getType();
      Entity seleft = lexp.right.getEntity();
      tcSelect(stleft,stright,seleft); 
      return true; 
    }        
    else if (operator.equals("->select") || operator.equals("->sortedBy") ||  
             operator.equals("->reject") || operator.equals("->selectMinimals") || 
			 operator.equals("->selectMaximals"))
    { // System.out.println("Type-checking select/reject " + this); 
      left.typeCheck(types,entities,contexts,env);
      Vector scontext = new Vector(); 
      Type stleft = left.getType();
      // must be collection type 
      Type seltype = left.elementType; 
      if (seltype != null && seltype.isEntity())
      { scontext.add(seltype.getEntity()); } 
      else if (stleft != null && stleft.isEntity())
      { scontext.add(stleft.getEntity()); }   
      else if (seltype == null) 
      { System.err.println("!!! TYPE ERROR: no valid element type in " + left + " in " + this); 
        // JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
        //        "Type error", JOptionPane.ERROR_MESSAGE);
      } 

      scontext.addAll(contexts); 
      right.typeCheck(types,entities,scontext,env); 
      Type stright = right.getType();
      Entity seleft = left.getEntity();
      tcSelect(stleft,stright,seleft); 
      return true; 
    }  
    else if (operator.equals("->collect") || operator.equals("->unionAll") ||
             operator.equals("->intersectAll") || operator.equals("->concatenateAll"))   // and |C
    { // System.out.println("Type-checking collect " + this); 
      left.typeCheck(types,entities,contexts,env);
      Vector ccontext = new Vector(); 
      Type ctleft = left.getType();
      // must be a collection type 
      Type celtype = left.elementType; 
      if (celtype != null && celtype.isEntity())
      { ccontext.add(celtype.getEntity()); } 
      else if (ctleft != null && ctleft.isEntity())
      { ccontext.add(ctleft.getEntity()); }   
      else if (celtype == null) 
      { System.err.println("Warning: no element type in " + left + " in " + this); 
        // JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
        //      "Type error", JOptionPane.ERROR_MESSAGE);
        celtype = new Type("OclAny", null); 
      } 

      ccontext.addAll(contexts); 
      right.typeCheck(types,entities,ccontext,env); 
      Type ctright = right.getType();
      Entity ceright = right.getEntity();
      tcCollect(ctleft,ctright,ceright); 
      return true; 
    } // as for exists etc, include |C
    else if (operator.equals("|C") || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { BinaryExpression lexp = (BinaryExpression) left;
      // must be a collection

      boolean lrt = lexp.right.typeCheck(types,entities,contexts,env);
      Type et = lexp.right.elementType;

      if (et == null) 
      { Type tt = lexp.right.getType(); 
        if (tt != null) 
        { et = tt.getElementType(); } 
        System.out.println(">> Type of " + lexp.right + " = " + tt + "(" + et + ")"); 
      }  

      if (et == null)
      { System.err.println("Warning: no element type for " + lexp.right + " in " + this);                     
        // JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this,
        //  "Type error", JOptionPane.ERROR_MESSAGE);
        et = new Type("OclAny", null); 
      }

      context.addAll(contexts);
      Attribute att = 
        new Attribute(lexp.left + "", et, ModelElement.INTERNAL);
      if (et != null) 
      { att.setElementType(et.getElementType()); } 

      Vector env1 = new Vector();
      env1.addAll(env);
      env1.add(att);
      lexp.typeCheck(types,entities,contexts,env1);
      boolean rtc = right.typeCheck(types,entities,context,env1);
      Type stleft = lexp.right.getType();
      Type stright = right.getType();
      Entity seright = null;
      if (right.elementType != null)
      { if (right.elementType.isEntity())
        { seright = right.elementType.getEntity(); }
      }
      tcCollect(stleft,stright,seright);
      // System.out.println("TYPE OF " + this + " IS " + type + ", " + elementType); 
      return true;
    }
    else if (operator.equals("->any"))
    { left.typeCheck(types,entities,contexts,env);
      Vector ccontext = new Vector(); 
      Type ctleft = left.getType();
      // must be a collection type 
      Type celtype = left.elementType; 
      if (celtype != null && celtype.isEntity())
      { ccontext.add(celtype.getEntity()); } 
      else if (ctleft != null && ctleft.isEntity())
      { ccontext.add(ctleft.getEntity()); }   
      else if (celtype == null) 
      { System.err.println("Warning: no element type in " + left + " in " + this); 
        // JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
        //  "Type error", JOptionPane.ERROR_MESSAGE);
        celtype = new Type("OclAny", null); 
      } 

      ccontext.addAll(contexts); 
      right.typeCheck(types,entities,ccontext,env); 
      type = celtype; // for ->any(Pred)
      if (type != null && type.isCollectionType())
      { multiplicity = ModelElement.MANY; 
        elementType = type.getElementType(); 
      } 
      return true; 
    }
    else if (operator.equals("|A"))
    { BinaryExpression lexp = (BinaryExpression) left;
      // must be a collection

      boolean lrt = lexp.right.typeCheck(types,entities,contexts,env);
      Type et = lexp.right.elementType;
      if (et == null)
      { System.err.println("Warning: no element type for " + lexp.right + " in " + this);
                     
        // JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this,
        //   "Type error", JOptionPane.ERROR_MESSAGE);
        et = new Type("OclAny", null); 
      }

      context.addAll(contexts);
      Attribute att = 
        new Attribute(lexp.left + "", et, ModelElement.INTERNAL);
      if (et != null) 
      { att.setElementType(et.getElementType()); } 

      Vector env1 = new Vector();
      env1.addAll(env);
      env1.add(att);
      lexp.typeCheck(types,entities,contexts,env1);
      boolean rtc = right.typeCheck(types,entities,context,env1);
      Type stleft = lexp.right.getType();
      Type stright = right.getType();
      Entity seright = null;
      if (right.elementType != null)
      { if (right.elementType.isEntity())
        { seright = right.elementType.getEntity(); }
      }
      // tcCollect(stleft,stright,seright);
      // System.out.println("TYPE OF " + this + " IS " + type + ", " + elementType); 
      type = et; // for ->any(x | Pred)
      if (type != null && type.isCollectionType())
      { multiplicity = ModelElement.MANY; 
        elementType = type.getElementType(); 
      } 
      return true;
    }
    else if ("->closure".equals(operator))
    { type = new Type("Set",null); 
      context.addAll(contexts); 
      left.typeCheck(types,entities,context,env); 

      if (left.elementType != null && left.elementType.isEntity()) 
      { entity = left.elementType.getEntity(); } 
      else 
      { entity = left.entity; } // the owner of the relation being closured

      if (left.isMultiple()) {} 
      else 
      { System.err.println("ERROR: LHS of " + this + " must be collection");                             
        JOptionPane.showMessageDialog(null, "ERROR: LHS of " + this + " must be collection",                                                   "Type error", JOptionPane.ERROR_MESSAGE);
      }

      if (entity != null)       
      { Association ast = entity.getDefinedRole(right + ""); 
        elementType = left.elementType; 
        type.setElementType(elementType); 
        if (ast != null)
        { Entity entc = ast.closureEntity(); 
          if (entc != null) 
          { elementType = new Type(entc); 
            type.setElementType(elementType);
          } 
 
          if (ast.isClosurable()) { } 
          else 
          { JOptionPane.showMessageDialog(null, "ERROR: association in " + this + " cannot be closured",
		                                 "Expression error", JOptionPane.ERROR_MESSAGE);
          } 
          // System.out.println(">>> FOUND ASSOCIATION " + ast); 
        } 
        Vector context2 = new Vector(); 
        context2.add(entity); 
        right.typeCheck(types,entities,context2,env); 
      } 
      else 
      { System.err.println("!! ERROR: no entity for " + this);
        elementType = left.elementType; 
        type.setElementType(elementType); 
      } 
      multiplicity = ModelElement.MANY;
      // System.out.println(">>>> Element type = " + elementType + " TYPE= " + type + " ENTIY=" + entity);  
      return true; 
    } 
    
    boolean lt = false; 
    if (left == null) 
    { System.err.println(">>> SYNTAX ERROR: Left expression of " + this + " invalid!"); 
      JOptionPane.showMessageDialog(null, "Left expression of " + this + " invalid!", 
                                      "Syntax error", JOptionPane.ERROR_MESSAGE);
      return false; 
    } 
    else 
    { lt = left.typeCheck(types,entities,contexts,env); } 

    boolean rt = false; 

    if (right == null) 
    { System.err.println(">>> SYNTAX ERROR: Right expression of " + this + " invalid!"); 
      JOptionPane.showMessageDialog(null, "Right expression of " + this + " invalid!", 
                                      "Syntax error", JOptionPane.ERROR_MESSAGE);
      return false; 
    } 
    else 
    { rt = right.typeCheck(types,entities,contexts,env); } 

    // For a -> operator, treat left like objectRef 
    Type tleft = left.getType();
    Type tright = right.getType();
    Entity eleft = left.getEntity();
    Entity eright = right.getEntity();

    if (operator.equals(":") && left.umlkind == VARIABLE) // or UNKNOWN
    { Attribute var = (Attribute) ModelElement.lookupByName(left + "",env); 
      if (var == null && !("result".equals(left + "")))
      { // add it for subsequent type checking 
        Attribute att = 
          new Attribute(left + "",right.elementType,ModelElement.INTERNAL); 
        env.add(att); 
        if (right.elementType != null) 
        { att.setElementType(right.elementType.getElementType()); }  
      } // set the entity to eleft or eright? 

      if (left.type == null) 
      { left.type = right.elementType; } 
    } 

    if (operator.equals("=") && left.umlkind == VARIABLE) // or UNKNOWN
    { Attribute var = (Attribute) ModelElement.lookupByName(left + "",env); 
      if (var == null && !("result".equals(left + "")))
      { // add it for subsequent type checking 
        Attribute att = 
          new Attribute(left + "",right.type,ModelElement.INTERNAL); 
        att.setElementType(right.elementType); 
        env.add(att); 
      }  // set the entity to eleft?
 
      if (left.type == null) 
      { left.type = right.type; } 

      if (left.elementType == null) 
      { left.elementType = right.elementType; } 
      // System.out.println("SETTING VARIABLE TYPE: " + type + " " + elementType + " " + var); 
    } 

    // ->oclAsType(T)? T.umlkind == CLASSID, and type = T.elementType

    if ("->count".equals(operator) || 
        "->indexOf".equals(operator) || 
        operator.equals("->lastIndexOf") ||
        operator.equals("->compareTo"))
    { type = new Type("int",null); } 
    else if ("->oclAsType".equals(operator))
    { type = Type.getTypeFor(right + "", types, entities); 
      if (type == null) 
      { System.err.println("!! ERROR: unrecognised type in cast expression: " + this);
        // JOptionPane.showMessageDialog(null, "Unrecognised type in cast " + this, 
        // "Type error", JOptionPane.ERROR_MESSAGE);
        type = left.getType(); 
      }
      else if (type.getElementType() != null) 
      { elementType = type.getElementType(); } 
      else 
      { elementType = left.getElementType(); } 
    }  
    else if ("->at".equals(operator))
    { if ("String".equals(left.type + ""))
      { type = new Type("String", null); 
        elementType = type; 
      }  
      else 
      { type = left.elementType;
        if (type != null) 
        { elementType = type.getElementType(); } 
        else 
        { Attribute leftvar = (Attribute) ModelElement.lookupByName(left + "", env); 
          if (leftvar != null) 
          { type = leftvar.getType(); 
            elementType = leftvar.getElementType(); 
          }

          if (elementType == null) 
          { System.err.println("! Warning: no element type for " + left + " in environment " + env); 
            elementType = new Type("OclAny", null); 
          } 
        }  
      } 
    }  // and right must be of type int.
    else if ("->apply".equals(operator)) 
    { // lhs must be a function
      Type ftype = left.getType(); 

      if (ftype != null) 
      { type = ftype.getElementType(); }

      if (type == null) 
      { System.err.println("Warning: no function result type in " + this); 
        type = new Type("OclAny", null); 
      } 
      else 
      { elementType = type.elementType; } 
    }  
    else if ("->pow".equals(operator))
    { type = new Type("double",null); } 
    else if ("->gcd".equals(operator))
    { type = new Type("long",null); } 
    else if (operator.equals("div"))
    { type = left.getType(); } 
    else if ("->hasPrefix".equals(operator) || 
             "->hasSuffix".equals(operator) ||
             "->hasMatch".equals(operator) || 
             "->isMatch".equals(operator) ||
             operator.equals("->equalsIgnoreCase") ||
             "->oclIsKindOf".equals(operator) || 
             "->oclIsTypeOf".equals(operator))
    { type = new Type("boolean",null); } 
    else if (operator.equals("->before") || operator.equals("->after") || operator.equals("->firstMatch")) 
    { type = new Type("String",null); 
      elementType = new Type("String", null); 
    }  
    else if (operator.equals("->excludingAt") && left.isString()) 
    { type = new Type("String",null);
      elementType = new Type("String", null); 
    }  
    else if (operator.equals("->excludingAt") && left.isCollection()) 
    { type = left.type;
      elementType = left.elementType; 
    }  
    else if ("->split".equals(operator) || "->allMatches".equals(operator))
    { type = new Type("Sequence",null); 
      elementType = new Type("String",null);
      type.elementType = elementType;  
    } 
    else if ("->restrict".equals(operator) || 
             "->antirestrict".equals(operator))
    { if (tleft != null) 
      { type = tleft; 
        elementType = left.elementType; 
      } 
      else 
      { type = new Type("Map", null); } 
    } 
    else if (comparitors.contains(operator))
    { tcEq(tleft,tright,eleft,eright); }
    else if (operator.equals("+"))
    { tcPlus(tleft,tright,eleft,eright); }
    else if (operator.equals("-"))
    { tcMinus(tleft,tright,eleft,eright); }
    else if (operator.equals("\\/") || operator.equals("^") || operator.equals("->append") ||
             operator.equals("->union") || operator.equals("->including") ||
             operator.equals("->symmetricDifference") || 
             operator.equals("->excluding") ||
             operator.equals("->excludingAt") ||
             operator.equals("->excludingFirst") ||
             operator.equals("->prepend") || 
             operator.equals("->append") || 
             operator.equals("->intersection") || 
             operator.equals("/\\"))
    { tcSetOps(tleft,tright,eleft,eright); }
    else if (operator.equals("*") || operator.equals("mod") ||
             operator.equals("div") || operator.equals("/"))
    { tcMathOps(tleft,tright,eleft,eright); }
    else if (operator.equals("|->"))
    { type = tright; } // hack
    else if (operator.equals("<+"))
    { type = tleft; } // hack
    else if (operator.equals(":") ||
             operator.equals("<:") || operator.equals("->includesAll") ||
             operator.equals("/<:") || operator.equals("->excludesAll") ||
             operator.equals("/:"))
    { boolean rmult = right.isMultiple();
      type = new Type("boolean",null);
      // System.out.println(left + " type= " + tleft); 
      // System.out.println(right + " type= " + tright); 

      if (rmult) 
      { }
      else 
      { System.err.println("!! TYPE ERROR: RHS of " + this + " must be a collection");
        // JOptionPane.showMessageDialog(null, "RHS of " + this + " must be a collection!", 
        //    "Type error", JOptionPane.ERROR_MESSAGE);
      } // deduce type of one side from that of other

      if (tleft == null && tright != null)
      { if (operator.equals(":") || operator.equals("/:"))
        { left.setType(right.getElementType()); } 
        else // if (operator.equals("<:") || operator.equals("/<:"))
        { left.setType(tright); } 
      } 
      else if (tright == null && tleft != null) 
      { if (operator.equals(":") || operator.equals("/:"))
        { Type rst = new Type("Set", null); 
          right.setType(rst); 
          if (right.elementType == null && eleft != null)
          { right.setElementType(tleft); // new Type(eleft)); 
            rst.setElementType(tleft); 
          }
        }
        else 
        { right.setType(tleft); } 
      } // and set one element type to the other when not null
    }
    else if (operator.equals("->includes") || operator.equals("->excludes"))
    { boolean lmult = left.isMultiple();
      type = new Type("boolean",null); 
      if (lmult) 
      { }
      else 
      { System.err.println("TYPE ERROR: LHS of " + this + " must be a collection");
       JOptionPane.showMessageDialog(null, "LHS of " + this + " must be a collection!", 
                                     "Type error", JOptionPane.ERROR_MESSAGE);
      } // deduce type of one side from that of other

      if (tright == null && tleft != null)
      { right.setType(left.getElementType()); } 
      else if (tleft == null && tright != null) 
      { Type lst = new Type("Set", null); 
        left.setType(lst); 
        if (left.elementType == null && eright != null)
        { left.setElementType(tright); // new Type(eright)); 
          lst.setElementType(tright); // new Type(eright)); 
        }
      } // and set one element type to the other when not null
    }
    else if (operator.equals("&") || operator.equals("<=>") || operator.equals("xor") ||  
             operator.equals("or") || operator.equals("=>"))
    { tcLogical(tleft,tright,eleft,eright); } 
    // else, it is an extension operator, type is given by its definition
    else 
    { type = Expression.getOperatorType(operator); } 

    if (type == null)
    { System.out.println("Warning: No type found for " + this); 
      type = new Type("OclAny", null);  
                                    
      return false; 
    }
    return true;
  }
  // x : y, x <: y is x = y if y single. x /: y, x /<: y is x /= y

// rolea = roleb ?? one multiple other not?
  private void tcEq(Type tleft, Type tright,
                    Entity eleft, Entity eright)
  { if (tleft != null && tright != null)
    { String tlname = tleft.getName();
      String trname = tright.getName();
      if (tleft.equals(tright))
      { type = new Type("boolean",null); }
      else if (Type.isNumericType(tlname) && Type.isNumericType(trname))
      { type = new Type("boolean",null); }  // not valid to assign though
      else if (left.elementType != null && trname.equals("" + left.elementType))
      { type = new Type("boolean",null); }  
      else if (right.elementType != null && tlname.equals("" + right.elementType))
      { type = new Type("boolean",null); } 
      else // or if same element types
      { System.err.println("WARNING: different " +
                           "types " + tleft + " " + tright + " in comparison " + this);
        type = new Type("boolean",null);
      }
    }
    else if (eleft != null && eright != null)
    { if (eleft.equals(eright)) // only valid for =, /=
      { type = new Type("boolean",null); }
      else 
      { Entity common = Entity.commonSuperclass(eleft,eright); 
        if (common != null)
        { // System.out.println("Common superclass is: " + common); 
          type = new Type("boolean",null); 
        }
        else   
        { System.err.println("WARNING: incompatible " +
                             "entities in comparison " + this);
          type = new Type("boolean",null);
        }
      } 
    }
    else 
    { if (tleft != null && right.getType() == null)
      { right.setType(tleft);
        right.setElementType(left.getElementType());
        type = new Type("boolean",null);
      }
      else if (tright != null)
      { left.setType(tright);
        left.setElementType(right.getElementType());
        type = new Type("boolean",null);
      }
      else 
      { System.err.println("!!! TYPE ERROR: Unable to type: " + this);
        // JOptionPane.showMessageDialog(null, "Unable to type " + this, 
        //         "Type error", JOptionPane.ERROR_MESSAGE);
        type = null; 
      }
    }
    type = new Type("boolean",null);
  }

  private void tcPlus(Type tleft, Type tright, Entity eleft, Entity eright)
  { if (tleft != null && tright != null)
    { String tlname = tleft.getName();
      String trname = tright.getName();
  
      if (tlname.equals("double") && Type.isNumericType(trname))
      { type = new Type("double",null); }
      else if (trname.equals("double") && Type.isNumericType(tlname))
      { type = new Type("double",null); }
      else if (tlname.equals("long") && Type.isNumericType(trname))
      { type = new Type("long",null); }
      else if (trname.equals("long") && Type.isNumericType(tlname))
      { type = new Type("long",null); }
      else if (tleft.equals(tright) && tlname.equals("int"))
      { type = new Type("int",null); }
      else if (tlname.equals("String") || trname.equals("String"))
      { type = new Type("String",null); }
      else
      { System.err.println("Warning: disallowed types " + tlname + " " + trname + " in +: " + this);
        // JOptionPane.showMessageDialog(null, "Disallowed types in " + this, 
        //                              "Type error", JOptionPane.WARNING_MESSAGE);
        type = tleft; // new Type("double",null);
      } 
    }
    else
    { if (tright == null && tleft != null)
      { right.setType(tleft);
        type = tleft;
      }
      else if (tleft == null && tright != null)
      { left.setType(tright);
        type = tright;
      }
      else 
      { System.err.println("TYPE ERROR: disallowed types " + tleft + " " + tright + " in +: " + this);
        // JOptionPane.showMessageDialog(null, "Disallowed types in " + this, 
        //                              "Type error", JOptionPane.ERROR_MESSAGE);
        type = new Type("OclAny", null); 
      }
    }
  }

  // also case of left - right with left multiple?
  private void tcMinus(Type tleft, Type tright,
                       Entity eleft, Entity eright)
  { // System.out.println("FOR " + this + " Left type is " + left.getType() + " Elementtype is: " + left.getElementType()); 

    if (Type.isCollectionType(left.getType()))
    { type = left.getType(); 
      elementType = left.getElementType(); 
    } 
    else if (left.isMultiple() && left.isOrdered())
    { type = new Type("Sequence",null); 
      elementType = left.elementType; 
      type.setElementType(elementType); 
    } 
    else if (left.isMultiple())
    { type = new Type("Set",null); 
      elementType = left.elementType; 
      type.setElementType(elementType); 
    } // also set entities 
    else if (tleft != null && tright != null)
    { String tlname = tleft.getName();
      String trname = tright.getName();
      if (tlname.equals("String") && trname.equals("String"))
      { type = new Type("String",null); } 
      else if (tlname.equals("Sequence"))
      { type = new Type("Sequence",null); 
        elementType = left.elementType; 
        type.setElementType(elementType);
      } 
      else if (tlname.equals("Set"))
      { type = new Type("Set",null); 
        elementType = left.elementType; 
        type.setElementType(elementType);
      } 
      else if (Type.isNumericType(trname) && tlname.equals("double"))
      { type = new Type("double",null); }
      else if (Type.isNumericType(tlname) && trname.equals("double"))
      { type = new Type("double",null); }
      else if (Type.isNumericType(trname) && tlname.equals("long"))
      { type = new Type("long",null); }
      else if (Type.isNumericType(tlname) && trname.equals("long"))
      { type = new Type("long",null); }
      else if (tleft.equals(tright) && tlname.equals("int"))
      { type = new Type("int",null); }
      else if (type == null)
      { System.err.println("Warning!: disallowed types in -: " + this); 
        // JOptionPane.showMessageDialog(null, "Disallowed types in " + this, 
        //                               "Type error", JOptionPane.ERROR_MESSAGE);
        type = tleft; 
      } 
    }
    else
    { System.err.println("TYPE ERROR: undefined types in -: " + this);
      if (tright == null && tleft != null)
      { right.setType(tleft);
        type = tleft;
      }
      else if (tleft == null && tright != null)
      { left.setType(tright);
        type = tright;
      }
      else 
      { type = new Type("OclAny", null); }
    }
    Type etleft = left.getElementType(); 
    Type etright = right.getElementType(); 
    if (etleft != null) // if classes, take closest common super
    { elementType = etleft;
      if (etright == null)
      { right.setElementType(etleft); } 
    } // should be the same
    else if (etright != null) 
    { elementType = etright;
      left.setElementType(etright); 
    } // seems wrong to do this. 
  }
  
  private void tcSetOps(Type tleft, Type tright,
                        Entity eleft, Entity eright)
  { // union and intersection

    String tlname = "Set"; 
    if (tleft != null)
    { tlname = tleft.getName(); } 
    else 
    { System.err.println("!! WARNING: no type for: " + left); } 

    String trname = "Set"; 
    if (tright != null) 
    { trname = tright.getName(); } 
    else 
    { System.err.println("!! WARNING: no type for: " + right); } 
    
      
    if (operator.equals("^") || operator.equals("->prepend") || operator.equals("->append"))
    { if ("Sequence".equals(tlname)) 
      { type = tleft; } 
      else 
      { System.err.println("!! WARNING: ^, ->prepend, ->append must be applied to a sequence: " + this);  
        type = new Type("Sequence",null);
      }  
    } 
    else if (operator.equals("->excluding") || 
             operator.equals("->excludingAt") ||
             operator.equals("->excludingFirst") ||
             operator.equals("->including") ||
             operator.equals("->restrict") ||
             operator.equals("->antirestrict") || 
             operator.equals("->intersection") ||
             operator.equals("/\\"))
    { if ("Sequence".equals(tlname))
      { type = tleft; }  
      else if ("Map".equals(tlname))
      { type = tleft; }  
      else if ("Set".equals(tlname))
      { type = tleft; } 
      else  
      { System.err.println("!! WARNING: unknown type for LHS of " + this);  
        type = new Type("Set",null); 
      } 
    } 
    else if (operator.equals("->union") || operator.equals("\\/")) 
    { if (tlname.equals("Sequence") && trname.equals("Sequence"))
      { type = tleft; } 
      else if (tlname.equals("Map") && trname.equals("Map"))
      { type = tleft; } 
      else if (tlname.equals("Set") && trname.equals("Set"))
      { type = tleft; }
      else
      { System.err.println("!! WARNING: different types for LHS and RHS of " + this);  
        type = tleft; 
      } 
    } 
    else // ->symmetricDifference
    { type = new Type("Set",null); 
      elementType = left.elementType; 
    } 
  

    if (operator.equals("^") || operator.equals("->union") ||
        operator.equals("->intersection") ||  
        // || operator.equals("->restrict") || 
        operator.equals("->symmetricDifference") ||
        operator.equals("/\\") || operator.equals("\\/"))
    { if (tright == null && tleft != null)
      { right.setType(tleft);
        type = tleft; // new Type("Set",null);
      }
      else if (tleft == null && tright != null)
      { left.setType(tright);
        type = tright; // new Type("Set",null);
      }
      else if (tleft == null && tright == null) 
      {        
        // JOptionPane.showMessageDialog(null, "Null types in " + this, 
        //                              "Type error", JOptionPane.ERROR_MESSAGE);
        System.err.println("!! Warning: null types on both sides of " + this);
        if (operator.equals("^"))
        { type = new Type("Sequence", null); } 
        else if (operator.equals("->restrict"))
        { type = new Type("Map", null); } 
        else 
        { type = new Type("Set",null); }
        // new Type("void", null);   
      }
    }

    if (operator.equals("->restrict") ||
        operator.equals("->antirestrict"))
    { if (type == null) 
      { type = new Type("Map", null); } 
    } 

    Type etleft = left.getElementType(); 
    Type etright = right.getElementType(); 
    Type maxtype = etleft; 
    if (Type.isSubType(etleft,etright))
    { maxtype = etright; } 
    
    if (operator.equals("->intersection") || 
        operator.equals("/\\") || 
        operator.equals("->restrict") ||
        operator.equals("->antirestrict") ||
        operator.equals("->excluding") ||
        operator.equals("->excludingAt") ||                 
        operator.equals("->excludingFirst")
       )
    { elementType = etleft;
      type.setElementType(elementType); 
    } 
    else if (operator.equals("->including") || 
             operator.equals("->prepend") || 
             operator.equals("->append"))
    { Type newleftET = Type.refineType(etleft,right.getType()); 
      System.out.println(">> Deduced element type of " + this + " = " + newleftET); 
      elementType = newleftET; 
      type.setElementType(newleftET); 
    } 
    else if (operator.equals("->union") || 
             operator.equals("^"))
    { Type newleftET = Type.refineType(etleft,etright); 
      System.out.println(">> Deduced element type of " + this + " = " + newleftET); 
      elementType = newleftET; 
      type.setElementType(newleftET); 
    } 
    else if (etleft != null) // if classes, take closest common super
    { elementType = maxtype;
      type.setElementType(elementType); 
      if (etright == null)
      { right.setElementType(etleft); } 
    } // should be the same
    else if (etright != null) 
    { elementType = maxtype; 
      type.setElementType(elementType); 
      left.setElementType(etright); 
    } // it's wrong to do this. 
    
    if (elementType != null && elementType.isEntity())
    { entity = elementType.getEntity(); } 

    if (isSorted())
    { type.setSorted(true); } 
  } 

  private void tcSelect(Type tleft, Type tright, Entity eleft)
  { // left should be set, sequence or map, right a boolean
    // All cases where result is a subset or rearrangement of the selectleft
	// ie., operators "selectMaximals", "selectMinimals", "sortedBy", "select", "reject" 
	
    Expression selectleft = left; 

    if (operator.equals("|") || operator.equals("|R") ||
        operator.equals("|sortedBy") ||
        "|selectMinimals".equals(operator) ||  
        "|selectMaximals".equals(operator))
    { selectleft = ((BinaryExpression) left).right; 
      // tleft = selectleft.getType(); 
    }
    
    // if (selectleft.isMultiple())
    if (Type.isSequenceType(tleft))
    { type = new Type("Sequence",null); } 
    else if (operator.equals("->sortedBy") || operator.equals("|sortedBy"))
    { type = new Type("Sequence",null); } 
    else if (Type.isMapType(tleft))
    { type = new Type("Map", null); }
    else if (Type.isSetType(tleft)) 
    { type = new Type("Set",null); } 
    else 
    { System.err.println("TYPE ERROR!!: LHS of select/reject must be a collection! " + this); 
      // JOptionPane.showMessageDialog(null, "LHS of " + this + " must be a collection", 
      //                                    "Type error", JOptionPane.ERROR_MESSAGE);
      type = new Type("Set",null); 
      // return; 
    } 

    Type etleft = selectleft.getElementType(); 
    if (etleft != null) 
    { elementType = etleft; } 
    else if ((selectleft + "").equals(eleft + ""))
    { elementType = new Type(eleft); } 
    type.setElementType(elementType); 
    entity = eleft; // ? 
    // System.out.println("Element type of: " + this + " is " + elementType); 

    if (isSorted())
    { type.setSorted(true); } 
  } 

  private void tcCollect(Type tleft, Type tright, Entity eright)
  { // left should be set or sequence, right an expression

    Expression collectleft = left; 

    if (operator.equals("|C") || 
        "|unionAll".equals(operator) || "|intersectAll".equals(operator) ||
        "|concatenateAll".equals(operator))
    { collectleft = ((BinaryExpression) left).right; }
    
    if (collectleft.isMultiple())
    { } 
    else 
    { System.err.println("!!! TYPE ERROR: LHS of collect must be a collection! " + this); 
      JOptionPane.showMessageDialog(null, "LHS must be a collection: " + this, 
                                          "Type error", JOptionPane.ERROR_MESSAGE);
      // type = null; 
      // return; 
    } 

    if (tright == null) 
    { System.err.println("!!! TYPE ERROR: No type for collect RHS: " + this); 
      JOptionPane.showMessageDialog(null, "ERROR: No type for collect RHS: " + this,                                          
	                                "Type error", JOptionPane.ERROR_MESSAGE);
      return; 
    } 
    

    if (operator.equals("->collect") || operator.equals("|C"))
    { elementType = (Type) tright.clone();
      type = new Type("Sequence",null);
	  if (Type.isMapType(tleft))
	  { type = new Type("Map", null); }   // map->collect(e) is the composed map. 
    } 
    else // ->intersectAll, ->unionAll, ->concatenateAll and | versions of these
    { if (right.elementType == null) 
      { elementType = null; } 
      else 
      { elementType = (Type) right.elementType.clone(); } 

      if (operator.equals("->unionAll") && tleft.getName().equals("Sequence") &&
          (right.isOrdered() || "self".equals("" + right))) // dcat in B
      { type = new Type("Sequence",null); 
        if ("self".equals("" + right))
        { elementType = left.getElementType(); 
          type.setElementType(left.getElementType()); 
        }
      }  
      else if (operator.equals("|concatenateAll") || operator.equals("->concatenateAll"))
	  { type = new Type("Sequence", null); } 
	  else 
      { type = new Type("Set",null); }     // intersectAll always unordered
      // entity = eright; 
    } // ?? 

    if (elementType != null && elementType.isEntity())
    { entity = elementType.getEntity(); } 

    type.setElementType(elementType); 

    // System.out.println("++++ Type/Element type of: " + this + " is " + type + " " + elementType); 
  
    if (isSorted())
    { type.setSorted(true); } 
  } 

  private void tcMathOps(Type tleft, Type tright,
                         Entity eleft, Entity eright)
  { // *, mod and divide -- not allowed to do Set*number. mod not valid for doubles. 
    
    if (tleft != null && tright != null)
    { String tlname = tleft.getName();
      String trname = tright.getName();
      if (Type.isNumericType(tlname) && Type.isNumericType(trname))
      { } 
      else 
      { System.err.println("TYPE ERROR: arguments must be numeric in: " + this); 
        JOptionPane.showMessageDialog(null, "Arguments not numeric in: " + this, 
                                            "Type error", JOptionPane.ERROR_MESSAGE);
      } 

      if (trname.equals("double") || tlname.equals("double")) 
      { type = new Type("double",null); }
      else if (tlname.equals("long") || trname.equals("long"))
      { type = new Type("long",null); }
      else if (trname.equals("int") && trname.equals("int"))
      { type = new Type("int",null); }
      else 
      { System.err.println("TYPE ERROR: invalid types " + tlname + " " + trname + " in: " + this);
        type = new Type("int", null);
      }
    } 
    else 
    { System.err.println("TYPE ERROR: invalid types " + tleft + " " + tright + " in: " + this);
      JOptionPane.showMessageDialog(null, "Missing types in: " + this, 
                                          "Type error", JOptionPane.ERROR_MESSAGE);
      if (tleft != null)
      { right.setType(tleft);
        type = tleft;
      }
      else if (tright != null)
      { left.setType(tright);
        type = tright;
      }
      else 
      { type = null; }
    }
    elementType = type; 
  }


  private void tcLogical(Type tleft, Type tright,
                         Entity eleft, Entity eright)
  { // must both be boolean. Events are boolean
    if (tleft != null && tright != null)
    { String tlname = tleft.getName();
      String trname = tright.getName();
      if (tlname.equals("boolean") && trname.equals("boolean"))
      { type = new Type("boolean",null); }
      else
      { System.err.println("TYPE ERROR: invalid types in: " + this);
        JOptionPane.showMessageDialog(null, "Arguments must be booleans: " + this, 
                                            "Type error", JOptionPane.ERROR_MESSAGE);
      }
    } 
    else 
    { System.err.println("TYPE ERROR: invalid types in: " + this);
      JOptionPane.showMessageDialog(null, "Missing types in: " + this, 
                                          "Type error", JOptionPane.ERROR_MESSAGE);
    }
    type = new Type("boolean",null); 
    elementType = type; 
  }  

/*
  private void tcInOps(Type tleft, Type tright,
                       Entity eleft, Entity eright)
*/

  public boolean conflictsWith(Expression e)
  { if (e instanceof UnaryExpression)
    { UnaryExpression ue = (UnaryExpression) e; 
      if ("->isDeleted".equals(ue.operator) && operator.equals(":"))
      { if ((ue.argument + "").equals(left + ""))   // l : e & l->isDeleted()
        { return true; } 
      if ((ue.argument + "").equals(right + ""))  // l : e & e->isDeleted()
      { return true; } 
    } 
    if ("->isDeleted".equals(ue.operator) && operator.equals("->includes"))
    { if ((ue.argument + "").equals(right + ""))   // e->includes(l) & l->isDeleted()
      { return true; } 
      if ((ue.argument + "").equals(left + ""))  // e->includes(r) & e->isDeleted()
      { return true; } 
    } 
    if ("->isEmpty".equals(ue.operator) && operator.equals(":"))
    { if ((ue.argument + "").equals(right + ""))  // l : e & e->isEmpty()
      { return true; } 
    } 
    if ("->isEmpty".equals(ue.operator) && operator.equals("->includes"))
    { if ((ue.argument + "").equals(left + ""))  // e->includes(l) & e->isEmpty()
      { return true; } 
    } 
    return false; 
  } /* These cases also belong in UnaryExpression */ 

  if (e instanceof BinaryExpression)
  { BinaryExpression be = (BinaryExpression) e;
    if (be.operator.equals("&"))
    { return conflictsWith(be.left) ||
             conflictsWith(be.right);
    }
    if (be.operator.equals("or"))
    { return conflictsWith(be.left) &&
             conflictsWith(be.right);
    }
    if (be.operator.equals("=>"))
    { boolean leftconflict = conflictsWith(be.left); 
      return !leftconflict && conflictsWith(be.right); 
    } 
    if (be.operator.equals("#") || be.operator.equals("#1") || be.operator.equals("#LC") ||
        "->existsLC".equals(operator) || be.operator.equals("->exists"))
    { return conflictsWith(be.right); }  // provided be.left var not in this

    if (operator.equals("->exists") && be.operator.equals("->forAll") && 
        (left + "").equals(be.left + ""))
    { return right.conflictsWith(be.right); } 
    if (operator.equals("->exists1") && be.operator.equals("->forAll") && 
        (left + "").equals(be.left + ""))
    { return right.conflictsWith(be.right); }
    if (operator.equals("->forAll") && be.operator.equals("->exists") && 
        (left + "").equals(be.left + ""))
    { return right.conflictsWith(be.right); } 
    if (operator.equals("->forAll") && be.operator.equals("->exists1") && 
        (left + "").equals(be.left + ""))
    { return right.conflictsWith(be.right); } 
    // likewise for #, #1, !
    if (operator.equals("->forAll") && be.operator.equals("->forAll") && 
        (left + "").equals(be.left + ""))
    { return right.conflictsWith(be.right); } 
    else 
    { return conflictsWith(be.operator,
                           be.left,be.right);
    }
  }
  return false;
}

public boolean conflictsWith(String op, Expression el,
                             Expression er)
{ // System.out.println("CONFLICT: " + this + "   " + el + " " + op + " " + er); 

  if (left.toString().equals(el.toString()) &&
      right.toString().equals(er.toString()))
  { boolean res = conflictsOp(operator,op); 
    // System.out.println("CONFLCT: " + res); 
    return res; 
  }
  if (left.toString().equals(er.toString()) &&
      right.toString().equals(el.toString()))
  { return conflictsReverseOp(operator,op); }
  if (operator.equals("="))
  { return conflictsWithEq(op,el,er); }
  if (operator.equals(":"))
  { return conflictsWithIn(op,el,er); }
//  if (comparitors.contains(operator))
//  { return conflictsWithComp(op,el,er); }
  if (operator.equals("&"))  // shouldn't occur
  { return left.conflictsWith(op,el,er) ||
           right.conflictsWith(op,el,er);
  }
  if (operator.equals("or"))
  { return left.conflictsWith(op,el,er) &&
           right.conflictsWith(op,el,er);
  }
  return false;
}

public boolean conflictsWithEq(String op, Expression el,
                               Expression er)
{ /* if (op.equals("="))
  { if (left.toString().equals(el.toString()) &&
        right.getKind() == VALUE && 
        er.getKind() == VALUE &&
        !right.toString().equals(er.toString()))
    { return true; }
  }
  else 
  */ 
  if (op.equals(":"))
  { if ((right + "").equals("Set{}") && (left + "").equals(er + "")) 
    { return true; }  // left = Set{} & x : left
    if ((right + "").equals("Set{}") && ("self." + left).equals(er + "")) 
    { return true; } // left = Set{} & x : self.left
    if ((right + "").equals("Sequence{}") && (left + "").equals(er + "")) 
    { return true; } 
    if ((right + "").equals("Sequence{}") && ("self." + left).equals(er + "")) 
    { return true; }
    if ((right + "").equals("0") && (left + "").equals(er + ".size")) 
    { return true; } // er.size = 0 & x : er 
    if ((right + "").equals("0") && ("self." + left).equals(er + ".size")) 
    { return true; }
    if ((right + "").equals("0") && (left + "").equals(er + "->size()")) 
    { return true; } 
    if ((right + "").equals("0") && ("self." + left).equals(er + "->size()")) 
    { return true; }
  } // and other cases of  p = Set{} & x : p  
  
  return false;
}

public boolean conflictsWithIn(String op, Expression el,
                               Expression er)
{ if (op.equals("="))
  { if ((right + "").equals(el + "") && ("Set{}").equals(er + "")) 
    { return true; }  // x = Set{} & er : x 
    if ((right + "").equals(el + "") && ("Sequence{}").equals(er + "")) 
    { return true; } 
    if ((right + ".size").equals(el + "") && ("0").equals(er + "")) 
    { return true; }  //  left : right & right.size = 0
    if ((right + "->size()").equals(el + "") && ("0").equals(er + "")) 
    { return true; }
  } // and other cases of  x : p & p = Set{}   
  
  return false;
}

  public Expression skolemize(Expression sourceVar, java.util.Map env)
  { Expression res;

    if (operator.equals("#") || operator.equals("#LC") || 
        operator.equals("#1"))  // and right.right.umlkind == CLASSID
    { Expression var = ((BinaryExpression) left).left; 
      Expression targetType = ((BinaryExpression) left).right;
      Expression sourceType = new BasicExpression(sourceVar.type + "");  
      // replace var in right by func(sourceVar), record func: sourceType -> targetType
      String func = Identifier.nextIdentifier("sk_"); 
      UnaryExpression fapp = new UnaryExpression(func,sourceVar); 
      Expression newr = right.substituteEq(var + "", fapp); 
      env.put(func,new BinaryExpression("->",sourceType,targetType)); 
      return newr.skolemize(sourceVar,env); 
    }

    Expression nleft = left.skolemize(sourceVar,env); 
    Expression nrigh = right.skolemize(sourceVar,env); 
    res = new BinaryExpression(operator,nleft,nrigh); 
    res.setBrackets(needsBracket); 
    return res; 
  } 

  public String cstlQueryForm(java.util.Map srcvarMap)
  { String lqf = left.cstlQueryForm(srcvarMap); 
    String rqf = right.cstlQueryForm(srcvarMap); 
    if ("+".equals(operator) && isString())
    { return lqf + "" + rqf; }
    if ("->union".equals(operator))
    { return lqf + "" + rqf; }
    if ("^".equals(operator))
    { return lqf + "" + rqf; }

    return this + ""; 
  }  
    
  public String cstlConditionForm(java.util.Map srcvarmap)
  { String lqf = left.cstlConditionForm(srcvarmap); 
    String rqf = right.cstlConditionForm(srcvarmap); 
    if ("&".equals(operator))
    { return lqf + ", " + rqf; } 
    // for "or", copy the rule 
    if ("=".equals(operator))
    { return lqf + " " + rqf; } 
    if ("/=".equals(operator))
    { return lqf + " not " + rqf; }
    if ("->hasStereotype".equals(operator))
    { return lqf + " " + rqf; }
    return this + ""; 
  } 

  public String negatedcstlConditionForm(java.util.Map srcvarmap)
  { String lqf = left.cstlConditionForm(srcvarmap); 
    String rqf = right.cstlConditionForm(srcvarmap); 
    // if ("&".equals(operator))
    // { return lqf + ", " + rqf; } 
    // for "or", copy the rule 
    if ("=".equals(operator))
    { return lqf + " not " + rqf; } 
    if ("/=".equals(operator))
    { return lqf + " " + rqf; }
    if ("->hasStereotype".equals(operator))
    { return lqf + " not " + rqf; }
    return "not " + this + ""; 
  } 

  public String queryForm(java.util.Map env, boolean local)
  { String res;
    boolean bNeeded = needsBracket; 

    String cont = "Controller.inst()"; 

    if (operator.equals("#") || "->existsLC".equals(operator) || 
        operator.equals("#LC") || operator.equals("->exists"))
    { return existsQueryForm(env,local); } 

    if (operator.equals("#1") || operator.equals("->exists1"))
    { return exists1QueryForm(env,local); } 

    if (operator.equals("->forAll") || operator.equals("!"))
    { return forAllQueryForm(env,local); } 
	
    if (operator.equals("let"))
    { String acc = accumulator.getName(); 
      Expression sbst = right.substituteEq(acc,left); 
      return sbst.queryForm(env,local); 
    } // Or, extend env by acc |-> left

    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();
    boolean lprim = left.isPrimitive();
    boolean rprim = right.isPrimitive();
    String lqf = left.classqueryForm(env,local);
    String rqf = right.queryForm(env,local);
    String rw = rqf; 
    if (rprim) 
    { rw = right.wrap(rqf); }
 
    // System.out.println(left + " is primitive: " + lprim + " is multiple: " + lmult); 
    // System.out.println(right + " is primitive: " + rprim + " is multiple: " + rmult); 


    String typ = ""; 
    if (type != null) 
    { typ = type.getJava(); } 
    else 
    { System.err.println("!! Warning: no type for " + this); }
	 
    String javaOp = javaOpOf(operator);
    res = lqf + " " + javaOp + " " + rqf; // default
    // if & or or: &&, ||

    if (operator.equals("|") || operator.equals("->select"))
    { return selectQueryForm(lqf,rqf,env,local); } 

    if (operator.equals("|C") || operator.equals("->collect"))   
    { return collectQueryForm(lqf,rqf,rprim,env,local); } 

    if (operator.equals("|A") || operator.equals("->any"))   
    { String getany = anyQueryForm(lqf,rqf,rprim,env,local); 
      if (Type.isPrimitiveType(type))
      { return unwrap(getany); } 
      return "((" + typ + ") " + getany + ")"; 
    } 

    if (operator.equals("->selectMaximals") || operator.equals("|selectMaximals"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      if (operator.equals("->selectMaximals") && left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryForm(env,local); }   // And pass lqf to collectQueryForm? 
      else if (operator.equals("|selectMaximals") && ((BinaryExpression) left).right.umlkind == CLASSID)
      { BinaryExpression leftbe = (BinaryExpression) left; 
	    lqf = ((BasicExpression) leftbe.right).classExtentQueryForm(env,local); 
	  }   // And pass lqf to collectQueryForm? 
      return "Set.maximalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->selectMinimals") || operator.equals("|selectMinimals"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      if (operator.equals("->selectMinimals") && left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryForm(env,local); }   // And pass lqf to collectQueryForm? 
      else if (operator.equals("|selectMinimals") && ((BinaryExpression) left).right.umlkind == CLASSID)
      { BinaryExpression leftbe = (BinaryExpression) left; 
	    lqf = ((BasicExpression) leftbe.right).classExtentQueryForm(env,local); 
	  }
	  return "Set.minimalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->sortedBy") || operator.equals("|sortedBy"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      if (operator.equals("->sortedBy") && left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryForm(env,local); }   // And pass lqf to collectQueryForm? 
      else if (operator.equals("|sortedBy") && ((BinaryExpression) left).right.umlkind == CLASSID)
      { BinaryExpression leftbe = (BinaryExpression) left; 
        lqf = ((BasicExpression) leftbe.right).classExtentQueryForm(env,local); 
      }      
      return "Set.sortedBy(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->count"))
    { return "Set.count(" + lqf + "," + rw + ")"; } 

    if (operator.equals("->restrict"))
    { return "Set.restrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->antirestrict"))
    { return "Set.antirestrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->includesKey"))
    { return "Set.includesKey(" + lqf + "," + rw + ")"; } 

    if (operator.equals("->excludesKey"))
    { return "Set.excludesKey(" + lqf + "," + rw + ")"; } 

    if (operator.equals("->apply"))
    { return "(" + lqf + ").apply(" + rqf + ")"; } 

    if (operator.equals("->at"))
    { if (left.type != null && "String".equals(left.type.getName()))
      { return "(" + lqf + ".charAt(" + rqf + " - 1) + \"\")"; }  // and for Java6, 7, etc. 

      if (left.type == null) 
      { if (right.type != null && 
            "String".equals(right.type.getName()))
        { lqf = "((Map) " + lqf + ")"; 
          left.type = new Type("Map", null);
          left.type.elementType = type;  
        } 
        else 
        { lqf = "((List) " + lqf + ")"; 
          left.type = new Type("Sequence", null);
          left.type.elementType = type;  
        } 
      } 

      String ind = lqf + ".get(" + rqf + ")"; 
      String getind = lqf + ".get(" + rqf + " - 1)";
	   
      if (left.type != null && left.type.isMapType())
      { return "((" + typ + ") " + ind + ")"; }
      
      if ("String".equals(right.type + ""))  // a map lookup
      { getind = lqf + ".get(" + rqf + ")"; } 

      if (type == null) 
      { if (left.elementType == null) 
        { return getind; } 
        else 
        { type = left.elementType; 
          typ = type.getJava(); 
        } 
      } 

      if (Type.isPrimitiveType(type))
      { return unwrap(getind); } 
      return "((" + typ + ") " + getind + ")"; 
    } 

    if (operator.equals("->pow"))
    { return "Math.pow(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->gcd"))
    { return "Set.gcd(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->compareTo")) 
    { if (left.isNumeric() && right.isNumeric())
      { res = "(" + lqf + " < " + rqf + ")?-1:((" + lqf + " > " + rqf + ")?1:0)"; } 
      else 
      { res = lqf + ".compareTo(" + rqf + ")"; }  
      return res; 
    } 

    if (operator.equals("->isUnique") || operator.equals("|isUnique"))  // and define for B and for Maps
    { String fcollect = collectQueryForm(lqf,rqf,rprim,env,local);
      return "Set.isUnique(" + fcollect + ")"; 
    } 

    if (operator.equals("->closure"))
    { String rel = right + ""; 

      // System.err.println(">>>> Entity= " + entity + " left= " + left + 
      //                    " LEFTTYPE= " + left.type + 
      //                    " " + left.elementType); 

      if (entity != null) { } 
      else if (left.elementType != null && left.elementType.isEntity()) 
      { entity = left.elementType.getEntity(); } 
      else if (left.entity != null) 
      { entity = left.entity; } // the owner of the relation being closured
      else if (left instanceof SetExpression)
      { entity = ((SetExpression) left).findEntity(); 
        type = new Type("Set", null);
        typ = type.getJava();  
      } 

      if (entity == null) 
      { System.err.println("!! No entity for " + this); 
        JOptionPane.showMessageDialog(null, "No entity for: " + this, 
                                      "Semantic error", JOptionPane.ERROR_MESSAGE);
        return ""; 
      }  

      if (left.isMultiple()) {} 
      else 
      { System.err.println("ERROR: LHS of " + this + " must be collection");                             
        JOptionPane.showMessageDialog(null, "ERROR: LHS of " + this + " must be collection",                               
		                              "Type error", JOptionPane.ERROR_MESSAGE);
      }

      if (entity != null)       
      { Association ast = entity.getDefinedRole(right + ""); 
        if (ast != null)
        { Entity entc = ast.closureEntity(); 
          if (entc != null) 
          { elementType = new Type(entc); }  
          if (type != null) 
          { type.setElementType(elementType); }
 
          if (ast.isClosurable()) { } 
          else 
          { JOptionPane.showMessageDialog(null, "ERROR: association in " + this + " cannot be closured",                               
		                                  "Expression error", JOptionPane.ERROR_MESSAGE);
          }
          Entity ent1 = ast.getEntity1(); 
          return "Set.closure" + ent1.getName() + rel + "(" + lqf + ")";  
        } 
        return "Set.closure" + entity.getName() + rel + "(" + lqf + ")"; 
      } 
    } // left must be set-valued. 

    if (operator.equals("->intersectAll") || operator.equals("|intersectAll"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      return "Set.intersectAll(" + col + ")"; 
    } 

    if (operator.equals("->unionAll") || operator.equals("|unionAll"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      if (left.isOrdered() && (right.isOrdered() || "self".equals(right + "")))
      { return "Set.concatenateAll(" + col + ")"; } 
      return "Set.unionAll(" + col + ")"; 
    } 

    if (operator.equals("|concatenateAll"))
    { String col = collectQueryForm(lqf,rqf,rprim,env,local); 
      return "Set.concatenateAll(" + col + ")";
    } 

    if (operator.equals("->reject") || operator.equals("|R"))
    { String sel = rejectQueryForm(lqf,rqf,env,local);
      return sel; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals(":"))  
    { return "(" + lqf + " instanceof " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsKindOf"))  
    { return "(" + lqf + " instanceof " + right + ")"; } 

    // ->oclIsTypeOf (in the exact class)
	
    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsTypeOf"))  
    { return "(" + lqf + ".getClass() == " + right + ".class)"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("<:"))  
    { rqf = ((BasicExpression) right).classExtentQueryForm(env,local);
      return "(" + rqf + ".containsAll(" + lqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includes"))  
    { return "(" + rqf + " instanceof " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryForm(env,local);
      return "(" + lqf + ".containsAll(" + rqf + "))"; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("/:")) 
    { return "!(" + lqf + " instanceof " + right + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludes")) 
    { return "!(" + rqf + " instanceof " + left + ")"; } 


    if (operator.equals("=>"))
    { return "(!(" + lqf + ") || " + rqf + ")"; } 

    if (operator.equals("xor"))
    { return "((" + lqf + " && !(" + rqf + ")) || (!(" + lqf + ") && " + rqf + "))"; } 

    if (operator.equals("->oclAsType"))
    { // Type typ = Type.getTypeFor("" + right,types,entities); 
      if (type != null) 
      { String jtyp = type.getJava(); 
        return "((" + jtyp + ") " + lqf + ")"; 
      }
      return "((" + right + ") " + lqf + ")"; 
    }  

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (!lmult && !rmult)
    { if (operator.equals("=")) // and comparitors
      { res = composeEqQueryForms(lqf,rqf,lprim,rprim); }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = composeNeqQueryForms(lqf,rqf,lprim,rprim); }
      else if (comparitors.contains(operator))
      { res = composeComparitorQueryForms(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/") || 
               operator.equals("mod") || operator.equals("div"))
      { res = composeMathOpQueryForms(lqf,rqf,lprim,rprim);
        bNeeded = needsBracket;
      } 
      else if (operator.equals("->indexOf"))
      { res = lqf + ".indexOf(" + rqf + ") + 1"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->lastIndexOf"))
      { res = lqf + ".lastIndexOf(" + rqf + ") + 1"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->count"))  // for strings
      { res = "Set.count(" + lqf + "," + rw + ")"; 
        bNeeded = false; 
      } 
      else if (operator.equals("->pow"))
      { res = "Math.pow(" + lqf + ", " + rqf + ")"; 
        bNeeded = false; 
      } 
      else if (operator.equals("->hasPrefix"))
      { res = lqf + ".startsWith(" + rqf + ")"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->hasSuffix"))
      { res = lqf + ".endsWith(" + rqf + ")"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->hasMatch"))
      { res = "Set.hasMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->isMatch"))
      { res = lqf + ".matches(" + rqf + ")"; } 
      else if (operator.equals("->equalsIgnoreCase"))
      { res = lqf + ".equalsIgnoreCase(" + rqf + ")"; } 
      else if (operator.equals("->before"))
      { res = "Set.before(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->after"))
      { res = "Set.after(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->split"))
      { res = "Set.split(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->allMatches"))
      { res = "Set.allMatches(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->firstMatch"))
      { res = "Set.firstMatch(" + lqf + ", " + rqf + ")"; } 
    }
    else if (lmult && !rmult) // convert right to mult
    { String rss = right.makeSet(rw);
      bNeeded = false; 
   
      if (operator.equals("->includes"))
      { res = lqf + ".contains(" + rw + ")"; }
      else if (operator.equals("->excludes"))
      { res = "!(" + lqf + ".contains(" + rw + "))"; }
      else if (operator.equals("->excluding"))
      { res = "Set.subtract(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->excludingAt"))
      { res = "Set.removeAt(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->excludingFirst"))
      { res = "Set.removeFirst(" + lqf + "," + rw + ")"; }
      else if (operator.equals("->append"))
      { res = "Set.concatenate(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->including") && left.isSetValued())
      { res = "Set.union(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->including") && left.isSequenceValued())
      { res = "Set.concatenate(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->prepend"))
      { 
        res = "Set.concatenate(" + rss + "," + lqf + ")"; 
      }
      else if (operator.equals("="))
      { if (Type.isSetType(left.getType()))
        { res = "Set.equals(" + lqf + "," + rss + ")"; } 
        else 
        { res = lqf + ".equals(" + rss + ")"; }
      } 
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { if (Type.isSetType(left.getType()))
        { res = "!(Set.equals(" + lqf + "," + rss + "))"; } 
        else 
        { res = "!(" + lqf + ".equals(" + rss + "))"; }
      } 
      else if (operator.equals(":") || operator.equals("<:"))
      { res = rss + ".containsAll(" + lqf + ")"; }
      else if (operator.equals("->includesAll"))
      { res = lqf + ".containsAll(" + rss + ")"; } 
      else if (operator.equals("/<:"))
      { res = "!(" + rss + ".containsAll(" + lqf + ")"; } 
      else if (operator.equals("->excludesAll"))
      { res = "Set.intersection(" + rss + "," + lqf + ").size() == 0"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->count"))
      { res = "Set.count(" + lqf + "," + rw + ")"; } 
      else if (operator.equals("->indexOf"))
      { res = lqf + ".indexOf(" + rw + ") + 1"; 
        bNeeded = true; 
      } 
      else if (operator.equals("->lastIndexOf"))
      { res = lqf + ".lastIndexOf(" + rw + ") + 1"; 
        bNeeded = true; 
      } 
      else   
      { // res = lqf + " " + operator + " " + rqf;
        res = composeSetQueryForms(lqf,rqf); 
        bNeeded = true; 
      } // composeSetQueryForms(lqf,rs,rqf); }  
    }
    else if (rmult && !lmult)
    { String lw = ""; 
      bNeeded = false; 

      if (lprim) 
      { lw = left.wrap(lqf); } // only if left primitive
      else 
      { lw = lqf; } 
      String ls = left.makeSet(lw);
      
      if (operator.equals("="))
      { if (Type.isSetType(right.getType()))
        { res = "Set.equals(" + ls + "," + rqf + ")"; } 
        else 
        { res = ls + ".equals(" + rqf + ")"; } 
      }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { if (Type.isSetType(right.getType()))
        { res = "!(Set.equals(" + ls + "," + rqf + "))"; } 
        else 
        { res = "!(" + ls + ".equals(" + rqf + "))"; } 
      }
      else if (operator.equals(":") || operator.equals("<:"))
      { res = rqf + ".contains(" + lw + ")"; }
      else if (operator.equals("/:"))
      { res = "!(" + rqf + ".contains(" + lw + "))"; }
      else if (operator.equals("->excludesAll"))
      { res = "Set.intersection(" + rqf + "," + ls + ").size() == 0";
        bNeeded = true; 
      } 
      else 
      { res = lqf + " " + operator + " " + rqf; 
        bNeeded = true; 
      } // composeSetQueryForms(ls,rqf); }
    }
    else // both sets
    if (operator.equals("->indexOf"))
    { res = "Collections.indexOfSubList(" + lqf + "," + rqf + ")+1"; 
      bNeeded = true; 
    } 
    else if (operator.equals("->lastIndexOf"))
    { res = "Collections.lastIndexOfSubList(" + lqf + "," + rqf + ") + 1"; 
      bNeeded = true; 
    } 
    else   
    { // res = lqf + " " + operator + " " + rqf;
      res = composeSetQueryForms(lqf,rqf); 
      bNeeded = true; 
    }
    // { res = composeSetQueryForms(lqf,rqf); }

    if (bNeeded)
    { return "( " + res + " )"; }
    return res;
  } // add brackets if needed

  public String queryFormJava6(java.util.Map env, boolean local)
  { String res;
    String cont = "Controller.inst()"; 

    if (operator.equals("#") || operator.equals("#LC") ||
        "->existsLC".equals(operator) || operator.equals("->exists"))
    { return existsQueryFormJava6(env,local); } 

    if (operator.equals("#1") || operator.equals("->exists1"))
    { return exists1QueryFormJava6(env,local); } 

    if (operator.equals("->forAll") || operator.equals("!"))
    { return forAllQueryFormJava6(env,local); } 

    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();
    boolean lprim = left.isPrimitive();
    boolean rprim = right.isPrimitive();
    String lqf = left.queryFormJava6(env,local);
    String rqf = right.queryFormJava6(env,local);
    String rw = rqf; 
    if (rprim) 
    { rw = right.wrap(rqf); } 

    String javaOp = javaOpOf(operator);

    String typ = ""; 
	if (type != null) 
	{ typ = type.getJava6(); } 
	else 
	{ System.err.println("!! Warning: no type for " + this); }
 
    res = lqf + " " + javaOp + " " + rqf; // default
    // if & or or: &&, ||

    if (operator.equals("|") || operator.equals("->select"))
    { return selectQueryFormJava6(lqf,rqf,env,local); } 

    if (operator.equals("|C") || operator.equals("->collect"))  
    { return collectQueryFormJava6(lqf,rqf,rprim,env,local); } 

    if (operator.equals("|A") || operator.equals("->any"))   
    { String getany = anyQueryFormJava6(lqf,rqf,rprim,env,local); 
      if (Type.isPrimitiveType(type))
      { return unwrap(getany); } 
       
      return "((" + typ + ") " + getany + ")"; 
    } 

    if (operator.equals("->selectMaximals"))
    { String col = collectQueryFormJava6(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava6(env,local); }   
      // And pass lqf to collectQueryForm? 
      return "Set.maximalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->selectMinimals"))
    { String col = collectQueryFormJava6(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava6(env,local); } 
      return "Set.minimalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->sortedBy")) // left must be ordered
    { String col = collectQueryFormJava6(lqf,rqf,rprim,env,local); // ordered version 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava6(env,local); } 
      return "Set.sortedBy(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->count"))  // not for strings
    { return "Collections.frequency(" + lqf + "," + rw + ")"; } 

    if (operator.equals("->restrict"))
    { return "Set.restrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->antirestrict"))
    { return "Set.antirestrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->at") && type != null)
    { String getind = lqf + ".get(" + rqf + " - 1)"; 
      String ind = lqf + ".get(" + rqf + ")"; 
      
      if ("String".equals(left.type + ""))
      { return "(" + lqf + ".charAt(" + rqf + " - 1) + \"\")"; }  // and for Java6, 7, etc. 
      else if (left.type != null && left.type.isMapType())
      { return "((" + typ + ") " + ind + ")"; }
      else if (Type.isPrimitiveType(type))
      { return unwrap(getind); } 
      return "((" + typ + ") " + getind + ")"; 
    } 

    if (operator.equals("->apply"))
    { return "(" + lqf + ").apply(" + rqf + ")"; } 

    if (operator.equals("->pow"))
    { return "Math.pow(" + lqf + ", " + rqf + ")"; } 

    if (operator.equals("->compareTo")) 
    { if (left.isNumeric() && right.isNumeric())
      { res = "(" + lqf + " < " + rqf + ")?-1:((" + lqf + " > " + rqf + ")?1:0)"; } 
      else 
      { res = lqf + ".compareTo(" + rqf + ")"; }  
      return res; 
    } 

    if (operator.equals("->gcd"))
    { return "Set.gcd(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->isUnique"))  // and define for B
    { String fcollect = collectQueryFormJava6(lqf,rqf,rprim,env,local);
      return "Set.isUnique(" + fcollect + ")"; 
    } 

    if (operator.equals("->intersectAll"))
    { String col = collectQueryFormJava6(lqf,rqf,rprim,env,local); 
      return "Set.intersectAll(" + col + ")"; 
    } 

    if (operator.equals("->unionAll"))
    { String col = collectQueryFormJava6(lqf,rqf,rprim,env,local); 
      if (left.isOrdered() && (right.isOrdered() || "self".equals(right + "")))
      { return "Set.concatenateAll(" + col + ")"; } 
      return "Set.unionAll(" + col + ")"; 
    } 

    if (operator.equals("->closure"))
    { String rel = right + ""; 
      if (entity == null) 
      { JOptionPane.showMessageDialog(null, "No entity for: " + this, 
                                      "Semantic error", JOptionPane.ERROR_MESSAGE);
        return ""; 
      }  
      return "Set.closure" + entity.getName() + rel + "(" + lqf + ")"; 
    } // left must be set-valued. 

    if (operator.equals("->reject") || operator.equals("|R"))
    { String sel = rejectQueryFormJava6(lqf,rqf,env,local);
      return sel; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals(":"))  
    { return "(" + lqf + " instanceof " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsKindOf"))  
    { return "(" + lqf + " instanceof " + right + ")"; }  // Java6 version of type named by right

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsTypeOf"))  
    { return "(" + lqf + ".getClass() == " + right + ".class)"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("/:")) 
    { return "!(" + lqf + " instanceof " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("<:"))  
    { rqf = ((BasicExpression) right).classExtentQueryFormJava6(env,local);
      return "(" + rqf + ".containsAll(" + lqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includes"))  
    { return "(" + rqf + " instanceof " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormJava6(env,local);
      return "(" + lqf + ".containsAll(" + rqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludes")) 
    { return "!(" + rqf + " instanceof " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormJava6(env,local);
      return "Collections.disjoint(" + lqf + "," + rqf + ")"; 
    } 

    if (operator.equals("=>"))
    { return "(!(" + lqf + ") || " + rqf + ")"; } 

    if (operator.equals("->oclAsType"))
    { if (type != null) 
      { String jtyp = type.getJava6(); 
        return "((" + jtyp + ") " + lqf + ")"; 
      }
      return lqf; 
    } 

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (!lmult && !rmult)
    { if (operator.equals("=")) // and comparitors
      { res = composeEqQueryForms(lqf,rqf,lprim,rprim); }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = composeNeqQueryForms(lqf,rqf,lprim,rprim); }
      else if (comparitors.contains(operator))
      { res = composeComparitorQueryForms(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/") || 
               operator.equals("mod") || operator.equals("div"))
      { res = composeMathOpQueryForms(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("->count"))
      { res = "Set.count(" + lqf + "," + rw + ")"; } 
      else if (operator.equals("->indexOf"))
      { res = "(" + lqf + ".indexOf(" + rw + ") + 1)"; }
      else if (operator.equals("->lastIndexOf"))
      { res = "(" + lqf + ".lastIndexOf(" + rw + ") + 1)"; } 
      else if (operator.equals("->hasPrefix"))
      { res = "(" + lqf + ".startsWith(" + rqf + "))"; } 
      else if (operator.equals("->hasSuffix"))
      { res = "(" + lqf + ".endsWith(" + rqf + "))"; }
      else if (operator.equals("->hasMatch"))
      { res = "Set.hasMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->isMatch"))
      { res = lqf + ".matches(" + rqf + ")"; } 
      else if (operator.equals("->equalsIgnoreCase"))
      { res = "(" + lqf + ".equalsIgnoreCase(" + rqf + "))"; }  
      else if (operator.equals("->before"))
      { res = "Set.before(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->after"))
      { res = "Set.after(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->split"))
      { res = "Set.split(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->allMatches"))
      { res = "Set.allMatches(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->firstMatch"))
      { res = "Set.firstMatch(" + lqf + ", " + rqf + ")"; } 
    }
    else if (lmult && !rmult) // convert right to mult
    { String rss = right.makeSetJava6(rw);
      if (left.isOrdered())
      { rss = right.makeSequenceJava6(rw); } 
         
      if (operator.equals("->includes"))
      { res = lqf + ".contains(" + rw + ")"; }
      else if (operator.equals("->excludes"))
      { res = "!(" + lqf + ".contains(" + rw + "))"; }
      else if (operator.equals("->excluding"))
      { res = "Set.subtract(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->excludingAt"))
      { res = "Set.removeAt(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->excludingFirst"))
      { res = "Set.removeFirst(" + lqf + "," + rw + ")"; }
      else if (operator.equals("->append"))
      { res = "Set.concatenate(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->including"))
      { res = "Set.union(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->prepend"))
      { res = "Set.concatenate(" + rss + "," + lqf + ")"; }
      else 
      { String rs = right.makeSetJava6(rqf);
        if (left.isOrdered())
        { rs = right.makeSequenceJava6(rqf); } 
 
        if (operator.equals("="))
        { res = lqf + ".equals(" + rs + ")"; }
        else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
        { res = "!(" + lqf + ".equals(" + rs + "))"; }
        else if (operator.equals(":") || operator.equals("<:"))
        { res = rs + ".containsAll(" + lqf + ")"; }
        else if (operator.equals("->includesAll"))
        { res = lqf + ".containsAll(" + rs + ")"; } 
        else if (operator.equals("/<:"))
        { res = "!(" + rs + ".containsAll(" + lqf + ")"; } 
        else if (operator.equals("->excludesAll"))
        { res = "Collections.disjoint(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("->including"))
        { res = "Set.union(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->excluding"))
        { res = "Set.subtract(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->excludingAt"))
        { res = "Set.removeAt(" + lqf + "," + rqf + ")"; } 
        else if (operator.equals("->excludingFirst"))
        { res = "Set.removeFirst(" + lqf + "," + rw + ")"; } 
        else if (operator.equals("->prepend"))
        { res = "Set.concatenate(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("->append"))
        { res = "Set.concatenate(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->count"))
        { res = "Collections.frequency(" + lqf + "," + rw + ")"; } 
        else if (operator.equals("->indexOf"))
        { res = "(" + lqf + ".indexOf(" + rw + ") + 1)"; } 
        else if (operator.equals("->lastIndexOf"))
        { res = "(" + lqf + ".lastIndexOf(" + rw + ") + 1)"; }
        else if (operator.equals("->hasMatch"))
        { res = "Set.hasMatch(" + lqf + ", " + rqf + ")"; } 
        else if (operator.equals("->isMatch"))
        { res = lqf + ".matches(" + rqf + ")"; } 
        /* else if (operator.equals("->hasPrefix"))
        { res = "(" + lqf + ".startsWith(" + rqf + "))"; } 
        else if (operator.equals("->hasSuffix"))
        { res = "(" + lqf + ".endsWith(" + rqf + "))"; } */ 
        else   
        { res = composeSetQueryFormsJava6(lqf,rs); } 
      } 
    }
    else if (rmult && !lmult)
    { String ls = left.makeSetJava6(lqf);
      if (right.isOrdered())
      { ls = left.makeSequenceJava6(lqf); } 
      String lw = ""; 
      if (lprim) 
      { lw = left.wrap(lqf); } // only if left primitive
      else 
      { lw = lqf; } 

      if (operator.equals("="))
      { res = ls + ".equals(" + rqf + ")"; } 
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = "!(" + ls + ".equals(" + rqf + "))"; } 
      else if (operator.equals(":") || operator.equals("<:"))
      { res = rqf + ".contains(" + lw + ")"; }
      else if (operator.equals("/:"))
      { res = "!(" + rqf + ".contains(" + lw + "))"; }
      else if (operator.equals("->excludesAll"))
      { res = "Collections.disjoint(" + ls + "," + rqf + ")"; } 
      else 
      { res = "(" + lqf + " " + operator + " " + rqf + ")"; 
        // bNeeded = true; 
      } // composeSetQueryForms(ls,rqf); }
      // else 
      // { res = composeSetQueryFormsJava6(ls,rqf); }
    }
    else // both sets
    if (operator.equals("->indexOf"))
    { res = "(Collections.indexOfSubList(" + lqf + "," + rqf + ")+1)"; 
    } 
    else if (operator.equals("->lastIndexOf"))
    { res = "(Collections.lastIndexOfSubList(" + lqf + "," + rqf + ") + 1)";  
    } 
    else 
    { res = composeSetQueryFormsJava6(lqf,rqf); }

    if (needsBracket)
    { return "( " + res + " )"; }
    return res;
  } // add brackets if needed

  public String queryFormJava7(java.util.Map env, boolean local)
  { String res;
    String cont = "Controller.inst()"; 

    if (operator.equals("#") || operator.equals("#LC") ||
        "->existsLC".equals(operator) || operator.equals("->exists"))
    { return quantifierQueryFormJava7(env,local); } 

    if (operator.equals("#1") || operator.equals("->exists1"))
    { return quantifierQueryFormJava7(env,local); } 

    if (operator.equals("->forAll") || operator.equals("!"))
    { return forAllQueryFormJava7(env,local); } 

    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();
    boolean lprim = left.isPrimitive();
    boolean rprim = right.isPrimitive();
    String lqf = left.queryFormJava7(env,local);
    String rqf = right.queryFormJava7(env,local);
    String rw = rqf; 
    if (rprim) 
    { rw = right.wrap(rqf); } 

    String javaOp = javaOpOf(operator);

    String typ = ""; 
    if (type != null) 
    { typ = type.getJava7(elementType); } 
    else 
    { System.err.println("!! Warning: no type for " + this); }
	
    res = lqf + " " + javaOp + " " + rqf; // default
    // if & or or: &&, ||

    if (operator.equals("|") || operator.equals("->select"))
    { return selectQueryFormJava7(lqf,rqf,env,local); } 

    if (operator.equals("|C") || operator.equals("->collect"))  
    { return collectQueryFormJava7(lqf,rqf,rprim,env,local); } 

    if (operator.equals("|A") || operator.equals("->any"))   
    { String getany = anyQueryFormJava7(lqf,rqf,rprim,env,local); 
      if (Type.isPrimitiveType(type))
      { return unwrap(getany); } 
      return "((" + typ + ") " + getany + ")"; 
    } 

    if (operator.equals("->selectMaximals"))
    { String col = collectQueryFormJava7(lqf,rqf,rprim,env,local); 
      String jType = type.getJava7(elementType); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava7(env,local); } 
      return "((" + jType + ") Ocl.maximalElements(" + lqf + ", ((ArrayList<Comparable>) " + col + ")))"; 
    } 

    if (operator.equals("->selectMinimals"))
    { String col = collectQueryFormJava7(lqf,rqf,rprim,env,local); 
      String jType = type.getJava7(elementType); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava7(env,local); } 
      return "((" + jType + ") Ocl.minimalElements(" + lqf + ", ((ArrayList<Comparable>) " + col + ")))"; 
    } 

    if (operator.equals("->sortedBy")) // left must be ordered
    { String col = collectQueryFormJava7(lqf,rqf,rprim,env,local); // ordered version 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormJava7(env,local); } 
      return "Ocl.sortedBy(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->count") && !("String".equals(left.type + "")))  // not for strings
    { return "Collections.frequency(" + lqf + "," + rw + ")"; } 

    if (operator.equals("->restrict"))
    { return "Ocl.restrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->antirestrict"))
    { return "Ocl.antirestrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->at") && type != null)
    { String getind = lqf + ".get(" + rqf + " - 1)"; 
      String ind = lqf + ".get(" + rqf + ")"; 
      
      if ("String".equals(left.type + ""))
      { return "(" + lqf + ".charAt(" + rqf + " - 1) + \"\")"; }  // and for Java6, 7, etc. 
      else if (left.type != null && left.type.isMapType())
      { return "((" + typ + ") " + ind + ")"; }
      else if (Type.isPrimitiveType(type))
      { return unwrap(getind); } 
      return "((" + typ + ") " + getind + ")"; 
    } 

    if (operator.equals("->apply"))
    { return "(" + lqf + ").apply(" + rqf + ")"; } 

    if (operator.equals("->pow"))
    { return "Math.pow(" + lqf + ", " + rqf + ")"; } 

    if (operator.equals("->compareTo")) 
    { if (left.isNumeric() && right.isNumeric())
      { res = "(" + lqf + " < " + rqf + ") ? -1 : ((" + lqf + " > " + rqf + ") ? 1 : 0)"; } 
      else 
      { res = lqf + ".compareTo(" + rqf + ")"; }  
      return res; 
    } 

    if (operator.equals("->gcd"))
    { return "Ocl.gcd(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->isUnique"))  // and define for B
    { String fcollect = collectQueryFormJava7(lqf,rqf,rprim,env,local);
      return "Ocl.isUnique(" + fcollect + ")"; 
    } 

    if (operator.equals("->intersectAll"))
    { String col = collectQueryFormJava7(lqf,rqf,rprim,env,local); 
      return "Ocl.intersectAll(" + col + ")"; 
    } 

    if (operator.equals("->unionAll"))
    { String col = collectQueryFormJava7(lqf,rqf,rprim,env,local); 
      String jtype = type.getJava7(elementType); 
      if (left.isOrdered() && (right.isOrdered() || "self".equals(right + "")))
      { return "((" + jtype + ") Ocl.concatenateAll(" + col + "))"; } 
      return "((" + jtype + ") Ocl.unionAll(" + col + "))"; 
    } 

    if (operator.equals("->closure"))
    { String rel = right + ""; 
      if (entity == null) 
      { JOptionPane.showMessageDialog(null, "No entity for: " + this, 
                                      "Semantic error", JOptionPane.ERROR_MESSAGE);
        return ""; 
      }  
      return "Ocl.closure" + entity.getName() + rel + "(" + lqf + ")"; 
    } // left must be set-valued. 

    if (operator.equals("->reject") || operator.equals("|R"))
    { String sel = rejectQueryFormJava7(lqf,rqf,env,local);
      return sel; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals(":"))  
    { return "(" + lqf + " instanceof " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsKindOf"))  
    { return "(" + lqf + " instanceof " + right + ")"; }  
	
	if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsTypeOf"))  
    { return "(" + lqf + ".getClass() == " + right + ".class)"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("/:")) 
    { return "!(" + lqf + " instanceof " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("<:"))  
    { rqf = ((BasicExpression) right).classExtentQueryFormJava7(env,local);
      return "(" + rqf + ".containsAll(" + lqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includes"))  
    { return "(" + rqf + " instanceof " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormJava7(env,local);
      return "(" + lqf + ".containsAll(" + rqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludes")) 
    { return "!(" + rqf + " instanceof " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormJava7(env,local);
      return "Collections.disjoint(" + lqf + "," + rqf + ")"; 
    } 

    if (operator.equals("=>"))
    { return "(!(" + lqf + ") || " + rqf + ")"; } 

    if (operator.equals("->oclAsType"))
    { if (type != null) 
      { String jtyp = type.getJava7(elementType); 
        return "((" + jtyp + ") " + lqf + ")"; 
      }
      return lqf; 
    } 

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (!lmult && !rmult)
    { if (operator.equals("=")) // and comparitors
      { res = composeEqQueryForms(lqf,rqf,lprim,rprim); }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = composeNeqQueryForms(lqf,rqf,lprim,rprim); }
      else if (comparitors.contains(operator))
      { res = composeComparitorQueryForms(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/") || 
               operator.equals("mod") || operator.equals("div"))
      { res = composeMathOpQueryFormsJava7(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("->count"))
      { res = "Ocl.count(" + lqf + "," + rw + ")"; } 
      else if (operator.equals("->indexOf"))
      { res = "(" + lqf + ".indexOf(" + rw + ") + 1)"; } 
      else if (operator.equals("->lastIndexOf"))
      { res = "(" + lqf + ".lastIndexOf(" + rw + ") + 1)"; } 
      else if (operator.equals("->hasPrefix"))
      { res = "(" + lqf + ".startsWith(" + rqf + "))"; } 
      else if (operator.equals("->hasSuffix"))
      { res = "(" + lqf + ".endsWith(" + rqf + "))"; }
      else if (operator.equals("->hasMatch"))
      { res = "Ocl.hasMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->isMatch"))
      { res = lqf + ".matches(" + rqf + ")"; }  
      else if (operator.equals("->equalsIgnoreCase"))
      { res = "(" + lqf + ".equalsIgnoreCase(" + rqf + "))"; } 
      else if (operator.equals("->before"))
      { res = "Ocl.before(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->after"))
      { res = "Ocl.after(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->split"))
      { res = "Ocl.split(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->allMatches"))
      { res = "Ocl.allMatches(" + lqf + "," + rqf + ")"; }    
      else if (operator.equals("->firstMatch"))
      { res = "Ocl.firstMatch(" + lqf + ", " + rqf + ")"; } 
    }
    else if (lmult && !rmult) // convert right to mult
    { String rss = right.makeSetJava7(rw);
      if (left.isOrdered())
      { rss = right.makeSequenceJava7(rw); } 
         
      if (operator.equals("->includes"))
      { res = lqf + ".contains(" + rw + ")"; }
      else if (operator.equals("->excludes"))
      { res = "!(" + lqf + ".contains(" + rw + "))"; }
      else if (operator.equals("->excluding"))
      { res = "Ocl.subtract(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->excludingAt"))
      { res = "Ocl.removeAt(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->excludingFirst"))
      { res = "Ocl.removeFirst(" + lqf + "," + rw + ")"; }
      else if (operator.equals("->append"))
      { res = "Ocl.concatenate(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->including"))
      { res = "Ocl.union(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->prepend"))
      { res = "Ocl.concatenate(" + rss + "," + lqf + ")"; }
      else 
      { String rs = right.makeSetJava7(rqf);
        if (left.isOrdered())
        { rs = right.makeSequenceJava7(rqf); } 
 
        if (operator.equals("="))
        { res = lqf + ".equals(" + rs + ")"; }
        else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
        { res = "!(" + lqf + ".equals(" + rs + "))"; }
        else if (operator.equals(":") || operator.equals("<:"))
        { res = rs + ".containsAll(" + lqf + ")"; }
        else if (operator.equals("->includesAll"))
        { res = lqf + ".containsAll(" + rs + ")"; } 
        else if (operator.equals("/<:"))
        { res = "!(" + rs + ".containsAll(" + lqf + ")"; } 
        else if (operator.equals("->excludesAll"))
        { res = "Collections.disjoint(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("->including"))
        { res = "Ocl.union(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->excluding"))
        { res = "Ocl.subtract(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->excluding"))
        { res = "Ocl.removeAt(" + lqf + "," + rqf + ")"; } 
        else if (operator.equals("->prepend"))
        { res = "Ocl.concatenate(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("->append"))
        { res = "Ocl.concatenate(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->count"))
        { res = "Collections.frequency(" + lqf + "," + rw + ")"; } 
        else if (operator.equals("->indexOf"))
        { res = "(" + lqf + ".indexOf(" + rw + ") + 1)"; }
        else if (operator.equals("->lastIndexOf"))
        { res = "(" + lqf + ".lastIndexOf(" + rw + ") + 1)"; } 
        /* else if (operator.equals("->hasPrefix"))
        { res = "(" + lqf + ".startsWith(" + rqf + "))"; } 
        else if (operator.equals("->hasSuffix"))
        { res = "(" + lqf + ".endsWith(" + rqf + "))"; } */ 
        else   
        { res = composeSetQueryFormsJava7(lqf,rs); } 
      } 
    }
    else if (rmult && !lmult)
    { String lw = ""; 
      if (lprim) 
      { lw = left.wrap(lqf); } // only if left primitive
      else 
      { lw = lqf; } 

      String ls = left.makeSetJava7(lw);
      if (right.isOrdered())
      { ls = left.makeSequenceJava7(lw); } 

      if (operator.equals("="))
      { res = ls + ".equals(" + rqf + ")"; } 
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = "!(" + ls + ".equals(" + rqf + "))"; } 
      else if (operator.equals(":") || operator.equals("<:"))
      { res = rqf + ".contains(" + lw + ")"; }
      else if (operator.equals("/:"))
      { res = "!(" + rqf + ".contains(" + lw + "))"; }
      else if (operator.equals("->excludesAll"))
      { res = "Collections.disjoint(" + ls + "," + rqf + ")"; } 
      else 
      { res = "(" + lqf + " " + operator + " " + rqf + ")"; 
        // bNeeded = true; 
      } 
      // else 
      // { res = composeSetQueryFormsJava7(ls,rqf); }
    }
    else // both sets
    if (operator.equals("->indexOf"))
    { res = "(Collections.indexOfSubList(" + lqf + "," + rqf + ")+1)"; 
      // bNeeded = true; 
    } 
    else if (operator.equals("->lastIndexOf"))
    { res = "(Collections.lastIndexOfSubList(" + lqf + "," + rqf + ") + 1)"; 
      // bNeeded = true; 
    } 
    else 
    { res = composeSetQueryFormsJava7(lqf,rqf); }

    if (needsBracket)
    { return "( " + res + " )"; }
    return res;
  } // add brackets if needed

  public String queryFormCSharp(java.util.Map env, boolean local)
  { String res;
    String cont = "Controller.inst()"; 

    if (operator.equals("#") || operator.equals("#LC") || 
        "->existsLC".equals(operator) || operator.equals("->exists"))
    { return existsQueryFormCSharp(env,local); } 

    if (operator.equals("#1") || operator.equals("->exists1"))
    { return exists1QueryFormCSharp(env,local); } 

    if (operator.equals("->forAll") || operator.equals("!"))
    { return forAllQueryFormCSharp(env,local); } 

    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();
    boolean lprim = left.isPrimitive();
    boolean rprim = right.isPrimitive();
    String lqf = left.queryFormCSharp(env,local);
    String rqf = right.queryFormCSharp(env,local);
    // if (lqf == null || rqf == null) 
    // { return null; } 

    boolean bNeeded = false; 

    String javaOp = javaOpOf(operator);
    res = lqf + " " + javaOp + " " + rqf; // default
    // if & or or: &&, ||

    if (operator.equals("|") || operator.equals("->select"))
    { return selectQueryFormCSharp(lqf,rqf,env,local); } 

    if (operator.equals("|C") || operator.equals("->collect"))
    { return collectQueryFormCSharp(lqf,rqf,rprim,env,local); } 

    if (operator.equals("->selectMaximals"))
    { String col = collectQueryFormCSharp(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCSharp(env,local); }   
      return "SystemTypes.maximalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->selectMinimals"))
    { String col = collectQueryFormCSharp(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCSharp(env,local); } 
      return "SystemTypes.minimalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->sortedBy"))
    { String col = collectQueryFormCSharp(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCSharp(env,local); } 
      return "SystemTypes.sortedBy(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->count"))
    { return "SystemTypes.count(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->restrict"))
    { return "SystemTypes.restrictMap(" + lqf + "," + rqf + ")"; } 


    if (operator.equals("->antirestrict"))
    { return "SystemTypes.antirestrictMap(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->prepend"))
    { return "SystemTypes.prepend(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->append"))
    { return "SystemTypes.append(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->at"))
    {    
      System.err.println(this + " with " + left.type + " " + left.elementType); 

      if ("String".equals(left.type + ""))
      { return "(" + lqf + ").Substring(" + rqf + "-1 , 1)"; } 

      if (Type.isReferenceType(left.type))
      { return lqf + "[" + rqf + "-1]"; } 

      if (left.getElementType() != null)
      { String typ = left.getElementType().getCSharp(); 
       
        if (left.type != null && left.type.isMapType())
        { return "((" + typ + ") " + lqf + "[" + rqf + "])"; }
	  
        return "((" + typ + ") " + lqf + "[" + rqf + " - 1])";
      } 
      
      System.err.println("WARNING!: no element type in " + left); 

      if (left.type != null && left.type.isMapType())
      { return "(" + lqf + ")[" + rqf + "]"; }

      return "(" + lqf + ")[" + rqf + " - 1]";
    } 

    if (operator.equals("->apply"))
    { return lqf + ".Invoke(" + rqf + ")"; } 

    if (operator.equals("|A") || operator.equals("->any"))   
    { String getany = anyQueryFormCSharp(lqf,rqf,rprim,env,local); 
      String typ = type.getCSharp(); 
      return "((" + typ + ") " + getany + ")"; 
    } 
    
    if (operator.equals("->pow"))
    { return "Math.Pow(" + lqf + ", " + rqf + ")"; }      

    if (operator.equals("->gcd"))
    { return "SystemTypes.gcd(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->compareTo")) 
    { if (left.isNumeric() && right.isNumeric())
      { res = "((" + lqf + " < " + rqf + ") ? -1 : ((" + lqf + " > " + rqf + ") ? 1 : 0))"; } 
      else 
      { res = "((IComparable) " + lqf + ").CompareTo(" + rqf + ")"; }  
      return res; 
    } 

    if (operator.equals("+"))
    { String pres = lqf + " + " + rqf;
      if (needsBracket)
      { pres = "(" + pres + ")"; } 
 
      if ("null".equals(lqf) || "null".equals(rqf))
      { return pres; } 
      if (left.isPrimitive() && right.isPrimitive())
      { return pres; } 
      if (left.isString() && right.isString())
      { return pres; } 
      if (left.isString() && right.isPrimitive())
      { return pres; } 
      if (left.isPrimitive() && right.isString())
      { return pres; } 

      if (left.isString())
      { pres = lqf + " + " + rqf + ".ToString()"; 
        if (needsBracket)
        { pres = "(" + pres + ")"; } 
      } 
      else if (right.isString())
      { pres = lqf + ".ToString() + " + rqf;
        if (needsBracket)
        { pres = "(" + pres + ")"; } 
      }

      return pres; 
    }  

    if (operator.equals("->isUnique"))  // and define for B
    { String fcollect = collectQueryFormCSharp(lqf,rqf,rprim,env,local);
      return "SystemTypes.isUnique(" + fcollect + ")"; 
    } 

    if (operator.equals("->intersectAll"))
    { String col = collectQueryFormCSharp(lqf,rqf,rprim,env,local); 
      return "SystemTypes.intersectAll(" + col + ")"; 
    } 

    if (operator.equals("->unionAll"))
    { String col = collectQueryFormCSharp(lqf,rqf,rprim,env,local); 
      if (left.isOrdered() && (right.isOrdered() || "self".equals(right + "")))
      { return "SystemTypes.concatenateAll(" + col + ")"; } 
      return "SystemTypes.unionAll(" + col + ")"; 
    } 

    if (operator.equals("->closure"))
    { String rel = right + ""; 
      if (entity == null) 
      { JOptionPane.showMessageDialog(null, "No entity for: " + this, 
                                      "Semantic error", JOptionPane.ERROR_MESSAGE);
        return ""; 
      }  
      return "SystemTypes.closure" + entity.getName() + rel + "(" + lqf + ")"; 
    } // left must be set-valued. 

    if (operator.equals("->reject") || operator.equals("|R"))
    { String sel = rejectQueryFormCSharp(lqf,rqf,env,local);
      return sel; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals(":"))  
    { return "(" + lqf + " is " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsKindOf"))  
    { return "(" + lqf + " is " + right + ")"; } 
	
	if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsTypeOf"))  
    { return "(" + lqf + " is " + right + ")"; } 


    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("/:")) 
    { return "!(" + lqf + " is " + right + ")"; } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("<:"))  
    { rqf = ((BasicExpression) right).classExtentQueryFormCSharp(env,local);
      return "(SystemTypes.isSubset(" + lqf + ", " + rqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includes"))  
    { return "(" + rqf + " is " + left + ")"; } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormCSharp(env,local);
      return "(SystemTypes.isSubset(" + rqf + ", " + lqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludes")) 
    { return "!(" + rqf + " is " + left + ")"; } 


    if (operator.equals("=>"))
    { return "(!(" + lqf + ") || " + rqf + ")"; } 

    if (operator.equals("->oclAsType"))
    { if (type != null) 
      { String jtyp = type.getCSharp(); 
        return "((" + jtyp + ") " + lqf + ")"; 
      }
      return lqf;
    } 

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opcsharp = Expression.getOperatorCSharp(op); 
      if (opcsharp != null && opcsharp.length() > 0)
      { return opcsharp.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (!lmult && !rmult)
    { if (operator.equals("=")) // and comparitors
      { res = composeEqQueryFormsCSharp(lqf,rqf,lprim,rprim); }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = composeNeqQueryFormsCSharp(lqf,rqf,lprim,rprim); }
      else if (comparitors.contains(operator))
      { res = composeComparitorQueryFormsCSharp(lqf,rqf,lprim,rprim); 
        bNeeded = needsBracket; 
      } 
      else if (operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/") || 
               operator.equals("mod") || operator.equals("div"))
      { res = composeMathOpQueryFormsCSharp(lqf,rqf,lprim,rprim);
        bNeeded = needsBracket; 
      } 
      else if (operator.equals("->indexOf"))
      { res = "(" + lqf + ".IndexOf(" + rqf + ") + 1)"; } 
      else if (operator.equals("->lastIndexOf"))
      { res = "(" + lqf + ".LastIndexOf(" + rqf + ") + 1)"; } 
      else if (operator.equals("->count"))
      { res = "SystemTypes.count(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->hasPrefix"))
      { res = "(" + lqf + ".StartsWith(" + rqf + "))"; } 
      else if (operator.equals("->hasSuffix"))
      { res = "(" + lqf + ".EndsWith(" + rqf + "))"; } 
      else if (operator.equals("->hasMatch"))
      { res = "SystemTypes.hasMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->isMatch"))
      { res = "SystemTypes.isMatch(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->equalsIgnoreCase"))
      { res = "(" + lqf + ".ToLower().Equals(" + rqf + ".ToLower()))"; } 
      else if (operator.equals("->before"))
      { res = "SystemTypes.before(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->after"))
      { res = "SystemTypes.after(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->split"))
      { res = "SystemTypes.split(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->allMatches"))
      { res = "SystemTypes.allMatches(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->firstMatch"))
      { res = "SystemTypes.firstMatch(" + lqf + "," + rqf + ")"; } 
      else if (operator.equals("->excludingAt") && left.isString())
      { res = "SystemTypes.removeAtString(" + 
                                      lqf + "," + rqf + ")"; 
      }
      else if (operator.equals("->excludingAt"))
      { res = "SystemTypes.removeAt(" + 
                                      lqf + "," + rqf + ")"; 
      }
    }
    else if (lmult && !rmult) // convert right to mult
    { String rw = rqf; 
      String rss = right.makeSetCSharp(rw);
        
      if (operator.equals("->includes"))
      { res = lqf + ".Contains(" + rw + ")"; }
      else if (operator.equals("->excludes"))
      { res = "!(" + lqf + ".Contains(" + rw + "))"; }
      else if (operator.equals("->excluding"))
      { res = "SystemTypes.subtract(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->excludingAt"))
      { if (left.isString())
        { res = "SystemTypes.removeAtString(" + 
                                      lqf + "," + rqf + ")"; 
        }
        else 
        { res = "SystemTypes.removeAt(" + 
                                      lqf + "," + rqf + ")"; 
        }
      } 
      else if (operator.equals("->excludingFirst"))
      { res = "SystemTypes.removeFirst(" + lqf + "," + rw + ")"; }
      else if (operator.equals("->including") && left.isOrdered())
      { res = "SystemTypes.concatenate(" + lqf + "," + rss + ")"; }
      else if (operator.equals("->including"))
      { res = "SystemTypes.union(" + lqf + "," + rss + ")"; }
      else 
      { String rs = right.makeSetCSharp(rqf);
        if (operator.equals("="))
        { if (Type.isSetType(left.getType()))
          { res = "SystemTypes.equalsSet(" + lqf + "," + rs + ")"; } 
          else 
          { res = lqf + ".Equals(" + rs + ")"; }
        } 
        else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
        { if (Type.isSetType(left.getType()))
          { res = "!(SystemTypes.equalsSet(" + lqf + "," + rs + "))"; } 
          else 
          { res = "!(" + lqf + ".Equals(" + rs + "))"; }
        } 
        else if (operator.equals(":") || operator.equals("<:"))
        { res = "SystemTypes.isSubset(" + lqf + "," + rs + ")"; }
        else if (operator.equals("->includesAll"))
        { res = "SystemTypes.isSubset(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("/<:"))
        { res = "!(SystemTypes.isSubset(" + lqf + "," + rs + "))"; } 
        else if (operator.equals("->excludesAll"))
        { res = "SystemTypes.intersection(" + rs + "," + lqf + ").Count == 0"; } 
        else if (operator.equals("->including"))
        { res = "SystemTypes.union(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->excluding"))
        { res = "SystemTypes.subtract(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->prepend"))
        { res = "SystemTypes.concatenate(" + rs + "," + lqf + ")"; } 
        else if (operator.equals("->append"))
        { res = "SystemTypes.concatenate(" + lqf + "," + rs + ")"; } 
        else if (operator.equals("->count"))
        { res = "SystemTypes.count(" + lqf + "," + rqf + ")"; } 
        else if (operator.equals("->indexOf"))
        { res = "(" + lqf + ".IndexOf(" + rqf + ") + 1)"; } 
        else if (operator.equals("->lastIndexOf"))
        { res = "(" + lqf + ".LastIndexOf(" + rqf + ") + 1)"; } 
        else if (operator.equals("->hasMatch"))
        { res = "SystemTypes.hasMatch(" + lqf + ", " + rqf + ")"; } 
        else if (operator.equals("->isMatch"))
        { res = "SystemTypes.isMatch(" + lqf + "," + rqf + ")"; } 
        /* else if (operator.equals("->hasPrefix"))
        { res = "(" + lqf + ".StartsWith(" + rqf + "))"; } 
        else if (operator.equals("->hasSuffix"))
        { res = "(" + lqf + ".EndsWith(" + rqf + "))"; } */  
        else   
        { res = composeSetQueryFormsCSharp(lqf,rs); } 
      } 
    }
    else if (rmult && !lmult)
    { String ls = left.makeSetCSharp(lqf);
      String lw = lqf; 

      if (operator.equals("="))
      { if (Type.isSetType(right.getType()))
        { res = "SystemTypes.equalsSet(" + ls + "," + rqf + ")"; } 
        else 
        { res = ls + ".Equals(" + rqf + ")"; } 
      }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { if (Type.isSetType(right.getType()))
        { res = "!(SystemTypes.equalsSet(" + ls + "," + rqf + "))"; } 
        else 
        { res = "!(" + ls + ".Equals(" + rqf + "))"; } 
      }
      else if (operator.equals(":") || operator.equals("<:"))
      { res = rqf + ".Contains(" + lw + ")"; }
      else if (operator.equals("/:"))
      { res = "!(" + rqf + ".Contains(" + lw + "))"; }
      else if (operator.equals("->excludesAll"))
      { res = "(SystemTypes.intersection(" + rqf + "," + ls + ").Count == 0)"; } 
      else 
      { res = "(" + lqf + " " + operator + " " + rqf + ")"; 
        // bNeeded = true; 
      } 
      // else 
      // { res = composeSetQueryFormsCSharp(ls,rqf); }
    }
    else // both sets
    if (operator.equals("->indexOf"))
    { res = "SystemTypes.indexOfSubList(" + lqf + "," + rqf + ") + 1"; 
      bNeeded = true; 
    } 
    else if (operator.equals("->lastIndexOf"))
    { res = "SystemTypes.lastIndexOfSubList(" + lqf + "," + rqf + ") + 1"; 
      bNeeded = true; 
    } 
    else 
    { res = composeSetQueryFormsCSharp(lqf,rqf); }

    if (needsBracket || bNeeded)
    { return "( " + res + " )"; }
    return res;
  } // add brackets if needed

  public String queryFormCPP(java.util.Map env, boolean local)
  { String res;
    String cont = "Controller::inst->"; 

    if (operator.equals("#") || operator.equals("#LC") ||
        "->existsLC".equals(operator) || operator.equals("->exists"))
    { return existsQueryFormCPP(env,local); } 

    if (operator.equals("#1") || operator.equals("->exists1"))
    { return exists1QueryFormCPP(env,local); } 

    if (operator.equals("->forAll") || operator.equals("!"))
    { return forAllQueryFormCPP(env,local); } 

    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();
    boolean lprim = left.isPrimitive();
    boolean rprim = right.isPrimitive();
    String lqf = left.queryFormCPP(env,local);
    String rqf = right.queryFormCPP(env,local);

    Type lelemt = left.getElementType(); 
    String ename = "";  
    Entity ent = null; 
    String lcet = "void*"; 
    if (lelemt != null) 
    { lcet = lelemt.getCPP();
      ent = lelemt.getEntity();  
      ename = lelemt.getName();
    } 

    Type relemt = right.getElementType(); 
    String rename = ""; 
    String rcet = "void*"; 
    if (relemt != null) 
    { rcet = relemt.getCPP(); 
      rename = relemt.getName(); 
    } 

    String javaOp = javaOpOf(operator);
    res = lqf + " " + javaOp + " " + rqf; // default
    // if & or or: &&, ||

    if (operator.equals("|") || operator.equals("->select"))
    { return selectQueryFormCPP(lqf,rqf,env,local); } 

    if (operator.equals("|C") || operator.equals("->collect"))
    { return collectQueryFormCPP(lqf,rqf,rprim,env,local); } 

    if (operator.equals("->selectMaximals"))
    { String col = collectQueryFormCPP(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCPP(env,local); }   
      return "UmlRsdsLib<" + lcet + ">::maximalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->selectMinimals"))
    { String col = collectQueryFormCPP(lqf,rqf,rprim,env,local); 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCPP(env,local); } 
      return "UmlRsdsLib<" + lcet + ">::minimalElements(" + lqf + ", " + col + ")"; 
    } 

    if (operator.equals("->restrict"))
    { return "UmlRsdsLib<" + lcet + ">::restrict(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->antirestrict"))
    { return "UmlRsdsLib<" + lcet + ">::antirestrict(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->sortedBy"))
    { // String col = collectQueryFormCPP(lqf,rqf,rprim,env,local);
      java.util.Map enva = (java.util.Map) ((java.util.HashMap) env).clone(); 
      java.util.Map envb = (java.util.Map) ((java.util.HashMap) env).clone();
      String var1 = ename.toLowerCase() + "_1"; 
      String var2 = ename.toLowerCase() + "_2"; 
      enva.put(ename,var1); 
      envb.put(ename,var2);  
      String rqf1 = right.queryFormCPP(enva,false); 
      String rqf2 = right.queryFormCPP(envb,false); 
      String fname = Identifier.nextIdentifier(ename); 
      String compareFunc = "  friend bool compareTo" + fname + "(" + ename + "* " + var1 + 
                                                                ", " + ename + "* " + var2 + ")\n" + 
                           "  { return " + rqf1 + " < " + rqf2 + "; }\n";
      // System.out.println(compareFunc);    

      if (ent != null) 
      { ent.addAux(compareFunc); } 
      else if (entity != null) 
      { entity.addAux(compareFunc); } 
 
      if (left.umlkind == CLASSID)
      { lqf = ((BasicExpression) left).classExtentQueryFormCPP(env,local); } 
      return "std::sort(" + lqf + "->begin(), " + lqf + "->end(), compareTo" + fname + ")"; 
    } 

    if (operator.equals("->count"))
    { return "UmlRsdsLib<" + lcet + ">::count(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->equalsIgnoreCase"))
    { return "UmlRsdsLib<" + lcet + ">::equalsIgnoreCase(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("->compareTo")) 
    { res = "(" + lqf + " < " + rqf + ")?-1:((" + lqf + " > " + rqf + ")?1:0)";
      return res; 
    } 
    // But the user must define such an operation in their classes

    if (operator.equals("->at") && left.getType() != null)
    { Type lefttype = left.getType();
      Type lelemtype = left.getElementType();  

      if ("String".equals(lefttype.getName()))
      { return "(" + lqf + ").substr(" + rqf + "-1 , 1)"; } 

      if (lefttype.isMapType())
      { if (lelemtype != null) 
        { String etyp = 
             lelemtype.getCPP(lelemtype.getElementType()); 
          return "((" + etyp + ") (" + lqf + ")->at(" + rqf + "))"; 
        }
        return "(" + lqf + ")->at(" + rqf + ")"; 
      } 

      if (lelemtype != null) 
      { String etyp = 
             lelemtype.getCPP(lelemtype.getElementType());
        return "((" + etyp + ") (" + lqf + ")->at(" + rqf + " - 1))"; 
      }
      return "(" + lqf + ")->at(" + rqf + " - 1)"; 
    } 

    if (operator.equals("->apply"))
    { return "(" + lqf + ")(" + rqf + ")"; } 
	
    if (operator.equals("|A") || operator.equals("->any"))   
    { String getany = anyQueryFormCPP(lqf,rqf,env,local); 
      String typ = type.getCPP(elementType); 
      return "((" + typ + ") " + getany + ")"; 
    } 


    if (operator.equals("->pow"))
    { return "pow(" + lqf + ", " + rqf + ")"; }      

    if (operator.equals("->gcd"))
    { return "UmlRsdsLib<long>::gcd(" + lqf + "," + rqf + ")"; } 

    if (operator.equals("+"))
    { String pres = lqf + " + " + rqf;
      if (needsBracket)
      { pres = "(" + pres + ")"; } 
 
      if ("null".equals(lqf) && "null".equals(rqf))
      { return "\"\""; }
      if ("null".equals(lqf))
      { return cppStringOf(rqf, right.getType()); }
      if ("null".equals(rqf))
      { return cppStringOf(lqf, left.getType()); }
 
      if (left.isPrimitive() && right.isPrimitive())
      { return pres; } 
      if (left.isString() && right.isString())
      { pres = "string(" + lqf + ").append(" + rqf + ")"; 
        return pres; 
      } 
      else if (left.isString())
      { return "string(" + lqf + ").append(" + cppStringOf(rqf, right.getType()) + ")"; } 
      else if (right.isString())
      { return "(" + cppStringOf(lqf, left.getType()) + ").append(" + rqf + ")"; } 
       
     /* if (left.isString() && right.isPrimitive())
      { return pres; } 
      if (left.isPrimitive() && right.isString())
      { return pres; } 

      if (left.isString())
      { pres = lqf + " + " + rqf + ".ToString()"; 
        if (needsBracket)
        { pres = "(" + pres + ")"; } 
      } 
      else if (right.isString())
      { pres = lqf + ".ToString() + " + rqf;
        if (needsBracket)
        { pres = "(" + pres + ")"; } 
      } */ 

      return pres; 
    }  

    if (operator.equals("->isUnique"))  // and define for B
    { String fcollect = collectQueryFormCPP(lqf,rqf,rprim,env,local);
      // if (left.umlkind == CLASSID)
      // { lqf = ((BasicExpression) left).classExtentQueryFormCPP(env,local); } 
      // return "(" + lqf + "->size() == " + fcollect + "->size())"; 
      return "UmlRsdsLib<" + rcet + ">::isUnique(" + fcollect + ")"; 
    } 

    if (operator.equals("->intersectAll"))
    { String col = collectQueryFormCPP(lqf,rqf,rprim,env,local); 
      return "UmlRsdsLib<" + rcet + ">::intersectAll(" + col + ")"; 
    } // rcet, not lcet?

    if (operator.equals("->unionAll"))
    { String col = collectQueryFormCPP(lqf,rqf,rprim,env,local); 
      if (left.isOrdered() && (right.isOrdered() || "self".equals(right + "")))
      { return "UmlRsdsLib<" + rcet + ">::concatenateAll(" + col + ")"; } 
      return "UmlRsdsLib<" + rcet + ">::unionAll(" + col + ")"; 
    } // rcet, not lcet

    if (operator.equals("->closure"))
    { String rel = right + ""; 
      if (entity == null) 
      { JOptionPane.showMessageDialog(null, "No entity for: " + this, 
                                      "Semantic error", JOptionPane.ERROR_MESSAGE);
        return ""; 
      }  
      return "UmlRsdsLib<" + rcet + ">::closure" + entity.getName() + rel + "(" + lqf + ")"; 
    } // left must be set-valued. 

    if (operator.equals("->reject") || operator.equals("|R"))
    { String sel = rejectQueryFormCPP(lqf,rqf,env,local);
      return sel; 
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals(":"))  
    { String rinstances = cont + "get" + ("" + right).toLowerCase() + "_s()"; 
      return "UmlRsdsLib<" + right + "*>::isIn((" + right + "*) " + lqf + ", " + rinstances + ")";
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsKindOf"))  
    { String rinstances = cont + "get" + ("" + right).toLowerCase() + "_s()"; 
      return "UmlRsdsLib<" + right + "*>::isIn((" + right + "*) " + lqf + ", " + rinstances + ")";
    } 
	
    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("->oclIsTypeOf"))  
    { String rinstances = cont + "get" + ("" + right).toLowerCase() + "_s()"; 
      return "UmlRsdsLib<" + right + "*>::isIn((" + right + "*) " + lqf + ", " + rinstances + ")";
    }

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("/:")) 
    { String rinstances = cont + "get" + ("" + right).toLowerCase() + "_s()"; 
      return "!(UmlRsdsLib<" + right + "*>::isIn((" + right + "*) " + lqf + ", " + rinstances + "))";
    } 

    if (right.umlkind == CLASSID && 
        ((BasicExpression) right).arrayIndex == null && operator.equals("<:"))  
    { rqf = ((BasicExpression) right).classExtentQueryFormCPP(env,local);
      return "(UmlRsdsLib<" + right + "*>::isSubset(" + lqf + ", " + rqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includes"))  
    { String linstances = cont + "get" + ("" + left).toLowerCase() + "_s()"; 
      return "UmlRsdsLib<" + left + "*>::isIn(" + rqf + ", " + linstances + ")"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->includesAll"))  
    { lqf = ((BasicExpression) left).classExtentQueryFormCPP(env,local);
      return "(UmlRsdsLib<" + left + "*>::isSubset(" + rqf + ", " + lqf + "))"; 
    } 

    if (left.umlkind == CLASSID && 
        ((BasicExpression) left).arrayIndex == null && operator.equals("->excludes")) 
    { String linstances = cont + "get" + ("" + left).toLowerCase() + "_s()"; 
      return "!(UmlRsdsLib<" + left + "*>::isIn(" + rqf + ", " + linstances + "))"; 
    }  

    if (operator.equals("=>"))
    { return "(!(" + lqf + ") || " + rqf + ")"; } 

    if (operator.equals("->oclAsType"))
    { if (type != null) 
      { String jtyp = type.getCPP(elementType);
        if ("string".equals(jtyp))
        { return "string(" + lqf + ")"; }  
        return "((" + jtyp + ") " + lqf + ")"; 
      }
      return lqf;
    } 

    if (operator.equals("->at"))
    { if (type != null) 
      { String jtyp = type.getCPP(elementType); 
        return "((" + jtyp + ") (" + lqf + ")->at(" + rqf + " -1))"; 
      }
      return lqf;
    } // plus version for strings


    // No extension ops for C++? 
    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opcpp = Expression.getOperatorCPP(op); 
      if (opcpp != null && opcpp.length() > 0)
      { return opcpp.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (!lmult && !rmult)
    { if (operator.equals("=")) // and comparitors
      { res = composeEqQueryFormsCPP(lqf,rqf,lprim,rprim); }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { res = composeNeqQueryFormsCPP(lqf,rqf,lprim,rprim); }
      else if (comparitors.contains(operator))
      { res = composeComparitorQueryFormsCPP(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/") || 
               operator.equals("mod") || operator.equals("div"))
      { res = composeMathOpQueryFormsCPP(lqf,rqf,lprim,rprim); } 
      else if (operator.equals("->indexOf"))
      { res = "UmlRsdsLib<" + lcet + ">::indexOf(" + rqf + ", " + lqf + ")"; } 
      else if (operator.equals("->lastIndexOf"))
      { res = "UmlRsdsLib<" + lcet + ">::lastIndexOf(" + rqf + ", " + lqf + ")"; } 
      else if (operator.equals("->count"))
      { res = "UmlRsdsLib<" + lcet + ">::count(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->equalsIgnoreCase"))
      { res = "UmlRsdsLib<string>::equalsIgnoreCase(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->hasPrefix"))
      { res = "UmlRsdsLib<string>::startsWith(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->hasSuffix"))
      { res = "UmlRsdsLib<string>::endsWith(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->isMatch"))
      { res = "UmlRsdsLib<string>::isMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->hasMatch"))
      { res = "UmlRsdsLib<string>::hasMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->allMatches"))
      { res = "UmlRsdsLib<string>::allMatches(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->firstMatch"))
      { res = "UmlRsdsLib<string>::firstMatch(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->split"))
      { res = "UmlRsdsLib<string>::split(" + lqf + ", " + rqf + ")"; } 
      else if (operator.equals("->excludingAt"))
      { res = "UmlRsdsLib<" + lcet + ">::removeAt(" + lqf + "," + rqf + ")"; }
    }
    else if (lmult && !rmult) // convert right to mult
    { String rw = rqf; 
      
      if (operator.equals("->includes"))
      { res = "UmlRsdsLib<" + lcet + ">::isIn(" + rw + ", " + lqf + ")"; }
      else if (operator.equals("->excludes"))
      { res = "!(UmlRsdsLib<" + lcet + ">::isIn(" + rw + ", " + lqf + "))"; }
      else if (operator.equals("->excluding"))
      { String rss = right.makeSetCPP(rw);
        res = "UmlRsdsLib<" + lcet + ">::subtract(" + lqf + "," + rss + ")";  
      }
      else if (operator.equals("->excludingAt"))
      { res = "UmlRsdsLib<" + lcet + ">::removeAt(" + lqf + "," + rqf + ")"; }
      else if (operator.equals("->excludingFirst"))
      { res = "UmlRsdsLib<" + lcet + ">::removeFirst(" + lqf + "," + rw + ")"; }
      else if (operator.equals("->append"))   // Only for sequences
      { String rss = right.makeSequenceCPP(rw);
        res = "UmlRsdsLib<" + lcet + ">::concatenate(" + lqf + "," + rss + ")"; 
      }
      else if (operator.equals("->including")) // Only for sets
      { if (left.isOrdered() || left.hasSequenceType())
        { String rssq = right.makeSequenceCPP(rw);
          res = "UmlRsdsLib<" + lcet + ">::concatenate(" + rssq + "," + lqf + ")"; 
        } 
        else 
        { String rss = right.makeSetCPP(rw);
          res = "UmlRsdsLib<" + lcet + ">::unionSet(" + lqf + "," + rss + ")";
        }  
      }
      else if (operator.equals("->prepend")) // Only for sequences
      { String rss = right.makeSequenceCPP(rw);
        res = "UmlRsdsLib<" + lcet + ">::concatenate(" + rss + "," + lqf + ")"; 
      }
      else 
      { String rs = right.makeSetCPP(rqf);
        String rsq = right.makeSequenceCPP(rqf); 
        if (operator.equals("="))
        { if (left.isOrdered() || left.hasSequenceType())
          { res = "(*(" + lqf + ") == *(" + rsq + "))"; } 
          else 
          { res = "(*(" + lqf + ") == *(" + rs + "))"; }
        }  
        else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
        { if (left.isOrdered() || left.hasSequenceType())
          { res = "(*(" + lqf + ") != *(" + rsq + "))"; } 
          else 
          { res = "(*(" + lqf + ") != *(" + rs + "))"; }
        }
        else if (operator.equals(":") || operator.equals("<:"))
        { if (left.isOrdered() || left.hasSequenceType()) 
          { lqf = "UmlRsdsLib<" + lcet + ">::asSet(" + lqf + ")"; } 
          res = "UmlRsdsLib<" + lcet + ">::isSubset(" + lqf + ", " + rs + ")"; 
        } 
        else if (operator.equals("->includesAll"))
        { res = "UmlRsdsLib<" + lcet + ">::isSubset(" + rs + ", " + lqf + ")"; } 
        else if (operator.equals("/<:"))
        { res = "!(UmlRsdsLib<" + lcet + ">::isSubset(" + lqf + ", " + rs + "))"; } 
        else if (operator.equals("->excludesAll"))
        { res = "(UmlRsdsLib<" + lcet + ">::intersection(" + rs + ", " + lqf + ")->size() == 0)"; } 
        else if (operator.equals("->including"))
        { res = "UmlRsdsLib<" + lcet + ">::unionSet(" + lqf + ", " + rs + ")"; } 
        else if (operator.equals("->excluding"))
        { res = "UmlRsdsLib<" + lcet + ">::subtract(" + lqf + ", " + rs + ")"; } 
        else if (operator.equals("->prepend"))
        { res = "UmlRsdsLib<" + lcet + ">::concatenate(" + rsq + ", " + lqf + ")"; } 
        else if (operator.equals("->append"))
        { res = "UmlRsdsLib<" + lcet + ">::concatenate(" + lqf + ", " + rsq + ")"; } 
        else if (operator.equals("->count"))
        { res = "UmlRsdsLib<" + lcet + ">::count(" + lqf + ", " + rqf + ")"; } 
        else if (operator.equals("->indexOf"))
        { res = "UmlRsdsLib<" + lcet + ">::indexOf(" + rqf + ", " + lqf + ")"; } 
        else if (operator.equals("->lastIndexOf"))
        { res = "UmlRsdsLib<" + lcet + ">::lastIndexOf(" + rqf + ", " + lqf + ")"; } 
        /* else if (operator.equals("->hasPrefix"))
        { res = "UmlRsdsLib<string>::startsWith(" + lqf + ", " + rqf + ")"; } 
        else if (operator.equals("->hasSuffix"))
        { res = "UmlRsdsLib<string>::endsWith(" + lqf + ", " + rqf + ")"; } */ 
        else   
        { res = composeSetQueryFormsCPP(lqf,rs,lcet); } 
      } 
    }
    else if (rmult && !lmult)
    { String ls = left.makeSetCPP(lqf);
      String lw = lqf; 

      if (operator.equals("="))
      { if (Type.isCollectionOrEntity(right.getType()))
        { res = "(*(" + ls + ") == *(" + rqf + "))"; } 
        else 
        { res = "(" + lqf + " == " + rqf + ")"; }  
      }
      else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
      { if (Type.isCollectionOrEntity(right.getType()))
        { res = "(*(" + ls + ") != *(" + rqf + "))"; } 
        else 
        { res = "(" + lqf + " != " + rqf + ")"; }    
      }
      else if (operator.equals(":") || operator.equals("<:"))
      { res = "UmlRsdsLib<" + lcet + ">::isIn(" + lw + ", " + rqf + ")"; }
      else if (operator.equals("/:"))
      { res = "!(UmlRsdsLib<" + lcet + ">::isIn(" + lw + ", " + rqf + "))"; }
      else if (operator.equals("->excludesAll"))
      { res = "(UmlRsdsLib<" + lcet + ">::intersection(" + rqf + ", " + ls + ")->size() == 0)"; } 
      else 
      { res = "(" + lqf + " " + operator + " " + rqf + ")"; 
        // bNeeded = true; 
      } 
      // else 
      // { res = composeSetQueryFormsCPP(ls,rqf,lcet); }
    }
    else // both sets
    if (operator.equals("->indexOf"))
    { res = "UmlRsdsLib<" + lcet + ">::indexOf(" + rqf + ", " + lqf + ")"; } 
    else if (operator.equals("->lastIndexOf"))
    { res = "UmlRsdsLib<" + lcet + ">::lastIndexOf(" + rqf + ", " + lqf + ")"; } 
    else 
    { res = composeSetQueryFormsCPP(lqf,rqf,lcet); }

    if (needsBracket)
    { return "( " + res + " )"; }
    return res;
  } // add brackets if needed  

  public String exists1QueryForm(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if (operator.equals("->exists1"))
    { lqf = left.queryForm(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("Warning: no element type for: " + left); 
        // JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
        // "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#1")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("Warning: no element type of: " + beleft); 
        // JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
        // "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = null;  
    if (operator.equals("->exists1") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x";  // formal parameter 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
                    
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename);   // actual parameter
      if (ex == null) 
      { ex = "this"; } // for outer instance context only. NOT VALID IN STATIC CONTEXT

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists1 query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExists1Definition(existsleft,lqf,right,existsvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String exists1QueryFormJava6(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if (operator.equals("->exists1"))
    { lqf = left.queryFormJava6(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#1")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = null;  
    if (operator.equals("->exists1") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x";  // formal parameter 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
            
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename);   // actual parameter
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 

        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists1 query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExists1DefinitionJava6(existsleft,lqf,right,existsvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String quantifierQueryFormJava7(java.util.Map env, boolean local)
  { String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if (operator.equals("->exists1") || 
        "->existsLC".equals(operator) || operator.equals("->exists") || operator.equals("->forAll"))
    { lqf = left.queryFormJava7(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#1") || operator.equals("#LC") || 
             operator.equals("#") || operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = null;  
    if (localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x";  // formal parameter 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
            
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename);   // actual parameter
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = "unknownQuantifier()"; 
    if (operator.equals("->exists1") || operator.equals("#1"))
    { res = BSystemTypes.getExists1DefinitionJava7(existsleft,lqf,right,existsvar,env1,pars); } 
    else if (operator.equals("->exists") || operator.equals("#") || 
             operator.equals("#LC") || operator.equals("->existsLC"))
    { res = BSystemTypes.getExistsDefinitionJava7(existsleft,lqf,right,existsvar,env1,pars); } 
    else if (operator.equals("->forAll") || operator.equals("!"))
    { res = BSystemTypes.getForAllDefinitionJava7(existsleft,lqf,right,existsvar,env1,pars); } 
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String exists1QueryFormCSharp(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if (operator.equals("->exists1"))
    { lqf = left.queryFormCSharp(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#1")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = null;  
    if (operator.equals("->exists1") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x";  // formal parameter 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
            
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename);   // actual parameter
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists1 query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExists1DefinitionCSharp(existsleft,lqf,right,existsvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String exists1QueryFormCPP(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if (operator.equals("->exists1"))
    { lqf = left.queryFormCPP(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#1")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = null;  
    if (operator.equals("->exists1") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x";  // formal parameter 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
            
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename);   // actual parameter
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists1 query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExists1DefinitionCPP(existsleft,lqf,right,existsvar,env1,pars); 
    return "" + res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String selectQueryForm(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->select"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|") || operator.equals("|selectMaximals") || operator.equals("|selectMinimals")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self"))
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");   // so that it is not expressed as 'this' in the code
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    /* for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (use.getType() != null) 
      { Attribute par = new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        if (pars.contains(par)) { } 
        else 
        { pars.add(par);
          if ("self".equals(use.data))
          { callpars = callpars + ", this"; } 
          else 
          { callpars = callpars + "," + use.data; } 
        } 
      } 
    } */

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getSelectDefinition(selectleft,lqf,right,selectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String selectQueryFormJava6(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->select"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  


    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");   // so that it is not expressed as 'this' in the code
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getSelectDefinitionJava6(selectleft,lqf,right,selectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String selectQueryFormJava7(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->select") || operator.equals("->reject"))
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|") || operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  


    Vector euses = right.getBaseEntityUses(); 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self"); 
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = "unknownSelect()"; 
    if (operator.equals("->select") || operator.equals("|"))
    { res = BSystemTypes.getSelectDefinitionJava7(selectleft,lqf,right,selectvar,env1,pars); } 
    else if (operator.equals("->reject") || operator.equals("|R"))
    { res = BSystemTypes.getRejectDefinitionJava7(selectleft,lqf,right,selectvar,env1,pars); }  
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String selectQueryFormCSharp(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 
    String selectvar = null; 
    Expression selectleft = left;

    Entity localentity = entity; 
    if (operator.equals("->select"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    // Entity localentity = left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getSelectDefinitionCSharp(selectleft,lqf,right,selectvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String selectQueryFormCPP(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    Entity localentity = entity; // left.elementType.getEntity();
    String selectvar = null; 
    Expression selectleft = left;

    if (operator.equals("->select"))
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  


    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar) || 
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");   
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getSelectDefinitionCPP(selectleft,lqf,right,selectvar,env1,pars); 
    return // "UmlRsdsLib<" + localentity.getName() + "*>::" + 
               res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String rejectQueryForm(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->reject"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating REJECT " + uses + " " + env1); 
    String res = BSystemTypes.getRejectDefinition(selectleft,lqf,right,selectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String rejectQueryFormJava6(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->reject"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getRejectDefinitionJava6(selectleft,lqf,right,selectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String rejectQueryFormJava7(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->reject"))
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getRejectDefinitionJava7(selectleft,lqf,right,selectvar,env1,pars); 
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String rejectQueryFormCSharp(String lqf, String rqf, java.util.Map env, 
                                       boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->reject"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getRejectDefinitionCSharp(selectleft,lqf,right,selectvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String rejectQueryFormCPP(String lqf, String rqf, java.util.Map env, 
                                       boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();

    String selectvar = null; 
    Expression selectleft = left;
    Entity localentity = null; 
 
    if (operator.equals("->reject"))
    { // lqf = left.queryForm(env,local);
      // selectleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|R")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar + "") ||
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getRejectDefinitionCPP(selectleft,lqf,right,selectvar,env1,pars); 
    return res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String anyQueryForm(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // any_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String collectvar = null; 
    Expression collectleft = left;
    Entity localentity = left.entity; // left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.



    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|A")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);                 
	    JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " Uses: " + uses); 
    // System.out.println("Collect local entity: " + localentity); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) ||
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Collect " + uses + " " + env1); 
    String res = BSystemTypes.getAnyDefinition(collectleft,lqf,right,collectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String anyQueryFormJava6(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // any_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String collectvar = null; 
    Expression collectleft = left;
    Entity localentity = left.entity; 
    // left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.



    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|A")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);                 
	    JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " Uses: " + uses); 
    // System.out.println("Collect local entity: " + localentity); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) ||
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getAnyDefinitionJava6(collectleft,lqf,right,collectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String anyQueryFormJava7(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // any_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String collectvar = null; 
    Expression collectleft = left;
    Entity localentity = left.entity; 
    // left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.



    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|A")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);                 
	    JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " Uses: " + uses); 
    // System.out.println("Collect local entity: " + localentity); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) ||
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getAnyDefinitionJava7(collectleft,lqf,right,collectvar,env1,pars); 
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String anyQueryFormCSharp(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // any_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String collectvar = null; 
    Expression collectleft = left;
    Entity localentity = left.entity; 
    // left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.



    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|A")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);                 
	    JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " Uses: " + uses); 
    // System.out.println("Collect local entity: " + localentity); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) ||
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getAnyDefinitionCSharp(collectleft,lqf,right,collectvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 
  
  private String anyQueryFormCPP(String lqf, String rqf, java.util.Map env, 
                                 boolean local) 
  { // select_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    Entity localentity = entity; // left.elementType.getEntity();
    String selectvar = null; 
    Expression selectleft = left;

    if (operator.equals("->any"))
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("|A")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      selectleft = beleft.right; 
      selectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  


    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Base entities: " + trueeuses); 
    // System.out.println(localentity); 
    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the select. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(selectvar) || 
          (selectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (selectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");   
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Select " + uses + " " + env1); 
    String res = BSystemTypes.getAnyDefinitionCPP(selectleft,lqf,right,selectvar,env1,pars); 
    return // "UmlRsdsLib<" + localentity.getName() + "*>::" + 
               res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String collectQueryForm(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // collect_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    String collectvar = null; 
    Expression collectleft = left;
    Entity localentity = left.entity; // left.elementType.getEntity();

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.



    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|C") || operator.equals("|unionAll") || operator.equals("|intersectAll") || 
	    operator.equals("|concatenateAll") || operator.equals("|sortedBy") || operator.equals("|isUnique")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);                
	    JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " Uses: " + uses); 
    // System.out.println("Collect local entity: " + localentity); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) ||
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Collect " + uses + " " + env1); 
    String res = BSystemTypes.getCollectDefinition(collectleft,lqf,right,rprim,collectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String collectQueryFormJava6(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // collect_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 
    String collectvar = null; 
    Expression collectleft = left;

    Entity localentity = left.entity; // left.elementType.getEntity();
    if (operator.equals("|C")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("TYPE ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " filtered: " + trueeuses); 
    // System.out.println("Collect local entity: " + localentity); 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) || 
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Collect " + uses + " " + env); 
    String res =             BSystemTypes.getCollectDefinitionJava6(collectleft,lqf,right,rprim,collectvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String collectQueryFormJava7(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // collect_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 
    String collectvar = null; 
    Expression collectleft = left;

    Entity localentity = left.entity; // left.elementType.getEntity();
    if (operator.equals("|C")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("TYPE ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); } // May be null if primitive, String, etc
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " filtered: " + trueeuses); 
    // System.out.println("Collect local entity: " + localentity); 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) || 
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println(">>>>> Creating Collect " + right + " " + right.getType()); 
    String res = BSystemTypes.getCollectDefinitionJava7(collectleft,lqf,right,rprim,collectvar,env1,pars); 
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String collectQueryFormCSharp(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // collect_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();
    String collectvar = null; 
    Expression collectleft = left;

    Entity localentity = left.entity; // left.elementType.getEntity();
    if (operator.equals("|C")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("TYPE ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    // System.out.println("Collect base entities: " + euses + " filtered: " + trueeuses); 
    // System.out.println("Collect local entity: " + localentity); 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i);

      System.out.println(">> Variable use: " + use + " " + use.getType() + " " + use.arrayType + " " + use.getElementType()); 
 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) || 
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        pars.add(par);
        par.setElementType(use.getElementType()); 
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Collect " + uses + " " + env); 
    String res =       BSystemTypes.getCollectDefinitionCSharp(collectleft,lqf,right,rprim,collectvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  private String collectQueryFormCPP(String lqf, String rqf, boolean rprim,
                                 java.util.Map env, 
                                 boolean local) 
  { // collect_ind(lqf) where ind is a unique index for left and right
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    // Entity localentity = left.elementType.getEntity();
    String collectvar = null; 
    Expression collectleft = left;

    Entity localentity = left.entity; // left.elementType.getEntity();
    if (operator.equals("|C")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      collectleft = beleft.right; 
      collectvar = beleft.left + ""; 
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("TYPE ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  
    else 
    { if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 

    Vector euses = right.getBaseEntityUses(); 
    // Should only have one element, the owner of the operation in which 
    // this occurs. Others are localentity and its superclasses.

    Vector trueeuses = new Vector(); 
    if (localentity != null) 
    { trueeuses = localentity.removeMyUses(euses); }  
    else 
    { trueeuses = euses; } 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the collect. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || (use.data + "").equals(collectvar) || 
          (collectvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (collectvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Collect " + uses + " " + env); 
    String res = BSystemTypes.getCollectDefinitionCPP(collectleft,lqf,right,rprim,collectvar,env1,pars); 
    return "" + res.substring(0,res.length()-1) + callpars + ")"; 
  } 



  public String existsQueryForm(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if ("->existsLC".equals(operator) || operator.equals("->exists"))
    { lqf = left.queryForm(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#") || operator.equals("#LC"))  
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { Type eltype = beleft.right.elementType; 
        // System.out.println("Element type of: " + beleft + " is " + eltype); 
        localentity = eltype.getEntity();
      }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->exists") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("******* Creating Exists query form " + existsleft + " " + lqf + " " + existsvar + " " + right); 
    String res = BSystemTypes.getExistsDefinition(existsleft,lqf,right,existsvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String existsQueryFormJava6(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if ("->existsLC".equals(operator) || operator.equals("->exists"))
    { lqf = left.queryFormJava6(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#") || operator.equals("#LC")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { Type eltype = beleft.right.elementType; 
        // System.out.println("Element type of: " + beleft + " is " + eltype); 
        localentity = eltype.getEntity();
      }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->exists") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExistsDefinitionJava6(existsleft,lqf,right,existsvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  public String existsQueryFormCSharp(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if ("->existsLC".equals(operator) || operator.equals("->exists"))
    { lqf = left.queryFormCSharp(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#") || operator.equals("#LC")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.out.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { Type eltype = beleft.right.elementType; 
        // System.out.println("Element type of: " + beleft + " is " + eltype); 
        localentity = eltype.getEntity();
      }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->exists") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    String res = BSystemTypes.getExistsDefinitionCSharp(existsleft,lqf,right,existsvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String existsQueryFormCPP(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String existsvar = null; 
    Expression existsleft = null;
    Entity localentity = null; 
 
    String lqf = ""; 
    if ("->existsLC".equals(operator) || operator.equals("->exists"))
    { lqf = left.queryFormCPP(env,local);
      existsleft = left; 
      // localentity = left.getEntity(); // or entity of the elementType
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("#") || operator.equals("#LC")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      existsleft = beleft.right; 
      existsvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { Type eltype = beleft.right.elementType; 
        // System.out.println("Element type of: " + beleft + " is " + eltype); 
        localentity = eltype.getEntity();
      }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->exists") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the exists. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(existsvar) ||
          (existsvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (existsvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 

    // System.out.println("Creating Exists query form " + uses + " " + env1 + " " + pars); 
    String res = BSystemTypes.getExistsDefinitionCPP(existsleft,lqf,right,existsvar,env1,pars); 
    return "" + res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  public String forAllQueryForm(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String forallvar = null; 
    Expression forallleft = null; 
    String lqf = ""; 
    Entity localentity = null; 

    if (operator.equals("->forAll"))
    { lqf = left.queryForm(env,local);
      forallleft = left; 
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryForm(env,local); 
      forallleft = beleft.right; 
      forallvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->forAll") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the forAll. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(forallvar) ||
          (forallvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (forallvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 
    // System.out.println("Creating forAll query form " + uses + " " + env1); 
    String res = BSystemTypes.getForAllDefinition(forallleft,lqf,right,
                                                  forallvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String forAllQueryFormJava6(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String forallvar = null; 
    Expression forallleft = null; 
    String lqf = ""; 
    Entity localentity = null; 

    if (operator.equals("->forAll"))
    { lqf = left.queryFormJava6(env,local);
      forallleft = left; 
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava6(env,local); 
      forallleft = beleft.right; 
      forallvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->forAll") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    /* if (trueeuses.size() > 0)
    { Entity context = (Entity) trueeuses.get(0); 

      // Not the true context, only the apparant type of naked features
      System.out.println("Apparent context: " + context); 

      Type etype = new Type(context); 
      Attribute eself = new Attribute("self",etype,ModelElement.INTERNAL); 
      pars.add(eself); 
      callpars = ", this"; 
    } */ 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the forAll. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(forallvar) ||
          (forallvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (forallvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 
    // System.out.println("Creating forAll query form " + uses + " " + env1); 
    String res = BSystemTypes.getForAllDefinitionJava6(forallleft,lqf,right,
                                                  forallvar,env1,pars); 
    return "Set." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String forAllQueryFormJava7(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String forallvar = null; 
    Expression forallleft = null; 
    String lqf = ""; 
    Entity localentity = null; 

    if (operator.equals("->forAll"))
    { lqf = left.queryFormJava7(env,local);
      forallleft = left; 
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left);
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormJava7(env,local); 
      forallleft = beleft.right; 
      forallvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->forAll") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    /* if (trueeuses.size() > 0)
    { Entity context = (Entity) trueeuses.get(0); 

      // Not the true context, only the apparant type of naked features
      System.out.println("Apparent context: " + context); 

      Type etype = new Type(context); 
      Attribute eself = new Attribute("self",etype,ModelElement.INTERNAL); 
      pars.add(eself); 
      callpars = ", this"; 
    } */ 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the forAll. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(forallvar) ||
          (forallvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (forallvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 
    // System.out.println("Creating forAll query form " + uses + " " + env1); 
    String res = BSystemTypes.getForAllDefinitionJava7(forallleft,lqf,right,
                                                  forallvar,env1,pars); 
    return "Ocl." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String forAllQueryFormCSharp(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String forallvar = null; 
    Expression forallleft = null; 
    String lqf = ""; 
    Entity localentity = null; 

    if (operator.equals("->forAll"))
    { lqf = left.queryFormCSharp(env,local);
      forallleft = left; 
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCSharp(env,local); 
      forallleft = beleft.right; 
      forallvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft);
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->forAll") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 


    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the forAll. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(forallvar) ||
          (forallvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (forallvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Type useType = use.getType(); 
        Type useElementType = use.getElementType(); 

        if (use.arrayIndex != null)
        { useType = use.arrayType; 
          useElementType = use.getType(); 
        }

        Attribute par = 
          new Attribute(use.data,useType,ModelElement.INTERNAL); 
        par.setElementType(useElementType); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;    
      } 
    } 
    // System.out.println("Creating forAll query form " + uses + " " + env1); 
    String res = BSystemTypes.getForAllDefinitionCSharp(forallleft,lqf,right,
                                                  forallvar,env1,pars); 
    return "SystemTypes." + res.substring(0,res.length()-1) + callpars + ")"; 
  } 

  public String forAllQueryFormCPP(java.util.Map env, boolean local)
  { // String rqf = right.queryForm(env,local);
    String forallvar = null; 
    Expression forallleft = null; 
    String lqf = ""; 
    Entity localentity = null; 

    if (operator.equals("->forAll"))
    { lqf = left.queryFormCPP(env,local);
      forallleft = left; 
      if (left.elementType == null) 
      { System.err.println("DESIGN ERROR: no element type for: " + left); 
        JOptionPane.showMessageDialog(null, "no element type for " + left + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      } 
      else
      { localentity = left.elementType.getEntity(); }  
    } 
    else if (operator.equals("!")) 
    { BinaryExpression beleft = (BinaryExpression) left; 
      lqf = beleft.right.queryFormCPP(env,local); 
      forallleft = beleft.right; 
      forallvar = beleft.left + ""; 
      // localentity = beleft.right.getEntity(); // or entity of the elementType
      if (beleft.right == null || beleft.right.elementType == null)
      { System.err.println("DESIGN ERROR: no element type of: " + beleft); 
        JOptionPane.showMessageDialog(null, "no element type for " + beleft + " in " + this, 
                                      "Design error", JOptionPane.ERROR_MESSAGE);
      }
      else  
      { localentity = beleft.right.elementType.getEntity(); }  
    }  

    Vector euses = right.getBaseEntityUses(); 
    Vector uses = right.getVariableUses(); 
    Vector pars = new Vector(); 
    Vector parnames = new Vector(); 
    String callpars = ""; 

    java.util.Map env1 = (java.util.Map) ((java.util.HashMap) env).clone(); 

    Vector trueeuses = new Vector();  
    if (operator.equals("->forAll") && localentity != null)
    { trueeuses = localentity.removeMyUses(euses); } 
    else 
    { trueeuses = euses; } 

    for (int j = 0; j < trueeuses.size(); j++) 
    { Entity externalEnt = (Entity) trueeuses.get(j); 
      // System.out.println("External entity used in " + this + " is: " + externalEnt); 
      Type etype = new Type(externalEnt); 
      String ename = externalEnt.getName(); 
      String evar = ename.toLowerCase() + "x"; 
      Attribute eself = new Attribute(evar,etype,ModelElement.INTERNAL); 
      eself.setElementType(etype); 
      
      pars.add(eself); 
      parnames.add(evar); 
      String ex = (String) env.get(ename); 
      if (ex == null) 
      { ex = "this"; } // for outer instance context only

      callpars = callpars + "," + ex;      
      env1.put(ename,evar);   // for evaluating the predicate of the forAll. 
    } 

    for (int i = 0; i < uses.size(); i++) 
    { BasicExpression use = (BasicExpression) uses.get(i); 
      if (parnames.contains(use.data) || 
          (use.data + "").equals(forallvar) ||
          (forallvar == null && (use.data + "").equals("self"))) 
      {}  
      else if (forallvar != null && (use.data + "").equals("self") && use.getType() != null)
      { Attribute par = 
          new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + ", this";    
        env1.put(use.getType().getName(), "self");  
      } 
      else if (use.getType() != null) 
      { Attribute par = new Attribute(use.data,use.getType(),ModelElement.INTERNAL); 
        par.setElementType(use.getElementType()); 
        pars.add(par);
        parnames.add(use.data); 
        callpars = callpars + "," + use.data;   
      } 
    } 
    // System.out.println("Creating C++ forAll query form " + uses + " " + env1); 
    String res =   
      BSystemTypes.getForAllDefinitionCPP(forallleft,lqf,right,forallvar,env1,pars); 
    return // "UmlRsdsLib<" + localentity + "*>::" + 
           res.substring(0,res.length()-1) + callpars + ")"; 
  } 


  private String composeNeqQueryForms(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res; 
    if (lprim && rprim)
    { res = lqf + " != " + rqf; }
    else if (lprim) // also for booleans, doubles
    { String rr = right.unwrap(rqf);
      res = lqf + " != " + rr;
    }
    else if (rprim)
    { String ll = left.unwrap(lqf);
      res = ll + " != " + rqf;
    }
    else // both objects
    { res = "!(" + lqf + ".equals(" + rqf + "))"; }
    return res; 
  }


  private String composeNeqQueryFormsCSharp(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res; 
    if (lprim || rprim)
    { res = lqf + " != " + rqf; }
    else // both objects
    { res = "!(" + lqf + ".Equals(" + rqf + "))"; }
    return res; 
  }

  private String composeNeqQueryFormsCPP(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res = lqf + " != " + rqf; 
    if (Type.isCollectionOrEntity(left.getType()) || 
        Type.isCollectionOrEntity(right.getType()))
    { res = "(*(" + lqf + ") != *(" + rqf + "))"; } 
    return res; 
  }


  private String composeEqQueryForms(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res;

    if (left.isString())
    { res = "((String) " + lqf + ").equals(" + rqf + ")"; } 
    else if (right.isString())
    { res = "((String) " + rqf + ").equals(" + lqf + ")"; } 
    else if (lprim && rprim)
    { res = lqf + " == " + rqf; }
    else if (lprim) // also for booleans, doubles
    { String rr = right.unwrap(rqf);
      res = lqf + " == " + rr;
    }
    else if (rprim)
    { String ll = left.unwrap(lqf);
      res = ll + " == " + rqf;
    }
    else // both objects. Special case for sets: lqf.containsAll(rqf) && 
         // rqf.containsAll(lqf)
    { res = lqf + ".equals(" + rqf + ")"; }
    return res;  
  }

  private String composeEqQueryFormsCSharp(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res;
    if (left.isString())
    { res = "((string) " + lqf + ").Equals(" + rqf + ")"; } 
    else if (right.isString())
    { res = "((string) " + rqf + ").Equals(" + lqf + ")"; } 
    else if (lprim || rprim)
    { res = lqf + " == " + rqf; }
    else // both objects. Special case for sets: lqf.containsAll(rqf) && 
         // rqf.containsAll(lqf)
    { res = lqf + ".Equals(" + rqf + ")"; }
    return res;  
  }

  private String composeEqQueryFormsCPP(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { String res = lqf + " == " + rqf; 
    if (Type.isCollectionOrEntity(left.getType()) || 
        Type.isCollectionOrEntity(right.getType()))
    { res = "(*(" + lqf + ") == *(" + rqf + "))"; } 
    return res;  
  }


  private String composeComparitorQueryForms(String lqf, String rqf, 
                                             boolean lprim, boolean rprim)
  { String res; 
    // System.out.println("Comparitor query form: " + this + " " + lprim + " " +
    //                    rprim); 
    if (lprim && rprim) 
    { res = lqf + " " + operator + " " + rqf; } 
    else if (lprim)
    { String rr = right.unwrap(rqf); 
      res = lqf + " " + operator + " " + rr; 
    } 
    else if (rprim) 
    { String ll = left.unwrap(lqf); 
      res = ll + " " + operator + " " + rqf; 
    } 
    else  // includes case of Strings 
    { res = "(" + lqf + ".compareTo(" + rqf + ") " + operator + " 0)"; } 
    // System.out.println("Comparitor query form: " + this + " is " + res); 
    return res;     
  } 

  private String composeComparitorQueryFormsCSharp(String lqf, String rqf, 
                                             boolean lprim, boolean rprim)
  { String res; 
    // System.out.println("Comparitor query form: " + this + " " + lprim + " " +
    //                    rprim); 
    if (lprim || rprim) 
    { res = lqf + " " + operator + " " + rqf; }  
    else  // includes case of Strings 
    { res = "(" + lqf + ".CompareTo(" + rqf + ") " + operator + " 0)"; } 
    // System.out.println("Comparitor query form: " + this + " is " + res); 
    return res;     
  } 

  private String composeComparitorQueryFormsCPP(String lqf, String rqf, 
                                             boolean lprim, boolean rprim)
  { String res = lqf + " " + operator + " " + rqf;  
    return res;     
  } 
 
  private String composeMathOpQueryForms(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { // operator is *, mod, /, +, -
    String res; 
    String op = operator; 
    if (operator.equals("mod"))
    { op = "%"; } 
    else if (operator.equals("div"))
    { op = "/"; } 

    if (operator.equals("-") && "String".equals(left.getType() + "") && 
        "String".equals(right.getType() + ""))
    { res = "Set.subtract(" + lqf + "," + rqf + ")"; } 
    else if (lprim && rprim)
    { res = lqf + " " + op + " " + rqf; }
    else if (lprim)
    { String rr = right.unwrap(rqf); 
      res = lqf + " " + op + " " + rr;
    }
    else if (rprim)
    { String ll = left.unwrap(lqf); 
      res = ll + " " + op + " " + rqf;
    }
    else 
    { String rr = right.unwrap(rqf);
      String ll = left.unwrap(lqf); 
      res = ll + " " + op + " " + rr;
    }

    if ("div".equals(operator))
    { if ("int".equals(type + ""))
      { res = "((int) (" + res + "))"; } 
      else 
      { res = "((long) (" + res + "))"; } 
    } 

    return res;
  }

  private String composeMathOpQueryFormsJava7(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { // operator is *, mod, /, +, -
    String res; 
    String op = operator; 
    if (operator.equals("mod"))
    { op = "%"; } 
    else if (operator.equals("div"))
    { op = "/"; } 

    if (operator.equals("-") && "String".equals(left.getType() + "") && 
        "String".equals(right.getType() + ""))
    { res = "Ocl.subtract(" + lqf + "," + rqf + ")"; } 
    else if (lprim && rprim)
    { res = lqf + " " + op + " " + rqf; }
    else if (lprim)
    { String rr = right.unwrap(rqf); 
      res = lqf + " " + op + " " + rr;
    }
    else if (rprim)
    { String ll = left.unwrap(lqf); 
      res = ll + " " + op + " " + rqf;
    }
    else 
    { String rr = right.unwrap(rqf);
      String ll = left.unwrap(lqf); 
      res = ll + " " + op + " " + rr;
    }

    if ("div".equals(operator))
    { if ("int".equals(type + ""))
      { res = "((int) (" + res + "))"; } 
      else 
      { res = "((long) (" + res + "))"; } 
    } 

    return res;
  }

  private String composeMathOpQueryFormsCSharp(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { // operator is *, mod, /, +, -
    String res; 
    String op = operator; 
    if (operator.equals("mod"))
    { op = "%"; } 
    else if (operator.equals("div"))
    { op = "/"; } 

    if (operator.equals("+"))
    { if ("null".equals(lqf) || "null".equals(rqf))
      { return lqf + " + " + rqf; } 
      if (left.isPrimitive() && right.isPrimitive())
      { return lqf + " + " + rqf; } 
      if (left.isString() && right.isString())
      { return lqf + " + " + rqf; } 
      if (left.isString() && right.isPrimitive())
      { return lqf + " + " + rqf; } 
      if (left.isPrimitive() && right.isString())
      { return lqf + " + " + rqf; } 
      if (left.isString())
      { return lqf + " + " + rqf + ".ToString()"; } 
      if (right.isString())
      { return lqf + ".ToString() + " + rqf; }

      return lqf + " + " + rqf; 
    }  



    if (operator.equals("-") && "String".equals(left.getType().getName()) && 
        "String".equals(right.getType().getName()))
    { res = "SystemTypes.subtract(" + lqf + "," + rqf + ")"; } 
    else 
    { res = lqf + " " + op + " " + rqf; }

    if ("div".equals(operator))
    { if ("int".equals(type + ""))
      { res = "((int) (" + res + "))"; } 
      else 
      { res = "((long) (" + res + "))"; } 
    } 
    
    return res;
  }

  private String composeMathOpQueryFormsCPP(String lqf, 
                   String rqf, boolean lprim, 
                   boolean rprim)
  { // operator is *, mod, /, +, -
    String res; 
    String op = operator; 
    if (operator.equals("mod"))
    { op = "%"; } 
    else if (operator.equals("div"))
    { op = "/"; } 

    if (operator.equals("+"))
    { if ("String".equals(left.getType().getName()))
      { if ("String".equals(right.getType().getName()))
        { if (left.umlkind == VALUE)
          { res = "string(" + lqf + ").append(" + rqf + ")"; } 
          else 
          { res = lqf + ".append(" + rqf + ")"; }
        }  
        else 
        { if (left.umlkind == VALUE)
          { res = "string(" + lqf + ").append(std::to_string(" + rqf + "))"; } 
          else 
          { res = lqf + ".append(std::to_string(" + rqf + "))"; }
        } 
      } 
      else if ("String".equals(right.getType().getName()))
      { res = "std::to_string(" + lqf + ").append(" + rqf + ")"; } 
      else 
      { res = lqf + " + " + rqf; } 
    } 
    else if (operator.equals("-") && "String".equals(left.getType().getName()) && 
             "String".equals(right.getType().getName()))
    { res = "UmlRsdsLib<string>::subtract(" + lqf + ", " + rqf + ")"; } 
    else 
    { res = lqf + " " + op + " " + rqf; }
    
    if ("div".equals(operator))
    { res = "((int) (" + res + "))"; } 

    return res;
  }


  private String composeSetQueryForms(String lqf, String rqf)
  { String res = lqf + " " + operator + " " + rqf; 
    if (operator.equals("="))
    { if (Type.isSetType(left.getType()) || Type.isSetType(right.getType()))
      { res = "Set.equals(" + lqf + "," + rqf + ")"; } 
      else 
      { res = lqf + ".equals(" + rqf + ")"; }  
    } // order shouldn't matter
    else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { if (Type.isSetType(left.getType()) || Type.isSetType(right.getType()))
      { res = "!(Set.equals(" + lqf + "," + rqf + "))"; } 
      else 
      { res = "!(" + lqf + ".equals(" + rqf + "))"; }
    } 
    else if (operator.equals(":") || operator.equals("<:")) 
    { res = rqf + ".containsAll(" + lqf + ")"; }
    else if (operator.equals("->includesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "Set.includesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = lqf + ".containsAll(" + rqf + ")"; }
	} 
    else if (operator.equals("-"))
    { if (left.isMap() && right.isMap())
	  { res = "Set.excludeAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Set.subtract(" + lqf + "," + rqf + ")"; } 
	}
    else if (operator.equals("->excluding"))  
    { res = "Set.subtract(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("\\/") || operator.equals("->union")) 
    { if (left.isMap())
      { res = "Set.unionMap(" + lqf + "," + rqf + ")"; }
      else if (left.isOrdered())
      { res = "Set.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Set.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("->including"))
    { if (left.isOrdered())
      { res = "Set.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Set.union(" + lqf + "," + rqf + ")"; }
    }
    else if (operator.equals("/\\") || operator.equals("->intersection"))
    { if (left.isMap())
	  { res = "Set.intersectionMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Set.intersection(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("/:") || operator.equals("/<:"))
    { res = "!(" + rqf + ".containsAll(" + lqf + "))"; } 
    else if (operator.equals("->excludesAll"))
    { if (left.isMap())
	  { res = "Set.excludesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "(Set.intersection(" + rqf + "," + lqf + ").size() == 0)"; }
	}        
    else if (operator.equals("^"))
    { res = "Set.concatenate(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->symmetricDifference"))
    { res = "Set.symmetricDifference(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->prepend"))
    { res = "Set.concatenate(" + rqf + "," + lqf + ")"; } 
    else if (operator.equals("->append"))
    { res = "Set.concatenate(" + lqf + "," + rqf + ")"; } 
    return res; 
  } // what about comparitors? 

  private String composeSetQueryFormsJava6(String lqf, String rqf)
  { String res = lqf + " " + operator + " " + rqf; 
    if (operator.equals("="))
    { res = lqf + ".equals(" + rqf + ")"; }  
    else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { res = "!(" + lqf + ".equals(" + rqf + "))"; }
    else if (operator.equals(":") || operator.equals("<:")) 
    { res = rqf + ".containsAll(" + lqf + ")"; }
    else if (operator.equals("->includesAll"))
    { if (left.isMap())
	  { res = "Set.includesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = lqf + ".containsAll(" + rqf + ")"; }
	}
    else if (operator.equals("-"))
    { if (left.isMap())
	  { res = "Set.excludeAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Set.subtract(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("->excluding"))  
    { res = "Set.subtract(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("\\/") || operator.equals("->union")) 
    { if (left.isMap())
	  { res = "Set.unionMap(" + lqf + "," + rqf + ")"; }
	  else if (left.isOrdered() && right.isOrdered())
      { res = "Set.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Set.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("->including"))
    { if (left.isOrdered())
      { res = "Set.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Set.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("/\\") || operator.equals("->intersection"))
    { if (left.isMap())
	  { res = "Set.intersectionMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Set.intersection(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("/:") || operator.equals("/<:"))
    { res = "!(" + rqf + ".containsAll(" + lqf + "))"; } 
    else if (operator.equals("->excludesAll"))
    { if (left.isMap())
	  { res = "Set.excludesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Collections.disjoint(" + lqf + "," + rqf + ")"; }
	}        
    else if (operator.equals("^"))
    { res = "Set.concatenate(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->symmetricDifference"))
    { res = "Set.symmetricDifference(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->prepend"))
    { res = "Set.concatenate(" + rqf + "," + lqf + ")"; } 
    else if (operator.equals("->append"))
    { res = "Set.concatenate(" + lqf + "," + rqf + ")"; } 
    return res; 
  } // what about comparitors? 

  private String composeSetQueryFormsJava7(String lqf, String rqf)
  { String res = lqf + " " + operator + " " + rqf; 
    if (operator.equals("="))
    { res = lqf + ".equals(" + rqf + ")"; }  
    else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { res = "!(" + lqf + ".equals(" + rqf + "))"; }
    else if (operator.equals(":") || operator.equals("<:")) 
    { res = rqf + ".containsAll(" + lqf + ")"; }
    else if (operator.equals("->includesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "Ocl.includesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = lqf + ".containsAll(" + rqf + ")"; }
	} 
    else if (operator.equals("-"))
    { if (left.isMap() && right.isMap())
	  { res = "Ocl.excludeAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Ocl.subtract(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("->excluding"))  
    { res = "Ocl.subtract(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("\\/") || operator.equals("->union")) 
    { if (left.isMap() && right.isMap())
	  { res = "Ocl.unionMap(" + lqf + "," + rqf + ")"; }
	  else if (left.isOrdered() && right.isOrdered())
      { res = "Ocl.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Ocl.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("->including"))
    { if (left.isOrdered())
      { res = "Ocl.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "Ocl.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("/\\") || operator.equals("->intersection"))
    { if (left.isMap() && right.isMap())
	  { res = "Ocl.intersectionMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Ocl.intersection(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("/:") || operator.equals("/<:"))
    { res = "!(" + rqf + ".containsAll(" + lqf + "))"; } 
    else if (operator.equals("->excludesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "Ocl.excludesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "Collections.disjoint(" + lqf + "," + rqf + ")"; }
	}        
    else if (operator.equals("^"))
    { res = "Ocl.concatenate(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->symmetricDifference"))
    { res = "Ocl.symmetricDifference(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->prepend"))
    { res = "Ocl.concatenate(" + rqf + "," + lqf + ")"; } 
    else if (operator.equals("->append"))
    { res = "Ocl.concatenate(" + lqf + "," + rqf + ")"; } 
    return res; 
  } // what about comparitors? 


  private String composeSetQueryFormsCSharp(String lqf, String rqf)
  { String res = lqf + " " + operator + " " + rqf; 
    if (operator.equals("="))
    { if (Type.isSetType(left.getType()) || Type.isSetType(right.getType()))
      { res = "SystemTypes.equalsSet(" + lqf + "," + rqf + ")"; } 
      else 
      { res = lqf + ".Equals(" + rqf + ")"; }  
    } // order shouldn't matter
    else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { if (Type.isSetType(left.getType()) || Type.isSetType(right.getType()))
      { res = "!(SystemTypes.equalsSet(" + lqf + "," + rqf + "))"; } 
      else 
      { res = "!(" + lqf + ".Equals(" + rqf + "))"; }
    } 
    else if (operator.equals(":") || operator.equals("<:")) 
    { res = "SystemTypes.isSubset(" + lqf + "," + rqf + ")"; }
    else if (operator.equals("->includesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "SystemTypes.includesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "SystemTypes.isSubset(" + rqf + "," + lqf + ")"; }
	} 
    else if (operator.equals("-"))
    { if (left.isMap() && right.isMap())
	  { res = "SystemTypes.excludeAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "SystemTypes.subtract(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("->excluding"))  
    { res = "SystemTypes.subtract(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("\\/") || operator.equals("->union")) 
    { if (left.isMap() && right.isMap())
      { res = "SystemTypes.unionMap(" + lqf + "," + rqf + ")"; }
      else if (left.isOrdered())
      { res = "SystemTypes.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "SystemTypes.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("->including"))
    { if (left.isOrdered())
      { res = "SystemTypes.concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "SystemTypes.union(" + lqf + "," + rqf + ")"; }
    } 
    else if (operator.equals("/\\") || operator.equals("->intersection"))
    { if (left.isMap() && right.isMap())
	  { res = "SystemTypes.intersectionMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "SystemTypes.intersection(" + lqf + "," + rqf + ")"; }
	} 
    else if (operator.equals("/:") || operator.equals("/<:"))
    { res = "!(SystemTypes.isSubset(" + lqf + ", " + rqf + "))"; } 
    else if (operator.equals("->excludesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "SystemTypes.excludesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "(SystemTypes.intersection(" + rqf + "," + lqf + ").Count == 0)"; }
	}        
    else if (operator.equals("^"))
    { res = "SystemTypes.concatenate(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->symmetricDifference"))
    { res = "SystemTypes.symmetricDifference(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->prepend"))
    { res = "SystemTypes.concatenate(" + rqf + "," + lqf + ")"; } 
    else if (operator.equals("->append"))
    { res = "SystemTypes.concatenate(" + lqf + "," + rqf + ")"; } 
    return res; 
  } // what about comparitors? 
    
  private String composeSetQueryFormsCPP(String lqf, String rqf, String lcet)
  { String res = lqf + " " + operator + " " + rqf; 
    if (operator.equals("="))
    { // if (Type.isCollectionOrEntity(left.getType()) || Type.isCollectionOrEntity(right.getType()))
      { res = "(*(" + lqf + ") == *(" + rqf + "))"; } 
      // else 
      // { res = "(" + lqf + " == " + rqf + ")"; }
    }  
    else if (operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { // if (Type.isCollectionOrEntity(left.getType()) || Type.isCollectionOrEntity(right.getType()))
      { res = "(*(" + lqf + ") != *(" + rqf + "))"; } 
      // else 
      // { res = "(" + lqf + " != " + rqf + ")"; }
    } 
    else if (operator.equals(":") || operator.equals("<:")) 
    { res = "UmlRsdsLib<" + lcet + ">::isSubset(" + lqf + ", " + rqf + ")"; }
    else if (operator.equals("->includesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "UmlRsdsLib<" + lcet + ">::includesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "UmlRsdsLib<" + lcet + ">::isSubset(" + rqf + ", " + lqf + ")"; }
	} 
    else if (operator.equals("-"))
    { if (left.isMap() && right.isMap())
	  { res = "UmlRsdsLib<" + lcet + ">::excludeAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "UmlRsdsLib<" + lcet + ">::subtract(" + lqf + ", " + rqf + ")"; }
	} 
    else if (operator.equals("->excluding"))
    { // String rss = makeSetCPP(rqf); 
      res = "UmlRsdsLib<" + lcet + ">::subtract(" + lqf + ", " + rqf + ")"; 
    } 
    else if (operator.equals("->including"))
    { // String rss = makeSetCPP(rqf); 
      if (left.isOrdered())
      { res = "UmlRsdsLib<" + lcet + ">::concatenate(" + lqf + ", " + rqf + ")"; } 
      else 
      { res = "UmlRsdsLib<" + lcet + ">::unionSet(" + lqf + ", " + rqf + ")"; } 
    } 
    else if (operator.equals("\\/") || operator.equals("->union"))
    { if (left.isMap() && right.isMap())
	  { res = "UmlRsdsLib<" + lcet + ">::unionMap(" + lqf + "," + rqf + ")"; }
	  else if (left.isOrdered())
      { res = "UmlRsdsLib<" + lcet + ">::concatenate(" + lqf + ", " + rqf + ")"; } 
      else // left is a set
      { res = "UmlRsdsLib<" + lcet + ">::unionSet(" + lqf + ", " + rqf + ")"; }
    } 
    else if (operator.equals("/\\") || operator.equals("->intersection"))
    { if (left.isMap() && right.isMap())
	  { res = "UmlRsdsLib<" + lcet + ">::intersectionMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "UmlRsdsLib<" + lcet + ">::intersection(" + lqf + ", " + rqf + ")"; }
	} 
    else if (operator.equals("/:") || operator.equals("/<:"))
    { res = "!(UmlRsdsLib<" + lcet + ">::isSubset(" + lqf + ", " + rqf + "))"; } 
    else if (operator.equals("->excludesAll"))
    { if (left.isMap() && right.isMap())
	  { res = "UmlRsdsLib<" + lcet + ">::excludesAllMap(" + lqf + "," + rqf + ")"; } 
	  else 
	  { res = "(UmlRsdsLib<" + lcet + ">::intersection(" + rqf + ", " + lqf + ")->size() == 0)"; }
	}        
    else if (operator.equals("^"))
    { res = "UmlRsdsLib<" + lcet + ">::concatenate(" + lqf + ", " + rqf + ")"; } 
    else if (operator.equals("->symmetricDifference"))
    { res = "UmlRsdsLib<" + lcet + ">::symmetricDifference(" + lqf + "," + rqf + ")"; } 
    else if (operator.equals("->prepend"))
    { res = "UmlRsdsLib<" + lcet + ">::prepend(" + lqf + ", " + rqf + ")"; } 
    else if (operator.equals("->append"))
    { res = "UmlRsdsLib<" + lcet + ">::append(" + lqf + ", " + rqf + ")"; } 
    return res; 
  } // what about comparitors? 
    
  public String updateForm(java.util.Map env, boolean local)
  { String val2;
    if (operator.equals("#") || operator.equals("#1"))
    { return updateFormExists(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it
   
    if (operator.equals("#LC"))
    { return updateFormExistsLC(env,local); } 

    if (operator.equals("!"))  // Only version with variable is ok
    { BinaryExpression beleft = (BinaryExpression) left; 
      String lqf1 = beleft.right.queryForm(env,local);
      if (beleft.right.umlkind == CLASSID)
      { lqf1 = ((BasicExpression) beleft.right).classExtentQueryForm(env,local); } 
      String eleftname = ""; 
      String evarname = beleft.left + "";  
      
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      
      Type eleft = beleft.right.getElementType();  // Must be an entity type 
      // System.out.println(">>>>>eleft is>>> " + eleft); 

      String tname = ""; 
      if (eleft == null || "null".equals(eleft + "")) 
      { eleftname = "Object"; 
        tname = "Object"; 
      } 
      else 
      { eleftname = eleft.getName();  
        // System.out.println(">>>>>eleftname is>>> " + eleftname); 
        tname = eleftname;  // entities and String

        if ("int".equals(eleftname))
        { eleftname = "Integer"; 
          tname = "int"; 
        } 
        else if ("long".equals(eleftname))
        { eleftname = "Long"; 
          tname = "long"; 
        } 
        else if ("double".equals(eleftname))
        { eleftname = "Double"; 
          tname = "double"; 
        } 
        else if ("boolean".equals(eleftname))
        { eleftname = "Boolean"; 
          tname = "boolean"; 
        } 
        else if (eleftname.startsWith("Set") || eleftname.startsWith("Sequence"))
        { eleftname = "List"; tname = "List"; } 
        else if (eleft != null && eleft.isEntity())
        { newenv.put(eleftname,evarname); }
      } // not really needed to have  E |-> ex in environment. 

      String ufr1 = right.updateForm(newenv,false);

      String elist = Identifier.nextIdentifier("_" + tname.toLowerCase() + "_list");
      String indvar = Identifier.nextIdentifier("_ind"); 

          
      if ("Integer".equals(eleftname) && (left instanceof BasicExpression))
      { BasicExpression leftbe = (BasicExpression) left; 
        Vector leftpars = leftbe.getParameters(); 
        if (leftpars != null && leftpars.size() >= 2 && "subrange".equals(leftbe.data))
        { Expression startexp = (Expression) leftpars.get(0); 
          Expression endexp = (Expression) leftpars.get(1);
          String startexpqf = startexp.queryForm(env,false); 
          String endexpqf = endexp.queryForm(env,false); 
          return 
            "    for (int " + evarname + " = " + startexpqf + "; " + evarname + " <= " + endexpqf + "; " + evarname + "++)\n" + 
            "    { " + ufr1 + "\n" + 
            "    }\n";
         } 
         else 
         { return  "    List " + elist + " = new Vector();\n" + 
            "    " + elist + ".addAll(" + lqf1 + ");\n" + 
            "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
            "    { int " + evarname + " = ((" + eleftname + ") " + elist + 
                     ".get(" + indvar + ")).intValue();\n" + 
            "      " + ufr1 + "\n" + 
            "    }\n";
         } 
      } 
      else if (eleft != null && Type.isPrimitiveType(eleft))
      { return  "    List " + elist + " = new Vector();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
        "    { " + tname + " " + evarname + 
                   " = ((" + eleftname + ") " + elist + 
                     ".get(" + indvar + "))." + tname + "Value();\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 

      return 
        "    List " + elist + " = new Vector();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
        "    { " + eleftname + " " + evarname + " = (" + eleftname + ") " + elist + 
                     ".get(" + indvar + ");\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
    }    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { String ufl = left.updateForm(env,local);
      String ufr = right.updateForm(env,local);
      return ufl + "\n    " + ufr;
    }
	
    if (operator.equals("let"))
    { // { var acc : T := e; ... E ... }
      String acc = accumulator.getName(); 
      Type acct = accumulator.getType(); 
      String lett = acct.getJava(); 
      String val = left.queryForm(env,local); 
      java.util.Map env1 = new java.util.HashMap(); 
      env1.putAll(env); 
      String stats = right.updateForm(env1,local); 
      return "\n    { final " + lett + " " + acc + " = " + val + ";\n" + stats + "\n    }"; 
    } 

    if (operator.equals("or"))
    { String ufl = left.updateForm(env,local);
      String qfl = left.queryForm(env,local);
      String ufr = right.updateForm(env,local);
      return ufl + "\n    if (" + qfl + ") {}\n    else\n    { " + ufr + " }";
    }

    if (operator.equals("xor"))
    { Expression notleft = negate(left); 
      Expression notright = negate(right);
      Statement stat1 = left.statLC(env,local); 
      Statement stat2 = notright.statLC(env,local); 
      String ufl = stat1.updateForm(env,local,new Vector(), new Vector(), new Vector()) + 
                   "\n    " + stat2.updateForm(env,local,new Vector(), new Vector(), new Vector()); 
  
      String qfl1 = left.queryForm(env,local);
      String qfl2 = notright.queryForm(env,local);
      String ufr1 = right.updateForm(env,local);
      String ufr2 = notleft.updateForm(env,local);
      return ufl + "\n    if (" + qfl1 + " && " + qfl2 + ") {}\n    else\n    { " + ufr1 + 
                   "\n      " + ufr2 + " }";
    }

    if (operator.equals("=>"))
    { String ufl = left.queryForm(env,local); 
      String ufr = right.updateForm(env,local); 
      if ("true".equals(ufl))
      { return ufr; }
      return "  if (" + ufl + ") \n" + 
             "  { " + ufr + "}";
    } // accumulate the cases

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      String lqf = left.queryForm(env,local); 
      String rqf = right.queryForm(env,local); 

      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 

    if (operator.equals("=") && left instanceof BasicExpression)
    { val2 = right.queryForm(env,local);
      return ((BasicExpression) left).updateFormEq(env,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof UnaryExpression)
    { val2 = right.queryForm(env,local);
      return ((UnaryExpression) left).updateFormEq("Java4",env,((UnaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof BinaryExpression)
    { val2 = right.queryForm(env,local);
      return ((BinaryExpression) left).updateFormEq("Java4",env,((BinaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { val2 = right.queryForm(env,local);
      return ((BasicExpression) left).updateForm(env,"<:",val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BinaryExpression)
    { return ((BinaryExpression) left).updateFormSubset("Java4", env, right, local); }
    else if (operator.equals("->includesAll") && left instanceof UnaryExpression)
    { return ((UnaryExpression) left).updateFormSubset("Java4", env, right, local); }
    else if (operator.equals("->includes") && left instanceof BinaryExpression)
    { return ((BinaryExpression) left).updateFormIn("Java4", env, right, local); }
    else if (operator.equals("->includes") && left instanceof UnaryExpression)
    { return ((UnaryExpression) left).updateFormIn("Java4", env, right, local); }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { val2 = right.queryForm(env,local);
      return ((BasicExpression) left).updateForm(env,"-",val2,right,local);
    }
    else if (operator.equals("->excludesAll") && left instanceof UnaryExpression)
    { return ((UnaryExpression) left).updateFormSubtract("Java4", env,right,local); }
    else if (operator.equals("->excludesAll") && left instanceof BinaryExpression)
    { return ((BinaryExpression) left).updateFormSubtract("Java4", env,right,local); }
    else if (operator.equals("->excludes") && left instanceof UnaryExpression)
    { return ((UnaryExpression) left).updateFormNotIn("Java4", env,right,local); }
    else if (operator.equals("->excludes") && left instanceof BinaryExpression)
    { return ((BinaryExpression) left).updateFormNotIn("Java4", env,right,local); }
    else if ((operator.equals(":") || 
              operator.equals("<:") || operator.equals("/<:") ||
              operator.equals("/:")) && (right instanceof BasicExpression))
    { String val1 = left.queryForm(env,local);
      BasicExpression ber = (BasicExpression) right;   // Why cast? 
      /* boolean lmult = left.isMultiple();
      if (lmult)
      { if (operator.equals(":") || operator.equals("<:"))
        { return ber.updateForm(env,"<:",val1,left,local); }
        if (operator.equals("/:") || operator.equals("/<:"))
        { return ber.updateForm(env,"-",val1,left,local); }
      } */ 
      if (left.isPrimitive())
      { val1 = left.wrap(val1); }
      return ber.updateForm(env,operator,val1,left,local);
    }
    else if ((operator.equals("->includes") || 
              operator.equals("->excludes")) && (left instanceof BasicExpression))
    { String val3 = right.queryForm(env,local);
      BasicExpression bel = (BasicExpression) left;
      /* boolean rmult = right.isMultiple();
      if (rmult)
      { if (operator.equals("->includes"))
        { return bel.updateForm(env,"<:",val3,right,local); }
        if (operator.equals("->excludes"))
        { return bel.updateForm(env,"-",val3,right,local); }
      } */ 

      if (right.isPrimitive())
      { val3 = right.wrap(val3); }
      if (operator.equals("->includes"))
      { return bel.updateForm(env,":",val3,right,local); }
      else 
      { return bel.updateForm(env,"/:",val3,right,local); }
    }
    else
    { String qf = queryForm(env,local); 
	  return "if (" + qf + ") { } else { System.err.println(\"Assertion " + this + " fails\"); }\n";  
	}
  }

public String updateFormIn(String language, java.util.Map env, Expression var, boolean local)
{ if ("->union".equals(operator))
  { BinaryExpression test1 = new BinaryExpression("->includes", left, var);
    BinaryExpression test2 = new BinaryExpression("->includes", right, var);
    BinaryExpression test = new BinaryExpression("or", test1, test2);
    BinaryExpression add2a = new BinaryExpression(":", var, left);
    BinaryExpression add2b = new BinaryExpression(":", var, right);
    String addleft = add2a.updateForm(language,env,local);
    String addright = add2b.updateForm(language,env,local);
    String testfull = addleft;
    if (left instanceof BasicExpression && ((BasicExpression) left).isZeroOneRole())
    { Expression test3 = new UnaryExpression("->size", left);
      Expression atcap = new BinaryExpression("=", test3, new BasicExpression(0));
      testfull = "if (" + atcap.queryForm(language, env, local) + ") { " + addright + " }\n " +
        "    else { " + addleft + " } "; 
    }
    return "  if (" + test.queryForm(language, env, local) + ") { }\n" +
      "  else " + testfull;
  } 
  else if ("->intersection".equals(operator))
  { Expression e1 = new BinaryExpression(":", var, left);
    Expression e2 = new BinaryExpression(":", var, right);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("^".equals(operator))
  { BinaryExpression test1 = new BinaryExpression("->includes", left, var);
    BinaryExpression test2 = new BinaryExpression("->includes", right, var);
    BinaryExpression test = new BinaryExpression("or", test1, test2);
    Expression addb = new BinaryExpression(":", var, right);
    return "  if (" + test.queryForm(language,env,local) + ") { }\n" +
      "  else { " + addb.updateForm(language,env,local) + " } ";
  }
  else if ("-".equals(operator))
  { Expression e1 = new BinaryExpression(":", var, left);
    Expression e2 = new BinaryExpression("/:", var, right);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->sortedBy".equals(operator))
  { Expression e1 = new BinaryExpression(":", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->select".equals(operator) && (var instanceof BasicExpression))
  { Expression e1 = new BinaryExpression(":", var, left);
    Expression e2 = right.addReference((BasicExpression) var, left.elementType);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->reject".equals(operator) && (var instanceof BasicExpression))
  { Expression e1 = new BinaryExpression(":", var, left);
    Expression e2 = right.addReference((BasicExpression) var, left.elementType);
    Expression e3 = Expression.negate(e2);
    Expression e = new BinaryExpression("&", e1, e3);
    return e.updateForm(language,env,local);
  }
  else if ("->excluding".equals(operator))
  { Expression test = new BinaryExpression("=", var, right);
    Expression adda = new BinaryExpression(":", var, left);
    return "  if (" + test.queryForm(language, env, local) + ") { }\n" +
      "  else { " + adda.updateForm(language, env, local) + " }";
  }
  else if ("->including".equals(operator))
  { Expression test = new BinaryExpression("=", var, right);
    Expression adda = new BinaryExpression(":", var, left);
    return "  if (" + test.queryForm(language, env, local) + ") { }\n" +
      "  else { " + adda.updateForm(language, env, local) + " }";
  }
  else
  { String qf = queryForm(env,local); 
    return "if (" + qf + ") { } else { System.err.println(\"Assertion " + this + " fails\"); }\n";  
  }
}

public String updateFormNotIn(String language, java.util.Map env, Expression var, boolean local)
{ if ("->union".equals(operator) || "^".equals(operator))
  { BinaryExpression test1 = new BinaryExpression("/:", var,  left);
    BinaryExpression test2 = new BinaryExpression("/:", var, right);
    BinaryExpression e = new BinaryExpression("&", test1, test2);
    return e.updateForm(language, env, local);  
  } 
  else if ("->intersection".equals(operator))
  { Expression test = new BinaryExpression(":", var, left);
    Expression rema = new BinaryExpression("/:", var, left);
    Expression remb = new BinaryExpression("/:", var, right);
    return "  if (" + test.queryForm(language, env, local) + ") { " + rema.updateForm(language, env, local) + " } \n" +
  "  else { " + remb.updateForm(language, env, local) + " }";
  } 
  else if ("-".equals(operator))
  { Expression e1 = new BinaryExpression("/:", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->collect".equals(operator))
  { Expression e1 = new BinaryExpression("=", var, right);
    Expression e2 = new BinaryExpression("->reject", left, e1);
    Expression e = new BinaryExpression("=", left, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->sortedBy".equals(operator))
  { Expression e1 = new BinaryExpression("/:", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->select".equals(operator) && (var instanceof BasicExpression))
  { Expression e1 = new BinaryExpression("/:", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->reject".equals(operator) && (var instanceof BasicExpression))
  { Expression e1 = new BinaryExpression("/:", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->including".equals(operator) || "->excluding".equals(operator))
  { Expression test = new BinaryExpression("=", var, right);
    Expression adda = new BinaryExpression("/:", var, left);
    return "  if (" + test.queryForm(language, env, local) + ") { }\n" +
      "  else { " + adda.updateForm(language, env, local) + " }";
  }
  return "/* No update form for " + var + " : " + this + " */";
}

public String updateFormSubset(String language, java.util.Map env, Expression var, boolean local)
{ if ("->union".equals(operator))
  { BinaryExpression test = new BinaryExpression("->includesAll", this, var);
    BinaryExpression add2a = new BinaryExpression("<:", var, left);
    BinaryExpression add2b = new BinaryExpression("<:", var, right);
    String addleft = add2a.updateForm(language,env,local);
    String addright = add2b.updateForm(language,env,local);
    String testfull = addleft;
    if (left instanceof BasicExpression && ((BasicExpression) left).isZeroOneRole())
    { testfull = addright; }
    return "  if (" + test.queryForm(language, env, local) + ") { }\n" +
      "  else { " + testfull + " } ";
  } 
  else if ("->intersection".equals(operator))
  { Expression e1 = new BinaryExpression("<:", var, left);
    Expression e2 = new BinaryExpression("<:", var, right);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("^".equals(operator))
  { BinaryExpression test = new BinaryExpression("->includes", this, var);
    Expression addb = new BinaryExpression("<:", var, right);
    return "  if (" + test.queryForm(language,env,local) + ") { }\n" +
      "  else { " + addb.updateForm(language,env,local) + " } ";
  }
  else if ("-".equals(operator))
  { Expression e1 = new BinaryExpression("<:", var, left);
    Expression e2 = new BinaryExpression("->excludesAll", right, var);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->sortedBy".equals(operator))
  { Expression e1 = new BinaryExpression("<:", var, left);
    return e1.updateForm(language,env,local);
  }
  else if ("->select".equals(operator))
  { Expression e1 = new BinaryExpression("<:", var, left);
    Expression e2 = new BinaryExpression("->forAll", var, right);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->reject".equals(operator))
  { Expression e1 = new BinaryExpression("<:", var, left);
    Expression e3 = Expression.negate(right);
    Expression e2 = new BinaryExpression("->forAll", var, e3);
    Expression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->excluding".equals(operator) || "->including".equals(operator))
  { Expression subb = new BinaryExpression("->excluding", var, right);
    Expression adda = new BinaryExpression("<:", subb, left);
    return adda.updateForm(language, env, local);
  }
  return "/* No update form for " + var + " <: " + this + " */";
}

public String updateFormSubtract(String language, java.util.Map env, Expression var, boolean local)
{ if ("->union".equals(operator) || "^".equals(operator))
  { BinaryExpression e1 = new BinaryExpression("->excludesAll", left, var);
    BinaryExpression e2 = new BinaryExpression("->excludesAll", right, var);
    BinaryExpression e = new BinaryExpression("&", e1, e2);
    return e.updateForm(language, env, local);  
  } 
  else if ("->intersection".equals(operator))
  { Expression inter = new BinaryExpression("->intersection", var, right);
    inter.setBrackets(true);
    Expression rema = new BinaryExpression("-", left, inter);
    Expression remb = new BinaryExpression("=", left, rema);
    return rema.updateForm(language, env, local);
  } 
  else if ("-".equals(operator))
  { Expression e1 = new BinaryExpression("-", var, right);
    e1.setBrackets(true);
    Expression e2 = new BinaryExpression("-", left, e1);
    Expression e3 = new BinaryExpression("=", left, e2);
    return e3.updateForm(language,env,local);
  }
  else if ("->collect".equals(operator))
  { Expression e1 = new BinaryExpression(":", right, var);
    Expression e2 = new BinaryExpression("->reject", left, e1);
    Expression e = new BinaryExpression("=", left, e2);
    return e.updateForm(language,env,local);
  }
  else if ("->sortedBy".equals(operator))
  { Expression e1 = new BinaryExpression("->excludesAll", left, var);
    return e1.updateForm(language,env,local);
  }
  else if ("->select".equals(operator))
  { Expression selfv = new BasicExpression("self", 0);
    Expression e1 = new BinaryExpression(":", selfv, var);
    Expression pred = new BinaryExpression("&", e1, right);
    Expression rej = new BinaryExpression("->reject", left, pred);
    Expression assign = new BinaryExpression("=", left, rej);
   
   return assign.updateForm(language,env,local);
  }
  else if ("->reject".equals(operator))
  { Expression selfv = new BasicExpression("self", 0);
    Expression e1 = new BinaryExpression(":", selfv, var);
    Expression nright = Expression.negate(right);
    Expression pred = new BinaryExpression("&", e1, nright);
    Expression rej = new BinaryExpression("->reject", left, pred);
    Expression assign = new BinaryExpression("=", left, rej);
   return assign.updateForm(language,env,local);
  }
  else if ("->including".equals(operator))
  { Expression rema = new BinaryExpression("->excludesAll", left, var);
    return rema.updateForm(language, env, local);
  }
  else if ("->excluding".equals(operator))
  { Expression remb = new BinaryExpression("->excluding", var, right);
    remb.setBrackets(true);
    Expression suba = new BinaryExpression("-", left, remb);
    Expression assign = new BinaryExpression("=", left, suba);
    return assign.updateForm(language, env, local);
  }
  return "/* No update form for " + this + "->excludesAll(" + var + ") */";
}

  public boolean isTestable() 
  { if ("result".equals("" + left)) 
    { return false; } 

    if ("result".equals("" + right)) 
    { return false; } 
    return true;
  } 

  public boolean isExecutable()
  { if ("#LC".equals(operator) || operator.equals("#") || operator.equals("#1"))
    { return left.isExecutable() && right.isExecutable(); } // should be or

    if (operator.equals("!") || operator.equals("=>"))  // Only version with variable is ok
    { return right.isExecutable(); } 

    if (operator.equals("&"))
    { return left.isExecutable() || right.isExecutable(); } 

    if (operator.equals("or"))
    { return left.isExecutable() || right.isExecutable(); } 

    if (operator.equals("=") && left instanceof BasicExpression)
    { return ((BasicExpression) left).isAssignable(); }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { return ((BasicExpression) left).isAssignable(); }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { return ((BasicExpression) left).isAssignable(); }
    else if (operator.equals(":") && right instanceof BasicExpression) 
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.isAssignable();
    }
    else if (operator.equals("<:") && right instanceof BasicExpression) 
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.isAssignable();
    }
    else if (operator.equals("/<:") && right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.isAssignable();
    }
    else if (operator.equals("/:") && right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.isAssignable();
    }
    else if (operator.equals("->includes") && left instanceof BasicExpression) 
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.isAssignable();
    }
    else if (operator.equals("->excludes") && left instanceof BasicExpression) 
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.isAssignable();
    }

    return false; 
  } 

  public String updateFormExistsLC(java.util.Map env, boolean local)
  { Statement stat = statLC(env,local); 
    if (stat != null) 
    { return stat.updateForm(env,local,new Vector(), new Vector(), new Vector()); } 
    else 
    { return "/* Invalid expression: " + this + " */"; } 
  }  
 
  public String updateFormExistsLCJava6(java.util.Map env, boolean local)
  { Statement stat = statLC(env,local); 
    if (stat != null) 
    { return stat.updateFormJava6(env,local); } 
    else 
    { return "/* Invalid expression: " + this + " */"; } 
  }  

  public String updateFormExistsLCJava7(java.util.Map env, boolean local)
  { Statement stat = statLC(env,local); 
    if (stat != null) 
    { return stat.updateFormJava7(env,local); } 
    else 
    { return "/* Invalid expression: " + this + " */"; } 
  }  

  public String updateFormExistsLCCSharp(java.util.Map env, boolean local)
  { Statement stat = statLC(env,local); 
    if (stat != null) 
    { return stat.updateFormCSharp(env,local); } 
    else 
    { return "/* Invalid expression: " + this + " */"; } 
  }  

  public String updateFormExistsLCCPP(java.util.Map env, boolean local)
  { Statement stat = statLC(env,local); 
    if (stat != null) 
    { return stat.updateFormCPP(env,local); } 
    else 
    { return "/* Invalid expression: " + this + " */"; } 
  }  

public Statement statLC(java.util.Map env, boolean local)
{ String val2;
    
  if (operator.equals("#") || operator.equals("#1"))
  { return generateDesignExists(env,local);  } 

  if (operator.equals("#LC"))
  { return statLCExists(env,local); }

  if (operator.equals("or"))
  { SequenceStatement skip = new SequenceStatement();
    if (left.isExecutable())
    { Statement leftdesign = left.statLC(env,local);
      if (right.isExecutable())
      { Statement rightdesign = right.statLC(env,local); 
        SequenceStatement res = new SequenceStatement(); 
        res.addStatement(leftdesign); 
        res.addStatement(new ConditionalStatement(this, skip, rightdesign)); 
        return res; 
      } 
      return new ConditionalStatement(this, skip, leftdesign); 
    }
    else
    { Statement rightdesign = right.statLC(env,local); 
      return new ConditionalStatement(this, skip, rightdesign); 
    }
  }

  if (operator.equals("!"))
  { Statement body = right.statLC(env,local);
    BinaryExpression bel = (BinaryExpression) left;
    return iterationLoop(bel.left, bel.right, body);
  }

  if (operator.equals("&"))
  { Statement ufl = left.statLC(env,local); 
    Statement ufr = right.statLC(env,local);
    if (ufl instanceof SequenceStatement)
    { ((SequenceStatement) ufl).addStatement(ufr);
      return ufl;
    }
    SequenceStatement sstat = new SequenceStatement();
    sstat.addStatement(ufl); 
    sstat.addStatement(ufr);
    return sstat;
  }

  if (operator.equals("=>"))
  { Statement ufr = right.statLC(env,local);
    if ("true".equals(left + ""))
    { return ufr; }
    ConditionalStatement istat = new ConditionalStatement(left,ufr);
    return istat;
  }

  if (operator.equals("=") && (left instanceof BasicExpression))
  { return new AssignStatement(left,right); }

  if (left instanceof BasicExpression)
  { BasicExpression lbe = (BasicExpression) left;
    if (operator.equals("->includesAll"))
    { Statement upd = lbe.generateDesignSubset(right);
      if (lbe.isOrdered())
      { Statement skip = new SequenceStatement();
        upd.setBrackets(true);
        return new ConditionalStatement(this, skip, upd);
      }
      return upd;
    } // but ->forAll in fact
    else if (operator.equals("->excludesAll"))
    { Statement upd = lbe.generateDesignSubtract(right);
      Statement skip = new SequenceStatement();
      upd.setBrackets(true);
      return new ConditionalStatement(this, skip, upd);
    }
    else if (operator.equals("->includes"))
    { Statement upd = lbe.generateDesignIn(right);
      if (lbe.isOrdered())
      { Statement skip = new SequenceStatement();
        upd.setBrackets(true);
        return new ConditionalStatement(this, skip, upd);
      }
      return upd;
    }
    else if (operator.equals("->excludes"))
    { Statement upd = lbe.generateDesignNotIn(right);
      Statement skip = new SequenceStatement();
      upd.setBrackets(true);
      return new ConditionalStatement(this, skip, upd);
    }
  }

  if (right instanceof BasicExpression)
  { BasicExpression rbe = (BasicExpression) right;
    if (operator.equals("<:"))
    { Statement upd = rbe.generateDesignSubset(left);
      BinaryExpression test = new BinaryExpression("->includesAll",right,left);
      test.setType(type);
      test.setElementType(elementType);
      if (rbe.isOrdered())
      { Statement skip = new SequenceStatement();
        upd.setBrackets(true);
        return new ConditionalStatement(test, skip, upd);
      }
      return upd;
    } // but ->forAll in fact
    else if (operator.equals("/<:"))
    { Statement upd = rbe.generateDesignSubtract(left);
      Statement skip = new SequenceStatement();
      BinaryExpression test = new BinaryExpression("->excludesAll",right,left);
      test.setType(type);
      test.setElementType(elementType);
      upd.setBrackets(true);
      return new ConditionalStatement(test, skip, upd);
    }
    else if (operator.equals(":"))
    { Statement upd = rbe.generateDesignIn(left);
      BinaryExpression test = new BinaryExpression("->includes",right,left);
      test.setType(type);
      test.setElementType(elementType);

      if (rbe.isOrdered())
      { Statement skip = new SequenceStatement();
        upd.setBrackets(true);
        return new ConditionalStatement(test, skip, upd);
      }
      return upd;
    }
    else if (operator.equals("/:"))
    { Statement upd = rbe.generateDesignNotIn(left);
      BinaryExpression test = new BinaryExpression("->excludes",right,left);
      test.setType(type);
      test.setElementType(elementType);
      Statement skip = new SequenceStatement();
      upd.setBrackets(true);
      return new ConditionalStatement(test, skip, upd);
    }
  }

  return new InvocationStatement("/* No LC update form for : " + this + " */");
}

public Statement statLCExists(java.util.Map env, boolean local)
{ BinaryExpression lexp = (BinaryExpression) left;
  Expression lvar = lexp.left;

  Expression evar = (Expression) lvar.clone(); 
  evar.umlkind = VARIABLE; 

  Statement res = new InvocationStatement("/* No LC update form for : " + this + " */");

  Expression ltype = lexp.right;
  Type etype = ltype.getElementType();
  if (etype == null)
  { return res; }
  Entity ent = etype.getEntity();
  if (ent == null)
  { return res; }
  evar.setType(new Type(ent)); 

  // Case of non-writable ltype
  // is var evar : ltype; stat(right)

  Vector preds = right.getConjuncts();
  
  System.out.println(">->-> Conjuncts of " + right + " are: " + preds); 

  String ename = ent.getName();
  Expression eset = new BasicExpression(ename.toLowerCase() + "$set");
  eset.umlkind = VARIABLE; 
  eset.setType(new Type("Set",null));
  eset.setElementType(etype);

  Expression etest = new UnaryExpression("->isEmpty", eset);
  Expression eany = new UnaryExpression("->any", eset);
  Expression eexp = new BasicExpression(ent); 
  BasicExpression einstances = new BasicExpression("allInstances"); 
  einstances.setObjectRef(eexp); 
  einstances.umlkind = FUNCTION; 
  einstances.setType(new Type("Set", null)); 
  einstances.setElementType(etype); 

  Statement assigneset = new AssignStatement(eset, einstances); 
  Statement eassign = new AssignStatement(evar, eany);
  Vector remainder = new Vector();

  Attribute pk = Expression.hasPrimaryKey(ltype);
  if (pk != null)
  { Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
    if (keyval != null)
    { String kvqf = keyval.queryForm(env,local);
      BasicExpression invokecreent = new BasicExpression("createByPK" + ename);
      invokecreent.umlkind = UPDATEOP;
      invokecreent.type = new Type(ent);
      invokecreent.elementType = new Type(ent);
      invokecreent.addParameter(keyval);
      AssignStatement assgn = new AssignStatement(evar,invokecreent);
      assgn.setType(etype);
      if (ltype.umlkind == CLASSID)
      { if (remainder.size() > 0)
        { Expression rem = (Expression) remainder.get(0);
          Statement remstat = rem.statLC(env,local);
          SequenceStatement sts = new SequenceStatement();
          sts.addStatement(assgn);
          sts.addStatement(remstat);
          return sts;
        }
        else // no remainder
        { return assgn; }
      }
      else // ltype not class
      { Statement add2ltype = left.statLC(env,local);
        SequenceStatement sts = new SequenceStatement();
        sts.addStatement(assgn);
        sts.addStatement(add2ltype); 
        if (remainder.size() > 0)
        { Expression rem = (Expression) remainder.get(0);
          Statement remstat = rem.statLC(env,local);
          sts.addStatement(remstat);
          return sts;
        }
        else // no remainder
        { return sts; }
      }
    } 
    else // no keyval
    { if (preds.size() > 0)
      { Expression p1 = (Expression) preds.get(0);
        BinaryExpression erange = new BinaryExpression(":", evar, eset); 
        erange.setType(new Type("Set",null));
        erange.setElementType(etype);
        Expression selp1 = new BinaryExpression("|", erange, p1);
        Statement aselp1 = new AssignStatement(eset,selp1);
        SequenceStatement sts = new SequenceStatement();
        CreationStatement createeset = new CreationStatement("Set(" + ename + ")", "" + eset);
        createeset.setType(new Type("Set", null));
        createeset.setElementType(etype);
        CreationStatement ini = new CreationStatement("" + ename, "" + evar);
        ini.setType(etype);
        ini.setElementType(etype);
        sts.addStatement(createeset);
        sts.addStatement(assigneset); 
        sts.addStatement(ini);
        sts.addStatement(aselp1);
        BasicExpression invokecreent1 = new BasicExpression("create" + ename);
        invokecreent1.umlkind = UPDATEOP;
        invokecreent1.type = new Type(ent);
        invokecreent1.elementType = new Type(ent);
        AssignStatement assgn1 = new AssignStatement(evar,invokecreent1);
         // assgn1.setType(etype);
        Statement ufr = right.statLC(env,local);
        SequenceStatement ifcase = new SequenceStatement();
        ifcase.addStatement(assgn1);
        ifcase.addStatement(ufr);
        Vector prest = new Vector();
        prest.addAll(preds);
        prest.remove(0);
        Statement elsecase = existsLC(prest,eset,etest,evar,eassign,env,local);
        ConditionalStatement cs = new ConditionalStatement(etest,ifcase,elsecase);
        sts.addStatement(cs);
        return sts;
      }
    }
  }  
  else // no key 
  { if (preds.size() > 0)
    { Expression p1 = (Expression) preds.get(0);
      BinaryExpression erange = new BinaryExpression(":", evar, eset); 
      erange.setType(new Type("Set",null));
      erange.setElementType(etype);
      Expression selp1 = new BinaryExpression("|", erange, p1);
      Statement aselp1 = new AssignStatement(eset,selp1);
      SequenceStatement sts = new SequenceStatement();
      CreationStatement createeset = new CreationStatement("Set(" + ename + ")", "" + eset);
      createeset.setType(new Type("Set", null));
      createeset.setElementType(etype);
      CreationStatement ini = new CreationStatement("" + ename, "" + evar);
      ini.declarationOnly = true; 
      ini.setType(etype);
      ini.setElementType(etype);
      sts.addStatement(createeset);
      sts.addStatement(assigneset); 
      sts.addStatement(ini);
      sts.addStatement(aselp1);
      BasicExpression invokecreent1 = new BasicExpression("create" + ename);
      invokecreent1.umlkind = UPDATEOP;
      invokecreent1.type = new Type(ent);
      invokecreent1.elementType = new Type(ent);
      AssignStatement assgn1 = new AssignStatement(evar,invokecreent1);
         // assgn1.setType(etype);
      Statement ufr = right.statLC(env,local);
      SequenceStatement ifcase = new SequenceStatement();
      ifcase.addStatement(assgn1);
      ifcase.addStatement(ufr);
      Vector prest = new Vector();
      prest.addAll(preds);
      prest.remove(0);
      Statement elsecase = existsLC(prest,eset,etest,evar,eassign,env,local);
      ConditionalStatement cs = new ConditionalStatement(etest,ifcase,elsecase);
      sts.addStatement(cs);
      return sts;
    }
  } 
  return res; 
}

public Statement existsLC(Vector preds, Expression eset, Expression etest, 
                          Expression evar, Statement eassign, java.util.Map env, boolean local)
{ BinaryExpression erange = new BinaryExpression(":", evar, eset); 
  erange.setType(eset.getType()); 
  erange.setElementType(eset.getElementType()); 

  if (preds.size() == 1)
  { Expression pk = (Expression) preds.get(0); 
    Statement pkstat = pk.statLC(env,local);
     
    if (pk.isTestable())
    { Expression selpk = new BinaryExpression("|", erange, pk);
      Statement stat2 = new AssignStatement(eset, selpk);
      SequenceStatement res = new SequenceStatement();
      res.addStatement(eassign);
      res.addStatement(stat2);
      Statement ifst = new ConditionalStatement(etest, pkstat, eassign);
      res.addStatement(ifst);
      res.setBrackets(true);
      return res;
    }
    else
    { SequenceStatement r2 = new SequenceStatement();
      r2.addStatement(eassign);
      r2.addStatement(pkstat);
      r2.setBrackets(true);
      return r2;
    }
  }
  else if (preds.size() > 0) 
  { Expression pj = (Expression) preds.get(0);
    Vector prem = new Vector();
    prem.addAll(preds);
    prem.remove(0);
    Statement remstat = existsLC(prem,eset,etest,evar,eassign,env,local);
    if (pj.isTestable())
    { Expression selpj = new BinaryExpression("|", erange, pj);
      Statement stat3 = new AssignStatement(eset, selpj);
      SequenceStatement resg = new SequenceStatement();
      resg.addStatement(eassign);
      resg.addStatement(stat3);
      Statement doallpred = SequenceStatement.statLC(preds,env,local);
      ConditionalStatement cs = new ConditionalStatement(etest, doallpred, remstat);
      resg.addStatement(cs);
      resg.setBrackets(true);
      return resg;
    }
    else
    { // just do pj and carry on
      Statement pjstat = pj.statLC(env,local);
      SequenceStatement rr = new SequenceStatement();
      rr.addStatement(eassign);
      rr.addStatement(pjstat);
      rr.addStatement(remstat);
      rr.setBrackets(true);
      return rr;
    }
  }
  return new SequenceStatement(); // preds.size() == 0 
}


  public Statement generateDesign(java.util.Map env, boolean local)
  { String val2;
    if (operator.equals("#") || operator.equals("#1"))
    { return generateDesignExists(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it

    if (operator.equals("#LC"))
    { return statLC(env,local); } 
   
    if (operator.equals("!"))  // Only version with variable is ok
    { Statement body = right.generateDesign(env,local); 
      return iterationLoop(((BinaryExpression) left).left, ((BinaryExpression) left).right, body); 
    }    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { Statement ufl = left.generateDesign(env,local);
      Statement ufr = right.generateDesign(env,local);
      SequenceStatement sstat = new SequenceStatement(); 
      sstat.addStatement(ufl); sstat.addStatement(ufr); 
      return sstat; 
    }

    if (operator.equals("or"))
    { Statement ufl = left.generateDesign(env,local);
      Statement ufr = right.generateDesign(env,local);
      SequenceStatement sstat = new SequenceStatement(); 
      sstat.addStatement(ufl);
      SequenceStatement skip = new SequenceStatement();  
      ConditionalStatement cs = new ConditionalStatement(left,skip,ufr); 
      sstat.addStatement(cs); 
      return sstat; 
    }
	
	// And for xor

    if (operator.equals("let") && accumulator != null)
    { // let accumulator = left in right
	  SequenceStatement sstat = new SequenceStatement();
	  Type typ1 = accumulator.getType();   
	  String varname = accumulator.getName(); 
      CreationStatement cs1 = new CreationStatement(typ1 + "",varname); 
      cs1.setType(typ1); 
      cs1.setElementType(typ1.getElementType());  
      cs1.setInitialisation(left);
	  sstat.addStatement(cs1); 
      Statement ufr = right.generateDesign(env,local); 
      sstat.addStatement(ufr); 
	  return sstat; 
    } 

    if (operator.equals("=>"))
    { Statement ufr = right.generateDesign(env,local); 
      if ("true".equals("" + left))
      { return ufr; }
      ConditionalStatement istat = new ConditionalStatement(left, ufr); 
      return istat; 
    } // accumulate the cases

    if (operator.equals("=") && left instanceof BasicExpression)
    { return new AssignStatement(left, right); }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.generateDesignSubset(right);
    }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.generateDesignSubtract(right);
    }
    else if (operator.equals(":") && right instanceof BasicExpression) 
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.generateDesignIn(left);
    }
    else if (operator.equals("<:") && right instanceof BasicExpression) 
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.generateDesignSubset(left);
    }
    else if (operator.equals("/<:") && right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.generateDesignSubtract(left);
    }
    else if (operator.equals("/:") && right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      return rbe.generateDesignNotIn(left);
    }
    else if (operator.equals("->includes") && left instanceof BasicExpression) 
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.generateDesignIn(right);
    }
    else if (operator.equals("->excludes") && left instanceof BasicExpression) 
    { BasicExpression lbe = (BasicExpression) left;
      return lbe.generateDesignNotIn(right);
    }

    return new ImplicitInvocationStatement(this);
  } 

  public String updateFormEq(String language, java.util.Map env, String op, 
                           String val, Expression var, boolean local)
  { // update   this = var  to this, val is
    // query form of var. 

   if ("->at".equals(operator))
   { // left->at(right) = val; left evaluates to a sequence or map
     return BasicExpression.updateFormEqIndex(language,left,right,val,var,env,local); 
   }  

   if ("->select".equals(operator))
   { // (left - (left->select(right) - var))->union(var->select(right))
     BinaryExpression se = (BinaryExpression) clone();
     BinaryExpression subfg = new BinaryExpression("-", se, var );
     subfg.setBrackets(true);
     BinaryExpression leftminus = new BinaryExpression("-", left, subfg); 
     BinaryExpression selectg = new BinaryExpression("->select", var, right);
     BinaryExpression uniexp = new BinaryExpression("->union", leftminus, selectg); 
     BinaryExpression assign = new BinaryExpression("=", left, uniexp); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->reject".equals(operator))
   { // (left - var)->select(right)->union(var->reject(right))
     BinaryExpression subfg = new BinaryExpression("-", left, var );
     subfg.setBrackets(true);
     BinaryExpression se = new BinaryExpression("->select", subfg, right);
     BinaryExpression rejectg = new BinaryExpression("->reject", var, right);
     BinaryExpression uniexp = new BinaryExpression("->union", se, rejectg); 
     BinaryExpression assign = new BinaryExpression("=", left, uniexp); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->including".equals(operator) && Type.isSetType(type))
   { // left->intersection(var)->union(var - { right })
     BinaryExpression subfg = new BinaryExpression("->intersection", left, var );
     SetExpression se = new SetExpression();
     se.addElement(right);
     BinaryExpression mins = new BinaryExpression("-", var, se); 
     BinaryExpression uniexp = new BinaryExpression("->union", subfg, mins);
     BinaryExpression assign = new BinaryExpression("=", left, uniexp); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->union".equals(operator) && Type.isSetType(type))
   { // left->intersection(var)->union(var - right)
     BinaryExpression subfg = new BinaryExpression("->intersection", left, var );
     BinaryExpression mins = new BinaryExpression("-", var, right); 
     BinaryExpression uniexp = new BinaryExpression("->union", subfg, mins);
     BinaryExpression assign = new BinaryExpression("=", left, uniexp); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->excluding".equals(operator))
   { // left->intersection(var->including(right))->union(var)
     BinaryExpression var1 = new BinaryExpression("->including", var, right );

     BinaryExpression subfg = new BinaryExpression("->intersection", left, var1 );
     BinaryExpression uniong = new BinaryExpression("->union", subfg, var);
     BinaryExpression assign = new BinaryExpression("=", left, uniong); 
     return assign.updateForm(language,env,local);
   }  
   else if ("-".equals(operator) && Type.isCollectionType(type))
   { // left->intersection(var->union(right))->union(var)
     BinaryExpression var1 = new BinaryExpression("->union", var, right );

     BinaryExpression subfg = new BinaryExpression("->intersection", left, var1 );
     BinaryExpression uniong = new BinaryExpression("->union", subfg, var);
     BinaryExpression assign = new BinaryExpression("=", left, uniong); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->intersection".equals(operator) || "/\\".equals(operator))
   { // (left - (right - var))->union(var)
     BinaryExpression var1 = new BinaryExpression("-", right, var);
     var1.setBrackets(true);
     BinaryExpression subfg = new BinaryExpression("-", left, var1 );
     subfg.setBrackets(true); 
     BinaryExpression uniong = new BinaryExpression("->union", subfg, var);
     BinaryExpression assign = new BinaryExpression("=", left, uniong); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->append".equals(operator) ||
      ("->including".equals(operator) && Type.isSequenceType(type)))
   { // left := val->front()
     UnaryExpression frg = new UnaryExpression("->front", var );
     BinaryExpression assign = new BinaryExpression("=", left, frg); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->prepend".equals(operator))
   { // left := val->tail()
     UnaryExpression frg = new UnaryExpression("->tail", var );
     BinaryExpression assign = new BinaryExpression("=", left, frg); 
     return assign.updateForm(language,env,local);
   }  
   else if ("^".equals(operator) ||
         ("->union".equals(operator) && Type.isSequenceType(type)))
   { // left := var.subrange(1, var.size - right.size)
     UnaryExpression vsize = new UnaryExpression("->size", var );
     UnaryExpression gsize = new UnaryExpression("->size", right );
     BinaryExpression sdiff = new BinaryExpression("-", vsize, gsize);
     BasicExpression subrg = new BasicExpression("subrange",0);
     subrg.setObjectRef(var);
     subrg.addParameter(new BasicExpression(1));
     subrg.addParameter(sdiff); 

     BinaryExpression assign = new BinaryExpression("=", left, subrg); 
     return assign.updateForm(language,env,local);
   }  
   else if ("->pow".equals(operator))
   { BinaryExpression dv = new BinaryExpression("/", new BasicExpression(1.0), right);
     BinaryExpression newpow = new BinaryExpression("->pow", var, dv);
     BinaryExpression assign = new BinaryExpression("=", left, newpow);
     return assign.updateForm(language, env, local);
   }
   else if ("+".equals(operator) && Type.isNumericType(type))
   { // left = val - right
     BinaryExpression newright = new BinaryExpression("-", var, right); 
     BinaryExpression assign = new BinaryExpression("=", left, newright);
     return assign.updateForm(language, env, local);
   }
   else if ("-".equals(operator) && Type.isNumericType(type))
   { // left = val + right
     BinaryExpression newright = new BinaryExpression("+", var, right); 
     BinaryExpression assign = new BinaryExpression("=", left, newright);
     return assign.updateForm(language, env, local);
   }
   else if ("*".equals(operator) && Type.isNumericType(type))
   { // left = val / right
     BinaryExpression newright = new BinaryExpression("/", var, right); 
     BinaryExpression assign = new BinaryExpression("=", left, newright);
     return assign.updateForm(language, env, local);
   }
   else if ("/".equals(operator) && Type.isNumericType(type))
   { // left = val * right
     BinaryExpression newright = new BinaryExpression("*", var, right); 
     BinaryExpression assign = new BinaryExpression("=", left, newright);
     return assign.updateForm(language, env, local);
   }

   return "/* No update form for: " + this + " = " + var + " */\n"; 
 }

  public String updateFormJava6(java.util.Map env, boolean local)
  { String val2;

    if (operator.equals("#") || operator.equals("#1"))
    { return updateFormExistsJava6(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it

    if (operator.equals("#LC"))
    { return updateFormExistsLCJava6(env,local); } 
   
    if (operator.equals("!"))  // Only version with variable is ok
    { BinaryExpression beleft = (BinaryExpression) left; 
      String lqf1 = beleft.right.queryFormJava6(env,local);
      if (beleft.right.umlkind == CLASSID)
      { lqf1 = ((BasicExpression) beleft.right).classExtentQueryFormJava6(env,local); } 
      String eleftname = ""; 
      String evarname = beleft.left + "";  
      
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      String tname = "";       
      Type eleft = beleft.right.getElementType();  // Must be an entity type 
      if (eleft == null || "null".equals(eleft + "")) 
      { eleftname = "Object"; tname = "Object"; } 
      else 
      { eleftname = eleft.getName();  
        // System.out.println(">>>>>" + eleftname); 
        tname = eleftname; 

        if ("int".equals(eleftname))
        { eleftname = "Integer"; tname = "int"; } 
        else if (eleftname.startsWith("Set"))
        { eleftname = "HashSet"; } 
        else if (eleftname.startsWith("Sequence"))
        { eleftname = "ArrayList"; } 
        else if ("long".equals(eleftname))
        { eleftname = "Long"; 
          tname = "long"; 
        } 
        else if ("double".equals(eleftname))
        { eleftname = "Double"; 
          tname = "double"; 
        } 
        else if ("boolean".equals(eleftname))
        { eleftname = "Boolean"; 
          tname = "boolean"; 
        } 
        else if (eleft != null && eleft.isEntity())
        { newenv.put(eleftname,evarname); }
      } 

      String ufr1 = right.updateFormJava6(newenv,false);

      String elist = Identifier.nextIdentifier("_" + tname.toLowerCase() + "_list");
      String indvar = Identifier.nextIdentifier("_ind"); 

      if ("Integer".equals(eleftname))
      { return  
         "    ArrayList " + elist + " = new ArrayList();\n" + 
         "    " + elist + ".addAll(" + lqf1 + ");\n" + 
         "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
         "    { int " + evarname + " = ((" + eleftname + ") " + elist + 
                      ".get(" + indvar + ")).intValue();\n" + 
         "      " + ufr1 + "\n" + 
         "    }\n"; 
      } 
      else if (eleft != null && Type.isPrimitiveType(eleft))
      { return  "    ArrayList " + elist + " = new ArrayList();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
        "    { " + tname + " " + evarname + 
                   " = ((" + eleftname + ") " + elist + 
                     ".get(" + indvar + "))." + tname + "Value();\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 
    
      String ltypename = "HashSet"; 
      if (left.isOrdered())
      { ltypename = "ArrayList"; } 

      return 
        "    Collection " + elist + " = new " + ltypename + "();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (Object " + indvar + " : " + elist + ")\n" + 
        "    { " + eleftname + " " + evarname + " = (" + eleftname + ") " + indvar + ";\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
    }    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { String ufl = left.updateFormJava6(env,local);
      String ufr = right.updateFormJava6(env,local);
      return ufl + "\n    " + ufr;
    }

    if (operator.equals("=>"))
    { String ufl = left.queryFormJava6(env,local); 
      String ufr = right.updateFormJava6(env,local); 
      if ("true".equals(ufl))
      { return ufr; }
      return "  if (" + ufl + ") \n" + 
             "  { " + ufr + "}";
    } // accumulate the cases

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      String lqf = left.queryFormJava6(env,local); 
      String rqf = right.queryFormJava6(env,local); 

      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 


    if (operator.equals("=") && left instanceof BasicExpression)
    { val2 = right.queryFormJava6(env,local);
      return ((BasicExpression) left).updateFormEqJava6(env,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof UnaryExpression)
    { val2 = right.queryFormJava6(env,local);
      return ((UnaryExpression) left).updateFormEq("Java6", env, ((UnaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof BinaryExpression)
    { val2 = right.queryFormJava6(env,local);
      return ((BinaryExpression) left).updateFormEq("Java6", env, ((BinaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormJava6(env,local);
      return ((BasicExpression) left).updateFormJava6(env,"<:",val2,right,local);
    }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormJava6(env,local);
      return ((BasicExpression) left).updateFormJava6(env,"-",val2,right,local);
    }
    else if ((operator.equals(":") || 
              operator.equals("<:") || operator.equals("/<:") ||
              operator.equals("/:")) && (right instanceof BasicExpression))
    { String val1 = left.queryFormJava6(env,local);
      BasicExpression ber = (BasicExpression) right;
      if (left.isPrimitive())
      { val1 = left.wrap(val1); }
      return ber.updateFormJava6(env,operator,val1,left,local);
    }
    else if ((operator.equals("->includes") || 
              operator.equals("->excludes")) && (left instanceof BasicExpression))
    { String val3 = right.queryFormJava6(env,local);
      BasicExpression bel = (BasicExpression) left;

      if (right.isPrimitive())
      { val3 = right.wrap(val3); }
      if (operator.equals("->includes"))
      { return bel.updateFormJava6(env,":",val3,right,local); }
      else 
      { return bel.updateFormJava6(env,"/:",val3,right,local); }
    }
	else
    { String qf = queryFormJava6(env,local); 
	  return "if (" + qf + ") { } else { System.err.println(\"Assertion " + this + " fails\"); }\n";  
	}

    // else
    // { return "{} /* Cannot produce update code for: " + this + " */"; }
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { String val2;

    // System.out.println("UPDATE FORM JAVA7 of " + this); 

    if (operator.equals("#") || operator.equals("#1"))
    { return updateFormExistsJava7(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it

    if (operator.equals("#LC"))
    { return updateFormExistsLCJava7(env,local); } 
   
    if (operator.equals("!"))  // Only version with variable is ok
    { BinaryExpression beleft = (BinaryExpression) left; 
      String lqf1 = beleft.right.queryFormJava7(env,local);
      if (beleft.right.umlkind == CLASSID)
      { lqf1 = ((BasicExpression) beleft.right).classExtentQueryFormJava7(env,local); } 
      String eleftname = ""; 
      String evarname = beleft.left + "";  
      
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      String tname = "";       
      Type tleft = beleft.right.getType(); 
      Type eleft = beleft.right.getElementType();  // Must be an entity type 
      if (eleft == null || "null".equals(eleft + "")) 
      { eleftname = "Object"; tname = "Object"; 
        System.out.println("ERROR: null element type for quantifier: " + this); 
      } 
      else 
      { eleftname = eleft.getName();  
        // System.out.println(">>>>>" + eleftname); 
        tname = eleftname; 

        if ("int".equals(eleftname))
        { eleftname = "Integer"; tname = "int"; } 
        else if (eleftname.startsWith("Set"))
        { eleftname = eleft.getJava7(eleft.getElementType()); } 
        else if (eleftname.startsWith("Sequence"))
        { eleftname = eleft.getJava7(eleft.getElementType()); } 
        else if ("long".equals(eleftname))
        { eleftname = "Long"; 
          tname = "long"; 
        } 
        else if ("double".equals(eleftname))
        { eleftname = "Double"; 
          tname = "double"; 
        } 
        else if ("boolean".equals(eleftname))
        { eleftname = "Boolean"; 
          tname = "boolean"; 
        } 
        else if (eleft != null && eleft.isEntity())
        { newenv.put(eleftname,evarname); }
      } // not needed

      String ufr1 = right.updateFormJava7(newenv,false);

      String elist = Identifier.nextIdentifier("_" + tname.toLowerCase() + "_list");
      String indvar = Identifier.nextIdentifier("_ind"); 

      if ("Integer".equals(eleftname))
      { return  
         "    ArrayList<Integer> " + elist + " = new ArrayList<Integer>();\n" + 
         "    " + elist + ".addAll(" + lqf1 + ");\n" + 
         "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
         "    { int " + evarname + " = ((Integer) " + elist + 
                      ".get(" + indvar + ")).intValue();\n" + 
         "      " + ufr1 + "\n" + 
         "    }\n"; 
      } 
      else if (eleft != null && Type.isPrimitiveType(eleft))
      { return  "    ArrayList<" + eleftname + "> " + elist + " = new ArrayList<" + eleftname + ">();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".size(); " + indvar + "++)\n" + 
        "    { " + tname + " " + evarname + 
                   " = ((" + eleftname + ") " + elist + 
                     ".get(" + indvar + "))." + tname + "Value();\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 
    
      String ltypename = tleft.getJava7(eleft); 
      // if (left.isOrdered())
      // { ltypename = "ArrayList"; } 
      // else if (left.isSorted())
      // { ltypename = "TreeSet"; } 
      // define the program type for sorted sets in Type. 

      return 
        "    " + ltypename + " " + elist + " = new " + ltypename + "();\n" + 
        "    " + elist + ".addAll(" + lqf1 + ");\n" + 
        "    for (" + eleftname + " " + evarname + " : " + elist + ")\n" + 
        "    { " + ufr1 + "\n" + 
        "    }\n"; 
    }    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { String ufl = left.updateFormJava7(env,local);
      String ufr = right.updateFormJava7(env,local);
      return ufl + "\n    " + ufr;
    }

    if (operator.equals("=>"))
    { String ufl = left.queryFormJava7(env,local); 
      String ufr = right.updateFormJava7(env,local); 
      if ("true".equals(ufl))
      { return ufr; }
      return "  if (" + ufl + ") \n" + 
             "  { " + ufr + "}";
    } // accumulate the cases

    if (extensionoperators.containsKey(operator))
    { String op = operator;
      String opjava = Expression.getOperatorJava(op); 
      String lqf = left.queryFormJava7(env,local); 
      String rqf = right.queryFormJava7(env,local); 

      if (opjava != null && opjava.length() > 0)
      { return opjava.replaceAll("_1",lqf).replaceAll("_2",rqf); } 
      if (operator.startsWith("->"))
      { op = operator.substring(2,operator.length()); } 
      return lqf + "." + op + "(" + rqf + ")";
    } 


    if (operator.equals("=") && left instanceof BasicExpression)
    { val2 = right.queryFormJava7(env,local);
      // System.out.println("QUERY FORM JAVA7: " + val2); 
      return ((BasicExpression) left).updateFormEqJava7(env,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof UnaryExpression)
    { val2 = right.queryFormJava7(env,local);
      return ((UnaryExpression) left).updateFormEq("Java7", env, ((UnaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof BinaryExpression)
    { val2 = right.queryFormJava7(env,local);
      return ((BinaryExpression) left).updateFormEq("Java7", env, ((BinaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormJava7(env,local);
      return ((BasicExpression) left).updateFormJava7(env,"<:",val2,right,local);
    }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormJava7(env,local);
      return ((BasicExpression) left).updateFormJava7(env,"-",val2,right,local);
    }
    else if ((operator.equals(":") || 
              operator.equals("<:") || operator.equals("/<:") ||
              operator.equals("/:")) && (right instanceof BasicExpression))
    { String val1 = left.queryFormJava7(env,local);
      BasicExpression ber = (BasicExpression) right;
      if (left.isPrimitive())
      { val1 = left.wrap(val1); }
      return ber.updateFormJava7(env,operator,val1,left,local);
    }
    else if ((operator.equals("->includes") || 
              operator.equals("->excludes")) && (left instanceof BasicExpression))
    { String val3 = right.queryFormJava7(env,local);
      BasicExpression bel = (BasicExpression) left;

      if (right.isPrimitive())
      { val3 = right.wrap(val3); }
      if (operator.equals("->includes"))
      { return bel.updateFormJava7(env,":",val3,right,local); }
      else 
      { return bel.updateFormJava7(env,"/:",val3,right,local); }
    }
    else
    { String qf = queryFormJava7(env,local); 
	  return "if (" + qf + ") { } else { System.err.println(\"Assertion " + this + " fails\"); }\n";  
	}

    // else
    // { return "{} /* Cannot produce update code for: " + this + " */"; }
  }

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String val2;
    if (operator.equals("#") || operator.equals("#1"))
    { return updateFormExistsCSharp(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it

    if (operator.equals("#LC"))
    { return updateFormExistsLCCSharp(env,local); } 
   
    if (operator.equals("!"))  // Only version with variable is ok
    { BinaryExpression beleft = (BinaryExpression) left; 
      String lqf1 = beleft.right.queryFormCSharp(env,local);
      if (beleft.right.umlkind == CLASSID)
      { lqf1 = ((BasicExpression) beleft.right).classExtentQueryFormCSharp(env,local); } 
      String eleftname = ""; 
      String evarname = beleft.left + "";  
      
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

      String tname = "";       
      Type eleft = beleft.right.getElementType();  // Must be an entity type 
      if (eleft == null || "null".equals(eleft + "")) 
      { eleftname = "object"; tname = "object"; } 
      else 
      { eleftname = eleft.getName();  
        // System.out.println(">>>>>" + eleftname); 
        tname = eleftname; 

        if (eleftname.startsWith("Set") || eleftname.startsWith("Sequence"))
        { eleftname = "ArrayList"; 
          tname = "ArrayList"; 
        } 
        else if ("int".equals(eleftname))
        { eleftname = "Integer"; tname = "int"; } 
        else if ("long".equals(eleftname))
        { eleftname = "Long"; 
          tname = "long"; 
        } 
        else if ("double".equals(eleftname))
        { eleftname = "Double"; 
          tname = "double"; 
        } 
        else if ("boolean".equals(eleftname))
        { eleftname = "Boolean"; 
          tname = "bool"; 
        } 
        else if ("String".equals(eleftname))
        { tname = "string"; 
          eleftname = "string"; 
        } 
        else if (eleft != null && eleft.isEntity())
        { newenv.put(eleftname,evarname); }
      } // not necessary

      String ufr1 = right.updateFormCSharp(newenv,false);

      String elist = Identifier.nextIdentifier("_" + tname.toLowerCase() + "_list");
      String indvar = Identifier.nextIdentifier("_ind"); 

      if ("Integer".equals(eleftname))
      { return  
        "    ArrayList " + elist + " = new ArrayList();\n" + 
        "    " + elist + ".AddRange(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".Count; " + indvar + "++)\n" + 
        "    { int " + evarname + " = (int) " + elist + "[" + indvar + "];\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 
      else if (eleft != null && Type.isBasicType(eleft))
      { return  "    ArrayList " + elist + " = new ArrayList();\n" + 
        "    " + elist + ".AddRange(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".Count; " + indvar + "++)\n" + 
        "    { " + tname + " " + evarname + 
                   " = ((" + tname + ") " + elist + "[" + indvar + "]);\n" +
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 

      return 
        "    ArrayList " + elist + " = new ArrayList();\n" + 
        "    " + elist + ".AddRange(" + lqf1 + ");\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + ".Count; " + indvar + "++)\n" + 
        "    { " + eleftname + " " + evarname + " = (" + eleftname + ") " + elist + 
                     "[" + indvar + "];\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
    }    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { String ufl = left.updateFormCSharp(env,local);
      String ufr = right.updateFormCSharp(env,local);
      return ufl + "\n    " + ufr;
    }

    if (operator.equals("=>"))
    { String ufl = left.queryFormCSharp(env,local); 
      String ufr = right.updateFormCSharp(env,local); 
      if ("true".equals(ufl))
      { return ufr; }
      return "  if (" + ufl + ") \n" + 
             "  { " + ufr + "}";
    } // accumulate the cases

    if (operator.equals("=") && left instanceof BasicExpression)
    { val2 = right.queryFormCSharp(env,local);
      return ((BasicExpression) left).updateFormEqCSharp(env,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof UnaryExpression)
    { val2 = right.queryFormCSharp(env,local);
      return ((UnaryExpression) left).updateFormEq("CSharp", env, ((UnaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof BinaryExpression)
    { val2 = right.queryFormCSharp(env,local);
      return ((BinaryExpression) left).updateFormEq("CSharp", env, ((BinaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormCSharp(env,local);
      return ((BasicExpression) left).updateFormCSharp(env,"<:",val2,right,local);
    }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormCSharp(env,local);
      return ((BasicExpression) left).updateFormCSharp(env,"-",val2,right,local);
    }
    else if ((operator.equals(":") || 
              operator.equals("<:") || operator.equals("/<:") ||
              operator.equals("/:")) && (right instanceof BasicExpression))
    { String val1 = left.queryFormCSharp(env,local);
      BasicExpression ber = (BasicExpression) right;
      // if (left.isPrimitive())
      // { val1 = ((BasicExpression) left).wrap(val1); }
      return ber.updateFormCSharp(env,operator,val1,left,local);
    }
    else if ((operator.equals("->includes") || operator.equals("->excludes")) && 
             (left instanceof BasicExpression))
    { String val3 = right.queryFormCSharp(env,local);
      BasicExpression bel = (BasicExpression) left;

      // if (right.isPrimitive())
      // { val3 = ((BasicExpression) right).wrap(val3); }
      if (operator.equals("->includes"))
      { return bel.updateFormCSharp(env,":",val3,right,local); }
      else 
      { return bel.updateFormCSharp(env,"/:",val3,right,local); }
    }
    else
    { return "{} /* Cannot produce update code for: " + this + " */"; }
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { String val2;
    if (operator.equals("#") || operator.equals("#1"))
    { return updateFormExistsCPP(env,local);  } 
      // For #1 test qf first: if (qf) { } else { uf } 
      // Special case of ( x : E ) #1 ( x.id = v & P ) Should also check P before doing it

    if (operator.equals("#LC"))
    { return updateFormExistsLCCPP(env,local); } 
   
    if (operator.equals("!"))  // Only version with variable is ok
    { BinaryExpression beleft = (BinaryExpression) left; 
      String lqf1 = beleft.right.queryFormCPP(env,local);
      if (beleft.right.umlkind == CLASSID)
      { lqf1 = ((BasicExpression) beleft.right).classExtentQueryFormCPP(env,local); } 
      String eleftname = ""; 
      String evarname = beleft.left + "";  
      
      java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      
      Type eleft = beleft.right.getElementType();  
      String tname = ""; 
     
      if (eleft == null || "null".equals(eleft + "")) 
      { eleftname = "void*"; tname = "void"; } 
      else 
      { eleftname = eleft.getName();  
        Type eelemt = eleft.getElementType(); 
        tname = eleftname; 

        if ("int".equals(eleftname))
        { eleftname = "Integer"; tname = "int"; } 
        else if (eleftname.startsWith("Set"))
        { eleftname = eleft.getCPP(eelemt); 
          tname = "List"; 
        } 
        else if (eleftname.startsWith("Sequence"))
        { eleftname = eleft.getCPP(eelemt); 
          tname = "List"; 
        }
        else if ("String".equals(eleftname))
        { tname = "string"; 
          eleftname = "string"; 
        } 
        else if ("boolean".equals(eleftname))
        { tname = "bool"; } 
        else if (eleft != null && eleft.isEntity())
        { eleftname = eleftname + "*"; 
          newenv.put(eleftname,evarname); 
        }
      } // not necessary

      String ufr1 = right.updateFormCPP(newenv,false);

      String elist = Identifier.nextIdentifier("_" + tname.toLowerCase() + "_list");
      String indvar = Identifier.nextIdentifier("_ind"); 

      if ("Integer".equals(eleftname))
      { return  "    vector<int>* " + elist + " = " + lqf1 + ";\n" + 
          "    for (int " + indvar + " = 0; " + indvar + " < " + elist + "->size(); " + indvar + "++)\n" + 
        "    { int " + evarname + " = (*" + elist + ")[" + indvar + "];\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 
      else if (eleft != null && Type.isBasicType(eleft))
      { return  "    vector<" + tname + ">* " + elist + " = " + lqf1 + ";\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + "->size(); " + indvar + "++)\n" + 
        "    { " + tname + " " + evarname + " = (*" + elist + ")[" + indvar + "];\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n"; 
      } 
      return 
        "    vector<" + eleftname + ">* " + elist + " = new vector<" + eleftname + ">();\n" + 
        "    " + elist + "->insert(" + elist + "->end(), " + lqf1 + "->begin(), " + 
                                   lqf1 + "->end());\n" + 
        "    for (int " + indvar + " = 0; " + indvar + " < " + elist + "->size(); " + indvar + "++)\n" + 
        "    { " + eleftname + " " + evarname + " = (*" + elist + ")[" + indvar + "];\n" + 
        "      " + ufr1 + "\n" + 
        "    }\n" + 
        "    delete " + elist + ";\n"; 
    } // could copy and delete in the other cases also.    


    // Analyse if-then-else structure      
    if (operator.equals("&"))
    { String ufl = left.updateFormCPP(env,local);
      String ufr = right.updateFormCPP(env,local);
      return ufl + "\n    " + ufr;
    }

    if (operator.equals("=>"))
    { String ufl = left.queryFormCPP(env,local); 
      String ufr = right.updateFormCPP(env,local); 
      if ("true".equals(ufl))
      { return ufr; }
      return "  if (" + ufl + ") \n" + 
             "  { " + ufr + "}";
    } // accumulate the cases

    if (operator.equals("=") && left instanceof BasicExpression)
    { val2 = right.queryFormCPP(env,local);
      return ((BasicExpression) left).updateFormEqCPP(env,val2,local);
    }
    else if (operator.equals("=") && left instanceof UnaryExpression)
    { val2 = right.queryFormCPP(env,local);
      return left.updateForm("CPP",env,((UnaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("=") && left instanceof BinaryExpression)
    { val2 = right.queryFormCPP(env,local);
      return left.updateForm("CPP",env,((BinaryExpression) left).operator,val2,right,local);
    }
    else if (operator.equals("->includesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormCPP(env,local);
      return ((BasicExpression) left).updateFormCPP(env,"<:",val2,right,local);
    }
    else if (operator.equals("->excludesAll") && left instanceof BasicExpression)
    { val2 = right.queryFormCPP(env,local);
      return ((BasicExpression) left).updateFormCPP(env,"-",val2,right,local);
    }
    else if ((operator.equals(":") || 
              operator.equals("<:") || operator.equals("/<:") ||
              operator.equals("/:")) && (right instanceof BasicExpression))
    { String val1 = left.queryFormCPP(env,local);
      BasicExpression ber = (BasicExpression) right;
      /* boolean lmult = left.isMultiple();
      if (lmult)
      { if (operator.equals(":") || operator.equals("<:"))
        { return ber.updateFormCPP(env,"<:",val1,left,local); }
        if (operator.equals("/:") || operator.equals("/<:"))
        { return ber.updateFormCPP(env,"-",val1,left,local); }
      } */ 
      // if (left.isPrimitive())
      // { val1 = ((BasicExpression) left).wrap(val1); }
      return ber.updateFormCPP(env,operator,val1,left,local);
    }
    else if ((operator.equals("->includes") || operator.equals("->excludes")) && 
             (left instanceof BasicExpression))
    { String val3 = right.queryFormCPP(env,local);
      BasicExpression bel = (BasicExpression) left;
      /* boolean rmult = right.isMultiple();
      if (rmult)
      { if (operator.equals("->includes"))
        { return bel.updateFormCPP(env,"<:",val3,right,local); }
        if (operator.equals("->excludes"))
        { return bel.updateFormCPP(env,"-",val3,right,local); }
      } */ 

      // if (right.isPrimitive())
      // { val3 = ((BasicExpression) right).wrap(val3); }
      if (operator.equals("->includes"))
      { return bel.updateFormCPP(env,":",val3,right,local); }
      else 
      { return bel.updateFormCPP(env,"/:",val3,right,local); }
    }
    else
    { return "{} /* disallowed operator: " +
             operator + " */";
    }
  }

  public Statement generateDesignExists(java.util.Map env, boolean local)
  { 
    // Statement ufl = left.generateDesign(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryForm(env,local);

       
    Statement ufr = right.generateDesign(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println("Object creation for " + lvar + " sets features " + wr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println(">>> Creation is complete"); } 
          else 
          { System.out.println("??? Creation may be incomplete: not all of " + feats +
                               " are set"); 
          }
          System.out.println();  
        }
        else 
        { System.out.println("!!! Some features set are not in entity features " + 
                             feats);
          System.out.println();  
        }
      }
    }                   

    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("!!! TYPE ERROR, no entity element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for LHS of " + this, 
                                    "Type error", JOptionPane.ERROR_MESSAGE);
      return new SequenceStatement();  
    } 

    Entity ent = ltype.elementType.getEntity(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      
      if (pk == null)
      { // lvar : T := createT() ; where T is type of ltype
        // ufr
        BasicExpression invokecreent1; 
        if ((ltype + "").startsWith("Set("))
        { invokecreent1 = new BasicExpression("createSet"); 
          invokecreent1.setParameters(new Vector());  // I'm sure this is correct
          invokecreent1.type = new Type("Set", null); 
        } 
        else if ((ltype + "").startsWith("Sequence("))
        { invokecreent1 = new BasicExpression("createSequence"); 
          invokecreent1.setParameters(new Vector());  // I'm sure this is correct
          invokecreent1.type = new Type("Sequence", null); 
        } 
        else 
        { invokecreent1 = new BasicExpression("create" + ent); 
          invokecreent1.setParameters(new Vector());  // I'm sure this is correct
          invokecreent1.type = new Type(ent); 
        }
 
        invokecreent1.umlkind = UPDATEOP;
            // invokecreent.entity = ent; 
        invokecreent1.elementType = new Type(ent);  
            // and Controller as an objectRef
        AssignStatement assgn1 = new AssignStatement(lvar, invokecreent1); 
        assgn1.setType(ltype.elementType); 
        SequenceStatement res = new SequenceStatement(); 
          // res.addStatement(ini); 
        res.addStatement(assgn1); 
        res.addStatement(ufr); 
        return res; 
      } 
      else 
      { // lvar : ent := createent(keyval); ufr
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryForm(env,local); 
          if (ltype.umlkind == CLASSID)
          { // CreationStatement cre = new CreationStatement("" + ent, "" + lvar); 
            // cre.setType(ltype.elementType); 
            // cre.setElementType(ltype.elementType); 
            // cre.setEntity(ent); 
            BasicExpression invokecreent = new BasicExpression("createByPK" + ent); 
            invokecreent.umlkind = UPDATEOP;
            // invokecreent.entity = ent; 
            invokecreent.type = new Type(ent); 
            invokecreent.elementType = new Type(ent);  
            invokecreent.addParameter(keyval); 
            // and Controller as an objectRef
            AssignStatement assgn = new AssignStatement(lvar, invokecreent); 
            assgn.setType(ltype.elementType); 
            if (remainder.size() > 0)
            { Expression rem = (Expression) remainder.get(0);
              Statement remstat = rem.generateDesign(env,local); 
             
              SequenceStatement res = new SequenceStatement(); 
              // res.addStatement(cre); 
              res.addStatement(assgn); res.addStatement(remstat); 
              return res;
            }  
            else // just E->exists( x ! true ) 
            { return assgn; } 
          } 
          return new SequenceStatement(); 
        } 
        else // pkey != null but no pkey = val in the rhs.  
        { // CreationStatement ini = new CreationStatement("" + ent, "" + lvar); 
          // ini.setType(ltype.elementType); 
          // ini.setElementType(ltype.elementType);
          // ini.setEntity(ent); 
          BasicExpression invokecreent2 = new BasicExpression("create" + ent); 
          invokecreent2.umlkind = UPDATEOP;
            // invokecreent.entity = ent; 
          invokecreent2.type = new Type(ent); 
          invokecreent2.elementType = new Type(ent);  
          invokecreent2.setParameters(new Vector());  // I'm sure this is correct
            // and Controller as an objectRef
          AssignStatement assgn2 = new AssignStatement(lvar, invokecreent2); 
          assgn2.setType(ltype.elementType); 
          SequenceStatement res = new SequenceStatement(); 
        // res.addStatement(ini); 
          res.addStatement(assgn2); 
          res.addStatement(ufr); 
          return res; 
        } 
      } 
    } 
             
    if (operator.equals("#1"))
    { BinaryExpression test = new BinaryExpression("#", left, right); 
      CreationStatement ini2 = new CreationStatement("" + ent, "" + lvar); 
      ini2.setType(ltype.elementType); 
      ini2.setElementType(ltype.elementType);
      ini2.setEntity(ent); 
      BasicExpression invokecreent3 = new BasicExpression("create" + ent); 
      invokecreent3.umlkind = UPDATEOP;
            // invokecreent.entity = ent; 
      invokecreent3.setParameters(new Vector());  // I'm sure this is correct
      invokecreent3.type = new Type(ent); 
      invokecreent3.elementType = new Type(ent);  
            // and Controller as an objectRef
      AssignStatement assgn3 = new AssignStatement(lvar, invokecreent3); 
      SequenceStatement seq = new SequenceStatement(); 
      seq.addStatement(ini2); 
      seq.addStatement(assgn3); 
      seq.addStatement(ufr); 
      ConditionalStatement queryform = new ConditionalStatement(test, new SequenceStatement(), seq);
      return queryform; 
    }  // assume no primary key in this case - redundant. 
    
    return new SequenceStatement();  
  }  
     
  public String updateFormExists(java.util.Map env, boolean local)
  { String cont = "Controller.inst()"; 

    String ufl = left.updateForm(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryForm(env,local);

       
      // java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      // newenv.put(ltype + "",lvar + "");
    String ufr = right.updateForm(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println(">>> Object creation for " + lvar + " sets features " + wr + 
                       " in update " + ufr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println(">>> Creation is complete"); } 
          else 
          { System.out.println("??? Creation may be incomplete: not all of " + feats +
                               " are set"); 
          } 
        }
        else 
        { System.out.println("!!! Some features set are not in entity features " + 
                             feats); 
        }
        System.out.println(); 
      }
    }                   

    String res = ""; 
    String searchcode = ""; 

    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("!!! TYPE ERROR, no entity element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for LHS of " + this, 
                                    "Type error", JOptionPane.ERROR_MESSAGE);
      return " /* Invalid ->exists */ {}\n"; 
    } 
    Entity ent = ltype.elementType.getEntity(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk != null) 
      { // System.out.println(); 
        // System.out.println("Exists with primary key " + pk); 
        
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryForm(env,local); 
          if (ltype.umlkind == CLASSID)
          { searchcode = "  if (" + cont + ".get" + ent + "ByPK(" + kvqf + ") != null)\n"; }  
          else 
          { searchcode = "   if (" + lqf + ".contains(" + cont + ".get" + ent + "ByPK(" + kvqf + ")))\n"; } 
 
          res = res + searchcode; 
          res = res + "    { " + ent + " " + lvar + " = " + cont + ".get" + ent + "ByPK(" + kvqf + ");\n "; 
     
          if (remainder.size() > 0)
          { Expression rem = (Expression) remainder.get(0); 
            // System.out.println("REMAINDER == " + remainder); 
            res = res + "    " + rem.updateForm(env,local) + "\n"; 
          } 
          res = res + "  }\n";
            
          if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
                (ent != null && ent.isAbstract()))
          { return res; }
              
          res = res + "    else\n" +   
                      "    { " + ufl + "\n    " + ufr + " }\n";
          return res; 
        } 
      } 

      if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryForm(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ").size() > 0)\n" + 
               "  { " + ent + " " + lvar + " = (" + ent + ") (" + lqf + ").get(0);\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      }
        
      if (operator.equals("#1"))
      { String queryform = existsQueryForm(env,local); // op is actually #1 here
        return "  if (" + queryform + ") { }\n" + 
               "  else\n" + 
               "  { " + ufl + "\n    " + ufr + " }\n"; 
      } 
      else 
      { return ufl + "\n    " + ufr; }
    } 

    return res; 
  }

  public String updateFormExistsJava6(java.util.Map env, boolean local)
  { String cont = "Controller.inst()"; 

    String ufl = left.updateFormJava6(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryFormJava6(env,local);

       
      // java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      // newenv.put(ltype + "",lvar + "");
    String ufr = right.updateFormJava6(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println("Object creation for " + lvar + " sets features " + wr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println("Creation is complete"); } 
          else 
          { System.out.println("Creation may be incomplete: not all of " + feats +
                               " are set"); 
          } 
        }
        else 
        { System.out.println("Some features set are not in entity features " + 
                             feats); 
        }
      }
    }                   
    String res = ""; 
    String searchcode = ""; 
    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("TYPE ERROR: no element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
                                      "Type error", JOptionPane.ERROR_MESSAGE);
      return "  {}\n"; 
    } 
    Entity ent = ltype.elementType.getEntity(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk != null) 
      { System.out.println(); 
        // System.out.println("Exists with primary key " + pk); 
        
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryFormJava6(env,local); 
          if (ltype.umlkind == CLASSID)
          { searchcode = "  if (" + cont + ".get" + ent + "ByPK(" + kvqf + ") != null)\n"; }  
          else 
          { searchcode = "   if (" + lqf + ".contains(" + cont + ".get" + ent + "ByPK(" + kvqf + ")))\n"; } 
 
          res = res + searchcode; 
          res = res + "    { " + ent + " " + lvar + " = " + cont + ".get" + ent + "ByPK(" + kvqf + ");\n "; 
     
          if (remainder.size() > 0)
          { Expression rem = (Expression) remainder.get(0); 
            // System.out.println("REMAINDER == " + remainder); 
            res = res + "    " + rem.updateFormJava6(env,local) + "\n"; 
          } 
          res = res + "  }\n";
            
          if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
                (ent != null && ent.isAbstract()))
          { return res; }
              
          res = res + "    else\n" +   
                      "    { " + ufl + "\n    " + ufr + " }\n";
          return res; 
        } 
      } 

      if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryFormJava6(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ").size() > 0)\n" + 
               "  { " + ent + " " + lvar + " = (" + ent + ") (" + lqf + ").get(0);\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      }
        
      if (operator.equals("#1"))
      { String queryform = existsQueryFormJava6(env,local); // op is actually #1 here
        return "  if (" + queryform + ") { }\n" + 
               "  else\n" + 
               "  { " + ufl + "\n    " + ufr + " }\n"; 
      } 
      else 
      { return ufl + "\n    " + ufr; }
    } 

    return res; 
  }

  public String updateFormExistsJava7(java.util.Map env, boolean local)
  { String cont = "Controller.inst()"; 

    String ufl = left.updateFormJava7(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryFormJava7(env,local);

       
      // java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      // newenv.put(ltype + "",lvar + "");
    String ufr = right.updateFormJava7(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println("Object creation for " + lvar + " sets features " + wr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println("Creation is complete"); } 
          else 
          { System.out.println("Creation may be incomplete: not all of " + feats +
                               " are set"); 
          } 
        }
        else 
        { System.out.println("Some features set are not in entity features " + 
                             feats); 
        }
      }
    }                   
    String res = ""; 
    String searchcode = ""; 
    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("TYPE ERROR: no element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
                                      "Type error", JOptionPane.ERROR_MESSAGE);
      return "  {}\n"; 
    } 
    Entity ent = ltype.elementType.getEntity(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk != null) 
      { System.out.println(); 
        // System.out.println("Exists with primary key " + pk); 
        
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryFormJava7(env,local); 
          if (ltype.umlkind == CLASSID)
          { searchcode = "  if (" + cont + ".get" + ent + "ByPK(" + kvqf + ") != null)\n"; }  
          else 
          { searchcode = "   if (" + lqf + ".contains(" + cont + ".get" + ent + "ByPK(" + kvqf + ")))\n"; } 
 
          res = res + searchcode; 
          res = res + "    { " + ent + " " + lvar + " = " + cont + ".get" + ent + "ByPK(" + kvqf + ");\n "; 
     
          if (remainder.size() > 0)
          { Expression rem = (Expression) remainder.get(0); 
            // System.out.println("REMAINDER == " + remainder); 
            res = res + "    " + rem.updateFormJava7(env,local) + "\n"; 
          } 
          res = res + "  }\n";
            
          if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
                (ent != null && ent.isAbstract()))
          { return res; }
              
          res = res + "    else\n" +   
                      "    { " + ufl + "\n    " + ufr + " }\n";
          return res; 
        } 
      } 

      if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryFormJava7(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ").size() > 0)\n" + 
               "  { " + ent + " " + lvar + " = (" + ent + ") (" + lqf + ").get(0);\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      }
        
      if (operator.equals("#1"))
      { String queryform = quantifierQueryFormJava7(env,local); // op is actually #1 here
        return "  if (" + queryform + ") { }\n" + 
               "  else\n" + 
               "  { " + ufl + "\n    " + ufr + " }\n"; 
      } 
      else 
      { return ufl + "\n    " + ufr; }
    } 

    return res; 
  }

  public String updateFormExistsCSharp(java.util.Map env, boolean local)
  { String cont = "Controller.inst()"; 

    String ufl = left.updateFormCSharp(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryFormCSharp(env,local);

       
      // java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      // newenv.put(ltype + "",lvar + "");
    String ufr = right.updateFormCSharp(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println("Object creation for " + lvar + " sets features " + wr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println("Creation is complete"); } 
          else 
          { System.out.println("Creation may be incomplete: not all of " + feats +
                               " are set"); 
          } 
        }
        else 
        { System.out.println("Some features set are not in entity features " + 
                             feats); 
        }
      }
    }                   
    String res = ""; 
    String searchcode = ""; 
    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("ERROR: no element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
                                      "Type error", JOptionPane.ERROR_MESSAGE);
      return "  {}\n"; 
    } 
    Entity ent = ltype.elementType.getEntity(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk != null) 
      { System.out.println(); 
        // System.out.println("Exists with primary key " + pk); 
        
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryFormCSharp(env,local); 
          if (ltype.umlkind == CLASSID)
          { searchcode = "  if (" + cont + ".get" + ent + "ByPK(" + kvqf + ") != null)\n"; }  
          else 
          { searchcode = "   if (" + lqf + ".Contains(" + cont + ".get" + ent + "ByPK(" + kvqf + ")))\n"; } 
 
          res = res + searchcode; 
          res = res + "    { " + ent + " " + lvar + " = " + cont + ".get" + ent + "ByPK(" + kvqf + ");\n  "; 
     
          if (remainder.size() > 0)
          { Expression rem = (Expression) remainder.get(0); 
            // System.out.println("REMAINDER == " + remainder); 
            res = res + "    " + rem.updateFormCSharp(env,local) + "\n"; 
          } 
          res = res + "  }\n";
            
          if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
                (ent != null && ent.isAbstract()))
          { return res; }
              
          res = res + "    else\n" +   
                      "    { " + ufl + "\n    " + ufr + " }\n";
          return res; 
        } 
      } 

      if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryFormCSharp(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ").Count > 0)\n" + 
               "  { " + ent + " " + lvar + " = (" + ent + ") " + lqf + "[0];\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      }
        
      if (operator.equals("#1"))
      { String queryform = existsQueryFormCSharp(env,local); // op is actually #1 here
        return "  if (" + queryform + ") { }\n" + 
               "  else\n" + 
               "  { " + ufl + "\n    " + ufr + " }\n"; 
      } 
      else 
      { return ufl + "\n    " + ufr; }
    } 

    return res; 
  }

  public String updateFormExistsCPP(java.util.Map env, boolean local)
  { String cont = "Controller::inst->"; 

    String ufl = left.updateFormCPP(env,local);
    BinaryExpression lexp = (BinaryExpression) left; 
    Expression lvar = lexp.left; 
    Expression ltype = lexp.right;
    String lqf = ltype.queryFormCPP(env,local);

       
      // java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
      // newenv.put(ltype + "",lvar + "");
    String ufr = right.updateFormCPP(env,local);
      // Check completeness: 
    Vector wr = right.writeFrame(); 
    System.out.println("Object creation for " + lvar + " sets features " + wr); 
    Type t = lvar.getType();
    if (t != null)
    { Entity e = t.getEntity(); 
      if (e != null)
      { Vector feats = e.allDefinedFeatures(); 
        if (feats.containsAll(wr))
        { if (wr.containsAll(feats))
          { System.out.println("Creation is complete"); } 
          else 
          { System.out.println("Creation may be incomplete: not all of " + feats +
                               " are set"); 
          } 
        }
        else 
        { System.out.println("Some features set are not in entity features " + feats); }
      }
    }  
                 
    String res = ""; 
    String searchcode = ""; 
    if (ltype.elementType == null || !ltype.elementType.isEntity())
    { System.err.println("ERROR: no element type in exists: " + this); 
      JOptionPane.showMessageDialog(null, "no element type for " + lexp.right + " in " + this, 
                                      "Type error", JOptionPane.ERROR_MESSAGE);
      return "  {}\n"; 
    } 

    Entity ent = ltype.elementType.getEntity(); 
    String ename = ent.getName(); 

    if (operator.equals("#1") || operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk != null) 
      { System.out.println(); 
        // System.out.println("Exists with primary key " + pk); 
        
         
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        if (keyval != null) // Special case, matching on id first
        { String kvqf = keyval.queryFormCPP(env,local); 
          if (ltype.umlkind == CLASSID)
          { searchcode = "  if (" + cont + "get" + ename + "ByPK(" + kvqf + ") != 0)\n"; }  
          else 
          { searchcode = "  if (UmlRsdsLib<" + ename + "*>::isIn(" + 
                                cont + "get" + ename + "ByPK(" + kvqf + "), " + lqf + "))\n"; 
          } 
 
          res = res + searchcode; 
          res = res + "    { " + ename + "* " + lvar + " = " + cont + "get" + ename + "ByPK(" + kvqf + ");\n"; 
     
          if (remainder.size() > 0)
          { Expression rem = (Expression) remainder.get(0); 
            // System.out.println("REMAINDER == " + remainder); 
            res = res + "    " + rem.updateFormCPP(env,local) + "\n"; 
          } 
          res = res + "  }\n";
            
          if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
                (ent != null && ent.isAbstract()))
          { return res; }
              
          res = res + "    else\n" +   
                      "    { " + ufl + "\n    " + ufr + " }\n";
          return res; 
        } 
      } 

      if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryFormCPP(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ")->size() > 0)\n" + 
               "  { " + ename + "* " + lvar + " = (*" + lqf + ")[0];\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      }
        
      if (operator.equals("#1"))
      { String queryform = existsQueryFormCPP(env,local); // op is actually #1 here
        return "  if (" + queryform + ") { }\n" + 
               "  else\n" + 
               "  { " + ufl + "\n    " + ufr + " }\n"; 
      } 
      else 
      { return ufl + "\n    " + ufr; }
    } 

    return res; 
  }

  public BExpression bqueryForm(java.util.Map env)
  { BExpression le = left.bqueryForm(env);
    BExpression les = le.simplify(); 

    if (operator.equals("#") || operator.equals("#LC"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      // BExpression lll = lexp.left.bqueryForm(env); 
      // String t = lexp.right + ""; 
      // env1.put(t,lll); 
      BExpression rrr = right.bqueryForm(env); 
      BExpression res = new BQuantifierExpression("exists",x,
                              new BBinaryExpression("&",les,rrr)); 
      return res; 
    } 

    if (operator.equals("!"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      // BExpression lll = lexp.left.bqueryForm(env); 
      // String t = lexp.right + ""; 
      // env1.put(t,lll); 
      BExpression rrr = right.bqueryForm(env); 
      BExpression res = new BQuantifierExpression("forall",x,
                              new BBinaryExpression("=>",les,rrr)); 
      return res; 
    } 

    if (operator.equals("#1"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      BExpression rng = lexp.right.bqueryForm(env); 
      BExpression rrr = right.bqueryForm(env);
      BExpression sel = new BSetComprehension(x,rng,rrr);
      BExpression selsize = new BUnaryExpression("card",sel);   
      BExpression res = new BBinaryExpression("=",selsize,
                              new BBasicExpression("1")); 
      return res; 
    } 


    BExpression re = right.bqueryForm(env);
    boolean lmult = left.isMultiple();
    boolean rmult = right.isMultiple();

    if (operator.equals("|") || operator.equals("->select") ||
        operator.equals("|R") || operator.equals("->reject"))
    { return selectBinvariantForm(le,re,env,true); } 

    if (operator.equals("->collect") || operator.equals("->unionAll") ||
        operator.equals("|C") || operator.equals("->intersectAll"))
    { return collectBinvariantForm(le,re,env,true); } 

    if (operator.equals("->forAll") || operator.equals("->exists") ||
        "->existsLC".equals(operator) || operator.equals("->exists1"))
    { return quantifierBinvariantForm(le,re,env,true); } 

    if (operator.equals("="))
    { if (lmult && !rmult)
      { BSetExpression se; 
        if (re instanceof BSetExpression)
        { se = (BSetExpression) re; } 
        else 
        { se = new BSetExpression(); 
          se.addElement(re); 
        } 
        BExpression res = new BBinaryExpression("<:",le,se);
        res.setBrackets(needsBracket); 
        return res; 
      }
      if (!lmult && rmult)
      { return new BBinaryExpression(":",le,re); }  
    } // and other cases, and for /=

    String op = operator; 
    if (operator.equals("!=") || operator.equals("<>"))
    { op = "/="; }
    else if (operator.equals("->union"))
    { op = "\\/"; } 
    else if (operator.equals("->intersection"))
    { op = "/\\"; } 
    else if (operator.equals("->oclIsKindOf") || operator.equals("->oclIsTypeOf"))
    { op = ":"; } 

    // System.out.println(left + " b form is " + le + " " + le.setValued());
    les = BExpression.unSet(les); 
    // System.out.println("simplified is: " + les); 
    BExpression res = re.simplify();

    BExpression result; 
    if (operator.equals("->excludesAll"))
    { result = new BBinaryExpression("=",
                     new BBinaryExpression("/\\",les,res),
                     new BSetExpression()); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->includesAll"))
    { BExpression ll = les;
      if (left.isOrderedB())
      { ll = new BUnaryExpression("ran",les); }
      result = new BBinaryExpression("<:",res,ll); 
    } 
    else if (operator.equals("->includes"))
    { BExpression ll = les; 
      if (left.isOrderedB())
      { ll = new BUnaryExpression("ran",les); }  
      result = new BBinaryExpression(":",res,ll);
    } 
    else if (operator.equals("->excludes"))
    { BExpression ll = les; 
      if (left.isOrderedB())
      { ll = new BUnaryExpression("ran",les); }
      result = new BBinaryExpression("/:",res,ll);
    } 
    else if (operator.equals("->including"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(res); 
      result = new BBinaryExpression("\\/",les,bset); 
      result.setBrackets(true); 
    } // and case for ordered, but should use ->prepend, ->append in such cases
    else if (operator.equals("->excluding"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(res); 
      result = new BBinaryExpression("-",les,bset); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->prepend"))
    { result = new BBinaryExpression("->",res,les); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->append"))
    { result = new BBinaryExpression("<-",les,res); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->indexOf"))  // min(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,img); 
      result = new BUnaryExpression("min",res1); 
    } 
    else if (operator.equals("->lastIndexOf"))  // max(bel~[{ber}]) 
    { BSetExpression limg = new BSetExpression(); 
      limg.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,limg); 
      result = new BUnaryExpression("max",res1); 
    } 
    else if (operator.equals("->count"))  // card(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,img); 
      result = new BUnaryExpression("card",res); 
    } 
    else 
    { result = new BBinaryExpression(op,les,res); } 

    if (operator.equals("/\\") || operator.equals("\\/") ||
        operator.equals("->union") || operator.equals("->intersection") || 
        operator.equals("->excludesAll") || operator.equals("->includesAll") ||
        operator.equals("^") || operator.equals("-"))
    { if (les.setValued() || res.setValued())
      { result.setMultiplicity(ModelElement.MANY); }
      result.setBrackets(needsBracket); 
    }

    if (objectRef != null)
    { BExpression ref = objectRef.bqueryForm(env);
      result.setBrackets(true);  
      if (result.multiplicity == ModelElement.MANY)
      { return new BApplySetExpression(result,ref); } 
      return new BApplyExpression(result,ref); 
    }
    return result.simplify(); 
  }
  // isPrefix, isSuffix? 

  public BExpression bqueryForm()
  { BExpression le = left.bqueryForm();
    BExpression les = le.simplify();
 
    if (operator.equals("#") || operator.equals("#LC"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      // BExpression lll = lexp.left.bqueryForm(env); 
      // String t = lexp.right + ""; 
      // env1.put(t,lll); 
      BExpression rrr = right.bqueryForm(); 
      BExpression res = new BQuantifierExpression("exists",x,
                              new BBinaryExpression("&",les,rrr)); 
      return res; 
    } 

    if (operator.equals("!"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      // BExpression lll = lexp.left.bqueryForm(env); 
      // String t = lexp.right + ""; 
      // env1.put(t,lll); 
      BExpression rrr = right.bqueryForm(); 
      BExpression res = new BQuantifierExpression("forall",x,
                              new BBinaryExpression("=>",les,rrr)); 
      return res; 
    } 

    if (operator.equals("#1"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      BExpression rng = lexp.right.bqueryForm(); 
      BExpression rrr = right.bqueryForm();
      BExpression sel = new BSetComprehension(x,rng,rrr);
      BExpression selsize = new BUnaryExpression("card",sel);   
      BExpression res = new BBinaryExpression("=",selsize,
                              new BBasicExpression("1")); 
      return res; 
    } 

    BExpression re = right.bqueryForm();

    String op = operator; 
    if (operator.equals("!=") || operator.equals("<>"))
    { op = "/="; }
    else if (operator.equals("->union"))
    { op = "\\/"; } 
    else if (operator.equals("->intersection"))
    { op = "/\\"; } 
    else if (operator.equals("->oclIsKindOf") || operator.equals("->oclIsTypeOf"))
    { op = ":"; } 

    // System.out.println(left + " b form is " + le + " " + le.setValued());
    les = BExpression.unSet(les); 
    // System.out.println("simplified is: " + les); 
    BExpression res = re.simplify();

    java.util.Map env0 = new java.util.HashMap(); 

    if (operator.equals("|") || operator.equals("->select") ||
        operator.equals("|R") || operator.equals("->reject"))
    { return selectBinvariantForm(le,re,env0,true); } 

    if (operator.equals("->collect") || operator.equals("->unionAll") ||
        operator.equals("|C") || operator.equals("->intersectAll"))
    { return collectBinvariantForm(le,re,env0,true); } 

    if (operator.equals("->forAll") || operator.equals("->exists") ||
        "->existsLC".equals(operator) || operator.equals("->exists1"))
    { return quantifierBinvariantForm(le,re,env0,true); } 

    BExpression result; 
    if (operator.equals("->excludesAll"))
    { result = new BBinaryExpression("=",
                     new BBinaryExpression("/\\",les,res),
                     new BSetExpression()); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->includesAll"))
    { result = new BBinaryExpression("<:",res,les); } 
    else if (operator.equals("->including"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(res); 
      result = new BBinaryExpression("\\/",les,bset); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->excluding"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(res); 
      result = new BBinaryExpression("-",les,bset); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->includes"))
    { result = new BBinaryExpression(":",res,les); } 
    else if (operator.equals("->excludes"))
    { result = new BBinaryExpression("/:",res,les); } 
    else if (operator.equals("->prepend"))
    { result = new BBinaryExpression("->",res,les); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->append"))
    { result = new BBinaryExpression("<-",les,res); 
      result.setBrackets(true); 
    } 
    else if (operator.equals("->indexOf"))  // min(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,img); 
      result = new BUnaryExpression("min",res1); 
    } 
    else if (operator.equals("->lastIndexOf"))  // max(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,img); 
      result = new BUnaryExpression("max",res1); 
    } 
    else if (operator.equals("->count"))  // card(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(res); 
      BExpression func = new BPostfixExpression("~", les); 
      BExpression res1 = new BApplySetExpression(func,img); 
      result = new BUnaryExpression("card",res); 
    } 
    else 
    { result = new BBinaryExpression(op,les,res); } 
    
    if (operator.equals("/\\") || operator.equals("\\/") ||
        operator.equals("->union") || operator.equals("->intersection") ||
        operator.equals("->includesAll") || operator.equals("->excludesAll") ||
        operator.equals("^") || operator.equals("-"))
    { if (les.setValued() || res.setValued())
      { result.setMultiplicity(ModelElement.MANY); }
      result.setBrackets(needsBracket); 
    }

    if (objectRef != null)
    { BExpression ref = objectRef.bqueryForm();
      result.setBrackets(true);  
      if (result.multiplicity == ModelElement.MANY)
      { return new BApplySetExpression(result,ref); } 
      return new BApplyExpression(result,ref); 
    }

    return result; 
  }  // select, reject, includes, excludes, collect, exists, forall, exists1

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression val2;
    String s;
    if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String dvar = lexp.left + "";
      Expression typ = lexp.right; 

      // Only makes sense for entities typ: 
      if (typ.umlkind != CLASSID) { return new BBasicStatement("skip"); }  

      String dvars = ("" + typ).toLowerCase() + "s";
      String tname = objectType(typ,env,local);
      BExpression dexp = new BBasicExpression(dvar);
      dexp.setKind(VARIABLE); 
      BExpression texp = new BBasicExpression(tname);
      texp.setKind(VARIABLE); 
      BExpression objs = new BBasicExpression(dvars);
      objs.setKind(VARIABLE); 

      BExpression tobjs = 
        new BBasicExpression(tname.substring(0,tname.length()-4).toLowerCase() + "s"); 
      tobjs.setKind(VARIABLE); 
      BExpression mns = new BBinaryExpression("-",texp,tobjs);
      BExpression exp = new BBinaryExpression(":",dexp,mns);

      // BExpression s1 = BExpression.unSet(s); 
      // String snme = s1 + "";
      // BStatement assgn =
      //   constructLocalBOp("set",snme,null,dexp,null);
      BStatement addst =
         ((BasicExpression) lexp.right).constructLocalBOp("add",dvars,null,dexp,null,null);
      BParallelStatement stat = new BParallelStatement();
      stat.addStatement(addst);
      // and add same for each ancestor of typ, if an entity. 
      stat.addStatements(ancestorAddStatements(typ,dexp)); 
      BStatement stat2 = right.bupdateForm(env,local); 
      stat.addStatement(stat2);
      BStatement bodystat = stat.seq2parallel(); 
        // BStatement.separateUpdates(stat.getStatements()); 

      Vector vars = new Vector();
      vars.add(dvar);
    // 
    // IF #x(x:T & right) THEN skip 
    // ELSE ANY snmex WHERE snmex : data.toUpperCase() -
    //   data.toLowerCase() + "s" THEN snme(datax) := snmex ||
    //      datas := datas \/ {snmex} END END

      BStatement res1 = new BAnyStatement(vars,exp,bodystat); 

      if (operator.equals("#1"))
      { BExpression test = binvariantForm(env,local); 
        BStatement res = new BIfStatement(test,new BBasicStatement("skip"),res1);
        return res;  
      } 
      else 
      { return res1; } 
    } 
    
    if (operator.equals("&"))
    { BParallelStatement bp = new BParallelStatement();
      BStatement sl = left.bupdateForm(env,local);
      BStatement sr = right.bupdateForm(env,local);
      bp.addStatement(sl);
      bp.addStatement(sr);
      Vector rd2 = right.allFeaturesUsedIn(); // but some are only written
      Vector wrf1 = sl.wr(); 
      Vector wrf2 = sr.wr(); 
      // if (inters1.size() > 0 || inters2.size() > 0)
      // { sl = sl.normalise(); 
      //   sr = sr.normalise(); 
      // } FROM bOpUpdateform???
      return bp.sequence2parallel(sl,sr,wrf1,wrf2,rd2); 
      // combine the updates
    }

    if (operator.equals("or"))
    { BStatement sl = left.bupdateForm(env,local);
      BStatement sr = right.bupdateForm(env,local);
      return new BChoiceStatement(sl,sr); 
    }

    if (operator.equals("=>"))
    { BExpression cond = left.binvariantForm(env,local); 
      BStatement code = right.bupdateForm(env,local);
      BExpression conds = cond.simplify(); 
      if (conds.equals("TRUE") || conds.equals("true"))
      { return code; } 
      return new BIfStatement(conds,code,null); 
    } 

    if (operator.equals("="))
    { val2 = right.binvariantForm(env,local); 
         // right.bqueryForm(env);  // binvariantForm is sufficient
         // if its a singleton set just take the single element. 
      boolean lmultiple = left.isMultiple();
     
      BExpression val3 = val2.simplify(); 
      if (!lmultiple && (val3 instanceof BSetExpression))
      { BSetExpression bset = (BSetExpression) val3; 
        if (bset.isSingleton())
        { val3 = bset.getElement(0); } // but could be intended?
      } 
      return ((BasicExpression) left).bEqupdateForm(env,val3,local);
    }
    else if (operator.equals(":") ||  // if rhs not basic do ANY lx WHERE lx:rhs ...
             operator.equals("<:") ||
             operator.equals("/:") ||
             operator.equals("/<:"))
    { // if (right instanceof BasicExpression)
      val2 = left.binvariantForm(env,local);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      // BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) right).bupdateForm(env,operator,val3,local);
    } // If right.isOrderedB() then : means append, etc. 
    else if (operator.equals("->includes"))
    { // if (right instanceof BasicExpression)
      val2 = right.binvariantForm(env,local);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      // BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,":",val3,local);
    }
    else if (operator.equals("->excludes"))
    { // if (right instanceof BasicExpression)
      val2 = right.binvariantForm(env,local);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      // BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"/:",val3,local);
    }
    else if (operator.equals("->includesAll"))
    { // if (right instanceof BasicExpression)
      val2 = right.bqueryForm(env);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"<:",val4,local);
    }
    else if (operator.equals("->excludesAll"))
    { // if (right instanceof BasicExpression)
      val2 = right.bqueryForm(env);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"/<:",val4,local);
    }
    else
    { return new BBasicStatement("skip"); }  
  }

  public BStatement bOpupdateForm(java.util.Map env, boolean local)
  { BExpression val2;
    String s;
    if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC"))  
    // assume (x : C) # P, where C is a class name
    { BinaryExpression lexp = (BinaryExpression) left; 
      String dvar = lexp.left + "";
      Expression typ = lexp.right; 
      if (typ.umlkind != CLASSID) { return new BBasicStatement("skip"); }  

      String dvars = ("" + typ).toLowerCase() + "s";
      String tname = objectType(typ,env,local);
      BBasicExpression dexp = new BBasicExpression(dvar);
      dexp.setKind(VARIABLE); 
      BBasicExpression texp = new BBasicExpression(tname);
      texp.setKind(VARIABLE); 
      BBasicExpression objs = new BBasicExpression(dvars);
      objs.setKind(VARIABLE); 
      BBasicExpression tobjs = 
        new BBasicExpression(tname.substring(0,tname.length()-4).toLowerCase() + "s"); 
      tobjs.setKind(VARIABLE); 
      BExpression mns = new BBinaryExpression("-",texp,tobjs);
      // BExpression mns = new BBinaryExpression("-",texp,objs);
      BExpression exp = new BBinaryExpression(":",dexp,mns);
      // BExpression s1 = BExpression.unSet(s); 
      // String snme = s1 + "";
      // BStatement assgn =
      //   constructLocalBOp("set",snme,null,dexp,null);
      BStatement addst =
         ((BasicExpression) lexp.right).constructLocalBOp("add",dvars,null,dexp,null,null);
      BParallelStatement stat = new BParallelStatement();
      stat.addStatement(addst);
      stat.addStatements(ancestorAddStatements(typ,dexp)); 
      BStatement stat2 = right.bupdateForm(env,local); 
      stat.addStatement(stat2);

      BStatement bodystat = stat.seq2parallel(); 
        // BStatement.separateUpdates(stat.getStatements()); 

      Vector vars = new Vector();
      vars.add(dvar);
    // IF #x(x:T & right) THEN skip 
    // ELSE ANY snmex WHERE snmex : data.toUpperCase() -
    //   data.toLowerCase() + "s" THEN snme(datax) := snmex ||
    //      datas := datas \/ {snmex} END END
      BStatement res1 = new BAnyStatement(vars,exp,bodystat);
      if (operator.equals("#1"))  
      { BExpression test = binvariantForm(env,local); 
        BStatement res = new BIfStatement(test,new BBasicStatement("skip"),res1);
        return res;  
      } 
      else 
      { return res1; } 
    }

    
    if (operator.equals("&"))
    { BParallelStatement bp = new BParallelStatement();
      BStatement sl = left.bupdateForm(env,local);
      BStatement sr = right.bupdateForm(env,local);
      // BStatement bodystat = BStatement.separateUpdates(bp.getStatements()); 
      // return bodystat; // use separate updates
      Vector rd2 = right.allFeaturesUsedIn(); // but some are only written
      Vector wrf1 = sl.wr(); 
      Vector wrf2 = sr.wr();
      Vector inters1 = VectorUtil.intersection(wrf1,wrf2);
      Vector inters2 = VectorUtil.intersection(wrf1,rd2);
      if (inters1.size() > 0 || inters2.size() > 0)
      { sl = sl.normalise(); 
        sr = sr.normalise(); 
      } 
      bp.addStatement(sl);
      bp.addStatement(sr);
      return bp.sequence2parallel(sl,sr,wrf1,wrf2,rd2); 
    } // Should this be in bupdateForm also?? 

    if (operator.equals("or"))
    { BStatement sl = left.bupdateForm(env,local);
      BStatement sr = right.bupdateForm(env,local);
      return new BChoiceStatement(sl,sr); 
    }

    if (operator.equals("=>"))
    { BExpression cond = left.binvariantForm(env,local); 
      BStatement code = right.bupdateForm(env,local);
      BExpression conds = cond.simplify(); 
      if (conds.equals("TRUE") || conds.equals("true"))
      { return code; } 
      return new BIfStatement(conds,code,null); 
    } 

    if (operator.equals("="))
    { val2 = right.binvariantForm(env,local); // is sufficient
      // if its a singleton set just take the single element. 
      boolean lmultiple = left.isMultiple();
     
      BExpression val3 = val2.simplify(); 
      if (!lmultiple && (val3 instanceof BSetExpression))
      { BSetExpression bset = (BSetExpression) val3; 
        if (bset.isSingleton())
        { val3 = bset.getElement(0); } // or make left a set?
      } 
      return ((BasicExpression) left).bEqupdateForm(env,val3,local);
    }
    else if (operator.equals(":") ||  // if rhs not basic do ANY lx WHERE lx:rhs ...
             operator.equals("<:") ||
             operator.equals("/:") ||
             operator.equals("/<:"))
    { // if (right instanceof BasicExpression)
      val2 = left.binvariantForm(env,local); // sufficient
      BExpression val3 = val2.simplify();
      BExpression val4 = BExpression.unSet(val3);   // Should never be needed
      return ((BasicExpression) right).bupdateForm(env,operator,val4,local);
    }  // Special cases if right.isOrderedB()
    else if (operator.equals("->includes"))
    { // if (right instanceof BasicExpression)
      val2 = right.binvariantForm(env,local);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      // BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,":",val3,local);
    }
    else if (operator.equals("->excludes"))
    { // if (right instanceof BasicExpression)
      val2 = right.binvariantForm(env,local);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      // BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"/:",val3,local);
    }
    else if (operator.equals("->includesAll"))
    { // if (right instanceof BasicExpression)
      val2 = right.bqueryForm(env);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"<:",val4,local);
    }
    else if (operator.equals("->excludesAll"))
    { // if (right instanceof BasicExpression)
      val2 = right.bqueryForm(env);  // binvariantForm sufficient
      BExpression val3 = val2.simplify();
      BExpression val4 = BExpression.unSet(val3); 
      return ((BasicExpression) left).bupdateForm(env,"/<:",val4,local);
    }
    else
    { System.out.println("No update form for " + this); 
      return new BBasicStatement("skip");
    }  
  }

  public boolean isMultiple()
  { if (operator.equals("\\/") || operator.equals("->union") ||
        operator.equals("->intersection") || operator.equals("->prepend") ||
        operator.equals("->including") || operator.equals("->excluding") ||
        operator.equals("->excludingAt") || 
        operator.equals("->excludingFirst") || 
        operator.equals("/\\") || operator.equals("^") || operator.equals("->append") ||
        (operator.equals("-") && left.isMultiple())) 
    { return true; }

    if (operator.equals("->select") || operator.equals("->reject") ||
        operator.equals("->closure") || operator.equals("->sortedBy") || 
        operator.equals("|") || operator.equals("|R") || operator.equals("|C") || 
        operator.equals("->collect") || operator.equals("->selectMinimals") ||
        operator.equals("->selectMaximals") || operator.equals("->unionAll") || operator.equals("->concatenateAll") || 
        operator.equals("->intersectAll") || operator.equals("->symmetricDifference") ||
        operator.equals("->split") || operator.equals("->allMatches"))
    { return true; } 

    // And map operators: 
    if (operator.equals("->restrict") || operator.equals("->antirestrict")) 
    { return true; } 

    if (operator.equals("->at") || operator.equals("->any") || operator.equals("|A"))
    { return Type.isCollectionType(left.elementType); } 

    if (operator.equals("let"))
    { return right.isMultiple(); }
	
    return false;
  }

  public boolean isSorted()
  { 
    if (operator.equals("->sortedBy")) { return true; } 

    if (operator.equals("\\/") || operator.equals("->union")) 
    { return left.isSorted() && right.isSorted(); }
    // Union of two sorted collections uses a merge sort
    // merging.  

    if (operator.equals("->intersection") ||
        operator.equals("->including") || operator.equals("->excluding") ||
        operator.equals("->excludingAt") ||
        operator.equals("->excludingFirst") || 
        operator.equals("/\\") || 
        (operator.equals("-") && left.isMultiple()) ) 
    { return left.isSorted(); }

    if (operator.equals("|") || operator.equals("|R"))
    { return ((BinaryExpression) left).right.isSorted(); }
 
    if (operator.equals("->select") || 
        operator.equals("->reject") || 
        operator.equals("->restrict") || 
        operator.equals("->antirestrict"))
    { return left.isSorted(); } 
	
    if (operator.equals("let"))
    { return right.isSorted(); }


    // if (operator.equals("->at"))
    // { return Type.isSequenceType(left.elementType); } 

    return false;
  }

  public boolean isOrdered()
  { if (operator.equals("->sortedBy") || operator.equals("->collect") ||
        operator.equals("|C") || "|concatenateAll".equals(operator)) 
    { return true; } 

    if (operator.equals("\\/") || operator.equals("->union")) 
    { return left.isOrdered() && right.isOrdered(); } 

    if (operator.equals("->intersection") || operator.equals("->prepend") ||
        operator.equals("->including") || 
        operator.equals("->excluding") || 
        operator.equals("->excludingAt") ||
        operator.equals("->excludingFirst") || 
        operator.equals("/\\") || 
        operator.equals("^") || 
        operator.equals("->append") ||
        (operator.equals("-") && left.isMultiple())) 
    { return left.isOrdered(); }

    if (operator.equals("|") || operator.equals("|R") ||
	    "|selectMinimals".equals(operator) || "|selectMaximals".equals(operator))
    { return ((BinaryExpression) left).right.isOrdered(); }
 
    if (operator.equals("->select") || operator.equals("->reject") ||
        operator.equals("->selectMinimals") ||
        operator.equals("->selectMaximals"))
    { return left.isOrdered(); } 
	
    if (operator.equals("->at") || operator.equals("->any"))
    { return Type.isSequenceType(left.elementType); } 

    if (operator.equals("|A"))
    { return Type.isSequenceType(((BinaryExpression) left).right.elementType); } 

    if (operator.equals("let"))
    { return right.isOrdered(); }

    return false;
  }

  public boolean isOrderedB()
  { 
    if (operator.equals("->sortedBy") || operator.equals("->collect") ||
        operator.equals("|C")) 
    { return true; } 

    if (operator.equals("\\/") || operator.equals("->union")) 
    { return left.isOrderedB() && right.isOrderedB(); } 

    if (operator.equals("->intersection") || operator.equals("->prepend") ||
        operator.equals("->including") || operator.equals("->excluding") ||
        operator.equals("->excludingAt") ||
        operator.equals("->excludingFirst") ||  
        operator.equals("/\\") || operator.equals("^") || operator.equals("->append") ||
        (operator.equals("-") && left.isMultiple())) 
    { return left.isOrderedB(); }

    if (operator.equals("|") || operator.equals("|R"))
    { return ((BinaryExpression) left).right.isOrderedB(); }
 
    if (operator.equals("->select") || operator.equals("->reject") ||
        operator.equals("->selectMinimals") ||
        operator.equals("->selectMaximals"))
    { return left.isOrderedB(); } 

    if (operator.equals("->at"))
    { return Type.isSequenceType(left.elementType); } 

    return false;
  }


  public BExpression binvariantForm(java.util.Map env, boolean local)
  { BExpression bel = left.binvariantForm(env,local);
    BExpression ber = right.binvariantForm(env,local);
    boolean lmultiple = left.isMultiple();
    boolean rmultiple = right.isMultiple();

    if (operator.equals("#"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      if (lexp.right.isOrderedB())
      { BExpression belr = lexp.right.binvariantForm(env,local); 
        BExpression newl = new BUnaryExpression("ran",belr); 
        bel = new BBinaryExpression(":", new BBasicExpression(x), newl);
      }  
      BExpression res = new BQuantifierExpression("exists",x,
                              new BBinaryExpression("&",bel,ber)); 
      return res; 
    } 

    if (operator.equals("!"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      // BExpression lll = lexp.left.bqueryForm(env); 
      // String t = lexp.right + ""; 
      // env1.put(t,lll); 
      if (lexp.right.isOrderedB())
      { BExpression belr = lexp.right.binvariantForm(env,local); 
        BExpression newl = new BUnaryExpression("ran",belr); 
        bel = new BBinaryExpression(":", new BBasicExpression(x), newl);
      }  
      BExpression res = new BQuantifierExpression("forall",x,
                              new BBinaryExpression("=>",bel,ber)); 
      return res; 
    } 

    if (operator.equals("#1"))
    { BinaryExpression lexp = (BinaryExpression) left; 
      String x = lexp.left + ""; 
      BExpression rng = lexp.right.binvariantForm(env,local); 
      if (lexp.right.isOrderedB())
      { rng = new BUnaryExpression("ran", rng); }  

      BExpression sel = new BSetComprehension(x,rng,ber);
      BExpression selsize = new BUnaryExpression("card",sel);   
      BExpression res = new BBinaryExpression("=",selsize,
                              new BBasicExpression("1")); 
      return res; 
    } 

    if (operator.equals("|") || operator.equals("->select") ||
        operator.equals("|R") || operator.equals("->reject"))
    { return selectBinvariantForm(bel,ber,env,local); } 

    if (operator.equals("->collect") || operator.equals("->unionAll") ||
        operator.equals("|C") || operator.equals("->intersectAll"))
    { return collectBinvariantForm(bel,ber,env,local); } 

    if (operator.equals("->forAll") || operator.equals("->exists") ||
        "->existsLC".equals(operator) || operator.equals("->exists1"))
    { return quantifierBinvariantForm(bel,ber,env,local); } 

    if (operator.equals("="))
    { if (lmultiple && !rmultiple)
      { BSetExpression se; 
        if (ber instanceof BSetExpression)
        { se = (BSetExpression) ber; } 
        else 
        { se = new BSetExpression(); 
          se.addElement(ber); 
          se.setOrdered(left.isOrderedB()); 
        } 
        BExpression res = new BBinaryExpression("=",bel,se);  // promote right to a set
        res.setBrackets(needsBracket); 
      } 
    } // and other cases, rmult && !lmult, and for /=

    if (operator.equals(":") || operator.equals("/:"))
    { if (!lmultiple && rmultiple && right.isOrderedB())
      { BExpression rr = new BUnaryExpression("ran",ber); 
        BExpression res = new BBinaryExpression(operator,bel,rr); 
        res.setBrackets(needsBracket); 
        return res; 
      }
      else if ("String".equals(right.type + ""))
      { BExpression rr = new BUnaryExpression("ran",ber); 
        BExpression res = new BBinaryExpression(operator,bel,rr); 
        res.setBrackets(needsBracket);
        return res;  
      } 
    } 
    // also for <: and /<:  if one of left or right is ordered.

    if (operator.equals("<:") || operator.equals("/<:"))
    { BExpression newl = bel; 
      BExpression newr = ber; 
      if (lmultiple && left.isOrderedB())
      { newl = new BUnaryExpression("ran",bel); } 
      if (rmultiple && right.isOrderedB())
      { newr = new BUnaryExpression("ran",ber); } 
      BExpression res = new BBinaryExpression(operator,newl,newr); 
      res.setBrackets(needsBracket); 
      return res; 
    } 
      
    if (operator.equals("->includes"))
    { if (lmultiple && !rmultiple)
      { BExpression ll = bel; 
        if (left.isOrderedB())
        { ll = new BUnaryExpression("ran",bel); }  
        BExpression res = new BBinaryExpression(":",ber,ll); 
        res.setBrackets(needsBracket); 
        return res; 
      }
      else if ("String".equals(left.type + ""))
      { BExpression ll = new BUnaryExpression("ran",bel); 
        BExpression res = new BBinaryExpression(":",ber,ll); 
        res.setBrackets(needsBracket); 
        return res; 
      } 
    } 

    if (operator.equals("->oclIsKindOf") || operator.equals("->oclIsTypeOf"))
    { BExpression es = new BBasicExpression((right + "").toLowerCase() + "s"); 
      BExpression res = new BBinaryExpression(":",bel,es); 
      res.setBrackets(needsBracket); 
      return res; 
    } 

    if (operator.equals("->at"))
    { BExpression res = new BApplyExpression(bel,ber); 
      return res; 
    } 

    if (operator.equals("->excludes"))
    { if (lmultiple && !rmultiple)
      { BExpression ll = bel; 
        if (left.isOrderedB())
        { ll = new BUnaryExpression("ran",bel); }  
        BExpression res = new BBinaryExpression("/:",ber,ll); 
        res.setBrackets(needsBracket); 
        return res; 
      }
      else if ("String".equals(left.type + ""))
      { BExpression ll = new BUnaryExpression("ran",bel); 
        BExpression res = new BBinaryExpression("/:",ber,ll); 
        res.setBrackets(needsBracket); 
        return res; 
      } 
    } 

    if (operator.equals("->isUnique"))
    { // card(r(l)) = card(l)
      BApplySetExpression rapplyl = new BApplySetExpression(ber,bel); 
      BUnaryExpression ll = new BUnaryExpression("card",rapplyl); 
      BUnaryExpression rr = new BUnaryExpression("card",bel); 
      return new BBinaryExpression("=",ll,rr); 
    } 

    String op = operator; 
    if (operator.equals("!=") || operator.equals("<>"))
    { op = "/="; }
    else if (operator.equals("->union") && left.isOrderedB() && right.isOrderedB())
    { op = "^"; } 
    else if (operator.equals("\\/") && left.isOrderedB() && right.isOrderedB())
    { op = "^"; } 
    else if (operator.equals("->union"))
    { op = "\\/"; } 
    else if (operator.equals("->intersection"))
    { op = "/\\"; } 

    if (operator.equals("->prepend"))
    { BExpression res = new BBinaryExpression("->",ber,bel); 
      res.setBrackets(needsBracket); 
      return res; 
    } 
    else if (operator.equals("->append"))
    { BExpression res = new BBinaryExpression("<-",bel,ber); 
      res.setBrackets(needsBracket); 
      return res; 
    } 
    else if (operator.equals("->indexOf"))  // min(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(ber); 
      BExpression func = new BPostfixExpression("~", bel); 
      BExpression res = new BApplySetExpression(func,img); 
      res = new BUnaryExpression("min",res); 
      return res; 
    } 
    else if (operator.equals("->lastIndexOf"))  // max(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(ber); 
      BExpression func = new BPostfixExpression("~", bel); 
      BExpression res1 = new BApplySetExpression(func,img); 
      BExpression result = new BUnaryExpression("max",res1);
      return result;  
    } 
    else if (operator.equals("->count"))  // card(bel~[{ber}]) 
    { BSetExpression img = new BSetExpression(); 
      img.addElement(ber); 
      BExpression func = new BPostfixExpression("~", bel); 
      BExpression res = new BApplySetExpression(func,img); 
      res = new BUnaryExpression("card",res); 
      return res; 
    } 
    else if (operator.equals("->including"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(ber); 
      if (left.isOrderedB())
      { BExpression resord = new BBinaryExpression("<-", bel, ber); 
        return resord; 
      } 
      BExpression res = new BBinaryExpression("\\/",bel,bset); 
      res.setBrackets(needsBracket); 
      return res; 
    } 
    else if (operator.equals("->excluding"))
    { BSetExpression bset = new BSetExpression(); 
      bset.addElement(ber); 
      BExpression res = new BBinaryExpression("-",bel,bset); 
      res.setBrackets(needsBracket); 
      return res; 
    } 
    else if (operator.equals("=") || operator.equals("-") ||
        operator.equals("\\/") || operator.equals("/\\") ||
        operator.equals("^") || operator.equals("->union") ||
        operator.equals("->intersection") ||
        operator.equals("/=") || operator.equals("!=") || operator.equals("<>"))
    { // if one of bel, ber are sets and other isn't,
      // promote the non-set to be a set:
      if (operator.equals("->union") || operator.equals("\\/"))
      { if (left.isOrderedB() && right.isOrderedB())
        { operator = "^"; } 
      } 

      if (lmultiple == rmultiple)
      { if (operator.equals("/\\") || operator.equals("\\/") ||
            operator.equals("->intersection") || operator.equals("->union") ||
            operator.equals("-") || operator.equals("^"))
        { BExpression result = new BBinaryExpression(op,bel,ber);
          if (lmultiple)
          { result.setMultiplicity(ModelElement.MANY); }
          result.setBrackets(needsBracket); 
          return result;
        }
      }  // cases of sequences not handled. 
      else if (rmultiple)
      { BExpression newbel = BExpression.makeSet(bel);
        BExpression res = new BBinaryExpression(op,newbel,ber); 
        res.setBrackets(needsBracket); 
        return res; 
      }
      else if (lmultiple)
      { BExpression newber = BExpression.makeSet(ber);
        BExpression res = new BBinaryExpression(op,bel,newber); 
        res.setBrackets(needsBracket); 
        return res; 
      }
    }
    else if (operator.equals(":"))
    { if (lmultiple)
      { BExpression res = new BBinaryExpression("<:",bel,ber); 
        res.setBrackets(needsBracket); 
        return res; 
      }
    }
    else if (operator.equals("/:"))
    { if (lmultiple) // bel intersection ber = {}
      { BExpression empty = 
          new BSetExpression(new Vector());
        BExpression res = 
          new BBinaryExpression("=",
            new BBinaryExpression("/\\",bel,ber),empty);
        res.setBrackets(needsBracket); 
        return res; 
      }
    }
    else if (comparitors.contains(operator))
    { return comparitorsBinvariantForm(op,bel,ber,lmultiple,rmultiple); } 
    BExpression res = new BBinaryExpression(op,bel,ber);
    if (objectRef != null)
    { BExpression func = objectRef.binvariantForm(env,local); 
      res.setBrackets(true); 
      return new BApplyExpression(res,func); 
    } // or ApplySet if objectRef.isMultiple()
    res.setBrackets(needsBracket); 
    return res; 
  } // ->excludingAt, ->restrict, etc. 

  private BExpression selectBinvariantForm(BExpression bel, BExpression ber, 
                                           java.util.Map env, boolean local)
  { // { var | var : bel & right.binvariantForm(env <+ { e |-> var }, false) }
    // Assume that left is not ordered. Ordered version needs to be treated. 

    Type etype = left.getElementType(); 
    String ename; 
    String var; 
    Expression predicate = right; 

    if (operator.equals("|") || operator.equals("|R"))
    { BinaryExpression beleft = (BinaryExpression) left; 
      var = beleft.left + ""; 
      etype = beleft.right.getElementType(); 
      ename = "" + etype; 
      bel = beleft.right.binvariantForm(env,local); 
    } 
    else if (etype == null) 
    { System.err.println("Warning -- null element type in select: " + this); 
      ename = "Object"; 
      var = "object_xx"; 
    } 
    else 
    { ename = "" + etype;  // should be a class
      // System.out.println("Element type of " + left + " is " + ename); 
      var = ename.toLowerCase() + "_xx"; 
    } 

    java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
    if (operator.equals("|") || operator.equals("|R")) { } 
    else { newenv.put(ename,new BBasicExpression(var)); }

    BExpression pred = predicate.binvariantForm(newenv,false);     

    if (operator.equals("->reject") || operator.equals("|R"))
    { pred = new BUnaryExpression("not",pred); } 

    if (left.isOrdered())
    { return seqselectBinvariantForm(var, bel, pred, newenv); } 

    return new BSetComprehension(var,bel,pred); 
  } // need same kind of thing with exists, forall, reject, collect, |R

  private BExpression collectBinvariantForm(BExpression bel, BExpression ber, 
                                           java.util.Map env, boolean local)
  { // { var,y | var : bel & y = right.binvariantForm(env <+ { e |-> var }, false) }
    // left not ordered: 
    // { i,y | var = bel(i) & y = right.binvariantForm(env <+ { e |-> var }, false) }
    
    Type etype = left.getElementType(); 
    String ename; 
    String var; 
    Expression collectleft = left; 

    if (operator.equals("|C"))
    { collectleft = ((BinaryExpression) left).right; 
      var = ((BinaryExpression) left).left + "";
      etype = collectleft.getElementType(); 
    } 
    else 
    { var = (etype + "").toLowerCase() + "_xx"; }

    if (etype == null) 
    { System.err.println("ERROR -- null element type in collect: " + this); 
      ename = "Object"; 
      var = "object_xx"; 
    } 
    else 
    { ename = "" + etype;  // should be a class
      // System.out.println("Element type of " + left + " is " + ename); 
    } 

    BBasicExpression varbe = new BBasicExpression(var); 

    java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 

    if (operator.equals("|C")) { } 
    else 
    { newenv.put(ename,varbe); } 

    BExpression pred = right.binvariantForm(newenv,false);     
    String var2 = "xx_value"; 
    BBasicExpression var2be = new BBasicExpression(var2); 

    Vector vars = new Vector(); 
    vars.add(var); 
    vars.add(var2); 

    pred = new BBinaryExpression("=", var2be, pred); 
    BExpression collvalue; 

    if (collectleft.isOrderedB())
    { String var0 = "xx_index"; 
      Vector vars2 = new Vector(); 
      vars2.add(var0); 
      vars2.add(var2); 
      BBasicExpression var0be = new BBasicExpression(var0); 

      BExpression dombel = new BUnaryExpression("dom",bel); 
      BExpression indombel = new BBinaryExpression(":", var0be, dombel);
      BExpression belapply = new BApplyExpression(bel, var0be); 
      BExpression eqbelapply = new BBinaryExpression("=", varbe, belapply);  
      BQuantifierExpression existsvarb = 
           new BQuantifierExpression("exists", var,
              new BBinaryExpression("&", eqbelapply, pred));         
      collvalue = new BSetComprehension(vars, 
                       new BBinaryExpression("&",indombel,existsvarb));
    } 
    else 
    { BExpression rng = new BBinaryExpression(":",varbe,bel); 
      pred = new BBinaryExpression("&",rng,pred); 
      BSetComprehension collexp = new BSetComprehension(vars,pred);
      collvalue = new BUnaryExpression("ran", collexp);
    }  

    if (operator.equals("->collect") || operator.equals("|C"))
    { return collvalue; } 
    else if (operator.equals("->unionAll") & left.isOrderedB() && 
             (right.isOrderedB() || "self".equals(right + "")))
    { return new BUnaryExpression("dcat",collvalue); } 
    else if (operator.equals("->unionAll"))
    { if (left.isOrderedB())
      { collvalue = new BUnaryExpression("ran", collvalue); } 
      return new BUnaryExpression("union",collvalue); 
    } 
    else 
    { if (left.isOrderedB())
      { collvalue = new BUnaryExpression("ran", collvalue); } 
      return new BUnaryExpression("inter",collvalue); 
    }   
  } // need same kind of thing with exists, forall, reject, collect, |C


  private BExpression quantifierBinvariantForm(BExpression bel, BExpression ber, 
                                           java.util.Map env, boolean local)
  { // { var | var : bel & right.binvariantForm(env <+ { e |-> var }, false) } = ?
    // for ordered use ran(bel)

    Type etype = left.getElementType(); 
    String ename; 
    String var; 
    if (etype == null) 
    { System.err.println("Warning -- null element type in quantifier: " + this); 
      ename = "Object"; 
      var = "object_xx"; 
    } 
    else 
    { ename = "" + etype;  // should be a class
      System.out.println("Element type of " + left + " is " + ename); 
      var = ename.toLowerCase() + "_xx"; 
    } 
    java.util.Map newenv = (java.util.Map) ((java.util.HashMap) env).clone(); 
    newenv.put(ename,new BBasicExpression(var)); 
    BExpression pred = right.binvariantForm(newenv,false);     

    if (left.isOrderedB())
    { bel = new BUnaryExpression("ran",bel); } 

    BSetComprehension satisfiesPred = new BSetComprehension(var,bel,pred);

    BExpression result; 

    if (operator.equals("->forAll"))
    { result = new BBinaryExpression("=",satisfiesPred,bel); }
    else 
    { BExpression cardset = new BUnaryExpression("card",satisfiesPred); 
      if ("->existsLC".equals(operator) || operator.equals("->exists"))
      { result = new BBinaryExpression(">",cardset,new BBasicExpression("0")); } 
      else 
      { result = new BBinaryExpression("=",cardset,new BBasicExpression("1")); } 
    }  

    return result; 
  } 

  private BExpression comparitorsBinvariantForm(String op,
                                                BExpression bel, BExpression ber,
                                                boolean lmult, boolean rmult)
  { if (!lmult && !rmult)
    { return new BBinaryExpression(op,bel,ber); }
    if (lmult && !rmult)
    { // !xx.(xx: bel => xx operator ber)  Gen new variable xx
      String xx = Identifier.nextIdentifier("xx_var"); 
      BExpression bxx = new BBasicExpression(xx); 
      BExpression compar = new BBinaryExpression(op,bxx,ber); 
      BExpression inexp = new BBinaryExpression(":",bxx,bel); 
      BExpression pred = new BBinaryExpression("=>",inexp,compar); 
      return new BQuantifierExpression("forall",xx,pred); 
    } 
    if (rmult && !lmult)
    { // !xx.(xx: ber => bel operator xx)  Gen new variable xx
      String xx = Identifier.nextIdentifier("xx_var"); 
      BExpression bxx = new BBasicExpression(xx); 
      BExpression compar = new BBinaryExpression(op,bel,bxx); 
      BExpression inexp = new BBinaryExpression(":",bxx,ber); 
      BExpression pred = new BBinaryExpression("=>",inexp,compar); 
      return new BQuantifierExpression("forall",xx,pred); 
    } 
    // !(xx,yy).(xx: bel & yy : ber => xx operator yy)  Gen xx, yy
    String xx = Identifier.nextIdentifier("xx_var"); 
    String yy = Identifier.nextIdentifier("xx_var"); 
    BExpression bxx = new BBasicExpression(xx);
    BExpression byy = new BBasicExpression(yy);  
    BExpression compar = new BBinaryExpression(op,bxx,byy); 
    BExpression inexp1 = new BBinaryExpression(":",bxx,bel); 
    BExpression inexp2 = new BBinaryExpression(":",byy,ber); 
    BExpression pred1 = new BBinaryExpression("=>",inexp2,compar); 
    BExpression pred2 = new BQuantifierExpression("forall",yy,pred1); 
    BExpression pred3 = new BBinaryExpression("=>",inexp1,pred2); 
    return new BQuantifierExpression("forall",xx,pred3); 
  } 
      
  public int typeCheckEq(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0) // Assume bel is the component/att.
      { leftcat = lcat;
        bel.kind = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
        bel.mult = lmult;
        rightcat = sm.getCategory(ber.data);
        ber.kind = rightcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
        ber.mult = rmult;
        break;
      }
    }

    if (leftcat == Statemachine.STATEMACHINE &&
        rightcat == Statemachine.STATE)
    { modality = leftsm.cType;
      if (lmult <= 1 && bel.objectRef != null)
      { System.err.println("Object ref specified " +
          "for non-multiple component: " +
          bel);
        modality = ERROR;
        return ERROR;
      }
      return modality;
    }

    // swap left and right if rightcat == ATTRIBUTE
    if (leftcat == Statemachine.ATTRIBUTE &&
        leftsm.getRange(bel.data).contains(ber.data))
    { modality = leftsm.cType;
      if (lmult <= 1 && bel.objectRef != null)
      { System.err.println("Object ref specified " +
          "for non-multiple component: " +
          bel);
        modality = ERROR;
        return ERROR;
      } // also check ber.data is an integer in range
      return modality;
    }

    if (leftsm == null)
    { System.err.println("I can't find a component " +
                         "for: " + bel);
    }
    else
    { System.err.println("Strange combination in equality " +
                         toString()); 
    }
    modality = ERROR;
    return ERROR;
  }


  public int typeCheckAnd(Vector sms)
  { int tal = left.typeCheck(sms);
    int tar = right.typeCheck(sms);
    if ((tal == ERROR) || (tar == ERROR))
    { modality = ERROR;
      return ERROR; 
    }
    if ((tal == HASEVENT && tar != HASEVENT) ||
        (tal != HASEVENT && tar == HASEVENT)) 
    { modality = HASEVENT; 
      return HASEVENT; 
    } 
    if (tal != HASEVENT && tal == tar)
    { modality = tal; 
      return tal; 
    }
    else 
    { modality = MIXED; 
      return MIXED; 
    }
  }

  public Expression createActionForm(final Vector sms)
  { if ((left instanceof BasicExpression) &&
        (right instanceof BasicExpression) &&
        operator.equals("="))
    { return createEqActionForm(sms); }
    else if (operator.equals("&")) // should never happen
    { Expression lact = left.createActionForm(sms);
      Expression ract = right.createActionForm(sms);
      Expression actionForm =
        new BinaryExpression("||",lact,ract);
      javaForm =
        new BinaryExpression(";",lact.javaForm,
                                 ract.javaForm);
      actionForm.javaForm = javaForm;
      return actionForm;
    }
    return this;
  }

  private Expression createEqActionForm(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0)
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }
    if (rightcat == Statemachine.STATEMACHINE)
    { int temp = rightcat;  // swap left and right -- 
      rightcat = leftcat;   // should already have happened in 
      leftcat = temp;       // normalisation. 
      temp = rmult;
      rmult = lmult;
      lmult = temp;
      Statemachine stemp = rightsm;
      rightsm = leftsm;
      leftsm = stemp;
    }

    if (leftcat == Statemachine.STATEMACHINE &&
        rightcat == Statemachine.STATE)
    { String acts = bel.data + "Set" + ber.data;
      BasicExpression act = new BasicExpression(acts);
      if (lmult <= 1)
      { javaForm =
          new BasicExpression("Actuator" + bel.data +
                              "." + acts + "()");
        act.javaForm = javaForm;
        return act;
      }
      else if (bel.objectRef != null)
      { act.objectRef =
          (Expression) bel.objectRef.clone();
        // act1 = new BasicExpression(act.toB()); 
        javaForm = new BasicExpression(act.toJava() + "()"); // act
        // javaForm.objectRef =
        //  (Expression) bel.objectRef.clone();
        act.javaForm = javaForm;  // act1
        return act;               // act1
      }
      else  // shouldn't happen
      { return new BasicExpression(bel.data +
                     "SetAll(" + ber.data + ")");
      } // in Java ; of each Set.
    }

    // swap left and right if rightcat == ATTRIBUTE
    if (leftcat == Statemachine.ATTRIBUTE &&
        leftsm.getRange(bel.data).contains(ber.data))
    { String attset = "set" + bel.data +
                      "(" + ber.data + ")";
      if (lmult <= 1)
      { BasicExpression act2 =
          new BasicExpression(attset);
        javaForm =
          new BasicExpression("M" + leftsm.label +
                "." + attset);
        act2.javaForm = javaForm;
        return act2;
      }
      else if (bel.objectRef != null)
      { BasicExpression act3 =
          new BasicExpression("set" + bel.data +
                     "(" + bel.objectRef.toB() + "," +
                     ber.data + ")");
        BasicExpression jf = new BasicExpression(attset);
        jf.objectRef =
          (Expression) bel.objectRef.clone();
        javaForm = new BasicExpression(jf.toJava()); 
        act3.javaForm = javaForm; 
        return act3;
      }
      else // shouldn't happen
      { return new BasicExpression("setAll" +
                     bel.data + "(" + ber.data + ")");
      }
    }

    System.err.println("I can't produce a sensible " +
          "action form from: " + bel + " = " + ber);
    return this;
  }

  public Scope resultScope()
  { if (left.equalsString("result") &&
        operator.equals(":"))
    { return new Scope("in",this); } // this formula then ignored
                                     // in update.
    if (right.equalsString("result") &&
        operator.equals("->includes"))
    { return new Scope("in",new BinaryExpression(":",right,left)); } 

    if (left.equalsString("result") &&
        operator.equals("<:"))
    { return new Scope("subset",this); }

    if (right.equalsString("result") &&
        operator.equals("->includesAll"))
    { return new Scope("subset", new BinaryExpression("subset",right,left)); }

    if (left.equalsString("result") &&
        operator.equals("="))
    { return new Scope("eq",this); }
    if (right.equalsString("result") &&
        operator.equals("="))
    { return new Scope("eq",reverse()); } 
            // removeExpression must remove these as well
    if (left instanceof BasicExpression) 
    { BasicExpression lbe = (BasicExpression) left; 
      if ("result".equals(lbe.arrayIndex + ""))
      { return new Scope("array",this,lbe); } 
    } 
    if (right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right; 
      if ("result".equals(rbe.arrayIndex + ""))
      { return new Scope("array",this,rbe); } 
    } 
    if (operator.equals("&"))
    { Scope res1 = left.resultScope();
      Scope res2 = right.resultScope();
      if (res1 != null) 
      { // return (BinaryExpression) Expression.simplify("&",res1,res2,
        //
        return res1;
      } 
      else 
      { return res2; } 
    }
    if (operator.equals("=>") || operator.equals("#") || operator.equals("#1") ||
        operator.equals("#LC") || operator.equals("!"))
    { return right.resultScope(); }  // this ? 
    return null;
  }
  // also case where result occurs as an array index: 
  // be[result]

  public Expression simplify(final Vector vars) 
  { Expression lsimp = left.simplify(vars);
    Expression rsimp = right.simplify(vars);
    return simplify(operator,lsimp,rsimp,vars); 
  }

  public Expression simplify() 
  { Expression lsimp = left.simplify();
    Expression rsimp = right.simplify();
    return simplify(operator,lsimp,rsimp,needsBracket); 
  }

  public Expression substitute(final Expression oldE,
                               final Expression newE)
  { if (operator.equals("#") || operator.equals("#1") || operator.equals("!") || operator.equals("|A") ||
        operator.equals("#LC") || operator.equals("|") || operator.equals("|C") || operator.equals("|R"))
    { Expression var = ((BinaryExpression) left).left; 
      Vector vars = oldE.getVariableUses();
      Vector vars1 = newE.getVariableUses(); 
      if (VectorUtil.containsEqualString(var + "",vars) ||
          VectorUtil.containsEqualString(var + "",vars1))
      { return this; }
      else 
      { Expression newR = right.substitute(oldE,newE);
        Expression newL = left.substitute(oldE,newE);   // Added 9.1.2016
        Expression result = new BinaryExpression(operator,newL,newR); 
        result.formalParameter = formalParameter; 
        result.setBrackets(needsBracket); 
        return result; 
      } // Surely should substitute in left.right ??? 
    } 

    if (oldE == left)
    { Expression newR = right.substitute(oldE,newE);
      Expression result = new BinaryExpression(operator,newE,newR);
      result.formalParameter = formalParameter; 
      result.setBrackets(needsBracket); 
      return result; 
    }
    else if (oldE == right)
    { Expression newL = left.substitute(oldE,newE);
      Expression result = new BinaryExpression(operator,newL,newE);
      result.formalParameter = formalParameter; 
      result.setBrackets(needsBracket); 
      return result; 
    }
    else 
    { Expression newLeft = left.substitute(oldE,newE);
      Expression newRight = right.substitute(oldE,newE);
      Expression result = new BinaryExpression(operator, newLeft, newRight); 
      result.formalParameter = formalParameter; 
      result.setBrackets(needsBracket); 
      return result; 
    }
  }  // What if oldE == left and right? 

  public Expression substituteEq(final String oldVar, 
                                 final Expression newVal)
  { // if (oldVar.equals(left.toString()))
    // { return new BinaryExpression(operator,newVal,right); }
    // else if (oldVar.equals(right.toString()))
    // { return new BinaryExpression(operator,left,newVal); }
    // else
    // { System.out.println("left is: " + left);

    // System.out.println("SUBSTITUTING " + oldVar + " BY " + newVal + " IN " + this); 

    Expression newLeft = null;
    if (left != null)
    { newLeft = left.substituteEq(oldVar,newVal); }
    // System.out.println("after substitution: " + newLeft); 
    // System.out.println("right is: " + right); 
    Expression newRight = null;
    if (right != null)
    { newRight = right.substituteEq(oldVar,newVal); }

    /* if (operator.equals("#") || operator.equals("#1") || operator.equals("!") ||
        operator.equals("|") || operator.equals("|R") || operator.equals("|C"))
    { Expression var = ((BinaryExpression) left).left; 
      Vector vars1 = newVal.getVariableUses(); 
      if (VectorUtil.containsEqualString(var + "",vars1)) 
      { return this; } 
      else if ((var + "").equals(oldVar))
      { Expression scope = ((BinaryExpression) left).right; 
        Expression scopesub = scope.substituteEq(oldVar,newVal); 
        Expression res1 = new BinaryExpression(":",var,scopesub); 
        return new BinaryExpression(operator,res1,right); 
      }
      else  
      { Expression result = new BinaryExpression(operator,newLeft,newRight);
        if (needsBracket) 
        { result.setBrackets(true); } 
        return result; 
      }
    } */ 

    Expression res = new BinaryExpression(operator,newLeft,newRight);
    res.formalParameter = formalParameter; 
    res.setBrackets(needsBracket);  
    // System.out.println("***** " + this + " after substitution =  " + res); 
    return res; 
    // }
  }  // what if oldVar equals left and right? 

  public boolean hasVariable(final String s)
  { return (left.hasVariable(s) || right.hasVariable(s)); }

  public Vector variablesUsedIn(final Vector vars)
  { Vector res1 = left.variablesUsedIn(vars);
    Vector res2 = right.variablesUsedIn(vars);
    return VectorUtil.vector_merge(res1,res2); 
  }

  public Vector componentsUsedIn(final Vector sms)
  { Vector res1 = left.componentsUsedIn(sms);
    Vector res2 = right.componentsUsedIn(sms);
    return VectorUtil.union(res1,res2);
  }

  public boolean selfConsistent(final Vector vars) 
  { Vector res1 = left.variablesUsedIn(vars);
    // System.out.println("Lefts are: " + res1); 
    Vector res2 = right.variablesUsedIn(vars);
    // System.out.println("Rights are: " + res2); 
    Vector inter = VectorUtil.intersection(res1, res2);
    // System.out.println("Intersection: " + inter);  
    if (inter.size() > 0)
    { System.out.println("Duplicate variables in operands of " + toString()); 
      return false; 
    } 
    return true; 
  } 

  public Maplet findSubexp(final String var)
  { if (operator.equals("="))
    { if ((left instanceof BasicExpression) &&
          left.toString().equals(var))
      { return new Maplet(this,null); }
      if ((right instanceof BasicExpression) &&
          right.toString().equals(var))
      { return new Maplet(this,null); }
      else 
      { return new Maplet(null,this); } 
    }
    else /* Operator is & */
    { Maplet f1 = left.findSubexp(var);
      Maplet f2 = right.findSubexp(var); 
      Expression exp = simplify(operator,(Expression) f1.dest, 
                                (Expression) f2.dest, null); 
      if (f1.source == null)  
      { return new Maplet(f2.source, exp); }
      else
      { return new Maplet(f1.source, exp); } 
    }
    /* Don't expect to find more than one! */
  }

  public Expression filter(final Vector vars)
  { if (operator.equals("="))
    { return filterEquality(left,right,this,vars); }
    else if (operator.equals("&"))
    { Expression e1 = left.filter(vars);
      Expression e2 = right.filter(vars);
      return simplify("&",e1,e2, vars); 
    }   /* vars should be redundant */ 
    else { return this; } 
  }



  public Vector splitAnd(Vector sms) 
  { Vector res = new Vector(); 
    if (operator.equals("&"))
    { Vector res1 = left.splitAnd(sms); 
      Vector res2 = right.splitAnd(sms); 
      res = VectorUtil.vector_merge(res1,res2); 
      return res; 
    } 
    else if (operator.equals("="))
    { return expandEqSucc(sms); } 
    else 
    { System.out.println("Expression in succedent without = or &!"); 
      return res; 
    } 
  } 

    // Also deals with operator being "<", etc
  private Vector expandEqAnte(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;
    Vector res = new Vector();

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0)
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }

    if ((leftcat == Statemachine.ATTRIBUTE ||
         leftcat == Statemachine.STATEMACHINE) &&
        (rightcat == Statemachine.ATTRIBUTE ||
         rightcat == Statemachine.STATEMACHINE))
    { Vector range1 = leftsm.getRange(bel.data);
      Vector range2 = rightsm.getRange(ber.data);
      Vector inter =
      //  VectorUtil.intersection(range1,range2);
          VectorUtil.satisfies(operator,range1,range2);
      for (int i = 0; i < inter.size(); i++)
      { Maplet val = (Maplet) inter.get(i);
        BasicExpression bsc1 =
          new BasicExpression((String) val.source);
        BasicExpression bsc2 =
          new BasicExpression((String) val.dest);
        BinaryExpression be1 =
          new BinaryExpression("=",bel,bsc1);
        BinaryExpression be2 =
          new BinaryExpression("=",ber,bsc2);
        BinaryExpression andexp =
          new BinaryExpression("&",be1,be2);
        res.add(andexp);
      }
    }
    else if ((leftcat == Statemachine.ATTRIBUTE ||
              leftcat == Statemachine.STATEMACHINE) && 
              Expression.isNumber(ber.data))
    { // it is var < val, etc. 
      Vector range1 = leftsm.getRange(bel.data);
      Vector range2 = new Vector(); 
      range2.add(ber.data); 
      Vector inter = VectorUtil.satisfies(operator,range1,range2);
      for (int i = 0; i < inter.size(); i++)
      { Maplet val = (Maplet) inter.get(i);
        BasicExpression bsc1 =
          new BasicExpression((String) val.source);
        BinaryExpression be1 =
          new BinaryExpression("=",bel,bsc1);
        res.add(be1); 
      } 
    } 
    else if (leftcat != Statemachine.ATTRIBUTE &&
             leftcat != Statemachine.STATEMACHINE) 
    { res.add(reverse()); } 
    else 
    { res.add(clone()); }
    return res;
  }  // splitOr, for antecedent.
  // Danger in that bel, ber are not cloned? 

  public Vector splitOr(final Vector components)
  { Vector res = new Vector();
    if (operator.equals("&"))
    { Vector lefts = left.splitOr(components);
      Vector rights = right.splitOr(components);
      for (int i = 0; i < lefts.size(); i++)
      { Expression e1 = (Expression) lefts.get(i);
        for (int j = 0; j < rights.size(); j++)
        { Expression e2 = (Expression) rights.get(j);
          Expression e = new BinaryExpression("&",e1,e2);
          res.add(e); 
        }
      }
    }
    else if (operator.equals("or"))
    { Vector lefts = left.splitOr(components);
      Vector rights = right.splitOr(components);
      res = VectorUtil.union(lefts,rights); 
    }
    else if (operator.equals("!="))
    { Expression be = new BinaryExpression("=",left,right);
      res = be.computeNegation4ante(components); 
      /* if (res == null || res.size() == 0) 
         issue warning. */ 
    }
    else if (comparitors.contains(operator))
    { res = expandEqAnte(components); }   
    return res; 
  }

  public Expression expandMultiples(Vector sms)
  { if (operator.equals("="))
    { return expandEqMultiples(sms); }
    if (operator.equals("&"))
    { Expression newleft =
        left.expandMultiples(sms);
      Expression newright =
        right.expandMultiples(sms);
      return simplifyAnd(newleft,newright);
    }
    else
    { return this; }
  }

  private Vector expandEqSucc(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;
    Vector res = new Vector();

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0) 
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }
    if ((leftcat == Statemachine.ATTRIBUTE ||
         leftcat == Statemachine.STATEMACHINE) &&
        (rightcat == Statemachine.ATTRIBUTE ||
         rightcat == Statemachine.STATEMACHINE))
    { Vector range1 = leftsm.getRange(bel.data);
      Vector range2 = rightsm.getRange(ber.data);
      Vector inter =
        VectorUtil.intersection(range1,range2);
      for (int i = 0; i < inter.size(); i++)
      { String val = (String) inter.get(i);
        BasicExpression bsc1 = 
          new BasicExpression(val);
        BasicExpression bsc2 = 
          new BasicExpression(val);
        BinaryExpression be1 = 
          new BinaryExpression("=",bel,bsc1);
        BinaryExpression be2 =
          new BinaryExpression("=",ber,bsc2);
        BinaryExpression andexp;
        if (leftsm.cType == DCFDArea.SENSOR)
        { andexp = new BinaryExpression("=>",be1,be2); }
        else if (rightsm.cType == DCFDArea.SENSOR)
        { andexp = new BinaryExpression("=>",be2,be1); }
        else // ACTACT
        { andexp = new BinaryExpression("=>",be1,be2); }   
        res.add(andexp);
      } // Only correct if ranges are actually the same
    }
    else if (leftcat != Statemachine.ATTRIBUTE && 
             leftcat != Statemachine.STATEMACHINE) 
    { res.add(reverse()); } 
    else 
    { res.add(clone()); }
    return res;
  }  // splitAnd, for succedent.

  private Expression expandEqMultiples(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;
    Expression res = null;

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0) 
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }
    if ((leftcat == Statemachine.ATTRIBUTE ||
         leftcat == Statemachine.STATEMACHINE) &&
        leftsm.getMultiplicity() > 1 &&
        bel.objectRef == null)
    { Vector insts = leftsm.getInstances();
      for (int i = 0; i < insts.size(); i++)
      { String val = (String) insts.get(i);
        BasicExpression bsc1 = 
          (BasicExpression) bel.clone();
        bsc1.objectRef = new BasicExpression(val);
        BasicExpression bsc2 = 
          (BasicExpression) ber.clone();
        BinaryExpression be1 = 
          new BinaryExpression("=",bsc1,bsc2);
        if (res == null)
        { res = be1; }
        else
        { res =
            new BinaryExpression("&",res,be1);
        }
      }
    }
    else
    { res = this; }
    return res;
  }

  
  public Expression removeExpression(final Expression e)
  { /* Assumes e is of form  v = x */
    if (e == null)
    { return this; }
    if (("" + this).equals("" + e))
    { return null; } 
    if (operator.equals("=>") || "->existsLC".equals(operator) ||
        operator.equals("->exists") || operator.equals("->exists1") ||
        operator.equals("->forAll") || operator.equals("#") ||
        operator.equals("#1") || operator.equals("#LC") || operator.equals("!"))
    { Expression succ = right.removeExpression(e);
      return new BinaryExpression(operator,left,succ); 
    } 

    if (e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e;
      if (be.operator.equals("&"))
      { Expression rleft = removeExpression(be.left);
        Expression rright = removeExpression(be.right);
        return simplify("&",rleft,rright,new Vector());
      }
     // if (be.operator.equals("="))
     // { Expression rem1 = removeExpression(be);
        // return rem1.removeExpression(be.reverse());
     //   return rem1; // endless loop if be is its own reverse
     // }
    }
    if (e.equals(left))
    { return right.removeExpression(e); }
    else if (e.equals(right))
    { return left.removeExpression(e); }
    else 
    { Expression res1 = left.removeExpression(e);
      Expression res2 = right.removeExpression(e);
      return simplify(operator,res1,res2, new Vector()); 
    }  /* or null? */ 
  }

  public boolean implies(final Expression e)
  { if (equals(e)) { return true; }
    if (e.equalsString("true")) { return true; } 
    if (equalsString("false")) { return true; } 
    if (operator.equals("&"))
    { if (e instanceof BinaryExpression)
      { BinaryExpression be = (BinaryExpression) e;
        if (comparitors.contains(be.operator)) // or other non-logical ops
        { boolean res1 = left.implies(be);
          boolean res2 = right.implies(be);
          return (res1 || res2); 
        }
        else if (be.operator.equals("&"))
        { boolean res1 = implies(be.left);
          boolean res2 = implies(be.right);
          return (res1 && res2); 
        } 
        else if (be.operator.equals("or"))
        { boolean res1 = implies(be.left);
          boolean res2 = implies(be.right);
          return (res1 || res2); 
        } 
        else if (be.operator.equals("=>"))
        { return implies(be.right); } 
        else 
        { return false; } 
      }
      else if (e instanceof BasicExpression)
      { return (left.implies(e) || right.implies(e)); }
      else 
      { return false; } 
    }
    else if (operator.equals("="))
    { if (e instanceof BinaryExpression)
      { BinaryExpression be = (BinaryExpression) e;
        if (comparitors.contains(be.operator) || be.operator.equals("<:") ||
            be.operator.equals("->includesAll"))
        { return impliesEq(be.operator,be); } 
        else if (be.operator.equals("&"))
        { return (implies(be.left) && implies(be.right)); } 
      }
      else if (e instanceof BasicExpression)
      { return false; }
      else 
      { return false; }  
    }
    else if (operator.equals("<") || operator.equals(">"))
    { if (e instanceof BinaryExpression)
      { BinaryExpression be = (BinaryExpression) e;
        if (comparitors.contains(be.operator))
        { return impliesLG(be.operator,be); } 
        else if (be.operator.equals("&"))
        { return (implies(be.left) && implies(be.right)); } 
      }
      return false;
    }
    else { return false; }
    return false; 
  } // and cases of  x > y  =>  x >= y  etc

  private boolean impliesEq(String op, BinaryExpression be)
  { if (op.equals("="))
    { return (equals(be) || equals(be.reverse())); }
    else if (op.equals("<=") || op.equals(">=") || op.equals("<:") ||
             op.equals("->includesAll"))
    { return (left.equals(be.left) && right.equals(be.right)) || 
             (right.equals(be.left) && left.equals(be.right));  
    }
    return false; 
  } 

  private boolean impliesLG(String op, BinaryExpression be)
  { if (operator.equals("<"))
    { if (op.equals("/=") || op.equals("!=") || operator.equals("<>"))
      { return ((left.equals(be.left) && right.equals(be.right)) ||
                (right.equals(be.left) && left.equals(be.right))); 
      }
      if (op.equals("<="))
      { return left.equals(be.left) && right.equals(be.right); }
      if (op.equals(">="))
      { return left.equals(be.right) && right.equals(be.left); }
      return false;
    }
    if (operator.equals(">"))
    { if (op.equals("/=") || op.equals("!=") || operator.equals("<>"))
      { return ((left.equals(be.left) && right.equals(be.right)) ||
                (right.equals(be.left) && left.equals(be.right))); 
      }
      if (op.equals(">="))
      { return left.equals(be.left) && right.equals(be.right); }
      if (op.equals("<="))
      { return left.equals(be.right) && right.equals(be.left); }
      return false;
    }
    return false; 
  } 

  private BinaryExpression reverse()
  { return new BinaryExpression(operator,right,left); }

  public boolean consistentWith(Expression e)
  { if (equals(e))  { return true; }
    if (operator.equals("&"))
    { return (left.consistentWith(e) ||
                 right.consistentWith(e)); }
    else if (operator.equals("="))
    { return subformulaOf(e); }
    else if (comparitors.contains(operator) && e instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) e; 
      if (left.equals(be.left) && right.equals(be.right))
      { return consistentOps(operator,be.operator); } 
      if (left.equals(be.right) && right.equals(be.left))
      { return consistentOps(operator,negateOp(be.operator)); } // ??
    }
    { return false; }
  } // and cases of  x > y  cw  x >= y  etc, = with <:

  // private boolean consistentWithEq(Expression e) 
  // { if 

  private boolean consistentOps(String op1, String op2)
  { if (op1.equals("<"))
    { return (op2.equals("<=") || op2.equals("/=") ||
              op2.equals("!="));
    }
    if (op1.equals(">"))
    { return (op2.equals(">=") || op2.equals("/=") ||
              op2.equals("!="));
    }
    if (op1.equals("="))
    { return (op2.equals("<=") || op2.equals(">=") ||
              op2.equals("<:") || op2.equals("->includesAll"));
    }
    if (op1.equals("/=") || op1.equals("!=") || operator.equals("<>"))
    { return (op2.equals("<") || op2.equals("/=") ||
              op2.equals("/<:") || op2.equals("->excludesAll") ||   // added 9.1.2016
              op2.equals(">") || op2.equals("!=") || op2.equals("<>"));
    }
    if (op1.equals("<="))
    { return (op2.equals("<") || op2.equals(">=") ||
              op2.equals("="));
    }
    if (op1.equals(">="))
    { return (op2.equals(">") || op2.equals("<=") ||
              op2.equals("="));
    }
    if (op1.equals("<:") || op1.equals("->includesAll"))
    { return op2.equals("="); }
    if (op1.equals("/<:") || op1.equals("->excludesAll"))
    { return op2.equals("/=") || op2.equals("!=") || op2.equals("<>"); }
    return false;
  }


  public boolean subformulaOf(final Expression e)
  { /* Assume operator is "=" */ // if (e.equals(this)) return true; 
   if (e instanceof BinaryExpression)
   { BinaryExpression be = (BinaryExpression) e;
     if (be.operator.equals("="))
     { return (equals(be) || equals(be.reverse())); }
     else if (be.operator.equals("&"))
     { return (subformulaOf(be.left) || subformulaOf(be.right)); }
     else if (be.operator.equals("#") || "->existsLC".equals(operator) || 
              be.operator.equals("->exists") || operator.equals("#LC") ||
              be.operator.equals("#1") || be.operator.equals("->exists1") ||
              be.operator.equals("->forAll") || be.operator.equals("!"))
     { return subformulaOf(be.right); }  // and if be.operator.equals("=>") ???
     else { return false; }
   }
   else 
   { return false; }
  }

  public Expression computeNegation4succ()
  { if (operator.equals("="))
    { return computeEqNegation4succ(); }
    if (operator.equals("&"))
    { // System.err.println("Can't negate: " + toString() +
      //                   " for succ.");
      Expression notB = right.computeNegation4succ(); 
      Expression res = new BinaryExpression("=>",left,notB);
      res.setBrackets(needsBracket); 
      return res; 
    }  // not(A & B) is A => not(B)
    else if (operator.equals("=>"))
    { Expression notB = right.computeNegation4succ(); 
      Expression res = new BinaryExpression("&",left,notB);
      res.setBrackets(needsBracket); 
      return res; 
    }  // not(A => B) is A & not(B)
    else if (operator.equals("#") || operator.equals("!") ||
             operator.equals("->existsLC") || "->exists".equals(operator) ||
             operator.equals("->forAll"))
    { Expression rneg = right.computeNegation4succ(); 
      String op = invertOp(operator); 
      Expression result = new BinaryExpression(op,left,rneg);
      result.setBrackets(needsBracket); 
      return result;
    }  
    else if (operator.equals("->includesAll"))
    { Expression result = new BinaryExpression("/<:",right,left);
      result.setBrackets(needsBracket); 
      return result;
    } 
    else if (operator.equals("->excludesAll"))
    { Expression inter = new BinaryExpression("/\\",left,right); 
      Expression result = new UnaryExpression("->notEmpty",inter);
      result.setBrackets(needsBracket); 
      return result;
    } 
    else if (operator.equals("or"))  // added 9.1.2016 
    { Expression notA = left.computeNegation4succ();  
      Expression notB = right.computeNegation4succ(); 
      Expression res = new BinaryExpression("&",notA,notB);
      res.setBrackets(needsBracket); 
      return res;
    } 
      
    Expression result; 
    if (comparitors.contains(operator) || ":".equals(operator) || 
        "/:".equals(operator) || "<:".equals(operator) || "/<:".equals(operator) ||
        "->includes".equals(operator) || "->excludes".equals(operator))
    { String op = negateOp(operator);
      result = new BinaryExpression(op,left,right);
    } 
    else 
    { result = new UnaryExpression("not", this); } 
    result.setBrackets(needsBracket); 
    return result; 
  } // but not for +, -, etc 

  private Expression computeEqNegation4succ()
  { if (right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      if (rbe.umlkind == VALUE)
      { if (rbe.data.equals("true"))
        { Expression newright = new BasicExpression("false"); 
          return new BinaryExpression("=",left,newright); 
        }
        if (rbe.data.equals("false"))
        { Expression newright = new BasicExpression("true"); 
          return new BinaryExpression("=",left,newright); 
        } 
        Type rt = rbe.getType();
        Vector vals = rt.getValues();
        if (vals != null)
        { Vector newvals = (Vector) vals.clone();
          newvals.remove(rbe.data);
          if (newvals.size() == 1)
          { String other = (String) newvals.get(0);
            BasicExpression otherbe = new BasicExpression(other);
            if (left instanceof BasicExpression)
            { BasicExpression lbe = (BasicExpression) left; 
              if (lbe.objectRef != null && 
                  lbe.objectRef.isMultiple())
              { return new BinaryExpression(":",otherbe,left); } 
            }
            return new BinaryExpression("=",left,otherbe); 
          }
          else 
          { SetExpression se = 
              new SetExpression(newvals); 
            if (left instanceof BasicExpression)
            { BasicExpression lbe = (BasicExpression) left; 
              if (lbe.objectRef != null && 
                  lbe.objectRef.isMultiple())
              { SetExpression empty = new SetExpression(); 
                Expression inter = new BinaryExpression("/\\",left,se); 
                return new BinaryExpression("/=",inter,empty); 
              } 
            }
            return new BinaryExpression(":",left,se);
          }
        }
      }
    }
    return new BinaryExpression("/=",left,right);
  }  // But non-basics can also be multiple ...

  public Vector computeNegation4ante()
  { if (operator.equals("="))
    { return computeEqNegation4ante(); }
    else if (operator.equals("&"))
    { Vector res1 = left.computeNegation4ante();
      Vector res2 = right.computeNegation4ante();
      return VectorUtil.vector_merge(res1,res2);
    }
    else if (operator.equals("!=") || operator.equals("/=") || operator.equals("<>"))
    { Vector res = new Vector();
      res.add(new BinaryExpression("=",left,right));
      return res;
    }
    Vector res = new Vector();
      
    // negate the op. 
    Expression result; 
    if (comparitors.contains(operator) || ":".equals(operator) || 
        "/:".equals(operator) || "<:".equals(operator) || "/<:".equals(operator) ||
        "->includes".equals(operator) || "->excludes".equals(operator))
    { String op = negateOp(operator);
      result = new BinaryExpression(op,left,right);
    } 
    else 
    { result = new UnaryExpression("not", this); } 
    result.setBrackets(needsBracket); 
    res.add(result);

    return res;
  }

  private Vector computeEqNegation4ante()
  { Vector res = new Vector(); 
    if (right instanceof BasicExpression)
    { BasicExpression rbe = (BasicExpression) right;
      if (rbe.umlkind == VALUE)
      { if (rbe.data.equals("true"))
        { Expression newright = new BasicExpression("false");
          res.add(new BinaryExpression("=",left,newright));
          return res; 
        }
        if (rbe.data.equals("false"))
        { Expression newright = new BasicExpression("true");
          res.add(new BinaryExpression("=",left,newright));
          return res; 
        }
        Type rt = rbe.getType();
        Vector vals = rt.getValues();
        if (vals != null)
        { Vector newvals = (Vector) vals.clone();
          newvals.remove(rbe.data);
          if (newvals.size() == 1)
          { String other = (String) newvals.get(0);
            BasicExpression otherbe = new BasicExpression(other);
            if (left instanceof BasicExpression)
            { BasicExpression lbe = (BasicExpression) left; 
              if (lbe.objectRef != null && 
                  lbe.objectRef.isMultiple())
              { res.add(new BinaryExpression(":",otherbe,left));
                return res;
              } 
            }
            res.add(new BinaryExpression("=",left,otherbe));
            return res;  
          }
          else 
          { if (left instanceof BasicExpression)
            { BasicExpression lbe = (BasicExpression) left; 
              if (lbe.objectRef != null && 
                  lbe.objectRef.isMultiple())
              { for (int i = 0; i < newvals.size(); i++)
                { String val = (String) newvals.get(i); 
                  BasicExpression valbe = new BasicExpression(val); 
                  res.add(new BinaryExpression(":",valbe,left));
                }
                return res;  
              } 
            }
            for (int j = 0; j < newvals.size(); j++) 
            { String val = (String) newvals.get(j); 
              BasicExpression valbe = new BasicExpression(val); 
              res.add(new BinaryExpression("=",left,valbe));
            }
            return res;
          }
        }
      }
    }
    res.add(new BinaryExpression("/=",left,right));
    return res;
  }


  public Expression computeNegation4succ(final Vector sms)
  { if (operator.equals("="))
    { return computeEqNegation4succ(sms); }
    else if (operator.equals("&"))
    { System.err.println("Can't compute negation for succ of " +
                         toString());
      return new BasicExpression("false");
    }
    else if (operator.equals("!="))
    { return new BinaryExpression("=",left,right); }
    // clone them?
    return new BasicExpression("false");
  }

  public Expression computeEqNegation4succ(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;
    Expression newsucc = null;

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0)
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }
    if (rightcat == Statemachine.STATEMACHINE)
    { int temp = rightcat;  // swap left and right
      rightcat = leftcat;
      leftcat = temp;
      temp = rmult;
      rmult = lmult;
      lmult = temp;
      Statemachine stemp = rightsm;
      rightsm = leftsm;
      leftsm = stemp;
    }
    // String lvalue = bel.toString();

    if (leftcat == Statemachine.STATEMACHINE &&
        rightcat == Statemachine.STATE)
    { Vector otherstates =
        (Vector) leftsm.getStateNames().clone();
      otherstates.remove(ber.data);
      if (otherstates.size() == 1)
      { String otherstate = (String) otherstates.get(0);
        newsucc =
          new BinaryExpression("=",
               (Expression) bel.clone(),
               new BasicExpression(otherstate));
        return newsucc;
      }
      else // otherstates.size() > 1
      { newsucc =
          new BinaryExpression("!=",
                (Expression) bel.clone(),
                (Expression) ber.clone());
        return newsucc;
      }
    }
    // swap left and right if rightcat == ATTRIBUTE
    if (leftcat == Statemachine.ATTRIBUTE)
    { Vector othervals = leftsm.getRange(bel.data);
      othervals.remove(ber.data);
      if (othervals.size() == 1)
      { String otherval = (String) othervals.get(0);
        newsucc =
          new BinaryExpression("=",
               (Expression) bel.clone(),
               new BasicExpression(otherval));
        return newsucc;
      }
      else if (othervals.size() > 1) 
      { newsucc =
          new BinaryExpression("!=",
                (Expression) bel.clone(),
                (Expression) ber.clone());
        return newsucc;
      }
      else // shouldn't happen
      { newsucc = new BasicExpression("false"); }
    }

    System.err.println("I can't produce a sensible " +
          "negation for succ of: " + bel + " = " + ber);
    return newsucc;
  }


  public Vector computeNegation4ante(final Vector sms)
  { if (operator.equals("="))
    { return computeEqNegation4ante(sms); }
    else if (operator.equals("&"))
    { Vector res1 = left.computeNegation4ante(sms);
      Vector res2 = right.computeNegation4ante(sms);
      return VectorUtil.vector_merge(res1,res2);
    }
    else if (operator.equals("!="))
    { Vector res = new Vector();
      res.add(new BinaryExpression("=",left,right));
      return res;
    }
    // clone them?
    return new Vector();
  }

  public Vector computeEqNegation4ante(final Vector sms)
  { BasicExpression bel = (BasicExpression) left;
    BasicExpression ber = (BasicExpression) right;
    int leftcat = Statemachine.NONE;
    int rightcat = Statemachine.NONE;
    Statemachine leftsm = null;
    Statemachine rightsm = null;
    int lmult = 0;
    int rmult = 0;
    Vector newante = new Vector();

    for (int i = 0; i < sms.size(); i++)
    { Statemachine sm = (Statemachine) sms.get(i);
      int lcat = sm.getCategory(bel.data);
      if (lcat >= 0)
      { leftcat = lcat;
        leftsm = sm;
        lmult = sm.getMultiplicity();
      }
      int rcat = sm.getCategory(ber.data);
      if (rcat >= 0)
      { rightcat = rcat;
        rightsm = sm;
        rmult = sm.getMultiplicity();
      }
    }
    if (rightcat == Statemachine.STATEMACHINE)
    { int temp = rightcat;  // swap left and right
      rightcat = leftcat;
      leftcat = temp;
      temp = rmult;
      rmult = lmult;
      lmult = temp;
      Statemachine stemp = rightsm;
      rightsm = leftsm;
      leftsm = stemp;
    }
    // String lvalue = bel.toString();

    if (leftcat == Statemachine.STATEMACHINE &&
        rightcat == Statemachine.STATE)
    { Vector otherstates =
        (Vector) leftsm.getStateNames().clone();
      otherstates.remove(ber.data);
      for (int i = 0; i < otherstates.size(); i++)
      { String otherstate = (String) otherstates.get(i);
        newante.add(
          new BinaryExpression("=",
               (Expression) bel.clone(),
               new BasicExpression(otherstate)));
      }
      return newante;
    }
    // swap left and right if rightcat == ATTRIBUTE
    if (leftcat == Statemachine.ATTRIBUTE)
    { Vector othervals = leftsm.getRange(bel.data);
      othervals.remove(ber.data);
      for (int i = 0; i < othervals.size(); i++)
      { String otherval = (String) othervals.get(i);
        newante.add(
          new BinaryExpression("=",
               (Expression) bel.clone(),
               new BasicExpression(otherval)));
      }
      return newante;
    }

    System.err.println("I can't produce a sensible " +
          "negation for ante of: " + bel + " = " + ber);
    return newante;
  }

  public DataDependency getDataItems()
  { DataDependency res = left.getDataItems();
    res.union(right.getDataItems());
    return res;
  }

  public DataDependency rhsDataDependency()
  { // if p.f = val then  val, p --> f
    if (operator.equals("&"))
	{ DataDependency dd1 = left.rhsDataDependency(); 
	  DataDependency dd2 = right.rhsDataDependency(); 
	  dd1.union(dd2); 
	  return dd1; 
	}
    else if (operator.equals("#"))
	{ DataDependency dd2 = right.rhsDataDependency(); 
	  return dd2; 
	}
    else if (operator.equals("="))
    { if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left; 
        DataDependency dr = right.getDataItems(); 
        // if (bel.objectRef != null || bel.arrayIndex != null)
        // { DataDependency pr = bel.getDataItems(); 
        //   dr.union(pr); 
        // }
        dr.addTarget(new BasicExpression(bel.data));
        System.out.println(">>> Data dependency: " + this + " is: " + dr); 
        return dr; 
      }
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right; 
        DataDependency dl = left.getDataItems(); 
        if (ber.objectRef != null || ber.arrayIndex != null)
        { DataDependency pr = ber.getDataItems(); 
          dl.union(pr); 
        } 
        dl.addTarget(new BasicExpression(ber.data)); 
        return dl; 
      } 
      else // assume both are changed? -- no, neither, surely?
      { DataDependency ddl = left.getDataItems(); 
        DataDependency ddr = right.getDataItems(); 
        DataDependency dd = new DataDependency(); 
        dd.addTargets(ddl); 
        dd.addTargets(ddr); 
        return dd; 
      } 
    }       
    if (operator.equals(":") || operator.equals("/:") ||
        operator.equals("/<:") || operator.equals("<:"))
    { DataDependency ddr = new DataDependency();
      DataDependency ddl = left.getDataItems();
      if (right instanceof BasicExpression)
      { BasicExpression rbe = (BasicExpression) right;
        if (rbe.objectRef != null || rbe.arrayIndex != null)
        { ddr = rbe.getDataItems(); }

        ddl.union(ddr); 
        ddl.addTarget(new BasicExpression(rbe.data)); 
        return ddl;
      }
      else  // it is the left that is the updated thing? 
      { ddr = right.getDataItems();
        ddl.addTargets(ddr);
        return ddl;
      }
    }
    if (operator.equals("->includes") || operator.equals("->excludes") ||
        operator.equals("->excludesAll") || operator.equals("->includesAll"))
    { DataDependency ddl = new DataDependency();
      DataDependency ddr = right.getDataItems();
      if (left instanceof BasicExpression)
      { BasicExpression lbe = (BasicExpression) left;
        if (lbe.objectRef != null || lbe.arrayIndex != null)
        { ddl = lbe.getDataItems(); }

        ddr.union(ddl); 
        ddr.addTarget(new BasicExpression(lbe.data)); 
        return ddr;
      }
      else  // it is the left that is the updated thing? 
      { ddl = left.getDataItems();
        ddr.addTargets(ddl);
        return ddr;
      }
    }
    return new DataDependency(); 
  }

  public UpdateFeatureInfo updateFeature(String feature)
  { UpdateFeatureInfo res = null;
    if (operator.equals("&"))
    { // chain together UFI's?
      if (left instanceof BinaryExpression)
      { BinaryExpression beleft = (BinaryExpression) left;
        UpdateFeatureInfo ufi1 = beleft.updateFeature(feature);
        if (ufi1 != null) { ufi1.newexp = this; } 
        return ufi1; 
      }
      if (right instanceof BinaryExpression)
      { BinaryExpression beright = (BinaryExpression) right;
        UpdateFeatureInfo ufi2 = beright.updateFeature(feature);
        if (ufi2 != null) { ufi2.newexp = this; }
        return ufi2;  
      }
      return null;
    }
    if (operator.equals("#"))
    { if (right instanceof BinaryExpression)
      { UpdateFeatureInfo ufi1 = ((BinaryExpression) right).updateFeature(feature);
        if (ufi1 != null) { ufi1.newexp = this; }
        return ufi1; 
      } 
      else 
      { UpdateFeatureInfo ufi2 = ((BinaryExpression) left).updateFeature(feature);
        if (ufi2 != null) { ufi2.newexp = this; }
        return ufi2; 
      } 
    }
    if (operator.equals("="))
    { if (left instanceof BasicExpression)
      { BasicExpression be = (BasicExpression) left;
        String f = be.getData();
        if (f.equals(feature) && be.objectRef == null)  // try rhs instead 
        { if (right instanceof BasicExpression) 
          { BasicExpression ber = (BasicExpression) right;
            String f2 = ber.getData();
            if (f2.equals(feature) && ber.objectRef == null) 
            { return null; } // give up
            Entity e2 = ber.getEntity(); 
            if (ber.umlkind == ATTRIBUTE)
            { Attribute att2 = e2.getAttribute(f2); 
              if (att2 != null) 
              { if (att2.isAssignable()) 
                { return new UpdateFeatureInfo(f2,left,ber.objectRef,reverse()); } 
              }
            }
            else if (ber.umlkind == ROLE)
            { return new UpdateFeatureInfo(f2,left,ber.objectRef,reverse()); }
          }
        } 
        else 
        { Entity e = be.getEntity(); 
          if (be.umlkind == ATTRIBUTE)
          { Attribute att = e.getAttribute(f); 
            if (att != null) 
            { if (att.isAssignable()) 
              { return new UpdateFeatureInfo(f,right,be.objectRef,this); } 
            }
          }
          else if (be.umlkind == ROLE)
          { return new UpdateFeatureInfo(f,right,be.objectRef,this); } 
          // roles can be frozen however
        }
      }
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        String f = ber.getData();
        if (f.equals(feature) && ber.objectRef == null) // give up
        { return null; } 
        Entity e = ber.getEntity(); 
        if (ber.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) 
            { return new UpdateFeatureInfo(f,left,ber.objectRef,reverse()); } 
          }
        }
        else if (ber.umlkind == ROLE)
        { return new UpdateFeatureInfo(f,left,ber.objectRef,reverse()); } 
      }
      return null; 
    }
    else if (operator.equals(":") ||
             operator.equals("/:") || operator.equals("/<:") || 
             operator.equals("<:"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        // must be a role
        String f = ber.getData(); // and swap exp?
        if (f.equals(feature) && ber.objectRef == null) 
        // return left, if a basic expression? 
        { return null; } 
        return new UpdateFeatureInfo(f,left,ber.objectRef,this); 
      }
      else if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if ("result".equals(bel.getData()))
        { return new UpdateFeatureInfo("result",right,bel.objectRef,this); }
      }
    }
    return null;
  } // cases of ->includes, etc


  public Expression updateExpression()
  { if (operator.equals("=") || operator.equals("->includes") ||
        operator.equals("->excludes") || operator.equals("->includesAll") ||
        operator.equals("->excludesAll"))
    { if (left instanceof BasicExpression)
      { BasicExpression be = (BasicExpression) left;
        String f = be.getData();
        Entity e = be.getEntity(); 
        if (be.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) { return right; } }
        }
        else if (be.umlkind == ROLE)
        { return right; }  // roles can be frozen however
      }
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        String f = ber.getData();
        Entity e = ber.getEntity(); 
        if (ber.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) { return left; } }
        }
        else if (ber.umlkind == ROLE)
        { return left; }  // roles can be frozen however
      }
      return null; 
    }
    else if (operator.equals(":") ||
             operator.equals("/:") || operator.equals("/<:") || 
             operator.equals("<:"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        // must be a role
        return left; // and swap exp?
      }
      else if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if ("result".equals(bel.getData()))
        { return right; }
      }
    }
    return null;
  }  // cases of ->includes, etc 

  public Expression updateReference()
  { if (operator.equals("=") || operator.equals("->includes") ||
        operator.equals("->excludes") || operator.equals("->includesAll") ||
        operator.equals("->excludesAll"))
    { if (left instanceof BasicExpression)
      { BasicExpression be = (BasicExpression) left;
        String f = be.getData();
        Entity e = be.getEntity(); 
        if (be.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) { return be.objectRef; } }
        }
        else if (be.umlkind == ROLE)
        { return be.objectRef; }  // roles can be frozen however
      }
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        String f = ber.getData();
        Entity e = ber.getEntity(); 
        if (ber.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) { return ber.objectRef; } }
        }
        else if (ber.umlkind == ROLE)
        { return ber.objectRef; }  // roles can be frozen however
      }
      return null; 
    }
    else if (operator.equals(":") ||
             operator.equals("/:") ||
             operator.equals("<:"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        // must be a role
        return ber.objectRef; // and swap exp?
      }
      else if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if ("result".equals(bel.getData()))
        { return null; }
      }
    }
    return null;
  }

  public boolean isUpdateable()   // true if can be used to generate update code
  { boolean res = false;
    if (operator.equals("&"))
    { // chain together UFI's?
      if (left instanceof BinaryExpression)
      { BinaryExpression beleft = (BinaryExpression) left;
        return beleft.isUpdateable();
      }
      if (right instanceof BinaryExpression)
      { BinaryExpression beright = (BinaryExpression) right;
        return beright.isUpdateable();
      }
      return false;
    }
    if (operator.equals("=>") || operator.equals("!"))
    { if (right instanceof BinaryExpression)
      { return ((BinaryExpression) right).isUpdateable(); }
      return false; 
    }  
    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { if (right instanceof BinaryExpression)
      { if (((BinaryExpression) right).isUpdateable())
        { return true; } 
      } 
      if (left instanceof BinaryExpression)
      { BinaryExpression beleft = (BinaryExpression) left;
        return beleft.isUpdateable();
      }
      return false;
    }
    if (operator.equals("="))
    { if (left instanceof BasicExpression)
      { return left.isAssignable(); }  
        // must be assignable - a VARIABLE, ROLE or ATTRIBUTE
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        String f = ber.getData();
        Entity e = ber.getEntity(); 
        if (ber.umlkind == ATTRIBUTE)
        { Attribute att = e.getAttribute(f); 
          if (att != null) 
          { if (att.isAssignable()) 
            { return true; } 
          }
          return false; 
        }
        return true; 
      }
    }
    else if (operator.equals(":") || 
             operator.equals("/:") || operator.equals("/<:") || 
             operator.equals("<:"))
    { if (right instanceof BasicExpression)
      { return right.isAssignable(); }  // and assignable
      else if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if ("result".equals(bel.getData()))
        { return true; }
      }
    }
    else if (operator.equals("->includes") || operator.equals("->excludes") || 
             operator.equals("->includesAll") || 
             operator.equals("->excludesAll"))
    { if (left instanceof BasicExpression)
      { return left.isAssignable(); }  // and assignable
      else if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if ("result".equals(ber.getData()))
        { return true; }
      }
    }
    return false;
  }

  public Vector allBinarySubexpressions()
  { // returns new Vector() by default.
    Vector res = new Vector();
    if (left instanceof BasicExpression &&
        right instanceof BasicExpression)
    { res.add(clone()); }
    else if (operator.equals("&")) 
    { res.addAll(left.allBinarySubexpressions());
      res.addAll(right.allBinarySubexpressions());
    }
    else if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC") ||
             operator.equals("!"))
    { res.addAll(right.allBinarySubexpressions()); }
    return res;
  } // case of => ? 

  public Vector allConjuncts()
  { // returns new Vector() by default.
    Vector res = new Vector();
    if (operator.equals("&")) 
    { Vector map1 = left.allConjuncts();
      Vector map2 = right.allConjuncts();
      // (conj,x & right) for each (conj,x) : map1, etc:
      for (int i = 0; i < map1.size(); i++) 
      { Vector conjx = (Vector) map1.get(i); 
        Expression x = (Expression) conjx.get(1); 
        Expression newx = Expression.simplify("&",x,right,new Vector()); 
        Vector newconjx = new Vector(); 
        newconjx.add(conjx.get(0)); 
        newconjx.add(newx); 
        res.add(newconjx);
      } 
      for (int i = 0; i < map2.size(); i++) 
      { Vector conjx = (Vector) map2.get(i); 
        Expression x = (Expression) conjx.get(1); 
        Expression newx = Expression.simplify("&",left,x,new Vector()); 
        Vector newconjx = new Vector(); 
        newconjx.add(conjx.get(0)); 
        newconjx.add(newx); 
        res.add(newconjx); 
      }
    }
    else 
    { Vector map = new Vector(); 
      map.add(this); 
      map.add(null); 
      res.add(map);
    } 
    return res;
  }

  public Vector getConjuncts()
  { // returns new Vector() by default.
    Vector res = new Vector();
    if (operator.equals("&")) { } 
    else 
    { res.add(this); 
      return res; 
    } 

 
    Vector map1 = left.getConjuncts();
    Vector map2 = right.getConjuncts();
    res = VectorUtil.union(map1, map2); 
    return res;
  }

  public Vector allDisjuncts()
  { // returns new Vector() by default.
    Vector res = new Vector();
    if (operator.equals("or")) { } 
    else 
    { res.add(this); 
      return res; 
    } 

 
    Vector map1 = left.allDisjuncts();
    Vector map2 = right.allDisjuncts();
    res = VectorUtil.union(map1, map2); 
    return res;
  }

  // The set of features in writable mode in the expression
  public Vector writeFrame()
  { Vector res = new Vector(); 
    if (operator.equals("="))
    { if (left instanceof BasicExpression) 
      { BasicExpression bel = (BasicExpression) left; 
        res.add(bel.getData()); 
      } 
      return res; 
    }   
    if (operator.equals("&") || operator.equals("xor"))
    { res = left.writeFrame(); 
      res.addAll(right.writeFrame()); 
      return res; 
    } 
    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { res = right.writeFrame();
      if (left instanceof BinaryExpression) 
      { BinaryExpression leftbe = (BinaryExpression) left; 
        if (leftbe.right.umlkind != CLASSID) 
        { return res; } 
        Entity e = leftbe.right.entity; 
        if (e == null)
        { return res; } 
        if (e.isAbstract())
        { return res; } 
        if ((leftbe.right + "").endsWith("@pre"))
        { return res; } 
        // all superclasses of e, and e. 
        Vector supers = e.getAllSuperclasses(); 
        Vector wrleft = new Vector(); 
        wrleft.add(e + ""); 
        for (int i = 0; i < supers.size(); i++) 
        { Entity esup = (Entity) supers.get(i); 
          wrleft.add(esup + ""); 
        } 
        return VectorUtil.union(res,wrleft);  
      }
      return res; 
    } 
    if (operator.equals("=>") || operator.equals("!") ||
        operator.equals("->forAll")) 
       // || operator.equals(":") ||
       // operator.equals("<:"))
    { return right.writeFrame(); } 
    if (operator.equals("->select") || operator.equals("->reject") ||  // ??
        operator.equals("->includes") || operator.equals("->excludes") ||
        operator.equals("->excludesAll") || operator.equals("->includesAll"))
    { return left.writeFrame(); } 
    return res; 
  } 

  public Vector wr(Vector assocs)
  { Vector res = new Vector(); 
    if (operator.equals("=") || operator.equals("->includes") || operator.equals("->excludes") ||
        operator.equals("->includesAll") || operator.equals("->excludesAll"))
    { return left.wr(assocs); }  // just the left.data
    if (operator.equals(":") || operator.equals("<:") || 
        operator.equals("/:") || operator.equals("/<:"))
    { return right.wr(assocs); } 
    if (operator.equals("&") || operator.equals("xor"))
    { return VectorUtil.union(left.wr(assocs),right.wr(assocs)); } 
    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { // left is not written if it is preform, abstract class or not a class
      res = right.wr(assocs); 
      if (left instanceof BinaryExpression) 
      { BinaryExpression leftbe = (BinaryExpression) left; 
        if (leftbe.right.umlkind != CLASSID) 
        { return res; } 
        Entity e = leftbe.right.entity; 
        if (e == null)
        { return res; } 
        if (e.isAbstract())
        { return res; } 
        if ((leftbe.right + "").endsWith("@pre"))
        { return res; } 
        return VectorUtil.union(res,left.wr(assocs));  
      } 
    } 
    if (operator.equals("=>") || operator.equals("!") ||
        operator.equals("->forAll"))  // surely? 
    { return right.wr(assocs); } 
    if (operator.equals("->select") || operator.equals("->reject") || // for isDeleted
        operator.equals("->includes") || operator.equals("->excludes") || 
        operator.equals("->excludesAll") || operator.equals("->includesAll"))
    { return left.wr(assocs); } 
    return res; 
  } 

  public Vector cwr(Vector assocs)
  { // The data which is completely over-written

    Vector res = new Vector(); 

    if (operator.equals("="))
    { return left.wr(assocs); }  // just the left.data

    if (operator.equals(":") || operator.equals("<:") || 
        operator.equals("/:") || operator.equals("/<:"))
    { return res; } 

    if (operator.equals("&") || operator.equals("xor"))
    { return VectorUtil.union(left.cwr(assocs),right.cwr(assocs)); } 

    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { // left is not written if it is preform, abstract class or not a class
      return right.cwr(assocs); 
    } 

    if (operator.equals("=>") || operator.equals("!") ||
        operator.equals("->forAll"))
    { return right.cwr(assocs); } 

    return res; 
  } 

  // cases of /:, /<: ?? ->exists, ->exists1, ->forAll? 
  /* 
  public Vector wrdel(Vector assocs)
  { Vector res = new Vector(); 
    if (operator.equals("->select") || operator.equals("->reject") || 
         operator.equals("->excluding"))
    { return left.wrdel(assocs); } 
    if (operator.equals("->including") || 
        operator.equals("->union") || operator.equals("->intersection") ||
        operator.equals("\\/") || operator.equals("/\\"))
    { res = left.wrdel(assocs); 
      return VectorUtil.union(res,right.wrdel(assocs)); 
    }  
    return res; 
  } */ 


  public Vector allReadFrame()
  { Vector res = new Vector(); 
    res = left.allReadFrame(); 
    return VectorUtil.union(res,right.allReadFrame()); 
  } 

  public Vector readFrame()
  { Vector res = new Vector(); 
    if (operator.equals("=") || operator.equals("->excludes") ||
        operator.equals("->includes") || operator.equals("->excludesAll") ||
        operator.equals("->includesAll"))
    { if (left instanceof BasicExpression) 
      { BasicExpression beleft = (BasicExpression) left; 
        if (beleft.arrayIndex != null) 
        { res.addAll(beleft.arrayIndex.allReadFrame()); } 
        if (beleft.objectRef != null) 
        { res.addAll(beleft.objectRef.allReadFrame()); }
        res.addAll(right.allReadFrame()); 
        return res; 
      } 
      res = left.allReadFrame(); 
      res.addAll(right.allReadFrame()); 
      return res; 
    }

    if (operator.equals(":") || operator.equals("<:") || 
        operator.equals("/:") || operator.equals("/<:"))
    { if (right instanceof BasicExpression) 
      { BasicExpression beright = (BasicExpression) right; 
        if (beright.arrayIndex != null) 
        { res.addAll(beright.arrayIndex.allReadFrame()); } 
        if (beright.objectRef != null) 
        { res.addAll(beright.objectRef.allReadFrame()); }
        res.addAll(left.allReadFrame()); 
        return res;  
      }
      res = left.allReadFrame(); 
      res.addAll(right.allReadFrame()); 
      return res; 
    }   

    if (operator.equals("=>"))
    { Vector lrf = left.allReadFrame(); 
      return VectorUtil.union(lrf,right.readFrame()); 
    } 

    if (operator.equals("!")) 
    { Vector rfscope = ((BinaryExpression) left).right.allReadFrame(); 
      return VectorUtil.union(rfscope,right.readFrame()); 
    } // some cases of #, #1 are also like this

    // special treatment of #, #1 needed, eg, t.f = a & t.g = t.f*2 & 
    
    return VectorUtil.union(left.readFrame(),right.readFrame());
  } 

  public Vector internalReadFrame()
  { Vector res = new Vector(); 

    if (operator.equals("=") || operator.equals("->excludes") ||
        operator.equals("->includes") || operator.equals("->excludesAll") ||
        operator.equals("->includesAll"))
    { if (left instanceof BasicExpression) 
      { BasicExpression beleft = (BasicExpression) left; 
        if (beleft.arrayIndex != null) 
        { res.addAll(beleft.arrayIndex.allReadFrame()); } 
        if (beleft.objectRef != null) 
        { res.addAll(beleft.objectRef.allReadFrame()); }
        res.addAll(right.allReadFrame()); 
        return res; 
      } 
      res = left.allReadFrame(); 
      res.addAll(right.allReadFrame()); 
      return res; 
    }

    if (operator.equals(":") || operator.equals("<:") || 
        operator.equals("/:") || operator.equals("/<:"))
    { if (right instanceof BasicExpression) 
      { BasicExpression beright = (BasicExpression) right; 
        if (beright.arrayIndex != null) 
        { res.addAll(beright.arrayIndex.allReadFrame()); } 
        if (beright.objectRef != null) 
        { res.addAll(beright.objectRef.allReadFrame()); }
        res.addAll(left.allReadFrame()); 
        return res;  
      }
      res = left.allReadFrame(); 
      res.addAll(right.allReadFrame()); 
      return res; 
    }   

    if (operator.equals("=>"))
    { Vector lrf = left.allReadFrame(); 
      return VectorUtil.union(lrf,right.internalReadFrame()); 
    } 

    if (operator.equals("!")) 
    { Vector rfscope = ((BinaryExpression) left).right.allReadFrame(); 
      return VectorUtil.union(rfscope,right.internalReadFrame()); 
    } // some cases of #, #1 are also like this

    // special treatment of #, #1 needed, eg, t.f = a & t.g = t.f*2 & 

    if (operator.equals("&"))
    { Vector lrf = left.internalReadFrame(); 
      Vector assocs = new Vector(); 
      Vector wf = left.cwr(assocs); 
      Vector rrf = right.readFrame(); 
      Vector resr = new Vector(); 
      resr.addAll(rrf); 
      resr.removeAll(wf); // read elements that were not over-written in left. 

      Vector rr = VectorUtil.union(lrf, resr); 
      // System.out.println("INTERNAL READ FRAME OF " + this + " IS: " + rr);
      return rr;   
    } 
    
    return VectorUtil.union(left.internalReadFrame(),right.internalReadFrame());
  } 

  public boolean confluenceCheck(Vector iterated, Vector created) 
  { // true if the only assignments are c.f = e for c in created, or c : s
    // where neither s or s@pre are read
    // Local assignments are also ok. 

    // assignments c.cId = e for created c and cId an identify must have: 
    // (i) e is a variable, or an identity attribute of an iterated element, or 
    // a 1-1 function of such an attribute; (ii) e cannot be a constant and 
    // should not be a many-1 function. 

    boolean res = false; 

    if (operator.equals("="))
    { if (left instanceof BasicExpression) 
      { BasicExpression bel = (BasicExpression) left; 
        if (created.contains(bel.objectRef + ""))
        { return checkIdentityAssignment(bel,iterated,created); } 
        else 
        { Vector vleft = new Vector();
          vleft.add(bel.objectRef + "");  
          Vector vright = right.innermostVariables(); 
          if (vleft.contains("self"))
          { if (vright.size() == 0)
            { return true; }  // assigning a value to feature of self
            else if (vright.size() == 1 && vright.contains("self"))
            { return true; }  // updating self by its own data
          }  
          else if (vleft.contains("null")) 
          { if (vright.size() == 0)
            { return true; } 
            else if (vright.size() == 1 && vright.contains("self"))
            { return true; }
          }  
          System.out.println(">>> Confluence warning: assignment not to new object or iterated source object: " + this); 
          return false;
        } 
      } 
      return false; // not a valid assignment 
    }
    else if (operator.equals(":"))
    { if (created.contains(left + ""))  // and right.data is unordered
      { if (right.entity != null && right instanceof BasicExpression && 
            right.entity.isOrderedRole(((BasicExpression) right).getData()))
        { System.err.println(">>> Potentially fails confluence, addition to ordered role: " + this); 
          return false; 
        } 
        return true; 
      } 
      else if (right instanceof BasicExpression)
      { BasicExpression re = (BasicExpression) right; 
        if (created.contains(re.objectRef + ""))
        { return true; } 
        System.out.println("May fail confluence: addition to not newly created object " + this);
        return false;  
      } 
      return true;  // not an update
    }
    else if (operator.equals("->includes"))
    { if (created.contains(right + ""))  // and left.data is unordered
      { if (left.entity != null && left instanceof BasicExpression && 
            left.entity.isOrderedRole(((BasicExpression) left).getData()))
        { System.err.println(">>> Potentially fails confluence, addition to ordered role: " + this); 
          return false; 
        } 
        return true;
      } 
      else if (left instanceof BasicExpression)
      { BasicExpression le = (BasicExpression) left; 
        if (created.contains(le.objectRef + ""))
        { return true; } 
        System.out.println("May fail confluence: addition to not newly created object " + this);
        return false;  
      } 
      return true; // not an update
    }
    else if (operator.equals("<:"))
    { if (right instanceof BasicExpression) 
      { if (right.isOrdered())
        { System.out.println(">>> Potentially fails confluence: addition to ordered collection: " + this); 
          return false; 
        } 
        else 
        { BasicExpression re = (BasicExpression) right; 
          if (created.contains(re.objectRef + ""))
          { return true; }
          System.out.println(">>> Confluence warning: adds objects not newly created: " + this); 
          return false; 
        } 
      } 
      return true;  // not an update 
    }
    else if (operator.equals("->includesAll"))
    { if (left instanceof BasicExpression) 
      { if (left.isOrdered())
        { System.out.println(">>> Confluence warning: addition to ordered collection: " + this); 
          return false; 
        } 
        else 
        { BasicExpression le = (BasicExpression) left; 
          if (created.contains(le.objectRef + ""))
          { return true; }
          System.out.println(">>> Confluence warning: adds objects not newly created: " + this); 
          return false; 
        } 
      } 
      return true;  // not an update 
    }
    else if (operator.equals("&"))
    { res = left.confluenceCheck(iterated, created); 
      if (res) 
      { return right.confluenceCheck(iterated, created); } 
      else 
      { return false; }  
    } 
    else if (operator.equals("=>"))
    { return right.confluenceCheck(iterated, created); }  
    else if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { if (left instanceof BinaryExpression && ((BinaryExpression) left).right.entity != null) 
      { BinaryExpression lbe = (BinaryExpression) left; 
        Vector cr = new Vector(); 
        cr.addAll(created); 

        Vector ukeys = lbe.right.entity.getUniqueAttributes(); 
        
        if (("" + lbe.right).endsWith("@pre") || lbe.right.entity.isAbstract())
        { } 
        else if (iterated.size() <= 1 || ukeys.size() == 0) 
        { cr.add(lbe.left + ""); } 
        return right.confluenceCheck(iterated, cr);
      }
      else 
      { return right.confluenceCheck(iterated, created); }
    } 
    else if (operator.equals("->exists") || 
             operator.equals("->exists1"))
    { return right.confluenceCheck(iterated, created); }
    else if (operator.equals("!"))
    { if (left instanceof BinaryExpression && ((BinaryExpression) left).right.entity != null) 
      { BinaryExpression lbe = (BinaryExpression) left; 
        Vector cr = new Vector(); 
        cr.addAll(iterated); 
        cr.add(lbe.left); 
        return right.confluenceCheck(cr, created); 
      } 
      return right.confluenceCheck(iterated, created);
    } 
    else 
    { return false; } 
  }

  public boolean checkIdentityAssignment(BasicExpression bel, Vector iterated, Vector created) 
  { // assignments c.cId = e for created c and cId an identify must have: 
    // (i) e is a variable, or an identity attribute of an iterated element, or 
    // a 1-1 function of such an attribute; (ii) e cannot be a constant and 
    // should not be a many-1 function. 

    boolean res = false; 
    if (bel.umlkind != ATTRIBUTE)
    { return true; } 

    Entity lefte = left.getEntity(); 
    if (bel.objectRef != null) 
    { Type lot = bel.objectRef.getElementType(); 
      if (lot.isEntity())
      { lefte = lot.getEntity(); } 
    } 

    if (lefte == null)  // not a feature of an entity
    { return true; } 

    Attribute key = lefte.getUniqueAttribute(bel.data); 
    if (key == null) 
    { return true; } 

    System.out.println(); 
    // System.out.println(">>> Checking assignment to key attribute " + bel.data + " of " + lefte); 

    if (right instanceof BasicExpression) 
    { BasicExpression rbe = (BasicExpression) right; 
      if (rbe.umlkind == CONSTANT && iterated.size() > 0) 
      { System.err.println(">>> Confluence error: assigning constant to key of created object within an iteration: " + this); 
        return false; 
      } 
      else if (rbe.umlkind == VALUE && iterated.size() == 0) 
      { System.err.println(">>> Warning, possible confluence error: assigning value to key of created object: " + this); 
        return false; 
      } 
      else if (rbe.umlkind == VALUE && iterated.size() > 0) 
      { System.err.println(">>> Confluence error: assigning value to key of created object within an iteration: " + this); 
        return false; 
      } 
      else if (rbe.umlkind == CONSTANT && iterated.size() == 0) 
      { System.err.println(">>> Warning, possible confluence error: assigning constant to key of created object: " + this); 
        return false; 
      } 
      else if (rbe.umlkind == VARIABLE) 
      { System.out.println(">>> Assignment of variable to identity attribute, error if variable is not value of a source primary key or 1-1 function of value: " + this); 
        return true; 
      } 
      else if (rbe.umlkind == ATTRIBUTE) 
      { Expression ror = rbe.objectRef; 
        Entity rent = right.getEntity(); 
        if (ror != null) 
        { rent = ror.getElementType().getEntity(); } 

        Attribute ratt = rent.getUniqueAttribute(rbe.data); 
        if (ratt == null) 
        { System.out.println("Possible confluence error: Assignment of non-identity attribute to identity attribute: " + this);            
          return true;
        }  
      } 
      else if (rbe.umlkind == FUNCTION) // ok for reverse, dubious for front, tail, subrange, etc
      { if (rbe.data.equals("reverse"))
        { return true; } 
        if (rbe.data.equals("front") || "tail".equals(rbe.data) || "subrange".equals(rbe.data) 
            || "toLowerCase".equals(rbe.data) || "toUpperCase".equals(rbe.data))
        { System.out.println(">>> Possible confluence error: Assignment of many-one function value to identity attribute: " + this);            
          return true;
        } 
      } 
      else if (right instanceof UnaryExpression) 
      { UnaryExpression rune = (UnaryExpression) right; 
        if (rune.operator.equals("front") || "tail".equals(rune.operator) || 
            "subrange".equals(rune.operator) || "toLowerCase".equals(rune.operator) ||
            "toUpperCase".equals(rune.operator))
        { System.out.println(">>> Possible confluence error: Assignment of many-one function value to identity attribute: " + this);            
          return true;
        }
      } // reverse is ok again.
      else if (right instanceof BinaryExpression) 
      { BinaryExpression rbin = (BinaryExpression) right; 
        if (rbin.operator.equals("+")) 
        { if (rbin.left.umlkind == VALUE && rbin.right.umlkind == VALUE) 
          { System.err.println(">>> Confluence error: Assignment of constant value to an identity attribute: " + this);    
            return false;
          } 
          else if (rbin.left.umlkind == VALUE || rbin.right.umlkind == VALUE) 
          { return true; }
        } 
        else if (rbin.operator.equals("-"))
        { System.out.println(">>> Possible confluence error: Assignment of many-one function value to identity attribute: " + this);            
          return true;
        }
      }     
    } 
    return true; 

  }  
 

  // returns true if opf(val) can possibly make expression true
  public boolean relevantOccurrence(String op, Entity ent, 
                                    String val,
                                    String f)
  { boolean rel1 = left.relevantOccurrence(op,ent,val,f);
    boolean rel2 = right.relevantOccurrence(op,ent,val,f);
    if (!rel1 && !rel2) { return false; }

    if (operator.equals("=") && op.equals("set"))
    { if (left instanceof BasicExpression &&
          right instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        BasicExpression ber = (BasicExpression) right;
        if (bel.data.equals(f) &&
            !ber.data.equals(val) &&
            ber.getType().getValues() != null &&
            ber.getType().getValues().contains(val))
        { return false; }
        if (ber.data.equals(f) &&
            !bel.data.equals(val) &&
            bel.getType().getValues() != null &&
            bel.getType().getValues().contains(val))
        { return false; }
      }
      return true;  // only if f occurs in this? 
    }
    else if (operator.equals("/=") && op.equals("set"))
    { if (left instanceof BasicExpression &&
          right instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        BasicExpression ber = (BasicExpression) right;
        if (bel.data.equals(f) &&
            ber.data.equals(val) &&
            ber.getType().getValues() != null &&
            bel.getType().getValues().contains(val))
        { return false; }
      }
      return true;
    }
    else if (operator.equals("<:")  && op.equals("add"))  // also for :
    { if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if (bel.data.equals(f) && !rel2)
        { return false; }
      }
      return true;
    }
    else if (operator.equals("/:") && op.equals("add"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    else if (operator.equals("<:")  && op.equals("remove"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    else if (operator.equals(":") && op.equals("remove"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    return true;
  }

  // returns true if opf(val) can possibly make expression false
  public boolean canFalsify(String op, Entity ent, 
                            String val, String f)
  { boolean rel1 = left.allFeaturesUsedIn().contains(f); 
    boolean rel2 = right.allFeaturesUsedIn().contains(f);
    if (!rel1 && !rel2) { return false; }  // a change to f cannot affect this
                                           // if no data-dependent feature of f
                                           // occurs in this. 

    if (operator.equals("<:") && op.equals("add"))  // also for :
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    else if (operator.equals("/:") && op.equals("remove"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    else if (operator.equals("<:")  && op.equals("remove"))
    { if (left instanceof BasicExpression)
      { BasicExpression bel = (BasicExpression) left;
        if (bel.data.equals(f) && !rel2)
        { return false; }
      }
      return true;
    }
    else if (operator.equals(":") && op.equals("add"))
    { if (right instanceof BasicExpression)
      { BasicExpression ber = (BasicExpression) right;
        if (ber.data.equals(f) && !rel1)
        { return false; }
      }
      return true;
    }
    return true;
  }

  public Expression matchCondition(String op, String f,
                     Expression exbe)
  { // any special rules based on op?
    Expression lmc = left.matchCondition(op,f,exbe);
    Expression rmc = right.matchCondition(op,f,exbe);
    return simplifyOr(lmc,rmc);
  }


  public Expression featureSetting(String var, String feat, Vector remainder)
  { // if (left instanceof BasicExpression) 
    // { BasicExpression beleft = (BasicExpression) left; 
    //   if (beleft.data.equals(feat))
    //   { 
    if ((left + "").equals(var + "." + feat))
    { return right; } 
    if (operator.equals("&"))
    { Vector remainder1 = new Vector(); 
      Expression lfs = left.featureSetting(var,feat,remainder1);
      if (lfs == null)
      { remainder.add(left); 
        return right.featureSetting(var,feat,remainder);
      }
      remainder.add(right);  // remainder1.add(right) ?? 
      return lfs; 
    } 
    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { Vector rem = new Vector(); 
      Expression res = right.featureSetting(var,feat,rem);
      if (rem.size() > 0)
      { Expression rempred = (Expression) rem.get(0); 
        remainder.add(new BinaryExpression(operator,left,rempred)); 
        return res; 
      }  
    } 
    return null; 
  } 

  public Expression featureSetting2(String var, String feat, Vector remainder)
  { if ((left + "").equals(var + "." + feat))  // operator.equals("=")
    { // remainder = new Vector(); 
      return right; 
    }
 
    
    if (operator.equals("&"))
    { Vector remainder1 = new Vector(); 
      Vector remainder2 = new Vector(); 
      Expression lfs = left.featureSetting2(var,feat,remainder1);
      if (lfs == null)
      { Expression res = right.featureSetting2(var,feat,remainder2);
        Vector rem = (Vector) simplifyAnd(remainder1,remainder2);
        if (rem != null && rem.size() > 0)
        { remainder.add(rem.get(0)); }    // remainder.size() == 0 
        return res; 
      }
      remainder2.add(right);
      Vector rem = (Vector) simplifyAnd(remainder1,remainder2);   
      if (rem != null && rem.size() > 0)
      { remainder.add(rem.get(0)); }    // remainder.size() == 0 
      return lfs; 
    } 

    if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { Vector rem = new Vector(); 
      Expression res = right.featureSetting2(var,feat,rem);
      if (rem.size() > 0) // res could be null
      { Expression rempred = (Expression) rem.get(0); 
        remainder.add(new BinaryExpression(operator,left,rempred)); 
        return res; 
      }  
    } 
    
    remainder.add(this); 
    return null; 
  } 

  public Expression featureAdding2(String var, Vector remainder)
  { if (":".equals(operator) && (left + "").equals(var + ""))
    { 
      return right; 
    }
 
    if ("->includes".equals(operator) && (right + "").equals(var + ""))
    { 
      return left; 
    }
 
   if (operator.equals("&"))
    { Vector remainder1 = new Vector(); 
      Vector remainder2 = new Vector(); 
      Expression lfs = left.featureAdding2(var,remainder1);
      if (lfs == null)
      { Expression res = right.featureAdding2(var,remainder2);
        Vector rem = (Vector) simplifyAnd(remainder1,remainder2);
        if (rem != null && rem.size() > 0)
        { remainder.add(rem.get(0)); }    // remainder.size() == 0 
        return res; 
      }
      remainder2.add(right);
      Vector rem = (Vector) simplifyAnd(remainder1,remainder2);   
      if (rem != null && rem.size() > 0)
      { remainder.add(rem.get(0)); }    // remainder.size() == 0 
      return lfs; 
    } 

    /* if (operator.equals("#") || operator.equals("#1"))
    { Vector rem = new Vector(); 
      Expression res = right.featureAdding2(var,rem);
      if (rem.size() > 0) // res could be null
      { Expression rempred = (Expression) rem.get(0); 
        remainder.add(new BinaryExpression(operator,left,rempred)); 
        return res; 
      }  
    } */ 
    
    remainder.add(this); 
    return null; 
  } 


  public int syntacticComplexity() 
  { int res = left.syntacticComplexity();
    res = res + right.syntacticComplexity(); 
    if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC") || operator.equals("!") ||
        operator.equals("|") || operator.equals("|R") || operator.equals("|C"))
    { return res; }  
    return res + 1; 
  } 

  public int cyclomaticComplexity() 
  { if (operator.equals("#") || operator.equals("#1") || operator.equals("#LC") || operator.equals("!"))
    { return right.cyclomaticComplexity(); } 
    else if (operator.equals("&") || operator.equals("or") || operator.equals("=>"))
    { return left.cyclomaticComplexity() + right.cyclomaticComplexity(); }  
    return 1; 
  } 

  // assume outermost operator is an exists
  public Expression isExistsForall(Vector foralls, Expression tracest)
  { if (operator.equals("#") || operator.equals("#LC") || operator.equals("#1"))
    { Expression rem = right.isExistsForall(foralls, tracest);
      if (rem == null) { return null; }
      else
      { return new BinaryExpression(operator,left,rem); }
    }
    else if (operator.equals("&"))
    { Expression lrem = left.isForall(foralls, tracest);
      Expression rrem = right.isForall(foralls, tracest);
      if (lrem == null && rrem == null)
      { return null; }
      else if (lrem == null)
      { return Expression.simplifyAnd(left,rrem); }
      else if (rrem == null)
      { return Expression.simplifyAnd(lrem,right); }
      return null; 
    }
    else if (operator.equals("!"))
    { foralls.add(this); 
      return tracest; // new BasicExpression("$$");
    } // for the trace
    else 
    { return null; }
  }

  public Expression isForall(Vector foralls, Expression tracest)
  { if (operator.equals("&"))
    { Expression lrem = left.isForall(foralls, tracest);
      Expression rrem = right.isForall(foralls, tracest);
      if (lrem == null && rrem == null)
      { return null; }
      else if (lrem == null)
      { return Expression.simplifyAnd(left,rrem); }
      else if (rrem == null)
      { return Expression.simplifyAnd(lrem,right); }
      return null; 
    }
    else if (operator.equals("!"))
    { foralls.add(this); 
      return tracest;
    } // for the trace
    else 
    { return null; }
  }

private BExpression seqselectBinvariantForm(String var, BExpression bsimp, BExpression pexp, 
                                            java.util.Map env)
{ String i = Identifier.nextIdentifier("i");
  String j = Identifier.nextIdentifier("j");
  String k = var; // Identifier.nextIdentifier("k");
  String f = Identifier.nextIdentifier("f");
  BExpression fbe = new BBasicExpression(f);
  BExpression ibe = new BBasicExpression(i);
  BExpression jbe = new BBasicExpression(j);
  BExpression kbe = new BBasicExpression(k);
  String ssq = Identifier.nextIdentifier("sqxx");
  BExpression ssqe = new BBasicExpression(ssq);
  String n = Identifier.nextIdentifier("n");
  BExpression nbe = new BBasicExpression(n);
  BExpression domsq = new BUnaryExpression("dom", bsimp);
  // env1 maps left.elementType + "" to new BApplyExpression(left.binvariantForm(env,local), kbe)

  BExpression predk = pexp; // right.binvariantForm(env,local);
  BExpression setcomp = new BSetComprehension(k, domsq, predk);
  BExpression eqcard = new BBinaryExpression("=", new BUnaryExpression("card", ssqe), nbe);
  BExpression ileqj = new BBinaryExpression("<=", ibe, jbe);
  BExpression fileqfj = 
    new BBinaryExpression("<=", new BApplyExpression(f,ibe), new BApplyExpression(f,jbe));
  BExpression leq = new BBinaryExpression("=>", ileqj, fileqfj);
  Vector vars = new Vector();
  vars.add(i); vars.add(j);
  BExpression domf = new BUnaryExpression("dom", fbe);
  BExpression indomfi = new BBinaryExpression(":", ibe, domf); 
  BExpression indomfj = new BBinaryExpression(":", jbe, domf); 
  BExpression indomf = new BBinaryExpression("&", indomfi, indomfj); 
  BExpression indomfleq = new BBinaryExpression("&", indomf, leq); 

  BExpression ord = new BQuantifierExpression("forall", vars, indomfleq);
  BExpression ransq = new BUnaryExpression("ran", bsimp);
  BExpression ssqrange = // new BBinaryExpression(":", ssqe, 
       new BUnaryExpression("seq", ransq);
  String ii = Identifier.nextIdentifier("ix");
  BExpression iibe = new BBasicExpression(ii);
  BExpression align = new BBinaryExpression("=>", 
   new BBinaryExpression(":", iibe, 
      new BUnaryExpression("dom", ssqe)),
   new BBinaryExpression("=", new BApplyExpression(ssqe, iibe),
      new BApplyExpression(bsimp, new BApplyExpression(fbe, iibe))));
  // i : dom(sq) => sq(i) = left(f(i))

  BExpression pred2 = new BQuantifierExpression("forall", ii, align);
  BExpression rpred = new BBinaryExpression("&", eqcard, pred2);
 //  BExpression rpred = new BBinaryExpression("&", ssqrange, rpred1);
  BExpression pred = new BBinaryExpression("&", ord, rpred); 
  BExpression frange = new BBinaryExpression(":", fbe, new BUnaryExpression("seq", setcomp));
  BExpression qf = new BQuantifierExpression("exists", f, 
                                             new BBinaryExpression("&", frange, pred));
 
  BExpression nrange = new BBinaryExpression(":", nbe, domsq); 
  BExpression qn = new BQuantifierExpression("exists",n,
  new BBinaryExpression("&", nrange, qf));
  BExpression res = new BSetComprehension(ssq, ssqrange, qn);
  return new BUnaryExpression("union",res);
}
  // union({ sq | sq : seq(ran(left)) & #n.(n : dom(left) & 
  //           #f.(f : seq({ k | k : dom(left) & right }) & align & 
  //                    !(i,j).(i : dom(f) & j : dom(f) & i <= j => f(i) <= f(j)) & 
  //           card(sq) = n))})
  
// BSetComprehension(var, rng, e)
// is { var | var : rng & e }

  public void changedEntityName(String oldN, String newN)
  { left.changedEntityName(oldN, newN); 
    right.changedEntityName(oldN, newN); 
  } 

  public boolean isRuleCallDisjunction(Vector rules) 
  { if (operator.equals("or")) { } 
    else 
    { return false; } 

    if (left instanceof BasicExpression) 
    { BasicExpression bleft = (BasicExpression) left; 
      if (bleft.isRuleCall(rules)) { } 
      else 
      { return false; } 
    } 
    else if (left instanceof BinaryExpression) 
    { BinaryExpression binleft = (BinaryExpression) left; 
      if (binleft.isRuleCallDisjunction(rules)) { } 
      else 
      { return false; } 
    } 
    else 
    { return false; } 

    if (right instanceof BasicExpression) 
    { BasicExpression bright = (BasicExpression) right; 
      if (bright.isRuleCall(rules)) { } 
      else 
      { return false; } 
    }
    else if (right instanceof BinaryExpression) 
    { BinaryExpression binright = (BinaryExpression) right; 
      if (binright.isRuleCallDisjunction(rules)) { } 
      else 
      { return false; } 
    } 
    else 
    { return false; } 

    return true; 
  }      

  public String cg(CGSpec cgs)
  { String etext = this + "";
    if (needsBracket) 
    { etext = "(" + etext + ")"; } 

    Vector args = new Vector();
    Vector eargs = new Vector(); 

    if ("|C".equals(operator) || "|R".equals(operator) || 
        "|".equals(operator) || "#".equals(operator) || 
        "#1".equals(operator) || "|sortedBy".equals(operator) ||
		"|intersectAll".equals(operator) || "|unionAll".equals(operator) ||
		"|concatenateAll".equals(operator) || "|isUnique".equals(operator) ||
		"|selectMaximals".equals(operator) || "|selectMinimals".equals(operator) || 
        "!".equals(operator) || "|A".equals(operator))
    { BinaryExpression beleft = (BinaryExpression) getLeft();  
      args.add(beleft.getRight().cg(cgs)); 
      args.add(beleft.getLeft().cg(cgs)); 
      args.add(right.cg(cgs)); 
      eargs.add(beleft.getRight()); 
      eargs.add(beleft.getLeft()); 
      eargs.add(right); 
    } 
    else if ("->collect".equals(operator) || "->reject".equals(operator) || 
             "->select".equals(operator) || "->exists".equals(operator) || 
             "->exists1".equals(operator) || "->forAll".equals(operator) ||
             "->intersectAll".equals(operator) || "->unionAll".equals(operator) ||
		     "->selectMaximals".equals(operator) || "->selectMinimals".equals(operator) || 
             "->isUnique".equals(operator) || "->sortedBy".equals(operator) || 
             "->any".equals(operator))
    { String id = Identifier.nextIdentifier("var");
      args.add(left.cg(cgs));
      eargs.add(left); 
      BasicExpression v = new BasicExpression(id); 
      v.setType(left.getElementType()); 
      args.add(v.cg(cgs));
      eargs.add(v);  
      Expression newright = right.addReference(v,left.getElementType()); 
      newright.setType(right.getType()); 
      newright.setElementType(right.getElementType()); 
      args.add(newright.cg(cgs));
      eargs.add(newright); 
    } 
    else 
    { args.add(left.cg(cgs)); 
      args.add(right.cg(cgs)); 
      eargs.add(left); 
      eargs.add(right);
    } 

    CGRule r = cgs.matchedBinaryExpressionRule(this,etext);
    if (r != null)
    { System.out.println(">> Found rule " + r + " for: " + etext); 
      String res = r.applyRule(args,eargs,cgs); 
      if (needsBracket) 
      { return "(" + res + ")"; } 
      else 
      { return res; } 
    } 
    return etext;
  }

}


