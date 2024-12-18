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

class ExecutionEditDialog extends JDialog
{ JPanel bottom;
  JButton okButton, cancelButton;
  DialogPanel dialogPanel;

  String defaultName; 
  String defaultT1; 
  String defaultT2; 
  String messageId;  

   String newName;
   String newT1;
   String newT2;
   String newMessageId;

   public ExecutionEditDialog(JFrame owner)
   { super(owner, true);
     setTitle("Edit Execution Instance Properties");
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
                                           String t2,String mid)
  { // defaultName = ev;
    defaultT1 = t1;
    defaultT2 = t2;
    messageId = mid;
    dialogPanel.setOldFields(evs,t1,t2,mid);
  }

  public void setFields(String ev, String t1, String t2,
                                   String mid)
  { newName = ev;
    newT1 = t1;
    newT2 = t2;
    newMessageId = mid;
  }

  public String getName() { return newName; } 
  public String getT1() { return newT1; } 
  public String getT2() { return newT2; } 
  public String getMessageId() { return messageId; } 

  class DialogPanel extends JPanel
  { JLabel nameLabel;
    JComboBox nameCombo;  /* Trigger event name */
    JLabel t1Label;
    JTextField t1Field;  /* start time annotation */
    JLabel t2Label;
    JTextField t2Field;  /* end time annotation */
    JLabel midLabel;
    JTextField midField;  /* message instance */

  DialogPanel()
  { nameLabel = new JLabel("Operation:");
    nameCombo = new JComboBox();
    t1Label = new JLabel("Start time:");
    t1Field = new JTextField();
    t2Label = new JLabel("End time:");
    t2Field = new JTextField();
    midLabel = new JLabel("Message id:");
    midField = new JTextField();
    
    add(nameLabel);
    add(nameCombo);
    add(t1Label);
    add(t1Field);
    add(t2Label);
    add(t2Field);
    add(midLabel);
    add(midField);
 }

  public void setOldFields(Vector evs, String t1, String t2,
                           String mid)
  { 
    for (int i = 0; i < evs.size(); i++) 
    { nameCombo.addItem(((Named) evs.get(i)).label); }  
    t1Field.setText(t1); 
    t2Field.setText(t2); 
    midField.setText(mid); 
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
     midLabel.setBounds(10,100,60,30); 
     midField.setBounds(70,120,270,20); 
  }

  public void reset()
  { // nameField.setText("");
    t1Field.setText("");
    t2Field.setText("");
    nameCombo.removeAllItems(); 
    midField.setText(""); 
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
                   dialogPanel.midField.getText()); 
       }
       else 
       { setFields(null,null,null,null); } 
   dialogPanel.reset();
   setVisible(false); } 
 } /* inner class */ 
}

