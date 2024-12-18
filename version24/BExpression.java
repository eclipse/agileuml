import java.util.Vector;

/* Package: B */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public abstract class BExpression
{ protected boolean needsBracket = false; 
  protected int multiplicity = ModelElement.ONE;
  protected int kind = Expression.UNKNOWN; 

  public final static int ROOT = 0; 
  public final static int SUBCLASS = 1; 
  public final static int FEATURE = 2; 
  public final static int LOGICAL = 3; 

  protected int category = LOGICAL; 

  public void setKind(int k)
  { kind = k; } 

  public int getKind()
  { return kind; } 

  public void setCategory(int c)
  { category = c; } 

  public int getCategory()
  { return category; } 

  public abstract boolean setValued(); 

  public abstract BExpression simplify(); 

  public BExpression substituteEq(String oldE, BExpression newE)
  { return this; }  // default

  public void setMultiplicity(int m)
  { multiplicity = m; }

  public static BExpression makeSet(BExpression e)
  { if (e.setValued()) { return e; }
    Vector elems = new Vector();
    elems.add(e);
    return new BSetExpression(elems);
  }

  public static BExpression unmakeSet(BExpression e)
  { if (e == null) { return e; }  // should not occur
    if (e instanceof BSetExpression)
    { BSetExpression bset = (BSetExpression) e; 
      if (bset.isSingleton())
      { BExpression res = bset.getElement(0); 
        return res; 
      } 
    }
    return e; 
  } 

  public static BExpression unSet(BExpression e)
  { if (e == null) { return e; }
    if (e instanceof BSetExpression)
    { BSetExpression se = (BSetExpression) e;
      if (se.isSingleton())
      { BExpression res = se.getElement(0);
        System.out.println(res + " IS SET-VALUED: " + res.setValued()); 
        if (res.setValued())
        { return res; }
      }
    }
    return e;
  }

  public void setBrackets(boolean brack)
  { needsBracket = brack; } 

  public boolean conflictsWith(final BExpression e)
  { return false; } // default 

  public boolean conflictsWith(String op, BExpression el,
                               BExpression er)
  { return false; } 

  public abstract Vector rd(); 
}


