import java.io.*; 
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JOptionPane; 
import javax.swing.JTextArea; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
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

  package: Utilities
*/ 

public class Compiler2
{ static final int INUNKNOWN = 0; 
  static final int INBASICEXP = 1; 
  static final int INSYMBOL = 2; 
  static final int INSTRING = 3; 

  Vector lexicals; // of StringBuffer
  Vector lexs = new Vector(); // of String 
  int[] bcount; // bracket count at this point in the lexical list
  Vector ops = new Vector(); // of OpOccurrence
  Expression finalexpression; 


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
      { System.err.println("Clone: " + str + " at " + i); 
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
            { clones.add(">>CLONE at " + i + "," + j + " SIZE= " + k + ">> " + str);  
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
      { clones.add(">>CLONE at " + i + "," + j + " SIZE= " + k + ">> " + str);  
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
  { if (str.equals("exists") || str.equals("existsLC") || str.equals("exists1") || 
        str.equals("forAll") || str.equals("allInstances") ||
        str.equals("select") || str.equals("collect") || str.equals("reject") ||
        str.equals("includes") || str.equals("including") || str.equals("excludes") ||
        str.equals("excluding") || str.equals("intersection") || str.equals("union") ||
        str.equals("unionAll") || str.equals("intersectAll") || str.equals("at") ||
        str.equals("selectMaximals") || str.equals("selectMinimals") || str.equals("not") ||
        str.equals("any") || str.equals("size") || str.equals("last") ||
        str.equals("first") || str.equals("includesAll") || str.equals("excludesAll") ||
        str.equals("append") || str.equals("prepend") || str.equals("min") ||
        str.equals("max") || str.equals("sum") || str.equals("display") ||
        str.equals("before") || str.equals("after") || str.equals("xor") ||
        str.equals("toLowerCase") || str.equals("toUpperCase") || str.equals("Sum") ||
        str.equals("Prd") || str.equals("symmetricDifference") || str.equals("oclIsUndefined") ||
        str.equals("reverse") || str.equals("sort") || str.equals("subcollections") || 
        str.equals("front") || str.equals("tail") || str.equals("insertAt") || 
        str.equals("subrange") || str.equals("characters") || str.equals("isLong") || 
        str.equals("closure") || str.equals("asSet") || str.equals("asSequence") || 
        str.equals("sqr") || str.equals("floor") || str.equals("ceil") ||
        str.equals("round") || str.equals("exp") || str.equals("pow") ||
        str.equals("sin") || str.equals("cos") || str.equals("tan") || str.equals("toLong") ||
        str.equals("asin") || str.equals("acos") || str.equals("atan") ||
        str.equals("sinh") || str.equals("cosh") || str.equals("tanh") ||
        str.equals("log10") || str.equals("cbrt") || str.equals("isInteger") ||
        str.equals("toInteger") || str.equals("isReal") || str.equals("toReal") ||
        str.equals("log") || str.equals("count") || str.equals("hasPrefix") ||
        str.equals("hasSuffix") || str.equals("isEmpty") || str.equals("notEmpty") ||
        str.equals("isUnique") || str.equals("prd") || str.equals("sortedBy") || 
        str.equals("sqrt") || str.equals("abs") || "flatten".equals(str) || 
        str.equals("oclAsType") || str.equals("oclIsKindOf") || str.equals("or") ||
        str.equals("indexOf") || str.equals("isDeleted") || str.equals("iterate"))
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
  { if (Expression.alloperators.contains(tok) || "[".equals(tok) || "]".equals(tok) ||
        "{".equals(tok) || "}".equals(tok) || "Set{".equals(tok) || "Sequence{".equals(tok) ||
        "Set".equals(tok) || "Sequence".equals(tok) || "Integer".equals(tok) ||
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
    if (variables.contains(str) || "Integer".equals(str) || str.equals("Real"))
    { return true; } 
    if (context != null && context.hasFeatureOrOperation(str))
    { return true; }
    if (isKeyword(str))
    { messages.add("Error: " + str + " invalid identifier (keyword)");
      JOptionPane.showMessageDialog(null, "Error: " + str + " invalid identifier (keyword)",
                                    "Syntax error", JOptionPane.ERROR_MESSAGE);  
      return false; 
    }
    if (invalidLexical(str))
    { messages.add("Error: " + str + " invalid lexical");
      JOptionPane.showMessageDialog(null, "Error: " + str + " invalid lexical",
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
  { if ("(".equals(str) || ")".equals(str) || "[".equals(str) || "]".equals(str) ||
        "{".equals(str) || "}".equals(str) || Expression.alloperators.contains(str) || 
        ",".equals(str) || 
        "%".equals(str) || "_".equals(str) || "?".equals(str) || "~".equals(str) ) 
    { return true; } 
    return false; 
  } 


  public boolean isKeyword(String str) 
  { if (str.equals("class") || str.equals("if") || str.equals("while") ||
        str.equals("return") || str.equals("break") || str.equals("continue") || 
        str.equals("float") || str.equals("char") || str.equals("byte") || 
        // str.equals("boolean") || str.equals("int") || 
        // str.equals("long") || str.equals("double") ||  
        str.equals("transient") || str.equals("volatile") || str.equals("short") ||
        str.equals("native") || str.equals("enum") || str.equals("package") ||
        str.equals("strictfp") || str.equals("wait") || str.equals("goto") || 
        str.equals("const") || str.equals("notify") || str.equals("notifyAll") || 
        str.equals("case") || str.equals("switch") || str.equals("this") || str.equals("null") ||
        str.equals("new") || str.equals("try") || str.equals("catch") || str.equals("finally") ||
        str.equals("synchronized") || str.equals("until") || str.equals("do") || 
        str.equals("interface") || str.equals("extends") || str.equals("implements") ||
        str.equals("for") || str.equals("instanceof") || str.equals("private") || 
        str.equals("public") || str.equals("final") || str.equals("static") ||
        str.equals("void") || str.equals("abstract") || str.equals("protected") ||
        str.equals("else") || str.equals("throw") || str.equals("throws"))
    { return true; }  // Java keywords. "super" is allowed. 
    return false; 
  } 

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
  { return (c == '<' || c == '>' || c == '=' || c == '*' || c == '/' || c == '\\' ||
            c == '-' || c == ',' || c == ';' || c == '!' ||
            c == '+' || c == '&' || c == '^' || c == ':' || c == '|' ||
            c == '(' || c == ')' || c == '{' || c == '}' || c == '[' ||
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
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (isBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the expression
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }           
        else if (isStringDelimiter(c))
        { sb = new StringBuffer();     // start new buffer for the string
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
          { System.err.println("Unrecognised literal: " + c); } 
        }
      } 
      else if (in == INBASICEXP)
      { if (isBasicExpCharacter(c) || c == '"')  // Why allow " in a basic exp???
        { sb.append(c); }              // carry on adding to current basic exp
        else if (c == '_') 
        { // System.out.println("Metavariable symbol: " + c); 
          sb.append(c); 
        } 
        else if (isSymbolCharacter(c))
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
      { if (isStringDelimiter(c))
        { sb = new StringBuffer();     // start new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append('"'); 
        }
        else if (c == '(' || c == ')')
        { sb = new StringBuffer();     // start new buffer for the new symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          previous = c; 
          sb.append(c); 
        }
        else if (c == '_') 
        { // System.out.println("Metavariable symbol: " + c); 
          sb = new StringBuffer();     // start new buffer for the string
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
    // System.out.println(ops);        
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
        ("int".equals(typ) || "long".equals(typ) || typ.equals("String") || 
         "Integer".equals(typ) || "Real".equals(typ) || typ.equals("Boolean") || 
         "void".equals(typ) || typ.equals("double") || typ.equals("boolean") ||
         typ.startsWith("_")))
    { System.out.println("Creating new basic type " + typ);
      return new Type(typ,null);
    }
    else if ("Map".equals(typ))  // for ATL
    { Type tt = null; 
      for (int i = st + 2; i < en; i++) 
      { String ss = lexicals.get(i) + ""; 
        if (ss.equals(","))
        { Type t1 = parseType(st+2,i-1,entities,types); 
          Type t2 = parseType(i+1,en-1,entities,types);
          if (t1 != null && t2 != null) 
          { tt = new Type("Map",t1,t2);
            tt.setKeyType(t1);  
            tt.setElementType(t2); 
            return tt; 
          } 
        }
      } 
      if (tt == null) 
      { System.err.println("******** Invalid map type: " + showLexicals(st,en)); 
        return null; 
      }
      else 
      { return tt; } 
    }    
    else if (typ.equals("Set") || typ.equals("Sequence"))
    { Type tt = new Type(typ,null); 
      Type et = parseType(st + 2, en - 1, entities, types); 
      if (et != null && tt != null) 
      { tt.setElementType(et); } 
      return tt; 
    } 
    else
    { Entity ee = (Entity) ModelElement.lookupByName(typ,entities);
      if (ee != null) 
      { return new Type(ee); } 
      else 
      { return (Type) ModelElement.lookupByName(typ,types); } 
    } 
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
        Expression exp1 = parse_expression(0, i+10, en); 
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
          Expression exp1 = parse_expression(0, i+3, en); 
          sb.setCondition(exp1);     
        } 
      }
    } 
  } // should pair up the defined terms uses and definitions. 

  public Expression parse(JTextArea messageArea) 
  { messageArea.append("LEXICALS: " + lexicals + "\n\r"); 
    Expression ee = parse_expression(0,0,lexicals.size()-1); 
    finalexpression = ee;
    if (ee == null) 
    { messageArea.append("Not a valid OCL constraint. Trying to parse as an operation.\n\r"); } 
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
              Vector conds = parseCGconditions(conditions); 
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
              Vector conds = parseCGconditions(conditions); 
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
              Vector conds = parseCGconditions(conditions); 
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
        System.out.println("LHS of type rule = " + lhsexp); 

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
              Vector conds = parseCGconditions(conditions); 
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
              Vector conds = parseCGconditions(conditions); 
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

  public CGRule parse_TextCodegenerationrule(String rule)
  { String buff = ""; 
    for (int i = 0; i < rule.length(); i++) 
    { char c = rule.charAt(i); 
      if (c == '|' && i + 2 < rule.length() && rule.charAt(i+1) == '-' && 
          rule.charAt(i+2) == '-' && rule.charAt(i+3) == '>') 
      { String lhs = rule.substring(0,i); 
        
        Vector variables = new Vector(); 
        for (int k = 0; k + 1 < lhs.length(); k++) 
        { char lex = lhs.charAt(k); 
          if (lex == '_')
          { variables.add("_" + lhs.charAt(k+1)); } 
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
              Vector conds = parseCGconditions(conditions); 
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
              Vector conds = parseCGconditions(conditions); 
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
              Vector conds = parseCGconditions(conditions); 
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
        Expression lhs = parse_expression(bcount,pstart,i-1); 
        Expression rhs = null; 
        System.out.println("LHS = " + rhs); 

        for (int j = i+2; j <= pend; j++) 
        { String lxj = lexicals.get(j) + ""; 
          if ("<".equals(lxj) && j+1 < pend && "when".equals(lexicals.get(j+1) + "") && 
              ">".equals(lexicals.get(j+2) + "")) 
          { rhs = parse_expression(bcount,i+2,j-1); 
            System.out.println("RHS = " + rhs); 
          } 
        } 
        if (rhs == null) // no <when> 
        { rhs = parse_expression(bcount,i+2,pend); 
          System.out.println("RHS = " + rhs); 
        } 
        return lhs; 
      } 
    } 
    return null; 
  } 

  public Expression parse() 
  { // System.out.println("LEXICALS: " + lexicals); 
    Expression ee = parse_expression(0,0,lexicals.size()-1); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parseExpression() 
  { // System.out.println("LEXICALS: " + lexicals); 
    Expression ee = parse_expression(0,0,lexicals.size()-1); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parse_expression(int bcount, int pstart, int pend)
  { Expression ee = null; 
    
    if ("if".equals(lexicals.get(pstart) + "") && "endif".equals(lexicals.get(pend) + ""))
    { ee = parse_conditional_expression(bcount,pstart,pend); 
      if (ee != null) { return ee; } 
    } 

    if ("not".equals(lexicals.get(pstart) + ""))
    { ee = parse_expression(bcount,pstart+1,pend); 
      if (ee != null) 
      { return new UnaryExpression("not", ee); } 
    } 
 
    if ("_".equals(lexicals.get(pstart) + "") && pend == pstart+1)
    { ee = parse_expression(bcount,pstart+1,pend); 
      if (ee != null) 
      { return new UnaryExpression("_", ee); } 
    } 

    OpOccurrence op = getBestOcc(pstart,pend); 
    if (op == null) // No logical ops here
    { ee = parse_eq_expression(bcount,pstart,pend);
      if (ee == null) 
      { ee = parse_factor_expression(bcount,pstart,pend); }
      return ee; 
    }
    if (op.bcount > bcount) // bracketed expression
    { ee = parse_eq_expression(bcount,pstart,pend); 
      if (ee == null) 
      { ee = parse_factor_expression(bcount,pstart,pend); }
      return ee; 
    }  
    else 
    { ee = parse_implies_expression(bcount,pstart,pend,op.location,op.op); }
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

public Expression parse_conditional_expression(int bc, int st, int en)
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
      { System.err.println("Too many endifs"); return null; }
    }
    else if ("then".equals(lxj) && ifcount == 1)
    { Expression test = parse_expression(bc, st+1, j-1);
     if (test == null)
     { System.err.println("Error in if test expression"); return null;  }
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
         { Expression ifpart = parse_expression(bc, j+1, k-1);
           Expression elsepart = parse_expression(bc, k+1, en-1);
           if (ifpart != null && elsepart != null)
           { return new ConditionalExpression(test,ifpart,elsepart); } else { return null; }
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
      { System.err.println("Too many endifs"); return null; }
    }
    else if ("then".equals(lxj) && ifcount == 1)
    { Expression test = parse_ATLexpression(bc, st+1, j-1,entities,types);
      if (test == null)
      { System.err.println("Error in if test expression"); return null;  }
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
      { System.err.println("Too many in clauses"); return null; }
      else 
      { Expression var = parse_ATLexpression(bc, st+1, st+1,entities,types);
        if (var == null)
        { System.err.println("Error in let variable"); return null;  }

        for (int k = st+2; k < j; k++)
        { String lxk = lexicals.get(k) + "";
          if ("=".equals(lxk))
          { Type ltype = parseType(st+3,k-1,entities,types);
            if (ltype == null)
            { System.err.println("Error in let type"); return null;  }

            Expression lexp = parse_ATLexpression(bc,k+1,j-1,entities,types);
            if (lexp == null)
            { System.err.println("Error in let definition expression"); return null;  }

            Expression lbody = parse_ATLexpression(bc,j+1,en,entities,types); 
            if (lbody == null)
            { System.err.println("Error in let body"); }

            if (ltype != null && lexp != null && lbody != null) 
            { BinaryExpression letexp = new BinaryExpression("let", lexp, lbody); 
              letexp.accumulator = new Attribute(var + "", ltype, ModelElement.INTERNAL); 
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

  public BinaryExpression parse_implies_expression(int bcount, int pstart, 
                                                   int pend, int pos, String op)
  { // There must be a logical operator at top level in the expression
    Expression e1 = null; 
    Expression e2 = null;
    // System.out.println("Trying to parse logical expression"); 
    // System.out.println("Trying at: " + pos + " " + op); 
    e1 = parse_expression(bcount,pstart,pos-1); 

    // if (e1 == null) 
    // { System.err.println("Invalid expression: " + showLexicals(pstart, pos-1)); }
 
    e2 = parse_expression(bcount,pos+1,pend); 

    // if (e2 == null) 
    // { System.err.println("Invalid expression: " + showLexicals(pos + 1, pend)); }

    if (e1 == null || e2 == null)
    { System.out.println("Failed to parse: " + showLexicals(pstart, pend)); 
      return null; 
    }
    else 
    { BinaryExpression ee = new BinaryExpression(op,e1,e2);
      // System.out.println("Parsed implies expression: " + ee); 
      return ee; 
    } 
  }
   
  public String showLexicals(int st, int en)
  { String res = ""; 
    for (int i = st; i <= en; i++) 
    { res = res + lexicals.get(i) + " "; } 
    return res; 
  } 

  /* public BinaryExpression parse_or_expression(int pstart, 
                                              int pend)
  { 
    Expression e1 = null; 
    Expression e2 = null;

    System.out.println("Trying to parse or expression"); 
    
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
 
    System.out.println("Trying to parse and expression");
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
                                              int pend)
  { Expression e1 = null; 
    Expression e2 = null;

    // System.out.println("Trying to parse eq. expression"); 

    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 
      if (ss.equals(":=") || ss.equals(":") || ss.equals("<:") ||
          ss.equals("/:") || ss.equals("/<:") || ss.equals("<>") || 
          Expression.comparitors.contains(ss))
      { e1 = parse_additive_expression(bc,pstart,i-1); 
        e2 = parse_additive_expression(bc,i+1,pend); 
        if (e1 == null || e2 == null)
        { } // return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println("Parsed equality expression: " + ee); 
          return ee; 
        }
      }
    }
    return parse_additive_expression(bc,pstart,pend); 
    // return null; 
  }  // <> is only for ATL

  public Expression parse_additive_expression(int bc, int pstart, int pend)
  { for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("+") || ss.equals("-") || ss.equals("\\/"))
      { Expression e1 = parse_factor_expression(bc,pstart,i-1);
        Expression e2 = parse_additive_expression(bc,i+1,pend);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println("Parsed additive expression: " + ee);
          return ee;
        } 
      }     
    } 
    return parse_factor_expression(bc,pstart,pend); 
  } // reads left to right, not putting priorities on * above +

  public Expression parse_factor_expression(int bc, int pstart, int pend)
  { // System.out.println("Trying to parse factor expression"); 
    for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if // (ss.equals("+") || ss.equals("-") || 
         (ss.equals("/\\") || ss.equals("^") ||
          ss.equals("*") || ss.equals("/") || ss.equals("div") || 
          ss.equals("mod"))
      { Expression e1 = parse_basic_expression(bc,pstart,i-1);  // factor2
        Expression e2 = parse_factor_expression(bc,i+1,pend);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println("Parsed factor expression: " + ee);
          return ee;
        } 
      }     
    } 
    return parse_factor2_expression(bc,pstart,pend); 
  } // reads left to right, not putting priorities on * above +
  // div is only for ATL. 

  public Expression parse_factor2_expression(int bc, int pstart, int pend)
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
             "sum".equals(ss2) || "sort".equals(ss2) || "asSet".equals(ss2) ||
             "sqrt".equals(ss2) || "sqr".equals(ss2) || "asSequence".equals(ss2) ||
             "last".equals(ss2) || "first".equals(ss2) || "closure".equals(ss2) ||
             "subcollections".equals(ss2) || "reverse".equals(ss2) || "prd".equals(ss2) ||
             "tail".equals(ss2) || "front".equals(ss2) || "isEmpty".equals(ss2) ||
             "notEmpty".equals(ss2) || "toUpperCase".equals(ss2) || "flatten".equals(ss2) ||  
             "toLowerCase".equals(ss2) || "isInteger".equals(ss2) || "toLong".equals(ss2) ||
             "toInteger".equals(ss2) || "isReal".equals(ss2) || "toReal".equals(ss2) ||
             "exp".equals(ss2) || "log".equals(ss2) || "log10".equals(ss2) || 
             "sin".equals(ss2) || "cos".equals(ss2) || "allInstances".equals(ss2) ||
             "tan".equals(ss2) || "oclIsUndefined".equals(ss2) ||
             "floor".equals(ss2) || "ceil".equals(ss2) || "round".equals(ss2) ||
             "abs".equals(ss2) || "cbrt".equals(ss2) || "asin".equals(ss2) ||
             "acos".equals(ss2) || "atan".equals(ss2) || "isLong".equals(ss2) ||
             "sinh".equals(ss2) || "cosh".equals(ss2) || "tanh".equals(ss2) ||
             "values".equals(ss2) || "keys".equals(ss2) ) )
        { Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) 
          { // System.err.println("Invalid unary -> expression at: " + showLexicals(pstart,pend)); 
            continue; 
          }
          UnaryExpression ue = new UnaryExpression(ss+ss2,ee2);
          // System.out.println("Parsed unary expression: " + ue); 
          return ue;   // This excludes additional operators to the ones listed above.  
        } 
        else if ("exists".equals(ss2) && 
                 i+5 < pend && // "(".equals(lexicals.get(i+2) + "") &&
                               "|".equals(lexicals.get(i+4) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->exists(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
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
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("#1",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("existsLC".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->existsLC(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
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
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
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
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
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
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
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
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|C",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if ("any".equals(ss2) && 
                 i+5 < pend && "|".equals(lexicals.get(i+4) + "") &&
                               // "(".equals(lexicals.get(i+2) + "") &&
                               ")".equals(lexicals.get(pend) + ""))
        { // It is ->any(v|...)
          Expression ee1 = parse_expression(bc+1,i+5,pend-1);
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) { continue; } 
          BasicExpression bevar = 
            new BasicExpression(lexicals.get(i+3) + "",0);
          BinaryExpression be = 
            new BinaryExpression("|A",new BinaryExpression(":",bevar,ee2),ee1); 
          // System.out.println("Parsed: " + be); 
          return be; 
        } 
        else if (pend == i+3) // && "(".equals(lexicals.get(i+2) + "") &&
                              //  ")".equals(lexicals.get(pend) + ""))  
        { Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) { continue; } 
          UnaryExpression be = new UnaryExpression(ss+ss2,ee2); 
          // System.out.println("Parsed new unary -> expression: " + be); 
          return be; 
        } 
        else if (i + 3 <= pend) // && "(".equals(lexicals.get(i+2) + "") &&
                                //   ")".equals(lexicals.get(pend) + "")) 
        // this should allow new Binary operators 
        { // System.out.println("Tring tp parse at " + ss2); 
          Expression ee1 = parse_expression(bc+1,i+3,pend-1); 
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_factor_expression(bc,pstart,i-1); 
          if (ee2 == null) { continue; } 
          BinaryExpression be = new BinaryExpression(ss+ss2,ee2,ee1); 
          System.out.println("Parsed binary -> expression: " + be); 
          return be; 
        } 
      }     
    } 
    return parse_basic_expression(bc,pstart,pend); 
  } // reads left to right, not putting priorities on * above +

  // add the case for ->iterate

  public Expression parse_basic_expression(int bc, int pstart, int pend)
  { // System.out.println("Trying tp parse basic expression from " + pstart + " to " + pend); 

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
      if (isKeyword(ss))
      { System.err.println(">>>: Invalid basic expression: keyword: " + ss); 
        return null;
      } 

      BasicExpression ee = // new BasicExpression(ss,0);   // (ss) surely?
                       new BasicExpression(ss,0); 
      Expression ef = ee.checkIfSetExpression(); 

      // if (ef instanceof BasicExpression) 
      // { ((BasicExpression) ef).setPrestate(ee.getPrestate()); } 

      // System.out.println("Parsed basic expression: " + ss + " " + ee + " " + ef); 
      return ef;
    }

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "{".equals(lexicals.get(pstart) + ""))
    { return parse_set_expression(bc,pstart,pend); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Sequence".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_sequence_expression(bc,pstart+1,pend); } 

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Set".equals(lexicals.get(pstart) + "") &&
        "{".equals(lexicals.get(pstart + 1) + ""))
    { return parse_set_expression(bc,pstart+1,pend); } 

    if (pstart < pend && "-".equals(lexicals.get(pstart) + ""))
    { Expression arg = parse_additive_expression(bc,pstart+1,pend); 
      if (arg == null) 
      { System.err.println("Not additive expression: " + showLexicals(pstart+1, pend)); 
        return null; 
      } 
      return new UnaryExpression("-",arg); 
    } // likewise for "not"

    if (pstart + 1 < pend && ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+1) + ""))
    { Expression op = parse_basic_expression(bc,pstart,pstart); 
      
      Vector ve = parse_fe_sequence(bc,pstart+2,pend-1); 
      if (ve != null && op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op; 

        if (Expression.isFunction(beop.data))
        { beop.umlkind = Expression.FUNCTION; } 
        else 
        { beop.setIsEvent(); } 
 
        beop.setParameters(ve); 
        // System.out.println("Parsed call expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart + 2 < pend && ")".equals(lexicals.get(pend) + "") && 
        "(".equals(lexicals.get(pstart+2) + "") && 
        ("" + lexicals.get(pstart + 1)).charAt(0) == '.')
    { // iden.op(args)
      Expression objref = parse_basic_expression(bc,pstart,pstart); 
      String opstring = lexicals.get(pstart + 1) + ""; 
      String opstr = opstring.substring(1,opstring.length()); 

      Vector ve = parse_fe_sequence(bc,pstart+3,pend-1); 
      if (ve != null && objref != null && objref instanceof BasicExpression) // must be
      { BasicExpression beopref = (BasicExpression) objref; 
        BasicExpression beop = new BasicExpression(opstr); 

        if (Expression.isFunction(opstr))
        { beop.umlkind = Expression.FUNCTION; } 
        else 
        { beop.setIsEvent(); } 
 
        beop.setParameters(ve);
        beop.setObjectRef(beopref);  
        // System.out.println("Parsed call expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && "]".equals(lexicals.get(pend) + "") && 
        "[".equals(lexicals.get(pstart+1) + ""))
    { String ss = lexicals.elementAt(pstart).toString(); 
      BasicExpression ee = new BasicExpression(ss,0); 
      Expression op = ee.checkIfSetExpression(); 
      // Expression op = parse_basic_expression(bc,pstart,pstart); 
      Expression arg = parse_additive_expression(bc,pstart+2,pend-1); 
      if (arg == null) 
      { System.err.println("ERROR: Invalid array argument: " + showLexicals(pstart+2,pend-1)); } 
      else if (op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        // System.out.println("Parsed array expression: " + beop);
        return beop;  
      } 
      // return null; 
    }       

    if (pstart < pend && "]".equals(lexicals.get(pend-1) + "") && 
        "[".equals(lexicals.get(pstart+1) + ""))  // be[arrind].be2
    { String tail = lexicals.get(pend) + ""; 
      if (tail.charAt(0) == '.')
      { tail = tail.substring(1,tail.length()); } 
      Expression op = parse_basic_expression(bc,pstart,pstart); 
      Expression arg = parse_additive_expression(bc,pstart+2,pend-2); 
      if (arg == null) 
      { System.err.println("ERROR: Invalid array argument: " + showLexicals(pstart+2,pend-2)); } 
      else if (op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        BasicExpression te = // new BasicExpression(ss,0);   // (ss) surely?
                       new BasicExpression(tail,0); 
        Expression ef = te.checkIfSetExpression(); 
        if (ef != null && (ef instanceof BasicExpression)) 
        { ((BasicExpression) ef).setInnerObjectRef(beop); }  
        // System.out.println("Parsed array 1 expression: " + ef);
        return ef;  
      } 
      // return null; 
    }       

  if (pstart + 1 < pend && ")".equals(lexicals.get(pend) + "") && 
    !("(".equals(lexicals.get(pstart) + "")))
  { for (int i = pstart + 1; i < pend; i++)
    { String strs = lexicals.get(i) + "";
      if ("(".equals(strs))
      { Expression op = parse_basic_expression(bc, pstart, i-1);
        if (op != null && op instanceof BasicExpression)
        { Vector ve = parse_fe_sequence(bc,i+1,pend-1);
          if (ve != null)
          { BasicExpression beop = (BasicExpression) op;
            beop.setIsEvent();
            beop.setParameters(ve);
            // System.out.println("Parsed extended call expression " + beop);
            return beop;
          }
        }
      }
    }
  }

  /*  if (pstart < pend &&  
        "[".equals(lexicals.get(pstart+1) + ""))  // be[arrind].something
    { for (int k = pstart + 2; k < pend; k++) 
      { String kstring = lexicals.get(k) + ""; 
        if ("]".equals(kstring))
        { Expression op = parse_basic_expression(bc,pstart,pstart); 
          Expression arg = parse_factor_expression(bc,pstart+2,k-1); 
      
          String tail = lexicals.get(k+1) + ""; 
          if (tail.charAt(0) == '.')
          { tail = tail.substring(1,tail.length()); } 
          if (op instanceof BasicExpression) // must be
          { BasicExpression beop = (BasicExpression) op;  
            beop.setArrayIndex(arg); 
            Expression te = parse_basic_expression(bc,k+1,pend); 
            if (te != null)
            { if (te instanceof BasicExpression)
              { BasicExpression bte = (BasicExpression) te; 
                bte.setData(tail);  
                bte.setObjectRef(beop); 
                System.out.println("Parsed array 2 expression: " + bte);
                return bte;  
              }
              else 
              { return null; } 
            }        
          } 
        }
      } 
      return null; 
    }   */     

    return parse_bracketed_expression(bc,pstart,pend); 
  }

  public Expression parse_set_expression(int bc,int pstart,int pend)
  { Vector ve = parse_fe_sequence(bc,pstart+1,pend-1); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,false); 
    // System.out.println("Parsed set expression: " + res); 
    return res; 
  } 

  public Expression parse_sequence_expression(int bc,int pstart,int pend)
  { Vector ve = parse_fe_sequence(bc,pstart+1,pend-1); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    // System.out.println("Parsed sequence expression: " + res); 
    return res; 
  } 

  public Expression parse_map_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_map_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    res.setType(new Type("Map",null)); 
    // System.out.println("Parsed map expression: " + res); 
    return res; 
  } 

  public Vector parse_fe_sequence(int bc, int st, int ed)
  { Vector res = new Vector(); 
    // if (st == ed) // just one basic exp
    // { Expression elast = parse_basic_expression(bc,st,ed);
    //   res.add(elast); 
    //   return res; 
    // } 
    if (st > ed)
    { return res; } 
    return getFESeq(bc,st,ed); 

    // Only commas at the top level of the expression are wanted! 
    /* 
    for (int i = st; i < ed; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_factor_expression(bc,st,i-1); 
        if (e1 == null)
        { continue; } // skip this comma
        res = parse_fe_sequence(bc,i+1,ed); 
        if (res == null)
        { continue; } // skip this comma
        res.add(0,e1);
        return res; 
      } 
    }
    Expression elast = parse_factor_expression(bc,st,ed);
    if (elast != null) 
    { res.add(elast); }  
    return res; 
    */ 
  }  

  private Vector getFESeq(int bc, int st, int en)
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
           Expression exp = parse_additive_expression(bc,st0,i-1);
           if (exp == null) 
           { System.out.println("Invalid additive exp: " + buff);
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
     { Expression exp = parse_additive_expression(bc,st0,en);
       if (exp == null) 
       { System.out.println("Invalid additive exp: " + buff);
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
           { System.out.println("Invalid pair exp: " + buff);
             return null;
           }
           exp.setBrackets(true); 
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
       { System.out.println("Invalid pair exp: " + buff);
         return null;
       }
       exp.setBrackets(true); 
       res.add(exp);
     }
     else
     { System.err.println("Not fe sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }

  public Expression parse_pair_expression(int bc, int st, int en, Vector entities, Vector types)
  { // st is "(" and en is ")" 
    for (int i = st+1; i < en; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_ATLexpression(bc,st + 1, i-1,entities,types); 
        Expression e2 = parse_ATLexpression(bc,i+1, en-1,entities,types); 
        if (e1 != null && e2 != null)
        { return new BinaryExpression(",", e1, e2); } 
      } 
    } 
    return null; 
  }  




  public BehaviouralFeature operationDefinition(Vector entities, Vector types)
  { return operationDefinition(0, lexicals.size()-1, entities, types); } 


public BehaviouralFeature operationDefinition(int st, int en, Vector entities, Vector types)
{ if (en <= st) { return null; }
  boolean valid = false; 
  boolean foundpre = false; 
  boolean foundpost = false; 

  String opname = lexicals.get(st) + "";
  BehaviouralFeature bf = new BehaviouralFeature(opname);
  int st0 = st + 1;
  for (int i = st0; i < en; i++)
  { String lx = lexicals.get(i) + "";
    String lx1 = lexicals.get(i+1) + "";
    if (lx.equals("pre") && lx1.equals(":"))
    { parseOpDecs(st+1,i-1,entities,types,bf); 
      foundpre = true; 
      st0 = i+2;
    }
    else if (lx.equals("post") && lx1.equals(":"))
    { Expression pre = parse_expression(0,st0,i-1);
      if (pre == null)
      { System.err.println("ERROR: Invalid precondition: " + showLexicals(st0,i-1)); 
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
          Expression pots = parse_expression(0,i+2,j-1); 
          if (pots == null) 
          { System.err.println("ERROR: Invalid postcondition: " + showLexicals(i+2,en));
            bf.setPost(new BasicExpression(true)); 
          } 
          else 
          { bf.setPost(pots); } 
          Statement act = parseStatement(j+2,en,entities,types); 
          if (act == null) 
          { System.err.println("ERROR: Invalid postcondition: " + showLexicals(i+2,en)); } 
          bf.setActivity(act); 
          return bf;  
        }    
      }
    }
  }

  /* Expression pst = parse_expression(0,st0,en);
  if (pst == null)
  { System.err.println("ERROR: Invalid postcondition: " + showLexicals(st0,en));
    bf.setPost(new BasicExpression(true)); 
  }
  else 
  { bf.setPost(pst); 
    valid = true; 
  } */ 

  if (foundpost == false)
  { System.err.println("**** Invalid operation definition, no postcondition: " + showLexicals(st,en)); 
    parseOpDecs(st+1,en,entities,types,bf); 
  }
  else 
  { Expression pst = parse_expression(0,st0,en);
    if (pst == null)
    { System.err.println("ERROR: Invalid postcondition: " + showLexicals(st0,en));
      bf.setPost(new BasicExpression(true)); 
    }
    else 
    { bf.setPost(pst); 
      valid = true; 
    }
  } 

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

  // System.out.println(np + " Parameters"); 
  

  Vector res = new Vector();
  for (int j = st; j <= en; j++)
  { String lx = lexicals.get(j) + "";
    if ("(".equals(lx)) { bcnt++; }
    else if (")".equals(lx)) 
    { bcnt--; 
      if (bcnt < 0)
      { System.err.println("*** Extra closing bracket in declaration: " + showLexicals(st,j)); 
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
      { System.err.println("*** ERROR: Invalid return type: " + showLexicals(j+1, en)); 
        bf.setQuery(false); 
      }
      else if ("void".equals(rt + ""))
      { bf.setQuery(false); } 
      else 
      { bf.setQuery(true); } 
      bf.setResultType(rt);
    } // it is query unless the result type is null or void. 
  }

  if (bcnt > 0)
  { System.err.println("*** Unclosed bracket in declaration: " + showLexicals(st,en)); }
}

private Attribute parseParameterDec(int st, int en, Vector entities, Vector types)
{ // should be att : Type
  if (st+2 > en) { return null; } 

  String attname = lexicals.get(st) + "";
  Type typ = parseType(st+2,en,entities,types);
  if (typ == null)
  { System.err.println("**** ERROR: Invalid/unknown type: " + showLexicals(st+2, en)); 
    return null; 
  }
  Attribute att = new Attribute(attname, typ, ModelElement.INTERNAL);
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
    { Expression val = parse_expression(0,i+1,en); 
      Type typ = parseType(st+2,i-1,entities,types); 
      if (typ == null)
      { System.err.println("**** ERROR: Invalid/unknown type: " + showLexicals(st+2, i-1)); 
        return null; 
      }
      Attribute att = new Attribute(attname,typ,ModelElement.INTERNAL); 
      att.setInitialExpression(val); 
      return att; 
    } 
  }
  Type tt = parseType(st+2,en,entities,types); 
  if (tt == null)
  { System.err.println("**** ERROR: Invalid/unknown type: " + showLexicals(st+2, en)); 
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
        // System.out.println("parsed call expression: " + opref); 
        return opref; 
      } 
    }
    // System.out.println("Failed to parse call expression"); 
    return null; 
  }

  public Vector parse_call_arguments(int st, int ed)
  { Vector res = new Vector(); 
    for (int i = st; i < ed; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_basic_expression(st,i-1); 
        res = parse_call_arguments(i+1,ed); 
        if (e1 != null) { res.add(0,e1); }
        return res; 
      } 
    }
    return res; 
  }  */ 
 

        

  public Expression parse_bracketed_expression(int bcount, int pstart, int pend)
  { if (lexicals.size() > 2 &&
        lexicals.get(pstart).toString().equals("(") &&
        lexicals.get(pend).toString().equals(")"))
    { Expression eg = parse_expression(bcount+1, pstart+1, pend-1);
      // System.out.println("parsed bracketed expression: " + eg);  
      if (eg != null)
      { eg.setBrackets(true); }
      return eg; 
    }
    return null; 
  } // also case of pstart = (, pend-1 = ), pend.charAt(0) == '.'
 


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
      // System.out.println("Parsed implies expression: " + ee); 
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
          // System.out.println("Parsed equality expression: " + ee); 
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
          // System.out.println("Parsed additive expression: " + ee);
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
      { Expression e1 = parse_ATLbasic_expression(bc,pstart,i-1,entities,types);  // factor2
        Expression e2 = parse_ATLfactor_expression(bc,i+1,pend,entities,types);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          // System.out.println("Parsed factor expression: " + ee);
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
             "sum".equals(ss2) || "sort".equals(ss2) || "asSet".equals(ss2) ||
             "sqrt".equals(ss2) || "sqr".equals(ss2) || "asSequence".equals(ss2) ||
             "last".equals(ss2) || "first".equals(ss2) || "closure".equals(ss2) ||
             "subcollections".equals(ss2) || "reverse".equals(ss2) || "prd".equals(ss2) ||
             "tail".equals(ss2) || "front".equals(ss2) || "isEmpty".equals(ss2) ||
             "notEmpty".equals(ss2) || "toUpperCase".equals(ss2) || "flatten".equals(ss2) ||  
             "toLowerCase".equals(ss2) || "isInteger".equals(ss2) || "toLong".equals(ss2) ||
             "toInteger".equals(ss2) || "isReal".equals(ss2) || "toReal".equals(ss2) ||
             "exp".equals(ss2) || "log".equals(ss2) || "log10".equals(ss2) || 
             "sin".equals(ss2) || "cos".equals(ss2) || 
             "tan".equals(ss2) || "oclIsUndefined".equals(ss2) ||
             "floor".equals(ss2) || "ceil".equals(ss2) || "round".equals(ss2) ||
             "abs".equals(ss2) || "cbrt".equals(ss2) || "asin".equals(ss2) ||
             "acos".equals(ss2) || "atan".equals(ss2) || "isLong".equals(ss2) ||
             "sinh".equals(ss2) || "cosh".equals(ss2) || "tanh".equals(ss2)) )
        { Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) 
          { // System.err.println("Invalid unary -> expression at: " + showLexicals(pstart,pend)); 
            continue; 
          }
          UnaryExpression ue = new UnaryExpression(ss+ss2,ee2);
          // System.out.println("Parsed unary expression: " + ue); 
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
        else if ("iterate".equals(ss2) && ";".equals(lexicals.get(i+4) + "") && i+7 < pend)
        { String bevar = lexicals.get(i+3) + "";
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
          // System.out.println("Parsed new unary -> expression: " + be); 
          return be; 
        } 
        else if (i + 3 <= pend) // && "(".equals(lexicals.get(i+2) + "") &&
                                //   ")".equals(lexicals.get(pend) + "")) 
        // this should allow new Binary operators 
        { // System.out.println("Tring tp parse at " + ss2); 
          Expression ee1 = parse_ATLexpression(bc+1,i+3,pend-1,entities,types); 
          if (ee1 == null) { continue; } 
          Expression ee2 = parse_ATLfactor_expression(bc,pstart,i-1,entities,types); 
          if (ee2 == null) { continue; } 
          BinaryExpression be = new BinaryExpression(ss+ss2,ee2,ee1); 
          // System.out.println("Parsed binary -> expression: " + be); 
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
      { // System.err.println("ERROR: Invalid basic expression: " + ss); 
        return null; 
      } 

      if (isKeyword(ss))
      { // System.err.println("ERROR: Invalid basic expression: keyword: " + ss); 
        return null;
      } 

      BasicExpression ee = // new BasicExpression(ss,0);   // (ss) surely?
                       new BasicExpression(ss,0); 
      Expression ef = ee.checkIfSetExpression(); 

      // if (ef instanceof BasicExpression) 
      // { ((BasicExpression) ef).setPrestate(ee.getPrestate()); } 

      // System.out.println("Parsed basic expression: " + ss + " " + ee + " " + ef); 
      return ef;
    }

    if (pstart < pend && "}".equals(lexicals.get(pend) + "") && 
        "Map".equals(lexicals.get(pstart) + ""))
    { return parse_map_expression(bc,pstart+1,pend,entities,types); } 

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
      { // System.err.println("Not ATL additive expression: " + showLexicals(pstart+1, pend)); 
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
        // System.out.println("Parsed call expression: " + beop);
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
        // System.out.println("Parsed call expression: " + beop);
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
        // System.out.println("Parsed array expression: " + beop);
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
      { System.err.println("Invalid ATL array argument: " + showLexicals(pstart+2,pend-2)); } 
      else if (op != null && op instanceof BasicExpression) // must be
      { BasicExpression beop = (BasicExpression) op;  
        beop.setArrayIndex(arg); 
        BasicExpression te = new BasicExpression(tail); 
        BasicExpression ef = (BasicExpression) te.checkIfSetExpression(); 
        if (ef != null) 
        { ef.setInnerObjectRef(beop); }  
        // System.out.println("Parsed array 1 expression: " + ef);
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
            // System.out.println("Parsed extended call expression " + beop);
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
      // System.out.println("parsed bracketed expression: " + eg);  
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
    // System.out.println("Parsed set expression: " + res); 
    return res; 
  } 

  public Expression parse_ATLsequence_expression(int bc,int pstart,int pend, Vector entities, Vector types)
  { Vector ve = parse_ATLfe_sequence(bc,pstart+1,pend-1,entities,types); 
    if (ve == null) 
    { return null; } 
    Expression res = new SetExpression(ve,true); 
    // System.out.println("Parsed sequence expression: " + res); 
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
           { // System.out.println("Invalid ATL additive exp: " + buff);
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
     { System.err.println("Not ATL fe sequence: " + showLexicals(st,en)); 
       return null;
     }
     return res; 
   }


  /* public Statement parseStatement(Vector entities, Vector types)
  { int en = lexicals.size(); 
    return parseStatement(0,en-1,entities,types); 
  } */ 
    


  public Statement parseStatement(int s, int e, Vector entities, Vector types)
  { Statement st = parseSequenceStatement(s,e,  entities, types);
    if (st == null)
    { st = parseLoopStatement(s,e, entities, types); }
    if (st == null)
    { st = parseConditionalStatement(s,e,  entities,  types); }
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
      { s1 = parseStatement(s,i-1, entities,types);
        s2 = parseStatement(i+1,e, entities,types);
        if (s1 == null || s2 == null) { continue; } // try another ;  
        // { return null; }
        else
        { SequenceStatement res = new SequenceStatement();
          res.addStatement(s1);
          res.addStatement(s2);
          // System.out.println("Parsed ; statement: " + res);
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
        { test = parse_expression(0,s+1,i-1);
          if (test == null) 
          { System.err.println("ERROR: Invalid test in for loop: " + showLexicals(s+1,i-1)); } 
          s1 = parseStatement(i+1, e, entities, types);
          if (s1 == null || test == null)
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
        { test = parse_expression(0,s+1,i-1);
          if (test == null) 
          { System.err.println("ERROR: Invalid test in while loop: " + showLexicals(s+1,i-1)); } 
          s1 = parseStatement(i+1, e, entities, types);
          if (s1 == null || test == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.WHILE); 
          // System.out.println("Parsed while statement: " + res);
          return res;
        }
      }
      // System.err.println("Unable to parse while statement: " + lexicals.get(s) + " ... " + 
      //                    lexicals.get(e)); 
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
        { test = parse_expression(0,s+1,i-1);
          if (test == null) 
          { System.err.println("ERROR: Invalid test in conditional: " + showLexicals(s+1,i-1)); 
            continue; 
          } 

          for (int j = i+1; j < e; j++)
          { String ss1 = lexicals.get(j) + "";
            if (ss1.equals("else"))
            { s1 = parseStatement(i+1, j-1, entities, types);
              s2 = parseBasicStatement(j+1,e, entities, types);  // else must be bracketed or basic
              if (s1 == null || s2 == null || test == null)
              { continue; }

              IfStatement res = new IfStatement(test,s1,s2);
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



  public Statement parseBasicStatement(int s, int e, Vector entities, Vector types)
  { if (e == s)
    { if ("skip".equals(lexicals.get(e) + ""))
      { return new InvocationStatement("skip",null,null); }
       // operation call
      else if ("return".equals(lexicals.get(e) + ""))
      { return new ReturnStatement(); }
      else 
      { Expression ee = parse_basic_expression(0,s,e);
        if (ee == null) 
        { // System.err.println("Invalid basic expression: " + showLexicals(s,e)); 
          return null; 
        } 
        InvocationStatement istat = new InvocationStatement(ee + "",null,null);
        istat.setCallExp(ee); 
        return istat; 
      } // what about continue and break? 
    }

    if ("(".equals(lexicals.get(s) + "") &&
        ")".equals(lexicals.get(e) + ""))
    { Statement stat = parseStatement(s+1,e-1,entities,types); 
      if (stat != null)
      { stat.setBrackets(true); }
      return stat; 
    }  // set brackets
    else if ("return".equals(lexicals.get(s) + ""))
    { Expression ret = parse_expression(0,s+1,e); 
      if (ret != null) 
      { return new ReturnStatement(ret); }  
    } 
    else if ("execute".equals(lexicals.get(s) + ""))
    { Expression ret = parse_expression(0,s+1,e); 
      if (ret != null) 
      { return new ImplicitInvocationStatement(ret); }  
    }
    else if ("var".equals(lexicals.get(s) + ""))
    { // creation with complex type
      String varname = lexicals.get(s+1) + ""; 
      Type typ = parseType(s+3,e,entities,types);
      CreationStatement cs = new CreationStatement(typ + "", varname); 
      cs.setType(typ); 
      if (typ != null) 
      { cs.setElementType(typ.getElementType()); } 
      return cs; 
    }  
    else if (e == s + 2 && ":".equals(lexicals.get(s+1) + ""))
    { CreationStatement cs = new CreationStatement(lexicals.get(e) + "", lexicals.get(s) + "");
      Type tt = Type.getTypeFor(lexicals.get(e) + "", new Vector(), new Vector()); // assume primitive
      cs.setType(tt); 
      return cs; 
    } // allow any type
    else 
    { for (int i = s; i < e; i++) 
      { String ss = lexicals.get(i) + ""; 
        if (":=".equals(ss))
        { if (i == s + 3 && ":".equals(lexicals.get(s + 1) + ""))
          { // Assignment with declaration 
            Type t = Type.getTypeFor(lexicals.get(s + 2) + "", new Vector(), new Vector()); 
            String var = lexicals.get(s) + ""; 
            Expression exp2 = parse_expression(0,i+1,e);
            if (exp2 == null) { return null; } 
            /* System.out.println("Parsed assignment with type: " + var + " : " +
                               t + " := " + exp2); */
            AssignStatement ast = new AssignStatement(new BasicExpression(var), exp2); 
            ast.setType(t); 
            ast.setOperator(":="); 
            return ast; 
          } 
          Expression e1 = parse_basic_expression(0,s,i-1); 
          Expression e2 = parse_expression(0,i+1,e); 
          if (e1 == null || e2 == null)
          { // System.err.println("ERROR: Unable to parse basic command: " + showLexicals(s,e)); 
            return null; 
          } 
          // System.out.println("Parsed assignment: " + e1 + " := " + e2); 
          AssignStatement res = new AssignStatement(e1,e2);
          res.setOperator(":="); 
          return res;  
        } 
      }
    }  // all of these should use parseType to allow general types. 
       // should allow  v : T := e with general type T 

    Expression ee = parse_basic_expression(0,s,e);
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

    Statement s1 = null;
    Vector s2 = new Vector();

    for (int i = s; i < e; i++)
    { String ss = lexicals.get(i) + "";
      if (ss.equals(";") && bcc == bc && !instring)
      { s1 = parseStatement(s,i-1,entities,types);
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



  public Vector parseKM3(Vector entities, Vector types, Vector gens, Vector pasts)
  { Vector res = new Vector(); 
    int en = lexicals.size()-1; 
    
    // retain the package name, it becomes the system name. 
    int prevstart = 0; 
    for (int i = 0; i < en; i++) 
    { String lex = lexicals.get(i) + ""; 
      if ("package".equals(lex))
      { prevstart = i; 
        break; 
      } 
    } 

    int st = prevstart; 

    for (int i = st+1; i < en; i++) 
    { String lex = lexicals.get(i) + "";
      if ("package".equals(lex))
      { // parse contents of previous package: 
        Vector resp = parseKM3package(prevstart,i-1,entities,types,gens,pasts); 
        res.addAll(resp); 
        prevstart = i; 
      } 
    } 
       
    Vector resp = parseKM3package(prevstart,en-1,entities,types,gens,pasts); 
    res.addAll(resp); 
    return res; 
  } 

  public UseCase parseKM3UseCase(Vector entities, Vector types, 
                                 Vector gens, Vector pasts)
  { int en = lexicals.size()-1; 
    return parseKM3UseCase(0,en,entities,types,gens,pasts); 
  } 


  public Vector parseKM3package(int st, int en, 
                                Vector entities, Vector types, Vector gens, Vector pasts)
  { Vector res = new Vector(); 
    if ("package".equals(lexicals.get(st) + "") && 
        "{".equals(lexicals.get(st+2) + ""))
    { String pname = lexicals.get(st+1) + ""; 
      System.out.println("Parsing package " + pname); 

      for (int i = st+3; i < en;  i++) 
      { String lx = lexicals.get(i) + ""; 
        if ("class".equals(lx) || "abstract".equals(lx) || "enumeration".equals(lx) || 
            "interface".equals(lx) || "datatype".equals(lx) || "usecase".equals(lx))
        { Vector v = parseKM3PackageContents(i,en-1,entities,types,gens,pasts); 
          res.addAll(v); 
          return res; 
        }
      } 
    } 
    return res; 
  } 


  public Vector parseKM3PackageContents(int st, int en, Vector entities, 
                                        Vector types, Vector gens, Vector pasts)
  { Vector res = new Vector(); 
    if (st >= en)
    { return res; } 
    for (int i = st + 2; i <= en;  i++) 
    { String lx = lexicals.get(i) + ""; 
      if ("class".equals(lx) || "abstract".equals(lx) || "enumeration".equals(lx) ||
          "interface".equals(lx) || "datatype".equals(lx) || "usecase".equals(lx))
      { Object e = parseKM3classifier(st,i-1,entities,types,gens,pasts);
        if (e != null && e instanceof Type)
        { types.add(e); }
		// else if (e != null && e instanceof Entity && "interface".equals(lx))
		// { ((Entity) e).setInterface(true); } 
        res = parseKM3PackageContents(i,en,entities,types,gens,pasts);  
        if (e != null) { res.add(0,e); }  
        return res; 
      }
    } 

    Object e1 = parseKM3classifier(st,en,entities,types,gens,pasts);
    if (e1 != null) 
    { res.add(e1); 
      if (e1 instanceof Type)
      { types.add(e1); }
	  // else if (e1 != null && e1 instanceof Entity && "interface".equals(lx))
	  // { ((Entity) e1).setInterface(true); } 
    } 
      
    return res; 
  } 

  public Object parseKM3classifier(Vector entities, Vector types, 
                                   Vector gens, Vector pasts)
  { return parseKM3classifier(0, lexicals.size()-1, entities, types, gens, pasts); } 


  public Object parseKM3classifier(int st, int en, Vector entities, Vector types, 
                                   Vector gens, Vector pasts)
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
    { return new Type(lexicals.get(st + 1) + "", null); } 
    else if ("enumeration".equals(lx))
    { int j = st + 2; 
      Vector values = new Vector(); 
      while (j < en)
      { if ("literal".equals(lexicals.get(j) + "")) 
        { values.add(lexicals.get(j+1) + ""); 
          j = j + 2; 
        }
        else 
        { j++; } 
      }
      Type tt = new Type(lexicals.get(st + 1) + "", values); 
      return tt; 
    } 
    else if ("usecase".equals(lx))
    { return parseKM3UseCase(st, en, entities, types, gens, pasts); } 
    else        
    { return null; } 

    String supr = ""; 

    if ("extends".equals(lexicals.get(start) + ""))
    { supr = lexicals.get(start+1) + ""; 
      int p = start + 1; 
      PreGeneralisation pregen = new PreGeneralisation(); 
      pregen.e1name = rname; 
      pregen.e2name = supr; 
      gens.add(pregen);   // to be linked in UCDArea. 

      p++; 
      if ((lexicals.get(p) + "").equals("{"))
      { start = p+1; } 
      else 
      { while (!(lexicals.get(p) + "").equals("{"))
        { supr = lexicals.get(p+1) + ""; // multiple inheritance: extends C1, C2 { 
          PreGeneralisation pregen2 = new PreGeneralisation(); 
          pregen2.e1name = rname; 
          pregen2.e2name = supr; 
          System.err.println("Warning: multiple inheritance for " + rname); 
          gens.add(pregen2);   // to be linked in UCDArea.
          p = p+1; 
        }  
        start = p + 1;
      }  
    } 
    else 
    { start = start + 1; } 

    
    Entity res = new Entity(rname); 
    res.setCardinality("*"); 

    if (abstr) 
    { res.setAbstract(true); } 
    if (interf) 
    { res.setInterface(true); } 
    entities.add(res); 

    System.out.println(">>> Parsing KM3 class " + rname); 

    int reached = start; // start of the next element to be parsed. reached <= i

    for (int i = start + 3; i < en; i++) 
    { String lx2 = lexicals.get(i) + ""; 
      if ("attribute".equals(lx2) || "reference".equals(lx2) || "operation".equals(lx2) ||
          "query".equals(lx2) || "static".equals(lx2)) 
      { String lxr = lexicals.get(reached) + ""; 
        if ("attribute".equals(lxr) || "reference".equals(lxr) || "query".equals(lxr) || 
            "operation".equals(lxr) || "static".equals(lxr)) 
        { if ("static".equals(lxr))
          { if ("attribute".equals(lexicals.get(reached+1) + ""))
            { Attribute attr = parseAttributeClause(reached+1, i - 1, entities, types);
              if (attr == null) 
              { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 2)); 
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
              }  
            } 
          } 
          else if ("attribute".equals(lxr))
          { Attribute attr = parseAttributeClause(reached, i - 1, entities, types);
            if (attr == null) 
            { System.err.println("!! Cannot parse attribute " + 
                                   lexicals.get(reached + 1)); 
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
            } 
            else 
            { ast.e1name = rname; 
              pasts.add(ast);
            }  
            // res.addAssociation(ast); 
          } 
          else if ("operation".equals(lxr) || "query".equals(lxr)) 
          { BehaviouralFeature bf = 
              operationDefinition(reached + 1, i-2, entities, types); 
            if (bf != null) 
            { res.addOperation(bf); } 
            else 
            { System.err.println("ERROR: Invalid operation definition: " + 
                               showLexicals(reached+1,i-2));
            } 
          } 
          reached = i;   // one element has been parsed, go on to start of next one. 
        } 
        else 
        { reached = i; 
          i++; 
        } 
        if ("static".equals(lx2)) { i++; } // don't test the next keyword. 
      }
    }  

    if (reached + 3 < en - 1) 
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
        } 
        else 
        { ast.e1name = rname; 
          pasts.add(ast);
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
        } 
      } 

    } 

    return res; 
  } 

  public UseCase parseKM3UseCase(int st, int en, Vector entities, Vector types, 
                                 Vector gens, Vector pasts)
  { // usecase name { postconditions }
    String nme = lexicals.get(st+1) + ""; 

    UseCase uc = new UseCase(nme); 

    for (int i = st + 2; i < en; i++) 
    { String lx = lexicals.get(i) + ""; 
      if (":".equals(lx) && ":".equals(lexicals.get(i+1) + ""))
      { String scope = lexicals.get(i-1) + ""; // must be present, can be void
        Entity ent = (Entity) ModelElement.lookupByName(scope, entities); 

        for (int j = i+2; j < en; j++) 
        { if (":".equals(lexicals.get(j) + "") && ":".equals(lexicals.get(j+1) + ""))
          { Expression ee = parse_expression(0, i+2, j-2); 
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
          else if (j == en-1)  // the closing } 
          { Expression ff = parse_expression(0, i+2, j); 
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

    return uc; 
  } 

  public Attribute parseAttributeClause(int st, int en, Vector entities, Vector types)
  { // attribute name [stereotypes] : type ; 

    String nme = lexicals.get(st+1) + ""; 

    for (int i = st+2; i < en; i++) 
    { if (":".equals(lexicals.get(i) + ""))
      { Type typ = parseType(i+1,en-1,entities,types); 
        if (typ != null) 
        { Attribute att = new Attribute(nme,typ,ModelElement.INTERNAL);
          att.setElementType(typ.getElementType());  

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
          } 
          return att;
        }  
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

    if (";".equals(lexicals.get(reached) + "")) 
    { return res; } 
    else if ("oppositeOf".equals(lexicals.get(reached) + ""))
    { res.role1 = lexicals.get(reached+1) + ""; 
      return res; 
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
  { if ("module".equals(lexicals.get(0) + "") && 
        ";".equals(lexicals.get(2) + ""))
    { ATLModule mod = new ATLModule(lexicals.get(1) + ""); 
      for (int i = 0; i < lexicals.size();  i++) 
      { if ("lazy".equals(lexicals.get(i) + "") || "helper".equals(lexicals.get(i) + "") ||
            "unique".equals(lexicals.get(i) + "") || "rule".equals(lexicals.get(i) + ""))
        { Vector v = parse_ATL_module(i,lexicals.size()-1,entities,types,mod); 
          mod.setElements(v); 
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
    { if ("lazy".equals(lexicals.get(i) + "") || "rule".equals(lexicals.get(i) + "") ||
          "unique".equals(lexicals.get(i) + "") || "helper".equals(lexicals.get(i) + ""))
      { Rule r = parseATLrule(st,i-1,entities,types,mod);
        res = parse_ATL_module(i,en,entities,types,mod);  
        if (r != null) { res.add(0,r); }  
        return res; 
      }
    } 
    Rule r1 = parseATLrule(st,en,entities,types,mod);
    if (r1 != null) { res.add(r1); }      
    return res; 
  } 

  public MatchedRule parseATLrule(int st, int en, Vector entities, Vector types, ATLModule mod)
  { boolean lazy = false; 
    boolean unique = false; 
    String rname = ""; 
    int start = st; 

    if ("lazy".equals(lexicals.get(st) + "") &&
        "rule".equals(lexicals.get(st + 1) + "") &&  
        en - st >= 4 && "}".equals(lexicals.get(en) + ""))
    { lazy = true; 
      start = st + 4; 
      rname = lexicals.get(st + 2) + ""; 
    } 
    else if ("unique".equals(lexicals.get(st) + "") &&
        "lazy".equals(lexicals.get(st + 1) + "") &&  
        en - st >= 5 && "}".equals(lexicals.get(en) + ""))
    { lazy = true; 
      unique = true; 
      start = st + 5; 
      rname = lexicals.get(st + 3) + ""; 
    } 
    else if ("rule".equals(lexicals.get(st) + "") && 
        en - st >= 4 && "}".equals(lexicals.get(en) + ""))
    { lazy = false; 
      start = st + 3; 
      rname = lexicals.get(st + 1) + "";
    } 
    else if ("helper".equals(lexicals.get(st) + "") && 
        en - st >= 9 && "context".equals(lexicals.get(st + 1) + "") && 
        ";".equals(lexicals.get(en) + "")) 
    { String ent = lexicals.get(st + 2) + ""; 
      String attname = lexicals.get(st + 5) + ""; 
      if ("(".equals(lexicals.get(st + 6) + ""))
      { // helper operation of an entity or built-in type
        for (int p = st+7; p < en; p++) 
        { String ps = lexicals.get(p) + ""; 
          if (ps.equals("="))
          { for (int q = p-1; q > st+7; q--) 
            { String qs = lexicals.get(q) + ""; 
              if (qs.equals(":"))
              { Vector params = parseAttributeDecs(st+7,q-2,entities,types);
                Type atType = parseType(q+1,p-1,entities,types); 
                Expression defn = parse_ATLexpression(0,p+1,en-1,entities,types); 
                BehaviouralFeature bf = new BehaviouralFeature(attname,params,true,atType); 
 
                Expression resexp = new BasicExpression("result"); 
                resexp.setType(atType); 
                resexp.setUmlKind(Expression.VARIABLE); 
                defn.setBrackets(true); 
                Expression post = new BinaryExpression("=", resexp, defn); 
                
                bf.setPost(post); // actually to return the defn value
                Entity entet = (Entity) ModelElement.lookupByName(ent,entities); 
                bf.setOwner(entet); 
                if (entet != null) 
                { entet.addOperation(bf); } 
                
                mod.addOperation(bf);   
                return null; 
              } 
            } 
          } 
        } 
      } 

      // helper 'attribute' - actually a cached operation
      for (int r = st+7; r < en; r++) 
      { String rs = lexicals.get(r) + ""; 
        if (rs.equals("="))
        { Type restype = parseType(st+7,r-1,entities,types); 
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
          bf.setPost(post); // actually to return the defn value
          Entity ente = (Entity) ModelElement.lookupByName(ent,entities); 
          bf.setOwner(ente); 
          if (ente != null) 
          { ente.addOperation(bf); } 
          mod.addOperation(bf);  
          bf.setCached(true); 
          return null;
        }  
      } 
      return null; 
    } 
    else if ("helper".equals(lexicals.get(st) + "") && 
        en - st >= 7 && "def".equals(lexicals.get(st + 1) + "") && 
        ";".equals(lexicals.get(en) + "")) 
    { String attname = lexicals.get(st + 3) + ""; 
      if ("(".equals(lexicals.get(st + 4) + ""))
      { // helper operation of the ATL module
        for (int pp = st+4; pp < en; pp++) 
        { String pps = lexicals.get(pp) + ""; 
          if (pps.equals("="))
          { for (int qq = pp-1; qq > st+7; qq--) 
            { String qqs = lexicals.get(qq) + ""; 
              if (qqs.equals(":"))
              { Vector params = parseAttributeDecs(st+4,qq-2,entities,types);
                Type atType = parseType(qq+1,pp-1,entities,types); 
                Expression defn = parse_ATLexpression(0,pp+1,en-1,entities,types); 
                BehaviouralFeature bf = new BehaviouralFeature(attname,params,true,atType); 
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

      // static attribute of the module 
      for (int rr = st+5; rr < en; rr++) 
      { String rrs = lexicals.get(rr) + ""; 
        if (rrs.equals("="))
        { Type hrestype = parseType(st+5,rr-1,entities,types); 
          Expression hexp = parse_ATLexpression(0,rr+1,en-1,entities,types); 
          Attribute att = new Attribute(attname,hrestype,ModelElement.INTERNAL); 
          att.setInitialExpression(hexp); 
          mod.addAttribute(att);
          // att.setOwner(mod); 
          att.setStatic(true); 
          return null;
        }  
      } 
      return null; 
    } 
    else 
    { return null; }  

    System.out.println("Parsing rule " + rname); 

    if ("(".equals(lexicals.get(st+2) + ""))
    { for (int d = st + 2; d < en; d++)
      { String sd = lexicals.get(d) + "";
        if (sd.equals("{"))
        { Vector params = parseAttributeDecs(st+3,d-2,entities,types);
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


    for (int i = start; i < en; i++) 
    { if ("to".equals(lexicals.get(i) + "")) 
      { InPattern fromClause = parseFromClause(start, i - 1, entities, types); 
        for (int j = i; j < en; j++) 
        { if ("do".equals(lexicals.get(j) + "") && "{".equals(lexicals.get(j+1) + "") && 
              "}".equals(lexicals.get(en-1) + "") )
          { OutPattern toClause = parseToClause(i, j - 1, entities, types); 
            Statement stat = parseATLStatement(j+2, en-2,entities,types);
            MatchedRule mr = new MatchedRule(lazy, unique); 
            mr.setInPattern(fromClause); 
            mr.setOutPattern(toClause);
            System.out.println("Action block: " + stat); 
            mr.setActionBlock(stat);  
            mr.setName(rname); 
            return mr;
          } 
        }  
        OutPattern toClause = parseToClause(i, en - 1, entities, types); 
        MatchedRule mr = new MatchedRule(lazy, unique); 
        mr.setInPattern(fromClause); 
        mr.setOutPattern(toClause); 
        mr.setName(rname); 
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

    // System.out.println("Parsing from clause"); 
      
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

    System.out.println("Parsing to clause"); 
      
    OutPattern res = new OutPattern(); 
    Vector patts = parse_to_clause(st + 1, dec_end, entities, types); 
    res.setElements(patts); 
    return res; 
  } 


  public Vector parse_to_clause(int st, int en, Vector entities, Vector types)
  { Vector res = new Vector(); 
    if (st >= en) { return res; } 

    if (":".equals(lexicals.get(st + 1) + "") && 
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
            { g = parse_expression(0,i+3,j); 
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
      Expression whenexp = parse_expression(0,start,en); 
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
      Expression whenexp = parse_expression(0,start,en); 
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
        { whenexp = parse_expression(0,start,j-1); 
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
          { whenexp = parse_expression(0,start,j-1); 
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
        { Expression whenexp = parse_expression(0,start,j-1); 
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
      Expression whenexp = parse_expression(0,start,en); 
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
        { Expression val = parse_basic_expression(bc,prev+2,prev+2);
          pti.setValue(val);
          res.add(pti);
        }
        else // an object template
        { ObjectTemplateExp te = (ObjectTemplateExp) parse_template_exp(bc,prev+2,i-1,entities,types); 
          if (te != null) 
          { pti.setTemplate(te); } 
          else 
          { Expression ve = parse_expression(bc, prev+2, i-1); 
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
    { Expression val = parse_basic_expression(bc,prev+2,prev+2);
      pti.setValue(val);
      res.add(pti);
    }
    else // an object template
    { ObjectTemplateExp te = (ObjectTemplateExp) parse_template_exp(bc,prev+2,en-1,entities,types); 
      if (te != null) 
      { pti.setTemplate(te); } 
      else 
      { Expression ve = parse_expression(bc, prev+2, en-1); 
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

          
  

  public static void main(String[] args)
  { // System.out.println(Double.MAX_VALUE); 

     Compiler2 c0 = new Compiler2(); 
    // c0.nospacelexicalanalysis("s : Set(E) { x ++ y }"); 
    // System.out.println(c0.lexicals);

      // c0.nospacelexicalanalysis("m->restrict(Set{1})");
      c0.nospacelexicalanalysis("w/(h*h)");  
      int en = c0.lexicals.size(); 
      Expression e = c0.parseExpression(); 

      System.out.println(e);  
      System.out.println(((BinaryExpression) e).getRight().needsBracket); 

      e.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 
      String str = e.queryForm(new java.util.HashMap(), true); 
      System.out.println(str); 

      Compiler2 c1 = new Compiler2(); 
      c1.nospacelexicalanalysis("m->keys()"); 
      int ex = c1.lexicals.size(); 
      Expression eee = c1.parseExpression(); 
      System.out.println(eee); 
      eee.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 
      System.out.println(eee.queryFormCSharp(new java.util.HashMap(), true)); 

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

