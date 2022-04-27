/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 
import java.io.*; 

public class ASTSymbolTerm extends ASTTerm
{ String symbol = ""; 

  public ASTSymbolTerm(String s) 
  { symbol = s; } 

  public String getTag()
  { return symbol; } 

  public String tagFunction()
  { return "~"; } 

  public boolean hasTag(String tagx) 
  { return false; } 

  public boolean hasSingleTerm() 
  { return false; } 

  public boolean isNestedSymbolTerm() 
  { return true; } 

  public String cg(CGSpec cgs)
  { return symbol; } 

  public String cgRules(CGSpec cgs, Vector rules)
  { if (rules == null) 
    { return symbol; } 

    for (int i = 0; i < rules.size(); i++) 
    { CGRule r = (CGRule) rules.get(i);
      Vector tokens = r.lhsTokens; 
      Vector vars = r.getVariables(); 

      if (vars.size() > 0 || tokens.size() > 1)
      { // System.out.println("> Rule " + r + " has too many variables/tokens to match basic term " + this); 
        continue; 
      } 

      if (tokens.size() == 0) 
      { return symbol; } 

      String tok = (String) tokens.get(0); 

      if (symbol.equals(tok))
      { return r.rhs; } 
    }
    return symbol; 
  } 

  public String toString()
  { return symbol; } 

  public String getSymbol()
  { return symbol; } 

  public String toJSON()
  { String res = "{ \"root\" : \"" + symbol + "\", \"children\" : [] }"; 
    return res; 
  } 

  public boolean equals(Object obj)
  { if (obj instanceof ASTSymbolTerm) 
    { return symbol.equals(((ASTSymbolTerm) obj).symbol); } 
    return false; 
  } 

  public String literalForm()
  { return symbol; } 

  public int arity()
  { return 0; } 

  public int nonSymbolArity()
  { return 0; } 

  public Vector symbolTerms()
  { return new Vector(); }  

  public Vector nonSymbolTerms()
  { return new Vector(); } 

  public Vector getTerms()
  { return new Vector(); } 

  public ASTTerm removeOuterTag()
  { return null; }  

  public ASTTerm getTerm(int i) 
  { return null; }

  public Vector tokenSequence()
  { Vector res = new Vector(); 
    res.add("\"" + symbol + "\""); 
    return res; 
  } 

  public int termSize() 
  { return 1; } 

  public String asTextModel(PrintWriter out)
  { return "\"" + symbol + "\""; } 


  public String getLabel()
  { return null; } 

  public boolean isLabeledStatement()
  { return false; } 

