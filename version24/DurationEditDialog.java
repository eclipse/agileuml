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

class DurationEditDialog extends JDialog
{ JPanel bottom;
   JButton okButton, cancelButton;
   DialogPanel dialogPanel;

   String defaultName; 
   String defaultT1; 
   String defaultT2; 

   String newName;
   String newT1;
   String newT2;
   String newTA1;
   String newTA2;

   public DurationEditDialog(JFrame owner)
   { super(owner, true);
     setTitle("Edit Duration Properties");
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

  public void setOldFields(Vector t1s, Vector t2s, String t1, 
                                           String t2)
  { // defaultName = ev;
    defaultT1 = t1;
    defaultT2 = t2;
    dialogPanel.setOldFields(t1s,t2s,t1,t2);
  }

  public void setFields(String ta1, String ta2, String t1, String t2)
  { newTA1 = ta1;
    newTA2 = ta2; 
    newT1 = t1;
    newT2 = t2;
  }

  public String getName() { return newName; } 
  public String getT1() { return newT1; } 
  public String getT2() { return newT2; } 
  public String getTA1() { return newTA1; } 
  public String getTA2() { return newTA2; } 

  class DialogPanel extends JPanel
  { JLabel startLabel;
    JComboBox startCombo;  /* start time annotation */
    JLabel endLabel;
    JComboBox endCombo;  /* end time annotation */
    JLabel t1Label;
    JTextField t1Field;  /* lower bound */
    JLabel t2Label;
    JTextField t2Field;  /* upper bound */

  DialogPanel()
  { startLabel = new JLabel("Start:");
    startCombo = new JComboBox();
    endLabel = new JLabel("End:");
    endCombo = new JComboBox();
    t1Label = new JLabel("Lower bound:");
    t1Field = new JTextField();
    t2Label = new JLabel("Upper bound:");
    t2Field = new JTextField();

    add(startLabel);
    add(startCombo);
    add(endLabel);
    add(endCombo);
    add(t1Label);
    add(t1Field);
    add(t2Label);
    add(t2Field);
 }

  public void setOldFields(Vector t1s, Vector t2s, String t1, String t2)
  { 
    for (int i = 0; i < t1s.size(); i++) 
    { startCombo.addItem(((TimeAnnotation) t1s.get(i)).getName()); }  
    for (int i = 0; i < t2s.size(); i++) 
    { endCombo.addItem(((TimeAnnotation) t2s.get(i)).getName()); }  
    t1Field.setText(t1); 
    t2Field.setText(t2); 
  } 

  public Dimension getPreferredSize()
  { return new Dimension(350,270); }

  public Dimension getMinimumSize()
  { return new Dimension(350,270); }

  public void doLayout()
  { startLabel.setBounds(10,10,60,30);
    startCombo.setBounds(70,15,270,20);
    endLabel.setBounds(10,40,60,30);
    endCombo.setBounds(70,45,270,20);
    t1Label.setBounds(10,70,60,30);
    t1Field.setBounds(70,75,270,20);
    t2Label.setBounds(10,100,60,30);
    t2Field.setBounds(70,105,270,20);
  }

  public void reset()
  { // nameField.setText("");
    t1Field.setText("");
    t2Field.setText("");
    startCombo.removeAllItems(); 
    endCombo.removeAllItems(); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
       String label = button.getText();
       if ("Ok".equals(label))
       { setFields((String) dialogPanel.startCombo.getSelectedItem(),
                   (String) dialogPanel.endCombo.getSelectedItem(),
                   dialogPanel.t1Field.getText(),
                   dialogPanel.t2Field.getText()); 
       }
       else 
       { setFields(null,null,null,null); } 
   dialogPanel.reset();
   setVisible(false); } 
 } /* inner class */ 
}

