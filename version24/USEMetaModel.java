import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class USEMetaModel 
{ String name = ""; 
  private Vector classes = new Vector(); // of USEClassDeclaration
  private Vector associations = new Vector(); // of USEAssociationDeclaration

  public USEMetaModel(String nme)
  { name = nme; } 

  public void addClass(USEClassDeclaration cd)
  { classes.add(cd); } 

  public void addAssociation(USEAssociationDeclaration ad)
  { associations.add(ad); } 
}

class USEClassDeclaration
{ String name = ""; 

  public USEClassDeclaration(String nme)
  { name = nme; } 
}  

class USEAssociationDeclaration
{ String name = ""; 
  Vector roles = new Vector();  // of USERoleDeclaration

  public USEAssociationDeclaration(String nme)
  { name = nme; } 

  public void addRole(String entity, String mult, String rolename)
  { USERoleDeclaration rd = new USERoleDeclaration(entity,mult,rolename); 
    roles.add(rd); 
  } 

  public void addRole(USERoleDeclaration rd)
  { roles.add(rd); } 
}

class USERoleDeclaration
{ String entity = ""; 
  String mult = "*"; // default
  String rolename = ""; 

  public USERoleDeclaration(String ent, String mul, String rolen)
  { entity = ent; mult = mul; rolename = rolen; } 
} 
 