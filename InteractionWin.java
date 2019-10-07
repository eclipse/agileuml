/*
      * Classname : InteractionWin
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the (JFrame) window for producing
      * the interaction diagram. It has its 
      * own menu and tool bar. 

  package: Interaction editor
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


public class InteractionWin extends JFrame implements ActionListener 
{ private JLabel messageLabel;	 
  private InteractionArea drawArea;


  private TempInvEditDialog tinvDialog;
 
  public InteractionWin(String s, Vector entities) 
  { JMenuBar menuBar;
    JMenu fileMenu, createMenu;
    JMenuItem saveMenu; 
    JMenuItem stateMenu;
    JMenuItem transMenu;
    JMenuItem state2Menu;
    JMenuItem eventMenu;
    
    setDefaultCloseOperation(HIDE_ON_CLOSE);

    //Add regular components to the window, using the default BorderLayout.
    Container contentPane = getContentPane();

    drawArea = new InteractionArea(this,s,entities);	

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

    // VIEW menu heading
    //--------------------
    JMenu viewMenu = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V); 
    // menuBar.add(viewMenu);

    JMenuItem stateList = new JMenuItem("Lifelines");
    stateList.addActionListener(this);
    viewMenu.add(stateList);

    JMenuItem transList = new JMenuItem("Messages");
    transList.addActionListener(this);
    viewMenu.add(transList);

    JMenuItem eventList = new JMenuItem("States");
    eventList.addActionListener(this);
    viewMenu.add(eventList);

    JMenuItem attList = new JMenuItem("Executions");
    attList.addActionListener(this);
    viewMenu.add(attList);

    JMenuItem taList = new JMenuItem("Time annotations");
    taList.addActionListener(this);
    viewMenu.add(taList);

    JMenuItem daList = new JMenuItem("Duration annotations");
    daList.addActionListener(this);
    viewMenu.add(daList);

    //CREATE menu heading
    //--------------------
    createMenu = new JMenu("Create");
    createMenu.setMnemonic(KeyEvent.VK_C);
    createMenu.getAccessibleContext().setAccessibleDescription(
	     "To create lifelines and messages");
    menuBar.add(createMenu);
    
    //a group of JMenuItems for Create
    stateMenu = new JMenuItem("Lifeline");
    stateMenu.setMnemonic(KeyEvent.VK_L); 
    stateMenu.setAccelerator(KeyStroke.getKeyStroke(
       KeyEvent.VK_L, ActionEvent.ALT_MASK));
    stateMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws a lifeline");
    stateMenu.addActionListener(this);
    createMenu.add(stateMenu);

    transMenu = new JMenuItem("Message");
    transMenu.setMnemonic(KeyEvent.VK_M); 
    transMenu.setAccelerator(KeyStroke.getKeyStroke(
       KeyEvent.VK_M, ActionEvent.ALT_MASK));
    createMenu.add(transMenu);
    transMenu.addActionListener(this);

    state2Menu = new JMenuItem("Lifeline state");
    state2Menu.getAccessibleContext().setAccessibleDescription(
	  "Draws state as a rounded rectangle");
    state2Menu.addActionListener(this);
    createMenu.add(state2Menu);
     
    JMenuItem state3Menu = new JMenuItem("Execution instance");
    state3Menu.getAccessibleContext().setAccessibleDescription(
	  "Draws execution instance as a rectangle");
    state3Menu.addActionListener(this);
    createMenu.add(state3Menu);
    
    JMenuItem taMenu = new JMenuItem("Time annotation");
    taMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws time annotation at point");
    taMenu.addActionListener(this);
    createMenu.add(taMenu);

    JMenuItem daMenu = new JMenuItem("Duration annotation");
    daMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws duration annotation");
    daMenu.addActionListener(this);
    createMenu.add(daMenu);
           

   //EDIT menu heading 
   //--------------------
   JMenu editMenu = new JMenu("Edit");
   editMenu.getAccessibleContext().setAccessibleDescription(
                                           "To edit states and transitions"); 
   menuBar.add(editMenu);
   menuBar.add(viewMenu); 

   JMenuItem editState = new JMenuItem("Edit"); 
   JMenuItem moveElement = new JMenuItem("Move");
   JMenuItem gluemoveMI = new JMenuItem("Glue move");  
   JMenuItem resizeMI = new JMenuItem("Resize");  

   JMenuItem deleteElement = new JMenuItem("Delete"); 
   JMenuItem shrinkMI = new JMenuItem("Shrink"); 

   editState.addActionListener(this); 
   moveElement.addActionListener(this);
   gluemoveMI.addActionListener(this); 
   resizeMI.addActionListener(this); 
   deleteElement.addActionListener(this);
   shrinkMI.addActionListener(this);

   editMenu.add(editState); 
   editMenu.add(moveElement);
   editMenu.add(gluemoveMI);  
   editMenu.add(resizeMI);  
   editMenu.add(deleteElement); 
   editMenu.add(shrinkMI); 

   editMenu.addSeparator(); 

   // Synthesis MENU
   JMenu synthMenu = new JMenu("Synthesis"); 
   menuBar.add(synthMenu); 

   JMenuItem ralSynthesis = new JMenuItem("RAL");
   ralSynthesis.addActionListener(this); 
   synthMenu.add(ralSynthesis); 

   JMenuItem rtlSynthesis = new JMenuItem("RTL");
   rtlSynthesis.addActionListener(this); 
   synthMenu.add(rtlSynthesis); 
  }

  public InteractionArea getDrawArea() 
  { return drawArea; } 

  

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

        /* File startingpoint = new File("output"); 
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(startingpoint); 
        fc.setDialogTitle("Save Java code"); 
        int returnVal = fc.showSaveDialog(this); 
        if (returnVal == JFileChooser.APPROVE_OPTION)
        { File file = fc.getSelectedFile();
          drawArea.saveJavaToFile(file);
          messageLabel.setText("Saved Java code"); 
        } */ 
      }
      else if (label.equals("Save B"))
      { System.out.println("Saving B code of module");
       /* File startingpoint = new File("output"); 
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(startingpoint); 
        fc.setDialogTitle("Save B code"); 
        int returnVal = fc.showSaveDialog(this); 
        if (returnVal == JFileChooser.APPROVE_OPTION)
        { File file = fc.getSelectedFile();
          drawArea.saveBToFile(file);
          messageLabel.setText("Saved B code"); 
        } */ 
      }
      else if (label.equals("Save data")) 
      { drawArea.saveDataToFile("out.dat"); 
        messageLabel.setText("Saved data to out.dat"); 
      } 
      else if (label.equals("Load data")) 
      { drawArea.loadDataFromFile("out.dat");
        messageLabel.setText("Loaded data from out.dat"); 
      } 
      else if (label.equals("Lifelines"))
      { System.out.println("List of lifelines");
        System.out.println("---------------");
        drawArea.disp_States();
       }
       else if (label.equals("Print"))
       { printData(); } 
       else if (label.equals("Messages"))
       {
        System.out.println("List of Messages");
        System.out.println("-------------------");
        drawArea.disp_Trans();
       }
       else if (label.equals("States"))
       {
        System.out.println("List of States");
        System.out.println("-------------------");
        drawArea.dispLStates();
       }
       else if (label.equals("Executions"))
       {
        System.out.println("List of Executions");
        System.out.println("-------------------");
        drawArea.dispExecutions();
       }
       else if (label.equals("Time annotations"))
       {
        System.out.println("List of time annotations");
        System.out.println("-------------------");
        drawArea.dispTimeAnnotations();
       }
       else if (label.equals("Duration annotations"))
       {
        System.out.println("List of duration annotations");
        System.out.println("-------------------");
        drawArea.dispDurationAnnotations();
       }
       else if (label.equals("Lifeline"))
       { System.out.println("Creating a lifeline");
         drawArea.setDrawMode(InteractionArea.POINTS);
         messageLabel.setText("Click on start location and drag to end"); 
       }
       else if (label.equals("Lifeline state"))
       { System.out.println("Creating a state");
         drawArea.setDrawMode(InteractionArea.LSTATE);
         messageLabel.setText("To create a lifeline state, click on location"); 
       }
       else if (label.equals("Message"))
       { System.out.println("Creating a message");
         drawArea.setDrawMode(InteractionArea.SLINES);
         messageLabel.setText("To create a message, click and drag"); 
       }	
       else if (label.equals("Time annotation"))
       { System.out.println("Creating a time annotation");
         drawArea.setDrawMode(InteractionArea.TANN);
         messageLabel.setText("Click on start location and drag to end"); 
       }
       else if (label.equals("Duration annotation"))
       { System.out.println("Creating a duration annotation");
         drawArea.setDrawMode(InteractionArea.DANN);
         messageLabel.setText("Click on start location and drag to end"); 
       }
      else if (label.equals("Event"))
      {
        System.out.println("Creating an event");
        drawArea.setDrawMode(InteractionArea.EVENTS);
       }
       else if (label.equals("Execution instance"))
       { System.out.println("Creating an execution instance");
         drawArea.setDrawMode(InteractionArea.EXECOCC);
         messageLabel.setText("To create an execution, click and drag"); 
       } 
       else if (label.equals("Edit"))
       { System.out.println("Select state or transition to edit"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.EDITING); 
       } 
       else if (label.equals("Move"))
       { System.out.println("Select state or transition to move");
         drawArea.setDrawMode(InteractionArea.EDIT);
         drawArea.setEditMode(InteractionArea.MOVING); 
       } 
       else if (label.equals("Glue move"))
       { System.out.println("Select state to move"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.GLUEMOVE);
       } 
       else if (label.equals("Resize"))
       { System.out.println("Select state to resize"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.RESIZING);
       } 
       else if (label.equals("Delete"))
       { System.out.println("Select state or transition to delete");
         drawArea.setDrawMode(InteractionArea.EDIT);
         drawArea.setEditMode(InteractionArea.DELETING); 
       }
       else if (label.equals("Delete Attribute"))
       { repaint(); 
       } 
       else if (label.equals("Shrink"))
       { drawArea.shrink(2); 
         repaint(); 
       } 
       else if (label.equals("Set Multiplicity"))
       {  
       }
       else if (label.equals("Synthesise B"))
       {  } 
       else if (label.equals("Sequential Java"))
       { 
       }  
       else if (label.equals("Concurrent Java"))
       {       }  
       else if (label.equals("Simulation Loop Java"))
       {  } 
       else if (label.equals("RAL"))
       { drawArea.generateRAL(); }
       else if (label.equals("RTL"))
       {  } 
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

  public static void main(String[] args)
  { InteractionWin window = new InteractionWin("Interaction editor",null);  
    window.setSize(500, 400);
    window.setVisible(true);   
  }

}








