import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: QVT */ 

public class PropertyTemplateItem
{ Attribute referredProperty; // may represent association
  Expression value = null;
  ObjectTemplateExp template = null;

  Attribute actualValue = null; // for use by the ModelMatching process. 

  public PropertyTemplateItem(Attribute att)
  { referredProperty = att; }

  public Object clone()
  { PropertyTemplateItem res = new PropertyTemplateItem(referredProperty); 
    if (value != null) 
    { Expression newvalue = (Expression) value.clone(); 
      res.value = newvalue; 
    } 
    if (template != null) 
    { ObjectTemplateExp ote = (ObjectTemplateExp) template.clone(); 
      res.template = ote; 
    } 
    return res; 
  } 

  public PropertyTemplateItem overrideBy(PropertyTemplateItem p) 
  { // assume referredProperty.name.equals(p.referredPropertyName)

    if (value != null) 
    { PropertyTemplateItem newp = (PropertyTemplateItem) p.clone(); 
      return newp; 
    } 

    if (template != null && p.template != null) 
    { ObjectTemplateExp rt = template.overrideBy(p.template); 
      PropertyTemplateItem rov = new PropertyTemplateItem(referredProperty); 
      rov.template = rt; 
      return rov; 
    } 

    System.err.println("!! ERROR: cannot override " + this + " by " + p); 
    return null; 
  } 

  public void setSource()
  { if (template != null) 
    { template.setSource(); } 
  } 

  public void addPTI(Attribute p, Object v) 
  { if (template != null) 
    { template.addPTI(p,v); } 
  } 

  public void setActualValue(Attribute p) 
  { actualValue = p; } 

  public void setValue(Expression v)
  { value = v; }

  public void setTemplate(ObjectTemplateExp t)
  { template = t; }

  public void setEntity(Entity e)
  { referredProperty.setEntity(e); }

  public Vector getObjectTemplateAttributes(Vector vars)
  { if (template != null) 
    { return template.getObjectTemplateAttributes(vars); } 
    else 
    { return new Vector(); } 
  } 

  public Vector objectTemplateAttributes()
  { if (template != null) 
    { return template.objectTemplateAttributes(); } 
    else 
    { return new Vector(); } 
  } 

  public Expression toGuardCondition(Vector bound, Expression contextObj, Expression post)
  { // post[value/referredProperty], or 
    // referredProperty = template, as UML-RSDS expression
   Expression result = new BasicExpression(true);
   if (referredProperty == null) 
   { System.err.println(">>> No feature defined for property template item: " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value.isVariable()) 
     { // Expression valueexp = new BasicExpression(value);
       // if (bound.contains(value + ""))
       if ("=".equals(op))
       { result = post.substitute(value, fexp); } 
       else 
       { result = Expression.simplifyAnd(new BinaryExpression(op, value, fexp),post); }
     }
     else // value : fexp 
     { // just a value expression
       result = Expression.simplifyAnd(new BinaryExpression(op, value, fexp),post); 
     } 
  }
  else if (template != null)
  { Entity f = template.getEntity();
    Attribute x = template.bindsTo;
    Expression xexp = new BasicExpression(x);
    result = new BinaryExpression(op, xexp, fexp); 

    if (template.isEmpty()) { }
    else
    { Expression andcond = template.toGuardCondition(bound,xexp,post);
      result = new BinaryExpression("&", result, andcond); 
    }
  }
  return result;
 }

  public Expression toSourceExpression(Vector bound, Expression contextObj)
  { // Expresses referredProperty = value, or 
    // referredProperty = template, as UML-RSDS expression
   Expression result = new BasicExpression(true);
   if (referredProperty == null) 
   { System.err.println("!! ERROR: No feature defined for property template item: " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value.isVariable()) 
     { // Expression valueexp = new BasicExpression(value);
       if (bound.contains(value + ""))
       { if ("=".equals(op))
         { result = new BinaryExpression("=", fexp, value);  }
         else 
         { result = new BinaryExpression(op, value, fexp); }
       }
       else
       { result = new BinaryExpression(op, value, fexp); 
         bound.add(value + ""); 
       }
    }
    else // just a value expression
    { result = new BinaryExpression("=", fexp, value);  }
  }
  else if (template != null)
  { Entity f = template.getEntity();
    Attribute x = template.bindsTo;
    Expression xexp = new BasicExpression(x);
    result = new BinaryExpression(op, xexp, fexp); 
    bound.add(x.getName());
    if (template.isEmpty()) { }
    else
    { Expression andcond = template.toSourceExpression(bound,xexp);
      result = new BinaryExpression("&", result, andcond); 
    }
  }
  return result;
 }

