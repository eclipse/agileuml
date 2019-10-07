import java.util.StringTokenizer; 
import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class XMLAttribute
{ String name;
  String value;

  XMLAttribute()
  { name = ""; value = ""; }
  
  XMLAttribute(String n, String v)
  { name = n; value = v; }

  public String toString()
  { return name + "=" + value; } 

  public String getName()
  { return name; } 
 
  public String getValue()
  { return value; } 

  public void setname(String n)
  { name = n; } 
 
  public void setvalue(String v)
  { value = v; } 

/*   public void getDataDeclaration(PrintWriter out, String instance, String ent)
  { // returns instance.name = value or value : instance.name
    // as appropriate
    if ("team".equals(name))
    // Attribute att = ent.getDefinedAttribute(name); 
    // if (att != null) // name is an attribute of ent
    { // if ("String".equals(att.getType() + ""))
      out.println(instance + "." + name + " = " + value); 
    } 
    else if ("xPos".equals(name) || "yPos".equals(name) ||
             "number".equals(name)) // trim off the quotes
    { String val = value.substring(1,value.length() - 1); 
      out.println(instance + "." + name + " = " + val); 
    } 
    else // association
    { // Association ast = ent.getDefinedRole(name); 
      if ("field".equals(name)) 
      { // if (ast.getCard2() == ModelElement.ONE)
        // value is single "//@inst.number" or "$id"
        int atind = value.indexOf("@");
        if (atind < 0) 
        { return; }   
        String obj = value.substring(atind + 1, value.length() -1); 
        int dotind = obj.indexOf(".");
        if (dotind < 0) { return; } 
        String obj1 = obj.substring(0,dotind); 
        String obj2 = obj.substring(dotind + 1, obj.length()); 
        out.println(instance + "." + name + " = " + obj1 + obj2); 
        return; 
      } 
      else if ("west".equals(name) || "east".equals(name) || "south".equals(name) ||
               "north".equals(name)) 
      { return; } 
      else // a list of objects
      { StringTokenizer st = new StringTokenizer(value,"\" "); 
        Vector objs = new Vector(); 
        String res = ""; 

        while (st.hasMoreTokens())
        { objs.add(st.nextToken()); }

        for (int i = 0; i < objs.size(); i++) 
        { String oneobj = (String) objs.get(i); 
          // System.out.println(oneobj); 

          int atind = oneobj.indexOf("@");
          if (atind < 0) 
          { continue; }   
          String obj = oneobj.substring(atind + 1, oneobj.length()); 
          int dotind = obj.indexOf(".");
          if (dotind < 0) 
          { // case of a ball: 
            out.println(obj + "0 : " + instance + "." + name);
            return; 
          } 
          String obj1 = obj.substring(0,dotind); 
          String obj2 = obj.substring(dotind + 1, obj.length()); 
          out.println(obj1 + obj2 + " : " + instance + "." + name); 
        }
      } 
    } 
  }  */ 

  public void getDataDeclaration(PrintWriter out, String instance, Entity ent, java.util.Map idmap)
  { // returns instance.name = value or value : instance.name
    // as appropriate

    Attribute att = ent.getDefinedAttribute(name); 
    if (att == null) 
    { Entity subent = ent.searchForSubclassWithAttribute(name); 
      if (subent != null) 
      { att = subent.getDefinedAttribute(name); } 
    } 
    if (att != null) // name is an attribute of ent or of a subclass
    { if ("String".equals(att.getType() + ""))
      { out.println(instance + "." + name + " = " + value); } 
      else // trim off the quotes
      { String val = value.substring(1,value.length() - 1); 
        out.println(instance + "." + name + " = " + val); 
      } 
    } 
    else 
    { Association ast = ent.getDefinedRole(name); 
      if (ast == null) 
      { Entity subent = ent.searchForSubclassWithRole(name); 
        if (subent != null) 
        { ast = subent.getDefinedRole(name); } 
      } 
      if (ast != null && ast.getCard2() == ModelElement.ONE)
      { // value is single "//@inst.number" or "$id"
        int atind = value.indexOf("@");
        if (atind < 0) 
        { String v = (String) idmap.get(value); 
          if (v == null) { return; }  // unrecognised reference
          else 
          { out.println(instance + "." + name + " = " + v); 
            return; 
          } 
        }   
        String obj = value.substring(atind + 1, value.length() -1); 
        int dotind = obj.indexOf(".");
        if (dotind < 0) 
        { out.println(instance + "." + name + " = " + obj + "0");
          return; 
        } 
        String obj1 = obj.substring(0,dotind); 
        String obj2 = obj.substring(dotind + 1, obj.length()); 
        out.println(instance + "." + name + " = " + obj1 + obj2); 
        return; 
      } 
      else // a list of objects
      { StringTokenizer st = new StringTokenizer(value,"\" "); 
        Vector objs = new Vector(); 
        String res = ""; 

        while (st.hasMoreTokens())
        { objs.add(st.nextToken()); }

        for (int i = 0; i < objs.size(); i++) 
        { String oneobj = (String) objs.get(i); 
          // System.out.println(oneobj); 

          int atind = oneobj.indexOf("@");
          if (atind < 0) 
          { String v = (String) idmap.get(oneobj); 
            if (v == null) { continue; }  // unrecognised reference
            else 
            { out.println(v + " : " + instance + "." + name); } 
          }   
          String obj = oneobj.substring(atind + 1, oneobj.length()); 
          int dotind = obj.indexOf(".");
          if (dotind < 0) 
          { out.println(obj + "0 : " + instance + "." + name);
            continue;
          } 
          String obj1 = obj.substring(0,dotind); 
          String obj2 = obj.substring(dotind + 1, obj.length()); 
          out.println(obj1 + obj2 + " : " + instance + "." + name); 
        } 
      } 
    } 
  }  

}
