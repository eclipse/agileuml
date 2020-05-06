import java.util.Vector; 
import java.util.List; 
import java.util.StringTokenizer; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Class Diagram */ 

// Subclasses are: Entity, Attribute, Association, Type, BehaviouralFeature, UseCase

public abstract class ModelElement implements SystemTypes
{ protected String name;
  protected Vector stereotypes = new Vector(); // of String
  protected Vector comments = new Vector(); // of String

  /* attribute kind */
  public static final int NONE = 0; 
  public static final int SEN = 2;
  public static final int INTERNAL = 3;
  public static final int ACT = 7;
  public static final int DERIVED = 5; // no set operations
  public static final int SUMMARY = 8; 
 
  /* role multiplicity */ 
  public static final int QUALIFIER = -4; 
  public static final int AGGREGATION01 = -3; 
  public static final int AGGREGATION1 = -2; 
  public static final int ZEROONE = -1;
  public static final int MANY = 0;
  public static final int ONE = 1;
 
  /* feature visibility */
  public static final int PUBLIC = 0; 
  public static final int PRIVATE = 1; 
  public static final int PROTECTED = 2; 
  public static final int PACKAGE = 3; 
     
  static java.util.Map nsscache = new java.util.HashMap(); 

  public ModelElement(String nme)
  { name = nme; }
 
  public String getName()
  { return name; }

  public void setName(String newname)
  { name = newname; }

  public String cg(CGSpec cgs)
  { return this + ""; }

  public static Vector getNames(Vector elems)
  { Vector res = new Vector(); 
    for (int i = 0; i < elems.size(); i++) 
    { ModelElement e = (ModelElement) elems.get(i); 
      res.add(e.getName()); 
    } 
    return res; 
  }

  public static String composeNames(Vector elems)
  { String res = ""; 
    for (int i = 0; i < elems.size(); i++) 
    { ModelElement e = (ModelElement) elems.get(i); 
      res = res + e.getName(); 
      if (i < elems.size() - 1) 
      { res = res + "."; } 
    } 
    return res; 
  }

  public static String underscoredNames(Vector elems)
  { String res = ""; 
    for (int i = 0; i < elems.size(); i++) 
    { ModelElement e = (ModelElement) elems.get(i); 
      res = res + e.getName(); 
      if (i < elems.size() - 1) 
      { res = res + "_"; } 
    } 
    return res; 
  }

  public static int composeCard1(Vector elems)
  { int res = ModelElement.MANY;
    if (elems.size() == 0) 
    { return res; } 

    Attribute att = (Attribute) elems.get(0); 
    int attcard1 = att.getCard1(); 
    if (elems.size() == 1) 
    { return attcard1; } 

    Vector tail = new Vector(); 
    tail.addAll(elems); 
    tail.remove(0); 
    int tailcard1 = ModelElement.composeCard1(tail); 
    if (attcard1 == MANY || tailcard1 == MANY) 
    { return MANY; } 
    else if (attcard1 == ZEROONE || tailcard1 == ZEROONE) 
    { return ZEROONE; } 
    return ONE; 
  }

  public static boolean composeUnique(Vector elems)
  { boolean res = false; 
    if (elems.size() == 0) 
    { return res; } 

    Attribute att = (Attribute) elems.get(0); 
    boolean attuniq = att.isUnique(); 
    if (elems.size() == 1) 
    { return attuniq; } 

    Vector tail = new Vector(); 
    tail.addAll(elems); 
    tail.remove(0); 
    boolean tailuniq = ModelElement.composeUnique(tail); 
    if (attuniq && tailuniq) 
    { return true; } 
    return false; 
  }

