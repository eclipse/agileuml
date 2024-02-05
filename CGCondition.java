/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: CSTL */ 

import java.util.Vector; 
import javax.swing.*;

public class CGCondition
{ String stereotype = "";
  String variable = "";
  boolean positive = true;
  String quantifier = "";
  boolean isSubstitute = false; 
     // e/x means replace x by e 
  boolean isMatches = false; 
     // compare variable to stereotype
  boolean isWith = false; 
  boolean isNested = false; 


  public CGCondition()
  { } 

  public CGCondition(String prop, String var)
  { stereotype = prop;
    variable = var;
  }

  public CGCondition(Expression expr) 
  { // _i = stereo  or  _i /= stereo

    if (expr instanceof BinaryExpression) 
    { BinaryExpression ee = (BinaryExpression) expr; 

      Vector vars = ee.metavariables(); 
      if (vars.size() > 0) 
      { variable = (String) vars.get(0); } 
      else 
      { variable = "_1"; } 
 
      if ("/=".equals(ee.getOperator()))
      { positive = false; } 

      stereotype = ee.getRight() + ""; 
    } 
  } 

  public static CGCondition newCGCondition(
    String category, Expression expr, Vector rulevars) 
  { // _i = stereo  or  _i /= stereo

    String stereo = ""; 
    String var = ""; 
    boolean pos = true; 
    boolean nestedCondition = false; 

    if (expr instanceof BinaryExpression) 
    { BinaryExpression ee = (BinaryExpression) expr; 

      String oper = ee.getOperator(); 

      Vector vars = ee.metavariables(); 
      if (vars.size() > 0) 
      { var = (String) vars.get(0); } 
      else if (category.equals("OclStatement"))
      { var = "_4"; } 
      else 
      { var = "_1"; } 
 
      if ("/=".equals(oper))
      { pos = false; } 
      else if ("isNested".equals(oper))
      { nestedCondition = true; } 
      // otherwise assumed to be "="


      stereo = ee.getRight() + ""; 
    }

    if (rulevars.contains(var))
    { CGCondition res = new CGCondition(stereo,var);
      res.setPositive(pos); 
      res.setIsNested(nestedCondition); 
      return res; 
    } 

    return null;  
  } 

  public Vector metavariables()
  { Vector vars1 = CGRule.metavariables(variable); 
    Vector vars2 = CGRule.metavariables(stereotype); 
    Vector res = VectorUtil.union(vars1,vars2); 
    return res; 
  } 

  public void setVariable(String v) 
  { variable = v; } 

  public boolean hasVariable()
  { if (variable != null && 
        variable.startsWith("_")) 
    { return true; } 
    return false; 
  } 

  public void setVariableMetafeature(String mf) 
  { variable = variable + "`" + mf; } 

  public void setStereotypeMetafeature(String mf) 
  { stereotype = stereotype + "`" + mf; } 

  public void setStereotype(String st) 
  { stereotype = st; } 

  public void addToStereotype(String st) 
  { stereotype = stereotype + st; } 

  public void setPositive(boolean pos)
  { positive = pos; }

  public void setPositive()
  { positive = true; }

  public void setNegative()
  { positive = false; }

  public void setSubstitute()
  { isSubstitute = true; } 

  public void setMatches()
  { isMatches = true; } 

  public void setExistential()
  { quantifier = "any"; }

  public void setUniversal()
  { quantifier = "all"; }

  public void setIsWith(boolean w)
  { isWith = w; } 

  public void setIsNested(boolean w)
  { isNested = w; } 

  public String toString()
  { String res = variable;

    if ("all".equals(quantifier))
    { res = res + " all"; } 
    else if ("any".equals(quantifier))
    { res = res + " any"; }
    else if (isSubstitute)
    { res = res + " /"; }  
    else if (isMatches)
    { res = res + " matches"; }  
    else if (isWith)
    { res = res + " with"; }  
    else if (isNested)
    { res = res + " isNested"; }  
 
    if (positive) { } 
    else  
    { res = res + " not"; } 
    return res + " " + stereotype; 
  } 

  public boolean equals(Object other)
  { if (other instanceof CGCondition)
    { CGCondition cc = (CGCondition) other; 
      if (toString().equals(cc + ""))
      { return true; } 
    } 
    return false; 
  } 

  public String applyPostAction(String res, 
            Vector variables, Vector eargs, Vector newargs,
            CGSpec cgs, Vector entities, Vector rhsVariables)
  { if (isSubstitute)
    { String rep = variable; 
      String varx = variable; 
      String var = stereotype; 

      Vector metafs = CGRule.metafeatures(variable); 

      // System.out.println("***>> Action metafeatures of " + variable + " are: " + metafs); 

      if (metafs.size() > 0)
      { 
        // If the variable has a metafeature: _i`mf
        // evaluate _i`mf in cgs as the rep 

        String mf = (String) metafs.get(0); 
        int mfindex = mf.indexOf("`"); 
        varx = mf.substring(0,mfindex); 
        String mffeat = mf.substring(mfindex+1,mf.length()); 
        if (mffeat != null && variables.contains(varx)) 
        { int i = variables.indexOf(varx); 
          ASTTerm ast = (ASTTerm) eargs.get(i); 
          rep = CGRule.applyMetafeature(
                             mffeat,ast,cgs,entities);
        } 
        else 
        { rep = varx; }  
      } 
      else if (variables.contains(varx))
      { int i = variables.indexOf(varx); 
        rep = (String) newargs.get(i); 
      }
      else if (rhsVariables.contains(varx))
      { rep = ASTTerm.getStereotypeValue(varx); }  
       

      if (variables.contains(stereotype))
      { int i = variables.indexOf(stereotype); 
        var = (String) newargs.get(i); 
      } 
      else if (rhsVariables.contains(stereotype))
      { var = ASTTerm.getStereotypeValue(stereotype); }  

      res = res.replace(var,rep); 
    } 

    return res; 
  } // Either could have a metafeature. 

