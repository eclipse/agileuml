/******************************
* Copyright (c) 2003--2024 Kevin Lano
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
  Vector datatypeRules;
  Vector textRules; 

  Vector entities; 
  Vector types; 

  java.util.Map categoryRules = new java.util.TreeMap(); 
                // String -> Vector of CGRule
  java.util.Map umlRules = new java.util.HashMap(); 
                // String -> Vector of CGRule

  public CGSpec(Vector ents, Vector typs)
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
    datatypeRules = new Vector();
    textRules = new Vector();

    // umlRules.put("", typeDefinitionRules); 
    umlRules.put("Type", typeUseRules); 
    umlRules.put("BasicExpression", basicExpressionRules); 
    umlRules.put("BinaryExpression", binaryExpressionRules); 
    umlRules.put("ConditionalExpression", conditionalExpressionRules); 
    umlRules.put("UnaryExpression", unaryExpressionRules); 
    umlRules.put("SetExpression", setExpressionRules); 
    umlRules.put("Statement", statementRules); 
    umlRules.put("Class", classRules); 
    umlRules.put("UseCase", usecaseRules); 
    umlRules.put("Attribute", attributeRules); 
    umlRules.put("Operation", operationRules); 
    umlRules.put("Enumeration", enumerationRules); 
    umlRules.put("Datatype", datatypeRules); 
    umlRules.put("Attribute", attributeRules); 
    umlRules.put("Package", packageRules); 
    umlRules.put("Parameter", parameterRules); 
    umlRules.put("ParameterArgument", parameterArgumentRules); 
   

    entities = ents;
    types = typs;   
  }

  public void setTypes(Vector typs)
  { types = typs; } 

  public void setEntities(Vector ents)
  { entities = ents; } 

  public void addCategory(String category)
  { Vector rules = (Vector) categoryRules.get(category); 
    if (rules == null) 
    { rules = new Vector(); } 
    categoryRules.put(category,rules); 
  } 

  public void addCategoryRule(String category, CGRule r)
  { Vector rules = (Vector) categoryRules.get(category); 
    if (rules == null) 
    { rules = new Vector(); } 
    if (rules.contains(r)) 
    { return; } 
    rules.add(r); 
    r.setRuleset(category); 
    categoryRules.put(category,rules); 
  } 

  public void addInitialCategoryRule(String category, CGRule r)
  { Vector rules = (Vector) categoryRules.get(category); 
    if (rules == null) 
    { rules = new Vector(); } 
    rules.add(0,r); 
    r.setRuleset(category); 
    categoryRules.put(category,rules); 
  } 

  public void addCategoryRuleInOrder(String category, CGRule r)
  { Vector rules = (Vector) categoryRules.get(category); 
    if (rules == null) 
    { rules = new Vector(); } 

    if (rules.contains(r)) { } 
    else 
    { rules = insertRuleInOrder(r,rules); 
      r.setRuleset(category); 
    } 

    categoryRules.put(category,rules); 
  } 

  private Vector insertRuleInOrder(CGRule r, Vector rs)
  { Vector res = new Vector(); 

    if (rs.contains(r))
    { return rs; } 

    if (rs.size() == 0)
    { res.add(r); 
      return res; 
    } 

    CGRule fst = (CGRule) rs.get(0); 
    Vector newres = new Vector(); 
    int ind = 0; 

    while (ind < rs.size())
    { int ordering = r.compareTo(fst); 

      // System.out.println("++++ " + r + ".compareTo(" + fst + ") = " + ordering); 

      if (ordering == 1) 
      { newres.add(fst); } 
      else if (ordering == Integer.MAX_VALUE)
      { newres.add(fst); } 
      // else if (ordering == 0) 
      // { newres.add(fst); } 
      else if (ordering == -1 || 
               ordering == 0)
      { break; } 

      if (ind + 1 < rs.size())
      { ind++; 
        fst = (CGRule) rs.get(ind);  
      } 
      else 
      { break; } 
    } 

    if (r.compareTo(fst) == 0 || r.equals(fst)) 
    { } 
    else 
    { newres.add(r); }  

    while (ind < rs.size())
    { if (newres.contains(fst)) { } 
      else 
      { newres.add(fst); }

      if (ind + 1 < rs.size())
      { ind++; 
        fst = (CGRule) rs.get(ind);  
      } 
      else 
      { break; } 
    } 

    return newres; 
  } 

  public boolean hasRuleset(String category)
  { Vector rules = (Vector) categoryRules.get(category); 
    if (rules == null) 
    { return false; } 
    return true; 
  } 


  public Vector getRulesForCategory(String category)
  { Vector res = (Vector) categoryRules.get(category); 
    if (res == null) 
    { System.err.println("! Warning: no rules for category " + category); 
      res = new Vector(); 
    } 
    return res; 
  } 

  public String applyRuleset(String category, ASTTerm obj)
  { Vector res = (Vector) categoryRules.get(category); 
    if (res == null) 
    { System.err.println("!! Warning: no rules for category " + category); 
      return obj + ""; 
    } 
    return obj.cgRules(this,res); 
  } 

/*  public String applyUMLRuleset(String category, ModelElement obj)
  { Vector res = (Vector) categoryRules.get(category); 
    if (res == null) 
    { System.err.println("!! Warning: no rules for category " + category); 
      return obj + ""; 
    } 
    
  } */  

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
   /* for (int i = 0; i < statementRules.size(); i++) 
	{ CGRule rule = (CGRule) statementRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	statementRules.removeAll(removedRules); */ 
	statementRules.add(r); 
  }


  public void addClassRule(CGRule r)
  { Vector removedRules = new Vector(); 
   /* for (int i = 0; i < classRules.size(); i++) 
	{ CGRule rule = (CGRule) classRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	classRules.removeAll(removedRules); */  
	classRules.add(r); 
  }

  public void addPackageRule(CGRule r)
  { r.metafeatures = r.metafeatures(r.rhs);
    packageRules.add(r); 
  }

  public void addUseCaseRule(CGRule r)
  { Vector removedRules = new Vector(); 
    /* for (int i = 0; i < usecaseRules.size(); i++) 
	{ CGRule rule = (CGRule) usecaseRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	usecaseRules.removeAll(removedRules); */ 
	usecaseRules.add(r); 
  }

  public void addAttributeRule(CGRule r)
  { Vector removedRules = new Vector(); 
    /* for (int i = 0; i < attributeRules.size(); i++) 
	{ CGRule rule = (CGRule) attributeRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	attributeRules.removeAll(removedRules); */  
	attributeRules.add(r); 
  }

  public void addParameterRule(CGRule r)
  { parameterRules.add(r); }

  public void addParameterArgumentRule(CGRule r)
  { parameterArgumentRules.add(r); }

  public void addOperationRule(CGRule r)
  { Vector removedRules = new Vector(); 
  /*  for (int i = 0; i < operationRules.size(); i++) 
	{ CGRule rule = (CGRule) operationRules.get(i); 
	  if (r.equalsLHS(rule)) 
	  { removedRules.add(rule); } 
	} 
	operationRules.removeAll(removedRules); */  
	operationRules.add(r); 
  }

  public void addEnumerationRule(CGRule r)
  { enumerationRules.add(r); }

  public void addDatatypeRule(CGRule r)
  { datatypeRules.add(r); }

  public void addTextRule(CGRule r)
  { // r.lhspattern = r.convertToPattern(r.lhs);
    // r.lhspatternlist = r.convertToPatterns(r.lhs);           
    textRules.add(r);
  }

  public void replaceParameter(String str)
  { for (int i = 0; i < textRules.size(); i++) 
    { CGRule r = (CGRule) textRules.get(i); 
      r.replaceParameter(str); 
    } 
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

    if (datatypeRules.size() > 0) 
    { res = res + "Datatype::\n";
      for (int x = 0; x < datatypeRules.size(); x++)
      { CGRule r = (CGRule) datatypeRules.get(x);
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


    java.util.Set ks = categoryRules.keySet(); 
    Vector catkeys = new Vector(); 
    catkeys.addAll(ks); 
    for (int p = 0; p < catkeys.size(); p++) 
    { String catg = (String) catkeys.get(p); 
      Vector crules = (Vector) categoryRules.get(catg); 
      if (crules != null && crules.size() > 0) 
      { res = res + catg + "::\n";
        for (int x = 0; x < crules.size(); x++)
        { CGRule r = (CGRule) crules.get(x);
          res = res + r + "\n"; 
        } 
      }
      res = res + "\n"; 
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
 
    boolean instring = false; 

    for (int i = 0; i < n; i++) 
    { char x = str.charAt(i); 

      if (x == '"' && !instring) 
      { instring = true; } 
      else if (x == '"' && instring) 
      { instring = false; } 

      if (x == '\n' && !instring)
      { out.println(res); 
        res = new StringBuffer(); 
      } 
      else if (i < n-1 && x == '\\' && str.charAt(i+1) == 'n' && !instring)
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

  public CGRule matchedDatatypeRule(Object t, String typetext)
  { for (int x = 0; x < datatypeRules.size(); x++)
    { CGRule r = (CGRule) datatypeRules.get(x);
      if (typetext.equals(r.lhs))
      { return r; } // exact match
      else if (t instanceof Type && 
               ((Type) t).isDatatype() && r.lhs.startsWith("datatype"))
      { return r; }
    }
    return null;
  } 

  public CGRule matchedTypeUseRule(Type t, String typetext)
  { String tname = t.getName(); 
    Type elemT = t.getElementType(); 
    Vector args = new Vector(); 
    

    
    for (int x = 0; x < typeUseRules.size(); x++)
    { CGRule selected = null; 
      CGRule r = (CGRule) typeUseRules.get(x);
      String trimmedlhs = r.lhs.trim(); 
      // int varcount = r.variables().size(); 
	  
	 // System.out.println(">>+ Type +> " + t + " is: " + typetext + " rule lhs: " + trimmedlhs); 
	  
      if (typetext.equals(trimmedlhs))
      { args.add(t); 
        selected = r; 
      } // exact match -- assume no variables
      // else if (t.isMapType() && trimmedlhs.startsWith("Map"))
      // { return r; }
      else if (t.isMapType() && 
               trimmedlhs.equals("Map(_1,_2)"))
      { args.add(t.getKeyType());
        args.add(t.getElementType());  
        selected = r; 
      }
      else if (t.isSortedMapType() && 
               trimmedlhs.equals("SortedMap(_1,_2)"))
      { args.add(t.getKeyType());
        args.add(t.getElementType());  
        selected = r; 
      }
      else if (t.isFunctionType() && 
               trimmedlhs.equals("Function(_1,_2)"))
      { args.add(t.getKeyType());
        args.add(t.getElementType());  
        selected = r; 
      }
      else if (t.isEnumeratedType() && 
               (r.hasCondition("enumerated") || 
                r.hasCondition("Enumerated")) )
      { args.add(t);
        selected = r; 
      }
      // else if (t.isEntityType() && r.hasCondition("class"))
      // { System.out.println(">> Condition class satisfied for " + t); 
      //   selected = r; 
      // }
      else if (t.isSetType() && trimmedlhs.startsWith("Set") )
      { if (elemT != null && 
            trimmedlhs.equals("Set(" + elemT + ")"))
        { args.add(elemT);
          selected = r; 
        }
        else if (elemT == null && 
                 (trimmedlhs.equals("Set()") ||
                  trimmedlhs.equals("Set")))
        { selected = r; }
        else if (elemT != null && trimmedlhs.equals("Set(_1)"))
        { args.add(elemT);
          selected = r; 
        } 
      } 
      else if (t.isSortedSetType() && 
               trimmedlhs.startsWith("SortedSet") )
      { if (elemT != null && 
            trimmedlhs.equals("SortedSet(" + elemT + ")"))
        { args.add(elemT);
          selected = r; 
        }
        else if (elemT == null && 
                 (trimmedlhs.equals("SortedSet()") ||
                  trimmedlhs.equals("SortedSet")))
        { selected = r; }
        else if (elemT != null && 
                 trimmedlhs.equals("SortedSet(_1)"))
        { args.add(elemT);
          selected = r; 
        } 
      } 
      else if (t.isSequenceType() && 
               trimmedlhs.startsWith("Sequence"))
      { if (elemT != null && 
            trimmedlhs.equals("Sequence(" + elemT + ")"))
        { args.add(elemT);
          selected = r; 
        }
        else if (elemT == null && 
                 (trimmedlhs.equals("Sequence()") || 
                  trimmedlhs.equals("Sequence")))
        { selected = r; }
        else if (elemT != null && trimmedlhs.equals("Sequence(_1)"))
        { args.add(elemT);
          selected = r; 
        }
      }
      else if (t.isRef() && 
               trimmedlhs.startsWith("Ref"))
      { if (elemT != null && trimmedlhs.equals("Ref(" + elemT + ")"))
        { args.add(elemT);
          selected = r; 
        }
        else if (elemT == null && 
                 (trimmedlhs.equals("Ref()") || 
                  trimmedlhs.equals("Ref")))
        { selected = r; }
        else if (elemT != null && trimmedlhs.equals("Ref(_1)"))
        { args.add(elemT);
          selected = r; 
        }
      } 
      else if (!t.isSetType() && !t.isRef() && 
               !t.isSequenceType() && 
               !t.isMapType() && !t.isFunctionType() &&
               trimmedlhs.equals("_1"))
      { args.add(t);
        selected = r; 
      } 

      // System.out.println(); 
      // System.out.println(">>>---->>> Selected type rule: " + selected + " for " + args); 
      // System.out.println(); 

      if (selected != null &&
          selected.satisfiesConditions(args,
                                       entities,this))
      { return selected; } 
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
	  
      String trimmedlhs = r.lhs.trim(); 

      if (etext.equals(trimmedlhs))
      { selected = r; } // exact match
      else if (op.startsWith("->") && trimmedlhs.endsWith(op + "()"))
      { selected = r; }
      else if (trimmedlhs.startsWith(op))
      { selected = r; }

      if (selected != null && 
          selected.satisfiesConditions(args,
                                       entities,this))
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
      else if (op.equals("repeat") && trimmedlhs.startsWith(op))
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
      else if (op.equals("continue") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("error") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("catch") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("finally") && trimmedlhs.startsWith(op))
      { return r; }
      else if (op.equals("assert") && trimmedlhs.startsWith(op))
      { Vector args = ((AssertStatement) e).cgparameters(); 
        if (r.variables.size() == args.size())
        { return r; }
      }
      else if (op.equals("try") && trimmedlhs.startsWith(op))
      { Vector args = ((TryStatement) e).cgparameters(); 
        if (r.variables.size() == args.size())
        { return r; }
      }
      else if (e instanceof SequenceStatement && (trimmedlhs.indexOf(";") > -1))
      { return r; }
      else if (op.equals("var") && trimmedlhs.startsWith(op) && 
               e instanceof CreationStatement)
      { CreationStatement cse = (CreationStatement) e; 
        Vector args = cse.cgparameters(); 
        if (r.variables.size() == 3 && 
            cse.initialExpression != null && 
            r.satisfiesConditions(args,entities,this))
        { return r; }
        else if (r.variables.size() == 2 && 
                 cse.initialExpression == null && 
                 r.satisfiesConditions(args,
                                       entities,this))
        { return r; }
        // else if (r.variables.size() == 1 &&
        //          cse.initialExpression == null && 
        //          r.satisfiesConditions(args,entities,this))
        // { return r; }
      } 
      else if (op.equals(":=") && 
               (trimmedlhs.indexOf(op) > -1))
      { Vector args = ((AssignStatement) e).cgparameters(); 
        if (r.variables.size() == args.size() && 
            r.satisfiesConditions(args,entities,this))
        { return r; }
      }
      else if (op.equals("execute") && 
               trimmedlhs.startsWith(op))
      { Vector args = 
          ((ImplicitInvocationStatement) e).cgparameters(); 
        if (r.satisfiesConditions(args,entities,this))
        { return r; }
      }
      else if (op.equals("call") && trimmedlhs.startsWith(op))
      { Vector args = ((InvocationStatement) e).cgparameters();
        System.out.println(">>> Call statement " + e + " matches rule " + r); 
		 
        if (r.satisfiesConditions(args,entities,this))
        { return r; }
      }
      else if (op.equals("call") && trimmedlhs.equals("skip"))
      { InvocationStatement istat = (InvocationStatement) e;
        if (istat.isSkip() || "skip".equals(istat + ""))
        { System.out.println(">>> skip call statement " + e + " matches rule " + r); 
		 
          return r; 
        }
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
    else if ("!".equals(op))
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
    else if ("let".equals(op))
    { Attribute acc = e.getAccumulator(); 
      BasicExpression varbe = 
        new BasicExpression(acc);
      Type vartyp = acc.getType();   
      args.add(varbe); 
      args.add(vartyp); 
      args.add(e.getLeft()); 
      args.add(e.getRight()); 
    } 
    else if ("->collect".equals(op) || 
             "->reject".equals(op) || "->any".equals(op) || 
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
    else if ("->iterate".equals(op))
    { args.add(e.getLeft());
      System.out.println(">>>> Iterate expression: " + e); 
	   
      String iter = "self"; 
      if (e.iteratorVariable != null) 
      { iter = e.iteratorVariable; } 
      BasicExpression iterVar = 
         new BasicExpression(iter); 
      iterVar.setType(e.getLeft().getElementType()); 
      args.add(iterVar); 
      
      BasicExpression acc = new BasicExpression("_acc");
 
      if (e.accumulator != null) 
      { acc = new BasicExpression(e.accumulator); 
        args.add(acc);
        Expression initval = 
          e.accumulator.getInitialExpression(); 
        args.add(initval);
      } 
      else 
      { args.add(acc); 
        args.add(new BasicExpression(0)); 
      } 
      args.add(e.getRight());        
    } 
    else if ("->oclAsType".equals(op) && e.getType() != null)
    { args.add(e.getLeft()); 
      args.add(e.getType()); 
    } // likewise for ->oclIsKindOf, ->oclIsTypeOf
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

      if (selected != null && 
          selected.satisfiesConditions(args,
                                       entities,this))
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
      { return r; } // exact match of literal. 
      // else if ("null".equals(r.lhs) && "null".equals(etext))
      // { return r; } 
      else if (e.data.equals("displayString") && 
               trimmedlhs.equals("displayString(_1)"))
      { return r; }
      else if (e.data.equals("displayint") && 
               trimmedlhs.equals("displayint(_1)"))
      { return r; }
      else if (e.data.equals("displaylong") && 
               trimmedlhs.equals("displaylong(_1)"))
      { return r; }
      else if (e.data.equals("displaydouble") && 
               trimmedlhs.equals("displaydouble(_1)"))
      { return r; }
      else if (e.data.equals("displayboolean") && 
               trimmedlhs.equals("displayboolean(_1)"))
      { return r; }
      else if (e.data.equals("displaySequence") && 
               trimmedlhs.equals("displaySequence(_1)"))
      { return r; }
      else if (e.data.equals("displaySet") && 
               trimmedlhs.equals("displaySet(_1)"))
      { return r; }
      else if (e.data.equals("displayMap") && 
               trimmedlhs.equals("displayMap(_1)"))
      { return r; }
      else if (e.data.equals("allInstances") && 
               trimmedlhs.equals("_1.allInstances"))
      { return r; }
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("subrange") && 
               "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.subrange(_1,_2)"))
      { return r; }  
      else if (pars != null && pars.size() == 4 && 
               e.data.equals("Sum") && 
               "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.Sum(_1,_2,_3,_4)"))
      { return r; }  
      else if (pars != null && pars.size() == 4 && 
               e.data.equals("Prd") && 
               "Integer".equals(e.getObjectRef() + "") && 
               trimmedlhs.equals("Integer.Prd(_1,_2,_3,_4)"))
      { return r; }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("subrange") && 
               trimmedlhs.equals("_1.subrange(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }  
      else if (pars != null && pars.size() == 1 && 
               e.data.equals("subrange") && 
               trimmedlhs.equals("_1.subrange(_2)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
      }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("insertAt") && 
               trimmedlhs.equals("_1.insertAt(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("setAt") && 
               trimmedlhs.equals("_1.setAt(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("replace") && 
               trimmedlhs.equals("_1.replace(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }  
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("replaceAll") && 
               trimmedlhs.equals("_1.replaceAll(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }   
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("replaceAllMatches") && 
               trimmedlhs.equals("_1.replaceAllMatches(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }   
      else if (pars != null && pars.size() == 2 && 
               e.data.equals("replaceFirstMatch") && 
               trimmedlhs.equals("_1.replaceFirstMatch(_2,_3)"))
      { selected = r; 
        args.add(obj); 
        args.add(pars.get(0)); 
        args.add(pars.get(1)); 
      }   
      else if (ind != null && pars == null && 
               trimmedlhs.equals("_1[_2]"))
      { selected = r;
        BasicExpression e1 = (BasicExpression) e.clone(); 
        e1.arrayIndex = null; 
        e1.elementType = e.type; 

        // type of e1 could be either a map or sequence or string.
        // System.out.println(); 
        // System.out.println(">>> " + e1 + " type is: " + e.arrayType); 

        // System.out.println(">>> " + e1 + " element type is: " + e1.elementType); 
        // System.out.println(); 
 
        e1.type = e.arrayType; 
        if (e1.type == null && 
            ind.type != null && 
            ind.type.isInteger())
        { e1.type = new Type("Sequence", null); } 
        else if (e1.type == null)  
        { e1.type = new Type("Map", null); } 

        args.add(e1); // But again the type can be different. 
        args.add(ind); // condition must be on _2 
      }
      else if (ind == null && obj != null && pars == null && trimmedlhs.equals("_1._2"))
      { selected = r; 
        args.add(obj); 
        args.add(cl); 
        System.out.println("^^^^ " + cl + " is static: " + cl.isStatic); 
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
      else if (trimmedlhs.endsWith("_1(_2)") && pars != null && 
            ModelElement.haveCommonPrefix(etext,trimmedlhs))
      { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs);
        int brind = etext.indexOf("("); 

        if (prefix.length() == trimmedlhs.length() - 6 && 
            prefix.length() == brind) 
        { selected = r; 
          System.out.println(">>> matched text rule: " + r + " for " + etext + " with prefix " + prefix); 
          System.out.println(); 
          args.add(e);   // should be for _1 
          args.add(pars);
          matchedtextrules.add(r);
        }  
      }
      else if (trimmedlhs.endsWith("(_1)") && pars != null && ModelElement.haveCommonPrefix(etext,trimmedlhs))
      { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs); 
        int brind = etext.indexOf("("); 


        if (prefix.length() == trimmedlhs.length() - 4 &&
            prefix.length() == brind) 
        { selected = r; 
          System.out.println(">>> matched text rule: " + r + " for " + etext + " with prefix " + prefix); 
          System.out.println(); 
          args.add(pars);
          matchedtextrules.add(r);
        }  
      }
      else if (trimmedlhs.endsWith("_1()") && (pars == null || pars.size() == 0) && 
	           ModelElement.haveCommonPrefix(etext,trimmedlhs))
      { String prefix = ModelElement.longestCommonPrefix(etext,trimmedlhs); 
        int brind = etext.indexOf("("); 
        if (prefix.length() == trimmedlhs.length() - 4 && 
            prefix.length() == brind)
        { System.out.println(">>> matched text rule: " + r + " for " + etext + " with prefix " + prefix); 
          System.out.println(); 
          selected = r; 
          args.add(e);
          matchedtextrules.add(r);
        }  
      }

      if (selected != null && 
          selected.satisfiesConditions(args,
                                       entities,this))
      { return selected; } 
    }
    return null;
  } // _1 binds to objectRef or left part, _2 to right part/index


  public CGRule matchedSetExpressionRule(SetExpression e, String etext)
  { boolean ordered = e.isOrdered();
    Vector elems = e.getElements(); 
    Vector args = new Vector(); 
    
    CGRule selected = null; 
	
    for (int x = 0; x < setExpressionRules.size(); x++)
    { CGRule r = (CGRule) setExpressionRules.get(x);
      String trimmedlhs = r.lhs.trim(); 
	  
      if (etext.equals(trimmedlhs))
      { selected = r; } // exact match
      else if (etext.startsWith("Set{") && etext.endsWith("}") && 
               trimmedlhs.startsWith("Set{") && trimmedlhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0)
        { selected = r; } // r is empty set -- just white space in lhs between {}
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { args.add(elems); 
          selected = r; 
        } 
      } 
      else if (etext.startsWith("SortedSet{") && etext.endsWith("}") && 
               trimmedlhs.startsWith("SortedSet{") && trimmedlhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0)
        { selected = r; } // r is empty set -- just white space in lhs between {}
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { args.add(elems); 
          selected = r; 
        } 
      } 
      else if (etext.startsWith("Sequence{") && etext.endsWith("}") && 
               trimmedlhs.startsWith("Sequence{") && trimmedlhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0)
        { selected = r; } // r is empty sequence
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { args.add(elems); 
          selected = r; 
        } 
      } 
      else if (etext.startsWith("Map{") && etext.endsWith("}") && 
               trimmedlhs.startsWith("Map{") && trimmedlhs.endsWith("}"))
      { if (elems.size() == 0 && r.variables.size() == 0) 
        { selected = r; }  // empty map
        else if (elems.size() > 0 && r.variables.size() > 0) 
        { args.add(elems); 
          selected = r; 
        } 
      } 
      else if (etext.equals("Ref{}") && 
               trimmedlhs.equals("Ref{}"))
      { selected = r; } 
      else if (etext.startsWith("Ref(") && etext.endsWith("}") && 
               trimmedlhs.startsWith("Ref(") && trimmedlhs.endsWith("}"))
      { args.add(e.getElementType()); 
        if (elems.size() == 0 && r.variables.size() == 1) 
        { selected = r; }  // empty ref
        else if (elems.size() > 0 && r.variables.size() > 1) 
        { args.add(elems); 
          selected = r; 
        } 
      } 
    }

    if (selected != null && 
        selected.satisfiesConditions(args,
                                     entities,this))
    { return selected; } 

    return null;
  } // _1 binds to elements if any. Could also be _*

  public CGRule matchedEntityRule(Entity e, String ctext)
  { Vector eargs = new Vector(); 
    eargs.add(e); 

    // Vector args = new Vector(); 
    // args.add(e.cg(this)); 

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
	  
      if (selected != null && 
          selected.satisfiesConditions(eargs,
                                       entities,this))
      { return selected; } 

    }
    return null;
  } // _1 binds to class name

  public CGRule matchedAttributeRule(Attribute e, 
                                     String ctext)
  { CGRule selected = null;

    Vector eargs = new Vector(); 
    eargs.add(e); 
    eargs.add(e.getType());  
    if (e.getInitialExpression() != null) 
    { eargs.add(e.getInitialExpression()); } 
  
    /* Vector args = new Vector(); 
    args.add(e.cg(this)); 
    if (e.getType() != null)
    { args.add(e.getType().cg(this)); } 
    else 
    { args.add("void"); }   
    if (e.getInitialExpression() != null) 
    { args.add(e.getInitialExpression().cg(this)); } */ 

    for (int x = 0; x < attributeRules.size(); x++)
    { CGRule r = (CGRule) attributeRules.get(x);

      if (r.lhs.indexOf("attribute") > -1) { } 
      else 
      { continue; } 

      if (ctext.equals(r.lhs) && r.variableCount() == 0)
      { selected = r; } // exact match
      else if (e.isStatic() && 
               eargs.size() == r.variableCount())
      { if (r.lhs.indexOf("static") > -1)     
        { selected = r; }
      }
      // else if (r.hasCondition("primary") && e.isPrimaryAttribute())
      // { selected = r; } 
      else if (e.isUnique())
      { if (r.lhs.indexOf("identity") > -1)     
        { selected = r; }
      }
      else if (r.lhs.indexOf("identity") < 0 && 
               r.lhs.indexOf("static") < 0 &&
               !e.isStatic() && !e.isUnique() &&
               eargs.size() == r.variableCount() )   
      { selected = r; }
    
      if (selected != null && 
          selected.satisfiesConditions(eargs,
                                       entities,this))
      { return selected; } 
    }

    return null;
  } 

  public CGRule matchedReferenceRule(Attribute e, String ctext)
  { for (int x = 0; x < attributeRules.size(); x++)
    { CGRule r = (CGRule) attributeRules.get(x);
	
      System.out.println(">> Trying to match reference " + e + " with type " + e.getType()); 
      System.out.println(">> Trying Rule: " + r); 
      System.out.println(); 
      System.out.println(">> Entities are: " + entities); 
      System.out.println(); 

	  
      if (r.lhs.startsWith("reference")) 
      { Type t = e.getType(); 
        if (t == null) { continue; }
		
        String tname = t.getName(); 
		
        if (t.isEntityType(entities)) 
        { if (r.hasCondition("class"))
          { return r; }
          else if (r.hasNegativeCondition("class"))
          { } 
          else if (r.hasNoCondition()) 
          { return r; }
        } 
        else 
        { if (r.hasNegativeCondition("class"))
          { return r; }
          else if (r.hasCondition("class"))
          { } 
          else if (r.hasNoCondition()) 
          { return r; }
        } 
		
        if (t.isMapType()) 
        { if (r.hasCondition("map"))
          { return r; }
          else if (r.hasNegativeCondition("map"))
          { } 
          else if (r.hasNoCondition())
          { return r; }
        } 
        	
        if (t.isFunctionType()) 
        { if (r.hasCondition("function"))
          { return r; }
          else if (r.hasNegativeCondition("function"))
          { } 
          else if (r.hasNoCondition())
          { return r; }
        } 
        else 
        { if (r.hasNegativeCondition("function"))
          { return r; } 
          else if (r.hasCondition("function"))
          { } 
          else if (r.hasNoCondition())
          { return r; }
        }

        if (t.isSequenceType()) 
        { if (r.hasCondition("Sequence"))
          { return r; }
          else if (r.hasNegativeCondition("Sequence"))
          { } 
          else if (r.hasNoCondition())
          { return r; } 
        } 
        else 
        { if (r.hasNegativeCondition("Sequence"))
          { return r; } 
          else if (r.hasCondition("Sequence"))
          { } 
          else if (r.hasNoCondition())
          { return r; }
        }

        if (t.isSetType()) 
        { if (r.hasCondition("Set"))
          { return r; }
          else if (r.hasNegativeCondition("Set"))
          { } 
          else if (r.hasNoCondition())
          { return r; } 
        } 
        else 
        { if (r.hasNegativeCondition("Set"))
          { return r; } 
          else if (r.hasCondition("Set"))
          { } 
          else if (r.hasNoCondition())
          { return r; }
        }
		
        if (t.isCollectionType()) 
        { if (r.hasCondition("collection"))
          { return r; }
          else if (r.hasNegativeCondition("collection"))
          { } 
          else if (r.hasNoCondition())
          { return r; } 
        } 
        else 
        { if (r.hasNegativeCondition("collection"))
          { return r; } 
          else if (r.hasCondition("collection"))
          { } 
          else if (r.hasNoCondition())
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

  public CGRule matchedOperationRule(BehaviouralFeature e, 
                                     String ctext)
  { Vector args = new Vector(); 
    args.add(e); 
    args.add(e.getParameters()); 
    args.add(e.getType()); 
    args.add(e.getPre()); 
    args.add(e.getPost()); 
    args.add(e.getActivity());

    Vector typepars = e.getTypeParameters();  
    Type typepar = null; 
    if (typepars != null && typepars.size() > 0)
    { typepar = (Type) typepars.get(0); } 
    
    CGRule selected = null; 
    for (int x = 0; x < operationRules.size(); x++)
    { CGRule r = (CGRule) operationRules.get(x);
      String trimmedlhs = r.lhs.trim(); 

      if (ctext.equals(trimmedlhs))
      { selected = r; } // exact match
      else if (e.isQuery())
      { if (trimmedlhs.indexOf("query") > -1) 
        { if (e.isStatic())
          { if (trimmedlhs.indexOf("static") > -1)     
            { if (trimmedlhs.indexOf("<") < 0 && 
                  typepar == null)
              { selected = r; }
              else if (trimmedlhs.indexOf("<") > -1 && 
                       typepar != null) 
              { selected = r; }
            }
          } 
          else if (trimmedlhs.indexOf("static") < 0)
          { if (trimmedlhs.indexOf("<") < 0 && 
                typepar == null)
            { selected = r; }
            else if (trimmedlhs.indexOf("<") > -1 && 
                     typepar != null) 
            { selected = r; } 
          } 
        } 
      }
      else if (trimmedlhs.indexOf("operation") > -1) 
      { if (e.isStatic())
        { if (trimmedlhs.indexOf("static") > -1)     
          { if (trimmedlhs.indexOf("<") < 0 && 
              typepar == null)
            { selected = r; }
            else if (trimmedlhs.indexOf("<") > -1 && 
                     typepar != null) 
            { selected = r; } 
          }
        }
        else if (trimmedlhs.indexOf("static") < 0)   
        { if (trimmedlhs.indexOf("<") < 0 && 
              typepar == null)
          { selected = r; }
          else if (trimmedlhs.indexOf("<") > -1 && 
                   typepar != null) 
          { selected = r; } 
        } 
      } 
    
      if (selected != null && 
          selected.satisfiesConditions(args,
                                       entities,this))
      { return selected; } 
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

