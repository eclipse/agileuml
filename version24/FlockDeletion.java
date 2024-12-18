import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class FlockDeletion extends TypeMappingConstruct
{ public FlockDeletion(String t, Expression g)
  { super(t,g); }

  public String toString()
  { return "delete " + originalType + " when: " + guard; }

  public Constraint toConstraint(Entity e)
  { Type et = new Type(e);
    Expression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(e);
    selfexp.setType(et);
    selfexp.setElementType(et);
    Expression ante = guard.substituteEq("original", selfexp);
    ante.setPre(); 
    Expression succ = new UnaryExpression("->isDeleted",selfexp);
    Constraint con = new Constraint(ante,succ);
    con.setOwner(e);
    return con;
  }

}
