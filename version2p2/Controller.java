import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import java.lang.*;
import java.lang.reflect.*;
import java.util.StringTokenizer;
import java.io.*;



class MathLib
  implements SystemTypes
{
  private static int ix = 0; // internal
  private static int iy = 0; // internal
  private static int iz = 0; // internal

  public MathLib()
  {
    this.ix = 0;
    this.iy = 0;
    this.iz = 0;

  }



  public String toString()
  { String _res_ = "(MathLib) ";
    _res_ = _res_ + ix + ",";
    _res_ = _res_ + iy + ",";
    _res_ = _res_ + iz;
    return _res_;
  }

  public static MathLib parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = new Vector();
    StringTokenizer _st1 =
      new StringTokenizer(_line, ",");
    while (_st1.hasMoreTokens())
    { String _str = _st1.nextToken();
      if (_str != null) 
      { _line1vals.add(_str.trim()); }
    }
    MathLib mathlibx = new MathLib();
    mathlibx.ix = Integer.parseInt((String) _line1vals.get(0));
    mathlibx.iy = Integer.parseInt((String) _line1vals.get(1));
    mathlibx.iz = Integer.parseInt((String) _line1vals.get(2));
    return mathlibx;
  }


  public void writeCSV(PrintWriter _out)
  { MathLib mathlibx = this;
    _out.print("" + mathlibx.ix);
    _out.print(" , ");
    _out.print("" + mathlibx.iy);
    _out.print(" , ");
    _out.print("" + mathlibx.iz);
    _out.println();
  }


  public static void setix(int ix_x) { ix = ix_x; }

public void localSetix(int ix_x) {   }


    public static void setAllix(List mathlibs,int val)
  { for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      Controller.inst().setix(mathlibx,val); } }


  public static void setiy(int iy_x) { iy = iy_x; }

public void localSetiy(int iy_x) {   }


    public static void setAlliy(List mathlibs,int val)
  { for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      Controller.inst().setiy(mathlibx,val); } }


  public static void setiz(int iz_x) { iz = iz_x; }

public void localSetiz(int iz_x) {   }


    public static void setAlliz(List mathlibs,int val)
  { for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      Controller.inst().setiz(mathlibx,val); } }


    public static int getix() { return ix; }

    public static List getAllix(List mathlibs)
  { List result = new Vector();
   if (mathlibs.size() > 0)
   { result.add(new Integer(MathLib.ix)); }
    return result; }

    public static List getAllOrderedix(List mathlibs)
  { List result = new Vector();
    for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      result.add(new Integer(mathlibx.getix())); } 
    return result; }

    public static int getiy() { return iy; }

    public static List getAlliy(List mathlibs)
  { List result = new Vector();
   if (mathlibs.size() > 0)
   { result.add(new Integer(MathLib.iy)); }
    return result; }

    public static List getAllOrderediy(List mathlibs)
  { List result = new Vector();
    for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      result.add(new Integer(mathlibx.getiy())); } 
    return result; }

    public static int getiz() { return iz; }

    public static List getAlliz(List mathlibs)
  { List result = new Vector();
   if (mathlibs.size() > 0)
   { result.add(new Integer(MathLib.iz)); }
    return result; }

    public static List getAllOrderediz(List mathlibs)
  { List result = new Vector();
    for (int i = 0; i < mathlibs.size(); i++)
    { MathLib mathlibx = (MathLib) mathlibs.get(i);
      result.add(new Integer(mathlibx.getiz())); } 
    return result; }

    public static double pi()
  {   double result = 0;
 
  result = 3.14159265;
    return result;
  }


    public static double e()
  {   double result = 0;
 
  result = Math.exp(1);
    return result;
  }


    public static void setSeeds(int x,int y,int z)
  { MathLib.setix(x);
    MathLib.setiy(y);
    MathLib.setiz(z);
  }

    public static double nrandom()
  {   double result = 0;
 
  ix = ( ix * 171 ) % 30269;
   iy = ( iy * 172 ) % 30307;
   iz = ( iz * 170 ) % 30323;
   result = ( ix / 30269.0 + iy / 30307.0 + iz / 30323.0 );
             return result;
  }


    public static double random()
  {   double result = 0;
 
  double r = MathLib.nrandom(); 
     result = ( r - ((int) Math.floor(r)) );
       return result;
  }


    public static long combinatorial(int n,int m)
  {   long result = 0;
    if (n < m || m < 0) { return result; } 
   
  if (n - m < m) 
  {   result = Set.prdint(Set.integerSubrange(m + 1,n)) / Set.prdint(Set.integerSubrange(1,n - m));
 
  }  else
      if (n - m >= m) 
  {   result = Set.prdint(Set.integerSubrange(n - m + 1,n)) / Set.prdint(Set.integerSubrange(1,m));
 
  }       return result;
  }


    public static long factorial(int x)
  {   long result = 0;
 
  if (x < 2) 
  {   result = 1;
 
  }  else
      if (x >= 2) 
  {   result = Set.prdint(Set.integerSubrange(2,x));
 
  }       return result;
  }


    public static int gcd(int x,int y)
  {  int result;
  int l;
    int k;
  l = x;
  k = y;
    while (l != 0 && k != 0) 
  {    if (l < k) { k = k % l; }
 else l = l % k; }
     if (l == 0) {   return k; }
 else   return l;





  }


    public static int lcm(int x,int y)
  {   int result = 0;
    if (x < 1 || y < 1) { return result; } 
   
  result = ( x * y ) / MathLib.gcd(x,y);
    return result;
  }


    public static double integrate(List f,double d)
  {   double result = 0;
    if (f.size() < 1 || d < 1) { return result; } 
   
  result = d * Set.sumdouble(Set.subrange(f,2,f.size() - 1)) + d * ( ((Double) f.get(0)).doubleValue() + ((Double) Set.last(f)).doubleValue() ) / 2.0;
    return result;
  }


    public static double asinh(double x)
  {   double result = 0;
 
  result = Math.log(( x + Math.sqrt(( x * x + 1 )) ));
    return result;
  }


    public static double acosh(double x)
  {   double result = 0;
    if (x < 1) { return result; } 
   
  result = Math.log(( x + Math.sqrt(( x * x - 1 )) ));
    return result;
  }


    public static double atanh(double x)
  {   double result = 0;
    if (x == 1) { return result; } 
   
  result = 0.5 * Math.log(( ( 1 + x ) / ( 1 - x ) ));
    return result;
  }


    public static boolean isPrime(int x)
  {  boolean result;
   if (x < 2) {   return false; }

     if (x == 2) {   return true; }

    int b = ((int) Math.floor(Math.sqrt(x)));
    int i = 2;
    while (i <= b) 
  {    if (x % i == 0) {   return false; }
 else i = i + 1; }
    return true;





  }



}


