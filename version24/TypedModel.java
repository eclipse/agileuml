import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public class TypedModel extends NamedElement
{ RelationalTransformation transformation;
  Vector entities = new Vector(); // of Entity

  public TypedModel(String nme, RelationalTransformation t)
  { super(nme);
    transformation = t; 
  }

  public void addEntity(Entity e)
  { entities.add(e); }

  public String toString() 
  { return getName(); } 
}
