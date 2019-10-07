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

public class OutPattern
{ Vector elements; // OutPatternElement

  public OutPattern()
  { elements = new Vector(); } 

  public void setElements(Vector elems)
  { elements = elems; 
    if (elements.size() > 0) 
    { OutPatternElement e1 = (OutPatternElement) elements.get(0); 
      e1.setDefault(true); 
    }
  }  

  public OutPatternElement getElement(int i) 
  { if (i < elements.size()) 
    { return (OutPatternElement) elements.get(i); } 
    return null; 
  } 

  public void addElement(OutPatternElement ipe) 
  { elements.add(ipe); } 

  public void setElement(OutPatternElement ope)
  { elements.clear(); 
    elements.add(ope); 
  } 

  public int size() 
  { return elements.size(); } 

  public Vector allVariables()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ope = (OutPatternElement) elements.get(i); 
      res.add(ope.variable); 
    } 
    return res; 
  } 

  public String toString()
  { String res = "to "; 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement pe = (OutPatternElement) elements.get(i); 
      res = res + pe; 
      if (i < elements.size() - 1) 
      { res = res + ",\n    "; } 
    } 
    return res; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement pe = (OutPatternElement) elements.get(i); 
      pe.typeCheck(types,entities,contexts,env); 
    } 
    return true; 
  } 

  public Expression toExpression(Vector types, Vector ents, Vector env, 
                                 java.util.Map interp, UseCase uc) 
  { Expression res = new BasicExpression(true); 
    for (int i = 0; i < elements.size(); i++)
    { OutPatternElement ipe = (OutPatternElement) elements.get(i); 
      Expression exp = ipe.toExpression(types,ents,env,interp,uc);
      if (exp != null) 
      { res = Expression.simplify("&",res,exp,null); } 
    } 
    return res; 
  } 

  public Vector sourceTypesRead()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ipe = (OutPatternElement) elements.get(i); 
      res.addAll(ipe.sourceTypesRead()); 
    } 
    return res; 
  } 

  public boolean hasType(String typ) 
  { // typ occurs as an output type of the pattern, or superclass of such a type

    for (int i = 0; i < elements.size(); i++)
    { OutPatternElement ipe = (OutPatternElement) elements.get(i); 

      // System.out.println("Typ: " + typ + " My output type: " + ipe.variable.getType()); 

      if (typ.equals(ipe.variable.getType() + ""))
      { return true; } 
    } 
    return false; 
  } 

  public boolean hasVariable(String var) 
  { // var occurs as an output variable

    for (int i = 0; i < elements.size(); i++)
    { OutPatternElement ipe = (OutPatternElement) elements.get(i); 

      if (var.equals("\"" + ipe.variable.getName() + "\""))
      { return true; } 
    } 
    return false; 
  } 

  public OutPattern slice()
  { OutPattern res = new OutPattern(); 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ep = (OutPatternElement) elements.get(i); 
      Vector bnds = ep.slice(); 
      OutPatternElement newep = new OutPatternElement(ep.variable);
      newep.setBindings(bnds); 
      res.addElement(newep); 
    } // and remove bnds from ep.bindings
    return res; 
  }  
      
  public int complexity() 
  { int result = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ep = (OutPatternElement) elements.get(i); 
      result = result + ep.complexity(); 
    } 
    return result; 
  } 

  public int cyclomaticComplexity() 
  { int result = 0; 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ep = (OutPatternElement) elements.get(i); 
      result = result + ep.cyclomaticComplexity(); 
    } 
    return result; 
  } 

  public Vector operationsUsedIn() 
  { Vector result = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ep = (OutPatternElement) elements.get(i); 
      result.addAll(ep.operationsUsedIn()); 
    } 
    return result; 
  } 

  public Vector getUses(String data) 
  { Vector result = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { OutPatternElement ep = (OutPatternElement) elements.get(i); 
      result.addAll(ep.getUses(data)); 
    } 
    return result; 
  } 
}