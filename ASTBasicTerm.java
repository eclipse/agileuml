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

import javax.swing.*;


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

  public String tagFunction()
  { return tag; } 

  public ASTTerm removeWhitespaceTerms()
  { String vtrim = value.trim(); 
    if (vtrim.equals("\\r\\n") || 
        vtrim.equals("\\r\\n\\r\\n") || 
        vtrim.equals("\\r\\n\\r\\n\\r\\n") || 
        vtrim.equals("\\r\\n\\r\\n\\r\\n\\r\\n"))
    { return null; } 

    if (vtrim.endsWith("\\r\\n\\r\\n\\r\\n"))
    { String str = vtrim.substring(0,vtrim.length()-12); 
      ASTTerm ntrm = new ASTBasicTerm(tag,str);
      return ntrm; 
    } 

    if (vtrim.endsWith("\\r\\n\\r\\n"))
    { String str = vtrim.substring(0,vtrim.length()-8); 
      ASTTerm ntrm = new ASTBasicTerm(tag,str);
      return ntrm; 
    } 

    if (vtrim.endsWith("\\n\\r") ||
        vtrim.endsWith("\\r\\n"))
    { String str = vtrim.substring(0,vtrim.length()-4); 
      ASTTerm ntrm = new ASTBasicTerm(tag,str);
      return ntrm; 
    } 

    return this; 
  }  

  public ASTTerm removeExtraNewlines()
  { return this; } 

  public ASTTerm replaceCobolIdentifiers()
  { if ("FILLER".equals(value))
    { ASTTerm.cobolFillerCount++; 
      return new ASTBasicTerm(tag, "FILLER_F" + ASTTerm.cobolFillerCount); 
    } 
    
    if (tag.equals("cobolWord") ||
        tag.equals("figurativeConstant") ||  
        tag.equals("specialRegister"))
    { String vtrim = value.trim(); 
      String vsub = 
        vtrim.replace("-", ASTTerm.cobolHyphenReplacement); 
      return new ASTBasicTerm(tag,vsub);
    } 

    if (tag.equals("numericLiteral"))
    { String vtrim = value.trim(); 
      String vsub = vtrim.replace(",", 
                      ASTTerm.cobolCommaReplacement);
      if (vsub.startsWith("."))
      { vsub = "0" + vsub; }  
      return new ASTBasicTerm(tag,vsub);
    } 

    return this; 
  }  

  public ASTTerm replaceAmbiguousCobolNames(Vector rnames)
  { return this; }

  public ASTTerm substituteEq(String str, ASTTerm newtrm)
  { if (str.equals(value))
    { return newtrm; }
    return this; 
  }  
        
  public boolean hasTag(String tagx) 
  { return tagx.equals(tag); } 

  public boolean hasSingleTerm() 
  { return true; } 

  public boolean isNestedSymbolTerm() 
  { return true; }  

  public Vector allNestedSubterms()
  { return new Vector(); }  

  public Vector allIdentifiers(Vector tags)
  { Vector res = new Vector(); 
    if (tags.contains(tag))
    { res.add(value); } 
    return res; 
  } 

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

  public Vector getTerms()
  { Vector res = new Vector(); 
    res.add(new ASTSymbolTerm(value));
    return res; 
  }  

  public Vector allNestedTagsArities()
  { Vector res = new Vector(); 
    Vector pair = new Vector(); 
    pair.add(tag); pair.add(1); 
    res.add(pair); 
    return res; 
  } 

  public Vector allTagsArities()
  { Vector res = new Vector(); 
    Vector pair = new Vector(); 
    pair.add(tag); pair.add(1); 
    res.add(pair); 
    return res; 
  } 

  public java.util.Set allTagsIn()
  { java.util.Set res = new java.util.HashSet(); 
    res.add(tag); 
    return res; 
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
  { return value; } 

  public String literalFormSpaces()
  { return value; } 

  public String evaluationLiteralForm()
  { return value; } 

  public Vector tokenSequence()
  { Vector res = new Vector(); 
    res.add("\"" + value + "\""); 
    return res; 
  } 

  public int termSize() 
  { return 1; } 

  public int size() 
  { return 1; } 



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

    ASTTerm term0 = getTerm(0); 

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
      // _* and _+ do not match. 

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
        { // allocate terms(0) to tok
          eargs.add(term0); 
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
      { 
        for (int p = 0; p < eargs.size(); p++)
        { // String textp = ((ASTTerm) eargs.get(p)).literalForm();
          ASTTerm term = (ASTTerm) eargs.get(p);  
          String textp = term.cg(cgs); 
             
          args.add(textp); 
        }

        Vector ents = new Vector(); 

        /* JOptionPane.showMessageDialog(null, 
           "Checking basic term rule " + r + " for " + 
           args + " " + eargs,   "",
           JOptionPane.INFORMATION_MESSAGE); */ 

        if (r.satisfiesAllConditions(args,eargs,ents,cgs)) 
             // r.satisfiesConditions(eargs,ents,cgs))
        { System.out.println(">>>> Applying basic term " + tag + " rule " + r + " for " + this); 
          return r.applyRule(args,eargs,cgs); 
        }  
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

  public java.util.Set allMathMetavariables() 
  { java.util.Set res = new java.util.HashSet(); 
    
    if (CSTL.isMathMetavariable(value))
    { res.add(value); } 
   
    return res; 
  } 

  public java.util.HashMap hasMatch(ASTTerm rterm, 
                                    java.util.HashMap res) 
  { return fullMatch(rterm,res); } 

  public java.util.HashMap fullMatch(ASTTerm rterm, 
                                     java.util.HashMap res) 
  { // This term matches to a schematic term rterm

    String rlit = rterm.literalForm(); 
    if (value.equals(rlit))
    { return res; } 

    // if (CSTL.isCSTLVariable(rlit))
    if (CSTL.isMathMetavariable(rlit))
    { ASTTerm oldterm = (ASTTerm) res.get(rlit); 
      if (oldterm == null)
      { res.put(rlit, this); 
        return res; 
      } 
      else if (value.equals(oldterm.literalForm()))
      { } 
      else 
      { return null; } 
    }

    return null; 
  }  

  public ASTTerm instantiate(java.util.HashMap res) 
  { // replace _i by res.get(_i)

    // if (CSTL.isCSTLVariable(value))
    if (CSTL.isMathMetavariable(value))
    { ASTTerm oldterm = (ASTTerm) res.get(value); 
      if (oldterm != null)
      { return oldterm; } 
    }

    return new ASTBasicTerm(tag,value); 
  }  

  public String getLabel()
  { return null; } 

  public boolean isLabeledStatement()
  { return false; } 

  public Vector cexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)

  { // if ("argumentExpressionList".equals(tag))
    Vector res = new Vector();
    return res;  
  }

  public Statement cpreSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Statement cpostSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return null; } 

  public Type pointersToRefType(String tname, Type m)
  { Type res = m; 

    if ("*".equals(value))
    { if ("char".equals(tname))
      { res = new Type("String", null); } 
      else if ("FILE".equals(tname))
      { res = new Type("OclFile", null); }
      else 
      { res = new Type("Ref", null); 
        res.setElementType(m);
      }  
      return res; 
    } 
    return m; 
  } 

  public String cdeclarationStorageClass()
  { if ("storageClassSpecifier".equals(tag))
    { return value; } 
    return null; 
  } 


  public Type cdeclarationToType(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { if ("typeSpecifier".equals(tag) || 
        "typedefName".equals(tag))
    { String tname = value; 
      if ("char".equals(tname) || "short".equals(tname))
      { return new Type("int", null); }
      if ("_Bool".equals(tname))
      { return new Type("boolean", null); }  
      if ("float".equals(tname))
      { return new Type("double", null); } 
      if ("int".equals(tname) || "long".equals(tname) || 
          "double".equals(tname) || "void".equals(tname))
      { return new Type(tname,null); } 
      if ("time_t".equals(tname) || "clock_t".equals(tname) ||
          "size_t".equals(tname) || "fpos_t".equals(tname))
      { return new Type("long",null); }  
      
    
      Entity tent = (Entity) ModelElement.lookupByName(
                                        tname, entities); 
      if (tent != null) 
      { return new Type(tent); } 

      Type t = (Type) ModelElement.lookupByName(tname,types); 
      if (t != null) 
      { return t; } 
    } 

    return null; 
  }

  public Vector cdeclaratorToModelElements(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { ModelElement mx = 
      cdeclaratorToModelElement(vartypes,varelemtypes,types,
                                entities); 
    Vector res = new Vector(); 
    if (mx != null) 
    { res.add(mx); } 
    return res; 
  } 

  public ModelElement cdeclaratorToModelElement(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { System.out.println(">>> BasicTerm declarator to ModelElement: (" + tag + " " + value + ")"); 

    if ("directDeclarator".equals(tag) || 
        "typedefName".equals(tag))
    { return new Attribute(value, new Type("OclAny", null), 
                           ModelElement.INTERNAL); 
    } 
    return null; 
  } 

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


  public Type deduceType()
  { if (Expression.isString(value))
    { return new Type("String",null); }
   
    if (Expression.isInteger(value))
    { return new Type("int",null); }

    if (Expression.isLong(value))
    { return new Type("long",null); }

    if (Expression.isDouble(value))
    { return new Type("double",null); }

    if ("true".equals(value) || "false".equals(value))
    { return new Type("boolean", null); } 

    return new Type("OclAny", null); 
  } 

  public Type deduceElementType()
  { if (Expression.isString(value))
    { return new Type("String",null); }
   
    return new Type("OclAny", null); 
  } 


  public Expression cexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { if ("primaryExpression".equals(tag))
    { System.out.println(">> Basic primary expression: " + value); 

      if ("TRUE".equals(value))
      { return new BasicExpression(true); } 
      if ("FALSE".equals(value))
      { return new BasicExpression(false); } 


      Type t = (Type) vartypes.get(value); 
      if (t != null) 
      { BasicExpression be = new BasicExpression(value); 
        be.setType(t); 
        be.setElementType((Type) varelemtypes.get(value)); 
        return be; 
      } 

      if ("strcmp".equals(value))
      { Type stringtype = new Type("String", null); 
        Attribute x1 = 
          new Attribute("_x1", stringtype, 
                        ModelElement.INTERNAL); 
        Attribute x2 = 
          new Attribute("_x2", stringtype, 
                        ModelElement.INTERNAL);
        BasicExpression x1be = 
          BasicExpression.newVariableBasicExpression(x1); 
        BasicExpression x2be = 
          BasicExpression.newVariableBasicExpression(x2); 
 
        UnaryExpression letexpr = 
          new UnaryExpression("lambda", 
                new BinaryExpression("->compareTo", 
                                     x1be, x2be));
        letexpr.setAccumulator(x2);
        Type ftype1 = new Type("Function", null); 
        ftype1.setKeyType(stringtype); 
        ftype1.setElementType(new Type("int", null));  
        letexpr.setType(ftype1); 

        UnaryExpression res = 
          new UnaryExpression("lambda", letexpr); 
        res.setAccumulator(x1); 

        Type ftype2 = new Type("Function", null); 
        ftype2.setKeyType(stringtype); 
        ftype2.setElementType(ftype1);  
        res.setType(ftype2); 

        return res; 
      }  

      if ("stdout".equals(value))
      { BasicExpression be = new BasicExpression("System_out"); 
        be.setType(new Type("OclFile", null)); 
        return be; 
      } 

      if ("stdin".equals(value))
      { BasicExpression be = new BasicExpression("System_in"); 
        be.setType(new Type("OclFile", null)); 
        return be; 
      } 

      if ("stderr".equals(value))
      { BasicExpression be = new BasicExpression("System_err"); 
        be.setType(new Type("OclFile", null)); 
        return be; 
      } 

      if ("EOF".equals(value))
      { return new BasicExpression(-1); }


      if ("HUGE_VAL".equals(value))
      { Expression resx =
          new BasicExpression("Math_PINFINITY");
        resx.setType(new Type("double", null)); 
        return resx; 
      } 

      if ("NAN".equals(value))
      { Expression resx =
          new BasicExpression("Math_NaN");
        resx.setType(new Type("double", null)); 
        return resx; 
      } 

      if ("CHAR_BIT".equals(value))
      { return new BasicExpression(8); } 
      if ("CHAR_MAX".equals(value))
      { return new BasicExpression(255); } 
      if ("CHAR_MIN".equals(value))
      { return new BasicExpression(0); } 
      if ("INT_MAX".equals(value))
      { return new BasicExpression(2147483647); } 
      if ("INT_MIN".equals(value))
      { return new BasicExpression(-2147483647); } 
      if ("LONG_MAX".equals(value))
      { return new BasicExpression(9223372036854775807L); } 
      if ("LONG_MIN".equals(value))
      { return new BasicExpression(-9223372036854775808L); } 
      if ("SCHAR_MAX".equals(value))
      { return new BasicExpression(127); } 
      if ("SCHAR_MIN".equals(value))
      { return new BasicExpression(-127); } 
      if ("UCHAR_MAX".equals(value))
      { return new BasicExpression(255); } 
      if ("UCHAR_MIN".equals(value))
      { return new BasicExpression(0); } 
      if ("SHRT_MAX".equals(value))
      { return new BasicExpression(32767); } 
      if ("SHRT_MIN".equals(value))
      { return new BasicExpression(-32767); } 
      if ("UINT_MAX".equals(value))
      { return new BasicExpression(4294967295L); } 
      if ("USHRT_MAX".equals(value))
      { return new BasicExpression(65535); } 
      if ("ULONG_MAX".equals(value))
      { long mm = 1 + 2*(9223372036854775807L); 
        Expression umax = new BasicExpression(mm);
        umax.setBrackets(true); 
        return umax;  
      } 
      if ("FLT_RADIX".equals(value))
      { return new BasicExpression(2); } 
      if ("FLT_ROUNDS".equals(value))
      { return new BasicExpression(1); } 
      if ("FLT_DIG".equals(value))
      { return new BasicExpression(6); } 
      if ("FLT_EPSILON".equals(value))
      { double d = 1.0/100000; 
        return new BasicExpression(d);
      } 
      if ("FLT_MANT_DIG".equals(value))
      { return new BasicExpression(24); } 
      if ("FLT_MAX".equals(value))
      { double d = 1.0*Math.pow(10,37); 
        return new BasicExpression(d); 
      } 
      if ("FLT_MAX_EXP".equals(value))
      { return new BasicExpression(128); }
      if ("FLT_MIN".equals(value))
      { double d = 1.0/Math.pow(10,37); 
        return new BasicExpression(d); 
      }
      if ("FLT_MIN_EXP".equals(value))
      { return new BasicExpression(-125); } 
      if ("DBL_DIG".equals(value))
      { return new BasicExpression(10); } 
      if ("DBL_EPSILON".equals(value))
      { double d = 1.0/1000000000; 
        return new BasicExpression(d);
      } 
      if ("DBL_MANT_DIG".equals(value))
      { return new BasicExpression(53); } 
      if ("DBL_MAX".equals(value))
      { double d = 1.0*Math.pow(10,37); 
        return new BasicExpression(d); 
      } 
      if ("DBL_MAX_EXP".equals(value))
      { return new BasicExpression(1024); }
      if ("DBL_MIN".equals(value))
      { double d = 1.0/Math.pow(10,37); 
        return new BasicExpression(d); 
      }
      if ("DBL_MIN_EXP".equals(value))
      { return new BasicExpression(-1021); } 
      if ("EDOM".equals(value))
      { return new BasicExpression(33); } 
      if ("ERANGE".equals(value))
      { return new BasicExpression(34); } 
 

 
      if ("NULL".equals(value))
      { return new BasicExpression("null"); } 
      if ("true".equals(value))
      { return new BasicExpression(true); } 
      if ("false".equals(value))
      { return new BasicExpression(false); } 

      BasicExpression v = new BasicExpression(value); 
      if (Expression.isString(value))
      { if ('\'' == value.charAt(0))
        { BasicExpression ve = new BasicExpression("\"" + value.substring(1,value.length()-1) + "\""); 
          ve.setType(new Type("String",null)); 
          ve.setUmlKind(Expression.VALUE);
          UnaryExpression res = 
            new UnaryExpression("->char2byte", ve); 
          res.setType(new Type("int", null)); 
          return res; 
        } 
        v.setType(new Type("String",null)); 
        v.setUmlKind(Expression.VALUE); 
      }
      else if (Expression.isInteger(value))
      { v.setType(new Type("int",null)); 
        v.setUmlKind(Expression.VALUE); 
      }
      else if (Expression.isLong(value))
      { v.setType(new Type("long",null)); 
        v.setUmlKind(Expression.VALUE); 
      }
      else if (Expression.isDouble(value))
      { v.setType(new Type("double",null)); 
        v.setUmlKind(Expression.VALUE); 
      }

      Entity mainC = (Entity) ModelElement.lookupByName(
                                      "FromC", ents);
      if (mainC != null) 
      { BehaviouralFeature bf = mainC.getOperation(value); 

        if (bf != null) 
        { System.out.println(">>> Function defined in main program: " + value + " " + bf.display() + " " + bf.isVarArg()); 
          BasicExpression bfcall = 
            BasicExpression.newStaticCallBasicExpression(
                                                 bf,mainC); 
          Expression lam = 
            UnaryExpression.newLambdaUnaryExpression(bfcall, bf); 
          Type ftype = bf.getFunctionType(); 
          lam.setType(ftype); 
          return lam; 
        }

        Attribute att = mainC.getAttribute(value); 
        if (att != null) 
        { System.out.println(">>> Global attribute: " + value + " : " + att.getType()); 
          BasicExpression expr = 
            BasicExpression.newStaticAttributeBasicExpression(
                                                    att);
          expr.variable = att;  
          return expr; 
        }       
      } 

      return v; 
    } 
     
    return null; 
  } 


  /* JavaScript processing: */ 

  public Vector jsclassDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 

  public Vector jsupdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jscompleteUpdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jspreSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Vector jspostSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { return new Vector(); } 

  public Expression jsexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents)
  { if ("this".equals(value))
    { Expression expr = 
        BasicExpression.newVariableBasicExpression("self"); 
      return expr;
    } 

    if ("super".equals(value))
    { Expression expr = 
        BasicExpression.newVariableBasicExpression("super"); 
      return expr;
    }

    if ("null".equals(value) || "undefined".equals(value))
    { Expression expr = 
        BasicExpression.newVariableBasicExpression("null");
      expr.setType(new Type("OclAny", null));  
      return expr;
    }

    if ("true".equals(value))
    { return new BasicExpression(true); } 
    
    if ("false".equals(value))
    { return new BasicExpression(false); } 

    if ("templateStringAtom".equals(tag))
    {  
      return new BasicExpression(value); 
    } 

    if ("numericLiteral".equals(tag))
    { 
      BasicExpression v = new BasicExpression(value); 
      if (Expression.isInteger(value))
      { v.setType(new Type("int",null)); 
        v.setUmlKind(Expression.VALUE);
        v = 
          new BasicExpression(
                Expression.convertInteger(value));  
      }
      else if (Expression.isLong(value))
      { v.setType(new Type("long",null)); 
        v.setUmlKind(Expression.VALUE); 
        v = 
          new BasicExpression(
                Expression.convertLong(value));
      }
      else if (Expression.isDouble(value))
      { v.setType(new Type("double",null)); 
        v.setUmlKind(Expression.VALUE); 
      } 
      return v; 
    }
    else if ("bigintLiteral".equals(tag))
    { BasicExpression v = new BasicExpression(value); 
      if (value.endsWith("n"))
      { value = value.substring(0,value.length()-1);
        v = new BasicExpression(value);
      } 
      v.setType(new Type("long",null)); 
      v.setUmlKind(Expression.VALUE); 
      return v; 
    }   
    else if ("literal".equals(tag) || 
             "propertyName".equals(tag)) 
    { int sze = value.length(); 

      if (sze > 1 && '/' == value.charAt(0) && 
          '/' == value.charAt(sze-1))
      { BasicExpression regexpr = 
          new BasicExpression("\"" + 
            value.substring(1,value.length()-1) + "\""); 
        regexpr.setType(new Type("String",null)); 
        regexpr.setElementType(new Type("String",null)); 
        regexpr.setUmlKind(Expression.VALUE);
        return regexpr; 
      } 

      if (sze > 1 && '/' == value.charAt(0) && 
          value.lastIndexOf("/") > 0)
      { int indx = value.lastIndexOf("/"); 
        BasicExpression regexpr = 
          new BasicExpression("\"" + 
            value.substring(1,indx) + "\""); 
        regexpr.setType(new Type("String",null)); 
        regexpr.setElementType(new Type("String",null)); 
        regexpr.setUmlKind(Expression.VALUE);
        return regexpr; 
      }  

      BasicExpression v = new BasicExpression(value); 
      if (Expression.isString(value))
      { if ('\'' == value.charAt(0))
        { BasicExpression ve = 
            new BasicExpression("\"" + 
                  value.substring(1,value.length()-1) + "\""); 
          ve.setType(new Type("String",null)); 
          ve.setElementType(new Type("String",null)); 
          ve.setUmlKind(Expression.VALUE);
          return ve; 
        } 
        v.setType(new Type("String",null)); 
        v.setElementType(new Type("String",null)); 
        v.setUmlKind(Expression.VALUE);
        return v;  
      }
    } 
    else if ("identifier".equals(tag) || 
             "keyword".equals(tag))
    { 

      if ("PI".equals(value))
      { Expression res = 
          new BasicExpression(3.1415926535897); 
        res.setType(new Type("double", null)); 
        return res; 
      }

      if ("Infinity".equals(value))
      { Expression res = 
          BasicExpression.newValueBasicExpression(
                                     "Math_PINFINITY");
        res.setType(new Type("double", null));  
        return res; 
      }

      if ("NaN".equals(value))
      { Expression expr = 
          BasicExpression.newValueBasicExpression(
                                           "Math_NaN");
        expr.setType(new Type("double", null));  
        return expr; 
      } 

   /* Is value a feature of some known object? */ 

      Entity mainC = (Entity) ModelElement.lookupByName(
                                      "FromJavaScript", ents);
      if (mainC != null) 
      { BehaviouralFeature bf = mainC.getOperation(value); 

        if (bf != null) 
        { System.out.println(">>> Function defined in main program: " + value + " " + bf.display() + " " + bf.isVarArg()); 
          BasicExpression sexpr = 
            BasicExpression.newVariableBasicExpression(
                                                "self"); 
          sexpr.setType(new Type(mainC)); 
          BasicExpression lam = 
            BasicExpression.newVariableBasicExpression(
                                                 value,sexpr); 
          Type ftype = bf.getFunctionType(); 
          lam.setType(ftype); 
          return lam; 
        }

        Attribute att = mainC.getAttribute(value); 
        if (att != null) 
        { System.out.println(">>> Global attribute: " + value + " : " + att.getType()); 
          BasicExpression expr = 
            new BasicExpression(att);
          expr.variable = att;  
          return expr; 
        }       
      }

      Type t = (Type) vartypes.get(value); 
      if (t != null) 
      { BasicExpression be = new BasicExpression(value); 
        be.setType(t); 
        Object elemt = varelemtypes.get(value); 
        if (elemt != null && elemt instanceof Type)
        { be.setElementType((Type) elemt); }  
        return be; 
      } 

      BasicExpression v = new BasicExpression(value); 
      return v; 
    } 
     
    return null; 
  } 

  public Vector jsexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { System.out.println(">> jsexpressionListToKM3 for " + tag + " with value " + value); 
    System.out.println(); 

    Vector res = new Vector(); 
    return res; 
  }

  public Vector jsvariableDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { System.out.println(">> jsvariableDeclarationToKM3 for " + tag + " with value " + value); 
    System.out.println(); 

    // if ("identifier".equals(tag))
    { Attribute attr = 
        new Attribute(value,new Type("OclAny", null), ModelElement.INTERNAL); 
      Vector res = new Vector(); 
      res.add(attr); 
      return res; 
    } 
  } 

  public Vector jsstatementListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { Vector res = new Vector();
    return res;  
  }


  public Vector jsstatementToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities)
  { return new Vector(); } 



  /* Java processing */ 

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

  public String km3typeForJavaType()
  { if ("String".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    }
 
    if ("char".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    }
 
    if ("Character".equals(value) || 
        "InetAddress".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    }
 
    if ("CharSequence".equals(value) || 
        "Segment".equals(value) || 
        "JsonString".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    }
 
    if ("StringBuffer".equals(value) || "Path".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    }
 
    if ("StringBuilder".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; 
    } 

    if ("int".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }
 
    if ("Integer".equals(value) || 
        "AtomicInteger".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }
 
    if ("Byte".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }
 
    if ("Short".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }
 
    if ("byte".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    }
 
    if ("short".equals(value))
    { modelElement = new Type("int", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "int"; 
    } 

    if ("double".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    }
 
    if ("Double".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    }
 
    if ("Number".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    }
 
    if ("Float".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    }
 
    if ("float".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    }
 
    if ("BigDecimal".equals(value) || 
        "JsonNumber".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    } 
    
    if ("long".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; 
    }
 
    if ("BigInteger".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; 
    }
 
    if ("Long".equals(value) ||
        "AtomicLong".equals(value))
    { modelElement = new Type("long", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "long"; 
    } 

    if ("Boolean".equals(value) || "boolean".equals(value) ||
        "AtomicBoolean".equals(value))
    { modelElement = new Type("boolean", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "boolean"; 
    }

    if ("Object".equals(value) || "?".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; 
    }

    if ("Collection".equals(value) || 
        "Iterable".equals(value) || 
        "AbstractCollection".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

    if ("Class".equals(value))
    { modelElement = new Type("OclType", null); 
      expression = new BasicExpression((Type) modelElement); 
      addRequiredLibrary("OclType");  
      return "OclType"; 
    }

    if ("Comparable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; 
    }

    if ("Cloneable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; 
    }

    if ("Serializable".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; 
    }

    if ("Runnable".equals(value))
    { modelElement = new Type("Runnable", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Runnable"; 
    }



    if ("Constructor".equals(value) || 
        "Executable".equals(value) || 
        "Method".equals(value))
    { modelElement = new Type("OclOperation", null); 
      expression = new BasicExpression((Type) modelElement);
      addRequiredLibrary("OclType");  
       
      return "OclOperation"; 
    }

    if ("Field".equals(value))
    { modelElement = new Type("OclAttribute", null); 
      expression = new BasicExpression((Type) modelElement); 
      addRequiredLibrary("OclType");  
      return "OclAttribute"; 
    }

    if ("Thread".equals(value) || "Runtime".equals(value) || 
        "Process".equals(value) || "Timer".equals(value) || 
        "TimerTask".equals(value))
    { modelElement = new Type("OclProcess", null); 
      expression = new BasicExpression((Type) modelElement);
      addRequiredLibrary("OclProcess");  
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
      addRequiredLibrary("OclDate");  
      return "OclDate"; 
    }

    if ("Calendar".equals(value) || 
        "Timestamp".equals(value) || 
        "GregorianCalendar".equals(value))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; 
    }

    if ("Random".equals(value))
    { modelElement = new Type("OclRandom", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRandom"; 
    }

    if ("Pattern".equals(value) || 
        "FileFilter".equals(value) ||
        "FilenameFilter".equals(value) || 
        "Matcher".equals(value))
    { modelElement = new Type("OclRegex", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRegex"; 
    } 

    if ("ArrayList".equals(value) || 
        // "Array".equals(value) || 
        "AbstractList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }
 
    if ("Vector".equals(value) || 
        "LinkedList".equals(value) ||
        "List".equals(value) ||
        "Stack".equals(value) || "Queue".equals(value) ||
        "Deque".equals(value) || 
        "AbstractSequentialList".equals(value) ||
        "ArrayDeque".equals(value) ||
        "BlockingDeque".equals(value) ||
        "LinkedBlockingDeque".equals(value) ||
        "ArrayBlockingQueue".equals(value) ||
        "BlockingQueue".equals(value) || 
        "ListOrderedSet".equals(value) ||
        "SetUniqueList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }
 
    if ("PriorityQueue".equals(value)  ||
        "PriorityBlockingQueue".equals(value) ||
        "TreeBag".equals(value) || 
        "SortedBag".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setSorted(true);  
      expression = new BasicExpression((Type) modelElement); 
      return "SortedSequence"; 
    } // and it is sorted
 
    if ("Stream".equals(value) || "JsonArray".equals(value) ||
        "JSONArray".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    } 

    if ("Bag".equals(value) || "HashBag".equals(value) ||
        "TreeList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence";
    }

    if ("Multiset".equals(value) || 
        "MultiSet".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    } 
     
    if ("BitSet".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("boolean", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(boolean)";
    } 

    if ("DoubleStream".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("double", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(double)"; 
    } 

    if ("AtomicLongArray".equals(value) || 
        "LongStream".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("long", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(long)"; 
    } 

    if ("AtomicIntegerArray".equals(value) || 
        "IntStream".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(
                              new Type("int", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(int)"; 
    } 

    if ("ByteBuffer".equals(value) || 
        "IntBuffer".equals(value) ||
        "ShortBuffer".equals(value) || 
        "CharBuffer".equals(value) || 
        "LongBuffer".equals(value) 
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("int", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(int)";
    } 

    if ("FloatBuffer".equals(value) || 
        "DoubleBuffer".equals(value)
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("double", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(double)";
    } 

    if ("Set".equals(value) || "HashSet".equals(value) ||
        "EnumSet".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; 
    }
 
    if ("SortedSet".equals(value) || "TreeSet".equals(value))
    { modelElement = new Type("Set", null); 
      ((Type) modelElement).setSorted(true);  
      expression = new BasicExpression((Type) modelElement); 
      return "Set";
    } 

    if ("Map".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("HashMap".equals(value) || 
        "LinkedHashMap".equals(value) || 
        "EnumMap".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 
    
    if ("SortedMap".equals(value) || "TreeMap".equals(value))
    { modelElement = new Type("Map", null); 
      ((Type) modelElement).setSorted(true);  
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("Hashtable".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("Properties".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("ImmutableMap".equals(value) || 
        "Pair".equals(value) ||
        "Triple".equals(value) || "Entry".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("JSONObject".equals(value) || 
        "JsonObject".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }

    if ("Comparator".equals(value))
    { modelElement = new Type("OclComparator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclComparator"; 
    }

    if ("Enumeration".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    }
 
    if ("Iterator".equals(value) ||
        "ListIterator".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    }
 
    if ("StringTokenizer".equals(value) ||  
        "Spliterator".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    }
 
    if ("ResultSet".equals(value) ||
        "ResultSetMetaData".equals(value) ||
        "CachedRowSet".equals(value) ||
        "FilteredRowSet".equals(value) ||
        "JdbcRowSet".equals(value) ||
        "JoinRowSet".equals(value) ||
        "RowSet".equals(value) ||
        "WebRowSet".equals(value) ||
        "Cursor".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 

    if ("File".equals(value) || 
        "FileDescriptor".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
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
    if ("PrintStream".equals(value) || 
        "InputStream".equals(value) || 
        "OutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("FileOutputStream".equals(value) ||
        "FileInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("Reader".equals(value) || "FileReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 

    if ("Writer".equals(value) ||
        "StringWriter".equals(value) || 
        "FileWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("RandomAccessFile".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("BufferedReader".equals(value) || 
        "LittleEndianInput".equals(value) ||
        "BigEndianInput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("BufferedWriter".equals(value) ||
        "LittleEndianOutput".equals(value) ||
        "BigEndianOutput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }

    if ("FileStore".equals(value) || 
        "ByteChannel".equals(value) ||
        "Channel".equals(value) ||
        "ReadableByteChannel".equals(value) ||
        "WritableByteChannel".equals(value) ||
        "SeekableByteChannel".equals(value) ||
        "FileChannel".equals(value))
    { return "OclFile"; } 

 
    if ("InputStreamReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 

    if ("OutputStreamWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }

    if ("PrintWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 

    if ("JDBCDatabase".equals(value) ||
        "Connection".equals(value) ||
        "DriverManager".equals(value) ||     
        "HttpURLConnection".equals(value) ||  
        "URLConnection".equals(value) ||  
        "Socket".equals(value) || 
        "ServerSocket".equals(value) || 
        "URL".equals(value) || 
        "InetAddress".equals(value) || 
        "Inet4Address".equals(value) || 
        "BluetoothSocket".equals(value) || 
        "SQLiteDatabase".equals(value))
    { modelElement = new Type("OclDatasource", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDatasource"; 
    } 

    if ("PreparedStatement".equals(value) || 
        "CachedStatement".equals(value) ||
        "Statement".equals(value) || 
        "CallableStatement".equals(value))
    { modelElement = new Type("SQLStatement", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SQLStatement"; 
    } 
 
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
      return "SystemException"; 
    }

    if ("VirtualMachineError".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; 
    }
 
    if ("AssertionError".equals(value))
    { modelElement = new Type("AssertionException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "AssertionException"; 
    } 
    
 
    if ("Exception".equals(value))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; 
    }
 
    if ("RuntimeException".equals(value) || 
        "InterruptedException".equals(value) ||
        "IllegalMonitorStateException".equals(value))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; 
    }
 
    if ("IOException".equals(value) ||
        "SQLException".equals(value) ||  
        "EOFException".equals(value) ||
        // "UnknownHostException".equals(value) || 
        "SocketException".equals(value))
    { modelElement = new Type("IOException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IOException"; 
    }
 
    if ("ClassCastException".equals(value))
    { modelElement = new Type("CastingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "CastingException"; 
    } 

    if ("NullPointerException".equals(value))
    { modelElement = new Type("NullAccessException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "NullAccessException"; 
    } 

    if ("ArithmeticException".equals(value))
    { modelElement = new Type("ArithmeticException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ArithmeticException"; 
    }

    if (value.endsWith("IndexOutOfBoundsException") || 
        "ArrayStoreException".equals(value))
    { modelElement = new Type("IndexingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IndexingException"; 
    } 

    if ("NoSuchElementException".equals(value) ||
        "MalformedURLException".equals(value) || 
        "UnknownHostException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; 
    }

    if ("InputMismatchException".equals(value) ||
        "UnsupportedOperationException".equals(value) ||
        "IllegalStateException".equals(value) || 
        "NumberFormatException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; 
    }
    // if ("ArrayIndexOutOfBoundsException".equals(value) ||
    //     "StringIndexOutOfBoundsException".equals(value))
    // { return "IndexingException"; } 
    if ("IllegalAccessException".equals(value) ||
        "LinkageError".equals(value) || 
        "SecurityException".equals(value) ||  
        "NoClassDefFoundError".equals(value) ||
        "BindException".equals(value) ||
        "ReadOnlyBufferException".equals(value) || 
        "ReadOnlySystemException".equals(value) ||
        "ConcurrentModificationException".equals(value)
       )
    { modelElement = new Type("AccessingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "AccessingException"; 
    }

    if (value.endsWith("Exception"))
    { modelElement = new Type("ProgramException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ProgramException"; 
    } // default
 
    return null; 
  } 

  public String toKM3type()
  { if (ASTTerm.entities == null) 
    { ASTTerm.entities = new Vector(); } 
	
    if ("typeTypeOrVoid".equals(tag) && 
        "void".equals(value))
    { modelElement = new Type("void", null); 
      expression = new BasicExpression((Type) modelElement);
      ASTTerm.setType(this, "void"); 
      return "void"; 
    } 
      
    if ("classOrInterfaceType".equals(tag) || 
        "classType".equals(tag))
    { String resx = km3typeForJavaType(); 
      if (resx != null) 
      { return resx; } 

      Entity ent = 
        (Entity) ModelElement.lookupByName(value,
                                           ASTTerm.entities); 
      if (ent != null) 
      { modelElement = new Type(ent); } 
      else if (Entity.validEntityName(value))
      { ent = new Entity(value); 
        ASTTerm.entities.add(ent);
        modelElement = new Type(ent); 
      }
 
      ASTTerm.setType(this, "OclType"); 
      return value; 
    }

    if ("primary".equals(tag)) // enum
    { Type typ = (Type) ModelElement.lookupByName(value,
                                           ASTTerm.enumtypes); 
      if (typ != null) 
      { modelElement = typ;
        ASTTerm.setType(this, "OclType"); 
        expression = new BasicExpression(typ);
        return value;
      }  
    } 

    if ("typeArgument".equals(tag))
    { if ("?".equals(value))
      { modelElement = new Type("OclAny", null); 
        return "OclAny"; 
      }
 
      Entity ent = 
        (Entity) ModelElement.lookupByName(value,
                                           ASTTerm.entities); 
      if (ent != null) 
      { modelElement = new Type(ent); } 
      else if (Entity.validEntityName(value))
      { ent = new Entity(value); 
        ASTTerm.entities.add(ent);
        modelElement = new Type(ent);  
      }

      if (ent != null) 
      { ent.setIsGenericParameter(true); }   
      return value; 
    } 

    return toKM3(); 
  } 

  public Vector getParameterExpressions()
  { return new Vector(); } 

  public String lambdaParametersToKM3()
  { if ("lambdaParameters".equals(tag))
    {  
      modelElement = 
        new Attribute(value,new Type("OclAny", null),
                      ModelElement.INTERNAL);
      modelElements = new Vector(); 
      modelElements.add(modelElement); 
      return value;  
    } 
    else 
    { modelElement = 
        new Attribute(tag,new Type("OclAny", null),
                      ModelElement.INTERNAL);
      modelElements = new Vector(); 
      modelElements.add(modelElement);  
      modelElement = 
        new Attribute(value,new Type("OclAny", null),
                      ModelElement.INTERNAL);
      modelElements.add(modelElement);  
      return tag + ", " + value; 
    } 
  } 

  public String toKM3()
  { if ("lambdaParameters".equals(tag))
    { expression = 
        BasicExpression.newVariableBasicExpression(value); 
      return value;
    } 

    if ("this".equals(value))
    { expression = 
        BasicExpression.newVariableBasicExpression("self"); 
      return "self";
    } 

    String resx = km3typeForJavaType();
    if (resx != null) 
    { return resx; } 
 
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
    if ("CharSequence".equals(value) || 
        "Segment".equals(value) || 
        "JsonString".equals(value))
    { modelElement = new Type("String", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "String"; } 
    if ("StringBuffer".equals(value) || "Path".equals(value))
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
      return "double"; 
    }
 
    if ("BigDecimal".equals(value) || 
        "JsonNumber".equals(value))
    { modelElement = new Type("double", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "double"; 
    } 
    
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

    if ("Object".equals(value) || "?".equals(value))
    { modelElement = new Type("OclAny", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclAny"; }

    if ("Collection".equals(value) || 
        "Iterable".equals(value) || 
        "AbstractCollection".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }

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
    { modelElement = new Type("Runnable", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Runnable"; }



    if ("Constructor".equals(value) || 
        "Executable".equals(value) || 
        "Method".equals(value))
    { modelElement = new Type("OclOperation", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclOperation";
    }

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
      return "OclDate"; 
    }

    if ("Calendar".equals(value) || 
        "Timestamp".equals(value) || 
        "GregorianCalendar".equals(value))
    { modelElement = new Type("OclDate", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDate"; 
    }

    if ("Random".equals(value))
    { modelElement = new Type("OclRandom", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRandom";
    }

    if ("Pattern".equals(value) || 
        "FileFilter".equals(value) ||
        "FilenameFilter".equals(value) || 
        "Matcher".equals(value))
    { modelElement = new Type("OclRegex", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclRegex"; } 

    if ("ArrayList".equals(value)) // "Array".equals(value))
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
      return "Sequence"; 
    }
 
    if ("LinkedList".equals(value) || "List".equals(value) ||
        "Stack".equals(value) || 
        "Queue".equals(value) || "Deque".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence";
    }
 
    if ("BlockingQueue".equals(value) ||
        "AbstractSequentialList".equals(value) ||
        "ArrayDeque".equals(value) ||
        "BlockingDeque".equals(value) ||
        "LinkedBlockingDeque".equals(value) ||
        "ArrayBlockingQueue".equals(value) ||
        "ListOrderedSet".equals(value) ||
        "SetUniqueList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    }
 
    if ("PriorityQueue".equals(value) ||
        "PriorityBlockingQueue".equals(value) ||
        "TreeBag".equals(value) || 
        "SortedBag".equals(value))
    { modelElement = new Type("Sequence", null); 
      ((Type) modelElement).setSorted(true); 
      expression = new BasicExpression((Type) modelElement); 
      return "SortedSequence"; 
    } 
    
    if ("Stream".equals(value) || "JsonArray".equals(value) ||
        "JSONArray".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    } 

    if ("Bag".equals(value) || "HashBag".equals(value) ||
        "TreeList".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence";
    }

    if ("Multiset".equals(value) || 
        "MultiSet".equals(value))
    { modelElement = new Type("Sequence", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence"; 
    } 
     
    if ("BitSet".equals(value))
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("boolean", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(boolean)"; 
    } 

    if ("ByteBuffer".equals(value) || 
        "IntBuffer".equals(value) ||
        "ShortBuffer".equals(value) || 
        "CharBuffer".equals(value) || 
        "LongBuffer".equals(value) 
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("int", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(int)";
    } 

    if ("FloatBuffer".equals(value) || 
        "DoubleBuffer".equals(value)
       )
    { modelElement = new Type("Sequence", null);
      ((Type) modelElement).setElementType(new Type("double", null));  
      expression = new BasicExpression((Type) modelElement); 
      return "Sequence(double)";
    } 

    if ("Set".equals(value) ||
        "HashSet".equals(value) || "EnumSet".equals(value))
    { modelElement = new Type("Set", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; 
    }
 
    if ("SortedSet".equals(value) || "TreeSet".equals(value))
    { modelElement = new Type("Set", null);
      ((Type) modelElement).setSorted(true);  
      expression = new BasicExpression((Type) modelElement); 
      return "Set"; 
    } 

    if ("Map".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("HashMap".equals(value) || 
        "LinkedHashMap".equals(value) || 
        "EnumMap".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("SortedMap".equals(value) || "TreeMap".equals(value))
    { modelElement = new Type("Map", null); 
      ((Type) modelElement).setSorted(true);  
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("Hashtable".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }
 
    if ("ImmutableMap".equals(value) ||
        "Triple".equals(value) || "Pair".equals(value)
        || "Entry".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("Properties".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    } 

    if ("JSONObject".equals(value) || 
        "JsonObject".equals(value))
    { modelElement = new Type("Map", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "Map"; 
    }

    if ("Comparator".equals(value))
    { modelElement = new Type("OclComparator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclComparator"; 
    }

    if ("Enumeration".equals(value) ||
        "Iterator".equals(value) ||
        "ListIterator".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    }
 
    if ("ResultSet".equals(value) ||
        "ResultSetMetaData".equals(value) ||
        "CachedRowSet".equals(value) ||
        "FilteredRowSet".equals(value) ||
        "JdbcRowSet".equals(value) ||
        "JoinRowSet".equals(value) ||
        "RowSet".equals(value) ||
        "WebRowSet".equals(value) ||
        "Cursor".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 

    if ("StringTokenizer".equals(value))
    { modelElement = new Type("OclIterator", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclIterator"; 
    } 

    if ("File".equals(value) || 
        "FileDescriptor".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("Formatter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
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
    if ("PrintStream".equals(value) || "InputStream".equals(value) || 
        "OutputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 
    if ("FileOutputStream".equals(value) ||
        "FileInputStream".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("FileStore".equals(value) || 
        "ByteChannel".equals(value) ||
        "Channel".equals(value) ||
        "ReadableByteChannel".equals(value) ||
        "WritableByteChannel".equals(value) ||
        "SeekableByteChannel".equals(value) ||
        "FileChannel".equals(value))
    { return "OclFile"; } 

    if ("Reader".equals(value) || "FileReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; } 

    if ("Writer".equals(value) || 
        "StringWriter".equals(value) ||
        "FileWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("RandomAccessFile".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; }
 
    if ("BufferedReader".equals(value) || 
        "LittleEndianInput".equals(value) ||
        "BigEndianInput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("BufferedWriter".equals(value) || 
        "LittleEndianOutput".equals(value) ||
        "BigEndianOutput".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("InputStreamReader".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }
 
    if ("OutputStreamWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    }

    if ("PrintWriter".equals(value))
    { modelElement = new Type("OclFile", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclFile"; 
    } 
 
    if ("JDBCDatabase".equals(value) || 
        "Connection".equals(value) ||
        "DriverManager".equals(value) ||    
        "HttpURLConnection".equals(value) ||  
        "URLConnection".equals(value) ||  
        "Socket".equals(value) || 
        "ServerSocket".equals(value) || 
        "URL".equals(value) || 
        "InetAddress".equals(value) || 
        "Inet4Address".equals(value) || 
        "BluetoothSocket".equals(value) ||  
        "SQLiteDatabase".equals(value))
    { modelElement = new Type("OclDatasource", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclDatasource"; 
    } 

    if ("PreparedStatement".equals(value) || 
        "CachedStatement".equals(value) ||
        "Statement".equals(value) || 
        "CallableStatement".equals(value))
    { modelElement = new Type("SQLStatement", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SQLStatement"; 
    } 

    if ("Throwable".equals(value))
    { modelElement = new Type("OclException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "OclException"; 
    } 

    if ("Error".equals(value))
    { modelElement = new Type("SystemException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "SystemException"; 
    }
 
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
        "SQLException".equals(value) ||  
        "EOFException".equals(value) ||
        "SocketException".equals(value))
    { modelElement = new Type("IOException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IOException"; 
    }
 
    if ("ClassCastException".equals(value))
    { modelElement = new Type("CastingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "CastingException"; 
    } 

    if ("NullPointerException".equals(value))
    { modelElement = new Type("NullAccessException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "NullAccessException"; 
    } 

    if ("ArithmeticException".equals(value))
    { modelElement = new Type("ArithmeticException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "ArithmeticException"; 
    }

    if (value.endsWith("IndexOutOfBoundsException") || 
        "ArrayStoreException".equals(value))
    { modelElement = new Type("IndexingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IndexingException"; 
    } 

    if ("NoSuchElementException".equals(value) ||
        "MalformedURLException".equals(value) || 
        "UnknownHostException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; 
    }

    if ("InputMismatchException".equals(value) ||
        "UnsupportedOperationException".equals(value) ||
        "IllegalStateException".equals(value) || 
        "NumberFormatException".equals(value))
    { modelElement = new Type("IncorrectElementException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "IncorrectElementException"; 
    }
    // if ("ArrayIndexOutOfBoundsException".equals(value) ||
    //     "StringIndexOutOfBoundsException".equals(value))
    // { return "IndexingException"; } 
    if ("IllegalAccessException".equals(value) ||
        "LinkageError".equals(value) || 
        "SecurityException".equals(value) ||  
        "NoClassDefFoundError".equals(value) ||
        "BindException".equals(value) ||
        "ReadOnlyBufferException".equals(value) || 
        "ReadOnlySystemException".equals(value) ||
        "ConcurrentModificationException".equals(value)
       )
    { modelElement = new Type("AccessingException", null); 
      expression = new BasicExpression((Type) modelElement); 
      return "AccessingException"; 
    }
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

    if ("classBodyDeclaration".equals(tag) && 
        ";".equals(value))
    { return ""; } 

    if ("interfaceBodyDeclaration".equals(tag) && 
        ";".equals(value))
    { return ""; } 

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

      value = Expression.removeUnderscores(value); 

      if (value.endsWith("L") || value.endsWith("l"))
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

      value = Expression.removeUnderscores(value); 

      expression.setType(new Type("double", null)); 
      ASTTerm.setType(this,"double");
      ASTTerm.setType(value,"double");
 
      String baseValue = value; 
      
      if (value.endsWith("F") || value.endsWith("f"))
      { baseValue = 
           value.substring(0,value.length()-1);
      } 
        
      try {
          double nn = Double.parseDouble(baseValue); 
          long nnlong = Double.doubleToLongBits(nn); 
          double dx = Double.longBitsToDouble(nnlong); 
          expression = new BasicExpression(dx); 
          return "" + dx; 
      }
      catch (Exception _ex)
      { expression = 
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

  public String toKM3asObject(Entity ent)
  { String res = ""; 
    String ename = ent.getName(); 
      
    if ("enumConstant".equals(tag))
    { res = "static attribute " + value + " : " + ename + 
            " := " + ename + ".new" + ename + "()"; 
      Attribute att = 
          new Attribute(value, new Type(ent), 
                        ModelElement.INTERNAL);
      att.setStatic(true); 
      Expression call = 
          BasicExpression.newStaticCallBasicExpression(
                                  "new" + ename, ename); 
      att.setInitialExpression(call);  
      ent.addAttribute(att); 
    } 

    return res; 
  } 


  public String typeArgumentsToKM3ElementType()
  { if ("?".equals(value))
    { return "OclAny"; }
    return value; 
  } 

  /* Visual Basic 6/VBA processing */ 

  public Type vbToKM3type(java.util.Map vartypes, 
    java.util.Map varelemtypes, java.util.Map functions,
    Vector types, Vector ents)
  { if ("baseType".equals(tag))
    { if ("DATE".equalsIgnoreCase(value))
      { addRequiredLibrary("OclDate"); 
        return new Type("OclDate", null);
      }
      else if ("INTEGER".equalsIgnoreCase(value))
      { return new Type("int", null); } 
      else if ("LONG".equalsIgnoreCase(value))
      { return new Type("long", null); }
      else if ("BOOLEAN".equalsIgnoreCase(value))
      { return new Type("boolean", null); } 
      else if ("SINGLE".equalsIgnoreCase(value))
      { return new Type("double", null); } 
      else if ("DOUBLE".equalsIgnoreCase(value))
      { return new Type("double", null); } 
      else if ("STRING".equalsIgnoreCase(value))
      { return new Type("String", null); } 
      else if ("VARIANT".equalsIgnoreCase(value))
      { return new Type("OclAny", null); } 
    } 
    return null; 
  } 

  public Expression vbToKM3expression(java.util.Map vartypes, 
    java.util.Map varelemtypes, java.util.Map functions, Vector types, Vector ents)
  { if ("ambiguousIdentifier".equals(tag))
    { Type t = (Type) vartypes.get(value); 
      if (t != null) 
      { Attribute att = 
          new Attribute(value,t,ModelElement.INTERNAL); 
        return new BasicExpression(att);
      } 
      BehaviouralFeature bf = 
        (BehaviouralFeature) functions.get(value); 
      if (bf != null) 
      { return new BasicExpression(bf); } 
      return BasicExpression.newVariableBasicExpression(
                                        value); 
    } 

    if ("integerLiteral".equals(tag) || 
        "octalLiteral".equals(tag))
    { BasicExpression v = new BasicExpression(value); 
      if (Expression.isInteger(value))
      { v.setType(new Type("int",null)); 
        v.setUmlKind(Expression.VALUE);
        v = 
          new BasicExpression(
                Expression.convertInteger(value));  
      }
      else if (Expression.isLong(value))
      { v.setType(new Type("long",null)); 
        v.setUmlKind(Expression.VALUE); 
        v = 
          new BasicExpression(
                Expression.convertLong(value));
      }
    } 

    if ("doubleLiteral".equals(tag))
    { BasicExpression v = new BasicExpression(value); 
      if (Expression.isDouble(value))
      { v.setType(new Type("double",null)); 
        v.setUmlKind(Expression.VALUE); 
      } 
      return v; 
    }
   

    if ("True".equalsIgnoreCase(value))
    { return new BasicExpression(true); } 
    
    if ("False".equalsIgnoreCase(value))
    { return new BasicExpression(false); } 

    if ("Nothing".equalsIgnoreCase(value) ||
        "Null".equalsIgnoreCase(value))
    { return BasicExpression.newValueBasicExpression(
                                             "null"); 
    } 

    return null; 
  }    


  /* General functions */ 


  public boolean isCharacter()
  { if (value.length() > 2 && 
        value.charAt(0) == '\'' && 
        value.charAt(value.length()-1) == '\'')
    { return true; } 
    return false; 
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
  { if (tag.equals("floatLiteral") ||
        tag.equals("doubleLiteral")) 
    { return true; } 
    if (Expression.isDouble(value))
    { return true; } 
    return false; 
  } 

  public boolean isBoolean()
  { if (value.equalsIgnoreCase("true") || 
        value.equalsIgnoreCase("false"))
    { return true; } 
    return false; 
  } // Ok for Java, C, VB6 and OCL.

  public boolean isString() 
  { return Expression.isString(value); } 
 

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

  public ASTTerm updatedObject()
  { return this; } 

  public boolean updatesObject(ASTTerm t)
  { return false; } 

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
  { return value; } 

  public String antlrElement2cstl(Vector rulerefs, Vector conds)
  { if ("terminal".equals(tag))
    { if ("'".equals(value.charAt(0) + ""))
      { value = value.substring(1); } 
      if ("'".equals(value.charAt(value.length()-1) + ""))
      { value = value.substring(0,value.length()-1); } 
      return value + " "; 
    } 

    if ("ruleref".equals(tag))
    { int i = rulerefs.size(); 
      rulerefs.add(value); 
      conds.add("_" + (i+1) + " " + value); 

      return "_" + (i+1) + " "; 
    }

    return ""; 
  } 

  public Vector normaliseAntlr()
  { Vector alts = new Vector(); 
    Vector thisalt = new Vector(); 
    thisalt.add(this); 
    alts.add(thisalt); 
    return alts; 
  } 

  public int cobolDataWidth()
  { if ("integerLiteral".equals(tag))
    { return value.length(); }

    if ("X".equals(value) || "9".equals(value) || 
        ",".equals(value) || "A".equals(value) ||
        "B".equals(value) || "0".equals(value) ||
        "/".equals(value) || "+".equals(value) ||
        "-".equals(value) || "$".equals(value) ||
        "£".equals(value) || ".".equals(value) ||
        "V".equals(value) || "P".equals(value) || 
        "Z".equals(value) || "*".equals(value))
    { return 1; } 

    // S, P, V do not add to length. 
 
    return 0; 
  } 

  public int cobolIntegerWidth()
  { if ("integerLiteral".equals(tag))
    { return value.length(); }

    if ("9".equals(value) || "0".equals(value) || 
        "P".equals(value))
    { return 1; } 
 
    return 0; 
  } 

  public int cobolFractionWidth()
  { if ("integerLiteral".equals(tag))
    { return value.length(); }

    if ("9".equals(value) || "0".equals(value) || 
        "P".equals(value))
    { return 1; } 
 
    return 0; 
  } 

  public Type cobolDataType()
  { if ("integerLiteral".equals(tag) || "9".equals(value) ||
        "S".equals(value))
    { return ASTCompositeTerm.intType; }

    if ("X".equals(value) || 
        ",".equals(value) || "A".equals(value) ||
        "B".equals(value) || "0".equals(value) ||
        "/".equals(value) || "+".equals(value) ||
        "-".equals(value) || "$".equals(value) ||
        "£".equals(value) || ".".equals(value) ||
        "Z".equals(value) || "*".equals(value))
    { return ASTCompositeTerm.stringType; } 

    if ("P".equals(value) || "V".equals(value))
    { return ASTCompositeTerm.doubleType; } 
 
    return null; 
  } 

  public boolean cobolIsSigned()
  { if ("S".equals(value))
    { return true; }
 
    return false; 
  } 

  public Vector cobolDataDefinitions(java.util.Map context, Vector invs)
  { // Each immediately higher level item becomes an attribute
    // of container. If composite, it also becomes a class
    Vector res = new Vector(); 
    return res; 
  } 

  public Vector cobolPerformThruDefinitions(java.util.Map context, Vector invs)
  { Vector res = new Vector(); 
    return res; 
  } 
 
  public static void main(String[] args)
  { ASTBasicTerm tt = new ASTBasicTerm("primaryExpression", "'a'"); 
    System.out.println(tt.isCharacter()); 

    ASTBasicTerm ttrr = new ASTBasicTerm("numericLiteral", "0,552"); 
    // "PAY-CHECKS-MAY"); 
    ASTTerm ttx = ttrr.replaceCobolIdentifiers(); 
    System.out.println(ttx); 
  } 

  public ASTTerm mathOCLSubstitute(String var, ASTTerm repl)
  { // System.out.println(">>> MathOCL term " + this); 

    if ("identifier".equals(tag))
    { if (var.equals(value)) 
      { return repl; } 
      return new ASTBasicTerm(tag, value); 
    }

    return this; 
  } 
 

  public void checkMathOCL()
  { // System.out.println(">>> MathOCL term " + this); 

    if ("identifier".equals(tag))
    { ASTTerm t1 = getTerm(0); 
      String vv = t1.literalForm(); 

      if ("e".equals(vv))
      { return; } 

      Object val = ASTTerm.mathoclvars.get(vv); 
      if (val == null) 
      { // JOptionPane.showMessageDialog(null, 
        //   "Warning!: variable " + vv + " does not have a definition",   "",
        //   JOptionPane.WARNING_MESSAGE);
        System.err.println("!! Warning: " + vv + " has no definition");  
      } 
    }
  } 

  public Vector mathOCLVariables()
  { // System.out.println(">>> MathOCL term " + this); 
    Vector res = new Vector(); 

    if ("identifier".equals(tag))
    { ASTTerm t1 = getTerm(0); 
      String vv = t1.literalForm(); 
      res.add(vv); 
    }

    return res; 
  } 

} 