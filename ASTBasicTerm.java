/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 
import java.io.*; 


public class ASTBasicTerm extends ASTTerm
{ String tag = ""; 
  String value = ""; 

  public ASTBasicTerm(String t, String v) 
  { tag = t; 
    value = v; 
  } 

  public void setTag(String t)
  { tag = t; } 

  public String getTag()
  { return tag; } 

  public boolean hasTag(String tagx) 
  { return tagx.equals(tag); } 

  public boolean hasSingleTerm() 
  { return true; } 

  public int arity()
  { return 1; } 

  public int nonSymbolArity()
  { return 0; } 

  public Vector symbolTerms()
  { return new Vector(); }  

  public Vector nonSymbolTerms()
  { return new Vector(); } 


  public ASTTerm removeOuterTag()
  { return new ASTSymbolTerm(value); }  

  public ASTTerm getTerm(int i) 
  { if (i == 0)
    { return new ASTSymbolTerm(value); } 
    return null; 
  }

  public void setValue(String v)
  { value = v; } 

  public String getValue()
  { return value; } 

  public String toString()
  { String res = "(" + tag + " " + value + ")"; 
    return res; 
  } 

  public boolean equals(Object obj)
  { if (obj instanceof ASTBasicTerm) 
    { ASTBasicTerm other = (ASTBasicTerm) obj; 
      return tag.equals(other.tag) && 
             value.equals(other.value); 
    } 
    return false; 
  } 


  public String toJSON()
  { String res = "{ \"root\" : \"" + value + "\", \"children\" : [] }"; 
    return res; 
  } 

  public String literalForm()
  { String res = value; 
    return res; 
  } 

  public Vector tokenSequence()
  { Vector res = new Vector(); 
    res.add("\"" + value + "\""); 
    return res; 
  } 

  public String asTextModel(PrintWriter out)
  { String id = Identifier.nextIdentifier(tag); 
    out.println(id + " : " + tag);  
    out.println(id + ".value = \"" + value + "\"");
    return id;  
  } 

  public String cg(CGSpec cgs)
  { Vector rules = cgs.getRulesForCategory(tag); 
    return cgRules(cgs,rules); 
  } 

  public String cgRules(CGSpec cgs, Vector rules)
  { if (rules == null) 
    { return value; } 

    for (int i = 0; i < rules.size(); i++) 
    { CGRule r = (CGRule) rules.get(i);
      Vector tokens = r.lhsTokens; 
      Vector vars = r.getVariables(); 

      if (vars.size() > 1 || tokens.size() > 1)
      { // System.out.println("> Rule " + r + " has too many variables/tokens to match basic term " + this); 
        continue; 
      } 
      

      // Either one variable _i (and token) or 
      // no variable and one token. 

      // System.out.println("> Trying to match variables/tokens of rule " + r + " for " + this);  
        
      Vector args = new Vector(); 
        // Strings resulting from terms[k].cg(cgs)
      Vector eargs = new Vector(); 
        // the actual terms[k]

      int k = 0; 
      boolean failed = false; 
      for (int j = 0; j < tokens.size() && !failed; j++) 
      { String tok = (String) tokens.get(j); 
        if (vars.contains(tok))
        { // allocate terms(j) to tok
          eargs.add(value); 
          k++; 
        } 
        else if (tok.equals(value))
        { } 
        else 
        { // System.out.println("> Rule " + r + " does not match " + this); 
          // System.out.println(tok + " /= " + value); 
          failed = true; // try next rule 
        } 
      } 

      if (!failed)
      { System.out.println("> Matched " + tag + " rule " + r + " for " + this);  

        for (int p = 0; p < eargs.size(); p++)
        { String textp = (String) eargs.get(p); 
          args.add(textp); 
        } 
        return r.applyRule(args,eargs,cgs);
      }   
    } 


    if (CGRule.hasDefaultRule(rules))
    { Vector tagrules = cgs.getRulesForCategory(tag);
      if (tagrules.equals(rules)) 
      { return toString(); }
      System.out.println(">> Applying default rule _0 |-->_0 to " + this);  
      return this.cgRules(cgs,tagrules); 
    } 

    return toString(); 
  }

  public String queryForm()
  { return toKM3(); } 

