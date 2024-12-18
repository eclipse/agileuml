import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.EventObject;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.*; 

public class ExDialog extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ExDialogPanel dialogPanel;
  private Vector defaultValues; // of String
  private Map newValues; // String --> String

  public ExDialog(JFrame owner,String title,
                  Vector labels)
  { super(owner, true);  // modal dialog
    setTitle(title);
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ExDButtonHandler bHandler = new ExDButtonHandler();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ExDialogPanel(labels);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
  }

  public void setOldFields(Vector values)
  { defaultValues = values;
    dialogPanel.setOldFields(values); 
  } // Assume they are in same order as dialog fields

  public void setFields(Map fvalues)
  { newValues = fvalues; }

  public void resetFields()
  { newValues = new HashMap(); }

  public String getField(String f) 
  { return (String) newValues.get(f); }


 class ExDButtonHandler implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { setFields(dialogPanel.getMap()); }
     else 
     { resetFields(); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}


class ExDialogPanel extends JPanel
{ private Vector labels = new Vector();  // of JLabel
  private Vector texts = new Vector();   // of JTextField
  private static final int LABELHEIGHT = 30;
  private static final int FIELDHEIGHT = 20;
  private static final int LABELWIDTH = 70;
  private static final int FIELDWIDTH = 300;

  public ExDialogPanel(Vector fields)
  { for (int i = 0; i < fields.size(); i++)
    { String field = (String) fields.get(i);
      JLabel nameLabel = new JLabel(field);
      labels.add(nameLabel); 
      JTextField nameField = new JTextField();
      texts.add(nameField); 
      add(nameLabel);
      add(nameField);
      setPreferredSize(new Dimension(LABELWIDTH+FIELDWIDTH+30,
                                     20 + LABELHEIGHT*labels.size())); 
    }
  }

  public void setOldFields(Vector values)
  { for (int i = 0; i < values.size(); i++)
    { String val = (String) values.get(i);
      JTextField field = (JTextField) texts.get(i);
      field.setText(val);
    }
  }

  public Dimension getPreferredSize()
  { int depth = 20 + LABELHEIGHT*labels.size();
    return new Dimension(LABELWIDTH+FIELDWIDTH+30,
                         depth); 
  }

  public Dimension getMinimumSize()
  { return getPreferredSize(); }

  public void doLayout()
  { int x = 10;
    int y = 10;
    for (int i = 0; i < labels.size(); i++)
    { JLabel lab = (JLabel) labels.get(i);
      lab.setBounds(x,y,LABELWIDTH,LABELHEIGHT);
      JTextField fld = (JTextField) texts.get(i);
      fld.setBounds(x+LABELWIDTH,y+5,
                    FIELDWIDTH,FIELDHEIGHT);
      y += LABELHEIGHT;
    }
  }

  public void reset()
  { for (int i = 0; i < texts.size(); i++)
    { JTextField fld = (JTextField) texts.get(i);
      fld.setText("");
    }
  }

  public Map getMap() // returns fld |-> val pairs
  { Map res = new HashMap();
    for (int i = 0; i < labels.size(); i++)
    { String lab = ((JLabel) labels.get(i)).getText();
      String val = 
        ((JTextField) texts.get(i)).getText();
      res.put(lab,val);
    }
    return res;
  }
}  

