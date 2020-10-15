import java.util.Vector; 
import java.io.*; 
import javax.swing.JOptionPane;

/******************************
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Activity */ 

abstract class Statement 
implements Cloneable
{ private int indent = 0; 
  protected Entity entity = null;  // owner of the statement/its method

  protected boolean brackets = false; 

  public static final int WHILE = 0; 
  public static final int FOR = 1; 

  public void setEntity(Entity e)
  { entity = e; } 

  public void setBrackets(boolean b)
  { brackets = b; } 

  abstract protected Object clone(); 

  abstract void display(); 

  abstract String getOperator(); 

  public static boolean isEmpty(Statement st)
  { if (st == null) { return true; } 
    if (st instanceof SequenceStatement) 
    { SequenceStatement sq = (SequenceStatement) st; 
      if (sq.size() == 0) 
      { return true; } 
    } 
    return false; 
  } 

  public String cg(CGSpec cgs)
  { return this + ""; }

  public abstract Vector metavariables(); 

  abstract String bupdateForm(); 

  public abstract BStatement bupdateForm(java.util.Map env, boolean local);

  abstract void display(PrintWriter out); 

  public void displayImp(String var) 
  { display(); }    /* Default */ 

  public void displayImp(String var, PrintWriter out) 
  { display(out); } 

  abstract void displayJava(String t); 

  abstract void displayJava(String t, PrintWriter out); 
 
  abstract Statement substituteEq(String oldE, Expression newE); 

  abstract Expression wpc(Expression post); 

  abstract Vector dataDependents(Vector allvars, Vector vars); 

  abstract String toStringJava(); 

  abstract String saveModelData(PrintWriter out); 

  abstract boolean typeCheck(Vector types, Vector entities, 
                             Vector contexts, Vector env); 

  boolean typeCheck(Vector types, Vector entities, Vector env)
  { Vector contexts = new Vector(); 
    if (entity != null) 
    { contexts.add(entity); } 
    return typeCheck(types,entities,contexts,env); 
  }  
  
  public static Statement buildIf(Vector conds, Vector stats)
  { IfStatement res = new IfStatement();
    for (int i = 0; i < conds.size(); i++)
    { Expression cond = (Expression) conds.get(i);
      Statement stat = (Statement) stats.get(i);
      IfCase ic = new IfCase(cond,stat);
      res.addCase(ic);
    }
    return res;
  }

  abstract boolean updates(Vector v); 

  Expression toExpression() { return new BasicExpression("skip"); } 

  public Statement generateDesign(java.util.Map env, boolean local)
  { return this; }  

  public Statement statLC(java.util.Map env, boolean local)
  { return this; }  

  public abstract String updateForm(java.util.Map env, boolean local, Vector types, 
                                    Vector entities, Vector vars);

  public abstract String updateFormJava6(java.util.Map env, boolean local);

  public abstract String updateFormJava7(java.util.Map env, boolean local);

  public abstract String updateFormCSharp(java.util.Map env, boolean local);

  public abstract String updateFormCPP(java.util.Map env, boolean local);

  public Vector allPreTerms()
  { return new Vector(); } 

  public Vector allPreTerms(String var)
  { return new Vector(); } 

  public boolean isSkip() 
  { return false; }

  public abstract Statement dereference(BasicExpression var); 

  public String processPreTerms(Statement post, Vector preterms, java.util.Map env, boolean local,
                                Vector types, Vector entities, Vector vars)
  { if (preterms.size() > 0) 
    { Statement newpost = (Statement) post.clone(); 
      System.out.println(">>> PRE terms in statement " + post + " : " + preterms); 
      Vector processed = new Vector(); 
      Vector localatts = new Vector(); 

      String newdecs = ""; 
      for (int i = 0; i < preterms.size(); i++)
      { BasicExpression preterm = (BasicExpression) preterms.get(i);
        if (processed.contains(preterm)) { continue; }  
        // also skip if the preterm is not valid. 
        Type typ = preterm.getType();  // but actual type may be list if multiple
        Type actualtyp; 
        String newdec = ""; 
        String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
        String pretermqf = preterm.classqueryForm(env,true); 
          
        BasicExpression prebe = new BasicExpression(pre_var); 

        if (preterm.isMultiple())
        { if (preterm.isOrdered())
          { actualtyp = new Type("Sequence", null); } 
          else 
          { actualtyp = new Type("Set",null); }  
          actualtyp.setElementType(preterm.getElementType()); 
 
          if (preterm.umlkind == Expression.CLASSID && preterm.arrayIndex == null) 
          { pretermqf = "Controller.inst()." + pretermqf.toLowerCase() + "s"; } 
          newdec = actualtyp.getJava() + " " + pre_var + " = new Vector();\n" + 
                 "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
        } 
        else 
        { actualtyp = typ;
          newdec = "  " + actualtyp.getJava() + " " + pre_var + " = " + pretermqf + ";\n";
        } 
        newdecs = newdecs + "    " + newdec; 
        prebe.type = actualtyp; 
        prebe.elementType = preterm.elementType; 
        prebe.entity = preterm.getEntity(); 
        System.out.println(">> PRE variable " + prebe + " type= " + actualtyp + 
                              " elemtype= " + prebe.elementType); 
          
        Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
        preatt.setElementType(preterm.elementType); 
        preatt.setEntity(preterm.getEntity()); 
        localatts.add(preatt); 
        System.out.println(">>> New preterm variable: " + preatt + " : " + actualtyp + " " + 
                           preterm.getEntity()); 
        newpost = newpost.substituteEq("" + preterm,prebe); 
        processed.add(preterm); 
      }  // substitute(preterm,prebe) more appropriate 

      Vector context = new Vector(); 
      if (entity != null) 
      { context.add(entity); } 
      newpost.typeCheck(types,entities,context,localatts);  // and the vars
      return newdecs + "\n  " + newpost.updateForm(env,local,types,entities,localatts);
    } 
    return post.updateForm(env,local,types,entities,vars);  
  }  

  public String processPreTermsJava6(Statement post, Vector preterms, java.util.Map env, boolean local)
  { if (preterms.size() > 0) 
    { Statement newpost = (Statement) post.clone(); 
      System.out.println("PRE terms: " + preterms); 
      Vector processed = new Vector(); 
      Vector localatts = new Vector(); 

      String newdecs = ""; 
      for (int i = 0; i < preterms.size(); i++)
      { BasicExpression preterm = (BasicExpression) preterms.get(i);
        if (processed.contains(preterm)) { continue; }  
        Type typ = preterm.getType();  // but actual type may be list if multiple
        Type actualtyp; 
        String newdec = ""; 
        String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
        String pretermqf = preterm.classqueryFormJava6(env,true); 
          
        BasicExpression prebe = new BasicExpression(pre_var); 

        if (preterm.isMultiple())
        { if (preterm.isOrdered())
          { actualtyp = new Type("Sequence", null); } 
          else 
          { actualtyp = new Type("Set",null); }  
          actualtyp.setElementType(preterm.getElementType()); 
          newdec = actualtyp.getJava6() + " " + pre_var + " = " + 
                   actualtyp.initialValueJava6() + ";\n" + 
                   "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
        } 
        else 
        { actualtyp = typ;
          newdec = actualtyp.getJava6() + " " + pre_var + " = " + pretermqf + ";\n";
        } 
        newdecs = newdecs + "    " + newdec; 
        prebe.type = actualtyp; 
        prebe.elementType = preterm.elementType; 
        // System.out.println("PRE variable " + prebe + " type= " + actualtyp + 
        //                      " elemtype= " + prebe.elementType); 
          
        Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
        preatt.setElementType(preterm.elementType); 
        localatts.add(preatt); 
        newpost = newpost.substituteEq("" + preterm,prebe); 
        processed.add(preterm); 
      }  // substitute(preterm,prebe) more appropriate 

      // newpost.typeCheck(types,entities,context,localatts);
      return newdecs + "\n  " + newpost.updateFormJava6(env,local);
    } 
    return post.updateFormJava6(env,local);  
  }  

  public String processPreTermsJava7(Statement post, Vector preterms, java.util.Map env, boolean local)
  { if (preterms.size() > 0) 
    { Statement newpost = (Statement) post.clone(); 
      // System.out.println("PRE terms: " + preterms); 
      Vector processed = new Vector(); 
      Vector localatts = new Vector(); 

      String newdecs = ""; 
      for (int i = 0; i < preterms.size(); i++)
      { BasicExpression preterm = (BasicExpression) preterms.get(i);
        if (processed.contains(preterm)) { continue; }  
        Type typ = preterm.getType();  // but actual type may be list if multiple
        Type actualtyp; 
        String newdec = ""; 
        String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
        String pretermqf = preterm.classqueryFormJava7(env,true); 
          
        BasicExpression prebe = new BasicExpression(pre_var); 

        if (preterm.isMultiple())
        { if (preterm.isOrdered())
          { actualtyp = new Type("Sequence",null); } 
          else 
          { actualtyp = new Type("Set",null); } 
          actualtyp.setElementType(preterm.getElementType()); 
          newdec = actualtyp.getJava7(preterm.getElementType()) + " " + pre_var + " = " + 
                   actualtyp.initialValueJava7() + ";\n" + 
                   "    " + pre_var + ".addAll(" + pretermqf + ");\n"; 
        } 
        else 
        { actualtyp = typ;
          newdec = actualtyp.getJava7(preterm.getElementType()) + " " + pre_var + " = " + pretermqf + ";\n";
        } 
        newdecs = newdecs + "    " + newdec; 
        prebe.type = actualtyp; 
        prebe.elementType = preterm.elementType; 
        // System.out.println("PRE variable " + prebe + " type= " + actualtyp + 
        //                      " elemtype= " + prebe.elementType); 
          
        Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
        preatt.setElementType(preterm.elementType); 
        localatts.add(preatt); 
        newpost = newpost.substituteEq("" + preterm,prebe); 
        processed.add(preterm); 
      }  // substitute(preterm,prebe) more appropriate 

      // newpost.typeCheck(types,entities,context,localatts);
      return newdecs + "\n  " + newpost.updateFormJava7(env,local);
    } 
    return post.updateFormJava7(env,local);  
  }  

  public String processPreTermsCSharp(Statement post, Vector preterms, java.util.Map env, boolean local)
  { if (preterms.size() > 0) 
    { Statement newpost = (Statement) post.clone(); 
      // System.out.println("PRE terms: " + preterms); 
      Vector processed = new Vector(); 
      Vector localatts = new Vector(); 

      String newdecs = ""; 
      for (int i = 0; i < preterms.size(); i++)
      { BasicExpression preterm = (BasicExpression) preterms.get(i);
        if (processed.contains(preterm)) { continue; }  
        // also skip if the preterm is not valid. 
        Type typ = preterm.getType();  // but actual type may be list if multiple
        Type actualtyp; 
        String newdec = ""; 
        String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
        String pretermqf = preterm.classqueryFormCSharp(env,true); 
          
        BasicExpression prebe = new BasicExpression(pre_var); 

        if (preterm.isMultiple())
        { if (preterm.isOrdered())
          { actualtyp = new Type("Sequence", null); } 
          else 
          { actualtyp = new Type("Set",null); }  
          actualtyp.setElementType(preterm.getElementType()); 
          newdec = actualtyp.getCSharp() + " " + pre_var + " = new ArrayList();\n" + 
                   "    " + pre_var + ".AddRange(" + pretermqf + ");\n"; 
        } 
        else 
        { actualtyp = typ;
          newdec = actualtyp.getCSharp() + " " + pre_var + " = " + pretermqf + ";\n";
        } 
        newdecs = newdecs + "    " + newdec; 
        prebe.type = actualtyp; 
        prebe.elementType = preterm.elementType; 
        // System.out.println("PRE variable " + prebe + " type= " + actualtyp + 
        //                      " elemtype= " + prebe.elementType); 
          
        Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
        preatt.setElementType(preterm.elementType); 
        localatts.add(preatt); 
        newpost = newpost.substituteEq("" + preterm,prebe); 
        processed.add(preterm); 
      }  // substitute(preterm,prebe) more appropriate 

      // newpost.typeCheck(types,entities,context,localatts);
      return newdecs + "\n  " + newpost.updateFormCSharp(env,local);
    } 
    return post.updateFormCSharp(env,local);  
  }  

  public String processPreTermsCPP(Statement post, Vector preterms, java.util.Map env, boolean local)
  { if (preterms.size() > 0) 
    { Statement newpost = (Statement) post.clone(); 
      // System.out.println("PRE terms: " + preterms); 
      Vector processed = new Vector(); 
      Vector localatts = new Vector(); 

      String newdecs = ""; 
      for (int i = 0; i < preterms.size(); i++)
      { BasicExpression preterm = (BasicExpression) preterms.get(i);
        if (processed.contains(preterm)) { continue; }  
        // also skip if the preterm is not valid. 
        Type typ = preterm.getType();  // but actual type may be list if multiple
        Type actualtyp; 
        String newdec = ""; 
        String pre_var = Identifier.nextIdentifier("pre_" + preterm.data);
        String pretermqf = preterm.classqueryFormCPP(env,true); 
          
        BasicExpression prebe = new BasicExpression(pre_var); 

        if (preterm.isMultiple())
        { if (preterm.isOrdered())
          { actualtyp = new Type("Sequence", null); } 
          else 
          { actualtyp = new Type("Set",null); }  
          actualtyp.setElementType(preterm.getElementType()); 
          String cpptype = actualtyp.getCPP(preterm.getElementType()); 
          newdec = cpptype + " " + pre_var + " = new " + cpptype + "();\n"; 
          if (preterm.isOrdered())
          { newdec = newdec +  
              "    " + pre_var + "->insert(" + pre_var + "->end(), " + pretermqf + "->begin(), " + pretermqf + "->end());\n"; 
          } 
          else 
          { newdec = newdec +  
              "    " + pre_var + "->insert(" + pretermqf + "->begin(), " + pretermqf + "->end());\n"; 
          } 
        } 
        else 
        { actualtyp = typ;
          newdec = actualtyp.getCPP(preterm.getElementType()) + " " + pre_var + " = " + pretermqf + ";\n";
        } 
        newdecs = newdecs + "    " + newdec; 
        prebe.type = actualtyp; 
        prebe.elementType = preterm.elementType; 
        // System.out.println("PRE variable " + prebe + " type= " + actualtyp + 
        //                      " elemtype= " + prebe.elementType); 
          
        Attribute preatt = 
            new Attribute(pre_var,actualtyp,ModelElement.INTERNAL); 
        preatt.setElementType(preterm.elementType); 
        localatts.add(preatt); 
        newpost = newpost.substituteEq("" + preterm,prebe); 
        processed.add(preterm); 
      }  // substitute(preterm,prebe) more appropriate 

      // newpost.typeCheck(types,entities,context,localatts);
      return newdecs + "\n  " + newpost.updateFormCPP(env,local);
    } 
    return post.updateFormCPP(env,local);  
  }  

  public abstract Vector readFrame(); 

  public abstract Vector writeFrame(); 

  public abstract Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp);  

  public abstract Statement replaceModuleReferences(UseCase uc);  

  public static Statement combineIfStatements(Statement s1, Statement s2) 
  { if (s1 instanceof IfStatement)
    { IfStatement ifstat = (IfStatement) s1; 
      Statement stat2 = ifstat.getElsePart(); 
      if (stat2 == null) 
      { ifstat.setElse(s2); }
      else 
      { SequenceStatement ep = new SequenceStatement(); 
        ep.addStatement(stat2); 
        ep.addStatement(s2); 
        ifstat.setElse(ep); 
      } 
      return ifstat; 
    } 
    else
    { SequenceStatement res = new SequenceStatement(); 
      res.addStatement(s1); 
      res.addStatement(s2); 
      return res; 
    } 
  }          

  abstract public int syntacticComplexity(); 

  abstract public int cyclomaticComplexity(); 

  abstract public int epl(); 

  abstract public Vector allOperationsUsedIn(); 

  abstract public Vector equivalentsUsedIn(); 

  abstract public String toEtl(); 
}


class ReturnStatement extends Statement
{ Expression value = null; 
  
  public ReturnStatement()
  { value = null; } 

  public ReturnStatement(Expression e)
  { value = e; } 

  public String getOperator() 
  { return "return"; } 

  public Object clone()
  { return new ReturnStatement(value); } 

  public boolean hasValue()
  { return value != null; } 

  public void display()
  { System.out.print("  return"); 
    if (value != null)
    { System.out.print(" " + value); } 
    System.out.println(";"); 
  }  

  public void display(PrintWriter out)
  { out.print("  return"); 
    if (value != null)
    { out.print(" " + value); } 
    out.println(";"); 
  }  

  public void displayJava(String t)
  { display(); }  

  public void displayJava(String t, PrintWriter out)
  { display(out); }  
 
  public Statement substituteEq(String oldE, Expression newE)
  { if (value != null)
    { Expression newval = value.substituteEq(oldE,newE); 
      ReturnStatement res = new ReturnStatement(newval);
      res.setEntity(entity); 
      return res;  
    } 
    return this; 
  } 

  public String toString()
  { if (value == null)
    { return "return "; } 
    return "return " + value;
  } 

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("returnstatement_"); 
    out.println(res + " : ReturnStatement"); 
    out.println(res + ".statId = \"" + res + "\""); 
    if (value != null) 
    { String valueid = value.saveModelData(out); 
      out.println(valueid + " : " + res + ".returnValue"); 
    } 
    return res; 
  } 

  public String bupdateForm()
  { return " "; } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { return new BBasicStatement("skip"); } 

  public String toStringJava()
  { String res = "  return"; 
    if (value != null)
    { java.util.Map env = new java.util.HashMap(); 
      if (entity != null) 
      { env.put(entity.getName(),"this"); 
        res = res + " " + value.queryForm(env,true);
      }
      else 
      { res = res + " " + value.toJava(); } 
    } 
    res = res + ";"; 
    return res; 
  }  

  public String toEtl()
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value; } 
    res = res + ";"; 
    return res; 
  }  

  public boolean typeCheck(Vector types, Vector entities, Vector ctxs, Vector env)
  { if (value == null) { return true; } 
    return value.typeCheck(types,entities,ctxs,env); 
  }  

  public void displayImp(String var, PrintWriter out) 
  { } 
 
  public Expression wpc(Expression post)
  { return post; }  

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities,
                           Vector vars)
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value.queryForm(env,local); } 
    res = res + ";"; 
    return res; 
  }  

  public String updateFormJava6(java.util.Map env, boolean local)
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value.queryFormJava6(env,local); } 
    res = res + ";"; 
    return res; 
  }  

  public String updateFormJava7(java.util.Map env, boolean local)
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value.queryFormJava7(env,local); } 
    res = res + ";"; 
    return res; 
  }  

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value.queryFormCSharp(env,local); } 
    res = res + ";"; 
    return res; 
  }  

  public String updateFormCPP(java.util.Map env, boolean local)
  { String res = "  return"; 
    if (value != null)
    { res = res + " " + value.queryFormCPP(env,local); } 
    res = res + ";"; 
    return res; 
  }  

  public Vector allPreTerms()
  { Vector res = new Vector();
    if (value == null) 
    { return res; } 
    return value.allPreTerms(); 
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    if (value == null) 
    { return res; } 
    return value.allPreTerms(var); 
  }  

  public Statement dereference(BasicExpression var)
  { if (value == null) 
    { return new ReturnStatement(value); }
    Expression val = value.dereference(var); 
    return new ReturnStatement(val); 
  }  

  public Vector metavariables()
  { Vector res = new Vector(); 
    if (value != null) 
    { return value.metavariables(); }  
    return res; 
  } 

  public Vector readFrame() 
  { Vector res = new Vector();
    if (value == null) 
    { return res; } 
    return value.allReadFrame(); 
  } 

  public Vector writeFrame() 
  { Vector res = new Vector();
    return res;
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { if (value == null) 
    { return this; } 
    Expression val = value.checkConversions(propType,propElemType,interp); 
    return new ReturnStatement(val); 
  }   

  public Statement replaceModuleReferences(UseCase uc)
  { if (value == null) 
    { return this; } 
    Expression val = value.replaceModuleReferences(uc); 
    return new ReturnStatement(val); 
  }   

  public int syntacticComplexity()
  { if (value == null) 
    { return 1; } 
    return value.syntacticComplexity() + 1; 
  } 

  public int cyclomaticComplexity()
  { return 0; }  // no predicate nodes

  public int epl()
  { return 0; }  

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    if (value == null) 
    { return res; } 
    return value.allOperationsUsedIn(); 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    if (value == null) 
    { return res; } 
    return value.equivalentsUsedIn(); 
  } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    if (value != null) 
    { args.add(value.cg(cgs)); } 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  }
}


class BreakStatement extends Statement
{ public void display()
  { System.out.println("  break;"); }  

  public String getOperator() 
  { return "break"; } 

  public Object clone()
  { return new BreakStatement(); } 

