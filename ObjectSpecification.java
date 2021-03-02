import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ObjectSpecification extends ModelElement
{ // String objectName;
  
  String objectClass = "";
  Entity entity = null;
  List atts = new ArrayList();  // String
  java.util.Map attvalues = new HashMap(); // String --> Object
    // basic values are stored as strings, as are object names
    // Collections are stored as Vectors

  List elements = new ArrayList(); // ObjectSpecification
  boolean isSwingObject = true; 

  public ObjectSpecification(String nme, String typ)
  { // objectName = nme;
    super(nme);
    objectClass = typ;
    if (isJavaGUIClass(typ)) 
    { isSwingObject = true; } 
    else 
    { isSwingObject = false; } 
  }

  public void setEntity(Entity cls) 
  { entity = cls; } 

  public Entity getEntity() 
  { return entity; } 

  public void setValue(String att, Object val) 
  { if (val instanceof String)
    { attvalues.put(att,val); }
    else if (val instanceof ObjectSpecification)
    { String nme = ((ObjectSpecification) val).getName(); 
      attvalues.put(att,nme); 
    }
    else if (val instanceof Vector)
    { Vector vect = (Vector) val; 
      Vector actualval = new Vector(); 
      for (int i = 0; i < vect.size(); i++)
      { Object obj = vect.get(i); 
        if (obj instanceof String)
        { actualval.add(obj); }
        else if (obj instanceof ObjectSpecification)
        { actualval.add(((ObjectSpecification) obj).getName()); }
      } 
      attvalues.put(att,actualval); 
    }
  } 

  public Object getRawValue(String att) 
  { return attvalues.get(att); } 

  public String toString()
  { String res = getName() + " : " + objectClass; 
       // + " { " +
       //    attvalues + "; " + elements + " }";
    return res; 
  }

  public String toILP()
  { String nme = getName();
    String cls = objectClass.toLowerCase();
    String res = cls + "(" + nme + ")\n"; 
    for (int i = 0; i < atts.size(); i++)
    { String att = (String) atts.get(i); 
      if (attvalues.get(att) != null)
      { res = res + cls + "_" + att + "(" + nme + "," + toILP(attvalues.get(att)) + ")\n"; }
    }
    return res;
  }

  public static String toILP(Object val) 
  { if (val instanceof String) 
    { return (String) val; } 
    else if (val instanceof Vector)
    { String res = "["; 
      Vector objs = (Vector) val; 
      for (int i = 0; i < objs.size(); i++) 
      { if (objs.get(i) instanceof String)
        { res = res + objs.get(i); } 
        else if (objs.get(i) instanceof ObjectSpecification)
        { res = res + ((ObjectSpecification) objs.get(i)).getName(); } 
      
        if (i < objs.size() - 1) 
        { res = res + ", "; } 
      } 
      return res + "]";
    } 
    return "?";  
  } 


  public String correspondence2ILP(ObjectSpecification tobj)
  { String nme = getName();
    String cls = objectClass.toLowerCase();
    String tnme = tobj.getName(); 
    String tcls = tobj.objectClass.toLowerCase(); 

    String res = cls + "2" + tcls + "(" + nme + "," + tnme + ")\n"; 
    return res;
  }

  public boolean equals(Object x) 
  { if (x instanceof ObjectSpecification)
    { ObjectSpecification spec = (ObjectSpecification) x; 
      String xname = spec.getName(); 
      return xname.equals(getName()); 
       // && 
       //  spec.attvalues != null && attvalues != null && 
       //  attvalues.equals(spec.attvalues); 
    } 
    return false; 
  } 

  public boolean hasDefinedValue(String att)
  { if ("self".equals(att)) 
    { return true; } 

    Object val = attvalues.get(att); 
    return (val != null); 
  } 

  public boolean hasDefinedValue(Attribute att, ModelSpecification mod)
  { String attname = att.getName(); 
    if (attvalues.get(attname) != null) 
    { return true; } 

    Vector path = att.getNavigation(); 
    if (path.size() == 0)
    { path.add(att); }
    return hasDefinedValue(path, mod); 
  } 

  public boolean hasDefinedValue(Vector atts, ModelSpecification mod)
  { if (atts.size() == 0) 
    { return true; }
	
    Attribute fst = (Attribute) atts.get(0);  
    String attname = fst.getName(); 
    Vector rest = new Vector(); 
    rest.addAll(atts); 
    rest.remove(0); 
	
    if ("self".equals(attname)) 
    { return hasDefinedValue(rest, mod); }
	
    if (atts.size() == 1) 
    { Object val = attvalues.get(attname);
      return (val != null); 
    } 
    else 
    { ObjectSpecification ospec = getReferredObject(attname,mod); 
      if (ospec != null) 
      { return ospec.hasDefinedValue(rest, mod); }
      return false; 
    }  
  }

  public int getInt(String att) 
  { // Where we know that att represents an int value 
    String val = (String) attvalues.get(att); 
    if (val != null)
    { int x = Integer.parseInt(val);  
      return x; 
    }
    return 0; 
  } 

  public long getLong(String att) 
  { // Where we know that att represents an int value 
    String val = (String) attvalues.get(att); 
    if (val != null)
    { long x = Long.parseLong(val);  
      return x; 
    }
    return 0; 
  } 

  public double getDouble(String att) 
  { // Where we know that att represents an int value 
    String val = (String) attvalues.get(att); 
    if (val != null)
    { double x = Double.parseDouble(val);  
      return x; 
    }
    return 0; 
  } 

  public double getNumeric(String att) 
  { double res = 0; 

    String val = (String) attvalues.get(att); 
    if (val != null) 
    { try { res = Integer.parseInt(val); } 
      catch (Exception _e) 
      { try { res = Long.parseLong(val); } 
        catch (Exception _f) 
        { res = Double.parseDouble(val); } 
      } 
    } 
    return res; 
  }  

  public double getNumericValue(Attribute att, ModelSpecification mod) 
  { double res = 0; 
    String val = "0.0"; 
    String attname = att.getName(); 

    Vector path = att.getNavigation(); 
    if (path.size() <= 1)
    { return getNumeric(attname); } 
    else 
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      ObjectSpecification obj = getReferredObject(path.get(0) + "", mod); 
      if (obj == null) 
      { return 0.0; } 
      return obj.getNumericValue(new Attribute(pathtail), mod); 
    } 
  }  

  public String getStringValue(Attribute att, ModelSpecification mod) 
  { String res = ""; 
    String attname = att.getName(); 

    Vector path = att.getNavigation(); 
    if (path.size() <= 1)
    { return getString(attname); } 
    else 
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      ObjectSpecification obj = getReferredObject(path.get(0) + "", mod); 
      if (obj == null) 
      { return ""; } 
      return obj.getStringValue(new Attribute(pathtail), mod); 
    } 
  }  

  public static Vector getStringValues(Attribute att, Vector objects, ModelSpecification mod)
  { Vector res = new Vector(); 
    for (int i = 0; i < objects.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objects.get(i); 
      String attvalue = obj.getStringValue(att,mod); 
      if (attvalue != null) 
      { res.add(attvalue); } 
    } 
    return res; 
  } 

  public String getEnumerationValue(Attribute att, ModelSpecification mod) 
  { String res = ""; 
    String attname = att.getName(); 

    Vector path = att.getNavigation(); 
    if (path.size() <= 1)
    { return getEnumeration(attname); } 
    else 
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      ObjectSpecification obj = getReferredObject(path.get(0) + "", mod); 
      if (obj == null) 
      { return ""; } 
      return obj.getEnumerationValue(new Attribute(pathtail), mod); 
    } 
  }  

  public ObjectSpecification[] getCollectionAsObjectArray(Attribute att, ModelSpecification mod)
  { Vector res = getCollectionValue(att,mod);

    System.out.println("+++---> Value of " + att + " on " + this + " = " + res); 
 
    if (res == null) 
    { return null; } 
    ObjectSpecification[] objs = new ObjectSpecification[res.size()]; 
    for (int i = 0; i < res.size(); i++)
    { ObjectSpecification obj = (ObjectSpecification) res.get(i); 
      objs[i] = obj; 
    } 
    return objs; 
  } 

  public String[] getCollectionAsStringArray(Attribute att, ModelSpecification mod)
  { Vector res = getCollectionValue(att,mod); 
    if (res == null) 
    { return null; } 
    String[] objs = new String[res.size()]; 
    for (int i = 0; i < res.size(); i++)
    { String obj = (String) res.get(i); 
      if (obj != null && '"' == obj.charAt(0) && 
          '"' == obj.charAt(obj.length() - 1)) 
      { objs[i] = obj.substring(1,obj.length()-1); } 
      else 
      { objs[i] = obj; }  
    } 
    return objs; 
  } 

  public Vector getCollectionValue(Attribute att, ModelSpecification mod) 
  { Vector rawvalues = getRawCollectionValue(att, mod); 
    Vector res = new Vector(); 
	
    Type elemT = att.getElementType(); 
    if (elemT != null && elemT.isEntity())
    { for (int i = 0; i < rawvalues.size(); i++) 
      { if (rawvalues.get(i) instanceof ObjectSpecification)
        { res.add(rawvalues.get(i)); }
	   else if (rawvalues.get(i) instanceof String)
	   { ObjectSpecification obj = mod.getObject(rawvalues.get(i) + ""); 
           if (obj != null) 
	     { res.add(obj); }
	   } 
      } 
      return res; 
    } 
    else 
    { return rawvalues; }
  } 
		
  public Vector getRawCollectionValue(Attribute att, ModelSpecification mod) 
  { Vector res = new Vector(); 
    String attname = att.getName(); 

    Vector path = att.getNavigation(); 
    if (path.size() <= 1)
    { return getCollection(attname); } 
    else if (path.size() > 1)
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      Attribute head = (Attribute) path.get(0); 
      Attribute tail = new Attribute(pathtail); 
	  
      // System.out.println(">>> " + head + " is collection: " + head.isCollection()); 
      // System.out.println(">>> " + tail + " is collection: " + Attribute.isMultipleValued(pathtail)); 
	  

      if (head.isCollection())
      { Vector objs = getCollection(head.getName()); 
        if (objs == null) { return res; } 
        if (Attribute.isMultipleValued(pathtail))
        { for (int i = 0; i < objs.size(); i++) 
          { if (objs.get(i) instanceof ObjectSpecification)
            { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
              Vector values = obj.getCollectionValue(tail,mod); 
              if (values != null)
              { res.addAll(values); }
            }   
          } 
          return res; 
        } 
        else 
        { for (int i = 0; i < objs.size(); i++) 
          { if (objs.get(i) instanceof ObjectSpecification)
		 { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
            // get the value of obj.tail and add to res
              Object val = obj.getValueOf(tail,mod);
              if (val != null) 
              { res.add(val); }
            }   
          } 
        } 
      } 
      else // head is single-valued object 
      { ObjectSpecification obj = getReferredObject(head.getName(), mod); 
        if (obj == null) 
        { return res; } 
        return obj.getCollectionValue(tail, mod); 
      } 
    } 
    return res; 
  }  

  public String getString(String att) 
  { 
    String val = (String) attvalues.get(att); 
    if (val != null && val.startsWith("\"") && val.endsWith("\"")) 
    { return val.substring(1,val.length()-1); } 
	else if (val != null) 
	{ return val; }
    return "";  
  } // Need also to remove the quotes

  public String getEnumeration(String att) 
  { 
    String val = (String) attvalues.get(att); 
    if (val != null) 
    { return val; } 
    return "";  
  } 

  public Vector getCollection(String att) 
  { Object x = attvalues.get(att); 
    if (x == null) 
    { return new Vector(); }
    if (x instanceof Vector)
    { return (Vector) x; }
    Vector res = new Vector(); 
    res.add(x); 
    return res; 
  }  

  public String getSetValue(Vector objs) 
  { String res = "Set{"; 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
      res = res + obj.getName(); 
      if (i < objs.size() - 1) 
      { res = res + ", "; } 
    }
    return res + "}";  
  }  

  public String getSequenceValue(Vector objs) 
  { String res = "Sequence{"; 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
      res = res + obj.getName(); 
      if (i < objs.size() - 1) 
      { res = res + ", "; } 
    }
    return res + "}";  
  }  

  public boolean getBoolean(String att) 
  { // Where we know that att represents a boolean value 
    String val = (String) attvalues.get(att); 
    if (val != null)
    { if ("true".equals(val))
      { return true; } 
      return false; 
    }
    return false; 
  } 

  public ObjectSpecification getReferredObject(Attribute att, ModelSpecification mod) 
  { String attname = att.getName(); 

    Vector path = att.getNavigation(); 
    if (path.size() <= 1)
    { return getReferredObject(attname,mod); } 
    else 
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      ObjectSpecification obj = getReferredObject(path.get(0) + "", mod); 
      if (obj == null) 
      { return null; } 
      return obj.getReferredObject(new Attribute(pathtail), mod); 
    } 
  } // also case of collections of objects 
 
  /* public ObjectSpecification getReferredObject(Vector att, ModelSpecification mod) 
  { for (int i = 0; i < att.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) att.get(i); 
      return obj; 
    }
    return null;  
  }  */ 

  public ObjectSpecification getReferredObject(String att, ModelSpecification mod) 
  { if ("self".equals(att))
    { return this; } 

    Object objectName = attvalues.get(att); 
    if (objectName == null) 
    { return null; }
    else if (objectName instanceof String)
    { return mod.getObject(objectName + ""); } 
    else if (objectName instanceof Vector)
    { Vector objs = (Vector) objectName; 
	 if (objs.size() == 0) { return null; } 
	 return (ObjectSpecification) objs.get(0); 
    }
    return null;  
  }  

  public String getValue(Expression expr, ModelSpecification mod)
  { String v = expr + ""; 
    Type t = expr.getType(); 

    
    if (expr instanceof BasicExpression)
    { BasicExpression be = (BasicExpression) expr; 
      if (be.objectRef != null)
      { ObjectSpecification spec = mod.getObjectValue((BasicExpression) be.objectRef); 
        if (spec != null)
        { BasicExpression attr = new BasicExpression(be.data); 
          attr.setType(t); 
          return spec.getValue(attr,mod); 
        }
      } 
    }
	
    Object val = attvalues.get(v); 
    if (val == null) 
    { if (t != null && t.isSetType())
      { return "Set{}"; } 
      else if (t != null && t.isSequenceType())
      { return "Sequence{}"; } 
      else if (t != null && t.isEnumeration())
      { return expr + ""; } 
      else if (t != null && t.isNumeric())
      { return expr + ""; } 
      else if (t != null && t.isString())
      { return expr + ""; } // "\"" + expr + "\""; } 
      else // and numerics?  
      { return "null"; }
    } 
    else // if (val != null) 
    { if (val instanceof Vector) 
      { if (t != null && t.isSetType())
        { return getSetValue((Vector) val); }
        else if (t != null && t.isSequenceType())
        { return getSequenceValue((Vector) val); } 
      }
      return val + ""; 
    } 
    // return expr + ""; 
  } 

  public static Vector getAllValuesOf(Attribute attr, Vector objs, ModelSpecification mod)
  { Vector res = new Vector(); 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
      Object x = obj.getValueOf(attr,mod); 
      if (x != null) 
      { res.add(x); } 
    } 
    return res; 
  } 

  // Assuming a singular value for attr: 
  public Object getValueOf(Attribute attr, ModelSpecification mod)
  { String attname = attr + ""; 
    Type t = attr.getType(); 
    Vector path = attr.getNavigation(); 

    if (path.size() <= 1)
    { return attvalues.get(attname); } 
    else 
    { Vector pathtail = new Vector(); 
      pathtail.addAll(path); 
      pathtail.remove(0); 
      Attribute att1 = (Attribute) path.get(0); 
      if (att1 != null && att1.isEntity())
      { ObjectSpecification obj = getReferredObject(att1.getName(), mod); 
        if (obj == null) 
        { return null; } 
        return obj.getValueOf(new Attribute(pathtail), mod);
      } 
      else if (att1 != null && att1.isCollection()) // an Entity collection
      { Vector vect = getCollection(att1.getName()); 
        Vector res = new Vector(); 
        Attribute tail = new Attribute(pathtail); 
        for (int i = 0; i < vect.size(); i++) 
        { ObjectSpecification objx = null; 
          if (vect.get(i) instanceof ObjectSpecification)
          { objx = (ObjectSpecification) vect.get(i); } 
          else if (vect.get(i) instanceof String)
		  { String idx = (String) vect.get(i); 
		    objx = mod.getObject(idx); 
		  } 
          if (objx != null) 
		  { Object val = objx.getValueOf(tail, mod); 
            if (val != null) 
            { res.add(val); }
		  } 
		  else 
		  { System.err.println("!! Error: expected an object value for: (" + this + ")." + attr); } 
        }
        return res;  
      } 
    }  
    return null; 
  } 

  public boolean satisfiesBinaryCondition(BinaryExpression cond, ModelSpecification mod) 
  { String condop = cond.getOperator(); 
    if ("&".equals(condop))
    { return satisfiesCondition(cond.getLeft(),mod) && 
             satisfiesCondition(cond.getRight(),mod); 
    } 
    else if ("or".equals(condop))
    { return satisfiesCondition(cond.getLeft(),mod) || 
             satisfiesCondition(cond.getRight(),mod); 
    } 
    else if ("=".equals(condop))
    { String valleft = getValue(cond.getLeft(),mod); 
      String valright = getValue(cond.getRight(),mod);
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft != null && valright != null && valleft.equals(valright))
      { return true; } 
      return false; 
    } 
    else if ("/=".equals(condop))
    { String valleft = getValue(cond.getLeft(),mod); 
      String valright = getValue(cond.getRight(),mod); 
      if (valleft != null && valright != null && valleft.equals(valright))
      { return false; }
      else if (valleft != null && !valleft.equals(valright))
      { return true; }  
      return false; 
    } 
    else if ("->oclIsTypeOf".equals(condop))
    { ObjectSpecification obj = getReferredObject(cond.getLeft() + "",mod); 
      if (obj != null && 
          obj.objectClass.equals(cond.getRight() + ""))
      { return true; }
	  return false;  
    } 
    else if ("->oclIsKindOf".equals(condop))
    { ObjectSpecification obj = getReferredObject(cond.getLeft() + "",mod); 
      if (obj != null && entity != null && 
          Entity.isDescendantOrEqualTo(entity, cond.getRight() + ""))
      { return true; }
	  return false;  
    } 
	else if ("->hasPrefix".equals(condop))
	{ String vleft = getValue(cond.getLeft(),mod); 
	  String vright = getValue(cond.getRight(),mod);
	  // System.out.println(">>> left = " + vleft + ", right = " + vright + " left->hasPrefix(right) = " + vleft.startsWith(vright));  
	  if (vleft != null && vright != null)
	  { if (vright.length() > 0 && vleft.startsWith(vright.substring(0,vright.length()-1)))
        { return true; }
	  } 
	  return false; 
	} 
	else if ("->hasSuffix".equals(condop))
	{ String vleft = getValue(cond.getLeft(),mod); 
	  String vright = getValue(cond.getRight(),mod); 
	  // System.out.println(">>> left = " + vleft + ", right = " + vright + " left->hasSuffix(right) = " + vleft.endsWith(vright));  
	  if (vleft != null && vright != null)
	  { if (vright.length() > 0 && vleft.endsWith(vright.substring(1,vright.length())))
        { return true; }
	  } 
	  return false; 
	} 
    return false; 
  }  

  public boolean satisfiesBasicCondition(BasicExpression cond, ModelSpecification mod)
  { String val = getValue(cond,mod); 
    if ("true".equals(val + "")) { return true; } 
    return false; 
  } 
 
  public boolean satisfiesUnaryCondition(UnaryExpression cond, ModelSpecification mod)
  { String op = cond.getOperator(); 
    Expression arg = cond.getArgument(); 

    if ("not".equals(op + "")) 
    { return !satisfiesCondition(arg, mod); }
    else if ("->isEmpty".equals(op + ""))
    { String val = getValue(arg,mod); 
      // System.out.println("***>> Value of " + arg + " is " + val); 
      if (val != null && val.endsWith("{}")) 
      { return true; }
	  // else if (val == null) { return true; }  
      return false; 
    }
    else if ("->notEmpty".equals(op + ""))
    { String val = getValue(arg,mod); 
      // System.out.println("***>> Value of " + arg + " is " + val); 
      if (val != null && val.endsWith("{}")) 
      { return false; } 
      else if (val != null) 
      { return true; } 
      return false;  
    }  
    return false; 
  } 

  public boolean satisfiesSetCondition(SetExpression cond, ModelSpecification mod)
  { return false; } 

  public boolean satisfiesCondition(Expression cond, ModelSpecification mod)
  { if (cond instanceof BinaryExpression) 
    { return satisfiesBinaryCondition((BinaryExpression) cond, mod); }
    else if (cond instanceof BasicExpression)
    { return satisfiesBasicCondition((BasicExpression) cond, mod); }
    else if (cond instanceof UnaryExpression)
    { return satisfiesUnaryCondition((UnaryExpression) cond, mod); }
    else if (cond instanceof SetExpression)
    { return satisfiesSetCondition((SetExpression) cond, mod); }
    return false;
  } 
	 

  public static boolean isJavaGUIClass(String s)
  { return s.equals("Frame") || s.equals("Panel") || s.equals("Button") ||
           s.equals("MenuBar") || s.equals("Menu") || s.equals("MenuItem") ||
           s.equals("Table") || s.equals("TextField") || s.equals("TextArea") ||
           s.equals("Dialog") || s.equals("Label"); 
  } 

  public void addAttribute(String att, String value)
  { if (atts.contains(att)) { } 
    else 
    { atts.add(att); } 
    attvalues.put(att,value); 
  }

  public void addAttribute(String att, Vector value)
  { if (atts.contains(att)) { } 
    else 
    { atts.add(att); } 
    attvalues.put(att,value); 
  }

  public void addAttributeElement(String att, String value)
  { Vector v = new Vector(); 
    if (atts.contains(att)) 
    { v = (Vector) attvalues.get(att); } 
    else 
    { atts.add(att);
      attvalues.put(att,v); 
    } 
    v.add(value); 
  }

  public void addAttributeElement(String att, ObjectSpecification value)
  { Vector v = new Vector(); 
    if (atts.contains(att)) 
    { v = (Vector) attvalues.get(att); } 
    else 
    { atts.add(att);
      attvalues.put(att,v); 
    } 
    v.add(value); 
  }

  public void addelement(ObjectSpecification elem)
  { elements.add(elem); }

  public List getelements() { return elements; }

  public List getatts() { return atts; }

  public boolean hasAttribute(String att)
  { return atts.contains(att); }

  public String getattvalue(String att)
  { return (String) attvalues.get(att); }

  public String getDeclaration()
  { if (isSwingObject == false)
    { return objectClass + " " + getName() + " = new " + objectClass + "();"; } 

    String res = "J" + objectClass + " " + getName() + " ";
    if (objectClass.equals("Button") || 
        objectClass.equals("MenuItem"))
    { String btext = getattvalue("text");
      res = res + " = new J" + objectClass + "(" + btext + ");"; 
    }
    else if (objectClass.equals("Label") || 
             objectClass.equals("Menu"))
    { String btext = getattvalue("text");
      res = res + " = new J" + objectClass + "(" + btext + ");"; 
    }
    else if (objectClass.equals("Table"))
    { res = res + ";"; } 
    else 
    { res = res + " = new J" + objectClass + "();"; }
    return res;
  }

  public String getDefinition()
  { String res = "";
    if (!isSwingObject)
    { for (int i = 0; i < atts.size(); i++) 
      { String att = (String) atts.get(i); 
        String val = getattvalue(att); 
        res = res + getName() + ".set" + att + "(" + val + ");\n      "; 
      } 
      return res; 
    }

    if (objectClass.equals("Button") || 
        objectClass.equals("MenuItem"))
    { String btext = getattvalue("text");
      res = getName() + ".addActionListener(this);"; 
    }
    else if (objectClass.equals("Table"))
    { String rows = getattvalue("rows"); 
      String cols = getattvalue("columns"); 
      String data = getattvalue("cells"); 
      res = getName() + " = JTableBuilder.buildTable(" + data + "," + cols + 
                                                     "," + rows + ");";
    } 


    for (int i = 0; i < elements.size(); i++)
    { ObjectSpecification elem = 
         (ObjectSpecification) elements.get(i);
      res = res + "\n    " + getName() + ".add(" +
               elem.getName() + ");";
    }
    return res;
  }

  public List getcomponents(List objs)
  { List res = new ArrayList();
     for (int i = 0; i < atts.size(); i++)
     { String att = (String) atts.get(i);
       String val = getattvalue(att);
        // if an object, add:
       ModelElement valobj = 
            ModelElement.lookupByName(val,objs);
       if (valobj != null && 
           valobj instanceof ObjectSpecification)
       { res.add(valobj); }
    }
    return res;
  }

  public List getallcomponents(List objs)
  { // all ObjectSpecs which it (recursively) contains
     List res = new ArrayList();
     for (int i = 0; i < atts.size(); i++)
     { String att = (String) atts.get(i);
       String val = getattvalue(att);
       ModelElement valobj = 
            ModelElement.lookupByName(val,objs);
       if (valobj != null && 
           valobj instanceof ObjectSpecification)
       { ObjectSpecification valos = (ObjectSpecification) valobj;
         res.add(valos); 
         res.addAll(valos.getallcomponents(objs)); 
       }
    }
    return res;
  }

  public void generateJava(PrintWriter out)
  { } 

}
