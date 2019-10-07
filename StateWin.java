/*
      * Classname : StateWin
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the (JFrame) window for producing
      * the statecharts for each component in the DCFD diagram. It has its 
      * own menu and tool bar. (extension - provide statechart properties) 

  package: Statemachine editor
*/
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*; 
import java.awt.print.*; 
import java.util.Vector; 
import java.util.StringTokenizer; 


public class StateWin extends JFrame implements ActionListener 
{ private JLabel messageLabel;	 
  private StatechartArea drawArea;
  SmBuilder builder; 

  JMenuItem attMI; 
  JMenuItem javaSynthesis1;
  JMenuItem javaSynthesis2;
  JMenuItem javaSynthesis3;
  JMenuItem setMultiplicity; 

  private TempInvEditDialog tinvDialog;
 
  public StateWin(String s, SmBuilder sm) 
  {
    builder = sm; 
    JMenuBar menuBar;
    JMenu fileMenu, createMenu;
    JMenuItem saveMenu; 
    JMenuItem stateMenu;
    JMenuItem transMenu;
    JMenuItem state2Menu;
    JMenuItem eventMenu;
    
    setDefaultCloseOperation(HIDE_ON_CLOSE);

    //Add regular components to the window, using the default BorderLayout.
    Container contentPane = getContentPane();

    drawArea = new StatechartArea(this,s);	
    if (builder != null) 
    { builder.build(drawArea); } 

    //Put the drawing area in a scroll pane.
    JScrollPane state_scroll = new JScrollPane(drawArea, 
				     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    state_scroll.setPreferredSize(new Dimension(1000,2000));
    contentPane.add(state_scroll, BorderLayout.CENTER);
	
    messageLabel = new JLabel("Click within the framed area. Press keys u l r d to navigate");
    contentPane.add(messageLabel, BorderLayout.SOUTH); 

   
    //Create the menu bar.
    menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    // FILE menu heading
    //---------------------
    fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.getAccessibleContext().setAccessibleDescription(
			"Allows user to save/file work");
    menuBar.add(fileMenu);
    
    //a group of JMenuItems under the "File" option
    saveMenu = new JMenu("Save");
    // saveMenu.setMnemonic(KeyEvent.VK_S); 
       
    fileMenu.add(saveMenu);

    JMenuItem saveJavaMI = new JMenuItem("Save Java"); 
    saveJavaMI.addActionListener(this); 
    saveMenu.add(saveJavaMI); 

    JMenuItem saveBMI = new JMenuItem("Save B"); 
    saveBMI.addActionListener(this); 
    saveMenu.add(saveBMI); 

    JMenuItem saveDataMI = new JMenuItem("Save data"); 
    saveDataMI.addActionListener(this); 
    saveMenu.add(saveDataMI); 
    
    fileMenu.addSeparator(); 

    JMenuItem loadMenu = new JMenuItem("Load data"); 
    loadMenu.addActionListener(this);
    fileMenu.add(loadMenu); 
    
    fileMenu.addSeparator();
        
    JMenuItem printMI = new JMenuItem("Print");
    printMI.addActionListener(this);
    fileMenu.add(printMI); 

    JMenuItem copyMI = new JMenuItem("Create Instance");
    copyMI.addActionListener(this);
    fileMenu.add(copyMI); 

    JMenuItem productMI = new JMenuItem("Create Product");
    productMI.addActionListener(this);
    fileMenu.add(productMI); 

    // VIEW menu heading
    //--------------------
    JMenu viewMenu = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V); 
    // menuBar.add(viewMenu);

    JMenuItem stateList = new JMenuItem("State_List");
    stateList.addActionListener(this);
    viewMenu.add(stateList);

    JMenuItem transList = new JMenuItem("Transition_List");
    transList.addActionListener(this);
    viewMenu.add(transList);

    JMenuItem eventList = new JMenuItem("Event_List");
    eventList.addActionListener(this);
    viewMenu.add(eventList);

    JMenuItem attList = new JMenuItem("Attribute_List");
    attList.addActionListener(this);
    viewMenu.add(attList);

    //CREATE menu heading
    //--------------------
    createMenu = new JMenu("Create");
    createMenu.setMnemonic(KeyEvent.VK_C);
    createMenu.getAccessibleContext().setAccessibleDescription(
	     "To create states and transitions");
    menuBar.add(createMenu);
    
    //a group of JMenuItems for Create
    stateMenu = new JMenuItem("State");
    stateMenu.setMnemonic(KeyEvent.VK_S); 
    stateMenu.setAccelerator(KeyStroke.getKeyStroke(
       KeyEvent.VK_S, ActionEvent.ALT_MASK));
    stateMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws a state as a rounded rectangle");
    stateMenu.addActionListener(this);
    createMenu.add(stateMenu);

    state2Menu = new JMenuItem("OR state");
    state2Menu.getAccessibleContext().setAccessibleDescription(
	  "Draws an OR state as a rounded rectangle");
    state2Menu.addActionListener(this);
    createMenu.add(state2Menu);
    
    transMenu = new JMenuItem("Transition");
    transMenu.setMnemonic(KeyEvent.VK_T); 
    transMenu.setAccelerator(KeyStroke.getKeyStroke(
       KeyEvent.VK_T, ActionEvent.ALT_MASK));
    createMenu.add(transMenu);
    transMenu.addActionListener(this); 
    
    // JMenuItem inputMenu = new JMenuItem("Input");
    // inputMenu.addActionListener(this);
    // transMenu.add(inputMenu);
    
    // JMenuItem outputMenu = new JMenuItem("Output");
    // outputMenu.addActionListener(this);
    // transMenu.add(outputMenu);
           
    /* eventMenu = new JMenuItem("Event",KeyEvent.VK_E);
    eventMenu.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_E, ActionEvent.ALT_MASK));
    eventMenu.getAccessibleContext().setAccessibleDescription(
			     "Provide the name of an event on a transition");
    eventMenu.addActionListener(this);
    createMenu.add(eventMenu); */ 

    createMenu.addSeparator(); 

    attMI = new JMenuItem("Attribute"); 
    attMI.addActionListener(this); 
    createMenu.add(attMI); 

    int componentType = drawArea.getCType(); 
    System.out.println("CType is: " + componentType); 
    // if (componentType != DCFDArea.ACTUATOR) 
    attMI.setEnabled(false);  

   //EDIT menu heading 
   //--------------------
   JMenu editMenu = new JMenu("Edit");
   editMenu.getAccessibleContext().setAccessibleDescription("To edit states and transitions"); 
   menuBar.add(editMenu);
   menuBar.add(viewMenu); 

   JMenuItem editState = new JMenuItem("Edit"); 
   JMenuItem moveElement = new JMenuItem("Move");
   JMenuItem gluemoveMI = new JMenuItem("Glue move");  
   JMenuItem resizeMI = new JMenuItem("Resize");  

   JMenuItem deleteElement = new JMenuItem("Delete"); 
   JMenuItem deleteAtt = new JMenuItem("Delete Attribute"); 
   JMenuItem shrinkMI = new JMenuItem("Shrink"); 

   editState.addActionListener(this); 
   moveElement.addActionListener(this);
   gluemoveMI.addActionListener(this); 
   resizeMI.addActionListener(this); 
   deleteElement.addActionListener(this);
   deleteAtt.addActionListener(this);
   shrinkMI.addActionListener(this);

   editMenu.add(editState); 
   editMenu.add(moveElement);
   editMenu.add(gluemoveMI);  
   editMenu.add(resizeMI);  
   editMenu.add(deleteElement); 
   editMenu.add(deleteAtt); 
   editMenu.add(shrinkMI); 

   editMenu.addSeparator(); 

   setMultiplicity = new JMenuItem("Set Multiplicity"); 
   setMultiplicity.addActionListener(this);
   editMenu.add(setMultiplicity); 
   // if (componentType != DCFDArea.ACTUATOR)
   setMultiplicity.setEnabled(false); 

   // Synthesis MENU
   JMenu synthMenu = new JMenu("Synthesis"); 
   menuBar.add(synthMenu); 

   JMenuItem checkMI = new JMenuItem("Check Structure"); 
   checkMI.addActionListener(this);
   synthMenu.add(checkMI); 
   synthMenu.addSeparator(); 

   JMenuItem bSynthesis = new JMenuItem("Synthesise B");
   bSynthesis.addActionListener(this); 
   synthMenu.add(bSynthesis); 

   // JMenuItem bSynthSingle = new JMenuItem("Single Instance"); 
   // bSynthSingle.addActionListener(this);
   // bSynthesis.add(bSynthSingle); 

   // JMenuItem bSynthMult = new JMenuItem("Multiple Instances");
   // bSynthMult.addActionListener(this);
   // bSynthesis.add(bSynthMult);

   javaSynthesis1 = new JMenuItem("Sequential Java"); 
   javaSynthesis1.addActionListener(this);
   synthMenu.add(javaSynthesis1); 

   javaSynthesis2 = new JMenuItem("Simulation Loop Java"); 
   javaSynthesis2.addActionListener(this);
   synthMenu.add(javaSynthesis2); 

   javaSynthesis3 = new JMenuItem("Concurrent Java"); 
   javaSynthesis3.addActionListener(this);
   synthMenu.add(javaSynthesis3); 

   JMenuItem smvSynthesis = new JMenuItem("Synthesise SMV"); 
   smvSynthesis.addActionListener(this);
   synthMenu.add(smvSynthesis);

   synthMenu.addSeparator(); 
   JMenuItem verifyInvMI = new JMenuItem("Behavioural Compatibility"); 
   verifyInvMI.addActionListener(this);
   synthMenu.add(verifyInvMI); 

   JMenuItem sliceMI = new JMenuItem("Data slice"); 
   sliceMI.addActionListener(this);
   synthMenu.add(sliceMI); 

   JMenuItem iesliceMI = new JMenuItem("Input event slice"); 
   iesliceMI.addActionListener(this);
   synthMenu.add(iesliceMI); 

   JMenuItem oesliceMI = new JMenuItem("Output event slice"); 
   oesliceMI.addActionListener(this);
   synthMenu.add(oesliceMI); 
  }