  public static String composeAggregation(Vector elems)
  { String res = "";
    if (elems.size() == 0) 
    { return res; } 

    Attribute att = (Attribute) elems.get(0); 
    if (att.isAggregation())
    { res = "aggregation"; 
      if (elems.size() == 1) 
      { return res; }
    }  

    Vector tail = new Vector(); 
    tail.addAll(elems); 
    tail.remove(0); 
    String tailagg = ModelElement.composeAggregation(tail); 
    if ("aggregation".equals(tailagg) && "aggregation".equals(res)) 
    { return res; } 
     
    return ""; 
  }

  public static String composeSourceTarget(Vector elems)
  { String res = "";
    if (elems.size() == 0) 
    { return res; } 

    Attribute att = (Attribute) elems.get(0); 
    if (att.isSource())
    { res = "source"; 
      if (elems.size() == 1) 
      { return res; }
    }  
    else if (att.isTarget())
    { res = "target"; 
      if (elems.size() == 1) 
      { return res; }
    }  


    Vector tail = new Vector(); 
    tail.addAll(elems); 
    tail.remove(0); 
    String tailagg = ModelElement.composeSourceTarget(tail); 
    if ("source".equals(tailagg) && "source".equals(res)) 
    { return res; } 
    else if ("target".equals(tailagg) && "target".equals(res)) 
    { return res; }  
    return ""; 
  }

  public void setStereotypes(Vector s) 
  { stereotypes = s; } 

  public void setStereotypes(String s) 
  { Vector strs = new Vector(); 
    StringTokenizer st = new StringTokenizer(s); 
    while (st.hasMoreTokens())
    { String se = st.nextToken().trim();      
      if (!se.equals("none")) 
      { strs.add(se); }  
    } 
    setStereotypes(strs); 
  } 

  public String getStereotype(int i)
  { if (0 <= i && i < stereotypes.size())
    { return (String) stereotypes.get(i); }
    return null; 
  }

  public Vector getStereotypes()
  { return stereotypes; }

  public void addStereotype(String s)
  { if (stereotypes.contains(s)) { } 
    else 
    { stereotypes.add(s); }
  } 

  public void addStereotypes(Vector ss)
  { for (int i = 0; i < ss.size(); i++) 
    { String s = (String) ss.get(i); 
      addStereotype(s);
    }
  }  

  public void removeStereotype(String s)
  { stereotypes.remove(s); }

  public boolean hasStereotype(String s)
  { return stereotypes.contains(s); }

  public Vector getComments()
  { return comments; }

  public void addComment(String s)
  { comments.add(s); } 

  public void removeComment(String s)
  { comments.remove(s); }

  public boolean isDerived()
  { return stereotypes.contains("derived"); } 

  public String toString()
  { return name; } 

  public static String destring(String s)
  { if (s.length() > 2)
    { if (s.charAt(0) == '"' && s.charAt(s.length()-1) == '"') 
      { return s.substring(1,s.length()-1); } 
    } 
    return s; 
  } 

  public static Type model2type(String val) 
  { String sval = val; 
    if ("void".equals(val)) { return null; } 
    else if ("Integer".equals(val)) { sval = "int"; } 
    else if ("Real".equals(val)) { sval = "double"; } 
    else if ("Boolean".equals(val)) { sval = "boolean"; } 
    else if ("SequenceType".equals(val)) { sval = "Sequence"; } 
    else if ("SetType".equals(val)) { sval = "Set"; } 

    Type typ = new Type(sval, null);
    return typ; 
  }                   

  public void asTextModel(PrintWriter out) { } 

  public boolean equals(Object e) 
  { if (e != null && e instanceof ModelElement)
    { return e.toString().equals(toString()); } 
    return false; 
  } 

  public String saveData()
  { return name; }  // default implementation

  public void saveData(PrintWriter out)
  { out.println(name); }  // default implementation

  public static String baseName(String ename)
  { int ind = ename.indexOf("$");
    if (ind < 0) { return ename; }
    return ename.substring(ind + 1,ename.length());
  }

  public static ModelElement lookupByName(String nme, List mes)
  { for (int i = 0; i < mes.size(); i++) 
    { ModelElement me = (ModelElement) mes.get(i); 
      if (me.getName().equals(nme))
      { return me; } 
    } 
    return null; 
  } 

