import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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

public interface SystemTypes
{
  public class Ocl
  { private Vector elements = new Vector();






  public static List collect_0(List _l)
  { // implements: Integer.subrange(1,n + 1)->collect( j | 0 )
    List _results_0 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { int j = ((Integer) _l.get(_i)).intValue();
      Integer collect_x = new Integer(0);
      if (collect_x != null) { _results_0.add(collect_x); }
    }
    return _results_0;
  }

  public static List collect_1(List _l,int n)
  { // implements: Integer.subrange(1,m + 1)->collect( i | Integer.subrange(1,n + 1)->collect( j | 0 ) )
    List _results_1 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { int i = ((Integer) _l.get(_i)).intValue();
      List collect_x = Ocl.collect_0(Ocl.integerSubrange(1,n + 1));
      if (collect_x != null) { _results_1.add(collect_x); }
    }
    return _results_1;
  }


    public static boolean equals(List a, List b)
    { return a.containsAll(b) && b.containsAll(a); }


    public Ocl add(Object x)
    { if (x != null) { elements.add(x); }
      return this; }

    public Ocl add(int x)
    { elements.add(new Integer(x));
      return this; }

    public Ocl add(long x)
    { elements.add(new Long(x));
      return this; }

    public Ocl add(double x)
    { elements.add(new Double(x));
      return this; }

    public Ocl add(boolean x)
    { elements.add(new Boolean(x));
      return this; }

    public List getElements() { return elements; }


  public static Comparable max(List l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) l.get(0); 
    for (int i = 1; i < l.size(); i++)
    { Comparable e = (Comparable) l.get(i);
      if (res.compareTo(e) < 0) { res = e; } }
    return res; }


