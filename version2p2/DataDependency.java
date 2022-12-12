import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class DataDependency
{ Vector sources = new Vector(); // of BasicExpression
  Vector targets = new Vector(); // of BasicExpression

  public DataDependency() { }

  public DataDependency(Vector src, Vector trg)
  { sources = src;
    targets = trg;
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
}

