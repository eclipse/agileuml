import java.io.*;
import javax.swing.*;

import java.util.Vector; 

/******************************
* Copyright (c) 2003--2022 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* package: CSTL */ 


public class CSTL
{ // All *.cstl files in output directory are loaded

  static java.util.Map templates = new java.util.HashMap();

  public static void loadTemplates(Vector types, Vector entities)
  { File dir = new File("./cg");
    String[] dirfiles = dir.list();
    for (int i = 0; i < dirfiles.length; i++)
    { File sub = new File("./cg/" + dirfiles[i]);
      if (sub != null && dirfiles[i].endsWith(".cstl") && 
          !(dirfiles[i].equals("cg.cstl")))
      { System.out.println(">>> Found CSTL template: " + dirfiles[i]); 
        CGSpec cg = new CGSpec(entities); 
        CGSpec res = loadCSTL(cg, sub,types,entities); 
        if (res != null) 
        { addTemplate(dirfiles[i], res); }         
      }
    }
  }

  public static void loadTemplates(Vector types, Vector entities, String excluding)
  { File dir = new File("./cg");
    String[] dirfiles = dir.list();
    for (int i = 0; i < dirfiles.length; i++)
    { File sub = new File("./cg/" + dirfiles[i]);
      if (sub != null && dirfiles[i].endsWith(".cstl") && 
          !(dirfiles[i].equals(excluding)))
      { System.out.println(">>> Loading CSTL template: " + dirfiles[i]); 
        CGSpec cg = new CGSpec(entities); 
        CGSpec res = loadCSTL(cg, sub,types,entities); 
        if (res != null) 
        { addTemplate(dirfiles[i], res); }         
      }
    }
  }

  public static void loadTemplates(Vector fileNames, Vector types, Vector entities)
  { File dir = new File("./cg");
    String[] dirfiles = dir.list();
    for (int i = 0; i < dirfiles.length; i++)
    { File sub = new File("./cg/" + dirfiles[i]);
      if (sub != null && dirfiles[i].endsWith(".cstl") && 
          fileNames.contains(dirfiles[i]))
      { System.out.println(">>> Found CSTL template: " + dirfiles[i]); 
        CGSpec cg = new CGSpec(entities); 
        CGSpec res = loadCSTL(cg, sub,types,entities); 
        if (res != null) 
        { addTemplate(dirfiles[i], res); }         
      }
    }
  }

  public static CGSpec loadCSTL(File file, Vector types, Vector entities)
  { CGSpec cg = new CGSpec(entities); 
    return loadCSTL(cg, file, types, entities);
  }  