  public static Comparable min(List l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) l.get(0); 
    for (int i = 1; i < l.size(); i++)
    { Comparable e = (Comparable) l.get(i);
      if (res.compareTo(e) > 0) { res = e; } }
    return res; }


  public static List union(List a, List b)
  { List res = new Vector(); 
    for (int i = 0; i < a.size(); i++)
    { Object x = a.get(i); 
      if (x == null || res.contains(x)) { } else { res.add(x); } 
    }
    for (int j = 0; j < b.size(); j++)
    { Object y = b.get(j); 
      if (y == null || res.contains(y)) { } else { res.add(y); }
    }
    return res; }


  public static List subtract(List a, List b)
  { List res = new Vector(); 
    res.addAll(a);
    res.removeAll(b);
    return res; }

  public static String subtract(String a, String b)
  { String res = ""; 
    for (int i = 0; i < a.length(); i++)
    { if (b.indexOf(a.charAt(i)) < 0) { res = res + a.charAt(i); } }
    return res; }



  public static List intersection(List a, List b)
  { List res = new Vector(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }



  public static List symmetricDifference(List a, List b)
  { List res = new Vector();
    for (int i = 0; i < a.size(); i++)
    { Object _a = a.get(i);
      if (b.contains(_a) || res.contains(_a)) { }
      else { res.add(_a); }
    }
    for (int j = 0; j < b.size(); j++)
    { Object _b = b.get(j);
      if (a.contains(_b) || res.contains(_b)) { }
      else { res.add(_b); }
    }
    return res;
  }



  public static boolean isUnique(List evals)
  { List vals = new Vector(); 
    for (int i = 0; i < evals.size(); i++)
    { Object ob = evals.get(i); 
      if (vals.contains(ob)) { return false; }
      vals.add(ob);
    }
    return true;
  }


  public static int sumint(List a)
  { int sum = 0; 
    for (int i = 0; i < a.size(); i++)
    { Integer x = (Integer) a.get(i); 
      if (x != null) { sum += x.intValue(); }
    } 
    return sum; }

  public static double sumdouble(List a)
  { double sum = 0.0; 
    for (int i = 0; i < a.size(); i++)
    { Double x = (Double) a.get(i); 
      if (x != null) { sum += x.doubleValue(); }
    } 
    return sum; }

  public static long sumlong(List a)
  { long sum = 0; 
    for (int i = 0; i < a.size(); i++)
    { Long x = (Long) a.get(i); 
      if (x != null) { sum += x.longValue(); }
    } 
    return sum; }

  public static String sumString(List a)
  { String sum = ""; 
    for (int i = 0; i < a.size(); i++)
    { Object x = a.get(i); 
      sum = sum + x; }
    return sum;  }



  public static int prdint(List a)
  { int res = 1; 
    for (int i = 0; i < a.size(); i++)
    { Integer x = (Integer) a.get(i); 
      if (x != null) { res *= x.intValue(); }
    } 
    return res; }

  public static double prddouble(List a)
  { double res = 1; 
    for (int i = 0; i < a.size(); i++)
    { Double x = (Double) a.get(i); 
      if (x != null) { res *= x.doubleValue(); }
    } 
    return res; }

  public static long prdlong(List a)
  { long res = 1; 
    for (int i = 0; i < a.size(); i++)
    { Long x = (Long) a.get(i); 
      if (x != null) { res *= x.longValue(); }
    }
    return res;  }



  public static List concatenate(List a, List b)
  { List res = new Vector(); 
    res.addAll(a); 
    res.addAll(b); 
    return res; }




  public static List asSet(List a)
  { List res = new Vector(); 
    for (int i = 0; i < a.size(); i++)
    { Object obj = a.get(i);
      if (res.contains(obj)) { } 
      else { res.add(obj); }
    } 
    return res; 
  }


  public static List reverse(List a)
  { List res = new Vector(); 
    for (int i = a.size() - 1; i >= 0; i--)
    { res.add(a.get(i)); } 
    return res; }

  public static String reverse(String a)
  { String res = ""; 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a.charAt(i); } 
    return res; }



  public static List front(List a)
  { List res = new Vector(); 
    for (int i = 0; i < a.size() - 1; i++)
    { res.add(a.get(i)); } 
    return res; }


  public static List tail(List a)
  { List res = new Vector(); 
    for (int i = 1; i < a.size(); i++)
    { res.add(a.get(i)); } 
    return res; }


    public static Object first(List v)
    { if (v.size() == 0) { return null; }
      return v.get(0);
    }


    public static Object last(List v)
    { if (v.size() == 0) { return null; }
      return v.get(v.size() - 1);
    }



  public static List sort(final List a)
  { int i = a.size()-1;
    return mergeSort(a,0,i);
  }

  static List mergeSort(final List a, int ind1, int ind2)
  { List res = new Vector();
    if (ind1 > ind2)
    { return res; }
    if (ind1 == ind2)
    { res.add(a.get(ind1));
      return res;
    }
    int mid = (ind1 + ind2)/2;
    List a1;
    List a2;
    if (mid == ind1)
    { a1 = new Vector();
      a1.add(a.get(ind1));
      a2 = mergeSort(a,mid+1,ind2);
    }
    else
    { a1 = mergeSort(a,ind1,mid-1);
      a2 = mergeSort(a,mid,ind2);
    }
    int i = 0;
    int j = 0;
    while (i < a1.size() && j < a2.size())
    { Comparable e1 = (Comparable) a1.get(i); 
      Comparable e2 = (Comparable) a2.get(j);
      if (e1.compareTo(e2) < 0) // e1 < e2
      { res.add(e1);
        i++; // get next e1
      } 
      else 
      { res.add(e2);
        j++; 
      } 
    } 
    if (i == a1.size())
    { for (int k = j; k < a2.size(); k++)
      { res.add(a2.get(k)); } 
    } 
    else 
    { for (int k = i; k < a1.size(); k++) 
      { res.add(a1.get(k)); } 
    } 
    return res;
  }


  public static List sortedBy(final List a, List f)
  { int i = a.size()-1;
    java.util.Map f_map = new java.util.HashMap();
    for (int j = 0; j < a.size(); j++)
    { f_map.put(a.get(j), f.get(j)); }
    return mergeSort(a,f_map,0,i);
  }

  static List mergeSort(final List a, java.util.Map f, int ind1, int ind2)
  { List res = new Vector();
    if (ind1 > ind2)
    { return res; }
    if (ind1 == ind2)
    { res.add(a.get(ind1));
      return res;
    }
    if (ind2 == ind1 + 1)
    { Comparable e1 = (Comparable) f.get(a.get(ind1)); 
      Comparable e2 = (Comparable) f.get(a.get(ind2));
      if (e1.compareTo(e2) < 0) // e1 < e2
      { res.add(a.get(ind1)); res.add(a.get(ind2)); return res; }
      else 
      { res.add(a.get(ind2)); res.add(a.get(ind1)); return res; }
    }
    int mid = (ind1 + ind2)/2;
    List a1;
    List a2;
    if (mid == ind1)
    { a1 = new Vector();
      a1.add(a.get(ind1));
      a2 = mergeSort(a,f,mid+1,ind2);
    }
    else
    { a1 = mergeSort(a,f,ind1,mid-1);
      a2 = mergeSort(a,f,mid,ind2);
    }
    int i = 0;
    int j = 0;
    while (i < a1.size() && j < a2.size())
    { Comparable e1 = (Comparable) f.get(a1.get(i)); 
      Comparable e2 = (Comparable) f.get(a2.get(j));
      if (e1.compareTo(e2) < 0) // e1 < e2
      { res.add(a1.get(i));
        i++; // get next e1
      } 
      else 
      { res.add(a2.get(j));
        j++; 
      } 
    } 
    if (i == a1.size())
    { for (int k = j; k < a2.size(); k++)
      { res.add(a2.get(k)); } 
    } 
    else 
    { for (int k = i; k < a1.size(); k++) 
      { res.add(a1.get(k)); } 
    } 
    return res;
  }


  public static List integerSubrange(int i, int j)
  { List tmp = new Vector(); 
    for (int k = i; k <= j; k++)
    { tmp.add(new Integer(k)); } 
    return tmp;
  }

  public static String subrange(String s, int i, int j)
  { return s.substring(i-1,j); }

  public static List subrange(List l, int i, int j)
  { List tmp = new Vector(); 
    for (int k = i-1; k < j; k++)
    { tmp.add(l.get(k)); } 
    return tmp; 
  }



  public static List prepend(List l, Object ob)
  { List res = new Vector();
    res.add(ob);
    res.addAll(l);
    return res;
  }


  public static List append(List l, Object ob)
  { List res = new Vector();
    res.addAll(l);
    res.add(ob);
    return res;
  }


  public static int count(List l, Object obj)
  { int res = 0; 
    for (int _i = 0; _i < l.size(); _i++)
    { if (obj == l.get(_i)) { res++; } 
      else if (obj != null && obj.equals(l.get(_i))) { res++; } 
    }
    return res; 
  }

  public static int count(String s, String x)
  { int res = 0; 
    if ("".equals(s)) { return res; }
    int ind = s.indexOf(x); 
    if (ind == -1) { return res; }
    String ss = s.substring(ind+1,s.length());
    res++; 
    while (ind >= 0)
    { ind = ss.indexOf(x); 
      if (ind == -1 || ss.equals("")) { return res; }
      res++; 
      ss = ss.substring(ind+1,ss.length());
    } 
    return res;
  }



  public static List characters(String str)
  { char[] _chars = str.toCharArray();
    Vector _res = new Vector();
    for (int i = 0; i < _chars.length; i++)
    { _res.add("" + _chars[i]); }
    return _res;
  }



    public static Object any(List v)
    { if (v.size() == 0) { return null; }
      return v.get(0);
    }


    public static List subcollections(List v)
    { Vector res = new Vector();
      if (v.size() == 0)
      { res.add(new Vector());
        return res;
      }
      if (v.size() == 1)
      { res.add(new Vector());
        res.add(v);
        return res;
      }
      Vector s = new Vector();
      Object x = v.get(0);
      s.addAll(v);
      s.remove(0);
      List scs = subcollections(s);
      res.addAll(scs);
      for (int i = 0; i < scs.size(); i++)
      { Vector sc = (Vector) scs.get(i);
        Vector scc = new Vector();
        scc.add(x);
        scc.addAll(sc);
        res.add(scc);
      }
      return res;
    }


  public static Vector maximalElements(List s, List v)
  { Vector res = new Vector();
    if (s.size() == 0) { return res; }
    Comparable largest = (Comparable) v.get(0);
    res.add(s.get(0));
    
    for (int i = 1; i < s.size(); i++)
    { Comparable next = (Comparable) v.get(i);
      if (largest.compareTo(next) < 0)
      { largest = next;
        res.clear();
        res.add(s.get(i));
      }
      else if (largest.compareTo(next) == 0)
      { res.add(s.get(i)); }
    }
    return res;
  }

  public static Vector minimalElements(List s, List v)
  { Vector res = new Vector();
    if (s.size() == 0) { return res; }
    Comparable smallest = (Comparable) v.get(0);
    res.add(s.get(0));
    
    for (int i = 1; i < s.size(); i++)
    { Comparable next = (Comparable) v.get(i);
      if (next.compareTo(smallest) < 0)
      { smallest = next;
        res.clear();
        res.add(s.get(i));
      }
      else if (smallest.compareTo(next) == 0)
      { res.add(s.get(i)); }
    }
    return res;
  }


  public static List intersectAll(List se)
  { List res = new Vector(); 
    if (se.size() == 0) { return res; }
    res.addAll((List) se.get(0));
    for (int i = 1; i < se.size(); i++)
    { res.retainAll((List) se.get(i)); }
    return res;
  }



  public static List unionAll(List se)
  { List res = new Vector(); 
    for (int i = 0; i < se.size(); i++)
    { List b = (List) se.get(i); 
      for (int j = 0; j < b.size(); j++)
      { Object y = b.get(j); 
        if (y == null || res.contains(y)) { } else { res.add(y); } 
      }
    }
    return res;
  }



    public static List concatenateAll(List a)
    { List res = new Vector();
      for (int i = 0; i < a.size(); i++)
      { List r = (List) a.get(i);
        res.addAll(r); 
      }
      return res;
    }



  public static List insertAt(List l, int ind, Object ob)
  { List res = new Vector();
    for (int i = 0; i < ind-1 && i < l.size(); i++)
    { res.add(l.get(i)); }
    if (ind <= l.size() + 1) { res.add(ob); }
    for (int i = ind-1; i < l.size(); i++)
    { res.add(l.get(i)); }
    return res;
  }
  public static String insertAt(String l, int ind, Object ob)
  { String res = "";
    for (int i = 0; i < ind-1 && i < l.length(); i++)
    { res = res + l.charAt(i); }
    if (ind <= l.length() + 1) { res = res + ob; }
    for (int i = ind-1; i < l.length(); i++)
    { res = res + l.charAt(i); }
    return res;
  }


 public static boolean isInteger(String str)
  { try { Integer.parseInt(str); return true; }
    catch (Exception _e) { return false; }
  }


 public static boolean isReal(String str)
  { try { double d = Double.parseDouble(str); 
          if (Double.isNaN(d)) { return false; }
          return true; }
    catch (Exception _e) { return false; }
  }


 public static boolean isLong(String str)
  { try { Long.parseLong(str); return true; }
    catch (Exception _e) { return false; }
  }


  public static boolean oclIsTypeOf(Object x, String E)
  { try { 
    if (x.getClass() == Class.forName(E))
    { return true; } 
    else 
    { return false; }
    } 
    catch (Exception e) { return false; }
  } 


  public static Vector tokeniseCSV(String line)
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


  public static boolean includesAllMap(Map sup, Map sub)
  { Vector keys = new Vector();
    keys.addAll(sub.keySet());
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (sup.containsKey(key))
      { if (sub.get(key).equals(sup.get(key)))
        {}
        else
        { return false; }
      }
      else 
      { return false; }
    }    
    return true;
  }


  public static boolean excludesAllMap(Map sup, Map sub)
  { Vector keys = new Vector();
    keys.addAll(sub.keySet());
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (sup.containsKey(key))
      { if (sub.get(key).equals(sup.get(key)))
        { return false; }
      }
    }    
    return true;
  }


  public static Map includingMap(Map m, Object src, Object trg) 
  { Map copy = new HashMap();
    copy.putAll(m); 
    copy.put(src,trg);
    return copy;
  } 


  public static Map excludeAllMap(Map m1, Map m2)  
  { // m1 - m2 
    Vector keys = new Vector(); 
    keys.addAll(m1.keySet()); 
    Map res = new HashMap(); 
   
    for (int x = 0; x < keys.size(); x++) 
    { Object key = keys.get(x); 
      if (m2.containsKey(key)) 
      { } 
      else 
      { res.put(key,m1.get(key));  } 
    }     
    return res; 
  } 


  public static Map excludingMapKey(Map m, Object k)   
  { // m - { k |-> m(k) }   
    Map res = new HashMap();  
    res.putAll(m);  
    res.remove(k);  
    return res;  
  }  


  public static Map excludingMapValue(Map m, Object v) 
  { // m - { k |-> v } 
    Vector keys = new Vector();
    keys.addAll(m.keySet());
    Map res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (v.equals(m.get(key)))
      { }
      else
      { res.put(key,m.get(key));  }
    }    
    return res;
  }


  public static Map unionMap(Map m1, Map m2) 
  { Map res = new HashMap();
    res.putAll(m1);
    res.putAll(m2);    
    return res;
  }


  public static Map intersectionMap(Map m1, Map m2) 
  { Vector keys = new Vector();
    keys.addAll(m1.keySet());
    Map res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (m2.containsKey(key) && m1.get(key).equals(m2.get(key)))
      { res.put(key,m1.get(key));  }
    }    
    return res;
  }

  }
}
