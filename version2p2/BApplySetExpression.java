import java.util.Vector;

public class BApplySetExpression extends BExpression
{ private String function;
  private BExpression func; 
  private BExpression arg;

  public BApplySetExpression(String f, BExpression a)
  { function = f;
    func = new BBasicExpression(f); 
    arg = a;
  }

  public BApplySetExpression(BExpression f, BExpression a)
  { func = f;
    function = "" + f; 
    arg = a;
  }

  public Vector rd()
  { Vector res = new Vector(); 
    res.addAll(func.rd()); 
    return VectorUtil.union(res,arg.rd()); 
  } 

  public boolean setValued()
  { return true; } 

  public void setMultiplicity(int m) 
  { multiplicity = m; } 

  public BExpression simplify()
  { BExpression sa = arg.simplify(); 
    if (sa instanceof BSetExpression)
    { BSetExpression se = (BSetExpression) sa;
      if (se.isSingleton())
      { BExpression arg1 = se.getElement(0);
        Vector rr = new Vector();
        if (arg1.setValued())
        { // f[{arg1}] is f[arg]
          BApplySetExpression newaps = new BApplySetExpression(func,arg1); 
          newaps.setMultiplicity(multiplicity); 
          if (multiplicity == ModelElement.ONE)
          { return newaps; }
          else 
          { return new BUnaryExpression("union",newaps); } 
        } // else, its {f(arg1)} 
        BApplyExpression ap = 
          new BApplyExpression(func,arg1);
        ap.setMultiplicity(multiplicity);
       // if (multiplicity == ModelElement.ONE) 
       // {
          rr.add(ap);
          return new BSetExpression(rr); 
       // }  // or just ap if setValued anyway
       // else 
       // { return ap; } 
      }
      else if (se.isEmpty())
      { return new BSetExpression(new Vector()); }
    }
    BApplySetExpression res = new BApplySetExpression(func,sa);
    res.setMultiplicity(multiplicity); 
    return res; 
  }

  public String toString() 
  { return function + "[" + arg + "]"; }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    BExpression argE = arg.substituteEq(oldE,newE); 
    BExpression res = new BApplySetExpression(function,argE); 
    res.setMultiplicity(multiplicity); 
    return res; 
  } 
}  // and in func