  public String evaluateProperty(Vector vars, Vector eargs, 
                  Vector reps, CGSpec cgs, 
                  Vector entities, Vector globalVariables)
  { // stereotype can be _i, _$ for LHS or global 
    // variables, or _i`f for metafeature f, or a value 
    // without variables. 
    // Result is evaluation in ruleset cgs with actual args
    // eargs, reps

    String stereo = new String(stereotype); 
    for (int x = 0; x < reps.size() && x < vars.size(); x++)
    { String var = (String) vars.get(x);
      String arg1 = (String) reps.get(x); 

      String svarx = var; 
      String smffeat = null; 
      Vector stereomfs = CGRule.metafeatures(stereo); 
      if (stereomfs.size() > 0)
      { 
        // If stereo is variable with a metafeature: _i`mf
        // evaluate _i`mf in cgs and substitute in stereo

        String smf = (String) stereomfs.get(0); 
        int smfindex = smf.indexOf("`"); 
        svarx = smf.substring(0,smfindex); 
        smffeat = smf.substring(smfindex+1,smf.length()); 
        if (smffeat != null && var.equals(svarx)) 
        { int indv = vars.indexOf(svarx);
 
          if (indv >= 0 && eargs.get(indv) instanceof ASTTerm)
          {  
            ASTTerm earg = (ASTTerm) eargs.get(indv); 
            stereo = CGRule.applyMetafeature(
                               smffeat,earg,cgs,entities); 
          }
        } 
      } 

      stereo = stereo.replace(var,arg1);
    }

    for (int y = 0; y < globalVariables.size(); y++) 
    { String rvar = (String) globalVariables.get(y);
      String varValue = ASTTerm.getStereotypeValue(rvar); 
      if (varValue != null) 
      { stereo = stereo.replace(rvar,varValue); 

        // JOptionPane.showMessageDialog(null, 
        //   "Global variable " + rvar + " value is " + stereo,   "",
        //   JOptionPane.INFORMATION_MESSAGE);

        System.out.println(">--> Replacing global variable " + rvar + " by " + varValue); 
      }
    } // No metafeatures allowed

    return stereo;  
  }


