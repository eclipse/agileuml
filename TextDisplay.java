import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class TextDisplay extends JFrame  
{  // the area for text output:
  private JTextArea text = new JTextArea(); 
  
  public TextDisplay() 
  { addWindowListener(     /* Kills display when */
      new WindowAdapter()  /* window is closed.      */ 
      { public void windowClosing(WindowEvent e) 
        { setVisible(false);
          dispose();
        }   
      });  

    Container contentPane = getContentPane();
    
    JScrollPane scroller = 
      new JScrollPane(text,      
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroller.setPreferredSize(new Dimension(300,600));
    contentPane.add(scroller, BorderLayout.CENTER);
    setSize(300,400);
    show();
  }

  public TextDisplay(String title, String file)
  { this();
    setTitle(title);
    text.setFont(new Font("Serif",Font.BOLD,18)); 
    displayFile(file);
  }

  private void displayFile(String file)
  { try 
    { FileReader fr = new FileReader(file);
      text.read(fr,null);
      fr.close();
    }
    catch(IOException e)
    { System.err.println("Cannot access " + file); }
  }

  public static void main(String[] args)
  { TextDisplay td = new TextDisplay("Java code",
                            "TextDisplay.java");
  }
}








