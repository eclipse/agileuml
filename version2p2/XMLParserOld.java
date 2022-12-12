import java.util.Vector; 
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
  { return (c == '<' || c == '>' || c == '=' || c == '*' || c == '/' || c == '\\' ||
            c == '-' || c == ',' || c == '\'' || c == '?' ||
            c == '+' || c == '&' || c == '^' || c == '|' ||
            c == '(' || c == ')' || c == '{' || c == '}' || c == '[' ||
            c == ']' || c == '#'); 
  } 
  // { return (c == '<' || c == '>' || c == '='); } 

  private boolean isXMLBasicExpCharacter(char c)
  { return (Character.isLetterOrDigit(c) || c == '.' || c == '$' || c == '@' || c == ':'); } 

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
        else if (c == ' ' || c == '\n') 
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
        else if (c == ' ' || c == '\n')
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
        else if (c == ' ' || c == '\n')
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
      { return parsexmlnode(i+2,lexicals.size() - 1); } 
    } 
    return null; 
  } 

  public XMLNode parsexmlnode(int st, int en)
  { if ("<".equals(lexicals.get(st) + ""))
    { String tag = lexicals.get(st + 1) + "";

      System.out.println("Parsing " + tag); 

      XMLNode xnode = new XMLNode(tag);
      xmlnodes.add(xnode); 

      for (int i = st + 2; i <= en; i++)
      { String str = lexicals.get(i) + "";


        if (i + 1 <= en)
        { if ("=".equals(lexicals.get(i+1) + ""))
          { XMLAttribute xatt = new XMLAttribute(str, lexicals.get(i+2) + "");
            xnode.addAttribute(xatt);
            xmlattributes.add(xatt); 
          }
          else if ("</".equals(str) && i + 2 <= en && 
              tag.equals(lexicals.get(i+1) + "") &&
              ">".equals(lexicals.get(i+2) + ""))
          { return xnode; }
        }

        if (">".equals(str))
        { Vector sns = parsesubnodes(tag, i+1, en);
          xnode.setsubnodes(sns);
          return xnode;
        }

        if ("/>".equals(str))
        { return xnode; }
      }
    }
    return null;
  }


  public Vector parsesubnodes(String tag, int st, int en)
  { Vector res = new Vector();
    System.out.println("Parsing subnodes of " + tag); 

    for (int i = st; i <= en; i++)
    { String str = lexicals.get(i) + "";
      if ("</".equals(str) && i + 2 <= en &&
           tag.equals(lexicals.get(i+1) + "") &&
           ">".equals(lexicals.get(i+2) + ""))
      { return res; }

      if (">".equals(str)) 
      { String tag1 = lexicals.get(st + 1) + ""; 
        for (int j = i+1; j < en; j++) 
        { String str1 = lexicals.get(j) + ""; 
          if ("</".equals(str1) && tag1.equals(lexicals.get(j+1) + ""))
          { XMLNode xn = parsexmlnode(st,j+2); 
            // xmlnodes.add(xn); 
            System.out.println("Parsed inner node: " + xn);
            if (xn != null) { res.add(xn); } 
            res.addAll(parsesubnodes(tag,j+3,en));
            return res;
          } 
        }
      }  

      if ("</".equals(str) && i + 2 <= en &&
           !(tag.equals(lexicals.get(i+1) + "")) &&
           ">".equals(lexicals.get(i+2) + ""))
      { XMLNode xn = parsexmlnode(st,i+2);
        // xmlnodes.add(xn); 
        System.out.println("Parsed: " + xn); 

        if (xn != null) { res.add(xn); }
        res.addAll(parsesubnodes(tag,i+3,en));
        return res;
      }

      if ("/>".equals(str))
      { XMLNode xn = parsexmlnode(st,i);

        System.out.println("Parsed subnode: " + xn); 

        if (xn != null) { res.add(xn); }
        res.addAll(parsesubnodes(tag,i+1,en));
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
    System.out.println(xml); 

    try { br.close(); } catch(IOException e) { return null; }
    return xml; 
  } 

  public static List allXMLNodes()
  { return xmlnodes; } 

  public static List allXMLAttributes()
  { return xmlattributes; } 
}