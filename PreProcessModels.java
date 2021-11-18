/******************************
* Copyright (c) 2003--2021 Kevin Lano
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

import java.util.Vector;

public class PreProcessModels
{ // Read typeExamples.txt
  //      expressionExamples.txt
  //      statementExamples.txt
  //      operationExamples.txt
  //      attributeExamples.txt
  //      classExamples.txt

  public static void preprocess()
  { Vector typeExamples = new Vector(); // Type
    String oclTypeModel = ""; 

    try 
    { File tfile = new File("./output/typeExamples.txt");
	  
      if (tfile == null) { return; }
	 
      System.out.println(">>> Loading type examples");
 
      BufferedReader br = null;
      String s;
      boolean eof = false;
    
      Vector entities = new Vector(); 
      Vector types = new Vector(); 

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
        }  
    
        String exampleName = "ocltype" + linecount; 
        String modelString = ""; 

        linecount++;
 
        Compiler2 comp = new Compiler2();  
        comp.nospacelexicalanalysis(s); 
        Type tt = comp.parseType(entities,types); 
        if (tt != null) 
        { typeExamples.add(tt); 
          if ("void".equals(tt.getName()) ||
              Type.isPrimitiveType(tt))
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
      { System.err.println(">>> No file output/typeExamples.txt found"); }

    System.out.println(">> Type examples: " + typeExamples); 
    System.out.println(">> Type model: " + oclTypeModel); 
  }

  public static void main(String[] args)
  { PreProcessModels.preprocess(); } 

} 
