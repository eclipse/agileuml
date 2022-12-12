import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import java.lang.*;
import java.lang.reflect.*;
import java.util.StringTokenizer;
import java.io.*;



class Elector
  implements SystemTypes
{
  private String surname = ""; // internal
  private String forename = ""; // internal
  private String postcode = ""; // internal
  private String address1 = ""; // internal
  private String address2 = ""; // internal
  private String address3 = ""; // internal

  public Elector()
  {
    this.surname = "";
    this.forename = "";
    this.postcode = "";
    this.address1 = "";
    this.address2 = "";
    this.address3 = "";

  }



  public String toString()
  { String _res_ = "(Elector) ";
    _res_ = _res_ + surname + ",";
    _res_ = _res_ + forename + ",";
    _res_ = _res_ + postcode + ",";
    _res_ = _res_ + address1 + ",";
    _res_ = _res_ + address2 + ",";
    _res_ = _res_ + address3;
    return _res_;
  }

static Vector tokeniseCSV(String line)
{ StringBuffer buff = new StringBuffer();
  int x = 0;
  int len = line.length();
  boolean instring = false;
  Vector res = new Vector();
  while (x < len)
  { char chr = line.charAt(x);
    x++;
    if (chr == ',')
    { if (instring) { buff.append(chr); }
      else
      { res.add(buff.toString().trim());
        buff = new StringBuffer();
      }
    }
    else if ('"' == chr)
    { if (instring) { instring = false; }
      else { instring = true; }
    }
    else
    { buff.append(chr); }
  }
  res.add(buff.toString().trim()); 
  return res;
}

  public static Elector parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = tokeniseCSV(_line);

    Elector electorx = new Elector();
    electorx.surname = (String) _line1vals.get(0);
    electorx.forename = (String) _line1vals.get(1);
    electorx.postcode = (String) _line1vals.get(2);
    electorx.address1 = (String) _line1vals.get(3);
    electorx.address2 = (String) _line1vals.get(4);
    electorx.address3 = (String) _line1vals.get(5);
    return electorx;
  }


  public void writeCSV(PrintWriter _out)
  { Elector electorx = this;
    _out.print("" + electorx.surname);
    _out.print(" , ");
    _out.print("" + electorx.forename);
    _out.print(" , ");
    _out.print("" + electorx.postcode);
    _out.print(" , ");
    _out.print("" + electorx.address1);
    _out.print(" , ");
    _out.print("" + electorx.address2);
    _out.print(" , ");
    _out.print("" + electorx.address3);
    _out.println();
  }


  public void setsurname(String surname_x) { surname = surname_x;  }


    public static void setAllsurname(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setsurname(electorx,val); } }


  public void setforename(String forename_x) { forename = forename_x;  }


    public static void setAllforename(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setforename(electorx,val); } }


  public void setpostcode(String postcode_x) { postcode = postcode_x;  }


    public static void setAllpostcode(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setpostcode(electorx,val); } }


  public void setaddress1(String address1_x) { address1 = address1_x;  }


    public static void setAlladdress1(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setaddress1(electorx,val); } }


  public void setaddress2(String address2_x) { address2 = address2_x;  }


    public static void setAlladdress2(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setaddress2(electorx,val); } }


  public void setaddress3(String address3_x) { address3 = address3_x;  }


    public static void setAlladdress3(List electors,String val)
  { for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      Controller.inst().setaddress3(electorx,val); } }


    public String getsurname() { return surname; }

    public static List getAllsurname(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getsurname())) { }
      else { result.add(electorx.getsurname()); } }
    return result; }

    public static List getAllOrderedsurname(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getsurname()); } 
    return result; }

    public String getforename() { return forename; }

    public static List getAllforename(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getforename())) { }
      else { result.add(electorx.getforename()); } }
    return result; }

    public static List getAllOrderedforename(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getforename()); } 
    return result; }

    public String getpostcode() { return postcode; }

    public static List getAllpostcode(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getpostcode())) { }
      else { result.add(electorx.getpostcode()); } }
    return result; }

    public static List getAllOrderedpostcode(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getpostcode()); } 
    return result; }

    public String getaddress1() { return address1; }

    public static List getAlladdress1(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getaddress1())) { }
      else { result.add(electorx.getaddress1()); } }
    return result; }

    public static List getAllOrderedaddress1(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getaddress1()); } 
    return result; }

    public String getaddress2() { return address2; }

    public static List getAlladdress2(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getaddress2())) { }
      else { result.add(electorx.getaddress2()); } }
    return result; }

    public static List getAllOrderedaddress2(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getaddress2()); } 
    return result; }

    public String getaddress3() { return address3; }

    public static List getAlladdress3(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      if (result.contains(electorx.getaddress3())) { }
      else { result.add(electorx.getaddress3()); } }
    return result; }

    public static List getAllOrderedaddress3(List electors)
  { List result = new Vector();
    for (int i = 0; i < electors.size(); i++)
    { Elector electorx = (Elector) electors.get(i);
      result.add(electorx.getaddress3()); } 
    return result; }


}



