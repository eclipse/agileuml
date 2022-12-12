import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/* Package: ClassDiagram GUI */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class AstEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private AstDialogPanel dialogPanel;
  private String defaultName;
  private String defaultRole1;
  private String defaultRole2;
  private String defaultCard1;
  private String defaultCard2;
  private boolean defaultOrdered = false; 
  private boolean defaultSorted = false; 
  private boolean defaultFrozen = false;
  private boolean defaultAddOnly = false;
  private String defaultStereotypes = ""; 

  private String newName;
  private String newRole1; 
  private String newRole2;
  private String newCard1;
  private String newCard2;
  private boolean newOrdered = false; 
  private boolean newSorted = false; 
  private boolean newFrozen = false;
  private boolean newAddOnly = false;
  private String newStereotypes = ""; 
  private boolean cancel = false; 

  public AstEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Edit Association Properties");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new AstDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); }

  public void setOldFields(String nme, String role1, String role2,
                           String card1, String card2, boolean ordered, 
                           boolean froz, boolean addOnly)
  { defaultName = nme;
    defaultRole1 = role1; 
    defaultRole2 = role2;
    defaultCard1 = card1;
    defaultCard2 = card2; 
    defaultOrdered = ordered; 
    defaultFrozen = froz;
    defaultAddOnly = addOnly;
    dialogPanel.setOldFields(nme,role1,role2,card1,card2,ordered,froz,
                             addOnly); 
  }

  public void setFields(String nme, String role1, String role2,
                        String card1, String card2, boolean ordered, 
                        boolean froz, boolean addOnly, String stereo)
  { newName = nme;
    newRole1 = role1; 
    newRole2 = role2;
    newCard1 = card1;
    newCard2 = card2;
    newOrdered = ordered; 
    newFrozen = froz;
    newAddOnly = addOnly;
    newStereotypes = stereo;  
  }

  public void setCancel(boolean c)
  { cancel = c; } 


  public String getName() { return newName; }
  public String getRole1() { return newRole1; }
  public String getRole2() { return newRole2; }
  public String getCard1() { return newCard1; }
  public String getCard2() { return newCard2; }
  public boolean getOrdered() { return newOrdered; } 
  public boolean getFrozen() { return newFrozen; }
  public boolean getAddOnly() { return newAddOnly; }
  public String getStereotypes() { return newStereotypes; } 
  public boolean isCancel() { return cancel; } 

  class AstDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel role1Label;
    JTextField role1Field;  
    private JLabel role2Label;
    JTextField role2Field;  
    private JLabel card1Label;
    // JTextField card1Field;
    JComboBox card1Box; 
    private JLabel card2Label;
    // JTextField card2Field;
    JComboBox card2Box; 
    
    JCheckBox unorderedBox, orderedBox, sortedascBox; // , sorteddescBox;
    private JPanel orderedPanel;
    private ButtonGroup orderedGroup;  /* order */ 

    JCheckBox frozBox, notfrozBox;
    private JPanel frozenPanel;
    private ButtonGroup frozenGroup;  /* frozen */ 

    JCheckBox addonlyBox, notaddonlyBox;
    private JPanel addonlyPanel;
    private ButtonGroup addonlyGroup;  /* add only */ 

    private JLabel stereoLabel; 
    JComboBox stereoBox; 


    public AstDialogPanel()
    { nameLabel = new JLabel("Name:");
      nameField = new JTextField();
      role1Label = new JLabel("Role1:");
      role1Field = new JTextField();
      role2Label = new JLabel("Role2:");
      role2Field = new JTextField();
      card1Label = new JLabel("Card1:");
      card1Box = new JComboBox();
      card1Box.addItem("1");
      card1Box.addItem("*");
      card1Box.addItem("0..1"); 
      card1Box.addItem("aggregation 1"); 
      card1Box.addItem("aggregation 0..1"); 
      card1Box.addItem("qualifier"); 
      card2Label = new JLabel("Card2:");
      card2Box = new JComboBox();
      card2Box.addItem("1");
      card2Box.addItem("*");
      card2Box.addItem("0..1"); 
      
      orderedPanel = new JPanel();
      unorderedBox = new JCheckBox("Unordered",true);
      orderedBox = new JCheckBox("Ordered");
      sortedascBox = new JCheckBox("Sorted <");
      // sorteddescBox = new JCheckBox("Sorted >");
      // sortedascBox.setEnabled(false); 
      // sorteddescBox.setEnabled(false); 
      orderedPanel.add(unorderedBox);
      orderedPanel.add(orderedBox);
      orderedPanel.add(sortedascBox);
      // orderedPanel.add(sorteddescBox);
      orderedPanel.setBorder(
        BorderFactory.createTitledBorder("Ordering of role2"));
      orderedGroup = new ButtonGroup(); 
      orderedGroup.add(unorderedBox);
      orderedGroup.add(orderedBox);
      orderedGroup.add(sortedascBox);
      // orderedGroup.add(sorteddescBox);

      frozenPanel = new JPanel();
      frozBox = new JCheckBox("Frozen");
      notfrozBox = new JCheckBox("Modifiable",true);
      frozenPanel.add(frozBox);
      frozenPanel.add(notfrozBox);
      frozenPanel.setBorder(
        BorderFactory.createTitledBorder("Updateability of role2"));
      frozenGroup = new ButtonGroup(); 
      frozenGroup.add(frozBox);
      frozenGroup.add(notfrozBox);
      
      addonlyPanel = new JPanel();
      notaddonlyBox = new JCheckBox("Not add only",true);
      addonlyBox = new JCheckBox("Add only");
      addonlyPanel.add(notaddonlyBox);
      addonlyPanel.add(addonlyBox);
      addonlyPanel.setBorder(
        BorderFactory.createTitledBorder("role2 add only"));
      addonlyGroup = new ButtonGroup(); 
      addonlyGroup.add(notaddonlyBox);
      addonlyGroup.add(addonlyBox);

      stereoLabel = new JLabel("Stereotypes:"); 
      stereoBox = new JComboBox(); 
      stereoBox.setEditable(true); 
      stereoBox.addItem("none"); 
      stereoBox.addItem("persistent"); 
      stereoBox.addItem("explicit"); 
      stereoBox.addItem("implicit"); 
      stereoBox.addItem("derived"); 
      stereoBox.addItem("source"); 
      stereoBox.addItem("target"); 
      stereoBox.addItem("auxiliary"); 

      add(nameLabel);
      add(nameField);
      add(role1Label);
      add(role1Field);
      add(role2Label);
      add(role2Field);
      add(card1Label);
      add(card1Box);
      add(card2Label);
      add(card2Box);

      add(orderedPanel); 
      add(frozenPanel);
      add(addonlyPanel);
      add(stereoLabel); 
      add(stereoBox); 
    }

  public void setOldFields(String nme, String role1, String role2,
                           String card1, String card2, boolean ordered, 
                           boolean froz, boolean addonly)
  { nameField.setText(nme);
    role1Field.setText(role1);
    role2Field.setText(role2);
    card1Box.setSelectedItem(card1);
    card2Box.setSelectedItem(card2); 
    if (froz)
    { frozBox.setSelected(true); }
    else
    { notfrozBox.setSelected(true); } 

    if (ordered)
    { orderedBox.setSelected(true); }
    else 
    { unorderedBox.setSelected(true); }

    if (addonly)
    { addonlyBox.setSelected(true); } 
    else 
    { notaddonlyBox.setSelected(true); } 
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,380); }

  public Dimension getMinimumSize()
  { return new Dimension(450,380); }

  public void doLayout()
  { nameLabel.setBounds(10,10,90,30);
    nameField.setBounds(100,15,270,20);
    role1Label.setBounds(10,40,90,30);
    role1Field.setBounds(100,45,270,20);

    role2Label.setBounds(10,70,90,30);
    role2Field.setBounds(100,75,270,20);
    card1Label.setBounds(10,100,90,30);
    card1Box.setBounds(100,105,270,20);
    card2Label.setBounds(10,130,90,30);
    card2Box.setBounds(100,135,270,20);
    orderedPanel.setBounds(10,190,400,50); 
    frozenPanel.setBounds(10,240,400,50); 
    addonlyPanel.setBounds(10,290,400,50); 
    stereoLabel.setBounds(10,340,90,30); 
    stereoBox.setBounds(100,345,270,20); 
  }

  public void reset()
  { nameField.setText("");
    role1Field.setText("");
    role2Field.setText("");
    card1Box.setSelectedItem("*");
    card2Box.setSelectedItem("*"); 
    notfrozBox.setSelected(true);
    unorderedBox.setSelected(true);
    notaddonlyBox.setSelected(true); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 dialogPanel.role1Field.getText(),
                 dialogPanel.role2Field.getText(),
                 (String) dialogPanel.card1Box.getSelectedItem(),
                 (String) dialogPanel.card2Box.getSelectedItem(),
                 dialogPanel.orderedBox.isSelected(), 
                 dialogPanel.frozBox.isSelected(),
                 dialogPanel.addonlyBox.isSelected(),
                 (String) dialogPanel.stereoBox.getSelectedItem()); 
       setCancel(false); 
     }
     else 
     { setFields(null,null,null,null,
                 null,false,false,false,null); 
       setCancel(true); 
     }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




