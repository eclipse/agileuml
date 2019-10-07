import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Class diagram  */ 
 
public class Generalisation extends ModelElement
{ private Entity ancestor;
  private Entity descendent;
  private boolean realization = false; // true if ancestor.isInterface()

  public Generalisation(String nme) 
  { super(nme); } 

  public Generalisation(Entity ans, Entity dec)
  { super(dec.getName() + "_" + ans.getName());
    ancestor = ans;
    descendent = dec;
  }

  public void setAncestor(Entity e)
  { ancestor = e; } 

  public void setDescendent(Entity e)
  { descendent = e; } 

  public Entity getAncestor() { return ancestor; }

  public Entity getDescendent() { return descendent; }

  public void setRealization(boolean r)
  { realization = r; } 

  public boolean isRealization() 
  { return realization; } 

  public String toString()
  { return descendent.getName() + " ---|> " +
           ancestor.getName();
  }

  public void asTextModel(PrintWriter out) 
  { String nme = getName(); 
    out.println(nme + " : Generalization"); 
    out.println(nme + ".name = \"" + nme + "\"");
    out.println(nme + " : " + ancestor + ".specialization"); 
    out.println(nme + ".general = " + ancestor); 
    out.println(nme + " : " + descendent + ".generalization"); 
    out.println(nme + ".specific = " + descendent);  
  } 

  public void generateJava(java.io.PrintWriter out)
  { if (realization)
    { out.println(descendent.getName() + " implements " + 
                       ancestor.getName()); 
    }
    else 
    { out.println(descendent.getName() + " extends " + 
                  ancestor.getName());
    } 
  } 

  public String saveData()
  { return ""; } 
}

