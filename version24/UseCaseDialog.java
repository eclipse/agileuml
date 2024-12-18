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
* Copyright (c) 2003,2020 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/


public class UseCaseDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private UCDialogPanel dialogPanel;
  private String defaultName; 
  // private String defaultEntity;
  private String defaultStereotype; 
 
  private String parameterList; 

  private String newName;
  // private String newEntity;
  private String newStereotype; 
  // private String newDescription; 
  private String newParameterList; 
  private String newType; 

  public UseCaseDialog(JFrame owner)
  { super(owner, true);
    setTitle("Define General Use Case");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new UCDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String ename, String rname)
  { defaultName = nme;
    // defaultEntity = ename;
    // defaultRole = rname; 
    dialogPanel.setOldFields(nme,ename,rname); 
  }

  public void setFields(String nme, String ename, String rname, 
                        String desc, String ext, String typ)
  { newName = nme;
    // newEntity = ename;
    newStereotype = rname;
    // newDescription = desc; 
    newParameterList = ext; 
    newType = typ; 
  }

  public String getName() { return newName; }
  // public String getEntity() { return newEntity; }
  public String getStereotype() { return newStereotype; } 
  // public String getDescription() { return newDescription; } 
  public String getParameters() { return newParameterList; } 
  public String getUseCaseType() { return newType; } 
 
  class UCDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    // private JLabel entityLabel;
    // JTextField entityField;  
    // private JLabel roleLabel; 
    // JTextField roleField; 
    // JLabel descriptionLabel; 
    // JTextArea descriptionArea; 
    JLabel parametersLabel; 
    JTextField parametersField; 
    JLabel typeLabel; 
    JComboBox typeField; 
    private JLabel stereotypeLabel;
    JComboBox stereotypeField; 
    
    public UCDialogPanel()
    { nameLabel = new JLabel("Name:");
      nameField = new JTextField();

      // entityLabel = new JLabel("Entity:");
      // entityField = new JTextField();
      // roleLabel = new JLabel("Role/Attribute:"); 
      // roleField = new JTextField(); 
      // descriptionLabel = new JLabel("Description:"); 
      // descriptionArea = new JTextArea(); 
      parametersLabel = new JLabel("Parameters: x type"); 
      parametersField = new JTextField(); 
      typeLabel = new JLabel("Result type:");
      typeField = new JComboBox();
      typeField.addItem("none"); 
      typeField.addItem("Sequence"); 
      typeField.addItem("Set"); 
      typeField.addItem("int"); 
      typeField.addItem("long"); 
      typeField.addItem("double"); 
      typeField.addItem("boolean"); 
      typeField.addItem("String"); 
      typeField.setEditable(true); 
    
      stereotypeLabel = new JLabel("Stereotype:");
      stereotypeField = new JComboBox();
      stereotypeField.addItem("none"); 
      stereotypeField.addItem("private"); 
      stereotypeField.addItem("public"); 
      stereotypeField.setEditable(true); 
      
      add(nameLabel);
      add(nameField);
      add(parametersLabel); 
      add(parametersField); 
      add(typeLabel);
      add(typeField);
      add(stereotypeLabel);
      add(stereotypeField); 
    }

  public void setOldFields(String nme, String entity, String role)
  { nameField.setText(nme);
    parametersField.setText(""); 
    // typeField.setText(""); 
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,200); }

  public Dimension getMinimumSize()
  { return new Dimension(450,200); }

  public void doLayout()
  { nameLabel.setBounds(10,10,130,30);
    nameField.setBounds(140,15,270,20);
    // entityLabel.setBounds(10,40,130,30);
    // entityField.setBounds(140,45,270,20);
    // roleLabel.setBounds(10,70,130,30); 
    // roleField.setBounds(140,75,270,20); 
    // descriptionLabel.setBounds(10,130,90,30); 
    // descriptionArea.setBounds(140,105,300,100); 
    parametersLabel.setBounds(10,40,130,30); 
    parametersField.setBounds(140,45,270,20); 
    typeLabel.setBounds(10,70,130,30); 
    typeField.setBounds(140,75,270,20);
    stereotypeLabel.setBounds(10,100,130,30); 
    stereotypeField.setBounds(140,105,270,20); 
  }

  public void reset()
  { nameField.setText("");
    // entityField.setText("");
    // roleField.setText(""); 
    // descriptionArea.setText(""); 
    parametersField.setText(""); 
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields((String) dialogPanel.nameField.getText(),
                 "",
                 (String) dialogPanel.stereotypeField.getSelectedItem(),
                 "",
                 dialogPanel.parametersField.getText(),
                 (String) dialogPanel.typeField.getSelectedItem()); 
     }
     else 
     { setFields(null,null,null,null,null,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




