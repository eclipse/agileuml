package kmehr;

import java.util.Vector;

public class XMLNode
{ String tag;
  Vector attributes = new Vector(); // of XMLAttribute
  Vector subnodes = new Vector(); // of XMLNode

  XMLNode()
  { tag = ""; }

  XMLNode(String s)
  { tag = s; }

  public String getTag()
  { return tag; }

  public void settag(String t)
  { tag = t; }

  public void setsubnodes(Vector sn)
  { subnodes = sn; }

  public Vector getSubnodes()
  { return subnodes; }

  public Vector getSubnodesWithTag(String nname)
  { Vector res = new Vector(); 
    for (int i = 0; i < subnodes.size(); i++) 
    { XMLNode sn = (XMLNode) subnodes.get(i); 
      if (nname.equals(sn.getTag()))
      { res.add(sn); } 
    } 
    return res; 
  } 

  public XMLNode getSubnodeWithTag(String nname)
  { for (int i = 0; i < subnodes.size(); i++) 
    { XMLNode sn = (XMLNode) subnodes.get(i); 
      if (nname.equals(sn.getTag()))
      { return sn; } 
    } 
    return null; 
  } 

  public Vector getAttributes()
  { return attributes; }

  public void setattributes(Vector sn)
  { attributes = sn; }

  public void addAttribute(XMLAttribute xatt)
  { attributes.add(xatt); }

  public void addattributes(XMLAttribute xatt)
  { attributes.add(xatt); }

  public String getAttributeValue(String aname)
  { String res = null; 
    for (int i = 0; i < attributes.size(); i++) 
    { XMLAttribute xatt = (XMLAttribute) attributes.get(i); 
      if (aname.equalsIgnoreCase(xatt.getName()))
      { res = xatt.getValue();
        break;
      } 
    }
    if (res == null) { return res; } 

    int len = res.length(); 
    if ("\"".equals(res.charAt(0) + ""))
    { res = res.substring(1,len); }
    len = res.length();
    if ("\"".equals(res.charAt(len-1) + ""))
    { res = res.substring(0,len-1); }
      
    return res;
  } 

  public String getString(String aname)
  { String val = getAttributeValue(aname); 
    return val; 
  }

  public int getInt(String aname)
  { String val = getAttributeValue(aname); 
    try { 
       return Integer.parseInt(val);
    } catch (Exception e) { return 0; }
  }

  public long getLong(String aname)
  { String val = getAttributeValue(aname); 
    try { 
       return Long.parseLong(val);
    } catch (Exception e) { return 0; }
  }

  public double getDouble(String aname)
  { String val = getAttributeValue(aname); 
    try { 
       return Double.parseDouble(val);
    } catch (Exception e) { return 0.0; }
  }

  public boolean getBoolean(String aname)
  { String val = getAttributeValue(aname); 
    try { 
       return Boolean.parseBoolean(val);
    } catch (Exception e) { return false; }
  }

  public java.util.HashMap getMap(String aname)
  { return new java.util.HashMap(); }

  public String toString()
  { String res = "<" + tag; 
    for (int i = 0; i < attributes.size(); i++) 
    { XMLAttribute xatt = (XMLAttribute) attributes.get(i);
      res = res + " " + xatt;
    } 
    if (subnodes.size() == 0)
    { return res + "/>\n"; }

    res = res + ">\n"; 
    for (int j = 0; j < subnodes.size(); j++)
    { res = res + subnodes.get(j); } 
    return res + "</" + tag + ">\n";
  } 
}

