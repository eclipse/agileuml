import java.util.Vector; 
import java.io.PrintWriter; 

/******************************
* Copyright (c) 2003-2025 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class Actor extends ModelElement
{ Vector dependenciesTo = new Vector(); // of Dependency to UseCase
  Vector dependenciesFrom = new Vector(); // of Dependency from UseCase
  
  public Actor(String nme)
  { super(nme); } 
  
  public void generateJava(PrintWriter out) { } 
  
  public void setType(Type t) { }  

  public Type getType() 
  { return null; } 

  public void addParameter(Attribute att) { } 

  public Vector getParameters() 
  { return new Vector(); }

  public String saveData()
  { return ""; } 

} 