  public static ModelElement lookupByName(String nme, Vector mes)
  { for (int i = 0; i < mes.size(); i++) 
    { ModelElement me = (ModelElement) mes.get(i); 
      if (me.getName().equals(nme))
      { return me; } 
    } 
    return null; 
  } 

  public static Expression lookupExpressionByName(String nme, Vector mes)
  { for (int i = 0; i < mes.size(); i++) 
    { Expression me = (Expression) mes.get(i); 
      if ((me + "").equals(nme))
      { return me; } 
    } 
    return null; 
  } 

  public static Expression lookupExpressionByData(String nme, Vector mes)
  { for (int i = 0; i < mes.size(); i++) 
    { BasicExpression me = (BasicExpression) mes.get(i); 
      if ((me.data).equals(nme))
      { return me; } 
    } 
    return null; 
  } 

  public static boolean containsName(String nme, Vector mes)
  { ModelElement me = lookupByName(nme,mes); 
    return me != null; 
  } 

  public static String tab(int indent)
  { String res = ""; 
    for (int i = 0; i < indent; i++) 
    { res = res + "  "; } 
    return res; 
  } 

  public abstract void generateJava(PrintWriter out);

  public static int convertCard(String s)
  { int n = 0;
    if ("*".equals(s))
    { return MANY; }
    if ("aggregation 1".equals(s))
    { return AGGREGATION1; } 
    if ("aggregation 0..1".equals(s))
    { return AGGREGATION01; } 
    if ("qualifier".equals(s))
    { return QUALIFIER; } 
    if (s.indexOf(".") < 0)  // not a range
    { try
      { n = Integer.parseInt(s); }
      catch (Exception e)
      { System.err.println("not a valid number");
        return MANY;
      }
      return n;  // ONE is 1
    }
    else
    { return ZEROONE; } // assume
  }

  public static String convertCard(int c)
  { if (c == ZEROONE)
    { return "0..1"; }
    if (c == ONE)
    { return "1"; }
    if (c == MANY)
    { return "*"; }
    if (c == AGGREGATION1)
    { return "aggregation 1"; } 
    if (c == AGGREGATION01)
    { return "aggregation 0..1"; } 
    if (c == QUALIFIER)
    { return "qualifier"; } 
    return ""+c;
  }

  public static int maxCard(int c, int q)
  { if (c == ZEROONE)
    { return 1; } 
    if (c == ONE) 
    { return 1; } 
    if (c == MANY)
    { return q; } 
    return q; // the card of the target entity 
  } 

  public static int mergeMultiplicities(int m1, int m2)
  { // returns closest generalisation of m1, m2: ONE <= ZEROONE <= MANY
    if (m1 == MANY || m2 == MANY) 
    { return MANY; } 
    if (m1 == ZEROONE || m2 == ZEROONE) 
    { return ZEROONE; } 
    return ONE; 
  } 

  public static String capitalise(String nme)
  { String fl = nme.substring(0,1);
    String rem = 
        nme.substring(1,nme.length());
    return fl.toUpperCase() + rem;
  }

  public String constructorParameterCPP()
  { return null; } 

