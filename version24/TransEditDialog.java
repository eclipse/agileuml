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

class TransEditDialog extends JDialog
{ JPanel bottom;
   JButton okButton, cancelButton;
   DialogPanel dialogPanel;

   String defaultName; 
   String defaultGuard; 
   String defaultGens; 
   // boolean defaultInput; 

   String newName;
   String newGuard;
   String newGens;
   boolean isInput;

   public TransEditDialog(JFrame owner)
   { super(owner, true);
     setTitle("Edit Transition Properties");
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

  public void setOldFields(Vector evs, String gd, String gen)
  { // defaultName = ev;
    defaultGuard = gd;
    defaultGens = gen;
    // defaultInput = i;
    dialogPanel.setOldFields(evs,gd,gen);
  }

  public void setFields(String ev, String gd, String gen)
  { newName = ev;
    newGuard = gd;
    newGens = gen;
  }

  public String getName() { return newName; } 
  public String getGuard() { return newGuard; } 
  public String getGens() { return newGens; } 
  // public boolean getInput() { return isInput; } 

  class DialogPanel extends JPanel
  { JLabel nameLabel;
    JComboBox nameCombo;  /* Trigger event name */
    JLabel guardLabel;
    JTextField guardField;  /* Guard expression */
    JLabel genLabel;
    JTextField genField;     /* Generations */
    // JCheckBox inBox, outBox;
    // JPanel inoutPanel;
    // ButtonGroup group; 

  DialogPanel()
  { nameLabel = new JLabel("Event:");
    nameCombo = new JComboBox();
    guardLabel = new JLabel("Guard:");
    guardField = new JTextField();
    genLabel = new JLabel("Generations:");
    genField = new JTextField();
    // inoutPanel = new JPanel();
    // inBox = new JCheckBox("Input",true);
    // outBox = new JCheckBox("Output");
    // inoutPanel.add(inBox);
    // inoutPanel.add(outBox);
    // inoutPanel.setBorder(
    //    BorderFactory.createTitledBorder("Input or Output"));
    // group = new ButtonGroup(); 
    // group.add(inBox);
    // group.add(outBox);

    add(nameLabel);
    add(nameCombo);
    add(guardLabel);
    add(guardField);
    add(genLabel);
    add(genField);
    // add(inoutPanel);
 }

  public void setOldFields(Vector evs, String gd, String gen)
  { 
    for (int i = 0; i < evs.size(); i++) 
    { nameCombo.addItem("" + evs.get(i)); }  
    guardField.setText(gd); 
    genField.setText(gen); 
    // if (i)
    // { inBox.setSelected(true); } 
    // else 
    // { outBox.setSelected(true); }
  } 

  public Dimension getPreferredSize()
  { return new Dimension(350,170); }

  public Dimension getMinimumSize()
  { return new Dimension(350,170); }

  public void doLayout()
  { nameLabel.setBounds(10,10,60,30);
     nameCombo.setBounds(70,15,270,20);
     guardLabel.setBounds(10,40,60,30);
     guardField.setBounds(70,45,270,20);
     genLabel.setBounds(10,70,60,30);
     genField.setBounds(70,75,270,20);
     // inoutPanel.setBounds(10,100,330,50); 
  }

  public void reset()
  { // nameField.setText("");
     guardField.setText("");
     genField.setText("");
     nameCombo.removeAllItems(); 
     // inBox.setSelected(true); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
       String label = button.getText();
       if ("Ok".equals(label))
       { setFields((String) dialogPanel.nameCombo.getSelectedItem(),
                   dialogPanel.guardField.getText(),
                   dialogPanel.genField.getText()); 
       }
                   // dialogPanel.inBox.isSelected()); }
       else 
       { setFields(null,null,null); } 
   dialogPanel.reset();
   setVisible(false); } 
 } /* inner class */ 
}

