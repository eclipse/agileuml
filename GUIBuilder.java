import java.util.*; 

/******************************
* Copyright (c) 2003--2021 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: Utilities */ 

public class GUIBuilder
{ // parses list of constraints, converts them to object
  // specs, then to Java code.
  private Compiler comp = new Compiler();

  public List parseAll(List cons)
  { List res = new ArrayList();
    for (int i = 0; i < cons.size(); i++)
    { String con = (String) cons.get(i);
      comp.lexicalanalysis(con);
      Expression e = comp.parse();
      if (e == null)
      { System.out.println("error in expression: " + con); }
      else
      { res.add(e); }
    }
    return res;
  }

  public List objectSpecs(List cons)
  { List res = new ArrayList();
    List statements = new ArrayList(); 

    for (int i = 0; i < cons.size(); i++)
    { Expression e = (Expression) cons.get(i);
      if (e instanceof BinaryExpression)
      { BinaryExpression be = (BinaryExpression) e;
        Expression bl = be.left;
        Expression br = be.right;
        String op = be.operator;
        if (bl instanceof BasicExpression && bl != null)
        { BasicExpression bel = (BasicExpression) bl;
          if (op.equals(":"))
          { ModelElement lob = ModelElement.lookupByName(bl + "",res);
            ModelElement rob = ModelElement.lookupByName(br + "",res);
            if (rob == null)  // br is a type
            { ObjectSpecification obj = 
                 new ObjectSpecification(bel + "",br + "");
              res.add(obj);
            }
            else if (lob != null && lob instanceof ObjectSpecification &&
	             rob instanceof ObjectSpecification)
            { ObjectSpecification ros = (ObjectSpecification) rob;
	      ros.addelement((ObjectSpecification) lob); 
	    }
          }
          else if (op.equals("=") && bel.objectRef instanceof BasicExpression)
          { BasicExpression objnme = (BasicExpression) bel.objectRef;
            ModelElement obj = ModelElement.lookupByName(objnme + "",res);
            if (obj != null && obj instanceof ObjectSpecification)
            { ObjectSpecification os = (ObjectSpecification) obj;
              os.addAttribute(bel.data,br + "");
            }
          }
        }
      }
      else 
      { statements.add(e); } 
    }
    return res;
  }

  public List getCommands(String frme, List objs)
  { List res = new ArrayList();
    ModelElement obj = ModelElement.lookupByName(frme,objs);
    if (obj != null && obj instanceof ObjectSpecification)
    { ObjectSpecification os = (ObjectSpecification) obj;
      List atts = os.getatts();
      for (int i = 0; i < atts.size(); i++)
      { String att = (String) atts.get(i);
        String val = os.getattvalue(att);
        // if an object, add if a button or menuitem:
        ModelElement valobj = 
            ModelElement.lookupByName(val,objs);
        if (valobj != null && 
            valobj instanceof ObjectSpecification)
        { ObjectSpecification valos = (ObjectSpecification) valobj;
          String oc = valos.objectClass;
          if (oc.equals("Button") || oc.equals("MenuItem"))
          { res.add(valos); }
          else
          { res.addAll(getCommands(val,objs)); }
        }
      } 
    }
    return res;
  }

  public List getAllCommands(List objs)
  { List res = new ArrayList();
    for (int i = 0; i < objs.size(); i++)
    { if (objs.get(i) instanceof ObjectSpecification)
      { ObjectSpecification obj = (ObjectSpecification) objs.get(i);
        String oc = obj.objectClass;
        if (oc.equals("Button") || oc.equals("MenuItem"))
        { res.add(obj); }
      }
    }
    return res;
  }

  public void buildMainFrame(ObjectSpecification frme,
         String nme, List objs, String f, List eops)
  { JavaClass jc = new JavaClass(nme); 
    jc.setextendsList("JFrame");
    jc.addimplementsList("ActionListener");
    // add attribute for each direct component of frame
    List components = (List) ((ArrayList) objs).clone();
      // frme.getallcomponents(objs);
    components.remove(frme); 

    String dec = "";
    for (int i = 0; i < components.size(); i++)
    { ObjectSpecification comp = 
        (ObjectSpecification) components.get(i);
      dec = dec + "  " + comp.getDeclaration() + "\n";
    }
    jc.setdeclaration(dec);

    JavaOperation cnst = new JavaOperation(nme);
    String val = frme.getattvalue("title");
    String defn = "    super(\"" + val.substring(1,val.length()-1) + "\");\n";
    defn = defn + 
       "    Container " + f + 
       "_container = getContentPane();\n";

    for (int i = 0; i < components.size(); i++)
    { ObjectSpecification comp = 
        (ObjectSpecification) components.get(i);
      defn = defn + "    " + comp.getDefinition() + "\n";
    }

    if (frme.hasAttribute("menubar"))
    { String mobj = frme.getattvalue("menubar");
      defn = defn + "    setJMenuBar(" + mobj + ");\n";
    }
    if (frme.hasAttribute("north"))
    { String nobj = frme.getattvalue("north");
      defn = defn + "    " + f + "_container.add(" +
        nobj + ",BorderLayout.NORTH);\n";
    }
    if (frme.hasAttribute("east"))
    { String eobj = frme.getattvalue("east");
      defn = defn + "    " + f + "_container.add(" +
        eobj + ",BorderLayout.EAST);\n";
    }
    if (frme.hasAttribute("center"))
    { String cobj = frme.getattvalue("center");
      defn = defn + "    " + f + "_container.add(" +
        cobj + ",BorderLayout.CENTER);\n";
    }
    if (frme.hasAttribute("west"))
    { String wobj = frme.getattvalue("west");
      defn = defn + "    " + f + "_container.add(" +
        wobj + ",BorderLayout.WEST);\n";
    }
    if (frme.hasAttribute("south"))
    { String sobj = frme.getattvalue("south");
      defn = defn + "    " + f + "_container.add(" +
        sobj + ",BorderLayout.SOUTH);\n";
    }

    cnst.setdefn(defn);
    jc.setconstructor(cnst);

    JavaOperation acP = 
      new JavaOperation("actionPerformed");
    acP.setinpars("ActionEvent e");
    acP.setoutpars("void");
    String acPdefn = "if (e == null)\n" +
               "    { return; }\n" +
               "    String cmd = e.getActionCommand();\n";
    List commands = getAllCommands(objs);
    for (int j = 0; j < commands.size(); j++)
    { ObjectSpecification co = (ObjectSpecification) commands.get(j);
      String btext = co.getattvalue("text");
      String comm = co.getattvalue("command");
      if (btext != null && comm != null && 
          comm.length() > 2)
      { String cmm = comm.substring(1,comm.length()-1); 
        acPdefn = acPdefn +
            "    if (" + btext + ".equals(cmd))\n" +
            "    { " + cmm + "(); }\n";
        JavaOperation iop = new JavaOperation(cmm);
        iop.setinpars(""); 
        iop.setoutpars("void"); 
        if (eops.contains(cmm)) // an entity operation
        { String edialog = "C" + cmm + "Dialog"; 
          String opendialog = "    " + edialog + " " + cmm + " = new " + edialog +
                              "(this);\n" + 
            "    " + edialog + ".setLocationRelativeTo(this);\n" + 
            "    " + edialog + ".pack();\n" + 
            "    " + edialog + ".setVisible(true);\n"; 
          iop.setdefn(opendialog); 
        }
        else 
        { iop.setdefn("System.out.println(" + comm + ");"); }
        jc.addoperations(iop);  
      }
    }
    acP.setdefn(acPdefn);
    jc.addoperations(acP);
    String maindef = 
       nme + " " + f + " = new " + nme + "();\n" +
       "    " + f + ".setSize(400,400);\n" +
       "    " + f + ".setVisible(true);";
    jc.setmaindef(maindef);
    System.out.println("import javax.swing.*;"); 
    System.out.println("import javax.swing.event.*;"); 
    System.out.println("import java.awt.*;"); 
    System.out.println("import java.awt.event.*;\n\n"); 

    System.out.println(jc);
  }

