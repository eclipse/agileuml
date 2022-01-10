import java.util.*; 
import java.io.*; 
import javax.swing.JOptionPane; 

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
  static ObjectSpecification defaultInstance = new ObjectSpecification("null", "OclAny");

  List atts = new ArrayList();  // String
  java.util.Map attvalues = new HashMap(); // String --> Object
    // basic values are stored as strings, as are object names
    // Collections are stored as Vectors
    // ASTs as ASTTerm instances. 

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

  public static ObjectSpecification getDefaultInstance()
  { return defaultInstance; } 

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
  } // What about numbers, etc? 

  public Object getRawValue(String att) 
  { return attvalues.get(att); } 

  public String toString()
  { String res = getName() + " : " + objectClass; 
       // + " { " +
       //    attvalues + "; " + elements + " }";
    return res; 
  }

  public Vector getParameters() { return new Vector(); } 

  public void addParameter(Attribute att) { } 

  public Type getType() { return null; } 

  public void setType(Type t) { } 

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
        { try { res = Double.parseDouble(val); } 
          catch (Exception _x) 
          { System.err.println("!! Wrong value: " + val + " for numeric feature " + att); 
            return res; 
          }
        }
      } 
    } 
    else if (att != null) // att itself is a number? 
    { try { res = Integer.parseInt(att); } 
      catch (Exception _e) 
      { try { res = Long.parseLong(att); } 
        catch (Exception _f) 
        { try { res = Double.parseDouble(att); } 
          catch (Exception _x) 
          { System.err.println("!! Wrong value: " + att + " for numeric feature " + att); 
            return res; 
          }
        } 
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

  public double getNumericValue(Expression expr, ModelSpecification mod)
  { if (expr instanceof BasicExpression) 
    { BasicExpression be = (BasicExpression) expr; 
      if ("size".equals(be.data))
      { Expression arg = be.getObjectRef(); 
        if (arg.isString())
        { String str = getStringValue(arg,mod); 
          if (str != null) 
          { return str.length()-2; } 
          return 0; 
        } 
        else if (arg.isCollection())
        { Vector vect = getCollectionValue(arg,mod);
          if (vect != null)  
          { return vect.size(); } 
          return 0; 
        }
      } 
      Vector atts = be.decompose(); 
      Attribute att = new Attribute(atts); 
      return getNumericValue(att,mod); 
    } 
    else if (expr instanceof UnaryExpression) 
    { UnaryExpression ue = (UnaryExpression) expr; 
      Expression arg = ue.getArgument();   
      if (ue.operator.equals("->size"))
      { if (arg.isString())
        { String str = getStringValue(arg,mod);

          // System.out.println(">>> Size of string " + str); 
 
          if (str != null) 
          { if (str.startsWith("\"") && str.endsWith("\""))
            { return str.length()-2; }
            return str.length(); 
          }  
          return 0; 
        } 
        else if (arg.isCollection())
        { Vector vect = getCollectionValue(arg,mod); 
          if (vect != null) 
          { return vect.size(); } 
          return 0;  
        } 
      } 
      else if (ue.operator.equals("->sin"))
      { double d1 = getNumericValue(arg,mod);
        return Math.sin(d1); 
      } 
      else if (ue.operator.equals("->cos"))
      { double d1 = getNumericValue(arg,mod);
        return Math.cos(d1); 
      } 
      else if (ue.operator.equals("->tan"))
      { double d1 = getNumericValue(arg,mod);
        return Math.tan(d1); 
      } 
      // else if (ue.operator.equals("->sinh"))
      // { double d1 = getNumericValue(arg,mod);
      //   return Math.sinh(d1); 
      // } 
      // else if (ue.operator.equals("->cosh"))
      // { double d1 = getNumericValue(arg,mod);
      //   return Math.cosh(d1); 
      // } 
      // else if (ue.operator.equals("->tanh"))
      // { double d1 = getNumericValue(arg,mod);
      //  return Math.tanh(d1); 
      // } 
      else if (ue.operator.equals("->round"))
      { double d1 = getNumericValue(arg,mod);
        return (int) Math.round(d1); 
      } 
      else if (ue.operator.equals("->abs"))
      { double d1 = getNumericValue(arg,mod);
        return Math.abs(d1); 
      } 
      else if (ue.operator.equals("->floor"))
      { double d1 = getNumericValue(arg,mod);
        return (int) Math.floor(d1); 
      } 
      else if (ue.operator.equals("->ceil"))
      { double d1 = getNumericValue(arg,mod);
        return (int) Math.ceil(d1); 
      } 
      else if (ue.operator.equals("->sqrt"))
      { double d1 = getNumericValue(arg,mod);
        return Math.sqrt(d1); 
      } 
      else if (ue.operator.equals("->cbrt"))
      { double d1 = getNumericValue(arg,mod);
        return Math.pow(d1, (1.0/3)); 
      } 
      else if (ue.operator.equals("->log"))
      { double d1 = getNumericValue(arg,mod);
        return Math.log(d1); 
      } 
      else if (ue.operator.equals("->exp"))
      { double d1 = getNumericValue(arg,mod);
        return Math.exp(d1); 
      } 
      else if (ue.operator.equals("->max"))
      { Vector v1 = getCollectionValue(arg,mod);
        return AuxMath.numericMax(v1); 
      } 
      else if (ue.operator.equals("->min"))
      { Vector v1 = getCollectionValue(arg,mod);
        return AuxMath.numericMin(v1); 
      } 
      else if (ue.operator.equals("->sum"))
      { Vector v1 = getCollectionValue(arg,mod);
        return AuxMath.numericSum(v1); 
      } 
      else if (ue.operator.equals("->prd"))
      { Vector v1 = getCollectionValue(arg,mod);
        return AuxMath.numericPrd(v1); 
      } 
      else if ("->any".equals(ue.operator))
      { Vector v1 = getCollectionValue(arg,mod); 
        if (v1 != null && v1.size() > 0)
        { int vsize = v1.size(); 
          return ((Double) v1.get(vsize/2)).doubleValue();
        }  
        return 0.0; 
      } 
    }
    else if (expr instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) expr; 
      Expression lft = be.getLeft(); 
      Expression rgt = be.getRight(); 
      
      if ("->at".equals(be.operator))
      { Vector col = getCollectionValue(lft,mod); 
        if (col == null || col.size() == 0) 
        { return 0.0; }
        double indx = getNumericValue(rgt,mod);
        
        if (indx > 0)
        { return ((Double) col.get((int) (indx-1))).doubleValue(); }
        return 0.0; 
      } // Only for sequences, not maps
      else if (be.operator.equals("+"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return d1 + d2; 
      }
      else if (be.operator.equals("*"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return d1 * d2; 
      }
      else if (be.operator.equals("mod"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return d1 % d2; 
      }
      else if (be.operator.equals("-"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return d1 - d2; 
      }
      else if (be.operator.equals("/") || 
               be.operator.equals("div"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return d1 / d2; 
      }
      else if (be.operator.equals("->pow"))
      { double d1 = getNumericValue(lft,mod); 
        double d2 = getNumericValue(rgt,mod); 
        return Math.pow(d1,d2); 
      }
    } 
    return 0; 
  } 

  public String getStringValue(Expression expr, ModelSpecification mod)
  { if (expr instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) expr; 
      Expression lft = be.getLeft(); 
      Expression rgt = be.getRight(); 
      
      if ("+".equals(be.operator))
      { String s1 = ""; 
        if (lft.isString())
        { s1 = getStringValue(lft,mod); } 
        else if (lft.isNumeric())
        { s1 = "" + getNumericValue(lft,mod); } 
        String s2 = ""; 
        if (rgt.isString())
        { s2 = getStringValue(rgt,mod); } 
        else if (rgt.isNumeric())
        { s2 = "" + getNumericValue(rgt,mod); } 
        return s1 + s2; 
      } 
      else if ("->at".equals(be.operator))
      { Vector col = getCollectionValue(lft,mod); 
        if (col == null || col.size() == 0) 
        { return ""; }

        System.out.println(">> Collection value is " + col); 

        double indx = getNumericValue(rgt,mod);
        
        if (indx > 0)
        { return "" + col.get((int) (indx-1)); }
        return ""; 
      } // Only for sequences, not maps

    } 
    else if (expr instanceof UnaryExpression) 
    { UnaryExpression ue = (UnaryExpression) expr; 
      Expression arg = ue.getArgument(); 
      
      if ("->toLowerCase".equals(ue.operator))
      { String s1 = getStringValue(arg,mod); 
        if (s1 == null) { return null; } 
        return s1.toLowerCase(); 
      } 
      else if ("->toUpperCase".equals(ue.operator))
      { String s1 = getStringValue(arg,mod); 
        if (s1 == null) { return null; } 
        return s1.toUpperCase(); 
      } 
      else if ("->trim".equals(ue.operator))
      { String s1 = getStringValue(arg,mod); 
        if (s1 == null) { return null; } 
        return s1.trim(); 
      } // But handle the " " at ends if they exist
      else if ("->sum".equals(ue.operator))
      { Vector v1 = getCollectionValue(arg,mod); 
        return AuxMath.stringSum(v1); 
      } 
      else if ("->min".equals(ue.operator))
      { Vector v1 = getCollectionValue(arg,mod); 
        return AuxMath.stringMin(v1); 
      } 
      else if ("->max".equals(ue.operator))
      { Vector v1 = getCollectionValue(arg,mod); 
        return AuxMath.stringMax(v1); 
      } 
      else if ("->any".equals(ue.operator))
      { Vector v1 = getCollectionValue(arg,mod); 
        if (v1 != null && v1.size() > 0)
        { int vsize = v1.size(); 
          return "" + v1.get(vsize/2);
        }  
        return null; 
      } 
    } 
    else if (expr instanceof BasicExpression) 
    { BasicExpression bexpr = (BasicExpression) expr; 
      if (bexpr.isValue())
      { return bexpr.getData(); } 
      Vector atts = bexpr.decompose(); 
      Attribute att = new Attribute(atts); 
      return getStringValue(att,mod); 
    } 
    return ""; 
  } 

  public String getStringValue(Attribute att, ModelSpecification mod) 
  { String res = ""; 

    if (att == null) 
    { return "null"; } 

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
      { return "null"; } 
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

  public ObjectSpecification getObjectValue(Expression expr, ModelSpecification mod)
  { if (expr instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) expr; 
      Expression left = be.getLeft(); 
      Expression right = be.getRight(); 

      if ("->at".equals(be.operator))
      { Vector col = getCollectionValue(left,mod); 
        if (col == null || col.size() == 0) 
        { return null; }
        double indx = getNumericValue(right,mod);
        System.out.println("??? Value of " + right + " is " + indx);   
        if (indx > 0)
        { return (ObjectSpecification) col.get((int) (indx-1)); }
        return null; 
      } // Only for sequences, not maps
    } 
    else if (expr instanceof UnaryExpression) 
    { UnaryExpression ue = (UnaryExpression) expr; 
      Expression arg = ue.getArgument(); 
      
      if ("->first".equals(ue.operator))
      { Vector col = getCollectionValue(arg,mod); 
        if (col == null || col.size() == 0) 
        { return null; } 
        return (ObjectSpecification) col.get(0); 
      } 
      else if ("->last".equals(ue.operator))
      { Vector col = getCollectionValue(arg,mod); 
        if (col == null || col.size() == 0) 
        { return null; } 
        return (ObjectSpecification) col.get(col.size()); 
      } 
      else if ("->any".equals(ue.operator))
      { Vector col = getCollectionValue(arg,mod); 
        if (col == null || col.size() == 0) 
        { return null; } 
        int indx = col.size()/2; 
        return (ObjectSpecification) col.get(indx); 
      } 
    } 
    else if (expr instanceof BasicExpression) 
    { Vector atts = ((BasicExpression) expr).decompose(); 
      Attribute att = new Attribute(atts); 
      return getObjectValue(att,mod); 
    } 
    return null; 
  } 

  public ObjectSpecification getObjectValue(Attribute att, ModelSpecification mod) 
  { String res = ""; 
    String attname = att.getName(); 

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
      return obj.getObjectValue(new Attribute(pathtail), mod); 
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

  public String getEnumerationValue(Expression expr, ModelSpecification mod)
  { if (expr.isFeature()) 
    { Vector atts = ((BasicExpression) expr).decompose(); 
      Attribute att = new Attribute(atts); 
      String val = getEnumerationValue(att,mod); 
	  // System.err.println(">>> Enumeration value of (" + this + ")." + expr + " is: " + val);
	  return val;  
    } 
	return "" + expr; 
  } 

  public ObjectSpecification[] getCollectionAsObjectArray(Attribute att, ModelSpecification mod)
  { Vector res = getCollectionValue(att,mod);

    // System.out.println("+++---> Value of " + att + " on " + this + " = " + res); 
 
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

  public ASTTerm getTreeValue(Attribute att, ModelSpecification mod) 
  { String nme = att.getName(); 
    return getTree(nme); 
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
  } // expecting either a collection of objects or a 
    // collection of basic values of the same type,
    // represented as strings.

  public Vector getCollectionValue(Expression expr, ModelSpecification mod)
  { System.out.println(">>> Evaluating collection value of " + expr); 

    if (expr instanceof SetExpression) 
    { SetExpression sexpr = (SetExpression) expr; 
      Vector res = new Vector();
      Vector elems = sexpr.getElements();  

      if (expr.isOrdered())
      { for (int i = 0; i < elems.size(); i++) 
        { Expression elem = (Expression) elems.get(i); 
          Object val = getActualValueOf(elem,mod);

          System.out.println(">> Actual value of " + elem + " is " + val); 

          if (val != null) 
          { res.add(val); }
        }  
      } 
      else 
      { for (int i = 0; i < elems.size(); i++) 
        { Expression elem = (Expression) elems.get(i); 
          Object val = getActualValueOf(elem,mod);
          if (val != null)
          { if (res.contains(val)) { }  
            else 
            { res.add(val); }
          } 
        }  
      } 

      return res; 
    } 
    else if (expr instanceof BasicExpression) 
    { BasicExpression be = (BasicExpression) expr; 
      if ("allInstances".equals(be.data))
      { Expression eobjref = be.getObjectRef(); 
        String ename = "" + eobjref; 
        // System.out.println(">>> looking up " + ename); 
        Vector eobjects = mod.getObjectsOf(ename); 
        return eobjects; 
      } 
      else if ("subrange".equals(be.data) || 
               "subSequence".equals(be.data))
      { Expression eobjref = be.getObjectRef(); 
        Expression par1 = be.getParameter(1); 
        Expression par2 = be.getParameter(2);
        double p1 = getNumericValue(par1,mod); 
        double p2 = getNumericValue(par2,mod);  
        Vector objs = getCollectionValue(eobjref,mod); 
        return AuxMath.subrange(objs,(int) p1, (int) p2); 
      } 
      Vector atts = ((BasicExpression) expr).decompose(); 
      Attribute att = new Attribute(atts); 
      return getCollectionValue(att,mod); 
    } 
    else if (expr instanceof UnaryExpression)
    { UnaryExpression uecoll = (UnaryExpression) expr;
      Expression arg = uecoll.getArgument(); 
 
      String op = uecoll.operator;
 
      if (op.equals("->front"))
      { Vector argVal = getCollectionValue(arg,mod); 
        Vector res = new Vector();
        res.addAll(argVal); 
        if (res.size() > 0)
        { res.remove(res.size()-1); }  
        return res;  
      } 
      else if (op.equals("->tail"))
      { Vector argVal = getCollectionValue(arg,mod); 
        Vector res = new Vector();
        res.addAll(argVal); 
        if (res.size() > 0) 
        { res.remove(0); }  
        return res;  
      } 
      else if (op.equals("->reverse"))
      { Vector argVal = getCollectionValue(arg,mod); 
        Vector res = new Vector();
        res.addAll(argVal);
        Vector rres = new Vector();  
        Collections.reverse(res);
        rres.addAll(res); 
        return rres;   
      } 
    } 
    else if (expr instanceof BinaryExpression)
    { BinaryExpression becoll = (BinaryExpression) expr;
      Expression lft = becoll.getLeft(); 
      Expression rgt = becoll.getRight(); 
 
      String op = becoll.operator; 
      if (op.equals("->union"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Vector rightVal = getCollectionValue(rgt,mod); 
        Vector res = new Vector();
        if (Type.isSequenceType(becoll.getType()))
        { res.addAll(leftVal); 
          res.addAll(rightVal); 
          return res;
        } 
        else 
        { res = AuxMath.setUnion(leftVal,rightVal); 
          return res; 
        }  
      } 
      else if (op.equals("->including"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Object rightVal = getValue(expr,mod); 
        Vector res = new Vector(); 
        res.addAll(leftVal); 

        if (Type.isSequenceType(becoll.getType()))
        { res.add(rightVal); } 
        else if (res.contains(rightVal)) { } 
        else 
        { res.add(rightVal); } 

        return res; 
      }
      else if (op.equals("->intersection"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Vector rightVal = getCollectionValue(rgt,mod); 
        Vector res = new Vector(); 
        res.addAll(leftVal); 
        res.retainAll(rightVal); 
        return res; 
      } 
      else if (op.equals("->excluding"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Object rightVal = getValue(expr,mod); 
        Vector res = new Vector(); 
        res.addAll(leftVal); 
        Vector rvals = new Vector(); 
        rvals.add(rightVal);
        res.removeAll(rvals);  
        return res; 
      }
      else if (op.equals("-"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Vector rightVal = getCollectionValue(rgt,mod); 
        Vector res = new Vector(); 
        res.addAll(leftVal); 
        res.removeAll(rightVal); 
        return res; 
      } 
      else if (op.equals("->select"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        // for each x : leftVal, evaluate rightVal
        // System.out.println(">> ->select Left value = " + leftVal); 
        Vector res = new Vector(); 
        if (leftVal == null) 
        { return res; } 
        for (int i = 0; i < leftVal.size(); i++)
        { if (leftVal.get(i) instanceof ObjectSpecification)
          { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
            if (x.satisfiesCondition(rgt,mod))
            { res.add(x); } 
          } 
        } 
        // System.out.println(">> ->select result = " + res); 
        return res; 
      } 
      else if (op.equals("->reject"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Vector res = new Vector(); 
        if (leftVal == null) 
        { return res; } 
        for (int i = 0; i < leftVal.size(); i++)
        { if (leftVal.get(i) instanceof ObjectSpecification)
          { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
            if (x.satisfiesCondition(rgt,mod))
            { } 
            else 
            { res.add(x); } 
          } 
        } 
        return res; 
      } 
      else if (op.equals("->collect"))
      { Vector leftVal = getCollectionValue(lft,mod); 
        Vector res = new Vector(); 
        if (leftVal == null) 
        { return res; } 
        for (int i = 0; i < leftVal.size(); i++)
        { if (leftVal.get(i) instanceof ObjectSpecification)
          { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
            res.add(x.getActualValueOf(rgt,mod));  
          } 
        } 
        return res; 
      } 
    } 
    return new Vector(); 
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
        if (objs == null) 
        { return res; } 
        
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

  public ASTTerm getTree(String att) 
  { Object x = attvalues.get(att); 
    if (x == null) 
    { return null; }
    if (x instanceof ASTTerm)
    { return (ASTTerm) x; }
    if (x instanceof String) 
    { String stree = (String) x; 
      Compiler2 cc = new Compiler2(); 
      ASTTerm tt = cc.parseGeneralAST(stree);
      if (tt == null) 
      { JOptionPane.showMessageDialog(null, "Warning: invalid AST: " + stree, 
           "", JOptionPane.ERROR_MESSAGE);  
      }
      return tt;  
    } 
    return null; 
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
  
    if ("true".equals(att)) 
    { return true; }
    else if ("false".equals(att))
    { return false; }
	 
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
      if (objs.size() == 0) 
      { return null; } 
      return (ObjectSpecification) objs.get(0); 
    }
    return null;  
  }  

  public String getValue(Expression expr, ModelSpecification mod)
  { String v = expr + ""; 
    Type t = expr.getType(); 

    if ("true".equals(expr + ""))
    { return "true"; }
    else if ("false".equals(expr + ""))
    { return "false"; }
    else if ("self".equals(expr + ""))
    { return this + ""; }
    
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

  public static Vector getAllValuesOf(Expression expr, Vector objs, ModelSpecification mod)
  { Vector res = new Vector(); 
    for (int i = 0; i < objs.size(); i++) 
    { ObjectSpecification obj = (ObjectSpecification) objs.get(i); 
      Object x = obj.getValueOf(expr,mod); 
      if (x != null) 
      { res.add(x); } 
    } 
    return res; 
  } 

  public Object getValueOf(Expression expr, ModelSpecification mod)
  { // assume for the present, expr is boolean. 
    if (expr.isBoolean())
    { if (satisfiesCondition(expr,mod))
      { return "true"; } 
      else
      { return "false"; }
    } 
	
    if (expr.isNumeric())
    { return "" + getNumericValue(expr,mod); }
	
    if (expr.isCollection())
    { return getCollectionValue(expr,mod); } 
	
    if (expr.isEntity())
    { if ("self".equals(expr + ""))
      { return this + ""; } 
      return getObjectValue(expr,mod); 
    } // and self.att, etc. 
	
    if (expr.isEnumerated()) // must be a BasicExpression
    { return getEnumerationValue(expr,mod); }
	
    return getStringValue(expr,mod);  // Strings
  } 

  public Object getActualValueOf(Expression expr, ModelSpecification mod)
  { // assume for the present, expr is boolean. 
    if (expr.isBoolean())
    { if (satisfiesCondition(expr,mod))
      { return new Boolean(true); } 
      else
      { return new Boolean(false); }
    } 
	
    if (expr.isNumeric())
    { return new Double(getNumericValue(expr,mod)); }
	
    if (expr.isCollection())
    { return getCollectionValue(expr,mod); } 
	
    if (expr.isEntity())
    { if ("self".equals(expr + ""))
      { return this; } 
      return getObjectValue(expr,mod); 
    } // and self.att, etc. 
	
    if (expr.isEnumerated()) // must be a BasicExpression
    { return getEnumerationValue(expr,mod); }
	
    return getStringValue(expr,mod);  // Strings
  } 

  public Object getActualValueOf(Attribute att, ModelSpecification mod)
  { if (att.isBoolean())
    { if (satisfiesBasicCondition(att,mod))
      { return new Boolean(true); } 
      else
      { return new Boolean(false); }
    } 
	
    if (att.isNumeric())
    { return new Double(getNumericValue(att,mod)); }
	
    if (att.isCollection())
    { return getCollectionValue(att,mod); } 
	
    if (att.isEntity())
    { if ("self".equals(att + ""))
      { return this; } 
      return getObjectValue(att,mod); 
    } // and self.att, etc. 
	
    if (att.isEnumerated()) // must be a BasicExpression
    { return getEnumerationValue(att,mod); }
	
    return getStringValue(att,mod);  // Strings
  } 


  public boolean satisfiesConditionalCondition(ConditionalExpression cond, ModelSpecification mod) 
  { Expression tst = cond.getTest(); 
    Expression ifexp = cond.getIf(); 
    Expression elseexp = cond.getElse(); 
    boolean sattst = satisfiesCondition(tst, mod); 
    if (sattst) 
    { return satisfiesCondition(ifexp,mod); } 
    else 
    { return satisfiesCondition(elseexp,mod); } 
  } 

  public boolean satisfiesBinaryCondition(BinaryExpression cond, ModelSpecification mod) 
  { String condop = cond.getOperator();
    Expression lft = cond.getLeft(); 
    Expression rgt = cond.getRight(); 
 
    if ("&".equals(condop))
    { boolean sat1 = satisfiesCondition(cond.getLeft(),mod); 
      boolean sat2 = satisfiesCondition(cond.getRight(),mod);
      // System.out.println(this + " satisfies? " + sat1 + " " + cond.getLeft()); 
      // System.out.println(this + " satisfies? " + sat2 + " " + cond.getRight()); 
      return sat1 && sat2; 
    } 
    else if ("or".equals(condop))
    { return satisfiesCondition(cond.getLeft(),mod) || 
             satisfiesCondition(cond.getRight(),mod); 
    } 
    else if ("=".equals(condop))
    { String valleft = getValueOf(cond.getLeft(),mod) + ""; 
      String valright = getValueOf(cond.getRight(),mod) + "";
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft != null && valright != null && valleft.equals(valright))
      { return true; } 
      return false; 
    } 
    else if ("/=".equals(condop))
    { String valleft = getValueOf(cond.getLeft(),mod) + ""; 
      String valright = getValueOf(cond.getRight(),mod) + ""; 
      if (valleft != null && valright != null && valleft.equals(valright))
      { return false; }
      else if (valleft != null && !valleft.equals(valright))
      { return true; }  
      return false; 
    } 
    else if (">".equals(condop) && cond.getLeft().isNumeric() && cond.getRight().isNumeric())
    { double valleft = getNumericValue(cond.getLeft(),mod); 
      double valright = getNumericValue(cond.getRight(),mod);
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft > valright)
      { return true; } 
      return false; 
    } 
    else if ("<".equals(condop) && cond.getLeft().isNumeric() && cond.getRight().isNumeric())
    { double valleft = getNumericValue(cond.getLeft(),mod); 
      double valright = getNumericValue(cond.getRight(),mod);
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft < valright)
      { return true; } 
      return false; 
    } 
    else if ("<=".equals(condop) && cond.getLeft().isNumeric() && cond.getRight().isNumeric())
    { double valleft = getNumericValue(cond.getLeft(),mod); 
      double valright = getNumericValue(cond.getRight(),mod);
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft <= valright)
      { return true; } 
      return false; 
    } 
    else if (">=".equals(condop) && cond.getLeft().isNumeric() && cond.getRight().isNumeric())
    { double valleft = getNumericValue(cond.getLeft(),mod); 
      double valright = getNumericValue(cond.getRight(),mod);
      // System.out.println("---> Value of " + cond.getLeft() + " is ---> " + valleft); 
      // System.out.println("---> Value of " + cond.getRight() + " is ---> " + valright); 
 
      if (valleft >= valright)
      { return true; } 
      return false; 
    } 
    else if ("->oclIsTypeOf".equals(condop))
    { ObjectSpecification obj = getObjectValue(cond.getLeft(),mod);
      // getReferredObject(cond.getLeft() + "",mod); 
      if (obj != null && 
          obj.objectClass.equals(cond.getRight() + ""))
      { return true; }
      return false;  
    } 
    else if ("->oclIsKindOf".equals(condop))
    { ObjectSpecification obj = getObjectValue(cond.getLeft(),mod);
      // getReferredObject(cond.getLeft() + "",mod); 
      if (obj != null && obj.entity != null && 
          Entity.isDescendantOrEqualTo(obj.entity, cond.getRight() + ""))
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
        { return true; } // assumes string values always have "". 
      } 
      return false; 
    } 
    else if ("->includes".equals(condop))
    { Vector leftValue = getCollectionValue(cond.getLeft(),mod); 
      Object rightValue = getValueOf(cond.getRight(),mod); 
      if (leftValue != null && leftValue.contains(rightValue))
      { return true; } 
      return false; 
    } 
    else if ("->excludes".equals(condop))
    { Vector leftValue = getCollectionValue(cond.getLeft(),mod); 
      Object rightValue = getValueOf(cond.getRight(),mod); 
      if (leftValue != null && leftValue.contains(rightValue))
      { return false; } 
      return true; 
    } 
    else if ("->includesAll".equals(condop))
    { Vector leftValue = getCollectionValue(cond.getLeft(),mod); 
      Vector rightValue = getCollectionValue(cond.getRight(),mod); 
      if (leftValue != null && leftValue.containsAll(rightValue))
      { return true; } 
      return false; 
    } 
    else if ("->excludesAll".equals(condop))
    { Vector leftValue = getCollectionValue(cond.getLeft(),mod); 
      Vector rightValue = getCollectionValue(cond.getRight(),mod); 
      Vector intersect = new Vector(); 
      
      intersect.addAll(leftValue); 
      intersect.retainAll(rightValue); 
      if (intersect.size() > 0)
      { return false; } 
      return true; 
    } 
    else if (condop.equals("->exists"))
    { Vector leftVal = getCollectionValue(lft,mod); 
      System.out.println(">> left val for ->exists: " + leftVal); 

      if (leftVal == null) 
      { return false; } 

      for (int i = 0; i < leftVal.size(); i++)
      { if (leftVal.get(i) instanceof ObjectSpecification)
        { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
          if (x.satisfiesCondition(rgt,mod))
          { System.out.println(">> " + x + " satisfies " + rgt); 
            return true; 
          } 
        } 
      } 
      return false;  
    } 
    else if (condop.equals("->exists1"))
    { Vector leftVal = getCollectionValue(lft,mod); 
      if (leftVal == null) 
      { return false; } 

      System.out.println(">> left val for ->exists1 " + leftVal); 

      boolean found = false; 
      for (int i = 0; i < leftVal.size(); i++)
      { if (leftVal.get(i) instanceof ObjectSpecification)
        { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
          boolean xok = x.satisfiesCondition(rgt,mod); 
          if (xok && found)
          { return false; } 
          else if (xok) 
          { found = true; } 
        } 
      } 
      return found;  
    } 
    else if (condop.equals("->forAll"))
    { Vector leftVal = getCollectionValue(lft,mod); 
      if (leftVal == null) 
      { return true; } 
      for (int i = 0; i < leftVal.size(); i++)
      { if (leftVal.get(i) instanceof ObjectSpecification)
        { ObjectSpecification x = (ObjectSpecification) leftVal.get(i); 
          if (x.satisfiesCondition(rgt,mod))
          { } 
          else 
          { return false; } 
        } 
      } 
      return true;  
    } 
    return false; 
  }  

  public boolean satisfiesBasicCondition(BasicExpression cond, ModelSpecification mod)
  { String val = getValue(cond,mod); 
    if ("true".equals(val + "")) 
    { return true; } 
    return false; 
  } 

  public boolean satisfiesBasicCondition(Attribute cond, ModelSpecification mod)
  { Object val = getValueOf(cond,mod); 
    if ("true".equals(val + "")) 
    { return true; } 
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
    else if ("->oclIsUndefined".equals(op + ""))
    { String val = getValue(arg,mod); 
      if (val == null) 
      { return true; }
      return false; 
    }
    else if ("->oclIsNew".equals(op + ""))
    { String val = getValue(arg,mod); 
      if (val == null) 
      { return false; }
      return true; 
    }
    return false; 
  } 

  public boolean satisfiesSetCondition(SetExpression cond, ModelSpecification mod)
  { return false; } 

  public boolean satisfiesCondition(Expression cond, ModelSpecification mod)
  { if (cond == null) 
    { return true; } 

    if (cond instanceof BinaryExpression) 
    { return satisfiesBinaryCondition((BinaryExpression) cond, mod); }
    else if (cond instanceof BasicExpression)
    { return satisfiesBasicCondition((BasicExpression) cond, mod); }
    else if (cond instanceof UnaryExpression)
    { return satisfiesUnaryCondition((UnaryExpression) cond, mod); }
    else if (cond instanceof SetExpression)
    { return satisfiesSetCondition((SetExpression) cond, mod); }
    else if (cond instanceof ConditionalExpression)
    { return satisfiesConditionalCondition((ConditionalExpression) cond, mod); }
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
  // numbers, booleans, objects & enums are entered as a string with no quotes, but actual string values have "". 
  // ASTs begin and end with ( )

  public void addAttribute(String att, Vector value)
  { if (atts.contains(att)) { } 
    else 
    { atts.add(att); } 
    attvalues.put(att,value); 
  }
  // Likewise for trees, which are nested vectors. 

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
  
  
  public static void main(String[] args) 
  { ModelSpecification mod = new ModelSpecification();
    Entity ent = new Entity("A"); 
	 
    Expression e1 = new BasicExpression(true); 
    Expression e2 = new BasicExpression(-3); 
    Expression e3 = new BasicExpression("\"My name\""); 
    e3.setType(new Type("String", null)); 
    Expression e4 = new BasicExpression("self"); 
    e4.setType(new Type(ent)); 
    Expression e5 = new BasicExpression(5.8);
    BinaryExpression add = new BinaryExpression("+", e2, e5); 
    add.setType(new Type("double", null));  
    Expression e6 = new BasicExpression("\"me\""); 
    e6.setType(new Type("String", null)); 
    BinaryExpression suff = new BinaryExpression("->hasSuffix", e3, e6); 
    suff.setType(new Type("boolean", null)); 
    BinaryExpression less = new BinaryExpression("<", e2, e5); 
    less.setType(new Type("boolean", null));  
	
    ObjectSpecification obj = new ObjectSpecification("a1", "A");
    obj.setEntity(ent);  
    mod.addObject(obj); 
    System.out.println(mod + ""); 
	
	System.out.println(obj.getValueOf(e1,mod)); 
	System.out.println(obj.getValueOf(e2,mod)); 
	System.out.println(obj.getValueOf(e3,mod)); 
	System.out.println(obj.getValueOf(e4,mod)); 
	System.out.println(obj.getValueOf(add,mod));
	// System.out.println(obj.getValueOf(e6,mod));
	
	System.out.println("\"name\"->hasSuffix(\"me\")  is " + obj.getValueOf(suff,mod));
	System.out.println(obj.getValueOf(less,mod));
	
	Vector vect = new Vector(); 
	vect.add("0.0"); 
	vect.add("-3.0"); 
	vect.add("6.0"); 
	obj.setValue("r", vect); 
	
	Type intset = new Type("Set", null); 
	intset.setElementType(new Type("double", null)); 
	
	Attribute r = new Attribute("r", intset, ModelElement.INTERNAL); 
	
	BasicExpression rexp = new BasicExpression(r); 
	BinaryExpression isin = new BinaryExpression("->includes", rexp, e2);
	isin.setType(new Type("boolean", null)); 
	
	System.out.println(obj.getValueOf(rexp,mod)); 
	System.out.println(obj.getValueOf(isin,mod)); 
  }

}
