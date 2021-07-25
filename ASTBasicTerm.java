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

  public void setValue(String v)
  { value = v; } 

  public String toString()
  { String res = "(" + tag + " " + value + ")"; 
    return res; 
  } 

  public String literalForm()
  { String res = value; 
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
    return toString(); 
  }

  public String queryForm()
  { return toKM3(); } 

  public String toKM3()
  { 
    if ("this".equals(value))
    { return "self"; } 

    if ("char".equals(value))
    { return "String"; } 
    if ("Character".equals(value))
    { return "String"; } 
    if ("CharSequence".equals(value))
    { return "String"; } 
    if ("StringBuffer".equals(value))
    { return "String"; } 
    if ("StringBuilder".equals(value))
    { return "String"; } 

    if ("Integer".equals(value))
    { return "int"; } 
    if ("Byte".equals(value))
    { return "int"; } 
    if ("Short".equals(value))
    { return "int"; } 
    if ("byte".equals(value))
    { return "int"; } 
    if ("short".equals(value))
    { return "int"; } 

    if ("Double".equals(value))
    { return "double"; } 
    if ("Number".equals(value))
    { return "double"; } 
    if ("Float".equals(value))
    { return "double"; } 
    if ("float".equals(value))
    { return "double"; } 
    if ("BigDecimal".equals(value))
    { return "double"; } 
    
    if ("BigInteger".equals(value))
    { return "long"; } 
    if ("Long".equals(value))
    { return "long"; } 

    if ("Boolean".equals(value))
    { return "boolean"; }

    if ("Object".equals(value))
    { return "OclAny"; }
    if ("Collection".equals(value))
    { return "OclCollection"; }
    if ("Class".equals(value))
    { return "OclType"; }
    if ("Comparable".equals(value))
    { return "OclComparable"; }
    if ("Constructor".equals(value))
    { return "OclOperation"; }
    if ("Method".equals(value))
    { return "OclOperation"; }
    if ("Field".equals(value))
    { return "OclAttribute"; }

    if ("Thread".equals(value) || "Runnable".equals(value) ||
        "Process".equals(value))
    { return "OclProcess"; } 

    if ("ThreadGroup".equals(value))
    { return "OclProcessGroup"; } 

    if ("Date".equals(value))
    { return "OclDate"; }
    if ("Calendar".equals(value))
    { return "OclDate"; }

    if ("Pattern".equals(value) || 
        "FileFilter".equals(value) ||
        "FilenameFilter".equals(value) || 
        "Matcher".equals(value))
    { return "OclRegex"; } 

    if ("ArrayList".equals(value))
    { return "Sequence"; } 
    if ("Vector".equals(value))
    { return "Sequence"; } 
    if ("LinkedList".equals(value))
    { return "Sequence"; } 
    if ("List".equals(value))
    { return "Sequence"; } 
    if ("Stack".equals(value))
    { return "Sequence"; } 
    if ("Queue".equals(value))
    { return "Sequence"; } 
    if ("BlockingQueue".equals(value))
    { return "Sequence"; } 
    if ("ArrayBlockingQueue".equals(value))
    { return "Sequence"; } 
    if ("PriorityQueue".equals(value))
    { return "Sequence"; } 
     
    if ("HashSet".equals(value))
    { return "Set"; } 
    if ("SortedSet".equals(value))
    { return "Set"; } 
    if ("TreeSet".equals(value))
    { return "Set"; } 

    if ("HashMap".equals(value))
    { return "Map"; } 
    if ("SortedMap".equals(value))
    { return "Map"; } 
    if ("TreeMap".equals(value))
    { return "Map"; } 
    if ("Hashtable".equals(value))
    { return "Map"; } 
    if ("Properties".equals(value))
    { return "Map"; } 

    if ("Enumeration".equals(value))
    { return "OclIterator"; } 
    if ("Iterator".equals(value))
    { return "OclIterator"; } 
    if ("ListIterator".equals(value))
    { return "OclIterator"; } 
    if ("StringTokenizer".equals(value))
    { return "OclIterator"; } 

    if ("File".equals(value))
    { return "OclFile"; } 
    if ("Formatter".equals(value))
    { return "OclFile"; } 
    if ("Scanner".equals(value))
    { return "OclFile"; } 
    if ("ObjectInputStream".equals(value))
    { return "OclFile"; } 
    if ("ObjectOutputStream".equals(value))
    { return "OclFile"; } 
    if ("ObjectInput".equals(value))
    { return "OclFile"; } 
    if ("ObjectOutput".equals(value))
    { return "OclFile"; } 
    if ("DataInput".equals(value))
    { return "OclFile"; } 
    if ("DataOutput".equals(value))
    { return "OclFile"; } 
    if ("DataInputStream".equals(value))
    { return "OclFile"; } 
    if ("DataOutputStream".equals(value))
    { return "OclFile"; } 
    if ("PipedInputStream".equals(value))
    { return "OclFile"; } 
    if ("PipedOutputStream".equals(value))
    { return "OclFile"; }
    if ("FilterInputStream".equals(value))
    { return "OclFile"; } 
    if ("FilterOutputStream".equals(value))
    { return "OclFile"; } 
    if ("BufferedInputStream".equals(value))
    { return "OclFile"; } 
    if ("BufferedOutputStream".equals(value))
    { return "OclFile"; } 
    if ("PrintStream".equals(value))
    { return "OclFile"; } 
    if ("Reader".equals(value))
    { return "OclFile"; } 
    if ("Writer".equals(value))
    { return "OclFile"; } 
    if ("BufferedReader".equals(value))
    { return "OclFile"; } 
    if ("BufferedWriter".equals(value))
    { return "OclFile"; } 
    if ("InputStreamReader".equals(value))
    { return "OclFile"; } 
    if ("InputStreamWriter".equals(value))
    { return "OclFile"; }
    if ("PrintWriter".equals(value))
    { return "OclFile"; } 
 
    if ("Throwable".equals(value))
    { return "OclException"; } 

    if ("Error".equals(value))
    { return "SystemException"; } 
    if ("AWTError".equals(value))
    { return "SystemException"; } 
    if ("ThreadDeath".equals(value))
    { return "SystemException"; }
    if ("VirtualMachineError".equals(value))
    { return "SystemException"; } 
    if ("AssertionError".equals(value))
    { return "AssertionException"; } 
    
 
    if ("Exception".equals(value))
    { return "ProgramException"; } 
    if ("RuntimeException".equals(value))
    { return "ProgramException"; } 
    if ("IOException".equals(value))
    { return "IOException"; } 
    if ("ClassCastException".equals(value))
    { return "CastingException"; } 
    if ("NullPointerException".equals(value))
    { return "NullAccessException"; } 
    if ("ArithmeticException".equals(value))
    { return "ArithmeticException"; }
    if (value.endsWith("IndexOutOfBoundsException"))
    { return "IndexingException"; } 
    if ("NoSuchElementException".equals(value))
    { return "IncorrectElementException"; }
    if ("InputMismatchException".equals(value) ||
        "NumberFormatException".equals(value))
    { return "IncorrectElementException"; }
    // if ("ArrayIndexOutOfBoundsException".equals(value) ||
    //     "StringIndexOutOfBoundsException".equals(value))
    // { return "IndexingException"; } 
    if ("IllegalAccessException".equals(value) || 
        "NoClassDefFoundError".equals(value))
    { return "AccessingException"; } 

    String type = ASTTerm.getType(value);
   
      
    if (type != null) 
    { System.out.println(">>> Type of " + value + " is " + type); } 
    else if (tag.equals("integerLiteral"))
    { System.out.println(">>> Type of " + value + " is integer"); 
      ASTTerm.setType(value,"integer"); 
    }
    else if (tag.equals("floatLiteral"))
    { System.out.println(">>> Type of " + value + " is double"); 
      ASTTerm.setType(value,"real"); 
    }
    else if (tag.equals("literal") && value.endsWith("\"") && 
             value.startsWith("\""))
    { System.out.println(">>> Type of " + value + " is String"); 
      ASTTerm.setType(value,"String"); 
    }
    else if (tag.equals("literal") && value.endsWith("\'") && 
             value.startsWith("\'"))
    { System.out.println(">>> Type of " + value + " is String"); 
      value = "\"" + value.substring(1,value.length()-1) + "\""; 
      ASTTerm.setType(value,"String"); 
    }
    else if (tag.equals("literal") && 
             (value.equals("true") || value.equals("false"))
            )
    { System.out.println(">>> Type of " + value + " is String"); 
      ASTTerm.setType(value,"boolean"); 
    } 
  
    return value; 
  } 

  public String getType()
  { String type = (String) types.get(value); 
    if (type != null) 
    { return type; } 
    else if (tag.equals("integerLiteral"))
    { return "integer"; }
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

  public boolean updatesObject()
  { return false; } 
} 