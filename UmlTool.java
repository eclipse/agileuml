/******************************
* Copyright (c) 2003--2024 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

/* 
 * Classname : UmlTool
 * 
 * Version information : 2.3
 *
 * Date : December 2023
 * 
 * Description : This describes the GUI interface of 
 * the UML RSDS tool,
 * which includes a simple window, 
 * the menu options and their action 
 * listeners.
 package: Class Diagram Editor
 
 */

import javax.swing.*;
import javax.swing.event.*;
// import java.awt.*;
import java.awt.event.*;
import java.util.Vector; 
import java.util.StringTokenizer;
import java.io.*; 
import java.awt.print.*;
import java.lang.Runtime; 

public class UmlTool extends 
             JFrame implements ActionListener 
{ // counts the number of frames opened
  private static int openFrameCount = 0;
  private UCDArea ucdArea;
  JEditorPane helppane = null; 
        

  Object currentBuilder = null;
  String name; /* Name of the system */
  String controllerName;  
    /* Name of main (root) controller */ 
  String extending = null;  /* Ancestor, if any */
  private String prefix = null;     
    /* Needed for subcontrollers in B */  
  
  Vector invariants = new Vector();  
    /* Invariants of system */ 
  Vector opspecs = new Vector();     
    /* Operation specs derived from invariants */ 
  private Vector preopSpecs = new Vector(); 
  private Vector dependencies = new Vector();  
    /* Variable dependencies */ 
  private Vector transdeps = new Vector();    
    /* Transitive closure of dependencies */ 
  private Vector subsystemSpecs = new Vector();  
    /* Horiz decomp subsystems */ 
  private Vector phaseSubsystems = new Vector();  
    /* Phase decomp subsystems */ 
  private Vector smvModules;  /* List of these */
  private Vector javaInitInvs = new Vector(); 
  private Vector bInitInvs = new Vector(); 
 
  private Vector schedulers = new Vector(); 
  private Vector plugins = new Vector(); 

  private JLabel thisLabel;
  private boolean saved = true; // true when all data has been saved.

  private JMenuItem consischeck;   
    /* Menu item for consistency checking */
  private JMenuItem completenessCheck; 
  private JMenuItem depends; 
  private JMenuItem safety2op; 
  private JMenuItem testMenu; 
  private JMenuItem javaMenu; 
  private JMenuItem horizMI; 
  private JMenuItem hierMI; 
  private JMenuItem phaseMI; 
  private JMenuItem faultdetMI;
  private JMenuItem smvMI;  
  private JMenuItem schedulMI; 
  private JMenuItem verifyMI; 

  private SInvEditDialog sinvDialog; // dialog for invariants
  private Compiler2 comp = new Compiler2(); // to parse expressions
  private ListShowDialog listShowDialog; 
	  
public void findPlugins()
{ File dir = new File(".");
  String[] dirfiles = dir.list();
  for (int i = 0; i < dirfiles.length; i++)
  { File sub = new File(dirfiles[i]);
    if (sub.isDirectory())
    { String[] subfiles = sub.list();
      for (int j = 0; j < subfiles.length; j++)
      { if (subfiles[j].equals(dirfiles[i] + ".jar"))
        { plugins.add(dirfiles[i]);
          System.out.println("Found plugin: " + dirfiles[i]); 
          continue;
        }
      }
    }
  }
}

  public UmlTool() 
  { addWindowListener(new WindowAdapter() 
      { public void windowClosing(WindowEvent e) 
        { System.exit(0); }
      });
    
    // Add regular components to the window, 
    // using the default BorderLayout.
    java.awt.Container contentPane = getContentPane();
    
    // Add the dcfd drawing area to the frame
    ucdArea = new UCDArea(this);
    
    // Put the drawing area in a scroll pane.
    JScrollPane scroller = new JScrollPane(ucdArea, 
      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroller.setPreferredSize(new java.awt.Dimension(600,600));
    contentPane.add(scroller, java.awt.BorderLayout.CENTER);
    
    // Add the label to the frame
    thisLabel = 
      new JLabel("Click within the framed area.");
    contentPane.add(thisLabel, java.awt.BorderLayout.SOUTH); 

    ImageIcon bistateIcon = 
      new ImageIcon("bistate.gif", "two state icon"); 
    ImageIcon switchIcon = 
      new ImageIcon("switch.gif", "switch icon"); 
    ImageIcon singstateIcon = 
      new ImageIcon("singstate.gif", 
                    "singlestate icon"); 
    ImageIcon valveIcon = 
      new ImageIcon("valve.gif", "valve icon");
    ImageIcon openIcon = 
      new ImageIcon("open.gif", "open icon"); 
    ImageIcon saveIcon = 
      new ImageIcon("save.gif", "save icon"); 

    findPlugins(); 
	
    //Create the menu bar.
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    //Build the first menu.
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.setToolTipText(
      "Load UML-RSDS data and save B, Java etc");
    menuBar.add(fileMenu);
    
    //a group of JMenuItems under the "File" option
    JMenuItem newMI = new JMenuItem("Set name"); 
    newMI.addActionListener(this); 
    // newMI.setMnemonic(KeyEvent.VK_N);
    newMI.setToolTipText(
      "Assign a name to a UML-RSDS system/module");
    fileMenu.add(newMI); 

    JMenuItem recentMI = new JMenuItem("Recent"); 
    recentMI.setToolTipText(
      "Loads UML-RSDS data from output/mm.txt");
    recentMI.addActionListener(this); 
    recentMI.setMnemonic(KeyEvent.VK_R);
    fileMenu.add(recentMI); 

    JMenuItem saveMI = 
      new JMenuItem("Save",openIcon);
    saveMI.setToolTipText(
      "Saves current UML-RSDS model in output/mm.txt");
        saveMI.addActionListener(this);
    saveMI.setMnemonic(KeyEvent.VK_S);
    fileMenu.add(saveMI);

    JMenu loadMetamodelMenu = 
      new JMenu("Load Metamodel");
    fileMenu.add(loadMetamodelMenu);

    JMenuItem loadDataMI = 
      new JMenuItem("Load *.txt",openIcon);
    loadDataMI.addActionListener(this);
    loadDataMI.setMnemonic(KeyEvent.VK_L);
    loadMetamodelMenu.add(loadDataMI);

    JMenuItem loadkm3MI = 
      new JMenuItem("Load *.km3",openIcon);
    loadkm3MI.addActionListener(this);
    loadkm3MI.setToolTipText(
      "Loads *.km3 metamodel");
    // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMetamodelMenu.add(loadkm3MI);

    JMenuItem loadModelMI = 
      new JMenuItem("Load model",openIcon);
    loadModelMI.setToolTipText(
      "Loads instance of UML-RSDS metamodel");
    loadModelMI.addActionListener(this);
    loadMetamodelMenu.add(loadModelMI);

    JMenuItem csvMI = new JMenuItem("Load CSV"); 
    csvMI.setToolTipText(
      "Loads UML-RSDS data from output/mm.csv");
    csvMI.addActionListener(this); 
    loadMetamodelMenu.add(csvMI); 

    JMenuItem loadEcoreMI = 
      new JMenuItem("Load Ecore",openIcon);
    loadEcoreMI.addActionListener(this);
    // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMetamodelMenu.add(loadEcoreMI);

    JMenu loadMTMenu = 
      new JMenu("Load transformation");
    fileMenu.add(loadMTMenu);



    JMenuItem loadATLMI = 
      new JMenuItem("Load standard ATL",openIcon);
    loadATLMI.addActionListener(this);
    loadATLMI.setToolTipText(
      "Load ATL module from mm.atl");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadATLMI);

    JMenuItem loadrefATLMI = 
      new JMenuItem("Load refining ATL",openIcon);
    loadrefATLMI.addActionListener(this);
    loadrefATLMI.setToolTipText(
      "Load ATL module from mm.atl");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadrefATLMI);

    JMenuItem loadFlockMI = 
      new JMenuItem("Load Flock",openIcon);
    loadFlockMI.addActionListener(this);
    loadFlockMI.setToolTipText(
      "Load Flock module from flock.txt");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadFlockMI);

    JMenuItem loadETLMI = 
      new JMenuItem("Load ETL",openIcon);
    loadETLMI.addActionListener(this);
    loadETLMI.setToolTipText(
      "Load ETL module from mm.etl");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadETLMI);

    JMenuItem loadQVTMI = 
      new JMenuItem("Load QVT-R",openIcon);
    loadQVTMI.addActionListener(this);
    loadQVTMI.setToolTipText(
      "Load QVT-R module from mm.qvt");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadQVTMI);

    JMenuItem loadTLMI = 
      new JMenuItem("Load TL",openIcon);
    loadTLMI.addActionListener(this);
    loadTLMI.setToolTipText(
      "Load TL module from file.tl");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadTLMI);

    JMenuItem loadCSTLMI = 
      new JMenuItem("Load CSTL",openIcon);
    loadCSTLMI.addActionListener(this);
    loadCSTLMI.setToolTipText(
      "Load CSTL module from file.cstl");
        // loadEcoreMI.setMnemonic(KeyEvent.VK_L);
    loadMTMenu.add(loadCSTLMI);

    fileMenu.addSeparator(); 

    JMenuItem tl2umlrsds = 
      new JMenuItem("Map TL to bx"); 
    tl2umlrsds.addActionListener(this);
    fileMenu.add(tl2umlrsds);

    JMenuItem checkTL = 
      new JMenuItem("Check TL wrt model"); 
    checkTL.addActionListener(this);
    fileMenu.add(checkTL);

    fileMenu.addSeparator(); 

    JMenuItem loadDataRequirements = 
      new JMenuItem("Formalise data requirements"); 
    loadDataRequirements.addActionListener(this);
    loadDataRequirements.setToolTipText(
      "Creates UML/OCL in output/mm.km3 from POS-tagged/parsed text in output/nlpout.txt. Needs output/background.txt thesaurus.");
    fileMenu.add(loadDataRequirements);

    JMenuItem loadbehaviourRequirements = 
      new JMenuItem("Formalise behaviour requirements"); 
    loadbehaviourRequirements.addActionListener(this);
    loadbehaviourRequirements.setToolTipText(
      "Creates UML/OCL in output/mm.km3 from POS-tagged text in output/tagged.txt.");
    fileMenu.add(loadbehaviourRequirements);

    JMenuItem loadopRequirements = 
      new JMenuItem("Formalise operation requirements"); 
    loadopRequirements.addActionListener(this);
    loadopRequirements.setToolTipText(
      "Creates UML/OCL in output/mm.km3 from POS-tagged/parsed text in output/nlpout.txt. Needs output/background.txt thesaurus.");
    fileMenu.add(loadopRequirements);

    fileMenu.addSeparator(); 

    JMenuItem loadGenericMI = 
      new JMenuItem("From Java AST",openIcon);
    loadGenericMI.addActionListener(this);
    loadGenericMI.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR Java parser, in output/ast.txt");
    // loadDataMI.setMnemonic(KeyEvent.VK_L);
    fileMenu.add(loadGenericMI);

    JMenuItem fromCMI = 
      new JMenuItem("From C AST",openIcon);
    fromCMI.addActionListener(this);
    fromCMI.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR C parser, in output/ast.txt");
    // loadDataMI.setMnemonic(KeyEvent.VK_L);
    fileMenu.add(fromCMI);

    JMenuItem fromJSMI = 
      new JMenuItem("From JavaScript AST",openIcon);
    fromJSMI.addActionListener(this);
    fromJSMI.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR JavaScript parser, in output/ast.txt");
    // loadDataMI.setMnemonic(KeyEvent.VK_L);
    fileMenu.add(fromJSMI);

    JMenuItem fromVBMI = 
      new JMenuItem("From VB AST",openIcon);
    fromVBMI.addActionListener(this);
    fromVBMI.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR VisualBasic6 parser, in output/ast.txt");
    fileMenu.add(fromVBMI);

    JMenuItem fromCobol = 
      new JMenuItem("From COBOL AST",openIcon);
    fromCobol.addActionListener(this);
    fromCobol.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR Cobol85 parser, in output/ast.txt");
    fileMenu.add(fromCobol);

    JMenuItem fromPython = 
      new JMenuItem("From Python AST",openIcon);
    fromPython.addActionListener(this);
    fromPython.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR Python parser, in output/ast.txt");
    fileMenu.add(fromPython);

    JMenuItem fromPascal = 
      new JMenuItem("From Pascal AST",openIcon);
    fromPascal.addActionListener(this);
    fromPascal.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR pascal parser, in output/ast.txt");
    fileMenu.add(fromPascal);

    JMenuItem fromSQL = 
      new JMenuItem("From SQL AST",openIcon);
    fromSQL.addActionListener(this);
    fromSQL.setToolTipText(
      "Creates UML/OCL from AST produced by ANTLR SQLite parser, in output/ast.txt");
    fileMenu.add(fromSQL);

    fileMenu.addSeparator(); 

    JMenuItem randomModel = 
      new JMenuItem("Random model");
    randomModel.addActionListener(this);
    randomModel.setToolTipText(
      "Creates random UML model");
    fileMenu.add(randomModel);

    JMenuItem randomModels = 
      new JMenuItem("Random models (to Java)");
    randomModels.addActionListener(this);
    randomModels.setToolTipText(
      "Creates random UML models and translates to Java");
    fileMenu.add(randomModels);

    JMenuItem randomModelsPy = 
      new JMenuItem("Random models (to Python)");
    randomModelsPy.addActionListener(this);
    randomModelsPy.setToolTipText(
      "Creates random UML models and translates to Python");
    fileMenu.add(randomModelsPy);

    fileMenu.addSeparator(); 

    JMenu loadComponentMenu = 
      new JMenu("Load component");
    fileMenu.add(loadComponentMenu);

    JMenuItem loadDateMI = 
      new JMenuItem("Date",saveIcon); 
    loadDateMI.addActionListener(this); 
    loadComponentMenu.add(loadDateMI);  

    JMenuItem loadXMLParserMI = 
      new JMenuItem("XMLParser",saveIcon); 
    loadXMLParserMI.addActionListener(this); 
    loadComponentMenu.add(loadXMLParserMI);  

    JMenuItem convertMenu = new JMenu("Convert"); 
    fileMenu.add(convertMenu); 

    JMenuItem mapTL2CSTL = new JMenuItem("Map TL to CSTL"); 
    mapTL2CSTL.addActionListener(this); 
    convertMenu.add(mapTL2CSTL);     

    JMenuItem convertMI = new JMenuItem("Convert XSI (small) to Data"); 
    convertMI.addActionListener(this); 
    convertMenu.add(convertMI);     

    JMenuItem convert2MI = new JMenuItem("Convert XSI (large) to Data"); 
    convert2MI.addActionListener(this); 
    convertMenu.add(convert2MI);     


    JMenuItem saveMenu = new JMenu("Save As");
    // saveMenu.setMnemonic(KeyEvent.VK_S);
    fileMenu.add(saveMenu);

    // JMenuItem saveJavaMI = new JMenuItem("Save Java");
    // saveJavaMI.addActionListener(this);
    // saveMenu.add(saveJavaMI);

    // JMenuItem saveBMI = new JMenuItem("Save B");
    // saveBMI.addActionListener(this);
    // saveMenu.add(saveBMI);

    JMenuItem saveDataMI = 
      new JMenuItem("Save as data",saveIcon); 
    saveDataMI.addActionListener(this); 
    saveMenu.add(saveDataMI);  

    JMenuItem saveModelMI = 
      new JMenuItem("Save design",saveIcon); 
    saveModelMI.addActionListener(this); 
    saveMenu.add(saveModelMI);  
    
    JMenuItem saveEMFMI = new JMenuItem("Save as EMF"); 
    saveEMFMI.addActionListener(this); 
    saveMenu.add(saveEMFMI); 

    JMenuItem saveKM3MI = new JMenuItem("Save as KM3"); 
    saveKM3MI.addActionListener(this); 
    saveMenu.add(saveKM3MI); 

    JMenuItem savesimpKM3MI = new JMenuItem("Simple KM3"); 
    savesimpKM3MI.addActionListener(this); 
    saveMenu.add(savesimpKM3MI); 

    JMenuItem saveEcoreMI = new JMenuItem("Save as Ecore"); 
    saveEcoreMI.addActionListener(this); 
    saveMenu.add(saveEcoreMI); 

    // JMenuItem saveSmvMI = new JMenuItem("Save Smv"); 
    // saveSmvMI.addActionListener(this); 
    // saveMenu.add(saveSmvMI); 

    JMenuItem saveXmlMI = new JMenuItem("Save as XMI"); 
    saveXmlMI.addActionListener(this); 
    saveMenu.add(saveXmlMI); 

    JMenuItem saveUSEMI = new JMenuItem("Save as USE"); 
    saveUSEMI.addActionListener(this); 
    saveMenu.add(saveUSEMI); 

    JMenuItem saveplantMI = new JMenuItem("Save as PlantUML"); 
    saveplantMI.addActionListener(this); 
    saveMenu.add(saveplantMI); 


    JMenuItem saveCSVMI = new JMenuItem("Save as CSV"); 
    saveCSVMI.addActionListener(this); 
    saveMenu.add(saveCSVMI); 

    fileMenu.addSeparator();

    JMenuItem printMI = new JMenuItem("Print"); 
    printMI.addActionListener(this);
    printMI.setMnemonic(KeyEvent.VK_P);
    fileMenu.add(printMI); 

    // fileMenu.addSeparator();
    // JMenuItem getAbsMap = 
    //   new JMenuItem("Abstraction mapping"); 
    // getAbsMap.addActionListener(this); 
    // fileMenu.add(getAbsMap); 

    fileMenu.addSeparator(); 
    
    JMenuItem exitMenu = new JMenuItem("Exit");
    exitMenu.addActionListener(this);
    exitMenu.setMnemonic(KeyEvent.VK_X);
    fileMenu.add(exitMenu); 
    
    //Build second menu in the menu bar.
    JMenu createMenu = new JMenu("Create");
    createMenu.setToolTipText(
      "Create classes, features and invariants");
    createMenu.setMnemonic(KeyEvent.VK_C);
    menuBar.add(createMenu);
    
    //a group of JMenuItems for Create
    JMenuItem classMenu = new JMenuItem("Class");
    classMenu.addActionListener(this); 
    createMenu.add(classMenu);

    JMenuItem typeMenu = new JMenuItem("Type");
    typeMenu.addActionListener(this);
    createMenu.add(typeMenu);

    // JMenuItem sensorMenu = new JMenu("Sensor");
    // createMenu.add(sensorMenu);

    // JMenuItem toggleMenu = new JMenuItem("Toggle"); 
    // toggleMenu.addActionListener(this);
    // sensorMenu.add(toggleMenu); 

    // JMenuItem switchMenu = 
    //  new JMenuItem("Switch",switchIcon); 
    // switchMenu.addActionListener(this);
    // sensorMenu.add(switchMenu); 

    // JMenuItem singlestateMenu = 
    //   new JMenuItem("Single State",singstateIcon); 
    // singlestateMenu.addActionListener(this); 
    // sensorMenu.add(singlestateMenu); 

    // JMenuItem radiobutt2Menu = 
    //   new JMenuItem("Radio Button (2)");
    // radiobutt2Menu.addActionListener(this);
    // sensorMenu.add(radiobutt2Menu);

    // JMenuItem rb3MI = new JMenuItem("Radio Button (3)");
    // rb3MI.addActionListener(this);
    // sensorMenu.add(rb3MI);

    // JMenuItem linmeasMenu = 
    //   new JMenuItem("Linear Measure");
    // linmeasMenu.addActionListener(this);
    // sensorMenu.add(linmeasMenu);

    // JMenuItem attMI = new JMenuItem("Attribute"); 
    // attMI.addActionListener(this);
    // sensorMenu.add(attMI); 

    // JMenuItem generalsensorMenu = 
    //   new JMenuItem("General Sensor"); 
    // generalsensorMenu.addActionListener(this);
    // sensorMenu.add(generalsensorMenu); 

    // JMenuItem actuatorMenu = new JMenu("Actuator");
    // createMenu.add(actuatorMenu);

    // JMenuItem onoffMenu = 
    //   new JMenuItem("OnOff",bistateIcon); 
    // onoffMenu.addActionListener(this);
    // actuatorMenu.add(onoffMenu); 

    // JMenuItem twowayMI = new JMenuItem("TwoWay"); 
    // twowayMI.addActionListener(this); 
    // actuatorMenu.add(twowayMI); 

    // JMenuItem singstatMI =
    //   new JMenuItem("SingleState",singstateIcon);
    // singstatMI.addActionListener(this);
    // actuatorMenu.add(singstatMI);

    // JMenuItem generalactuatorMenu = 
    //   new JMenuItem("General Actuator");    
    // generalactuatorMenu.addActionListener(this);
    // actuatorMenu.add(generalactuatorMenu);

    // JMenuItem subunitMenu = new JMenu("Subunit");   
    // createMenu.add(subunitMenu);

    // JMenuItem timerMI = new JMenuItem("Timer"); 
    // timerMI.addActionListener(this);
    // subunitMenu.add(timerMI); 

    // JMenuItem valve4stateMenu = 
    //   new JMenuItem("Valve",valveIcon);
    // valve4stateMenu.addActionListener(this);
    // subunitMenu.add(valve4stateMenu);

    // JMenuItem ovalMenu = new JMenuItem("Controller");
    // ovalMenu.addActionListener(this);
    // createMenu.add(ovalMenu);

    JMenuItem assocMI = new JMenuItem("Association"); 
    assocMI.addActionListener(this);
    createMenu.add(assocMI); 

    JMenuItem inheritMI = new JMenuItem("Inheritance");
    inheritMI.addActionListener(this);
    createMenu.add(inheritMI);

    JMenuItem assocCMI = new JMenuItem("Association Class"); 
    assocCMI.addActionListener(this);
    createMenu.add(assocCMI); 

    JMenuItem ucMI = new JMenu("Use Case"); 
    // ucMI.addActionListener(this); 
    createMenu.add(ucMI);


    JMenuItem genusecase = 
      new JMenuItem("General Use Case"); 
    genusecase.addActionListener(this);
    ucMI.add(genusecase);

    JMenuItem eisusecase = 
      new JMenuItem("EIS Use Case"); 
    eisusecase.addActionListener(this);
    ucMI.add(eisusecase);

    // and special cases, such as loadXmi, loadCSV

    JMenuItem scMI = new JMenuItem("Entity Statemachine");
    scMI.addActionListener(this);
    createMenu.add(scMI);

    JMenuItem opscMI = new JMenuItem("Operation Statemachine");
    opscMI.addActionListener(this);
    createMenu.add(opscMI);

    JMenuItem cactMI = new JMenuItem("Entity Activity");
    cactMI.addActionListener(this);
    createMenu.add(cactMI);

    JMenuItem opactMI = new JMenuItem("Operation Activity");
    opactMI.addActionListener(this);
    createMenu.add(opactMI);

    JMenuItem ucactMI = new JMenuItem("Use Case Activity");
    ucactMI.addActionListener(this);
    createMenu.add(ucactMI);

    JMenuItem interactioncMI = new JMenuItem("Interaction");
    interactioncMI.addActionListener(this);
    createMenu.add(interactioncMI);

    JMenuItem reqMI = new JMenuItem("Requirements");
    reqMI.addActionListener(this);
    createMenu.add(reqMI);

    JMenuItem archMI = new JMenuItem("Architecture");
    archMI.addActionListener(this);
    createMenu.add(archMI);

    JMenuItem guiMI = new JMenuItem("GUI");
    guiMI.addActionListener(this);
    createMenu.add(guiMI);

    JMenuItem componentMI = new JMenuItem("Component");
    componentMI.addActionListener(this); 
    createMenu.add(componentMI);

    createMenu.addSeparator(); 

    // JMenuItem transMenu = new JMenu("Event flow");
    // createMenu.add(transMenu);

    // JMenuItem inputMenu = new JMenuItem("Input");
    // inputMenu.addActionListener(this);
    // transMenu.add(inputMenu);

    // JMenuItem outputMenu = new JMenuItem("Output");
    // outputMenu.addActionListener(this);
    // transMenu.add(outputMenu);
      
    // JMenuItem invMenu = new JMenu("Invariant"); 
    // createMenu.add(invMenu); 

    JMenuItem safetyinv = 
      new JMenuItem("Invariant"); 
    safetyinv.addActionListener(this);
    createMenu.add(safetyinv);

    // JMenuItem operationalinv = 
    //   new JMenuItem("Action Invariant"); 
    // operationalinv.addActionListener(this);
    // invMenu.add(operationalinv);

    // JMenuItem temporalinv = 
    //   new JMenuItem("Temporal Invariant");
    // temporalinv.addActionListener(this);
    // invMenu.add(temporalinv);

    /* Edit Menu */ 
    JMenu editMenu = new JMenu("Edit"); 
    editMenu.setMnemonic(KeyEvent.VK_E);
    editMenu.setToolTipText("Edit Class diagram");
    menuBar.add(editMenu);


    JMenuItem editKM3 = new JMenuItem("Edit KM3"); 
    editKM3.addActionListener(this); 
    editMenu.add(editKM3); 

    editMenu.addSeparator(); 

    JMenuItem moveElement = new JMenuItem("Move");
    JMenuItem deleteElement = new JMenuItem("Delete");
    JMenuItem modifyElement = new JMenuItem("Modify"); 
    JMenuItem moveallright = new JMenuItem("Move all right"); 
    JMenuItem movealldown = new JMenuItem("Move all down"); 
    JMenuItem moveallleft = new JMenuItem("Move all left"); 
    JMenuItem moveallup = new JMenuItem("Move all up"); 

    moveElement.addActionListener(this);
    deleteElement.addActionListener(this);
    modifyElement.addActionListener(this);
    moveallright.addActionListener(this); 
    movealldown.addActionListener(this); 
    moveallleft.addActionListener(this); 
    moveallup.addActionListener(this); 

    editMenu.add(moveElement);
    editMenu.add(deleteElement);
    editMenu.add(modifyElement); 
    editMenu.add(moveallright); 
    editMenu.add(movealldown); 
    editMenu.add(moveallleft); 
    editMenu.add(moveallup); 

    editMenu.addSeparator(); 


    JMenuItem editUseCase = new JMenuItem("Edit Use Case"); 
    editUseCase.addActionListener(this); 
    editUseCase.setMnemonic(KeyEvent.VK_U);
    editMenu.add(editUseCase); 

    JMenuItem deleteUseCase = new JMenuItem("Delete Use Case"); 
    deleteUseCase.addActionListener(this); 
    editMenu.add(deleteUseCase); 

    JMenuItem editActivity = new JMenuItem("Edit Operation Activity"); 
    editActivity.addActionListener(this); 
    editMenu.add(editActivity); 

    JMenuItem editEntActivity = new JMenuItem("Edit Entity Activity"); 
    editEntActivity.addActionListener(this); 
    editMenu.add(editEntActivity); 

    JMenuItem editUCActivity = new JMenuItem("Edit Use Case Activity"); 
    editUCActivity.addActionListener(this); 
    editMenu.add(editUCActivity); 


    JMenuItem deleteInvs = 
      new JMenuItem("Delete Invariant");
    deleteInvs.addActionListener(this);
    editMenu.add(deleteInvs);

    /* Analyse Menu */ 
    JMenu analyseMenu = new JMenu("Analyse"); 
    analyseMenu.setToolTipText(
      "Type-check and quality analyse models");
    analyseMenu.setMnemonic(KeyEvent.VK_A); 
    menuBar.add(analyseMenu); 

    JMenuItem tcMenuItem = new JMenuItem("Type-check"); 
    tcMenuItem.addActionListener(this); 
    tcMenuItem.setToolTipText(
      "Re-check expressions after a class diagram change");
    tcMenuItem.setEnabled(true); 
    // desMenuItem.setMnemonic(KeyEvent.VK_D);
    analyseMenu.add(tcMenuItem); 

    JMenuItem tinferenceMenuItem = new JMenuItem("Type inference"); 
    tinferenceMenuItem.addActionListener(this); 
    tinferenceMenuItem.setToolTipText(
      "Infers types where possible");
    tinferenceMenuItem.setEnabled(true); 
    // desMenuItem.setMnemonic(KeyEvent.VK_D);
    analyseMenu.add(tinferenceMenuItem); 

    JMenuItem qualCheck = 
      new JMenuItem("Quality check"); 
    qualCheck.setToolTipText(
      "Checks for code smells and other flaws");
    qualCheck.addActionListener(this);
    analyseMenu.add(qualCheck);

    JMenuItem measuresItem = new JMenuItem("Quality measures"); 
    measuresItem.addActionListener(this);
    analyseMenu.add(measuresItem);

    JMenuItem energyAnal = new JMenuItem("Energy analysis"); 
    energyAnal.addActionListener(this);
    analyseMenu.add(energyAnal);

    JMenuItem carchItem = new JMenuItem("Clean architecture properties"); 
    carchItem.addActionListener(this);
    analyseMenu.add(carchItem);

    JMenuItem ddepsItem = new JMenuItem("Data dependencies"); 
    ddepsItem.addActionListener(this);
    analyseMenu.add(ddepsItem);

    /* View Menu */ 
    JMenu viewMenu = new JMenu("View"); 
    viewMenu.setToolTipText(
      "View models and generated code");
    viewMenu.setMnemonic(KeyEvent.VK_V); 
    menuBar.add(viewMenu); 


    // JMenuItem cdItem = new JMenuItem("Class Diagram"); 
    // cdItem.addActionListener(this);
    // viewMenu.add(cdItem);

    // viewMenu.addSeparator(); 

    JMenuItem invMenuItem = new JMenuItem("Invariants"); 
    invMenuItem.addActionListener(this); 
    viewMenu.add(invMenuItem); 

    // JMenuItem sensorsMenuItem = 
    //   new JMenuItem("Sensors"); 
    // sensorsMenuItem.addActionListener(this);
    // viewMenu.add(sensorsMenuItem); 

    // JMenuItem actuatorsMenuItem = 
    //   new JMenuItem("Actuators");
    // actuatorsMenuItem.addActionListener(this);
    // viewMenu.add(actuatorsMenuItem);

    JMenuItem typesMI = new JMenuItem("Types"); 
    typesMI.addActionListener(this);
    viewMenu.add(typesMI);

    JMenuItem operationsMI = new JMenuItem("Operations"); 
    operationsMI.addActionListener(this);
    viewMenu.add(operationsMI);

    JMenuItem activityviewMI = new JMenuItem("Activities"); 
    activityviewMI.addActionListener(this);
    viewMenu.add(activityviewMI);

    JMenuItem ucsMI = new JMenuItem("Use cases"); 
    ucsMI.addActionListener(this);
    viewMenu.add(ucsMI);

    JMenuItem oclMI = new JMenuItem("OCL & ASTs"); 
    oclMI.addActionListener(this);
    viewMenu.add(oclMI);

    viewMenu.addSeparator(); 


    JMenuItem ucdepsMI = new JMenuItem("Use Case Dependencies"); 
    ucdepsMI.addActionListener(this);
    viewMenu.add(ucdepsMI);

    JMenuItem compareItem = new JMenuItem("Compare models"); 
    compareItem.addActionListener(this);
    viewMenu.add(compareItem);


    viewMenu.addSeparator();

    JMenuItem contbcode = 
      new JMenuItem("Controller B Code"); 
    contbcode.addActionListener(this);
    viewMenu.add(contbcode);
    contbcode.setEnabled(false); 

    JMenuItem contJavacode = 
      new JMenuItem("Controller Java Code");
    contJavacode.addActionListener(this);
    viewMenu.add(contJavacode);
    contJavacode.setEnabled(false); 

    JMenuItem smvModsMI = new JMenuItem("Smv Modules"); 
    smvModsMI.addActionListener(this); 
    viewMenu.add(smvModsMI);
    smvModsMI.setEnabled(false); 

    JMenuItem subsystemsMI =
      new JMenuItem("Subsystems"); 
    subsystemsMI.addActionListener(this);
    viewMenu.add(subsystemsMI);
    subsystemsMI.setEnabled(false); 

    /* Transformation Menu */ 
    JMenu transMenu = new JMenu("Transform"); 
    transMenu.setMnemonic(KeyEvent.VK_T);
    transMenu.setToolTipText("Transform UML Models"); 
    menuBar.add(transMenu); 

    JMenuItem qualityMenu = new JMenu("Refactoring"); 
    transMenu.add(qualityMenu); 

    JMenuItem extractIntf = 
      new JMenuItem("Extract Interface"); 
    extractIntf.setToolTipText(
      "Defines new interface with operations of the class");
    extractIntf.addActionListener(this);
    qualityMenu.add(extractIntf);

    JMenuItem extractOper = 
      new JMenuItem("Extract Operation"); 
    extractOper.setToolTipText(
      "Defines new operation for cloned expressions/statements");
    extractOper.addActionListener(this);
    qualityMenu.add(extractOper);

    JMenuItem extractComponent = 
      new JMenuItem("Split class"); 
    extractComponent.setToolTipText(
      "Splits class into client/supplier components");
    extractComponent.addActionListener(this);
    qualityMenu.add(extractComponent);

    JMenuItem remredin = 
      new JMenuItem("Remove Redundant Inheritance"); 
    remredin.addActionListener(this);
    qualityMenu.add(remredin);

    JMenuItem remmultinherit = 
      new JMenuItem("Remove Multiple Inheritance"); 
    remmultinherit.addActionListener(this);
    qualityMenu.add(remmultinherit);


    JMenuItem introsup = 
      new JMenuItem("Introduce Superclass"); 
    introsup.addActionListener(this);
    qualityMenu.add(introsup);

    JMenuItem pushdownatt = 
      new JMenuItem("Pushdown abstract features"); 
    pushdownatt.addActionListener(this);
    qualityMenu.add(pushdownatt);

    // qualityMenu.addSeparator(); 

    JMenuItem moveatt = 
      new JMenuItem("Move attribute"); 
    moveatt.addActionListener(this);
    qualityMenu.add(moveatt);

    JMenuItem moveop = 
      new JMenuItem("Move operation"); 
    moveop.addActionListener(this);
    moveop.setToolTipText(
      "Moves operation from one class to another");
    qualityMenu.add(moveop);

    JMenuItem moveopto = 
      new JMenuItem("Move operation to parameter class"); 
    moveopto.setToolTipText(
      "Moves operation into class of first class-typed parameter");
    moveopto.addActionListener(this);
    qualityMenu.add(moveopto);

    JMenuItem removeCallDefinition = 
      new JMenuItem("Replace call by definition"); 
    removeCallDefinition.addActionListener(this);
    removeCallDefinition.setToolTipText(
      "Replaces calls of operation by operation code");
    qualityMenu.add(removeCallDefinition);

    JMenuItem removeRecursionop = 
      new JMenuItem("Replace recursion by loops"); 
    removeRecursionop.addActionListener(this);
    removeRecursionop.setToolTipText(
      "Replaces self calls of operation by loop code where possible");
    qualityMenu.add(removeRecursionop);

    JMenuItem splitSegmentsop = 
      new JMenuItem("Split operation"); 
    splitSegmentsop.addActionListener(this);
    splitSegmentsop.setToolTipText(
      "Splits operation code into segments where possible");
    qualityMenu.add(splitSegmentsop);

    JMenuItem hoistDecsop = 
      new JMenuItem("Hoist local declarations"); 
    hoistDecsop.addActionListener(this);
    hoistDecsop.setToolTipText(
      "Hoists local declarations to start of operation code");
    qualityMenu.add(hoistDecsop);

    JMenuItem reduceNestingop = 
      new JMenuItem("Reduce code nesting"); 
    reduceNestingop.addActionListener(this);
    reduceNestingop.setToolTipText(
      "Reduces depth of code nesting where possible");
    qualityMenu.add(reduceNestingop);

    JMenuItem refineMenu = new JMenu("Refinement"); 
    transMenu.add(refineMenu); 

    JMenuItem sc2cd = 
      new JMenuItem("Express Statemachine on Class Diagram"); 
    sc2cd.addActionListener(this); 
    refineMenu.add(sc2cd); 

    JMenuItem introprim = 
      new JMenuItem("Introduce Primary Key"); 
    introprim.addActionListener(this);
    refineMenu.add(introprim);

    JMenuItem introprimAll = 
      new JMenuItem("Add all Primary Keys"); 
    introprimAll.addActionListener(this);
    refineMenu.add(introprimAll);

    JMenuItem remmanymany = 
      new JMenuItem("Remove *--* Associations"); 
    remmanymany.addActionListener(this);
    refineMenu.add(remmanymany);

    JMenuItem reminherit = 
      new JMenuItem("Replace Inheritance by Association"); 
    reminherit.addActionListener(this); 
    refineMenu.add(reminherit); 

    JMenuItem aggsubs = 
      new JMenuItem("Aggregate Subclasses"); 
    aggsubs.addActionListener(this); 
    refineMenu.add(aggsubs); 

    JMenuItem remacs = 
      new JMenuItem("Remove Association Classes"); 
    remacs.addActionListener(this);
    refineMenu.add(remacs);

    JMenuItem introforeign = 
      new JMenuItem("Introduce Foreign Key"); 
    introforeign.addActionListener(this);
    refineMenu.add(introforeign);

    JMenuItem introbacktracking = 
      new JMenuItem("Matching by backtracking"); 
    introbacktracking.addActionListener(this);
    refineMenu.add(introbacktracking);


    JMenuItem patternsMenu = new JMenu("Design Patterns"); 
    transMenu.add(patternsMenu); 

    JMenuItem valueObjectMI = new JMenuItem("Value Object"); 
    valueObjectMI.addActionListener(this);
    valueObjectMI.setToolTipText(
      "Puts group of operation parameters into a new class");
    patternsMenu.add(valueObjectMI);

    JMenuItem singletonMI = new JMenuItem("Singleton"); 
    singletonMI.addActionListener(this);
    singletonMI.setToolTipText(
      "Defines a selected class to be a singleton");
    patternsMenu.add(singletonMI);

    JMenuItem observerMI = new JMenuItem("Observer"); 
    observerMI.addActionListener(this);
    observerMI.setToolTipText(
      "Optimised version of Observer pattern");
    patternsMenu.add(observerMI);

    JMenuItem iteratorMI = new JMenuItem("Iterator"); 
    iteratorMI.addActionListener(this);
    iteratorMI.setToolTipText(
      "Optimised version of Iterator pattern");
    patternsMenu.add(iteratorMI);

    JMenuItem blackboardMI = new JMenuItem("Blackboard"); 
    blackboardMI.addActionListener(this);
    blackboardMI.setToolTipText(
      "Optimised version of Blackboard pattern");
    patternsMenu.add(blackboardMI);

    JMenuItem mvcMI = new JMenuItem("MVC"); 
    mvcMI.addActionListener(this);
    mvcMI.setToolTipText(
      "Optimised version of MVC pattern");
    patternsMenu.add(mvcMI);

    JMenuItem facadeMI = new JMenuItem("Facade"); 
    facadeMI.setToolTipText(
      "Checks if 2+ classes all reference 2+ same other classes");
    facadeMI.addActionListener(this);
    patternsMenu.add(facadeMI);

    JMenuItem phasedconsMI = new JMenuItem("Phased Construction"); 
    phasedconsMI.setToolTipText(
      "Removes nested exists(forAll(exists quantifiers in succedents");
    phasedconsMI.addActionListener(this);
    patternsMenu.add(phasedconsMI);

    JMenuItem implicitcopyMI = new JMenuItem("Implicit Copy"); 
    implicitcopyMI.addActionListener(this);
    implicitcopyMI.setToolTipText(
      "Defines copy transformation based on language\n" + "interpretation from source to target");
    patternsMenu.add(implicitcopyMI);

    JMenuItem auxmetaMI = new JMenuItem("Auxiliary Metamodel"); 
    auxmetaMI.setToolTipText(
      "Defines trace class between source and target entities");
    auxmetaMI.addActionListener(this);
    patternsMenu.add(auxmetaMI);

    /* Synthesise Menu */ 
    JMenu synthMenu = new JMenu("Synthesis"); 
    synthMenu.setMnemonic(KeyEvent.VK_S);
    synthMenu.setToolTipText(
      "Development Process Steps"); 
    menuBar.add(synthMenu); 

    JMenuItem testsMI = new JMenuItem("Generate tests"); 
    testsMI.addActionListener(this);
    testsMI.setToolTipText(
      "Generates MutationTest and TestsGUI");
    // testsMI.setEnabled(false); 
    synthMenu.add(testsMI); 

    JMenuItem desMenuItem = new JMenuItem("Generate Design"); 
    desMenuItem.addActionListener(this); 
    desMenuItem.setToolTipText(
      "Produces imperative UML version of the specification use cases");
    desMenuItem.setEnabled(true); 
    desMenuItem.setMnemonic(KeyEvent.VK_D);
    synthMenu.add(desMenuItem); 

    synthMenu.addSeparator(); 

    JMenuItem requirements2TL = 
      new JMenuItem("MT requirements to TL"); 
    requirements2TL.addActionListener(this);
    synthMenu.add(requirements2TL);
    
    synthMenu.addSeparator(); 

    JMenuItem flatten = 
      new JMenu("Synthesise transformation"); 
    synthMenu.add(flatten);
    
    JMenuItem namesimilarity = 
      new JMenuItem("Name similarity (NSS)"); 
    namesimilarity.addActionListener(this);
    flatten.add(namesimilarity);

    JMenuItem flatten2 = 
      new JMenuItem("Name semantics (NMS)"); 
    flatten2.addActionListener(this);
    flatten.add(flatten2);

    JMenuItem allmaps = new JMenuItem("All maps (DSS)"); 
    flatten.add(allmaps); 
    allmaps.addActionListener(this); 


    JMenuItem flatten1 = 
      new JMenuItem("injective maps (DSS)"); 
    flatten1.addActionListener(this);
    flatten.add(flatten1);

    JMenuItem flattenstrict = 
      new JMenuItem("inheritance-preserving (DSS)"); 
    flattenstrict.addActionListener(this);
    flatten.add(flattenstrict);

    JMenuItem iteroptmaps = new JMenuItem("Iterative optimisation"); 
    flatten.add(iteroptmaps); 
    iteroptmaps.addActionListener(this); 


    JMenuItem graphstructure = 
      new JMenuItem("Graph structure (GSS)"); 
    graphstructure.addActionListener(this);
    flatten.add(graphstructure);

    JMenuItem geditdistance = 
      new JMenuItem("Graph edit distance (GES)"); 
    geditdistance.addActionListener(this);
    flatten.add(geditdistance);


    JMenuItem ontosimilarity = 
      new JMenuItem("Ontological similarity (SCS)"); 
    ontosimilarity.addActionListener(this);
    flatten.add(ontosimilarity);

    JMenuItem refinementsimilarity = 
      new JMenuItem("Composite score"); 
    refinementsimilarity.addActionListener(this);
    flatten.add(refinementsimilarity);

    JMenuItem gasimilarity = 
      new JMenuItem("Genetic algorithm"); 
    gasimilarity.addActionListener(this);
    flatten.add(gasimilarity);

    JMenuItem fromloadedTL = 
      new JMenuItem("From loaded TL"); 
    fromloadedTL.addActionListener(this);
    flatten.add(fromloadedTL);

    synthMenu.addSeparator(); 

    JMenuItem cgbe = 
      new JMenuItem("CGBE"); 
    cgbe.addActionListener(this);
    cgbe.setToolTipText(
      "Learns CSTL from output/typeExamples.txt, expressionExamples, statementExamples, declarationExamples, paired source-target text example files. Needs configuration.txt, output/mmCGBE.txt, output/forwardCGBE.txt");
    synthMenu.add(cgbe);

    JMenuItem ltbeFromText = 
      new JMenuItem("LTBE from text"); 
    ltbeFromText.addActionListener(this);
    ltbeFromText.setToolTipText(
      "Learns CSTL from source-target paired text example files. Needs ANTLR parsers for source & target languages");
    synthMenu.add(ltbeFromText);

    JMenuItem ltbeFromASTs = 
      new JMenuItem("LTBE from ASTs"); 
    ltbeFromASTs.addActionListener(this);
    ltbeFromASTs.setToolTipText(
      "Learns CSTL from output/sourceasts.txt, targetasts AST example files");
    synthMenu.add(ltbeFromASTs);

    JMenuItem validateCGBE = 
      new JMenuItem("Validate CGBE"); 
    validateCGBE.setToolTipText(
      "Executes CSTL script on ASTS in output/asts.txt");
    validateCGBE.addActionListener(this);
    synthMenu.add(validateCGBE);

    synthMenu.addSeparator(); 

    JMenuItem normalise = 
      new JMenuItem("Form Contrapositives"); 
    normalise.addActionListener(this);
    synthMenu.add(normalise);

    consischeck = new JMenuItem("Transitive Closures"); 
    consischeck.addActionListener(this);
    // consischeck.setEnabled(false); 
    synthMenu.add(consischeck); 

    completenessCheck = 
      new JMenuItem("Completeness Analysis"); 
    completenessCheck.addActionListener(this); 
    completenessCheck.setEnabled(true); 
    synthMenu.add(completenessCheck); 

    JMenuItem consistencycheck = 
      new JMenuItem("Consistency Analysis"); 
    consistencycheck.addActionListener(this); 
    consistencycheck.setEnabled(true); 
    synthMenu.add(consistencycheck); 

    smvMI = new JMenuItem("Generate SMV");
    smvMI.addActionListener(this);
    smvMI.setEnabled(true);
    synthMenu.add(smvMI);

    // depends = new JMenuItem("Dependency Analysis"); 
    // depends.addActionListener(this);
    // depends.setEnabled(false); 
    // synthMenu.add(depends); 

    safety2op = 
      new JMenuItem("Static to Action Invariants"); 
    safety2op.addActionListener(this);
    safety2op.setEnabled(true); 
    synthMenu.add(safety2op); 


    testMenu = new JMenu("Generate B/Z3");
    // testMenu.addActionListener(this);
    testMenu.setEnabled(true); 
    synthMenu.add(testMenu);  // two options: machine for system, for entities

    JMenuItem bforentitiesMI = new JMenuItem("B for entities");
    bforentitiesMI.addActionListener(this);
    testMenu.add(bforentitiesMI);

    JMenuItem bforsystemMI = new JMenuItem("B for system");
    bforsystemMI.addActionListener(this);
    testMenu.add(bforsystemMI);

    JMenuItem z3forsystemMI = new JMenuItem("Z3 for system");
    z3forsystemMI.addActionListener(this);
    testMenu.add(z3forsystemMI);

    synthMenu.addSeparator(); 


    verifyMI = new JMenuItem("Verify Invariants"); 
    verifyMI.addActionListener(this);
    verifyMI.setToolTipText(
      "Checks system constraints against a model in output/out.txt");
    verifyMI.setEnabled(true); 
    synthMenu.add(verifyMI); 


    JMenu buildMenu = new JMenu("Build"); 
    /* Build Menu */ 
    buildMenu.setMnemonic(KeyEvent.VK_B);
    buildMenu.setToolTipText(
      "Builds implementations"); 
    menuBar.add(buildMenu); 

    javaMenu = new JMenuItem("Generate Java4"); 
    javaMenu.addActionListener(this);
    // javaMenu.setEnabled(false); 
    javaMenu.setMnemonic(KeyEvent.VK_J);
    buildMenu.add(javaMenu); 

    JMenuItem java6Menu = new JMenuItem("Generate Java6"); 
    java6Menu.addActionListener(this);
    // javaMenu.setEnabled(false); 
    java6Menu.setMnemonic(KeyEvent.VK_6);
    buildMenu.add(java6Menu); 


    JMenuItem java7Menu = new JMenuItem("Generate Java7"); 
    java7Menu.addActionListener(this);
    java7Menu.setMnemonic(KeyEvent.VK_7);
    buildMenu.add(java7Menu); 

    JMenuItem java8Menu = new JMenuItem("Generate Java8"); 
    java8Menu.addActionListener(this);
    java8Menu.setMnemonic(KeyEvent.VK_8);
    buildMenu.add(java8Menu); 

    buildMenu.addSeparator(); 

    JMenuItem csharpMenu = new JMenuItem("Generate C#"); 
    csharpMenu.addActionListener(this);
    // javaMenu.setEnabled(false); 
    buildMenu.add(csharpMenu); 

    JMenuItem cppMenu = new JMenuItem("Generate C++"); 
    cppMenu.addActionListener(this);
    cppMenu.setToolTipText(
      "C++11"); 
    // javaMenu.setEnabled(false); 
    buildMenu.add(cppMenu); 

    JMenuItem cMenu = new JMenuItem("Generate C header"); 
    cMenu.addActionListener(this);
    // javaMenu.setEnabled(false); 
    buildMenu.add(cMenu); 

    JMenuItem ccMenu = new JMenuItem("Generate C code"); 
    ccMenu.addActionListener(this);
    // javaMenu.setEnabled(false); 
    buildMenu.add(ccMenu); 

    JMenuItem pyMenu = new JMenuItem("Generate Python 3.8"); 
    pyMenu.addActionListener(this);
    pyMenu.setToolTipText(
      "Python 3.8"); 
    buildMenu.add(pyMenu); 

    JMenuItem py3Menu = new JMenuItem("Generate Python 3.10"); 
    py3Menu.addActionListener(this);
    py3Menu.setToolTipText(
      "Python 3.10"); 
    buildMenu.add(py3Menu);

    JMenuItem goMenu = new JMenuItem("Generate Go"); 
    goMenu.addActionListener(this);
    buildMenu.add(goMenu); 

    JMenuItem mambaMenu = new JMenuItem("Generate Mamba"); 
    mambaMenu.addActionListener(this);
    mambaMenu.setToolTipText(
      "Mamba zAppDev language https://zappdev.io/"); 
    buildMenu.add(mambaMenu); 

    /* JMenuItem runMI = new JMenuItem("Run"); 
    runMI.addActionListener(this);
    // javaMenu.setEnabled(false); 
    buildMenu.add(runMI); */ 

    buildMenu.addSeparator(); 

    JMenuItem cstlGenerator = new JMenuItem("Use CSTL specification"); 
    cstlGenerator.addActionListener(this);
    buildMenu.add(cstlGenerator); 

    JMenuItem cgtlGenerator = new JMenuItem("Use CGTL specification"); 
    cgtlGenerator.addActionListener(this);
    buildMenu.add(cgtlGenerator); 

    JMenuItem cstl4ast = new JMenuItem("Apply CSTL/CGTL to AST"); 
    cstl4ast.addActionListener(this);
    buildMenu.add(cstl4ast); 

    buildMenu.addSeparator(); 


    JMenu webMI = new JMenu("Web System"); 
    buildMenu.add(webMI); 
    
    JMenuItem servletMI = new JMenuItem("Servlet style"); 
    webMI.add(servletMI); 
    servletMI.addActionListener(this); 

    JMenuItem jspMI = new JMenuItem("JSP style"); 
    webMI.add(jspMI); 
    jspMI.addActionListener(this); 

    JMenuItem j2eeMI = new JMenuItem("J2EE style"); 
    webMI.add(j2eeMI); 
    j2eeMI.addActionListener(this); 

    buildMenu.addSeparator(); 

    JMenuItem androidMI = new JMenuItem("Android system"); 
    buildMenu.add(androidMI); 
    androidMI.addActionListener(this); 

    JMenuItem iosMI = new JMenuItem("iOS system"); 
    buildMenu.add(iosMI); 
    iosMI.addActionListener(this); 

    JMenuItem swiftuiMI = new JMenuItem("SwiftUI system"); 
    buildMenu.add(swiftuiMI); 
    swiftuiMI.addActionListener(this); 

    buildMenu.addSeparator(); 

    JMenu webserv = new JMenu("Web Service"); 
    buildMenu.add(webserv); 
    
    JMenuItem restMI = new JMenuItem("REST style"); 
    webserv.add(restMI); 
    restMI.addActionListener(this); 

    JMenuItem soapMI = new JMenuItem("SOAP style"); 
    webserv.add(soapMI); 
    soapMI.addActionListener(this); 

    /* JMenu decomposeMenu = new JMenu("Decomposition");
    decomposeMenu.setMnemonic(KeyEvent.VK_D);
    decomposeMenu.setToolTipText(
      "Design Decompositions");
    menuBar.add(decomposeMenu); 

    horizMI = new JMenuItem("Horizontal"); 
    horizMI.addActionListener(this);
    horizMI.setEnabled(false);
    decomposeMenu.add(horizMI); 

    hierMI = new JMenuItem("Hierarchical"); 
    hierMI.addActionListener(this); 
    decomposeMenu.add(hierMI); 
    hierMI.setEnabled(false);

    phaseMI = new JMenuItem("Phase/Mode"); 
    phaseMI.addActionListener(this);
    decomposeMenu.add(phaseMI); 
    phaseMI.setEnabled(false);
    JMenuItem dynphaseMI =
      new JMenuItem("Dynamic Phase/Mode"); 
    decomposeMenu.add(dynphaseMI); 
    dynphaseMI.setEnabled(false);

    decomposeMenu.addSeparator(); 

    faultdetMI = new JMenuItem("Fault Detection");
    faultdetMI.addActionListener(this);
    faultdetMI.setEnabled(false);
    decomposeMenu.add(faultdetMI);

    schedulMI = new JMenuItem("Scheduling");
    decomposeMenu.add(schedulMI);
    schedulMI.addActionListener(this);
    schedulMI.setEnabled(false); */ 

    JMenu extensionsMenu = new JMenu("Extensions");
    // fileMenu.setMnemonic(KeyEvent.VK_F);
    extensionsMenu.setToolTipText(
      "To define external libraries and extensions");
    menuBar.add(extensionsMenu);
    
    JMenuItem newImport = new JMenuItem("Import library"); 
    newImport.addActionListener(this); 
    extensionsMenu.add(newImport); 

    JMenuItem newOperator = new JMenuItem("Add -> operator"); 
    newOperator.addActionListener(this); 
    extensionsMenu.add(newOperator); 

    
    JMenu newExport = new JMenu("Export system as library"); 
    // newExport.addActionListener(this); 
    extensionsMenu.add(newExport); 
    JMenuItem exportJ4MI = new JMenuItem("Export as Java4"); 
    newExport.add(exportJ4MI); 
    exportJ4MI.addActionListener(this);
    JMenuItem exportJ6MI = new JMenuItem("Export as Java6"); 
    newExport.add(exportJ6MI); 
    exportJ6MI.addActionListener(this);
    JMenuItem exportCSMI = new JMenuItem("Export as C#"); 
    newExport.add(exportCSMI); 
    exportCSMI.addActionListener(this);

    for (int i = 0; i < plugins.size(); i++) 
    { String plugin = (String) plugins.get(i); 
      JMenuItem pitem = new JMenuItem(plugin); 
      extensionsMenu.add(pitem); 
      pitem.addActionListener(this); 
    } 

    JMenu helpMenu = new JMenu("Help"); 
    helpMenu.setMnemonic(KeyEvent.VK_H);
    helpMenu.setToolTipText("Help on the UML Tool");
    menuBar.add(helpMenu);
    JMenuItem helpMI = new JMenuItem("Help"); 
    helpMenu.add(helpMI); 
    helpMI.addActionListener(this);
  }

  public void setInvariants(Vector invs) 
  { invariants = invs; } 

  public void setPrefix(String pre) 
  { prefix = pre; } 

  public String getPrefix()
  { return prefix; } 

  public void setControllerName(String nme) 
  { controllerName = nme; } 

  public String getControllerName() 
  { return controllerName; } 

  public void setExtending(String ext) 
  { extending = ext; } 

  public Vector getSubsystemSpecs() 
  { return subsystemSpecs; } 

  public void actionPerformed(ActionEvent e)
  { Object eventSource = e.getSource();
    if (eventSource instanceof JMenuItem)
    { String label = (String) e.getActionCommand();
      if (label.equals("Exit"))
      { System.out.println("Exit UML Tool");
        dispose();
        System.exit(0);
      }
      else if (label.equals("Class"))
      { thisLabel.setText("Creating a class");
        buildSensorComponent();
        saved = false; 
      }
      else if (label.equals("Association")) //solid line
      { thisLabel.setText("Click and drag to create association");
        ucdArea.setDrawMode(UCDArea.SLINES);
        saved = false; 
      }
      else if (label.equals("Inheritance")) 
      { thisLabel.setText("Click and drag from subclass to draw inheritance");
        ucdArea.setDrawMode(UCDArea.DLINES);
        saved = false; 
      }
      else if (label.equals("Association Class")) //solid line
      { thisLabel.setText("Click and drag to create association class");
        // ucdArea.setDrawMode(UCDArea.ACLINES);
        buildAssociationClass(); 
        saved = false; 
      }
      else if (label.equals("EIS Use Case"))
      { ucdArea.addUseCase(); 
        saved = false; 
      }
      else if (label.equals("General Use Case"))
      { ucdArea.addGeneralUseCase(); 
        saved = false; 
      }
      else if (label.equals("Entity Statemachine"))
      { createStatechart(); } 
      else if (label.equals("Operation Statemachine"))
      { createOperationStatechart(); }
      else if (label.equals("Entity Activity"))
      { createEntityActivity(); } 
      else if (label.equals("Edit Entity Activity"))
      { editEntityActivity(); } 
      else if (label.equals("Operation Activity"))
      { createOperationActivity(); }
      else if (label.equals("Edit Operation Activity"))
      { editOperationActivity(); }
      else if (label.equals("Use Case Activity"))
      { createUseCaseActivity(); } 
      else if (label.equals("Edit Use Case Activity"))
      { editUseCaseActivity(); }
      else if (label.equals("Interaction"))
      { InteractionWin iwindow = 
          new InteractionWin("Interaction editor",ucdArea.getEntities());  
        iwindow.setSize(500, 400);
        iwindow.setTitle("Interaction editor"); 
        iwindow.setVisible(true); 
      }   
      else if (label.equals("Requirements"))
      { RequirementsWin rwindow = 
          new RequirementsWin("Requirements editor",ucdArea.getEntities(),ucdArea);  
        rwindow.setSize(500, 400);
        rwindow.setTitle("Requirements editor"); 
        rwindow.setVisible(true); 
      }   
      else if (label.equals("Architecture"))
      { ArchitectureWin awindow = 
          new ArchitectureWin("Architecture editor",ucdArea.getEntities(),ucdArea);  
        awindow.setSize(500, 400);
        awindow.setTitle("Architecture editor"); 
        awindow.setVisible(true); 
      }   
      else if (label.equals("GUI"))
      { buildGUI(); } 
      else if (label.equals("Component"))
      { ucdArea.loadComponent(); } 
      else if (label.equals("OCL & ASTs"))
      { ucdArea.printOCL(); } 
      else if (label.equals("Type"))
      { String tname = 
          JOptionPane.showInputDialog("Enter type name:");
        if (tname == null)
        { return; }
        if (tname.equals("int") || 
            tname.equals("String") || tname.equals("long") || 
            tname.equals("double") || 
            tname.equals("boolean") ||
            tname.equals("OclAny") ||
            tname.equals("Set") ||
            tname.equals("Sequence") || 
            tname.equals("Map") || 
            tname.equals("Function") || 
            tname.equals("OclType") )
        { System.err.println("ERROR: Cannot redefine inbuilt type!: " + tname);
          return;
        }
        String values =
          JOptionPane.showInputDialog("Enter values, separated by spaces:");
        StringTokenizer st = 
          new StringTokenizer(values); 
        Vector elements = new Vector(); 
        while (st.hasMoreTokens())
        { String se = st.nextToken().trim();
          elements.add(se); 
        } 
        ucdArea.addType(tname,elements); 
        saved = false; 
      } 
      else if (label.equals("Date"))
      { ucdArea.createDateComponent(); }
      else if (label.equals("XMLParser"))
      { ucdArea.createXMLParserComponent(); }
      else if (label.equals("Single State"))
      { }
      else if (label.equals("Radio Button (2)")) 
      { }
      else if (label.equals("Radio Button (3)"))
      { }
      else if (label.equals("Linear Measure")) 
      { }
      else if (label.equals("Attribute"))
      { }
      else if (label.equals("General Sensor"))
      { thisLabel.setText("Creating a sensor component");
        // currentBuilder = null; 
        // ucdArea.setDrawMode(UCDArea.POINTS);
        // ucdArea.setComponentMode(UCDArea.SENSOR); 
        buildSensorComponent(); 
      }
      // else if (label.equals("OnOff"))
      // { }
      // else if (label.equals("TwoWay"))
      // { }
      // else if (label.equals("Valve"))
      // { }
      // else if (label.equals("Timer"))
      // { }
      // else if (label.equals("SingleState"))
      // { }
      // else if (label.equals("General Actuator"))
      // { currentBuilder = null; 
      //   thisLabel.setText("Creating actuator component");
      //   // ucdArea.setDrawMode(UCDArea.POINTS);
      //   // ucdArea.setComponentMode(UCDArea.ACTUATOR);
      //   buildActuatorComponent(); 
      // }
      // else if (label.equals("Controller"))
      // { }
      // else if (label.equals("Input")) //dashed line
      // { thisLabel.setText("Creating an input flow");
      //   ucdArea.setDrawMode(UCDArea.DLINES);
      // }
      // else if (label.equals("Output")) //solid line
      // { thisLabel.setText("Creating an output flow");
      //   ucdArea.setDrawMode(UCDArea.SLINES);
      // }
      else if (label.equals("Invariant")) 
      { thisLabel.setText("Enter invariant: " + 
                          "assumption => conclusion");
        Invariant inv = createStaticInvariant("","","");
        if (inv != null)
        { ucdArea.addInvariant(inv); }
        resetEnablings();
        repaint();
        saved = false; 
      } 
      // else if (label.equals("Action Invariant"))
      // { } 
      // else if (label.equals("Temporal Invariant"))
      // { }
      // else if (label.equals("Save Java"))
      // { } 
      // else if (label.equals("Save Smv"))
      // { } 
      // else if (label.equals("Save B"))
      // { } 
      else if (label.equals("Set name"))
      { // prompt for new system name and erase all old data, give warning
        /* if (saved) { } 
        else 
        { JOptionPane.showInputDialog("Old data will be lost!"); } 
        ucdArea.clearAllData(); 
        repaint(); */  
        name =
          JOptionPane.showInputDialog("Enter name for the system:");
        controllerName = name + "Controller";
        ucdArea.setSystemName(name); 
        saved = true; 
      }
      else if (label.equals("Recent"))
      { ucdArea.loadFromFile("mm.txt"); 
        thisLabel.setText("Model loaded from output/mm.txt"); 
      }
      else if (label.equals("Save"))
      { ucdArea.saveDataToFile("mm.txt"); 
        saved = true; 
        thisLabel.setText("Model saved to output/mm.txt"); 
      } 
      else if (label.equals("Load CSV"))
      { ucdArea.loadFromCSV("mm.csv"); 
        thisLabel.setText("Model loaded from output/mm.csv"); 
      }
      else if (label.equals("Save as data"))
      { ucdArea.saveDataToFile();
        thisLabel.setText("Model saved"); 
        saved = true; 
      }
      else if (label.equals("Save design"))
      { ucdArea.saveModelToFile("output/model.txt");        
        thisLabel.setText("Design model saved to output/model.txt"); 
      }
      else if (label.equals("Save as EMF"))
      { ucdArea.saveEMFToFile(); } 
      else if (label.equals("Save as KM3"))
      { ucdArea.saveKM3ToFile(); } 
      else if (label.equals("Simple KM3"))
      { ucdArea.saveSimpleKM3ToFile(); } 
      else if (label.equals("Save as Ecore"))
      { ucdArea.saveEcoreToFile(); } 
      else if (label.equals("Save as XMI"))
      { System.out.println(ucdArea.generateXml()); } 
      else if (label.equals("Save as USE"))
      { ucdArea.saveUSEDataToFile("mm.use"); 
        thisLabel.setText("Model saved to output/mm.use"); 
      } 
      else if (label.equals("Save as PlantUML"))
      { ucdArea.savePlantUMLToFile("mm.plantuml"); 
        thisLabel.setText("Model saved to output/mm.plantuml"); 
      } 
      else if (label.equals("Save as CSV"))
      { ucdArea.saveCSV(); } 
      else if (label.equals("Map TL to CSTL"))
      { ucdArea.mapTL2CSTL(); } 
      else if (label.equals("Convert XSI (small) to Data"))
      { ucdArea.convertXsiToData(); } 
      else if (label.equals("Convert XSI (large) to Data"))
      { ucdArea.convertXsiToData2(); } 
      else if (label.equals("Load *.txt")) 
      { ucdArea.loadFromFile();
        saved = true; 
      }
      else if (label.equals("Load model")) 
      { ucdArea.loadModelFromFile();
        saved = true; 
      }
      else if (label.equals("Load Ecore")) 
      { ucdArea.loadEcoreFromFile();
        saved = true; 
      }
      else if (label.equals("Load *.km3")) 
      { ucdArea.loadKM3FromFile();
        saved = true; 
      }
      else if (label.equals("Load TL")) 
      { ucdArea.loadTL();
         // saved = true; 
      }
      else if (label.equals("Load CSTL")) 
      { ucdArea.loadCSTL();
         // saved = true; 
      }
      else if (label.equals("Load standard ATL")) 
      { ucdArea.loadATL();
         // saved = true; 
      }
      else if (label.equals("Load refining ATL"))
      { settupATLrefiningMode(); } 
      else if (label.equals("Load ETL")) 
      { ucdArea.loadETL();
         // saved = true; 
      }
      else if (label.equals("Load Flock")) 
      { ucdArea.loadFlock();
         // saved = true; 
      }
      else if (label.equals("Load QVT-R")) 
      { ucdArea.loadQVT();
         // saved = true; 
      }
      else if (label.equals("From Java AST")) 
      { ucdArea.loadGenericUseCase();
        saved = true; 
      }
      else if (label.equals("From C AST")) 
      { ucdArea.fromCAST();
        saved = true; 
      }
      else if (label.equals("From JavaScript AST")) 
      { ucdArea.loadFromJavaScript();
        saved = true; 
      }
      else if (label.equals("From VB AST")) 
      { ucdArea.loadFromVB();
        saved = true; 
      }
      else if (label.equals("From COBOL AST")) 
      { ucdArea.loadFromCobol();
        saved = true; 
      }
      else if (label.equals("From Python AST")) 
      { ucdArea.loadFromPython();
        saved = true; 
      }
      else if (label.equals("From Pascal AST")) 
      { ucdArea.loadFromPascal();
        saved = true; 
      }
      else if (label.equals("From SQL AST")) 
      { ucdArea.loadFromSQL();
        saved = true; 
      }
      else if (label.equals("Random model")) 
      { ucdArea.randomModel();
        saved = true; 
      }
      else if (label.equals("Random models (to Java)")) 
      { String val = 
          JOptionPane.showInputDialog("How many models to create?"); 
        if (val != null) 
        { int num = Integer.parseInt(val); 
          for (int i = 0; i < num; i++) 
          { ucdArea = new UCDArea(this); 
            ucdArea.randomModels2Java();
          }
        } 
      }
      else if (label.equals("Random models (to Python)")) 
      { String val = 
          JOptionPane.showInputDialog("How many models to create?"); 
        if (val != null) 
        { int num = Integer.parseInt(val); 
          for (int i = 0; i < num; i++) 
          { ucdArea = new UCDArea(this); 
            randomModels2Python();
          }
        } 
      }
      else if (label.equals("Print"))
      { printData(); } 
      else if (label.equals("B for entities"))
      { File file = new File("output/BCode.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateB(out);
          out.close();
          thisLabel.setText("B model saved to output/BCode.txt"); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating B"); }

        new TextDisplay("B code","output/BCode.txt");
      } 
      else if (label.equals("B for system"))
      { File file = new File("output/BCode.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateMergedB(out);
          thisLabel.setText("B model saved to output/BCode.txt"); 
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating B"); }

        new TextDisplay("B code","output/BCode.txt");
      } 
      else if (label.equals("Z3 for system"))
      { File file = new File("output/Z3Code.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateZ3(out);
          thisLabel.setText("Z3 model saved to output/Z3Code.txt"); 
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Z3"); }

        new TextDisplay("Z3 code","output/Z3Code.txt");
      } 
      else if (label.equals("Generate tests"))
      { ucdArea.testCases(); } 
      else if (label.equals("Verify Invariants"))
      { ucdArea.verifyInvariants(); } 
      else if (label.equals("Generate Java4"))
      { String sysName = ucdArea.getSystemName(); 
        String dirName = "output";         

        if (sysName != null && sysName.length() > 0) 
        { dirName = sysName; 
          File dir = new File(sysName); 
          if (dir.exists()) { } 
          else 
          { dir.mkdir(); }  
        } 

        File file = new File(dirName + "/Controller.java");
        File file2 = new File(dirName + "/SystemTypes.java");
        File file3 = new File(dirName + "/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          PrintWriter out3 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file3)));
          ucdArea.generateJava(out,out2,out3);
          out.close();
          out2.close(); 
          thisLabel.setText("Java code saved to " + dirName + "/GUI.java"); 
          out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        TextDisplay td = new TextDisplay("Java code", dirName + "/Controller.java");
        td.setFont(new java.awt.Font("Serif",java.awt.Font.BOLD,18)); 
      } 
      else if (label.equals("Generate Java6"))
      { String sysName = ucdArea.getSystemName(); 
        String dirName = "output";         

        if (sysName != null && sysName.length() > 0) 
        { dirName = sysName; 
          File dir = new File(sysName); 
          if (dir.exists()) { } 
          else 
          { dir.mkdir(); }  
        } 

        File file = new File(dirName + "/Controller.java");
        File file2 = new File(dirName + "/SystemTypes.java");
        // File file3 = new File(dirName + "/ControllerInterface.java");

        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          ucdArea.generateJava6(out,out2);
          out.close();
          thisLabel.setText("Java code saved to " + dirName + "/GUI.java"); 
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        new TextDisplay("Java code", dirName + "/Controller.java");
      } 
      else if (label.equals("Generate Java7"))
      { String sysName = ucdArea.getSystemName(); 
        String dirName = "output";         

        if (sysName != null && sysName.length() > 0) 
        { dirName = sysName; 
          File dir = new File(sysName); 
          if (dir.exists()) { } 
          else 
          { dir.mkdir(); }  
        } 

        File file = new File(dirName + "/Controller.java");
        File file2 = new File(dirName + "/SystemTypes.java");

        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          ucdArea.generateJava7(out,out2);
          out.close();
          thisLabel.setText("Java code saved to " + dirName + "/GUI.java"); 
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        new TextDisplay("Java 7 code", dirName + "/Controller.java");
      } 
      else if (label.equals("Generate Java8"))
      { String sysName = ucdArea.getSystemName(); 
        String dirName = "output";         

        if (sysName != null && sysName.length() > 0) 
        { dirName = sysName; 
          File dir = new File(sysName); 
          if (dir.exists()) { } 
          else 
          { dir.mkdir(); }  
        } 

        File file = new File(dirName + "/Application.java");

        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateJava8(out);
          out.close();
          thisLabel.setText("Java code saved to " + dirName + "/GUI.java"); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        new TextDisplay("Java 8 code", dirName + "/Application.java");
      } 
      else if (label.equals("Generate C#"))
      { File file = new File("output/Program.cs");  // Controller.cs
        File file2 = new File("output/SystemTypes.cs");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          ucdArea.generateCSharp(out,out2);
          thisLabel.setText("C# code saved to output/Controller.cs"); 
          out.close();
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("Error generating C#"); }

        new TextDisplay("C# code","output/Program.cs");
      } 
      else if (label.equals("Generate C++"))
      { File file = new File("output/controller.h");
        File file2 = new File("output/Controller.cpp");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          ucdArea.generateCPP(out,out2);
          thisLabel.setText("C++ code saved to output/Controller.cpp"); 
          out.close();
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating C++"); }

        new TextDisplay("C++ code","output/controller.h");
      } 
      else if (label.equals("Generate C header"))
      { ucdArea.saveModelToFile("model.txt"); 
        ucdArea.saveInterfaceDescription("app.itf"); 

        // try { wait(500); } catch (Exception _w) { } 
          
        RunApp rapp1 = new RunApp("uml2Ca"); 
          
        try
        { rapp1.setFile("app.h"); 

          Thread appthread = new Thread(rapp1); 
          appthread.start(); 
        } 
        catch (Exception ee1) 
        { System.err.println("!! Unable to run uml2Ca"); }     
      } 
      else if (label.equals("Generate C code"))
      { RunApp rapp1 = new RunApp("uml2Cb"); 

        try
        { rapp1.setFile("app.c"); 
          Thread appthread = new Thread(rapp1); 
          appthread.start(); 
        } 
        catch (Exception ee2) 
        { System.err.println("!! Unable to run uml2Cb"); } 

        File file = new File("gui.c");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.produceCUI(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating C UI"); }
      }
      else if (label.equals("Use CSTL specification"))
      { ucdArea.applyCSTLSpecification(); } 
      else if (label.equals("Use CGTL specification"))
      { ucdArea.applyCGTL(); } 
      else if (label.equals("Apply CSTL/CGTL to AST"))
      { ucdArea.applyCSTLtoAST(); } 
      else if (label.equals("Generate Python 3.8"))
      { ucdArea.saveModelToFile("output/model.txt"); 


        RunApp rapp1 = new RunApp("uml2py"); 

        try
        { rapp1.setFile("app.py"); 
          Thread appthread = new Thread(rapp1); 
          appthread.start(); 
        } 
        catch (Exception ee2) 
        { System.err.println("!! Unable to run uml2py.jar"); } 

        File pythonTests = new File("tester.py"); 
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(pythonTests)));
          Vector ucs = ucdArea.getGeneralUseCases(); 
          Vector typs = ucdArea.getTypes(); 
          Vector ents = ucdArea.getEntities(); 
          String testcode = 
             GUIBuilder.buildTestsGUIPython(
                                ucs,"",typs,ents);    
          out.println(testcode);
          out.close();

          ucdArea.generateMutationTesterPython(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Python tests"); }
        
      }
      else if (label.equals("Generate Python 3.10"))
      { ucdArea.saveModelToFile("output/model.txt"); 

        // java.util.Date d1 = new java.util.Date(); 
        // long t1 = d1.getDate(); 

        RunApp rapp1 = new RunApp("uml2py3"); 
        Thread appthread = null;
 
        try
        { rapp1.setFile("app.py"); 
          appthread = new Thread(rapp1); 
          appthread.start(); 
        } 
        catch (Exception ee2) 
        { System.err.println("!! Unable to run uml2py3.jar"); } 

        File pythonTests = new File("tester.py"); 
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(pythonTests)));
          Vector ucs = ucdArea.getGeneralUseCases(); 
          Vector typs = ucdArea.getTypes(); 
          Vector ents = ucdArea.getEntities(); 
          String testcode = 
             GUIBuilder.buildTestsGUIPython(
                                ucs,"",typs,ents);    
          out.println(testcode);
          out.close();

          ucdArea.generateMutationTesterPython(); 
          if (appthread != null) 
          { appthread.join(); } 
        }
        catch (Exception ex)
        { System.out.println("!! Error generating Python tests"); }

        // java.util.Date d2 = new java.util.Date(); 
        // long t2 = d2.getDate(); 
        // System.out.println(">>> Code generation took " + (t2-t1) + "ms"); 
      }
      else if (label.equals("Generate Go"))
      { ucdArea.generateGo(); } 
      else if (label.equals("Generate Mamba"))
      { ucdArea.generateMamba(); } 
      else if (label.equals("Type-check"))
      { ucdArea.typeCheck(); } 
      else if (label.equals("Type inference"))
      { ucdArea.typeInference(); } 
      else if (label.equals("Generate Design"))
      { ucdArea.generateDesign(); } 
      else if (label.equals("Generate SMV"))
      { File file = new File("output/tmpsmv.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateSmv(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating SMV"); }

        new TextDisplay("SMV code","output/tmpsmv.txt");
      } 
      else if (label.equals("Servlet style"))
      { File file = new File("output/servlets.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateWebSystem(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Web System"); }

        new TextDisplay("Web System code","output/servlets.txt");
      } 
      else if (label.equals("JSP style"))
      { File file = new File("output/jsps.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateJSPWebSystem(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Web System"); }

        new TextDisplay("Web System code","output/jsps.txt");
      } 
      else if (label.equals("Android system"))
      { File file = new File("output/app/AndroidManifest.xml");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateAndroidLayouts(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Android System"); }

        new TextDisplay("Android System code","output/app/AndroidManifest.xml");
      } 
      else if (label.equals("iOS system"))
      { ucdArea.generateIOSApp(); } 
      else if (label.equals("SwiftUI system"))
      { ucdArea.generateSwiftUIApp(); } 
      else if (label.equals("J2EE style"))
      { File file = new File("output/j2ees.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.generateJ2EEWebSystem(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Web System"); }

        new TextDisplay("Web System code","output/j2ees.txt");
      } 
      else if (label.equals("REST style"))
      { ucdArea.generateRESTWebService(); } 
      else if (label.equals("SOAP style"))
      { ucdArea.generateSOAPWebService(); } 
      else if (label.equals("Run"))
      { Runtime proc = Runtime.getRuntime(); 
        String dir = ucdArea.getSystemName(); 
        if (dir == null || dir.trim().length() == 0)
        { System.err.println("!! Run can only be used if a system name is defined"); 
          return; 
        } 

        try 
        { Process p = proc.exec("javac " + dir + "/*.java"); 
          InputStream stdin = p.getInputStream(); 
          // InputStreamReader insr = new InputStreamReader(stdin); 
          // BufferedReader inbr = new BufferedReader(insr); 
          // String outline = inbr.readLine(); 

          StreamGobble igb = new StreamGobble(stdin); 
          InputStream stderr = p.getErrorStream(); 
          StreamGobble egb = new StreamGobble(stderr); 

          // InputStreamReader errsr = new InputStreamReader(stderr); 
          // BufferedReader errbr = new BufferedReader(errsr); 
          // String errline = errbr.readLine(); 
          
          System.out.println("Compiling Java code ....");
          // while (outline != null || errline != null) 
          // { if (outline != null) 
          // while (outline != null) 
          // { System.out.println(outline); 
          //   outline = inbr.readLine();
          // }
 
          // while (errline != null) 
          // { System.out.println(errline); 
          //   errline = errbr.readLine();
          // } 
          // }
          egb.start(); igb.start();   
          int exitjavac = p.waitFor(); 
          if (exitjavac == 0) 
          { System.out.println(">>> Compilation successfull"); } 
          else 
          { System.out.println("! Compilation problem, exit code: " + exitjavac); 
            return; 
          }  
        } 
        catch (Exception ee) 
        { System.err.println("!! Unable to compile generated code: check the specification for errors"); } 

        File manifest = new File("Manifest.txt");
        try
        { PrintWriter mout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(manifest)));
          mout.println("Main-Class: " + dir + ".GUI");
          mout.close();
        }
        catch (IOException ex)
        { System.out.println("! Error generating Manifest.txt");
          return; 
        }

        try { Process p1 = proc.exec("jar cvfm " + dir + "/" + dir + ".jar Manifest.txt " + dir + "/*.class"); 
          InputStream sin = p1.getInputStream(); 
          StreamGobble isg = new StreamGobble(sin); 
          // InputStreamReader inr = new InputStreamReader(sin); 
          // BufferedReader ibr = new BufferedReader(inr); 
          // String oline = ibr.readLine(); 
          System.out.println("Building jar ....");
          /* while (oline != null) 
          { System.out.println(oline); 
            oline = ibr.readLine(); 
          }  */
          isg.start(); 
          int exitjar = p1.waitFor(); 
          System.out.println(">>> Jar exit code: " + exitjar); 
          Process p2 = proc.exec("java -jar " + dir + "/" + dir + ".jar"); 

          InputStream sin2 = p2.getInputStream(); 
          InputStreamReader inr2 = new InputStreamReader(sin2); 
          BufferedReader ibr2 = new BufferedReader(inr2); 
          String oline2 = ibr2.readLine(); 
          System.out.println("java ....");
          while (oline2 != null) 
          { System.out.println(oline2); 
            oline2 = ibr2.readLine(); 
          }  
          int exitjar2 = p2.waitFor(); 
          System.out.println(">>> Java exit code: " + exitjar2);

          Thread appthread = new Thread(new RunApp(dir)); 
          appthread.start(); 
        } 
        catch (Exception ee1) 
        { System.err.println("! Unable to run application"); }     
      }
      else if (label.equals("Invariants"))
      { displayInvariants(ucdArea.displayInvariants()); 
        ucdArea.listInvariants();
      } 
      else if (label.equals("DCFD"))
      { ucdArea.setView(UCDArea.DCFD); } 
      else if (label.equals("Class Diagram"))
      { ucdArea.setView(UCDArea.CLASSDIAGRAM); } 
      // else if (label.equals("Subsystems"))
      // { }
      // else if (label.equals("Sensors"))
      // {  } 
      // else if (label.equals("Actuators"))
      // {  }
      else if (label.equals("Types"))
      { ucdArea.listTypes(); }
      else if (label.equals("Operations"))
      { File file = new File("output/tmp");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.listOperations(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating operations"); }

        new TextDisplay("Operations","output/tmp");
      }  
      else if (label.equals("Activities"))
      { ucdArea.displayActivities(); }  
      else if (label.equals("Use cases"))
      { File file = new File("output/tmp");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.listUseCases(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating use cases"); }

        new TextDisplay("Use cases","output/tmp");
      }  
      else if (label.equals("Quality measures"))
      { File file = new File("output/tmp.txt");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          ucdArea.displayMeasures(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating measures"); }

        new TextDisplay("Measures","output/tmp.txt");
      }  
      else if (label.equals("Energy analysis"))
      { ucdArea.energyAnalysis(); }  
      else if (label.equals("Clean architecture properties"))
      { ucdArea.cleanArchitectureCheck(); }
      else if (label.equals("Data dependencies"))
      { ucdArea.displayDataDependencies(); }
      else if (label.equals("Compare models"))
      { String nme1 =
          JOptionPane.showInputDialog("Enter first model name (.km3 extension omitted): ");
        String nme2 =
          JOptionPane.showInputDialog("Enter second model name (.km3 extension omitted): ");
        if (nme1 != null && nme2 != null) 
        { ucdArea.compareModels(nme1, nme2); } 
      }  
      else if (label.equals("Controller B Code"))
      {  } 
      else if (label.equals("Controller Java Code"))
      {  } 
      else if (label.equals("Smv Modules"))
      { }
      else if (label.equals("MT requirements to TL"))
      { ucdArea.requirements2TL0(); } 
      else if (label.equals("All maps (DSS)"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.flattenModel("all maps",t); 
      } 
      else if (label.equals("Iterative optimisation"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.iterativeOptimisation("all maps",t);
      } 
      else if (label.equals("Genetic algorithm"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.flattenModelGA(t); 
      } 
      else if (label.equals("injective maps (DSS)"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.flattenModel("1-1 maps",t);
      } 
      else if (label.equals("inheritance-preserving (DSS)"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.flattenModel("inheritance-preserving",t);
      }
      else if (label.equals("Graph structure (GSS)"))
      { ucdArea.computeLeadership(); }  
      else if (label.equals("Graph edit distance (GES)"))
      { ucdArea.graphEditDistance(); }  
      else if (label.equals("Name similarity (NSS)"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.nameSimilarity(t);
      }  
      else if (label.equals("Name semantics (NMS)"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.nameSemanticSimilarity(t); 
      }  
      else if (label.equals("Ontological similarity (SCS)"))
      { Vector t = ucdArea.loadThesaurus(); 
        ucdArea.ontologicalSimilarity(t); 
      }  
      else if (label.equals("Composite score"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.refinementScore(t); 
      }  
      else if (label.equals("From loaded TL"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.synthesiseFromTL(t); 
      } 
      else if (label.equals("Map TL to bx"))
      { Vector t = ucdArea.loadThesaurus();
        ucdArea.mapTL2UMLRSDS(t); 
      } 
      else if (label.equals("Check TL wrt model"))
      { 
        ucdArea.checkTLmodel(); 
      } 
      else if (label.equals("CGBE"))
      { ucdArea.cgbeOCL2Program("configuration.txt"); } 
      else if (label.equals("LTBE from text"))
      { ucdArea.ltbeFromText(); }
      else if (label.equals("LTBE from ASTs"))
      { ucdArea.cgbe(null); } 
      else if (label.equals("Validate CGBE"))
      { ucdArea.validateCGBE(); } 
      else if (label.equals("Formalise data requirements"))
      { 
        ucdArea.formaliseDataRequirements(); 
      } 
      else if (label.equals("Formalise behaviour requirements"))
      { 
        ucdArea.formaliseBehaviourRequirements(); 
      } 
      else if (label.equals("Formalise operation requirements"))
      { 
        ucdArea.formaliseOperationRequirements(); 
      } 
      else if (label.equals("Use Case Dependencies"))
      { ucdArea.showUCDependencies(); } 
      else if (label.equals("Form Contrapositives"))
      { ucdArea.formShortestPaths();
        ucdArea.addContrapositives();
      }
      else if (label.equals("Transitive Closures"))
      { ucdArea.addTransitiveComps(); } 
      else if (label.equals("Delete Invariant"))
      { deleteInvariant(); }
      else if (label.equals("Completeness Analysis"))
      { boolean comp = ucdArea.checkCompleteness();
        if (comp)
        { thisLabel.setText("Invariants are syntactically complete"); }
        else 
        { thisLabel.setText("! Invariants are not complete"); } 
      }
      else if (label.equals("Consistency Analysis"))
      { boolean res = ucdArea.consistencyCheck();
        res = res && ucdArea.diagramCheck(); 
        if (res)
        { thisLabel.setText("Model passed consistency checks"); }
        else 
        { thisLabel.setText("!! Model failed consistency checks"); } 
      }
      // else if (label.equals("Dependency Analysis")) 
      // { } 
      else if (label.equals("Static to Action Invariants"))
      { removeRedundancies();
        convertToOperationalForm(); 
        thisLabel.setText("Invariants converted to action forms"); 
      } 
      // else if (label.equals("Fault Detection"))
      // { } 
      // else if (label.equals("Horizontal"))
      // { } 
      // else if (label.equals("Hierarchical")) 
      // { } 
      // else if (label.equals("Phase/Mode"))
      // { } 
      // else if (label.equals("Scheduling"))
      // { } 
      else if (label.equals("Move"))
      { System.out.println(">> Select a class or association");
        thisLabel.setText("Select a class or association");
        ucdArea.setDrawMode(UCDArea.EDIT);
        ucdArea.setEditMode(UCDArea.MOVING);
        saved = false; 
      }
      else if (label.equals("Delete"))
      { System.out.println(">> Select a class or association");
        thisLabel.setText("Select a class or association");
        ucdArea.setDrawMode(UCDArea.EDIT);
        ucdArea.setEditMode(UCDArea.DELETING);
        saved = false; 
      }
      else if (label.equals("Modify"))
      { System.out.println(">> Select a class or association");
        thisLabel.setText("Select a class or association");
        ucdArea.setDrawMode(UCDArea.EDIT);
        ucdArea.setEditMode(UCDArea.MODIFY);
        saved = false; 
      }
      else if (label.equals("Move all right"))
      { ucdArea.moveAllRight();
        saved = false;
        repaint();  
      }
      else if (label.equals("Move all down"))
      { ucdArea.moveAllDown();
        saved = false;
        repaint();  
      }
      else if (label.equals("Move all left"))
      { ucdArea.moveAllLeft();
        saved = false;
        repaint();  
      }
      else if (label.equals("Move all up"))
      { ucdArea.moveAllUp();
        saved = false;
        repaint();  
      }
      else if (label.equals("Edit Use Case"))
      { System.out.println(">> Select a use case");
        thisLabel.setText("Select a use case");
        // String nme =
        //  JOptionPane.showInputDialog("Enter use case name:");
        Vector res = ucdArea.selectUseCase(); 
        if (res.size() > 0)
        { ucdArea.interactiveEditUseCase(res.get(0) + "");  
          saved = false; 
        } 
      }
      else if (label.equals("Edit KM3"))
      { ucdArea.interactiveEditKM3();  
        saved = false;  
      }
      else if (label.equals("Delete Use Case"))
      { System.out.println(">> Select a use case");
        thisLabel.setText("Select a use case");
        Vector res = ucdArea.selectUseCase(); 
        if (res.size() > 0)
        { ucdArea.deleteUseCase(res.get(0) + "");  
          saved = false; 
        } 
      }      
      // else if (label.equals("Abstraction mapping"))
      // { } 
      // else if (label.equals("Generate SMV"))
      // { } 
      else if (label.equals("Help"))
      { if (helppane != null) 
        { helppane.setVisible(true); 
          return; 
        }
        
        helppane = new JEditorPane();  
        helppane.setEditable(false); 
        helppane.setSize(300,400); 
        helppane.setText("Troubleshooting: \n\n" + 
                  "1. Unable to save/view data? \n" + 
                  "   An 'output' subdirectory must exist\n" + 
                  "   in the directory in which the tool is executed.\n" + 
                  "   All models, code, etc are stored there.\n\n" + 

                  "2a. Unable to parse conditions? \n" + 
                  "   Check that formulae  s->forAll(x | P)  etc\n" + 
                  "   are written *without* spaces between -> and \n" + 
                  "   forAll. Sometimes more brackets are needed, eg:\n" + 
                  "   ( 1 - ( ( 1 - sectors[k].q )->pow(i) ) )->pow(m - i)\n\n" + 
                  "2b. Unable to parse statements? \n" + 
                  "   Activities should not be bracketed:\n" + 
                  "   activity: (x := x+1; y := x*y);\n" + 
                  "   should be:\n" + 
                  "   activity: x := x+1; y := x*y;\n\n" + 
                  "2c. Unable to parse expressions? \n" + 
                  "   Object references should be bracketed:\n" + 
                  "   arr[i].f\n" + 
                  "   should be:\n" + 
                  "   (arr[i]).f" + 
                  "\n\n" + 

                  "3. Code runs forever? \n" + 
                  "   Choose option 'n' when asked to optimise type\n" + 
                  "   2 or 3 constraints.\n\n" + 
                  "4. Java compiler errors of undeclared variables?\n" + 
                  "   Check that all identifiers are valid. In a\n" + 
                  "   constraint on class E, only features of E can be\n" +
                  "   used unqualified, other features need an \n" + 
                  "   object reference obj.f or C.f.\n\n" +  
                  "5. Java compiler errors of duplicate variables?\n" + 
                  "   Use distinct let variables in postcondition \n" + 
                  "   constraints in a use case uc - all these variables\n" +
                  "   go into the same Controller operation for uc.\n\n" +  
                  "For more help, please read the manual:\n" + 
                  "https://www.agilemde.co.uk/umlrsds.pdf\n\n");
        int w = getWidth(); 
        int h = getHeight(); 
 
        getContentPane().add(new JScrollPane(helppane), java.awt.BorderLayout.EAST); 
        setSize(w + 300, h); 
        helppane.setVisible(true); 
        helppane.repaint(); 
        repaint(); 
        java.awt.LayoutManager ll = getLayout(); 
        if (ll != null)
        { ll.layoutContainer(getContentPane()); }  
        repaint(); 


        // new TextDisplay("Guidelines","umlrsds.pdf");
        Runtime proc = Runtime.getRuntime(); 
        try { Process p = proc.exec("C:\\Program Files\\Mozilla Firefox\\firefox.exe http://www.agilemde.co.uk/umlrsds20.pdf"); } 
        catch (Exception ee) 
        { System.err.println("!! Unable to open the UML-RSDS manual: requires Firefox"); } 
      }
      else if (label.equals("Quality check"))
      { ucdArea.qualityCheck(); }
      else if (label.equals("Extract Interface"))
      { ucdArea.extractInterface(); 
        repaint(); 
      }
      else if (label.equals("Extract Operation"))
      { ucdArea.extractOperation(); 
        repaint(); 
      }
      else if (label.equals("Split class"))
      { ucdArea.extractComponent(); 
        repaint(); 
      }
      else if (label.equals("Introduce Superclass"))
      { introduceSuperclass(); 
        repaint(); 
      }
      else if (label.equals("Introduce Foreign Key"))
      { introduceForeignKeys();
        repaint();
      }
      else if (label.equals("Add all Primary Keys"))
      { ucdArea.introducePrimaryKeyToAllClasses(); 
        repaint(); 
      }
      else if (label.equals("Introduce Primary Key"))
      { introducePrimaryKeys(); 
        repaint(); 
      }
      else if (label.equals("Remove *--* Associations"))
      { removeManyManyAssociations(); 
        repaint(); 
      }
      else if (label.equals("Remove Association Classes"))
      { removeAssociationClasses(); 
        repaint(); 
      }
      else if (label.equals("Replace Inheritance by Association"))
      { removeInheritance(); 
        repaint(); 
      } 
      else if (label.equals("Remove Redundant Inheritance"))
      { removeRedundantInheritances(); 
        repaint(); 
      }
      else if (label.equals("Remove Multiple Inheritance"))
      { removeMultipleInheritance(); 
        repaint(); 
      }
      else if (label.equals("Aggregate Subclasses"))
      { aggregateSubclasses(); 
        repaint(); 
      } 
      else if (label.equals("Express Statemachine on Class Diagram"))
      { ucdArea.convertStatecharts(); } 
      else if (label.equals("Matching by backtracking"))
      { introduceBacktracking(); } 
      else if (label.equals("Pushdown abstract features"))
      { pushdownFeatures(); } 
      else if (label.equals("Move operation"))
      { ucdArea.moveOperation(); 
        repaint(); 
      } 
      else if (label.equals("Move operation to parameter class"))
      { ucdArea.moveOperationToParameterClass(); 
        repaint(); 
      } 
      else if (label.equals("Move attribute"))
      { ucdArea.moveAttribute(); 
        repaint(); 
      } 
      else if (label.equals("Replace call by definition"))
      { this.replaceCallByDefinition(); } 
      else if (label.equals("Replace recursion by loops"))
      { this.transformOperationActivity(); } 
      else if (label.equals("Split operation"))
      { this.splitOperationActivity(); } 
      else if (label.equals("Hoist local declarations"))
      { this.hoistOperationLocalDecs(); } 
      else if (label.equals("Reduce code nesting"))
      { this.reduceCodeNesting(); } 
      else if (label.equals("Value Object"))
      { ucdArea.makeValueObjects(); }
      else if (label.equals("Singleton"))
      { makeSingletons(); }
      else if (label.equals("Observer"))
      { ucdArea.observerPattern(); }
      else if (label.equals("Iterator"))
      { ucdArea.iteratorPattern(); }
      else if (label.equals("Blackboard"))
      { ucdArea.blackboardPattern(); }
      else if (label.equals("MVC"))
      { ucdArea.mvcPattern(); }
      else if (label.equals("Facade"))
      { checkForFacades(); }  
      else if (label.equals("Phased Construction"))
      { applyPhasedConstruction(); }  
      else if (label.equals("Implicit Copy"))
      { implicitCopy(); }  
      else if (label.equals("Auxiliary Metamodel"))
      { ucdArea.auxiliaryMetamodel(); }  
      else if (label.equals("Import library"))
      { // String tname = 
        //   JOptionPane.showInputDialog("Enter import (in Java syntax):");
        ucdArea.addImport(""); 
      } 
      else if (label.equals("Add -> operator"))
      { OperatorEditDialog oed = new OperatorEditDialog(this); 
        oed.pack();
        oed.setVisible(true);
        String opname = oed.getName(); 
        String optype = oed.getType();  
        String opjava = oed.getPre(); 
        String opcsharp = oed.getPost(); 
        // Type t = ucdArea.lookupType(optype);
        // if (t != null)
        // { 
        Expression.addOperator(opname,
                               ucdArea.getTypes(),optype); 
        // }
        if (opjava != null && opjava.trim().length() > 0)
        { Expression.addOperatorJava(opname, opjava); }   
        if (opcsharp != null && opcsharp.trim().length() > 0)
        { Expression.addOperatorCSharp(opname, opcsharp); }   
      } 
      else if (label.equals("Export as Java4"))
      { // String tname = 
        //  JOptionPane.showInputDialog("Enter class name:");
        String sysName = ucdArea.getSystemName(); 
        String dirName = "output";         

        if (sysName != null && sysName.length() > 0) 
        { dirName = sysName; 
          File dir = new File(sysName); 
          if (dir.exists()) { } 
          else 
          { dir.mkdir(); }  
        } 

        File file = new File(dirName + "/Controller.java");
        File file2 = new File(dirName + "/SystemTypes.java");
        File file3 = new File(dirName + "/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          PrintWriter out3 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file3)));
          ucdArea.exportClasses(out,out2,out3);
          out.close();
          out2.close(); 
          thisLabel.setText("Java code saved to " + dirName + "/*.java"); 
          out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("Error generating Java"); }
      } 
      else if (label.equals("Export as Java6"))
      { // String tname = 
        //  JOptionPane.showInputDialog("Enter class name:");
        File file = new File("output/Controller.java");
        File file2 = new File("output/SystemTypes.java");
        // File file3 = new File("output/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          // PrintWriter out3 = new PrintWriter(
          //                     new BufferedWriter(
          //                       new FileWriter(file3)));
          ucdArea.exportClassesJava6(out,out2,null);
          out.close();
          out2.close(); 
          thisLabel.setText("Java code saved to output/*.java"); 
          // out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("Error generating Java"); }
      } 
      else if (label.equals("Export as Java7"))
      { // String tname = 
        //  JOptionPane.showInputDialog("Enter class name:");
        File file = new File("output/Controller.java");
        File file2 = new File("output/SystemTypes.java");
        // File file3 = new File("output/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          // PrintWriter out3 = new PrintWriter(
          //                     new BufferedWriter(
          //                       new FileWriter(file3)));
          ucdArea.exportClassesJava7(out,out2,null);
          out.close();
          out2.close(); 
          thisLabel.setText("Java code saved to output/*.java"); 
          // out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("Error generating Java"); }
      } 
      else if (label.equals("Export as C#"))
      { File file = new File("output/Controller.cs");
        File file2 = new File("output/SystemTypes.cs");
        // File file3 = new File("output/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          // PrintWriter out3 = new PrintWriter(
          //                     new BufferedWriter(
          //                       new FileWriter(file3)));
          ucdArea.exportClassesCSharp(out,out2,null);
          out.close();
          out2.close(); 
          thisLabel.setText("C# code saved to output/*.cs"); 
          // out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("Error generating C#"); }
      } 
      else if (plugins.contains(label))
      { ucdArea.saveModelToFile("model.txt"); 

        try { wait(500); } catch (Exception _w) { } 
          
        RunApp rapp1 = new RunApp(label); 
          
        try
        { rapp1.setFile("out.txt"); 

          Thread appthread = new Thread(rapp1); 
          appthread.start(); 
        } 
        catch (Exception ee1) 
        { System.err.println("Unable to run application: " + label); }     
      } 

    } 
  }

  private void displayInvariants(Vector invs)
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(invs); 
    thisLabel.setText("Select invariant to edit"); 
    System.out.println("Select invariant to edit");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        if (vals[i] instanceof Constraint)
        { Constraint con = (Constraint) vals[i]; 
          System.out.println(con); 
          Expression ante = con.antecedent(); 
          Expression succ = con.succedent(); 
          Invariant cc = createStaticInvariant(con.getOwner() + "", "" + ante, "" + succ); 
          System.out.println(cc); 
          if (cc != null) 
          { ucdArea.removeInvariant(con); 
            ucdArea.addInvariant(cc); 
          } 
        } 
        else 
        { System.out.println(vals[i] + " is not a constraint"); }
      } 
    } 
  }


  private void buildSensorComponent() 
  { repaint();
    String nme =
      JOptionPane.showInputDialog("Enter class name:");
    ucdArea.setComponentName(nme);
    System.out.println("Click on location to create " + nme);
    thisLabel.setText("Click on location to create " + nme); 
    ucdArea.setDrawMode(UCDArea.POINTS);
    ucdArea.setComponentMode(UCDArea.SENSOR);
  } 

  private void buildAssociationClass() 
  { repaint();
    String nme =
      JOptionPane.showInputDialog("Enter class name:");
    ucdArea.setComponentName(nme);
    System.out.println("Click and drag to create association " + nme);
    thisLabel.setText("Click and drag to create association " + nme);
    ucdArea.setDrawMode(UCDArea.ACLINES);
    ucdArea.setComponentMode(UCDArea.SENSOR);
  } 

  private void buildActuatorComponent() 
  { repaint(); 
    String nme = 
      JOptionPane.showInputDialog("Enter class name:");
    System.out.println("Click on location to create " + nme);
    thisLabel.setText("Click on location to create " + nme); 
    ucdArea.setComponentName(nme);
    ucdArea.setDrawMode(UCDArea.POINTS);
    ucdArea.setComponentMode(UCDArea.ACTUATOR);
  }

  private void printData()
  { PrinterJob pj = PrinterJob.getPrinterJob();
    pj.setPrintable(ucdArea);
    if (pj.printDialog())
    { try { pj.print(); }
      catch (Exception ex) { ex.printStackTrace(); }
    }
  }

  public Invariant createStaticInvariant(String ent, String assump, String conc)
  { if (sinvDialog == null)
    { sinvDialog = new SInvEditDialog(this);
      sinvDialog.pack();
      sinvDialog.setLocationRelativeTo(this);
    }
    sinvDialog.setOldFields(ent,assump,conc,true,false,false);
    sinvDialog.setVisible(true);

    Vector ents = ucdArea.getEntities(); 
    String sAssump = sinvDialog.getAssumption();
    String sConc = sinvDialog.getConclusion();
    String newent = sinvDialog.getEntity(); 
    Entity owner = (Entity) ModelElement.lookupByName(newent, ents); 

    if (sAssump != null && sConc != null)
    { System.out.println("New static invariant");
      comp = new Compiler2(); 
      comp.nospacelexicalanalysis(sAssump);
      Vector antesymbs = new Vector(); 
      antesymbs.add("|"); antesymbs.add("="); antesymbs.add(":"); 
      Vector messages = new Vector(); 
      comp.checkSyntax(owner,ents,antesymbs,messages); 
      if (messages.size() > 0)
      { System.err.println(messages); } 

      Expression eAssump = comp.parse();
      if (eAssump == null)
      { eAssump = new BasicExpression("true"); }
      comp = new Compiler2(); 
      comp.nospacelexicalanalysis(sConc);
      Vector succsymbs = new Vector(); 
      succsymbs.add("|");  
      comp.checkSyntax(owner,ents,succsymbs,messages); 
      if (messages.size() > 0)
      { System.err.println(messages); } 
      Expression eConc = comp.parse();
      if (eConc == null)
      { eConc = new BasicExpression("true"); }

      boolean isSys = sinvDialog.isSystem();
      boolean isCrit = sinvDialog.isCritical();
      boolean isBehav = sinvDialog.isBehaviour(); 
      boolean isOrd = sinvDialog.isOrdered(); 

      Invariant i2 = new SafetyInvariant(eAssump,eConc);
      // i2.createActionForm(ucdArea.getAllComponents());
      i2.setSystem(isSys);
      i2.setCritical(isCrit);
      i2.setBehavioural(isBehav); 
      i2.setOrdered(isOrd); 
      i2.setOwnerText(sinvDialog.getEntity()); 
      if (isOrd)
      { String ordBy = JOptionPane.showInputDialog("Enter ordering expression:");
        comp = new Compiler2(); 
        comp.nospacelexicalanalysis(ordBy); 
        Expression ordByExp = comp.parse(); 
        i2.setOrderedBy(ordByExp); 
      } 
      // invariants.add(i2); 
      thisLabel.setText("Static invariant added");
      return i2;
    }
    else
    { thisLabel.setText("Invalid syntax -- not added"); }
    return null;
  }
  
  public void resetEnablings() 
  { // consischeck.setEnabled(false); 
    // completenessCheck.setEnabled(false); 
    // depends.setEnabled(false);
    // safety2op.setEnabled(false);
    // testMenu.setEnabled(false);
    // javaMenu.setEnabled(false);
    // smvMI.setEnabled(false);  
    // horizMI.setEnabled(false); 
    // phaseMI.setEnabled(false);
    // faultdetMI.setEnabled(false);  
    // verifyMI.setEnabled(false);
    // schedulMI.setEnabled(false); 
    // schedular = null; 
    // schedulers.clear(); 
    // smvModules = null; 
  }  
// also set schedular = null, etc? Undo all 
// decompositions? 

  private void introducePrimaryKeys()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entities to add primary keys to"); 
    System.out.println("Select entities to add primary keys to");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        if (vals[i] instanceof Entity)
        { Entity ent = (Entity) vals[i]; 
          ent.createPrimaryKey(); 
        } 
        else 
        { System.out.println(vals[i] + " is not a class"); }
      } 
    } 
  }

  private void introduceSuperclass()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entities to abstract to superclass"); 
    System.out.println("Select entities to abstract to superclass");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { Entity[] ents = new Entity[vals.length]; 
      for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        if (vals[i] instanceof Entity)
        { Entity ent = (Entity) vals[i]; 
          System.out.println(ent); 
          ents[i] = ent; 
        } 
        else 
        { System.out.println(vals[i] + " is not a class");
          return;
        }
      } 
      Entity.introduceSuperclass(ents,ucdArea); 
    } 
  }

  private void createStatechart()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to create statemachine for"); 
    System.out.println("Select entity to create statemachine for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.createStatechart(ent); 
    } 
  }

  private void createOperationStatechart()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getOperations()); 
    thisLabel.setText("Select operation to create statemachine for"); 
    System.out.println("Select operation to create statemachine for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof BehaviouralFeature)
    { BehaviouralFeature op = (BehaviouralFeature) vals[0];
      ucdArea.createStatechart(op); 
    } 
  }

  private void deleteInvariant()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getConstraints()); 
    thisLabel.setText("Select constraint to delete"); 
    System.out.println("Select constraint to delete");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Constraint)
    { Constraint con = (Constraint) vals[0];
      ucdArea.removeConstraint(con); 
    } 
  }

  private void introduceForeignKeys()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    Vector allasts = ucdArea.getAssociations(); 
    listShowDialog.setOldFields(Association.getManyOneAssociations(allasts));
    thisLabel.setText("Select associations to implement with foreign key"); 
    System.out.println("Select associations to implement with foreign key");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      // System.out.println(vals[i]);
      { Association ast = (Association) vals[i]; 
        ast.introduceForeignKey(); 
      }
    }  
  }

  private void pushdownFeatures()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getInterfaces()); // getClasses?
    thisLabel.setText("Select interface to pushdown features from"); 
    System.out.println("Select interface to pushdown features from");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        if (vals[i] instanceof Entity)
        { Entity ent = (Entity) vals[i]; 
          ent.pushdownAttributes(); 
          ent.pushdownAssociations(ucdArea); 
        } 
        else 
        { System.out.println(vals[i] + " is not a class"); }
      } 
    } 
  }

  private void introduceBacktracking()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    Vector assocs = ucdArea.getAssociations(); 
    Vector cons = ucdArea.getConstraints(); 

    Vector poss = new Vector();
    for (int i = 0; i < assocs.size(); i++)
    { Association ast = (Association) assocs.get(i);
      if (ast.getCard2() == ModelElement.ZEROONE && ast.getRole2() != null)
      { poss.add(ast); }
    }
    listShowDialog.setOldFields(poss);
    thisLabel.setText("Select association to fill using matching"); 
    System.out.println("Select association to fill using matching");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      // System.out.println(vals[i]);
      { Association ast = (Association) vals[i]; 
        Transformation.fillUsingMatching(ast,cons); 
      }
    }  
  }

  private void convertToOperationalForm()
  { Vector res = new Vector();
    Vector sms = new Vector(); 
    res = invariants; 
    // sms = dcfdArea.getAllComponents();  

    /* for (int i = 0; i < invariants.size(); i++)
    { Invariant inv = (Invariant) invariants.elementAt(i);
      if ((inv instanceof SafetyInvariant) && inv.convertableToOp())
      { Vector newInvs = ucdArea.convertInv((SafetyInvariant) inv);
        res = VectorUtil.vector_merge(res,newInvs); 
      } 
    } */ 

    System.out.println(">> New action invariants are:");

    for (int k = 0; k < res.size(); k++)
    { Invariant ii = (Invariant) res.elementAt(k);
      ii.createActionForm(sms);
      /* ii.setSystem(true);  by default */
      // ii.typeCheck(sms); 
      // System.out.println(ii); 
    }

    invariants = VectorUtil.vector_merge(invariants,res); 
  }


  private void removeRedundancies()
  { Vector redundancies = new Vector(); 

    for (int i = 0; i < invariants.size(); i++) 
    { Invariant inv = (Invariant) invariants.elementAt(i); 
      if ((inv instanceof SafetyInvariant) && inv.convertableToOp())
      { for (int j = i+1; j < invariants.size(); j++) 
        { Invariant inv2 = (Invariant) invariants.elementAt(j); 
          if ((inv2 instanceof SafetyInvariant) && inv2.convertableToOp())
          { if (inv.succedent().equals(inv2.succedent()))
            { if (inv.antecedent().implies(inv2.antecedent()))
              { redundancies.add(inv); 
                System.out.println("!! Redundant invariant: " + inv); } 
              else if (inv2.antecedent().implies(inv.antecedent()))
              { redundancies.add(inv2);
                System.out.println("!! Redundant invariant: " + inv2); } 
            } 
          } 
        } 
      } 
    } 

    for (int k = 0; k < redundancies.size(); k++) 
    { invariants.remove(redundancies.elementAt(k)); } 
  } 

  private void removeMultipleInheritance()
  { Vector ents = ucdArea.getEntities(); 
    Vector list = new Vector();
    for (int i = 0; i < ents.size(); i++) 
    { Entity ent = (Entity) ents.get(i); 
      if (ent.hasMultipleInheritance())
      { System.out.println("!! Error: multiple inheritance in " + ent); 
        Entity oldsuperclass = ent.getSuperclass(); 
        Entity removedsup = ent.getSuperclasses(0); 
        Entity newent = ent.removeMultipleInheritance();
        ucdArea.removeInheritance(ent,oldsuperclass);
        ucdArea.removeInheritance(ent,removedsup); 
        ucdArea.addEntity(oldsuperclass,newent,100);
        ucdArea.addInheritance(newent,ent);  
      } // set visuals for newent's superclasses, interfaces
    } 
  }  
    

  private void removeRedundantInheritances()
  { Vector ents = ucdArea.getEntities(); 
    Vector list = new Vector();
    java.util.Map dups = new java.util.HashMap();  
    for (int i = 0; i < ents.size(); i++) 
    { Entity ent = (Entity) ents.get(i); 
      Vector dup = ent.hasDuplicateInheritance(); 
      if (dup.size() > 0)
      { System.out.println("!! Entity " + ent + " has duplicate inheritances of: " +
                           dup); 
        list.add(ent); 
        dups.put(ent,dup); 
      }
    }

    for (int i = 0; i < list.size(); i++) 
    { Entity e1 = (Entity) list.get(i); 
      Vector vv = (Vector) dups.get(e1); 
      for (int j = 0; j < vv.size(); j++) 
      { Entity e2 = (Entity) vv.get(j); 
        ucdArea.removeInheritance(e1,e2); 
      } 
    } 
  } 

  private void removeInheritance()
  { Vector gens = ucdArea.getGeneralisations(); 
    Vector newasts = new Vector(); 

    if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(gens);
    thisLabel.setText("Select generalisations to replace by an association"); 
    System.out.println(">>> Select generalisations to replace by an association"); 

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      // System.out.println(vals[i]);
      { Generalisation gen = (Generalisation) vals[i]; 
        Entity ans = gen.getAncestor(); 
        Entity dec = gen.getDescendent();
        String ansname = ans.getName(); 
        String role2 = ansname.toLowerCase() + "r"; 
        Association newast = new Association(dec,ans,ModelElement.ZEROONE,
                                             ModelElement.ONE,"",role2);  
        dec.addAssociation(newast); 
        ucdArea.removeInheritance(dec,ans); 
        newasts.add(newast); 
      }
    }  
    ucdArea.addAssociations(newasts); 
  } 

  private void createEntityActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to create activity for"); 
    System.out.println(">> Select entity to create activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.createEntityActivity(ent);
    } 
  }

  private void editEntityActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to edit activity of"); 
    System.out.println(">> Select entity to edit activity of");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.editEntityActivity(ent);
    } 
  }

  private void createOperationActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to create operation activity for"); 
    System.out.println(">> Select entity to create operation activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.createOperationActivity(ent);
    } 
  }

  private void editOperationActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to edit operation activity for"); 
    System.out.println(">> Select entity to edit operation activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.editOperationActivity(ent);
    } 
  }

  private void hoistOperationLocalDecs()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to hoist operation activity for"); 
    System.out.println(">> Select entity to hoist operation activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.hoistOperationLocalDecs(ent);
    } 
  }

  private void reduceCodeNesting()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to reduce code nesting for"); 
    System.out.println(">> Select entity to reduce code nesting for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.reduceCodeNesting(ent);
    } 
  }

  private void transformOperationActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to transform operation activity for"); 
    System.out.println(">> Select entity to transform operation activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.transformOperationActivity(ent);
    } 
  }

  private void replaceCallByDefinition()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entity to replace calls by definitions"); 
    System.out.println(">> Select entity to replace calls by definitions");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.replaceCallsByDefinitions(ent);
    } 
  }

  private void splitOperationActivity()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); 
    thisLabel.setText("Select entity to split operation activity for"); 
    System.out.println(">> Select entity to split operation activity for");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0 &&
        vals[0] instanceof Entity)
    { Entity ent = (Entity) vals[0];
      ucdArea.splitOperationActivity(ent);
    } 
  }

  private void createUseCaseActivity()
  { thisLabel.setText("Select use case to create activity for"); 
    System.out.println(">> Select use case to create activity for");
    String ucname = 
      JOptionPane.showInputDialog("Enter use case name:");

    ucdArea.createUseCaseActivity(ucname); 
  }

  private void editUseCaseActivity()
  { thisLabel.setText("Select use case to edit activity for"); 
    System.out.println(">> Select use case to edit activity for");
    String ucname = 
      JOptionPane.showInputDialog("Enter use case name:");

    ucdArea.editUseCaseActivity(ucname); 
  }

  public void setMessage(String txt)
  { thisLabel.setText(txt); } 

  private void aggregateSubclasses()
  { // replaces subclasses of given class by the class itself, 
    // attributes and associations involving these subclasses 
    // are moved up to the class. 
    if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    Vector allents = ucdArea.getEntities(); 
    Vector allassocs = ucdArea.getAssociations(); 

    listShowDialog.setOldFields(allents);
    thisLabel.setText("Select class to aggregate all subclasses of"); 
    System.out.println(">> Select class to aggregate all subclasses of");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        Entity ent = (Entity) vals[i];
        Vector ops = ent.getOperations();  
        Vector allsubs = ent.getAllSubclasses(); 
        System.out.println(allsubs);
        // form new type: 
        String ename = ent.getName(); 
        Vector enums = ModelElement.getNames(allsubs); 
        enums.add(ename); 
        ucdArea.addType(ename + "subclass",enums);
        ModelElement modetype = 
        ucdArea.getType(ename + "subclass"); 
        Attribute modeatt = 
          new Attribute(ename.toLowerCase() + "_in", modetype,
                        ModelElement.INTERNAL); 
        ent.addAttribute(modeatt);                                       

        for (int j = 0; j < allsubs.size(); j++) 
        { Entity sube = (Entity) allsubs.get(j); 
          Vector atts = sube.getAttributes(); 
          ent.addAttributes(atts); 
          Vector sops = sube.getOperations(); 
          // add to superclass if not already there.
          sops.removeAll(ops); 
          ops.addAll(sops);  
          Vector assts = sube.getAssociations(); // from sube
          ent.addAssociations(assts); // also need to modify their sources, visuals
          for (int k = 0; k < assts.size(); k++) 
          { Association ast = (Association) assts.get(k); 
            ucdArea.redirectAssociation(ent,ast); 
            ast.makeE1Optional(); 
          } 
          for (int p = 0; p < allassocs.size(); p++) 
          { Association ast = (Association) allassocs.get(p); 
            if (ast.getEntity2() == sube)
            { ast.setEntity2(ent); 
              ast.makeE2Optional(); 
              ucdArea.redirectAssociation(ast,ent); 
            } 
          } 
          Vector invs = sube.getInvariants(); 
          ent.addInvariants(invs); 
          ucdArea.removeEntity(sube); 
          VisualData vd = ucdArea.getVisualOf(sube); 
          ucdArea.removeVisual(vd); 
        } 
      }
    }  
  } 

  private void removeManyManyAssociations() 
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    Vector allasts = ucdArea.getAssociations(); 
    listShowDialog.setOldFields(Association.getManyManyAssociations(allasts));
    thisLabel.setText("Select associations to implement with new table class"); 
    System.out.println(">> Select associations to implement with new table class");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      // System.out.println(vals[i]);
      { Association ast = (Association) vals[i]; 
        ast.removeManyManyAssociation(ucdArea); 
      }
    }  
  }

  private void removeAssociationClasses() 
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    Vector allasts = ucdArea.getAssociationClasses(); 
    listShowDialog.setOldFields(allasts);
    thisLabel.setText("Select association classes to split"); 
    System.out.println(">> Select association classes to split");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      // System.out.println(vals[i]);
      { Entity ast = (Entity) vals[i]; 
        ast.removeAssociationClass(ucdArea); 
      }
    }  
  }

  private void makeSingletons()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(ucdArea.getEntities()); // getClasses?
    thisLabel.setText("Select entities to make into singletons"); 
    System.out.println(">> Select entities to make into singletons");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { System.out.println(vals[i]);
        if (vals[i] instanceof Entity)
        { Entity ent = (Entity) vals[i]; 
          ent.makeSingleton(); 
        } 
        else 
        { System.out.println(vals[i] + " is not a class"); }
      } 
    } 
  }
        
  private void checkForFacades()
  { // if 2 or more entities share 2 or more suppliers
    Vector ents = ucdArea.getEntities(); 
    java.util.Map suppliers = new java.util.HashMap(); 

    for (int i = 0; i < ents.size(); i++) 
    { Entity e = (Entity) ents.get(i); 
      Vector supps = e.getSuppliers(); 
      suppliers.put(e,supps); 
    }

    for (int i = 0; i < ents.size(); i++) 
    { Entity e = (Entity) ents.get(i); 
      for (int j = i+1; j < ents.size(); j++) 
      { Entity e2 = (Entity) ents.get(j); 
        Vector inter = new Vector(); 
        inter.addAll((Vector) suppliers.get(e)); 
        inter.retainAll((Vector) suppliers.get(e2)); 
        if (inter.size() > 1) 
        { String fname = ModelElement.underscoredNames(inter);
          String interdecs = ""; 
          for (int k = 0; k < inter.size(); k++) 
          { Entity supp = (Entity) inter.get(k); 
            String sname = supp.getName(); 
            interdecs = interdecs + 
                  "reference " + sname.toLowerCase() + " : " +
                  sname + ";\n  "; 
          }  
          System.out.println(">>> Possible facade: \n"); 
          System.out.println("class Facade_" + fname); 
          System.out.println("{ " + interdecs + "\n}\n"); 
          System.out.println("for " + e + " & " + e2);
          System.out.println();  
        } 
      } 
    } 
  }

  private void applyPhasedConstruction()
  { // Go through each use case, or select one
    Vector usecases = ucdArea.getGeneralUseCases(); 
    Vector ents = ucdArea.getEntities(); 
    Vector typs = ucdArea.getTypes(); 

    for (int i = 0; i < usecases.size(); i++) 
    { UseCase uc = (UseCase) usecases.get(i); 
      uc.applyPhasedConstruction(typs,ents); 
    } 
  } 

  public void buildGUI()
  { System.out.println(">> Specify input file for GUI description:"); 
    java.util.List invs = new java.util.ArrayList(); 

    BufferedReader br = null;
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File file;

    File startingpoint = new File("output");
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(startingpoint);
    fc.setDialogTitle("Load GUI constraints");
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    { file = fc.getSelectedFile(); }
    else
    { System.err.println("Load aborted");
      return; 
    }

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException e)
    { System.out.println("File not found: " + file);
      return; 
    }


    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException e)
      { System.out.println("Reading failed.");
        return; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else if (s.length() > 0)
      { invs.add(s); } 
    } 
    try { br.close(); } catch(IOException e) { }

    GUIBuilder gb = new GUIBuilder(); 
    java.util.List econs = gb.parseAll(invs);
    java.util.List objs = gb.objectSpecs(econs);
    System.out.println(objs);
    Vector ents = ucdArea.getEntities(); 

    if (objs.size() > 0)
    { ObjectSpecification fr = (ObjectSpecification) objs.get(0);
      String f = fr.getName();
      String nme = fr.objectClass;
      java.util.ArrayList eops = new java.util.ArrayList(); 

      for (int i = 0; i < ents.size(); i++) 
      { Entity ent = (Entity) ents.get(i); 
        String ename = ent.getName(); 
        eops.add("create" + ename); 
        eops.add("edit" + ename); 
      }         
      gb.buildMainFrame(fr,nme,objs,f,eops);
    } 

    for (int i = 0; i < ents.size(); i++) 
    { Entity ent = (Entity) ents.get(i); 
      System.out.println(ent.dialogDefinition("Ccreate" + ent.getName() + "Dialog")); 
    } 
  }

  public void implicitCopy()
  { Vector sources = ucdArea.getSourceEntities(); 
    Vector targets = ucdArea.getTargetEntities(); 

    System.out.println(">> Sources: " + sources); 
    System.out.println(">> Targets: " + targets); 

    java.util.Map entityattmaps = new java.util.HashMap(); 
    java.util.Map entityastmaps = new java.util.HashMap(); 

    System.out.println(">>> Possible source-target mappings are: "); 

    Vector maps = allEntityMaps(sources,targets); 
    if (maps.size() == 0) 
    { System.out.println("!! No mapping is possible!"); 
      return; 
    } 
    
    java.util.Map best = (java.util.Map) maps.get(0); 
    int highest = 0; 

    for (int i = 0; i < maps.size(); i++) 
    { java.util.Map mp = (java.util.Map) maps.get(i); 
      System.out.println(mp); 
      java.util.Map mpentityattmaps = new java.util.HashMap(); 
      java.util.Map mpentityastmaps = new java.util.HashMap(); 
      int score = 0; 
      for (int j = 0; j < sources.size(); j++) 
      { Entity src = (Entity) sources.get(j); 
        Entity trg = (Entity) mp.get(src); 
        Vector satts = src.getAttributes(); 
        Vector tatts = trg.getAttributes(); 
        Vector attmaps = 
                 ucdArea.allAttributeMaps(satts,tatts); 
        java.util.Map bestattmap = null; 
        int maxattmapsize = 0; 

        for (int k = 0; k < attmaps.size(); k++) 
        { java.util.Map attmap = (java.util.Map) attmaps.get(k); 
          // System.out.println(attmap); 
          score = score + attmap.size();
          if (attmap.size() > maxattmapsize) 
          { bestattmap = attmap; 
            maxattmapsize = attmap.size(); 
          }  
        }
        mpentityattmaps.put(src,bestattmap); 

        java.util.Map bestastmap = null; 
        int maxastmapsize = 0; 

        Vector sass = src.getAssociations(); 
        Vector tass = trg.getAssociations(); 
        Vector astmaps = allAssociationMaps(sass,tass,mp); 
        for (int k = 0; k < astmaps.size(); k++) 
        { java.util.Map astmap = (java.util.Map) astmaps.get(k); 
          score = score + astmap.size(); 
          // System.out.println(astmap); 
          if (astmap.size() > maxastmapsize) 
          { bestastmap = astmap; 
            maxastmapsize = astmap.size(); 
          }  
        } 
        mpentityastmaps.put(src,bestastmap); 
      }

      if (score > highest) 
      { best = mp; 
        highest = score; 
        entityattmaps.clear(); 
        entityattmaps.putAll(mpentityattmaps); 
        entityastmaps.clear(); 
        entityastmaps.putAll(mpentityastmaps); 
      }           
    } 
    System.out.println(">> Best entity map is: " + best); 
    // Then choose its attribute and association maps
    Vector cons = ucdArea.maps2constraints(sources,best,
                           entityattmaps,entityastmaps);


    UseCase uc = new UseCase("implicitcopy",null); 

    for (int p = 0; p < cons.size(); p++) 
    { Constraint cn = (Constraint) cons.get(p); 
      // System.out.println(cn); 
      uc.addPostcondition(cn); 
    }  
    Vector types = ucdArea.getTypes(); 
    Vector entities = ucdArea.getEntities(); 
    uc.typeCheck(types,entities);
    ucdArea.addGeneralUseCase(uc);  
  } 


  public Vector allEntityMaps(Vector sources, Vector targets)
  { Vector res = new Vector();
    if (sources.size() == 0)
    { res.add(new java.util.HashMap()); 
      return res;
    }
    Vector subsources = new Vector();
    subsources.addAll(sources);
    Entity src = (Entity) sources.get(0);
    subsources.remove(0);
    Vector submaps = allEntityMaps(subsources, targets);
    for (int i = 0; i < submaps.size(); i++)
    { java.util.Map f = (java.util.Map) submaps.get(i);
      Vector rng = new Vector();
      rng.addAll(f.values());
      // if (rng.containsAll(targets)) 
      // { res.add(f); } else
      for (int j = 0; j < targets.size(); j++)
      { Entity trg = (Entity) targets.get(j);
        if (rng.contains(trg)) { }
        else if (src.isAbstract() && !(trg.isAbstract())) { }
        else 
        { java.util.Map g = new java.util.HashMap();
          g.putAll(f); g.put(src,trg);
          res.add(g);
        }
      }
    }
    return res;
  }

  public void settupATLrefiningMode()
  { Vector sources = ucdArea.getSourceEntities(); 
    for (int i = 0; i < sources.size(); i++) 
    { Entity ee = (Entity) sources.get(i); 
      if (ee.isRoot() && !ee.isInterface())
      { ee.addPrimaryKey("$id"); } 
    } 
    copyMetamodel(); 
    Vector maps = defaultEntityMap(); 
    java.util.Map emap = (java.util.Map) maps.get(0); 
    java.util.Map atmap = (java.util.Map) maps.get(1); 
    java.util.Map astmap = (java.util.Map) maps.get(2);
    Vector cons = 
      ucdArea.maps2constraints(sources,emap,atmap,astmap);
    // UseCase uc = new UseCase("refiningATL",null);  
    // for (int i = 0; i < cons.size(); i++) 
    // { Constraint con = (Constraint) cons.get(i); 
    //   System.out.println(con); 
    //   uc.addPostcondition(con); 
    // } 
    
    Vector entities = ucdArea.getEntities(); 
    Vector types = ucdArea.getTypes(); 

    Compiler2 c = new Compiler2(); 
    BufferedReader br = null;
    Vector res = new Vector();
    String s;
    boolean eof = false;
    File file = new File("output/mm.atl");  /* default */ 

    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException _e)
    { System.out.println("!! File not found: " + file);
      return; 
    }

    String atlstring = ""; 

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException _ex)
      { System.out.println("!! Reading failed.");
        return; 
      }
      if (s == null) 
      { eof = true; 
        break; 
      }
      else 
      { atlstring = atlstring + s + " "; } 
    }
    c.nospacelexicalanalysis(atlstring); 
    
        //  c.displaylexs(); 
    ATLModule mr = c.parseATL(entities,types); 
    UseCase uc = mr.toUML(types,entities,cons);  
    uc.typeCheck(types,entities); 
    System.out.println(mr);  
    ucdArea.addGeneralUseCase(uc);  
  } 
 

  public Vector defaultEntityMap()
  { Vector sources = ucdArea.getSourceEntities(); 
    Vector targets = ucdArea.getTargetEntities(); 
    java.util.Map res = new java.util.HashMap(); 
    Vector maps = new Vector(); 
    java.util.Map attmaps = new java.util.HashMap(); 
    java.util.Map astmaps = new java.util.HashMap(); 

    for (int i = 0; i < sources.size(); i++) 
    { Entity src = (Entity) sources.get(i); 
      // assume it has name IN$E
      String sname = src.getName(); 
      String bname = ModelElement.baseName(sname);
      Entity trg = (Entity) ModelElement.lookupByName("OUT$" + bname, targets);
      if (trg != null) 
      { res.put(src,trg); 
        java.util.Map sattmap = defaultAttributeMap(src,trg);
        attmaps.put(src, sattmap);
        java.util.Map sastmap = defaultAssociationMap(src,trg); 
        astmaps.put(src, sastmap); 
      } 
    }
    maps.add(res); maps.add(attmaps); maps.add(astmaps);  
    return maps; 
  } 

          
  private java.util.Map defaultAttributeMap(Entity src, Entity trg) 
  { java.util.Map res = new java.util.HashMap(); 
    Vector srcatts = src.getAttributes(); 
    Vector trgatts = trg.getAttributes(); 
    for (int i = 0; i < srcatts.size(); i++) 
    { Attribute satt = (Attribute) srcatts.get(i); 
      Attribute tatt = (Attribute) ModelElement.lookupByName(satt.getName(), trgatts); 
      if (tatt != null) 
      { res.put(satt,tatt); } 
    } 
    return res; 
  } 

  private java.util.Map defaultAssociationMap(Entity src, Entity trg) 
  { java.util.Map res = new java.util.HashMap(); 
    Vector srcatts = src.getAssociations(); 
    // Vector trgatts = trg.getAssociations(); 
    for (int i = 0; i < srcatts.size(); i++) 
    { Association satt = (Association) srcatts.get(i); 
      Association tatt = trg.getRole(satt.getRole2()); 
      if (tatt != null) 
      { res.put(satt,tatt); } 
    } 
    return res; 
  } 