  public Statement cpreSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Statement cpostSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Vector cexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)

  { Vector res = new Vector(); 
    return res; 
  } 

  public Type pointersToRefType(String tname, Type t)
  { return t; } 

  public Type cdeclarationToType(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public ModelElement cdeclaratorToModelElement(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Vector cdeclaratorToModelElements(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Vector cparameterListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 

  public Attribute cparameterToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Vector cstatementListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { Vector res = new Vector();
    return res;  
  }

  public Statement cstatementToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Statement cupdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Statement cbasicUpdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Expression cexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new BasicExpression(symbol); } 


  /* JavaScript abstraction: */ 

  public Expression jsexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new BasicExpression(symbol); } 

  public Vector jsexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { 
    Vector res = new Vector(); 
    return res; 
  } 


  public Vector jsupdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jsvariableDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 

  public Vector jsstatementListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { Vector res = new Vector();
    return res;  
  }

  public Vector jsstatementToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 

 
  /* Java abstraction: */ 

  public String queryForm()
  { return toKM3(); } 

  public String toKM3()
  { if ("<EOF>".equals(symbol))
    { return ""; }
    if ("{".equals(symbol)) 
    { return ""; } 
    if ("}".equals(symbol)) 
    { return ""; } 
    if (";".equals(symbol)) 
    { return ""; } 
    
    if ("=".equals(symbol)) 
    { return " := "; } 
    if ("==".equals(symbol)) 
    { return " = "; } 
    if ("!=".equals(symbol)) 
    { return " /= "; } 

    if ("&&".equals(symbol) || "&".equals(symbol)) 
    { return " & "; }
    if ("||".equals(symbol) || "|".equals(symbol)) 
    { return " or "; }
 
    if ("<".equals(symbol)) 
    { return " < "; } 
    if (">".equals(symbol)) 
    { return " > "; } 
    if ("<=".equals(symbol)) 
    { return " <= "; } 
    if (">=".equals(symbol)) 
    { return " >= "; } 

    if ("'".equals(symbol)) 
    { return "\""; } 
    if ("^".equals(symbol)) 
    { return " xor "; } 
    if ("!".equals(symbol) || "~".equals(symbol)) 
    { return " not "; } 
    if ("%".equals(symbol)) 
    { return " mod "; } 

    if ("+".equals(symbol)) 
    { return "+"; } 
    if ("-".equals(symbol)) 
    { return "-"; } 
    if ("*".equals(symbol)) 
    { return "*"; } 
    if ("/".equals(symbol)) 
    { return "/"; } 

    if ("this".equals(symbol))
    { expression = new BasicExpression("self"); 
      return "self";
    } 

    if ("break".equals(symbol))
    { statement = new BreakStatement();
      return "  break "; 
    } 

    if ("continue".equals(symbol))
    { statement = new ContinueStatement(); 
      return "  continue "; 
    } 

    if ("List".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; 
    }
 
    if ("ArrayList".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; }
 
    if ("Vector".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; } 

    if ("LinkedList".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; } 

    if ("PriorityQueue".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; }

    if ("Stream".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      return "Sequence"; }
 
    if ("Set".equals(symbol)) 
    { modelElement = new Type("Set", null); 
      return "Set"; } 
    if ("HashSet".equals(symbol)) 
    { modelElement = new Type("Set", null); 
      return "Set"; } 
    if ("TreeSet".equals(symbol)) 
    { modelElement = new Type("Set", null); 
      return "Set"; } 

    if ("HashMap".equals(symbol) || "Map".equals(symbol)) 
    { modelElement = new Type("Map", null); 
      return "Map"; } 
    if ("TreeMap".equals(symbol)) 
    { modelElement = new Type("Map", null); 
      return "Map"; } 

    if ("Thread".equals(symbol) || "Process".equals(symbol)) 
    { modelElement = new Type("OclProcess", null); 
      return "OclProcess"; } 

    if ("Predicate".equals(symbol)) 
    { modelElement = new Type("Function", null);
      ((Type) modelElement).elementType = 
                               new Type("boolean", null);   
      return "Function"; 
    }

    if ("Function".equals(symbol)) 
    { modelElement = new Type("Function", null);
      ((Type) modelElement).elementType = 
                               new Type("OclAny", null);   
      return "Function"; 
    }

    if ("else".equals(symbol))
    { return " else "; } 

    return symbol; 
  } 

  public boolean isIdentifier()
  { return false; }

  public boolean isCharacter()
  { if (symbol.length() > 2 && 
        symbol.charAt(0) == '\'' && 
        symbol.charAt(symbol.length()-1) == '\'')
    { return true; } 
    return false; 
  } 

  public boolean isInteger()
  { if (Expression.isInteger(symbol) || 
        Expression.isLong(symbol))
    { return true; } 
    return false; 
  } 

  public boolean isDouble()
  { if (Expression.isDouble(symbol))
    { return true; } 
    return false; 
  } 

  public boolean isBoolean()
  { if (symbol.equals("true") || symbol.equals("false"))
    { return true; } 
    return false; 
  } // Ok for Java and OCL.

  public boolean isString() 
  { return Expression.isString(symbol); } 
 

  public boolean updatesObject(ASTTerm t)
  { return false; } 

  public boolean callSideEffect()
  { return false; }

  public boolean hasSideEffect()
  { return false; }

  public String preSideEffect()
  { return null; } 

  public String postSideEffect()
  { return null; } 

} 