public class Controller implements SystemTypes, ControllerInterface
{
  Vector electors = new Vector();
  private static Controller uniqueInstance; 


  private Controller() { } 


  public static Controller inst() 
    { if (uniqueInstance == null) 
    { uniqueInstance = new Controller(); }
    return uniqueInstance; } 


  public static void loadModel(String file)
  {
    try
    { BufferedReader br = null;
      File f = new File(file);
      try 
      { br = new BufferedReader(new FileReader(f)); }
      catch (Exception ex) 
      { System.err.println("No file: " + file); return; }
      Class cont = Class.forName("Controller");
      java.util.Map objectmap = new java.util.HashMap();
      while (true)
      { String line1;
        try { line1 = br.readLine(); }
        catch (Exception e)
        { return; }
        if (line1 == null)
        { return; }
        line1 = line1.trim();

        if (line1.length() == 0) { continue; }
        if (line1.startsWith("//")) { continue; }
        String left;
        String op;
        String right;
        if (line1.charAt(line1.length() - 1) == '"')
        { int eqind = line1.indexOf("="); 
          if (eqind == -1) { continue; }
          else 
          { left = line1.substring(0,eqind-1).trim();
            op = "="; 
            right = line1.substring(eqind+1,line1.length()).trim();
          }
        }
        else
        { StringTokenizer st1 = new StringTokenizer(line1);
          Vector vals1 = new Vector();
          while (st1.hasMoreTokens())
          { String val1 = st1.nextToken();
            vals1.add(val1);
          }
          if (vals1.size() < 3)
          { continue; }
          left = (String) vals1.get(0);
          op = (String) vals1.get(1);
          right = (String) vals1.get(2);
        }
        if (":".equals(op))
        { int i2 = right.indexOf(".");
          if (i2 == -1)
          { Class cl;
            try { cl = Class.forName("" + right); }
            catch (Exception _x) { System.err.println("No entity: " + right); continue; }
            Object xinst = cl.newInstance();
            objectmap.put(left,xinst);
            Class[] cargs = new Class[] { cl };
            Method addC = null;
            try { addC = cont.getMethod("add" + right,cargs); }
            catch (Exception _xx) { System.err.println("No entity: " + right); continue; }
            if (addC == null) { continue; }
            Object[] args = new Object[] { xinst };
            addC.invoke(Controller.inst(),args);
          }
          else
          { String obj = right.substring(0,i2);
            String role = right.substring(i2+1,right.length());
            Object objinst = objectmap.get(obj); 
            if (objinst == null) 
            { continue; }
            Object val = objectmap.get(left);
            if (val == null) 
            { continue; }
            Class objC = objinst.getClass();
            Class typeclass = val.getClass(); 
            Object[] args = new Object[] { val }; 
            Class[] settypes = new Class[] { typeclass };
            Method addrole = Controller.findMethod(objC,"add" + role);
            if (addrole != null) 
            { addrole.invoke(objinst, args); }
            else { System.err.println("Error: cannot add to " + role); }
          }
        }
        else if ("=".equals(op))
        { int i1 = left.indexOf(".");
          if (i1 == -1) 
          { continue; }
          String obj = left.substring(0,i1);
          String att = left.substring(i1+1,left.length());
          Object objinst = objectmap.get(obj); 
          if (objinst == null) 
          { System.err.println("No object: " + obj); continue; }
          Class objC = objinst.getClass();
          Class typeclass; 
          Object val; 
          if (right.charAt(0) == '"' &&
              right.charAt(right.length() - 1) == '"')
          { typeclass = String.class;
            val = right.substring(1,right.length() - 1);
          } 
          else if ("true".equals(right) || "false".equals(right))
          { typeclass = boolean.class;
            if ("true".equals(right))
            { val = new Boolean(true); }
            else
            { val = new Boolean(false); }
          }
          else 
          { val = objectmap.get(right);
            if (val != null)
            { typeclass = val.getClass(); }
            else 
            { int i;
              long l; 
              double d;
              try 
              { i = Integer.parseInt(right);
                typeclass = int.class;
                val = new Integer(i); 
              }
              catch (Exception ee)
              { try 
                { l = Long.parseLong(right);
                  typeclass = long.class;
                  val = new Long(l); 
                }
                catch (Exception eee)
                { try
                  { d = Double.parseDouble(right);
                    typeclass = double.class;
                    val = new Double(d);
                  }
                  catch (Exception ff)
                  { continue; }
                }
              }
            }
          }
          Object[] args = new Object[] { val }; 
          Class[] settypes = new Class[] { typeclass };
          Method setatt = Controller.findMethod(objC,"set" + att);
          if (setatt != null) 
          { setatt.invoke(objinst, args); }
          else { System.err.println("No attribute: " + objC.getName() + "::" + att); }
        }
      }
    } catch (Exception e) { }
  }

