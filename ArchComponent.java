import java.io.*; 
import java.util.Vector; 

/* Package: Architecture */ 
/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ArchComponent extends ModelElement
{ public String id = "F1"; 
  public String text = ""; 
  public String requirementKind = "functional"; 
  private boolean product = true; 
  private boolean local = false; 
  private Vector providedInterfaces = new Vector(); // of Entity
  private Vector requiredInterfaces = new Vector(); // of Entity
  private Vector scenarios = new Vector();
  private Type type = null;  


  public ArchComponent(String nme, String i, String txt, String knd)
  { super(nme); 
    id = i ; 
    text = txt; 
    requirementKind = knd; 
  }

  public ArchComponent(String nme)
  { super(nme); } 

  public String getText() 
  { return text; } 
  
  public String getKind()
  { return requirementKind; } 

  public String getScope() 
  { if (local) 
    { return "local"; } 
    return "global"; 
  } 

  public boolean isGlobal()
  { if (local) 
    { return false; }
    return true; 
  }  

  public boolean hasScenarios()
  { return scenarios.size() > 0; } 

  public Vector getScenarios()
  { return scenarios; } 

  public void setText(String txt)
  { text = txt; } 

  public void setKind(String k)
  { requirementKind = k; } 

  public void setScope(String k)
  { if ("local".equals(k))
    { local = true; } 
    else 
    { local = false; }
  } 
 
  public void addScenario(Scenario sc)
  { scenarios.add(sc); } 

  public void addProvidedInterface(Entity pintf)
  { providedInterfaces.add(pintf); } 

  public void addRequiredInterface(Entity pintf)
  { requiredInterfaces.add(pintf); } 


  public String toString()
  { return "Component: " + name + "\n"; } 

  public Vector getParameters()
  { return new Vector(); } 

  public void addParameter(Attribute att) 
  { } 

  public Type getType() { return type; }

  public void setType(Type t) { type = t; }  

  public void generateJava(PrintWriter out) { } 

  public UseCase generateUseCase(Vector types, Vector entities) 
  { UseCase uc = new UseCase(name); 
    for (int i = 0; i < scenarios.size(); i++) 
    { Scenario sc = (Scenario) scenarios.get(i); 
      Constraint p = sc.getConstraint(types, entities); 
      uc.addPostcondition(p); 
    } 
    return uc; 
  } 

  public String saveData()
  { String res = name + "\n\n\n";
    // for (int i = 0; i < scenarios.size(); i++) 
    // { Scenario sc = (Scenario) scenarios.get(i); 
    //   res = res + sc.saveData(name) + "\n"; 
    // } 
    return res; 
  } 

  public void saveModelData(PrintWriter out)
  { out.println(name + " : ArchComponent"); 
    // out.println(name + ".id = \"" + id + "\""); 
    // out.println(name + ".text = \"" + text + "\""); 
    // out.println(name + ".requirementKind = \"" + requirementKind + "\""); 
    // out.println(name + ".localScope = " + local);
 
    /* for (int i = 0; i < scenarios.size(); i++) 
    { Scenario sc = (Scenario) scenarios.get(i); 
      sc.saveModelData(out,name); 
    } */ 
    
  } 

} 