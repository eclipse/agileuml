/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 

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

  public String cg(CGSpec cgs)
  { Vector rules = cgs.getRulesForCategory(tag); 

    for (int i = 0; i < rules.size(); i++) 
    { CGRule r = (CGRule) rules.get(i);
      Vector tokens = r.lhsTokens; 
      Vector vars = r.getVariables(); 

      if (vars.size() > 1 || tokens.size() > 1)
      { System.out.println("> Rule " + r + " has too many variables/tokens to match basic term " + this); 
        continue; 
      } 
      

      // Either one variable _i (and token) or 
      // no variable and one token. 

      System.out.println("> Trying to match variables/tokens of rule " + r + " for " + this);  
        
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
        { System.out.println("> Rule " + r + " does not match " + this); 
          System.out.println(tok + " /= " + value); 
          failed = true; // try next rule 
        } 
      } 

      if (!failed)
      { System.out.println("> Matched rule " + r + " for " + this);  

        for (int p = 0; p < eargs.size(); p++)
        { String textp = (String) eargs.get(p); 
          args.add(textp); 
        } 
        return r.applyRule(args,eargs,cgs);
      }   
    } 
    return toString(); 
  }


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

    if ("Long".equals(value))
    { return "long"; } 

    if ("Boolean".equals(value))
    { return "boolean"; }

    if ("Object".equals(value))
    { return "OclAny"; }
    // if ("Collection".equals(value))
    // { return "Collection"; }

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

    if ("Formatter".equals(value))
    { return "File"; } 
    if ("Scanner".equals(value))
    { return "File"; } 
    if ("ObjectInputStream".equals(value))
    { return "File"; } 
    if ("ObjectOutputStream".equals(value))
    { return "File"; } 
    if ("ObjectInput".equals(value))
    { return "File"; } 
    if ("ObjectOutput".equals(value))
    { return "File"; } 
    if ("DataInput".equals(value))
    { return "File"; } 
    if ("DataOutput".equals(value))
    { return "File"; } 
    if ("DataInputStream".equals(value))
    { return "File"; } 
    if ("DataOutputStream".equals(value))
    { return "File"; } 
    if ("PipedInputStream".equals(value))
    { return "File"; } 
    if ("PipedOutputStream".equals(value))
    { return "File"; }
    if ("FilterInputStream".equals(value))
    { return "File"; } 
    if ("FilterOutputStream".equals(value))
    { return "File"; } 
    if ("BufferedInputStream".equals(value))
    { return "File"; } 
    if ("BufferedOutputStream".equals(value))
    { return "File"; } 
    if ("PrintStream".equals(value))
    { return "File"; } 
    if ("Reader".equals(value))
    { return "File"; } 
    if ("Writer".equals(value))
    { return "File"; } 
    if ("BufferedReader".equals(value))
    { return "File"; } 
    if ("BufferedWriter".equals(value))
    { return "File"; } 
    if ("InputStreamReader".equals(value))
    { return "File"; } 
    if ("InputStreamWriter".equals(value))
    { return "File"; }
    if ("PrintWriter".equals(value))
    { return "File"; } 
 
    if ("Throwable".equals(value))
    { return "Exception"; } 

    if ("Error".equals(value))
    { return "EnvironmentException"; } 
    if ("AWTError".equals(value))
    { return "EnvironmentException"; } 
    if ("ThreadDeath".equals(value))
    { return "EnvironmentException"; }
	if ("VirtualMachineError".equals(value))
    { return "EnvironmentException"; } 
	if ("AssertionError".equals(value))
    { return "EnvironmentException"; } 
 
    if ("Exception".equals(value))
    { return "ProgramException"; } 
    if ("RuntimeException".equals(value))
    { return "ProgramException"; } 
    if ("IOException".equals(value))
    { return "ProgramException"; } 
    if ("ClassCastException".equals(value))
    { return "ProgramException"; } 
    if ("NullPointerException".equals(value))
    { return "ProgramException"; } 
    if ("ArithmeticException".equals(value))
    { return "ProgramException"; }
    if ("IndexOutOfBoundsException".equals(value))
    { return "ProgramException"; } 
    if ("NoSuchElementException".equals(value))
    { return "ProgramException"; }
    if ("InputMismatchException".equals(value))
    { return "ProgramException"; }
    if ("ArrayIndexOutOfBoundsException".equals(value))
    { return "ProgramException"; } 

    return value; 
  } 
} 