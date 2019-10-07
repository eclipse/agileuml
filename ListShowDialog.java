/* ListShowDialog.java -- a simple example of a dialog containing 
   a JList with a single item selection.  Used in Rsds to show and 
   delete invariants. */ 

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.io.*; 


class ListShowDialog extends JDialog
{ 
  JPanel bottom;
  JButton okButton, cancelButton;
  ListShowPanel showPanel;
  int newSelection;
  int[] newIndexes; 
  Object[] newValues; 
   
   ListShowDialog(JFrame owner)
   { super(owner, true);
     setTitle("Model Element List");
     okButton = new JButton("Ok");
     cancelButton = new JButton("Cancel");
     ISButtonHandler bHandler = new ISButtonHandler();
     okButton.addActionListener(bHandler);
     cancelButton.addActionListener(bHandler);
      
     bottom = new JPanel();
     bottom.add(okButton);
     bottom.add(cancelButton);
     bottom.setBorder(BorderFactory.createEtchedBorder());
     showPanel = new ListShowPanel();
     getContentPane().setLayout(new BorderLayout());
     getContentPane().add(bottom, BorderLayout.SOUTH);
     getContentPane().add(showPanel, BorderLayout.CENTER); }

  public void setOldFields(Vector invs)
  {  showPanel.setOldFields(invs); }

  public void setFields(int sel, Object[] inds)
  { newSelection = sel; 
    newValues = inds;
  }
 
  public int getSelected() { return newSelection; }

  public Object[] getSelectedValues()
  { return newValues; } 

 class ISButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(showPanel.getSelected(),showPanel.getSelectedValues()); }
     else if ("Cancel".equals(label))
     { setFields(-1,null); } 
  
     showPanel.reset();
     setVisible(false); 
   }
 } 
}

class ListShowPanel extends JPanel
{ JList invList;
   JScrollPane scrollPane;
  
  ListShowPanel()
  { invList = new JList();
    invList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
    scrollPane = new JScrollPane(invList);
    // add(invList);
   add(scrollPane); }

  public void setOldFields(Vector list)
   { invList.setListData(list); }

  public Dimension getPreferredSize()
  { return new Dimension(500,300); }

  public Dimension getMinimumSize()
  { return new Dimension(300,100); }

  public void doLayout()
  { scrollPane.setBounds(10,10,480,280); }

  public void reset()
  { invList.clearSelection(); }

  public int getSelected()
  { return  invList.getSelectedIndex(); }

  public Object[] getSelectedValues()
  { return invList.getSelectedValues(); } 
 }  
