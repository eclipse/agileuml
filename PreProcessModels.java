/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: TransformationSynthesis */ 

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.lang.Runtime; 

import java.util.Vector;
import java.util.Date; 


public class PreProcessModels
{ // Reads output/typeExamples.txt
  //       output/expressionExamples.txt
  //       output/statementExamples.txt
  //       output/declarationExamples.txt
  // Parses source (OCL/UML) examples using Compiler2
  // Parses target (program) examples with Antlr
  // 
  // Produces output/out.txt 

  static CGBEDialog opDialog = null; 

  public static void preprocess()
  { if (opDialog == null)
    { opDialog = new CGBEDialog(null);
      opDialog.pack();
      // opDialog.setLocationRelativeTo(this);
    }
    opDialog.setOldFields("Java", "typeTypeOrVoid",
            "expression", "statement",
            "classBodyDeclaration", "classDeclaration",
            "localVariableDeclaration", 
            "enumDeclaration");
    opDialog.setVisible(true);
   
    String tlang = opDialog.getName(); 
    String typesr = opDialog.getTypesRule(); 
    String exprsr = opDialog.getExpressionsRule(); 
    String statsr = opDialog.getStatementsRule(); 
    String declnsr = opDialog.getDeclarationsRule(); 
    String classesr = opDialog.getClassesRule(); 
    String localdecsr = opDialog.getLocalDecsRule(); 
    String enumsr = opDialog.getEnumsRule(); 

    // System.out.println(">>> " + nme + " " + typr + " " + 
    //                    expr); 


    Vector typeExamples = new Vector(); // Type
    String oclTypeModel = ""; 
    Vector programTypeExamples = new Vector(); // String

    System.out.println();
    String targetLanguage = ""; 
    // String tlang = 
    //   JOptionPane.showInputDialog("Enter target language name (of Antlr parser): ");
    if (tlang == null) 
    { System.err.println("!! Invalid language name"); 
      return; 
    } 
    targetLanguage = tlang; 


    Vector entities = new Vector(); 
    Vector types = new Vector();

    Date d1 = new Date(); 
    long t1 = d1.getTime(); 
 
    try 
    { File tfile = new File("./output/typeExamples.txt");
	  
      if (tfile == null) { return; }
	 
      System.out.println(">>> Loading type examples");
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      

      br = new BufferedReader(new FileReader(tfile));
      
      int linecount = 0; 

      while (!eof)
      { try 
        { s = br.readLine(); }
        catch (IOException ioe)
        { System.out.println("!! Reading " + tfile.getName() + " failed.");
          return; 
        }

        if (s == null) 
        { eof = true; 
          break; 
        }

        s = s.trim();

        if (s.length() == 0) 
        { continue; } 

        String progstring = ""; 
        int tabindex = s.indexOf("\t"); 
        if (tabindex >= 0) 
        { progstring = s.substring(tabindex+1,s.length());
          s = s.substring(0,tabindex);
          tabindex = progstring.indexOf("\t");   
          while (tabindex >= 0)
          { progstring = progstring.substring(tabindex+1,progstring.length()); 
            tabindex = progstring.indexOf("\t");   
          }
          
          System.out.println(">> Program text: " + progstring); 
          programTypeExamples.add(progstring); 
        }  
    
        String exampleName = "ocltype" + linecount; 
        String modelString = ""; 

        linecount++;
 
        Compiler2 comp = new Compiler2();  
        comp.nospacelexicalanalysis(s); 
        Type tt = comp.parseType(entities,types); 
        if (tt != null) 
        { typeExamples.add(tt); 
          if ("void".equals(tt.getName()))
          { modelString = exampleName + " : OclVoidType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if ("String".equals(tt.getName()))
          { modelString = exampleName + " : OclStringType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if ("OclAny".equals(tt.getName()))
          { modelString = exampleName + " : OclAnyType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if (Type.isPrimitiveType(tt))
          { modelString = exampleName + " : OclBasicType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if (Type.isClassifierType(tt))
          { modelString = exampleName + " : OclClassifierType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if ("Map".equals(tt.getName()))
          { modelString = exampleName + " : OclMapType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if ("Function".equals(tt.getName()))
          { modelString = exampleName + " : OclFunctionType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else if (Type.isCollectionType(tt))
          { modelString = exampleName + " : OclCollectionType\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclTypeModel = oclTypeModel + modelString; 
          } 
          else 
          { System.err.println("! Unknown type category: " + tt); }
        } 
      } 

      br.close(); 
    } catch(Exception e)
      { System.err.println(">>> Error processing output/typeExamples.txt"); 
        e.printStackTrace(); 
      }

    System.out.println(">> Type examples: " + typeExamples); 
    System.out.println(">> OCL type model: " + oclTypeModel);

    String progModelString = ""; 
    String progid = ""; 

    String targetRule = ""; 
    // String trule = 
    //    JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " types): ");
    if (typesr == null) 
    { System.err.println("!! Error: no type rule"); 
      return; 
    } 
    targetRule = typesr; 


    try 
    { // File temp = new File("tmp.txt"); 
      Runtime proc = Runtime.getRuntime(); 
        
      for (int i = 0; i < programTypeExamples.size(); i++) 
      { // BufferedWriter bw = new BufferedWriter(new FileWriter(temp)); 
        String progEx = (String) programTypeExamples.get(i); 
        // bw.write(progEx + "\n");
        // bw.close();
        Thread.sleep(50); 

        // type tmp.txt | java org.antlr.v4.gui.TestRig Java typeTypeOrVoid -tree >ast.txt

        // Process p = proc.exec("parseProgramType.bat"); 
        // InputStream pstdin = p.getInputStream(); 
        // StreamGobble igb = new StreamGobble(pstdin); 
        // InputStream pstderr = p.getErrorStream(); 
        // StreamGobble egb = new StreamGobble(pstderr); 
 
        // egb.start(); igb.start();   
        // int exitp = p.waitFor();

        // File af = new File("ast.txt"); 
        // BufferedReader astbr = new BufferedReader(new FileReader(af));
        // String asttext = astbr.readLine(); 
        // astbr.close(); 

        Process p2 = proc.exec("grun.bat " + targetLanguage + " " + targetRule + " -tree"); 

        OutputStream sout = p2.getOutputStream(); 
        OutputStreamWriter outw = new OutputStreamWriter(sout); 
        BufferedWriter brw = new BufferedWriter(outw);
        brw.write(progEx + "\n"); 
        brw.close();  
  
        InputStream sin2 = p2.getInputStream(); 
        InputStreamReader inr2 = new InputStreamReader(sin2); 
        BufferedReader ibr2 = new BufferedReader(inr2); 
        String stext = ""; 
        String oline2 = ibr2.readLine(); 
        System.out.println(">>> parsing .... " + progEx);
        while (oline2 != null) 
        { stext = oline2; 
          oline2 = ibr2.readLine();
        }
        String asttext = stext.trim();  
        int exitjar2 = p2.waitFor(); 
        System.out.println(">>> Exit code: " + exitjar2);

        progid = "progtype" + i; 
        if (asttext.startsWith("("))
        { progModelString = progModelString + progid + " : ProgramType\n" + 
            progid + ".ast = " + asttext + "\n\n"; 
          System.out.println(">> Parsed: --- " + asttext);
        } 
        else 
        { progModelString = progModelString + progid + " : ProgramType\n"; 
          System.err.println("!! Error: could not parse " + progEx); 
        }   
      } 
    } 
    catch (Exception fex) 
    { System.err.println("!! Error parsing program type examples!"); 
      fex.printStackTrace(); 
    }  

    Date d2 = new Date(); 
    long t2 = d2.getTime(); 

    System.out.println(">>> Time for pre-processing types: " + (t2-t1)); 

    System.out.println(">>> program model: " + 
                       progModelString); 

    


    Vector expressionExamples = new Vector(); // Expression
    String oclExpressionModel = ""; 
    Vector programExpressionExamples = new Vector(); // String

    try 
    { File efile = new File("./output/expressionExamples.txt");
	  
      if (efile == null) { return; }
	 
      System.out.println(">>> Loading expression examples");
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      br = new BufferedReader(new FileReader(efile)); 
      
      int linecount = 0; 

      while (!eof)
      { try 
        { s = br.readLine(); }
        catch (IOException ioe)
        { System.out.println("!! Reading " + efile.getName() + " failed.");
          return; 
        }

        if (s == null) 
        { eof = true; 
          break; 
        }

        s = s.trim();

        if (s.length() == 0) 
        { continue; } 

        String progstring = ""; 
        int tabindex = s.indexOf("\t"); 
        if (tabindex >= 0) 
        { progstring = s.substring(tabindex+1,s.length());
          s = s.substring(0,tabindex);
          tabindex = progstring.indexOf("\t");   
          while (tabindex >= 0)
          { progstring = progstring.substring(tabindex+1,progstring.length()); 
            tabindex = progstring.indexOf("\t");   
          }
          
          System.out.println(">> Program text: " + progstring); 
          programExpressionExamples.add(progstring); 
        }  
    
        String exampleName = "oclexpr" + linecount; 
        String modelString = ""; 

        linecount++;

        Vector env = new Vector(); 
 
        Compiler2 comp = new Compiler2();  
        comp.nospacelexicalanalysis(s); 
        Expression tt = comp.parseExpression(entities,types); 
        if (tt != null) 
        { tt.typeCheck(types,entities,env); 
          expressionExamples.add(tt); 
          if (Expression.isOclValue(tt))
          { modelString = exampleName + " : OclValue\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            String oclExprType = tt.getOclType();
            if (oclExprType != null) 
            { modelString = modelString +  
                exampleName + ".type = " + oclExprType + "\n"; 
            } 
            oclExpressionModel = oclExpressionModel + modelString + "\n";
          } 
          else if (Expression.isOclIdentifier(tt))
          { modelString = exampleName + " : OclIdentifier\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
            env.add(new Attribute((BasicExpression) tt)); 
          } 
          else if (Expression.isOclFieldAccess(tt))
          { modelString = exampleName + " : OclFieldAccess\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclOperationCall0(tt))
          { modelString = exampleName + " : OclOperationCall0\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclOperationCallN(tt))
          { modelString = exampleName + " : OclOperationCallN\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclArrayAccess(tt))
          { modelString = exampleName + " : OclArrayAccess\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclUnaryPrefix(tt))
          { modelString = exampleName + " : OclUnaryPrefix\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclUnaryPostfix(tt))
          { modelString = exampleName + " : OclUnaryPostfix\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclUnaryLambda(tt))
          { modelString = exampleName + " : OclLambdaExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclBinaryInfix(tt))
          { modelString = exampleName + " : OclInfixBinaryExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclBinaryArrow(tt))
          { modelString = exampleName + " : OclArrowOpBinExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isOclIteratorExpression(tt))
          { modelString = exampleName + " : OclIteratorExpression\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isEmptyCollectionExpression(tt))
          { modelString = exampleName + " : EmptyCollectionExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isNonEmptySetExpression(tt))
          { modelString = exampleName + " : NonEmptySetExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (Expression.isNonEmptySequenceExpression(tt))
          { modelString = exampleName + " : NonEmptySequenceExpr\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else if (tt instanceof ConditionalExpression)
          { modelString = exampleName + " : OclConditionalExpression\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n\n"; 
            oclExpressionModel = oclExpressionModel + modelString; 
          } 
          else 
          { System.err.println("!! Unknown expression category: " + tt); }
        } 
      } 

      br.close(); 
    } catch(Exception e)
      { System.err.println(">>> Error processing output/expressionExamples.txt"); }

    System.out.println(">> Expression examples: " + expressionExamples); 
    System.out.println(">> Expression model: " + oclExpressionModel);

    String exprprogModelString = ""; 
    progid = ""; 

    // trule = 
    //     JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " expressions): ");
    if (exprsr == null) 
    { System.err.println("!! No rule for expressions"); 
      return; 
    } 
    targetRule = exprsr; 

    try 
    { // File temp = new File("tmp.txt"); 
      Runtime proc = Runtime.getRuntime(); 
        
      for (int i = 0; i < programExpressionExamples.size(); i++) 
      { // BufferedWriter bw = new BufferedWriter(new FileWriter(temp)); 
        String progEx = (String) programExpressionExamples.get(i); 
        // bw.write(progEx + "\n");
        // bw.close();
        Thread.sleep(50); 
        // Process p = proc.exec("parseProgramExpression.bat"); 
        // InputStream stdin = p.getInputStream(); 
        // StreamGobble igb = new StreamGobble(stdin); 
        // InputStream stderr = p.getErrorStream(); 
        // StreamGobble egb = new StreamGobble(stderr); 
 
        // egb.start(); igb.start();   
        // int exitp = p.waitFor();

        // File af = new File("ast.txt"); 
        // BufferedReader astbr = new BufferedReader(new FileReader(af));
        // String asttext = astbr.readLine(); 
        // astbr.close(); 

        Process p2 = proc.exec("grun.bat " + targetLanguage + " " + targetRule + " -tree"); 

        OutputStream sout = p2.getOutputStream(); 
        OutputStreamWriter outw = new OutputStreamWriter(sout); 
        BufferedWriter brw = new BufferedWriter(outw);
        brw.write(progEx + "\n"); 
        brw.close();  
  
        InputStream sin2 = p2.getInputStream(); 
        InputStreamReader inr2 = new InputStreamReader(sin2); 
        BufferedReader ibr2 = new BufferedReader(inr2); 
        String stext = ""; 
        String oline2 = ibr2.readLine(); 
        System.out.println(">>> parsing .... " + progEx);
        while (oline2 != null) 
        { stext = oline2; 
          oline2 = ibr2.readLine();
        }
        String asttext = stext.trim();  
        int exitjar2 = p2.waitFor(); 
        System.out.println(">>> Exit code: " + exitjar2);

        progid = "progexpr" + i;
        if (asttext.startsWith("(")) 
        { exprprogModelString = exprprogModelString + progid + " : ProgramExpression\n" + 
            progid + ".ast = " + asttext + "\n\n"; 
          System.out.println("--- " + asttext);  
        } 
        else 
        { exprprogModelString = exprprogModelString + progid + " : ProgramExpression\n"; 
          System.err.println("!! Error: could not parse " + progEx);  
        } 
      } 
    } 
    catch (Exception fex) 
    { System.err.println("!! Error parsing program expression examples!"); 
      fex.printStackTrace(); 
    }  

    Date d3 = new Date(); 
    long t3 = d3.getTime(); 

    System.out.println(">>> Time for pre-processing expressions: " + (t3-t2)); 

    System.out.println(">>> Expression model: " + 
                       exprprogModelString); 


    /* Statements */ 

    Vector statementExamples = new Vector(); // Statement
    String oclStatementModel = ""; 
    Vector programStatementExamples = new Vector(); // String

    try 
    { File sfile = new File("./output/statementExamples.txt");
	  
      if (sfile == null) { return; }
	 
      System.out.println(">>> Loading statement examples");
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      br = new BufferedReader(new FileReader(sfile));

      
      int linecount = 0; 

      while (!eof)
      { try 
        { s = br.readLine(); }
        catch (IOException ioe)
        { System.out.println("!! Reading " + sfile.getName() + " failed.");
          return; 
        }

        if (s == null) 
        { eof = true; 
          break; 
        }

        s = s.trim();

        if (s.length() == 0) 
        { continue; } 

        String progstring = ""; 
        int tabindex = s.indexOf("\t"); 
        if (tabindex >= 0) 
        { progstring = s.substring(tabindex+1,s.length());
          s = s.substring(0,tabindex);
          tabindex = progstring.indexOf("\t");   
          while (tabindex >= 0)
          { progstring = progstring.substring(tabindex+1,progstring.length()); 
            tabindex = progstring.indexOf("\t");   
          }
          
          System.out.println(">> Program text: " + progstring); 
          programStatementExamples.add(progstring); 
        }  
    
        String exampleName = "oclstat" + linecount; 
        String modelString = ""; 

        linecount++;

        // Vector env = new Vector(); 
 
        Compiler2 comp = new Compiler2();  
        comp.nospacelexicalanalysis(s); 
        Statement tt = comp.parseStatement(); 
        if (tt != null) 
        { statementExamples.add(tt); 

          if (SequenceStatement.isBlock1(tt))
          { modelString = exampleName + " : OclBlock1\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (Statement.isOclBasicStatement(tt))
          { modelString = exampleName + " : OclBasicStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if ("skip".equals(tt + ""))
          { modelString = exampleName + " : OclSkipStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof InvocationStatement)
          { modelString = exampleName + " : OclCallStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof ImplicitInvocationStatement)
          { modelString = exampleName + " : OclImplicitCallStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof ErrorStatement)
          { modelString = exampleName + " : OclErrorStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof ReturnStatement)
          { modelString = exampleName + " : OclReturnStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof AssignStatement)
          { modelString = exampleName + " : OclAssignStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof ConditionalStatement)
          { modelString = exampleName + " : OclConditionalStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof CreationStatement)
          { modelString = exampleName + " : OclCreationStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof WhileStatement && 
            "while".equals(
               ((WhileStatement) tt).getOperator()) )
          { modelString = exampleName + " : OclWhileStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof WhileStatement && 
            "for".equals(
               ((WhileStatement) tt).getOperator()) )
          { modelString = exampleName + " : OclForStatement\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else if (tt instanceof SequenceStatement && 
                   SequenceStatement.isBlockN(tt))
          { modelString = exampleName + " : OclBlockN\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclStatementModel = oclStatementModel + modelString + "\n";
          } 
          else 
          { System.err.println("!! Unknown statement category: " + tt); }
        } 
      } 

      br.close(); 
    } catch(Exception e)
      { System.err.println(">>> Error processing output/statementExamples.txt"); }

    System.out.println(">> Statement examples: " + statementExamples); 
    System.out.println(">> Statement model: " + oclStatementModel);


    // trule = 
    //    JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " statements): ");
    if (statsr == null) 
    { System.err.println("!! No statements rule"); 
      return;
    } 

    String statTargetRule = statsr; 

    // String trule = 
    //     JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " local declarations): ");
    if (localdecsr == null) 
    { System.err.println("!! No local declaration rule"); 
      return; 
    } 

    String declTargetRule = localdecsr; 

    String statProgModelString = ""; 
    progid = ""; 

    try 
    { // File temp = new File("tmp.txt"); 
      Runtime proc = Runtime.getRuntime(); 
        
      for (int i = 0; i < programStatementExamples.size() && 
                      i < statementExamples.size(); 
           i++) 
      { // BufferedWriter bw = new BufferedWriter(new FileWriter(temp)); 
        String progEx = (String) programStatementExamples.get(i); 
        // bw.write(progEx + "\n");
        // bw.close();
        Thread.sleep(50); 
        Process p; 

        if (statementExamples.get(i) instanceof CreationStatement)
        { p = proc.exec("grun.bat " + targetLanguage + " " + declTargetRule + " -tree"); } 
        else 
        { p = proc.exec("grun.bat " + targetLanguage + " " + statTargetRule + " -tree"); } 
 
        // InputStream stdin = p.getInputStream(); 
        // StreamGobble igb = new StreamGobble(stdin); 
        // InputStream stderr = p.getErrorStream(); 
        // StreamGobble egb = new StreamGobble(stderr); 
 
        // egb.start(); igb.start();   
        // int exitp = p.waitFor();

        // File af = new File("ast.txt"); 
        // BufferedReader astbr = new BufferedReader(new FileReader(af));
     
        OutputStream sout = p.getOutputStream(); 
        OutputStreamWriter outw = new OutputStreamWriter(sout); 
        BufferedWriter brw = new BufferedWriter(outw);
        brw.write(progEx + "\n"); 
        brw.close();  
  
        InputStream sin2 = p.getInputStream(); 
        InputStreamReader inr2 = new InputStreamReader(sin2); 
        BufferedReader ibr2 = new BufferedReader(inr2); 
        String stext = ""; 
        String oline2 = ibr2.readLine(); 
        System.out.println(">>> parsing .... " + progEx);
        while (oline2 != null) 
        { stext = oline2; 
          oline2 = ibr2.readLine();
        }

        String asttext = stext.trim();  
        int exitjar2 = p.waitFor(); 
        System.out.println(">>> Exit code: " + exitjar2);

        progid = "progstat" + i;
        if (asttext.startsWith("(")) 
        { statProgModelString = statProgModelString + progid + " : ProgramStatement\n" + 
            progid + ".ast = " + asttext + "\n\n"; 
          System.out.println("--- " + asttext);  
        } 
        else 
        { statProgModelString = statProgModelString + progid + " : ProgramStatement\n"; 
          System.err.println("!! Error: could not parse " + progEx);  
        } 
      } 
    } 
    catch (Exception fex) 
    { System.err.println("!! Error parsing program statement examples!"); 
      fex.printStackTrace(); 
    }  

    Date d4 = new Date(); 
    long t4 = d4.getTime(); 

    System.out.println(">>> Time for pre-processing statements: " + (t4-t3)); 

    System.out.println(">>> Statement model: " + statProgModelString); 


    /* Declarations */ 

    Vector operationExamples = new Vector(); // BehaviouralFeature
    String oclOperationModel = ""; 
    Vector programOperationExamples = new Vector(); // String

    try 
    { File dfile = new File("./output/declarationExamples.txt");
	  
      if (dfile == null) 
      { throw new FileNotFoundException(); }
	 
      System.out.println(">>> Loading declaration examples");
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      br = new BufferedReader(new FileReader(dfile));
      
      int linecount = 0; 

      while (!eof)
      { try 
        { s = br.readLine(); }
        catch (IOException ioe)
        { System.out.println("!! Reading " + dfile.getName() + " failed.");
          return; 
        }

        if (s == null) 
        { eof = true; 
          break; 
        }

        s = s.trim();

        if (s.length() == 0) 
        { continue; } 

        String progstring = ""; 
        int tabindex = s.indexOf("\t"); 
        if (tabindex >= 0) 
        { progstring = s.substring(tabindex+1,s.length());
          s = s.substring(0,tabindex);
          tabindex = progstring.indexOf("\t");   
          while (tabindex >= 0)
          { progstring = progstring.substring(tabindex+1,progstring.length()); 
            tabindex = progstring.indexOf("\t");   
          }
          
          System.out.println(">> Program text: " + progstring); 
          programOperationExamples.add(progstring); 
        }  
    
        String exampleName = "oclop" + linecount; 
        String modelString = ""; 

        linecount++;

        // Vector env = new Vector(); 
 
        Compiler2 comp = new Compiler2();  
        comp.nospacelexicalanalysis(s);
        String keyword = comp.getLexical(0); 
        ModelElement tt = null; 

        if ("static".equals(keyword))
        { keyword = comp.getLexical(1); }  
        
        if ("query".equals(keyword) || 
            "operation".equals(keyword)) 
        { tt = comp.parseOperation(entities,types); } 
        else if ("attribute".equals(keyword))
        { tt = comp.parseAttribute(entities,types); }
        else if ("class".equals(keyword))
        { tt = (ModelElement) 
                  comp.parseKM3Class(entities,types); 
        } 
        else if ("enumeration".equals(keyword))
        { tt = (ModelElement) 
                  comp.parseKM3Enumeration(entities,types); 
        } 
        

        if (tt != null) 
        { operationExamples.add(tt); 

          if (tt instanceof Entity && Entity.isEmptyClass(tt))
          { modelString = exampleName + " : OclEmptyClass\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (tt instanceof Entity && Entity.isNonEmptyClass(tt))
          { modelString = exampleName + " : OclNonEmptyClass\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (tt instanceof Type) 
          { modelString = exampleName + " : OclEnumeration\n" + 
              exampleName + ".ast = " + 
                 ((Type) tt).toDeclarationAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isStatic0(tt))
          { modelString = exampleName + " : OclStaticOp0\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isStaticN(tt))
          { modelString = exampleName + " : OclStaticOpN\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isQuery0(tt))
          { modelString = exampleName + " : OclQuery0\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isQueryN(tt))
          { modelString = exampleName + " : OclQueryN\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isUpdate0(tt))
          { modelString = exampleName + " : OclUpdate0\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (BehaviouralFeature.isUpdateN(tt))
          { modelString = exampleName + " : OclUpdateN\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (tt instanceof Attribute && 
                   ((Attribute) tt).isStatic())
          { modelString = exampleName + " : StaticAttribute\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else if (tt instanceof Attribute)
          { modelString = exampleName + " : InstanceAttribute\n" + 
              exampleName + ".ast = " + tt.toAST() + "\n"; 
             
            oclOperationModel = oclOperationModel + modelString + "\n";
          } 
          else 
          { System.err.println("!! Unknown declaration category: " + tt); }
        } 
      } 

      br.close(); 
    } catch(Exception _e)
      { System.err.println(">>> Error processing output/declarationExamples.txt");
        _e.printStackTrace();
      }

    System.out.println(">> Declaration examples: " + operationExamples); 
    System.out.println(">> Declaration model: " + oclOperationModel);


    // trule = 
    //    JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " attribute declarations): ");
    if (declnsr == null) 
    { System.err.println("!! No feature declarations rule"); 
      return; 
    } 

    String attTargetRule = declnsr; 

    // trule = 
    //     JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " operation declarations): ");
    if (declnsr == null) 
    { return; } 

    String operTargetRule = declnsr; 

    // trule = 
    //     JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " class/struct declarations): ");
    if (classesr == null) 
    { System.err.println("!! No class parser rule"); 
      return; 
    } 

    String classTargetRule = classesr; 

    // trule = 
    //     JOptionPane.showInputDialog("Enter target language parser rule (for " + targetLanguage + " enum declarations): ");
    if (enumsr == null) 
    { System.err.println("!! No rule for parsing enums"); 
      return; 
    } 

    String typeDecTargetRule = enumsr; 


    String opprogModelString = ""; 
    progid = ""; 
     
    try 
    { // File temp = new File("tmp.txt"); 
      Runtime proc = Runtime.getRuntime(); 
        
      for (int i = 0; i < programOperationExamples.size() && 
                      i < operationExamples.size(); 
           i++) 
      { // BufferedWriter bw = new BufferedWriter(new FileWriter(temp)); 
        String progEx = (String) programOperationExamples.get(i); 
        // bw.write(progEx + "\n");
        // bw.close();
        // Thread.sleep(50); 
        Process p; 

        String dec = ""; 

        if (operationExamples.get(i) instanceof BehaviouralFeature)
        { p = proc.exec("grun.bat " + targetLanguage + " " + operTargetRule + " -tree"); 
          dec = "ProgramOperation"; 
        } 
        else if (operationExamples.get(i) instanceof Attribute)
        { p = proc.exec("grun.bat " + targetLanguage + " " + attTargetRule + " -tree"); 
          dec = "ProgramAttribute"; 
        } 
        else if (operationExamples.get(i) instanceof Type) 
        { p = proc.exec("grun.bat " + targetLanguage + " " + typeDecTargetRule + " -tree"); 
          dec = "ProgramEnumeration"; 
        } 
        else
        { p = proc.exec("grun.bat " + targetLanguage + " " + classTargetRule + " -tree"); 
          dec = "ProgramClass"; 
        } 
 
        // InputStream stdin = p.getInputStream(); 
        // StreamGobble igb = new StreamGobble(stdin); 
        // InputStream stderr = p.getErrorStream(); 
        // StreamGobble egb = new StreamGobble(stderr); 
 
        // egb.start(); igb.start();   
        // int exitp = p.waitFor();

        // File af = new File("ast.txt"); 
        // BufferedReader astbr = new BufferedReader(new FileReader(af));
        // String asttext = astbr.readLine(); 
        // astbr.close(); 

        OutputStream sout = p.getOutputStream(); 
        OutputStreamWriter outw = new OutputStreamWriter(sout); 
        BufferedWriter brw = new BufferedWriter(outw);
        brw.write(progEx + "\n"); 
        brw.close();  
  
        InputStream sin2 = p.getInputStream(); 
        InputStreamReader inr2 = new InputStreamReader(sin2); 
        BufferedReader ibr2 = new BufferedReader(inr2); 
        String stext = ""; 
        String oline2 = ibr2.readLine(); 
        System.out.println(">>> parsing .... " + progEx);
        while (oline2 != null) 
        { stext = oline2; 
          oline2 = ibr2.readLine();
        }

        String asttext = stext.trim();  
        int exitjar2 = p.waitFor(); 
        System.out.println(">>> Exit code: " + exitjar2);

        progid = "progop" + i; 
        opprogModelString = opprogModelString + 
            progid + " : " + dec + "\n" + 
            progid + ".ast = " + asttext + "\n\n"; 

        System.out.println("--- " + asttext);  
      } 
    } 
    catch (Exception fex) 
    { System.err.println("!! Error parsing program declaration examples!"); 
      fex.printStackTrace(); 
    }  

    Date d5 = new Date(); 
    long t5 = d5.getTime(); 

    System.out.println(">>> Time for pre-processing declarations: " + (t5-t4)); 

    System.out.println(opprogModelString); 

    File outfile = new File("output/out.txt"); 

    if (typeExamples.size() == programTypeExamples.size())
    { // Assume corresponding if no errors.
       
      try 
      { BufferedWriter bwout = new BufferedWriter(new FileWriter(outfile)); 
        bwout.write(oclTypeModel + "\n");
        bwout.write(progModelString + "\n");
        for (int k = 0; k < typeExamples.size(); k++) 
        { String oclObj = "ocltype" + k;
          String progObj = "progtype" + k; 
          bwout.write(oclObj + " |-> " + progObj + "\n");
        } 

        bwout.write("\n"); 

        if (expressionExamples.size() == programExpressionExamples.size())
        { bwout.write(oclExpressionModel + "\n");
          bwout.write(exprprogModelString + "\n");
          for (int h = 0; h < expressionExamples.size(); h++) 
          { String oclObj = "oclexpr" + h;
            String progObj = "progexpr" + h; 
            bwout.write(oclObj + " |-> " + progObj + "\n");
          }
        }  

        bwout.write("\n"); 

        if (statementExamples.size() == programStatementExamples.size())
        { bwout.write(oclStatementModel + "\n");
          bwout.write(statProgModelString + "\n");
          for (int h = 0; h < statementExamples.size(); h++) 
          { String oclObj = "oclstat" + h;
            String progObj = "progstat" + h; 
            bwout.write(oclObj + " |-> " + progObj + "\n");
          }
        }  

        bwout.write("\n"); 

        if (operationExamples.size() == programOperationExamples.size())
        { bwout.write(oclOperationModel + "\n");
          bwout.write(opprogModelString + "\n");
          for (int h = 0; h < operationExamples.size(); h++) 
          { String oclObj = "oclop" + h;
            String progObj = "progop" + h; 
            bwout.write(oclObj + " |-> " + progObj + "\n");
          }
        }  


        bwout.close(); 
      } 
      catch (Exception mex) { } 
    } 


    if (typeExamples.size() < programTypeExamples.size())   
    { System.err.println("!! Fewer OCL type examples than program examples -- some OCL examples may have wrong syntax"); } 
    else if (typeExamples.size() > programTypeExamples.size()) 
    { System.err.println("!! Fewer program type examples than OCL examples -- some program examples may have wrong syntax"); } 

    if (expressionExamples.size() < programExpressionExamples.size())   
    { System.err.println("!! Fewer OCL expression examples than program examples -- some OCL examples may have wrong syntax"); } 
    else if (expressionExamples.size() > programExpressionExamples.size()) 
    { System.err.println("!! Fewer program expression examples than OCL examples -- some program examples may have wrong syntax"); } 


    if (statementExamples.size() < programStatementExamples.size())   
    { System.err.println("!! Fewer OCL statement examples than program examples -- some OCL examples may have wrong syntax"); } 
    else if (statementExamples.size() > programStatementExamples.size()) 
    { System.err.println("!! Fewer program statement examples than OCL examples -- some program examples may have wrong syntax"); } 


    if (operationExamples.size() < programOperationExamples.size())   
    { System.err.println("!! Fewer OCL declaration examples than program examples -- some OCL examples may have wrong syntax"); } 
    else if (operationExamples.size() > programOperationExamples.size()) 
    { System.err.println("!! Fewer program declaration examples than OCL examples -- some program examples may have wrong syntax"); } 

  }

  public static void parseExamples(String fname, Vector sexamples, Vector texamples)
  { // reads file fname, each line is an example 
    // stext tabs ttext
    // sexamples will be the LHS texts, texamples the 
    // RHS texts

    try 
    { File tfile = new File(fname);
	  
      if (tfile == null) { return; }
	 
      System.out.println(">>> Loading examples from " + fname);
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      try
      { br = new BufferedReader(new FileReader(tfile)); }
      catch (FileNotFoundException fnfe)
      { System.out.println("File not found: " + tfile.getName());
        return; 
      }
  
      int linecount = 0; 

      while (!eof)
      { try 
        { s = br.readLine(); }
        catch (IOException ioe)
        { System.out.println("!! Reading " + tfile.getName() + " failed.");
          return; 
        }

        if (s == null) 
        { eof = true; 
          break; 
        }

        s = s.trim();

        if (s.length() == 0) 
        { continue; } 

        String progstring = ""; 
        int tabindex = s.indexOf("\t"); 
        if (tabindex >= 0) 
        { progstring = s.substring(tabindex+1,s.length());
          s = s.substring(0,tabindex);
          tabindex = progstring.indexOf("\t");   
          while (tabindex >= 0)
          { progstring = progstring.substring(tabindex+1,progstring.length()); 
            tabindex = progstring.indexOf("\t");   
          }

          System.out.println(">> Source text: " + s); 
          sexamples.add(s); 
          
          System.out.println(">> Target text: " + progstring); 
          texamples.add(progstring); 
        }  
    
        linecount++;
      } 

      br.close(); 
    } catch(Exception e)
      { System.err.println(">>> Error processing " + fname); }

    System.out.println(">> Source examples: " + sexamples); 
    System.out.println(">> Target examples: " + texamples);
  } 

  public static String applyAntlr(String language, String rulename, String inText)
  { // Returns AST from grun.bat language rulename -tree
    try { 
      Runtime proc = Runtime.getRuntime(); 
      Process p2 = proc.exec("grun.bat " + language + " " + rulename + " -tree"); 

      OutputStream sout = p2.getOutputStream(); 
      OutputStreamWriter outw = new OutputStreamWriter(sout); 
      BufferedWriter brw = new BufferedWriter(outw);
      brw.write(inText + "\n"); 
      brw.close();  
  
      InputStream sin2 = p2.getInputStream(); 
      InputStreamReader inr2 = new InputStreamReader(sin2); 
      BufferedReader ibr2 = new BufferedReader(inr2); 
      String stext = ""; 
      String oline2 = ibr2.readLine(); 
      System.out.println(">>> parsing .... " + inText);
      while (oline2 != null) 
      { stext = oline2; 
        oline2 = ibr2.readLine();
      }
      String asttext = stext.trim();  
      int exitjar2 = p2.waitFor(); 
      System.out.println(">>> Exit code: " + exitjar2);
      return asttext; 
    } 
    catch (Exception e) 
    { System.out.println(">>> Error in parsing: " + e);
      return ""; 
    } 
  } 

  public static void main(String[] args)
  { PreProcessModels.preprocess(); } 

} 
