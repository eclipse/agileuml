/*
      * Classname : Transition
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the logical class for the 
      * transitions (lines) in the statechart diagram.
*/
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.io.*;
import java.util.Vector; 
import java.util.StringTokenizer; 

class Transition extends Named implements Serializable
{
  Event event;             /* Trigger event */ 
  State source;            /* Starting state */ 
  State target;            /* Ending state, can be same as source */ 
  Expression guard = new BasicExpression("true"); 
              /* Boolean-valued condition, default "true" */ 
  Vector generations = new Vector();  /* List of generated events */ 

  //Link to the graphical instance
  //LineData line;
  public static final int CONTROLLED = 0;  // actuator transition
  public static final int UNCONTROLLED = 1;  // sensor transition
 
  public Transition(String id)
  {  
    super(id); 
    System.out.println("-- Transition created");
  }

  public Transition(String lab, State src, State targ, Event ev)
   {  super(lab);
      source = src;
      target = targ;
      event = ev;
      guard = new BasicExpression("true"); 
      generations = new Vector(); 
  }

  public void setEvent(Event e) 
  { event = e; } 
 
  public Event getEvent() 
  { return event; } 
  
  // public Event get_event()
  // {
    // return event;
  // }
  
  public void setSource(State src)
  {
    source = src;
  }

  public State getSource() 
  { return source; } 

  public void setTarget(State targ)
  {
    target = targ;
  }

  public State getTarget() 
  { return target; } 

  public Expression getGuard()
  { return guard; } 

  public Vector getGenerations()
  { return generations; } 

 //   public void setLine(Line ln)
//    {
//      line = ln;
//    }

  public void deleteState(State s) 
  { if (s == source) 
    { source = null; 
      System.out.println("There is no source for " + label); 
    } 
    if (s == target) 
    { target = null; 
      System.out.println("There is no target for " + label); 
    } 
  } 

  public Vector dataDependents(Vector allvars, Vector vars, java.util.Map reach,
                               State sliceState)
  { // all the variables x such that x |-> y for some y in vars
    Vector res = new Vector(); 
    Vector gvars0 = new Vector(); 
    Vector vars1 = new Vector(); 
    vars1.addAll(vars); 

    Vector deps = new Vector(); 
    Statement acts = new InvocationStatement(new Event("skip")); 

    if (generations == null || generations.size() == 0)
    { deps.addAll(vars); } 
    else 
    { Expression gen = (Expression) generations.get(0); 
      acts = generationToJava(gen);  
      deps = acts.dataDependents(allvars,vars1); 
    }

    /* if (source == target)
    { if (generations == null || generations.size() == 0)
      { return vars1; }  // valid for skip and blocking semantics only
      else 
      { if (acts.updates(vars1)) { } 
        else 
        { return vars1; } 
      } 
    } */ 

    if (guard != null)
    { if (target != source && isOnlyPath(reach)) { } 
      else 
      { gvars0 = guard.variablesUsedIn(allvars); }  // only in case guard
    }                                               // affects path function
    
    Vector gvars = new Vector(); 
    for (int i = 0; i < deps.size(); i++) 
    { String v = (String) deps.get(i); 
      if (gvars0.contains(v)) { } 
      else 
      { gvars.add(v); } 
    }
    gvars.addAll(gvars0);  

    return gvars; 
  }  

  private boolean isOnlyPath(java.util.Map reach)
  { Vector intoTarget = target.trans_to; 
    Vector sourceReaches = (Vector) reach.get(source); 

    for (int i = 0; i < intoTarget.size(); i++) 
    { Transition tr = (Transition) intoTarget.get(i); 
      if (tr == this) { } 
      else if (sourceReaches.contains(tr.source))
      { return false; } // there is another path not through this
    } 
    return true; 
  } 

      
  private boolean isPathIndependent(java.util.Map reach,State sliceState,
                                    Vector vars)
  { boolean res = false;
    if (source.trans_from.size() == 1)
    { return true; }   // the only path from source
    for (int i = 0; i < source.trans_from.size(); i++)
    { Transition other = (Transition) source.trans_from.get(i);
      if (other != this)
      { if (ineffectiveTransition(other,reach,vars,sliceState)) { }
        else { return false; }
      }
    }
    return true;
  }

