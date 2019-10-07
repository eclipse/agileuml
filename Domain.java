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


public abstract class Domain extends NamedElement 
{ boolean isCheckable = false;
  boolean isEnforceable = false;
  QVTRule rule;
  TypedModel typedModel;
  Attribute rootVariable;

  public Domain(String nme, QVTRule r, TypedModel tm, Attribute att)
  { super(nme); 
    rule = r;
    typedModel = tm;
    rootVariable = att; 
  }

  public abstract Domain overrideBy(Domain d); 

  public void setCheckable(boolean c)
  { isCheckable = c; }

  public void setEnforceable(boolean en)
  { isEnforceable = en; }

  public String getRootName()
  { return rootVariable.getName(); } 

  public Vector getObjectTemplateAttributes(Vector vars)
  { Vector res = new Vector(); 
    String dname = rootVariable.getName(); 
    for (int i = 0; i < vars.size(); i++) 
    { String v = vars.get(i) + ""; 
      if (dname.equals(v))
      { res.add(rootVariable); } 
    } 
    return res; 
  } 

  public Vector objectTemplateAttributes()
  { Vector res = new Vector(); 
    if (rootVariable.getType() != null && 
        rootVariable.getType().isEntity())
    { res.add(rootVariable); }  
    return res; 
  } 

  public abstract boolean typeCheck(Vector entities, Vector types, Vector contexts, Vector env);  
   

  public abstract Expression toGuardCondition(Vector bound, Expression post);

  public abstract Expression toSourceExpression(Vector bound);

  public abstract Expression toTargetExpression(Vector bound);

  public abstract Expression toSourceExpressionOp(Vector bound);

  public abstract Expression toTargetExpressionOp(Vector bound);

  public abstract int complexity(); 

  public abstract int cyclomaticComplexity(); 

  public abstract int epl(); 

  public abstract Vector operationsUsedIn(); 

  public Expression deleteDomainExp()
  { BasicExpression arg = new BasicExpression(rootVariable); 
    arg.setData("self"); 
    return new UnaryExpression("->isDeleted", arg); 
  } 

  public Expression checkTrace(Entity rtrace)
  { // R$trace@pre->exists( r$trace : r$trace.d = self )
    // traces$R$d->notEmpty()

    Type t = rootVariable.getType(); 
    if (t != null && t.isEntity())
    { Entity e = t.getEntity(); 
      Association ast = e.getRole("traces$" + rule.getName() + "$" + rootVariable.getName()); 
      if (ast != null) 
      { BasicExpression traces = new BasicExpression(ast);
        traces.setPrestate(true); 
        UnaryExpression isempty = new UnaryExpression("->isEmpty", traces); 
        return isempty; 
      } 
    }  

    String tracename = rtrace.getName(); 
    Type ttype = new Type(rtrace); 
    BasicExpression tracevar = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression traceent = new BasicExpression(rtrace); 
    traceent.setPrestate(true); 

    BasicExpression traced = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression d = new BasicExpression(rootVariable); 
    d.setObjectRef(traced); 
    BasicExpression selfvar = new BasicExpression(rootVariable.getType(), "self"); 
    BinaryExpression eq = new BinaryExpression("=", d, selfvar); 
    BinaryExpression indom = new BinaryExpression(":", tracevar, traceent); 
    BinaryExpression res = new BinaryExpression("#", indom, eq); 
    return res; 
  } 

  public Expression checkTraceEmpty(Entity rtrace)
  { // not(R$trace@pre->exists( r$trace : r$trace.d = self ))
    // traces$R$d->isEmpty()

    Type t = rootVariable.getType(); 
    if (t != null && t.isEntity())
    { Entity e = t.getEntity(); 
      Association ast = e.getRole("traces$" + rule.getName() + "$" + rootVariable.getName()); 
      if (ast != null) 
      { BasicExpression traces = new BasicExpression(ast);
        traces.setPrestate(true); 
        UnaryExpression isempty = new UnaryExpression("->isEmpty", traces); 
        return isempty; 
      } 
    }  

    String tracename = rtrace.getName(); 
    Type ttype = new Type(rtrace); 
    BasicExpression tracevar = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression traceent = new BasicExpression(rtrace); 
    traceent.setPrestate(true); 

    BasicExpression traced = new BasicExpression(ttype, tracename.toLowerCase() + "$x"); 
    BasicExpression d = new BasicExpression(rootVariable); 
    d.setObjectRef(traced); 
    BasicExpression selfvar = new BasicExpression(rootVariable.getType(), "self"); 
    BinaryExpression eq = new BinaryExpression("=", d, selfvar); 
    BinaryExpression indom = new BinaryExpression(":", tracevar, traceent); 
    BinaryExpression res = new BinaryExpression("#", indom, eq); 
    return new UnaryExpression("not", res); 
  } 
}
