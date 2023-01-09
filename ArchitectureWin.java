/*
      * Classname : ArchitectureWin
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the (JFrame) window for producing
      * UML Component diagrams. It has its 
      * own menu and tool bar. 
  package: Architecture
*/
/******************************
* Copyright (c) 2003--2023 Kevin Lano
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


public class ArchitectureWin extends JFrame implements ActionListener 
{ private JLabel messageLabel;	 
  private ArchitectureArea drawArea;
  private ListShowDialog listShowDialog; 


  private TempInvEditDialog tinvDialog;
 
  public ArchitectureWin(String s, Vector entities, UCDArea parent) 
  { JMenuBar menuBar;
    JMenu fileMenu, createMenu;
    JMenuItem saveMenu; 
    JMenuItem stateMenu;
    JMenuItem transMenu;
    JMenuItem state2Menu;
    JMenuItem eventMenu;
    
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    setTitle(s); 

    //Add regular components to the window, using the default BorderLayout.
    Container contentPane = getContentPane();

    drawArea = new ArchitectureArea(this,parent,s,entities);	

    //Put the drawing area in a scroll pane.
    JScrollPane state_scroll = new JScrollPane(drawArea, 
				     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    state_scroll.setPreferredSize(new Dimension(500,400));
    contentPane.add(state_scroll, BorderLayout.CENTER);
	
    messageLabel = new JLabel("Click within the framed area.");
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
    // saveMenu.setMnemonic(KeyEvent.VK_S); 
       

    JMenuItem saveDataMI = new JMenuItem("Save data"); 
    saveDataMI.addActionListener(this); 
    fileMenu.add(saveDataMI);
    
    JMenuItem loadMenu = new JMenuItem("Load data"); 
    loadMenu.addActionListener(this);
    fileMenu.add(loadMenu); 
    
    fileMenu.addSeparator();

    JMenuItem saveModelMI = new JMenuItem("Save as model"); 
    saveModelMI.addActionListener(this); 
    fileMenu.add(saveModelMI);

    /* fileMenu.addSeparator();
        

    JMenuItem loadDataRequirementsMI = new JMenuItem("Formalise data reqs"); 
    loadDataRequirementsMI.addActionListener(this); 
    fileMenu.add(loadDataRequirementsMI); */ 

    fileMenu.addSeparator();

    JMenuItem printMI = new JMenuItem("Print");
    printMI.addActionListener(this);
    fileMenu.add(printMI); 

    // VIEW menu heading
    //--------------------
    JMenu viewMenu = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V); 
    // menuBar.add(viewMenu);

    JMenuItem stateList = new JMenuItem("Components");
    stateList.addActionListener(this);
    viewMenu.add(stateList);

    JMenuItem transList = new JMenuItem("Interfaces");
    transList.addActionListener(this);
    viewMenu.add(transList);

    //CREATE menu heading
    //--------------------
    createMenu = new JMenu("Create");
    createMenu.setMnemonic(KeyEvent.VK_C);
    createMenu.getAccessibleContext().setAccessibleDescription(
	     "To create components, interfaces and connectors");
    menuBar.add(createMenu);
    
    //a group of JMenuItems for Create
    stateMenu = new JMenuItem("Component");
    stateMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws a component");
    stateMenu.addActionListener(this);
    createMenu.add(stateMenu);

    transMenu = new JMenuItem("Provided interface");
    createMenu.add(transMenu);
    transMenu.addActionListener(this);

    JMenuItem scenarioMenu = new JMenuItem("Required interface");
    createMenu.add(scenarioMenu);
    scenarioMenu.addActionListener(this);

    // state2Menu = new JMenuItem("Lifeline state");
    // state2Menu.getAccessibleContext().setAccessibleDescription(
    //	  "Draws state as a rounded rectangle");
    // state2Menu.addActionListener(this);
    // createMenu.add(state2Menu);
     
    // JMenuItem state3Menu = new JMenuItem("Execution instance");
    // state3Menu.getAccessibleContext().setAccessibleDescription(
    //		  "Draws execution instance as a rectangle");
    // state3Menu.addActionListener(this);
    // createMenu.add(state3Menu);
    
    // JMenuItem taMenu = new JMenuItem("Time annotation");
    // taMenu.getAccessibleContext().setAccessibleDescription(
    //	  "Draws time annotation at point");
    // taMenu.addActionListener(this);
    // createMenu.add(taMenu);

    // JMenuItem daMenu = new JMenuItem("Duration annotation");
    // daMenu.getAccessibleContext().setAccessibleDescription(
    //	  "Draws duration annotation");
    // daMenu.addActionListener(this);
    // createMenu.add(daMenu);
           

   //EDIT menu heading 
   //--------------------
   JMenu editMenu = new JMenu("Edit");
   editMenu.getAccessibleContext().setAccessibleDescription(
                                           "To edit elements"); 
   menuBar.add(editMenu);
   menuBar.add(viewMenu); 

   JMenuItem editState = new JMenuItem("Edit component");
   JMenuItem editScenario = new JMenuItem("Edit interface");  
   JMenuItem moveElement = new JMenuItem("Move");
   // JMenuItem gluemoveMI = new JMenuItem("Glue move");  
   // JMenuItem resizeMI = new JMenuItem("Resize");  

   JMenuItem deleteElement = new JMenuItem("Delete"); 
   JMenuItem shrinkMI = new JMenuItem("Shrink"); 

   editState.addActionListener(this); 
   editScenario.addActionListener(this); 
   moveElement.addActionListener(this);
   // gluemoveMI.addActionListener(this); 
   // resizeMI.addActionListener(this); 
   deleteElement.addActionListener(this);
   shrinkMI.addActionListener(this);

   editMenu.add(editState); 
   editMenu.add(editScenario); 
   editMenu.add(moveElement);
   // editMenu.add(gluemoveMI);  
   // editMenu.add(resizeMI);  
   editMenu.add(deleteElement); 
   editMenu.add(shrinkMI); 

   editMenu.addSeparator(); 

   // Synthesis MENU
   JMenu synthMenu = new JMenu("Synthesis"); 
   menuBar.add(synthMenu); 

   JMenuItem ralSynthesis = new JMenuItem("Derive Java");
   ralSynthesis.addActionListener(this); 
   synthMenu.add(ralSynthesis); 

   // JMenuItem rtlSynthesis = new JMenuItem("RTL");
   // rtlSynthesis.addActionListener(this); 
   // synthMenu.add(rtlSynthesis); 
  }

  public ArchitectureArea getDrawArea() 
  { return drawArea; } 

  

  public void actionPerformed(ActionEvent e)
  {
    Object eventSource = e.getSource();
    if (eventSource instanceof JMenuItem)
    {
      String label = (String) e.getActionCommand();
      if (label.equals("Save data")) 
      { drawArea.saveDataToFile("architecture.txt"); 
        messageLabel.setText("Saved data to architecture.txt"); 
      } 
      else if (label.equals("Load data")) 
      { drawArea.loadDataFromFile("out.dat");
        messageLabel.setText("Loaded data from architecture.txt"); 
      } 
      if (label.equals("Save as model")) 
      { drawArea.saveModelDataToFile("archmodel.txt"); 
        messageLabel.setText("Saved data to archmodel.txt"); 
      } 
      else if (label.equals("Components"))
      { System.out.println(">>> List of components");
        System.out.println(">>> ------------------");
        drawArea.updateModel(); 
        drawArea.disp_States();
      }
      else if (label.equals("Interfaces"))
      { System.out.println(">>> List of interfaces");
        System.out.println(">>> ------------------");
        drawArea.disp_Trans();
      }
      else if (label.equals("Print"))
      { printData(); } 
      else if (label.equals("Derive Java"))
      {
        drawArea.synthesiseJava();
      }
      else if (label.equals("Component"))
      { System.out.println("Creating a component: click on location");
        drawArea.setDrawMode(RequirementsArea.POINTS);
        messageLabel.setText("Click on location"); 
      }
      else if (label.equals("Required interface"))
      { System.out.println("Click and drag from a component to create a required interface");
        drawArea.setDrawMode(InteractionArea.DLINES);
        messageLabel.setText("Click and drag from a component to create a required interface"); 
      }
      else if (label.equals("Provided interface"))
      { System.out.println("Creating a provided interface of a component:\n" + 
          "Click and drag from component to interface location");
        drawArea.setDrawMode(RequirementsArea.SLINES);
        messageLabel.setText("Click and drag from component to interface location"); 
       }	
       else if (label.equals("Edit component"))
       { System.out.println("Select component to edit"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.EDITING); 
         messageLabel.setText("Click within the component");
       } 
       else if (label.equals("Edit interface"))
       { System.out.println("Select interface to edit"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.EDITING);
         messageLabel.setText("Click near centre of line");
         // editScenario(); 
       } 
       else if (label.equals("Move"))
       { System.out.println("Select component or interface to move");
         drawArea.setDrawMode(InteractionArea.EDIT);
         drawArea.setEditMode(InteractionArea.MOVING); 
         messageLabel.setText("Click on component or interface");
       } 
       else if (label.equals("Delete"))
       { System.out.println("Select component/interface or connection to delete");
         drawArea.setDrawMode(InteractionArea.EDIT);
         drawArea.setEditMode(InteractionArea.DELETING); 
       }
       else if (label.equals("Shrink"))
       { drawArea.shrink(1.2); 
         repaint(); 
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

  public static void main(String[] args)
  { ArchitectureWin window = new ArchitectureWin("Architecture editor",null,null);  
    window.setSize(500, 400);
    window.setVisible(true);   
  }

}