  public String bupdateForm()
  { return " "; } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { return new BBasicStatement("skip"); } 

  public void display(PrintWriter out)
  { out.println("  break;"); }  

  public void displayJava(String t)
  { display(); }  

  public void displayJava(String t, PrintWriter out)
  { display(out); }  
 
  public Statement substituteEq(String oldE, Expression newE)
  {  
    return this; 
  } 

  public String toStringJava()
  { return "  break;"; }

  public String toEtl()
  { return "  break;"; }

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("breakstatement_"); 
    out.println(res + " : BreakStatement"); 
    out.println(res + ".statId = \"" + res + "\""); 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { return true; }  
 
  public Expression wpc(Expression post)
  { return post; }  

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, 
                           Vector entities, Vector vars)
  { return toStringJava(); }  

  public String updateFormJava6(java.util.Map env, boolean local)
  { return toStringJava(); }  

  public String updateFormJava7(java.util.Map env, boolean local)
  { return toStringJava(); }  

  public String updateFormCSharp(java.util.Map env, boolean local)
  { return toStringJava(); }  

  public String updateFormCPP(java.util.Map env, boolean local)
  { return toStringJava(); }  

  public Statement dereference(BasicExpression var)
  { return new BreakStatement(); }  

  public Vector readFrame() 
  { Vector res = new Vector();
    return res; 
  } 

  public Vector writeFrame() 
  { Vector res = new Vector();
    return res;
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return new BreakStatement(); } 

  public Statement replaceModuleReferences(UseCase uc)
  { return new BreakStatement(); } 

  public int syntacticComplexity()
  { return 1; } 

  public int cyclomaticComplexity()
  { return 0; }  // no predicate nodes

  public int epl()
  { return 0; }  

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector metavariables()
  { Vector res = new Vector(); 
    return res; 
  }
 
  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  }
}


class InvocationStatement extends Statement
{ String action; 
  String target; 
  // Event event;
  String assignsTo = "";
  String assignsType = ""; 

  private Vector parameters = new Vector();  
  Expression callExp; 


  public InvocationStatement(Event ee)
  { // event = ee; 
    action = ee.label; 
    assignsTo = null; 
    target = null; }

  /* InvocationStatement(String var, Event ee) 
  { assignsTo = var; 
    action = ee.label;  // ??? 
    event = ee; }  */ 

  public String getOperator() 
  { return "call"; } 

  InvocationStatement(String act, String targ, String assigns)
  { action = act; 
    target = targ; 
    assignsTo = assigns; 
  } 

  InvocationStatement(BehaviouralFeature bf)
  { action = bf.getName(); 
    target = null; 
    assignsTo = null; 
    parameters = new Vector(); 
    parameters.addAll(bf.getParameters()); 
    BasicExpression calle = 
         new BasicExpression(bf + "", 0);
    Expression callee = calle.checkIfSetExpression();
    if (callee == null) { return; }
    if (bf.isQuery())
    { callee.setUmlKind(Expression.QUERY); } 
    else 
    { callee.setUmlKind(Expression.UPDATEOP); } 
    callee.setType(bf.getResultType());
    callee.setElementType(bf.getElementType());
    callee.setEntity(bf.getEntity());
    callExp = callee; 
  } 

  InvocationStatement(BasicExpression be)
  { action = be.getData(); 
    target = null; 
    assignsTo = null; 
    parameters = new Vector(); 
    parameters.addAll(be.getParameters()); 
    callExp = be; 
  } 

  InvocationStatement(String act)
  { action = act; 
    target = null; 
    assignsTo = null; 
    callExp = new BasicExpression(act); 
  } 

  public boolean isSkip()
  { if ("skip".equals(action)) { return true; } 
    return false; 
  } 

  public void setCallExp(Expression e)
  { callExp = e; } 

  public void setAssignsTo(String atype, String avar)
  { assignsType = atype; 
    assignsTo = avar; 
  } 

  public void setEntity(Entity ent)
  { entity = ent; 
    if (callExp != null) 
    { callExp.setEntity(ent); }  
  } 

  public Object clone()
  { InvocationStatement res = new InvocationStatement(action,target,assignsTo);
    res.setCallExp(callExp); // clone it 
    res.setAssignsTo(assignsType,assignsTo); 
    res.entity = entity; 
    return res; 
  } // parameters? 

  public Statement dereference(BasicExpression var)
  { InvocationStatement res = new InvocationStatement(action,target,assignsTo); 
    if (callExp != null) 
    { res.setCallExp(callExp.dereference(var)); }
    res.entity = entity; 
    return res; 
  }  // parameters? 


  public Statement substituteEq(String oldE, Expression newE)
  { String act = action; 
    String targ = target; 
    String ast = assignsTo; 
    
    if (target != null && target.equals(oldE))
    { targ = newE.toString(); } 

    if (assignsTo != null && assignsTo.equals(oldE))
    { ast = newE.toString(); }


    InvocationStatement res = new InvocationStatement(act,targ,ast);
    res.entity = entity;
  
    if (callExp != null)
    { Expression newce = callExp.substituteEq(oldE,newE); 
      res.setCallExp(newce);
    }

    return res; 
  } // parameters? 

  public String toString()  /* B display */  
  { String res = ""; 
    if (assignsTo != null) 
    { res = assignsTo + " <-- "; } 
    res = res + action; 
    if (target != null)   /* Instance of multiple component */ 
    { res = res + "(" + target + ")"; } 
    return res; 
  } 

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("operationcallstatement_"); 
    out.println(res + " : OperationCallStatement"); 
    out.println(res + ".statId = \"" + res + "\""); 

    if (assignsTo != null) 
    { out.println(res + ".assignsTo = " + assignsTo); } 

    if (callExp != null)
    { String callid = callExp.saveModelData(out); 
      out.println(res + ".callExp = " + callid);
    }

    return res; 
  } 

  public String bupdateForm()
  { return toString(); } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { BasicExpression cex = (BasicExpression) callExp; 
        String callString = cex.data; 
        BExpression uf = cex.objectRef.binvariantForm(env,local);
        Vector pars = new Vector(); 
        pars.add(uf); 
        return new BOperationCall(callString, pars);
      }
    } 
    return new BBasicStatement("skip"); 
  }  

  public String toStringJava() 
  { String res = ""; 
    if ("skip".equals(action)) { return res; } 
    
    if (assignsTo != null)  
    { res = assignsTo + " = "; }
    if (target != null) 
    { res = res + target + "."; }  
    res = res + action + ";";  
    return res; 
  } 

  public String toStringJava(String targ)
  { String res = "";
    if (assignsTo != null)
    { res = assignsTo + " = "; }
    if (targ != null)        /* Overrides target */ 
    { res = res + targ + "."; }
    res = res + action + ";";
    return res;
  }


  public String toEtl()
  { String res = "";
    if (assignsTo != null)
    { res = assignsTo + " = "; }
    res = res + action + ";";
    return res;
  }


  public void display()
  { 
    System.out.print(toString()); 
  }

  public void display(PrintWriter out)
  { out.print(toString()); }

  public void displayJava(String targ)
  { if (targ != null) 
    { System.out.print(toStringJava(targ)); }
    else 
    { System.out.print(toStringJava()); } 
  }

  public void displayJava(String targ, PrintWriter out)
  { if (targ != null) 
    { out.print(toStringJava(targ)); }
    else 
    { out.print(toStringJava()); }  
  }

  public boolean typeCheck(Vector types, Vector entities, Vector ctxs, Vector env)
  { if (callExp != null)
    { callExp.typeCheck(types,entities,ctxs,env); } 
    return true;
  }  

  public Expression wpc(Expression post)
  { return post; }

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller.inst()." + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = callExp.updateForm(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }

  public String deltaUpdateForm(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller.inst()." + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = ((BasicExpression) callExp).deltaUpdateForm(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormJava6(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller.inst()." + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = callExp.updateFormJava6(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller.inst()." + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = callExp.updateFormJava7(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }


  public String updateFormCSharp(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller.inst()." + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = callExp.updateFormCSharp(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { if (callExp != null)
    { if (callExp instanceof BasicExpression)
      { String callString = ((BasicExpression) callExp).data; 
        if ("loadModel".equals(callString))
        { return "    " + callExp + ";"; } 
        if ("saveModel".equals(callString))
        { return "    Controller::inst->" + callExp + ";"; } 
        else
        { String call = assignsType + " " + assignsTo; 
          String uf = callExp.updateFormCPP(env,local);
          if (assignsTo != null && assignsTo.length() > 0)
          { return call + " = " + uf; } 
          else 
          { return "   " + uf; }  
        }
      }
      else 
      { return toStringJava(); }  
    } 
    else 
    { return toStringJava(); }  
  }
  // But the assignsType needs to be converted to C++, likewise for C#

  public Vector allPreTerms()
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.allPreTerms(); 
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.allPreTerms(var); 
  }  

  public Vector readFrame() 
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    if (callExp instanceof BasicExpression)
    { BasicExpression callbe = (BasicExpression) callExp; 
      String callString = callbe.data;
      Vector callpars = callbe.getParameters();
      for (int i = 0; i < callpars.size(); i++) 
      { Expression callpar = (Expression) callpars.get(i); 
        res.addAll(callpar.allReadFrame()); 
      } 
 
      if (entity != null) 
      { BehaviouralFeature op = entity.getDefinedOperation(callString); 
        if (op != null) 
        { Expression post = op.getPost(); 
          Vector params = op.getParameters(); 
          Vector postrd = post.allReadFrame(); 
          // subtract each params name:
          res.addAll(postrd);  
          for (int p = 0; p < params.size(); p++) 
          { String par = "" + params.get(p); 
            res.remove(par); 
          } 
        }
        // System.out.println("Invocation " + callString + " READ FRAME= " + res); 
        return res; 
      } 
    }   
    return callExp.allReadFrame(); 
  } 

  public Vector writeFrame() 
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    if (callExp instanceof BasicExpression)
    { BasicExpression callbe = (BasicExpression) callExp; 
      String callString = callbe.data;
      Vector callpars = callbe.getParameters();
      
      if (entity != null) 
      { BehaviouralFeature op = entity.getDefinedOperation(callString); 
        if (op != null) 
        { Expression post = op.getPost(); 
          Vector params = op.getParameters(); 
          Vector postrd = post.writeFrame(); 
          // subtract each params name:
          res.addAll(postrd);  
          for (int p = 0; p < params.size(); p++) 
          { String par = "" + params.get(p); 
            res.remove(par); 
          } 
        }
        // System.out.println("Invocation " + callString + " WRITE FRAME= " + res); 
        return res; 
      } 
    }   
    return res; 
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc)
  { if (callExp == null) { return this; } 
    BasicExpression ce = (BasicExpression) callExp.replaceModuleReferences(uc);
    return new InvocationStatement(ce); 
  } 

  public int syntacticComplexity()
  { if (callExp == null) 
    { return 1; } 
    return callExp.syntacticComplexity() + 1; 
  } 

  public int cyclomaticComplexity()
  { return 0; }  // no predicate nodes

  public int epl()
  { return 0; }  

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    if (callExp == null) 
    { return res; } 
    return callExp.allOperationsUsedIn(); 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    if (callExp == null) 
    { return res; } 
    return callExp.equivalentsUsedIn(); 
  } 

  public Vector metavariables()
  { Vector res = new Vector();
    if (callExp != null) 
    { return callExp.metavariables(); }  
    return res; 
  } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    if (etext.equals("skip")) 
    { return "    {}"; }
	
    Vector args = new Vector();
   /* if (callExp != null && callExp instanceof BasicExpression) 
    { String res = ""; 
      BasicExpression call = (BasicExpression) callExp; 
      Vector pps = call.getParameters();
      String parstring = "(";  
      if (pps != null) 
      { for (int i = 0; i < pps.size(); i++) 
        { Expression par = (Expression) pps.get(i);
	     parstring = parstring + par.cg(cgs); 
	     if (i < pps.size() - 1) 
	      { parstring = parstring + ","; }
	    }
	  }
	  parstring = parstring + ")";
	   
	  if (call.getObjectRef() != null)
	  { res = "    " + call.objectRef.cg(cgs) + "." + call.getData() + parstring + ";\n"; }
	  else 
	  { res = "    " + call.getData() + parstring + ";\n"; }
	  return res; 
	}   
	else */ 
    if (callExp != null) 
    { args.add(callExp.cg(cgs));  
      Vector eargs = new Vector();
      eargs.add(callExp);  
      CGRule r = cgs.matchedStatementRule(this,etext);
      if (r != null)
      { return r.applyRule(args,eargs,cgs); }
    } 
    return etext;
  }

  public Vector cgparameters()
  { Vector args = new Vector();
    if (callExp != null) 
    { args.add(callExp); } 
    return args; 
  } 

}


class ImplicitInvocationStatement extends Statement
{ Expression callExp; 


  public ImplicitInvocationStatement(Expression ee)
  { callExp = ee; } 

  public void setEntity(Entity ent)
  { entity = ent; 
    callExp.setEntity(ent); 
  } 

  public String getOperator() 
  { return "execute"; } 

  public boolean isSkip()
  { if ("true".equals(callExp + "")) 
    { return true; } 
    return false; 
  } 

  public Object clone()
  { ImplicitInvocationStatement res = 
      new ImplicitInvocationStatement(callExp);
    res.entity = entity; 
    return res; 
  } 

  public Statement dereference(BasicExpression var)
  { ImplicitInvocationStatement res = 
      new ImplicitInvocationStatement(callExp.dereference(var));
    res.entity = entity; 
    return res; 
  } 

  public Statement substituteEq(String oldE, Expression newE)
  { Expression newExp = callExp.substituteEq(oldE,newE); 

    return new ImplicitInvocationStatement(newExp); 
  } 

  public String toString()  /* B display */  
  { String res = "execute ( " + callExp + " )"; 
    return res; 
  } 

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("implicitcallstatement_"); 
    out.println(res + " : ImplicitCallStatement"); 
    out.println(res + ".statId = \"" + res + "\""); 

    if (callExp != null)
    { String callid = callExp.saveModelData(out); 
      out.println(res + ".callExp = " + callid);
    }

    return res; 
  } 

  public String bupdateForm()
  { return " " + callExp; }   // ANY vars' WHERE callExp[vars'/vars] THEN vars := vars' 
                              // where vars are variables of callExp 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { return callExp.bupdateForm(env,local); 
    /* Vector fs = callExp.allFeaturesUsedIn(); 
    BExpression qual = callExp.binvariantForm(env,local);
    BParallelStatement bps = new BParallelStatement(); 
    Vector newfs = new Vector(); 
    for (int i = 0; i < fs.size(); i++) 
    { String feat = (String) fs.get(i); 
      String featnew = feat + "_new"; 
      newfs.add(featnew); 
      BBasicExpression newbfeat = new BBasicExpression(featnew); 
      qual = qual.substituteEq(feat,newbfeat);
      bps.addStatement(new BAssignStatement(new BBasicExpression(feat), newbfeat));  
    }  
    return new BAnyStatement(newfs,qual,bps); */     
  } 

  public String toStringJava() 
  { String res = "execute ( " + callExp + " )"; 
    return res; 
  } 

  public String toEtl() 
  { String res = "  " + callExp + ";"; 
    return res; 
  } 


  public String toStringJava(String targ)
  { return toStringJava(); }


  public void display()
  { 
    System.out.print(toString()); 
  }

  public void display(PrintWriter out)
  { out.print(toString()); }

  public void displayJava(String targ)
  { if (targ != null) 
    { System.out.print(toStringJava(targ)); }
    else 
    { System.out.print(toStringJava()); } 
  }

  public void displayJava(String targ, PrintWriter out)
  { if (targ != null) 
    { out.print(toStringJava(targ)); }
    else 
    { out.print(toStringJava()); }  
  }

  public boolean typeCheck(Vector types, Vector entities, Vector ctxs, Vector env)
  { if (callExp != null)
    { callExp.typeCheck(types,entities,ctxs,env); } 
    return true;
  }  

  public Expression wpc(Expression post)
  { return post; }

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public Statement generateDesign(java.util.Map env, boolean local)
  { return callExp.generateDesign(env,local); }  

  public Statement statLC(java.util.Map env, boolean local)
  { return callExp.statLC(env,local); }  

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { if (callExp != null)
    { String uf = callExp.updateForm(env,local);
      return "   " + uf;   
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormJava6(java.util.Map env, boolean local)
  { if (callExp != null)
    { String uf = callExp.updateFormJava6(env,local);
      return "   " + uf;   
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { if (callExp != null)
    { String uf = callExp.updateFormJava7(env,local);
      return "   " + uf;   
    } 
    else 
    { return toStringJava(); }  
  }


  public String updateFormCSharp(java.util.Map env, boolean local)
  { if (callExp != null)
    { String uf = callExp.updateFormCSharp(env,local);
      return "   " + uf;   
    } 
    else 
    { return toStringJava(); }  
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { if (callExp != null)
    { String uf = callExp.updateFormCPP(env,local);
      return "   " + uf;   
    } 
    else 
    { return toStringJava(); }  
  }


  public Vector allPreTerms()
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.allPreTerms(); 
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.allPreTerms(var); 
  }  

  public Vector readFrame() 
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.readFrame(); 
  } 

  public Vector writeFrame() 
  { Vector res = new Vector();
    if (callExp == null) 
    { return res; } 
    return callExp.writeFrame(); 
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc)
  { if (callExp == null) { return this; } 
    Expression ce = callExp.replaceModuleReferences(uc);
    return new ImplicitInvocationStatement(ce); 
  } 

  public int syntacticComplexity()
  { if (callExp == null) 
    { return 1; } 
    return callExp.syntacticComplexity() + 1; 
  } 

  public int cyclomaticComplexity()
  { return 0; }  // no predicate nodes

  public int epl()
  { return 0; }  

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    if (callExp == null) 
    { return res; } 
    return callExp.allOperationsUsedIn(); 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    if (callExp == null) 
    { return res; } 
    return callExp.equivalentsUsedIn(); 
  } 

  public Vector metavariables()
  { Vector res = new Vector();
    if (callExp != null) 
    { return callExp.metavariables(); }  
    return res; 
  } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    if (callExp != null) 
    { args.add(callExp.cg(cgs)); } 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  } 

  public Vector cgparameters()
  { Vector args = new Vector();
    if (callExp != null) 
    { args.add(callExp); } 
    return args; 
  } 
}



class WhileStatement extends Statement
{ private Expression loopTest; 
  private Statement body; 
  // also need invariant and variant for B
  private Expression invariant; 
  private Expression variant; 
  private int loopKind = WHILE; 
  
  private Expression loopVar; // for (loopVar : loopRange) do ...
  private Expression loopRange; 

  public WhileStatement(Expression e, Statement b)
  { loopTest = e; 
    if (b == null) 
    { body = new SequenceStatement(); } 
    else 
    { body = b; } 
  } 

  public String getOperator() 
  { if (loopKind == WHILE) 
    { return "while"; }
    return "for"; 
  } 
 

  public void setLoopKind(int lk)
  { loopKind = lk; } 

  public void setLoopRange(Expression lv, Expression lr)
  { loopVar = lv;
    loopRange = lr;
  } 

  public void setEntity(Entity e)
  { entity = e; 
    if (body != null) 
    { body.setEntity(e); } 
  }

  public Statement getBody()
  { return body; } 

  public Expression getTest()
  { return loopTest; } 

  public Object clone()
  { Expression lv = null; 
    if (loopVar != null) 
    { lv = (Expression) loopVar.clone(); }  
    Expression lr = null; 
    if (loopRange != null) 
    { lr = (Expression) loopRange.clone(); }  
    Expression lt = null; 
    if (loopTest != null) 
    { lt = (Expression) loopTest.clone(); }  
    Statement newbody = (Statement) body.clone(); 
    WhileStatement res = new WhileStatement(lt,newbody); 
    res.setEntity(entity); 
    res.setLoopKind(loopKind); 
    res.setLoopRange(lv,lr); 
    res.setBrackets(brackets); 
    Expression inv = null; 
    if (invariant != null) 
    { inv = (Expression) invariant.clone(); }  
    res.setInvariant(inv); 
    Expression var = null; 
    if (variant != null) 
    { var = (Expression) variant.clone(); }  
    res.setVariant(var); 

    return res; 
  } 

