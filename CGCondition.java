/******************************
* Copyright (c) 2003--2022 Kevin Lano
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

    if (expr instanceof BinaryExpression) 
    { BinaryExpression ee = (BinaryExpression) expr; 

      Vector vars = ee.metavariables(); 
      if (vars.size() > 0) 
      { var = (String) vars.get(0); } 
      else if (category.equals("OclStatement"))
      { var = "_4"; } 
      else 
      { var = "_1"; } 
 
      if ("/=".equals(ee.getOperator()))
      { pos = false; } 

      stereo = ee.getRight() + ""; 
    }

    if (rulevars.contains(var))
    { CGCondition res = new CGCondition(stereo,var);
      res.setPositive(pos); 
      return res; 
    } 
    return null;  
  } 

  public void setVariable(String v) 
  { variable = v; } 

  public void setVariableMetafeature(String mf) 
  { variable = variable + "`" + mf; } 

  public void setStereotypeMetafeature(String mf) 
  { stereotype = stereotype + "`" + mf; } 

  public void setStereotype(String st) 
  { stereotype = st; } 

  public void setPositive(boolean pos)
  { positive = pos; }

  public void setPositive()
  { positive = true; }

  public void setNegative()
  { positive = false; }

  public String toString()
  { String res = variable; 
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

  public void applyAction(Vector vars, Vector eargs, Vector reps)
  { // vv stereo
    // means ast.setStereotype(stereorep)
    // where ast is the eargs element of vars variable vv
    // stereorep is stereo with each var 
    // replaced by corresponding rep

    String stereo = new String(stereotype); 
    for (int x = 0; x < reps.size() && x < vars.size(); x++)
    { String var = (String) vars.get(x);
      String arg1 = (String) reps.get(x); 
      stereo = stereo.replace(var,arg1);
    }

    int ind = vars.indexOf(variable); 
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
        if (positive) 
        { ast.addStereotype(stereo); } 
        else 
        { ast.removeStereotype(stereo); } 
      } 
    

      System.out.println(">>> Executed action " + obj + " (" + positive + ") " + stereo);  
      JOptionPane.showMessageDialog(null, 
         "Executed action " + obj + " (" + positive + ") " + stereo,   "",
         JOptionPane.INFORMATION_MESSAGE);
    }  
  } 

  public static boolean conditionsSatisfied(
              Vector conditions, Vector args, 
              Vector entities, CGSpec cgs) 
  { boolean res = true; 
    for (int i = 0; i < args.size(); i++) 
    { Object m = args.get(i); 
      String var = "_" + (i+1);
 
      for (int j = 0; j < conditions.size(); j++) 
      { CGCondition cond = (CGCondition) conditions.get(j); 
        if (cond.variable != null) 
        { int mfindex = cond.variable.indexOf("`"); 
          String cvar = cond.variable; 
          if (mfindex > 0) 
          { cvar = cvar.substring(0,mfindex); } 
          if (var.equals(cvar))
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
                   cond.conditionSatisfied((ASTTerm) m, entities,cgs))
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

  public boolean stereotypeConditionSatisfied(ModelElement m, Vector entities, CGSpec cgs)
  { if (m.hasStereotype(stereotype))
    { return positive; }

    if (!m.hasStereotype(stereotype))
    { return !positive; }

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

  public boolean conditionSatisfied(Vector v, Vector entities, CGSpec cgs)
  { if ("empty".equals(stereotype.toLowerCase()) && 
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

  public boolean conditionSatisfied(Expression e, Vector entities, CGSpec cgs)
  { Type t = e.getType();
    Type et = e.getElementType(); 

    int kind = e.getUMLKind(); 

    String edata = e + ""; 

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
      { return ("Set".equals(tname) || "Sequence".equals(tname)); }
      else
      { return !("Set".equals(tname)) && !("Sequence".equals(tname)); }
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

  public boolean conditionSatisfied(ASTTerm a, Vector entities, CGSpec cgs)
  { String alit = a.literalForm(); 

    Vector metafs = CGRule.metafeatures(variable); 

    System.out.println("*** Metafeatures of " + variable + " are: " + metafs); 

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

      if (repl != null && repl.equals(stereotype))
      { return positive; } 
      else 
      { return !positive; } 
    } 
      
    // Also the stereotype could have one. 

    if (a instanceof ASTCompositeTerm)
    { ASTCompositeTerm ac = (ASTCompositeTerm) a; 
      
      if (ac.tag.equalsIgnoreCase(stereotype))
      { return positive; }

      if ("multiple".equals(stereotype) || 
          "singleton".equals(stereotype) ||
          "1ary".equals(stereotype) ||
          "2ary".equals(stereotype) ||
          "3ary".equals(stereotype) ||
          "4ary".equals(stereotype) ||
          "5ary".equals(stereotype))
      { return conditionSatisfied(ac.getTerms(), entities, cgs); }
    } 
    else if (a instanceof ASTBasicTerm)
    { ASTBasicTerm ac = (ASTBasicTerm) a; 
      
      if (ac.tag.equalsIgnoreCase(stereotype))
      { return positive; }

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

    if ("Class".equals(stereotype))
    { if (Type.isOclEntityType(alit,entities) || 
          a.hasStereotype("Class")) 
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
    
