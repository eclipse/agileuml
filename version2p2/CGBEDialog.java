import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: GUI */ 

public class CGBEDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private CGBEDialogPanel dialogPanel;
  private String defaultName;
  private String defaultType;
  private String defaultExpressions; 
  private String defaultStatements;
  private String defaultLocalDecs; 
  private String defaultDeclarations;
  private String defaultClasses;
  private String defaultEnums;  

  private String newName;
  private String newType;
  private String newExpressions; 
  private String newStatements;
  private String newLocalDecs; 
  private String newDeclarations;
  private String newClasses; 
  private String newEnums; 


  public CGBEDialog(JFrame owner)
  { super(owner, true);
    setTitle("Specify CGBE grammar/rules");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new CGBEDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
    CGBESizeHandler sh = new CGBESizeHandler(dialogPanel); 
    addComponentListener(sh); 
  }

  public void setOldFields(String nme, String type, String params,
                           String pre, String post, String cls, 
                           String locdecs, String enumdecs)
  { defaultName = nme;
    defaultType = type;
    defaultExpressions = params; 
    defaultStatements = pre;
    defaultLocalDecs = locdecs;
    defaultDeclarations = post; 
    defaultClasses = cls;  
    defaultEnums = enumdecs; 
    dialogPanel.setOldFields(nme,type,params,pre,
                             post,cls,locdecs,enumdecs); 
  }

  public void setFields(String nme, String type, 
                        String params, 
                        String pre, String post, 
                        String stereo, 
                        String locdecs, 
                        String enumdecs)
  { newName = nme;
    newType = type;
    newExpressions = params; 
    newStatements = pre;
    newLocalDecs = locdecs;
    newDeclarations = post;
    newClasses = stereo;  
    newEnums = enumdecs; 
  }

  public String getName() 
  { return newName; }

  public String getTypesRule() 
  { return newType; }

  public String getExpressionsRule() 
  { return newExpressions; } 

  public String getStatementsRule() 
  { return newStatements; }

  public String getLocalDecsRule() 
  { return newLocalDecs; }
 
  public String getDeclarationsRule() 
  { return newDeclarations; }

  public String getClassesRule() 
  { return newClasses; } 

  public String getEnumsRule() 
  { return newEnums; } 

  class CGBEDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel typeLabel;
    JTextField typeField; 
    private JLabel exprsLabel; 
    JTextField exprsField; 
    private JLabel statsLabel;
    JTextField statsField;
    
    private JLabel declnsLabel;
    JTextField declnsField;
    
    private JLabel classesLabel; 
    JTextField classesField; 

    private JLabel localdecsLabel; 
    JTextField localdecsField; 

    private JLabel enumsLabel; 
    JTextField enumsField; 
    
    public CGBEDialogPanel()
    { nameLabel = new JLabel("Target Antlr grammar name:");
      nameField = new JTextField();
      nameField.setToolTipText("Antlr grammar name for target language"); 
      typeLabel = new JLabel("Grammar rule for types:");
      typeField = new JTextField();
      typeField.setToolTipText("Antlr grammar rule for parsing types"); 

      exprsLabel = new JLabel("Rule for expressions:"); 
      exprsField = new JTextField(); 
      
      statsLabel = new JLabel("Rule for statements:");
      statsField = new JTextField();

      declnsLabel = new JLabel("Rule for fields/operations:");
      declnsField = new JTextField();
      declnsField.setToolTipText("Grammar rule to parse data and function declarations"); 

      classesLabel = new JLabel("Rule for classes/structs:");
      classesField = new JTextField();

      localdecsLabel = new JLabel("Rule for local declarations:");
      localdecsField = new JTextField();

      enumsLabel = new JLabel("Rule for enum declarations:");
      enumsField = new JTextField();

      add(nameLabel); 
      add(nameField); 
      
      add(typeLabel); 
      add(typeField); 

      add(exprsLabel); 
      add(exprsField); 
      
      add(statsLabel);
      add(statsField); 
      
      add(declnsLabel); 
      add(declnsField);       

      add(classesLabel); 
      add(classesField);       

      add(localdecsLabel); 
      add(localdecsField);       

      add(enumsLabel); 
      add(enumsField);       
    }


  public void setOldFields(String nme, String type, String params, 
                           String pre, String post, String cls,
                           String locdecs, String enumdecs)
  { nameField.setText(nme);
    typeField.setText(type);  
    exprsField.setText(params); 
    statsField.setText(pre);
    declnsField.setText(post); 
    classesField.setText(cls);  
    localdecsField.setText(locdecs); 
    enumsField.setText(enumdecs); 
  }

  public Dimension getPreferredSize()
  { return new Dimension(500,300); }

  public Dimension getMinimumSize()
  { return new Dimension(500,300); }

  public void doLayout()
  {  
    nameLabel.setBounds(10,10,200,30);
    nameField.setBounds(210,15,270,20);
    typeLabel.setBounds(10,40,200,30);
    typeField.setBounds(210,45,270,20);
    exprsLabel.setBounds(10,70,200,30); 
    exprsField.setBounds(210,75,270,20); 
    statsLabel.setBounds(10,100,200,30);
    statsField.setBounds(210,105,270,20);
    declnsLabel.setBounds(10,130,200,30);
    declnsField.setBounds(210,135,270,20);
    classesLabel.setBounds(10,160,200,30); 
    classesField.setBounds(210,165,270,20); 
    localdecsLabel.setBounds(10,190,200,30); 
    localdecsField.setBounds(210,195,270,20);
    enumsLabel.setBounds(10,220,200,30); 
    enumsField.setBounds(210,225,270,20); 
  }

  public void resize()
  { 
    int newwidth = getWidth();     
    nameField.setSize(newwidth - 210, 20); 
    exprsField.setSize(newwidth - 210, 20); 
    typeField.setSize(newwidth - 210, 20); 
    classesField.setSize(newwidth - 210, 20); 
    declnsField.setSize(newwidth - 210, 20); 
    statsField.setSize(newwidth - 210, 20); 
    localdecsField.setSize(newwidth - 210, 20); 
    enumsField.setSize(newwidth - 210, 20); 
  }

  public void reset()
  { nameField.setText("Java");
    typeField.setText("typeTypeOrVoid");
    exprsField.setText("expression"); 
    statsField.setText("statement");
    declnsField.setText("classBodyDeclaration"); 
    classesField.setText("classDeclaration");
    localdecsField.setText("localVariableDeclaration");
    enumsField.setText("enumDeclaration");  
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 dialogPanel.typeField.getText(),
                 dialogPanel.exprsField.getText(), 
                 dialogPanel.statsField.getText(),
                 dialogPanel.declnsField.getText(),
                 dialogPanel.classesField.getText(),
                 dialogPanel.localdecsField.getText(),
                 dialogPanel.enumsField.getText()); 
     }
     else 
     { setFields(null,null,null,
                 null,null,null,null,null); 
     }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }

 class CGBESizeHandler implements ComponentListener
 { CGBEDialogPanel panel; 

   CGBESizeHandler(CGBEDialogPanel p) 
   { panel = p; } 

   public void componentResized(ComponentEvent e) 
   { panel.resize(); } 

   public void componentMoved(ComponentEvent e) { }
 
   public void componentShown(ComponentEvent e) { }

   public void componentHidden(ComponentEvent e) { }
  } 
}




