import java.util.Vector; 

/* Package: B AMN */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BVarStatement extends BStatement
{ private String var; 
  private BStatement code;

  public BVarStatement(String v, BStatement body)
  { var = v;
    code = body;
  }

  public String getVar()
  { return var; } 

  public BStatement getCode()
  { return code; } 

  public Vector wr() 
  { return code.wr(); } 

  public Vector rd() 
  { Vector res = code.rd(); 
    res.remove(var); 
    return res; 
  } 

  public String toString()
  { String res = "VAR " + var + " IN\n";
    res = res + code + " END";
    return res;
  }

  public BStatement substituteEq(String oldE, BExpression newE)
  { if (var.equals(oldE)) { return this; } 
    BStatement ncode = code.substituteEq(oldE,newE); 
    return new BVarStatement(var,ncode); 
  } 

  public BStatement simplify()
  { BStatement ncode = code.simplify(); 
    return new BVarStatement(var,ncode); 
  } 

  public BStatement seq2parallel()
  { BStatement ncode = code.seq2parallel(); 
    return new BVarStatement(var,ncode); 
  } 

  public BStatement normalise()
  { BStatement stat = code.normalise(); 
    BStatement res = new BVarStatement(var,stat); 
    return res;  
  } 
}

