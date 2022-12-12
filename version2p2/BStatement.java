import java.util.Vector; 
import java.util.Map; 
import java.util.HashMap; 

/* Package: B AMN */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public abstract class BStatement
{ private String writeFrame; 
  private Vector writeFrameList = new Vector(); 

  public void setWriteFrame(String s)
  { writeFrame = s; } 

  public void setWriteFrame(Vector v)
  { writeFrameList = v; } 

  public String getWriteFrame()
  { return writeFrame; } 

  public abstract Vector wr(); 
  // { return writeFrameList; } 

  public abstract Vector rd(); 

  public abstract BStatement normalise(); 
  
  public boolean equals(Object obj)
  { if (obj == null)
    { return false; }
    return obj.toString().equals(toString());
  }

  public abstract BStatement seq2parallel();  
      
  public BStatement sequence2parallel(BStatement s1, BStatement s2, 
                                      Vector wfr1, Vector wfr2, Vector rd2)
  { // equivalent parallel statement to s1; s2
    System.out.println("Converting B sequence to parallel: " + s1 + " ; " + s2); 
    System.out.println(wfr1 + " " + wfr2 + " " + rd2); 
    System.out.println(); 

    Vector inters1 = VectorUtil.intersection(wfr1,wfr2);
    Vector inters2 = VectorUtil.intersection(wfr1,rd2);
    BParallelStatement res = new BParallelStatement();

    if (inters1.size() == 0 && inters2.size() == 0)  // s1 does not write any data of s2
    { res.addStatement(s1); 
      BStatement stat2 = s2.seq2parallel(); 
      res.addStatement(stat2);
      return res;
    }

    if (s1 instanceof BAssignStatement)
    { BAssignStatement s1stat = (BAssignStatement) s1; 
      BAssignStatement s1norm = s1stat; 
      BStatement s2norm = s2; 

      if (inters1.size() > 0) 
      { s1norm = (BAssignStatement) s1stat.normalise(); 
        s2norm = s2.normalise(); 
      }
      BExpression lhs = s1norm.getLhs();
      BExpression rhs = s1norm.getRhs();
       
      BStatement stat2 = s2norm.seq2parallel();
      BStatement s2new = stat2.substituteEq(lhs + "",rhs);
      BStatement s3 = s2new.simplify(); 
      System.out.println("S2 simplified is: " + s3); 
      System.out.println(); 

      if (inters1.size() == 0)  // no common written data
      { res.addStatement(s1norm); 
        res.addStatement(s3);
        return res;
      }
      else
      { return s3; }
    }

    if (s1 instanceof BIfStatement)
    { BExpression cond = ((BIfStatement) s1).getCond();
      BStatement ifpart = ((BIfStatement) s1).getIf();
      BStatement elsepart = ((BIfStatement) s1).getElse();
      Vector fr2 = ifpart.wr();
      BStatement s3 = sequence2parallel(ifpart,s2,fr2,wfr2,rd2);
      Vector fr3 = elsepart.wr();
      BStatement s4 = sequence2parallel(elsepart,s2,fr3,wfr2,rd2);
      return new BIfStatement(cond,s3,s4);
    }

    if (s1 instanceof BAnyStatement)
    { Vector vars = ((BAnyStatement) s1).getVars();
      BExpression cond = ((BAnyStatement) s1).getWhere();
      BStatement thenpart = ((BAnyStatement) s1).getThen();
      // Vector fr2 = thenpart.wr();
      BStatement s3 = sequence2parallel(thenpart,s2,wfr1,wfr2,rd2);
      return new BAnyStatement(vars,cond,s3);
    }

    if (s1 instanceof BParallelStatement) 
    { ((BParallelStatement) s1).addStatement(s2); 
      return s1.seq2parallel(); 
    } 

    if (s1 instanceof BBasicStatement) 
    { return s2; } 

    return this; 
  }
    
  public static BStatement separateUpdates(Vector statements)
  { // for each vbl, gather updates to vbl 
    Map updates = new HashMap();
    Vector updated = new Vector();
    for (int i = 0; i < statements.size(); i++)
    { BStatement bs = (BStatement) statements.get(i);
      if (bs == null) { continue; } 
      String var = bs.getWriteFrame(); 
      System.out.println("Statement:::: " + bs + " on variable " + var); 
      BStatement varupd = (BStatement) updates.get(var);
      if (varupd == null)
      { updates.put(var,bs);
        updated.add(var);
      }
      else 
      { BStatement newupd = addCase(var,varupd,bs);
        updates.put(var,newupd);
      }
    }
    BParallelStatement res = new BParallelStatement();
    for (int j = 0; j < updated.size(); j++)
    { String v = (String) updated.get(j);
      BStatement vs = (BStatement) updates.get(v);
      vs.setWriteFrame(v);
      res.addStatement(vs);
    }
    res.setWriteFrame(updated);
    System.out.println("Complete statement:::: " + res); 
    return res;
  }

  private static BStatement addCase(String var,
                             BStatement stat, 
                             BStatement cse)
  { if (stat == null) 
    { if (cse instanceof BAssignStatement)
      { return ((BAssignStatement) cse).normalise(); }
      return cse; 
    }

    if (cse == null) { return stat; }

    if (stat instanceof BIfStatement && cse instanceof BIfStatement)
    { return combineIfStatements(var,(BIfStatement) stat, (BIfStatement) cse); } 

    if (stat instanceof BIfStatement)
    { BIfStatement ifstat = (BIfStatement) stat;
      BStatement ifpart = ifstat.getIf(); 
      BStatement elsepart = ifstat.getElse();
      BStatement newif = addCase(var,ifpart,cse); 
      BStatement newelse = addCase(var,elsepart,cse);
      ifstat.setElse(newelse);
      ifstat.setIf(newif); 
      return ifstat;
    }

    if (cse instanceof BIfStatement)
    { BIfStatement if2 = (BIfStatement) cse;
      BStatement ifpart2 = if2.getIf(); 
      BStatement else2 = if2.getElse();
      BStatement newif2 = addCase(var,stat,ifpart2); 
      BStatement newelse2 = addCase(var,stat,else2);
      if2.setIf(newif2);  
      if2.setElse(newelse2);
      return if2;
    } 
    return combineUpdates(var,stat,cse);
  } 

  // Can optimise if both if statements, with contradictory or identical 
  // conditions: if e then if not(e) then c1 else c2 end else c3  ==
  //             if e then c2 else c3
  // 
 
  private static BStatement combineIfStatements(String var,
                                                BIfStatement stat, BIfStatement cse)
  { BExpression e1 = stat.getCond(); 
    BExpression e2 = cse.getCond();
    BStatement cseif = cse.getIf(); 
    BStatement cseelse = cse.getElse(); 
    BStatement ifpart = stat.getIf(); 
    BStatement elsepart = stat.getElse();
    
    System.out.println("COMBINING If's with conditions " + e1 + " " + e2); 
  
    if (("" + e1).equals("" + e2))
    { BStatement newif = addCase(var,ifpart,cseif); 
      BStatement newelse = addCase(var,elsepart,cseelse);
      stat.setElse(newelse);
      stat.setIf(newif); 
      return stat;
    }

    if (e1.conflictsWith(e2))
    { BStatement newif = addCase(var,ifpart,cseelse); 
      BStatement newelse = addCase(var,elsepart,cse);
      stat.setElse(newelse);
      stat.setIf(newif); 
      return stat;
    }   

    BStatement newif = addCase(var,ifpart,cse); 
    BStatement newelse = addCase(var,elsepart,cse);
    stat.setElse(newelse);
    stat.setIf(newif); 
    return stat;
  }
    
 

  private static BStatement combineUpdates(String var,
                                    BStatement stat,
                                    BStatement cse)
  { // combine effects of statements on same var
    if (stat instanceof BAssignStatement &&
        cse instanceof BAssignStatement)
    { BAssignStatement s1 = (BAssignStatement) stat;
      BAssignStatement s2 = (BAssignStatement) cse;
      BAssignStatement s3 = (BAssignStatement) s2.normalise(); // makes everything var := var <+ e
      BExpression rhs1 = s1.getRhs();
      BExpression rhs2 = s3.getRhs();
      BExpression newrhs =
        combineUpdateExpressions(var,rhs1,rhs2);
      return new BAssignStatement(
               new BBasicExpression(var),newrhs);
    }
    if (stat instanceof BOperationCall && 
        cse instanceof BOperationCall) 
    { BOperationCall c1 = (BOperationCall) stat; 
      BOperationCall c2 = (BOperationCall) cse; 
      BExpression v1 = c1.updateExpression(var); 
      BExpression v2 = c2.updateExpression(var); 
      BExpression v; 
      if ((v1 + "").equals(v2 + ""))
      { v = v1; } 
      else 
      { v = new BBinaryExpression("<+",v1,v2); }
      Vector pars = new Vector(); 
      pars.add(v); 
      return new BOperationCall("update" + var,pars); 
    }
    return cse;
  }  // case of BOperationCall

  private static BExpression combineUpdateExpressions(String v,
                                      BExpression upd1,
                                      BExpression upd2)
  { System.out.println("WARNING ==> Possible update conflicts on: " + v + " " + 
                       upd1 + " " + upd2); 
    BExpression res;
    if (upd1 instanceof BBinaryExpression &&
        upd2 instanceof BBinaryExpression)
    { BBinaryExpression be1 = (BBinaryExpression) upd1;
      BBinaryExpression be2 = (BBinaryExpression) upd2;
      BExpression ll = be1.leftmostArgument();
      BExpression ll2 = be2.getLeft();
      if (v.equals(ll.toString()) &&
          v.equals(ll2.toString()) )
      { be1.setBrackets(true); 
        res = new BBinaryExpression(be2.getOperator(),be1,
                                    be2.getRight());
        return res;
      } // the order matters: (a /\ b) \/ c isn't (a \/ c) /\ b
    }
    return upd2;
  }

  public abstract BStatement substituteEq(String oldE, BExpression newE); 

  public abstract BStatement simplify(); 
}