  public void applyAction(Vector vars, Vector eargs, 
                  Vector reps, CGSpec cgs, 
                  Vector entities, Vector globalVariables)
  { // vv stereo
    // means ast.setStereotype(stereorep)
    // where ast is the eargs element of vars variable vv
    // stereorep is stereo with each var 
    // replaced by corresponding rep

    // If vv has no corresponding eargs, then it is a global
    // variable, vv stereo sets its value to stereo. 

    // If vv is ww`mf then evaluate ww`mf (for ruleset
    // or built-in function mf) and add stereo to 
    // stereotypes of the result. If mf is neither
    // a ruleset nor a built-in function, then 
    // it is tagged value setting mf=stereo for ww.

    // _* all stereo 
    // sets the stereotype for each term in the _* list

    // _i`f with _j  uses _j as the parameter _$ in f::

    if (isSubstitute || isMatches || isNested) 
    { return; } 

    String stereo = new String(stereotype); 
    Vector stereomfs = CGRule.metafeatures(stereo); 

    for (int x = 0; x < reps.size() && x < vars.size(); x++)
    { String var = (String) vars.get(x);
      String arg1 = (String) reps.get(x); 

      String svarx = var; 
      String smffeat = null; 
      if (stereomfs.size() > 0)
      { 
        // If stereo has a metafeature: _i`mf
        // evaluate _i`mf in cgs and set stereo to result 

        String smf = (String) stereomfs.get(0); 
        int smfindex = smf.indexOf("`"); 
        svarx = smf.substring(0,smfindex); 
        smffeat = smf.substring(smfindex+1,smf.length()); 
        if (smffeat != null && var.equals(svarx)) 
        { int indv = vars.indexOf(svarx);
 
          if (indv >= 0 && eargs.get(indv) instanceof ASTTerm)
          {  
            ASTTerm earg = (ASTTerm) eargs.get(indv); 
            stereo = CGRule.applyMetafeature(
                             smffeat,earg,cgs,entities); 
          }
        } 
      } 

      stereo = stereo.replace(var,arg1);

      /* JOptionPane.showMessageDialog(null, 
          "LHS variable " + var + " value is " + arg1 + "\n" + 
          " New stereotype value is: " + stereo,   "",
          JOptionPane.INFORMATION_MESSAGE); */
    }

    for (int y = 0; y < globalVariables.size(); y++) 
    { String rvar = (String) globalVariables.get(y);

      String varValue = ASTTerm.getStereotypeValue(rvar); 

      /* JOptionPane.showMessageDialog(null, 
          "Global variable " + rvar + " value is " + varValue + "\n" + 
          "Old stereotype value is: " + stereo + "\n" + 
          "Metafeatures " + stereomfs,   "",
          JOptionPane.INFORMATION_MESSAGE); */ 

      if (stereomfs.size() > 0)
      { 
        // If stereo has a metafeature: rvar`mf
        // evaluate rvar`mf in cgs and set stereo to result 

        String gmf = (String) stereomfs.get(0); 
        int gmfindex = gmf.indexOf("`"); 
        String gvarx = gmf.substring(0,gmfindex); 
        String gmffeat = 
                     gmf.substring(gmfindex+1,gmf.length());
 
        if (gmffeat != null && rvar.equals(gvarx)) 
        { // find the value of varValue`gmffeat
          if (varValue != null) 
          { // stereo = stereo.replace(rvar,varValue); 

            String gmfvalue = 
              ASTTerm.getTaggedValue(varValue, gmffeat); 
            if (gmfvalue != null) 
            { stereo = stereo.replace(gmf,gmfvalue); }
          }
        } 
      } 
      else if (varValue != null) // no stereotype
      { stereo = stereo.replace(rvar,varValue); 

        System.out.println(">--> Replacing global variable " + rvar + " by " + varValue); 
      }

    } 

  
    // System.out.println(">>> Applying action " + variable + " (" + positive + ") " + stereo);  
    // JOptionPane.showMessageDialog(null, 
    //       "Applying action " + variable + " (" + positive + ") " + stereo,   "",
    //       JOptionPane.INFORMATION_MESSAGE); 

    String varx = variable; 
    String mffeat = null; 

    if (quantifier.equals("all") && "_*".equals(varx))
    { // set each term in _* to the stereo 

      int starind = vars.indexOf(varx);
 
      if (starind >= 0)
      { Object obj = eargs.get(starind); 
        if (obj instanceof Vector) 
        { Vector termlist = (Vector) obj; 
          for (int p = 0; p < termlist.size(); p++) 
          { if (termlist.get(p) instanceof ASTTerm)
            { ASTTerm trm = (ASTTerm) termlist.get(p); 
              ASTTerm.setType(trm,stereo);
              ASTTerm.addStereo(trm,stereo);
              System.out.println("***>> Applying action _* all " + stereo + " to: " + trm); 
            }
          }
        }  
      }
      return;  
    }

    if (isWith)
    { ASTTerm.setStereotypeValue("_$", stereo);
      // JOptionPane.showMessageDialog(null, 
      //     "Global variable _$ value is " + stereo,   "",
      //     JOptionPane.INFORMATION_MESSAGE);
    }

    Vector metafs = CGRule.metafeatures(variable); 

    // System.out.println("***>> Action metafeatures of " + variable + " are: " + metafs); 

    if (metafs.size() > 0)
    { 
      // If the variable has a metafeature: _i`mf
      // evaluate _i`mf in cgs and set stereotype of result 

      String mf = (String) metafs.get(0); 
      int mfindex = mf.indexOf("`"); 
      varx = mf.substring(0,mfindex); 
      mffeat = mf.substring(mfindex+1,mf.length()); 
    } 

    int ind = vars.indexOf(varx);
 
    if (ind >= 0)
    { Object obj = eargs.get(ind); 
      if (obj instanceof ModelElement) 
      { ModelElement me = (ModelElement) obj; 
        if (positive) 
        { me.addStereotype(stereo); } 
        else 
        { me.removeStereotype(stereo); } 
      } 
      else if (obj instanceof ASTTerm) 
      { ASTTerm ast = (ASTTerm) obj;
        String lit = ast.literalForm(); 

        // if there is a metafeature of variable, apply it: 

        if (mffeat != null) 
        { if (cgs.hasRuleset(mffeat))
          { String repl = CGRule.applyMetafeature(
                             mffeat,ast,cgs,entities); 

            // Evaluate ast`mffeat and set its stereo 

            /* JOptionPane.showMessageDialog(null, 
              "repl: " + repl + " Ruleset mffeat: " + mffeat + 
              " stereo: " + stereo,   "",
              JOptionPane.INFORMATION_MESSAGE); */ 
    
            if (isWith) { } 
            else if (positive && repl != null) 
            { ASTTerm.setType(repl,stereo);
              ASTTerm.addStereo(repl,stereo); 
            } 
            else if (repl != null) 
            { ASTTerm.setType(repl,null);
              ASTTerm.removeStereo(repl,stereo);
            } 
          }
          else if (CSTL.isInbuiltFunction(mffeat))
          { // standard metafeature such as `first etc
            String repl = CGRule.applyMetafeature(
                             mffeat,ast,cgs,entities); 

            if (positive && repl != null) 
            { ASTTerm.setType(repl,stereo);
              ASTTerm.addStereo(repl,stereo); 
            } 
            else if (repl != null) 
            { ASTTerm.setType(repl,null);
              ASTTerm.removeStereo(repl,stereo);
            } 

          /*  JOptionPane.showMessageDialog(null,
              "Executed action " + repl + 
              " |-> " + stereo + " Tagged values = " + ASTTerm.metafeatures, 
              "", JOptionPane.INFORMATION_MESSAGE);  
            */
          } 
          else // No ruleset, set ast`mffeat=stereo
          { ASTTerm.setTaggedValue(ast, mffeat, stereo); 
            
          /*  JOptionPane.showMessageDialog(null,
              "Executed action " + ast + "`" + mffeat + 
              " = " + stereo + " Tagged values = " + ASTTerm.metafeatures, 
              "", JOptionPane.INFORMATION_MESSAGE); */   
            // repl = stereo; 
          }  
    
          /* System.out.println(">>> Executed action " + repl + " (" + positive + ") " + stereo);  
          JOptionPane.showMessageDialog(null, 
             "Executed action " + repl + " (" + positive + ") " + stereo,   "",
             JOptionPane.INFORMATION_MESSAGE); */ 
        } 
        else 
        { if (isWith) { } 
          else if (positive) 
          { // ast.addStereotype(stereo); 
            ASTTerm.addStereo(lit,stereo); 
          } 
          else 
          { // ast.removeStereotype(stereo); 
            ASTTerm.removeStereo(lit,stereo); 
          }
          /* JOptionPane.showMessageDialog(null, 
             "Executed action " + ast + " (" + positive + ") " + stereo + " " + ASTTerm.getStereotypes(ast),   "",
             JOptionPane.INFORMATION_MESSAGE); */  
        }  
      }  
    }
    else // varx is a global variable
    { /* JOptionPane.showMessageDialog(null, 
             "Set global variable " + varx + " " + mffeat + " " + stereo,   "",
             JOptionPane.INFORMATION_MESSAGE);  */ 

      if (mffeat == null || mffeat.length() == 0) 
      { ASTTerm.setStereotypeValue(varx,stereo); }
      else // varx`mffeat = stereo
      { String varValue = ASTTerm.getStereotypeValue(varx);
        if (varValue != null)  
        { ASTTerm.setTaggedValue(varValue,mffeat,stereo); }
      }  
    }   
  } 

  public static boolean conditionsSatisfied(
              Vector conditions,  
              Vector vars, Vector eargs, 
              Vector entities, CGSpec cgs, Vector gvars) 
  { boolean res = true; 
    for (int i = 0; i < eargs.size(); i++) 
    { Object m = eargs.get(i); 
      String var = "_" + (i+1); // assumes numbered _1, _2 ...
 
      for (int j = 0; j < conditions.size(); j++) 
      { CGCondition cond = (CGCondition) conditions.get(j); 
        if (cond.variable != null) 
        { int mfindex = cond.variable.indexOf("`"); 
          String cvar = cond.variable; 
          if (mfindex > 0) 
          { cvar = cvar.substring(0,mfindex); }

          if ("_*".equals(cvar) && 
              m instanceof Vector)
          { if (cond.conditionSatisfied((Vector) m,entities,cgs))
            { } 
            else 
            { return false; } 
            System.out.println("||| Condition " + cond + " is satisfied by " + m); 
          } 
          else if ("_+".equals(cvar) && 
              m instanceof Vector)
          { if (cond.conditionSatisfied((Vector) m,entities,cgs))
            { } 
            else 
            { return false; } 
            System.out.println("||| Condition " + cond + " is satisfied by " + m); 
          } 
          else if (var.equals(cvar))
          { if (m instanceof Type && 
              cond.conditionSatisfied((Type) m, entities,cgs)) 
            { }
            else if (m instanceof Expression && 
                   cond.conditionSatisfied((Expression) m, entities,cgs))
            { } 
            else if (m instanceof Statement && 
                   cond.conditionSatisfied((Statement) m, entities,cgs) )
            { } 
            else if (m instanceof Attribute && 
                    cond.conditionSatisfied((Attribute) m, entities,cgs) )
            { } 
            else if (m instanceof ModelElement && 
                   cond.stereotypeConditionSatisfied((ModelElement) m, entities,cgs))
            { } 
            else if (m instanceof Vector && 
                   cond.conditionSatisfied((Vector) m, entities,cgs)) 
            { }
            else if (m instanceof String && 
                   cond.conditionSatisfied((String) m, entities,cgs)) 
            { }
            else if (m instanceof ASTTerm && 
               cond.conditionSatisfiedASTTerm((ASTTerm) m, vars,
                  eargs, new Vector(), entities, cgs, gvars))
            { System.out.println("||| ASTTerm condition " + cond + " satisfied by term " + m); 
              System.out.println(); 
            } 
            else 
            { return false; } 
		  
            System.out.println("||| Condition " + cond + " is satisfied by " + m); 
          } 
        } 
      } 
    } 
    return res; 
  } 

