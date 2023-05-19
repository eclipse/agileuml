package kmehr;

import java.util.Vector; 
import java.util.List; 
import java.io.*;

public class XMLParser
{ static private final int INUNKNOWN = 0;
  static private final int INBASICEXP = 1; 
  static private final int INSYMBOL = 2; 
  static private final int INSTRING = 3; 

  private Vector lexicals; // of StringBuffer
  private static Vector xmlnodes = new Vector();
  private static Vector xmlattributes = new Vector();


  private boolean isXMLSymbolCharacter(char c)
  { return (c == '<' || c == '>' || c == '=' || 
            c == '*' || c == '/' || c == '\\' ||
            // c == '-' || 
      c == ',' || c == '\'' || c == '?' ||
      c == '+' || c == '&' || c == '^' || c == '|' ||
      c == '(' || c == ')' || c == '{' || c == '}' || c == '[' ||
      c == ']' || c == '#'); 
  } 

  private boolean isXMLBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || 
            c == '$' || c == '@' || c == ':' || c == '-'); 
  }

  public static void convertXsiToData(String xmlstring)
  { BufferedWriter brout = null; 
    PrintWriter pwout = null; 
    File outfile = new File("./in.txt");

    try
    { brout = new BufferedWriter(new FileWriter(outfile));
      pwout = new PrintWriter(brout); 
    }
    catch (Exception e)
    { System.out.println("!! Errors with file: " + outfile);
      return; 
    }

    Vector res = new Vector();

    XMLParser comp = new XMLParser(); 
    comp.nospacelexicalanalysisxml(xmlstring); 
    XMLNode xml = comp.parseXML(); 

    java.util.Map instancemap = new java.util.HashMap(); // String --> Vector 
    java.util.Map entmap = new java.util.HashMap();       // String --> String
    Vector entcodes = new Vector();


    Vector enodes = xml.getSubnodes(); // all instances
    for (int i = 0; i < enodes.size(); i++) 
    { XMLNode enode = (XMLNode) enodes.get(i); 
      String cname = enode.getTag(); 
      Vector einstances = (Vector) instancemap.get(cname);
      if (einstances != null) 
      { einstances.add(enode); } 
    } 
      
    for (int j = 0; j < entcodes.size(); j++)
    { String ename = (String) entcodes.get(j); 
      Vector elems = (Vector) instancemap.get(ename);
      for (int k = 0; k < elems.size(); k++) 
      { XMLNode enode = (XMLNode) elems.get(k);
        String tname = enode.getAttributeValue("xsi:type");
        if (tname == null) 
        { tname = (String) entmap.get(ename); }
        else 
        { int colonind = tname.indexOf(":");
          if (colonind >= 0)
          { tname = tname.substring(colonind + 1,tname.length()); }
        }   
        pwout.println(ename + "" + k + " : " + tname);
      } 
    } 

    for (int j = 0; j < entcodes.size(); j++) 
    { String ename = (String) entcodes.get(j); 
      Vector elems = (Vector) instancemap.get(ename); 
      for (int k = 0; k < elems.size(); k++) 
      { XMLNode enode = (XMLNode) elems.get(k);
        Vector atts = enode.getAttributes();
        for (int p = 0; p < atts.size(); p++) 
        { XMLAttribute patt = (XMLAttribute) atts.get(p);
          if (patt.getName().equals("xsi:type") || patt.getName().equals("xmi:id")) { }
          else 
          { patt.getDataDeclaration(pwout,ename + k, (String) entmap.get(ename)); }
        } 
      }  
    } 
    try { pwout.close(); } catch (Exception ee) { }
  }


  public void nospacelexicalanalysisxml(String str)
  { int in = INUNKNOWN; 
    char previous = ' '; 

    int explen = str.length();
    lexicals = new Vector(explen);  /* Sequence of lexicals */ 
    StringBuffer sb = null;    /* Holds current lexical item */ 

    for (int i = 0; i < explen; i++)
    { char c = str.charAt(i); 
      if (in == INUNKNOWN) 
      { if (isXMLSymbolCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (isXMLBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the expression
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }           
        else if (c == '"')
        { sb = new StringBuffer();     // start new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append(c); 
        }
        else if (c == ' ' || c == '\n' || c == '\t')
        { } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          sb.append(c); 
        }
      } 
      else if (in == INBASICEXP)
      { if (isXMLBasicExpCharacter(c) || c == '"')
        { sb.append(c); }              // carry on adding to current basic exp
        else if (isXMLSymbolCharacter(c))
        { sb = new StringBuffer();     // start new buffer for the symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          sb.append(c); 
          previous = c; 
        }
        else if (c == ' ' || c == '\n' || c == '\t')
        { in = INUNKNOWN; } 
        else
        { sb = new StringBuffer();     // unrecognised lexical
          lexicals.addElement(sb);  
          in = INUNKNOWN; 
          sb.append(c); 
        }
      }
      else if (in == INSYMBOL)
      { if (c == '"')
        { sb = new StringBuffer();     // start new buffer for the string
          lexicals.addElement(sb);  
          in = INSTRING; 
          sb.append(c); 
        }
        else if (c == '(' || c == ')')
        { sb = new StringBuffer();     // start new buffer for the new symbol
          lexicals.addElement(sb);  
          in = INSYMBOL; 
          previous = c; 
          sb.append(c); 
        }
        else if (isXMLBasicExpCharacter(c))
        { sb = new StringBuffer();     // start new buffer for basic exp
          lexicals.addElement(sb);  
          in = INBASICEXP; 
          sb.append(c); 
        }
        else if (isXMLSymbolCharacter(c))
        { if (validFollowingCharacter(previous,c))
          { sb.append(c); } 
          else 
          { sb = new StringBuffer();     // start new buffer for the new symbol
            lexicals.addElement(sb);  
            in = INSYMBOL; 
            sb.append(c); 
          }
          previous = c;
        } 
        else if (c == ' ' || c == '\n' || c == '\t')
        { in = INUNKNOWN; } 
      }
      else if (in == INSTRING)
      { if (c == '"')  /* end of string */
        { sb.append(c);
          in = INUNKNOWN; 
        } 
        else 
        { sb.append(c); }
      }    
      previous = c;
    }
  }

  private boolean validFollowingCharacter(char c1, char c2)
  { if (c1 == '<') 
    { if (c2 == '=' || c2 == ':' || c2 == '/') { return true; }
      return false; 
    }
    if (c1 == '>')
    { if (c2 == '=') { return true; }
      return false; 
    }
    if (c1 == '/') 
    { if (c2 == '=' || c2 == ':' || c2 == '\\' || c2 == '>') { return true; }
      return false; 
    }
    if (c1 == '\\')
    { if (c2 == '/') { return true; } 
      return false; 
    }
    if (c1 == '=')
    { if (c2 == '>') { return true; } 
      return false; 
    }
    if (c1 == '-')
    { if (c2 == '>') { return true; }
      return false; 
    }
    if (c1 == ':')
    { if (c2 == '=') { return true; }
      return false; 
    }
    return false;
  } 


  public XMLNode parseXML()
  { for (int i = 0; i < lexicals.size(); i++) 
    { if ("?".equals(lexicals.get(i) + "") &&
          ">".equals(lexicals.get(i+1) + ""))
      { XMLNode root = new XMLNode();
        return parsexmlnode(root,i+2,lexicals.size() - 1); 
      }
    } 
    return null;
  } 

  public XMLNode parsexmlnode(XMLNode parent, int st, int en)
  { if ("<".equals(lexicals.get(st) + ""))
    { String tag = lexicals.get(st + 1) + "";

      XMLNode xnode = new XMLNode();
      xnode.settag(tag); 
      xmlnodes.add(xnode); 

      for (int i = st + 2; i <= en; i++)
      { String str = lexicals.get(i) + "";

        // System.out.println(">> Lexical " + str + " of " + tag); 

        if (i + 1 <= en)
        { if ("=".equals(lexicals.get(i+1) + ""))
          { XMLAttribute xatt = new XMLAttribute(); 
            xatt.setname(str);
            xatt.setvalue(lexicals.get(i+2) + "");
            xnode.addattributes(xatt);
            xmlattributes.add(xatt); 
          }
          else if ("</".equals(str) && i + 2 <= en &&
              tag.equals(lexicals.get(i+1) + "") &&
              ">".equals(lexicals.get(i+2) + ""))
          { return xnode; }
        }

        if (i + 4 <= en &&  
          ">".equals(str) &&
          "</".equals(lexicals.get(i+2) + "") &&
          ">".equals(lexicals.get(i+4) + ""))
        { if ((lexicals.get(i-1) + "").equals(
                            lexicals.get(i+3) + ""))
          { System.out.println("+++ " + parent + "." +  
                             lexicals.get(i+3) + "=" +  
                             lexicals.get(i+1) + ""); 
            XMLAttribute xatt = new XMLAttribute(); 
            xatt.setname(lexicals.get(i+3) + "");
            xatt.setvalue(lexicals.get(i+1) + "");
            parent.addattributes(xatt);
            xmlattributes.add(xatt);
          }
          else 
          { System.out.println("+++ " + xnode + ".value" +  
                             " =" +  
                             lexicals.get(i+1) + ""); 
            XMLAttribute xattV = new XMLAttribute(); 
            xattV.setname("value");
            xattV.setvalue(lexicals.get(i+1) + "");
            xnode.addattributes(xattV); 
            xmlattributes.add(xattV);
            return xnode; 
          }

          i = i + 6; 
          str = lexicals.get(i) + "";
        } 
        else if (">".equals(str))
        { Vector sns = parsesubnodes(xnode, tag, i+1, en);
          xnode.setsubnodes(sns);
          return xnode;
        }
        
        if ("/>".equals(str))
        { return xnode; }
      }
    }
    return null;
  }

  public Vector parsesubnodes(XMLNode xnode, String tag, int st, int en)
  { Vector res = new Vector();

   /* System.out.println(">>> Subnodes from " + st + " " + en); 
    for (int i = st; i <= en; i++)
    { String str = lexicals.get(i) + "";
      System.out.println(str); }
    System.out.println(">>>>>>>>>>>>"); 

    if (st + 3 == en && tag.equals(lexicals.get(en-1) + "") 
        && "</".equals(lexicals.get(en-2) + "") && 
        ">".equals(lexicals.get(en) + ""))
    { System.out.println(tag + "=" + lexicals.get(st) + ""); 
      XMLAttribute xatt = new XMLAttribute(); 
      xatt.setname(tag);
      xatt.setvalue(lexicals.get(st) + "");
      xnode.addattributes(xatt);
      xmlattributes.add(xatt);
      return res; 
    } */ 
 
    for (int i = st; i <= en; i++)
    { String str = lexicals.get(i) + "";
      if ("</".equals(str) && i + 2 <= en &&
           tag.equals(lexicals.get(i+1) + "") &&
           ">".equals(lexicals.get(i+2) + ""))
      { return res; }

  /* 
      if (i + 3 <= en &&  
          "</".equals(lexicals.get(i+1) + "") && 
          ">".equals(lexicals.get(i+3) + ""))
      { System.out.println(lexicals.get(i+2) + "=" + lexicals.get(st) + ""); 
        XMLAttribute xatt = new XMLAttribute(); 
        xatt.setname(lexicals.get(i+2) + "");
        xatt.setvalue(lexicals.get(i) + "");
        xnode.addattributes(xatt);
        xmlattributes.add(xatt);
        i = i + 3; 
      } */ 

      if (">".equals(str)) 
      { String tag1 = lexicals.get(st + 1) + ""; 
        for (int j = i+1; j < en; j++) 
        { String str1 = lexicals.get(j) + ""; 
          if ("</".equals(str1) && tag1.equals(lexicals.get(j+1) + ""))
          { XMLNode xn = parsexmlnode(xnode,st,j+2); 
            if (xn != null) { res.add(xn); } 
            res.addAll(parsesubnodes(xnode,tag,j+3,en));
            return res;
          } 
        }
      }

      if ("</".equals(str) && i + 2 <= en &&
           !(tag.equals(lexicals.get(i+1) + "")) &&
           ">".equals(lexicals.get(i+2) + ""))
      { XMLNode xn = parsexmlnode(xnode,st,i+2);
        if (xn != null) { res.add(xn); }
        res.addAll(parsesubnodes(xnode,tag,i+3,en));
        return res;
      }

      if ("/>".equals(str))
      { XMLNode xn = parsexmlnode(xnode,st,i);

        if (xn != null) 
        { res.add(xn); }
        res.addAll(parsesubnodes(xnode,tag,i+1,en));
        return res;
      }
    }
    return res;
  }

  public static XMLNode parseXML(String f)
  { BufferedReader br = null;
    String s;
    boolean eof = false;
    File file = new File(f);

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException e)
    { System.out.println("File not found: " + file);
      return null;
    }
    String xmlstring = ""; 

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("Reading file failed.");
        return null;
      }
      if (s == null) 
      { eof = true;
        break;
      }
      else 
      { xmlstring = xmlstring + s + " "; }
    }

    xmlnodes.clear(); 
    xmlattributes.clear();

    XMLParser comp = new XMLParser();  
    comp.nospacelexicalanalysisxml(xmlstring);
    XMLNode xml = comp.parseXML();

    try { br.close(); } catch(IOException e) { return null; }
    return xml;
  }

  public static List allXMLNodes()
  { return xmlnodes; }

  public static List allXMLAttributes()
  { return xmlattributes; }

  public static void convertXsi()
  { BufferedReader br = null;
    
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File infile = new File("./xsi.txt");  /* default */
    try
    { br = new BufferedReader(new FileReader(infile)); }
    catch (Exception e)
    { System.out.println("Errors with files: " + infile);
      return;
    }
    String xmlstring = "";

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("Reading failed.");
        return;
      }
      if (s == null) 
      { eof = true;
        break;
      }
      else 
      { xmlstring = xmlstring + s + " "; }
    }

    XMLParser.convertXsiToData(xmlstring);
  }

  public static String saveXSI(Vector v1, Vector v2)
  { StringBuffer out = new StringBuffer(); 
    return out.toString();
  }

}
