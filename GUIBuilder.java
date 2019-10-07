import java.util.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
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

    res = res + "  JButton loadModelButton = new JButton(\"loadModel\");\n";

    cons = cons + 
        "  tPanel.add(loadModelButton);\n" +
        "  loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveModelButton = new JButton(\"saveModel\");\n";

    res = res + "  JButton loadXmiButton = new JButton(\"loadXmi\");\n";


    cons = cons + 
        "  tPanel.add(saveModelButton);\n" +
        "  saveModelButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"saveModel\".equals(cmd))\n" +
         "    { cont.saveModel(\"out.txt\");  \n" + 
         "      cont.saveXSI(\"xsi.txt\"); \n" + 
         "      return; } \n";

    cons = cons + 
        "  tPanel.add(loadXmiButton);\n" +
        "  loadXmiButton.addActionListener(this);\n";
      
    aper = aper +
         "    if (\"loadXmi\".equals(cmd))\n" +
         "    { cont.loadXSI();  \n" + 
         "      cont.checkCompleteness();\n" + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton loadCSVButton = new JButton(\"loadCSVs\");\n";

    cons = cons + 
        "  tPanel.add(loadCSVButton);\n" +
        "  loadCSVButton.addActionListener(this);\n";
      
    String loadcsvop = "    { " + contname + ".loadCSVModel();\n";   
            
    aper = aper +
         "    if (\"loadCSVs\".equals(cmd))\n" + loadcsvop + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveCSVButton = new JButton(\"saveCSVs\");\n";

    cons = cons + 
        "  tPanel.add(saveCSVButton);\n" +
        "  saveCSVButton.addActionListener(this);\n";
      
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
            else if (typ.isCollectionType())
            { String convertElementType = "typ" + parnme; 
              Type elemTyp = typ.getElementType(); 
              if (elemTyp == null) { } 
              else if ("int".equals(elemTyp.getName()))
              { convertElementType = "new Integer(Integer.parseInt(typ" + parnme + "))"; } 
              else if ("long".equals(elemTyp.getName()))
              { convertElementType = "new Long(Long.parseLong(typ" + parnme + "))"; } 
              else if ("double".equals(elemTyp.getName()))
              { convertElementType = "new Double(Double.parseDouble(typ" + parnme + "))"; } 

              prompt = prompt + 
                "    java.util.List " + parnme + " = new Vector();\n" + 
                "    int " + parnme + "j = " + j + ";\n" +
                "    String typ" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
                "    " + parnme + "j++; typ" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
                "    while (!typ" + parnme + ".equals(\"}\"))\n" + 
                "    { " + parnme + ".add(" + convertElementType + "); \n" + 
                "      " + parnme + "j++; typ" + parnme + " = (String) _values.get(" + parnme + "j);\n" +
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
      "  {  GUI gui = new GUI();\n" +
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
        "  panel.add(loadModelButton);\n" +
        "  loadModelButton.addActionListener(this);\n";
      
    String loadmodelop = "    { " + contname + ".loadModel(\"in.txt\");\n";   
         
    if (incr)
    { loadmodelop = "    { " + contname + ".loadModelDelta(\"in.txt\");\n"; } 
   
    aper = aper +
         "    if (\"loadModel\".equals(cmd))\n" + loadmodelop + 
         "      System.err.println(\"Model loaded\");\n" + 
         "      return; } \n";

    res = res + "  JButton saveModelButton = new JButton(\"saveModel\");\n";

    cons = cons + 
        "  panel.add(saveModelButton);\n" +
        "  saveModelButton.addActionListener(this);\n";
      
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
      "  {  GUI gui = new GUI();\n" +
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
