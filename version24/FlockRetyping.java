import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class FlockRetyping extends TypeMappingConstruct
{ String evolvedType = "";

  public FlockRetyping(String t, Expression g, String nt)
  { super(t,g);
    evolvedType = nt;
  }

  public String toString()
  { return "retype " + originalType + " to " + evolvedType + " when: " + guard; }

  public Constraint toConstraint1(Entity e, Entity f)
  { Type ft = new Type(f);
    Type et = new Type(e);
    Expression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(e);
    selfexp.setType(et);
    selfexp.setElementType(et);

    Expression ante = guard.substituteEq("original", selfexp);
    String fname = evolvedType.toLowerCase();

    BasicExpression fvar = new BasicExpression(fname);
    fvar.setUmlKind(Expression.VARIABLE);
    fvar.setEntity(f);
    fvar.setType(ft);
    fvar.setElementType(ft);

    BasicExpression ftype = new BasicExpression(f.getName());
    ftype.setUmlKind(Expression.CLASSID);
    ftype.setEntity(f);
    ftype.setType(new Type("Set",null));
    ftype.setElementType(ft);

    BasicExpression id = new BasicExpression("$id");
    id.setType(new Type("String",null));
    id.setUmlKind(Expression.ATTRIBUTE);
    id.setEntity(e);

    BasicExpression fid = new BasicExpression("$id");
    fid.setType(new Type("String",null));
    fid.setUmlKind(Expression.ATTRIBUTE);
    fid.setEntity(f);
    fid.setObjectRef(fvar);

    Expression setid = 
        new BinaryExpression("=", fid, id);
    Expression fexp =
        new BinaryExpression(":",fvar,ftype);
    Expression succ = 
         new BinaryExpression("#",fexp,setid);
    Constraint con = new Constraint(ante,succ);
    con.setOwner(e);
    return con;
  }

  public Constraint toConstraint2(Entity e, Entity f, Vector ignoring)
  { // If no migrate rule for e, do conservative copy of features
    Type ft = new Type(f);
    Type et = new Type(e);
    BasicExpression selfexp = new BasicExpression("self");
    selfexp.setUmlKind(Expression.VARIABLE);
    selfexp.setEntity(e);
    selfexp.setType(et);
    selfexp.setElementType(et);

    Expression ante = guard.substituteEq("original", selfexp);
    Expression succ = new BasicExpression("true");
    String fname = evolvedType.toLowerCase();

    BasicExpression fvar = new BasicExpression(fname);
    fvar.setUmlKind(Expression.VARIABLE);
    fvar.setEntity(f);
    fvar.setType(ft);
    fvar.setElementType(ft);

    BasicExpression fid = new BasicExpression("$id");
    fid.setType(new Type("String",null));
    fid.setUmlKind(Expression.ATTRIBUTE);
    fid.setEntity(e);

    BasicExpression felem = new BasicExpression(f.getName());
    felem.setUmlKind(Expression.CLASSID);
    felem.setEntity(f);
    felem.setArrayIndex(fid);
    felem.setType(ft);
    felem.setElementType(ft);

    BinaryExpression feq = new BinaryExpression("=", fvar, felem);
    ante = new BinaryExpression("&", ante, feq);

    Vector eatts = e.allAttributes();
    Vector fatts = f.allAttributes();
    for (int i = 0; i < eatts.size(); i++)
    { Attribute eatt = (Attribute) eatts.get(i);
      String eattname = eatt.getName();
      // if ("$id".equals(eattname)) { continue; } 
      if (ignoring.contains(eattname)) { continue; } 

      Attribute fatt = (Attribute) ModelElement.lookupByName(eattname,fatts);
      if (fatt != null) 
      { // and types compatible, assignable 
        Type fattType = fatt.getType(); 
        Type eattType = eatt.getType(); 
        if (Type.isSubType(eattType,fattType)) 
        { BasicExpression evaratt = new BasicExpression(eattname);
          evaratt.setType(eattType);
          evaratt.setUmlKind(Expression.ATTRIBUTE);
          evaratt.setEntity(e);
          evaratt.setObjectRef(selfexp);

          BasicExpression fvaratt = new BasicExpression(eattname);
          fvaratt.setType(fattType);
          fvaratt.setUmlKind(Expression.ATTRIBUTE);
          fvaratt.setEntity(f);
          fvaratt.setObjectRef(fvar);

          Expression setatt = 
            new BinaryExpression("=", fvaratt, evaratt);
          succ = Expression.simplify("&",succ,setatt,new Vector());
        } 
      }
    }

    Vector easts = e.allAssociations();
    // Vector fasts = f.allAssociations();
    for (int i = 0; i < easts.size(); i++)
    { Association east = (Association) easts.get(i);
      String eastname = east.getRole2();
      Association fast = f.getDefinedRole(eastname);
      if (fast != null) 
      { // and types compatible, assignable 
        BasicExpression evarast = new BasicExpression(eastname);
        evarast.setType(east.getType2());
        Entity e2 = east.getEntity2(); 
        evarast.setUmlKind(Expression.ROLE);
        evarast.setEntity(e);
        evarast.setElementType(new Type(e2));
        evarast.setObjectRef(selfexp);

        BasicExpression fvarast = new BasicExpression(eastname);
        fvarast.setType(fast.getType2());
        fvarast.setUmlKind(Expression.ROLE);
        fvarast.setEntity(f);
        fvarast.setObjectRef(fvar);

        Entity fe2 = fast.getEntity2();
        fvarast.setElementType(new Type(fe2)); 

        BasicExpression equiv = new BasicExpression(fe2.getName());
        equiv.setUmlKind(Expression.CLASSID);
        BasicExpression evarastid = new BasicExpression("$id");
        evarastid.setType(new Type("String",null));
        evarastid.setUmlKind(Expression.ATTRIBUTE);
        evarastid.setObjectRef(evarast);
        evarastid.setEntity(east.getEntity2()); 

        equiv.setArrayIndex(evarastid);
        equiv.setType(fast.getType2());

        Expression setast = 
            new BinaryExpression("=", fvarast, equiv);
        succ = Expression.simplify("&",succ,setast,new Vector());
      }
    }
    // if ("true".equals(succ + "")) { return null; } 
    Constraint con = new Constraint(ante,succ);
    con.setOwner(e);
    return con;
  }


}
