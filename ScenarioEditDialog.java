import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/* Package: Requirements */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class ScenarioEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ScenarioDialogPanel dialogPanel;
  private String defaultName; // 
  private String defaultText;
  private String defaultText1;
  private String defaultCons;  
 
  private String newName;
  private String newText; 
  private String newText1; 
  private String newCons; 
 
  public ScenarioEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Define Scenario");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ScenarioButtonHandler bHandler = new ScenarioButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ScenarioDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String text, String text1, String cons)
  { defaultName = nme;
    defaultText = text;
    defaultText1 = text1; 
    defaultCons = cons; 
    dialogPanel.setOldFields(nme,text,text1,cons); 
  }

  public void setFields(String nme, String ename, String rname, String scope)
  { newName = nme;
    newText = ename;
    newText1 = rname;
    newCons = scope; 
  }

  public String getName() { return newName; }
  public String getText() { return newText; }
  public String getText1() { return newText1; } 
  public String getCons() { return newCons; } 
 
  class ScenarioDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel textLabel;
    JTextArea textField;  
    private JLabel text1Label; 
    JTextArea text1Field; 
    private JLabel consLabel; 
    JTextArea consField; 
    
    public ScenarioDialogPanel()
    { nameLabel = new JLabel("Name/Id:");
      nameField = new JTextField();

      textLabel = new JLabel("Informal text:");
      textField = new JTextArea();
      text1Label = new JLabel("Semi-formal text:"); 
      text1Field = new JTextArea(); 
      
      consLabel = new JLabel("Constraint:"); 
      consField = new JTextArea(); 

      add(nameLabel);
      add(nameField);
      add(textLabel);
      add(textField);
      add(text1Label); 
      add(text1Field); 
      add(consLabel); 
      add(consField); 
    }

  public void setOldFields(String nme, String text, String text1, String cons)
  { nameField.setText(nme);
    textField.setText(text);
    text1Field.setText(text1);
    consField.setText(cons);
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,300); }

  public Dimension getMinimumSize()
  { return new Dimension(450,300); }

  public void doLayout()
  { nameLabel.setBounds(10,10,130,30);
    nameField.setBounds(140,15,270,20);
    textLabel.setBounds(10,40,130,30);
    textField.setBounds(140,45,270,50);
    text1Label.setBounds(10,100,130,30); 
    text1Field.setBounds(140,105,270,50); 
    consLabel.setBounds(10,160,130,30); 
    consField.setBounds(140,165,270,50); 
 }

  public void reset()
  { nameField.setText("");
    textField.setText("");
    text1Field.setText("");
    consField.setText("");
  }
 }  /* inner class */

 class ScenarioButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 dialogPanel.textField.getText(),
                 dialogPanel.text1Field.getText(),
                 dialogPanel.consField.getText()); 
     }
     else 
     { setFields(null,null,null,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