  public static CGSpec loadCSTL(CGSpec res, File file, Vector types, Vector entities)
  { 
    BufferedReader br = null;
    String s;
    boolean eof = false;
    
    try
    { br = new BufferedReader(new FileReader(file)); }
    catch (FileNotFoundException _e)
    { System.err.println("!! ERROR: File not found: " + file);
      return null; 
    }

    int noflines = 0; 
    String mode = "none"; 
    String category = null; 

    while (!eof)
    { try { s = br.readLine(); }
      catch (IOException _ex)
      { System.out.println("ERROR!!: Reading CSTL file failed.");
        return null; 
      }

      if (s == null) 
      { eof = true; 
        break; 
      }
      
      s = s.trim(); 

      if (s.startsWith("/*") && s.endsWith("*/")) 
      { } 
      else if (s.length() == 0) { } 
      // else if (s.startsWith("import "))
      // { String[] strs = s.split(" "); 
      //   if (strs.length > 1) 
      //   { String toimport = strs[1]; 
      //     File ff = new File("./cg/" + toimport); 
      //     loadCSTL(res, ff, types, entities); 
      //   }
      // }  
      else if (s.equals("Type::"))
      { mode = "types"; }         
      else if (s.equals("Enumeration::"))
      { mode = "enumerations"; }         
      else if (s.equals("Datatype::"))
      { mode = "datatypes"; }         
      else if (s.equals("BasicExpression::"))
      { mode = "basicexpressions"; }  
      else if (s.equals("UnaryExpression::"))
      { mode = "unaryexpressions"; }  
      else if (s.equals("BinaryExpression::"))
      { mode = "binaryexpressions"; }  
      else if (s.equals("SetExpression::"))
      { mode = "setexpressions"; }  
      else if (s.equals("ConditionalExpression::"))
      { mode = "conditionalexpressions"; }  
      else if (s.equals("Class::"))
      { mode = "entities"; }  
      else if (s.equals("Attribute::"))
      { mode = "attributes"; }  
      else if (s.equals("Parameter::"))
      { mode = "parameters"; }  
      else if (s.equals("ParameterArgument::"))
      { mode = "parameterarguments"; }  
      else if (s.equals("Operation::"))
      { mode = "operations"; }  
      else if (s.equals("Statement::"))
      { mode = "statements"; }  
      else if (s.equals("Package::"))
      { mode = "packages"; }  
      else if (s.equals("UseCase::"))
      { mode = "usecases"; }  
      else if (s.endsWith("::"))
      { mode = "texts";
        int colonindex = s.indexOf(":");  
        category = s.substring(0,colonindex); 
      }         
      else if ("types".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_TypeCodegenerationrule(s.trim());
        if (r != null) 
        { res.addTypeUseRule(r); } 
        else 
        { alertRule("Type", s); }
      }  
      else if ("basicexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addBasicExpressionRule(r); } 
        else 
        { alertRule("Basic expression", s); }
      }  
      else if ("unaryexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_UnaryExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addUnaryExpressionRule(r); } 
        else 
        { alertRule("Unary expression", s); }
      }  
      else if ("binaryexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addBinaryExpressionRule(r); } 
        else 
        { alertRule("Binary expression", s); }
      }  
      else if ("setexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addSetExpressionRule(r); }
        else 
		{ alertRule("Set expression", s); }
      }  
      else if ("conditionalexpressions".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_ExpressionCodegenerationrule(s.trim());
        if (r != null) 
        { res.addConditionalExpressionRule(r); } 
        else 
		{ alertRule("Conditional expression", s); }
      }  
      else if ("entities".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addClassRule(r); } 
        else 
		{ alertRule("Entity", s); }
      }         
      else if ("enumerations".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addEnumerationRule(r); } 
        else 
        { alertRule("Enumeration", s); }
      } 
      else if ("datatypes".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim());
        if (r != null) 
        { res.addDatatypeRule(r); } 
        else 
        { alertRule("Datatype", s); }
      }          
      else if ("packages".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_EntityCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addPackageRule(r); } 
        else 
		{ alertRule("Package", s); }
      }         
      else if ("attributes".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_AttributeCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addAttributeRule(r); }
        else 
		{ alertRule("Attribute/reference", s); }
 
      }         
      else if ("operations".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_OperationCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addOperationRule(r); } 
        else 
		{ alertRule("Operation", s); }

      }  
      else if ("parameters".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_OperationCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addParameterRule(r); }
        else 
        { alertRule("Parameter", s); } 
      }       
      else if ("parameterarguments".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_OperationCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addParameterArgumentRule(r); } 
        else 
        { alertRule("Parameter argument", s); } 
      }       
      else if ("statements".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_StatementCodegenerationrule(s.trim(),entities,types); 
        if (r != null) 
        { res.addStatementRule(r); } 
        else 
        { alertRule("Statement", s); } 
      }         
      else if ("usecases".equals(mode))
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_UseCaseCodegenerationrule(s.trim(),entities,types); 
        if (r != null) 
        { res.addUseCaseRule(r); }
        else 
        { alertRule("UseCase", s); }  
      }         
      else if ("texts".equals(mode) && category != null)
      { Compiler2 c = new Compiler2(); 
        CGRule r = c.parse_TextCodegenerationrule(s.trim()); 
        if (r != null) 
        { res.addCategoryRule(category,r); }
        else 
        { alertRule("Could not parse category " + category + " rule", s); }  
      }         
    }
    System.out.println(">>> Parsed: " + res); 
    return res; 
  }

  private static void alertRule(String kind, String r)
  { System.err.println("!!! Unable to parse " + kind + " rule: " + r);
    JOptionPane.showMessageDialog(null, "Warning: Unable to parse " + kind + " rule: " + r, "", JOptionPane.ERROR_MESSAGE);
  }

  public static void addTemplate(String filename, CGSpec cg) 
  { templates.put(filename,cg); }

  public static boolean hasTemplate(String filename)
  { CGSpec cg = (CGSpec) templates.get(filename); 
    if (cg != null) 
    { return true; } 
    return false; 
  } 

  public static CGSpec getTemplate(String name)
  { return (CGSpec) templates.get(name); }
}

