import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 

/* package: Class Diagram Editor */ 
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class ModifyEntityDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ModifyDialogPanel dialogPanel;

  private boolean addAtt = false;
  private boolean addOp = false;
  private boolean removeAtt = false;
  private boolean removeOp = false; 
  private boolean editOp = false; 
  private boolean editName = false; 
  private boolean editStereotypes = false; 

  public ModifyEntityDialog(JFrame owner)
  { super(owner, true);
    setTitle("Modify Class Features");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ModifyDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER);
  }

  public void setFields(boolean aa, boolean ao, boolean ra, boolean ro,
                        boolean eo, boolean en, boolean est)
  { addAtt = aa;
    addOp = ao;
    removeAtt = ra;
    removeOp = ro;
    editOp = eo; 
    editName = en; 
    editStereotypes = est; 
  }

  public boolean isAddAtt() { return addAtt; } 

  public boolean isAddOp() { return addOp; }

  public boolean isRemoveAtt() { return removeAtt; }

  public boolean isRemoveOp() { return removeOp; }

  public boolean isEditOp() { return editOp; } 

  public boolean isEditName() { return editName; } 

  public boolean isEditStereotypes() { return editStereotypes; } 

  class ModifyDialogPanel extends JPanel  
  { JCheckBox addAttBox, addOpBox, removeAttBox, removeOpBox, editOpBox, editNameBox,
              editStereotypesBox;
    private JPanel queryPanel;
    private ButtonGroup queryGroup;  /* query or not */ 

    public ModifyDialogPanel()
    { queryPanel = new JPanel();
      queryPanel.setLayout(new GridLayout(4,1)); 
      addAttBox = new JCheckBox("Add Attribute",true);
      addOpBox = new JCheckBox("Add Operation");
      removeAttBox = new JCheckBox("Remove Attribute");
      removeOpBox = new JCheckBox("Remove Operation"); 
      // removeOpBox.setEnabled(false); 
      editOpBox = new JCheckBox("Edit Operation"); 
      editNameBox = new JCheckBox("Edit Name"); 
      editStereotypesBox = new JCheckBox("Edit Stereotypes"); 

      queryPanel.add(addAttBox);
      queryPanel.add(addOpBox);
      queryPanel.add(removeAttBox);
      queryPanel.add(removeOpBox);
      queryPanel.add(editOpBox);
      queryPanel.add(editNameBox);
      queryPanel.add(editStereotypesBox);

      queryPanel.setBorder(
        BorderFactory.createTitledBorder("Modify Class"));
      queryGroup = new ButtonGroup(); 
      queryGroup.add(addAttBox);
      queryGroup.add(addOpBox);
      queryGroup.add(removeAttBox);
      queryGroup.add(removeOpBox);
      queryGroup.add(editOpBox);
      queryGroup.add(editNameBox);
      queryGroup.add(editStereotypesBox);
  
      add(queryPanel);
    }

  public Dimension getPreferredSize()
  { return new Dimension(300,220); }  // 500,80

  public Dimension getMinimumSize()
  { return new Dimension(300,220); }  // 500,80

  public void doLayout()
  { queryPanel.setBounds(10,10,280,200); }  // 10,10,450,50

  public void reset()
  { addAttBox.setSelected(true); }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.addAttBox.isSelected(),
                 dialogPanel.addOpBox.isSelected(),
                 dialogPanel.removeAttBox.isSelected(),
                 dialogPanel.removeOpBox.isSelected(),
                 dialogPanel.editOpBox.isSelected(), 
                 dialogPanel.editNameBox.isSelected(),
                 dialogPanel.editStereotypesBox.isSelected()); 
     }
     else 
     { setFields(false,false,false,false,false,false,false); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




