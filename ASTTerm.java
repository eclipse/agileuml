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

  public abstract String literalForm(); 

  public abstract String asTextModel(PrintWriter out); 

  public abstract String cg(CGSpec cgs); 

  public abstract String cgRules(CGSpec cgs, Vector rules); 

  // Only for programming languages. 
  public abstract boolean updatesObject(ASTTerm t); 

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
  { if ("integer".equals(str))
    { return isInteger(); } 
    if ("real".equals(str))
    { return isReal(); } 
    if ("boolean".equals(str))
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

} 