  public boolean isActionless()
  { return (generations == null || generations.size() == 0); } 

  private boolean ineffectiveTransition(Transition other,
      java.util.Map reach, Vector vars, State sliceState)
  { boolean res = false;
    if (other.target == source &&
        ("" + other.event).equals("" + event) &&
        (other.generations == null ||
         other.generations.size() == 0))
    { return true; }
    if (other.target == source &&
        ("" + other.event).equals("" + event) &&
        other.generations != null &&
        other.generations.size() > 0)
    { Expression ge = (Expression) other.generations.get(0); 
      Statement gs = generationToJava(ge); 
      if (gs.updates(vars)) 
      { } 
      else 
      { return true; } 
    } 
    if (other.target != source &&
        ("" + other.event).equals("" + event))
    { Vector reaches = (Vector) reach.get(other.target);
      if (reaches != null && reaches.contains(sliceState)) { } 
      else 
      { return true; }
    }
    return res;
  }

  public void slice(Vector allvars, Vector tvars)
  { if (source == target && (generations == null || generations.size() == 0))
    { guard = new BasicExpression("true");
      return; 
    } 
    // valid for skip and blocking semantics only
    if (generations == null || generations.size() == 0)
    { return; } 

    Expression gen = (Expression) generations.get(0); 
    Statement acts = generationToJava(gen);  
    acts.typeCheck(new Vector(), new Vector(), new Vector(), new Vector()); 

    if (acts instanceof SequenceStatement)
    { Vector stats = ((SequenceStatement) acts).slice(allvars,tvars);
      Expression gexp = (new SequenceStatement(stats)).toExpression(); 
      generations = new Vector(); 
      generations.add(gexp); 
    } 
    else if (acts instanceof AssignStatement)
    { Vector res = ((AssignStatement) acts).slice(allvars,tvars); 
      if (res.size() == 0)
      { generations = new Vector(); } 
    }  
  }

  private boolean isPathIndependent()
  { boolean res = false;
    if (source.trans_from.size() == 1)
    { return true; }   // the only path from source
    if (source.trans_from.size() == 2)
    { Transition other = (Transition) source.trans_from.get(0);
      if (other == this)
      { other = (Transition) source.trans_from.get(1); }
      if (other.target == source &&
          ("" + other.event).equals("" + event) &&
          guard != null &&
          (other.generations == null ||
           other.generations.size() == 0) &&
          guard.conflictsWith(other.guard))
      { return true; }
    }
    return false;
  }

  public void outputEventSlice(Vector evs)
  { Vector newgens = new Vector();
    for (int i = 0; i < generations.size(); i++)
    { Expression gen = (Expression) generations.get(i);
      Expression ngn = outputSliceGen(gen,evs);
      if (ngn != null)
      { newgens.add(ngn); }
    }
    generations = newgens;
  }

  private Expression outputSliceGen(Expression g, Vector evs)
  { if (g instanceof BinaryExpression)
    { BinaryExpression gbe = (BinaryExpression) g;
      if ("&".equals(gbe.operator))
      { Expression g1 = outputSliceGen(gbe.left,evs);
        Expression g2 = outputSliceGen(gbe.right,evs); 
        return Expression.simplify("&",g1,g2,false);
      }
    }
    if (evs.contains(g + ""))
    { return null; }
    if (g instanceof BasicExpression)
    { BasicExpression beg = (BasicExpression) g; 
      if (evs.contains(beg.data))
      { return null; } 
    } 
    return g;
  }
  
  public boolean isSelfSkipTransition()
  { if (source == target &&
        (generations == null || generations.size() == 0)
       )
    { return true; } 
    return false; 
  } 
      
  public Statement transitionToCode()
  { Expression tent = target.getEntryAction(); 
    Statement entrystat = generationToJava(tent); 

    if (generations == null || generations.size() == 0)
    { return entrystat; } 
    Expression gen = (Expression) generations.get(0); 
    Statement acts = generationToJava(gen);  
    
    
    if ((acts instanceof SequenceStatement) && entrystat != null) 
    { ((SequenceStatement) acts).addStatement(entrystat); 
      return acts; 
    } 
    else if (entrystat != null) 
    { SequenceStatement ss = new SequenceStatement(); 
      ss.addStatement(acts); 
      ss.addStatement(entrystat); 
      return ss; 
    } 
    return acts; 
  } 

