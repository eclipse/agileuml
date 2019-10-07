import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: ATL */ 

public class InPattern
{ Vector elements; // of InPatternElement

  public InPattern()
  { elements = new Vector(); } 

  public void setElements(Vector elems)
  { elements = elems; } 

  public void addElement(InPatternElement ipe) 
  { elements.add(ipe); } 

  public int size() 
  { return elements.size(); } 

  public String toString()
  { String res = "from "; 
    for (int i = 0; i < elements.size(); i++) 
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      res = res + "    " + ipe; 
      if (i < elements.size() - 1) 
      { res = res + ",\n"; } 
    } 
    return res; 
  } 

  public String firstType()  // Will become the owner of the constraint of the rule
  { if (elements.size() > 0) 
    { InPatternElement ipe = (InPatternElement) elements.get(0); 
      return ipe.getType(); 
    } 
    return null; 
  }

  public Entity firstEntity()
  { if (elements.size() > 0) 
    { InPatternElement ipe = (InPatternElement) elements.get(0); 
      Type typ = ipe.variable.getType();
      if (typ.isEntity())
      { return typ.getEntity(); }  
    } 
    return null; 
  }

  public boolean hasType(String typ) 
  { // typ occurs as a type of the pattern, or superclass of such a type

    for (int i = 0; i < elements.size(); i++)
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      if (typ.equals(ipe.variable.getType() + ""))
      { return true; } 
    } 
    return false; 
  } 

  public Expression toExpression(UseCase uc)
  { // pre: elements.size() > 0
    if (elements.size() == 0) 
    { return new BasicExpression(true); } 

    InPatternElement ipe0 = (InPatternElement) elements.get(0);
    Expression res = ipe0.condition; 
    for (int i = 1; i < elements.size(); i++)
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      res = Expression.simplify("&",res,ipe.getFullCondition(uc),null);  
    } 
    return res; 
  }  

  public Vector allVariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      if (ipe.variable != null) 
      { res.add(ipe.variable); } 
    } 
    return res; 
  }  

  public int complexity() 
  { int result = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      result = result + ipe.complexity(); 
    } 
    return result; 
  } 

  public int cyclomaticComplexity() 
  { int result = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      result = result + ipe.cyclomaticComplexity(); 
    } 
    return result; 
  } 

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      if (ipe.condition != null) 
      { res.addAll(ipe.condition.allOperationsUsedIn()); } 
    } 
    return res; 
  } 

  public Vector getUses(String data)
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { InPatternElement ipe = (InPatternElement) elements.get(i); 
      if (ipe.condition != null) 
      { res.addAll(ipe.condition.getUses(data)); } 
    } 
    return res; 
  } 

} 