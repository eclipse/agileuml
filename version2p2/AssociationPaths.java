import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class AssociationPaths
{ static Vector pairs = new Vector(); // of StringPair
  static Map pathmap = new HashMap(); // String --> (String --> Vector)

  public AssociationPaths(Vector entities)
  { for (int i = 0; i < entities.size(); i++)
    { Entity e1 = (Entity) entities.get(i);
      for (int j = i+1; j < entities.size(); j++)
      { Entity e2 = (Entity) entities.get(j);
        String s1 = e1.getName();
        String s2 = e2.getName(); 
        StringPair ep = StringPair.getPair(s1,s2);
        pairs.add(ep);
        setPath(ep.s1,ep.s2,new Vector());
      }
    }
    System.out.println(pairs);
    System.out.println(pathmap); 
  }

  private static Vector getPath(String e1, String e2)
  { if (e1.compareTo(e2) < 0)
    { return getPath0(e1,e2); }
    else
    { return getPath0(e2,e1); }
  }

  private static Vector getPath0(String e1, String e2)
  { Map pm = (Map) pathmap.get(e1);
    if (pm == null)
    { return new Vector(); }
    else
    { return (Vector) pm.get(e2); } 
  }

  private static void setPath(String s1, String s2, Vector v)
  { if (s1.compareTo(s2) < 0)
    { setPath0(s1,s2,v); }
    else
    { setPath0(s2,s1,v); }
  }

  private static void setPath0(String s1, String s2, Vector v)
  { Map pm = (Map) pathmap.get(s1);
    if (pm == null)
    { pm = new HashMap();
      pm.put(s2,v);
      pathmap.put(s1,pm);
    }
    else
    { pm.put(s2,v); }
  }
  

  public String toString()
  { return pathmap + ""; } 

  private static Vector getNodes(Vector path)
  { Vector nodes = new Vector();
    for (int i = 0; i < path.size(); i++)
    { Association ast = (Association) path.get(i);
      nodes.add(ast.getEntity1().getName());
      nodes.add(ast.getEntity2().getName());
    }
    return nodes;
  }

  public static void addNewAssociation(Entity e1, Entity e2, Association ast)
  { // replace e1,e2 path by [ast] and update others
    String s1 = e1.getName();
    String s2 = e2.getName(); 
    Vector newpath = new Vector();
    newpath.add(ast);
    for (int i = 0; i < pairs.size(); i++)
    { StringPair ep = (StringPair) pairs.get(i);
      if (ep.matches(s1,s2))
      { setPath0(ep.s1,ep.s2,newpath); }
      else if (ep.s1.equals(s2)) // new e1 -> ep.e2 path candidate
      { Vector ep2 = getPath(s1,ep.s2);
        Vector eppath = getPath0(ep.s1,ep.s2); 
        if (eppath != null && eppath.size() > 0)
        { if (ep2 == null || ep2.size() == 0)  // create new one: [ast]^ep.path
          { Vector pp = new Vector();
            pp.add(ast);
            pp.addAll(eppath);
            setPath(s1,ep.s2,pp); 
            // mark these for update in paths as well
          }
          else if (eppath.size() + 1 < ep2.size())
          { Vector pp = new Vector(); 
            pp.add(ast); 
            pp.addAll(eppath); 
            setPath(s1,ep.s2,pp); 
          }
        }
      }  // and the other way round
      else if (ep.s1.equals(s1)) // new e2 -> ep.e2 path candidate
      { Vector ep2 = getPath(s2,ep.s2);
        Vector eppath = getPath0(ep.s1,ep.s2); 
        if (eppath != null && eppath.size() > 0)
        { if (ep2 == null || ep2.size() == 0)  // create new one: [ast]^ep.path
          { Vector pp = new Vector();
            pp.add(ast);
            pp.addAll(eppath);
            setPath(s2,ep.s2,pp); 
            // mark these for update in paths as well
          }
          else if (eppath.size() + 1 < ep2.size())
          { Vector pp = new Vector(); 
            pp.add(ast); 
            pp.addAll(eppath); 
            setPath(s2,ep.s2,pp); 
          }
        }
      }
      else if (ep.s2.equals(s2)) // new ep.e1 -> e1 path candidate
      { Vector ep2 = getPath(ep.s1,s1);
        Vector eppath = getPath0(ep.s1,ep.s2); 
        if (eppath != null && eppath.size() > 0)
        { if (ep2 == null || ep2.size() == 0)  // create new one: ep.path^[ast]
          { Vector pp = new Vector();
            pp.addAll(eppath);
            pp.add(ast); 
            setPath(ep.s1,s1,pp); 
            // mark these for update in paths as well
          }
          else if (eppath.size() + 1 < ep2.size())
          { Vector pp = new Vector();                         
            pp.addAll(eppath);
            pp.add(ast); 
            setPath(ep.s1,s1,pp); 
          }
        }
      }  // and the other way round
      else if (ep.s2.equals(s1)) // new ep.e1 -> e2 path candidate
      { Vector ep2 = getPath(ep.s1,s2);
        Vector eppath = getPath0(ep.s1,ep.s2); 
        if (eppath != null && eppath.size() > 0)
        { if (ep2 == null || ep2.size() == 0)  // create new one: ep.path^[ast]
          { Vector pp = new Vector();
            pp.addAll(eppath);
            pp.add(ast); 
            setPath(ep.s1,s2,pp); 
            // mark these for update in paths as well
          }
          else if (eppath.size() + 1 < ep2.size())
          { Vector pp = new Vector();  
            pp.addAll(eppath);
            pp.add(ast); 
            setPath(ep.s1,s2,pp); 
          }
        }
      }
      else
      { Vector eppath = getPath0(ep.s1,ep.s2); 
        replaceSubtraces(eppath,s1,s2,ep.s1,ep.s2,ast);
      }
    }

    for (int j = 0; j < pairs.size(); j++)
    { StringPair sp1 = (StringPair) pairs.get(j);
      Vector path1 = getPath0(sp1.s1,sp1.s2);
      if (path1.size() == 0) { continue; }

      for (int k = 1 + j; k < pairs.size(); k++)
      { StringPair sp2 = (StringPair) pairs.get(k); 
        Vector path2 = getPath0(sp2.s1,sp2.s2);
        if (path2.size() == 0) { continue; } 
        if ( (sp1.s2.equals(s1) && sp2.s1.equals(s2)) ||
             (sp1.s2.equals(s2) && sp2.s1.equals(s1)) )
        { if (sp1.s1.equals(sp2.s2)) { continue; }
          else  // paths must be disjoint --- no entities in common
          { Vector nodes1 = getNodes(path1);
            Vector nodes2 = getNodes(path2);
            nodes1.retainAll(nodes2);
            if (nodes1.size() != 0) { continue; } 
            Vector epath = getPath(sp1.s1,sp2.s2);
            if (epath == null || epath.size() == 0)
            { epath = new Vector();
              epath.addAll(path1);
              epath.add(ast);
              epath.addAll(path2);
              setPath(sp1.s1,sp2.s2,epath);
            }
            else if (path1.size() + 1 + path2.size() < epath.size())
            { epath = new Vector();
              epath.addAll(path1);
              epath.add(ast);
              epath.addAll(path2);
              setPath(sp1.s1,sp2.s2,epath); 
            }
          }
        }
      }
    }
  }

  private static void replaceSubtraces(Vector path,String s1,String s2,
                                       String es1, String es2, Association ast)
  { Vector before = new Vector();
    Vector between = new Vector();
    Vector after = new Vector();
    boolean s1seen = false;
    boolean s2seen = false;

    for (int i = 0; i < path.size(); i++)
    { Association aa = (Association) path.get(i);
      String e1 = aa.getEntity1().getName();
      String e2 = aa.getEntity2().getName();
      if (s1seen && s2seen)
      { after.add(aa); }
      else if (s1seen || s2seen)
      { between.add(aa); }
      else
      { before.add(aa); }
  
      if (s1.equals(e1) || s1.equals(e2))
      { s1seen = true; }
      else if (s2.equals(e1) || s2.equals(e2))
      { s2seen = true; }
    }

    if (s1seen && s2seen)
    { if (between.size() > 1)
      { // replace in path by ast
        path.clear();
        path.addAll(before);
        path.add(ast);
        path.addAll(after);
      }
    }
    // else if (s1seen)
  }

  public static Vector getNeededAssociations(Vector needed)
  { Vector assocs = new Vector(); 
    // assume 1st entity is the src, if any
    if (needed == null || needed.size() == 0) { return assocs; } 

    Entity src = (Entity) needed.get(0);
    String s1 = src.getName(); 
    needed.remove(0); 
    Vector found = new Vector(); 
    found.add(src); 
    for (int i = 0; i < needed.size(); i++) 
    { Entity e2 = (Entity) needed.get(i);
      String s2 = e2.getName(); 
      // add shortest path src --> e2 to assocs, 
      // add each entity on this path to found, 
      // quit when found.containsAll(needed)
      Vector path = getPath(s1,s2); 

      if (path == null) { } 
      else 
      { assocs = VectorUtil.union(assocs,path); }
 
      for (int j = 0; j < path.size(); j++) 
      { Association ast = (Association) path.get(j); 
        Entity ent1 = ast.getEntity1(); 
        Entity ent2 = ast.getEntity2(); 
        if (found.contains(ent1)) { } 
        else 
        { found.add(ent1); } 
        if (found.contains(ent2)) { } 
        else 
        { found.add(ent2); } 
      }
      if (found.containsAll(needed))
      { return assocs; } 
    }
    return assocs; 
  }
}