  public Expression toSourceExpression0(Vector bound, Expression contextObj)
  { // Expresses referredProperty = value, or 
    // referredProperty = template, as UML-RSDS expression
   Expression result = new BasicExpression(true);
   if (referredProperty == null) 
   { System.err.println("!! ERROR: No feature defined for property template item: " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value instanceof BasicExpression) 
     { BasicExpression valueexp = (BasicExpression) value;
       // BasicExpression realexp = new BasicExpression(valueexp.variable); 

       if (bound.contains(value + ""))
       { if ("=".equals(op))
         { result = new BinaryExpression("=", fexp, valueexp);  }
         else 
         { result = new BinaryExpression(op, valueexp, fexp); }
       }
       else
       { result = new BinaryExpression(op, valueexp, fexp); 
         bound.add(value + ""); 
       }
    }
    else // just a value expression
    { result = new BinaryExpression("=", fexp, value);  }
  }
  return result;
 }

  public boolean isManyValued() 
  { return referredProperty != null && referredProperty.isMultiple(); } 

  public Expression toTargetExpression(Vector bound, Expression contextObj, Expression setting)
  { // Expresses referredProperty = value, or 
    // referredProperty = template, as UML-RSDS expression

   Expression result = new BasicExpression(true);
 
   if (referredProperty == null) 
   { System.err.println("No feature defined for " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value.isVariable()) 
     { // Expression valueexp = new BasicExpression(value);
       if (bound.contains(value + ""))
       { if ("=".equals(op))
         { result = new BinaryExpression("=", fexp, value);  }
         else 
         { result = new BinaryExpression(op, value, fexp); }
       }
       else // should not occur
       { result = new BinaryExpression(op, value, fexp); 
         bound.add(value + ""); 
       }
    }
    else // just a value expression
    { result = new BinaryExpression("=", fexp, value);  }
  }
  else if (template != null)
  { // Entity f = template.getEntity();
    Attribute x = template.bindsTo;
    Expression xexp = new BasicExpression(x);

    // Expression setting;
    if (referredProperty.isMultiple())
    { setting = new BinaryExpression(":", xexp, fexp); }
    else
    { setting = new BinaryExpression("=", fexp, xexp); }

    // if (template.isEmpty())
    // { bound.add(x.getName());
    //   result = new BinaryExpression("&", result, setting); 
    // }
    // else
    { Expression andcond = template.toTargetExpression(bound,xexp,setting);
      result = new BinaryExpression("&", result, andcond);
    } 
  }
  return result;
 }

  public Expression toTargetExpressionOp(Vector bound, Expression contextObj, Expression setting)
  { // Expresses referredProperty = value, or 
    // referredProperty = template, as UML-RSDS expression

   Expression result = new BasicExpression(true);
 
   if (referredProperty == null) 
   { System.err.println("!! ERROR: No feature defined for " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value.isVariable()) 
     { // Expression valueexp = new BasicExpression(value);
       if (bound.contains(value + ""))
       { if ("=".equals(op))
         { result = new BinaryExpression("=", fexp, value);  }
         else 
         { result = new BinaryExpression(op, value, fexp); }
       }
       else // should not occur
       { result = new BinaryExpression(op, value, fexp); 
         bound.add(value + ""); 
       }
    }
    else // just a value expression
    { result = new BinaryExpression("=", fexp, value);  }
  }
  else if (template != null)
  { // Entity f = template.getEntity();
    Attribute x = template.bindsTo;
    Expression xexp = new BasicExpression(x);

    // Expression setting;
    if (referredProperty.isMultiple())
    { setting = new BinaryExpression(":", xexp, fexp); }
    else
    { setting = new BinaryExpression("=", fexp, xexp); }

    // bound.add(x.getName());
    // if (template.isEmpty())
    // { bound.add(x.getName());
    //   result = new BinaryExpression("&", result, setting); 
    // }
    // else
    { Expression andcond = template.toTargetExpressionOp(bound,xexp,setting);
      result = new BinaryExpression("&", result, andcond);
    } 
  }
  return result;
 }