  public Statement dereference(BasicExpression var)
  { Expression lv = null; 
    if (loopVar != null) 
    { lv = (Expression) loopVar.clone(); }  
    Expression lr = null; 
    if (loopRange != null) 
    { lr = (Expression) loopRange.dereference(var); }  
    Expression lt = null; 
    if (loopTest != null) 
    { lt = (Expression) loopTest.dereference(var); }
    if ((var + "").equals(loopVar + ""))
    { WhileStatement res1 = new WhileStatement(lt,body); 
      res1.setEntity(entity); 
      res1.setLoopKind(loopKind); 
      res1.setLoopRange(lv,lr); 
      res1.setBrackets(brackets); 
      res1.setInvariant(invariant); 
      res1.setVariant(variant); 
      return res1; 
    } 
    Statement newbody = (Statement) body.dereference(var); 
    WhileStatement res = new WhileStatement(lt,newbody); 
    res.setEntity(entity); 
    res.setLoopKind(loopKind); 
    res.setLoopRange(lv,lr); 
    res.setBrackets(brackets); 
    Expression inv = null; 
    if (invariant != null) 
    { inv = (Expression) invariant.dereference(var); }  
    res.setInvariant(inv); 
    Expression vv = null; 
    if (variant != null) 
    { vv = (Expression) variant.dereference(var); }  
    res.setVariant(vv); 

    return res; 
  } 

  public void setInvariant(Expression inv) 
  { invariant = inv; } 

  public void setVariant(Expression inv) 
  { variant = inv; } 

  public static WhileStatement createInvocationLoop(BasicExpression call, Expression range)
  { String v = Identifier.nextIdentifier("loopvar$"); 
    BasicExpression ve = new BasicExpression(v); 

    Type elemt = range.getElementType(); 
    ve.setType(elemt);
    if (elemt != null) 
    { ve.setElementType(elemt.getElementType()); } 
    ve.umlkind = Expression.VARIABLE; 

    BinaryExpression test = new BinaryExpression(":", ve, range); 
    test.setType(new Type("boolean", null)); 
    test.setElementType(new Type("boolean", null)); 

    BasicExpression invokee = (BasicExpression) call.clone(); 
    invokee.setObjectRef(ve); 

    InvocationStatement invoke = new InvocationStatement(invokee); 
    WhileStatement lp = new WhileStatement(test, invoke); 
    lp.setLoopKind(Statement.FOR);
    lp.setLoopRange(ve,range);  
        // lp.setLoopTest(test); 
    return lp;
  } 
  
  public String bupdateForm()
  { String res = "  WHILE (" + loopTest + ")"; 
    res = res + "  DO \n "; 
    res = res + body.bupdateForm(); 
    if (invariant != null) 
    { res = res + "  INVARIANT " + invariant; } 
    if (variant != null) 
    { res = res + "  VARIANT " + variant; } 
    res = res + "  END";
    return res;  
  } // for loops: introduce new index variable

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression btest = new BBasicExpression("true"); 
    if (loopRange != null && loopVar != null)
    { // for sequence: 
      if (loopRange.isOrdered())
      { String ind = Identifier.nextIdentifier(loopVar + "_ind");
        BasicExpression indbe = new BasicExpression(ind); 
        BBasicExpression indbeb = new BBasicExpression(ind);   
        Expression loopRangeSize0 = new UnaryExpression("->size",loopRange); 
        Expression tst0 = new BinaryExpression("<=", indbe, loopRangeSize0); 
        btest = tst0.binvariantForm(env,local); 
        BParallelStatement ss = new BParallelStatement(false); 
        BAssignStatement bast = new BAssignStatement(indbeb,new BBasicExpression("1"));
        BApplyExpression seqAtInd = new BApplyExpression(loopRange.binvariantForm(env,local),indbeb); 
        BinaryExpression loopRangeSize = new BinaryExpression("+",
                                           new UnaryExpression("->size",loopRange),
                                           new BasicExpression("1")); 
        Expression tst = new BinaryExpression("<=", indbe, loopRangeSize); 
        BExpression invb = tst.binvariantForm(env,local);
        BStatement bbody = body.bupdateForm(env,local); 
        BAssignStatement bast0 = new BAssignStatement(
            new BBasicExpression(loopVar + ""), seqAtInd); 
        ss.addStatement(bast0); 
        ss.addStatement(bbody); 
        BAssignStatement bast1 = new BAssignStatement(indbeb,
                                   new BBinaryExpression("+", indbeb, new BBasicExpression("1")));
        ss.addStatement(bast1); 
        BinaryExpression var1 = 
          new BinaryExpression("+",
            new BinaryExpression("-",new UnaryExpression("->size",loopRange),indbe), 
                                        new BasicExpression("1")); 
        BExpression bvar1 = var1.binvariantForm(env,local); 
        BStatement loop1 = new BLoopStatement(btest,invb,bvar1,
                                  new BVarStatement(loopVar + "", ss) );
        BParallelStatement ss0 = new BParallelStatement(false); 
        ss0.addStatement(bast); 
        ss0.addStatement(loop1); 
        BStatement res = new BVarStatement(ind,ss0);
        return res;  
      }
      else 
      { String ind = Identifier.nextIdentifier(loopVar + "_unprocessed");
        BasicExpression indbe = new BasicExpression(ind); 
        BBasicExpression indbeb = new BBasicExpression(ind);  
        BExpression loopvarb = loopVar.binvariantForm(env,local);  
        BExpression brange = loopRange.binvariantForm(env,local); 
        BExpression emptysetb = new BSetExpression(); 
        btest = new BBinaryExpression("/=", indbeb, emptysetb); 
        BParallelStatement ss = new BParallelStatement(false); 
        BAssignStatement bast = new BAssignStatement(indbeb,brange);
        BExpression indInRange = new BBinaryExpression(":",loopvarb,indbeb); 
        Expression tst = new BinaryExpression("<:", indbe, loopRange); 
        BExpression invb = tst.binvariantForm(env,local);
        BStatement bbody = body.bupdateForm(env,local); 
        Vector indsetelems = new Vector(); 
        indsetelems.add(loopVar); 
        SetExpression indset = new SetExpression(indsetelems); 
        
        BExpression indsetb = indset.binvariantForm(env,local); 
        BAssignStatement bast0 = new BAssignStatement(indbeb, 
                                       new BBinaryExpression("-", indbeb, indsetb)); 
        ss.addStatement(bast0); 
        ss.addStatement(bbody); 
        Expression var1 = new UnaryExpression("->size",indbe); 
        BExpression bvar1 = var1.binvariantForm(env,local); 
        Vector loopanyvars = new Vector(); 
        loopanyvars.add(loopVar + ""); 
        BStatement loop1 = new BLoopStatement(btest,invb,bvar1,
                                   new BAnyStatement(loopanyvars, indInRange, ss) );
        BParallelStatement ss0 = new BParallelStatement(false); 
        ss0.addStatement(bast); 
        ss0.addStatement(loop1); 
        BStatement res = new BVarStatement(ind,ss0);
        return res;  
      } 
    } 
    if (loopTest != null) 
    { btest = loopTest.binvariantForm(env,local); }  
    BExpression binv = new BBasicExpression("true"); 
    if (invariant != null) 
    { binv = invariant.binvariantForm(env,local); } 
    BExpression bvar = new BBasicExpression("true"); 
    if (variant != null) 
    { bvar = variant.binvariantForm(env,local); }  

    // System.out.println("LOOP BODY = " + body); 

    BStatement bbody = body.bupdateForm(env,local); 

    // System.out.println("LOOP BODY = " + bbody); 

    return new BLoopStatement(btest,binv,bvar,bbody); 
  } 

  public void display()
  { System.out.println("  WHILE (" + loopTest + ")"); 
    if (invariant != null) 
    { System.out.println("  INVARIANT " + invariant); } 
    if (variant != null) 
    { System.out.println("  VARIANT " + variant); } 

    System.out.println("  DO \n "); 
    body.display(); 
    System.out.println("  END"); 
  } 

  public void display(PrintWriter out)
  { out.println("  WHILE (" + loopTest + ")"); 
    if (invariant != null) 
    { out.println("  INVARIANT " + invariant); } 
    if (variant != null) 
    { out.println("  VARIANT " + variant); } 
    out.println("  DO\n "); 
    body.display(out); 
    out.println("  END"); 
  } 

  public void displayImp(String var, PrintWriter out) 
  { out.println("  WHILE (" + loopTest + ")"); 
    if (invariant != null) 
    { out.println("  INVARIANT " + invariant); } 
    if (variant != null) 
    { out.println("  VARIANT " + variant); } 
    out.println("  DO\n "); 
    body.displayImp(var,out); 
    out.println("  END"); 
  } 

 
  public void displayJava(String t) 
  { if (brackets)
    { System.out.println(" ( while ( " + loopTest.toJava() + " )"); 
      System.out.println("   {\n "); 
      body.displayJava(t); 
      System.out.println("   } )"); 
    } 
    else 
    { System.out.println("  while ( " + loopTest.toJava() + " )"); 
      System.out.println("  {\n "); 
      body.displayJava(t); 
      System.out.println("  }");
    }  
  } 

  public void displayJava(String t, PrintWriter out)
  { String loop = "while"; 
    if (loopKind == FOR)
    { loop = "for"; } 

    if (brackets)
    { out.println(" ( " + loop + " ( " + loopTest.toJava() + " )"); 
      out.println("   {\n "); 
      body.displayJava(t,out); 
      out.println("  } )");
    } 
    else  
    { out.println("  " + loop + " ( " + loopTest.toJava() + " )"); 
      out.println("  {\n "); 
      body.displayJava(t,out); 
      out.println("  }");
    }  
  }   

  public String saveModelData(PrintWriter out)
  { String res = ""; 

    if (loopKind == FOR)
    { res = Identifier.nextIdentifier("boundedloopstatement_"); 
      out.println(res + " : BoundedLoopStatement");
    } 
    else 
    { res = Identifier.nextIdentifier("unboundedloopstatement_"); 
      out.println(res + " : UnboundedLoopStatement");
    } 
    out.println(res + ".statId = \"" + res + "\""); 

    String testid = loopTest.saveModelData(out); 
    String bodyid = body.saveModelData(out); 
    out.println(res + ".test = " + testid); 
    out.println(res + ".body = " + bodyid);  

    if (loopVar != null) 
    { String lvid = loopVar.saveModelData(out); 
      out.println(res + ".loopVar = " + lvid);
    } 

    if (loopRange != null) 
    { String lrid = loopRange.saveModelData(out); 
      out.println(res + ".loopRange = " + lrid);
    } 
 
    
    return res; 
  } 

 
  public Statement substituteEq(String oldE, Expression newE)
  { Statement newbody = body.substituteEq(oldE,newE); 
    Expression lv = null; 
    if (loopVar != null) 
    { lv = loopVar.substituteEq(oldE,newE); }  
    Expression lr = null; 
    if (loopRange != null) 
    { lr = loopRange.substituteEq(oldE,newE); }  
    Expression lt = null; 
    if (loopTest != null) 
    { lt = loopTest.substituteEq(oldE,newE); }  
    WhileStatement res = new WhileStatement(lt,newbody); 
    res.setEntity(entity); 
    res.setLoopKind(loopKind); 
    res.setLoopRange(lv,lr); 
    res.setBrackets(brackets);
 
    Expression inv = null; 
    if (invariant != null) 
    { inv = (Expression) invariant.substituteEq(oldE,newE); }  
    res.setInvariant(inv); 

    Expression var = null; 
    if (variant != null) 
    { var = (Expression) variant.substituteEq(oldE,newE); }  
    res.setVariant(var); 

    return res; 
  }  

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { Statement newbody = body.checkConversions(e,propType, propElemType, interp); 
    WhileStatement res = new WhileStatement(loopTest,newbody); 
    res.setEntity(entity); 
    res.setLoopKind(loopKind); 
    res.setLoopRange(loopVar,loopRange); 
    res.setBrackets(brackets);
    res.setInvariant(invariant); 
    res.setVariant(variant); 

    return res; 
  } 

  public Statement replaceModuleReferences(UseCase uc)
  { Statement newbody = body.replaceModuleReferences(uc);
    Expression lt = loopTest.replaceModuleReferences(uc);  
    WhileStatement res = new WhileStatement(lt,newbody); 
    res.setEntity(entity); 
    res.setLoopKind(loopKind);
    Expression lr = loopRange; 
    if (loopRange != null) 
    { lr = loopRange.replaceModuleReferences(uc); } 
 
    res.setLoopRange(loopVar,lr); 
    res.setBrackets(brackets);
    res.setInvariant(invariant); 
    res.setVariant(variant); 

    return res; 
  } 

  public String toStringJava() 
  { java.util.Map env = new java.util.HashMap(); 
    if (entity != null)
    { String ename = entity.getName(); 
      env.put(ename,"this");
    } 
    String loop = "while"; 
    if (loopKind == FOR)
    { loop = "for"; } 

    String res = "  " + loop + " (" + loopTest.queryForm(env,false) + ")"; 
    res = res + "  {\n "; 
    res = res + body.toStringJava(); 
    return res + "  }"; 
  } 

  public String toEtl() 
  { String loop = "while"; 
    if (loopKind == FOR)
    { loop = "for"; } 

    String res = "  " + loop + " (" + loopTest + ")"; 
    res = res + "  {\n "; 
    res = res + body.toEtl(); 
    return res + "  }"; 
  } 

  public String toString()
  { String res = " while "; 
    if (loopKind == FOR)
    { res = " for "; } 
    res = res + loopTest + " do " + body + " "; 
    if (brackets)
    { res = "( " + res + " )"; } 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector ctxs, Vector env)
  { Vector env1 = new Vector(); 
    env1.addAll(env);
    /* A copy should be made of env, but lots of bad things happen if this is done. We 
       don't know why. */
 
    boolean res = loopTest.typeCheck(types,entities,ctxs,env);  
    if (loopRange != null) 
    { res = loopRange.typeCheck(types,entities,ctxs,env);
      Type lrt = loopRange.getType(); 
      Type lret = loopRange.getElementType(); 

      // System.out.println(">>> Type of loop range " + loopRange + " is " + lrt + "(" + lret + ")");   
      Attribute lv = new Attribute(loopVar + "", lret, ModelElement.INTERNAL); 
      if (lret != null) 
      { lv.setElementType(lret.getElementType()); 
        if (lret.isEntity())
        { lv.setEntity(lret.getEntity()); } 
      } 
      // System.out.println(">>> Entity of loop variable " + lv + " is " + lv.getEntity());   
      env1.add(lv); 
    } 
    return body.typeCheck(types,entities,ctxs,env1); 
  }  

  public Expression wpc(Expression post)
  { return loopTest; } // actually the invariant

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  
  // should not occur as a transition action

  public boolean updates(Vector v) 
  { return false; } 

  public Statement generateDesign(java.util.Map env, boolean local)
  { Statement bdy = body.generateDesign(env,local); 
    WhileStatement result = (WhileStatement) clone(); 
    if (loopRange != null && loopRange instanceof BasicExpression)
    { if (loopRange.umlkind == Expression.CLASSID) 
      { BasicExpression lr = new BasicExpression("allInstances"); 
        lr.umlkind = Expression.FUNCTION;
        lr.setIsEvent(); 
        lr.setParameters(null);  
        lr.type = loopRange.type; 
        lr.elementType = loopRange.elementType; 
        lr.setObjectRef(loopRange); 
        result.loopRange = lr; 
      } 
    } 
    result.body = bdy; 
    return result; 
  }  

  public Statement statLC(java.util.Map env, boolean local)
  { Statement bdy = body.statLC(env,local); 
    WhileStatement result = (WhileStatement) clone(); 
    result.body = bdy; 
    return result; 
  }  

  public String updateForm(java.util.Map env, boolean local, Vector types, 
                           Vector entities, Vector vars)
  { if (loopKind == FOR)
    { if (loopVar != null && loopRange != null)
      { String lv = loopVar.queryForm(new java.util.HashMap(), local);  // env?
        String lr; 
        if (loopRange instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) loopRange; 
          lr = lran.classqueryForm(env, local); 
        } 
        else 
        { lr = loopRange.queryForm(env, local); } 

        Type et = loopRange.getElementType(); 
        String etr = "Object"; 
        if (et == null) 
        { System.err.println("!1 Error: null element type for loop range: " + loopRange);
          JOptionPane.showMessageDialog(null, "ERROR: No element type for: " + loopRange,
                                        "Type error", JOptionPane.ERROR_MESSAGE); 
          if (loopVar.getType() != null)
          { etr = loopVar.getType().getJava(); }
        }  
        else
        { etr = et.getJava(); }
 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
        env1.put(etr,lv); 

        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTerms(body, preterms, env1, local, types, entities, vars); 

        String extract = "(" + etr + ") " + rang + ".get(" + ind + ")"; 
        if ("int".equals(etr))
        { extract = "((Integer) " + rang + ".get(" + ind + ")).intValue()"; } 
        else if ("double".equals(etr))
        { extract = "((Double) " + rang + ".get(" + ind + ")).doubleValue()"; } 
        else if ("long".equals(etr))
        { extract = "((Long) " + rang + ".get(" + ind + ")).longValue()"; } 
        else if ("boolean".equals(etr))
        { extract = "((Boolean) " + rang + ".get(" + ind + ")).booleanValue()"; } 

        return "  List " + rang + " = new Vector();\n" + 
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = " + extract + ";\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } // All pre terms within body, involving lv, should be evaluated before body. 
      else if (loopTest != null && (loopTest instanceof BinaryExpression))
      { // assume it is  var : exp 
        BinaryExpression lt = (BinaryExpression) loopTest; 
        String lv = lt.left.queryForm(env, local); 
        String lr; 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
 
        if (lt.right instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) lt.right; 
          lr = lran.classqueryForm(env, local); 
        } 
        else 
        { lr = lt.right.queryForm(env, local); } 
        Type et = lt.right.getElementType(); 
        String etr = et.getJava(); 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 
        env1.put(etr,lv); 

        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTerms(body, preterms, env1, local, types, entities, vars); 

        return "  List " + rang + " = new Vector();\n" + 
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (" + etr + ") " + rang + ".get(" + ind + ");\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } 
      return "  for (" + loopTest.queryForm(env,local) + ") \n" + 
             "  { " + body.updateForm(env,local,types,entities,vars) + " }"; 
    } 
    else // loopKind == WHILE
    { return "  while (" + loopTest.queryForm(env,local) + ") \n" + 
             "  { " + body.updateForm(env,local,types,entities,vars) + " }"; 
    } 
 }  

  public String updateFormJava6(java.util.Map env, boolean local)
  { if (loopKind == FOR)
    { if (loopVar != null && loopRange != null)
      { String lv = loopVar.queryFormJava6(new java.util.HashMap(), local);  // env?
        String lr; 
        if (loopRange instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) loopRange; 
          lr = lran.classqueryFormJava6(env, local); 
        } 
        else 
        { lr = loopRange.queryFormJava6(env, local); } 

        Type et = loopRange.getElementType(); 
        String etr = "Object"; 
        if (et == null) 
        { System.err.println("Error: null element type for " + loopRange);
          JOptionPane.showMessageDialog(null, "ERROR: No element type for: " + loopRange,
                                        "Type error", JOptionPane.ERROR_MESSAGE); 
          if (loopVar.getType() != null)
          { etr = loopVar.getType().getJava6(); }
        }  
        else
        { etr = et.getJava6(); }
 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
        env1.put(etr,lv); 
        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsJava6(body, preterms, env1, local); 

        String extract = "(" + etr + ") " + rang + ".get(" + ind + ")"; 
        if ("int".equals(etr))
        { extract = "((Integer) " + rang + ".get(" + ind + ")).intValue()"; } 
        else if ("double".equals(etr))
        { extract = "((Double) " + rang + ".get(" + ind + ")).doubleValue()"; } 
        else if ("long".equals(etr))
        { extract = "((Long) " + rang + ".get(" + ind + ")).longValue()"; } 
        else if ("boolean".equals(etr))
        { extract = "((Boolean) " + rang + ".get(" + ind + ")).booleanValue()"; } 

        return "  ArrayList " + rang + " = new ArrayList();\n" +
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = " + extract + ";\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } 
      else if (loopTest != null && (loopTest instanceof BinaryExpression))
      { // assume it is  var : exp 
        BinaryExpression lt = (BinaryExpression) loopTest; 
        String lv = lt.left.queryFormJava6(env, local); 
        String lr; 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
 
        if (lt.right instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) lt.right; 
          lr = lran.classqueryFormJava6(env, local); 
        } 
        else 
        { lr = lt.right.queryFormJava6(env, local); } 
        Type et = lt.right.getElementType();   
        if (et == null) 
        { System.err.println("ERROR: no element type for loop iteration " + this); 
          et = new Type("int", null); 
        } 
        String etr = et.typeWrapperJava6();  // ok for String, entities, collections. Not prims
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 
        env1.put(etr,lv); 
        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsJava6(body, preterms, env1, local); 

        return "  ArrayList " + rang + " = new ArrayList();\n" +
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (" + etr + ") " + rang + ".get(" + ind + ");\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } 
      return "  for (" + loopTest.queryFormJava6(env,local) + ") \n" + 
             "  { " + body.updateFormJava6(env,local) + " }"; 
    } 
    else // loopKind == WHILE
    { return "  while (" + loopTest.queryFormJava6(env,local) + ") \n" + 
             "  { " + body.updateFormJava6(env,local) + " }"; 
    } 
 }  

  public String updateFormJava7(java.util.Map env, boolean local)
  { if (loopKind == FOR)
    { if (loopVar != null && loopRange != null)
      { String lv = loopVar.queryFormJava7(new java.util.HashMap(), local);  // env?
        String lr; 
        if (loopRange instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) loopRange; 
          lr = lran.classqueryFormJava7(env, local); 
        } 
        else 
        { lr = loopRange.queryFormJava7(env, local); } 

        Type et = loopRange.getElementType(); 
        String etr = "Object"; 
        if (et == null) 
        { System.err.println("Error: null element type for " + loopRange);
          JOptionPane.showMessageDialog(null, "ERROR: No element type for: " + loopRange,
                                        "Type error", JOptionPane.ERROR_MESSAGE); 
          if (loopVar.getType() != null)
          { etr = loopVar.getType().getJava7(loopVar.getElementType()); }
        }  
        else
        { etr = et.getJava7(et.getElementType()); }
 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
        env1.put(etr,lv); 
        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsJava7(body, preterms, env1, local); 

        String extract = "(" + etr + ") " + rang + ".get(" + ind + ")"; 
        if ("int".equals(etr))
        { extract = "((Integer) " + rang + ".get(" + ind + ")).intValue()"; } 
        else if ("double".equals(etr))
        { extract = "((Double) " + rang + ".get(" + ind + ")).doubleValue()"; } 
        else if ("long".equals(etr))
        { extract = "((Long) " + rang + ".get(" + ind + ")).longValue()"; } 
        else if ("boolean".equals(etr))
        { extract = "((Boolean) " + rang + ".get(" + ind + ")).booleanValue()"; } 

        return "  ArrayList<" + etr + "> " + rang + " = new ArrayList<" + etr + ">();\n" +
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = " + extract + ";\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } 
      else if (loopTest != null && (loopTest instanceof BinaryExpression))
      { // assume it is  var : exp 
        BinaryExpression lt = (BinaryExpression) loopTest; 
        String lv = lt.left.queryFormJava7(env, local); 
        String lr; 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
 
        if (lt.right instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) lt.right; 
          lr = lran.classqueryFormJava7(env, local); 
        } 
        else 
        { lr = lt.right.queryFormJava7(env, local); } 
        Type et = lt.right.getElementType(); 
        if (et == null) 
        { System.err.println("ERROR: no element type for loop iteration " + this); 
          et = new Type("int", null); 
        } 
        String etr = et.typeWrapperJava7();  
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 
        env1.put(etr,lv); 
        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsJava7(body, preterms, env1, local); 

        return "  ArrayList<" + etr + "> " + rang + " = new ArrayList<" + etr + ">();\n" +
               "  " + rang + ".addAll(" + lr + ");\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (" + etr + ") " + rang + ".get(" + ind + ");\n" +
               "    " + newbody + "\n" + 
               "  }"; 
      } 
      return "  for (" + loopTest.queryFormJava7(env,local) + ") \n" + 
             "  { " + body.updateFormJava7(env,local) + " }"; 
    } 
    else // loopKind == WHILE
    { return "  while (" + loopTest.queryFormJava7(env,local) + ") \n" + 
             "  { " + body.updateFormJava7(env,local) + " }"; 
    } 
 }  

  public String updateFormCSharp(java.util.Map env, boolean local)
  { if (loopKind == FOR)
    { if (loopVar != null && loopRange != null)
      { String lv = loopVar.queryFormCSharp(new java.util.HashMap(), local);  // env?
        String lr; 
        if (loopRange instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) loopRange; 
          lr = lran.classqueryFormCSharp(env, local); 
        } 
        else 
        { lr = loopRange.queryFormCSharp(env, local); } 

        Type et = loopRange.getElementType(); 
        String etr = "object"; 
        if (et == null) 
        { System.err.println("Error: null element type for " + loopRange);
          if (loopVar.getType() != null)
          { etr = loopVar.getType().getCSharp(); }
        }  
        else
        { etr = et.getCSharp(); }
 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
        env1.put(etr,lv); 

        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsCSharp(body, preterms, env1, local); 

        return "  ArrayList " + rang + " = " + lr + ";\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".Count; " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (" + etr + ") " + rang + "[" + ind + "];\n" +
               "    " + newbody + " }"; 
      } 
      else if (loopTest != null && (loopTest instanceof BinaryExpression))
      { // assume it is  var : exp 
        BinaryExpression lt = (BinaryExpression) loopTest; 
        String lv = lt.left.queryFormCSharp(env, local); 
        String lr; 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
 
        if (lt.right instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) lt.right; 
          lr = lran.classqueryFormCSharp(env, local); 
        } 
        else 
        { lr = lt.right.queryFormCSharp(env, local); } 
        Type et = lt.right.getElementType(); 
        String etr = et.getCSharp(); 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 
        env1.put(etr,lv); 

        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsCSharp(body, preterms, env1, local); 

        return "  ArrayList " + rang + " = " + lr + ";\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + ".Count; " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (" + etr + ") " + rang + "[" + ind + "];\n" +
               "    " + newbody + " }"; 
      } 
      return "  for (" + loopTest.queryFormCSharp(env,local) + ") \n" + 
             "  { " + body.updateFormCSharp(env,local) + " }"; 
    } 
    else // loopKind == WHILE
    { return "  while (" + loopTest.queryFormCSharp(env,local) + ") \n" + 
             "  { " + body.updateFormCSharp(env,local) + " }"; 
    } 
 }  

  public String updateFormCPP(java.util.Map env, boolean local)
  { if (loopKind == FOR)
    { if (loopVar != null && loopRange != null)
      { String lv = loopVar.queryFormCPP(new java.util.HashMap(), local);  // env?
        String lr; 
        if (loopRange instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) loopRange; 
          lr = lran.classqueryFormCPP(env, local); 
        } 
        else 
        { lr = loopRange.queryFormCPP(env, local); } 

        Type et = loopRange.getElementType(); 
        String etr = "void*"; 
        if (et == null) 
        { System.err.println("Error: null element type for " + loopRange);
          if (loopVar.getType() != null)
          { etr = loopVar.getType().getCPP(); }
        }  
        else
        { etr = et.getCPP(); }
 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
        env1.put(etr,lv); 

        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsCPP(body, preterms, env1, local); 

        return "  vector<" + etr + ">* " + rang + " = new vector<" + etr + ">();\n" + 
               "  " + rang + "->insert(" + rang + "->end(), " + lr + "->begin(), " + 
                                       lr + "->end());\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + "->size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (*" + rang + ")[" + ind + "];\n" +
               "    " + newbody + " }"; 
      } 
      else if (loopTest != null && (loopTest instanceof BinaryExpression))
      { // assume it is  var : exp 
        BinaryExpression lt = (BinaryExpression) loopTest; 
        String lv = lt.left.queryFormCPP(env, local); 
        String lr; 

        java.util.Map env1 = (java.util.HashMap) ((java.util.HashMap) env).clone();
 
        if (lt.right instanceof BasicExpression)
        { BasicExpression lran = (BasicExpression) lt.right; 
          lr = lran.classqueryFormCPP(env, local); 
        } 
        else 
        { lr = lt.right.queryFormCPP(env, local); } 
        Type et = lt.right.getElementType(); 
        String etr = et.getCPP(); 
        String ind = Identifier.nextIdentifier("_i");
        String rang = Identifier.nextIdentifier("_range"); 
        env1.put(etr,lv); 
        Vector preterms = body.allPreTerms(lv); 
        String newbody = processPreTermsCPP(body, preterms, env1, local); 

        return "  vector<" + etr + ">* " + rang + " = new vector<" + etr + ">();\n" + 
               "  " + rang + "->insert(" + rang + "->end(), " + lr + "->begin(), " + 
                                       lr + "->end());\n" + 
               "  for (int " + ind + " = 0; " + ind + " < " + rang + "->size(); " + ind + "++)\n" + 
               "  { " + etr + " " + lv + " = (*" + rang + ")[" + ind + "];\n" +
               "    " + newbody + " }"; 
      } 
      return "  for (" + loopTest.queryFormCPP(env,local) + ") \n" + 
             "  { " + body.updateFormCPP(env,local) + " }"; 
    } 
    else // loopKind == WHILE
    { return "  while (" + loopTest.queryFormCPP(env,local) + ") \n" + 
             "  { " + body.updateFormCPP(env,local) + " }"; 
    } 
  }  

  public Vector allPreTerms()
  { Vector res = body.allPreTerms();
    Vector res1 = new Vector(); 
    if (loopVar != null) 
    { res1 = body.allPreTerms(loopVar + ""); } // These must be handled *within* the loop
     
    if (loopTest == null) 
    { return res; } 
    Vector res2 = loopTest.allPreTerms();
    res.addAll(res2); // union, and loopRange?
    res.removeAll(res1); 
    return res;  
  }  

  public Vector allPreTerms(String var)
  { Vector res = body.allPreTerms(var);

    // if (loopRange != null) 
    // { res.addAll(loopRange.allPreTerms(var)); } 

    if (loopTest == null) 
    { return res; } 
    Vector res1 = loopTest.allPreTerms(var);
    res.addAll(res1); // union, and loopRange?
    return res;  
  }  

  public Vector readFrame()
  { Vector res = body.readFrame();

    if (loopRange != null) 
    { res.addAll(loopRange.allReadFrame()); } 
     
    if (loopTest == null) 
    { return res; } 
    Vector res2 = loopTest.allReadFrame();
    res.addAll(res2);  

    // System.out.println("LOOP READ FRAME = " + res); 

    return res;  
  }  

  public Vector writeFrame() 
  { Vector res = body.writeFrame();
    return res; 
  } 

  public int syntacticComplexity()
  { int res = body.syntacticComplexity(); 

    if (loopKind == FOR) 
    { if (loopRange != null) 
      { int rcomp = loopRange.syntacticComplexity(); 
        return res + rcomp + 1; 
      } 
    } 

    if (loopTest == null) 
    { return res + 1; } 
    return loopTest.syntacticComplexity() + res + 1; 
  } 

  public int cyclomaticComplexity()
  { int res = body.cyclomaticComplexity(); 

    if (loopKind == FOR && loopRange != null) 
    { return res; } // bounded loop fixed number of iterations. 

    if (loopTest == null) 
    { return res; } 
    return loopTest.cyclomaticComplexity() + res; 
  } 

  public int epl()
  { return body.epl(); }  

  public Vector allOperationsUsedIn()
  { Vector res = body.allOperationsUsedIn();

    if (loopRange != null) 
    { res.addAll(loopRange.allOperationsUsedIn()); } 
     
    if (loopTest == null) 
    { return res; } 
    Vector res2 = loopTest.allOperationsUsedIn();
    res.addAll(res2);  

    // System.out.println("LOOP READ FRAME = " + res); 

    return res;  
  }  

  public Vector equivalentsUsedIn()
  { Vector res = body.equivalentsUsedIn();

    if (loopRange != null) 
    { res.addAll(loopRange.equivalentsUsedIn()); } 
     
    if (loopTest == null) 
    { return res; } 
    Vector res2 = loopTest.equivalentsUsedIn();
    res.addAll(res2);  

    // System.out.println("LOOP READ FRAME = " + res); 

    return res;  
  }  

  public String cg(CGSpec cgs)
  { String etext = this + "";

    Vector eargs = new Vector(); 
    Vector args = new Vector();
    if (loopKind == WHILE) 
    { args.add(loopTest.cg(cgs));
      args.add(body.cg(cgs));
      eargs.add(loopTest); 
      eargs.add(body); 
    } 
    else 
    { args.add(loopVar + ""); 
      args.add(loopRange.cg(cgs)); 
      args.add(body.cg(cgs)); 
      eargs.add(loopVar); 
      eargs.add(loopRange); 
      eargs.add(body); 
    } 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext;
  } // for FOR, need the loopVar : loopRange
    // instead of test. 

  public Vector metavariables()
  { Vector res = new Vector(); 
    if (loopKind == WHILE && loopTest != null) 
    { res.addAll(loopTest.metavariables()); } 
    else if (loopVar != null && loopRange != null) 
    { res.addAll(loopVar.metavariables()); 
      res.addAll(loopRange.metavariables()); 
    }  
    res = VectorUtil.union(res,body.metavariables()); 
    return res; 
  } 


} 
 

