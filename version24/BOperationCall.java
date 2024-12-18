import java.util.Vector; 

/* Package: B */
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BOperationCall extends BStatement
{ private String opName;
  private Vector arguments = new Vector(); // BExpression

  public BOperationCall(String op, Vector args)
  { opName = op;
    arguments = args;
  }

  public String toString()
  { String res = opName;
    int pc = arguments.size();
    if (pc > 0)
    { res = res + "(";
      for (int i = 0; i < pc; i++)
      { res = res + arguments.get(i);
        if (i < pc - 1)
        { res = res + ","; }
      }
      res = res + ")";
    }
    return res;
  }

  public BStatement substituteEq(String oldE, BExpression newE)
  { // if (oldE.equals(toString()))
    // { return newE; } 
    Vector nargs = new Vector(); 
    for (int i = 0; i < nargs.size(); i++)
    { BExpression be = (BExpression) nargs.get(i); 
      BExpression nbe = be.substituteEq(oldE,newE); 
      nargs.add(nbe); 
    } 
    return new BOperationCall(opName,nargs); 
  } 

  public BExpression updateExpression(String var)
  { // if it is setAllvar(S,v), return S*{v}, etc
    if (opName.equals("setAll" + var))
    { BExpression set = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      BSetExpression valset = new BSetExpression();
      valset.addElement(val);
      return new BBinaryExpression("*",set,valset);
    }
    if (opName.equals("set" + var))
    { BExpression set = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      BSetExpression valset = new BSetExpression();
      valset.addElement(new BBinaryExpression("|->",set,val));
      return valset;
    }
    if (opName.equals("update" + var))
    { return (BExpression) arguments.get(0); }
    if (opName.equals("add" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { ex |-> var(ex) \/ { val }}
      BSetExpression valset = new BSetExpression();
      valset.addElement(val);
      BApplyExpression fex = 
        new BApplyExpression(var,ex);
      BBinaryExpression union =
        new BBinaryExpression("\\/",fex,valset);
      BBinaryExpression mapsto = 
        new BBinaryExpression("|->",ex,union);
      BSetExpression maplet = new BSetExpression();
      maplet.addElement(mapsto);
      return maplet;
    }
    else if (opName.equals("remove" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { ex |-> var(ex) - { val }}
      BSetExpression valset = new BSetExpression();
      valset.addElement(val);
      BApplyExpression fex = 
        new BApplyExpression(var,ex);
      BBinaryExpression minus =
        new BBinaryExpression("-",fex,valset);
      BBinaryExpression mapsto = 
        new BBinaryExpression("|->",ex,minus);
      BSetExpression maplet = new BSetExpression();
      maplet.addElement(mapsto);
      return maplet;
    }
    else if (opName.equals("union" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { ex |-> var(ex) \/ val }
      BApplyExpression fex = 
        new BApplyExpression(var,ex);
      BBinaryExpression union =
        new BBinaryExpression("\\/",fex,val);
      BBinaryExpression mapsto = 
        new BBinaryExpression("|->",ex,union);
      BSetExpression maplet = new BSetExpression();
      maplet.addElement(mapsto);
      return maplet;
    }
    else if (opName.equals("subtract" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { ex |-> var(ex) - val }
      BApplyExpression fex = 
        new BApplyExpression(var,ex);
      BBinaryExpression minus =
        new BBinaryExpression("-",fex,val);
      BBinaryExpression mapsto = 
        new BBinaryExpression("|->",ex,minus);
      BSetExpression maplet = new BSetExpression();
      maplet.addElement(mapsto);
      return maplet;
    }
    else if (opName.equals("addAll" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { exx,varx | exx : ex &
      //                  varx = var(exx) \/ { val } }
      String exx = "e_xx";
      String varx = var + "_x";
      Vector args = new Vector();
      args.add(exx);
      args.add(varx);
      BBasicExpression exxbe =
        new BBasicExpression(exx);
      BApplyExpression fex = 
        new BApplyExpression(var,exxbe);
      BBasicExpression varxbe = 
        new BBasicExpression(varx);
      BSetExpression valset = new BSetExpression();
      valset.addElement(val);
    
      BBinaryExpression union =
        new BBinaryExpression("\\/",fex,valset);
      BBinaryExpression eq = 
        new BBinaryExpression("=",varxbe,union);
      BBinaryExpression range =
        new BBinaryExpression(":",exxbe,ex);
      BExpression pred =
        new BBinaryExpression("&",range,eq);
      BSetComprehension res = 
        new BSetComprehension(args,pred);
      return res;
    }
    else if (opName.equals("removeAll" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { exx,varx | exx : ex &
      //                  varx = var(exx) - { val } }
      String exx = "e_xx";
      String varx = var + "_x";
      Vector args = new Vector();
      args.add(exx);
      args.add(varx);
      BBasicExpression exxbe =
        new BBasicExpression(exx);
      BApplyExpression fex = 
        new BApplyExpression(var,exxbe);
      BBasicExpression varxbe = 
        new BBasicExpression(varx);
      BSetExpression valset = new BSetExpression();
      valset.addElement(val);
    
      BBinaryExpression minus =
        new BBinaryExpression("-",fex,valset);
      BBinaryExpression eq = 
        new BBinaryExpression("=",varxbe,minus);
      BBinaryExpression range =
        new BBinaryExpression(":",exxbe,ex);
      BExpression pred =
        new BBinaryExpression("&",range,eq);
      BSetComprehension res = 
        new BSetComprehension(args,pred);
      return res;
    }
    else if (opName.equals("unionAll" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { exx,varx | exx : ex &
      //                  varx = var(exx) \/ val }
      String exx = "e_xx";
      String varx = var + "_x";
      Vector args = new Vector();
      args.add(exx);
      args.add(varx);
      BBasicExpression exxbe =
        new BBasicExpression(exx);
      BApplyExpression fex = 
        new BApplyExpression(var,exxbe);
      BBasicExpression varxbe = 
        new BBasicExpression(varx);
    
      BBinaryExpression union =
        new BBinaryExpression("\\/",fex,val);
      BBinaryExpression eq = 
        new BBinaryExpression("=",varxbe,union);
      BBinaryExpression range =
        new BBinaryExpression(":",exxbe,ex);
      BExpression pred =
        new BBinaryExpression("&",range,eq);
      BSetComprehension res = 
        new BSetComprehension(args,pred);
      return res;
    }
    else if (opName.equals("subtractAll" + var))
    { BExpression ex = (BExpression) arguments.get(0);
      BExpression val = (BExpression) arguments.get(1);
      // var := var <+ { exx,varx | exx : ex &
      //                  varx = var(exx) - val }
      String exx = "e_xx";
      String varx = var + "_x";
      Vector args = new Vector();
      args.add(exx);
      args.add(varx);
      BBasicExpression exxbe =
        new BBasicExpression(exx);
      BApplyExpression fex = 
        new BApplyExpression(var,exxbe);
      BBasicExpression varxbe = 
        new BBasicExpression(varx);
    
      BBinaryExpression minus =
        new BBinaryExpression("-",fex,val);
      BBinaryExpression eq = 
        new BBinaryExpression("=",varxbe,minus);
      BBinaryExpression range =
        new BBinaryExpression(":",exxbe,ex);
      BExpression pred =
        new BBinaryExpression("&",range,eq);
      BSetComprehension res = 
        new BSetComprehension(args,pred);
      return res;
    }
    return null;
  }

  public BStatement simplify()
  { return this; } 

  public BStatement seq2parallel()
  { return this; } 

  public BStatement normalise()
  { return this; } 

  public Vector rd()
  { Vector res = new Vector(); 
    for (int i = 0; i < arguments.size(); i++) 
    { BExpression arg = (BExpression) arguments.get(i); 
      res = VectorUtil.union(res,arg.rd()); 
    } 
    return res; 
  } 

  public Vector wr()
  { Vector res = new Vector(); 
    return res; 
  } 

} 
