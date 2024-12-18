import java.util.Vector; 

/* Copyright K. Lano 2003-2013
   Package: B AMN */ 

public class BApplyExpression extends BExpression
{ private String function;
  private BExpression func; // function = func.toString()
  private BExpression arg; // may be list
  private Vector args; 
  
  public BApplyExpression(String f, BExpression a)
  { function = f;
    func = new BBasicExpression(f); 
    arg = a;
  }

  public BApplyExpression(BExpression f, BExpression a)
  { function = f + "";
    func = f; 
    arg = a;
  }

  public BApplyExpression(BExpression f, Vector arglist)
  { function = f + "";
    func = f; 
    args = arglist;
    String argstring = ""; 
    for (int i = 0; i < arglist.size(); i++) 
    { argstring = argstring + arglist.get(i); 
      if (i < arglist.size() - 1)
      { argstring = argstring + ","; } 
    } 
    arg = new BBasicExpression(argstring); 
  } 
 
  public Vector rd()
  { Vector res = func.rd(); 
    res = VectorUtil.union(res,arg.rd()); 
    return res; 
  } 

  public String getFunction()
  { return function; }

  public BExpression getArgument()
  { return arg; }

  public void setMultiplicity(int m)
  { multiplicity = m; } 

  public boolean setValued()
  { if (multiplicity != ModelElement.ONE)
    { return true; }
    else
    { return arg.setValued(); }
  }

  public BExpression simplify()
  { if (arg == null)
    { System.err.println("Function: " + function + " with null argument");
      return null;
    }

    if (func instanceof BBinaryExpression)  // (f <+ {x |-> b})(x) is b
    { BBinaryExpression bbe = (BBinaryExpression) func; 
      if (bbe.getOperator().equals("<+") && (bbe.getRight() instanceof BSetExpression))
      { BSetExpression update = (BSetExpression) bbe.getRight(); 
        BExpression elem0 = update.getElement(0); 
        if (elem0 != null && (elem0 instanceof BBinaryExpression)) 
        { BBinaryExpression belem0 = (BBinaryExpression) elem0; 
          if (belem0.getOperator().equals("|->") && (arg + "").equals(belem0.getLeft() + "")) 
          { return belem0.getRight(); } 
        } 
      } 
    } 

    BExpression sarg = arg.simplify();
    if (sarg instanceof BSetExpression)
    { BSetExpression se = (BSetExpression) sarg; 
      if (se.isSingleton())
      { BExpression argp = se.getElement(0); 
        // Vector vv = new Vector(); 
        BApplyExpression newapp = new BApplyExpression(func,argp);
        newapp.setMultiplicity(multiplicity); 
        // vv.add(newapp); 
        // return new BSetExpression(vv);
        return newapp; 
      } // f(set) is never meant for real in UML-RSDS
    }
    return 
      new BApplyExpression(func,sarg); 
  }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    BExpression argE = arg.substituteEq(oldE,newE); 
    BExpression funcE = func.substituteEq(oldE,newE);
    if (funcE instanceof BBasicExpression) {} 
    else 
    { funcE.setBrackets(true); }   
    BExpression res = new BApplyExpression(funcE,argE); 
    res.setMultiplicity(multiplicity); 
    return res; 
  } 

  public String toString() 
  { if (function == null)
    { return func + "(" + arg + ")"; }
    return function + "(" + arg + ")";
  }
}

