import java.util.Vector; 

/* Package: B AMN */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BLoopStatement extends BStatement
{ private BExpression test;
  private BExpression invariant;
  private BExpression variant; 
  private BStatement body; 

  public BLoopStatement(BExpression tst, BExpression inv, BExpression var, BStatement bdy)
  { test = tst;
    invariant = inv;
    variant = var;
    body = bdy;  
  }

  public BExpression getTest()
  { return test; }

  public BExpression getInvariant()
  { return invariant; }

  public BExpression getVariant()
  { return variant; }

  public String toString()
  { return "WHILE " + test + "\n" + 
           "  DO " + body + "\n" + 
           "  INVARIANT " + invariant + "\n" + 
           "  VARIANT " + variant + "\n" + 
           "END"; 
  } 

  public BStatement substituteEq(String oldE, BExpression newE)
  { // BExpression nlhs = lhs.substituteEq(oldE,newE); 
    BExpression ntest = test.substituteEq(oldE,newE); 
    BExpression ninvariant = invariant.substituteEq(oldE,newE); 
    BExpression nvariant = variant.substituteEq(oldE,newE); 
    BStatement nbody = body.substituteEq(oldE,newE); 
    return new BLoopStatement(ntest,ninvariant,nvariant,nbody); 
  }

  public BStatement simplify()
  { // BExpression nlhs = lhs.substituteEq(oldE,newE); 
    BExpression ntest = test.simplify(); 
    BExpression ninvariant = invariant.simplify(); 
    BExpression nvariant = variant.simplify(); 
    BStatement nbody = body.simplify(); 
    return new BLoopStatement(ntest,ninvariant,nvariant,nbody); 
  }

  public Vector wr()
  { return body.wr(); } 

  public Vector rd()
  { Vector res = new Vector(); 
    res.addAll(test.rd()); 
    res = VectorUtil.union(res,body.rd()); 
    return res; 
  } 

  public BStatement seq2parallel()
  { return this; } 

  public BStatement normalise()
  { BStatement nbody = body.normalise(); 
    return new BLoopStatement(test,invariant,variant,nbody);
  }

}

