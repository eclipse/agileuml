/*
      * Classname : EvtnameDialog
      * 
      * Version information : 1
      *
      * Date : 
      * 
      * Description: This class describes the dialog box that allows the user to enter
	* the name of the event and check that its more than one character long.
      */
/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

class EvtNameDialog extends JDialog {
    private String typedText = null;   //the text entered by the user

    private JOptionPane optionPane;

    public String getValidatedText() {
	  System.out.println("Got event ==> " + typedText);
        return typedText;
    }

    public EvtNameDialog(Frame aFrame) {
        super(aFrame, true);

        setTitle("Add an event to eventlist");

        final String msgString1 = "Enter event name:";
        final JTextField textField = new JTextField(15);
        Object[] array = {msgString1, textField};

        final String btnString1 = "Enter";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(array, 
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(btnString1);
            }
        });

        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible() 
                 && (e.getSource() == optionPane)
                 && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
                     prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    // Reset the JOptionPane's value.
                    // If you don't do this, then if the user
                    // presses the same button next time, no
                    // property change event will be fired.
                    optionPane.setValue(
                            JOptionPane.UNINITIALIZED_VALUE);

			  //if "Enter" has been pressed 
                    if (value.equals(btnString1)) {
                            typedText = textField.getText();
                       // String ucText = typedText.toUpperCase();
                       // if (ucText.equals(magicWord)) {
				  if (typedText.length() > 1) {
                            // we're done; dismiss the dialog
                            setVisible(false);
                        } else { 
                            // text was invalid
                            textField.selectAll();
                            JOptionPane.showMessageDialog(
                                            EvtNameDialog.this,
                                            "Sorry, \"" + typedText + "\" "
                                            + "event name too short.\n",
                                            "Please enter again",
                                            JOptionPane.ERROR_MESSAGE);
                            typedText = null;
                        }
                    } else { // user closed dialog or clicked cancel
                      /*  dd.setLabel("It's OK.  "
                                 + "We won't force you to type "
                                 + magicWord + ".");*/
                        typedText = null;
                        setVisible(false);
                    }
                }
            }
        });
    }
}