  public StatechartArea getDrawArea() 
  { return drawArea; } 

  public void enableExtraMenus()
  { attMI.setEnabled(true); 
    // setMultiplicity.setEnabled(true); 
    javaSynthesis1.setEnabled(true); 
    javaSynthesis2.setEnabled(true); 
    javaSynthesis3.setEnabled(true); 
  } 
  
  public void disableExtraMenus()
  { attMI.setEnabled(false); 
    javaSynthesis1.setEnabled(false); 
    javaSynthesis2.setEnabled(false); 
    javaSynthesis3.setEnabled(false); 
  } 

  public void actionPerformed(ActionEvent e)
  {
    Object eventSource = e.getSource();
    if (eventSource instanceof JMenuItem)
    {
	String label = (String) e.getActionCommand();
	if (label.equals("Save Java"))
	{ System.out.println("Saving Java code of module");
        // add methods for saving module etc. 
        //  drawArea.disp_States();
        //  drawArea.disp_Trans();

        File startingpoint = new File("output"); 
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(startingpoint); 
        fc.setDialogTitle("Save Java code"); 
        int returnVal = fc.showSaveDialog(this); 
        if (returnVal == JFileChooser.APPROVE_OPTION)
        { File file = fc.getSelectedFile();
          drawArea.saveJavaToFile(file);
          messageLabel.setText("Saved Java code"); 
        } 
      }
      else if (label.equals("Save B"))
      { System.out.println("Saving B code of module");
        File startingpoint = new File("output"); 
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(startingpoint); 
        fc.setDialogTitle("Save B code"); 
        int returnVal = fc.showSaveDialog(this); 
        if (returnVal == JFileChooser.APPROVE_OPTION)
        { File file = fc.getSelectedFile();
          drawArea.saveBToFile(file);
          messageLabel.setText("Saved B code"); 
        } 
      }
      else if (label.equals("Save data")) 
      { String modulename = drawArea.getName(); 
        drawArea.saveDataToFile("output/" + modulename + ".dat"); 
        messageLabel.setText("Saved data to output/" + modulename + ".dat"); 
      } 
      else if (label.equals("Load data")) 
      { String modulename = drawArea.getName(); 
        drawArea.loadDataFromFile("output/" + modulename + ".dat");
        messageLabel.setText("Loaded data from output/" + modulename + ".dat"); 
      } 
      else if (label.equals("State_List"))
      { System.out.println("List of States");
        System.out.println("---------------");
        drawArea.disp_States();
       }
       else if (label.equals("Print"))
       { printData(); } 
       else if (label.equals("Create Instance"))
       { String component = JOptionPane.showInputDialog("Instance name:"); 
         StateWin state_window = new StateWin(component,null);

         state_window.setTitle(component);
         state_window.setSize(300, 300);
         state_window.setVisible(true);

         drawArea.createCopy(component,state_window.getDrawArea());
       } 
       else if (label.equals("Create Product"))
       { String c1 = JOptionPane.showInputDialog("File 1 name:");
         String c2 = JOptionPane.showInputDialog("File 2 name:"); 
         drawArea.createProduct(c1,c2); 
       } 
       else if (label.equals("Transition_List"))
       {
        System.out.println("List of Transitions");
        System.out.println("-------------------");
        drawArea.disp_Trans();
       }
       else if (label.equals("Event_List"))
       {
        System.out.println("List of Events");
        System.out.println("-------------------");
        drawArea.dispEvents();
       }
       else if (label.equals("Attribute_List"))
       {
        System.out.println("List of Attributes");
        System.out.println("-------------------");
        drawArea.dispAttributes();
       }
       else if (label.equals("State"))
       { System.out.println("Creating a state");
         drawArea.setDrawMode(StatechartArea.POINTS);
         messageLabel.setText("To create a state, click on location"); 
       }
       else if (label.equals("OR state"))
       { System.out.println("Creating an OR state");
         drawArea.setDrawMode(StatechartArea.ORSTATE);
         messageLabel.setText("To create an OR state, click on location"); 
       }
       else if (label.equals("Transition"))
       { System.out.println("Creating a transition");
         drawArea.setDrawMode(StatechartArea.SLINES);
         messageLabel.setText("To create a transition, click and drag"); 
       }	
      else if (label.equals("Event"))
      {
        System.out.println("Creating an event");
        drawArea.setDrawMode(StatechartArea.EVENTS);
       }
       else if (label.equals("Attribute"))
       { /* System.out.println("Enter attribute name"); 
         String att = JOptionPane.showInputDialog("Attribute name:"); 
         drawArea.setAttribute(att); 
         String attmax = 
           JOptionPane.showInputDialog("Max value of attribute:"); 
         drawArea.setMaxval(attmax); */
         AttEditDialog aed = new AttEditDialog(this);
         aed.setVisible(true);  
         aed.setSize(200,400); 
         drawArea.addAttribute(aed.getName(),
                               aed.getAttributeType(), aed.getInit());   
       } 
       else if (label.equals("Edit"))
       { System.out.println("Select state or transition to edit"); 
         drawArea.setDrawMode(StatechartArea.EDIT); 
         drawArea.setEditMode(StatechartArea.EDITING); 
       } 
       else if (label.equals("Move"))
       { System.out.println("Select state or transition to move");
         drawArea.setDrawMode(StatechartArea.EDIT);
         drawArea.setEditMode(StatechartArea.MOVING); 
       } 
       else if (label.equals("Glue move"))
       { System.out.println("Select state to move"); 
         drawArea.setDrawMode(StatechartArea.EDIT); 
         drawArea.setEditMode(StatechartArea.GLUEMOVE);
       } 
       else if (label.equals("Resize"))
       { System.out.println("Select state to resize"); 
         drawArea.setDrawMode(StatechartArea.EDIT); 
         drawArea.setEditMode(StatechartArea.RESIZING);
       } 
       else if (label.equals("Delete"))
       { System.out.println("Select state or transition to delete");
         drawArea.setDrawMode(StatechartArea.EDIT);
         drawArea.setEditMode(StatechartArea.DELETING); 
       }
       else if (label.equals("Delete Attribute"))
       { drawArea.module.deleteAttribute(); 
         repaint(); 
       } 
       else if (label.equals("Shrink"))
       { drawArea.shrink(2); 
         repaint(); 
       } 
       else if (label.equals("Set Multiplicity"))
       { System.out.println("Enter the multiplicity of the component in the system"); 
         String multip = JOptionPane.showInputDialog("Multiplicity (2, 3, etc)"); 
         if (multip != null)
         { int mult = Integer.parseInt(multip); 
           drawArea.setMultiplicity(mult); 
         } 
       }
       else if (label.equals("Synthesise B"))
       { drawArea.synthesiseBCode(); } 
       else if (label.equals("Sequential Java"))
       { System.out.println("Synthesising Sequential Java code");
         drawArea.synthesiseJava();
       }  
      else if (label.equals("Check Structure")) 
      { drawArea.checkStructure(); } 
       else if (label.equals("Concurrent Java"))
       { System.out.println("Synthesising Concurrent Java code");
         drawArea.synthesiseConcurrentJava();
       }  
       else if (label.equals("Simulation Loop Java"))
       { drawArea.synthesiseSimulationJava(); } 
       else if (label.equals("Synthesise SMV"))
       { System.out.println("Synthesising SMV");
         drawArea.synthesiseSmv();
       }
       else if (label.equals("Behavioural Compatibility"))
       { // System.out.println("Enter temporal invariant"); 
         // Invariant inv = createTemporalInvariant(); 
         // drawArea.module.verifyInvariant((TemporalInvariant) inv);
         drawArea.module.checkRefinement(); 
       } 
       else if (label.equals("Data slice")) 
       { String stname = JOptionPane.showInputDialog("From state:");
         String att = JOptionPane.showInputDialog("Attribute:");  

         drawArea.slice(stname,att); 
       } 
       else if (label.equals("Input event slice")) 
       { String stname = JOptionPane.showInputDialog("Event to remove:");
  
         drawArea.inputEventSlice(stname); 
       } 
       else if (label.equals("Output event slice")) 
       { String stnames = JOptionPane.showInputDialog("Events to remove:");
  
         StringTokenizer st = 
          new StringTokenizer(stnames); 
         Vector elements = new Vector(); 
         while (st.hasMoreTokens())
         { String se = st.nextToken().trim();
           elements.add(se); 
         }

         drawArea.outputEventSlice(elements); 
       } 
     }
   }

