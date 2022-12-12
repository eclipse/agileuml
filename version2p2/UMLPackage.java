import java.util.Vector; 
import java.io.*; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Class Diagram */ 


public class UMLPackage extends ModelElement
{ Vector contents = new Vector(); 

  public UMLPackage(String nme)
  { super(nme); }  

  public void addModelElement(ModelElement me)
  { contents.add(me); } // type, class or usecase

  public String generateJava()
  { return "package " + name + "{ }"; } 

  public void generateJava(PrintWriter out)
  { out.println("package " + name + "{ }"); } 

  public void setType(Type t) { } 

  public Type getType() { return null; } 

  public void addParameter(Attribute att) 
  { } 

  public Vector getParameters()
  { return new Vector(); } 
} 

