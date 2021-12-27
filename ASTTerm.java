/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 
import java.io.*; 

public abstract class ASTTerm
{ String id = ""; 

  // A term represents one of the following UML/OCL things:

  
  Expression expression = null; 
  Statement statement = null; 
  ModelElement modelElement = null; 
  Vector modelElements = null;   
  Vector expressions = null; // for parameter/argument lists

  static String packageName = null; 
  static Vector enumtypes; 
  static Vector entities; 
  static Entity currentClass = null; // Current context class

  java.util.Map metafeatures = new java.util.HashMap(); 
     // String --> String, eg., recording the conceptual
     // type of the element. 

  static java.util.Map types = new java.util.HashMap(); 
     // String --> String for general type of identifiers
     // valid at the scope of the current term. 

  public abstract String toString(); 

  public abstract String getTag(); 

  public abstract String literalForm();

  public abstract ASTTerm removeOuterTag(); 

  public abstract ASTTerm getTerm(int i); 

  public abstract Vector tokenSequence(); 

  public abstract Vector symbolTerms(); 

  public abstract Vector nonSymbolTerms(); 

  public abstract String toJSON(); 

  public abstract String asTextModel(PrintWriter out); 

  public abstract String cg(CGSpec cgs); 

  public abstract String cgRules(CGSpec cgs, Vector rules); 

  // Only for programming languages. 
  public abstract boolean updatesObject(ASTTerm t); 

  public abstract boolean callSideEffect(); 

  // Only for programming languages. 
  public abstract boolean hasSideEffect(); 

  public abstract boolean isIdentifier(); 

  public abstract String preSideEffect(); 

  public abstract String postSideEffect(); 

  public boolean hasMetafeature(String f) 
  { String val = (String) metafeatures.get(f); 
    return val != null; 
  } 

  public void setMetafeature(String f, String val) 
  { metafeatures.put(f,val); } 

  public String getMetafeatureValue(String f) 
  { String val = (String) metafeatures.get(f); 
    return val;  
  } 


  public static void setType(ASTTerm t, String val) 
  { String f = t.literalForm(); 
    types.put(f,val); 
  } 

  public static void setType(String f, String val) 
  { types.put(f,val); } 

  public static String getType(String f) 
  { String val = (String) types.get(f); 
    return val;  
  } 

  public static String getType(ASTTerm t) 
  { String val = (String) types.get(t.literalForm());
    if (val == null && t instanceof ASTBasicTerm) 
    { ASTBasicTerm bt = (ASTBasicTerm) t; 
      return bt.getType(); 
    } 
    return val;  
  }

  public static String getElementType(ASTTerm t) 
  { String val = ASTTerm.getType(t);
    if (val != null)
    { Type typ = Type.getTypeFor(val, ASTTerm.enumtypes, ASTTerm.entities); 
      if (typ != null && typ.elementType != null) 
      { return typ.elementType + ""; } 
    } 
    return "OclAny";  
  }


  public boolean hasType(String str)
  { if ("integer".equalsIgnoreCase(str))
    { return isInteger(); } 
    if ("real".equalsIgnoreCase(str))
    { return isReal(); } 
    if ("boolean".equalsIgnoreCase(str))
    { return isBoolean(); } 

    if ("Sequence".equals(str))
    { return isSequence(); } 
    if ("StringSequence".equals(str))
    { return isStringSequence(); } 
    if ("IntegerSequence".equals(str))
    { return isIntegerSequence(); }
    if ("RealSequence".equals(str))
    { return isRealSequence(); }
    if ("BooleanSequence".equals(str))
    { return isBooleanSequence(); }
 
    if ("Set".equals(str))
    { return isSet(); } 
    if ("Map".equals(str))
    { return isMap(); } 
    if ("Function".equals(str))
    { return isFunction(); } 
    if ("Collection".equals(str))
    { return isCollection(); } 
    if ("File".equals(str))
    { return isFile(); } 
    if ("Date".equals(str))
    { return isDate(); } 
    if ("Process".equals(str))
    { return isProcess(); } 

    String typ = ASTTerm.getType(this);
    if (typ == null) 
    { return false; } 
 
    return typ.equals(str); 
  }  


