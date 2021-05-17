import java.util.Vector; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class ASTCompositeTerm extends ASTTerm
{ String tag = ""; 
  Vector terms = new Vector(); // of ASTTerm

  public ASTCompositeTerm(String t, Vector subtrees)
  { tag = t; 
    terms = subtrees; 
  } 

  public void addTerm(ASTTerm t) 
  { terms.add(t); } 

  public String toString()
  { String res = "(" + tag; 
    for (int i = 0; i < terms.size(); i++) 
    { res = res + " " + terms.get(i); } 
    res = res + ")"; 
    return res; 
  } 

  public String cg(CGSpec cgs)
  { // Find the cgs rules r for tag, match the terms to 
    // the r LHS, then apply first rule to the cg results of 
    // the LHS matchings. 

    Vector rules = cgs.getRulesForCategory(tag); 
    for (int i = 0; i < rules.size(); i++) 
    { CGRule r = (CGRule) rules.get(i);
      Vector tokens = r.lhsTokens; 
      Vector vars = r.getVariables(); 

      if (tokens.size() != terms.size())
      { System.out.println("> " + tag + " rule " + r + " does not match " + this);  
        System.out.println(" Different numbers of elements on rule LHS (" + tokens.size() + ") to subterms: (" + terms.size() + ")"); 
        continue; 
      } 

      System.out.println("> Trying to match tokens of rule " + r + " for " + this);  
        
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
          eargs.add(terms.get(j)); 
          k++; 
        } 
        else if (tok.equals(terms.get(j) + ""))
        { } 
        else 
        { System.out.println("> " + tag + " rule " + r + " does not match " + this); 
          System.out.println(tok + " /= " + terms.get(j)); 
          failed = true; // try next rule 
        } 
      } 

      if (failed == false) 
      { System.out.println("> Matched " + tag + " rule " + r + " for " + this);  

        for (int p = 0; p < eargs.size(); p++)
        { ASTTerm term = (ASTTerm) eargs.get(p); 
          String textp = term.cg(cgs); 
          args.add(textp); 
        } 

        Vector ents = new Vector(); 

        if (r.satisfiesConditions(eargs,ents))
        { return r.applyRule(args,eargs,cgs); }  
      }
    }  
    return toString(); 
  }


  public boolean isAssignment()
  { if ("expression".equals(tag) && terms.size() == 3)
    { ASTTerm op = (ASTTerm) terms.get(1);
      if ("=".equals(op))
      { return true; }  
    }
    return false;
  } 

  public String toKM3Assignment()
  { if (terms.size() == 3) // BinaryExpression
    { ASTTerm op = (ASTTerm) terms.get(1); 
      ASTTerm e1 = (ASTTerm) terms.get(0);
      ASTTerm e2 = (ASTTerm) terms.get(2);
      return e1.toKM3() + " := " + e2.toKM3(); 
    } 
    return toKM3(); 
  } 

  public String toKM3Test()
  { if (tag.equals("forControl") && terms.size() > 2)
    { ASTTerm test = (ASTTerm) terms.get(2);
	  return test.toKM3(); 
	} 
	ASTTerm test = (ASTTerm) terms.get(0);
    return test.toKM3(); 
  } 

  public String toKM3Init()
  { if (tag.equals("forControl"))
    { ASTCompositeTerm init = (ASTCompositeTerm) terms.get(0);
	  return init.toKM3Init(); 
	} 
	else if (tag.equals("forInit"))
	{ ASTTerm init = (ASTTerm) terms.get(0);
	  return init.toKM3(); 
	}  
    return null; 
  } 

  public String toKM3Incr()
  { if (tag.equals("forControl") && terms.size() > 4)
    { ASTTerm incr = (ASTTerm) terms.get(4);
      return incr.toKM3();
    } 
    return null;  
  } 

  public String toKM3Var()
  { if ("variableDeclarators".equals(tag))
    { ASTCompositeTerm vd1 = (ASTCompositeTerm) terms.get(0);
      return vd1.toKM3Var();
    } 
    if ("variableDeclarator".equals(tag))
    { ASTTerm var = (ASTTerm) terms.get(0); 
      return var.toKM3(); 
    } 
    return null;  
  } 

  public String toKM3VarInit()
  { if ("variableDeclarators".equals(tag))
    { ASTCompositeTerm vd1 = (ASTCompositeTerm) terms.get(0);
      return vd1.toKM3VarInit();
    } 
    if ("variableDeclarator".equals(tag) && terms.size() > 2)
    { ASTTerm var = (ASTTerm) terms.get(2); 
      return var.toKM3(); 
    } 
    return null;  
  } 

  public Vector getCallArguments(ASTTerm t) 
  { Vector res = new Vector(); 
    if (t instanceof ASTCompositeTerm) 
    { ASTCompositeTerm tt = (ASTCompositeTerm) t; 
      if ("expressionList".equals(tt.tag))
      { for (int i = 0; i < tt.terms.size(); i++) 
        { ASTTerm telem = (ASTTerm) tt.terms.get(i); 
          if (telem instanceof ASTSymbolTerm) { } 
          else 
          { res.add(telem); } 
        } 
      } 
    } 
    return res; 
  } 

  public String featureAccess(ASTTerm arg, ASTTerm call, String args, String calls)
  { if (call instanceof ASTCompositeTerm)
    { ASTCompositeTerm callterm = (ASTCompositeTerm) call; 
      if (callterm.tag.equals("methodCall"))
      { Vector callterms = callterm.terms; 
        String called = callterms.get(0) + "";
 
        if ("max".equals(called) && "Math".equals(args))
        { ASTTerm callargs = (ASTTerm) callterms.get(2);
          Vector cargs = getCallArguments(callargs); 
          ASTTerm callarg1 = (ASTTerm) cargs.get(0); 
          ASTTerm callarg2 = (ASTTerm) cargs.get(1);
          String callp1 = callarg1.toKM3(); 
          String callp2 = callarg2.toKM3(); 

          return "Set{" + callp1 + ", " + callp2 + "}->max()"; 
        }  
        else if ("min".equals(called) && "Math".equals(args))
        { ASTTerm callargs = (ASTTerm) callterms.get(2);
          Vector cargs = getCallArguments(callargs); 
          ASTTerm callarg1 = (ASTTerm) cargs.get(0); 
          ASTTerm callarg2 = (ASTTerm) cargs.get(1);
          String callp1 = callarg1.toKM3(); 
          String callp2 = callarg2.toKM3(); 

          return "Set{" + callp1 + ", " + callp2 + "}->min()"; 
        }  
        else if ("put".equals(called) && callterms.size() >= 3)
        { ASTTerm callargs = (ASTTerm) callterms.get(2);
          Vector cargs = getCallArguments(callargs); 
          ASTTerm callarg1 = (ASTTerm) cargs.get(0); 
          ASTTerm callarg2 = (ASTTerm) cargs.get(1);
          String callp1 = callarg1.toKM3(); 
          String callp2 = callarg2.toKM3(); 

          return args + "[" + callp1 + "] := " + callp2; 
        }  
        else if ("add".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + " := " + args + "->including(" + callp + ")"; 
        }
        else if ("addAll".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + " := " + args + "->union(" + callp + ")"; 
        }
        else if ("charAt".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + "[" + callp + " + 1]"; 
        }
        else if ("indexOf".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return "(" + args + "->indexOf(" + callp + ") - 1)"; 
        }
        else if ("substring".equals(called))
        { ASTTerm callargs = (ASTTerm) callterms.get(2);
          Vector cargs = getCallArguments(callargs); 
          ASTTerm callarg1 = (ASTTerm) cargs.get(0); 
          ASTTerm callarg2 = (ASTTerm) cargs.get(1);
          String callp1 = callarg1.toKM3(); 
          String callp2 = callarg2.toKM3(); 

          return args + ".subrange(" + callp1 + " + 1, " + callp2 + ")"; 
        }  
        else if ("concat".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return "(" + args + " + " + callp + ")"; 
        }
        else if ("endsWith".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + "->endsWith(" + callp + ")"; 
        }
        else if ("equals".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + " = " + callp; 
        }
        else if ("equalsIgnoreCase".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return "(" + args + "->toLowerCase() = " + callp + "->toLowerCase())"; 
        }
        else if ("removeAll".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + " := " + args + " - " + callp; 
        }
        else if ("println".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return "FileManager.writeln(" + args + ", " + callp + ")"; 
        }
        else if ("startsWith".equals(called))
        { ASTTerm callarg = (ASTTerm) callterms.get(2); 
          String callp = callarg.toKM3(); 
          return args + "->startsWith(" + callp + ")"; 
        }
        else if ("length".equals(called))
        { // ASTTerm callarg = (ASTTerm) callterms.get(2); 
          // String callp = callarg.toKM3(); 
          return args + "->size()"; 
        }
        else if ("toString".equals(called))
        { // ASTTerm callarg = (ASTTerm) callterms.get(2); 
          // String callp = callarg.toKM3(); 
          return "(" + args + " + \"\")"; 
        }
      }
    }

    if ("System".equals(args) && "out".equals(calls))
    { return "\"System.out\""; } 
    if ("System".equals(args) && "in".equals(calls))
    { return "\"System.in\""; } 
    if ("System".equals(args) && "err".equals(calls))
    { return "\"System.err\""; } 

    return args + "." + calls; 
  } 



  public String toKM3()
  { if ("creator".equals(tag))
    { // 2 arguments
      ASTTerm cls = (ASTTerm) terms.get(0); 
      String clsname = cls.toKM3(); 

      ASTTerm args = (ASTTerm) terms.get(1);
      if (args instanceof ASTCompositeTerm && 
          "arrayCreatorRest".equals(((ASTCompositeTerm) args).tag))
      { ASTCompositeTerm argsterm = (ASTCompositeTerm) args; 
        ASTTerm sze = (ASTTerm) argsterm.terms.get(1);
        String defaultValue = getDefaultValue(clsname);  
        return "Integer.subrange(1," + sze.toKM3() + ")->collect(" + defaultValue + ")"; 
      } 

      String args1 = args.toKM3(); 
      return "create" + clsname + args1; 
    } 

    if ("createdName".equals(tag))
    { ASTTerm mainclass = (ASTTerm) terms.get(0);
      return mainclass.toKM3(); 
    } // Ignore type parameters in creation. 

    if ("expression".equals(tag))
    { System.out.println(">> Expression with " + terms.size() + " terms " + terms);
      for (int y = 0; y < terms.size(); y++)
      { ASTTerm yt = (ASTTerm) terms.get(y); 
        System.out.println(">>> Term " + y + ": " + yt); 
      }  
      System.out.println(); 

      if (terms.size() == 1) // Identifier or literal
      { ASTTerm t = (ASTTerm) terms.get(0); 
        return t.toKM3(); 
      } 

      if (terms.size() == 2) // UnaryExpression
      { ASTTerm op = (ASTTerm) terms.get(0); 
        ASTTerm arg = (ASTTerm) terms.get(1);

        if ("new".equals(op + ""))
        { return arg.toKM3(); } 

        String op1 = op.toKM3(); 
        String arg1 = arg.toKM3(); 

        if ("++".equals(arg1))
        { return op1 + " := " + op1 + " + 1"; } 
        if ("--".equals(arg1))
        { return op1 + " := " + op1 + " - 1"; } 
        if ("++".equals(op1))
        { return arg1 + " := " + arg1 + " + 1"; } 
        if ("--".equals(op1))
        { return arg1 + " := " + arg1 + " - 1"; } 
        return op.toKM3() + arg.toKM3(); 
      }  

      if (terms.size() == 3) // BinaryExpression
      { ASTTerm op = (ASTTerm) terms.get(1); 
        ASTTerm e1 = (ASTTerm) terms.get(0);
        ASTTerm e2 = (ASTTerm) terms.get(2);
        String opx = op.toKM3(); 
        String e1x = e1.toKM3(); 
        String e2x = e2.toKM3();
 
        if ("+=".equals(op + ""))
        { return e1x + " := " + e1x + " + " + e2x; } 
        if ("*=".equals(op + ""))
        { return e1x + " := " + e1x + " * " + e2x; } 
        if ("/=".equals(op + ""))
        { return e1x + " := " + e1x + " / " + e2x; } 
        if ("-=".equals(op + ""))
        { return e1x + " := " + e1x + " - " + e2x; } 
        if ("^=".equals(op + ""))
        { return e1x + " := " + e1x + " xor " + e2x; } 
        if ("&=".equals(op + ""))
        { return e1x + " := " + e1x + " & " + e2x; } 
        if ("|=".equals(op + ""))
        { return e1x + " := " + e1x + " or " + e2x; } 
        if ("%=".equals(op + ""))
        { return e1x + " := " + e1x + " mod " + e2x; } 
        if ("=".equals(op + ""))
        { return e1x + " := " + e2x; } 
        if (".".equals(op + ""))
        { return featureAccess(e1,e2,e1x,e2x); } 

        return e1.toKM3() + op.toKM3() + e2.toKM3(); 
      }  

      if (terms.size() == 4 && 
          "[".equals(terms.get(1) + "") && 
          "]".equals(terms.get(3) + "")) // array access
      { ASTTerm arr = (ASTTerm) terms.get(0); 
        ASTTerm ind = (ASTTerm) terms.get(2);

        String arrx = arr.toKM3(); 
        String indx = ind.toKM3(); 
        return arrx + "[" + indx + " + 1]";
      } // It must be indexed by integers. Not a map. 


      if (terms.size() == 5 && "?".equals(terms.get(1) + ""))
      { // ConditionalExpression
        ASTTerm cond = (ASTTerm) terms.get(0); 
        ASTTerm ifoption = (ASTTerm) terms.get(2);
        ASTTerm elseoption = (ASTTerm) terms.get(4);
        String condx = cond.toKM3(); 
        String ifx = ifoption.toKM3(); 
        String elsex = elseoption.toKM3();
        return "if " + condx + " then " + ifx + " else " + elsex + " endif"; 
      } 
    } 

    if ("statement".equals(tag))
    { System.out.println(">> Statement with " + terms.size() + " terms "); 
      for (int h = 0; h < terms.size(); h++) 
      { System.out.println("Term " + h + ": " + terms.get(h)); } 
      System.out.println(); 

      if (terms.size() == 1) // Single statement
      { ASTTerm t = (ASTTerm) terms.get(0); 
        return t.toKM3(); 
      } 
      else if (terms.size() >= 2 && "throw".equals(terms.get(0) + "")) 
      { ASTTerm t = (ASTTerm) terms.get(1);
        return "error " + t.toKM3(); 
      }
      else if (terms.size() == 2) // Return, break, continue or expression statement
      { ASTTerm t = (ASTTerm) terms.get(0);
        if (t.isAssignment()) 
        { return t.toKM3Assignment(); } 
        return t.toKM3(); 
      }
      else if (terms.size() > 2 && "if".equals(terms.get(0) + ""))
      { ASTTerm texpr = (ASTTerm) terms.get(1);
        String res = "if " + texpr.toKM3() + " then "; 
        for (int i = 2; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + tt.toKM3();
        } 
        return res; 
      } 
      else if (terms.size() > 2 && "try".equals(terms.get(0) + ""))
      { ASTTerm tbody = (ASTTerm) terms.get(1);
        String res = "try " + tbody.toKM3() + "\n";
        for (int i = 2; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + tt.toKM3();
        } 
        return res; 
      } 
      else if (terms.size() > 2 && "switch".equals(terms.get(0) + ""))
      { ASTTerm ttest = (ASTTerm) terms.get(1); 
        String res = "switch " + ttest.toKM3() + "\n";
        for (int i = 2; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + tt.toKM3();
        } 
        return res; 
      } 
      else if (terms.size() > 2 && "while".equals(terms.get(0) + ""))
      { ASTTerm texpr = (ASTTerm) terms.get(1);
        String res = "while " + texpr.toKM3() + " do "; 
        for (int i = 2; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + tt.toKM3();
        } 
        return res; 
      } 
      else if (terms.size() > 3 && "do".equals(terms.get(0) + ""))
      { ASTTerm stat = (ASTTerm) terms.get(1);
        String statcode = stat.toKM3();  
        ASTTerm texpr = (ASTTerm) terms.get(3);
        String res = "  " + statcode + " ;\n" + 
          "  while " + texpr.toKM3() + "\n  do\n" + 
          "    " + statcode; 
        return res; 
      } 
      else if (terms.size() > 2 && "for".equals(terms.get(0) + ""))
      { ASTCompositeTerm forControl = (ASTCompositeTerm) terms.get(2);
        String tst = forControl.toKM3Test(); 
        String init = forControl.toKM3Init(); 
        String incr = forControl.toKM3Incr(); 
		
        String res = "  "; 
        if (init != null) 
        {  res = res + init + " ;\n  "; } 
		
        res = res + "for " + tst + "\n  do\n  ( ";
		 
        for (int i = 4; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + tt.toKM3();
        } // but could be empty. Also, any continue
          // must be preceded by incr.  
		
        if (incr == null) 
        { return res + " )"; }
        res = res + " ; \n  " + forControl.toKM3Incr() + "\n" + 
        "  )";  
        
        return res; 
      } 
      else if (terms.size() > 3 && "assert".equals(terms.get(0) + ""))
      { ASTTerm expr = (ASTTerm) terms.get(1);
        ASTTerm mess = (ASTTerm) terms.get(3);
        String res = "  assert " + expr.toKM3() + " do ExceptionManager.createEnvironmentException(\"AssertionError\"," + mess.toKM3() + ")"; 
        return res; 
      } 
      else if (terms.size() > 2 && "return".equals(terms.get(0) + ""))
      { ASTTerm texpr = (ASTTerm) terms.get(1);
        String res = "return " + texpr.toKM3() + " "; 
        return res; 
      } 
    }  

    if ("catchClause".equals(tag))
    { ASTTerm ctest = (ASTTerm) terms.get(2); 
      ASTTerm cvar = (ASTTerm) terms.get(3); 
      ASTTerm cbody = (ASTTerm) terms.get(5); 
      return "  catch " + cvar.toKM3() + " : " + ctest.toKM3() + " do " + cbody.toKM3() + "\n"; 
    } 

    if ("finallyBlock".equals(tag))
    { ASTTerm fbody = (ASTTerm) terms.get(1); 
      return "  endtry " + fbody.toKM3() + "\n"; 
    } 

    if ("switchBlockStatementGroup".equals(tag))
    { ASTTerm ctest = (ASTTerm) terms.get(0); 
      if (terms.size() > 1)
      { ASTTerm code = (ASTTerm) terms.get(1); 
        return ctest.toKM3() + " do " + code.toKM3() + "\n";
      }  
      return ctest.toKM3() + " do skip\n";
    } 

    if ("switchLabel".equals(tag))
    { if ("case".equals(terms.get(0) + ""))
      { ASTTerm test = (ASTTerm) terms.get(1); 
        // if (terms.size() > 2)
        // { ASTTerm code = (ASTTerm) terms.get(2); 
        //   return "  case " + test.toKM3() + " do " + code.toKM3() + "\n"; 
        // } 
        return "  case " + test.toKM3(); // + " do skip\n"; 
      }
      else if ("default".equals(terms.get(0) + ""))
      { // if (terms.size() > 1)
        // { ASTTerm code = (ASTTerm) terms.get(1); 
        //   return "  endswitch " + code.toKM3() + "\n"; 
        // } 
        return "  endswitch"; // do skip\n"; 
      } 
    }
   
    if ("arrayInitializer".equals(tag))
    { String res = "Sequence{"; 
      for (int i = 0; i < terms.size(); i++) 
      { ASTTerm tt = (ASTTerm) terms.get(i); 
        res = res + tt.toKM3();
      } 
      res = res + "}"; 
      return res; 
    } 
      

    if ("localVariableDeclaration".equals(tag) && terms.size() > 1)
    { ASTTerm typeTerm = (ASTTerm) terms.get(0); 
      ASTCompositeTerm varTerm = (ASTCompositeTerm) terms.get(1); 
      String km3type = typeTerm.toKM3(); 
      String km3var = varTerm.toKM3Var(); 
      String km3init = varTerm.toKM3VarInit(); 
      String res = "var " + km3var + " : " + km3type; 
      if (km3init != null) 
      { res = res + " := " + km3init; }  
      return res; 
    }   

    if ("fieldDeclaration".equals(tag) && terms.size() > 1)
    { ASTTerm typeTerm = (ASTTerm) terms.get(0); 
      ASTCompositeTerm varTerm = (ASTCompositeTerm) terms.get(1); 
      String km3type = typeTerm.toKM3(); 
      String km3var = varTerm.toKM3Var(); 
      String km3init = varTerm.toKM3VarInit(); 
      String res = "  attribute " + km3var + " : " + km3type; 
      // if (km3init != null) 
      // { res = res + " := " + km3init; }  
      return res + ";\n"; 
    }   

    if ("typeType".equals(tag))
    { if (terms.size() >= 3 && 
          "[".equals(terms.get(1) + "") &&
          "]".equals(terms.get(2) + ""))
      { String tt = ((ASTTerm) terms.get(0)).toKM3();
        for (int i = 1; i+1 < terms.size(); i = i + 2) 
        { if ((terms.get(i) + "").equals("[") &&
              (terms.get(i+1) + "").equals("]"))
          { tt = "Sequence(" + tt + ")"; } 
        } 
        return tt;  
      } 
    } 

    if ("classOrInterfaceType".equals(tag))
    { if (terms.size() > 1)  // A parameterised type 
      { ASTTerm baseType = (ASTTerm) terms.get(0); 
        ASTTerm typepars = (ASTTerm) terms.get(1); 
        String btype = baseType.toKM3();
        String pars = typepars.toKM3(); 
        return btype + "(" + pars + ")"; 
      }
      else 
      { ASTTerm t0 = (ASTTerm) terms.get(0);
        return t0.toKM3(); 
      } // should never occur
    } 
 
    if ("typeArguments".equals(tag) && terms.size() > 1)
    { // < arg >   or   < arg1 , arg2 >
      ASTTerm typepar0 = (ASTTerm) terms.get(1); 
      String res = typepar0.toKM3(); 
      if (terms.size() > 3)
      { ASTTerm typepar1 = (ASTTerm) terms.get(3); 
        res = res + ", " + typepar1.toKM3(); 
      } 
      return res; 
    } 

    if ("enhancedForControl".equals(tag))
    { ASTTerm typeTerm = (ASTTerm) terms.get(0); 
      ASTTerm varTerm = (ASTTerm) terms.get(1); 
	  ASTTerm rangeTerm = (ASTTerm) terms.get(3); 
      // String km3type = typeTerm.toKM3(); 
      String km3var = varTerm.toKM3(); 
      String km3range = rangeTerm.toKM3(); 
      String res = km3var + " : " + km3range; 
      // if (km3var != null) 
      // { res = res + " := " + km3init; }  
      return res; 
    }   

    if ("block".equals(tag))
    { System.out.println(">> Statement block with " + terms.size() + " terms " + terms); 

      if (terms.size() == 1) // Single statement
      { ASTTerm t = (ASTTerm) terms.get(0); 
        return t.toKM3(); 
      } 
      else // (terms.size() >= 2) // Series of statements
      { String res = "";
        int count = 0;  
        String prev = ((ASTTerm) terms.get(0)).toKM3();
          
        for (int i = 1; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          String next = tt.toKM3(); 
          if (prev.length() > 0) 
          { res = res + " " + prev; 
            count++; 
          }  
          if (prev.length() > 0 && next.length() > 0)
          { res = res + " ;\n  "; }
          prev = next; 
        }
 
        if (prev.length() > 0) 
        { res = res + " " + prev; 
          count++; 
        }  
          
        if (count > 1) 
        { res = " ( " + res + " )"; }
 
        return res + " ";  
      } 
    } 

    if ("methodDeclaration".equals(tag))
    { ASTTerm mtype = (ASTTerm) terms.get(0); 
      ASTTerm mname = (ASTTerm) terms.get(1);
      ASTTerm mparams = (ASTTerm) terms.get(2); 
 
      String res = "operation " + mname + mparams.toKM3() + " : " + mtype.toKM3() + "\n" + 
              "  pre: true\n" + "  post: true\n"; 
      if (terms.size() > 3)
      { res = res + "  activity:\n"; 
        for (int i = 3; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + "    " + tt.toKM3() + "\n"; 
        }
      }  
      return res + "  ;\n"; 
    }

    if ("constructorDeclaration".equals(tag))
    { ASTTerm mname = (ASTTerm) terms.get(0);
      ASTTerm mparams = (ASTTerm) terms.get(1); 
 
      String res = "\n  operation initialise" + mname + mparams.toKM3() + " : " + mname + "\n" + 
              "  pre: true\n" + "  post: true\n"; 
      if (terms.size() > 2)
      { res = res + "  activity:\n"; 
        for (int i = 2; i < terms.size(); i++) 
        { ASTTerm tt = (ASTTerm) terms.get(i); 
          res = res + "   " + tt.toKM3();
          if (i < terms.size() - 1) 
          { res = res + "\n"; }
          else 
          { res = res + ";\n"; }  
        }
      }  
      return res + "    return self;\n"; 
    }

    if ("formalParameter".equals(tag))
    { ASTTerm mtype = (ASTTerm) terms.get(0); 
      ASTTerm mname = (ASTTerm) terms.get(1);
      
      String res = mname.toKM3() + " : " + mtype.toKM3();  
      return res;
    }

    if ("enumDeclaration".equals(tag) && 
        terms.size() > 3)
    { ASTTerm ename = (ASTTerm) terms.get(1); 
      ASTTerm literals = (ASTTerm) terms.get(3); 
      return "enumeration " + ename +
             " {\n" + literals.toKM3() + "}"; 
    }  

    if ("modifier".equals(tag))
    { return ""; } 

    if ("enumConstants".equals(tag))
    { String res = ""; 
      for (int i = 0; i < terms.size(); i++) 
      { ASTTerm lit = (ASTTerm) terms.get(i); 
        if (",".equals(lit + "")) { } 
        else
        { res = res + "  literal " + lit.toKM3() +
                ";\n";
        } 
      } 
      return res;  
    }  

    if ("classDeclaration".equals(tag) && 
        terms.size() > 2)
    { ASTTerm ename = (ASTTerm) terms.get(1); 
      ASTTerm contents = (ASTTerm) terms.get(2); 
      return "class " + ename +
             " {\n" + contents.toKM3() + "}"; 
    }  


    if (terms.size() == 1) // Identifier or literal
    { ASTTerm t = (ASTTerm) terms.get(0); 
      return t.toKM3();     
    } 

    String res = "";  
    for (int i = 0; i < terms.size(); i++) 
    { ASTTerm tt = (ASTTerm) terms.get(i); 
      res = res + tt.toKM3(); 
    } 
    return res; 
  } 
} 