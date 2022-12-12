import java.util.Vector; 
import java.util.Set; 
import java.util.HashSet; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: ETL */ 

public class NamedBlock
{ String name;
  Statement body;
  boolean isPre = true;

  public NamedBlock(String n, Statement c)
  { name = n; body = c; }

  public String getName()
  { return name; } 

  public void setBody(Statement s)
  { body = s; }

  public void setPre(boolean p)
  { isPre = p; }

  public boolean isPre()
  { return isPre; } 

  public String toString()
  { String res = "";
    if (isPre) 
    { res = "pre "; }
    else
    { res = "post "; }
    res = res + name + " {\n";
    res = res + body + " }\n";
    return res;
  }

  public boolean typeCheck(Vector types, Vector entities, Vector contexts, Vector env) 
  { return body.typeCheck(types,entities,contexts,env); } 
  

  public int syntacticComplexity()
  { int res = body.syntacticComplexity(); 
    System.out.println("*** Syntactic complexity of block " + name + " = " + res); 
    return res; 
  } 

  public int cyclomaticComplexity()
  { int res = body.cyclomaticComplexity(); 
    System.out.println("*** Cyclomatic complexity of block " + name + " = " + res); 
    return res; 
  } 

  public int epl()
  { int res = body.epl(); 
    return res; 
  } 

  public void getCallGraph(Map res)
  { Vector bfcalls = body.allOperationsUsedIn(); 
    for (int j = 0; j < bfcalls.size(); j++) 
    { res.add_pair(name, bfcalls.get(j)); } 
  } 

  public int analyseEFO(Map m) 
  { // count the number of different targets of calls from the map
    Set calls = new HashSet(); 
    for (int i = 0; i < m.elements.size(); i++) 
    { Maplet mm = (Maplet) m.get(i); 
      calls.add(mm.getTarget() + ""); 
    } 
    int res = calls.size(); 

    System.out.println("*** Named Block " + getName() + " has fan-out = " + res); 
    return res; 
  } 

  public Constraint toConstraint(Vector entities, Vector types, UseCase uc)
  { // becomes rule that just invokes operation op, which has activity body

    BasicExpression betrue = new BasicExpression(true);
    betrue.setUmlKind(Expression.VALUE); 
    betrue.setType(new Type("boolean",null));
    String opname = "$" + name;
    BehaviouralFeature op = new BehaviouralFeature(opname);
    op.setActivity(body);
    uc.addOperation(op); 
    op.setUseCase(uc); 
    BasicExpression invoke = 
        new BasicExpression(uc.classifier.getName() + "." + opname + "()",0); 
    Expression callf = invoke.checkIfSetExpression(); 
    Constraint cons = new Constraint(betrue,callf); 
    return cons; 
  } 

}