    public static int editDistanceMatrix(List d,String s,String t,int m,int n)
  {  int result;
  List _range1 = new Vector();
  _range1.addAll(Ocl.integerSubrange(1,n));
  for (int _i0 = 0; _i0 < _range1.size(); _i0++)
  { int j = ((Integer) _range1.get(_i0)).intValue();
      List _range3 = new Vector();
  _range3.addAll(Ocl.integerSubrange(1,m));
  for (int _i2 = 0; _i2 < _range3.size(); _i2++)
  { int i = ((Integer) _range3.get(_i2)).intValue();
      List sqi;
  sqi = ((List) d.get(i));
     if ((s.charAt(i - 1) + "").equals((t.charAt(j - 1) + ""))) 
   { sqi.set(j, new Integer(((Integer) ((List) d.get(i - 1)).get(j - 1)).intValue())); }
 
    else 
      { sqi.set((j), 
                new Integer(((Integer) Ocl.min((new SystemTypes.Ocl()).add(new Integer(( ((Integer) ((List) d.get(i - 1)).get(j)).intValue() + 1 ))).add(
                   new Integer(( ((Integer) ((List) d.get(i)).get(j - 1)).intValue() + 1 ))).add(new Integer(( ((Integer) ((List) d.get(i - 1)).get(j - 1)).intValue() + 1 ))).getElements())).intValue())); } 


  }
  }
    return ((Integer) ((List) d.get(m)).get(n)).intValue();

  }


    public static int editDistance(String s,String t)
  {  int result;
  int m;
    int n;
  m = s.length();
  n = t.length();
    List d;
  d = Ocl.collect_1(Ocl.integerSubrange(1,m + 1),n);
    List _range5 = new Vector();
  _range5.addAll(Ocl.integerSubrange(1,m + 1));
  for (int _i4 = 0; _i4 < _range5.size(); _i4++)
  { int i = ((Integer) _range5.get(_i4)).intValue();
      List sq;
  sq = ((List) d.get(i - 1));
  sq.set((1 -1), new Integer(i - 1));


  }
    List sq1;
  sq1 = ((List) d.get(1 - 1));
    List _range7 = new Vector();
  _range7.addAll(Ocl.integerSubrange(1,n + 1));
  for (int _i6 = 0; _i6 < _range7.size(); _i6++)
  { int j = ((Integer) _range7.get(_i6)).intValue();
    sq1.set((j -1), new Integer(j - 1));
  }
    return editDistanceMatrix(d,s,t,m,n);

  }

  public static double similarity(String s,String t)
  
  {   double result = 0;
 
  
      Double res = (Double) nsscache.get(s + " " + t); // assume no spaces in s or t
      if (res != null)
      { return res.doubleValue(); } 

      if (s.length() == 0 && t.length() == 0) 
  
      {   result = 1;
 
  }  
      else
      
        if (s.length() + t.length() > 0) 
 
        {   result = ( 1.0 * ( s.length() + t.length() - editDistance(s,t) ) ) / ( s.length() + t.length() );
 
  }    
      nsscache.put(s + " " + t, new Double(result)); 

   return result;
  }



  public static boolean haveCommonSuffix(String s, String t)
  { int n = s.length(); 
    int m = t.length(); 
    if (s.endsWith(t)) { return true; } 
    if (t.endsWith(s)) { return true; } 
    if (n < m) 
    { for (int i = 3; i < n; i++) 
      { String sub = s.substring(n-i,n); 
        // System.out.println(sub); 
        if (t.endsWith(sub))
        { return true; } 
      } 
    } 
    else 
    { for (int i = 3; i < m; i++) 
      { String sub = t.substring(m-i,m); 
        // System.out.println(sub); 
        if (s.endsWith(sub))
        { return true; } 
      } 
    } 
    return false; 
  } 
 
  public static String longestCommonSuffix(String s, String t)
  { int n = s.length(); 
    int m = t.length(); 
    if (s.endsWith(t)) { return t; } 
    if (t.endsWith(s)) { return s; } 
    if (n < m) 
    { for (int i = 1; i < n-2; i++) 
      { String sub = s.substring(i,n); 
        // System.out.println(sub); 
        if (t.endsWith(sub))
        { return sub; } 
      } 
    } 
    else 
    { for (int i = 1; i < m-2; i++) 
      { String sub = t.substring(i,m); 
        // System.out.println(sub); 
        if (s.endsWith(sub))
        { return sub; } 
      } 
    } 
    return ""; 
  } 

