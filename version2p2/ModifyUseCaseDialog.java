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

public class ModifyUseCaseDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ModifyUseCaseDialogPanel dialogPanel;

  private boolean addPre = false;
  private boolean addPost = false;
  private boolean removePre = false;
  private boolean removePost = false; 
  private boolean editConstraint = false; 
  private boolean addExtends = false; 
  private boolean addIncludes = false; 
  private boolean addSuper = false; 
  private boolean expand = false; 
  private boolean genInverse = false; 
  private boolean inheritFrom = false; 
  private boolean addInv = false; 
  private boolean addAtt = false; 
  private boolean addOp = false; 
  private boolean removeAtt = false; 
  private boolean removeOp = false; 
  private boolean editOp = false; 

  public ModifyUseCaseDialog(JFrame owner)
  { super(owner, true);
    setTitle("Modify Use Case");
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ModifyUseCaseDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER);
  }

  public void setFields(boolean aa, boolean ao, boolean ra, boolean ro,
                        boolean eo, boolean aex, boolean ainc, boolean expd,
                        boolean inv, boolean ifrom, boolean addi, boolean adda,
                        boolean addop, boolean rematt, boolean remop, boolean edop)
  { addPre = aa;
    addPost = ao;
    removePre = ra;
    removePost = ro;
    editConstraint = eo; 
    addExtends = aex; 
    addIncludes = ainc;
    expand = expd;  
    genInverse = inv; 
    inheritFrom = ifrom;
    addInv = addi;
    addAtt = adda;  
    addOp = addop; 
    removeAtt = rematt;  
    removeOp = remop; 
    editOp = edop; 
  }

  public boolean isAddPre() { return addPre; } 

  public boolean isAddPost() { return addPost; }

  public boolean isRemovePre() { return removePre; }

  public boolean isRemovePost() { return removePost; }

  public boolean isEditConstraint() { return editConstraint; } 

  public boolean isAddExtends() { return addExtends; } 

  public boolean isAddIncludes() { return addIncludes; } 
 
  public boolean isExpand() { return expand; } 

  public boolean isGenInverse() { return genInverse; } 

  public boolean isInheritFrom() { return inheritFrom; } 

  public boolean isAddInv() { return addInv; } 

  public boolean isAddAtt() { return addAtt; } 

  public boolean isAddOp() { return addOp; } 

  public boolean isRemoveAtt() { return removeAtt; } 

  public boolean isRemoveOp() { return removeOp; } 

  public boolean isEditOp() { return editOp; } 

  class ModifyUseCaseDialogPanel extends JPanel
  { JCheckBox addPreBox, addPostBox, removePreBox, removePostBox,
         editConstraintBox, addExtendsBox, addIncludesBox, addSpecBox, 
         expandBox, invertBox, addInvBox, addAttBox, addOpBox, remAttBox, remOpBox, 
         editOpBox; 

    private JPanel queryPanel;
    private ButtonGroup queryGroup;  /* query or not */ 

    public ModifyUseCaseDialogPanel()
    { queryPanel = new JPanel();
      queryPanel.setLayout(new GridLayout(4,4)); 
      addPreBox = new JCheckBox("Add Precondition");
      addPostBox = new JCheckBox("Add Postcondition",true);
      removePreBox = new JCheckBox("Remove Precondition");
      removePostBox = new JCheckBox("Remove Postcondition"); 
      // removePreBox.setEnabled(false); 
      // removePostBox.setEnabled(false); 
      editConstraintBox = new JCheckBox("Edit Constraint"); 
      // editConstraintBox.setEnabled(false); 
      addExtendsBox = new JCheckBox("Add Extension"); 
      addIncludesBox = new JCheckBox("Add Inclusion"); 
      addSpecBox = new JCheckBox("Execution mode"); 
      // addSpecBox.setEnabled(false); 
      expandBox = new JCheckBox("Expand"); 
      invertBox = new JCheckBox("Reverse"); 
      addInvBox = new JCheckBox("Add invariant"); 
      addAttBox = new JCheckBox("Add attribute"); 
      addOpBox = new JCheckBox("Add operation"); 
      remAttBox = new JCheckBox("Remove attribute"); 
      remOpBox = new JCheckBox("Remove operation"); 
      editOpBox = new JCheckBox("Edit operation"); 

      queryPanel.add(addPreBox);
      queryPanel.add(addPostBox);
      queryPanel.add(removePreBox);
      queryPanel.add(removePostBox);
      queryPanel.add(editConstraintBox);
      queryPanel.add(addExtendsBox); 
      queryPanel.add(addIncludesBox); 
      queryPanel.add(addSpecBox); 
      queryPanel.add(expandBox); 
      queryPanel.add(invertBox); 
      queryPanel.add(addInvBox); 
      queryPanel.add(addAttBox); 
      queryPanel.add(addOpBox); 
      queryPanel.add(remAttBox); 
      queryPanel.add(remOpBox); 
      queryPanel.add(editOpBox); 

      queryPanel.setBorder(
        BorderFactory.createTitledBorder("Modify Use Case"));
      queryGroup = new ButtonGroup(); 
      queryGroup.add(addPreBox);
      queryGroup.add(addPostBox);
      queryGroup.add(removePreBox);
      queryGroup.add(removePostBox);
      queryGroup.add(editConstraintBox);
      queryGroup.add(addExtendsBox);
      queryGroup.add(addIncludesBox);
      queryGroup.add(addSpecBox); 
      queryGroup.add(expandBox); 
      queryGroup.add(invertBox); 
      queryGroup.add(addInvBox); 
      queryGroup.add(addAttBox); 
      queryGroup.add(addOpBox); 
      queryGroup.add(remAttBox); 
      queryGroup.add(remOpBox); 
      queryGroup.add(editOpBox); 
  
      add(queryPanel);
    }

  public Dimension getPreferredSize()
  { return new Dimension(700,300); }  // 500,80

  public Dimension getMinimumSize()
  { return new Dimension(700,300); }  // 500,80

  public void doLayout()
  { queryPanel.setBounds(10,10,650,250); }  // 10,10,450,50

  public void reset()
  { addPostBox.setSelected(true); }
 }  /* inner class */

 class ButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.addPreBox.isSelected(),
                 dialogPanel.addPostBox.isSelected(),
                 dialogPanel.removePreBox.isSelected(),
                 dialogPanel.removePostBox.isSelected(),
                 dialogPanel.editConstraintBox.isSelected(),
                 dialogPanel.addExtendsBox.isSelected(),
                 dialogPanel.addIncludesBox.isSelected(),
                 dialogPanel.expandBox.isSelected(),
                 dialogPanel.invertBox.isSelected(),
                 dialogPanel.addSpecBox.isSelected(),
                 dialogPanel.addInvBox.isSelected(), dialogPanel.addAttBox.isSelected(),
                 dialogPanel.addOpBox.isSelected(), dialogPanel.remAttBox.isSelected(), 
                 dialogPanel.remOpBox.isSelected(), dialogPanel.editOpBox.isSelected()); 
     }
     else 
     { setFields(false,false,false,false,false,false,false,false,false,false,false,false,
                 false,false,false,false); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}




