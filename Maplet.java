import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Utilities */ 

class Maplet
{ Object source;
  Object dest;  /* Usually both are named */

  Maplet(Object src, Object dst)
  { source = src;
    dest = dst; 
  }


  public boolean equals(Object x) 
  { if (x instanceof Maplet)
    { Maplet m = (Maplet) x; 
      if (source.equals(m.source) && dest.equals(m.dest))
      { return true; } 
    } 
    return false; 
  } 


  public Object getSource() 
  { return source; } 

  public Object getTarget()
  { return dest; } 

  public void print_maplet()
  { System.out.print(((Named) source).label);
    System.out.print("  |-->  ");
    System.out.println(((Named) dest).label); }

  public void print_maplet(PrintWriter out)
  { out.print(((Named) source).label);
    out.print("  |-->  ");
    out.println(((Named) dest).label); 
  }

  public String toString()
  { return source + "  |-->   " + dest; } 

  public void display() 
  { System.out.print(source); 
    System.out.print("  |-->  ");
    System.out.println(dest); }

  /* public boolean equals(Object mm) 
  { if (mm instanceof Maplet) 
    { Maplet maplet = (Maplet) mm; 
      return (source.equals(maplet.source) && 
              dest.equals(maplet.dest)); 
    } 
    else 
    { return false; } 
  } */ 

 public static void main(String[] args)
  { Vector t = new Vector(); 
    Vector vals1 = new Vector(); 
    vals1.add("b"); vals1.add("c"); vals1.add("d"); 
    t.add(new Maplet("a", vals1));
    Vector vals2 = new Vector(); 
    vals2.add("e"); vals2.add("f"); 
    t.add(new Maplet("h",vals2));  
    System.out.println(Map.submappings(t)); 
  }     
}

class Map
{ // really a multi-map

  int maxmap = 100;
  Vector elements;  /* vector of maplets */

  Map()  { elements = new Vector(maxmap); }

  Map(Vector elems)  { elements = elems; }

  Map(Vector sources, Vector targets) 
  { elements = new Vector(); 
    if (targets.size() == 0) { } 
    else if (targets.size() == 1) 
    { Object t = targets.get(0); 
      for (int j = 0; j < sources.size(); j++) 
      { Maplet mx = new Maplet(sources.get(j),t); 
        elements.add(mx); 
      } 
    } 
    else 
    { for (int i = 0; i < sources.size() && i < targets.size(); i++) 
      { Maplet mm = new Maplet(sources.get(i), targets.get(i)); 
        elements.add(mm); 
      } 
    } 
  } 

  Map(Map mold) 
  { elements = new Vector(); 
    for (int i = 0; i < mold.elements.size(); i++) 
    { Maplet mm = (Maplet) mold.elements.get(i); 
      Maplet mmnew = new Maplet(mm.source, mm.dest); 
      elements.add(mmnew); 
    } 
  } 