class CreationStatement extends Statement
{ String createsInstanceOf;
  String assignsTo;
  private Type instanceType = null; 
  private Type elementType = null; 
  boolean declarationOnly = false; 
  String initialValue = null; 
  boolean isFrozen = false;  // true when a constant is declared. 

  public CreationStatement(String cio, String ast)
  { createsInstanceOf = cio;
    assignsTo = ast; 
  }

  public void setInitialValue(String init)
  { initialValue = init; } 

  public void setFrozen(boolean froz)
  { isFrozen = froz; } 

  public String getOperator() 
  { return "var"; } 

/*  public String cg(CGSpec cgs)
  { String etext = "var " + assignsTo + " : " + createsInstanceOf; 
    Vector args = new Vector();
    args.add(assignsTo);
    args.add(createsInstanceOf);
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  } */ 

  public void setInstanceType(Type t)
  { instanceType = t; 
    if (instanceType != null) 
    { createsInstanceOf = instanceType.getName(); }
  }  

  public void setType(Type t)
  { instanceType = t; 
    if (instanceType != null) 
    { createsInstanceOf = instanceType.getName(); }
  }  

  public void setElementType(Type t)
  { elementType = t; 
    if (instanceType != null) 
    { instanceType.setElementType(t); }  
  } 

  public Object clone()
  { return this; } 

  public Statement dereference(BasicExpression var)
  { return this; } 

  public Statement substituteEq(String oldE, Expression newE)
  { String cio = createsInstanceOf; 
    String ast = assignsTo; 

    if (oldE.equals(createsInstanceOf))
    { cio = newE.toString(); }
    if (oldE.equals(assignsTo))
    { ast = newE.toString(); }

    CreationStatement res = new CreationStatement(cio,ast);
    res.setType(instanceType); 
    res.setElementType(elementType);  
    return res; 
  } 

  public String toString()
  { if (initialValue != null) 
    { return "  var " + assignsTo + " = " + initialValue; } 
    else if (instanceType != null)
    { return "  var " + assignsTo + " : " + instanceType; }
    else 
    { return "  var " + assignsTo + " : " + createsInstanceOf; }
  } 

  public String saveModelData(PrintWriter out) 
  { String res = Identifier.nextIdentifier("creationstatement_"); 
    out.println(res + " : CreationStatement");
    out.println(res + ".statId = \"" + res + "\"");  
    out.println(res + ".createsInstanceOf = \"" + createsInstanceOf + "\""); 
    out.println(res + ".assignsTo = \"" + assignsTo + "\""); 
    String tname = "Integer"; // default

    if (instanceType != null) 
    { tname = instanceType.getUMLModelName(out); } 
    out.println(res + ".type = " + tname); 

    // System.out.println("Creation STAT TYpe = " + instanceType + 
    //                    " (" + instanceType.elementType + ")");  

    if (elementType != null) 
    { String etname = elementType.getUMLModelName(out); 
      out.println(res + ".elementType = " + etname); 
    } 
    else 
    { out.println(res + ".elementType = " + tname); } 
    return res; 
  } 

  public String bupdateForm()
  { return assignsTo + " :: " + createsInstanceOf; } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { Vector updates = new Vector(); 
    updates.add(assignsTo); 
    Expression assignsToE = new BasicExpression(assignsTo); 
    Expression createsInstanceOfE = new BasicExpression(createsInstanceOf); 
    Expression whereexp = new BinaryExpression(":", assignsToE, createsInstanceOfE); 
    BExpression bqual = whereexp.binvariantForm(env,local); 
    return new BAnyStatement(updates, bqual, new BBasicStatement("skip"));
  } // No - add to the entity involved. 

  public String toEtl()
  { if (initialValue != null) 
    { return "  var " + assignsTo + " = " + initialValue + ";"; } 
    else 
    { return "  var " + assignsTo + ";"; } 
  }

  public String toStringJava()
  { String mode = ""; 
    if (isFrozen) 
    { mode = "final "; } 

    if (initialValue != null) 
    { String jType = instanceType.getJava(); 
      return "  " + mode + jType + " " + assignsTo + " = " + initialValue + ";"; 
    } 
    else if (instanceType != null)
    { String jType = instanceType.getJava(); 
      if (Type.isBasicType(instanceType)) 
      { return "  " + mode + jType + " " + assignsTo + ";"; } 
      else if (declarationOnly) 
      { return "  " + mode + jType + " " + assignsTo + ";"; } 
      else if (Type.isCollectionType(instanceType))
      { return "  " + mode + "List " + assignsTo + ";"; } 
      else if (instanceType.isEntity())
      { Entity ent = instanceType.getEntity(); 
        if (ent.hasStereotype("external"))
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n"; } 
        else  
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n" + 
               "  Controller.inst().add" + jType + "(" + assignsTo + ");"; 
        } 
      } // The 2nd statement only if is an entity of this system, not external
    } 
    else if (createsInstanceOf.equals("boolean") || createsInstanceOf.equals("int") ||
        createsInstanceOf.equals("long") || 
        createsInstanceOf.equals("String") || createsInstanceOf.equals("double"))
    { return "  " + mode + createsInstanceOf + " " + assignsTo + ";"; } 

    if (createsInstanceOf.startsWith("Set") || createsInstanceOf.startsWith("Sequence"))
    { return "  List " + assignsTo + ";"; } 