  public static Statement generationToJava(Expression gen) 
  { BinaryExpression g = null; 
    String gop; 

    if (gen == null) { return null; } 
    if (gen instanceof BinaryExpression)
    { g = (BinaryExpression) gen; 
      gop = g.operator; 
    } 
    else 
    { return new InvocationStatement(new Event("" + gen)); } 
 
    if (!gop.equals("&"))
    { if (gop.equals("="))  
      { return new AssignStatement(g.left,g.right); }
      Event e = new Event("" + g); 
      return new InvocationStatement(e);
    }  

    SequenceStatement ss = new SequenceStatement(); 
    Expression g1 = g.getLeft(); 
    Expression g2 = g.getRight();
    if (g1 instanceof BinaryExpression) 
    { BinaryExpression gen1 = (BinaryExpression) g1; 
      if (gen1.operator.equals("&"))
      { ss.mergeSequenceStatements(generationToJava(gen1)); } 
      else if (gen1.operator.equals("="))
      { ss.addStatement(new AssignStatement(gen1.left,gen1.right)); } 
      else 
      { ss.addStatement(new InvocationStatement(new Event("" + gen1))); } 
    }
    else 
    { ss.addStatement(new InvocationStatement(new Event("" + g1))); } 
      
    if (g2 instanceof BinaryExpression) 
    { BinaryExpression gen2 = (BinaryExpression) g2; 
      if (gen2.operator.equals("&"))
      { ss.mergeSequenceStatements(generationToJava(gen2)); } 
      else if (gen2.operator.equals("="))
      { ss.addStatement(new AssignStatement(gen2.left,gen2.right)); } 
      else 
      { ss.addStatement(new InvocationStatement(new Event("" + gen2))); } 
    }
    else 
    { ss.addStatement(new InvocationStatement(new Event("" + g2))); } 
    
    return ss; 
  } 

  public boolean similarToAny(Vector trs)
  { boolean res = false;
    for (int i = 0; i < trs.size(); i++)
    { Transition tr = (Transition) trs.get(i);
      if (tr.target == target &&
          ("" + tr.event).equals("" + event) &&
          ("" + tr.generations).equals("" + generations) &&
          ("" + tr.guard).equals("" + guard))
      { return true; }
    }
    return false;
  }

  public boolean mergeTransition(Transition t1)
  { if (target == t1.target &&
         ("" + event).equals("" + t1.event) &&
         ("" + generations).equals("" + t1.generations))
    { guard = 
        (new BinaryExpression("or",guard,t1.guard)).simplify();
      return true; 
    }
    return false;
  }



  public void dispSrcTarg()
  { System.out.println(); 
    System.out.println("TRANSITION ==> " + label);
    if (source == null)
      {
	System.out.println("There is no source for " + label);
      }
    else
      {
	System.out.println("---source ==>" + source.label);
	
      }
    if (target == null)
      {
	System.out.println("There is no target for " + label);
      }
    else
      {	
	System.out.println("---target ==>" + target.label);
      }
    System.out.println("Generations: " + genToString()); 
    System.out.println("Guard: " + guardToString()); 
    // System.out.println(); 
  }

  public void display_transition()
  {  System.out.println(source.label + " -- " + 
                        label + " : " + event.label + " -> " + target.label); 
  }

  public Object clone() 
  { Transition res = new Transition(label,source,target,event); 
    Vector newgen = (Vector) generations.clone(); 
    Expression newguard = (Expression) guard.clone(); 
    res.generations = newgen; 
    res.guard = newguard; 
    return res; 
  } 

  // Old definition, for RSDS tool:
  public void setGenerations(String s, Vector events, int multip)
  { StringTokenizer st = new StringTokenizer(s,"/");
    Vector res = new Vector();
    while (st.hasMoreTokens())
    { String se = st.nextToken().trim();
      Event e = (Event) VectorUtil.lookup(se,events);
      if (e == null) 
      { System.out.println("Can't find event called " + se);
        System.out.println("Assuming it is an assignment att := val");
        AssignStatement as = parseAssignStatement(se,multip); 
        // Event ee = new Event("" + as);
        res.add(as); 
      } 
      else 
      { res.add(new InvocationStatement(e)); 
        System.out.println("Added event " + e + " to generations"); 
      }
    }
    generations = res; 
  }

