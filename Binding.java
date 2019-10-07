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


public class Binding
{ String propertyName; 
  Expression expression; 
  Expression oclExpression = null; 

  public Binding(String att, Expression exp)
  { propertyName = att;
    expression = exp;
  } 

  public String toString()
  { return propertyName + " <- " + expression; } 

  public Expression toExpression(String nme, Type typ, Vector types, Vector ents, Vector env,
                                 java.util.Map interp, UseCase uc) 
  { // nme.propertyName = expression, with conversions of thisModule

    Type propType = null; // Target type of the output variable being instantiated. 
    Type propElemType = null; 

    BasicExpression lhs = new BasicExpression(propertyName); 

    lhs.objectRef = new BasicExpression(nme); 

    Vector contexts = new Vector(); 
    expression.typeCheck(types,ents,contexts,env); 
    if (typ.isEntity())
    { Entity e = typ.getEntity(); 
      propType = e.getDefinedFeatureType(propertyName); 
      propElemType = e.getDefinedFeatureElementType(propertyName); 
      // System.out.println("DEFINED FEATURE TYPE OF " + propertyName + " IS " + propType + "(" + 
      //                    propElemType + ")"); 
    }  

    Expression replacedModule = expression.replaceModuleReferences(uc); 
    oclExpression = replacedModule.checkConversions(propType,propElemType,interp); 
    ModelElement met = ModelElement.lookupByName(nme, env);
    if (met != null) 
    { oclExpression = oclExpression.addPreForms(nme); } 
    return new BinaryExpression("=",lhs,oclExpression); 
  } 

  public Vector sourceTypesRead()
  { Vector res = new Vector(); 
    if (oclExpression != null) 
    { res = oclExpression.allReadFrame(); } 
    System.out.println("Read frame of " + oclExpression + " is " + res); 
    return res; 
  }     

  public boolean isAssociationUpdate(Entity ent)
  { if (ent.hasRole(propertyName))
    { return true; } 
    return false; 
  } 

  public int complexity() 
  { int result = 2 + expression.syntacticComplexity(); 
    return result; 
  } 

  public int cyclomaticComplexity() 
  { int result = expression.cyclomaticComplexity(); 
    return result; 
  } 

  public Vector operationsUsedIn()
  { return expression.allOperationsUsedIn(); } 

  public Vector getUses(String data)
  { return expression.getUses(data); } 
}