  public static boolean haveCommonPrefix(String s, String t)
  { int n = s.length(); 
    int m = t.length(); 
    if (s.startsWith(t)) { return true; } 
    if (t.startsWith(s)) { return true; } 
    if (n < m) 
    { for (int i = n-1; i > 1; i--) 
      { String sub = s.substring(0,i); 
        // System.out.println(sub); 
        if (t.startsWith(sub))
        { return true; } 
      } 
    } 
    else 
    { for (int i = m-1; i > 1; i--) 
      { String sub = t.substring(0,i); 
        // System.out.println(sub); 
        if (s.startsWith(sub))
        { return true; } 
      } 
    } 
    return false; 
  } 

  public static String longestCommonPrefix(String s, String t)
  { int n = s.length(); 
    int m = t.length(); 
    if (s.startsWith(t)) { return t; } 
    if (t.startsWith(s)) { return s; } 
    if (n < m) 
    { for (int i = n-1; i > 1; i--) 
      { String sub = s.substring(0,i); 
        // System.out.println(sub); 
        if (t.startsWith(sub))
        { return sub; } 
      } 
    } 
    else 
    { for (int i = m-1; i > 1; i--) 
      { String sub = t.substring(0,i); 
        // System.out.println(sub); 
        if (s.startsWith(sub))
        { return sub; } 
      } 
    } 
    return ""; 
  } 

private static void addBuffer(StringBuffer b, Vector res)
{ if (b != null && b.length() > 0)
  { res.add(b.toString()); }
  // b.clear();
}

public static Vector splitIntoWords(String str)
{ int len = str.length();
  Vector res = new Vector();
  StringBuffer buf = new StringBuffer();
  boolean inUpper = false;
  boolean inLower = false;
  boolean inNumber = false;
  boolean inOther = true;
  char prevc = ' ';

  for (int k = 0; k < len; k++)
  { char c = str.charAt(k);
    if (Character.isDigit(c))
    { if (inLower) 
      { addBuffer(buf,res);
        buf = new StringBuffer(); 
        inLower = false;
      }
      inUpper = false;
      inOther = false;
      inNumber = true;
    }
    else if (Character.isUpperCase(c))
    { if (inLower) 
      { addBuffer(buf,res);
        buf = new StringBuffer(); 
        inLower = false;
      }
      inUpper = true;
      inOther = false;
      inNumber = false;
      prevc = c;
    }
    else if (Character.isLowerCase(c))
    { if (inOther || inNumber) 
      { buf = new StringBuffer(); 
        buf.append(c); 
      }
      else if (inLower) 
      { buf.append(c); }
      else if (inUpper)
      { buf.append(prevc);
        buf.append(c);
        inUpper = false;
      }
      inNumber = false;
      inOther = false;
      inLower = true;
    }
    else // other character
    { if (inLower) 
      { addBuffer(buf,res);
        buf = new StringBuffer(); 
        inLower = false;
      }
      inUpper = false;
      inOther = true;
      inNumber = false;
    }
  }
  if (inLower) 
  { addBuffer(buf,res); }
  return res; 
}

  public static void main(String[] args) 
  { System.out.println(longestCommonSuffix("DataType", "CType")); 
    System.out.println(longestCommonSuffix("PTArc", "TPArc"));

    System.out.println(splitIntoWords("memberOf.father")); 
     
    // System.out.println(longestCommonSuffix("ENamedElement", "NamedElement"));
    System.out.println(similarity("type.name.ff","type.owner.gg")); 
    System.out.println(similarity("owner.name.ff","type.name.gg"));     
    Vector v = new Vector(); 
    System.out.println(Entity.nmsSimilarity("type.name.ff","type.owner.gg",v)); 
    System.out.println(Entity.nmsSimilarity("owner.name.ff","type.name.gg",v));     
    System.out.println(Entity.nmsSimilarity("father","Male",v)); 
    System.out.println(Entity.nmsSimilarity("mother","Female",v));     

    System.out.println(longestCommonPrefix("colour", "color")); 
  } 

}
