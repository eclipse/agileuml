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

public class EntityCreateDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ECDialogPanel dialogPanel;
  private String defaultName;
  private String defaultCard;
  private String defaultStereotypes; 

  private String newName;
  private String newCard;
  private String newStereotypes; 

  public EntityCreateDialog(JFrame owner)
  { super(owner, true);
    setTitle("Class cardinality and stereotypes");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ECDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(String nme, String card, String stereo)
  { defaultName = nme;
    defaultCard = card;
    defaultStereotypes = stereo; 
    dialogPanel.setOldFields(nme,card,stereo); 
  }

  public void setFields(String nme, String card, String stereo)
  { newName = nme;
    newCard = card;
    newStereotypes = stereo;
  }

  public String getName() { return newName; }

  public String getCard() { return newCard; }
 
  public String getStereotypes() { return newStereotypes; } 
 
  class ECDialogPanel extends JPanel
  { private JLabel nameLabel;
    JTextField nameField;  /* name */
    private JLabel cardLabel;
    JTextField cardField;  
    private JLabel stereotypesLabel; 
    JComboBox stereotypesField; 
    
    public ECDialogPanel()
    { stereotypesLabel = new JLabel("Stereotype:");
      stereotypesField = new JComboBox();
      stereotypesField.addItem("none"); 
      stereotypesField.addItem("abstract");
      stereotypesField.addItem("interface"); 
      stereotypesField.addItem("persistent"); 
      stereotypesField.addItem("utility"); 
      stereotypesField.addItem("sequential"); 
      stereotypesField.addItem("leaf"); 
      stereotypesField.addItem("active"); 
      stereotypesField.addItem("source"); 
      stereotypesField.addItem("target"); 
      stereotypesField.addItem("auxiliary"); 
      stereotypesField.addItem("external"); 
      stereotypesField.addItem("externalApp"); 
      stereotypesField.addItem("ERelation"); 
      // nameField.addItem("remove"); 
      // nameField.setEditable(false); 

      nameLabel = new JLabel("Name:");
      nameField = new JTextField();
      cardLabel = new JLabel("Cardinality (* or n):"); 
      cardField = new JTextField(); 
    
      add(nameLabel);
      add(nameField);
      add(cardLabel);
      add(cardField);
      add(stereotypesLabel); 
      add(stereotypesField); 
    }

  public void setOldFields(String nme, String card, String stereo)
  { nameField.setText(nme);
    cardField.setText(card);
  }

  public Dimension getPreferredSize()
  { return new Dimension(450,300); }

  public Dimension getMinimumSize()
  { return new Dimension(450,300); }

  public void doLayout()
  { nameLabel.setBounds(10,10,90,30);
    nameField.setBounds(100,15,270,20);
    cardLabel.setBounds(10,40,90,30);
    cardField.setBounds(100,45,270,20);
    stereotypesLabel.setBounds(10,70,90,30); 
    stereotypesField.setBounds(100,75,270,20); 
  }

  public void reset()
  { nameField.setText("");
    cardField.setText("");
  }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.nameField.getText(),
                 dialogPanel.cardField.getText(),
                 (String) dialogPanel.stereotypesField.getSelectedItem()); 
     }
     else 
     { setFields(null,null,null); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