  public static String buildUCGUI(Vector ucs, String sysName, boolean incr)
  { String res = "import javax.swing.*;\n" +
      "import javax.swing.event.*;\n" +
      "import java.awt.*;\n" +
      "import java.awt.event.*;\n" + 
      "import java.util.Vector;\n" + 
      "import java.io.*;\n" + 
      "import java.util.StringTokenizer;\n\n";  

    String contname = "Controller"; 

    if (sysName == null || sysName.length() == 0) { } 
    else 
    { contname = sysName + "." + contname; } 

    res = res +
      "public class GUI extends JFrame implements ActionListener\n" +
      "{ JPanel panel = new JPanel();\n" +
      "  JPanel tPanel = new JPanel();\n" +
      "  JPanel cPanel = new JPanel();\n" +
      "  " + contname + " cont = " + contname + ".inst();\n";

    String cons = " public GUI()\n" +
      "  { super(\"Select use case to execute\");\n" +
      "    panel.setLayout(new BorderLayout());\n" + 
      "    panel.add(tPanel, BorderLayout.NORTH);\n" +  
      "    panel.add(cPanel, BorderLayout.CENTER);\n" +  
      "    setContentPane(panel);\n" + 
      "    addWindowListener(new WindowAdapter() \n" + 
      "    { public void windowClosing(WindowEvent e)\n" +  
      "      { System.exit(0); } });\n";

    String aper = "  public void actionPerformed(ActionEvent e)\n" +
      "  { if (e == null) { return; }\n" +
      "    String cmd = e.getActionCommand();\n";

    res = res + "    JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "    tPanel.add(loadModelButton);\n" +
        "    loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "    JButton saveModelButton = new JButton(\"saveModel\");\n";

    res = res + "    JButton loadXmiButton = new JButton(\"loadXmi\");\n";


    cons = cons + 
        "    tPanel.add(saveModelButton);\n" +
        "    saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
         "      cont.saveXSI(\"xsi.txt\"); \n" + 
         "      return;\n" + 
		 "    } \n";

    cons = cons + 
        "    tPanel.add(loadXmiButton);\n" +
        "    loadXmiButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"loadXmi\".equals(cmd))\n" +
         "    { cont.loadXSI();  \n" + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton loadCSVButton = new JButton(\"loadCSVs\");\n";

    cons = cons + 
        "    tPanel.add(loadCSVButton);\n" +
        "    loadCSVButton.addActionListener(this);\n";
      
    String loadcsvop = "    { " + contname + ".loadCSVModel();\n";   
            
    aper = aper +
         "    if (\"loadCSVs\".equals(cmd))\n" + loadcsvop + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveCSVButton = new JButton(\"saveCSVs\");\n";

    cons = cons + 
        "    tPanel.add(saveCSVButton);\n" +
        "    saveCSVButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveCSVs\".equals(cmd))\n" +
         "    { cont.saveCSVModel();  \n" + 
         "      return; } \n";

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 

        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        Type resultType = uc.getResultType(); 

        String parstring = ""; 
        String parnames = uc.getParameterNames(); 

        String prompt = ""; 
        if (pars.size() > 0)
        { prompt = "String _vals = JOptionPane.showInputDialog(\"Enter parameters " + parnames + ":\");\n" +                                 "    Vector _values = new Vector();\n" + 
                 "    StringTokenizer _st = new StringTokenizer(_vals, \" ,\");\n" +  
                 "    while (_st.hasMoreTokens())\n" + 
                 "    { String _se = _st.nextToken().trim();\n" + 
                 "      _values.add(_se); \n" + 
                 "    }\n\n"; 


          for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName(); 
            parstring = parstring + parnme; 
            if (j < pars.size() - 1)
            { parstring = parstring + ","; } 

            Type typ = par.getType(); 
            if ("int".equals(typ + ""))
            { prompt = prompt + 
                "    int " + parnme + 
                  " = Integer.parseInt((String) _values.get(" + j + "));\n"; 
            } 
            else if ("long".equals(typ + ""))
            { prompt = prompt + 
                "    long " + parnme + 
                  " = Long.parseLong((String) _values.get(" + j + "));\n"; 
            } 
            else if ("double".equals(typ + ""))
            { prompt = prompt + 
                "    double " + parnme + 
                  " = Double.parseDouble((String) _values.get(" + j + "));\n"; 
            } 
            else if (typ != null && typ.isEnumeration())
            { String tname = typ.getName(); 
              prompt = prompt + 
                "    int " + parnme + 
                  " = SystemTypes.parse" + tname + "((String) _values.get(" + j + "));\n"; 
            } 
            else if (typ != null && typ.isEntity())
            { Entity ent = typ.getEntity();
              String ename = ent.getName().toLowerCase();  
              Attribute pk = ent.getPrincipalPrimaryKey(); 
              // arguments are supplied in the GUI as strings
              if (pk != null)
              { String indexname = ename + pk.getName() + "index"; 
                prompt = prompt + 
                "    " + ent.getName() + " " + parnme + 
                  " = (" + ent.getName() + ") Controller.inst()." + indexname + ".get((String) _values.get(" + j + "));\n"; 
              }
            }
            else if (typ.isCollectionType()) // Only collections of basic types: numerics and strings
            { if (typ.getName().equals("Map"))
              { System.out.println("!! Warning: map parameters cannot be instantiated from the GUI!"); }
			
              String convertElementType = "typ_" + parnme; 
              Type elemTyp = typ.getElementType(); 
              if (elemTyp == null) { } 
              else if ("int".equals(elemTyp.getName()))
              { convertElementType = "new Integer(Integer.parseInt(typ_" + parnme + "))"; } 
              else if ("long".equals(elemTyp.getName()))
              { convertElementType = "new Long(Long.parseLong(typ_" + parnme + "))"; } 
              else if ("double".equals(elemTyp.getName()))
              { convertElementType = "new Double(Double.parseDouble(typ_" + parnme + "))"; } 

              prompt = prompt + 
                "    java.util.List " + parnme + " = new Vector();\n" + 
                "    int " + parnme + "j = " + j + ";\n" +
                "    String typ_" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
                "    " + parnme + "j++; typ_" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
                "    while (!typ_" + parnme + ".equals(\"}\") && " + parnme + "j < _values.size())\n" + 
                "    { " + parnme + ".add(" + convertElementType + "); \n" + 
                "      " + parnme + "j++;\n" + 
                "      typ_" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
                "    }\n"; 
            } 
            else // if ("boolean".equals(typ + ""))
            { prompt = prompt + 
                "    String " + parnme + " = (String) _values.get(" + j + ");\n"; 
            }
          }  
        }
      
    
        res = res + "  JButton " + nme + "Button = new JButton(\"" + nme + "\");\n";
 
        cons = cons + 
          "  cPanel.add(" + nme + "Button);\n" +
          "  " + nme + "Button.addActionListener(this);\n";
      
        String call = " cont." + nme + "(" + parstring + ") "; 
        if (resultType != null) 
        { call = " System.out.println( " + call + " ) "; } 
      
        aper = aper +
           "    if (\"" + nme + "\".equals(cmd))\n" +
           "    { " + prompt + call + ";  return; } \n";
      }
    } 
    cons = cons + "  }\n\n";
    aper = aper + "  }\n\n";
    res = res + "\n" + cons + aper +
      "  public static void main(String[] args)\n" +
      "  { GUI gui = new GUI();\n" +
      "    gui.setSize(550,400);\n" +
      "    gui.setVisible(true);\n" +
      "  }\n }";
    return res;
  }

  public static String buildTestsGUIJava6(Vector ucs, String sysName, boolean incr, Vector types, Vector entities)
  { String res = "import javax.swing.*;\n" +
      "import javax.swing.event.*;\n" +
      "import java.awt.*;\n" +
      "import java.awt.event.*;\n" + 
      "import java.util.Vector;\n" + 
      "import java.util.Collection;\n" + 
      "import java.util.Map;\n" + 
      "import java.util.HashMap;\n" + 
      "import java.util.List;\n" + 
      "import java.util.HashSet;\n" + 
      "import java.util.ArrayList;\n" + 
      "import java.io.*;\n" + 
      "import java.util.StringTokenizer;\n\n";  

    String contname = "Controller"; 

    if (sysName == null || sysName.length() == 0) { } 
    else 
    { contname = sysName + "." + contname; } 

    String mutationTestsOp = ""; 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i);
      if (ent.isDerived() || ent.isComponent()) 
      { continue; }  
      String ename = ent.getName();
      String es = ename.toLowerCase() + "s"; 
      Vector eops = ent.getOperations();  
      
