import java.util.Vector; 
import java.io.*; 

/* Package: Class diagrams */
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class InheritanceFamily
{ private Vector maximals = new Vector(); // Entity
  private Vector members = new Vector();  // Entity
  // maximals <: members

  private boolean valid = true;

  public boolean hasMember(Entity e)
  { return members.contains(e); }

  public boolean hasMaximal(Entity e)
  { return maximals.contains(e); }

  public boolean isValid() { return valid; }

  public void invalidate() { valid = false; }

  public void addMaximal(Entity e)
  { maximals.add(e);
    members.add(e);
  }

  public void addMember(Entity e)
  { members.add(e); }

  public void replaceMaximal(Entity desc, Entity ansc)
  { maximals.remove(desc);
    addMaximal(ansc);
  }

  public void add(Entity desc, Entity ansc)
  { addMember(desc);
    addMaximal(ansc);
  }

  public void remove(Entity e)
  { maximals.remove(e); 
    members.remove(e); 
  } 

  public int size()
  { return members.size(); } 

  public String toString()
  { return "{maximals: " + maximals + " members: " +
           members + "}";
  }

  public void pureUnion(InheritanceFamily f,Entity e)
  { members = VectorUtil.union(members,f.members);
    maximals = VectorUtil.union(maximals,
                                f.maximals);
    maximals.remove(e);
  }

  public void impureUnion(InheritanceFamily f)
  { members = VectorUtil.union(members,f.members);
    maximals = VectorUtil.union(maximals,
                                f.maximals);
    valid = false;
  }

  public InheritanceFamily splitFamily(Entity d)
  { members.remove(d);
    InheritanceFamily f = new InheritanceFamily();
    f.addMaximal(d);
    // for each descendent of d, remove from this,
    // add to f.members:
    addDescendents(d,f);
    return f;
  }

  private void addDescendents(Entity e,
                              InheritanceFamily f)
  { Vector subs = e.getSubclasses();
    for (int i = 0; i < subs.size(); i++)
    { Entity sb = (Entity) subs.get(i);
      members.remove(sb);
      f.addMember(sb);
      addDescendents(sb,f);
    }
  }

  public void saveModelData(PrintWriter out)
  { String id = Identifier.nextIdentifier("inheritancefamily"); 
    out.println(id + " : InheritanceFamily"); 
    for (int i = 0; i < members.size(); i++) 
    { Entity e = (Entity) members.get(i); 
      out.println(e.getName() + " : " + id + ".members"); 
    } 
  } 
}

