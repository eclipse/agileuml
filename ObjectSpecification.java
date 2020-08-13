import java.util.*; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2020 Kevin Lano
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
      return xname.equals(getName()) && 
         spec.attvalues != null && attvalues != null && 
         attvalues.equals(spec.attvalues); 
    } 
    return false; 
  } 

  public boolean hasDefinedValue(String att)
  { if ("self".equals(att)) 
    { return true; } 

    Object val = attvalues.get(att); 
    return (val != null); 
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
	  
	  System.out.println(">>> " + head + " is collection: " + head.isCollection()); 
	  System.out.println(">>> " + tail + " is collection: " + Attribute.isMultipleValued(pathtail)); 
	  

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
    if (val != null) 
    { return val.substring(1,val.length()-1); } 
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
      else 
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
      ObjectSpecification obj = getReferredObject(path.get(0) + "", mod); 
      if (obj == null) 
      { return null; } 
      return obj.getValueOf(new Attribute(pathtail), mod); 
    }  
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
      if (valleft != null && valleft.equals(valright))
      { return true; } 
      return false; 
    } 
    else if ("->oclIsTypeOf".equals(condop))
    { ObjectSpecification obj = getReferredObject(cond.getLeft() + "",mod); 
      if (obj != null && 
          obj.objectClass.equals(cond.getRight() + ""))
      { return true; } 
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
      System.out.println("***>> Value of " + arg + " is " + val); 
      if (val != null && val.endsWith("{}")) 
      { return true; } 
      return false; 
    }
    else if ("->notEmpty".equals(op + ""))
    { String val = getValue(arg,mod); 
      System.out.println("***>> Value of " + arg + " is " + val); 
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
