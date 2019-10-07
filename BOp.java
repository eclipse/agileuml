import java.util.Vector;
import java.util.List; 

/* package: B */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BOp extends ModelElement
{ private String result = null;
  private List params = new Vector(); // of String
  private BExpression pre;
  private BStatement code;
  private String signature; 

  public BOp(String nme, String res, List pars, 
             BExpression p, BStatement s)
  { super(nme);
    result = res;
    params = pars;
    pre = p;
    code = s;
  }

  public void setSignature(String sig)
  { signature = sig; } 

  public String getSignature() 
  { return signature; } 

  public String getResult()
  { return result; } 

  public List getParameters()
  { return params; } 

  public BExpression getPre()
  { return pre; } 

  public void addPre(BExpression p)
  { pre = BBinaryExpression.simplify("&",pre,p,false); } 

  public BStatement getCode()
  { return code; } 

  public void generateJava(java.io.PrintWriter out)  // not used
  { out.println("  public void " + getName() + "()"); } 

  public static BOp buildGetOperation(
                             String nme, String cx,
                             BExpression cs)
  // nmex <-- getnme(cx) = 
  //   PRE cx: cs THEN nmex := nme(cx) END
  { Vector pars = new Vector();
    String opname = "get" + nme;
    String attx = nme + "x"; 
    pars.add(cx);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression attxbe = 
                       new BBasicExpression(attx);
    BBinaryExpression pre1 = 
      new BBinaryExpression(":",cxbe,cs);
    BExpression rhs = new BApplyExpression(nme,cxbe);
    BStatement code = new BAssignStatement(attxbe,rhs);
    return new BOp(opname,attx,pars,pre1,code);
  }

  public static BOp buildGetQualOperation(
                             String nme, String cx,
                             BExpression cs)
  // nmex <-- getnme(cx,ind) = 
  //   PRE cx: cs THEN nmex := nme(cx)(ind) END
  { Vector pars = new Vector();
    String opname = "get" + nme;
    String attx = nme + "x"; 

    pars.add(cx);
    pars.add("ind"); 
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression indbe = new BBasicExpression("ind"); 

    BBasicExpression attxbe = 
                       new BBasicExpression(attx);

    BBinaryExpression pre1 = 
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 = 
      new BBinaryExpression(":",indbe,new BBasicExpression("STRING")); 
    BBinaryExpression oppre = 
      new BBinaryExpression("&",pre1,pre2); 

    BExpression rhs = new BApplyExpression(nme,cxbe);
    BStatement code = new BAssignStatement(attxbe,
                            new BApplyExpression(rhs,indbe));
    return new BOp(opname,attx,pars,oppre,code);
  }

  public static BOp buildStaticGetOperation(String nme)
  // nmex <-- getnme = 
  //   nmex := nme; 
  { Vector pars = new Vector();
    String opname = "get" + nme;
    String attx = nme + "x"; 
    
    BBasicExpression attxbe = 
                       new BBasicExpression(attx);
    BExpression rhs = new BBasicExpression(nme);
    BStatement code = new BAssignStatement(attxbe,rhs);
    return new BOp(opname,attx,pars,null,code);
  }

  public static BOp buildUpdateOperation(String nme, BExpression cs, 
                                  Association ast, BExpression btbe, boolean inj)
  // updatenme(nmex) = 
  //   PRE nmex: cs +-> T THEN nme := nme <+ nmex END
  { Vector pars = new Vector();
    String opname = "update" + nme;
    String attx = nme + "x"; 
    pars.add(attx);
    BBasicExpression nmebe = new BBasicExpression(nme); 
    BBasicExpression attxbe = 
                       new BBasicExpression(attx);
    BExpression pfun = new BBinaryExpression("+->",cs,btbe); 
    BBinaryExpression pre =
      new BBinaryExpression(":",attxbe,pfun);

    BExpression rhs = new BBinaryExpression("<+",nmebe,attxbe);
    BStatement code = new BAssignStatement(nmebe,rhs);

    if (inj) 
    { // rhs: cs >-> btbe
      BExpression inje = new BBinaryExpression(">->",cs,btbe); 
      BExpression injpre = new BBinaryExpression(":",rhs,inje); 
      pre = new BBinaryExpression("&",pre,injpre); 
    } 

    /* if (ast != null) 
    { String role1 = ast.getRole1(); 
      if (role1 != null && role1.length() > 0)
      { int card1 = ast.getCard1(); 
        int card2 = ast.getCard2(); 
        Entity ent1 = ast.getEntity1(); 
        Entity ent2 = ast.getEntity2(); 
        BParallelStatement body = new BParallelStatement(); 
        code.setWriteFrame(nme); 
        body.addStatement(code); 

        if (card2 != ONE)
        { // role1 := { bxx,axx | axx : dom(attxbe) & bxx : attxbe(axx) }, ...
          BExpression role1be = new BBasicExpression(role1); 
          String e1name = ent1.getName();
          String e2name = ent2.getName(); // should be different 
          String e1var = e1name.toLowerCase() + "_xx"; 
          String e2var = e2name.toLowerCase() + "_xx"; 
          String e1s = e1name.toLowerCase() + "s"; 
          BExpression e1sbe = new BBasicExpression(e1s); 
          Vector scompvars = new Vector(); 
          scompvars.add(e2var);
          scompvars.add(e1var); 
          BExpression e2varbe = new BBasicExpression(e2var); 
          BExpression e1varbe = new BBasicExpression(e1var);
          BExpression attxapp = new BApplyExpression(attx,e1varbe);
          BExpression domattx = new BUnaryExpression("dom",attxbe); 

          BExpression pred11 = new BBinaryExpression(":",e1varbe,domattx); 
          BExpression pred12 = new BBinaryExpression(":",e2varbe,attxapp); 
          BExpression pred1 = new BBinaryExpression("&",pred11,pred12); 
          BExpression comp1 = new BSetComprehension(scompvars,pred1); 
          BExpression role2app = new BApplyExpression(nme, e1varbe);         
          BExpression role1sub = new BBinaryExpression(":",e1varbe,e1sbe);
          BExpression pred21 = new BBinaryExpression("/:",e1varbe,domattx);
          BExpression pred22 = new BBinaryExpression(":",e2varbe,role2app);
          BExpression pred2 = new BBinaryExpression("&",pred22,pred21);
          BExpression comp2 = new BSetComprehension(scompvars,
                                    new BBinaryExpression("&",role1sub,pred2)); 
          BExpression invrhs = new BBinaryExpression("\\/", comp1, comp2);        
          BStatement invcode = new BAssignStatement(role1be,invrhs); 
          invcode.setWriteFrame(role1); 
          body.addStatement(invcode); 
          return new BOp(opname,null,pars,pre,body);
        } 
      } 
    } */


    /* if (card2 == ModelElement.ZEROONE)
    { String cname = cs.substring(0,cs.length()-1); 
      BExpression zpre = zeroOneConstraint(nme,cname,"update",attx);
      pre = new BBinaryExpression("&",pre,zpre); 
    } */
    return new BOp(opname,null,pars,pre,code);
  }
  
  public static BOp buildSetOperation(
                         String nme, String cx,
                         BExpression cs,
                         BExpression btbe, boolean inj, String role1, int card1, int card2,
                         Entity ent, Vector entities, Vector types, Vector invs)
  // setnme(cx,nmex) = 
  //   PRE cx: cs & nmex: T THEN nme(cx) := attx END
  { Vector pars = new Vector();
    String opname = "set" + nme;
    String attx = nme + "xx"; 
    String e2name = ""; 

    Type t; 
    Attribute att = ent.getDefinedAttribute(nme); 
    if (att != null)
    { t = att.getType(); } 
    else 
    { Association ast = ent.getRole(nme); 
      Entity ent2 = ast.getEntity2(); 
      e2name = ent2 + ""; 

      if (ast != null && card2 == ModelElement.ONE)
      { t = new Type(ent2); } 
      else 
      { t = new Type("Set",null); } 
    } // and element type is Entity2 in 2nd case?
    pars.add(cx);
    pars.add(attx);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression attxbe = 
                       new BBasicExpression(attx);
    BBinaryExpression pre1 = 
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",attxbe,btbe);
    BExpression pre = 
      new BBinaryExpression("&",pre1,pre2);  // and card(attxbe) = card2 for n
    if (inj) 
    { String cname = cx.substring(0,cx.length()-1); 
      BExpression inje = injConstraint(nme,cname,attx); 
      pre = new BBinaryExpression("&",pre,inje); 
    } 
    if (card2 == ModelElement.ZEROONE)
    { String cname = cx.substring(0,cx.length()-1); 
      BExpression zpre = zeroOneConstraint(nme,cname,"set",attx);
      pre = new BBinaryExpression("&",pre,zpre); 
    }

    BExpression lhs = new BApplyExpression(nme,cxbe);
    BStatement code = new BAssignStatement(lhs,attxbe);
    code.setWriteFrame(nme); 
    BParallelStatement body = new BParallelStatement();
    body.addStatement(code); 

    if (role1 != null && role1.length() > 0)
    { BExpression role1be = new BBasicExpression(role1); 
      String e1name = ent.getName(); 
      String e1var = e1name.toLowerCase() + "_xx"; 
      String e2var = e2name.toLowerCase() + "_xx"; 
      String e2s = e2name.toLowerCase() + "s"; 
      BExpression e2sbe = new BBasicExpression(e2s); 

      Vector cxset = new Vector(); 
      cxset.add(cxbe); 
      BExpression cxsetbe = new BSetExpression(cxset,false); 

      if (card2 == MANY) 
      { if (card1 == ONE)
        { // role1 := (attxbe <<| role1) \\/ attxbe*{cxbe}
          BExpression rhs1 = new BBinaryExpression("<<|",attxbe,role1be);
          rhs1.setBrackets(true);  
          BExpression rhs2 = new BBinaryExpression("*",attxbe,cxsetbe); 
          BExpression invrhs = new BBinaryExpression("\\/",rhs1,rhs2); 
          BStatement invcode = new BAssignStatement(role1be,invrhs); 
          invcode.setWriteFrame(role1); 
          body.addStatement(invcode); 
        }            
        else  
        { Vector scompvars = new Vector(); 
          scompvars.add(e2var);
          scompvars.add(e1var); 
          BExpression e2varbe = new BBasicExpression(e2var); 
          BExpression e1varbe = new BBasicExpression(e1var); 
          BExpression pred11 = new BBinaryExpression(":",e2varbe,attxbe); 
          BExpression role1app = new BApplyExpression(role1,e2varbe);
          BExpression role1add = new BBinaryExpression("\\/",role1app,cxsetbe);             
          BExpression pred12 = new BBinaryExpression("=",e1varbe,role1add); 
          BExpression pred1 = new BBinaryExpression("&",pred11,pred12); 
          BExpression comp1 = new BSetComprehension(scompvars,pred1); 
          BExpression role1sub = new BBinaryExpression("-",role1app,cxsetbe);
          BExpression pred21 = new BBinaryExpression("/:",e2varbe,attxbe);
          BExpression pred22 = new BBinaryExpression(":",e2varbe,e2sbe);
          BExpression pred2 = new BBinaryExpression("&",pred22,pred21);
          BExpression pred23 = new BBinaryExpression("=",e1varbe,role1sub); 
          BExpression comp2 = new BSetComprehension(scompvars,
                                    new BBinaryExpression("&",pred2,pred23)); 
          BExpression invrhs = new BBinaryExpression("\\/", comp1, comp2);        
          BStatement invcode = new BAssignStatement(role1be,invrhs); 
          invcode.setWriteFrame(role1); 
          body.addStatement(invcode); 
        } // role1 := { bxx,axx | bxx : brx & axx = ar(bxx) \\/ { ax } } \\/ ... 
      }
      else if (card2 == ONE && card1 != ONE)
      { // role1 := { bx |-> ar(bx) \\/ {ax} } \\/ { bb,aa | bb : bs & bb /= bx & aa : ar(bx) - { ax } }
        Vector scompvars = new Vector(); 
        scompvars.add(e2var);
        scompvars.add(e1var); 
        BExpression e2varbe = new BBasicExpression(e2var); 
        BExpression e1varbe = new BBasicExpression(e1var); 
        BExpression pred11 = new BBinaryExpression("=",e2varbe,attxbe); 
        BExpression role1app = new BApplyExpression(role1,e2varbe);
        BExpression role1add = new BBinaryExpression("\\/",role1app,cxsetbe);             
        BExpression pred12 = new BBinaryExpression("=",e1varbe,role1add); 
        BExpression pred1 = new BBinaryExpression("&",pred11,pred12); 
        BExpression comp1 = new BSetComprehension(scompvars,pred1); 
        BExpression role1sub = new BBinaryExpression("-",role1app,cxsetbe);
        BExpression pred21 = new BBinaryExpression("/=",e2varbe,attxbe);
        BExpression pred22 = new BBinaryExpression(":",e2varbe,e2sbe);
        BExpression pred2 = new BBinaryExpression("&",pred22,pred21);
        BExpression pred23 = new BBinaryExpression("=",e1varbe,role1sub); 
        BExpression comp2 = new BSetComprehension(scompvars,
                                    new BBinaryExpression("&",pred2,pred23)); 
        BExpression invrhs = new BBinaryExpression("\\/", comp1, comp2);        
        BStatement invcode = new BAssignStatement(role1be,invrhs); 
        invcode.setWriteFrame(role1); 
        body.addStatement(invcode); 
      } 

    } 
   
     
    Vector vpars = new Vector(); 
    // and the attx parameter
    Attribute p1 = new Attribute(attx,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event =
      new BehaviouralFeature("set" + nme,vpars,false,null);

    String ename = ent.getName();
    java.util.Map env = new java.util.HashMap(); 
    env.put(ename, new BBasicExpression(cx)); 

    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("set",nme,ent,attx,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        BStatement stat; 
        if (typed)
        { stat = ccnew.synthesiseBCode(ent,nme,true);
          body.addStatement(stat);
        } 
      }
      else if (con.getEvent() == null && con.involvesFeature(nme))
      { BExpression inv = con.binvariantForm(env,false); 
        inv.setBrackets(true); 
        BExpression inv1 = inv.substituteEq(nme + "(" + cx + ")", attxbe);  
        pre = new BBinaryExpression("&",pre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!
    }
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }


  public static BOp buildStaticSetOperation(
                             String nme, String cx, BExpression cs,
                             BExpression btbe,
                             boolean inj, int card2,
                             Entity ent, Vector entities,
                             Vector types, Vector invs)
  // setnme(nmex) = 
  //   PRE nmex: T THEN nme := nmex END
  { Vector pars = new Vector();
    String opname = "set" + nme;
    String attx = nme + "x"; 
    
    pars.add(attx);
    BBasicExpression attxbe = 
                       new BBasicExpression(attx);
    BExpression pre = new BBinaryExpression(":",attxbe,btbe);
    BExpression lhs = new BBasicExpression(nme);
    BStatement code = new BAssignStatement(lhs,attxbe);
    BParallelStatement body = new BParallelStatement(); 
    body.addStatement(code); 

    Type t;
    Attribute att = ent.getAttribute(nme); 
    if (att != null)
    { t = att.getType(); } 
    else
    { return null; }

    Vector vpars = new Vector();
    // and the attx parameter
    Attribute p1 = new Attribute(attx,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event = new BehaviouralFeature("set" + nme,vpars,false,null);

    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("set",nme,ent,attx,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        BStatement stat; 
        if (typed)
        { stat = ccnew.staticSynthesiseBCode(ent,nme,true);
          body.addStatement(stat);
        } 
      }
    }        
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }

  // Not valid for {unique} attributes:
  public static BOp buildSetAllOperation(String nme,
                      String cx, BExpression cs,
                      BExpression btbe)
  // setAllnme(cx,nmex) =
  //   PRE cx <: cs & nmex: T 
  //   THEN nme := nme <+ (cx*{nmex}) END
  { Vector pars = new Vector();
    String opname = "setAll" + nme;
    String nmex = nme + "x";
    pars.add(cx);
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBasicExpression nmebe = new BBasicExpression(nme);
    BBinaryExpression pre1 =
      new BBinaryExpression("<:",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BExpression pre = 
      new BBinaryExpression("&",pre1,pre2);
    Vector nvals = new Vector();
    nvals.add(nmexbe);
    BExpression nmeset = new BSetExpression(nvals);

    BExpression update =
      new BBinaryExpression("*",cxbe,nmeset);
    BExpression rhs =
      new BBinaryExpression("<+",nmebe,update);
    BStatement code =
      new BAssignStatement(nmebe,rhs);
    return new BOp(opname,null,pars,pre,code);
  }

  public static BOp buildSetIndOp(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe, Entity ent)
  // setnme(cx,ind,e2x) = 
  //   PRE cx: cs & ind: dom(nme(cx)) & e2x: btbe
  //   THEN nme(cx)(ind) := e2x END
  { Vector pars = new Vector();
    String opname = "set" + nme;
    Type t; 
    Association ast = ent.getRole(nme); 
    t = new Type(ast.getEntity2());  
    
    String e2x = ast.getEntity2().getName().toLowerCase() + "xx"; 

    pars.add(cx);
    pars.add("ind"); 
    pars.add(e2x);

    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression e2xbe = 
                       new BBasicExpression(e2x);
    BBasicExpression indbe = new BBasicExpression("ind"); 

    BBinaryExpression pre1 = 
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",e2xbe,btbe);
    BExpression pre = 
      new BBinaryExpression("&",pre1,pre2);  

    BExpression lhs = new BApplyExpression(nme,cxbe);
    BExpression indom = new BBinaryExpression(":",indbe,
                          new BUnaryExpression("dom",lhs)); 
    BExpression lhs1 = new BApplyExpression(lhs,indbe); 

    BStatement code = new BAssignStatement(lhs1,e2xbe);
    code.setWriteFrame(nme); 
    pre = new BBinaryExpression("&",pre,indom); 

    return new BOp(opname,null,pars,pre,code);
  }

  public static BOp buildSetQualOp(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe, Entity ent)
  // setnme(cx,ind,e2x) = 
  //   PRE cx: cs & ind: STRING & e2x: btbe
  //   THEN nme(cx)(ind) := e2x END
  { Vector pars = new Vector();
    String opname = "set" + nme;
    Type t; 
    Association ast = ent.getRole(nme); 
    t = new Type(ast.getEntity2());  
    
    String e2x = ast.getEntity2().getName().toLowerCase() + "xx"; 

    pars.add(cx);
    pars.add("ind"); 
    pars.add(e2x);

    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression e2xbe = 
                       new BBasicExpression(e2x);
    BBasicExpression indbe = new BBasicExpression("ind"); 

    BBinaryExpression pre1 = 
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",e2xbe,btbe);
    BExpression pre = 
      new BBinaryExpression("&",pre1,pre2);  

    BExpression lhs = new BApplyExpression(nme,cxbe);
    BExpression indom = new BBinaryExpression(":",indbe,
                          new BBasicExpression("STRING")); 
    BExpression lhs1 = new BApplyExpression(lhs,indbe); 

    BStatement code = new BAssignStatement(lhs1,e2xbe);
    code.setWriteFrame(nme); 
    pre = new BBinaryExpression("&",pre,indom); 

    return new BOp(opname,null,pars,pre,code);
  }




  public static BOp buildDelOperation(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe, String role1, int card1, 
                Entity ent, Vector entities, Vector types, Vector invs)
  // removenme(cx,nmex) =
  //   PRE cx: cs & nmex: T
  //   THEN nme(cx) := nme(cx) - { nmex } END, or more complex if nme is ordered
  { Vector pars = new Vector();
    String opname = "remove" + nme;
    String nmex = nme + "xx";
    pars.add(cx);
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBinaryExpression pre1 =
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    BExpression lhs = new BApplyExpression(nme,cxbe);
    Vector vals = new Vector();
    vals.add(nmexbe);
    BSetExpression nmese = new BSetExpression(vals);
    BExpression rhs =
      new BBinaryExpression("-",lhs,nmese);
    rhs.setBrackets(true); 
    if (ent.getRole(nme).isOrdered())
    { BBasicExpression nmebe = new BBasicExpression(nme); 
      BBinaryExpression rresf = new BBinaryExpression("|>>",nmebe,nmese);
      Vector subparams = new Vector();
      subparams.add("_ii_");
      subparams.add("_yy_");
      BUnaryExpression domf = new BUnaryExpression("dom", rresf);
      BBasicExpression jjbe = new BBasicExpression("_jj_"); 
      BBinaryExpression rge = new BBinaryExpression("..", new BBasicExpression("1"),  jjbe);
      BBinaryExpression intrsec = new BBinaryExpression("/\\", domf, rge);
      BUnaryExpression cardintrsec = new BUnaryExpression("card", intrsec);
      BBasicExpression iibe = new BBasicExpression("_ii_"); 
      BBinaryExpression iieq = new BBinaryExpression("=", iibe, cardintrsec);
      BBinaryExpression jjindom = new BBinaryExpression(":", jjbe, domf);
      BApplyExpression appf = new BApplyExpression(nmebe, jjbe);
      BBasicExpression yybe = new BBasicExpression("_yy_"); 
      BBinaryExpression yyeq = new BBinaryExpression("=", yybe, appf);
      BBinaryExpression and1 = new BBinaryExpression("&", jjindom, yyeq);
      BBinaryExpression and2 = new BBinaryExpression("&", and1, iieq);
      BQuantifierExpression exists1 = new BQuantifierExpression("#", "_jj_", and2);
      rhs = new BSetComprehension(subparams, exists1);
      lhs = nmebe; 
    }
    java.util.Map env = new java.util.HashMap(); 
    env.put(ent.getName(), new BBasicExpression(cx)); 

    BStatement code = new BAssignStatement(lhs,rhs);
    code.setWriteFrame(nme); 
    BParallelStatement body = new BParallelStatement(); 
    body.addStatement(code); 

    if (role1 != null && role1.length() > 0)
    { if (card1 != ONE) 
      { BExpression invlhs = new BApplyExpression(role1,nmexbe);
        Vector invvals = new Vector();
        invvals.add(cxbe);
        BSetExpression role1se = new BSetExpression(invvals,false);
        BExpression invrhs = new BBinaryExpression("-",invlhs,role1se); 
        invrhs.setBrackets(true);
        BStatement inversecode = new BAssignStatement(invlhs,invrhs); 
        inversecode.setWriteFrame(role1); 
        body.addStatement(inversecode); 
      } 
      else // role1 := role1 domain anti-restriction { nmexbe } 
      { BExpression role1be = new BBasicExpression(role1); 
        Vector invvals = new Vector();
        invvals.add(nmexbe);
        BSetExpression role1se = new BSetExpression(invvals,false);
        
        BExpression invrhs = new BBinaryExpression("<<|",role1se,role1be); 
        BStatement inversecode = new BAssignStatement(role1be,invrhs); 
        inversecode.setWriteFrame(role1); 
        body.addStatement(inversecode); 
      } 
    } 


    Vector vpars = new Vector(); 
    // and the attx parameter
    Association ast = ent.getDefinedRole(nme); 
    Type t = new Type(ast.getEntity2()); 
      
    Attribute p1 = new Attribute(nmex,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event = new BehaviouralFeature("remove" +
                                                      nme,vpars,false,null);
    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("remove",nme,ent,nmex,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        if (typed)
        { BStatement stat = ccnew.bupdateOperation(ent,true);
          body.addStatement(stat);
        } 
      }
      else if (con.getEvent() == null && con.involvesFeature(nme))
      { BExpression inv = con.binvariantForm(env,false); 
        inv.setBrackets(true); 
      
        BExpression inv1 = inv.substituteEq(nme + "(" + cx + ")", rhs);  
        pre = new BBinaryExpression("&",pre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!

    }        
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }

  public static BOp buildDelQualOperation(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe,
                Entity ent, Vector entities, Vector types, Vector invs)
  // removenme(cx,ind,nmex) =
  //   PRE cx: cs & ind : STRING & nmex: T
  //   THEN nme(cx)(ind) := nme(cx)(ind) - { nmex } END
  { Vector pars = new Vector();
    String opname = "remove" + nme;
    String nmex = nme + "xx";
    pars.add(cx);
    pars.add("ind"); 
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBasicExpression indbe = new BBasicExpression("ind"); 

    BBinaryExpression pre1 =
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BBinaryExpression pre3 = 
      new BBinaryExpression(":",indbe,new BBasicExpression("STRING")); 
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    pre = new BBinaryExpression("&",pre,pre3); 

    BExpression lhs = new BApplyExpression(nme,cxbe);
    lhs = new BApplyExpression(lhs,indbe); 

    Vector vals = new Vector();
    vals.add(nmexbe);
    BSetExpression nmese = new BSetExpression(vals);
    BExpression rhs =
      new BBinaryExpression("-",lhs,nmese);
    rhs.setBrackets(true); 

    java.util.Map env = new java.util.HashMap(); 
    env.put(ent.getName(), new BBasicExpression(cx)); 

    BStatement code = new BAssignStatement(lhs,rhs);
    code.setWriteFrame(nme); 
    BParallelStatement body = new BParallelStatement(); 
    body.addStatement(code); 

    Vector vpars = new Vector(); 
    // and the attx parameter
    Association ast = ent.getDefinedRole(nme); 
    Type t = new Type(ast.getEntity2()); 
      
    Attribute p1 = new Attribute(nmex,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event = new BehaviouralFeature("remove" +
                                                      nme,vpars,false,null);
    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("remove",nme,ent,nmex,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        if (typed)
        { BStatement stat = ccnew.bupdateOperation(ent,true);
          body.addStatement(stat);
        } 
      }
      else if (con.getEvent() == null && con.involvesFeature(nme))
      { BExpression inv = con.binvariantForm(env,false); 
        inv.setBrackets(true); 
      
        BExpression inv1 = inv.substituteEq(nme + "(" + cx + ")", rhs);  
        pre = new BBinaryExpression("&",pre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!

    }        
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }

  public static BOp buildAddOperation(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe,
                             String role1, int card1, int card2, boolean isseq,
                    Entity ent, Vector entities, Vector types, Vector invs)
  // addnme(cx,nmex) =
  //   PRE cx: cs & nmex: T &
  //       card(nme(cx) \/ {nmex}) <= 1 (for ZEROONE)
  //   THEN nme(cx) := nme(cx) \/ { nmex } END or ^ [nmex] for a sequence
  //    inverse:  role1(nmex) := role1(nmex) \/ { cx } 
  { Vector pars = new Vector();
    String opname = "add" + nme;
    String nmex = nme + "xx";
    pars.add(cx);
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBinaryExpression pre1 =
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    BExpression lhs = new BApplyExpression(nme,cxbe);
    Vector vals = new Vector();
    vals.add(nmexbe);
    BSetExpression nmese = new BSetExpression(vals,isseq);
    BExpression rhs;
    if (isseq)
    { rhs = new BBinaryExpression("^",lhs,nmese); } 
    else 
    { rhs = new BBinaryExpression("\\/",lhs,nmese); }
    rhs.setBrackets(true); 

    java.util.Map env = new java.util.HashMap(); 
    env.put(ent.getName(), new BBasicExpression(cx)); 

    if (card2 == ZEROONE)
    { BExpression cardleq =
        new BBinaryExpression("<=",
          new BUnaryExpression("card",rhs),
            new BBasicExpression("1"));
      pre = new BBinaryExpression("&",pre,cardleq);
    }
    BStatement code = new BAssignStatement(lhs,rhs);
    code.setWriteFrame(nme); 
    BParallelStatement body = new BParallelStatement(); 
    body.addStatement(code); 

    if (role1 != null && role1.length() > 0)
    { if (card1 != ONE) 
      { BExpression invlhs = new BApplyExpression(role1,nmexbe);
        Vector invvals = new Vector();
        invvals.add(cxbe);
        BSetExpression role1se = new BSetExpression(invvals,false);
        BExpression invrhs = new BBinaryExpression("\\/",invlhs,role1se); 
        invrhs.setBrackets(true);
        BStatement inversecode = new BAssignStatement(invlhs,invrhs); 
        inversecode.setWriteFrame(role1); 
        body.addStatement(inversecode); 
      } 
      else // role1(nmexbe) := cx
      { BExpression invlhs = new BApplyExpression(role1,nmexbe);
        BStatement inversecode = new BAssignStatement(invlhs,cxbe); 
        inversecode.setWriteFrame(role1); 
        body.addStatement(inversecode); 
      } 
    } 

    Vector vpars = new Vector(); 
    // and the attx parameter
    Association ast = ent.getDefinedRole(nme); 
    Type t = new Type(ast.getEntity2()); 
      
    Attribute p1 = new Attribute(nmex,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event = new BehaviouralFeature("add" + nme,vpars,false,null);

    if (isseq)
    { event.setOrdered(true); } 

    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("add",nme,ent,nmex,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        if (typed)
        { BStatement stat = ccnew.bupdateOperation(ent,true);
          body.addStatement(stat);
        } 
      }
      else if (con.getEvent() == null && con.involvesFeature(nme))
      { BExpression inv = con.binvariantForm(env,false); 
        inv.setBrackets(true); 
      
        BExpression inv1 = inv.substituteEq(nme + "(" + cx + ")", rhs);  
        pre = new BBinaryExpression("&",pre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!

    }        
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }

  public static BOp buildAddQualOperation(
                             String nme, String cx,
                             BExpression cs,
                             BExpression btbe,
                             int card2, boolean isseq,
                    Entity ent, Vector entities, Vector types, Vector invs)
  // addnme(cx,ind,nmex) =
  //   PRE cx: cs & ind : STRING & nmex: T &
  //       card(nme(cx)(ind) \/ {nmex}) <= 1 (for ZEROONE)
  //   THEN nme(cx)(ind) := nme(cx)(ind) \/ { nmex } END or ^ [nmex] for a sequence
  { Vector pars = new Vector();
    String opname = "add" + nme;
    String nmex = nme + "xx";
    pars.add(cx);
    pars.add("ind"); 
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBasicExpression indbe = new BBasicExpression("ind"); 
    BBinaryExpression pre1 =
      new BBinaryExpression(":",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BBinaryExpression pre3 = 
      new BBinaryExpression(":",indbe,new BBasicExpression("STRING")); 
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    pre = new BBinaryExpression("&",pre,pre3); 
    BExpression lhs = new BApplyExpression(nme,cxbe);
    lhs = new BApplyExpression(lhs,indbe); 

    Vector vals = new Vector();
    vals.add(nmexbe);
    BSetExpression nmese = new BSetExpression(vals,isseq);
    BExpression rhs;
    if (isseq)
    { rhs = new BBinaryExpression("^",lhs,nmese); } 
    else 
    { rhs = new BBinaryExpression("\\/",lhs,nmese); }
    rhs.setBrackets(true); 

    java.util.Map env = new java.util.HashMap(); 
    env.put(ent.getName(), new BBasicExpression(cx)); 

    if (card2 == ZEROONE)
    { BExpression cardleq =
        new BBinaryExpression("<=",
          new BUnaryExpression("card",rhs),
            new BBasicExpression("1"));
      pre = new BBinaryExpression("&",pre,cardleq);
    }
    BStatement code = new BAssignStatement(lhs,rhs);
    code.setWriteFrame(nme); 
    BParallelStatement body = new BParallelStatement(); 
    body.addStatement(code); 

    Vector vpars = new Vector(); 
    // and the attx parameter
    Association ast = ent.getDefinedRole(nme); 
    Type t = new Type(ast.getEntity2()); 
      
    Attribute p1 = new Attribute(nmex,t,ModelElement.INTERNAL); 
    vpars.add(p1); 
    BehaviouralFeature event = new BehaviouralFeature("add" + nme,vpars,false,null);

    if (isseq)
    { event.setOrdered(true); } 

    Vector contexts = new Vector(); 
    if (ent != null) 
    { contexts.add(ent); } 

    for (int i = 0; i < invs.size(); i++)
    { Constraint con = (Constraint) invs.get(i); 
      Constraint ccnew = con.bmatches("add",nme,ent,nmex,event); 
      if (ccnew != null) 
      { boolean typed = ccnew.typeCheck(types,entities,contexts); 
        if (typed)
        { BStatement stat = ccnew.bupdateOperation(ent,true);
          body.addStatement(stat);
        } 
      }
      else if (con.getEvent() == null && con.involvesFeature(nme))
      { BExpression inv = con.binvariantForm(env,false); 
        inv.setBrackets(true); 
      
        BExpression inv1 = inv.substituteEq(nme + "(" + cx + ")", rhs);  
        pre = new BBinaryExpression("&",pre,inv1); 
      } // must ensure that not both an invariant and its contrapositive are used!

    }        
    BStatement newbody = BStatement.separateUpdates(body.getStatements()); 
    return new BOp(opname,null,pars,pre,newbody);
  }

  public static BOp buildAddAllOperation(String nme,
                      String cx, BExpression cs,
                      BExpression btbe)
  // addAllnme(cx,nmex) =
  //   PRE cx <: cs & nmex: T
  //   THEN
  //     nme := nme <+ { x:cx | x |-> nme(x) \/ {nmex}}
  //   END
  { Vector pars = new Vector();
    String opname = "addAll" + nme;
    String nmex = nme + "x";
    pars.add(cx);
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBasicExpression nmebe = new BBasicExpression(nme);
    BBinaryExpression pre1 =
      new BBinaryExpression("<:",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression(":",nmexbe,btbe);
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    Vector nvals = new Vector();
    nvals.add(nmexbe);
    BExpression nmeset = new BSetExpression(nvals);

    String xxvar = "xx";
    BExpression xxbe = new BBasicExpression(xxvar);
    BExpression xapp =
      new BApplyExpression(nme,xxbe);
    BExpression pred =
      new BBinaryExpression("|->",xxbe,
        new BBinaryExpression("\\/",xapp,nmeset));
    BExpression update =
      new BSetComprehension(xxvar,cxbe,pred);
    BExpression rhs =
      new BBinaryExpression("<+",nmebe,update);
    BStatement code =
      new BAssignStatement(nmebe,rhs);
    return new BOp(opname,null,pars,pre,code);
  }


  public static BOp buildUnionAllOperation(String nme,
                      String cx, BExpression cs,
                      BExpression btbe)
  // unionAllnme(cx,nmex) =
  //   PRE cx <: cs & nmex <: T
  //   THEN
  //     nme := nme <+ { x:cx | x |-> nme(x) \/ nmex}
  //   END
  { Vector pars = new Vector();
    String opname = "unionAll" + nme;
    String nmex = nme + "x";
    pars.add(cx);
    pars.add(nmex);
    BBasicExpression cxbe = new BBasicExpression(cx);
    BBasicExpression nmexbe =
                       new BBasicExpression(nmex);
    BBasicExpression nmebe = new BBasicExpression(nme);
    BBinaryExpression pre1 =
      new BBinaryExpression("<:",cxbe,cs);
    BBinaryExpression pre2 =
      new BBinaryExpression("<:",nmexbe,btbe);
    BExpression pre =
      new BBinaryExpression("&",pre1,pre2);
    String xxvar = "xx";
    BExpression xxbe = new BBasicExpression(xxvar);
    BExpression xapp =
      new BApplyExpression(nme,xxbe);
    BExpression pred =
      new BBinaryExpression("|->",xxbe,
        new BBinaryExpression("\\/",xapp,nmexbe));
    BExpression update =
      new BSetComprehension(xxvar,cxbe,pred);
    BExpression rhs =
      new BBinaryExpression("<+",nmebe,update);
    BStatement code =
      new BAssignStatement(nmebe,rhs);
    return new BOp(opname,null,pars,pre,code);
  }

  public String toString()
  { String res = "  ";
    if (result != null)
    { res = res + result + " <-- "; }
    res = res + getName();
    int numbParams = params.size();
    if (numbParams > 0)
    { res = res + "(";
      for (int i = 0; i < numbParams; i++)
      { res = res + params.get(i);
        if (i < numbParams - 1)
        { res = res + ","; }
      }
      res = res + ")"; 
    }
    
    res = res + " = \n";
    if (pre == null) 
    { res = res + "    " + code; } 
    else 
    { res = res + "    PRE " + pre + "\n";
      res = res + "    THEN " + code + "\n";
      res = res + "    END";
    }
    return res;
  }    

  private static BExpression injConstraint(String role,
                                           String cname, String val)
  { // val /: role[cs - {cx}]
    String cs = cname.toLowerCase() + "s";
    String cx = cname.toLowerCase() + "x";
    BExpression cxbe = new BBasicExpression(cx);
    BExpression csbe = new BBasicExpression(cs);
    BExpression valbe = new BBasicExpression(val);
    Vector s = new Vector();
    s.add(cxbe);
    BExpression newset = new BSetExpression(s);
    BExpression mins =
      new BBinaryExpression("-",csbe,newset);
    BExpression app =
      new BApplySetExpression(role,mins);
    return new BBinaryExpression("/:",valbe,app);
  }

  private static BExpression zeroOneConstraint(String role,
                                               String cname, String op,
                        String val)
  { // card(role') <= 1 where role' is new value of role
    String cs = cname.toLowerCase() + "s";
    String cx = cname.toLowerCase() + "x";
    BExpression cxbe = new BBasicExpression(cx);
    BExpression valbe = new BBasicExpression(val);
    BExpression newset;
    if (op.equals("set"))
    { newset = valbe; }
    else
    { BExpression app = new BApplyExpression(role,cxbe);
      if (op.equals("add"))
      { Vector singset = new Vector();
        singset.add(valbe);
        BExpression sing = new BSetExpression(singset);
        newset =
          new BBinaryExpression("\\/",app,sing);
      }
      else // op is "union"
      { newset =
          new BBinaryExpression("\\/",app,valbe);
      }
    }
    return new BBinaryExpression("<=",
                 new BUnaryExpression("card",newset),
                 new BBasicExpression("1"));
  }

  private static BExpression zeroOneQualConstraint(String role,
                                                   String cname, String op,
                        String val)
  { // card(role') <= 1 where role' is new value of role
    String cs = cname.toLowerCase() + "s";
    String cx = cname.toLowerCase() + "x";
    BExpression cxbe = new BBasicExpression(cx);
    BExpression valbe = new BBasicExpression(val);
    BExpression indbe = new BBasicExpression("ind"); 

    BExpression newset;
    if (op.equals("set"))
    { newset = valbe; }
    else
    { BExpression app = new BApplyExpression(role,cxbe);
      app = new BApplyExpression(app,indbe); 

      if (op.equals("add"))
      { Vector singset = new Vector();
        singset.add(valbe);
        BExpression sing = new BSetExpression(singset);
        newset =
          new BBinaryExpression("\\/",app,sing);
      }
      else // op is "union"
      { newset =
          new BBinaryExpression("\\/",app,valbe);
      }
    }
    return new BBinaryExpression("<=",
                 new BUnaryExpression("card",newset),
                 new BBasicExpression("1"));
  }

}

