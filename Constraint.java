import java.util.Vector; 
import java.util.Map; 
import java.util.HashMap; 
import java.io.*; 
import javax.swing.*; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
// package: OCL

public class Constraint extends ConstraintOrGroup
{ private String att1;
  private String val1;
  private BasicExpression leftbe; 
  private BasicExpression rightbe; 
  private String operator; 
  private BinaryExpression cond0 = null; 
  private Expression cond = null; 
  private Expression succ = null; 
  private BehaviouralFeature event = null; // for action invariants
  private boolean actionInv = false; 

  // assume it is of form  (att1 operator val1) & cond => succ
  private Vector associations = new Vector(); // Association
  private Entity owner = null;                // Entity
  private boolean ownerisPre = false;
  private Vector variables = new Vector();    // Attribute
  private boolean local = false;              // local inv of owner
  private boolean behavioural = true;         // generates code 
  private Vector baseEntities = new Vector(); // Entity

  private java.util.Map variableRanges = new HashMap();  // String --> Expression 
    // the scopes of the variables in the antecedent
  private Vector secondaryVars = new Vector(); // of String

  private java.util.Map letDefinitions = new HashMap();  // String --> Expression 
    // the definitions of let variables in the antecedent
  private Vector letVars = new Vector();   // of String

  private UseCase usecase = null; 
  private int constraintKind = 0;  // 1, 2 or 3
  private boolean ordered = false; 
  private Expression orderedBy = null; // an expression of owner

  // Should not be used: 
  public Constraint(String a1, String v1, String a2,
                    String v2, Vector rels)
  { cond0 = new BinaryExpression("=", 
                  new BasicExpression(a1), new BasicExpression(v1)); 
    att1 = a1;
    val1 = v1;
    succ = new BinaryExpression("=",
                  new BasicExpression(a2), new BasicExpression(v2));
    associations = rels;
  }

  public Object clone()
  { Constraint res = new Constraint(cond0,cond,succ,
                                    (Vector) associations.clone()); 
    res.event = event; 
    res.owner = owner; 
    res.local = local; 
    res.actionInv = actionInv; 
    res.behavioural = behavioural; 
    res.ordered = ordered; 
    if (orderedBy != null) 
    { res.orderedBy = (Expression) orderedBy.clone(); }  
    if (baseEntities != null) 
    { res.baseEntities = (Vector) baseEntities.clone(); } 
    res.constraintKind = constraintKind; 
    res.ownerisPre = ownerisPre; 
    // res.usecase = usecase; 
    res.id = id; 
    // res.type = type; 
    // res.elementType = elementType; 
    return res; 
  }

  public Constraint() { } 

  public Constraint(BehaviouralFeature e, Expression cnd, 
                    Expression sc, Vector rels, Vector needed)
  { cond0 = null;   // never want to match against one of these 
    event = e; 
    cond = cnd; 
    succ = sc; 
    associations = rels;
    baseEntities = needed; 
  }

  public Constraint(Expression c0, Expression cnd,
                    Expression sc, Vector rels)
  { 
    if (c0 != null && (c0 instanceof BinaryExpression))
    { BinaryExpression beante = (BinaryExpression) c0; 
      cond0 = beante; 
      if (beante.left instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left;  
        att1 = leftbe.toString();
      }
      if (beante.right instanceof BasicExpression)
      { rightbe = (BasicExpression) beante.right;  
        val1 = rightbe.toString();
      }
      operator = cond0.operator;
    } 
    cond = cnd;
    succ = sc;
    associations = rels;
  }

  public Constraint(BehaviouralFeature e, Expression c0, Expression cnd,
                    Expression sc, Vector rels)
  { this(c0,cnd,sc,rels); 
    event = e; 
  } 


  public static Constraint getConstraint(Expression inv)
  { if (inv == null) { return null; } 

    if (inv instanceof BinaryExpression) 
    { BinaryExpression exp = (BinaryExpression) inv; 
      if ("=>".equals(exp.operator))
      { return new Constraint(exp.left, exp.right); } 
      else 
      { return new Constraint(new BasicExpression(true), inv); } 
    } 
    else 
    { return new Constraint(new BasicExpression(true), inv); } 
  } 

  public Constraint(Expression scond, Expression succdt)
  { Expression ante = scond; 
    succ = succdt; 
    behavioural = false;   // but it might be behavioural? 
    ordered = false; 
    orderedBy = null; 

    if (ante != null && (ante instanceof BinaryExpression)) 
    { BinaryExpression beante = (BinaryExpression) ante.clone(); 
      String op = beante.operator; 
      if ((op.equals("=") || op.equals(":") || op.equals("/=") || 
           op.equals("/:") || op.equals("<:") || 
           Expression.comparitors.contains(op)) &&
          beante.left instanceof BasicExpression &&
          beante.right instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left; 
        att1 = leftbe.toString(); 
        rightbe = (BasicExpression) beante.right; 
        val1 = rightbe.toString(); 
        operator = op; 
        cond0 = beante; 
        cond = null; 
      } 
      else if (op.equals("&") && beante.left instanceof BinaryExpression)
      { cond = beante.right; 
        beante = (BinaryExpression) beante.left; 
        if (beante.left instanceof BasicExpression && 
            beante.right instanceof BasicExpression) 
        { leftbe = (BasicExpression) beante.left;
          att1 = leftbe.toString();
          rightbe = (BasicExpression) beante.right;
          val1 = rightbe.toString();
          operator = beante.operator; 
          cond0 = beante; 
        } 
        else 
        { cond0 = null; 
          cond = ante; 
        } 
      } 
      else // left or right not basic
      { cond0 = null; 
        cond = ante; 
      } 
    } // else, left null
    else 
    { cond0 = null; 
      cond = ante;
    } 
  } 



  public Constraint(SafetyInvariant si, Vector rels)
  { // assume si is a = b & C => c = d
    Expression ante = si.antecedent(); 
    succ = si.succedent(); 
    associations = rels;
    behavioural = si.isBehavioural(); 
    ordered = si.isOrdered(); 
    orderedBy = si.getOrderedBy(); 

    if (ante instanceof BinaryExpression) 
    { BinaryExpression beante = (BinaryExpression) ante.clone(); 
      String op = beante.operator; 
      if ((op.equals("=") || op.equals(":") || op.equals("/=") || 
           op.equals("/:") || op.equals("<:") || 
           Expression.comparitors.contains(op)) &&
          beante.left instanceof BasicExpression &&
          beante.right instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left; 
        att1 = leftbe.toString(); 
        rightbe = (BasicExpression) beante.right; 
        val1 = rightbe.toString(); 
        operator = op; 
        cond0 = beante; 
        cond = null; 
      } 
      else if (op.equals("&") && beante.left instanceof BinaryExpression)
      { cond = beante.right; 
        beante = (BinaryExpression) beante.left; 
        if (beante.left instanceof BasicExpression && 
            beante.right instanceof BasicExpression) 
        { leftbe = (BasicExpression) beante.left;
          att1 = leftbe.toString();
          rightbe = (BasicExpression) beante.right;
          val1 = rightbe.toString();
          operator = beante.operator; 
          cond0 = beante; 
        } 
        else 
        { cond0 = null; 
          cond = ante; 
        } 
      } 
      else // left or right not basic
      { cond0 = null; 
        cond = ante; 
      } 
    } // else, left null
    else 
    { cond0 = null; 
      cond = ante; 
    } 
  } 

  public String cg(CGSpec cgs)
  { Expression ante = antecedent(); 
    Expression succ = succedent(); 
    if (ante == null) 
    { return succ.cg(cgs); } 
    if ("true".equals(ante + ""))
    { return succ.cg(cgs); } 
    ConditionalExpression ce = new ConditionalExpression(ante,succ,new BasicExpression(true)); 
    return ce.cg(cgs); 
  } 

  public static Constraint createAppCons(Vector entities) 
  { // forms a constraint to express target atts are assigned values
    // values already type-checked

    Entity app = (Entity) ModelElement.lookupByName("Application", entities);
    BasicExpression apptype = new BasicExpression(app); 
    String appvar = app.getName().toLowerCase() + "x"; 
    BasicExpression appbe = new BasicExpression(app, appvar); 
    BinaryExpression appin = new BinaryExpression(":", appbe, apptype); 
    BasicExpression truebe = new BasicExpression(true); 
    BinaryExpression appexists = new BinaryExpression("#", appin, truebe); 
    return new Constraint(truebe, appexists); 
  } 
    


  public static Constraint fromExcel(Vector entities, Entity target, Vector atts, Vector values)
  { // forms a constraint to express target atts are assigned values
    // values already type-checked

    Entity excellib = (Entity) ModelElement.lookupByName("ExcelLib", entities);
    Entity app = (Entity) ModelElement.lookupByName("Application", entities);
 
    Expression antepred = new BasicExpression("true"); 
    String tvarname = target.getName().toLowerCase() + "x"; 
    BasicExpression tvar = new BasicExpression(target, tvarname); 
    Expression tdec = new BinaryExpression(":", tvar, new BasicExpression(target)); 
    Expression succpred = new BasicExpression("true"); 
    Vector sources = new Vector(); 
    Vector svars = new Vector(); 

    for (int i = 0; i < atts.size(); i++)
    { Attribute att = (Attribute) atts.get(i); 
      Expression exp = (Expression) values.get(i); 
      BasicExpression attexp = new BasicExpression(att); 
      attexp.setObjectRef(tvar); 

      if (exp.umlkind == Expression.VALUE) 
      { Expression eq = new BinaryExpression("=", attexp, exp); 
        succpred = Expression.simplifyAnd(succpred, eq); 
        target.addStereotype("source"); 
      } 
      else if (exp.umlkind == Expression.ATTRIBUTE)
      { Entity ow = exp.getEntity(); 
        if (ow == target) 
        { BasicExpression owexp = (BasicExpression) ((Expression) exp).clone(); 
          owexp.setObjectRef(tvar); 
          Expression eq = new BinaryExpression("=", attexp, owexp); 
          succpred = Expression.simplifyAnd(succpred, eq); 
        } 
        else // another entity 
        { String svarname = ow.getName().toLowerCase() + "x"; 
          BasicExpression svar = new BasicExpression(ow, svarname); 
          if (sources.contains(ow)) { } 
          else 
          { sources.add(ow); 
            svars.add(svarname); 
            BinaryExpression scop = new BinaryExpression(":", svar, new BasicExpression(ow)); 
            antepred = Expression.simplifyAnd(antepred, scop); 
          } 
          BasicExpression owexp = (BasicExpression) ((Expression) exp).clone(); 
          owexp.setObjectRef(svar); 
          Expression eq = new BinaryExpression("=", attexp, owexp); 
          succpred = Expression.simplifyAnd(succpred, eq); 
        } 
      } 
      else if (exp instanceof BasicExpression && ((BasicExpression) exp).isReduceFunctionCall())
      { BasicExpression attx = new BasicExpression(att); 
        attx.data = att.getName() + "x"; 
        Expression cexp = ((BasicExpression) exp).convertExcelExpression(); 
        Expression anteeq = new BinaryExpression("=", attx, cexp); 
        antepred = Expression.simplifyAnd(antepred, anteeq); 
        Expression eq = new BinaryExpression("=", attexp, attx); 
        succpred = Expression.simplifyAnd(succpred, eq); 
      } 
      else 
      { // Entity ow = exp.getEntity(); 
        // String svarname = ow.getName().toLowerCase() + "x"; 
        // BasicExpression svar = new BasicExpression(ow, svarname); 
        // if (sources.contains(ow)) { } 
        // else 
        // { sources.add(ow); 
        //   BinaryExpression scop = new BinaryExpression(":", svar, new BasicExpression(ow)); 
        //   antepred = Expression.simplifyAnd(antepred, scop); 
        // } 
        // Expression owexp = (Expression) ((Expression) exp).clone(); 
        // Expression rexp = owexp.replaceReference(svar, new Type(ow));   
        Vector qvs = new Vector(); 
        Vector antes = new Vector(); 
        Expression rexp = exp.excel2Ocl(target, entities, qvs, antes); 
        for (int j = 0; j < qvs.size(); j++) 
        { String qv = (String) qvs.get(j); 
          if (svars.contains(qv)) { } 
          else 
          { antepred = Expression.simplifyAnd(antepred, (Expression) antes.get(j));
            svars.add(qv); 
          } 
        } 
        Expression eq = new BinaryExpression("=", attexp, rexp); 
        succpred = Expression.simplifyAnd(succpred, eq); 
      } 
    } 
    Constraint res; 
    if (svars.contains(tvarname))
    { res = new Constraint(antepred, succpred); } 
    else 
    { res = new Constraint(antepred, new BinaryExpression("#", tdec, succpred)); } 
    res.setOwner(app);  
    return res; 
  } 

  public Sbvrse generateSbvrse()
  { Sbvrse s = new Sbvrse(); 
    String def = Identifier.nextIdentifier("Cond");
    s.setDefinedTerm(def); 
    Entity e = getOwner(); 
    if (e == null) { return s; } 
    String ename = e.getName(); 
    String ex = ename.toLowerCase();
    BasicExpression eexp = new BasicExpression(e,ex); 
 
    s.setSourceInstance(ex); 
    Expression succ = succedent(); 
    if (succ instanceof BinaryExpression)
    { BinaryExpression besucc = (BinaryExpression) succ; 
      if ("#".equals(besucc.operator) ||
          "#1".equals(besucc.operator))
      { BinaryExpression lbe = (BinaryExpression) besucc.left; 
        s.setTargetInstance(lbe.left + ""); 
        s.setTargetType(lbe.right + ""); 
        Expression p = (Expression) besucc.right.clone(); 
        p.addReference(eexp, new Type(e));    
        s.setSuccedent(p); 
      } 
    } 
    return s; 
  }     

  public boolean isTrivial()
  { if ("true".equals(succ + "")) { return true; } 
    if ("false".equals(antecedent() + "") ) { return true; } 
    if ("false".equals(cond0 + "") ) { return true; } 
    if ("false".equals(cond + "") ) { return true; } 
    return false; 
  } 

  public Expression definedness()
  { return new BinaryExpression("=>", antecedent(), succ.definedness()); } 

  public void addAntecedent(Expression e)
  { Expression newante = Expression.simplifyAnd(e,antecedent()); 
    if (newante instanceof BinaryExpression) 
    { BinaryExpression beante = (BinaryExpression) newante.clone(); 
      String op = beante.operator; 
      if ((op.equals("=") || op.equals(":") || op.equals("/=") || 
           op.equals("/:") || op.equals("<:") || 
           Expression.comparitors.contains(op)) &&
          beante.left instanceof BasicExpression &&
          beante.right instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left; 
        att1 = leftbe.toString(); 
        rightbe = (BasicExpression) beante.right; 
        val1 = rightbe.toString(); 
        operator = op; 
        cond0 = beante; 
        cond = null; 
      } 
      else if (op.equals("&") && beante.left instanceof BinaryExpression)
      { cond = beante.right; 
        beante = (BinaryExpression) beante.left; 
        if (beante.left instanceof BasicExpression && 
            beante.right instanceof BasicExpression) 
        { leftbe = (BasicExpression) beante.left;
          att1 = leftbe.toString();
          rightbe = (BasicExpression) beante.right;
          val1 = rightbe.toString();
          operator = beante.operator; 
          cond0 = beante; 
        } 
        else 
        { cond0 = null; 
          cond = newante; 
        } 
      } 
      else // left or right not basic
      { cond0 = null; 
        cond = newante; 
      } 
    } // else, left null
    else 
    { cond0 = null; 
      cond = newante; 
    } 
  } 

  public void setAntecedent(Expression newante)
  { if (newante instanceof BinaryExpression) 
    { BinaryExpression beante = (BinaryExpression) newante.clone(); 
      String op = beante.operator; 
      if ((op.equals("=") || op.equals(":") || op.equals("/=") || 
           op.equals("/:") || op.equals("<:") || 
           Expression.comparitors.contains(op)) &&
          beante.left instanceof BasicExpression &&
          beante.right instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left; 
        att1 = leftbe.toString(); 
        rightbe = (BasicExpression) beante.right; 
        val1 = rightbe.toString(); 
        operator = op; 
        cond0 = beante; 
        cond = null; 
      } 
      else if (op.equals("&") && beante.left instanceof BinaryExpression)
      { cond = beante.right; 
        beante = (BinaryExpression) beante.left; 
        if (beante.left instanceof BasicExpression && 
            beante.right instanceof BasicExpression) 
        { leftbe = (BasicExpression) beante.left;
          att1 = leftbe.toString();
          rightbe = (BasicExpression) beante.right;
          val1 = rightbe.toString();
          operator = beante.operator; 
          cond0 = beante; 
        } 
        else 
        { cond0 = null; 
          cond = newante; 
        } 
      } 
      else // left or right not basic
      { cond0 = null; 
        cond = newante; 
      } 
    } // else, left null
    else 
    { cond0 = null; 
      cond = newante; 
    } 
  } 

  public void setSuccedent(Expression e)
  { succ = e; } 

  public void addSuccedent(Expression e)
  { succ = Expression.simplifyAnd(succ,e); } 

  public void update(SafetyInvariant si, Vector rels)
  { // assume si is a = b & C => c = d
    Expression ante = si.antecedent(); 
    succ = si.succedent(); 
    associations = rels;
    behavioural = si.isBehavioural(); 
    ordered = si.isOrdered(); 
    orderedBy = si.getOrderedBy(); 

    if (ante instanceof BinaryExpression) 
    { BinaryExpression beante = (BinaryExpression) ante.clone(); 
      String op = beante.operator; 
      if ((op.equals("=") || op.equals(":") || op.equals("/=") || 
           op.equals("/:") || op.equals("<:") || 
           Expression.comparitors.contains(op)) &&
          beante.left instanceof BasicExpression &&
          beante.right instanceof BasicExpression)
      { leftbe = (BasicExpression) beante.left; 
        att1 = leftbe.toString(); 
        rightbe = (BasicExpression) beante.right; 
        val1 = rightbe.toString(); 
        operator = op; 
        cond0 = beante; 
        cond = null; 
      } 
      else if (op.equals("&") && beante.left instanceof BinaryExpression)
      { cond = beante.right; 
        beante = (BinaryExpression) beante.left; 
        if (beante.left instanceof BasicExpression && 
            beante.right instanceof BasicExpression) 
        { leftbe = (BasicExpression) beante.left;
          att1 = leftbe.toString();
          rightbe = (BasicExpression) beante.right;
          val1 = rightbe.toString();
          operator = beante.operator; 
          cond0 = beante; 
        } 
        else 
        { cond0 = null; 
          cond = ante; 
        } 
      } 
      else // left or right not basic
      { cond0 = null; 
        cond = ante; 
      } 
    } // else, left null
    else 
    { cond0 = null; 
      cond = ante;
    } 
  } 

  /* public void setId(int i)
  { id = i; } 

  public int getId()
  { return id; } */ 

  public void setOrdered(boolean ord)
  { ordered = ord; } 

  public void setOrderedBy(Expression ob)
  { orderedBy = ob; } 

  public void setUseCase(UseCase uc)
  { usecase = uc; } 

  public void setConstraintKind(int k)
  { constraintKind = k; } 

  public int getConstraintKind(Vector assocs)
  { return constraintKind; } 

  public int getConstraintKind()
  { return constraintKind; } 

  public void setAssociations(Vector rels)
  { associations = rels; } 

  public void setActionInv()
  { actionInv = true; } 

  public void setNeeded(Vector bes)
  { baseEntities = bes; } 


  public Expression getSecondaryQuantifier(int i)
  { if (i < secondaryVars.size())
    { String var = (String) secondaryVars.get(i); 
      Expression rng = (Expression) variableRanges.get(var);
      return rng; 
    } 
    return null; 
  } 

  public String getSecondaryVar(int i)
  { if (i < secondaryVars.size())
    { String var = (String) secondaryVars.get(i); 
      return var; 
    } 
    return null; 
  } 

  public void replaceSecondaryRange(int i, String var, Expression rng)
  { if (i < secondaryVars.size())
    { variableRanges.put(var,rng); } 
  } 

  public void setCond(Expression cnd)
  { cond = cnd; } 

  public void setSucc(Expression sc)
  { succ = sc; } 

  public boolean hasAntecedent()
  { Expression ante = antecedent(); 
    if (ante == null) 
    { return false; } 
    if ("true".equals(ante + ""))
    { return false; } 
    return true; 
  } 

  public Expression antecedent()
  { if (cond0 == null && cond == null)
    { return new BasicExpression(true); } 
    if (cond0 == null) 
    { return cond; } 
    if (cond == null) 
    { return cond0; } 
    return Expression.simplifyAnd(cond0,cond); 
  } 

  public Expression succedent()
  { if (succ == null) 
    { return new BasicExpression(true); }
    return succ; 
  } 

  public boolean isOrdered()
  { return ordered; } 

  public Constraint addReference(String ref, Type t) 
  { Constraint res = (Constraint) clone(); 
    BasicExpression refbe = new BasicExpression(ref); 
    refbe.setType(t); 
    if (cond0 != null)
    { res.cond0 = (BinaryExpression) cond0.addReference(refbe,t); } 
    if (cond != null) 
    { res.cond = cond.addReference(refbe,t); }
    if (succ != null) 
    { res.succ = succ.addReference(refbe,t); }
    return res; 
  } 
    

  public Vector readFrame()
  { Vector res = new Vector(); 
    Expression ante = antecedent(); 
    if (ante != null) 
    { res.addAll(ante.allReadFrame()); } 

    if (orderedBy != null) 
    { res.addAll(orderedBy.allReadFrame()); } 
    
    Expression sc = succedent(); 

    if (sc != null)
    { return VectorUtil.union(res,sc.readFrame()); } 
    return res; 
  } // and owner, or owner@pre

  public Vector internalReadFrame()
  { Vector res = new Vector(); 
    Expression ante = antecedent(); 
    if (ante != null) 
    { res.addAll(ante.allReadFrame()); } 

    if (orderedBy != null) 
    { res.addAll(orderedBy.allReadFrame()); } 
    
    Expression sc = succedent(); 

    if (sc != null)
    { return VectorUtil.union(res,sc.internalReadFrame()); } 
    return res; 
  } // and owner, or owner@pre

  public Vector anteReadFrame()
  { Vector res = new Vector(); 
    Expression ante = antecedent(); 
    if (ante != null) 
    { res.addAll(ante.allReadFrame()); } 
    return res; 
  } 

  public Vector wr(Vector assocs)
  { Vector res = new Vector(); 
    Expression succ = succedent(); 
    { res.addAll(succ.wr(assocs)); } 
    return res; 
  } 

  public Vector cwr(Vector assocs)
  { Vector res = new Vector(); 
    Expression succ = succedent(); 
    { res.addAll(succ.cwr(assocs)); } 
    return res; 
  } 

  public DataDependency rhsDataDependency()
  { return succ.rhsDataDependency(); } 

  public Vector allPreTerms()
  { Expression antced = antecedent(); 
    Expression succed = succedent(); 
    Vector res = new Vector(); 
    if (antced != null) 
    { res = antced.allPreTerms(); } 
    if (owner != null && ownerisPre) 
    { res.add(owner + "@pre"); } 
    return VectorUtil.union(res,succed.allPreTerms()); 
  } 

  public boolean confluenceCheck0()
  { // if there is feature with f@pre in readFrame, and f in write frame, 
    // then confluence may fail
    boolean res = true; 

    Vector wrs = wr(new Vector()); 
    Vector reads = readFrame(); 
    Vector wrAndrd = new Vector();  
    wrAndrd.addAll(wrs); 
    wrAndrd.retainAll(reads); 
    if (wrAndrd.size() > 0)
    { System.out.println("May fail confluence: reads and writes " + wrAndrd); }  
    
    // for (int i = 0; i < wrs.size(); i++) 
    // { String v = (String) wrs.get(i); 
    // int colonindex = v.indexOf(":"); 
    // if (colonindex > 0)
    // { String vf = v.substring(colonindex + 2, v.length()); 
      
    for (int j = 0; j < reads.size(); j++) 
    { String prer = reads.get(j) + ""; 
      int atind = prer.indexOf("@"); 
      if (atind > 0) 
      { String presr = prer.substring(0,atind); 
        if (wrs.contains(presr))
        { System.out.println("Possible confluence failure, writes " + presr + " and reads " + prer);
          res = false; 
        }
      }  
    }
    return res;  
  } 
 
  public boolean isPureDeletionConstraint(Vector assocs)
  { if (succ instanceof UnaryExpression)
    { if ("self->isDeleted()".equals(succ + ""))
      { Vector wrr = wr(assocs); 
        Vector rdd = readFrame();
        if (ownerisPre)
        { rdd.add(owner + "@pre"); } 
        else if (owner != null) 
        { rdd.add("" + owner); }
 
        System.out.println("Read/write frames: " + rdd + " " + wrr); 
        Vector inter = new Vector(); 
        inter.addAll(wrr); 
        inter.retainAll(rdd); 
        System.out.println("Intersection of wr and rd is: " + inter); 
        if (inter.size() <= 1) 
        { System.out.println("Implement " + this + " by a bounded iteration, not a fixpoint loop?"); 
          String optimise = 
            JOptionPane.showInputDialog("Implement " + this + " by bounded loop?: (y/n) ");
          if ("y".equals(optimise))
          { return true; } 
          return false;  
        } 
      } 
    } 
    return false; 
  } 


  public Constraint normalise() // removes => from succedent, assumes cond0 == null
  { if (succ instanceof BinaryExpression)
    { BinaryExpression be = (BinaryExpression) succ; 
      if ("=>".equals(be.operator))
      { Expression newcond = (new BinaryExpression("&",cond,be.left)).simplify(); 
        Constraint next = (Constraint) clone(); 
        next.setCond(newcond); 
        next.setSucc(be.right); 
        return next.normalise(); 
      } 
    } 
    return this; 
  } 

  public void addAssociation(Association ast)
  { associations.add(ast); } 

  public boolean isLocal() { return local; } 

  public boolean onAssociation(Association ast) 
  { return associations.contains(ast) && associations.size() == 1; } 

  public void setLocal(boolean loc) 
  { local = loc; }

  public void setBehavioural(boolean b)
  { behavioural = b; } 

  public Vector innermostEntities()
  { Vector res = succ.innermostEntities();
    if (cond0 != null)
    { res = VectorUtil.union(res,cond0.innermostEntities()); }
    if (cond != null)
    { res = VectorUtil.union(res,cond.innermostEntities()); }
    return res;
  }

  public ConstraintOrGroup substituteEq(String oldE, Expression newE)
  { Expression ncond0 = null; 
    Expression ncond = null; 
    Expression nsucc = null; 
    if (cond0 != null)
    { ncond0 = cond0.substituteEq(oldE,newE); }
    if (cond != null)
    { ncond = cond.substituteEq(oldE,newE); }
    nsucc = succ.substituteEq(oldE,newE); 
    Constraint cnew = new Constraint(event,ncond0,ncond,nsucc,associations);
    if (actionInv) { cnew.setActionInv(); } 
    cnew.constraintKind = constraintKind; 
    cnew.event = event; 
    cnew.owner = owner; 
    cnew.ownerisPre = ownerisPre; 
    cnew.local = local; 
    // res.actionInv = actionInv; 
    cnew.behavioural = behavioural; 
    return cnew; 
  }  // and copy owner, etc. 

  public Entity getOwner()
  { return owner; } 

  public boolean getispreOwner()
  { return ownerisPre; } 

  public void setOwner(Entity e) 
  { owner = e; } 

  public void setisPre(boolean e) 
  { ownerisPre = e; } 

  public void setPrestate(boolean e) 
  { ownerisPre = e; } 

  public void setOwner(Entity e, boolean isPre) 
  { owner = e; 
    ownerisPre = isPre; 
  } 

  public boolean checkIfLocal()
  { if (owner == null) 
    { return false; } 
    if (associations.size() > 0) { return false; } 

    Vector myfeatures = allFeaturesUsedIn(); 
    Vector ownerfeatures = owner.allDefinedFeatures(); 
    // System.out.println("My features are: " + myfeatures); 
    // System.out.println("Owner's defined features are: " + ownerfeatures); 
    if (ownerfeatures.containsAll(myfeatures))
    { local = true; 
      System.out.println(">>> Constraint " + this + " is a local invariant of " + owner); 
      return true; 
    } 
    return false; 
  } 

  public BehaviouralFeature getEvent() 
  { return event; } 

  public Vector getAssociations()
  { return associations; } 

