package uml2py3;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import java.util.function.Predicate;
import java.util.function.Function;


public interface SystemTypes
{
  public static final int value = 0;
  public static final int attribute = 1;
  public static final int role = 2;
  public static final int variable = 3;
  public static final int constant = 4;
  public static final int function = 5;
  public static final int queryop = 6;
  public static final int operation = 7;
  public static final int classid = 8;


  public static int parseUMLKind(String _x) {
    if ("value".equals(_x)) { return 0; }
    if ("attribute".equals(_x)) { return 1; }
    if ("role".equals(_x)) { return 2; }
    if ("variable".equals(_x)) { return 3; }
    if ("constant".equals(_x)) { return 4; }
    if ("function".equals(_x)) { return 5; }
    if ("queryop".equals(_x)) { return 6; }
    if ("operation".equals(_x)) { return 7; }
    if ("classid".equals(_x)) { return 8; }
    return 0;
  }

  public class Set
  { private Vector elements = new Vector();

    public static long now = 0;

    public static long getTime()
    { java.util.Date d = new java.util.Date();
      return d.getTime();
    }

    public static double roundN(double x, int n)
    { if (n == 0) 
      { return Math.round(x); }
      double y = x*Math.pow(10,n); 
      return Math.round(y)/Math.pow(10,n);
    }

    public static double truncateN(double x, int n)
    { if (n < 0) 
      { return (int) x; }
      double y = x*Math.pow(10,n); 
      return ((int) y)/Math.pow(10,n);
    }


  public static List select_3(List _l,List oonames)
  { // Implements: generalization.general.allOperations()->select(op | op.name /: oonames)
    List _results_3 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation op = (Operation) _l.get(_i);
      if (!(oonames.contains(op.getname())))
      { _results_3.add(op); }
    }
    return _results_3;
  }

