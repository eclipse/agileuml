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


public class InPatternElement
{ Attribute variable; 
  Expression condition; 

  public InPatternElement(Attribute att, Expression exp)
  { variable = att;
    condition = exp;
  } 

  public String toString()
  { String res = variable.getName() + " : MM1!" + variable.getType(); 
    if (condition == null || "true".equals(condition + ""))
    { return res; } 
    return res + " ( " + condition + " )"; 
  } 

  public void setCondition(Expression cond)
  { condition = cond; } 

  public Expression getFullCondition(UseCase uc)
  { BasicExpression be = new BasicExpression(variable.getName()); 
    be.setType(variable.getType()); 
    BasicExpression betype = new BasicExpression(variable.getType() + ""); 
    Expression res = new BinaryExpression(":", be, betype);
    Expression newcond = condition.replaceModuleReferences(uc);  
    return Expression.simplify("&", res, newcond, null); 
  } // need to convert the condition to remove "thisModule"

  public String getType()
  { return variable.getType() + ""; } 

  public int complexity()
  { int result = 2 + variable.getType().complexity(); 
    if (condition != null) 
    { result = result + condition.syntacticComplexity(); } 
    return result; 
  } 

  public int cyclomaticComplexity()
  { int result = 0; 
    if (condition != null) 
    { result = result + condition.cyclomaticComplexity(); } 
    return result; 
  } 
}