class Testprimes
  implements SystemTypes
{

  public Testprimes()
  {

  }



  public String toString()
  { String _res_ = "(Testprimes) ";
    return _res_;
  }

  public static Testprimes parseCSV(String _line)
  { if (_line == null) { return null; }
    Vector _line1vals = new Vector();
    StringTokenizer _st1 =
      new StringTokenizer(_line, ",");
    while (_st1.hasMoreTokens())
    { String _str = _st1.nextToken();
      if (_str != null) 
      { _line1vals.add(_str.trim()); }
    }
    Testprimes testprimesx = new Testprimes();
    return testprimesx;
  }


  public void writeCSV(PrintWriter _out)
  { Testprimes testprimesx = this;
    _out.println();
  }


  


}



public class Controller implements SystemTypes, ControllerInterface
{
  Vector mathlibs = new Vector();
  Vector testprimess = new Vector();
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
    { File _mathlib = new File("MathLib.csv");
      __br = new BufferedReader(new FileReader(_mathlib));
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
        { MathLib mathlibx = MathLib.parseCSV(__s.trim());
          if (mathlibx != null)
          { __cont.addMathLib(mathlibx); }
        }
      }
    }
    catch(Exception __e) { }
    try
    { File _testprimes = new File("Testprimes.csv");
      __br = new BufferedReader(new FileReader(_testprimes));
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
        { Testprimes testprimesx = Testprimes.parseCSV(__s.trim());
          if (testprimesx != null)
          { __cont.addTestprimes(testprimesx); }
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
  for (int _i = 0; _i < mathlibs.size(); _i++)
  { MathLib mathlibx_ = (MathLib) mathlibs.get(_i);
    out.println("mathlibx_" + _i + " : MathLib");
    out.println("mathlibx_" + _i + ".ix = " + mathlibx_.getix());
    out.println("mathlibx_" + _i + ".iy = " + mathlibx_.getiy());
    out.println("mathlibx_" + _i + ".iz = " + mathlibx_.getiz());
  }

  for (int _i = 0; _i < testprimess.size(); _i++)
  { Testprimes testprimesx_ = (Testprimes) testprimess.get(_i);
    out.println("testprimesx_" + _i + " : Testprimes");
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
    instancemap.put("mathlibs", new Vector()); 
    instancemap.put("mathlib",new Vector()); 
    entcodes.add("mathlibs");
    entcodes.add("mathlib");
    entmap.put("mathlibs","MathLib");
    entmap.put("mathlib","MathLib");
    eallatts = new Vector();
    eallatts.add("ix");
    eallatts.add("iy");
    eallatts.add("iz");
    allattsmap.put("MathLib", eallatts);
    eallatts = new Vector();
    stringattsmap.put("MathLib", eallatts);
    eallatts = new Vector();
    onerolesmap.put("MathLib", eallatts);
    instancemap.put("testprimess", new Vector()); 
    instancemap.put("testprimes",new Vector()); 
    entcodes.add("testprimess");
    entcodes.add("testprimes");
    entmap.put("testprimess","Testprimes");
    entmap.put("testprimes","Testprimes");
    eallatts = new Vector();
    allattsmap.put("Testprimes", eallatts);
    eallatts = new Vector();
    stringattsmap.put("Testprimes", eallatts);
    eallatts = new Vector();
    onerolesmap.put("Testprimes", eallatts);
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
    for (int _i = 0; _i < mathlibs.size(); _i++)
    { MathLib mathlibx_ = (MathLib) mathlibs.get(_i);
       out.print("<mathlibs xsi:type=\"My:MathLib\"");
    out.print(" ix=\"" + mathlibx_.getix() + "\" ");
    out.print(" iy=\"" + mathlibx_.getiy() + "\" ");
    out.print(" iz=\"" + mathlibx_.getiz() + "\" ");
    out.println(" />");
  }

    for (int _i = 0; _i < testprimess.size(); _i++)
    { Testprimes testprimesx_ = (Testprimes) testprimess.get(_i);
       out.print("<testprimess xsi:type=\"My:Testprimes\"");
    out.println(" />");
  }

    out.println("</UMLRSDS:model>");
    out.close(); 
  }


  public void saveCSVModel()
  { try {
      File _mathlib = new File("MathLib.csv");
      PrintWriter _out_mathlib = new PrintWriter(new BufferedWriter(new FileWriter(_mathlib)));
      for (int __i = 0; __i < mathlibs.size(); __i++)
      { MathLib mathlibx = (MathLib) mathlibs.get(__i);
        mathlibx.writeCSV(_out_mathlib);
      }
      _out_mathlib.close();
      File _testprimes = new File("Testprimes.csv");
      PrintWriter _out_testprimes = new PrintWriter(new BufferedWriter(new FileWriter(_testprimes)));
      for (int __i = 0; __i < testprimess.size(); __i++)
      { Testprimes testprimesx = (Testprimes) testprimess.get(__i);
        testprimesx.writeCSV(_out_testprimes);
      }
      _out_testprimes.close();
    }
    catch(Exception __e) { }
  }



  public void addMathLib(MathLib oo) { mathlibs.add(oo); }

  public void addTestprimes(Testprimes oo) { testprimess.add(oo); }



  public MathLib createMathLib()
  { MathLib mathlibx = new MathLib();
    addMathLib(mathlibx);
    return mathlibx;
  }

  public Testprimes createTestprimes()
  { Testprimes testprimesx = new Testprimes();
    addTestprimes(testprimesx);
    return testprimesx;
  }


public void setix(int ix_x) 
  { MathLib.setix(ix_x);
  for (int i = 0; i < mathlibs.size(); i++)
  { MathLib mathlibx = (MathLib) mathlibs.get(i);
    setix(mathlibx,ix_x); } }

  public void setix(MathLib mathlibx, int ix_x) 
  { mathlibx.localSetix(ix_x);
    }


public void setiy(int iy_x) 
  { MathLib.setiy(iy_x);
  for (int i = 0; i < mathlibs.size(); i++)
  { MathLib mathlibx = (MathLib) mathlibs.get(i);
    setiy(mathlibx,iy_x); } }

  public void setiy(MathLib mathlibx, int iy_x) 
  { mathlibx.localSetiy(iy_x);
    }


public void setiz(int iz_x) 
  { MathLib.setiz(iz_x);
  for (int i = 0; i < mathlibs.size(); i++)
  { MathLib mathlibx = (MathLib) mathlibs.get(i);
    setiz(mathlibx,iz_x); } }

  public void setiz(MathLib mathlibx, int iz_x) 
  { mathlibx.localSetiz(iz_x);
    }



  public static void setSeeds(int x,int y,int z)
  { MathLib.setSeeds(x, y, z); }



  public void killAllMathLib(List mathlibxx)
  { for (int _i = 0; _i < mathlibxx.size(); _i++)
    { killMathLib((MathLib) mathlibxx.get(_i)); }
  }

  public void killMathLib(MathLib mathlibxx)
  { if (mathlibxx == null) { return; }
   mathlibs.remove(mathlibxx);
  }



  public void killAllTestprimes(List testprimesxx)
  { for (int _i = 0; _i < testprimesxx.size(); _i++)
    { killTestprimes((Testprimes) testprimesxx.get(_i)); }
  }

  public void killTestprimes(Testprimes testprimesxx)
  { if (testprimesxx == null) { return; }
   testprimess.remove(testprimesxx);
  }




  
  
  public void testprimes() 
  { 

       System.out.println("" + MathLib.isPrime(911));

       System.out.println("" + MathLib.isPrime(70771));


  }


 
}



