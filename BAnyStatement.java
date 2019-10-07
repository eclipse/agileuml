import java.util.Vector; 

/* Copyright K. Lano 2003-2013 
   Package: B AMN 
*/ 

public class BAnyStatement extends BStatement
{ private Vector vars = new Vector(); // of String
  private BExpression pred;
  private BStatement code;

  public BAnyStatement(Vector vs, BExpression qual,
                       BStatement body)
  { vars = vs;
    pred = qual;
    code = body;
  }

  public Vector getVars()
  { return vars; } 

  public BExpression getWhere()
  { return pred; } 

  public BStatement getThen()
  { return code; } 

  public Vector wr() 
  { return code.wr(); } 

  public Vector rd() 
  { Vector res = pred.rd(); 
    res = VectorUtil.union(res,code.rd()); 
    res.removeAll(vars); 
    return res; 
  } 

  public String toString()
  { String res = "ANY ";
    for (int i = 0; i < vars.size(); i++)
    { String var = (String) vars.get(i);
      res = res + var;
      if (i < vars.size() - 1)
      { res = res + ", "; }
    }
    res = res + " WHERE " + pred + "\n" + "    THEN " +
          code + " END";
    return res;
  }

  public BStatement substituteEq(String oldE, BExpression newE)
  { if (vars.contains(oldE)) { return this; } 
    BExpression npred = pred.substituteEq(oldE,newE); 
    BStatement ncode = code.substituteEq(oldE,newE); 
    return new BAnyStatement(vars,npred,ncode); 
  } 

  public BStatement simplify()
  { BExpression npred = pred.simplify(); 
    BStatement ncode = code.simplify(); 
    return new BAnyStatement(vars,npred,ncode); 
  } 

  public BStatement seq2parallel()
  { BStatement ncode = code.seq2parallel(); 
    return new BAnyStatement(vars,pred,ncode); 
  } 

  public BStatement normalise()
  { BAnyStatement res; 
    BStatement stat = code.normalise(); 
    if (stat instanceof BAnyStatement) 
    { BAnyStatement inner = (BAnyStatement) stat; 
      Vector newvars = new Vector(); 
      newvars.addAll(vars); 
      newvars.addAll(inner.getVars()); 
      BExpression newpred = new BBinaryExpression("&",pred,inner.getWhere()); 
      res = new BAnyStatement(newvars,newpred,inner.getThen()); 
    } 
    else 
    { res = new BAnyStatement(vars,pred,stat); }
    return res;  
  } 
}