  public Object clone()
  { Vector newelems = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      Maplet mmnew = new Maplet(mm.source, mm.dest); 
      newelems.add(mmnew); 
    } 
    return new Map(newelems); 
  } 

  public boolean equals(Object x) 
  { // equals as sets, ignoring duplicates
    if (x instanceof Map) 
    { Map m = (Map) x; 
      Vector melems = m.elements; 
      for (int i = 0; i < elements.size(); i++) 
      { Maplet mm = (Maplet) elements.get(i); 
        if (melems.contains(mm)) { } 
        else 
        { return false; } 
      } 
      for (int i = 0; i < melems.size(); i++) 
      { Maplet mmx = (Maplet) melems.get(i); 
        if (elements.contains(mmx)) { } 
        else 
        { return false; } 
      } 
      return true; 
    } 
    return false; 
  } 

  public Vector getElements()
  { return elements; } 

  public int size()
  { return elements.size(); } 

  public Maplet get(int i)
  { return (Maplet) elements.get(i); } 

  public void clear()
  { elements = new Vector(maxmap); }

  public void add_element(Maplet m)
  {  elements.addElement(m);  /* assumes still a map */
  }

  public void add_pair(Object o1, Object o2)
  { Maplet mm = new Maplet(o1,o2);
    if (elements.contains(mm)) { } 
    else 
    { elements.addElement(mm); }
  } 

  public void remove_pair(Object o1, Object o2)
  { Maplet mm = new Maplet(o1,o2);
    Vector mms = new Vector(); 
    mms.add(mm); 
    elements.removeAll(mms); 
  } 

  public static Map extendDomainRange(Map m, Vector extra) 
  { Vector elems = new Vector(); 
    
    elems.addAll(m.elements); 
    Map res = new Map(elems); 

    for (int i = 0; i < extra.size(); i++) 
    { // Maplet mm = new Maplet(extra.get(i), extra.get(i)); 
      res.set(extra.get(i),extra.get(i)); 
    } 
    return res; 
  } 

  public static Map extendDomainRange(Map m, Vector srcs, Vector trgs) 
  { Vector elems = new Vector(); 
    elems.addAll(m.elements); 
    Map res = new Map(elems); 

    for (int i = 0; i < srcs.size() && i < trgs.size(); i++) 
    { res.set(srcs.get(i), trgs.get(i)); } 
    return res; 
  } 

  public static Map union(Map m1, Map m2)
  { Vector elems = m1.elements; 
    Vector elems2 = m2.elements; 
    Map res = new Map(); 

    for (int i = 0; i < elems.size(); i++) 
    { Maplet mm = (Maplet) elems.get(i);
      if (res.elements.contains(mm)) { } 
      else 
      { res.add_element(mm); }  
    }

    for (int i = 0; i < elems2.size(); i++) 
    { Maplet mm = (Maplet) elems2.get(i);
      if (res.elements.contains(mm)) { } 
      else 
      { res.add_element(mm); }  
    }

    return res; 
  } 

  public Map removeDuplicates() 
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      if (res.contains(mm)) { } 
      else 
      { res.add(mm); } 
    } 
    return new Map(res); 
  } 

 public void print_map()
 { for (int i = 0; i < elements.size(); i++)
   { ((Maplet) elements.elementAt(i)).print_maplet(); }
 }

 public String toString()
 { String res = ""; 

   for (int i = 0; i < elements.size(); i++)
   { res = res + " " + ((Maplet) elements.elementAt(i)) + "\n"; }
   return res; 
 }

  public boolean in_domain(Object x)
  { if (x == null) { return false; } 
    for (int i = 0; i < elements.size(); i++)
    { if (x.equals(((Maplet) elements.elementAt(i)).source))
      { return true; } 
    }
    return false; 
  }

  public boolean in_range(Object x)
  { if (x == null) { return false; } 
    for (int i = 0; i < elements.size(); i++)
    { if (x.equals(((Maplet) elements.elementAt(i)).dest))
      { return true; } 
    }
    return false; 
  }

  public Vector domain()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { Maplet mm = (Maplet) elements.elementAt(i); 
      Object src = mm.source; 
      if (res.contains(src)) { } 
      else 
      { res.add(src); }
    } 
    return res; 
  }
 
  public Vector range()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { Maplet mm = (Maplet) elements.elementAt(i); 
      Object src = mm.dest; 
      if (res.contains(src)) { } 
      else 
      { res.add(src); }
    } 
    return res; 
  }


  public Object apply(Object x)
  { if (x == null) { return null; } 

    for (int i = 0; i < elements.size(); i++)
    { if (x.equals(((Maplet) elements.elementAt(i)).source))
      { return ((Maplet) elements.elementAt(i)).dest; } 
    }
    return null; 
  }

  public Object get(Object x)
  { if (x == null) { return null; } 

    for (int i = 0; i < elements.size(); i++)
    { if (x.equals(((Maplet) elements.elementAt(i)).source))
      { return ((Maplet) elements.elementAt(i)).dest; } 
    }
    return null; 
  }

  public void set(Object x, Object y)
  { if (x == null) { return; } 

    for (int i = 0; i < elements.size(); i++)
    { Maplet m = (Maplet) elements.elementAt(i); 
      if (x.equals(m.source))
      { m.dest = y;
        return; 
      } 
    }
    elements.add(new Maplet(x,y)); 
  }

  public Vector image(Vector ss)
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { Maplet mm = (Maplet) elements.elementAt(i);
      Object src = mm.source; 
      if (ss.contains(src))
      { Object trg = mm.dest; 
        if (res.contains(trg)) { } 
        else 
        { res.add(trg); }
      }
    } 
    return res; 
  }

  public Vector inverseImage(Vector tt)
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { Maplet mm = (Maplet) elements.elementAt(i);
      Object trg = mm.dest; 
      if (tt.contains(trg))
      { Object src = mm.source; 
        if (res.contains(src)) { } 
        else 
        { res.add(src); }
      }
    } 
    return res; 
  }

  public static Vector compose(Vector map1, Vector map2)
  { Vector res = new Vector();
    for (int i = 0; i < map1.size(); i++)
    { Maplet mm1 = (Maplet) map1.elementAt(i);
      for (int j = 0; j < map2.size(); j++)
      { Maplet mm2 = (Maplet) map2.elementAt(j);
        if (mm1.dest.equals(mm2.source))
        { Maplet mm3 = new Maplet(mm1.source, mm2.dest);
          res.add(mm3); } } }
    return res; }

  public static Map inverse(Map m)
  { Vector elems = new Vector(); 
    for (int i = 0; i < m.elements.size(); i++) 
    { Maplet mm = (Maplet) m.elements.get(i); 
      Maplet nmm = new Maplet(mm.dest, mm.source); 
      elems.add(nmm); 
    } 
    return new Map(elems); 
  } 

  public static Map compose(Map m1, Map m2)
  { Map res = new Map(); 
    res.elements = compose(m1.elements, m2.elements); 
    return res; 
  } 

  public void put(Object src, Object trg)
  { // really extension
    Map map2 = new Map(); 
    map2.add_pair(src,trg); 
    Vector newelems = compose(elements,map2.elements); 
    elements = VectorUtil.union(elements,newelems); 
  } 

  public static Vector transitiveClosure(Vector map)
  { Vector res = (Vector) map.clone();
    for (int i = 0; i < map.size(); i++)
    { Vector res1 = compose(res,map);
      Vector res2 = VectorUtil.vector_merge(res,res1);
      // System.out.println("Trans closure step " + i + ": " + res2);
      res = res2; 
    }
    return res; 
  }   

  public void addAll(Vector maplets)
  { elements.addAll(maplets); } 

  public void addAll(Map m2)
  { elements.addAll(m2.elements); } 

  public void removeAll(Vector maplets)
  { elements.removeAll(maplets); } 

  public static int countLoops(Map m)
  { // Assumes no self-loops in m
    Map m2 = new Map(); 
    int res = 0; 

    Map mnew = Map.compose(m,m); 
    while (mnew.size() > 0)
    { mnew.print_map(); 
      m2.clear(); 

      for (int i = 0; i < mnew.elements.size(); i++) 
      { Maplet mm = (Maplet) mnew.elements.get(i); 
        if (mm.source.equals(mm.dest)) 
        { System.out.println("Loop on " + mm.source); 
          res++; 
        }
        else 
        { m2.add_pair(mm.source, mm.dest); }
      }  
      mnew = Map.compose(m2,m); 
    } 
    return res; 
  } 
       
  public int countSelfMaps()
  { int res = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      if (mm.source != null && mm.source.equals(mm.dest)) 
      { res++; } 
    } 
    return res; 
  } 

  public Vector getSelfMaps()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      if (mm.source != null && mm.source.equals(mm.dest)) 
      { if (res.contains(mm.source)) { } 
        else 
        { res.add(mm.source); } 
      } 
    } 
    return res; 
  } 


  public static Map domainRestriction(Vector dom, Map m)
  { Vector elems = new Vector(); 
    for (int i = 0; i < m.elements.size(); i++) 
    { Maplet mm = (Maplet) m.elements.get(i); 
      if (dom.contains(mm.source)) 
      { elems.add(mm); } 
    } 
    return new Map(elems); 
  } 

  public Vector descendents(Entity e) 
  { Vector sups = e.getSuperclasses(); 
    Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      Entity esrc = (Entity) mm.source; 
      Vector edest = (Vector) mm.dest; 
      if (sups.contains(esrc))
      { if (e.isAbstract())
        { res = VectorUtil.union(res,edest); } 
        else 
        { for (int j = 0; j < edest.size(); j++)
          { Entity ed = (Entity) edest.get(j); 
            // esrc is abstract, but only concrete classes are
            // valid targets of e, if it is concrete
            res = VectorUtil.union(res,ed.getAllConcreteSubclasses());
          } 
        } 
      } 
    } // and all descendents of the edest
    return res; 
  } 

  public Vector ancestors(Entity e) 
  { Vector subs = e.getSubclasses(); 
    Vector res = new Vector(); 

    for (int i = 0; i < elements.size(); i++) 
    { Maplet mm = (Maplet) elements.get(i); 
      Entity esrc = (Entity) mm.source; 
      Vector edest = (Vector) mm.dest; 
      if (subs.contains(esrc))
      { res = VectorUtil.union(res,edest); } 
    } // and all ancestors of the edest
    return res; 
  } 

  public static Vector createUnitMaps(Vector dom, Vector rng) 
  { Vector res = new Vector(); 
    // all maps { x |-> y } for x in dom, y in rng. 

    for (int i = 0; i < dom.size(); i++) 
    { Object x = dom.get(i); 
      for (int j = 0; j < rng.size(); j++) 
      { Object y = rng.get(j); 
        Map m = new Map(); 
        m.add_pair(x,y); 
        res.add(m); 
      } 
    } 
    return res; 
  } 

  public static Vector allbijMaps(Vector dom, Vector rng) 
  { // dom.size <= rng.size
    Vector res = new Vector(); 

    if (dom.size() == 0) 
    { res.add(new Map());
      return res; 
    } 

    if (dom.size() == 1) 
    { res = createUnitMaps(dom,rng); 
      return res; 
    } 
    
    // dom.size() > 1
    for (int i = 0; i < dom.size(); i++) 
    { Object x = dom.get(i); 
      Vector dom1 = new Vector(); 
      dom1.addAll(dom); 
      dom1.remove(x); 
      Vector maps = Map.allbijMaps(dom1,rng); 
      res = VectorUtil.union(res,maps); 
      for (int k = 0; k < maps.size(); k++) 
      { Map mm = (Map) maps.get(k); 
        Vector mmran = mm.range(); 
        for (int j = 0; j < rng.size(); j++)
        { Object y = rng.get(j); 
          if (mmran.contains(y)) { } 
          else 
          { Map mmcopy = (Map) mm.clone(); 
            mmcopy.add_pair(x,y); 
            if (res.contains(mmcopy)) { } 
            else 
            { res.add(mmcopy); } 
          }  
        } 
      } 
    } 
    return res; 
  } 

  public static Vector allMaps(Vector dom, Vector rng) 
  { // dom.size <= rng.size

    Vector res = new Vector(); 

    if (dom.size() == 0) 
    { res.add(new Map());
      return res; 
    } 

    if (dom.size() == 1) 
    { res = createUnitMaps(dom,rng); 
      return res; 
    } 
    
    // dom.size() > 1
    for (int i = 0; i < dom.size(); i++) 
    { Object x = dom.get(i); 
      Vector dom1 = new Vector(); 
      dom1.addAll(dom); 
      dom1.remove(x); 
      Vector maps = Map.allMaps(dom1,rng); 
      res = VectorUtil.union(res,maps); 
      for (int k = 0; k < maps.size(); k++) 
      { Map mm = (Map) maps.get(k); 
        Vector mmran = mm.range(); 
        for (int j = 0; j < rng.size(); j++)
        { Object y = rng.get(j); 
          Map mmcopy = (Map) mm.clone(); 
          mmcopy.add_pair(x,y); 
          if (res.contains(mmcopy)) { } 
          else 
          { res.add(mmcopy); }   
        } 
      } 
    } 
    return res; 
  } 

  public static Vector allRelations(Vector dom, Vector rng) 
  { // dom.size <= rng.size

    Vector res = new Vector(); 
    res.addAll(Map.allMaps(dom,rng)); 
    Vector invmaps = Map.allMaps(rng,dom); 
    for (int i = 0; i < invmaps.size(); i++) 
    { Map mm = (Map) invmaps.get(i); 
      Map mmcopy = Map.inverse(mm); 
      if (res.contains(mmcopy)) { } 
      else 
      { res.add(mmcopy); }    
    } 
    return res; 
  } 

  public boolean isInjective()
  { boolean res = true;  
    // assuming domain has no duplicates

    for (int i = 0; i < elements.size(); i++)
    { Maplet m1 = (Maplet) elements.get(i); 
      for (int j = i+1; j < elements.size(); j++) 
      { Maplet m2 = (Maplet) elements.get(j); 
        if (m1.dest == null && m1.dest == m2.dest) 
        { return false; } 
        else if (m1.dest != null && m2.dest != null && m1.dest.equals(m2.dest))
        { return false; } 
      } 
    }
    return res; 
  }

  public static Vector submappings(Vector elems)
  { // All maps from domain(m) of maplets x -> y where y : m(x)
    Vector res = new Vector(); 

    if (elems.size() == 0) 
    { res.add(new Vector()); 
      return res; 
    }
  
    Maplet mm = (Maplet) elems.get(0); 
    Object x = mm.source; 
    Vector xvalues = (Vector) mm.dest;
    Vector rem = new Vector(); 
    rem.addAll(elems); 
    rem.remove(0); 
    Vector remmaps = submappings(rem); 
    for (int i = 0; i < remmaps.size(); i++) 
    { Vector rm = (Vector) remmaps.get(i); 
      for (int j = 0; j < xvalues.size(); j++) 
      { Object y = xvalues.get(j); 
        Vector newmap = new Vector(); 
        newmap.add(new Maplet(x,y)); 
        newmap.addAll(rm); 
        res.add(newmap); 
      } 
    } 
    return res; 
  } 

}




