import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/* Package: GUI */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class AttEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private AttDialogPanel dialogPanel;
  private String defaultName;
  private String defaultType;
  private int defaultKind;
  private String defaultInit = null;
  private boolean defaultUnique = false; 
  private boolean defaultFrozen = false;
  private boolean defaultInstanceScope = true;

  private String newName;
  private String newType;
  private int newKind;
  private String newInit = null;
  private boolean newUnique = false; 
  private boolean newFrozen = false;
  private boolean newInstanceScope = true;

  public AttEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Edit Attribute Properties");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new AttDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); }

  public void setOldFields(String nme, String typ,
                           int kind, String ini, boolean uniq, 
                           boolean froz, boolean instS)
  { defaultName = nme;
    defaultType = typ;
    defaultKind = kind;
    defaultInit = ini; 
    defaultUnique = uniq; 
    defaultFrozen = froz;
    defaultInstanceScope = instS;
    dialogPanel.setOldFields(nme,typ,kind,ini,uniq,froz,
                             instS); 
  }

  public void setFields(String nme, String typ,
                        int kind, String ini, boolean uniq, 
                        boolean froz, boolean instS)
  { newName = nme;
    newType = typ;
    newKind = kind;
    newInit = ini;
    newUnique = uniq; 
    newFrozen = froz;
    newInstanceScope = instS; 
  }

  public String getName() { return newName; }
  public String getAttributeType() { return newType; }
  public int getKind() { return newKind; }
  public String getInit() { return newInit; }
  public boolean getUnique() { return newUnique; } 
  public boolean getFrozen() { return newFrozen; }
  public boolean getInstanceScope() 
  { return newInstanceScope; }

  class AttDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel typeLabel;
    // JTextField typeField;  /* type */
    JComboBox typeCombo; 
    private JLabel initLabel;
    JTextField initField;  /* initial value */
    
    JCheckBox senBox, intBox, actBox, derBox;
    private JPanel kindPanel;
    private ButtonGroup kindGroup;  /* kind */ 

    JCheckBox uniqBox, notuniqBox;
    private JPanel uniqPanel;
    private ButtonGroup uniqGroup;  /* unique */

    JCheckBox frozBox, notfrozBox;
    private JPanel frozenPanel;
    private ButtonGroup frozenGroup;  /* frozen */ 

    JCheckBox instanceBox, classBox;
    private JPanel scopePanel;
    private ButtonGroup scopeGroup;  /* scope */ 

    public AttDialogPanel()
    { nameLabel = new JLabel("Name:");
      nameField = new JTextField();
      typeLabel = new JLabel("Type:");
      // typeField = new JTextField();
      typeCombo = new JComboBox(); 
      typeCombo.addItem("int");     // Integer
      typeCombo.addItem("boolean"); // Boolean
      typeCombo.addItem("String");  // String 
      typeCombo.addItem("double");  // Real
      typeCombo.addItem("long");    // Integer
      typeCombo.setEditable(true); 
    
      initLabel = new JLabel("Initial value:");
      initField = new JTextField();
      
      kindPanel = new JPanel();
      senBox = new JCheckBox("Sensor");
      intBox = new JCheckBox("Internal",true);
      actBox = new JCheckBox("Actuator");
      derBox = new JCheckBox("Derived");
      kindPanel.add(senBox);
      kindPanel.add(intBox);
      kindPanel.add(actBox);
      kindPanel.add(derBox);
      kindPanel.setBorder(
        BorderFactory.createTitledBorder("Kind"));
      kindGroup = new ButtonGroup(); 
      kindGroup.add(senBox);
      kindGroup.add(intBox);
      kindGroup.add(actBox);
      kindGroup.add(derBox);

      frozenPanel = new JPanel();
      frozBox = new JCheckBox("Frozen");
      notfrozBox = new JCheckBox("Modifiable",true);
      frozenPanel.add(frozBox);
      frozenPanel.add(notfrozBox);
      frozenPanel.setBorder(
        BorderFactory.createTitledBorder("Updateability"));
      frozenGroup = new ButtonGroup(); 
      frozenGroup.add(frozBox);
      frozenGroup.add(notfrozBox);
      
      scopePanel = new JPanel();
      classBox = new JCheckBox("Class");
      instanceBox = new JCheckBox("Instance",true);
      scopePanel.add(classBox);
      scopePanel.add(instanceBox);
      scopePanel.setBorder(
        BorderFactory.createTitledBorder("Scope"));
      scopeGroup = new ButtonGroup(); 
      scopeGroup.add(classBox);
      scopeGroup.add(instanceBox);

      uniqPanel = new JPanel();
      uniqBox = new JCheckBox("Unique");
      notuniqBox = new JCheckBox("Not unique",true);
      uniqPanel.add(uniqBox);
      uniqPanel.add(notuniqBox);
      uniqPanel.setBorder(
        BorderFactory.createTitledBorder("Uniqueness"));
      uniqGroup = new ButtonGroup();
      uniqGroup.add(uniqBox);
      uniqGroup.add(notuniqBox);
      
      add(nameLabel);
      add(nameField);
      add(typeLabel);
      add(typeCombo);
      add(initLabel);
      add(initField);

      add(kindPanel); 
      add(frozenPanel);
      add(scopePanel);
      add(uniqPanel); 
    }

  public void setOldFields(String nme, String typ,
                           int kind, String init, boolean uniq, 
                           boolean froz, boolean scpe)
  { nameField.setText(nme);
    // typeField.setText(typ);
    initField.setText(init);

    if (froz)
    { frozBox.setSelected(true); }
    else
    { notfrozBox.setSelected(true); } 

    if (scpe)
    { instanceBox.setSelected(true); }
    else 
    { classBox.setSelected(true); }

    if (uniq)
    { uniqBox.setSelected(true); } 
    else 
    { notuniqBox.setSelected(true); } 

    if (kind == ModelElement.SEN)
    { senBox.setSelected(true); }
    else if (kind == ModelElement.INTERNAL)
    { intBox.setSelected(true); }
    else if (kind == ModelElement.ACT)
    { actBox.setSelected(true); }
    else if (kind == ModelElement.DERIVED)
    { derBox.setSelected(true); }
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,350); }

  public Dimension getMinimumSize()
  { return new Dimension(450,650); }

  public void doLayout()
  { nameLabel.setBounds(10,10,90,30);
    nameField.setBounds(100,15,270,20);
    typeLabel.setBounds(10,40,90,30);
    typeCombo.setBounds(100,45,270,20);
    initLabel.setBounds(10,70,90,30);
    initField.setBounds(100,75,270,20);
    kindPanel.setBounds(10,100,400,50); 
    frozenPanel.setBounds(10,160,400,50); 
    scopePanel.setBounds(10,210,400,50); 
    uniqPanel.setBounds(10,260,400,50); 
  }

  public void reset()
  { nameField.setText("");
    // typeField.setText("");
    initField.setText("");
    notfrozBox.setSelected(true);
    intBox.setSelected(true);
    instanceBox.setSelected(true); 
    notuniqBox.setSelected(true); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { int kind;
       if (dialogPanel.senBox.isSelected())
       { kind = ModelElement.SEN; }
       else if (dialogPanel.actBox.isSelected())
       { kind = ModelElement.ACT; }
       else if (dialogPanel.derBox.isSelected())
       { kind = ModelElement.DERIVED; }
       else 
       { kind = ModelElement.INTERNAL; }
       setFields(dialogPanel.nameField.getText(),
                 (String) dialogPanel.typeCombo.getSelectedItem(),
                 kind,
                 dialogPanel.initField.getText(),
                 dialogPanel.uniqBox.isSelected(), 
                 dialogPanel.frozBox.isSelected(),
                 dialogPanel.instanceBox.isSelected()); 
     }
     else 
     { setFields(null,null,ModelElement.INTERNAL,
                 null,false,false,true); 
     }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