  public abstract Type cdeclarationToType(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract ModelElement cdeclaratorToModelElement(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Vector cparameterListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Attribute cparameterToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Statement cstatementToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Statement cupdateForm(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Vector cstatementListToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Vector cexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Expression cexpressionToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 
  

  public abstract String queryForm(); 

  public abstract String toKM3(); 

  public boolean isAssignment() 
  { return false; } 

  public String toKM3Assignment()
  { return toKM3(); } 

  public static boolean isInteger(String typ) 
  { return 
      "int".equals(typ) || "short".equals(typ) || 
      "byte".equals(typ) || "Integer".equals(typ) || 
      "Short".equals(typ) || "Byte".equals(typ) ||
      "BigInteger".equals(typ) || "integer".equals(typ) ||  
      "long".equals(typ) || "Long".equals(typ); 
  } 

  public static boolean isReal(String typ) 
  { return 
      "float".equals(typ) || "double".equals(typ) || 
      "BigDecimal".equals(typ) || "real".equals(typ) || 
      "Float".equals(typ) || "Double".equals(typ); 
  } 

  public static boolean isString(String typ) 
  { return 
      "String".equals(typ) || "Character".equals(typ) || 
      "StringBuffer".equals(typ) || "char".equals(typ) || 
      "StringBuilder".equals(typ); 
  } 

  public static boolean isBoolean(String typ) 
  { return 
      "boolean".equals(typ) || "Boolean".equals(typ); 
  } 

  public boolean isString() 
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    return 
      "String".equals(typ) || "Character".equals(typ) || 
      "StringBuffer".equals(typ) || "char".equals(typ) || 
      "StringBuilder".equals(typ); 
  } 

  public boolean isInteger() 
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    return ASTTerm.isInteger(typ); 
  } 

  public boolean isReal() 
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    return ASTTerm.isReal(typ); 
  } 

  public boolean isNumber() 
  { String typ = ASTTerm.getType(literalForm()); 
    return ASTTerm.isReal(typ) || ASTTerm.isInteger(typ); 
  } 

