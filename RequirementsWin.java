/*
      * Classname : RequirementsWin
      * 
      * Version information : 1
      *
      * Date :
      * 
      * Description : This class describes the (JFrame) window for producing
      * SySML. It has its 
      * own menu and tool bar. 
  package: Requirements
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


public class RequirementsWin extends JFrame implements ActionListener 
{ private JLabel messageLabel;	 
  private RequirementsArea drawArea;
  private ListShowDialog listShowDialog; 


  private TempInvEditDialog tinvDialog;
 
  public RequirementsWin(String s, Vector entities, UCDArea parent) 
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

    drawArea = new RequirementsArea(this,parent,s,entities);	

    //Put the drawing area in a scroll pane.
    JScrollPane state_scroll = new JScrollPane(drawArea, 
				     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				     JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    state_scroll.setPreferredSize(new Dimension(1000,2000));
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
        
    JMenuItem printMI = new JMenuItem("Print");
    printMI.addActionListener(this);
    fileMenu.add(printMI); 

    // VIEW menu heading
    //--------------------
    JMenu viewMenu = new JMenu("View");
    viewMenu.setMnemonic(KeyEvent.VK_V); 
    // menuBar.add(viewMenu);

    JMenuItem stateList = new JMenuItem("Requirements");
    stateList.addActionListener(this);
    viewMenu.add(stateList);

    // JMenuItem transList = new JMenuItem("Messages");
    // transList.addActionListener(this);
    // viewMenu.add(transList);

    // JMenuItem eventList = new JMenuItem("States");
    // eventList.addActionListener(this);
    // viewMenu.add(eventList);

    // JMenuItem attList = new JMenuItem("Executions");
    // attList.addActionListener(this);
    // viewMenu.add(attList);

    // JMenuItem taList = new JMenuItem("Time annotations");
    // taList.addActionListener(this);
    // viewMenu.add(taList);

    // JMenuItem daList = new JMenuItem("Duration annotations");
    // daList.addActionListener(this);
    // viewMenu.add(daList);

    //CREATE menu heading
    //--------------------
    createMenu = new JMenu("Create");
    createMenu.setMnemonic(KeyEvent.VK_C);
    createMenu.getAccessibleContext().setAccessibleDescription(
	     "To create requirements and subgoals");
    menuBar.add(createMenu);
    
    //a group of JMenuItems for Create
    stateMenu = new JMenuItem("Requirement");
    stateMenu.getAccessibleContext().setAccessibleDescription(
	  "Draws a requirement");
    stateMenu.addActionListener(this);
    createMenu.add(stateMenu);

    transMenu = new JMenuItem("Subgoal relation");
    createMenu.add(transMenu);
    transMenu.addActionListener(this);

    JMenuItem scenarioMenu = new JMenuItem("Scenario");
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
                                           "To edit requirements"); 
   menuBar.add(editMenu);
   menuBar.add(viewMenu); 

   JMenuItem editState = new JMenuItem("Edit requirement");
   JMenuItem editScenario = new JMenuItem("Edit scenario");  
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

   JMenuItem ralSynthesis = new JMenuItem("Derive use cases");
   ralSynthesis.addActionListener(this); 
   synthMenu.add(ralSynthesis); 

   // JMenuItem rtlSynthesis = new JMenuItem("RTL");
   // rtlSynthesis.addActionListener(this); 
   // synthMenu.add(rtlSynthesis); 
  }

  public RequirementsArea getDrawArea() 
  { return drawArea; } 

  

  public void actionPerformed(ActionEvent e)
  {
    Object eventSource = e.getSource();
    if (eventSource instanceof JMenuItem)
    {
      String label = (String) e.getActionCommand();
      if (label.equals("Save data")) 
      { drawArea.saveDataToFile("requirements.txt"); 
        messageLabel.setText("Saved data to requirements.txt"); 
      } 
      else if (label.equals("Load data")) 
      { drawArea.loadDataFromFile("out.dat");
        messageLabel.setText("Loaded data from requirements.txt"); 
      } 
      else if (label.equals("Requirements"))
      { System.out.println("List of requirements");
        System.out.println("---------------");
        drawArea.disp_States();
       }
       else if (label.equals("Print"))
       { printData(); } 
       else if (label.equals("Derive use cases"))
       {
        drawArea.synthesiseUseCases();
       }
       else if (label.equals("Requirement"))
       { System.out.println("Creating a requirement");
         drawArea.setDrawMode(RequirementsArea.POINTS);
         messageLabel.setText("Click on location"); 
       }
       else if (label.equals("Scenario"))
       { System.out.println("Creating a scenario");
         drawArea.setDrawMode(InteractionArea.TANN);
         messageLabel.setText("To create a scenario, click on its requirement"); 
       }
       else if (label.equals("Subgoal relation"))
       { System.out.println("Creating a subgoal relation");
         drawArea.setDrawMode(RequirementsArea.SLINES);
         messageLabel.setText("To create a relation, click and drag"); 
       }	
       else if (label.equals("Edit requirement"))
       { System.out.println("Select requirement to edit"); 
         drawArea.setDrawMode(InteractionArea.EDIT); 
         drawArea.setEditMode(InteractionArea.EDITING); 
       } 
       else if (label.equals("Edit scenario"))
       { System.out.println("Select scenario to edit"); 
         editScenario(); 
       } 
       else if (label.equals("Move"))
       { System.out.println("Select state or transition to move");
         drawArea.setDrawMode(InteractionArea.EDIT);
         drawArea.setEditMode(InteractionArea.MOVING); 
       } 
       else if (label.equals("Delete"))
       { System.out.println("Select state or transition to delete");
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

  private void editScenario()
  { if (listShowDialog == null)
    { listShowDialog = new ListShowDialog(this);
      listShowDialog.pack();
      listShowDialog.setLocationRelativeTo(this); 
    }
    listShowDialog.setOldFields(drawArea.getScenarios()); 
    messageLabel.setText("Select scenario to edit"); 
    System.out.println("Select scenario to edit");

    listShowDialog.setVisible(true); 

    Object[] vals = listShowDialog.getSelectedValues();
    if (vals != null && vals.length > 0)
    { for (int i = 0; i < vals.length; i++) 
      { // System.out.println(vals[i]);
        if (vals[i] instanceof Scenario)
        { Scenario sc = (Scenario) vals[i]; 
          drawArea.editScenario(sc); 
        } 
        else 
        { System.out.println(vals[i] + " is not a scenario"); }
      } 
    } 
  }

  public static void main(String[] args)
  { RequirementsWin window = new RequirementsWin("Requirements editor",null,null);  
    window.setSize(500, 400);
    window.setVisible(true);   
  }

}








