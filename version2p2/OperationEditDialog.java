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

public class OperationEditDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private OpDialogPanel dialogPanel;
  private String defaultName;
  private String defaultType;
  private String defaultParams; 
  private String defaultPre;
  private String defaultPost;
  private boolean defaultQuery = true;
  private String defaultStereotypes; 

  private String newName;
  private String newType;
  private String newParams; 
  private String newPre;
  private String newPost;
  private boolean newQuery = true;
  private String newStereotypes; 

  public OperationEditDialog(JFrame owner)
  { super(owner, true);
    setTitle("Edit Operation Properties");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new OpDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
    SizeHandler sh = new SizeHandler(dialogPanel); 
    addComponentListener(sh); 
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

  public void setStereotypes(Vector stereos)
  { dialogPanel.setStereotypes(stereos); } 

  public void setFields(String nme, String type, String params, 
                        String pre, String post, boolean query,
                        String stereo)
  { newName = nme;
    newType = type;
    newParams = params; 
    newPre = pre;
    newPost = post;
    newQuery = query;
    newStereotypes = stereo;  
  }

  public String getName() { return newName; }
  public String getType() { return newType; }
  public String getParams() { return newParams; } 
  public String getPre() { return newPre; }
  public String getPost() { return newPost; }
  public boolean getQuery() { return newQuery; }
  public String getStereotypes() { return newStereotypes; } 

  class OpDialogPanel extends JPanel
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

    private JLabel stereotypesLabel; 
    JComboBox stereotypesField; 
    
    public OpDialogPanel()
    { nameLabel = new JLabel("Name:");
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
      typeField.setToolTipText("Element types written as: Sequence(etype), Set(etype)"); 

      paramsLabel = new JLabel("Parameters, eg: x T y S "); 
      paramsField = new JTextField(); 
      preLabel = new JLabel("Pre:");
      preField = new JTextArea(30,10);
      preField.setLineWrap(true); 
      preField.setToolTipText("If no precondition, write true here"); 
      postLabel = new JLabel("Post:");
      postField = new JTextArea(30,10);
      postField.setLineWrap(true); 
      // postField.setResizable(true); 
      postField.setToolTipText("Constraint defining result/effect"); 

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

      stereotypesLabel = new JLabel("Stereotypes:");
      stereotypesField = new JComboBox();
      stereotypesField.setEditable(true); 
        
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
      add(stereotypesLabel); 
      add(stereotypesField);       
    }


  public void setStereotypes(Vector stereos) 
  { if (stereos == null || stereos.size() == 0)
    { stereotypesField.addItem("none"); 
      stereotypesField.addItem("abstract");
      stereotypesField.addItem("sequential"); 
      stereotypesField.addItem("static"); 
      stereotypesField.addItem("leaf"); 
      stereotypesField.addItem("explicit"); 
      stereotypesField.addItem("cached"); 
    } 
    else 
    { for (int i = 0; i < stereos.size(); i++) 
      { String str = (String) stereos.get(i); 
        stereotypesField.addItem(str); 
      } 
    } 
  } 

  public void setOldFields(String nme, String type, String params, 
                           String pre, String post, boolean query)
  { nameField.setText(nme);
    if (type == null || "null".equals(type) || "void".equals(type)) { } 
    else if (type.equals("int") || type.equals("long") || type.equals("double") ||
             type.equals("none") || type.equals("boolean") || type.equals("Sequence") ||
             type.equals("Set") || type.equals("String"))
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
    
    nameLabel.setBounds(10,10,120,30);
    nameField.setBounds(130,15,270,20);
    typeLabel.setBounds(10,40,120,30);
    typeField.setBounds(130,45,270,20);
    paramsLabel.setBounds(10,70,120,30); 
    paramsField.setBounds(130,75,270,20); 
    preLabel.setBounds(10,100,120,30);
    scroller1.setBounds(130,105,270,80);
    postLabel.setBounds(10,190,120,30);
    scroller2.setBounds(130,195,270,80);
    queryPanel.setBounds(10,280,400,50); 
    stereotypesLabel.setBounds(10,330,120,30); 
    stereotypesField.setBounds(130,335,270,20); 
  }

  public void resize()
  { 
    int newwidth = getWidth();     
    // nameLabel.setBounds(10,10,120,30);
    // nameField.setBounds(130,15,270,20);
    // typeLabel.setBounds(10,40,120,30);
    // typeField.setBounds(130,45,270,20);
    // paramsLabel.setBounds(10,70,120,30); 
    // paramsField.setBounds(130,75,270,20); 
    // preLabel.setBounds(10,100,120,30);
    preField.setSize(newwidth - 150, 10); 
    scroller1.setBounds(130,105,newwidth - 150,80);
    // postLabel.setBounds(10,190,120,30);
    postField.setSize(newwidth - 150, 10); 
    scroller2.setBounds(130,195,newwidth - 150,80);
    // queryPanel.setBounds(10,280,400,50); 
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

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 (String) dialogPanel.typeField.getSelectedItem(),
                 dialogPanel.paramsField.getText(), 
                 dialogPanel.preField.getText(),
                 dialogPanel.postField.getText(),
                 dialogPanel.queryBox.isSelected(),
                 (String) dialogPanel.stereotypesField.getSelectedItem()); 
     }
     else 
     { setFields(null,null,null,
                 null,null,true,null); 
     }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }

 class SizeHandler implements ComponentListener
 { OpDialogPanel panel; 

   SizeHandler(OpDialogPanel p) 
   { panel = p; } 

   public void componentResized(ComponentEvent e) 
   { panel.resize(); } 

   public void componentMoved(ComponentEvent e) { }
 
   public void componentShown(ComponentEvent e) { }

   public void componentHidden(ComponentEvent e) { }
  } 
}




