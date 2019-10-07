import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.*;
import java.util.EventObject;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.*;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

class TempInvEditDialog extends JDialog
{ 
  JPanel bottom;
  JButton okButton, cancelButton;
  TempInvDialogPanel dialogPanel;
  String defaultOp = "";
  String defaultCond = "";
  String defaultEffect = "";
  boolean defaultisSys = true;
  boolean defaultisCrit = false;

  Vector newOp = new Vector();
  String newCond;
  String newEffect;
  boolean newisSys;
  boolean newisCrit;

  TempInvEditDialog(JFrame owner)
  { super(owner, 
          "Edit/Create Temporal Invariant", true);
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ButtonHandler bHandler = new ButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
      
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(
      BorderFactory.createEtchedBorder());
    dialogPanel = new TempInvDialogPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel,
                         BorderLayout.CENTER); }

  public void setOldFields(Vector op, String c,
                           String e, boolean isSys,
                           boolean isCrit)
  { defaultOp = "";
    for (int i = 0; i < op.size(); i++)
    { defaultOp = 
        defaultOp + (String) op.get(i) + " "; }
    defaultCond = c;
    defaultEffect = e;
    defaultisSys = isSys;
    defaultisCrit = isCrit;
    dialogPanel.setOldFields(defaultOp, c,e,isSys,isCrit); }

  public void setFields(String op, String c, String e,
                        boolean isSys, boolean isCrit)
  { StringTokenizer st = new StringTokenizer(op);
    newOp = new Vector(); 

    if (op == null)
    { newOp = null; }
    else 
    { while (st.hasMoreTokens())
      { newOp.add((String) st.nextToken()); }
    }

    newCond = c;
    newEffect = e;
    newisSys = isSys;
    newisCrit = isCrit; }
 
  public Vector getOp() { return newOp; }

  public String getCond() { return newCond; }

  public String getEffect() { return newEffect; }

  public boolean isSystem() { return newisSys; }

  public boolean isCritical() { return newisCrit; }

  class ButtonHandler implements ActionListener
  { public void actionPerformed(ActionEvent ev)
    { JButton button = (JButton) ev.getSource();
      String label = button.getText();
      if ("Ok".equals(label))
      { setFields(dialogPanel.getOp(),
                  dialogPanel.getCond(),
                  dialogPanel.getEffect(),
                  dialogPanel.isSystem(),
                  dialogPanel.isCritical()); }
      else 
      { setFields(null, null, null, true, false); }
      dialogPanel.reset();
      setVisible(false); 
    } 
  }
}

class TempInvDialogPanel extends JPanel
{ JLabel opLabel;
  JTextField opField;  /* Modal ops */
  JLabel condLabel;
  JTextField condField;
  JLabel effectLabel;
  JTextField effectField;   

  JPanel envPanel;
  ButtonGroup bg1;
  JCheckBox systemInv;
  JCheckBox environmentInv;

  JPanel critPanel;
  ButtonGroup bg2;
  JCheckBox noncriticalInv;
  JCheckBox criticalInv;

  TempInvDialogPanel()
  { envPanel = new JPanel();
    bg1 = new ButtonGroup();
    systemInv = new JCheckBox("System", true);
    environmentInv = 
      new JCheckBox("Environment");
    bg1.add(systemInv);
    bg1.add(environmentInv);
    envPanel.add(systemInv);
    envPanel.add(environmentInv);
    envPanel.setBorder(
      BorderFactory.createTitledBorder(
        "System Requirement or Environment " + 
        "Assumption?"));
    
    critPanel = new JPanel();
    bg2 = new ButtonGroup();
    noncriticalInv = 
      new JCheckBox("Non-critical", true);
    criticalInv = 
      new JCheckBox("Critical");
    bg2.add(noncriticalInv);
    bg2.add(criticalInv);
    critPanel.add(noncriticalInv);
    critPanel.add(criticalInv);
    critPanel.setBorder(
      BorderFactory.createTitledBorder(
        "Critical (eg: Safety) or " + 
        "Non-critical?"));
    
    opLabel = new JLabel("Modalities:");
    opField = new JTextField();
    condLabel = new JLabel("Assumption:");
    condField = new JTextField();
    effectLabel = new JLabel("Conclusion:");
    effectField = new JTextField();
    setBorder(
      BorderFactory.createTitledBorder(
       "Temporal invariant: " +
       "Assumption => Modalities(Conclusion)")); 
    add(condLabel);
    add(condField);
    add(opLabel);
    add(opField);
    add(effectLabel);
    add(effectField); 
    add(envPanel);
    add(critPanel); }

  public void setOldFields(String op, String c, 
                           String e, boolean isSys,
                           boolean isCrit)
  { opField.setText(op);
    condField.setText(c);
    effectField.setText(e);
    systemInv.setSelected(isSys);
    criticalInv.setSelected(isCrit); }

  public Dimension getPreferredSize()
  { return new Dimension(410,250); }

  public Dimension getMinimumSize()
  { return new Dimension(410,250); }

  public void doLayout()
  { condLabel.setBounds(10,10,90,30);
    condField.setBounds(100,15,300,20);
    opLabel.setBounds(10,40,90,30);
    opField.setBounds(100,45,300,20);
    effectLabel.setBounds(10,70,90,30);
    effectField.setBounds(100,75,300,20); 
    envPanel.setBounds(10,110,390,50); 
    critPanel.setBounds(10,170,390,50); }

  
  public void reset()
  { opField.setText("");
    condField.setText("");
    effectField.setText("");
    systemInv.setSelected(true);
    criticalInv.setSelected(false); }

  public String getOp()
  { return opField.getText(); }

  public String getCond()
  { return condField.getText(); }

  public String getEffect()
  { return effectField.getText(); }
  
  public boolean isSystem()
  { return systemInv.isSelected(); }

  public boolean isCritical()
  { return criticalInv.isSelected(); }
}









