import java.io.*; 
import java.util.Vector;

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
*/ 

public class Compiler
{ static final int INUNKNOWN = 0; 
  static final int INBASICEXP = 1; 
  static final int INSYMBOL = 2; 
  static final int INSTRING = 3; 

  Vector lexicals; 
  Expression finalexpression; 

  /* This method breaks up a string into a sequence of strings, using 
     spaces as the break points:  */ 

  public void lexicalanalysis(String str) 
  { 
    boolean intoken = false;  /* True if a token has been recognised */ 
    boolean instring = false;  // true if within a string
    

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i);

      if (intoken)
      { if (instring)
        { if (c == '"')
          { sb.append(c); instring = false; 
            // intoken = false;
          }
          else 
          { sb.append(c); }
        }
        else  // intoken, not instring
        { if (c == '"')
          { instring = true; 
            // sb = new StringBuffer();
            // lexicals.addElement(sb); 
            sb.append(c);
          }
          else if (c == ' ' || c == '\n')
          { intoken = false; }
          else 
          { sb.append(c); }
        }
      }
      else // not intoken
      { if (c == '"')
        { intoken = true; 
          instring = true; sb = new StringBuffer();
          lexicals.addElement(sb); sb.append(c);
        }
        else if (c == ' ' || c == '\n')
        { intoken = false; }
        else 
        { intoken = true; sb = new StringBuffer();
          lexicals.addElement(sb); sb.append(c);
        }
      }
    }
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

  private boolean isBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || c == '(' || c == ')' ||
            c == '{' || c == '}' || c == '[' || c == ']' || c == ',');
  } 

  private boolean isSymbolCharacter(char c)
  { return (c == '<' || c == '>' || c == '=' || c == '*' || c == '/' || c == '-' ||
            c == '\'' || c == '+' || c == '&' || c == '^' || c == ':' || c == '|' ||
            c == '#'); 
  } 
        

  public void nospacelexicalanalysis(String str) 
  { int in = INUNKNOWN; 
    char previous; 

    int explen = str.length(); 
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 
      if (in == INBASICEXP)
      { if (isBasicExpCharacter(c) || c == '"')
        { sb.append(c); }              // carry on adding to current basic exp
        else if (isSymbolCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
        }
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          in = INUNKNOWN; 
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
          sb.append(c); 
        }
        else if (isBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for basic exp
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }
      }
      previous = c; 
    }
  }

  public void displaylexs()
  { 
    for (int i = 0; i < lexicals.size(); i++)
    { 
      System.out.println(((StringBuffer) lexicals.elementAt(i)).toString()); 
    } 
  }

  public Expression parse() 
  { 
    Expression ee = parse_expression(0,lexicals.size()-1); 
    finalexpression = ee; 
    return ee; 
  } 

  public Expression parse_expression(int pstart, int pend)
  { 
    Expression ee = parse_bracketed_expression(pstart,pend); 
    if (ee == null) 
    { ee = parse_binary_expression(pstart,pend); 
      if (ee == null) 
      { System.out.println("Failed to parse binary expression " + pstart + 
                           ".." + pend + " -- trying basic");
      
        ee = parse_basic_expression(pstart,pend);
        return ee; 
      }
    }
    return ee; 
  }

  public Expression parse_binary_expression(int pstart,
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
  }   

  public BinaryExpression parse_implies_expression(int pstart, 
                                                   int pend)
  { 
    Expression e1 = null; 
    Expression e2 = null;
    boolean found = false; 

    System.out.println("Trying to parse implies expression"); 

    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 

      System.out.println(ss); 

      if (ss.equals("=>") || ss.equals("<=>") || ss.equals("#"))
      { 
        e1 = parse_binary_expression(pstart,i-1); 
        e2 = parse_binary_expression(i+1,pend); 
        if (e1 == null || e2 == null)
        { return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed implies expression: " + ee); 
          return ee; }
      }
    }
    return null; 
  }
   
  public BinaryExpression parse_or_expression(int pstart, 
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
        { return null; }
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
  
    for (int i = pstart; i < pend; i++)
    {
      String ss = lexicals.elementAt(i).toString();
      if (ss.equals("&"))
      {
        e1 = parse_binary_expression(pstart,i-1);
        e2 = parse_binary_expression(i+1,pend);
        if (e1 == null || e2 == null)
        {  } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed and expression: " + ee);
          return ee; 
        }
      }
    }
    return null;
  }


  public BinaryExpression parse_eq_expression(int pstart, 
                                              int pend)
  { Expression e1 = null; 
    Expression e2 = null;

    System.out.println("Trying to parse eq. expression"); 

    for (int i = pstart; i < pend; i++)
    { 
      String ss = lexicals.elementAt(i).toString(); 
      if (ss.equals(":=") || ss.equals(":") || ss.equals("<:") ||
          ss.equals("/:") || ss.equals("/<:") ||
          Expression.comparitors.contains(ss))
      { e1 = parse_factor_expression(pstart,i-1); 
        e2 = parse_factor_expression(i+1,pend); 
        if (e1 == null || e2 == null)
        { return null; }
        else 
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed equality expression: " + ee); 
          return ee; 
        }
      }
    }
    return null; 
  }

  public Expression parse_factor_expression(int pstart, int pend)
  { System.out.println("Trying to parse factor expression"); 
    for (int i = pstart; i < pend; i++) 
    { String ss = lexicals.get(i).toString(); 
      if (ss.equals("+") || ss.equals("-") || ss.equals("/\\") || ss.equals("^") ||
          ss.equals("\\/") || ss.equals("*") || ss.equals("/") || ss.equals("|"))
      { Expression e1 = parse_basic_expression(pstart,i-1);
        Expression e2 = parse_factor_expression(i+1,pend);
        if (e1 == null || e2 == null)
        { } // return null; }
        else
        { BinaryExpression ee = new BinaryExpression(ss,e1,e2);
          System.out.println("Parsed factor expression: " + ee);
          return ee;
        } 
      }
    } 
    return parse_basic_expression(pstart,pend); 
  } // reads left to right, not putting priorities on * above +

  public Expression parse_basic_expression(int pstart, int pend)
  { 
    if (pstart == pend)
    { String ss = lexicals.elementAt(pstart).toString(); 
      BasicExpression ee = new BasicExpression(ss,0); 
      Expression ef = ee.checkIfSetExpression(); 

      // if (ef instanceof BasicExpression) 
      // { ((BasicExpression) ef).setPrestate(ee.getPrestate()); } 

      System.out.println("Parsed basic expression: " + ss + " " + ee + " " + ef); 
      return ef;
    }
    // if (pstart < pend && ")".equals(lexicals.get(pend) + "") && 
    //     !("(".equals(lexicals.get(pstart) + "")))
    // { return parse_call_expression(pstart,pend); } 
    return parse_bracketed_expression(pstart,pend); 
  }

  public Expression parse_call_expression(int pstart, int pend)
  { for (int i = pstart; i < pend; i++)
    { String ss = lexicals.get(i) + ""; 
      if ("(".equals(ss))
      { Expression opref = parse_basic_expression(pstart,i-1); 
        if (opref == null)  { return null; } 
        Vector args = parse_call_arguments(i+1,pend-1); 
         
        if (opref instanceof BasicExpression)
        { ((BasicExpression) opref).setParameters(args); } 
        System.out.println("parsed call expression: " + opref); 
        return opref; 
      } 
    }
    System.out.println("Failed to parse call expression"); 
    return null; 
  }

  public Vector parse_call_arguments(int st, int ed)
  { Vector res = new Vector(); 
    for (int i = st; i < ed; i++)
    { String ss = lexicals.get(i) + ""; 
      if (",".equals(ss))
      { Expression e1 = parse_basic_expression(st,i-1); 
        res = parse_call_arguments(i+1,ed); 
        res.add(0,e1);
        return res; 
      } 
    }
    return res; 
  } 
 

        

  public Expression parse_bracketed_expression(int pstart, int pend)
  { if (lexicals.size() > 2 &&
        lexicals.get(pstart).toString().equals("(") &&
        lexicals.get(pend).toString().equals(")"))
    { Expression eg = parse_expression(pstart+1,pend-1);
      System.out.println("parsed bracketed expression: " + eg);  
      if (eg != null)
      { eg.setBrackets(true); }
      return eg; 
    }
    return null; 
  }
 
  public Statement parseStatement(int s, int e)
  { Statement st = parseSequenceStatement(s,e);
    if (st == null)
    { st = parseLoopStatement(s,e); }
    if (st == null)
    { st = parseConditionalStatement(s,e); }
    if (st == null)
    { st = parseBasicStatement(s,e); }
    return st;
  }


  public Statement parseSequenceStatement(int s, int e)
  { Statement s1 = null;
    Statement s2 = null;
    for (int i = s; i < e; i++)
    { String ss = lexicals.get(i) + "";
      if (ss.equals(";"))
      { s1 = parseStatement(s,i-1);
        s2 = parseStatement(i+1,e);
        if (s1 == null || s2 == null) { return null; }
        else
        { SequenceStatement res = new SequenceStatement();
          res.addStatement(s1);
          res.addStatement(s2);
          System.out.println("Parsed ; statement: " + res);
          return res;
        }
      }
    }
    return null;
  }

  public Statement parseLoopStatement(int s, int e)
  { if ("for".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("do"))
        { test = parse_expression(s+1,i-1);
          s1 = parseStatement(i+1, e);
          if (s1 == null || test == null)
          { return null; }
          WhileStatement res = new WhileStatement(test,s1);
          res.setLoopKind(Statement.FOR); 
          if (test instanceof BinaryExpression)
          { BinaryExpression betest = (BinaryExpression) test; 
            if (":".equals(betest.getOperator()))
            { res.setLoopRange(betest.getLeft(), betest.getRight()); } 
          }
          System.out.println("Parsed for statement: " + res);
          return res;
        }
      }
    }
    return null;
  }

  public Statement parseConditionalStatement(int s, int e)
  { if ("if".equals(lexicals.get(s) + ""))
    { Statement s1 = null;
      Statement s2 = null;
      Expression test = null;
      for (int i = s+1; i < e; i++)
      { String ss = lexicals.get(i) + "";
        if (ss.equals("then"))
        { test = parse_expression(s+1,i-1);
          for (int j = i+1; j < e; j++)
          { String ss1 = lexicals.get(j) + "";
            if (ss1.equals("else"))
            { s1 = parseStatement(i+1, j-1);
              s2 = parseBasicStatement(j+1,e);
              if (s1 == null || s2 == null || test == null)
              { return null; }
              IfStatement res; 
              if (s2.isSkip())
              { res = new IfStatement(test,s1); } 
              else 
              { res = new IfStatement(test,s1,s2); } 
              System.out.println("Parsed if statement: " + res);
              return res;
            }
          }
        }
      }
    }
    return null;
  }



  public Statement parseBasicStatement(int s, int e)
  { if (e == s)
    { if ("skip".equals(lexicals.get(e) + ""))
      { return new InvocationStatement("skip",null,null); }
      else // operation call
      { Expression ee = parse_basic_expression(s,e);
        InvocationStatement istat = new InvocationStatement(ee + "",null,null);
        istat.setCallExp(ee); 
        return istat; 
      }
    }
    if ("(".equals(lexicals.get(s) + "") &&
        ")".equals(lexicals.get(e) + ""))
    { return parseStatement(s+1,e-1); }  // set brackets
    else 
    { for (int i = s; i < e; i++) 
      { String ss = lexicals.get(i) + ""; 
        if (":=".equals(ss))
        { Expression e1 = parse_expression(s,i-1); 
          Expression e2 = parse_expression(i+1,e); 
          if (e1 == null || e2 == null)
          { return null; } 
          System.out.println("Parsed assignment: " + e1 + " := " + e2); 
          return new AssignStatement(e1,e2); 
        } 
      }
    }
    return null;
  }

  public Statement parseStatement()
  { return parseStatement(0,lexicals.size()-1); }

  public static void main(String[] args)
  { Compiler c = new Compiler();
    c.lexicalanalysis("( kind = initial => n : InitialNode # n.name1 = name ) & ( kind = join => n : JoinNode # n.name1 = name ) & ( kind = fork => n : ForkNode # n.name1 = name ) & ( kind = junction => ( incoming.size = 1 => n : DecisionNode # n.name1 = name ) & ( incoming.size > 1 => n : MergeNode # n.name1 = name ) )");
    Expression ss = c.parse(); 
    if (ss != null) 
    { ss.display(); }
  }

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

