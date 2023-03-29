import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Map; 
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;

class OclType { 
  static 
  { OclType.OclType_index = new HashMap<String,OclType>();
    OclType stringType = OclType.createByPKOclType("String"); 
    stringType.actualMetatype = String.class; 
    OclType intType = OclType.createByPKOclType("int"); 
    intType.actualMetatype = int.class; 
    OclType longType = OclType.createByPKOclType("long"); 
    longType.actualMetatype = long.class; 
    OclType doubleType = OclType.createByPKOclType("double"); 
    doubleType.actualMetatype = double.class; 
    OclType booleanType = OclType.createByPKOclType("boolean"); 
    booleanType.actualMetatype = boolean.class; 
    OclType voidType = OclType.createByPKOclType("void"); 
    voidType.actualMetatype = void.class; 
  } 

  public Class actualMetatype = null; 

  OclType() { }

  OclType(String nme)
  { name = nme; 
    OclType.createByPKOclType(nme); 
  } 

  OclType(Class c) 
  { this(c.getName());
    actualMetatype = c; 
  }


  static OclType createOclType() 
  { OclType result = new OclType();
    return result; 
  }

  String name = ""; /* primary */
  static Map<String,OclType> OclType_index;

  static OclType createByPKOclType(String namex)
  { OclType result = OclType.OclType_index.get(namex);
    if (result != null) 
    { return result; }
    result = new OclType();
    OclType.OclType_index.put(namex,result);
    result.name = namex;
    return result; }

  static void killOclType(String namex)
  { OclType rem = OclType_index.get(namex);
    if (rem == null) { return; }
    ArrayList<OclType> remd = new ArrayList<OclType>();
    remd.add(rem);
    OclType_index.remove(namex);
  }

  ArrayList<OclAttribute> attributes = new ArrayList<OclAttribute>();
  ArrayList<OclOperation> operations = new ArrayList<OclOperation>();
  ArrayList<OclOperation> constructors = new ArrayList<OclOperation>();
  ArrayList<OclType> innerClasses = new ArrayList<OclType>();
  ArrayList<OclType> componentType = new ArrayList<OclType>();
  ArrayList<OclType> superclasses = new ArrayList<OclType>();

  public void setMetatype(Class cls)
  { actualMetatype = cls; } 

  public String getName()
  { return name; }


  public ArrayList<OclType> getClasses()
  { return innerClasses; }


  public ArrayList<OclType> getDeclaredClasses()
  {
    ArrayList<OclType> result = new ArrayList<OclType>();
    result = Ocl.subtract(innerClasses,Ocl.unionAll(Ocl.collectSequence(superclasses,(sc)->{return sc.getClasses();})));
    return result;
  }


  public OclType getComponentType()
  {
    OclType result = null;
    if (componentType.size() > 0)
    {
      result = Ocl.any(componentType);
    }
    return result;
  }


  public ArrayList<OclAttribute> getFields()
  { if (actualMetatype != null) 
    { attributes.clear(); 
      Field[] flds = actualMetatype.getFields(); 
      for (int i = 0; i < flds.length; i++) 
      { OclAttribute att = new OclAttribute(flds[i].getName()); 
        att.setType(new OclType(flds[i].getType())); 
        attributes.add(att); 
      } 
    } 
    return attributes;
  }

  public static ArrayList<OclAttribute> allAttributes(Object obj)
  { ArrayList<OclAttribute> res = new ArrayList<OclAttribute>(); 
    if (obj == null) 
    { return res; } 

    if (obj instanceof Map)
    { Map mp = (Map) obj; 

      java.util.Set keys = mp.keySet(); 
      for (Object k : keys)
      { String key = k + ""; 
        OclAttribute katt = 
           new OclAttribute(key);
        Object val = mp.get(k);
        if (val != null) 
        { Class vtype = val.getClass(); 
          OclType otype = new OclType(vtype); 
          katt.setType(otype); 
        } 
        res.add(katt); 
      } 
      return res; 
    } 
      
    Class metatype = obj.getClass(); 
    Field[] flds = metatype.getFields(); 
    for (int i = 0; i < flds.length; i++) 
    { OclAttribute att = new OclAttribute(flds[i].getName()); 
      att.setType(new OclType(flds[i].getType())); 
      res.add(att); 
    }
    return res;  
  } 


  public OclAttribute getDeclaredField(String s)
  { attributes = getFields(); 
    OclAttribute result = null;
    result = Ocl.any(Ocl.selectSequence(attributes,(att)->{return att.name.equals(s);}));
    return result;
  }


  public OclAttribute getField(String s)
  { attributes = getFields(); 
    OclAttribute result = null;
    result = Ocl.any(Ocl.selectSequence(attributes,(att)->{return att.name.equals(s);}));
    return result;
  }


  public ArrayList<OclAttribute> getDeclaredFields()
  {
    ArrayList<OclAttribute> result = new ArrayList<OclAttribute>();
    if (actualMetatype != null) 
    { Field[] flds = actualMetatype.getDeclaredFields(); 
      for (int i = 0; i < flds.length; i++) 
      { OclAttribute att = new OclAttribute(flds[i].getName()); 
        att.setType(new OclType(flds[i].getType())); 
        result.add(att); 
      } 
    } 
    return result;
  }


