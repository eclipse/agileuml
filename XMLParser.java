import java.util.Vector; 
import java.util.List; 
import java.io.*;

/* Package: Utilities */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

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

  /* public static void convertXsiToData(String xmlstring)
  { BufferedWriter brout = null; 
    PrintWriter pwout = null; 
    File outfile = new File("./in.txt"); 

    try
    { brout = new BufferedWriter(new FileWriter(outfile)); 
      pwout = new PrintWriter(brout); 
    }
    catch (Exception e)
    { System.out.println("Errors with file: " + outfile);
      return; 
    }

    Vector res = new Vector();

    // Compiler2 comp = new Compiler2();  
    XMLParser comp = new XMLParser(); 
    comp.nospacelexicalanalysisxml(xmlstring); 
    XMLNode xml = comp.parseXML(); 
    // System.out.println(xml); 

    java.util.Map instancemap = new java.util.HashMap(); // String --> Vector 
    java.util.Map entmap = new java.util.HashMap();       // String --> String
    Vector entcodes = new Vector(); 

    instancemap.put("ball",new Vector()); 
    instancemap.put("abstractfields", new Vector()); 
    instancemap.put("fields",new Vector()); 
    instancemap.put("players",new Vector()); 
    instancemap.put("goalfields",new Vector()); 
    instancemap.put("goalkeepers",new Vector()); 
    instancemap.put("fieldplayers",new Vector()); 
    entcodes.add("ball"); entcodes.add("abstractfields"); 
    entcodes.add("fields"); entcodes.add("players"); 
    entcodes.add("goalfields"); entcodes.add("goalkeepers"); 
    entcodes.add("fieldplayers"); 
    entmap.put("ball","Ball"); 
    entmap.put("abstractfields","AbstractField"); 
    entmap.put("fields", "Field"); 
    entmap.put("players", "Player"); 
    entmap.put("goalfields", "GoalField"); 
    entmap.put("goalkeepers", "GoalKeeper"); 
    entmap.put("fieldplayers", "FieldPlayer"); 


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

        // String idval = enode.getAttributeValue("xmi:id");
        // if (idval != null) 
        // { idmap.put(idval,ename + "" + k); }  
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
  }  */ 


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
      { return parsexmlnode(i+2,lexicals.size() - 1); } 
    } 
    return null; 
  } 

  public XMLNode parsexmlnode(int st, int en)
  { if ("<".equals(lexicals.get(st) + ""))
    { String tag = lexicals.get(st + 1) + "";

      System.out.println("Parsing " + tag); 

      XMLNode xnode = new XMLNode();
      xnode.settag(tag); 
      xmlnodes.add(xnode); 

      for (int i = st + 2; i <= en; i++)
      { String str = lexicals.get(i) + "";


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

/*  public static void convertXsi()
  { // parseXML("test.xml"); 
    BufferedReader br = null;
    
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File infile = new File("./xsi.txt");   
    
    try
    { br = new BufferedReader(new FileReader(infile));
    }
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
  } */ 

/*   public static String saveXSI(Vector moveplayers, Vector shootballs)
  { StringBuffer out = new StringBuffer(); 
    out.append("<?xml version=\"1.0\" encoding=\"ASCII\"?>");
    out.append("<updatemodel:Update xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\">");
    
    for (int _i = 0; _i < moveplayers.size(); _i++)
    { MovePlayer moveplayerx_ = (MovePlayer) moveplayers.get(_i);
      out.append("<actions xsi:type=\"updatemodel:MovePlayer\"");
      out.append(" playerNumber=\"" + moveplayerx_.getplayerNumber() + "\" ");
      out.append(" xDist=\"" + moveplayerx_.getxDist() + "\" ");
      out.append(" yDist=\"" + moveplayerx_.getyDist() + "\" ");
      out.append(" />");
    }

    for (int _i = 0; _i < shootballs.size(); _i++)
    { ShootBall shootballx_ = (ShootBall) shootballs.get(_i);
      out.append("<actions xsi:type=\"updatemodel:ShootBall\"");
      out.append(" playerNumber=\"" + shootballx_.getplayerNumber() + "\" ");
      out.append(" xDist=\"" + shootballx_.getxDist() + "\" ");
      out.append(" yDist=\"" + shootballx_.getyDist() + "\" ");
      out.append(" />");
    }

    out.append("</updatemodel:Update>");
    return out.toString(); 
  }  */ 


}