  public static boolean allConditionsSatisfied(CGRule r, 
              Vector conditions,  
              Vector vars, Vector eargs, 
              Vector reps,
              Vector entities, CGSpec cgs, Vector gvars) 
  { boolean res = true; 
    
    // JOptionPane.showMessageDialog(null, 
    //      "|||| Checking conditions " + conditions + " with global variables " + gvars,   "",
    //     JOptionPane.INFORMATION_MESSAGE); 

    for (int j = 0; j < conditions.size(); j++) 
    { CGCondition cond = (CGCondition) conditions.get(j); 
      if (cond.variable != null) 
      { String cvar = cond.variable; 
        int mfindex = cvar.indexOf("`"); 
        if (mfindex > 0) 
        { cvar = cvar.substring(0,mfindex); }

        int ind = r.variables.indexOf(cvar); 
          // variablePosition for CGTL rules

        if (ind < 0) 
        { // Global variable or unknown thing.

          String evaluatedStereo = 
              cond.evaluateProperty(vars, eargs, 
                      reps, cgs, 
                      entities, gvars); 
          String gval = 
              (String) ASTTerm.metafeatures.get(cvar);
            
          if (gvars.contains(cvar))
          { // and evaluate its metafeature if any

            /* JOptionPane.showMessageDialog(null, 
              "|||| Checking condition " + gval + " = " + cond.stereotype + "~" + evaluatedStereo,   "",
              JOptionPane.INFORMATION_MESSAGE); */ 

            if (evaluatedStereo.equals(gval))
            { } 
            else if (gval != null & 
              ASTTerm.metafeatures.get(gval) instanceof Vector &&
              ((Vector) ASTTerm.metafeatures.get(gval)).contains(evaluatedStereo))
            { } 
            else 
            { return false; } 
          }
          else // not a variable on LHS
          { return false; } 

          /* JOptionPane.showMessageDialog(null, 
              "|||| Condition " + gval + " " + evaluatedStereo + " is true",   "",
              JOptionPane.INFORMATION_MESSAGE); */  
        } 
        else if (ind >= 0 && ind < eargs.size())
        { Object m = eargs.get(ind); 

          if ("_*".equals(cvar) && 
              m instanceof Vector)
          { if (cond.conditionSatisfied((Vector) m,entities,cgs, gvars))
            { } 
            else 
            { return false; } 
            System.out.println("||| Condition " + cond + " is satisfied by " + m); 
          } 
          else if ("_+".equals(cvar) && 
              m instanceof Vector)
          { if (cond.conditionSatisfied((Vector) m,entities,cgs, gvars))
            { } 
            else 
            { return false; } 
            System.out.println("||| Condition " + cond + " is satisfied by " + m); 
          } 
          else if (m instanceof Type && 
              cond.conditionSatisfied((Type) m, entities,cgs)) 
          { }
          else if (m instanceof Expression && 
                   cond.conditionSatisfied((Expression) m, entities,cgs))
          { } 
          else if (m instanceof Statement && 
                   cond.conditionSatisfied((Statement) m, entities,cgs) )
          { } 
          else if (m instanceof Attribute && 
                   cond.conditionSatisfied((Attribute) m, entities,cgs) )
          { } 
          else if (m instanceof ModelElement && 
                   cond.stereotypeConditionSatisfied((ModelElement) m, entities,cgs))
          { } 
          else if (m instanceof Vector && 
                   cond.conditionSatisfied((Vector) m, entities,cgs,gvars)) 
          { }
          else if (m instanceof String && 
                   cond.conditionSatisfied((String) m, entities,cgs)) 
          { }
          else if (m instanceof ASTTerm && 
                   cond.conditionSatisfiedASTTerm((ASTTerm) m,
                          vars, eargs, reps,  
                          entities, cgs, gvars))
          { System.out.println("||| ASTTerm condition " + cond + " satisfied by term " + m); 
            System.out.println(); 
          } 
          else 
          { return false; } 
		  
          System.out.println("||| Condition " + cond + " is satisfied by " + m); 
        } 
      } 
    } 

    return res; 
  } 

  public boolean stereotypeConditionSatisfied(ModelElement m, Vector entities, CGSpec cgs)
  { if (m.hasStereotype(stereotype))
    { return positive; }

    if (!m.hasStereotype(stereotype))
    { return !positive; }

    return false; 
  } 



  public boolean conditionSatisfied(Object t, Vector entities, CGSpec cgs, Vector gvars)
  { if (t instanceof Type) 
    { return conditionSatisfied((Type) t, entities,cgs); } 
    if (t instanceof Attribute) 
    { return conditionSatisfied((Attribute) t, entities,cgs); } 
    if (t instanceof Expression) 
    { return conditionSatisfied((Expression) t, entities,cgs); } 
    if (t instanceof Statement) 
    { return conditionSatisfied((Statement) t, entities,cgs); } 
    if (t instanceof ASTTerm) 
    { Vector vars = new Vector(); 
      Vector eargs = new Vector(); 
      Vector reps = new Vector(); 
      return conditionSatisfiedASTTerm((ASTTerm) t,
                          vars, eargs, reps,  
                          entities, cgs, gvars); 
    } 
    if (t instanceof Vector) 
    { return conditionSatisfied((Vector) t, entities,cgs); }
    if (t instanceof String) 
    { return conditionSatisfied((String) t, entities,cgs); } 
 
    return false; 
  } 


