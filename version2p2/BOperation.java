import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class BOperation   
{ String name;
  String precond;
  IfStatement body;

  public BOperation(String n, String p, IfStatement b)
  { name = n;
    precond = p;
    body = b; 
  }

  public void display()
  { System.out.print("  " + name + " = ");
    if (body.isEmpty()) 
    { System.out.print(" skip"); 
      return; 
    }
    else  
    { System.out.println(); } 
    System.out.println("    PRE " + precond);
    System.out.println("    THEN");
    body.display();
    System.out.print("    END"); 
  }

  public void displayImp(String var)  
       /* var is statemachine name */
  { System.out.print("  " + name + " =");
    if (body.isEmpty()) 
    { System.out.print(" skip"); 
      return; 
    } 
    else 
    { System.out.println(); } 
    System.out.println("    VAR " + var + "x");
    System.out.println("    IN " + var + "x <-- " + var + "_VAL_VAR;");     
    Statement newBody = body.substituteEq(var, new BasicExpression(var + "x"));
    newBody.displayImp(null);
    System.out.print("    END"); 
  } // VAL_NVAR if Attribute

  public void displayImp(String var, PrintWriter out)
       /* var is statemachine name */
  { out.print("  " + name + " =");
    if (body.isEmpty())
    { out.print(" skip");
      return;
    }
    else
    { out.println(); }
    out.println("    VAR " + var + "x");
    out.println("    IN " + var + "x <-- " + var + "_VAL_VAR;");
    Statement newBody = body.substituteEq(var, new BasicExpression(var + "x"));
    newBody.displayImp(null,out);
    out.print("    END");
  } // VAL_NVAR if Attribute

  public void displayMultImp(String var)
       /* var is statemachine name */
  { System.out.print("  " + name + " =");
    if (body.isEmpty())
    { System.out.print(" skip");
      return;
    }
    else
    { System.out.println(); }

    int n = var.length(); 
    String lib = var.substring(0,1).toUpperCase() +
                 var.substring(1,n);
    System.out.println("    VAR " + var + "x");
    System.out.println("    IN " + var + "x <-- " + lib + "_VAL_FNC_OBJ(oo);");
    Statement newBody = body.substituteEq(var, new BasicExpression(var + "x"));
    newBody.displayImp(null);
    System.out.print("    END");
  }

  public void displayMultImp(String var, PrintWriter out)
       /* var is statemachine name */
  { out.print("  " + name + " =");
    if (body.isEmpty())
    { out.print(" skip");
      return;
    }
    else
    { out.println(); }

    int n = var.length();
    String lib = var.substring(0,1).toUpperCase() +
                 var.substring(1,n);
    out.println("    VAR " + var + "x");
    out.println("    IN " + var + "x <-- " + lib + "_VAL_FNC_OBJ(oo);");
    Statement newBody = body.substituteEq(var, new BasicExpression(var + "x"));
    newBody.displayImp(null,out);
    out.print("    END");
  }

  public void display(PrintWriter out)
  { out.print("  " + name + " =");
    if (body.cases.size() == 0) 
    { out.print(" skip"); 
      return; 
    } 
    else 
    { out.println(); } 
    out.println("    PRE " + precond);
    out.println("    THEN");
    body.display(out);
    out.print("    END"); 
  }

  public void displayJava(String stat)
  { System.out.println("public " + stat + "void " + name + "()");
    System.out.println("/* Assumes: " + precond + " */");
    System.out.println("{");
    body.displayJava(null);      /* Or ""? */ 
    System.out.println("}"); 
  }

  public void displayJava(PrintWriter out, String stat)
  { out.println("public " + stat + "void " + name + "()");
    out.println("/* Assumes: " + precond + " */");
    out.println("{");
    body.displayJava("", out);
    out.println("}"); 
  }
  // Should display java forms of precond. 
}









