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


public class DomainPattern extends Pattern
{ RelationDomain relationDomain;
  TemplateExp templateExpression;

  public DomainPattern(RelationDomain rd, TemplateExp t)
  { relationDomain = rd;
    templateExpression = t;
  }

  public Object clone()
  { TemplateExp te = (TemplateExp) ((ObjectTemplateExp) templateExpression).clone(); 
    return new DomainPattern(relationDomain, te); 
  } 

  public String toString()
  { return "" + templateExpression; }

  public Expression toGuardCondition(Vector bound, Expression contextObj, Expression post)
  { return templateExpression.toGuardCondition(bound, contextObj, post); }

  public Expression toSourceExpression(Vector bound, Expression contextObj)
  { return templateExpression.toSourceExpression(bound, contextObj); }

  public Expression toTargetExpression(Vector bound, Expression contextObj, Expression setting)
  { return templateExpression.toTargetExpression(bound, contextObj, setting); }

  public Expression toTargetExpressionOp(Vector bound, Expression contextObj, Expression setting)
  { return templateExpression.toTargetExpressionOp(bound, contextObj, setting); }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { return templateExpression.typeCheck(types,entities,contexts,env); } 

  public int complexity() 
  { return templateExpression.complexity(); } 

  public int epl() 
  { return templateExpression.epl(); } 

  public Vector getObjectTemplateAttributes(Vector vars)
  {  
    return templateExpression.getObjectTemplateAttributes(vars); 
  } 

  public Vector objectTemplateAttributes()
  {  
    return templateExpression.objectTemplateAttributes(); 
  } 

}

