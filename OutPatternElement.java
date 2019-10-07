import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package ATL. */ 


public class OutPatternElement
{ Attribute variable; 
  Vector bindings; // Binding
  boolean isDefault = false;  // true for the first output element

  public OutPatternElement(Attribute att)
  { variable = att; 
    bindings = new Vector(); 
  } 

  public void setBindings(Vector elems)
  { bindings = elems; } 

  public void setDefault(boolean def) 
  { isDefault = def; } 

  public void addBinding(Binding ipe) 
  { bindings.add(ipe); } 

  public String toString()
  { String res = variable.getName() + " : MM2!" + variable.getType() + " (\n"; 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bn = (Binding) bindings.get(i); 
      res = res + "    " + bn; 
      if (i < bindings.size() - 1)
      { res = res + ",\n"; } 
    } 
    return res + " )"; 
  } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { for (int i = 0; i < bindings.size(); i++) 
    { Binding bn = (Binding) bindings.get(i);
      bn.expression.typeCheck(types,entities,contexts,env);  
    } 
    return true; 
  } 

  public String getType()
  { return variable.getType() + ""; } 

  public Entity getEntity()
  { Type t = variable.getType(); 
    if (t.isEntity())
    { return t.getEntity(); }
    return null; 
  }  

  public Expression toExpression(Vector types, Vector ents, Vector env, java.util.Map interp,
                                 UseCase uc) 
  { // typ->exists( var | var.$id = $id & bind1exp & ... & bindnexp )
    // thisModule.att replaced by uc.classifier.getName() + "." + att
    // thisModule.rule(pars) replaced by uc.classifier.getName() + "." + op(pars) 
    // for called rule 
    // thisModule.rule(pars) replaced by pars[1] + "." + op(pars.tail) 
    // for helper operation
 
    Type typ = variable.getType(); // Must be a target entity type. 
    String nme = variable.getName(); // If nme : env.name, need to @pre in binding RHS
    BasicExpression var = new BasicExpression(variable); 
    var.setType(typ); 
    var.setMultiplicity(ModelElement.ONE);  
    BasicExpression id; 
    
    if (isDefault) 
    { id = new BasicExpression("$id"); } 
    else 
    { id = new BasicExpression("\"" + nme + "_\" + $id"); } 
 
    id.setType(new Type("String",null)); 
    id.setMultiplicity(ModelElement.ONE); 
    BasicExpression varid = new BasicExpression("$id"); 
    varid.setType(new Type("String",null)); 
    varid.setMultiplicity(ModelElement.ONE); 
    varid.setObjectRef(var); 
    Expression res = new BasicExpression(true); 
    ModelElement met = ModelElement.lookupByName(nme, env);
    if (met == null) 
    { res = new BinaryExpression("=",varid,id); } 
 
    for (int i = 0; i < bindings.size(); i++)
    { Binding ipe = (Binding) bindings.get(i); 
      Expression bexp = ipe.toExpression(nme,typ,types,ents,env,interp,uc); 
      if (bexp != null) 
      { res = Expression.simplify("&",res,bexp,null); } 
    } 
    return res; 
    // return new BinaryExpression("#", new BinaryExpression(":",
    //                                    new BasicExpression(nme), new BasicExpression(typ + "")),
    //                                  res); 
  } 

  public Vector sourceTypesRead()
  { Vector res = new Vector(); 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bn = (Binding) bindings.get(i); 
      res.addAll(bn.sourceTypesRead()); 
    } 
    return res; 
  }

  public Vector slice()
  { // returns the bindings that update association ends 
    Vector res = new Vector(); 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bnd = (Binding) bindings.get(i); 
      if (variable.getType().isEntity() && 
          bnd.isAssociationUpdate(variable.getType().getEntity()))
      { res.add(bnd); } 
    } 
    bindings.removeAll(res); 
    return res; 
  } 

  public int complexity() 
  { int result = 2 + variable.getType().complexity(); 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bnd = (Binding) bindings.get(i); 
      result = result + bnd.complexity(); 
    } 
    return result; 
  }     

  public int cyclomaticComplexity() 
  { int result = 0; 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bnd = (Binding) bindings.get(i); 
      result = result + bnd.cyclomaticComplexity(); 
    } 
    return result; 
  }     

  public Vector operationsUsedIn() 
  { Vector result = new Vector(); 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bnd = (Binding) bindings.get(i); 
      result.addAll(bnd.operationsUsedIn()); 
    } 
    return result; 
  }     

  public Vector getUses(String data) 
  { Vector result = new Vector(); 
    for (int i = 0; i < bindings.size(); i++) 
    { Binding bnd = (Binding) bindings.get(i); 
      result.addAll(bnd.getUses(data)); 
    } 
    return result; 
  }     

}