  public String toString()
  { String res = ""; 
    boolean previous = false; 
    if (event != null) 
    { res = res + event; 
      previous = true; 
    } 
    if (cond0 != null) 
    { if (previous) { res = res + " & "; } 
      res = res + cond0; 
      previous = true; 
    } 
    if (cond != null)
    { if (previous) { res = res + " & "; }
      res = res + cond; 
      previous = true; 
    } 
    if (previous) 
    { res = res + " =>\n     "; } 

    String extra = ""; 

    if (owner != null) 
    { extra = extra + owner; } 

    if (ownerisPre) 
    { extra = extra + "@pre"; }
 
    String assoc = ""; 
    if (associations.size() > 0) 
    { assoc = " on " + associations; } 

    return extra + "::\n" + res + succ + assoc;  
  } 

  public String ucToString()
  { String res = ""; 
    boolean previous = false; 
    if (cond0 != null) 
    { res = res + cond0; 
      previous = true; 
    } 
    if (cond != null)
    { if (previous) { res = res + " & "; }
      res = res + cond; 
      previous = true; 
    } 
    if (previous) 
    { res = res + " => "; } 

    String extra = ""; 

    if (owner != null) 
    { extra = owner.getName(); } 

    if (ownerisPre) 
    { extra = extra + "@pre"; }
 
    return extra + "::\n\r" + res + succ + "\n\r\n\r";  
  } 

  public String toSQL()
  { String res = ""; 
    boolean previous = false; 
    if (cond0 != null) 
    { if ("true".equals(cond0 + "")) { } 
      else 
      { res = res + cond0.toSQL(); 
        previous = true;
      } 
    } 
    if (cond != null)
    { if ("true".equals(cond + "")) { } 
      else 
      { if (previous) { res = res + " AND "; }
        res = res + cond.toSQL(); 
        previous = true; 
      }
    } 
    if (previous) 
    { res = "NOT(" + res + ") OR "; } 

    return res + "(" + succ.toSQL() + ")";  
  } 


  public BExpression bqueryForm()
  { BExpression res = new BBasicExpression("true"); 
    BExpression lhs = null; 
    BExpression rhs = null; 
    boolean previous = false; 
    if (event != null) 
    { lhs = new BBasicExpression(event.toString()); 
      previous = true; 
    } 
    if (cond0 != null) 
    { if (previous) 
      { lhs = new BBinaryExpression("&",lhs,cond0.bqueryForm()); } 
      else 
      { lhs = cond0.bqueryForm(); }
      previous = true; 
    } 
    if (cond != null)
    { if (previous) 
      { lhs = new BBinaryExpression("&",lhs,cond.bqueryForm()); }
      else 
      { lhs = cond.bqueryForm(); } 
      previous = true; 
    } 
    if (previous) 
    { res = new BBinaryExpression("=>",lhs,succ.bqueryForm()); } 
    else 
    { res = succ.bqueryForm(); } 
    return res;  
  } 

