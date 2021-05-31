/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 

public abstract class ASTTerm
{ String id = ""; 

  java.util.Map metafeatures = new java.util.HashMap(); 
     // String --> String, eg., recording the conceptual
     // type of the element. 

  java.util.Map types = new java.util.HashMap(); 
     // String --> String for general type of identifiers

  public abstract String toString(); 

  public abstract String literalForm(); 

  public abstract String cg(CGSpec cgs); 

  public abstract String cgRules(CGSpec cgs, Vector rules); 

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
      "long".equals(typ) || "Long".equals(typ); 
  } 

  public static boolean isReal(String typ) 
  { return 
      "float".equals(typ) || "double".equals(typ) || 
      "Float".equals(typ) || "Double".equals(typ); 
  } 

  public static boolean isString(String typ) 
  { return 
      "String".equals(typ) || "Character".equals(typ) || 
      "StringBuffer".equals(typ) || "char".equals(typ) || 
      "StringBuilder".equals(typ); 
  } 

  public String getDefaultValue(String typ) 
  { if (isInteger(typ))
    { return "0"; } 
    if (isReal(typ))
    { return "0.0"; } 
    if (isString(typ))
    { return "\"\""; } 
    return "null"; 
  } 

} 