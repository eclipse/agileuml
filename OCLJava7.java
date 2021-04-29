import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;


public interface SystemTypes
{
  public class Ocl
  { 








    public static <T> HashSet<T> addSet(HashSet<T> s, T x)
    { if (x != null) { s.add(x); }
      return s; }

    public static HashSet<Integer> addSet(HashSet<Integer> s, int x)
    { s.add(new Integer(x));
      return s; }

    public static HashSet<Double> addSet(HashSet<Double> s, double x)
    { s.add(new Double(x));
      return s; }

    public static HashSet<Long> addSet(HashSet<Long> s, long x)
    { s.add(new Long(x));
      return s; }

    public static HashSet<Boolean> addSet(HashSet<Boolean> s, boolean x)
    { s.add(new Boolean(x));
      return s; }

    public static <T> ArrayList<T> addSequence(ArrayList<T> s, T x)
    { if (x != null) { s.add(x); }
      return s; }

    public static ArrayList<Integer> addSequence(ArrayList<Integer> s, int x)
    { s.add(new Integer(x));
      return s; }

    public static ArrayList<Double> addSequence(ArrayList<Double> s, double x)
    { s.add(new Double(x));
      return s; }

    public static ArrayList<Long> addSequence(ArrayList<Long> s, long x)
    { s.add(new Long(x));
      return s; }

    public static ArrayList<Boolean> addSequence(ArrayList<Boolean> s, boolean x)
    { s.add(new Boolean(x));
      return s; }

    public static <T> ArrayList<T> asSequence(Collection<T> c)
    { ArrayList<T> res = new ArrayList<T>(); res.addAll(c); return res; }

    public static <T> HashSet<T> asSet(Collection<T> c)
    { HashSet res = new HashSet<T>(); res.addAll(c); return res; }


  public static <T> HashSet<T> union(HashSet<T> a, Collection<T> b)
  { HashSet<T> res = new HashSet<T>(); 
    res.addAll(a); res.addAll(b);
    return res; }

  public static <T> TreeSet<T> union(TreeSet<T> a, Collection<T> b)
  { TreeSet<T> res = new TreeSet<T>(); 
    res.addAll(a); res.addAll(b);
    return res; }

  public static <T> HashSet<T> union(ArrayList<T> a, Set<T> b)
  { HashSet<T> res = new HashSet<T>(); 
    res.addAll(a); res.addAll(b);
    return res; }

  public static <T> ArrayList<T> union(ArrayList<T> a, ArrayList<T> b)
  { ArrayList<T> res = new ArrayList<T>(); 
    res.addAll(a); res.addAll(b);
    return res; }


  public static <T> HashSet<T> subtract(HashSet<T> a, Collection<T> b)
  { HashSet<T> res = new HashSet<T>(); 
    res.addAll(a);
    res.removeAll(b);
    return res; }

  public static <T> TreeSet<T> subtract(TreeSet<T> a, Collection<T> b)
  { TreeSet<T> res = new TreeSet<T>(); 
    res.addAll(a);
    res.removeAll(b);
    return res; }

  public static <T> ArrayList<T> subtract(ArrayList<T> a, Collection<T> b)
  { ArrayList<T> res = new ArrayList<T>(); 
    res.addAll(a);
    res.removeAll(b);
    return res; }

  public static String subtract(String a, String b)
  { String res = ""; 
    for (int i = 0; i < a.length(); i++)
    { if (b.indexOf(a.charAt(i)) < 0) { res = res + a.charAt(i); } }
    return res; }