 public boolean typeCheck(Vector entities, Vector types, Vector contexts, Vector env)
 { if (value != null) 
   { value.typeCheck(entities,types,contexts,env); } 
   if (template != null)
   { template.typeCheck(entities,types,contexts,env); } 
   return true; 
 } 
 
  public String toString()
  { String res = referredProperty + " = ";
    if (value != null)
    { res = res + value; }
    else // template != null
    { res = res + template; }
    return res; 
  }

  public String toQVTO(Expression contextObj, Attribute src, Vector bound, Map whens)
  { String res = "";

    if (referredProperty == null) 
    { System.err.println("No feature defined for " + this); 
      return ""; 
    } 

    int tmult = ModelElement.ONE; 
    int smult = ModelElement.ONE; 

    BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
    String op = ":=";
    String resolution = "resolveoneIn"; 
    if (referredProperty.isMultiple())
    { op = "+="; 
      resolution = "resolveIn"; 
      tmult = ModelElement.MANY; 
    }

    // Type ttarg = trg.getType(); 
    // int tmult = ttarg.typeMultiplicity(); 

    if (value != null)
    { if (value instanceof BasicExpression) 
      { // Expression valueexp = new BasicExpression(value);
        
        Attribute actualVar = ((BasicExpression) value).variable;
        if (actualVar != null)  
        { Expression varbe = new BasicExpression(actualVar); 
          Type tsrc = actualVar.getType(); 
          if (tsrc != null) 
          { smult = tsrc.typeMultiplicity(); } 

          if (bound.contains(value + ""))
          { // May need data conversions 
            if (":=".equals(op))
            { res = res + fexp + " := " + src + "." + varbe;  }
            else 
            { res = res + fexp + " " + op + " " + varbe; }
          }
          else // should not occur
          { res = res + fexp + " " + op + " " + src + "." + varbe; 
            bound.add(value + ""); 
          }
        } 
        else 
        { res = res + fexp + " := " + src + "." + value;  }
      }
      else // just a value expression
      { res = res + fexp + " := " + src + "." + value;  }

      if (tmult == ModelElement.ONE && smult != ModelElement.ONE) 
      { res = res + "->any(); "; } 
      else if (tmult != ModelElement.ONE && smult == ModelElement.ONE)
      { res = res + "->as" + referredProperty.getType().getName() + "(); "; } 
      else 
      { res = res + "; "; } 
    }
    else if (template != null)
    { // Entity f = template.getEntity();
      Attribute x = template.bindsTo;
      Expression xexp = new BasicExpression(x);

    // Expression setting;
    // if (referredProperty.isMultiple())
    // { setting = new BinaryExpression(":", xexp, fexp); }
    //  else
    //  { setting = new BinaryExpression("=", fexp, xexp); }

      Vector domainwhens = whens.domain(); 
      Expression etrg = (Expression) ModelElement.lookupExpressionByName(x + "", domainwhens); 
      if (etrg != null) 
      { BasicExpression esrc = (BasicExpression) whens.get(etrg); 
        { if (esrc != null) 
          { Type tsrc = esrc.getType(); 
            Type tesrc = tsrc.getElementType(); 
            String tesrcname = tesrc.getName(); 
            Attribute targetFeature = ((BasicExpression) etrg).variable; 
            Attribute sourceFeature = ((BasicExpression) esrc).variable; 

            BasicExpression texp = new BasicExpression(contextObj, targetFeature);
            BasicExpression sexp = new BasicExpression(new BasicExpression(src), sourceFeature); 
          
            Type ttrg = etrg.getType(); 
            Type tetrg = ttrg.getElementType(); 
            String tetrgname = tetrg.getName(); 
            res = res + "  " + texp + " := " + 
                        sexp + "." + resolution + "(" + tesrcname + "::" + 
                        tesrcname + "2" + tetrgname + ", " + tetrgname + ");"; 
            String andcond = template.toQVTOnoobject(texp,src,bound,whens);
            return res + " " + andcond;
          } 
          else // a new object is needed
          { return res + " " + template.toQVTO(xexp,src,bound,whens); } 
        }  
      } 

      String andcond = template.toQVTO(xexp,src,bound,whens);
      res = res + " " + andcond;
    } 

    return res; 
  }

