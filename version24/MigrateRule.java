import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class MigrateRule extends TypedAndGuardedConstruct
{ Vector ignored = new Vector();  // String
  Statement body;

  public MigrateRule(String t, Expression g)
  { super(t,g); }

  public void addIgnored(String ig)
  { ignored.add(ig); }

  public void setIgnored(Vector ig)
  { ignored.clear(); 
    ignored.addAll(ig); 
  }

  public void setBody(Statement b)
  { body = b; }   


  public String toString()
  { String res = "migrate " + originalType + "\n";
    res = res + "when: " + guard + "\n";
    if (ignored.size() > 0)
    { res = res + "ignoring " + ignored.get(0);
      for (int i = 1; i < ignored.size(); i++)
      { res = res + ", " + ignored.get(i); }
      res = res + "\n";
    }
    res = res + "{ " + body + " }";
    return res;
  }

  public Vector toConstraint2(Entity src, Vector retypes, Vector tentities, int index, FlockRetyping def)
  { // src is source entity named by originalType,
    // retypes the retyping rules on this
    Vector res = new Vector();

    Vector wrfr = new Vector();
    if (body != null)
    { wrfr = body.writeFrame(); }   // really wr(assocs)
    Vector ig = new Vector();
    ig.addAll(ignored);
    ig.add("$id");
    ig.addAll(wrfr);

    Expression succ;

    Type et = new Type(src);
    Expression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(src);
    selfexp.setType(et);
    selfexp.setElementType(et);
    // Expression ante = guard.substituteEq("original", selfexp);     
    // opcall.setElementType();
    // and actual parameter fvar : F

    BasicExpression betrue = new BasicExpression("true");
    betrue.setUmlKind(Expression.VALUE); 
    betrue.setType(new Type("boolean",null));

    if (retypes.size() == 0)
    { // default copy behaviour for src
      String srcname = src.getName();
      String bname = ModelElement.baseName(srcname);
      Entity trg = (Entity) ModelElement.lookupByName("OUT$" + bname, tentities);
      if (trg != null && trg.isConcrete()) 
      { String trgname = "OUT$" + bname;
        FlockRetyping defaultR = new FlockRetyping(bname,betrue,bname);
        retypes.add(defaultR);
      }
    }  // else, add the default case for src
    else if (def != null) 
    { retypes.add(def); } 

    System.out.println("RETYPES for " + src + " ARE: " + retypes); 

    for (int i = 0; i < retypes.size(); i++)
    { FlockRetyping rt = (FlockRetyping) retypes.get(i);
      Expression rtguard = rt.guard;
      Entity trg = (Entity) ModelElement.lookupByName("OUT$" + rt.evolvedType,tentities);
      if (trg == null) { continue; } 

      Type targettype = new Type(trg);
      Attribute param = new Attribute("migrated",targettype,ModelElement.INTERNAL);

      String opname = src.getName() + "migrationOp" + index + "$" + i;
      BehaviouralFeature op = new BehaviouralFeature(opname);
      Statement opcode = body.substituteEq("original", selfexp);
      opcode.setEntity(src);  
      Statement opcode1 = opcode.checkConversions(trg,null,null,new java.util.HashMap()); 
      op.setActivity(opcode1);
       // and parameter migrated : ft
      BasicExpression opcall = new BasicExpression(opname);
      opcall.setUmlKind(Expression.UPDATEOP);
      opcall.setEntity(src);
      opcall.setType(new Type("boolean",null));
      op.addParameter(param);
       // BasicExpression callf = (BasicExpression) opcall.clone();
      BasicExpression fvar = new BasicExpression(rt.evolvedType.toLowerCase());
      fvar.setUmlKind(Expression.VARIABLE);
      fvar.setEntity(trg);
      fvar.setType(targettype);
      fvar.setElementType(targettype);
      // callf.addParameter(fvar); 

      BasicExpression invoke = 
        new BasicExpression("self." + opname + "(" + fvar + ")",0); 
      Expression callf = invoke.checkIfSetExpression(); 
      Expression sbexp = guard.substituteEq("original", selfexp); 
          

      if ((guard + "").equals(rtguard + ""))
      { Constraint rcon = rt.toConstraint2(src,trg,ig);
        // set the parameter of opcall
        if (rcon == null) 
        { rcon = new Constraint(sbexp,betrue);
          rcon.setOwner(src); 
        } 
        rcon.addSuccedent(callf);
        op.setEntity(src);
        src.addOperation(op); 
        res.add(rcon);
        // System.out.println("CONSTRAINT for " + this + " " + rt + " IS: " + rcon); 
        return res; 
      }
      Constraint rcon1 = rt.toConstraint2(src,trg,ig);
      if (rcon1 == null) 
      { Expression rtexp = rtguard.substituteEq("original", selfexp); 
        rcon1 = new Constraint(rtexp,betrue);
        rcon1.setOwner(src); 
      } 
      rcon1.addAntecedent(sbexp);
      // set the parameter of opcall
      rcon1.addSuccedent(callf);
      if (rcon1.isTrivial()) { } 
      else 
      { op.setEntity(src);
        src.addOperation(op); 
        res.add(rcon1); 
        // System.out.println("CONSTRAINT for " + this + " " + rt + " IS: " + rcon1); 
      } 
    }
    return res;
  }

}


