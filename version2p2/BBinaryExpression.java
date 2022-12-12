import java.util.Vector; 

public class BBinaryExpression extends BExpression
{ private BExpression left;
  private BExpression right;
  private String operator;

  public BBinaryExpression(String op, BExpression l,
                           BExpression r)
  { operator = op;
    left = l;
    right = r;
  }

  public String getOperator()
  { return operator; }

  public BExpression getLeft()
  { return left; }

  public BExpression getRight()
  { return right; }

  public Vector rd()
  { Vector res = left.rd(); 
    res = VectorUtil.union(res,right.rd()); 
    return res; 
  } 

  public BExpression leftmostArgument()
  { if (left instanceof BBinaryExpression) 
    { BBinaryExpression bbe = (BBinaryExpression) left; 
      return bbe.leftmostArgument(); 
    } 
    return left; 
  }

  public boolean setValued()
  { // boolean leftset = left.setValued();
    //  boolean rightset = right.setValued();
    if (Expression.comparitors.contains(operator))
    { return false; }  // boolean valued
    if (operator.equals("\\/")) { return true; }
    if (operator.equals("/\\")) { return true; }
    if (operator.equals("<+")) { return true; }
    if (operator.equals("^")) { return true; }
    if (operator.equals("-") && 
        (left.setValued() || right.setValued()))
    { return true; }
    return false;
  }
    
  public BExpression simplify()
  { BExpression bl = left.simplify();
    BExpression br = right.simplify();
    if (bl == null) { bl = left; }
    if (br == null) { br = right; } 
    boolean leftset = bl.setValued();
    boolean rightset = br.setValued();
    if ("=".equals(operator))
    { if (leftset && !rightset)
      { br = BExpression.makeSet(br); }  // not bl
      else if (rightset && !leftset)
      { bl = BExpression.makeSet(bl); }  // not br
    }
    return simplify(operator,bl,br,needsBracket); 
  } 
    

  public static BExpression simplify(String op, BExpression l, BExpression r,
                                     boolean brack)
  { if (l == null) { return r; } 
    if (r == null) { return l; } 
    if (op.equals("="))
    { return simplifyEq(l,r); } 
    if (op.equals(":"))
    { return simplifyIn(l,r); } 
    if (op.equals("/:"))
    { return simplifyNin(l,r); } 
    if (op.equals("<:"))
    { return simplifySub(l,r); } 
    if (op.equals("/<:"))
    { BExpression e = simplifySub(l,r); 
      return simplifyNot(e); 
    } 
    if (op.equals("/="))
    { return simplifyNeq(l,r); } 
    if (op.equals("&"))
    { return simplifyAnd(l,r); } 
    if (op.equals("=>"))
    { return simplifyImp(l,r,brack); } 
    if (op.equals("or"))
    { return simplifyOr(l,r,brack); } 
    if (op.equals("+") || op.equals("/") || op.equals("*") || op.equals("-"))
    { return simplifyMath(op,l,r,brack); }
    return new BBinaryExpression(op,l,r);
  }

  private static BExpression simplifyEq(BExpression bl, BExpression br)
  { if (("" + bl).equals("" + br))
    { return new BBasicExpression("true"); } 
    if (bl instanceof BSetExpression &&
        br instanceof BSetExpression)
    { BSetExpression bs1 = (BSetExpression) bl;
      BSetExpression bs2 = (BSetExpression) br;
      if (bs1.isSingleton() && bs2.isSingleton())
      { return new BBinaryExpression("=",
                                     bs1.getElement(0),
                                     bs2.getElement(0));
      }
    }
    return new BBinaryExpression("=",bl,br); 
  } // Also if different VALUEs return false.

  public static BExpression simplifyIn(BExpression bl, BExpression br)
  { // BExpression newleft = bl; 
    String newop = ":"; 

    if (bl instanceof BSetExpression)
    { BSetExpression bls = (BSetExpression) bl;
      if (bls.isEmpty()) { return new BBasicExpression("true"); } 
      if (bls.isSingleton())
      { BExpression elem = (BExpression) bls.getElement(0); 
        return simplifyIn(elem,br); 
      } 
      else 
      { newop = "<:"; }  // : being used to mean subset
    } 

    if (br instanceof BSetExpression)
    { BSetExpression bs = (BSetExpression) br;
      if (bs.isEmpty()) 
      { if (bl instanceof BSetExpression)
        { BSetExpression bls = (BSetExpression) bl; 
          if (bls.isSingleton())
          { return new BBasicExpression("false"); } 
          else 
          { return new BBinaryExpression("=",bl,br); }
        }
        return new BBinaryExpression("=",bl,br);
      } 
      if (bs.isSingleton())
      { if (bl instanceof BSetExpression)
        { BSetExpression bls = (BSetExpression) bl;
          if (bls.isSingleton())
          { return new BBinaryExpression("=",bls.getElement(0),bs.getElement(0)); }
          return new BBinaryExpression("<:",bl,br); 
        }
        return new BBinaryExpression(":",bl,br);
      }
      if (bl instanceof BSetExpression)
      { BSetExpression bls = (BSetExpression) bl;
        if (bls.isSubsetOf(bs))
        { return new BBasicExpression("true"); } 
      } 
      if (bs.hasElement(bl))
      { return new BBasicExpression("true"); } 
    }
    return new BBinaryExpression(newop,bl,br); 
  } 

