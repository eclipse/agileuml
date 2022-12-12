import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: utilities */ 

public class XMLNode
{ String tag;
  Vector attributes = new Vector(); // of XMLAttribute
  Vector subnodes = new Vector(); // of XMLNode
  String content = ""; 

  XMLNode()
  { tag = ""; }

  XMLNode(String s)
  { tag = s; }

  public String getTag()
  { return tag; } 

  public String getContent()
  { return content; } 

  public void settag(String t)
  { tag = t; } 

  public void setContent(String c)
  { content = c; } 

  public void setsubnodes(Vector sn)
  { subnodes = sn; }

  public Vector getSubnodes()
  { return subnodes; } 

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
      if (aname.equals(xatt.getName()))
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

  public String toString()
  { String res = "<" + tag; 
    for (int i = 0; i < attributes.size(); i++) 
    { XMLAttribute xatt = (XMLAttribute) attributes.get(i); 
      res = res + " " + xatt; 
    } 

    if (content.length() == 0 && subnodes.size() == 0)
    { return res + "/>\n"; } 

    res = res + ">" + content + "\n"; 
    for (int j = 0; j < subnodes.size(); j++) 
    { res = res + subnodes.get(j); } 
    return res + "</" + tag + ">\n"; 
  } 
}