  public boolean isBoolean() 
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    return 
      "boolean".equals(typ) || "Boolean".equals(typ); 
  } 

  public boolean isCollection()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Sequence".equals(typ) || "Set".equals(typ) ||
        typ.startsWith("Sequence(") || typ.startsWith("Set("))
    { return true; } 
    return false; 
  } 

  public boolean isSet()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Set".equals(typ) || typ.startsWith("Set("))
    { return true; } 
    return false; 
  } 

  public boolean isSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Sequence".equals(typ) || typ.startsWith("Sequence("))
    { return true; } 
    return false; 
  } 

  public boolean isMap()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Map".equals(typ) || typ.startsWith("Map("))
    { return true; } 
    return false; 
  } 

  public boolean isFunction()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Function".equals(typ) || typ.startsWith("Function("))
    { return true; } 
    return false; 
  } 

  public boolean isDate()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclDate".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isFile()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclFile".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isOclFile()
  { return isFile(); } 

  public boolean isProcess()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclProcess".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isStringSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(String)"))
    { return true; } 
    return false; 
  } 

  public boolean isIntegerSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(int)"))
    { return true; } 
    return false; 
  } 

  public boolean isRealSequence()
  { return isDoubleSequence(); } 

  public boolean isDoubleSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(double)"))
    { return true; } 
    return false; 
  } 

  public boolean isBooleanSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(boolean)"))
    { return true; } 
    return false; 
  } 

  public boolean isBitSet()
  { return isBooleanSequence(); } 
  // and original jtype is "BitSet". 

  public String getDefaultValue(String typ) 
  { if (isInteger(typ))
    { return "0"; } 
    if (isReal(typ))
    { return "0.0"; } 
    if (isString(typ))
    { return "\"\""; } 
    if (isBoolean(typ))
    { return "false"; } 
    
    return "null"; 
  } 

  public Expression getExpression()
  { return expression; } 

  public Statement getStatement()
  { return statement; } 

  public Entity getEntity()
  { if (modelElement != null && modelElement instanceof Entity)
    { return (Entity) modelElement; }
    return null; 
  }  

  public abstract boolean hasTag(String tagx); 

  public abstract boolean hasSingleTerm(); 

  public abstract int arity(); 

  public abstract int nonSymbolArity(); 

  public static boolean constantTrees(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    ASTTerm t0 = trees[0]; 
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
	  if (tx == null) 
	  { return false; }
      if (tx.equals(t0)) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean allSymbolTerms(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx instanceof ASTSymbolTerm) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean allNestedSymbolTerms(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 
      if (tx.arity() == 1) 
      { ASTTerm ttx = tx.getTerm(0); 

        if (ttx instanceof ASTSymbolTerm) { } 
        else 
        { return false; }
      } 
      else 
      { return false; }  
    } 
    return true; 
  }   


  public static boolean functionalSymbolMapping(ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence is functional.
    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { sattvalues[i] = strees[i].literalForm(); } 

    for (int i = 0; i < ttrees.length; i++) 
    { tattvalues[i] = ttrees[i].literalForm(); } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 

  public static boolean functionalNestedSymbolMapping(ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence of strees single element & 
    // target terms is functional.

    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { sattvalues[i] = strees[i].literalForm(); } 

    for (int i = 0; i < ttrees.length; i++) 
    { tattvalues[i] = ttrees[i].literalForm(); } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 

  public static TypeMatching createNewFunctionalMapping(String name, ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence is functional.
    Type str = new Type("String", null); 
    TypeMatching tm = new TypeMatching(str,str);
    tm.setName(name);  

    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { sattvalues[i] = strees[i].literalForm(); } 

    for (int i = 0; i < ttrees.length; i++) 
    { tattvalues[i] = ttrees[i].literalForm(); } 

    tm.setStringValues(sattvalues,tattvalues); 
    return tm; 
  } 

  public static boolean functionalTermMapping(Vector strees, Vector ttrees)
  { // The correspondence is functional.
    String[] sattvalues = new String[strees.size()]; 
    String[] tattvalues = new String[ttrees.size()]; 

    for (int i = 0; i < strees.size(); i++) 
    { ASTTerm st = (ASTTerm) strees.get(i); 
      if (st == null) { return false; } 
      sattvalues[i] = st.literalForm(); 
    } 

    for (int i = 0; i < ttrees.size(); i++) 
    { ASTTerm tt = (ASTTerm) ttrees.get(i); 
      if (tt == null) { return false; }
      tattvalues[i] = tt.literalForm(); 
    } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 


  public static String commonTag(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return null; }
    ASTTerm t0 = trees[0]; 
    if (t0 == null) { return null; }
	
    if (t0 instanceof ASTBasicTerm) 
    { return ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { return ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return null; } 
  }

  public static boolean sameTag(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0]; 
    if (t0 == null) { return false; }
	
    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx.hasTag(tag0)) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTails(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    Vector tail0 = new Vector(); 
    ASTTerm t0 = trees[0]; 

    if (t0 instanceof ASTCompositeTerm) 
    { Vector trms = new Vector(); 
      trms.addAll(((ASTCompositeTerm) t0).getTerms());
      if (trms.size() == 0) 
      { return false; } 
      trms.remove(0); 
      tail0.addAll(trms);  
    } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx instanceof ASTCompositeTerm) 
      { Vector trms = new Vector(); 
        trms.addAll(((ASTCompositeTerm) tx).getTerms());
        if (trms.size() == 0) 
        { return false; } 
        trms.remove(0); 
        if (tail0.equals(trms)) { } 
        else 
        { return false; } 
      } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTagSingleArgument(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0]; 
    if (t0 == null) 
    { return false;} 

    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm && 
             t0.hasSingleTerm()) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 
      else if (tx.hasTag(tag0) && tx.hasSingleTerm()) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTagSameArity(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0];
    if (t0 == null) 
    { return false;} 
 
    int arit = t0.arity(); 

    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 
      else if (tx.hasTag(tag0) && tx.arity() == arit) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static ASTTerm[] removeOuterTag(ASTTerm[] trees, Vector remd)
  { if (trees == null || trees.length == 0) 
    { return trees; }

    ASTTerm[] result = new ASTTerm[trees.length]; 

    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i];
      if (tx == null) 
      { remd.add(null); } 
      else  
      { ASTTerm tnew = tx.removeOuterTag(); 
        result[i] = tnew; 
        remd.add(tnew); 
      } 
    } 
    return result; 
  }   

  public static ASTTerm[] subterms(ASTTerm[] trees, int i, Vector remd)
  { if (trees == null || trees.length == 0) 
    { return trees; }

    ASTTerm[] result = new ASTTerm[trees.length]; 

    for (int j = 0; j < trees.length; j++) 
    { ASTTerm tx = trees[j]; 
      if (tx == null)
      { result[j] = null; 
        remd.add(null); 
      }
      else 
      { ASTTerm tnew = tx.getTerm(i); 
        result[j] = tnew; 
        remd.add(tnew);
      }  
    } // terms indexed from 0

    return result; 
  }   

 public static boolean equalTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i]? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 
          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

 /*   public static boolean matchingTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i], or corresponding under mod? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 
          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else if (mod.correspondingTrees(xx,yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    } */ 

    public static boolean matchingTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i], or corresponding under mod? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 
          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else if (mod.correspondingTrees(sent, xx, yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean matchingTrees(Entity sent, ASTTerm xx, ASTTerm yy, ModelSpecification mod)
    { System.out.println(">>>> Comparing " + xx + " to " + yy); 
      if (xx == null) 
      { return false; } 
      else if (xx.equals(yy)) 
      { return true; } 
      else if (mod.correspondingTrees(sent, xx, yy)) 
      { return true; } 
      return false; 
    }

 /* 
    
  public static boolean singletonTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]') for the same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTSymbolTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTSymbolTerm) xx).getSymbol();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(xx,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

  public static boolean singletonTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]') for the same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTSymbolTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTSymbolTerm) xx).getSymbol();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(sent, xx, ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }


    public static boolean sameArityTrees(ASTTerm[] xs, ASTTerm[] ys)
    { // Is each ys[i].terms.size() == xs[i].terms.size()? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          int n = xx.arity(); 
          int m = yvect.arity(); 
          if (n != m) 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

    public static boolean sameNonSymbolArity(ASTTerm[] xs, ASTTerm[] ys)
    { // Is number of non-symbols in ys[i].terms == 
      // number of non-symbols in xs[i].terms? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          int n = xx.nonSymbolArity();
          System.out.println(">***> Non-symbol arity of " + xx + " = " + n); 
 
          int m = yvect.nonSymbolArity(); 
          System.out.println(">***> Non-symbol arity of " + yvect + " = " + m); 

          if (n != m) 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

/* 
    public static boolean embeddedTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]' ..terms..) for same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTCompositeTerm && 
              ((ASTCompositeTerm) yvect).getTerms().size() > 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(xx,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

    public static boolean embeddedTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]' ..terms..) for same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTCompositeTerm && 
              ((ASTCompositeTerm) yvect).getTerms().size() > 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(sent, xx, ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

 /*
  public static boolean nestedSingletonTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each xs[i] = (tag1 pi) where ys[i] = (tag2 pi) 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTBasicTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTBasicTerm) xx).getValue();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1 && 
                   xx instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) xx).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            ASTCompositeTerm xt = (ASTCompositeTerm) xx; 
            ASTTerm xt0 = (ASTTerm) xt.getTerms().get(0); 
            if (xt0.equals(ct0) || 
                mod.correspondingTrees(xt0,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

  public static boolean nestedSingletonTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each xs[i] = (tag1 pi) where ys[i] = (tag2 pi) 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTBasicTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTBasicTerm) xx).getValue();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1 && 
                   xx instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) xx).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            ASTCompositeTerm xt = (ASTCompositeTerm) xx; 
            ASTTerm xt0 = (ASTTerm) xt.getTerms().get(0); 
            if (xt0.equals(ct0) || 
                mod.correspondingTrees(sent,xt0,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

  public static ASTTerm[] composeIntoTrees(Vector termSeqs, Vector res)
  { int n = termSeqs.size(); 
    if (n == 0) 
    { return null; } 
    ASTTerm[] terms0 = (ASTTerm[]) termSeqs.get(0); 
    ASTTerm[] trees = new ASTTerm[terms0.length]; 

    for (int i = 0; i < terms0.length; i++) 
    { ASTCompositeTerm ct = 
        new ASTCompositeTerm("TAG"); 
      for (int j = 0; j < n; j++) 
      { ASTTerm[] termsj = (ASTTerm[]) termSeqs.get(j); 
        ct.addTerm(termsj[i]); 
      } 
      trees[i] = ct; 
      res.add(ct); 
    } 
    return trees; 
  } 

  public static Vector generateASTExamples(Vector javaASTs)
  { /* Generate corresponding OCL and Java example ASTs */
    Vector res = new Vector(); 

    /* Basic numeric expressions: */ 

    for (int i = 500; i < 700; i++)
    { ASTBasicTerm tocl = new ASTBasicTerm("BasicExpression", ""+i); 
      res.add(tocl); 
      ASTBasicTerm javat1 = new ASTBasicTerm("integerLiteral", ""+i); 
      ASTCompositeTerm javat2 = new ASTCompositeTerm("literal", javat1);
      ASTCompositeTerm javat3 = new ASTCompositeTerm("expression", javat2);
      javaASTs.add(javat3); 
    }

    /* Unary expressions: */ 

    Vector oclunary = new Vector(); 
    Vector junary = new Vector();
 
    for (int i = 0; i < 200; i++)
    { Vector args = new Vector(); 
      args.add(new ASTSymbolTerm("-")); 
      args.add(res.get(i)); 
      ASTCompositeTerm ounary = 
        new ASTCompositeTerm("UnaryExpression", args); 
      oclunary.add(ounary); 
     
      Vector jargs = new Vector(); 
      jargs.add(new ASTSymbolTerm("-")); 
      jargs.add(javaASTs.get(i)); 
      ASTCompositeTerm jx = 
        new ASTCompositeTerm("expression", jargs); 
      junary.add(jx); 
    } 

    res.addAll(oclunary); 
    javaASTs.addAll(junary); 
      

    return res;  

  /* (expression (primary (literal (floatLiteral 0.33)))) */ 
  /* (expression (primary (literal "double"))) */ 
  /* (expression (primary (literal true))) */ 
  /* (expression (primary id)) */ 


    /* Binary numeric expressions: 

    ASTBasicTerm t1 = new ASTBasicTerm("BasicExpression", "1"); 
    ASTSymbolTerm st = new ASTSymbolTerm("+"); 
    ASTBasicTerm t2 = new ASTBasicTerm("BasicExpression", "2"); 
    Vector args = new Vector(); 
    args.add(t1); args.add(st); args.add(t2); 
    ASTCompositeTerm t = new ASTCompositeTerm("BinaryExpression", args); 
    res.add(t); 

    ASTBasicTerm jt1 = new ASTBasicTerm("integerLiteral", "1"); 
    ASTCompositeTerm jt2 = new ASTCompositeTerm("literal", jt1);
    ASTCompositeTerm jt3 = new ASTCompositeTerm("expression", jt2);
    ASTBasicTerm jt4 = new ASTBasicTerm("integerLiteral", "2"); 
    ASTCompositeTerm jt5 = new ASTCompositeTerm("literal", jt4);
    ASTCompositeTerm jt6 = new ASTCompositeTerm("expression", jt5); 
    ASTSymbolTerm stj = new ASTSymbolTerm("+"); 
    Vector jargs = new Vector(); 
    jargs.add(jt3); jargs.add(stj); jargs.add(jt6);
    ASTCompositeTerm tj = 
      new ASTCompositeTerm("expression", jargs); 
    javaASTs.add(tj); 
    return res; */ 
  }  

  public static void main(String[] args) 
  { ASTBasicTerm t = new ASTBasicTerm("OclBasicExpression", "true"); 
    System.out.println(t.isInteger()); 
    System.out.println(t.isBoolean()); 
  } 
} 