    return "  " + mode + createsInstanceOf + " " + assignsTo + " = new " + createsInstanceOf + "();\n" + 
           "  Controller.inst().add" + createsInstanceOf + "(" + assignsTo + ");"; 
  }

  public String toStringJava6()
  { if (instanceType != null)
    { String jType = instanceType.getJava6(); 
      if (Type.isBasicType(instanceType)) 
      { return "  " + jType + " " + assignsTo + ";"; } 
      else if (Type.isSetType(instanceType))
      { return "  HashSet " + assignsTo + ";"; } 
      else if (Type.isSequenceType(instanceType))
      { return "  ArrayList " + assignsTo + ";"; } 
      else if (instanceType.isEntity())
      { Entity ent = instanceType.getEntity(); 
        if (ent.hasStereotype("external"))
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n"; } 
        else
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n" + 
                 "  Controller.inst().add" + jType + "(" + assignsTo + ");"; 
        }
      }  
    } 
    else if (createsInstanceOf.equals("boolean") || createsInstanceOf.equals("int") ||
        createsInstanceOf.equals("long") || 
        createsInstanceOf.equals("String") || createsInstanceOf.equals("double"))
    { return "  " + createsInstanceOf + " " + assignsTo + ";"; } 

    if (createsInstanceOf.startsWith("Set"))
    { return "  HashSet " + assignsTo + ";"; } 
    else if (createsInstanceOf.startsWith("Sequence"))
    { return "  ArrayList " + assignsTo + ";"; } 

    return "  " + createsInstanceOf + " " + assignsTo + " = new " + createsInstanceOf + "();\n" + 
           "  Controller.inst().add" + createsInstanceOf + "(" + assignsTo + ");"; 
  }

  public String toStringJava7()
  { // System.out.println("CREATION STATEMENT: " + instanceType + " " + elementType + " " + assignsTo); 
    // System.out.println("=========================================================================");
 
    if (instanceType != null)
    { String jType = instanceType.getJava7(elementType); 
      if (Type.isBasicType(instanceType) || Type.isSetType(instanceType) || 
          Type.isSequenceType(instanceType)) 
      { return "  " + jType + " " + assignsTo + ";"; } 
      else if (instanceType.isEntity())
      { Entity ent = instanceType.getEntity(); 
        if (ent.hasStereotype("external"))
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n"; } 
        else
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n" + 
                 "  Controller.inst().add" + jType + "(" + assignsTo + ");"; 
        }
      }  
    } 
    else if (createsInstanceOf.equals("boolean") || createsInstanceOf.equals("int") ||
        createsInstanceOf.equals("long") || 
        createsInstanceOf.equals("String") || createsInstanceOf.equals("double"))
    { return "  " + createsInstanceOf + " " + assignsTo + ";"; } 

    if (createsInstanceOf.startsWith("Set"))
    { return "  HashSet " + assignsTo + ";"; } 
    else if (createsInstanceOf.startsWith("Sequence"))
    { return "  ArrayList " + assignsTo + ";"; } 

    return createsInstanceOf + " " + assignsTo + " = new " + createsInstanceOf + "();\n" + 
           "  Controller.inst().add" + createsInstanceOf + "(" + assignsTo + ");"; 
  }


  public String toStringCSharp()
  { String cstype = createsInstanceOf; 
    if (instanceType != null)
    { String jType = instanceType.getCSharp(); 
      if (Type.isBasicType(instanceType)) 
      { return "  " + jType + " " + assignsTo + ";"; } 
      else if (Type.isCollectionType(instanceType))
      { return "  ArrayList " + assignsTo + ";"; } 
      else if (instanceType.isEntity())
      { Entity ent = instanceType.getEntity(); 
        if (ent.hasStereotype("external"))
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n"; } 
        else
        { return "  " + jType + " " + assignsTo + " = new " + jType + "();\n" + 
                 "  Controller.inst().add" + jType + "(" + assignsTo + ");"; 
        } 
      } 
    } 
    else if (createsInstanceOf.startsWith("Set") || createsInstanceOf.startsWith("Sequence"))
    { return "  ArrayList " + assignsTo + ";"; } 

    if (createsInstanceOf.equals("boolean")) 
    { cstype = "bool"; 
      return "  " + cstype + " " + assignsTo + ";";   
    } 
    else if (createsInstanceOf.equals("String")) 
    { cstype = "string"; 
      return "  " + cstype + " " + assignsTo + ";"; 
    } 
    else if (createsInstanceOf.equals("int") || 
             createsInstanceOf.equals("long") || createsInstanceOf.equals("double"))
    { return "  " + createsInstanceOf + " " + assignsTo + ";"; } 

    return createsInstanceOf + " " + assignsTo + " = new " + createsInstanceOf + "();\n" + 
                 "  Controller.inst().add" + createsInstanceOf + "(" + assignsTo + ");";  
  } // ignores enumerations

  public String toStringCPP()  // Need elemnt type for collections 
  { String cstype = createsInstanceOf; 
    String cet = "void*"; 
    if (instanceType != null)
    { String jType = instanceType.getCPP(elementType); 
      if (Type.isBasicType(instanceType)) 
      { return "  " + jType + " " + assignsTo + ";"; } 
      else if (Type.isCollectionType(instanceType))
      { return "  " + jType + " " + assignsTo + ";"; } 
      else if (instanceType.isEntity())
      { Entity ent = instanceType.getEntity(); 
	    String ename = ent.getName(); 
        if (ent.hasStereotype("external"))
        { return "  " + jType + " " + assignsTo + " = new " + ename + "();\n"; } 
        else
        { return "  " + jType + " " + assignsTo + " = new " + ename + "();\n" + 
                 "  Controller::inst->add" + ename + "(" + assignsTo + ");";
        }  
      } 
    } 
    else if (elementType != null) 
    { cet = elementType.getCPP(); }

    if (createsInstanceOf.startsWith("Set"))
    { return "  set<" + cet + ">* " + assignsTo + ";"; } 

    if (createsInstanceOf.startsWith("Sequence"))
    { return "  vector<" + cet + ">* " + assignsTo + ";"; } 

    if (createsInstanceOf.equals("boolean")) 
    { cstype = "bool"; 
      return "  " + cstype + " " + assignsTo + ";";   
    } 
    else if (createsInstanceOf.equals("String")) 
    { cstype = "string"; 
      return "  " + cstype + " " + assignsTo + ";"; 
    } 
    else if (createsInstanceOf.equals("int") || 
             createsInstanceOf.equals("long") || createsInstanceOf.equals("double"))
    { return "  " + createsInstanceOf + " " + assignsTo + ";"; } 

    return createsInstanceOf + " " + assignsTo + " = new " + createsInstanceOf + "();\n" + 
                 "  Controller::inst->add" + createsInstanceOf + "(" + assignsTo + ");"; 
  } // and add to the set of instances? 

  public void display()
  { System.out.print(toString()); }

  public void display(PrintWriter out)
  { out.print(toString()); } 

  public void displayJava(String target)
  { System.out.println(toStringJava()); } 

  public void displayJava(String target, PrintWriter out)
  { out.println(toStringJava()); } 

  public boolean typeCheck(Vector types, Vector entities, Vector ctxs, Vector env)
  { Attribute att = 
          new Attribute(assignsTo,instanceType,ModelElement.INTERNAL); 

    Type typ = Type.getTypeFor(createsInstanceOf, types, entities); 
    instanceType = typ; 
    if (elementType != null) 
    { instanceType.setElementType(elementType); } 
 
    att.setType(typ); 
    
    if (elementType != null) 
    { att.setElementType(elementType); } 
 
    env.add(att); 
    return true; 
  }  // createsInstanceOf must be a primitive type, String or entity, if Sequence, Set
     // there is not necessarily an element type. Needs be set when the statement is parsed. 

  public Expression wpc(Expression post)
  { return post; }

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities,
                           Vector vars)
  { return toStringJava(); }  

  public String updateFormJava6(java.util.Map env, boolean local)
  { return toStringJava6(); }  

  public String updateFormJava7(java.util.Map env, boolean local)
  { return toStringJava7(); }  

  public String updateFormCSharp(java.util.Map env, boolean local)
  { return toStringCSharp(); }  

  public String updateFormCPP(java.util.Map env, boolean local)
  { return toStringCPP(); }  

  public Vector readFrame()
  { Vector res = new Vector(); 
    // res.add(createsInstanceOf); 
    return res; 
  } 

  public Vector writeFrame()
  { Vector res = new Vector(); 
    res.add(createsInstanceOf); 
    if (assignsTo != null)
    { res.add(assignsTo); } 
    return res; 
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc)
  { return this; } 

  public int syntacticComplexity()
  { return 3; } // depends upon the type really. 

  public int cyclomaticComplexity()
  { return 0; } 

  public int epl()
  { return 1; }  

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector metavariables()
  { Vector res = new Vector(); 
    if (assignsTo != null) 
    { if (assignsTo.startsWith("_"))
      { res.add(assignsTo); } 
    } 
    
    if (instanceType != null) 
    { res.addAll(instanceType.metavariables()); }  
    return res; 
  } 

  public Vector cgparameters()
  { Vector args = new Vector();
    if (assignsTo != null) 
    { args.add(assignsTo); } 
    if (instanceType != null) 
    { args.add(instanceType); } 
    return args; 
  } 


  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    Vector eargs = new Vector(); 
	
    if (assignsTo != null) 
    { args.add(assignsTo); 
      eargs.add(assignsTo); 
    } 
	
    if (instanceType != null) 
    { args.add(instanceType.cg(cgs)); 
      eargs.add(instanceType); 
    }
	 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext;
  } 
}



class SequenceStatement extends Statement
{ private Vector statements = new Vector();

  public SequenceStatement(Vector stats)
  { statements = stats; } 

  public String getOperator() 
  { return ";"; } 

  public Object clone()
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = (Statement) stat.clone(); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public int size()
  { return statements.size(); } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
    if (statements.size() == 0)
    { etext = "skip"; 
	  return ""; 
    }
    else if (statements.size() == 1)
    { Statement st = (Statement) statements.get(0);
      return st.cg(cgs);
    }
    else
    { SequenceStatement tailst = new SequenceStatement();
      Statement st0 = (Statement) statements.get(0);
      Vector newsts = new Vector();
      newsts.addAll(statements);
      newsts.remove(0);
      tailst.statements = newsts;
      args.add(st0.cg(cgs));
      args.add(tailst.cg(cgs));
    }
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { // System.out.println(">>> Sequence rule: " + r + 
      //                    " " + args); 
      String res = r.applyRule(args);
      // System.out.println(">>> Applied sequence rule: " + res);  
	  return res; 
    }
    return etext;
  }

  public Statement dereference(BasicExpression var)
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = (Statement) stat.dereference(var); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = stat.checkConversions(e,propType,propElemType,interp); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public Statement replaceModuleReferences(UseCase uc)
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = stat.replaceModuleReferences(uc); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public Statement generateDesign(java.util.Map env, boolean local)
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = stat.generateDesign(env,local); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public Statement statLC(java.util.Map env, boolean local)
  { Vector newstats = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      Statement newstat = stat.statLC(env,local); 
      newstats.add(newstat); 
    } 
    SequenceStatement res = new SequenceStatement(newstats);
    res.setEntity(entity); 
    res.setBrackets(brackets); 
    return res;  
  } 

  public static Statement statLC(Vector preds, java.util.Map env, boolean local) 
  { if (preds.size() == 0) 
    { return new SequenceStatement(); } 
    else if (preds.size() == 1) 
    { Expression e = (Expression) preds.get(0); 
      return e.statLC(env,local); 
    } 
    else 
    { SequenceStatement sts = new SequenceStatement(); 
      for (int i = 0; i < preds.size(); i++) 
      { Expression p = (Expression) preds.get(i); 
        Statement st = p.statLC(env,local); 
        sts.addStatement(st);
      } 
      return sts; 
    } 
  } 
 

  public SequenceStatement()
  { statements = new Vector(); } 

  public int getSize()
  { return statements.size(); } 

  public void setEntity(Entity e)
  { entity = e;
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i);
      if (stat.entity == null) 
      { stat.setEntity(e); }  
    } 
  }

  public void addStatement(Statement s)
  { if (s != null) 
    { statements.add(s); }
  } 

  public void addStatement(int pos, Statement s)
  { if (pos >= statements.size())
    { statements.add(s); } 
    else
    { statements.add(pos,s); }
  }

  public void addBeforeEnd(Statement s)
  { int sz = statements.size(); 
    if (sz == 0)
    { statements.add(s); } 
    else 
    { statements.add(sz-1,s); } 
  } 

  public Vector getStatements()
  { return statements; } 

  public Statement getStatement(int i) 
  { return (Statement) statements.get(i); } 

  public Statement substituteEq(String oldE, Expression newE)
  { SequenceStatement stats = new SequenceStatement(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = ((Statement) statements.get(i)).substituteEq(oldE,newE);
      stats.addStatement(stat);
    } 
    stats.entity = entity; 
    stats.setBrackets(brackets); 
    return stats;
  } 

  public void display()
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      // if (i > 0) 
      // { 
      System.out.print("  ");
      ss.display();  /* Problem if invocation statements have NL's */
      if (i < statements.size() - 1) 
      { System.out.println(" || "); }
    } 
  }

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("sequencestatement_"); 
    out.println(res + " : SequenceStatement");
    out.println(res + ".statId = \"" + res + "\"");  
    // out.println(res + ".kind = sequence");
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      String ssid = ss.saveModelData(out); 
      out.println(ssid + " : " + res + ".statements"); 
    } 
    return res; 
  } 

  public String bupdateForm()
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      res = res + "  " + ss.bupdateForm(); 
      if (i < statements.size() - 1)
      { res = res + ";\n"; }
    } 
    return res; 
  }

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BParallelStatement res = new BParallelStatement(false); 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      res.addStatement(ss.bupdateForm(env,local)); 
    } 
    return res; 
  }


  public void displayImp(String var)
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      System.out.print("  "); ss.displayImp(var); 
      if (i < statements.size() - 1)
      { System.out.println(";"); }
    } 
  }

  public void displayImp(String var, PrintWriter out)
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      out.print("  "); ss.displayImp(var,out);
      if (i < statements.size() - 1)
      { out.println(";"); }
    }
  }

  public void display(PrintWriter out)
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      out.print("  "); ss.display(out);
      if (i < statements.size() - 1)
      { out.println(" || "); }
    } 
  }    

  public void displayJava(String target)
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { System.out.print("  "); }
      if (ss != null)
      { ss.displayJava(target); } 
      System.out.println(); 
    } 
  }

  public void displayJava(String target, PrintWriter out)
  { for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { out.print("  "); }
      if (ss != null)
      { ss.displayJava(target,out); }
      out.println(); 
    } 
  }

  public String toStringJava()
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.toStringJava(); }
      res = res + "\n"; 
    } 
    // if (brackets)
    // { res = "( " + res + " )"; } 
    return res; 
  }

  public String toEtl()
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.toEtl(); }
      res = res + "\n"; 
    } 
    // if (brackets)
    // { res = "( " + res + " )"; } 
    return res; 
  }

  public String toString()
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i < statements.size() - 1)     
      { res = res + ss + " ; "; }
      else 
      { res = res + ss + " "; } 
    } 
    if (brackets)
    { res = "( " + res + " )"; } 
    return res; 
  }


  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { boolean res = true;  
    // Vector newenv = new Vector(); 
    // newenv.addAll(env); 

    // if (statements.size() > 0 && (statements.get(0) instanceof CreationStatement))
    // { CreationStatement crs = (CreationStatement) statements.get(0); 
    //  // Add crs.assignsTo as new env of type crs.createsInstanceOf
    //  Type typ = Type.getTypeFor(crs.createsInstanceOf,types,entities); 
    //  Attribute param = new Attribute(crs.assignsTo, typ, ModelElement.INTERNAL); 
    //  newenv.add(param); 
    // } 

    for (int i = 0; i < statements.size(); i++) 
    { Statement s = (Statement) statements.get(i); 
      Vector context = new Vector(); 
      Entity ee = s.entity; 
      if (ee != null) 
      { if (cs.size() > 0 && (ee + "").equals(cs.get(0) + "")) { } 
        else 
        { context.add(ee); }
      } 
      context.addAll(cs); 
      res = s.typeCheck(types,entities,context,env);
      // System.err.println("ENV = " + env);  
    } 
    return res; 
  }  

  public Expression wpc(Expression post)
  { Expression e1 = (Expression) post.clone();
    for (int i = statements.size()-1; i >= 0; i--)
    { Statement stat = (Statement) statements.get(i);
      Expression e2 = stat.wpc(e1);
      e1 = e2;
    } 
    return e1; 
  }

  public Vector dataDependents(Vector allvars, Vector vars)
  { Vector vbls = new Vector(); 
    vbls.addAll(vars); 

    for (int i = statements.size() - 1; i >= 0; i--) 
    { Statement stat = (Statement) statements.get(i); 
      Vector v = stat.dataDependents(allvars, vbls); 
      vbls = new Vector(); 
      vbls.addAll(v); 
    } 
    return vbls; 
  }  

  public Vector slice(Vector allvars, Vector vars)
  { Vector vbls = new Vector(); 
    vbls.addAll(vars); 
    Vector deleted = new Vector(); 

    for (int i = statements.size() - 1; i >= 0; i--) 
    { Statement stat = (Statement) statements.get(i); 
      if (stat instanceof SequenceStatement)
      { SequenceStatement stat1 = (SequenceStatement) stat; 
        Vector ss = stat1.slice(allvars,vbls); 
        statements.remove(stat); 
        statements.add(i,new SequenceStatement(ss)); 
      } 
      else if (stat.updates(vbls)) 
      { System.out.println(stat + " updates " + vbls); } // include in slice
      else 
      { deleted.add(stat); 
        System.out.println("Deleting statement: " + stat); 
      } 
      Vector v = stat.dataDependents(allvars, vbls); 
      vbls = new Vector(); 
      vbls.addAll(v); 
    } 
    
    for (int j = 0; j < deleted.size(); j++) 
    { statements.remove(deleted.get(j)); } 
    return statements; 
  } 

  public boolean updates(Vector v) 
  { for (int i = 0; i < statements.size(); i++) 
    { Statement st = (Statement) statements.get(i);
      if (st.updates(v)) { return true; }
    }
    return false; 
 } 

  public Expression toExpression()
  { Expression res = new BasicExpression("skip");
    for (int i = 0; i < statements.size(); i++)
    { Statement st = (Statement) statements.get(i);
      Expression e = st.toExpression();
      if (i > 0)
      { res = new BinaryExpression("&",res,e); }
      else 
      { res = e; }
    }
    return res;
  }

  public void mergeSequenceStatements(Statement s)
  { if (s instanceof SequenceStatement)
    { statements.addAll(((SequenceStatement) s).statements); }
    else 
    { statements.add(s); }
  }

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities,
                           Vector vars)
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.updateForm(env,local,types,entities,vars); }
      res = res + "\n"; 
    } 
    return res; 
  }

  public String deltaUpdateForm(java.util.Map env, boolean local)
  { String res = "";   // interprets A.op(pars) as iteration over A's in _modobjs
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { if (ss instanceof InvocationStatement) 
        { res = res + ((InvocationStatement) ss).deltaUpdateForm(env,local); }
        else if (ss instanceof SequenceStatement) 
        { res = res + ((SequenceStatement) ss).deltaUpdateForm(env,local); } 
        else 
        { res = res + ss.updateForm(env,local,new Vector(), new Vector(), new Vector()); }
      } 
      res = res + "\n"; 
    } 
    return res; 
  }

  public String updateFormJava6(java.util.Map env, boolean local)
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.updateFormJava6(env,local); }
      res = res + "\n"; 
    } 
    return res; 
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.updateFormJava7(env,local); }
      res = res + "\n"; 
    } 
    return res; 
  }

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.updateFormCSharp(env,local); }
      res = res + "\n"; 
    } 
    return res; 
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { String res = ""; 
    for (int i = 0; i < statements.size(); i++)
    { Statement ss = (Statement) statements.elementAt(i);
      if (i > 0)                 /* Hack */ 
      { res = res + "  "; }
      if (ss != null)
      { res = res + ss.updateFormCPP(env,local); }
      res = res + "\n"; 
    } 
    return res; 
  }

  public Vector allPreTerms()
  { Vector res = new Vector();
    for (int i = 0; i < statements.size(); i++) 
    { res.addAll(((Statement) statements.get(i)).allPreTerms()); }  
    return res; 
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    for (int i = 0; i < statements.size(); i++) 
    { res.addAll(((Statement) statements.get(i)).allPreTerms(var)); }  
    return res; 
  }  

  public Vector readFrame()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res.addAll(stat.readFrame()); 
    } 
    return res; 
  } 

  public Vector writeFrame()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res.addAll(stat.writeFrame()); 
    } 
    return res; 
  } 

  public int syntacticComplexity()
  { int res = 0; 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res = res + stat.syntacticComplexity(); 
    } 

    if (res > 0) 
    { res = res + statements.size() - 1; } 
 
    return res; 
  } 

  public int cyclomaticComplexity()
  { int res = 0; 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res = res + stat.cyclomaticComplexity(); 
    } 
    return res; 
  } 

  public int epl()
  { int res = 0; 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res = res + stat.epl(); 
    } 
    return res; 
  } 

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res.addAll(stat.allOperationsUsedIn()); 
    } 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      res.addAll(stat.equivalentsUsedIn()); 
    } 
    return res; 
  } 

  public Vector metavariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++) 
    { Statement stat = (Statement) statements.get(i); 
      { res.addAll(stat.metavariables()); } 
    } 
    
    return res; 
  } 
}


class CaseStatement extends Statement
{ Map cases = new Map();

  // This statement should never arise except from operation statemachines

  public Object clone() { return this; } 

  public String getOperator() 
  { return "case"; } 

  public Statement dereference(BasicExpression var) { return this; } 

  public Statement substituteEq(String oldE, Expression newE)
  { CaseStatement cs = new CaseStatement(); 
    Vector ss = cases.elements; 
    for (int i = 0; i < ss.size(); i++) 
    { Maplet mm = (Maplet) ss.get(i); 
      Statement stat = ((Statement) mm.dest).substituteEq(oldE,newE); 
      Maplet nn = new Maplet(mm.source,stat); 
      cs.addCase(nn); 
    } 
    return cs; 
  } 

  public void addCase(Maplet mm)
  { cases.add_element(mm); }

  public void addCase(Named n, Statement s)
  { Maplet mm = new Maplet(n,s);
    cases.add_element(mm); }

  public Statement getCaseFor(Named nn)
  { Statement res = (Statement) cases.apply(nn);
    return res; }

