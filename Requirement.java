import java.io.*; 
import java.util.Vector; 

/* Package: Requirements */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class Requirement extends ModelElement
{ public String id = "F1"; 
  public String text; 
  public String requirementKind = "functional"; 
  private boolean product = true; 
  private boolean local = false; 
  private Vector scenarios = new Vector(); // of Scenario

  public Requirement(String nme, String i, String txt, String knd)
  { super(nme); 
    id = i ; 
    text = txt; 
    requirementKind = knd; 
  }

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


  public String toString()
  { return "Requirement: " + name + " " + id + "\n" + 
           text + "\n" + 
           requirementKind + "\n" + 
           local + "\n" + 
           scenarios; 
  } 

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
  { String res = name + " " + id + " \n" + text + "\n" + requirementKind + "\n" + 
           local + "\n\n";
    for (int i = 0; i < scenarios.size(); i++) 
    { Scenario sc = (Scenario) scenarios.get(i); 
      res = res + sc.saveData(name) + "\n"; 
    } 
    return res; 
  } 

} 