  public static Method findMethod(Class c, String name)
  { Method[] mets = c.getMethods(); 
    for (int i = 0; i < mets.length; i++)
    { Method m = mets[i];
      if (m.getName().equals(name))
      { return m; }
    } 
    return null;
  }


  public static void loadCSVModel()
  { boolean __eof = false;
    String __s = "";
    Controller __cont = Controller.inst();
    BufferedReader __br = null;
    try
    { File _elector = new File("Elector.csv");
      __br = new BufferedReader(new FileReader(_elector));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { Elector electorx = Elector.parseCSV(__s.trim());
          if (electorx != null)
          { __cont.addElector(electorx); }
        }
      }
    }
    catch(Exception __e) { }
  }


  public void checkCompleteness()
  {   }


  public void saveModel(String file)
  { File outfile = new File(file); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
  for (int _i = 0; _i < electors.size(); _i++)
  { Elector electorx_ = (Elector) electors.get(_i);
    out.println("electorx_" + _i + " : Elector");
    out.println("electorx_" + _i + ".surname = \"" + electorx_.getsurname() + "\"");
    out.println("electorx_" + _i + ".forename = \"" + electorx_.getforename() + "\"");
    out.println("electorx_" + _i + ".postcode = \"" + electorx_.getpostcode() + "\"");
    out.println("electorx_" + _i + ".address1 = \"" + electorx_.getaddress1() + "\"");
    out.println("electorx_" + _i + ".address2 = \"" + electorx_.getaddress2() + "\"");
    out.println("electorx_" + _i + ".address3 = \"" + electorx_.getaddress3() + "\"");
  }

    out.close(); 
  }


  public static void loadXSI()
  { boolean __eof = false;
    String __s = "";
    String xmlstring = "";
    BufferedReader __br = null;
    try
    { File _classmodel = new File("in.xmi");
      __br = new BufferedReader(new FileReader(_classmodel));
      __eof = false;
      while (!__eof)
      { try { __s = __br.readLine(); }
        catch (IOException __e)
        { System.out.println("Reading failed.");
          return;
        }
        if (__s == null)
        { __eof = true; }
        else
        { xmlstring = xmlstring + __s; }
      } 
      __br.close();
    } 
    catch (Exception _x) { }
    Vector res = convertXsiToVector(xmlstring);
    File outfile = new File("_in.txt");
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
    for (int i = 0; i < res.size(); i++)
    { String r = (String) res.get(i); 
      out.println(r);
    } 
    out.close();
    loadModel("_in.txt");
  }

  public static Vector convertXsiToVector(String xmlstring)
  { Vector res = new Vector();
    XMLParser comp = new XMLParser();
    comp.nospacelexicalanalysisxml(xmlstring);
    XMLNode xml = comp.parseXML();
    if (xml == null) { return res; } 
    java.util.Map instancemap = new java.util.HashMap(); // String --> Vector
    java.util.Map entmap = new java.util.HashMap();       // String --> String
    Vector entcodes = new Vector(); 
    java.util.Map allattsmap = new java.util.HashMap(); // String --> Vector
    java.util.Map stringattsmap = new java.util.HashMap(); // String --> Vector
    java.util.Map onerolesmap = new java.util.HashMap(); // String --> Vector
    java.util.Map actualtype = new java.util.HashMap(); // XMLNode --> String
    Vector eallatts = new Vector();
    instancemap.put("electors", new Vector()); 
    instancemap.put("elector",new Vector()); 
    entcodes.add("electors");
    entcodes.add("elector");
    entmap.put("electors","Elector");
    entmap.put("elector","Elector");
    eallatts = new Vector();
    eallatts.add("surname");
    eallatts.add("forename");
    eallatts.add("postcode");
    eallatts.add("address1");
    eallatts.add("address2");
    eallatts.add("address3");
    allattsmap.put("Elector", eallatts);
    eallatts = new Vector();
    eallatts.add("surname");
    eallatts.add("forename");
    eallatts.add("postcode");
    eallatts.add("address1");
    eallatts.add("address2");
    eallatts.add("address3");
    stringattsmap.put("Elector", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Elector", eallatts);
    eallatts = new Vector();
  Vector enodes = xml.getSubnodes();
  for (int i = 0; i < enodes.size(); i++)
  { XMLNode enode = (XMLNode) enodes.get(i);
    String cname = enode.getTag();
    Vector einstances = (Vector) instancemap.get(cname); 
    if (einstances == null) 
    { einstances = (Vector) instancemap.get(cname + "s"); }
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
      res.add(ename + k + " : " + tname);
      actualtype.put(enode,tname);
    }   
  }
  for (int j = 0; j < entcodes.size(); j++) 
  { String ename = (String) entcodes.get(j); 
    Vector elems = (Vector) instancemap.get(ename); 
    for (int k = 0; k < elems.size(); k++)
    { XMLNode enode = (XMLNode) elems.get(k);
      String tname = (String) actualtype.get(enode);
      Vector tallatts = (Vector)  allattsmap.get(tname);
      Vector tstringatts = (Vector)  stringattsmap.get(tname);
      Vector toneroles = (Vector)  onerolesmap.get(tname);
      Vector atts = enode.getAttributes();
      for (int p = 0; p < atts.size(); p++) 
      { XMLAttribute patt = (XMLAttribute) atts.get(p); 
        if (patt.getName().equals("xsi:type") || patt.getName().equals("xmi:id")) {} 
        else 
        { patt.getDataDeclarationFromXsi(res,tallatts,tstringatts,toneroles,ename + k, (String) entmap.get(ename)); } 
      }
    } 
  }  
  return res; } 

  public void saveXSI(String file)
  { File outfile = new File(file); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("<UMLRSDS:model xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\">");
    for (int _i = 0; _i < electors.size(); _i++)
    { Elector electorx_ = (Elector) electors.get(_i);
       out.print("<electors xsi:type=\"My:Elector\"");
    out.print(" surname=\"" + electorx_.getsurname() + "\" ");
    out.print(" forename=\"" + electorx_.getforename() + "\" ");
    out.print(" postcode=\"" + electorx_.getpostcode() + "\" ");
    out.print(" address1=\"" + electorx_.getaddress1() + "\" ");
    out.print(" address2=\"" + electorx_.getaddress2() + "\" ");
    out.print(" address3=\"" + electorx_.getaddress3() + "\" ");
    out.println(" />");
  }

    out.println("</UMLRSDS:model>");
    out.close(); 
  }


  public void saveCSVModel()
  { try {
      File _elector = new File("Elector.csv");
      PrintWriter _out_elector = new PrintWriter(new BufferedWriter(new FileWriter(_elector)));
      for (int __i = 0; __i < electors.size(); __i++)
      { Elector electorx = (Elector) electors.get(__i);
        electorx.writeCSV(_out_elector);
      }
      _out_elector.close();
    }
    catch(Exception __e) { }
  }



  public void addElector(Elector oo) { electors.add(oo); }



  public Elector createElector()
  { Elector electorx = new Elector();
    addElector(electorx);
    return electorx;
  }


