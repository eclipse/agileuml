import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.*;
import java.util.EventObject;
import java.util.Vector;
import java.io.*;

/* Package: Class Diagram */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class SInvEditDialog extends JDialog
{ 
  JPanel bottom;
  JButton okButton, cancelButton;
  SInvDialogPanel dialogPanel;
  
  String defaultEntity = ""; 
  String defaultAssumption = "";
  String defaultConclusion = "";
  boolean defaultisSystem = true;
  boolean defaultisCritical = false;
  boolean defaultisBehaviour = true; 
  boolean defaultOrdering = false; 

  String newEntity; 
  String newAssumption;
  String newConclusion;
  boolean newisSystem;
  boolean newisCritical;
  boolean newisBehaviour; 
  boolean newOrdering; 
   
  SInvEditDialog(JFrame owner)
  { super(owner, "Edit/Create Constraint", true);
    
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
      
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(
      BorderFactory.createEtchedBorder());
    dialogPanel = new SInvDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel,
                         BorderLayout.CENTER); 
    InvSizeHandler sh = new InvSizeHandler(dialogPanel); 
    addComponentListener(sh); 
  }

  public void setOldFields(String e, String a, String cl,
                           boolean isSys, boolean isCrit, boolean isOrd)
  { defaultEntity = e; 
    defaultAssumption = a;
    defaultConclusion = cl;
    defaultisSystem = isSys;
    defaultisCritical = isCrit;
    defaultOrdering = isOrd; 
    dialogPanel.setOldFields(e,a,cl,isSys,isCrit,isOrd); 
  }

  public void setFields(String e, String a, String cl,
                        boolean isSys, boolean isCrit,
                        boolean isBehav, boolean isOrd)
  { newEntity = e; 
    newAssumption = a;
    newConclusion = cl; 
    newisSystem = isSys;
    newisCritical = isCrit;
    newisBehaviour = isBehav;
    newOrdering = isOrd; 
  }
 
  public String getEntity() 
  { return newEntity; }

  public String getAssumption() 
  { return newAssumption; }
  
  public String getConclusion() 
  { return newConclusion; }

  public boolean isSystem()
  { return newisSystem; }

  public boolean isCritical()
  { return newisCritical; }

  public boolean isBehaviour()
  { return newisBehaviour; }

  public boolean isOrdered()
  { return newOrdering; } 

  class ButtonHandler implements ActionListener
  { public void actionPerformed(ActionEvent ev)
    { JButton button = (JButton) ev.getSource();
      String label = button.getText();
      if ("Ok".equals(label))
      { setFields(dialogPanel.getEntity(), dialogPanel.getAssump(),
                  dialogPanel.getConc(),
                  dialogPanel.isSystem(),
                  dialogPanel.isCritical(),
                  dialogPanel.isBehaviour(), dialogPanel.isOrdered()); }
      else
      { setFields(null,null,null,true,false,true,false); }
      dialogPanel.reset();
      setVisible(false); 
    } 
  }

 class InvSizeHandler implements ComponentListener
 { SInvDialogPanel panel; 

   InvSizeHandler(SInvDialogPanel p) 
   { panel = p; } 

   public void componentResized(ComponentEvent e) 
   { panel.resize(); } 

   public void componentMoved(ComponentEvent e) { }
 
   public void componentShown(ComponentEvent e) { }

   public void componentHidden(ComponentEvent e) { }
  } 

}


class SInvDialogPanel extends JPanel
{ JLabel entityLabel;
  JTextField entityField;
  JLabel assumpLabel;
  JTextArea assumpField;
  JLabel concLabel;
  JTextArea concField;

  JPanel envPanel;
  ButtonGroup bg1;
  JCheckBox systemInv;
  JCheckBox environmentInv;

  JPanel critPanel;
  ButtonGroup bg2;
  JCheckBox noncriticalInv;
  JCheckBox criticalInv;

  JPanel behavPanel;
  ButtonGroup bg3;
  JCheckBox behaviourInv;
  JCheckBox preconditionInv;

  JPanel orderPanel;
  ButtonGroup bg4;
  JCheckBox orderInv;
  JCheckBox noorderInv;

