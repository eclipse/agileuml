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


public class RequirementEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private REDialogPanel dialogPanel;
  private String defaultName; // 
  private String defaultText;
  private String defaultKind;
  private String defaultScope;  
 
  private String newName;
  private String newText; 
  private String newKind; 
  private String newScope; 
 
  public RequirementEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Define Requirement");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    REButtonHandler bHandler = new REButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new REDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String text, String kind, String scope)
  { defaultName = nme;
    defaultText = text;
    defaultKind = kind; 
    defaultScope = scope; 
    dialogPanel.setOldFields(nme,text,kind,scope); 
  }

  public void setFields(String nme, String ename, String rname, String scope)
  { newName = nme;
    newText = ename;
    newKind = rname;
    newScope = scope; 
  }

  public String getName() { return newName; }
  public String getText() { return newText; }
  public String getKind() { return newKind; } 
  public String getScope() { return newScope; } 
 
  class REDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel textLabel;
    JTextField textField;  
    private JLabel kindLabel; 
    JComboBox kindField; 
    private JLabel scopeLabel; 
    JComboBox scopeField; 
    
    public REDialogPanel()
    { nameLabel = new JLabel("Name/Id:");
      nameField = new JTextField();

      textLabel = new JLabel("Text:");
      textField = new JTextField();
      kindLabel = new JLabel("Kind:"); 
      kindField = new JComboBox(); 
      kindField.addItem("functional");  
      kindField.addItem("non-functional");
      kindField.addItem("other");  
      kindField.setEditable(true); 
    
      scopeLabel = new JLabel("Scope:"); 
      scopeField = new JComboBox(); 
      scopeField.addItem("global");  
      scopeField.addItem("local");
      scopeField.addItem("other");  
      scopeField.setEditable(true); 

      add(nameLabel);
      add(nameField);
      add(textLabel);
      add(textField);
      add(kindLabel); 
      add(kindField); 
      add(scopeLabel); 
      add(scopeField); 
    }

  public void setOldFields(String nme, String entity, String role, String scope)
  { nameField.setText(nme);
    textField.setText(entity);
    // kindField.setText(role);
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,300); }

  public Dimension getMinimumSize()
  { return new Dimension(450,300); }

  public void doLayout()
  { nameLabel.setBounds(10,10,130,30);
    nameField.setBounds(140,15,270,20);
    textLabel.setBounds(10,40,130,30);
    textField.setBounds(140,45,270,20);
    kindLabel.setBounds(10,70,130,30); 
    kindField.setBounds(140,75,270,20); 
    scopeLabel.setBounds(10,100,130,30); 
    scopeField.setBounds(140,105,270,20); 
 }

  public void reset()
  { nameField.setText("");
    textField.setText("");
    // kindField.setText("");  
  }
 }  /* inner class */

 class REButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields((String) dialogPanel.nameField.getText(),
                 dialogPanel.textField.getText(),
                 (String) dialogPanel.kindField.getSelectedItem(),
                 (String) dialogPanel.scopeField.getSelectedItem()); 
     }
     else 
     { setFields(null,null,null,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




