import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public abstract class TemplateExp
{ Attribute bindsTo;
  Expression where;

  public TemplateExp(Attribute v, Expression e)
  { bindsTo = v;
    where = e;
  }

  public abstract boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env);  

  public abstract Expression toGuardCondition(Vector bound, Expression contextObj, Expression post);

  public abstract Expression toSourceExpression(Vector bound, Expression contextObj);

  public abstract Expression toTargetExpression(Vector bound, Expression contextObj, Expression setting);

  public abstract Expression toTargetExpressionOp(Vector bound, Expression contextObj, Expression setting);

  public abstract Vector getObjectTemplateAttributes(Vector vars);  

  public abstract Vector objectTemplateAttributes();  

  public abstract int complexity(); 

  public int cyclomaticComplexity()
  { if (where != null) 
    { return where.cyclomaticComplexity(); } 
    return 0; 
  }  

  public abstract Vector operationsUsedIn(); 

  public abstract int epl(); 

  public void setSource()
  { if (bindsTo != null) 
    { bindsTo.addStereotype("source"); } 
  }  
}