  public ArrayList<OclOperation> getMethods()
  { if (actualMetatype != null) 
    { operations.clear(); 
      Method[] mets = actualMetatype.getMethods(); 
      for (int i = 0; i < mets.length; i++) 
      { OclOperation op = new OclOperation(mets[i].getName()); 
        if (mets[i].getReturnType() != null) 
        { op.setType(new OclType(mets[i].getReturnType())); }  
        operations.add(op); 
      } 
    } 
    return operations; 
  }


  public ArrayList<OclOperation> getDeclaredMethods()
  {
    ArrayList<OclOperation> result = new ArrayList<OclOperation>();
    if (actualMetatype != null) 
    { Method[] mets = actualMetatype.getDeclaredMethods(); 
      for (int i = 0; i < mets.length; i++) 
      { OclOperation op = new OclOperation(mets[i].getName()); 
        if (mets[i].getReturnType() != null) 
        { op.setType(new OclType(mets[i].getReturnType())); }  
        result.add(op); 
      } 
    } 
    return result;
  }


  public ArrayList<OclOperation> getConstructors()
  {
    ArrayList<OclOperation> result = new ArrayList<OclOperation>();
    if (actualMetatype != null) 
    { Constructor[] mets = actualMetatype.getDeclaredConstructors(); 
      for (int i = 0; i < mets.length; i++) 
      { OclOperation op = new OclOperation(mets[i].getName()); 
        result.add(op); 
      } 
    } 
    return result;
  }


  public OclType getSuperclass()
  { if (actualMetatype != null) 
    { Class superclass = actualMetatype.getSuperclass();
      if (superclass != null)
      { superclasses.clear(); 
        superclasses.add(new OclType(superclass)); 
      } 
    } 
    OclType result = null;
    if (superclasses.size() > 0)
    { result = Ocl.any(superclasses); } 
    return result;
  }

  public static boolean hasAttribute(Object obj, String att)
  { if (obj == null) 
    { return false; } 

    if (obj instanceof Map)
    { Map mp = (Map) obj; 
      java.util.Set keys = mp.keySet(); 
      for (Object k : keys)
      { if (att.equals(k + ""))
        { return true; } 
      } 
      return false; 
    } 

    Class metatype = obj.getClass(); 
    try {
      Field fld = metatype.getField(att); 
      return fld != null;
    } catch (Exception e) 
      { return false; }  
  } 

  public static Object getAttributeValue(Object obj, String att)
  { if (obj == null) 
    { return null; } 

    if (obj instanceof Map)
    { Map mp = (Map) obj; 
      java.util.Set keys = mp.keySet(); 
      for (Object k : keys)
      { if (att.equals(k + ""))
        { return mp.get(k); } 
      } 
      return null; 
    } 

    Class metatype = obj.getClass(); 
    try {
      Field fld = metatype.getField(att); 
      if (fld != null)
      { return fld.get(obj); } 
    } catch (Exception e) 
      { }  
    return null; 
  } 

  public static void setAttributeValue(Object obj, String att, Object val)
  { if (obj == null) 
    { return; } 

    if (obj instanceof Map)
    { Map mp = (Map) obj; 
      mp.put(att,val); 
      return; 
    } 

    Class metatype = obj.getClass(); 
    try {
      Field fld = metatype.getField(att); 
      if (fld != null)
      { fld.set(obj,val); } 
    } catch (Exception e) 
      { }  
  } 

  public static void removeAttribute(Object obj, String att)
  { if (obj instanceof Map)
    { Map mp = (Map) obj; 
      mp.remove(att); 
      return; 
    } 

    System.err.println(
       "removeAttribute is invalid in Java");
  }  
  
  public Object newInstance()
  { if (actualMetatype != null) 
    { try { return actualMetatype.newInstance(); }
	  catch (InstantiationException _e) { return null; }
	  catch (IllegalAccessException _e) { return null; }
    } 
	return null; 
  } 

  public boolean isArray()
  { if ("Sequence".equals(name)) 
    { return true; } 

    if (actualMetatype != null)
    { return actualMetatype.isArray(); } 

    return false; 
  }  

  public boolean isPrimitive()
  { if ("int".equals(name) || 
        "long".equals(name) ||
        "double".equals(name) ||
        "boolean".equals(name)) 
    { return true; } 

    if (actualMetatype != null)
    { return actualMetatype.isPrimitive(); } 

    return false; 
  }  

  public boolean isInterface()
  { if (actualMetatype != null) 
    { return actualMetatype.isInterface(); } 
    return false; 
  } 

  public boolean isAssignableFrom(OclType c)
  { if (c.actualMetatype != null && actualMetatype != null) 
    { return actualMetatype.isAssignableFrom(
                                 c.actualMetatype); 
    } 
    return false; 
  } 

  public boolean isInstance(Object obj)
  { if (actualMetatype != null) 
    { return actualMetatype.isInstance(obj); } 
    return false; 
  } 
  
  public static OclType loadExecutableObject(String s)
  { try { 
      Class c = Class.forName(s); 
      if (c != null) 
	  { return new OclType(c); }
    } 
    catch (Exception _ex) { return null; }
    return null; 
  }

  public static void unloadExecutableObject(String s)
  { /* Not supported in Java */  
  }
}