  public static BExpression simplifyNin(BExpression bl, BExpression br)
  { BExpression e = simplifyIn(bl,br); 
    return simplifyNot(e); 
  } 

    /* if (br instanceof BSetExpression)
    { BSetExpression bs = (BSetExpression) br;
      if (bs.isEmpty()) { return new BBasicExpression("true"); } 
      if (bs.isSingleton())
      { if (bl instanceof BSetExpression)  // must be singleton
        { BSetExpression bls = (BSetExpression) bl;
          return new BBinaryExpression("/=",bls.getElement(0),
                                       bs.getElement(0));
        }
        return new BBinaryExpression("/=",bl,
                                     bs.getElement(0));
      }
    }
    return new BBinaryExpression("/:",bl,br); 
  } */


  public static BExpression simplifyNeq(BExpression bl, BExpression br)
  { if ((""+bl).equals(""+br))
    { return new BBasicExpression("false"); } 
    if (bl instanceof BBinaryExpression &&
        br instanceof BSetExpression) 
    { BBinaryExpression blbe = (BBinaryExpression) bl; 
      BSetExpression bsetr = (BSetExpression) br; 
      if (blbe.operator.equals("/\\") && bsetr.isEmpty())
      { BExpression bll = blbe.left; 
        if (bll instanceof BSetExpression)
        { BSetExpression bllse = (BSetExpression) bll; 
          if (bllse.isSingleton())
          { BExpression belem = bllse.getElement(0); 
            return new BBinaryExpression(":",belem,blbe.right);
          } 
        }  
      }
    }
    BExpression e = simplifyEq(bl,br); 
    return simplifyNot(e); 
  }  

  public static BExpression simplifySub(BExpression bl, BExpression br)
  { if (("" + bl).equals("" + br))
    { return new BBasicExpression("true"); } 
    if (br instanceof BSetExpression)
    { BSetExpression bs = (BSetExpression) br;
      if (bl instanceof BSetExpression)
      { BSetExpression bsl = (BSetExpression) bl; 
        if (bsl.isSubsetOf(bs))
        { return new BBasicExpression("true"); } 
      } 
      if (bs.isEmpty()) 
      { return new BBinaryExpression("=",bl,new BSetExpression()); }
    }
    if (bl instanceof BSetExpression)
    { BSetExpression bsl = (BSetExpression) bl; 
      if (bsl.isEmpty()) { return new BBasicExpression("true"); } 
      if (bsl.isSingleton())
      { return new BBinaryExpression(":",bsl.getElement(0),br); }
      // if both singletons, it is = 
    }
    return new BBinaryExpression("<:",bl,br); 
  } 


  private static BExpression simplifyAnd(BExpression l, BExpression r)
  { if (l.equals(r)) { return l; }
    if (l.conflictsWith(r)) { return new BBasicExpression("false"); } 
    // if (l.implies(r)) { return l; } 
    // if (r.implies(l)) { return r; } 
    if ("TRUE".equals("" + l)) { return r; }
    if ("true".equals("" + l)) { return r; } 
    if ("TRUE".equals("" + r)) { return l; }
    if ("true".equals("" + r)) { return l; }   // etc
    if ("FALSE".equals("" + l)) { return l; }
    if ("false".equals("" + l)) { return l; }
    if ("FALSE".equals("" + r)) { return r; }
    if ("false".equals("" + r)) { return r; } 
    return new BBinaryExpression("&",l,r);
  }

  private static BExpression simplifyImp(BExpression l, BExpression r,
                                         boolean bracket)
  { if (l.equals(r)) { return new BBasicExpression("true"); }
    // and if (l.implies(r))
    if ("TRUE".equals("" + l)) { return r; }
    if ("true".equals("" + l)) { return r; } 
    if ("TRUE".equals("" + r)) { return new BBasicExpression("true"); }
    if ("true".equals("" + r)) { return r; }   
    if ("FALSE".equals("" + l)) { return new BBasicExpression("true"); }
    if ("false".equals("" + l)) { return new BBasicExpression("true"); }
    if ("FALSE".equals("" + r)) { return simplifyNot(l); }
    if ("false".equals("" + r)) { return simplifyNot(l); } 
    BExpression res = new BBinaryExpression("=>",l,r);
    res.setBrackets(bracket); 
    return res; 
  }