/* tree2tree dataset format: */ 

/* {"target_ast": 
      {"root": "<LET>", 
       "children": 
          [{"root": "blank", 
              "children": []}, 
           {"root": "<IF>", 
              "children": 
                [{"root": "<CMP>", 
                  "children": 
                     [{"root": "<Expr>", 
                       "children": 
                          [{"root": "0", 
                               "children": []}]
                      }, 
                      {"root": ">", 
                          "children": []}, 
                      {"root": "<Expr>", 
                          "children": 
                            [{"root": "x", 
                              "children": []}]
                      }
                     ]
                 }, 

                 {"root": "<LET>", 
                     "children": 
                        [{"root": "y", 
                             "children": []}, 
                         {"root": "<Expr>", 
                          "children": 
                             [{"root": "y", "children": []}]
                         }, 
                         {"root": "<UNIT>", "children": []}
                        ]
                 }, 
                 {"root": "<LET>", 
                     "children": 
                        [{"root": "y", "children": []}, 
                         {"root": "<Expr>", 
                             "children": 
                                [{"root": "y", 
                                  "children": []}]
                         }, 
                         {"root": "<UNIT>", 
                             "children": []}
                        ]
                 }
               ]}, 
             {"root": "<LET>", "children": [{"root": "x", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}, {"root": "<LET>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}, {"root": "<UNIT>", "children": []}]}]}]}, 

"source_prog": ["if", "0", ">", "x", "then", "y", "=", "y", "else", "y", "=", "y", "endif", ";", "x", "=", "1", ";", "y", "=", "1"], 

"source_ast": {"root": "<SEQ>", "children": [{"root": "<IF>", "children": [{"root": "<CMP>", "children": [{"root": "<Expr>", "children": [{"root": "0", "children": []}]}, {"root": ">", "children": []}, {"root": "<Expr>", "children": [{"root": "x", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "y", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "y", "children": []}]}]}]}, {"root": "<SEQ>", "children": [{"root": "<ASSIGN>", "children": [{"root": "x", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}]}]}]}, 

"target_prog": ["let", "blank", "=", "if", "0", ">", "x", "then", "let", "y", "=", "y", "in", "()", "else", "let", "y", "=", "y", "in", "()", "in", "let", "x", "=", "1", "in", "let", "y", "=", "1", "in", "()"]} */ 

