import java.awt.*; 
import java.util.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public abstract class Transformation 
{ abstract ClassDiagram transform(ClassDiagram incd);

  public static void fillUsingMatching(Association ast, Vector constraints)
  { String a = ast.getEntity1().getName();
    String b = ast.getEntity2().getName();
    java.util.Map env = new HashMap();
    env.put(a,"ax");
    env.put(b,"bx");
    ArrayList conss = new ArrayList();
    String p = "";
    for (int i = 0; i < constraints.size(); i++)
    { Constraint cst = (Constraint) constraints.get(i);
      if (cst.onAssociation(ast))
      { conss.add(cst);
        p = p + cst.queryForm(env,false);
        if (i < constraints.size()-1)
        { p = p + " && "; }
      }
    }
    String prog = 
      Transformation.genABController(a,b,ast.getRole2(),p);
    System.out.println(prog);
  }

  public static String genABController(String a, String b,
                          String br, String p)
  { String result = "import java.util.*;\n\n" +
      "public class " + a + b + "Controller\n"  +
      "{ List as = new ArrayList();\n" +
      "  List bs = new ArrayList();\n" +
      "  List history = new ArrayList();\n" +
      "  List unassigned = new ArrayList();\n" +
      "  boolean failed = false;\n\n" +

      "  public " + a + b + "Controller(List asx, List bsx)\n" +
      "  { as = asx;\n" +
      "    bs = bsx;\n" +
      "    unassigned.addAll(asx);\n" +
      "  }\n\n" +

      "  public boolean assignAB()\n" +
      "  { while (unassigned.size() > 0 && !failed)\n" +
      "    { " + a + " ax = (" + a + ") unassigned.get(0);\n" +
      "      List possible = getPossibleBs(ax);\n" +
      "      if (possible.size() > 0)\n" +
      "      { " + b + " bx = (" + b + ") possible.get(0);\n" +
      "        ax.add" + br + "(bx);\n" +
      "        possible.remove(0);\n" +
      "        " + a + b + "Decision d = new " + a + b + "Decision(ax,bx,possible);\n" +
      "        unassigned.remove(0);\n" +
      "        history.add(d);\n" +
      "      }\n" +
      "      else\n" +
      "      { failed = redo(); }\n" +
      "    }\n" +
      "    return failed;\n" +
      "  }\n\n" +

      "  List getPossibleBs(" + a + " ax)\n" +
      "  { List result = new ArrayList();\n" +
      "    for (int i = 0; i < bs.size(); i++)\n" +
      "    { " + b + " bx = (" + b + ") bs.get(i);\n" +
      "      if (" + p + ")\n" +
      "      { result.add(bx); }\n" +
      "    }\n" +
      "    return result;\n" +
      "  }\n\n" +

      "  public boolean redo()\n" +
      "  { if (history.size() == 0)\n" + 
      "    { return true; }\n" +
      "    else \n" +
      "    { " + a + b + "Decision d = (" + a + b +
      "Decision) history.get(history.size()-1);\n" +
      "      if (d.isRedoable())\n" +
      "      { d.redo(); \n" +
      "        return false;\n" +
      "      }\n" +
      "      else\n" +
      "      { d.undo();\n" +
      "        unassigned.add(0,d.assignedTo);\n" +
      "        history.remove(d);\n" +
      "        return redo();\n" + 
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n\n" +

      "class " + a + b + "Decision\n" +
      "{ " + b + " choice;\n" +
      "  List alternatives = new ArrayList();\n" +
      "  " + a + " assignedTo;\n\n" +

      "  " + a + b + "Decision(" + a + " ax, " + b + " bx, List bsx)\n" +
      "  { choice = bx;\n" +
      "    assignedTo = ax;\n" +
      "    alternatives.addAll(bsx);\n" +
      "  }\n\n" +

      "  boolean isRedoable()\n" +
      "  { return alternatives.size() > 0; }\n\n" +

      "  void redo()\n" +
      "  { choice = (" + b + ") alternatives.get(0);\n" +
      "    assignedTo.add" + br + "(choice);\n" +
      "    alternatives.remove(0);\n" +
      "  }\n\n" +

      "  void undo()\n" +
      "  { assignedTo.remove" + br + "(choice); }\n" +
      "}\n";
    return result;
  }
}

class RemManyManyTransform extends Transformation
{ private Entity ent1;
  private Entity ent2;
  private Association oldast;

  RemManyManyTransform(Entity e1, Entity e2,
                       Association ast)
  { ent1 = e1;
    ent2 = e2;
    oldast = ast;
  }

  public ClassDiagram transform(ClassDiagram incd)
  { // remove oldast, introduce new entity between
    // ent1 and ent2, and 2 new associations
    // If constraint of ent1 refers to ent2r, 
    // replace by ent1ent2r.ent2r
    Entity e1 = oldast.getEntity1();
    Entity e2 = oldast.getEntity2();
    if (e1 != ent1 && e1 != ent2)
    { System.err.println("Association must link " +
                         "the entities!");
      return incd;
    }
    if (e2 != ent1 && e2 != ent2)
    { System.err.println("Association must link " +
                         "the entities!");
      return incd;
    }
    if (e1 == e2)
    { System.err.println("Cannot be self-association");
      return incd;
    }
    int c1 = oldast.getCard1();
    int c2 = oldast.getCard2();
    if (c1 != ModelElement.MANY ||
        c2 != ModelElement.MANY)
    { System.err.println("Association must be " +
                         "many-many");
      return incd;
    }
    String e1name = ent1.getName();
    String e2name = ent2.getName();
    String ename = e1name + e2name;
    Entity newent = new Entity(ename);
    RectData rd1 = (RectData) incd.getVisualDataOf(ent1);
    RectData rd2 = (RectData) incd.getVisualDataOf(ent2);
    if (rd1 == null || rd2 == null)
    { System.err.println("Can't find visuals of " +
                         e1name + " or " + e2name);
      return incd;
    }
    int x1 = rd1.getx();
    int y1 = rd1.gety();
    int x2 = rd2.getx();
    int y2 = rd2.gety();
    int xx = (x1 + x2)/2;
    int yy = (y1 + y2)/2;
    RectData rd = 
      new RectData(xx,yy,Color.black,UCDArea.SOLID,0);
    rd.setLabel(ename);
    incd.addEntity(newent);
    incd.addVisual(rd);
    rd.setModelElement(newent);
    // assume e1 == ent1 && e2 == ent2
    Association as1 =
      new Association(e1,newent,ModelElement.ONE,
                      ModelElement.MANY,
                      oldast.getRole1(),
                      ename + "r");
    Association as2 =
      new Association(newent,e2,ModelElement.MANY,
                      ModelElement.ONE,ename + "r",
                      oldast.getRole2());
    incd.addAssociation(as1);
    incd.addAssociation(as2);
    e1.removeAssociation(oldast);
    e1.addAssociation(as1);
    newent.addAssociation(as2);
    LineData l1 = 
      new LineData(x1+40,y1+10,xx+5,yy+10,
                   0,0);
    l1.setModelElement(as1);
    incd.addVisual(l1);
    LineData l2 =
      new LineData(xx+40,yy+10,x2+5,y2+10,
                   0,0);
    l2.setModelElement(as2);
    incd.addVisual(l2);
    return incd;
  }
}