  private static BExpression simplifyOr(BExpression l, BExpression r,
                                        boolean bracket)
  { if (l.equals(r)) { return l; }
    // if (l.implies(r)) { return r; } 
    // if (r.implies(l)) { return l; } 
    if ("TRUE".equals("" + l)) { return new BBasicExpression("true"); }
    if ("true".equals("" + l)) { return new BBasicExpression("true"); } 
    if ("TRUE".equals("" + r)) { return new BBasicExpression("true"); }
    if ("true".equals("" + r)) { return new BBasicExpression("true"); }   // etc
    if ("FALSE".equals("" + l)) { return r; }
    if ("false".equals("" + l)) { return r; }
    if ("FALSE".equals("" + r)) { return l; }
    if ("false".equals("" + r)) { return l; } 
    BExpression res = new BBinaryExpression("or",l,r);
    res.setBrackets(bracket);
    return res; 
  }

  private static BExpression simplifyMath(String op, BExpression l,
                                          BExpression r, boolean brack)
  { BExpression res; 
    BExpression ls = unmakeSet(l);
    BExpression rs = unmakeSet(r);
    if (op.equals("-"))
    { if (l.setValued() || r.setValued())  // ls, rs?
      { res = new BBinaryExpression(op,l,r);
        res.setBrackets(brack); 
        return res; 
      }
    }
    res = new BBinaryExpression(op,ls,rs);
    res.setBrackets(brack); 
    return res; 
  } // could also evaluate them.

  public static BExpression simplifyNot(BExpression e)
  { // turn < into >= etc
    if ("FALSE".equals("" + e)) { return new BBasicExpression("true"); }
    if ("false".equals("" + e)) { return new BBasicExpression("true"); }
    if ("TRUE".equals("" + e)) { return new BBasicExpression("false"); }
    if ("true".equals("" + e)) { return new BBasicExpression("false"); }
    if (e instanceof BBinaryExpression)
    { BBinaryExpression be = (BBinaryExpression) e; 
      String beop = be.operator; 
      String newop = null; 
      if (beop.equals("=")) { newop = "/="; }
      if (beop.equals("<=")) { newop = ">"; } 
      if (beop.equals("<")) { newop = ">="; } 
      if (beop.equals(">=")) { newop = "<"; } 
      if (beop.equals("/=")) { newop = "="; } 
      if (beop.equals("!=")) { newop = "="; } 
      if (beop.equals(">")) { newop = "<="; } 
      if (beop.equals(":")) { newop = "/:"; } 
      if (beop.equals("/:")) { newop = ":"; } 
      if (beop.equals("<:")) { newop = "/<:"; } 
      if (beop.equals("/<:")) { newop = "<:"; }
      // for &, or, =>, not also  
      if (newop != null)
      { return new BBinaryExpression(newop,be.left,be.right); }
    } 
    return new BUnaryExpression("not",e); 
  }

  public String toString() 
  { String res = left + " " + operator + " " + right; 
    if (needsBracket)
    { return "( " + res + " )"; }  // eg, for or inside & 
    else
    { return res; }
  } 

  public BExpression substituteEq(String oldE, BExpression newE)
  { if (oldE.equals(toString()))
    { return newE; } 
    BExpression le = left.substituteEq(oldE,newE); 
    BExpression re = right.substituteEq(oldE,newE); 
    BExpression res = new BBinaryExpression(operator,le,re);
    res.setBrackets(needsBracket); 
    return res; 
  } // copy kind also

  public boolean conflictsWith(BExpression e)  
  { if (e instanceof BBinaryExpression)
    { BBinaryExpression be = (BBinaryExpression) e;
      if (be.operator.equals("&"))
      { return conflictsWith(be.left) ||
               conflictsWith(be.right);
      }
      if (be.operator.equals("or"))
      { return conflictsWith(be.left) &&
               conflictsWith(be.right);
      }
      else 
      { return conflictsWith(be.operator,
                             be.left,be.right);
      }
    }
    return false;
  }

  public boolean conflictsWith(String op, BExpression el,
                               BExpression er)
  { if (left.toString().equals(el.toString()) &&
        right.toString().equals(er.toString()))
    { return Expression.conflictsOp(operator,op); }
    if (left.toString().equals(er.toString()) &&
        right.toString().equals(el.toString()))
    { return Expression.conflictsReverseOp(operator,op); }
    if (operator.equals("="))
    { return conflictsWithEq(op,el,er); }
//  if (comparitors.contains(operator))
//  { return conflictsWithComp(op,el,er); }
    if (operator.equals("&"))  // shouldn't occur
    { return left.conflictsWith(op,el,er) ||
             right.conflictsWith(op,el,er);
    }
    if (operator.equals("or"))
    { return left.conflictsWith(op,el,er) &&
             right.conflictsWith(op,el,er);
    }
    return false;
  }

  public boolean conflictsWithEq(String op, BExpression el,
                                 BExpression er)
  { if (op.equals("="))
    { if (left.toString().equals(el.toString()) &&
          right.getKind() == Expression.VALUE && 
          er.getKind() == Expression.VALUE &&
          !right.toString().equals(er.toString()))
      { return true; }
    }
    return false;
  }
}

