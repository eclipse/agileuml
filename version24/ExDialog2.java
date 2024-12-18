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

public class ExDialog2 extends JDialog
{ private JPanel bottom;
  private JButton okButton, cancelButton;
  private ExDialogPanel2 dialogPanel;
  private String[] defaultValues; 
  private String[] newValues; 

  // Assumes arguments are non-null
  public ExDialog2(JFrame owner,String title,
                   String[] labels, String[] res)
  { super(owner, title, true);  // modal dialog
    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    ExDButtonHandler2 bHandler = new ExDButtonHandler2();
    okButton.addActionListener(bHandler);
    cancelButton.addActionListener(bHandler);
    bottom = new JPanel();
    bottom.add(okButton);
    bottom.add(cancelButton);
    bottom.setBorder(BorderFactory.createEtchedBorder());
    dialogPanel = new ExDialogPanel2(labels);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(bottom, BorderLayout.SOUTH);
    getContentPane().add(dialogPanel, BorderLayout.CENTER); 
    newValues = res; 
    defaultValues = new String[res.length]; 
    for (int i = 0; i < res.length; i++) 
    { defaultValues[i] = ""; } 
  }

  public void setOldFields(String[] values)
  { defaultValues = values;
    dialogPanel.setOldFields(values); 
  } // Assume they are in same order as dialog fields

  public void setFields(String[] fvalues)
  { newValues = fvalues; }

  public void resetFields()
  { newValues = (String[]) defaultValues.clone(); }

  public String getField(int i) 
  { return newValues[i]; }


 class ExDButtonHandler2 implements ActionListener
 { public void actionPerformed(ActionEvent ev)
   { JButton button = (JButton) ev.getSource();
     String label = button.getText();
     if ("Ok".equals(label))
     { dialogPanel.setFields(newValues); }
     else 
     { resetFields(); }
 
     dialogPanel.reset();
     setVisible(false); 
   } 
 }
}


class ExDialogPanel2 extends JPanel
{ private JLabel[] labels; 
  private JTextField[] texts;  
  private static final int LABELHEIGHT = 30;
  private static final int FIELDHEIGHT = 20;
  private static final int LABELWIDTH = 70;
  private static final int FIELDWIDTH = 300;

  public ExDialogPanel2(String[] fields)
  { labels = new JLabel[fields.length]; 
    texts = new JTextField[fields.length];
    for (int i = 0; i < fields.length; i++)
    { JLabel nameLabel = new JLabel(fields[i]);
      labels[i] = nameLabel; 
      JTextField nameField = new JTextField();
      texts[i] = nameField; 
      add(nameLabel);
      add(nameField);
    } 
    setPreferredSize(new Dimension(LABELWIDTH+FIELDWIDTH+30,
                                   20 + LABELHEIGHT*labels.length)); 
  }

  // Assumes: values != null && values.length <= texts.length 
  public void setOldFields(String[] values)
  { for (int i = 0; i < texts.length; i++)
    { JTextField field = texts[i];
      field.setText(values[i]);
    }
  }

  public Dimension getPreferredSize()
  { int depth = 20 + LABELHEIGHT*labels.length;
    return new Dimension(LABELWIDTH+FIELDWIDTH+30,
                         depth); 
  }

  public Dimension getMinimumSize()
  { return getPreferredSize(); }

  public void doLayout()
  { int x = 10;
    int y = 10;
    for (int i = 0; i < labels.length; i++)
    { JLabel lab = labels[i];
      lab.setBounds(x,y,LABELWIDTH,LABELHEIGHT);
      JTextField fld = texts[i];
      fld.setBounds(x+LABELWIDTH,y+5,
                    FIELDWIDTH,FIELDHEIGHT);
      y += LABELHEIGHT;
    }
  }

  public void reset()
  { for (int i = 0; i < texts.length; i++)
    { JTextField fld = texts[i];
      fld.setText("");
    }
  }

  public void setFields(String[] res) 
  { for (int i = 0; i < texts.length; i++) 
    { res[i] = texts[i].getText(); } 
  } 
}  

