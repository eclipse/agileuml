/******************************
* Copyright (c) 2003--2025 Kevin Lano
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

  public ASTTerm removeWhitespaceTerms()
  { String strim = symbol.trim(); 

    // System.out.println(">>> Trimming: " + strim); 

    if (strim.equals("\\n\\r") || 
        strim.equals("\\r\\n") || 
        strim.equals("\\r\\n\\r\\n") ||
        strim.equals("\\r\\n\\r\\n\\r\\n"))
    { // System.out.println(">>> Trimmed: null");
      return null; 
    } 

    if (strim.endsWith("\\r\\n\\r\\n\\r\\n"))
    { String str = strim.substring(0, strim.length()-12); 
      ASTTerm ntrm = new ASTSymbolTerm(str);
      // System.out.println(">>> Trimmed: " + str);
      return ntrm; 
    } 

    if (strim.endsWith("\\r\\n\\r\\n"))
    { String str = strim.substring(0, strim.length()-8); 
      // System.out.println(">>> Trimmed: " + str);
      ASTTerm ntrm = new ASTSymbolTerm(str);
      return ntrm; 
    } 

    if (strim.endsWith("\\n\\r") ||
        strim.endsWith("\\r\\n"))
    { String str = strim.substring(0, strim.length()-4); 
      // System.out.println(">>> Trimmed: " + str);
      ASTTerm ntrm = new ASTSymbolTerm(str);
      return ntrm; 
    } 

    // System.out.println(">>> Trimmed: " + strim); 

    return this; 
  }  

  public ASTTerm removeExtraNewlines()
  { return this; } 

  public ASTTerm replaceAmbiguousCobolNames(Vector rnames)
  { return this; }

  public ASTTerm replaceCobolIdentifiers()
  { if ("FILLER".equals(symbol))
    { ASTTerm.cobolFillerCount++; 
      return new ASTSymbolTerm("FILLER_F" + ASTTerm.cobolFillerCount); 
    } 
    return this; 
  } 

  public ASTTerm substituteEq(String str, ASTTerm newtrm)
  { if (str.equals(symbol))
    { return newtrm; }
    return this; 
  }  

  public boolean hasTag(String tagx) 
  { return false; } 

  public boolean hasSingleTerm() 
  { return false; } 

  public boolean isNestedSymbolTerm() 
  { return true; } 

  public Vector allNestedSubterms()
  { return new Vector(); }  

  public Vector allIdentifiers(Vector tags)
  { Vector res = new Vector(); 
    return res; 
  } 

  public String cg(CGSpec cgs)
  { return symbol; } 

  public String cgRules(CGSpec cgs, Vector rules)
  { System.out.println(">>> cgRules for " + this + " " + rules); 

    if (rules == null) 
    { return symbol; } 

    for (int i = 0; i < rules.size(); i++) 
    { CGRule r = (CGRule) rules.get(i);
      Vector tokens = r.lhsTokens; 
      Vector vars = r.getVariables(); 

      System.out.println("> Rule " + r + " has tokens " + tokens + " variables " + vars);

      if (vars.size() > 1 || tokens.size() > 1)
      { // System.out.println("> Rule " + r + " has too many variables/tokens to match basic term " + this); 
        continue; 
      } 

      if (tokens.size() == 0 && vars.size() == 0) 
      { return symbol; } 

      if (tokens.size() == 1 && vars.size() == 0)
      { String tok = (String) tokens.get(0); 

        if (symbol.equals(tok))
        { return r.rhs; }
      }  
      else if (vars.size() == 1) // _i |-->r.rhs
      { String var = "" + vars.get(0); 
        return r.rhs.replace(var,symbol); 
      } 
    }  // No testing of conditions 

    return symbol; 
  } 

  public java.util.Set allMathMetavariables() 
  { java.util.Set res = new java.util.HashSet(); 
    
    if (CSTL.isMathMetavariable(symbol))
    { res.add(symbol); } 
   
    return res; 
  } 
    
  public java.util.HashMap hasMatch(ASTTerm rterm, 
                                    java.util.HashMap res) 
  { return fullMatch(rterm,res); } 

  public java.util.HashMap fullMatch(ASTTerm rterm, 
                                     java.util.HashMap res) 
  { // This term matches to a schematic term rterm

    String rlit = rterm.literalForm(); 
    if (symbol.equals(rlit))
    { return res; } 

    // if (CSTL.isCSTLVariable(rlit))
    if (CSTL.isMathMetavariable(rlit))
    { ASTTerm oldterm = (ASTTerm) res.get(rlit); 
      if (oldterm == null)
      { res.put(rlit, this); 
        return res; 
      } 
      else if (symbol.equals(oldterm.literalForm()))
      { } 
      else 
      { return null; } 
    }

    return null; 
  }  

  public ASTTerm instantiate(java.util.HashMap res) 
  { // replace _i by res.get(_i)

    // if (CSTL.isCSTLVariable(symbol))
    if (CSTL.isMathMetavariable(symbol))
    { ASTTerm oldterm = (ASTTerm) res.get(symbol); 
      if (oldterm != null)
      { return oldterm; } 
    }

    return new ASTSymbolTerm(symbol); 
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


  public String literalFormSpaces()
  { return symbol; } 

  public String evaluationLiteralForm()
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

  public Vector allNestedTagsArities()
  { return new Vector(); } 

  public Vector allTagsArities()
  { return new Vector(); } 

  public java.util.Set allTagsIn()
  { return new java.util.HashSet(); } 

  public ASTTerm removeOuterTag()
  { return null; }  

  public ASTTerm getTerm(int i) 
  { return null; }

  public Type deduceType()
  { return new Type("void", null); } 

  public Type deduceElementType()
  { return new Type("void", null); } 

  public Vector tokenSequence()
  { Vector res = new Vector(); 
    res.add("\"" + symbol + "\""); 
    return res; 
  } 

  public int termSize() 
  { return 1; } 

  public int size() 
  { return 0; } 

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

  public String cdeclarationStorageClass()
  { return null; } 

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
  { if ("TRUE".equals(symbol))
    { return new BasicExpression(true); } 
    if ("FALSE".equals(symbol))
    { return new BasicExpression(false); } 

    return new BasicExpression(symbol);
  } 


  /* JavaScript abstraction: */ 

  public Vector jsclassDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 

  public Expression jsexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new BasicExpression(symbol); } 

  public Vector jsexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { 
    Vector res = new Vector(); 
    return res; 
  } 

  public Vector jscompleteUpdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jsupdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jspreSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jspostSideEffect(java.util.Map vartypes, 
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

  public String getJavaLabel()
  { return null; } 

  public boolean isJavaLabeledStatement()
  { return false; } 

  public ASTTerm getJavaLabeledStatement()
  { return null; } 

  public String getJSLabel()
  { return null; } 

  public boolean isJSLabeledStatement()
  { return false; } 

  public ASTTerm getJSLabeledStatement()
  { return null; } 

  public boolean isLocalDeclarationStatement()
  { return false; } 

  public Vector getParameterExpressions()
  { return new Vector(); } 

  public String lambdaParametersToKM3()
  { if (",".equals(symbol) || "(".equals(symbol) ||
        ")".equals(symbol))
    { return symbol; } 
    modelElement = 
      new Attribute(symbol,new Type("OclAny", null),
                    ModelElement.INTERNAL);
    modelElements = new Vector(); 
    modelElements.add(modelElement); 
    return symbol; 
  } 

  public String toKM3type()
  { String res = toKM3();

    System.out.println("+++ toKM3type on symbol " + symbol + " " + res + " " + modelElement); 
 
    if (modelElement == null) 
    { Entity ee = (Entity) ModelElement.lookupByName(symbol,
                                   ASTTerm.entities); 
      if (ee != null) 
      { modelElement = new Type(ee); 
        expression = new BasicExpression((Type) modelElement);
        return res; 
      } 

      Type tt = (Type) ModelElement.lookupByName(symbol, 
                                       ASTTerm.enumtypes); 
      if (tt != null) 
      { modelElement = tt; 
        expression = new BasicExpression((Type) modelElement);
        return res; 
      } 

      Entity newent = new Entity(symbol); // if valid name 
      modelElement = new Type(newent); 
      expression = new BasicExpression((Type) modelElement);
      return res;  
    } 

    return res; 
  } 

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
    { return "="; } 
    if ("!=".equals(symbol)) 
    { return "/="; } 

    if ("&&".equals(symbol) || "&".equals(symbol)) 
    { return "&"; }
    if ("||".equals(symbol) || "|".equals(symbol)) 
    { return "or"; }
 
    if ("<".equals(symbol)) 
    { return "<"; } 
    if (">".equals(symbol)) 
    { return ">"; } 
    if ("<=".equals(symbol)) 
    { return "<="; } 
    if (">=".equals(symbol)) 
    { return ">="; } 

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

    if ("Integer".equals(symbol) || 
        "AtomicInteger".equals(symbol))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }

    if ("Long".equals(symbol) || 
        "AtomicLong".equals(symbol))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; 
    }
 
    if ("Boolean".equals(symbol) || 
        "AtomicBoolean".equals(symbol))
    { modelElement = new Type("boolean", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "boolean"; 
    }

    if ("Date".equals(symbol))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; 
    }

    if ("Calendar".equals(symbol) || 
        "Timestamp".equals(symbol) || 
        "GregorianCalendar".equals(symbol))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; 
    }

    if ("Random".equals(symbol))
    { modelElement = new Type("OclRandom", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRandom"; 
    }

    if ("Pattern".equals(symbol) || 
        "FileFilter".equals(symbol) ||
        "FilenameFilter".equals(symbol) || 
        "Matcher".equals(symbol))
    { modelElement = new Type("OclRegex", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRegex"; 
    } 

    if ("Collection".equals(symbol) || 
        "Iterable".equals(symbol) || 
        "AbstractCollection".equals(symbol))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

    if ("AtomicLongArray".equals(symbol) || 
        "LongStream".equals(symbol))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("long", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(long)"; 
    } 

    if ("AtomicIntegerArray".equals(symbol) || 
        "IntStream".equals(symbol))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("int", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(int)"; 
    } 

    if ("DoubleStream".equals(symbol))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("double", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(double)"; 
    } 

    if ("List".equals(symbol) || "ArrayList".equals(symbol))
        // "Array".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence";
    }
 
    if ("Vector".equals(symbol) || "Stack".equals(symbol) ||
        "LinkedList".equals(symbol) || 
        "AbstractList".equals(symbol) ||
        "AbstractSequentialList".equals(symbol) ||
        "Queue".equals(symbol) ||
        "Deque".equals(symbol) ||
        "ArrayDeque".equals(symbol) ||
        "BlockingDeque".equals(symbol) ||
        "LinkedBlockingDeque".equals(symbol) ||
        "BlockingQueue".equals(symbol) ||
        "ArrayBlockingQueue".equals(symbol) ||
        "ListOrderedSet".equals(symbol) ||
        "SetUniqueList".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

    if ("PriorityQueue".equals(symbol) ||
        "PriorityBlockingQueue".equals(symbol) || 
        "TreeBag".equals(symbol) || 
        "SortedBag".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      ((Type) modelElement).setSorted(true); 
      expression = new BasicExpression((Type) modelElement); 
      return "SortedSequence"; 
    }

    if ("Stream".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

    if ("Bag".equals(symbol) || "HashBag".equals(symbol) ||
        "TreeList".equals(symbol))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence";
    }

    if ("JsonArray".equals(symbol) || 
        "JSONArray".equals(symbol)) 
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

    if ("Multiset".equals(symbol) || 
        "MultiSet".equals(symbol))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    } 

 
    if ("Set".equals(symbol) || "HashSet".equals(symbol) ||
        "EnumSet".equals(symbol)) 
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; 
    }
 
    if ("TreeSet".equals(symbol) || 
        "SortedSet".equals(symbol)) 
    { modelElement = new Type("Set", null); 
      ((Type) modelElement).setSorted(true); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; 
    } 

    if ("BitSet".equals(symbol))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("boolean", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(boolean)"; 
    } 

    if ("ByteBuffer".equals(symbol) || 
        "IntBuffer".equals(symbol) ||
        "ShortBuffer".equals(symbol) || 
        "CharBuffer".equals(symbol) || 
        "LongBuffer".equals(symbol) 
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("int", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(int)";
    } 

    if ("FloatBuffer".equals(symbol) || 
        "DoubleBuffer".equals(symbol)
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("double", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(double)";
    } 

    if ("HashMap".equals(symbol) || 
        "LinkedHashMap".equals(symbol) || 
        "EnumMap".equals(symbol) || 
        "Hashtable".equals(symbol) || 
        "Map".equals(symbol)) 
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("TreeMap".equals(symbol) || 
        "SortedMap".equals(symbol)) 
    { modelElement = new Type("Map", null); 
      ((Type) modelElement).setSorted(true); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("Thread".equals(symbol) || "Process".equals(symbol)) 
    { modelElement = new Type("OclProcess", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclProcess"; 
    } 

    if ("Predicate".equals(symbol)) 
    { modelElement = new Type("Function", null);
      ((Type) modelElement).keyType = 
                               new Type("String", null);   
      ((Type) modelElement).elementType = 
                               new Type("boolean", null);  
      expression = new BasicExpression((Type) modelElement); 
 
      return "Function"; 
    }

    if ("Function".equals(symbol)) 
    { modelElement = new Type("Function", null);
      ((Type) modelElement).keyType = 
                               new Type("String", null);   
      ((Type) modelElement).elementType = 
                               new Type("OclAny", null);   
      expression = new BasicExpression((Type) modelElement); 
      return "Function"; 
    }

    if ("Properties".equals(symbol))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("JSONObject".equals(symbol) || 
        "JsonObject".equals(symbol))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }

    if ("Comparator".equals(symbol))
    { modelElement = new Type("OclComparator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclComparator"; 
    }

    if ("ImmutableMap".equals(symbol) ||
        "Triple".equals(symbol) || 
        "Entry".equals(symbol) || "Pair".equals(symbol))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }


    if ("Enumeration".equals(symbol) ||
        "Iterator".equals(symbol))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    }
 
    if ("ListIterator".equals(symbol))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 
    
    if ("StringTokenizer".equals(symbol) || 
        "Spliterator".equals(symbol))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 

    if ("ResultSet".equals(symbol) || 
        "ResultSetMetaData".equals(symbol) ||
        "CachedRowSet".equals(symbol) ||
        "FilteredRowSet".equals(symbol) ||
        "JdbcRowSet".equals(symbol) ||
        "JoinRowSet".equals(symbol) ||
        "RowSet".equals(symbol) ||
        "WebRowSet".equals(symbol) ||
        "Cursor".equals(symbol))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 

    if ("File".equals(symbol) || 
        "FileDescriptor".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Formatter".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Scanner".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectOutputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectInput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectOutput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataInput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataOutput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataOutputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("PipedInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("PipedOutputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; }
    if ("FilterInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FilterOutputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("BufferedInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 
    
    if ("BufferedOutputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 

    if ("PrintStream".equals(symbol) || 
        "OutputStream".equals(symbol) ||
        "InputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("FileOutputStream".equals(symbol) ||
        "FileInputStream".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("Reader".equals(symbol) || 
        "FileReader".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("Writer".equals(symbol) ||
        "FileWriter".equals(symbol) || 
        "StringWriter".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("RandomAccessFile".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("BufferedReader".equals(symbol) ||        
        "LittleEndianInput".equals(symbol) ||
        "BigEndianInput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("BufferedWriter".equals(symbol) ||               
        "LittleEndianOutput".equals(symbol) ||
        "BigEndianOutput".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("InputStreamReader".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("OutputStreamWriter".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; }
    if ("PrintWriter".equals(symbol))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 

    if ("FileStore".equals(symbol) || 
        "ByteChannel".equals(symbol) ||
        "Channel".equals(symbol) ||
        "ReadableByteChannel".equals(symbol) ||
        "WritableByteChannel".equals(symbol) ||
        "SeekableByteChannel".equals(symbol) ||
        "FileChannel".equals(symbol))
    { return "OclFile"; } 

    if ("JDBCDatabase".equals(symbol) ||  
        "Connection".equals(symbol) ||  
        "HttpURLConnection".equals(symbol) ||  
        "URLConnection".equals(symbol) ||  
        "SQLiteDatabase".equals(symbol) ||
        "URL".equals(symbol) ||
        "DriverManager".equals(symbol) ||   
        "Socket".equals(symbol) || 
        "ServerSocket".equals(symbol) || 
        "BluetoothSocket".equals(symbol))
    { modelElement = new Type("OclDatasource", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDatasource"; 
    } 

    if ("PreparedStatement".equals(symbol) || 
        "CachedStatement".equals(symbol) ||
        "Statement".equals(symbol) || 
        "CallableStatement".equals(symbol))
    { modelElement = new Type("SQLStatement", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SQLStatement"; 
    } 

    if ("else".equals(symbol))
    { return " else "; } 

    return symbol; 
  } 

  public String toKM3asObject(Entity ent)
  { return ""; } 

  public String typeArgumentsToKM3ElementType()
  { return symbol; } 

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

  public ASTTerm updatedObject()
  { return this; } 

  public boolean callSideEffect()
  { return false; }

  public boolean hasPreSideEffect()
  { return false; } 

  public boolean hasPostSideEffect()
  { return false; } 

  public boolean hasSideEffect()
  { return false; }

  public String preSideEffect()
  { return null; } 

  public String postSideEffect()
  { return null; } 

  public String antlr2cstl()
  { return symbol; } 

  public Vector normaliseAntlr()
  { return new Vector(); } 

  public String antlrElement2cstl(Vector rulerefs, Vector conds)
  { return symbol; } 

  public int cobolDataWidth()
  { 
    if ("X".equals(symbol) || "9".equals(symbol) || 
        ",".equals(symbol) || "A".equals(symbol) ||
        "B".equals(symbol) || "0".equals(symbol) ||
        "Z".equals(symbol) ||
        "/".equals(symbol) || "+".equals(symbol) ||
        "-".equals(symbol) || "$".equals(symbol) ||
        "£".equals(symbol) || ".".equals(symbol) ||
        "Z".equals(symbol) || "*".equals(symbol))
    { return 1; } 

    // S, P, V do not add to length. 
 
    return 0; 
  } 


  public int cobolIntegerWidth()
  { 
    if ("9".equals(symbol) || "0".equals(symbol) || 
        "Z".equals(symbol) ||
        "P".equals(symbol))
    { return 1; } 
 
    return 0; 
  } 

  public int cobolFractionWidth()
  { 
    if ("9".equals(symbol) || "0".equals(symbol) || 
        "P".equals(symbol))
    { return 1; } 
 
    return 0; 
  } 


  public Type cobolDataType()
  { if ("9".equals(symbol) || "Z".equals(symbol) ||
        "S".equals(symbol))
    { return ASTCompositeTerm.intType; }

    if ("X".equals(symbol) || 
        ",".equals(symbol) || "A".equals(symbol) ||
        "B".equals(symbol) || "0".equals(symbol) ||
        "/".equals(symbol) || "+".equals(symbol) ||
        "-".equals(symbol) || "$".equals(symbol) ||
        "£".equals(symbol) || ".".equals(symbol) ||
        "*".equals(symbol))
    { return ASTCompositeTerm.stringType; } 

    if ("P".equals(symbol) ||
        "V".equals(symbol))
    { return ASTCompositeTerm.doubleType; } 
 
    return null; 
  } 

  public boolean cobolIsSigned()
  { if ("S".equals(symbol))
    { return true; }
 
    return false; 
  } 

  public Vector cobolDataDefinitions(java.util.Map context, Vector invs)
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector cobolPerformThruDefinitions(java.util.Map context, Vector invs)
  { Vector res = new Vector(); 
    return res; 
  } 

  public void checkMathOCL()
  { } 

  public Vector mathOCLVariables()
  { return new Vector(); }

  public ASTTerm mathOCLSubstitute(String var, ASTTerm repl)
  { return new ASTSymbolTerm(symbol); }  

  public static void main(String[] args) 
  { String ss = "\r\n\r\n\r\n"; 
    System.out.println(ss.matches("(\\r\\n)+"));

    ASTTerm t1 = new ASTBasicTerm("tag1", "100"); 
    ASTTerm t2 = new ASTBasicTerm("tag2", "_1");
    java.util.HashMap mm = new java.util.HashMap(); 
    System.out.println(t1.fullMatch(t2,mm));  
 
  } 
} 