class EntityPair
{ Entity e1;
  Entity e2;

  EntityPair(Entity e, Entity f)
  { e1 = e;
    e2 = f;
  }

  public static EntityPair getPair(Entity e1, Entity e2)
  { if (e1.compareTo(e2) < 0)   
    { return new EntityPair(e1,e2); } 
    return new EntityPair(e2,e1); 
  } 

  public boolean equals(Object obj)
  { if (obj instanceof EntityPair)
    { EntityPair ep = (EntityPair) obj;
      if (ep.e1 == e1 && ep.e2 == e2)
      { return true; }
      if (ep.e2 == e1 && ep.e1 == e2)
      { return true; }
      return false;
    }
    return false;
  }

  public String toString()
  { return e1 + "," + e2; } 
}



class StringPair
{ String s1;
  String s2;

  StringPair(String e, String f)
  { s1 = e;
    s2 = f;
  }

  public static StringPair getPair(String e1, String e2)
  { if (e1.compareTo(e2) < 0)   
    { return new StringPair(e1,e2); } 
    return new StringPair(e2,e1); 
  } 

  public boolean matches(String ss1, String ss2)
  { return ((ss1.equals(s1) && ss2.equals(s2)) ||
            (ss1.equals(s2) && ss2.equals(s1)));
  }

  public boolean equals(Object obj)
  { if (obj instanceof StringPair)
    { StringPair ep = (StringPair) obj;
      if (ep.s1.equals(s1) && ep.s2.equals(s2))
      { return true; }
      return false;
    }
    return false;
  }

  public String toString()
  { return s1 + "," + s2; } 
}

