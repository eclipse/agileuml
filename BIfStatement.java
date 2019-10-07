import java.util.Vector; 

/* Package: B AMN  */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BIfStatement extends BStatement
{ private BStatement ifpart;
  private BStatement elsepart;
  private BExpression cond;

  public BIfStatement(BExpression cnd, 
                      BStatement ip, BStatement ep)
  { ifpart = ip;
    elsepart = ep;
    cond = cnd;
  }

  public BIfStatement(BExpression cnd, 
                      BStatement ip)
  { ifpart = ip;
    elsepart = new BBasicStatement("skip");
    cond = cnd;
  }

  public BStatement getIf()
  { return ifpart; } 

  public BStatement getElse()
  { return elsepart; }

  public BExpression getCond()
  { return cond; } 

  public void setElse(BStatement stat)
  { elsepart = stat; }

  public void setIf(BStatement stat)
  { ifpart = stat; } 

  public String toString()
  { String res = "IF " + cond + "\n" +
                 "      THEN " + ifpart + "\n"; 
    if (elsepart != null) 
    { res = res + "      ELSE " + elsepart + "\n"; } 
    res = res + "      END";
    return res; 
  }

  public Vector wr() 
  { Vector res = new Vector(); 
    res.addAll(ifpart.wr()); 
    if (elsepart != null) 
    { return VectorUtil.union(res,elsepart.wr()); } 
    return res; 
  } 

  public Vector rd() 
  { Vector res = new Vector();
    res.addAll(cond.rd());  
    res = VectorUtil.union(res,ifpart.rd()); 
    if (elsepart != null) 
    { return VectorUtil.union(res,elsepart.rd()); } 
    return res; 
  } 

  public void extendWithCase(BStatement stat)
  { if (elsepart == null)
    { elsepart = stat; } 
    else 
    { if (elsepart instanceof BIfStatement)
      { ((BIfStatement) elsepart).extendWithCase(stat); }
      else if (stat instanceof BIfStatement) 
      { ((BIfStatement) stat).extendWithCase(elsepart); 
        elsepart = stat; 
      } 
    }  
  } 

  public BStatement substituteEq(String oldE, BExpression newE)
  { BExpression ncond = null; 
    BStatement nif = null; 
    BStatement nelse = null; 
    if (cond != null) 
    { ncond = cond.substituteEq(oldE,newE); }
    if (ifpart != null)  
    { nif = ifpart.substituteEq(oldE,newE); }
    if (elsepart != null) 
    { nelse = elsepart.substituteEq(oldE,newE); }
    return new BIfStatement(ncond,nif,nelse);
  }

  public BStatement simplify()
  { BExpression ncond = null; 
    BStatement nif = null; 
    BStatement nelse = null; 
    if (cond != null) 
    { ncond = cond.simplify(); }
    if (ifpart != null)  
    { nif = ifpart.simplify(); }
    if (elsepart != null) 
    { nelse = elsepart.simplify(); }
    return new BIfStatement(ncond,nif,nelse);
  }

  public BStatement normalise()
  { BStatement nif = null; 
    BStatement nelse = null; 
    if (ifpart != null)  
    { nif = ifpart.normalise(); }
    if (elsepart != null) 
    { nelse = elsepart.normalise(); }
    return new BIfStatement(cond,nif,nelse);
  }

  public BStatement seq2parallel()
  { BStatement nif = null; 
    BStatement nelse = null; 
    if (ifpart != null)  
    { nif = ifpart.seq2parallel(); }
    if (elsepart != null) 
    { nelse = elsepart.seq2parallel(); }
    return new BIfStatement(cond,nif,nelse);
  }

}