  public void display()  /* Unused. */ 
  { int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      System.out.println("IF " + ((Named) mm.source).label + " THEN ");
      System.out.print("  "); 
      ((Statement) mm.dest).display(); 
      if (i < n-1) 
      { System.out.println("ELSE"); } } 
    for (int j = 0; j < n; j++)
    { System.out.print("END  "); } 
    System.out.println(" "); 
  } 

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("sequencestatement_"); 
    out.println(res + " : SequenceStatement");
    out.println(res + ".statId = \"" + res + "\"");  
    out.println(res + ".kind = choice");
    for (int i = 0; i < cases.elements.size(); i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      Statement ss = (Statement) mm.dest; 
      String ssid = ss.saveModelData(out); 
      out.println(ssid + " : " + res + ".statements"); 
    } 
    return res; 
  } // and the labels? 

  public String bupdateForm()  /* Unused. */ 
  { int n = cases.elements.size();
    String res = ""; 

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      res = res + "IF " + ((Named) mm.source).label + " THEN ";
      res = res + "  "; 
      res = res + ((Statement) mm.dest).bupdateForm() + "\n"; 
      if (i < n-1) 
      { res = res + "ELSE\n"; } } 
    for (int j = 0; j < n; j++)
    { res = res + "END  "; } 
    res = res + " \n"; 
    return res; 
  } 

  public BStatement bupdateForm(java.util.Map env, boolean local) 
  { return new BBasicStatement("skip"); } // should never be called. 

  public void display(PrintWriter out)   /* Unused */ 
  { int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      out.println("IF " + ((Named) mm.source).label + " THEN ");
      out.print("  ");
      ((Statement) mm.dest).display(out);
      if (i < n-1)
      { System.out.println("ELSE"); } }
    for (int j = 0; j < n; j++)
    { out.print("END  "); }
    out.println(" "); }  

  public void display(String s)
  { int n = cases.elements.size();
    if (n == 0) 
    { System.out.println("  skip"); 
      return; 
    } 

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      System.out.println("  IF " + s + " = " + 
                         ((Named) mm.source).label + " THEN ");
      System.out.print("    ");
      ((Statement) mm.dest).display();  
      if (i < n-1)
      { System.out.println("  ELSE"); } 
    }
     
     /* System.out.print("  "); */ 
    for (int j = 0; j < n; j++)
    { System.out.print("  END"); }
    /* System.out.println(" "); */ 
  }

  public void displayMult(String s)
  { int n = cases.elements.size();
    if (n == 0) 
    { System.out.println("  skip");
      return;
    }

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      System.out.println("  IF " + s + "(oo) = " + 
                         ((Named) mm.source).label + " THEN ");
      System.out.print("    ");
      ((Statement) mm.dest).display();  // add (oo) to calls.
      if (i < n-1)
      { System.out.println("  ELSE"); } 
    }
     
     /* System.out.print("  "); */ 
    for (int j = 0; j < n; j++)
    { System.out.print("  END"); }
    /* System.out.println(" "); */ 
  }

  public void display(String s, PrintWriter out)
  { int n = cases.elements.size();
    if (n == 0)
    { out.println("  skip");
      return;
    }

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      out.println("  IF " + s + " = " +
                         ((Named) mm.source).label + " THEN ");
      out.print("    ");
      ((Statement) mm.dest).display(out);
      if (i < n-1)
      { out.println("  ELSE"); } 
    }
    
     /* System.out.print("  "); */
    for (int j = 0; j < n; j++)
    { out.print("  END"); }
    /* System.out.println(" "); */ 
  }


  public void displayJava(String s)
  /* s is name of actuator/sensor */
  { int n = cases.elements.size();

    // if (n == 0)
    // { System.out.println("  skip");
      // return;
    // }


    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      System.out.println("  if (M" + s + "." + s + " == " + 
                          ((Named) mm.source).label + ")");
       System.out.print("    { ");
       ((Statement) mm.dest).displayJava("M" + s);
       System.out.println("    }"); 
       if (i < n-1)
       { System.out.println("  else {"); } 
    }
  } 
     
  public void displayJava(String s, PrintWriter out)
  /* s is name of actuator/sensor */
  { int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      out.println("  if (M" + s + "." + s + " == " + 
                  ((Named) mm.source).label + ")");
      out.print("    { ");
      ((Statement) mm.dest).displayJava("M" + s, out);
      out.println("    }"); 
      if (i < n-1)
      { out.println("  else {"); } 
    }
  }

  public String toStringJava()
  /* s is name of actuator/sensor */
  { int n = cases.elements.size();
    String res = ""; 
    String s = "s"; 

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      res = res + "  if (M" + s + "." + s + " == " + 
                  ((Named) mm.source).label + ")";
      res = res + "    {\n";
      res = res + ((Statement) mm.dest).toStringJava();
      res = res + "    }\n"; 
      if (i < n-1)
      { res = res + "  else {\n"; } 
    }
    return res; 
  }

  public String toEtl()
  /* s is name of actuator/sensor */
  { int n = cases.elements.size();
    String res = ""; 
    String s = "s"; 

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      res = res + "  if (M" + s + "." + s + " == " + 
                  ((Named) mm.source).label + ")";
      res = res + "    {\n";
      res = res + ((Statement) mm.dest).toEtl();
      res = res + "    }\n"; 
      if (i < n-1)
      { res = res + "  else {\n"; } 
    }
    return res; 
  }

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { return true; }   // type check each case dest? 

  public Expression wpc(Expression post)
  { return post; }  // Will not occur in a transition action. 

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  /* Should never be called: */ 
  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { return toStringJava(); }

  public String updateFormJava6(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormJava7(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormCSharp(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormCPP(java.util.Map env, boolean local)
  { return toStringJava(); }

  public Vector readFrame()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector writeFrame()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc)
  { return this; } 

  public int syntacticComplexity() 
  { int res = 0; 
    int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      Statement cse = (Statement) mm.dest;
      res = res + cse.syntacticComplexity() + 1; 
    }
    return res; 
  }

  public int cyclomaticComplexity() 
  { int res = 0; 
    int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      Statement cse = (Statement) mm.dest;
      res = res + cse.cyclomaticComplexity() + 1; 
    }
    return res; 
  }

  public int epl() 
  { int res = 0; 
    int n = cases.elements.size();

    for (int i = 0; i < n; i++)
    { Maplet mm = (Maplet) cases.elements.elementAt(i);
      Statement cse = (Statement) mm.dest;
      res = res + cse.epl() + 1; 
    }
    return res; 
  }


  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < cases.elements.size(); i++) 
    { Maplet mm = (Maplet) cases.elements.get(i); 
      res.addAll(((Statement) mm.dest).allOperationsUsedIn()); 
    } 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < cases.elements.size(); i++) 
    { Maplet mm = (Maplet) cases.elements.get(i); 
      res.addAll(((Statement) mm.dest).equivalentsUsedIn()); 
    } 
    return res; 
  } 

  public Vector metavariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < cases.elements.size(); i++) 
    { Maplet mm = (Maplet) cases.elements.get(i); 
      res.addAll(((Statement) mm.dest).metavariables()); 
    } 
    return res; 
  } 
}


class ErrorStatement extends Statement
{ public void display()
  { System.out.println("SELECT false THEN skip END"); }

  public String getOperator() 
  { return "error"; } 

  public Object clone() { return this; } 

  public Statement dereference(BasicExpression var) { return this; } 

  public Statement substituteEq(String oldE, Expression newE)
  { // try
    { return (ErrorStatement) clone(); } 
    // catch (CloneNotSupportedException e)
    // { return this; } 
  } 

  public void display(PrintWriter out)
  { out.println("SELECT false THEN skip END"); }

  public String bupdateForm()
  { return "SELECT false THEN skip END\n"; }

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { return new BBasicStatement("SELECT false THEN skip END"); }

  public void displayJava(String t)
  { System.out.println(" {} /* Unreachable state */");
  }

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("errorstatement_"); 
    out.println(res + ".statId = \"" + res + "\"");  
    out.println(res + " : ErrorStatement"); 
    return res; 
  } 

  public String toStringJava()
  { return " {} /* Unreachable state */"; }
  
  public String toEtl()
  { return ""; }

  public void displayJava(String t, PrintWriter out)
  { out.println(" {} /* Unreachable state */"); } 

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { return true; } 
  
  public Expression wpc(Expression post)
  { return post; }

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { return toStringJava(); }

  public String updateFormJava6(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormJava7(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormCSharp(java.util.Map env, boolean local)
  { return toStringJava(); }

  public String updateFormCPP(java.util.Map env, boolean local)
  { return toStringJava(); }

  public Vector readFrame()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector writeFrame()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc)
  { return this; } 

  public int syntacticComplexity()
  { return 1; } 

  public int cyclomaticComplexity()
  { return 0; } 

  public int epl()
  { return 0; } 

  public Vector allOperationsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector equivalentsUsedIn()
  { Vector res = new Vector(); 
    return res; 
  } 

  public Vector metavariables()
  { Vector res = new Vector(); 
    return res; 
  } 
}


class IfStatement extends Statement
{ Vector cases = new Vector();    /* of IfCase */

  public String getOperator() 
  { return "if"; } 

  public IfStatement() { } 

  public IfStatement(Expression test, Statement ifpart, Statement elsepart)
  { IfCase ic1 = new IfCase(test,ifpart); 
    cases.add(ic1); 
    if ("skip".equals(elsepart + "")) { } 
    else 
    { IfCase ic2 = new IfCase(new BasicExpression("true"),elsepart); 
      cases.add(ic2);
    }  
  } 

  public IfStatement(Expression test, Statement ifpart)
  { IfCase ic1 = new IfCase(test,ifpart); 
    cases.add(ic1); 
  } 

  public IfStatement(Statement ifpart, Statement elsepart)
  { if (ifpart instanceof IfStatement)
    { cases.addAll(((IfStatement) ifpart).cases); } 
    else 
    { cases.add(new IfCase(new BasicExpression(true), ifpart)); }  
    cases.add(new IfCase(new BasicExpression(true), elsepart)); 
  } 

  public Object clone() 
  { Vector newcases = new Vector(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase cse = (IfCase) cases.get(i); 
      IfCase newcse = (IfCase) cse.clone(); 
      newcases.add(newcse); 
    } 
    IfStatement res = new IfStatement(); 
    res.cases = newcases; 
    res.setEntity(entity); 
    return res; 
  }  // clone the conditions

  public Statement generateDesign(java.util.Map env, boolean local)
  { Vector newcases = new Vector(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase cse = (IfCase) cases.get(i); 
      IfCase newcse = (IfCase) cse.generateDesign(env,local); 
      newcases.add(newcse); 
    } 
    IfStatement res = new IfStatement(); 
    res.cases = newcases; 
    res.setEntity(entity); 
    return res; 
  }  // clone the conditions

  public Expression getTest()
  { if (cases.size() > 0)
    { IfCase case1 = (IfCase) cases.get(0); 
      return case1.getTest(); 
    } 
    return new BasicExpression(true); 
  } 

  public Statement getIfPart()
  { if (cases.size() > 0)
    { IfCase case1 = (IfCase) cases.get(0); 
      return case1.getIf(); 
    } 
    return null; 
  } 

  public Statement getElsePart()
  { if (cases.size() > 1)
    { IfCase case1 = (IfCase) cases.get(1); 
      return case1.getIf(); 
    } 
    return null; 
  } 

  public void setElse(Statement s)
  { if (cases.size() > 1)
    { IfCase case1 = (IfCase) cases.get(1); 
      case1.setIf(s); 
    } 
  } 
  

  public Statement dereference(BasicExpression var) 
  { Vector newcases = new Vector(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase cse = (IfCase) cases.get(i); 
      IfCase newcse = (IfCase) cse.dereference(var); 
      newcases.add(newcse); 
    } 
    IfStatement res = new IfStatement(); 
    res.cases = newcases; 
    res.setEntity(entity); 
    return res; 
  }  // clone the conditions

  public void setEntity(Entity e)
  { entity = e; 
    for (int i = 0; i < cases.size(); i++)
    { IfCase ic = (IfCase) cases.get(i); 
      ic.setEntity(e); 
    }
  }

  public boolean isEmpty() 
  { return cases.size() == 0; } 

  public void addCase(Expression test, Statement action) 
  { IfCase ic = new IfCase(test,action); 
    cases.add(ic); 
  } 

  public void addCase(IfCase ic)
  { cases.add(ic); }

  public void addCases(IfStatement stat) 
  { cases.addAll(stat.cases); } 

  public Statement substituteEq(String oldE, Expression newE)
  { IfStatement is = new IfStatement(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      IfCase ic2 = ic.substituteEq(oldE,newE); 
      is.addCase(ic2); } 
    return is; 
  } 

  public void display()
  { int n = cases.size();
    if (n == 0) 
    { System.out.println("      skip");
      return; } 
    for (int j = 0; j < n; j++)
    { IfCase ic = (IfCase) cases.elementAt(j);
      System.out.print("    "); 
      ic.display(); 
      if (j < n-1) 
      { System.out.println("    ELSE"); } 
    }

    System.out.print("  "); 
    for (int k = 0; k < n; k++)
    { System.out.print("  END"); }
    System.out.println(""); 
  }

  public String bupdateForm()
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { res = res + "      skip\n";
      return res; 
    } 
    for (int j = 0; j < n; j++)
    { IfCase ic = (IfCase) cases.elementAt(j);
      res = res + "    "; 
      res = res + ic.bupdateForm(); 
      if (j < n-1) 
      { res = res + "    ELSE\n"; } 
    }

    System.out.print("  "); 
    for (int k = 0; k < n; k++)
    { res = res + "  END"; }
    res = res + "\n"; 
    return res;
  }

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { int n = cases.size();
    if (n == 0) 
    { return new BBasicStatement("skip"); }
     
    IfCase ic1 = (IfCase) cases.get(0); 
    Expression test1 = ic1.getTest(); 
    Statement if1 = ic1.getIf(); 
    

    BIfStatement res = new BIfStatement(test1.binvariantForm(env,local), 
                                        if1.bupdateForm(env,local)); 
    BIfStatement bifelse = res; 
    for (int j = 1; j < n; j++) 
    { IfCase ic = (IfCase) cases.get(j); 
      Expression tst = ic.getTest(); 
      Statement ifstat = ic.getIf(); 
      BIfStatement remif = new BIfStatement(tst.binvariantForm(env,local),
                                          ifstat.bupdateForm(env,local)); 
      bifelse.setElse(remif);
      bifelse = remif;  
    }
    return res;  
  }

  public void displayImp(String var)
  { int n = cases.size();
    if (n == 0) 
    { System.out.println("      skip");
      return; } 

    for (int j = 0; j < n; j++)
    { IfCase ic = (IfCase) cases.elementAt(j);
      System.out.print("    "); 
      ic.displayImp(var); 
      if (j < n-1) 
      { System.out.println("    ELSE"); } }

    System.out.print("  "); 
    for (int k = 0; k < n; k++)
    { System.out.print("  END"); }
      System.out.println(""); }

  public void displayImp(String var, PrintWriter out)
  { int n = cases.size();
    if (n == 0)
    { out.println("      skip");
      return; }

    for (int j = 0; j < n; j++)
    { IfCase ic = (IfCase) cases.elementAt(j);
      out.print("    ");
      ic.displayImp(var,out);
      if (j < n-1)
      { out.println("    ELSE"); } }

    out.print("  ");
    for (int k = 0; k < n; k++)
    { out.print("  END"); }
    out.println(""); 
  }


  public void display(PrintWriter out)
  { int n = cases.size();
    if (n == 0) 
    { out.println("      skip");
      return; } 

    for (int j = 0; j < n; j++)
    { IfCase ic = (IfCase) cases.elementAt(j);
      out.print("    "); 
      ic.display(out); 
      if (j < n-1) 
      { out.println("    ELSE"); } }

    out.print("  "); 
    for (int k = 0; k < n; k++)
    { out.print("  END"); }
      out.println(""); 
  }

  public String saveModelData(PrintWriter out)
  { Statement cs = convertToConditionalStatement(); 
    return cs.saveModelData(out); 
  } 

  /* String res = Identifier.nextIdentifier("conditionalstatement_"); 
    out.println(res + " : ConditionalStatement");
    out.println(res + ".statId = \"" + res + "\"");  

    if (cases.size() > 0) 
    { IfCase case1 = (IfCase) cases.elementAt(0); 
      Expression test = case1.getTest(); 
      String testid = test.saveModelData(out); 
      out.println(res + ".test = " + testid); 
      Statement ifPart = case1.getIf(); 
      String ifpartid = ifPart.saveModelData(out); 
      out.println(res + ".ifPart = " + ifpartid); 
    } 

    if (cases.size() > 1) 
    { IfCase case2 = (IfCase) cases.get(1); 
      Statement elsePart = case2.getIf(); 
      String elsepartid = elsePart.saveModelData(out); 
      out.println(res + ".elsePart = " + elsepartid); 
    } // not quite right

    return res; 
  } */

   public void displayJava(String target)
   { int n = cases.size();
     if (n == 0) 
     { return; } 

     for (int j = 0; j < n; j++)
     { IfCase ic = (IfCase) cases.elementAt(j);
       System.out.print("    "); 
       ic.displayJava(target); 
       if (j < n-1) 
       { System.out.println("    else"); } }
   } 

   public void displayJava(String target, PrintWriter out)
   { int n = cases.size();
     if (n == 0) 
     { return; } 

     for (int j = 0; j < n; j++)
     { IfCase ic = (IfCase) cases.elementAt(j);
       out.print("    "); 
       ic.displayJava(target, out); 
       if (j < n-1) 
       { out.println("    else"); } 
     }
   } 

   public String toStringJava()
   { String res = ""; 
     int n = cases.size();
     if (n == 0) 
     { return res; } 

     for (int j = 0; j < n; j++)
     { IfCase ic = (IfCase) cases.elementAt(j);
       res = res + "    "; 
       res = res + ic.toStringJava(); 
       if (j < n-1) 
       { res = res + "    else\n"; } 
     }
     return res; 
   } 

   public String toEtl()
   { String res = ""; 
     int n = cases.size();
     if (n == 0) 
     { return res; } 

     for (int j = 0; j < n; j++)
     { IfCase ic = (IfCase) cases.elementAt(j);
       Expression test = ic.getTest();
       Statement stat = ic.getIf();
       if ("true".equals(test + ""))
       { res = res + stat; }
       else 
       { res = res + "  if (" + test + ") { " + stat.toEtl() + " }\n";
         if (j < n-1)
         { res = res + "  else "; }
       }       
     }
     return res; 
   } 

   public String toString()
   { int n = cases.size();
     String res = "";
     for (int i = 0; i < n; i++)
     { IfCase ic = (IfCase) cases.get(i);
       Expression test = ic.getTest();
       Statement stat = ic.getIf();
       if ("true".equals(test + ""))
       { res = res + stat; }
       else 
       { res = res + "  if " + test + " then " + stat;
         if (i < n-1)
         { res = res + " else "; }
       }
     }
     return res;
   }

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { boolean res = true;
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res = ic.typeCheck(types,entities,cs,env) && res; 
    }
    return res;
  }

  public Expression wpc(Expression post)
  { Expression res = null;
    for (int i = 0; i < cases.size(); i++)
    { IfCase ic = (IfCase) cases.get(i);
      Expression test = ic.getTest();
      Statement ifS = ic.getIf();
      Expression e1 = ifS.wpc(post);
      Expression disj =
        new BinaryExpression("&",test,e1);
      if (res == null)
      { res = disj; }
      else
      { res = new BinaryExpression("or",res,disj); }
    }
    return res;
  }

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { return false; } 

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { return res; } 

    if (n == 1)
    { IfCase ic0 = (IfCase) cases.get(0);
      res = "   " + ic0.updateForm(env,local,types,entities,vars); 
      return res; 
    } 
    else if (n == 2) 
    { IfCase ic0 = (IfCase) cases.get(0);
      IfCase ic1 = (IfCase) cases.get(1);
      res = "   " + ic0.updateForm(env,local,types,entities,vars); 
      if ("true".equals(ic1.getTest()))
      { Statement ep = ic1.getIf(); 
        res = res + "    else { " + ep.updateForm(env,local,types,entities,vars) + " }"; 
      } 
      else 
      { res = res + " else " + ic1.updateForm(env,local,types,entities,vars); }  
      return res; 
    } 
    else
    { for (int j = 0; j < n; j++)
      { IfCase ic = (IfCase) cases.elementAt(j);
        res = res + "    "; 
        res = res + ic.updateForm(env,local,types,entities,vars); 
        if (j < n-1) 
        { IfCase next = (IfCase) cases.get(j+1); 
          if (next.isNull()) { } 
          else 
          { res = res + "    else\n"; } 
        }
      }
      return res;
    }  
  } 

  public String updateFormJava6(java.util.Map env, boolean local)
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { return res; } 

    if (n == 1)
    { IfCase ic0 = (IfCase) cases.get(0);
      res = "   " + ic0.updateFormJava6(env,local); 
      return res; 
    } 
    else if (n == 2) 
    { IfCase ic0 = (IfCase) cases.get(0);
      IfCase ic1 = (IfCase) cases.get(1);
      res = "   " + ic0.updateFormJava6(env,local); 
      if ("true".equals(ic1.getTest()))
      { Statement ep = ic1.getIf(); 
        res = res + "    else { " + ep.updateFormJava6(env,local) + " }"; 
      } 
      else 
      { res = res + " else " + ic1.updateFormJava6(env,local); }  
      return res; 
    } 
    else
    { for (int j = 0; j < n; j++)
      { IfCase ic = (IfCase) cases.elementAt(j);
        res = res + "    "; 
        res = res + ic.updateFormJava6(env,local); 
        if (j < n-1) 
        { IfCase next = (IfCase) cases.get(j+1); 
          if (next.isNull()) { } 
          else 
          { res = res + "    else\n"; } 
        }
      }
      return res;
    }  
  } 

  public String updateFormJava7(java.util.Map env, boolean local)
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { return res; } 

    if (n == 1)
    { IfCase ic0 = (IfCase) cases.get(0);
      res = "   " + ic0.updateFormJava7(env,local); 
      return res; 
    } 
    else if (n == 2) 
    { IfCase ic0 = (IfCase) cases.get(0);
      IfCase ic1 = (IfCase) cases.get(1);
      res = "   " + ic0.updateFormJava7(env,local); 
      if ("true".equals(ic1.getTest()))
      { Statement ep = ic1.getIf(); 
        res = res + "    else { " + ep.updateFormJava7(env,local) + " }"; 
      } 
      else 
      { res = res + " else " + ic1.updateFormJava7(env,local); }  
      return res; 
    } 
    else
    { for (int j = 0; j < n; j++)
      { IfCase ic = (IfCase) cases.elementAt(j);
        res = res + "    "; 
        res = res + ic.updateFormJava7(env,local); 
        if (j < n-1) 
        { IfCase next = (IfCase) cases.get(j+1); 
          if (next.isNull()) { } 
          else 
          { res = res + "    else\n"; } 
        }
      }
      return res;
    }  
  } 

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { return res; } 

    if (n == 1)
    { IfCase ic0 = (IfCase) cases.get(0);
      res = "   " + ic0.updateFormCSharp(env,local); 
      return res; 
    } 
    else if (n == 2) 
    { IfCase ic0 = (IfCase) cases.get(0);
      IfCase ic1 = (IfCase) cases.get(1);
      res = "   " + ic0.updateFormCSharp(env,local); 
      if ("true".equals(ic1.getTest()))
      { Statement ep = ic1.getIf(); 
        res = res + "    else { " + ep.updateFormCSharp(env,local) + " }"; 
      } 
      else 
      { res = res + " else " + ic1.updateFormCSharp(env,local); }  
      return res; 
    } 
    else
    { for (int j = 0; j < n; j++)
      { IfCase ic = (IfCase) cases.elementAt(j);
        res = res + "    "; 
        res = res + ic.updateFormCSharp(env,local); 
        if (j < n-1) 
        { IfCase next = (IfCase) cases.get(j+1); 
          if (next.isNull()) { } 
          else 
          { res = res + "    else\n"; } 
        }
      }
      return res;
    }  
  } 

  public String updateFormCPP(java.util.Map env, boolean local)
  { String res = ""; 
    int n = cases.size();
    if (n == 0) 
    { return res; } 

    if (n == 1)
    { IfCase ic0 = (IfCase) cases.get(0);
      res = "   " + ic0.updateFormCPP(env,local); 
      return res; 
    } 
    else if (n == 2) 
    { IfCase ic0 = (IfCase) cases.get(0);
      IfCase ic1 = (IfCase) cases.get(1);
      res = "   " + ic0.updateFormCPP(env,local); 
      if ("true".equals(ic1.getTest()))
      { Statement ep = ic1.getIf(); 
        res = res + "    else { " + ep.updateFormCPP(env,local) + " }"; 
      } 
      else 
      { res = res + " else " + ic1.updateFormCPP(env,local); }  
      return res; 
    } 
    else
    { for (int j = 0; j < n; j++)
      { IfCase ic = (IfCase) cases.elementAt(j);
        res = res + "    "; 
        res = res + ic.updateFormCPP(env,local); 
        if (j < n-1) 
        { IfCase next = (IfCase) cases.get(j+1); 
          if (next.isNull()) { } 
          else 
          { res = res + "    else\n"; } 
        }
      }
      return res;
    }  
  } 


  public Vector allPreTerms()
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.allPreTerms());
    } 
    return res;  
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.allPreTerms(var));
    } 
    return res;  
  }  

  public Vector readFrame()
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.readFrame());
    } 
    return res;  
  }  

  public Vector writeFrame()
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.writeFrame());
    } 
    return res;  
  }  

  public Statement checkConversions(Entity e, Type propType, Type propElemType, java.util.Map interp)
  { return this; } 

  public Statement replaceModuleReferences(UseCase uc) 
  { Vector newcases = new Vector(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase cse = (IfCase) cases.get(i); 
      IfCase newcse = (IfCase) cse.replaceModuleReferences(uc); 
      newcases.add(newcse); 
    } 
    IfStatement res = new IfStatement(); 
    res.cases = newcases; 
    res.setEntity(entity); 
    return res; 
  }  // clone the conditions

  public Statement convertToConditionalStatement()
  { int n = cases.size();
    if (n == 0) { return null; }
    return convert2Conditional(cases);
  }

  private static Statement convert2Conditional(Vector cases)
  { if (cases.size() == 1)
    { IfCase ic = (IfCase) cases.get(0);
      Expression test = ic.getTest();
      Statement stat = ic.getIf();
      if ("true".equals(test + ""))
      { return stat; }
      else 
      { return new ConditionalStatement(test, stat); }
    }
    else 
    { IfCase ic = (IfCase) cases.get(0);
      Expression test = ic.getTest();
      Statement stat = ic.getIf();

      Vector tail = new Vector();
      tail.addAll(cases);
      tail.remove(0);
      Statement tailstat = convert2Conditional(tail);
      return new ConditionalStatement(test,stat,tailstat);
    } 
  }

  public int syntacticComplexity()
  { int res = 0;
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res = res + ic.syntacticComplexity() + 1;
    } 
    return res;  
  }  

  public int cyclomaticComplexity()
  { int res = 0;
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res = res + ic.cyclomaticComplexity();
    } 
    return res;  
  }  

  public int epl()
  { int res = 0; 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res = res + ic.epl();
    } 
    return res; 
  }

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.allOperationsUsedIn());
    } 
    return res;  
  }  

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.equivalentsUsedIn());
    } 
    return res;  
  }  

  public Vector metavariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < cases.size(); i++) 
    { IfCase ic = (IfCase) cases.get(i); 
      res.addAll(ic.metavariables());
    } 
    return res; 
  } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();

    if (cases.size() > 0) 
    { IfCase ic1 = (IfCase) cases.get(0); 
      Expression test1 = ic1.getTest(); 
      if ("true".equals(test1 + ""))
      { Statement stat1 = ic1.getIf(); 
        return stat1.cg(cgs); 
      } 
    } 

    if (cases.size() > 0) 
    { IfCase ic1 = (IfCase) cases.get(0); 
      args.add(ic1.getTest().cg(cgs));
      args.add(ic1.getIf().cg(cgs)); 
    } 
    
    if (cases.size() > 1) // if then else
    { IfCase ic2 = (IfCase) cases.get(1); 
      args.add(ic2.getIf().cg(cgs));
    } 
    else  // if then
    { args.add(""); }
	
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  }
}


