import java.io.*; 
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Vector; 

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/* K. Lano 2010-2018
   Adapted from Oracle example of JTextPane
   https://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html

Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
   Package: GUI
*/ 

public class UseCaseEditor extends JFrame 
{
    JTextPane textPane;
    AbstractDocument doc;
    static final int MAX_CHARACTERS = 600;
    JTextArea messageArea;
    String newline = "\n";
    HashMap actions;
    UseCase useCase = null; 
    Vector entities = null; 
    String ownerName = ""; 
    Vector ownerNames = new Vector(); // String
    Vector cons = new Vector(); // Constraint
    Vector invs = new Vector(); // SafetyInvariant
    Vector ops = new Vector();  // BehaviouralFeature - all static for use cases
    UCDArea classArea = null; 

    public UseCaseEditor(UCDArea parent, UseCase uc, Vector ents) 
    {
        super("Use Case Editor, editing: " + uc);
        useCase = uc; 
        entities = ents; 
        classArea = parent; 

        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5,5,5,5));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
        } else {
            System.err.println("Error: invalid document");
        }
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(100, 250));

        messageArea = new JTextArea(15, 80);
        messageArea.setEditable(false);
        JScrollPane scrollPaneForLog = new JScrollPane(messageArea);

        JSplitPane splitPane = new JSplitPane(
                                       JSplitPane.VERTICAL_SPLIT,
                                       scrollPane, scrollPaneForLog);
        splitPane.setOneTouchExpandable(true);

        JPanel statusPane = new JPanel(new GridLayout(1, 1));

        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusPane, BorderLayout.PAGE_END);

        actions = createActionTable(textPane);
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu);
        mb.add(styleMenu);
        setJMenuBar(mb);

        initDocument();
        textPane.setCaretPosition(0);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public Vector getInvs() 
    { return invs; } 

    protected void addBindings() {
        InputMap inputMap = textPane.getInputMap();

        //Ctrl-b to go backward one character
        // KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        // inputMap.put(key, DefaultEditorKit.backwardAction);

        //Ctrl-f to go forward one character
        // key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
        // inputMap.put(key, DefaultEditorKit.forwardAction);

        //Ctrl-p to go up one line
        // key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
        // inputMap.put(key, DefaultEditorKit.upAction);

        //Ctrl-n to go down one line
        // key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
        // inputMap.put(key, DefaultEditorKit.downAction);
    }

    protected JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        Action checkAction = new CheckAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
        menu.add(checkAction); 

        Action saveAction = new SaveAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
        menu.add(saveAction); 
        // menu.add(getActionByName(DefaultEditorKit.loadFileAction));

        Action loadAction = new LoadAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
        menu.add(loadAction); 


        menu.addSeparator();

        menu.add(getActionByName(DefaultEditorKit.cutAction));
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.pasteAction));

        return menu;
    }

    protected JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        return menu;
    }

    protected void initDocument() {
        String initString[] =
                { "Enter use case postconditions as:",
                  "ContextClass::",
                  "Condition => Effect",
                  "The ContextClass can be omitted, but include ::"};

        SimpleAttributeSet[] attrs = initAttributes(initString.length);

        /* try {
            for (int i = 0; i < initString.length; i ++) {
                doc.insertString(doc.getLength(), initString[i] + newline,
                        attrs[i]);
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        } */ 
        if (useCase != null) 
        { try 
          { // doc.insertString(0, "use case: " + useCase.getName() + newline + newline, attrs[1]); 
            java.util.Vector atts = useCase.getOwnedAttribute();
            for (int j = 0; j < atts.size(); j++) 
            { Attribute att = (Attribute) atts.get(j); 
              doc.insertString(doc.getLength(), att.getName() + " : " + att.getType() + "\n\r", attrs[1]); 
            }


            doc.insertString(doc.getLength(), "\n\r", attrs[1]); 

            java.util.Vector ops = useCase.getOperations(); 
            for (int k = 0; k < ops.size(); k++) 
            { BehaviouralFeature bf = (BehaviouralFeature) ops.get(k);
              doc.insertString(doc.getLength(), useCase.getName() + "::\n\r" + bf.displayForUC(), attrs[1]);  
            }    

            doc.insertString(doc.getLength(), "\n\r", attrs[1]); 

            java.util.Vector posts = useCase.getPostconditions(); 
            for (int i = 0; i < posts.size(); i++) 
            { Constraint cn = (Constraint) posts.get(i); 
              doc.insertString(doc.getLength(), cn.ucToString(), attrs[1]); 
            }
          }  
          catch (Exception ble) 
          { System.err.println("Couldn't insert initial text."); }
        } 
    }

    protected SimpleAttributeSet[] initAttributes(int length) {
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

        attrs[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs[0], "SansSerif");
        StyleConstants.setFontSize(attrs[0], 16);

        attrs[1] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setBold(attrs[1], true);

        attrs[2] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setItalic(attrs[2], true);

        attrs[3] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[3], 20);

        return attrs;
    }

    private HashMap createActionTable(JTextComponent textComponent) {
        HashMap actions = new HashMap();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
	return actions;
    }

   public int getConstraintStart(String txt, int en)
   { int st = 0; 
     StringBuffer line = new StringBuffer(); 
     StringBuffer conbuffer = new StringBuffer(); 

     for (int i = 0; i < en; i++)
     { char c = txt.charAt(i); 
       if (c == '\n')
       { String ls = line.toString(); 
         String tls = ls.trim(); 
         if (tls.endsWith("::"))
         { st = i+1; 
           int j = tls.indexOf(':'); 
           ownerName = tls.substring(0,j); 
           ownerNames.add(ownerName); 
           // System.out.println(ownerNames); 
           cons.add(conbuffer.toString().trim()); 
           // System.out.println("CONSTRAINT: " + conbuffer); 
           conbuffer = new StringBuffer(); 
         } 
         else 
         { conbuffer.append(line.toString() + " "); } 
         line = new StringBuffer(); 
       } 
       else if (c != '\r') 
       { line.append(c); } 
     } 

     conbuffer.append(line.toString()); 
     cons.add(conbuffer.toString()); 
     // System.out.println("CONSTRAINT: " + cons); 
           
     return st; 
   } 

    private Action getActionByName(String name) {
        return (Action) actions.get(name);
    }

  class CheckAction extends AbstractAction
  { public CheckAction()
    { super("Check"); }

    public void actionPerformed(ActionEvent e)
    { int pos = textPane.getCaretPosition();
      if (pos == 0) { return; }
      Compiler2 comp = new Compiler2();
      try 
      { String txt = textPane.getText(0, pos+1);
        int st = getConstraintStart(txt,pos); 
        textPane.select(st,pos);
        txt = textPane.getText(st, pos-st); 
        comp.nospacelexicalanalysis(txt.trim());
        Expression ee = comp.parse(messageArea);
        if (ee == null)
        { BehaviouralFeature bf = comp.operationDefinition(entities, new Vector()); 
          if (bf == null) 
          { textPane.setSelectedTextColor(Color.red); }
          else 
          { messageArea.append("Valid operation definition\n\r"); 
            textPane.setSelectedTextColor(Color.green); 
            return; 
          }
        } 
        else
        { textPane.setSelectedTextColor(Color.green); }
        Vector antesymbs = new Vector(); 
        antesymbs.add("|");  
        Vector messages = new Vector();
        Entity owner = (Entity) ModelElement.lookupByName(ownerName, entities);
        if (owner == null) 
        { messageArea.append("Missing or invalid entity name: " + ownerName + "\n\r"); }   
        comp.checkSyntax(owner,entities,antesymbs,messages); 
        if (messages.size() > 0)
        { for (int h = 0; h < messages.size(); h++) 
          { String mess = (String) messages.get(h); 
            messageArea.append(mess + "\n\r"); 
          } 
        }       
      } catch (Exception ee) { } 
    }
  }

 class SaveAction extends AbstractAction
  { public SaveAction()
    { super("Save"); }

    public void actionPerformed(ActionEvent e)
    { int pos = doc.getLength(); 
      // System.out.println("Document length = " + pos); 
      int pos2 = textPane.getCaretPosition();
      // System.out.println("Caret position = " + pos2); 
      if (pos2 > pos) 
      { pos = pos2; } 
      if (pos == 0) { return; }
      ownerNames.clear(); 
      cons.clear(); 
      String txt = ""; 
      try 
      { txt = textPane.getText(0, pos+1); } 
      catch(Exception _e) { return; } 

      File file = new File("output/_" + useCase.getName() + ".txt");  /* default */ 
      try
      { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
        out.println(txt); 
        out.close(); 
      }
      catch (IOException ioe) 
      { System.out.println("Error saving data"); } 
      
      getConstraintStart(txt,pos); 
            
      useCase.clearPostconditions();
      String attdecs = (String) cons.get(0);  
      Compiler2 cc = new Compiler2(); 
      if (attdecs != null && attdecs.length() > 0)
      { cc.nospacelexicalanalysis(attdecs); 
        Vector atts = cc.parseAttributeDecs(entities,new Vector()); 
        if (atts != null && atts.size() > 0)
        { useCase.setAttributes(atts); } 
      }  

      for (int i = 0; i < ownerNames.size(); i++) 
      { System.out.println("Parsing feature/constraint for " + ownerNames.get(i)); 
        System.out.println("Parsing feature/constraint " + cons.get(i+1)); 
        Compiler2 comp = new Compiler2(); 
        comp.nospacelexicalanalysis((String) cons.get(i+1));
        
        Expression inv = comp.parse(); 
        if (inv != null) 
        { if (inv instanceof BinaryExpression) 
          { BinaryExpression be = (BinaryExpression) inv; 
            if ("=>".equals(be.operator))
            { Expression eAssump = be.getLeft(); 
              Expression eConc = be.getRight(); 
              Invariant i2 = new SafetyInvariant(eAssump,eConc);
              i2.setOwnerText((String) ownerNames.get(i)); 
              classArea.addUseCasePostcondition(useCase,i2); 
              invs.add(i2); 
            } 
            else 
            { Expression eAssump = new BasicExpression(true); 
              Expression eConc = inv; 
              Invariant i2 = new SafetyInvariant(eAssump,eConc);
              i2.setOwnerText((String) ownerNames.get(i)); 
              classArea.addUseCasePostcondition(useCase,i2); 
              invs.add(i2); 
            }
          }
        }
        else // it should be an operation definition
        { BehaviouralFeature bf = comp.operationDefinition(entities, new Vector()); 
          if (bf == null) 
          { System.out.println("Failed to parse operation"); } 
          else 
          { System.out.println("Parsed operation " + bf.display()); 
            useCase.replaceOperation(bf); 
          } 
        }          
      }
      setVisible(false); 
    } 
  } 

  class LoadAction extends AbstractAction
  { public LoadAction()
    { super("Load from file"); }

    public void actionPerformed(ActionEvent e)
    { File file = new File("output/_" + useCase.getName() + ".txt");  /* default */ 
      // doc = new StyledDocument(file); 
      try
      { FileInputStream br = new FileInputStream(file); 
        textPane.read(br,file); 
      }
      catch (Exception _e)
      { System.out.println("Problem loading file: " + file);
        return; 
      }
    } 
  } 

  /*   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    } */ 
}

