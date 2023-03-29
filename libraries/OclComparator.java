import java.util.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.function.Function;
import java.io.Serializable;

class OclComparator { static ArrayList<OclComparator> OclComparator_allInstances = new ArrayList<OclComparator>();

  OclComparator() { OclComparator_allInstances.add(this); }

  static OclComparator createOclComparator() { OclComparator result = new OclComparator();
    return result; }

  String oclcomparatorId = ""; /* primary */
  static Map<String,OclComparator> OclComparator_index = new HashMap<String,OclComparator>();

  static OclComparator createByPKOclComparator(String oclcomparatorIdx)
  { OclComparator result = OclComparator.OclComparator_index.get(oclcomparatorIdx);
    if (result != null) { return result; }
    result = new OclComparator();
    OclComparator.OclComparator_index.put(oclcomparatorIdx,result);
    result.oclcomparatorId = oclcomparatorIdx;
    return result; }

  static void killOclComparator(String oclcomparatorIdx)
  { OclComparator rem = OclComparator_index.get(oclcomparatorIdx);
    if (rem == null) { return; }
    ArrayList<OclComparator> remd = new ArrayList<OclComparator>();
    remd.add(rem);
    OclComparator_index.remove(oclcomparatorIdx);
    OclComparator_allInstances.removeAll(remd);
  }


  public int compare(Object lhs, Object rhs)
  {
    int result = 0;
    result = (((Comparable) lhs).compareTo(rhs));
    return result;
  }


  public static ArrayList<Object> lowerSegment(ArrayList<Object> col, Object x, OclComparator cmp)
  {
    ArrayList<Object> result = new ArrayList<Object>();
    result = Ocl.selectSequence(col,(y)->{return cmp.compare(y, x) < 0;});
    return result;
  }


  public static ArrayList<Object> sortWith(ArrayList<Object> col, OclComparator cmp)
  {
    ArrayList<Object> result = new ArrayList<Object>();
    result = Ocl.sortedBy(col, Ocl.collectSequence(col, (x)->{ return OclComparator.lowerSegment(col, x, cmp).size(); }));
    return result;
  }


  public static Object maximumWith(ArrayList<Object> col, OclComparator cmp)
  {
    Object result = null;
    result = Ocl.any(Ocl.selectSequence(col,(x)->{return Ocl.forAll(col, (y)->{ return cmp.compare(y, x) <= 0; });}));
    return result;
  }


  public static Object minimumWith(ArrayList<Object> col, OclComparator cmp)
  {
    Object result = null;
    result = Ocl.any(Ocl.selectSequence(col,(x)->{return Ocl.forAll(col, (y)->{ return cmp.compare(y, x) >= 0; });}));
    return result;
  }


  public static int binarySearch(ArrayList<Object> col, Object x, OclComparator cmp)
  {
    int result = 0;
    result = OclComparator.lowerSegment(col, x, cmp).size();
    return result;
  }

}