      for (int j = 0; j < eops.size(); j++) 
      { BehaviouralFeature bf = (BehaviouralFeature) eops.get(j); 
        if (bf.isMutatable())
        { String bfname = bf.getName();  
          mutationTestsOp = mutationTestsOp + 
          "      int[] " + es + "_" + bfname + "_counts = new int[100]; \n" +   
          "      int[] " + es + "_" + bfname + "_totals = new int[100]; \n" +   
          "      for (int _i = 0; _i < Controller.inst()." + es + ".size(); _i++)\n" + 
          "      { " + ename + " _ex = (" + ename + ") Controller.inst()." + es + ".get(_i);\n" +  
          "        MutationTest." + bfname + "_mutation_tests(_ex," + es + "_" + bfname + "_counts, " + es + "_" + bfname + "_totals);\n" + 
          "      }\n" + 
          "      System.out.println();\n";  
        } 
      }
    } 


    res = res +
      "public class TestsGUI extends JFrame implements ActionListener\n" +
      "{ JPanel panel = new JPanel();\n" +
      "  " + contname + " cont = " + contname + ".inst();\n";

    String cons = " public TestsGUI()\n" +
      "  { super(\"Select use case to test\");\n" +
      "    setContentPane(panel);\n" + 
      "    addWindowListener(new WindowAdapter() \n" + 
      "    { public void windowClosing(WindowEvent e)\n" +  
      "      { System.exit(0); } });\n";

    String aper = "  public void actionPerformed(ActionEvent e)\n" +
      "  { if (e == null) { return; }\n" +
      "    String cmd = e.getActionCommand();\n";

    res = res + "  JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "    panel.add(loadModelButton);\n" +
        "    loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveModelButton = new JButton(\"saveModel\");\n";

    cons = cons + 
        "    panel.add(saveModelButton);\n" +
        "    saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
    //         "      cont.saveXSI(\"xsi.txt\"); \n" + 
         "      return; } \n";

    res = res + "    JButton loadCSVButton = new JButton(\"Mutation Tests\");\n";

    cons = cons + 
         "    panel.add(loadCSVButton);\n" +
         "    loadCSVButton.addActionListener(this);\n";
                  
    aper = aper +
         "    if (\"Mutation Tests\".equals(cmd))\n" + 
         "    { System.err.println(\"Mutation tests\");\n" +
         mutationTestsOp +  
         "      return;\n" + 
         "    } \n";

    aper = aper + 
         "    int[] intTestValues = {0, -1, 1, 2147483647, -2147483648};\n" + 
         "    long[] longTestValues = {0, -1, 1, " + Long.MAX_VALUE + "L, " + Long.MIN_VALUE + "L};\n" + 
         "    double[] doubleTestValues = {0, -1, 1, " + Double.MAX_VALUE + ", " + Double.MIN_VALUE + "};\n" +
         "    boolean[] booleanTestValues = {false, true};\n" + 
         "    String[] stringTestValues = {\"\", \" abc_XZ \", \"#�$* &~@'\"};\n";

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        if (pars.size() > 0)
        { java.util.Map upperBounds = new java.util.HashMap(); 
          java.util.Map lowerBounds = new java.util.HashMap(); 
	
          Vector bounds = new Vector(); 
          java.util.Map aBounds = new java.util.HashMap(); 
      
          for (int k = 0; k < uc.preconditions.size(); k++) 
          { Constraint con = (Constraint) uc.preconditions.get(k);
            Expression pre = con.succedent();  
            pre.getParameterBounds(pars,bounds,aBounds);
   
            Expression.identifyUpperBounds(pars,aBounds,upperBounds); 
            Expression.identifyLowerBounds(pars,aBounds,lowerBounds); 
          } 
		  
          for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            Type typ = par.getType(); 
            String tname = typ + ""; 
            Vector testassignments = par.testValues("parameters", lowerBounds, upperBounds);
			
            if ("int".equals(tname))
            { aper = aper + 
			     "    int[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";  
            } 
            else if ("long".equals(tname))
            { aper = aper + 
                     "    long[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";    
            } 
            else if ("double".equals(tname))
            { aper = aper + 
                 "    double[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";  
            } 
          }
        }
      }
    }
	 
    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
        String indent = "    "; 
        
        String checkCode = ""; 
		
        
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        Type resultType = uc.getResultType(); 
        String testscript = "";
        String teststring = ""; 
        String posttests = ""; 

        res = res + "    JButton " + nme + "Button = new JButton(\"" + nme + "\");\n";
 
        cons = cons + 
          "    panel.add(" + nme + "Button);\n" +
          "    " + nme + "Button.addActionListener(this);\n";

        String parstring = ""; 
        String parnames = uc.getParameterNames(); 

				
        if (pars.size() > 0)
        { for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            String indexvar = "_index_" + j; 
            indent = indent + "  "; 
			 
            parstring = parstring + parnme; 
            teststring = teststring + "\"" + parnme + " = \" + " + parnme; 

            if (j < pars.size() - 1)
            { parstring = parstring + ","; 
              teststring = teststring + " + \"; \" + "; 
            } 

            Type typ = par.getType(); 
            String tname = typ + ""; 
			
            if ("int".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ int " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("long".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ long " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("double".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ double " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("boolean".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 2; " + indexvar + "++)\n" + 
                  indent + "{ boolean " + parnme + " = booleanTestValues[" + indexvar + "];\n";  
            } 
            else if ("String".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                  indent + "{ String " + parnme + " = stringTestValues[" + indexvar + "];\n";  
            } 
            else if (typ != null && typ.isEnumeration())
            { Vector vals = typ.getValues(); 
              int nvals = vals.size(); 
              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nvals + "; " + indexvar + "++)\n" + 
                  indent + "{ int " + parnme + " = " + indexvar + ";\n";
            } 
            else if (typ.isEntity())
            { String ename = tname.toLowerCase() + "s";
              Entity ee = typ.getEntity(); 
			   
              Vector eeinvariants = new Vector(); 
              if (ee != null) 
              { eeinvariants.addAll(ee.getInvariants()); }

              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < Controller.inst()." + ename + ".size(); " + indexvar + "++)\n" + 
                  indent + "{ " + tname + " " + parnme + " = (" + tname + ") Controller.inst()." + ename + ".get(" + indexvar + ");\n";
              for (int p = 0; p < eeinvariants.size(); p++)
              { java.util.Map env = new java.util.HashMap(); 
                env.put(tname, parnme); 
                Constraint conp = (Constraint) eeinvariants.get(p); 
                Constraint conpr = conp.addReference(parnme,typ); 
                String constring = conpr.queryFormJava6(env,true); 
				
                testscript = testscript + 
		          indent + "  if (" + constring + ")\n" + 
		          indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at start; \"); }\n" + 
                     indent + "  else \n" + 
                     indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at start; \"); }\n"; 
                posttests = posttests + 
		          indent + "  if (" + constring + ")\n" + 
		          indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at end; \"); }\n" + 
                     indent + "  else \n" + 
                     indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at end; \"); }\n"; 
              }
				  		  
            } // Check invariants of parnme. 
            else if (typ.isCollectionType()) // Try with empty list, singleton & random list
            { Type elemTyp = typ.getElementType();
              String collType = "ArrayList"; 
              if (typ.isSequence())
              { } 
              else 
              { collType = "HashSet"; } 
 
              if (elemTyp == null) { } 
              else if ("int".equals(elemTyp.getName()))
              { testscript = testscript + 
                      indent + "\n" + 
				indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
				indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
				indent + "  if (" + indexvar + " < 5)\n" + 
				indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + "])); }\n" + 
				indent + "  if (" + indexvar + " < 3)\n" + 
				indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + " + 2])); }\n";  
              } 
              else if ("long".equals(elemTyp.getName()))
              { testscript = testscript + 
                      indent + "\n" + 
				indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
				indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
				indent + "  if (" + indexvar + " < 5)\n" + 
				indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + "])); }\n" +
				indent + "  if (" + indexvar + " < 3)\n" + 
				indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + " + 2])); }\n"; 
              }  
              else if ("double".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 5)\n" + 
                  indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + "])); }\n" + 
                  indent + "  if (" + indexvar + " < 3)\n" + 
                  indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + " + 2])); }\n";
              }
              else if ("boolean".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 2)\n" + 
                  indent + "  { " + parnme + ".add(new Boolean(booleanTestValues[" + indexvar + "])); }\n";
              } 
              else if ("String".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 4; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 3)\n" + 
                  indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + "]); }\n" + 
                  indent + "  if (" + indexvar + " < 2)\n" + 
                  indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + " + 1]); }\n";
              } 
              else if (elemTyp != null && elemTyp.isEnumeration())
              { Vector vals = elemTyp.getValues(); 
                int nvals = vals.size(); 
                testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " <= " + nvals + "; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < " + nvals + ")\n" + 
                  indent + "  { " + parnme + ".add(new Integer(" + indexvar + ")); }\n"; 
              }
              else if (elemTyp.isEntity())
              { String etname = elemTyp.getName(); 
			    // Entity ee = elemTyp.getEntity(); 
				
                String eename = etname.toLowerCase() + "s"; 
			    
                testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " <= Controller.inst()." + etname + ".size(); " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n";  
                testscript = testscript + 
                  indent + "  if (" + indexvar + " < Controller.inst()." + etname + ".size())\n" + 
                  indent + "  { " + parnme + ".add(Controller.inst()." + eename + ".get(" + indexvar + ")); }\n";
              }  
            }  
          }  
        }
      
        for (int j = 0; j < uc.preconditions.size(); j++)
        { java.util.Map env = new java.util.HashMap(); 
          Constraint con = (Constraint) uc.preconditions.get(j); 
          checkCode = checkCode + 
		              indent + "  if (" + con.queryFormJava6(env,true) + ")\n" + 
		              indent + "  { System.out.print(\" Precondition " + j + " is valid; \"); }\n" + 
                         indent + "  else \n" + 
                         indent + "  { System.out.print(\" Precondition " + j + " fails; \"); }\n"; 
        }

      
        String call = " cont." + nme + "(" + parstring + ")"; 
        if (resultType != null) 
        { call = " System.out.println(\" ==> Result: \" + " + call + " )"; } 
		
		call = indent + "  try { " + call + ";\n" + 
		       indent + "  }\n" + 
                  indent + "  catch (Throwable _e) { System.out.println(\" !! Exception occurred: test failed !! \"); }\n"; 
		
           if (teststring.equals(""))
           { teststring = "\"\""; } 
	   
		testscript = testscript + 
             indent + "  System.out.println();\n" + 
             indent + "  System.out.print(\">>> Test: \" + " + teststring + ");\n" + 
             checkCode + 
             call + posttests + "\n" + 
             indent + "  System.out.println();\n" + 
             indent + "  System.out.println();\n"; 
        // indent = indent.substring(0,indent.length()-2);  
		
		for (int p = 0; p < pars.size(); p++) 
		{ testscript = testscript + 
		               indent + "}\n";
		  indent = indent.substring(0,indent.length()-2);  
		}
      
        aper = aper +
           "    if (\"" + nme + "\".equals(cmd))\n" +
           "    { " + testscript + "\n" + 
           "      return;\n" + 
           "    } \n";
      }
    } 
	
    cons = cons + "  }\n\n";
    aper = aper + "  }\n\n";
    res = res + "\n" + cons + aper +
      "  public static void main(String[] args)\n" +
      "  { TestsGUI gui = new TestsGUI();\n" +
      "    gui.setSize(550,400);\n" +
      "    gui.setVisible(true);\n" +
      "  }\n }";
    return res;
  }

  public static String buildTestsGUIJava8(Vector ucs, String sysName, boolean incr, Vector types, Vector entities)
  { String res = "import javax.swing.*;\n" +
      "import javax.swing.event.*;\n" +
      "import java.awt.*;\n" +
      "import java.awt.event.*;\n" + 
      "import java.util.Vector;\n" + 
      "import java.util.Collection;\n" + 
      "import java.util.Collections;\n" + 
      "import java.util.Map;\n" + 
      "import java.util.HashMap;\n" + 
      "import java.util.List;\n" + 
      "import java.util.HashSet;\n" + 
      "import java.util.ArrayList;\n" + 
      "import java.io.*;\n" + 
      "import java.util.StringTokenizer;\n\n";  

    String contname = "ModelFacade"; 

    if (sysName == null || sysName.length() == 0) { } 
    else 
    { contname = sysName + "." + contname; } 

    String mutationTestsOp = ""; 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i);
      if (ent.isDerived() || ent.isComponent()) 
      { continue; }  
      String ename = ent.getName();
      String es = ename.toLowerCase(); 
      String einstances = ename + "." + ename + "_allInstances"; 
      Vector eops = ent.getOperations();  
      
      for (int j = 0; j < eops.size(); j++) 
      { BehaviouralFeature bf = (BehaviouralFeature) eops.get(j); 
        if (bf.isMutatable())
        { String bfname = bf.getName();  
          mutationTestsOp = mutationTestsOp + 
          "      int[] " + es + "_" + bfname + "_counts = new int[100]; \n" +   
          "      int[] " + es + "_" + bfname + "_totals = new int[100]; \n" +   
          "      for (int _i = 0; _i < " + einstances + ".size(); _i++)\n" + 
          "      { " + ename + " _ex = (" + ename + ") " + einstances + ".get(_i);\n" +  
          "        MutationTest." + bfname + "_mutation_tests(_ex," + es + "_" + bfname + "_counts, " + es + "_" + bfname + "_totals);\n" + 
          "      }\n" + 
          "      System.out.println();\n";  
        } 
      }
    } 


    res = res +
      "public class TestsGUI extends JFrame implements ActionListener\n" +
      "{ JPanel panel = new JPanel();\n" +
      "  " + contname + " cont = " + contname + ".getInstance();\n";

    String cons = " public TestsGUI()\n" +
      "  { super(\"Select use case to test\");\n" +
      "    setContentPane(panel);\n" + 
      "    addWindowListener(new WindowAdapter() \n" + 
      "    { public void windowClosing(WindowEvent e)\n" +  
      "      { System.exit(0); } });\n";

    String aper = "  public void actionPerformed(ActionEvent e)\n" +
      "  { if (e == null) { return; }\n" +
      "    String cmd = e.getActionCommand();\n";

    /* res = res + "  JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "    panel.add(loadModelButton);\n" +
        "    loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveModelButton = new JButton(\"saveModel\");\n";

    cons = cons + 
        "    panel.add(saveModelButton);\n" +
        "    saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
         "      return; } \n";  */ 

    res = res + "    JButton loadCSVButton = new JButton(\"Mutation Tests\");\n";

    cons = cons + 
         "    panel.add(loadCSVButton);\n" +
         "    loadCSVButton.addActionListener(this);\n";
                  
    aper = aper +
         "    if (\"Mutation Tests\".equals(cmd))\n" + 
         "    { System.err.println(\"Mutation tests\");\n" +
         mutationTestsOp +  
         "      return;\n" + 
         "    } \n";

    aper = aper + 
         "    int[] intTestValues = {0, -1, 1, 2147483647, -2147483648};\n" + 
         "    long[] longTestValues = {0, -1, 1, " + Long.MAX_VALUE + "L, " + Long.MIN_VALUE + "L};\n" + 
         "    double[] doubleTestValues = {0, -1, 1, " + Double.MAX_VALUE + ", " + Double.MIN_VALUE + "};\n" +
         "    boolean[] booleanTestValues = {false, true};\n" + 
         "    String[] stringTestValues = {\"\", \" abc_XZ \", \"#�$* &~@'\"};\n";

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        if (pars.size() > 0)
        { java.util.Map upperBounds = new java.util.HashMap(); 
          java.util.Map lowerBounds = new java.util.HashMap(); 
	
          Vector bounds = new Vector(); 
          java.util.Map aBounds = new java.util.HashMap(); 
      
          for (int k = 0; k < uc.preconditions.size(); k++) 
          { Constraint con = (Constraint) uc.preconditions.get(k);
            Expression pre = con.succedent();  
            pre.getParameterBounds(pars,bounds,aBounds);
   
            Expression.identifyUpperBounds(pars,aBounds,upperBounds); 
            Expression.identifyLowerBounds(pars,aBounds,lowerBounds); 
          } 
		  
          for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            Type typ = par.getType(); 
            String tname = typ + ""; 
            Vector testassignments = par.testValues("parameters", lowerBounds, upperBounds);
			
            if ("int".equals(tname))
            { aper = aper + 
			     "    int[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";  
            } 
            else if ("long".equals(tname))
            { aper = aper + 
                     "    long[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";    
            } 
            else if ("double".equals(tname))
            { aper = aper + 
                 "    double[] " + nme + par + "TestValues = {";
              for (int y = 0; y < testassignments.size(); y++) 
              { String tval = (String) testassignments.get(y); 
                aper = aper + tval; 
                if (y < testassignments.size() - 1) 
                { aper = aper + ", "; }
              }
              aper = aper + "};\n";  
            } 
          }
        }
      }
    }
	 
    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
        String indent = "    "; 
        
        String checkCode = ""; 
		
        
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        Type resultType = uc.getResultType(); 
        String testscript = "";
        String teststring = ""; 
        String posttests = ""; 

        res = res + "    JButton " + nme + "Button = new JButton(\"" + nme + "\");\n";
 
        cons = cons + 
          "    panel.add(" + nme + "Button);\n" +
          "    " + nme + "Button.addActionListener(this);\n";

        String parstring = ""; 
        String parnames = uc.getParameterNames(); 

				
        if (pars.size() > 0)
        { for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            String indexvar = "_index_" + j; 
            indent = indent + "  "; 
			 
            parstring = parstring + parnme; 
            teststring = teststring + "\"" + parnme + " = \" + " + parnme; 

            if (j < pars.size() - 1)
            { parstring = parstring + ","; 
              teststring = teststring + " + \"; \" + "; 
            } 

            Type typ = par.getType(); 
            String tname = typ + ""; 
			
            if ("int".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ int " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("long".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ long " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("double".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ double " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("boolean".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 2; " + indexvar + "++)\n" + 
                  indent + "{ boolean " + parnme + " = booleanTestValues[" + indexvar + "];\n";  
            } 
            else if ("String".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                  indent + "{ String " + parnme + " = stringTestValues[" + indexvar + "];\n";  
            } 
            else if (typ != null && typ.isEnumeration())
            { Vector vals = typ.getValues(); 
              int nvals = vals.size(); 
              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nvals + "; " + indexvar + "++)\n" + 
                  indent + "{ int " + parnme + " = " + indexvar + ";\n";
            } 
            else if (typ.isEntity())
            { String ename = tname.toLowerCase() + "s";
              String einstances = tname + "." + tname + "_allInstances";
              Entity ee = typ.getEntity(); 
			   
              Vector eeinvariants = new Vector(); 
              if (ee != null) 
              { eeinvariants.addAll(ee.getInvariants()); }

              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + einstances + ".size(); " + indexvar + "++)\n" + 
                  indent + "{ " + tname + " " + parnme + " = (" + tname + ") " + einstances + ".get(" + indexvar + ");\n";
              for (int p = 0; p < eeinvariants.size(); p++)
              { java.util.Map env = new java.util.HashMap(); 
                env.put(tname, parnme); 
                Constraint conp = (Constraint) eeinvariants.get(p); 
                Constraint conpr = conp.addReference(parnme,typ); 
                String constring = conpr.queryFormJava7(env,true); 
				
                testscript = testscript + 
		          indent + "  if (" + constring + ")\n" + 
		          indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at start; \"); }\n" + 
                     indent + "  else \n" + 
                     indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at start; \"); }\n"; 
                posttests = posttests + 
		          indent + "  if (" + constring + ")\n" + 
		          indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at end; \"); }\n" + 
                     indent + "  else \n" + 
                     indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at end; \"); }\n"; 
              }
				  		  
            } // Check invariants of parnme. 
            else if (typ.isCollectionType()) // Try with empty list, singleton & random list
            { Type elemTyp = typ.getElementType();
              String collType = "ArrayList"; 
              if (typ.isSequence())
              { } 
              else 
              { collType = "HashSet"; }
 
              if (elemTyp == null) { } 
              else if ("int".equals(elemTyp.getName()))
              { testscript = testscript + 
                      indent + "\n" + 
				indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
				indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
				indent + "  if (" + indexvar + " < 5)\n" + 
				indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + "])); }\n" + 
				indent + "  if (" + indexvar + " < 3)\n" + 
				indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + " + 2])); }\n";  
              } 
              else if ("long".equals(elemTyp.getName()))
              { testscript = testscript + 
                      indent + "\n" + 
				indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
				indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
				indent + "  if (" + indexvar + " < 5)\n" + 
				indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + "])); }\n" +
				indent + "  if (" + indexvar + " < 3)\n" + 
				indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + " + 2])); }\n"; 
              }  
              else if ("double".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 5)\n" + 
                  indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + "])); }\n" + 
				  indent + "  if (" + indexvar + " < 3)\n" + 
                  indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + " + 2])); }\n";
              }
              else if ("boolean".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 2)\n" + 
                  indent + "  { " + parnme + ".add(new Boolean(booleanTestValues[" + indexvar + "])); }\n";
              } 
              else if ("String".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 4; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < 3)\n" + 
                  indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + "]); }\n" + 
                  indent + "  if (" + indexvar + " < 2)\n" + 
                  indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + " + 1]); }\n";
              } 
              else if (elemTyp != null && elemTyp.isEnumeration())
              { Vector vals = elemTyp.getValues(); 
                int nvals = vals.size(); 
                testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " <= " + nvals + "; " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n" + 
                  indent + "  if (" + indexvar + " < " + nvals + ")\n" + 
                  indent + "  { " + parnme + ".add(new Integer(" + indexvar + ")); }\n"; 
              }
              else if (elemTyp.isEntity())
              { String etname = elemTyp.getName(); 
                String eeinstances = etname + "." + etname + "_allInstances"; 				
                String eename = etname.toLowerCase() + "s"; 
			    
                testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " <= " + eeinstances + ".size(); " + indexvar + "++)\n" + 
                  indent + "{ " + collType + " " + parnme + " = new " + collType + "();\n";  
                testscript = testscript + 
                  indent + "  if (" + indexvar + " < " + eeinstances + ".size())\n" + 
                  indent + "  { " + parnme + ".add(" + eeinstances + ".get(" + indexvar + ")); }\n";
              }  
            }  
          }  
        }
      
        for (int j = 0; j < uc.preconditions.size(); j++)
        { java.util.Map env = new java.util.HashMap(); 
          Constraint con = (Constraint) uc.preconditions.get(j); 
          checkCode = checkCode + 
		              indent + "  if (" + con.queryForm(env,true) + ")\n" + 
		              indent + "  { System.out.print(\" Precondition " + j + " is valid; \"); }\n" + 
                         indent + "  else \n" + 
                         indent + "  { System.out.print(\" Precondition " + j + " fails; \"); }\n"; 
        }

      
        String call = " cont." + nme + "(" + parstring + ")"; 
        if (resultType != null) 
        { call = " System.out.println(\" ==> Result: \" + " + call + " )"; } 
		
		call = indent + "  try { " + call + ";\n" + 
		       indent + "  }\n" + 
                  indent + "  catch (Throwable _e) { System.out.println(\" !! Exception occurred: test failed !! \"); }\n"; 
		
           if (teststring.equals(""))
           { teststring = "\"\""; } 
	   
		testscript = testscript + 
             indent + "  System.out.println();\n" + 
             indent + "  System.out.print(\">>> Test: \" + " + teststring + ");\n" + 
             checkCode + 
             call + posttests + "\n" + 
             indent + "  System.out.println();\n" + 
             indent + "  System.out.println();\n"; 
        // indent = indent.substring(0,indent.length()-2);  
		
		for (int p = 0; p < pars.size(); p++) 
		{ testscript = testscript + 
		               indent + "}\n";
		  indent = indent.substring(0,indent.length()-2);  
		}
      
        aper = aper +
           "    if (\"" + nme + "\".equals(cmd))\n" +
           "    { " + testscript + "\n" + 
           "      return;\n" + 
           "    } \n";
      }
    } 
	
    cons = cons + "  }\n\n";
    aper = aper + "  }\n\n";
    res = res + "\n" + cons + aper +
      "  public static void main(String[] args)\n" +
      "  { TestsGUI gui = new TestsGUI();\n" +
      "    gui.setSize(550,400);\n" +
      "    gui.setVisible(true);\n" +
      "  }\n }";
    return res;
  }
  
  public static String buildTestsGUI(Vector ucs, String sysName, boolean incr, Vector types, Vector entities)
  { String res = "import javax.swing.*;\n" +
      "import javax.swing.event.*;\n" +
      "import java.awt.*;\n" +
      "import java.awt.event.*;\n" + 
      "import java.util.Vector;\n" + 
      "import java.io.*;\n" + 
      "import java.util.StringTokenizer;\n\n";  

    String mutationTestsOp = ""; 
    for (int i = 0; i < entities.size(); i++) 
    { Entity ent = (Entity) entities.get(i);
      if (ent.isDerived() || ent.isComponent()) 
      { continue; }  
      String ename = ent.getName();
      String es = ename.toLowerCase() + "s"; 
      Vector eops = ent.getOperations();  
      
      for (int j = 0; j < eops.size(); j++) 
      { BehaviouralFeature bf = (BehaviouralFeature) eops.get(j); 
        if (bf.isMutatable())
        { String bfname = bf.getName();  
          mutationTestsOp = mutationTestsOp + 
          "      int[] " + es + "_" + bfname + "_counts = new int[100]; \n" +   
          "      int[] " + es + "_" + bfname + "_totals = new int[100]; \n" +   
          "      for (int _i = 0; _i < Controller.inst()." + es + ".size(); _i++)\n" + 
          "      { " + ename + " _ex = (" + ename + ") Controller.inst()." + es + ".get(_i);\n" +  
          "        MutationTest." + bfname + "_mutation_tests(_ex," + es + "_" + bfname + "_counts, " + es + "_" + bfname + "_totals);\n" + 
          "      }\n" + 
          "      System.out.println();\n";  
        } 
      }
    } 

    String contname = "Controller"; 

    if (sysName == null || sysName.length() == 0) { } 
    else 
    { contname = sysName + "." + contname; } 

    res = res +
      "public class TestsGUI extends JFrame implements ActionListener\n" +
      "{ JPanel panel = new JPanel();\n" +
      "  JPanel tPanel = new JPanel();\n" +
      "  JPanel cPanel = new JPanel();\n" +
      "  " + contname + " cont = " + contname + ".inst();\n";

    String cons = " public TestsGUI()\n" +
      "  { super(\"Select use case to test\");\n" +
      "    panel.setLayout(new BorderLayout());\n" + 
      "    panel.add(tPanel, BorderLayout.NORTH);\n" +  
      "    panel.add(cPanel, BorderLayout.CENTER);\n" +  
      "    setContentPane(panel);\n" + 
      "    addWindowListener(new WindowAdapter() \n" + 
      "    { public void windowClosing(WindowEvent e)\n" +  
      "      { System.exit(0); } });\n";

    String aper = "  public void actionPerformed(ActionEvent e)\n" +
      "  { if (e == null) { return; }\n" +
      "    String cmd = e.getActionCommand();\n";

    res = res + "    JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "    tPanel.add(loadModelButton);\n" +
        "    loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "    JButton saveModelButton = new JButton(\"saveModel\");\n";

    res = res + "    JButton loadXmiButton = new JButton(\"loadXmi\");\n";


    cons = cons + 
        "    tPanel.add(saveModelButton);\n" +
        "    saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
         "      cont.saveXSI(\"xsi.txt\"); \n" + 
         "      return; } \n";

    cons = cons + 
        "    tPanel.add(loadXmiButton);\n" +
        "    loadXmiButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"loadXmi\".equals(cmd))\n" +
         "    { cont.loadXSI();  \n" + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "    JButton loadCSVButton = new JButton(\"Mutation Tests\");\n";

    cons = cons + 
         "    tPanel.add(loadCSVButton);\n" +
         "    loadCSVButton.addActionListener(this);\n";
                  
    aper = aper +
         "    if (\"Mutation Tests\".equals(cmd))\n" + 
         "    { System.err.println(\"Mutation tests\");\n" +
         mutationTestsOp +  
         "      return;\n" + 
         "    } \n";

    // res = res + "    JButton saveCSVButton = new JButton(\"saveCSVs\");\n";

    // cons = cons + 
    //     "    tPanel.add(saveCSVButton);\n" +
    //     "    saveCSVButton.addActionListener(this);\n";
      
    // aper = aper +
    //      "    if (\"saveCSVs\".equals(cmd))\n" +
    //      "    { cont.saveCSVModel();  \n" + 
    //      "      return; } \n";

	aper = aper + 
         "    int[] intTestValues = {0, -1, 1, 2147483647, -2147483648};\n" + 
         "    long[] longTestValues = {0, -1, 1, " + Long.MAX_VALUE + "L, " + Long.MIN_VALUE + "L};\n" + 
         "    double[] doubleTestValues = {0, -1, 1, " + Double.MAX_VALUE + ", " + Double.MIN_VALUE + "};\n" +
         "    boolean[] booleanTestValues = {false, true};\n" + 
         "    String[] stringTestValues = {\"\", \" abc_XZ \", \"#�$* &~@'\"};\n";

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        if (pars.size() > 0)
        { java.util.Map upperBounds = new java.util.HashMap(); 
          java.util.Map lowerBounds = new java.util.HashMap(); 
	
          Vector bounds = new Vector(); 
          java.util.Map aBounds = new java.util.HashMap(); 
      
          for (int k = 0; k < uc.preconditions.size(); k++) 
          { Constraint con = (Constraint) uc.preconditions.get(k);
            Expression pre = con.succedent();  
            pre.getParameterBounds(pars,bounds,aBounds);
   
            Expression.identifyUpperBounds(pars,aBounds,upperBounds); 
            Expression.identifyLowerBounds(pars,aBounds,lowerBounds); 
		  } 
		  
          for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            Type typ = par.getType(); 
			String tname = typ + ""; 
			Vector testassignments = par.testValues("parameters", lowerBounds, upperBounds);
			
            if ("int".equals(tname))
            { aper = aper + 
			     "    int[] " + nme + par + "TestValues = {";
			  for (int y = 0; y < testassignments.size(); y++) 
			  { String tval = (String) testassignments.get(y); 
			    aper = aper + tval; 
				if (y < testassignments.size() - 1) 
				{ aper = aper + ", "; }
			  }
			  aper = aper + "};\n";  
		    } 
            else if ("long".equals(tname))
            { aper = aper + 
			     "    long[] " + nme + par + "TestValues = {";
			  for (int y = 0; y < testassignments.size(); y++) 
			  { String tval = (String) testassignments.get(y); 
			    aper = aper + tval; 
				if (y < testassignments.size() - 1) 
				{ aper = aper + ", "; }
			  }
			  aper = aper + "};\n";  
		    } 
	        else if ("double".equals(tname))
            { aper = aper + 
			     "    double[] " + nme + par + "TestValues = {";
			  for (int y = 0; y < testassignments.size(); y++) 
			  { String tval = (String) testassignments.get(y); 
			    aper = aper + tval; 
				if (y < testassignments.size() - 1) 
				{ aper = aper + ", "; }
			  }
			  aper = aper + "};\n";  
		    } 

		  }
		}
	  }
	}
	 
    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (me instanceof UseCase) 
      { UseCase uc = (UseCase) me;
        if (uc.isDerived()) { continue; } 
		String indent = "    "; 
        
		// String checkCode = uc.getQueryCode("Java4", types, entities); 
        String checkCode = ""; 
		
        
        String nme = uc.getName();
        Vector pars = uc.getParameters(); 
        Type resultType = uc.getResultType(); 
        String testscript = "";
		String teststring = ""; 
		String posttests = ""; 

        res = res + "    JButton " + nme + "Button = new JButton(\"" + nme + "\");\n";
 
        cons = cons + 
          "    cPanel.add(" + nme + "Button);\n" +
          "    " + nme + "Button.addActionListener(this);\n";

        String parstring = ""; 
        String parnames = uc.getParameterNames(); 

				
        if (pars.size() > 0)
        { for (int j = 0; j < pars.size(); j++) 
          { Attribute par = (Attribute) pars.get(j); 
            String parnme = par.getName();
            String indexvar = "_index_" + j; 
            indent = indent + "  "; 
			 
            parstring = parstring + parnme; 
            teststring = teststring + "\"" + parnme + " = \" + " + parnme; 
            if (j < pars.size() - 1)
            { parstring = parstring + ","; 
              teststring = teststring + " + \"; \" + "; 
            } 

            Type typ = par.getType(); 
            String tname = typ + ""; 
			
            if ("int".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ int " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("long".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ long " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("double".equals(tname))
            { testscript = testscript + 
                indent + "\n" + 
                indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nme + par + "TestValues.length; " + indexvar + "++)\n" + 
                indent + "{ double " + parnme + " = " + nme + par + "TestValues[" + indexvar + "];\n";  
            } 
            else if ("boolean".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 2; " + indexvar + "++)\n" + 
                  indent + "{ boolean " + parnme + " = booleanTestValues[" + indexvar + "];\n";  
            } 
            else if ("String".equals(typ + ""))
            { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                  indent + "{ String " + parnme + " = stringTestValues[" + indexvar + "];\n";  
            } 
            else if (typ != null && typ.isEnumeration())
            { Vector vals = typ.getValues();  
              int nvals = vals.size(); 
              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < " + nvals + "; " + indexvar + "++)\n" + 
                  indent + "{ int " + parnme + " = " + indexvar + ";\n";
            } 
            else if (typ.isEntity())
            { String ename = tname.toLowerCase() + "s";
              Entity ee = typ.getEntity(); 
			   
              Vector eeinvariants = new Vector(); 
              if (ee != null) 
              { eeinvariants.addAll(ee.getInvariants()); }

              testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < Controller.inst()." + ename + ".size(); " + indexvar + "++)\n" + 
                  indent + "{ " + tname + " " + parnme + " = (" + tname + ") Controller.inst()." + ename + ".get(" + indexvar + ");\n";
              for (int p = 0; p < eeinvariants.size(); p++)
              { java.util.Map env = new java.util.HashMap(); 
                env.put(tname, parnme); 
                Constraint conp = (Constraint) eeinvariants.get(p); 
                Constraint conpr = conp.addReference(parnme,typ); 
                String constring = conpr.queryForm(env,true); 
				
                testscript = testscript + 
                  indent + "  if (" + constring + ")\n" + 
                  indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at start; \"); }\n" + 
                  indent + "  else \n" + 
                  indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at start; \"); }\n"; 
                posttests = posttests + 
                  indent + "  if (" + constring + ")\n" + 
                  indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " is valid at end; \"); }\n" + 
                  indent + "  else \n" + 
                  indent + "  { System.out.print(\"" + parnme + " : " + tname + " invariant " + p + " fails at end; \"); }\n"; 
              }
				  		  
            } // Check invariants of parnme. 
            else if (typ.isCollectionType()) // Try with empty list, singleton & random list
            { Type elemTyp = typ.getElementType(); 
              if (elemTyp == null) { } 
              else if ("int".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
                  indent + "{ Vector " + parnme + " = new Vector();\n" + 
                  indent + "  if (" + indexvar + " < 5)\n" + 
                  indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + "])); }\n" +   
                  indent + "  if (" + indexvar + " < 3)\n" + 
                  indent + "  { " + parnme + ".add(new Integer(intTestValues[" + indexvar + " + 2])); }\n";  
              } 
              else if ("long".equals(elemTyp.getName()))
              { testscript = testscript + 
                  indent + "\n" + 
                  indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
                  indent + "{ java.util.List " + parnme + " = new Vector();\n" + 
                  indent + "  if (" + indexvar + " < 5)\n" + 
                  indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + "])); }\n" +  
                  indent + "  if (" + indexvar + " < 3)\n" + 
				  indent + "  { " + parnme + ".add(new Long(longTestValues[" + indexvar + " + 2])); }\n"; 
                }  
                else if ("double".equals(elemTyp.getName()))
                { testscript = testscript + 
                    indent + "\n" + 
                    indent + "for (int " + indexvar + " = 0; " + indexvar + " < 6; " + indexvar + "++)\n" + 
                    indent + "{ java.util.List " + parnme + " = new Vector();\n" + 
                    indent + "  if (" + indexvar + " < 5)\n" + 
                    indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + "])); }\n" + 
				    indent + "  if (" + indexvar + " < 3)\n" + 
                    indent + "  { " + parnme + ".add(new Double(doubleTestValues[" + indexvar + " + 2])); }\n";
                  }
                  else if ("boolean".equals(elemTyp.getName()))
                 { testscript = testscript + 
                     indent + "\n" + 
                     indent + "for (int " + indexvar + " = 0; " + indexvar + " < 3; " + indexvar + "++)\n" + 
                     indent + "{ java.util.List " + parnme + " = new Vector();\n" + 
                     indent + "  if (" + indexvar + " < 2)\n" + 
                     indent + "  { " + parnme + ".add(new Boolean(booleanTestValues[" + indexvar + "])); }\n";
                 } 
                 else if ("String".equals(elemTyp.getName()))
                 { testscript = testscript + 
                     indent + "\n" + 
                     indent + "for (int " + indexvar + " = 0; " + indexvar + " < 4; " + indexvar + "++)\n" + 
                     indent + "{ java.util.List " + parnme + " = new Vector();\n" + 
                     indent + "  if (" + indexvar + " < 3)\n" + 
                     indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + "]); }\n" + 
                     indent + "  if (" + indexvar + " < 2)\n" + 
                     indent + "  { " + parnme + ".add(stringTestValues[" + indexvar + " + 1]); }\n";
                 } 
                 else if (elemTyp != null && elemTyp.isEnumeration())
                 { Vector vals = elemTyp.getValues();   
                   int nvals = vals.size(); 
                   testscript = testscript + 
                      indent + "\n" + 
                      indent + "for (int " + indexvar + " = 0; " + indexvar + " <= " + nvals + "; " + indexvar + "++)\n" + 
                      indent + "{ java.util.List " + parnme + " = new Vector();\n" + 
                      indent + "  if (" + indexvar + " < " + nvals + ")\n" + 
                      indent + "  { " + parnme + ".add(new Integer(" + indexvar + ")); }\n"; 
                  }
                  else if (elemTyp.isEntity())
			  { String etname = elemTyp.getName(); 
			    // Entity ee = elemTyp.getEntity(); 
				
			    String eename = etname.toLowerCase() + "s"; 
			    
				testscript = testscript + 
                  indent + "\n" + 
				  indent + "for (int " + indexvar + " = 0; " + indexvar + " <= Controller.inst()." + etname + ".size(); " + indexvar + "++)\n" + 
				  indent + "{ java.util.List " + parnme + " = new Vector();\n";  
                testscript = testscript + 
			      indent + "  if (" + indexvar + " < Controller.inst()." + etname + ".size())\n" + 
			      indent + "  { " + parnme + ".add(Controller.inst()." + eename + ".get(" + indexvar + ")); }\n";
              }  
			}  
          }  
        }
      
        for (int j = 0; j < uc.preconditions.size(); j++)
        { java.util.Map env = new java.util.HashMap(); 
          Constraint con = (Constraint) uc.preconditions.get(j); 
          checkCode = checkCode + 
		              indent + "  if (" + con.queryForm(env,true) + ")\n" + 
		              indent + "  { System.out.print(\" Precondition " + j + " is valid; \"); }\n" + 
					  indent + "  else \n" + 
					  indent + "  { System.out.print(\" Precondition " + j + " fails; \"); }\n"; 
        }

      
        String call = " cont." + nme + "(" + parstring + ")"; 
        if (resultType != null) 
        { call = " System.out.println(\" ==> Result: \" + " + call + " )"; } 
		
		call = indent + "  try { " + call + ";\n" + 
		       indent + "  }\n" + 
			   indent + "  catch(Exception _e) { System.out.println(\" !! Exception occurred: test failed !! \"); }\n"; 
			   
           if (teststring.equals(""))
           { teststring = "\"\""; } 

		testscript = testscript + 
		  indent + "  System.out.println();\n" + 
             indent + "  System.out.print(\">>> Test: \" + " + teststring + ");\n" + 
		  checkCode + 
		  call + posttests + "\n" + 
		  indent + "  System.out.println();\n" + 
             indent + "  System.out.println();\n"; 
        // indent = indent.substring(0,indent.length()-2);  
		
		for (int p = 0; p < pars.size(); p++) 
		{ testscript = testscript + 
		               indent + "}\n";
		  indent = indent.substring(0,indent.length()-2);  
		}
      
        aper = aper +
           "    if (\"" + nme + "\".equals(cmd))\n" +
           "    { " + testscript + "\n" + 
		"      return;\n" + 
           "    } \n";
      }
    } 
	
    cons = cons + "  }\n\n";
    aper = aper + "  }\n\n";
    res = res + "\n" + cons + aper +
      "  public static void main(String[] args)\n" +
      "  { TestsGUI gui = new TestsGUI();\n" +
      "    gui.setSize(550,400);\n" +
      "    gui.setVisible(true);\n" +
      "  }\n }";
    return res;
  }

  public static String buildUCGUIJava6(Vector ucs, String sysName, boolean incr)
  { String res = "import javax.swing.*;\n" +
      "import javax.swing.event.*;\n" +
      "import java.awt.*;\n" +
      "import java.awt.event.*;\n" + 
      "import java.util.Vector;\n" + 
      "import java.util.Collection;\n" + 
      "import java.util.HashSet;\n" + 
      "import java.util.ArrayList;\n" + 
      "import java.io.*;\n" + 
      "import java.util.StringTokenizer;\n\n";  

    String contname = "Controller"; 

    if (sysName == null || sysName.length() == 0) { } 
    else 
    { contname = sysName + "." + contname; } 

    res = res +
      "public class GUI extends JFrame implements ActionListener\n" +
      "{ JPanel panel = new JPanel();\n" +
      "  " + contname + " cont = " + contname + ".inst();\n";

    String cons = " public GUI()\n" +
      "  { super(\"Select use case to execute\");\n" +
      "    setContentPane(panel);\n" + 
      "    addWindowListener(new WindowAdapter() \n" + 
      "    { public void windowClosing(WindowEvent e)\n" +  
      "      { System.exit(0); } });\n";

    String aper = "  public void actionPerformed(ActionEvent e)\n" +
      "  { if (e == null) { return; }\n" +
      "    String cmd = e.getActionCommand();\n";

    res = res + "  JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "    panel.add(loadModelButton);\n" +
        "    loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop +
		 "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveModelButton = new JButton(\"saveModel\");\n";

    cons = cons + 
        "    panel.add(saveModelButton);\n" +
        "    saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
    //         "      cont.saveXSI(\"xsi.txt\"); \n" + 
         "      return; } \n";

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (!(me instanceof UseCase)) { continue; } 

      UseCase uc = (UseCase) me;
      if (uc.isDerived()) { continue; } 

      String nme = uc.getName();
      Vector pars = uc.getParameters(); 
      Type resultType = uc.getResultType(); 

      String parstring = ""; 
      String parnames = uc.getParameterNames(); 

      String prompt = ""; 
      if (pars.size() > 0)
      { prompt = "String _vals = JOptionPane.showInputDialog(\"Enter parameters " + parnames + ":\");\n" +                                 "    Vector _values = new Vector();\n" + 
                 "    StringTokenizer _st = new StringTokenizer(_vals);\n" +  
                 "    while (_st.hasMoreTokens())\n" + 
                 "    { String _se = _st.nextToken().trim();\n" + 
                 "      _values.add(_se); \n" + 
                 "    }\n\n"; 


        for (int j = 0; j < pars.size(); j++) 
        { Attribute par = (Attribute) pars.get(j); 
          String parnme = par.getName(); 
          parstring = parstring + parnme; 
          if (j < pars.size() - 1)
          { parstring = parstring + ","; } 

          Type typ = par.getType(); 
          if ("int".equals(typ + ""))
          { prompt = prompt + 
              "    int " + parnme + 
                " = Integer.parseInt((String) _values.get(" + j + "));\n"; 
          } 
          else if ("long".equals(typ + ""))
          { prompt = prompt + 
              "    long " + parnme + 
                " = Long.parseLong((String) _values.get(" + j + "));\n"; 
          } 
          else if ("double".equals(typ + ""))
          { prompt = prompt + 
              "    double " + parnme + 
                " = Double.parseDouble((String) _values.get(" + j + "));\n"; 
          } 
          else if (typ != null && typ.isEnumeration())
          { String tname = typ.getName(); 
            prompt = prompt + 
                "    int " + parnme + 
                  " = SystemTypes.parse" + tname + "((String) _values.get(" + j + "));\n"; 
          } 
          else if (typ != null && typ.isEntity())
          { Entity ent = typ.getEntity();
            String ename = ent.getName();  
            Attribute pk = ent.getPrincipalPrimaryKey(); // arguments are supplied in the GUI as strings
            if (pk != null)
            { // String indexname = ename + pk.getName() + "index"; 
              prompt = prompt + 
                  "    " + ename + " " + parnme + 
                  " = (" + ename + ") Controller.inst().get" + ename + "ByPK((String) _values.get(" + j + "));\n"; 
            }
          }
          else if (typ.isCollectionType())
          { String j6typ = "HashSet"; 
            if (Type.isSequenceType(typ))
            { j6typ = "ArrayList"; } 
            prompt = prompt + 
              "    java.util.Collection " + parnme + " = new " + j6typ + "();\n" + 
              "    File file" + parnme + " = new File((String) _values.get(" + j + "));\n" +
              "    BufferedReader br" + parnme + " = null;\n" + 
              "    try { br" + parnme + " = new BufferedReader(new FileReader(file" + parnme + ")); }\n" + 
              "    catch(Exception _e) { System.err.println(\"no file\"); return; }\n" + 
              "    while (true)\n" + 
              "    { String _s = null; \n" + 
              "      try { _s = br" + parnme + ".readLine(); }\n" + 
              "      catch (Exception _ex) { break; }\n" + 
              "      if (_s == null) { break; }\n" +  
              "      " + parnme + ".add(_s); \n" + 
              "    }\n"; 
          } 
          else // if ("boolean".equals(typ + ""))
          { prompt = prompt + 
              "    String " + parnme + " = (String) _values.get(" + j + ");\n"; 
          } 
        }
      }
    
      res = res + "  JButton " + nme + "Button = new JButton(\"" + nme + "\");\n";

      cons = cons + 
        "  panel.add(" + nme + "Button);\n" +
        "  " + nme + "Button.addActionListener(this);\n";
      
      String call = " cont." + nme + "(" + parstring + ") "; 
      if (resultType != null) 
      { call = " System.out.println( " + call + " ) "; } 
      
      aper = aper +
         "    if (\"" + nme + "\".equals(cmd))\n" +
         "    { " + prompt + call + ";  return; } \n";
    }
    cons = cons + "  }\n\n";
    aper = aper + "  }\n\n";
    res = res + "\n" + cons + aper +
      "  public static void main(String[] args)\n" +
      "  { GUI gui = new GUI();\n" +
      "    gui.setSize(400,400);\n" +
      "    gui.setVisible(true);\n" +
      "  }\n }";
    return res;
  }

  public static String generateCSharpGUI(Vector ucs)
  { String res = ""; 
    //  "using System;\n" +
    //  "using System.Linq;\n" + 
    //  "using System.Threading.Tasks;\n" + 
    //  "using System.Windows.Forms;\n\n";

    res = res + "public class GUI : Form\n" + 
      "{ Controller cont;\n\n" +
      "  public GUI()\n" + 
      "  { InitializeComponent(); cont = Controller.inst(); }\n\n" + 
      "  private void loadModel_Click(object sender, EventArgs e)\n" +
      "  { cont.loadModel(); }\n\n" + 
      "  private void saveModel_Click(object sender, EventArgs e)\n" + 
      "  { cont.saveModel(\"out.txt\"); }\n\n";
    String initbutts = "";
    String declrs = "";
    String addcontrols = "";
    for (int i = 0; i < ucs.size(); i++)
    { UseCase uc = (UseCase) ucs.get(i);
      String ucnme = uc.getName();
      initbutts = initbutts + 
        "    this." + ucnme + " = new System.Windows.Forms.Button();\n";   
      declrs = declrs + 
        "    private System.Windows.Forms.Button " + ucnme + ";\n";
      addcontrols = addcontrols + 
        "    this.Controls.Add(this." + ucnme + ");\n";
      Vector pars = uc.getParameters();
      String parstring = "";  
      for (int j = 0; j < pars.size(); j++) 
      { Attribute par = (Attribute) pars.get(j); 
        String parname = par.getName(); 
        String fldnme = ucnme + "_" + parname; 

        parstring = parstring + Expression.convertCSharp(fldnme + ".Text", par.getType());
        if (j < pars.size() - 1) { parstring = parstring + ", "; } 
 
        initbutts = initbutts + 
        "    this." + fldnme + " = new System.Windows.Forms.TextBox();\n";
        declrs = declrs + 
        "    private System.Windows.Forms.TextBox " + fldnme + ";\n";
        addcontrols = addcontrols + 
        "    this.Controls.Add(this." + fldnme + ");\n";
      } 
      res = res + 
        "  private void " + ucnme + "_Click(object sender, EventArgs e)\n" + 
        "  { cont." + ucnme + "(" + parstring + "); }\n\n";
    }

    res = res +
      "  private System.ComponentModel.IContainer components = null;\n\n" + 
      "  protected override void Dispose(bool disposing)\n" + 
      "  { if (disposing && (components != null))\n" + 
      "      { components.Dispose(); }\n" + 
      "      base.Dispose(disposing);\n" + 
      "  }\n\n" + 
      "  private void InitializeComponent()\n" + 
      "  { this.loadModel = new System.Windows.Forms.Button();\n" + 
      "    this.saveModel = new System.Windows.Forms.Button();\n" + initbutts +
      "    this.SuspendLayout(); \n" +  
      "    this.loadModel.Location = new System.Drawing.Point(10, 24);\n" + 
      "    this.loadModel.Name = \"loadModel\";\n" + 
      "    this.loadModel.Size = new System.Drawing.Size(75, 23);\n" + 
      "    this.loadModel.TabIndex = 0;\n" + 
      "    this.loadModel.Text = \"loadModel\";\n" +
      "    this.loadModel.UseVisualStyleBackColor = true;\n" +
      "    this.loadModel.Click += new System.EventHandler(this.loadModel_Click);\n" +
      "    this.saveModel.Location = new System.Drawing.Point(95, 24);\n" +
      "    this.saveModel.Name = \"saveModel\";\n" +
      "    this.saveModel.Size = new System.Drawing.Size(75, 23);\n" +
      "    this.saveModel.TabIndex = 1;\n" +
      "    this.saveModel.Text = \"saveModel\";\n" +
      "    this.saveModel.UseVisualStyleBackColor = true;\n" +
      "    this.saveModel.Click += new System.EventHandler(this.saveModel_Click);\n"; 

    int maxx = 300; 

    for (int i = 0; i < ucs.size(); i++)
    { ModelElement me = (ModelElement) ucs.get(i); 
      if (!(me instanceof UseCase)) { continue; } 
      UseCase uc = (UseCase) me;
      String ucnme = uc.getName();
      int x = 10; 
      int y = 60 + i*30;
      res = res + 
        "     this." + ucnme + ".Location = new System.Drawing.Point(" + x + ", " + y + ");\n" +
        "     this." + ucnme + ".Name = \"" + ucnme + "\";\n" +
        "     this." + ucnme + ".Size = new System.Drawing.Size(85, 23);\n" +
        "     this." + ucnme + ".TabIndex = " + (2+i) + ";\n" +
        "     this." + ucnme + ".Text = \"" + ucnme + "\";\n" +
        "     this." + ucnme + ".UseVisualStyleBackColor = true;\n" +
        "     this." + ucnme + ".Click += new System.EventHandler(this." + ucnme + "_Click);\n";   
      Vector ps = uc.getParameters(); 
      for (int j = 0; j < ps.size(); j++) 
      { Attribute pr = (Attribute) ps.get(j); 
        String prnme = pr.getName();
        String fieldnme = ucnme + "_" + prnme; 
        int xx = x + 95 + j*80; 
        int yy = y;  
        res = res + 
        "     this." + fieldnme + ".Location = new System.Drawing.Point(" + xx + ", " + yy + ");\n" +
        "     this." + fieldnme + ".Name = \"" + prnme + "\";\n" +
        "     this." + fieldnme + ".Size = new System.Drawing.Size(75, 23);\n" +
        "     this." + fieldnme + ".Text = \"" + prnme + "\";\n"; 
        if (xx + 75 > maxx) { maxx = xx + 75; } 
      }         
    }

    res = res +
        "     this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);\n" +
        "     this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;\n" +
        "     this.ClientSize = new System.Drawing.Size(" + maxx + ", 90 + 30*" + (ucs.size()) + ");\n" +
        "     this.Controls.Add(this.loadModel);\n" +
        "     this.Controls.Add(this.saveModel);\n" + addcontrols + 
        "     this.Name = \"GUI\";\n" +
        "     this.Text = \"GUI\";\n" +
        "     this.ResumeLayout(false);\n" +
        "   }\n\n";

   res = res +
    "     private System.Windows.Forms.Button loadModel;\n" +
    "     private System.Windows.Forms.Button saveModel;\n" + declrs + 
    "  }\n\n" +
    "  static class Program\n" +
    "  {\n" +
    "    [STAThread]\n" +
    "    static void Main()\n" +
    "    {\n" +
    "        Application.EnableVisualStyles();\n" +
    "        Application.SetCompatibleTextRenderingDefault(false);\n" +
    "        Application.Run(new GUI());\n" +
    "    }\n" +
    " }\n\n";
   return res;
 }

  public static void main(String[] args)
  { List cons = new ArrayList();
    cons.add("fr : MyFrame");
    cons.add("fr.title = \"App\"");
    cons.add("p : Panel");
    cons.add("fr.center = p");
    cons.add("b : Button");
    cons.add("b : p");
    cons.add("b.text = \"Start\"");
    cons.add("b.command = \"start_op\"");
    GUIBuilder gb = new GUIBuilder();
    List econs = gb.parseAll(cons);
    List objs = gb.objectSpecs(econs);
    System.out.println(objs);
    if (objs.size() > 0)
    { ObjectSpecification fr = (ObjectSpecification) objs.get(0);
      String f = fr.getName();
      String nme = fr.objectClass;
      gb.buildMainFrame(fr,nme,objs,f,new ArrayList());
    } 
  }
}