public void setsurname(Elector electorx, String surname_x) 
  { electorx.setsurname(surname_x);
    }


public void setforename(Elector electorx, String forename_x) 
  { electorx.setforename(forename_x);
    }


public void setpostcode(Elector electorx, String postcode_x) 
  { electorx.setpostcode(postcode_x);
    }


public void setaddress1(Elector electorx, String address1_x) 
  { electorx.setaddress1(address1_x);
    }


public void setaddress2(Elector electorx, String address2_x) 
  { electorx.setaddress2(address2_x);
    }


public void setaddress3(Elector electorx, String address3_x) 
  { electorx.setaddress3(address3_x);
    }





  public void killAllElector(List electorxx)
  { for (int _i = 0; _i < electorxx.size(); _i++)
    { killElector((Elector) electorxx.get(_i)); }
  }

  public void killElector(Elector electorxx)
  { if (electorxx == null) { return; }
   electors.remove(electorxx);
  }



public static void main(String[] args)
{ Controller c = Controller.inst(); 
  c.loadCSVModel();
  File outfile = new File("print.txt"); 
    PrintWriter out; 
    try { out = new PrintWriter(new BufferedWriter(new FileWriter(outfile))); }
    catch (Exception e) { return; } 
  
   out.println(); 
   out.println(); 

  int esize = c.electors.size();
  for (int i = 0; i < esize-3; i = i+3)
  { Elector e1 = (Elector) c.electors.get(i); 
    Elector e2 = (Elector) c.electors.get(i+1);
    Elector e3 = (Elector) c.electors.get(i+2); 
    out.printf("   %25s        %25s        %25s\n", e1.getforename() + " " + e1.getsurname(), e2.getforename() + " " + e2.getsurname(), e3.getforename() + " " + e3.getsurname()); 
    out.printf("   %25s        %25s        %25s\n", e1.getaddress1(), e2.getaddress1(), e3.getaddress1()); 
    out.printf("   %25s        %25s        %25s\n", e1.getaddress2(), e2.getaddress2(), e3.getaddress2());
    out.printf("   %25s        %25s        %25s\n", e1.getpostcode(), e2.getpostcode(), e3.getpostcode());
    out.println(); 
    out.println(); 
    out.println(); 
    out.println(); 
    out.println(); 
    out.println(); 
    out.println(); 
  } 
  out.close();  
 
  
} 

   
}



