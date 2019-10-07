import java.util.Vector; 
import java.io.*; 
import java.util.StringTokenizer; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: OCL */

public class Type extends ModelElement
{ private Vector values; // null except for enumerations
  boolean isEntity = false;  // represents an entity as a type
  Entity entity = null; 

  // For future use: 

  // boolean userDefined = false; 
  Type elementType = null; 
  // Expression arrayBound = null; 
  boolean sorted = false;  // for collections 

  Type keyType = null;  // for maps -- by default this is String for a map
  // Note below the assumption that this is String, also in the code generators.

  static java.util.Map simMap = new java.util.HashMap(); 
   
  public Type(String nme, Vector vals)
  { super(nme);
    if ("Map".equals(nme))
    { keyType = new Type("String", null); } 
    if ("Boolean".equals(nme))
    { setName("boolean"); } 
    else if ("Integer".equals(nme))
    { setName("int"); } 
    else if ("Real".equals(nme))
    { setName("double"); } 
    values = vals;
  }

  public Type(String nme, Type arg, Type range)
  { super(nme);
    keyType = arg; 
    elementType = range;
  } // for Map

  public Type(Entity e)
  { this(e + "",null); 
    isEntity = true; 
    entity = e; 
    elementType = this; 
    // elementType = new Type(e); 
  } 

  public boolean isEnumeratedType() 
  { return values != null; } 


  public boolean isEntityType() 
  { return entity != null; } 

  public boolean isSetType() 
  { return "Set".equals(name); } 

  public boolean isSequenceType() 
  { return "Sequence".equals(name); } 


  public Object clone()
  { Type result; 
    if (isEntity) 
    { result = new Type(entity); } 
    else 
    { result = new Type(getName(), values); } 

    if (elementType == null) { } 
    else if (this != elementType)
    { result.setElementType((Type) elementType.clone()); }
    else 
    { result.setElementType(elementType); } 

    if (keyType == null) { } 
    else if (this != keyType) 
    { result.setKeyType((Type) keyType.clone()); } 
    else 
    { result.setKeyType(keyType); } 
 
    return result; 
  } 

  public void setElementType(Type e)
  { elementType = e; } 

  public Type getElementType()
  { return elementType; } 

  public void setKeyType(Type e)
  { keyType = e; } 

  public Type getKeyType()
  { return keyType; } 

  public void setSorted(boolean s)
  { sorted = s; } 

  public boolean isSorted()
  { return sorted; } 

  public Entity getEntity() { return entity; } 

  public int complexity() 
  { if ("Sequence".equals(name) && elementType != null)
    { return 1 + elementType.complexity(); } 

    if ("Set".equals(name) && elementType != null)
    { return 1 + elementType.complexity(); } 

    if ("Map".equals(name) && elementType != null && keyType != null)
    { return 1 + elementType.complexity() + keyType.complexity(); } 

    return 1; 
  } 
    

