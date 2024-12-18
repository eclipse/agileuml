import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*; 
import java.util.Vector; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class MessageEditDialog extends JDialog
{ JPanel bottom;
   JButton okButton, cancelButton;
   DialogPanel dialogPanel;

   String defaultName; 
   String defaultT1; 
   String defaultT2; 
   boolean defaultQuantifier = false;  // exists 

   String newName;
   String newT1;
   String newT2;
   boolean newQuantifier;

   public MessageEditDialog(JFrame owner)
   { super(owner, true);
     setTitle("Edit Message Properties");
     okButton = new JButton("Ok");
     cancelButton = new JButton("Cancel");
     ButtonHandler bHandler = new ButtonHandler();
     okButton.addActionListener(bHandler);
     cancelButton.addActionListener(bHandler);
     bottom = new JPanel();
     bottom.add(okButton);
     bottom.add(cancelButton);
     bottom.setBorder(BorderFactory.createEtchedBorder());
     dialogPanel = new DialogPanel();
     getContentPane().setLayout(new BorderLayout());
     getContentPane().add(bottom, BorderLayout.SOUTH);
     getContentPane().add(dialogPanel, BorderLayout.CENTER); }

  public void setOldFields(Vector evs, String t1, 
                                           String t2,boolean quan)
  { // defaultName = ev;
    defaultT1 = t1;
    defaultT2 = t2;
    defaultQuantifier = quan;
    dialogPanel.setOldFields(evs,t1,t2,quan);
  }

  public void setFields(String ev, String t1, String t2,
                                       boolean q)
  { newName = ev;
    newT1 = t1;
    newT2 = t2;
    newQuantifier = q;
  }

  public String getName() { return newName; } 
  public String getT1() { return newT1; } 
  public String getT2() { return newT2; } 
  public boolean getQuantifier() { return newQuantifier; } 

  class DialogPanel extends JPanel
  { JLabel nameLabel;
    JComboBox nameCombo;  /* Trigger event name */
    JLabel t1Label;
    JTextField t1Field;  /* send time annotation */
    JLabel t2Label;
    JTextField t2Field;  /* receive time annotation */
    JCheckBox inBox, outBox;
    JPanel inoutPanel;
    ButtonGroup group; 

  DialogPanel()
  { nameLabel = new JLabel("Operation:");
    nameCombo = new JComboBox();
    t1Label = new JLabel("Send time:");
    t1Field = new JTextField();
    t2Label = new JLabel("Receive time:");
    t2Field = new JTextField();
    inoutPanel = new JPanel();
    inBox = new JCheckBox("<exists>",true);
    outBox = new JCheckBox("<forall>");
    inoutPanel.add(inBox);
    inoutPanel.add(outBox);
    inoutPanel.setBorder(
        BorderFactory.createTitledBorder("Stereotype"));
    group = new ButtonGroup(); 
    group.add(inBox);
    group.add(outBox);

    add(nameLabel);
    add(nameCombo);
    add(t1Label);
    add(t1Field);
    add(t2Label);
    add(t2Field);
    add(inoutPanel);
 }

  public void setOldFields(Vector evs, String t1, String t2,
                                   boolean quan)
  { 
    for (int i = 0; i < evs.size(); i++) 
    { nameCombo.addItem(((ModelElement) evs.get(i)).getName()); }  
    t1Field.setText(t1); 
    t2Field.setText(t2); 
    if (quan)
    { outBox.setSelected(true); } 
    else 
    { inBox.setSelected(true); }
  } 

  public Dimension getPreferredSize()
  { return new Dimension(350,270); }

  public Dimension getMinimumSize()
  { return new Dimension(350,270); }

  public void doLayout()
  { nameLabel.setBounds(10,10,60,30);
     nameCombo.setBounds(70,15,270,20);
     t1Label.setBounds(10,40,60,30);
     t1Field.setBounds(70,45,270,20);
     t2Label.setBounds(10,70,60,30);
     t2Field.setBounds(70,75,270,20);
     inoutPanel.setBounds(10,100,330,50); 
  }

  public void reset()
  { // nameField.setText("");
    t1Field.setText("");
    t2Field.setText("");
     nameCombo.removeAllItems(); 
     inBox.setSelected(true); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
       String label = button.getText();
       if ("Ok".equals(label))
       { setFields((String) dialogPanel.nameCombo.getSelectedItem(),
                   dialogPanel.t1Field.getText(),
                   dialogPanel.t2Field.getText(),
                   dialogPanel.outBox.isSelected()); 
       }
       else 
       { setFields(null,null,null,false); } 
   dialogPanel.reset();
   setVisible(false); } 
 } /* inner class */ 
}

