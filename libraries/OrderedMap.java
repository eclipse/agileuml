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

class OrderedMap<K,T> { static ArrayList<OrderedMap> OrderedMap_allInstances = new ArrayList<OrderedMap>();

  OrderedMap() { OrderedMap_allInstances.add(this); }

  static OrderedMap createOrderedMap() { OrderedMap result = new OrderedMap();
    return result; }

  ArrayList<K> elements = (new ArrayList<K>());
  HashMap<K,T> items = (new HashMap<K,T>());
  String orderedmapId = ""; /* primary */
  static Map<String,OrderedMap> OrderedMap_index = new HashMap<String,OrderedMap>();

  static OrderedMap createByPKOrderedMap(String orderedmapIdx)
  { OrderedMap result = OrderedMap.OrderedMap_index.get(orderedmapIdx);
    if (result != null) { return result; }
    result = new OrderedMap();
    OrderedMap.OrderedMap_index.put(orderedmapIdx,result);
    result.orderedmapId = orderedmapIdx;
    return result; }

  static void killOrderedMap(String orderedmapIdx)
  { OrderedMap rem = OrderedMap_index.get(orderedmapIdx);
    if (rem == null) { return; }
    ArrayList<OrderedMap> remd = new ArrayList<OrderedMap>();
    remd.add(rem);
    OrderedMap_index.remove(orderedmapIdx);
    OrderedMap_allInstances.removeAll(remd);
  }


  public T getByIndex(int i)
  {
    T result = null;
    if (i > 0 && i <= elements.size())
    {
      result = (T) items.get((K) elements.get(i - 1));
    }
    else {
      result = null;
    }
    return result;
  }


  public T getByKey(K k)
  {
    T result = null;
    result = (T) items.get(k);
    return result;
  }


  public void add(K k, T t)
  {
    elements = Ocl.append(elements,k);
    items.put(k,t);
  }


  public void remove(int i)
  {
    K k = ((K) (elements).get(i - 1));
    elements = Ocl.removeAt(elements,i);
    items = Ocl.antirestrictMap(items,Ocl.initialiseSet(k));
  }

}

