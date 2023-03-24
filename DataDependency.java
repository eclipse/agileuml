import java.util.Vector; 

/******************************
* Copyright (c) 2003--2023 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class DataDependency
{ Vector sources = new Vector(); // of BasicExpression
  Vector targets = new Vector(); // of BasicExpression

  // Represent data dependency of constraints. 

  public DataDependency() { }

  public DataDependency(Vector src, Vector trg)
  { sources = src;
    targets = trg;
  }

  public DataDependency(BasicExpression e1, 
                        BasicExpression e2)
  { sources.add(e1);
    targets.add(e2);
  }

  public DataDependency union(DataDependency dd)
  { sources.addAll(dd.sources);
    targets.addAll(dd.targets);
    return this; 
  }

  public DataDependency composeWith(DataDependency dd)
  { // result has sources of this and targets of dd, if 
    // there is a common element in this.targets and dd.sources

    Vector inter = (Vector) targets.clone(); 
    inter.retainAll(dd.sources);
    if (inter.size() > 0)
    { return new DataDependency(sources,dd.targets); } 
    return null; 
  }

  public void addSource(BasicExpression be)
  { sources.add(be); }

  public void addTarget(BasicExpression be)
  { targets.add(be); }

  public void addSources(DataDependency srcs)
  { sources.addAll(srcs.sources); }

  public void addTargets(DataDependency targs)
  { targets.addAll(targs.sources); } 

  public String toString()
  { return sources + " --> " + targets; } 

  public boolean equals(Object obj)
  { if (obj instanceof DataDependency)
    { String s1 = toString(); 
      return s1.equals(obj + ""); 
    }
    return false; 
  }  

  public static Vector composeDataDependencySets(Vector dds1,
                                                 Vector dds2)
  { // Sequential composition of data dependencies
    // dds1->union(dds2) plus composed dependencies
  
    Vector res = new Vector(); 
    res.addAll(dds1); 
    res.addAll(dds2); 

    for (int i = 0; i < dds1.size(); i++) 
    { DataDependency dd1 = (DataDependency) dds1.get(i); 
      for (int j = 0; j < dds2.size(); j++) 
      { DataDependency dd2 = (DataDependency) dds2.get(j); 
        DataDependency dd3 = dd1.composeWith(dd2); 
        if (dd3 != null)
        { if (res.contains(dd3)) { } 
          else 
          { res.add(dd3); }
        }  
      } 
    } 
    return res; 
  } 

  public static void main(String[] args)
  { BasicExpression v1 = new BasicExpression("v1"); 
    BasicExpression v2 = new BasicExpression("v2");
    BasicExpression v3 = new BasicExpression("v3");

    DataDependency d1 = new DataDependency(v1,v2); 
    DataDependency d2 = new DataDependency(v2,v3); 

    DataDependency dd = d1.composeWith(d2);
    System.out.println(dd);

    Vector dds1 = new Vector(); 
    dds1.add(d1); 
    Vector dds2 = new Vector(); 
    dds2.add(d2); 
 
    Vector ss = 
      DataDependency.composeDataDependencySets(dds1,dds2); 
    System.out.println(ss);

  } 
}