   private void printData()
   { PrinterJob pj = PrinterJob.getPrinterJob();
     pj.setPrintable(drawArea);
     if (pj.printDialog())
     { try { pj.print(); }
       catch (Exception ex) { ex.printStackTrace(); }
     }
   }

  
  public void updateLabel(Point point) {
        messageLabel.setText("Click occurred at coordinate ("
		      + point.x + ","+point.y+").");
  }

  private Invariant createTemporalInvariant()
  { if (tinvDialog == null)
    { tinvDialog = new TempInvEditDialog(this);
      tinvDialog.pack();
      tinvDialog.setLocationRelativeTo(this);
    }
    tinvDialog.setOldFields(new Vector(),"","",true,false);
    tinvDialog.setVisible(true);

    Compiler comp = new Compiler(); 
    Vector op = tinvDialog.getOp();
    String sCond = tinvDialog.getCond();
    String sEffect = tinvDialog.getEffect();
    boolean isSys = tinvDialog.isSystem();
    boolean isCrit = tinvDialog.isCritical();

    if (sCond != null)
    {
      System.out.println("New temporal invariant");

      comp.lexicalanalysis(sCond);
      Expression eCond = comp.parse();
      if (eCond == null)
      { eCond = new BasicExpression("true"); }
      comp.lexicalanalysis(sEffect);
      Expression eEffect = comp.parse();
      if (eEffect == null)
      { eEffect = new BasicExpression("true"); }

      Invariant inv =
        new TemporalInvariant(eCond,eEffect,op);
      inv.setSystem(isSys);
      inv.setCritical(isCrit);
      repaint();
      return inv;
    }
    else
    { repaint();
      System.out.println("Invariant not created");
      return null;
    }
  }

}


