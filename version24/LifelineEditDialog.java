import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class LifelineEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private DialogPanel dialogPanel;
  

  private String defaultName;
  private boolean defaultInit;
  private String defaultClass; 
  private String defaultEntry; 

  private String newName;
  private boolean newInit;
  private String newClass; 
  private String newEntry; 
   
  public LifelineEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Edit Lifeline Properties");
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
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String ev, Vector ents, boolean init)
  { defaultName = ev;
    defaultInit = init; 
    dialogPanel.setOldFields(ev,ents,init); 
  }

  public void setFields(String ev, String cls, boolean i, String ent)
  { newName = ev;
    newInit = i; 
    newClass = cls; 
    newEntry = ent; 
  }

  public String getName() { return newName; }
  public boolean getTerminates() { return newInit; }
  public String getEntity() { return newClass; } 
  public String getEntry() { return newEntry; } 

  class DialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* Object name */
    
    private JLabel classLabel;
    JComboBox classField;  /* Class */

    private JLabel entryLabel;
    JTextField entryField;  /* State entry action */
   
    JCheckBox initBox, notinitBox;
    private JPanel initPanel;
    private ButtonGroup group; 

    public DialogPanel()
    { nameLabel = new JLabel("Name:");
      nameField = new JTextField();
      classLabel = new JLabel("Class:");
      classField = new JComboBox();
      entryLabel = new JLabel("Entry action:");
      entryField = new JTextField();
      initPanel = new JPanel();
      initBox = new JCheckBox("Terminates");
      notinitBox = new JCheckBox("Not terminated",true);
      initPanel.add(initBox);
      initPanel.add(notinitBox);
      initPanel.setBorder(
        BorderFactory.createTitledBorder("Terminated or not"));
      group = new ButtonGroup(); 
      group.add(initBox);
      group.add(notinitBox);

      add(nameLabel);
      add(nameField);
      add(classLabel);
      add(classField);
      add(entryLabel); 
      add(entryField); 
      add(initPanel); 
    }

  public void setOldFields(String nme, Vector ents, boolean init)
  { nameField.setText(nme);
    if (init)
    { initBox.setSelected(true); }
    else
    { notinitBox.setSelected(true); } 
    classField.removeAllItems(); 
    for (int i = 0; i < ents.size(); i++) 
    { classField.addItem(((Entity) ents.get(i)).getName()); }  
  }

  public Dimension getPreferredSize()
  { return new Dimension(350,160); }

  public Dimension getMinimumSize()
  { return new Dimension(350,160); }

  public void doLayout()
  { nameLabel.setBounds(10,10,60,30);
    nameField.setBounds(70,15,270,20);
    classLabel.setBounds(10,40,60,30);
    classField.setBounds(70,45,270,20);
    entryLabel.setBounds(10,70,60,30);
    entryField.setBounds(70,75,270,20);
    initPanel.setBounds(10,100,330,50); 
  }

  public void reset()
  { nameField.setText("");
    notinitBox.setSelected(true); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 (String) dialogPanel.classField.getSelectedItem(), 
                 dialogPanel.initBox.isSelected(),
                 dialogPanel.entryField.getText()); 
     }
     else 
     { setFields(null,null,false,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}