class AssignStatement extends Statement 
{ private Type type = null;  // for declarations 
  private Expression lhs;
  private Expression rhs;
  private boolean copyValue = false; 
  private String operator = ":=";  // default

  public AssignStatement(Expression left, Expression right)
  { lhs = left;
    rhs = right; 
  }

  public AssignStatement(Attribute left, Expression right)
  { lhs = new BasicExpression(left);
    rhs = right; 
  }

  public AssignStatement(Binding b) 
  { lhs = new BasicExpression(b.getPropertyName()); 
    rhs = b.expression; 
  } 

  public AssignStatement(String left, Expression right) 
  { lhs = new BasicExpression(left); 
    rhs = right; 
  } 

  public String getOperator() 
  { return ":="; } 

  public Expression getLeft()
  { return lhs; } 


  public void setType(Type t)
  { type = t; } 

  public void setElementType(Type t)
  { lhs.elementType = t; 
    // rhs.elementType = t; 
  } 

  public Vector cgparameters()
  { Vector args = new Vector();
    args.add(lhs);
    args.add(rhs);
    return args; 
  } 


  public String basiccg(CGSpec cgs)
  { // assumes type == null 
    String etext = this + "";
    Vector args = new Vector();
    args.add(lhs.cg(cgs));
    Vector eargs = new Vector(); 
    eargs.add(lhs); 
    Expression rhsnopre = rhs.removePrestate(); 
    args.add(rhsnopre.cg(cgs));
    eargs.add(rhsnopre);
 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext; 
  }
  
  
  
  public String cg(CGSpec cgs)
  { if (type != null) 
    { // process as  var lhs : type ; lhs := rhs; 
      SequenceStatement stat = new SequenceStatement(); 
      CreationStatement cre = new CreationStatement(type + "", lhs + "");
      cre.setType(type); 
      cre.setElementType(lhs.elementType);  
      AssignStatement newas = new AssignStatement(lhs,rhs);
	  newas.type = null;  
      stat.addStatement(cre); 
      stat.addStatement(newas); 
      return stat.cg(cgs); 
    } 

    String etext = this + "";
    Vector args = new Vector();
    args.add(lhs.cg(cgs));
    Vector eargs = new Vector(); 
    eargs.add(lhs); 
    Expression rhsnopre = rhs.removePrestate(); 
    args.add(rhsnopre.cg(cgs));
    eargs.add(rhsnopre);
 
    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return etext;
  }

  public void setCopyValue(boolean b)
  { copyValue = b; } 

  public void setOperator(String op)
  { operator = op; } 

  public Object clone()
  { Expression newlhs = (Expression) lhs.clone(); 
    Expression newrhs = (Expression) rhs.clone(); 
    AssignStatement res = new AssignStatement(newlhs,newrhs); 
    res.setType(type); 
    res.setCopyValue(copyValue); 
    return res; 
  } 

  public Statement dereference(BasicExpression var)
  { Expression newlhs = (Expression) lhs.dereference(var); 
    Expression newrhs = (Expression) rhs.dereference(var); 
    AssignStatement res = new AssignStatement(newlhs,newrhs); 
    res.setType(type); 
    res.setCopyValue(copyValue); 
    return res; 
  } 

  public Statement substituteEq(String oldE, Expression newE)
  { Expression lhs2 = (Expression) lhs.clone(); 
        /* lhs.substituteEq(oldE,newE); */ 
    Expression rhs2 = rhs.substituteEq(oldE,newE); 
    AssignStatement res = new AssignStatement(lhs2,rhs2); 
    res.setType(type); 
    res.setCopyValue(copyValue); 
    return res; 
  } 

  public String toString() 
  { if (type == null) 
    { return lhs + " " + operator + " " + rhs + " "; }
    else 
    { return lhs + " : " + type + " := " + rhs + " "; } 
  }  

  public String saveModelData(PrintWriter out) 
  { String res = Identifier.nextIdentifier("assignstatement_"); 
    out.println(res + " : AssignStatement"); 
    out.println(res + ".statId = \"" + res + "\""); 
    String lhsid = lhs.saveModelData(out); 
    String rhsid = rhs.saveModelData(out); 
    out.println(res + ".left = " + lhsid); 
    out.println(res + ".right = " + rhsid); 
    if (type != null) 
    { String typeid = type.getUMLModelName(out); 
      out.println(typeid + " : " + res + ".type"); 
    } 
    return res; 
  } 

  public void display()
  { if (type == null) 
    { System.out.println(lhs + " := " + rhs + " "); }
    else 
    { System.out.println(lhs + " : " + type + " := " + rhs + " "); } 
  } 

