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


public class PrimitiveDomain extends Domain 
{ 
  public PrimitiveDomain(String nme, QVTRule r, TypedModel tm, Attribute v)
  { super(nme, r, tm,v);  }

  public Object clone()
  { PrimitiveDomain res = new PrimitiveDomain(name,rule,typedModel,rootVariable);
    return res; 
  } 

  public Domain overrideBy(Domain d) 
  { return d; } 

  public String toString()
  { String res = "  primitive domain " +
        " " + rootVariable.getName() + " : " + rootVariable.getType() + ";";
    return res;
  }

  public int complexity()
  { return rootVariable.syntacticComplexity(); } 

  public int cyclomaticComplexity()
  { return 0; } 

  public int epl()
  { return 1; } 

  public Vector operationsUsedIn()
  { return new Vector(); } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { return true; }  



  public Expression toSourceExpression(Vector bound)
  { return new BinaryExpression(rootVariable); 
    // not needed if variable is bound? 
  }

  public Expression toTargetExpression(Vector bound)
  { return new BasicExpression(true);
  }

  public Expression toSourceExpressionOp(Vector bound)
  { return new BasicExpression(true); }

  public Expression toTargetExpressionOp(Vector bound)
  { return new BasicExpression(true);
  }

  public Expression toGuardCondition(Vector bound, Expression e) 
  { return new BinaryExpression(rootVariable); 
    // not needed if variable is bound? 
  }

}
