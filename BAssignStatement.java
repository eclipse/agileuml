import java.util.Vector; 

/* Copyright K. Lano 2003-2013
   Package: B AMN
*/ 

public class BAssignStatement extends BStatement
{ private BExpression lhs;
  private BExpression rhs;

  public BAssignStatement(BExpression l, BExpression r)
  { lhs = l;
    rhs = r;
  }

  public BExpression getRhs()
  { return rhs; }

  public BExpression getLhs()
  { return lhs; }

  public String toString()
  { return lhs + " := " + rhs; }

  public BStatement substituteEq(String oldE, BExpression newE)
  { // BExpression nlhs = lhs.substituteEq(oldE,newE); 
    BExpression nrhs = rhs.substituteEq(oldE,newE); 
    return new BAssignStatement(lhs,nrhs); 
  }

  public BStatement simplify()
  { // BExpression nlhs = lhs.substituteEq(oldE,newE); 
    BExpression nrhs = rhs.simplify(); 
    return new BAssignStatement(lhs,nrhs); 
  }

  public Vector wr()
  { Vector res = new Vector(); 

    if (lhs instanceof BBasicExpression)
    { res.add(lhs + ""); 
      return res; 
    }
    if (lhs instanceof BApplyExpression)
    { BApplyExpression bae = (BApplyExpression) lhs;
      String f = bae.getFunction();
      res.add(f); 
      return res; 
    } 
    return res; 
  } 

  public Vector rd()
  { Vector res = new Vector(); 
    res.addAll(rhs.rd()); 

    if (lhs instanceof BBasicExpression)
    { return res; }

    if (lhs instanceof BApplyExpression)
    { BApplyExpression bae = (BApplyExpression) lhs;
      BExpression arg = bae.getArgument();
      res = VectorUtil.union(res,arg.rd()); 
      return res; 
    } 
    return res; 
  } 

  public BStatement seq2parallel()
  { return this; } 

  public BStatement normalise()
  { if (lhs instanceof BBasicExpression)
    { return this; }
    if (lhs instanceof BApplyExpression)
    { BApplyExpression bae = (BApplyExpression) lhs;
      String f = bae.getFunction();
      BExpression arg = bae.getArgument();
      
      BExpression maplet = 
        new BBinaryExpression("|->",arg,rhs);
      BSetExpression bse = 
        new BSetExpression();
      bse.addElement(maplet);
      BExpression newlhs =
        new BBasicExpression(f);
      BExpression newrhs =
        new BBinaryExpression("<+",newlhs,bse);
      newrhs.setBrackets(true); 
      return new BAssignStatement(newlhs,newrhs);
    }
    return this;
  }

}

