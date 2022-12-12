import java.util.StringTokenizer; 
import java.util.Vector; 
import java.io.*; 

public class XMLAttribute
{ String name;
  String value;
  
  XMLAttribute(String n, String v)
  { name = n; value = v; }

  public String toString()
  { return name + "=" + value; } 

  public String getName()
  { return name; } 
 
  public String getValue()
  { return value; } 

  public void getDataDeclaration(PrintWriter out, String instance, Entity ent, java.util.Map idmap)
  { // returns instance.name = value or value : instance.name
    // as appropriate
    Attribute att = ent.getDefinedAttribute(name); 
    if (att != null) // name is an attribute of ent
    { if ("String".equals(att.getType() + ""))
      { out.println(instance + "." + name + " = " + value); } 
      else // trim off the quotes
      { String val = value.substring(1,value.length() - 1); 
        out.println(instance + "." + name + " = " + val); 
      } 
    } 
    else 
    { Association ast = ent.getDefinedRole(name); 
      if (ast != null) 
      { if (ast.getCard2() == ModelElement.ONE)
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
          if (dotind < 0) { return; } 
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
            if (dotind < 0) { continue; } 
            String obj1 = obj.substring(0,dotind); 
            String obj2 = obj.substring(dotind + 1, obj.length()); 
            out.println(obj1 + obj2 + " : " + instance + "." + name); 
          }
        } 
      } 
    } 
  }  
}
