import java.util.Vector; 
import java.io.*; 
import java.util.StringTokenizer; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
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
  Type elementType = null; // this is the range type of a map

  // Expression arrayBound = null; 
  boolean sorted = false;  // for collections 

  Type keyType = null;  // for maps and functions -- by default this is String for a map
  // Note below the assumption that this is String, also in the code generators.

  // cached type correspondence for MT synthesis: 
  static java.util.Map simMap = new java.util.HashMap(); 

  Type alias = null;  // For datatypes 

  static java.util.Map exceptions2java = new java.util.HashMap(); 
  static 
  { exceptions2java.put("OclException", "Throwable"); 
    exceptions2java.put("ProgramException", "Exception"); 
    // also, "RuntimeException"
    exceptions2java.put("SystemException", "Error"); 
    exceptions2java.put("IOException", "IOException"); 
    exceptions2java.put("CastingException", "ClassCastException"); 
    exceptions2java.put("NullAccessException", "NullPointerException"); 
    exceptions2java.put("IndexingException", "IndexOutOfBoundsException");
    // also "ArrayStoreException"
    exceptions2java.put("ArithmeticException", "ArithmeticException");
    exceptions2java.put("IncorrectElementException", "InputMismatchException");
    // Also, "NoSuchElementException"
    exceptions2java.put("AssertionException", "AssertionError");
    exceptions2java.put("AccessingException", "IllegalAccessException");
    // also, "NoClassDefFoundError" ?? 
  } 

  static java.util.Map exceptions2csharp = new java.util.HashMap(); 
  static 
  { exceptions2csharp.put("OclException", "Exception"); 
    exceptions2csharp.put("ProgramException", "Exception"); 
    // also, "RuntimeException"
    exceptions2csharp.put("SystemException", "SystemException"); 
    exceptions2csharp.put("IOException", "IOException"); 
    exceptions2csharp.put("CastingException", "InvalidCastException"); 
    exceptions2csharp.put("NullAccessException", "NullReferenceException"); 
    exceptions2csharp.put("IndexingException", "IndexOutOfRangeException");
    // also "ArrayStoreException"
    exceptions2csharp.put("ArithmeticException", "ArithmeticException");
    // also "DivideByZeroException
    exceptions2csharp.put("IncorrectElementException", "ArgumentException");
    // Also, "NoSuchElementException"
    exceptions2csharp.put("AssertionException", "Exception");
    exceptions2csharp.put("AccessingException", "AccessViolationException");
    // also, "NoClassDefFoundError" ?? 
  } 

  static java.util.Map exceptions2cpp = new java.util.HashMap(); 
  static 
  { exceptions2cpp.put("OclException", "exception"); 
    exceptions2cpp.put("ProgramException", "logic_error"); 
    // also, "runtime_error"
    exceptions2cpp.put("SystemException", "system_error"); 
    exceptions2cpp.put("IOException", "io_exception");
        // user-defined exception 
    exceptions2cpp.put("CastingException", "bad_cast"); 
    exceptions2cpp.put("NullAccessException", "null_access_exception"); 
    exceptions2cpp.put("IndexingException", "out_of_range");
    
    exceptions2cpp.put("ArithmeticException", "arithmetic_exception");
    // user-defined 

    // also "DivideByZeroException
    exceptions2cpp.put("IncorrectElementException", "invalid_argument");
    // Also, "range_error" for incorrect values
    exceptions2cpp.put("AssertionException", "assertion_exception");

    exceptions2cpp.put("AccessingException", "accessing_exception");
    // User-defined exception

    // also, "NoClassDefFoundError" ?? 
  } 

  static java.util.Map cexceptions = 
     new java.util.HashMap(); 
  static 
  { cexceptions.put("SIGABRT", "AssertionException"); 
    cexceptions.put("SIGFPE", "ArithmeticException"); 
    cexceptions.put("SIGILL", "SystemException"); 
    cexceptions.put("SIGINT", "IOException"); 
    cexceptions.put("SIGSEGV", "AccessingException"); 
    cexceptions.put("SIGTERM", "OclException"); 
  } 


   
  public Type(String nme, Vector vals)
  { super(nme);
    if ("Map".equals(nme))
    { keyType = new Type("String", null); } 
    if ("Boolean".equalsIgnoreCase(nme))
    { setName("boolean"); } 
    else if ("Integer".equalsIgnoreCase(nme))
    { setName("long"); } 
    else if ("Real".equalsIgnoreCase(nme))
    { setName("double"); } 
    values = vals;
  } // also convert EInt, ELong, etc to our types

  public Type(String nme, Type arg, Type range)
  { super(nme);
    keyType = arg; 
    elementType = range;
  } // for Map and Function

  public Type(Entity e)
  { this(e + "",null); 
    isEntity = true; 
    entity = e; 
    elementType = this; 
    // elementType = new Type(e); 
  } 

  public boolean isEnumeratedType() 
  { return values != null; } 

  // public boolean isEnumeration() 
  // { return values != null; } 

  public boolean isClassEntityType() 
  { return entity != null && !(entity.isStruct()); } 

  public boolean isStructEntityType() 
  { return entity != null && entity.isStruct(); } 

  public boolean isEntityType() 
  { return entity != null; } 

  public boolean isEntityType(Vector ents) 
  { if (entity != null)
    { return true; } 
    Entity ex = (Entity) ModelElement.lookupByName(name, ents);
    if (ex != null) 
    { return true; } 
    if ("OclType".equals(name) || "OclAny".equals(name) ||
        "OclProcess".equals(name) ||  
        "OclFile".equals(name) || "OclRandom".equals(name))
    { return true; } 
    return false; 
 } 

  public boolean isSetType() 
  { return "Set".equals(name); } 

  public boolean isSequenceType() 
  { return "Sequence".equals(name); } 

  public boolean isMapType() 
  { return "Map".equals(name); } 

  public boolean isFunctionType() 
  { return "Function".equals(name); } 

  public boolean isVoidType() 
  { return "void".equals(name); } 

  public boolean isSet() 
  { return "Set".equals(name); } 

  public boolean isSequence() 
  { return "Sequence".equals(name); } 

  public boolean isMap() 
  { return "Map".equals(name); } 

  public boolean isFunction() 
  { return "Function".equals(name); } 

  public boolean isVoid() 
  { return "void".equals(name); } 

  public static boolean isOclExceptionType(Expression expr)
  { Type t = expr.getType(); 
    if (t == null) 
    { return false; } 
    String nme = t.getName(); 
    if (exceptions2java.get(nme) != null) 
    { return true; } 
    return false; 
  } 

  public static boolean isOclExceptionType(String nme)
  { if (exceptions2java.get(nme) != null) 
    { return true; } 
    return false; 
  } 

  public boolean isOclException()
  { String nme = getName(); 
    if (exceptions2java.get(nme) != null) 
    { return true; } 
    return false; 
  } 

  public static String getOCLExceptionForC(String sig)
  { return (String) cexceptions.get(sig); } 

  public Type getType()
  { return new Type("OclType", null); } 

  public void setType(Type t) 
  { } 

  public void addParameter(Attribute p)
  { } 

  public Vector getParameters()
  { return new Vector(); } 

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
 
    result.setAlias(alias); 

    return result; 
  } 

  public void setElementType(Type e)
  { elementType = e; } 

  public void setInnerElementType(Type e)
  { if ("Function".equals(name) || "Ref".equals(name) || 
        "Sequence".equals(name) || "Set".equals(name) || 
        "Map".equals(name))
    { if (elementType == null) 
      { elementType = e; } 
      else if ("OclAny".equals(elementType.getName()))
      { elementType = e; } 
      else if ("void".equals(elementType.getName()))
      { elementType = e; } 
      else 
      { elementType.setInnerElementType(e); } 
    } 
    else 
    { elementType = e; } 
  } 

  public Type getElementType()
  { return elementType; } 

  public Type getInnerElementType()
  { if ("Function".equals(name) || "Ref".equals(name) || 
        "Sequence".equals(name) || "Set".equals(name) || 
        "Map".equals(name))
    { if (elementType != null) 
      { return elementType.getInnerElementType(); } 
      else 
      { return elementType; } 
    } 
    else 
    { return this; } 
  } 

  public void setKeyType(Type e)
  { keyType = e; } 

  public Type getKeyType()
  { return keyType; } 

  public void setSorted(boolean s)
  { sorted = s; } 

  public boolean isSorted()
  { return sorted; } 

  public void setAlias(Type a)
  { alias = a; } 

  public Type getAlias()
  { return alias; } 

  public Entity getEntity() 
  { return entity; } 

  public static Type correctElementType(Type typ, Type et, 
                              Vector types, Vector entities)
  { System.out.println(">> Type = " + typ + " Element type = " + et); 

    if (et == null && typ != null)
    { String tname = typ + ""; 
      if ((tname.startsWith("Sequence") || 
           tname.startsWith("Set")) && 
          tname.indexOf("(") > 0)
      { Type realtype = Type.getTypeFor(tname, types, entities); 
        System.out.println(">> Real type = " + realtype + " (" + realtype.elementType + ")"); 

        if (realtype != null) 
        { typ.name = realtype.getName(); 
          return realtype.getElementType();  
        } 
      }
      else if (typ.elementType != null) 
      { return typ.elementType; }  
    } 
    return et; 
  } 
  
  public Entity getEntityOrElementEntity() 
  { if (entity != null) 
    { return entity; }
	if (elementType != null && elementType.isEntity())
	{ return elementType.entity; }
	return null; 
  }

  public Vector metavariables()
  { Vector res = new Vector(); 
    if (name.startsWith("_"))
    { res.add(name); } 
    if ("Sequence".equals(name) && elementType != null)
    { return elementType.metavariables(); } 
    else if ("Set".equals(name) && elementType != null)
    { return elementType.metavariables(); } 
    else if ("Ref".equals(name) && elementType != null)
    { return elementType.metavariables(); } 
    else if ("Map".equals(name) || "Function".equals(name))
    { Vector vars = new Vector(); 
	  if (keyType != null) 
	  { vars.addAll(keyType.metavariables()); } 
	  if (elementType != null) 
      { vars.addAll(elementType.metavariables()); } 
      return vars;  
    } 
    return res; 
  } 

  public int complexity() 
  { if ("Sequence".equals(name) && elementType != null)
    { return 1 + elementType.complexity(); } 

    if ("Set".equals(name) && elementType != null)
    { return 1 + elementType.complexity(); } 

    if ("Map".equals(name) && elementType != null && keyType != null)
    { return 1 + elementType.complexity() + keyType.complexity(); } 

    if ("Function".equals(name) && elementType != null && keyType != null)
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

  public static boolean isFunctionType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return "Function".equals(nme); 
  } 

  public static boolean isExceptionType(Type t)
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return exceptions2java.get(nme) != null;
  }

  public static boolean isExceptionType(String t)
  { return exceptions2java.get(t) != null; }

  public boolean isReference()
  { return ("Ref".equals(name)); } 

  public static boolean isReferenceType(Type t)
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return "Ref".equals(nme);
  }

  public boolean isRef()
  { return ("Ref".equals(name)); } 

  public boolean isNestedReference()
  { if ("Ref".equals(name) && 
        elementType == null)
    { return true; }

    if ("int".equals(name) || "double".equals(name) ||
        "String".equals(name) || "long".equals(name) ||
        "boolean".equals(name) || isEntity() || 
        values != null) 
    { return false; } 

    if (elementType == null)
    { return false; } 

    String etname = elementType.getName(); 

    if ("Ref".equals(name) && 
        "void".equals(etname))
    { return true; } 

    if ("Ref".equals(name) && 
        "OclAny".equals(etname))
    { return true; } 

    return elementType.isNestedReference(); 
  } 

  public static Type replaceInnerType(Type t, Type r)
  { String tname = t.getName(); 
    Type et = t.getElementType(); 

    if ("Ref".equals(tname) && 
        et == null)
    { return r; }

    if ("int".equals(tname) || "double".equals(tname) ||
        "String".equals(tname) || "long".equals(tname) ||
        "boolean".equals(tname) || t.isEntity() || 
        t.values != null) 
    { return t; } 

    if (et == null)
    { return t; } 

    String etname = et.getName(); 

    if ("Ref".equals(tname) && 
        "void".equals(etname))
    { return r; } 

    if ("Ref".equals(tname) && 
        "OclAny".equals(etname))
    { return r; } 

    Type ret = Type.replaceInnerType(et,r);
    Type res = new Type(tname, null); 
    res.setElementType(ret); 
    return res;  
  } 

  public boolean isCollection()
  { return isCollectionType(this); } 

  public static boolean isCollectionType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return ("Sequence".equals(nme) || "Set".equals(nme) || "Map".equals(nme)); 
  } // But really, should exclude Map from this. 

  public static boolean isRefType(Type t) 
  { if (t == null) { return false; } 
    String nme = t.getName(); 
    return ("Ref".equals(nme)); 
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

  public static boolean isIntegerType(Type t)
  { if (t == null) { return false; } 
    String tname = t.getName(); 
    if ("int".equals(tname) || "long".equals(tname))
    { return true; } 
    return false; 
  } 

  public boolean isIntegerType()
  { if ("int".equals(name) || "long".equals(name))
    { return true; } 
    return false; 
  } 

  public boolean isStringType()
  { if ("String".equals(name))
    { return true; } 
    return false; 
  } 

  public boolean isRealType()
  { if ("double".equals(name))
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

    if (t1name.equals("Map") && t2name.equals("Map"))
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

    if (t1name.equals("Map") && t2name.equals("Map"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      return Type.isSubType(et1,et2,mm,entities); 
    } 

    return false; 
  } 

  public static boolean isSpecialisedOrEqualType(Type t1, Type t2) 
  { // Assignment  v2 := v1  does not need a conversion, 
    // otherwise should be  v2 := v1->oclAsType(t2)

    if (t1 == null || t2 == null) 
    { return false; } 
    
    String t1name = t1.getName(); 
    String t2name = t2.getName(); 
    
    if (t1.isEntity() && t2.isEntity()) 
    { Entity tsrc = t1.getEntity(); 
      Entity ttrg = t2.getEntity();
       
      if (Entity.isAncestor(ttrg,tsrc))
      { return true; } 
      else 
      { return t1name.equals(t2name); }
    }  

    if (t2.getName().equals("double"))
    { if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      if (t1.getName().equals("double")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("long"))
    { if (t1.getName().equals("int")) { return true; } 
      if (t1.getName().equals("long")) { return true; } 
      return false; 
    } 

    if (t2.getName().equals("int"))
    { if (t1.getName().equals("int")) { return true; } 
      return false; 
    } 

    if (t1name.equals("Sequence") && t2name.equals("Sequence"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      if (et1 == null) 
      { return et2 == null; } 
      if (et2 == null) 
      { return true; } 
      return et1.getName().equals(et2.getName()); 
    } 

    if (t1name.equals("Set") && t2name.equals("Sequence"))
    { return false; } 

    if (t1name.equals("Set") && t2name.equals("Set"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      if (et1 == null) 
      { return et2 == null; } 
      if (et2 == null) 
      { return true; } 
      return et1.getName().equals(et2.getName()); 
    } 

    if (t1name.equals("Map") && t2name.equals("Map"))
    { Type et1 = t1.getElementType(); 
      Type et2 = t2.getElementType(); 
      if (et1 == null) 
      { return et2 == null; } 
      if (et2 == null) 
      { return true; } 
      return et1.getName().equals(et2.getName()); 
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
    
  public static BehaviouralFeature booleanEnumConversionFunctionETL(Type e, String srcnme) 
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

      Type restype = new Type("MM2!" + nme,null);
      Type btype = new Type("Boolean",null);  
      Vector pars = new Vector(); 
      Attribute p = new Attribute("self",btype); 
      // pars.add(p); 
      BehaviouralFeature bf = new BehaviouralFeature("boolean2" + nme + srcnme,pars,true,restype);
      bf.setOwner(new Entity("Boolean"));  
      ReturnStatement assignbest = 
        new ReturnStatement(new BasicExpression("MM2!" + nme + "#" + bestval)); 
      ReturnStatement assignworst = 
        new ReturnStatement(new BasicExpression("MM2!" + nme + "#" + worstval)); 
      BinaryExpression vtrue = 
        new BinaryExpression("=",new BasicExpression(p),new BasicExpression(true)); 
      ConditionalStatement cs = new ConditionalStatement(vtrue,assignbest,assignworst); 
      bf.setActivity(cs); 
      return bf; 
    } 
    return null; 
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

  public static BehaviouralFeature enumBooleanConversionFunctionETL(Type e, String trgnme) 
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

      Type restype = new Type("MM2!" + nme,null);
      Type btype = new Type("Boolean",null);  
      Vector pars = new Vector(); 
      Attribute p = new Attribute("self",restype); 
      // pars.add(p); 
      BehaviouralFeature bf = new BehaviouralFeature(nme + "2boolean" + trgnme,pars,true,btype); 
      bf.setOwner(new Entity("MM2!" + nme)); 
      ReturnStatement returntrue = new ReturnStatement(new BasicExpression(true)); 
      ReturnStatement returnfalse = new ReturnStatement(new BasicExpression(false)); 
      BinaryExpression isbest = 
        new BinaryExpression("=",new BasicExpression(p),new BasicExpression("#" + bestval)); 
      ConditionalStatement cs = new ConditionalStatement(isbest,returntrue,returnfalse); 
      bf.setActivity(cs); 
      return bf; 
    } 
    return null; 
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

  public static BehaviouralFeature enumStringConversionFunctionETL(Type e1)
  { String res = "  helper def: " + e1.getName() + "2String" + 
                 "(s : MM1!" + e1.getName() + ") : String =\n    "; 
    String restail = ""; 

    Vector pars = new Vector(); 
    Type restype = new Type("String", null); 
    BehaviouralFeature bf = new BehaviouralFeature("" + e1.getName() + "2String", 
                                                   pars,true,restype); 
    bf.setOwner(new Entity("MM1!" + e1.getName())); 
    
    Vector values1 = e1.getValues(); 
    int s1 = values1.size(); 

    String lastval = (String) values1.get(s1 - 1); 
    BasicExpression lastvale = new BasicExpression("\"" + lastval + "\""); 
    Statement code = new ReturnStatement(lastvale); 
 
    for (int j = 0; j < s1-1; j++) 
    { String val = (String) values1.get(j); 
      BasicExpression vale = new BasicExpression("\"" + val + "\""); 
      code = new ConditionalStatement(new BasicExpression("self = #" + val), 
                                      new ReturnStatement(vale),code); 
    } 
    bf.setActivity(code); 
    return bf; 
  } 

  public static BehaviouralFeature stringEnumConversionFunctionETL(Type e1)
  { String res = "  helper def: String2" + e1.getName() + 
                 "(s : String) : MM2!" + e1.getName() + " =\n    "; 

    Vector pars = new Vector(); 
    Type restype = new Type("MM2!" + e1.getName(), null); 
    BehaviouralFeature bf = new BehaviouralFeature("String2" + e1.getName(), 
                                                   pars,true,restype); 
    bf.setOwner(new Entity("String")); // HACK

    String name1 = e1.getName(); 
    
    Vector values1 = e1.getValues(); 
    int s1 = values1.size(); 

    String lastval = (String) values1.get(s1 - 1); 
    BasicExpression lastvale = new BasicExpression("MM2!" + name1 + "#" + lastval); 
    Statement code = new ReturnStatement(lastvale); 
 
    for (int j = 0; j < s1-1; j++) 
    { String val = (String) values1.get(j); 
      BasicExpression vale = new BasicExpression("MM2!" + name1 + "#" + val); 
      code = new ConditionalStatement(new BasicExpression("self = \"" + val + "\""), 
                                      new ReturnStatement(vale),code); 
    } 
    bf.setActivity(code); 
    return bf; 
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
    if (isFunctionType(this)) { return "null"; }
    if (alias != null)    // For datatypes
    { return alias.initialValueJava6(); } 
   
    return "0"; 
  } 

  public String initialValueJava7()  // not used? 
  { if (isSequenceType(this))
    { return "new ArrayList<" + elementType.typeWrapperJava7() + ">()"; } 
    if (isSetType(this))
    { return "new HashSet<" + elementType.typeWrapperJava7() + ">()"; } 
    // Take account of sortedness

    if (isMapType(this))
    { return "new HashMap<String, " + elementType.typeWrapperJava7() + ">()"; } 
    if ("boolean".equals(getName())) { return "false"; }
    if (isEntity) { return "null"; }  
    if ("String".equals(getName())) { return "\"\""; } 
    if (isFunctionType(this)) { return "null"; }  
    if (alias != null)    // For datatypes
    { return alias.initialValueJava7(); } 
    return "0"; 
  } 

  public String getJava8Definition(String packageName) 
  { if (values == null) 
    { return ""; } 

    String res = "package " + packageName + ";\n\r" + 
                 "\n\r" + 
                 "public enum " + name + " { ";
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      res = res + val; 
      if (i < values.size() - 1)
      { res = res + ", "; } 
    } 
    return res + " }\n\r\n\r"; 
  }  
   
  public String getSwiftDefinition(String packageName) 
  { String res = // "package " + packageName + ";\n\r" + 
                 // "\n\r" + 
                 "enum " + name + " : String { ";
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      res = res + "  case " + val; 
      if (i < values.size() - 1)
      { res = res + "\n "; } 
    } 
    return res + " }\n\r\n\r"; 
  }  

  public static String nsValueOf(Attribute att) 
  { String attname = att.getName(); 
    Type t = att.getType(); 
    if (t == null) 
    { return "_x." + attname; } 

    String tname = t.getName(); 
    if ("String".equals(tname))
    { return "_x." + attname + " as NSString"; } 
    else if (t.isNumeric())
    { return "NSNumber(value: _x." + attname + ")"; } 
    else
    { return "NSString(string: _x." + attname + ")"; } 
  } 

  public static String nsOptionalValueOf(Attribute att) 
  { String attname = att.getName(); 
    Type t = att.getType(); 
    if (t == null) 
    { return "_x." + attname + "!"; } 

    String tname = t.getName(); 
    if ("String".equals(tname))
    { return "_x." + attname + "! as NSString"; } 
    else if (t.isNumeric())
    { return "NSNumber(value: _x." + attname + "!)"; } 
    else
    { return "NSString(string: _x." + attname + "!)"; } 
  } 


  public boolean isEnumeration()
  { return (values != null); } 

  public boolean isBoolean()
  { return "boolean".equals(getName()); } 

  public boolean isString()
  { return "String".equals(getName()); } 

  public boolean isNumeric()
  { String nme = getName();
    if ("int".equals(nme))
    { return true; } 
    if ("long".equals(nme))
    { return true; } 
    if ("double".equals(nme))
    { return true; }
    return false;  
  } 

  public boolean isInteger()
  { String nme = getName();
    if ("int".equals(nme))
    { return true; } 
    if ("long".equals(nme))
    { return true; } 
    return false;  
  } 

  public boolean isInt()
  { String nme = getName();
    if ("int".equals(nme))
    { return true; } 
    return false;  
  } 

  public boolean isLong()
  { String nme = getName();
    if ("long".equals(nme))
    { return true; } 
    return false;  
  } 

  public boolean isReal()
  { String nme = getName();
    if ("double".equals(nme))
    { return true; } 
    return false;  
  } 

  public boolean isEntity()
  { return isEntity; } 

  public boolean isEntity(Vector ents)
  { if (isEntity)
    { return true; }

    Entity ent = (Entity) ModelElement.lookupByName(getName(), ents); 
    if (ent != null) 
    { return true; } 
    return false; 
  }  

  public Entity getEntity(Vector ents)
  { if (isEntity)
    { return entity; }

    Entity ent = (Entity) ModelElement.lookupByName(getName(), ents); 
    if (ent != null) 
    { return ent; } 
    return null; 
  }  

  public boolean isAbstractEntity()
  { return entity != null && entity.isAbstract(); } 

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

  public boolean hasValue(Expression st)
  { if (values == null) 
    { return false; } 
    return values.contains(st + ""); 
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

  public static BehaviouralFeature enumConversionFunctionETL(Type e1, Type e2, Vector thesaurus)
  { String res = "  helper def: convert" + e1.getName() + "_" + e2.getName() + 
                 "(s : MM1!" + e1.getName() + ") : MM2!" + e2.getName() + " =\n    "; 
    String restail = ""; 

    Vector pars = new Vector(); 
    Type restype = new Type("MM2!" + e2.getName(), null); 
    BehaviouralFeature bf = new BehaviouralFeature("convert" + e1.getName() + "_" + e2.getName(), 
                                                   pars,true,restype); 
    bf.setOwner(new Entity("MM1!" + e1.getName())); 

    String name1 = e1.getName().toLowerCase(); 
    String name2 = e2.getName().toLowerCase();
    
    Vector values1 = e1.getValues(); 
    Vector values2 = e2.getValues(); 
    int s1 = values1.size(); 
    int s2 = values2.size(); 

    String lastval = (String) values1.get(values1.size() - 1); 
    double bestscore1 = 0;
    String besttarget1 = values2.get(0) + ""; 
 
    for (int j = 0; j < values2.size(); j++) 
    { String valt = (String) values2.get(j); 
      double namesim = ModelElement.similarity(lastval,valt); 
      double namesemsim = Entity.nmsSimilarity(lastval,valt, thesaurus); 
      double nsim = (namesim + namesemsim - namesim*namesemsim); 
      if (nsim > bestscore1) 
      { bestscore1 = nsim; 
        besttarget1 = valt; 
      } 
    } 

    Statement code; 
    if (bestscore1 > 0) 
    { code = new ReturnStatement(new BasicExpression("#" + besttarget1)); } 
    else 
    { code = new SequenceStatement(); } // should not occur

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
      { code = new ConditionalStatement(new BasicExpression("self = #" + val), 
                                        new ReturnStatement(new BasicExpression("#" + besttarget)), 
                                        code); 
      }       
    } 

    bf.setActivity(code); 
    return bf; 
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

  public static boolean isDatatype(String nme, Vector types)
  { if (nme.equals("int") || nme.equals("String") ||  
        nme.equals("double") || nme.equals("boolean") ||
        nme.equals("long"))
    { return true; }
    Type tt = (Type) ModelElement.lookupByName(nme, types); 
    if (tt == null) 
    { return false; } 
    return true; 
  } 

  public boolean isDatatype()
  { return values == null && alias != null; } 
     
  public boolean isPrimitive()
  { String nme = getName();
    if (nme.equals("int") || 
        nme.equals("double") || nme.equals("boolean") ||
        nme.equals("long") || values != null)
    { return true; }
    return false;
  } 

  public static boolean isPrimitiveType(Type t)
  { if (t == null) { return false; } 
    String nme = t.getName();
    if (nme.equals("int") || nme.equals("double") || 
        nme.equals("boolean") ||
        nme.equals("long") || t.values != null)
    { return true; }
    return false;
  } 

  public static boolean isBasicType(Type e)
  { if (e == null) 
    { return false; } 
    if ("String".equals(e.getName()))
    { return true; } 
    return e.isPrimitive(); 
  } 

  public static boolean isClassifierType(Type e)
  { if (e == null) 
    { return false; } 
    String estr = e + ""; 
    if ("String".equals(estr) || "OclDate".equals(estr) ||
        "OclAny".equals(estr) || "OclType".equals(estr) || 
        "OclProcess".equals(estr) || "OclFile".equals(estr) ||
        "OclRandom".equals(estr))
    { return true; } 
    return false; 
  } 

  public boolean isValueType()
  { if (isBasicType(this)) 
    { return true; }
 
    if ("Sequence".equals(name) && elementType != null)
    { return elementType.isValueType(); } 
    else if ("Set".equals(name) && elementType != null)
    { return elementType.isValueType(); } 

    return false; 
  } 

  public static boolean isValueType(Type t) 
  { if (t == null) 
    { return false; } 
    return t.isValueType(); 
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
    if ((nme.equals("Map") || nme.equals("Function")) && elementType != null) 
    { return elementType.typeMultiplicity(); } 

    return ModelElement.ONE;
  }  

  public boolean isMultiple()
  { String nme = getName(); 
    if (nme.equals("Set") || nme.equals("Sequence"))
    { return true; }  
    if (nme.equals("Map") || nme.equals("Function"))
    { return elementType != null && elementType.isMultiple(); } // ie., the application result is multiple
    return false; 
  } // Shouldn't Maps be multiple anyway? 

  public boolean isParsable()
  { String nme = getName(); 
    if ("String".equals(nme) || "boolean".equals(nme) ||
        "int".equals(nme) || "long".equals(nme) || 
        "double".equals(nme))
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
    if (nme.equals("boolean")) { return "Boolean.parseBoolean(" + data + ")"; } 
    return data; 
  } 

  public String dataExtractionCodeIOS(String data)
  { String nme = getName(); 
    if (nme.equals("String")) 
    { return data; } 
    if (nme.equals("int")) { return "Int(" + data + ")"; } 
    else if (nme.equals("long")) { return "Long(" + data + ")"; } 
    else if (nme.equals("double")) { return "Double(" + data + ")"; } 
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

  public String collectionExtractionCodeIOS(String ex, String aname, String data)
  { String nme = elementType.getName();
    String extractValue = "_x"; // data + "[_i]"; 

    if (nme.equals("int")) 
    { extractValue = "Int(" + extractValue + ")"; } 
    else if (nme.equals("long")) 
    { extractValue = "Long(" + extractValue + ")"; } 
    else if (nme.equals("double")) 
    { extractValue = "Double(" + extractValue + ")"; }
 
    String res = "for (_,_x) in " + data + ".enumerated()\n" + 
             "    { " + ex + "." + aname + ".append(" + extractValue + ") }\n";   
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

    if (nme.equals("Map") || nme.equals("Function"))
    { String kt = "String"; 
      if (keyType != null) 
      { kt = keyType.getUMLName(); } 
      String et = "OclAny"; 
      if (elementType != null) 
      { et = elementType.getUMLName(); } 
      return nme + "(" + kt + ", " + et + ")"; 
    } 

    return nme; 
  } 

  public String getUMLModelName(PrintWriter out)
  { String nme = getName(); 
    if (nme.equals("int")) 
    { return "Integer"; } 
    if (nme.equals("long")) 
    { return "Long"; } 
    if (nme.equals("double")) 
    { return "Real"; } 
    if (nme.equals("boolean")) 
    { return "Boolean"; } 
	
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
  
      if (keyType != null) 
      { String etype = keyType.getUMLModelName(out); 
        out.println("SetType" + tid + ".keyType = " + etype); 
      } 
      else 
      { out.println("SetType" + tid + ".keyType = void"); } 

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

      if (keyType != null) 
      { String ktype = keyType.getUMLModelName(out); 
        out.println("SequenceType" + tid + ".keyType = " + ktype); 
      } 
      else 
      { out.println("SequenceType" + tid + ".keyType = void"); } 

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
      { out.println("MapType" + tid + ".elementType = String"); } 

      if (keyType != null) 
      { String ktype = keyType.getUMLModelName(out); 
        out.println("MapType" + tid + ".keyType = " + ktype); 
      } 
      else 
      { out.println("MapType" + tid + ".keyType = String"); } 
      // assume key type is String
	  
      out.println("MapType" + tid + ".typeId = \"" + tid + "\""); 
      return "MapType" + tid; 
    } 
    else if (nme.equals("Function"))
    { String tid = Identifier.nextIdentifier(""); 
      out.println("FunctionType" + tid + " : CollectionType"); 
      out.println("FunctionType" + tid + ".name = \"Function\""); 

      if (elementType != null) 
      { String etype = elementType.getUMLModelName(out); 
        out.println("FunctionType" + tid + ".elementType = " + etype); 
      } 
      else 
      { out.println("FunctionType" + tid + ".elementType = OclAny"); } 
      // assume key type is String

      if (keyType != null) 
      { String ktype = keyType.getUMLModelName(out); 
        out.println("FunctionType" + tid + ".keyType = " + ktype); 
      } 
      else 
      { out.println("FunctionType" + tid + ".keyType = String"); } 

      out.println("FunctionType" + tid + ".typeId = \"" + tid + "\""); 
      return "FunctionType" + tid; 
    }

    if (values == null && alias != null)  
    { return alias.getUMLModelName(out); } 

    return nme; 
  } // Function types for the future. 

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

    if (etype.endsWith("String"))
    { return new Type("String",null); } 
    if (etype.endsWith("EInt") || etype.endsWith("Integer"))
    { return new Type("int",null); } 
    if (etype.endsWith("ELong"))
    { return new Type("long",null); } 
    if (etype.endsWith("EDouble") || etype.endsWith("Real"))
    { return new Type("double",null); } 
    if (etype.endsWith("Boolean"))
    { return new Type("boolean",null); }

    String et = "";
    if (etype.startsWith("#/"))
    { int x = etype.lastIndexOf("/"); 
      et = etype.substring(x,etype.length()); 
    }

    Type t =
       (Type) ModelElement.lookupByName(et,types);

    if (t == null) 
    { return new Type("String",null); } 

    return t;  
  } 

  public String getKM3()
  { String res = ""; 
    if (values == null)  // for basic types only
    { if (alias != null) 
      { res = "  datatype " + name + " = " + alias.getName() + ";\n"; } 
      else 
      { res = "  datatype " + name + ";\n"; } 

      return res; 
    } 
    
    res = "  enumeration " + name + " {\n";
    
    for (int i = 0; i < values.size(); i++) 
    { String val = (String) values.get(i); 
      res = res + "    literal " + val + ";\n"; 
    } 
    res = res + "  }\n";
    return res;  
  } 

    
  public void saveKM3(PrintWriter out)
  { String res = ""; 
    if (values == null)  // for basic types only
    { if (alias != null) 
      { out.println("  datatype " + name + " = " + alias.getName() + ";\n"); } 
      else 
      { out.println("  datatype " + name + ";"); }
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

    if (tname.equals("Map") || tname.equals("Function")) { return false; } 

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
    
	if (tname.equals("Map") || tname.equals("Function")) 
	{ return false; } 

    if (t.getEntity() != null)  { return false; } 
    return true; // enum
  }

  public static boolean isExtendedAttributeType(Type t, Type elemT) 
  { if (t == null) // need to get type for atts
    { return false; }

    if (t.values != null) 
    { return true; }     // enumeration

    String tname = t.getName();
    if (tname.equals("int") || tname.equals("double") || tname.equals("String") || 
        tname.equals("long") || tname.equals("boolean"))
    { return true; } 

    if (tname.equals("Set") || tname.equals("Sequence"))
    { if (elemT != null && elemT.isEntity())
      { return false; }
      if (elemT != null && isExtendedAttributeType(elemT,null))
      { return true; } 
      return false; 
    } 
    
    if (tname.equals("Map") || tname.equals("Function")) 
    { return false; } 

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
      if (nme.equals("int") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("double"))
      { return "0.0"; } 
      if (nme.equals("Set") || nme.equals("Sequence"))
      { return "new Vector()"; }
      if (nme.equals("Map")) 
      { return "new HashMap()"; } 
      if (alias != null)    // For datatypes
      { return alias.getDefault(); } 

      return "null";    // for class types, functions, OclAny
    }

    if (values.size() > 0)
    { return (String) values.get(0); } 

    return "null";
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
      else if (nme.equals("double"))
      { return new BasicExpression(0.0); }
      else if (nme.equals("int") || nme.equals("long"))
      { return new BasicExpression(0); }
      else if (nme.equals("Set") || 
               nme.equals("Sequence") || 
               nme.equals("Map"))
      { res = new SetExpression();
        if (nme.equals("Sequence"))
        { ((SetExpression) res).setOrdered(true); }
        res.setType(this); 
        res.setElementType(elemt); 
      }
      else if ("OclAny".equals(nme))
      { res = new BasicExpression(0); }
      else if ("OclType".equals(nme))
      { res = new UnaryExpression("->oclType", 
                    new BasicExpression(0)); 
        res.setType(this); 
        res.setElementType(elemt); 
      }
      else if (isEntity() ||  
               "OclDate".equals(nme) || 
               "OclFile".equals(nme) || 
               "OclVoid".equals(nme) || 
               "OclProcess".equals(nme) || 
               "OclRandom".equals(nme) || 
               "OclIterator".equals(nme))
      { res = new BasicExpression("null"); 
        res.setType(this); 
        res.setElementType(elemt); 
      } 
      else if (nme.equals("Function"))
      { // lambda x : keyType in elementType.defaultValueExpression()
        Expression elementDefault = null; 
        if (elementType != null) 
        { elementDefault = elementType.getDefaultValueExpression(); }
        else 
        { elementDefault = new BasicExpression("null"); }
		 
        res = new UnaryExpression("lambda", elementDefault); 
        ((UnaryExpression) res).setAccumulator(new Attribute("_x", keyType, ModelElement.INTERNAL));  
        return res; 
      }
      else if (alias != null)    // For datatypes
      { return alias.getDefaultValueExpression(); } 
      else // unknown type
      { res = new BasicExpression("null"); } 
    }
    else // values != null
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
      if (nme.equals("int") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("double"))
      { return "0.0"; }
      if (nme.equals("Set"))
      { return "new HashSet()"; } 
      if (nme.equals("Sequence"))
      { return "new ArrayList()"; }
      if (nme.equals("Map"))
      { return "new HashMap()"; } 
      if (alias != null)    // For datatypes
      { return alias.getDefaultJava6(); } 
      return "null";    // for class types, functions
    }
    else if (values.size() > 0) 
    { return (String) values.get(0); } 
    return "null"; 
  }
  // Set and Sequence?

  public String getDefaultJava7()
  { if (values == null) // so not enumerated
    { String nme = getName();
      if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("double"))
      { return "0.0"; }
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
      if (alias != null)    // For datatypes
      { return alias.getDefaultJava7(); } 
      return "null";    // for class types, functions
    } 
    else if (values.size() > 0) 
    { return (String) values.get(0); } 
    return "null"; 
  }
  // sorted sets?

  public String getDefaultCSharp()
  { String nme = getName();
      
    if (values == null) // so not enumerated
    { if (nme.equals("String"))
      { return "\"\""; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("double"))
      { return "0.0"; }
      if (nme.equals("Set") || nme.equals("Sequence"))
      { return "new ArrayList()"; }
      if (nme.equals("Map"))
      { return "new Hashtable()"; }
      if (alias != null)    // For datatypes
      { return alias.getDefaultCSharp(); } 

      return "null";    // for classes, functions, Ref, OclAny
    }
    return nme + "." + values.get(0);
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
      { return "string(\"\")"; }
      if (nme.equals("boolean"))
      { return "false"; }
      if (nme.equals("int") || nme.equals("long"))
      { return "0"; }
      if (nme.equals("double"))
      { return "0.0"; }

      if (nme.equals("Set"))
      { return "new std::set<" + et + ">()"; } 
      if (nme.equals("Sequence"))
      { return "new vector<" + et + ">()"; }
      if (nme.equals("Map"))
      { return "new map<string, " + et + ">()"; }
      if (alias != null)    // For datatypes
      { return alias.getDefaultCPP(et); } 
      return "NULL";    // for classes, Ref, OclAny, functions
    }
    return getName() + "(0)"; // (String) values.get(0);
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

  public static String resolveTypeParametersCSharp(Vector tpars, Vector fpars, Vector apars)
  { String res = "<"; 
    for (int i = 0; i < tpars.size(); i++) 
    { Type typ = (Type) tpars.get(i); 
      String tpname = typ.getName(); // T is just an ident
      boolean found = false; 

      for (int j = 0; j < fpars.size() && !found; j++) 
      { Attribute fpar = (Attribute) fpars.get(j); 
        Expression apar = (Expression) apars.get(j); 

        if (tpname.equals(fpar.getType() + ""))
        { res = res + apar.getType().getCSharp();
          found = true; 
        } 
        else if (fpar.isSequence() || fpar.isSet())
        { Type fpelemtype = fpar.getElementType(); 
          Type apelemtype = apar.getElementType(); 
          if (tpname.equals(fpelemtype + ""))
          { res = res + apelemtype.getCSharp(); 
            found = true; 
          } 
        } 
      }

      if (!found)
      { res = res + "object"; } 

      if (i < tpars.size()-1)
      { res = res + ","; }  
    }

    return res + ">"; 
  } 


  public static String resolveTypeParametersCPP(Vector tpars, Vector fpars, Vector apars)
  { String res = "<"; 
    for (int i = 0; i < tpars.size(); i++) 
    { Type typ = (Type) tpars.get(i); 
      String tpname = typ.getName(); // T is just an ident
      boolean found = false; 

      for (int j = 0; j < fpars.size() && !found; j++) 
      { Attribute fpar = (Attribute) fpars.get(j); 
        Expression apar = (Expression) apars.get(j); 

        if (tpname.equals(fpar.getType() + ""))
        { res = res + apar.getType().getCPP();
          found = true; 
        } 
        else if (fpar.isSequence() || fpar.isSet())
        { Type fpelemtype = fpar.getElementType(); 
          Type apelemtype = apar.getElementType(); 
          if (tpname.equals(fpelemtype + ""))
          { res = res + apelemtype.getCPP(); 
            found = true; 
          } 
        } 
      }

      if (!found)
      { res = res + "void*"; } 

      if (i < tpars.size()-1)
      { res = res + ","; }  
    }

    return res + ">"; 
  } 



  public String getCSharp()  
  { String nme = getName();

    if (nme.equals("Ref"))
    { String restype = "object"; 
      if (elementType != null) 
      { restype = elementType.getCSharp();
        if (isBasicType(elementType) ||
            elementType.isStructEntityType() ||  
            "Ref".equals(elementType.getName()) || 
            "void".equals(elementType.getName()))
        { return restype + "*"; } 
        else 
        { return restype; }
      }  
      else 
      { return "void*"; } 
    } 

    if (nme.equals("Set") || nme.equals("Sequence"))
    { return "ArrayList"; }

    if (nme.equals("Map"))
    { return "Hashtable"; }

    if (nme.equals("Function"))
    { String key = "object"; 
      if (keyType != null) 
      { key = keyType.getCSharp(); }
      String restype = "object"; 
      if (elementType != null) 
      { restype = elementType.getCSharp(); } 

      return "Func<" + key + "," + restype + ">"; 
    }

    if (nme.equals("String")) { return "string"; }  
    if (nme.equals("boolean")) { return "bool"; } 
    if (nme.equals("long")) { return "long"; }

    if (alias != null)    // For datatypes
    { return alias.getCSharp(); } 
 
    if (nme.equals("OclDate"))
    { return "DateTime"; } 
    if (nme.equals("OclAny"))
    { return "object"; } 
    if (nme.equals("OclType"))
    { return "OclType"; } 
    if (nme.equals("OclRandom"))
    { return "OclRandom"; } 
    if (nme.equals("OclIterator"))
    { return "OclIterator"; } 
    if (nme.equals("OclProcess"))
    { return "OclProcess"; } 
    if (nme.equals("OclFile"))
    { return "OclFile"; } 

    String jex = (String) exceptions2csharp.get(nme); 
    if (jex != null) 
    { return jex; } 

    if (entity != null && 
        entity.isGeneric())
    { String res = nme + "<"; 
      Vector v = entity.getTypeParameters(); 
      for (int i = 0; i < v.size(); i++) 
      { Type tt = (Type) v.get(i); 
        res = res + tt.getName(); 
        if (i < v.size() - 1)
        { res = res + ","; }
      }
      res = res + ">"; 
      return res; 
    } 

    return nme; 
  } 

  public String getSwift(String elemType)
  { String nme = getName();
    if (nme.equals("Set")) 
    { return "Set<" + elemType + ">"; } 
    if (nme.equals("Sequence"))
    { return "[" + elemType + "]"; } 
    
    if (nme.equals("Map"))
    { String kt = "String"; 
      if (keyType != null) 
      { kt = keyType.getSwift(""); }
      return "Dictionary<" + kt + ", " + elemType + ">"; 
    } 

    if (nme.equals("Function"))
    { String kt = "String"; 
      if (keyType != null) 
      { kt = keyType.getSwift(""); }
      return "(" + kt + ") -> " + elemType; 
    } 
	
    if (nme.equals("String")) { return "String"; }  
    if (nme.equals("boolean")) { return "Bool"; } 
    if (nme.equals("int")) { return "Int"; } 
    if (nme.equals("long")) { return "Int"; } 
    if (nme.equals("double")) { return "Double"; } 
    if (isEntity) { return nme; } 
    if (alias != null)    // For datatypes
    { return alias.getSwift(); } 

    if (nme.equals("OclAny"))
    { return "Any"; } 
    if (nme.equals("OclType"))
    { return "Any.Type"; } 

    return nme;  // enumerations, OclIterator
  } 

  public String getSwift()
  { String nme = getName();
    if (nme.equals("String")) { return "String"; }  
    if (nme.equals("boolean")) { return "Bool"; } 
    if (nme.equals("int")) { return "Int"; } 
    if (nme.equals("long")) { return "Int64"; } 
    if (nme.equals("double")) { return "Double"; } 
    if (isEntity) { return nme; } 
    if (alias != null)    // For datatypes
    { return alias.getSwift(); } 
    if (isEnumeration()) { return nme; }

    String elemType = "Any"; 
    if (elementType != null) 
    { elemType = elementType.getSwift(); }
  
    if (nme.equals("Set")) 
    { return "Set<" + elemType + ">"; } 
    if (nme.equals("Sequence"))
    { return "[" + elemType + "]"; } 
    if (nme.equals("Map"))
    { return "Dictionary<String, " + elemType + ">"; } 
    if (nme.equals("Function"))
    { return "(String) -> " + elemType; } 

    if (nme.equals("OclAny"))
    { return "Any"; } 
    if (nme.equals("OclType"))
    { return "Any.Type"; } 

    return nme; 
  } 
    
  public String getSwiftDefaultValue()
  { String nme = getName();
    if (nme.equals("String")) { return "\"\""; }  
    if (nme.equals("boolean")) { return "false"; } 
    if (nme.equals("int")) { return "0"; } 
    if (nme.equals("long")) { return "0"; } 
    if (nme.equals("double")) 
    { return "0.0"; } 

    if (alias != null)    // For datatypes
    { return alias.getSwiftDefaultValue(); } 

    if (nme.equals("WebDisplay")) 
    { return "WebDisplay.defaultInstance()"; } 
    else if (nme.equals("ImageDisplay"))
    { return "ImageDisplay.defaultInstance()"; } 
    else if (nme.equals("GraphDisplay"))
    { return "GraphDisplay.defaultInstance()"; } 

    if (isEntity || "OclAny".equals(nme) || "OclType".equals(nme) || 
        "OclVoid".equals(nme) || 
        "OclIterator".equals(nme)) 
    { return "nil"; } 

    if (isEnumeration()) 
    { return nme + "." + values.get(0); }

    String elemType = "Any"; 
    if (elementType != null) 
    { elemType = elementType.getSwift(); }

    if (nme.equals("Set")) 
    { return "Set<" + elemType + ">()"; } 
    if (nme.equals("Sequence"))
    { return "[]()"; } 
    if (nme.equals("Map"))
    { return "Dictionary<String, " + elemType + ">()"; } 
    return "nil"; 
  } 

  public String getCPP(String elemType)
  { String nme = getName();
    if (nme.equals("Ref"))
    { return elemType + "*"; } 
    if (nme.equals("Set")) 
    { return "std::set<" + elemType + ">*"; } 
    if (nme.equals("Sequence"))
    { return "vector<" + elemType + ">*"; } 
    if (nme.equals("Map"))
    { return "map<string, " + elemType + ">*"; } 
    if (nme.equals("String")) { return "string"; }  
    if (nme.equals("boolean")) { return "bool"; } 
    if (nme.equals("Function"))
    { return "function<" + elemType + "(string)>"; } 
    if (isEntity) 
    { if (entity.genericParameter)
      { return nme; } 
      if (entity.isGeneric())
      { String res = nme + "<"; 
        Vector v = entity.getTypeParameters(); 
        for (int i = 0; i < v.size(); i++) 
        { Type tt = (Type) v.get(i); 
          res = res + tt.getName(); 
          if (i < v.size() - 1)
          { res = res + ","; }
        }
        res = res + ">*"; 
        return res; 
      } 
      return nme + "*"; 
    } 

    if (alias != null)    // For datatypes
    { return alias.getCPP(elemType); } 

    if (nme.equals("OclAny"))
    { return "void*"; } 
    if (nme.equals("OclType"))
    { return "OclType*"; } 
    if (nme.equals("OclRandom"))
    { return "OclRandom*"; } 
    if (nme.equals("OclDate"))
    { return "OclDate*"; } 
    if (nme.equals("OclProcess"))
    { return "OclProcess*"; } 
    if (nme.equals("OclFile"))
    { return "OclFile*"; } 
    if (nme.equals("OclIterator"))
    { return "OclIterator*"; } 


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
    if (nme.equals("Set") || nme.equals("Sequence") ||
        nme.equals("Ref") ||
        nme.equals("Map") || nme.equals("Function"))
    { return "void*"; }
    if (nme.equals("String")) 
    { return "string"; }  
    if (nme.equals("boolean")) 
    { return "bool"; } 
    if (alias != null)    // For datatypes
    { return alias.getCPP(); } 
 
    String jex = (String) exceptions2cpp.get(nme); 
    if (jex != null) 
    { return jex; } 

    if (nme.equals("OclDate"))
    { return "OclDate*"; } 
    if (nme.equals("OclAny"))
    { return "void*"; } 
    if (nme.equals("OclType"))
    { return "OclType*"; } 
    if (nme.equals("OclRandom"))
    { return "OclRandom*"; } 
    if (nme.equals("OclProcess"))
    { return "OclProcess*"; } 
    if (nme.equals("OclFile"))
    { return "OclFile*"; } 
    if (nme.equals("OclIterator"))
    { return "OclIterator*"; } 

    if (isEntity) 
    { if (entity.genericParameter) 
      { return nme; } 
      if (entity.isGeneric())
      { String res = nme + "<"; 
        Vector v = entity.getTypeParameters(); 
        for (int i = 0; i < v.size(); i++) 
        { Type tt = (Type) v.get(i); 
          res = res + tt.getName(); 
          if (i < v.size() - 1)
          { res = res + ","; }
        }
        res = res + ">*"; 
        return res; 
      } 

      return nme + "*"; 
    }

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
    else if (alias != null) 
    { String aname = alias.getUMLName(); 
      String nme = getName(); 
      out.println(nme + " : PrimitiveType"); 
      out.println(aname + " : " + nme + ".alias"); 
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
	 
    if (nme.equals("Map") || nme.equals("Function"))
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
    if (nme.equals("Map") || nme.equals("Function"))
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

    if (nme.equals("Function"))
    { if (keyType != null && elementType != null)
      { return "Evaluation<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elementType != null) 
      { return "Evaluation<String, " + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "Evaluation<String, String>"; } 
    } 

    if (alias != null)    // For datatypes
    { return alias.getJava(); } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclType"))
    { return "Class"; } 

    String jex = (String) exceptions2java.get(nme); 
    if (jex != null) 
    { return jex; } 

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

    if (nme.equals("Function"))
    { if (keyType != null && elementType != null)
      { return "Evaluation<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elementType != null) 
      { return "Evaluation<String, " + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "Evaluation<String, String>"; } 
    } 

    if (alias != null)    // For datatypes
    { return alias.getJava6(); } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclAny"))
    { return "Class"; } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    String jex = (String) exceptions2java.get(nme); 
    if (jex != null) 
    { return jex; } 

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
    { if (keyType != null && elementType != null) 
      { return "HashMap<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "HashMap"; } 
    } 

    if (nme.equals("Function"))
    { if (keyType != null && elementType != null) 
      { return "Evaluation<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else 
      { return "Evaluation<String,Object>"; } 
    } 

    if (alias != null)    // For datatypes
    { return alias.getJava7(); } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclType"))
    { return "Class"; } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    String jex = (String) exceptions2java.get(nme); 
    if (jex != null) 
    { return jex; } 

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
    { if (keyType != null && elementType != null) 
      { return "HashMap<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elemType != null) 
      { return "HashMap<String, " + elemType.typeWrapperJava7() + ">"; } 
      else 
      { return "HashMap"; }
    }  

    if (nme.equals("Function"))
    { if (keyType != null && elementType != null) 
      { return "Evaluation<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elemType != null) 
      { return "Evaluation<String, " + elemType.typeWrapperJava7() + ">"; } 
      else 
      { return "Evaluation<String,Object>"; } 
    } 

    if (alias != null)    // For datatypes
    { return alias.getJava7(); } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    if (nme.equals("OclType"))
    { return "Class"; } 

    if (values == null)
    { return nme; }
    // if (nme.equals("long")) { return "long"; } 

    return "int"; 
  }

  public static String getJava7Type(Type typ, Type elemType)
  { String et = "Object"; 
    if (elemType != null) 
    { et = elemType.typeWrapperJava7(); } 

    String kt = "String"; 
    // if (keyType != null) 
    // { kt = keyType.typeWrapperJava7(); } 

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
    { return "HashMap<" + kt + ", " + et + ">"; } 

    if (nme.equals("Function"))
    { return "Evaluation<" + kt + ", " + et + ">"; } 

    if (typ.alias != null)    // For datatypes
    { return typ.alias.typeWrapperJava7(); } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclType"))
    { return "Class"; } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    if (typ.values == null)
    { return typ.typeWrapperJava7(); }
    return "Integer"; 
  }

  public String getJava8()
  { String nme = getName();
    // String et = ""; 
    // if (elementType != null && elementType != this) 
    // { et = elementType.getJava7(); } 
    // else 
    // { et = "Object"; } 

    if (nme.equals("Set"))
    { String tname = "HashSet"; 
      
      if (elementType != null) 
      { return tname + "<" + elementType.typeWrapperJava8() + ">"; } 
      else 
      { return tname; } 
    } 

    if (nme.equals("Sequence"))
    { if (elementType != null) 
      { return "ArrayList<" + elementType.typeWrapperJava8() + ">"; } 
      else 
      { return "ArrayList"; } 
    } 

    if (nme.equals("Map"))
    { if (keyType != null && elementType != null) 
      { return "HashMap<" + keyType.typeWrapperJava8() + ", " + elementType.typeWrapperJava8() + ">"; } 
      else if (elementType != null) 
      { return "HashMap<String, " + elementType.typeWrapperJava8() + ">"; } 
      else 
      { return "HashMap"; } 
    } 

    if (nme.equals("Function"))
    { if (keyType != null && elementType != null) 
      { return "Evaluation<" + keyType.typeWrapperJava8() + ", " + elementType.typeWrapperJava8() + ">"; } 
      else if (elementType != null) 
      { return "Evaluation<String, " + elementType.typeWrapperJava8() + ">"; } 
      else 
      { return "Evaluation<String,Object>"; } 
    } 

    if (nme.equals("OclAny"))
    { return "Object"; } 

    if (nme.equals("OclType"))
    { return "Class"; } 

    if (nme.equals("OclDate"))
    { return "Date"; } 

    String jex = (String) exceptions2java.get(nme); 
    if (jex != null) 
    { return jex; } 

    if (alias != null)    // For datatypes
    { return alias.typeWrapperJava8(); } 

    return nme;  
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
    if (alias != null)    // For datatypes
    { return alias.typeWrapper(); } 
    if (nme.equals("OclAny"))
    { return "Object"; } 
    if (nme.equals("OclType"))
    { return "Class"; } 
    if (nme.equals("OclDate"))
    { return "Date"; } 

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
    if (alias != null)    // For datatypes
    { return alias.typeWrapperJava6(); } 
    if (nme.equals("OclAny"))
    { return "Object"; } 
    if (nme.equals("OclType"))
    { return "Class"; } 
    if (nme.equals("OclDate"))
    { return "Date"; } 

    if (values != null) { return "Integer"; } 
    return nme; 
  } 

  public static String typeWrapperJava(Type t)
  { if (t == null) 
    { return "Object"; } 
    else 
    { return t.typeWrapperJava7(); } 
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
    { if (keyType != null && elementType != null) 
      { return "HashMap<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elementType != null) 
      { return "HashMap<String, " + elementType.typeWrapperJava7() + ">"; }
      else 
      { return "HashMap<String, Object>"; }
    } 

    if ("Function".equals(nme)) 
    { if (keyType != null && elementType != null) 
      { return "Evaluation<" + keyType.typeWrapperJava7() + ", " + elementType.typeWrapperJava7() + ">"; } 
      else if (elementType != null) 
      { return "Evaluation<String, " + elementType.typeWrapperJava7() + ">"; }
      else 
      { return "Evaluation<String, Object>"; }
    } 

    if ("int".equals(nme)) { return "Integer"; } 
    if ("double".equals(nme)) { return "Double"; } 
    if ("long".equals(nme)) { return "Long"; } 
    if ("boolean".equals(nme)) { return "Boolean"; }
    if (alias != null)    // For datatypes
    { return alias.typeWrapperJava7(); } 
    if (nme.equals("OclAny"))
    { return "Object"; } 
    if (nme.equals("OclType"))
    { return "Class"; } 
    if (nme.equals("OclDate"))
    { return "Date"; } 
 
    if (values != null) { return "Integer"; } 
    return nme; 
  } // For enumerations, would be better to represent as Java enums. 
  

  public String typeWrapperJava8()  // for Java8
  { String nme = getName(); 
    if (isEntity()) { return nme; } 

    if ("Set".equals(nme)) 
    { String tname = "HashSet"; 
      // if (sorted) 
      // { tname = "TreeSet"; } 
      if (elementType != null) 
      { return tname + "<" + elementType.typeWrapperJava8() + ">"; }
      else 
      { return tname + "<Object>"; }
    } 

    if ("Sequence".equals(nme)) 
    { if (elementType != null) 
      { return "ArrayList<" + elementType.typeWrapperJava8() + ">"; }
      else 
      { return "ArrayList<Object>"; }
    } 

    if ("Map".equals(nme)) 
    { if (keyType != null && elementType != null) 
      { return "HashMap<" + keyType.typeWrapperJava8() + ", " + elementType.typeWrapperJava8() + ">"; } 
      else if (elementType != null) 
      { return "HashMap<String, " + elementType.typeWrapperJava8() + ">"; }
      else 
      { return "HashMap<String, Object>"; }
    } 

    if ("Function".equals(nme)) 
    { if (keyType != null && elementType != null) 
      { return "Evaluation<" + keyType.typeWrapperJava8() + ", " + elementType.typeWrapperJava8() + ">"; } 
      else if (elementType != null) 
      { return "Evaluation<String, " + elementType.typeWrapperJava8() + ">"; }
      else 
      { return "Evaluation<String, Object>"; }
    } 

    if ("int".equals(nme)) { return "Integer"; } 
    if ("double".equals(nme)) { return "Double"; } 
    if ("long".equals(nme)) { return "Long"; } 
    if ("boolean".equals(nme)) { return "Boolean"; } 

    if (alias != null)    // For datatypes
    { return alias.typeWrapperJava8(); } 
    if (nme.equals("OclAny"))
    { return "Object"; } 
    if (nme.equals("OclType"))
    { return "Class"; } 
    if (nme.equals("OclDate"))
    { return "Date"; } 
 
    return nme; 
  } // For enumerations, would be better to represent as Java enums. 
  
  /* public void asTextModel(PrintWriter out) 
  { String nme = getName(); 
    out.println(nme + " : Type"); 
    out.println(nme + ".name = \"" + nme + "\""); 
  } */ 

  public void generateDeclaration(PrintWriter out)
  { String nme = getName(); 

    if (values == null)
    { return; } 

    for (int i = 0; i < values.size(); i++)
    { out.println("  public static final int " 
                   + values.get(i) + " = " + i + ";");
    }
	
    out.println();
    out.println(); 
    out.println("  public static int parse" + nme + "(String _x) {"); 
    for (int i = 0; i < values.size(); i++) 
    { out.println("    if (\"" + values.get(i) + "\".equals(_x)) { return " + i + "; }"); }
    out.println("    return 0;"); 
    out.println("  }"); 
    out.println(); 
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
        if (i < values.size() - 1) 
        { out.print(", "); } 
      }
      out.println(" };\n");
    }
    else if (alias != null) 
    { out.println("  typedef " + alias.getCPP() + " " + getName() + ";"); } 
  }

  public static boolean isOCLExceptionType(String typ) 
  { if ("OclException".equals(typ) || 
        "IndexingException".equals(typ) || 
        "AccessingException".equals(typ) || 
        "AssertionException".equals(typ) || 
        "ArithmeticException".equals(typ) || 
        "IncorrectElementException".equals(typ) || 
        "IOException".equals(typ) || 
        "CastingException".equals(typ) || 
        "NullAccessException".equals(typ) || 
        "ProgramException".equals(typ) || 
        "SystemException".equals(typ))
    { return true; } 
    return false; 
  } 

  public static Type typeCheck(Type t, Vector types, Vector entities)
  { // instantiate t by any generic types
    if (t == null) 
    { return new Type("OclAny", null); } 
    String tname = t + "";
    return Type.getTypeFor(tname, types, entities); 
  }  
    

  public static Type getTypeFor(String typ)
  { Vector typs = new Vector(); 
    Vector ents = new Vector(); 
    return getTypeFor(typ, typs, ents); 
  } 

  public static Type getTypeFor(String typ, Vector types, Vector entities)
  { if (typ == null) { return null; } 

    if ("int".equals(typ) || "double".equals(typ) || 
        "boolean".equals(typ) ||
        "OclAny".equals(typ) || "OclType".equals(typ) ||
        "OclFile".equals(typ) || "OclAttribute".equals(typ) || 
        "OclOperation".equals(typ) || 
        "OclException".equals(typ) ||
        Type.isOCLExceptionType(typ) ||   
        "OclProcess".equals(typ) || "OclRandom".equals(typ) || 
        "OclIterator".equals(typ) || "OclDate".equals(typ) || 
        "long".equals(typ) || "String".equals(typ))
    { return new Type(typ,null); } 

    if (typ.equals("Sequence") || "Set".equals(typ) || 
        "Map".equals(typ) || "Function".equals(typ) ||
        "Ref".equals(typ))
    { return new Type(typ,null); } 
  
    Entity ent = (Entity) ModelElement.lookupByName(typ,entities); 
    if (ent != null) 
    { return new Type(ent); } 

    Type res = (Type) ModelElement.lookupByName(typ,types); 
    if (res != null) 
    { return res; } 

    if (typ.startsWith("Ref(") && typ.endsWith(")"))
    { String nt = typ.substring(4,typ.length()-1);
      Type innerT = getTypeFor(nt, types, entities); 
      Type resT = new Type("Ref",null); 
      resT.setElementType(innerT); 
      return resT; 
    }   
    
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

    if (typ.startsWith("Map(") && typ.endsWith(")"))
    { for (int i = 4; i < typ.length(); i++) 
      { if (",".equals(typ.charAt(i) + ""))
        { String nt = typ.substring(4,i);
          Type innerT = getTypeFor(nt, types, entities);
          String rt = typ.substring(i+1,typ.length()-1);
          Type restT = getTypeFor(rt, types, entities); 
          if (innerT != null && restT != null) 
          { Type resT = new Type("Map",null);
            resT.setKeyType(innerT);  
            resT.setElementType(restT); 
            return resT; 
          } 
        }
      }
    }   

    if (typ.startsWith("Function(String,") && typ.endsWith(")"))
    { String nt = typ.substring(16,typ.length()-1);
      Type innerT = getTypeFor(nt, types, entities); 
      Type resT = new Type("Function",null);
      resT.setKeyType(new Type("String", null));  
      resT.setElementType(innerT); 
      return resT; 
    }   

    if (typ.startsWith("Function(") && typ.endsWith(")"))
    { for (int i = 9; i < typ.length(); i++) 
	  { if (",".equals(typ.charAt(i) + ""))
	    { String nt = typ.substring(9,i);
          Type innerT = getTypeFor(nt, types, entities);
          String rt = typ.substring(i+1,typ.length()-1);
          Type restT = getTypeFor(rt, types, entities); 
          if (innerT != null && restT != null) 
          { Type resT = new Type("Function",null);
            resT.setKeyType(innerT);  
            resT.setElementType(restT); 
            return resT; 
          } 
        }
      }
    }   

    return null; 
  } // and cases for Set(t), Sequence(t), Map(A,B)

  public static Type determineType(Vector exps)
  { Type expectedType = null;
    for (int j = 0; j < exps.size(); j++)
    { Expression be = (Expression) exps.get(j);
      Type t = be.getType();

      System.out.println(">> Type of " + be + " = " + t); 

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

  public static Type refineType(Type oldType, Type t)
  { if (t == null) 
    { return oldType; }
    
    if (oldType == null)
    { return t; }
    
    if (oldType.equals(t)) 
    { return oldType; }
    
    String tn1 = oldType.getName();
    String tn2 = t.getName();
        
    if (tn1.equals("double") && (tn2.equals("int") || tn2.equals("long")))
    { return oldType; }

    if (tn1.equals("long") && tn2.equals("int"))
    { return oldType; }

    if (tn2.equals("double") && (tn1.equals("int") || tn1.equals("long")))
    { return oldType; }
    
    if (tn2.equals("long") && tn1.equals("int"))
    { return t; }
    
    Entity e1 = oldType.getEntity(); 
    Entity e2 = t.getEntity(); 
    if (e1 != null && e2 != null)
    { if (e1 == e2) 
      { return oldType; } 
      
      Entity e = Entity.commonSuperclass(e1,e2); 
      if (e == null) 
      { System.err.println("! Warning: incompatible element types " + e1 + " " + e2);
        return oldType;
      }

      return new Type(e);
    }

    System.err.println("! Warning: unexpected combination of types in a collection: " + oldType + " " + t); 

    return oldType;
  }


  public static Type mostSpecificType(Type t1, Type t2) 
  { if (t1 == null) { return t2; } 
    if (t2 == null) { return t1; } 
    String t1name = t1.getName(); 
    String t2name = t2.getName(); 
    
    if (t1name.equals("OclAny"))
    { return t2; } 
    if (t2name.equals("OclAny"))
    { return t1; } 

    if (t1name.equals(t2name))  
    { if ("Set".equals(t1name) || "Sequence".equals(t1name) || "Map".equals(t1name) || "Function".equals(t1name))
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

    if (t1name.equals("OclAny"))
    { return t1; } 
    if (t2name.equals("OclAny"))
    { return t2; } 

    if (t1name.equals(t2name))  
    { if ("Set".equals(t1name) || "Sequence".equals(t1name) || "Map".equals(t1name) || "Function".equals(t1name))
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
    { res = "\n"; } 
    else 
    { for (int i = 0; i < values.size(); i++)
      { res = res + values.get(i) + " "; }
      res = res + "\n"; 
    } 
  
    if (alias != null) 
    { res = res + alias + "\n"; }
 
    return res; 
  }

  public String toXml()
  { return "  <UML:Datatype name=\"" + getName() +
           "\"/>\n";
  }

  public String toString()
  { String nme = getName(); 
    if ("Set".equals(nme) || "Sequence".equals(nme) || 
        "Ref".equals(nme))
    { if (elementType != null) 
      { return nme + "(" + elementType + ")"; } 
    } 

    if ("Map".equals(nme) || "Function".equals(nme))
    { String kt = keyType + ""; 
      String et = elementType + ""; 
      if (keyType == null) 
      { kt = "String"; } 
      if (elementType == null)
      { et = "OclAny"; } 
      return nme + "(" + kt + "," + et + ")"; 
    } 

    return nme; 
  } 

  public String toAST()
  { String res = "(OclType "; 
    String nme = getName(); 
    if ("Set".equals(nme) || "Sequence".equals(nme))
    { if (elementType == null)
      { return res + nme + ")"; } 
      else  
      { return res + nme + " ( " + elementType.toAST() + " ) )"; } 
    } 

    if ("Map".equals(nme) || "Function".equals(nme))
    { String kt; 
      String et; 
      if (keyType == null) 
      { kt = "(OclType String)"; }
      else 
      { kt = keyType.toAST(); }  
      if (elementType == null)
      { et = "(OclType OclAny)"; } 
      else 
      { et = elementType.toAST(); } 
      return res + nme + " ( " + kt + " , " + et + " ) )"; 
    } 

    return res + nme + ")"; 
  } 

  public String toDeclarationAST()
  { if (values == null) 
    { return ""; }
 
    String res = "(OclEnumeration enumeration "; 
    String nme = getName(); 
    res = res + nme + " { (OclLiterals ";
    for (int i = 0; i < values.size(); i++) 
    { res = res + "(OclLiteral literal " + values.get(i) + ") "; 
      if (i < values.size() - 1) 
      { res = res + " ; "; } 
    }
    return res + " ) } )";  
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
  }  // Map(String, T) composed with R is Map(String, T composed with R)? 
        
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
    Vector eargs = new Vector(); 
	
    if (isMapType() || isFunctionType())
    { args.add(keyType.cg(cgs)); 
      eargs.add(keyType); 
      if (elementType == null) 
      { args.add("OclAny"); 
        eargs.add(new Type("OclAny", null)); 
      } 
      else 
      { args.add(elementType.cg(cgs)); 
        eargs.add(elementType); 
      }  
    } 
    else if (isCollectionType() || isRef())
    { if (elementType == null) 
	 { args.add("OclAny"); 
	   eargs.add(new Type("OclAny", null)); 
	 } 
	 else 
      { args.add(elementType.cg(cgs)); 
        eargs.add(elementType); 
      }
    } 
    else
    { args.add(typetext);
      eargs.add(this); 
    }

    CGRule r = cgs.matchedTypeUseRule(this,typetext);
    System.out.println(">>> Matched type rule " + r + " for type " + this + " " + typetext + " " +  args); 
	
    if (r != null)
    { String res = r.applyRule(args,eargs,cgs); 
      System.out.println(">>> Resulting type = " + res); 
      return res;
    }
    return typetext;
  }

  public String cgDatatype(CGSpec cgs)
  { // primitive type with alias != null

    String typetext = this + "";
    Vector args = new Vector();
    Vector eargs = new Vector();  

    args.add(getName()); 
    args.add(alias.cg(cgs)); 

    eargs.add(getName()); 
    eargs.add(alias); 

    CGRule r = cgs.matchedDatatypeRule(this,typetext);
    if (r != null)
    { return r.applyRule(args,eargs,cgs); }
    return typetext;
  }

  public String cgEnum(CGSpec cgs)
  { String typetext = this + "";
    if (values == null) 
    { return typetext; } 

    Vector args = new Vector();
    String arg = ""; 
    Vector lits = new Vector(); 

    for (int i = 0; i < values.size(); i++) 
    { String v = (String) values.get(i); 
      EnumLiteral lit = new EnumLiteral(v); 
      lits.add(lit); 
    } 
    String litstring = cgLiteralsList(lits,cgs); 

    args.add(getName()); 
    args.add(litstring); 

    CGRule r = cgs.matchedEnumerationRule(this,typetext);
    if (r != null)
    { return r.applyRule(args); }
    return typetext;
  }

  public String cgLiteralsList(Vector literals, CGSpec cgs)
  { if (literals.size() == 1)
    { EnumLiteral lit = (EnumLiteral) literals.get(0); 
      String text = "literal " + lit;
      Vector args = new Vector(); 
      args.add(lit + "");  
      CGRule r = cgs.matchedEnumerationRule(lit,text);
      if (r != null)
      { return r.applyRule(args); }
      return lit + ""; 
    } 
    else 
    { EnumLiteral lit1 = (EnumLiteral) literals.get(0); 
      Vector taillits = new Vector(); 
      taillits.addAll(literals); 
      taillits.remove(0); 
      String stail = cgLiteralsList(taillits,cgs); 
      Vector args = new Vector(); 
      args.add(lit1 + ""); 
      args.add(stail); 
      CGRule r = cgs.matchedEnumerationRule(literals,"");
      if (r != null)
      { return r.applyRule(args); }
      return lit1 + ", " + stail; 
    } 
  }  

  public Vector testValues()
  { Vector res = new Vector(); 
  
    String t = getName(); 
    Vector vs = getValues(); 

    if ("int".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 
      res.add("2147483647");  // Integer.MAX_VALUE);
      res.add("-2147483648"); // Integer.MIN_VALUE);
    } 
    else if ("long".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 
      res.add("" + Long.MAX_VALUE);
      res.add("" + Long.MIN_VALUE);
    } 
    else if ("double".equals(t))
    { res.add("0"); 
      res.add("-1");
      res.add("1"); 
      res.add("" + Double.MAX_VALUE);
      res.add("" + Double.MIN_VALUE);
    } 
    else if ("boolean".equals(t))
    { res.add("true"); 
      res.add("false");
    }
    else if ("String".equals(t))
    { res.add("\"\""); 
      res.add("\" abc_XZ \"");
      res.add("\"#�$* &~@':\"");
    }
    else if (vs != null && vs.size() > 0) 
    { for (int j = 0; j < vs.size(); j++)   
      { String v0 = (String) vs.get(j); 
        res.add(v0); 
      } 
    }
    else if (isEntity())
    { String obj = t.toLowerCase() + "x_0"; 
        // Identifier.nextIdentifier(t.toLowerCase()); 
      res.add(obj); 
    }
    return res;   
  } 
  
  public static boolean typeCompatible(Type t1, Type elemt1, Type t2, Type elemt2)
  { // Something of type t1(elemt1) can be converted to something of type t2(elemt2)
    String t1name = t1.getName(); 
	String t2name = t2.getName(); 
	
    if (t1.isCollection() && t2.isCollection() && t1name.equals(t2name))
	{ return typeCompatible(elemt1, elemt1, elemt2, elemt2); }   // sets and sequences of compatible types
	
	if (t1.isCollection() && !t2.isCollection())
	{ return typeCompatible(elemt1,elemt1,t2,elemt2); }  // reduce set(String) to String, etc
	
	if (t1.isEntity() && t2.isEntity())
	{ return Entity.isDescendant(t1.entity,t2.entity); }  // including equality. 
	
	if (t1.isEntity() && t2.isString())  // ok if t1.entity has a toString operation
	{ if (t1.entity.hasOperation("toString"))
	  { return true; }
	}
	
	if (t1.isNumeric() && t2.isString())
	{ return true; }
	
	if ((t1name + "").equals("int") && (t2name + "").equals("long")) 
    { return true; } // and other cases where t1 is a subtype of t2
    
	if ((t1name + "").equals(t2name + "")) 
    { return true; }  
    
	return false; 
  } 


  public static void main(String[] args) 
  { /* Boolean b = new Boolean(true); 
    System.out.println(b.booleanValue()); */ 
    System.out.println(getTypeFor("Function(double,Function(int,double))",new Vector(),new Vector()));
    System.out.println(getTypeFor("Map(Map(int,double),double)",new Vector(),new Vector()));

   System.out.println(Type.isBasicType(new Type("void", null))); 

   Type ref = new Type("Ref", null); 
   ref.setElementType(new Type("void", null)); 

   System.out.println(ref.getCSharp()); 

    /* Type t1 = new Type("Sequence",null); t1.setElementType(new Type("int", null)); 
    Type t2 = new Type("Sequence",null); t2.setElementType(new Type("long", null)); 
    System.out.println(mostSpecificType(t1, t2));  

    Vector v1 = new Vector(); 
    v1.add("a"); v1.add("b"); v1.add("c"); v1.add("d"); 
    Vector v2 = new Vector(); 
    v2.add("c"); v2.add("a"); v2.add("d"); 
 
    Type t1 = new Type("T1", v1); 
    Type t2 = new Type("T2", v2); 
    System.out.println(Type.enumSimilarity(t1,t2));
	
	Type tt = new Type("Map",null); 
	tt.setElementType(t1); 
	
	
	System.out.println(tt); 
	*/  
  } 
}

class EnumLiteral
{ String value = ""; 

  EnumLiteral(String v)
  { value = v; } 

  public String toString()
  { return value; } 

  public String cg(CGSpec cgs)
  { String typetext = this + "";
    Vector args = new Vector();
    args.add(typetext); 

    CGRule r = cgs.matchedEnumerationRule(this,typetext);
    if (r != null)
    { return r.applyRule(args); }
    return typetext;
  }
} 