  public String queryForm(java.util.Map env, boolean local)
  { String res = ""; 
    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryForm(env,local); 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "!(" + cond0eval + ")"; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryForm(env,local); 
      if (condeval.equals("true")) {} 
      else 
      { if (previous)
        { res = res + " || !(" + condeval + ")"; } 
        else 
        { res = "!(" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    String rhs = succ.queryForm(env,local); 
    if (previous) 
    { res = res + " || (" + rhs + ")"; } 
    else 
    { res = rhs; } 
    return res;  
  } 

  public String queryFormJava6(java.util.Map env, boolean local)
  { String res = ""; 
    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormJava6(env,local); 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "!(" + cond0eval + ")"; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryFormJava6(env,local); 
      if (condeval.equals("true")) {} 
      else 
      { if (previous)
        { res = res + " || !(" + condeval + ")"; } 
        else 
        { res = "!(" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    String rhs = succ.queryFormJava6(env,local); 
    if (previous) 
    { res = res + " || (" + rhs + ")"; } 
    else 
    { res = rhs; } 
    return res;  
  } 

  public String queryFormJava7(java.util.Map env, boolean local)
  { String res = ""; 
    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormJava7(env,local); 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "!(" + cond0eval + ")"; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryFormJava7(env,local); 
      if (condeval.equals("true")) {} 
      else 
      { if (previous)
        { res = res + " || !(" + condeval + ")"; } 
        else 
        { res = "!(" + condeval + ")"; 
          previous = true;
        } 
      }
    } 

    String rhs = succ.queryFormJava7(env,local); 
    if (previous) 
    { res = res + " || (" + rhs + ")"; } 
    else 
    { res = rhs; } 
    return res;  
  } 

  public String updateForm(java.util.Map env, boolean local)
  { String res = ""; 
    if (behavioural == false) 
    { return res; } 

    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryForm(env,local); 
      if (cond0eval == null) { return res; } 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "    if (" + cond0eval; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryForm(env,local); 
      if (condeval == null) { return ""; }
      if (condeval.equals("true")) 
      { if (previous) 
        { res = res + " )"; }
      } 
      else 
      { if (previous)
        { res = res + " && " + condeval + ")"; } 
        else 
        { res = "    if (" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    else 
    { if (previous) 
      { res = res + " )"; }
    } 
    String rhs = succ.updateForm(env,local); 
    if (rhs == null) { return ""; }
    if (previous) 
    { res = res + " { " + rhs + " }"; } 
    else 
    { res = "    " + rhs; } 
    return res;  
  } 


  public String updateFormJava6(java.util.Map env, boolean local)
  { String res = ""; 
    if (behavioural == false) 
    { return res; } 

    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormJava6(env,local); 
      if (cond0eval == null) { return res; } 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "    if (" + cond0eval; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryFormJava6(env,local); 
      if (condeval == null) { return ""; }
      if (condeval.equals("true")) 
      { if (previous) 
        { res = res + " )"; }
      } 
      else 
      { if (previous)
        { res = res + " && " + condeval + ")"; } 
        else 
        { res = "    if (" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    else 
    { if (previous) 
      { res = res + " )"; }
    } 
    String rhs = succ.updateFormJava6(env,local); 
    if (rhs == null) { return ""; }
    if (previous) 
    { res = res + " { " + rhs + " }"; } 
    else 
    { res = "    " + rhs; } 
    return res;  
  } 

  public String updateFormJava7(java.util.Map env, boolean local)
  { String res = ""; 
    if (behavioural == false) 
    { return res; } 

    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormJava7(env,local); 
      if (cond0eval == null) { return res; } 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "    if (" + cond0eval; 
        previous = true; 
      }
    } 
    if (cond != null)
    { String condeval = cond.queryFormJava7(env,local); 
      if (condeval == null) { return ""; }
      if (condeval.equals("true")) 
      { if (previous) 
        { res = res + " )"; }
      } 
      else 
      { if (previous)
        { res = res + " && " + condeval + ")"; } 
        else 
        { res = "    if (" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    else 
    { if (previous) 
      { res = res + " )"; }
    } 
    String rhs = succ.updateFormJava7(env,local); 
    if (rhs == null) { return ""; }
    if (previous) 
    { res = res + " { " + rhs + " }"; } 
    else 
    { res = "    " + rhs; } 
    return res;  
  } 

  public String updateFormCSharp(java.util.Map env, boolean local)
  { String res = ""; 
    if (behavioural == false) 
    { return res; } 

    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormCSharp(env,local); 
      if (cond0eval == null) { return res; } 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "    if (" + cond0eval; 
        previous = true; 
      }
    } 

    if (cond != null)
    { String condeval = cond.queryFormCSharp(env,local); 
      if (condeval == null) { return ""; }
      if (condeval.equals("true")) 
      { if (previous) 
        { res = res + " )"; }
      } 
      else 
      { if (previous)
        { res = res + " && " + condeval + ")"; } 
        else 
        { res = "    if (" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    else 
    { if (previous) 
      { res = res + " )"; }
    } 

    String rhs = succ.updateFormCSharp(env,local); 
    if (rhs == null) { return ""; }
    if (previous) 
    { res = res + " { " + rhs + " }"; } 
    else 
    { res = "    " + rhs; } 
    return res;  
  } 

  public String updateFormCPP(java.util.Map env, boolean local)
  { String res = ""; 
    if (behavioural == false) 
    { return res; } 

    boolean previous = false; 
    if (cond0 != null) 
    { String cond0eval = cond0.queryFormCPP(env,local); 
      if (cond0eval == null) { return res; } 
      if (cond0eval.equals("true")) {} 
      else 
      { res = "    if (" + cond0eval; 
        previous = true; 
      }
    } 

    if (cond != null)
    { String condeval = cond.queryFormCPP(env,local); 
      if (condeval == null) { return ""; }
      if (condeval.equals("true")) 
      { if (previous) 
        { res = res + " )"; }
      } 
      else 
      { if (previous)
        { res = res + " && " + condeval + ")"; } 
        else 
        { res = "    if (" + condeval + ")"; 
          previous = true;
        } 
      }
    } 
    else 
    { if (previous) 
      { res = res + " )"; }
    } 

    String rhs = succ.updateFormCPP(env,local); 
    if (rhs == null) { return ""; }
    if (previous) 
    { res = res + " { " + rhs + " }"; } 
    else 
    { res = "    " + rhs; } 
    return res;  
  } 

  public static Vector substituteEqAll(String oldE, Expression newE, Vector exps)
  { Vector res = new Vector(); 
    for (int i = 0; i < exps.size(); i++) 
    { Constraint con = (Constraint) exps.get(i); 
      res.add(con.substituteEq(oldE,newE)); 
    } 
    return res; 
  } 

  public boolean equals(Object x)
  { if (x instanceof Constraint) 
    { return toString().equals("" + x); } 
    return false; 
  }

  public static BExpression conjoinAll(Vector constraints)
  { BExpression res = new BBasicExpression("true");
    java.util.Map env = new java.util.HashMap();  
    if (constraints.size() == 0) { return new BBasicExpression("true"); } 
    for (int i = 0; i < constraints.size(); i++) 
    { Constraint cc = (Constraint) constraints.get(i);
      if (cc.getEvent() != null) { continue; } 
      BExpression inv = // cc.bqueryForm();
                        cc.binvariantForm(env,true);  
      inv.setBrackets(true); 
      if (i > 0) 
      { res = new BBinaryExpression("&",res,inv); } 
      else 
      { res = inv; } 
    } 
    return res; 
  } 
 
  public static BExpression conjoinAllSelected(String att, String ename,
                                               Vector constraints)
  { BExpression res = new BBasicExpression("true");
    java.util.Map env = new java.util.HashMap();
    env.put(ename,new BBasicExpression(ename.toLowerCase() + "x"));   
    int count = 0; 
    if (constraints.size() == 0) { return new BBasicExpression("true"); } 
    for (int i = 0; i < constraints.size(); i++) 
    { Constraint cc = (Constraint) constraints.get(i);
      if (cc.behavioural) 
      { System.out.println("Behavioural: " + cc); } 
      else 
      { Vector myfeatures = cc.allFeaturesUsedIn(); 
        if (myfeatures.contains(att))
        { BExpression inv = // cc.bqueryForm();
                        cc.binvariantForm(env,false);  
          inv.setBrackets(true); 
          if (count > 0) 
          { res = new BBinaryExpression("&",res,inv); } 
          else 
          { res = inv; } 
          count++; // surely?
        }
      }
    } 
    return res; 
  } 
 
  public boolean typeCheck(Vector types, Vector ents, Vector contexts)
  { boolean res = true;
    variables.clear(); 
    Vector env = new Vector();
    Vector vars = new Vector(); 
    if (event != null) 
    { env = event.getParameters(); } 

    // System.out.println("Env is: " + env); 

    if (cond0 != null)
    { res = cond0.typeCheck(types,ents,contexts,env);
      vars.addAll(cond0.getVariableUses());
    }

    if (cond != null)
    { res = cond.typeCheck(types,ents,contexts,env);
      vars.addAll(cond.getVariableUses());
    }

    if (orderedBy != null) 
    { orderedBy.typeCheck(types,ents,contexts,env); } 

    res = succ.typeCheck(types,ents,contexts,env);
    vars.addAll(succ.getVariableUses()); 
    variableTypeAssignment(vars,env); 
    System.out.println("Variables are: " + variables); 
    return res;
  } 
  // check that all features of constraint occur in its associations
  // or connected classes. If not, add the necessary associations. 

  public boolean typeCheck(Vector types, Vector ents, Vector contexts, Vector env)
  { // env is list of parameters in scope - such as operation/use case parameters
    // or previously defined variables. 

    boolean res = true;
    variables.clear(); 
    Vector vars = new Vector(); 
    
    // System.out.println("ENVIRONMENT of " + this + " is: " + env); 

    if (cond0 != null)
    { res = cond0.typeCheck(types,ents,contexts,env);
      vars.addAll(cond0.getVariableUses());
    }

    if (cond != null)
    { res = cond.typeCheck(types,ents,contexts,env);
      vars.addAll(cond.getVariableUses());
    }

    if (orderedBy != null) 
    { orderedBy.typeCheck(types,ents,contexts,env); } 

    res = succ.typeCheck(types,ents,contexts,env);
    vars.addAll(succ.getVariableUses()); 
    variableTypeAssignment(vars,env); 
    // System.out.println("VARIABLES are: " + variables); 
    return res;
  } 

  public Constraint deltaForm0() // form for modified not new objects
  { // E:: qvars & lvars & Ante => B->exists( b | b.idB = idE & BCond & Post ) becomes
    // B:: BCond(self) & not(idB : E@pre.idE@pre) => self->isDeleted()

    if (owner == null) 
    { // System.err.println("Cannot create delta form of type 0 constraint: " + this); 
      return type0delta(); 
    } 

    Vector pars = new Vector(); 
    if (usecase != null) 
    { pars.addAll(usecase.getParameters()); } 
    Vector pars1 = ModelElement.getNames(pars); 

    Type otype = new Type(owner); 

    BasicExpression ownerexp = new BasicExpression(owner); // E
    ownerexp.setPrestate(true); 

    BasicExpression betrue = new BasicExpression(true); 

    Expression succ = succedent(); 

    if (succ instanceof BinaryExpression && 
        ((BinaryExpression) succ).left instanceof BinaryExpression) {}  
    else 
    { System.err.println("Cannot create delta0 form - succedent must be B->exists(b|P): " + this); 
      return null; 
    } 

    BinaryExpression besucc = (BinaryExpression) succ;  // B->exists( b | right )
    BinaryExpression left = (BinaryExpression) besucc.left; 
    Expression right = (Expression) besucc.right; 
    Expression ltype = left.right; 
    BasicExpression lvar = (BasicExpression) left.left; 
    Entity B = ltype.getEntity(); 
    if (B == null) 
    { System.err.println("Cannot create delta0 form - no entity->exists: " + this); 
      return null; 
    } 

    Vector tvars = new Vector(); 
    tvars.add("" + lvar);  
    Vector tcondpost = new Vector(); 
    tcondpost.add(new BasicExpression("true")); 
    tcondpost.add(new BasicExpression("true")); 
    Vector tcondPosts = right.splitToTCondPost(tvars,tcondpost); 
    Expression tcond = (Expression) tcondPosts.get(0); 
    Expression tpost = (Expression) tcondPosts.get(1); 

    Expression bcond = tcond.dereference(lvar); 

    BasicExpression Bexp = new BasicExpression(B); 
    BasicExpression bself = new BasicExpression(B, "self"); 
    Expression newante = null; 
    Expression newsucc = new UnaryExpression("->isDeleted",bself); 

    Attribute ppk = owner.getPrincipalPK(); 


    if (besucc.operator.equals("#1") || besucc.operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk == null) 
      { System.err.println("Cannot create delta0 form - no id for: " + B); 
        return null; 
      } 
      else if (pk != null) 
      { BasicExpression bid = new BasicExpression(pk); 
        bid.setPrestate(true); 
  
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        BinaryExpression testid; 

        if (keyval == null)
        { System.err.println("Cannot create delta0 form: no " + lvar + "." + pk + " = val");
          return null;
        }
        else if (ppk != null && ppk.getName().equals(keyval + ""))
        { BasicExpression keye = new BasicExpression(ppk);   // idE
          keye.setPrestate(true); 
          keye.setObjectRef(ownerexp);   // E@pre.idE@pre
          testid = new BinaryExpression(":", bid, keye); 
        }
        else
        { BinaryExpression keyeq = new BinaryExpression("=", bid, keyval); 
          BasicExpression ownerE = new BasicExpression(owner);
          String oname = owner.getName().toLowerCase() + "x";
          BasicExpression ovarbe = new BasicExpression(oname);
          ovarbe.setType(otype);
          ovarbe.setElementType(otype);
          ovarbe.umlkind = Expression.VARIABLE;
          BinaryExpression oran = new BinaryExpression(":", ovarbe, ownerE); 
          Expression ante = antecedent();
          // but not the qvars, lvars part
          // BasicExpression betrue = new BasicExpression(true); 
          Vector allvars = new Vector(); 
          Vector v0 = new Vector(); 
          v0.add(betrue); 
          v0.add(betrue.clone()); 
          Vector qvars1 = new Vector(); 
          Vector lvars1 = new Vector(); 
          Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
          // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
          Expression antevars = (Expression) splitante.get(0); 
          Expression anterem = (Expression) splitante.get(1); 
          System.out.println("Ante1: " + antevars + " Ante2: " + anterem); 

          Expression existsowner = Expression.simplifyAnd(keyeq,anterem);
          Expression ante1 = existsowner.addReference(ovarbe,otype);
          Expression ante2 = ante1; 
          Vector lvars = new Vector(); 
          Vector qvars = secondaryVariables(lvars,pars); 
          if (qvars.size() > 0)
          { ante2 = addQLquantifiers(ante1,qvars,lvars,ovarbe,otype); } 
          testid = new BinaryExpression("#", oran, ante2);
        } 
        newante = Expression.simplifyAnd(bcond, new UnaryExpression("not",testid)); 
      } 

      Constraint res = new Constraint(newante, newsucc); 
      res.setOwner(B); 
      res.ownerisPre = true; 
      res.constraintKind = constraintKind; 
      return res; 
    } 
    else 
    { System.err.println("Invalid succedent for delta: " + this); 
      return null; 
    } 
  } 

  public Constraint deltaForm() // form for modified not new objects
  { // qvars & lvars & Ante => B->exists( b | b.id = idE & BCond & Post ) becomes
    // qvars & lvars & idE : B@pre.id & not(Ante) & b = B@pre[idE] & BCond(b) => b->isDeleted()

    if (owner == null) 
    { System.err.println("Cannot create delta form of type 0 constraint: " + this); 
      return null; 
    } 

    Vector pars = new Vector(); 
    if (usecase != null) 
    { pars.addAll(usecase.getParameters()); } 
    Vector pars1 = ModelElement.getNames(pars); 


    Expression ante = antecedent(); 
    if (ante == null || "true".equals(ante + ""))
    { return null; } 

    BasicExpression betrue = new BasicExpression(true); 
    Vector allvars = new Vector(); 
    Vector v0 = new Vector(); 
    v0.add(betrue); 
    v0.add(betrue.clone()); 
    Vector qvars1 = new Vector(); 
    Vector lvars1 = new Vector(); 
    Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
      // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
    Expression ante1 = (Expression) splitante.get(0); 
    Expression ante2 = (Expression) splitante.get(1); 
    System.out.println("Ante1: " + ante1 + " Ante2: " + ante2); 

    if (ante2 == null || "true".equals(ante2 + ""))
    { return null; } 
    Expression newante = new UnaryExpression("not",ante2); 

    Expression succ = succedent(); 
    if (succ instanceof BinaryExpression && 
        ((BinaryExpression) succ).left instanceof BinaryExpression) {}  
    else 
    { System.err.println("Cannot create delta form - succedent must be B->exists(b|P): " + this); 
      return null; 
    } 
    BinaryExpression besucc = (BinaryExpression) succ;  // B->exists( b | right )
    BinaryExpression left = (BinaryExpression) besucc.left; 
    Expression right = (Expression) besucc.right; 
    Expression ltype = left.right; 
    Expression lvar = left.left; 
    Expression newsucc = new UnaryExpression("->isDeleted",lvar); 
    Entity B = ltype.getEntity(); 
    if (B == null) 
    { System.err.println("Cannot create delta form - no entity->exists: " + this); 
      return null; 
    } 
    BasicExpression Bexp = new BasicExpression(B); 
    Bexp.setPrestate(true); 

    if (besucc.operator.equals("#1") || besucc.operator.equals("#")) 
    { Attribute pk = Expression.hasPrimaryKey(ltype); 
      if (pk == null) 
      { System.err.println("Cannot create delta form - no id for: " + B); 
        return null; 
      } 
      else // if (pk != null) 
      { BasicExpression bid = new BasicExpression(pk); 
        bid.setObjectRef(Bexp); 
        bid.setPrestate(true); 
        Vector remainder = new Vector(); 
        Expression keyval = right.featureSetting2("" + lvar, "" + pk, remainder);
        BasicExpression Bexp2 = new BasicExpression(B); 
        Bexp2.setPrestate(true); 
        // BasicExpression aid2 = new BasicExpression(ppk); 
        BinaryExpression testid;  
        if (keyval != null) // b.id = keyval
        { testid = new BinaryExpression(":", keyval, bid); 
          Bexp2.setArrayIndex(keyval);
        } 
        else 
        { Attribute ppk = owner.getPrincipalPK(); 
          if (ppk == null) 
          { System.err.println("Cannot create delta form - no id for: " + owner); 
            return null; 
          } 
          BasicExpression keye = new BasicExpression(ppk); 
          testid = new BinaryExpression(":", keye, bid);
          Bexp2.setArrayIndex(keye);
          System.err.println("WARNING: must be equation " + lvar + "." + pk + 
                             " = " + ppk + " in the ->exists"); 
        } // not right: need to include B->exists( b bcond & b.lvar = keyval )
        newante = new BinaryExpression("&",newante,testid); 
        newante = Expression.simplifyAnd(ante1,newante); 
        // BinaryExpression eqb = new BinaryExpression("=", lvar, Bexp2);   // b = B[idE] 
        // newante = new BinaryExpression("&",newante,eqb);    
        newsucc = new UnaryExpression("->isDeleted",Bexp2); 
      } 

      /* if ((ltype + "").equals(ent + "@pre") || ltype.umlkind != CLASSID || 
          (ent != null && ent.isAbstract()))
      { if (ltype.umlkind == CLASSID)
        { lqf = ((BasicExpression) ltype).classExtentQueryForm(env,local); } 
        // System.err.println("Must ensure " + lqf + " nonempty!"); 
        return "  if ((" + lqf + ").size() > 0)\n" + 
               "  { " + ent + " " + lvar + " = (" + ent + ") (" + lqf + ").get(0);\n" + 
               "    " + ufr + "\n" + 
               "  }\n"; 
      } */ 
      Constraint res = new Constraint(newante, newsucc); 
      res.setOwner(owner); 
      res.constraintKind = constraintKind; 
      return res; 
    } 
    else 
    { System.err.println("Invalid succedent for delta: " + this); 
      return null; 
    } 
  } 

 
  public ConstraintOrGroup invert() // swaps over quantifiers 
  { // Ante => B->exists( b | TCond & Post ) becomes
    // TCond[self/b] => owner->exists( x | x.Ante & x.Post~ ) 

    if (owner == null) 
    { System.out.println("Inverting type 0 constraint: " + this); 
      return type0invert(); 
    } 

    Vector pars = new Vector(); 
    if (usecase != null) 
    { pars.addAll(usecase.getParameters()); } 
    Vector pars1 = ModelElement.getNames(pars); 

    Expression ante = antecedent();
    Entity owner2 = null; 
    if (succ instanceof BinaryExpression)
    { BinaryExpression besucc = (BinaryExpression) succ; 
      if (besucc.operator.equals("#") ||
          besucc.operator.equals("#1"))
      { BinaryExpression sleft = (BinaryExpression) besucc.left; 
        BasicExpression var = (BasicExpression) sleft.left; 
        Expression entB = sleft.right;
        Type etype = entB.getElementType(); 
        if (etype != null && etype.entity != null)
        { owner2 = etype.entity; } 
        else 
        { System.err.println("Cannot invert constraint with null target entity: " + this); 
          return null; 
        } 


        Vector lvars = new Vector(); 
        Vector qvars = secondaryVariables(lvars,pars); 
        // Each qvar becomes an existential quantifier of reverse succedent
        // But omit quantifier if qvar = val in reverse
        // S->exists( sx | Q & sx = val )  is   sx = val & Q

      BasicExpression betrue = new BasicExpression(true); 
      Vector qvars1 = new Vector(); 
      Vector lvars1 = new Vector(); 
      Vector allvars = new Vector(); 
      Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
 
      Vector splitante; 
      if (ante != null) 
      { splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); } 
      else 
      { splitante = v0; }  
      // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 
      System.out.println("ante1: " + ante1 + " ante2: " + ante2); 
      
        String oname = owner + ""; 
        String ovar = oname.toLowerCase() + "x"; 
        Type otype = new Type(owner); 
        BasicExpression ovarbe = new BasicExpression(ovar); 
        ovarbe.setType(otype); 
        ovarbe.umlkind = Expression.VARIABLE;
        BasicExpression oexp = new BasicExpression(owner); 
        Type settype = new Type("Set",null); 
        settype.setElementType(otype); 
        oexp.setType(settype); 
        oexp.umlkind = Expression.CLASSID;
        oexp.elementType = otype; 
        oexp.multiplicity = ModelElement.MANY;  

        BinaryExpression newquant = new BinaryExpression(":",ovarbe,oexp); 

        Expression newtcond = ante2.addReference(ovarbe,otype);

        Vector vscopes = new Vector(); 
        Vector ttvars = new Vector(); 
        Expression basicpost = 
          Expression.removeExistentials(besucc.right,vscopes,ttvars); 
        // System.out.println(vscopes + "  " + ttvars + "  " + basicpost); 
        Expression escopes = Expression.conjoin(vscopes); 
       
        // Vector tvars = new Vector(); 
        ttvars.add("" + var);  
        Vector tcondpost = new Vector(); 
        tcondpost.add(new BasicExpression(true)); 
        tcondpost.add(new BasicExpression(true)); 
        Vector tcondPosts = basicpost.splitToTCondPost(ttvars,tcondpost); 
        // System.out.println("Split as: " + tcondPosts); 
        Expression tcond = (Expression) tcondPosts.get(0); 
        Expression tpost = (Expression) tcondPosts.get(1); 

        Expression tcondcl = (Expression) tcond.clone(); 
        Expression tcondclone = tcondcl.dereference(var); 
        Expression tcond2 = 
            Expression.simplifyAnd(escopes,tcondclone); 

        Expression succ2 = tpost.invert(); 
        Expression succ3 = Expression.simplifyAnd(succ2, ante1); 
        Expression newsucc = succ3.addReference(ovarbe,otype); 
        // Expression ante2 = newante.invert(); 

        Expression newposte = Expression.simplifyAnd(newsucc,newtcond); 
        Expression newpost = newposte.dereference(var); 
        // Expression ipost = newpost.invert(); 

        Vector parms = new Vector(); 

        if (qvars.size() > 0 || lvars.size() > 0) // auxiliary quantifiers
        { for (int i = 0; i < variables.size(); i++)
          { Attribute var1 = (Attribute) variables.get(i); 
            String vname = var1 + ""; 
            if (qvars.contains(vname) && !parms.contains(vname))
            { parms.add(vname); 
              Expression qrange = (Expression) variableRanges.get(vname);
              Expression qvar = new BasicExpression(vname); 
              Type ranelemt = qrange.getElementType(); 
              qvar.setType(ranelemt);
              if (ranelemt != null) 
              { qvar.setElementType(ranelemt.getElementType()); } 
 
              Expression qentity; 
              if (qrange.umlkind == Expression.CLASSID) 
              { qentity = new BasicExpression(qrange.entity); } 
              else 
              { qentity = new BasicExpression(qrange.getElementType() + ""); 
                // newpost = new BinaryExpression("&",
                //            new BinaryExpression(":",qvar,qrange),newpost);
              }  // put *inside* the quantifiers of newpost
               
              var1.setType(ranelemt);
              if (ranelemt != null) 
              { var1.setElementType(ranelemt.getElementType()); } 
 
              System.out.println("Quantified variable " + var1 + 
                                 " type = " + var1.getType());
              
              newpost = 
                new BinaryExpression("#",
                  new BinaryExpression(":",qvar,qentity),newpost); 
            } 
            else if (lvars.contains(vname) && !parms.contains(vname))
            { parms.add(vname); 
              Expression lrange = (Expression) letDefinitions.get(vname);
              Expression ldef = lrange.addReference(ovarbe,otype);
              newpost = newpost.substituteEq(vname, ldef); 
            } 
            else if (!parms.contains(vname)) 
            { System.err.println("Unquantified variable, not permitted: " + vname); } 
          }  
        } 

        Constraint res = new Constraint(null, tcond2, 
                new BinaryExpression("#",newquant,newpost),new Vector()); 
        res.setOwner(owner2); 
        return res; 
      } 
      else // if ("true".equals(ante + ""))
      { Expression newsucc = succ.invert(); 
        BasicExpression truex = new BasicExpression(true); 
        Constraint ress = new Constraint(truex, newsucc); 
        ress.setOwner(newsucc.entity); 
        return ress; 
      } 
    } 
    else // if ("true".equals(ante + ""))
    { Expression newsucc = succ.invert(); 
      BasicExpression truex = new BasicExpression(true); 
      Constraint ress = new Constraint(truex, newsucc); 
      ress.setOwner(newsucc.entity); 
      return ress; 
    } 
  } 
         
  public Expression addQLquantifiers(Expression newpost, Vector qvars, Vector lvars, BasicExpression ovarbe, Type otype)
  { 
    Vector parms = new Vector(); 

    if (qvars.size() > 0 || lvars.size() > 0) // auxiliary quantifiers
    { for (int i = 0; i < variables.size(); i++)
      { Attribute var1 = (Attribute) variables.get(i); 
        String vname = var1 + ""; 
        if (qvars.contains(vname) && !parms.contains(vname))
        { parms.add(vname); 
          Expression qrange = (Expression) variableRanges.get(vname);
          Expression qvar = new BasicExpression(vname); 
          Type ranelemt = qrange.getElementType(); 
          qvar.setType(ranelemt);
          if (ranelemt != null) 
          { qvar.setElementType(ranelemt.getElementType()); } 
 
          Expression qentity; 
          if (qrange.umlkind == Expression.CLASSID) 
          { qentity = new BasicExpression(qrange.entity); 
            newpost = 
                new BinaryExpression("#",
                  new BinaryExpression(":",qvar,qentity),newpost); 
          } 
          else 
          { qentity = new BasicExpression(qrange.getElementType() + ""); 
            Expression qdef = qrange.addReference(ovarbe,otype);
            newpost = 
                new BinaryExpression("#",
                  new BinaryExpression(":",qvar,qdef),newpost); 
          }  // put *inside* the quantifiers of newpost
               
          var1.setType(ranelemt);
          if (ranelemt != null) 
          { var1.setElementType(ranelemt.getElementType()); } 

          System.out.println("Quantified variable " + var1 + 
                                 " type = " + var1.getType());
              
        } 
        else if (lvars.contains(vname) && !parms.contains(vname))
        { parms.add(vname); 
          Expression lrange = (Expression) letDefinitions.get(vname);
          Expression ldef = lrange.addReference(ovarbe,otype);
          newpost = newpost.substituteEq(vname, ldef); 
        } 
        else if (!parms.contains(vname)) 
        { System.err.println("Unquantified variable, not permitted: " + vname); } 
      }  
    } 
    return newpost; 
  } 

  public Constraint type0delta()
  { Constraint inverse = (Constraint) type0invert(); 
    Expression ante = inverse.antecedent(); 
    Expression succ = inverse.succedent(); 
    Entity ent = inverse.getOwner(); 
    if (ent != null) 
    { BasicExpression evar = new BasicExpression(ent,"self");
      UnaryExpression newsucc = new UnaryExpression("->isDeleted", evar);
      Expression nsucc = new UnaryExpression("not", succ); 
      Expression newante = new BinaryExpression("&", ante, nsucc);
      Constraint res = new Constraint(newante, newsucc); 
      res.setOwner(ent); 
      return res; 
    } 
    else 
    { return inverse.generalType0deletion(); } 
  }   
 

  public ConstraintOrGroup type0invert() // swaps over quantifiers 
  { // A->forall( a | Ante => B->exists( b | TCond & Post ) ) becomes
    // B:: TCond[self/b] => A->exists( a | Post~ & Ante ) 

    Vector pars = new Vector(); 
    if (usecase != null) 
    { pars.addAll(usecase.getParameters()); } 
    Vector pars1 = ModelElement.getNames(pars); 
  
    Expression ante = antecedent();
    Entity owner2 = null; 
    if (succ instanceof BinaryExpression)
    { BinaryExpression besucc = (BinaryExpression) succ; 
      if (besucc.operator.equals("#") ||
          besucc.operator.equals("#1"))
      { BinaryExpression sleft = (BinaryExpression) besucc.left; 
        BasicExpression var = (BasicExpression) sleft.left; 
        Expression entB = sleft.right;
        Type etype = entB.getElementType(); 
        if (etype != null && etype.entity != null)
        { owner2 = etype.entity; } 
        else 
        { System.err.println("Cannot invert constraint with null target entity: " + this); 
          return null; 
        } 


        Vector lvars = new Vector(); 
        Vector qvars = secondaryVariables(lvars,pars); 
        // Each qvar becomes an existential quantifier of reverse succedent
        // But omit quantifier if qvar = val in reverse
        // S->exists( sx | Q & sx = val )  is   sx = val & Q

        BasicExpression betrue = new BasicExpression(true); 
        Vector qvars1 = new Vector(); 
        Vector lvars1 = new Vector(); 
        Vector allvars = new Vector(); 
        Vector v0 = new Vector(); 
        v0.add(betrue); 
        v0.add(betrue.clone()); 
      
        Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
        // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
        Expression ante1 = (Expression) splitante.get(0); 
        Expression ante2 = (Expression) splitante.get(1); 
        System.out.println("ante1: " + ante1 + " ante2: " + ante2); 
      
    /*     String oname = owner + ""; 
        String ovar = oname.toLowerCase() + "x"; 
        Type otype = new Type(owner); 
        BasicExpression ovarbe = new BasicExpression(ovar); 
        ovarbe.setType(otype); 
        ovarbe.umlkind = Expression.VARIABLE;
        BasicExpression oexp = new BasicExpression(oname); 
        oexp.setType(new Type("Set",null)); 
        oexp.umlkind = Expression.CLASSID;
        oexp.elementType = otype; 
        oexp.multiplicity = ModelElement.MANY;  
 
        BinaryExpression newquant = new BinaryExpression(":",ovarbe,oexp); */ 

        Expression newtcond = ante2; // .addReference(ovarbe,otype);

        Vector vscopes = new Vector(); 
        Vector ttvars = new Vector(); 
        Expression basicpost = 
          Expression.removeExistentials(besucc.right,vscopes,ttvars); 
        // System.out.println(vscopes + "  " + ttvars + "  " + basicpost); 
        Expression escopes = Expression.conjoin(vscopes); 
       
        // Vector tvars = new Vector(); 
        ttvars.add("" + var);  
        Vector tcondpost = new Vector(); 
        tcondpost.add(new BasicExpression("true")); 
        tcondpost.add(new BasicExpression("true")); 
        Vector tcondPosts = basicpost.splitToTCondPost(ttvars,tcondpost); 
        // System.out.println("Split as: " + tcondPosts); 
        Expression tcond = (Expression) tcondPosts.get(0);  // TCond(b)
        Expression tpost = (Expression) tcondPosts.get(1);  // Post(b) 

        Expression tcondcl = (Expression) tcond.clone(); 
        Expression tcondclone = tcondcl.dereference(var);   // TCond(self)
        Expression tcond2 = 
            Expression.simplifyAnd(escopes,tcondclone); 

        Expression succ2 = tpost.invert(); 
        Expression newsucc = succ2; // .addReference(ovarbe,otype); 
        
        Expression newposte = Expression.simplifyAnd(newsucc,newtcond); 
        Expression newpost = newposte.dereference(var);        // Post~(self) & Ante(a)

        Vector parms = new Vector(); 

        if (qvars.size() > 0 || lvars.size() > 0) // auxiliary quantifiers
        { for (int i = 0; i < variables.size(); i++)
          { Attribute var1 = (Attribute) variables.get(i); 
            String vname = var1 + ""; 
            if (qvars.contains(vname) && !parms.contains(vname))
            { parms.add(vname); 
              Expression qrange = (Expression) variableRanges.get(vname);
              Expression qvar = new BasicExpression(vname); 
              Type ranelemt = qrange.getElementType();
              qvar.setType(ranelemt); 
              if (ranelemt != null)
              { qvar.setElementType(ranelemt.getElementType()); }   
              Expression qentity; 
              if (qrange.umlkind == Expression.CLASSID) 
              { qentity = new BasicExpression(qrange.entity); } 
              else 
              { qentity = new BasicExpression(qrange.getElementType() + ""); 
                // newpost = new BinaryExpression("&",
                //            new BinaryExpression(":",qvar,qrange),newpost);
              }  // put *inside* the quantifiers of newpost
               
              var1.setType(ranelemt);
              if (ranelemt != null)
              { var1.setElementType(ranelemt.getElementType()); }   
 
              System.out.println("Quantified variable " + var1 + 
                                 " type = " + var1.getType());
              
              newpost = 
                new BinaryExpression("#",
                  new BinaryExpression(":",qvar,qentity),newpost); 
            } 
            else if (lvars.contains(vname) && !parms.contains(vname))
            { parms.add(vname); 
              Expression lrange = (Expression) letDefinitions.get(vname);
              Expression ldef = lrange; // .addReference(ovarbe,otype);
              newpost = newpost.substituteEq(vname, ldef); 
            } 
            else if (!parms.contains(vname)) 
            { System.err.println("Unquantified variable, not permitted: " + var); } 
          }  
        } 

        Constraint res = new Constraint(null, tcond2, newpost, associations); 
        res.setOwner(owner2); 
        return res; 
      } 
      else // if ("true".equals(ante + ""))
      { Expression newsucc = succ.invert(); 
        Constraint ress = new Constraint(new BasicExpression(true), newsucc); 
        ress.setOwner(newsucc.entity); 
        return ress; 
      } 
    } 
    else // if ("true".equals(ante + ""))
    { Expression newsucc = succ.invert(); 
      BasicExpression truex = new BasicExpression(true); 
      Constraint ress = new Constraint(truex, newsucc); 
      ress.setOwner(newsucc.entity); 
      return ress; 
    } 
    // System.out.println("Unable to invert constraint: " + this); 
    // return this; 
  } 

public Constraint generalType0deletion()
{ // forAll.A.. R(a) => exists.B..P becomes
  // forAll.A..not(exists.B..) => a->isDeleted()

  Vector aranges = new Vector();
  Vector aconds = new Vector();
  Vector avars = new Vector();
 
  Expression succ = succedent();
  while ((succ instanceof BinaryExpression) && "!".equals(((BinaryExpression) succ).operator))
  { BinaryExpression fquant = (BinaryExpression) succ;
    aranges.add(fquant.left);
    avars.add(((BinaryExpression) fquant.left).left);
    Expression rght = fquant.right;
    if (rght instanceof BinaryExpression)
    { BinaryExpression rbe = (BinaryExpression) rght;
      if ("=>".equals(rbe.operator))
      { aconds.add(rbe.left);
        succ = rbe.right;
      }
      else 
      { aconds.add(new BasicExpression(true));
        succ = rbe;
      }
    }
    else
    { aconds.add(new BasicExpression(true));
      succ = rght;
    }
  }

  Expression deleteAll = new BasicExpression(true);
  for (int ii = 0; ii < avars.size(); ii++)
  { Expression avar = (Expression) avars.get(ii);
    Expression dela = new UnaryExpression("->isDeleted", avar);
    deleteAll = Expression.simplifyAnd(deleteAll, dela);
  }
  Expression npred = new UnaryExpression("not", succ);
  Expression newsucc = new BinaryExpression("=>", npred, deleteAll);
  for (int ii = 0; ii < aranges.size(); ii++)
  { BinaryExpression arange = (BinaryExpression) aranges.get(ii);
    Expression acond = (Expression) aconds.get(ii); 
    if ("true".equals(acond + ""))
    { newsucc = new BinaryExpression("!", arange, newsucc); }
    else 
    { BinaryExpression aimp = new BinaryExpression("=>", acond, newsucc);
      newsucc = new BinaryExpression("!", arange, aimp); 
    }
  }
  Constraint res = new Constraint(new BasicExpression(true), newsucc);
  res.constraintKind = constraintKind;
  return res;
}

public Constraint generalType0inverse()
{ // forAll.A.. R(a) => exists.B..P becomes
  // forAll.B..exists.A..R(a) & P~

  Vector aranges = new Vector();
  Vector aconds = new Vector();
  Vector branges = new Vector();
  Vector bconds = new Vector();

  Expression succ = succedent();
  while ((succ instanceof BinaryExpression) && "!".equals(((BinaryExpression) succ).operator))
  { BinaryExpression fquant = (BinaryExpression) succ;
    aranges.add(fquant.left);
    Expression rght = fquant.right;
    if (rght instanceof BinaryExpression)
    { BinaryExpression rbe = (BinaryExpression) rght;
      if ("=>".equals(rbe.operator))
      { aconds.add(rbe.left);
        succ = rbe.right;
      }
      else 
      { aconds.add(new BasicExpression(true));
        succ = rbe;
      }
    }
    else
    { aconds.add(new BasicExpression(true));
      succ = rght;
    }
  }

  while ((succ instanceof BinaryExpression) && ("#".equals(((BinaryExpression) succ).operator) || "#1".equals(((BinaryExpression) succ).operator)))
  { BinaryExpression equant = (BinaryExpression) succ;
    branges.add(equant.left);
    Expression rght = equant.right;
    if (rght instanceof BinaryExpression)
    { BinaryExpression rbe = (BinaryExpression) rght;
      if ("&".equals(rbe.operator))
      { bconds.add(rbe.left);
        succ = rbe.right;
      }
      else 
      { bconds.add(new BasicExpression(true));
        succ = rbe;
      }
    }
    else
    { bconds.add(new BasicExpression(true));
      succ = rght;
    }
  }

  Expression newsucc = succ.invert();

  for (int ii = 0; ii < aranges.size(); ii++)
  { BinaryExpression arange = (BinaryExpression) aranges.get(ii);
    Expression acond = (Expression) aconds.get(ii); 
    if ("true".equals(acond + ""))
    { newsucc = new BinaryExpression("#", arange, newsucc); }
    else 
    { BinaryExpression aand = new BinaryExpression("&", acond, newsucc);
      newsucc = new BinaryExpression("!", arange, aand); 
    }
  }
  for (int ii = 0; ii < branges.size(); ii++)
  { BinaryExpression brange = (BinaryExpression) branges.get(ii);
    Expression bcond = (Expression) bconds.get(ii); 
    if ("true".equals(bcond + ""))
    { newsucc = new BinaryExpression("!", brange, newsucc); }
    else 
    { BinaryExpression bimp = new BinaryExpression("=>", bcond, newsucc);
      newsucc = new BinaryExpression("!", brange, bimp); 
    }
  }
  Constraint res = new Constraint(new BasicExpression(true), newsucc);
  res.constraintKind = constraintKind;
  return res;
}

  public Vector secondaryVariables(Vector lvars, Vector pars)  // quantified variables
  { Expression ante = antecedent(); 
    // reset the definitions: 
    secondaryVars = new Vector(); 
    letVars = new Vector(); 
    variableRanges = new java.util.HashMap(); 
    letDefinitions = new java.util.HashMap(); 
    Vector parnames = ModelElement.getNames(pars); 

    Vector res = new Vector(); 
    if (ante != null && (ante instanceof BinaryExpression))
    { BinaryExpression abe = (BinaryExpression) ante; 
      secondaryVars = abe.variableRanges(variableRanges,parnames); 
      // System.out.println(">>> Secondary variables are: " + secondaryVars); 
      res = secondaryVars; 
      // System.out.println(">>> Scopes are: " + variableRanges); 

      letVars = abe.letDefinitions(letDefinitions,parnames); 
      // System.out.println(">>> Let variables are: " + letVars); 
      // System.out.println(">>> Definitions are: " + letDefinitions); 
      lvars.addAll(letVars); 
    } 
    return res; 
  } 

  public void addLetVar(Attribute att, String var, Expression rng)
  { letVars.add(var); 
    letDefinitions.put(var,rng); 
    variables.add(att); 
  } 

  public Vector allFeaturesUsedIn()  // add arguments of event? 
  { Vector res = new Vector();
    if (cond0 != null)
    { res.addAll(cond0.allFeaturesUsedIn()); }
    if (cond != null)
    { res.addAll(cond.allFeaturesUsedIn()); }
    if (succ != null)
    { res.addAll(succ.allFeaturesUsedIn()); }
    return res;
  }

  public Vector operationsUsedIn()  // add arguments of event? 
  { Vector res = new Vector();
    if (cond0 != null)
    { res.addAll(cond0.allOperationsUsedIn()); }
    if (cond != null)
    { res.addAll(cond.allOperationsUsedIn()); }
    if (succ != null)
    { res.addAll(succ.allOperationsUsedIn()); }
    return res;
  }

  public boolean isRolenameOf(String f)
  { for (int i = 0; i < associations.size(); i++) 
    { Association ast = (Association) associations.get(i); 
      String rle = ast.getRole2(); 
      if (rle.equals(f))
      { return true; } 
    } 
    return false; 
  } 

  public boolean involvesFeature(String f)  // if f is a role of an assoc, or feature
  { if (isRolenameOf(f)) { return true; } 
    return allFeaturesUsedIn().contains(f); 
  } 

  public static Vector allFeaturesUsedIn(Vector invs)  // add arguments of event? 
  { Vector res = new Vector();
    for (int i = 0; i < invs.size(); i++) 
    { Constraint c = (Constraint) invs.get(i); 
      res.addAll(c.allFeaturesUsedIn()); 
    } 
    return res;
  }


  public Vector allLhsFeatures()  // add arguments of event? 
  { Vector res = new Vector();
    if (cond0 != null)
    { res.addAll(cond0.allFeaturesUsedIn()); }
    if (cond != null)
    { res.addAll(cond.allFeaturesUsedIn()); }
    return res;
  }

  public Vector allRhsFeatures()
  { Vector res = new Vector();
    if (succ != null)
    { res.addAll(succ.allFeaturesUsedIn()); }
    return res;
  }

  public Vector allLhsValues()  // add arguments of event? 
  { Vector res = new Vector();
    if (cond0 != null)
    { res.addAll(cond0.allValuesUsedIn()); }
    if (cond != null)
    { res.addAll(cond.allValuesUsedIn()); }
    return res;
  }

  public Vector allRhsValues()
  { Vector res = new Vector();
    if (succ != null)
    { res.addAll(succ.allValuesUsedIn()); }
    return res;
  }

  public Vector allEntitiesUsedIn()
  { Vector res = new Vector(); 
    if (cond0 != null) 
    { res = VectorUtil.union(res,cond0.allEntitiesUsedIn()); } 
    if (cond != null) 
    { res = VectorUtil.union(res,cond.allEntitiesUsedIn()); } 
    res = VectorUtil.union(res,succ.allEntitiesUsedIn()); 
    return res; 
  }

/*  public boolean matches(String att, String val)  // For setEq
  { if (att1 == null || val1 == null)
    { return false; } 
    return att1.equals(att) && val1.equals(val); 
  }  */

  public String globalUpdateOp(Entity ent, boolean local)
  { String res = ""; 
    if (owner == null)
    { return res; } 

    java.util.Map env0 = new java.util.HashMap(); 
    String ename = owner.getName();

    Expression ante = antecedent(); 
    Expression succ = succedent(); 

    if (owner == ent)   // operation on feature of constraint owner
    { // Apply constraint to modified instance only
      String ex = ename.toLowerCase() + "x"; 
      env0.put(ename,ex); 
      if (ante != null && !((ante + "").equals("true")))
      { res = res + "    if (" + ante.queryForm(env0,local) + ")\n"; 
        res = res + "    { " + succ.updateForm(env0,local) + " }\n" + 
                    "\n";
      } 
      else 
      { res = res + "    " + succ.updateForm(env0,local) + "\n" + 
                    "\n";
      }  
      return res; 
    } 
      
 
    // iteration: for (ex : owner) do this.updateForm(env,local)
    String es = "Controller.inst()." + ename.toLowerCase() + "s";  
    String e_x = ename.toLowerCase() + "_x"; 
    env0.put(ename,e_x); 

    res = "  for (int _i = 0; _i < " + es + ".size(); _i++)\n" + 
          "  { " + ename + " " + e_x + " = (" + ename + ") " + es + ".get(_i);\n"; 
    if (ante == null || (ante + "").equals("true")) 
    { res = res + "    " + succ.updateForm(env0,local) + " \n" + 
          "  }\n"; 
    } 
    else 
    { res = res + "    if (" + ante.queryForm(env0,local) + ")\n";  
      res = res + "    { " + succ.updateForm(env0,local) + " }\n" + 
            "  }\n"; 
    } 
    return res; 
  } 

  public String globalUpdateOpJava6(Entity ent, boolean local)
  { String res = ""; 
    if (owner == null)
    { return res; } 

    java.util.Map env0 = new java.util.HashMap(); 
    String ename = owner.getName();

    Expression ante = antecedent(); 
    Expression succ = succedent(); 

    if (owner == ent)   // operation on feature of constraint owner
    { // Apply constraint to modified instance only
      String ex = ename.toLowerCase() + "x"; 
      env0.put(ename,ex); 
      if (ante != null && !((ante + "").equals("true")))
      { res = res + "    if (" + ante.queryFormJava6(env0,local) + ")\n"; 
        res = res + "    { " + succ.updateFormJava6(env0,local) + " }\n" + 
                    "\n";
      } 
      else 
      { res = res + "    " + succ.updateFormJava6(env0,local) + "\n" + 
                    "\n";
      }  
      return res; 
    } 
      
 
    // iteration: for (ex : owner) do this.updateForm(env,local)
    String es = "Controller.inst()." + ename.toLowerCase() + "s";  
    String e_x = ename.toLowerCase() + "_x"; 
    env0.put(ename,e_x); 

    res = "  for (int _i = 0; _i < " + es + ".size(); _i++)\n" + 
          "  { " + ename + " " + e_x + " = (" + ename + ") " + es + ".get(_i);\n"; 
    if (ante == null || (ante + "").equals("true")) 
    { res = res + "    " + succ.updateFormJava6(env0,local) + " \n" + 
          "  }\n"; 
    } 
    else 
    { res = res + "    if (" + ante.queryFormJava6(env0,local) + ")\n";  
      res = res + "    { " + succ.updateFormJava6(env0,local) + " }\n" + 
            "  }\n"; 
    } 
    return res; 
  } 

  public String globalUpdateOpJava7(Entity ent, boolean local)
  { String res = ""; 
    if (owner == null)
    { return res; } 

    java.util.Map env0 = new java.util.HashMap(); 
    String ename = owner.getName();

    Expression ante = antecedent(); 
    Expression succ = succedent(); 

    if (owner == ent)   // operation on feature of constraint owner
    { // Apply constraint to modified instance only
      String ex = ename.toLowerCase() + "x"; 
      env0.put(ename,ex); 
      if (ante != null && !((ante + "").equals("true")))
      { res = res + "    if (" + ante.queryFormJava7(env0,local) + ")\n"; 
        res = res + "    { " + succ.updateFormJava7(env0,local) + " }\n" + 
                    "\n";
      } 
      else 
      { res = res + "    " + succ.updateFormJava7(env0,local) + "\n" + 
                    "\n";
      }  
      return res; 
    } 
      
 
    // iteration: for (ex : owner) do this.updateForm(env,local)
    String es = "Controller.inst()." + ename.toLowerCase() + "s";  
    String e_x = ename.toLowerCase() + "_x"; 
    env0.put(ename,e_x); 

    res = "  for (int _i = 0; _i < " + es + ".size(); _i++)\n" + 
          "  { " + ename + " " + e_x + " = (" + ename + ") " + es + ".get(_i);\n"; 
    if (ante == null || (ante + "").equals("true")) 
    { res = res + "    " + succ.updateFormJava7(env0,local) + " \n" + 
          "  }\n"; 
    } 
    else 
    { res = res + "    if (" + ante.queryFormJava7(env0,local) + ")\n";  
      res = res + "    { " + succ.updateFormJava7(env0,local) + " }\n" + 
            "  }\n"; 
    } 
    return res; 
  } 
      
  public String updateOperation(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    BasicExpression tt = new BasicExpression("true"); 
    tt.setType(new Type("boolean",null)); 
    tt.kind = ModelElement.INTERNAL;
    tt.umlkind = Expression.VALUE;  

    Vector pars = new Vector(); 
    if (event != null) 
    { pars = event.getParameters(); } 

    System.out.println("UPDATE OPERATION: should iterate over " + baseEntities); 
    Vector needed = new Vector(); 
    needed.addAll(baseEntities); // really the base entities of the new constraint. 
    needed.remove(ent); 

    java.util.Map env = new java.util.HashMap(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      if (att.getType() != null && att.getType().isEntity) 
      { env.put(att.getType().entity + "", att.getName()); 
        System.out.println("Parameter " + att + " type " + att.getType()); 
        needed.remove(att.getType().entity); 
      }
    }

    String ename = ent.getName(); 
    if (local) 
    { env.put(ename,"this"); } 
    else 
    { String ex = ename.toLowerCase() + "x";
      env.put(ename,ex); 
    } 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    int index = 2; 

    System.out.println("Variables are: " + variables);     

    String res = ""; 
    for (int i = 0; i < variables.size(); i++)
    { Attribute var = (Attribute) variables.get(i);
      String einame = var.getType() + "";  // assume it is a class type 
      String indexe = var.getName() + "_index";
      String eix = var.getName();   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      // env.put(einame,eix);
      System.out.println("Iterating over " + eix + " : " + einame); 
      Expression fullcond3 = 
        Expression.removeTypePredicate(fullcond,eix,einame); 
      System.out.println(fullcond3); // type check it
      fullcond = fullcond3.simplify(); 
      needed.remove(var.getType().getEntity());  
      index++; 
    } 
    for (int i = 0; i < needed.size(); i++)
    { Entity e = (Entity) needed.get(i);
      String einame = e.getName(); 
      String indexe = einame.toLowerCase() + "_index";
      String eix = einame.toLowerCase() + "x";   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      env.put(einame,eix); 
      index++; 
    } 
    // System.out.println(fullcond); // type check it
    // Expression fullcond2 = fullcond.simplify(); 

    res = res + 
          Association.genEventCode(rels,env,fullcond,index,succ,local); 
    for (int i = 0; i < needed.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    for (int i = 0; i < variables.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    return res; 
  }

  public String updateOperationJava6(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    BasicExpression tt = new BasicExpression("true"); 
    tt.setType(new Type("boolean",null)); 
    tt.kind = ModelElement.INTERNAL;
    tt.umlkind = Expression.VALUE;  

    Vector pars = new Vector(); 
    if (event != null) 
    { pars = event.getParameters(); } 

    // System.out.println("UPDATE OPERATION: should iterate over " + baseEntities); 
    Vector needed = new Vector(); 
    needed.addAll(baseEntities); // really the base entities of the new constraint. 
    needed.remove(ent); 

    java.util.Map env = new java.util.HashMap(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      if (att.getType() != null && att.getType().isEntity) 
      { env.put(att.getType().entity + "", att.getName()); 
        // System.out.println("Parameter " + att + " type " + att.getType()); 
        needed.remove(att.getType().entity); 
      }
    }

    String ename = ent.getName(); 
    if (local) 
    { env.put(ename,"this"); } 
    else 
    { String ex = ename.toLowerCase() + "x";
      env.put(ename,ex); 
    } 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    int index = 2; 

    System.out.println("Variables are: " + variables);     

    String res = ""; 
    for (int i = 0; i < variables.size(); i++)
    { Attribute var = (Attribute) variables.get(i);
      String einame = var.getType() + "";  // assume it is a class type 
      String indexe = var.getName() + "_index";
      String eix = var.getName();   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      // env.put(einame,eix);
      System.out.println("Iterating over " + eix + " : " + einame); 
      Expression fullcond3 = 
        Expression.removeTypePredicate(fullcond,eix,einame); 
      System.out.println(fullcond3); // type check it
      fullcond = fullcond3.simplify(); 
      needed.remove(var.getType().getEntity());  
      index++; 
    } 
    for (int i = 0; i < needed.size(); i++)
    { Entity e = (Entity) needed.get(i);
      String einame = e.getName(); 
      String indexe = einame.toLowerCase() + "_index";
      String eix = einame.toLowerCase() + "x";   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      env.put(einame,eix); 
      index++; 
    } 
    // System.out.println(fullcond); // type check it
    // Expression fullcond2 = fullcond.simplify(); 

    res = res + 
          Association.genEventCodeJava6(rels,env,fullcond,index,succ,local); 
    for (int i = 0; i < needed.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    for (int i = 0; i < variables.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    return res; 
  }

  public String updateOperationJava7(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    BasicExpression tt = new BasicExpression("true"); 
    tt.setType(new Type("boolean",null)); 
    tt.kind = ModelElement.INTERNAL;
    tt.umlkind = Expression.VALUE;  

    Vector pars = new Vector(); 
    if (event != null) 
    { pars = event.getParameters(); } 

    // System.out.println("UPDATE OPERATION: should iterate over " + baseEntities); 
    Vector needed = new Vector(); 
    needed.addAll(baseEntities); // really the base entities of the new constraint. 
    needed.remove(ent); 

    java.util.Map env = new java.util.HashMap(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      if (att.getType() != null && att.getType().isEntity) 
      { env.put(att.getType().entity + "", att.getName()); 
        // System.out.println("Parameter " + att + " type " + att.getType()); 
        needed.remove(att.getType().entity); 
      }
    }

    String ename = ent.getName(); 
    if (local) 
    { env.put(ename,"this"); } 
    else 
    { String ex = ename.toLowerCase() + "x";
      env.put(ename,ex); 
    } 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    int index = 2; 

    System.out.println("Variables are: " + variables);     

    String res = ""; 
    for (int i = 0; i < variables.size(); i++)
    { Attribute var = (Attribute) variables.get(i);
      String einame = var.getType() + "";  // assume it is a class type 
      String indexe = var.getName() + "_index";
      String eix = var.getName();   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      // env.put(einame,eix);
      System.out.println("Iterating over " + eix + " : " + einame); 
      Expression fullcond3 = 
        Expression.removeTypePredicate(fullcond,eix,einame); 
      System.out.println(fullcond3); // type check it
      fullcond = fullcond3.simplify(); 
      needed.remove(var.getType().getEntity());  
      index++; 
    } 
    for (int i = 0; i < needed.size(); i++)
    { Entity e = (Entity) needed.get(i);
      String einame = e.getName(); 
      String indexe = einame.toLowerCase() + "_index";
      String eix = einame.toLowerCase() + "x";   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + ".get(" + 
                         indexe + ");\n"; 
      env.put(einame,eix); 
      index++; 
    } 
    // System.out.println(fullcond); // type check it
    // Expression fullcond2 = fullcond.simplify(); 

    res = res + 
          Association.genEventCodeJava7(rels,env,fullcond,index,succ,local); 
    for (int i = 0; i < needed.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    for (int i = 0; i < variables.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    return res; 
  }

  public String updateOperationCSharp(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    BasicExpression tt = new BasicExpression("true"); 
    tt.setType(new Type("boolean",null)); 
    tt.kind = ModelElement.INTERNAL;
    tt.umlkind = Expression.VALUE;  

    Vector pars = new Vector(); 
    if (event != null) 
    { pars = event.getParameters(); } 

    // System.out.println("UPDATE OPERATION: should iterate over " + baseEntities); 
    Vector needed = new Vector(); 
    needed.addAll(baseEntities); // really the base entities of the new constraint. 
    needed.remove(ent); 

    java.util.Map env = new java.util.HashMap(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      if (att.getType() != null && att.getType().isEntity) 
      { env.put(att.getType().entity + "", att.getName()); 
        // System.out.println("Parameter " + att + " type " + att.getType()); 
        needed.remove(att.getType().entity); 
      }
    }

    String ename = ent.getName(); 
    if (local) 
    { env.put(ename,"this"); } 
    else 
    { String ex = ename.toLowerCase() + "x";
      env.put(ename,ex); 
    } 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    int index = 2; 

    System.out.println("Variables are: " + variables);     

    String res = ""; 
    for (int i = 0; i < variables.size(); i++)
    { Attribute var = (Attribute) variables.get(i);
      String einame = var.getType() + "";  // assume it is a class type 
      String indexe = var.getName() + "_index";
      String eix = var.getName();   
      String eis = einame.toLowerCase() + "_s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".Count; " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + "[" + indexe + "];\n"; 
      // env.put(einame,eix);
      // System.out.println("Iterating over " + eix + " : " + einame); 
      Expression fullcond3 = 
        Expression.removeTypePredicate(fullcond,eix,einame); 
      System.out.println(fullcond3); // type check it
      fullcond = fullcond3.simplify(); 
      needed.remove(var.getType().getEntity());  
      index++; 
    } 

    for (int i = 0; i < needed.size(); i++)
    { Entity e = (Entity) needed.get(i);
      String einame = e.getName(); 
      String indexe = einame.toLowerCase() + "_index";
      String eix = einame.toLowerCase() + "x";   
      String eis = einame.toLowerCase() + "s";   
      res = res + ModelElement.tab(index) + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + ".Count; " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + " " + eix + " = (" + einame + ") " + 
                                eis + "[" + indexe + "];\n"; 
      env.put(einame,eix); 
      index++; 
    } 
    // System.out.println(fullcond); // type check it
    // Expression fullcond2 = fullcond.simplify(); 

    res = res + 
          Association.genEventCodeCSharp(rels,env,fullcond,index,succ,local); 
    for (int i = 0; i < needed.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    for (int i = 0; i < variables.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    return res; 
  }

  public String updateOperationCPP(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    BasicExpression tt = new BasicExpression("true"); 
    tt.setType(new Type("boolean",null)); 
    tt.kind = ModelElement.INTERNAL;
    tt.umlkind = Expression.VALUE;  

    Vector pars = new Vector(); 
    if (event != null) 
    { pars = event.getParameters(); } 

    // System.out.println("UPDATE OPERATION: should iterate over " + baseEntities); 
    Vector needed = new Vector(); 
    needed.addAll(baseEntities); // really the base entities of the new constraint. 
    needed.remove(ent); 

    java.util.Map env = new java.util.HashMap(); 
    for (int i = 0; i < pars.size(); i++) 
    { Attribute att = (Attribute) pars.get(i); 
      if (att.getType() != null && att.getType().isEntity) 
      { env.put(att.getType().entity + "", att.getName()); 
        // System.out.println("Parameter " + att + " type " + att.getType()); 
        needed.remove(att.getType().entity); 
      }
    }

    String ename = ent.getName(); 
    if (local) 
    { env.put(ename,"this"); } 
    else 
    { String ex = ename.toLowerCase() + "x";
      env.put(ename,ex); 
    } 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    int index = 2; 

    System.out.println("Variables are: " + variables);     

    String res = ""; 
    for (int i = 0; i < variables.size(); i++)
    { Attribute var = (Attribute) variables.get(i);
      String einame = var.getType() + "";  // assume it is a class type 
      String indexe = var.getName() + "_index";
      String eix = var.getName();   
      String eis = "Controller::inst->get" + einame.toLowerCase() + "_s()";
      String esx = "_" + einame.toLowerCase() + "s"; 
   
      res = res + ModelElement.tab(index) + 
                         "vector<" + einame + "*>* " + esx + " = " + eis + ";\n" + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + "->size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + "* " + eix + " = (*" + esx + ")[" + indexe + "];\n"; 
      // env.put(einame,eix);
      // System.out.println("Iterating over " + eix + " : " + einame); 
      Expression fullcond3 = 
        Expression.removeTypePredicate(fullcond,eix,einame); 
      System.out.println(fullcond3); // type check it
      fullcond = fullcond3.simplify(); 
      needed.remove(var.getType().getEntity());  
      index++; 
    } 

    for (int i = 0; i < needed.size(); i++)
    { Entity e = (Entity) needed.get(i);
      String einame = e.getName(); 
      String indexe = einame.toLowerCase() + "_index";
      String eix = einame.toLowerCase() + "x";   
      String eis = "Controller::inst->get" + einame.toLowerCase() + "_s()";
      String esx = "_" + einame.toLowerCase() + "s";  
   
      res = res + ModelElement.tab(index) + 
                         "vector<" + einame + "*>* " + esx + " = " + eis + ";\n" + 
                         "for (int " + indexe + " = 0; " + indexe + " < " + 
                         eis + "->size(); " + indexe + "++)\n" + 
            ModelElement.tab(index) + 
                         "{ " + einame + "* " + eix + " = (*" + esx + ")[" + indexe + "];\n"; 
      env.put(einame,eix); 
      index++; 
    } 
    // System.out.println(fullcond); // type check it
    // Expression fullcond2 = fullcond.simplify(); 

    res = res + 
          Association.genEventCodeCPP(rels,env,fullcond,index,succ,local); 
    for (int i = 0; i < needed.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    for (int i = 0; i < variables.size(); i++)
    { res = res + ModelElement.tab(index) + "}\n"; 
      index--; 
    } 
    return res; 
  }


  public String globalUpdateOpCSharp(Entity ent, boolean local)
  { String res = ""; 
    if (owner == null)
    { return res; } 

    java.util.Map env0 = new java.util.HashMap(); 
    String ename = owner.getName();

    Expression ante = antecedent(); 
    Expression succ = succedent(); 

    if (owner == ent)   // operation on feature of constraint owner
    { // Apply constraint to modified instance only
      String ex = ename.toLowerCase() + "x"; 
      env0.put(ename,ex); 
      if (ante != null && !((ante + "").equals("true")))
      { res = res + "    if (" + ante.queryFormCSharp(env0,local) + ")\n"; 
        res = res + "    { " + succ.updateFormCSharp(env0,local) + " }\n" + 
                    "\n";
      } 
      else 
      { res = res + "    " + succ.updateFormCSharp(env0,local) + "\n" + 
                    "\n";
      }  
      return res; 
    } 
      
 
    // iteration: for (ex : owner) do this.updateForm(env,local)
    String es = "Controller.inst()." + ename.toLowerCase() + "_s";  
    String e_x = ename.toLowerCase() + "_x"; 
    env0.put(ename,e_x); 

    res = "  for (int _i = 0; _i < " + es + ".Count; _i++)\n" + 
          "  { " + ename + " " + e_x + " = (" + ename + ") " + es + "[_i];\n"; 
    if (ante == null || (ante + "").equals("true")) 
    { res = res + "    " + succ.updateFormCSharp(env0,local) + " \n" + 
          "  }\n"; 
    } 
    else 
    { res = res + "    if (" + ante.queryFormCSharp(env0,local) + ")\n";  
      res = res + "    { " + succ.updateFormCSharp(env0,local) + " }\n" + 
            "  }\n"; 
    } 
    return res; 
  } 

  public String globalUpdateOpCPP(Entity ent, boolean local)
  { String res = ""; 
    if (owner == null)
    { return res; } 

    java.util.Map env0 = new java.util.HashMap(); 
    String ename = owner.getName();

    Expression ante = antecedent(); 
    Expression succ = succedent(); 

    if (owner == ent)   // operation on feature of constraint owner
    { // Apply constraint to modified instance only
      String ex = ename.toLowerCase() + "x"; 
      env0.put(ename,ex); 
      if (ante != null && !((ante + "").equals("true")))
      { res = res + "    if (" + ante.queryFormCPP(env0,local) + ")\n"; 
        res = res + "    { " + succ.updateFormCPP(env0,local) + " }\n" + 
                    "\n";
      } 
      else 
      { res = res + "    " + succ.updateFormCPP(env0,local) + "\n" + 
                    "\n";
      }  
      return res; 
    } 
      
 
    // iteration: for (ex : owner) do this.updateForm(env,local)
    String es = "Controller::inst->get" + ename.toLowerCase() + "_s()";  
    String e_x = ename.toLowerCase() + "_x"; 
    String esx = "_" + e_x + "s"; 
    env0.put(ename,e_x); 

    res = "  vector<" + ename + "*>* " + esx + " = " + es + ";\n" + 
          "  for (int _i = 0; _i < " + esx + "->size(); _i++)\n" + 
          "  { " + ename + "* " + e_x + " = (*" + esx + ")[_i];\n"; 
    if (ante == null || (ante + "").equals("true")) 
    { res = res + "    " + succ.updateFormCPP(env0,local) + " \n" + 
          "  }\n"; 
    } 
    else 
    { res = res + "    if (" + ante.queryFormCPP(env0,local) + ")\n";  
      res = res + "    { " + succ.updateFormCPP(env0,local) + " }\n" + 
            "  }\n"; 
    } 
    return res; 
  } 



  public BStatement synthesiseBCode(Entity e, String feature,
                                    boolean local)
  { if (actionInv)  // if action invariant?
    { return bupdateOperation(e,local); }
    
    UpdateFeatureInfo ufi = succ.updateFeature(feature);
    System.out.println("synthesiseBCode for: " + this + " " + ufi); 

    if (ufi == null)
    { return new BBasicStatement("skip"); } // no valid update
                                            // But case of succ.isEvent() ?    
    Expression ue = ufi.updateExp;
    succ = ufi.newexp; 
    System.out.println("Update expression is: " + ue);
    Vector ents0 = ue.innermostEntities();  // allEntitiesUsedIn();
    System.out.println("entities are: " + ents0);
    String feat = ufi.feature; 
    Vector ents = (Vector) ents0.clone();
    if (cond != null)
    { ents = VectorUtil.union(ents,cond.innermostEntities()); }
    if (cond0 != null)
    { ents = VectorUtil.union(ents,cond0.innermostEntities()); }
  
    if (ents.size() == 0 ||
        (ents.size() == 1 && ents.contains(e))) 
    { return bupdateOperation(e,local); }
    else if (ents0.size() == 0 ||
        (ents0.size() == 1 && ents0.contains(e))) 
    { return bUpdateOperation(e,local); }
    else 
    { Expression uref = ufi.updateRef; 
      return bGeneralUpdateOp(e,feat,ue,uref,local);
    }
  }  // ignores cond, cond0? 

  public BStatement staticSynthesiseBCode(Entity e, String feature,
                                    boolean local)
  { UpdateFeatureInfo ufi = succ.updateFeature(feature);
    if (ufi == null)
    { return new BBasicStatement("skip"); } // no valid update
    Expression ue = ufi.updateExp;
    succ = ufi.newexp; 
    System.out.println("Update expression is: " + ue);
    Vector ents0 = ue.allEntitiesUsedIn();
    System.out.println("entities are: " + ents0);
    String feat = ufi.feature; 
    Vector ents = (Vector) ents0.clone();
    if (cond != null)
    { ents.addAll(cond.allEntitiesUsedIn()); }
    if (cond0 != null)
    { ents.addAll(cond0.allEntitiesUsedIn()); }
  
    if (cond == null)
    { return staticbupdateOperation(e,local); }
    else if (ents0.size() == 0 ||
        (ents0.size() == 1 && ents0.contains(e))) 
    { return staticbUpdateOperation(e,local); }
    else 
    { Expression uref = ufi.updateRef; 
      return bGeneralUpdateOp(e,feat,ue,uref,local);
    }
  }  // ignores cond, cond0? 

  // ent is the initiating or owning entity
  public BStatement bupdateOperation(Entity ent,boolean local)
  // for setatt1(ex,val1)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    String ex = ent.getName().toLowerCase() + "x"; 
    BExpression exbe = new BBasicExpression(ex); 
    Vector elems = new Vector(); 
    elems.add(exbe); 
    env.put(ent.getName(),new BSetExpression(elems));
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 

    System.out.println("Gen B event code: " + fullcond + " => " + succ); 
    
    return Association.genBEventCode(rels,env,fullcond,succ,local); 
  }

  public BStatement staticbupdateOperation(Entity ent,boolean local)
  // for setatt1(val1)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    String es = ent.getName().toLowerCase() + "s"; 
    BExpression esbe = new BBasicExpression(es);
    esbe.setMultiplicity(ModelElement.MANY);  
     
    env.put(ent.getName(),esbe);
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    
    return Association.genBEventCode(rels,env,fullcond,succ,local); 
  }

  public BStatement bUpdateOperation(Entity e, boolean local)
  { String src = e.getName();
    Entity targ = Expression.getTargetEntity(succ);
    if (targ == null) { return null; }  // no update possible
    String target = "" + targ;  // getName()
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    BBasicExpression oobe = new BBasicExpression(src.toLowerCase() + "x");
    env.put(src,oobe);
    // BExpression scond =
    //    Association.genBUpdateSet(rels,target,env,cond);
    String tx = target.toLowerCase() + "x";
    String ts = target.toLowerCase() + "s";
    BExpression tsbe = new BBasicExpression(ts);
    tsbe.setMultiplicity(ModelElement.MANY); 
    env.put(target,new BBasicExpression(tx)); // surely? 
    BExpression scond =
        Association.genBUpdateSet(rels,target,env,cond);
    BExpression objs =
      new BSetComprehension(tx,tsbe,scond);
    java.util.Map env2 = new java.util.HashMap();
    Vector ooset = new Vector();
    ooset.add(oobe);
    BExpression oose =
        new BSetExpression(ooset);
    if (!(src.equals(target))) 
    { env2.put(src,oose); } 
    env2.put(target,objs);
    return succ.bupdateForm(env2,local);
  }  // src.equals(target) possible for local invariants, then the 
     // source and target features should be different. 

  public BStatement staticbUpdateOperation(Entity e, boolean local)
  { String src = e.getName();
    Entity targ = Expression.getTargetEntity(succ);
    if (targ == null) { return null; }  // no update possible
    String target = "" + targ;  // getName()
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    java.util.Map env2 = new java.util.HashMap();
    
    String oo = src.toLowerCase() + "x"; 
    BBasicExpression oobe = new BBasicExpression(oo);
    env.put(src,oobe);
    BExpression scond =
        Association.genBUpdateSet(rels,target,env,cond);
    String tx = target.toLowerCase() + "x";
    String ts = target.toLowerCase() + "s";
    BExpression tsbe = new BBasicExpression(ts);
    tsbe.setMultiplicity(ModelElement.MANY);
    if (!src.equals(target))
    { String srcs = src.toLowerCase() + "s"; 
      BBasicExpression srcsbe = new BBasicExpression(srcs); 
      srcsbe.setMultiplicity(ModelElement.MANY); 
      scond = new BBinaryExpression("&",
                new BBinaryExpression(":",oobe,srcsbe),scond); 
      scond = new BQuantifierExpression("exists",oo,scond); 
      env2.put(src,srcsbe); 
    } 
    BExpression objs =
      new BSetComprehension(tx,tsbe,scond);
    env2.put(target,objs);
    return succ.bupdateForm(env2,local);
  }  // src.equals(target) possible for local invariants, then the 
     // source and target features should be different. 

  public BStatement bGeneralUpdateOp(Entity e, String f, Expression ue,
                                     Expression obj, boolean local)
  { String src = e.getName();
    String tfeatx = f + "x"; // the updated feature of targ
    Entity targ = Expression.getTargetEntity(succ);
    if (targ == null) { return null; }  // no update possible
    String target = "" + targ;  // getName()
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    BBasicExpression oobe = new BBasicExpression(src.toLowerCase() + "x");
    BBasicExpression tfeatbe =
      new BBasicExpression(tfeatx); 
    int mult = targ.roleMultiplicity(f); 
    System.out.println("Target feature " + f + " of " + target + " mult " + mult); 
    tfeatbe.setMultiplicity(mult); 
    // if f is a MANY role, set multiplicity of tfeatbe to be MANY
    env.put(src,oobe);
    BExpression scond =
        Association.genBUpdateExp(rels,target,env,cond,ue,tfeatbe);  // op is "set"
    String tx = target.toLowerCase() + "x";
    String ts = target.toLowerCase() + "s";
    BExpression tsbe = new BBasicExpression(ts);
    tsbe.setMultiplicity(ModelElement.MANY); 

    BExpression txbe = new BBasicExpression(tx); 
    Vector vars = new Vector();
    vars.add(tx);
    vars.add(tfeatx);
    BExpression ran = new BBinaryExpression(":",txbe,tsbe); 

    BExpression objs =
        new BSetComprehension(vars,new BBinaryExpression("&",ran,scond));
    Vector params = new Vector();
    params.add(objs); 
    if (local) 
    { BExpression fbe = new BBasicExpression(f); 
      BExpression upd = new BBinaryExpression("<+",fbe,objs); 
      BAssignStatement res = new BAssignStatement(fbe,upd); 
      return res; 
    }
    else 
    { BOperationCall res = new BOperationCall("update" + f,params); 
      res.setWriteFrame(f); // Surely?
      return res; // updatef(objs)
    }
  }  // src.equals(target) possible for local invariants, then the 
     // source and target features should be different. 
     // Ignores cond0? 

  public BStatement staticbGeneralUpdateOp(Entity e, String f, Expression ue,
                                           Expression obj, boolean local)
  { String src = e.getName();
    String tfeatx = f + "x"; // the updated feature of targ
    Entity targ = Expression.getTargetEntity(succ);
    if (targ == null) { return null; }  // no update possible
    String target = "" + targ;  // getName()
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    String oo = src.toLowerCase() + "x"; 
    BBasicExpression oobe = new BBasicExpression(oo);
    BBasicExpression tfeatbe =
      new BBasicExpression(tfeatx); 
    int mult = targ.roleMultiplicity(f); 
    System.out.println("Target feature " + f + " of " + target + " mult " + mult); 
    tfeatbe.setMultiplicity(mult); 
    // if f is a MANY role, set multiplicity of tfeatbe to be MANY
    env.put(src,oobe);
    BExpression scond =
        Association.genBUpdateExp(rels,target,env,cond,ue,tfeatbe);  // op is "set"
    if (!src.equals(target))
    { String srcs = src.toLowerCase() + "s"; 
      BBasicExpression srcsbe = new BBasicExpression(srcs); 
      srcsbe.setMultiplicity(ModelElement.MANY); 
      scond = new BBinaryExpression("&",
                new BBinaryExpression(":",oobe,srcsbe),scond); 
      scond = new BQuantifierExpression("exists",oo,scond); 
    } 

    String tx = target.toLowerCase() + "x";
    String ts = target.toLowerCase() + "s";
    BExpression tsbe = new BBasicExpression(ts);
    tsbe.setMultiplicity(ModelElement.MANY); 

    BExpression txbe = new BBasicExpression(tx); 
    Vector vars = new Vector();
    vars.add(tx);
    vars.add(tfeatx);
    BExpression ran = new BBinaryExpression(":",txbe,tsbe); 

    BExpression objs =
        new BSetComprehension(vars,new BBinaryExpression("&",ran,scond));
    Vector params = new Vector();
    params.add(objs); 
    if (local) 
    { BExpression fbe = new BBasicExpression(f); 
      BExpression upd = new BBinaryExpression("<+",fbe,objs); 
      BAssignStatement res = new BAssignStatement(fbe,upd); 
      return res; 
    }
    else 
    { BOperationCall res = new BOperationCall("update" + f,params); 
      res.setWriteFrame(f); 
      return res; // updatef(objs)
    }
  }  // src.equals(target) possible for local invariants, then the 
     // source and target features should be different. 

  public Vector sqlOperation(Entity ent, String f, boolean local)
  // for att1val1(Entity1 oo)
  { Vector rels = (Vector) associations.clone(); 
    // UpdateFeatureInfo ufi = succ.updateFeature(f); 
    // Vector needed = new Vector(); 
    // needed.add(ent); 
    // if (cond != null)
    // { needed = VectorUtil.union(needed,cond.innermostEntities()); } 
    // plus those of update expression and reference if not null
    // target entity is needed if reference == null

    // System.out.println("SQL UPDATE OPERATION: Ignores cond0: " + cond0); 
    java.util.Map env = new java.util.HashMap(); 
    String ename = ent.getName(); 
    env.put(ename,ename);
    String eId = ename + "." + ename.toLowerCase() + "Id"; 
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    // int index = 2; 
    Vector res = new Vector(); 
    if (fullcond == null)
    { res.add(""); } 
    else 
    { res.add(fullcond.queryForm(env,local)); } 
    res.add("UPDATE " + ename + Association.genSQLCode(rels,eId,cond,succ));
    return res;  
  } // actually updates entity of target of succ
    // only make test external to UPDATE if it only involves fx
   
  public String toOcl()
  { Entity e = null; 
    String ename = "";
    String ex = ""; 
    java.util.Map env = new java.util.HashMap(); 

    if (owner == null)
    { if (associations.size() > 0)
      { e = ((Association) associations.get(0)).getEntity1(); } 
    }
    else 
    { e = owner; }
    
    if (e != null) 
    { ename = e.getName(); 
      ex = ename.toLowerCase() + "x"; 
      env.put(ename,ex); 
    }
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    String arg = Association.getOclCode(associations,env,fullcond,
                                        succ,false);
    if (e != null)
    { return ename + ".allInstances()->forAll(" + ex + " |" + arg + ")"; } 
    return arg; // but meaningless
  }
                                  
  public BExpression binvariant()
  { // If associations empty, ent is owning entity
    Entity entity = owner;
    Vector needed = allEntitiesUsedIn(); // innermostEntities();
    System.out.println("Needed entities: " + needed); 
    Vector rels0 = AssociationPaths.getNeededAssociations(needed);
    System.out.println("needed associations: " + rels0);  
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    if (associations.size() != 0)  // the first needed entity
    { Association ast =
        (Association) associations.get(0);
      entity = ast.getEntity1();
    }
    if (entity == null)
    { return new BBasicExpression("false"); } 
    String ename = entity.getName();
    String es = ename.toLowerCase() + "s";
    String ex = ename.toLowerCase() + "x";
    BExpression evar = new BBasicExpression(ex);
    BExpression bes = new BBasicExpression(es);
    BExpression range =
      new BBinaryExpression(":",evar,bes);
    env.put(ename,evar);
    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    BExpression pred =   // plus intermediate entities in needed
      Association.genBInvariantCode(rels,needed,env,fullcond,succ);
    BExpression res = 
      new BQuantifierExpression("forall",ex,
        new BBinaryExpression("=>",range,pred));
    // for each variable, add quantifier var: T
    return addVariableQuantifiers(variables,res); 
  }

  public BExpression bprecondition(java.util.Map env)
  { // If associations empty, ent is owning entity
    /* Entity entity = owner;
    Vector needed = allEntitiesUsedIn(); // innermostEntities();
    System.out.println("Needed entities: " + needed); 
    Vector rels0 = AssociationPaths.getNeededAssociations(needed);
    System.out.println("needed associations: " + rels0);  
    Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    if (associations.size() != 0)  // the first needed entity
    { Association ast =
        (Association) associations.get(0);
      entity = ast.getEntity1();
    }
    if (entity == null)
    { return new BBasicExpression("false"); } 
    String ename = entity.getName();
    String ex = ename.toLowerCase() + "x";
    BExpression evar = new BBasicExpression(ex);
    env.put(ename,evar); */ 

    Vector rels = (Vector) associations.clone();
    Vector needed = allEntitiesUsedIn(); // innermostEntities();

    Expression fullcond = Expression.simplify("&",cond0,cond,new Vector()); 
    BExpression pred =   // plus intermediate entities in needed
      Association.genBInvariantCode(rels,needed,env,fullcond,succ);
    // for each variable, add quantifier var: T
    return addVariableQuantifiers(variables,pred); 
  }


  public BExpression binvariantForm(java.util.Map env, boolean local)
  { Vector empty = new Vector(); 
    Expression fullcond = Expression.simplify("&",cond0,cond,empty); 
    Expression exp = Expression.simplify("=>",fullcond,succ,empty); 
    return exp.binvariantForm(env,local); 
  }

  public String queryOperation(Entity ent)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    env.put(ent.getName(),"this");  // "oo" ??? 
    int index = 2;
    return Association.genEventCode(rels,env,cond,
                                    index,succ,false); 
  }


  public String queryOperationJava6(Entity ent)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    env.put(ent.getName(),"this");  // "oo" ??? 
    int index = 2;
    return Association.genEventCodeJava6(rels,env,cond,
                                    index,succ,false); 
  }

  public String queryOperationJava7(Entity ent)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    env.put(ent.getName(),"this");  // "oo" ??? 
    int index = 2;
    return Association.genEventCodeJava7(rels,env,cond,
                                    index,succ,false); 
  }

  public String queryOperationCSharp(Entity ent)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    env.put(ent.getName(),"this");  // "oo" ??? 
    int index = 2;
    return Association.genEventCodeCSharp(rels,env,cond,
                                    index,succ,false); 
  }

  public String queryOperationCPP(Entity ent)
  { Vector rels = (Vector) associations.clone();
    java.util.Map env = new java.util.HashMap();
    env.put(ent.getName(),"this");  // "oo" ??? 
    int index = 2;
    return Association.genEventCodeCPP(rels,env,cond,
                                    index,succ,false); 
  } 

  public static BExpression addVariableQuantifiers(Vector vars,
                                                   BExpression e)
  { for (int i = 0; i < vars.size(); i++) 
    { Attribute att = (Attribute) vars.get(i); 
      String nme = att.getName(); 
      Type t = att.getType(); 
      String bt = t.generateB(att.getElementType()); 
      BExpression btbe = new BBasicExpression(bt); 
      BExpression attbe = new BBasicExpression(nme); 
      BExpression range = new BBinaryExpression(":",attbe,btbe); 
      e = new BQuantifierExpression("forall",nme,
                new BBinaryExpression("=>",range,e)); 
    } 
    return e; 
  } 

  public String saveAsUSEData()
  { String res = "context " + owner + " inv:\n"; 
    java.util.Map env = new java.util.HashMap(); 

    Expression ante = antecedent(); 
    if (ante == null || "true".equals(ante + "")) { } 
    else 
    { res = res + "  " + ante.toOcl(env,true) + " =>"; } 
    res = res + "  " + succ.toOcl(env,true) + "\n"; 
    return res; 
  } 

  public String saveData(PrintWriter out)
  { out.println("Constraint:"); 
    out.println(cond0); 
    out.println(cond); 
    out.println(succ); 
    String res = "Constraint:\n" + cond0 + "\n" + 
           cond + "\n" + succ + "\n"; // also behavioural
    String line4 = ""; 
    if (associations.size() == 0) 
    { res = res + owner;
      line4 = line4 + owner;
      if (ownerisPre) 
      { res = res + "@pre"; 
        line4 = line4 + "@pre"; 
      } 
    } 
    else 
    { for (int i = 0; i < associations.size(); i++)
      { String assn = ((ModelElement) associations.get(i)).getName() + " ";
        res = res + assn; 
        line4 = line4 + assn; 
      } 
    }
    if (usecase == null) 
    { } 
    else 
    { res = res + " " + usecase.getName(); 
      line4 = line4 + " " + usecase.getName(); 
    } 
    out.println(line4);  
    res = res + "\n";  
    String line5 = "false"; 
    if (ordered) 
    { line5 = "" + orderedBy; 
      res = res + orderedBy; 
    } 
    else 
    { res = res + "false"; } 
    out.println(line5); 
    res = res + "\n"; 
    
    out.println(); 
    out.println(); 
    return res + "\n\n"; 
  }  // the baseEntities are regenerated

  public String saveModelData(PrintWriter out)
  { String id = Identifier.nextIdentifier("constraint_");  
    out.println(id + " :  Constraint"); 

    if (owner != null) 
    { out.println(id + ".owner = " + owner); } 

    if (cond0 != null)
    { String cond0id = cond0.saveModelData(out); 
      out.println(id + ".condition0 = " + cond0id); 
    }  

    if (cond != null) 
    { String condid = cond.saveModelData(out); 
      out.println(id + ".condition = " + condid);
    }  

    String succid = succ.saveModelData(out); 
    out.println(id + ".succedent = " + succid);

    if (orderedBy != null) 
    { String obid = orderedBy.saveModelData(out);     
      out.println(id + ".orderedBy = " + obid);
    } 

    if (usecase != null) 
    { out.println(id + ".usecase = " + usecase.getName()); } 
    
    return id; 
  }  // the baseEntities are regenerated

  public void saveKM3(PrintWriter out)
  { String ostring = ""; 
    if (owner != null) 
    { ostring = owner.getName(); } 

    out.println("    " + ostring + "::"); 
    out.println(antecedent() + " => " + succedent() + ";"); 
    out.println(); 
  }  // the baseEntities are regenerated

  public String getKM3()
  { String ostring = ""; 
    if (owner != null) 
    { ostring = owner.getName(); } 

    String res = "    " + ostring + "::\n"; 
    res = res +  "      " + antecedent() + " => " + succedent() + ";\n"; 
    res = res + "\n"; 
    return res; 
  }  // the baseEntities are regenerated

  public String toAST()
  { String ostring = ""; 
    if (owner != null) 
    { ostring = owner.getName(); } 

    String res = "(Constraint " + ostring + " :: "; 
    res = res + antecedent().toAST() + " => " + succedent().toAST() + " ; )"; 
    return res; 
  }  // the baseEntities are regenerated

  public String saveAssertion(PrintWriter out)
  { out.println("Assumption:"); 
    out.println(cond0); 
    out.println(cond); 
    out.println(succ); 
    String res = "Assumption:\n" + cond0 + "\n" + 
           cond + "\n" + succ + "\n"; // also behavioural
    String line4 = ""; 
    if (associations.size() == 0) 
    { res = res + owner;
      line4 = line4 + owner;
    } 
    else 
    { for (int i = 0; i < associations.size(); i++)
      { String assn = ((ModelElement) associations.get(i)).getName() + " ";
        res = res + assn; 
        line4 = line4 + assn; 
      } 
    }
    if (usecase == null) 
    { } 
    else 
    { res = res + " " + usecase.getName(); 
      line4 = line4 + " " + usecase.getName(); 
    } 
    out.println(line4);  
    res = res + "\n";  
    
    out.println(); 
    out.println(); 
    return res + "\n\n"; 
  }  // the baseEntities are regenerated

  public String saveInvariant(PrintWriter out)
  { out.println("UseCaseInvariant:"); 
    out.println(cond0); 
    out.println(cond); 
    out.println(succ); 
    String res = "UseCaseInvariant:\n" + cond0 + "\n" + 
           cond + "\n" + succ + "\n"; // also behavioural
    String line4 = ""; 
    if (associations.size() == 0) 
    { res = res + owner;
      line4 = line4 + owner;
    } 
    else 
    { for (int i = 0; i < associations.size(); i++)
      { String assn = ((ModelElement) associations.get(i)).getName() + " ";
        res = res + assn; 
        line4 = line4 + assn; 
      } 
    }
    if (usecase == null) 
    { } 
    else 
    { res = res + " " + usecase.getName(); 
      line4 = line4 + " " + usecase.getName(); 
    } 
    out.println(line4);  
    res = res + "\n";  
    
    out.println(); 
    out.println(); 
    return res + "\n\n"; 
  }  // the baseEntities are regenerated

  public Constraint matches(String op, String feature,
                      Entity ent, String val,
                      BehaviouralFeature ev)
  { // returns null if opfeature(oo,val) does not
    // affect constraint, otherwise returns action cons.
    if (!behavioural)
    { return null; }  // generate a precondition instead
    if (succ == null) { return null; }

    Expression cond1 = Expression.simplify("&",cond0,cond,new Vector());
        
    if (event != null)
    { System.out.println("Trying Match: " + op + feature + 
                         " with " + this); 
      if (event.getName().equals(op + feature))
      { Constraint cc = new Constraint(ev,cond1,succ,associations,baseEntities);
        cc.setOwner(owner); 
        return cc; 
      }
      return null; 
    } // likewise if succ is an event

    BasicExpression valbe = new BasicExpression(val); 
    // set its type

    Vector rdf = readFrame(); 
    System.out.println("READ frame: " + rdf + " " + feature); 

    if (rdf.contains(ent + "::" + feature))
    { if (op.equals("add") && (succ instanceof BinaryExpression))
      { BinaryExpression besucc = (BinaryExpression) succ; 
        if ("!".equals(besucc.operator) && 
            feature.equals(((BinaryExpression) besucc.left).right + ""))
        { Expression newright = 
            besucc.right.substituteEq(((BinaryExpression) besucc.left).left + "",valbe); 

          System.out.println("** Instantiated constraint: " + newright); 
 
          Constraint cc = new Constraint(ev,cond1,newright,associations,baseEntities); 
          cc.setOwner(owner); 
          return cc; 
        }
        else if ("#".equals(besucc.operator) && 
            feature.equals(((BinaryExpression) besucc.left).right + ""))
        { return null; }
        else 
        { return this; }   
      } 
      else if (op.equals("remove") && (succ instanceof BinaryExpression))
      { BinaryExpression besucc = (BinaryExpression) succ; 
        if ("!".equals(besucc.operator) && 
            feature.equals(((BinaryExpression) besucc.left).right + ""))
        { return null; } 
        else 
        { return this; } 
      } 
      else 
      { return this; } 
    } 

    UpdateFeatureInfo targ = succ.updateFeature(feature);
    if (targ == null || targ.feature == null)
    { return null; } // can't update anything

    Vector foccs = succ.getUses(feature); // For wpc: getRelevantUses?
    System.out.println(feature + " uses are: " + foccs); 
    boolean allNaked = Expression.nakedUses(feature,foccs); 
    System.out.println(allNaked + "\n"); 
        
    String ename = ent.getName();

    if (cond0 == null)  // look for occurrences in cond instead
    { if (cond != null) 
      { // if (foccs.size() > 0)  // but only the relevant ones are wanted
        if (cond.relevantOccurrence(op,ent,val,feature) || isRolenameOf(feature))
        { System.out.println("Relevant occurrence: " + feature + " " +                              cond);
          Expression newcond;
          Expression newsucc = targ.newexp; 
          if (op.equals("set") && allNaked)
          { newcond = cond.substituteEq(feature,valbe); 
            newsucc = newsucc.substituteEq(feature,valbe); 
          } // assume succ has only read-only occurrences of feature.
          else 
          { newcond = cond; } 
          // in B: substitute f \\/ { valbe }  for add, etc. 
          Expression cond2 = newcond.simplify(); 
          Constraint cc =
            new Constraint(ev,cond2,newsucc,associations,baseEntities);
          cc.setOwner(owner); 
          return cc;  
        } // must add information that valbe is of parameter type
        System.out.println("Not relevant occurrence: " + feature + " " + cond);
          
      }
      succ = targ.newexp; 
      return matchesRhs(op,feature,ename,val,ev); 
    } 

    System.out.println("Trying to match: " + feature + " " + val + 
                       " against " + this); 
    Expression newcond0; 
    Expression newcond = new BasicExpression("true");
    Expression newsucc = targ.newexp; 
  
    if (cond0.relevantOccurrence(op,ent,val,feature) || isRolenameOf(feature))
    {   
      if (op.equals("set") && allNaked)
      { newcond0 = cond0.substituteEq(feature,valbe); 
        newsucc = newsucc.substituteEq(feature,valbe); 
      }
      else 
      { newcond0 = cond0; }
      if (cond != null) 
      { if (op.equals("set") && allNaked)
        { newcond = cond.substituteEq(feature,valbe); }
        else 
        { newcond = cond; }
      }
      Expression cond2 = (new BinaryExpression("&",newcond0,newcond)).simplify(); 
      Constraint cc = new Constraint(ev,cond2,newsucc,associations,baseEntities);
      cc.setOwner(owner); 
      return cc;  
    } // must add information that valbe is of parameter type, in type check
    else if (cond != null) 
    { // Vector foccs = cond.getUses(feature); For wpc: getRelevantUses?
      // System.out.println(cond); 
      // if (foccs.size() > 0)  // but only the relevant ones are wanted
      if (cond.relevantOccurrence(op,ent,val,feature) || isRolenameOf(feature))
      { System.out.println("Relevant occurrence: " + feature + " " + cond);
        if (op.equals("set") && allNaked)
        { newcond = cond.substituteEq(feature,valbe); 
          newcond0 = cond0.substituteEq(feature,valbe); 
          newsucc = newsucc.substituteEq(feature,valbe); 
        } // assume succ has only read-only occurrences of feature.
        else 
        { newcond = cond; 
          newcond0 = cond0; 
        } 
        // in B: substitute f \\/ { valbe }  for add, etc. 
        Expression cond2 = (new BinaryExpression("&",newcond0,newcond)).simplify(); 
        Constraint cc = 
          new Constraint(ev,cond2,newsucc,associations,baseEntities); 
        cc.setOwner(owner); 
        return cc; 
      } // must add information that valbe is of parameter type
      else 
      { System.out.println("Not relevant occurrence: " + feature + " " + cond); }
    }      
    succ = targ.newexp; 
    return matchesRhs(op,feature,ename,val,event);
  } // easier to combine cond0 and cond first

  // but no match if rolename and a remove??

  public Constraint matchCreate(String ex,
    String ename, BehaviouralFeature ev)
  { // assume associations empty
    // if (!behavioural) { return null; }
    Expression cond1 =
        Expression.simplify("&",cond0,cond,new Vector());
    if (cond1 == null) { return null; }
    if (cond1 instanceof BinaryExpression)
    { BinaryExpression antexp = 
        (BinaryExpression) cond1;
      if ((antexp.right + "").equals(ename) && antexp.operator.equals(":"))
      { Expression newsucc =
            succ.substituteEq(antexp.left + "",
              new BasicExpression(ex));
        Constraint cc = new Constraint(ev,
            new BasicExpression("true"),newsucc,
              associations,baseEntities);
        cc.setOwner(owner); 
        return cc; 
      }
    }  // and this conjoined with other formulae
    Vector rf = readFrame(); 
    if (rf.contains(ename))
    { return this; } 
    return null;
  }

  public Constraint matchKill(String ex,
    String ename, BehaviouralFeature ev)
  { // assume associations empty
    // if (!behavioural) { return null; }
    /* Expression cond1 =
        Expression.simplify("&",cond0,cond,new Vector());
    if (cond1 == null) { return null; }
    if (cond1 instanceof BinaryExpression)
    { BinaryExpression antexp = 
        (BinaryExpression) cond1;
      if ((antexp.right + "").equals(ename) && antexp.operator.equals(":"))
      { Expression newsucc =
            succ.substituteEq(antexp.left + "",
              new BasicExpression(ex));
         return new Constraint(ev,
            new BasicExpression("true"),newsucc,
              associations,baseEntities);
      }
    } */ 
    Vector rf = readFrame(); 
    if (rf.contains(ename))
    { return this; } 
    return null;
  }


  public Constraint matchesOp(String op, BehaviouralFeature ev)
  { // returns null if opfeature(oo,val) does not
    // affect constraint, otherwise returns action cons.
    if (!behavioural)
    { return null; }  // generate a precondition instead
    if (succ == null) { return null; }

    if (event != null)
    { System.out.println("Trying op match: " + op + 
                         " with " + this); 
      if (event.getName().equals(op))
      { Expression cond1 = Expression.simplify("&",cond0,cond,new Vector());
        Constraint res = new Constraint(ev,cond1,succ,associations,baseEntities);
        res.setOwner(owner); 
        return res; 
      }
    }
    return null; 
  }  // wr(ev.post) intersection (wr(this) union rd(this)) not empty

  public Constraint bmatches(String op, String feature,
                      Entity ent, String val,
                      BehaviouralFeature ev)
  { // returns null if opfeature(oo,val) does not
    // affect constraint, otherwise returns action cons.
    if (!behavioural) { return null; } // return a precondition instead
    if (succ == null) { return null; }
    // if (cond0 == null) { return null; } // No:
    /* if relevant occ in cond, take wpc of it, else if in succ
       do substitute of new value in it. try cond or succ */ 

    if (event != null)
    { System.out.println("Trying Match: " + op + feature + 
                         " with " + this); 
      if (event.getName().equals(op + feature))
      { Expression cond1 = Expression.simplify("&",cond0,cond,new Vector());
        Constraint cnew = new Constraint(ev,cond1,succ,associations,baseEntities);
        if (actionInv) { cnew.setActionInv(); } 
        cnew.setOwner(owner); 
        return cnew; 
      }
      return null; 
    } // likewise if succ is an event

    UpdateFeatureInfo targ = succ.updateFeature(feature);
    if (targ == null || targ.feature == null)
    { return null; } // can't update anything

    succ = targ.newexp;
    Vector uses = succ.getUses(feature);

    String ename = ent.getName();
    BasicExpression oobe = new BasicExpression(ename.toLowerCase() + "x");
    BasicExpression valbe = new BasicExpression(val);

    System.out.println("Trying to match: " + feature + " " + val + 
                       " against " + this +
                       "\n Uses are: " + uses + " Update feature " + targ);
    if (cond0 == null)
    { if (cond == null)
      { if (uses.size() > 0)
        { Expression newpost =
            BehaviouralFeature.wpc(succ,uses,oobe,op,
                                   feature,ev.isOrdered(),valbe);
          System.out.println("wpc of " + op + " " + ev.isOrdered() + " " + 
                             succ + " is " + newpost); 
          Constraint cc = 
            new Constraint(ev,cond,newpost,associations,baseEntities); 
          cc.setOwner(owner); 
          return cc; 
        }
        else if (isRolenameOf(feature))
        { Constraint cc = 
            new Constraint(ev,cond,succ,associations,baseEntities); 
          cc.setOwner(owner); 
          return cc; 
        } 
        else 
        { return null; } 
      }
      else
      { Vector uses2 = cond.getUses(feature);
        if (uses.size() + uses2.size() > 0)
        { Expression newcond =
            BehaviouralFeature.wpc(cond,uses2,oobe,op,feature,
                                   ev.isOrdered(),valbe);
          System.out.println(op + " " + ev.isOrdered() + 
                             " wpc of " + cond + " is " + newcond); 
       
          Expression newpost =
            BehaviouralFeature.wpc(succ,uses,oobe,op,feature,
                                   ev.isOrdered(),valbe); 
          System.out.println(op + " wpc of " + succ + " is " + newpost); 
          Constraint cc = 
            new Constraint(ev,newcond,newpost,associations,baseEntities);
          cc.setOwner(owner); 
          return cc;  
        }
        else if (isRolenameOf(feature))
        { Constraint cc = 
            new Constraint(ev,cond,succ,associations,baseEntities);
          cc.setOwner(owner); 
          return cc;
        } 
        else
        { return null; }
      }
    }

    if (cond0.relevantOccurrence(op,ent,val,feature))
    { System.out.println("Relevant occurrence: " + cond0 + " of " + 
                         feature); 
      if (analysable(feature))
      { System.out.println("Analysable " + this + " " + feature); 
        if (op.equals("set"))
        { return matchSet(feature,val,ev,oobe); }
        else if (op.equals("add"))
        { Constraint newcc = matchAdd(feature,ev,oobe,valbe);
          newcc.setOwner(owner); 
          System.out.println("NEW CONSTRAINT: " + newcc); 
          return newcc; 
        }
        else if (op.equals("remove"))
        { return matchRemove(feature,ev,oobe,valbe); }
      }
      else 
      { Expression pre = Expression.simplify("&",cond0,cond,new Vector()); 
        Vector uses3 = pre.getUses(feature); 
        Expression newcond =
            BehaviouralFeature.wpc(pre,uses3,oobe,op,feature,
                                   ev.isOrdered(),valbe);
        System.out.println(op + " wpc of " + pre + " is " + newcond); 
       
        Expression newpost =
            BehaviouralFeature.wpc(succ,uses,oobe,op,feature,
                                   ev.isOrdered(),valbe); 
        System.out.println(op + " wpc of " + succ + " is " + newpost); 
        Constraint cc =
          new Constraint(ev,newcond,newpost,associations,baseEntities);
        cc.setOwner(owner); 
        return cc;  
      }
    }         

    if (uses.size() > 0)  // feature affects the succedent 
    { Expression pre = Expression.simplify("&",cond0,cond,new Vector()); 
      Vector uses3 = pre.getUses(feature); 
      Expression newcond =
            BehaviouralFeature.wpc(pre,uses3,oobe,op,feature,
                                   ev.isOrdered(),valbe);
      System.out.println(op + " wpc of " + pre + " is " + newcond); 
       
      Expression newpost =
            BehaviouralFeature.wpc(succ,uses,oobe,op,feature,
                                   ev.isOrdered(),valbe); 
      System.out.println(op + " wpc of " + succ + " is " + newpost); 
      Constraint cc = 
        new Constraint(ev,newcond,newpost,associations,baseEntities);
      cc.setOwner(owner); 
      return cc;  
    }
    System.out.println("Not matched"); // return wpc of ante if relevent
    return null;
  }

  private Constraint matchAdd(String feature,
                              BehaviouralFeature event,
                              Expression oobe,
                              Expression xxbe)
  { System.out.println("Match add with " + feature + " " + event + 
                       " " + oobe + " " + xxbe); 

    if (operator.equals(":"))
    { if (rightbe.data.equals(feature))
      { Expression newcond;
        Expression newsucc;
        if (leftbe.umlkind == Expression.VARIABLE)
        { System.out.println("Case of a variable: " + leftbe); 
          newcond = Expression.substitute(cond,leftbe,xxbe);
          newsucc =
            succ.substituteEq("" + leftbe,xxbe);
        }
        else
        { Expression eq1 =
            new BinaryExpression("=",xxbe,leftbe);
          newcond = Expression.simplify("&",eq1,cond,new Vector());
          newsucc = succ;
        }
        if (rightbe.objectRef == null)
        { Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else if (rightbe.objectRef.umlkind == Expression.VARIABLE)
        { newcond = Expression.substitute(newcond, 
                                          rightbe.objectRef,
                                          oobe);
          newsucc =
             newsucc.substitute(rightbe.objectRef,
                                oobe);
          Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else if (rightbe.objectRef.isMultiple())
        { Expression eq2 =
            new BinaryExpression(":",oobe,
                                 rightbe.objectRef);
          newcond = Expression.simplify("&",eq2,newcond,new Vector()); 
          Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else
        { Expression eq2 =
            new BinaryExpression("=",oobe,
                                 rightbe.objectRef);
          newcond = Expression.simplify("&",eq2,newcond,new Vector());
          Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
      }
      return null;
    }
    return null;
    // case of removerole(oo,xx)
  }


  /* I don't understand this at all! */

  private boolean analysable(String feature) // true if matchSet, matchAdd, 
  {                                          // matchRemove can be used
    if (operator == null) { return false; }  // no cond0
    if (operator.equals("=") || operator.equals("<:") || 
        operator.equals("<=") || operator.equals(">=")) // leftbe = rightbe
    { if (leftbe.data.equals(feature))
      { return true; } 
    }
    else if (operator.equals("<") || operator.equals(">"))
    { if (leftbe.data.equals(feature))
      { return true; }
    }  // And other way round for both the above
    else if (operator.equals(":") || operator.equals("/:"))
    { if (feature.equals(rightbe.data))
      { return true; } 
    }
    else if (operator.equals("/=")) // leftbe /= rightbe
    { if (leftbe.data.equals(feature))
      { return true; }
    }
    return false;
  }

  private Constraint matchSet(String feature,String val,
                              BehaviouralFeature event,
                              Expression oobe)
  { if (operator == null) { return null; }  // no cond0
    if (operator.equals("=") || operator.equals("<:") || 
        operator.equals("<=") || operator.equals(">=")) // leftbe = rightbe
    { if (leftbe.data.equals(feature))
      { return matchSetEq(feature,val,event,oobe); } 
    }
    else if (operator.equals("<") || operator.equals(">"))
    { if (leftbe.data.equals(feature))
      { return matchSetLG(feature,val,event,oobe); }
    }  // And other way round for both the above
    else if (operator.equals(":"))
    { if (feature.equals(rightbe.data))
      { if (rightbe.objectRef == null)
        { return null; } // val: feature not correct
        else if (rightbe.objectRef.isMultiple())
        { Expression newante =
            new BinaryExpression(":",oobe,
                                 rightbe.objectRef);
          BasicExpression valbe = new BasicExpression(val); 
          Expression ante1 =
            new BinaryExpression("=",valbe,leftbe);
          newante = new BinaryExpression("&",newante,ante1); 
          Expression ante2 =
            Expression.simplify("&",newante,cond,new Vector()); 
          Constraint cc = 
            new Constraint(event,ante2, 
                           succ,associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        return null;
      } // cases of /= and /:
    }
    else if (operator.equals("/=")) // leftbe /= rightbe
    { if (leftbe.data.equals(feature))
//          rightbe.umlkind == Expression.VALUE)
//          !val.equals("" + rightbe))
      { Expression valbe = new BasicExpression(val);
        Expression neq = new BinaryExpression("/=",valbe,rightbe); 
        Expression newcond = Expression.simplify("&",neq,cond,new Vector()); 
        if (leftbe.objectRef == null)
        { Constraint cc = 
            new Constraint(event,newcond,succ,associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else if (leftbe.objectRef.isMultiple())
        { // oo : objs
          Expression eq3 =
            new BinaryExpression(":",oobe,
                                 leftbe.objectRef);
          Expression ante2 =
            Expression.simplify("&",eq3,newcond,new Vector());
          Expression newsucc = succ.substituteEq(feature,valbe); 
          Constraint cc = 
            new Constraint(event,ante2,newsucc,associations,baseEntities); 
          cc.setOwner(owner); 
          return cc; 
        } 
        else if (leftbe.objectRef.umlkind == Expression.VARIABLE)  
        { Expression ncond =
            Expression.substitute(newcond,leftbe.objectRef,oobe);
          Expression nsucc =
            succ.substitute(leftbe.objectRef,oobe);
          Constraint cc = new Constraint(event,ncond,nsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else
        { Expression eq4 =
            new BinaryExpression("=",leftbe.objectRef,
                                 oobe);
          Expression ante2 =
            Expression.simplify("&",eq4,newcond,new Vector());
          Expression newsucc = succ.substituteEq(feature,valbe); 
          Constraint cc = new Constraint(event,ante2,newsucc,  
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
      }
    }
    return null;
  }

  private Constraint matchSetEq(String feature,String val,
                              BehaviouralFeature event,
                              Expression oobe)
  { Expression newcond;
    Expression newsucc;
    System.out.println("Trying to match set " + feature + val); 
    Expression valbe = new BasicExpression(val);
    if (rightbe.umlkind == Expression.VARIABLE &&
        operator.equals("="))
    { valbe = new BasicExpression(val);
      newcond = Expression.substitute(cond,rightbe,valbe);
      newsucc = succ.substitute(rightbe,valbe);
    }
    else
    { System.out.println("Trying match set"); 
      Expression xxbe = new BasicExpression(val);
      if (cond != null)
      { newcond = cond.substituteEq(feature,xxbe); }
      else
      { newcond = cond; }
      Expression eq3 =
        new BinaryExpression(operator,xxbe,rightbe);
      newcond =
        Expression.simplify("&",eq3,newcond,new Vector());
      newsucc = succ.substituteEq(feature,xxbe);
      valbe = rightbe;
    }
    if (leftbe.objectRef == null)
    { Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
    else if (leftbe.objectRef.umlkind == Expression.VARIABLE)
    { newcond = Expression.substitute(newcond,leftbe.objectRef,oobe);
      newsucc =
        newsucc.substitute(leftbe.objectRef,oobe);
      Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
    else if (leftbe.objectRef.isMultiple())
    { BasicExpression oof =
        new BasicExpression(feature);
      SetExpression ss = new SetExpression(); 
      ss.addElement(oobe); 
      Expression nleft =
        new BinaryExpression("-",leftbe.objectRef,ss);
      nleft.setBrackets(true); 
      oof.setObjectRef(nleft);  
      Expression newante =
        new BinaryExpression(operator,oof,rightbe);
      Expression ante2 = Expression.simplify("&",newante,newcond,
                                             new Vector()); 
      Constraint cc = new Constraint(event,ante2, 
                                     newsucc,associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    } 
    else // single object, not multiple.
    { Expression eq8 =
        new BinaryExpression("=",leftbe.objectRef,oobe);
      newcond =
        Expression.simplify("&",eq8,newcond,new Vector());
      Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
  } 

  private Constraint matchSetLG(String feature,String val,
                                BehaviouralFeature event,
                                Expression oobe)
  { Expression newcond;
    Expression newsucc;
    Expression valbe = new BasicExpression(val);
    /* if (val.equals("" + rightbe))
    { return null; }   
    else   // if both VALUE, can decide if val operator rightbe is true
    { */
    Expression xxbe = new BasicExpression(val);
    Expression eq3 =
      new BinaryExpression(operator,xxbe,rightbe);
    newcond =
      Expression.simplify("&",eq3,cond,new Vector());
    newsucc = succ.substituteEq(feature,xxbe);   // not just succ
    valbe = rightbe;
    
    if (leftbe.objectRef == null)
    { Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
    else if (leftbe.objectRef.umlkind == Expression.VARIABLE)
    { newcond =
        newcond.substitute(leftbe.objectRef,oobe);
      newsucc =
        newsucc.substitute(leftbe.objectRef,oobe);
      Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
    else if (leftbe.objectRef.isMultiple())
    { BasicExpression oof =
        new BasicExpression(feature);
      SetExpression ss = new SetExpression(); 
      ss.addElement(oobe); 
      Expression nleft =
        new BinaryExpression("-",leftbe.objectRef,ss);
      nleft.setBrackets(true); 
      oof.setObjectRef(nleft);  
      Expression newante =
        new BinaryExpression(operator,oof,rightbe);
      Expression ante2 = Expression.simplify("&",newante,newcond,
                                             new Vector()); 
      Constraint cc = new Constraint(event,ante2, 
                                     newsucc,associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    } 
    else // single object, not multiple.
    { Expression eq8 =
        new BinaryExpression("=",leftbe.objectRef,oobe);
      newcond =
        Expression.simplify("&",eq8,newcond,new Vector());
      Constraint cc = new Constraint(event,newcond,newsucc,
                                     associations,baseEntities);
      cc.setOwner(owner); 
      return cc; 
    }
  } 


  private Constraint matchRemove(String feature,
                              BehaviouralFeature event,
                              Expression oobe,
                              Expression xxbe)
  { if (operator.equals("/:"))
    { if (rightbe.data.equals(feature))
      { Expression newcond;
        Expression newsucc;
        Expression valbe;
        if (leftbe.umlkind == Expression.VARIABLE)
        { newcond =
            Expression.substitute(cond,leftbe,xxbe);
          newsucc =
            succ.substituteEq("" + leftbe,xxbe);
          valbe = xxbe;
        }
        else
        { Expression eq1 =
            new BinaryExpression("=",xxbe,leftbe);
          newcond = Expression.simplify("&",eq1,cond,new Vector()); 
          newsucc = succ;
          valbe = leftbe;
        }
        if (rightbe.objectRef == null)
        { Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else if (rightbe.objectRef.umlkind == Expression.VARIABLE)
        { newcond =
             Expression.substitute(newcond,rightbe.objectRef,
                                   oobe);
          newsucc =
             newsucc.substitute(rightbe.objectRef,
                                  oobe);
          Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }  // assuming variable non-multiple
        else if (rightbe.objectRef.isMultiple())
        { Expression eq2 =
            new BinaryExpression(":",oobe,
                                 rightbe.objectRef);
          // also  leftbe /: (objs - {oo}).role
          newcond = Expression.simplify("&",eq2,newcond,new Vector()); 
          BasicExpression oof =
            new BasicExpression(feature);
          SetExpression ss = new SetExpression(); 
          ss.addElement(oobe); 
          Expression nright =
            new BinaryExpression("-",rightbe.objectRef,ss);
          nright.setBrackets(true); 
          oof.setObjectRef(nright); 
          Expression newante =
            new BinaryExpression("/:",valbe,oof);
          Expression ante2 = 
            Expression.simplify("&",newante,newcond,new Vector()); 
          Constraint cc = new Constraint(event,ante2, 
                                         newsucc,associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
        else
        { Expression eq2 =
            new BinaryExpression("=",oobe,
                                 rightbe.objectRef);
          newcond = Expression.simplify("&",eq2,newcond,new Vector()); 
          Constraint cc = new Constraint(event,newcond,newsucc,
                                         associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        }
      }
      return null;
    }
    return null;
  }

  // get all the variable uses and construct their
  // types, as a map: String --> Type
  // for exp: uses
  //  if name /: params, add use to list of uses for
  //  that var. Add name to var list.
  // rationalise type of each var.
  private void variableTypeAssignment(Vector v1, Vector params)
  { Vector pars = new Vector();
    if (event != null)
    { pars = event.getParameterNames(); }
    pars.add("result");
    Vector vars = new Vector();
    java.util.Map uses = new java.util.HashMap();
    for (int i = 0; i < v1.size(); i++)
    { BasicExpression use = (BasicExpression) v1.get(i);
      if (vars.contains(use.data))
      { Vector vuses = (Vector) uses.get(use.data);
        vuses.add(use);
      }
      else if (!pars.contains(use.data))
      { vars.add(use.data);
        Vector newuses = new Vector();
        newuses.add(use);
        uses.put(use.data,newuses);
      }
    }
    // System.out.println("Variable uses are: " + uses); 
    variables = rationaliseVariableTypes(vars,uses,params);
  }

  public static Vector variableTypeAssign(Vector v1, Vector params)
  { // Vector pars = new Vector();
    Vector vars = new Vector();
    java.util.Map uses = new java.util.HashMap();
    for (int i = 0; i < v1.size(); i++)
    { BasicExpression use = (BasicExpression) v1.get(i);
      if (vars.contains(use.data))
      { Vector vuses = (Vector) uses.get(use.data);
        vuses.add(use);
      }
      else
      { vars.add(use.data);
        Vector newuses = new Vector();
        newuses.add(use);
        uses.put(use.data,newuses);
      }
    }
    // System.out.println("Variable uses are: " + uses); 
    return rationaliseVariableTypes(vars,uses,params);
  }


  public static Vector rationaliseVariableTypes(Vector vars,
                                                java.util.Map uses, Vector params)
  { Vector res = new Vector();
    for (int i = 0; i < vars.size(); i++)
    { String var = (String) vars.get(i);
      Vector vuses = (Vector) uses.get(var);
      Type t; 
      Attribute par = (Attribute) ModelElement.lookupByName(var,params); 
      if (par != null) 
      { t = par.getType(); } 
      else 
      { t = Type.determineType(vuses);
        if (t == null)
        { System.out.println("!! Warning: Badly typed variable: " + var);
          t = new Type("Object",null);  // why? 
        } 
      }

      for (int j = 0; j < vuses.size(); j++)
      { BasicExpression be = (BasicExpression) vuses.get(j);
        be.setType(t);
        // System.out.println("Type of " + be + " determined as: " + t); 
      }

      Attribute att = new Attribute(var,t,ModelElement.INTERNAL);
      res.add(att);
    }
    return res; 
  }

  public Constraint matchesRhs(String op, String feature,
                      String ename, String val,
                      BehaviouralFeature event)
  { // if feature occurs in Rhs, return constraint itself. 
    System.out.println("Matching RHS: " + op + " " + feature + " " 
                       + cond0 + " " + cond + " " + succ); 
    if (succ == null) { return null; } 

    Vector foccs = succ.getUses(feature); // For wpc: getRelevantUses?
    System.out.println(feature + " uses are: " + foccs); 
    boolean allNaked = Expression.nakedUses(feature,foccs); 
    System.out.println(allNaked + "\n"); 
    // And for cond, cond0

    Vector rfeatures = succ.allFeaturesUsedIn(); 
    if (rfeatures.contains(feature))
    { UpdateFeatureInfo targ = succ.updateFeature(feature);
      System.out.println("Update feature is: " + targ);
      if (targ != null && targ.feature != null)
      { Expression cond2 = Expression.simplify("&",cond0,cond,false); 
        Expression valbe = new BasicExpression(val); 
        if (op.equals("set") && allNaked)
        { Expression newsucc = succ.substituteEq(feature,valbe); 
          System.out.println("Multiplicity --- " + valbe + " --- is " + 
                             valbe.isMultiple()); 
          Constraint cc =
            new Constraint(event,cond2,newsucc,associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        } 
        Constraint cc = 
          new Constraint(event,cond2,succ,associations,baseEntities); 
        cc.setOwner(owner); 
        return cc; 
      } 
      else 
      { Expression pre = BehaviouralFeature.wpc(succ,op,feature,val); 
        System.out.println("Extra precondition needed on " + op + feature + 
                           ": " + pre); 
        return null; 
      } 
    } 
    return null; 
  } 

  public Constraint bmatchesRhs(String op, String feature,
                      String ename, String val,
                      BehaviouralFeature event)
  { // if feature occurs in Rhs, return constraint itself. 
    if (succ == null) { return null; } 
    Vector rfeatures = succ.allFeaturesUsedIn(); 
    if (rfeatures.contains(feature))
    { UpdateFeatureInfo targ = succ.updateFeature(feature);  
      System.out.println("Update feature is: " + targ); 
      if (targ != null && targ.feature != null)
      { if (op.equals("set"))
        { Expression newsucc = succ.substituteEq(feature,
                                      new BasicExpression(val));
          Constraint cc = 
            new Constraint(event,cond,newsucc,associations,baseEntities);
          cc.setOwner(owner); 
          return cc; 
        } // also replace feature by feature <+ { ex |-> valbe }
        Constraint cc = new Constraint(event,cond,succ,associations,baseEntities);
        cc.setOwner(owner); 
        return cc;
      } // for add, remove as well. 
      else
      { Expression pre = BehaviouralFeature.wpc(succ,op,feature,val); 
        System.out.println("Extra precondition needed on " + op + feature + 
                           ": " + pre); 
        return null; 
      } 
    } 
    return null; 
  } 


  private Constraint matchSetRhs(String feature,
                                 String val,
                                 Expression oobe,
                               BehaviouralFeature event)
  { if (succ instanceof BinaryExpression)
    { BinaryExpression bsucc = (BinaryExpression) succ;
      String op = bsucc.getOperator();
      Expression le = bsucc.getLeft();
      Expression re = bsucc.getRight();
      if (op.equals("="))
      { if (le instanceof BasicExpression)
        { BasicExpression lbe = (BasicExpression) le;
          if (lbe.data.equals(feature))    // and other way round
          { return matchSetEqRhs(feature,val,oobe,
                                 lbe,re,event);
          }
          else
          { return null; }
        }
      }
      else if (op.equals(":"))
      { if (le instanceof BasicExpression)
        { BasicExpression lbe = (BasicExpression) le;
          if (lbe.data.equals(feature))
          { return matchSetInRhs(feature,val,oobe,
                                 lbe,re,event);
          }
          else
          { return null; }
        }
      }
    }
    return null;
  }


  private Constraint matchAddRhs(String feature,
                                 Expression oobe,
                               BehaviouralFeature event)
  { if (succ instanceof BinaryExpression)
    { BinaryExpression bsucc = (BinaryExpression) succ;
      String op = bsucc.getOperator();
      Expression le = bsucc.getLeft();
      Expression re = bsucc.getRight();
      if (op.equals("="))
      { if (le instanceof BasicExpression)
        { BasicExpression lbe = (BasicExpression) le;
          if (lbe.data.equals(feature))
          { return matchAddEqRhs(feature,oobe,
                                 lbe,re,event);
          }
          else
          { return null; }
        }
      }
    }
    return null;
  }

  private Constraint matchRemoveRhs(String feature,
                                    Expression oobe,
                                    BehaviouralFeature event)
  { return matchAddRhs(feature,oobe,event); }

  private Constraint matchSetEqRhs(String feature,
                       String val, Expression oobe,
                       BasicExpression lbe,
                       Expression re,
                       BehaviouralFeature event)
  { Expression newsucc;
    Expression newcond = null;
    Expression newcond0 = null;
    BasicExpression valbe = new BasicExpression(val);
    if (lbe.objectRef == null)
    { if (re.toString().equals(val))
      { return null; } // nothing to do
      newsucc = new BinaryExpression("=",re,valbe);
      Constraint cc = new Constraint(event,cond0,cond,newsucc,
                                     associations);
      cc.setOwner(owner); 
      return cc; 
    }
    if (lbe.objectRef.umlkind == Expression.VARIABLE)
    { newsucc = new BinaryExpression("=",re,valbe);
      if (cond != null)
      { newcond = cond.substitute(lbe.objectRef,oobe); }
      if (cond0 != null)
      { newcond0 =
                cond0.substitute(lbe.objectRef,oobe);
      }
      newsucc = newsucc.substitute(lbe.objectRef,oobe);
      Constraint cc = new Constraint(event,newcond0,
                                     newcond,newsucc,
                                     associations);
      cc.setOwner(owner); 
      return cc;
    }
    if (lbe.objectRef.isMultiple())
    { // oo: objs => AX(re = (objs - {oo}).feature \/ {val})
      Expression extra;
      if (lbe.multiplicity == ModelElement.ONE) // mult of feature?
      { extra = new SetExpression();
        ((SetExpression) extra).addElement(valbe);
      }
      else
      { extra = valbe; }
      Expression pre =
        new BinaryExpression(":",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      SetExpression ooset = new SetExpression();
      ooset.addElement(oobe);
      BasicExpression oof = new BasicExpression(feature);
      Expression newref = new BinaryExpression("-",lbe.objectRef,ooset);
      newref.setBrackets(true); 
      oof.setObjectRef(newref); 
      
      newsucc =
        new BinaryExpression("=",re,
          new BinaryExpression("\\/",oof,extra));
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
    else
    { // oo = obj => AX(re = val)
      Expression pre =
        new BinaryExpression("=",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      newsucc =
        new BinaryExpression("=",re,valbe);
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
  }

  private Constraint matchSetInRhs(String feature,
                       String val, Expression oobe,
                       BasicExpression lbe,
                       Expression re,
                       BehaviouralFeature event)
  { Expression newcond = cond;
    Expression newcond0 = cond0; 
    BasicExpression valbe = new BasicExpression(val);
    Expression newsucc =
      new BinaryExpression(":",valbe,re);
    if (lbe.objectRef == null)
    { Constraint cc = new Constraint(event,cond0,cond,newsucc,
                                     associations);
      cc.setOwner(owner); 
      return cc; 
    }
    if (lbe.objectRef.umlkind == Expression.VARIABLE)
    { if (cond != null)
      { newcond = cond.substitute(lbe.objectRef,oobe); }
      if (cond0 != null)
      { newcond0 =
                cond0.substitute(lbe.objectRef,oobe);
      }
      newsucc = newsucc.substitute(lbe.objectRef,oobe);
      Constraint cc = new Constraint(event,newcond0,newcond,newsucc,
                                     associations);
      cc.setOwner(owner); 
      return cc; 
    }
    else if (lbe.objectRef.isMultiple()) // so is <:
    { String newop;
      if (lbe.multiplicity == ModelElement.ONE) // mult of feature?
      { newop = ":"; }
      else
      { newop = "<:"; }
      Expression pre =
        new BinaryExpression(":",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      newsucc =
        new BinaryExpression(newop,valbe,re);
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
    else
    { Expression pre =
        new BinaryExpression("=",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
  }

  private Constraint matchAddEqRhs(String feature,
                       Expression oobe,
                       BasicExpression lbe,
                       Expression re,
                       BehaviouralFeature event)
  { BasicExpression fbe = new BasicExpression(feature);
    Expression newsucc =
      new BinaryExpression("=",re,fbe);
    Expression newcond = null;
    Expression newcond0 = null;
    if (lbe.objectRef == null)
    { return new Constraint(event,cond0,cond,newsucc,
                            associations);
    }
    if (lbe.objectRef.umlkind == Expression.VARIABLE)
    { if (cond != null)
      { newcond = cond.substitute(lbe.objectRef,oobe); }
      if (cond0 != null)
      { newcond0 =
                cond0.substitute(lbe.objectRef,oobe);
      }
      newsucc = newsucc.substitute(lbe.objectRef,oobe);
      Constraint cc = new Constraint(event,newcond0,
                                     newcond,newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
    if (lbe.objectRef.isMultiple())
    { // oo: objs => AX(re = objs.feature)
      Expression pre =
        new BinaryExpression(":",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      fbe.setObjectRef(lbe.objectRef);
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
    else
    { // oo = obj => AX(re = val)
      Expression pre =
        new BinaryExpression("=",oobe,lbe.objectRef);
      newcond = Expression.simplify("&",cond,pre,
                                    new Vector());
      Constraint cc = new Constraint(event,cond0,newcond,
                                     newsucc,associations);
      cc.setOwner(owner); 
      return cc; 
    }
  }

  public DataDependency getDataFlows()
  { DataDependency dd = new DataDependency();
    if (!behavioural) 
    { return dd; }
    if (cond0 != null)
    { dd = cond0.getDataItems(); } 
    if (cond != null)
    { dd.union(cond.getDataItems()); }
    dd.union(succ.rhsDataDependency());
    return dd;
  }

  public Vector neededAssociations(Vector ents, Vector allassocs)
  { // builds up paths between ents as much as possible
    Map paths = new HashMap(); // ents --> seq(seq(Association))
    Vector res = new Vector(); 
    for (int i = 0; i < allassocs.size(); i++) 
    { Association ast = (Association) allassocs.get(i); 
      Entity e1 = ast.getEntity1(); 
      if (ents.contains(e1))
      { Vector newpath = new Vector(); 
        newpath.add(ast); 
        Vector e1paths = (Vector) paths.get(e1); 
        if (e1paths == null) 
        { Vector newpathv = new Vector(); 
          newpathv.add(newpath); 
          paths.put(e1,newpathv);
        } 
        else 
        { e1paths.add(newpath); }
      }
      else 
      { addToPaths(ast,ents,paths); } 
    } 
    System.out.println(paths); 
    return res;   // extract the assocs that link needed entities
  }

  private void addToPaths(Association ast, Vector ents, Map paths)
  { // ast.entity1 /: ents
    for (int i = 0; i < ents.size(); i++) 
    { Entity e = (Entity) ents.get(i); 
      Vector epaths = (Vector) paths.get(e); 
      if (epaths != null)
      { // find out if ast.entity1 is in any path, if so, extend or spawn
        for (int j = 0; j < epaths.size(); j++) 
        { Vector epath = (Vector) epaths.get(j); 
          for (int k = 0; k < epath.size(); k++)
          { Association east = (Association) epath.get(k); 
            if (east.getEntity2() == ast.getEntity1())
            { if (k == epath.size() - 1) // add to end
              { epath.add(ast); } 
              else 
              { Vector newpath = (Vector) epath.clone(); 
                // trim down to k elements then add ast. 
                epaths.add(newpath); 
              } 
            }
          }
        }
      }
    }
  }

  public String toXml()
  { String res = "    <constraint>\n" +
      "      <body>" + toString() + "</body>\n" +
      "    </constraint>\n";
    return res;
  }

  public static BinaryExpression regularise(Expression pre,
                                  Expression post)
  { Expression newpre = null;
    Expression newpost = post;
    while (pre instanceof BinaryExpression &&
           "=>".equals(((BinaryExpression) 
                                pre).operator))
    { BinaryExpression bpre = (BinaryExpression) pre;
      newpre = 
        Expression.simplify("&",newpre,bpre.left,false);
      pre = bpre.right;
    }
    newpre = 
        Expression.simplify("&",newpre,pre,false);
    while (newpost instanceof BinaryExpression &&
           "=>".equals(((BinaryExpression) 
                                newpost).operator))
    { BinaryExpression bpst = 
        (BinaryExpression) newpost;
      newpre = 
        Expression.simplify("&",newpre,bpst.left,false);
      newpost = bpst.right;
    }
    return new BinaryExpression("=>",newpre,newpost);
  }                       

  public void analyseConstraint(Vector assocs) 
  // calculates constraintKind and qvars, lvars
  { Constraint inv = this; 
    Vector rf = inv.readFrame(); 
    Vector wf = inv.wr(assocs); 
    int constraintType = 0; 
    Entity owner = inv.getOwner(); 

    if (owner != null)
    { if (inv.getispreOwner())
      { rf.add(owner + "@pre"); } 
      else 
      { rf.add(owner + ""); } 
    } 
    Vector pars = new Vector(); 
    if (usecase != null) 
    { pars.addAll(usecase.getParameters()); } 

    System.out.println(""); 
    System.out.println(">>> WRITE FRAME of constraint " + this + " = " + wf); 
    System.out.println(">>> READ FRAME of constraint " + this + " = " + rf); 
    System.out.println(""); 

    { Vector intersect = new Vector(); 
      intersect.addAll(rf); 
      intersect.retainAll(wf); 
     
      if (intersect.size() > 0)  // type 2 or 3 constraint
      { constraintType = 2; 
        inv.setConstraintKind(2); 
        if (wf.contains(owner + "") && rf.contains(owner + ""))
        { constraintType = 3; 
          inv.setConstraintKind(3); 
          System.out.println("Type 3 constraint: writes and reads " + 
                              intersect); 
          JOptionPane.showMessageDialog(null, "Constraint " + inv + " is of type 3", 
                           "Constraint analysis",
                           JOptionPane.INFORMATION_MESSAGE);   
        } 
        else 
        { Vector anterf = inv.anteReadFrame(); 
          Vector intersect2 = new Vector(); 
          intersect2.addAll(anterf); 
          intersect2.retainAll(wf); 
          if (intersect2.size() > 0)
          { constraintType = 3; 
            inv.setConstraintKind(3); 
            System.out.println("Type 3 constraint: writes and reads " + 
                             intersect); 
            JOptionPane.showMessageDialog(null, "Constraint " + inv + " is of type 3", 
                            "Constraint analysis",
                            JOptionPane.INFORMATION_MESSAGE);   
          }
          else 
          { System.out.println("Type 2 constraint: writes and reads " + 
                             intersect); 
            JOptionPane.showMessageDialog(null, "Constraint " + inv + " is of type 2", 
                             "Constraint analysis",
                             JOptionPane.INFORMATION_MESSAGE);  
          }   
        } 
      }
      else 
      { System.out.println("Type 1 constraint (Write and read frames are disjoint)"); 
        /* JOptionPane.showMessageDialog(null, "Constraint " + inv + " is of type 1", 
                             "Constraint analysis",
                             JOptionPane.INFORMATION_MESSAGE);  */ 
        constraintType = 1; 
        inv.setConstraintKind(1); 
      } 
    } 

    if (owner == null) 
    { constraintType = 0;
      inv.setConstraintKind(0);   
      System.out.println("Type 0 constraint"); 
      /* JOptionPane.showMessageDialog(null, "Constraint " + inv + " is of type 0", 
                             "Constraint analysis",
                             JOptionPane.INFORMATION_MESSAGE); */   
    } 

    Vector lvars = new Vector(); 
    Vector qvars = inv.secondaryVariables(lvars,pars);  
    // System.out.println("LET variables = " + lvars); 
    // System.out.println("QUANTIFIED variables = " + qvars); 

    Vector res = allPreTerms(); 
    // System.out.println("All pre-terms: " + res); 
  }   // no analysis of groups? 

  public String mapToCheckOp(String language, UseCase usecase, int num, Vector types, Vector entities)
  { // if (owner == null) 
    // { return null; }   // return ""; 
    this.usecase = usecase;
    String ucname = ""; 
    if (usecase != null) 
    { ucname = usecase.getName(); } 
 
    BasicExpression betrue = new BasicExpression(true); 

    Expression ante = antecedent();
    Expression sc = succedent(); 
    Expression body; 
    Vector qvars = secondaryVars; 
    Vector allvars = new Vector(); 
    Vector pars = new Vector(); 
    pars.addAll(usecase.getParameters()); 
    Vector pars1 = ModelElement.getNames(pars); 

    if (ante == null) 
    { body = sc; } 
    else if ("true".equals(ante + ""))
    { body = sc; } 
    else if (qvars.size() > 0)
    { Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
      Vector qvars1 = new Vector(); 
      Vector lvars1 = new Vector(); 
      Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
      // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 

      // System.out.println("Variable quantifiers: " + ante1); 
      // System.out.println("Assumptions: " + ante2);
      if (ante2 == null || "true".equals(ante2 + ""))
      { body = sc; } 
      else 
      { body = new BinaryExpression("=>",ante2,sc); } 

      Vector parms = new Vector(); 
      Vector loopindexes = new Vector(); 
      String callpars = ""; 

      int parcount = 0; 

      for (int i = 0; i < variables.size(); i++)
      { Attribute var = (Attribute) variables.get(i); 
        String vname = var + ""; 
        if (qvars.contains(vname) && !parms.contains(vname))
        { parms.add(vname); 
          Expression qrange = (Expression) variableRanges.get(vname);  
          Type qetype = qrange.getElementType(); 
          var.setType(qetype);
          if (qetype != null) 
          { var.setElementType(qetype.getElementType()); } 
 
          // System.out.println("Quantified variable " + var + 
          //                    " type = " + var.getType());
          Expression qvar = new BasicExpression(vname); 
          qvar.setType(qetype); 
           
          body = 
            new BinaryExpression("!",new BinaryExpression(":",qvar,qrange),body); 
        } 
        else 
        { System.err.println("Unquantified variable, not permitted in precondition: " + var); } 
      } 
    } 
    else 
    { body = new BinaryExpression("=>",ante,sc); } 

    Expression exp; 
    Vector contexts = new Vector(); 

    if (owner != null) 
    { Expression ee = new BasicExpression(owner.getName()); 
      ee.umlkind = Expression.CLASSID; 
      contexts.add(owner); 

      Type et = new Type(owner); 
      ee.type = new Type("Set",null); 
      ee.elementType = et; 

      exp = new BinaryExpression("->forAll",ee,body); 
    } 
    else 
    { exp = body; } 

    java.util.Map env = new java.util.HashMap(); 
    exp.typeCheck(types,entities,contexts,pars); // + parameters of use case

    // System.out.println("Assumption " + num + " is: " + exp); 
    
    // query form of owner->forAll(ante => sc)
    String q = exp.queryForm(language,env,false); 
    if ("Java4".equals(language) || "Java6".equals(language) || "Java7".equals(language))
    { return "  if (" + q + ")\n" + 
           "  { System.out.println(\"Use case " + ucname + " assumption " + num + " is true\"); }\n" +
           "  else\n" + 
           "  { System.out.println(\"Use case " + ucname + " assumption " + num + " is false\"); }\n";
    } 
    else if ("CSharp".equals(language))
    { return "  if (" + q + ")\n" + 
           "  { Console.WriteLine(\"Assumption " + num + " is true on source model\"); }\n" +
           "  else\n" + 
           "  { Console.WriteLine(\"Assumption " + num + " is false on source model\"); }\n";
    } 
    else
    { return " if (" + q + ")\n" + 
           "  { cout << \"Assumption \" << " + num + " << \" is true\" << endl; }\n" +
           "  else\n" + 
           "  { cout << \"Assumption \" << " + num + " << \" is false\" << endl; }\n";
    }  
  } // Also cases of implicit quantifiers, qvars.size() > 0

  public Statement mapToDesign0(UseCase usecase)
  { this.usecase = usecase; 
    constraintKind = 0; 
    Expression sc = succedent(); 
    Statement res = null; 
    Vector pars = new Vector(); 
    pars.addAll(usecase.getParameters()); 
    Vector pars1 = ModelElement.getNames(pars); 

    // System.out.println("Type 0 constraint: " + this); 

    // It becomes code directly in the use case operation in the Controller.
 
    Vector actualvars = new Vector(); 
    for (int i = 0; i < variables.size(); i++) 
    { Attribute av = (Attribute) variables.get(i); 
      if (av == null || av.getName().equals("self")) { } 
      else 
      { actualvars.add(av); } 
    } 

    Vector qvars = secondaryVars; 

    boolean confl0 = confluenceCheck0(); 
    boolean confl = sc.confluenceCheck(new Vector(), new Vector()); 
    if (confl && confl0) 
    { System.out.println(">>> Constraint " + this + " satisfies confluence check"); 
      // JOptionPane.showMessageDialog(null, "Constraint " + this + "\n satisfies syntactic confluence check.", 
      //                       "Confluent type 0 constraint",
      //                       JOptionPane.INFORMATION_MESSAGE);  
    } 
    else 
    { System.out.println(">>> Possible confluence error in " + this); 
      // JOptionPane.showMessageDialog(null, "Warning: constraint\n" + this + "\n may not be confluent", 
      //                        "Possible confluence error in constraint",
      //                        JOptionPane.WARNING_MESSAGE);  
    } 


    Expression ante = antecedent();
    Expression betrue = new BasicExpression(true); 
    Expression exp; 

    Vector parms = new Vector(); 
    Vector outerparms = new Vector(); 
    String callpars = ""; 
    String outercallpars = ""; 
    int parcount = 0; 
    int outerparcount = 0; 

    /* JOptionPane.showMessageDialog(null,"Variables: " + actualvars + 
             " QVARS: " + qvars + " LVARS: " + letVars, "", JOptionPane.WARNING_MESSAGE);
    */ 

    Vector allvars = new Vector(); 
    Vector allpreds = new Vector(); 
    Vector qvars1 = new Vector(); 
    Vector lvars1 = new Vector(); 

    if (actualvars.size() > 0) // (qvars.size() > 0 || letVars.size() > 0)
    { Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
      Vector splitante = ante.splitToCond0Cond1Pred(v0,pars1,qvars1,lvars1,allvars,allpreds); 
      // System.out.println("Variables: " + actualvars + " " + qvars1 + " " + lvars1 + " " + allvars); 

     /* JOptionPane.showMessageDialog(null,"Variables: " + actualvars + 
             " QVARS: " + qvars1 + " LVARS: " + lvars1 + "\n " + 
             allvars + " " + allpreds, "", JOptionPane.WARNING_MESSAGE);
       */ 

      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 

      System.out.println("Variable quantifiers: " + ante1); 
      System.out.println("Assumptions: " + ante2);
      // if (ante2 == null || "true".equals(ante2 + ""))
      { res = new ImplicitInvocationStatement(sc); } 
      // else 
      // { res = new ImplicitInvocationStatement(new BinaryExpression("=>",ante2,sc)); } 
    } 
    else 
    { if ("true".equals(ante + "") || ante == null)
      { res = new ImplicitInvocationStatement(sc); } 
      else 
      { exp = new BinaryExpression("=>",ante,sc); 
        res = new ImplicitInvocationStatement(exp); 
      }
      return res; 
    } 
     
    Statement forloop = q2LoopsPred(allvars,qvars1,lvars1,res); 

    return forloop; 
  }

  public Statement mapToDesign1(UseCase usecase, Vector types, Vector entities, Vector env)
  { if (owner == null || usecase == null) 
    { return null; } 
    this.usecase = usecase;

    Vector contexts = new Vector();  
    if (owner != null) 
    { contexts.add(owner); }
 
    constraintKind = 1; 
    Expression sc = succedent(); 

    Vector actualvars = new Vector(); 
    for (int i = 0; i < variables.size(); i++) 
    { Attribute av = (Attribute) variables.get(i); 
      if (av == null || av.getName().equals("self") || 
          owner.hasFeature(av.getName())) 
      { } 
      else 
      { actualvars.add(av); } 
    } 

    Vector pars = new Vector(); 
    pars.addAll(usecase.getParameters()); 
    Vector pars1 = ModelElement.getNames(pars); 

    BasicExpression betrue = new BasicExpression(true); 

    BasicExpression selfexp = new BasicExpression("self"); 
    selfexp.setEntity(owner); 
    selfexp.setType(new Type(owner)); 
    selfexp.setElementType(new Type(owner)); 
    Vector iterated = new Vector(); 
    iterated.add(selfexp); 

    System.out.println(); 
    System.out.println(">>> Checking confluence of " + this); 

    // analyse confluence conditions:
    boolean confl0 = confluenceCheck0(); 
    boolean confl = sc.confluenceCheck(iterated, new Vector()); 
    if (confl && confl0) 
    { System.out.println(">>> Constraint " + this + "\n satisfies confluence check"); 
      // JOptionPane.showMessageDialog(null, "Constraint " + this + "\n satisfies syntactic confluence check.", 
      //                        "Confluent type 1 constraint",
      //                        JOptionPane.INFORMATION_MESSAGE);  
    } 
    else 
    { System.out.println(">>> Possible confluence error in " + this); 
      // JOptionPane.showMessageDialog(null, "Warning: constraint\n" + this + "\n may not be confluent", 
      //                        "Possible confluence error in constraint",
      //                        JOptionPane.WARNING_MESSAGE);  
    } 

    Vector qvars = secondaryVars; 

    String opname = 
      usecase.getName().toLowerCase() + id; 
    String ename = owner.getName(); 
    SequenceStatement orderEnt = null; 
    BasicExpression lve = null; 

    if (ordered) // use ename->sortBy(orderedBy) instead of ename
    { String sortedE = ename + "sorted" + id; 
      lve = new BasicExpression(sortedE);
      Type seqt = new Type("Sequence",null); 
      seqt.setElementType(new Type(owner)); 
      lve.setType(seqt); 
      lve.setMultiplicity(ModelElement.MANY); 
      lve.setUmlKind(Expression.VARIABLE); 
      lve.setElementType(new Type(owner));
      lve.setEntity(owner); 
      BasicExpression ebe = new BasicExpression(ename); 
      Type settyp = new Type("Set",null); 
      settyp.setElementType(new Type(owner)); 
      ebe.setType(settyp); 
      ebe.setMultiplicity(ModelElement.MANY); 
      ebe.setUmlKind(Expression.CLASSID);
      ebe.setEntity(owner);  
      ebe.setElementType(new Type(owner));  

      Expression ldef = new BinaryExpression("->sortedBy",ebe,orderedBy);  
      CreationStatement linit = new CreationStatement("Sequence",sortedE); 
      Type creseqtype = new Type("Sequence", null); 
      creseqtype.setElementType(new Type(owner)); 
      linit.setInstanceType(creseqtype); 
      linit.setElementType(new Type(owner));
      Statement lassign = new AssignStatement(lve,ldef);
      ename = sortedE;  
      orderEnt = new SequenceStatement(); 
      orderEnt.addStatement(linit); 
      orderEnt.addStatement(lassign); 
    } 

    Type voidtype = new Type("void", null); 
    BehaviouralFeature bf = 
      new BehaviouralFeature(opname, new Vector(), false, voidtype); 
    // bf.setResultType(new Type("void", null)); 
    bf.setElementType(new Type("void", null)); 
    bf.setBx(usecase.isBx()); 
    bf.setPre(betrue); 
    Expression ante = antecedent();
    bf.setDerived(true);  
    bf.addStereotype("explicit"); 
    BehaviouralFeature bfouter = null; // used for 2nd-ary vars
    String bfoutercall = ""; 

    Vector parms = new Vector(); 
    Vector outerparms = new Vector(); 
    String callpars = ""; 
    String outercallpars = ""; 
    int parcount = 0; 
    int outerparcount = 0; 

    // System.out.println("ALL Variables: " + actualvars + " QVARS: " + qvars + " LVARS: " + letVars); 
    Vector allvars = new Vector(); 
    Vector allpreds = new Vector(); 

    if (actualvars.size() > 0) // (qvars.size() > 0 || letVars.size() > 0)
    { Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
      Vector qvars1 = new Vector(); 
      Vector lvars1 = new Vector(); 
      Vector splitante = ante.splitToCond0Cond1Pred(v0,pars1,qvars1,lvars1,allvars,allpreds); 
      // System.out.println("ALL Variables: " + actualvars + " " + qvars1 + " " + lvars1 + " " + allvars); 
       
      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 

      // System.out.println("Variable quantifiers: " + ante1); 
      // System.out.println("Assumptions: " + ante2);
      // if (ante2 == null || "true".equals(ante2 + ""))
      { bf.setPost(sc); } 
      // else 
      // { bf.setPost(new BinaryExpression("=>",ante2,sc)); } 
      // each variable becomes a parameter of bf, and a new operation iterating
      // bf over these variables is needed

      Vector loopindexes = new Vector(); 


      for (int i = 0; i < actualvars.size(); i++)
      { Attribute var = (Attribute) actualvars.get(i); 
        String vname = var + ""; 
        if (qvars.contains(vname))
        { parms.add(var); 
          Expression rnge = (Expression) variableRanges.get(vname); 
          Type rantype = rnge.getElementType(); 
          var.setType(rantype); 
          if (rantype != null) 
          { var.setElementType(rantype.getElementType()); } 
          // System.out.println("Variable " + var + " type = " + var.getType()); 

          loopindexes.add(vname);
          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else if (letVars.contains(vname))
        { parms.add(var); 
          Expression varld = (Expression) letDefinitions.get(vname); 
          var.setType(varld.getType()); 
          var.setElementType(varld.getElementType()); 
          // System.out.println("Let variable " + var + 
          //                    " type = " + 
          //                    var.getType() + "(" + var.getElementType() + ")"); 

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else 
        { System.err.println("***** Additional parameter variable: " + var + " of type " + var.getType() + " " + var.getElementType()); 

          Attribute varatt = (Attribute) ModelElement.lookupByName(var + "", env); 
          if (varatt != null) 
          { var.setType(varatt.getType()); 
            var.setElementType(varatt.getElementType()); 
          }  
          parms.add(var); 
          outerparms.add(var);

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }

          if (outerparcount > 0)
          { outercallpars = outercallpars + "," + vname; 
            outerparcount++; 
          } 
          else 
          { outercallpars = vname; 
            outerparcount = 1; 
          }
        } 
      } 
        
      bf.setParameters(parms);  // only those in qvars
      BasicExpression invoke = 
        new BasicExpression("self." + opname + "(" + callpars + ")",0); 
      Expression ef = invoke.checkIfSetExpression(); 
      ef.setEntity(owner); 
      ef.setUmlKind(Expression.UPDATEOP); 
      // ef.setType(new Type("void", null)); 

      InvocationStatement call = 
        new InvocationStatement("self." + opname + "(" + callpars + ")",null,null); 
      call.setCallExp(ef); // the parameters don't have types here
      Statement forloop = q2LoopsPred(allvars,loopindexes,letVars,call); 
      bfouter = 
        new BehaviouralFeature(opname + "outer", new Vector(), false, voidtype); 
      bfouter.setElementType(new Type("void", null)); 
      bfouter.setPre(betrue); 
      bfouter.setPost(betrue); 
      bfouter.setActivity(forloop);
      bfouter.setDerived(true);  
      bfouter.setBx(usecase.isBx()); 
      bfouter.setParameters(outerparms); 
      // bfouter.addStereotype("explicit"); 
      bfoutercall = ename + "." + opname + "outer(" + outercallpars + ")";
    } 
    else // actualvars.size() == 0
    { if (ante == null) 
      { bf.setPost(sc); } 
      else if ("true".equals(ante + ""))
      { bf.setPost(sc); }
      else  
      { bf.setPost(new BinaryExpression("=>",ante,sc)); } 
    }
    // type check it
 
    owner.addOperation(bf); 
    bf.setEntity(owner); 
    bf.typeCheck(types,entities,contexts,env); 
          
    if (bfouter != null) 
    { bfouter.setEntity(owner); 
      owner.addOperation(bfouter); 
      bfouter.typeCheck(types,entities,contexts,env); 

      BasicExpression invokeouter = 
        new BasicExpression(bfoutercall,0); 
      Expression efo = invokeouter.checkIfSetExpression(); 
      efo.setEntity(owner); // ??
      efo.setUmlKind(Expression.UPDATEOP); 

      Statement invokeouterop = 
        new InvocationStatement(bfoutercall,null,null); 
      ((InvocationStatement) invokeouterop).setCallExp(efo); 

      Expression invokeouterrange = new BasicExpression(owner); 
      invokeouterop = WhileStatement.createInvocationLoop((BasicExpression) efo, invokeouterrange); 
      
      if (orderEnt != null) 
      { ((BasicExpression) efo).setObjectRef(lve); 
        invokeouterrange = lve; 
        invokeouterop = WhileStatement.createInvocationLoop((BasicExpression) efo, invokeouterrange); 
        orderEnt.addStatement(invokeouterop); 
        return orderEnt; 
      } 
      return invokeouterop; 
    } 
     
    BasicExpression invoke = 
      new BasicExpression(ename + "." + opname + "()",0); 
    Expression ef = invoke.checkIfSetExpression(); 
    ef.setEntity(owner); // ? 
    ef.setUmlKind(Expression.UPDATEOP); 

    BasicExpression bfcall = 
        new BasicExpression("self." + opname + "(" + callpars + ")",0); 
    Expression bfcalle = bfcall.checkIfSetExpression(); 
    bfcalle.setEntity(owner); 
    bfcalle.setUmlKind(Expression.UPDATEOP); 
    Expression erange = new BasicExpression(owner); 
    Statement invokeloop = WhileStatement.createInvocationLoop((BasicExpression) bfcalle, erange); 

    InvocationStatement invokeop = 
      new InvocationStatement(ename + "." + opname + "()",null,null); 
    invokeop.setCallExp(ef); 
    invokeop.setEntity(owner); 

    if (orderEnt != null) 
    { // ((BasicExpression) ef).setObjectRef(lve); 
      // orderEnt.addStatement(invokeop); 
      invokeloop = WhileStatement.createInvocationLoop((BasicExpression) bfcalle, lve); 
      orderEnt.addStatement(invokeloop); 
      return orderEnt; 
    } 
    
    return invokeloop; // invokeop;  
  }

  public Statement quantifiersToLoops(Vector vars, Statement call)
  { if (vars.size() == 0) 
    { return call; } 

    String v = (String) vars.get(0); 
    BasicExpression vbe = new BasicExpression(v); 

    Expression range = (Expression) variableRanges.get(v); 
    vbe.setType(range.getElementType()); 
    vbe.setUmlKind(Expression.VARIABLE); 
    vars.remove(v); 
    Statement innerbody = quantifiersToLoops(vars,call); 

    Expression test1 = new BinaryExpression(":",vbe,range); 
    WhileStatement fs = new WhileStatement(test1,innerbody); 
    fs.setLoopKind(Statement.FOR);
    fs.setLoopRange(vbe,range); 
    // System.out.println("????? FOR loop " + v + " : " + range + " " + range.getElementType()); 
    return fs; 
  } 

  public Statement quantifiersToLoops(Vector vars, Vector lvars, Statement call)
  { if (vars.size() == 0) 
    { if (lvars.size() == 0) 
      { return call; } 
      else
      { SequenceStatement ldefs = new SequenceStatement(); 
        for (int i = 0; i < lvars.size(); i++) 
        { String lv = (String) lvars.get(i); 
          Expression lve = new BasicExpression(lv);
          Expression ldef = (Expression) letDefinitions.get(lv);  
          lve.setType(ldef.getType()); 
          lve.setElementType(ldef.getElementType()); 
          lve.setUmlKind(Expression.VARIABLE); 
          CreationStatement linit = new CreationStatement(ldef.getType() + "",lv); 
          linit.setInstanceType(ldef.getType()); 
          linit.setElementType(ldef.getElementType()); 
          AssignStatement lassign = new AssignStatement(lve,ldef); 
          lassign.setCopyValue(true); 
          ldefs.addStatement(linit); 
          lassign.setEntity(owner); 
          ldefs.addStatement(lassign); 
        } 
        ldefs.addStatement(call); 
        ldefs.setEntity(owner); 
        return ldefs; 
      } 
    } 
    

    String v = (String) vars.get(0); 
    BasicExpression vbe = new BasicExpression(v); 

    Expression range = (Expression) variableRanges.get(v); 
    vbe.setType(range.getElementType()); 
    vbe.setUmlKind(Expression.VARIABLE); 
    vars.remove(v); 
    Statement innerbody = quantifiersToLoops(vars,lvars,call); 

    Expression test1 = new BinaryExpression(":",vbe,range); 
    WhileStatement fs = new WhileStatement(test1,innerbody); 
    fs.setLoopKind(Statement.FOR);
    fs.setLoopRange(vbe,range); 
    // System.out.println("FOR loop " + v + " : " + range + " " + range.getElementType()); 
    return fs; 
  } 

  public Statement q2Loops(Vector allvars, Vector qvars, Vector lvars, Statement stat)
  { if (allvars.size() == 0)
    { return stat; } 

    /* if (qvars.size() == 0) 
    { if (lvars.size() == 0) 
      { return stat; } 
      else
      { SequenceStatement ldefs = new SequenceStatement(); 
        for (int i = 0; i < lvars.size(); i++) 
        { String lv = (String) lvars.get(i); 
          Expression lve = new BasicExpression(lv);
          Expression ldef = (Expression) letDefinitions.get(lv);  
          lve.setType(ldef.getType()); 
          lve.setElementType(ldef.getElementType()); 
          lve.setUmlKind(Expression.VARIABLE); 
          CreationStatement linit = new CreationStatement(ldef.getType() + "",lv); 
          AssignStatement lassign = new AssignStatement(lve,ldef); 
          ldefs.addStatement(linit); 
          lassign.setEntity(owner); 
          ldefs.addStatement(lassign); 
        } 
        ldefs.addStatement(stat); 
        ldefs.setEntity(owner); 
        return ldefs; 
      } 
    } */ 
    
    String v = (String) allvars.get(0); 
    Vector av2 = new Vector(); 
    av2.addAll(allvars); 
    av2.remove(0); 
    Statement innerbody = q2Loops(av2,qvars,lvars,stat); 

    BasicExpression vbe = new BasicExpression(v); 
    vbe.setUmlKind(Expression.VARIABLE); 
      
    if (qvars.contains(v))
    { Expression range = (Expression) variableRanges.get(v); 
      if (range == null) { return innerbody; } 
      Type rantype = range.getElementType(); 
      vbe.setType(rantype);
      if (rantype != null) 
      { vbe.setElementType(rantype.getElementType()); } 
 
    
      Expression test1 = new BinaryExpression(":",vbe,range); 
      test1.setType(new Type("boolean", null)); 
      test1.setElementType(new Type("boolean", null)); 

      WhileStatement fs = new WhileStatement(test1,innerbody); 
      fs.setLoopKind(Statement.FOR);
      fs.setLoopRange(vbe,range); 
      // System.out.println("FOR loop " + v + " : " + range + " " + range.getElementType()); 
      return fs; 
    } 
    else if (lvars.contains(v))
    { SequenceStatement ldefs = new SequenceStatement(); 
      Expression ldef = (Expression) letDefinitions.get(v);  
      if (ldef == null) { return innerbody; } 
      vbe.setType(ldef.getType()); 
      vbe.setElementType(ldef.getElementType()); 
      if (ldef.getType().isEntity())  // don't want to create it, just assign
      { AssignStatement lassign1 = new AssignStatement(vbe,ldef);
        lassign1.setType(ldef.getType());  
        lassign1.setEntity(owner); 
        ldefs.addStatement(lassign1);
        // System.out.println("Assignment for LET: " + lassign1); 
      } 
      else // collections, primitive types  
      { CreationStatement linit = new CreationStatement(ldef.getType() + "",v); 
        linit.setInstanceType(ldef.getType()); 
        linit.setElementType(ldef.getElementType()); 
        AssignStatement lassign = new AssignStatement(vbe,ldef); 
        lassign.setElementType(ldef.getElementType()); 
        lassign.setCopyValue(true); 
        ldefs.addStatement(linit); 
        lassign.setEntity(owner); 
        ldefs.addStatement(lassign); 
      } 
      ldefs.addStatement(innerbody); // but could itself be a sequence statement
      ldefs.setEntity(owner); 
      // System.out.println("LET definition " + v + " = " + ldef + " " + ldef.getElementType()); 

      return ldefs; 
    } 

    return innerbody; // condition case: if v then innerbody else skip
  } 

  public Statement q2Loops(Vector allvars, Vector qvars, Vector lvars, Statement stat, 
                           BasicExpression varbe, Type ownertype)
  { if (allvars.size() == 0)
    { return stat; } 
    
    String v = (String) allvars.get(0); 
    Vector av2 = new Vector(); 
    av2.addAll(allvars); 
    av2.remove(0); 
    Statement innerbody = q2Loops(av2,qvars,lvars,stat,varbe,ownertype); 

    BasicExpression vbe = new BasicExpression(v); 
    vbe.setUmlKind(Expression.VARIABLE); 
      
    if (qvars.contains(v))
    { Expression range = (Expression) variableRanges.get(v); 
      if (range == null) { return innerbody; } 
      Type rantype = range.getElementType(); 
      vbe.setType(rantype);
      if (rantype != null) 
      { vbe.setElementType(rantype.getElementType()); } 
 
      Expression vrange = range.addReference(varbe,ownertype); 
    
      Expression test1 = new BinaryExpression(":",vbe,vrange); 
      test1.setType(new Type("boolean", null)); 
      test1.setElementType(new Type("boolean", null)); 

      WhileStatement fs = new WhileStatement(test1,innerbody); 
      fs.setLoopKind(Statement.FOR);
      fs.setLoopRange(vbe,vrange); 
      // System.out.println("FOR loop " + v + " : " + vrange + " " + vrange.getElementType()); 
      return fs; 
    } 
    else if (lvars.contains(v))
    { SequenceStatement ldefs = new SequenceStatement(); 
      Expression ldef = (Expression) letDefinitions.get(v);  
      if (ldef == null) { return innerbody; } 
      vbe.setType(ldef.getType()); 
      vbe.setElementType(ldef.getElementType()); 
      Expression vldef = ldef.addReference(varbe,ownertype); 

      if (ldef.getType().isEntity())  // don't want to create it, just assign
      { AssignStatement lassign1 = new AssignStatement(vbe,vldef);
        lassign1.setType(ldef.getType());  
        lassign1.setEntity(owner); 
        ldefs.addStatement(lassign1);
        // System.out.println("Assignment for LET: " + lassign1); 
      } 
      else // collections, primitive types  
      { CreationStatement linit = new CreationStatement(ldef.getType() + "",v); 
        linit.setInstanceType(ldef.getType()); 
        linit.setElementType(ldef.getElementType()); 
        AssignStatement lassign = new AssignStatement(vbe,vldef); 
        lassign.setElementType(ldef.getElementType()); 
        lassign.setCopyValue(true); 
        ldefs.addStatement(linit); 
        lassign.setEntity(owner); 
        ldefs.addStatement(lassign); 
      } 
      ldefs.addStatement(innerbody); // but could itself be a sequence statement
      ldefs.setEntity(owner); 
      // System.out.println("LET definition " + v + " = " + vldef + " " + vldef.getElementType()); 

      return ldefs; 
    } 

    return innerbody; // condition case: if v then innerbody else skip
  } 

  public Statement q2LoopsPred(Vector allvars, Vector qvars, Vector lvars, Statement stat)
  { if (allvars.size() == 0)
    { return stat; } 
    
    Object obj = allvars.get(0); 
    Vector av2 = new Vector(); 
    av2.addAll(allvars); 
    av2.remove(0); 
    Statement innerbody = q2LoopsPred(av2,qvars,lvars,stat); 

    if (obj instanceof String) 
    { String v = (String) obj; 
      BasicExpression vbe = new BasicExpression(v); 
      vbe.setUmlKind(Expression.VARIABLE); 
      
      if (qvars.contains(v))
      { Expression range = (Expression) variableRanges.get(v); 
        if (range == null) { return innerbody; } 
        Type rantype = range.getElementType(); 
        vbe.setType(rantype);
        if (rantype != null) 
        { vbe.setElementType(rantype.getElementType()); } 
        if (rantype != null && Type.isEntityType(rantype))
        { vbe.setEntity(rantype.getEntity()); } 
        else 
        { vbe.setEntity(range.entity); } 
        
        Expression test1 = new BinaryExpression(":",vbe,range); 
        test1.setType(new Type("boolean", null)); 
        test1.setElementType(new Type("boolean", null)); 

        WhileStatement fs = new WhileStatement(test1,innerbody); 
        fs.setLoopKind(Statement.FOR);
        fs.setLoopRange(vbe,range); 
        // setEntity? 
        // System.out.println("??? FOR loop " + vbe + " : " + range + " Entity: " +
        //                    vbe.entity + " range element type: " + range.getElementType()); 
        return fs; 
      } 
      else if (lvars.contains(v))
      { SequenceStatement ldefs = new SequenceStatement(); 
        Expression ldef = (Expression) letDefinitions.get(v);  
        if (ldef == null) { return innerbody; } 
        vbe.setType(ldef.getType()); 
        vbe.setElementType(ldef.getElementType()); 
		if (ldef.getType() == null)
		{ System.err.println("!! ERROR: no type for " + ldef); 
		  AssignStatement lassign0 = new AssignStatement(vbe,ldef);
          lassign0.setType(ldef.getType());  
          lassign0.setElementType(ldef.getElementType()); 
          lassign0.setEntity(owner); 
          ldefs.addStatement(lassign0);
		}
        else if (ldef.getType().isEntity())  // don't want to create it, just assign
        { AssignStatement lassign1 = new AssignStatement(vbe,ldef);
          lassign1.setType(ldef.getType());  
          lassign1.setElementType(ldef.getElementType()); 
          lassign1.setEntity(owner); 
          ldefs.addStatement(lassign1);
          // System.out.println("Assignment for LET: " + lassign1); 
        } 
        else // collections, primitive types  
        { CreationStatement linit = new CreationStatement(ldef.getType() + "",v); 
          linit.setInstanceType(ldef.getType()); 
          linit.setElementType(ldef.getElementType()); 
          AssignStatement lassign = new AssignStatement(vbe,ldef); 
          lassign.setCopyValue(true); 
          lassign.setElementType(ldef.getElementType()); 
          ldefs.addStatement(linit); 
          lassign.setEntity(owner); 
          ldefs.addStatement(lassign); 
        } 
        ldefs.addStatement(innerbody); // but could itself be a sequence statement
        ldefs.setEntity(owner); 
        // System.out.println("LET definition " + v + " = " + ldef + " " + ldef.getElementType()); 

        return ldefs; 
      }
    } 
    else if (obj instanceof Expression) 
    { ConditionalStatement ifstat = new ConditionalStatement((Expression) obj, innerbody); 
      return ifstat; 
    }   

    return innerbody; // condition case: if v then innerbody else skip
  } 


  public Statement mapToDesign2(UseCase usecase, String optimise, Vector types, Vector entities)
  { return mapToDesign2(usecase,optimise,null,types,entities); } 

  public Statement mapToDesign2(UseCase usecase, String optimise,   
                                SequenceStatement oldstat, Vector types, Vector entities)
  { if (owner == null) 
    { System.err.println("!!! ERROR: null owner for constraint " + this); 
      return null; 
    } 
    this.usecase = usecase; 
    constraintKind = 2; 

    System.out.println(">>> Type 2 constraint, confluence must be shown using a variant");  

    Type booltype = new Type("boolean",null); 
    Type ownertype = new Type(owner); 
    
    Vector actualvars = new Vector(); 
    for (int i = 0; i < variables.size(); i++) 
    { Attribute av = (Attribute) variables.get(i); 
      if (av == null || av.getName().equals("self") || owner.hasFeature(av.getName())) { } 
      else 
      { actualvars.add(av); } 
    } 
    Vector pars = new Vector(); 
    pars.addAll(usecase.getParameters()); 
    Vector pars1 = ModelElement.getNames(pars); 

    String opname = 
      usecase.getName().toLowerCase() + id; 
    String optestname = opname + "test"; 
    BasicExpression betrue = new BasicExpression(true); 
    betrue.setType(booltype); 
    BasicExpression befalse = new BasicExpression(false); 
    befalse.setType(booltype); 

    Expression nac = succedent().computeNegation4succ(); 
    // Negative application condition
    Expression nac1 = nac.removePrestate(); 

    Type voidtype = new Type("void", null); 
    BehaviouralFeature bf = 
      new BehaviouralFeature(opname, new Vector(), false, voidtype);
    bf.setElementType(new Type("void", null)); 
    bf.setPre(betrue);  
    bf.setPost(succedent()); 
    bf.setDerived(true);
    bf.addStereotype("explicit"); 
    bf.setBx(usecase.isBx()); 
 
    BehaviouralFeature bftest = 
      new BehaviouralFeature(optestname, new Vector(), true, booltype); 
    bftest.setPost(betrue); 
    bftest.setDerived(true); 
    bftest.setBx(usecase.isBx()); 

    Statement rettrue = new ReturnStatement(betrue); 
    Statement retfalse = new ReturnStatement(befalse); 
    Statement innerif = 
          new IfStatement(succedent(),retfalse);  // omits update operations in the test
    SequenceStatement sstat0 = new SequenceStatement(); 
    sstat0.addStatement(innerif); 
    sstat0.addStatement(rettrue);  
    innerif.setEntity(owner); 
    sstat0.setEntity(owner); 
    sstat0.setBrackets(true); 

    int parcount = 0; 
    int outerparcount = 0; 
    Vector parms = new Vector(); 
    Vector outerparms = new Vector(); 
    String callpars = ""; 
    String outercallpars = ""; 

    Vector loopindexes = new Vector(); 
    Vector qvars = secondaryVars; 
    Vector allvars = new Vector(); 
        
    Expression ante = antecedent(); 
    
    if (actualvars.size() == 0) // (qvars.size() == 0 && letVars.size() == 0)
    { if (ante == null) 
      { bftest.setActivity(sstat0); 
        // bf.setPre(nac1); 
      } 
      else if ("true".equals(ante + ""))
      { bftest.setActivity(sstat0); 
        // bf.setPre(nac1); 
      } 
      else 
      { SequenceStatement outerss = new SequenceStatement(); 
        Statement outerif; 
        if (ante.conflictsWith(succedent()) || "y".equals(optimise))
        { System.out.println("Omitting test of: " + succedent()); 
          outerif = new IfStatement(ante,rettrue); 
          Expression antex = ante.removePrestate(); 
          bf.setPre(antex); 
        } 
        else 
        { outerif = new IfStatement(ante,sstat0); 
          // bf.setPre(new BinaryExpression("&",ante,nac));  
        } 
 
        outerss.addStatement(outerif); 
        outerss.addStatement(retfalse);       
        outerif.setEntity(owner); 
        outerss.setEntity(owner); 
        bftest.setActivity(outerss);
      }
    } 
    else // (actualvars.size() > 0)
    { Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
      Vector qvars1 = new Vector(); 
      Vector lvars1 = new Vector(); 
      Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
      // use the Pred version

      // System.out.println("Variables: " + variables + " " + qvars1 + " " + lvars1); 
      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 
      // System.out.println("Variables: " + actualvars); 
      // System.out.println("Variable quantifiers: " + ante1); 
      // System.out.println("Assumptions: " + ante2); 
      SequenceStatement outerss = new SequenceStatement(); 
      Statement outerif; 
      if (ante2 == null) 
      { bftest.setActivity(sstat0); 
        // bf.setPre(nac1); 
      } 
      else if ("true".equals(ante2 + ""))
      { bftest.setActivity(sstat0); 
        // bf.setPre(nac1); 
      } 
      else 
      { if (ante2.conflictsWith(succedent()) || "y".equals(optimise))
        { System.out.println("Omitting negative application condition: " +
                             succedent()); 
          outerif = new IfStatement(ante2,rettrue);
          Expression ante2x = ante2.removePrestate(); 
          // bf.setPre(ante2x); 
        } 
        else 
        { outerif = new IfStatement(ante2,sstat0); 
          Expression ante2x = ante2.removePrestate(); 
          // bf.setPre(new BinaryExpression("&",ante2x,nac1)); 
        } 
 
        outerss.addStatement(outerif); 
        outerss.addStatement(retfalse);       
        outerif.setEntity(owner); 
        outerss.setEntity(owner); 
        bftest.setActivity(outerss);
        // bftest.addStereotype("explicit"); 
      }
      // each variable becomes a parameter of bf, and a new operation iterating
      // bf over these variables is needed


      for (int i = 0; i < actualvars.size(); i++)
      { Attribute var = (Attribute) actualvars.get(i); 
        String vname = var + ""; 
        if (qvars.contains(vname))
        { parms.add(var); 
          Expression range = (Expression) variableRanges.get(vname); 
          Type rantype = range.getElementType(); 
          var.setType(rantype);
          if (rantype != null) 
          { var.setElementType(rantype.getElementType()); } 
 
          // System.out.println("Quantified variable " + var + 
          //                    " type = " + var.getType()); 
          loopindexes.add(vname);
          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else if (letVars.contains(vname))
        { parms.add(var); 
          Expression varld = (Expression) letDefinitions.get(vname); 
          var.setType(varld.getType()); 
          var.setElementType(varld.getElementType()); 
          // System.out.println("Let variable " + var + 
          //                    " type = " + 
          //                    var.getType() + "(" + var.getElementType() + ")"); 

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else 
        { // System.err.println("Additional parameter variable: " + var); 
          parms.add(var); 
          outerparms.add(var); 

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }

          if (outerparcount > 0)
          { outercallpars = outercallpars + "," + vname; 
            outerparcount++; 
          } 
          else 
          { outercallpars = vname; 
            outerparcount = 1; 
          }
        } 
      } 

    }         

    // type check it

    bf.setEntity(owner); 
    owner.addOperation(bf);
    bftest.setEntity(owner); 
    owner.addOperation(bftest);  
    bf.setParameters(parms); 
    bftest.setParameters(parms); 
    bf.typeCheck(types,entities); 
    bftest.typeCheck(types,entities); 

    String ename = owner.getName(); 
    String evar = Identifier.nextIdentifier(ename.toLowerCase() + "x"); 
    BasicExpression ownerbe = new BasicExpression(ename); 
    ownerbe.setUmlKind(Expression.CLASSID);
    ownerbe.setElementType(ownertype); 
 
    BasicExpression varbe = new BasicExpression(evar); 
    varbe.setUmlKind(Expression.VARIABLE); 
    varbe.setType(ownertype); 

    SequenceStatement orderEnt = null; 
    BasicExpression lve = null; 

    if (ordered) // use ename->sortBy(orderedBy) instead of ename
    { String sortedE = ename + "sorted" + id; 
      lve = new BasicExpression(sortedE);
      Type seqtype = new Type("Sequence",null);
      seqtype.setElementType(new Type(owner));  
      lve.setType(seqtype); 
      lve.setMultiplicity(ModelElement.MANY); 
      lve.setUmlKind(Expression.VARIABLE); 
      lve.setElementType(new Type(owner));
      lve.setEntity(owner); 
      BasicExpression ebe = new BasicExpression(ename); 
      Type settype = new Type("Set",null); 
      settype.setElementType(new Type(owner)); 
      ebe.setType(settype); 
      ebe.setMultiplicity(ModelElement.MANY); 
      ebe.setUmlKind(Expression.CLASSID);
      ebe.setEntity(owner);  
      ebe.setElementType(new Type(owner));  
      Expression ldef = new BinaryExpression("->sortedBy",ebe,orderedBy);  
      CreationStatement linit = new CreationStatement("Sequence",sortedE); 
      linit.setInstanceType(seqtype); 
      linit.setElementType(new Type(owner));  
      Statement lassign = new AssignStatement(lve,ldef);
      // ename = ename + "sorted";  
      orderEnt = new SequenceStatement(); 
      orderEnt.addStatement(linit); 
      orderEnt.addStatement(lassign); 
      ownerbe = lve; 
    } 

    Expression test1 = new BinaryExpression(":",varbe,ownerbe); 
    test1.setType(booltype); 
    test1.setElementType(booltype); 
 
    BasicExpression testinvoke = 
      new BasicExpression(evar + "." + optestname + "(" + callpars + ")",0); 
    Expression testinvokee = testinvoke.checkIfSetExpression(); 
    testinvokee.setUmlKind(Expression.QUERY); 
    testinvokee.setType(booltype);
    testinvokee.setEntity(owner);  

    Compiler2 c2 = new Compiler2();
    c2.nospacelexicalanalysis(evar + "." + opname + "(" + callpars + ")");  
    Expression opinvokee = c2.parse(); 
    if (opinvokee == null) 
    { System.err.println("Invalid call of " + opname); 
      return null; 
    } 
    opinvokee.setUmlKind(Expression.UPDATEOP); 
    opinvokee.setEntity(owner);  

    InvocationStatement invokeop = 
      new InvocationStatement(evar + "." + opname + "(" + callpars + ")", null,null); 
    invokeop.setCallExp(opinvokee); // opinvokee 

    BasicExpression running = 
      new BasicExpression(opname + "_running"); 
    running.setType(booltype); 

    if (oldstat != null)  // does not work for multiple ordered constraints. 
    { WhileStatement wsold = (WhileStatement) oldstat.getStatement(2); 
      running = (BasicExpression) wsold.getTest(); 
    } 

    CreationStatement cre = new CreationStatement("boolean", running + ""); 
    cre.setInstanceType(new Type("boolean", null)); 
    cre.setElementType(new Type("boolean", null)); 

    Statement init = new AssignStatement(running,betrue); 
    Statement setfalse = new AssignStatement(running,befalse); 

    SequenceStatement sstat1 = new SequenceStatement(); 
    sstat1.addStatement(invokeop); 
    sstat1.addStatement(init); 
    sstat1.setBrackets(true); 

    // Statement skipstat = new InvocationStatement("skip",null,null); 
    IfStatement innerbody = new IfStatement(testinvokee,sstat1); 
    Statement forloop; 
    if (parcount > outerparcount) // (loopindexes.size() > 0) 
    { forloop = q2Loops(allvars,loopindexes,letVars,innerbody);
         // use the Pred version
    } 
    else 
    { forloop = innerbody; } 


    WhileStatement fs = new WhileStatement(test1,forloop); 
    fs.setLoopKind(Statement.FOR);
    fs.setLoopRange(varbe,ownerbe); 
    fs.setEntity(owner); 

    if (oldstat != null)  // does not work for multiple ordered constraints. 
    { WhileStatement wsold = (WhileStatement) oldstat.getStatement(2); 
      SequenceStatement sstat2old = (SequenceStatement) wsold.getBody(); 
      sstat2old.addStatement(fs); 
      return oldstat; 
    } 
  
    SequenceStatement sstat2 = new SequenceStatement();
    sstat2.addStatement(setfalse); 
    sstat2.addStatement(fs);
    sstat2.setBrackets(true); 

    WhileStatement ws = new WhileStatement(running, sstat2); 
    ws.setLoopKind(Statement.WHILE); 

    SequenceStatement sstat3; 
    if (ordered) 
    { sstat3 = orderEnt; } 
    else 
    { sstat3 = new SequenceStatement(); }  
    sstat3.addStatement(cre); 
    sstat3.addStatement(init); 
    sstat3.addStatement(ws); // The code invoked in the controller operation
    // sstat3.setBrackets(true); 
    sstat3.setEntity(owner); 

    return sstat3;  
  }

  public Statement mapToDesign3(UseCase usecase, String optimise, Vector types, Vector entities)
  { return mapToDesign3(usecase,optimise,new Vector(),types,entities); } 

  public Statement mapToDesign3(UseCase usecase, String optimise, Vector innerss, Vector types,
                                Vector entities)
  { if (owner == null) 
    { JOptionPane.showMessageDialog(null, "Error: constraint " + this + " has null owner.\n" + 
                             "Type 3 constraints must have owners.", 
                             "Invalid type 3 constraint",
                             JOptionPane.ERROR_MESSAGE);  
      return null;
    } 

    if (succedent() == null) 
    { JOptionPane.showMessageDialog(null, "Error: constraint " + this + " has null succedent.\n" + 
                             "Constraints must have non-null succedents.", 
                             "Invalid type 3 constraint",
                             JOptionPane.ERROR_MESSAGE);  
      return null; 
    } 

    this.usecase = usecase; 
    constraintKind = 3; 

    System.out.println(">>> Type 3 constraint, confluence must be shown using a variant");  

    Expression nac = succedent().computeNegation4succ(); 
    // Negative application condition
    Expression nac1 = nac.removePrestate(); 

    Type booltype = new Type("boolean",null); 
    Type ownertype = new Type(owner); 
    Vector pars = new Vector(); 
    pars.addAll(usecase.getParameters()); 
    Vector pars1 = ModelElement.getNames(pars); 

    String callpars = ""; 
    String outercallpars = ""; 
    Vector loopindexes = new Vector(); 
    Vector qvars = secondaryVars; 

    String opname = 
      usecase.getName().toLowerCase() + id; 
    String optestname = opname + "test"; 
    BasicExpression betrue = new BasicExpression(true); 
    betrue.setType(booltype); 
    BasicExpression befalse = new BasicExpression(false); 
    befalse.setType(booltype); 
    Expression sc = succedent();   // omit update ops from test

    Type voidtype = new Type("void", null); 
    BehaviouralFeature bf = 
      new BehaviouralFeature(opname, new Vector(), false, voidtype); 
    bf.setElementType(new Type("void", null));    bf.setBx(usecase.isBx()); 
    bf.setPost(sc); 
    bf.setDerived(true); 
    bf.addStereotype("explicit"); 

    BehaviouralFeature bftest = 
      new BehaviouralFeature(optestname, new Vector(), true, booltype); 
    bftest.setPost(betrue); 
    bftest.setDerived(true); 
    bftest.setBx(usecase.isBx()); 
    // bftest.addStereotype("explicit"); 

    Statement rettrue = new ReturnStatement(betrue); 
    Statement retfalse = new ReturnStatement(befalse); 
    Statement innerif = 
          new IfStatement(sc,retfalse);
    SequenceStatement sstat0 = new SequenceStatement(); 
    sstat0.addStatement(innerif); 
    sstat0.addStatement(rettrue);  
    innerif.setEntity(owner); 
    sstat0.setEntity(owner); 
    sstat0.setBrackets(true); 

    int parcount = 0; 
    int outerparcount = 0; 
    Vector outerparms = new Vector(); 
    
    SequenceStatement outerss = new SequenceStatement(); 
    Statement outerif; 
    
    Vector allvars = new Vector(); 
    Vector actualvars = new Vector(); 
    for (int i = 0; i < variables.size(); i++) 
    { Attribute av = (Attribute) variables.get(i); 
      if (av == null || av.getName().equals("self") || owner.hasFeature(av.getName())) { } 
      else 
      { actualvars.add(av); } 
    } 
      
    Expression ante = antecedent();
    Expression antex = null; 
    if (ante != null) 
    { antex = ante.removePrestate(); } 
 
    // if ("y".equals(optimise))
    // { outerif = new IfStatement(ante,rettrue);
    //   outerss.addStatement(outerif); 
    //   outerss.addStatement(retfalse);       
    //   outerif.setEntity(owner); 
    //   outerss.setEntity(owner); 
    //   bftest.setActivity(outerss);
    // } 
    // else 
    if (ante == null || "true".equals(ante + ""))
    { bftest.setActivity(sstat0);
      // bf.setPre(nac1); 
    } 
    else if (actualvars.size() == 0) // (qvars.size() == 0 && letVars.size() == 0)
    { if (ante.conflictsWith(sc) || "y".equals(optimise))
      { System.out.println("!! CONFLICT between: " + ante + " and " + sc + ". I WILL OMIT not(succ) test"); 
        outerif = new IfStatement(ante,rettrue);
        // bf.setPre(antex); 
      } 
      else 
      { outerif = new IfStatement(ante,sstat0); 
        // bf.setPre(new BinaryExpression("&",antex,nac1)); 
      }
      outerss.addStatement(outerif); 
      outerss.addStatement(retfalse);       
      outerif.setEntity(owner); 
      outerss.setEntity(owner); 
      bftest.setActivity(outerss);
    } 
    else // actualvars.size() > 0
    { Vector v0 = new Vector(); 
      v0.add(betrue); 
      v0.add(betrue.clone()); 
      Vector qvars1 = new Vector(); 
      Vector lvars1 = new Vector(); 
      Vector splitante = ante.splitToCond0Cond1(v0,pars1,qvars1,lvars1,allvars); 
      // use the Pred version

      // System.out.println("Variables: " + actualvars + " " + qvars1 + " " + lvars1); 
      Expression ante1 = (Expression) splitante.get(0); 
      Expression ante2 = (Expression) splitante.get(1); 
      Expression ante2x = ante2.removePrestate(); 

      // System.out.println("Variables: " + variables); 
      // System.out.println("Variable quantifiers: " + ante1); 
      // System.out.println("Assumptions: " + ante2); 
      // SequenceStatement outerss = new SequenceStatement(); 
      // Statement outerif; 
      if ("y".equals(optimise))
      { System.out.println("CONFLICT assumed between: " + ante2 + " and " + sc + ". Omitting not(succ) test"); 
        outerif = new IfStatement(ante2,rettrue); 
        outerss.addStatement(outerif); 
        outerss.addStatement(retfalse);       
        outerif.setEntity(owner); 
        outerss.setEntity(owner); 
        bftest.setActivity(outerss);
        // bf.setPre(ante2x); 
      }
      else if (ante2 == null || "true".equals(ante2 + ""))
      { bftest.setActivity(sstat0);
        // bf.setPre(nac1); 
      } 
      else 
      { if (ante2.conflictsWith(succedent()))
        { System.out.println("Conflict between: " + ante2 + " and " + sc); 
          outerif = new IfStatement(ante2,rettrue); 
          // bf.setPre(ante2x); 
        } 
        else 
        { outerif = new IfStatement(ante2,sstat0);
          // bf.setPre(new BinaryExpression("&",ante2x,nac1)); 
        } 
 
        outerss.addStatement(outerif); 
        outerss.addStatement(retfalse);       
        outerif.setEntity(owner); 
        outerss.setEntity(owner); 
        bftest.setActivity(outerss);
      }
      // each variable becomes a parameter of bf, and a new operation iterating
      // bf over these variables is needed

      Vector parms = new Vector(); 

      for (int i = 0; i < actualvars.size(); i++)
      { Attribute var = (Attribute) actualvars.get(i); 
        String vname = var + ""; 
        if (qvars.contains(vname))
        { parms.add(var); 
          Expression range = (Expression) variableRanges.get(vname);
          Type rantype = range.getElementType();  
          var.setType(rantype);
          if (rantype != null) 
          { var.setElementType(rantype.getElementType()); }  
          // System.out.println("Secondary quantifier variable " + var + 
          //                    " type = " + var.getType()); 
          loopindexes.add(vname);

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else if (letVars.contains(vname))
        { parms.add(var); 
          Expression varld = (Expression) letDefinitions.get(vname); 
          var.setType(varld.getType()); 
          var.setElementType(varld.getElementType()); 
          // System.out.println("Let variable " + var + 
          //                    " type = " + 
          //                    var.getType() + "(" + var.getElementType() + ")"); 

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }   
        } 
        else 
        { // System.err.println("Additional parameter variable: " + var); 
          parms.add(var); 
          outerparms.add(var); 

          if (parcount > 0)
          { callpars = callpars + "," + vname; 
            parcount++; 
          } 
          else 
          { callpars = vname; 
            parcount = 1; 
          }

          if (outerparcount > 0)
          { outercallpars = outercallpars + "," + vname; 
            outerparcount++; 
          } 
          else 
          { outercallpars = vname; 
            outerparcount = 1; 
          }
        } 
      } 

      bf.setParameters(parms); 
      bftest.setParameters(parms); 
      pars.addAll(parms); 
    }         

    // type check it

    bf.setEntity(owner); 
    owner.addOperation(bf);  // Must type-check these operations
    bftest.setEntity(owner); 
    owner.addOperation(bftest);  

    bf.typeCheck(types,entities); 
    bftest.typeCheck(types,entities); 


    String ename = owner.getName(); 
    String evar = ename.toLowerCase() + "x"; 
    BasicExpression ownerbe = new BasicExpression(ename); 
    ownerbe.setUmlKind(Expression.CLASSID);
    Type seqtype = new Type("Sequence", null); 
    seqtype.setElementType(ownertype); 
    ownerbe.setType(seqtype); 
    ownerbe.setElementType(ownertype); 
    ownerbe.setEntity(owner); 
    Attribute evaratt = new Attribute(evar, ownertype, ModelElement.INTERNAL); 
    pars.add(evaratt); 
 
    BasicExpression varbe = new BasicExpression(evar); 
    varbe.setUmlKind(Expression.VARIABLE); 
    varbe.setType(ownertype); 
    varbe.setElementType(ownertype); 

    Expression test1 = new BinaryExpression(":",varbe,ownerbe); 
    test1.setType(booltype); 
    test1.setElementType(booltype); 
 
    BasicExpression testinvoke = 
      new BasicExpression(evar + "." + optestname + "(" + callpars + ")",0); 
    Expression testinvokee = testinvoke.checkIfSetExpression(); 
    testinvokee.setUmlKind(Expression.QUERY); 
    testinvokee.setType(booltype);
    testinvokee.setEntity(owner);  

    Vector contexts = new Vector(); 
    contexts.add(owner); 
    testinvokee.typeCheck(types,entities,contexts,pars); 

    Compiler2 c2 = new Compiler2();
    c2.nospacelexicalanalysis(evar + "." + opname + "(" + callpars + ")");  
    Expression opinvokee = c2.parse(); 

    opinvokee.typeCheck(types,entities,contexts,pars); 

    // BasicExpression opinvoke = 
    //   new BasicExpression(evar + "." + opname + "()",0); 
    // Expression opinvokee = opinvoke.checkIfSetExpression(); 
    opinvokee.setUmlKind(Expression.UPDATEOP); 
    opinvokee.setEntity(owner);  

    InvocationStatement invokeop = 
      new InvocationStatement(evar + "." + opname + "(" + callpars + ")", null, null); 
    invokeop.setCallExp(opinvokee); // opinvokee 

    BasicExpression running = 
      new BasicExpression(opname + "_running"); 
    running.setType(booltype); 

    // Statement retfalse = new ReturnStatement(befalse); 

    SequenceStatement sstat1 = new SequenceStatement(); 
    sstat1.addStatement(invokeop); 
    sstat1.addStatement(rettrue); 
    sstat1.setBrackets(true); 

    // IfStatement innerbody = new IfStatement(testinvokee,sstat1); 
    Statement innerbody = new IfStatement(testinvokee, sstat1);

    if (usecase != null && usecase.isBacktracking())
    { BacktrackingSpecification bs = usecase.getBacktrackingSpecification();
      bs.generateDesign2(types,entities); 
      Statement successcase = bs.getSuccessCase(evar);
      Statement backtrackcase = bs.getBacktrackCase(evar);
      innerbody = new SequenceStatement();
      ((SequenceStatement) innerbody).addStatement(new IfStatement(testinvokee, invokeop));
      ((SequenceStatement) innerbody).addStatement(successcase);
      ((SequenceStatement) innerbody).addStatement(backtrackcase);
      ((SequenceStatement) innerbody).addStatement(rettrue); 
    }

    Statement forloop; 
    if (parcount > outerparcount) // (loopindexes.size() > 0) 
    { forloop = q2Loops(allvars,loopindexes,letVars,innerbody,varbe,ownertype); } 
    else 
    { forloop = innerbody; } 
    // use Pred version

    SequenceStatement orderEnt = null; 
    BasicExpression lve = null; 

    if (ordered) // use ename->sortBy(orderedBy) instead of ename
    { String sortedE = ename + "sorted" + id; 
      lve = new BasicExpression(sortedE);
      Type seqtype1 = new Type("Sequence",null); 
      seqtype1.setElementType(new Type(owner));  
      lve.setType(seqtype1); 
      lve.setMultiplicity(ModelElement.MANY); 
      lve.setUmlKind(Expression.VARIABLE); 
      lve.setElementType(new Type(owner));
      lve.setEntity(owner); 

      BasicExpression ebe = new BasicExpression(ename); 
      Type settype1 = new Type("Set",null); 
      settype1.setElementType(new Type(owner)); 
      ebe.setType(settype1); 
      ebe.setMultiplicity(ModelElement.MANY); 
      ebe.setUmlKind(Expression.CLASSID);
      ebe.setEntity(owner);  
      ebe.setElementType(new Type(owner));  
      Expression ldef = new BinaryExpression("->sortedBy",ebe,orderedBy);  
      CreationStatement linit = new CreationStatement("Sequence",sortedE); 
      Type seqtype2 = new Type("Sequence",null); 
      seqtype2.setElementType(new Type(owner)); 
      linit.setInstanceType(seqtype2);  
      linit.setElementType(new Type(owner));  
      Statement lassign = new AssignStatement(lve,ldef);
      // ename = ename + "sorted";  
      orderEnt = new SequenceStatement(); 
      orderEnt.addStatement(linit); 
      orderEnt.addStatement(lassign); 
      ownerbe = lve; 
    } 

    WhileStatement fs = new WhileStatement(test1,forloop); 
    fs.setLoopKind(Statement.FOR);
    fs.setLoopRange(varbe,ownerbe); 
    fs.setBrackets(true); 
    fs.setEntity(owner); 

    SequenceStatement sstat2; 
    if (innerss.size() > 0)   // From a previous constraint in the constraint group
    { sstat2 = (SequenceStatement) innerss.get(0); 
      sstat2.addBeforeEnd(fs); 
      return sstat2; 
    } 
    
    sstat2 = new SequenceStatement();
    if (ordered) 
    { sstat2.addStatement(orderEnt); } 
    sstat2.addStatement(fs);
    sstat2.addStatement(retfalse); 
    innerss.add(sstat2); 

    BehaviouralFeature bfsearch = 
      new BehaviouralFeature(opname + "search",
                             new Vector(), false, booltype); 
    bfsearch.setBx(usecase.isBx()); 
    bfsearch.setPost(betrue); 
    bfsearch.setInstanceScope(false); 
    bfsearch.setEntity(owner);
    bfsearch.setParameters(outerparms);  
    owner.addOperation(bfsearch);
    bfsearch.setActivity(sstat2); 
    bfsearch.setDerived(true); 

    /* Set up the call to search: */
    Compiler2 c3 = new Compiler2();
    c3.nospacelexicalanalysis(owner + "." + opname + "search(" + outercallpars + ")");  
    Expression searchopinvokee = c3.parse(); 

    // BasicExpression opinvoke = 
    //   new BasicExpression(evar + "." + opname + "()",0); 
    // Expression opinvokee = opinvoke.checkIfSetExpression(); 
    searchopinvokee.setUmlKind(Expression.UPDATEOP); 
    searchopinvokee.setEntity(owner);  

    // InvocationStatement searchinvokeop = 
    //     new InvocationStatement(owner + "." + opname + "search()",null,null); 
    // searchinvokeop.setCallExp(searchopinvokee); 
    
    CreationStatement cre = new CreationStatement("boolean",opname + "_running"); 
    cre.setElementType(new Type("boolean",null));  
    cre.setInstanceType(new Type("boolean",null));  
      
    Statement init = new AssignStatement(running,betrue); 
    Statement callsearch = 
        new AssignStatement(running,searchopinvokee); 
    WhileStatement ws = new WhileStatement(running, callsearch); 
    ws.setLoopKind(Statement.WHILE); 

    SequenceStatement sstat3 = new SequenceStatement(); 
    sstat3.addStatement(cre); 
    sstat3.addStatement(init); 
    sstat3.addStatement(ws); // The code invoked in the controller operation
    // sstat3.setBrackets(true); 
    sstat3.setEntity(owner); 

    return sstat3;  
  }

  public int syntacticComplexity()
  { Expression ante = antecedent(); 
    Expression succ = succedent(); 
    if (ante == null || "true".equals(ante + "")) 
    { return succ.syntacticComplexity(); } 
    else 
    { int res = ante.syntacticComplexity(); 
      res = res + 1 + succ.syntacticComplexity(); 
      return res; 
    } 
  } 

  public int cyclomaticComplexity()
  { Expression ante = antecedent(); 
    if (ante == null || "true".equals(ante + "")) 
    { return 0; } 
    else 
    { int res = ante.cyclomaticComplexity(); 
      return res + 1; 
    } 
  } 

  public void findClones(java.util.Map clones)
  { Expression ante = antecedent(); 
    Expression succ = succedent();
    String cname = usecase + "_" + id; 
     
    if (ante == null || "true".equals(ante + "")) 
    { succ.findClones(clones,cname,null); } 
    else 
    { ante.findClones(clones,cname,null); 
      succ.findClones(clones,cname,null);  
    } 
  } 

  // Only to type 1 constraints
  public Vector applyCIForm()
  { Vector res = new Vector(); // The new constraints
    Expression succ = succedent();
    if (succ instanceof BinaryExpression)
    { BinaryExpression besucc = (BinaryExpression) succ;
      Vector foralls = new Vector();
      if ("#".equals(besucc.operator) || "#1".equals(besucc.operator))
      { BinaryExpression lbe = (BinaryExpression) besucc.left;

        // Assume lbe.right is an entity type:
        Type ret = lbe.right.getElementType();
        Entity rightent; 
        String exdomain = "";  
        if (ret == null) 
        { exdomain = lbe.right + ""; 
          rightent = lbe.right.getEntity(); 
        } 
        else 
        { exdomain = ret.getName(); 
          rightent = ret.getEntity(); 
        } 
        // Add *--* association of the owner, going to exdomain
        Association traceast = new Association(owner, rightent, ModelElement.MANY, ModelElement.MANY,
                                               null, "$trace" + exdomain); 
        owner.addAssociation(traceast); 
 
        Expression trace = new BasicExpression("$trace" + exdomain);
        trace.setUmlKind(Expression.ROLE); 
        trace.setEntity(owner); 
        trace.setElementType(new Type(rightent)); 
        trace.setType(new Type("Set",null)); 
        trace.setMultiplicity(ModelElement.MANY); 

        BinaryExpression tracest = new BinaryExpression(":",lbe.left,trace);

        Expression newsucc = besucc.isExistsForall(foralls, tracest); 
        if (newsucc == null) { return res; }
        // a new *-* association of owner to lbe.right
        // Expression c1succ = newsucc.substituteEq("$$",tracest);      
        Constraint c1 = new Constraint(antecedent(), newsucc);
        c1.setOwner(owner);
        res.add(c1);
        for (int j = 0; j < foralls.size(); j++)
        { Expression forallexp = (Expression) foralls.get(j);
           Constraint c2 = new Constraint(tracest, forallexp);
           c2.setOwner(owner);
           res.add(c2);
        } 
      }
    }
    return res;
  }
    
  public void changedEntityName(String oldN, String newN)
  { if (cond0 != null)
    { cond0.changedEntityName(oldN, newN); } 
    if (cond != null) 
    { cond.changedEntityName(oldN, newN); } 
    succ.changedEntityName(oldN, newN); 
  } 
}


    /* if (val.equals("" + rightbe))
    { if (leftbe.objectRef == null)
      { return this; }
      else if (leftbe.objectRef.isMultiple())
      { // (objs - {oo}).feature *operator* val
        BasicExpression oof =
          new BasicExpression(feature);
        SetExpression ss = new SetExpression();
        ss.addElement(oobe);
        Expression nleft =
          new BinaryExpression("-",leftbe.objectRef,ss);
        oof.setObjectRef(nleft);
        Expression newante =
          new BinaryExpression(operator,oof,valbe);
        Expression newante2 = 
          Expression.simplify("&",newante,cond,new Vector());
        return new Constraint(event,newante2,
          succ,associations);
      }  // But if val, rightbe are different values of same type, return null
      else if (leftbe.objectRef.umlkind == Expression.VARIABLE)
      { newcond = Expression.substitute(cond,leftbe.objectRef,oobe);
        newsucc =
          succ.substitute(leftbe.objectRef,oobe);
        return
          new Constraint(event,newcond,newsucc,
                         associations,baseEntities);
      }
      else
      { Expression newante =
          new BinaryExpression("=",oobe,
                               leftbe.objectRef);
        Expression newante2 = 
          Expression.simplify("&",newante,cond,new Vector()); 
        return
          new Constraint(event,newante2, 
            succ,associations);
      }
    }
    else
    if (rightbe.umlkind == Expression.VALUE &&
        rightbe.getType().hasValue(val)) // and they are different
    { if (operator.equals("=")) { return null; } 
      else if (rightbe.getType().compare(operator,"" + rightbe,val))
      { return this; } 
      return null; 
    } 
    else */
