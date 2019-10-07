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
/* Package: GUI */ 

public class OperatorEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private OperDialogPanel dialogPanel;
  private String defaultName;
  private String defaultType;
  private String defaultParams; 
  private String defaultPre;
  private String defaultPost;
  private boolean defaultQuery = true;
  // private String defaultStereotypes; 

  private String newName;
  private String newType;
  private String newParams; 
  private String newPre = null;
  private String newPost = null;
  private boolean newQuery = true;
  // private String newStereotypes; 

  public OperatorEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Define new -> operator");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    OperButtonHandler bHandler = new OperButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new OperDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String type, String params,
                           String pre, String post, boolean query)
  { defaultName = nme;
    defaultType = type;
    defaultParams = params; 
    defaultPre = pre;
    defaultPost = post; 
    defaultQuery = query; 
    dialogPanel.setOldFields(nme,type,params,pre,post,query); 
  }

  public void setFields(String nme, String type, String params, 
                        String pre, String post, boolean query)
  { newName = nme;
    newType = type;
    newParams = params; 
    newPre = pre;
    newPost = post;
    newQuery = query;
    // newStereotypes = stereo;  
  }

  public String getName() { return newName; }
  public String getType() { return newType; }
  public String getParams() { return newParams; } 
  public String getPre() { return newPre; }
  public String getPost() { return newPost; }
  public boolean getQuery() { return newQuery; }
  // public String getStereotypes() { return newStereotypes; } 

  class OperDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel typeLabel;
    JComboBox typeField; 
    private JLabel paramsLabel; 
    JTextField paramsField; 
    private JLabel preLabel;
    JTextArea preField;
    JScrollPane scroller1, scroller2;

    private JLabel postLabel;
    JTextArea postField;
    
    JCheckBox queryBox, updateBox;
    private JPanel queryPanel;
    private ButtonGroup queryGroup;  /* query or not */ 

    // private JLabel stereotypesLabel; 
    // JComboBox stereotypesField; 
    
    public OperDialogPanel()
    { nameLabel = new JLabel("Name (including ->):");
      nameField = new JTextField();
      typeLabel = new JLabel("Type:");
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

      paramsLabel = new JLabel("Parameter (if any), eg: x"); 
      paramsField = new JTextField(); 
      preLabel = new JLabel("Java code:");
      preField = new JTextArea(30,6);
      preField.setLineWrap(true); 
      postLabel = new JLabel("C# code:");
      postField = new JTextArea(30,6);
      postField.setLineWrap(true); 
      
      queryPanel = new JPanel();
      queryBox = new JCheckBox("Query",true);
      updateBox = new JCheckBox("Update");
      queryPanel.add(queryBox);
      queryPanel.add(updateBox);
      queryPanel.setBorder(
        BorderFactory.createTitledBorder("Query or update"));
      queryGroup = new ButtonGroup(); 
      queryGroup.add(queryBox);
      queryGroup.add(updateBox);

      // stereotypesLabel = new JLabel("Stereotype:");
      // stereotypesField = new JComboBox();
      // stereotypesField.addItem("none"); 
      // stereotypesField.addItem("abstract");
      // stereotypesField.addItem("sequential"); 
      // stereotypesField.addItem("static"); 
      // stereotypesField.addItem("leaf"); 
      // stereotypesField.addItem("explicit"); 
      // stereotypesField.addItem("cached"); 
        
      add(nameLabel);
      add(nameField);
      add(typeLabel);
      add(typeField);
      add(paramsLabel); 
      add(paramsField); 
      add(preLabel);
      scroller1 = new JScrollPane(preField, 
         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroller1.setPreferredSize(new Dimension(300,65));
      add(scroller1);

      add(postLabel);
      scroller2 = new JScrollPane(postField, 
         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scroller2.setPreferredSize(new Dimension(300,65));
      add(scroller2);

      add(queryPanel);
      // add(stereotypesLabel); 
      // add(stereotypesField);       
    }

  public void setOldFields(String nme, String type, String params, 
                           String pre, String post, boolean query)
  { nameField.setText(nme);
    if (type == null || "null".equals(type) || "void".equals(type)) { } 
    else 
    { typeField.setSelectedItem(type); } 
    paramsField.setText(params); 
    preField.setText(pre);
    postField.setText(post); 
    if (query)
    { queryBox.setSelected(true); }
    else
    { updateBox.setSelected(true); } 
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,400); }

  public Dimension getMinimumSize()
  { return new Dimension(450,400); }

  public void doLayout()
  { 
    nameLabel.setBounds(10,10,150,30);
    nameField.setBounds(160,15,270,20);
    typeLabel.setBounds(10,40,150,30);
    typeField.setBounds(160,45,270,20);
    paramsLabel.setBounds(10,70,150,30); 
    paramsField.setBounds(160,75,270,20); 
    preLabel.setBounds(10,100,150,30);
    scroller1.setBounds(160,105,270,80);
    postLabel.setBounds(10,190,150,30);
    scroller2.setBounds(160,195,270,80);
    queryPanel.setBounds(10,280,400,50); 
    // stereotypesLabel.setBounds(10,330,120,30); 
    // stereotypesField.setBounds(130,335,270,20); 
  }

  public void reset()
  { nameField.setText("");
    typeField.setSelectedItem("none");
    paramsField.setText(""); 
    preField.setText("");
    postField.setText(""); 
    queryBox.setSelected(true);
  }
 }  /* inner class */

 class OperButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 (String) dialogPanel.typeField.getSelectedItem(),
                 dialogPanel.paramsField.getText(), 
                 dialogPanel.preField.getText(),
                 dialogPanel.postField.getText(),
                 dialogPanel.queryBox.isSelected()); 
                 // (String) dialogPanel.stereotypesField.getSelectedItem()); 
     }
     else 
     { setFields(null,null,null,
                 null,null,true); 
     }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