  public String bupdateForm()
  { return lhs + " := " + rhs; }

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression brhs = rhs.binvariantForm(env,local); 
    BStatement stat = ((BasicExpression) lhs).bEqupdateForm(env,brhs,local); 
    return stat; 
  } // ignores type

  public void displayImp(String var) 
  { System.out.print(lhs + "_STO_VAR(" + rhs + ")"); }

  public void displayImp(String var, PrintWriter out)
  { out.print(lhs + "_STO_VAR(" + rhs + ")"); }

  public void display(PrintWriter out)
  { out.print(lhs + " := " + rhs); }

  public void displayJava(String target)
  { if (type != null) 
    { System.out.print("  " + type.getJava() + " "); } 
    System.out.print(lhs + " = " + rhs + ";  " + 
                     "System.out.println(\"" + lhs + " set to " + rhs + "\");");
  }

  public void displayJava(String target, PrintWriter out)
  { if (type != null) 
    { out.print("  " + type.getJava() + " "); } 
    out.print(lhs + " = " + rhs + ";  " + 
              "System.out.println(\"" + lhs + " set to " + 
              rhs + "\");"); 
  }

  public String toStringJava()
  { java.util.Map env = new java.util.HashMap(); 
    if (entity != null) 
    { env.put(entity.getName(),"this"); } 
    String res = (new BinaryExpression("=",lhs,rhs)).updateForm(env,true);  
    if (type != null) 
    { res = "  " + type.getJava() + " " + res; } 
    
    return res; 
  }

  public String toEtl()
  { String res = lhs + " = " + rhs + ";";  
    return res; 
  }

  // public String toString()
  // { return lhs + " := " + rhs + " "; } 

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { // Also recognise the type as an entity or enumeration if it exists
    boolean res = lhs.typeCheck(types,entities,cs,env); 
    res = rhs.typeCheck(types,entities,cs,env);
    if (lhs.type == null && rhs.type != null) 
    { lhs.type = rhs.type; } 
    if (rhs.elementType != null && lhs.elementType == null) 
    { lhs.elementType = rhs.elementType; } 
    else if (lhs.elementType != null && rhs.elementType == null) 
    { rhs.elementType = lhs.elementType; } 

    if (type != null)  // declare it
    { Attribute att = new Attribute(lhs + "", rhs.type, ModelElement.INTERNAL); 
      att.setElementType(lhs.elementType); 
      env.add(att); 
    } 

    return res; 
  }

  public Expression wpc(Expression post)
  { return post.substituteEq(lhs.toString(),rhs); }

  public Vector dataDependents(Vector allvars, Vector vars)
  { if (vars.contains(lhs.toString()))
    { // remove this variable and add all vars of rhs to vars
      vars.remove(lhs.toString()); 
      Vector es = rhs.variablesUsedIn(allvars); 
      for (int i = 0; i < es.size(); i++) 
      { String var = (String) es.get(i); 
        if (vars.contains(var)) { } 
        else 
        { vars.add(var); } 
      } 
    } 
    return vars; 
  }  

  public boolean updates(Vector v) 
  { if (v.contains(lhs.toString()))
    { return true; }
    return false; 
  }  // contains(lhs.data) ???

  public Vector slice(Vector allvars, Vector vars)
  { Vector res = new Vector(); 
    if (vars.contains(lhs.toString()))  // lhs.data
    { res.add(this); } 
    else 
    { System.out.println("Deleting statement: " + this); } 
    return res; 
  }  

  public Expression toExpression()
  { return new BinaryExpression("=",lhs,rhs); }

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities,
                           Vector vars)
  { // if (entity != null) 
    // { env.put(entity.getName(),"this"); } 
    if (copyValue && type != null && type.isCollectionType())
    { String res = "  " + type.getJava() + " " + lhs + " = new Vector();\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryForm(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && lhs.getType().isCollectionType())
    { String res = "  " + lhs + " = new Vector();\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryForm(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 

    // if lhs has a target entity element type, and rhs has a source entity element type
    // do a type conversion  lhs := TRef[rhs.$id]
    Type letype = lhs.getElementType(); 
    Type retype = rhs.getElementType(); 
    if (letype != null && retype != null && letype.isEntity() && retype.isEntity())
    { Entity srcent = retype.getEntity(); 
      Entity trgent = letype.getEntity(); 
      if (srcent.isSourceEntity() && trgent.isTargetEntity())
      { BasicExpression fid = new BasicExpression("$id");
        fid.setType(new Type("String",null));
        fid.setUmlKind(Expression.ATTRIBUTE);
        fid.setEntity(srcent);
        fid.setObjectRef(rhs); 

        BasicExpression felem = new BasicExpression(trgent.getName());
        felem.setUmlKind(Expression.CLASSID);
        felem.setEntity(trgent);
        felem.setArrayIndex(fid);
        felem.setType(letype);
        felem.setElementType(letype);

        BinaryExpression feq = new BinaryExpression("=", lhs, felem);
        String fres = feq.updateForm(env,local);
        if (type != null) 
        { fres = "  " + type.getJava() + " " + fres; }
        return fres;  
      } 
    } 


    String res = (new BinaryExpression("=",lhs,rhs)).updateForm(env,local);  
    if (type != null) 
    { res = "  " + type.getJava() + " " + res; } 
    
    return res; 
  } 

  public String updateFormJava6(java.util.Map env, boolean local)
  { // if (entity != null) 
    // { env.put(entity.getName(),"this"); } 
    if (copyValue && type != null && type.isCollectionType())
    { String res = "  " + type.getJava6() + " " + lhs + " = " + type.initialValueJava6() + ";\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryFormJava6(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && lhs.getType().isCollectionType())
    { String res = "  " + lhs + " = " + lhs.getType().initialValueJava6() + ";\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryFormJava6(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 

    String res = (new BinaryExpression("=",lhs,rhs)).updateFormJava6(env,local);  
    if (type != null) 
    { res = "  " + type.getJava6() + " " + res; } 
    
    return res; 
  } 

  public String updateFormJava7(java.util.Map env, boolean local)
  { // if (entity != null) 
    // { env.put(entity.getName(),"this"); } 
    if (copyValue && type != null && type.isCollectionType())
    { String res = "  " + type.getJava7(lhs.elementType) + " " + lhs + " = " + type.initialValueJava7() + ";\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryFormJava7(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && lhs.getType().isCollectionType())
    { String res = "  " + lhs + " = " + lhs.getType().initialValueJava7() + ";\n"; 
      res = res + "  " + lhs + ".addAll(" + rhs.queryFormJava7(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 

    String res = (new BinaryExpression("=",lhs,rhs)).updateFormJava7(env,local);  
    if (type != null) 
    { res = "  " + type.getJava7(lhs.elementType) + " " + res; } 
    
    return res; 
  } 

  public String updateFormCSharp(java.util.Map env, boolean local)
  { // if (entity != null) 
    // { env.put(entity.getName(),"this"); } 
    if (copyValue && type != null && type.isCollectionType())
    { String res = "  " + type.getCSharp() + " " + lhs + " = new ArrayList();\n"; 
      res = res + "  " + lhs + ".AddRange(" + rhs.queryFormCSharp(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && lhs.getType().isCollectionType())
    { String res = "  " + lhs + " = new ArrayList();\n"; 
      res = res + "  " + lhs + ".AddRange(" + rhs.queryFormCSharp(env,local) + ");\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 

    String res = (new BinaryExpression("=",lhs,rhs)).updateFormCSharp(env,local);  
    if (type != null) 
    { res = "  " + type.getCSharp() + " " + res; } 
    
    return res; 
  } 

  public String updateFormCPP(java.util.Map env, boolean local)
  { // if (entity != null) 
    // { env.put(entity.getName(),"this"); } 

    if (copyValue && type != null && Type.isSequenceType(type))
    { String elemt = rhs.getElementType().getCPP(); 
      String res = "  vector<" + elemt + ">* " + lhs + " = new vector<" + elemt + ">();\n"; 
      String rqf = rhs.queryFormCPP(env,local); 
      res = res + "  " + lhs + "->insert(" + lhs + "->end(), " + rqf + "->begin(), " + 
                                         rqf + "->end());\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && type != null && Type.isSetType(type))
    { String elemt = rhs.getElementType().getCPP(); 
      String res = "  set<" + elemt + ">* " + lhs + " = new set<" + elemt + ">();\n"; 
      String rqf = rhs.queryFormCPP(env,local); 
      res = res + "  " + lhs + "->insert(" + rqf + "->begin(), " + rqf + "->end());\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && Type.isSequenceType(lhs.getType()))
    { String elemt = rhs.getElementType().getCPP(); 
      String rqf = rhs.queryFormCPP(env,local); 
      String res = "  " + lhs + " = new vector<" + elemt + ">();\n"; 
      res = res + "  " + lhs + "->insert(" + lhs + "->end(), " + rqf + "->begin(), " + 
                                         rqf + "->end());\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 
    else if (copyValue && lhs.getType() != null && Type.isSetType(lhs.getType()))
    { String elemt = rhs.getElementType().getCPP(); 
      String rqf = rhs.queryFormCPP(env,local); 
      String res = "  " + lhs + " = new set<" + elemt + ">();\n"; 
      res = res + "  " + lhs + "->insert(" + rqf + "->begin(), " + 
                                         rqf + "->end());\n"; 
      return res; 
    } // For type.isEntityType() or strings, clone the rhs. 



    String res = (new BinaryExpression("=",lhs,rhs)).updateFormCPP(env,local);  
    if (type != null) 
    { res = "  " + type.getCPP(rhs.getElementType()) + " " + res; } 
    
    return res; 
  } 

  public Vector allPreTerms()
  { Vector res = rhs.allPreTerms();
    return res;  
  }  

  public Vector allPreTerms(String var)
  { Vector res = rhs.allPreTerms(var);
    return res;  
  }  

  public Vector readFrame()
  { Vector res = new Vector();
    res.addAll(rhs.allReadFrame());  
    return res;  
  }  

  public Vector writeFrame()
  { Vector res = new Vector();
    if (lhs instanceof BasicExpression) 
    { String frame = ((BasicExpression) lhs).data; 
      Entity e = lhs.getEntity(); 
      if (e != null) 
      { frame = e.getName() + "::" + frame; } 
      res.add(frame); 
    } 
    // res.add(lhs + "");  // lhs.data if a BasicExpression
    return res;  
  }  

  public Statement checkConversions(Entity e, Type _propType, Type _propElemType, java.util.Map interp)
  { if (lhs instanceof BasicExpression)
    { BasicExpression belhs = (BasicExpression) lhs; 
      // Type propType = lhs.getType(); 
      // Type propElemType = lhs.getElementType();
      String propertyName = belhs.getData(); 
      Type propType = e.getDefinedFeatureType(propertyName); 
      Type propElemType = e.getDefinedFeatureElementType(propertyName); 
      // System.out.println("CONVERTING " + rhs + " TO TYPE " + propType + " " + propElemType); 

      Expression newrhs = rhs.checkConversions(propType,propElemType,interp); 
      AssignStatement res = new AssignStatement(lhs,newrhs); 
      res.setType(type); 
      res.setCopyValue(copyValue); 
      return res; 
    } 
    else  
    { return this; }
  }  

  public Statement replaceModuleReferences(UseCase uc)
  { if (lhs instanceof BasicExpression)
    { BasicExpression belhs = (BasicExpression) lhs; 

      Expression newlhs = lhs.replaceModuleReferences(uc); 
      Expression newrhs = rhs.replaceModuleReferences(uc); 
      AssignStatement res = new AssignStatement(newlhs,newrhs); 
      res.setType(type); 
      res.setCopyValue(copyValue); 
      return res; 
    } 
    else  
    { return this; }
  }  

  public int syntacticComplexity()
  { return lhs.syntacticComplexity() + rhs.syntacticComplexity() + 1; } 

  public int cyclomaticComplexity()
  { return 0; } 

  public int epl()
  { return 0; }  // although a typed assignment should be 1

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    res.addAll(rhs.allOperationsUsedIn());  
    return res;  
  }  

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    res.addAll(rhs.equivalentsUsedIn());  
    return res;  
  }  

  public Vector metavariables()
  { Vector res = lhs.metavariables();
    res.addAll(rhs.metavariables());  
    return res;  
  }  
}


class IfCase
{ private Expression test; 
  private Statement ifPart;
  private Entity entity;

  IfCase(Expression t, Statement i)
  { test = t; 
    ifPart = i; 
  }

  public Object clone()
  { Expression newtest = (Expression) test.clone(); 
    Statement newif = (Statement) ifPart.clone(); 
    IfCase res = new IfCase(newtest,newif); 
    res.setEntity(entity); 
    return res; 
  }  

  public IfCase dereference(BasicExpression var)
  { Expression newtest = (Expression) test.dereference(var); 
    Statement newif = (Statement) ifPart.dereference(var); 
    IfCase res = new IfCase(newtest,newif); 
    res.setEntity(entity); 
    return res; 
  }  

  public IfCase generateDesign(java.util.Map env, boolean local)
  { Statement newif = ifPart.generateDesign(env,local); 
    IfCase res = new IfCase(test,newif); 
    res.setEntity(entity); 
    return res; 
  }  

  public boolean isNull()
  { return "true".equals(test + "") && "skip".equals(ifPart + ""); } 

  public Expression getTest() 
  { return test; } 

  public Statement getIf()
  { return ifPart; } 

  public void setIf(Statement s)
  { ifPart = s; } 

  public void setEntity(Entity e)
  { entity = e; 
    ifPart.setEntity(e);   // surely?? 
  }

  public IfCase substituteEq(String oldE, Expression newE) 
  { Expression e = test.substituteEq(oldE,newE); 
    Statement stat = ifPart.substituteEq(oldE,newE);
    IfCase res = new IfCase(e,stat);
    res.setEntity(entity); 
    return res; 
  } 

  public void display()
  { System.out.print("IF " + test + " THEN "); 
    ifPart.display();
    System.out.println(""); 
  }

  public String bupdateForm()
  { String res = "IF " + test + " THEN "; 
    res = res + ifPart.bupdateForm();
    return res + "\n"; 
  }

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression btest = test.binvariantForm(env,local); 
    BStatement bif = ifPart.bupdateForm(env,local); 
    return new BIfStatement(btest,bif); 
  } 

  public void displayImp(String var) 
  { System.out.print("IF " + test + " THEN "); 
    ifPart.displayImp(var); 
    System.out.println(""); 
  }

  public void displayImp(String var, PrintWriter out)
  { out.print("IF " + test + " THEN ");
    ifPart.displayImp(var,out);
    out.println(""); 
  }
 
  public void display(PrintWriter out)
  { out.print("IF " + test + " THEN ");
    ifPart.display(out);
    out.println(""); 
  }

  public void displayJava(String t)
  { System.out.print("if (" + test.toJava() + 
                       ") { "); 
    ifPart.displayJava(t); 
    System.out.println(" }"); 
  }

  public String toStringJava()
  { java.util.Map env = new java.util.HashMap();
    if (entity != null)
    { env.put(entity.getName(),"this"); } 
    String res = "if (" + test.queryForm(env,false) + 
                       ") { "; 
    res = res + ifPart.toStringJava(); 
    return res + " }\n";
  }

  public String toEtl()
  { String res = "if (" + test + ") { "; 
    res = res + ifPart.toEtl(); 
    return res + " }\n";
  }

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { boolean res1 = test.typeCheck(types,entities,cs,env);
    boolean res2 = ifPart.typeCheck(types,entities,cs,env);
    return res1 && res2; 
  }


  public void displayJava(String t, PrintWriter out)
  { out.print("if (" + test.toJava() + 
                       ") { "); 
    ifPart.displayJava(t, out); 
    out.println(" }"); 
  }

  public String updateForm(java.util.Map env, boolean local, Vector types, Vector entities, 
                           Vector vars)
  { // if (entity != null)
    // { env.put(entity.getName(),"this"); } 

    if ("true".equals("" + test))
    { return ifPart.updateForm(env,local,types,entities,vars); } 

    String res = "if (" + test.queryForm(env,false) + 
                       ") { "; 
    res = res + ifPart.updateForm(env,local,types,entities,vars); 
    return res + " }\n";
  }  // (env,local) in both places. 

  public String updateFormJava6(java.util.Map env, boolean local)
  { // if (entity != null)
    // { env.put(entity.getName(),"this"); } 

    if ("true".equals("" + test))
    { return ifPart.updateFormJava6(env,local); } 

    String res = "if (" + test.queryFormJava6(env,false) + 
                       ") { "; 
    res = res + ifPart.updateFormJava6(env,local); 
    return res + " }\n";
  }

  public String updateFormJava7(java.util.Map env, boolean local)
  { // if (entity != null)
    // { env.put(entity.getName(),"this"); } 

    if ("true".equals("" + test))
    { return ifPart.updateFormJava7(env,local); } 

    String res = "if (" + test.queryFormJava7(env,false) + 
                       ") { "; 
    res = res + ifPart.updateFormJava7(env,local); 
    return res + " }\n";
  }


  public String updateFormCSharp(java.util.Map env, boolean local)
  { // if (entity != null)
    // { env.put(entity.getName(),"this"); } 

    if ("true".equals("" + test))
    { return ifPart.updateFormCSharp(env,local); } 

    String res = "if (" + test.queryFormCSharp(env,false) + 
                       ") { "; 
    res = res + ifPart.updateFormCSharp(env,local); 
    return res + " }\n";
  }

  public String updateFormCPP(java.util.Map env, boolean local)
  { // if (entity != null)
    // { env.put(entity.getName(),"this"); } 

    if ("true".equals("" + test))
    { return ifPart.updateFormCPP(env,local); } 

    String res = "if (" + test.queryFormCPP(env,false) + 
                       ") { "; 
    res = res + ifPart.updateFormCPP(env,local); 
    return res + " }\n";
  }

  public Vector allPreTerms()
  { Vector res1 = test.allPreTerms(); 
    return VectorUtil.union(res1,ifPart.allPreTerms()); 
  }  

  public Vector allPreTerms(String var)
  { Vector res1 = test.allPreTerms(var); 
    return VectorUtil.union(res1,ifPart.allPreTerms(var)); 
  }  

  public Vector readFrame()
  { Vector res = new Vector();
    res.addAll(test.allReadFrame()); 
    res.addAll(ifPart.readFrame()); 
    return res;  
  }  

  public Vector writeFrame()
  { Vector res = new Vector();
    res.addAll(ifPart.writeFrame()); 
    return res;  
  }  

  public IfCase replaceModuleReferences(UseCase uc) 
  { Expression e = test.replaceModuleReferences(uc); 
    Statement stat = ifPart.replaceModuleReferences(uc);
    IfCase res = new IfCase(e,stat);
    res.setEntity(entity); 
    return res; 
  } 

  public int syntacticComplexity()
  { int res = test.syntacticComplexity(); 
    res = res + ifPart.syntacticComplexity();
    return res + 1; 
  }

  public int cyclomaticComplexity()
  { int res = test.cyclomaticComplexity(); 
    res = res + ifPart.cyclomaticComplexity();
    return res; 
  }

  public int epl()
  { int res = 0; 
    res = res + ifPart.epl();
    return res; 
  }

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    res.addAll(test.allOperationsUsedIn()); 
    res.addAll(ifPart.allOperationsUsedIn()); 
    return res;  
  }  

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    res.addAll(test.equivalentsUsedIn()); 
    res.addAll(ifPart.equivalentsUsedIn()); 
    return res;  
  }  

  public Vector metavariables()
  { Vector res = test.metavariables();
    res.addAll(ifPart.metavariables());  
    return res;  
  }  
} 


class ConditionalStatement extends Statement
{ Expression test;
  Statement ifPart;
  Statement elsePart;

  ConditionalStatement(Expression e, Statement s)
  { test = e;
    ifPart = s;
    elsePart = null;
  }

  ConditionalStatement(Expression e, Statement s1, Statement s2)
  { test = e;
    ifPart = s1;
    elsePart = s2;
  }

  public void setElse(Statement stat) 
  { elsePart = stat; } 

  public String getOperator() 
  { return "if"; } 

  public Expression getTest()
  { return test; } 

  public String cg(CGSpec cgs)
  { String etext = this + "";
    Vector args = new Vector();
	
	if ("true".equals(test + ""))
	{ return ifPart.cg(cgs); }
	
    args.add(test.cg(cgs));
    args.add(ifPart.cg(cgs));
    if (elsePart == null) 
	{ elsePart = new SequenceStatement(); } 
	args.add(elsePart.cg(cgs));

    CGRule r = cgs.matchedStatementRule(this,etext);
    if (r != null)
    { return r.applyRule(args); }
    return etext;
  }

  public Object clone()
  { Expression testc = (Expression) test.clone(); 
    Statement ifc = (Statement) ifPart.clone(); 
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = (Statement) elsePart.clone(); }
    return new ConditionalStatement(testc, ifc, elsec); 
  }  

  public Statement generateDesign(java.util.Map env, boolean local)
  { Statement ifc = ifPart.generateDesign(env,local);
    if ("true".equals(test + ""))
	{ return ifc; } 
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = elsePart.generateDesign(env,local); }
    return new ConditionalStatement(test, ifc, elsec); 
  }  

  public String toString()
  { String res = "if " + test + " then " + ifPart;
    if (elsePart != null)
    { res = res + " else " + elsePart; }
    return res;
  }

  public String toStringJava()
  { String res = "if (" + test + ") { " + ifPart + " } ";
    if (elsePart != null)
    { res = res + " else { " + elsePart + " }"; }
    return res;
  }

  public String toEtl()
  { String res = "  if (" + test + ") { " + ifPart.toEtl() + " }\n";
    if (elsePart != null)
    { res = res + "  else { " + elsePart.toEtl() + " }"; }
    return res;
  }

  public void display(java.io.PrintWriter out)
  { String res = "if " + test + " then " + ifPart;
    if (elsePart != null)
    { res = res + " else " + elsePart; }
    out.println(res);
  }

  public void display()
  { String res = "if " + test + " then " + ifPart;
    if (elsePart != null)
    { res = res + " else " + elsePart; }
    System.out.println(res);
  }

  public void displayJava(String v, java.io.PrintWriter out)
  { out.println("    if (" + test + ")"); 
    out.println("    { " + ifPart + " }");
    if (elsePart != null)
    { out.println("    else "); 
      out.println("    { " + elsePart + " }"); 
    }
  }

  public void displayJava(String v)
  { String res = "if (" + test + ") { " + ifPart + " }";
    if (elsePart != null)
    { res = res + " else { " + elsePart + " }"; }
    System.out.println(res);
  }

  public String saveModelData(PrintWriter out)
  { String res = Identifier.nextIdentifier("conditionalstatement_");
    out.println(res + " : ConditionalStatement");
    out.println(res + ".statId = \"" + res + "\"");
    String testid = test.saveModelData(out);
    out.println(res + ".test = " + testid);
    String ifpartid = ifPart.saveModelData(out);
    out.println(res + ".ifPart = " + ifpartid);
    if (elsePart != null)
    { String elsepartid = elsePart.saveModelData(out);
      out.println(elsepartid + " : " + res + ".elsePart");
    }
    return res;
  }

  public Statement dereference(BasicExpression v)
  { Expression testc = test.dereference(v); 
    Statement ifc = ifPart.dereference(v); 
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = elsePart.dereference(v); }
    return new ConditionalStatement(testc, ifc, elsec); 
  }  

  public Statement substituteEq(String oldE, Expression newE)
  { Expression testc = test.substituteEq(oldE, newE); 
    Statement ifc = ifPart.substituteEq(oldE, newE); 
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = elsePart.substituteEq(oldE, newE); }
    return new ConditionalStatement(testc, ifc, elsec); 
  }  

  public boolean typeCheck(Vector types, Vector entities, Vector cs, Vector env)
  { boolean res = test.typeCheck(types,entities,cs,env); 
    res = ifPart.typeCheck(types,entities,cs,env);
    if (elsePart != null) 
    { res = elsePart.typeCheck(types, entities, cs, env); } 
    return res; 
  }

  public Expression wpc(Expression post)
  { BinaryExpression ifimpl = new BinaryExpression("=>", test, ifPart.wpc(post));
    if (elsePart != null) 
	{ UnaryExpression ntest = new UnaryExpression("not", test); 
	  BinaryExpression elseimpl = new BinaryExpression("=>", ntest, elsePart.wpc(post)); 
	  return new BinaryExpression("&", ifimpl, elseimpl); 
    }
	return ifimpl; 
  }  
  // and else if present

  public Vector dataDependents(Vector allvars, Vector vars)
  { return vars; }  

  public boolean updates(Vector v) 
  { if (ifPart.updates(v))
    { return true; }
    else if (elsePart != null && elsePart.updates(v))
    { return true; } 
    return false; 
  }  // contains(lhs.data) ???


  public String updateForm(java.util.Map env, boolean local, Vector types,
                           Vector entities, Vector vars)
  { if ("true".equals(test + ""))
    { return "    { " + ifPart.updateForm(env,local,types,entities,vars) + " }\n"; } 
	
	String res = "    if (" + test.queryForm(env,local) + ")\n";
    res = res +  "    { " + ifPart.updateForm(env,local,types,entities,vars) + " }\n";
    if (elsePart != null)
    { res = res + "    else { " + elsePart.updateForm(env,local,types,entities,vars) + " }\n"; }
    return res;
  } 

  public String updateFormJava6(java.util.Map env, boolean local)
  { if ("true".equals(test + ""))
    { return "    { " + ifPart.updateFormJava6(env,local) + " }\n"; } 
	
	String res = "if (" + test.queryFormJava6(env,local) + ")\n";
    res = res + "{ " + ifPart.updateFormJava6(env,local) + " }\n";
    if (elsePart != null)
    { res = res + "else { " + elsePart.updateFormJava6(env,local) + " }\n"; }
    return res;
  } 

  public String updateFormJava7(java.util.Map env, boolean local)
  { if ("true".equals(test + ""))
    { return "    { " + ifPart.updateFormJava7(env,local) + " }\n"; } 
	
	String res = "if (" + test.queryFormJava7(env,local) + ")\n";
    res = res + "{ " + ifPart.updateFormJava7(env,local) + " }\n";
    if (elsePart != null)
    { res = res + "else { " + elsePart.updateFormJava7(env,local) + " }\n"; }
    return res;
  } 

  public String updateFormCSharp(java.util.Map env, boolean local)
  { if ("true".equals(test + ""))
    { return "    { " + ifPart.updateFormCSharp(env,local) + " }\n"; } 
	
	String res = "if (" + test.queryFormCSharp(env,local) + ")\n";
    res = res + "{ " + ifPart.updateFormCSharp(env,local) + " }\n";
    if (elsePart != null)
    { res = res + "else { " + elsePart.updateFormCSharp(env,local) + " }\n"; }
    return res;
  } 

  public String updateFormCPP(java.util.Map env, boolean local)
  { if ("true".equals(test + ""))
    { return "    { " + ifPart.updateFormCPP(env,local) + " }\n"; } 
	
	String res = "if (" + test.queryFormCPP(env,local) + ")\n";
    res = res + "{ " + ifPart.updateFormCPP(env,local) + " }\n";
    if (elsePart != null)
    { res = res + "else { " + elsePart.updateFormCPP(env,local) + " }\n"; }
    return res;
  } 

  public BStatement bupdateForm(java.util.Map env, boolean local)
  { BExpression cond = test.binvariantForm(env,local); 
    BStatement ifstat = ifPart.bupdateForm(env,local);
    if (elsePart != null)
    { return new BIfStatement(cond,ifstat,
                     elsePart.bupdateForm(env,local)); 
    } 
    else 
    { return new BIfStatement(cond,ifstat); } 
  } 

  public String bupdateForm()
  { BExpression cond = test.bqueryForm(); 
    String ifstat = ifPart.bupdateForm();
    if (elsePart != null)
    { return "IF " + cond + " THEN " + ifstat + " ELSE " + elsePart.bupdateForm() + " END"; } 
    else 
    { return "IF " + cond + " THEN " + ifstat + " END"; } 
  } 

  public Vector allPreTerms()
  { Vector res = new Vector();
    res.addAll(test.allPreTerms()); 
    res.addAll(ifPart.allPreTerms()); 
    if (elsePart != null) 
    { res.addAll(elsePart.allPreTerms()); }  
    return res;  
  }  

  public Vector allPreTerms(String var)
  { Vector res = new Vector();
    res.addAll(test.allPreTerms(var)); 
    res.addAll(ifPart.allPreTerms(var)); 
    if (elsePart != null) 
    { res.addAll(elsePart.allPreTerms(var)); }  
    return res;  
  }  

  public Vector readFrame()
  { Vector res = new Vector();
    res.addAll(test.allReadFrame()); 
    res.addAll(ifPart.readFrame()); 
    if (elsePart != null) 
    { res.addAll(elsePart.readFrame()); } 
    return res;  
  }  

  public Vector writeFrame()
  { Vector res = new Vector();
    res.addAll(ifPart.writeFrame()); 
    if (elsePart != null) 
    { res.addAll(elsePart.writeFrame()); } 
    return res;  
  }  

  public Statement checkConversions(Entity e, Type propType, Type propElemType, 
                                    java.util.Map interp)
  { Statement ifc = ifPart.checkConversions(e,propType,propElemType,interp); 
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = elsePart.checkConversions(e,propType,propElemType,interp); }
    return new ConditionalStatement(test, ifc, elsec); 
  }  

  public Statement replaceModuleReferences(UseCase uc)
  { Statement ifc = ifPart.replaceModuleReferences(uc);
    Expression tt = test.replaceModuleReferences(uc);  
    Statement elsec = null; 
    if (elsePart != null) 
    { elsec = elsePart.replaceModuleReferences(uc); }
    return new ConditionalStatement(tt, ifc, elsec); 
  }  


  public int syntacticComplexity()
  { int res = test.syntacticComplexity();
    res = res + ifPart.syntacticComplexity(); 
    if (elsePart != null)
    { res = res + elsePart.syntacticComplexity(); }
    return res + 1;
  }

  public int cyclomaticComplexity()
  { int res = test.cyclomaticComplexity(); 
    res = res + ifPart.cyclomaticComplexity();
    if (elsePart != null) 
    { res = res + elsePart.cyclomaticComplexity(); } 
    return res; 
  }

  public int epl()
  { int res = 0; 
    res = res + ifPart.epl();
    if (elsePart != null) 
    { res = res + elsePart.epl(); } 
    return res; 
  }

  public Vector allOperationsUsedIn()
  { Vector res = new Vector();
    res.addAll(test.allOperationsUsedIn()); 
    res.addAll(ifPart.allOperationsUsedIn()); 
    if (elsePart != null) 
    { res.addAll(elsePart.allOperationsUsedIn()); } 
    return res;  
  }  

  public Vector equivalentsUsedIn()
  { Vector res = new Vector();
    res.addAll(test.equivalentsUsedIn()); 
    res.addAll(ifPart.equivalentsUsedIn()); 
    if (elsePart != null) 
    { res.addAll(elsePart.equivalentsUsedIn()); } 
    return res;  
  }  

  public Vector metavariables()
  { Vector res = test.metavariables();
    res.addAll(ifPart.metavariables());  
    if (elsePart != null) 
    { res.addAll(elsePart.metavariables()); }   
    return res;  
  }  
}