  public String toKM3()
  { 
    if ("this".equals(value))
    { expression = 
        BasicExpression.newVariableBasicExpression("self"); 
      return "self";
    } 

   
    if ("String".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("char".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("Character".equals(value) || 
        "InetAddress".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("CharSequence".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("StringBuffer".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("StringBuilder".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 

    if ("int".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 
    if ("Integer".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 
    if ("Byte".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 
    if ("Short".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 
    if ("byte".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 
    if ("short".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; } 

    if ("double".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    if ("Double".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    if ("Number".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    if ("Float".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    if ("float".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    if ("BigDecimal".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; } 
    
    if ("long".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; } 
    if ("BigInteger".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; } 
    if ("Long".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; } 

    if ("Boolean".equals(value) || "boolean".equals(value))
    { modelElement = new Type("boolean", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "boolean"; }

    if ("Object".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }
    if ("Collection".equals(value) || "AbstractCollection".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }
    if ("Class".equals(value))
    { modelElement = new Type("OclType", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclType"; }
    if ("Comparable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }
    if ("Cloneable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }
    if ("Serializable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }
    if ("Runnable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }



    if ("Constructor".equals(value))
    { modelElement = new Type("OclOperation", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclOperation"; }
    if ("Method".equals(value))
    { modelElement = new Type("OclOperation", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclOperation"; }
    if ("Field".equals(value))
    { modelElement = new Type("OclAttribute", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAttribute"; }

    if ("Thread".equals(value) || "Runtime".equals(value) || 
        "Process".equals(value) || "Timer".equals(value) || 
        "TimerTask".equals(value))
    { modelElement = new Type("OclProcess", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclProcess"; 
    } 

    if ("ThreadGroup".equals(value))
    { modelElement = new Type("OclProcessGroup", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclProcessGroup"; 
    } 

    if ("Date".equals(value))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; }
    if ("Calendar".equals(value) || 
        "GregorianCalendar".equals(value))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; }

    if ("Random".equals(value))
    { modelElement = new Type("OclRandom", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRandom"; }

    if ("Pattern".equals(value) || 
        "FileFilter".equals(value) ||
        "FilenameFilter".equals(value) || 
        "Matcher".equals(value))
    { modelElement = new Type("OclRegex", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRegex"; } 

    if ("ArrayList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("AbstractList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("Vector".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("LinkedList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("List".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("Stack".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("Queue".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("BlockingQueue".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("ArrayBlockingQueue".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("PriorityQueue".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
    if ("Stream".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; } 
     
    if ("BitSet".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("boolean", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(boolean)"; } 

    if ("Set".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; } 
    if ("HashSet".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; } 
    if ("SortedSet".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; } 
    if ("TreeSet".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; } 

    if ("Map".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 
    if ("HashMap".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 
    if ("SortedMap".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 
    if ("TreeMap".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 
    if ("Hashtable".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 
    if ("Properties".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; } 

    if ("Enumeration".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; } 
    if ("Iterator".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; } 
    if ("ListIterator".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; } 
    if ("StringTokenizer".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; } 

    if ("File".equals(value) || 
        "FileDescriptor".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Formatter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Scanner".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectOutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectInput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("ObjectOutput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataInput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataOutput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("DataOutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("PipedInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("PipedOutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; }
    if ("FilterInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FilterOutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("BufferedInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("BufferedOutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("PrintStream".equals(value) || "Socket".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FileOutputStream".equals(value) ||
        "FileInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Reader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FileReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("Writer".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FileWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("BufferedReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("BufferedWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("InputStreamReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("OutputStreamWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; }
    if ("PrintWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
 
    if ("Throwable".equals(value))
    { modelElement = new Type("OclException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclException"; } 

    if ("Error".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; } 
    if ("AWTError".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; } 
    if ("ThreadDeath".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; }
    if ("VirtualMachineError".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; } 
    if ("AssertionError".equals(value))
    { modelElement = new Type("AssertionException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "AssertionException"; } 
    
 
    if ("Exception".equals(value))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; } 
    if ("RuntimeException".equals(value) || 
        "InterruptedException".equals(value) ||
        "IllegalMonitorStateException".equals(value))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; } 
    if ("IOException".equals(value) || 
        "EOFException".equals(value) ||
        "SocketException".equals(value))
    { modelElement = new Type("IOException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IOException"; } 
    if ("ClassCastException".equals(value))
    { modelElement = new Type("CastingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "CastingException"; } 
    if ("NullPointerException".equals(value))
    { modelElement = new Type("NullAccessException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "NullAccessException"; } 
    if ("ArithmeticException".equals(value))
    { modelElement = new Type("ArithmeticException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ArithmeticException"; }
    if (value.endsWith("IndexOutOfBoundsException") || 
        "ArrayStoreException".equals(value))
    { modelElement = new Type("IndexingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IndexingException"; 
    } 
    if ("NoSuchElementException".equals(value) ||
        "UnknownHostException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; 
    }
    if ("InputMismatchException".equals(value) ||
        "UnsupportedOperationException".equals(value) ||
        "NumberFormatException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; }
    // if ("ArrayIndexOutOfBoundsException".equals(value) ||
    //     "StringIndexOutOfBoundsException".equals(value))
    // { return "IndexingException"; } 
    if ("IllegalAccessException".equals(value) ||
        "LinkageError".equals(value) || 
        "SecurityException".equals(value) ||  
        "NoClassDefFoundError".equals(value) ||
        "BindException".equals(value))
    { modelElement = new Type("AccessingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "AccessingException"; }
    if (value.endsWith("Exception"))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; } // default 

    if ("typeParameter".equals(tag) || 
        "classOrInterfaceType".equals(tag))
    { modelElement = Type.getTypeFor(value, enumtypes, entities); 

      if (modelElement == null)
      { modelElement = new Type(value, null); }  

      expression = new BasicExpression((Type) modelElement);
      System.out.println(">> Identified type " + this + " ==> " + modelElement); 
 
      return value; 
    } 


    String typ = ASTTerm.getType(value);
    expression = 
      BasicExpression.newValueBasicExpression(value,typ); 

    System.out.println(">> Expression of " + this + " ==> " + expression); 
      
    if (typ != null) 
    { System.out.println(">>> Type of " + value + " is " + typ);
      // expression.setType(new Type(typ, null)); 
    } 
    
    if (tag.equals("integerLiteral"))
    { System.out.println(">>> Type of " + value + " is integer"); 
      if (value.endsWith("L"))
      { ASTTerm.setType(value,"long");
        ASTTerm.setType(this,"long");
 
        expression.setType(new Type("long", null));
        return value.substring(0,value.length()-1);    
      } 
      else    
      { ASTTerm.setType(value,"int"); 
        ASTTerm.setType(this,"int");
        expression.setType(new Type("int", null));
      }  
    }
    else if (tag.equals("floatLiteral"))
    { System.out.println(">>> Type of " + value + " is double"); 
      expression.setType(new Type("double", null)); 
      ASTTerm.setType(this,"double");
      ASTTerm.setType(value,"double");
 
      if (value.endsWith("F"))
      { String baseValue = 
                     value.substring(0,value.length()-1); 
        expression = 
          BasicExpression.newValueBasicExpression(
                                         baseValue,typ);
        return baseValue;  
      } 
    }
    else if (tag.equals("literal") && value.endsWith("\"") && 
             value.startsWith("\""))
    { System.out.println(">>> Type of " + value + " is String"); 
      expression.setType(new Type("String", null)); 

      ASTTerm.setType(this,"String"); 
      ASTTerm.setType(value,"String"); 
    }
    else if (tag.equals("literal") && value.endsWith("\'") && 
             value.startsWith("\'"))
    { System.out.println(">>> Type of " + value + " is String"); 
      value = "\"" + value.substring(1,value.length()-1) + "\""; 
      expression.setType(new Type("String", null)); 
      ASTTerm.setType(this,"String"); 
      ASTTerm.setType(value,"String"); 
    }
    else if (tag.equals("literal") && 
             (value.equals("true") || value.equals("false"))
            )
    { System.out.println(">>> Type of " + value + " is String"); 
      expression.setType(new Type("boolean", null)); 
      ASTTerm.setType(this,"boolean"); 
      ASTTerm.setType(value,"boolean"); 
    } 
  
    return value; 
  } 

  public boolean isInteger()
  { if (tag.equals("integerLiteral")) 
    { return true; } 
    if (Expression.isInteger(value) || 
        Expression.isLong(value))
    { return true; } 
    return false; 
  } 

  public boolean isDouble()
  { if (tag.equals("floatLiteral")) 
    { return true; } 
    if (Expression.isDouble(value))
    { return true; } 
    return false; 
  } 

  public boolean isBoolean()
  { if (value.equals("true") || value.equals("false"))
    { return true; } 
    return false; 
  } // Ok for Java and OCL. 

  public boolean isIdentifier()
  { return "primary".equals(tag) && 
           value.length() > 0 && 
           Character.isJavaIdentifierStart(value.charAt(0)); 
  } 

  public String getType()
  { String type = (String) types.get(value); 
    if (type != null) 
    { return type; } 
    else if (tag.equals("integerLiteral"))
    { return "int"; }
    else if (tag.equals("floatLiteral"))
    { return "double"; }
    else if (tag.equals("literal") && value.endsWith("\"") && 
             value.startsWith("\""))
    { return "String"; }
    else if (tag.equals("literal") && value.endsWith("\'") && 
             value.startsWith("\'"))
    { return "String"; }
    else if (tag.equals("literal") && value.endsWith("\'") && 
             value.startsWith("\'"))
    { return "String"; }
    else if (tag.equals("literal") && 
             (value.equals("true") || value.equals("false"))
            )
    { return "boolean"; } 
  
    return "OclAny"; 
  }

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