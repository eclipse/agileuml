import java.io.*; 
import java.awt.*;
import java.awt.Graphics;
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
import javax.swing.JApplet; 
import javax.swing.text.*;
import javax.swing.event.*;

/* K. Lano 2010-2022
   
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

public class AgileUMLApp extends JApplet implements DocumentListener
{
    JTextPane textPane;
    AbstractDocument doc;
    static final int MAX_CHARACTERS = 1000;
    JTextArea messageArea;
    String newline = "\n";
    HashMap actions;

    String systemName = "app"; 
    private JLabel thisLabel;

    Vector entities = new Vector(); 
    Vector types = new Vector(); 

    public void init() 
    {
        // types = new Vector(); 

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

      SimpleAttributeSet[] attrs = initAttributes(4);

      try {
            doc.insertString(0, "class Person { String name; int age; }", attrs[1]);
          }
          catch (BadLocationException ble) {
            System.err.println("Couldn't insert code text.");
        } 

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(250, 350));

        messageArea = new JTextArea(30, 80);
        messageArea.setEditable(false);
        JScrollPane scrollPaneForLog = new JScrollPane(messageArea);

        JSplitPane splitPane = new JSplitPane(
                       JSplitPane.VERTICAL_SPLIT,
                       scrollPane, scrollPaneForLog);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200); 

        // JPanel statusPane = new JPanel(new GridLayout(2, 1));

        getContentPane().add(splitPane, BorderLayout.CENTER);
        // getContentPane().add(statusPane, BorderLayout.EAST);
        thisLabel = 
          new JLabel("Type & click within the framed area.");
        getContentPane().add(thisLabel, BorderLayout.SOUTH); 

        actions = createActionTable(textPane);
        JMenu editMenu = createEditMenu(); 
        JMenu absMenu = createAbstractionMenu(); 
        JMenu transMenu = createTranslationMenu();
        // JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu); 
        mb.add(absMenu); 
        mb.add(transMenu);
        // mb.add(styleMenu);
        setJMenuBar(mb);

        textPane.setCaretPosition(0);

        // pack();
        setVisible(true);
    }

    public void paint(Graphics g)
    { setVisible(true); } 

    private javax.swing.Action getActionByName(String name) 
    { return (javax.swing.Action) actions.get(name); }

    protected JMenu createEditMenu() 
    { JMenu menu = new JMenu("Edit");

      javax.swing.Action cAction = getActionByName(DefaultEditorKit.cutAction);
        // cAction.setValue(Action.NAME,"Cut");  
      JMenuItem cutMI = menu.add(cAction);
      cutMI.setLabel("Cut"); 
      
      javax.swing.Action cpyAction = getActionByName(DefaultEditorKit.copyAction);
      JMenuItem copyMI = menu.add(cpyAction); 
      copyMI.setLabel("Copy"); 
      
      javax.swing.Action pteAction = getActionByName(DefaultEditorKit.pasteAction);
      JMenuItem pasteMI = menu.add(pteAction); 
      pasteMI.setLabel("Paste"); 

      return menu;
    }

    protected JMenu createAbstractionMenu() 
    { JMenu menu = new JMenu("Abstraction");

      javax.swing.Action fromCAction = new AbstractFromCAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(fromCAction); 

      javax.swing.Action fromJavaAction = new AbstractFromJavaAction(); 
      // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(fromJavaAction); 

      return menu; 
   } 

    protected JMenu createTranslationMenu() 
    { JMenu menu = new JMenu("Translate");

      javax.swing.Action toJavaAction = new Translate2JavaAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(toJavaAction); 

      javax.swing.Action toCSAction = new Translate2CSAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(toCSAction); 

      return menu; 
   } 


    public void changedUpdate(DocumentEvent e)
    { int offset = e.getOffset(); 
	 int pos = textPane.getCaretPosition();
      // System.out.println(">> Update event at: " + offset + " " + pos); 
    }

	public void removeUpdate(DocumentEvent e)
	{ int offset = e.getOffset(); 
	  int pos = textPane.getCaretPosition();
       // System.out.println(">> Remove event at: " + offset + " " + pos); 
	}

	public void insertUpdate(DocumentEvent e)
	{ int offset = e.getOffset(); 
	  int pos = textPane.getCaretPosition();
       // try {
       //   System.out.println(">> Insert event at: " + offset + " " + pos + " " + textPane.getText(pos,1)); 
       // } catch (Exception _e) { } 
     } 

    private HashMap createActionTable(JTextComponent textComponent) 
    { HashMap actions = new HashMap();
      javax.swing.Action[] actionsArray = textComponent.getActions();
      for (int i = 0; i < actionsArray.length; i++) 
      { javax.swing.Action a = actionsArray[i];
        actions.put(a.getValue(javax.swing.Action.NAME), a);
      }
      return actions;
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


  class AbstractFromCAction extends javax.swing.AbstractAction
  { public AbstractFromCAction()
    { super("Abstract from C"); }

    public void actionPerformed(ActionEvent e)
    { int pos = doc.getLength(); 
      // System.out.println("Document length = " + pos); 
      int pos2 = textPane.getCaretPosition();
      // System.out.println("Caret position = " + pos2); 
      if (pos2 > pos) 
      { pos = pos2; } 
      if (pos == 0) { return; }

      String txt = "";
      String asttext = "";
  
      try 
      { txt = textPane.getText(0, pos+1); } 
      catch(Exception _e) { return; } 

      String[] args = {"C", "translationUnit"}; 
      try { 
        org.antlr.v4.gui.AntlrGUI antlr = 
          new org.antlr.v4.gui.AntlrGUI(args); 

        antlr.setText(txt); 

        antlr.process(); 

        asttext = antlr.getResultText(); 
        messageArea.setText("" + asttext);
      } 
      catch (Exception _expt) 
      { _expt.printStackTrace(); } 


      Compiler2 comp = new Compiler2();
      ASTTerm xx = comp.parseGeneralAST(asttext);
      java.util.Map m1 = new java.util.HashMap();
      java.util.Map m2 = new java.util.HashMap();
      Vector v1 = new Vector();
      Vector v2 = new Vector(); 
    // v1.addAll(types); 
    // v2.addAll(entities); 

      if (xx == null)
      { System.err.println("!! Invalid C AST"); 
        return;
      } 

      if (xx instanceof ASTCompositeTerm) { } 
      else
      { System.err.println("!! Invalid C AST: " + xx); 
        return;
      } 

      entities = new Vector(); 
      types = new Vector(); 

      ((ASTCompositeTerm) xx).identifyCFunctions(null,m1,m2,v1,v2);

      Vector mxs = 
        ((ASTCompositeTerm) xx).cprogramToKM3(null,m1,m2,v1,v2); 

      String restext = ""; 

      for (int i = 0; i < v1.size(); i++) 
      { Type tt = (Type) v1.get(i); 
        
        restext = restext + tt.getKM3() + "\n"; 
        types.add(tt); 
      } 

      for (int i = 0; i < v2.size(); i++) 
      { Entity newent = (Entity) v2.get(i); 
        if (newent.isInterface() ||
          newent.hasConstructor()) 
        { } 
        else if (newent.isStruct())
        { newent.addStaticConstructor(); } 
        else 
        { newent.addDefaultConstructor(); }
        entities.add(newent); 
      }

      for (int i = 0; i < v2.size(); i++) 
      { Entity ent = (Entity) v2.get(i); 
        restext = restext + ent.getKM3() + "\n"; 
      } 
        
      messageArea.setText("" + restext);
    }
  }


  class AbstractFromJavaAction extends javax.swing.AbstractAction
  { public AbstractFromJavaAction()
    { super("Abstract from Java"); }

    public void actionPerformed(ActionEvent e)
    { int pos = doc.getLength(); 
      // System.out.println("Document length = " + pos); 
      int pos2 = textPane.getCaretPosition();
      // System.out.println("Caret position = " + pos2); 
      if (pos2 > pos) 
      { pos = pos2; } 
      if (pos == 0) { return; }

      String txt = "";
      String asttext = "";
  
      try 
      { txt = textPane.getText(0, pos+1); } 
      catch(Exception _e) { return; } 

      String[] args = {"Java", "compilationUnit"}; 
      try { 
        org.antlr.v4.gui.AntlrGUI antlr = 
          new org.antlr.v4.gui.AntlrGUI(args); 

        antlr.setText(txt); 

        antlr.process(); 

        asttext = antlr.getResultText(); 
        messageArea.setText("" + asttext);
      } 
      catch (Exception _expt) 
      { _expt.printStackTrace(); } 

      Compiler2 comp = new Compiler2();
      ASTTerm xx = comp.parseGeneralAST(asttext);

        if (xx == null) 
        { System.err.println("!! Invalid text for general AST"); 
          return; 
        } 

      xx.entities = new Vector(); 
      xx.enumtypes = new Vector(); 

      String restext = xx.toKM3(); 
      System.out.println(restext); 
      messageArea.setText(restext);

      entities = new Vector(); 
      String pname = ASTTerm.packageName; 
      if (pname != null) 
      { System.out.println(">>> System name is: " + pname); 
        // systemName = pname; 
      } 

      if (xx.modelElement != null) 
      { if (xx.modelElement instanceof Entity) 
        { Entity newent = (Entity) xx.modelElement; 
          if (newent.isInterface() || newent.hasConstructor()) 
          { } 
          else 
          { newent.addDefaultConstructor(); } 

          entities.add(xx.modelElement); 
        } 
        else if (xx.modelElement instanceof BehaviouralFeature)
        { Entity ex = new Entity("FromJava"); 
          ex.addOperation((BehaviouralFeature) xx.modelElement);  
        } 
      } 
      else if (xx.modelElements != null) 
      { for (int i = 0; i < xx.modelElements.size(); i++) 
        { ModelElement me = (ModelElement) xx.modelElements.get(i); 
          if (me instanceof Entity) 
          { Entity newent = (Entity) me; 
            if (newent.isInterface() ||
                newent.hasConstructor()) 
            { } 
            else 
            { newent.addDefaultConstructor(); } 

            entities.add(newent); 
          } 
          else if (me instanceof BehaviouralFeature)
          { Entity ex = new Entity("FromJava"); 
            ex.addOperation((BehaviouralFeature) me);  
          } 
        } // and add inheritances. 
      } 
    }
  } 


  class Translate2JavaAction extends javax.swing.AbstractAction
  { public Translate2JavaAction()
    { super("Translate to Java"); }

    public void actionPerformed(ActionEvent e)
    { int pos = doc.getLength(); 
      // System.out.println("Document length = " + pos); 
      int pos2 = textPane.getCaretPosition();
      // System.out.println("Caret position = " + pos2); 
      if (pos2 > pos) 
      { pos = pos2; } 
      if (pos == 0) { return; }
      String txt = ""; 
      try 
      { txt = textPane.getText(0, pos+1); } 
      catch(Exception _e) { return; } 

      Compiler2 comp = new Compiler2();
      ASTTerm trm = comp.parseGeneralAST(txt);
      System.out.println(trm); 
      messageArea.setText("" + trm); 
    } 
  } 


  class Translate2CSAction extends javax.swing.AbstractAction
  { public Translate2CSAction()
    { super("Translate to C#"); }

    public void actionPerformed(ActionEvent e)
    { StringWriter sw = new StringWriter(); 
      PrintWriter out = new PrintWriter(sw);   
      for (int i = 0; i < entities.size(); i++) 
      { Entity ent = (Entity) entities.get(i);
        if (ent.isExternal() || ent.isComponent()) 
        { continue; }  
        ent.generateCSharp(entities,types,out);     
      } 
      String res = sw.toString(); 
      messageArea.setText(res);
    } 
  }
}