/* 
  public Vector maps2constraints(Vector entities, 
                  java.util.Map entityMap, 
                  java.util.Map attmaps, 
                  java.util.Map astmaps)
  { Vector res = new Vector();
        // copy the objects and attributes
    Vector phase2 = new Vector();
        // copy the association ends

    for (int i = 0; i < entities.size(); i++)
    { Entity ent = (Entity) entities.get(i);
      Entity tent = (Entity) entityMap.get(ent);
      if (tent == null) { continue; }
      String tname = tent.getName();
      Type ttype = new Type(tent); 

      BasicExpression texp = new BasicExpression(tname);
      texp.setUmlKind(Expression.CLASSID); 
      texp.setEntity(tent); 
      texp.setType(new Type("Set",null)); 
      texp.setElementType(ttype); 

      BasicExpression texp2 = new BasicExpression(tname);
      texp2.setUmlKind(Expression.CLASSID); 
      texp2.setEntity(tent); 
      texp2.setType(ttype); 
      texp2.setElementType(ttype); 
      BasicExpression idexp2 = new BasicExpression("$id"); 
      idexp2.setUmlKind(Expression.ATTRIBUTE); 
      idexp2.setType(new Type("String",null)); 
      idexp2.setEntity(ent); 
      texp2.setArrayIndex(idexp2); 

      String tx = tname.toLowerCase() + "x";
      BasicExpression txexp = new BasicExpression(tx);
      txexp.setUmlKind(Expression.VARIABLE); 
      txexp.setType(ttype); 
      txexp.setElementType(ttype); 

      BasicExpression txexp2 = new BasicExpression(tx);
      txexp2.setUmlKind(Expression.VARIABLE); 
      txexp2.setType(ttype); 
      txexp2.setEntity(tent); 
      txexp2.setElementType(ttype); 

      Vector atts = ent.getAttributes();
      java.util.Map attmap = (java.util.Map) attmaps.get(ent);
      Expression tcond = new BasicExpression("true");
      tcond.setUmlKind(Expression.VALUE); 
      tcond.setType(new Type("boolean",null)); 

      for (int j = 0; j < atts.size(); j++)
      { Attribute att = (Attribute) atts.get(j);
        Attribute tatt = (Attribute) attmap.get(att);
        if (tatt != null)
        { String attname = att.getName(); 
          BasicExpression attexp = new BasicExpression(attname);
          attexp.setUmlKind(Expression.ATTRIBUTE); 
          attexp.setEntity(ent); 
          attexp.setType(att.getType()); 
          
          String tattname = tatt.getName();
          BasicExpression tr = new BasicExpression(tattname);
          tr.setUmlKind(Expression.ATTRIBUTE); 
          tr.setEntity(tent); 
          tr.setType(tatt.getType()); 
          tr.setObjectRef(txexp);
          BinaryExpression eqatt = new BinaryExpression("=", tr, attexp);
          tcond = Expression.simplifyAnd(tcond, eqatt);
        }
      }
      BinaryExpression rang = new BinaryExpression(":", txexp, texp);
      Expression post = new BinaryExpression("#", rang, tcond);
      Constraint con = new Constraint(new BasicExpression("true"), post);
      con.setOwner(ent);
      res.add(con);

      Expression tcond2 = new BasicExpression("true");
      tcond2.setUmlKind(Expression.VALUE); 
      tcond2.setType(new Type("boolean",null)); 

      Vector asts = ent.getAssociations();
      java.util.Map astmap = (java.util.Map) astmaps.get(ent);
      for (int j = 0; j < asts.size(); j++)
      { Association ast = (Association) asts.get(j);
        Association tast = (Association) astmap.get(ast);
        if (tast != null)
        { String tastname = tast.getRole2();
          Entity e2 = tast.getEntity2(); 
          String e2name = e2.getName(); 

          BasicExpression idexp = new BasicExpression("$id"); 
          idexp.setUmlKind(Expression.ATTRIBUTE); 
          idexp.setType(new Type("String",null)); 
          idexp.setEntity(ast.getEntity2()); 
          BasicExpression e2exp = new BasicExpression(e2name);
          e2exp.setUmlKind(Expression.CLASSID); 
          BasicExpression r2exp = new BasicExpression(ast.getRole2()); 
          r2exp.setUmlKind(Expression.ROLE); 
          r2exp.setEntity(ent); 
          idexp.setObjectRef(r2exp);  
          e2exp.setArrayIndex(idexp);
 
          BasicExpression tra = new BasicExpression(tastname);
          tra.setObjectRef(txexp2);
          tra.setUmlKind(Expression.ROLE); 
          tra.setEntity(tast.getEntity1()); 
          
          BinaryExpression eqast = 
            new BinaryExpression("=", tra, e2exp);
          tcond2 = Expression.simplifyAnd(tcond2, eqast);
        }  // No, needs to be tra = tast.getEntity2()[ast.getRole2().$id]
      }
      BinaryExpression preeq = new BinaryExpression("=", txexp2, texp2);
      Constraint con2 = new Constraint(preeq, tcond2);
      con2.setOwner(ent);
      Attribute txatt = new Attribute(tx, ttype, ModelElement.INTERNAL); 
      txatt.setEntity(tent); 
      txatt.setElementType(ttype); 
      con2.addLetVar(txatt,tx,texp2); 
      phase2.add(con2);
    }
    res.addAll(phase2); 
    return res;
  } */ 

 /* 
  public Vector allAttributeMaps(Vector sourceatts, Vector targetatts)
  { Vector res = new Vector();
    if (sourceatts.size() == 0)
    { res.add(new java.util.HashMap()); 
      return res;
    }
    Vector subsources = new Vector();
    subsources.addAll(sourceatts);
    Attribute src = (Attribute) sourceatts.get(0);
    subsources.remove(0);
    Vector submaps = allAttributeMaps(subsources, targetatts);
    for (int i = 0; i < submaps.size(); i++)
    { java.util.Map f = (java.util.Map) submaps.get(i);
      Vector rng = new Vector();
      rng.addAll(f.values());
      // if (rng.containsAll(targets)) 
      // { res.add(f); } else
      for (int j = 0; j < targetatts.size(); j++)
      { Attribute trg = (Attribute) targetatts.get(j);
        if (rng.contains(trg)) { }
        else if (Type.isSubType(src.getType(), trg.getType())) 
        { java.util.Map g = new java.util.HashMap();
          g.putAll(f); g.put(src,trg);
          res.add(g);
        }
      }
    }
    return res;
  } */ 

  public Vector allAssociationMaps(Vector sourceasts, Vector targetasts, 
                                   java.util.Map chi)
  { Vector res = new Vector();
    if (sourceasts.size() == 0)
    { res.add(new java.util.HashMap()); 
      return res;
    }
    Vector subsources = new Vector();
    subsources.addAll(sourceasts);
    Association src = (Association) sourceasts.get(0);
    subsources.remove(0);
    Vector submaps = allAssociationMaps(subsources, targetasts, chi);
    for (int i = 0; i < submaps.size(); i++)
    { java.util.Map f = (java.util.Map) submaps.get(i);
      Vector rng = new Vector();
      rng.addAll(f.values());
      // if (rng.containsAll(targets)) 
      // { res.add(f); } else
      for (int j = 0; j < targetasts.size(); j++)
      { Association trg = (Association) targetasts.get(j);
        if (rng.contains(trg)) { }
        else if (chi.get(src.getEntity2()) == trg.getEntity2() && 
                 src.getCard2() == trg.getCard2() &&                  
                 src.getCard1() == trg.getCard1()) 
        { java.util.Map g = new java.util.HashMap();
          g.putAll(f); g.put(src,trg);
          res.add(g);
        }
      }
    }
    return res;
  }

  public void copyMetamodel()
  { // copies all source elements to corresponding target ones
    Vector sources = ucdArea.getSourceEntities(); 
    java.util.Map srctrg = new java.util.HashMap(); 

    for (int i = 0; i < sources.size(); i++) 
    { Entity src = (Entity) sources.get(i); 
      Entity trg = src.targetCopy(); 
      srctrg.put(src,trg); 
      ucdArea.addEntity(src,trg,400); 
    } 

    for (int i = 0; i < sources.size(); i++) 
    { Entity src = (Entity) sources.get(i); 
      Entity trg = (Entity) srctrg.get(src);
      Vector newelems = src.copyToTarget(trg, srctrg); 
      ucdArea.addElements(newelems); // Associations or Generalisations
    }          
  } 

  public void randomModels2Python()
  { ucdArea.randomModel(); 
    ucdArea.typeCheck(); 
    ucdArea.typeCheck();
    // ucdArea.generateDesign(); 

    long index = (new java.util.Date()).getTime(); 

    try
    { File km3file = 
        new File("output/example" + index + ".km3"); 

      PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(km3file)));

      ucdArea.saveKM3(out);
      out.flush();  
      out.close();

      System.out.println(">>> Saved UML to file " + km3file); 

      Thread.sleep(100); 
    }
    catch (Exception e) 
    { System.err.println("!! Error saving KM3"); }
 
    try
    { Thread.sleep(100); 
   
      ucdArea = new UCDArea(this); 
            
      File km3file = 
        new File("output/example" + index + ".km3"); 
      ucdArea.loadKM3FromFile(km3file); 
      ucdArea.typeCheck(); 
      ucdArea.typeCheck();
      // ucdArea.generateDesign(); 

      ucdArea.saveModelToFile("output/model.txt"); 

      RunApp rapp1 = new RunApp("uml2py3"); 
      rapp1.setFile("app" + index + ".py"); 
      Thread appthread = new Thread(rapp1); 
      appthread.start();

      Thread.sleep(200);  
    } 
    catch (Exception ee2) 
    { System.err.println("!! Unable to run uml2py3.jar"); } 
  } 

  public static void main(String[] args) 
  { UmlTool window = new UmlTool();
  
    if (args.length == 1 && "-java2python".equals(args[0]))
    { window.ucdArea.java2python(); 
      window.ucdArea.saveModelToFile("output/model.txt"); 

      RunApp rapp1 = new RunApp("uml2py"); 

      try
      { rapp1.setFile("app.py"); 
        Thread appthread = new Thread(rapp1); 
        appthread.start(); 
      } 
      catch (Exception ee2) 
      { System.err.println("!! Unable to run uml2py"); } 
      return; 
    } 

    if (args.length == 1 && "-js2python".equals(args[0]))
    { window.ucdArea.javascript2python(); 
      window.ucdArea.saveModelToFile("output/model.txt"); 

      RunApp rapp1 = new RunApp("uml2py"); 

      try
      { rapp1.setFile("app.py"); 
        Thread appthread = new Thread(rapp1); 
        appthread.start(); 
      } 
      catch (Exception ee2) 
      { System.err.println("!! Unable to run uml2py"); } 
      return; 
    } 

    if (args.length == 1 && "-java2csharp".equals(args[0]))
    { window.ucdArea.java2csharp(); 
      return; 
    } 

    if (args.length == 1 && "-java2cpp".equals(args[0]))
    { window.ucdArea.java2cpp(); 
      return; 
    } 

    if (args.length == 1 && "-java2swift".equals(args[0]))
    { window.ucdArea.java2swift(); 
      return; 
    } 

    if (args.length == 1 && "-vb2py".equals(args[0]))
    { window.ucdArea.vb2py(); 
      window.ucdArea.saveModelToFile("output/model.txt"); 

      RunApp rapp1 = new RunApp("uml2py"); 

      try
      { rapp1.setFile("app.py"); 
        Thread appthread = new Thread(rapp1); 
        appthread.start(); 
      } 
      catch (Exception ee2) 
      { System.err.println("!! Unable to run uml2py"); } 

      return; 
    } 

    if (args.length == 1 && "-cobol2java".equals(args[0]))
    { window.ucdArea.cobol2java(); 
      return; 
    } 

    if (args.length == 1 && "-python2java".equals(args[0]))
    { window.ucdArea.python2java(); 
      return; 
    } 

    if (args.length == 3 && "-cgtl".equals(args[0]))
    { window.ucdArea.applyCSTLtoAST(args[1],args[2]); 
      return; 
    } 

    if (args.length == 3 && "-compareModels".equals(args[0]))
    { window.ucdArea.compareModels(args[1], args[2]);
      return;
    }

    if (args.length == 3 && "-cgbeValidate".equals(args[0]))
    { String fname = args[1]; 
      String astfile = args[2]; 
      try 
      { File cstlfile = new File(fname); 
        window.ucdArea.testCSTLwithASTS(cstlfile,astfile);
      } catch (Exception ex) { 
        System.err.println("!!ERROR: No file: " + fname); 
      }  
      return; 
    } 

    if (args.length == 2 && "-cgbeTrain".equals(args[0]))
    { String fname = args[1]; 
      try 
      { window.ucdArea.cgbeOCL2Program(fname);
      } catch (Exception ex) { 
        System.err.println("!!ERROR: No file: " + fname); 
      }  
      return; 
    } 

    if (args.length == 1 && "-antlr2cstl".equals(args[0]))
    { ASTCompositeTerm.convertAntlr2CSTL(); 
      return; 
    } 

    window.setTitle("AgileUML Toolset, Eclipse Incubation Project Version 2.3");
    window.setControllerName("Controller"); 
    window.setSize(500, 400);
    window.setVisible(true);   

    if (args.length == 1) 
    { 
      window.ucdArea.loadFromFile("mm.txt");
      if ("-jsp".equals(args[0]))
      { File file = new File("output/tmp");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          window.ucdArea.generateJSPWebSystem(out);
          out.close();
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Web System"); }

        new TextDisplay("Web System code","output/tmp");
        return; 
      } 

      window.ucdArea.generateDesign();
      if ("-gj".equals(args[0])) 
      { File file = new File("output/Controller.java");
        File file2 = new File("output/SystemTypes.java");
        File file3 = new File("output/ControllerInterface.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          PrintWriter out3 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file3)));
          window.ucdArea.generateJava(out,out2,out3);
          out.close();
          out2.close(); 
          out3.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        TextDisplay td = new TextDisplay("Java code","output/Controller.java");
        td.setFont(new java.awt.Font("Serif",java.awt.Font.BOLD,18)); 
      }       
      else if ("-gj6".equals(args[0]))
      { File file = new File("output/Controller.java");
        File file2 = new File("output/SystemTypes.java");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          window.ucdArea.generateJava6(out,out2);
          out.close();
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating Java"); }

        new TextDisplay("Java code","output/Controller.java");
      } 
      else if ("-gcs".equals(args[0]))
      { File file = new File("output/Controller.cs");
        File file2 = new File("output/SystemTypes.cs");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          window.ucdArea.generateCSharp(out,out2);
          out.close();
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating C#"); }

        new TextDisplay("C# code","output/Controller.cs");
      } 
      else if ("-gcpp".equals(args[0]))
      { File file = new File("output/Controller.h");
        File file2 = new File("output/Controller.cpp");
        try
        { PrintWriter out = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file)));
          PrintWriter out2 = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(file2)));
          window.ucdArea.generateCPP(out,out2);
          out.close();
          out2.close(); 
        }
        catch (IOException ex)
        { System.out.println("!! Error generating C++"); }

        new TextDisplay("C++ code","output/Controller.h");
      } 
    }    
  }
}

class StreamGobble extends Thread
{ InputStream str; 
  
  StreamGobble(InputStream st)
  { str = st; } 

  public void run() 
  { try { 
      InputStreamReader isr = new InputStreamReader(str); 
      BufferedReader br = new BufferedReader(isr); 
      String line = br.readLine(); 
      while (line != null) 
      { System.out.println(line); 
        line = br.readLine(); 
      } 
    } catch (IOException ioe)
    { ioe.printStackTrace(); } 
  }    
} 


