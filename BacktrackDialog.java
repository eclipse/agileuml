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


public class BacktrackDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private BacktrackDialogPanel dialogPanel;
  private String defaultName; // stereotype
  private String defaultEntity;
  private String defaultRole; // for add, remove, setatt, findByatt
 
  private String defaultDescription; 
  private String extendsUC; 

  private String newName;
  private String newEntity;
  private String newRole; 
  private String newDescription; 
  private String newExtends; 
  private String newType; 

  public BacktrackDialog(JFrame owner)
  { super(owner, true);
    setTitle("Define Backtracking");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new BacktrackDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String ename, String rname, String poss)
  { defaultName = nme;
    defaultEntity = ename;
    defaultRole = rname;
    newExtends = poss;  
    dialogPanel.setOldFields(nme,ename,rname,poss); 
  }

  public void setFields(String nme, String ename, String rname, 
                        String desc, String ext, String typ)
  { newName = nme;
    newEntity = ename;
    newRole = rname;
    newDescription = desc; 
    newExtends = ext; 
    newType = typ; 
  }

  public String getName() { return newName; }  // use case name
  public String getEntity() { return newEntity; }  // context entity
  public String getRole() { return newRole; }    // backtrack condition
  public String getDescription() { return newDescription; }  // undo statements
  public String getParameters() { return newExtends; }  // possible choices expression
  public String getUseCaseType() { return newType; } // success condition
 
  class BacktrackDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel entityLabel;
    JTextField entityField;  
    private JLabel roleLabel; 
    JTextField roleField; 
    JLabel descriptionLabel; 
    JTextArea descriptionArea; 
    JLabel extendsLabel; 
    JTextField extendsField; 
    JLabel typeLabel; 
    JTextField typeField; 
    
    public BacktrackDialogPanel()
    { nameLabel = new JLabel("Use case name:");
      nameField = new JTextField();
      nameField.setEditable(true); 

      entityLabel = new JLabel("Entity:");
      entityField = new JTextField();
      roleLabel = new JLabel("Backtrack condition:"); 
      roleField = new JTextField(); 
      descriptionLabel = new JLabel("undo statements:"); 
      descriptionArea = new JTextArea(); 
      extendsLabel = new JLabel("Choice expression:"); 
      extendsField = new JTextField(); 
      typeLabel = new JLabel("Success condition:");
      typeField = new JTextField();
      typeField.setEditable(true); 
    
      add(nameLabel);
      add(nameField);
      add(entityLabel);
      add(entityField);
      add(roleLabel); 
      add(roleField); 
      add(descriptionLabel); 
      add(descriptionArea); 
      add(extendsLabel); 
      add(extendsField); 
      add(typeLabel);
      add(typeField); 
    }

  public void setOldFields(String nme, String entity, String role, String poss)
  { nameField.setText(nme);
    entityField.setText(entity);
    roleField.setText(role);
    descriptionArea.setText(""); 
    extendsField.setText(poss); 
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,300); }

  public Dimension getMinimumSize()
  { return new Dimension(450,300); }

  public void doLayout()
  { nameLabel.setBounds(10,10,110,30);
    nameField.setBounds(120,15,270,20);
    entityLabel.setBounds(10,40,110,30);
    entityField.setBounds(120,45,270,20);
    roleLabel.setBounds(10,70,110,30); 
    roleField.setBounds(120,75,270,20); 
    descriptionLabel.setBounds(10,110,90,30); 
    descriptionArea.setBounds(120,105,300,100); 
    extendsLabel.setBounds(10,210,90,30); 
    extendsField.setBounds(120,215,270,20); 
    typeLabel.setBounds(10,240,90,30); 
    typeField.setBounds(120,245,270,20); 
  }

  public void reset()
  { nameField.setText("");
    entityField.setText("");
    roleField.setText(""); 
    descriptionArea.setText(""); 
    extendsField.setText(""); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 dialogPanel.entityField.getText(),
                 dialogPanel.roleField.getText(),
                 dialogPanel.descriptionArea.getText(),
                 dialogPanel.extendsField.getText(),
                 dialogPanel.typeField.getText()); 
     }
     else 
     { setFields(null,null,null,null,null,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