  public Expression toUMLRSDS(Vector bound, Expression contextObj, 
                              Expression setting, Map whens)
  { // Expresses referredProperty = value, or 
    // referredProperty = template, as UML-RSDS expression

   Expression result = new BasicExpression(true);
 
   if (referredProperty == null) 
   { System.err.println("No feature defined for " + this); 
     return result; 
   } 

   BasicExpression fexp = new BasicExpression(contextObj, referredProperty);
   String op = "=";
   if (referredProperty.isMultiple())
   { op = ":"; }
    
   if (value != null)
   { if (value instanceof BasicExpression) 
     { // Expression valueexp = new BasicExpression(value);
       Attribute actualVar = ((BasicExpression) value).variable; 
       Expression varbe = new BasicExpression(actualVar); 

       if (bound.contains(value + ""))
       { 
         if ("=".equals(op))
         { result = new BinaryExpression("=", fexp, varbe);  }
         else 
         { result = new BinaryExpression(op, varbe, fexp); }
       }
       else // should not occur
       { result = new BinaryExpression("=", fexp, varbe); 
         bound.add(value + ""); 
       }
    }
    else // just a value expression
    { result = new BinaryExpression("=", fexp, value);  }
  } // do data conversions
  else if (template != null)
  { // Entity f = template.getEntity();
    Attribute x = template.bindsTo;
    Expression xexp = new BasicExpression(x);

    // Expression setting;
    if (referredProperty.isMultiple())
    { setting = new BinaryExpression(":", xexp, fexp); }
    else
    { setting = new BinaryExpression("=", fexp, xexp); }

    Vector domainwhens = whens.domain(); 
    Expression etrg = (Expression) ModelElement.lookupExpressionByName(x + "", domainwhens); 
    if (etrg != null) 
    { BasicExpression esrc = (BasicExpression) whens.get(etrg); 
      { if (esrc != null) 
        { Type tsrc = esrc.getType(); 
          Type tesrc = tsrc.getElementType(); 
          String tesrcname = tesrc.getName(); 
          BasicExpression srcid = new BasicExpression(tesrcname.toLowerCase() + "Id");
          BasicExpression srcex = new BasicExpression(esrc.variable); 
          srcid.setObjectRef(srcex);  
          
          Type ttrg = etrg.getType(); 
          Type tetrg = ttrg.getElementType(); 
          String tetrgname = tetrg.getName(); 
          BasicExpression xref = new BasicExpression(tetrgname.toLowerCase() + "Id"); 
          if (esrc.variable.isManyValued())
          { srcid.setObjectRef(esrc); } 
          xref.setObjectRef(xexp); 
          BinaryExpression equalIds = new BinaryExpression("=", xref, srcid); 
          setting = new BinaryExpression("&",equalIds,setting);
        } 
      }  
    } 

    // if (template.isEmpty())
    // { bound.add(x.getName());
    //   result = new BinaryExpression("&", result, setting); 
    // }
    // else
    { Expression andcond = template.toUMLRSDS(bound,xexp,setting,whens);
      result = new BinaryExpression("&", result, andcond);
    } 
  }
  return result;
 }


  public int complexity() 
  { int res = 2; 
    if (value != null)
    { res = res + value.syntacticComplexity(); }
    else if (template != null)
    { res = res + template.complexity(); }
    return res; 
  }
    
  public int epl() 
  { int res = 0; 
    if (template != null)
    { res = res + template.epl(); }
    return res; 
  }

  public Vector operationsUsedIn()
  { Vector res = new Vector(); 
    if (value != null)
    { res.addAll(value.allOperationsUsedIn()); }
    else if (template != null)
    { res.addAll(template.operationsUsedIn()); }
    return res; 
  } 
    
}