  public static <T> HashSet<T> intersection(HashSet<T> a, Collection<T> b)
  { HashSet<T> res = new HashSet<T>(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }

  public static <T> TreeSet<T> intersection(TreeSet<T> a, Collection<T> b)
  { TreeSet<T> res = new TreeSet<T>(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }

  public static <T> ArrayList<T> intersection(ArrayList<T> a, Collection<T> b)
  { ArrayList<T> res = new ArrayList<T>(); 
    res.addAll(a);
    res.retainAll(b);
    return res; }



  public static <T> Set<T> symmetricDifference(Collection<T> a, Collection<T> b)
  { Set<T> res = new HashSet<T>();
    for (T _a : a)
    { if (b.contains(_a)) { }
      else { res.add(_a); }
    }
    for (T _b : b)
    { if (a.contains(_b)) { }
      else { res.add(_b); }
    }
    return res;
  }



  public static <T> boolean isUnique(Collection<T> evals)
  { HashSet<T> vals = new HashSet<T>(); 
    for (T ob : evals)
    { if (vals.contains(ob)) { return false; }
      vals.add(ob);
    }
    return true;
  }


  public static int sumint(Collection<Integer> a)
  { int sum = 0; 
    for (Integer x : a)
    { if (x != null) { sum += x.intValue(); }
    } 
    return sum; }

  public static double sumdouble(Collection<Double> a)
  { double sum = 0.0; 
    for (Double x : a)
    { if (x != null) { sum += x.doubleValue(); }
    } 
    return sum; }

  public static long sumlong(Collection<Long> a)
  { long sum = 0; 
    for (Long x : a)
    { if (x != null) { sum += x.longValue(); }
    } 
    return sum; }

  public static String sumString(Collection<String> a)
  { String sum = ""; 
    for (String x : a)
    { sum = sum + x; }
    return sum;  }



  public static int prdint(Collection<Integer> a)
  { int prd = 1; 
    for (Integer x : a)
    { if (x != null) { prd *= x.intValue(); }
    } 
    return prd; }

  public static double prddouble(Collection<Double> a)
  { double prd = 1; 
    for (Double x : a)
    { if (x != null) { prd *= x.doubleValue(); }
    } 
    return prd; }

  public static long prdlong(Collection<Long> a)
  { long prd = 1; 
    for (Long x : a)
    { if (x != null) { prd *= x.longValue(); }
    } 
    return prd; }



  public static <T> ArrayList<T> concatenate(Collection<T> a, Collection<T> b)
  { ArrayList<T> res = new ArrayList<T>(); 
    res.addAll(a); 
    res.addAll(b); 
    return res; }




  public static ArrayList reverse(Collection a)
  { ArrayList res = new ArrayList(); 
    res.addAll(a); Collections.reverse(res); 
    return res; }

  public static String reverse(String a)
  { String res = ""; 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a.charAt(i); } 
    return res; }



  public static <T> ArrayList<T> front(ArrayList<T> a)
  { ArrayList<T> res = new ArrayList<T>(); 
    for (int i = 0; i < a.size() - 1; i++)
    { res.add(a.get(i)); } 
    return res; }


  public static <T> ArrayList<T> tail(ArrayList<T> a)
  { ArrayList<T> res = new ArrayList<T>(); 
    for (int i = 1; i < a.size(); i++)
    { res.add(a.get(i)); } 
    return res; }


  public static ArrayList sort(Collection a)
  { ArrayList res = new ArrayList();
    res.addAll(a);
    Collections.sort(res);
    return res;
  }



  public static <T> ArrayList<T> sortedBy(final ArrayList<T> a, ArrayList<? extends Comparable> f)
  { int i = a.size()-1;
    java.util.Map<T,Comparable> f_map = new java.util.HashMap<T,Comparable>();
    for (int j = 0; j < a.size(); j++)
    { f_map.put(a.get(j), (Comparable) f.get(j)); }
    return mergeSort(a,f_map,0,i);
  }

  static <T> ArrayList<T> mergeSort(final ArrayList<T> a, java.util.Map<T,Comparable> f, int ind1, int ind2)
  { ArrayList<T> res = new ArrayList<T>();
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
    ArrayList<T> a1;
    ArrayList<T> a2;
    if (mid == ind1)
    { a1 = new ArrayList<T>();
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


  public static ArrayList<Integer> integerSubrange(int i, int j)
  { ArrayList<Integer> tmp = new ArrayList<Integer>(); 
    for (int k = i; k <= j; k++)
    { tmp.add(new Integer(k)); } 
    return tmp;
  }

  public static String subrange(String s, int i, int j)
  { return s.substring(i-1,j); }

  public static <T> ArrayList<T> subrange(ArrayList<T> l, int i, int j)
  { ArrayList<T> tmp = new ArrayList<T>(); 
    for (int k = i-1; k < j; k++)
    { tmp.add(l.get(k)); } 
    return tmp; 
  }



  public static <T> ArrayList<T> prepend(List<T> l, T ob)
  { ArrayList<T> res = new ArrayList<T>();
    res.add(ob);
    res.addAll(l);
    return res;
  }


  public static <T> ArrayList<T> append(List<T> l, T ob)
  { ArrayList<T> res = new ArrayList<T>();
    res.addAll(l);
    res.add(ob);
    return res;
  }


  public static <T> int count(Collection<T> l, T obj)
  { return Collections.frequency(l,obj); }

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



  public static List<String> characters(String str)
  { char[] _chars = str.toCharArray();
    List<String> _res = new ArrayList<String>();
    for (int i = 0; i < _chars.length; i++)
    { _res.add("" + _chars[i]); }
    return _res;
  }



    public static <T> T any(Collection<T> v)
    { for (T o : v) { return o; }
      return null;
    }


    public static <T> T first(Collection<T> v)
    { for (T o : v) { return o; }
      return null;
    }


    public static <T> T last(List<T> v)
    { if (v.size() == 0) { return null; }
      return v.get(v.size() - 1);
    }



    public static <T> ArrayList<List<T>> subcollections(ArrayList<T> v)
    { ArrayList<List<T>> res = new ArrayList<List<T>>();
      if (v.size() == 0)
      { res.add(new ArrayList<T>()); return res; }
      if (v.size() == 1)
      { res.add(new ArrayList<T>()); res.add(v); return res;
       }
      ArrayList<T> s = new ArrayList<T>();
      T x = v.get(0);
      s.addAll(v);
      s.remove(0);
      ArrayList<List<T>> scs = subcollections(s);
      res.addAll(scs);
      for (int i = 0; i < scs.size(); i++)
      { ArrayList<T> sc = (ArrayList<T>) scs.get(i);
        ArrayList<T> scc = new ArrayList<T>();
        scc.add(x); scc.addAll(sc); res.add(scc); 
      }
      return res;
    }

    public static <T> HashSet<Set<T>> subcollections(HashSet<T> v)
    { HashSet<Set<T>> res = new HashSet<Set<T>>();
      if (v.size() == 0) { res.add(new HashSet<T>()); return res; }
      if (v.size() == 1) { res.add(new HashSet<T>()); res.add(v); return res;
       }
      HashSet<T> s = new HashSet<T>();
      T x = null; int _i = 0;
      for (T _o : v)
      { if (_i == 0) { x = _o; _i++; }
         else { s.add(_o); }
      }
      HashSet<Set<T>> scs = subcollections(s);
      res.addAll(scs);
      for (Set<T> _obj : scs)
      { HashSet<T> sc = (HashSet<T>) _obj;
        HashSet<T> scc = new HashSet<T>();
        scc.add(x); scc.addAll(sc); res.add(scc); 
      }
      return res;
    }


  public static <T extends Comparable<T>> ArrayList<T> maximalElements(List<T> s, List<Comparable> v)
  { ArrayList<T> res = new ArrayList<T>();
    if (s.size() == 0) { return res; }
    Comparable largest = v.get(0);
    res.add(s.get(0));
    
    for (int i = 1; i < s.size(); i++)
    { Comparable next = v.get(i);
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

  public static <T> ArrayList<T> minimalElements(ArrayList<T> s, ArrayList<Comparable> v)
  { ArrayList<T> res = new ArrayList<T>();
    if (s.size() == 0) { return res; }
    Comparable smallest = v.get(0);
    res.add(s.get(0));
    
    for (int i = 1; i < s.size(); i++)
    { Comparable next = v.get(i);
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


  public static <T> HashSet<T> intersectAll(Collection<Collection<T>> se)
  { HashSet<T> res = new HashSet<T>(); 
    if (se.size() == 0) { return res; }
    res.addAll((Collection<T>) Ocl.any(se));
    for (Collection<T> _o : se)
    { res.retainAll(_o); }
    return res;
  }



  public static HashSet unionAll(Collection se)
  { HashSet res = new HashSet(); 
    for (Object _o : se)
    { res.addAll((Collection) _o); }
    return res;
  }



    public static ArrayList concatenateAll(ArrayList a)
    { ArrayList res = new ArrayList();
      for (int i = 0; i < a.size(); i++)
      { Collection r = (Collection) a.get(i);
        res.addAll(r); 
      }
      return res;
    }



  public static <T> ArrayList<T> insertAt(List<T> l, int ind, T ob)
  { ArrayList<T> res = new ArrayList<T>();
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



  public static ArrayList<String> allMatches(String str, String regex)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    ArrayList<String> res = new ArrayList<String>();
    while (matcher.find())
    { res.add(matcher.group() + ""); }
    return res; 
  }


  public static ArrayList<String> split(String str, String delim)
  { String[] splits = str.split(delim);
    ArrayList<String> res = new ArrayList<String>();
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
    { result = result + Ocl.subrange(s,1,i - 1) + rep;
      s = Ocl.subrange(s,i + delim.length(),s.length());
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


  public static <D,R> boolean includesAllMap(Map<D,R> sup, Map<D,R> sub)
  { Set<D> keys = sub.keySet();
  
    for (D key : keys)
    { if (sup.containsKey(key))
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


  public static <D,R> boolean excludesAllMap(Map<D,R> sup, Map<D,R> sub)
  { Set<D> keys = sub.keySet();
  
    for (D key : keys)
    { if (sup.containsKey(key))
      { if (sub.get(key).equals(sup.get(key)))
        { return false; }
      }
    }    
    return true;
  }


  public static <D,R> HashMap<D,R> includingMap(Map<D,R> m, D src, R trg)
  { HashMap<D,R> copy = new HashMap<D,R>();
    copy.putAll(m); 
    copy.put(src,trg);
    return copy;
  } 


  public static <D,R> HashMap<D,R> excludeAllMap(Map<D,R> m1, Map m2)
  { // m1 - m2
    HashMap<D,R> res = new HashMap<D,R>();
    Set<D> keys = m1.keySet(); 
  
    for (D key : keys)
    { if (m2.containsKey(key))
      { }
      else
      { res.put(key,m1.get(key));  }
    }    
    return res;
  }


  public static <D,R> HashMap<D,R> excludingMapKey(Map<D,R> m, D k)
  { // m - { k |-> m(k) } 
    HashMap<D,R> res = new HashMap<D,R>();
    res.putAll(m);
    res.remove(k);
    return res;
  }


  public static <D,R> HashMap<D,R> excludingMapValue(Map<D,R> m, R v)
  { // m - { k |-> v }
    HashMap<D,R> res = new HashMap<D,R>();
    Set<D> keys = m.keySet(); 
    
    for (D key : keys)
    { if (v.equals(m.get(key)))
      { }
      else
      { res.put(key,m.get(key));  }
    }    
    return res;
  }


  public static <D,R> HashMap<D,R> unionMap(Map<D,R> m1, Map<D,R> m2)
  { HashMap<D,R> res = new HashMap<D,R>();
    res.putAll(m1);
    res.putAll(m2);    
    return res;
  }


  public static <D,R> HashMap<D,R> intersectionMap(Map<D,R> m1, Map m2)
  { HashMap<D,R> res = new HashMap<D,R>();
    Set<D> keys = m1.keySet(); 
  
    for (D key : keys)
    { if (m2.containsKey(key) && m1.get(key) != null && m1.get(key).equals(m2.get(key)))
      { res.put(key,m1.get(key));  }
    }    
    return res;
  }

  public static <D,R> HashMap<D,R> restrictMap(Map<D,R> m1, Set<D> ks) 
  { Set<D> keys = new HashSet<D>();
    keys.addAll(m1.keySet());
    HashMap<D,R> res = new HashMap<D,R>();
  
    for (D key : keys)
    { if (ks.contains(key))
      { res.put(key,m1.get(key)); }
    }    
    return res;
  }

  }
}
