import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BChoiceStatement extends BStatement
{ private BStatement left; 
  private BStatement right; 

  public BChoiceStatement(BStatement l, BStatement r)
  { left = l; 
    right = r; 
  } 

  public String toString()
  { return "CHOICE " + left + " OR " + right + " END"; } 

  public Vector wr()
  { Vector res = new Vector(); 
    res.addAll(left.wr()); 
    return VectorUtil.union(res,right.wr()); 
  } 

  public Vector rd()
  { Vector res = new Vector(); 
    res.addAll(left.rd()); 
    return VectorUtil.union(res,right.rd()); 
  } 

  public BStatement substituteEq(String oldE, BExpression newE)
  { BStatement nl = left.substituteEq(oldE,newE);
    BStatement nr = right.substituteEq(oldE,newE);
    return new BChoiceStatement(nl,nr);
  }

  public BStatement simplify()
  { BStatement nl = left.simplify();
    BStatement nr = right.simplify();
    return new BChoiceStatement(nl,nr);
  }

  public BStatement normalise() 
  { BStatement ln = left.normalise(); 
    BStatement rn = right.normalise(); 
    return new BChoiceStatement(ln,rn); 
  } 

  public BStatement seq2parallel() 
  { BStatement ln = left.seq2parallel(); 
    BStatement rn = right.seq2parallel(); 
    return new BChoiceStatement(ln,rn); 
  } 
} 