  JScrollPane scroller1, scroller2;

  
  SInvDialogPanel()
  { entityLabel = new JLabel("Entity (context):");
    entityField = new JTextField(30);
    entityField.setToolTipText("The class which the constraint operates on, or leave blank if no class"); 

    assumpLabel = new JLabel("Assumption:");
    assumpField = new JTextArea(30,6);
    assumpField.setToolTipText("If assumption is true, the conclusion is (expected to be) true"); 
    assumpField.setLineWrap(true); 
    concLabel = new JLabel("Conclusion:");
    concField = new JTextArea(30,10);
    concField.setLineWrap(true); 
  
    envPanel = new JPanel();
    bg1 = new ButtonGroup();
    systemInv = new JCheckBox("System", true);
    environmentInv = 
      new JCheckBox("Environment");
    bg1.add(systemInv);
    bg1.add(environmentInv);
    envPanel.add(systemInv);
    envPanel.add(environmentInv);
    envPanel.setBorder(
      BorderFactory.createTitledBorder(
        "System Requirement or Environment " + 
        "Assumption?"));
    
    critPanel = new JPanel();
    bg2 = new ButtonGroup();
    noncriticalInv = 
      new JCheckBox("Non-critical", true);
    criticalInv = 
      new JCheckBox("Critical");
    bg2.add(noncriticalInv);
    bg2.add(criticalInv);
    critPanel.add(noncriticalInv);
    critPanel.add(criticalInv);
    critPanel.setBorder(
      BorderFactory.createTitledBorder(
        "Critical (eg: Safety) or " + 
        "Non-critical?"));
    
    behavPanel = new JPanel();
    bg3 = new ButtonGroup();
    behaviourInv = 
      new JCheckBox("Update code", true);
    preconditionInv = 
      new JCheckBox("Preconditions");
    bg3.add(behaviourInv);
    bg3.add(preconditionInv);
    behavPanel.add(behaviourInv);
    behavPanel.add(preconditionInv);
    behavPanel.setBorder(
      BorderFactory.createTitledBorder(
        "Generate update or precondition " + 
        "code?"));

    orderPanel = new JPanel();
    bg4 = new ButtonGroup();
    orderInv = 
      new JCheckBox("Ordered (ascending)");
    noorderInv = 
      new JCheckBox("Unordered", true);
    bg4.add(orderInv);
    bg4.add(noorderInv);
    orderPanel.add(orderInv);
    orderPanel.add(noorderInv);
    orderPanel.setBorder(
      BorderFactory.createTitledBorder(
        "Ordered iteration over entity?"));
    
    setBorder(
       BorderFactory.createTitledBorder(
                       "Constraint on Entity: " +
                       "Assumption => Conclusion"));

    add(entityLabel); 
    add(entityField); 

    add(assumpLabel);
    scroller1 = new JScrollPane(assumpField, 
         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroller1.setPreferredSize(new Dimension(300,65));
    add(scroller1); 
    // add(assumpField); 
    add(concLabel);

    scroller2 = new JScrollPane(concField, 
       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
       JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroller2.setPreferredSize(new Dimension(300,65));
    add(scroller2);

    add(envPanel);
    add(critPanel); 
    add(behavPanel);
    add(orderPanel); 
  }
 
  public void setOldFields(String e, String a, String cl,
                      boolean isSys, boolean isCrit, boolean isOrd)
  { entityField.setText(e); 
    assumpField.setText(a);
    concField.setText(cl); 
    systemInv.setSelected(isSys);
    criticalInv.setSelected(isCrit); 
    orderInv.setSelected(isOrd); 
  }

  public Dimension getPreferredSize()
  { return new Dimension(390,500); }

  public Dimension getMinimumSize()
  { return new Dimension(390,500); }

  public Dimension getMaximumSize()
  { return new Dimension(390,500); }

  public void doLayout()
  { int labelwidth = 80;
    int fieldwidth = 270;

    entityLabel.setBounds(10,30,90,30);
    entityField.setBounds(100,35,270,30);

    assumpLabel.setBounds(10,70,90,30);
    scroller1.setBounds(100,75,270,80);
    concLabel.setBounds(10,160,90,30);
    scroller2.setBounds(100,165,270,80);
    envPanel.setBounds(10,260,360,50);
    critPanel.setBounds(10,320,360,50); 
    behavPanel.setBounds(10,380,360,50);
    orderPanel.setBounds(10,440,360,50); 
  }

  public void resize()
  { int newwidth = getWidth();     

    // entityLabel.setBounds(10,30,90,30);
    // entityField.setBounds(100,35,270,30);

    // assumpLabel.setBounds(10,70,90,30);
    assumpField.setSize(newwidth - 150, 6); 
    scroller1.setBounds(100,75,newwidth - 150,80);
    // concLabel.setBounds(10,160,90,30);
    concField.setSize(newwidth - 150, 10); 
    scroller2.setBounds(100,165,newwidth - 150,80);
    // envPanel.setBounds(10,260,360,50);
    // critPanel.setBounds(10,320,360,50); 
    // behavPanel.setBounds(10,380,360,50);
    // orderPanel.setBounds(10,440,360,50); 
  }


  public void reset()
  { assumpField.setText("");
    concField.setText(""); 
    systemInv.setSelected(true); 
    noncriticalInv.setSelected(true); 
    behaviourInv.setSelected(true); 
    orderInv.setSelected(false); 
  }

  public String getEntity()
  { return entityField.getText(); } 

  public String getAssump()
  { return assumpField.getText(); }

  public String getConc()
  { return concField.getText(); }

  public boolean isSystem()
  { return systemInv.isSelected(); }

  public boolean isCritical()
  { return criticalInv.isSelected(); }

  public boolean isBehaviour()
  { return behaviourInv.isSelected(); }

  public boolean isOrdered()
  { return orderInv.isSelected(); } 
}





















