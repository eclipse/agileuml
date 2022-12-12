import java.util.Vector;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BSetExpression extends BExpression
{ private Vector elements = new Vector();
  private boolean ordered = false; 

  public BSetExpression() { } 
  
  public BSetExpression(Vector vals)
  { elements = vals; }

  public BSetExpression(Vector vals,boolean ord)
  { this(vals); 
    ordered = ord; 
  }

  public boolean isSingleton()
  { return elements.size() == 1; }

  public boolean isEmpty()
  { return elements.size() == 0; }

  public boolean isOrdered()
  { return ordered; } 

  public void setOrdered(boolean ord)
  { ordered = ord; } 

  public BExpression getElement(int i)
  { return (BExpression) elements.get(i); }

  public void addElement(BExpression b) 
  { elements.add(b); } 

  public boolean hasElement(BExpression b)
  { boolean res = false; 
    for (int i = 0; i < elements.size(); i++) 
    { if (("" + elements.get(i)).equals("" + b))
      { return true; } 
    } 
    return res; 
  }

  public Vector rd()
  { Vector res = new Vector(); 
    for (int i = 0; i < elements.size(); i++) 
    { BExpression elem = (BExpression) elements.get(i); 
      res = VectorUtil.union(res,elem.rd()); 
    } 
    return res; 
  }

  public boolean isSubsetOf(BSetExpression bset)
  { boolean res = true; 
    for (int i = 0; i < elements.size(); i++) 
    { BExpression elem = (BExpression) elements.get(i); 
      if (bset.hasElement(elem)) { } 
      else 
      { return false; } 
    } 
    return res; 
  } 

  public boolean setValued()
  { return true; } 

  public BExpression simplify()
  { if (isSingleton())
    { BExpression elem = (BExpression) elements.get(0);
      BExpression esimp = elem.simplify();  
      if (esimp.setValued())
      { return esimp; } // cannot have sets of sets in OCL
    }
    Vector selemts = new Vector(); 
    for (int i = 0; i < elements.size(); i++)
    { BExpression e = (BExpression) elements.get(i);
      selemts.add(e.simplify());
    }
    return new BSetExpression(selemts,ordered); 
  }

  public String toString() 
  { String res; 
    if (ordered && elements.size() == 0) 
    { return "<>"; } 
    if (ordered)
    { res = "["; } 
    else
    { res = "{"; }

    for (int i = 0; i < elements.size(); i++)
    { res = res + elements.get(i);
      if (i < elements.size() - 1)
      { res = res + ", "; }
    }

    if (ordered) 
    { res = res + "]"; } 
    else 
    { res = res + "}"; }
    return res;  
  }

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    BSetExpression res = new BSetExpression(new Vector(),ordered);
    for (int i = 0; i < elements.size(); i++)
    { BExpression elem = (BExpression) elements.get(i);
      res.addElement(elem.substituteEq(oldE,newE)); 
    }
    return res; 
  }

  public boolean conflictsWith(BExpression e)
  { return false; } 
}

