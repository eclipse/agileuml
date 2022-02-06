/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: CSTL */ 

/* Changed in 13.11.2021: metafeatures can consist of letters
   *and* digits, eg. _1`2nd
*/ 

import java.util.Vector; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import java.io.*; 


public class CGRule
{ String lhs;
  String rhs;
  Vector variables; // The _i or _* in the lhs -- 
                    // no additional variable should be in rhs
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

  public CGRule(String ls, String rs)
  { lhs = ls; 
    rhs = rs; 
    variables = metavariables(ls); 

    Vector rvariables = metavariables(rs); 
    if (variables.containsAll(rvariables)) { } 
    else 
    { System.err.println("!! Error: some extra metavariables on RHS of " + this); }

    conditions = new Vector();
    metafeatures = metafeatures(rhs);
    String[] toks = ls.split(" "); 
    for (int i = 0; i < toks.length; i++) 
    { lhsTokens.add(toks[i]); }  
  }

  public static boolean hasDefaultRule(Vector rules)
  { for (int i = 0; i < rules.size(); i++) 
    { CGRule rr = (CGRule) rules.get(i); 
      if ("_0".equals(rr.lhs.trim()) && 
          "_0".equals(rr.rhs.trim())) 
      { return true; } 
    } 
    return false; 
  } // Could add conditions. 

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
    for (int i = 0; i < 10; i++) 
    { String var = "_" + i; 
      if (str.indexOf(var) > -1) 
      { res.add(var); } 
    } 

    if (str.indexOf("_*") > -1)
    { res.add("_*"); } 

