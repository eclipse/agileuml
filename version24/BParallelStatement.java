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

public class BParallelStatement extends BStatement
{ private Vector statements = new Vector(); // BStatement
  private boolean isParallel = true; 

  public BParallelStatement() { }

  public BParallelStatement(Vector sts)
  { statements = sts; }

  public BParallelStatement(boolean par) 
  { isParallel = par; }

  public BParallelStatement(boolean par, Vector sts)
  { isParallel = par; statements = sts; }

  public void addStatement(BStatement st)
  { statements.add(st); }

  public void addStatements(Vector sts)
  { for (int i = 0; i < sts.size(); i++)
    { BStatement st = (BStatement) sts.get(i); 
      statements.add(st); 
    } 
  } 

  public Vector getStatements()
  { return statements; }

  public String toString()
  { String res = "";
    String op = "||"; 
    if (isParallel == false) 
    { op = ";"; } 

    int n = statements.size();
    if (n == 0) { return "  skip\n"; } 
    for (int i = 0; i < n-1; i++)
    { res = res + statements.get(i) + " " + op + "\n      "; }
    res = res + statements.get(n-1);
    return res;
  }

  public BStatement substituteEq(String oldE, BExpression newE)
  { Vector nstats = new Vector();
    for (int i = 0; i < statements.size(); i++)
    { BStatement stat = (BStatement) statements.get(i);
      BStatement nstat = stat.substituteEq(oldE,newE);
      nstats.add(nstat);
    }
    return new BParallelStatement(isParallel, nstats);
  }

  public BStatement simplify()
  { Vector nstats = new Vector();
    for (int i = 0; i < statements.size(); i++)
    { BStatement stat = (BStatement) statements.get(i);
      BStatement nstat = stat.simplify();
      nstats.add(nstat);
    }
    return new BParallelStatement(isParallel, nstats);
  }


  public Vector wr()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++)
    { BStatement stat = (BStatement) statements.get(i); 
      res = VectorUtil.union(res,stat.wr());
    }
    return res;
  }

  public Vector rd()
  { Vector res = new Vector(); 
    for (int i = 0; i < statements.size(); i++)
    { BStatement stat = (BStatement) statements.get(i); 
      res = VectorUtil.union(res,stat.rd());
    }
    return res;
  }

  public BStatement normalise()
  { BParallelStatement res = new BParallelStatement(); 
    for (int i = 0; i < statements.size(); i++)
    { BStatement stat = (BStatement) statements.get(i); 
      res.addStatement(stat.normalise());
    }
    return res;
  }

  public BStatement seq2parallel()
  { BParallelStatement res = new BParallelStatement();
    if (statements.size() < 2) 
    { res.statements.addAll(statements); 
      return res; 
    } 
    BStatement s1 = (BStatement) statements.get(0); 
    Vector rem = new Vector(); 
    Vector wr1 = s1.wr();  
    for (int i = 1; i < statements.size(); i++)
    { rem.add(statements.get(i)); }
    BParallelStatement s2 = new BParallelStatement(rem); 
    Vector wr2 = s2.wr(); 
    Vector rd2 = s2.rd(); 
    return s1.sequence2parallel(s1,s2,wr1,wr2,rd2);
  }
    
}

