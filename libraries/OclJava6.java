package app;

import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public interface SystemTypes
{
  public class Set
  { 


  public static ArrayList copySequence(ArrayList s)
  { ArrayList result = new ArrayList();
    result.addAll(s);
    return result;
  }

  public static HashMap copyMap(Map s)
  { HashMap result = new HashMap();
    result.putAll(s);
    return result;
  }

  public static ArrayList sequenceRange(Object[] arr, int n)
  { ArrayList res = new ArrayList();
    for (int i = 0; i < n; i++) 
    { res.add(arr[i]); } 
    return res; 
  }

  public static int sequenceCompare(List sq1, List sq2)
  { int res = 0;
    for (int i = 0; i < sq1.size() && i < sq2.size(); i++)
    { Object elem1 = sq1.get(i);
      if (((Comparable) elem1).compareTo(sq2.get(i)) < 0)
      { return -1; }
      else if (((Comparable) elem1).compareTo(sq2.get(i)) > 0)
      { return 1; }
    }

    if (sq1.size() > sq2.size())
    { return 1; }
    if (sq2.size() > sq1.size())
    { return -1; }
    return res;
  }


    public static long getTime()
    { java.util.Date d = new java.util.Date();
      return d.getTime();
    }








    public static HashSet addSet(HashSet s, Object x)
    { if (x != null) { s.add(x); }
      return s; }

    public static HashSet addSet(HashSet s, int x)
    { s.add(new Integer(x));
      return s; }

    public static HashSet addSet(HashSet s, long x)
    { s.add(new Long(x));
      return s; }

    public static HashSet addSet(HashSet s, double x)
    { s.add(new Double(x));
      return s; }

    public static HashSet addSet(HashSet s, boolean x)
    { s.add(new Boolean(x));
      return s; }

    public static ArrayList addSequence(ArrayList s, Object x)
    { if (x != null) { s.add(x); }
      return s; }

    public static ArrayList addSequence(ArrayList s, int x)
    { s.add(new Integer(x));
      return s; }

    public static ArrayList addSequence(ArrayList s, long x)
    { s.add(new Long(x));
      return s; }

    public static ArrayList addSequence(ArrayList s, double x)
    { s.add(new Double(x));
      return s; }

    public static ArrayList addSequence(ArrayList s, boolean x)
    { s.add(new Boolean(x));
      return s; }


    public static ArrayList asSequence(Collection c)
    { ArrayList res = new ArrayList();
      res.addAll(c);
      return res;
    }

    public static ArrayList asBag(Collection c)
    { ArrayList res = new ArrayList(); 
      res.addAll(c); 
      Collections.sort(res);
      return res;
    }



    public static HashSet asSet(Collection c)
    { HashSet res = new HashSet();
      res.addAll(c);
      return res;
    }

    public static ArrayList asOrderedSet(Collection c)
    { ArrayList res = new ArrayList();
      for (Object x : c)
      { if (res.contains(x)) { }
        else 
        { res.add(x); }
      } 
      return res; 
    }



  public static Comparable max(Collection l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) Set.any(l); 
    for (Object _o : l)
    { Comparable e = (Comparable) _o;
      if (res.compareTo(e) < 0) { res = e; } }
    return res; }


  public static Comparable min(Collection l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) Set.any(l); 
    for (Object _o : l)
    { Comparable e = (Comparable) _o;
      if (res.compareTo(e) > 0) { res = e; } }
    return res; }


  public static HashSet union(HashSet a, Collection b)
  { HashSet res = new HashSet(); 
    res.addAll(a); res.addAll(b);
    return res; }

  public static ArrayList union(ArrayList a, Collection b)
  { ArrayList res = new ArrayList(); 
    res.addAll(a); res.addAll(b);
    return res; }



  public static HashSet subtract(HashSet a, Collection b)
  { HashSet res = new HashSet(); 
    res.addAll(a);
    res.removeAll(b);
    return res;
  }

  public static ArrayList subtract(ArrayList a, Collection b)
  { ArrayList res = new ArrayList(); 
    res.addAll(a);
    res.removeAll(b);
    return res;
  }

  public static String subtract(String a, String b)
  { String res = ""; 
    for (int i = 0; i < a.length(); i++)
    { if (b.indexOf(a.charAt(i)) < 0) { res = res + a.charAt(i); } }
    return res; }



  public static HashSet intersection(HashSet a, Collection b)
  { HashSet res = new HashSet(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }

  public static ArrayList intersection(ArrayList a, Collection b)
  { ArrayList res = new ArrayList(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }



  public static HashSet symmetricDifference(Collection a, Collection b)
  { HashSet res = new HashSet();
    for (Object _a : a)
    { if (b.contains(_a)) { }
      else { res.add(_a); }
    }
    for (Object _b : b)
    { if (a.contains(_b)) { }
      else { res.add(_b); }
    }
    return res;
  }



  public static boolean isUnique(Collection evals)
  { HashSet vals = new HashSet(); 
    for (Object ob : evals)
    { if (vals.contains(ob)) { return false; }
      vals.add(ob);
    }
    return true;
  }


  public static long gcd(long xx, long yy)
  { long x = Math.abs(xx);
    long y = Math.abs(yy);
    while (x != 0 && y != 0)
    { long z = y; 
      y = x % y; 
      x = z;
    } 
    if (y == 0)
    { return x; }
    if (x == 0)
    { return y; }
    return 0;
  } 


  public static int sumint(Collection a)
  { int sum = 0; 
    for (Object _o : a)
    { Integer x = (Integer) _o; 
      if (x != null) { sum += x.intValue(); }
    } 
    return sum; }

  public static double sumdouble(Collection a)
  { double sum = 0.0; 
    for (Object _o : a)
    { Double x = (Double) _o; 
      if (x != null) { sum += x.doubleValue(); }
    } 
    return sum; }

  public static long sumlong(Collection a)
  { long sum = 0; 
    for (Object _o : a)
    { Long x = (Long) _o; 
      if (x != null) { sum += x.longValue(); }
    } 
    return sum; }

  public static String sumString(Collection a)
  { String sum = ""; 
    for (Object x : a)
    { sum = sum + x; }
    return sum;  }



  public static int prdint(Collection a)
  { int prd = 1; 
    for (Object _o : a)
    { Integer x = (Integer) _o; 
      if (x != null) { prd *= x.intValue(); }
    } 
    return prd; }

  public static double prddouble(Collection a)
  { double prd = 1; 
    for (Object _o : a)
    { Double x = (Double) _o; 
      if (x != null) { prd *= x.doubleValue(); }
    } 
    return prd; }

  public static long prdlong(Collection a)
  { long prd = 1; 
    for (Object _o : a)
    { Long x = (Long) _o; 
      if (x != null) { prd *= x.longValue(); }
    } 
    return prd; }



  public static ArrayList concatenate(Collection a, Collection b)
  { ArrayList res = new ArrayList(); 
    res.addAll(a); 
    res.addAll(b); 
    return res;
  }




  public static ArrayList reverse(Collection a)
  { ArrayList res = new ArrayList(); 
    res.addAll(a); 
    Collections.reverse(res); 
    return res; }

  public static String reverse(String a)
  { String res = ""; 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a.charAt(i); } 
    return res; }



  public static ArrayList front(ArrayList a)
  { ArrayList res = new ArrayList(); 
    for (int i = 0; i < a.size() - 1; i++)
    { res.add(a.get(i)); } 
    return res; }


  public static ArrayList tail(ArrayList a)
  { ArrayList res = new ArrayList(); 
    for (int i = 1; i < a.size(); i++)
    { res.add(a.get(i)); } 
    return res; }


  public static ArrayList sort(Collection a)
  { ArrayList res = new ArrayList();
    res.addAll(a);
    Collections.sort(res);
    return res;
  }



  public static ArrayList sortedBy(final ArrayList a, ArrayList f)
  { int i = a.size()-1;
    java.util.Map f_map = new java.util.HashMap();
    for (int j = 0; j < a.size(); j++)
    { f_map.put(a.get(j), f.get(j)); }
    return mergeSort(a,f_map,0,i);
  }

  static ArrayList mergeSort(final ArrayList a, java.util.Map f, int ind1, int ind2)
  { ArrayList res = new ArrayList();
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
    ArrayList a1;
    ArrayList a2;
    if (mid == ind1)
    { a1 = new ArrayList();
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


  public static ArrayList integerSubrange(int i, int j)
  { ArrayList tmp = new ArrayList(); 
    for (int k = i; k <= j; k++)
    { tmp.add(new Integer(k)); } 
    return tmp;
  }

  public static String subrange(String s, int i, int j)
  { return s.substring(i-1,j); }

  public static ArrayList subrange(ArrayList l, int i, int j)
  { ArrayList tmp = new ArrayList(); 
    for (int k = i-1; k < j; k++)
    { tmp.add(l.get(k)); } 
    return tmp; 
  }



  public static ArrayList prepend(ArrayList l, Object ob)
  { ArrayList res = new ArrayList();
    res.add(ob);
    res.addAll(l);
    return res;
  }


  public static ArrayList append(ArrayList l, Object ob)
  { ArrayList res = new ArrayList();
    res.addAll(l);
    res.add(ob);
    return res;
  }


  public static int count(Collection l, Object obj)
  { int res = 0; 
    for (Object _o : l)
    { if (obj == _o) { res++; } 
      else if (obj != null && obj.equals(_o)) { res++; } 
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



  public static ArrayList characters(String str)
  { char[] _chars = str.toCharArray();
    ArrayList _res = new ArrayList();
    for (int i = 0; i < _chars.length; i++)
    { _res.add("" + _chars[i]); }
    return _res;
  }



    public static Object any(Collection v)
    { for (Object o : v) { return o; }
      return null;
    }


    public static Object first(Collection v)
    { for (Object o : v) { return o; }
      return null;
    }


    public static Object last(ArrayList v)
    { if (v.size() == 0) { return null; }
      return v.get(v.size() - 1);
    }



    public static ArrayList subcollections(ArrayList v)
    { ArrayList res = new ArrayList();
      if (v.size() == 0) { res.add(new ArrayList()); return res; }
      if (v.size() == 1) { res.add(new ArrayList()); res.add(v); return res;
       }
      ArrayList s = new ArrayList();
      Object x = v.get(0);
      s.addAll(v);
      s.remove(0);
      ArrayList scs = subcollections(s);
      res.addAll(scs);
      for (int i = 0; i < scs.size(); i++)
      { ArrayList sc = (ArrayList) scs.get(i);
        ArrayList scc = new ArrayList();
        scc.add(x); scc.addAll(sc); res.add(scc); 
      }
      return res;
    }

    public static HashSet subcollections(HashSet v)
    { HashSet res = new HashSet();
      if (v.size() == 0) { res.add(new HashSet()); return res; }
      if (v.size() == 1) { res.add(new HashSet()); res.add(v); return res;
       }
      HashSet s = new HashSet();
      Object x = null; int _i = 0;
      for (Object _o : v)
      { if (_i == 0) { x = _o; _i++; }
         else { s.add(_o); }
      }
      HashSet scs = subcollections(s);
      res.addAll(scs);
      for (Object _obj : scs)
      { HashSet sc = (HashSet) _obj;
        HashSet scc = new HashSet();
        scc.add(x); scc.addAll(sc); res.add(scc); 
      }
      return res;
    }


  public static ArrayList maximalElements(ArrayList s, ArrayList v)
  { ArrayList res = new ArrayList();
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

  public static ArrayList minimalElements(ArrayList s, ArrayList v)
  { ArrayList res = new ArrayList();
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


  public static HashSet intersectAll(Collection se)
  { HashSet res = new HashSet(); 
    if (se.size() == 0) { return res; }
    res.addAll((Collection) Set.any(se));
    for (Object _o : se)
    { res.retainAll((Collection) _o); }
    return res;
  }



  public static HashSet unionAll(Collection se)
  { HashSet res = new HashSet(); 
    for (Object _o : se)
    { Collection b = (Collection) _o; 
      res.addAll(b);
    }
    return res;
  }



    public static ArrayList concatenateAll(List a)
    { ArrayList res = new ArrayList();
      for (int i = 0; i < a.size(); i++)
      { Collection r = (Collection) a.get(i);
        res.addAll(r); 
      }
      return res;
    }



  public static ArrayList insertAt(ArrayList l, int ind, Object ob)
  { ArrayList res = new ArrayList();
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


  public static ArrayList removeAt(ArrayList l, int ind)
  { ArrayList res = new ArrayList();
    res.addAll(l); 
    if (ind <= res.size() && ind >= 1)
    { res.remove(ind - 1); } 
    return res;
  }

  public static String removeAt(String ss, int ind)
  { StringBuffer sb = new StringBuffer(ss); 
    if (ind <= ss.length() && ind >= 1)
    { sb.deleteCharAt(ind - 1); } 
    return sb.toString();
  }

  public static ArrayList removeFirst(ArrayList l, Object x)
  { ArrayList res = new ArrayList();
    res.addAll(l); 
    res.remove(x);
    return res;
  }

  public static ArrayList setAt(ArrayList l, int ind, Object val)
  { ArrayList res = new ArrayList();
    res.addAll(l); 
    if (ind <= res.size() && ind >= 1)
    { res.set(ind - 1,val); } 
    return res;
  }

  public static String setAt(String ss, int ind, Object val)
  { String res = ss;
    if (ind <= res.length() && ind >= 1)
    { res = ss.substring(0,ind-1); 
      res = res + val + ss.substring(ind);
    } 
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


 public static String byte2char(int b)
  { try { byte[] bb = {(byte) b}; 
      return new String(bb); }
    catch (Exception _e)
    { return ""; }
  }

  public static int char2byte(String s)
  { if (s == null || s.length() == 0)
    { return -1; } 
    return (int) s.charAt(0);
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


  public static String before(String s, String sep)
  { if (sep.length() == 0) { return s; }
    int ind = s.indexOf(sep);
    if (ind < 0) { return s; }
    return s.substring(0,ind); 
  }


  public static String after(String s, String sep)
  { int ind = s.indexOf(sep);
    int seplength = sep.length();
    if (ind < 0) { return ""; }
    if (seplength == 0) { return ""; }
    return s.substring(ind + seplength, s.length()); 
  }


  public static boolean hasMatch(String str, String regex)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str); 
    if (matcher.find())
    { return true; }
    return false;
  }



  public static ArrayList allMatches(String str, String regex)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    ArrayList res = new ArrayList();
    while (matcher.find())
    { res.add(matcher.group() + ""); }
    return res; 
  }


  public static String firstMatch(String str, String regex)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    String res = null;
    if (matcher.find())
    { res = matcher.group() + ""; }
    return res; 
  }


  public static ArrayList split(String str, String delim)
  { String[] splits = str.split(delim);
    ArrayList res = new ArrayList();
    for (int j = 0; j < splits.length; j++)
    { if (splits[j].length() > 0)
      { res.add(splits[j]); }
    }
    return res;
  }


  public static String replace(String str, String delim, String rep)
  { String result = "";
    String s = str + "";
    int i = (s.indexOf(delim) + 1);
    if (i == 0)
    { return s; }
    
    int sublength = delim.length();
    if (sublength == 0)
    { return s; }
    
    while (i > 0)
    { result = result + Set.subrange(s,1,i - 1) + rep;
      s = Set.subrange(s,i + delim.length(),s.length());
      i = (s.indexOf(delim) + 1);
    }
    result = result + s;
    return result;
  }


  public static String replaceAll(String str, String regex, String rep)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    return matcher.replaceAll(rep);
  }


  public static String replaceFirstMatch(String str, String regex, String rep)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    return matcher.replaceFirst(rep);
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


  public static HashMap includingMap(Map m, Object src, Object trg) 
  { HashMap copy = new HashMap();
    copy.putAll(m); 
    copy.put(src,trg);
    return copy;
  } 


  public static HashMap excludeAllMap(Map m1, Map m2)  
  { // m1 - m2 
    Vector keys = new Vector(); 
    keys.addAll(m1.keySet()); 
    HashMap res = new HashMap(); 
   
    for (int x = 0; x < keys.size(); x++) 
    { Object key = keys.get(x); 
      if (m2.containsKey(key)) 
      { } 
      else 
      { res.put(key,m1.get(key));  } 
    }     
    return res; 
  } 


  public static HashMap excludingMapKey(Map m, Object k)   
  { // m - { k |-> m(k) }   
    HashMap res = new HashMap();  
    res.putAll(m);  
    res.remove(k);  
    return res;  
  }  


  public static HashMap excludingMapValue(Map m, Object v) 
  { // m - { k |-> v } 
    Vector keys = new Vector();
    keys.addAll(m.keySet());
    HashMap res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (v.equals(m.get(key)))
      { }
      else
      { res.put(key,m.get(key));  }
    }    
    return res;
  }


  public static HashMap unionMap(Map m1, Map m2) 
  { HashMap res = new HashMap();
    res.putAll(m1);
    res.putAll(m2);    
    return res;
  }


  public static HashMap intersectionMap(Map m1, Map m2) 
  { Vector keys = new Vector();
    keys.addAll(m1.keySet());
    HashMap res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (m2.containsKey(key) && m1.get(key) != null && m1.get(key).equals(m2.get(key)))
      { res.put(key,m1.get(key));  }
    }    
    return res;
  }

  public static HashMap restrictMap(Map m1, Vector ks) 
  { Vector keys = new Vector();
    keys.addAll(m1.keySet());
    HashMap res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (ks.contains(key))
      { res.put(key,m1.get(key)); }
    }    
    return res;
  }

  public static HashMap antirestrictMap(Map m1, Vector ks) 
  { Vector keys = new Vector();
    keys.addAll(m1.keySet());
    HashMap res = new HashMap();
  
    for (int x = 0; x < keys.size(); x++)
    { Object key = keys.get(x);
      if (ks.contains(key)) { }
      else { res.put(key,m1.get(key)); }
    }    
    return res;
  }

  public static boolean includesKey(Map m, Object key) 
  { Object val = m.get(key); 
    if (val == null) { return false; }
    return true;
  }

  public static boolean excludesKey(Map m, Object key) 
  { Object val = m.get(key); 
    if (val == null) { return true; }
    return false;
  }

  public static boolean includesValue(Map m, Object val) 
  { Vector keys = new Vector();
    keys.addAll(m.keySet());
    for (int x = 0; x < keys.size(); x++)
    { Object v = m.get(x);
      if (v != null && v.equals(val))
      { return true;  }
    }    
    return false;
  }

  public static boolean excludesValue(Map m, Object val) 
  { Vector keys = new Vector();
    keys.addAll(m.keySet());
    for (int x = 0; x < keys.size(); x++)
    { Object v = m.get(x);
      if (v != null && v.equals(val))
      { return false;  }
    }    
    return true;
  }


  }
}