/* Raw format: 
{"for_tree": 
   ["<SEQ>", ["<ASSIGN>", "y", ["<Expr>", "y"]], 
             ["<SEQ>", 
                ["<IF>", 
                   ["<CMP>", ["<Expr>", 1], "<", ["<Expr>", 0]], 
                   ["<ASSIGN>", "x", ["<Expr>", 0]], 
                   ["<ASSIGN>", "x", ["<Expr>", 0]]
                ], 
                ["<ASSIGN>", "y", ["<Expr>", 1]]
              ]
    ], 

  "raw_for": "y = y ; if 1 < 0 then x = 0 else x = 0 endif ; y = 1", "raw_lam": "let y = y in let blank = if 1 < 0 then let x = 0 in () else let x = 0 in () in let y = 1 in ()", 

  "lam_tree": 
     ["<LET>", "y", 
        ["<Expr>", "y"], 
        ["<LET>", "blank", 
            ["<IF>", 
                ["<CMP>", ["<Expr>", 1], "<", ["<Expr>", 0]], 
                ["<LET>", "x", 
                        ["<Expr>", 0], "<UNIT>"], 
                        ["<LET>", "x", ["<Expr>", 0], "<UNIT>"]
                 ], 
                 ["<LET>", "y", ["<Expr>", 1], "<UNIT>"]
             ]
      ]
   } */ 

