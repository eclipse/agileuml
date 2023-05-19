package kmehr;

import java.util.StringTokenizer;
import java.util.Vector; 
import java.io.*; 

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

  public void getDataDeclaration(PrintWriter out, String instance, String ent)
  { }
  public void getDataDeclarationFromXsi(Vector res, Vector atts, Vector stringatts, Vector oneroles, String instance, String ent)
  { if (value.startsWith("\"") && value.endsWith("\""))
    { if (stringatts.contains(name))
      { res.add(instance + "." + name + " = " + value); }
      else if (atts.contains(name)) // trim off the quotes
      { String val = value.substring(1,value.length() - 1); 
        res.add(instance + "." + name + " = " + val);
      } 
    } 
    else if (stringatts.contains(name))
    { res.add(instance + "." + name + " = \"" + value + "\""); }
    else if (atts.contains(name)) // trim off the quotes
    { String val = value; 
      res.add(instance + "." + name + " = " + val);
    } 
    else // association
    { if (oneroles.contains(name)) 
      { int atind = value.indexOf("@");
       if (atind < 0) 
        { return; } // invalid object value
        String obj = value.substring(atind + 1, value.length() -1);
        int dotind = obj.indexOf(".");
        if (dotind < 0) 
        { res.add(instance + "." + name + " = " + obj + "0");
          return; 
        } 
        String obj1 = obj.substring(0,dotind); 
        String obj2 = obj.substring(dotind + 1, obj.length()); 
        res.add(instance + "." + name + " = " + obj1 + obj2);
        return;
      } 
      else // a list of objects
      { StringTokenizer st = new StringTokenizer(value,"\" ");
        Vector objs = new Vector(); 
        
        while (st.hasMoreTokens())
        { objs.add(st.nextToken()); }

        for (int i = 0; i < objs.size(); i++) 
        { String oneobj = (String) objs.get(i);
          
          int atind = oneobj.indexOf("@");
          if (atind < 0) 
          { continue; }   
          String obj = oneobj.substring(atind + 1, oneobj.length());
          int dotind = obj.indexOf(".");
          if (dotind < 0) 
          { res.add(obj + "0 : " + instance + "." + name);
            continue;
          } 
          String obj1 = obj.substring(0,dotind); 
          String obj2 = obj.substring(dotind + 1, obj.length()); 
          res.add(obj1 + obj2 + " : " + instance + "." + name);
        }
      } 
    } 
  }

}
