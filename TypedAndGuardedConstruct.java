import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public abstract class TypedAndGuardedConstruct
{ String originalType = "";
  Expression guard;

  public TypedAndGuardedConstruct(String t, Expression g)
  { originalType = t; 
    guard = g;
  }

  public void addGuard(Expression g)
  { Expression newguard = Expression.simplifyAnd(g,guard); 
    System.out.println("Added guard: " + newguard); 
    guard = (Expression) newguard.clone(); 
  }  
}