  public boolean conditionSatisfied(Object t, Vector entities, CGSpec cgs)
  { if (t instanceof Type) 
    { return conditionSatisfied((Type) t, entities,cgs); } 
    if (t instanceof Attribute) 
    { return conditionSatisfied((Attribute) t, entities,cgs); } 
    if (t instanceof Expression) 
    { return conditionSatisfied((Expression) t, entities,cgs); } 
    if (t instanceof Statement) 
    { return conditionSatisfied((Statement) t, entities,cgs); } 
    if (t instanceof ASTTerm) 
    { Vector gvars = new Vector(); 
      return conditionSatisfied((ASTTerm) t, entities,cgs, gvars); 
    } 
    if (t instanceof Vector) 
    { return conditionSatisfied((Vector) t, entities,cgs); }
    if (t instanceof String) 
    { return conditionSatisfied((String) t, entities,cgs); } 
 
    return false; 
  } 


  public boolean conditionSatisfied(Type t, Vector entities, CGSpec cgs)
  { System.out.println("||| Checking type condition " + t + " " + stereotype); 
    System.out.println(); 

    if ("string".equals(stereotype.toLowerCase()) && t.isStringType())
    { return positive; }

    if ("class".equals(stereotype.toLowerCase()) && t.isEntityType(entities))
    { System.out.println("||| Condition class satisfied for " + t); 
      return positive; 
    }

    if ("interface".equals(stereotype.toLowerCase()) && t.isInterfaceType(entities))
    { System.out.println("||| Condition interface satisfied for " + t); 
      return positive; 
    }

    if ("void".equals(stereotype.toLowerCase()) && (t == null || "void".equals(t.getName()) || t.isVoidType()))
    { return positive; }

    if ("enumerated".equals(stereotype.toLowerCase()) && t.isEnumeratedType())
    { return positive; }

    if ("datatype".equals(stereotype.toLowerCase()) && t.isDatatype())
    { return positive; }

    if ("map".equals(stereotype.toLowerCase()) && t.isMapType())
    { return positive; }

    if ("function".equals(stereotype.toLowerCase()) && t.isFunctionType())
    { return positive; }

    if ("collection".equals(stereotype.toLowerCase()) && t.isCollectionType())
    { return positive; }

    if ("sequence".equals(stereotype.toLowerCase()) && t.isSequenceType())
    { return positive; }

    if ("set".equals(stereotype.toLowerCase()) && t.isSetType())
    { return positive; }

    if ("ref".equals(stereotype.toLowerCase()) && t.isRef())
    { return positive; }
    

    if ("integer".equals(stereotype.toLowerCase()) && t.isInteger())
    { return positive; }

    if ("real".equals(stereotype.toLowerCase()) && t.isReal())
    { return positive; }

    if ("int".equals(stereotype.toLowerCase()) && t.isInt())
    { return positive; }

    if ("long".equals(stereotype.toLowerCase()) && t.isLong())
    { return positive; }

    if ("class".equals(stereotype.toLowerCase()) && !(t.isEntityType(entities)))
    { System.out.println("||| " + t + " is not a class"); 
      return !positive; 
    }

    if ("interface".equals(stereotype.toLowerCase()) && !(t.isInterfaceType(entities)))
    { System.out.println("||| " + t + " is not an interface"); 
      return !positive; 
    }


    if ("void".equals(stereotype.toLowerCase()) && t != null && !t.isVoidType())
    { return !positive; }

    if ("enumerated".equals(stereotype.toLowerCase()) && !(t.isEnumeratedType()))
    { return !positive; }

    if ("map".equals(stereotype.toLowerCase()) && !t.isMapType())
    { return !positive; }

    if ("function".equals(stereotype.toLowerCase()) && !t.isFunctionType())
    { return !positive; }

    if ("collection".equals(stereotype.toLowerCase()) && !(t.isCollectionType()))
    { return !positive; }

    if ("sequence".equals(stereotype.toLowerCase()) && !(t.isSequenceType()))
    { return !positive; }

    if ("set".equals(stereotype.toLowerCase()) && !(t.isSetType()))
    { return !positive; }

    if ("ref".equals(stereotype.toLowerCase()) && !(t.isRef()))
    { return !positive; }

    if ("integer".equals(stereotype.toLowerCase()) && !(t.isInteger()))
    { return !positive; }

    if ("real".equals(stereotype.toLowerCase()) && !(t.isReal()))
    { return !positive; }

    if ("int".equals(stereotype.toLowerCase()) && !(t.isInt()))
    { return !positive; }

    if ("long".equals(stereotype.toLowerCase()) && !(t.isLong()))
    { return !positive; }

    return false;
  }

  public boolean conditionSatisfied(Attribute a, Vector entities, CGSpec cgs)
  { if ("primary".equals(stereotype.toLowerCase()) && a.isPrimaryAttribute())
    { return positive; }
    if ("static".equals(stereotype.toLowerCase()) && a.isStatic())
    { return positive; }
    if (a.hasStereotype(stereotype))
    { return positive; }
    return false;
  }

  public boolean conditionSatisfied(Vector v, Vector entities, CGSpec cgs, Vector gvars)
  { System.out.println(".>>>. Checking vector condition " + quantifier + " " + stereotype); 
    
    if ("all".equals(quantifier))
    { if (v.size() == 0) 
      { return true; } 

      CGCondition gcond = new CGCondition(stereotype, "_1"); 
      gcond.setPositive(positive);  
      for (int i = 0; i < v.size(); i++) 
      { Object x = v.get(i); 
        if (gcond.conditionSatisfied(x,entities,cgs)) { } 
        else 
        { return false; } 
      } 
      return true; 
    }   

    if ("any".equals(quantifier))
    { if (v.size() == 0) 
      { return false; } 

      CGCondition gcond = new CGCondition(stereotype,"_1");
      gcond.setPositive(positive);  
      for (int i = 0; i < v.size(); i++) 
      { Object x = v.get(i); 
        if (gcond.conditionSatisfied(x,entities,cgs)) 
        { return true; } 
      } 
      return false; 
    }

    if ("empty".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() == 0))
    { return positive; }

    if ("empty".equals(stereotype.toLowerCase()) && 
        v != null && v.size() > 0)
    { return !positive; }

