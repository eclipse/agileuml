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

/* Copyright K. Lano 2010-2023
   
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

public class CSTLApp extends JApplet implements DocumentListener
{
    JTextPane textPane1;
    AbstractDocument doc1;
    JTextArea messageArea1;

    JTextPane textPane2;
    AbstractDocument doc2;
    JTextArea messageArea2;

    String newline = "\n";
    HashMap actions;

    String systemName = "app"; 
    private JLabel thisLabel;

    Vector entities = new Vector(); 
    Vector types = new Vector(); 

    public void init() 
    {
        // types = new Vector(); 

        textPane1 = new JTextPane();
        textPane1.setCaretPosition(0);
        textPane1.setMargin(new Insets(5,5,5,5));
        StyledDocument styledDoc1 = textPane1.getStyledDocument();
        if (styledDoc1 instanceof AbstractDocument) 
        { doc1 = (AbstractDocument) styledDoc1;
          doc1.addDocumentListener(this); 
        } 
	   else 
	   {
          System.err.println("!! Error: invalid document");
        }

      SimpleAttributeSet[] attrs = initAttributes(4);

      try {
            doc1.insertString(0, "type\n  : 'Sequence' '(' type ')'\n  | 'Set' '(' type ')'  \n  | 'Bag' '(' type ')' \n  | 'OrderedSet' '(' type ')'\n  | 'Map' '(' type ',' type ')'\n  | 'Function' '(' type ',' type ')'\n  | ID\n    ; \n\nexpressionList\n : (expression ',')* expression\n ; \n \nexpression\n    : logicalExpression\n  | conditionalExpression\n  | lambdaExpression  \n  | letExpression\n    ;\n\nbasicExpression\n  : 'null' \n  | basicExpression '.' ID\n  | basicExpression '(' expressionList? ')'\n  | basicExpression '[' expression ']' \n  | ID '@pre'  \n  | INT  \n  | FLOAT_LITERAL\n  | STRING_LITERAL\n  | ID\n  | '(' expression ')'\n  ; \n\nconditionalExpression\n  : 'if' expression 'then' expression 'else' expression 'endif'\n    ; \n\nlambdaExpression\n  : 'lambda' ID ':' type 'in' expression\n  ; \n\nletExpression\n  : 'let' ID ':' type '=' expression 'in' expression\n  ; \n\nlogicalExpression\n  : logicalExpression '=>' logicalExpression\n  | logicalExpression 'implies' logicalExpression\n  | logicalExpression 'or' logicalExpression  \n  | logicalExpression 'xor' logicalExpression  \n  | logicalExpression '&' logicalExpression \n  | logicalExpression 'and' logicalExpression \n  | 'not' logicalExpression  \n  | equalityExpression\n  ; \n\nequalityExpression\n  : additiveExpression\n    ('=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' |\n    ':'| '/:' | '<:') additiveExpression \n  | additiveExpression\n  ; \n\nadditiveExpression\n  : factorExpression ('+' | '-' | '..' | '|->') additiveExpression\n  | factorExpression\n    ; \n\nfactorExpression\n  : factorExpression ('*' | '/' | 'mod' | 'div') factorExpression\n  | '-' factorExpression \n  | '+' factorExpression  \n  | factor2Expression\n  ; \n\nfactor2Expression\n  : factor2Expression '->size()'\n  | factor2Expression '->copy()'  \n  | factor2Expression ('->isEmpty()' |\n    '->notEmpty()' | \n    '->asSet()' | '->asBag()' |\n    '->asOrderedSet()' | \n    '->asSequence()' | \n    '->sort()' ) \n  | factor2Expression '->any()'   \n  | factor2Expression '->log()'  \n  | factor2Expression '->exp()' \n  | factor2Expression '->sin()'  \n  | factor2Expression '->cos()' \n  | factor2Expression '->tan()'  \n  | factor2Expression '->asin()'  \n  | factor2Expression '->acos()' \n  | factor2Expression '->atan()' \n  | factor2Expression '->log10()' \n  | factor2Expression '->first()'  \n  | factor2Expression '->last()' \n  | factor2Expression '->front()'  \n  | factor2Expression '->tail()' \n  | factor2Expression '->reverse()'\n  | factor2Expression '->tanh()'  \n  | factor2Expression '->sinh()' \n  | factor2Expression '->cosh()' \n  | factor2Expression '->floor()'  \n  | factor2Expression '->ceil()' \n  | factor2Expression '->round()' \n  | factor2Expression '->abs()' \n  | factor2Expression '->oclType()' \n  | factor2Expression '->allInstances()' \n  | factor2Expression '->oclIsUndefined()' \n  | factor2Expression '->oclIsInvalid()' \n  | factor2Expression '->oclIsNew()' \n  | factor2Expression '->sum()'  \n  | factor2Expression '->prd()'  \n  | factor2Expression '->max()'  \n  | factor2Expression '->min()'  \n  | factor2Expression '->sqrt()'  \n  | factor2Expression '->cbrt()'  \n  | factor2Expression '->sqr()' \n  | factor2Expression '->characters()' \n  | factor2Expression '->toInteger()'  \n  | factor2Expression '->toReal()' \n  | factor2Expression '->toBoolean()' \n  | factor2Expression '->toUpperCase()' \n  | factor2Expression '->toLowerCase()' \n  | factor2Expression ('->unionAll()' | '->intersectAll()' | '->concatenateAll()')\n  | factor2Expression ('->pow' | '->gcd') '(' expression ')' \n  | factor2Expression ('->at' | '->union' | '->intersection' \n    | '->includes' | '->excludes' | '->including' \n    | '->excluding' | '->includesAll'  \n    | '->symmetricDifference' \n    | '->excludesAll' | '->prepend' | '->append'\n    | '->count' | '->apply') '(' expression ')' \n  | factor2Expression ('->hasMatch' | '->isMatch' |\n      '->firstMatch' | '->indexOf' | \n      '->lastIndexOf' | '->split' | \n      '->hasPrefix' | \n      '->hasSuffix' | \n      '->equalsIgnoreCase' ) '(' expression ')'\n    | factor2Expression ('->oclAsType' | '->oclIsTypeOf' |\n      '->oclIsKindOf' | \n      '->oclAsSet') '(' expression ')'\n  | factor2Expression '->collect' '(' identifier '|' expression ')'\n  | factor2Expression '->select' '(' identifier '|' expression ')' \n  | factor2Expression '->reject' '(' identifier '|' expression ')' \n  | factor2Expression '->forAll' '(' identifier '|' expression ')' \n  | factor2Expression '->exists' '(' identifier '|' expression ')' \n  | factor2Expression '->exists1' '(' identifier '|' expression ')' \n  | factor2Expression '->one' '(' identifier '|' expression ')' \n  | factor2Expression '->any' '(' identifier '|' expression ')' \n  | factor2Expression '->closure' '(' identifier '|' expression ')' \n  | factor2Expression '->sortedBy' '(' identifier '|' expression ')' \n  | factor2Expression '->isUnique' '(' identifier '|' expression ')' \n  | factor2Expression '->subrange' '(' expression ',' expression ')'  \n  | factor2Expression '->replace' '(' expression ',' expression ')'  \n  | factor2Expression '->replaceAll' '(' expression ',' expression ')' \n  | factor2Expression '->replaceAllMatches' '(' expression ',' expression ')' \n  | factor2Expression '->replaceFirstMatch' '(' expression ',' expression ')'  \n  | factor2Expression '->insertAt' '(' expression ',' expression ')'  \n  | factor2Expression '->insertInto' '(' expression ',' expression ')'  \n  | factor2Expression '->setAt' '(' expression ',' expression ')' \n  | factor2Expression '->iterate' '(' identifier ';' identifier '=' expression '|' expression ')' \n  | setExpression \n  | basicExpression\n   ;\n \nsetExpression\n  : 'OrderedSet{' expressionList? '}'\n  | 'Bag{' expressionList? '}'  \n  | 'Set{' expressionList? '}' \n  | 'Sequence{' expressionList? '}'\n  | 'Map{' expressionList? '}'\n   ;\n", attrs[1]);
          }
          catch (BadLocationException ble) {
            System.err.println("!! Couldn't insert ruleset text.");
        } 

        textPane1.setEditable(false); 

        JScrollPane scrollPane = new JScrollPane(textPane1);
        scrollPane.setPreferredSize(new Dimension(75, 350));

        messageArea1 = new JTextArea(30, 30);
        messageArea1.setEditable(true);
        
        JScrollPane scrollPaneForLog1 = new JScrollPane(messageArea1);

        JSplitPane splitPane = new JSplitPane(
                       JSplitPane.VERTICAL_SPLIT,
                       scrollPane, scrollPaneForLog1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300); 

        // JPanel statusPane = new JPanel(new GridLayout(2, 1));

        getContentPane().add(splitPane, BorderLayout.WEST);


        textPane2 = new JTextPane();
        textPane2.setCaretPosition(0);
        textPane2.setMargin(new Insets(5,5,5,5));
        StyledDocument styledDoc2 = textPane2.getStyledDocument();
        if (styledDoc2 instanceof AbstractDocument) 
        { doc2 = (AbstractDocument) styledDoc2;
          doc2.addDocumentListener(this); 
        } 
	   else 
	   {
          System.err.println("!! Error: invalid document");
        }

        try {
            doc2.insertString(0, "basicExpression::\n_1 . _2 |-->_1._2\n_1 ( _2 ) |-->_1(_2)\n_1 [ _2 ] |-->_1[_2-1]\n( _1 ) |-->(_1)\n_1 |-->_1\n\nfactor2Expression::\n_1 |-->_1\n\nfactorExpression::\n_1 |-->_1\n\nadditiveExpression::\n_1 |-->_1\n\nequalityExpression::\n_1 |-->_1\n\nlogicalExpression::\n_1 |-->_1\n\nexpression::\n_1 |-->_1\n\n" + "identifier::\n_1 |-->_1\n", attrs[1]);
          }
          catch (BadLocationException ble) {
            System.err.println("!! Couldn't insert ruleset text.");
        } 

        JScrollPane scrollPane2 = new JScrollPane(textPane2);
        scrollPane2.setPreferredSize(new Dimension(70, 350));

        messageArea2 = new JTextArea(30, 25);
        messageArea2.setEditable(false);
        JScrollPane scrollPaneForLog2 = new JScrollPane(messageArea2);

        JSplitPane splitPane2 = new JSplitPane(
                       JSplitPane.VERTICAL_SPLIT,
                       scrollPane2, scrollPaneForLog2);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setDividerLocation(300); 

        getContentPane().add(splitPane2, BorderLayout.CENTER);

        thisLabel = 
          new JLabel("Edit CSTL in top right window, source OCL in bottom left.");

        getContentPane().add(thisLabel, BorderLayout.SOUTH); 

        actions = createActionTable(textPane1);
        JMenu editMenu = createEditMenu(); 
        JMenu absMenu = createToolMenu(); 
        // JMenu transMenu = createTranslationMenu();
        // JMenu styleMenu = createStyleMenu();
        JMenuBar mb = new JMenuBar();
        mb.add(editMenu); 
        mb.add(absMenu); 
        // mb.add(transMenu);
        // mb.add(styleMenu);
        setJMenuBar(mb);

        // textPane1.setCaretPosition(0);
        textPane2.setCaretPosition(0);

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

    protected JMenu createToolMenu() 
    { JMenu menu = new JMenu("Tools");

      javax.swing.Action runAction = new ExecuteAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(runAction); 

      return menu; 
   } 

 /*    protected JMenu createTranslationMenu() 
    { JMenu menu = new JMenu("Translate");

      javax.swing.Action toJavaAction = new Translate2JavaAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(toJavaAction); 

      javax.swing.Action toCSAction = new Translate2CSAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(toCSAction); 

      javax.swing.Action toCPPAction = new Translate2CPPAction(); 
        // checkAction.setMnemonic(KeyEvent.VK_K);
      menu.add(toCPPAction); 

      return menu; 
   } */ 

   public void typeCheck()
   { for (int i = 0; i < entities.size(); i++) 
     { Entity e = (Entity) entities.get(i); 
       e.typeCheckAttributes(types,entities); 
       e.typeCheckOps(types,entities); 
       e.typeCheckInvariants(types,entities); 
     } 

     /* for (int j = 0; j < useCases.size(); j++) 
     { if (useCases.get(j) instanceof UseCase)
       { UseCase uc = (UseCase) useCases.get(j); 
         uc.typeCheck(types,entities); 
       } 
     }  */ 
   }

    public void changedUpdate(DocumentEvent e)
    { // int offset = e.getOffset(); 
	 // int pos = textPane1.getCaretPosition();
      // System.out.println(">> Update event at: " + offset + " " + pos); 
    }

	public void removeUpdate(DocumentEvent e)
	{ // int offset = e.getOffset(); 
	  // int pos = textPane1.getCaretPosition();
       // System.out.println(">> Remove event at: " + offset + " " + pos); 
	}

	public void insertUpdate(DocumentEvent e)
	{ // int offset = e.getOffset(); 
	  // int pos = textPane1.getCaretPosition();
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


  class ExecuteAction extends javax.swing.AbstractAction
  { public ExecuteAction()
    { super("Execute CSTL"); }

    public void actionPerformed(ActionEvent e)
    { int pos = doc2.getLength(); 
      // System.out.println("Document length = " + pos); 
      int pos2 = textPane2.getCaretPosition();
      // System.out.println("Caret position = " + pos2); 
      if (pos2 > pos) 
      { pos = pos2; } 
      if (pos == 0) { return; }

      String txt = "";
      String asttext = "";
  
      try 
      { txt = textPane2.getText(0, pos+1); } 
      catch(Exception _e) { return; } 

      String[] linearray = txt.split("[\\n\\r]+"); 
      Vector lines = new Vector();
      for (int i = 0; i < linearray.length; i++) 
      { lines.add(linearray[i]); } 
      CGSpec res = new CGSpec(new Vector(), new Vector()); 
      CSTL.loadCGTL(res,lines);

      String sourcetext = messageArea1.getText();

      if (sourcetext == null || sourcetext.length() == 0)
      { JOptionPane.showMessageDialog(null, 
            "Enter an OCL expression in the lower left window", 
            "", JOptionPane.ERROR_MESSAGE); 
        return; 
      }  
 

      String[] args = {"OCL", "expression"}; 
      try { 
        org.antlr.v4.gui.AntlrGUI antlr = 
          new org.antlr.v4.gui.AntlrGUI(args); 

        antlr.setText(sourcetext); 

        antlr.process(); 

        asttext = antlr.getResultText(); 
        messageArea2.setText("" + asttext);
      } 
      catch (Exception _expt) 
      { messageArea2.append(_expt.getMessage()); 
        return; 
      }

      Compiler2 comp = new Compiler2();
      ASTTerm xx = comp.parseGeneralAST(asttext);

        if (xx == null) 
        { messageArea2.append("!! Invalid text for general AST"); 
          return; 
        } 

      String restext = xx.cg(res); 
      messageArea2.setText(restext);
    }
  }
}