  public static HashMap select_3(Map _l,List oonames)
  { // Implements: generalization.general.allOperations()->select(op | op.name /: oonames)
    HashMap _results_3 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation op = (Operation) _l.get(_i);
      if (!(oonames.contains(op.getname())))
      { _results_3.put(_i, op); }
    }
    return _results_3;
  }


  public static List select_7(List _l)
  { // Implements: allProperties()->select(a | a.isStatic = false)
    List _results_7 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property a = (Property) _l.get(_i);
      if (a.getisStatic() == false)
      { _results_7.add(a); }
    }
    return _results_7;
  }

  public static HashMap select_7(Map _l)
  { // Implements: allProperties()->select(a | a.isStatic = false)
    HashMap _results_7 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property a = (Property) _l.get(_i);
      if (a.getisStatic() == false)
      { _results_7.put(_i, a); }
    }
    return _results_7;
  }


  public static List select_10(List _l)
  { // Implements: ownedOperation->select(x | x.isStatic & x.isCached & x.parameters.size = 1)
    List _results_10 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation x = (Operation) _l.get(_i);
      if (x.getisStatic() && x.getisCached() && x.getparameters().size() == 1)
      { _results_10.add(x); }
    }
    return _results_10;
  }

  public static HashMap select_10(Map _l)
  { // Implements: ownedOperation->select(x | x.isStatic & x.isCached & x.parameters.size = 1)
    HashMap _results_10 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation x = (Operation) _l.get(_i);
      if (x.getisStatic() && x.getisCached() && x.getparameters().size() == 1)
      { _results_10.put(_i, x); }
    }
    return _results_10;
  }


  public static List select_12(List _l)
  { // Implements: ownedOperation->select(x | x.isStatic = false & x.isCached & x.parameters.size = 1)
    List _results_12 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation x = (Operation) _l.get(_i);
      if (x.getisStatic() == false && x.getisCached() && x.getparameters().size() == 1)
      { _results_12.add(x); }
    }
    return _results_12;
  }

  public static HashMap select_12(Map _l)
  { // Implements: ownedOperation->select(x | x.isStatic = false & x.isCached & x.parameters.size = 1)
    HashMap _results_12 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation x = (Operation) _l.get(_i);
      if (x.getisStatic() == false && x.getisCached() && x.getparameters().size() == 1)
      { _results_12.put(_i, x); }
    }
    return _results_12;
  }


  public static List select_14(List _l)
  { // Implements: allProperties()->select(x | x.isStatic)
    List _results_14 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property x = (Property) _l.get(_i);
      if (x.getisStatic())
      { _results_14.add(x); }
    }
    return _results_14;
  }

  public static HashMap select_14(Map _l)
  { // Implements: allProperties()->select(x | x.isStatic)
    HashMap _results_14 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property x = (Property) _l.get(_i);
      if (x.getisStatic())
      { _results_14.put(_i, x); }
    }
    return _results_14;
  }


  public static List select_17(List _l)
  { // Implements: allProperties()->select(property_17_xx | isUnique)
    List _results_17 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property property_17_xx = (Property) _l.get(_i);
      if (property_17_xx.getisUnique())
      { _results_17.add(property_17_xx); }
    }
    return _results_17;
  }

  public static HashMap select_17(Map _l)
  { // Implements: allProperties()->select(property_17_xx | isUnique)
    HashMap _results_17 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property property_17_xx = (Property) _l.get(_i);
      if (property_17_xx.getisUnique())
      { _results_17.put(_i, property_17_xx); }
    }
    return _results_17;
  }




  public static boolean exists_4(List _l, String d)
  { // Implements: allProperties()->exists(p|p.name = d & p.lower = 1 & p.upper = 1)
    for (int _i = 0; _i < _l.size(); _i++)
    { Property p = (Property) _l.get(_i);
      if (((String) p.getname()).equals(d) && p.getlower() == 1 && p.getupper() == 1) { return true; }
    }
    return false;
  }

  public static boolean exists_5(List _l, String d)
  { // Implements: allProperties()->exists(p|p.name = d & ( p.lower /= 1 or p.upper /= 1 ) & p.isOrdered = false)
    for (int _i = 0; _i < _l.size(); _i++)
    { Property p = (Property) _l.get(_i);
      if (((String) p.getname()).equals(d) && ( p.getlower() != 1 || p.getupper() != 1 ) && p.getisOrdered() == false) { return true; }
    }
    return false;
  }

  public static boolean exists_6(List _l, String d)
  { // Implements: allProperties()->exists(p|p.name = d & ( p.lower /= 1 or p.upper /= 1 ) & p.isOrdered = true)
    for (int _i = 0; _i < _l.size(); _i++)
    { Property p = (Property) _l.get(_i);
      if (((String) p.getname()).equals(d) && ( p.getlower() != 1 || p.getupper() != 1 ) && p.getisOrdered() == true) { return true; }
    }
    return false;
  }

  public static boolean exists_9(List _l)
  { // Implements: superclass->exists(c|c.isInterface = false)
    for (int _i = 0; _i < _l.size(); _i++)
    { Entity c = (Entity) _l.get(_i);
      if (c.getisInterface() == false) { return true; }
    }
    return false;
  }

  public static boolean exists_16(List _l)
  { // Implements: allProperties()->exists(k|k.isUnique)
    for (int _i = 0; _i < _l.size(); _i++)
    { Property k = (Property) _l.get(_i);
      if (k.getisUnique()) { return true; }
    }
    return false;
  }




  public static List collect_0(List _l,Enumeration enumerationx)
  { // implements: ownedLiteral->collect( lt | "  " + lt.name + " = " + ownedLiteral->indexOf(lt) + "\n" )
    List _results_0 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { EnumerationLiteral lt = (EnumerationLiteral) _l.get(_i);
      String collect_x = "  " + lt.getname() + " = " + ( enumerationx.getownedLiteral().indexOf(lt) + 1 ) + "\n";
      if (collect_x != null) { _results_0.add(collect_x); }
    }
    return _results_0;
  }

  public static HashMap collect_0(Map _l,Enumeration enumerationx)
  { // implements: ownedLiteral->collect( lt | "  " + lt.name + " = " + ownedLiteral->indexOf(lt) + "\n" )
    HashMap _results_0 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { EnumerationLiteral lt = (EnumerationLiteral) _l.get(_i);
      String collect_x = "  " + lt.getname() + " = " + ( enumerationx.getownedLiteral().indexOf(lt) + 1 ) + "\n";
      if (collect_x != null) { _results_0.put(_i,collect_x); }
    }
    return _results_0;
  }

  public static List collect_1(List _l)
  { // implements: specialization->collect( generalization_1_xx | specific.allLeafSubclasses() )
    List _results_1 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Generalization generalization_1_xx = (Generalization) _l.get(_i);
      List collect_x = generalization_1_xx.getspecific().allLeafSubclasses();
      if (collect_x != null) { _results_1.add(collect_x); }
    }
    return _results_1;
  }

  public static HashMap collect_1(Map _l)
  { // implements: specialization->collect( generalization_1_xx | specific.allLeafSubclasses() )
    HashMap _results_1 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Generalization generalization_1_xx = (Generalization) _l.get(_i);
      List collect_x = generalization_1_xx.getspecific().allLeafSubclasses();
      if (collect_x != null) { _results_1.put(_i,collect_x); }
    }
    return _results_1;
  }

  public static List collect_2(List _l)
  { // implements: self.ownedOperation->collect( operation_2_xx | name )
    List _results_2 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation operation_2_xx = (Operation) _l.get(_i);
      String collect_x = operation_2_xx.getname();
      if (collect_x != null) { _results_2.add(collect_x); }
    }
    return _results_2;
  }

  public static HashMap collect_2(Map _l)
  { // implements: self.ownedOperation->collect( operation_2_xx | name )
    HashMap _results_2 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation operation_2_xx = (Operation) _l.get(_i);
      String collect_x = operation_2_xx.getname();
      if (collect_x != null) { _results_2.put(_i,collect_x); }
    }
    return _results_2;
  }

  public static List collect_8(List _l)
  { // implements: allProperties()->select( a | a.isStatic = false )->collect( x | x.initialisation() )
    List _results_8 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property x = (Property) _l.get(_i);
      String collect_x = x.initialisation();
      if (collect_x != null) { _results_8.add(collect_x); }
    }
    return _results_8;
  }

  public static HashMap collect_8(Map _l)
  { // implements: allProperties()->select( a | a.isStatic = false )->collect( x | x.initialisation() )
    HashMap _results_8 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property x = (Property) _l.get(_i);
      String collect_x = x.initialisation();
      if (collect_x != null) { _results_8.put(_i,collect_x); }
    }
    return _results_8;
  }

  public static List collect_11(List _l)
  { // implements: ownedOperation->select( x | x.isStatic & x.isCached & x.parameters.size = 1 )->collect( y | "  " + y.name + "_cache = dict({})" + "\n" )
    List _results_11 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation y = (Operation) _l.get(_i);
      String collect_x = "  " + y.getname() + "_cache = dict({})" + "\n";
      if (collect_x != null) { _results_11.add(collect_x); }
    }
    return _results_11;
  }

  public static HashMap collect_11(Map _l)
  { // implements: ownedOperation->select( x | x.isStatic & x.isCached & x.parameters.size = 1 )->collect( y | "  " + y.name + "_cache = dict({})" + "\n" )
    HashMap _results_11 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation y = (Operation) _l.get(_i);
      String collect_x = "  " + y.getname() + "_cache = dict({})" + "\n";
      if (collect_x != null) { _results_11.put(_i,collect_x); }
    }
    return _results_11;
  }

  public static List collect_13(List _l)
  { // implements: ownedOperation->select( x | x.isStatic = false & x.isCached & x.parameters.size = 1 )->collect( y | "    self." + y.name + "_cache = dict({})" + "\n" )
    List _results_13 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Operation y = (Operation) _l.get(_i);
      String collect_x = "    self." + y.getname() + "_cache = dict({})" + "\n";
      if (collect_x != null) { _results_13.add(collect_x); }
    }
    return _results_13;
  }

  public static HashMap collect_13(Map _l)
  { // implements: ownedOperation->select( x | x.isStatic = false & x.isCached & x.parameters.size = 1 )->collect( y | "    self." + y.name + "_cache = dict({})" + "\n" )
    HashMap _results_13 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Operation y = (Operation) _l.get(_i);
      String collect_x = "    self." + y.getname() + "_cache = dict({})" + "\n";
      if (collect_x != null) { _results_13.put(_i,collect_x); }
    }
    return _results_13;
  }

  public static List collect_15(List _l)
  { // implements: allProperties()->select( x | x.isStatic )->collect( y | "  " + y.name + " = " + y.type.defaultInitialValue() + "\n" )
    List _results_15 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property y = (Property) _l.get(_i);
      String collect_x = "  " + y.getname() + " = " + y.gettype().defaultInitialValue() + "\n";
      if (collect_x != null) { _results_15.add(collect_x); }
    }
    return _results_15;
  }

  public static HashMap collect_15(Map _l)
  { // implements: allProperties()->select( x | x.isStatic )->collect( y | "  " + y.name + " = " + y.type.defaultInitialValue() + "\n" )
    HashMap _results_15 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property y = (Property) _l.get(_i);
      String collect_x = "  " + y.getname() + " = " + y.gettype().defaultInitialValue() + "\n";
      if (collect_x != null) { _results_15.put(_i,collect_x); }
    }
    return _results_15;
  }

  public static List collect_18(List _l)
  { // implements: pars->tail()->collect( p | ", " + p )
    List _results_18 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { String p = (String) _l.get(_i);
      String collect_x = ", " + p;
      if (collect_x != null) { _results_18.add(collect_x); }
    }
    return _results_18;
  }

  public static HashMap collect_18(Map _l)
  { // implements: pars->tail()->collect( p | ", " + p )
    HashMap _results_18 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { String p = (String) _l.get(_i);
      String collect_x = ", " + p;
      if (collect_x != null) { _results_18.put(_i,collect_x); }
    }
    return _results_18;
  }

  public static List collect_19(List _l)
  { // implements: Enumeration->collect( enumeration_19_xx | name )
    List _results_19 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Enumeration enumeration_19_xx = (Enumeration) _l.get(_i);
      String collect_x = enumeration_19_xx.getname();
      if (collect_x != null) { _results_19.add(collect_x); }
    }
    return _results_19;
  }

  public static HashMap collect_19(Map _l)
  { // implements: Enumeration->collect( enumeration_19_xx | name )
    HashMap _results_19 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Enumeration enumeration_19_xx = (Enumeration) _l.get(_i);
      String collect_x = enumeration_19_xx.getname();
      if (collect_x != null) { _results_19.put(_i,collect_x); }
    }
    return _results_19;
  }

  public static List collect_20(List _l)
  { // implements: arrayIndex->collect( expr | expr.type.name )
    List _results_20 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Expression expr = (Expression) _l.get(_i);
      String collect_x = expr.gettype().getname();
      if (collect_x != null) { _results_20.add(collect_x); }
    }
    return _results_20;
  }

  public static HashMap collect_20(Map _l)
  { // implements: arrayIndex->collect( expr | expr.type.name )
    HashMap _results_20 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Expression expr = (Expression) _l.get(_i);
      String collect_x = expr.gettype().getname();
      if (collect_x != null) { _results_20.put(_i,collect_x); }
    }
    return _results_20;
  }

  public static List collect_21(List _l)
  { // implements: parameters->collect( p | ", " + p.name )
    List _results_21 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Property p = (Property) _l.get(_i);
      String collect_x = ", " + p.getname();
      if (collect_x != null) { _results_21.add(collect_x); }
    }
    return _results_21;
  }

  public static HashMap collect_21(Map _l)
  { // implements: parameters->collect( p | ", " + p.name )
    HashMap _results_21 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Property p = (Property) _l.get(_i);
      String collect_x = ", " + p.getname();
      if (collect_x != null) { _results_21.put(_i,collect_x); }
    }
    return _results_21;
  }

  public static List collect_22(List _l,int indent)
  { // implements: statements->collect( s | s.toPython(indent) )
    List _results_22 = new Vector();
    for (int _i = 0; _i < _l.size(); _i++)
    { Statement s = (Statement) _l.get(_i);
      String collect_x = s.toPython(indent);
      if (collect_x != null) { _results_22.add(collect_x); }
    }
    return _results_22;
  }

  public static HashMap collect_22(Map _l,int indent)
  { // implements: statements->collect( s | s.toPython(indent) )
    HashMap _results_22 = new HashMap();
    java.util.Set _keys = _l.keySet();
    for (Object _i : _keys)
    { Statement s = (Statement) _l.get(_i);
      String collect_x = s.toPython(indent);
      if (collect_x != null) { _results_22.put(_i,collect_x); }
    }
    return _results_22;
  }



    public static boolean equals(List a, List b)
    { return a.containsAll(b) && b.containsAll(a); }


    public Set add(Object x)
    { if (x != null) { elements.add(x); }
      return this; }

    public Set add(int x)
    { elements.add(new Integer(x));
      return this; }

    public Set add(long x)
    { elements.add(new Long(x));
      return this; }

    public Set add(double x)
    { elements.add(new Double(x));
      return this; }

    public Set add(boolean x)
    { elements.add(new Boolean(x));
      return this; }

    public Vector getElements() { return elements; }


  public static Vector copyCollection(Vector s)
  { Vector result = new Vector();
    result.addAll(s);
    return result;
  }

  public static HashMap copyMap(Map s)
  { HashMap result = new HashMap();
    result.putAll(s);
    return result;
  }

  public static int[] newRefint(int x)
  { int[] res = new int[1]; 
    res[0] = x; 
    return res; 
  } 

  public static long[] newReflong(long x)
  { long[] res = new long[1]; 
    res[0] = x; 
    return res; 
  } 

  public static double[] newRefdouble(double x)
  { double[] res = new double[1]; 
    res[0] = x; 
    return res; 
  } 

  public static boolean[] newRefboolean(boolean x)
  { boolean[] res = new boolean[1]; 
    res[0] = x; 
    return res; 
  } 

  public static String[] newRefString(String x)
  { String[] res = new String[1]; 
    res[0] = x; 
    return res; 
  } 

  public static Object[] newRefObject(Object x)
  { Object[] res = new Object[1]; 
    res[0] = x; 
    return res; 
  } 

  public static Vector sequenceRange(Object[] arr, int n)
  { Vector res = new Vector();
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



  public static Comparable max(List l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) l.get(0); 
    for (int i = 1; i < l.size(); i++)
    { Comparable e = (Comparable) l.get(i);
      if (res.compareTo(e) < 0) { res = e; } }
    return res; }
  public static Comparable max(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return max(range);  }



  public static Comparable min(List l)
  { Comparable res = null; 
    if (l.size() == 0) { return res; }
    res = (Comparable) l.get(0); 
    for (int i = 1; i < l.size(); i++)
    { Comparable e = (Comparable) l.get(i);
      if (res.compareTo(e) > 0) { res = e; } }
    return res; }
  public static Comparable min(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return min(range);  }



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
    return res;
  }

  public static String subtract(String a, String b)
  { String res = ""; 
    for (int i = 0; i < a.length(); i++)
    { if (b.indexOf(a.charAt(i)) < 0)
      { res = res + a.charAt(i); }
    }
    return res;
  }



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

  public static int sumint(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return sumint(range);  }

  public static double sumdouble(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return sumdouble(range);  }

  public static long sumlong(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return sumlong(range);  }

  public static String sumString(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return sumString(range);  }



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

  public static int prdint(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return prdint(range);  }

  public static double prddouble(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return prddouble(range);  }

  public static long prdlong(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return prdlong(range);  }



  public static List concatenate(List a, List b)
  { List res = new Vector(); 
    res.addAll(a); 
    res.addAll(b); 
    return res;
  }

  public static Vector singleValueMatrix(Vector sh, Object x)
  { Vector result;
    if ((sh).size() == 0)
    { return (new Vector()); }
    int n = (int) sh.get(0);
   
    if ((sh).size() == 1)
    { Vector _results_0 = new Vector();
      for (int _i = 0; _i < n; _i++)
      { _results_0.add(x); }
      return _results_0;
    }

    Vector tl = (Vector) Set.tail(sh);
    Vector res = (new Vector());
    Vector _results_1 = new Vector();
    for (int _i = 0; _i < n; _i++)
    { _results_1.add(Set.singleValueMatrix(tl,x)); }
    res = (Vector) Set.concatenate(res, _results_1);
    return res;
  }



  public static List closurePropertyqualifier(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { Property propertyx = (Property) _r.get(_i);
      closurePropertyqualifier(propertyx, _path);
    }
    return _path;
  }

  private static void closurePropertyqualifier(Property propertyx, List _path)
  { if (_path.contains(propertyx)) { return; }
    _path.add(propertyx);
    List qualifierx = propertyx.getqualifier();
    for (int _i = 0; _i < qualifierx.size(); _i++)
    { if (qualifierx.get(_i) instanceof Property)
      { Property propertyxx = (Property) qualifierx.get(_i);
        closurePropertyqualifier(propertyxx, _path);
      }
      else { _path.add(qualifierx.get(_i)); }
    }
  }
  public static List closureCollectionExpressionelements(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { CollectionExpression collectionexpressionx = (CollectionExpression) _r.get(_i);
      closureCollectionExpressionelements(collectionexpressionx, _path);
    }
    return _path;
  }

  private static void closureCollectionExpressionelements(CollectionExpression collectionexpressionx, List _path)
  { if (_path.contains(collectionexpressionx)) { return; }
    _path.add(collectionexpressionx);
    List elementsx = collectionexpressionx.getelements();
    for (int _i = 0; _i < elementsx.size(); _i++)
    { if (elementsx.get(_i) instanceof CollectionExpression)
      { CollectionExpression collectionexpressionxx = (CollectionExpression) elementsx.get(_i);
        closureCollectionExpressionelements(collectionexpressionxx, _path);
      }
      else { _path.add(elementsx.get(_i)); }
    }
  }
  public static List closureBasicExpressionparameters(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _r.get(_i);
      closureBasicExpressionparameters(basicexpressionx, _path);
    }
    return _path;
  }

  private static void closureBasicExpressionparameters(BasicExpression basicexpressionx, List _path)
  { if (_path.contains(basicexpressionx)) { return; }
    _path.add(basicexpressionx);
    List parametersx = basicexpressionx.getparameters();
    for (int _i = 0; _i < parametersx.size(); _i++)
    { if (parametersx.get(_i) instanceof BasicExpression)
      { BasicExpression basicexpressionxx = (BasicExpression) parametersx.get(_i);
        closureBasicExpressionparameters(basicexpressionxx, _path);
      }
      else { _path.add(parametersx.get(_i)); }
    }
  }
  public static List closureBasicExpressionarrayIndex(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _r.get(_i);
      closureBasicExpressionarrayIndex(basicexpressionx, _path);
    }
    return _path;
  }

  private static void closureBasicExpressionarrayIndex(BasicExpression basicexpressionx, List _path)
  { if (_path.contains(basicexpressionx)) { return; }
    _path.add(basicexpressionx);
    List arrayIndexx = basicexpressionx.getarrayIndex();
    for (int _i = 0; _i < arrayIndexx.size(); _i++)
    { if (arrayIndexx.get(_i) instanceof BasicExpression)
      { BasicExpression basicexpressionxx = (BasicExpression) arrayIndexx.get(_i);
        closureBasicExpressionarrayIndex(basicexpressionxx, _path);
      }
      else { _path.add(arrayIndexx.get(_i)); }
    }
  }
  public static List closureBasicExpressionobjectRef(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { BasicExpression basicexpressionx = (BasicExpression) _r.get(_i);
      closureBasicExpressionobjectRef(basicexpressionx, _path);
    }
    return _path;
  }

  private static void closureBasicExpressionobjectRef(BasicExpression basicexpressionx, List _path)
  { if (_path.contains(basicexpressionx)) { return; }
    _path.add(basicexpressionx);
    List objectRefx = basicexpressionx.getobjectRef();
    for (int _i = 0; _i < objectRefx.size(); _i++)
    { if (objectRefx.get(_i) instanceof BasicExpression)
      { BasicExpression basicexpressionxx = (BasicExpression) objectRefx.get(_i);
        closureBasicExpressionobjectRef(basicexpressionxx, _path);
      }
      else { _path.add(objectRefx.get(_i)); }
    }
  }
  public static List closureEntitysuperclass(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { Entity entityx = (Entity) _r.get(_i);
      closureEntitysuperclass(entityx, _path);
    }
    return _path;
  }

  private static void closureEntitysuperclass(Entity entityx, List _path)
  { if (_path.contains(entityx)) { return; }
    _path.add(entityx);
    List superclassx = entityx.getsuperclass();
    for (int _i = 0; _i < superclassx.size(); _i++)
    { if (superclassx.get(_i) instanceof Entity)
      { Entity entityxx = (Entity) superclassx.get(_i);
        closureEntitysuperclass(entityxx, _path);
      }
      else { _path.add(superclassx.get(_i)); }
    }
  }
  public static List closureConditionalStatementelsePart(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { ConditionalStatement conditionalstatementx = (ConditionalStatement) _r.get(_i);
      closureConditionalStatementelsePart(conditionalstatementx, _path);
    }
    return _path;
  }

  private static void closureConditionalStatementelsePart(ConditionalStatement conditionalstatementx, List _path)
  { if (_path.contains(conditionalstatementx)) { return; }
    _path.add(conditionalstatementx);
    List elsePartx = conditionalstatementx.getelsePart();
    for (int _i = 0; _i < elsePartx.size(); _i++)
    { if (elsePartx.get(_i) instanceof ConditionalStatement)
      { ConditionalStatement conditionalstatementxx = (ConditionalStatement) elsePartx.get(_i);
        closureConditionalStatementelsePart(conditionalstatementxx, _path);
      }
      else { _path.add(elsePartx.get(_i)); }
    }
  }
  public static List closureSequenceStatementstatements(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { SequenceStatement sequencestatementx = (SequenceStatement) _r.get(_i);
      closureSequenceStatementstatements(sequencestatementx, _path);
    }
    return _path;
  }

  private static void closureSequenceStatementstatements(SequenceStatement sequencestatementx, List _path)
  { if (_path.contains(sequencestatementx)) { return; }
    _path.add(sequencestatementx);
    List statementsx = sequencestatementx.getstatements();
    for (int _i = 0; _i < statementsx.size(); _i++)
    { if (statementsx.get(_i) instanceof SequenceStatement)
      { SequenceStatement sequencestatementxx = (SequenceStatement) statementsx.get(_i);
        closureSequenceStatementstatements(sequencestatementxx, _path);
      }
      else { _path.add(statementsx.get(_i)); }
    }
  }
  public static List closureTryStatementcatchClauses(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { TryStatement trystatementx = (TryStatement) _r.get(_i);
      closureTryStatementcatchClauses(trystatementx, _path);
    }
    return _path;
  }

  private static void closureTryStatementcatchClauses(TryStatement trystatementx, List _path)
  { if (_path.contains(trystatementx)) { return; }
    _path.add(trystatementx);
    List catchClausesx = trystatementx.getcatchClauses();
    for (int _i = 0; _i < catchClausesx.size(); _i++)
    { if (catchClausesx.get(_i) instanceof TryStatement)
      { TryStatement trystatementxx = (TryStatement) catchClausesx.get(_i);
        closureTryStatementcatchClauses(trystatementxx, _path);
      }
      else { _path.add(catchClausesx.get(_i)); }
    }
  }
  public static List closureTryStatementendStatement(List _r)
  { Vector _path = new Vector();
    for (int _i = 0; _i < _r.size(); _i++)
    { TryStatement trystatementx = (TryStatement) _r.get(_i);
      closureTryStatementendStatement(trystatementx, _path);
    }
    return _path;
  }

  private static void closureTryStatementendStatement(TryStatement trystatementx, List _path)
  { if (_path.contains(trystatementx)) { return; }
    _path.add(trystatementx);
    List endStatementx = trystatementx.getendStatement();
    for (int _i = 0; _i < endStatementx.size(); _i++)
    { if (endStatementx.get(_i) instanceof TryStatement)
      { TryStatement trystatementxx = (TryStatement) endStatementx.get(_i);
        closureTryStatementendStatement(trystatementxx, _path);
      }
      else { _path.add(endStatementx.get(_i)); }
    }
  }


  public static List asSet(List a)
  { Vector res = new Vector(); 
    for (int i = 0; i < a.size(); i++)
    { Object obj = a.get(i);
      if (res.contains(obj)) { } 
      else { res.add(obj); }
    } 
    return res; 
  }

  public static List asOrderedSet(List a)
  { return asSet(a); }

  public static List asSet(Map m)
  { Vector range = new Vector();
    range.addAll(m.values());
    return asSet(range);
  }

  public static Vector mapAsSequence(Map m)
  { Vector range = new Vector();
    java.util.Set ss = m.entrySet();
    for (Object x : ss)
    { Map.Entry ee = (Map.Entry) x;
      HashMap mx = new HashMap(); 
      mx.put(ee.getKey(), ee.getValue());
      range.add(mx); 
    } 
    return range;
  }

  public static Vector mapAsSet(Map m)
  { Vector range = mapAsSequence(m); 
    return (Vector) asSet(range); 
  }



  public static List reverse(List a)
  { List res = new Vector(); 
    for (int i = a.size() - 1; i >= 0; i--)
    { res.add(a.get(i)); } 
    return res;
  }

  public static String reverse(String a)
  { String res = ""; 
    for (int i = a.length() - 1; i >= 0; i--)
    { res = res + a.charAt(i); } 
    return res;
  }



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

  public static List asSequence(final List a)
  { return a; }

  public static List asBag(final List a)
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
  { int len = s.length();
    if (len == 0) { return s; }
    if (j > len) { j = len; }
    if (j < i) { return ""; }
    if (i > len) { return ""; }
    if (i < 1) { i = 1; }
    return s.substring(i-1,j);
  }

  public static List subrange(List l, int i, int j)
  { List tmp = new Vector(); 
    if (i < 0) { i = l.size() + i; }
    if (j < 0) { j = l.size() + j; }
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

  public static int count(Map m, Object obj)
  { List range = new Vector();
    range.addAll(m.values());
    return count(range,obj);  }

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
  public static Object any(Map m)
  { List range = new Vector();
    range.addAll(m.values());
    return any(range);  }



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

  public static HashMap unionAllMap(List se)
  { HashMap res = new HashMap(); 
    for (int i = 0; i < se.size(); i++)
    { Map b = (Map) se.get(i); 
      res.putAll(b);
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


  public static List insertInto(List l, int ind, List ob)
  { List res = new Vector();
    for (int i = 0; i < ind-1 && i < l.size(); i++)
    { res.add(l.get(i)); }
    for (int j = 0; j < ob.size(); j++)
    { res.add(ob.get(j)); }
    for (int i = ind-1; i < l.size(); i++)
    { res.add(l.get(i)); }
    return res;
  }

  public static List excludingSubrange(List l, int startIndex, int endIndex)
  { List res = new Vector();
    for (int i = 0; i < startIndex-1 && i < l.size(); i++)
    { res.add(l.get(i)); }
    for (int i = endIndex; i < l.size(); i++)
    { res.add(l.get(i)); }
    return res;
  }

  public static String setSubrange(String ss, int startIndex, int endIndex, String v)
  { String res = ss;
    if (ss.length() < endIndex)
    { for (int i = ss.length()-1; i < endIndex; i++)
      { res = res + " "; }
    }
    return res.substring(0,startIndex-1) + v +
           res.substring(endIndex);  
  }

  public static String insertInto(String l, int ind, Object ob)
  { String res = "";
    for (int i = 0; i < ind-1 && i < l.length(); i++)
    { res = res + l.charAt(i); }
    res = res + ob; 
    for (int i = ind-1; i < l.length(); i++)
    { res = res + l.charAt(i); }
    return res;
  }
  public static String excludingSubrange(String l, int startIndex, int endIndex)
  { String res = "";
    for (int i = 0; i < startIndex-1 && i < l.length(); i++)
    { res = res + l.charAt(i); }
    for (int i = endIndex; i < l.length(); i++)
    { res = res + l.charAt(i); }
    return res;
  }



  public static List removeAt(List l, int ind)
  { List res = new Vector();
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

  public static List removeFirst(List l, Object x)
  { List res = new Vector();
    res.addAll(l); 
    res.remove(x);
    return res;
  }

  public static List setAt(List l, int ind, Object val)
  { List res = new Vector();
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
  { try { Integer.parseInt(str.trim()); return true; }
    catch (Exception _e) { return false; }
  }

 public static int toInt(String str)
  { /* Trim leading 0's */
    if (str == null || str.length() == 0)
    { return 0; }
    String trm = str.trim();
    while (trm.length() > 0 && trm.charAt(0) == '0')
    { trm = trm.substring(1); }
    if (trm.indexOf(".") > 0)
    { trm = trm.substring(0,trm.indexOf(".")); }
    try { int x = Integer.parseInt(trm.trim());
          return x; }
    catch (Exception _e) { return 0; }
  }

  public static int toInteger(String str)
  { if (str == null || str.length() == 0)
    { return 0; }
    String trm = str.trim();
    while (trm.length() > 0 && trm.charAt(0) == '0')
    { trm = trm.substring(1); }
    if (trm.indexOf(".") > 0)
    { trm = trm.substring(0,trm.indexOf(".")); }
    try { int x = Integer.decode(trm).intValue();
      return x; 
    }
    catch (Exception _e) { return 0; }
  }



 public static boolean isReal(String str)
  { try { double d = Double.parseDouble(str.trim()); 
          if (Double.isNaN(d)) { return false; }
          return true; }
    catch (Exception _e) { return false; }
  }

 public static double toDouble(String str)
  { try { double x = Double.parseDouble(str.trim());
          return x; }
    catch (Exception _e) { return 0; }
  }


 public static boolean isLong(String str)
  { try { Long.parseLong(str.trim()); return true; }
    catch (Exception _e) { return false; }
  }

 public static long toLong(String str)
  { try { long x = Long.parseLong(str.trim());
          return x; }
    catch (Exception _e) { return 0; }
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



  public static Vector allMatches(String str, String regex)
  { java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    Vector res = new Vector();
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


  public static Vector split(String str, String delim)
  { String[] splits = str.split(delim);
    Vector res = new Vector();
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
  { if (str == null) { return null; }
    java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = patt.matcher(str);
    return matcher.replaceAll(rep);
  }


  public static String replaceFirstMatch(String str, String regex, String rep)
  { if (str == null) { return null; }
    java.util.regex.Pattern patt = java.util.regex.Pattern.compile(regex);
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

  public static HashMap intersectAllMap(List col)
  { HashMap res = new HashMap();
    if (col.size() == 0) 
    { return res; } 

    Map m0 = (Map) col.get(0);
    res.putAll(m0); 

    for (int i = 1; i < col.size(); i++)
    { Map m = (Map) col.get(i);
      res = Set.intersectionMap(res,m);
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
    if (val == null)
    { return false; }
    return true;
  }

  public static boolean excludesKey(Map m, Object key) 
  { Object val = m.get(key); 
    if (val == null)
    { return true; }
    return false;
  }

  public static boolean includesValue(Map m, Object val) 
  { Vector keys = new Vector();
    keys.addAll(m.keySet());
    for (int x = 0; x < keys.size(); x++)
    { Object v = m.get(x);
      if (v != null && v.equals(val))
      { return true; }
    }    
    return false;
  }

  public static boolean excludesValue(Map m, Object val) 
  { Vector keys = new Vector();
    keys.addAll(m.keySet());
    for (int x = 0; x < keys.size(); x++)
    { Object v = m.get(x);
      if (v != null && v.equals(val))
      { return false; }
    }    
    return true;
  }



  public static <T> T[] asReference(Vector sq, T[] r)
  {
    for (int i = 0; i < sq.size() && i < r.length; i++)
    { r[i] = (T) sq.get(i); }
    return r;
  }

  public static int[] resizeTo(int[] arr, int n)
  { int[] tmp = new int[n];
    for (int i = 0; i < n; i++)
    { tmp[i] = arr[i]; }
    return tmp;
  }

  public static long[] resizeTo(long[] arr, int n)
  { long[] tmp = new long[n];
    for (int i = 0; i < n; i++)
    { tmp[i] = arr[i]; }
    return tmp;
  }

  public static double[] resizeTo(double[] arr, int n)
  { double[] tmp = new double[n];
    for (int i = 0; i < n; i++)
    { tmp[i] = arr[i]; }
    return tmp;
  }

  public static boolean[] resizeTo(boolean[] arr, int n)
  { boolean[] tmp = new boolean[n];
    for (int i = 0; i < n; i++)
    { tmp[i] = arr[i]; }
    return tmp;
  }

  public static Object[] resizeTo(Object[] arr, int n)
  { Object[] tmp = new Object[n];
    for (int i = 0; i < n; i++)
    { tmp[i] = arr[i]; }
    return tmp;
  }

  public static Vector sequenceRange(int[] arr, int n)
  { Vector res = new Vector();
    for (int i = 0; i < n && i < arr.length; i++)
    { res.add(new Integer(arr[i])); }
    return res; 
  }

  public static Vector sequenceRange(long[] arr, int n)
  { Vector res = new Vector();
    for (int i = 0; i < n && i < arr.length; i++)
    { res.add(new Long(arr[i])); }
    return res; 
  }

  public static Vector sequenceRange(double[] arr, int n)
  { Vector res = new Vector();
    for (int i = 0; i < n && i < arr.length; i++)
    { res.add(new Double(arr[i])); }
    return res; 
  }

  public static Vector sequenceRange(boolean[] arr, int n)
  { Vector res = new Vector();
    for (int i = 0; i < n && i < arr.length; i++)
    { res.add(new Boolean(arr[i])); }
    return res; 
  }

  public static <T> Vector asSequence(T[] r)
  { Vector res = new Vector(); 
    for (int i = 0; i < r.length; i++)
    { res.add(r[i]); }
    return res;
  }


  }
}
