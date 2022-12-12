import java.util.Vector; 

/* Package: Transformations */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class BacktrackingSpecification
{ Entity context;
  UseCase transformation;
  Constraint rule;  // first postcond of use case
  Expression success = null;  // optional
  Expression backtrack;
  Expression successAction = new UnaryExpression("->display", new BasicExpression("\"search succeeded\""));  // optional
  Expression possible;  // 1st quantifier range of rule
  Statement undoAction;

  Type valueType;  // possible.elementType
   Attribute history;
   Attribute chosen;
   String valueVar;

  public BacktrackingSpecification(Entity e, UseCase uc, Expression bktk, 
                                   Expression poss, Statement undo)
  { context = e;
    transformation = uc;
    backtrack = bktk;
    possible = poss;
    undoAction = undo;

    String nme = transformation.getName();
    Type seqtype = new Type("Sequence",null);
    Type seqtype1 = new Type("Sequence",null);
    Type settype = new Type("Set",null);
    valueType = possible.getElementType();
    settype.setElementType(valueType); 

    if (valueType == null)
    { System.err.println(possible + " must have valid element type"); }
    else 
    { history = new Attribute(nme + "$history",seqtype,ModelElement.DERIVED);
      history.setElementType(seqtype1);
      history.setEntity(context);
      history.setInstanceScope(false);
      context.addAttribute(history);
      chosen = new Attribute(nme + "$chosen",settype,ModelElement.DERIVED);
      chosen.setElementType(valueType);
      chosen.setEntity(context);
      context.addAttribute(chosen);
      rule = (Constraint) transformation.getPostcondition(1);
      // valueVar is 1st quantified variable
    }
  }

  public void generateDesign1(Vector types, Vector entities)
  { addGetPossibleValues(types,entities);
    modifyRule(); 
  }

  public void generateDesign2(Vector types, Vector entities)
  { addSuccessTest(types,entities);
    addSuccessAction(types,entities);
    addRedo(types,entities);
    // addUndo(types,entities); 
    addBacktrackTest(types,entities);
    addBacktrackop(types,entities);
  }

  public void setSuccess(Expression se)
  { success = se; }

  public void addSuccessTest(Vector types, Vector entities)
  { if (success == null) { return; }
    String nme = transformation.getName(); 
    Type booltype = new Type("boolean",null);
    BasicExpression res = new BasicExpression("result");
    res.setType(booltype);
    BasicExpression betrue = new BasicExpression(true);
    betrue.setType(booltype);
    res.setUmlKind(Expression.VARIABLE);
    Expression eqtrue = new BinaryExpression("=", res, betrue);
    Expression succtest = new BinaryExpression("=>", success, eqtrue);
    BehaviouralFeature stest = new BehaviouralFeature(nme + "success", new Vector(), true, booltype);
    stest.setPost(succtest);
    // stest.setDerived(true);
    stest.setEntity(context);
    context.addOperation(stest);
    stest.typeCheck(types,entities);
  }

  public void addSuccessAction(Vector types, Vector entities)
  { if (successAction == null) { return; }
    String nme = transformation.getName(); 
    BehaviouralFeature sop = new BehaviouralFeature(nme + "successAction", new Vector(), false, null);
    sop.setPost(successAction);
    // sop.setDerived(true);
    sop.setEntity(context);
    context.addOperation(sop);
    sop.typeCheck(types,entities);
  }

  public Statement getSuccessCase(String evar)
  { // if evar.nmesuccess() then (evar.successAction(); return false)
    Type booltype = new Type("boolean",null);
    String nme = transformation.getName(); 
    BasicExpression successtest = 
         new BasicExpression(evar + "." + nme + "success()", 0);
    Expression successteste = successtest.checkIfSetExpression();
    if (successteste == null) { return null; }
    successteste.setUmlKind(Expression.QUERY);
    successteste.setType(booltype);
    successteste.setEntity(context);
    BasicExpression befalse = new BasicExpression(false);
    befalse.setType(booltype);
    Statement retfalse = new ReturnStatement(befalse);
    BasicExpression successop = 
         new BasicExpression(evar + "." + nme + "successAction()", 0);
    Expression successope = successop.checkIfSetExpression();
    if (successope == null) { return null; }
    successope.setUmlKind(Expression.UPDATEOP);
    successope.setEntity(context);
    InvocationStatement callop = new InvocationStatement(evar + "." + nme + "successAction()",null,null);
    callop.setCallExp(successope);
    SequenceStatement callsuccess = new SequenceStatement();
    callsuccess.addStatement(callop);
    callsuccess.addStatement(retfalse);
    callsuccess.setBrackets(true); 
    Statement res = new IfStatement(successteste, callsuccess);
    res.setEntity(context);
    return res;
  }

 /* public void addUndo(Vector types, Vector entities)
  { if (undoAction == null) { return; }
    String nme = transformation.getName(); 
    Vector pars = new Vector();
    String var = rule.getSecondaryVar(0);
    BasicExpression v = new BasicExpression(var);
    v.setUmlKind(Expression.VARIABLE);
    v.setType(valueType); 
    Attribute valx = new Attribute(var, valueType, ModelElement.INTERNAL); 
    pars.add(valx);
    Type booltype = new Type("boolean",null);
    BasicExpression betrue = new BasicExpression("true");
    betrue.setType(booltype);

    BehaviouralFeature sop = new BehaviouralFeature(nme + "undo", pars, false, null);
    sop.setPost(betrue);
    // sop.setDerived(true);
    sop.setEntity(context);
    SequenceStatement undoss = new SequenceStatement();
    // _history := _history.front
    BasicExpression hist = new BasicExpression(history);
    AssignStatement sethistfront = 
        new AssignStatement(hist, new UnaryExpression("->front", hist));
    undoss.addStatement(sethistfront);
    undoss.addStatement(undoAction);
    // _chosen := Sequence{}
    BasicExpression ch = new BasicExpression(chosen);
    SetExpression empty = new SetExpression();
    // empty.setOrdered(true); 
    AssignStatement setch = 
        new AssignStatement(ch, empty);
    undoss.addStatement(setch); 
    sop.setActivity(undoss); 
    context.addOperation(sop);
    sop.typeCheck(types,entities);
  } */ 

  public void addRedo(Vector types, Vector entities)
  { if (undoAction == null || rule == null) { return; }
    String nme = transformation.getName(); 
    Vector pars = new Vector();
    String var = rule.getSecondaryVar(0);
    BasicExpression v = new BasicExpression(var);
    v.setUmlKind(Expression.VARIABLE);
    v.setType(valueType); 
    Attribute valx = new Attribute(var, valueType, ModelElement.INTERNAL); 
    v.variable = valx; 
    pars.add(valx);
    Type booltype = new Type("boolean",null);
    BasicExpression betrue = new BasicExpression(true);
    betrue.setType(booltype);

    BehaviouralFeature sop = new BehaviouralFeature(nme + "redo", pars, false, null);
    sop.setPost(betrue);
    // sop.setDerived(true);
    sop.setEntity(context);

    SequenceStatement redoss = new SequenceStatement();


    // v := getPossibleValues()->any()
    BasicExpression getpossvalues = 
         new BasicExpression("getPossibleValues()", 0);
    Expression possee = getpossvalues.checkIfSetExpression();
    if (possee == null) { return; }
    possee.setUmlKind(Expression.QUERY);
    possee.setType(possible.getType());
    possee.setElementType(valueType);
    possee.setEntity(context);
    UnaryExpression anyexp = new UnaryExpression("->any", possee); 
    AssignStatement reassign = new AssignStatement(v,anyexp); 
    redoss.addStatement(reassign); 

    String rulename = transformation.getName() + rule.getId();
    BehaviouralFeature bf = context.getOperation(rulename);
    if (bf == null) 
    { System.err.println("No operation for rule: " + rulename); 
      return; 
    } 

    InvocationStatement bfcall = new InvocationStatement(bf);
    bfcall.setEntity(context);
    redoss.addStatement(bfcall);

    sop.setActivity(redoss); 
    context.addOperation(sop);
    sop.typeCheck(types,entities);
  }

  public void addBacktrackTest(Vector types, Vector entities)
  { if (backtrack == null) { return; }
    String nme = transformation.getName(); 
    Type booltype = new Type("boolean",null);
    BasicExpression res = new BasicExpression("result");
    res.setType(booltype);
    BasicExpression betrue = new BasicExpression(true);
    betrue.setType(booltype);
    res.setUmlKind(Expression.VARIABLE);
    BasicExpression zero = new BasicExpression(0); 
    BasicExpression getpossvalues = 
         new BasicExpression("getPossibleValues()", 0);
    Expression getpossvaluese = getpossvalues.checkIfSetExpression();
    if (getpossvaluese == null) { return; }
    getpossvaluese.setUmlKind(Expression.QUERY);
    getpossvaluese.setType(possible.getType());
    getpossvaluese.setElementType(valueType);
    getpossvaluese.setEntity(context);
    UnaryExpression posssize = new UnaryExpression("->size", getpossvaluese); 
    Expression backtracke = new BinaryExpression("=", posssize, zero); 
    Expression eqtrue = new BinaryExpression("=", res, betrue);
    Expression succtest = 
       new BinaryExpression("=>", backtracke, eqtrue);
    BehaviouralFeature stest = new BehaviouralFeature(nme + "backtracktest", new Vector(), true, booltype);
    stest.setPost(succtest);
    // stest.setDerived(true);
    stest.setEntity(context);
    context.addOperation(stest);
    stest.typeCheck(types,entities);
  }

  public Statement getBacktrackCase(String evar)
  { // if evar.nmebacktracktest() then (result = evar.backtrack(); return result)
    Type booltype = new Type("boolean",null);
    String nme = transformation.getName(); 
    BasicExpression backtracktest = 
         new BasicExpression(evar + "." + nme + "backtracktest()", 0);
    Expression backtrackteste = backtracktest.checkIfSetExpression();
    if (backtrackteste == null) { return null; }
    backtrackteste.setUmlKind(Expression.QUERY);
    backtrackteste.setType(booltype);
    backtrackteste.setEntity(context);
    // BasicExpression befalse = new BasicExpression("false");
    // befalse.setType(booltype);
    // Statement retfalse = new ReturnStatement(befalse);
    BasicExpression backtrackop = 
         new BasicExpression(evar + "." + nme + "backtrack()", 0);
    Expression backtrackope = backtrackop.checkIfSetExpression();
    if (backtrackope == null) { return null; }
    backtrackope.setUmlKind(Expression.UPDATEOP);
    backtrackope.setEntity(context);
    backtrackope.setType(booltype);

    BasicExpression res = new BasicExpression("result");
    res.setType(booltype);
    res.setUmlKind(Expression.VARIABLE);
    AssignStatement callop = new AssignStatement(res, backtrackope);
    Statement testres = 
       // new IfStatement(res, new SequenceStatement(), 
          new ReturnStatement(res); 
    SequenceStatement callbacktrack = new SequenceStatement();
    callbacktrack.addStatement(callop);
    callbacktrack.addStatement(testres);
    callbacktrack.setBrackets(true); 
    Statement resb = new IfStatement(backtrackteste, callbacktrack);
    resb.setEntity(context);
    return resb;
  }

  public void addGetPossibleValues(Vector types, Vector entities)
  { if (possible == null) { return; }
    String nme = transformation.getName(); 
    Type settype = possible.getType();
    BasicExpression res = new BasicExpression("result");
    res.setType(settype);
    BasicExpression ch = new BasicExpression(chosen);
    res.setUmlKind(Expression.VARIABLE);
    Expression sub = new BinaryExpression("-", possible, ch); 
    Expression eqres = new BinaryExpression("=", res, sub);
    BehaviouralFeature stest = new BehaviouralFeature("getPossibleValues", new Vector(), true, settype);
    stest.setPost(eqres);
    // stest.setDerived(true);
    stest.setEntity(context);
    // stest.setType(settype); 
    stest.setElementType(valueType); 
    context.addOperation(stest);
    stest.typeCheck(types,entities);
  }

  public void addBacktrackop(Vector types, Vector entities)
  { if (history == null || possible == null ||
         chosen == null) 
    { return; }
    String nme = transformation.getName(); 
    String evar = context.getName().toLowerCase() + "x";
    Type booltype = new Type("boolean",null);
    Type settype = possible.getType();
    BasicExpression betrue = new BasicExpression(true);
    betrue.setType(booltype);
    BasicExpression befalse = new BasicExpression(false);
    befalse.setType(booltype);
    ReturnStatement rettrue = new ReturnStatement(betrue);
    ReturnStatement retfalse = new ReturnStatement(befalse);

    BasicExpression ch = new BasicExpression("chosen");
    ch.setType(chosen.getType());
    ch.setElementType(valueType); 
    ch.setUmlKind(Expression.VARIABLE);
    Expression zeroexp = new BasicExpression(0); 
    BasicExpression getpossvalues = 
         new BasicExpression(evar + ".getPossibleValues()", 0);
    Expression getpossvaluese = getpossvalues.checkIfSetExpression();
    if (getpossvaluese == null) { return; }
    getpossvaluese.setUmlKind(Expression.QUERY);
    getpossvaluese.setType(possible.getType());
    getpossvaluese.setElementType(valueType);
    getpossvaluese.setEntity(context);

    Expression hist = new BasicExpression(history);
    BasicExpression histsize = new BasicExpression("size");
    histsize.setObjectRef(hist); 
    Expression histtest = new BinaryExpression(">", histsize, zeroexp);

    // _histlast : Sequence := _history.last
    // CreationStatement crehistlast = new CreationStatement("Sequence", "_histlast");
    BasicExpression histlast = new BasicExpression("_histlast");
    histlast.setType(history.getElementType());
    histlast.setUmlKind(Expression.VARIABLE);
    UnaryExpression lst = new UnaryExpression("->last", hist); 
    BinaryExpression castlst = 
      new BinaryExpression("->oclAsType", lst, new BasicExpression("Sequence"));  
    AssignStatement gethistlast = 
        new AssignStatement(histlast, castlst);
    gethistlast.setType(history.getElementType());
    
    // evar : E := _histlast[2]
    // CreationStatement creevar = new CreationStatement(context.getName(), evar);
    BasicExpression ex = new BasicExpression(evar);
    ex.setType(new Type(context));
    ex.setUmlKind(Expression.VARIABLE); 
    BasicExpression secondind = (BasicExpression) histlast.clone();
    secondind.setArrayIndex(new BasicExpression(2)); 
    BinaryExpression cast2ndind = new BinaryExpression("->oclAsType", secondind, 
                                         new BasicExpression(context + "")); 
    AssignStatement getevar = 
        new AssignStatement(ex, cast2ndind);
    getevar.setType(new Type(context));
    
    // $value : valueType := _histlast.last
    // CreationStatement creeval = new CreationStatement(valueType + "", "$value");
    BasicExpression valx = new BasicExpression("$value");
    valx.setType(valueType);
    valx.setUmlKind(Expression.VARIABLE); 
    BasicExpression finalind = (BasicExpression) histlast.clone();
    finalind.setArrayIndex(new BasicExpression(3)); 
    BinaryExpression castfinal = new BinaryExpression("->oclAsType", finalind, 
                                         new BasicExpression("" + valueType)); 
    AssignStatement getval = 
        new AssignStatement(valx, castfinal);
    getval.setType(valueType);

    // _history := _history.front
    BasicExpression hist2 = new BasicExpression(history);
    AssignStatement sethistfront = 
        new AssignStatement(hist2, new UnaryExpression("->front", hist2));
    // redoss.addStatement(sethistfront);

    // redoss.addStatement(undoAction);
    
    // chosen : Set(valueType) := ex.getPossibleValues() 
    CreationStatement creech = new CreationStatement("Set", "chosen");
    creech.setType(chosen.getType()); 
    creech.setElementType(valueType); 

    AssignStatement getch = 
        new AssignStatement(ch, getpossvaluese);

    BasicExpression chsize = new BasicExpression("size");
    chsize.setObjectRef(ch);
    chsize.setType(new Type("int", null)); 

    BinaryExpression chgr0 = new BinaryExpression(">", chsize, zeroexp);

    BasicExpression redoop = 
         new BasicExpression(evar + "." + nme + "redo($value)", 0);
    Expression redoope = redoop.checkIfSetExpression();
    if (redoope == null) { return; }
    redoope.setUmlKind(Expression.UPDATEOP);
    redoope.setEntity(context);
    InvocationStatement callredo = new InvocationStatement(evar + "." + nme + "redo($value)",null,null);
    callredo.setCallExp(redoope); 
    SequenceStatement redoss = new SequenceStatement();
    redoss.addStatement(callredo);
    redoss.addStatement(rettrue);

    /* BasicExpression undoop = 
         new BasicExpression(evar + "." + nme + "undo($value)", 0);
    Expression undoope = undoop.checkIfSetExpression();
    if (undoope == null) { return; }
    undoope.setUmlKind(Expression.UPDATEOP);
    undoope.setEntity(context);
    InvocationStatement callundo = new InvocationStatement(evar + "." + nme + "undo($value)",null,null);
    callundo.setCallExp(undoope); */ 

    IfStatement ifstat = new IfStatement(chgr0,redoss);

    SequenceStatement sstat2 = new SequenceStatement(); // inner ss
    sstat2.setBrackets(true); 
    // sstat2.addStatement(crehistlast); 
    sstat2.addStatement(gethistlast);
    // sstat2.addStatement(creevar); 
    sstat2.addStatement(getevar); 
    // sstat2.addStatement(creeval); 
    sstat2.addStatement(getval); 
    sstat2.addStatement(sethistfront); 
    sstat2.addStatement(undoAction); 
    sstat2.addStatement(creech); 
    sstat2.addStatement(getch); 
    sstat2.addStatement(ifstat);
 
    SequenceStatement sstat = new SequenceStatement();
    WhileStatement ws = new WhileStatement(histtest, sstat2);
    ws.setLoopKind(Statement.WHILE);
    sstat.addStatement(ws);
    sstat.addStatement(retfalse);

    BehaviouralFeature bop = new BehaviouralFeature(nme + "backtrack", new Vector(), false, booltype);
    bop.setPost(betrue);
    bop.setInstanceScope(false); 
    // bop.setDerived(true);
    bop.setEntity(context);
    bop.setActivity(sstat);
    context.addOperation(bop);
    bop.typeCheck(types,entities);
  }

  public void modifyRule()
  { // replaces possible by getPossibleValues(), adds
    // assignments to chosen and history
    String var = rule.getSecondaryVar(0);
    Expression quant = rule.getSecondaryQuantifier(0);
    BasicExpression v = new BasicExpression(var);
    v.setUmlKind(Expression.VARIABLE);
    v.setType(valueType); 
    BasicExpression getpossvalues = 
         new BasicExpression("getPossibleValues()", 0);
    Expression getpossvaluese = getpossvalues.checkIfSetExpression();
    if (getpossvaluese == null) { return; }
    getpossvaluese.setUmlKind(Expression.QUERY);
    getpossvaluese.setType(possible.getType());
    getpossvaluese.setElementType(valueType);
    getpossvaluese.setEntity(context);
    rule.replaceSecondaryRange(1,var,getpossvaluese); 
    Expression ante = rule.antecedent();
    Expression newante = ante.substitute(quant, getpossvaluese); 
    rule.setAntecedent(newante);

    BasicExpression che = new BasicExpression(chosen); 
    BinaryExpression addchosen = new BinaryExpression("->includes",che,v);
    BasicExpression histe = new BasicExpression(history); 
    String rulename = transformation.getName() + "1";
    BasicExpression rle = new BasicExpression("\"" + rulename + "\"");
    rle.setType(new Type("String",null));
    BasicExpression selfexp = new BasicExpression("self"); 
    selfexp.setType(new Type(context));
    Vector vals = new Vector();
    vals.add(rle); vals.add(selfexp); vals.add(v); 
    SetExpression tuple = new SetExpression(vals);
    tuple.setOrdered(true);  
    BinaryExpression addhist = new BinaryExpression("->includes",histe,tuple); 
    rule.addSuccedent(addchosen);
    rule.addSuccedent(addhist);
  }
 
}
