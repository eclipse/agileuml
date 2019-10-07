import java.util.Vector; 


public class BBasicExpression extends BExpression
{ private String data;
  private Vector parameters; // for constants (functions)
  // private int umlkind; 

  public BBasicExpression(String d)
  { data = d; }

  public BBasicExpression(String d, Vector pars)
  { data = d; 
    parameters = pars; 
  } 

  // public void setUmlKind(int k) 
  // { umlkind = k; } 

  public Vector rd()
  { Vector res = new Vector(); 
    if (kind != Expression.VALUE) 
    { res.add(data); } // add all parameters? 
    return res; 
  } 

  public boolean setValued()
  { return false; }  // could be

  public BExpression simplify()
  { BExpression res = new BBasicExpression(data,parameters);
    res.setKind(kind); 
    return res; 
  }

  public String toString() 
  { if (parameters == null) 
    { return data; }
    else 
    { String res = data + "("; 
      for (int i = 0; i < parameters.size(); i++) 
      { res = res + parameters.get(i); 
        if (i < parameters.size() - 1)
        { res = res + ","; } 
      } 
      res = res + ")"; 
      return res; 
    } 
  } 
      
  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString())) { return newE; } 
    if (parameters != null) 
    { Vector newpars = new Vector(); 
      for (int i = 0; i < parameters.size(); i++) 
      { BExpression par = (BExpression) parameters.get(i); 
        BExpression newpar = par.substituteEq(oldE,newE); 
        newpars.add(newpar); 
      } 
      return new BBasicExpression(data,newpars); 
    } 
    return this; 
  } 
}