    return res; 
  } 

  public static Vector metafeatures(String str) 
  { Vector res = new Vector();
    String substr = "" + str; 
 
    for (int i = 0; i < 10; i++) 
    { String var = "_" + i + "`";

      if (i == 0) 
      { var = "_*`"; } 

      substr = "" + str; 
      while (substr.indexOf(var) > -1) 
      { int j = substr.indexOf(var); 
        String f = var; 

        boolean found = false; 
        for (int k = j+3; k < substr.length() && !found; k++) 
        { if (Character.isLetterOrDigit(substr.charAt(k)))
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
  } // look for identifier/with digits starting from the `

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

  public boolean equals(Object other)
  { if (other instanceof CGRule)
    { String ostring = other + ""; 
      if (ostring.equals(toString()))
      { return true; } 
    } 
    return false; 
  } 

  public int compareTo(CGRule r)
  { String rlhs = r.lhs + ""; 

    if (rlhs.equals(lhs + ""))
    { if (conditions == null && r.conditions != null) 
      { return 1; } // More general than r

      if (conditions != null && r.conditions == null) 
      { return -1; } // More specific than r

      if (conditions != null && r.conditions != null && 
          conditions.containsAll(r.conditions))
      { if (r.conditions.containsAll(conditions))
        { return 0; } // Conflict
        else 
        { return -1; } 
      } 

      if (conditions != null && r.conditions != null && 
          r.conditions.containsAll(conditions))
      { return 1; } // r is more specialised 
    } 

    if (rlhs.indexOf(lhs + "") >= 0)
    { // lhs is substring of rlhs, 
      // this rule is more general than r
      // It must follow r if they are in one ruleset.
 
      return 1; 
    } 

    if ("_*".equals(lhs + ""))
    { return 1; } 

    if ("_*".equals(rlhs + ""))
    { return -1; } 

    if (lhsTokens.size() == r.lhsTokens.size() && 
        variables.size() > r.variables.size())
    { // r has more non-variable terms, so is more specific
      return 1; 
    } 

    if (lhsTokens.size() < r.lhsTokens.size() && 
        variables.size() == r.variables.size())
    { // r has more non-variable terms, so is more specific
      return 1; 
    } 

    /* if (r.variables.containsAll(variables) && 
        r.variables.size() > variables.size())
    { // r has strictly more variables - so is more specific
      return -1; 
    } 

    if (r.variables.size() > variables.size())
    { // r has strictly more variables - so is more specific
      return -1; 
    } */ 


    if ((lhs + "").indexOf(rlhs) >= 0)
    { return -1; } 
    // this goes before r

    if (lhsTokens.size() == r.lhsTokens.size() && 
        variables.size() < r.variables.size())
    { // this has more non-variable terms, so is more specific
      return -1; 
    } 

    if (lhsTokens.size() > r.lhsTokens.size() && 
        variables.size() == r.variables.size())
    { // this has more non-variable terms, so is more specific
      return -1; 
    } 

/*  if (variables.containsAll(r.variables) && 
        variables.size() > r.variables.size())
    { // this has strictly more variables - so more specific
      return 1; 
    } 

    if (variables.size() > r.variables.size())
    { // this has strictly more variables - more specific
      return 1; 
    } */ 

    return Integer.MAX_VALUE; // incomparable 
  } 

  public Expression getLhsExpression()
  { return lhsexp; } 
 

  public void addCondition(CGCondition cond)
  { if (conditions == null) 
    { conditions = new Vector(); } 
    conditions.add(cond); 
  }

  public boolean hasCondition(String prop)
  { if (conditions == null) 
    { return false; } 
    for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equalsIgnoreCase(cond.stereotype) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasNegativeCondition(String prop)
  { if (conditions == null) 
    { return false; } 
    for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equalsIgnoreCase(cond.stereotype) && !cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasCondition(String prop, String var)
  { if (conditions == null) 
    { return false; } 
    for (int x = 0; x < conditions.size(); x++)
    { CGCondition cond = (CGCondition) conditions.get(x);
      if (prop.equalsIgnoreCase(cond.stereotype) && var.equals(cond.variable) && cond.positive)
      { return true; }
    }
    return false;
  }

  public boolean hasNoCondition()
  { if (conditions == null || conditions.size() == 0)
    { return true; } 
    return false;
  }

  public boolean satisfiesConditions(Vector args, Vector entities)
  { return CGCondition.conditionsSatisfied(conditions,args,entities); } 

  public int variablePosition(String var)
  { // The index of var in the arguments in the LHS
    int varCount = 0; 
    for (int i = 0; i < lhsTokens.size(); i++) 
    { String tok = (String) lhsTokens.get(i); 
      
      if (var.equals(tok))
      { return varCount+1; } 
      
      if (tok.startsWith("_") && 
          tok.length() >= 2 && 
          (tok.charAt(1) == '*' || 
           Character.isDigit(tok.charAt(1))))
      { varCount++; } 
    } 
    return -1; 
  } 

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
    System.out.println(">***> LHS tokens: " + lhsTokens); 
      
    Vector entities = cgs.entities; 
    Vector types = cgs.types; 

    String res = rhs + "";
    for (int j = 0; j < metafeatures.size(); j++) 
    { String mf = (String) metafeatures.get(j); 
      String mfvar = mf.substring(0,2); 
      String mffeat = mf.substring(3,mf.length());

      // if ("*".equals(mfvar.charAt(1) + "")) 
      // { continue; } 

      int k = 0; 
      if ("*".equals(mfvar.charAt(1) + ""))
      { k = variablePosition("_*"); } // the position of * in the vbls
      else 
      { k = Integer.parseInt(mfvar.charAt(1) + ""); }  
      // Actually the argument corresponding to _k

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
        else if ("defaultSubclass".equals(mffeat))
        { String repl = ""; 
          if (obj instanceof Entity)
          { Entity ee = (Entity) obj; 
            Entity esub = ee.getDefaultSubclass(); 
            if (esub != null) 
            { repl = esub.getName(); 
              System.out.println(">--> Replacing " + mf + " by " + repl); 
              res = res.replace(mf,repl);
            }
          } 
          else if (obj instanceof Type)
          { Type etype = (Type) obj; 
            Type tsub = etype.defaultSubtype(entities);
            repl = tsub.cg(cgs);  
            System.out.println(">--> Replacing " + mf + " by " + repl); 
            res = res.replace(mf,repl);  
          }
       /*   else if (obj instanceof ModelElement)
          { repl = ((ModelElement) obj).getName(); 
            System.out.println(">--> Replacing " + mf + " by " + repl); 
            res = res.replace(mf,repl);
          } */ 
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
          System.out.println(">--> Element type of " + e + " is: " + t);
          System.out.println(); 
 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing metafeature " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
          res = res.replace(mf,repl1);
        }
        else if ("elementType".equals(mffeat) && obj instanceof Type)
        { Type ee = (Type) obj; 
          Type t = ee.getElementType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
          res = res.replace(mf,repl1);
        }
        else if ("elementType".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getElementType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
            // res = res.replaceAll(mf,repl1);
          res = res.replace(mf,repl1);
        } 
        else if ("type".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
          res = res.replace(mf,repl1);
        }
        else if ("type".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
          res = res.replace(mf,repl1);
        } 
        else if ("type".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
          res = res.replace(mf,repl1);
        } 
        else if ("typename".equals(mffeat) && obj instanceof Expression)
        { Expression e = (Expression) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
          else 
          { res = res.replace(mf,"OclAny"); } 
        }
        else if ("typename".equals(mffeat) && obj instanceof Attribute)
        { Attribute att = (Attribute) obj; 
          Type t = att.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
          else 
          { res = res.replace(mf,"OclAny"); } 
        } 
        else if ("typename".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature e = (BehaviouralFeature) obj; 
          Type t = e.getType(); 
          if (t != null) 
          { String repl = t.getName(); 
            res = res.replace(mf,repl);
          } 
          else 
          { res = res.replace(mf,"OclAny"); } 
        } 
        else if ("elementType".equals(mffeat) && obj instanceof BehaviouralFeature)
        { BehaviouralFeature bf = (BehaviouralFeature) obj; 
          Type t = bf.getElementType(); 
          if (t == null) 
          { t = new Type("OclAny", null); } 
          String repl = t.cg(cgs); 
          String repl1 = correctNewlines(repl); 
            // System.out.println(">--> Replacing " + mf + " by " + repl1); 
          res = res.replace(mf,repl1);
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
            else if (obj instanceof Expression)
            { Expression e = (Expression) obj; 
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

          System.out.println(">***> Applying " + mffeat + " to ASTTerm " + obj); 
          System.out.println(); 
          
          if ("type".equals(mffeat))
          { String repl = ASTTerm.getType(term);
            System.out.println(">>> Type of " + term + " is " + repl); 
            System.out.println(); 
 
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
          { // Vector of terms except the first 2
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
          else if ("tailtailtail".equals(mffeat))
          { // Vector of terms except the first 3
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              Vector tailterms = new Vector(); 
              tailterms.addAll(ct.terms);
              String repl = ""; 
              for (int q = 3; q < tailterms.size(); q++) 
              { ASTTerm ct1 = (ASTTerm) tailterms.get(q); 
                String tcg = ct1.cg(cgs);
                repl = repl + tcg; 
              }  
              String repl1 = correctNewlines(repl); 
              
              res = res.replace(mf,repl1);
            } 
          } 
          else if ("tailtailtailtail".equals(mffeat))
          { // Vector of terms except the first 4
            if (obj instanceof ASTCompositeTerm)
            { ASTCompositeTerm ct = (ASTCompositeTerm) obj; 
              Vector tailterms = new Vector(); 
              tailterms.addAll(ct.terms);
              String repl = ""; 
              for (int q = 4; q < tailterms.size(); q++) 
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
            else if (CSTL.hasTemplate(mffeat + ".cstl")) // Try to load a new CSTL specification
            { System.out.println(">>> Template exists: " + 
                                 mffeat); 
              CGSpec newcgs = CSTL.getTemplate(mffeat + ".cstl"); 
              System.out.println(); 
              String repl = ((ASTTerm) obj).cg(newcgs);
            
              if (repl != null) 
              { String repl1 = correctNewlines(repl); 
                System.out.println(">--> Replacing " + mf + " by " + repl1); 
                res = res.replace(mf,repl1);
              } 
            } 
            else 
            { System.out.println("!! No template " + mffeat + " exists"); 
              File sub = new File("./cg/" + mffeat + ".cstl");
      
              CGSpec newcgs = new CGSpec(entities,types); 
              CGSpec xcgs = 
                CSTL.loadCSTL(newcgs,sub,types,entities); 
              if (xcgs != null)
              { CSTL.addTemplate(mffeat + ".cstl", xcgs); 
                String rpl = ((ASTTerm) obj).cg(xcgs);   
                String repl1 = correctNewlines(rpl); 
                System.out.println(">--> Replacing " + mf + " by " + repl1); 
                res = res.replace(mf,repl1);
              }              
            } 
          }  
        }
        else if (obj instanceof String && 
                 cgs.hasRuleset(mffeat))
        { System.out.println(">***> Valid ruleset " + mffeat);  
          System.out.println();
          ASTSymbolTerm asymbol = new ASTSymbolTerm(obj + "");  
          String repl = cgs.applyRuleset(mffeat, asymbol);
          System.out.println(">***> Applying ruleset " + mffeat + " to ASTSymbolTerm " + obj); 
          System.out.println(); 

          if (repl != null) 
          { String repl1 = correctNewlines(repl); 
            System.out.println(">--> Replacing " + mf + " by " + repl1); 
            res = res.replace(mf,repl1);
          } 
        }  // Other string functions could be added.  
        else if (obj instanceof Vector) // Of ASTTerm
        { Vector v = (Vector) obj;
          String repl = "";
          if (cgs.hasRuleset(mffeat))
          { System.out.println(">***> Valid ruleset " + mffeat);  
            System.out.println(); 
            String replv = ""; 
            for (int p = 0; p < v.size(); p++)
            { if (v.get(p) instanceof ASTTerm)
              { ASTTerm x = (ASTTerm) v.get(p); 
                replv = replv + cgs.applyRuleset(mffeat,x);
              } 
            } 
            System.out.println(">***> Applying ruleset " + mffeat + " to ASTTerm " + obj); 
            System.out.println(); 

            if (replv != null) 
            { String repl1v = correctNewlines(replv); 
              System.out.println(">--> Replacing " + mf + " by " + repl1v); 
              res = res.replace(mf,repl1v);
            } 
          } 
          else if ("front".equals(mffeat) && v.size() > 0 &&
                   v.get(0) instanceof ASTTerm)
          { String replv = ""; 
            for (int p = 0; p < v.size()-1; p++)
            { if (v.get(p) instanceof ASTTerm)
              { ASTTerm x = (ASTTerm) v.get(p); 
                replv = replv + x.cg(cgs);
              } 
            } 
            String repl1 = correctNewlines(replv); 
              
            res = res.replace(mf,repl1);
          }
          else if ("first".equals(mffeat) && v.size() > 0 &&
                   v.get(0) instanceof ASTTerm)
          { ASTTerm v1 = (ASTTerm) v.get(0);   
            repl = v1.cg(cgs); 
            String repl1 = correctNewlines(repl); 
              
            res = res.replace(mf,repl1);
          }
          else if ("last".equals(mffeat) && v.size() > 0 &&
                   v.get(v.size()-1) instanceof ASTTerm)
          { ASTTerm v1 = (ASTTerm) v.get(v.size()-1);   
            repl = v1.cg(cgs); 
            String repl1 = correctNewlines(repl); 
              
            res = res.replace(mf,repl1);
          }
          System.out.println(">> Applied vector rule: " + res); 
        }   
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
  { CGRule r = new CGRule("_1 _2", "_1>_2"); 
    System.out.println(r.lhsTokens); 
    System.out.println(r.variables); 

    CGRule r1 = new CGRule("error _2", "throw _2"); 
    System.out.println(r1.lhsTokens); 
    System.out.println(r1.variables); 

    System.out.println(r.compareTo(r1)); 
    System.out.println(r1.compareTo(r)); 


    // System.out.println(metafeatures("for (_1`elementType _2 : _1) do { _3 }")); 
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
    System.out.println(b); 

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
    } */ 

  } 
}
