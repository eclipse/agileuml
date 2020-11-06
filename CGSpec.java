/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: CSTL */ 

import java.util.Vector; 
import java.io.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class CGSpec
{ Vector typeDefinitionRules;
  Vector typeUseRules;
  Vector basicExpressionRules;
  Vector unaryExpressionRules;
  Vector conditionalExpressionRules;
  Vector binaryExpressionRules;
  Vector setExpressionRules;
  Vector statementRules;
  Vector classRules;
  Vector packageRules;
  Vector usecaseRules;
  Vector attributeRules;
  Vector parameterRules;
  Vector parameterArgumentRules; 
  Vector operationRules;
  Vector enumerationRules;
  Vector textRules; 
  Vector entities; 

  public CGSpec(Vector ents)
  { typeDefinitionRules = new Vector();
    typeUseRules = new Vector();
    basicExpressionRules = new Vector();
    unaryExpressionRules = new Vector();
    binaryExpressionRules = new Vector();
    conditionalExpressionRules = new Vector();
    setExpressionRules = new Vector();
    statementRules = new Vector();
    classRules = new Vector();
    packageRules = new Vector();
    usecaseRules = new Vector();
    attributeRules = new Vector();
    parameterRules = new Vector();
	parameterArgumentRules = new Vector(); 
    operationRules = new Vector();
    enumerationRules = new Vector();
    textRules = new Vector();
    entities = ents;  
  }

  public void addTypeUseRule(CGRule r)
  { typeUseRules.add(r); }

  public void addUnaryExpressionRule(CGRule r)
  { unaryExpressionRules.add(r); }

  public void addBinaryExpressionRule(CGRule r)
  { binaryExpressionRules.add(r); }

  public void addSetExpressionRule(CGRule r)
  { setExpressionRules.add(r); }

  public void addBasicExpressionRule(CGRule r)
  { basicExpressionRules.add(r); }

  public void addConditionalExpressionRule(CGRule r)
  { conditionalExpressionRules.add(r); }

  public void addStatementRule(CGRule r)
  { Vector removedRules = new Vector(); 
    for (int i = 0; i < statementRules.size(); i++) 
	{ CGRule rule = (CGRule) statementRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	statementRules.removeAll(removedRules); 
	statementRules.add(r); 
  }


  public void addClassRule(CGRule r)
  { Vector removedRules = new Vector(); 
    for (int i = 0; i < classRules.size(); i++) 
	{ CGRule rule = (CGRule) classRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	classRules.removeAll(removedRules); 
	classRules.add(r); 
  }

  public void addPackageRule(CGRule r)
  { r.metafeatures = r.metafeatures(r.rhs);
    packageRules.add(r); 
  }

  public void addUseCaseRule(CGRule r)
  { Vector removedRules = new Vector(); 
    for (int i = 0; i < usecaseRules.size(); i++) 
	{ CGRule rule = (CGRule) usecaseRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	usecaseRules.removeAll(removedRules); 
	usecaseRules.add(r); 
  }

  public void addAttributeRule(CGRule r)
  { Vector removedRules = new Vector(); 
    for (int i = 0; i < attributeRules.size(); i++) 
	{ CGRule rule = (CGRule) attributeRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	attributeRules.removeAll(removedRules); 
	attributeRules.add(r); 
  }

  public void addParameterRule(CGRule r)
  { parameterRules.add(r); }

  public void addParameterArgumentRule(CGRule r)
  { parameterArgumentRules.add(r); }

  public void addOperationRule(CGRule r)
  { Vector removedRules = new Vector(); 
    for (int i = 0; i < operationRules.size(); i++) 
	{ CGRule rule = (CGRule) operationRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	operationRules.removeAll(removedRules); 
	operationRules.add(r); 
  }

  public void addEnumerationRule(CGRule r)
  { enumerationRules.add(r); }

  public void addTextRule(CGRule r)
  { // r.lhspattern = r.convertToPattern(r.lhs);
    // r.lhspatternlist = r.convertToPatterns(r.lhs);           
    textRules.add(r);
  }

  public String toString()
  { String res = ""; 

    if (packageRules.size() > 0) 
    { res = res + "Package::\n";
      for (int x = 0; x < packageRules.size(); x++)
      { CGRule r = (CGRule) packageRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (enumerationRules.size() > 0) 
    { res = res + "Enumeration::\n";
      for (int x = 0; x < enumerationRules.size(); x++)
      { CGRule r = (CGRule) enumerationRules.get(x);
        res = res + r + "\n"; 
      } 
    }   
    res = res + "\n"; 

    if (typeUseRules.size() > 0) 
    { res = res + "Type::\n";
      for (int x = 0; x < typeUseRules.size(); x++)
      { CGRule r = (CGRule) typeUseRules.get(x);
        res = res + r + "\n"; 
      } 
    }   
    res = res + "\n"; 

    if (classRules.size() > 0) 
    { res = res + "Class::\n";
      for (int x = 0; x < classRules.size(); x++)
      { CGRule r = (CGRule) classRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (attributeRules.size() > 0) 
    { res = res + "Attribute::\n";
      for (int x = 0; x < attributeRules.size(); x++)
      { CGRule r = (CGRule) attributeRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (operationRules.size() > 0) 
    { res = res + "Operation::\n";
      for (int x = 0; x < operationRules.size(); x++)
      { CGRule r = (CGRule) operationRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (parameterRules.size() > 0) 
    { res = res + "Parameter::\n";
      for (int x = 0; x < parameterRules.size(); x++)
      { CGRule r = (CGRule) parameterRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (parameterArgumentRules.size() > 0) 
    { res = res + "ParameterArgument::\n";
      for (int x = 0; x < parameterArgumentRules.size(); x++)
      { CGRule r = (CGRule) parameterArgumentRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (statementRules.size() > 0) 
    { res = res + "Statement::\n";
      for (int x = 0; x < statementRules.size(); x++)
      { CGRule r = (CGRule) statementRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (basicExpressionRules.size() > 0) 
    { res = res + "BasicExpression::\n";
      for (int x = 0; x < basicExpressionRules.size(); x++)
      { CGRule r = (CGRule) basicExpressionRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (unaryExpressionRules.size() > 0) 
    { res = res + "UnaryExpression::\n";
      for (int x = 0; x < unaryExpressionRules.size(); x++)
      { CGRule r = (CGRule) unaryExpressionRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (binaryExpressionRules.size() > 0) 
    { res = res + "BinaryExpression::\n";
      for (int x = 0; x < binaryExpressionRules.size(); x++)
      { CGRule r = (CGRule) binaryExpressionRules.get(x);
        res = res + r + "\n"; 
      } 
    }
    res = res + "\n"; 

    if (setExpressionRules.size() > 0) 
    { res = res + "SetExpression::\n";
      for (int x = 0; x < setExpressionRules.size(); x++)
      { CGRule r = (CGRule) setExpressionRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (conditionalExpressionRules.size() > 0) 
    { res = res + "ConditionalExpression::\n";
      for (int x = 0; x < conditionalExpressionRules.size(); x++)
      { CGRule r = (CGRule) conditionalExpressionRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (usecaseRules.size() > 0) 
    { res = res + "UseCase::\n";
      for (int x = 0; x < usecaseRules.size(); x++)
      { CGRule r = (CGRule) usecaseRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    if (textRules.size() > 0) 
    { res = res + "Text::\n";
      for (int x = 0; x < textRules.size(); x++)
      { CGRule r = (CGRule) textRules.get(x);
        res = res + r + "\n"; 
      } 
    }

    res = res + "\n"; 

    return res; 
  } 

  public void transformPackage(String typestring, String classesstring, String ucstring,
                               Vector types, Vector entities, Vector useCases, 
                               PrintWriter out) 
  { if (packageRules.size() > 0) 
    { CGRule r = (CGRule) packageRules.get(0);
      r.metafeatures = r.metafeatures(r.rhs); 
      // System.out.println(">>> " + r + " metafeatures = " + r.metafeatures);
	  
      Vector args = new Vector();
      Vector eargs = new Vector(); 
	  
      args.add("MainApp");  
      args.add(typestring); 
      args.add(classesstring);
      args.add(ucstring); 

      eargs.add("MainApp"); 
      eargs.add(types); 
      eargs.add(entities);  
      eargs.add(useCases); 

      String res = r.applyRule(args,eargs,this);
      displayText(res,out); 
      return;  
    } 
    displayText(typestring,out); 
    out.println(); 
    out.println(); 
    displayText(classesstring,out); 
  }  

  public void displayText(String str, PrintWriter out) 
  { int n = str.length();
    StringBuffer res = new StringBuffer(); 
 
    for (int i = 0; i < n; i++) 
    { char x = str.charAt(i); 
      if (x == '\n')
      { out.println(res); 
        res = new StringBuffer(); 
      } 
      else if (i < n-1 && x == '\\' && str.charAt(i+1) == 'n')
      { out.println(res); 
        res = new StringBuffer(); 
        i++; 
      } 
      else 
      { res.append(x); } 
    }
    out.println(res);  
  }       

  public CGRule matchedEnumerationRule(Object t, String typetext)
  { for (int x = 0; x < enumerationRules.size(); x++)
    { CGRule r = (CGRule) enumerationRules.get(x);
      if (typetext.equals(r.lhs))
      { return r; } // exact match
      else if (t instanceof Type && 
               ((Type) t).isEnumeratedType() && r.lhs.startsWith("enumeration"))
      { return r; }
      else if (t instanceof Vector && r.lhs.startsWith("literal") && r.lhs.indexOf(",") > -1)
      { return r; } 
      else if (t instanceof EnumLiteral && r.lhs.startsWith("literal") && 
               r.lhs.indexOf(",") < 0)
      { return r; }
    }
    return null;
  } 

  public CGRule matchedTypeUseRule(Type t, String typetext)
  { String tname = t.getName(); 
    Type elemT = t.getElementType(); 
	
    for (int x = 0; x < typeUseRules.size(); x++)
    { CGRule r = (CGRule) typeUseRules.get(x);
      String trimmedlhs = r.lhs.trim(); 
      // int varcount = r.variables().size(); 
	  
      if (typetext.equals(trimmedlhs))
      { return r; } // exact match
      else if (t.isMapType() && trimmedlhs.startsWith("Map"))
      { return r; }
      else if (t.isEnumeratedType() && r.hasCondition("enumerated"))
      { return r; }
      else if (t.isEntityType() && r.hasCondition("class"))
      { return r; }
      else if (t.isSetType() && trimmedlhs.startsWith("Set"))
      { if (elemT != null && trimmedlhs.equals("Set(" + elemT + ")"))
	    { return r; }
		else if (elemT == null && trimmedlhs.equals("Set()"))
		{ return r; }
		else if (trimmedlhs.equals("Set(_1)"))
		{ return r; } 
	  } 
      else if (t.isSequenceType() && trimmedlhs.startsWith("Sequence"))
      { if (elemT != null && trimmedlhs.equals("Sequence(" + elemT + ")"))
	    { return r; }
		else if (elemT == null && trimmedlhs.equals("Sequence()"))
		{ return r; }
		else if (trimmedlhs.equals("Sequence(_1)"))
		{ return r; }
	  } 
    }
    return null;
  } // _1 binds to type or elementType

  public CGRule matchedUnaryExpressionRule(UnaryExpression e, String etext)
  { String op = e.getOperator();
    Expression arg = e.getArgument();
    Vector args = new Vector(); 
    args.add(arg); 

    for (int x = 0; x < unaryExpressionRules.size(); x++)
    { CGRule r = (CGRule) unaryExpressionRules.get(x);
      CGRule selected = null; 

      if (etext.equals(r.lhs))
      { selected = r; } // exact match
      else if (op.startsWith("->") && r.lhs.endsWith(op + "()"))
      { selected = r; }
      else if (etext.startsWith(op) && r.lhs.startsWith(op))
      { selected = r; }

      if (selected != null && selected.satisfiesConditions(args,entities))
      { return selected; } 
   }
   return null;
  } // _1 binds to argument

  public CGRule matchedStatementRule(Statement e, String etext)
  { String op = e.getOperator();

    for (int x = 0; x < statementRules.size(); x++)
    { CGRule r = (CGRule) statementRules.get(x);
      String trimmedlhs = r.lhs.trim(); 

      if (etext.equals(trimmedlhs))
      { return r; } // exact match
      else if (op.equals("while") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("for") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("return") && trimmedlhs.startsWith(op))
      { if (((ReturnStatement) e).hasValue() && r.hasVariables())
        { return r; }
        else 
        { if (!((ReturnStatement) e).hasValue() && !r.hasVariables())
          { return r; } 
        } 
      } 
      else if (op.equals("if") && trimmedlhs.startsWith(op) && (e instanceof ConditionalStatement))
      { // ConditionalStatement cs = (ConditionalStatement) e; 
        // if ((cs.getTest() + "").equals("true") && 
        //     r.lhs.startsWith("if true"))
        // { return r; }
        // else if ((cs.getTest() + "").equals("false") &&
        //          r.lhs.startsWith("if false"))
        // { return r; } 
        // else 
        { return r; } 
      } 
      else if (op.equals("if") && trimmedlhs.startsWith(op) && (e instanceof IfStatement))
      { // IfStatement cs = (IfStatement) e; 
        // if ((cs.getTest() + "").equals("true") && 
        //     r.lhs.startsWith("if true"))
        // { return r; }
        // else if ((cs.getTest() + "").equals("false") &&
        //          r.lhs.startsWith("if false"))
        // { return r; } 
        // else 
        { return r; } 
      } 
      else if (op.equals("break") && trimmedlhs.startsWith(op))
      { return r; }
      else if (e instanceof SequenceStatement && (trimmedlhs.indexOf(";") > -1))
      { return r; }
      else if (op.equals("var") && trimmedlhs.startsWith(op))
      { Vector args = ((CreationStatement) e).cgparameters(); 
        if (r.satisfiesConditions(args,entities))
        { return r; }
      } 
      else if (op.equals(":=") && (trimmedlhs.indexOf(op) > -1))
      { Vector args = ((AssignStatement) e).cgparameters(); 
        if (r.satisfiesConditions(args,entities))
        { return r; }
      }
      else if (op.equals("execute") && trimmedlhs.startsWith(op))
      { Vector args = ((ImplicitInvocationStatement) e).cgparameters(); 
        if (r.satisfiesConditions(args,entities))
        { return r; }
      }
      else if (op.equals("call") && trimmedlhs.startsWith(op))
      { Vector args = ((InvocationStatement) e).cgparameters();
	    System.out.println(">>> Call statement " + e + " matches rule " + r); 
		 
        if (r.satisfiesConditions(args,entities))
        { return r; }
      }
   }
   return null;
  } 

  public CGRule matchedBinaryExpressionRule(BinaryExpression e, String etext)
  { String op = e.getOperator();

    if ("/=".equals(op))
    { op = "<>"; } 

    Vector args = new Vector(); 
    if ("|".equals(op))
    { op = "->select";
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("|C".equals(op))
    { op = "->collect"; 
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("|R".equals(op))
    { op = "->reject"; 
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    if ("!".equals(op))
    { op = "->forAll";
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("#".equals(op))
    { op = "->exists"; 
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("#1".equals(op))
    { op = "->exists1"; 
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("|A".equals(op))
    { op = "->any"; 
      BinaryExpression beleft = (BinaryExpression) e.getLeft();  
      args.add(beleft.getRight()); 
      args.add(beleft.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("->collect".equals(op) || "->reject".equals(op) || "->any".equals(op) || 
             "->select".equals(op) || "->exists".equals(op) || 
             "->exists1".equals(op) || "->forAll".equals(op) ||
             "->isUnique".equals(op) || "->sortedBy".equals(op))
    { args.add(e.getLeft());
      BasicExpression v = new BasicExpression("v"); 
      v.setType(e.getLeft().getElementType()); 
      args.add(v); 
      args.add(e.getRight()); 
	  // add v as a reference to the right? 
    } // and for ->sortedBy, etc
    else 
    { args.add(e.getLeft()); 
      args.add(e.getRight()); 
    } 

    for (int x = 0; x < binaryExpressionRules.size(); x++)
    { CGRule r = (CGRule) binaryExpressionRules.get(x);
      CGRule selected = null; 
	  Expression rexpr = r.getLhsExpression(); 

      if (etext.equals(r.lhs))
      { selected = r; } // exact match
      else if (op.startsWith("->") && (r.lhs.indexOf(op) > -1))
      { selected = r; }
      else if (r.lhsexp != null && rexpr instanceof BinaryExpression && 
	           ((BinaryExpression) rexpr).getOperator().equals(op))
      { selected = r; } 
	  // else if (r.lhs.indexOf(op) > -1)
      // { selected = r; }

      if (selected != null && selected.satisfiesConditions(args,entities))
      { return selected; } 
   }
   return null;
  } // _1 binds to left, _2 to right

  public CGRule matchedConditionalExpressionRule(ConditionalExpression e, String etext)
  { // Vector args = new Vector(); 
    // args.add(e.getTest()); 
    // args.add(e.getIfExp()); 
    // args.add(e.getElseExp()); 

    for (int x = 0; x < conditionalExpressionRules.size(); x++)
    { CGRule r = (CGRule) conditionalExpressionRules.get(x);
      CGRule selected = null; 

      // if ((e.getTest() + "").equals("true") && r.lhs.startsWith("if true"))
      // { selected = r; } // exact match
      // else if ((e.getTest() + "").equals("false") && r.lhs.startsWith("if false"))
      // { selected = r; }
      // else 
      { selected = r; }

      if (selected != null)
      { return selected; } 
   }
   return null;
  } 

  public CGRule matchedBasicExpressionRule(BasicExpression e, String etext, Vector matchedtextrules)
  { Expression obj = e.getObjectRef();
    Expression ind = e.getArrayIndex();
    Vector pars = e.getParameters(); 
    
	BasicExpression cl = (BasicExpression) e.clone(); 
	cl.objectRef = null; 
	cl.setParameters(null);
	// c1.typeCheck(types,entities,context,env);  
	// But the type and multiplicity can actually be different to e's. 
	
    for (int x = 0; x < basicExpressionRules.size(); x++)
    { CGRule r = (CGRule) basicExpressionRules.get(x);
      CGRule selected = null; 
      Vector args = new Vector(); 
      String trimmedlhs = r.lhs.trim(); 

      if (etext.equals(r.lhs) && r.variableCount() == 0)
      { return r; } // exact match
      // else if ("null".equals(r.lhs) && "null".equals(etext))
      // { return r; } 
      else if (e.data.equals("allInstances") && trimmedlhs.equals("_1.allInstances"))
      { return r; }
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("subrange") && "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.subrange(_1,_2)"))
      { return r; }  
      else if (pars != null && pars.size() == 4 && 
               e.data.equals("Sum") && "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.Sum(_1,_2,_3,_4)"))
      { return r; }  
      else if (pars != null && pars.size() == 4 && 
               e.data.equals("Prd") && "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.Prd(_1,_2,_3,_4)"))
      { return r; }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("subrange") && trimmedlhs.equals("_1.subrange(_2,_3)"))
      { selected = r; 
	    args.add(obj); 
		args.add(pars.get(0)); 
		args.add(pars.get(1)); 
	  }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("insertAt") && trimmedlhs.equals("_1.insertAt(_2,_3)"))
      { selected = r; 
	    args.add(obj); 
	    args.add(pars.get(0)); 
	    args.add(pars.get(1)); 
	  }  
      else if (ind != null && pars == null && trimmedlhs.equals("_1[_2]"))
      { selected = r;
	    BasicExpression e1 = (BasicExpression) e.clone(); 
	    e1.arrayIndex = null; 
        args.add(e1); // But again the type can be different. 
        args.add(ind); // condition is on _2 
      }
      else if (ind == null && obj != null && pars == null && trimmedlhs.equals("_1._2"))
      { selected = r; 
	    args.add(obj); 
	    args.add(cl); 
	  }
      else if (obj != null && ind == null && pars != null && trimmedlhs.equals("_1._2(_3)"))
      { selected = r; 
	    args.add(obj); 
	    args.add(cl); 
	    args.add(pars); 
	  }
      else if (obj == null && ind == null && pars != null && trimmedlhs.equals("_1(_2)"))
      { selected = r; 
        args.add(cl); 
        args.add(pars); 
      }
      else if (obj == null && ind == null && pars == null && trimmedlhs.equals("_1"))
      { selected = r; 
        args.add(e); 
      }
	  else if (trimmedlhs.endsWith("_1(_2)") && pars != null && ModelElement.haveCommonPrefix(etext,trimmedlhs))
	  { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs);
	    if (prefix.length() == trimmedlhs.length() - 6) 
        { selected = r; 
          args.add(e);   // should be for _1 
          args.add(pars);
          matchedtextrules.add(r);
		}  
	  }
	  else if (trimmedlhs.endsWith("(_1)") && pars != null && ModelElement.haveCommonPrefix(etext,trimmedlhs))
	  { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs); 
        if (prefix.length() == trimmedlhs.length() - 4) 
		{ selected = r; 
          args.add(pars);
          matchedtextrules.add(r);
		}  
	  }
	  else if (trimmedlhs.endsWith("_1()") && (pars == null || pars.size() == 0) && 
	           ModelElement.haveCommonPrefix(etext,trimmedlhs))
	  { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs); 
        if (prefix.length() == trimmedlhs.length() - 4)
		{ selected = r; 
          args.add(e);
          matchedtextrules.add(r);
		}  
	  }

      if (selected != null && selected.satisfiesConditions(args,entities))
      { return selected; } 
    }
    return null;
  } // _1 binds to objectRef or left part, _2 to right part/index

  public CGRule matchedSetExpressionRule(SetExpression e, String etext)
  { boolean ordered = e.isOrdered();
    Vector elems = e.getElements(); 

    for (int x = 0; x < setExpressionRules.size(); x++)
    { CGRule r = (CGRule) setExpressionRules.get(x);
      if (etext.equals(r.lhs))
      { return r; } // exact match
      else if (etext.startsWith("Set{") && etext.endsWith("}") && 
               r.lhs.startsWith("Set{") && r.lhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0)
        { return r; } // r is empty set -- just white space in lhs between {}
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { return r; } 
      } 
      else if (etext.startsWith("Sequence{") && etext.endsWith("}") && 
               r.lhs.startsWith("Sequence{") && r.lhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0)
        { return r; } // r is empty set
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { return r; } 
      } 
      else if (etext.startsWith("Map{") && etext.endsWith("}") && 
               r.lhs.startsWith("Map{") && r.lhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0) 
        { return r; }  // empty map
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { return r; } 
      } 
    }
    return null;
  } // _1 binds to elements if any

  public CGRule matchedEntityRule(Entity e, String ctext)
  { Vector args = new Vector(); 
    args.add(e); 

    CGRule selected = null; 
	
	// System.out.println(">>> Class " + e.getName() + " isAbstract= " + e.isAbstract() + 
	//                    " extends= " + e.getSuperclass() + " hasInterfaces= " + e.hasInterfaces()); 
    // System.out.println(); 

    for (int x = 0; x < classRules.size(); x++)
    { CGRule r = (CGRule) classRules.get(x);
      if (ctext.equals(r.lhs))
      { selected = r; } // exact match
      else if (e.isInterface() && r.lhs.indexOf("interface") > -1)
      { selected = r; } 
      else if (e.isAbstract() && r.lhs.indexOf("abstract") > -1)
      { if (e.getSuperclass() != null && r.lhs.indexOf("extends") > -1)     
        { if (e.hasInterfaces()  && r.lhs.indexOf("implements") > -1) 
		  { selected = r; }
          else if (!e.hasInterfaces() && r.lhs.indexOf("implements") < 0) 
		  { selected = r; } 
		} 
	    else if (e.getSuperclass() == null & r.lhs.indexOf("extends") < 0) 
		{ if (e.hasInterfaces()  && r.lhs.indexOf("implements") > -1)     
          { selected = r; }
		  else if (!e.hasInterfaces() && r.lhs.indexOf("implements") < 0)
		  { selected = r; }
		}
      }
      else if (!e.isAbstract() && r.lhs.indexOf("abstract") < 0)
	  { if (e.getSuperclass() != null &&  r.lhs.indexOf("extends") > -1)   
        { if (e.hasInterfaces() && r.lhs.indexOf("implements") > -1)     
          { selected = r; }
          else if (!e.hasInterfaces() && r.lhs.indexOf("implements") < 0)     
          { selected = r; }
        } 
		else if (e.getSuperclass() == null && r.lhs.indexOf("extends") < 0)
        { if (e.hasInterfaces() && r.lhs.indexOf("implements") > -1)     
          { selected = r; }
          else if (!e.hasInterfaces() && r.lhs.indexOf("implements") < 0)     
          { selected = r; }
        }
	  } 
	  
      if (selected != null && selected.satisfiesConditions(args,entities))
      { return selected; } 

    }
    return null;
  } // _1 binds to class name

  public CGRule matchedAttributeRule(Attribute e, String ctext)
  { CGRule selected = null;

    Vector args = new Vector(); 
    args.add(e); 
    args.add(e.getType());  
  
    for (int x = 0; x < attributeRules.size(); x++)
    { CGRule r = (CGRule) attributeRules.get(x);

      if (r.lhs.indexOf("attribute") > -1) { } 
      else 
      { continue; } 

      if (ctext.equals(r.lhs) && r.variableCount() == 0)
      { selected = r; } // exact match
      else if (e.isStatic())
      { if (r.lhs.indexOf("static") > -1)     
        { selected = r; }
      }
      else if (r.hasCondition("primary") && e.isPrimaryAttribute())
      { selected = r; } 
      else if (e.isUnique())
      { if (r.lhs.indexOf("identity") > -1)     
        { selected = r; }
      }
      else if (r.lhs.indexOf("identity") < 0 && r.lhs.indexOf("static") < 0)   
      { selected = r; }
    
	  if (selected != null && selected.satisfiesConditions(args,entities))
      { return selected; } 
	}

    return null;
  } 

  public CGRule matchedReferenceRule(Attribute e, String ctext)
  { for (int x = 0; x < attributeRules.size(); x++)
    { CGRule r = (CGRule) attributeRules.get(x);

      if (r.lhs.startsWith("reference")) 
      { if (e.getType() != null && e.getType().isCollectionType()) 
        { if (r.hasCondition("collection"))
          { return r; }
          else if (r.hasNegativeCondition("collection"))
          { } 
          else 
          { return r; } 
        } 
        else if (e.getType() != null && !e.getType().isCollectionType())
        { if (r.hasNegativeCondition("collection"))
          { return r; } 
          else if (r.hasCondition("collection"))
          { } 
          else 
          { return r; }
        }
      }  
    }
    return null;
  } 

  public CGRule matchedParameterRule(Attribute e, Vector rem, String ctext)
  { for (int x = 0; x < parameterRules.size(); x++)
    { CGRule r = (CGRule) parameterRules.get(x);


      if (ctext.equals(r.lhs))
      { return r; } // exact match
      else if (r.lhs.indexOf(",") > -1 && rem.size() > 0) 
      { return r; } 
      else if (r.lhs.indexOf(",") < 0 && rem.size() == 0)
      { return r; }
    }
    return null;
  } 

  public CGRule matchedParameterArgumentRule(Expression e, Vector rem, String ctext)
  { for (int x = 0; x < parameterArgumentRules.size(); x++)
    { CGRule r = (CGRule) parameterArgumentRules.get(x);


      if (ctext.equals(r.lhs))
      { return r; } // exact match
      else if (r.lhs.indexOf(",") > -1 && rem.size() > 0) 
      { return r; } 
      else if (r.lhs.indexOf(",") < 0 && rem.size() == 0)
      { return r; }
    }
    return null;
  } 

  public CGRule matchedOperationRule(BehaviouralFeature e, String ctext)
  { for (int x = 0; x < operationRules.size(); x++)
    { CGRule r = (CGRule) operationRules.get(x);
      if (ctext.equals(r.lhs))
      { return r; } // exact match
      else if (e.isQuery())
      { if (r.lhs.indexOf("query") > -1) 
        { if (e.isStatic())
          { if (r.lhs.indexOf("static") > -1)     
            { return r; }
          } 
          else if (r.lhs.indexOf("static") < 0)
          { return r; } 
        } 
      }
      else if (r.lhs.indexOf("operation") > -1) 
      { if (e.isStatic())
        { if (r.lhs.indexOf("static") > -1)     
          { return r; }
        }
        else if (r.lhs.indexOf("static") < 0)   
        { return r; }
      } 
    }
    return null;
  } // distinguish queries and operations 

  public CGRule matchedUsecaseRule(UseCase e, String ctext)
  { for (int x = 0; x < usecaseRules.size(); x++)
    { CGRule r = (CGRule) usecaseRules.get(x);
      return r; 
    } 
    return null;
  } // distinguish operation descriptions and use cases. 

  public CGRule matchedTextRule(String text)
  { String realtext = "";
     
    /* Vector chars = new Vector(); 
    for (int i = 0; i < text.length(); i++) 
	{ realtext = realtext + text.charAt(i); 
	  chars.add(text.charAt(i)+""); 
	} */ 
  
    for (int i = 0; i < textRules.size(); i++) 
    { CGRule r = (CGRule) textRules.get(i); 
	

	 // String lhspattern = r.convertToPattern(r.lhs);
 
	 // if (lhspattern != null && lhspattern.length() > 0)
     // { Pattern expr = Pattern.compile(lhspattern); 

       // Matcher m = expr.matcher(text); 
	
       // boolean found = m.find();
	   // System.out.println(m);
       // if (found)
	   // { return r; }
       // else 
	   { Vector matchings = new Vector(); 
         boolean found = r.checkPatternList(text,matchings);
         if (found) 
         { return r; }  
       }
	 // }
   }
   return null;
  } 

} 

