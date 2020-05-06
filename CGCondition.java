/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Code Generation */ 

import java.util.Vector; 

public class CGCondition
{ String stereotype = "";
  String variable = "";
  boolean positive = true;

  public CGCondition()
  { } 

  public CGCondition(String prop, String var)
  { stereotype = prop;
    variable = var;
  }

  public void setVariable(String v) 
  { variable = v; } 

  public void setStereotype(String st) 
  { stereotype = st; } 

  public void setPositive()
  { positive = true; }

  public void setNegative()
  { positive = false; }

  public String toString()
  { String res = variable; 
    if (positive) { } 
    else  
    { res = res + " not"; } 
    return res + " " + stereotype; 
  } 

  public static boolean conditionsSatisfied(Vector conditions, Vector args) 
  { boolean res = true; 
    for (int i = 0; i < args.size(); i++) 
    { Object m = args.get(i); 
      String var = "_" + (i+1);
 
      for (int j = 0; j < conditions.size(); j++) 
      { CGCondition cond = (CGCondition) conditions.get(j); 
        if (cond.variable != null && var.equals(cond.variable))
        { if (m instanceof Type && 
              cond.conditionSatisfied((Type) m)) 
          { }
          else if (m instanceof Expression && 
                   cond.conditionSatisfied((Expression) m))
          { } 
          else if (m instanceof Statement && 
                   cond.conditionSatisfied((Statement) m)) 
          { } 
          else if (m instanceof Attribute && 
                    cond.conditionSatisfied((Attribute) m)) 
          { } 
          else if (m instanceof ModelElement && 
                   cond.stereotypeConditionSatisfied((ModelElement) m))
          { } 
          else 
          { return false; } 
        } 
      } 
    } 
    return res; 
  } 

  public boolean stereotypeConditionSatisfied(ModelElement m)
  { if (m.hasStereotype(stereotype))
    { return positive; }
    return false; 
  } 

  public boolean conditionSatisfied(Type t)
  { if ("class".equals(stereotype.toLowerCase()) && t.isEntityType())
    { return positive; }
    if ("enumerated".equals(stereotype.toLowerCase()) && t.isEnumeratedType())
    { return positive; }
    if ("collection".equals(stereotype.toLowerCase()) && t.isCollectionType())
    { return positive; }
    if ("sequence".equals(stereotype.toLowerCase()) && t.isSequenceType())
    { return positive; }
    if ("set".equals(stereotype.toLowerCase()) && t.isSetType())
    { return positive; }
    if ("class".equals(stereotype.toLowerCase()) && !(t.isEntityType()))
    { return !positive; }
    if ("enumerated".equals(stereotype.toLowerCase()) && !(t.isEnumeratedType()))
    { return !positive; }
    if ("collection".equals(stereotype.toLowerCase()) && !(t.isCollectionType()))
    { return !positive; }
    if ("sequence".equals(stereotype.toLowerCase()) && !(t.isSequenceType()))
    { return !positive; }
    if ("set".equals(stereotype.toLowerCase()) && !(t.isSetType()))
    { return !positive; }
    return false;
  }

  public boolean conditionSatisfied(Attribute a)
  { if ("primary".equals(stereotype.toLowerCase()) && a.isPrimaryAttribute())
    { return positive; }
    if (a.hasStereotype(stereotype))
    { return positive; }
    return false;
  }

  public boolean conditionSatisfied(Expression e)
  { Type t = e.getType();

    if (t == null) 
    { System.err.println("!! ERROR: null type in: " + e); 
      return false; 
    } 

    String tname = t.getName(); 

    if ("Set".equals(stereotype))
    { if (positive)
      { return "Set".equals(tname); }
      else
      { return !("Set".equals(tname)); }
    }
    else if ("Sequence".equals(stereotype))
    { if (positive)
      { return "Sequence".equals(tname); }
      else
      { return !("Sequence".equals(tname)); }
    }
    else if ("collection".equals(stereotype.toLowerCase()))
    { if (positive)
      { return ("Set".equals(tname) || "Sequence".equals(tname)); }
      else
      { return !("Set".equals(tname)) && !("Sequence".equals(tname)); }
    }
    else if ("String".equals(stereotype))
    { if (positive)
      { return "String".equals(tname); }
      else
      { return !("String".equals(tname)); }
    }
    else if ("numeric".equals(stereotype))
    { if (positive)
      { return t.isNumericType(); }
      else
      { return !(t.isNumericType()); }
    }
    else if ("object".equals(stereotype))
    { if (positive)
      { return t.isEntityType(); }
      else
      { return !(t.isEntityType()); }
    }
    else if ("enumerated".equals(stereotype))
    { if (positive)
      { return t.isEnumeratedType(); }
      else
      { return !(t.isEnumeratedType()); }
    }
    else if ("enumerationLiteral".equals(stereotype))
    { if (positive)
      { return t.isEnumeratedType() && t.hasValue(e); }
      else
      { return !(t.isEnumeratedType() && t.hasValue(e)); }
    }
    else if ("classId".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.CLASSID; }
      else
      { return e.umlkind != Expression.CLASSID; }
    }
    else if ("value".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.VALUE; }
      else
      { return e.umlkind != Expression.VALUE; }
    }
    else if ("variable".equals(stereotype))
    { if (positive)
      { return e.umlkind == Expression.VARIABLE; }
      else
      { return e.umlkind != Expression.VARIABLE; }
    }
    return false;
  }

  public boolean conditionSatisfied(Statement e)
  { if (e instanceof AssignStatement) 
    { AssignStatement st = (AssignStatement) e; 
      Expression left = st.getLeft(); 
      return conditionSatisfied(left); 
    } 
    return false; 
  } // and for other kinds of statement also 
}
    