  public void setGenerations(String s, Vector events)
  { Vector res = new Vector();
    if (s == null || s.equals("") || s.equals("null")) 
    { generations = res; 
      return; 
    } 
    if (s.charAt(0) == '/')
    { System.err.println("invalid generation: " + s); 
      s = s.substring(1,s.length());
    }  // crazy

    Compiler comp = new Compiler(); 
    comp.lexicalanalysis(s); 
    Expression gen = comp.parse(); 
    if (gen == null)
    { System.out.println("Unrecognised generation expression: " + s); } 
    else 
    { res.add(gen); } 
    generations = res; 
  }

  public String genToString()
  { String res = "";
    for (int i = 0; i < generations.size(); i++)
    { res = res + "/" + generations.get(i); 
   //               ((Event) generations.elementAt(i)).label; 
    }
    return res; 
  }

  public SequenceStatement genToSequenceStatement() 
  { SequenceStatement res = new SequenceStatement(); 
    for (int i = 0; i < generations.size(); i++) 
    { // Event ee = (Event) generations.elementAt(i); 
      // InvocationStatement is = new InvocationStatement(ee); 
      Statement is = (Statement) generations.get(i); 
      res.addStatement(is); 
    } 
    return res; 
  } 

  public void setGuard(String s) 
  { Compiler comp = new Compiler(); 
    comp.lexicalanalysis(s); 
    guard = comp.parse(); 
  } 

  private AssignStatement parseAssignStatement(String s, int multip) 
  { Compiler comp = new Compiler();
    comp.lexicalanalysis(s);
    Expression aexp = comp.parse();
    if (aexp instanceof BinaryExpression) 
    { BinaryExpression be = (BinaryExpression) aexp; 
      // operator should be ":=" 
      if (multip > 1) 
      { Expression lft = new BasicExpression(be.left + "(oo)"); 
        AssignStatement as = new AssignStatement(lft,be.right); 
        return as; 
      } 
      else 
      { AssignStatement as = new AssignStatement(be.left,be.right);
        return as;
      } 
    } 
    else 
    { System.err.println("Wrong syntax for assignment: " + s); 
      return null; 
    } 
  } 

  public String guardToString() 
  { String res = ""; 
    if (guard != null) 
    { if (guard.equalsString("true")) 
      { return res; } 
      else 
      { return "[" + guard + "]"; } 
    } 
    return res; 
  } 

  public static void liftAll(Vector trans, int i, 
                              Statemachine abs, 
                              Vector absStates)
  { for (int j = 0; j < trans.size(); j++)
    { Transition t = (Transition) trans.get(j);
      t.lift(i,abs,absStates);
    }
  }

  private void lift(int i, 
                    Statemachine abs,
                    Vector absStates)
  { Vector abstates = abs.states;
    for (int j = 0; j < abstates.size(); j++)
    { State st = (State) abstates.get(j);
        // System.out.println("Abstract state " + st); 
      String[] src = (String[]) absStates.get(j);  
        // System.out.println("Corresponding array is: " + src[0]); 
      if (src[i].equals(source.label))
      { String[] targ = (String[]) src.clone();
        targ[i] = target.label;
           // System.out.println("New target: " + targ[0]); 
        State targState = 
          (State) VectorUtil.lookup(
                    Named.nameFor(targ), abstates);
             // System.out.println("Named.nameFor(targ) is: " + Named.nameFor(targ)); 
             // System.out.println("Found state: " + targState); 
        if (targState != null) 
        { Transition tlift = 
            new Transition("t_" + i + "_" + j,
                           st,targState,event);
          abs.add_trans(tlift);
        } 
      }
    }
  }

  public static void liftSkipEvents(Vector ssevents, 
                                    Statemachine abs)
  //                                  Vector absStates)
  { Vector abstates = abs.states;
    for (int j = 0; j < abstates.size(); j++)
    { State st = (State) abstates.get(j);
      for (int k = 0; k < ssevents.size(); k++)
      { Event ev = (Event) ssevents.get(k);
        Transition evstskip =
          new Transition("t_" + st.label + "_" +
                         ev.label + "_skip", st, st, ev);
        abs.add_trans(evstskip);
      }
    }
  }

}
