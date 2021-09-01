import java.io.*; 
import java.awt.*;
import java.awt.Color; 
import java.awt.BorderLayout; 
import java.awt.GridLayout; 
import java.awt.Insets; 
import java.awt.Dimension; 
import java.awt.event.ActionEvent; 
import java.awt.event.*;
import java.util.HashMap;
import java.util.Vector; 

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/* K. Lano 2010-2021
   
  Adapted from Oracle example of JTextPane

 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
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

public class KM3Editor extends JFrame implements DocumentListener
{
    JTextPane textPane;
    AbstractDocument doc;
    static final int MAX_CHARACTERS = 1000;
    JTextArea messageArea;
    String newline = "\n";
    HashMap actions;

    String systemName = "app"; 
    Vector entities = new Vector(); // of Entity 
    Vector km3types = new Vector(); 
    Vector types = new Vector(); 
    Vector useCases = new Vector(); // of UseCase

    String ownerName = ""; 
    Vector ownerNames = new Vector(); // String
    Vector cons = new Vector(); // Constraint
    Vector invs = new Vector(); // SafetyInvariant
    Vector ops = new Vector();  // BehaviouralFeature - all static for use cases
    UCDArea classArea = null; 
    ActivityEditDialog actDialog; 
    private JLabel thisLabel;

    public KM3Editor(UCDArea parent, Vector ents, Vector ucs) 
    {
        super("KM3 Editor");
        systemName = parent.getSystemName(); 
        entities = ents; 
        km3types = parent.getKM3Types(); 
        types = new Vector(); 
        types.addAll(parent.getTypes()); 
        useCases = ucs; 

        classArea = parent; 

        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5,5,5,5));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) 
        { doc = (AbstractDocument) styledDoc;
          doc.addDocumentListener(this); 
        } 
	   else 
	   {
          System.err.println("Error: invalid document");
        }
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(100, 300));

        messageArea = new JTextArea(10, 50);
        messageArea.setEditable(false);
        JScrollPane scrollPaneForLog = new JScrollPane(messageArea);

        JSplitPane splitPane = new JSplitPane(
                       JSplitPane.VERTICAL_SPLIT,
                       scrollPane, scrollPaneForLog);
        splitPane.setOneTouchExpandable(true);

        JPanel statusPane = new JPanel(new GridLayout(2, 1));

        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusPane, BorderLayout.EAST);
        thisLabel = 
          new JLabel("Type & click within the framed area.");
        getContentPane().add(thisLabel, java.awt.BorderLayout.SOUTH); 

        actions = createActionTable(textPane);
        JMenu fileMenu = createFileMenu(); 
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(fileMenu); 
        mb.add(editMenu);
        mb.add(styleMenu);
        setJMenuBar(mb);

        initDocument();
        textPane.setCaretPosition(0);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // thisLabel.setText()

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

    protected JMenu createFileMenu() 
    { JMenu menu = new JMenu("File");

      Action loadAction = new LoadAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(loadAction); 
        
      Action saveAction = new SaveAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(saveAction); 
      return menu; 
   } 

    protected JMenu createEditMenu() 
    { JMenu menu = new JMenu("Edit");

      Action checkAction = new CheckAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      JMenuItem checkMI = menu.add(checkAction); 
      checkMI.setToolTipText(
           "First, position cursor under item to be checked");
            // checkAction.setMnemonic(KeyEvent.VK_K);
        
      Action searchAction = new SearchAction(); 
      menu.add(searchAction); 

      Action addCodeAction = new AddCodeAction(); 
      menu.add(addCodeAction); 

      menu.addSeparator();

      Action cAction = getActionByName(DefaultEditorKit.cutAction);
        // cAction.setValue(Action.NAME,"Cut");  
      JMenuItem cutMI = menu.add(cAction);
      cutMI.setLabel("Cut"); 
      
      Action cpyAction = getActionByName(DefaultEditorKit.copyAction);
      JMenuItem copyMI = menu.add(cpyAction); 
      copyMI.setLabel("Copy"); 
      
      Action pteAction = getActionByName(DefaultEditorKit.pasteAction);
      JMenuItem pasteMI = menu.add(pteAction); 
      pasteMI.setLabel("Paste"); 

      return menu;
    }

    protected JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        return menu;
    }

    protected void initDocument() 
    {
      String initString[] =
                { "Enter classes in KM3 format. Write use case postconditions as:",
                  "ContextClass::",
                  "Condition => Effect;",
                  "The ContextClass can be omitted, but include ::"};

      SimpleAttributeSet[] attrs = initAttributes(initString.length);

        /* try {
            for (int i = 0; i < initString.length; i++) 
            { doc.insertString(doc.getLength(), initString[i] + newline,
                        attrs[i]);
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        } */ 

       String sysName = "app"; 
       if (systemName != null && !systemName.equals(""))
       { sysName = systemName; }
	   
       try 
       { doc.insertString(0, "package " + sysName + " { \n\r\n\r" , attrs[1]); 
         
         for (int j = 0; j < km3types.size(); j++) 
         { String typ = (String) km3types.get(j);
		   // System.out.println(typ.class);  
           doc.insertString(doc.getLength(), typ, attrs[1]);  
         }
		 
         for (int j = 0; j < entities.size(); j++) 
         { Entity ent = (Entity) entities.get(j); 
           if (ent.isDerived()) { } 
	     else 
           { doc.insertString(doc.getLength(), ent.getKM3() + "\n\r\n\r", attrs[1]); } 
          }

           doc.insertString(doc.getLength(), "\n\r", attrs[1]); 
           Vector seen = new Vector(); 

           for (int j = 0; j < useCases.size(); j++) 
           { UseCase uc = (UseCase) useCases.get(j); 
             doc.insertString(doc.getLength(), uc.getKM3(seen) + "\n\r\n\r", attrs[1]); 
           }

           doc.insertString(doc.getLength(), "}\n\r", attrs[1]); 

      }  
      catch (Exception ble) 
      { System.err.println("Couldn't insert initial text."); }
   } 

    protected SimpleAttributeSet[] initAttributes(int length) 
    {
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

    private HashMap createActionTable(JTextComponent textComponent) 
    { HashMap actions = new HashMap();
      Action[] actionsArray = textComponent.getActions();
      for (int i = 0; i < actionsArray.length; i++) 
      { Action a = actionsArray[i];
        actions.put(a.getValue(Action.NAME), a);
      }
      return actions;
    }

   public int getClassStart(String txt, int en)
   { int st = 0; 
     int linestart = 0; // start position of the current line

     int linecount = 0; // line count of current line. 

     StringBuffer line = new StringBuffer(); 
     StringBuffer conbuffer = new StringBuffer(); 

     for (int i = 0; i < en; i++)
     { char c = txt.charAt(i); 
       if (c == '\n')
       { linecount++; 
         String ls = line.toString(); 
         String tls = ls.trim(); 
         if (tls.startsWith("abstract class") || 
             tls.startsWith("class") || 
             tls.startsWith("usecase") || 
             tls.startsWith("enumeration") || 
             tls.startsWith("datatype"))
         { st = linestart; 
           // int j = tls.indexOf(':'); 
           // ownerName = tls.substring(0,j); 
           // ownerNames.add(ownerName); 
           // System.out.println(ownerNames); 
           cons.add(conbuffer.toString()); 
           // System.out.println("CLASS: " + conbuffer); 
           conbuffer = new StringBuffer();
           conbuffer.append(tls); 
         } 
         else 
         { conbuffer.append(line.toString() + " "); } 
         line = new StringBuffer(); 
         linestart = i+1; 
       } 
       else if (c != '\r') 
       { line.append(c); } 
     } 

     thisLabel.setText("Line number: " + (linecount+1) + " Position: " + linestart); 

     conbuffer.append(line.toString()); 
     cons.add(conbuffer.toString()); 
     // System.out.println("CONSTRAINT: " + cons); 
           
     return st; 
   } 

    private Action getActionByName(String name) 
    { return (Action) actions.get(name); }
	
	public void changedUpdate(DocumentEvent e)
	{ int offset = e.getOffset(); 
	  int pos = textPane.getCaretPosition();
        // System.out.println("Update event at: " + offset + " " + pos); 
	}

	public void insertUpdate(DocumentEvent e)
	{ int offset = e.getOffset(); 
	  int pos = textPane.getCaretPosition();
        
	  try 
	  { String ch = textPane.getText(pos,1); 
	    // System.out.println("Insert event at: " + offset + " " + pos + " " + textPane.getText(pos,1)); 

         String txt = textPane.getText(0,pos); 
         
         char cc = ch.charAt(0); 
         if (Character.isLetterOrDigit(cc))
         { // look back to see what is the identifier/number
           String curriden = Compiler2.matchPrecedingOperator(cc,txt,txt.length()); 
           // System.out.println(">>> Identifier: " + curriden); 
           if (curriden != null && curriden.length() > 0)
           { String[] mess = {""}; 
             String keywd = Compiler2.isKeywordOrPart(curriden,mess); 
             if (keywd != null) 
             { thisLabel.setText(">>> Keyword/operator: " + keywd); 
               if (mess[0] != null && mess[0].length() > 0)
               { messageArea.setText(mess[0] + "\n\r"); } 
             } 
             else 
             { String idendefinition = Compiler2.findIdentifierDefinition(curriden,txt); 
               if (idendefinition != null && idendefinition.length() > 0) 
               { thisLabel.setText(">>> Identifier " + curriden + " defined at " + idendefinition); } 
             } 
           }  
         } 
         // else if (':' == cc)
         // { messageArea.setText("iden : Type for data declaration or\n  ::\n  P => Q;\n for usecase postcondition\n\r"); } 
         else if ('[' == cc)
         { messageArea.setText("col[val]\n access to val-indexed element of sequence or map col for read or update.\nEquivalent to col->at(val) for read access.\n\r"); } 


         Vector errors = new Vector();
         Vector colours = new Vector();
  
         if ("}".equals(ch) || ")".equals(ch) || "]".equals(ch))
         { int posb = Compiler2.matchPrecedingBracket(ch,txt,errors,colours);
		// System.out.println("Preceding bracket at: " + posb); 
           if (posb < 0) 
           { messageArea.append("No matching opening bracket for " + ch + "!\n"); } 
           else 
           { if (errors.size() > 0)
             { String err = (String) errors.get(0); 
         
               messageArea.append(err + "\n\r");
               Color colr = (Color) colours.get(0);   
               if (colr == Color.red)
               { textPane.setSelectedTextColor(colr); }  
		  // textPane.setCaretPosition(pos);
             } 
            }
          } 
	   } catch (Exception _e) { } 
    }

	public void removeUpdate(DocumentEvent e)
	{ int offset = e.getOffset(); 
	  int pos = textPane.getCaretPosition();
      // System.out.println("Remove event at: " + offset + " " + pos); 
	}

  class AddCodeAction extends javax.swing.AbstractAction
  { public AddCodeAction()
    { super("Add Code"); } 

    public void actionPerformed(ActionEvent e)
    { int pos = textPane.getCaretPosition(); 
      if (pos == 0) { return; } 

      if (actDialog == null)
      { actDialog = new ActivityEditDialog(classArea.parent);
        actDialog.pack();
        actDialog.setLocationRelativeTo(classArea.parent);
      }
      actDialog.setOldFields("","","","","",true);
      actDialog.setVisible(true);
   
      String post = actDialog.getPost(); 
      Compiler2 comp = new Compiler2(); 
      if (post == null)
      { System.out.println(">>>>> Invalid AST text: " + post); 
        return; 
      }

      ASTTerm xx = comp.parseGeneralAST(post);
      if (xx == null)
      { System.out.println(">>>>> Invalid AST text: " + post); 
        return; 
      }

      ASTTerm.enumtypes = new Vector(); 
      ASTTerm.enumtypes.addAll(types); 
      ASTTerm.entities = new Vector(); 
      ASTTerm.entities.addAll(entities); 

      String km3code = xx.toKM3(); 
      System.out.println(">>>>> Translated code: " + km3code); 
        
      SimpleAttributeSet[] attrs = initAttributes(4);

      try {
            doc.insertString(pos, km3code, attrs[1]);
          }
          catch (BadLocationException ble) {
            System.err.println("Couldn't insert code text.");
        } 
    } 
  } 

  class CheckAction extends javax.swing.AbstractAction
  { public CheckAction()
    { super("Check"); }

    public void actionPerformed(ActionEvent e)
    { int pos = textPane.getCaretPosition();
      if (pos == 0) { return; }
      Compiler2 comp = new Compiler2();
      try 
      { String txt = textPane.getText(0, pos+1);
        int st = getClassStart(txt,pos); 
        textPane.select(st,pos);
        txt = textPane.getText(st, pos-st); 
        comp.nospacelexicalanalysis(txt);

        // Vector ents = new Vector(); 
        // Vector typs = new Vector(); 
        Vector pregens = new Vector(); 
        Vector preassocs = new Vector(); 
        Vector errors = new Vector(); 

        Object cls = comp.parseKM3classifier(entities,types,pregens,preassocs,errors);
        // classArea.processKM3(ents,typs,pregens,preassocs,items); 

        System.out.println(">>> Types = " + types); 

        if (cls != null && errors.size() == 0) 
        { textPane.setSelectedTextColor(Color.green);  
 
          if (cls instanceof Entity) 
          { Entity clsx = (Entity) cls; 
		 System.out.println("PARSED class:\n" + clsx.getKM3());
            
            for (int q = 0; q < preassocs.size(); q++) 
            { PreAssociation pa = (PreAssociation) preassocs.get(q);  
              Entity e1 =
                (Entity) ModelElement.lookupByName(pa.e1name,entities);
              Entity e2 =
                (Entity) ModelElement.lookupByName(pa.e2name,entities);
      
	          // if (e1 == null)
			  // { e1 = (Entity) cls; }
              // Association ast = e1.getDefinedRole(pa.role2); 
			  // if (ast == null) 
              Association ast = new Association(clsx,e2,pa.role2);   
              ast.setEntity1(clsx); 
              ast.setEntity2(e2); 
              clsx.addAssociation(ast); 
			  
              ast.updateAssociation(pa.card1, pa.card2, pa.role1, pa.role2, pa.stereotypes);   
            } 
          }
          else if (cls instanceof UseCase)     
          { System.out.println("PARSED use case:\n" + ((UseCase) cls).getKM3()); } 
          else 
          { System.out.println("PARSED classifier:\n" + cls); } 
		  
          textPane.setSelectedTextColor(Color.green);  
        } 
        else 
        { System.out.println("SYNTAX errors in classifier!"); 
          textPane.setSelectedTextColor(Color.red);
          for (int i = 0; i < errors.size(); i++) 
          { Vector err = (Vector) errors.get(i); 
            for (int j = 0; j < err.size(); j++) 
            { messageArea.append(err.get(j) + "\n\r"); }  
            messageArea.append("\n\r"); 
	    }  
        }

        /* comp.checkSyntax(owner,entities,antesymbs,messages); 
        if (messages.size() > 0)
        { for (int h = 0; h < messages.size(); h++) 
          { String mess = (String) messages.get(h); 
            messageArea.append(mess + "\n\r"); 
          } 
        }  */ 
     
      } 
      catch (Exception ee) 
      { System.err.println("!! Parsing errors: ");
        ee.printStackTrace();  
        textPane.setSelectedTextColor(Color.red);  
      } 
    }
  }

  class SearchAction extends javax.swing.AbstractAction
  { public SearchAction()
    { super("Search"); }

    public void actionPerformed(ActionEvent e)
    { String searchfor = 
        JOptionPane.showInputDialog("Search for:");
      if (searchfor == null) { return; }
      boolean found = false; 
	  
      int len = searchfor.length();
      int en = doc.getLength(); 
	  
      for (int i = 0; i + len < en; i++)
      try 
      { String txt = textPane.getText(i, len);
        if (searchfor.equalsIgnoreCase(txt))
        { textPane.select(i,i+len);
          textPane.setSelectedTextColor(Color.gray);
          found = true;   
        }   
      } catch (Exception ee) { }
	  
      if (found)
      { System.out.println("Found: " + found); } 
      else 
      { System.out.println("Did not find " + searchfor); }  
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

      Compiler2 comp = new Compiler2();
      comp.nospacelexicalanalysis(txt);

      Vector ents = new Vector(); 
      Vector typs = new Vector(); 
      Vector pregens = new Vector(); 
      Vector preassocs = new Vector(); 
      Vector pnames = new Vector(); 

      Vector items = comp.parseKM3(ents,typs,pregens,preassocs,pnames);
      classArea.processKM3(ents,typs,pregens,preassocs,items); 
      if (pnames.size() > 0) 
      { classArea.setSystemName((String) pnames.get(0)); }

      File file = new File("output/mm.km3");  /* default */ 
      try
      { PrintWriter out =
          new PrintWriter(
            new BufferedWriter(new FileWriter(file)));
        out.println(txt); 
        out.close(); 
      }
      catch (IOException ioe) 
      { System.out.println("Error saving data"); } 
      
      // setVisible(false); 
    } 
  } 

  class LoadAction extends AbstractAction
  { public LoadAction()
    { super("Load from file"); }

    public void actionPerformed(ActionEvent e)
    { File file = new File("output/mm.km3");  /* default */ 
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

