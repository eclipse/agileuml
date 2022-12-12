import java.util.List; 
import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ClassDiagram
{ private List entities = new Vector();
  private List associations = new Vector();
  private List constraints = new Vector();
  private List types = new Vector();
  private List generalisations = new Vector();
  private List visuals = new Vector(); // VisualData

  public ClassDiagram(List es, List as, List cs,
                      List ts, List gs, List vs)
  { entities = es;
    associations = as;
    constraints = cs;
    types = ts;
    generalisations = gs;
    visuals = vs;
  }

  public Association getAssociation(String nme)
  { return (Association) 
      ModelElement.lookupByName(nme,(Vector) associations);
  }

  public Entity getEntity(String nme)
  { return (Entity) 
      ModelElement.lookupByName(nme,(Vector) entities);
  }

  public VisualData getVisualDataOf(ModelElement m)
  { for (int i = 0; i < visuals.size(); i++)
    { VisualData vd = (VisualData) visuals.get(i);
      if (vd.getModelElement() == m)  // equals ??
      { return vd; }
    }
    return null;
  }

  public void addEntity(Entity e)
  { entities.add(e); }

  public void removeAssociation(Association ast)
  { associations.remove(ast); }

  public void addAssociation(Association ast)
  { associations.add(ast); }

  public void addVisual(VisualData vd)
  { visuals.add(vd); }

  public void removeVisual(VisualData vd)
  { visuals.remove(vd); }

  public List getEntities()
  { return entities; }

  public List getAssociations()
  { return associations; }
  
}