  public static boolean isSetType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return "Set".equals(nme); 
  } 

  public static boolean isSequenceType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return "Sequence".equals(nme); 
  } 

  public static boolean isMapType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return "Map".equals(nme); 
  } 

  public static boolean isCollectionType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return ("Sequence".equals(nme) || "Set".equals(nme) || "Map".equals(nme)); 
  } 

  public static boolean isNumericType(String t)
  { if ("int".equals(t) || "double".equals(t) || "long".equals(t))
    { return true; } 
    return false; 
  } 

  public static boolean isNumericType(Type t)
  { if (t == null) { return false; } 
    String tname = t.getName(); 
    if ("int".equals(tname) || "double".equals(tname) || "long".equals(tname))
    { return true; } 
    return false; 
  } 

  public boolean isNumericType()
  { if ("int".equals(name) || "double".equals(name) || "long".equals(name))
    { return true; } 
    return false; 
  } 

  public static Type getImageType(Type t, Vector ems) 
  { if (t == null) 
    { return null; } 

    String tname = t.getName(); 
    if ("Sequence".equals(tname))
    { Type et = Type.getImageType(t.getElementType(),ems); 
      Type res = new Type("Sequence",null); 
      res.setElementType(et); 
      return res; 
    } 
    else if ("Set".equals(tname))
    { Type et = Type.getImageType(t.getElementType(),ems); 
      Type res = new Type("Set",null); 
      res.setElementType(et); 
      return res; 
    } 
    else if (Type.isEntityType(t))
    { EntityMatching em = ModelMatching.findEntityMatching(t.getEntity(),ems); 
      if (em != null) 
      { Type imgt = new Type(em.realtrg); 
        return imgt; 
      } 
    } 
    return t; 
  } 


  public static boolean isSubType(Type t1, Type t2) 
  { if (t1 == null || t2 == null) 
    { return false; } 

    if (t1.getName().equals(t2.getName())) { return true; }  // collections? 

    if (t1.isEntity() && t2.isEntity()) 
    { return Entity.isAncestor(t2.entity,t1.entity); } 
    if (t2.getName().equals("double"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("long"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("int"))
    { if (t1.isEnumeration()) { return true; } 
      return false; 
    } 

    return false; 
  } 


  public static boolean isRecursiveSubtype(Type t1, Type t2) 
  { if (t1 == null || t2 == null) 
    { return false; } 

    String t1name = t1.getName(); 
    String t2name = t2.getName(); 

    if ((t1 + "").equals(t2 + "")) { return true; }  // collections? 

    if (t1name.equals("Sequence") && t2name.equals("Set"))
    { return false; } 

    if (t1.isEntity() && t2.isEntity()) 
    { return Entity.isAncestor(t2.entity,t1.entity); } 

    if (t2.getName().equals("double"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("long"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("int"))
    { if (t1.isEnumeration()) { return true; } 
      return false; 
    } 

    if (t1name.equals("Sequence") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isRecursiveSubtype(et1,et2); 
    } 

    if (t1name.equals("Set") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isRecursiveSubtype(et1,et2); 
    } 

    if (t1name.equals("Set") && t2name.equals("Set"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isRecursiveSubtype(et1,et2); 
    } 

    return false; 
  } 

  public static double typeSimilarity(Type t1, Type t2, Map mm, Vector entities)
  { // recusively finds similarity of tsrc, ttrg
    if (t1 == null || t2 == null) 
    { return 0; } 

    String t1name = t1.getName(); 
    String t2name = t2.getName(); 

    if (t1name.equals(t2name))
    { if ("Set".equals(t1name))
      { return typeSimilarity(t1.getElementType(),t2.getElementType(),mm,entities); } 
      else if ("Sequence".equals(t1name))
      { return typeSimilarity(t1.getElementType(),t2.getElementType(),mm,entities); } 
      else // basic types
      { return 1.0; }  
    } 

    if (t1.isEntity() && t2.isEntity())
    { Entity tsrc = t1.getEntity(); 
      // String name$ = tsrc.getName() + "$"; 
      Entity fsrc = tsrc.flattenedCopy(); 
        // (Entity) ModelElement.lookupByName(name$, entities); 
      if (fsrc == null) 
      { fsrc = tsrc; }  // no $ names

      Entity ttrg = (Entity) mm.get(fsrc);
       
      if (ttrg != null) 
      { String fname = ttrg.getName(); 
        Entity realtarget = ttrg.realEntity(); 
        // if (fname.endsWith("$"))
        // { realtarget = (Entity) ModelElement.lookupByName(
        //                         fname.substring(0,fname.length()-1), entities);
        // }  
        // System.out.println(">>> TRYING to match " + tsrc + " " + fsrc + " to " + 
        //                    ttrg + " " + realtarget + " to " + t2.entity); 

        if (realtarget == null) 
        { realtarget = ttrg; } 

        if (t2.entity == realtarget) 
        { return 1.0; } 
        else if (Entity.isAncestor(t2.entity,realtarget))
        { return ModelMatching.SUBSUPER; } 
        else if (Entity.isAncestor(realtarget,t2.entity))
        { return ModelMatching.SUPERSUB; }  
      }
      return 0; // both entities but unrelated
    }  

    if (t2name.equals("long"))
    { if (t1name.equals("int")) 
      { return ModelMatching.INTLONG; } 
      if (t1name.equals("long")) 
      { return 1.0; } 
      return 0; 
    } 

    if (t2name.equals("int"))
    { if (t1name.equals("int")) 
      { return 1.0; } 
      if (t1name.equals("long")) 
      { return ModelMatching.LONGINT; } 
      return 0; 
    } 

    // if (t1name.equals("Sequence") && t2name.equals("Sequence"))
    // { Type et1 = t1.getElementType(); 
    //   Type et2 = t2.getElementType(); 
    //   return Type.typeSimilarity(et1,et2,mm,entities); 
    // } 

    if (t1name.equals("Set") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return ModelMatching.SETSEQUENCE*Type.typeSimilarity(et1,et2,mm,entities); 
    } 

    if (t1name.equals("Sequence") && t2name.equals("Set"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return ModelMatching.SEQUENCESET*Type.typeSimilarity(et1,et2,mm,entities); 
    } 

    // if (t1name.equals("Set") && t2name.equals("Set"))
    // { Type et1 = t1.getElementType(); 
    //   Type et2 = t2.getElementType(); 
    //   return Type.typeSimilarity(et1,et2,mm,entities); 
    // } 

    if (t1.isEnumerated() && "String".equals(t2name))
    { return ModelMatching.ENUMSTRING; } 
    else if ("String".equals(t1name) && t2.isEnumerated())
    { return ModelMatching.STRINGENUM; }     

    if (t1.isEnumerated() && t2.isEnumerated())
    { return Type.enumSimilarity(t1,t2); } 

    if (t1.isEnumerated() && Type.isNumericType(t2name))
    { return 0; } 

    if (t2.isEnumerated() && Type.isNumericType(t1name))
    { return 0; } 

    if (t2.isEnumerated() && "boolean".equals(t1name))
    { return ModelMatching.BOOLENUM*t2.booleanEnumSimilarity(); } 

    if (t1.isEnumerated() && "boolean".equals(t2name))
    { return ModelMatching.BOOLENUM*t1.enumBooleanSimilarity(); } 

    if (t1name.equals("Set"))
    { Type et1 = t1.getElementType(); 
      return ModelMatching.SETONE*Type.typeSimilarity(et1,t2,mm,entities); 
    } // one is collection of similar type to the other single element
    else if (t1name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      return ModelMatching.SEQUENCEONE*Type.typeSimilarity(et1,t2,mm,entities); 
    } 
    else if (t2name.equals("Set"))
    { Type et2 = t2.getElementType(); 
      double rr = ModelMatching.ONESET*Type.typeSimilarity(t1,et2,mm,entities);
      // System.out.println(">>>___> " + t1 + " " + t2 + " __type similarity= " + rr); 
      return rr;  
    } 
    else if (t2name.equals("Sequence"))
    { Type et2 = t2.getElementType(); 
      return ModelMatching.ONESEQUENCE*Type.typeSimilarity(t1,et2,mm,entities); 
    }
    
    return 0; 
  } 

  public static boolean isSubType(Type t1, Type t2, Map mm, Vector entities) 
  { if (t1 == null || t2 == null) 
    { return false; } 
    // if (t1.getName().equals(t2.getName())) { return true; }  // collections? 

    String t1name = t1.getName(); 
    String t2name = t2.getName(); 
    
    if (t1.isEntity() && t2.isEntity()) 
    { Entity tsrc = t1.getEntity(); 
      String name$ = tsrc.getName() + "$"; 
      Entity fsrc = (Entity) ModelElement.lookupByName(name$, entities); 
      Entity ttrg = (Entity) mm.get(fsrc);
       
      if (ttrg != null) 
      { String fname = ttrg.getName(); 
        Entity realtarget = (Entity) ModelElement.lookupByName(
                                fname.substring(0,fname.length()-1), entities); 
        // System.out.println(">>> TRYING to match " + realtarget + " as subclass of " + 
        //                    t2.entity); 

        if (realtarget != null) 
        { if (Entity.isAncestor(t2.entity,realtarget))
          { return true; } 
          else 
          { return t2.entity == realtarget; }
        }  
      }
    }  

    if (t2.getName().equals("double"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      if (t1.getName().equals("double")) { return true; } 
      return false; 
    } 
    if (t2.getName().equals("long"))
    { if (t1.isEnumeration()) { return true; } 
      if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      return false; 
    } 
    if (t2.getName().equals("int"))
    { if (t1.getName().equals("int")) { return true; } 
      if (t1.isEnumeration()) { return true; } 
      return false; 
    } 

    if (t1name.equals("Sequence") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isSubType(et1,et2,mm,entities); 
    } 

    if (t1name.equals("Set") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isSubType(et1,et2,mm,entities); 
    } 

    if (t1name.equals("Set") && t2name.equals("Set"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isSubType(et1,et2,mm,entities); 
    } 

    return false; 
  } 

  public static String booleanEnumConversionFunction(Type e, String srcnme) 
  { if (e.isEnumeration())
    { String nme = e.getName();
      double best = 0; 
      String bestval = (String) e.values.get(0); 
      double worst = 1; 
      String worstval = (String) e.values.get(e.values.size() - 1); 
  
      for (int i = 0; i < e.values.size(); i++) 
      { String val = (String) e.values.get(i); 
        double sim = ModelElement.similarity(val,srcnme); 
        if (sim > best)
        { best = sim; 
          bestval = val; 
        }  
        if (sim < worst)
        { worst = sim; 
          worstval = val; 
        }  
      } 

      String res = "  helper def : boolean2" + nme + srcnme + "(_v : Boolean) : MM2!" + nme + " =\n" + 
      "    if _v = true then #" + bestval + " else #" + worstval + " endif; \n\n"; 
      return res; 
    } 
    return ""; 
  } 
    
  public static String enumBooleanConversionFunction(Type e, String trgnme) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      double best = 0; 
      String bestval = (String) e.values.get(0); 

      for (int i = 0; i < e.values.size(); i++) 
      { String val = (String) e.values.get(i); 
        double sim = ModelElement.similarity(val,trgnme); 
        if (sim > best)
        { best = sim; 
          bestval = val; 
        }  
      } 

      String res = "  helper def : " + nme + "2boolean" + trgnme + "(_v : MM1!" + nme + ") : Boolean =\n" + 
      "    if _v = #" + bestval + " then true else false endif; \n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public static String stringEnumConversionFunction(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  helper def : String2" + nme + "(_v : String) : MM2!" + nme + " =\n" + 
        e.enumcases2EnumATL() + ";\n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public static String enumStringConversionFunction(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  helper def : " + nme + "2String(_v : MM1!" + nme + ") : String =\n" + 
         e.enumcases2StringATL() + ";\n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public String enumQueryOpsQVTR()
  { String res = "  query " + name + "2String(_v : " + name + ") : String\n" + 
      "  { " + enumcases2StringQVTR() + " }\n\n" + 
      "  query String2" + name + "(_v : String) : " + name + "\n" + 
      "  { " + enumcases2EnumQVTR() + " }\n\n"; 
    return res; 
  } 

  public String enumStringQueryOpQVTR()
  { String res = "  query " + name + "2String(_v : " + name + ") : String\n" + 
      "  { " + enumcases2StringQVTR() + " }\n\n";
    return res; 
  } 

  public String stringEnumQueryOpQVTR()
  { String res = "  query String2" + name + "(_v : String) : " + name + "\n" + 
      "  { " + enumcases2EnumQVTR() + " }\n\n"; 
    return res; 
  } 

  private String enumcases2StringQVTR()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if _v = " + val + " then \"" + val + "\" else "; 
      endres = endres + " endif "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "\"" + v + "\" "; 
    return res + endres; 
  }     

  private String enumcases2EnumQVTR()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if _v = \"" + val + "\" then " + val + " else "; 
      endres = endres + " endif "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + v + " "; 
    return res + endres; 
  }     

  public static String booleanEnumOpQVTR(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  query boolean2" + nme + "(_v : boolean) : " + nme + " \n" + 
      "  { if _v = false then " + e.values.get(0) + " else " + e.values.get(1) + " endif } \n\n"; 
      return res; 
    } 
    return ""; 
  } 
    
  public static String enumBooleanOpQVTR(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  query " + nme + "2boolean(_v : " + nme + ") : boolean\n" + 
      "  { if _v = " + e.values.get(0) + " then false else true endif } \n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public String enumQueryOps()
  { String res = "  query " + name + "2String(_v : " + name + ") : String\n" + 
      "  post: " + enumcases2String() + "\n\n" + 
      "  query String2" + name + "(_v : String) : " + name + "\n" + 
      "  post: " + enumcases2Enum() + " \n\n"; 
    return res; 
  } 


  public String enum2StringOp() 
  { String res = "  query " + name + "2String(_v : " + name + ") : String\n" + 
      "  pre: true\n" +  
      "  post: " + enumcases2String() + ";\n\n"; 
    return res; 
  } 

  public String string2EnumOp() 
  { String res = "  query String2" + name + "(_v : String) : " + name + "\n" + 
      "  pre: true\n" +  
      "  post: " + enumcases2Enum() + ";\n\n"; 
    return res; 
  } 

  private String enumcases2String()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "(_v = " + val + " => result = \"" + val + "\") & "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "(true => result = \"" + v + "\")"; 
    return res + endres; 
  }     

  private String enumcases2Enum()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "(_v = \"" + val + "\" => result = " + val + ") & "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "(true => result = " + v + ") "; 
    return res + endres; 
  }     

  public static String booleanEnumOp(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  query boolean2" + nme + "($v : boolean) : " + nme + " \n" + 
      "  pre: true\n" + 
      "  post: ($v = false => result = " + e.values.get(0) + ") & ($v = true => result = " + e.values.get(1) + "); \n\n"; 
      return res; 
    } 
    return ""; 
  } 
    
  public static String enumBooleanOp(Type e) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  query " + nme + "2boolean($v : " + nme + ") : boolean\n" + 
      "  pre: true\n" + 
      "  post: ($v = " + e.values.get(0) + " => result = false) & " + 
      " (true => result = true) \n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public String enumQueryOpsQVTO()
  { String res = "  query " + name + "::" + name + "2String(_v : " + name + ") : String\n" + 
      "  { " + enumcases2StringQVTO() + " }\n\n" + 
      "  query String::String2" + name + "(_v : String) : " + name + "\n" + 
      "  { " + enumcases2EnumQVTO() + " }\n\n"; 
    return res; 
  } 

  public String enum2StringOpQVTO()
  { String res = "  query " + name + "::" + name + "2String(_v : " + name + ") : String\n" + 
      "  { " + enumcases2StringQVTO() + " }\n\n"; 
    return res; 
  } 

  public String string2EnumOpQVTO()
  { String res = 
      "  query String::String2" + name + "(_v : String) : " + name + "\n" + 
      "  { " + enumcases2EnumQVTO() + " }\n\n"; 
    return res; 
  } 


  private String enumcases2StringQVTO()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if (_v = " + val + ") then { return \"" + val + "\"; } else "; 
      endres = endres + " endif; "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "{ return \"" + v + "\"; } "; 
    return res + endres; 
  }     

  private String enumcases2EnumQVTO()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if (_v = \"" + val + "\") then { return " + val + "; } else "; 
      endres = endres + " endif; "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "{ return " + v + "; } "; 
    return res + endres; 
  }     

  public static String booleanEnumOpQVTO(Type e, Vector thesaurus) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 

      String res = "  query Boolean::boolean2" + nme + "(_v : Boolean) : " + nme + " \n" + 
      "  { if _v = false then " + e.values.get(0) + " else " + e.values.get(1) + " endif } \n\n"; 
      return res; 
    } 
    return ""; 
  } 
    
  public static String enumBooleanOpQVTO(Type e, Vector thesaurus) 
  { if (e.isEnumeration())
    { String nme = e.getName(); 
      String res = "  query " + nme + "::" + nme + "2boolean(_v : " + nme + ") : Boolean\n" + 
      "  { if _v = " + e.values.get(0) + " then false else true endif } \n\n"; 
      return res; 
    } 
    return ""; 
  } 

  public String enumQueryOpsATL()
  { String res = "  helper def : " + name + "2String(_v : MM1!" + name + ") : String =\n" + 
      "    " + enumcases2StringATL() + "; \n\n" + 
      "  helper def : String2" + name + "(_v : String) : MM2!" + name + " =\n" + 
      "    " + enumcases2EnumATL() + ";\n\n"; 
    return res; 
  } 

  private String enumcases2StringATL()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if (_v = #" + val + ") then \"" + val + "\" else "; 
      endres = endres + " endif "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + "\"" + v + "\""; 
    return res + endres; 
  }     

  private String enumcases2EnumATL()
  { String res = ""; 
    String endres = ""; 
    for (int i = 0; i < values.size() - 1; i++) 
    { String val = (String) values.get(i); 
      res = res + "if (_v = \"" + val + "\") then #" + val + " else "; 
      endres = endres + " endif "; 
    } 
    String v = (String) values.get(values.size()-1); 
    res = res + " #" + v + " "; 
    return res + endres; 
  }     

  public String initialValueJava6()  // not used? 
  { if (isSequenceType(this))
    { return "new ArrayList()"; } 
    if (isSetType(this))
    { return "new HashSet()"; } 
    if (isMapType(this))
    { return "new HashMap()"; } 
    if ("boolean".equals(getName())) { return "false"; }
    if (isEntity) { return "null"; }  
    if ("String".equals(getName())) { return "\"\""; } 
    return "0"; 
  } 

  public String initialValueJava7()  // not used? 
  { if (isSequenceType(this))
    { return "new ArrayList<" + elementType.typeWrapperJava7() + ">()"; } 
    if (isSetType(this))
    { return "new HashSet<" + elementType.typeWrapperJava7() + ">()"; } 
    if (isMapType(this))
    { return "new HashMap<String, " + elementType.typeWrapperJava7() + ">()"; } 
    if ("boolean".equals(getName())) { return "false"; }
    if (isEntity) { return "null"; }  
    if ("String".equals(getName())) { return "\"\""; } 
    return "0"; 
  } 

  public boolean isEnumeration()
  { return (values != null); } 

  public boolean isBoolean()
  { return "boolean".equals(getName()); } 

  public boolean isString()
  { return "String".equals(getName()); } 

  public boolean isEntity()
  { return isEntity; } 

  public static boolean isEntityType(Type t)
  { if (t == null) 
    { return false; } 
    return t.isEntity; 
  } 

  public static boolean isCollectionOrEntity(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    if ("Set".equals(nme) || "Sequence".equals(nme)) { return true; } 
    if ("Map".equals(nme)) { return true; } /* Not sure */ 
    return t.isEntity(); 
  } 

  public static boolean isEntityCollection(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    if ("Set".equals(nme) || "Sequence".equals(nme))  
    { Type elemt = t.getElementType(); 
      if (elemt == null) 
      { return false; } 
      return elemt.isEntity();
    } 
    return false;  
  } 

  public Type mergeType(Type t)
  { if (values == null && t.values == null)
    { if (t.getName().equals(getName()))
      { return this; } // same type
      return null; 
    } 
    if (values != null && t.values != null)
    { if (("" + t.values).equals("" + values))
      { return this; } 
      return null; 
    } 
    return null; 
  }         // could also get closest common ancestor of 2 entities

  public Vector getValues()
  { return values; }

  public void setValues(Vector vals)
  { values = new Vector(); 
    values.addAll(vals);
  }

  public String getValuesAsString()
  { if (values == null) { return ""; } 
    String res = ""; 
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      res = res + val + " ";  
    } 
    return res; 
  } 

  public boolean hasValue(String st)
  { if (values == null) 
    { return false; } 
    return values.contains(st); 
  } 

  public boolean valueClash(Vector vals)
  { if (values == null) { return false; } 
    for (int i = 0; i < vals.size(); i++) 
    { String val = (String) vals.get(i); 
      if (values.contains(val))
      { System.err.println("Value " + val + " already defined in type " + name);
        return true; 
      } 
    } 
    return false; 
  } 


  public boolean isEnumerated()
  { return values != null; } 

  public static double enumSimilarity(Type e1, Type e2)
  { String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    String key = name1 + " " + name2;  
    Double d = (Double) simMap.get(key); 
    if (d != null)
    { return d.doubleValue(); } 

    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    double score = 0; 
    for (int i = 0; i < values1.size(); i++) 
    { String val = (String) values1.get(i); 
      for (int j = 0; j < values2.size(); j++) 
      { String valt = (String) values2.get(j); 
        double v1v2sim = ModelElement.similarity(val,valt); 
        // System.out.println(">>> match of " + val + " " + valt + " is " + v1v2sim); 
        score = (score + v1v2sim) - (score*v1v2sim); 
      } 
    } 

    // Vector intersect = VectorUtil.intersection(values1,values2); 
    // int s = intersect.size(); 
    double namesim = ModelElement.similarity(name1,name2); 
    double res = (score + namesim) - (score*namesim);
    // System.out.println(">>>> Similarity of " + name1 + " " + name2 + " is " + res); 
    simMap.put(key, new Double(res)); 
    return res;  
  } 

  public static String enumConversionFunction(Type e1, Type e2, Vector thesaurus)
  { String res = "  helper def: convert" + e1.getName() + "_" + e2.getName() + 
                 "(s : MM1!" + e1.getName() + ") : MM2!" + e2.getName() + " =\n    "; 
    String restail = ""; 

    String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    
    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    for (int i = 0; i < values1.size() - 1; i++) 
    { double bestscore = 0;
      String besttarget = values2.get(0) + ""; 
 
      String val = (String) values1.get(i); 

      for (int j = 0; j < values2.size(); j++) 
      { String valt = (String) values2.get(j); 
        double namesim = ModelElement.similarity(val,valt); 
        double namesemsim = Entity.nmsSimilarity(val,valt, thesaurus); 
        double nsim = (namesim + namesemsim - namesim*namesemsim); 
        if (nsim > bestscore) 
        { bestscore = nsim; 
          besttarget = valt; 
        } 
      } 

      if (bestscore > 0) 
      { res = res + "if s = #" + val + " then #" + besttarget + " else "; 
        restail = restail + " endif "; 
      }       
    } 

    String lastval = (String) values1.get(values1.size() - 1); 
    double bestscore = 0;
    String besttarget = values2.get(0) + ""; 
 
    for (int j = 0; j < values2.size(); j++) 
    { String valt = (String) values2.get(j); 
      double namesim = ModelElement.similarity(lastval,valt); 
      double namesemsim = Entity.nmsSimilarity(lastval,valt, thesaurus); 
      double nsim = (namesim + namesemsim - namesim*namesemsim); 
      if (nsim > bestscore) 
      { bestscore = nsim; 
        besttarget = valt; 
      } 
    } 

    if (bestscore > 0) 
    { res = res + " #" + besttarget + " "; }       

    return res + restail + ";\n\n"; 
  } 

  public static String enumConversionOpQVT(Type e1, Type e2)
  { String res = "  query convert" + e1.getName() + "_" + e2.getName() + 
                 "(s : " + e1.getName() + ") : " + e2.getName() + " =\n    "; 
    String restail = ""; 

    String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    
    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    for (int i = 0; i < values1.size() - 1; i++) 
    { double bestscore = 0;
      String besttarget = values2.get(0) + ""; 
 
      String val = (String) values1.get(i); 

      for (int j = 0; j < values2.size(); j++) 
      { String valt = (String) values2.get(j); 
        double v1v2sim = ModelElement.similarity(val,valt); 
        if (v1v2sim > bestscore) 
        { bestscore = v1v2sim; 
          besttarget = valt; 
        } 
      } 

      if (bestscore > 0) 
      { res = res + "if s = " + val + " then " + besttarget + " else "; 
        restail = restail + " endif "; 
      }       
    } 

    String lastval = (String) values1.get(values1.size() - 1); 
    double bestscore = 0;
    String besttarget = values2.get(0) + ""; 
 
    for (int j = 0; j < values2.size(); j++) 
    { String valt = (String) values2.get(j); 
      double v1v2sim = ModelElement.similarity(lastval,valt); 
      if (v1v2sim > bestscore) 
      { bestscore = v1v2sim; 
        besttarget = valt; 
      } 
    } 

    if (bestscore > 0) 
    { res = res + " " + besttarget + " "; }       

    return res + restail + ";\n\n"; 
  } 

  public static String enumConversionOpQVTO(Type e1, Type e2)
  { String res = "  query " + e1.getName() + "::convert" + e1.getName() + "_" + e2.getName() + 
                 "(s : " + e1.getName() + ") : " + e2.getName() + " =\n    "; 
    String restail = ""; 

    String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    
    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    for (int i = 0; i < values1.size() - 1; i++) 
    { double bestscore = 0;
      String besttarget = values2.get(0) + ""; 
 
      String val = (String) values1.get(i); 

      for (int j = 0; j < values2.size(); j++) 
      { String valt = (String) values2.get(j); 
        double v1v2sim = ModelElement.similarity(val,valt); 
        if (v1v2sim > bestscore) 
        { bestscore = v1v2sim; 
          besttarget = valt; 
        } 
      } 

      if (bestscore > 0) 
      { res = res + "if s = " + val + " then " + besttarget + " else "; 
        restail = restail + " endif "; 
      }       
    } 

    String lastval = (String) values1.get(values1.size() - 1); 
    double bestscore = 0;
    String besttarget = values2.get(0) + ""; 
 
    for (int j = 0; j < values2.size(); j++) 
    { String valt = (String) values2.get(j); 
      double v1v2sim = ModelElement.similarity(lastval,valt); 
      if (v1v2sim > bestscore) 
      { bestscore = v1v2sim; 
        besttarget = valt; 
      } 
    } 

    if (bestscore > 0) 
    { res = res + " " + besttarget + " "; }       

    return res + restail + ";\n\n"; 
  } 

  public static String enum2enumOp(Type e1, Type e2)
  { String res = "  query convert" + e1.getName() + "_" + e2.getName() + 
                 "(s : " + e1.getName() + ") : " + e2.getName() + "\n" +   
      "  pre: true\n" +  
      "  post:  "; 

    String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    
    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    for (int i = 0; i < values1.size(); i++) 
    { double bestscore = 0;
      String besttarget = values2.get(0) + ""; 
 
      String val = (String) values1.get(i); 

      for (int j = 0; j < values2.size(); j++) 
      { String valt = (String) values2.get(j); 
        double v1v2sim = ModelElement.similarity(val,valt); 
        if (v1v2sim > bestscore) 
        { bestscore = v1v2sim; 
          besttarget = valt; 
        } 
      } 

      if (bestscore > 0) 
      { res = res + "(s = " + val + " => result = " + besttarget + ") "; 
        if (i < values1.size() - 1) 
        { res = res + " & "; } 
      }       
    } 
    return res + ";\n\n"; 
  } 

  public double enumBooleanSimilarity()
  { int s1 = values.size(); 
    if (s1 <= 1) 
    { return 0; } 
    return 2.0/s1; 
  } // similarity for mapping this enumeration to a boolean

  public double booleanEnumSimilarity()
  { int s1 = values.size();
    if (s1 <= 1) 
    { return 0; }  
    return s1/2.0; 
  } // similarity for mapping boolean to this enumeration

  public double enumBooleanSimilarity(String nme)
  { 
    double score = 0; 
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      double sim = ModelElement.similarity(nme,val);
      score = (score + sim) - (score*sim); 
    }  
    return score; 
  } // There exists a value with similar name to the boolean attribute. 

  public boolean isPrimitive()
  { String nme = getName();
    if (nme.equals("int") || nme.equals("double") || nme.equals("boolean") ||
        nme.equals("long") || values != null)
    { return true; }
    return false;
  } 

  public static boolean isPrimitiveType(Type t)
  { if (t == null) { return false; } 
    String nme = t.getName();
    if (nme.equals("int") || nme.equals("double") || nme.equals("boolean") ||
        nme.equals("long") || t.values != null)
    { return true; }
    return false;
  } 

  public static boolean isBasicType(Type e)
  { if (e == null) { return false; } 
    if ("String".equals("" + e))
    { return true; } 
    return e.isPrimitive(); 
  } 

  public int typeMultiplicity()
  { String nme = getName(); 
    if (nme.equals("int") || nme.equals("double") || nme.equals("boolean") ||
        nme.equals("long") || values != null)
    { return ModelElement.ONE; }
    if (isEntity) 
    { return ModelElement.ONE; }
    if (nme.equals("Set") || nme.equals("Sequence"))
    { return ModelElement.MANY; }  
    if (nme.equals("Map")) 
    { return elementType.typeMultiplicity(); } 

    return ModelElement.ONE;
  }  

  public boolean isMultiple()
  { String nme = getName(); 
    if (nme.equals("Set") || nme.equals("Sequence"))
    { return true; }  
    if (nme.equals("Map"))
    { return elementType.isMultiple(); } 
    return false; 
  } 

  public boolean isParsable()
  { String nme = getName(); 
    if ("String".equals(nme) ||
        "int".equals(nme) || "long".equals(nme) || "double".equals(nme))
    { return true; } 
    return false; 
  } // also boolean

  public String dataExtractionCode(String data)
  { String nme = getName(); 
    if (nme.equals("String")) 
    { return data; } 
    if (nme.equals("int")) { return "Integer.parseInt(" + data + ")"; } 
    else if (nme.equals("long")) { return "Long.parseLong(" + data + ")"; } 
    else if (nme.equals("double")) { return "Double.parseDouble(" + data + ")"; } 
    // if (nme.equals("boolean")) { return "Boolean.parseBoolean(" + data + ")"; } 
    return data; 
  } 

  public String collectionExtractionCode(String ex, String aname, String data)
  { String nme = elementType.getName();
    String extractValue = "(String) " + data + ".get(_i)"; 

    if (nme.equals("int")) 
    { extractValue = "new Integer(Integer.parseInt((String) " + data + ".get(_i)))"; } 
    else if (nme.equals("long")) 
    { extractValue = "new Long(Long.parseLong((String) " + data + ".get(_i)))"; } 
    else if (nme.equals("double")) 
    { extractValue = "new Double(Double.parseDouble((String) " + data + ".get(_i)))"; }
 
    String res = "for (int _i = 0; _i < " + data + ".size(); _i++)\n" + 
             "    { " + ex + "." + aname + ".add(" + extractValue + "); }\n";   
    return res; 
  } 

  public String getUMLName()
  { String nme = getName(); 
    if (nme.equals("int")) { return "Integer"; } 
    if (nme.equals("long")) { return "Long"; } 
    if (nme.equals("double")) { return "Real"; } 
    if (nme.equals("boolean")) { return "Boolean"; } 
    if (nme.equals("Set") || nme.equals("Sequence"))
    { if (elementType != null) 
      { return nme + "(" + elementType.getUMLName() + ")"; } 
      else 
      { return nme; } 
    } 

    if (nme.equals("Map"))
    { return nme + "(" + keyType.getUMLName() + ", " + elementType.getUMLName() + ")"; } 

    return nme; 
  } 

  public String getUMLModelName(PrintWriter out)
  { String nme = getName(); 
    if (nme.equals("int")) { return "Integer"; } 
    if (nme.equals("long")) { return "Long"; } 
    if (nme.equals("double")) { return "Real"; } 
    if (nme.equals("boolean")) { return "Boolean"; } 
    if (nme.equals("Set")) 
    { String tid = Identifier.nextIdentifier(""); 
      out.println("SetType" + tid + " : CollectionType"); 
      out.println("SetType" + tid + ".name = \"Set\""); 
      if (elementType != null) 
      { String etype = elementType.getUMLModelName(out); 
        out.println("SetType" + tid + ".elementType = " + etype);
      }
      else 
      { out.println("SetType" + tid + ".elementType = void"); }
  
      out.println("SetType" + tid + ".typeId = \"" + tid + "\""); 
      return "SetType" + tid; 
    } 
    else if (nme.equals("Sequence"))
    { String tid = Identifier.nextIdentifier(""); 
      out.println("SequenceType" + tid + " : CollectionType"); 
      out.println("SequenceType" + tid + ".name = \"Sequence\""); 
      if (elementType != null) 
      { String etype = elementType.getUMLModelName(out); 
        out.println("SequenceType" + tid + ".elementType = " + etype);
      }  
      else 
      { out.println("SequenceType" + tid + ".elementType = void"); }

      out.println("SequenceType" + tid + ".typeId = \"" + tid + "\""); 
      return "SequenceType" + tid;
    } 
    else if (nme.equals("Map"))
    { String tid = Identifier.nextIdentifier(""); 
      out.println("MapType" + tid + " : CollectionType"); 
      out.println("MapType" + tid + ".name = \"Map\""); 
      if (elementType != null) 
      { String etype = elementType.getUMLModelName(out); 
        out.println("MapType" + tid + ".elementType = " + etype); 
      } 
      else 
      { out.println("MapType" + tid + ".elementType = void"); } 
      // assume key type is String
      out.println("MapType" + tid + ".typeId = \"" + tid + "\""); 
      return "MapType" + tid; 
    } 
    return nme; 
  } 

  public String getZ3Name()  // check
  { String nme = getName(); 
    if (nme.equals("int") || nme.equals("long")) { return "Int"; } 
    if (nme.equals("double")) { return "Real"; } 
    if (nme.equals("boolean")) { return "Bool"; } 
    return nme; 
  } // and others

  public String eType()   // long? 
  { String nme = getName(); 
    if (nme.equals("int") || nme.equals("long")) { return "EInt"; } 
    if (nme.equals("double")) { return "EDouble"; } 
    if (nme.equals("boolean")) { return "EBoolean"; }
    if (nme.equals("String")) { return "EString"; }  
    return nme; 
  } 

  public void saveEcore(PrintWriter out)
  { String res = ""; 
    if (values == null) 
    { return; } 
    res = "<eClassifiers xsi:type=\"ecore:EEnum\" name=\"" + name + "\">";
    out.println(res);  
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      out.println("  <eLiterals name=\"" + val + "\"/>"); 
    } 
  } 

  public static Type getEcoreType(String etype, Vector types)  // long?? 
  { if (etype == null) 
    { return null; } 

    if (etype.endsWith("EString"))
    { return new Type("String",null); } 
    if (etype.endsWith("EInt"))
    { return new Type("int",null); } 
    if (etype.endsWith("ELong"))
    { return new Type("long",null); } 
    if (etype.endsWith("EDouble"))
    { return new Type("double",null); } 
    if (etype.endsWith("EBoolean"))
    { return new Type("boolean",null); }

    String et = "";
    if (etype.startsWith("#//"))
    { et = etype.substring(3,etype.length()); }

    Type t =
       (Type) ModelElement.lookupByName(et,types);

    if (t == null) 
    { return new Type("String",null); } 

    return t;  
  } 
    
  public void saveKM3(PrintWriter out)
  { String res = ""; 
    if (values == null)  // for basic types only
    { out.println("  datatype " + name + ";"); 
      return; 
    } 
    res = "  enumeration " + name + " {";
    out.println(res);  
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      out.println("    literal " + val + ";"); 
    } 
    out.println("  }"); 
  } 

  public static boolean primitiveType(Type t) 
  { if (t == null) // need to get type for atts
    { return false; }
    String tname = t.getName();

    if (tname.equals("int") || tname.equals("double") ||
        tname.equals("long") || tname.equals("boolean"))
    { return true; } 

    if (tname.equals("String") || tname.equals("Set") || tname.equals("Sequence"))
    { return false; }

    if (tname.equals("Map")) { return false; } 

    if (t.getEntity() != null)  { return false; } 

    return true; // int, boolean, enum, double
  }

  public static boolean isAttributeType(String tname) 
  { if (tname.equals("int") || tname.equals("double") || tname.equals("String") || 
        tname.equals("long") || tname.equals("boolean") || 
        tname.equals("Integer") || tname.equals("Real") || 
        tname.equals("Boolean"))
    { return true; } 
    return false;
  } // But actually collection types and entities are also permitted. 

  public static boolean isAttributeType(Type t) 
  { if (t == null) // need to get type for atts
    { return false; }
    String tname = t.getName();
    if (tname.equals("int") || tname.equals("double") || tname.equals("String") || 
        tname.equals("long") || tname.equals("boolean"))
    { return true; } 

    if (tname.equals("Set") || tname.equals("Sequence"))
    { return false; }
    if (tname.equals("Map")) { return false; } 

    if (t.getEntity() != null)  { return false; } 
    return true; // enum
  }

  public boolean isCollectionType()
  { String nme = getName(); 
    return ("Sequence".equals(nme) || "Set".equals(nme) || "Map".equals(nme)); 
  } 


  public String getSmvType()
  { if (values == null) 
    { if ("boolean".equals(getName()))
      { return "boolean"; } 
      else if ("int".equals(getName()))
      { return "0..3"; } 
      else if ("String".equals(getName()))
      { return "{ empty, string0, string1, string2 }"; } 
      return null; 
    } 
    else 
    { String res = "{ "; 
      for (int i = 0; i < values.size(); i++) 
      { res = res + values.get(i); 
        if (i < values.size() - 1) 
        { res = res + ", "; } 
      } 
      return res + " }"; 
    } 
  }  // case for objects? 

  public Vector getSmvValues()
  { Vector res = new Vector(); 
    String typ = getName(); 
   
    if (values == null) 
    { if (typ.equals("boolean"))
      { res.add("FALSE"); 
        res.add("TRUE"); 
        return res;
      } // and case for int, objects? 
      if ("String".equals(typ))
      { res.add("empty"); res.add("string0"); res.add("string1"); 
        res.add("string2");
        return res;  
      } 
      else // numeric type
      { res = new Vector(); 
        res.add("0"); 
        res.add("1");
        res.add("2"); 
        res.add("3");   
        return res; 
      }
    }
    res.addAll(values); 
    return res; 
  } 
  
  public boolean compare(String operator, String val1, String val2)
  { if (values == null) { return false; } 
    int i1 = values.indexOf(val1); 
    int i2 = values.indexOf(val2);
    // could both be -1 if neither values are in the type
 
    if (operator.equals("=")) 
    { return i1 == i2; } 
    if (operator.equals("<="))
    { return i1 <= i2; } 
    if (operator.equals(">="))
    { return i1 >= i2; } 
    if (operator.equals("<"))
    { return i1 < i2; } 
    if (operator.equals(">"))
    { return i1 > i2; } 
    return false; 
  } 

  // public void setEntity(boolean e) 
  // { isEntity = e; } 

  public String getDefault()
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return "0"; }
      if (nme.startsWith("Set") || nme.startsWith("Sequence"))
      { return "new Vector()"; }
      if (nme.startsWith("Map")) 
      { return "new HashMap()"; } 
      return "null";    // for class types
    }
    return (String) values.get(0);
  }
  // Set and Sequence?

  public Expression getDefaultValueExpression()
  { return getDefaultValueExpression(elementType); } 

  public Expression getDefaultValueExpression(Type elemt)
  { Expression res = null; 
    if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { res = new BasicExpression("\"\""); }
      else if (nme.equals("boolean"))
      { return new BasicExpression(false); }
      else if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return new BasicExpression(0); }
      else if (nme.startsWith("Set") || nme.startsWith("Sequence") || nme.startsWith("Map"))
      { res = new SetExpression();
        if (nme.startsWith("Sequence"))
        { ((SetExpression) res).setOrdered(true); }
        res.setType(this); 
        res.setElementType(elemt); 
      }
      else 
      { res = new BasicExpression(0); } 
    }
    else 
    { res = new BasicExpression((String) values.get(0)); } 
    res.setType(this); 
    res.setElementType(elemt); 
    return res; 
  }

  public String getDefaultJava6()
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("Set"))
      { return "new HashSet()"; } 
      if (nme.equals("Sequence"))
      { return "new ArrayList()"; }
      if (nme.equals("Map"))
      { return "new HashMap()"; } 
      return "null";    // for class types
    }
    return (String) values.get(0);
  }
  // Set and Sequence?

  public String getDefaultJava7()
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("Set"))
      { if (elementType != null) 
        { return "new HashSet<" + elementType.typeWrapperJava7() + ">()"; }
        else 
        { return "new HashSet()"; }
      } 
      if (nme.equals("Sequence"))
      { if (elementType != null) 
        { return "new ArrayList<" + elementType.typeWrapperJava7() + ">()"; }
        else 
        { return "new ArrayList()"; } 
      } 
      if (nme.equals("Map"))
      { if (elementType != null) 
        { return "new HashMap<String," + elementType.typeWrapperJava7() + ">()"; } 
        else 
        { return "new HashMap()"; }
      }  
      return "null";    // for class types
    } 
    return (String) values.get(0);
  }
  // Set and Sequence?

  public String getDefaultCSharp()
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("Set") || nme.equals("Sequence"))
      { return "new ArrayList()"; }
      if (nme.equals("Map"))
      { return "new Hashtable()"; }
      return "null";    // for class types
    }
    return "0"; // (String) values.get(0);
  }

  public String getDefaultCPP(Type et)
  { if (et != null) 
    { return getDefaultCPP(et.getCPP(et.getElementType())); } 
    else if (elementType != null) 
    { return getDefaultCPP(elementType.getCPP(elementType.getElementType())); }
    else 
    { return getDefaultCPP("void*"); }  
  } 

  public String getDefaultCPP(String et)
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("double") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("Set"))
      { return "new set<" + et + ">()"; } 
      if (nme.equals("Sequence"))
      { return "new vector<" + et + ">()"; }
      if (nme.equals("Map"))
      { return "new map<string, " + et + ">()"; }
      return "NULL";    // for class types
    }
    return "0"; // (String) values.get(0);
  }


  public void generateJava(PrintWriter out)
  { out.print(getJava() + " "); }

  public void generateJava6(PrintWriter out)
  { out.print(getJava6() + " "); }

  public void generateJava7(PrintWriter out, Type elemType)
  { out.print(getJava7(elemType) + " "); }

  public void generateCSharp(PrintWriter out)
  { out.print(getCSharp() + " "); }

  public void generateCPP(PrintWriter out, Type elemType)
  { out.print(getCPP(elemType) + " "); }

  public String getCSharp()  
  { String nme = getName();
    if (nme.equals("Set") || nme.equals("Sequence"))
    { return "ArrayList"; }
    if (nme.equals("Map"))
    { return "Hashtable"; }
    if (nme.equals("String")) { return "string"; }  
    if (nme.equals("boolean")) { return "bool"; } 
    if (nme.equals("long")) { return "long"; } 
    if (values == null) { return nme; } 
    return "int";   
  } 

  public String getCPP(String elemType)
  { String nme = getName();
    if (nme.equals("Set")) 
    { return "set<" + elemType + ">*"; } 
    if (nme.equals("Sequence"))
    { return "vector<" + elemType + ">*"; } 
    if (nme.equals("Map"))
    { return "map<string, " + elemType + ">*"; } 
    if (nme.equals("String")) { return "string"; }  
    if (nme.equals("boolean")) { return "bool"; } 
    if (isEntity) { return nme + "*"; } 
    return nme;  // enumerations, long, int and double 
  } 

  public String getCPP(Type elemType)
  { String et = "void*"; 
    if (elemType != null) 
    { if (elemType.elementType != null && elemType != this) 
      { et = elemType.getCPP(elemType.elementType); }
      else 
      { et = elemType.getCPP("void*"); }
    } 
    return getCPP(et); 
  } 

  public String getCPP()  /* For attribute/simple types */ 
  { String nme = getName();
    if (nme.equals("Set") || nme.equals("Sequence") || nme.equals("Map"))
    { return "void*"; }
    if (nme.equals("String")) { return "string"; }  
    if (nme.equals("boolean")) { return "bool"; } 
    if (isEntity) { return nme + "*"; } 
    return nme;  // enumerations, int, long and double 
  } 

  public void asTextModel(PrintWriter out)
  { if (values != null) 
    { String nme = getName(); 
      out.println(nme + " : Enumeration"); 
      out.println(nme + ".name = \"" + nme + "\""); 
      for (int i = 0; i < values.size(); i++) 
      { String val = (String) values.get(i); 
        out.println(nme + "_" + val + " : EnumerationLiteral"); 
        out.println(nme + "_" + val + ".name = \"" + val + "\""); 
        out.println(nme + "_" + val + " : " + nme + ".ownedLiteral");
      }
    } 
  } 

  public String generateB()
  { String nme = getName();
    if (entity != null)
    { return nme.toLowerCase() + "s"; }
    if (values == null || values.size() == 0)
    { if (nme.equals("int") || nme.equals("long")) 
      { return "INT"; }  // really only for "int"
      if (nme.equals("String"))
      { return "STRING"; }
      if (nme.equals("double"))
      { return "NUM"; } 
      return "BOOL";
    }    // what about sets? 
    else 
    { return nme; }
  }

  public String generateB(Expression var)
  { String nme = getName();
    if (nme.equals("Set") || nme.equals("Sequence"))
    { Type et = var.elementType; 
      if (et != null)
      { String etB = et.generateB(); 
        if (nme.equals("Set")) { return "FIN(" + etB + ")"; } 
        else { return "seq(" + etB + ")"; } 
      } 
    } 
    if (nme.equals("Map"))
    { Type et = var.elementType; 
      if (et != null)
      { String etB = et.generateB(); 
        return "STRING +-> " + etB;   // Partial function  
      } 
    } 
    if (entity != null)
    { return nme.toLowerCase() + "s"; }
    if (values == null || values.size() == 0)
    { if (nme.equals("int") || nme.equals("long")) 
      { return "INT"; }
      if (nme.equals("String"))
      { return "STRING"; }
      if (nme.equals("double"))
      { return "NUM"; } 
      return "BOOL";
    }    // what about sets? 
    else 
    { return nme; }
  }

  public String generateB(Type elemType)
  { String nme = getName();
    if (nme.equals("Set") || nme.equals("Sequence"))
    { if (elemType != null)
      { String etB = ""; 
        if (elemType.elementType != null) 
        { etB = elemType.generateB(elemType.elementType); } 
        else 
        { etB = elemType.generateB(); } 
 
        if (nme.equals("Set")) { return "FIN(" + etB + ")"; } 
        else { return "seq(" + etB + ")"; } 
      } 
    } 
    if (nme.equals("Map"))
    { if (elemType != null)
      { String etB = ""; 
        if (elemType.elementType != null) 
        { etB = elemType.generateB(elemType.elementType); } 
        else 
        { etB = elemType.generateB(); } 
 
        return "STRING +-> " + etB;  
      } 
    } 
    if (entity != null)
    { return nme.toLowerCase() + "s"; }
    if (values == null || values.size() == 0)
    { if (nme.equals("int") || nme.equals("long")) 
      { return "INT"; }
      if (nme.equals("String"))
      { return "STRING"; }
      if (nme.equals("double"))
      { return "NUM"; } 
      return "BOOL";
    }    
    else 
    { return nme; }
  }
 
 
  public String getJava()
  { String nme = getName();
    if (nme.equals("Set") || nme.equals("Sequence"))
    { return "List"; } 
    if (nme.equals("Map"))
    { return "Map"; } 
    if (values == null)
    { return nme; }
    return "int"; 
  }

  public String getJava6()
  { String nme = getName();
    if (nme.equals("Set"))
    { return "HashSet"; } 
    if (nme.equals("Sequence"))
    { return "ArrayList"; } 
    if (nme.equals("Map"))
    { return "HashMap"; } 
    if (values == null)
    { return nme; }
    // if (nme.equals("long")) { return "long"; } 
    return "int"; 
  }

  public String getJava7()
  { String nme = getName();
    // String et = ""; 
    // if (elementType != null && elementType != this) 
    // { et = elementType.getJava7(); } 
    // else 
    // { et = "Object"; } 

    if (nme.equals("Set"))
    { String tname = "HashSet"; 
      if (sorted) 
      { tname = "TreeSet"; } 

      if (elementType != null) 
      { return tname + "<" + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return tname; } 
    } 

    if (nme.equals("Sequence"))
    { if (elementType != null) 
      { return "ArrayList<" + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "ArrayList"; } 
    } 

    if (nme.equals("Map"))
    { if (elementType != null) 
      { return "HashMap<String, " + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "HashMap"; } 
    } 

    if (values == null)
    { return nme; }
    // if (nme.equals("long")) { return "long"; } 
    return "int"; 
  }

  public String getJava7(Type elemType)
  { String nme = getName();
    String et = "Object"; 
    // if (elemType != null && elemType != this) 
    // { if (elemType.elementType != null) 
    //   { et = elemType.getJava7(elemType.elementType); }
    //   else 
    //   { et = elemType.getJava7(); } 
    // }  

    if (nme.equals("Set"))
    { String tname = "HashSet"; 
      if (sorted) 
      { tname = "TreeSet"; } 

      if (elemType != null) 
      { return tname + "<" + elemType.typeWrapperJava7() + ">"; } 
      else 
      { return tname; }
    }  

    if (nme.equals("Sequence"))
    { if (elemType != null) 
      { return "ArrayList<" + elemType.typeWrapperJava7() + ">"; } 
      else 
      { return "ArrayList"; }
    }  

    if (nme.equals("Map"))
    { if (elemType != null) 
      { return "HashMap<String, " + elemType.typeWrapperJava7() + ">"; } 
      else 
      { return "HashMap"; }
    }  

    if (values == null)
    { return nme; }
    // if (nme.equals("long")) { return "long"; } 
    return "int"; 
  }

  public static String getJava7Type(Type typ, Type elemType)
  { String et = "Object"; 
    if (elemType != null) 
    { et = elemType.typeWrapperJava7(); } 

    String nme = typ.getName(); 
    if (nme.equals("Set"))
    { String tname = "HashSet"; 
      if (typ.isSorted()) 
      { tname = "TreeSet"; } 
      return tname + "<" + et + ">"; 
    } 
    if (nme.equals("Sequence"))
    { return "ArrayList<" + et + ">"; } 
    if (nme.equals("Map"))
    { return "HashMap<String, " + et + ">"; } 
    if (typ.values == null)
    { return typ.typeWrapperJava7(); }
    return "Integer"; 
  }

  public String typeWrapper()  // for Java4
  { String nme = getName(); 
    if (isEntity()) { return nme; } 
    if ("Set".equals(nme) || "Sequence".equals(nme)) { return "List"; } 
    if ("Map".equals(nme)) { return "Map"; } 
    if ("int".equals(nme)) { return "Integer"; } 
    if ("double".equals(nme)) { return "Double"; } 
    if ("long".equals(nme)) { return "Long"; } 
    if ("boolean".equals(nme)) { return "Boolean"; } 
    if (values != null) { return "Integer"; } 
    return nme; 
  } 

  public String typeWrapperJava6()  // for Java6
  { String nme = getName(); 
    if (isEntity()) { return nme; } 
    if ("Set".equals(nme)) { return "HashSet"; } 
    if ("Sequence".equals(nme)) { return "ArrayList"; } 
    if ("Map".equals(nme)) { return "HashMap"; } 
    if ("int".equals(nme)) { return "Integer"; } 
    if ("double".equals(nme)) { return "Double"; } 
    if ("long".equals(nme)) { return "Long"; } 
    if ("boolean".equals(nme)) { return "Boolean"; } 
    if (values != null) { return "Integer"; } 
    return nme; 
  } 

  public String typeWrapperJava7()  // for Java7
  { String nme = getName(); 
    if (isEntity()) { return nme; } 

    if ("Set".equals(nme)) 
    { String tname = "HashSet"; 
      if (sorted) 
      { tname = "TreeSet"; } 
      if (elementType != null) 
      { return tname + "<" + elementType.typeWrapperJava7() + ">"; }
      else 
      { return tname + "<Object>"; }
    } 

    if ("Sequence".equals(nme)) 
    { if (elementType != null) 
      { return "ArrayList<" + elementType.typeWrapperJava7() + ">"; }
      else 
      { return "ArrayList<Object>"; }
    } 

    if ("Map".equals(nme)) 
    { if (elementType != null) 
      { return "HashMap<String, " + elementType.typeWrapperJava7() + ">"; }
      else 
      { return "HashMap<String, Object>"; }
    } 

    if ("int".equals(nme)) { return "Integer"; } 
    if ("double".equals(nme)) { return "Double"; } 
    if ("long".equals(nme)) { return "Long"; } 
    if ("boolean".equals(nme)) { return "Boolean"; } 
    if (values != null) { return "Integer"; } 
    return nme; 
  } 

  /* public void asTextModel(PrintWriter out) 
  { String nme = getName(); 
    out.println(nme + " : Type"); 
    out.println(nme + ".name = \"" + nme + "\""); 
  } */ 

  public void generateDeclaration(PrintWriter out)
  { if (values != null)
    { for (int i = 0; i < values.size(); i++)
      { out.println("  public static final int " 
                   + values.get(i) + " = " + i + ";");
      }
      out.println();
    }
  }

  public void generateDeclarationCSharp(PrintWriter out)
  { if (values != null)
    { out.print("  enum " + getName() + " { "); 
      for (int i = 0; i < values.size(); i++)
      { out.print(values.get(i)); 
        if (i < values.size() - 1) { out.print(", "); } 
      }
      out.println(" }\n");
    }
  }

  public void generateDeclarationCPP(PrintWriter out)
  { if (values != null)
    { out.print("  enum " + getName() + " { "); 
      for (int i = 0; i < values.size(); i++)
      { out.print(values.get(i)); 
        if (i < values.size() - 1) { out.print(", "); } 
      }
      out.println(" };\n");
    }
  }

  public static Type getTypeFor(String typ, Vector types, Vector entities)
  { if (typ == null) { return null; } 

    if ("int".equals(typ) || "double".equals(typ) || "boolean".equals(typ) ||
        "long".equals(typ) || "String".equals(typ))
    { return new Type(typ,null); } 

    if (typ.equals("Sequence") || "Set".equals(typ) || "Map".equals(typ))
    { return new Type(typ,null); } 
  
    Entity ent = (Entity) ModelElement.lookupByName(typ,entities); 
    if (ent != null) 
    { return new Type(ent); } 

    Type res = (Type) ModelElement.lookupByName(typ,types); 
    if (res != null) 
    { return res; } 
    
    if (typ.startsWith("Set(") && typ.endsWith(")"))
    { String nt = typ.substring(4,typ.length()-1);
      Type innerT = getTypeFor(nt, types, entities); 
      Type resT = new Type("Set",null); 
      resT.setElementType(innerT); 
      return resT; 
    }   

    if (typ.startsWith("Sequence(") && typ.endsWith(")"))
    { String nt = typ.substring(9,typ.length()-1);
      Type innerT = getTypeFor(nt, types, entities); 
      Type resT = new Type("Sequence",null); 
      resT.setElementType(innerT); 
      return resT; 
    }   

    if (typ.startsWith("Map(String,") && typ.endsWith(")"))
    { String nt = typ.substring(11,typ.length()-1);
      Type innerT = getTypeFor(nt, types, entities); 
      Type resT = new Type("Map",null);
      resT.setKeyType(new Type("String", null));  
      resT.setElementType(innerT); 
      return resT; 
    }   

    return null; 
  } // and cases for Set(t), Sequence(t), Map(A,B)

  public static Type determineType(Vector exps)
  { Type expectedType = null;
    for (int j = 0; j < exps.size(); j++)
    { Expression be = (Expression) exps.get(j);
      Type t = be.getType();
      if (t == null) { }
      else if (expectedType == null)
      { expectedType = t; }
      else if (expectedType.equals(t)) { }
      else
      { String tn1 = expectedType.getName();
        String tn2 = t.getName();
        if (tn1.equals("double") && (tn2.equals("int") || tn2.equals("long")))
        { }
        else if (tn1.equals("long") && tn2.equals("int"))
        { }
        else if (tn2.equals("double") && (tn1.equals("int") || tn1.equals("long")))
        { expectedType = t; }
        else if (tn2.equals("long") && tn1.equals("int"))
        { expectedType = t; }
        else 
        { Entity e1 = expectedType.getEntity(); 
          Entity e2 = t.getEntity(); 
          if (e1 != null && e2 != null)
          { if (e1 == e2) { } 
            else 
            { Entity e = Entity.commonSuperclass(e1,e2); 
              expectedType = new Type(e); 
            } // could be null
          }
          else // one is a class and other isn't or both are invalid
          { return null; }
        }
      }
    }
    return expectedType;
  }

  public static Type mostSpecificType(Type t1, Type t2) 
  { if (t1 == null) { return t2; } 
    if (t2 == null) { return t1; } 
    String t1name = t1.getName(); 
    String t2name = t2.getName(); 
    if (t1name.equals(t2name))  
    { if ("Set".equals(t1name) || "Sequence".equals(t1name) || "Map".equals(t1name))
      { Type et1 = t1.getElementType(); 
        Type et2 = t2.getElementType(); 
        Type et = mostSpecificType(et1, et2); 
        if (et == et1) { return t1; } 
        else { return t2; } 
      } 
      return t1; 
    } 

    if (t1.values != null) { return t1; } 
    if (t2.values != null) { return t2; } 
    if (t1name.equals("int") && t2name.equals("double")) { return t1; } 
    if (t2name.equals("int") && t1name.equals("double")) { return t2; } 
    if (t1name.equals("int") && t2name.equals("long")) { return t1; } 
    if (t2name.equals("int") && t1name.equals("long")) { return t2; }
    if (t1name.equals("long") && t2name.equals("double")) { return t1; } 
    if (t2name.equals("long") && t1name.equals("double")) { return t2; }  
    if (t1.isEntity() && t2.isEntity())
    { Entity e1 = t1.getEntity(); 
      Entity e2 = t2.getEntity(); 
      if (Entity.isAncestor(e1,e2)) { return t2; } 
      if (Entity.isAncestor(e2,e1)) { return t1; } 
    } 
    System.err.println("Cannot determine most-specific type of: " + t1 + " " + t2); 
    return t1; 
  } 

  public static Type mostGeneralType(Type t1, Type t2) 
  { if (t1 == null) { return t2; } 
    if (t2 == null) { return t1; } 
    String t1name = t1.getName(); 
    String t2name = t2.getName(); 

    if (t1name.equals(t2name))  
    { if ("Set".equals(t1name) || "Sequence".equals(t1name) || "Map".equals(t1name))
      { Type et1 = t1.getElementType(); 
        Type et2 = t2.getElementType(); 
        Type et = mostGeneralType(et1, et2); 
        if (et == et1) { return t1; } 
        else { return t2; } 
      } 
      return t1; 
    } 

    if (t1.values != null) { return t2; } 
    if (t2.values != null) { return t1; } 
    if (t1name.equals("int") && t2name.equals("double")) { return t2; } 
    if (t2name.equals("int") && t1name.equals("double")) { return t1; } 
    if (t1name.equals("int") && t2name.equals("long")) { return t2; } 
    if (t2name.equals("int") && t1name.equals("long")) { return t1; }
    if (t1name.equals("long") && t2name.equals("double")) { return t2; } 
    if (t2name.equals("long") && t1name.equals("double")) { return t1; }  
    if (t1.isEntity() && t2.isEntity())
    { Entity e1 = t1.getEntity(); 
      Entity e2 = t2.getEntity(); 
      if (Entity.isAncestor(e1,e2)) { return t1; } 
      if (Entity.isAncestor(e2,e1)) { return t2; } 
    } 
    System.err.println("Cannot determine most-general type of: " + t1 + " " + t2); 
    return t1; 
  } 

  public String saveData()
  { String res = ""; 
    if (values == null)
    { return "\n"; } 
    for (int i = 0; i < values.size(); i++)
    { res = res + values.get(i) + " "; }
    return res; 
  }

  public String toXml()
  { return "  <UML:Datatype name=\"" + getName() +
           "\"/>\n";
  }

  public String toString()
  { String nme = getName(); 
    if ("Set".equals(nme) || "Sequence".equals(nme))
    { if (elementType != null) 
      { return nme + "(" + elementType + ")"; } 
    } 

    if ("Map".equals(nme))
    { return nme + "(" + keyType + ", " + elementType + ")"; } 

    return nme; 
  } 

  public static Type composedType(Vector properties) 
  { int n = properties.size(); 

    if (n == 0) { return null; } 

    if (properties.size() == 1) 
    { Attribute prop = (Attribute) properties.get(0); 
      return prop.getType(); 
    } 
     
    
    Attribute p = (Attribute) properties.get(n-1); 
    Vector frontprops = new Vector(); 
    frontprops.addAll(properties); 
    frontprops.remove(p); 
    Type ct = Type.composedType(frontprops); 
    return ct.composeTypes(p.getType()); 
  } 

  public Type composeTypes(Type t)
  { // The type of a.b if a.type = this, b.type = t
    String tname = t.getName(); 
    String nme = getName(); 

    Type res = null; 

    if ("Set".equals(tname))
    { if ("Set".equals(nme) || "Sequence".equals(nme))
      { res = new Type("Set",null); 
        res.setElementType(t.getElementType()); 
        return res; 
      } 
      else 
      { return t; } 
    } 

    if ("Sequence".equals(tname))
    { if ("Set".equals(nme))
      { res = new Type("Set",null); 
        res.setElementType(t.getElementType()); 
        return res; 
      } 
      else  
      { return t; } 
    } 

    if ("Set".equals(nme))
    { res = new Type("Set",null); 
      res.setElementType(t); 
      return res; 
    } 
    else if ("Sequence".equals(nme))
    { res = new Type("Sequence",null); 
      res.setElementType(t); 
      return res; 
    }   
    else 
    { return t; } 
    
  } 
        
  public static int composeMultiplicities(Vector atts, String bound)
  { int res = 1; 
    for (int i = 0; i < atts.size(); i++) 
    { Attribute att = (Attribute) atts.get(i); 
      if ("upper".equals(bound))
      { res = res*att.getUpper(); } 
      else 
      { res = res*att.getLower(); } 
    } 
    return res; 
  } 

  public String cg(CGSpec cgs)
  { String typetext = this + "";
    Vector args = new Vector();
    if (isCollectionType())
    { args.add(elementType + ""); }
    else
    { args.add(typetext); }

    CGRule r = cgs.matchedTypeUseRule(this,typetext);
    if (r != null)
    { return r.applyRule(args); }
    return typetext;
  }


  public static void main(String[] args) 
  { /* Boolean b = new Boolean(true); 
    System.out.println(b.booleanValue()); 
    System.out.println(getTypeFor("Set(Sequence(int))",new Vector(),new Vector()));

    Type t1 = new Type("Sequence",null); t1.setElementType(new Type("int", null)); 
    Type t2 = new Type("Sequence",null); t2.setElementType(new Type("long", null)); 
    System.out.println(mostSpecificType(t1, t2));  */

    Vector v1 = new Vector(); 
    v1.add("a"); v1.add("b"); v1.add("c"); v1.add("d"); 
    Vector v2 = new Vector(); 
    v2.add("c"); v2.add("a"); v2.add("d"); 
 
    Type t1 = new Type("T1", v1); 
    Type t2 = new Type("T2", v2); 
    System.out.println(Type.enumSimilarity(t1,t2)); 
  } 
}
