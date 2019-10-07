/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class PreProperty
{ public String label; 
  public String name;
  public Entity owner = null; 
  public Type type = null; 
  public Entity entity2 = null; 
  public Type elementType = null; 
  public BehaviouralFeature op = null; 

  public PreProperty(String l)
  { label = l; }

  public void setName(String n) 
  { name = n; } 

  public void setOwner(Entity e)
  { owner = e; }  

  public void setEntity2(Entity e) 
  { entity2 = e; } 

  public void setType(Type t)
  { type = t; } 

  public void setElementType(Type t)
  { elementType = t; } 

  public void setOp(BehaviouralFeature bf)
  { op = bf; } 
} 