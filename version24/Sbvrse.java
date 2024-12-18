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

public class Sbvrse 
{ public String definedTerm; 
  public String sourceInstance; 
  public String targetType;
  public String targetInstance; 
  public String sourceType; 
  public Expression condition = new BasicExpression(true); 
  public Expression succedent;  

  public Sbvrse() { }

  public void setDefinedTerm(String txt)
  { definedTerm = txt; sourceType = txt; } 

  public void setSourceInstance(String txt)
  { sourceInstance = txt; } 

  public void setTargetInstance(String trg) 
  { targetInstance = trg; } 

  public void setTargetType(String tt) 
  { targetType = tt; } 

  public void setSourceType(String tt) 
  { sourceType = tt; } 

  public void setSuccedent(Expression scc)
  { succedent = scc; } 

  public void setCondition(Expression scc)
  { condition = scc; } 

  public String toString()
  { String res = "It is necessary that each " + definedTerm + " instance " + 
                 sourceInstance + " maps to a " + 
                 targetType + " instance " + targetInstance + " such that " + 
                 succedent; 
    if (definedTerm.equals(sourceType)) { return res; } 
    res = res + " ; " + 
        " Each " + sourceType + " is considered to be a " + definedTerm + " if it has " + condition; 
    return res; 
  } 

  public Constraint generateConstraint(Vector types, Vector entities)
  { Entity sent = (Entity) ModelElement.lookupByName(sourceType, entities); 
    if (sent == null || succedent == null) { return null; } 

    BasicExpression sx = new BasicExpression(sent, sourceInstance); 
    Expression succ1 = (Expression) succedent.clone(); 
    Expression succ = succ1.dereference(sx);
    Entity tent = (Entity) ModelElement.lookupByName(targetType, entities); 
    if (tent == null) { return null; } 

    BasicExpression tx = new BasicExpression(tent, targetInstance);
    BasicExpression texp = new BasicExpression(tent);  
    Expression post = new BinaryExpression("#", 
                             new BinaryExpression(":", tx, texp), succ);   
    Constraint cons = new Constraint(condition, post); 
    cons.setOwner(sent); 
    return cons; 
  } 
} 