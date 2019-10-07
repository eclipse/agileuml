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

public class RelationDomain extends Domain
{ DomainPattern pattern;

  public RelationDomain(String nme, QVTRule r, TypedModel tm, Attribute v, DomainPattern p)
  { super(nme, r, tm, v);
    pattern = p;
  }

  public Object clone()
  { DomainPattern dp1 = (DomainPattern) ((DomainPattern) pattern).clone(); 
    RelationDomain res = new RelationDomain(name,rule,typedModel,rootVariable,dp1);
    dp1.relationDomain = res;  
    res.isCheckable = isCheckable; 
    res.isEnforceable = isEnforceable; 
    return res; 
  } 

  public Domain overrideBy(Domain d) 
  { if (d instanceof PrimitiveDomain) 
    { System.err.println("!! ERROR: cannot override relation domain " + this + 
                         " by primitive domain " + d); 
      return null; 
    } 
    RelationDomain rd = (RelationDomain) d; 

    if (isCheckable && d.isCheckable) { } 
    else if (isEnforceable && d.isEnforceable) { } 
    else 
    { System.err.println("!! ERROR: cannot combine domains with different enforce/checkonly status: "); 
      System.err.println(this + " " + d); 
      return null;
    }  

    if (pattern != null && pattern.templateExpression != null &&
        (pattern.templateExpression instanceof ObjectTemplateExp))
    { ObjectTemplateExp mytemplate = (ObjectTemplateExp) pattern.templateExpression; 
      if (rd.pattern != null && rd.pattern.templateExpression != null &&
          (rd.pattern.templateExpression instanceof ObjectTemplateExp))
      { ObjectTemplateExp dtemplate = (ObjectTemplateExp) rd.pattern.templateExpression;
        ObjectTemplateExp rt = mytemplate.overrideBy(dtemplate); 
        RelationDomain res = new RelationDomain(name,rule,typedModel,rd.rootVariable,null); 
        DomainPattern dp = new DomainPattern(res,rt);
        res.pattern = dp; 
        res.isCheckable = rd.isCheckable; 
        res.isEnforceable = rd.isEnforceable; 
        return res;
      } 
    } 
    
    System.err.println("!! ERROR: cannot override " + this + " by " + rd); 
    return null;  
  } 
        

  public String toString()
  { String res = "enforce";
    if (isCheckable)
    { res = "checkonly"; }
    res = res + " domain " + typedModel +
        " " + pattern + ";";
    return res;
  }

  public int complexity()
  { return pattern.complexity(); } 

  public int cyclomaticComplexity()
  { return pattern.cyclomaticComplexity(); } 

  public int epl()
  { return pattern.epl(); } 

  public Vector operationsUsedIn()
  { return pattern.operationsUsedIn(); } 

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env)
  { return pattern.typeCheck(types,entities,contexts,env); }  


  public Vector getObjectTemplateAttributes(Vector vars)
  { Vector res = new Vector(); 
    String dname = rootVariable.getName(); 
    for (int i = 0; i < vars.size(); i++) 
    { String v = vars.get(i) + ""; 
      if (dname.equals(v))
      { res.add(rootVariable); } 
    } 
    res.addAll(pattern.getObjectTemplateAttributes(vars)); 
    return res; 
  } 

  public Vector objectTemplateAttributes()
  { Vector res = new Vector(); 
    res.add(rootVariable);  
    res.addAll(pattern.objectTemplateAttributes()); 
    return res; 
  } 


  public Expression toSourceExpression(Vector bound)
  { Expression contextObj = new BasicExpression(rootVariable);
    Expression res = pattern.toSourceExpression(bound, contextObj); 
    if (pattern != null && pattern.templateExpression != null &&
        (pattern.templateExpression instanceof ObjectTemplateExp))
    { Expression btexp = new BasicExpression(rootVariable);
      Expression scope = 
        new BasicExpression(((ObjectTemplateExp) pattern.templateExpression).referredClass);
      Expression decl = new BinaryExpression(":", btexp, scope); 
      res = Expression.simplify("&",decl,res,null);
    } 
    return res; 
  }

  public Expression toGuardCondition(Vector bound, Expression post)
  { Expression contextObj = new BasicExpression(rootVariable);
    // This should be bound, ie, in the call that is being guarded. 

    if (isCheckable)
    { bound.add(rootVariable.getName()); } 

    Expression res = pattern.toGuardCondition(bound, contextObj, post); 
    if (pattern != null && pattern.templateExpression != null &&
        (pattern.templateExpression instanceof ObjectTemplateExp))
    { Expression btexp = new BasicExpression(rootVariable);
      Expression scope = 
        new BasicExpression(((ObjectTemplateExp) pattern.templateExpression).referredClass);
      Expression decl = new BinaryExpression(":", btexp, scope); 
      res = Expression.simplify("&",decl,res,null);
    } 
    return res; 
  }

  public Expression toTargetExpression(Vector bound)
  { Expression contextObj = new BasicExpression(rootVariable);
    BasicExpression be = new BasicExpression(true);
    return pattern.toTargetExpression(bound, contextObj, be);
  }

  public Expression toSourceExpressionOp(Vector bound)
  { Expression contextObj = new BasicExpression(rootVariable);
    Expression res = pattern.toSourceExpression(bound, contextObj);  
    return res; 
  }

  public Expression toTargetExpressionOp(Vector bound)
  { Expression contextObj = new BasicExpression(rootVariable);
    BasicExpression be = new BasicExpression(true);
    return pattern.toTargetExpressionOp(bound, contextObj, be);
  }
}

