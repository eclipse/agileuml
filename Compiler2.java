import java.io.*; 
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JOptionPane; 
import javax.swing.JTextArea; 
import java.awt.*; 

/******************************
* Copyright (c) 2003--2025 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* This is a parser for simple expressions involving =, =>, <=>, &, or. 
   For example: var1 = 5 & var2 = 6 => var3 = tty
   No brackets are needed: => groups expressions first, then or then & 
   so   att = val & att2 = val2 or ggh = val4 => val5 = val6 
   is interpreted as 
   ((att = val & att2 = val2) or (ggh = val4)) => (val5 = val6)

  Modified to allow  !=  (25.12.2000)
  Modified to allow object references (7.4.2001)
  Modified to allow <, >, etc (12.4.2002)
  Modified to allow assignment := (21.4.2002)
  Modified to allow +, - inside := and comparitors (4.5.2002)
  Allow ":" (15.1.2003)
  Allow "<:, intersect, union" (6.4.2003) 
  Added "*, /" and checkIfSetExpression (16.4.2003) 
  Introduced "|" and unspaced parsing (2.1.2006)
  
  Breaking change: no longer accepts {elems} as a set, must be written as Set{elems} (28.12.2020)

  Breaking change: creation statements must be written as var x : C, not just as x : C (Version 2.0)
  
  package: Utilities
*/ 

public class Compiler2
{ static final int INUNKNOWN = 0; 
  static final int INBASICEXP = 1; 
  static final int INSYMBOL = 2; 
  static final int INSTRING = 3; 

  static final java.util.regex.Pattern 
     cstlVarPatt = java.util.regex.Pattern.compile("_[0-9]"); 
    
  static Vector extensionOperators = new Vector(); 

  Vector lexicals; // of StringBuffer
  Vector lexs = new Vector(); // of String 
  int[] bcount; // bracket count at this point in the lexical list
  Vector ops = new Vector(); // of OpOccurrence
  Expression finalexpression;

  static String atlModuleName = "";  

  public static void addOperator(String op) 
  { extensionOperators.add(op); } 

  public boolean isStringDelimiter(char c)
  { return (c == '"' || c == '`' || c == '\''); } 

  /* This method breaks up a string into a sequence of strings, using 
     spaces as the break points:  */ 

  public void lexicalanalysis(String str) 
  { 
    boolean intoken = false;  /* True if a token has been recognised */ 
    boolean instring = false;  // true if within a string
    

    int explen = str.length(); 
    lexicals = new Vector();  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    char prev = ' '; 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i);

      if (intoken)
      { if (instring)
        { if (isStringDelimiter(c) && prev != '\\')
          { sb.append('"'); instring = false; 
            // intoken = false;
          }
          else 
          { sb.append(c); }
        }
        else  // intoken, not instring
        { if (isStringDelimiter(c))
          { instring = true; 
            // sb = new StringBuffer();
            // lexicals.addElement(sb); 
            sb.append('"');
          }
          else if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
          { intoken = false; }
          else 
          { sb.append(c); }
        }
      }
      else // not intoken
      { if (isStringDelimiter(c))
        { intoken = true; 
          instring = true; sb = new StringBuffer();
          lexicals.addElement(sb); sb.append('"');
        }
        else if (c == ' ' || c == '\n' || c == '\t'|| c == '\r')
        { intoken = false; }
        else 
        { intoken = true; sb = new StringBuffer();
          lexicals.addElement(sb); sb.append(c);
        }
      }
      prev = c; 
    }
  
    int llen = lexicals.size(); 
    bcount = new int[llen]; 
    int bnest = 0; 
 
    for (int i = 0; i < llen; i++) 
    { StringBuffer sbb = (StringBuffer) lexicals.get(i); 
      lexs.add(sbb + "");
      bcount[i] = bnest; 
      if ("(".equals(sbb + "")) 
      { bnest++; }  
      else if (")".equals(sbb + ""))
      { bnest--; } 
      else if (Expression.operators.contains(sbb + ""))
      { 
        addToOps(new OpOccurrence(sbb + "",i,bnest)); 
      } 
      else if ("and".equals(sbb + ""))
      { addToOps(new OpOccurrence("&",i,bnest));
        sbb = new StringBuffer("&"); 
      } 
      else if ("implies".equals(sbb + ""))
      { addToOps(new OpOccurrence("=>",i,bnest));
        sbb = new StringBuffer("=>"); 
      } 
    }   
    System.out.println(ops);        
  }

  public int checkclones0()
  { Vector clones = new Vector(); 
    int clonecount = 0; 
    for (int i = 0; i < lexicals.size() - 10; i++) 
    { String str = ""; 
      for (int j = i; j < i+10; j++) 
      { str = str + lexicals.get(j); } 
      if (clones.contains(str)) 
      { System.err.println(">>> Clone: " + str + " at " + i); 
        clonecount++; 
      } 
      else 
      { clones.add(str); } 
    } 
    return clonecount; 
  } 

  public int checkclones(Vector types, Vector entities)
  { Vector clones = new Vector(); 

    int clonecount = 0; 

    for (int i = 0; i < lexicals.size() - 10; i++) 
    { boolean match = true; 
      int j = i + 11; 
      int k = 0; 
      String str = ""; 

      while (j + k < lexicals.size() && i + k < j && match) 
      { String stri = "" + lexicals.get(i+k); 
        String strj = "" + lexicals.get(j+k);
        if (stri.equals(strj))
        { k++; 
          str = str + " " + stri; 
        } 
        else 
        { match = false; 
          if (k > 10) 
          { Expression e = parse_ATLexpression(0,i,i+k-1,entities,types); 
            if (e != null) 
            { clones.add(">>> CLONE at " + i + "," + j + " SIZE= " + k + ">> " + str);  
              clonecount++;
            }  
          } 
          j = j + k + 1; 
          k = 0; 
          str = ""; 
          match = true; 
        } 
      }

      if (match && k > 10) 
      { clones.add(">>> CLONE at " + i + "," + j + " SIZE= " + k + ">> " + str);  
        clonecount++; 
      } 
    } 

    System.out.println(clones);  
    return clonecount; 
  } 

  private void addToOps(OpOccurrence oc)
  { // Orders ocs by bcount, then by op priority, then by position
    // if (ops.size() == 0)
    // { ops.add(oc); 
    //   return; 
    // } 
    System.out.println(oc); 

    for (int i = 0; i < ops.size(); i++)
    { OpOccurrence best = (OpOccurrence) ops.get(i); 
      if (oc.bcount < best.bcount)
      { ops.add(i,oc); 
        return; 
      }   
      if (oc.bcount == best.bcount && 
          oc.priority() < best.priority())
      { ops.add(i,oc); 
        return; 
      } 
      if (oc.bcount == best.bcount && oc.op.equals(best.op) &&
          oc.location < best.location) 
      { ops.add(i,oc); 
        return; 
      } 
    }    
    ops.add(oc); 
  } 

  private OpOccurrence getBestOcc(int st, int en)
  { OpOccurrence res = null; 
    for (int i = 0; i < ops.size(); i++)
    { OpOccurrence op = (OpOccurrence) ops.get(i); 
      if (op.location >= st && op.location <= en)
      { return op; } 
    } 
    return res; 
  } 

  private int getBestOpOccurrence(String op, int st, int en)
  { int pri = lexs.size() + 1; 
    int best = en + 1; 

    for (int i = st; i < en; i++)
    { String pp = (String) lexs.get(i); 
      if (op.equals(pp))
      { if (bcount[i] < pri)
        { best = i;
          pri = bcount[i];
        } 
      } 
    } 
    return best; 
  } 

  private int getBestAndOpOccurrence(int st, int en)
  { int pri = lexs.size() + 1; 
    int best = en + 1; 
    for (int i = st; i < en; i++)
    { String pp = (String) lexs.get(i); 
      if ("&".equals(pp))
      { if (bcount[i] < pri)
        { best = i; 
          pri = bcount[i]; 
        } 
      } 
    } 
    return best; 
  } 

   /*   if (str.charAt(i) == ' ' || str.charAt(i) == '\n')
      { 
        if (intoken) { intoken = false; } 
      } 
      else 
      { 
        if (intoken == false) 
        { 
          intoken = true; 
          sb = new StringBuffer();
          lexicals.addElement(sb);  
        }
        sb.append(str.charAt(i)); 
      }
    }
  }    */ 

  public boolean isValidOCLOperator(String str) 
  { if (str.equals("exists") || str.equals("existsLC") || 
        str.equals("exists1") || 
        str.equals("forAll") || str.equals("allInstances") ||
        str.equals("select") || str.equals("collect") || 
        str.equals("reject") ||
        str.equals("includes") || str.equals("including") || 
        str.equals("excludes") ||
        str.equals("excluding") || str.equals("intersection") || 
        str.equals("union") ||
        str.equals("unionAll") || str.equals("intersectAll") || 
        str.equals("at") ||
        str.equals("apply") || 
        str.equals("selectMaximals") || 
        str.equals("selectMinimals") || str.equals("not") ||
        str.equals("any") || str.equals("size") || 
        str.equals("last") ||
        str.equals("first") || str.equals("includesAll") || 
        str.equals("excludesAll") ||
        str.equals("append") || str.equals("prepend") || 
        str.equals("min") ||
        str.equals("max") || str.equals("sum") || 
        str.equals("display") ||
        str.equals("before") || str.equals("after") || 
        str.equals("xor") ||
        str.equals("toLowerCase") || str.equals("toUpperCase") || 
        str.equals("Sum") ||
        str.equals("Prd") || str.equals("symmetricDifference") || 
        str.equals("oclIsUndefined") ||
        str.equals("oclIsInvalid") ||
        str.equals("oclAsType") || 
        str.equals("oclIsKindOf") ||
        str.equals("oclIsTypeOf") ||  
        str.equals("oclIsNew") || 
        str.equals("reverse") || str.equals("sort") || 
        str.equals("subcollections") || 
        str.equals("iterator") || 
        str.equals("front") || str.equals("tail") || 
        str.equals("insertAt") || // str.equals("insertInto") || 
        str.equals("trim") || str.equals("lastIndexOf") ||
        str.equals("equalsIgnoreCase") || str.equals("split") ||
        str.equals("isMatch") || str.equals("hasMatch") ||
        str.equals("allMatches") || 
        str.equals("firstMatch") ||
        str.equals("replaceAllMatches") ||
        str.equals("replaceFirstMatch") ||
        str.equals("replace") || str.equals("replaceAll") ||
        str.equals("subrange") || str.equals("characters") || 
        str.equals("isLong") || str.equals("toLong") || 
        str.equals("closure") || 
        str.equals("asSet") || str.equals("asSequence") || 
        str.equals("asOrderedSet") ||
        str.equals("asArray") || 
        str.equals("resizeTo") || 
        str.equals("sequenceRange") || 
        str.equals("sqr") || 
        str.equals("floor") || str.equals("ceil") ||
        str.equals("round") || str.equals("exp") || 
        str.equals("pow") || str.equals("gcd") || 
        str.equals("sin") || str.equals("cos") || 
        str.equals("tan") || 
        str.equals("asin") || str.equals("acos") || 
        str.equals("atan") ||
        str.equals("sinh") || str.equals("cosh") || 
        str.equals("tanh") ||
        str.equals("log10") || str.equals("cbrt") || 
        str.equals("isInteger") ||
        str.equals("toInteger") || 
        str.equals("isReal") || str.equals("toReal") ||
        str.equals("toBoolean") || 
        str.equals("byte2char") ||
        str.equals("char2byte") ||
        str.equals("oclType") || str.equals("copy") || 
        str.equals("log") || str.equals("count") || 
        str.equals("hasPrefix") ||
        str.equals("hasSuffix") || 
        str.equals("isEmpty") || str.equals("notEmpty") ||
        str.equals("isUnique") || str.equals("prd") || 
        str.equals("sortedBy") ||
        // str.equals("excludingAt") ||
        // str.equals("excludingSubrange") ||  
        // str.equals("setSubrange") ||  
        str.equals("excludingFirst") || 
        // str.equals("setAt") || str.equals("restrict") ||
        // str.equals("antirestrict") ||  
        str.equals("sqrt") || str.equals("abs") || 
        "flatten".equals(str) || 
        str.equals("or") ||
        str.equals("indexOf") || 
        str.equals("isDeleted") || str.equals("iterate"))
    { return true; } 
    return false; 
  } 

  public boolean checkSyntax(Entity context, Vector entities, 
                             Vector varsymbs, Vector messages)
  { Vector variables = new Vector(); 
    Vector openBrackets = new Vector();
    for (int i = 0; i < lexicals.size(); i++)
    { String tok = lexicals.get(i) + "";
      if ("(".equals(tok))
      { openBrackets.add(0, new Integer(i)); }
      else if (")".equals(tok))
      { if (openBrackets.size() > 0)
        { openBrackets.remove(0); }
        else 
        { messages.add("Unmatched ) at token " + i);
          JOptionPane.showMessageDialog(null, "Unmatched ) at token " + i, "Syntax error", JOptionPane.ERROR_MESSAGE);  
          return false;
        } 
      }
      else if ("->".equals(tok))
      { if (i + 1 < lexicals.size())
        { if (isValidOCLOperator(lexicals.get(i+1) + ""))
          { i++; }
          else 
          { messages.add("Unrecognised OCL operator: " + lexicals.get(i+1) + " after ->");
            JOptionPane.showMessageDialog(null, "Unrecognised OCL operator: " + lexicals.get(i+1) + " after ->", "Syntax error", JOptionPane.ERROR_MESSAGE);  
            return false;
          }
        }
        else 
        { messages.add("Invalid syntax: -> at end of expression");
          JOptionPane.showMessageDialog(null, "Invalid syntax: -> at end of expression", "Syntax error", JOptionPane.ERROR_MESSAGE);  
          return false;
        }
      }
      else 
      { boolean res = validIdentifierOrOperator(i,tok,context,entities,varsymbs,variables,messages);
        if (res == false) { return res; }
      }
    }
    if (openBrackets.size() > 0)
    { messages.add("Unmatched brackets at positions " + openBrackets);
      JOptionPane.showMessageDialog(null, "Unmatched brackets at positions " + openBrackets,
                                    "Syntax error", JOptionPane.ERROR_MESSAGE);  
                return false;
    }
    return true;
  }

  public boolean validIdentifierOrOperator(int i, String tok, Entity context, 
                                           Vector entities, Vector varsymbs, 
                                           Vector variables, Vector messages)
  { if (Expression.alloperators.contains(tok) ||
        "[".equals(tok) || "]".equals(tok) ||
        "{".equals(tok) || "}".equals(tok) || 
        "Set{".equals(tok) || "Sequence{".equals(tok) ||
        "Set".equals(tok) || "Sequence".equals(tok) || 
        "SortedSet{".equals(tok) ||
        "SortedSet".equals(tok) ||
        "Map{".equals(tok) || "Map".equals(tok) ||
        "SortedMap{".equals(tok) || 
        "SortedMap".equals(tok) ||
        "Integer".equals(tok) ||
        "|".equals(tok) || ",".equals(tok))
    { return true; }
    ModelElement me = ModelElement.lookupByName(tok,entities); 
    if (me != null) { return true; } 
    if (Expression.isValue(tok))
    { return true; } 
    if (Expression.isSet(tok) || Expression.isSequence(tok))
    { return true; } 
    int dotind = tok.indexOf(".");
    if (dotind == -1)
    { return validIdentifier(i,tok,context,varsymbs,variables,entities,messages); }
    StringTokenizer st = new StringTokenizer(tok, ".");
    while (st.hasMoreTokens())
    { String str = st.nextToken();
      return validIdentifier(i,str,context,varsymbs,variables,entities,messages);
    }
    return true;
  }

  public boolean validIdentifier(int i, String tok, Entity context, 
                                 Vector varsymbs, Vector variables, Vector entities, Vector messages)
  { String str = tok; 
    int atind = tok.indexOf("@"); 
    if (atind >= 0)
    { str = tok.substring(0,atind); } 
    if (variables.contains(str) || 
        "Integer".equals(str) || str.equals("Real"))
    { return true; } 
    if (context != null && context.hasFeatureOrOperation(str))
    { return true; }
    if (isKeyword(str))
    { messages.add("Warning: " + str + " is a keyword");
      // JOptionPane.showMessageDialog(null, "Error: " + str + " invalid identifier (keyword)",
      //    "Syntax error", JOptionPane.ERROR_MESSAGE);  
      return false; 
    }
    if (invalidLexical(str))
    { messages.add("Error: " + str + " invalid lexical");
      JOptionPane.showMessageDialog(null, 
           "Error: " + str + " invalid lexical",
           "Syntax error", JOptionPane.ERROR_MESSAGE);  
      return false; 
    }
    // also if contains "_"
    if ("self".equals(str) && context != null) { } 
    else if (i < lexicals.size() - 1 && varsymbs.contains(lexicals.get(i+1) + "") ) 
    { JOptionPane.showMessageDialog(null, str + " is a variable",
                                    "Syntax check", JOptionPane.INFORMATION_MESSAGE);  
      variables.add(str); 
    } 
    else 
    { ModelElement me = ModelElement.lookupByName(str,entities); 
      if (me != null) { return true; } 
      else 
      { messages.add("Warning: " + str + " not entity or feature of context class: " + context);
        /* JOptionPane.showMessageDialog(null, "Warning: " + str + 
                                    " not feature of context class: " + context,
                                    "Syntax issue", JOptionPane.WARNING_MESSAGE);  */ 
      } 
    } 
    return true;
  }

  public boolean invalidLexical(String str) 
  { if ("%".equals(str) || "_".equals(str) || "?".equals(str) || "~".equals(str) ) 
    { return true; } 
    return false; 
  } 

  public boolean invalidBasicExp(String str) 
  { if ("(".equals(str) || ")".equals(str) || 
        "[".equals(str) || "]".equals(str) ||
        "{".equals(str) || "}".equals(str) || 
        Expression.alloperators.contains(str) || 
        ",".equals(str) || // "!".equals(str) || 
        "%".equals(str) || 
        "_".equals(str) || 
        "?".equals(str) || "~".equals(str) ) 
    { return true; } 
    return false; 
  } 


  public static boolean isKeyword(String str) 
  { if (str.equals("class") || 
        str.equals("if") || str.equals("while") ||
        str.equals("error") || 
        str.equals("catch") || 
        str.equals("try") || 
        str.equals("extends") || 
        str.equals("assert") || 
        str.equals("return") || str.equals("break") || 
        str.equals("continue") || 
        str.equals("float") || str.equals("char") || 
        str.equals("byte") || 
        str.equals("boolean") || str.equals("int") || 
        str.equals("long") || str.equals("double") ||  
        str.equals("transient") || str.equals("volatile") || 
        str.equals("short") ||
        str.equals("native") || str.equals("enum") || 
        str.equals("package") ||
        str.equals("strictfp") || str.equals("wait") || 
        str.equals("goto") || 
        str.equals("notify") || 
        str.equals("notifyAll") || 
        str.equals("case") || str.equals("switch") || 
        str.equals("this") || 
        str.equals("implements") ||
        str.equals("new") || 
        str.equals("interface") || str.equals("byte") || 
        str.equals("finally") ||
        str.equals("synchronized") || 
        str.equals("until") || str.equals("do") || 
        str.equals("interface") || 
        str.equals("extends") || str.equals("implements") ||
        str.equals("for") || str.equals("instanceof") || 
        str.equals("private") || 
        str.equals("public") || str.equals("final") || 
        str.equals("static") ||
        str.equals("void") || str.equals("abstract") || 
        str.equals("protected") ||
        str.equals("const") || str.equals("import") || 
        str.equals("imports") || 
        str.equals("else") || str.equals("Vector") ||
        str.equals("List") || str.equals("Collection") || 
        str.equals("throw") || str.equals("throws"))
    { return true; }  // Java keywords. "super" is allowed. 
    return false; 
  } 

  public static boolean isAnyKeyword(String str) 
  { if (isKeyword(str) || 
        str.equals("endtry") || 
        str.equals("reference") ||
        str.equals("attribute") || 
        str.equals("container") || 
        str.equals("usecase") || 
        str.equals("stereotype") || 
        str.equals("repeat") ||
        str.equals("null"))
    { return true; }  // Java keywords. "super" is allowed. 
    return false; 
  } 

  public static boolean isSimpleIdentifier(String s)
  { boolean res = true; 
    if (Character.isLetter(s.charAt(0))) {}
    else 
    { return false; } 

    for (int i = 0; i < s.length(); i++) 
    { if (Character.isLetterOrDigit(s.charAt(i)) || 
          (i >= 1 && s.charAt(i) == '_')) 
      { } 
      else 
      { return false; } 
    } 
    return res; 
  } // does not permit ! inside strings, or _

  private static boolean isIdentifier(String s)
  { boolean res = true; 
    if (Character.isDigit(s.charAt(0))) { return false; } 

    for (int i = 0; i < s.length(); i++) 
    { if (Character.isLetterOrDigit(s.charAt(i)) || 
          s.charAt(i) == '!' || s.charAt(i) == '@') { } 
      else 
      { return false; } 
    } 
    return res; 
  } // does not permit ! inside strings, or _

  private static boolean isATLIdentifier(String s)
  { boolean res = true; 
    if (Character.isLetter(s.charAt(0)) || s.charAt(0) == '#') 
    { } else { return false; }   

    for (int i = 1; i < s.length(); i++) 
    { if (Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '!') { } 
      else 
      { return false; } 
    } 
    return res; 
  } 

  private Vector identifierSequence(int st, int en)
  { if (st > en) { return null; }  // or empty sequence?
 
    Vector res = new Vector(); 
    for (int i = st; i < en; i++) 
    { String ss = lexicals.get(i) + ""; 
      if (isIdentifier(ss))
      { BasicExpression ee = new BasicExpression(ss,0); 
        res.add(ee.checkIfSetExpression()); 
      } 
      else if (",".equals(ss)) { } 
      else { return null; } 
    }

    String last = lexicals.get(en) + ""; 
    if (isIdentifier(last))
    { BasicExpression ee = new BasicExpression(last,0); 
      res.add(ee.checkIfSetExpression()); 
    } 
 
    return res; 
  } 
    
  private static boolean isBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || c == '$' || c == '@');  } 

  private static boolean isATLBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || c == '#' || c == '!' || c == '_'); } 

  private static boolean isSymbolCharacter(char c)
  { return (c == '<' || c == '>' || c == '=' || 
            c == '*' || c == '/' || c == '\\' ||
            c == '-' || c == ',' || c == ';' || c == '?' ||
            c == '!' ||
            c == '+' || c == '&' || c == '^' || 
            c == ':' || c == '|' ||
            c == '(' || c == ')' || c == '{' || c == '}' || 
            c == '[' ||
            c == ']' || c == '#'); 
  } // c == '\'' ||
  // why include single quote? It should not be used. 


  private static boolean isXMLSymbolCharacter(char c)
  { return (c == '<' || c == '>' || c == '=' || c == '*' || c == '/' || c == '\\' ||
            c == '-' || c == ',' || c == '\'' || c == '?' ||
            c == '+' || c == '&' || c == '^' || c == '|' ||
            c == '(' || c == ')' || c == '{' || c == '}' || c == '[' ||
            c == ']' || c == '#'); 
  } 
  // { return (c == '<' || c == '>' || c == '='); } 

  private static boolean isXMLBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || c == '$' || c == '@' || c == ':'); } 
        
  private static boolean isSymbolCharacterText(char c)
  { return (c == '(' || c == ')' || 
            c == '{' || c == '}' || c == '[' ||
            c == ']'); 
  } 

  private static boolean isSymbolCharacterAST(char c)
  { return (c == '(' || c == ')'); } 
  
  public void nospacelexicalanalysis(String str) 
  { int in = INUNKNOWN; 
    char previous = ' '; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    char prev = ' '; 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 
      if (in == INUNKNOWN) 
      { if (isSymbolCharacter(c))
        { sb = new StringBuffer();  // new buffer for symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (isBasicExpCharacter(c))
        { sb = new StringBuffer();  // new buffer for expression
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }           
        else if (isStringDelimiter(c))
        { sb = new StringBuffer();  // new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append('"'); 
        }
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') 
        { } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          sb.append(c); 
          if (c == '_') 
          { // System.out.println("Metavariable symbol: " + c); 
            in = INBASICEXP; 
          } 
          else
          { System.err.println("!! Unrecognised UML/OCL token: " + c); } 
        }
      } 
      else if (in == INBASICEXP)
      { if (isBasicExpCharacter(c) || 
            c == '"')           // Why allow " in a basic exp???
        { sb.append(c); }       // carry on adding to current basic exp
        else if (c == '_') 
        { // System.out.println("Metavariable symbol: " + c); 
          sb.append(c); 
        } 
        else if (isSymbolCharacter(c))
        { sb = new StringBuffer();     // new buffer for symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
        { in = INUNKNOWN; } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          in = INUNKNOWN; 
          System.err.println("!! Unrecognised token in expression: " + c); 
          sb.append(c); 
        }
      }
      else if (in == INSYMBOL)
      { if (isStringDelimiter(c))
        { sb = new StringBuffer();  // new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append('"'); 
        }
        else if (c == '(' || c == ')')
        { sb = new StringBuffer();  // new buffer for new symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          previous = c; 
          sb.append(c); 
        }
        else if (c == '_') 
        { // System.out.println("Metavariable symbol: " + c); 
          sb = new StringBuffer();  // new buffer for the string
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        } 
        else if (isBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for basic exp
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }
        else if (isSymbolCharacter(c))
        { if (validFollowingCharacter(previous,c))
          { sb.append(c); } 
          else 
          { sb = new StringBuffer();     // start new buffer for the new symbol
            lexicals.addElement(sb);  
            in = INSYMBOL; 
            sb.append(c); 
          }
          previous = c; 
        } 
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
        { in = INUNKNOWN; } 
      }
      else if (in == INSTRING) 
      { if (isStringDelimiter(c) && prev != '\\')  /* end of string */ 
        { sb.append('"'); 
          in = INUNKNOWN; 
        } 
        else 
        { sb.append(c); } 
      }    
      previous = c; 
      prev = c; 
    }

    int llen = lexicals.size(); 
    bcount = new int[llen]; 
    int bnest = 0; 
 
    for (int i = 0; i < llen; i++) 
    { StringBuffer sbb = (StringBuffer) lexicals.get(i); 
      lexs.add(sbb + "");
      bcount[i] = bnest; 
      if ("(".equals(sbb + "")) 
      { bnest++; }  
      else if (")".equals(sbb + ""))
      { bnest--; } 
      else if (Expression.operators.contains(sbb + ""))
      { 
        addToOps(new OpOccurrence(sbb + "",i,bnest)); 
      } 
      else if ("and".equals(sbb + ""))
      { addToOps(new OpOccurrence("&",i,bnest));
        sbb = new StringBuffer("&"); 
      } 
      else if ("implies".equals(sbb + ""))
      { addToOps(new OpOccurrence("=>",i,bnest));
        sbb = new StringBuffer("=>"); 
      } 
    }   

    // System.out.println(">>> Operator occurrences: " + ops);        
  }


  public void copyLexicals(Vector lexics)
  { lexicals = new Vector(); 
    lexicals.addAll(lexics); 

    int llen = lexicals.size(); 
    bcount = new int[llen]; 
    int bnest = 0; 
 
    for (int i = 0; i < llen; i++) 
    { StringBuffer sbb = (StringBuffer) lexicals.get(i); 
      lexs.add(sbb + "");
      bcount[i] = bnest; 
      if ("(".equals(sbb + "")) 
      { bnest++; }  
      else if (")".equals(sbb + ""))
      { bnest--; } 
      else if (Expression.operators.contains(sbb + ""))
      { 
        addToOps(new OpOccurrence(sbb + "",i,bnest)); 
      } 
      else if ("and".equals(sbb + ""))
      { addToOps(new OpOccurrence("&",i,bnest));
        sbb = new StringBuffer("&"); 
      } 
      else if ("implies".equals(sbb + ""))
      { addToOps(new OpOccurrence("=>",i,bnest));
        sbb = new StringBuffer("=>"); 
      } 
    }   

    // System.out.println(">>> Operator occurrences: " + ops);        
  }

  // Updated (November 2012) to handle XML: 

  public void nospacelexicalanalysisxml(String str) 
  { int in = INUNKNOWN; 
    char previous = ' '; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 
      if (in == INUNKNOWN) 
      { if (isXMLSymbolCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (isXMLBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the expression
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }           
        else if (c == '"')
        { sb = new StringBuffer();     // start new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append(c); 
        }
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') 
        { } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          System.err.println("Unrecognised literal: " + c); 
          sb.append(c); 
        }
      } 
      else if (in == INBASICEXP)
      { if (isXMLBasicExpCharacter(c) || c == '"')
        { sb.append(c); }              // carry on adding to current basic exp
        else if (isXMLSymbolCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
        { in = INUNKNOWN; } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          in = INUNKNOWN; 
          System.err.println("Unrecognised literal in expression: " + c); 
          sb.append(c); 
        }
      }
      else if (in == INSYMBOL)
      { if (c == '"')
        { sb = new StringBuffer();     // start new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append(c); 
        }
        else if (c == '(' || c == ')')
        { sb = new StringBuffer();     // start new buffer for the new symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          previous = c; 
          sb.append(c); 
        }
        else if (isXMLBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for basic exp
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }
        else if (isXMLSymbolCharacter(c))
        { if (validFollowingCharacterXML(previous,c))
          { sb.append(c); } 
          else 
          { sb = new StringBuffer();     // start new buffer for the new symbol
            lexicals.addElement(sb);  
            in = INSYMBOL; 
            sb.append(c); 
          }
          previous = c; 
        } 
        else if (c == ' ' || c == '\n' || c == '\t' || c == '\r')
        { in = INUNKNOWN; } 
      }
      else if (in == INSTRING) 
      { if (c == '"')  /* end of string */ 
        { sb.append(c); 
          in = INUNKNOWN; 
        } 
        else 
        { sb.append(c); } 
      }    
      previous = c; 
    }
  }

  /* For text from NLP: */ 
  
  public void nospacelexicalanalysisText(String str) 
  { int in = INUNKNOWN; 
    char previous = ' '; 

    boolean instring = false; 
    boolean inchar = false; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;         /* Current lexical item */ 

    char prev = ' '; 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 

      if (c == '\"' && prev != '\\') 
      { if (inchar) // '"' is ok but should be '\"' 
        { if (sb != null) 
          { sb.append("\""); }
        }  
        else if (instring) 
        { instring = false; 
          if (sb != null) 
          { sb.append(c); 
            sb = null; 
          } // ends a literal string. 
        }  
        else // starts a literal string. 
        { instring = true; 
          sb = new StringBuffer();     // start new buffer
          lexicals.addElement(sb);  
          sb.append(c); 
        } 
        prev = updatePrev(prev,c); 
      } 
      else if (instring)
      { if (sb != null) // should always be true. 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();     // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c); 
      } 
      else if (c == '\'' && prev != '\\') 
      { if (inchar) 
        { inchar = false; 
          if (sb != null) 
          { sb.append(c); 
            sb = null; 
          } // ends a literal char string. 
        }  
        else // starts a literal char string. 
        { inchar = true; 
          sb = new StringBuffer();     // start new buffer
          lexicals.addElement(sb);  
          sb.append(c); 
        } 
        prev = updatePrev(prev,c); 
      } 
      else if (inchar)
      { if (sb != null) // should always be true. 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();  // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c); 
      } 
      else if (isSymbolCharacterText(c)) // && !instring)
      { sb = new StringBuffer();     // start new buffer for the symbol
        lexicals.addElement(sb);  
        sb.append(c); 
        sb = null; 
        prev = updatePrev(prev,c); 
      }        
      else if (c == ' ' || c == '\n' || 
               c == '\t' || c == '\r') 
      { sb = null; 
        prev = updatePrev(prev,c); 
      } // end current buffer - not in a string 
      else // if (isBasicExpCharacter(c))
      { if (sb != null) 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();     // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c);            
      } 
    }
  }

  public void filterLexicals()
  { // removes invalid sequences such as ; ; and ; )

    Vector newlexicals = new Vector(); 
    boolean previousSemi = false; // last token was ';'
    StringBuffer semi = new StringBuffer(); 
    semi.append(";"); 

    for (int i = 0; i < lexicals.size(); i++) 
    { StringBuffer sb = (StringBuffer) lexicals.get(i); 
      String ss = sb + ""; 
      if (";".equals(ss))
      { if (previousSemi) { } // skip the token
        else 
        { previousSemi = true; } 
      } 
      else if (")".equals(ss))
      { if (previousSemi) 
        { previousSemi = false; 
          newlexicals.add(sb); 
        } 
        else 
        { newlexicals.add(sb); } 
      } 
      else if ("else".equals(ss))
      { if (previousSemi) 
        { previousSemi = false; 
          newlexicals.add(sb); 
        } 
        else 
        { newlexicals.add(sb); } 
      } 
      else if (previousSemi)
      { newlexicals.add(semi); 
        previousSemi = false; 
        newlexicals.add(sb); 
      } 
      else 
      { newlexicals.add(sb); } 
    } 

    lexicals = newlexicals; 
  }

  public void nospacelexicalanalysisAST(String str) 
  { int in = INUNKNOWN; 
    char previous = ' '; 

    boolean instring = false; 
    boolean inchar = false; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;         /* Current lexical item */ 

    char prev = ' '; 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 

      if (c == '\"' && prev != '\\') 
      { if (inchar) // '"' is ok but should be '\"' 
        { if (sb != null) 
          { sb.append("\""); }
        }  
        else if (instring) 
        { instring = false; 
          if (sb != null) 
          { sb.append(c); 
            sb = null; 
          } // ends a literal string. 
        }  
        else // starts a literal string. 
        { instring = true; 
          sb = new StringBuffer();     // start new buffer
          lexicals.addElement(sb);  
          sb.append(c); 
        } 
        prev = updatePrev(prev,c); 
      } 
      else if (instring)
      { if (sb != null) // should always be true. 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();     // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c); 
      } 
      else if (c == '\'' && prev != '\\') 
      { if (inchar) 
        { inchar = false; 
          if (sb != null) 
          { sb.append(c); 
            sb = null; 
          } // ends a literal char string. 
        }  
        else // starts a literal char string. 
        { inchar = true; 
          sb = new StringBuffer();     // start new buffer
          lexicals.addElement(sb);  
          sb.append(c); 
        } 
        prev = updatePrev(prev,c); 
      } 
      else if (inchar)
      { if (sb != null) // should always be true. 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();  // new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c); 
      } 
      else if (isSymbolCharacterAST(c)) // && !instring)
      { sb = new StringBuffer();   // new buffer for symbol
        lexicals.addElement(sb);  
        sb.append(c); 
        sb = null; 
        prev = updatePrev(prev,c); 
      }        
      else if (c == ' ' || c == '\n' || 
               c == '\t' || c == '\r') 
      { sb = null; 
        prev = updatePrev(prev,c); 
      } // end current buffer - not in a string 
      else // if (isBasicExpCharacter(c))
      { if (sb != null) 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();     // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }
        prev = updatePrev(prev,c);            
      } 
    }
  }

  private char updatePrev(char prev, char current)
  { if (prev == '\\' && current == '\\') 
    { return ' '; } /* disregard an escaped backslash */ 
    else 
    { return current; } 
  } 

  public void nospacelexicalanalysisSimpleText(String str) 
  { int in = INUNKNOWN; 
    char previous = ' '; 

    // boolean instring = false; 
    // boolean inchar = false; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    char prev = ' '; 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 

      if (isSymbolCharacterText(c))
      { sb = new StringBuffer();  // start new buffer for the symbol
        lexicals.addElement(sb);  
        sb.append(c); 
        sb = null; 
      }        
      else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') 
      { sb = null; } // end current buffer - not in a string 
      else 
      { if (sb != null) 
        { sb.append(c); } 
        else 
        { sb = new StringBuffer();     // start new buffer for the text
          lexicals.add(sb); 
          sb.append(c); 
        }           
      } 
    }
  }

  private static boolean validFollowingCharacter(char c1, char c2)
  { if (c1 == '<') 
    { if (c2 == '=' || c2 == ':' || c2 == '>')  // final case for ATL, treated as /=  
      { return true; }   /* /<: should be ok, but not </, very strange! */ 
      return false; 
    }
    if (c1 == '>')
    { if (c2 == '=') { return true; } 
      return false; 
    }
    if (c1 == '/') 
    { if (c2 == '=' || c2 == ':' || c2 == '\\' || c2 == '<') { return true; } 
      return false; 
    }
    if (c1 == '\\')
    { if (c2 == '/') { return true; } 
      return false; 
    }
    if (c1 == '=')
    { if (c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == '-')
    { if (c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == ':')
    { if (c2 == '=') { return true; } 
      return false; 
    }
    return false; 
  } 

  private static boolean validFollowingCharacterXML(char c1, char c2)
  { if (c1 == '<') 
    { if (c2 == '=' || c2 == ':' || c2 == '/') 
      { return true; }   
      return false; 
    }
    if (c1 == '>')
    { if (c2 == '=') { return true; } 
      return false; 
    }
    if (c1 == '/') 
    { if (c2 == '=' || c2 == ':' || c2 == '\\' || c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == '\\')
    { if (c2 == '/') { return true; } 
      return false; 
    }
    if (c1 == '=')
    { if (c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == '-')
    { if (c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == ':')
    { if (c2 == '=') { return true; } 
      return false; 
    }
    return false; 
  } 

 
   

  public void displaylexs()
  { 
    for (int i = 0; i < lexicals.size(); i++)
    { 
      System.out.println(((StringBuffer) lexicals.elementAt(i)).toString()); 
    } 
  }

  public void displaylexs(int a, int b)
  { 
    for (int i = a; i < lexicals.size() & i <= b; i++)
    { 
      System.out.println(((StringBuffer) lexicals.elementAt(i)).toString()); 
    } 
  }

  public Type parseType() 
  { int en = lexicals.size();
    return parseType(0, en-1, new Vector(), new Vector()); 
  } 

  public Type parseType(Vector entities, Vector types) 
  { int en = lexicals.size();
    return parseType(0,en-1,entities,types); 
  } 

  public Type parseType(int st, int en, Vector entities, Vector types) 
  { if (st > en) { return null; }
 
    String typ = lexicals.get(st) + ""; 

    if (st == en && 
        ("int".equals(typ) || "long".equals(typ) || 
         typ.equals("String") || 
         "Integer".equals(typ) || "Real".equals(typ) || 
         typ.equals("Boolean") || 
         "void".equals(typ) || typ.equals("double") || 
         typ.equals("boolean") ||
         "OclAny".equals(typ) || "OclVoid".equals(typ) ||
         "OclType".equals(typ) || "OclDate".equals(typ) ||
         "OclAttribute".equals(typ) || 
         "OclOperation".equals(typ) ||
         "OclDatasource".equals(typ) ||
         "SQLStatement".equals(typ) ||
         "OclIterator".equals(typ) || 
         "OclFile".equals(typ) || "OclRandom".equals(typ) ||
         "OclProcess".equals(typ) || 
         "OclException".equals(typ) ||  
         Type.isOCLExceptionType(typ) ||  
         typ.startsWith("_")))
    { // System.out.println("Creating new basic type " + typ);
      return new Type(typ,null);
    }
    else if ("Map".equals(typ) || "Function".equals(typ)) 
    { Type tt = null; 
      if (st == en) 
      { System.err.println("!! Warning, map/function types must have type parameters"); 
        tt = new Type(typ, null);
        tt.setKeyType(new Type("OclAny", null)); 
        tt.setElementType(new Type("OclAny", null)); 
        return tt;  
      } 

      if (st+1 <= en-1) 
      { String ob = lexicals.get(st+1) + ""; 
        String cb = lexicals.get(en) + ""; 
        if ("(".equals(ob) && ")".equals(cb))
        { for (int i = st + 2; i < en; i++) 
          { String ss = lexicals.get(i) + ""; 
            if (ss.equals(","))
            { Type t1 = parseType(st+2,i-1,entities,types); 
              Type t2 = parseType(i+1,en-1,entities,types);
              if (t1 != null && t2 != null) 
              { tt = new Type(typ,t1,t2);
                tt.setKeyType(t1);  
                tt.setElementType(t2);
                System.out.println("******* Parsed " + typ + " type: " + tt);  
                return tt;
              } 
              else if (t1 == null) 
              { System.err.println("!! Invalid type at: " + showLexicals(st+2,i-1)); } 
              else if (t2 == null) 
              { System.err.println("!! Invalid type at: " + showLexicals(i+1,en-1)); } 
            }
          } 
        }
      } 

      if (tt == null) 
      { System.err.println("!!ERROR!!: Invalid map/function type, it must have 2 type arguments: " + showLexicals(st,en)); 
        tt = new Type(typ, null);
        tt.setKeyType(new Type("OclAny", null)); 
        tt.setElementType(new Type("OclAny", null)); 
        return tt;  
      }
      else 
      { return tt; } 
    }    
    else if ("SortedMap".equals(typ)) 
    { Type tt = null; 
      if (st == en) 
      { System.err.println("!! Warning, map types must have type parameters"); 
        tt = new Type("Map", null);
        tt.setSorted(true); 
        tt.setKeyType(new Type("OclAny", null)); 
        tt.setElementType(new Type("OclAny", null)); 
        return tt;  
      } 

      if (st+1 <= en-1) 
      { String ob = lexicals.get(st+1) + ""; 
        String cb = lexicals.get(en) + ""; 
        if ("(".equals(ob) && ")".equals(cb))
        { for (int i = st + 2; i < en; i++) 
          { String ss = lexicals.get(i) + ""; 
            if (ss.equals(","))
            { Type t1 = parseType(st+2,i-1,entities,types); 
              Type t2 = parseType(i+1,en-1,entities,types);
              if (t1 != null && t2 != null) 
              { tt = new Type("Map", t1, t2);
                tt.setSorted(true); 
                tt.setKeyType(t1);  
                tt.setElementType(t2);
                System.out.println("******* Parsed " + typ + " type: " + tt);  
                return tt;
              } 
              else if (t1 == null) 
              { System.err.println("!! Invalid type at: " + showLexicals(st+2,i-1)); } 
              else if (t2 == null) 
              { System.err.println("!! Invalid type at: " + showLexicals(i+1,en-1)); } 
            }
          } 
        }
      } 

      if (tt == null) 
      { System.err.println("!!ERROR!!: Invalid map type, it must have 2 type arguments: " + showLexicals(st,en)); 
        tt = new Type("Map", null);
        tt.setSorted(true); 
        tt.setKeyType(new Type("OclAny", null)); 
        tt.setElementType(new Type("OclAny", null)); 
        return tt;  
      }
      else 
      { return tt; } 
    }    
    else if (typ.equals("Set") || typ.equals("Sequence") ||
             "Ref".equals(typ))
    { Type tt = new Type(typ,null);
      if (st == en) 
      { return tt; } 
 
      if (st + 2 <= en - 1) 
      { String ob = lexicals.get(st+1) + ""; 
        String cb = lexicals.get(en) + ""; 
        if ("(".equals(ob) && ")".equals(cb))
        { Type et = parseType(st + 2, en - 1, entities, types); 
          if (et != null && tt != null) 
          { tt.setElementType(et); 
            return tt; 
          }
          else 
          { System.err.println("!! Error: Unknown type in Set/Sequence argument: " + showLexicals(st+2,en-1)); } 
        } 
      }  
      // return tt; 
    } 
    else if (typ.equals("SortedSet") || 
             typ.equals("SortedSequence"))
    { Type tt = new Type(typ.substring(6), null);
      tt.setSorted(true); 

      if (st == en) 
      { return tt; } 
 
      if (st + 2 <= en - 1) 
      { String ob = lexicals.get(st+1) + ""; 
        String cb = lexicals.get(en) + ""; 
        if ("(".equals(ob) && ")".equals(cb))
        { Type et = parseType(st + 2, en - 1, entities, types); 
          if (et != null && tt != null) 
          { tt.setElementType(et); 
            return tt; 
          }
          else 
          { System.err.println("!! Error: Unknown type in Set/Sequence argument: " + showLexicals(st+2,en-1)); } 
        } 
      }  
      // return tt; 
    } 
    else
    { Object ee = ModelElement.lookupByName(typ,entities);
      Type resulttype = null; 
      if (ee != null && ee instanceof Entity) 
      { resulttype = new Type((Entity) ee); 
        if (st == en)
        { return resulttype; }
        if (st < en && "<".equals(lexicals.get(st+1) + "") &&
            ">".equals(lexicals.get(en) + ""))
        { Vector args = 
            parse_type_sequence(st+2,en-1,entities,types); 
          if (args != null) 
          { ((Entity) ee).setTypeParameters(args);
            // Create a new instantiation copy.  
            return resulttype; 
          } 
        } 
      }  
      else 
      { Object tt = ModelElement.lookupByName(typ,types); 
        if (tt != null && tt instanceof Type)
        { return (Type) tt; }
        if (tt != null)
        { return new Type(tt + "", null); } 
      } // should not have parameters.  
    } 

    System.err.println("!! ERROR: unknown type: " + typ + " in " + showLexicals(st,en)); 
        
    return null; 
  } // also need OrderedSet, Bag, Tuple for ATL. 

  public Sbvrse parseSbvrse(int st, int en) 
  { if (st > en) { return null; } 
    if (lexicals.size() < 10) { return null; } 
    Sbvrse sbvrse = new Sbvrse(); 

    String st1 = lexicals.get(0) + ""; 
    String st2 = lexicals.get(1) + ""; 
    String st3 = lexicals.get(2) + ""; 
    String st4 = lexicals.get(3) + ""; 
    String st5 = lexicals.get(4) + ""; 
    if (("It".equals(st1) || "it".equals(st1)) && "is".equals(st2) && 
        "necessary".equals(st3) && "that".equals(st4) && "each".equals(st5))
    { String definedTerm = ""; 
      int i = 5; 
      for ( ; i < lexicals.size(); i++) 
      { String str = lexicals.get(i) + ""; 
        if ("instance".equals(str))
        { // text between 5 and i-1 is a defined term  
          sbvrse.setDefinedTerm(definedTerm);
          break;  
        } 
        else 
        { definedTerm = definedTerm + str; } 
      } 

      if (i+9 < lexicals.size())
      { String idsource = lexicals.get(i+1) + ""; 
        sbvrse.setSourceInstance(idsource); 
        String st6 = lexicals.get(i+2) + "";  // maps
        String st7 = lexicals.get(i+3) + "";  // to
        String st8 = lexicals.get(i+4) + "";  // a or an
        String ttype = lexicals.get(i+5) + ""; 
        sbvrse.setTargetType(ttype); 
        String tid = lexicals.get(i+7) + ""; 
        sbvrse.setTargetInstance(tid);
        Expression exp1 = parse_expression(0, i+10, en, null, null); 
        sbvrse.setSuccedent(exp1);     
      } 
    } 
    return sbvrse; 
  } 

  public Sbvrse parseSbvrse()
  { int en1 = lexicals.size()-1; 
    for (int i = 0; i < lexicals.size(); i++) 
    { String lex = lexicals.get(i) + ""; 
      if (";".equals(lex.trim()))
      { System.out.println("+++++++++++"); 
        Sbvrse res = parseSbvrse(0,i-1); 
        parseSbvrse2(res,i+1,en1); 
        return res; 
      }
    } 
    return parseSbvrse(0,en1); 
  }
        
  public void parseSbvrse2(Sbvrse sb, int st, int en) 
  { if (st > en) { return; } 
    
    if ("Each".equals(lexicals.get(st) + "") && st + 5 < en)
    { String srcType = lexicals.get(st+1) + ""; 
      sb.setSourceType(srcType);  
      String st2 = lexicals.get(st + 2) + ""; 
      String st3 = lexicals.get(st + 3) + ""; 
      String st4 = lexicals.get(st + 4) + ""; 
      String st5 = lexicals.get(st + 5) + ""; 
      if ("is".equals(st2) && "considered".equals(st3) && 
          "to".equals(st4) && "be".equals(st5))
      { String definedTerm = ""; 
        int i = st + 6; 
        for ( ; i < lexicals.size(); i++) 
        { String str = lexicals.get(i) + ""; 
          if ("if".equals(str))
          { // text between st + 6 and i-1 is the defined term  
            // sbvrse.setDefinedTerm(definedTerm);
            break;  
          } 
          else 
          { definedTerm = definedTerm + str; } 
        } 

        if (i+2 < lexicals.size())
        { String st7 = lexicals.get(i+1) + "";  // it
          String st8 = lexicals.get(i+2) + "";  // has
          Expression exp1 = parse_expression(0, i+3, en, null, null); 
          sb.setCondition(exp1);     
        } 
      }
    } 
  } // should pair up the defined terms uses and definitions. 

  public Expression parse(JTextArea messageArea) 
  { messageArea.append("LEXICALS: " + lexicals + "\n\r"); 
    Vector env = new Vector(); 
    Expression ee = parse_expression(0,0,lexicals.size()-1,env,env); 
    finalexpression = ee;
    if (ee == null) 
    { messageArea.append("!! Not a valid OCL constraint. Trying to parse as an operation.\n\r"); } 
    else 
    { messageArea.append("This is a valid OCL expression for a constraint.\n\r"); }  
    return ee; 
  } 

  public CGRule parse_ExpressionCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 
        Expression lhsexp = parseExpression(); 
        if (lhsexp == null) 
        { lhsexp = parseATLExpression(); }

        if (lhsexp != null) 
        { if (lhsexp instanceof BinaryExpression)
          { BinaryExpression bexp = (BinaryExpression) lhsexp; 
            if (bexp.operator.equals("/=")) 
            { bexp.operator = "<>"; } 
          } 
			
          String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhsexp,rhstext,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhsexp,rhs); 
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 

  public CGRule parse_UnaryExpressionCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 
        Expression lhsexp = parseExpression();
        if (lhsexp == null)
        { lhsexp = parseLambdaExpression(); } 

        if (lhsexp != null) 
        { if (lhsexp instanceof BinaryExpression)
          { BinaryExpression bexp = (BinaryExpression) lhsexp; 
            if (bexp.operator.equals("/=")) 
            { bexp.operator = "<>"; } 
          } 
		
          String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhsexp,rhstext,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhsexp,rhs); 
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 


  public CGRule parse_StatementCodegenerationrule(String rule, Vector entities, Vector types)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 
        // Statement lhsexp = parseStatement(entities,types); 
        Vector variables = new Vector(); 
        for (int k = 0; k < lexicals.size(); k++) 
        { String lex = lexicals.get(k) + ""; 
          if (lex.startsWith("_"))
          { variables.add(lex); } 
        } 

        System.out.println("LHS variables = " + variables); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              // CGRule r = new CGRule(lhsexp,rhstext,conds); 
              return r; 
            } 
          } 
          // CGRule res = new CGRule(lhsexp,rhs); 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 

  public CGRule parse_UseCaseCodegenerationrule(String rule, Vector entities, Vector types)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 
        // Statement lhsexp = parseStatement(entities,types); 
        Vector variables = new Vector(); 
        for (int k = 0; k < lexicals.size(); k++) 
        { String lex = lexicals.get(k) + ""; 
          if (lex.startsWith("_"))
          { variables.add(lex); } 
        } 

        System.out.println(">> LHS variables = " + variables); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              // CGRule r = new CGRule(lhsexp,rhstext,conds); 
              return r; 
            } 
          } 
          // CGRule res = new CGRule(lhsexp,rhs); 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 

  public CGRule parse_TypeCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 
        Type lhsexp = parseType(new Vector(),new Vector()); 
        System.out.println(">> LHS of type rule = " + lhsexp); 

        if (lhsexp != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhsexp,rhstext,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhsexp,rhs);

          System.out.println("Variables of rule " + res + " are: " + res.variables);  
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 

  public CGRule parse_EntityCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 

        Vector variables = new Vector(); 
        for (int k = 0; k < lexicals.size(); k++) 
        { String lex = lexicals.get(k) + ""; 
          if (lex.startsWith("_"))
          { variables.add(lex); } 
        } 

        // System.out.println("LHS variables = " + variables); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          return res; 
        } 
      } 
    } 
    System.err.println(">>> Invalid rule: " + rule); 
    return null; 
  } 

  // For the following, assume broken into tokens by: 
  //
  // nospacelexicalanalysisText(xstring);
  // int sz = lexicals.size();  
    
  public CGRule parse_TextCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && 
          rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        // nospacelexicalanalysisText(lhs);
        nospacelexicalanalysisAST(lhs);
        int sz = lexicals.size();

        Vector variables = new Vector();
        Vector tokens = new Vector(); 
 
        /* for (int k = 0; k + 1 < lhs.length(); k++) 
        { char lex = lhs.charAt(k); 
          if (lex == '_')
          { variables.add("_" + lhs.charAt(k+1)); } 
        } */ 

        for (int k = 0; k < sz; k++) 
        { String lex = lexicals.get(k) + ""; 
          if (CSTL.isCSTLVariable(lex))
          { variables.add(lex); } 
          tokens.add(lex); 
        } 

        System.out.println(">> Text rule " + rule + " LHS variables = " + variables); 
        System.out.println(">> Text rule " + rule + " LHS tokens = " + tokens); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          System.out.println(">> Text rule " + rule + " RHS = " + rhs);
 
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { // Could be actions also 

              for (int kk = j+6; kk < rhs.length(); kk++) 
              { char e = rhs.charAt(kk); 
                if (e == '<' && j+7 < rhs.length() &&
                    rhs.charAt(kk+1) == 'a' &&
                    rhs.charAt(kk+2) == 'c' &&
                    rhs.charAt(kk+3) == 't' &&
                    rhs.charAt(kk+4) == 'i' &&
                    rhs.charAt(kk+5) == 'o' &&
                    rhs.charAt(kk+6) == 'n' &&
                    rhs.charAt(kk+7) == '>') 
                { String actions = 
                     rhs.substring(kk+8,rhs.length()); 
                  Vector acts = parse_rule_actions(actions); 
                  System.out.println(">> Rule actions are: " + actions + " " + acts); 
                  String rhstext = rhs.substring(0,j); 
                  String conditions = rhs.substring(j+6,kk); 
                  Vector conds = parse_conditions(conditions); 
                  CGRule res = 
                    new CGRule(lhs,rhstext,
                               variables,conds); 
                  res.setLHSTokens(tokens); 
                  res.setActions(acts);
                  System.out.println(">***> Rule with condition: " + conds + " and actions: " + acts); 
                  System.out.println(">***> Rule variables are: " + res.variables); 
                  System.out.println(">***> Rule metafeatures are: " + res.metafeatures); 
 
                  return res; 
                } 
              }  

              String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              r.setLHSTokens(tokens);
              System.out.println(">***> Rule with condition: " + conds); 
              System.out.println(">***> Rule variables are: " + r.variables); 
              System.out.println(">***> Rule metafeatures are: " + r.metafeatures); 
 
              return r; 
            } 
            else if (d == '<' && j+7 < rhs.length() &&
                rhs.charAt(j+1) == 'a' &&
                rhs.charAt(j+2) == 'c' &&
                rhs.charAt(j+3) == 't' &&
                rhs.charAt(j+4) == 'i' &&
                rhs.charAt(j+5) == 'o' &&
                rhs.charAt(j+6) == 'n' &&
                rhs.charAt(j+7) == '>') 
            { String actions = rhs.substring(j+8,rhs.length()); 
              Vector acts = parse_rule_actions(actions); 
              System.out.println(">> Rule actions are: " + actions + " " + acts); 
              String rhstext = rhs.substring(0,j); 
              CGRule res = 
                new CGRule(lhs,rhstext,
                           variables,new Vector()); 
              res.setLHSTokens(tokens); 
              res.setActions(acts); 

              System.out.println(">***> Rule variables are: " + res.variables); 
              System.out.println(">***> Rule metafeatures are: " + res.metafeatures); 
 
              return res; 
            }               
          } 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          res.setLHSTokens(tokens); 
 
          System.out.println(">***> Rule variables are: " + res.variables); 
          System.out.println(">***> Rule metafeatures are: " + res.metafeatures); 
 
          return res; 
        } 
      } 
    } 
    System.err.println("!! Invalid rule: " + rule); 
    return null; 
  } 
  
  public CGRule parse_AttributeCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 

        Vector variables = new Vector(); 
        for (int k = 0; k < lexicals.size(); k++) 
        { String lex = lexicals.get(k) + ""; 
          if (lex.startsWith("_"))
          { variables.add(lex); } 
        } 

        System.out.println(">> LHS variables = " + variables); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          return res; 
        } 
      } 
    } 
    System.err.println("!! Invalid rule: " + rule); 
    return null; 
  } 

  public CGRule parse_OperationCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        nospacelexicalanalysis(lhs); 

        Vector variables = new Vector(); 
        for (int k = 0; k < lexicals.size(); k++) 
        { String lex = lexicals.get(k) + ""; 
          if (lex.startsWith("_"))
          { variables.add(lex); } 
        } 

        System.out.println(">>> LHS variables = " + variables); 

        if (lhs != null) 
        { String rhs = rule.substring(i+4,rule.length());
          for (int j = 0; j < rhs.length(); j++) 
          { char d = rhs.charAt(j); 
            if (d == '<' && j+5 < rhs.length() &&
                rhs.charAt(j+1) == 'w' &&
                rhs.charAt(j+2) == 'h' &&
                rhs.charAt(j+3) == 'e' &&
                rhs.charAt(j+4) == 'n' &&
                rhs.charAt(j+5) == '>') 
            { String conditions = rhs.substring(j+6,rhs.length()); 
              Vector conds = parse_conditions(conditions); 
              String rhstext = rhs.substring(0,j); 
              CGRule r = new CGRule(lhs,rhstext,variables,conds); 
              return r; 
            } 
          } 
          CGRule res = new CGRule(lhs,rhs,variables,new Vector()); 
          return res; 
        } 
      } 
    } 

    System.err.println("!! Invalid rule: " + rule); 
    return null; 
  } 

  // For CSTL rule conditions and actions: 
  public static Vector parse_conditions(String str)
  { Vector conds = new Vector();
    Compiler2 newc = new Compiler2(); 
    newc.nospacelexicalanalysis(str);
    Vector lexs = newc.lexicals;
	
    System.out.println("+++ Condition lexicals:: " + lexs); 
    // _v id 
    // _v ` f id
    // _v not id
    // _v id ` g
    // _* any id
    // _* all id
    // etc

    CGCondition cg = new CGCondition(); 
	 
    for (int i = 0; i < lexs.size(); i++)
    { String se = lexs.get(i) + ""; 
      // System.out.println("+++ Lexical:: " + se + " " + cg.isMatches); 

      if (",".equals(se))
      { conds.add(cg); 
        cg = new CGCondition(); 
      } 
      else if (se.equals("_") && i + 1 < lexs.size() && 
               "*".equals(lexs.get(i+1) + ""))
      { cg.setVariable("_*"); 
        i++; 
      } 
      else if (se.equals("_") && i + 1 < lexs.size() && 
               "+".equals(lexs.get(i+1) + ""))
      { cg.setVariable("_+"); 
        i++; 
      } 
      else if (se.startsWith("_"))
      { if (cg.hasVariable())
        { if (cg.isMatches)
          { cg.addToStereotype(se); } 
          else 
          { cg.setStereotype(se); }
        }  
        else 
        { cg.setVariable(se); }
      }  
      else if (se.equals("`") && 
               i + 2 < lexs.size())
      { String mt = "" + lexs.get(i+1); 
        cg.setVariableMetafeature(mt);
        i++;
      } // has variable but not stereo
      else if (se.equals("`") && 
               i + 2 == lexs.size())
      { String mt = "" + lexs.get(i+1); 
        cg.setStereotypeMetafeature(mt);
        i++;
      } // has variable and stereo
      else if (se.equals("not"))
      { cg.setNegative(); } 
      else if (se.equals("any"))
      { cg.setExistential(); } 
      else if (se.equals("all"))
      { cg.setUniversal(); } 
      else if (se.equals("matches"))
      { cg.setMatches(); } 
      else if (se.equals("isNested"))
      { cg.setIsNested(true); } 
      else if (cg.isMatches)
      { cg.addToStereotype(se); } 
      else 
      { cg.setStereotype(se); } 
    }
    conds.add(cg); 
    return conds; 
  } // Could be metafeatures: _i`mf value
	
  // For CSTL rule actions: 
  public static Vector parse_rule_actions(String str)
  { // _i v
    // _i not v
    // e / x
    // _* all v
    // _i`f with w

    Vector conds = new Vector();
    Compiler2 newc = new Compiler2(); 
    newc.nospacelexicalanalysis(str);
    Vector lexs = newc.lexicals;
    System.out.println("+++ Action lexicals:: " + lexs); 
	
    CGCondition cg = new CGCondition(); 
	 
    boolean expectVar = true; 
    boolean expectStereo = false; 

    for (int i = 0; i < lexs.size(); i++)
    { String se = lexs.get(i) + ""; 
      if (",".equals(se))
      { conds.add(cg); 
        cg = new CGCondition(); 
        expectVar = true; 
        expectStereo = false; 
      } 
      else if (se.equals("_") && i + 1 < lexs.size() && 
               "*".equals(lexs.get(i+1) + ""))
      { cg.setVariable("_*"); 
        i++; 
        expectVar = false; 
        expectStereo = true;
      } 
      else if (se.equals("not"))
      { cg.setNegative();
        expectVar = false; 
        expectStereo = true;
      } 
      else if (se.equals("/"))
      { cg.setSubstitute();
        expectVar = false; 
        expectStereo = true;
      } 
      else if (se.equals("all"))
      { cg.setUniversal(); 
        expectVar = false; 
        expectStereo = true;
      } 
      else if (se.equals("with"))
      { cg.setIsWith(true); 
        expectVar = false; 
        expectStereo = true;
      } 
      else if (se.equals("`") && 
               i + 1 < lexs.size())
      { String mt = "" + lexs.get(i+1); 
        if (expectStereo) // just seen a variable 
        { cg.setVariableMetafeature(mt); } 
        else if (expectVar) 
        { cg.setStereotypeMetafeature(mt); }
        i++;
      } 
      else if (expectVar)
      { cg.setVariable(se); 
        expectVar = false; 
        expectStereo = true; 
      } 
      else if (expectStereo)
      { cg.setStereotype(se);
        expectVar = true; 
        expectStereo = false; 
      } 

      // System.out.println(se + " " + expectVar + " " + expectStereo + " " + cg); 
    }

    conds.add(cg); 
    return conds; 
  } // Could be metafeatures on both variable, stereotype
	
	
  public Vector parseCGconditions(String str) 
  { Vector conds = new Vector(); 
    StringTokenizer st = 
          new StringTokenizer(str); 
    
    CGCondition cg = new CGCondition(); 

    while (st.hasMoreTokens())
    { String se = st.nextToken().trim();
      if (",".equals(se))
      { conds.add(cg); 
        cg = new CGCondition(); 
      } 
      else if (se.startsWith("_"))
      { cg.setVariable(se); } 
      else if (se.equals("not"))
      { cg.setNegative(); } 
      else 
      { cg.setStereotype(se); } 
    }
    conds.add(cg); 
    return conds; 
  }    
    
  public Expression parseCGRule() 
  { System.out.println("LEXICALS: " + lexicals); 
    Expression ee = parseCGRule(0,0,lexicals.size()-1); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseCGRule(int bcount, int pstart, int pend)
  { // expr with _ |-> expr with _ <when> conditions
    for (int i = pstart; i <= pend; i++) 
    { String lx = lexicals.get(i) + ""; 
      if (lx.equals("|") && i + 1 <= pend && 
          "->".equals(lexicals.get(i+1) + ""))
      { // System.out.println(">>> Deliminter at token " + i); 
        Vector env = new Vector(); 
        Expression lhs = parse_expression(bcount,pstart,i-1,env,env); 
        Expression rhs = null; 
        System.out.println("LHS = " + rhs); 

        for (int j = i+2; j <= pend; j++) 
        { String lxj = lexicals.get(j) + ""; 
          if ("<".equals(lxj) && j+1 < pend && "when".equals(lexicals.get(j+1) + "") && 
              ">".equals(lexicals.get(j+2) + "")) 
          { rhs = parse_expression(bcount,i+2,j-1,env,env); 
            System.out.println("RHS = " + rhs); 
          } 
        } 
        if (rhs == null) // no <when> 
        { rhs = parse_expression(bcount,i+2,pend,env,env); 
          System.out.println("RHS = " + rhs); 
        } 
        return lhs; 
      } 
    } 
    return null; 
  } 

  public Expression parse() 
  { // System.out.println("LEXICALS: " + lexicals);
    Vector env = new Vector();  
    Expression ee = parse_expression(0,0,lexicals.size()-1,env,env); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseExpression() 
  { // System.out.println("LEXICALS: " + lexicals); 
    Vector env = new Vector(); 
    Expression ee = parse_expression(0,0,lexicals.size()-1,env,env); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseExpression(Vector entities, Vector types) 
  { Expression ee = parse_expression(0,0,lexicals.size()-1,entities,types); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseATLExpression() 
  { // System.out.println("LEXICALS: " + lexicals);
    Vector ents = new Vector(); 
    Vector typs = new Vector();  
    Expression ee = parse_ATLexpression(0,0,lexicals.size()-1,ents,typs); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseLambdaExpression() 
  { // System.out.println("LEXICALS: " + lexicals); 
    Vector v1 = new Vector(); 
    Vector v2 = new Vector(); 
    Expression ee = parse_lambda_expression(0,0,lexicals.size()-1,v1,v2); 
    finalexpression = ee; 
    return ee; 
  } 

 /*  public Expression parse_expression(int bcount, int pstart, int pend)
  { Vector ents = new Vector(); 
    Vector typs = new Vector();  
    Expression ee = parse_ATLexpression(bcount,pstart,pend-1,ents,typs); 
    return ee; 
  } */ 


  public Expression parse_expression(int bcount, int pstart, int pend, Vector entities, Vector types)
  { Expression ee = null; 
    
    if ("if".equals(lexicals.get(pstart) + "") && "endif".equals(lexicals.get(pend) + ""))
    { ee = parse_conditional_expression(bcount,pstart,pend,
                                        entities,types); 
      if (ee != null) { return ee; } 
    } 

    // if ("let".equals(lexicals.get(pstart) + "") && "endlet".equals(lexicals.get(pend) + ""))
    // { ee = parse_let_expression(bcount,pstart,pend); 
    //   if (ee != null) { return ee; } 
    // } 

    // if ("lambda".equals(lexicals.get(pstart) + ""))
    // { ee = parse_lambda_expression(bcount,pstart,pend); 
    //   if (ee != null) { return ee; } 
    // } 

    if ("not".equals(lexicals.get(pstart) + ""))
    { ee = parse_expression(bcount,pstart+1,pend,
                            entities,types); 
      if (ee != null) 
      { return new UnaryExpression("not", ee); } 
    } 

    if ("?".equals(lexicals.get(pstart) + ""))
    { ee = parse_basic_expression(bcount,pstart+1,pend,
                                  entities,types); 
      if (ee != null) 
      { return new UnaryExpression("?", ee); } 
    } 
 
    if ("!".equals(lexicals.get(pstart) + ""))
    { ee = parse_basic_expression(bcount,pstart+1,pend,
                                  entities,types); 
      if (ee != null) 
      { return new UnaryExpression("!", ee); } 
    } 
 
    if ("_".equals(lexicals.get(pstart) + "") && pend == pstart+1)
    { ee = parse_expression(bcount,pstart+1,pend,
                            entities,types); 
      if (ee != null) 
      { return new UnaryExpression("_", ee); } 
    } 

    OpOccurrence op = getBestOcc(pstart,pend); 
    if (op == null) // No logical ops here
    { ee = parse_eq_expression(bcount,pstart,pend,
                               entities,types);
      if (ee == null) 
      { ee = parse_factor_expression(bcount,pstart,pend,
                                     entities,types); 
      }
      return ee; 
    }

    if (op.bcount > bcount) // bracketed expression
    { ee = parse_eq_expression(bcount,pstart,pend,
                               entities,types); 
      if (ee == null) 
      { ee = parse_factor_expression(bcount,pstart,pend,
                                     entities,types); 
      }
      return ee; 
    }  
    else 
    { ee = parse_implies_expression(bcount,pstart,pend,
                                    op.location,op.op); 
    }
    return ee;  
  }

  public Expression parse_statement_expression(int bcount, int pstart, int pend, Vector entities, Vector types)
  { Expression ee = null; 
    Compiler2 newc = new Compiler2(); 
    Vector lexics = getLexicals(pstart,pend); 
    newc.copyLexicals(lexics); 
    ee = newc.parseExpression(); 

    if (ee != null) 
    { return ee; } 
    
    if ("if".equals(lexicals.get(pstart) + "") && "endif".equals(lexicals.get(pend) + ""))
    { ee = parse_conditional_expression(bcount,pstart,pend,entities,types); 
      if (ee != null) { return ee; } 
    } 

    // if ("let".equals(lexicals.get(pstart) + "") && "endlet".equals(lexicals.get(pend) + ""))
    // { ee = parse_let_expression(bcount,pstart,pend); 
    //   if (ee != null) { return ee; } 
    // } 

    if ("not".equals(lexicals.get(pstart) + ""))
    { ee = parse_expression(bcount,pstart+1,pend,entities,types); 
      if (ee != null) 
      { return new UnaryExpression("not", ee); } 
    } 

    if ("?".equals(lexicals.get(pstart) + ""))
    { ee = parse_basic_expression(bcount,pstart+1,pend,entities,types); 
      if (ee != null) 
      { return new UnaryExpression("?", ee); } 
    } 

    if ("!".equals(lexicals.get(pstart) + ""))
    { ee = parse_basic_expression(bcount,pstart+1,pend,entities,types); 
      if (ee != null) 
      { return new UnaryExpression("!", ee); } 
    } 
 
    if ("_".equals(lexicals.get(pstart) + "") && pend == pstart+1)
    { ee = parse_expression(bcount,pstart+1,pend,entities,types); 
      if (ee != null) 
      { return new UnaryExpression("_", ee); } 
    } 

    OpOccurrence op = getBestOcc(pstart,pend); 
    if (op == null) // No logical ops here
    { ee = parse_eq_expression(bcount,pstart,pend,entities,types);
      if (ee == null) 
      { ee = parse_factor_expression(bcount,pstart,pend,entities,types); }
      return ee; 
    }
    else 
    { ee = parse_implies_expression(bcount,pstart,pend,
                                    op.location,op.op); 
    }
    return ee;  
  }

  /* public Expression parse_binary_expression(int pstart,
                                                  int pend)
  { 
    Expression ee = parse_implies_expression(pstart,pend);
    if (ee == null) 
    { ee = parse_or_expression(pstart,pend); 
      if (ee == null) 
      { ee = parse_and_expression(pstart,pend); 
        if (ee == null)
        { ee = parse_eq_expression(pstart,pend);
          if (ee == null) 
          { ee = parse_factor_expression(pstart,pend); }
        }
      }
    }
    return ee; 
  }  */   

public Expression parse_conditional_expression(int bc, 
                        int st, int en, 
                        Vector entities, Vector types)
{ // st is "if", en is "endif"
  int ifcount = 1;
  String lxst = lexicals.get(st) + "";
  String lxen = lexicals.get(en) + "";
  if ("if".equals(lxst) && "endif".equals(lxen)) {}
  else { return null; }
  for (int j = st+1; j < en; j++)
  { String lxj = lexicals.get(j) + "";
    if ("if".equals(lxj))
    { ifcount++; }
    else if ("endif".equals(lxj))
    { ifcount--; 
      if (ifcount < 1)
      { System.err.println("!!! Too many endifs in conditional"); 
        return null; 
      }
    }
    else if ("then".equals(lxj) && ifcount == 1)
    { Expression test = parse_expression(bc, st+1, j-1, entities, types);
     if (test == null)
     { System.err.println("!! Error in if test expression"); return null;  }
      int ifcountk = 1;
      for (int k = j+1; k < en; k++)
      { String lxk = lexicals.get(k) + "";
        if ("if".equals(lxk))
        { ifcountk++; }
        else if ("endif".equals(lxk))
        { ifcountk--;  
          if (ifcountk < 1)
          { System.err.println("Too many endifs"); return null; }
        }
        else if ("else".equals(lxk) && ifcountk == 1)
        { Expression ifpart = parse_expression(bc, j+1, k-1, entities, types);
          Expression elsepart = parse_expression(bc, k+1, en-1, entities, types);
          if (ifpart != null && elsepart != null)
          { return new ConditionalExpression(test,ifpart,elsepart); } 
          else 
          { return null; }
        } else { }
      } // k loop
    } else { }
  } // j loop
  return null;
}

public Expression parse_ATLconditional_expression(int bc, int st, int en, Vector entities, Vector types)
{ // st is "if", en is "endif"
  int ifcount = 1;
  String lxst = lexicals.get(st) + "";
  String lxen = lexicals.get(en) + "";
  if ("if".equals(lxst) && "endif".equals(lxen)) {}
  else { return null; }
  for (int j = st+1; j < en; j++)
  { String lxj = lexicals.get(j) + "";
    if ("if".equals(lxj))
    { ifcount++; }
    else if ("endif".equals(lxj))
    { ifcount--; 
      if (ifcount < 1)
      { System.err.println("!! Error: Too many endifs"); 
        return null;
      }
    }
    else if ("then".equals(lxj) && ifcount == 1)
    { Expression test = parse_ATLexpression(bc, st+1, j-1,entities,types);
      if (test == null)
      { System.err.println("!! Error in if test expression"); return null;  }
       int ifcountk = 1;
       for (int k = j+1; k < en; k++)
       { String lxk = lexicals.get(k) + "";
         if ("if".equals(lxk))
         { ifcountk++; }
         else if ("endif".equals(lxk))
         { ifcountk--;  
           if (ifcountk < 1)
           { System.err.println("!! Error: Too many endifs"); return null; }
         }
         else if ("else".equals(lxk) && ifcountk == 1)
         { Expression ifpart = parse_ATLexpression(bc, j+1, k-1,entities,types);
           Expression elsepart = parse_ATLexpression(bc, k+1, en-1, entities,types);
           if (ifpart != null && elsepart != null)
           { return new ConditionalExpression(test,ifpart,elsepart); } else { return null; }
         } else { }
       } // k loop
     } else { }
  } // j loop
  return null;
}

/* Only for ATL, ETL, QVT-R */ 
public Expression parse_let_expression(int bc, int st, int en, Vector entities, Vector types)
{ // let v : T = e in expr
  // a BinaryExpression with operator "let" and accumulator v : T
  // expr should be bracketed for nested lets. 

  int letcount = 1;
  String lxst = lexicals.get(st) + "";
  if ("let".equals(lxst)) {}
  else { return null; }

  for (int j = st+1; j < en; j++)
  { String lxj = lexicals.get(j) + "";
    if ("let".equals(lxj))
    { letcount++; }
    else if ("in".equals(lxj))
    { if (letcount > 1)  // an inner let
      { letcount--; } 
      else if (letcount < 1)
      { System.err.println("!! Too many let in clauses"); 
        return null;
      }
      else 
      { Expression var = parse_ATLexpression(bc, st+1, st+1,entities,types);
        if (var == null)
        { System.err.println("!! Error in let variable"); return null;  }

        for (int k = st+2; k < j; k++)
        { String lxk = lexicals.get(k) + "";
          if ("=".equals(lxk))
          { Type ltype = parseType(st+3,k-1,entities,types);
            if (ltype == null)
            { System.err.println("!! Error in let type"); 
              return null;
            }

            Expression lexp = parse_ATLexpression(bc,k+1,j-1,entities,types);
            if (lexp == null)
            { lexp = parse_expression(bc,k+1,j-1,entities,types); } 
            if (lexp == null)  
            { System.err.println("!! Error in let definition expression"); 
              return null;
            }

            Expression lbody = parse_ATLexpression(bc,j+1,en,entities,types); 
            if (lbody == null)
            { lbody = parse_expression(bc,j+1,en,entities,types); }
            if (lbody == null)
            { System.err.println("!! Error in let body"); }

            if (ltype != null && lexp != null && lbody != null) 
            { BinaryExpression letexp = new BinaryExpression("let", lexp, lbody); 
              letexp.accumulator = new Attribute(var + "", ltype, ModelElement.INTERNAL); 
              letexp.accumulator.setInitialExpression(lexp);
              letexp.accumulator.setElementType(
                                    ltype.getElementType());  

              System.out.println(">>> Parsed let expression " + letexp + " " + letexp.accumulator.getElementType()); 

              return letexp; 
              // return new LetExpression(var,ltype,lexp,lbody); 
            } 
          }
        } // k loop 
      }
    } else { }  
  } // j loop
  return null;
} 

/* Only for ATL, ETL, QVT-R */ 
public Expression parse_lambda_expression(int bc, int st, int en, Vector entities, Vector types)
{ // lambda v : T in expr
  // a UnaryExpression with operator "lambda" and accumulator v : T
  // expr should be bracketed for nested lets. 

  int letcount = 1;
  String lxst = lexicals.get(st) + "";
  if ("lambda".equals(lxst)) {}
  else { return null; }

  for (int j = st+1; j < en; j++)
  { String lxj = lexicals.get(j) + "";
    if ("lambda".equals(lxj))
    { letcount++; }
    else if ("in".equals(lxj))
    { if (letcount > 1)  // an inner let
      { letcount--; } 
      else if (letcount < 1)
      { System.err.println("!! Error: Too many 'in' clauses in lambda expression"); 
        return null; 
      }
      else 
      { Expression var = parse_expression(bc, st+1, st+1,entities,types);
        if (var == null)
        { System.err.println("!! Invalid syntax of lambda variable: " + lexicals.get(st+1)); 
          return null;  
        }
        // lambda var : T in expr
		
        Type ltype = parseType(st+3,j-1,entities,types);
        if (ltype == null)
        { System.err.println("!! Error in lambda type: " + showLexicals(st+3,j-1)); 
          return null; 
        }

        Expression lbody = parse_expression(bc,j+1,en,entities,types); 
        if (lbody == null)
        { System.err.println("!! Error in lambda body expression"); 
          continue; 
        }

        if (ltype != null && lbody != null) 
        { UnaryExpression letexp = new UnaryExpression("lambda", lbody); 
          letexp.accumulator = new Attribute(var + "", ltype, ModelElement.INTERNAL); 
          return letexp; 
              // return new LetExpression(var,ltype,lexp,lbody); 
        } 
      }
    } else { }  
  } // j loop
  return null;
} 

  public BinaryExpression parse_implies_expression(int bcount, int pstart, 
                                                   int pend, int pos, String op)
  { // There must be a logical operator at top level in the expression
    Expression e1 = null; 
    Expression e2 = null;
    // System.out.println(">> Trying to parse logical expression"); 
    // System.out.println(">> Trying at: " + pos + " " + op); 
    Vector env = new Vector(); 
    e1 = parse_expression(bcount,pstart,pos-1,env,env); 

    // if (e1 == null) 
    // { System.err.println("!! Invalid expression: " + showLexicals(pstart, pos-1)); }
 
    e2 = parse_expression(bcount,pos+1,pend,env,env); 

    // if (e2 == null) 
    // { System.err.println("!! Invalid expression: " + showLexicals(pos + 1, pend)); }

    if (e1 == null || e2 == null)
    { 
      // System.out.println("!! Failed to parse: " + showLexicals(pstart, pend)); 
      return null; 
    }
    else 
    { BinaryExpression ee = new BinaryExpression(op,e1,e2);
      // System.out.println(">> Parsed implies expression: " + ee); 
      return ee; 
    } 
  }
   

  public void showLexicals()
  { int sze = lexicals.size(); 
    String lexs = showLexicals(0,sze-1); 
    System.out.println(lexs); 
  } 

  public String showLexicals(int st, int en)
  { String res = ""; 
    for (int i = st; i <= en; i++) 
    { res = res + lexicals.get(i) + " "; } 
    return res; 
  } 

  public Vector getLexicals(int st, int en)
  { Vector res = new Vector(); 
    for (int i = st; i <= en; i++) 
    { res.add(lexicals.get(i)); } 
    return res; 
  } 

  public String getLexical(int st)
  { return "" + lexicals.get(st); } 


  /* public BinaryExpression parse_or_expression(int pstart, 
                                              int pend)
  { 
    Expression e1 = null; 
    Expression e2 = null;

    // System.out.println("Trying to parse or expression"); 
    
    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 
      if (ss.equals("or"))
      { 
        e1 = parse_binary_expression(pstart,i-1); 
        e2 = parse_binary_expression(i+1,pend); 
        if (e1 == null || e2 == null)
        { } // return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed or expression: " + ee); 
          return ee; }
      }
    }
    return null; 
  }

  public BinaryExpression parse_and_expression(int pstart,
                                               int pend)
  {
    Expression e1 = null;
    Expression e2 = null;
 
    // System.out.println("Trying to parse and expression");
    int best = getBestAndOpOccurrence(pstart,pend); 
  
    // for (int i = pstart; i < pend; i++)
    // 
    if (best <= pend)
    {
      String ss = (String) lexs.get(best);
      if (ss.equals("&"))
      { System.out.println("trying & op at position " + best); 
        e1 = parse_binary_expression(pstart,best-1);
        e2 = parse_binary_expression(best+1,pend);
        if (e1 == null || e2 == null)
        { return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed and expression: " + ee);
          return ee; 
        }
      }
    }
    return null;
  }  */ 


  public Expression parse_eq_expression(int bc, int pstart, 
                                        int pend, 
                             Vector entities, Vector types)
  { Expression e1 = null; 
    Expression e2 = null;

    // System.out.println("Trying to parse eq. expression"); 

    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 
      if (ss.equals(":") || ss.equals("<:") ||
          ss.equals("/:") || ss.equals("/<:") || 
          ss.equals("<>") || ss.equals("<>=") ||  
          Expression.comparitors.contains(ss))
      { e1 = parse_additive_expression(bc,pstart,i-1,entities,types); 
        e2 = parse_additive_expression(bc,i+1,pend,entities,types); 
        if (e1 == null || e2 == null)
        { } // return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed equality expression: " + ee); 
          return ee; 
        }
      }
    }
    return parse_additive_expression(bc,pstart,pend,entities,types); 
    // return null; 
  }  // <> is only for ATL

  public Expression parse_additive_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("+") || ss.equals("\\/"))
      { Expression e1 = 
          parse_additive_expression(
                         bc,pstart,i-1, entities, types);
        Expression e2 = 
          parse_additive_expression(
                         bc,i+1,pend, entities, types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = 
            new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed additive expression: " + ee);
          return ee;
        } 
      }
      else if (ss.equals("-"))
      { Expression e1 = 
          parse_additive_expression(
                         bc,pstart,i-1, entities, types);
        Expression e2 = 
          parse_factor_expression(
                         bc,i+1,pend, entities, types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed additive expression: " + ee);
          return ee;
        } 
      }
      /* else if (ss.equals(".") && 
           i + 1 < pend && 
           ".".equals(lexicals.get(i+1) + "")) 
      { Expression e1 = 
            parse_factor_expression(
                         bc,pstart,i-1, entities, types);
        Expression e2 = 
            parse_factor_expression(
                         bc,i+2, pend, entities, types);
        if (e1 == null || e2 == null)         
        { } // return null; }
        else
        { BinaryExpression ee = 
              new BinaryExpression("..",e1,e2);
          System.out.println(">>>>> Parsed additive expression: " + ee);
          return ee;
        } 
      } */ 
    } 

    return parse_factor_expression(
                          bc,pstart,pend,entities,types); 
  } // reads left to right, a - b + c parsed as 
    // (a - b) + c

  public Expression parse_factor_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { // System.out.println("Trying to parse factor expression"); 
    for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if // (ss.equals("+") || ss.equals("-") || 
         (ss.equals("/\\") || ss.equals("^") ||
          ss.equals("*") || ss.equals("/") || 
          ss.equals("div") || 
          ss.equals("mod"))
      { Expression e1 = 
          parse_factor2_expression(
                        bc,pstart,i-1,entities,types);  
        // or basic
        Expression e2 = 
          parse_factor_expression(bc,i+1,pend,entities,types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed factor expression: " + ee);
          return ee;
        } 
      }     
    } 
    return parse_factor2_expression(bc,pstart,pend,entities,types); 
  } // reads left to right, not putting priorities on * above +
  // div is only for ATL. 

  public Expression parseAtExpression(int bc , int pstart, int pend, Vector entities, Vector types)
  { // e->at(i)

    for (int i = pend-1; pstart < i; i--) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("->") && i+2 < lexicals.size() && "(".equals(lexicals.get(i+2) + "") && 
          ")".equals(lexicals.get(pend) + ""))
      { String ss2 = lexicals.get(i+1).toString(); // must exist
        if (i + 3 <= pend && "at".equals(ss2)) 
        { // ee2->at(ee1) 
          Expression ee1 = parse_expression(bc+1,i+3,pend-1,entities,types); 
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          if (ee2 instanceof BasicExpression)
          { BasicExpression be = (BasicExpression) ee2;
            
            be.setArrayIndex(ee1);  
            // System.out.println(">>> Parsed at expression: " + be); 
            return be;
          }  
        } 
      }
    }
    return null; 
  }

  public Expression parseCollectExpression(int bc , int pstart, int pend, Vector entities, Vector types)
  { // e->collect( v | f )

    for (int i = pend-1; pstart < i; i--) 
    { String ss = lexicals.get(i).toString();
 
      if (ss.equals("->") && i+4 < lexicals.size() && 
          "(".equals(lexicals.get(i+2) + "") && 
          ")".equals(lexicals.get(pend) + ""))
      { String ss2 = lexicals.get(i+1).toString(); 
        String vdash = lexicals.get(i+4).toString(); 

        if (i + 3 <= pend && "collect".equals(ss2) && 
            i + 5 <= pend && "|".equals(vdash)) 
        { // ee2->collect( ind | ee1)

          Expression ind = 
            parse_expression(bc+1, i+3, i+3, entities, types);  
          Expression ee1 = 
            parse_expression(bc+1,i+5,pend-1,entities, types);
 
          if (ind == null || ee1 == null) 
          { continue; }
 
          Expression ee2 = 
             parse_factor_expression(
                       bc,pstart,i-1,entities,types); 

          if (ee2 == null) { continue; } 

          BinaryExpression incol = 
            new BinaryExpression(":", ind, ee2); 
          BinaryExpression res = 
            new BinaryExpression("|C", incol, ee1); 
          return res; 
        } 
      }
    }

    return null; 
  }
  
  public Expression parse_factor2_expression(int bc, 
      int pstart, int pend, Vector entities, Vector types)
  { // System.out.println("Trying to parse factor2 expression from " + pstart + " to " + pend); 
    // case of  argument->op() and left->op(right)
    
    for (int i = pend-1; pstart < i; i--) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("->") && i+2 < lexicals.size() && "(".equals(lexicals.get(i+2) + "") && 
          ")".equals(lexicals.get(pend) + ""))
      { String ss2 = lexicals.get(i+1).toString(); // must exist
        if (i + 3 == pend &&
            ("any".equals(ss2) || "size".equals(ss2) || 
             "isDeleted".equals(ss2) ||
             "display".equals(ss2) || 
             "min".equals(ss2) || "max".equals(ss2) ||
             "sum".equals(ss2) || "sort".equals(ss2) || 
             "asSet".equals(ss2) || "asBag".equals(ss2) ||
             "asOrderedSet".equals(ss2) || 
             "sqrt".equals(ss2) || "sqr".equals(ss2) || 
             "asSequence".equals(ss2) ||
             "asArray".equals(ss2) ||
             "last".equals(ss2) || "first".equals(ss2) || 
             "closure".equals(ss2) ||
             "subcollections".equals(ss2) || 
             "reverse".equals(ss2) || "prd".equals(ss2) ||
             "tail".equals(ss2) || "front".equals(ss2) || 
             "isEmpty".equals(ss2) ||
             "notEmpty".equals(ss2) || 
             "toUpperCase".equals(ss2) || 
             "flatten".equals(ss2) ||  
             "trim".equals(ss2) || 
             "toLowerCase".equals(ss2) || 
             "isInteger".equals(ss2) || 
             "toLong".equals(ss2) || "isLong".equals(ss2) ||
             "toInteger".equals(ss2) || 
             "toBoolean".equals(ss2) ||
             "isReal".equals(ss2) || "toReal".equals(ss2) ||
             "exp".equals(ss2) || "log".equals(ss2) || 
             "log10".equals(ss2) || 
             "sin".equals(ss2) || "cos".equals(ss2) || 
             "allInstances".equals(ss2) ||
             "tan".equals(ss2) || 
             "oclIsNew".equals(ss2) || 
             "oclIsUndefined".equals(ss2) || 
             "oclIsInvalid".equals(ss2) || 
             "oclType".equals(ss2) ||
             "copy".equals(ss2) ||  
             "iterator".equals(ss2) ||
             "char2byte".equals(ss2) || 
             "byte2char".equals(ss2) || 
             "unionAll".equals(ss2) || 
             "intersectAll".equals(ss2) || 
             "concatenateAll".equals(ss2) ||
             "floor".equals(ss2) || "ceil".equals(ss2) || 
             "round".equals(ss2) ||
             "abs".equals(ss2) || "cbrt".equals(ss2) || 
             "asin".equals(ss2) ||
             "acos".equals(ss2) || "atan".equals(ss2) || 
             "sinh".equals(ss2) || "cosh".equals(ss2) || 
             "tanh".equals(ss2) ||
             "values".equals(ss2) || "keys".equals(ss2) ||
             "succ".equals(ss2) || "pred".equals(ss2) ||
             "ord".equals(ss2) ||
             extensionOperators.contains(ss2) ) )
        { Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) 
          { // System.err.println("!! Invalid unary -> expression at: " + showLexicals(pstart,pend)); 
            continue; 
          }
          UnaryExpression ue = new UnaryExpression(ss+ss2,ee2);
          // System.out.println(">> Parsed unary expression: " + ue); 
          return ue;   // This excludes additional operators to the ones listed above.  
        } 
        else if ("exists".equals(ss2) && 
                 i+5 < pend && // "(".equals(lexicals.get(i+2) + "") &&
                               "|".equals(lexicals.get(i+4) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->exists(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if (("exists1".equals(ss2) || "one".equals(ss2)) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->exists1(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#1",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println(">> Parsed: " + be); 
          return be; 
        } 
        else if ("existsLC".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->existsLC(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#LC",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("forAll".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->forAll(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("!",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("select".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->select(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("reject".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->reject(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|R",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("collect".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|C",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("sortedBy".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->sortedBy(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|sortedBy",new BinaryExpression(":",bevar,ee2),ee1); 
          System.out.println("Parsed ->sortedBy expression with variable: " + be); 
          return be; 
        } 
        else if ("any".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->any(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|A",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("iterate".equals(ss2) && 
                 i+7 < pend && 
                 ";".equals(lexicals.get(i+4) + "") &&                               
                 "=".equals(lexicals.get(i+6) + "") &&                               
                 ")".equals(lexicals.get(pend) + ""))
        { // It is ->iterate(v; acc = value | rght )
          Expression ee1 = null; 
          Expression ee3 = null; 
          Expression ee2 = 
             parse_factor_expression(bc,pstart,i-1,
                                     entities,types); 
          if (ee2 == null) { continue; } 

          boolean foundpipe = false; 
          for (int t = i+7; t < pend && !foundpipe; t++) 
          { String tlex = lexicals.get(t) + ""; 
            if ("|".equals(tlex))
            { ee1 = parse_expression(bc+1,t+1,pend-1,entities,types);
              ee3 = parse_expression(bc+1,i+7,t-1,entities,types); 
              if (ee1 != null && ee3 != null) 
              { foundpipe = true; } 
            } 
          } 

          if (ee1 == null || ee3 == null) { continue; } 

          BinaryExpression be = 
            new BinaryExpression("->iterate",ee2,ee1); 
          be.setIteratorVariable(lexicals.get(i+3) + "");
          Attribute acc = 
            new Attribute(lexicals.get(i+5) + "",
                          new Type("OclAny", null), 
                          ModelElement.INTERNAL); 
          acc.setInitialExpression(ee3); 
          be.setAccumulator(acc);                
          return be; 
        } 
        else if ("select".equals(ss2) && 
                 i+7 < pend && 
                 ";".equals(lexicals.get(i+4) + "") &&                               
                 ":".equals(lexicals.get(i+6) + "") &&                               
                 ")".equals(lexicals.get(pend) + ""))
        { // It is ->select(v; acc : ss | rght )
          // Same as ->select(v | ss->exists( acc | rght ))

          Expression ee1 = null; 
          Expression ee3 = null; 
          Expression ee2 = 
             parse_factor_expression(bc,pstart,i-1,
                                     entities,types); 
          if (ee2 == null) { continue; } 

          boolean foundpipe = false; 
          for (int t = i+7; t < pend && !foundpipe; t++) 
          { String tlex = lexicals.get(t) + ""; 
            if ("|".equals(tlex))
            { ee1 = parse_expression(bc+1,t+1,pend-1,entities,types);
              ee3 = parse_expression(bc+1,i+7,t-1,entities,types); 
              if (ee1 != null && ee3 != null) 
              { foundpipe = true; } 
            } 
          } 

          if (ee1 == null || ee3 == null) { continue; } 

          String var = lexicals.get(i+3) + ""; 
          String accvar = lexicals.get(i+5) + ""; 
          Expression existsDom = 
            new BinaryExpression(":", 
                                 new BasicExpression(accvar),
                                 ee3); 

          Expression selectDom = 
            new BinaryExpression(":", 
                                 new BasicExpression(var),
                                 ee2); 
          BinaryExpression existsExpr = 
            new BinaryExpression("#", existsDom, ee1); 
          BinaryExpression be = 
            new BinaryExpression("|", selectDom, existsExpr); 
          // be.setIteratorVariable(lexicals.get(i+3) + "");
          // Vector env = new Vector(); 
          // ee3.typeCheck(types,entities,env); 
          // Attribute acc = 
          //   new Attribute(lexicals.get(i+5) + "",
          //                 ee3.getElementType(), 
          //                 ModelElement.INTERNAL); 
          // be.setAccumulatorRange(ee3); // domain of acc
          // be.setAccumulator(acc);                
          return be; 
        } 
        else if ("collect".equals(ss2) && 
                 i+7 < pend && 
                 ";".equals(lexicals.get(i+4) + "") &&                               
                 ":".equals(lexicals.get(i+6) + "") &&                               
                 ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v; acc : ss | rght )
          // Same as ->collect(v | ss->collect( acc | rght ))->concatenateAll()

          Expression ee1 = null; 
          Expression ee3 = null; 
          Expression ee2 = 
             parse_factor_expression(bc,pstart,i-1,
                                     entities,types); 
          if (ee2 == null) { continue; } 

          boolean foundpipe = false; 
          for (int t = i+7; t < pend && !foundpipe; t++) 
          { String tlex = lexicals.get(t) + ""; 
            if ("|".equals(tlex))
            { ee1 = parse_expression(bc+1,t+1,pend-1,entities,types);
              ee3 = parse_expression(bc+1,i+7,t-1,entities,types); 
              if (ee1 != null && ee3 != null) 
              { foundpipe = true; } 
            } 
          } 

          if (ee1 == null || ee3 == null) { continue; } 

          String var = lexicals.get(i+3) + ""; 
          String accvar = lexicals.get(i+5) + ""; 
          Expression existsDom = 
            new BinaryExpression(":", 
                                 new BasicExpression(accvar),
                                 ee3); 

          Expression selectDom = 
            new BinaryExpression(":", 
                                 new BasicExpression(var),
                                 ee2); 
          BinaryExpression existsExpr = 
            new BinaryExpression("|C", existsDom, ee1); 
          BinaryExpression be = 
            new BinaryExpression("|C", selectDom, existsExpr); 
          // be.setIteratorVariable(lexicals.get(i+3) + "");
          // Vector env = new Vector(); 
          // ee3.typeCheck(types,entities,env); 
          // Attribute acc = 
          //   new Attribute(lexicals.get(i+5) + "",
          //                 ee3.getElementType(), 
          //                 ModelElement.INTERNAL); 
          // be.setAccumulatorRange(ee3); // domain of acc
          // be.setAccumulator(acc);                
          return new UnaryExpression("->concatenateAll", be); 
        } 
        else if (pend == i+3) // && "(".equals(lexicals.get(i+2) + "") &&
                              //  ")".equals(lexicals.get(pend) + ""))  
        { Expression ee2 = parse_factor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          UnaryExpression be = new UnaryExpression(ss+ss2,ee2); 
          // System.out.println(">> Parsed new unary -> expression: " + be); 
          return be; 
        } 
        else if (i + 3 <= pend) // && "(".equals(lexicals.get(i+2) + "") &&
                                //   ")".equals(lexicals.get(pend) + "")) 
        // this should allow new Binary operators 
        { // System.out.println(">> Trying to parse at " + ss2); 
          Expression ee1 = parse_expression(bc+1,i+3,pend-1,entities,types); 
          if (ee1 == null) { continue; } 

          Expression ee2 = 
            parse_factor_expression(bc,pstart,i-1,
                                    entities,types); 

          if (ee2 == null) { continue; }

          if ("selectRows".equals(ss2))
          { // convert it to 
            // MathLib.dataTableFromRows(
            //   MathLib.rowsOfDataTable(ee2)->select(
            //     $row | ee1[$row/ee2] ) )
            
            BasicExpression realarg = 
              BasicExpression.newStaticCallBasicExpression(
                 "rowsOfDataTable", "MathLib", ee2);
            BasicExpression svar =
              BasicExpression.newVariableBasicExpression("$row");
            svar.isEvent = false;  
            Expression subee1 = 
                ee1.substituteEq("" + ee2, svar);  
            BinaryExpression domain = 
              new BinaryExpression(":", svar, realarg); 
            BinaryExpression be = 
              new BinaryExpression("|", domain, subee1);
            BasicExpression res = 
              BasicExpression.newStaticCallBasicExpression(
                 "dataTableFromRows", "MathLib", be); 
            return res; 
          }  

          if ("selectElements".equals(ss2))
          { // convert it to 
            // MatrixLib.selectElements(ee2, 
            //    lambda $x : double in ee1[$x/ee2])
            
            BasicExpression svar =
              BasicExpression.newVariableBasicExpression("$x");
            svar.isEvent = false;  
            Type dtype = new Type("double", null); 
            Expression subee1 = 
                ee1.substituteEq("" + ee2, svar);  
            Expression func = 
              UnaryExpression.newLambdaUnaryExpression(
                       "$x", dtype, subee1); 
            Vector pars = new Vector(); 
            pars.add(ee2); 
            pars.add(func); 
            BasicExpression res = 
              BasicExpression.newStaticCallBasicExpression(
                 "selectElements", "MatrixLib", pars); 
            return res; 
          }  
 
          BinaryExpression be = 
            new BinaryExpression(ss+ss2,ee2,ee1); 
          // System.out.println(">>> Parsed binary -> expression: " + be); 
          return be; 
        } 
      }     
    } 
    return parse_basic_expression(bc,pstart,pend,entities,types); 
  } // reads left to right, not putting priorities on * above +

  // add the case for ->iterate

  public Expression parse_basic_expression(int bc, 
       int pstart, int pend, Vector entities, Vector types)
  { 

    // if ("_".equals(lexicals.get(pstart) + "") && pend == pstart+1)
    // { Expression ee = parse_expression(bc,pstart+1,pend); 
    //   if (ee != null) 
    //   { return new UnaryExpression("_", ee); } 
    // } 

    if (pstart == pend)
    { String ss = lexicals.elementAt(pstart).toString(); 
      if (invalidBasicExp(ss))
      { // System.err.println("!!! ERROR: Invalid basic expression: " + ss); 
        return null; 
      } 

      if (cstlVarPatt.matcher(ss).find())
      { System.err.println("!! Warning: do not use identifiers _i for integers i: " + ss); }

      /* if (isKeyword(ss))
      { System.err.println(">>>: Invalid basic expression: keyword: " + ss); 
        return null;
      } */ 

      BasicExpression ee = 
         // new BasicExpression(ss,0);   // (ss) surely?
         new BasicExpression(ss,0); 
      Expression ef = ee.checkIfSetExpression(); 

      // if (ef instanceof BasicExpression) 
      // { ((BasicExpression) ef).setPrestate(ee.getPrestate()); } 

      // System.out.println("+++ Parsed basic expression: " + ss + " " + ee + " " + ef); 
      return ef;
    }

    boolean bracketsOk = balancedBrackets(pstart,pend); 
    if (bracketsOk) { } 
    else 
    { return null; } 

    // System.out.println(">> Trying to parse basic expression from: " + showLexicals(pstart,pend)); 

    if (pstart < pend && 
        "lambda".equals(lexicals.get(pstart) + ""))
    { Expression ee = parse_lambda_expression(bc,pstart,pend,entities,types); 
      if (ee != null) 
      { return ee; } 
    } 

    if (pstart < pend && "let".equals(lexicals.get(pstart) + ""))
    { Expression ee = parse_let_expression(bc,pstart,pend,entities,types); 
      if (ee != null) 
      { return ee; } 
    } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "SortedMap".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { System.out.println(">> Sorted map"); 
      return parse_sortedmap_expression(bc,pstart+1,pend,entities,types); 
    } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Map".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_map_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Sequence".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_sequence_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Set".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_set_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "SortedSet".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_sortedset_expression(bc,pstart+1,pend,entities,types); } 

    // Likewise, SortedSequence

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "SortedSequence".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_sortedsequence_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Ref".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_ref_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Ref".equals(lexicals.get(pstart) + "") &&
        "(".equals(lexicals.get(pstart + 1) + ""))
    { return parse_ref_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "-".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_additive_expression(bc,pstart+1,pend,entities,types); 
      if (arg == null) 
      { // System.err.println("!! Not additive expression: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 
    
      return new UnaryExpression("-",arg); 
    } // likewise for "not" and "~" and "?"


    if (pstart < pend && "+".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_additive_expression(bc,pstart+1,pend,entities,types); 
      if (arg == null) 
      { // System.err.println("!! Not additive expression: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 
      return arg; 
    } 


    if (pstart < pend && "?".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_basic_expression(bc,pstart+1,pend,entities,types); 
      if (arg == null) 
      { System.err.println("!! Error: references must be applied to basic expressions: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 

      return new UnaryExpression("?",arg); 
    } // likewise for "not" and "~"


    if (pstart < pend && "!".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_basic_expression(bc,pstart+1,pend,entities,types); 
      if (arg == null) 
      { System.err.println("!! Error: dereferences must be applied to basic expressions: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 

      return new UnaryExpression("!",arg); 
    } // likewise for "not" and "~"


    if (pstart + 1 < pend && ")".equals(lexicals.get(pend) + "") && 
      "(".equals(lexicals.get(pstart) + ""))
    { Expression res = parse_bracketed_expression(bc,pstart,pend,entities,types); 
      if (res != null) 
      { return res; } 
    } 

    if (pstart + 1 < pend && 
        ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+1) + ""))
    { // op(pars)

      Expression op = parse_basic_expression(bc,pstart,pstart,entities,types); 

      if (op != null && op instanceof BasicExpression)
      { Vector ve = 
          parse_fe_sequence(bc,pstart+2,pend-1,entities,types);
 
        if (ve != null) 
        { BasicExpression beop = (BasicExpression) op; 

          if (Expression.isFunction(beop.data))
          { beop.umlkind = Expression.FUNCTION; } 
          else 
          { beop.setIsEvent(); } 
 
          beop.setParameters(ve); 
        // System.out.println(">> Parsed call expression: " + beop);
          return beop;  
        }
      } 
      // return null; 
    }       

    if (pstart + 2 < pend && 
        ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+2) + "") && 
        ("" + lexicals.get(pstart + 1)).charAt(0) == '.')
    { // iden.op(args)

      Expression objref = parse_basic_expression(bc,pstart,pstart,entities,types); 
      String opstring = lexicals.get(pstart + 1) + ""; 
      String opstr = opstring.substring(1,opstring.length()); 

      Vector ve = parse_fe_sequence(bc,pstart+3,pend-1,entities,types); 
      if (ve != null && objref != null && 
          objref instanceof BasicExpression) // must be
      { BasicExpression beopref = (BasicExpression) objref; 
        BasicExpression beop = new BasicExpression(opstr); 

        if (Expression.isFunction(opstr))
        { beop.umlkind = Expression.FUNCTION; } 
        else 
        { beop.setIsEvent(); } 
 
        beop.setParameters(ve);
        beop.setObjectRef(beopref);  
        // System.out.println(">> Parsed call expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && 
        "]".equals(lexicals.get(pend) + "") && 
        "[".equals(lexicals.get(pstart+1) + ""))
    { // iden[indexes]

      String ss = lexicals.elementAt(pstart).toString(); 
      BasicExpression ee = new BasicExpression(ss,0); 
      Expression op = ee.checkIfSetExpression(); 
      // Expression op = parse_basic_expression(bc,pstart,pstart); 
      Expression arg = parse_additive_expression(bc,pstart+2,pend-1,entities,types); 
      if (arg == null) 
      { Vector args = parse_array_index_sequence(bc,pstart+2,pend-1,entities,types);  
        if (args != null) 
        { Expression opx = defineArrayIndex(op,args,entities,types); 
          // System.out.println(">>> Array index sequence: " + args + " for " + opx); 
          return opx; 
        } 
      } 
      else if (op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        // System.out.println(">> Parsed array expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && "]".equals(lexicals.get(pend-1) + "") && 
        "[".equals(lexicals.get(pstart+1) + "") &&
        (lexicals.get(pend) + "").charAt(0) == '.')  
    { // be[inds].f

      String tail = lexicals.get(pend) + ""; 
      if (tail.charAt(0) == '.')
      { tail = tail.substring(1,tail.length()); } 

      Expression op = parse_basic_expression(bc,pstart,pstart,entities,types); 
      Expression arg = parse_additive_expression(bc,pstart+2,pend-2,entities,types); 

      if (arg == null) 
      { System.err.println("!! ERROR: Invalid array argument: " + showLexicals(pstart+2,pend-2)); 
        Vector args = parse_array_index_sequence(bc,pstart+2,pend-2,entities,types);  
        if (args != null) 
        { Expression opx = 
            defineArrayIndex(op,args,entities,types); 
          System.out.println(">>> Array index sequence: " + args + " for " + opx);
          opx.setBrackets(true); 
          BasicExpression res = new BasicExpression(tail,0); 
          Expression ef = res.checkIfSetExpression(); 
          if (ef != null && (ef instanceof BasicExpression)) 
          { ((BasicExpression) ef).setInnerObjectRef(opx); 
            return ef;
          } 
        } 
      } 
      else if (op != null && 
               op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        BasicExpression te = 
          // new BasicExpression(ss,0);   // (ss) surely?
                       new BasicExpression(tail,0); 
        Expression ef = te.checkIfSetExpression(); 
        if (ef != null && (ef instanceof BasicExpression)) 
        { ((BasicExpression) ef).setInnerObjectRef(beop); }  
        // System.out.println(">> Parsed array 1 expression: " + ef);
        return ef;  
      } 
      // return null; 
    }       

    /* Also ( ... ) .f */ 

    if (pstart+1 < pend &&
        !(")".equals(lexicals.get(pend) + "")) && 
        ")".equals(lexicals.get(pend-1) + "") &&
        "(".equals(lexicals.get(pstart) + ""))  
    { String tail = lexicals.get(pend) + ""; 
      if (tail.charAt(0) == '.')
      { tail = tail.substring(1,tail.length()); 
      
        // System.out.println(">>> Parsing: (...) . " + tail); 
            
        Expression op = 
          parse_basic_expression(bc+1,
                        pstart+1,pend-2,entities,types);

        BasicExpression te = 
            // new BasicExpression(ss,0);   // (ss) surely?
            new BasicExpression(tail,0); 
           
        if (op != null && op instanceof BasicExpression) // must be
        { BasicExpression beop = (BasicExpression) op;  
          Expression ef = te.checkIfSetExpression(); 
          if (ef != null && (ef instanceof BasicExpression)) 
          { ((BasicExpression) ef).setInnerObjectRef(beop); }  
          return ef;  
        }
        else if (op instanceof UnaryExpression && 
                 "!".equals(((UnaryExpression) op).operator))
        { Expression ef = te.checkIfSetExpression(); 
          if (ef != null && (ef instanceof BasicExpression)) 
          { ((BasicExpression) ef).setInnerObjectRef(op); }  
          return ef;  
        } 
      } 
      // return null; 
    }       


  if (pstart + 1 < pend && 
      ")".equals(lexicals.get(pend) + "") && 
      "(".equals(lexicals.get(pstart) + ""))
  { // (be).op(pars)

    for (int i = pstart + 1; i < pend; i++)
    { String brack = lexicals.get(i-1) + ""; 
      String strs = lexicals.get(i) + "";
      String brack2 = lexicals.get(i+1) + "";
 
      if (brack.equals(")") && brack2.equals("(") && 
          strs.length() > 1 && '.' == strs.charAt(0))
      { Expression op = parse_basic_expression(bc, pstart+1, i-2, entities,types);
        System.out.println("*** Parsing extended basic expression " + op + strs + "(...)");

        if (op != null && op instanceof BasicExpression)
        { Vector ve = 
            parse_fe_sequence(bc,i+2,pend-1,entities,types);
           
          if (ve != null)
          { BasicExpression beop = (BasicExpression) op;
            beop.setBrackets(true); 
 
            String tail = strs.substring(1,strs.length()); 
            BasicExpression rees = new BasicExpression(tail, 0); 
            rees.setObjectRef(beop); // setInnerObjectRef  
            rees.setIsEvent();
            rees.setParameters(ve);
            System.out.println("*** Parsed extended call expression " + rees);
            return rees;
          }
        }
        else if (op instanceof UnaryExpression && 
                 "!".equals(((UnaryExpression) op).operator))
        { Vector ve = 
            parse_fe_sequence(bc,i+2,pend-1,entities,types);

          if (ve != null)
          { String tail = strs.substring(1,strs.length()); 
            BasicExpression rees = new BasicExpression(tail, 0); 
            op.setBrackets(true); 
            rees.setInnerObjectRef(op); // setInnerObjectRef  
            rees.setIsEvent();
            rees.setParameters(ve);
            System.out.println("*** Parsed extended call expression " + rees);
            return rees;
          }  
        } 
        else if (op instanceof BinaryExpression && 
                 "->at".equals(((BinaryExpression) op).operator))
        { Vector ve = 
            parse_fe_sequence(bc,i+2,pend-1,entities,types);

          if (ve != null)
          { String tail = strs.substring(1,strs.length()); 
            BasicExpression rees = new BasicExpression(tail, 0); 
            op.setBrackets(true); 

            rees.setInnerObjectRef(op); // setInnerObjectRef  
            rees.setIsEvent();
            rees.setParameters(ve);
            System.out.println("*** Parsed extended call expression " + rees);
            return rees;
          }  
        } 

      }
    }
  }

  if (pstart < pend &&  
      "[".equals(lexicals.get(pstart+1) + "") && 
      "]".equals(lexicals.get(pend-1) + "") && 
      (lexicals.get(pend) + "").charAt(0) == '.') 
  { // iden[ ... ].f  valid basic expression before .f

    Expression op = parse_basic_expression(
                      bc,pstart,pend-1,entities,types); 
    String tail = lexicals.get(pend) + ""; 
    if (tail.charAt(0) == '.')
    { tail = tail.substring(1,tail.length()); } 
    
    if (op instanceof BasicExpression) // must be
    { BasicExpression beop = (BasicExpression) op;  
      BasicExpression rees = new BasicExpression(tail, 0); 
      rees.setObjectRef(beop);  
      System.out.println(">>> Parsed array 2 expression: " + rees);
      return rees;  
    }
    // return null; 
  }       

  if (pstart + 1 < pend &&  
      "]".equals(lexicals.get(pend) + "")) 
  { // be [ ind ]  valid basic expression be

    for (int k = pend-1; k >= pstart; k--)
    { String lex = lexicals.get(k) + ""; 
      if ("[".equals(lex))
      { Expression op = parse_basic_expression(
                       bc,pstart,k-1,entities,types); 
      
        if (op instanceof BasicExpression) // must be
        { BasicExpression beop = (BasicExpression) op;  
          Expression arg = parse_additive_expression(
                         bc,k+1,pend-1,entities,types); 
          if (arg != null && beop.arrayIndex == null)
          { beop.setArrayIndex(arg); 
            System.out.println(">>> Parsed array 3 expression: " + beop);
            return beop;
          } 
        }
      }  
    }
      // return null; 
  }       

  if (pstart + 2 < pend)
  { for (int i = pstart + 1; i < pend; i++)
    { String ss = lexicals.get(i) + "";
      if ("|".equals(ss) && i+1 < pend && "->".equals(lexicals.get(i+1) + ""))
      { Expression key = parse_basic_expression(bc,pstart,i-1,entities,types); 
        Expression value = parse_expression(bc,i+2,pend,entities,types); 
        if (key != null && value != null) 
        { System.out.println(">>> Parsed maplet expression: " + key + " |-> " + value); 
          return new BinaryExpression("|->", key, value); 
        }
      }
    }
  }
 
  return null; 
}
  

  public Expression parse_set_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = 
      parse_fe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,false); 
    System.out.println(">>> Parsed set expression: " + res); 
    return res; 
  } 

  public Expression parse_sortedset_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = 
      parse_fe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,false);
    res.setSorted(true);  
    System.out.println(">>> Parsed sorted set expression: " + res); 
    return res; 
  } 

  public Expression parse_sequence_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = 
      parse_fe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    // System.out.println(">> Parsed sequence expression: " + res); 
    return res; 
  } 

  public Expression parse_sortedsequence_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = 
      parse_fe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true);
    res.setSorted(true);  
    System.out.println(">>> Parsed sorted sequence expression: " + res); 
    return res; 
  } 

  public Expression parse_ref_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { String lex = lexicals.get(pstart) + ""; 

    if ("(".equals(lex))
    { for (int i = pstart+1; i < pend; i++) 
      { String xx = lexicals.get(i) + ""; 
        if ("{".equals(xx))
        { Type rt = parseType(pstart+1,i-2,entities,types); 
          Vector args = 
            parse_fe_sequence(bc, i+1, pend-1,entities,types); 
          if (args == null) 
          { return null; } 
          Expression refexpr = 
            new SetExpression(args,true); 
          Type reft = new Type("Ref", null); 
          reft.setElementType(rt); 
          refexpr.setType(reft); 
          refexpr.setElementType(rt); 
          return refexpr; 
        } 
      }
      return null; 
    }  

    Vector ve = parse_fe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true);
    res.setType(new Type("Ref", null));  
    // System.out.println(">> Parsed set expression: " + res); 
    return res; 
  } 

  public Expression parse_map_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_map_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    res.setType(new Type("Map",null)); 
    // System.out.println(">> Parsed map expression: " + res); 
    return res; 
  } 

  public Expression parse_sortedmap_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_map_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    Type sortedMapType = new Type("Map", null); 
    sortedMapType.setSorted(true); 
    res.setType(sortedMapType);
    res.setSorted(true);  
    System.out.println(">> Parsed sorted map expression: " + res); 
    return res; 
  } 

  public Expression parse_ATLmap_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_ATLmap_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    res.setType(new Type("Map",null)); 
    // System.out.println(">> Parsed map expression: " + res); 
    return res; 
  } 

  public Vector parse_fe_sequence(int bc, int st, int ed, Vector entities, Vector types)
  { Vector res = new Vector(); 
    // if (st == ed) // just one basic exp
    // { Expression elast = parse_basic_expression(bc,st,ed);
    //   res.add(elast); 
    //   return res; 
    // } 
    if (st > ed)
    { return res; } 
    return getFESeq(bc,st,ed,entities,types); 

    // Only commas at the top level of the expression are wanted! 
    /* 
    for (int i = st; i < ed; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_factor_expression(bc,st,i-1,entities,types); 
        if (e1 == null)
        { continue; } // skip this comma
        res = parse_fe_sequence(bc,i+1,ed,entities,types); 
        if (res == null)
        { continue; } // skip this comma
        res.add(0,e1);
        return res; 
      } 
    }
    Expression elast = parse_factor_expression(bc,st,ed,entities,types);
    if (elast != null) 
    { res.add(elast); }  
    return res; 
    */ 
  }  

  private Vector getFESeq(int bc, int st, int en,
                          Vector entities, Vector types)
  { // Scans the lexicals inside the outer brackets of 
    // a potential fe-sequence

     int bcnt = 0; 
     int sqbcnt = 0; 
     int cbcnt = 0;

     Vector res = new Vector();
     int st0 = st;
     String buff = "";
     for (int i = st; i <= en; i++)
     { String lx = lexicals.get(i) + "";
       buff = buff + lx;
       if ("(".equals(lx)) { bcnt++; }
       else if (")".equals(lx)) 
       { bcnt--;
         if (bcnt < 0) { return null; }
       }
       else if ("[".equals(lx)) { sqbcnt++; }
       else if ("]".equals(lx)) { sqbcnt--; }
       else if ("{".equals(lx)) { cbcnt++; }
       else if ("}".equals(lx)) { cbcnt--; }
       else if (",".equals(lx))
       { if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
         { // top-level ,
           Expression exp = 
             parse_additive_expression(
                     bc,st0,i-1,entities,types);
           if (exp == null) 
           { System.out.println("!! Invalid additive exp: " + buff);
             return null;
           }

           res.add(exp);
           st0 = i + 1;
           buff = "";
         }
       }
     }
       // at the end:
     if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
     { Expression exp = 
         parse_additive_expression(bc,st0,en,entities,types);
       if (exp == null) 
       { System.out.println("!! Invalid additive/lambda expression: " + buff);
         return null;
       }
       res.add(exp);
     }
     else
     { // System.err.println("Not fe sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }

  private Expression defineArrayIndex(Expression be, Vector inds, Vector entities, Vector types)
  { if (inds.size() == 0) 
    { return be; } 
    Expression ind1 = (Expression) inds.get(0);
    Expression res = be; 
     
    if (be instanceof BasicExpression) 
    { ((BasicExpression) be).setArrayIndex(ind1); } 
    else 
    { res = new BinaryExpression("->at", res, ind1); }
    
    for (int i = 1; i < inds.size(); i++) 
    { Expression indx = (Expression) inds.get(i); 
      res = new BinaryExpression("->at", res, indx); 
    } 
    return res; 
  }  

  private Vector parse_array_index_sequence(int bc, int st, int en, Vector entities, Vector types)
  { // Scans the lexicals inside the outer brackets of 
    // a potential fe-sequence

     int bcnt = 0; int sqbcnt = 0; int cbcnt = 0;
     Vector res = new Vector();
     int st0 = st;
     String buff = "";
     for (int i = st; i <= en; i++)
     { String lx = lexicals.get(i) + "";
       buff = buff + lx;
       if ("(".equals(lx)) { bcnt++; }
       else if (")".equals(lx)) 
       { bcnt--;
         if (bcnt < 0) { return null; }
       }
       else if ("]".equals(lx) && 
                i+1 <= en && 
                "[".equals(lexicals.get(i+1) + ""))
       { // System.out.println(">> array index " + showLexicals(st0, (i-1))); 

         if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
         { // top-level ,
           Expression exp = parse_additive_expression(bc,st0,i-1,entities,types);
           if (exp == null) 
           { // System.out.println("!! Invalid additive exp: " + buff);
             return null;
           } 
           res.add(exp);
           st0 = i + 2;
           i = i+2; 
           buff = "";
         }
       }
       else if ("[".equals(lx)) { sqbcnt++; }
       else if ("]".equals(lx)) { sqbcnt--; }
       else if ("{".equals(lx)) { cbcnt++; }
       else if ("}".equals(lx)) { cbcnt--; }
     }
       // at the end:
     if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
     { // System.out.println(">> array index: " + showLexicals(st0,en));
 
       Expression exp = parse_additive_expression(bc,st0,en,entities,types);
         if (exp == null) 
         { // System.out.println("!! Invalid additive/lambda expression: " + buff);
           return null;
         }
       res.add(exp);
     }
     else
     { // System.err.println("!! Not fe sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }

  public Vector parse_map_sequence(int bc, int st, int ed, Vector entities, Vector types)
  { Vector res = new Vector(); 
    if (st > ed)
    { return res; } 
    return getMapSeq(bc,st,ed,entities,types); 
  } 

  private Vector getMapSeq(int bc, int st, int en, Vector entities, Vector types)
  { // Scans the lexicals inside the outer brackets of 
    // a potential fe-sequence

     int bcnt = 0; int sqbcnt = 0; int cbcnt = 0;
     Vector res = new Vector();
     int st0 = st;
     String buff = "";
     for (int i = st; i <= en; i++)
     { String lx = lexicals.get(i) + "";
       buff = buff + lx;
       if ("(".equals(lx)) { bcnt++; }
       else if (")".equals(lx)) 
       { bcnt--;
         if (bcnt < 0) { return null; }
       }
       else if ("[".equals(lx)) { sqbcnt++; }
       else if ("]".equals(lx)) { sqbcnt--; }
       else if ("{".equals(lx)) { cbcnt++; }
       else if ("}".equals(lx)) { cbcnt--; }
       else if (",".equals(lx))
       { if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
         { // top-level ,
           Expression exp = parse_pair_expression(bc,st0,i-1,entities,types);
           if (exp == null) 
           { System.out.println("!! Invalid pair exp: " + showLexicals(st0,i-1));
             return null;
           }
           res.add(exp);
           st0 = i + 1;
           buff = "";
         }
       }
     }
       // at the end:
     if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
     { Expression exp = parse_pair_expression(bc,st0,en,entities,types);
       if (exp == null) 
       { System.out.println("!! Invalid pair exp: " + showLexicals(st0,en));
         return null;
       }
       res.add(exp);
     }
     else
     { System.err.println("!! Not map element sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }

  public Vector parse_ATLmap_sequence(int bc, int st, int ed, Vector entities, Vector types)
  { Vector res = new Vector(); 
    if (st > ed)
    { return res; } 
    return getATLMapSeq(bc,st,ed,entities,types); 
  } 

  private Vector getATLMapSeq(int bc, int st, int en, Vector entities, Vector types)
  { // Scans the lexicals inside the outer brackets of 
    // a potential fe-sequence

     int bcnt = 0; int sqbcnt = 0; int cbcnt = 0;
     Vector res = new Vector();
     int st0 = st;
     String buff = "";
     for (int i = st; i <= en; i++)
     { String lx = lexicals.get(i) + "";
       buff = buff + lx;
       if ("(".equals(lx)) { bcnt++; }
       else if (")".equals(lx)) 
       { bcnt--;
         if (bcnt < 0) { return null; }
       }
       else if ("[".equals(lx)) { sqbcnt++; }
       else if ("]".equals(lx)) { sqbcnt--; }
       else if ("{".equals(lx)) { cbcnt++; }
       else if ("}".equals(lx)) { cbcnt--; }
       else if (",".equals(lx))
       { if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
         { // top-level ,
           Expression exp = parse_ATLpair_expression(bc,st0,i-1,entities,types);
           if (exp == null) 
           { System.out.println("!! Invalid ATL pair exp: " + showLexicals(st0,i-1));
             return null;
           }
           res.add(exp);
           st0 = i + 1;
           buff = "";
         }
       }
     }
       // at the end:
     if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
     { Expression exp = parse_ATLpair_expression(bc,st0,en,entities,types);
       if (exp == null) 
       { System.out.println("!! Invalid ATL pair exp: " + showLexicals(st0,en));
         return null;
       }
       res.add(exp);
     }
     else
     { System.err.println("!! Not map element sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }

  public Expression parse_pair_expression(int bc, int st, int en, Vector entities, Vector types)
  { // st is "(" and en is ")" 
    System.out.println(">>> Map contents: --> " + showLexicals(st,en));
	
    for (int i = st; i <= en; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss) && i < en) // st is "(" and en is ")" 
      { Expression e1 = parse_expression(bc,st + 1, i-1,entities,types); 
        Expression e2 = parse_expression(bc,i+1, en-1,entities,types); 
        if (e1 != null && e2 != null)
        { Expression expr = new BinaryExpression(",", e1, e2); 
          expr.setBrackets(true);
          return expr;  
        } 
      } // or the pair is not bracketed
      else if ("|".equals(ss) && i+1 < en && "->".equals(lexicals.get(i+1) + ""))
      { Expression e1 = parse_expression(bc,st, i-1,entities,types); 
        Expression e2 = parse_expression(bc,i+2, en,entities,types); 
        if (e1 != null && e2 != null)
        { return new BinaryExpression("|->", e1, e2); } 
      }	// or a single variable 
      else if ("_1".equals(ss)) 
      { Expression be = new BasicExpression(ss); 
        if (be != null) 
        { return be; }
      }   
    } 
    return null; 
  }  

  public Expression parse_ATLpair_expression(int bc, int st, int en, Vector entities, Vector types)
  { // st is "(" and en is ")" 
    System.out.println(">>> Map contents: --> " + showLexicals(st,en));
	
    for (int i = st; i <= en; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss) && i < en) // st is "(" and en is ")" 
      { Expression e1 = parse_expression(bc,st + 1, i-1,entities,types); 
        Expression e2 = parse_expression(bc,i+1, en-1,entities,types); 
        if (e1 != null && e2 != null)
        { Expression expr = new BinaryExpression("|->", e1, e2); 
          // expr.setBrackets(true);
          return expr;  
        } 
      } // or the pair is not bracketed
      else if ("|".equals(ss) && i+1 < en && "->".equals(lexicals.get(i+1) + ""))
      { Expression e1 = parse_expression(bc,st, i-1,entities,types); 
        Expression e2 = parse_expression(bc,i+2, en,entities,types); 
        if (e1 != null && e2 != null)
        { return new BinaryExpression("|->", e1, e2); } 
      }	// or a single variable 
      else if ("_1".equals(ss)) 
      { Expression be = new BasicExpression(ss); 
        if (be != null) 
        { return be; }
      }   
    } 
    return null; 
  }  


  public BehaviouralFeature parseOperation(Vector entities, Vector types)
  { int n = lexicals.size(); 
    if (n == 0) 
    { return null; } 
    BehaviouralFeature bf;

    String modality = "" + lexicals.get(0);
    if ("static".equals(modality)) 
    { bf = operationDefinition(2,n-1,entities,types); 
      if (bf != null) 
      { bf.setStatic(true); }
    } 
    else 
    { bf = operationDefinition(1,n-1,entities,types); }
 
    if (bf == null) 
    { return null; } 
    if ("query".equals(modality))
    { bf.setQuery(true); } 
    else 
    { bf.setQuery(false); } 
    return bf; 
  } 

  public BehaviouralFeature operationDefinition(Vector entities, Vector types)
  { return operationDefinition(0, lexicals.size()-1, entities, types); } 


public BehaviouralFeature operationDefinition(int st, int en, Vector entities, Vector types)
{ // name<typepars>(pars) : returnType 
  // pre: expr1
  // post: expr2
  // activity: stat; 
  
  if (en <= st) { return null; }
  boolean valid = false; 
  boolean foundpre = false; 
  boolean foundpost = false; 
  Vector localEntities = new Vector(); 

  String opname = lexicals.get(st) + "";
   
  BehaviouralFeature bf = new BehaviouralFeature(opname);
  int st0 = st + 1;
  int parsStart = st0; 

  if (st0+2 < en && 
      "<".equals(lexicals.get(st0) + "") && 
      ">".equals(lexicals.get(st0+2) + ""))
  { String parType = lexicals.get(st0+1) + "";
    Entity ptEnt = new Entity(parType); 
    ptEnt.genericParameter = true; 
    Type pt = new Type(ptEnt); 
    bf.addTypeParameter(pt);  
    localEntities.add(ptEnt); 
    st0 = st0+3; 
    parsStart = st0; 
    System.out.println(">> Generic operation " + opname + "<" + pt + ">"); 
  } 
  else if (st0+2 < en && 
      "<".equals(lexicals.get(st0) + ""))
  { for (int j = st0+1; j < en; j++) 
    { String lex = lexicals.get(j) + ""; 
      if (">".equals(lex))
      { Vector pars = 
          parse_generic_parameters(st0+1,j-1,entities); 
        if (pars != null) 
        { bf.setTypeParameters(pars); } 
        st0 = j+1; 
        parsStart = st0; 
        break; 
      } 
    } 
    System.out.println(">> Generic operation " + 
                       bf.getCompleteName()); 
  }  

  localEntities.addAll(entities); 

  for (int i = st0; i < en; i++)
  { String lx = lexicals.get(i) + "";
    String lx1 = lexicals.get(i+1) + "";
    if (lx.equals("pre") && lx1.equals(":"))
    { parseOpDecs(parsStart,i-1,localEntities,types,bf); 
      foundpre = true; 
      st0 = i+2;
    }
    else if (lx.equals("post") && lx1.equals(":"))
    { Expression pre = parse_expression(0,st0,i-1,localEntities,types);
      if (pre == null)
      { System.err.println("!! ERROR: Invalid precondition: " + showLexicals(st0,i-1)); 
        checkBrackets(st0,i-1); 
        pre = new BasicExpression(true); 
      }
      foundpre = true; 
      foundpost = true; 

      bf.setPre(pre);
      valid = true; 
      st0 = i+2;
      for (int j = st0; j < en; j++) 
      { String jx = lexicals.get(j) + ""; 
        if ("activity".equals(jx) && ":".equals(lexicals.get(j+1) + ""))
        { st0 = j+2; 
          Expression pots = parse_expression(0,i+2,j-1,localEntities,types); 
          if (pots == null) 
          { System.err.println("!! ERROR: Invalid postcondition syntax: " + showLexicals(i+2,j-1));
            checkBrackets(i+2,j-1); 
            bf.setPost(new BasicExpression(true)); 
          } 
          else 
          { bf.setPost(pots); } 
          Statement act = parseStatement(j+2,en,localEntities,types); 
          if (act == null) 
          { System.err.println("!! ERROR: Invalid activity syntax: " + showLexicals(j+2,en)); 
            checkBrackets(j+2,en); 
          } 
          bf.setActivity(act); 

          removeTypeParameters(bf.getTypeParameters(), entities); 

          return bf;  
        }    
      }
    }
    // removeTypeParameters(bf.getTypeParameters(), entities); 
  }

  /* Expression pst = parse_expression(0,st0,en);
  if (pst == null)
  { System.err.println("!! ERROR: Invalid postcondition: " + showLexicals(st0,en));
    bf.setPost(new BasicExpression(true)); 
  }
  else 
  { bf.setPost(pst); 
    valid = true; 
  } */ 

  if (foundpost == false)
  { System.err.println("!! Invalid operation definition, no postcondition: " + showLexicals(st,en)); 
    parseOpDecs(parsStart,en,localEntities,types,bf); 
  }
  else 
  { Expression pst = parse_expression(0,st0,en,localEntities,types);
    if (pst == null)
    { System.err.println("!! ERROR: Invalid postcondition: " + showLexicals(st0,en));
      bf.setPost(new BasicExpression(true)); 
    }
    else 
    { bf.setPost(pst); 
      valid = true; 
    }
  } 

  // removeTypeParameters(bf.getTypeParameters(), entities); 

  return bf;
}
     
    

private void parseOpDecs(int st, int en, Vector entities, Vector types, BehaviouralFeature bf)
{ int bcnt = 0; int st0 = st+1;
  int np = 0; 

  int bracketend = en; 
  for (int i = st; i <= en; i++) 
  { String lex = lexicals.get(i) + ""; 
    if ("(".equals(lex)) { bcnt++; } 
    else if (")".equals(lex)) 
    { bcnt--; 
      if (bcnt == 0) 
      { bracketend = i; 
        break; 
      }
    } 
    else if (":".equals(lex)) 
    { np++; } 
  } 
  bcnt = 0;  

  System.out.println(">>> Operation " + bf + " has " + np + " Parameters"); 
  // , and contextual entities: " + entities); 
  

  Vector res = new Vector();
  for (int j = st; j <= en; j++)
  { String lx = lexicals.get(j) + "";
    if ("(".equals(lx)) { bcnt++; }
    else if (")".equals(lx)) 
    { bcnt--; 
      if (bcnt < 0)
      { System.err.println("!! Extra closing bracket in declaration: " + showLexicals(st,j)); 
        return;
      }
    } 
    else if (":".equals(lx) && bcnt == 1)
    { String attnme = lexicals.get(j-1) + ""; 
      Attribute att = null; 
      for (int k = j+1; k <= bracketend; k++) 
      { String lxk = lexicals.get(k) + ""; 
        if (":".equals(lxk))
        { att = parseParameterDec(j-1, k-3, entities, types);     
          if (att == null)
          { System.err.println("*** Invalid parameter syntax: " + showLexicals(st0,j-1)); }
          else
          { System.out.println("*** Parsed parameter: " + att + " type: " + att.getType() + " " + 
                               att.getElementType()); 
            bf.addParameter(att); 
          }
          st0 = k+1;
          break; 
        } 
      }

      if (att == null) 
      { att = parseParameterDec(j-1, bracketend-1, entities, types); 
        if (att != null) 
        { System.out.println("*** Parsed parameter: " + att + " type: " + att.getType() + " " + 
                               att.getElementType()); 
          bf.addParameter(att); 
          st0 = bracketend + 1; 
        }     
      }       
    }
    else if (":".equals(lx) && bcnt == 0)
    { Type rt = parseType(j+1,en,entities,types);
      if (rt == null)
      { System.err.println("!! ERROR: Invalid return type: " + showLexicals(j+1, en)); 
        bf.setQuery(false); 
        rt = new Type("void", null); 
      }
      else if ("void".equals(rt + ""))
      { bf.setQuery(false); } 
      else 
      { bf.setQuery(true); } 
      bf.setResultType(rt);
      bf.setElementType(rt.getElementType()); 
    } // it is query unless the result type is null or void. 
  }

  if (bcnt > 0)
  { System.err.println("!! Unclosed bracket in declaration: " + showLexicals(st,en)); }
}

public Attribute parseAttribute(Vector entities, Vector types)
{ int n = lexicals.size(); 
  if (n <= 1) 
  { return null; } 
  String modality = "" + lexicals.get(0); 

  if ("attribute".equals(modality))
  { Attribute att = 
      parseParameterDec(1,n-1,entities,types); 
    return att; 
  } 
  else if ("static".equals(modality) && 
           "attribute".equals("" + lexicals.get(1)))
  { Attribute att = 
      parseParameterDec(2,n-1,entities,types); 
    att.setStatic(true);
    return att; 
  } 
  /* else if ("frozen".equals(modality) && 
           "attribute".equals("" + lexicals.get(1)))
  { Attribute att = 
      parseParameterDec(2,n-1,entities,types); 
    att.setFrozen(true);
    return att; 
  } */ 

  return null; 
} 

public Attribute parseParameterDeclaration(Vector entities, 
                                           Vector types)
{ int en = lexicals.size() - 1; 
  Attribute res = parseParameterDec(0,en,entities,types);
  if (res != null) 
  { res.setIsParameter(true); } 
  return res;  
} 

private Attribute parseParameterDec(int st, int en, Vector entities, Vector types)
{ // should be att : Type
  if (st+2 > en) { return null; } 

  String attname = lexicals.get(st) + "";
  Type typ = parseType(st+2,en,entities,types);
  if (typ == null)
  { System.err.println("!! ERROR: Invalid/unknown parameter type: " + showLexicals(st+2, en)); 
    return null; 
  }
  Attribute att = 
    new Attribute(attname, typ, ModelElement.INTERNAL);
  att.setElementType(typ.getElementType()); 
  return att;
}

private Attribute parseParameterDecInit(int st, int en, Vector entities, Vector types)
{ // can be att : Type = value
  if (st + 2 > en) { return null; } 

  String attname = lexicals.get(st) + ""; 
  for (int i = st+1; i < en; i++) 
  { String lex = lexicals.get(i) + ""; 
    if ("=".equals(lex))
    { Expression val = parse_expression(0,i+1,en,entities,types); 
      Type typ = parseType(st+2,i-1,entities,types); 
      if (typ == null)
      { System.err.println("!! ERROR: Invalid/unknown type: " + showLexicals(st+2, i-1)); 
        return null; 
      }
      Attribute att = new Attribute(attname,typ,ModelElement.INTERNAL); 
      att.setInitialExpression(val); 
      return att; 
    } 
  }
  Type tt = parseType(st+2,en,entities,types); 
  if (tt == null)
  { System.err.println("!! ERROR: Invalid/unknown type: " + showLexicals(st+2, en)); 
    return null; 
  }
  
  Attribute newatt = new Attribute(attname,tt,ModelElement.INTERNAL); 
  return newatt; 
} 
 

public Vector parseAttributeDecs(int st, int en, Vector entities, Vector types)
{ // att1 : T1; att2 : T2; ...

  int st0 = st;   
  Vector res = new Vector();
  for (int k = st+2; k < en; k++)
  { String lx = lexicals.get(k) + "";
 
    if (lx.equals(":"))
    { Attribute att = parseParameterDec(st0,k-3,entities,types); // k-2?   
      if (att != null)
      { res.add(att); }
      st0 = k-1;
    }
  }

  Attribute af = parseParameterDec(st0,en,entities,types);
  if (af != null)
  { res.add(af); }
  return res;
}


public Vector parseAttributeDecs(Vector entities, Vector types)
{ int n = lexicals.size(); 
  return parseAttributeDecs(0,n-1,entities,types); 
} 

public Vector parseAttributeDecsInit(int st, int en, Vector entities, Vector types)
{ // att1 : T1 = val1; att2 : T2 = val2; ...

  int st0 = st;   
  Vector res = new Vector();
  for (int k = st+2; k < en; k++)
  { String lx = lexicals.get(k) + "";
 
    if (lx.equals(":"))
    { Attribute att = parseParameterDecInit(st0,k-3,entities,types); // k-2?   
      if (att != null)
      { res.add(att); }
      st0 = k-1;
    }
  }

  Attribute af = parseParameterDecInit(st0,en,entities,types);
  if (af != null)
  { res.add(af); }
  return res;
}


public Vector parseAttributeDecsInit(Vector entities, Vector types)
{ int n = lexicals.size(); 
  return parseAttributeDecsInit(0,n-1,entities,types); 
} 

 /*  public Expression parse_call_expression(int pstart, int pend)
  { for (int i = pstart; i < pend; i++)
    { String ss = lexicals.get(i) + ""; 
      if ("(".equals(ss))
      { Expression opref = parse_basic_expression(pstart,i-1); 
        if (opref == null)  { return null; } 
        Vector args = parse_call_arguments(i+1,pend-1); 
         
        if (opref instanceof BasicExpression)
        { ((BasicExpression) opref).setParameters(args); } 
        // System.out.println(">> parsed call expression: " + opref); 
        return opref; 
      } 
    }
    // System.out.println("Failed to parse call expression"); 
    return null; 
  }

  public Vector parse_call_arguments(int st, int ed, Vector entities, Vector types)
  { Vector res = new Vector(); 
    for (int i = st; i < ed; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_basic_expression(st,i-1,entities,types); 
        res = parse_call_arguments(i+1,ed,entities,types); 
        if (e1 != null) { res.add(0,e1); }
        return res; 
      } 
    }
    return res; 
  }  */ 
 

        

  public Expression parse_bracketed_expression(int bcount, int pstart, int pend, Vector entities, Vector types)
  { if (pstart >= pend) 
    { return null; } 

    if (lexicals.size() > 2 &&
        lexicals.get(pstart).toString().equals("(") &&
        lexicals.get(pend).toString().equals(")"))
    { Expression eg = parse_expression(bcount+1, pstart+1, pend-1,entities,types);
      // System.out.println("+++ parsed bracketed expression: " + eg);  
      if (eg != null)
      { eg.setBrackets(true); }
      return eg; 
    }
    return null; 
  } // also case of pstart = (, pend-1 = ), pend.charAt(0) == '.'
 
  public ASTTerm parseAST(String xstring)
  { nospacelexicalanalysisText(xstring);
    int sz = lexicals.size();  
    ASTTerm res = parseAST(0,sz-1); 
    return res;
  }
  
  public ASTTerm parseAST(int st, int en)
  { ASTTerm res = null; 
    String lex = lexicals.get(st) + ""; 
	
	if ("(".equals(lex) && ")".equals(lexicals.get(en) + "")) {}
	else 
	{ return null; }
	
	String tag = lexicals.get(st+1) + ""; 
	String value = lexicals.get(en-1) + ""; 
	
	if (st+3 >= en)
	{ res = new ASTBasicTerm(tag,value); 
	  return res; 
	}
	
	Vector subtrees = parseASTSequence(st+2, en-1); 
	if (subtrees != null) 
	{ res = new ASTCompositeTerm(tag,subtrees); }

	return res; 
  }

  public Vector parseASTSequence(int st, int en) 
  { // expecting AST1 ... ASTn 
  
    Vector res = new Vector(); 
    String lex = lexicals.get(st) + ""; 
	
    if ("(".equals(lex) && ")".equals(lexicals.get(en) + "")) {}
    else 
    { return null; }
	
	for (int i = st+1; i <= en; i++) 
	{ String lexend = lexicals.get(i) + ""; 
	  if (")".equals(lexend))
	  { ASTTerm pn = parseAST(st,i); 
	    if (pn != null && i < en) 
		{ Vector rest = parseASTSequence(i+1,en); 
		  if (rest != null)
		  { res.add(pn); 
		    res.addAll(rest); 
		    return res; 
		  }
		}
		else if (pn != null && i >= en) 
		{ res.add(pn); 
		  return res; 
		}
	  }
	}
	return null; 
  }

  public void parseDataFeatureDefinition(String xstring, Entity currentclass, Vector entities, Vector types, Vector assocs)
  { nospacelexicalanalysisText(xstring);
    int sz = lexicals.size();  
    // Should be 2
    if (sz < 2) 
    { return; } 

    String typ = ""; 
    Type fulltype = null; 

    String f = lexicals.get(0) + ""; 
    String t1 = lexicals.get(1) + ""; 
    if ("array".equals(t1))
    { // array ( t )
      String t2 = lexicals.get(2) + ""; 
      if ("(".equals(t2))
      { String t = lexicals.get(3) + ""; 
        // System.out.println(">>> Feature " + f + " : Sequence(" + t + ")"); 
        typ = t; 
        Type elemt = Type.getTypeFor(typ,types,entities); 
        fulltype = new Type("Sequence",null);
        if (elemt == null) 
        { elemt = new Type(typ,null); } 
        fulltype.setElementType(elemt); 
      } 
      else 
      { // System.out.println(">>> Feature " + f + " : Sequence(" + t2 + ")");
        typ = t2; 
        Type elemt = Type.getTypeFor(typ,types,entities); 
        fulltype = new Type("Sequence",null); 
        if (elemt == null) 
        { elemt = new Type(typ,null); } 
        fulltype.setElementType(elemt); 
      }
    } 
    else 
    { // System.out.println(">>> Feature " + f + " : " + t1);
      typ = t1; 
      fulltype = Type.getTypeFor(typ,types,entities); 
      if (fulltype == null) 
      { fulltype = new Type(typ,null); } 
    } 

    if (Type.isDatatype(typ, types))
    { System.out.println(">>> attribute " + f + " : " + fulltype + ";"); 
      currentclass.addAttribute(f,fulltype); 
    } 
    else 
    { System.out.println(">>> reference " + f + " : " + fulltype + ";"); 
      currentclass.addReference(f,fulltype,assocs); 
    } 
  
  } 
  
  public ASTTerm parseGeneralAST(String xstring)
  { nospacelexicalanalysisText(xstring);
    int sz = lexicals.size();  
    ASTTerm res = parseGeneralAST(0,sz-1); 
    return res;
  }

  public ASTTerm parseSimpleAST(String xstring)
  { nospacelexicalanalysisSimpleText(xstring);
    int sz = lexicals.size();  
    ASTTerm res = parseGeneralAST(0,sz-1); 
    return res;
  }

  public ASTTerm parseMathOCLAST(String xstring)
  { nospacelexicalanalysisAST(xstring);
    int sz = lexicals.size();  
    ASTTerm res = parseGeneralAST(0,sz-1); 
    return res;
  }
  
  public ASTTerm parseGeneralAST(int st, int en)
  { ASTTerm res = null; 
  
    if (lexicals == null || lexicals.size() == 0) 
    { return res; }
	
    String lex = lexicals.get(st) + ""; 

    if (st == en) 
    { ASTSymbolTerm sym = new ASTSymbolTerm(lex);
      return sym;  
    }
	
    if ("(".equals(lex) && 
        ")".equals(lexicals.get(en) + "")) {}
    else 
    { return null; }
	

    String tag = lexicals.get(st+1) + ""; 
    String value = lexicals.get(en-1) + "";

    if (st+1 == en-1)
    { ASTSymbolTerm sym = new ASTSymbolTerm(tag);
      return sym; 
    } // But actually this is an error: ( tag )
	
    if (en <= st + 3 && isSimpleIdentifier(tag))
    { res = new ASTBasicTerm(tag,value); 
      return res; 
    } // (tag value)
	
    Vector subtrees = parseGeneralASTSequence(st+2, en-1); 
    if (subtrees != null) 
    { res = new ASTCompositeTerm(tag,subtrees); }

    return res; 
  }

  public Vector parseGeneralASTSequence(int st, int en) 
  { // expecting AST1 ... ASTn 
    // #opening brackets must = #closing
	
    Vector res = new Vector(); 
    if (st > en)
    { return res; }
	
    String lex = lexicals.get(st) + ""; 
	
    if (st == en) 
    { ASTSymbolTerm sym = new ASTSymbolTerm(lex); 
      res.add(sym); 
      return res; 
    }
	
    if ("(".equals(lex) && st < en && isSimpleIdentifier(lexicals.get(st+1) + ""))
    { // we expect an ASTTerm followed by the rest of the sequence
	  int ocount = 1; 
	  int ccount = 0; 
	  
	  for (int i = st+1; i <= en; i++) 
	  { String lexend = lexicals.get(i) + ""; 
          if ("(".equals(lexend))
          { ocount++; }
          else if (")".equals(lexend))
          { ccount++; }
		
	    if (")".equals(lexend) && ocount == ccount)
	    { ASTTerm pn = parseGeneralAST(st,i); 
	      if (pn != null && i < en) 
		  { Vector rest = parseGeneralASTSequence(i+1,en); 
		    if (rest != null)
		    { res.add(pn); 
		      res.addAll(rest); 
		      return res; 
		    }
		  }
		  else if (pn != null && i >= en) 
		  { res.add(pn); 
		    return res; 
		  }
	    }
	  }
	}
	else if ("(".equals(lex) && st < en)
	{ // the ( is a symbol  
       ASTSymbolTerm sym = new ASTSymbolTerm("("); 
       Vector rem = parseGeneralASTSequence(st+1,en); 
       if (rem != null)
       { res.add(sym); 
         res.addAll(rem); 
         return res; 
       }
	}
	else if (st < en)
     { ASTSymbolTerm sym = new ASTSymbolTerm(lex); 
       Vector rem = parseGeneralASTSequence(st+1,en); 
       if (rem != null)
       { res.add(sym); 
         res.addAll(rem); 
         return res; 
       }
	}
	return null; 
  }
  
  public Expression parse_ATLexpression(int bcount, int pstart, int pend, Vector entities, Vector types)
  { Expression ee = null; 
    
    if ("if".equals(lexicals.get(pstart) + "") && "endif".equals(lexicals.get(pend) + ""))
    { ee = parse_ATLconditional_expression(bcount,pstart,pend,entities,types); 
      if (ee != null) { return ee; } 
    } 

    if ("let".equals(lexicals.get(pstart) + ""))
    { ee = parse_let_expression(bcount,pstart,pend,entities,types); 
      if (ee != null) { return ee; } 
    } 

    if ("not".equals(lexicals.get(pstart) + ""))
    { ee = parse_ATLexpression(bcount,pstart+1,pend,entities,types); 
      if (ee != null) 
      { return new UnaryExpression("not", ee); } 
    } 
 
    OpOccurrence op = getBestOcc(pstart,pend); 
    if (op == null) // No logical ops here
    { ee = parse_ATLeq_expression(bcount,pstart,pend,entities,types);
      if (ee == null) 
      { ee = parse_ATLfactor_expression(bcount,pstart,pend,entities,types); }
      return ee; 
    }

    if (op.bcount > bcount) // bracketed expression
    { ee = parse_ATLeq_expression(bcount,pstart,pend,entities,types); 
      if (ee == null) 
      { ee = parse_ATLfactor_expression(bcount,pstart,pend,entities,types); }
      return ee; 
    }  
    else 
    { ee = parse_ATLimplies_expression(bcount,pstart,pend,op.location,op.op,entities,types); }
    return ee;  
  }

  public BinaryExpression parse_ATLimplies_expression(int bcount, int pstart, 
                                                   int pend, int pos, String op,
                                                   Vector entities, Vector types)
  { // There must be a logical operator at top level in the expression
    Expression e1 = null; 
    Expression e2 = null;
    // System.out.println("Trying to parse logical expression"); 
    // System.out.println("Trying at: " + pos + " " + op); 
    e1 = parse_ATLexpression(bcount,pstart,pos-1,entities,types); 

    // if (e1 == null) 
    // { System.err.println("Invalid expression: " + showLexicals(pstart, pos-1)); }
 
    e2 = parse_ATLexpression(bcount,pos+1,pend,entities,types); 

    // if (e2 == null) 
    // { System.err.println("Invalid expression: " + showLexicals(pos + 1, pend)); }

    if (e1 == null || e2 == null)
    { System.out.println("Failed to parse: " + showLexicals(pstart, pend)); 
      return null; 
    }
    else 
    { BinaryExpression ee = new BinaryExpression(op,e1,e2);
      // System.out.println(">> Parsed implies expression: " + ee); 
      return ee; 
    } 
  }

  public Expression parse_ATLeq_expression(int bc, int pstart, 
                                              int pend, Vector entities, Vector types)
  { Expression e1 = null; 
    Expression e2 = null;

    // System.out.println("Trying to parse eq. expression"); 

    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 
      if (ss.equals(":=") || ss.equals(":") || ss.equals("<:") ||
          ss.equals("/:") || ss.equals("/<:") || ss.equals("<>") || 
          Expression.comparitors.contains(ss))
      { e1 = parse_ATLadditive_expression(bc,pstart,i-1,entities,types); 
        e2 = parse_ATLadditive_expression(bc,i+1,pend,entities,types); 
        if (e1 == null || e2 == null)
        { } // return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed equality expression: " + ee); 
          return ee; 
        }
      }
    }
    return parse_ATLadditive_expression(bc,pstart,pend,entities,types); 
    // return null; 
  }  // <> is only for ATL

  public Expression parse_ATLadditive_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("+") || ss.equals("-") || ss.equals("\\/"))
      { Expression e1 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types);
        Expression e2 = parse_ATLadditive_expression(bc,i+1,pend,entities,types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed additive expression: " + ee);
          return ee;
        } 
      }     
    } 
    return parse_ATLfactor_expression(bc,pstart,pend,entities,types); 
  } // reads left to right, not putting priorities on * above +

  public Expression parse_ATLfactor_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { // System.out.println("Trying to parse factor expression"); 
    for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if // (ss.equals("+") || ss.equals("-") || 
         (ss.equals("/\\") || ss.equals("^") ||
          ss.equals("*") || ss.equals("/") || ss.equals("div") || 
          ss.equals("mod"))
      { Expression e1 = 
          parse_ATLbasic_expression(bc,pstart,i-1,
                                 entities,types);  // factor2
        Expression e2 = parse_ATLfactor_expression(bc,i+1,pend,entities,types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println(">> Parsed factor expression: " + ee);
          return ee;
        } 
      }     
    } 
    return parse_ATLfactor2_expression(bc,pstart,pend,entities,types); 
  } // reads left to right, not putting priorities on * above +
  // div is only for ATL. 

  public Expression parse_ATLfactor2_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { // System.out.println("Trying to parse factor2 expression from " + pstart + " to " + pend); 
    // case of  argument->op() and left->op(right)
    
    for (int i = pend-1; pstart < i; i--) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("->") && "(".equals(lexicals.get(i+2) + "") && 
          ")".equals(lexicals.get(pend) + ""))
      { String ss2 = lexicals.get(i+1).toString(); // must exist
        if (i + 3 == pend &&
            ("any".equals(ss2) || "size".equals(ss2) || "isDeleted".equals(ss2) ||
             "display".equals(ss2) || "min".equals(ss2) || "max".equals(ss2) ||
             "sum".equals(ss2) || "sort".equals(ss2) || "asSet".equals(ss2) || "asOrderedSet".equals(ss2) || 
             "sqrt".equals(ss2) || "sqr".equals(ss2) || 
             "asSequence".equals(ss2) ||
             "asArray".equals(ss2) ||
             "last".equals(ss2) || "first".equals(ss2) || "closure".equals(ss2) ||
             "subcollections".equals(ss2) || "reverse".equals(ss2) || "prd".equals(ss2) ||
             "tail".equals(ss2) || "front".equals(ss2) || "isEmpty".equals(ss2) ||
             "notEmpty".equals(ss2) || "toUpperCase".equals(ss2) || "flatten".equals(ss2) ||  
             "toLowerCase".equals(ss2) || "trim".equals(ss2) ||
             "isInteger".equals(ss2) || "toLong".equals(ss2) ||
             "toInteger".equals(ss2) || "isReal".equals(ss2) || "toReal".equals(ss2) ||
             "toBoolean".equals(ss2) || 
             "exp".equals(ss2) || "log".equals(ss2) || "log10".equals(ss2) || 
             "sin".equals(ss2) || "cos".equals(ss2) || "keys".equals(ss2) || "values".equals(ss2) ||  
             "tan".equals(ss2) || 
             "oclIsUndefined".equals(ss2) || 
             "oclIsInvalid".equals(ss2) || 
             "oclIsNew".equals(ss2) || 
             "oclType".equals(ss2) || 
             "copy".equals(ss2) ||  
             "unionAll".equals(ss2) ||
             "iterator".equals(ss2) ||
             "char2byte".equals(ss2) || 
             "byte2char".equals(ss2) ||  
             "intersectAll".equals(ss2) || 
             "concatenateAll".equals(ss2) ||
             "floor".equals(ss2) || "ceil".equals(ss2) || "round".equals(ss2) ||
             "succ".equals(ss2) || "pred".equals(ss2) || "ord".equals(ss2) ||
             "abs".equals(ss2) || "cbrt".equals(ss2) || "asin".equals(ss2) ||
             "acos".equals(ss2) || "atan".equals(ss2) || "isLong".equals(ss2) ||
             "sinh".equals(ss2) || "cosh".equals(ss2) || "tanh".equals(ss2)) )
        { Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) 
          { // System.err.println("!! Invalid unary -> expression at: " + showLexicals(pstart,pend)); 
            continue; 
          }
          UnaryExpression ue = new UnaryExpression(ss+ss2,ee2);
          // System.out.println(">> Parsed unary expression: " + ue); 
          return ue;   // This excludes additional operators to the ones listed above.  
        } 
        else if ("exists".equals(ss2) && 
                 i+5 < pend && // "(".equals(lexicals.get(i+2) + "") &&
                               "|".equals(lexicals.get(i+4) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->exists(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if (("exists1".equals(ss2) || "one".equals(ss2)) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->exists1(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#1",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("forAll".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->forAll(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("!",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("select".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->select(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("reject".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->reject(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|R",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("collect".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|C",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("unionAll".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|unionAll",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("intersectAll".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|intersectAll",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("sortedBy".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->sortedBy(v|...)

          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|sortedBy",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("selectMaximals".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|selectMaximals",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("selectMinimals".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->collect(v|...)
          Expression ee1 = parse_ATLexpression(bc+1,i+5,pend-1,entities,types);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|selectMinimals",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println(">> Parsed: " + be); 
          return be; 
        } 
        else if ("iterate".equals(ss2) && 
                 ";".equals(lexicals.get(i+4) + "") &&
                 ":".equals(lexicals.get(i+6) + "") && 
                 i+7 < pend)
        { // lft->iterate(var; acc: T=val | rgt)

          String bevar = lexicals.get(i+3) + "";
          String acc = lexicals.get(i+5) + "";

          for (int h = i+7; h < pend; h++)
          { String hs = lexicals.get(h) + "";
            if (hs.equals("="))
            { Type accType = parseType(i+7,h-1,entities,types);
              for (int g = h+1; g < pend; g++)
              { String gs = lexicals.get(g) + "";
                if (gs.equals("|"))
                { Expression lft = parse_ATLexpression(bc,h+1,g-1,entities,types);
                  Expression rgt = parse_ATLexpression(bc,g+1,pend-1,entities,types);
                  if (lft != null && rgt != null)
                  { BinaryExpression resbe = new BinaryExpression("->iterate", lft, rgt);
                    resbe.iteratorVariable = bevar;
                    resbe.accumulator = new Attribute(acc,accType,ModelElement.INTERNAL);
                    return resbe;
                  }
                }
              }
            }
          }
        }
        else if (pend == i+3) // && "(".equals(lexicals.get(i+2) + "") &&
                              //  ")".equals(lexicals.get(pend) + ""))  
        { Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          UnaryExpression be = new UnaryExpression(ss+ss2,ee2); 
          // System.out.println(">> Parsed new unary -> expression: " + be); 
          return be; 
        } 
        else if (i + 3 <= pend) // && "(".equals(lexicals.get(i+2) + "") &&
                                //   ")".equals(lexicals.get(pend) + "")) 
        // this should allow new Binary operators 
        { // System.out.println(">> Trying to parse at " + ss2); 
          Expression ee1 = parse_ATLexpression(bc+1,i+3,pend-1,entities,types); 
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BinaryExpression be = new BinaryExpression(ss+ss2,ee2,ee1); 
          // System.out.println(">> Parsed binary -> expression: " + be); 
          return be; 
        } 
      }     
    } 
    return parse_ATLbasic_expression(bc,pstart,pend,entities,types); 
  } // reads left to right, not putting priorities on * above +

  public Expression parse_ATLbasic_expression(int bc, int pstart, int pend, Vector entities, Vector types)
  { // System.out.println("Trying tp parse basic expression from " + pstart + " to " + pend); 

    if (pstart == pend)
    { String ss = lexicals.elementAt(pstart).toString(); 
      if (invalidBasicExp(ss))
      { System.err.println("!! Invalid ATL basic expression: " + ss); 
        return null; 
      } 

      if (isKeyword(ss))
      { System.err.println("!! Invalid ATL basic expression: keyword: " + ss); 
        return null;
      } 
  
      if ("OclUndefined".equals(ss))
      { BasicExpression resx = new BasicExpression("null"); 
        resx.umlkind = Expression.VALUE;  
        resx.setType(new Type("OclAny", null)); 
        return resx; 
      } 

      // replace "self" by "atlself" ??

      if ("thisModule".equals(ss))
      { String moduleClass = 
          Named.capitalise(Compiler2.atlModuleName);
        BasicExpression resx = 
          new BasicExpression(moduleClass);
        resx.umlkind = Expression.CLASSID;  
        return resx; 
      } 

      BasicExpression ee = // new BasicExpression(ss,0);   // (ss) surely?
                       new BasicExpression(ss,0); 
      Expression ef = ee.checkIfSetExpression(); 

      // if (ef instanceof BasicExpression) 
      // { ((BasicExpression) ef).setPrestate(ee.getPrestate()); } 

      // System.out.println("! Parsed basic expression: " + ss + " " + ee + " " + ef); 
      return ef;
    }

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Map".equals(lexicals.get(pstart) + "") && 
		"{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_ATLmap_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Sequence".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_ATLsequence_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Set".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_ATLset_expression(bc,pstart+1,pend,entities,types); } 

    if (pstart < pend && "-".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_ATLadditive_expression(bc,pstart+1,pend,entities,types); 
      if (arg == null) 
      { // System.err.println("! Not ATL additive expression: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 
      return new UnaryExpression("-",arg); 
    } // likewise for "not"

    if (pstart + 1 < pend && ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+1) + ""))
    { Expression op = parse_ATLbasic_expression(bc,pstart,pstart,entities,types); 
      
      Vector ve = parse_ATLfe_sequence(bc,pstart+2,pend-1,entities,types); 
      if (ve != null && op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op; 

        if (Expression.isFunction(beop.data))
        { beop.umlkind = Expression.FUNCTION; } 
        else 
        { beop.setIsEvent(); } 
 
        beop.setParameters(ve); 
        // System.out.println(">> Parsed call expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart + 2 < pend && ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+2) + "") && 
        ("" + lexicals.get(pstart + 1)).charAt(0) == '.')
    { Expression objref = parse_ATLbasic_expression(bc,pstart,pstart,entities,types); 
      String opstring = lexicals.get(pstart + 1) + ""; 
      String opstr = opstring.substring(1,opstring.length()); 

      Vector ve = parse_ATLfe_sequence(bc,pstart+3,pend-1,entities,types); 
      if (ve != null && objref != null && objref instanceof BasicExpression) // must be
      { BasicExpression beopref = (BasicExpression) objref; 
        BasicExpression beop = new BasicExpression(opstr); 

        if (Expression.isFunction(opstr))
        { beop.umlkind = Expression.FUNCTION; } 
        else 
        { beop.setIsEvent(); } 
 
        beop.setParameters(ve);
        beop.setObjectRef(beopref);  
        // System.out.println(">> Parsed call expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && "]".equals(lexicals.get(pend) + "") && 
        "[".equals(lexicals.get(pstart+1) + ""))
    { Expression op = parse_ATLbasic_expression(bc,pstart,pstart,entities,types); 
      Expression arg = parse_ATLfactor_expression(bc,pstart+2,pend-1,entities,types); 
      if (arg != null && op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        // System.out.println(">> Parsed array expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && "]".equals(lexicals.get(pend-1) + "") && 
        "[".equals(lexicals.get(pstart+1) + ""))  // be[arrind].be2
    { String tail = lexicals.get(pend) + ""; 
      if (tail.charAt(0) == '.')
      { tail = tail.substring(1,tail.length()); } 
      Expression op = parse_ATLbasic_expression(bc,pstart,pstart,entities,types); 
      Expression arg = parse_ATLfactor_expression(bc,pstart+2,pend-2,entities,types); 
      if (arg == null) 
      { System.err.println("!! Invalid ATL array argument: " + showLexicals(pstart+2,pend-2)); } 
      else if (op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        BasicExpression te = new BasicExpression(tail); 
        BasicExpression ef = (BasicExpression) te.checkIfSetExpression(); 
        if (ef != null) 
        { ef.setInnerObjectRef(beop); }  
        // System.out.println(">> Parsed array 1 expression: " + ef);
        return ef;  
      } 
      // return null; 
    }       

  if (pstart + 1 < pend && ")".equals(lexicals.get(pend) + "") && 
    !("(".equals(lexicals.get(pstart) + "")))
  { for (int i = pstart + 1; i < pend; i++)
    { String strs = lexicals.get(i) + "";
      if ("(".equals(strs))
      { Expression op = parse_ATLbasic_expression(bc, pstart, i-1,entities,types);
        if (op != null && op instanceof BasicExpression)
        { Vector ve = parse_ATLfe_sequence(bc,i+1,pend-1,entities,types);
          if (ve != null)
          { BasicExpression beop = (BasicExpression) op;
            beop.setIsEvent();
            beop.setParameters(ve);
            // System.out.println(">> Parsed extended call expression " + beop);
            return beop;
          }
        }
      }
    }
  }


   String ttail = lexicals.get(pend) + ""; 
   if (ttail.charAt(0) == '.')
   { ttail = ttail.substring(1,ttail.length());  
     BasicExpression op = new BasicExpression(ttail); 

     Expression arg = parse_ATLexpression(bc,pstart,pend-1,entities,types);
     if (arg != null)
     { op.setObjectRef(arg); 
       return op; 
     } 
   } 
       
   return parse_ATLbracketed_expression(bc,pstart,pend,entities,types); 
 } 

  public Expression parse_ATLbracketed_expression(int bcount, int pstart, int pend, Vector entities, Vector types)
  { if (lexicals.size() > 2 &&
        lexicals.get(pstart).toString().equals("(") &&
        lexicals.get(pend).toString().equals(")"))
    { Expression eg = parse_ATLexpression(bcount+1, pstart+1, pend-1,entities,types);
      // System.out.println(">> parsed bracketed expression: " + eg);  
      if (eg != null)
      { eg.setBrackets(true); }
      return eg; 
    }
    return null; 
  } // also case of pstart = (, pend-1 = ), pend.charAt(0) == '.'




  public Expression parse_ATLset_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_ATLfe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,false); 
    // System.out.println("!! Parsed set expression: " + res); 
    return res; 
  } 

  public Expression parse_ATLsequence_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_ATLfe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    // System.out.println("!! Parsed sequence expression: " + res); 
    return res; 
  } 


  public Vector parse_ATLfe_sequence(int bc, int st, int ed, Vector entities, Vector types)
  { Vector res = new Vector(); 
    // if (st == ed) // just one basic exp
    // { Expression elast = parse_basic_expression(bc,st,ed);
    //   res.add(elast); 
    //   return res; 
    // } 
    if (st > ed)
    { return res; } 
    return getATL_FESeq(bc,st,ed,entities,types); 

    // Only commas at the top level of the expression are wanted! 
  }  

  private Vector getATL_FESeq(int bc, int st, int en, Vector entities, Vector types)
  { // Scans the lexicals inside the outer brackets of 
    // a potential fe-sequence

     int bcnt = 0; int sqbcnt = 0; int cbcnt = 0;
     Vector res = new Vector();
     int st0 = st;
     String buff = "";
     for (int i = st; i <= en; i++)
     { String lx = lexicals.get(i) + "";
       buff = buff + lx;
       if ("(".equals(lx)) { bcnt++; }
       else if (")".equals(lx)) 
       { bcnt--;
         if (bcnt < 0) { return null; }
       }
       else if ("[".equals(lx)) { sqbcnt++; }
       else if ("]".equals(lx)) { sqbcnt--; }
       else if ("{".equals(lx)) { cbcnt++; }
       else if ("}".equals(lx)) { cbcnt--; }
       else if (",".equals(lx))
       { if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
         { // top-level ,
           Expression exp = parse_ATLadditive_expression(bc,st0,i-1,entities,types);
           if (exp == null) 
           { // System.out.println("!! Invalid ATL additive exp: " + buff);
             return null;
           }
           res.add(exp);
           st0 = i + 1;
           buff = "";
         }
       }
     }
       // at the end:
     if (bcnt == 0 && sqbcnt == 0 && cbcnt == 0)
     { Expression exp = parse_ATLadditive_expression(bc,st0,en,entities,types);
       if (exp == null) 
       { // System.out.println("Invalid ATL additive exp: " + buff);
         return null;
       }
       res.add(exp);
     }
     else
     { System.err.println("!! ERROR: Not ATL factor expression sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }


  public Statement parseStatement()
  { Vector entities = new Vector(); 
    Vector types = new Vector(); 
    int en = lexicals.size(); 
    return parseStatement(0,en-1,entities,types); 
  } 

  public boolean checkBrackets()
  { int en = lexicals.size(); 
    return checkBrackets(0,en-1); 
  } 
    
  public boolean checkBrackets(int st, int en)
  { int ob = 0;
    int cb = 0; 
    int osqb = 0;
    int csqb = 0;
    int ocb = 0;
    int ccb = 0;
    String mostrecentopen = null;  
	 
    for (int i = st; i <= en; i++)
    { String lex = lexicals.get(i) + ""; 
      if ("(".equals(lex))
      { ob++; 
        mostrecentopen = "("; 
      }
      else if (")".equals(lex))
      { cb++; 

        if (cb > ob)
        { System.err.println("!! Too many ) brackets (opening: " + ob + ", closing: " + cb + ") here->   " + showLexicals(st,i)); }

        if (mostrecentopen != null && !mostrecentopen.equals("("))
        { System.err.println("!! Error: closing " + mostrecentopen + " with ) here->   " + showLexicals(st,i)); 
        }
        else 
        { mostrecentopen = null; } 
      }
      else if ("[".equals(lex))
      { osqb++; 
        mostrecentopen = "["; 
      }
      else if ("]".equals(lex))
      { csqb++; 

        if (csqb > osqb)
        { System.err.println("!! Too many ] brackets (opening: " + osqb + ", closing: " + csqb + ") here->   " + showLexicals(st,i)); }

        if (mostrecentopen != null && !mostrecentopen.equals("["))
        { System.err.println("!! Error: closing " + mostrecentopen + " with ] here->   " + showLexicals(st,i)); }
        else 
        { mostrecentopen = null; } // [ closed with ]
      }
      else if ("{".equals(lex))
      { ocb++; 
        mostrecentopen = "{"; 
      }
      else if ("}".equals(lex))
      { ccb++; 
	
        if (ccb > ocb)
        { System.err.println("!! Too many } brackets (opening: " + ocb + ", closing: " + ccb + ") here->   " + showLexicals(st,i)); }

        if (mostrecentopen != null && !mostrecentopen.equals("{"))
        { System.err.println("!! Error: closing " + mostrecentopen + " with } here->   " + showLexicals(st,i)); }
        else 
        { mostrecentopen = null; } // { correctly closed
        
      }
    }

    if (ob == cb && osqb == csqb && ocb == ccb) { return true; }

    if (ob > cb)
    { System.err.println("!! Too many ( brackets (opening: " + ob + ", closing: " + cb + ") in   " + showLexicals(st,en)); }
    else if (cb > ob) 
    { System.err.println("!! Too many ) brackets (opening: " + ob + ", closing: " + cb + ") in   " + showLexicals(st,en)); }

    if (osqb > csqb)
    { System.err.println("!! Too many [ brackets (opening: " + osqb + ", closing: " + csqb + ") in   " + showLexicals(st,en)); }
    else if (csqb > osqb) 
    { System.err.println("!! Too many ] brackets (opening: " + osqb + ", closing: " + csqb + ") in   " + showLexicals(st,en)); }

    if (ocb > ccb)
    { System.err.println("!! Too many { brackets (opening: " + ocb + ", closing: " + ccb + ") in   " + showLexicals(st,en)); }
    else if (ccb > ocb) 
    { System.err.println("!! Too many } brackets (opening: " + ocb + ", closing: " + ccb + ") in   " + showLexicals(st,en)); }

    return false; 
  } 

  public boolean balancedBrackets()
  { int en = lexicals.size(); 
    return balancedBrackets(0,en-1); 
  } 

  public boolean balancedBrackets(int st, int en)
  { int ob = 0;
    int cb = 0; 
    int osqb = 0;
    int csqb = 0;
    int ocb = 0;
    int ccb = 0;
    String mostrecentopen = null; 
    boolean instring = false;  
	 
    for (int i = st; i <= en; i++)
    { String lex = lexicals.get(i) + ""; 
      if ("\"".equals(lex)) 
      { if (instring) 
        { instring = false; } 
        else 
        { instring = true; } 
      } 
      else if ("(".equals(lex) && !instring)
      { ob++; 
        mostrecentopen = "("; 
      }
      else if (")".equals(lex) && !instring)
      { cb++; 
        if (cb > ob)
        { /* System.err.println("!! Too many ) brackets (opening: " + ob + ", closing: " + cb + ") in   " + showLexicals(st,en)); */ 
          return false; 
        } 
      }
      else if ("[".equals(lex) && !instring)
      { osqb++; 
        mostrecentopen = "["; 
      }
      else if ("]".equals(lex) && !instring)
      { csqb++; 
        if (csqb > osqb)
        { /* System.err.println("!! Too many ] brackets (opening: " + osqb + ", closing: " + csqb + ") in   " + showLexicals(st,en)); */ 
          return false; 
        } 
      }
      else if ("{".equals(lex) && !instring)
      { ocb++; 
        mostrecentopen = "{"; 
      }
      else if ("}".equals(lex) && !instring)
      { ccb++; 
        if (ccb > ocb)
        { /* System.err.println("!! Too many } brackets (opening: " + ocb + ", closing: " + ccb + ") in   " + showLexicals(st,en)); */ 
          return false; 
        }
	}
    }

    if (ob == cb && osqb == csqb && ocb == ccb && !instring) 
    { return true; }

    return false; 
  } 

  public static String isKeywordOrPart(String st, String[] mess)
  { 

    if (st.length() < 2) 
    { return null; } 

    /* if ("::".equals(st))
    { mess[0] = "Scope declaration in usecase postcondition: \n:: cond => pred;\nor\nEntityName:: cond => pred;"; 
      return "::"; 
    } 

    if ("=>".equals(st))
    { mess[0] = "Logical implication, same as  implies : \ncond => pred"; 
      return "implies"; 
    } */ 


    if ("if".equals(st)) 
    { mess[0] = "Conditional expression: if expr then expr1 else expr2 endif\nor statement: if expr then statmt1 else statmt2"; 
      return "if"; 
    } 

    if ("then".startsWith(st)) 
    { mess[0] = "then part of if-expression or if-statement"; 
      return "then"; 
    }

 
    if ("while".startsWith(st)) 
    { mess[0] = "Unbounded loop statement: while expr do statement\nCan only be used in an activity"; 
      return "while"; 
    }

    if ("for".startsWith(st)) 
    { mess[0] = "Bounded loop statement: for variable : collection do statement\nThe variable must not already be defined\nThis statement can only be used in an activity"; 
      return "for"; 
    }

    if ("var".startsWith(st)) 
    { mess[0] = "Declaration/creation statement: var variable : Type\nCan only be used in an activity"; 
      return "var"; 
    } 

    if ("frozen".startsWith(st)) 
    { mess[0] = "Constant-valued attribute:\n" +
        "  attribute attr frozen : Type"; 
      return "frozen"; 
    } 
 
    if ("operation".startsWith(st)) 
    { mess[0] = "Operation declaration:\noperation name(parameters) : Type\n" + 
                "pre: expression\npost: expression\nactivity: statement;\n\n" + 
                "Or with a generic type parameter name T:\n" + 
                "operation name<T>(parameters) : Type\n" + 
                "pre: expression\npost: expression\nactivity: statement;\n\n" + 
        "Operation overloading should be avoided.\n"; 
      return "operation"; 
    } 
    
    if ("query".startsWith(st)) 
    { mess[0] = "Query operation declaration, returning a result value:\n" + 
                "query name(parameters) : Type\n" + 
                "pre: expression\npost: expression\nactivity: statement;\n\n" + 
                "Or with a generic type parameter name T:\n" + 
                "query name<T>(parameters) : Type\n" + 
                "pre: expression\npost: expression\nactivity: statement;\n";; 
      return "query"; 
    }
 
    if ("pre".startsWith(st)) 
    { mess[0] = "Operation precondition, eg: pre: true\nOr usecase precondition, eg: precondition par > 0;\n"; 
      return "pre: expr   or    precondition expr;"; 
    } 
 

    if ("let".startsWith(st))
    { mess[0] = "let expression to define local variable:\n let x : Type = init in (expr)"; 
      return "let"; 
    } 
 
    if ("activity".startsWith(st)) 
    { mess[0] = "Operation or usecase code, activity: statement;"; 
      return "activity:"; 
    }
 
    if ("class".startsWith(st)) 
    { mess[0] = "Class declaration, eg: class Name { ... }"; 
      return "class"; 
    } 

    if ("abstract".startsWith(st)) 
    { mess[0] = "Abstract class declaration, eg: abstract class Name { ... }"; 
      return "abstract class"; 
    } 

    if ("usecase".startsWith(st)) 
    { mess[0] = "usecase declaration, eg: usecase name : Type { ... }"; 
      return "usecase"; 
    } 



    if ("attribute".startsWith(st)) 
    { mess[0] = "attribute declaration, e.g.: attribute name : Type; or\n" + 
           "attribute name identity : String;\n" +  
           "attribute name frozen : String;\n"; 
      return "attribute"; 
    }

    if ("assert".startsWith(st)) 
    { mess[0] = "assert statement, e.g.: assert condition do message;"; 
      return "assert"; 
    }

    if (st.length() > 2)
    { if ("else".startsWith(st)) 
      { mess[0] = "else part of if-expression or if-statement"; 
        return "else"; 
      }

      if ("post".startsWith(st)) 
      { mess[0] = "Operation postcondition, e.g.: post: true\n" + 
                "or post: result = expression"; 
        return "post:"; 
      }

      if ("parameter".startsWith(st)) 
      { mess[0] = "usecase parameter, e.g.: parameter name : Type;"; 
        return "parameter"; 
      } 

      if ("oppositeOf".startsWith(st)) 
      { mess[0] = "oppositeOf clause in reference,\n" + 
          "e.g: reference children [*] ordered : Person oppositeOf parent;" + 
          "The matching declaration\n" + 
          "  reference parent : Person;\n" + 
          "is also necessary."; 
        return "oppositeOf"; 
      } 

    } 

    if (st.length() > 3) 
    { if ("reference".startsWith(st)) 
      { mess[0] = "reference declaration, eg: reference name : Type;\nType is a class type or collection (of class element type)\nGeneral syntax is:\n" + 
        "reference role2 [cardinality] [stereotypes] : Entity2 [opp];\n" + 
        "E.g: reference children [*] ordered : Person oppositeOf parent;";  
        return "reference"; 
      }

      if ("result".startsWith(st)) 
      { mess[0] = "result variable of usecase/query/operation with non-void result type\nEg., result = true"; 
        return "result"; 
      }

      if ("return".startsWith(st)) 
      { mess[0] = "return statement, eg: return result\nEnds control flow in the current activity branch\nCan only be used in an activity"; 
        return "return"; 
      }

      if ("break".startsWith(st)) 
      { mess[0] = "break statement, eg: break\nEnds control flow in the current activity branch\nCan only be used in an activity"; 
        return "break"; 
      }

      if ("continue".startsWith(st)) 
      { mess[0] = "continue statement, eg: continue\nJump to next iteration of enclosing loop\nCan only be used in an activity"; 
        return "continue"; 
      }

      if ("error".startsWith(st)) 
      { mess[0] = "error statement, eg: error createOclException()\nRaises an exception to be caught by a catch statement\nCan only be used in an activity"; 
        return "error"; 
      }

      if ("catch".startsWith(st)) 
      { mess[0] = "catch statement, eg: catch (x : OclException) do return null\nCatches and handles an exception\nCan only be used in a try statement"; 
        return "catch"; 
      }

      if ("finally".startsWith(st)) 
      { mess[0] = "finally clause of try statement, eg: finally return null\nAlways ends processing of try\nRegardless of previous catches"; 
        return "finally"; 
      }

      if ("lambda".startsWith(st))
      { mess[0] = "lambda expression to define function:\n lambda x : SourceType in expr"; 
        return "lambda"; 
      } 


    } 

    if (st.length() > 4)
    { 
      if ("->size".startsWith(st))
      { mess[0] = "arg->size() operator on strings, maps, sets, sequences\n" + 
          "Returns number of arg elements"; 
        return "arg->size()"; 
      }

      if ("->front".startsWith(st))
      { mess[0] = "arg->front() operator on strings, sequences\n" + 
          "Returns  arg.subrange(1,(arg->size())-1)"; 
        return "arg->front()"; 
      }

      if ("->tail".startsWith(st))
      { mess[0] = "arg->tail() operator on strings, sequences\n" + 
          "Returns  arg.subrange(2,arg->size())"; 
        return "arg->tail()"; 
      }

      if ("->sort".startsWith(st))
      { mess[0] = "arg->sort() operator on sets, sequences\n" + 
          "Returns sorted sequence of arg elements"; 
        return "arg->sort()"; 
      }

      if ("->trim".startsWith(st))
      { mess[0] = "arg->trim() operator on strings\n" + 
          "Copy of arg with leading and trailing whitespace removed"; 
        return "arg->trim()"; 
      }

      if ("->isMatch".startsWith(st))
      { mess[0] = "arg->isMatch(patt) operator on strings\n" + 
          "True if arg matches against pattern patt"; 
        return "arg->isMatch(patt)"; 
      }

      if ("->allMatches".startsWith(st))
      { mess[0] = "arg->allMatches(patt) operator on strings\n" + 
          "Returns sequence of matches in arg for pattern patt"; 
        return "arg->allMatches(patt)"; 
      }

      if ("->reverse".startsWith(st))
      { mess[0] = "arg->reverse() operator on strings, sequences\n" + 
          "Returns arg in reverse order"; 
        return "arg->reverse()"; 
      }

      if ("repeat".startsWith(st))
      { mess[0] = "repeat statement until expression\n" +  
                  "loop statement\n"; 
        return "repeat statement until expression"; 
      }


      if ("->restrict".startsWith(st))
      { mess[0] = "arg->restrict(keys) operator on maps.\n" + 
          "Returns submap of arg: the elements with key in keys"; 
        return "arg->restrict(keys)"; 
      }

      if ("->antirestrict".startsWith(st))
      { mess[0] = "arg->antirestrict(keys) operator on maps.\n" + 
          "Returns submap of arg: elements with key not in keys"; 
        return "arg->antirestrict(keys)"; 
      }

      if ("->first".startsWith(st))
      { mess[0] = "arg->first() operator on non-empty strings, sequences\n" + 
          "Returns  arg->at(1)"; 
        return "arg->first()"; 
      }

      if ("->last".startsWith(st))
      { mess[0] = "arg->last() operator on non-empty strings, sequences\n" + 
          "Returns  arg->at(arg->size())"; 
        return "arg->last()"; 
      }

      if ("->exists".startsWith(st))
      { mess[0] = "->exists or ->exists1 operators on sets, sequences\n" + 
          "Can also be used to create concrete class C instances: C->exists(x|predicate)"; 
        return "arg1->exists(x|Predicate) or arg1->exists1(x|Predicate)"; 
      }

      if ("->forAll".startsWith(st))
      { mess[0] = "->forAll operator on sets, sequences\n" + 
          "true if all elements of LHS satisfies predicate"; 
        return "arg1->forAll(x|Predicate)"; 
      }

      if ("->union".startsWith(st))
      { mess[0] = "Union (merge, concatenate, override) operator on sets, sequences and maps"; 
        return "arg1->union(arg2)"; 
      }

      if ("->intersection".startsWith(st))
      { mess[0] = "Intersection operator on sets, sequences and maps"; 
        return "arg1->intersection(arg2)"; 
      }

      if ("->includes".startsWith(st) || 
          "->includesAll".startsWith(st) || 
          "->including".startsWith(st))
      { mess[0] = "arg1->includes(elem) operator on sets, sequences. true if elem is in arg1\n" + 
          " arg1->includesAll(arg2)  true if all arg2 elements are in arg1\n" + 
          " arg1->including(elem)  The same as arg1->union(Set{elem})"; 
        return "arg1->includes(elem)  or  arg1->includesAll(arg2)  or  arg1->including(elem)"; 
      }

      if ("->excludes".startsWith(st) ||
          "->excludesAll".startsWith(st) ||  
          "->excluding".startsWith(st) || 
          "->excludingFirst".startsWith(st) ||
          "->excludingAt".startsWith(st))
      { mess[0] = "arg1->excludes(elem) operator on sets, sequences. true if elem is not in arg1\n" + 
          " arg1->excludesAll(arg2)  true if arg1, arg2 have no common elements\n" + 
          " arg1->excluding(elem)  The same as arg1 - Set{elem}\n" + 
          " arg1->excludingFirst(elem)  Removes first elem from sequence arg1\n" + 
          " arg1->excludingAt(int)  Removes sequence element at index ind"; 
        return "arg1->excludes(elem),  arg1->excludesAll(arg2),  arg1->excluding(elem), arg1->excludingFirst(elem) or arg1->excludingAt(int)"; 
      }
    
      if ("->select".startsWith(st))
      { mess[0] = "Selection (filter) operator on sets, sequences and maps.\n" + 
          "col->select(x | P)  returns collection/map of same type as col.\n" + 
          "col->selectMaximals(e), col->selectMinimals(e)  return subcollections of col for which e is maximal/minimal";  
        return "arg1->select(x | Predicate)"; 
      }

      if ("->reject".startsWith(st))
      { mess[0] = "Rejection (negative filter) operator on sets, sequences and maps.\n" + 
          "col->reject(x | P)  returns collection/map of same type as col.";  
 
        return "arg1->reject(x | Predicate)"; 
      }

      if ("->collect".startsWith(st))
      { mess[0] = "Collection operator on sets and sequences always returns a sequence\n" + 
          "so that duplicated expression values are preserved.\n" + 
          "col->collect(x | e)  returns sequence of all e values for x in col.\n" + 
          "m->collect(x | e)  for Map m returns map with keys k of m, mapped to e value of x = m[k]\n"; 
        return "arg1->collect(x | Expression)"; 
      }
  
      if ("->max".startsWith(st))
      { mess[0] = "Maximum element operator on sets & sequences of numbers/strings"; 
        return "arg->max()"; 
      }

      if ("->min".startsWith(st))
      { mess[0] = "Minimum element operator on sets & sequences of numbers/strings"; 
        return "arg->min()"; 
      }

      if ("->sum".startsWith(st))
      { mess[0] = "Summation operator on sets & sequences of numbers/strings"; 
        return "arg->sum()"; 
      }

      if ("->prd".startsWith(st))
      { mess[0] = "Product operator on sets & sequences of numbers"; 
        return "arg->prd()"; 
      }

      if ("->pow".startsWith(st))
      { mess[0] = "Power operator on numbers"; 
        return "arg->pow(exponent)"; 
      }

      if ("->gcd".startsWith(st))
      { mess[0] = "GCD operator on integers"; 
        return "arg->gcd(arg2)"; 
      }

      if ("->display".startsWith(st))
      { mess[0] = "Display operator on values. Used in statements\n" + 
             "execute v->display()\n"; 
        return "execute arg->display()"; 
      }

      if ("->apply".startsWith(st))
      { mess[0] = "Function application on functions f : Function(S,T)"; 
        return "f->apply(x)"; 
      } 

      if ("->asSet".startsWith(st))
      { mess[0] = "Converts collection to a set"; 
        return "x->asSet()"; 
      }

      if ("->asSequence".startsWith(st))
      { mess[0] = "Converts collection to sequence"; 
        return "x->asSequence()"; 
      }

      if ("->asBag".startsWith(st))
      { mess[0] = "Converts collection to Bag (a sorted sequence)"; 
        return "x->asBag()"; 
      }

      if ("->asOrderedSet".startsWith(st))
      { mess[0] = "Converts collection to OrderedSet (a sequence without duplicate elements)"; 
        return "x->asOrderedSet()"; 
      }

      if ("->iterate".startsWith(st))
      { mess[0] = "Iterate over x in col, combining elements: \n" + 
          "col->iterate(x; acc = init | arg)\n" + 
          "at each step: acc := arg(x,acc)\n" + 
          "This is only supported for Python and Java8"; 
        return "col->iterate(x; acc = init | arg)"; 
      }
    } 

    if (st.length() > 2)
    { if ("stereotype".startsWith(st)) 
      { mess[0] = "usecase stereotype, eg: stereotype private;";  
        return "stereotype"; 
      } 
 
      if ("static".startsWith(st)) 
      { mess[0] = "static attribute or operation, i.e., of class scope. Eg:\n" + "static attribute nobjs : int;\n" + 
          "static query pi() : double\npre: true post: result = 3.14159265\n\n" + 
          "Static operations are not inherited. Define them in concrete classes where possible.\n"; 
        return "static"; 
      } 

      if ("implements".startsWith(st))
      { mess[0] = "class implements one or more interfaces, eg.,\n" + 
                  "class A implements IA, IB { ... }"; 
        return "implements"; 
      } 

      if ("invariant".startsWith(st))
      { mess[0] = "invariant constraint of class, eg.,\n" + 
                  "invariant att1 = att2;"; 
        return "invariant"; 
      } 

      if ("String".startsWith(st)) 
      { mess[0] = "String type String. Empty string is \"\"\n" + 
          "Operators include:  s->size()  s1 + s2  s1->indexOf(s2)  s->at(index)\n" + 
          "s->display()  s->tail()  s->first()\n" + 
          "s.subrange(i,j)  s.subrange(i)  s.setAt(i,ch)\n" + 
          "s->isMatch(pattern)  s->allMatches(patt)  s->trim()\n"; 
        return "String"; 
      }
 
      if ("int".startsWith(st)) 
      { mess[0] = "32-bit integer type int,\n" + 
          "ranges from -(2->pow(31)) to 2->pow(31)-1\n" + 
          "Operators include: x mod y  x div y\n" + 
          "and usual arithmetic operators * / - + < <= > >= = /= etc"; 
        return "int"; 
      } 

      if ("Ref(".startsWith(st)) 
      { mess[0] = "Reference/pointer type Ref(T)\n" + 
          "The operator !x is used to dereference x : Ref(T)\n" + 
          "and !x has type T."; 
        return "Ref"; 
      } 

      if ("includes".startsWith(st)) 
      { mess[0] = "usecase included in another, eg: includes subroutine;"; 
        return "includes"; 
      } 

      if ("interface".startsWith(st)) 
      { mess[0] = "interface declaration:\n  interface P { attributes, operations }"; 
        return "interface"; 
      } 

      if ("true".startsWith(st)) 
      { mess[0] = "true value of boolean type"; 
        return "true"; 
      }

      if ("try".startsWith(st)) 
      { mess[0] = "try statement, eg: try x := y/z catch (e : ArithmeticException) do return null\nExecution with exception handling\nCan only be used in an activity"; 
        return "try";
      }

    
      if ("boolean".startsWith(st)) 
      { mess[0] = "Boolean-values type boolean,\n" + 
                  "values are true and false\n" + 
                  "Operators include:  b1 & b2  b1 or b2  not(b)  b1 => b2"; 
        return "boolean"; 
      } 

      if ("identity".startsWith(st)) 
      { mess[0] = "Identity attributes - String-valued attributes whose values\n"  + 
             "uniquely define the objects of its class: attribute id identity : String;"; 
        return "identity"; 
      } 
    } 

    if ("false".startsWith(st)) 
    { mess[0] = "false value of boolean type"; 
      return "false"; 
    }
 
    if ("null".startsWith(st)) 
    { mess[0] = "null object/value"; 
      return "null"; 
    } 

    if ("self".startsWith(st)) 
    { mess[0] = "self object - should not be used as a variable"; 
      return "self"; 
    } 

    if ("super".startsWith(st)) 
    { mess[0] = "super object - should not be used as a variable"; 
      return "super"; 
    } 

    if (st.length() > 2)
    { if ("double".startsWith(st)) 
      { mess[0] = "Real-values type, from -1.7976931348623157*(10->pow(308)) to 1.7976931348623157*(10->pow(308))\n" + 
          "Operators include: d->sqrt()  d1->pow(d2)  d->exp()  d->log()\n" + 
          "and usual arithmetic operators * / - + < <= > >= = /= etc"; 
        return "double"; 
      } 

      if ("void".startsWith(st)) 
      { mess[0] = "void type, used as operation/usecase return type to indicate there is no return value"; 
        return "void"; 
      } 

      if ("Sequence".startsWith(st)) 
      { mess[0] = "Sequence type, eg., Sequence(String), Sequence(boolean),\n" + 
          "Sequence(double) or Sequence(C) for class C.\n" + 
          "But Sequence or Sequence(OclAny) is bad practice & non-portable.\n" + 
          "Operators include:  sq->size()  sq->at(index)  sq1->union(sq2)  sq->append(x)\n" + 
           "sq->select(x|P)  sq->collect(x|P)  sq->forAll(x|P)\n" +  
          "Literal sequences are written as Sequence{ x, y, z }\n"; 
        return "Sequence(Type)"; 
      }
 
      if ("Set".startsWith(st)) 
      { mess[0] = "Set type, e.g., Set(String), Set(boolean), \n" + 
          "Set(double) or Set(C) for class C.\n" + 
          "But Set or Set(OclAny) is bad practice & non-portable.\n" + 
          "Operators include:  st->size()  st->includes(elem)  st1->union(st2)\n" + 
           "st->select(x|P)  st->collect(x|P)  st->forAll(x|P)\n" + 
          "Literal sets are written as Set{ x, y, z }\n"; 

        return "Set(Type)"; 
      }

      if ("SortedSet".startsWith(st)) 
      { mess[0] = "SortedSet type, e.g., SortedSet(String), SortedSet(int), \n" + 
          "SortedSet(double).\n" + 
          "But SortedSet by itself is bad practice & non-portable.\n" + 
          "Operators include:  st->size()  st->includes(elem)  st1->union(st2) (merge)\n" + 
           "st->select(x|P)  st->collect(x|P)  st->forAll(x|P)\n" + 
          "Literal sorted sets are written as SortedSet{ x, y, z }\n"  + 
          "!! This type is only implemented in C++, Java7 and Java8 !!\n"; 

        return "SortedSet(Type)"; 
      }

      if ("SortedMap".startsWith(st)) 
      { mess[0] = "SortedMap type, e.g., SortedMap(String,int)\n" + 
          "Operators include:  m->size()  m->at(key)  m1->union(m2)\n" + 
          "m->select(x|P)  m->keys()  m->values()\n" + 
          "Literal sorted maps are written as SortedMap{ x |-> y, a |-> b }\n" + 
          "!! SortedMap(S,T) is available in C++, Java7, Java8 only.\n"; 
        return "SortedMap(String,Type)"; 
      } 
 
      if ("Map".startsWith(st)) 
      { mess[0] = "Map type, e.g., Map(String,int)\n" + 
          "Operators include:  m->size()  m->at(key)  m1->union(m2)\n" + 
          "m->select(x|P)  m->keys()  m->values()\n" + 
          "Literal maps are written as Map{ x |-> y, a |-> b }\n" + 
          "SortedMap(S,T) is available in C++, Java7, Java8 only.\n"; 
        return "Map(String,Type)"; 
      } 

      if ("Function".startsWith(st)) 
      { mess[0] = "Function type, eg., Function(String,int)\n Operators include:  lambda x : S in T\n f->apply(x)\n"; 
        return "Function(String,Type)"; 
      } 

      if ("enumeration".startsWith(st)) 
      { mess[0] = "Enumeration, eg., enumeration Colour { literal red; literal blue; literal green; }"; 
        return "enumeration"; 
      } 

      if ("datatype".startsWith(st)) 
      { mess[0] = "New basic type, eg., datatype DateTime;\n" + 
                  "or alias type, eg.:  datatype Float = double;\n"; 
        return "datatype"; 
      } 

      if ("endif".startsWith(st)) 
      { mess[0] = "endif ends conditional expression  if e then e1 else e2 endif"; 
        return "endif"; 
      } 

      if ("OclType".startsWith(st) || 
          "OclAttribute".startsWith(st) || 
          "OclOperation".startsWith(st) ||
          "OclAny".startsWith(st) ||
          "OclProcess".startsWith(st) || 
          "OclRandom".startsWith(st) || 
          "OclFile".startsWith(st) || 
          "OclException".startsWith(st) ||
          "OclIterator".startsWith(st) ||
          "OclDatasource".startsWith(st))
      { mess[0] = "OclAny -- universal type.\n" + 
                  "OclType -- metatype of types. Requires ocltype.km3 library\n" + 
                  "OclAttribute -- metatype of fields. Requires ocltype.km3 library\n" + 
                  "OclOperation -- metatype of methods. Requires ocltype.km3 library\n" + 
                  "OclProcess -- type of processes. Requires oclprocess.km3 library\n" + 
                  "OclRandom -- random number generator. Needs oclrandom.km3\n" + 
                  "OclFile -- type of files. Needs oclfile.km3\n" + 
                  "OclIterator -- type of iterators. Needs ocliterator.km3\n" + 
                  "OclException -- type of exceptions. Needs oclexception.km3\n" +  
                  "OclDatasource -- type of relational databases and remote datasources.\n  Needs ocldatasource.km3, oclfile.km3, ocliterator.km3, ocldate.km3\n"; 
        return "Ocl library type"; 
      } 
    } 

    if (st.length() > 6)
    { if ("extends".startsWith(st))
      { mess[0] = "Class inheritance, eg., class User extends Person {"; 
        return "extends"; 
      }
 
      if ("extendedBy".startsWith(st))
      { mess[0] = "Use case extension, eg., extendedBy errorCase;\nDeclares errorCase as optional extra functionality of this use case\n"; 
        return "extendedBy"; 
      }

      if ("execute".startsWith(st))
      { mess[0] = "Execute expression as statement. Eg., execute (x->display())\n" + 
          "This also specifies direct updates of collections:\n" + 
          " execute (x : col)\n" + 
          " execute (x /: col)\n" + 
          " execute (x <: col)\n" +
          " execute (x /<: col)\n" +
          " execute col[i]->isDeleted()\n"; 
        return "execute"; 
      }
    } 



    if ("long".startsWith(st)) 
    { mess[0] = "64-bit integer type long,\n" + 
          "ranges from -(2->pow(63)) to (2->pow(63)-1)\n" +  
          "Operators include: x mod y  x div y\n" + 
          "and usual arithmetic operators * / - + < <= > >= = /= etc"; 
 
      return "long"; 
    } 
    

    if ("subrange".startsWith(st)) 
    { mess[0] = "subrange operator on sequences and strings\n"  + 
           "  ss.subrange(i,j) is subrange of ss starting at position i\n" + 
           "  and ending at position j. Positions start at 1\n"; 
      return "subrange"; 
    } 

    if ("excludingSubrange".startsWith(st)) 
    { mess[0] = "subrange removal operator on sequences and strings\n"  + 
           "  ss.excludingSubrange(i,j) is ss with subrange\n" + 
           "  from i to j removed. Positions start at 1\n"; 
      return "excludingSubrange"; 
    } 

    if ("setSubrange".startsWith(st)) 
    { mess[0] = "subrange update operator on sequences and strings\n"  + 
           "  ss.setSubrange(i,j,v) is ss with subrange\n" + 
           "  from i to j replaced by v. Positions start at 1\n"; 
      return "setSubrange"; 
    } 

    if ("insert".startsWith(st)) 
    { mess[0] = "insertAt/insertInto operators on sequences and strings\n"  + 
           "ss.insertAt(i,x) is ss with x inserted at position i as an element\n" + 
           "ss.insertInto(i,x) is ss with x inserted starting at position i as a subrange\n" + 
           "Positions start at 1, end at ss->size().\n"; 
      return "insertAt or insertInto"; 
    } 

    if ("replace".startsWith(st)) 
    { mess[0] = "replace/replaceFirstMatch/replaceAll/replaceAllMatches operators on strings\n"  + 
           "ss.replace(s1,s2) is ss with each literal substring s1 replaced by s2\n" + 
           "ss.replaceAllMatches(regexp,s) is ss with any match of regexp replaced by s\n" + 
           "ss.replaceFirstMatch(regexp,s) is ss with the first match of regexp replaced by s\n"; 
      return "replace; replaceAllMatches; replaceFirstMatch"; 
    } 

    if ("setAt".startsWith(st)) 
    { mess[0] = "setAt operator on sequences and strings\n"  + 
           "ss.setAt(i,x) is ss with position i set to x\n" + 
           "Positions start at 1, end at ss->size().\n"; 
      return "setAt"; 
    } 

    return null; 
  } 

  public static String matchPrecedingIdentifier(char cc, String txt, int pos)
  { String res = "" + cc; 
    for (int i = pos-1; i >= 0; i--) 
    { char cx = txt.charAt(i); 
      if (Character.isLetterOrDigit(cx) || cx == '_' || 
          cx == '$')
      { res = cx + res; } 
      else 
      { return res; } 
    } 
    return res; 
  } 

  public static String matchPrecedingOperator(char cc, String txt, int pos)
  { String res = "" + cc; 
    for (int i = pos-1; i >= 0; i--) 
    { char cx = txt.charAt(i); 
      if (Character.isLetterOrDigit(cx) || cx == '_' || 
          cx == '$')
      { res = cx + res; } 
      else if (cx == '>' && i > 0 && 
               txt.charAt(i-1) == '-')
      { res = "->" + res; 
        return res; 
      } 
      else 
      { return res; } 
    } 
    return res; 
  } 

  public static String findIdentifierDefinition(String iden, String txt)
  { if (iden.length() == 0) 
    { return null; } 

    // System.out.println(">>> Searching for " + iden + " definition"); 

    char idenlast = iden.charAt(iden.length()-1); 

    for (int i = txt.length()-1; i >= 0; i--) 
    { // Look for previous definitions  iden = something 
      // or  iden : sometype

      char cx = txt.charAt(i); 
      if (cx == '=' || cx == ':')
      { // is iden on LHS? 
        boolean scanning = true; 

        for (int j = i-1; j >= 0 && scanning; j--)
        { char cj = txt.charAt(j); 
          if (Character.isWhitespace(cj)) { } 
          else if (cj == idenlast && Character.isWhitespace(txt.charAt(j+1)))
          { String piden = matchPrecedingIdentifier(cj,txt,j);
            // System.out.println(">>> Matched: " + piden); 
 
            if (iden.equals(piden)) // Found definition
            { String subtext = txt.substring(j);
              int packageindex = subtext.indexOf("package "); 
              int classindex = subtext.indexOf("class "); 
              if (classindex >= 0 || packageindex >= 0) 
              { System.out.println(">>> identifier defined in different class/package: " + piden); 
                return null; 
              } 
              else 
              { int usecaseindex = subtext.indexOf("usecase "); 
                if (usecaseindex >= 0) 
                { System.out.println(">>> identifier defined in different usecase: " + piden); 
                  return null; 
                } 
                else if (cx == ':')  
                { String prevblock = getPreviousTextBlock(j - iden.length(), txt); 
                  String trimdec = prevblock.trim(); 
                  if ("attribute".equals(trimdec) || 
                      "reference".equals(trimdec) || 
                      "parameter".equals(trimdec))
                  { System.out.println(">> Identifier defined in current classifier: " + trimdec); } 
                  else 
                  { int operationindex = subtext.indexOf("operation "); 
                    int queryindex = subtext.indexOf("query "); 
                    if (operationindex >= 0 || queryindex >= 0) 
                    { System.out.println(">>> identifier defined in different operation: " + piden); }
                  } 

                  // if attribute or parameter or reference

                  String block = getNextTextBlock(i+1,txt); 
                  return iden + " " + cx + " " + block; 

                  // otherwise, an operation parameter
                } 
                else // an = definition 
                { int operationindex = subtext.indexOf("operation "); 
                  int queryindex = subtext.indexOf("query "); 
                  if (operationindex >= 0 || queryindex >= 0) 
                  { System.out.println(">>> identifier defined in different operation: " + piden); 
                    return null; 
                  } 
                  else  
                  { String block = getNextTextBlock(i+1,txt); 
                    return iden + " " + cx + " " + block; 
                  } 
                }             
              }
            }
          }
          else 
          { scanning = false; 
            break; 
          } // continue going back with i 
        }  
      }
    } 
    return null; 
  } 

  public static String getNextTextBlock(int pos, String txt)
  { String res = ""; 
    int i = pos; 
    for ( ; i < txt.length(); i++) 
    { char cx = txt.charAt(i); 
      if (Character.isWhitespace(cx)) { } 
      else 
      { break; } 
    } 

    for ( ; i < txt.length(); i++) 
    { char cx = txt.charAt(i); 
      if (Character.isWhitespace(cx)) 
      { return res; }
      res = res + cx;  
    } 
    return res; 
  } 

  public static String getPreviousTextBlock(int pos, String txt)
  { String res = ""; 
    int i = pos; 
    for ( ; i >= 0; i--) 
    { char cx = txt.charAt(i); 
      if (Character.isWhitespace(cx)) { } 
      else 
      { break; } 
    } 

    for ( ; i >= 0; i--) 
    { char cx = txt.charAt(i); 
      if (Character.isWhitespace(cx)) 
      { return res; }
      res = cx + res;  
    } 
    return res; 
  } 

  public static int lineCount(String txt, int i)
  { int lc = 1; 
    for (int j = 0; j < i; j++) 
    { if (txt.charAt(j) == '\n')
      { lc++; } 
    } 
    return lc; 
  } 

  public static int linePosition(String txt, int i)
  { int lp = 1; 
    for (int j = 0; j < i; j++) 
    { if (txt.charAt(j) == '\n')
      { lp = 1; }
      else 
      { lp++; }  
    } 
    return lp; 
  } 

  public static int matchPrecedingBracket(String ch, String txt, Vector errors, Vector colours)
  { if ("}".equals(ch) || ")".equals(ch) || "]".equals(ch))
    { } 
    else 
    { return -1; } 
	
	
    int ob = 0;
    int cb = 0; 
    int osqb = 0;
    int csqb = 0;
    int ocb = 0;
    int ccb = 0;
    String mostrecentclosed = ch;  
	
    for (int i = txt.length()-1; i >= 0; i--) 
    { String chr = txt.charAt(i) + ""; 
      if (ch.equals("}") && chr.equals("{") && ob == cb && csqb == osqb && ocb == ccb)
      { colours.add(Color.green);
        int lc = lineCount(txt,i); 
        int lp = linePosition(txt,i); 
        errors.add("This } correctly matches { on line " + lc + " position " + lp + ":\n"); 
        return i; 
      } 
      if (ch.equals(")") && chr.equals("(") && osqb == csqb && ocb == ccb && ob == cb)
      { colours.add(Color.green); 
        int lc = lineCount(txt,i); 
        int lp = linePosition(txt,i); 
        errors.add("This ) correctly matches ( on line " + lc + " position " + lp + ":\n"); 
        return i;
      } 
      if (ch.equals("]") && chr.equals("[") && ob == cb && ocb == ccb && osqb == csqb)
      { colours.add(Color.green); 
        int lc = lineCount(txt,i); 
        int lp = linePosition(txt,i); 
        errors.add("This ] correctly matches the [ on line " + lc + 
                   " position " + lp + ":\n"); 
        return i;
      }
      if (ch.equals("}") && ( (chr.equals("(") && cb < ob ) || (chr.equals("[") && csqb < osqb ) ) )
      { colours.add(Color.red); 
        int lc = lineCount(txt,i); 
        errors.add("Warning: incorrect bracket " + chr + " to match }: " + chr + " at line " + lc + ":\n"); 
        return i; 
      } 
      if (ch.equals(")") && ( (chr.equals("[") && osqb+1 != csqb) || (chr.equals("{") && ocb+1 != ccb) ) )
      { colours.add(Color.red); 
        errors.add("Error!: incorrect bracket to match ): " + chr + " at " + i + ":\n"); 
        return i; 
      } 
      if (ch.equals("]") && ((chr.equals("(") && ob+1 != cb) || (chr.equals("{") && ocb+1 != ccb) ) )
      { colours.add(Color.red); 
        errors.add("Error!: incorrect bracket to match ]: " + chr + " at " + i + ":\n"); 
        return i; 
      } 
	  if (chr.equals("}"))
	  { ccb++; }
	  else if (chr.equals("{"))
	  { ocb++; }
	  else if (chr.equals("("))
	  { ob++; }
	  else if (chr.equals(")"))
	  { cb++; }
	  else if (chr.equals("["))
	  { osqb++; }
	  else if (chr.equals("]"))
	  { csqb++; }
    } 
    errors.add("Warning: no opening bracket to match " + ch); 
	// colours.add(Color.red); 
    return -1; 
  } 
      


  public Statement parseStatement(int s, int e, Vector entities, Vector types)
  { /* boolean balanced = checkBrackets(s,e); 
    if (!balanced) 
	{ System.err.println("Not statement -- different number of ( and ) brackets"); 
	  return null; 
	} */ 

    Statement st = parseSequenceStatement(s,e,  entities, types);
    if (st == null)
    { st = parseLoopStatement(s,e, entities, types); }
    if (st == null)
    { st = parseConditionalStatement(s,e,  entities,  types); }
    if (st == null)
    { st = parseTryStatement(s,e,  entities,  types); }
    if (st == null)
    { st = parseBasicStatement(s,e,  entities,  types); }
    return st;
  }


  public Statement parseSequenceStatement(int s, int e, Vector entities, Vector types)
  { Statement s1 = null;
    Statement s2 = null;
    for (int i = s; i < e; i++)
    { String ss = lexicals.get(i) + "";
      if (ss.equals(";"))
      { int j = i+1; 
        for ( ; j < e && ";".equals(lexicals.get(j) + ""); j++) 
        { } 

        boolean bb = balancedBrackets(s,i-1); 
        if (bb == false) 
        { continue; } 

        // System.out.println(">>> Parsing at: " + showLexicals(s,i-1)); 

        s1 = parseStatement(s,i-1, entities,types);
        if (s1 == null) 
        { continue; } 
        s2 = parseStatement(j,e, entities,types);
        if (s2 == null) 
        { continue; } // try another ;  
        // { return null; }
        else
        { SequenceStatement res = new SequenceStatement();
          res.addStatement(s1);
          res.addStatement(s2);
          // System.out.println(">>> Parsed ; statement: " + res);
          return res;
        }
      }
    }
    return null;
  }

  public Statement parseLoopStatement(int s, int e, Vector entities, Vector types)
  { if ("for".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("do"))
        { test = parse_statement_expression(0,s+1,i-1,entities,types);
          if (test == null) 
          { System.err.println("!! ERROR: Invalid test in for loop: " + showLexicals(s+1,i-1)); 
            return null; 
          } 
          s1 = parseStatement(i+1, e, entities, types);
          if (s1 == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.FOR); 
          if (test instanceof BinaryExpression)
          { BinaryExpression betest = (BinaryExpression) test; 
            if (":".equals(betest.getOperator()))
            { res.setLoopRange(betest.getLeft(), betest.getRight()); } 
          }
          // System.out.println("Parsed for statement: " + res);
          return res;
        }
      }
      // System.err.println("Unable to parse for statement: " + lexicals.get(s) + " ... " + 
      //                    lexicals.get(e)); 
    }
    else if ("while".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("do"))
        { test = parse_statement_expression(0,s+1,i-1,entities,types);
          if (test == null) 
          { if ("(".equals(lexicals.get(s+1) + "") && 
                ")".equals(lexicals.get(i-1) + ""))
            { test = parse_statement_expression(0,s+2,i-2,entities,types); 
              if (test == null)
              { System.err.println("ERROR!!: Invalid test in while loop: " + showLexicals(s+1,i-1));
                return null; // continue; ???
              } 
              test.setBrackets(true); 
            }  
          } 
          s1 = parseStatement(i+1, e, entities, types);
          if (s1 == null || test == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.WHILE); 
          System.out.println(">>> Parsed while statement: " + res);
          return res;
        }
      }
      // System.err.println("Unable to parse while statement: " + lexicals.get(s) + " ... " + 
      //                    lexicals.get(e)); 
    }
    else if ("repeat".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("until"))
        { s1 = parseStatement(s+1, i-1, entities, types);
          if (s1 == null) { continue; } 
          test = parse_statement_expression(0,i+1,e,entities,types);
          if (test == null) 
          { if ("(".equals(lexicals.get(i+1) + "") && 
                ")".equals(lexicals.get(e-1) + ""))
            { test = parse_statement_expression(0,i+2,e-2,entities,types); 
              if (test == null)
              { System.err.println("!!ERROR!!: Invalid test in while loop: " + showLexicals(i+1,e-1));
                return null; // continue; ???
              } 
              test.setBrackets(true); 
            }  
          } 
          if (s1 == null || test == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.REPEAT); 
          System.out.println(">>> Parsed repeat statement: " + res);
          return res;
        }
      }
    }

    return null;
  }

  public Statement parseConditionalStatement(int s, int e, Vector entities, Vector types)
  { if ("if".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Statement s2 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("then"))
        { test = parse_statement_expression(0,s+1,i-1,entities,types);
          if (test == null) 
          { System.err.println("!! ERROR: Invalid test in conditional: " + showLexicals(s+1,i-1)); 
            continue; 
          } 

          for (int j = i+1; j < e; j++)
          { String ss1 = lexicals.get(j) + "";
            if (ss1.equals("else"))
            { s1 = parseStatement(i+1, j-1, entities, types);
              s2 = parseBasicStatement(j+1, e, entities, types);  // else must be bracketed or basic

              if (s2 == null && s1 != null && test != null) 
              { s2 = parseConditionalStatement(j+1, e, entities, types); } 

              if (s1 == null || s2 == null || test == null)
              { continue; }

              ConditionalStatement res = new ConditionalStatement(test,s1,s2);
              // System.out.println("Parsed if statement: " + res);
              return res;
            }
          }
        }
      }
      // System.err.println("Unable to parse if-then-else statement: " + showLexicals(s,e));  
    }
    return null;
  }  // no option for if-then without else. 

  public Statement parseTryStatement(int s, int e, Vector entities, Vector types)
  { // try statement ... 1+ catch statements ended by finally statement, 
    // or 0 catch and 1 finally, or 1+ catch statements
    // must be at least one catch or a finally

    if ("try".equals(lexicals.get(s) + ""))
    { Statement body = null;
      int currentStart = s; 
      TryStatement res = null; 

      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("catch") || ss.equals("finally"))
        { body = parseStatement(s+1,i-1,entities,types);
          if (body == null) 
          { // System.err.println("!! ERROR: Invalid body in try statement: " + showLexicals(s+1,i-1)); 
            return null; 
          } 
          res = new TryStatement(body); 
          currentStart = i;
          break; 
        } 
      }  
        
      for (int j = currentStart+1; j < e; j++)
      { String ss1 = lexicals.get(j) + "";
        if (ss1.equals("catch") || ss1.equals("finally"))
        { Statement cs1 = parseBasicStatement(currentStart, j-1, entities, types);
          if (cs1 == null) 
          { // System.err.println("!! ERROR: Invalid catch/finally in try statement: " + showLexicals(currentStart,j-1)); 
            return res; 
          }
          res.addClause(cs1);  
          currentStart = j; 
        } 
      }

      if (currentStart < e)
      { Statement cs2 = parseBasicStatement(currentStart,e,entities,types); 
        if (cs2 == null) 
        { // System.err.println("!! ERROR: Invalid catch/finally in try statement: " + showLexicals(currentStart,e)); 
          return res; 
        }
        res.addClause(cs2);  
      }
      return res; 
    }
    return null;
  }  // no option for if-then without else. 



  public Statement parseBasicStatement(int s, int e, Vector entities, Vector types)
  { /* skip, return, continue, break, error, catch, var, execute, 
       operation calls, assignment, bracketed statements */

    if (e == s)
    { if ("skip".equals(lexicals.get(e) + ""))
      { return new InvocationStatement("skip",null,null); }
       // operation call
      else if ("return".equals(lexicals.get(e) + ""))
      { return new ReturnStatement(); }
      else if ("break".equals(lexicals.get(e) + ""))
      { return new BreakStatement(); }
      else if ("continue".equals(lexicals.get(e) + ""))
      { return new ContinueStatement(); }
      // else if (";".equals(lexicals.get(e) + ""))
      // { return new InvocationStatement("skip",null,null); } 
      else 
      { Expression ee = parse_basic_expression(0,s,e,entities,types);
        if (ee == null) 
        { // System.err.println("!! Invalid basic expression: " + showLexicals(s,e)); 
          return null; 
        } 
        InvocationStatement istat = new InvocationStatement(ee + "",null,null);
        istat.setCallExp(ee); 
        return istat; 
      } 
    }

    boolean bb = balancedBrackets(s, e);
    if (bb) { } 
    else 
    { return null; } 
 
    if ("(".equals(lexicals.get(s) + "") &&
        ")".equals(lexicals.get(e) + ""))
    { Statement stat = parseStatement(s+1,e-1,entities,types); 
      if (stat == null) 
      { return null; } 

      SequenceStatement ss = new SequenceStatement(); 
      ss.addStatement(stat); 
      ss.setBrackets(true); 
      return ss; 
    }  // set brackets
    else if ("return".equals(lexicals.get(s) + ""))
    { Expression ret = parse_expression(0,s+1,e,entities,types); 
      if (ret != null) 
      { return new ReturnStatement(ret); }  
    } 
    else if ("execute".equals(lexicals.get(s) + ""))
    { Expression ret = parse_expression(0,s+1,e,entities,types); 
      if (ret != null) 
      { return new ImplicitInvocationStatement(ret); }  
    }
    else if ("catch".equals(lexicals.get(s) + "") && 
             "(".equals(lexicals.get(s+1) + ""))
    { for (int p = s+2; p < e; p++) 
      { String lxc = lexicals.get(p) + ""; 
        if (")".equals(lxc) && 
            "do".equals(lexicals.get(p+1) + ""))
        { Expression catchcond = parse_expression(0,s+2,p-1,entities,types); 
          Statement catchaction = parseBasicStatement(p+2,e,entities,types); 
          if (catchcond != null && catchaction != null) 
          { return new CatchStatement(catchcond,catchaction); }
        } 
      } 
      // System.err.println("!! Invalid catch statement: " + showLexicals(s,e));     
    } 
    else if ("assert".equals(lexicals.get(s) + ""))
    { for (int p = s+1; p < e; p++) 
      { String lxc = lexicals.get(p) + ""; 
        if ("do".equals(lxc))
        { Expression assertcond = parse_expression(0,s+1,p-1,entities,types); 
          Expression assertmsg = parse_expression(0,p+1,e,entities,types); 
          if (assertcond != null && assertmsg != null) 
          { return new AssertStatement(assertcond,assertmsg); }
        } 
      } 
      Expression assertc = parse_expression(0,s+1,e,entities,types);
      if (assertc != null) 
      { return new AssertStatement(assertc); } 
      // System.err.println("!! Invalid assert statement: " + showLexicals(s,e));     
    } 
    else if ("finally".equals(lexicals.get(s) + ""))
    { Statement finalaction = parseBasicStatement(s+1,e,entities,types); 
      if (finalaction != null) 
      { return new FinalStatement(finalaction); }
    } 
    else if ("error".equals(lexicals.get(s) + ""))
    { Expression ret = parse_expression(0,s+1,e,entities,types); 
      if (ret != null) 
      { return new ErrorStatement(ret); }
      /* else 
      { System.err.println("!! Invalid expression in error statement: " + showLexicals(s+1,e)); } */     
    } 
    else if ("var".equals(lexicals.get(s) + ""))
    { // creation with complex type
      String varname = lexicals.get(s+1) + ""; 
      for (int j = s+4; j < e; j++) 
      { String chr = lexicals.get(j) + ""; 
        if (":=".equals(chr))
        { Type typ1 = parseType(s+3,j-1,entities,types); 
          Expression expr = parse_expression(0,j+1,e,entities,types); 
          if (typ1 != null && expr != null) 
          { System.out.println(">>> Creation statement var " + varname + " : " + typ1 + " with initialisation " + expr);
            CreationStatement cs1 = new CreationStatement(typ1 + "",varname); 
            cs1.setType(typ1); 
            cs1.setKeyType(typ1.getKeyType()); 
            cs1.setElementType(typ1.getElementType());  
            cs1.setInitialisation(expr);
            return cs1;
          }   
        }
      } 
      Type typ = parseType(s+3,e,entities,types);
      if (typ != null) 
      { CreationStatement cs = new CreationStatement(typ + "", varname); 
        cs.setType(typ); 
        cs.setKeyType(typ.getKeyType()); 
        cs.setElementType(typ.getElementType());  
        return cs; 
      } 
      else 
      { System.err.println("!! Warning: unknown type in var statement: " + showLexicals(s+3,e));
        /* typ = new Type("OclAny", null); 
        CreationStatement cs1 = new CreationStatement("OclAny", varname); 
        cs1.setType(typ); 
        return cs1; */  
      }  
    }  
 /*   else if (e == s + 2 && ":".equals(lexicals.get(s+1) + ""))
    { CreationStatement cs = new CreationStatement(lexicals.get(e) + "", lexicals.get(s) + "");
      Type tt = Type.getTypeFor(lexicals.get(e) + "", new Vector(), new Vector()); // assume primitive
      if (tt != null) 
      { cs.setType(tt); }
      else 
      { System.err.println("!! Warning: Unknown type in : statement: " + lexicals.get(e)); }  
      return cs; 
    } */ // allow any type
    else 
    { for (int i = s; i < e; i++) 
      { String ss = lexicals.get(i) + ""; 
        if (":=".equals(ss))
        { if (i == s + 3 && ":".equals(lexicals.get(s + 1) + ""))
          { // Assignment with declaration 
            Type t = Type.getTypeFor(lexicals.get(s + 2) + "", new Vector(), new Vector()); 
            String var = lexicals.get(s) + ""; 
            Expression exp2 = parse_expression(0,i+1,e,entities,types);
            if (exp2 == null) { return null; } 
            /* System.out.println("Parsed assignment with type: " + var + " : " +
                               t + " := " + exp2); */
            AssignStatement ast = new AssignStatement(new BasicExpression(var), exp2); 
            ast.setType(t); 
            ast.setOperator(":="); 
            return ast; 
          } 

          Expression e1 = parse_basic_expression(0,s,i-1,entities,types); 
          if (e1 == null) 
          { e1 = parseAtExpression(0,s,i-1,entities,types); }
          if (e1 == null) 
          { e1 = parseCollectExpression(0,s,i-1,entities,types); }
          if (e1 == null) 
          { return null; }
 
          Expression e2 = parse_expression(0,i+1,e,entities,types); 
          if (e2 == null)
          { 
            return null; 
          } 
          System.out.println(">>> Parsed assignment: " + e1 + " := " + e2); 
          AssignStatement res = new AssignStatement(e1,e2);
          res.setOperator(":="); 
          return res;  
        } 
      }
    }  // all of these should use parseType to allow general types. 
       // should allow  v : T := e with general type T 

    Expression ee = parse_basic_expression(0,s,e,entities,types);
    if (ee != null) 
    { InvocationStatement istat = new InvocationStatement(ee + "",null,null);
      istat.setCallExp(ee); 
      return istat; 
    } 

    // System.out.println("ERROR: Unable to parse basic command: " + showLexicals(s,e)); 
    return null;
  }

  public Statement parseStatement(Vector entities, Vector types)
  { Vector stats = splitIntoStatementSequence(0,0,lexicals.size()-1, entities, types);
    // System.out.println("TOP LEVEL STATEMENTS ARE: " + stats);

    if (stats == null) 
    { return null; } 
 
    if (stats.size() == 0) 
    { return new SequenceStatement(); } 
    else if (stats.size() == 1) 
    { return (Statement) stats.get(0); } 
    else 
    { return new SequenceStatement(stats); } 
  }

  public Vector splitIntoStatementSequence(int bc, int s, int e, Vector entities, Vector types)
  { Vector res = new Vector(); 
    int bcc = bc; 
    boolean instring = false; 

    if (s >= e) 
    { return res; } 

    // If "(" is first, ")" is last, 
    // parse the statement sequence within them 

    if ("(".equals(lexicals.get(s) + "") && 
        ")".equals(lexicals.get(e) + ""))
    { res = splitIntoStatementSequence(bc,s+1,e-1,entities,types); 
      return res; 
    } 

    Statement s1 = null;
    Vector s2 = new Vector();

    for (int i = s; i < e; i++)
    { String ss = lexicals.get(i) + "";
      if (ss.equals(";") && bcc == bc && !instring)
      { // Top-level statement
        s1 = parseStatement(s,i-1,entities,types);
        if (s1 == null) 
        { System.err.println("WARNING: unrecognised statement: " + showLexicals(s,i-1)); 
          continue; 
        } 
        else
        { s2 = splitIntoStatementSequence(bc,i+1,e,entities,types); } 
        if (s2 == null) 
        { continue; } // try another ;  
        else
        { res.add(s1);
          res.addAll(s2);
          // System.out.println("Parsed ; statement: " + res);
          return res;
        }
      }
      else if ("(".equals(ss) && !instring) // and not inside a string
      { bcc++; } 
      else if (")".equals(ss) && !instring)
      { bcc--; } 
      else if ("\"".equals(ss) && !instring) // and not inside a string
      { instring = true; } 
      else if ("\"".equals(ss) && instring)
      { instring = false; } 
    }
    
    Statement ls = parseStatement(s,e,entities,types);
    if (ls != null) 
    { res.add(ls); } 
    return res; 
 }

  public Vector parseKM3()
  { Vector ents = new Vector();
    Vector typs = new Vector();
    Vector gens = new Vector();
    Vector pasts = new Vector();
    Vector pnames = new Vector();
    Vector items = parseKM3(ents,typs,gens,pasts,pnames);
    return ents; 
  }

  public Vector parseKM3(Vector entities, Vector types, Vector gens, Vector pasts, Vector pnames)
  { Vector res = new Vector(); 
    int en = lexicals.size()-1; 
    Vector importList = new Vector(); 
    
    // retain the package name, it becomes the system name. 
    int prevstart = 0; 
    for (int i = 0; i < en; i++) 
    { String lex = lexicals.get(i) + ""; 
      if ("package".equals(lex))
      { prevstart = i; 
        break; 
      } 
      else if ("import".equals(lex))
      { i++; 
        String imp = lexicals.get(i) + ""; 
        importList.add(imp); 
      } 
    } 

    System.out.println(">>> Importing: " + importList); 
    System.out.println(); 

    int st = prevstart; 

    for (int i = st+1; i < en; i++) 
    { String lex = lexicals.get(i) + "";
      if ("package".equals(lex))
      { // parse contents of previous package: 
        Vector resp = parseKM3package(prevstart,i-1,entities,types,gens,pasts,pnames); 
        res.addAll(resp); 
        prevstart = i; 
      } 
    } 
       
    Vector resp = parseKM3package(prevstart,en-1,entities,
                                  types,gens,pasts,pnames); 
    res.addAll(resp); 
    return res; 
  } 

  public UseCase parseKM3UseCase(Vector entities, Vector types, 
                                 Vector gens, Vector pasts)
  { int en = lexicals.size()-1; 
    return parseKM3UseCase(0,en,entities,types,gens,pasts); 
  } 


  public Vector parseKM3package(int st, int en, 
                     Vector entities, Vector types, 
                     Vector gens, Vector pasts, Vector pnames)
  { Vector res = new Vector(); 
    if ("package".equals(lexicals.get(st) + "") && 
        "{".equals(lexicals.get(st+2) + ""))
    { String pname = lexicals.get(st+1) + ""; 
      System.out.println(">> Parsing package " + pname); 
      pnames.add(pname); 

      for (int i = st+3; i < en;  i++) 
      { String lx = lexicals.get(i) + ""; 
        if ("class".equals(lx) || "abstract".equals(lx) || 
            "enumeration".equals(lx) || 
            "interface".equals(lx) || "datatype".equals(lx) || 
            "usecase".equals(lx))
        { Vector v = parseKM3PackageContents(i,en,entities,types,gens,pasts); 
          res.addAll(v); 
          return res; 
        }
      } 
    } 
    return res; 
  } 

  public void identifyKM3classifiers(
                                     Vector entities, 
                                     Vector types)
  { int st = 0; 
    int en = lexicals.size(); 
    if ("package".equals(lexicals.get(st) + "") && 
        "{".equals(lexicals.get(st+2) + ""))
    { String pname = lexicals.get(st+1) + ""; 
      System.out.println(">> Parsing package " + pname); 
    
      st = st+3; 
    }

    identifyKM3classifiers(st,en-1,entities,types); 
  } 

  public void identifyKM3classifiers(int st, int en, 
                                     Vector entities, 
                                     Vector types)
  { /* First pass of input to identify class and type names */ 
    int reached = st; 

    if (st >= en)
    { return; }
 
    for (int i = st + 2; i <= en; i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("class".equals(lx) || 
          ("abstract".equals(lx) && 
           i < en && "class".equals(lexicals.get(i+1) + ""))  
          || 
          "enumeration".equals(lx) ||
          "interface".equals(lx) || 
          "datatype".equals(lx) || "usecase".equals(lx))
      { Object e = identifyKM3classifier(
                       reached,i-1,entities,types);
        if (e != null) 
        { if (e instanceof Type)
          { if (types.contains(e)) { } 
            else 
            { types.add(e); }
          } 
          else if (e instanceof Entity)
          { if (entities.contains(e)) { } 
            else 
            { entities.add(e); }
          } 
        }
        reached = i;    
      }
    } 

    Object e1 = identifyKM3classifier(
                    reached,en,entities,types);
    if (e1 != null) 
    { if (e1 instanceof Type)
      { if (types.contains(e1)) { } 
        else 
        { types.add(e1); }
      } 
      else if (e1 instanceof Entity)
      { if (entities.contains(e1)) { } 
        else 
        { entities.add(e1); }
      } 
    } 
  } 


  public Vector parseKM3PackageContents(int st, int en, 
                                  Vector entities, 
                                  Vector types, 
                                  Vector gens, Vector pasts)
  { Vector res = new Vector(); 
    Vector errors = new Vector(); 
    int reached = st; 

    if (st >= en)
    { return res; }
 
    for (int i = st + 2; i <= en;  i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("class".equals(lx) || "abstract".equals(lx) || 
          "enumeration".equals(lx) ||
          "interface".equals(lx) || 
          "datatype".equals(lx) || "usecase".equals(lx))
      { Object e = parseKM3classifier(
              reached,i-1,entities,types,gens,pasts,errors);
        if (e != null) 
        { res.add(e); 
          reached = i; 
          if (e instanceof Type)
          { if (types.contains(e)) { } 
            else 
            { types.add(e); }
          } 
        }
 
	  // else if (e != null && e instanceof Entity && "interface".equals(lx))
        // { ((Entity) e).setInterface(true); } 
        // Vector remainder = parseKM3PackageContents(i,en,entities,types,gens,pasts);  
        // if (e != null) { res.add(0,e); }
		// res.addAll(remainder);   
        // return res; 
      }
    } 

    Object e1 = parseKM3classifier(
                 reached,en,entities,types,gens,pasts,errors);
    if (e1 != null) 
    { res.add(e1); 
      if (e1 instanceof Type)
      { if (types.contains(e1)) { } 
        else
        { types.add(e1); }
      } 
    } 
      
    return res; 
  } 

  public Object parseKM3Class(Vector entities, Vector types)
  { Vector gens = new Vector(); 
    Vector pasts = new Vector(); 
    Vector errors = new Vector(); 
    return parseKM3classifier(
                 entities,types,gens,pasts,errors); 
  } 

  public Object parseKM3Enumeration(Vector entities, Vector types)
  { Vector gens = new Vector(); 
    Vector pasts = new Vector(); 
    Vector errors = new Vector(); 
    return parseKM3classifier(
                entities,types,gens,pasts,errors); 
  } 


  public Vector parse_generic_parameters(
                     int st, int en, Vector entities)
  { Vector res = new Vector(); 

    for (int i = st; i <= en; i++) 
    { String parType = lexicals.get(i) + "";
      if (",".equals(parType)) 
      { continue; }  
      Entity parEnt = new Entity(parType);
      parEnt.genericParameter = true; 
      parEnt.addStereotype("derived"); 
 
      Type parT = new Type(parEnt); 
      res.add(parT); 
      entities.add(parEnt); 
    } 
    return res; 
  } 

  public Vector parse_type_sequence(
                     int st, int en, Vector entities, 
                     Vector types)
  { Vector res = new Vector(); 

    int prev = st; 
    for (int i = st; i <= en; i++) 
    { String parType = lexicals.get(i) + "";
      if (",".equals(parType)) 
      { Type typ = 
          parseType(prev,i-1,entities,types); 
        if (typ != null) 
        { res.add(typ);
          prev = i+1;
        } 
      }  
    } 

    if (prev <= en)
    { Type lasttyp = 
          parseType(prev,en,entities,types); 
      if (lasttyp != null) 
      { res.add(lasttyp); }
    }

    return res; 
  } 

  public Object parseKM3classifier(
                     Vector entities, Vector types, 
                     Vector gens, Vector pasts, Vector errors)
  { return parseKM3classifier(0, lexicals.size()-1, 
               entities, types, gens, pasts, errors); 
  } 

  public Object identifyKM3classifier(int st, int en, 
                              Vector entities, Vector types)
  { String rname = ""; 
    int start = st; 
    String lx = lexicals.get(st) + ""; 

    if ("abstract".equals(lx) &&
        "class".equals(lexicals.get(st + 1) + ""))
    { start = st + 4; 
      rname = lexicals.get(st + 2) + ""; 
    } 
    else if ("interface".equals(lx))
    { start = st + 3; 
      rname = lexicals.get(st + 1) + "";
    } 
    else if ("class".equals(lx))
    { start = st + 3; 
      rname = lexicals.get(st + 1) + "";
    } 
    else if ("datatype".equals(lx))
    { Type dt = new Type(lexicals.get(st + 1) + "", null); 
      if (st + 3 < lexicals.size() && 
          "=".equals(lexicals.get(st+2) + ""))
      { if (en <= st+4)
        { String aname = lexicals.get(st+3) + ""; 
        // start = st+4; 
          dt.setAlias(new Type(aname,null)); 

          JOptionPane.showMessageDialog(null, 
             "Simple datatype - should be removed before code-generation: " + dt + " = " + aname, 
             "", JOptionPane.INFORMATION_MESSAGE);  

          return dt;
        } 
        else 
        { Type aliasType = 
            parseType(st+3,en-1,entities,types);
          dt.setAlias(aliasType);

          JOptionPane.showMessageDialog(null, 
             "Complex datatype - should be removed before code-generation: " + dt + " = " + aliasType, 
             "", JOptionPane.INFORMATION_MESSAGE);  
           
          return dt; 
        }  
      }  
      dt.setAlias(new Type("String", null)); 
      return dt; 
    } 
    else if ("enumeration".equals(lx))
    { int j = st + 2; 
      Vector values = new Vector(); 
      Vector estereos = new Vector();
 
      while (j < en)
      { String lxr = lexicals.get(j) + ""; 
    
        if ("literal".equals(lxr)) 
        { values.add(lexicals.get(j+1) + ""); 
          j = j + 2; 
        }
        else if ("stereotype".equals(lxr))
        { String stereo = lexicals.get(j+1) + ""; 
          estereos.add(stereo); 
          j = j + 2; 
        } // Only simple stereotypes. 
        else 
        { j++; } 
      }

      Type tt = new Type(lexicals.get(st + 1) + "", values); 
      tt.addStereotypes(estereos); 

      return tt; 
    } 
    else if ("usecase".equals(lx))
    { return null; } 
    else        
    { System.err.println("!! Invalid classifier keyword: " + lx); 
      return null; 
    } 

    ModelElement ent = 
      ModelElement.lookupByName(rname, entities); 
    if (ent != null) 
    { return null; } 

    Entity res = new Entity(rname); 
 
    return res; 
  } 


  public Object parseKM3classifier(int st, int en, Vector entities, Vector types, 
                          Vector gens, Vector pasts, Vector errors)
  { boolean abstr = false; 
    boolean interf = false; 
    String rname = ""; 
    int start = st; 
    Vector atts = new Vector(); 
    Vector roles = new Vector(); 

    String lx = lexicals.get(st) + ""; 

    if ("abstract".equals(lx) &&
        "class".equals(lexicals.get(st + 1) + "") && 
        "}".equals(lexicals.get(en) + ""))
    { abstr = true; 
      start = st + 3; 
      rname = lexicals.get(st + 2) + ""; 
    } 
    else if ("interface".equals(lx) && 
             "}".equals(lexicals.get(en) + ""))
    { interf = true; 
      start = st + 2; 
      rname = lexicals.get(st + 1) + "";
    } 
    else if ("class".equals(lx) && 
             "}".equals(lexicals.get(en) + ""))
    { abstr = false;
      interf = false;  
      start = st + 2; 
      rname = lexicals.get(st + 1) + "";
    } 
    else if ("datatype".equals(lx))
    { String tname = lexicals.get(st + 1) + ""; 

      Type dt = (Type) ModelElement.lookupByName(tname,types); 
      if (dt == null) 
      { dt = new Type(tname, null); }

      if (en <= st+4)
      { String aname = lexicals.get(st+3) + ""; 
        // start = st+4; 
        dt.setAlias(new Type(aname,null)); 

        JOptionPane.showMessageDialog(null, 
             "Simple datatype -- remove before code-generation: " + dt + " = " + aname, 
             "", JOptionPane.INFORMATION_MESSAGE);  

        return dt;
      } 
      else 
      { Type aliasType = 
            parseType(st+3,en-1,entities,types);
        dt.setAlias(aliasType);

        JOptionPane.showMessageDialog(null, 
             "Complex datatype -- remove before code-generation: " + dt + " = " + aliasType, 
             "", JOptionPane.INFORMATION_MESSAGE);  
           
        return dt; 
      }  
      // dt.setAlias(new Type("String", null)); 
      // return dt; 
    } 
    else if ("enumeration".equals(lx))
    { int j = st + 2; 
      Vector values = new Vector(); 
      Vector estereos = new Vector();
 
      while (j < en)
      { String lxr = lexicals.get(j) + ""; 
    
        if ("literal".equals(lxr)) 
        { values.add(lexicals.get(j+1) + ""); 
          j = j + 2; 
        }
        else if ("stereotype".equals(lxr))
        { String stereo = lexicals.get(j+1) + ""; 
          estereos.add(stereo); 
          j = j + 2; 
        } // Only simple stereotypes. 
        else 
        { j++; } 
      }

      String tname = lexicals.get(st + 1) + ""; 
 
      Type tt = (Type) ModelElement.lookupByName(tname,types); 
      if (tt == null) 
      { tt = new Type(tname, values);
        tt.addStereotypes(estereos); 
      } 

      return tt; 
    } 
    else if ("usecase".equals(lx))
    { return parseKM3UseCase(st, en, entities, types, gens, pasts); } 
    else        
    { Vector error1 = new Vector(); 
      error1.add("Invalid classifier keyword"); 
      error1.add(showLexicals(st,en)); 
      errors.add(error1); 
      return null; 
    } 

    // Parsing a class. 
    // start is the index of element following the name.

    Entity res; 
    ModelElement melem = 
         ModelElement.lookupByName(rname,entities); 
    if (melem instanceof Entity) 
    { res = (Entity) melem; } 
    else 
    { res = new Entity(rname); 
      entities.add(res); 
    } 
    res.setCardinality("*"); 
	
    String supr = ""; 

    if ("<".equals(lexicals.get(start) + "") && 
        ">".equals(lexicals.get(start + 2) + ""))
    { System.out.println(">>> Generic class, parameter " + 
                         lexicals.get(start + 1));
      String parType = lexicals.get(start + 1) + ""; 
      Entity parEnt = new Entity(parType);
      parEnt.genericParameter = true; 
      parEnt.addStereotype("derived"); 
 
      Type parT = new Type(parEnt); 
      res.addTypeParameter(parT); 
      entities.add(parEnt); 
      start = start + 3;  
    } // But could be several between the <>
    else if ("<".equals(lexicals.get(start) + ""))
    { for (int gi = start+1; gi < en; gi++)
      { String sym = lexicals.get(gi) + ""; 
        if (">".equals(sym))
        { Vector gpars = 
             parse_generic_parameters(start+1,gi-1,entities); 
          System.out.println(">>> Generic class, parameters " 
                             + gpars);
          res.setTypeParameters(gpars); 
          start = gi+1; 
          break; 
        } 
      } 
    } 
     
    // start points to the "{" or the token following 
    // complete class name.      

    if ("extends".equals(lexicals.get(start) + "") || 
        "implements".equals(lexicals.get(start) + ""))
    { supr = lexicals.get(start+1) + ""; 
      int p = start + 1; 
      PreGeneralisation pregen = new PreGeneralisation(); 
      pregen.e1name = rname; 
      pregen.e2name = supr; 
      gens.add(pregen);   // to be linked in UCDArea. 
      System.err.println(">>> " + rname + " specialises " + supr); 

      p++; 
      if ((lexicals.get(p) + "").equals("{"))
      { start = p; }
      else if ((lexicals.get(p) + "").equals("<"))
      { // start = p+1; 
        while (!(lexicals.get(p) + "").equals("{"))
        { supr = lexicals.get(p) + ""; 
          if (">".equals(supr))
          { Vector args = 
              parse_type_sequence(start,p-1,entities,types); 
            pregen.setParameters(args); 
          } 
          p = p+1; 
        } 
        start = p;
      }    
      else 
      { while (!(lexicals.get(p) + "").equals("{"))
        { supr = lexicals.get(p) + ""; 
          if (",".equals(supr)) 
          { } 
          else 
          { PreGeneralisation pregen2 = 
              new PreGeneralisation(); 
            pregen2.e1name = rname; 
            pregen2.e2name = supr; 
            System.err.println(">>> " + rname + " specialises " + supr); 
            gens.add(pregen2);   // to be linked in UCDArea.
          } // it could also have type parameters 
          p = p+1; 
        }  
        start = p;
      }  
    } 
    // else 
    // { start = start + 1; } 

    // start points to the "{"
    
    if (abstr) 
    { res.setAbstract(true); } 
    if (interf) 
    { res.setInterface(true); } 
    // entities.add(res); 

    System.out.println(">>> Parsing KM3 class " + rname); 

    int reached = start; // start of the next element to be parsed. reached <= i

    System.out.println(">>> starting: " + lexicals.get(reached)); 

    for (int i = start + 1; i < en; i++) 
    { String lx2 = lexicals.get(i) + ""; 
      if ("attribute".equals(lx2) || 
          "reference".equals(lx2) || 
          "operation".equals(lx2) ||
          "query".equals(lx2) || // "frozen".equals(lx2) ||
          "static".equals(lx2) || "invariant".equals(lx2) || 
          "stereotype".equals(lx2)) 
      { String lxr = lexicals.get(reached) + ""; 
        if ("attribute".equals(lxr) || 
            "reference".equals(lxr) || "query".equals(lxr) || 
            "operation".equals(lxr) || "static".equals(lxr) || 
            "invariant".equals(lxr) || 
            // "frozen".equals(lxr) ||
            "stereotype".equals(lxr)) 
        { if ("static".equals(lxr))
          { if ("attribute".equals(lexicals.get(reached+1) + ""))
            { Attribute attr = parseAttributeClause(reached+1, i - 1, entities, types);
              if (attr == null) 
              { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 2)); 
                Vector error2 = new Vector(); 
                error2.add("Invalid attribute declaration"); 
                error2.add(showLexicals(reached+1,i-1)); 
                errors.add(error2); 
              } 
              else 
              { atts.add(attr);
                attr.setStatic(true); 
                res.addAttribute(attr);
              } 
            } 
            else if ("operation".equals(lexicals.get(reached+1) + "") || 
                     "query".equals(lexicals.get(reached+1) + "")) 
            { BehaviouralFeature bf = 
                operationDefinition(reached + 2, i-2, entities, types); 
              if (bf != null) 
              { bf.setStatic(true); 
                res.addOperation(bf); 
              } 
              else 
              { System.err.println("!! ERROR: Invalid operation definition: " + 
                               showLexicals(reached+2, i-2));
                Vector error3 = new Vector(); 
                error3.add("Invalid operation declaration"); 
                error3.add(showLexicals(reached+2,i-2)); 
                errors.add(error3); 
              }  
            } 
          } 
          /* else if ("frozen".equals(lxr))
          { if ("attribute".equals(lexicals.get(reached+1) + ""))
            { Attribute attr = parseAttributeClause(reached+1, i - 1, entities, types);
              if (attr == null) 
              { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 2)); 
                Vector error2 = new Vector(); 
                error2.add("Invalid attribute declaration"); 
                error2.add(showLexicals(reached+1,i-1)); 
                errors.add(error2); 
              } 
              else 
              { atts.add(attr);
                attr.setFrozen(true); 
                res.addAttribute(attr);
              } 
            }
          } */ 
          else if ("attribute".equals(lxr))
          { Attribute attr = parseAttributeClause(reached, i - 1, entities, types);
            if (attr == null) 
            { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 1));
              Vector error2 = new Vector(); 
              error2.add("Invalid attribute declaration"); 
              error2.add(showLexicals(reached,i-1)); 
              errors.add(error2); 
            } 
            else 
            { atts.add(attr);
              res.addAttribute(attr);
            } 
          } 
          else if ("reference".equals(lxr))
          { PreAssociation ast = parseAssociationClause(reached, i-1, entities, types); 
            if (ast == null) 
            { System.err.println("!! Cannot parse reference " + 
                                   lexicals.get(reached + 1));
              Vector error4 = new Vector(); 
              error4.add("Invalid reference declaration"); 
              error4.add(showLexicals(reached,i-1)); 
              errors.add(error4); 
            } 
            else 
            { ast.e1name = rname; 
              pasts.add(ast);
              System.out.println(">>> Parsed reference " + ast.e1name + "." + ast.role2 + " : " + ast.e2name); 
            }  
            // res.addAssociation(ast); 
          } 
          else if ("operation".equals(lxr) || "query".equals(lxr)) 
          { BehaviouralFeature bf = 
              operationDefinition(reached + 1, i-2, entities, types); 
            if (bf != null) 
            { res.addOperation(bf); } 
            else 
            { System.err.println("!! ERROR: Invalid operation definition: " + 
                               showLexicals(reached+1,i-2));
              Vector error3 = new Vector(); 
              error3.add("Invalid operation declaration"); 
              error3.add(showLexicals(reached+1,i-2)); 
              errors.add(error3); 
            } 
          }
          else if ("invariant".equals(lxr))
          { Expression expr = parse_expression(0,reached+1,i-2,entities,types); 
            if (expr != null) 
            { Constraint cons = 
                 Constraint.getConstraint(expr); 
              cons.setOwner(res); 
              res.addInvariant(cons);
            }
            else 
            { System.err.println("!! Invalid invariant expression: " + showLexicals(reached+1,i-2)); 
              checkBrackets(reached+1,i-2); 
              Vector error5 = new Vector(); 
              error5.add("Invalid invariant"); 
              error5.add(showLexicals(reached+1,i-2)); 
              errors.add(error5); 
            }
          }  
          else if ("stereotype".equals(lxr))
          { String stereo = ""; 
	       for (int kk = reached+1; kk <= i-2; kk++)
	       { // System.out.println(lexicals.get(k) + ""); 
               String lxstr = lexicals.get(kk) + ""; 
               stereo = stereo + lxstr;  
             }
             res.addStereotype(stereo); 
           } 
		    /* Expression expr = parse_expression(0,reached+1,i-2); 
		    if (expr != null) 
			{ if (expr instanceof BasicExpression) 
			  { res.addStereotype(expr + ""); }
			  else if (expr instanceof BinaryExpression)
			  { BinaryExpression binexpr = (BinaryExpression) expr; 
			    res.addStereotype(binexpr.left + "" + binexpr.operator + "" + binexpr.right); 
			  }
			  else 
			  { System.err.println("!! Unexpected form of stereotype expression: " + expr); }
			} */ 
			// else 
			// { System.err.println("!! Invalid stereotype expression: " + showLexicals(reached+1,i-2)); 
			//   checkBrackets(reached+1,i-2); 
			//   Vector error5 = new Vector(); 
			//   error5.add("Invalid stereotype"); 
			//   error5.add(showLexicals(reached+1,i-2)); 
			//   errors.add(error5); 
			// }
		  // }  
          reached = i;   // one element has been parsed, go on to start of next one. 
        } 
        else 
        { reached = i; 
          i++; 
        } 
        if ("static".equals(lx2)) { i++; } // don't test the next keyword. 
      }
    }  

    if (reached + 2 <= en - 1) 
    { boolean isStatic = false; 
      
      if ("static".equals(lexicals.get(reached) + ""))
      { isStatic = true; 
        reached = reached + 1; 
      } 
      
      if ("attribute".equals(lexicals.get(reached) + ""))
      { Attribute aa = parseAttributeClause(reached, en-1, entities, types); 
        if (aa == null) 
        { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 1)); 
          Vector error2 = new Vector(); 
          error2.add("Invalid attribute declaration"); 
          error2.add(showLexicals(reached,en-1)); 
          errors.add(error2); 
        } 
        else 
        { aa.setStatic(isStatic); 
          atts.add(aa); 
          res.addAttribute(aa);
        } 
      } 
      else if ("reference".equals(lexicals.get(reached) + ""))
      { PreAssociation ast = parseAssociationClause(reached, en-1, entities, types); 
        if (ast == null) 
        { System.err.println("!! Cannot parse reference " + 
                                   lexicals.get(reached + 1)); 
          Vector error4 = new Vector(); 
          error4.add("Invalid reference declaration"); 
          error4.add(showLexicals(reached,en-1)); 
          errors.add(error4); 
        } 
        else 
        { ast.e1name = rname; 
          pasts.add(ast);
          System.out.println(">>> Parsed reference " + 
              ast.e1name + "." + ast.role2 + " : " + 
              ast.e2name); 
        }  
        // res.addAssociation(ast); 
      }  
      else if ("operation".equals(lexicals.get(reached) + "") ||
               "query".equals(lexicals.get(reached) + "")) 
      { BehaviouralFeature bf = 
              operationDefinition(reached + 1, en-2, entities, types); 
        if (bf != null) 
        { bf.setStatic(isStatic); 
          res.addOperation(bf);
        } 
        else 
        { System.err.println("!! ERROR: Invalid operation definition: " + 
                             showLexicals(reached+1,en-2));
           Vector error3 = new Vector(); 
           error3.add("Invalid operation declaration"); 
           error3.add(showLexicals(reached+1,en-2));   
           errors.add(error3); 
        } 
      } 
      else if ("invariant".equals(lexicals.get(reached) + ""))
      { Expression expr = parse_expression(0,reached+1,en-2,entities,types); 
        if (expr != null) 
        { Constraint cons = Constraint.getConstraint(expr); 
          res.addInvariant(cons);
        }
        else 
        { System.err.println("!! Invalid invariant expression: " + showLexicals(reached+1,en-2)); 
          checkBrackets(reached+1,en-2); 
          Vector error5 = new Vector(); 
          error5.add("Invalid invariant"); 
          error5.add(showLexicals(reached+1,en-2)); 
          errors.add(error5); 
        }
      }  
      else if ("stereotype".equals(lexicals.get(reached) + ""))
	  { /* Expression expr = parse_expression(0,reached+1,en-2); 
         if (expr != null) 
    	    { if (expr instanceof BasicExpression) 
           { res.addStereotype(expr + ""); }
		  else if (expr instanceof BinaryExpression)
		  { BinaryExpression binexpr = (BinaryExpression) expr; 
		    res.addStereotype(binexpr.left + "" + binexpr.operator + "" + binexpr.right); 
		  }
		  else 
		  { System.err.println("!! Unexpected form of stereotype expression: " + expr); }
	    }
		else 
		{ System.err.println("!! Invalid stereotype expression: " + showLexicals(reached+1,en-2)); 
		  checkBrackets(reached+1,en-2); 
	      Vector error5 = new Vector(); 
		  error5.add("Invalid stereotype"); 
		  error5.add(showLexicals(reached+1,en-2)); 
		  errors.add(error5); 
		} */ 
		String stereo = ""; 
		for (int kk = reached+1; kk <= en-2; kk++)
           { // System.out.println(lexicals.get(k) + ""); 
             String lxstr = lexicals.get(kk) + ""; 
             stereo = stereo + lxstr;  
           }
		res.addStereotype(stereo); 
	  }   
    } 

    // remove res.typeParameters from entities
    Vector tpars = res.getTypeParameters();
    removeTypeParameters(tpars, entities);
 
    return res; 
  } 

  private void removeTypeParameters(Vector tpars, Vector entities)
  { Vector parEnts = new Vector(); 
    for (int i = 0; i < tpars.size(); i++) 
    { Type tp = (Type) tpars.get(i); 
      if (tp.isEntity())
      { parEnts.add(tp.getEntity()); } 
    } 
    entities.removeAll(parEnts); 
  } 

  public void parseUseCaseParameters(UseCase uc, int st, int en, Vector entities, Vector types)  
  {	int j = st; 
	while (j <= en) 
	{ String jx = lexicals.get(j) + "";
       if ("includes".equals(jx))
       { String inc = lexicals.get(j+1) + ""; 
         uc.addIncludes(inc);
         j = j + 3;  
       } 
       else if ("extendedBy".equals(jx))
       { String ext = lexicals.get(j+1) + ""; 
         uc.addExtends(ext);
         j = j + 3;  
      }  
      else if ("stereotype".equals(jx))
      { String stereo = lexicals.get(j+1) + ""; 
        for (int k = j+2; k <= en; k++)
        { // System.out.println(lexicals.get(k) + ""); 
          String lx = lexicals.get(k) + ""; 
          if (";".equals(lx))
          { uc.addStereotype(stereo);
            j = k; 
            k = en; // end the loop
	     }
          else 
          { stereo = stereo + lx; } 
        } 
         // uc.addStereotype(stereo);
         // j = j + 3; 
		 j++;  
      }  
      else if ("parameter".equals(jx))
      { String p = lexicals.get(j+1) + ""; 
        for (int k = j+2; k <= en; k++)
        { // System.out.println(lexicals.get(k) + ""); 
		
          if (";".equals(lexicals.get(k) + ""))
          { Type ptype = parseType(j+3,k-1,entities,types); 
            if (ptype != null) 
            { uc.addParameter(p,ptype);
              j = k; 
              k = en; 
            }
            else 
            { System.err.println("!! Invalid parameter type: " + showLexicals(j+3,k-1)); }
          } 
        } 
        j++;  
      }
      else if ("attribute".equals(jx))
      { String p = lexicals.get(j+1) + ""; 
        for (int k = j+2; k <= en; k++)
        { // System.out.println(lexicals.get(k) + ""); 
		
          if (";".equals(lexicals.get(k) + ""))
          { Type ptype = parseType(j+3,k-1,entities,types); 
            if (ptype != null) 
            { uc.addAttribute(p,ptype); // it is automatically static 
              j = k; 
              k = en; 
            }
            else 
            { System.err.println("!! Invalid attribute type: " + showLexicals(j+3,k-1)); }
          } 
        } 
        j++;  
      }
      else if ("query".equals(jx))   
      { for (int k = j+1; k <= en; k++)
        { // System.out.println(lexicals.get(k) + ""); 
		
          if (";".equals(lexicals.get(k) + ""))
          { 
            BehaviouralFeature bf = 
                operationDefinition(j+1, k-1, entities, types); 
            if (bf != null) 
            { bf.setStatic(true); 
              uc.addOperation(bf);
	         j = k; 
	         k = en;      
               System.out.println(">>> Recognised query operation: " + bf); 
            } 
            else 
            { System.err.println("!! ERROR: Invalid operation definition: " + 
                                 showLexicals(j, k-1));
            }
          }
        } 
        j++;   
      } 
      else if ("precondition".equals(jx))
      { for (int k = j+1; k <= en; k++)
        { // System.out.println(lexicals.get(k) + ""); 
		
          if (";".equals(lexicals.get(k) + ""))
          { Expression expr = parse_expression(0,j+1,k-1,entities,types); 
            if (expr != null) 
            { Constraint cons = Constraint.getConstraint(expr); 
              uc.addPrecondition(cons);
              j = k; 
              k = en; 
            }
            else 
            { System.err.println("!! Invalid precondition expression: " + showLexicals(j+1,k-1)); }
		} 
         } 
         j++;  
       }
       else 
       { j++; }   		
    }
  } 

  public Entity parseEntityName(int st, int en, 
                     Vector entities, Vector types)
  { // identifier, or identifier < seq of types >

    if (en == st)
    { String ename = lexicals.get(st) + ""; 
      Entity ent = 
         (Entity) ModelElement.lookupByName(ename,entities); 
      if (ent == null) 
      { ent = new Entity(ename); 
        return ent; 
      } 
    } 

    if (st < en)
    { String ename = lexicals.get(st) + ""; 
      Entity ent = 
         (Entity) ModelElement.lookupByName(ename,entities); 
      if (ent == null) 
      { ent = new Entity(ename); }

      if ("<".equals(lexicals.get(st+1) + "") && 
          ">".equals(lexicals.get(en) + ""))
      { Vector args = 
          parse_type_sequence(st+1,en-1,entities,types); 
        if (args != null) 
        { ent.setTypeParameters(args); 
          // No: if args /= ent.typeParameters (non-empty) 
          // then create a new instantiation of ent. 
          return ent; 
        } 
      } 
    } 

    return null; 
  } 
      
 

  public UseCase parseKM3UseCase(int st, int en, 
                          Vector entities, Vector types, 
                          Vector gens, Vector pasts)
  { // usecase name : type { parameters postconditions activity }
    String nme = lexicals.get(st+1) + "";
    String colon = lexicals.get(st+2) + "";
    Type uctyp = new Type("void",null);  // default 
    int openingBracket = st + 4; 
    boolean foundstart = false; 
    Vector ucpars = new Vector(); 
	
    for (int i = st + 3; i < en && !foundstart; i++)
    { String lx = lexicals.get(i) + ""; 
      if ("{".equals(lx))
      { openingBracket = i; 
        foundstart = true; 

        if (":".equals(colon)) // usecase uc : type {
        { uctyp = parseType(st+3,i-1,entities,types); }
        else // usecase uc ( pars ) : type {
        { boolean foundtype = false; 
          for (int j = i-1; j > st + 1 && !foundtype; j--)
          { String jx = lexicals.get(j) + ""; 
            if (":".equals(jx))
            { uctyp = parseType(j+1,i-1,entities,types); 
              foundtype = true; 
              if ("(".equals(lexicals.get(st+2) + "") &&
                  ")".equals(lexicals.get(j-1) + ""))
              { ucpars = 
                  parseAttributeDecs(st+3, j-2,
                                     entities, types); 
              } 
            } 
          } 
        } 
      } 
    }  
        
    UseCase uc = new UseCase(nme); 
    uc.setResultType(uctyp); 
    System.out.println(">>> use case " + nme + " has return type " + uctyp); 
    uc.setParameters(ucpars); 
    System.out.println(">>> use case " + nme + " has parameters " + ucpars); 

    if (foundstart == false) 
    { System.err.println("!! ERROR: use case syntax is: usecase " + nme + " : type { ... }");
      return uc;
    }
	
    int startpostconditions = st + 4;
    int endpostconditions = en;
    int startactivity = en;   
    boolean foundpostconditions = false; 
    boolean foundactivity = false; 
	
	
    for (int i = st + 4; i < en; i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("activity".equals(lx) && ":".equals(lexicals.get(i+1) + ""))
      { foundactivity = true;
        endpostconditions = i;
        startactivity = i+2;  
      }
    }
    
    for (int i = st + 4; i < en && i < endpostconditions; i++) 
    { String lx = lexicals.get(i) + ""; 
      if (":".equals(lx) && ":".equals(lexicals.get(i+1) + ""))
      { String scope = lexicals.get(i-1) + ""; // must be present, can be void
        Entity ent = (Entity) ModelElement.lookupByName(scope, entities); 
		
        if (foundpostconditions == false)
        { foundpostconditions = true; 
          startpostconditions = i-1; 
          parseUseCaseParameters(uc,st+2,i,entities,types); 
        }

        for (int j = i+2; j < en && j < endpostconditions; j++) 
        { if (":".equals(lexicals.get(j) + "") && ":".equals(lexicals.get(j+1) + ""))
          { Expression ee; 
            if (";".equals(lexicals.get(j-2) + ""))
            { ee = parse_expression(0, i+2, j-3,entities,types); } 
            else 
            { ee = parse_expression(0, i+2, j-2,entities,types); } 

            if (ee == null) 
            { checkBrackets(i+2,j-2); } 
            System.out.println(">>>> Parsed use case postcondition " + ee); 

            if (ee != null && (ee instanceof BinaryExpression))
            { BinaryExpression be = (BinaryExpression) ee; 
              if ("=>".equals(be.operator))
              { Constraint con = new Constraint(be.left,be.right); 
                con.setOwner(ent); 
                uc.addPostcondition(con);
                con.setUseCase(uc); 
              } 
              else  
              { Constraint cc = new Constraint(new BasicExpression(true), ee); 
                cc.setOwner(ent); 
                uc.addPostcondition(cc); 
                cc.setUseCase(uc); 
              }   
            } 
            else if (ee != null) 
            { Constraint cc = new Constraint(new BasicExpression(true), ee); 
              cc.setOwner(ent); 
              uc.addPostcondition(cc);
              cc.setUseCase(uc);  
            }   
            j = en; 
          }
          else if (j == endpostconditions-1)  // the closing } 
          { Expression ff; 
            if (";".equals(lexicals.get(j) + ""))
            { ff = parse_expression(0, i+2, j-1,entities,types); } 
            else 
            { ff = parse_expression(0, i+2, j,entities,types); } 

            if (ff == null)
            { checkBrackets(i+2,j); }
			 
            System.out.println(">>>> Parsed final use case postcondition " + ff); 
            if (ff != null && (ff instanceof BinaryExpression))
            { BinaryExpression fe = (BinaryExpression) ff; 
              if ("=>".equals(fe.operator))
              { Constraint con = new Constraint(fe.left,fe.right); 
                con.setOwner(ent); 
                uc.addPostcondition(con);
                con.setUseCase(uc); 
              } 
              else  
              { Constraint cc = new Constraint(new BasicExpression(true), ff); 
                cc.setOwner(ent); 
                uc.addPostcondition(cc); 
                cc.setUseCase(uc); 
              }   
            }
            else if (ff != null) 
            { Constraint cc = new Constraint(new BasicExpression(true), ff); 
              cc.setOwner(ent); 
              uc.addPostcondition(cc); 
              cc.setUseCase(uc); 
            }   
            j = en; 
          }
        } 
      }  
    } 

    if (foundpostconditions == false && foundactivity == true)
    { parseUseCaseParameters(uc,st+4,startactivity-2,entities,types);
      int actualend = en-1; 
      if (";".equals(lexicals.get(actualend) + ""))
      { actualend = en-2; } 

      Statement stat = parseStatement(startactivity,actualend,entities,types); 
	  if (stat != null)
	  { uc.setActivity(stat); } 
	  else
	  { checkBrackets(startactivity,actualend); 
	    System.err.println("!! Invalid statement at: " + showLexicals(startactivity,actualend)); 
      }
    }
    else if (foundactivity == true)
    { int actualend = en-1; 
      if (";".equals(lexicals.get(actualend) + ""))
      { actualend = en-2; } 
      Statement stat1 = parseStatement(startactivity,actualend,entities,types); 
      if (stat1 != null)
      { uc.setActivity(stat1); } 
      else
      { checkBrackets(startactivity,actualend); 
        System.err.println("!! Invalid statement at: " + showLexicals(startactivity,actualend)); 
      }
    }

    if (foundpostconditions == false && foundactivity == false)
    { int actualend = en-1; 
      if (";".equals(lexicals.get(actualend) + ""))
      { actualend = en-2; }
	  parseUseCaseParameters(uc,st+4,actualend,entities,types);
    } 
	
    return uc; 
  } 

  public Attribute parseAttributeClause(int st, int en, Vector entities, Vector types)
  { // attribute name [stereotypes] : type ;
    // attribute name [stereotypes] : type := init ;  

    String nme = lexicals.get(st+1) + ""; 

    for (int i = st+2; i < en; i++) 
    { if (":".equals(lexicals.get(i) + ""))
      { for (int j = i+1; j < en; j++) 
        { if (":=".equals(lexicals.get(j) + ""))
          { Expression init = 
              parse_expression(0,j+1,en-1,entities,types); 
            Type typ = 
              parseType(i+1,j-1,entities,types);
 
            if (typ != null) 
            { Attribute att = 
                 new Attribute(nme,typ,ModelElement.INTERNAL);
              att.setElementType(typ.getElementType());  
              System.out.println(">>> Attribute " + nme + ". Type = " + typ + ", elementType = " + att.getElementType()); 

              if (init != null) 
              { att.setInitialExpression(init); 
                System.out.println(">>> Attribute " + nme + ". Initial expression = " + init); 
              }

              for (int k = st+2; k < i; k++) 
              { String lx = lexicals.get(k) + ""; 
                if ("identity".equals(lx))
                { att.stereotypes.add("identity"); 
                  att.setIdentity(true); 
                }
                else if ("static".equals(lx))
                { att.stereotypes.add("static"); 
                  att.setStatic(true); 
                }
                else if ("derived".equals(lx))
                { att.stereotypes.add("derived"); 
                  att.setDerived(true); 
                }
                else if ("frozen".equals(lx))
                { // att.stereotypes.add("readOnly"); 
                  att.setFrozen(true); 
                }
              } 

              return att;  
            } 
            else 
            { System.err.println("!! Error: Unrecognised type at " + showLexicals(i+1,j-1)); }  
          } 
        } 

        Type typ = parseType(i+1,en-1,entities,types); 

        if (typ != null) 
        { Attribute att = 
              new Attribute(nme,typ,ModelElement.INTERNAL);
          att.setElementType(typ.getElementType());  
          System.out.println(">>> Attribute " + nme + ". Type = " + typ + ", elementType = " + att.getElementType()); 

          for (int j = st+2; j < i; j++) 
          { String lx = lexicals.get(j) + ""; 
            if ("identity".equals(lx))
            { att.stereotypes.add("identity"); 
              att.setIdentity(true); 
            }
            else if ("static".equals(lx))
            { att.stereotypes.add("static"); 
              att.setStatic(true); 
            }
            else if ("derived".equals(lx))
            { att.stereotypes.add("derived"); 
              att.setDerived(true); 
            }
            else if ("frozen".equals(lx))
            { // att.stereotypes.add("readOnly"); 
              att.setFrozen(true); 
            }
          } 

          return att;
        }
        else 
        { System.err.println("!! Error: Unrecognised type at " + showLexicals(i+1,en-1)); }  
      }  
    } 
    return null; 
  } 
    
  public PreAssociation parseAssociationClause(int st, int en, Vector entities, Vector types)
  { // reference role2 [cards] [stereotypes] : Entity2 [opp] ; 

    PreAssociation res = new PreAssociation();  
    res.role2 = lexicals.get(st+1) + "";
    res.card2 = ModelElement.ONE;   // default
    int reached = st+2; 
    for (int i = st+2; i < en; i++) 
    { if (":".equals(lexicals.get(i) + ""))
      { String tt = lexicals.get(i+1) + ""; 
        res.e2name = tt; 
		
        for (int j = st+2; j < i; j++) 
        { String lx = lexicals.get(j) + ""; 
          if ("ordered".equals(lx))
          { res.stereotypes.add("ordered"); } 
          else if ("container".equals(lx))
          { res.stereotypes.add("aggregation"); } 
          else if ("qualified".equals(lx))
          { res.stereotypes.add("qualified"); } 
          else if ("[".equals(lx))
          { if ("*".equals(lexicals.get(j+1) + ""))
            { // upper and lower are MANY 
              res.card2 = ModelElement.MANY; 
            }
            else if ("0".equals(lexicals.get(j+1) + "") && j+3 <= i &&  
                     "-".equals(lexicals.get(j+2) + "") && 
                     "1".equals(lexicals.get(j+3) + ""))
            { res.card2 = ModelElement.ZEROONE; } 
            else if ("1".equals(lexicals.get(j+1) + "") && j+2 <= i &&  
                     "-".equals(lexicals.get(j+2) + ""))
            { res.card2 = ModelElement.MANY; } 
            else if (j+2 <= i && "-".equals(lexicals.get(j+2) + ""))
            { res.card2 = ModelElement.MANY; } 
            else 
            { res.card2 = ModelElement.ONE; } 
          }  
        } 
        reached = i+2; 
      } 
    }

    String rlex = lexicals.get(reached) + ""; 
      // token after type name if any

    if (";".equals(rlex)) 
    { return res; } 
    else if ("oppositeOf".equals(rlex))
    { res.role1 = lexicals.get(reached+1) + ""; 
      return res; 
    }  
    else if ("Set".equals(res.e2name) && "(".equals(rlex))
    { res.e2name = lexicals.get(reached+1) + ""; 
      res.card2 = ModelElement.MANY; 
    } 
    else if ("Sequence".equals(res.e2name) && 
"(".equals(rlex))
    { res.e2name = lexicals.get(reached+1) + ""; 
      res.card2 = ModelElement.MANY; 
      res.stereotypes.add("ordered"); 
    }
    else if (":=".equals(rlex))
    { Expression init = 
              parse_expression(0,reached+1,en-1,entities,types); 
      if (init != null) 
      { res.setInitialExpression(init); 
        System.out.println(">>> Reference " + res.role2 + ". Initial expression = " + init); 
      }
    } 
          
    return res; 
  } 

  // public Statement parseATLStatement()
  // { return parseATLStatement(0, lexicals.size() - 1, new Vector(), new Vector()); } 

  public Statement parseATLStatement(int s, int e, Vector entities, Vector types)
  { Statement st = parseATLSequenceStatement(s,e,entities,types);
    if (st == null)
    { st = parseATLLoopStatement(s,e,entities,types); }
    if (st == null)
    { st = parseATLConditionalStatement(s,e,entities,types); }
    if (st == null && ";".equals(lexicals.get(e) + ""))
    { st = parseATLBasicStatement(s,e-1,entities,types); }
    return st;
  }


  public Statement parseATLSequenceStatement(int s, int e, Vector entities, Vector types)
  { Statement s1 = null;
    Statement s2 = null;
    for (int i = s; i < e; i++)
    { String ss = lexicals.get(i) + "";
      if (ss.equals(";") || ss.equals("}"))
      { s1 = parseATLStatement(s,i,entities,types);
        s2 = parseATLStatement(i+1,e,entities,types);
        if (s1 == null || s2 == null) { continue; } // try another ;  
        // { return null; }
        else
        { SequenceStatement res = new SequenceStatement();
          res.addStatement(s1);
          res.addStatement(s2);
          // System.out.println("Parsed ATL sequence statement: " + res);
          return res;
        }
      }
    }
    return null;
  }

  public Statement parseATLLoopStatement(int s, int e, Vector entities, Vector types)
  { if ("for".equals(lexicals.get(s) + "") && 
        "in".equals(lexicals.get(s+3) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("{") && "}".equals(lexicals.get(e) + ""))
        { Expression var = parse_ATLexpression(0,s+2,s+2,entities,types);
          Expression range = parse_ATLexpression(0,s+4,i-2,entities,types); 
          s1 = parseATLStatement(i+1, e-1,entities,types);
          if (var == null || range == null || s1 == null)
          { return null; }
          test = new BinaryExpression(":",var,range); 
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.FOR); 
          res.setLoopRange(var, range); 
          // System.out.println("Parsed ATL for statement: " + res);
          return res;
        }
      }
    }
    else if ("while".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("{"))
        { test = parse_ATLexpression(0,s+1,i-1,entities,types);
          s1 = parseATLStatement(i+1, e,entities,types);
          if (s1 == null || test == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.WHILE); 
          // System.out.println("Parsed ATL while statement: " + res);
          return res;
        }
      }
    } 
    return null;
  }

  public Statement parseATLConditionalStatement(int s, int e, Vector entities, Vector types)
  { if ("if".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Statement s2 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("{") && "}".equals(lexicals.get(e) + ""))
        { test = parse_ATLexpression(0,s+1,i-1,entities,types);
          if (test == null) { continue; } 
          
          for (int j = i+1; j < e; j++)
          { String ss1 = lexicals.get(j) + "";
            if (ss1.equals("else") && "{".equals(lexicals.get(j+1) + ""))
            { s1 = parseATLStatement(i+1, j-2,entities,types);
              s2 = parseATLStatement(j+2, e-1,entities,types);
              if (s1 == null || s2 == null || test == null)
              { continue; }
              IfStatement res = new IfStatement(test,s1,s2);
              // System.out.println("Parsed ATL if statement: " + res);
              return res;
            }
          }
          s1 = parseATLStatement(i+1,e-1,entities,types); 
          if (s1 != null)
          { return new IfStatement(test,s1); } 
        }
      }
    }
    return null;
  }

  public Statement parseATLBasicStatement(int s, int e, Vector entities, Vector types)
  { if (e == s)
    { if ("skip".equals(lexicals.get(e) + ""))
      { return new InvocationStatement("skip",null,null); }
       // operation call
      else if ("return".equals(lexicals.get(e) + ""))
      { return new ReturnStatement(); } 
      else 
      { Expression ee = parse_ATLbasic_expression(0,s,e,entities,types);
        InvocationStatement istat = new InvocationStatement(ee + "",null,null);
        istat.setCallExp(ee); 
        return istat; 
      }
    }
    else if ("return".equals(lexicals.get(s) + ""))
    { Expression ret = parse_ATLexpression(0,s+1,e,entities,types); 
      if (ret != null) 
      { return new ReturnStatement(ret); }
      else 
      { System.err.println("Invalid expression in return: " + showLexicals(s+1,e)); }    
    } 
    else if ("catch".equals(lexicals.get(s) + "") && 
             "(".equals(lexicals.get(s+1) + ""))
    { for (int p = s+2; p < e; p++) 
      { String lxc = lexicals.get(p) + ""; 
        if (")".equals(lxc) && 
            "do".equals(lexicals.get(p+1) + ""))
        { Expression catchcond = parse_ATLexpression(0,s+2,p-1,entities,types); 
          Statement catchaction = parseATLStatement(p+2,e,entities,types); 
          if (catchcond != null && catchaction != null) 
          { return new CatchStatement(catchcond,catchaction); }
        } 
      } 
      System.err.println("Invalid catch statement: " + showLexicals(s,e));     
    } 
    else if ("error".equals(lexicals.get(s) + ""))
    { Expression ret = parse_ATLexpression(0,s+1,e,entities,types); 
      if (ret != null) 
      { return new ErrorStatement(ret); }
      else 
      { System.err.println("Invalid expression in error statement: " + showLexicals(s+1,e)); }    
    } 
    else if ("assert".equals(lexicals.get(s) + ""))
    { for (int p = s+1; p < e; p++) 
      { String lxc = lexicals.get(p) + ""; 
        if ("do".equals(lxc))
        { Expression assertcond = parse_ATLexpression(0,s+1,p-1,entities,types); 
          Expression assertmsg = parse_ATLexpression(0,p+1,e,entities,types); 
          if (assertcond != null && assertmsg != null) 
          { return new AssertStatement(assertcond,assertmsg); }
        } 
      } 
      Expression assertc = parse_ATLexpression(0,s+1,e,entities,types);
      if (assertc != null) 
      { return new AssertStatement(assertc); } 
      System.err.println("!! Invalid assert statement: " + showLexicals(s,e));     
    } 
    else if ("var".equals(lexicals.get(s) + ""))  // for ETL
    { Expression lft = parse_ATLbasic_expression(0,s+1,s+1,entities,types); 
      if ("new".equals(lexicals.get(s+3) + "")) 
      { //  var v = new E() in ETL
        Type tt = parseType(s+4,e-2,entities,types); 
        if (tt != null) 
        { CreationStatement cr = new CreationStatement(tt.getName(), lft + ""); 
          cr.setType(tt); 
          return cr; 
        }
      }  
      Expression rgt = parse_ATLexpression(0,s+3,e,entities,types); 
      if (lft != null && rgt != null) 
      { // var lft = rgt; in ETL. 
        rgt.typeCheck(types,entities,new Vector(), new Vector()); 
        CreationStatement cr = new CreationStatement(rgt.getType() + "", lft + ""); 
        AssignStatement res = new AssignStatement(lft,rgt);
        res.setOperator("=");
        SequenceStatement ss = new SequenceStatement(); 
        ss.addStatement(cr); ss.addStatement(res); 
        return ss;   
      } // actually needs to declare lft. 
    }  
    else if ("delete".equals(lexicals.get(s) + ""))   // for ETL
    { Expression dexp = parse_ATLexpression(0,s+1,e,entities,types); 
      if (dexp != null) 
      { dexp.setBrackets(true); 
        Expression del = new UnaryExpression("->isDeleted", dexp); 
        return new ImplicitInvocationStatement(del); 
      }
    }  
    else 
    { for (int i = s; i < e; i++) 
      { String ss = lexicals.get(i) + ""; 
         
        // System.out.println("Trying to parse assignment " + ss); 

        if (i+1 < e && "<".equals(ss) && "-".equals(lexicals.get(i+1) + "") ) 
        { Expression e1 = parse_ATLbasic_expression(0,s,i-1,entities,types); 
          Expression e2 = parse_ATLexpression(0,i+2,e,entities,types); 
          if (e1 == null || e2 == null)
          { return null; } 
          System.out.println("Parsed ATL assignment: " + e1 + " <- " + e2); 
          AssignStatement res = new AssignStatement(e1,e2);
          res.setOperator("<-"); 
          return res;  
        } 
        else if (i+1 < e && ":".equals(ss) && ":=".equals(lexicals.get(i+1) + "") ) 
        { Expression e1 = parse_ATLbasic_expression(0,s,i-1,entities,types); 
          Expression e2 = parse_ATLexpression(0,i+2,e,entities,types); 
          if (e1 == null || e2 == null)
          { return null; } 
          System.out.println("Parsed ETL assignment: " + e1 + " ::= " + e2);
          BasicExpression equiv = new BasicExpression("equivalent"); 
          equiv.objectRef = e2;  
          equiv.setIsEvent();
          equiv.setParameters(new Vector());

          AssignStatement res = new AssignStatement(e1,equiv);
          res.setOperator("="); 
          return res;  
        } 
        else if ("=".equals(ss)) 
        { Expression e1 = parse_ATLbasic_expression(0,s,i-1,entities,types); 
          Expression e2 = parse_ATLexpression(0,i+1,e,entities,types); 
          if (e1 == null || e2 == null)
          { return null; } 
          System.out.println("Parsed assignment: " + e1 + " = " + e2); 
          AssignStatement rr = new AssignStatement(e1,e2);
          rr.setOperator("=");  
          return rr; 
        }
      } // = or ::= in ETL. 
    }
    Expression exp = parse_ATLexpression(0,s,e,entities,types); 
    if (exp != null) 
    { return new ImplicitInvocationStatement(exp); } 
    return null;
  }

  public Statement parseATLStatement()
  { Vector v1 = new Vector(); 
    Vector v2 = new Vector(); 
    return parseATLStatement(v1,v2); }

  public Statement parseATLStatement(Vector entities, Vector types)
  { return parseATLStatement(0,lexicals.size()-1,entities,types); }


  public ATLModule parseATL(Vector entities, Vector types)
  { String keyword = lexicals.get(0) + ""; 

    if (("module".equals(keyword) || 
         "library".equals(keyword)) && 
        ";".equals(lexicals.get(2) + ""))
    { String mname = lexicals.get(1) + ""; 
      ATLModule mod = new ATLModule(mname);
      Compiler2.atlModuleName = mname; 
 
      for (int i = 0; i < lexicals.size();  i++) 
      { if ("lazy".equals(lexicals.get(i) + "") || 
            "helper".equals(lexicals.get(i) + "") ||
            "unique".equals(lexicals.get(i) + "") || 
            "rule".equals(lexicals.get(i) + ""))
        { Vector v = parse_ATL_module(i,lexicals.size()-1,
                                      entities,types,mod); 
          // mod.setElements(v); 
          return mod; 
        }
      } 
    } 
    return null; 
  } 

  public Vector parse_ATL_module(int st, int en, Vector entities, Vector types, ATLModule mod)
  { Vector res = new Vector(); 
    if (st >= en)
    { return res; } 
    for (int i = st + 2; i <= en;  i++) 
    { if ("lazy".equals(lexicals.get(i) + "") || 
          "rule".equals(lexicals.get(i) + "") ||
          "unique".equals(lexicals.get(i) + "") || 
          "helper".equals(lexicals.get(i) + ""))
      { Rule r = parseATLrule(st,i-1,entities,types,mod);
        if (r != null) 
        { mod.addElement(r); } 
        res = parse_ATL_module(i,en,entities,types,mod);  
        if (r != null) { res.add(0,r); }  
        return res; 
      }
    } 

    Rule r1 = parseATLrule(st,en,entities,types,mod);
    if (r1 != null) 
    { mod.addElement(r1); 
      res.add(r1); 
    }      
    return res; 
  } 

  public MatchedRule parseATLrule(int st, int en, 
                Vector entities, Vector types, ATLModule mod)
  { boolean lazy = false; 
    boolean unique = false; 
    String rname = ""; 
    int start = st; 
    int rulestart = st; 

    if ("lazy".equals(lexicals.get(st) + "") &&
        "rule".equals(lexicals.get(st + 1) + "") &&  
        en - st >= 4 && "}".equals(lexicals.get(en) + ""))
    { lazy = true; 
      start = st + 4; 
      rname = lexicals.get(st + 2) + "";
      rulestart = st+3;  
    } 
    else if ("unique".equals(lexicals.get(st) + "") &&
        "lazy".equals(lexicals.get(st + 1) + "") &&  
        en - st >= 5 && "}".equals(lexicals.get(en) + ""))
    { lazy = true; 
      unique = true; 
      start = st + 5; 
      rname = lexicals.get(st + 3) + "";
      rulestart = st + 4;  
    } 
    else if ("rule".equals(lexicals.get(st) + "") && 
        en - st >= 4 && "}".equals(lexicals.get(en) + ""))
    { lazy = false; 
      start = st + 3; 
      rname = lexicals.get(st + 1) + "";
      rulestart = st + 2; 
    } 
    else if ("helper".equals(lexicals.get(st) + "") && 
        en - st >= 9 && 
        "context".equals(lexicals.get(st + 1) + "") && 
        ";".equals(lexicals.get(en) + "")) 
    { // helper context Ent def : name ( pars ) : Rtype = expr ;
      // helper context Ent def : name : Rtype = expr ; 

      for (int t = st + 2; t < en; t++) 
      { String lext = lexicals.get(t) + ""; 
        if ("def".equals(lext))
        { Type contextType = 
                  parseType(st+2,t-1,entities,types); 
          
          String ent = contextType + ""; 
          String attname = lexicals.get(t + 2) + ""; 

          System.out.println(">>> Helper operation " + attname + " on context " + contextType); 

          if ("(".equals(lexicals.get(t + 3) + ""))
          { // helper operation of an entity or built-in type
            for (int p = t+4; p < en; p++) 
            { String ps = lexicals.get(p) + ""; 
              if (ps.equals("="))
              { for (int q = p-1; q > t+4; q--) 
                { String qs = lexicals.get(q) + ""; 
                  if (qs.equals(":"))
                  { Vector params = 
                      parseAttributeDecs(t+4,q-2,entities,types);
                    Type atType = 
                      parseType(q+1,p-1,entities,types); 
                    Expression defn = 
                      parse_ATLexpression(0,p+1,en-1,entities,types); 
                    if (defn == null) 
                    { System.err.println("!! Invalid ATL expression: " + showLexicals(p+1,en-1)); 
                      return null; 
                    }
                    BehaviouralFeature bf = 
                      new BehaviouralFeature(
                            attname,params,true,atType);
                    // bf.setElementType(atType.getElementType());  
                    Expression resexp = new BasicExpression("result"); 
                    resexp.setType(atType); 
                    resexp.setUmlKind(Expression.VARIABLE); 
                    defn.setBrackets(true); 
                    Entity entet = (Entity) ModelElement.lookupByName(ent,entities); 
                    bf.setOwner(entet); 
                    Expression newdefn = defn; 

                    if (entet != null) 
                    { entet.addOperation(bf); } 
                    else if (contextType != null)
                    { Attribute newpar = 
                        new Attribute("atlself", 
                          contextType, ModelElement.INTERNAL); 
                      bf.addFirstParameter(newpar);
                      BasicExpression var = 
                        new BasicExpression(newpar); 
                      newdefn = 
                        defn.substituteEq("self", var);                        
                    }
                    Expression post = 
                      new BinaryExpression("=", resexp, 
                                           newdefn); 
                
                    bf.setPost(post); 
                    mod.addOperation(bf);   
                    return null; 
                  } 
                }
              }
            } 
          }
          else  
          { // helper context Type def : nme : Rtype = expr ; 
            // helper 'attribute' - actually a cached operation
            for (int r = t+4; r < en; r++) 
            { String rs = lexicals.get(r) + ""; 
              if (rs.equals("="))
              { Type restype = parseType(t+4,r-1,entities,types);

                System.out.println(">>> Helper attribute " + attname + " on context " + contextType); 
 
          // String atttype = lexicals.get(st + 7) + ""; 
                Expression exp = parse_ATLexpression(0,r+1,en-1,entities,types); 
          // Attribute att = new Attribute(attname,new Type(atttype,null),ModelElement.INTERNAL); 
          // att.setInitialExpression(exp); 
          // Entity ente = (Entity) ModelElement.lookupByName(ent,entities); 
          // att.setEntity(ente); 
          // mod.addAttribute(att);
                BehaviouralFeature bf = new BehaviouralFeature(attname,new Vector(),true,restype); 
                Expression resexp = new BasicExpression("result"); 
                resexp.setType(restype); 
                resexp.setUmlKind(Expression.VARIABLE); 
                exp.setBrackets(true); 
                Expression post = new BinaryExpression("=", resexp, exp); 
                Entity ente = (Entity) ModelElement.lookupByName(ent,entities); 
                bf.setOwner(ente); 
                Expression newpost = post; 

                if (ente != null) 
                { ente.addOperation(bf); }
                else 
                { Attribute selfatt = 
                    new Attribute("atlself",contextType,ModelElement.INTERNAL);  
                  bf.addFirstParameter(selfatt); 
                  BasicExpression var = 
                    new BasicExpression(selfatt); 
                  newpost = post.substituteEq("self", var); 
                } 

                bf.setPost(newpost);
           
                mod.addOperation(bf);  
                bf.setCached(true); 
                return null;
              }
            }
          }
        }  
      } 
      return null; 
    } 
    else if ("helper".equals(lexicals.get(st) + "") && 
        en - st >= 7 && 
        "def".equals(lexicals.get(st + 1) + "") && 
        ";".equals(lexicals.get(en) + "")) 
    { // helper def : name() : rt = expr ; 

      String attname = lexicals.get(st + 3) + ""; 
      if ("(".equals(lexicals.get(st + 4) + ""))
      { // helper operation of the ATL module

        System.out.println(">>> Helper module operation " + attname); 

        for (int pp = st+4; pp < en; pp++) 
        { String pps = lexicals.get(pp) + ""; 
          if (pps.equals("="))
          { for (int qq = pp-1; qq > st+4; qq--) 
            { String qqs = lexicals.get(qq) + ""; 
              if (qqs.equals(":"))
              { Vector params = parseAttributeDecs(st+4,qq-2,entities,types);
                Type atType = parseType(qq+1,pp-1,entities,types); 
                Expression defn = parse_ATLexpression(0,pp+1,en-1,entities,types); 
                BehaviouralFeature bf = new BehaviouralFeature(attname,params,true,atType); 
                bf.setElementType(atType.getElementType()); 
                Expression resexp = new BasicExpression("result"); 
                resexp.setType(atType); 
                resexp.setUmlKind(Expression.VARIABLE); 
                defn.setBrackets(true); 
                Expression post = new BinaryExpression("=", resexp, defn); 
        
                bf.setPost(post); // actually to return the defn value
                mod.addOperation(bf);  
                return null; 
              } 
            } 
          } 
        } 
      } 
      else 
      { // static attribute of the module 
        // helper def : name : rt = expr ;

        System.out.println(">>> Helper module attribute " + attname); 

        for (int rr = st+5; rr < en; rr++) 
        { String rrs = lexicals.get(rr) + ""; 
          if (rrs.equals("="))
          { Type hrestype = parseType(st+5,rr-1,entities,types); 
            Expression hexp = parse_ATLexpression(0,rr+1,en-1,entities,types); 
            Attribute att = new Attribute(attname,hrestype,ModelElement.INTERNAL); 
            att.setElementType(hrestype.getElementType()); 
            att.setInitialExpression(hexp); 
            mod.addAttribute(att);
          // att.setOwner(mod); 
            att.setStatic(true); 
            return null;
          }
        }  
      } 
      // return null; 
    } 
    else 
    { return null; }  

    System.out.println(">>> Parsing ATL rule " + rname + 
                       " " + lexicals.get(rulestart)); 

    if ("(".equals(lexicals.get(rulestart) + ""))
    { for (int d = rulestart; d < en; d++)
      { String sd = lexicals.get(d) + "";
        if (sd.equals("{"))
        { Vector params = 
            parseAttributeDecs(rulestart+1,d-2,
                               entities,types);
          for (int f = d+1; f < en; f++)
          { String sf = lexicals.get(f) + "";
            if ("using".equals(sf))
            { for (int g = f+1; g < en; g++) 
              { String sg = lexicals.get(g) + ""; 
                if ("to".equals(sg))
                { for (int h = g+1; h < en; h++) 
                  { String hs = lexicals.get(h) + ""; 
                    if ("do".equals(hs))
                    { Vector uvars = parseUsingClause(f, g-1, entities, types); 
                      OutPattern toClaus = parseToClause(g, h-1, entities, types);
                      Statement stat = parseATLStatement(h+2, en-2,entities,types);
                      MatchedRule cr = new MatchedRule(false,false);
                      cr.setOutPattern(toClaus);
                      cr.setUsing(uvars); 
                      cr.setActionBlock(stat);
                      cr.setName(rname);
                      cr.setParameters(params); 
                      cr.isCalled = true; 
                      return cr;
                    } 
                  } 
                } 
                else if ("do".equals(sg))
                { Vector uvars = parseUsingClause(f, g-1, entities, types); 
                  Statement stat = parseATLStatement(g+2, en-2, entities,types);
                  MatchedRule cr = new MatchedRule(false,false);
                  cr.setUsing(uvars); 
                  cr.setActionBlock(stat);
                  cr.setName(rname);
                  cr.setParameters(params); 
                  cr.isCalled = true; 
                  return cr; 
                } 

              } 
            }
            else if ("do".equals(sf))
            { OutPattern toClaus = parseToClause(d+1, f-1, entities, types);
              Statement stat = parseATLStatement(f+2, en-2,entities,types);
              MatchedRule cr = new MatchedRule(false,false);
              cr.setOutPattern(toClaus);
              cr.setActionBlock(stat);
              cr.setName(rname);
              cr.setParameters(params); 
              cr.isCalled = true; 
              return cr;
            }
          }
        }
      }
    }

    boolean isExtension = false; 
    String extendedRule = null; 

    start = rulestart; 
    for (int i = rulestart; i < en; i++) 
    { String lex = lexicals.get(i) + ""; 

      if ("extends".equals(lex)) 
      { isExtension = true;
        extendedRule = lexicals.get(i+1) + ""; 
      } 

      if ("{".equals(lex))
      { start = i; 
        break; 
      } 
    } // skip over extends.

    for (int i = start+1; i < en; i++) 
    { if ("to".equals(lexicals.get(i) + "")) 
      { InPattern fromClause = parseFromClause(start+1, i - 1, entities, types); 
        for (int j = i; j < en; j++) 
        { if ("do".equals(lexicals.get(j) + "") && "{".equals(lexicals.get(j+1) + "") && 
              "}".equals(lexicals.get(en-1) + "") )
          { OutPattern toClause = parseToClause(i, j - 1, entities, types); 
            Statement stat = parseATLStatement(j+2, en-2,entities,types);
            MatchedRule mr = new MatchedRule(lazy, unique); 
            mr.setInPattern(fromClause); 
            mr.setOutPattern(toClause);
            System.out.println(">> Rule action block: " + stat); 
            mr.setActionBlock(stat);  
            mr.setName(rname);
            if (isExtension)
            { MatchedRule suprule = 
                mod.getRuleByName(extendedRule); 
              mr.setSuperRule(suprule); 
            }  
            return mr;
          } 
        }  
        OutPattern toClause = parseToClause(i, en - 1, entities, types); 
        MatchedRule mr = new MatchedRule(lazy, unique); 
        mr.setInPattern(fromClause); 
        mr.setOutPattern(toClause); 
        mr.setName(rname); 
        if (isExtension)
        { MatchedRule suprule = 
                mod.getRuleByName(extendedRule); 
          mr.setSuperRule(suprule); 
        }  
        return mr; 
      }  
    } 

    return null; 
  } 

  public InPattern parseFromClause(int st, int en, Vector entities, Vector types) 
  { if ("from".equals(lexicals.get(st) + "")) { }
    else { return null; } 

    int dec_end = en; 
    for (int i = st+1; i < en; i++) 
    { if ("(".equals(lexicals.get(i) + ""))
      { dec_end = i - 1; 
        // System.out.println(dec_end); 
        break; 
      } 
    }

    System.out.println(">>> Parsing from clause " + showLexicals(st,en)); 
      
    InPattern res = new InPattern(); 
    InPatternElement ipe = null; 
   
    for (int j = st+1; j+2 <= dec_end; j = j+4)
    { String attname = lexicals.get(j) + ""; 
      String tname = lexicals.get(j+2) + ""; 

      // System.out.println("Input variable " + attname); 

      Entity ent =
          (Entity) ModelElement.lookupByName(tname,entities);
      Type t; 
      if (ent != null)
      { t = new Type(ent); } 
      else 
      { t = new Type(tname,null); }
      Attribute att = new Attribute(attname,t,ModelElement.INTERNAL); 
      ipe = new InPatternElement(att,null); 
      res.addElement(ipe); 
    }         
    
    if (dec_end < en && ipe != null) 
    { Expression cond = parse_ATLexpression(1, dec_end + 2, en - 1,entities,types); 
      ipe.setCondition(cond); 
    } 
      
    return res; 
  } 

  public OutPattern parseToClause(int st, int en, Vector entities, Vector types) 
  { if ("to".equals(lexicals.get(st) + "")) { }
    else { return null; } 

    int dec_end = en; 
    for (int i = st + 1; i < en; i++) 
    { if ("do".equals(lexicals.get(i) + ""))
      { dec_end = i - 1; 
        // System.out.println(dec_end); 
        break; 
      } 
    }

    System.out.println(">>> Parsing to clause " + showLexicals(st,en)); 
      
    OutPattern res = new OutPattern(); 
    Vector patts = parse_to_clause(st + 1, dec_end, entities, types); 
    res.setElements(patts); 
    return res; 
  } 


  public Vector parse_to_clause(int st, int en, Vector entities, Vector types)
  { Vector res = new Vector(); 
    if (st >= en) { return res; } 

    if (st + 5 <= en && 
        ":".equals(lexicals.get(st + 1) + "") && 
        "mapsTo".equals(lexicals.get(st + 3) + "") && 
        "(".equals(lexicals.get(st + 5) + "") )
    { String att = lexicals.get(st) + ""; 
      String typ = lexicals.get(st + 2) + ""; 
      Entity ent =
          (Entity) ModelElement.lookupByName(typ,entities);
      Type tt; 
      if (ent != null)
      { tt = new Type(ent); } 
      else 
      { tt = new Type(typ,null); }

      String mappedFromVar = lexicals.get(st + 4) + ""; 
      
      for (int i = st + 6; i <= en; i++) 
      { if (")".equals(lexicals.get(i) + "") && 
            (i == en ||
             ",".equals(lexicals.get(i + 1) + "") &&
             ":".equals(lexicals.get(i + 3) + "") 
            ) )
        { Vector outpat = parse_out_pattern(st + 6, i - 1, entities, types);
          Attribute var = new Attribute(att,tt,ModelElement.INTERNAL);  
          OutPatternElement ope = new OutPatternElement(var); 
          ope.setBindings(outpat);
          ope.setMappedFrom(mappedFromVar); 

          if (i < en) 
          { res = parse_to_clause(i + 2, en, entities, types); }   
          res.add(0,ope); 
          return res; 
        } 
      } 
    } 
    else if (":".equals(lexicals.get(st + 1) + "") && 
        "(".equals(lexicals.get(st + 3) + "") )
    { String att = lexicals.get(st) + ""; 
      String typ = lexicals.get(st + 2) + ""; 
      Entity ent =
          (Entity) ModelElement.lookupByName(typ,entities);
      Type tt; 
      if (ent != null)
      { tt = new Type(ent); } 
      else 
      { tt = new Type(typ,null); }
      
      for (int i = st + 4; i <= en; i++) 
      { if (")".equals(lexicals.get(i) + "") && 
            (i == en ||
             ",".equals(lexicals.get(i + 1) + "") &&
             ":".equals(lexicals.get(i + 3) + "") 
            ) )
        { Vector outpat = parse_out_pattern(st + 4, i - 1, entities, types);
          Attribute var = new Attribute(att,tt,ModelElement.INTERNAL);  
          OutPatternElement ope = new OutPatternElement(var); 
          ope.setBindings(outpat);
          if (i < en) 
          { res = parse_to_clause(i + 2, en, entities, types); }   
          res.add(0,ope); 
          return res; 
        } 
      } 
    } 
    return res; 
  } 

  public Vector parse_out_pattern(int st, int en, Vector entities, Vector types) 
  // The interior of the ( outpattern )
  { if (st >= en) { return new Vector(); } 
    Vector res = new Vector(); 

    if ("<".equals(lexicals.get(st + 1) + "") && "-".equals(lexicals.get(st + 2) + ""))
    { String att = lexicals.get(st) + ""; 
      for (int i = st + 3; i <= en; i++) 
      { if (",".equals(lexicals.get(i) + "") && "<".equals(lexicals.get(i + 2) + "") && 
            "-".equals(lexicals.get(i+3) + ""))
        { Expression cond = parse_ATLexpression(1, st + 3, i - 1, entities, types); 
          if (cond != null) 
          { Binding b = new Binding(att,cond); 
            res.add(b); 
            res.addAll(parse_out_pattern(i + 1, en, entities, types)); 
            return res; 
          } 
        } 
      }

      Expression cond1 = parse_ATLexpression(1, st + 3, en, entities, types); 
                  // The last binding expression

      if (cond1 != null) 
      { Binding b1 = new Binding(att,cond1); 
        res.add(b1); 
      } 
    } 
    
    return res; 
  } 

// using { var : typ = val; ... }
private Vector parseUsingClause(int st, int en, Vector entities, Vector types)
{ if ("using".equals(lexicals.get(st) + "")) {}
  else { return null; }

  int st0 = st;
  Vector res = new Vector();
  for (int k = st + 2; k < en; k++)
  { String lx = lexicals.get(k) + "";
    if (lx.equals(":")) 
    { String var = lexicals.get(k-1) + "";
      for (int m = k+1; m < en; m++)
      { String mx = lexicals.get(m) + "";
        if (mx.equals("="))
        { Type typ = parseType(k+1,m-1,entities,types);
         if (typ == null)
         { System.err.println("Invalid type: " + showLexicals(k+1,m-1));
           continue;
         }
         Attribute uvar = new Attribute(var,typ,ModelElement.INTERNAL);
         for (int n = m+1; n < en; n++)
         { String nx = lexicals.get(n) + "";
            if (nx.equals(";"))
            { Expression vdef = parse_ATLexpression(0,m+1,n-1,entities,types);
               if (vdef != null)
               { uvar.setInitialExpression(vdef);
                 res.add(uvar);
                 k = n+1; 
                }
              }
            }
          }
        }
      }
    }
    return res;
 }


  public EtlModule parseEtlModule()
  { return parseEtl(new Vector(), new Vector()); } 

  public EtlModule parseEtl(Vector entities, Vector types)
  { if ("module".equals(lexicals.get(0) + "") && 
        ";".equals(lexicals.get(2) + ""))
    { EtlModule mod = new EtlModule(lexicals.get(1) + ""); 
      for (int i = 3; i < lexicals.size();  i++) 
      { String lx = lexicals.get(i) + ""; 
        if ("pre".equals(lx) || "rule".equals(lx) || "@lazy".equals(lx) || "operation".equals(lx)
            || "post".equals(lx))
        { parse_Etl_module(i,lexicals.size()-1,entities,types,mod); 
          return mod; 
        }
      } 
    } 
    return null; 
  } 

  public void parse_Etl_module(int st, int en, Vector entities, Vector types, EtlModule mod)
  { if (st >= en)
    { return; } 
    for (int i = st + 2; i <= en;  i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("pre".equals(lx) || "rule".equals(lx) || "operation".equals(lx) ||
          "post".equals(lx) || "@lazy".equals(lx))
      { Object r = parseEtlrule(st,i-1,entities,types);
        if (r != null) 
        { parse_Etl_module(i,en,entities,types,mod);  
          mod.add(r); 
          return; 
        }   
      }
    } 
    Object r1 = parseEtlrule(st,en,entities,types);
    if (r1 != null) { mod.add(r1); }      
    return; 
  } 

  public Object parseEtlrule(int st, int en, Vector entities, Vector types)
  { String rname = ""; 
    int start = st; 

    if ("pre".equals(lexicals.get(st) + "") &&
        "{".equals(lexicals.get(st + 2) + "") &&  
        "}".equals(lexicals.get(en) + ""))
    { start = st + 3; 
      rname = lexicals.get(st + 1) + ""; 
      Statement stat = parseATLStatement(start,en-1,entities,types); 
      if (stat != null) 
      { stat.typeCheck(types,entities,new Vector(),new Vector()); 
        NamedBlock res = new NamedBlock(rname, stat); 
        res.setPre(true); 
        return res; 
      } 
    } 
    else if ("pre".equals(lexicals.get(st) + "") &&
        "{".equals(lexicals.get(st + 1) + "") &&  
        "}".equals(lexicals.get(en) + ""))
    { start = st + 2; 
      rname = "pre"; 
      Statement stat = parseATLStatement(start,en-1,entities,types); 
      if (stat != null) 
      { stat.typeCheck(types,entities,new Vector(),new Vector()); 
        NamedBlock res = new NamedBlock(rname, stat); 
        res.setPre(true); 
        return res; 
      } 
    } 
    else if ("rule".equals(lexicals.get(st) + "") &&
             "transform".equals(lexicals.get(st+2) + "") && 
             "to".equals(lexicals.get(st+6) + "") && 
             "}".equals(lexicals.get(en) + "") && en - st >= 6)
    { start = st + 6; 
      rname = lexicals.get(st + 1) + "";
      String invar = lexicals.get(st + 3) + "";
      String intype = lexicals.get(st + 5) + "";
      Entity inent = (Entity) ModelElement.lookupByName(intype,entities); 
      if (inent == null) 
      { System.err.println("*** Cannot find entity type " + intype); 
        inent = new Entity(intype); 
        entities.add(inent); 
      } 
      Attribute inatt = new Attribute(invar,new Type(inent),ModelElement.INTERNAL); 
      inatt.setElementType(new Type(inent)); 
      // if (whenexp != null) 
      // { return new FlockRetyping(rname, whenexp, newtype); } 
      TransformationRule r = new TransformationRule(rname,false,false); 
      r.setSource(inatt); 

      Vector env = new Vector(); 
      env.add(inatt); 
    
      for (int i = start; i < en; i++) 
      { if ("{".equals(lexicals.get(i) + ""))
        { Vector toclause = parseEtlToClause(start, i-1, entities, types); 
          r.addTargets(toclause); 
          Expression g = new BasicExpression(true); 
          Statement body; 
          if ("guard".equals(lexicals.get(i+1) + "")) 
          { for (int j = i+3; j < en - 1; j++) 
            { g = parse_expression(0,i+3,j,entities,types); 
              body = parseATLStatement(j+1,en-1,entities,types); 
              if (g != null && body != null) 
              { g.typeCheck(types,entities,new Vector(),env); 

                r.setGuard(g);

                env.addAll(toclause); 
 
                body.typeCheck(types,entities,new Vector(),env); 
                r.setBody(body); 
                return r; 
              } 
            } 
          } 
          body = parseATLStatement(i+1,en-1,entities,types);  
          r.setGuard(g); 
                
          env.addAll(toclause); 
 
          if (body != null) 
          { body.typeCheck(types,entities,new Vector(),env); } 
 
          r.setBody(body); 
          return r; 
        } 
      }
    } 
    else if ("post".equals(lexicals.get(st) + "") &&
        "{".equals(lexicals.get(st + 2) + "") &&  
        "}".equals(lexicals.get(en) + ""))
    { start = st + 3; 
      rname = lexicals.get(st + 1) + ""; 
      Statement stat = parseATLStatement(start,en-1,entities,types); 
      if (stat != null) 
      { NamedBlock res = new NamedBlock(rname, stat); 
        res.setPre(false); 
        return res; 
      } 
    }             
    else if ("post".equals(lexicals.get(st) + "") &&
        "{".equals(lexicals.get(st + 1) + "") &&  
        "}".equals(lexicals.get(en) + ""))
    { start = st + 2; 
      rname = "post"; 
      Statement stat = parseATLStatement(start,en-1,entities,types); 
      if (stat != null) 
      { NamedBlock res = new NamedBlock(rname, stat); 
        res.setPre(false); 
        return res; 
      } 
    }             
    else if ("@lazy".equals(lexicals.get(st) + ""))
    { TransformationRule rr = (TransformationRule) parseEtlrule(st+1,en,entities,types); 
      rr.setLazy(true); 
      return rr; 
    } // same with principal, abstract etc. 
    else if ("operation".equals(lexicals.get(st) + "")) 
    { BehaviouralFeature bf = parseEtlOperation(st+1,en,entities,types); 
      return bf; 
    } 
    return null; 
  } 

  public BehaviouralFeature parseEtlOperation(int st, int en, Vector entities, Vector types) 
  { // operation ContextEntity name(pardecs) { code } 
    String context = lexicals.get(st) + ""; 
    String nme = lexicals.get(st+1) + ""; 
    Entity ent =
          (Entity) ModelElement.lookupByName(context,entities);

    for (int i = st+2; i < en; i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("{".equals(lx))
      { Vector pars = parseAttributeDecs(st+3,i-2,entities,types);
        Statement code = parseATLStatement(i+1, en-1, entities, types);
        // deduce the return type from code. 
 
        BehaviouralFeature bf = new BehaviouralFeature(nme, pars, false, null); 
        bf.setEntity(ent); 
        bf.setActivity(code); 
        if (ent != null) { ent.addOperation(bf); } 
        return bf; 
      } 
    } 
    return null; 
  } 


  public Vector parseEtlToClause(int st, int en, Vector entities, Vector types) 
  { if ("to".equals(lexicals.get(st) + "")) { }
    else { return null; } 

    Vector res = new Vector();   
       
    for (int j = st+1; j+2 <= en; j = j+4)
    { String attname = lexicals.get(j) + ""; 
      String tname = lexicals.get(j+2) + ""; 

      // System.out.println("Output variable " + attname); 

      Entity ent =
          (Entity) ModelElement.lookupByName(tname,entities);
      Type t; 
      if (ent != null)
      { t = new Type(ent); } 
      else 
      { System.err.println("*** Cannot find entity type " + tname); 
        ent = new Entity(tname); 
        entities.add(ent); 
        t = new Type(ent); 
      }
      Attribute att = new Attribute(attname,t,ModelElement.INTERNAL); 
      att.setElementType(t); 
      res.add(att); 
    }         
      
    return res; 
  } // but the to-variables can be of general types, eg:  t : Sequence(String)




  public FlockModule parseFlockModule()
  { return parseFlock(new Vector(), new Vector()); } 

  public FlockModule parseFlock(Vector entities, Vector types)
  { if ("module".equals(lexicals.get(0) + "") && 
        ";".equals(lexicals.get(2) + ""))
    { FlockModule mod = new FlockModule(lexicals.get(1) + ""); 
      for (int i = 3; i < lexicals.size();  i++) 
      { if ("delete".equals(lexicals.get(i) + "") || "migrate".equals(lexicals.get(i) + "") || 
            "retype".equals(lexicals.get(i) + "") || "operation".equals(lexicals.get(i) + ""))
        { parse_Flock_module(i,lexicals.size()-1,entities,types,mod); 
          return mod; 
        }
      } 
    } 
    return null; 
  } 

  public void parse_Flock_module(int st, int en, Vector entities, Vector types, FlockModule mod)
  { if (st >= en)
    { return; } 
    for (int i = st + 2; i <= en;  i++) 
    { if ("delete".equals(lexicals.get(i) + "") || "retype".equals(lexicals.get(i) + "") ||
          "migrate".equals(lexicals.get(i) + "") || "operation".equals(lexicals.get(i) + ""))
      { Object r = parseFlockrule(st,i-1,entities,types);
        if (r != null) 
        { parse_Flock_module(i,en,entities,types,mod);  
          mod.add(r); 
          return; 
        }   
      }
    } 
    Object r1 = parseFlockrule(st,en,entities,types);
    if (r1 != null) { mod.add(r1); }      
    return; 
  } 

  public Object parseFlockrule(int st, int en, Vector entities, Vector types)
  { String rname = ""; 
    int start = st; 

    if ("delete".equals(lexicals.get(st) + "") &&
        "when".equals(lexicals.get(st + 2) + "") &&  
        ":".equals(lexicals.get(st + 3) + ""))
    { start = st + 4; 
      rname = lexicals.get(st + 1) + ""; 
      Expression whenexp = parse_expression(0,start,en,entities,types); 
      if (whenexp != null) 
      { return new FlockDeletion(rname, whenexp); } 
    } 
    else if ("retype".equals(lexicals.get(st) + "") &&
             "to".equals(lexicals.get(st+2) + "") && 
             "when".equals(lexicals.get(st+4) + "") && 
             ":".equals(lexicals.get(st+5) + "") && en - st >= 6)
    { start = st + 6; 
      rname = lexicals.get(st + 1) + "";
      String newtype = lexicals.get(st + 3) + "";
      Expression whenexp = parse_expression(0,start,en,entities,types); 
      if (whenexp != null) 
      { return new FlockRetyping(rname, whenexp, newtype); } 
    } 
    else if ("migrate".equals(lexicals.get(st) + "") &&
             "when".equals(lexicals.get(st + 2) + "") &&  
             ":".equals(lexicals.get(st + 3) + "") && 
             "}".equals(lexicals.get(en) + ""))
    { start = st + 4; 
      rname = lexicals.get(st + 1) + ""; 
      for (int j = start; j < en; j++) 
      { Vector iglist = new Vector(); 
        Expression whenexp = new BasicExpression("true"); 
        if ("ignoring".equals(lexicals.get(j) + ""))
        { whenexp = parse_expression(0,start,j-1,entities,types); 
          if (whenexp == null) { continue; } 
          for (int k = j+1; k < en; k++) 
          { if ("{".equals(lexicals.get(k) + "")) 
            { j = k; k = en; } 
            else if (",".equals(lexicals.get(k) + "")) { } 
            else 
            { iglist.add(lexicals.get(k) + ""); }
          }  
        } 
        if ("{".equals(lexicals.get(j) + "")) 
        { if (iglist.size() == 0) 
          { whenexp = parse_expression(0,start,j-1,entities,types); 
            if (whenexp == null) { continue; }
          }  
          Statement stat = parseStatement(j+1,en-1, entities, types); 
          MigrateRule res = new MigrateRule(rname, whenexp);
          res.setBody(stat);
          res.setIgnored(iglist);  
          return res;  
        } 
      } 
    } 
    else if ("operation".equals(lexicals.get(st) + "") && 
             "}".equals(lexicals.get(en) + ""))
    { start = st + 1;
      String etype = "IN$" + lexicals.get(start);  // Owner entity 
      rname = lexicals.get(st + 2) + "";  // operation name
      // skip "(", ")", ":"
      String returntype = lexicals.get(st + 6) + ""; // return type 
      if ("{".equals(lexicals.get(st + 7) + ""))
      { Statement stat = parseStatement(st + 8, en - 1, entities, types); 
        Entity owner = (Entity) ModelElement.lookupByName(etype, entities); 
        BehaviouralFeature op = new BehaviouralFeature(rname, new Vector(), true, 
                                                       new Type(returntype,null)); 
        op.setEntity(owner);
        if (owner != null) { owner.addOperation(op); }   
        op.setActivity(stat); 
        return op; 
      }
    }           


    return null; 
  } 


  public MigrateRule parseMigraterule(int st, int en, Vector entities, Vector types)
  { String rname = ""; 
    int start = st; 

    if ("migrate".equals(lexicals.get(st) + "") &&
        "when".equals(lexicals.get(st + 2) + "") &&  
        ":".equals(lexicals.get(st + 3) + ""))
       // "}".equals(lexicals.get(en) + ""))
    { start = st + 4; 
      rname = lexicals.get(st + 1) + ""; 
      for (int j = start; j < en; j++) 
      { if ("{".equals(lexicals.get(j) + "")) 
        { Expression whenexp = parse_expression(0,start,j-1,entities,types); 
          if (whenexp == null) { continue; } 
          Statement stat = parseStatement(j+1,en-1,entities,types); 
          MigrateRule res = new MigrateRule(rname, whenexp);
          res.setBody(stat); 
          return res;  
        } 
      } 
    } 
  /*     else if ("retype".equals(lexicals.get(st) + "") &&
             "to".equals(lexicals.get(st+2) + "") && 
             "when".equals(lexicals.get(st+4) + "") && 
             ":".equals(lexicals.get(st+5) + "") && en - st >= 6)
    { start = st + 6; 
      rname = lexicals.get(st + 1) + "";
      String newtype = lexicals.get(st + 3) + "";
      Expression whenexp = parse_expression(0,start,en,entities,types); 
      if (whenexp != null) 
      { return new FlockRetyping(rname, whenexp, newtype); } 
    } 
    else 
    { return null; }  */ 

    return null; 
  } 

  public RelationalTransformation parse_QVTR(int st, int en, Vector entities, Vector types)
  { String sts = lexicals.get(st) + "";
    String std = lexicals.get(st+2) + "";
    String ens = lexicals.get(en) + "";
    int start = st+3; 
    String extending = null; 

    if ("transformation".equals(sts)) 
    { for (int i = st + 2; i < en; i++) 
      { std = lexicals.get(i) + ""; 
        if ("{".equals(std)) 
        { start = i+1; 
          break; 
        } 
        else if ("extends".equals(std))
        { extending = lexicals.get(i+1) + ""; } 
      } 
    } 
    else 
    { System.err.println("!! QVT-R transformation must start with transformation name(parameters)"); 
      return null; 
    }

    String nme = lexicals.get(st + 1) + "";
    RelationalTransformation res = new RelationalTransformation(nme);
    res.setExtending(extending); 

    // int start = st+3;

    for (int w = st + 3; w + 5 < en; w++) 
    { String wlex = lexicals.get(w) + ""; 
      if ("key".equals(wlex))
      { // key eId { attname } ; 
        String eId = lexicals.get(w+1) + ""; 
        Entity ent = (Entity) ModelElement.lookupByName(eId,entities);
        if (ent == null) 
        { System.err.println(">!! Key declaration error: No entity exists with name: " + eId); 
          st = w+4;
          start = w+6;  
          break; 
        }  
        String attname = lexicals.get(w+3) + "";
        Attribute att = ent.getAttribute(attname); 
        if (att == null) 
        { System.err.println(">!! Key declaration error: No entity exists with name: " + eId); 
          st = w+4;
          start = w+6;  
          break; 
        }  
        att.setIdentity(true); 
        System.out.println(">>> Key constraint for " + eId + "::" + attname); 
        st = w + 4; 
        start = w+6; 
      } 
      else if ("relation".equals(wlex) || "top".equals(wlex) || "function".equals(wlex))
      { break; } // key declarations come before any relation or query
    } 

    for (int i = st + 5; i < en; i++)
    { String lex = lexicals.get(i) + "";
      if ("relation".equals(lex) || "top".equals(lex) || 
          "function".equals(lex) || "abstract".equals(lex))
      { Object r = parse_QVT_rule(start,i-1,entities,types,res);
        if (r != null && (r instanceof Relation)) 
        { res.addRule((Relation) r); 
          start = i;
        } 
        else if (r != null && (r instanceof BehaviouralFeature))
        { res.addFunction((BehaviouralFeature) r); 
          start = i; 
        } 
        else 
        { System.err.println(">>> Unrecognised QVT element between " + start + " and " + (i-1)); } 
      }
    }

    Object r = parse_QVT_rule(start,en-1,entities,types,res);
    if (r != null && (r instanceof Relation)) 
    { res.addRule((Relation) r); }
    else if (r != null && (r instanceof BehaviouralFeature))
    { res.addFunction((BehaviouralFeature) r); } 

    return res;    
  }

  public Object parse_QVT_rule(int st, int en, Vector entities, Vector types, RelationalTransformation trans)
  { String sts = lexicals.get(st) + "";
    String ens = lexicals.get(en) + "";
    boolean isAbstract = false; 
    boolean isTop = true;
    Expression wexp = null; 
    Expression wnexp = null; 
    Vector variables = new Vector(); 
    String overrides = null; 

    String nme = lexicals.get(st+1) + "";
    int start = st + 3; // after the {
    if ("abstract".equals(sts) && "top".equals(lexicals.get(st+1) + "") && 
        st + 3 < en && "}".equals(ens)) 
    { isAbstract = true;
      isTop = true;  
      // abstract top relation nme { 
      nme = lexicals.get(st+3) + ""; 
      start = st + 5; 
    }
    else if ("abstract".equals(sts) && "relation".equals(lexicals.get(st+1) + "") && 
             st + 3 < en && "}".equals(ens)) 
    { isAbstract = true; 
      isTop = false; 
      // abstract relation nme { 
      nme = lexicals.get(st+2) + ""; 
      start = st + 4; 
    }
    else if ("relation".equals(sts) && st + 3 < en && "}".equals(ens)) 
    { isTop = false; }
    else if ("top".equals(sts) && st + 4 < en && "}".equals(ens))
    { nme = lexicals.get(st + 2) + "";
      // top relation nme { 
      start = st + 4;
    }
    else if ("query".equals(sts))
    { return parse_QVT_function(st,en,entities,types); } 
    else 
    { return null; }

    for (int x = st + 1; x < en; x++) 
    { String xlex = lexicals.get(x) + ""; 
      if ("{".equals(xlex))
      { start = x+1; 
        String oclause = lexicals.get(x-2) + ""; 
        if ("overrides".equals(oclause))
        { overrides = lexicals.get(x-1) + ""; } 
        break; 
      } 
    }       

    // look for overrides clause. 

    int domend = en - 1; 
    int domstart = start; 

    for (int i = start; i < en; i++) 
    { String istr = lexicals.get(i) + ""; 
      if ("checkonly".equals(istr) ||
          "collection".equals(istr) || "enforce".equals(istr) || "primitive".equals(istr))
      { domstart = i; 
        break; 
      } 
    } 

    if (domstart > start) // there are some variables
    { variables = parseAttributeDecsInit(start,domstart-2,entities,types); }  

    for (int i = domstart; i < en; i++) 
    { String lex = lexicals.get(i) + ""; 
      if ("when".equals(lex))
      { domend = i-1; 
        for (int j = i+1; j < en; j++) 
        { String lx1 = lexicals.get(j) + ""; 
          if ("where".equals(lx1))
          { wnexp = parse_ATLexpression(0, i+2, j-2, entities, types); 
            wexp = parse_ATLexpression(0, j+2, en-2, entities, types);         
          } 
        }
        if (wnexp == null) // where not found 
        { wnexp = parse_ATLexpression(0, i+2, en-2, entities, types); }               
      } 
    } 

    if (wnexp == null && wexp == null)  // no when clause 
    { for (int i = start; i < en; i++) 
      { String lex = lexicals.get(i) + ""; 
        if ("where".equals(lex))
        { wexp = parse_ATLexpression(0, i+2, en-2, entities, types); 
          if (wexp != null) 
          { domend = i-1; } 
        } 
      } 
    }    

    Vector domains = parse_domains(domstart, domend, types, entities, trans);
    

    if (domains != null)
    { Relation res = new Relation(nme, trans);
      res.setIsTop(isTop);
      res.setIsAbstract(isAbstract);
      res.setOverrides(overrides);  
      res.setDomain(domains);
      if (wnexp != null) 
      { res.addWhen(wnexp); } 
      if (wexp != null) 
      { res.addWhere(wexp); } 
      res.addVariables(variables); 
      return res;
    }
    return null;
  }

  public Object parse_QVT_function(int st, int en, Vector entities, Vector types)
  { String sts = lexicals.get(st) + "";
    String ens = lexicals.get(en) + "";
    String nme = lexicals.get(st+1) + ""; 
    Type resType = new Type("boolean", null); 
    Vector pars = new Vector(); 
    BehaviouralFeature bf = new BehaviouralFeature(nme); 
    BasicExpression resvar = new BasicExpression("result"); 

    for (int i = st+2; i < en; i++) 
    { String istr = lexicals.get(i) + ""; 
      if ("{".equals(istr)) // start of function body
      { for (int j = i-1; j > st+2; j--) 
        { String jstr = lexicals.get(j) + ""; 
          if (":".equals(jstr)) // typing of the function
          { resType = parseType(j+1,i-1,entities,types); 
            if (resType == null) 
            { System.err.println(">>> Invalid result type for function " + nme); } 

            resvar.setType(resType); 
            bf.setResultType(resType); 

            parseOpDecs(st+2, j-1, entities, types, bf); 
            Expression code = parse_ATLexpression(0,i+1,en-1,entities,types); 
            if (code == null) 
            { System.err.println(">>> Invalid expression for function " + nme); } 

            BinaryExpression post = new BinaryExpression("=",resvar,code); 
            bf.setPost(post); 
            return bf; 
          } 
        } 
      } 
    } 
    return null; 
  } 
             

  // in general will need to have types such as Set(E) 

  private Vector parse_domains(int st, int en, Vector types, Vector entities, RelationalTransformation trans)
  { // enforce domain src x : T 
    String sts = lexicals.get(st) + "";
    String ens = lexicals.get(en) + "";
    boolean checkonly = false;
    boolean enforce = false;
    boolean primitive = false;
    boolean iscollection = false; 
    Vector res = new Vector();

    String nme = lexicals.get(st+1) + "";
    int start = st + 2; // start of template or primitive domain
    if ("primitive".equals(sts) && ";".equals(ens)) 
    { nme = lexicals.get(st+2) + "";
      primitive = true;
      start = st + 2; 
    }
    else if ("checkonly".equals(sts) && st + 4 < en && ";".equals(ens))
    { checkonly = true;
      nme = lexicals.get(st + 2) + "";
      start = st + 3;
      if (st + 5 < en && "Set".equals(lexicals.get(st + 4) + "") && 
          "(".equals(lexicals.get(st+5) + ""))
      { iscollection = true; } 
      else if (st + 5 < en && "Sequence".equals(lexicals.get(st + 4) + "") && 
          "(".equals(lexicals.get(st+5) + ""))
      { iscollection = true; } 
    }
    else if ("enforce".equals(sts) && st + 4 < en && ";".equals(ens))
    { enforce = true;
      nme = lexicals.get(st + 2) + "";
      start = st + 3;
    }
    else if ("collection".equals(sts) && st + 4 < en && ";".equals(ens))
    { iscollection = true;
      // checkonly = true; 
      nme = lexicals.get(st + 2) + "";
      start = st + 3;
    }

    TypedModel tm = new TypedModel(nme,trans);

    for (int i = st+2; i <= en; i++)
    { String ch = lexicals.get(i) + "";
      if (";".equals(ch))  // end of a domain
      { if (primitive) 
        { Type tt = parseType(st+4,i-1,entities,types); 
          if (tt == null) 
          { System.err.println(">>> Invalid type at " + lexicals.get(start)); } 
          Attribute att = new Attribute(nme,tt,ModelElement.INTERNAL); 
          att.addStereotype("source"); 
          PrimitiveDomain pd = new PrimitiveDomain(nme, null, tm, null); 
          pd.rootVariable = att; 
          res.add(pd); 
        } 
        else if (iscollection) 
        { TemplateExp cexp = parse_collection_exp(0, start, i-1, entities,types);
          if (cexp == null) 
          { System.err.println(">>> Syntax error in domain " + nme); 
            continue; 
          } // should not happen.           

          DomainPattern dp = new DomainPattern(null, cexp);
          RelationDomain rd = new RelationDomain(nme, null, tm, null, dp);
          dp.relationDomain = rd;
          rd.setCheckable(true);
          rd.setEnforceable(false);
          rd.rootVariable = cexp.bindsTo; 
          // if (checkonly)
          { rd.rootVariable.addStereotype("source"); 
            if (cexp != null) 
            { cexp.setSource(); }  
          }  
          res.add(rd);
        } 
        else 
        { TemplateExp texp = parse_template_exp(0, start, i-1, entities,types);
          if (texp == null) 
          { System.err.println(">>> Syntax error in domain " + nme); 
            continue; 
          } // should not happen.           

          DomainPattern dp = new DomainPattern(null, texp);
          RelationDomain rd = new RelationDomain(nme, null, tm, null, dp);
          dp.relationDomain = rd;
          rd.setCheckable(checkonly);
          rd.setEnforceable(enforce);
          rd.rootVariable = texp.bindsTo; 
          if (checkonly)
          { rd.rootVariable.addStereotype("source"); 
            if (texp != null) 
            { texp.setSource(); }  
          }  
          res.add(rd);
        } 

        if (i >= en) { return res; }
        sts = lexicals.get(i+1) + "";
        nme = lexicals.get(i+2) + "";
        start = i + 3; // start of template

        System.out.println(">> Next domain: " + sts + " " + nme + " " + 
                           lexicals.get(start)); 

        if ("checkonly".equals(sts))
        { checkonly = true;
          enforce = false; 
          primitive = false;
          iscollection = false;  
          nme = lexicals.get(i + 3) + "";
          start = i + 4;
        }
        else if ("collection".equals(sts))
        { checkonly = true;
          enforce = false; 
          primitive = false;
          iscollection = true;  
          nme = lexicals.get(i + 3) + "";
          start = i + 4;
        }
        else if ("enforce".equals(sts))
        { enforce = true;
          checkonly = false; 
          primitive = false; 
          iscollection = false; 
          nme = lexicals.get(i + 3) + "";
          start = i + 4;
        }
        else if ("primitive".equals(sts))
        { enforce = false;
          checkonly = false; 
          primitive = true; 
          iscollection = false; 
          nme = lexicals.get(i + 3) + "";
          start = i + 5;
        }
        tm = new TypedModel(nme,trans);
      } 
    }

    return res;
  }

  public TemplateExp parse_template_exp(int bc, int st, int en, Vector entities, Vector types)
  { // bindsTo : referredClass { f1 = template1, ..., fn = templaten } { optionalExpression }

    if (st > en) { return null; }

    String bind = lexicals.get(st) + "";

    System.out.println(">>> Parsing domain for " + bind); 

    if (st + 4 <= en && (":".equals(lexicals.get(st + 1) + "") ||
                         "<:=".equals(lexicals.get(st + 1) + "")
                        )
       )
    { String ename = lexicals.get(st + 2) + "";
      Entity e = (Entity) ModelElement.lookupByName(ename,entities);

      if ("{".equals(lexicals.get(st+3) + "") && "}".equals(lexicals.get(st+4) + ""))
      { ObjectTemplateExp res = new ObjectTemplateExp(null,null,e);
        if ("<:=".equals(lexicals.get(st + 1) + ""))
        { res.setLeastChange(true); 
          // System.out.println(">>> Least-change template: " + res); 
        } 

        res.setpart(new Vector());
        Attribute batt = new Attribute(bind, new Type(e), ModelElement.INTERNAL);
        res.bindsTo = batt;
        
        if (st + 4 < en && "{".equals(lexicals.get(st+5) + "") 
            && "}".equals(lexicals.get(en) + ""))
        { Expression wexp = parse_ATLexpression(bc,st+6,en-1, entities,types); 
          if (wexp != null) 
          { res.setConstraint(wexp); } 
        } 
        return res;
      }

      int expstart = en; 

      for (int j = st + 3; j < en; j++) 
      { String lx1 = lexicals.get(j) + ""; 
        String lx2 = lexicals.get(j+1) + ""; 
        if (lx1.equals("}") && lx2.equals("{"))
        { expstart = j+2;
          Expression wexp = parse_ATLexpression(bc,expstart,en-1,entities,types);  
          Vector proplist = parse_propertytemplate_list(bc, st + 3, j, e, entities, types);
          if (e != null)
          { ObjectTemplateExp res = new ObjectTemplateExp(null,null,e);

            if ("<:=".equals(lexicals.get(st + 1) + ""))
            { res.setLeastChange(true); 
              System.out.println(">>> Least-change template: " + res); 
            } 
            res.setpart(proplist);
            res.setConstraint(wexp); 
            Attribute batt = new Attribute(bind, new Type(e), ModelElement.INTERNAL);
            res.bindsTo = batt;
            return res;
          }
        } 
      }

      Vector proplist = parse_propertytemplate_list(bc, st + 3, en, e, entities, types);
      if (e != null)
      { ObjectTemplateExp res = new ObjectTemplateExp(null,null,e);
        if ("<:=".equals(lexicals.get(st + 1) + ""))
        { res.setLeastChange(true); 
          // System.out.println(">>> Least-change template: " + res); 
        } 
        res.setpart(proplist);
        Attribute batt = new Attribute(bind, new Type(e), ModelElement.INTERNAL);
        res.bindsTo = batt;
        return res;
      }
    }
    return null;
  }

  public TemplateExp parse_collection_exp(int bc, int st, int en, Vector entities, Vector types)
  { // bindsTo : Set(T) { att1 ++ att2 } { optionalExpression }

    if (st > en) { return null; }

    String bind = lexicals.get(st) + "";

    if (st + 6 <= en && ":".equals(lexicals.get(st + 1) + "") &&
        ("Set".equals(lexicals.get(st + 2) + "") ||
         "Sequence".equals(lexicals.get(st + 2) + ""))
       )
    { String ename = lexicals.get(st + 4) + "";
      String ctname = lexicals.get(st + 2) + ""; 

      Entity e = (Entity) ModelElement.lookupByName(ename,entities);

      if (e == null) 
      { System.err.println("!! ERROR: no class called " + ename); 
        return null; 
      } 

      Type settyp = new Type(ctname, null); 
      settyp.setElementType(new Type(e)); 

      if ("{".equals(lexicals.get(st+6) + "") && 
          "+".equals(lexicals.get(st+8) + "") &&
          "+".equals(lexicals.get(st+9) + "") &&
          "}".equals(lexicals.get(st+11) + ""))
      { CollectionTemplateExp res = new CollectionTemplateExp(null,null,settyp);
        Attribute batt = new Attribute(bind, settyp, ModelElement.INTERNAL);
        batt.setElementType(new Type(e)); 
        res.bindsTo = batt;

        String member = lexicals.get(st+7) + ""; 
        Attribute memberatt = new Attribute(member, new Type(e), ModelElement.INTERNAL);
        res.setMember(memberatt); 

        String rest = lexicals.get(st+10) + ""; 
        Attribute restatt = new Attribute(rest, settyp, ModelElement.INTERNAL);
        restatt.setElementType(new Type(e)); 
        res.setRest(restatt); 
        
        if (st + 13 < en && "{".equals(lexicals.get(st+12) + "") 
            && "}".equals(lexicals.get(en) + ""))
        { Expression wexp = parse_ATLexpression(bc,st+13,en-1, entities,types); 
          if (wexp != null) 
          { res.setConstraint(wexp); } 
        } 
        return res;
      }
    }
    return null;
  }

  private Vector parse_propertytemplate_list(int bc, int st, int en, Entity scope, Vector entities, Vector types)
  { if (scope == null) { return null; }
    String sts = lexicals.get(st) + "";
    String ens = lexicals.get(en) + "";
    if ("{".equals(sts) && "}".equals(ens)) { }
    else { return null; }

    int cbcnt = 0;
    int prev = st + 1;

    Vector res = new Vector();
    for (int i = st+1; i < en; i++)
    { String lex = lexicals.get(i) + "";
      if ("{".equals(lex)) { cbcnt++; }
      else if ("}".equals(lex)) { cbcnt--; }
      else if (",".equals(lex) && "=".equals(lexicals.get(i+2) + "")  && cbcnt == 0)
      { // prop = value, or prop = template, at top level

        String pname = lexicals.get(prev) + "";
        // System.out.println("Property name = " + pname); 
        Type typ = scope.getFeatureType(pname); 
        Attribute att = new Attribute(pname, typ, ModelElement.INTERNAL);
        PropertyTemplateItem pti = new PropertyTemplateItem(att);

        if (i == prev + 3)
        { Expression val = parse_basic_expression(bc,prev+2,prev+2,entities,types);
          pti.setValue(val);
          res.add(pti);
        }
        else // an object template
        { ObjectTemplateExp te = (ObjectTemplateExp) parse_template_exp(bc,prev+2,i-1,entities,types); 
          if (te != null) 
          { pti.setTemplate(te); } 
          else 
          { Expression ve = parse_expression(bc, prev+2, i-1,entities,types); 
            pti.setValue(ve); 
          } 
          res.add(pti); 
          System.out.println(">>> Parsed template item " + pti); 
        } 
        prev = i+1;
      }
    }
    String pname = lexicals.get(prev) + "";
    // System.out.println("Property name = " + pname); 

    Type typ = scope.getFeatureType(pname); 
    Attribute att = new Attribute(pname, typ, ModelElement.INTERNAL);
    PropertyTemplateItem pti = new PropertyTemplateItem(att);

    if (en == prev + 3)
    { Expression val = parse_basic_expression(bc,prev+2,prev+2,entities,types);
      pti.setValue(val);
      res.add(pti);
    }
    else // an object template
    { ObjectTemplateExp te = (ObjectTemplateExp) parse_template_exp(bc,prev+2,en-1,entities,types); 
      if (te != null) 
      { pti.setTemplate(te); } 
      else 
      { Expression ve = parse_expression(bc, prev+2, en-1,entities,types); 
        pti.setValue(ve); 
      } 
      res.add(pti);
    }      
    // System.out.println("Parsed template item " + pti); 

    return res;
  }


  public XMLNode parseXML()
  { for (int i = 0; i < lexicals.size(); i++) 
    { if ("?".equals(lexicals.get(i) + "") && 
          ">".equals(lexicals.get(i+1) + ""))
      { return parsexmlnode(i+2,lexicals.size() - 1); } 
    } 
    return null; 
  } 

  public XMLNode parseXMLNode()
  { return parsexmlnode(0,lexicals.size() - 1); } 

  public XMLNode parsexmlnode(int st, int en)
  { if (st < lexicals.size() && "<".equals(lexicals.get(st) + ""))
    { String tag = lexicals.get(st + 1) + "";

      // System.out.println(">>> Parsing XML tag: " + tag); 

      XMLNode xnode = new XMLNode(tag);
      for (int i = st + 2; i <= en && i < lexicals.size(); i++)
      { String str = lexicals.get(i) + "";


        if (i + 1 <= en)
        { if ("=".equals(lexicals.get(i+1) + ""))
          { XMLAttribute xatt = new XMLAttribute(str, lexicals.get(i+2) + "");
            xnode.addAttribute(xatt);
          }
          else if ("</".equals(str) && i + 2 <= en && 
              tag.equals(lexicals.get(i+1) + "") &&
              ">".equals(lexicals.get(i+2) + ""))
          { return xnode; }
        }

        if (">".equals(str))
        { Vector sns = parsesubnodes(xnode, tag, i+1, en);
          xnode.setsubnodes(sns);
          return xnode;
        }

        if ("/>".equals(str))
        { return xnode; }
      }
    }
    return null;
  }


  public Vector parsesubnodes(XMLNode xnode, String tag, int st, int en)
  { Vector res = new Vector();
    // System.out.println(">>> Parsing subnodes of " + tag); 

    String content = ""; 

    for (int i = st; i <= en; i++)
    { String str = lexicals.get(i) + "";
      if ("</".equals(str) && i + 2 <= en &&
           tag.equals(lexicals.get(i+1) + "") &&
           ">".equals(lexicals.get(i+2) + ""))
      { if (i > st && xnode != null) 
        { xnode.setContent(content); }  
        return res;
      } 
 

      if (">".equals(str)) 
      { String tag1 = lexicals.get(st + 1) + ""; 
        for (int j = i+1; j < en; j++) 
        { String str1 = lexicals.get(j) + ""; 
          if ("</".equals(str1) && tag1.equals(lexicals.get(j+1) + ""))
          { XMLNode xn = parsexmlnode(st,j+2); 
            // System.out.println(">>> Parsed inner XML node: " + xn);
            if (xn != null) { res.add(xn); } 
            res.addAll(parsesubnodes(null,tag,j+3,en));
            return res;
          } 
        }
      }  

      if ("</".equals(str) && i + 2 <= en &&
           !(tag.equals(lexicals.get(i+1) + "")) &&
           ">".equals(lexicals.get(i+2) + ""))
      { XMLNode xn = parsexmlnode(st,i+2);

        // System.out.println(">>> Parsed XML node: " + xn); 

        if (xn != null) { res.add(xn); }
        res.addAll(parsesubnodes(null,tag,i+3,en));
        return res;
      }

      if ("/>".equals(str))
      { XMLNode xn = parsexmlnode(st,i);

        // System.out.println(">>> Parsed XML subnode: " + xn); 

        if (xn != null) { res.add(xn); }
        res.addAll(parsesubnodes(null,tag,i+1,en));
        return res;
      }
      else 
      { content = content + str; } 
    }
    return res;
  } 

  
  public USEMetaModel parseUSEMetaModel()
  { USEMetaModel m = null; 
    int st = 0; 
    int en = lexicals.size(); 
    String str = lexicals.get(st) + ""; 
    if (st < en && "model".equals(str))
    { m = new USEMetaModel(lexicals.get(st + 1) + ""); } 
    else 
    { return m; }
    return parseUSEdeclarations(m,st + 2,en-1); 
  } 

  public USEMetaModel parseUSEdeclarations(USEMetaModel m, int st, int en) 
  { if (st > en) { return m; } 

    for (int i = st; i <= en; i++) 
    { String str = lexicals.get(i) + ""; 
      if ("class".equals(str))
      { for (int j = i; j <= en; j++) 
        { if ("end".equals(lexicals.get(j) + ""))
          { USEClassDeclaration cd = parseUSEClassDeclaration(i,j);
            if (cd != null) { m.addClass(cd); } 
            return parseUSEdeclarations(m,j+1,en);
          } 
        } 
      } 
      else if ("association".equals(str))
      { for (int j = i; j <= en; j++) 
        { if ("end".equals(lexicals.get(j) + ""))
          { USEAssociationDeclaration ad = parseUSEAssociationDeclaration(i,j); 
            if (ad != null) { m.addAssociation(ad); }  
            return parseUSEdeclarations(m,j+1,en); 
          } 
        } 
      } 
    }
    return m; 
  } 

  public USEClassDeclaration parseUSEClassDeclaration(int st, int en)
  { if (st < en)
    { String nme = lexicals.get(st + 1) + ""; 
      return new USEClassDeclaration(nme); 
    } 
    return null; 
  } 

  public USEAssociationDeclaration parseUSEAssociationDeclaration(int st, int en)
  { if (st + 1 < en) 
    { String nme = lexicals.get(st + 1) + ""; 
      USEAssociationDeclaration ad = new USEAssociationDeclaration(nme); 

      if ("between".equals(lexicals.get(st + 2) + ""))
      { for (int i = st + 3; i + 5 < en; i = i + 6) 
        { String ent = lexicals.get(i) + ""; 
          String mult = lexicals.get(i+2) + ""; 
          String rolename = lexicals.get(i+5) + ""; 
          System.out.println("Parsed role: " + ent + " " + mult + " " + rolename); 

          USERoleDeclaration rle = new USERoleDeclaration(ent,mult,rolename); 
          ad.addRole(rle); 
        } 
      } 
      return ad; 
    } 
    return null; 
  } 

  public static boolean isNounPhraseWord(String lex)
  { if (lex.endsWith("_DT") || lex.endsWith("_PDT") || lex.endsWith("_NNPS") || lex.endsWith("_WDT") ||
        lex.endsWith("_WH") || lex.endsWith("_FW") || lex.endsWith("_JJR") || 
        lex.endsWith("_CD") || lex.endsWith("_NNS") || lex.endsWith("_LS") || lex.endsWith("_JJS") ||
	    lex.endsWith("_NN") || lex.endsWith("_NNP") || lex.endsWith("_JJ") || lex.endsWith("_PRP") || 
		lex.endsWith("_PRP$") || lex.endsWith("_WP$"))
	 { return true; } 
	 return false; 
  } 
  
  public static boolean isVerbPhraseWord(String lex)
  { if (lex.endsWith("_VB") || lex.endsWith("_VBZ") || lex.endsWith("_TO") || lex.endsWith("_VBG") || 
        lex.endsWith("_MD") || lex.endsWith("_IN") || lex.endsWith("_VBD") ||
	    lex.endsWith("_VBN") || lex.endsWith("_VBP") || lex.endsWith("_RB") || lex.endsWith("_WRB") || 
		lex.endsWith("_EX"))
    { return true; }
	return false; 
  }
  
  public static boolean isConjunctionWord(String lex)
  { if (lex.endsWith("_IN") || lex.endsWith("_CC"))
    { return true; }
    return false; 
  }

  public Vector parseTaggedText()
  { int en = lexicals.size();
    // System.out.println("Lexicals = " + lexicals);  
    Vector newlexicals = preprocessWords(0,en-1);
	// System.out.println("New lexicals = " + newlexicals); 
    lexicals.clear(); 
    lexicals.addAll(newlexicals);
    int len = lexicals.size();   
    return parseTaggedText(0,len-1); 
  }


  public Vector parseRequirementsText()
  { int en = lexicals.size();
    // System.out.println("Lexicals = " + lexicals);  
    Vector newlexicals = preprocessWords(0,en-1);
	// System.out.println("New lexicals = " + newlexicals); 
    lexicals.clear(); 
    lexicals.addAll(newlexicals);
    int len = lexicals.size();   
    return parseRequirementsText(0,len-1); 
  }
  
  public Vector preprocessWords(int st, int en)
  { Vector res = new Vector(); 
    for (int j = st; j <= en; j++)
	{ String lex1 = lexicals.get(j) + "";
	  if ("-".equals(lex1) && j < en) 
	  { String lex2 = lexicals.get(j+1) + ""; 
	    if ("RRB".equals(lex2) && j <= en-2)
		{ j = j + 1; }
		else if ("LRB".equals(lex2) && j <= en-2)
		{ j = j + 1; }
		else if ("_".equals(lex2) && j <= en-2)
		{ j = j + 1; }
		else 
		{ res.add(lex1); }
	  } 
	  else
	  { res.add(lex1); }
	} 
	
	// res.addAll(lexicals); 
	int newen = res.size() - 1; 
	Vector newres = new Vector();
	  
	
    for (int i = st; i <= newen; i++) 
    { String lex1 = res.get(i) + "";
	  if (i+1 <= newen) 
	  { String lex2 = res.get(i+1) + "";  
	    if ("-".equals(lex2) && i+2 <= newen)
	    { String lex3 = lexicals.get(i+2) + "";
	      String composed = lex1 + "-" + lex3; 
          newres.add(composed);  
		  i = i + 2; 
	    }  
	    else if ("LRB".equals(lex1) || "RRB".equals(lex1) || lex1.startsWith("_")) 
	    {} 
	    else 
	    { newres.add(lex1); }
	  } 
	  else 
	  { newres.add(lex1); } 
	} 
	return newres; 
  }

  public Vector parseTaggedText(int st, int en)
  { Vector res = new Vector();
    int index = 0;  
    for (int i = st; i < en; i++) 
    { NLPWord wd = parseOneWord(i,i,index); 
      if (wd != null) 
      { res.add(wd); } 
      index++; 
    } 
    return res; 
  } 
  
  public Vector parseRequirementsText(int st, int en)
  { Vector res = new Vector(); 
    Vector noun1 = new Vector(); 
    int i = st; 
    boolean innoun1 = true; 
    while (i <= en && innoun1)
    { String lex = lexicals.get(i) + ""; 
      if (isNounPhraseWord(lex))
      { noun1.add(lex);
        i++;  
      }
      else if (isVerbPhraseWord(lex))
      { innoun1 = false; }
      else 
      { i++; } 
    } 
    RequirementsPhrase nounphrase1 = new RequirementsPhrase("noun", noun1); 
    res.add(nounphrase1);
	 
    boolean inverb1 = true;
    Vector verb1 = new Vector();  
    
    while (i <= en && inverb1)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb1 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb1.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb1 = false; }
	} 
	RequirementsPhrase verbphrase1 = new RequirementsPhrase("verb", verb1); 
	res.add(verbphrase1); 
	
	Vector noun2 = new Vector(); 
	boolean innoun2 = true; 
    while (i <= en && innoun2)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun2.add(lex); 
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun2 = false; }
	  else 
	  { i++; }
	} 
	RequirementsPhrase nounphrase2 = new RequirementsPhrase("noun", noun2); 
	res.add(nounphrase2);
	
	boolean inverb2 = true;
	Vector verb2 = new Vector();  
    
	while (i <= en && inverb2)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb2 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb2.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb2 = false; }
	} 
	RequirementsPhrase verbphrase2 = new RequirementsPhrase("verb", verb2); 
	res.add(verbphrase2); 
	
	Vector noun3 = new Vector(); 
	boolean innoun3 = true; 
    while (i <= en && innoun3)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun3.add(lex);
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun3 = false; }
	  else 
	  { i++; } 
	} 
	RequirementsPhrase nounphrase3 = new RequirementsPhrase("noun", noun3); 
	res.add(nounphrase3);

	boolean inverb3 = true;
	Vector verb3 = new Vector();  
    
	while (i <= en && inverb3)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb3 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb3.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb3 = false; }
	} 
	RequirementsPhrase verbphrase3 = new RequirementsPhrase("verb", verb3); 
	res.add(verbphrase3); 
	
	Vector noun4 = new Vector(); 
	boolean innoun4 = true; 
    while (i <= en && innoun4)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun4.add(lex);
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun4 = false; }
	  else 
	  { i++; } 
	} 
	RequirementsPhrase nounphrase4 = new RequirementsPhrase("noun", noun4); 
	res.add(nounphrase4);

	boolean inverb4 = true;
	Vector verb4 = new Vector();  
    
	while (i <= en && inverb4)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb4 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb4.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb4 = false; }
	} 
	RequirementsPhrase verbphrase4 = new RequirementsPhrase("verb", verb4); 
	res.add(verbphrase4); 
	
	Vector noun5 = new Vector(); 
	boolean innoun5 = true; 
    while (i <= en && innoun5)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun5.add(lex);
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun5 = false; }
	  else 
	  { i++; } 
	} 
	RequirementsPhrase nounphrase5 = new RequirementsPhrase("noun", noun5); 
	res.add(nounphrase5);

	boolean inverb5 = true;
	Vector verb5 = new Vector();  
    
	while (i <= en && inverb5)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb5 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb5.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb5 = false; }
	} 
	RequirementsPhrase verbphrase5 = new RequirementsPhrase("verb", verb5); 
	res.add(verbphrase5); 
	
	Vector noun6 = new Vector(); 
	boolean innoun6 = true; 
    while (i <= en && innoun6)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun6.add(lex);
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun6 = false; }
	  else 
	  { i++; } 
	} 
	RequirementsPhrase nounphrase6 = new RequirementsPhrase("noun", noun6); 
	res.add(nounphrase6);

	boolean inverb6 = true;
	Vector verb6 = new Vector();  
    
	while (i <= en && inverb6)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex))
	  { inverb6 = false; }
	  else if (isVerbPhraseWord(lex))
	  { verb6.add(lex); 
	    i++; 
	  } 
	  else 
	  { inverb6 = false; }
	} 
	RequirementsPhrase verbphrase6 = new RequirementsPhrase("verb", verb6); 
	res.add(verbphrase6); 
	
	Vector noun7 = new Vector(); 
	boolean innoun7 = true; 
    while (i <= en && innoun7)
    { String lex = lexicals.get(i) + "";
	  if (isNounPhraseWord(lex) || isConjunctionWord(lex))
	  { noun7.add(lex);
	    i++; 
	  }
	  else if (isVerbPhraseWord(lex))
	  { innoun7 = false; }
	  else 
	  { i++; } 
	} 
	RequirementsPhrase nounphrase7 = new RequirementsPhrase("noun", noun7); 
	res.add(nounphrase7);

    if (i < en)
	{ System.out.println("!! Requirements sentence too long!! Unprocessed text: " + showLexicals(i,en)); }
	System.out.println(); 
	
	return res; 
  }    
  // Comparatives: _RBR, _RBS, _JJR
  // Conjunctions: _CC, _IN
  // Existential: _EX
  // Particle: _RP
  // Possessive ending _POS
  // Whose: _WP$
  // Where adverb _WRB
  
  public NLPSentence parseNLP()
  { int en = lexicals.size(); 
    // Vector mes = new Vector(); 
    // for (int i = 0; i < en; i++)
	// { String lex = lexicals.get(i) + ""; 
	//   if ("Constituency".equals(lex) && i+1 < en && "parse".equals(lexicals.get(i+1) + ""))
	//   { for (int j = i+2; j < en; j++) 
	//     { String lex2 = lexicals.get(j) + ""; 
	// 	  if ("Dependency".equals(lex2) && j+1 < en && "parse".equals(lexicals.get(j+1) + ""))
	
   System.out.println(); 
   System.out.println(">>> Trying to parse " + showLexicals(0,en-1)); 

   // ASTTerm r1 = parseGeneralAST(0, en-1); 
   System.out.println(); 

   // System.out.println(">>> As general AST: " + r1); 
  
   NLPSentence res = parseRoot(0,en-1); 
   if (res == null) 
   { System.err.println("!! Failed to parse sentence"); }
   return res; 
 }
	//	} 
	//  } 
	// }
	// return ""; 
 
  public NLPWord parseOneWord(int st, int en, int index) 
  { // Pre: en >= st + 3

    
    String lex0 = "" + lexicals.get(st); 
    String lex1 = "" + lexicals.get(en); 
    if (st == en) 
    { int sb = lex0.indexOf("_"); 
      if (sb < 0) { return null; } 
      NLPWord wd = new NLPWord(lex0.substring(sb+1,lex0.length()), lex0.substring(0,sb));
      wd.setIndex(index); 
      return wd;  
    } 
    /* else if (en == st + 7 && "(".equals(lex0) && ")".equals(lex1) && 
	         "-".equals(lexicals.get(st+1) + "") && "-".equals(lexicals.get(en-1) + ""))
    { NLPWord res = new NLPWord("BRACKET", lexicals.get(st+2) + ""); 
      return res; 
    } */ 
    return null; 
  }
  
  public NLPWord parseWord(int st, int en) 
  { // Pre: en >= st + 3
  
    String lex0 = "" + lexicals.get(st); 
    String lex1 = "" + lexicals.get(en); 
    if (en == st + 3 && "(".equals(lex0) && ")".equals(lex1))
    { NLPWord res = new NLPWord("" + lexicals.get(st + 1), "" + lexicals.get(st + 2)); 
      return res; 
    }
    else if (en == st + 7 && "(".equals(lex0) && ")".equals(lex1) && 
	         "-".equals(lexicals.get(st+1) + "") && "-".equals(lexicals.get(en-1) + ""))
    { NLPWord res = new NLPWord("BRACKET", lexicals.get(st+2) + ""); 
      return res; 
    }
    else if (en > st + 3 && "(".equals(lex0) && ")".equals(lex1) && 
	         "POS".equals(lexicals.get(st+1) + ""))
    { NLPWord res = new NLPWord("POS", "POS"); 
      return res; 
    }
	// System.err.println("!! Not a word: " + showLexicals(st,en));
    return null; 
  }
  
  public NLPPhrase parsePhrase(int st, int en) 
  { String lex0 = "" + lexicals.get(st); 
    String lex1 = "" + lexicals.get(en); 
    if (en == st + 7 && "(".equals(lex0) && ")".equals(lex1) && 
	         "-".equals(lexicals.get(st+1) + "") && "-".equals(lexicals.get(en-1) + ""))
    { NLPPhrase res = new NLPPhrase("BRACKET", new Vector()); 
	  return res; 
    }
    else if (en == st + 5 && "(".equals(lex0) && ")".equals(lex1) && 
	         "-".equals(lexicals.get(st+1) + "") && "{".equals(lexicals.get(en-1) + ""))
    { NLPPhrase res = new NLPPhrase("OBRACKET", new Vector()); 
      return res; 
    }
    else if (en == st + 5 && "(".equals(lex0) && ")".equals(lex1) && 
	         "-".equals(lexicals.get(st+1) + "") && "}".equals(lexicals.get(en-1) + ""))
	{ NLPPhrase res = new NLPPhrase("CBRACKET", new Vector()); 
	  return res; 
	}
	else if (en > st + 3 && "(".equals(lex0) && ")".equals(lex1))
	{ String tag = "" + lexicals.get(st+1); 
	  Vector phs = parsePhraseList(st+2,en-1); 
	  if (phs == null) 
	  { // System.err.println("!! Not a phrase list: " + showLexicals(st+2,en-1)); 
	    return null; 
	  }
	  else 
	  { return new NLPPhrase(tag,phs); } 
	} 
	return null;  
  }
  
  public Vector parsePhraseList(int st, int en)
  { // a sequence of either words (Tag Wd) or phrases.
    if (st >= en) 
    { return new Vector(); }
    if (en < st+3) 
    { return null; } 
	
    NLPWord wd = parseWord(st,st+3); 
    if (wd != null) 
    { Vector rest = parsePhraseList(st+4,en); 
      if (rest == null) 
      { return null; }
      Vector res = new Vector(); 
      res.add(wd); 
      res.addAll(rest); 
      return res; 
    } 
    else 
    { int ocount = 1; 
      int ccount = 0; 
	  
      for (int i = st+1; i <= en; i++) 
      { String lexend = lexicals.get(i) + ""; 
        if ("(".equals(lexend))
        { ocount++; }
        else if (")".equals(lexend))
        { ccount++; }
		
        if (")".equals(lexend) && ocount == ccount)
        { NLPPhrase pr = parsePhrase(st,i); 
          if (pr != null) 
          { Vector rem = parsePhraseList(i+1,en); 
            if (rem == null) { return null; } 
            Vector result = new Vector(); 
            result.add(pr); 
            result.addAll(rem);
            return result; 
          }
        }
      }
    }
    return null; 
  } 
  
	 /* int ocount = 1; 
	  int ccount = 0; 
	  
	  for (int i = st+1; i <= en; i++) 
	  { String lexend = lexicals.get(i) + ""; 
	    if ("(".equals(lexend))
		{ ocount++; }
		else if (")".equals(lexend))
		{ ccount++; }
		
	    if (")".equals(lexend) && ocount == ccount)
	    { ASTTerm pn = parseGeneralAST(st,i); 
	      if (pn != null && i < en) 
		  { Vector rest = parseGeneralASTSequence(i+1,en); 
		    if (rest != null)
		    { res.add(pn); 
		      res.addAll(rest); 
		      return res; 
		    }
		  }
		  else if (pn != null && i >= en) 
		  { res.add(pn); 
		    return res; 
		  }
	    }
	  }
	}
	else if ("(".equals(lex) && st < en)
	{ // the ( is a symbol  
	  ASTSymbolTerm sym = new ASTSymbolTerm("("); 
	  Vector rem = parseGeneralASTSequence(st+1,en); 
      if (rem != null)
      { res.add(sym); 
        res.addAll(rem); 
        return res; 
      }
	}
	else if (st < en)
	{ ASTSymbolTerm sym = new ASTSymbolTerm(lex); 
	  Vector rem = parseGeneralASTSequence(st+1,en); 
      if (rem != null)
      { res.add(sym); 
        res.addAll(rem); 
        return res; 
      }
	} */ 


  public NLPSentence parseSentence(int st, int en) 
  { String lex0 = "" + lexicals.get(st); 
    String lex1 = "" + lexicals.get(en); 
	if (en > st + 3 && "(".equals(lex0) && ")".equals(lex1))
	{ String tag = "" + lexicals.get(st+1); 
	  Vector phs = parsePhraseList(st+2,en-1); 
	  if (phs == null) 
	  { // System.err.println("!! Not a phrase list: " + showLexicals(st+2,en-1)); 
	    return null; 
	  }
	  else 
	  {	return new NLPSentence(tag,phs); } 
	} 
	return null;  
  }
  
  public NLPSentence parseRoot(int st, int en) 
  { String lex0 = "" + lexicals.get(st); 
    String lex1 = "" + lexicals.get(en);
	
	System.out.println();  
	// System.out.println("***** " + lex0 + " " + lex1); 
	System.out.println(); 
	
	if (en > st + 3 && "(".equals(lex0) && ")".equals(lex1))
	{ String tag = "" + lexicals.get(st+1); 
	  NLPSentence sent = parseSentence(st+2,en-1); 
	  if (sent == null) 
	  { System.err.println("!! Error: Not a sentence: " + showLexicals(st+2,en-1)); 
	    return null; 
       }
       return sent; 
     } 
     return null;  
  }

  public static void main(String[] args)
  { // System.out.println(Double.MAX_VALUE); 
    Compiler2 c = new Compiler2();

  /*  String testast = "(expression (logicalExpression (equalityExpression (additiveExpression (factorExpression C_{ (expression (logicalExpression (equalityExpression (additiveExpression (factorExpression (factor2Expression (basicExpression 2))))))) } ^{ (expression (logicalExpression (equalityExpression (additiveExpression (factorExpression (factor2Expression (basicExpression 4))))))) })))))"; */ 

    // ASTTerm asst = c.parseGeneralAST(testast); 
    // System.out.println(asst); 

    // ASTTerm asst = c.parseMathOCLAST(testast); 
    // System.out.println(asst.literalForm());
    // System.out.println(asst.literalFormSpaces());

       //   Vector ents = new Vector(); 
       //   Vector typs = new Vector(); 
       //   CGSpec cgs = new CGSpec(ents,typs); 
       //   File fs = new File("cg/simplify.cstl"); 
       //   CSTL.loadCSTL(cgs,fs,ents,typs);
 
       //   String entcode = asst.cg(cgs);

       //   System.out.println(entcode); 
 

    // c.nospacelexicalanalysis("sq->iterate(v; acc = 0 | v + acc)"); 

    // c.nospacelexicalanalysis("SortedSet{x,a,y}");

    // c.nospacelexicalanalysis("(a[i][j]).f(1)");  
    // c.nospacelexicalanalysis("(!a).f(1)"); 
    // c.nospacelexicalanalysis("(OclFile[\"SYSOUT\"]).println(x)");

    // c.nospacelexicalanalysis("Map{ \"Name\" |-> Sequence{\"Braund, Mr. Owen Harris\"}->union(Sequence{\"Allen, Mr. William Henry\"}->union(Sequence{ \"Bonnell, Miss. Elizabeth\" })) }->union(Map{ \"Age\" |-> Sequence{22}->union(Sequence{35}->union(Sequence{ 58 })) }->union(Map{ \"Sex\" |-> Sequence{\"male\"}->union(Sequence{\"male\"}->union(Sequence{ \"female\" })) }->union(Map{ \"Fare\" |-> Sequence{102.0}->union(Sequence{99.0}->union(Sequence{ 250.0 })) }) ) )"); 

  
c.nospacelexicalanalysis("arr[i].x"); 
Expression zz = c.parseExpression(); 
System.out.println(zz); 

 /* c.nospacelexicalanalysis("table->restrict(table > v)");  
 BinaryExpression zz = (BinaryExpression) c.parseExpression(); 

 Expression zleft = zz.getLeft(); 
 zleft.setType(new Type("Sequence", null)); 

 System.out.println(zz);

 Expression yy = zz.transformPythonSelectExpressions(); 

 System.out.println(yy); */ 
 
 /* 
    c.nospacelexicalanalysis("execute (OclFile[\"system.in\"]).println(x)"); 

    Statement stat = c.parseStatement(); 

    System.out.println(stat); 

    c.showLexicals(); */ 

    // c.filterLexicals(); 

    // c.showLexicals(); 

    // stat = c.parseStatement(); 

    // System.out.println(stat); 

    /* zz.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 

    Expression pp = zz.simplifyOCL(); 

    System.out.println(pp); 

    pp.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 

    System.out.println(">>> " + pp.getType()); */ 

    // Compiler2 ccx = new Compiler2(); 
    // ccx.nospacelexicalanalysis("x : int"); 
    // ModelElement zz = ccx.parseParameterDeclaration(new Vector(), new Vector()); 
    // System.out.println(zz); 
    // System.out.println(zz.getType()); 

    // c.nospacelexicalanalysis("E<String,int>"); 
     
    // Vector exs = new Vector(); 
    // exs.add(new Entity("E")); 
    // Type xx = c.parseType(0,c.lexicals.size()-1,exs, new Vector()); 

    // c.nospacelexicalanalysis("arr[x][y]"); 

   // c.nospacelexicalanalysis("(OclDatasource.newOclDatasource()).execSQL(\"SELECT * FROM E\")"); 
    // c.nospacelexicalanalysis("Excel.Worksheets[Y].Range[X].Value");

   // c.nospacelexicalanalysis("(Excel.Worksheets[\"FBU\"].Range[\"k19\"]).Offset(I, 0)"); 

    // c.nospacelexicalanalysis("(OclFile.newOclFile_Read(OclFile.newOclFile(s))).readObject()"); 

    // c.nospacelexicalanalysis("(MyString).subrange((MyString)->indexOf((MyString)->trim()))"); 
	
    // c.nospacelexicalanalysis("(createCOBOLADD_Class()).COBOLADD(X1, X2)"); 
    // System.out.println(c.lexicals); 
    // System.out.println(c.balancedBrackets()); 
    // Expression xx = c.parseExpression(); 
    // System.out.println(xx); 

    // ASTTerm tt = c.parseSimpleAST("(lineStringLiteral \" (lineStringContent text) \")"); 

    // System.out.println(tt); 

    // CGRule rr = c.parse_TextCodegenerationrule("_1 = _2 |-->var _1 : _2`type<action> _1 _2`type"); 
    // System.out.println(rr); 

    // rr.replaceParameter("int"); 

    // System.out.println(rr); 
    
    // c.nospacelexicalanalysis(" while (x > 0) do  if (x = 5) then  break else continue"); 
    // Statement stat = c.parseStatement(); 
    // System.out.println(stat); 

    // Compiler2 comp = new Compiler2();  
    // Vector vv = new Vector(); 
    // comp.nospacelexicalanalysis("x <>= y"); 
    // Expression tt = comp.parseExpression(vv,vv); 
        
    // System.out.println(tt); 

    // c = new Compiler2(); 

    // c.nospacelexicalanalysis("i < v->size() & i < w->size()"); 
    // Expression e = c.parseExpression(); 
    // System.out.println(e); 

    // c.nospacelexicalanalysis("( var i : int ; i := 0 ; while ( i < v->size() & i < w->size() ) do i := i+1)");

    // c.nospacelexicalanalysis("( var ss : Set ; ss := Set{} ; var ot : OclIterator ; ot := OclIterator.newOclIterator(ss) ; var en : OclIterator ; en := OclIterator.newOclIterator(\"a long string\") )"); 

    // System.out.println(c.lexicals); 
  
    // Statement e = c.parseStatement(); 
    // System.out.println(e); 

    // Compiler2 cc = new Compiler2();
    // cc.nospacelexicalanalysis("Ref(int){5}"); 
    // System.out.println(cc.parseExpression()); 
 
    // cc.nospacelexicalanalysis("x <>= y"); 
    // System.out.println(cc.lexicals); 

    // cc.nospacelexicalanalysis("a[x][y+1][z*z]");
    // cc.nospacelexicalanalysis("(if b.subrange(10+1, b.size)->indexOf(\"a\"+\"\") > 0 then (b.subrange(10+1, b.size)->indexOf(\"a\"+\"\") + 10 - 1) else -1 endif)");
    // cc.nospacelexicalanalysis("x->oclAsType(E).att"); 

    // cc.nospacelexicalanalysis("?(x) = ?y[10]"); 
    // System.out.println(cc.lexicals); 
    // cc.nospacelexicalanalysis("(lambda s : String in (s->size() > 1))->apply(_var)"); 
    // cc.nospacelexicalanalysis("f->apply(_var)"); 
    // cc.nospacelexicalanalysis("lambda s : String in (s->size() > 1)"); 

    // System.out.println(cc.parseExpression()); 

    // System.out.println(cc.parseATLExpression()); 

    // cc.nospacelexicalanalysis("query op() : String pre: true post: true activity: skip"); 
    // BehaviouralFeature tt = cc.parseOperation(new Vector(), new Vector()); 
       
    // cc.nospacelexicalanalysis("attribute sq : Sequence(String)"); 
    // Attribute tt = cc.parseAttribute(new Vector(), new Vector());  
    // System.out.println(tt.toAST()); 

    /* 
 
    Vector v1 = new Vector(); 
    Vector v2 = new Vector(); 
    File cg = new File("cg/cg.cstl");

    Vector auxcstls = new Vector(); 
    // auxcstls.add("mapExpressionStatements.cstl"); 
    // auxcstls.add("cgprotocol.cstl"); 
 
    CGSpec cgs = CSTL.loadCSTL(cg,v1,v2); 
    CSTL.loadTemplates(auxcstls,v1,v2); 

    System.out.println(">>> CSTL ruleset: " + cgs); 

    ASTTerm xx =
      c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId x) = (variableInitializer (expression (expression (primary Short)) . MAX_VALUE))))) ;) (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType List)) (variableDeclarators (variableDeclarator (variableDeclaratorId p) = (variableInitializer (expression (expression (primary Collections)) . EMPTY_LIST))))) ;) (blockStatement (statement (expression (expression (primary x)) = (expression (expression (primary Collections)) . (methodCall min ( (expressionList (expression (primary p))) )))) ;)) }))"); 

    File chtml = new File("output/out.txt"); 
    try
    { PrintWriter chout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(chtml)));
      xx.asTextModel(chout); 
      chout.close(); 
    } catch (Exception __e) { }  
    

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType List)) (variableDeclarators (variableDeclarator (variableDeclaratorId pr) = (variableInitializer (expression new (creator (createdName LinkedList) (classCreatorRest (arguments ( ))))))))) ;) (blockStatement (statement (expression (expression (primary v)) = (expression (expression (primary pr)) . (methodCall getFirst ( )))) ;)) (blockStatement (statement (expression (expression (primary pr)) . (methodCall getLast ( ))) ;)) (blockStatement (statement (expression (expression (primary pr)) . (methodCall addAll ( (expressionList (expression (primary st))) ))) ;)) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId x) = (variableInitializer (expression (expression (primary Math)) . (methodCall floor ( (expressionList (expression (primary (literal (floatLiteral 5.0))))) ))))))) ;) (blockStatement (localVariableDeclaration (typeType (primitiveType double)) (variableDeclarators (variableDeclarator (variableDeclaratorId y) = (variableInitializer (expression (expression (primary Math)) . (methodCall sin ( (expressionList (expression (primary x))) ))))))) ;) (blockStatement (localVariableDeclaration (typeType (primitiveType double)) (variableDeclarators (variableDeclarator (variableDeclaratorId z) = (variableInitializer (expression (expression (primary Math)) . (methodCall pow ( (expressionList (expression (primary x)) , (expression (primary y))) ))))))) ;) }))"); 

   // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType double)) (variableDeclarators (variableDeclarator (variableDeclaratorId p) = (variableInitializer (expression (expression (primary Math)) . PI))))) ;) }))"); 

      // c.parseGeneralAST("(statement (expression (expression (primary xseq)) . (methodCall add ( (expressionList (expression (primary p))) ))) ;)");
 
    // c.parseGeneralAST("(statement for ( (forControl (enhancedForControl (typeType (classOrInterfaceType Integer)) (variableDeclaratorId x) : (expression (primary Lst)))) ) (statement (block { (blockStatement (statement (expression (expression (expression (primary System)) . out) . (methodCall println ( (expressionList (expression (primary x))) ))) ;)) })))"); 

   //  c.parseGeneralAST("(statement for ( (forControl (forInit (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId i) = (variableInitializer (expression (primary (literal (integerLiteral 0))))))))) ; (expression (expression (primary i)) < (expression (primary (literal (integerLiteral 10))))) ; (expressionList (expression (expression (primary i)) ++))) ) (statement (block { (blockStatement (statement (expression (expression (primary x)) *= (expression (primary i))) ;)) })))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType double)) (variableDeclarators (variableDeclarator (variableDeclaratorId x)))) ;) (blockStatement (statement (expression (expression (primary x)) = (expression (primary (literal (floatLiteral 3.0))))) ;)) (blockStatement (statement (expression (expression (primary y)) = (expression (expression (primary ( (expression (expression (primary x)) > (expression (primary (literal (floatLiteral 1.0))))) ))) ? (expression (primary (literal (integerLiteral 0)))) : (expression (primary (literal (integerLiteral 1)))))) ;)) }))"); 

  //  c.parseGeneralAST("(expression (expression (expression (primary System)) . out) . (methodCall println ( (expressionList (expression (primary x))) )))"); 

   //   c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType short) [ ]) (variableDeclarators (variableDeclarator (variableDeclaratorId sq) = (variableInitializer (expression new (creator (createdName (primitiveType int)) (arrayCreatorRest [ (expression (primary (literal (integerLiteral 3)))) ]))))))) ;) (blockStatement (statement for ( (forControl (forInit (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId i) = (variableInitializer (expression (primary (literal (integerLiteral 0))))))))) ; (expression (expression (primary i)) < (expression (expression (primary sq)) . length)) ; (expressionList (expression (expression (primary i)) ++))) ) (statement (block { (blockStatement (statement (expression (expression (expression (primary System)) . out) . (methodCall println ( (expressionList (expression (expression (primary sq)) [ (expression (primary i)) ])) ))) ;)) })))) }))"); 

      // c.parseGeneralAST("(statement (block { (blockStatement (statement while (parExpression ( (expression (expression (primary x)) > (expression (primary (literal (integerLiteral 0))))) )) (statement (block { (blockStatement (statement if (parExpression ( (expression (expression (primary x)) == (expression (primary (literal (integerLiteral 5))))) )) (statement (block { (blockStatement (statement break ;)) })) else (statement (block { (blockStatement (statement continue ;)) })))) })))) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (statement do (statement (block { (blockStatement (statement (expression (expression (primary x)) = (expression (expression (primary x)) * (expression (primary y)))) ;)) })) while (parExpression ( (expression (expression (primary x)) < (expression (primary (literal (integerLiteral 10))))) )) ;)) (blockStatement (statement (expression (methodCall call ( (expressionList (expression (primary x))) ))) ;)) }))"); 

    // c.parseGeneralAST("(statement (block { (blockStatement (statement try (block { (blockStatement (localVariableDeclaration (typeType (primitiveType char)) (variableDeclarators (variableDeclarator (variableDeclaratorId c) = (variableInitializer (expression (expression (primary str)) . (methodCall charAt ( (expressionList (expression (primary (literal (integerLiteral 2))))) ))))))) ;) }) (catchClause catch ( (catchType (qualifiedName Exception)) e ) (block { (blockStatement (statement (expression (expression (primary e)) . (methodCall printStackTrace ( ))) ;)) })))) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (statement try (block { (blockStatement (statement (expression (expression (primary y)) = (expression (expression (primary x)) / (expression (primary (literal (floatLiteral 1.0)))))) ;)) }) (catchClause catch ( (catchType (qualifiedName Exception)) e ) (block { (blockStatement (statement (expression (expression (primary e)) . (methodCall printStackTrace ( ))) ;)) })) (finallyBlock finally (block { (blockStatement (statement return ;)) })))) }))"); 

   //  c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId x) = (variableInitializer (expression (expression (primary Math)) . (methodCall max ( (expressionList (expression (primary y)) , (expression (primary z))) ))))))) ;) (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Map)) (variableDeclarators (variableDeclarator (variableDeclaratorId m) = (variableInitializer (expression new (creator (createdName HashMap) (classCreatorRest (arguments ( ))))))))) ;) (blockStatement (statement (expression (expression (primary m)) . (methodCall put ( (expressionList (expression (primary (literal \"a\"))) , (expression (primary x))) ))) ;)) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (statement assert (expression (primary ( (expression (expression (primary x)) < (expression (primary (literal (integerLiteral 10))))) ))) : (expression (primary (literal \"failure\"))) ;)) }))"); 

   // c.parseGeneralAST("(enumDeclaration enum Gender { (enumConstants (enumConstant male) , (enumConstant female) , (enumConstant other)) })"); 

     // c.parseGeneralAST("(methodDeclaration (typeTypeOrVoid (typeType (primitiveType int))) nextInt (formalParameters ( (formalParameterList (formalParameter (typeType (primitiveType int)) (variableDeclaratorId x)) , (formalParameter (typeType (primitiveType int)) (variableDeclaratorId y))) )) (methodBody (block { (blockStatement (statement return (expression (expression (expression (primary x)) + (expression (primary y))) - (expression (expression (primary x)) * (expression (primary y)))) ;)) })))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType double) [ ] [ ]) (variableDeclarators (variableDeclarator (variableDeclaratorId rr) = (variableInitializer (arrayInitializer { (variableInitializer (arrayInitializer { (variableInitializer (expression (primary (literal (floatLiteral 1.0))))) , (variableInitializer (expression (primary (literal (floatLiteral 3.0))))) })) , (variableInitializer (arrayInitializer { (variableInitializer (expression (primary (literal (floatLiteral 4.1))))) , (variableInitializer (expression (primary (literal (floatLiteral 5.9))))) })) }))))) ;) (blockStatement (statement (expression (expression (expression (expression (primary rr)) [ (expression (primary (literal (integerLiteral 1)))) ]) [ (expression (primary (literal (integerLiteral 2)))) ]) = (expression (primary (literal (floatLiteral 5.0))))) ;)) }))"); 


         // c.parseGeneralAST("(statement (block { (blockStatement (statement switch (parExpression ( (expression (primary v)) )) { (switchBlockStatementGroup (switchLabel case (expression (primary (literal true))) :) (blockStatement (statement (expression (expression (primary x)) = (expression (primary (literal (integerLiteral 1))))) ;)) (blockStatement (statement break ;))) (switchBlockStatementGroup (switchLabel case (expression (primary (literal false))) :) (blockStatement (statement (expression (expression (primary x)) = (expression (primary (literal (integerLiteral 2))))) ;)) (blockStatement (statement break ;))) (switchBlockStatementGroup (switchLabel default :) (blockStatement (statement (expression (expression (primary x)) = (expression (primary (literal (integerLiteral 3))))) ;))) })) }))"); 


      // c.parseGeneralAST("(statement (block { (blockStatement (statement while (parExpression ( (expression (expression (primary x)) > (expression (primary (literal (integerLiteral 0))))) )) (statement (block { (blockStatement (statement if (parExpression ( (expression (expression (primary x)) == (expression (primary (literal (integerLiteral 5))))) )) (statement (block { (blockStatement (statement break ;)) })) else (statement (block { (blockStatement (statement continue ;)) })))) })))) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType String)) (variableDeclarators (variableDeclarator (variableDeclaratorId x) = (variableInitializer (expression (primary (literal \"str\"))))))) ;) (blockStatement (statement (expression (expression (primary x)) = (expression (expression (primary x)) + (expression (primary (literal \"tail\"))))) ;)) (blockStatement (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId f) = (variableInitializer (expression (expression (primary x)) . (methodCall length ( ))))))) ;) }))");

      // c.parseGeneralAST("(statement (block { (blockStatement (statement throw (expression new (creator (createdName Exception) (classCreatorRest (arguments ( (expressionList (expression (primary (literal \"message\")))) ))))) ;)) }))");

      // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Map (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) , (typeArgument (typeType (classOrInterfaceType Integer))) >))) (variableDeclarators (variableDeclarator (variableDeclaratorId corr) = (variableInitializer (expression new (creator (createdName HashMap (typeArgumentsOrDiamond (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) , (typeArgument (typeType (classOrInterfaceType Integer))) >))) (classCreatorRest (arguments ( ))))))))) ;) }))");

      // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Vector)) (variableDeclarators (variableDeclarator (variableDeclaratorId r)))) ;) (blockStatement (statement (expression (expression (primary r)) = (expression new (creator (createdName Vector) (classCreatorRest (arguments ( )))))) ;)) }))"); 

   // c.parseGeneralAST("(statement (block { (blockStatement (statement (expression (expression (expression (primary this)) . x) = (expression (primary y))) ;)) }))"); 

    // c.parseGeneralAST("(typeType (classOrInterfaceType HashMap (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) , (typeArgument (typeType (classOrInterfaceType Integer))) >)))"); 

   // "(typeType (classOrInterfaceType List (typeArguments < (typeArgument (typeType (primitiveType byte))) >)))"); 

    // (typeType (classOrInterfaceType Integer) [ ])"); 

    // (expression (expression (primary line1)) . (methodCall length ( )) )"); 

  // (methodCall put ( (expressionList (expression (primary (literal \"a\"))) , (expression (primary x))) ))"); 

  // (methodCall max ( (expressionList (expression (primary y)) , (expression (primary z))) ) )"); 

    System.out.println(xx); 
    

    String tt = xx.cg(cgs); 
    System.out.println(tt); 

    System.out.println(); 

    System.out.println(xx.toKM3()); 

   // c = new Compiler2(); 

   // ASTTerm yy = c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Map (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) , (typeArgument (typeType (classOrInterfaceType Integer))) >))) (variableDeclarators (variableDeclarator (variableDeclaratorId corr) = (variableInitializer (expression new (creator (createdName HashMap (typeArgumentsOrDiamond (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) , (typeArgument (typeType (classOrInterfaceType Integer))) >))) (classCreatorRest (arguments ( ))))))))) ;) }))"); 

   // "(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType List (typeArguments < (typeArgument (typeType (primitiveType byte))) >))) (variableDeclarators (variableDeclarator (variableDeclaratorId rr)))) ;) (blockStatement (statement (expression (expression (primary rr)) = (expression new (creator (createdName ArrayList (typeArgumentsOrDiamond (typeArguments < (typeArgument (typeType (primitiveType byte))) >))) (classCreatorRest (arguments ( )))))) ;)) }))"); 


    // "(statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Vector)) (variableDeclarators (variableDeclarator (variableDeclaratorId r)))) ;) (blockStatement (statement (expression (expression (primary r)) = (expression new (creator (createdName Vector) (classCreatorRest (arguments ( )))))) ;)) }))"); 

    // System.out.println(yy); 
    // System.out.println(yy.toKM3()); 

  // (statement (block { (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType List (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) >))) (variableDeclarators (variableDeclarator (variableDeclaratorId rr)))) ;) (blockStatement (statement (expression (expression (primary rr)) = (expression new (creator (createdName ArrayList (typeArgumentsOrDiamond (typeArguments < (typeArgument (typeType (classOrInterfaceType String))) >))) (classCreatorRest (arguments ( )))))) ;)) }))

     // c.parseGeneralAST("(statement (block { (blockStatement (localVariableDeclaration (typeType (primitiveType int)) (variableDeclarators (variableDeclarator (variableDeclaratorId x) = (variableInitializer (expression (expression (primary Math)) . (methodCall max ( (expressionList (expression (primary y)) , (expression (primary z))) ))))))) ;) (blockStatement (localVariableDeclaration (typeType (classOrInterfaceType Map)) (variableDeclarators (variableDeclarator (variableDeclaratorId m) = (variableInitializer (expression new (creator (createdName HashMap) (classCreatorRest (arguments ( ))))))))) ;) (blockStatement (statement (expression (expression (primary m)) . (methodCall put ( (expressionList (expression (primary (literal \"a\"))) , (expression (primary x))) ))) ;)) }))"); 

     // c.parseGeneralAST("(statement (block { (blockStatement (statement assert (expression (primary ( (expression (expression (primary x)) < (expression (primary (literal (integerLiteral 10))))) ))) : (expression (primary (literal \"failure\"))) ;)) }))"); 

   //  c.parseGeneralAST("(enumDeclaration enum Gender { (enumConstants (enumConstant male) , (enumConstant female) , (enumConstant other)) })"); 


                 */ 
	
	
	/* java.util.Date d1 = new java.util.Date(); 
	long t1 = d1.getTime(); 

     Vector background = Thesarus.loadThesaurus("output/background.txt");
	 System.out.println(">>> Background information assumed: " + background); 
	  
     File infile = new File("output/nlpout.txt");
     BufferedReader br = null;
     Vector res = new Vector();
     String s;
     boolean eof = false;
    

     try
     { br = new BufferedReader(new FileReader(infile)); }
     catch (FileNotFoundException e)
     { System.out.println("File not found: " + infile.getName());
       return; 
     }


     String xmlstring = ""; 
     int linecount = 0; 
     boolean flag = false; 
     Vector sentences = new Vector(); 
	 
     while (!eof)
     { try { s = br.readLine(); }
       catch (IOException e)
       { System.out.println("Reading failed.");
         return; 
       }
	   
       if (s == null) 
       { eof = true; 
         break; 
       }
       else if (s.startsWith("Constituency parse:"))
       { flag = true; }
       else if (s.startsWith("Dependency Parse (enhanced plus plus dependencies):"))
       { flag = false; 
         sentences.add(xmlstring); 
         System.out.println("Read: " + xmlstring); 
	    xmlstring = ""; 
	  }
	  else if (flag) 
       { xmlstring = xmlstring + s + " "; } 
       linecount++; 
     } // replace ' and " in s by harmless characters. Remove - within a string or number. 
	 
	 Vector nlpsentences = new Vector(); 
     Vector mes = new Vector(); // entities and usecases from the model.
	  
     String km3model = ""; 
     for (int i = 0; i < sentences.size(); i++) 
     { String xstring = (String) sentences.get(i); 
       Compiler2 c0 = new Compiler2(); 
       c0.nospacelexicalanalysisText(xstring); 
       NLPSentence xres = c0.parseNLP();
       if (xres != null) 
       { xres.indexing(); 
	     xres.setId("" + (i+1)); 
		 xres.linkToPhrases(); 
		 
	     nlpsentences.add(xres); 
         System.out.println(">>> Sentence " + (i+1) + ": " + xres); 
         java.util.Map classifications = xres.classifyWords(background,mes); 
         System.out.println(">>> Using word classifications >>> " + classifications);
         km3model = xres.getKM3(mes,classifications); 
         System.out.println(); 
       }  
     } 	 
	     
    String outfile = "mm.km3"; 
    File appout = new File("output/" + outfile); 
    try
    { PrintWriter appfile = new PrintWriter(
                                new BufferedWriter(new FileWriter(appout)));
      
      appfile.println("package app {\n" + km3model + "\n}\n"); 
      appfile.close(); 
    }
    catch(Exception _dd) { }
	
	for (int i = 0; i < nlpsentences.size(); i++) 
	{ NLPSentence ss = (NLPSentence) nlpsentences.get(i); 
	  System.out.println(">>> Sentence " + (i+1)); 
	  System.out.println(">>> Derived elements: " + ss.derivedElements); 
	  System.out.println(); 
	}
	
	java.util.Date d2 = new java.util.Date(); 
	long t2 = d2.getTime(); 
	System.out.println(">>> Time taken = " + (t2-t1)); 
	
	// Compiler2 cx = new Compiler2(); 
	// cx.nospacelexicalanalysis("var f : Function(String,int)");
	// cx.nospacelexicalanalysis("findRoot(st, en, lambda x : double in (x*x - x))");
	// cx.nospacelexicalanalysis("Function(double,double)");  
	// int en = cx.lexicals.size()-1; 
	// System.out.println(cx.showLexicals(0,en));
	
	// cx.nospacelexicalanalysis("reference _1 : _2; |-->  var _1 : _2 = _2()\n<when> _2 collection"); 
	// System.out.println(cx.showLexicals(0,cx.lexicals.size()-1));  
    // CGRule r = cx.parse_ExpressionCodegenerationrule("Map{_1} |-->Ocl.initialiseMap(_1)"); 
	// Expression r = cx.parse_lambda_expression(0,0,en,new Vector(),new Vector());
	// Type r = cx.parseType();  
	// System.out.println(r);  
 	 
	 // try 
	 // { infile.close(); }
	 // catch (Exception _fx) { } 
	 
     // c0.nospacelexicalanalysis("$act(m) <= (now - settlement)/frequency"); 
	 // c0.nospacelexicalanalysis("result = (if (umlKind = classid) then addClassIdReference(x) else if (umlKind = value) then addValueReference(x) else addBEReference(x) endif endif)"); 
	 /* c0.nospacelexicalanalysis("(S (NP (NNS Bonds)) (VP (VBP have) (NP (NP (DT a) (JJ unique) (NN name)) (, ,) (NP (DT a) (JJ realvalued) (NN term)))))"); 
	 System.out.println(c0.lexicals); 
	 int n = c0.lexicals.size(); 
     // Expression expr = c0.parseExpression();
     NLPSentence sent = c0.parseSentence(0,n-1);
	 System.out.println(sent); 
	 System.out.println(sent.isSVO());  
     System.out.println(sent.isClassDefinition());  
     Vector elems = sent.modelElements();
	 
	 for (int i = 0; i < elems.size(); i++) 
	 { Entity ent = (Entity) elems.get(i); 
	   System.out.println(ent.getKM3()); 
	 } */ 
	  
	 
     /* Statement stat = c0.parseStatement(new Vector(), new Vector()); 
	 System.out.println(stat);
     c0.checkBrackets(); 
	 
	  Compiler2 c1 = new Compiler2(); 
	 c1.nospacelexicalanalysis("name <: \"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ -,.\"");
	 Expression expr = c1.parseExpression(); 
	 System.out.println(expr); */  
	 
      // c0.nospacelexicalanalysis("m->restrict(Set{1})");
      // c0.nospacelexicalanalysis("For_IN each_DT instance_NN of_IN the_DT class_NN Member_NNP in_IN the_DT IN_IN model_NN ,_, create_VBP an_DT instance_NN in_IN the_DT OUT_NNP model_NN"); 
	  // c0.nospacelexicalanalysis("The_DT Main_NNP rule_NN generates_VBZ a_DT PetriNet_NNP element_NN from_IN the_DT input_NN PathExp_NN element_NN ._."); 
	  // c0.nospacelexicalanalysis("A_DT singlevalued_JJ attribute_NN is_VBZ mapped_VBN to_TO a_DT column_NN ._."); 
      // c0.nospacelexicalanalysis("Each_DT class_NN is_VBZ mapped_VBN to_TO a_DT table_NN with_IN a_DT primary_JJ key_NN ._."); 
      // System.out.println(c0.lexicals); 
	   
      // int en = c0.lexicals.size(); 
      // Vector vv = c0.parseRequirementsText(0,en-1); 
	  
	  // c0.nospacelexicalanalysis("usecase uc { includes uc1; extends uc2; parameter x : int; attribute att : long; parameter y : double; precondition y > 0 & x >= 0; void:: true => 5->display() activity: y := y + x }"); 

      // UseCase xx = c0.parseKM3UseCase(new Vector(), new Vector(), new Vector(), new Vector()); 
	  	 
      // CGRule r1 = c0.parse_ExpressionCodegenerationrule("_1 + _2 |--> String(_1) + _2 <when>  _1 numeric, _2 String");  
    
	  // System.out.println(xx.display());
	  // System.out.println(xx.extendsList); 
	  // System.out.println(xx.includesList); 
	   
	  // System.out.println(r1.conditions);
	  // System.out.println(r1.variables);  
	  
      // System.out.println("" + parse_conditions("_1 String, _2 numeric"));  
      // System.out.println(((BinaryExpression) e).getRight().needsBracket); 

      // e.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 
      // String str = e.queryForm(new java.util.HashMap(), true); 
      // System.out.println(str); 

      /* Compiler2 c1 = new Compiler2(); 
      c1.nospacelexicalanalysis("m->keys()"); 
      int ex = c1.lexicals.size(); 
      Expression eee = c1.parseExpression(); 
      System.out.println(eee); 
      eee.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 
      System.out.println(eee.queryFormCSharp(new java.util.HashMap(), true)); */ 

  }  

    /* 
    CGRule r0 = c0.parse_TypeCodegenerationrule("Sequence(_1) |--> ArrayList<_1>"); 

    Compiler2 c = new Compiler2();
    Compiler2 c1 = new Compiler2();
    Compiler2 c2 = new Compiler2();
    CGRule r1 = c1.parse_ExpressionCodegenerationrule("_1[_2] |--> (_1).get(_2 - 1) <when> _2 numeric");  
    CGRule r2 = c.parse_ExpressionCodegenerationrule("_1._2(_3) |--> _1._2(_3)"); 
    CGRule r3 = c2.parse_ExpressionCodegenerationrule("_1._2 |--> _1.get_2()"); 
    // c2.nospacelexicalanalysis("_1->pow(_2)"); 
    // Expression e3 = c2.parse(); 
    System.out.println("rule0 = " + r0 + " variables= " + r0.variables);
    System.out.println("rule1 = " + r1 + " variables= " + r1.variables);
    System.out.println("rule2 = " + r2 + " variables= " + r2.variables);
    System.out.println("rule3 = " + r3 + " variables= " + r3.variables);
    // System.out.println(e3); 

    CGSpec cgs = new CGSpec(); 
    cgs.addBasicExpressionRule(r1); 
    cgs.addBasicExpressionRule(r2); 
    cgs.addBasicExpressionRule(r3); 
    Compiler2 cc = new Compiler2(); 
    cc.nospacelexicalanalysis("a.b[3]"); 
    Expression ff = cc.parse(); 
    System.out.println(ff.cg(cgs)); */ 

    // c.nospacelexicalanalysis("a[i].r.op(5) + a.op1(x).op2(y)"); 
    // c.nospacelexicalanalysis("package pp { class dd { } class ff { attribute pp : double; } class cc { attribute aa : int ; attribute bb : double; } }"); 
    // c.displaylexs(); 
    // Vector rr = c.parseKM3(new Vector(), new Vector(), new Vector(), new Vector());
    // System.out.println(rr);
    /* for (int y = 0; y < rr.size(); y++) 
    { Entity e = (Entity) rr.get(y); 
      System.out.println(e.getName()); 
      Vector vv = e.getAttributes();
      for (int i = 0; i < vv.size(); i++) 
      { System.out.println(vv.get(i)); }    
    } */ 
    
    // c.nospacelexicalanalysis("s->first");
    // c.nospacelexicalanalysis("(isStatic = true & (isCached = false or parameters.size /= 1) => result = (Statement.tab(indent) + \"def \" + name + \"(\" + Expression.tolist(parameters.name) + \") :\\n\" + activity.toPython(indent + 2) + \"\\n\"))");  
    // c.nospacelexicalanalysis("(isStatic = true & isCached = true & parameters.size = 1 => result =(Statement.tab(indent) + \"def \" + name + \"(\" + parameters.first.name + \") :\\n\" + Statement.tab(indent+2) + \"if str(\" + parameters.first.name + \") in \" + owner.name + \".\" + name + \"_cache :\\n\" + Statement.tab(indent + 4) + \"return \" + owner.name + \".\" + name + \"_cache[str(\" + parameters.first.name + \")]\\n\" + Statement.tab(indent+2) + \"result = \" + name + \"_uncached(\" + parameters.first.name + \")\\n\" + Statement.tab(indent+2) + owner.name + \".\" + name + \"_cache[str(\" + parameters.first.name + \")] = result\\n\" + Statement.tab(indent+2) + \"return result\\n\\n\") + (Statement.tab(indent) + \"def \" + name + \"_uncached(\" + Expression.tolist(parameters.name) + \") :\\n\" + activity.toPython(indent + 2) + \"\\n\"))"); 

    // c.nospacelexicalanalysis("(Statement.tab(indent) + \"def \" + name + \"(\" + parameters[1].name + \") :\")->display()"); 

    // c.nospacelexicalanalysis("(Statement.tab(indent+2) + \"if str(\" + parameters[1].name + \") in \" + owner.name + \".\" + name + \"_cache :\")->display()"); 
    // c.nospacelexicalanalysis("(Statement.tab(indent + 4) + \"return \" + owner.name + \".\" + name + \"_cache[str(\" + parameters[1].name + \")]\")->display()"); 
    // c.nospacelexicalanalysis("(Statement.tab(indent+2) + \"result = \" + name + \"_uncached(\" + parameters[1].name + \")\")->display()"); 
    // c.nospacelexicalanalysis("(Statement.tab(indent+2) + owner.name + \".\" + name + \"_cache[str(\" + parameters[1].name + \")] = result\")->display()"); 
    // c.nospacelexicalanalysis("(Statement.tab(indent+2) + \"return result\")->display() & (Statement.tab(indent) + \"def \" + name + \"_uncached(\" + Expression.tolist(parameters.name) + \") :\")->display() & activity.toPython(indent + 2)->display()");
   // c.nospacelexicalanalysis("x@pre[att@pre].f@pre");
   // Expression ee = c.parse(); 
   // System.out.println(ee); 
 
  //  c.nospacelexicalanalysis("incr() pre: true post: Ucwithops.att = 10 & \"test\"->display()"); 

//  ; d : Sequence ; d := Integer.subrange(0,m)->collect( i | Sequence{} )  ;  for i : Integer.subrange(0,m) do ( d[( i + 1 )] := Integer.subrange(0,n)->collect( j | 0 ) ; sq : Sequence ; sq := d->at(i+1) ; sq[0] := i ) ; sq1 : Sequence ; sq1 := d->at(1) ; for j : Integer.subrange(0,n) do sq1[(j+1)] := j ; for j : Integer.subrange(1,n) do for i : Integer.subrange(1,m) do (sqi : Sequence ; sqi := d->at(i+1) ; if s[i] = t[i] then sqi[(j+1)] := (d->at(i))->at(j) else sqi[(j+1)] := Set{ ((d->at(i))->at(j+1)+1), ((d->at(i+1))->at(j)+1), ((d->at(i))->at(j)+1) }->min() ) ; return (d->at(n+1))->at(m+1)");
 
   // c.displaylexs();
   // BehaviouralFeature e = c.operationDefinition(new Vector(), new Vector());  
   // System.out.println("Parsed: " + e.display()); 
  // } 

  /* 
    // USEMetaModel xn = c.parseUSEMetaModel();
    // c.nospacelexicalanalysis("module m; create OUT : T from IN : S; rule r { from x : S1, y : S2 ( x.a > 0 ) to p : ET ( d <- 4, h <- x.p ) } rule rr { from x : S2 ( x.b = one ) to k : ET2 ( y <- x.cc ) do { r <- 5; } }");
    // c.nospacelexicalanalysis("for ( v in c.specialization.specific.ownedAttribute ) { if (v.name = a.name) { v->isDeleted() ; } else { skip; } } "); 
    // c.nospacelexicalanalysis("if ( Entity->includes(att.type) ) { t.tcols->includes( att.makeForeignKeyColumnop() ) ; } else { skip; }"); 
    // c.nospacelexicalanalysis("result = \"Couple avgRating \" + avgRating + \", \" + commonMovies.size + \" movies (\" + (p1.name.sum + \",\" + p2.name.sum) + \")\""); 
    /* c.nospacelexicalanalysis("module m; pre pre1 { x := y } rule rr transform x : e1 to y : e2 { guard: x > 0 y := x } post post1 { z := w + 7 }"); 
    Entity e1 = new Entity("e1"); 
    Entity e2 = new Entity("e2");
    Vector ents = new Vector(); 
    ents.add(e1); ents.add(e2);  
    c.displaylexs(); 
    EtlModule em = c.parseEtl(ents,new Vector()); 
    System.out.println(em); 
    // Statement st = c.parseATLStatement(); 
    // c.nospacelexicalanalysis("a + b->collect( x | c + x.op() )->max() + d"); 
    // c.nospacelexicalanalysis("result = Integer.Sum(1, maxfails(k,s), mk, (sectors[k].mu * mk * sectors[k].L * PCond(k,mk) * PS(s - mk * sectors[k].L)))"); 
    // Expression exp = c.parse();
    // FlockModule m = c.parseFlockModule();  
    // System.out.println(exp);  
    // e1.typeCheck(new Vector(), new Vector(), new Vector());
    // String qf = e1.queryForm(new java.util.HashMap(), true); 
    // System.out.println(mr);  
    // System.out.println(mr.getInterpretation()); 
    // c.nospacelexicalanalysis("It is necessary that each integer type instance t maps to a CPrimitiveType instance c such that c.ctypeId = t.typeId & c.name = \"int\" ; Each Type is considered to be an integer type instance if it has name = \"int\""); 

  /*   c.nospacelexicalanalysis("nmethods = UMLClass[ci].features->select( f | f : UMLMethod )->size() & natts = UMLClass[ci].features->select( f | f : Attribute )->size() & (nmethods <= 1  => result = 0) & (nmethods > 1 & natts = 0 => result = mmi(ci,ci)/(1.0*nmethods*(nmethods-1))) & (natts /= 0 & nmethods = 1 => result = mai(ci,ci)/(1.0*natts)) & (nmethods > 1 & natts /= 0 => result = (mai(ci,ci)/(1.0*nmethods*natts) + mmi(ci,ci)/(1.0*nmethods*(nmethods-1))))");
  
    &
    (natts != 0 & nmethods = 1 => 
       result = mai(ci,ci)/(1.0*natts))"); 

    // c.nospacelexicalanalysis("1.0E+100"); 
    // c.displaylexs();
    // c.nospacelexicalanalysis("execute op() ; while E do execute op1() & op2() & op3()");  
    // Statement exp = c.parseStatement();

    c.nospacelexicalanalysis("(a => b) & (c => d) & (e => f) & (g => h) & (i => j) & (k => l)"); 
    Expression exp = c.parse(); 
    System.out.println(Expression.caselist(exp));  
  } 

    /* c.lexicalanalysis("( kind = initial => n : InitialNode # n.name1 = name ) & ( kind = join => n : JoinNode # n.name1 = name ) & ( kind = fork => n : ForkNode # n.name1 = name ) & ( kind = junction => ( incoming.size = 1 => n : DecisionNode # n.name1 = name ) & ( incoming.size > 1 => n : MergeNode # n.name1 = name ) )");  
    c.lexicalanalysis("( ( source : ObjectFlowState or target : ObjectFlowState ) => ( f : ObjectFlow ) # ( f.name1 = name & f.source = ActivityNode[source.name] & f.target = ActivityNode[target.name] & f.guard = OpaqueExpression[guard.name] ) ) & ( ( source /: ObjectFlowState & target /: ObjectFlowState ) => ( f : ControlFlow ) # ( f.name1 = name & f.source = ActivityNode[source.name] & f.target = ActivityNode[target.name] & f.guard = OpaqueExpression[guard.name] ) )"); 

      c.lexicalanalysis("ag : ActivityGraph & ag.name = \"ag\" & is : Pseudostate & is.name = \"is\" & is.kind = initial & fs : FinalState & fs.name = \"fs\" & as1 : ActionState & as1.name = \"Request service\" & as2 : ActionState & as2.name = \"Pay\" & as3 : ActionState & as3.name = \"Collect order\" & as4 : ActionState & as4.name = \"Take order\" & as5 : ActionState & as5.name = \"Deliver order\" & as6 : SimpleState & as6.name = \"Restock\" & as7 : ActionState & as7.name = \"Fill order\" & t : Type & of1 : ObjectFlowState & of1.name = \"Placed Order\" & of1.type = t & of2 : ObjectFlowState & of2.name = \"Entered Order\" & of2.type = t & of3 : ObjectFlowState & of3.name = \"Filled Order\" & of3.type = t & of4 : ObjectFlowState & of4.name = \"Delivered Order\" & of4.type = t & ds1 : Pseudostate & ds1.name = "ds1" & ds1.kind = junction & ds2 : Pseudostate & ds2.name = \"ds2\" & ds2.kind = junction & dc1 : Pseudostate & dc1.name = \"dc1\" & dc1.type = fork & dc2 : Pseudostate & dc2.name = \"dc2\" & dc2.type = join & be1 : BooleanExpression & be1.language = \"text\" & be1.body = \"in stock\" & g1 : Guard & g1.name = \"g1\" & g1.expression = be1 & be2 : BooleanExpression & be2.language = \"text\" & be2.body = \"not in stock\" & g2 : Guard & g2.name = \"g2\" & g2.expression = be2 & t0 : Transition & t0.name = \"t0\" & t0.source = is & t0.target = as1 & t1 : Transition & t1.name = \"t1\" & t1.source = as1 & t1.target = dc1 & t2 : Transition & t2.name = \"t2\" & t2.source = dc1 & t2.target = as2 & t3 : Transition & t3.name = \"t3\" & t3.source = as2 & t3.target = dc2 & t4 : Transition & t4.name = \"t4\" & t4.source = dc1 & t4.target = of1 & t5 : Transition & t5.name = \"t5\" & t5.source = of1 & t5.target = as4 & t6 : Transition & t6.name = \"t6\" & t6.source = as4 & t6.target = of2 & t7 : Transition & t7.name = \"t7\" & t7.source = of2 & t7.target = ds1 & t8 : Transition & t8.name = \"t8\" & t8.source = ds1 & t8.target = ds2 & t8.guard = {g1} & t9 : Transition & t9.name = \"t9\" & t9.source = ds1 & t9.target = as6 & t9.guard = {g2} & tr : Event & t10 : Transition & t10.name = \"t10\" & t10.source = as6 & t10.target = ds2 & t10.trigger = {tr} & t11 : Transition & t11.name = \"t11\" & t11.source = ds2 & t11.target = as7 & t12 : Transition & t12.name = \"t12\" & t12.source = as7 & t12.target = of3 & t13 : Transition & t13.name = \"t13\" & t13.source = of3 & t13.target = dc2 & t14 : Transition & t14.name = \"t14\" & t14.source = dc2 & t14.target = as5 & t15 : Transition & t15.name = \"t15\" & t15.source = as5 & t15.target = of4 & t16 : Transition & t16.name = \"t16\" & t16.source = of4 & t16.target = as3 & t17 : Transition & t17.name = \"t17\" & t17.source = as3 & t17.target = fs & cs : CompositeState & cs.name = \"cs\" & cs.subvertex = {is,fs,as1,as2,as3,as4,as5,as6,as7,of1,of2,of3,of4,ds1,ds2,dc1,dc2} & ag.top = cs & ag.transitions = {t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17} & p1 : Partition & p1.name = \"p1\" & p1.contents = {as1,t1,dc1,t4,t2,as2,t16,as3} & p2 : Partition & p2.name = \"p2\" & p2.contents = {t5,as4,t6,t13,dc2,t14,as5,t15} & p3 : Partition & p3.name = \"p3\" & p3.contents =  {t7,ds1,t8,t9,as6,as7,ds2,t10,t11,t12} & ag.partition = {p1,p2,p3}"); 


    Expression ss = c.parse(); 
    System.out.println(ss); 

    c.nospacelexicalanalysis("n*m-1"); 
    System.out.println(c.parse()); 
  }  */ 

/* public class ForStatement extends Statement
{ Expression test;
  Statement body;

  public ForStatement(Expression t, Statement b)
  { test = t; body = b; }
} */ 

  /* public static void main(String[] args)
  { Compiler c = new Compiler(); 
    // c.lexicalanalysis("( x * y ) + ( ( 3 / 2 ) + 4 )"); 
    c.lexicalanalysis("( x : B ) # ( x.name = \"felix\" & x.y = 2 )"); 
    System.out.println(c.parse()); 

    c.lexicalanalysis("xx + \"ui \n oop\" + \" \" "); 
    System.out.println(c.parse()); 
  } */ 
}