    if ("multiple".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() <= 1))
    { return !positive; }

    if ("multiple".equals(stereotype.toLowerCase()) && 
        v != null && v.size() > 1)
    { return positive; }

    if ("singleton".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 1))
    { return !positive; }

    if ("singleton".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 1)
    { return positive; }

    if ("1ary".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 1))
    { return !positive; }

    if ("1ary".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 1)
    { return positive; }

    if ("2ary".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 2))
    { return !positive; }

    if ("2ary".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 2)
    { return positive; }

    if ("3ary".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 3))
    { return !positive; }

    if ("3ary".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 3)
    { return positive; }

    if ("4ary".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 4))
    { return !positive; }

    if ("4ary".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 4)
    { return positive; }

    if ("5ary".equals(stereotype.toLowerCase()) && 
        (v == null || v.size() != 5))
    { return !positive; }

    if ("5ary".equals(stereotype.toLowerCase()) && 
        v != null && v.size() == 5)
    { return positive; }

    return false; 
  } 

  public boolean conditionSatisfied(Expression e, 
                         Vector entities, CGSpec cgs)
  { Type t = e.getType();
    Type et = e.getElementType(); 

    int kind = e.getUMLKind(); 

    String edata = e + ""; 

    if (isMatches)
    { // check that edata matches the stereo
      if (edata.matches(stereotype))
      { return true; } 
      return false; 
    } 

    Entity ent = 
      (Entity) ModelElement.lookupByName(edata,entities); 
	
	// if (ent != null)
	// { System.out.println(">> Expression " + e + " is a class"); }
	
	// System.out.println(">> Testing condition " + stereotype + " on expression " + e + " type = " + t); 

    String tname = ""; 

    if (t == null) 
    { System.err.println("!! WARNING: null type in: " + e); 
      // return false; 
    } 
    else 
    { tname = t.getName(); } 

    String etname = ""; 
    if (et != null) 
    { etname = et.getName(); } 

    if ("Set".equals(stereotype))
    { if (positive)
      { return "Set".equals(tname); }
      else
      { return !("Set".equals(tname)); }
    }
    else if ("Sequence".equals(stereotype))
    { if (positive)
      { return "Sequence".equals(tname); }
      else
      { return !("Sequence".equals(tname)); }
    }
    else if ("Map".equals(stereotype))
    { if (positive)
      { return "Map".equals(tname); }
      else
      { return !("Map".equals(tname)); }
    }
    else if ("Function".equals(stereotype))
    { if (positive)
      { return "Function".equals(tname); }
      else
      { return !("Function".equals(tname)); }
    }
    else if ("Ref".equals(stereotype))
    { if (positive)
      { return "Ref".equals(tname); }
      else
      { return !("Ref".equals(tname)); }
    }
    else if ("collection".equals(stereotype.toLowerCase()))
    { if (positive)
      { return ("Set".equals(tname) || 
                "Sequence".equals(tname)); }
      else
      { return !("Set".equals(tname)) 
               && !("Sequence".equals(tname)); }
    }
    else if ("String".equals(stereotype))
    { if (positive)
      { return "String".equals(tname); }
      else
      { return !("String".equals(tname)); }
    }
    else if ("numeric".equals(stereotype))
    { if (positive)
      { return t != null && t.isNumericType(); }
      else
      { return t == null || !(t.isNumericType()); }
    }
    else if ("integer".equals(stereotype))
    { if (positive)
      { return t != null && t.isIntegerType(); }
      else
      { return t == null || !(t.isIntegerType()); }
    }
    else if ("object".equals(stereotype))
    { if (positive)
      { if ("self".equals(edata) || "super".equals(edata))
        { return true; } 
        else if (t != null && 
                 t.isEntityType(entities) && ent == null)
        { return true; }
        else
        { return false; }
      }  
      else 
      { if ("self".equals(edata) || "super".equals(edata))
        { return false; } 
        else if (t != null && 
                 t.isEntityType(entities) && ent == null)
        { return false; }
        else
        { return true; }
      }  
    }
    else if ("Class".equals(stereotype))
    { if (positive)
      { return ent != null; }
      else
      { return ent == null; }
    }
    else if ("mapcollection".equals(stereotype.toLowerCase()))
    { if (positive)
      { return ("Set".equals(tname) || 
                "Sequence".equals(tname)) && 
               "Map".equals(etname); 
      }
      else
      { return !("Map".equals(etname)) || 
            (!("Set".equals(tname)) && 
             !("Sequence".equals(tname))); 
      }
    }
    else if ("enumerated".equals(stereotype))
    { if (positive)
      { return t != null && t.isEnumeratedType(); }
      else
      { return t == null || !(t.isEnumeratedType()); }
    }
    else if ("enumerationLiteral".equals(stereotype))
    { if (positive)
      { return t != null && 
               t.isEnumeratedType() && t.hasValue(e); 
      }
      else
      { return t == null || 
               !(t.isEnumeratedType() && t.hasValue(e));
      }
    }
    else if ("classId".equals(stereotype))
    { if (positive)
      { return kind == Expression.CLASSID; }
      else
      { return kind != Expression.CLASSID; }
    }
    else if ("value".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.VALUE; }
      else
      { return e.umlkind != Expression.VALUE; }
    }
    else if ("variable".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.VARIABLE; }
      else
      { return e.umlkind != Expression.VARIABLE; }
    }
    else if ("attribute".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.ATTRIBUTE; }
      else
      { return e.umlkind != Expression.ATTRIBUTE; }
    }
    else if ("role".equals(stereotype) || "reference".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.ROLE; }
      else
      { return e.umlkind != Expression.ROLE; }
    }
    else if ("intRef".equals(stereotype))
    { if (positive) 
      { return tname.equals("Ref") && etname.equals("int"); }
      else 
      { return !(tname.equals("Ref") && etname.equals("int")); } 
    } 
    else if ("longRef".equals(stereotype))
    { if (positive) 
      { return tname.equals("Ref") && etname.equals("long"); }
      else 
      { return !(tname.equals("Ref") && etname.equals("long")); } 
    } 
    else if ("doubleRef".equals(stereotype))
    { if (positive) 
      { return tname.equals("Ref") && etname.equals("double"); }
      else 
      { return !(tname.equals("Ref") && etname.equals("double")); } 
    } 
    else if ("booleanRef".equals(stereotype))
    { if (positive) 
      { return tname.equals("Ref") && etname.equals("boolean"); }
      else 
      { return !(tname.equals("Ref") && etname.equals("boolean")); } 
    } 
    else if ("StringRef".equals(stereotype))
    { if (positive) 
      { return tname.equals("Ref") && etname.equals("String"); }
      else 
      { return !(tname.equals("Ref") && etname.equals("String")); } 
    } 
    else if ("static".equals(stereotype))
    { if (positive) 
      { return e.isStatic(); }
      else 
      { return !(e.isStatic()); } 
    } 
    else if (edata.equals(stereotype))
    { return positive; } 
    else // _i is T   T is int, double, long, etc, or a enumeration or class name 
    { if (positive) 
      { return stereotype.equals(tname); }
      else 
      { return !(stereotype.equals(tname)); }
    }
    // return false;
  }

  public boolean conditionSatisfied(Statement e, Vector entities, CGSpec cgs)
  { if (e instanceof AssignStatement) 
    { AssignStatement st = (AssignStatement) e; 
      Expression left = st.getLeft(); 
      return conditionSatisfied(left,entities,cgs); 
    }
 
    return false; 
  } // and for other kinds of statement also 

  public boolean conditionSatisfiedASTTerm(ASTTerm a, 
                  Vector vars, Vector eargs, 
                  Vector reps, Vector entities, 
                  CGSpec cgs, Vector gvars)
  { // Condition variable keyword stereotype is true
    // for a, when reps are substituted for vars in possibly
    // both variable and stereotype. 

    // _i`f P 
    // valid if f is not a ruleset/built-in function
    // and _i has tagged value  f=P
    // Or if _i`f evaluates equal to P, 
    // or if it has stereotype P 

    String alit = a.literalForm(); 

    String stereo = "" + stereotype; 

    // JOptionPane.showMessageDialog(null, 
    //      "|||| Checking condition " + alit + " " + stereo,   "",
    //     JOptionPane.INFORMATION_MESSAGE); 

    /* In stereo and variable: 
         Replace vars[i] by eargs[i].cg(cgs) or reps[i] */ 
    /* Replace any gvars[j] by its value */
	
    if (quantifier.equals("all") || quantifier.equals("any"))
    { // CGCondition gcond = new CGCondition(stereotype, "_1"); 
      // gcond.setPositive(positive);
	  Vector subterms = a.getTerms();
	  boolean hasany = false; 
	  boolean hasall = true;  
	  for (int i = 0; i < subterms.size(); i++) 
	  { ASTTerm sb = (ASTTerm) subterms.get(i); 
	    if (stereotype.equals(sb.getTag())) 
		{ hasany = true; } 
		else 
		{ hasall = false; }
	  } 
	  
	  if (quantifier.equals("all") && positive)
	  { return hasall; } 
	  if (quantifier.equals("all") && !positive)
	  { return !hasall; } 
	  if (quantifier.equals("any") && positive)
	  { return hasany; } 
	  if (quantifier.equals("any") && !positive)
	  { return !hasany; } 
    }   

    Vector stereomfs = CGRule.metafeatures(stereo); 
    if (stereomfs.size() > 0)
    { 
      // If stereo has a metafeature: _i`mf
      // evaluate _i`mf in cgs and set stereo to result 
      
      String smf = (String) stereomfs.get(0); 
      int smfindex = smf.indexOf("`"); 
      String svarx = smf.substring(0,smfindex); 
      String smffeat = smf.substring(smfindex+1,smf.length()); 
      if (smffeat != null) 
      { int indsv = vars.indexOf(svarx);
        if (indsv >= 0 && 
            eargs.get(indsv) instanceof ASTTerm)
        {  
          ASTTerm searg = (ASTTerm) eargs.get(indsv); 
          stereo = CGRule.applyMetafeature(
                             smffeat,searg,cgs,entities); 
        }

        /* JOptionPane.showMessageDialog(null, 
         "|>|| Found metafeature " + smf + " for " + stereotype + " resulting value = " + stereo,   "",
         JOptionPane.INFORMATION_MESSAGE); */ 

      } 
    }

 
    for (int x = 0; x < reps.size() && x < vars.size(); x++)
    { String var = (String) vars.get(x);
      String arg1 = (String) reps.get(x); 

      // JOptionPane.showMessageDialog(null, 
      //   "||| Argument " + arg1 + " replacing " + var,   "",
      //   JOptionPane.INFORMATION_MESSAGE); 

     /* String svarx = var; 
      String smffeat = null; 
      Vector stereomfs = CGRule.metafeatures(stereo); 
      if (stereomfs.size() > 0)
      { 
        // If stereo has a metafeature: _i`mf
        // evaluate _i`mf in cgs and set stereo to result 

        String smf = (String) stereomfs.get(0); 
        int smfindex = smf.indexOf("`"); 
        svarx = smf.substring(0,smfindex); 
        smffeat = smf.substring(smfindex+1,smf.length()); 
        if (smffeat != null && var.equals(svarx)) 
        { int indv = vars.indexOf(svarx);
 
          if (indv >= 0 && eargs.get(indv) instanceof ASTTerm)
          {  
            ASTTerm earg = (ASTTerm) eargs.get(indv); 
            stereo = CGRule.applyMetafeature(
                             smffeat,earg,cgs,entities); 
          }
        } 
      } */  

      stereo = stereo.replace(var,arg1);
    }
 
    if (gvars.size() > 0) // variable is a global
    { for (int y = 0; y < gvars.size(); y++) 
      { String rvar = (String) gvars.get(y);
        String varValue = ASTTerm.getStereotypeValue(rvar); 
        if (varValue != null) 
        { stereo = stereo.replace(rvar,varValue); 

        /* JOptionPane.showMessageDialog(null, 
           "Global variable " + rvar + " value is " + stereo,   "",
          JOptionPane.INFORMATION_MESSAGE); 

        JOptionPane.showMessageDialog(null,
          ">--> Replacing global variable " + rvar + " by " + varValue + " in " + alit + " = " + stereo, "", 
          JOptionPane.INFORMATION_MESSAGE);  */ 
        }
      }

      /* JOptionPane.showMessageDialog(null, 
          "||| Condition " + alit + " = " + stereo,   "",
          JOptionPane.INFORMATION_MESSAGE); */ 

      if (alit.equals(stereo)) 
      { return positive; }  // or it has the stereo
    }

    Vector metafs = CGRule.metafeatures(variable); 

    System.out.println("*** Metafeatures of " + variable + " are: " + metafs); 
    /* JOptionPane.showMessageDialog(null, 
                 " metafeatures of = " + variable + 
                 " are: " + metafs, 
                 "", 
                 JOptionPane.INFORMATION_MESSAGE);
    */     

    if (metafs.size() > 0)
    { 
      // If the variable has a metafeature: _i`mf
      // evaluate _i`mf in cgs and test result against
      // stereotype

      String mf = (String) metafs.get(0); 
      int mfindex = mf.indexOf("`"); 
      String mfvar = mf.substring(0,mfindex); 
      String mffeat = mf.substring(mfindex+1,mf.length());
      String repl = CGRule.applyMetafeature(
                             mffeat,a,cgs,entities); 


      // System.out.println(">|>|> Testing " + repl + " with " + stereo); 
      /* JOptionPane.showMessageDialog(null, 
                 " Evaluated " + a + 
                 "`" + mffeat + " = " + repl, 
                 "", 
                 JOptionPane.INFORMATION_MESSAGE);  */ 

      if (isMatches)
      { // check that edata matches the stereo
        try { 
          if (repl.matches(stereo))
          { return true; } 
        }
        catch (Exception _ex) 
        { System.err.println("!! Invalid regular expression: " + stereo); 
          return false; 
        } 
        return false; 
      } 

      if (isNested)
      { // check that term is nested on stereo
        Vector tgs = new Vector(); 
        tgs.add(stereo); 
        if (a.hasNestedTags(tgs))
        { return true; } 
        return false; 
      } 

      Vector stereosOfVar = 
        (Vector) ASTTerm.metafeatures.get(repl); 

      if (repl != null && 
          repl.equals(stereo))
      { return positive; } 
      else if (stereosOfVar != null && 
          stereosOfVar.contains(stereo))
      { return positive; }  
      else 
      { return !positive; } 

    } 

    // System.out.println(">|>|> Testing " + alit + " with " + stereo); 

    if (isMatches)
    { // check that variable matches the stereo
      try { 
        if (alit.matches(stereo))
        { return true; } 
      }
      catch (Exception _ex) 
      { System.err.println("!! Invalid regular expression: " + stereo); 
        return false; 
      } 

      return false; 
    } 
      
    if (isNested)
    { // check that term is nested on stereo
      Vector tgs = new Vector(); 
      tgs.add(stereo); 
      if (a.hasNestedTags(tgs))
      { return true; } 
      return false; 
    } 

    // if ("integer".equalsIgnoreCase(stereotype))
    // { if (a.isInteger()) 
    //   { return positive; } 
    // } 


    if (a instanceof ASTCompositeTerm)
    { ASTCompositeTerm ac = (ASTCompositeTerm) a; 
      
      if (ac.tag.equalsIgnoreCase(stereotype))
      { return positive; }
      else if (cgs.hasRuleset(stereotype))
      { return !positive; } 

      if ("multiple".equals(stereotype) || 
          "singleton".equals(stereotype) ||
          "1ary".equals(stereotype) ||
          "2ary".equals(stereotype) ||
          "3ary".equals(stereotype) ||
          "4ary".equals(stereotype) ||
          "5ary".equals(stereotype))
      { return conditionSatisfied(ac.getTerms(), entities, cgs, gvars); }
    } 
    else if (a instanceof ASTBasicTerm)
    { ASTBasicTerm ac = (ASTBasicTerm) a; 
      
      if (ac.tag.equalsIgnoreCase(stereotype))
      { return positive; }
      else if (cgs.hasRuleset(stereotype))
      { return !positive; } 

      if ("multiple".equals(stereotype) ||
          "2ary".equals(stereotype) ||
          "3ary".equals(stereotype) ||
          "4ary".equals(stereotype) ||
          "5ary".equals(stereotype)) 
      { return !positive; } 

      if ("singleton".equals(stereotype) ||
          "1ary".equals(stereotype)) 
      { return positive; } 
    }
    else if (a instanceof ASTSymbolTerm)
    { ASTSymbolTerm ac = (ASTSymbolTerm) a; 
      
      if (ac.symbol.equalsIgnoreCase(stereotype))
      { return positive; }

      if ("multiple".equals(stereotype) || 
          "singleton".equals(stereotype) ||
          "1ary".equals(stereotype) ||
          "2ary".equals(stereotype) ||
          "3ary".equals(stereotype) ||
          "4ary".equals(stereotype) ||
          "5ary".equals(stereotype))
      { return !positive; } 
    } 

    if (a.hasType(stereotype))
    { System.out.println("|||>> condition that type of " + a + " = " + stereotype + " is satisfied"); 
      return positive; 
    } 

    if ("Class".equals(stereotype) || 
        "Entity".equals(stereotype))
    { if (Type.isOclEntityType(alit,entities) || 
          a.hasStereotype("Class") || 
          a.hasStereotype("Entity")) 
      { return positive; } 
      else 
      { return !positive; } 
    }  

    if ("Collection".equals(stereotype))
    { if (a.hasStereotype("Set") || 
          a.hasStereotype("Sequence") || 
          a.hasType("Set") || a.hasType("Sequence")) 
      { return positive; } 
      else 
      { return !positive; } 
    }  

    if ("updatesObject".equals(stereotype))
    { if (a.updatesObject(null))
      { return positive; } 
      else 
      { return !positive; } 
    } 

    if ("hasSideEffect".equals(stereotype))
    { if (a.hasSideEffect())
      { return positive; } 
      else 
      { return !positive; } 
    } 

    /* JOptionPane.showMessageDialog(null, 
          "" + a + " stereotypes " + ASTTerm.getStereotypes(a),   "",
          JOptionPane.INFORMATION_MESSAGE); */ 



    if (a.hasStereotype(stereotype))
    { return positive; }

    if (!a.hasStereotype(stereotype))
    { return !positive; }

    return false;
  }

  public boolean conditionSatisfied(String e, Vector entities, CGSpec cgs)
  { if (e == null) 
    { return !positive; }

    // if ("character".equals(stereotype))
    // { if (e.length() > 1 && e.startsWith("'") && 
    //       e.endsWith("'"))
    //   { return !positive; } 
    // } 

    if (e != null && e.equals(stereotype)) 
    { return positive; } 
    if (!e.equals(stereotype))
    { return !positive; }
    return false;    
  } 


}
    
