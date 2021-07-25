/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: CSTL */ 

import java.util.Vector; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 


public class CGRule
{ String lhs;
  String rhs;
  Vector variables; // The _i in lhs -- 
                    // no additional _i should be in rhs
  Vector metafeatures; // The _i`f in rhs
  String lhsop = "";
  Expression lhsexp = null; // For expression rules

  Vector lhsTokens = new Vector(); // String

  Vector conditions;
  String lhspattern = ""; // The LHS string as a regex pattern
  Vector lhspatternlist = new Vector(); 

  public CGRule(Expression lexp, Expression rexp, Vector whens)
  { Vector lvars = lexp.metavariables();
    Vector rvars = rexp.metavariables();
    
    if (lvars.containsAll(rvars)) {}
    else
    { System.err.println("!! Error: some extra metavariables on RHS of " + lexp + " |--> " + rexp); }
    lhs = lexp + "";
    rhs = rexp + "";
    variables = lvars;
    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Expression lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;
    lhsexp = lexp; 

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Expression lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;
    lhsexp = lexp; 

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Statement lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Statement lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(Type lexp, String rgt, Vector whens)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }


  public CGRule(Type lexp, String rgt)
  { variables = lexp.metavariables();
    lhs = lexp + "";
    rhs = rgt;

    Vector rvariables = metavariables(rgt); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs); 
  }

  public CGRule(String ls, String rs, Vector vs, Vector whens)
  { lhs = ls;
    rhs = rs;
    variables = vs;

    Vector rvariables = metavariables(rs); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = whens;
    metafeatures = metafeatures(rhs); 
  }

  public Vector getVariables()
  { return variables; } 

  public void setLHSTokens(Vector toks)
  { lhsTokens = toks; } 

  public boolean equalsLHS(CGRule r) 
  { return r.lhs.equals(lhs); } 

  public int variableCount()
  { if (variables == null) 
    { return 0; } 
    return variables.size(); 
  } 

  public boolean hasVariables()
  { return variables != null && variables.size() > 0; } 

  public static Vector metavariables(String str) 
  { Vector res = new Vector(); 
    for (int i = 1; i < 10; i++) 
    { String var = "_" + i; 
      if (str.indexOf(var) > -1) 
      { res.add(var); } 
    } 
    return res; 
  } 

  public static Vector metafeatures(String str) 
  { Vector res = new Vector();
    String substr = "" + str; 
 
    for (int i = 1; i < 10; i++) 
    { String var = "_" + i + "`";
      substr = "" + str; 
      while (substr.indexOf(var) > -1) 
      { int j = substr.indexOf(var); 
        String f = var; 

        boolean found = false; 
        for (int k = j+3; k < substr.length() && !found; k++) 
        { if (Character.isLetter(substr.charAt(k)))
          { f = f + substr.charAt(k); } 
          else 
          { if (res.contains(f)) { } 
            else 
            { res.add(f); } 
            found = true; 
          } 
        } 

        System.out.println(">>> found metafeature " + f + " for " + var); 
        if (res.contains(f)) { } 
        else 
        { res.add(f); }
        substr = substr.substring(j+4);  
      } 
    } 
    return res; 
  } // look for identifier starting from the `

  public String toString() 
  { String res = lhs + " |-->" + rhs; 
    if (conditions != null && conditions.size() > 0) 
    { res = res + "<when> "; 
      for (int i = 0; i < conditions.size(); i++) 
      { CGCondition cnd = (CGCondition) conditions.get(i); 
        res = res + cnd;
        if (i < conditions.size() - 1) 
        { res = res + ", "; }
      } 
    }
    return res;  
  } 

  public Expression getLhsExpression()
  { return lhsexp; } 
 

  public void addCondition(CGCondition cond)
  { conditions.add(cond); }

  public boolean hasCondition(String prop)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equals(cond.stereotype) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasNegativeCondition(String prop)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equalsIgnoreCase(cond.stereotype) && !cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasCondition(String prop, String var)
  { for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equalsIgnoreCase(cond.stereotype) && var.equals(cond.variable) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasNoCondition()
  { if (conditions.size() == 0)
    { return true; } 
    return false;
  }

  public boolean satisfiesConditions(Vector args, Vector entities)
  { return CGCondition.conditionsSatisfied(conditions,args,entities); } 

  public String applyRule(Vector args)
  { // substitute variables[i] by args[i] in rhs
    String res = rhs + "";
    for (int x = 0; x < args.size() && x < variables.size(); x++)
    { String var = (String) variables.get(x);
      String arg = (String) args.get(x);
      String arg1 = correctNewlines(arg); 
      // System.out.println(">--> Replacing " + var + " by " + arg1); 
      // res = res.replaceAll(var,arg1); For old Java version
      res = res.replace(var,arg1); 
    }
    return res;
  }

  public String applyRule(Vector args, Vector eargs, CGSpec cgs)
  { // substitute metafeatures[j] by the cgs transformation 
    // of the value of eargs[j] metafeature
    // substitute variables[i] by args[i] in rhs
    
    System.out.println(">***> Metafeatures of rule " + this + " are " + metafeatures); 

    String res = rhs + "";
    for (int j = 0; j < metafeatures.size(); j++) 
    { String mf = (String) metafeatures.get(j); 
      String mfvar = mf.substring(0,2); 
      String mffeat = mf.substring(3,mf.length());

      if ("*".equals(mfvar.charAt(1) + "")) 
      { continue; } // No metafeatures on _* variable

      int k = Integer.parseInt(mfvar.charAt(1) + "");  

      System.out.println(">***> Trying to apply metafeature " + mffeat + " to " + eargs + "[" + k + "]"); 
      System.out.println(); 

      if (k >= 1 && k <= eargs.size())
      { Object obj = eargs.get(k-1);
  
        System.out.println(">***> Applying metafeature " + mffeat + " to " + obj + " : " + obj.getClass().getName()); 
        System.out.println(); 

        if ("defaultValue".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Expression exp = ee.getDefaultValueExpression(); 
          if (exp != null) 
          { String repl = exp.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
            res = res.replace(mf,repl1);
          } 
        }
        else if ("defaultSubclass".equals(mffeat) && obj instanceof Entity)
        { Entity ee = (Entity) obj; 
          Entity esub = ee.getDefaultSubclass(); 
          if (esub != null) 
          { String repl = esub.getName(); 
            System.out.println(">--> Replacing " + mf + " by " + repl); 
            res = res.replace(mf,repl);
          } 
        }
        else if ("defaultSubclass".equals(mffeat) && obj instanceof Type)
        { Entity ee = ((Type) obj).getEntity(); 
          Entity esub = ee.getDefaultSubclass(); 
          if (esub != null) 
          { String repl = esub.getName(); 
            System.out.println(">--> Replacing " + mf + " by " + repl); 
            res = res.replace(mf,repl);
          } 
        }
        else if ("alias".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Type t = ee.getAlias(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
            res = res.replace(mf,repl1); 
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing metafeature " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
            res = res.replace(mf,repl1);
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Type t = ee.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
            res = res.replace(mf,repl1);
          } 
        }
        else if ("elementType".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("type".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        }
        else if ("type".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("type".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("typename".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
        }
        else if ("typename".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
        } 
        else if ("typename".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
        } 
        else if ("elementType".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature bf = (BehaviouralFeature) obj; 
          Type t = bf.getElementType(); 
          if (t != null) 
          { String repl = t.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        }
        else if ("owner".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Entity et = att.getOwner(); 
          if (et != null) 
          { String repl = et.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("owner".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Entity et = e.getOwner(); 
          if (et != null) 
          { String repl = et.cg(cgs); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("ownername".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Entity et = att.getOwner(); 
          if (et != null) 
          { String repl = et.getName(); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("ownername".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Entity et = e.getOwner(); 
          if (et != null) 
          { String repl = et.getName(); 
            String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if ("formalName".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Attribute fp = e.formalParameter; 

          System.out.println(">>** Replacing " + e + "`formalName by " + fp); 
		  
          if (fp != null) 
          { String repl = fp.getName(); 
            res = res.replace(mf,repl);
          } 
          else 
          { res = res.replace(mf,"_"); }
        }
        else if ("upper".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          int upper = e.upperBound(); 
		  
          System.out.println(">> Replacing " + e + "`upper by " + upper); 
		  
          res = res.replace(mf,upper + ""); 
        }
        else if ("upper".equals(mffeat) && obj instanceof Attribute)
        { Attribute e = (Attribute) obj; 
          int upper = e.upperBound(); 
		  
          System.out.println(">> Replacing " + e + "`upper by " + upper); 
		  
          res = res.replace(mf,upper + ""); 
        }
        else if ("lower".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          int lower = e.lowerBound(); 
		  
          System.out.println(">> Replacing " + e + "`lower by " + lower); 
		  
          res = res.replace(mf,lower + ""); 
        }
        else if ("lower".equals(mffeat) && obj instanceof Attribute)
        { Attribute e = (Attribute) obj; 
          int lower = e.lowerBound(); 
		  
          System.out.println(">> Replacing " + e + "`lower by " + lower); 
		  
          res = res.replace(mf,lower + ""); 
        }
        else if ("name".equals(mffeat) && obj instanceof ModelElement)
        { ModelElement e = (ModelElement) obj; 
          String repl = e.getName(); 
          if (repl != null) 
          { String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        } 
        else if (CSTL.hasTemplate(mffeat + ".cstl")) 
        { CGSpec template = CSTL.getTemplate(mffeat + ".cstl"); 
          if (template != null) 
          { System.out.println(">>> Applying CSTL template " + mffeat + " to " + obj); 

            String repl = null; 
            if (obj instanceof ModelElement)
            { ModelElement e = (ModelElement) obj; 
              repl = e.cg(template);
            } 
            else if (obj instanceof ASTTerm)
            { ASTTerm e = (ASTTerm) obj; 
              repl = e.cg(template);
            } 
            else if (obj instanceof Vector)
            { Vector v = (Vector) obj;
              repl = "";  
              for (int p = 0; p < v.size(); p++) 
              { ModelElement kme = (ModelElement) v.get(p); 
                repl = repl + kme.cg(template); 
              } 
            } 

            if (repl != null) 
            { String repl1 = correctNewlines(repl);
              // res = res.replaceAll(mf,repl1);
              res = res.replace(mf,repl1); 
            }  // _1`file for template file.cstl
            System.out.println(">>> Replaced form is: " + res); 
          } 
        } 
        else if (obj instanceof ASTTerm)
        { ASTTerm term = (ASTTerm) obj; 

          System.out.println(">***> Applying " + mffeat + " to " + obj); 
          System.out.println(); 
          
          if ("type".equals(mffeat))
          { String repl = ASTTerm.getType(term); 
            if (repl != null)   
            { res = res.replace(mf,repl); }  
          }   
          else if ("first".equals(mffeat))
          { // get first subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              if (ct.terms.size() > 0)
              { ASTTerm ct1 = (ASTTerm) ct.terms.get(0); 
                String repl = ct1.cg(cgs); 
                String repl1 = correctNewlines(repl); 
              
                res = res.replace(mf,repl1);
              } 
            } 
            else // it is the term itself
            { String repl = ((ASTTerm) obj).cg(cgs); 
              String repl1 = correctNewlines(repl); 
              
              res = res.replace(mf,repl1);
            } 
          }   
          else if ("second".equals(mffeat))
          { // get second subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              if (ct.terms.size() > 1)
              { ASTTerm ct1 = (ASTTerm) ct.terms.get(1); 
                String repl = ct1.cg(cgs); 
                String repl1 = correctNewlines(repl); 
              
                res = res.replace(mf,repl1);
              } 
            } 
          }   
          else if ("third".equals(mffeat))
          { // get third subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              if (ct.terms.size() > 2)
              { ASTTerm ct1 = (ASTTerm) ct.terms.get(2); 
                String repl = ct1.cg(cgs); 
                String repl1 = correctNewlines(repl); 
              
                res = res.replace(mf,repl1);
              } 
            } 
          }   
          else if ("fourth".equals(mffeat))
          { // get 4th subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              if (ct.terms.size() > 3)
              { ASTTerm ct1 = (ASTTerm) ct.terms.get(3); 
                String repl = ct1.cg(cgs); 
                String repl1 = correctNewlines(repl); 
              
                res = res.replace(mf,repl1);
              } 
            } 
          }   
          else if ("fifth".equals(mffeat))
          { // get 5th subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              if (ct.terms.size() > 4)
              { ASTTerm ct1 = (ASTTerm) ct.terms.get(4); 
                String repl = ct1.cg(cgs); 
                String repl1 = correctNewlines(repl); 
              
                res = res.replace(mf,repl1);
              } 
            } 
          }   
          else if ("last".equals(mffeat))
          { // get first subterm of obj
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              int tsize = ct.terms.size(); 
              ASTTerm ct1 = (ASTTerm) ct.terms.get(tsize-1); 
              String repl = ct1.cg(cgs); 
              String repl1 = correctNewlines(repl); 
              
              res = res.replace(mf,repl1);
            } 
          }   
          else if ("tail".equals(mffeat))
          { // Vector of terms except the first
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              Vector tailterms = new Vector(); 
              tailterms.addAll(ct.terms);
              String repl = ""; 
              for (int q = 1; q < tailterms.size(); q++) 
              { ASTTerm ct1 = (ASTTerm) tailterms.get(q); 
                String tcg = ct1.cg(cgs);
                repl = repl + tcg; 
              }  
              String repl1 = correctNewlines(repl); 
              
              res = res.replace(mf,repl1);
            } 
          } 
          else if ("tailtail".equals(mffeat))
          { // Vector of terms except the first
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              Vector tailterms = new Vector(); 
              tailterms.addAll(ct.terms);
              String repl = ""; 
              for (int q = 2; q < tailterms.size(); q++) 
              { ASTTerm ct1 = (ASTTerm) tailterms.get(q); 
                String tcg = ct1.cg(cgs);
                repl = repl + tcg; 
              }  
              String repl1 = correctNewlines(repl); 
              
              res = res.replace(mf,repl1);
            } 
          } 
          else if (cgs.hasRuleset(mffeat))
          { System.out.println(">***> Valid ruleset " + mffeat);  
            System.out.println(); 
            String repl = cgs.applyRuleset(mffeat,(ASTTerm) obj);
            System.out.println(">***> Applying ruleset " + mffeat + " to ASTTerm " + obj); 
            System.out.println(); 

            if (repl != null) 
            { String repl1 = correctNewlines(repl); 
              System.out.println(">--> Replacing " + mf + " by " + repl1); 
              res = res.replace(mf,repl1);
            } 
          } 
          else 
          { System.out.println(">!!!> no ruleset: " + mffeat); 
            if (term.hasMetafeature(mffeat))
            { String repl = term.getMetafeatureValue(mffeat); 
              if (repl != null) 
              { String repl1 = correctNewlines(repl); 
                // System.out.println(">--> Replacing " + mf + " by " + repl1); 
                res = res.replace(mf,repl1);
              }
            }
          }  
        }
        /* else if (obj instanceof Vector)
        { Vector v = (Vector) obj;
          String repl = "";
          if ("first".equals(mffeat))
          { Object v1 = v.get(0);   
            repl = v1.cg(cgs); 
            String repl1 = correctNewlines(repl); 
              
            res = res.replaceAll(mf,repl1);
          }
        } */  
        else 
        { System.err.println("!! Warning: could not apply metafeature " + mffeat + " to " + obj); } 
      } 
    }

    // Should check for satisfaction of conditions *after* such substitutions 

    // Extend this to allow users to define their own metafeatures in the specification
    // def: _x`f = _x.expr for some abstract syntax OCL expr. 
 
    System.out.println(">***> RHS after replacement of metafeatures: " + res); 
    System.out.println(); 

    for (int x = 0; x < args.size() && x < variables.size(); x++)
    { String var = (String) variables.get(x);
      String arg = (String) args.get(x);
      String arg1 = correctNewlines(arg); 
      // System.out.println(">--> Replacing " + var + " by " + arg1); 
      // res = res.replaceAll(var,arg1);
      res = res.replace(var,arg1);
    }
    return res;
  }
  
  public String applyTextRule(String actualText)
  { String res = "" + rhs; 
    // lhspattern = convertToPattern(lhs); 
    lhspatternlist = convertToPatterns(lhs);
	 
    // Pattern expr = Pattern.compile(lhspattern); 

    // Matcher m = expr.matcher(actualText); 

    // boolean found = m.find(); 
	  
    /* if (found)
    { int c = m.groupCount(); 
      // System.out.println(m);
	  
      for (int x = 0; x+1 <= c && x < variables.size(); x++)
      { String var = (String) variables.get(x);
        String arg = m.group(x+1);
        // String arg1 = correctNewlines(arg); 
        System.out.println(">--> Replacing " + var + " by " + arg); 
        res = res.replaceAll(var,arg);
      }
    } 
	else */ 
  
     Vector matchings = new Vector(); 
	 boolean found = checkPatternList(actualText,matchings); 
	 if (found && matchings.size() >= variableCount() && matchings.size() > 0) 
	 { System.out.println(">-->> Match of " + actualText + " to " + lhspatternlist);
	   for (int i = 0; i < variables.size(); i++) 
	   { String var = variables.get(i) + ""; 
	     String arg = (String) matchings.get(i); 
		 System.out.println(">--> Replacing " + var + " by " + arg); 
         res = res.replaceAll(var,arg);
      } 
	}  
	
    return res;
  }

  public static String correctNewlines(String str) 
  { String res = ""; 
    if (str.length() == 0) 
    { return res; } 

    boolean instring = false; 

    for (int i = 0; i < str.length() - 1; i++) 
    { char c1 = str.charAt(i); 
      char c2 = str.charAt(i+1);

      if (c1 == '"' && instring) 
      { instring = false; } 
      else if (c1 == '"')
      { instring = true; } 
 
      if (c1 == '\\' && c2 == 'n' && !instring)
      { res = res + '\n'; 
        i++;
        if (i == str.length() - 1)
        { return res; }  
      } 
      // else if (c1 == '\\' && instring) 
      // { res = res + "\\\\"; } 
      /* else if (c1 == '\\' && c2 == 'n' && instring)
      { res = res + "\\\\n"; 
        i++;
        if (i == str.length() - 1)
        { return res; }  
      } 
      else if (c1 == '\\' && c2 == '(' && instring)
      { res = res + "\\\\("; 
        i++;
        if (i == str.length() - 1)
        { return res; }  
      } 
      else if (c1 == '\\' && c2 == ')' && instring)
      { res = res + "\\\\)"; 
        i++;
        if (i == str.length() - 1)
        { return res; }  
      } */ 
      else 
      { res = res + c1; } 
    } 

    return res + str.charAt(str.length()-1); 
  } 
  
  public static Vector convertToPatterns(String str)
  { Vector res = new Vector(); 
    
    String fres = ""; 
    for (int i = 0; i < str.length(); i++) 
    { char c1 = str.charAt(i); 
	  if (i == str.length() - 1)
	  { fres = fres + c1;
	    res.add(fres);  
	    break; 
	  }
      char c2 = str.charAt(i+1); 
      if (c1 == '_' && 
          (c2 == '1' || c2 == '2' || c2 == '3' || c2 == '4' 
           || c2 == '5' || c2 == '6' || c2 == '7' ||
           c2 == '8' || c2 == '9'))
      { res.add(fres);  
	    res.add(("" + c1) + c2); 
	    fres = ""; 
        i++; 
      } 
      else 
      { fres = fres + c1; } 
    } 
	System.out.println("String list = " + res); 
	return res; 
  }
  
  public boolean checkPatternList(String text, Vector matched)
  { if (lhspatternlist == null)
    { return false; } 
  
    if (lhspatternlist.size() == 0)
    { return false; } 
  
    int pos = 0; 
    int i = 0;  
	
    while (i < lhspatternlist.size()) 
    { String tomatch = (String) lhspatternlist.get(i);
	  // System.out.println(">> matching " + tomatch); 
	  
	 if (tomatch.indexOf("_") >= 0)  // variable
	 { String found = ""; 
	   boolean continuematch = true; 
		
	   if (i < lhspatternlist.size() - 1)
	   { char startnext = ((String) lhspatternlist.get(i+1)).charAt(0); 
		int j = pos; 
		while (j < text.length() && continuematch)
		{ if (text.charAt(j) != startnext)
		  { found = found + text.charAt(j);
		    j++;
		  }
	        else if (found.length() > 0)
		   { System.out.println("--> Found text " + found + " for " + tomatch);
			pos = j;  
			matched.add(found); 
			continuematch = false; 
	        }
		   else 
		   { return false; }
		 }
		 i++; 
		}
		else // for the last text segment in the list
		{ for (int j = pos; j < text.length(); j++)
		  { found = found + text.charAt(j); }
		 
		  if (found.length() > 0) 
		  { System.out.println("--> Found text " + found + " for " + tomatch);
		    matched.add(found); 
		    return true;
		  } 
		  else 
		  { return false; } 
		}
	  }
	  else 
	  { int spos = 0;  
	    boolean continuematch = true; 
	    int j = pos; 
		while (j < text.length() && continuematch) 
	    { char x = text.charAt(j); 
	      if (spos < tomatch.length())
		  { if (tomatch.charAt(spos) == x) 
            { spos++; 
		      pos++;
		      j++; 
			  // System.out.println("Consumed " + x);
			} 
			else 
			{ // System.out.println("Mismatch: " + tomatch.charAt(spos) + " /= " + x); 
			  return false; 
			}  
		  }
		  else // go to next segment to match 
		  { j++;  
		    // matched.add(tomatch); 
		    continuematch = false;  
		  }
		}
		i++; 
	  }
	  // i++; 
	}
	// System.out.println(">>> Match list= " + matched); 
	return true; 
  } 


  public static String convertToPattern(String str) 
  { String res = ""; 
    if (str.length() == 0) 
    { return res; } 

    for (int i = 0; i < str.length(); i++) 
    { char c1 = str.charAt(i); 
      if (c1 == '(')
      { res = res + "\\("; }
      else if (c1 == ')')
      { res = res + "\\)"; }
      else if (c1 == '[' || c1 == ']' ||
          c1 == '*' || c1 == '.' || c1 == '?' || c1 == '{' ||
          c1 == '}')
      { res = res + '\\' + c1; } 
      else 
      { res = res + c1; } 
    } 

    String fres = ""; 
    for (int i = 0; i < res.length() - 1; i++) 
    { char c1 = res.charAt(i); 
      char c2 = res.charAt(i+1); 
      if (c1 == '_' && 
          (c2 == '1' || c2 == '2' || c2 == '3' || c2 == '4' 
           || c2 == '5' || c2 == '6' || c2 == '7' ||
           c2 == '8' || c2 == '9'))
      { fres = fres + "(.+)"; 
        i++; 
      } 
      else 
      { fres = fres + c1; } 
    } 
    return fres + res.charAt(res.length()-1); 
  } 

  public static void main(String[] args) 
  { // System.out.println(metafeatures("for (_1`elementType _2 : _1) do { _3 }")); 
    /* Vector vars = new Vector(); 
    vars.add("_1");
    vars.add("_2");
    CGRule r = new CGRule("createByPK_1(_2)", "createByPK_1(index: _2)", vars, new Vector());
    String rr = r.applyTextRule("createByPKPerson(x)");
    System.out.println(rr);  
	
    Vector patts = convertToPatterns("createByPK_1(_2)");
    r.lhspatternlist = patts; 
    Vector matched = new Vector(); 
    boolean b = r.checkPatternList("createByPKPerson(x)", matched);
    System.out.println(b); */    

    Pattern expr = Pattern.compile("([a-z]+)...([a-z]+)"); 

    Matcher m = expr.matcher("x = abc"); 

    boolean found = m.find(); 
	  
    if (found)
    { int c = m.groupCount(); 
      System.out.println(m + " " + c);
	  
      for (int x = 0; x+1 <= c; x++)
      { // String var = (String) variables.get(x);
        String arg = m.group(x+1);
        // String arg1 = correctNewlines(arg); 
        // System.out.println(">--> Replacing " + var + " by " + arg); 
        // res = res.replaceAll(var,arg);
        System.out.println("Group " + (x+1) + " is " + arg); 
      }
    } 

  } 
}
