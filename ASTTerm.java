/******************************
* Copyright (c) 2003--2025 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

import java.util.Vector; 
import java.io.*; 
import javax.swing.*;

import java.lang.reflect.Method;


public abstract class ASTTerm
{ String id = ""; 
  // Vector stereotypes = new Vector(); 
  

  // A programming language AST can be represented by
  // one of the following UML/OCL things:
  
  Expression expression = null; 
  Statement statement = null; 
  ModelElement modelElement = null; 
  Vector modelElements = null;   
  Vector expressions = null; // for parameter/argument lists

  static Vector requiredLibraries = new Vector(); 

  static String packageName = null; 
  static Vector enumtypes; 
  static Vector entities; 
  static Vector functionsInScope; 

  static Vector cobolClasses = new Vector(); 

  static Entity currentClass = null; // Current context class

  static java.util.Map metafeatures; 
  static { metafeatures = new java.util.HashMap(); } 
     // String --> Vector(String), 
     // eg., recording the conceptual
     // type of the element & stereotypes. 

  static java.util.Map types = new java.util.HashMap(); 
     // String --> String for general type of identifiers
     // valid at the scope of the current term. 

  static java.util.Map elementTypes = new java.util.HashMap(); 
     // String --> String for general elementType 
     // of identifiers
     // valid at the scope of the current term. 

  static java.util.Map cg_cache = new java.util.HashMap(); 
     // CGSpec --> (ASTTerm --> String)
     // But invalid to do this if the denotation needs to 
     // change, eg., a[x] is different for array/map a

  static java.util.Map mathoclvars; 
  static 
  { mathoclvars = new java.util.HashMap(); }  
     // String --> ASTTerm

  static Vector mathocltheorems; 
  static 
  { mathocltheorems = new Vector(); }  
     // pairs [concl,premise]

  static Vector mathoclrewrites; 
  static 
  { mathoclrewrites = new Vector(); }  
     // pairs [lhs,rhs]

  static int mathoclfunctionIndex; 

  static String cobolHyphenReplacement; 
  static 
  { cobolHyphenReplacement = "_"; } // or "$" for Java

  static String cobolCommaReplacement; 
  static 
  { cobolCommaReplacement = "."; } 

  static int cobolFillerCount; 
  static 
  { cobolFillerCount = 0; } 

  static Vector cobolDataDescriptionDataNames; 
  static
  { cobolDataDescriptionDataNames = new Vector(); } 

  static Vector cobolAmbiguousDataNames; 
  static
  { cobolAmbiguousDataNames = new Vector(); } 

  static Vector cqueryfunctions = new Vector(); 
  static
  { cqueryfunctions.add("sin"); 
    cqueryfunctions.add("cos"); 
    cqueryfunctions.add("tan"); 
    cqueryfunctions.add("asin"); 
    cqueryfunctions.add("acos"); 
    cqueryfunctions.add("atan");
    cqueryfunctions.add("sinh"); 
    cqueryfunctions.add("cosh"); 
    cqueryfunctions.add("tanh"); 
    cqueryfunctions.add("exp"); 
    cqueryfunctions.add("log"); 
    cqueryfunctions.add("log10"); 
    cqueryfunctions.add("sqrt"); 
    cqueryfunctions.add("ceil"); 
    cqueryfunctions.add("floor"); 
    cqueryfunctions.add("fabs"); 
    cqueryfunctions.add("abs"); 
    cqueryfunctions.add("labs"); 
    cqueryfunctions.add("pow"); 
    cqueryfunctions.add("atan2"); 
    cqueryfunctions.add("ldexp"); 
    cqueryfunctions.add("fmod"); 
    cqueryfunctions.add("atof"); 
    cqueryfunctions.add("atoi"); 
    cqueryfunctions.add("atol"); 
    cqueryfunctions.add("isalnum"); 
    cqueryfunctions.add("isalpha"); 
    cqueryfunctions.add("isspace"); 
    cqueryfunctions.add("isdigit"); 
    cqueryfunctions.add("isupper");
    cqueryfunctions.add("islower"); 
    cqueryfunctions.add("iscntrl"); 
    cqueryfunctions.add("isgraph"); 
    cqueryfunctions.add("isprint"); 
    cqueryfunctions.add("ispunct"); 
    cqueryfunctions.add("isxdigit");
    cqueryfunctions.add("isnan");  
    cqueryfunctions.add("calloc"); 
    cqueryfunctions.add("malloc"); 
    // cqueryfunctions.add("realloc"); 
    cqueryfunctions.add("strcmp"); 
    cqueryfunctions.add("strncmp"); 
    cqueryfunctions.add("strchr"); 
    cqueryfunctions.add("strrchr"); 
    cqueryfunctions.add("strlen"); 
    cqueryfunctions.add("strstr"); 
    cqueryfunctions.add("strpsn"); 
    cqueryfunctions.add("strcpsn"); 
    cqueryfunctions.add("strpbrk"); 
    cqueryfunctions.add("strerror"); 
    cqueryfunctions.add("getenv"); 
    cqueryfunctions.add("bsearch"); 
    cqueryfunctions.add("toupper"); 
    cqueryfunctions.add("tolower");
    cqueryfunctions.add("fopen");
    cqueryfunctions.add("div"); 
    cqueryfunctions.add("ldiv");

    cqueryfunctions.add("time"); 
    cqueryfunctions.add("difftime"); 
    cqueryfunctions.add("localtime"); 
    cqueryfunctions.add("mktime"); 
    cqueryfunctions.add("asctime");
    cqueryfunctions.add("ctime"); 
    cqueryfunctions.add("gmtime"); 
  }

  public abstract String toString(); 

  public abstract String getTag(); 

  public boolean hasNestedTag(String tg)
  { java.util.Set alltags = allTagsIn(); 
    if (alltags.contains(tg))
    { return true; } 
    return false; 
  }  

  public boolean hasNestedTags(Vector tgs)
  { java.util.Set alltags = allTagsIn(); 
    for (int i = 0; i < tgs.size(); i++) 
    { String tg = (String) tgs.get(i); 
      if (alltags.contains(tg))
      { } 
      else 
      { return false; } 
    } 
    return true;  
  }  

  public boolean hasExactNestedTags(Vector tgs)
  { java.util.Set alltags = allTagsIn(); 
    if (tgs.containsAll(alltags) && 
        alltags.containsAll(tgs))
    { return true; } 
    return false;   
  }  

  public abstract String literalForm();

  public abstract String literalFormSpaces();

  public abstract String evaluationLiteralForm();

  public static Vector getLiteralForms(Vector trms) 
  { Vector res = new Vector(); 
    for (int i = 0; i < trms.size(); i++) 
    { ASTTerm trm = (ASTTerm) trms.get(i); 
      res.add(trm.literalForm()); 
    } 
    return res; 
  } 

  public static void putCg_cache(CGSpec cgs, ASTTerm trm, String res)
  { java.util.Map cgsmap = (java.util.Map) cg_cache.get(cgs); 
    if (cgsmap == null) 
    { cgsmap = new java.util.HashMap(); }  
    cgsmap.put(trm, res); 
    cg_cache.put(cgs, cgsmap); 
  } 

  public static String getCg_cache(CGSpec cgs, ASTTerm trm)
  { java.util.Map cgsmap = (java.util.Map) cg_cache.get(cgs); 
    if (cgsmap == null) 
    { return null; }  
    String res = (String) cgsmap.get(trm); 
    return res; 
  } 

  public abstract ASTTerm removeWhitespaceTerms();

  public abstract ASTTerm removeExtraNewlines();

  public abstract ASTTerm replaceCobolIdentifiers();

  public abstract ASTTerm replaceAmbiguousCobolNames(Vector rnames);

  public abstract ASTTerm substituteEq(String str, ASTTerm newtrm);
  
  public abstract String tagFunction(); 

  public static Vector allNestedTagsArities(Vector sasts)
  { // [tag,arity] for each inner term of sasts. 

    Vector res = new Vector(); 
    for (int i = 0; i < sasts.size(); i++) 
    { ASTTerm trm = (ASTTerm) sasts.get(i); 
      Vector trms = trm.getTerms(); 
      for (int j = 0; j < trms.size(); j++) 
      { ASTTerm tj = (ASTTerm) trms.get(j); 
        Vector alltags = tj.allNestedTagsArities(); 
        res.addAll(alltags); 
      } 
    } 
    return res; 
  } 

  public static Vector allTagsArities(Vector sasts)
  { // [tag,arity] for each term of sasts and subterm, 
    // recursively. 

    Vector res = new Vector(); 
    for (int i = 0; i < sasts.size(); i++) 
    { ASTTerm trm = (ASTTerm) sasts.get(i); 
      Vector alltags = trm.allTagsArities(); 
      res.addAll(alltags); 
    } 
    return res; 
  } 

  public static Vector allTagsAtIndex(int index, ASTTerm[] sasts)
  { // tags for each i'th term of sasts

    Vector alltags = new Vector(); 

    Vector res = new Vector(); 
    for (int i = 0; i < sasts.length; i++) 
    { ASTTerm trm = (ASTTerm) sasts[i];
      ASTTerm termi = (ASTTerm) trm.getTerm(index);  
      String tagi = termi.getTag(); 
      if (tagi != null)
      { if (alltags.contains(tagi)) { } 
        else 
        { alltags.add(tagi); } 
      } 
    } 

    return alltags; 
  } 

  public static Vector allTagSetsAtIndex(int index, ASTTerm[] sasts)
  { // tags for each i'th term of sasts

    Vector alltags = new Vector(); 

    Vector res = new Vector(); 
    for (int i = 0; i < sasts.length; i++) 
    { ASTTerm trm = (ASTTerm) sasts[i];
      ASTTerm termi = (ASTTerm) trm.getTerm(index);  
      java.util.Set tagi = termi.allTagsIn(); 
      if (tagi != null)
      { if (alltags.contains(tagi)) { } 
        else 
        { alltags.add(tagi); } 
      } 
    } 

    return alltags; 
  } 
    
  public abstract Vector allNestedTagsArities(); 

  public abstract Vector allTagsArities(); 

  public abstract java.util.Set allTagsIn(); 

  public abstract Vector allIdentifiers(Vector tags);

  public static Vector rulesFromTagsArities(Vector tagsarities)
  { // For each (tag,n) : tagsarities
    // define CGRule  tag:: _1 |-->_1 or _* |-->_*
    java.util.Map cgrules = new java.util.HashMap();

    for (int k = 0; k < tagsarities.size(); k++) 
    { Vector tgar = (Vector) tagsarities.get(k); 
      String tg = (String) tgar.get(0); 
      int ar = (int) tgar.get(1);
      Vector rls = (Vector) cgrules.get(tg); 
      if (rls == null) 
      { rls = new Vector(); }  
      if (ar == 1) 
      { if (rls.contains("_1 |-->_1")) { } 
        else 
        { rls.add("_1 |-->_1"); }
      } 
      else 
      { if (rls.contains("_* |-->_*")) { } 
        else 
        { rls.add("_* |-->_*"); }  
      }
      cgrules.put(tg,rls);   
    } 

    Vector res = new Vector();
    for (Object obj : cgrules.keySet())
    { String key = (String) obj;   
      Vector rls = (Vector) cgrules.get(key); 
      String rle = key + "::\n"; 
      for (int p = 0; p < rls.size(); p++) 
      { rle = rle + rls.get(p) + "\n"; } 
      res.add(rle); 
    } 
    return res; 
  } 

  public static boolean isSubterm(ASTTerm t1, ASTTerm t2)
  { if (t1 == null || t2 == null) 
    { return false; } 

    String lit1 = t1.literalForm(); 
    String lit2 = t2.literalForm(); 

    if (lit1.equals(lit2))
    { return true; } // Only for t2 : ASTSymbolTerm, ASTBasicTerm

    if (t2 instanceof ASTCompositeTerm) 
    { ASTCompositeTerm ct = (ASTCompositeTerm) t2; 
      Vector trms = ct.getTerms(); 
      for (int i = 0; i < trms.size(); i++) 
      { ASTTerm subtrm = 
           (ASTTerm) trms.get(i); 
        if (ASTTerm.isSubterm(t1, subtrm))
        { return true; } 
      } 
      return false; 
    } 

    return false; 
  } 

  public static boolean isTermOf(ASTTerm sub, ASTTerm sup)
  { // sub is literally equal to a subterm of sup

    String sublit = sub.literalForm(); 

    Vector trms = sup.getTerms(); 
    for (int i = 0; i < trms.size(); i++) 
    { ASTTerm trm = (ASTTerm) trms.get(i); 
      if (sublit.equals(trm.literalForm()))
      { return true; } 
    } 
    return false; 
  } 
      
  public Vector allTagSubterms(String tagx)
  { Vector res = new Vector(); 
    if (getTag().equals(tagx))
    { res.add(this); }
    Vector trms = getTerms(); 
    for (int i = 0; i < trms.size(); i++) 
    { ASTTerm trm = (ASTTerm) trms.get(i); 
      Vector nres = trm.allTagSubterms(tagx); 
      res.addAll(nres); 
    } 
    return res; 
  }  

  


  public void addStereotype(String str) 
  { String lit = literalForm(); 
    Object stereo = ASTTerm.metafeatures.get(lit); 
    Vector stereotypes = new Vector(); 
    if (stereo == null)  
    { stereotypes.add(str); 
      ASTTerm.metafeatures.put(lit,stereotypes); 
      return; 
    } 
    else if (!(stereo instanceof Vector)) 
    { return; } // single-valued metafeature.  

    // System.out.println(">++++> Adding stereotype " + str + " to " + lit); 

    stereotypes = (Vector) stereo; 

    if (stereotypes.contains(str)) {} 
    else 
    { stereotypes.add(str); 
      ASTTerm.metafeatures.put(lit,stereotypes);
    }

    // JOptionPane.showMessageDialog(null, 
    //    "*** addStereotype " + str + " to " + lit + " Metafeature values of " + lit + " are " + stereotypes,   "",
    //          JOptionPane.INFORMATION_MESSAGE); 
  } 

  public static void addStereo(ASTTerm ast, String str)
  { if (ast == null) { return; } 

    String lit = ast.literalForm(); 
    addStereo(lit,str); 
  } 

  public static void addStereo(String lit, String str)
  { Object mfs = ASTTerm.metafeatures.get(lit); 
    if (mfs == null) 
    { mfs = new Vector(); 
      ASTTerm.metafeatures.put(lit, mfs); 
    } 

    if (mfs instanceof Vector)
    { 
      Vector stereotypes = (Vector) mfs;  

      if (stereotypes.contains(str)) {} 
      else 
      { stereotypes.add(0,str); 
        ASTTerm.metafeatures.put(lit, stereotypes); 
      }
    } 

    // JOptionPane.showMessageDialog(null, 
    //    "*** addStereo " + str + " to " + lit + " Metafeature values of " + lit + " are " + mfs,   "",
    //          JOptionPane.INFORMATION_MESSAGE); 

    System.out.println("*** " + lit + " metafeature values set to " +
                       ASTTerm.metafeatures.get(lit));  

  } 

  public static void setTaggedValue(ASTTerm ast, String mf, String val) 
  { // updates stereotypes of ast so that mf=val

    String lit = ast.literalForm();

    setTaggedValue(lit, mf, val); 
  } 


  public static void setTaggedValue(String lit, String mf, String val) 
  { Object mfs = ASTTerm.metafeatures.get(lit); 
    // System.out.println("*** " + lit + " has tagged values: " + 
    //                    mfs); 

    if (mfs == null) 
    { mfs = new Vector(); 
      ASTTerm.metafeatures.put(lit,mfs); 
    }

    if (mfs instanceof Vector)
    { Vector stereos = (Vector) mfs; 
      Vector newstereos = new Vector(); 
      for (int x = 0; x < stereos.size(); x++) 
      { String stereo = (String) stereos.get(x); 
        if (stereo.startsWith(mf + "=") ||
            stereo.startsWith(mf + " ="))
        { }
        else 
        { newstereos.add(stereo); } 
      } 
      newstereos.add(mf + "=" + val); 
      ASTTerm.metafeatures.put(lit,newstereos); 
      System.out.println("*** Set " + lit + 
                         " tagged values: " + 
                         newstereos); 
    } 
  }


  public void removeStereotype(String str) 
  { String lit = literalForm(); 
    
    Object mfs = ASTTerm.metafeatures.get(lit); 
    if (mfs == null) 
    { mfs = new Vector(); 
      ASTTerm.metafeatures.put(lit,mfs); 
    }
   
    if (mfs instanceof Vector)
    { 
      Vector stereotypes = (Vector) mfs; 
      Vector removed = new Vector(); 
      removed.add(str); 
      stereotypes.removeAll(removed);
      ASTTerm.metafeatures.put(lit,stereotypes);  
    } 

    System.out.println(">>> " + lit + " metafeature values = " +
                       ASTTerm.metafeatures.get(lit));  
  } 

  public static void removeStereo(String lit, String str) 
  { if (ASTTerm.metafeatures.get(lit) instanceof Vector)
    { 
      Vector stereotypes = 
        (Vector) ASTTerm.metafeatures.get(lit); 
      if (stereotypes == null) 
      { stereotypes = new Vector(); 
        ASTTerm.metafeatures.put(lit,stereotypes); 
      } 
      Vector removed = new Vector(); 
      removed.add(str); 
      stereotypes.removeAll(removed);
    }   

    System.out.println("*** " + lit + " metafeature values= " +
                       ASTTerm.metafeatures.get(lit));  
  } 

  public boolean hasStereotype(String str) 
  { String lit = literalForm(); 
    Object mfs = ASTTerm.metafeatures.get(lit); 
    if (mfs == null) 
    { return false; } 
 
    if (mfs instanceof Vector)
    { Vector stereotypes = (Vector) mfs; 
      return stereotypes.contains(str);
    } 

    return false;  
  } 

  public static String getStereotypeValue(String lit)
  { Object stereo = 
      ASTTerm.metafeatures.get(lit); 
    System.out.println(">>>--- Global variable " +  lit + 
                       " has value " + stereo);
    System.out.println();   
    if (stereo == null) 
    { return ""; } 
    else 
    { return "" + stereo; } 
  } 

  public static void setStereotypeValue(String lit, String val)
  { System.out.println(">>>--- Global variable " +  lit + 
                       " set to " + val);  
    System.out.println(); 
    ASTTerm.metafeatures.put(lit,val); 
  } 

  public static Vector getStereotypes(String lit) 
  { Object mfs = ASTTerm.metafeatures.get(lit); 
    if (mfs == null)
    { return new Vector(); }
    if (mfs instanceof Vector)
    { return (Vector) mfs; } 
    return null; 
  }  

  public static Vector getStereotypes(ASTTerm t) 
  { return getStereotypes(t.literalForm()); } 

  public static boolean hasTaggedValue(ASTTerm trm, String str) 
  { String lit = trm.literalForm();
    System.out.println("*** " + lit + " has tagged values: " + 
                       ASTTerm.metafeatures.get(lit)); 

    if (ASTTerm.metafeatures.get(lit) instanceof Vector)
    { 
      Vector stereotypes = 
        (Vector) ASTTerm.metafeatures.get(lit); 
      if (stereotypes == null) 
      { stereotypes = new Vector(); 
        ASTTerm.metafeatures.put(lit,stereotypes); 
      } 

      for (int x = 0; x < stereotypes.size(); x++) 
      { String stereo = (String) stereotypes.get(x); 
        if (stereo.startsWith(str + "=")) // or " ="
        { return true; } 
      } 
      return false; 
    } 
    return false; 
  } 

  public static String getTaggedValue(ASTTerm trm, String str) 
  { String lit = trm.literalForm(); 
    Object mfs = ASTTerm.metafeatures.get(lit); 
    if (mfs == null) 
    { mfs = new Vector(); 
      ASTTerm.metafeatures.put(lit,mfs); 
    } 

    // JOptionPane.showMessageDialog(null, 
    //   "Looking up: " + str + " of: " + lit + 
    //           " in: " + ASTTerm.metafeatures,   "",
    //           JOptionPane.INFORMATION_MESSAGE);

    // System.out.println("*** " + lit + 
    //                    " gets tagged values: " + 
    //                    mfs); 

    if (mfs instanceof Vector)
    { Vector stereotypes = (Vector) mfs; 
      for (int x = 0; x < stereotypes.size(); x++) 
      { String stereo = (String) stereotypes.get(x); 
        if (stereo.startsWith(str + "=")) // or " ="
        { int indx = stereo.indexOf("="); 
          return stereo.substring(indx + 1); 
        } 
      }
    }  

    return null; 
  } 

  public static String getTaggedValue(String lit, String str) 
  { Object mfs = ASTTerm.metafeatures.get(lit); 
     
    if (mfs instanceof Vector)
    { Vector stereotypes = (Vector) mfs; 
      for (int x = 0; x < stereotypes.size(); x++) 
      { String stereo = (String) stereotypes.get(x); 
        if (stereo.startsWith(str + "=")) // or " ="
        { int indx = stereo.indexOf("="); 
          return stereo.substring(indx + 1); 
        } 
      }
    }  

    return null; 
  } 

  public static String cgtlOperation(String opname, Vector eargs)
  { System.out.println(">>> External operation: " + opname + " on " + eargs); 

    /* System.out.println(">>> metafeatures: " + ASTTerm.metafeatures); 
    System.out.println(); */ 

    if ("symbolicAddition".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicAddition(e1,e2); 
    } 

    if ("symbolicSubtraction".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicSubtraction(e1,e2); 
    } 

    if ("symbolicMultiplication".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicMultiplication(e1,e2); 
    } 

    if ("symbolicDivision".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicDivision(e1,e2); 
    } 

    if ("symbolicNegateMultiplication".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicNegateMultiplication(e1,e2); 
    } 

    if ("symbolicLess".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicLess(e1,e2); 
    } 

    if ("symbolicLeq".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.symbolicLeq(e1,e2); 
    } 

    if ("symbolicDeterminant".equals(opname) && 
        eargs.size() == 1)
    { ASTTerm m = (ASTTerm) eargs.get(0); 
      return ASTTerm.symbolicDeterminant(m); 
    } 

    if ("expressAsPolynomial".equals(opname) && 
        eargs.size() == 2)
    { ASTTerm e1 = (ASTTerm) eargs.get(0); 
      ASTTerm e2 = (ASTTerm) eargs.get(1); 
      return ASTTerm.expressAsPolynomial(e1,e2); 
    } 

    // Also Eq and Neq

    if ("equationSolution".equals(opname) && 
        eargs.size() == 2)
    { // Solve exprs for ids
      ASTTerm exprs = (ASTTerm) eargs.get(0); 
      ASTTerm ids = (ASTTerm) eargs.get(1); 
      return ASTTerm.solveEquations(exprs,ids); 
    } 

    if ("attemptProof".equals(opname) && 
        eargs.size() == 2)
    { // Prove expr if assump
      ASTTerm exprs = (ASTTerm) eargs.get(0); 
      ASTTerm ids = (ASTTerm) eargs.get(1); 
      return ASTTerm.attemptProof(exprs,ids); 
    } 

    if ("attemptSubstitution".equals(opname) && 
        eargs.size() == 2)
    { // Prove expr if assump
      ASTTerm vbl = (ASTTerm) eargs.get(0); 
      ASTTerm expr = (ASTTerm) eargs.get(1); 
      String var = vbl.literalForm(); 
      ASTTerm def = (ASTTerm) ASTTerm.mathoclvars.get(var); 
      if (def == null) 
      { return "  Substitute " + var + " in " + 
               expr.literalFormSpaces(); 
      } 
      ASTTerm res = expr.mathOCLSubstitute(var,def);
      return res.literalFormSpaces();  
    } 
    /* _2<when> _2 expression<action> _1`value / _1
       Substitute _1 in _2 |-->Substitute _1 in _2
    */ 

    if ("groupTermsByPower".equals(opname) && 
        eargs.size() == 2)
    { // Prove expr if assump
      ASTTerm exprs = (ASTTerm) eargs.get(0); 
      ASTTerm ids = (ASTTerm) eargs.get(1); 
      return ASTTerm.groupTermsByPower(exprs,ids); 
    } 

    if ("pythonEval".equals(opname) && 
        eargs.size() == 1)
    { // (trailer (arguments ( 
      //   (arglist (argument ... "text" ...)) ) ))

      ASTTerm e2 = (ASTTerm) eargs.get(0);
      String arg = e2.literalForm(); 
      if (arg.startsWith("(") &&
          arg.endsWith(")"))
      { arg = arg.substring(1,arg.length()-1); }

      if (arg.startsWith("\"") &&
          arg.endsWith("\""))
      { arg = arg.substring(1,arg.length()-1); }
        
      String astexpr = 
            PreProcessModels.applyAntlr("Python",
                                   "expr",
                                   arg);
      if (astexpr == null || 
          "".equals(astexpr))
      { return "null"; } 

      Compiler2 c = new Compiler2();    

      ASTTerm xx =
             c.parseGeneralAST(astexpr); 

      if (xx == null) 
      { return "null"; } 

      return PreProcessModels.applyCGTL(xx, 
                                        "cg/python2UML.cstl"); 
    }       

    if ("pythonExec".equals(opname) && 
        eargs.size() == 1)
    { // (trailer (arguments ( 
      //   (arglist (argument ... "text" ...)) ) ))

      ASTTerm e2 = (ASTTerm) eargs.get(0);
      String arg = e2.literalForm(); 
      if (arg.startsWith("(") &&
          arg.endsWith(")"))
      { arg = arg.substring(1,arg.length()-1); }

      if (arg.startsWith("\"") &&
          arg.endsWith("\""))
      { arg = arg.substring(1,arg.length()-1); }
        
      String astexpr = 
            PreProcessModels.applyAntlr("Python",
                                   "stmt",
                                   arg);
      if (astexpr == null || 
          "".equals(astexpr))
      { return "null"; } 

      Compiler2 c = new Compiler2();    

      ASTTerm xx =
             c.parseGeneralAST(astexpr); 

      if (xx == null) 
      { return "null"; } 

      return PreProcessModels.applyCGTL(xx, 
                                        "cg/python2UML.cstl"); 
    }       

    int dotind = opname.indexOf("."); 
    // System.out.println(dotind); 
    if (dotind > 0)
    { String cname = opname.substring(0,dotind); 
      String mname = opname.substring(dotind + 1); 

      try { 
        Class cl = Class.forName(cname); 
        Object xinst = cl.newInstance();
        Method[] mets = cl.getDeclaredMethods(); 

        // System.out.println(">> Found class: " + cl + " methods " + mets.length);
 
        for (int i = 0; i < mets.length; i++)
        { Method m = mets[i]; 

          // System.out.println(m.getName()); 

          if (m.getName().equals(mname))
          { Object[] args = eargs.toArray(); 
            return (String) m.invoke(xinst,args); 
          } 
        } 
      }
      catch (Throwable _x) 
      { System.err.println("!! No class/method: " + cname + " " + mname); } 
    }

    return opname + "(" + eargs + ")"; 
  } 

  public static void addRequiredLibrary(String lib) 
  { if (requiredLibraries.contains(lib)) {}
    else 
    { requiredLibraries.add(lib); } 
  } 

  public static Vector getRequiredLibraries()
  { return requiredLibraries; } 

  public abstract ASTTerm removeOuterTag();
  /* For symbol terms return null; basic terms the 
     symbol of value; composite terms - the first term */  

  public abstract ASTTerm getTerm(int i); 

  public abstract Vector tokenSequence(); 

  public abstract int termSize(); 

  public abstract int size(); 

  public abstract Vector getTerms(); 

  public abstract Vector symbolTerms(); 

  public abstract Vector nonSymbolTerms(); 

  public abstract String toJSON(); 

  public abstract String asTextModel(PrintWriter out); 

  public abstract String cg(CGSpec cgs); 

  public abstract String cgRules(CGSpec cgs, Vector rules); 

  public abstract java.util.Set allMathMetavariables(); 

  public abstract java.util.HashMap hasMatch(ASTTerm rterm, 
                                    java.util.HashMap res); 
  
  public abstract java.util.HashMap fullMatch(ASTTerm rterm, 
                             java.util.HashMap res); 

  public abstract ASTTerm instantiate( 
                             java.util.HashMap res); 

  // Only for programming languages. 
  public abstract boolean updatesObject(ASTTerm t); 

  public abstract ASTTerm updatedObject();

  public abstract boolean callSideEffect(); 

  // Only for programming languages.

  public abstract boolean hasPreSideEffect();
 
  public abstract boolean hasPostSideEffect();

  public abstract boolean hasSideEffect(); 

  public abstract boolean isIdentifier(); 

  public static boolean isMathOCLIdentifier(String str)
  { int n = str.length(); 
    if (n > 0 && Character.isJavaIdentifierStart(str.charAt(0))) 
    { for (int i = 1; i < n; i++)
      { if (Character.isJavaIdentifierPart(str.charAt(i))) { } 
        else 
        { return false; } 
      } 
      return true; 
    } 
    return false; 
  } // also g{p} etc. 
 
  public abstract String preSideEffect(); 

  public abstract String postSideEffect(); 

  public boolean hasMetafeature(String f) 
  { Object mf = metafeatures.get(f); 
    if (mf instanceof String)
    { String val = (String) mf; 
      return val != null;
    }  
    return false; 
  } 

  public void setMetafeature(String f, String val) 
  { metafeatures.put(f,val); } 

  public String getMetafeatureValue(String f) 
  { Object mf = metafeatures.get(f); 
    if (mf instanceof String)
    { String val = (String) mf; 
      return val;  
    } 
    return null; 
  } 


  public static void setType(ASTTerm t, String val) 
  { String f = t.literalForm(); 
    types.put(f,val); 
  } 

  public static void setElementType(ASTTerm t, String val) 
  { String f = t.literalForm(); 
    elementTypes.put(f,val); 
  } 

  public static void setType(String f, String val) 
  { types.put(f,val); } 

  public static void setElementType(String f, String val) 
  { elementTypes.put(f,val); } 

  public static String getType(String f) 
  { String val = (String) types.get(f); 
    return val;  
  } 

  public static String getType(ASTTerm t) 
  { String val = (String) types.get(t.literalForm());
    if (val == null && t instanceof ASTBasicTerm) 
    { ASTBasicTerm bt = (ASTBasicTerm) t; 
      return bt.getType(); 
    } 
    return val;  
  }

  public abstract Type deduceType(); 

  public abstract Type deduceElementType(); 

  public static String getElementType(ASTTerm t) 
  { String val = ASTTerm.getType(t);
    if (val != null)
    { Type typ = Type.getTypeFor(val, 
                        ASTTerm.enumtypes, ASTTerm.entities); 
      if (typ != null && typ.elementType != null) 
      { return typ.elementType + ""; } 
    } 
    val = (String) elementTypes.get(t.literalForm()); 
    if (val != null) 
    { Type etyp = 
         Type.getTypeFor(val, 
                      ASTTerm.enumtypes, ASTTerm.entities); 
      if (etyp != null) 
      { return etyp + ""; } 
    } 
    return "OclAny";  
  }


  public boolean hasType(String str)
  { String alit = literalForm(); 

    /* JOptionPane.showMessageDialog(null, 
         "=== Testing " + alit + " has type " + str,   "",
                 JOptionPane.INFORMATION_MESSAGE); */  
           
    if ("character".equalsIgnoreCase(str))
    { return isCharacter(); } 
    if ("integer".equalsIgnoreCase(str) || 
        "int".equals(str))
    { return isInteger(); } 
    if ("real".equalsIgnoreCase(str) || 
        "double".equals(str))
    { return isReal(); } 
    if ("boolean".equalsIgnoreCase(str))
    { return isBoolean(); }
    if ("String".equalsIgnoreCase(str))
    { return isString(); }  

    if ("Sequence".equals(str))
    { return isSequence(); } 
    if ("StringSequence".equals(str))
    { return isStringSequence(); } 
    if ("IntegerSequence".equals(str))
    { return isIntegerSequence(); }
    if ("RealSequence".equals(str))
    { return isRealSequence(); }
    if ("BooleanSequence".equals(str))
    { return isBooleanSequence(); }
 
    if ("Set".equals(str))
    { return isSet(); } 
    if ("Map".equals(str))
    { return isMap(); } 
    if ("Function".equals(str))
    { return isFunction(); } 
    if ("Collection".equals(str))
    { return isCollection(); } 
    if ("File".equals(str))
    { return isFile(); } 
    if ("Date".equals(str))
    { return isDate(); } 
    if ("Process".equals(str))
    { return isProcess(); } 

    if ("Entity".equals(str) || 
        "Class".equals(str))
    { if (metafeatures != null) 
      { Vector vv = (Vector) metafeatures.get(alit); 
        if (vv != null && vv.contains(str))
        { return true; }
      }
    }  

    String typ = ASTTerm.getType(this);
    if (typ == null) 
    { return false; } 
 
    return typ.equals(str); 
  }  // equalsIgnoreCase?

  /* C abstraction: */ 

  public abstract boolean isLabeledStatement();  

  public abstract String getLabel();  

  public abstract Type pointersToRefType(String tname, Type t);

  public static Entity introduceCStruct(String nme, Vector ents)
  { if (nme.equals("tm"))
    { Entity ent = 
        (Entity) ModelElement.lookupByName("OclDate", ents); 
      if (ent != null) 
      { return ent; }
      ent = new Entity("OclDate"); 
      ent.addStereotype("component"); 
      ent.addStereotype("external"); 
      ents.add(ents.size()-1, ent); 
      return ent; 
    } // add the attributes? 

    Entity ent = (Entity) ModelElement.lookupByName(nme, ents); 
    if (ent != null) 
    { return ent; } 

    ent = new Entity(nme); 
    ent.addStereotype("struct"); 
    ents.add(ents.size()-1, ent); 

    if ("div_t".equals(nme))
    { Attribute quot = 
        new Attribute("quot", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(quot); 
      Attribute rem = 
        new Attribute("rem", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(rem); 
    } 
    else if ("ldiv_t".equals(nme))
    { Attribute quot = 
        new Attribute("quot", new Type("long", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(quot); 
      Attribute rem = 
        new Attribute("rem", new Type("long", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(rem); 
    } 
    /* else if ("tm".equals(nme))
    { Attribute secs = 
        new Attribute("tm_sec", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(secs); 
      Attribute mins = 
        new Attribute("tm_min", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(mins); 
      Attribute hrs = 
        new Attribute("tm_hour", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(hrs); 
      Attribute days = 
        new Attribute("tm_mday", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(days); 
      Attribute months = 
        new Attribute("tm_mon", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(months);
      Attribute years = 
        new Attribute("tm_year", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(years); 
      Attribute wday = 
        new Attribute("tm_wday", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(wday);
      Attribute yday = 
        new Attribute("tm_yday", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(yday); 
      Attribute isdst = 
        new Attribute("tm_isdst", new Type("int", null), 
                      ModelElement.INTERNAL); 
      ent.addAttribute(isdst);  
    } */ 

    return ent; 
  }

     
  public abstract Type cdeclarationToType(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract String cdeclarationStorageClass();

  public abstract ModelElement cdeclaratorToModelElement(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Vector cdeclaratorToModelElements(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Vector cparameterListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Attribute cparameterToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Statement cstatementToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Statement cupdateForm(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Statement cbasicUpdateForm(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Statement cpreSideEffect(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Statement cpostSideEffect(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Vector cstatementListToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 

  public static boolean cqueryFunction(String fname)
  { return cqueryfunctions.contains(fname); } 

  public abstract Vector cexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Expression cexpressionToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 
  

  /* JavaScript abstraction: */ 

  public abstract Vector jsclassDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents);  

  public boolean isDefinedFunction(Expression opexpr,
                      java.util.Map vartypes,
                      java.util.Map varelemtypes,
                      Vector types, Vector ents)
  { String opname = opexpr + ""; 
    Type tt = (Type) vartypes.get(opname);

    System.out.println(">>> Testing if " + opexpr + " : " + tt + " is a function"); 
 
    if (tt != null && tt.isFunction())
    { return true; } 
    else if (tt != null)
    { return false; } 

    for (int i = 0; i < ents.size(); i++) 
    { Entity ee = (Entity) ents.get(i); 
      if (ee.hasOperation(opname))
      { return true; } 
    } 

    for (int i = 0; i < ents.size(); i++) 
    { Entity ee = (Entity) ents.get(i); 
      Attribute att = (Attribute) ee.getAttribute(opname); 
      if (att != null && att.isFunction())
      { return true; } 
    } 

    return false; 
  } 


  public abstract Expression jsexpressionToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents); 

  public abstract Vector jsexpressionListToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities);

  public abstract Vector jsupdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents); 

  public abstract Vector jscompleteUpdateForm(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents); 

  public abstract Vector jspreSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents);

  public abstract Vector jspostSideEffect(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector ents);

  public abstract Vector jsvariableDeclarationToKM3(java.util.Map vartypes, 
    java.util.Map varelemtypes, Vector types, Vector entities); 

  public abstract Vector jsstatementToKM3(java.util.Map vartypes, java.util.Map varelemtypes, Vector types, Vector entities); 


  /* Java abstraction: */ 

  public abstract String queryForm(); 

  public abstract Vector getParameterExpressions(); 

  public abstract String getJavaLabel();

  public abstract boolean isJavaLabeledStatement();

  public abstract ASTTerm getJavaLabeledStatement();

  public abstract String getJSLabel();

  public abstract boolean isJSLabeledStatement();

  public abstract ASTTerm getJSLabeledStatement();

  public abstract boolean isLocalDeclarationStatement();

  public abstract String toKM3(); 

  public abstract String toKM3type(); 

  public abstract String typeArgumentsToKM3ElementType();

  public abstract String lambdaParametersToKM3();

  public Type toKM3CollectionType(Type innerType)
  { return null; } 

  public boolean isAssignment() 
  { return false; } 

  public boolean isCreation() 
  { return false; } 

  public String toKM3Assignment()
  { return toKM3(); } 

  public String toKM3creation()
  { return toKM3(); } 

  public abstract String toKM3asObject(Entity ent);

  public static boolean isInteger(String typ) 
  { return 
      "int".equals(typ) || "short".equals(typ) || 
      "byte".equals(typ) || "Integer".equals(typ) || 
      "Short".equals(typ) || "Byte".equals(typ) ||
      "BigInteger".equals(typ) || "integer".equals(typ) ||  
      "long".equals(typ) || "Long".equals(typ); 
  } // LongLong for VB

  public static boolean isReal(String typ) 
  { return 
      "float".equals(typ) || "double".equals(typ) || 
      "BigDecimal".equals(typ) || "real".equals(typ) || 
      "Float".equals(typ) || "Double".equals(typ); 
  } // Single for VB

  public static boolean isString(String typ) 
  { return 
      "String".equals(typ) || "Character".equals(typ) || 
      "StringBuffer".equals(typ) || "char".equals(typ) || 
      "StringBuilder".equals(typ); 
  } 

  public static boolean isBoolean(String typ) 
  { return 
      "boolean".equals(typ) || "Boolean".equals(typ); 
  } 

  public boolean isString() 
  { String litf = literalForm(); 
    String typ = ASTTerm.getType(litf); 
    if (typ == null) 
    { return Expression.isString(litf); } 
    return ASTTerm.isString(typ);  
  } 

  public boolean isCharacter()
  { String s = literalForm(); 
    if (s.length() > 2 && s.startsWith("'") && 
        s.endsWith("'"))
    { return true; } 
    return false; 
  } 

  public boolean isInteger() 
  { 
    String litf = literalForm(); 
    String typ = ASTTerm.getType(litf);
    if (typ == null)
    { return Expression.isInteger(litf) ||
             Expression.isLong(litf); 
    }   

    // JOptionPane.showInputDialog("isInteger for " + this + " " + typ); 

    return ASTTerm.isInteger(typ); 
  } 

  public static boolean isIntegerValued(double dd)
  { if (dd == ((int) dd)) 
    { return true; } 
    return false; 
  } 

  public boolean isReal() 
  { String litf = literalForm(); 
    String typ = ASTTerm.getType(litf); 
    if (typ == null) 
    { return Expression.isDouble(litf); } 
    return ASTTerm.isReal(typ); 
  } 

  public boolean isNumber() 
  { String litf = literalForm(); 
    String typ = ASTTerm.getType(litf);
    if (typ == null) 
    { return Expression.isInteger(litf) ||
             Expression.isLong(litf) || 
             Expression.isDouble(litf); 
    } 
 
    return ASTTerm.isReal(typ) || ASTTerm.isInteger(typ); 
  } 

  public boolean isBoolean() 
  { String litf = literalForm(); 
    String typ = ASTTerm.getType(litf); 
    if (typ == null) 
    { return "true".equalsIgnoreCase(litf) || 
             "false".equalsIgnoreCase(litf); 
    } 
    return 
      "boolean".equals(typ) || "Boolean".equals(typ); 
  } 

  public boolean isCollection()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Sequence".equals(typ) || "Set".equals(typ) ||
        "SortedSequence".equals(typ) ||
        typ.startsWith("SortedSequence(") ||
        typ.startsWith("Sequence(") || 
        typ.startsWith("Set("))
    { return true; } 
    return false; 
  } 

  public boolean isSet()
  { String litform = literalForm(); 

    if (litform != null && 
        litform.startsWith("Set{") && 
        litform.endsWith("}"))
    { return true; } 

    String typ = ASTTerm.getType(litform); 
    if (typ == null) 
    { return false; } 
    if ("Set".equals(typ) || typ.startsWith("Set("))
    { return true; } 
    return false; 
  } 

  public boolean isSequence()
  { String litform = literalForm(); 

    if (litform != null && 
        litform.startsWith("Sequence{") && 
        litform.endsWith("}"))
    { return true; } 

    String typ = ASTTerm.getType(litform); 
    if (typ == null) 
    { return false; } 
    if ("Sequence".equals(typ) || 
        typ.startsWith("Sequence(") ||
        "SortedSequence".equals(typ) || 
        typ.startsWith("SortedSequence("))
    { return true; } 
    return false; 
  } 

  public boolean isSortedSequence()
  { String litform = literalForm(); 

    String typ = ASTTerm.getType(litform); 
    if (typ == null) 
    { return false; } 
    if ("SortedSequence".equals(typ) || 
        typ.startsWith("SortedSequence("))
    { return true; } 
    return false; 
  } 

  public boolean isMap()
  { String litform = literalForm(); 

    if (litform != null && 
        litform.startsWith("Map{") && 
        litform.endsWith("}"))
    { return true; } 

    String typ = ASTTerm.getType(litform); 
    if (typ == null) 
    { return false; } 
    if ("Map".equals(typ) || typ.startsWith("Map("))
    { return true; } 
    
    
    return false; 
  } 

  public boolean isFunction()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("Function".equals(typ) || typ.startsWith("Function("))
    { return true; } 
    return false; 
  } 

  public boolean isDate()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclDate".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isOclDate()
  { return isDate(); } 

  public boolean isFile()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclFile".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isOclFile()
  { return isFile(); } 

  public boolean isProcess()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclProcess".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isStringSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(String)"))
    { return true; } 
    return false; 
  } 

  public boolean isIntegerSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(int)"))
    { return true; } 
    return false; 
  } 

  public boolean isRealSequence()
  { return isDoubleSequence(); } 

  public boolean isDoubleSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(double)"))
    { return true; } 
    return false; 
  } 

  public boolean isBooleanSequence()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if (typ.equals("Sequence(boolean)"))
    { return true; } 
    return false; 
  } 

  public boolean isBitSet()
  { return isBooleanSequence(); } 
  // and original jtype is "BitSet". 

  public boolean isOclIterator()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclIterator".equals(typ))
    { return true; } 
    return false; 
  } 

  public boolean isOclDatasource()
  { String typ = ASTTerm.getType(literalForm()); 
    if (typ == null) 
    { return false; } 
    if ("OclDatasource".equals(typ))
    { return true; } 
    return false; 
  } 


  public String getDefaultValue(String typ) 
  { if (isInteger(typ))
    { return "0"; } 
    if (isReal(typ))
    { return "0.0"; } 
    if (isString(typ))
    { return "\"\""; } 
    if (isBoolean(typ))
    { return "false"; } 
    
    return "null"; 
  } 

  public Expression getExpression()
  { return expression; } 

  public Statement getStatement()
  { return statement; } 

  public Entity getEntity()
  { if (modelElement != null && modelElement instanceof Entity)
    { return (Entity) modelElement; }
    return null; 
  }  

  public static boolean hasTag(Vector terms, String tag)
  { boolean res = false; 
    for (int i = 0; i < terms.size(); i++) 
    { ASTTerm tt = (ASTTerm) terms.get(i); 
      if (tt.hasTag(tag))
      { return true; } 
    } 
    return res; 
  } 

  public static ASTTerm getTermByTag(Vector terms, String tag)
  { ASTTerm res = null; 
    for (int i = 0; i < terms.size(); i++) 
    { ASTTerm tt = (ASTTerm) terms.get(i); 
      if (tt.hasTag(tag))
      { return tt; } 
    } 
    return res; 
  } 

  public abstract boolean hasTag(String tagx); 

  public abstract boolean hasSingleTerm(); 

  public abstract int arity(); 

  public abstract int nonSymbolArity(); 

  public static ASTTerm removeFirstTerm(ASTTerm t)
  { if (t instanceof ASTCompositeTerm)
    { ASTCompositeTerm tt = (ASTCompositeTerm) t; 
      Vector newterms = new Vector(); 
      newterms.addAll(tt.getTerms()); 
      newterms.remove(0); 
      return new ASTCompositeTerm(tt.getTag(), newterms); 
    }
    else if (t instanceof ASTBasicTerm)
    { return new ASTCompositeTerm(t.getTag()); }  
    return t; 
  } // Inconsistent with metamodel for terms. 

  public static boolean constantTrees(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    ASTTerm t0 = trees[0]; 
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; }
      if (tx.equals(t0)) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean allSymbolTerms(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx instanceof ASTSymbolTerm) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean allSymbolOrBasicTerms(ASTTerm[] trees)
  { if (trees.length == 0) 
    { return false; }
    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx instanceof ASTSymbolTerm) { } 
      else if (tx instanceof ASTBasicTerm) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean allNestedSymbolTerms(ASTTerm[] trees)
  { // All either symbols or nested symbols

    if (trees.length == 0) 
    { return false; }

    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 

      if (tx.arity() <= 1 && tx.isNestedSymbolTerm()) 
      { 
        // System.out.println(">>> Nested symbol term: " + tx); 
      } 
      else 
      { return false; }  
    } 
    return true; 
  }   

  public abstract boolean isNestedSymbolTerm(); 

  public abstract Vector allNestedSubterms(); 
  
  public static boolean recursivelyNestedEqual(
      ASTTerm[] strees, ASTTerm[] ttrees)
  { // Each strees[i] is a symbol, literally equal 
    // to the targets

    if (strees.length == 0) 
    { return false; }

    if (strees.length != ttrees.length) 
    { return false; } 

    for (int i = 0; i < strees.length; i++) 
    { ASTTerm sx = strees[i];
      ASTTerm tx = ttrees[i]; 
 
      if (sx == null || tx == null) 
      { return false; } 

      String slit = sx.literalForm(); 
      String tlit = tx.literalForm(); 
      
      if (slit.equals(tlit)) { } 
      else 
      { return false; } 
    } 

    return true; 
  }   


  public static boolean functionalSymbolMapping(ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence is functional. Not only symbols. 

    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { if (strees[i] == null) 
      { return false; } 
      sattvalues[i] = strees[i].literalForm(); 
    } 

    for (int i = 0; i < ttrees.length; i++) 
    { if (ttrees[i] == null) 
      { return false; } 
      tattvalues[i] = ttrees[i].literalForm(); 
    } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 

  public static boolean functionalASTMapping(Vector strees, Vector values)
  { // The correspondence of terms strees to strings values
    // is functional. Not only symbol terms.

    // Needs strees.size() <= values.size() 

    String[] sattvalues = new String[strees.size()]; 
    String[] tattvalues = new String[values.size()]; 

    for (int i = 0; i < strees.size(); i++) 
    { ASTTerm sterm = (ASTTerm) strees.get(i); 
      if (sterm == null) 
      { return false; } 
      sattvalues[i] = sterm.literalForm(); 
    } 

    for (int i = 0; i < values.size(); i++) 
    { String ts = (String) values.get(i); 
      if (ts == null) 
      { return false; } 
      tattvalues[i] = ts; 
    } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 

  public static boolean allSingletonTrees(Vector strees)
  { for (int i = 0; i < strees.size(); i++) 
    { ASTTerm sterm = (ASTTerm) strees.get(i); 
      if (sterm == null || sterm.arity() != 1) 
      { return false; } 
    } 

    return true; 
  } 

  public static boolean allSingletonTrees(ASTTerm[] strees)
  { for (int i = 0; i < strees.length; i++) 
    { ASTTerm sterm = strees[i]; 
      if (sterm == null || sterm.arity() != 1) 
      { return false; } 
    } 

    return true; 
  } 

  public static boolean functionalNestedSymbolMapping(ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence of strees single element & 
    // target terms is functional.

    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { if (strees[i] == null) 
      { return false; } 
      sattvalues[i] = strees[i].literalForm(); 
    } 

    for (int i = 0; i < ttrees.length; i++) 
    { if (ttrees[i] == null) 
      { return false; } 
      tattvalues[i] = ttrees[i].literalForm(); 
    } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 

  public static TypeMatching createNewFunctionalMapping(String name, ASTTerm[] strees, ASTTerm[] ttrees)
  { // The correspondence is functional.

    // If an identity mapping, return _1 |-->_1

    // A "default" case _* |--> v is included where v is the 
    // most frequent target of different source values. 

    // Should also recognise common string mappings such as
    // prefixing. 

    Type str = new Type("String", null); 
    TypeMatching tm = new TypeMatching(str,str);
    tm.setName(name);  

    String[] sattvalues = new String[strees.length]; 
    String[] tattvalues = new String[ttrees.length]; 

    for (int i = 0; i < strees.length; i++) 
    { sattvalues[i] = strees[i].literalForm(); } 

    for (int i = 0; i < ttrees.length; i++) 
    { tattvalues[i] = ttrees[i].literalForm(); } 

    if (AuxMath.isIdentityMapping(sattvalues,tattvalues))
    { tm.addDefaultMapping("_1", "_1"); 
      return tm; 
    }

    if (AuxMath.isPrefixed(sattvalues,tattvalues))
    { String pref = AuxMath.commonPrefix(
                                sattvalues, tattvalues); 
      if (pref != null) 
      { tm.addDefaultMapping("_1", pref + "_1"); 
        return tm;
      }  
    }

    if (AuxMath.isSuffixed(sattvalues,tattvalues))
    { String suff = AuxMath.commonSuffix(
                                sattvalues, tattvalues); 
      if (suff != null) 
      { tm.addDefaultMapping("_1", "_1" + suff); 
        return tm;
      }  
    }

    if (AuxMath.isPrefixedSuffixed(sattvalues,tattvalues))
    { String pref = AuxMath.commonPrefixSuffix1(
                                sattvalues, tattvalues); 
      String suff = AuxMath.commonPrefixSuffix2(
                                sattvalues, tattvalues); 
      if (pref != null && suff != null) 
      { tm.addDefaultMapping("_1", pref + "_1" + suff); 
        return tm;
      }  
    }

    // tail and front of strings also? 

    /* Identify default mapping _1 |--> v */ 

    String defaultValue = null; 
    int defaultCount = 0; 
    for (int i = 0; i < tattvalues.length; i++) 
    { String tval = tattvalues[i]; 
      java.util.HashSet svals = new java.util.HashSet(); 
      for (int j = 0; j < sattvalues.length && 
                      j < tattvalues.length; j++) 
      { if (tattvalues[j].equals(tval))
        { svals.add(sattvalues[j]); } 
      } 

      if (svals.size() > defaultCount)
      { defaultValue = tval;
        defaultCount = svals.size();
      }     
    } 

    System.out.println(">> Default mapping is _* |-->" + defaultValue); 


    tm.setStringValues(sattvalues,tattvalues);

    if (defaultValue != null) 
    { tm.addDefaultMapping("_*", defaultValue); } 
 
    return tm; 
  } 

  public static boolean hasNullTerm(Vector strees)
  { for (int i = 0; i < strees.size(); i++) 
    { ASTTerm st = (ASTTerm) strees.get(i); 
      if (st == null) 
      { System.err.println("!WARNING: null element in " + strees + " -- INVALID AST DATA"); 
        return true; 
      } 
    } 
    return false; 
  } 
    
  public static boolean functionalTermMapping(Vector strees, Vector ttrees)
  { // The correspondence is functional.
    String[] sattvalues = new String[strees.size()]; 
    String[] tattvalues = new String[ttrees.size()]; 

    for (int i = 0; i < strees.size(); i++) 
    { ASTTerm st = (ASTTerm) strees.get(i); 
      if (st == null) 
      { System.err.println("!WARNING: null element in " + strees + " -- INVALID AST DATA"); 
        return false; 
      } 
      sattvalues[i] = st.literalForm(); 
    } 

    for (int i = 0; i < ttrees.size(); i++) 
    { ASTTerm tt = (ASTTerm) ttrees.get(i); 
      if (tt == null) 
      { System.err.println("!WARNING: null element in " + ttrees + " -- INVALID AST DATA"); 
        return false; 
      }
      tattvalues[i] = tt.literalForm(); 
    } 
 
    return AuxMath.isFunctional(sattvalues,tattvalues); 
  } 


  public static String commonTag(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return null; }
    ASTTerm t0 = trees[0]; 
    if (t0 == null) { return null; }
	
    if (t0 instanceof ASTBasicTerm) 
    { return ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { return ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return null; } 
  }

  public static boolean sameTag(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0]; 
    if (t0 == null) { return false; }
	
    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx.hasTag(tag0)) { } // not a symbol
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTails(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    Vector tail0 = new Vector(); 
    ASTTerm t0 = trees[0]; 

    if (t0 instanceof ASTCompositeTerm) 
    { Vector trms = new Vector(); 
      trms.addAll(((ASTCompositeTerm) t0).getTerms());
      if (trms.size() == 0) 
      { return false; } 
      trms.remove(0); 
      tail0.addAll(trms);  
    } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx instanceof ASTCompositeTerm) 
      { Vector trms = new Vector(); 
        trms.addAll(((ASTCompositeTerm) tx).getTerms());
        if (trms.size() == 0) 
        { return false; } 
        trms.remove(0); 
        if (tail0.equals(trms)) // equal element by element 
        { } 
        else 
        { return false; } 
      } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTagSingleArgument(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0]; 
    if (t0 == null) 
    { return false;} 

    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm && 
             t0.hasSingleTerm()) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 
      else if (tx.hasTag(tag0) && tx.hasSingleTerm()) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean sameTagSameArity(ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    String tag0 = ""; 
    ASTTerm t0 = trees[0];
    if (t0 == null) 
    { return false;} 
 
    int arit = t0.arity(); 

    if (t0 instanceof ASTBasicTerm) 
    { tag0 = ((ASTBasicTerm) t0).getTag(); } 
    else if (t0 instanceof ASTCompositeTerm) 
    { tag0 = ((ASTCompositeTerm) t0).getTag(); } 
    else 
    { return false; } 

    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null) 
      { return false; } 
      else if (tx.hasTag(tag0) && tx.arity() == arit) { } 
      else 
      { return false; } 
    } 
    return true; 
  }   

  public static boolean alwaysSymbol(int ind, ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null || ind >= tx.arity()) 
      { return false; } 
      ASTTerm tsx = tx.getTerm(ind); 
      if (tsx instanceof ASTSymbolTerm) { } 
      else 
      { return false; } 
    } 
      
    return true; 
  } 

  public static boolean alwaysBasic(int ind, ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null || ind >= tx.arity()) 
      { return false; } 
      ASTTerm tsx = tx.getTerm(ind); 
      if (tsx instanceof ASTBasicTerm) { } 
      else 
      { return false; } 
    } 
      
    return true; 
  } 

  public static boolean alwaysNestedSymbolTerm(int ind, 
                                          ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }
    
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null || ind >= tx.arity()) 
      { return false; } 
      ASTTerm tsx = tx.getTerm(ind); 
      if (tsx == null) 
      { return false; }
      if (tsx.isNestedSymbolTerm()) { } 
      else 
      { return false; }  
    } 
      
    return true; 
  } 

  public static boolean allCompositeSameLength(
                                   ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }

    if (trees[0] == null) 
    { return false; } 

    int len0 = trees[0].arity(); 

    if (len0 <= 1) 
    { return false; } 
    
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null || tx.arity() != len0) 
      { return false; } 
    } 
      
    return true; 
  } 

  public static boolean allNonSymbolSameLength(
                                   ASTTerm[] trees)
  { if (trees == null || trees.length == 0) 
    { return false; }

    if (trees[0] == null) 
    { return false; } 

    int len0 = trees[0].arity(); 

    if (len0 < 1) 
    { return false; } 
    
    for (int i = 1; i < trees.length; i++) 
    { ASTTerm tx = trees[i]; 
      if (tx == null || tx.arity() != len0) 
      { return false; } 
    } 
      
    return true; 
  } 


  /* Used by ModelSpecification::conditionalTreeMappings */ 

  public static Vector symbolValues(int ind, ASTTerm[] trees)
  { /* Symbols at ind position in each trees[i] */ 
    
    Vector res = new Vector(); 

    if (trees == null || trees.length == 0) 
    { return res; }
    
    for (int i = 0; i < trees.length; i++) // not = 0 
    { ASTTerm tx = trees[i]; 
      if (tx == null || ind >= tx.arity()) 
      { continue; } 
      ASTTerm tsx = tx.getTerm(ind); 
      if (tsx instanceof ASTSymbolTerm) 
      { res.add(tsx.literalForm()); } 
      else if (tsx instanceof ASTBasicTerm) 
      { res.add(tsx.literalForm()); } 
    } 
      
    return res; 
  } 

  public static boolean hasTermValue(ASTTerm trm, int ind, String val) 
  { if (trm == null) 
    { return false; } 

    if (ind >= trm.arity())
    { return false; } 

    ASTTerm tsx = trm.getTerm(ind); 
    if (val.equals(tsx.literalForm()))
    { return true; } 
    return false; 
  } 

  public static boolean hasTagValue(ASTTerm trm, int ind, String val) 
  { if (trm == null) 
    { return false; } 

    if (ind >= trm.arity())
    { return false; } 

    ASTTerm tsx = trm.getTerm(ind); 
    if (val.equals(tsx.getTag()))
    { return true; } 
    return false; 
  } 

  public static boolean hasNestedTags(ASTTerm trm, int ind, 
                                      Vector tags) 
  { if (trm == null) 
    { return false; } 

    if (ind >= trm.arity())
    { return false; } 

    ASTTerm tsx = trm.getTerm(ind); 
    if (tsx != null && tsx.hasNestedTags(tags))
    { return true; } 
    return false; 
  } 
 
  public static boolean hasExactNestedTags(
                                      ASTTerm trm, int ind, 
                                      Vector tags) 
  { if (trm == null) 
    { return false; } 

    if (ind >= trm.arity())
    { return false; } 

    ASTTerm tsx = trm.getTerm(ind); 
    if (tsx != null && tsx.hasExactNestedTags(tags))
    { return true; } 
    return false; 
  } 
 

  public static ASTTerm[] removeOuterTag(ASTTerm[] trees, Vector remd)
  { if (trees == null || trees.length == 0) 
    { return trees; }

    ASTTerm[] result = new ASTTerm[trees.length]; 

    for (int i = 0; i < trees.length; i++) 
    { ASTTerm tx = trees[i];
      if (tx == null) 
      { result[i] = null; 
        remd.add(null); 
      } 
      else  
      { ASTTerm tnew = tx.removeOuterTag(); 
        result[i] = tnew; 
        remd.add(tnew); 
      } 
    } 
    return result; 
  } // For composite terms with single subterms, return those
    // subterms.   

  public static ASTTerm[] subterms(ASTTerm[] trees, int i, Vector remd)
  { // i'th subterms of trees

    if (trees == null || trees.length == 0) 
    { return trees; }

    ASTTerm[] result = new ASTTerm[trees.length]; 

    for (int j = 0; j < trees.length; j++) 
    { ASTTerm tx = trees[j]; 
      if (tx == null)
      { result[j] = null; 
        remd.add(null);
        // return null?  
      }
      else 
      { ASTTerm tnew = tx.getTerm(i); // null if no i'th term 
        result[j] = tnew; 
        remd.add(tnew);
      }  
    } // terms are indexed from 0. 

    return result; 
  }   

 public static boolean equalTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i]? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          // System.out.println(">>>> Comparing " + xx + " to " + yvect); 
          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static Vector createIdentityMappings
        (ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i] = xs[i] 
      // Create a separate mapping for each different arity
      // of terms. Look for constant symbol values. 

      Vector res = new Vector(); 
      java.util.Map aritymap = new java.util.HashMap(); 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx != null) 
          { int n = xx.arity(); 
            Vector aritynterms = 
               (Vector) aritymap.get(n); 
            if (aritynterms == null) 
            { aritynterms = new Vector(); 
              aritynterms.add(xx);
              aritymap.put(n,aritynterms);  
              BasicExpression expr = 
                BasicExpression.newASTBasicExpression(xx); 
              AttributeMatching am = 
                new AttributeMatching(expr,expr); 
              res.add(am); 
            }
            else 
            { aritynterms.add(xx); }  
          } 
        }
      } 
      return res; 
    }

    public static Vector createGeneralisedMappings
        (ASTTerm[] xs, Expression trg)
    { // Each xs[i] maps to same target. 
      // Create a separate mapping for each different arity
      // of xs. Extract constant symbol values. 

      Vector res = new Vector(); 
      java.util.Map aritymap = new java.util.HashMap(); 
      Vector doms = new Vector(); 

      if (xs.length > 1)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
         
          if (xx != null) 
          { int n = xx.arity(); 
            Integer nx = new Integer(n); 
            doms.add(nx); 
            Vector aritynterms = 
               (Vector) aritymap.get(nx); 
            if (aritynterms == null) 
            { aritynterms = new Vector(); 
              aritynterms.add(xx);
              aritymap.put(nx,aritynterms);  
            }
            else 
            { aritynterms.add(xx); }  
          } 
        }
      } 
      else 
      { return res; } 

      for (int j = 0; j < doms.size(); j++) 
      { // For each arity set aritymap(doms(j))
        // identify the generalised form of the terms

        Integer nx = (Integer) doms.get(j); 
        Vector arityns = (Vector) aritymap.get(nx); 

        // System.out.println(">>> Arity " + nx + " source terms are: " + arityns); 
        // System.out.println(); 

        if (arityns.size() == 1) 
        { ASTTerm st = (ASTTerm) arityns.get(0); 
          BasicExpression expr = 
                BasicExpression.newASTBasicExpression(st); 
          AttributeMatching am = 
                new AttributeMatching(expr,trg); 
          res.add(am); 
        } 
        else 
        { ASTTerm st0 = (ASTTerm) arityns.get(0); 
          BasicExpression expr = 
                BasicExpression.newASTBasicExpression(st0); 
          // schematic expression based on st0.

          int n = nx.intValue(); 
          for (int p = 0; p < n; p++) 
          { if (ASTTerm.constantTerms(arityns,p))
            { ASTTerm pterm = st0.getTerm(p); 
              String st0p = pterm.literalForm(); 
              Expression exprp = 
                      new BasicExpression(st0p);                    
              expr.setParameter(p+1, exprp); 
            }
            else 
            { expr.setParameter(p+1,
                    new BasicExpression("_" + (p+1))); 
            } 
          } 
          AttributeMatching am = 
                new AttributeMatching(expr,trg); 
          res.add(am); 
        }
      }  

      System.out.println(">>> Generalised matchings: " + res); 

      return res; 
    }

    public static boolean constantTerms(Vector trms, int p)
    { // The p subterms of all trms are the same 

      if (trms.size() == 0) 
      { return false; } 
   
      if (trms.size() == 1) 
      { return true; } 

      ASTTerm t0 = (ASTTerm) trms.get(0); 
      if (t0.arity() <= p) 
      { return false; } 

      ASTTerm subtermp = t0.getTerm(p);
 
      String lit = subtermp.literalForm(); 
      
      for (int i = 1; i < trms.size(); i++) 
      { ASTTerm t = (ASTTerm) trms.get(i); 
        ASTTerm subterm = t.getTerm(p);

        // System.out.println(" " + p + " subterm of " + t + " is " + subterm + " =? " + lit); 
 
        if (subterm != null && 
            subterm.literalForm().equals(lit))
        { } 
        else 
        { return false; } 
      } 

      // System.out.println(" " + p + " subterm is constant"); 

      return true; 
    } 

    public static boolean constantTerms(ASTTerm[] trms, int p)
    { // The p subterms of all trms are the same 

      if (trms.length == 0) 
      { return false; } 
   
      if (trms.length == 1) 
      { return true; } 

      ASTTerm t0 = trms[0]; 
      if (t0.arity() <= p) 
      { return false; } 

      ASTTerm subtermp = t0.getTerm(p);
 
      String lit = subtermp.literalForm(); 
      
      for (int i = 1; i < trms.length; i++) 
      { ASTTerm t = trms[i]; 
        ASTTerm subterm = t.getTerm(p);
 
        if (subterm != null && 
            subterm.literalForm().equals(lit))
        { } 
        else 
        { return false; } 
      } 

      return true; 
    } 

 /*   public static boolean matchingTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i], or corresponding under mod? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

         // System.out.println(">>>> Comparing " + xx + " to " + yvect); 
          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else if (mod.correspondingTrees(xx,yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    } */ 

    public static boolean matchingTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = xs[i], or corresponding under mod? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          // System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (xx == null) 
          { return false; } 
          else if (xx.equals(yvect)) { } 
          else if (mod.correspondingTrees(sent, xx, yvect)) { } 
          else  
          { return false; }
        }
        return true; 
      } 
      return false; 
    }

    public static boolean matchingTrees(Entity sent, ASTTerm xx, ASTTerm yy, ModelSpecification mod)
    { // System.out.println(">>>> Comparing " + xx + " to " + yy); 
      if (xx == null) 
      { return false; } 
      else if (xx.equals(yy)) 
      { return true; } 
      else if (mod.correspondingTrees(sent, xx, yy)) 
      { return true; } 
      return false; 
    }

 /* 
    
  public static boolean singletonTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]') (for the same tag)? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          // System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTSymbolTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTSymbolTerm) xx).getSymbol();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(xx,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

  public static boolean singletonTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]') for the same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          // System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTSymbolTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTSymbolTerm) xx).getSymbol();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } // or vice-versa
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(sent, xx, ct0)) 
            { System.out.println(">>> corresponding trees: " + 
                                 xx + " " + ct0); 
            } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }


    public static boolean sameArityTrees(ASTTerm[] xs, ASTTerm[] ys)
    { // Is each ys[i].terms.size() == xs[i].terms.size()? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          int n = xx.arity(); 
          int m = yvect.arity(); 
          if (n != m) 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

    public static boolean sameNonSymbolArity(ASTTerm[] xs, ASTTerm[] ys)
    { // Is number of non-symbols in ys[i].terms == 
      // number of non-symbols in xs[i].terms? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          int n = xx.nonSymbolArity();
          // System.out.println(">***> Non-symbol arity of " + xx + " = " + n); 
 
          int m = yvect.nonSymbolArity(); 
          // System.out.println(">***> Non-symbol arity of " + yvect + " = " + m); 

          if (n != m) 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

    public static boolean lowerNonSymbolArity(ASTTerm[] xs, ASTTerm[] ys)
    { // Is number of non-symbols in ys[i].terms <= 
      // number of non-symbols in xs[i].terms? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          if (xx == null || yvect == null)
          { return false; } 

          int n = xx.nonSymbolArity();
          // System.out.println(">*>*> Non-symbol arity of " + xx + " = " + n); 
 
          int m = yvect.nonSymbolArity(); 
          // System.out.println(">*>*> Non-symbol arity of " + yvect + " = " + m); 

          if (n < m) 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

    public static Vector concatenateCorrespondingTermLists(
                              ASTTerm xx, 
                              ModelSpecification mod)
    { Vector xxterms = xx.getTerms();
      Vector concatxxterms = new Vector(); 
 
      for (int j = 0; j < xxterms.size(); j++) 
      { ASTTerm xj = (ASTTerm) xxterms.get(j); 
        ASTTerm tj = mod.getCorrespondingTree(xj);
 
        System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 
 
        if (tj != null) 
        { 
          Vector tjterms = tj.getTerms();
          concatxxterms.addAll(tjterms);
        } 
      }  
      return concatxxterms; 
    }        
    
    /* Used with treeSequenceMapping4 in ModelSpecification */ 

    public static boolean treeconcatenations(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i].terms == concatenation of terms of the ti 
      // corresponding to the xs[i].terms

      // System.out.println(">>> Checking concatenation of terms"); 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return false; } 

          Vector xxterms = xx.getTerms();
          Vector concatxxterms = new Vector(); 
 
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            // System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 
 
            if (tj != null) 
            { 
              Vector tjterms = tj.getTerms();
              concatxxterms.addAll(tjterms);
            }
            else 
            { Vector subterms = 
                concatenateCorrespondingTermLists(xj,mod); 
              concatxxterms.addAll(subterms);
            } 
          }  
            
          // System.out.println(">>> Concatenantion of terms = " + concatxxterms); 
          // System.out.println(">>> Target terms = " + yy.getTerms()); 

          if (concatxxterms.equals(yy.getTerms())) { } 
          else 
          { return false; } 
        } 
        return true; 
      } 
      return false; 
    }

    /* The distinct symbol terms of xx */ 
    public static Vector concatenationSubMapping(
                              ASTTerm xx, 
                              ModelSpecification mod)
    { Vector xxterms = xx.getTerms();
      Vector symbs = new Vector(); 
 
      for (int j = 0; j < xxterms.size(); j++) 
      { ASTTerm xj = (ASTTerm) xxterms.get(j); 
        // ASTTerm tj = mod.getCorrespondingTree(xj);
        // System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 

        if (xj instanceof ASTSymbolTerm)
        { String symb = xj.literalForm(); 
          if (symbs.contains(symb)) { } 
          else 
          { symbs.add(symb); } 
        } 
 
        /* if (tj != null) 
        { 
          Vector tjterms = tj.getTerms();
          concatxxterms.addAll(tjterms);
        } */ 
      }  
      return symbs; 
    }        

    /* Called by ModelSpecification tree-2-tree mappings */ 

    public static AttributeMatching 
      concatenationTreeMapping(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod,
                        Vector tms)
    { // Each ys[i].terms == concatenation of terms of the ti 
      // corresponding to the xs[i].terms

      Vector pars = new Vector(); 
      Vector tpars = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { // for (int i = 0; i < xs.length; i++)
        // { 
          ASTTerm xx0 = xs[0]; 
          ASTTerm yy0 = ys[0]; 

          if (xx0 == null || yy0 == null)
          { return null; } 

          int n = xs[0].arity();
             // Maximum arity of any xs[i]

          for (int i = 0; i < xs.length; i++)
          { if (n < xs[i].arity())
            { n = xs[i].arity(); } 
          } 

          Vector[] xtermvectors = new Vector[n]; 
          String targetTag = ys[0].getTag(); 

          for (int i = 0; i < n; i++)
          { xtermvectors[i] = new Vector(); } 

          for (int i = 0; i < xs.length; i++)
          { ASTTerm xx = xs[i]; 
            ASTTerm yy = ys[i]; 

            if (xx == null || yy == null)
            { return null; } 

            Vector xxterms = xx.getTerms();
            Vector yyterms = yy.getTerms(); 
            // Vector tterms = new Vector(); 
 
            for (int j = 0; j < xxterms.size(); j++) 
            { ASTTerm xj = (ASTTerm) xxterms.get(j); 
              ASTTerm tj = mod.getCorrespondingTree(xj);
 
              // System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 
 
              if (xj instanceof ASTSymbolTerm) 
              { xtermvectors[j].add(xj); } 
              else if (tj != null) 
              { // if (tj.getTag().equals(targetTag))
                // { tterms.addAll(tj.getTerms()); }
                // else 
                // { return null; } 
              }  
              else // Not included in concatenation
              { xtermvectors[j].add(xj); } 
            }  
          }             

          Vector xxterms = xx0.getTerms();
            // xx0 is a typical example. 
          
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
  
            if (xj instanceof ASTSymbolTerm)
            { if (AuxMath.isConstantSeq(xtermvectors[j]))
              { pars.add(
                  new BasicExpression(xj.literalForm())); 
              } // j-th terms are all same symbol
              else 
              { pars.add(
                  new BasicExpression("_" + (j+1))); 
              } 
            } 
            else if (tj != null) 
            { pars.add(
                new BasicExpression("_" + (j+1))); 
              tpars.add(
                new BasicExpression("_" + (j+1))); 

              // Vector tjterms = tj.getTerms();
              // concatxxterms.addAll(tjterms);
            }
            else // nothing corresponds to xj
            { Vector deletedSymbols = 
                concatenationSubMapping(xj,mod); 
              String fid = 
                Identifier.nextIdentifier("concatruleset");
              TypeMatching tmnew = new TypeMatching(fid);

              for (int d = 0; d < deletedSymbols.size(); d++) 
              { String ds = (String) deletedSymbols.get(d); 
                tmnew.addValueMapping(ds, " "); 
              } 

              tmnew.addValueMapping("_0", "_0"); 
              tms.add(tmnew); 
              // a mapping that removes all symbols in xj

              String subid = 
                Identifier.nextIdentifier("concatrule");
              TypeMatching tmsub = new TypeMatching(subid);
              tmsub.addValueMapping("_*", "_*`" + fid); 
              tms.add(tmsub); 

              BasicExpression subexpr = 
                new BasicExpression(subid); 
              subexpr.setUmlKind(Expression.FUNCTION);
              subexpr.addParameter(new BasicExpression("_" + (j+1)));

              pars.add(
                new BasicExpression("_" + (j+1))); 
              tpars.add(subexpr);  
            } // nested mapping. 
          }  
        // }     
          
        BasicExpression srcterm = 
          new BasicExpression(xs[0]); 
        srcterm.setParameters(pars); 

        BasicExpression trgterm = 
          new BasicExpression(ys[0]); 
        trgterm.setParameters(tpars); 

        return new AttributeMatching(srcterm,trgterm); 
      } 
      return null; 
    }

    /* Called from ModelSpecification */ 
    public static boolean treesuffixes(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i].terms == concatenation of xs[i].terms
      // plus a constant suffix of terms. 

      // System.out.println(">&>&> Checking suffix relation of trees"); 

      java.util.HashSet suffixes = new java.util.HashSet(); 

      if (ys.length > 1 && xs.length == ys.length)
      { String targetTag = ys[0].getTag(); 

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return false; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms(); 
          Vector tterms = new Vector(); 
 
          // concatenate all terms of xj' for xj : xxterms

          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            // System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 
 
            if (tj != null) 
            { if (tj.getTag().equals(targetTag))
              { tterms.addAll(tj.getTerms()); }
              else 
              { return false; } 
            }  
          }  
            
          // System.out.println(">>> Mapped terms = " + tterms); 
          // System.out.println(">>> Target terms = " + yyterms); 

          if (tterms.size() == 0) 
          { return false; } 
          else if (AuxMath.isSequencePrefix(tterms,yyterms)) { } 
          else 
          { return false; }

          Vector suff = 
            AuxMath.sequenceSuffix(tterms,yyterms);
          suffixes.add(suff + "");  
        } 

        // System.out.println("--> Suffixes are: " + suffixes); 

        if (suffixes.size() == 1)
        { return true; } // constant suffix

        return false; 
      } 

      return false; 
    }

    public static boolean treesuffixFunction(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i].terms == concatenation of xs[i].terms
      // plus a suffix that is a function of some source term. 

      // Assume all source terms have same tag & arity
      // All target terms have same tag

      // System.out.println(">&>&> Checking function suffix relation of trees"); 

      
      Vector suffixes = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { int n = xs[0].arity(); 
        Vector[] xtermvectors = new Vector[n]; 
        String targetTag = ys[0].getTag(); 

        for (int i = 0; i < n; i++)
        { xtermvectors[i] = new Vector(); } 

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return false; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms(); 
          Vector tterms = new Vector(); 
 
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            // System.out.println(">>^^>> Corresponding tree of " + xj + " is " + tj); 
 
            if (xj instanceof ASTSymbolTerm) 
            { xtermvectors[j].add(xj); } 
            else if (tj != null) 
            { if (tj.getTag().equals(targetTag))
              { tterms.addAll(tj.getTerms()); }
              else 
              { return false; } 
            }  
            else // Not included in concatenation
            { xtermvectors[j].add(xj); } 
          }  
            
          // System.out.println(">>> Mapped terms = " + tterms); 
          // System.out.println(">>> Target terms = " + yyterms); 

          if (tterms.size() == 0) 
          { return false; } 
          else if (AuxMath.isSequencePrefix(tterms,yyterms)) { } 
          else 
          { return false; }

          Vector suff = 
            AuxMath.sequenceSuffix(tterms,yyterms);
          suffixes.add(suff + "");  
        } 

        // System.out.println("--> Suffixes are: " + suffixes); 

        if (suffixes.size() == 1)
        { return true; } 

        // Look for function from some non-empty
        // xtermvectors[j] to suffixes. 

        for (int j = 0; j < xtermvectors.length; j++) 
        { Vector sjterms = xtermvectors[j]; 
          if (sjterms.size() == suffixes.size()) // or <=
          { if (ASTTerm.functionalASTMapping(sjterms,suffixes))
            { // System.out.println("--> Functional mapping from " + sjterms + " to " + suffixes); 
              return true; 
            } // But it may not be a valid function. 
          } 
        }  

        return false; 
      } 

      return false; 
    }

    public static boolean treeprefixFunction(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i].terms == concatenation prefix + 
      // xs[i].terms
      
      // Assume all source terms have same tag & arity
      // All target terms have same tag

      // System.out.println(">&>&> Checking function prefix relation of trees"); 

      
      Vector prefixes = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { int n = xs[0].arity(); 
        Vector[] xtermvectors = new Vector[n]; 
        String targetTag = ys[0].getTag(); 

        for (int i = 0; i < n; i++)
        { xtermvectors[i] = new Vector(); } 

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return false; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms(); 
          Vector tterms = new Vector(); 
 
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            // System.out.println(">><<>> Corresponding tree of " + xj + " is " + tj); 
 
            if (xj instanceof ASTSymbolTerm) 
            { xtermvectors[j].add(xj); } 
            else if (tj != null) 
            { if (tj.getTag().equals(targetTag))
              { tterms.addAll(tj.getTerms()); }
              else 
              { return false; } 
            }  
            else 
            { xtermvectors[j].add(xj); } 
          }  
            
          // System.out.println(">>> Mapped terms = " + tterms); 
          // System.out.println(">>> Target terms = " + yyterms); 

          if (tterms.size() == 0) 
          { return false; } 
          else if (AuxMath.isSequenceSuffix(tterms,yyterms)) { } 
          else 
          { return false; }

          Vector pref = 
            AuxMath.sequencePrefix(tterms,yyterms);
          prefixes.add(pref + "");  
        } 

        // System.out.println("--> Prefixes are: " + prefixes); 

        if (prefixes.size() == 1)
        { return true; } 

        // Look for function from some non-empty
        // xtermvectors[j] to suffixes. 

        for (int j = 0; j < xtermvectors.length; j++) 
        { Vector sjterms = xtermvectors[j]; 
          // System.out.println("<<> Checking Functional mapping from " + sjterms + " to " + prefixes); 
              
          if (sjterms.size() == prefixes.size()) // or <=
          { if (ASTTerm.functionalASTMapping(sjterms,prefixes))
            { // System.out.println("--> Functional mapping from " + sjterms + " to " + prefixes); 
              return true; 
            } 
          } // May not be valid
        }  

        return false; 
      } 

      return false; 
    }

    public static AttributeMatching 
      suffixTreeFunctionMapping(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod,
                        Entity sent, Attribute satt, 
                        Attribute tatt, Vector sourceatts,
                        Vector tms, Vector ams)
    { // Each ys[i].terms == concatenation of xs[i].terms 
      // plus constant suffix of terms. 

      Vector pars = new Vector(); 
      Vector tpars = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { ASTTerm xx0 = xs[0]; 
        ASTTerm yy0 = ys[0]; 

        String ttag = yy0.getTag(); // All the same. 

        if (xx0 == null || yy0 == null)
        { return null; } 

        int n = xs[0].arity(); // All the same.  
        Vector[] xtermvectors = new Vector[n]; 
        ASTTerm[] targetValues = new ASTTerm[xs.length]; 

        for (int i = 0; i < n; i++)
        { xtermvectors[i] = new Vector(); } 

        Vector suffixes = new Vector();
        Vector suffixtermseqs = new Vector(); 
          // sequences of suffix terms.  

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return null; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms(); 
          Vector tterms = new Vector(); 
 
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            if (xj instanceof ASTSymbolTerm) 
            { xtermvectors[j].add(xj); } 
            else if (tj != null) 
            { tterms.addAll(tj.getTerms()); } 
            else 
            { xtermvectors[j].add(xj); } 
          }  
            
          //  System.out.println(">>> Mapped terms = " + tterms); 
          //  System.out.println(">>> Target terms = " + yyterms); 

          Vector suff = 
              AuxMath.sequenceSuffix(tterms,yyterms);
          suffixes.add(suff + "");  
          ASTTerm targterm = new ASTCompositeTerm(ttag,suff); 
          suffixtermseqs.add(targterm);
          targetValues[i] = targterm;  
        } // suffix terms corresponding to the xs[i]
 
        Vector xx0terms = xx0.getTerms();
        Vector yy0terms = yy0.getTerms();
          
        for (int j = 0; j < xx0terms.size(); j++) 
        { ASTTerm xj = (ASTTerm) xx0terms.get(j); 
          ASTTerm tj = mod.getCorrespondingTree(xj);
  
          if (xj instanceof ASTSymbolTerm)
          { if (AuxMath.isConstantSeq(xtermvectors[j]))
            { pars.add(
                new BasicExpression(xj.literalForm()));
            } 
            else
            { pars.add(
                new BasicExpression("_" + (j+1))); 
            }  
          } 
          else if (tj != null) 
          { pars.add(
                new BasicExpression("_" + (j+1))); 
            tpars.add(
                new BasicExpression("_" + (j+1))); 
          }
          else 
          { pars.add(
                new BasicExpression("_" + (j+1)));
          } 
            // else - a source term that the suffix could 
            // functionally depend upon.  
        }  

        boolean found = false; 

        for (int j = 0; j < xtermvectors.length && !found; j++) 
        { Vector sjterms = xtermvectors[j]; 
          if (sjterms.size() == suffixes.size()) // or <=
          { if (ASTTerm.functionalASTMapping(sjterms,suffixes))
            { // System.out.println("--> Functional mapping from " + sjterms + " to " + suffixes); 

              // Mapping from symbols at one argument place to
              // the suffixes. 

              java.util.Map sattvalueMap = 
                 new java.util.HashMap(); 

              ASTTerm[] sjtermvect = 
                 new ASTTerm[sjterms.size()];
              for (int p = 0; p < sjterms.size(); p++)
              { sjtermvect[p] = (ASTTerm) sjterms.get(p); }
 
              sattvalueMap.put(satt, sjtermvect); 
              AttributeMatching amsuffix = 
                mod.composedTreeFunction(sent,tatt,
                      sourceatts,
                      sattvalueMap,targetValues,
                      suffixtermseqs, tms, ams); 

              if (amsuffix != null && 
                  amsuffix.trgvalue != null) 
              { BasicExpression nameexpr = 
                  new BasicExpression("name"); 
                nameexpr.setUmlKind(Expression.FUNCTION);
                nameexpr.addParameter(
                  new BasicExpression("_" + (j+1)));  
                Expression newjpar = 
                  amsuffix.trgvalue.substituteEq(
                                         "_1",nameexpr);  
                tpars.add(newjpar); 
                found = true;
              }  
              else 
              { // System.out.println("!! No mapping from " + sjterms + " to " + suffixes); 
              } 
            } 
          } 
        }  

        if (!found) 
        { return null; } 
                  
        BasicExpression srcterm = 
          new BasicExpression(xs[0]); 
        srcterm.setParameters(pars); 

        BasicExpression trgterm = 
          new BasicExpression(ys[0]); 
        trgterm.setParameters(tpars); 

        return new AttributeMatching(srcterm,trgterm); 
      }
 
      return null; 
    }

    public static AttributeMatching 
      prefixTreeFunctionMapping(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod,
                        Entity sent, Attribute satt, 
                        Attribute tatt, Vector sourceatts,
                        Vector tms, Vector ams)
    { // Each ys[i].terms == concatenation of prefix     
      // plus xs[i].terms 
      
      Vector pars = new Vector(); 
      Vector tpars = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { ASTTerm xx0 = xs[0]; 
        ASTTerm yy0 = ys[0]; 

        String ttag = yy0.getTag(); // All the same. 

        if (xx0 == null || yy0 == null)
        { return null; } 


        int n = xs[0].arity(); 
        Vector[] xtermvectors = new Vector[n]; 
        ASTTerm[] targetValues = new ASTTerm[xs.length]; 

        for (int i = 0; i < n; i++)
        { xtermvectors[i] = new Vector(); } 

        Vector prefixes = new Vector();
        Vector prefixtermseqs = new Vector(); 
          // sequences of prefix terms.  

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return null; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms(); 
          Vector tterms = new Vector(); 
 
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
 
            if (xj instanceof ASTSymbolTerm) 
            { xtermvectors[j].add(xj); } 
            else if (tj != null) 
            { tterms.addAll(tj.getTerms()); } 
            else 
            { xtermvectors[j].add(xj); } 
          }  
            
          //  System.out.println(">>> Mapped terms = " + tterms); 
          //  System.out.println(">>> Target terms = " + yyterms); 

          Vector pref = 
              AuxMath.sequencePrefix(tterms,yyterms);
          prefixes.add(pref + "");  
          ASTTerm targterm = new ASTCompositeTerm(ttag,pref); 
          prefixtermseqs.add(targterm);
          targetValues[i] = targterm;  
        }

        Vector xx0terms = xx0.getTerms();
        Vector yy0terms = yy0.getTerms();
          
        for (int j = 0; j < xx0terms.size(); j++) 
        { ASTTerm xj = (ASTTerm) xx0terms.get(j); 
          ASTTerm tj = mod.getCorrespondingTree(xj);
  
          if (xj instanceof ASTSymbolTerm)
          { if (AuxMath.isConstantSeq(xtermvectors[j]))
            { pars.add(
                new BasicExpression(xj.literalForm())); 
            } 
            else 
            { pars.add(
                new BasicExpression("_" + (j+1))); 
            } 
          } 
          else if (tj != null) 
          { pars.add(
                new BasicExpression("_" + (j+1))); 
            tpars.add(
                new BasicExpression("_" + (j+1))); 
          }
          else 
          { pars.add(
                new BasicExpression("_" + (j+1)));
          } 
            // else - a source term that the suffix could 
            // functionally depend upon.  
        }  
 
        boolean found = false; 
        for (int j = 0; j < xtermvectors.length && !found; j++) 
        { Vector sjterms = xtermvectors[j]; 
          // System.out.println("<<>> Checking functional mapping from " + sjterms + " to " + prefixes); 
            
          if (sjterms.size() == prefixes.size()) // or <=
          { // System.out.println("<<>> Checking functional mapping from " + sjterms + " to " + prefixes); 
              
            if (ASTTerm.functionalASTMapping(sjterms,prefixes))
            { // System.out.println("<<>> Functional mapping from " + sjterms + " to " + prefixes); 
              java.util.Map sattvalueMap = 
                 new java.util.HashMap(); 

              ASTTerm[] sjtermvect = 
                 new ASTTerm[sjterms.size()];
              for (int p = 0; p < sjterms.size(); p++)
              { sjtermvect[p] = (ASTTerm) sjterms.get(p); }
 
              sattvalueMap.put(satt, sjtermvect); 
              AttributeMatching amprefix = 
                mod.composedTreeFunction(sent,tatt,
                      sourceatts,
                      sattvalueMap,targetValues,
                      prefixtermseqs, tms, ams); 

              if (amprefix != null && 
                  amprefix.trgvalue != null) 
              { BasicExpression nameexpr = 
                  new BasicExpression("name"); 
                nameexpr.setUmlKind(Expression.FUNCTION);
                nameexpr.addParameter(
                  new BasicExpression("_" + (j+1)));  
                Expression newjpar = 
                  amprefix.trgvalue.substituteEq(
                                         "_1",nameexpr);  
                tpars.add(0,newjpar); 
                found = true;
              }  
              
            } 
          } 
        }  
              
        if (!found) 
        { return null; } 
    
        BasicExpression srcterm = 
          new BasicExpression(xs[0]); 
        srcterm.setParameters(pars); 

        BasicExpression trgterm = 
          new BasicExpression(ys[0]); 
        trgterm.setParameters(tpars); 

        return new AttributeMatching(srcterm,trgterm); 
      }
 
      return null; 
    }

    public static AttributeMatching 
      suffixTreeMapping(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod,
                        Vector tms)
    { // Each ys[i].terms == concatenation of xs[i].terms 
      // plus suffix of terms, function of some x term. 

      Vector pars = new Vector(); 
      Vector tpars = new Vector(); 
      Vector tterms = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { // for (int i = 0; i < xs.length; i++)
        // { 
          ASTTerm xx = xs[0]; 
          ASTTerm yy = ys[0]; 

          if (xx == null || yy == null)
          { return null; } 

          Vector xxterms = xx.getTerms();
          Vector yyterms = yy.getTerms();
          
          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
  
            if (xj instanceof ASTSymbolTerm)
            { pars.add(
                new BasicExpression(xj.literalForm())); 
            } 
            else if (tj != null) 
            { pars.add(
                new BasicExpression("_" + (j+1))); 
              tpars.add(
                new BasicExpression("_" + (j+1))); 
              tterms.addAll(tj.getTerms());  
            }
            else 
            { pars.add(
                new BasicExpression("_" + (j+1))); 
            } 

            // else - a source term that the suffix could 
            // functionally depend upon. 
          }  

          for (int j = tterms.size(); j < yyterms.size(); j++)
          { ASTTerm yj = (ASTTerm) yyterms.get(j);
            tpars.add(new BasicExpression(yj)); 
          } // suffix 
            
        // }     
          
        BasicExpression srcterm = 
          new BasicExpression(xs[0]); 
        srcterm.setParameters(pars); 

        BasicExpression trgterm = 
          new BasicExpression(ys[0]); 
        trgterm.setParameters(tpars); 

        return new AttributeMatching(srcterm,trgterm); 
      } 
      return null; 
    }

    public static boolean tree2sequenceMapping(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod)
    { // Each ys[i].terms == same composition of mapped
      // xs[i].terms
      // plus constant prefixes/inserts/suffixes of terms. 

      // System.out.println(">&*>&*> Checking tree to sequence mapping"); 


      java.util.HashSet patterns = new java.util.HashSet(); 
      java.util.HashSet unuseds = new java.util.HashSet(); 


      if (ys.length > 1 && xs.length == ys.length)
      { String targettag = ys[0].getTag(); 


        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return false; } 

          Vector xxterms = xx.getTerms();
          Vector targets = yy.getTerms();

          Vector sources = new Vector(); 

          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
  
            if (xj instanceof ASTSymbolTerm)
            { } 
            else if (tj != null) 
            { // tj tag should be targettag

              if (tj.getTag().equals(targettag))
              { sources.add(tj.getTerms()); } 
              else 
              { return false; } 
            } 
            else 
            { return false; } 
          }

          Vector unused = new Vector(); 
          Vector pattern = 
            AuxMath.sequenceComposition(
              sources,targets,unused); 
  
          patterns.add(pattern + "");
          unuseds.add(unused + "");  
        } 
      } 

      // System.out.println("Unused source terms: " + unuseds); 
      // System.out.println("Mapping patterns: " + patterns); 

      if (patterns.size() == 1 && unuseds.size() == 1)
      { return true; }
      return false; 
    } 
 
    public static AttributeMatching 
      tree2SequenceMap(ASTTerm[] xs, 
                        ASTTerm[] ys, ModelSpecification mod,
                        Entity sent, Attribute satt, 
                        Attribute tatt, Vector sourceatts,
                        Vector tms, Vector ams)
    { // Each ys[i].terms == concatenation of xs[i].terms 
      // plus constant suffix of terms. 

      // System.out.println(">&*>&*> Assembling tree to sequence mapping"); 

      java.util.HashSet patterns = new java.util.HashSet(); 
      java.util.HashSet unuseds = new java.util.HashSet(); 

      Vector patternSequences = new Vector(); 

      if (ys.length > 1 && xs.length == ys.length)
      { String targettag = ys[0].getTag(); 

        for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return null; } 

          Vector xxterms = xx.getTerms();
          Vector targets = yy.getTerms();

          Vector sources = new Vector(); 

          for (int j = 0; j < xxterms.size(); j++) 
          { ASTTerm xj = (ASTTerm) xxterms.get(j); 
            ASTTerm tj = mod.getCorrespondingTree(xj);
  
            if (xj instanceof ASTSymbolTerm)
            { } 
            else if (tj != null) 
            { sources.add(tj.getTerms()); } 
            else 
            { return null; } 
          }

          Vector unused = new Vector(); 
          Vector pattern = 
            AuxMath.sequenceComposition(
              sources,targets,unused); 
  
          patterns.add(pattern + "");
          patternSequences.add(pattern); 
          unuseds.add(unused + "");  
        } 
     
        // System.out.println("Unused source terms: " + unuseds); 
        // System.out.println("Mapping patterns: " + patterns); 

        if (patterns.size() != 1 || unuseds.size() != 1)
        { return null; }
 
        Vector pars = new Vector(); 
        Vector tpars = new Vector(); 

        ASTTerm xx0 = xs[0]; 

        Vector xx0terms = xx0.getTerms();
        int xtermcount = 0; 
    
        for (int j = 0; j < xx0terms.size(); j++) 
        { ASTTerm xj = (ASTTerm) xx0terms.get(j); 
          ASTTerm tj = mod.getCorrespondingTree(xj);
        
          if (xj instanceof ASTSymbolTerm)
          { pars.add(
                new BasicExpression(xj.literalForm())); 
          } 
          else if (tj != null) 
          { pars.add(
                new BasicExpression("_" + (xtermcount+1))); 
          // tpars.add(
          //       new BasicExpression("_" + (xtermcount+1))); 
            xtermcount++; 
          }
        }
      
      // int n = xs[0].arity(); 
      // Vector[] xtermvectors = new Vector[n]; 
      // ASTTerm[] targetValues = new ASTTerm[xs.length]; 

      // for (int i = 0; i < n; i++)
      // { xtermvectors[i] = new Vector(); } 

      //  Vector suffixes = new Vector();
      //  Vector suffixtermseqs = new Vector(); 
          // sequences of suffix terms.  

        if (patternSequences.size() == 0) 
        { return null; } 

        Vector pseq = (Vector) patternSequences.get(0); 

        for (int k = 0; k < pseq.size(); k++) 
        { Object tt = pseq.get(k);

          if (tt instanceof ASTTerm) 
          { tpars.add(
               new BasicExpression((ASTTerm) tt));
          } 
          else if (tt instanceof String)
          { tpars.add(new BasicExpression((String) tt)); } 
        }
                     
        BasicExpression srcterm = 
          new BasicExpression(xs[0]); 
        srcterm.setParameters(pars); 

        BasicExpression trgterm = 
          new BasicExpression(ys[0]); 
        trgterm.setParameters(tpars); 

        return new AttributeMatching(srcterm,trgterm); 
      }
 
      return null; 
    }

    public static Vector targetBrackets(ASTTerm[] xs, ASTTerm[] ys)
    { // Is each ys[i].terms starts with same symbol and 
      // ends with another or same symbol? Return these symbols 
      Vector res = new Vector(); 
      String startSymbol = null; 
      String endSymbol = null; 
      
      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yy = ys[i]; 

          if (xx == null || yy == null)
          { return null; } 

          int n = xx.arity(); 
          int m = yy.arity(); 
        
          if (m < 2) 
          { return null; } 

          // ASTTerm s0 = xx.getTerm(0); 
          // ASTTerm sn = xx.getTerm(n-1); 

          ASTTerm t0 = yy.getTerm(0); 
          ASTTerm tn = yy.getTerm(m-1); 

          if (t0 instanceof ASTSymbolTerm && 
              tn instanceof ASTSymbolTerm)
          { if (startSymbol == null) 
            { startSymbol = t0.literalForm(); } 
            else if (startSymbol.equals(t0.literalForm())) { } 
            else 
            { return null; } // not consistent start symbols

            if (endSymbol == null) 
            { endSymbol = tn.literalForm(); } 
            else if (endSymbol.equals(tn.literalForm())) { } 
            else 
            { return null; } // not consistent end symbols
          } 
          else 
          { return null; } 
          // Should also be different to s0, sn
        } 
        
        if (startSymbol != null && endSymbol != null)
        { res.add(startSymbol); 
          res.add(endSymbol); 
          return res; 
        } 
      } 
      return null; 
    }


/* 
    public static boolean embeddedTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]' ..terms..) for same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTCompositeTerm && 
              ((ASTCompositeTerm) yvect).getTerms().size() > 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(xx,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

    public static boolean embeddedTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each ys[i] = (tag xs[i]' ..terms..) for same tag? 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTCompositeTerm && 
              ((ASTCompositeTerm) yvect).getTerms().size() > 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            if (xx.equals(ct0) || 
                mod.correspondingTrees(sent, xx, ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    }

 /*
  public static boolean nestedSingletonTrees(ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
    { // Is each xs[i] = (tag1 pi) where ys[i] = (tag2 pi) 

      if (ys.length > 1 && xs.length == ys.length)
      { for (int i = 0; i < xs.length; i++)
        { ASTTerm xx = xs[i]; 
          ASTTerm yvect = ys[i]; 

          System.out.println(">>>> Comparing " + xx + " to " + yvect); 

          if (yvect instanceof ASTBasicTerm && 
              xx instanceof ASTBasicTerm) 
          { String yy = ((ASTBasicTerm) yvect).getValue();
            String xsym = ((ASTBasicTerm) xx).getValue();  
            if (yy.equals(xsym)) { }
            else 
            { return false; }
          } 
          else if (yvect instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) yvect).getTerms().size() == 1 && 
                   xx instanceof ASTCompositeTerm && 
                   ((ASTCompositeTerm) xx).getTerms().size() == 1)
          { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
            ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
            ASTCompositeTerm xt = (ASTCompositeTerm) xx; 
            ASTTerm xt0 = (ASTTerm) xt.getTerms().get(0); 
            if (xt0.equals(ct0) || 
                mod.correspondingTrees(xt0,ct0)) { } 
            else 
            { return false; } 
          } 
          else 
          { return false; } 
        }
        return true; 
      } 
      return false; 
    } */ 

  public static boolean nestedSingletonTrees(Entity sent, ASTTerm[] xs, ASTTerm[] ys, ModelSpecification mod)
  { // Is each xs[i] = (tag1 pi) where ys[i] = (tag2 pi) 
    // Or (tag1 pi) (tag2 qi) where the pi ~ qi

    if (ys.length > 1 && xs.length == ys.length)
    { for (int i = 0; i < xs.length; i++)
      { ASTTerm xx = xs[i]; 
        ASTTerm yvect = ys[i]; 

        System.out.println(">>>> Comparing " + xx + " to " + yvect); 

        if (yvect instanceof ASTBasicTerm && 
            xx instanceof ASTBasicTerm) 
        { String yy = ((ASTBasicTerm) yvect).getValue();
          String xsym = ((ASTBasicTerm) xx).getValue();  
          if (yy.equals(xsym)) { }
          else 
          { return false; }
        } 
        else if (yvect instanceof ASTCompositeTerm && 
                 ((ASTCompositeTerm) yvect).getTerms().size() == 1 && 
                 xx instanceof ASTCompositeTerm && 
                 ((ASTCompositeTerm) xx).getTerms().size() == 1)
        { ASTCompositeTerm ct = (ASTCompositeTerm) yvect; 
          ASTTerm ct0 = (ASTTerm) ct.getTerms().get(0); 
          ASTCompositeTerm xt = (ASTCompositeTerm) xx; 
          ASTTerm xt0 = (ASTTerm) xt.getTerms().get(0); 
          if (xt0.equals(ct0) || 
              mod.correspondingTrees(sent,xt0,ct0)) 
          { System.out.println(">> Corresponding terms: " + xt0 + " " + ct0); } 
          else 
          { return false; } 
        } 
        else 
        { return false; } 
      }
      return true; 
    } 
    return false; 
  }

  public static AttributeMatching 
    compositeSource2TargetTrees(
      Entity sent, Attribute satt, 
      Attribute tatt, ASTTerm[] xs, ASTTerm[] ys, 
      ModelSpecification mod, Vector tms)
  { // Is there an index j of each xs[i] = (tag1 p1 ... pn) 
    // each pj = ys[i]  
    // or pj ~ ys[i]?
    // The nested mapping is then  _j |-->ys[0]
    // 
    // result = null indicates failure. 

    // JOptionPane.showInputDialog(">>>> compositeSource2TargetTrees " + xs[0] + " ---> " + ys[0]); 

    AttributeMatching res = null; 

    if (ys.length <= 1) 
    { return res; }

    if (xs.length != ys.length)
    { return res; } 

    // if (ys.length > 1 && xs.length == ys.length)
    ASTTerm s0 = xs[0]; 
    int xarity = s0.arity(); // put the longest one first

    for (int j = 0; j < xarity; j++) 
    { Vector jvect = new Vector(); 
      ASTTerm[] jterms = 
          ASTTerm.subterms(xs,j,jvect); 
      boolean jmatch = true; 
    
      for (int i = 0; i < xs.length && jmatch; i++)
      { ASTTerm xx = jterms[i]; 
        ASTTerm yy = ys[i]; 

        /* JOptionPane.showInputDialog(">>>> Comparing " + (j+1) + "th source subterm " + xx + " to " + yy); */ 

        // String xxlit = xx.literalForm(); 
        // String yylit = yy.literalForm(); 

        if (xx.equals(yy) || 
            mod.correspondingTrees(xx,yy)) 
        { System.out.println(">>-- Corresponding terms: " + xx + " " + yy); } 
        else 
        { jmatch = false; } 
      }

      if (jmatch) 
      { /* JOptionPane.showInputDialog(">> match from " + (j+1) + 
                    " source subterms to target " + ys[0]); */  
        BasicExpression sexpr = 
            BasicExpression.newASTBasicExpression(s0, xs);
          
        ASTTerm targ0 = ys[0]; 
        BasicExpression texpr = new BasicExpression(targ0);
        Vector newpars = new Vector(); 
        newpars.add(new BasicExpression("_" + (j+1))); 
        texpr.setParameters(newpars); 
 
        AttributeMatching amx = 
            new AttributeMatching(sexpr, texpr);
        return amx;   
      } 
    } // try next j
  
    /* JOptionPane.showInputDialog("!! No match found from " + xs[0] + 
                      " to " + ys[0] + " " + 
                      ASTTerm.allSingletonTrees(xs)); */  

    Vector sourceatts = new Vector(); 
    sourceatts.add(satt); 
          
    Vector trgJValues = new Vector(); 
    for (int k = 0; k < ys.length; k++) 
    { trgJValues.add(ys[k]); } 

    // Try to find tree mapping from some source to entire target

    for (int j = 0; j < xarity; j++) 
    { Vector jvect = new Vector(); 
      ASTTerm[] jterms = 
          ASTTerm.subterms(xs,j,jvect); 

      BasicExpression sexpr = 
        BasicExpression.newASTBasicExpression(
                           xs[0], xs); // jterms
      BasicExpression svar = 
            new BasicExpression("_" + (j+1));       

      java.util.HashMap sattvalueMap = new java.util.HashMap(); 
      sattvalueMap.put(satt, jterms); 
      Vector ams = new Vector(); 

      AttributeMatching amsub =  
          /* ASTTerm.compositeSource2TargetTrees(
               sent, sourceJValues1, ys, mod, tms); */ 
        mod.composedTreeFunction(sent,tatt,sourceatts,
              sattvalueMap, ys, trgJValues, tms, ams); 
     
      if (amsub != null)     
      {   
        String fid = 
             Identifier.nextIdentifier("subSourceF");
        TypeMatching tmnew = new TypeMatching(fid);
        tmnew.addValueMap(amsub);     
        tms.add(tmnew);

        /* JOptionPane.showInputDialog(">> " + fid + 
              " match from " + 
              (j+1) +   
              " source subterms " + jvect + 
              " to target: " + amsub); */ 
        
        BasicExpression fapp = 
            new BasicExpression(fid); 
        fapp.setUmlKind(Expression.FUNCTION);
        fapp.addParameter(svar);
        /* ASTTerm targ0 = ys[0]; 
        BasicExpression texpr = new BasicExpression(targ0);
        Vector newpars = new Vector(); 
        newpars.add(new BasicExpression("_" + (j+1))); 
        texpr.setParameters(newpars); */ 
 
        AttributeMatching amx = 
            new AttributeMatching(sexpr, fapp);
        return amx;   
      } 
    }

    /* JOptionPane.showInputDialog("!! No match found from " + xs[0] + 
                      " to " + ys[0] + " " + 
                      ASTTerm.allSingletonTrees(xs)); */ 

    // Try to unwrap the xs subterms. 
    for (int j = 0; j < xarity; j++) 
    { Vector jvect = new Vector(); 
      ASTTerm[] jterms = 
          ASTTerm.subterms(xs,j,jvect); 

    /*  JOptionPane.showInputDialog(">> Trying to find match from " + 
          (j+1) +   
          " nested source subterms " + jvect + 
          " to target: " + trgJValues); */ 

      Vector srcJValues1 = jvect; 
      ASTTerm[] sourceJValues1 = jterms;  
            // ASTTerm.subterms( 
            //          jterms,0,srcJValues1);

      ASTTerm[] subterms = sourceJValues1; 
        // Vector subtermsVector = srcJValues1; 

        // Unwrap until they are not all singletons. 
      int nestingDepth = 1; 

      while (ASTTerm.allSingletonTrees(subterms))
      { srcJValues1 = new Vector();
        sourceJValues1 = 
            ASTTerm.subterms( 
                      subterms,0,srcJValues1);
        subterms = sourceJValues1;
        nestingDepth++; 
      }

      java.util.HashMap sattvalueMap = new java.util.HashMap(); 
      sattvalueMap.put(satt,sourceJValues1); 
      Vector ams = new Vector(); 

      /* JOptionPane.showInputDialog("?? composedTreeFunction from " + 
          (j+1) +   
          " nested source subterms " + srcJValues1 + 
          " to target: " + trgJValues); */ 

        AttributeMatching amsub =  
          /* ASTTerm.compositeSource2TargetTrees(
               sent, sourceJValues1, ys, mod, tms); */ 
          mod.composedTreeFunction(sent,tatt,sourceatts,
                 sattvalueMap, ys, trgJValues, tms, ams); 

        if (amsub != null) 
        { BasicExpression sexpr = 
            BasicExpression.newASTBasicExpression(s0, xs);

          // If one subterm position is always the same, 
          // set it as a constant in sexpr. 

          // ASTTerm targ0 = ys[0]; 
          // BasicExpression texpr = new BasicExpression(targ0);
          // Vector newpars = new Vector(); 
          // newpars.add(new BasicExpression("_" + (j+1))); 
          // texpr.setParameters(newpars); 

          String fid = 
             Identifier.nextIdentifier("singleElement");
          TypeMatching tmnew = new TypeMatching(fid);
          tmnew.addValueMap(amsub);     
          tms.add(tmnew);
          BasicExpression svar = 
            new BasicExpression("_" + (j+1));       
          BasicExpression var1expr = new BasicExpression("_1"); 
          BasicExpression fapp = 
            new BasicExpression(fid); 
          fapp.setUmlKind(Expression.FUNCTION);
          fapp.addParameter(var1expr);

          /* JOptionPane.showInputDialog(">> " + fid + 
              " match from " + 
              (j+1) +   
              " source subterm depth " + nestingDepth + " to target " + amsub); */ 
          
          
          String tim = 
             Identifier.nextIdentifier("singleElementF");
          BasicExpression timexpr = 
            new BasicExpression(tim); 
          timexpr.setUmlKind(Expression.FUNCTION);
          timexpr.addParameter(svar);

          AttributeMatching amjx1 = 
            new AttributeMatching(var1expr, 
                                  fapp); 
          TypeMatching tmnew1 = 
                new TypeMatching(tim);
          tmnew1.addValueMap(amjx1);     
          tms.add(tmnew1);

          AttributeMatching amx = 
            new AttributeMatching(sexpr, timexpr);
          return amx; 
        } // It is actually sexpr |-->_(j+1)`tim where 
      }   // tim:: _1 |-->_1`fid
     

    return null; 
  }


  public static ASTTerm[] composeIntoTrees(Vector termSeqs, Vector res)
  { int n = termSeqs.size(); 
    if (n == 0) 
    { return null; } 
    ASTTerm[] terms0 = (ASTTerm[]) termSeqs.get(0); 
    ASTTerm[] trees = new ASTTerm[terms0.length]; 

    for (int i = 0; i < terms0.length; i++) 
    { ASTCompositeTerm ct = 
        new ASTCompositeTerm("TAG"); 
      for (int j = 0; j < n; j++) 
      { ASTTerm[] termsj = (ASTTerm[]) termSeqs.get(j); 
        ct.addTerm(termsj[i]); 
      } 
      trees[i] = ct; 
      res.add(ct); 
    } 
    return trees; 
  } 

  public static Vector generateASTExamples(Vector javaASTs)
  { /* Generate corresponding OCL and Java example ASTs */
    Vector res = new Vector(); 

    /* Basic numeric expressions: */ 

    for (int i = 500; i < 700; i++)
    { ASTBasicTerm tocl = new ASTBasicTerm("BasicExpression", ""+i); 
      res.add(tocl); 
      ASTBasicTerm javat1 = new ASTBasicTerm("integerLiteral", ""+i); 
      ASTCompositeTerm javat2 = new ASTCompositeTerm("literal", javat1);
      ASTCompositeTerm javat3 = new ASTCompositeTerm("expression", javat2);
      javaASTs.add(javat3); 
    }

    /* Unary expressions: */ 

    Vector oclunary = new Vector(); 
    Vector junary = new Vector();
 
    for (int i = 0; i < 200; i++)
    { Vector args = new Vector(); 
      args.add(new ASTSymbolTerm("-")); 
      args.add(res.get(i)); 
      ASTCompositeTerm ounary = 
        new ASTCompositeTerm("UnaryExpression", args); 
      oclunary.add(ounary); 
     
      Vector jargs = new Vector(); 
      jargs.add(new ASTSymbolTerm("-")); 
      jargs.add(javaASTs.get(i)); 
      ASTCompositeTerm jx = 
        new ASTCompositeTerm("expression", jargs); 
      junary.add(jx); 
    } 

    res.addAll(oclunary); 
    javaASTs.addAll(junary); 
      

    return res;  

  /* (expression (primary (literal (floatLiteral 0.33)))) */ 
  /* (expression (primary (literal "double"))) */ 
  /* (expression (primary (literal true))) */ 
  /* (expression (primary id)) */ 


    /* Binary numeric expressions: 

    ASTBasicTerm t1 = new ASTBasicTerm("BasicExpression", "1"); 
    ASTSymbolTerm st = new ASTSymbolTerm("+"); 
    ASTBasicTerm t2 = new ASTBasicTerm("BasicExpression", "2"); 
    Vector args = new Vector(); 
    args.add(t1); args.add(st); args.add(t2); 
    ASTCompositeTerm t = new ASTCompositeTerm("BinaryExpression", args); 
    res.add(t); 

    ASTBasicTerm jt1 = new ASTBasicTerm("integerLiteral", "1"); 
    ASTCompositeTerm jt2 = new ASTCompositeTerm("literal", jt1);
    ASTCompositeTerm jt3 = new ASTCompositeTerm("expression", jt2);
    ASTBasicTerm jt4 = new ASTBasicTerm("integerLiteral", "2"); 
    ASTCompositeTerm jt5 = new ASTCompositeTerm("literal", jt4);
    ASTCompositeTerm jt6 = new ASTCompositeTerm("expression", jt5); 
    ASTSymbolTerm stj = new ASTSymbolTerm("+"); 
    Vector jargs = new Vector(); 
    jargs.add(jt3); jargs.add(stj); jargs.add(jt6);
    ASTCompositeTerm tj = 
      new ASTCompositeTerm("expression", jargs); 
    javaASTs.add(tj); 
    return res; */ 
  }  

  public static void entitiesFromASTs(Vector asts, String suff, Vector es)
  { // For each tag t, create entity t+suff. Add subclass for 
    // each different arity of t terms.
    // Nesting: (t (t1 x) (t2 y)) becomes
    // t_t1$t_t2, etc.  

    Type treeType = new Type("OclAny", null); 

    for (int i = 0; i < asts.size(); i++) 
    { ASTTerm t = (ASTTerm) asts.get(i); 
      String tg = t.getTag() + suff; 
      int n = t.arity(); 
      if (n > 0) 
      { Entity ee = (Entity) ModelElement.lookupByName(tg,es); 
        if (ee == null) 
        { ee = new Entity(tg);

          System.out.println("Created entity: "  + tg); 
 
          if (suff.equals(""))
          { ee.addStereotype("source"); } 
          else 
          { ee.addStereotype("target"); } 
           
          ee.setAbstract(true); 
          Attribute astatt = 
            new Attribute("ast", treeType, ModelElement.INTERNAL); 
          ee.addAttribute(astatt); 
          es.add(ee); 
        } 

        String enname = tg + "_" + n; 
        Entity en = 
          (Entity) ModelElement.lookupByName(enname,es);
        if (en == null) 
        { en = new Entity(enname); 

          if (suff.equals(""))
          { en.addStereotype("source"); } 
          else 
          { en.addStereotype("target"); } 

          en.setSuperclass(ee); 
          ee.addSubclass(en); 
          System.out.println("Created entity: "  + enname); 
          es.add(en); 
        } 
      } 
    } 
  } 

  public static void deepEntitiesFromASTs(Vector asts, String suff, Vector es)
  { // For each different term t, create entity t' + suff.
    // Nesting: (t (t1 x) (t2 y)) entity has name
    // t_t1$t_t2, etc.  

    Type treeType = new Type("OclAny", null); 

    for (int i = 0; i < asts.size(); i++) 
    { ASTTerm t = (ASTTerm) asts.get(i); 
      String tg = t.tagFunction() + suff; 
      String tgsup = t.getTag() + suff; 
      int n = t.arity(); 
      if (n > 0) 
      { Entity sup = (Entity) ModelElement.lookupByName(tgsup,es); 
        if (sup == null) 
        { sup = new Entity(tgsup);

          System.out.println("Created entity: "  + sup); 
 
          if (suff.equals(""))
          { sup.addStereotype("source"); } 
          else 
          { sup.addStereotype("target"); } 
           
          sup.setAbstract(true); 
          Attribute astatt = 
            new Attribute("ast", treeType, ModelElement.INTERNAL); 
          sup.addAttribute(astatt); 
          es.add(sup); 
        } 

        Entity ee = (Entity) ModelElement.lookupByName(tg,es); 
        if (ee == null) 
        { ee = new Entity(tg);

          System.out.println("Created entity: "  + tg); 
 
          if (suff.equals(""))
          { ee.addStereotype("source"); } 
          else 
          { ee.addStereotype("target"); } 
           
          ee.setSuperclass(sup); 
          sup.addSubclass(ee); 
          es.add(ee); 
        } 
      } 
    } 
  } 

  public static Vector entityMatchingsFromASTs(Vector sasts, 
                         Vector tasts, Vector es)
  { // For corresponding s, t create entity matching
    // s.tag_s.arity |--> t.tag$T_t.arity. 

    Vector ems = new Vector(); 

    for (int i = 0; i < sasts.size() && i < tasts.size(); i++) 
    { ASTTerm s = (ASTTerm) sasts.get(i); 
      ASTTerm t = (ASTTerm) tasts.get(i); 
      
      int n = s.arity(); 
      int m = t.arity(); 

      if (n > 0 && m > 0) 
      { String sentname = s.getTag() + "_" + n; 
        Entity sent = 
          (Entity) ModelElement.lookupByName(sentname,es); 
        String tentname = t.getTag() + "$T_" + m; 
        Entity tent = 
          (Entity) ModelElement.lookupByName(tentname,es); 
        if (sent != null && tent != null) 
        { EntityMatching em = 
            new EntityMatching(sent,tent); 
          if (ems.contains(em)) { }
          else 
          { ems.add(em); }  
        } 
      } 
    } 
    return ems; 
  } 

  public static Vector deepEntityMatchingsFromASTs(Vector sasts, 
                         Vector tasts, Vector es)
  { // For corresponding s, t create entity matching
    // s.tag_s.arity |--> t.tag$T_t.arity. 

    Vector ems = new Vector(); 

    for (int i = 0; i < sasts.size() && i < tasts.size(); i++) 
    { ASTTerm s = (ASTTerm) sasts.get(i); 
      ASTTerm t = (ASTTerm) tasts.get(i); 
      
      int n = s.arity(); 
      int m = t.arity(); 

      if (n > 0 && m > 0) 
      { String sentname = s.tagFunction(); // Assume it is deep 
        Entity sent = 
          (Entity) ModelElement.lookupByName(sentname,es); 
        String tentname = t.getTag() + "$T_" + m; 
        Entity tent = 
          (Entity) ModelElement.lookupByName(tentname,es); 
        if (sent != null && tent != null) 
        { EntityMatching em = 
            new EntityMatching(sent,tent); 
          if (ems.contains(em)) { }
          else 
          { ems.add(em); }  
        } 
      } 
    } 
    return ems; 
  } 

  public static void modelSpecificationFromASTs(Vector sasts, 
                         Vector tasts, Vector es, 
                         ModelSpecification mod)
  { // For corresponding s, t create instances
    // s_inst : s.tag_s.arity 
    // s_inst.ast = s
    // t_inst : t.tag$T_t.arity
    // t_inst.ast = t
    // s_inst |-> t_inst 

    for (int i = 0; i < sasts.size() && i < tasts.size(); i++) 
    { ASTTerm s = (ASTTerm) sasts.get(i); 
      ASTTerm t = (ASTTerm) tasts.get(i); 
      
      int n = s.arity(); 
      int m = t.arity(); 

      if (n > 0 && m > 0) 
      { String sentname = s.getTag() + "_" + n; 
        String tentname = t.getTag() + "$T_" + m; 
        String sinst = sentname.toLowerCase() + "_" + i; 
        String tinst = tentname.toLowerCase() + "_" + i; 
        Entity sent = 
          (Entity) ModelElement.lookupByName(sentname,es); 
        ObjectSpecification sobj = 
          new ObjectSpecification(sinst,sentname); 
        sobj.setEntity(sent);
        sobj.setValue("ast", s); 
        Entity tent = 
          (Entity) ModelElement.lookupByName(tentname,es); 
        ObjectSpecification tobj = 
          new ObjectSpecification(tinst,tentname); 
        tobj.setEntity(tent);
        tobj.setValue("ast", t); 
        mod.addObject(sobj); 
        mod.addObject(tobj); 
        mod.addCorrespondence(sobj,tobj); 
      } 
    } 
  } 

  public static void deepModelSpecificationFromASTs(Vector sasts, 
                         Vector tasts, Vector es, 
                         ModelSpecification mod)
  { // For corresponding s, t create instances
    // s_inst : s.tag_s.arity 
    // s_inst.ast = s
    // t_inst : t.tag$T_t.arity
    // t_inst.ast = t
    // s_inst |-> t_inst 

    for (int i = 0; i < sasts.size() && i < tasts.size(); i++) 
    { ASTTerm s = (ASTTerm) sasts.get(i); 
      ASTTerm t = (ASTTerm) tasts.get(i); 
      
      int n = s.arity(); 
      int m = t.arity(); 

      if (n > 0 && m > 0) 
      { String sentname = s.tagFunction(); 
        String tentname = t.getTag() + "$T_" + m; 
        String sinst = sentname.toLowerCase() + "_" + i; 
        String tinst = tentname.toLowerCase() + "_" + i; 
        Entity sent = 
          (Entity) ModelElement.lookupByName(sentname,es); 
        ObjectSpecification sobj = 
          new ObjectSpecification(sinst,sentname); 
        sobj.setEntity(sent);
        sobj.setValue("ast", s); 
        Entity tent = 
          (Entity) ModelElement.lookupByName(tentname,es); 
        ObjectSpecification tobj = 
          new ObjectSpecification(tinst,tentname); 
        tobj.setEntity(tent);
        tobj.setValue("ast", t); 
        mod.addObject(sobj); 
        mod.addObject(tobj); 
        mod.addCorrespondence(sobj,tobj); 
      } 
    } 
  } 

  public static Vector randomBasicASTTermsForTag(String tag, 
                                       int n, int numb)
  { // numb (tag t1 ... tn) terms 
    Vector res = new Vector(); 
    for (int i = 0; i < numb; i++) 
    { Vector args = new Vector(); 
      for (int j = 0; j < n; j++) 
      { String rstring = ModelElement.randomString(10); 
        args.add(new ASTSymbolTerm(rstring)); 
      } 
      ASTCompositeTerm ct = 
        new ASTCompositeTerm(tag,args); 
      res.add(ct); 
    } 
    return res; 
  }    

  public static Vector randomCompositeASTTermsForTag(
    String tag, Vector subtermVectors, 
    int n, int numb)
  { // numb (tag t1 ... tn) terms where ti is a random
    // term from subtermVectors[i-1]
 
    Vector res = new Vector(); 
    for (int i = 0; i < numb; i++) 
    { Vector args = new Vector(); 
      for (int j = 0; j < n; j++) 
      { Vector termsj = (Vector) subtermVectors.get(j); 
        if (termsj.size() == 1) 
        { args.add(termsj.get(0)); } 
        else 
        { args.add(ModelElement.randomElement(termsj)); } 
      } 
      ASTCompositeTerm ct = 
        new ASTCompositeTerm(tag,args); 
      res.add(ct); 
    } 
    return res; 
  }    
 
  public abstract String antlr2cstl();
    
  public abstract String antlrElement2cstl(Vector rulerefs, Vector conds);
 
  public abstract Vector normaliseAntlr();

  public abstract Vector cobolDataDefinitions(java.util.Map context, Vector invs); 

  public abstract Vector cobolPerformThruDefinitions(java.util.Map context, Vector invs); 

  public abstract int cobolDataWidth(); 

  public abstract int cobolIntegerWidth(); 

  public abstract int cobolFractionWidth(); 

  public abstract Type cobolDataType(); 

  public abstract boolean cobolIsSigned(); 

  public static String coefficientOfPower(ASTTerm var, int n, 
                                          ASTTerm expr)
  { // coefficient of n-th power of v in expr, n > 0

    String v = var.literalForm(); 

    boolean isIn = ASTTerm.isSubterm(var,expr); 
    if (!isIn)
    { return "0"; }

    if (n == 1)
    { return coefficientOf(var,expr); }  
    
    if (n == 2) 
    { return coefficientOfSquare(var,expr); } 

    // Vector powers = ASTTerm.powersOf(var,expr);
    // if (VectorUtil.containsEqualString("" + n, powers) ||
    //     VectorUtil.containsEqualString(n + ".0", powers))
    // { } 
    // else 
    // { return "0"; } 

    String thisLiteral = expr.literalForm(); 

    if (v.equals(thisLiteral) && n == 1) 
    { return "1"; } 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.coefficientOfPower(var, n, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.coefficientOfPower(var, n, tt); 
        } 
      } 

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = 
                ASTTerm.coefficientOfPower(var, n, t1); 
            String coef2 = 
                ASTTerm.coefficientOfPower(var, n, t2);
 
            if ("0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1))
            { return "-(" + coef2 + ")"; } 
             
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2);  
            } 
              
            return coef1 + " - (" + coef2 + ")"; 
          }
        } 

        return "0"; 
      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = 
                ASTTerm.coefficientOfPower(var, n, t1); 
            String coef2 = 
                ASTTerm.coefficientOfPower(var, n, t2); 
            if ("0".equals(coef2) || "0.0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1) && "-".equals(opr))
            { return "-(" + coef2 + ")"; } 
            if ("0".equals(coef1) && "+".equals(opr))
            { return coef2; } 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = 
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              if ("+".equals(opr))
              { return "" + (val1 + val2); } 
              else 
              { return "" + (val1 - val2); } 
            } 
              
            if ("-".equals(opr))
            { return coef1 + " - (" + coef2 + ")"; } 
            else 
            { return coef1 + " + " + coef2; } 
          } 
        } 

        return "0"; 
      }  


      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            if (v.equals(tt.literalForm()) && n == 1)
            { return "-1"; } 
  
            String coef1 = 
               ASTTerm.coefficientOfPower(var, n, tt); 
            if (coef1.equals("0") || coef1.equals("0.0"))
            { return coef1; } 
            return "-(" + coef1 + ")"; 
          } 


          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = 
               ASTTerm.coefficientOfPower(var, n, tt); 
            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            Vector vv = new Vector(); 
            vv.add(var); 

            String cnst1 = ASTTerm.constantTerms(vv,t1); 
            String cnst2 = ASTTerm.constantTerms(vv,t2); 

            String coef1n = 
              ASTTerm.coefficientOfPower(var, n, t1); 
            String coef2n =                  
              ASTTerm.coefficientOfPower(var, n, t2); 

            String coef1 = t1.literalForm(); 
            String coef2 = t2.literalForm(); 

            // System.out.println(v + " in " + coef1 + " " + isIn1); 
            // System.out.println(v + " in " + coef2 + " " + isIn2); 

            String sumcoefs = "0"; 

            if ("0".equals(cnst1) || "0.0".equals(cnst1) ||
                "(0)".equals(cnst1))
            { }
            else if ("0".equals(coef2n) || 
                     "0.0".equals(coef2n) ||
                     "(0)".equals(coef2n))
            { } 
            else if (AuxMath.isGeneralNumeric(cnst1) &&
                AuxMath.isGeneralNumeric(coef2n))
            { double val1 = 
                    AuxMath.generalNumericValue(cnst1); 
              double val2 = 
                    AuxMath.generalNumericValue(coef2n);
              sumcoefs = sumcoefs + " + " + (val1 * val2);  
            } 
            else 
            { sumcoefs = 
                     sumcoefs + " + " + 
                     "(" + cnst1 + ")*(" + coef2n + ")";
            } 

            if ("0".equals(cnst2) || "0.0".equals(cnst2) ||
                "(0)".equals(cnst2))
            { }
            else if ("0".equals(coef1n) || 
                     "0.0".equals(coef1n) ||
                     "(0)".equals(coef1n))
            { } 
            else if (AuxMath.isGeneralNumeric(cnst2) &&
                AuxMath.isGeneralNumeric(coef1n))
            { double val1 = 
                    AuxMath.generalNumericValue(cnst2); 
              double val2 = 
                    AuxMath.generalNumericValue(coef1n);
              sumcoefs = sumcoefs + " + " + (val1 * val2);  
            } 
            else 
            { sumcoefs = 
                     sumcoefs + " + " + 
                     "(" + cnst2 + ")*(" + coef1n + ")";
            } 

            for (int i = 1; i < n; i++) 
            { coef1 = ASTTerm.coefficientOfPower(var, i, t1);
              coef2 = ASTTerm.coefficientOfPower(var, n-i, t2);

                // System.out.println(v + "->pow(" + i + ") coef= " + coef1); 
                // System.out.println(v + "->pow(" + (n-i) + ") coef= " + coef2); 

              if (AuxMath.isGeneralNumeric(coef1))
              { coef1 = 
                  "" + AuxMath.generalNumericValue(coef1); 
              } 

              if (AuxMath.isGeneralNumeric(coef2))
              { coef2 = 
                  "" + AuxMath.generalNumericValue(coef2); 
              } 

              if ("0".equals(coef1) || "0.0".equals(coef1) ||
                  "(0)".equals(coef1))
              { }
              else if ("0".equals(coef2) || 
                       "0.0".equals(coef2) ||
                       "(0)".equals(coef2))
              { } 
              else if (AuxMath.isGeneralNumeric(coef1) &&
                  AuxMath.isGeneralNumeric(coef2))
              { double val1 = 
                    AuxMath.generalNumericValue(coef1); 
                double val2 = 
                    AuxMath.generalNumericValue(coef2);
                sumcoefs = sumcoefs + " + " + (val1 * val2);  
              } 
              else 
              { sumcoefs = 
                     sumcoefs + " + " + 
                     "(" + coef1 + ")*(" + coef2 + ")";
              } 
            }  

            return sumcoefs;  
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            String coef1 = t1.literalForm(); 
            if (isIn1)
            { coef1 = 
                ASTTerm.coefficientOfPower(var, n, t1); 
            }
            String coef2 = t2.literalForm();
 
            if (isIn2)
            { JOptionPane.showMessageDialog(null, 
                 "!! Cannot determine coefficients for " + thisLiteral + ". It needs to be put in polynomial form",   "",
                 JOptionPane.INFORMATION_MESSAGE); 
            }

            if ("0".equals(coef1) || "0.0".equals(coef1))
            { return "0"; }  

            if (coef1.equals("1") || coef1.equals("1.0"))
            { return "1/(" + coef2 + ")"; } 

            if (coef2.equals("1") || coef2.equals("1.0"))
            { return coef1; } 

            return "(" + coef1 + ")/(" + coef2 + ")"; 
          } 
          else if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = 
              ASTTerm.coefficientOfPower(var, n, t1); 
            String coef2 = 
              ASTTerm.coefficientOfPower(var, n, t2);
 
            if ("0".equals(coef2) || "0.0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1) || "0.0".equals(coef1))
            { return "-(" + coef2 + ")"; } 
             
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2);  
            } 
              
            return coef1 + " - (" + coef2 + ")"; 
          } 
        } 
      }
    } 
   
    return "0"; 
  }  


  public static String coefficientOf(
                            ASTTerm var, ASTTerm expr)
  { // expr is coef*var where coef does not involve var

    // boolean isIn = ASTTerm.isSubterm(var,expr); 
    // if (!isIn)
    // { return "0"; } 

    if (expr instanceof ASTCompositeTerm)
    { ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      if ("factorExpression".equals(ct.getTag()) && 
          "†".equals(ct.getTerm(0) + ""))
      { String sq = coefficientOfSquare(var, ct.getTerm(1));
        if ("1.0".equals(sq) || "1".equals(sq))
        { return "1"; }
        if ("0.0".equals(sq) || "0".equals(sq))
        { return "0"; }
        else 
        { return "†(" + sq + ")"; } 
      }  
    } 

    Vector powers = ASTTerm.powersOf(var,expr);
    if (VectorUtil.containsEqualString("1", powers) ||
        VectorUtil.containsEqualString("1.0", powers))
    { } 
    else 
    { return "0"; } 

    String v = var.literalForm(); 
    String thisLiteral = expr.literalForm(); 

    if (v.equals(thisLiteral) || 
        thisLiteral.equals("(" + v + ")"))
    { return "1"; } 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.coefficientOf(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.coefficientOf(var, tt); 
        } 
      } 

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = ASTTerm.coefficientOf(var, t1); 
            String coef2 = ASTTerm.coefficientOf(var, t2);
 
            if ("0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1))
            { return "-(" + coef2 + ")"; } 
             
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = 
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2);  
            } 
              
            return coef1 + " - (" + coef2 + ")"; 
          }
        } 

        return "0"; 
      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = ASTTerm.coefficientOf(var, t1); 
            String coef2 = ASTTerm.coefficientOf(var, t2); 
            if ("0".equals(coef2) || "0.0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1) && "-".equals(opr))
            { return "-(" + coef2 + ")"; } 
            if ("0".equals(coef1) && "+".equals(opr))
            { return coef2; } 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = 
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              if ("+".equals(opr))
              { return "" + (val1 + val2); } 
              else 
              { return "" + (val1 - val2); } 
            } 
              
            if ("-".equals(opr))
            { return coef1 + " - (" + coef2 + ")"; } 
            else 
            { return coef1 + " + " + coef2; } 
          } 
        } 

        return "0"; 
      }  


      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            if (v.equals(tt.literalForm()))
            { return "-1"; } 
  
            String coef1 = ASTTerm.coefficientOf(var, tt); 
            if (coef1.equals("0") || coef1.equals("0.0"))
            { return coef1; } 
            return "-(" + coef1 + ")"; 
          } 


          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            if (v.equals(tt.literalForm()))
            { return "1"; } 

            String coef1 = ASTTerm.coefficientOf(var, tt); 
            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            Vector vv = new Vector(); 
            vv.add(var); 

            String cnst1 = ASTTerm.constantTerms(vv,t1); 
            String cnst2 = ASTTerm.constantTerms(vv,t2); 

            if (v.equals(t1.literalForm()))
            { return cnst2; } 

            if (v.equals(t2.literalForm()))
            { return cnst1; }

            String coef1 = ASTTerm.coefficientOf(var, t1);
            String coef2 = ASTTerm.coefficientOf(var, t2); 
            
            String total = "0"; 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(cnst2))
            { double val1 = 
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(cnst2);
              total = "" + (val1 * val2);  
            } 
            else 
            { total = "(" + coef1 + ")*(" + cnst2 + ")"; } 

            if (AuxMath.isGeneralNumeric(coef2) &&
                AuxMath.isGeneralNumeric(cnst1))
            { double val1 = 
                AuxMath.generalNumericValue(coef2); 
              double val2 = 
                AuxMath.generalNumericValue(cnst1);
              total = total + " + " + (val1 * val2);  
            } 
            else 
            { total = total + " + (" + coef2 + ")*(" + cnst1 + ")"; } 

            return total; 
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            String coef1 = t1.literalForm(); 
            if (isIn1)
            { coef1 = ASTTerm.coefficientOf(var, t1); }
            String coef2 = t2.literalForm();
 
            if (isIn2)
            { JOptionPane.showMessageDialog(null, 
                 "!! Cannot determine coefficients for " + thisLiteral + ". It needs to be put in polynomial form",   "",
                 JOptionPane.INFORMATION_MESSAGE); 
              coef2 = ASTTerm.coefficientOf(var, t2); 
            }

            if ("0".equals(coef1) || "0.0".equals(coef1))
            { return "0"; }  

            if (coef1.equals("1") || coef1.equals("1.0"))
            { return "1/(" + coef2 + ")"; } 

            if (coef2.equals("1") || coef2.equals("1.0"))
            { return coef1; } 

            return "(" + coef1 + ")/(" + coef2 + ")"; 
          } 
          else if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = ASTTerm.coefficientOf(var, t1); 
            String coef2 = ASTTerm.coefficientOf(var, t2);
 
            if ("0".equals(coef2) || "0.0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1) || "0.0".equals(coef1))
            { return "-(" + coef2 + ")"; } 
             
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2);  
            } 
              
            return coef1 + " - (" + coef2 + ")"; 
          } 
        } 
      }
    } 
   
    return "0"; 
  }  

  public static Vector powersOf(
                            ASTTerm var, ASTTerm expr)
  { // powers of var which occur in expr, assumed to be 
    // expanded as a sum of products of powers & coefficients. 

    Vector res = new Vector(); 

    boolean isIn = ASTTerm.isSubterm(var,expr); 
    if (!isIn)
    { res.add(0);
      return res;
    } 

    String v = var.literalForm(); 
    String elit = expr.literalForm(); 

    if (v.equals(elit) ||
        (v + "^{1}").equals(elit) || 
        (v + "^{1.0}").equals(elit))
    { res.add(1); 
      return res; 
    } 

    if ((v + "^{2}").equals(elit) || 
        (v + "^{2.0}").equals(elit))
    { res.add(2); 
      return res; 
    } 

    if ((v + "^{3}").equals(elit) || 
        (v + "^{3.0}").equals(elit))
    { res.add(3); 
      return res; 
    } 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms();
      String tg = ct.getTag();  
 
      if (subterms.size() == 1) 
      { return ASTTerm.powersOf(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("factorExpression".equals(tg) && 
          "†".equals(ct.getTerm(0) + ""))
      { Vector sqrtpowers = powersOf(var, ct.getTerm(1));
        return VectorUtil.vectorDivide(sqrtpowers,2);  
      }  

      if ("basicExpression".equals(tg))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.powersOf(var, tt); 
        } 
      } 

      if ("logicalExpression".equals(tg) || 
          "equalityExpression".equals(tg))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);
            Vector ress = ASTTerm.powersOf(var, t1); 
            ress.addAll(ASTTerm.powersOf(var, t2)); 
            return ress; 
          }
        } 

        res.add(0); 
        return res; 
      }  

      if ("additiveExpression".equals(tg))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            Vector powers1 = ASTTerm.powersOf(var, t1); 
            Vector powers2 = ASTTerm.powersOf(var, t2); 
            res.addAll(powers1); 
            res.addAll(powers2); 
            return res; 
          } 
        } 

        res.add(0); 
        return res; 
      }  

      if ("factor2Expression".equals(tg))
      { if (subterms.size() == 5 && 
            "^".equals(subterms.get(1) + "") && 
            "{".equals(subterms.get(2) + "") && 
            "}".equals(subterms.get(4) + ""))
        { ASTTerm arg = (ASTTerm) subterms.get(0); 
          ASTTerm pow = (ASTTerm) subterms.get(3); 
          String powlit = pow.literalForm(); 

          if (AuxMath.isGeneralNumeric(powlit))
          { double powd = AuxMath.parseGeneralNumeric(powlit); 
            if (v.equals(arg.literalForm()))
            { res.add(powd);  
              return res;
            }
            else 
            { Vector powers1 = ASTTerm.powersOf(var, arg); 
              res = VectorUtil.vectorMultiplication(powers1,powd); 
              return res; 
            }                 
          } 
        } 

        res.add(0); 
        return res; 
      }  


      if ("factorExpression".equals(tg))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            if (v.equals(tt.literalForm()))
            { res.add(1);
              return res;
            } 
  
            return ASTTerm.powersOf(var, tt); 
          } 


          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            if (v.equals(tt.literalForm()))
            { res.add(1);
              return res;
            } 

            return ASTTerm.powersOf(var, tt); 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          Vector mres = new Vector(); 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            if (v.equals(t1.literalForm()) && 
                v.equals(t2.literalForm()))
            { mres.add(2);
              return mres;
            } 
            else if (v.equals(t1.literalForm()))
            { mres.add(1);
              Vector powerst2 = ASTTerm.powersOf(var, t2);
              if (powerst2.size() == 0) 
              { return mres; } 
              return 
                VectorUtil.vectorSummation(mres,powerst2);
            } 
            else if (v.equals(t2.literalForm()))
            { mres.add(1);
              Vector powerst1 = ASTTerm.powersOf(var, t1);
              if (powerst1.size() == 0) 
              { return mres; } 
              return 
                VectorUtil.vectorSummation(mres,powerst1);
            }

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            Vector powers1 = ASTTerm.powersOf(var, t1);
            Vector powers2 = ASTTerm.powersOf(var, t2);

            /* System.out.println(">>> IS IN: " + isIn1 + " " + isIn2 + " powers " + powers1 + " " + powers2); */ 

            if (isIn1 && isIn2)
            { 
              // result is x + y for x : powers1, y : powers2
              return VectorUtil.vectorSummation(
                                      powers1,powers2); 
            }
            else if (isIn1)
            { return powers1; }
            else 
            { return powers2; } 
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            Vector powers1 = new Vector(); 
            Vector powers2 = new Vector();
 
            if (isIn1 && isIn2)
            { powers1 = ASTTerm.powersOf(var, t1); 
              powers2 = ASTTerm.powersOf(var, t2);
 
              // result is x - y for x : powers1, y : powers2
              return VectorUtil.vectorSubtraction(
                                      powers1,powers2); 
            }
            else if (isIn1)
            { return ASTTerm.powersOf(var, t1); }
            else 
            { Vector powers3 = ASTTerm.powersOf(var, t2); 
              Vector powers4 = new Vector(); 
              powers4.add(0); 
              return VectorUtil.vectorSubtraction(
                                      powers4,powers3);
            }
          } 
          else if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);
            Vector ress = ASTTerm.powersOf(var, t1); 
            ress.addAll(ASTTerm.powersOf(var, t2)); 
            return ress;  
          } 
        } 
      }
    } 
   
    return res; 
  }  

  public static Vector differentialsOf(
                            ASTTerm var, ASTTerm expr)
  { // differentials of var which occur in expr
    // for v being "x" these are (factorExpression ..x.. ´)
    Vector res = new Vector(); 

    boolean isIn = ASTTerm.isSubterm(var,expr); 
    if (!isIn)
    { res.add(0);
      return res;
    } 

    String v = var.literalForm(); 

    if (v.equals(expr.literalForm()))
    { res.add(0); 
      return res; 
    } 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2)
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.differentialsOf(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.differentialsOf(var, tt); 
        } 
      } 

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);
            Vector ress = ASTTerm.differentialsOf(var, t1); 
            ress.addAll(ASTTerm.differentialsOf(var, t2)); 
            return ress; 
          }
        } 

        res.add(0); 
        return res; 
      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            Vector powers1 = ASTTerm.differentialsOf(var, t1); 
            Vector powers2 = ASTTerm.differentialsOf(var, t2); 
            res.addAll(powers1); 
            res.addAll(powers2); 
            return res; 
          } 
        } 

        res.add(0); 
        return res; 
      }  

      if ("factor2Expression".equals(ct.tag))
      { if (subterms.size() == 5 && 
            "^".equals(subterms.get(1) + "") && 
            "{".equals(subterms.get(2) + "") && 
            "}".equals(subterms.get(4) + ""))
        { ASTTerm arg = (ASTTerm) subterms.get(0); 
          // ASTTerm pow = (ASTTerm) subterms.get(3); 
          // if (v.equals(arg.literalForm()))
          // { res.add(pow.literalForm()); 
          //   return res; 
          // }
          return arg.differentialsOf(var, arg);  
        } 

        res.add(0); 
        return res; 
      }  


      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { ASTTerm t1 = (ASTTerm) subterms.get(0); 
          String opr = t1 + ""; 
          ASTTerm t2 = (ASTTerm) subterms.get(1); 
          String t2lit = t2.literalForm(); 

          if ("-".equals(opr))
          { 
            if (v.equals(t2lit))
            { res.add(0);
              return res;
            } 
  
            return ASTTerm.differentialsOf(var, t2); 
          } 


          if ("+".equals(opr))
          { 
            if (v.equals(t2lit))
            { res.add(0);
              return res;
            } 

            return ASTTerm.differentialsOf(var, t2); 
          } 

          if ("´".equals(t2 + ""))
          { if (v.equals(t1.literalForm()))
            { res.add(1); 
              return res; 
            } 

            res.add(1);
            Vector powerst2 = ASTTerm.differentialsOf(var, t1);
            return VectorUtil.vectorSummation(res,powerst2);
          } // v must be a subterm of t2
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr) || "/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            if (v.equals(t1.literalForm()) && 
                v.equals(t2.literalForm()))
            { res.add(0);
              return res;
            } 
            else if (v.equals(t1.literalForm()))
            { 
              return ASTTerm.differentialsOf(var, t2);
            } 
            else if (v.equals(t2.literalForm()))
            { return ASTTerm.differentialsOf(var, t1); }

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            Vector powers1 = new Vector(); 
            Vector powers2 = new Vector(); 

            if (isIn1 && isIn2)
            { powers1 = ASTTerm.differentialsOf(var, t1); 
              powers2 = ASTTerm.differentialsOf(var, t2);
 
              // result is max(x,y) for x : powers1, y : powers2
              Vector allpows = VectorUtil.vectorMax(powers1, powers2);
              res.addAll(allpows); 
              return res;  
            }
            else if (isIn1)
            { return ASTTerm.differentialsOf(var, t1); }
            else 
            { return ASTTerm.differentialsOf(var, t2); } 
          } 
          else if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            Vector diffs1 = ASTTerm.differentialsOf(var, t1);
            Vector diffs2 = ASTTerm.differentialsOf(var, t2);
            res.addAll(diffs1); res.addAll(diffs2); 
            return res;  
          } 
        } 
      }
    } 
   
    return res; 
  }  

  public static String coefficientOfSquare(
                            ASTTerm var, ASTTerm expr)
  { // expr is coef*var*var or coef*var^{2}

    // boolean isIn = ASTTerm.isSubterm(var,expr); 
    // if (!isIn)
    // { return "0"; } 

    Vector powers = ASTTerm.powersOf(var, expr);

    /* 
    JOptionPane.showMessageDialog(null, 
           ">>> Powers of : " + var + " in " + expr +  
           " are " + powers, 
           "", 
           JOptionPane.INFORMATION_MESSAGE); */ 

    if (VectorUtil.containsEqualString("2", powers) ||
        VectorUtil.containsEqualString("2.0", powers))
    { } 
    else 
    { return "0"; } 

    String elit = expr.literalForm(); 

    String v = var.literalForm(); 
    String vsqr = v + "*" + v; 
    String vpow = v + "^{2}"; 
    String vpow2 = v + "^{2.0}"; 

    String vsqrb = "(" + v + "*" + v + ")"; 
    String vpowb = "(" + v + ")^{2}"; 
    String vpow2b = "(" + v + ")^{2.0}"; 

    if (vsqr.equals(elit) || vpow.equals(elit) || 
        vpow2.equals(elit))
    { return "1"; } 

    if (vsqrb.equals(elit) || 
        elit.equals("(" + vpow + ")") || 
        vpowb.equals(elit) || 
        elit.equals("(" + vpow2 + ")") ||
        vpow2b.equals(elit))
    { return "1"; } 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.coefficientOfSquare(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.coefficientOfSquare(var, tt); 
        } 
      } 

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = ASTTerm.coefficientOfSquare(var, t1); 
            String coef2 = ASTTerm.coefficientOfSquare(var, t2);
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2); 
            } 

            return coef1 + " - (" + coef2 + ")"; 
          }
        } 

        return "0"; 
      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = ASTTerm.coefficientOfSquare(var, t1); 
            String coef2 = ASTTerm.coefficientOfSquare(var, t2); 
            
            if ("0".equals(coef2) || "0.0".equals(coef2))
            { return coef1; }
            if (("0".equals(coef1) || "0.0".equals(coef1))
                && "-".equals(opr))
            { return "-(" + coef2 + ")"; } 
            if (("0".equals(coef1) || "0.0".equals(coef1))
                && "+".equals(opr))
            { return coef2; } 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              if ("+".equals(opr))
              { return "" + (val1 + val2); } 
              else 
              { return "" + (val1 - val2); }   
            } 
     
            return coef1 + " " + opr + " (" + coef2 + ")"; 
          } 
        } 

        return "0"; 
      }  

      if ("factor2Expression".equals(ct.tag))
      { if (subterms.size() == 4 && 
            "^{".equals(subterms.get(1) + "") &&  
            "}".equals(subterms.get(3) + ""))
        { ASTTerm arg = (ASTTerm) subterms.get(0); 
          ASTTerm pow = (ASTTerm) subterms.get(2); 
          String arglit = arg.literalForm(); 
          String powlit = pow.literalForm(); 
          
          if (v.equals(arglit) && 
              ("2".equals(powlit) ||
               "2.0".equals(powlit))
             )
          { return "1"; } 

          String c = coefficientOf(var,arg); 
          if (c.equals("0") || c.equals("0.0"))
          { return "0"; } 
          else if ("2".equals(powlit) ||
                   "2.0".equals(powlit))
          { return c + "*" + c; } 
        } 

        return "0"; 
      }  

      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 
  
            String coef1 = ASTTerm.coefficientOfSquare(var, tt); 
            if (coef1.equals("0") || coef1.equals("0.0"))
            { return coef1; } 
            return "-(" + coef1 + ")"; 
          } 


          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = ASTTerm.coefficientOfSquare(var, tt); 
            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String coef1 = t1.literalForm(); 
            String coef2 = t2.literalForm();

            Vector vv = new Vector(); 
            vv.add(var); 

            String cnst1 = ASTTerm.constantTerms(vv,t1); 
            String cnst2 = ASTTerm.constantTerms(vv,t2); 
              
            if (v.equals(coef1) && 
                v.equals(coef2))
            { return "1"; } 

            if (vpow.equals(coef2) ||
                vpow2.equals(coef2) ||
                vsqr.equals(coef2))
            { return cnst1; }

            if (vsqrb.equals(coef2) || 
                coef2.equals("(" + vpow + ")") || 
                vpowb.equals(coef2) || 
                coef2.equals("(" + vpow2 + ")") ||
                vpow2b.equals(coef2))
            { return cnst1; } 

            if (vpow.equals(coef1) ||
                vpow2.equals(coef1) ||
                vsqr.equals(coef1))
            { return cnst2; }

            if (vpowb.equals(coef1) ||
                coef1.equals("(" + vpow + ")") || 
                vpow2b.equals(coef1) ||
                coef1.equals("(" + vpow2 + ")") ||
                vsqrb.equals(coef1))
            { return cnst2; }

            String sqcoef1 = 
              ASTTerm.coefficientOfSquare(var, t1);
            String sqcoef2 = 
              ASTTerm.coefficientOfSquare(var, t2);

            coef1 = 
              ASTTerm.coefficientOf(var, t1);
            coef2 = 
              ASTTerm.coefficientOf(var, t2);

            String total = "0"; 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 =
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              total = "" + (val1 * val2); 
            } 
            else 
            { total = "(" + coef1 + ")*(" + coef2 + ")"; } 

            if (AuxMath.isGeneralNumeric(sqcoef1) &&
                AuxMath.isGeneralNumeric(cnst2))
            { double val1 =
                AuxMath.generalNumericValue(sqcoef1); 
              double val2 = 
                AuxMath.generalNumericValue(cnst2);
              total = total + " + " + (val1 * val2); 
            } 
            else 
            { total = total + " + (" + sqcoef1 + ")*(" + cnst2 + ")"; } 

            if (AuxMath.isGeneralNumeric(sqcoef2) &&
                AuxMath.isGeneralNumeric(cnst1))
            { double val1 =
                AuxMath.generalNumericValue(sqcoef2); 
              double val2 = 
                AuxMath.generalNumericValue(cnst1);
              total = total + " + " + (val1 * val2); 
            } 
            else 
            { total = total + " + (" + sqcoef2 + ")*(" + cnst1 + ")"; } 
 
            return total; 
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            boolean isIn1 = ASTTerm.isSubterm(var,t1); 
            boolean isIn2 = ASTTerm.isSubterm(var,t2); 

            String coef1 = t1.literalForm(); 
            if (isIn1 && !isIn2)
            { coef1 = ASTTerm.coefficientOfSquare(var, t1);
              return "(" + coef1 + ") " + opr + " (" + t2.literalForm() + ")"; 
            } 
            return "0";
          } 
          else if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String coef1 = ASTTerm.coefficientOfSquare(var, t1); 
            String coef2 = ASTTerm.coefficientOfSquare(var, t2);
            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = AuxMath.generalNumericValue(coef1); 
              double val2 = AuxMath.generalNumericValue(coef2);
              return "" + (val1 - val2); 
            } 

            return coef1 + " - (" + coef2 + ")"; 
          } 
        } 
      }
    } 
   
    return "0"; 
  }  

  public static String constantTerms(
                         Vector vars, ASTTerm expr)
  { // The parts of expr which do not involve any vars.

    // System.out.println(">>> Constant terms in: " + expr); 

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2)
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.constantTerms(vars, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return ASTTerm.constantTerms(vars, tt); 
        } 
      } 

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("=".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2);  
            String val1 = ASTTerm.constantTerms(vars, t1); 
            String val2 = ASTTerm.constantTerms(vars, t2);
            // val1 - val2

            if ("0".equals(val2))
            { return val1; } 
            if ("0".equals(val1))
            { return "-(" + val2 + ")"; } 

            if (AuxMath.isGeneralNumeric(val1) &&
                AuxMath.isGeneralNumeric(val2))
            { double v1 = AuxMath.generalNumericValue(val1); 
              double v2 = AuxMath.generalNumericValue(val2);
              return "" + (v1 - v2); 
            } 

            return val1 + " - (" + val2 + ")"; 
          }
        } 

        return "0"; 
      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = ASTTerm.constantTerms(vars, t1); 
            String coef2 = ASTTerm.constantTerms(vars, t2); 

            if ("0".equals(coef2))
            { return coef1; }
            if ("0".equals(coef1) && "-".equals(opr))
            { return "-(" + coef2 + ")"; } 
            if ("0".equals(coef1) && "+".equals(opr))
            { return coef2; } 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = 
                 AuxMath.generalNumericValue(coef1); 
              double val2 = 
                 AuxMath.generalNumericValue(coef2);
              if ("+".equals(opr))
              { return "" + (val1 + val2); } 
              else 
              { return "" + (val1 - val2); }   
            } 

            return coef1 + " " + opr + " (" + coef2 + ")"; 
          } 
        } 

        return "0"; 
      }  

      if ("factor2Expression".equals(ct.tag))
      { if (subterms.size() == 4 && 
            "^{".equals(subterms.get(1) + ""))
        { // expr^{power}
          ASTTerm tt = (ASTTerm) subterms.get(0); 
          ASTTerm power = (ASTTerm) subterms.get(2); 
          String powlit = power.literalForm(); 

          String coef1 = ASTTerm.constantTerms(vars, tt); 
          if ("0".equals(coef1))
          { return "0"; } 
          return "(" + coef1 + ")^{" + powlit + "}"; 
        } 
      }           

      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = ASTTerm.constantTerms(vars, tt); 
            
            if ("0".equals(coef1))
            { return "0"; } 
            return "-(" + coef1 + ")"; 
          } 


          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = ASTTerm.constantTerms(vars, tt); 

            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr) || "/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String coef1 = ASTTerm.constantTerms(vars, t1); 
            String coef2 = ASTTerm.constantTerms(vars, t2); 
            if (coef1.equals("0"))
            { return "0"; } 
            if ("*".equals(opr) && "0".equals(coef2))
            { return "0"; } 

            if (AuxMath.isGeneralNumeric(coef1) &&
                AuxMath.isGeneralNumeric(coef2))
            { double val1 = 
                AuxMath.generalNumericValue(coef1); 
              double val2 = 
                AuxMath.generalNumericValue(coef2);
              if ("*".equals(opr))
              { return "" + (val1 * val2); } 
              else 
              { return "" + (val1 / val2); }   
            } 

            return "(" + coef1 + ") " + opr + " (" + coef2 + ")"; 
          } 
        } 
      }
    } 

    String lit = expr.literalForm(); 

    for (int i = 0; i < vars.size(); i++) 
    { ASTTerm var = (ASTTerm) vars.get(i); 
      String v = var.literalForm(); 

      if (",".equals(v)) 
      { continue; } 

      // boolean isIn = ASTTerm.isSubterm(var,expr); 
      if (lit.equals(v))
      { return "0"; }
    } 
   
    return lit; // for numbers only 
  }  

  public static String symbolicEvaluation(ASTTerm e1)
  { String a = ""; 
    String e1lit = e1.literalForm(); 

    if (e1 instanceof ASTSymbolTerm) 
    { return e1lit; } 

    if (e1 instanceof ASTBasicTerm) 
    { return e1lit; } 

    String tg = e1.getTag(); 
    int n = e1.arity(); 

    if (tg.equals("basicExpression"))
    { if (n == 1) 
      { return e1lit; } 

      if (n == 3 && 
          e1lit.startsWith("g{"))
      { return e1lit; } 
    } 

    if (n == 1) 
    { ASTTerm trm = e1.getTerm(0); 
      return ASTTerm.symbolicEvaluation(trm); 
    } 

    if (tg.equals("setExpression"))
    { ASTTerm kind = e1.getTerm(0); 
      ASTTerm elems = e1.getTerm(1); 
      if ("}".equals(elems.literalForm()))
      { return kind.literalForm() + "}"; } 

      Vector elemterms = elems.getTerms(); 
      String res = kind.literalForm(); 

      for (int j = 0; j < elemterms.size(); j++) 
      { ASTTerm et = (ASTTerm) elemterms.get(j); 
        String eval = ASTTerm.symbolicEvaluation(et);
        res = res + eval; 
        if (j < elemterms.size()-1)
        { res = res + ", "; } 
      } 
      return res + "}"; 
    }         

    if (tg.equals("additiveExpression") && 
        n > 2 && 
        "+".equals(e1.getTerm(1) + ""))
    { a = symbolicAddition(e1.getTerm(0), e1.getTerm(2)); } 
    else if (tg.equals("additiveExpression") && 
        n > 2 && 
        "-".equals(e1.getTerm(1) + ""))
    { a = symbolicSubtraction(e1.getTerm(0), e1.getTerm(2)); }
    else if (tg.equals("factorExpression") && 
             "*".equals(e1.getTerm(1) + ""))
    { a = symbolicMultiplication(e1.getTerm(0), e1.getTerm(2)); }
    else if (tg.equals("factorExpression") && 
             "/".equals(e1.getTerm(1) + ""))
    { a = symbolicDivision(e1.getTerm(0), e1.getTerm(2)); }
    else if (tg.equals("factorExpression") && 
             "-".equals(e1.getTerm(0) + ""))
    { a = symbolicNegative(e1.getTerm(1)); } 
    else if (tg.equals("factorExpression") && 
             "+".equals(e1.getTerm(0) + ""))
    { a = symbolicEvaluation(e1.getTerm(1)); } 
    else if (tg.equals("factorExpression") && 
             "†".equals(e1.getTerm(0) + ""))
    { a = symbolicSqrt(e1.getTerm(1)); } 
    else if (tg.equals("factor2Expression") && 
             "^{".equals(e1.getTerm(1) + ""))
    { return symbolicExponentiation(
                e1.getTerm(0), e1.getTerm(2)); 
    } 
    else if (tg.equals("factor2Expression") && 
             "^".equals(e1.getTerm(1) + "") && 
             "{".equals(e1.getTerm(2) + ""))
    { /* JOptionPane.showMessageDialog(null, 
           ">>> Symbolic exponentiation : " + e1.getTerm(0) + "^{" + e1.getTerm(3), 
           "", 
           JOptionPane.INFORMATION_MESSAGE); */ 
 
      return symbolicExponentiation(
                e1.getTerm(0), e1.getTerm(3)); 
    } 
    else if (ASTTerm.isMathOCLBracketed(e1))
    { ASTTerm t1 = ASTTerm.mathOCLContent(e1); 

      String apart = ASTTerm.symbolicEvaluation(t1);
 
      if (AuxMath.isGeneralNumeric(apart))
      { return "" + AuxMath.generalNumericValue(apart); } 
      if (ASTTerm.isMathOCLIdentifier(apart))
      { return apart; }
      a = "(" + apart + ")"; 
    } 
    else 
    { a = e1.evaluationLiteralForm(); }

    if (AuxMath.isGeneralNumeric(a))
    { a = "" + AuxMath.generalNumericValue(a);
      return a; 
    }

    if ("(".equals(a.charAt(0) + "") && 
        ")".equals(a.charAt(a.length()-1) + ""))
    { String apart = a.substring(1,a.length()-1); 
      if (ASTTerm.isMathOCLIdentifier(apart))
      { return apart; } 
    } 

    return a; 
  } 

  public static String symbolicDeterminant(ASTTerm m)
  { // convert m to a Vector of Vector of numbers
    // and apply AuxMath.determinant

    Vector mrows = ASTTerm.mathOCLsequenceElements(m);
    return "" + AuxMath.determinant(mrows.size(), mrows);  
  } 

  public static Vector mathOCLsequenceElements(ASTTerm t)
  { String tg = t.getTag();
    Vector res = new Vector(); 
 
    if (tg.equals("setExpression"))
    { ASTTerm elems = t.getTerm(1); 
      if ("}".equals(elems.literalForm()))
      { return new Vector(); } 

      Vector elemterms = elems.getTerms(); 
      
      for (int j = 0; j < elemterms.size(); j++) 
      { ASTTerm et = (ASTTerm) elemterms.get(j); 
        String eval = ASTTerm.symbolicEvaluation(et);
        res.add(AuxMath.generalNumericValue(eval)); 
      } // but for matrix, do recursively

      return res; 
    } 

    Vector trms = t.getTerms(); 
    if (trms.size() == 1)
    { ASTTerm elems = (ASTTerm) trms.get(0); 
      return ASTTerm.mathOCLsequenceElements(elems); 
    } 
  
    return res; 
  } 

  public static String symbolicLess(ASTTerm e1, ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    if (e2.getTag().equals("additiveExpression") && 
        "+".equals(e2.getTerm(1) + ""))
    { ASTTerm t1 = e2.getTerm(0); 
      ASTTerm t2 = e2.getTerm(2); 
      // t2 or t1 is a +ve real and other is e1: 

      String b1 = t1.literalForm(); 
      String b2 = t2.literalForm(); 
  
      if (a.equals(b1) && AuxMath.isGeneralNumeric(b2))
      { double r2 = AuxMath.generalNumericValue("" + b2); 
        if (r2 > 0)
        { return "true"; } 
        return "false"; 
      } 

      if (a.equals(b2) && AuxMath.isGeneralNumeric(b1))
      { double r1 = AuxMath.generalNumericValue("" + b1); 
        if (r1 > 0)
        { return "true"; } 
        return "false"; 
      }
 
      b = symbolicAddition(t1,t2); 
    } 

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue("" + a); 
      double bval = AuxMath.generalNumericValue("" + b); 
      if (aval < bval) 
      { return "true"; } 
      return "false"; 
    }
    
    return a + " < " + b; 
  }  

  public static String symbolicLeq(ASTTerm e1, ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    if (e2.getTag().equals("additiveExpression") && 
        "+".equals(e2.getTerm(1) + ""))
    { ASTTerm t1 = e2.getTerm(0); 
      ASTTerm t2 = e2.getTerm(2); 
      // t2 or t1 is a +ve real and other is e1: 

      String b1 = t1.literalForm(); 
      String b2 = t2.literalForm(); 
  
      if (a.equals(b1) && AuxMath.isGeneralNumeric(b2))
      { double r2 = AuxMath.generalNumericValue("" + b2); 
        if (r2 >= 0)
        { return "true"; } 
        return "false"; 
      } 

      if (a.equals(b2) && AuxMath.isGeneralNumeric(b1))
      { double r1 = AuxMath.generalNumericValue("" + b1); 
        if (r1 >= 0)
        { return "true"; } 
        return "false"; 
      }
 
      b = symbolicAddition(t1,t2); 
    } 

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue("" + a); 
      double bval = AuxMath.generalNumericValue("" + b); 
      if (aval <= bval) 
      { return "true"; } 
      return "false"; 
    }
    
    return a + " <= " + b; 
  }  

  public static String symbolicAddition(ASTTerm e1, ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    if (ASTTerm.isMathOCLSubtraction(e1))
    { ASTTerm add2 = ASTTerm.mathOCLArgument(e1,2);
      String add2val = symbolicEvaluation(add2); 
      if (add2val.equals(b))
      { ASTTerm add1 = ASTTerm.mathOCLArgument(e1,0); 
        return ASTTerm.symbolicEvaluation(add1); 
      }
    }  // (a - b) + b |-->a

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue(a); 
      double bval = AuxMath.generalNumericValue(b); 
      return "" + (aval+bval); 
    }

    if (AuxMath.isGeneralNumeric(a))
    { a = "" + AuxMath.generalNumericValue(a); }

    if (AuxMath.isGeneralNumeric(b))
    { b = "" + AuxMath.generalNumericValue(b); }

    // Group by powers of x?

    if (a.equals("?") || b.equals("?"))
    { return "?"; }  

    if (a.equals("0") || a.equals("0.0"))
    { return b; } 

    if (b.equals("0") || b.equals("0.0"))
    { return a; }  

    if (a.equals("…") && b.equals("…"))
    { return a; }  

    if (a.equals("-…") && b.equals("…"))
    { return "?"; }  

    if (a.equals("…") && b.equals("-…"))
    { return "?"; }  

    if (a.equals("…") || b.equals("…"))
    { return "…"; }  
    
    if (a.equals("-…") || b.equals("-…"))
    { return "-…"; }  

    return a + " + " + b; 
  }  
     
  public static String symbolicSubtraction(ASTTerm e1, ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    boolean bneedsBracket = ASTTerm.mathOCLNeedsBracket(e2); 

    if (ASTTerm.isMathOCLAddition(e2))
    { ASTTerm add1 = ASTTerm.mathOCLArgument(e2,0); 
      ASTTerm add2 = ASTTerm.mathOCLArgument(e2,2);
       
      // e1 - (add1 + add2)
      a = 
          ASTTerm.symbolicSubtraction(e1,add1); 
      b = symbolicEvaluation(add2);
      bneedsBracket = ASTTerm.mathOCLNeedsBracket(add2);
    } 
    else if (ASTTerm.isMathOCLSubtraction(e2)) 
    { ASTTerm add1 = ASTTerm.mathOCLArgument(e2,0); 
      ASTTerm add2 = ASTTerm.mathOCLArgument(e2,2);
      // e1 - (add1 - add2)
      a = 
        ASTTerm.symbolicAddition(e1,add2); 
      b = symbolicEvaluation(add1); 
      bneedsBracket = ASTTerm.mathOCLNeedsBracket(add1);
    }  
    else if (ASTTerm.isMathOCLAddition(e1))
    { ASTTerm add2 = ASTTerm.mathOCLArgument(e1,2);
      String add2val = symbolicEvaluation(add2); 
      if (add2val.equals(b))
      { ASTTerm add1 = ASTTerm.mathOCLArgument(e1,0); 
        return ASTTerm.symbolicEvaluation(add1); 
      }
    }  
    // Case of (A+B)-B |-->A
      
    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue(a); 
      double bval = AuxMath.generalNumericValue(b); 
      return "" + (aval-bval); 
    }

    if (AuxMath.isGeneralNumeric(a))
    { a = "" + AuxMath.generalNumericValue(a); }

    if (AuxMath.isGeneralNumeric(b))
    { double bval = AuxMath.generalNumericValue(b);
      /* if (bval < 0) 
      { double b1 = -bval; 
        return a + " + " + b1; 
      } */ 

      b = "" + bval; 
    }

    if (b.equals(a))
    { return "0"; }  

    if (a.equals("0") || a.equals("0.0"))
    { if (ASTTerm.isMathOCLIdentifier(b) ||
          AuxMath.isGeneralNumeric(b))
      { return "-" + b; } 
      return "-(" + b + ")"; 
    } 

    if (b.equals("0") || b.equals("0.0"))
    { return a; }  

    if (a.equals("…") && b.equals("…"))
    { return "?"; }  

    if (a.equals("-…") && b.equals("…"))
    { return "-…"; }  

    if (a.equals("…") && b.equals("-…"))
    { return "…"; }  

    if (a.equals("-…") && b.equals("-…"))
    { return "?"; }  
    
    if (a.equals("…") || b.equals("-…"))
    { return "…"; }  

    if (a.equals("-…") || b.equals("…"))
    { return "-…"; }  

    if (ASTTerm.isMathOCLIdentifier(b) || 
        AuxMath.isGeneralNumeric(b))
    { bneedsBracket = false; } 
    
    if (bneedsBracket)
    { return a + " - (" + b + ")"; } 
    return a + " - " + b; 
  }  
     
  public static String symbolicMultiplication(ASTTerm e1, ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 


    // a/c * c is a: 
    if (ASTTerm.isMathOCLDivision(e1))
    { ASTTerm numer = ASTTerm.mathOCLNumerator(e1);   
      ASTTerm denom = ASTTerm.mathOCLDenominator(e1);
 
      if (b.equals(denom.literalForm()))
      { return numer.literalFormSpaces(); }

      a = ASTTerm.symbolicDivision(numer,denom);  
    } 

    // a*c/a is c: 

    if (ASTTerm.isMathOCLDivision(e2))
    { ASTTerm numer = ASTTerm.mathOCLNumerator(e2);   
      ASTTerm denom = ASTTerm.mathOCLDenominator(e2);
 
      if (a.equals(denom.literalForm()))
      { return numer.literalFormSpaces(); }

      b = ASTTerm.symbolicDivision(numer,denom);  
    } 

    if (ASTTerm.isMathOCLBracketed(e1))
    { ASTTerm parg = ASTTerm.mathOCLContent(e1); 
      return "(" + symbolicMultiplication(parg,e2) + ")";
    }
    else if (ASTTerm.isMathOCLBracketed(e2))
    { ASTTerm qarg = ASTTerm.mathOCLContent(e2); 
      return "(" + symbolicMultiplication(e1,qarg) + ")";
    } 

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue(a); 
      double bval = AuxMath.generalNumericValue(b); 
      return "" + (aval*bval); 
    }

    // Group by powers of x?

    if (AuxMath.isGeneralNumeric(a))
    { double aval = AuxMath.generalNumericValue(a);
      a = "" + aval; 

      if (aval > 0 && b.equals("…"))
      { return b; } 
      if (aval < 0 && b.equals("…"))
      { return "-…"; } 
      if (aval < 0 && b.equals("-…"))
      { return "…"; } 
      if (aval > 0 && b.equals("-…"))
      { return "-…"; } 
    } 

    if (AuxMath.isGeneralNumeric(b))
    { double bval = AuxMath.generalNumericValue(b); 

      b = "" + bval; 
 
      if (bval > 0 && a.equals("…"))
      { return a; } 
      if (bval < 0 && a.equals("…"))
      { return "-…"; } 
      if (bval < 0 && a.equals("-…"))
      { return "…"; } 
      if (bval > 0 && a.equals("-…"))
      { return "-…"; } 
    } 

    if (a.equals("?") || b.equals("?"))
    { return "?"; }  

    if (b.equals("1") || b.equals("1.0") ||
        b.equals("(1)") || b.equals("(1.0)"))
    { return a; }  

    if (a.equals("1") || a.equals("1.0") ||
        a.equals("(1)") || a.equals("(1.0)"))
    { return b; }  

    if (a.equals("…") && b.equals("…"))
    { return a; }  

    if (a.equals("-…") && b.equals("-…"))
    { return "…"; }  

    if (a.equals("…") && b.equals("-…"))
    { return b; }  

    if (a.equals("-…") && b.equals("…"))
    { return a; }  

    if ((a.equals("…") || a.equals("-…")) && b.equals("0"))
    { return "?"; }  

    if ((b.equals("…") || b.equals("-…")) && a.equals("0"))
    { return "?"; }  

    if (a.equals("0") || a.equals("0.0") || 
        b.equals("0") || b.equals("0.0"))
    { return "0"; } 

    String abrack = a; 
    String bbrack = b; 

    if (AuxMath.isNumeric(a) || 
        ASTTerm.isMathOCLIdentifier(a))
    { } 
    else if (ASTTerm.mathOCLNeedsBracket(e1))
    { abrack = "(" + a + ")"; } 

    if (AuxMath.isNumeric(b) || 
        ASTTerm.isMathOCLIdentifier(b))
    { } 
    else if (ASTTerm.mathOCLNeedsBracket(e2))
    { bbrack = "(" + b + ")"; } 

 
    return abrack + "*" + bbrack; 
  }  

  public static String symbolicNegateMultiplication(
                              ASTTerm e1, 
                              ASTTerm e2)
  { String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2);

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue(a); 
      double bval = AuxMath.generalNumericValue(b); 
      return "" + (-aval*bval); 
    }

    if (AuxMath.isGeneralNumeric(a))
    { a = "" + AuxMath.generalNumericValue(a); } 

    if (AuxMath.isGeneralNumeric(b))
    { b = "" + AuxMath.generalNumericValue(b); } 

    if ("0".equals(a) || a.equals("0.0") || 
        "0".equals(b) || b.equals("0.0")) 
    { return "0"; } 

    if ("1".equals(a) || a.equals("1.0")) 
    { return "-" + b; } 

    if ("1".equals(b) || b.equals("1.0")) 
    { return "-" + a; } 
    
    return "-" + a + "*" + b; 
  }  
     
  
  public static String symbolicNegative(ASTTerm e1) 
  { // for -x 

    if ("factorExpression".equals(e1.getTag()) && 
        "-".equals(e1.getTerm(0) + ""))
    { ASTTerm trm = e1.getTerm(1); 
      return ASTTerm.symbolicEvaluation(trm); 
    } 

    String a = ASTTerm.symbolicEvaluation(e1); 

    if (AuxMath.isGeneralNumeric(a))
    { double aval = AuxMath.generalNumericValue(a); 
      return "" + (-aval); 
    }

    if (a.equals("0") || a.equals("0.0"))
    { return "0"; } 
    
    return "-" + a; 
  }  


  public static String symbolicSqrt(ASTTerm e1) 
  { // for †expr 

    if ("factorExpression".equals(e1.getTag()) && 
        "*".equals(e1.getTerm(1) + "") && 
        e1.getTerm(0).equals(e1.getTerm(2)))
    { return ASTTerm.symbolicEvaluation(e1.getTerm(0)); } 

    String a = ASTTerm.symbolicEvaluation(e1); 

    if (a.equals("?"))
    { return "?"; }  


    if (AuxMath.isPerfectSquare(a))
    { double aval = AuxMath.generalNumericValue(a); 
      return "" + Math.sqrt(aval); 
    }

    if (a.equals("0") || a.equals("0.0"))
    { return "0"; } 

    if (a.equals("1") || a.equals("1.0"))
    { return "1"; } 

    if (a.equals("…"))
    { return a; }  

    if (a.equals("-…"))
    { return "?"; }  
    
    return "†" + a; 
  } 

  public static String symbolicExponentiation(ASTTerm e1, 
                                              ASTTerm e2) 
  { // for e1^{e2}

    String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    if (a.equals("?") || b.equals("?"))
    { return "?"; }  

    if (b.equals("0") || b.equals("0.0"))
    { return "1"; } 

    if (a.equals("1") || a.equals("1.0"))
    { return "1"; } 

    if (b.equals("1") || b.equals("1.0"))
    { return a; } 
    
    return a + "^{" + b + "}"; 
  } 
    
  public static String symbolicDivision(ASTTerm e1, ASTTerm e2)
  { if (ASTTerm.isMathOCLNegative(e1) && 
        ASTTerm.isMathOCLNegative(e2))
    { ASTTerm t1 = ASTTerm.mathOCLArgument(e1,1); 
      ASTTerm t2 = ASTTerm.mathOCLArgument(e2,1);
      return symbolicDivision(t1,t2); 
    } 
 
    String a = ASTTerm.symbolicEvaluation(e1); 
    String b = ASTTerm.symbolicEvaluation(e2); 

    if (AuxMath.isGeneralNumeric(a) && 
        AuxMath.isGeneralNumeric(b))
    { double aval = AuxMath.generalNumericValue(a); 
      double bval = AuxMath.generalNumericValue(b); 
      if (bval != 0)
      { return "" + (aval/bval); } 
      return aval + "/0"; 
    }

    // Group by powers of x? 

    if (AuxMath.isGeneralNumeric(a))
    { a = "" + AuxMath.generalNumericValue(a); 
      if ("…".equals(b) || "(…)".equals(b) || 
          "-…".equals(b) || "(-…)".equals(b))
      { return "0"; } 
    }  

    if (AuxMath.isGeneralNumeric(b))
    { b = "" + AuxMath.generalNumericValue(b); }  

    if (a.equals("?") || b.equals("?"))
    { return "?"; }  

    if (a.equals("0") || a.equals("0.0"))
    { if (b.equals("0") || b.equals("0.0"))
      { return "?"; } 
      return "0"; 
    } 

    if ((a.equals("…") || a.equals("…")) && 
        (b.equals("-…") || b.equals("…")))
    { return "?"; }  

    if ((a.equals("…") || a.equals("-…")) && b.equals("0"))
    { return a; }  

    if (b.equals("1") || b.equals("1.0"))
    { return a; }  

    if (a.equals(b))
    { return "1"; } 

    if (b.equals("-1") || b.equals("-1.0"))
    { if (ASTTerm.isMathOCLIdentifier(a) || 
          AuxMath.isGeneralNumeric(a))
      { return "-" + a; } 
      return "-(" + a + ")"; 
    }  

    if (a.equals("1") || a.equals("1.0"))
    { return "1/" + b; }  
    
    if (ASTTerm.mathOCLNeedsBracket(e2))
    { return a + "/(" + b + ")"; } 

    return a + "/" + b; 
  }  


  public static String symbolicMultiplication(
                            String var, ASTTerm expr)
  { // result is var*expr

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.symbolicMultiplication(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return "(" + 
            ASTTerm.symbolicMultiplication(var, tt) + ")"; 
        } 
      } // var * (expr) is (var*expr)

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          ASTTerm t1 = (ASTTerm) subterms.get(0);  
          ASTTerm t2 = (ASTTerm) subterms.get(2);  
          String m1 = ASTTerm.symbolicMultiplication(var,t1); 
          String m2 = ASTTerm.symbolicMultiplication(var,t2);
          return m1 + " " + opr + " " + m2;  
        }  
        
        if (subterms.size() == 2) 
        { String opr = subterms.get(0) + ""; 
          ASTTerm t1 = (ASTTerm) subterms.get(1);  
          String m1 = ASTTerm.symbolicMultiplication(var,t1); 
          return opr + " " + m1;  
        }  // for -expr, +expr

      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = ASTTerm.symbolicMultiplication(var, t1); 
            String coef2 = ASTTerm.symbolicMultiplication(var, t2); 

            if ("+".equals(opr))
            { return coef1 + " " + opr + " " + coef2; } 
            return coef1 + " " + opr + " (" + coef2 + ")"; 
          } 
        } // var * a+b is var*a + var*b
      }  

      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = ASTTerm.symbolicMultiplication(var, tt); 
            return "-(" + coef1 + ")"; 
          } 

          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = ASTTerm.symbolicMultiplication(var, tt); 
            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String coef1 = ASTTerm.symbolicMultiplication(var, t1);
            String mult2 = t2.literalForm();

            if ("0".equals(coef1) || 
                "0.0".equals(coef1)) 
            { return "0"; } 
            if ("1".equals(coef1) || 
                "1.0".equals(coef1)) 
            { return mult2; } 
            if ("0".equals(mult2) ||
                "0.0".equals(mult2))
            { return "0"; } 
            if ("1".equals(mult2) || 
                "1.0".equals(mult2)) 
            { return coef1; } 
            
            return "(" + coef1 + ")*(" + mult2 + ")"; 
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String coef2 = t2.literalForm(); 
            if (var.equals(coef2))
            { return t1.literalForm(); }

            String coef1 = t1.symbolicMultiplication(var,t1); 

            if ("0".equals(coef1) || 
                "0.0".equals(coef1)) 
            { return "0"; } 
            if ("1".equals(coef2) || 
                "1.0".equals(coef2)) 
            { return coef1; } 
            return "(" + coef1 + ")" + opr + "(" + coef2 + ")"; 
          } 
        } 
      }
    } 
   
    String ee = expr.literalFormSpaces(); 
    if ("0".equals(ee) || "0.0".equals(ee)) 
    { return "0"; } 
    return var + "*(" + ee + ")"; 
  }  

  public static String symbolicPostMultiplication(
                            String var, ASTTerm expr)
  { // result is expr*var

    if (expr instanceof ASTCompositeTerm)
    { // (factorExpression _1 * _2), etc
      
      ASTCompositeTerm ct = (ASTCompositeTerm) expr;
      Vector subterms = ct.getTerms(); 
 
      if (subterms.size() == 1) 
      { return ASTTerm.symbolicPostMultiplication(var, 
                                (ASTTerm) subterms.get(0)); 
      } 

      if ("basicExpression".equals(ct.tag))
      { if (subterms.size() == 3 && 
            "(".equals(subterms.get(0) + "") && 
            ")".equals(subterms.get(2) + ""))
        { ASTTerm tt = (ASTTerm) subterms.get(1); 
          return "(" + 
            ASTTerm.symbolicPostMultiplication(var, tt) + ")"; 
        } 
      } // var * (expr) is (var*expr)

      if ("logicalExpression".equals(ct.tag) || 
          "equalityExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          ASTTerm t1 = (ASTTerm) subterms.get(0);  
          ASTTerm t2 = (ASTTerm) subterms.get(2);  
          String m1 =
            ASTTerm.symbolicPostMultiplication(var,t1); 
          String m2 = 
            ASTTerm.symbolicPostMultiplication(var,t2);
          return m1 + " " + opr + " " + m2;  
        }  
        
        if (subterms.size() == 2) 
        { String opr = subterms.get(0) + ""; 
          ASTTerm t1 = (ASTTerm) subterms.get(1);  
          String m1 = 
             ASTTerm.symbolicPostMultiplication(var,t1);

          if ("+".equals(opr))
          { return m1; }  
          return opr + " " + m1;  
        }  // for -expr, +expr

      }  

      if ("additiveExpression".equals(ct.tag))
      { if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 
          if ("+".equals(opr) || "-".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 
            String coef1 = 
              ASTTerm.symbolicPostMultiplication(var, t1); 
            String coef2 = 
              ASTTerm.symbolicPostMultiplication(var, t2); 

            if ("+".equals(opr))
            { return coef1 + " " + opr + " " + coef2; }
            if (mathOCLNeedsBracket(t2)) 
            { return coef1 + " " + opr + " (" + coef2 + ")"; } 
            return coef1 + " " + opr + " " + coef2; 
          } 
        } // var * a+b is var*a + var*b
      }  

      if ("factorExpression".equals(ct.tag))
      { if (subterms.size() == 2)
        { String opr = subterms.get(0) + ""; 

          if ("-".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = 
              ASTTerm.symbolicPostMultiplication(var, tt); 
            if (mathOCLNeedsBracket(tt))
            { return "-(" + coef1 + ")"; } 
            return "-" + coef1; 
          } 

          if ("+".equals(opr))
          { ASTTerm tt = (ASTTerm) subterms.get(1); 

            String coef1 = 
              ASTTerm.symbolicPostMultiplication(var, tt); 
            return coef1; 
          } 
        }

        if (subterms.size() == 3)
        { String opr = subterms.get(1) + ""; 

          if ("*".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String mult2 = 
              ASTTerm.symbolicPostMultiplication(var, t2);
            String coef1 = t1.literalForm();

            if ("0".equals(coef1) || 
                "0.0".equals(coef1)) 
            { return "0"; } 
            if ("1".equals(coef1) || 
                "1.0".equals(coef1)) 
            { return mult2; } 
            if ("0".equals(mult2) ||
                "0.0".equals(mult2))
            { return "0"; } 
            if ("1".equals(mult2) || 
                "1.0".equals(mult2)) 
            { return coef1; } 
            
            return "(" + coef1 + ")*(" + mult2 + ")"; 
          } 
          else if ("/".equals(opr))
          { ASTTerm t1 = (ASTTerm) subterms.get(0);  
            ASTTerm t2 = (ASTTerm) subterms.get(2); 

            String coef2 = t2.literalForm(); 
            if (var.equals(coef2))
            { return t1.literalForm(); }

            String coef1 = 
              ASTTerm.symbolicPostMultiplication(var,t1); 

            if ("0".equals(coef1) || 
                "0.0".equals(coef1)) 
            { return "0"; } 
            if ("1".equals(coef2) || 
                "1.0".equals(coef2)) 
            { return coef1; } 
            return "(" + coef1 + ")/" + coef2; 
          } 
        } 
      }
    } 
   
    String ee = expr.literalFormSpaces(); 
    if ("0".equals(ee) || "0.0".equals(ee)) 
    { return "0"; } 
    return "(" + ee + ")*" + var; 
  }  

  public static String groupTermsByPower(ASTTerm expr, ASTTerm var)
  { // Assume that it is in plain polynomial form, no products
    // of polynomials. 

    Vector powers = ASTTerm.powersOf(var,expr);
    Vector vars = new Vector();
    vars.add(var);  

    //  double minp = VectorUtil.vectorMinimum(powers); 
    double maxp = VectorUtil.vectorMaximum(powers); 

    if (maxp <= 0.0)
    { return expr.literalForm(); } 

    Vector coefs = new Vector(); 
    Vector pows = ASTTerm.constructNPowers(
                                powers, var, expr, 
                                coefs);

    String res = ""; 
    String cnsts = 
              ASTTerm.constantTerms(vars,expr);
        
    for (int i = 0; i < pows.size(); i++) 
    { ASTTerm powi = (ASTTerm) pows.get(i); 
      res = res + 
            "(" + coefs.get(i) + ")*" + powi.literalForm() + " + "; 
    } 
  
    return res + cnsts; 
  } // Normalises a polynomial

  public static String expressAsPolynomial(ASTTerm trm, ASTTerm var)
  { if (ASTTerm.isMathOCLDivision(trm))
    { ASTTerm num = ASTTerm.mathOCLNumerator(trm); 
      ASTTerm den = ASTTerm.mathOCLDenominator(trm); 
      return ASTTerm.polynomialDivision(num,den,var); 
    } 
    else if (ASTTerm.isMathOCLMultiplication(trm))
    { ASTTerm num = ASTTerm.mathOCLArgument(trm,0); 
      ASTTerm den = ASTTerm.mathOCLArgument(trm,2); 
      return ASTTerm.polynomialMultiplication(num,den,var); 
    } 

    return trm.literalFormSpaces(); 
  } // and multiplication

  public static String polynomialMultiplication(ASTTerm p, ASTTerm q, ASTTerm var)
  { // result = p*q with terms expressed as coef*var^{pow}

    String ptg = p.getTag();
    Vector pargs = p.getTerms(); 

    String qtg = q.getTag();
    Vector qargs = q.getTerms();

    String v0 = var.literalForm();  

    String plit = p.literalForm(); 
    String qlit = q.literalForm(); 

    /* JOptionPane.showMessageDialog(null, 
        "Polynomial multiplication of " + plit + " and " + qlit,   "",         
        JOptionPane.INFORMATION_MESSAGE); */ 

    if ("1".equals(plit)) { return qlit; } 
    if ("1".equals(qlit)) { return plit; } 

    if (ASTTerm.isMathOCLBracketed(p))
    { ASTTerm parg = ASTTerm.mathOCLContent(p); 
      return "(" + polynomialMultiplication(parg,q,var) + ")";
    }
    else if (ASTTerm.isMathOCLBracketed(q))
    { ASTTerm qarg = ASTTerm.mathOCLContent(q); 
      return "(" + polynomialMultiplication(p,qarg,var) + ")";
    } 
    else if (ptg.equals("additiveExpression") && 
        pargs.size() == 3) 
    { ASTTerm parg1 = (ASTTerm) pargs.get(0); 
      ASTTerm parg2 = (ASTTerm) pargs.get(2); 
      String f1 = polynomialMultiplication(parg1,q,var); 
      String f2 = polynomialMultiplication(parg2,q,var);
 
      String opr = "" + pargs.get(1); 
      if ("+".equals(opr))
      { return f1 + " " + opr + " " + f2; }
      return f1 + " " + opr + " (" + f2 + ")";  
    } 
    else if (qtg.equals("additiveExpression") && 
        qargs.size() == 3) 
    { ASTTerm qarg1 = (ASTTerm) qargs.get(0); 
      ASTTerm qarg2 = (ASTTerm) qargs.get(2); 
      String f1 = polynomialMultiplication(p,qarg1,var); 
      String f2 = polynomialMultiplication(p,qarg2,var); 

      String opr = "" + qargs.get(1); 
      if ("+".equals(opr))
      { return f1 + " " + opr + " " + f2; }
      return f1 + " " + opr + " (" + f2 + ")";   
    }
    else if (ASTTerm.isMathOCLMultiplication(p))
    { ASTTerm parg1 = ASTTerm.mathOCLArgument(p,0); 
      ASTTerm parg2 = ASTTerm.mathOCLArgument(p,2);
      String p1lit = parg1.literalForm(); 

      if (v0.equals(p1lit))
      { String mult = polynomialMultiplication(parg2,q,var); 
        return "(" + mult + ")*" + v0; 
      } 
      else if (v0.equals(parg2.literalForm()))
      { String mult = polynomialMultiplication(parg1,q,var); 
        return "(" + mult + ")*" + v0; 
      }

      return p1lit + "*" + polynomialMultiplication(parg2,q,var);
    }
    else if (ASTTerm.isMathOCLMultiplication(q))
    { ASTTerm qarg1 = ASTTerm.mathOCLArgument(q,0);
      ASTTerm qarg2 = ASTTerm.mathOCLArgument(q,2); 
      String q1lit = qarg1.literalForm(); 

      if (v0.equals(q1lit))
      { String mult = polynomialMultiplication(p,qarg2,var); 
        return "(" + mult + ")*" + v0; 
      } 
      else if (v0.equals(qarg2.literalForm()))
      { String mult = polynomialMultiplication(p,qarg1,var); 
        return "(" + mult + ")*" + v0; 
      }

      return q1lit + "*" + polynomialMultiplication(p,qarg2,var);
    }
    else if (v0.equals(p.literalForm()))
    { return symbolicEvaluation(q) + "*" + v0; } 
    else if (v0.equals(q.literalForm()))
    { return p.literalForm() + "*" + v0; } 
    else if (pargs.size() == 1) 
    { return ASTTerm.polynomialMultiplication( 
                                (ASTTerm) pargs.get(0),q,var); 
    } 
    else if (qargs.size() == 1) 
    { return ASTTerm.polynomialMultiplication( 
                             p, (ASTTerm) qargs.get(0),var); 
    } 

    return symbolicMultiplication(p,q); 
  } 

  public static String polynomialDivision(ASTTerm p, ASTTerm q, ASTTerm var)
  { // P = (cP/cQ)*(x^{n})*Q + R where n is maxp - maxq
    // cP is coefP of maxp, cQ is coefQ of maxq
    // So P/Q = (cP/cQ)*(x^{n}) + R/Q

    Vector powersP = ASTTerm.powersOf(var,p);
    Vector powersQ = ASTTerm.powersOf(var,q);
    
    Vector vars = new Vector();
    vars.add(var);  

    String v0 = var.literalForm(); 


    //  double minp = VectorUtil.vectorMinimum(powers); 
    double maxp = VectorUtil.vectorMaximum(powersP); 
    double maxq = VectorUtil.vectorMaximum(powersQ); 

    if (maxp < maxq)
    { return "(" + p.literalFormSpaces() + ")/(" + q.literalFormSpaces() + ")"; } 

    int n = (int) (maxp - maxq); 

    Vector coefsP = new Vector(); 
    Vector powsP = ASTTerm.constructAllPowers((int) maxp,
                                powersP, var, p, 
                                coefsP);

    /* JOptionPane.showMessageDialog(null, 
        "powers of " + v0 + " in " + p + " are " + powsP + " coefficients: " + coefsP,   "",         
        JOptionPane.INFORMATION_MESSAGE); */ 

    Vector coefsQ = new Vector(); 
    Vector powsQ = ASTTerm.constructAllPowers((int) maxq,
                                powersQ, var, q, 
                                coefsQ);

  /*  JOptionPane.showMessageDialog(null, 
        "powers of " + v0 + " in " + q + " are " + powsQ + " coefficients: " + coefsQ,   "",         
        JOptionPane.INFORMATION_MESSAGE); */ 

    ASTTerm maxPpower = (ASTTerm) powsP.get(0);  
      // ASTTerm.constructNPower(((int) maxp) + "", var);

    String maxPcoef = (String) coefsP.get(0); 
      // ASTTerm.coefficientOf(maxPpower,p); 

    ASTTerm maxQpower = (ASTTerm) powsQ.get(0); 
      // ASTTerm.constructNPower(((int) maxq) + "", var);

    String maxQcoef = (String) coefsQ.get(0);  
      // ASTTerm.coefficientOf(maxQpower,q); 

    String mfactor = "((" + maxPcoef + ")/(" + maxQcoef + "))"; 
    if (maxPcoef.equals(maxQcoef))
    { mfactor = "1"; } 
    else if (maxQcoef.equals("1") || maxQcoef.equals("1.0"))
    { mfactor = maxPcoef; } 

    String res = mfactor + "*" + v0 + "^{" + n + "}"; 
    if (n == 0) 
    { res = mfactor; } 
    else if (n == 1) 
    { res = mfactor + "*" + v0; } 
    else if (n == 2) 
    { res = mfactor + "*" + v0 + "*" + v0; } 

    // String cnstsP = 
    //           ASTTerm.constantTerms(vars,p);

    // String cnstsQ = 
    //           ASTTerm.constantTerms(vars,q);

    // coefficient of x^m in R is 
    // coefP(m) - mfactor*coefQ(m - n)
        
    String r = "0"; 

    for (int i = 1; i < powsP.size(); i++) 
    { // powers are in descending order
      ASTTerm powi = (ASTTerm) powsP.get(i);
 
      if (i < coefsQ.size())
      { String coefQ = (String) coefsQ.get(i);

        if ("0".equals(coefQ))
        { r = r +  
            " + (" + coefsP.get(i) + ")*" + 
            powi.literalForm();
        }
        else  
        { r = r +  
            " + (" + coefsP.get(i) + " - " + mfactor + "*" +
                coefQ + ")*" + 
            powi.literalForm();
        } 
      } 
      else 
      { r = r +  
            " + (" + coefsP.get(i) + ")*" + 
            powi.literalForm();
      } 
    } 
  
    return res + " + (" + r + ")/(" + 
           q.literalFormSpaces() + ")"; 
  } 

  public static boolean mathOCLNeedsBracket(ASTTerm trm)
  { if (trm instanceof ASTBasicTerm) 
    { return false; } 

    if (trm instanceof ASTSymbolTerm) 
    { return false; } 

    if (ASTTerm.isMathOCLBracketed(trm))
    { return false; } 

    String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg) && 
        args.size() == 3 && 
        "(".equals(args.get(1) + "") && 
        ")".equals(args.get(2) + ""))
    { return false; } // expr()

    if ("basicExpression".equals(tg) && 
        args.size() == 3 && 
        "g{".equals(args.get(0) + "") && 
        "}".equals(args.get(2) + ""))
    { return false; } 

    if ("basicExpression".equals(tg) && 
        args.size() == 4 && 
        "(".equals(args.get(1) + "") && 
        ")".equals(args.get(3) + ""))
    { return false; } // expr(args)

    String lit = trm.literalForm(); 

    if (AuxMath.isGeneralNumeric(lit))
    { return false; } 

    if (ASTTerm.isMathOCLIdentifier(lit))
    { return false; } 

    if (args.size() == 1)
    { return ASTTerm.mathOCLNeedsBracket(
                       (ASTTerm) args.get(0)); 
    } 

    return true; 
  } 

  public static boolean isMathOCLBracketed(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg) && 
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { return true; } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLBracketed(arg0); 
    } 

    return false; 
  } 

  public static ASTTerm mathOCLContent(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if (trm instanceof ASTBasicTerm) 
    { return trm; } 

    if (trm instanceof ASTSymbolTerm) 
    { return trm; } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm arg = (ASTTerm) args.get(1); 
      return ASTTerm.mathOCLContent(arg); 
    } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLContent(arg0); 
    }

    return trm; 
  } 

  public static boolean isMathOCLConjunction(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("expressionList".equals(tg) && 
        args.size() > 1) 
    { return true; } 

    if ("expressionList".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLConjunction(arg0); 
    } 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "&".equals(args.get(1) + ""))
    { return true; } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 2 && 
        "not".equals(args.get(0) + "") && 
        ASTTerm.isMathOCLDisjunction((ASTTerm) args.get(1)))
    { return true; }  // and at least 2 disjuncts

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { return true; } 


    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { return true; } // a/b < c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { return true; } // a < b/c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { return true; } // a/b > c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { return true; } // a > b/c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { return true; } // a*b < 0

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(0)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(2)))
    { return true; } // 0 < a*b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "=".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { return true; } // a*b = 0

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLConjunction(arg0); 
    } 

    return false; 
  } 

  public static Vector mathOCLConjuncts(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("expressionList".equals(tg)) 
    { Vector res = new Vector(); 
      for (int i = 0; i < args.size(); i++) 
      { ASTTerm trmi = (ASTTerm) args.get(i); 
        if (trmi instanceof ASTSymbolTerm) { } 
        else 
        { Vector conjs = ASTTerm.mathOCLConjuncts(trmi);
          res.addAll(conjs); 
        } 
      }
      return res; 
    } 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "&".equals(args.get(1) + ""))
    { Vector res = new Vector(); 
      ASTTerm t1 = (ASTTerm) args.get(0); 
      ASTTerm t2 = (ASTTerm) args.get(2);
      res.addAll(ASTTerm.mathOCLConjuncts(t1)); 
      res.addAll(ASTTerm.mathOCLConjuncts(t2)); 
 
      return res; 
    } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 2 && 
        "not".equals(args.get(0) + "") && 
        ASTTerm.isMathOCLDisjunction((ASTTerm) args.get(1)))
    { ASTTerm conj = (ASTTerm) args.get(1); 
      Vector conjs = ASTTerm.mathOCLDisjuncts(conj); 
      if (conjs.size() > 1)
      { Vector res = new Vector(); 
        for (int i = 0; i < conjs.size(); i++) 
        { ASTTerm conji = (ASTTerm) conjs.get(i);
          Vector nargs = new Vector(); 
          nargs.add(new ASTSymbolTerm("not")); 
          nargs.add(conji);  
          ASTTerm disji = 
             new ASTCompositeTerm("logicalExpression", 
                                  nargs); 
          res.add(disji); 
        }
        return res; 
      } 
    }  

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm arg = (ASTTerm) args.get(1); 
      return ASTTerm.mathOCLConjuncts(arg); 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { // a/b < c is (b < 0 => b*c < a) & (0 < b => a < b*c)

      ASTTerm divis = (ASTTerm) args.get(0); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a 
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(denom);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // b < 0

      ASTTerm rhs = (ASTTerm) args.get(2); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(rhs);
      ASTTerm multDenomRhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(multDenomRhs);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(numer);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // b*c < a

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("=>"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // b < 0 => b*c < a

      Vector newargs5 = new Vector();
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(denom);
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < b
      
      Vector newargs6 = new Vector();
      newargs6.add(numer);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(multDenomRhs);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // a < b*c

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("=>"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // 0 < b => a < b*c

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { // a/b > c is (0 < b => b*c < a) & (b < 0 => a < b*c)

      ASTTerm divis = (ASTTerm) args.get(0); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a 
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(denom);
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // 0 < b

      ASTTerm rhs = (ASTTerm) args.get(2); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(rhs);
      ASTTerm multDenomRhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(multDenomRhs);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(numer);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // b*c < a

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("=>"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // 0 < b => b*c < a

      Vector newargs5 = new Vector();
      newargs5.add(denom);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // b < 0
      
      Vector newargs6 = new Vector();
      newargs6.add(numer);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(multDenomRhs);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // a < b*c

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("=>"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // b < 0 => a < b*c

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { // c < a/b is (b < 0 => a < b*c) & (0 < b => b*c < a)

      ASTTerm divis = (ASTTerm) args.get(2); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a 
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(denom);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // b < 0

      ASTTerm lhs = (ASTTerm) args.get(0); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(lhs);
      ASTTerm multDenomLhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(numer);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(multDenomLhs);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // a < b*c

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("=>"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // b < 0 => a < b*c

      Vector newargs5 = new Vector();
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(denom);
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < b
      
      Vector newargs6 = new Vector();
      newargs6.add(multDenomLhs);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(numer);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // b*c < a

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("=>"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // 0 < b => b*c < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { // c > a/b is (0 < b => a < b*c) & (b < 0 => b*c < a)

      ASTTerm divis = (ASTTerm) args.get(2); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis);  // a
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(denom);
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // 0 < b

      ASTTerm lhs = (ASTTerm) args.get(0); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(lhs);
      ASTTerm multDenomLhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(numer);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(multDenomLhs);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // a < b*c

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("=>"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // 0 < b => a < b*c

      Vector newargs5 = new Vector();
      newargs5.add(denom);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // b < 0
      
      Vector newargs6 = new Vector();
      newargs6.add(multDenomLhs);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(numer);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // b*c < a

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("=>"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // b < 0 => b*c < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { // a*b < 0 is (a < 0 => 0 < b) & (b < 0 => 0 < a)

      ASTTerm mult = (ASTTerm) args.get(0); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(aa);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(zz);
      ASTTerm aalessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // a < 0

      Vector newargs2 = new Vector();
      newargs2.add(zz);
      newargs2.add(new ASTSymbolTerm("<"));
      newargs2.add(bb);
      ASTTerm zerolessThanbb = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // 0 < b

      Vector newargs3 = new Vector();
      newargs3.add(aalessThanZero);
      newargs3.add(new ASTSymbolTerm("=>"));
      newargs3.add(zerolessThanbb);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs3); 
      // a < 0 => 0 < b

      Vector newargs4 = new Vector();
      newargs4.add(bb);
      newargs4.add(new ASTSymbolTerm("<"));
      newargs4.add(zz);
      ASTTerm bblessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs4); 
      // b < 0

      Vector newargs5 = new Vector();
      newargs5.add(zz);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(aa);
      ASTTerm zerolessThanaa = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < a

      Vector newargs6 = new Vector();
      newargs6.add(bblessThanZero);
      newargs6.add(new ASTSymbolTerm("=>"));
      newargs6.add(zerolessThanaa);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs6); 
      // b < 0 => 0 < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(0)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(2)))
    { // 0 < a*b is (0 < a => 0 < b) & (b < 0 => a < 0)

      ASTTerm mult = (ASTTerm) args.get(2); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(zz);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(aa);
      ASTTerm zerolessThanA = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // 0 < a

      Vector newargs2 = new Vector();
      newargs2.add(zz);
      newargs2.add(new ASTSymbolTerm("<"));
      newargs2.add(bb);
      ASTTerm zerolessThanB = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // 0 < b

      Vector newargs3 = new Vector();
      newargs3.add(zerolessThanA);
      newargs3.add(new ASTSymbolTerm("=>"));
      newargs3.add(zerolessThanB);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs3); 
      // 0 < a => 0 < b

      Vector newargs4 = new Vector();
      newargs4.add(bb);
      newargs4.add(new ASTSymbolTerm("<"));
      newargs4.add(zz);
      ASTTerm bblessThan0 = 
         new ASTCompositeTerm("equalityExpression", newargs4); 
      // b < 0

      Vector newargs5 = new Vector();
      newargs5.add(aa);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(zz);
      ASTTerm aalessThan0 = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // a < 0

      Vector newargs6 = new Vector();
      newargs6.add(bblessThan0);
      newargs6.add(new ASTSymbolTerm("=>"));
      newargs6.add(aalessThan0);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs6); 
      // b < 0 => a < 0

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "=".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { // a*b = 0 is (a /= 0 => b = 0) & (b /= 0 => a = 0)

      ASTTerm mult = (ASTTerm) args.get(0); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(aa);
      newargs1.add(new ASTSymbolTerm("/="));
      newargs1.add(zz);
      ASTTerm aaNeqZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // a /= 0

      Vector newargs2 = new Vector();
      newargs2.add(bb);
      newargs2.add(new ASTSymbolTerm("="));
      newargs2.add(zz);
      ASTTerm zeroEqbb = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // b = 0

      Vector newargs3 = new Vector();
      newargs3.add(aaNeqZero);
      newargs3.add(new ASTSymbolTerm("=>"));
      newargs3.add(zeroEqbb);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs3); 
      // a /= 0 => b = 0

      Vector newargs4 = new Vector();
      newargs4.add(bb);
      newargs4.add(new ASTSymbolTerm("/="));
      newargs4.add(zz);
      ASTTerm bbNeqZero = 
         new ASTCompositeTerm("equalityExpression", newargs4); 
      // b /= 0

      Vector newargs5 = new Vector();
      newargs5.add(aa);
      newargs5.add(new ASTSymbolTerm("="));
      newargs5.add(zz);
      ASTTerm zeroEqaa = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // a = 0

      Vector newargs6 = new Vector();
      newargs6.add(bbNeqZero);
      newargs6.add(new ASTSymbolTerm("=>"));
      newargs6.add(zeroEqaa);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs6); 
      // b /= 0 => a = 0

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLConjuncts(arg0); 
    } 

    Vector res = new Vector(); 
    res.add(trm); 
    return res; 
  } 

  public static boolean isMathOCLZero(ASTTerm trm)
  { String lit = trm.literalForm(); 
 
    if ("0".equals(lit) || 
        "0.0".equals(lit))
    { return true; } 

    return false; 
  } 

  public static boolean isMathOCLOne(ASTTerm trm)
  { String lit = trm.literalForm(); 
 
    if ("1".equals(lit) || 
        "1.0".equals(lit))
    { return true; } 

    return false; 
  } 

  public static boolean isMathOCLPolynomial(
                                    ASTTerm trm, ASTTerm var)
  { // + or - with at least one term having var
    // a + b + c is bracketed as a + (b + c)

    if (ASTTerm.isMathOCLAddition(trm) || 
        ASTTerm.isMathOCLSubtraction(trm)) 
    { } 
    else 
    { return false; } 

    ASTTerm arg1 = ASTTerm.mathOCLArgument(trm,0); 
    ASTTerm arg2 = ASTTerm.mathOCLArgument(trm,2); 

    boolean hasX1 = 
        ASTTerm.isSubterm(var, arg1);  
    boolean hasX2 = 
        ASTTerm.isSubterm(var, arg2);

    if (hasX1 || hasX2) { } 
    else 
    { return false; }   

    if (ASTTerm.isPolynomialTerm(arg1)) { } 
    else 
    { return false; } 

    if (ASTTerm.isPolynomialTerm(arg2)) 
    { return true; } 

    return ASTTerm.isMathOCLPolynomial(arg2,var); 
  } 

  public static boolean isPolynomialTerm(ASTTerm trm)
  { // A basic expression or multiplication of a basic 
    // and polynomial term

    String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg)) 
    { return true; }  

    if ("factorExpression".equals(tg) &&
        args.size() == 3 && 
        "*".equals(args.get(1) + ""))
    { ASTTerm arg1 = ASTTerm.mathOCLArgument(trm,0); 
      ASTTerm arg2 = ASTTerm.mathOCLArgument(trm,2); 
      if (ASTTerm.isBasicExpression(arg1))
      { if (ASTTerm.isPolynomialTerm(arg2))
        { return true; } 
        return ASTTerm.isBasicExpression(arg2); 
      } 
      return false; 
    } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isPolynomialTerm(arg0); 
    }

    return false; 
  } 

  public static boolean isBasicExpression(ASTTerm trm)
  { // A basic expression 

    String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg)) 
    { return true; }  

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isBasicExpression(arg0); 
    }

    return false; 
  } 

  public static boolean isMathOCLAddition(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("additiveExpression".equals(tg) &&
        args.size() == 3 && 
        "+".equals(args.get(1) + ""))
    { return true; } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { return isMathOCLAddition((ASTTerm) args.get(1)); } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isMathOCLAddition(arg0); 
    }

    return false; 
  } 

  public static boolean isMathOCLSubtraction(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("additiveExpression".equals(tg) &&
        args.size() == 3 && 
        "-".equals(args.get(1) + ""))
    { return true; } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { return isMathOCLSubtraction((ASTTerm) args.get(1)); } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isMathOCLSubtraction(arg0); 
    }

    return false; 
  } 

  public static boolean isMathOCLMultiplication(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("factorExpression".equals(tg) &&
        args.size() == 3 && 
        "*".equals(args.get(1) + ""))
    { return true; } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isMathOCLMultiplication(arg0); 
    }

    return false; 
  } 

  public static boolean isMathOCLDivision(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("factorExpression".equals(tg) &&
        args.size() == 3 && 
        "/".equals(args.get(1) + ""))
    { return true; } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isMathOCLDivision(arg0); 
    }

    return false; 
  } 

  public static ASTTerm mathOCLNumerator(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("factorExpression".equals(tg) &&
        args.size() == 3 && 
        "/".equals(args.get(1) + ""))
    { return (ASTTerm) args.get(0); } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.mathOCLNumerator(arg0); 
    }

    return null; 
  } 

  public static ASTTerm mathOCLDenominator(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("factorExpression".equals(tg) &&
        args.size() == 3 && 
        "/".equals(args.get(1) + ""))
    { return (ASTTerm) args.get(2); } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.mathOCLDenominator(arg0); 
    }

    return null; 
  } 

  public static ASTTerm mathOCLArgument(ASTTerm trm, int i)
  { // For i-th argument of binary or higher expressions

    String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { return mathOCLArgument((ASTTerm) args.get(1), i); } 

    if (args.size() >= 3 && i <= 2)
    { return (ASTTerm) args.get(i); } 

    if (args.size() >= 2 && i < 2)
    { return (ASTTerm) args.get(i); } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.mathOCLArgument(arg0,i); 
    }

    return null; 
  } // Nested unary terms until the expected n-ary term.

  public static boolean isMathOCLNegative(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("factorExpression".equals(tg) &&
        args.size() == 2 && 
        "-".equals(args.get(0) + ""))
    { return true; } 

    if (args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0);
      return ASTTerm.isMathOCLNegative(arg0); 
    }

    return false; 
  } 

  public static boolean isMathOCLDisjunction(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm tt = (ASTTerm) args.get(1);
      return ASTTerm.isMathOCLDisjunction(tt);
    } // ( expr )

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<=".equals(args.get(1) + ""))
    { return true; } // a <= b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">=".equals(args.get(1) + ""))
    { return true; } // a >= b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { return true; } // a/b < c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { return true; } // c < a/b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { return true; } // a/b > c

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { return true; } // c > a/b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { return true; } // a*b < 0

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(0)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(2)))
    { return true; } // 0 < a*b

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "=".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { return true; } // a*b = 0 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "or".equals(args.get(1) + ""))
    { return true; } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "=>".equals(args.get(1) + ""))
    { return true; } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 2 && 
        "not".equals(args.get(0) + "") && 
        ASTTerm.isMathOCLConjunction((ASTTerm) args.get(1)))
    { return true; } // at least 2 conjuncts

    if ("logicalExpression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLDisjunction(arg0); 
    } 

    if ("expression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLDisjunction(arg0); 
    } 

    if ("expressionList".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLDisjunction(arg0); 
    } 

    if ("expressionList".equals(tg) && 
        args.size() > 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      if (ASTTerm.isMathOCLDisjunction(arg0))
      { return true; } 
      Vector argstail = new Vector(); 
      argstail.addAll(args); 
      argstail.remove(0); 
      ASTCompositeTerm newante = 
        new ASTCompositeTerm("expressionList", argstail); 
      return ASTTerm.isMathOCLDisjunction(newante);  
    } 

    return false; 
  } 

  public static Vector mathOCLDisjuncts(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm tt = (ASTTerm) args.get(1);
      return ASTTerm.mathOCLDisjuncts(tt);
    } // ( expr )

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<=".equals(args.get(1) + ""))
    { Vector newargs1 = new Vector();
      newargs1.add(args.get(0)); // a
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(args.get(2)); // b
       
      ASTTerm d1 = new ASTCompositeTerm(tg, newargs1); 
      
      Vector newargs2 = new Vector();
      newargs2.add(args.get(0)); // a
      newargs2.add(new ASTSymbolTerm("="));
      newargs2.add(args.get(2)); // b
      
      ASTTerm d2 = new ASTCompositeTerm(tg, newargs2);
      Vector res = new Vector(); 
      res.add(d1); res.add(d2);  
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">=".equals(args.get(1) + ""))
    { Vector newargs1 = new Vector();
      newargs1.add(args.get(2)); // b
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(args.get(0)); // a
       
      ASTTerm d1 = new ASTCompositeTerm(tg, newargs1); 
      
      Vector newargs2 = new Vector();
      newargs2.add(args.get(0)); // a
      newargs2.add(new ASTSymbolTerm("="));
      newargs2.add(args.get(2)); // b
      
      ASTTerm d2 = new ASTCompositeTerm(tg, newargs2);
      Vector res = new Vector(); 
      res.add(d1); res.add(d2);  
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { // a/b < c is (b < 0 & b*c < a) or (0 < b & a < b*c)

      ASTTerm divis = (ASTTerm) args.get(0); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a 
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(denom);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // b < 0

      ASTTerm rhs = (ASTTerm) args.get(2); 
      // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(rhs);
      ASTTerm multDenomRhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(multDenomRhs);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(numer);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // b*c < a

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("&"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // b < 0 & b*c < a

      Vector newargs5 = new Vector();
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(denom);
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < b
      
      Vector newargs6 = new Vector();
      newargs6.add(numer);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(multDenomRhs);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // a < b*c

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("&"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // 0 < b & a < b*c

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(0)))
    { // a/b > c is (b < 0 & a < b*c) or (0 < b & b*c < a)

      ASTTerm divis = (ASTTerm) args.get(0); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(denom);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // b < 0

      ASTTerm rhs = (ASTTerm) args.get(2); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(rhs);
      ASTTerm multDenomRhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(numer);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(multDenomRhs);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // a < b*c

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("&"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // b < 0 & a < b*c

      Vector newargs5 = new Vector();
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(denom);
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < b

      Vector newargs6 = new Vector();
      newargs6.add(multDenomRhs);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(numer);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // b*c < a

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("&"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // 0 < b & b*c < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { // c < a/b is (b < 0 & a < b*c) or (0 < b & b*c < a)

      ASTTerm divis = (ASTTerm) args.get(2); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(denom);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // b < 0

      ASTTerm lhs = (ASTTerm) args.get(0); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(lhs);
      ASTTerm multDenomLhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(numer);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(multDenomLhs);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // a < b*c

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("&"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // b < 0 & a < b*c

      Vector newargs5 = new Vector();
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(denom);
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5);
      // 0 < b 
      
      Vector newargs6 = new Vector();
      newargs6.add(multDenomLhs);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(numer);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // b*c < a

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("&"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // 0 < b & b*c < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        ">".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLDivision((ASTTerm) args.get(2)))
    { // c > a/b is (0 < b & a < b*c) or (b < 0 & b*c < a)

      ASTTerm divis = (ASTTerm) args.get(2); 
      ASTTerm numer = ASTTerm.mathOCLNumerator(divis); // a
      ASTTerm denom = ASTTerm.mathOCLDenominator(divis); // b

      Vector newargs1 = new Vector();
      newargs1.add(new ASTBasicTerm("basicExpression", "0"));
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(denom);
      ASTTerm lessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // 0 < b

      ASTTerm lhs = (ASTTerm) args.get(0); // c

      Vector newargs2 = new Vector();
      newargs2.add(denom);
      newargs2.add(new ASTSymbolTerm("*"));
      newargs2.add(lhs);
      ASTTerm multDenomLhs = 
         new ASTCompositeTerm("factorExpression", newargs2); 
      // b*c

      Vector newargs3 = new Vector();
      newargs3.add(numer);
      newargs3.add(new ASTSymbolTerm("<"));
      newargs3.add(multDenomLhs);
      ASTTerm lessThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs3); 
      // a < b*c

      Vector newargs4 = new Vector();
      newargs4.add(lessThanZero);
      newargs4.add(new ASTSymbolTerm("&"));
      newargs4.add(lessThanNum);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs4); 
      // 0 < b & a < b*c

      Vector newargs5 = new Vector();
      newargs5.add(denom);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(new ASTBasicTerm("basicExpression", "0"));
      ASTTerm greaterThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // b < 0

      Vector newargs6 = new Vector();
      newargs6.add(multDenomLhs);
      newargs6.add(new ASTSymbolTerm("<"));
      newargs6.add(numer);
      ASTTerm greaterThanNum = 
         new ASTCompositeTerm("equalityExpression", newargs6); 
      // b*c < a

      Vector newargs7 = new Vector();
      newargs7.add(greaterThanZero);
      newargs7.add(new ASTSymbolTerm("&"));
      newargs7.add(greaterThanNum);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs7); 
      // b < 0 & b*c < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { // a*b < 0 is (a < 0 & 0 < b) or (b < 0 & 0 < a)

      ASTTerm mult = (ASTTerm) args.get(0); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(aa);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(zz);
      ASTTerm aalessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // a < 0

      Vector newargs2 = new Vector();
      newargs2.add(zz);
      newargs2.add(new ASTSymbolTerm("<"));
      newargs2.add(bb);
      ASTTerm zerolessThanbb = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // 0 < b

      Vector newargs3 = new Vector();
      newargs3.add(aalessThanZero);
      newargs3.add(new ASTSymbolTerm("&"));
      newargs3.add(zerolessThanbb);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs3); 
      // a < 0 & 0 < b

      Vector newargs4 = new Vector();
      newargs4.add(bb);
      newargs4.add(new ASTSymbolTerm("<"));
      newargs4.add(zz);
      ASTTerm bblessThanZero = 
         new ASTCompositeTerm("equalityExpression", newargs4); 
      // b < 0

      Vector newargs5 = new Vector();
      newargs5.add(zz);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(aa);
      ASTTerm zerolessThanaa = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // 0 < a

      Vector newargs6 = new Vector();
      newargs6.add(bblessThanZero);
      newargs6.add(new ASTSymbolTerm("&"));
      newargs6.add(zerolessThanaa);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs6); 
      // b < 0 & 0 < a

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "<".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(0)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(2)))
    { // 0 < a*b is (0 < a & 0 < b) or (b < 0 & a < 0)

      ASTTerm mult = (ASTTerm) args.get(2); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(zz);
      newargs1.add(new ASTSymbolTerm("<"));
      newargs1.add(aa);
      ASTTerm zerolessThanA = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // 0 < a

      Vector newargs2 = new Vector();
      newargs2.add(zz);
      newargs2.add(new ASTSymbolTerm("<"));
      newargs2.add(bb);
      ASTTerm zerolessThanB = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // 0 < b

      Vector newargs3 = new Vector();
      newargs3.add(zerolessThanA);
      newargs3.add(new ASTSymbolTerm("&"));
      newargs3.add(zerolessThanB);
      ASTTerm conj1 = 
         new ASTCompositeTerm("logicalExpression", newargs3); 
      // 0 < a & 0 < b

      Vector newargs4 = new Vector();
      newargs4.add(bb);
      newargs4.add(new ASTSymbolTerm("<"));
      newargs4.add(zz);
      ASTTerm bblessThan0 = 
         new ASTCompositeTerm("equalityExpression", newargs4); 
      // b < 0

      Vector newargs5 = new Vector();
      newargs5.add(aa);
      newargs5.add(new ASTSymbolTerm("<"));
      newargs5.add(zz);
      ASTTerm aalessThan0 = 
         new ASTCompositeTerm("equalityExpression", newargs5); 
      // a < 0

      Vector newargs6 = new Vector();
      newargs6.add(bblessThan0);
      newargs6.add(new ASTSymbolTerm("&"));
      newargs6.add(aalessThan0);
      ASTTerm conj2 = 
         new ASTCompositeTerm("logicalExpression", newargs6); 
      // b < 0 & a < 0

      Vector res = new Vector(); 
      res.add(conj1);   
      res.add(conj2); 
      return res; 
    } 

    if ("equalityExpression".equals(tg) &&
        args.size() == 3 && 
        "=".equals(args.get(1) + "") && 
        ASTTerm.isMathOCLZero((ASTTerm) args.get(2)) && 
        ASTTerm.isMathOCLMultiplication((ASTTerm) args.get(0)))
    { // a*b = 0 is a = 0 or b = 0

      ASTTerm mult = (ASTTerm) args.get(0); 
      ASTTerm aa = ASTTerm.mathOCLArgument(mult,0); 
      ASTTerm bb = ASTTerm.mathOCLArgument(mult,2); 

      ASTTerm zz = new ASTBasicTerm("basicExpression", "0"); 

      Vector newargs1 = new Vector();
      newargs1.add(aa);
      newargs1.add(new ASTSymbolTerm("="));
      newargs1.add(zz);
      ASTTerm aaEqZero = 
         new ASTCompositeTerm("equalityExpression", newargs1); 
      // a = 0

      Vector newargs2 = new Vector();
      newargs2.add(bb);
      newargs2.add(new ASTSymbolTerm("="));
      newargs2.add(zz);
      ASTTerm bbEqZero = 
         new ASTCompositeTerm("equalityExpression", newargs2); 
      // b = 0

      Vector res = new Vector(); 
      res.add(aaEqZero); 
      res.add(bbEqZero); 
      return res; 
    } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "or".equals(args.get(1) + ""))
    { Vector res = new Vector(); 
      ASTTerm lhs = (ASTTerm) args.get(0); 
      ASTTerm rhs = (ASTTerm) args.get(2); 
      res.addAll(ASTTerm.mathOCLDisjuncts(lhs)); 
      res.addAll(ASTTerm.mathOCLDisjuncts(rhs)); 
      return res; 
    } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "=>".equals(args.get(1) + ""))
    { // A => B is not(A) or B
      Vector res = new Vector(); 
      ASTTerm lhs = (ASTTerm) args.get(0); 
      ASTTerm rhs = (ASTTerm) args.get(2); 
      Vector notlhs = new Vector(); 
      notlhs.add(new ASTSymbolTerm("not")); 
      notlhs.add(lhs); 
      res.add(new ASTCompositeTerm("logicalExpression", notlhs)); 
      res.addAll(ASTTerm.mathOCLDisjuncts(rhs)); 
      return res; 
    } 

    if ("logicalExpression".equals(tg) &&
        args.size() == 2 && 
        "not".equals(args.get(0) + "") && 
        ASTTerm.isMathOCLConjunction((ASTTerm) args.get(1)))
    { ASTTerm conj = (ASTTerm) args.get(1); 
      Vector conjs = ASTTerm.mathOCLConjuncts(conj); 
      if (conjs.size() > 1)
      { Vector res = new Vector(); 
        for (int i = 0; i < conjs.size(); i++) 
        { ASTTerm conji = (ASTTerm) conjs.get(i);
          Vector nargs = new Vector(); 
          nargs.add(new ASTSymbolTerm("not")); 
          nargs.add(conji);  
          ASTTerm disji = 
             new ASTCompositeTerm("logicalExpression", 
                                  nargs); 
          res.add(disji); 
        }
        return res; 
      } 
    }  
 

    if ("logicalExpression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLDisjuncts(arg0); 
    } 

    if ("expression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLDisjuncts(arg0); 
    } 

    if ("expressionList".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLDisjuncts(arg0); 
    } 

    if ("expressionList".equals(tg) && 
        args.size() > 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      if (ASTTerm.isMathOCLDisjunction(arg0))
      { Vector res = new Vector(); 
        Vector arg0disjuncts = 
          ASTTerm.mathOCLDisjuncts(arg0);
        for (int i = 0; i < arg0disjuncts.size(); i++) 
        { ASTTerm disj = (ASTTerm) arg0disjuncts.get(i);  
          Vector newargs = new Vector();
          newargs.add(disj); 
          for (int j = 1; j < args.size(); j++) 
          { newargs.add(args.get(j)); } 
          res.add(
            new ASTCompositeTerm("expressionList", newargs));  
        } 
        return res; 
      } 

      Vector argstail = new Vector(); 
      argstail.addAll(args); 
      argstail.remove(0); 
      ASTCompositeTerm newante = 
        new ASTCompositeTerm("expressionList", argstail); 
      if (ASTTerm.isMathOCLDisjunction(newante))
      { Vector res = new Vector(); 
        Vector exprs = ASTTerm.mathOCLDisjuncts(newante); 
        for (int i = 0; i < exprs.size(); i++) 
        { ASTTerm expr = (ASTTerm) exprs.get(i); 
          // (expressionList terms)
          Vector oldlist = expr.getTerms(); 
          Vector newlist = new Vector(); 
          newlist.add(arg0); 
          newlist.addAll(oldlist); 
          res.add(  
            new ASTCompositeTerm("expressionList", newlist));
        } 
        return res; 
      } 
    } 

    Vector res = new Vector(); 
    res.add(trm); 
    return res; 
  } // Also case of x : Set{a,b,c} is 3 disjuncts, etc

  public static boolean isMathOCLImplication(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "=>".equals(args.get(1) + ""))
    { return true; } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm tt = (ASTTerm) args.get(1); 
      return ASTTerm.isMathOCLImplication(tt);
    } 

    if ("logicalExpression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLImplication(arg0); 
    } 

    if ("expression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.isMathOCLImplication(arg0); 
    } 

    return false; 
  } 

  public static ASTTerm mathOCLAntecedent(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "=>".equals(args.get(1) + ""))
    { return (ASTTerm) args.get(0); } 

    if ("logicalExpression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLAntecedent(arg0); 
    } 

    if ("expression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLAntecedent(arg0); 
    } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm tt = (ASTTerm) args.get(1); 
      return ASTTerm.mathOCLAntecedent(tt);
    } 

    return null; 
  } 

  public static ASTTerm mathOCLSuccedent(ASTTerm trm)
  { String tg = trm.getTag();
    Vector args = trm.getTerms(); 
 
    if ("logicalExpression".equals(tg) &&
        args.size() == 3 && 
        "=>".equals(args.get(1) + ""))
    { return (ASTTerm) args.get(2); } 

    if ("logicalExpression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLSuccedent(arg0); 
    } 

    if ("expression".equals(tg) && 
        args.size() == 1) 
    { ASTTerm arg0 = (ASTTerm) args.get(0); 
      return ASTTerm.mathOCLSuccedent(arg0); 
    } 

    if ("basicExpression".equals(tg) &&
        args.size() == 3 && 
        "(".equals(args.get(0) + "") && 
        ")".equals(args.get(2) + ""))
    { ASTTerm tt = (ASTTerm) args.get(1); 
      return ASTTerm.mathOCLSuccedent(tt);
    } 

    return null; 
  } 

  public static boolean mathOCLContradiction(ASTTerm xx, ASTTerm yy)
  { int n = xx.arity(); 
    int m = yy.arity(); 

    String xlit = xx.literalForm(); 
    String ylit = yy.literalForm(); 

    if (n == 2 && "not".equals(xx.getTerm(0) + ""))
    { ASTTerm arg = xx.getTerm(1); 
      if (ylit.equals(arg.literalForm()))
      { return true; } 
    } // not P, P 

    if (m == 2 && "not".equals(yy.getTerm(0) + ""))
    { ASTTerm arg = yy.getTerm(1); 
      if (xlit.equals(arg.literalForm()))
      { return true; } 
    } // P, not P 

    if (n == 3 && m == 3)
    { ASTTerm arg1x = xx.getTerm(0); 
      String opx = "" + xx.getTerm(1); 
      ASTTerm arg2x = xx.getTerm(2); 
      ASTTerm arg1y = yy.getTerm(0); 
      String opy = "" + yy.getTerm(1); 
      ASTTerm arg2y = yy.getTerm(2); 

      if (arg1x.equals(arg1y) && arg2x.equals(arg2y) && 
          opy.equals(Expression.negateOp(opx)))
      { return true; } 
    } // a opr b, a neg_op b

    return false; 
  } 
       
  public static boolean containsContradictions(Vector aterms)
  { // an expression and its negation are in aterms

    for (int i = 0; i < aterms.size(); i++) 
    { ASTTerm xx = (ASTTerm) aterms.get(i); 
      for (int j = i+1; j < aterms.size(); j++) 
      { ASTTerm yy = (ASTTerm) aterms.get(j); 
        if (ASTTerm.mathOCLContradiction(xx,yy))
        { return true; } 
      } 
    } 
    return false; 
  } 

  public static String attemptProof(ASTTerm succ, ASTTerm ante)
  { // Try to prove  ante => succ
    String slit = succ.literalFormSpaces(); 
    String alist = ante.literalFormSpaces(); // has , instead of &

    String alit = ""; 
    Vector aterms = ante.getTerms(); 
    for (int i = 0; i < aterms.size(); i++) 
    { ASTTerm atermi = (ASTTerm) aterms.get(i); 
      if (",".equals(atermi + ""))
      { alit = alit + " & "; } 
      else 
      { alit = alit + atermi.literalFormSpaces(); } 
    } 
       
    Vector assumptions = ASTTerm.mathOCLConjuncts(ante); 
    Vector assumptionlits = new Vector(); 
    for (int i = 0; i < assumptions.size(); i++) 
    { ASTTerm assump = (ASTTerm) assumptions.get(i); 
      assumptionlits.add(assump.literalFormSpaces()); 
    } 

    Vector thms = ASTTerm.mathocltheorems;
    Vector rewrites = ASTTerm.mathoclrewrites; 
 
    /* JOptionPane.showMessageDialog(null, 
       "### Rewrites: " + rewrites,   "",
       JOptionPane.INFORMATION_MESSAGE);  */ 


    // get variables svars from succ, 
    // avars from ante. 
    // For v : svars - avars, replace v in succ 
    // by v's definition
    // For v : avars - svars, replace v in ante 
    // by v's definition

    // Look for a Theorem conc when prem
    // If conc matches succ, replace by 
    // instantiated prem

    if (slit.equals(alit))
    { return "  Simplify true\n"; }

    if (assumptionlits.contains(slit))
    { return "  Simplify true\n"; } 

    if (ASTTerm.containsContradictions(assumptions))
    { return "  Simplify true\n"; } 

    for (int i = 0; i < thms.size(); i++) 
    { Vector thm = (Vector) thms.get(i); // [concl,premise]
      ASTTerm concl = (ASTTerm) thm.get(0); 
      ASTTerm premise = (ASTTerm) thm.get(1); 

      java.util.HashMap env = new java.util.HashMap(); 
      java.util.HashMap binds = 
         succ.fullMatch(concl,env); 

    /*  JOptionPane.showMessageDialog(null, 
          "### Binding: " + binds + " for " + 
          succ + " " + concl,   "",
          JOptionPane.INFORMATION_MESSAGE); 
       Let user choose if to apply this inference */ 

      if (binds != null) // replace succ by premise[binds]
      { ASTTerm newsucc = 
          premise.instantiate(binds); 
        /* JOptionPane.showMessageDialog(null, 
          "### Binding: " + binds + " for " + 
          succ + " " + concl + " " + newsucc,   "",
          JOptionPane.INFORMATION_MESSAGE); */ 

        return "  Prove " + newsucc.literalFormSpaces() + 
               " if " + alist + "\n";  
      } 
    } 

    if (ASTTerm.isMathOCLConjunction(succ))
    { Vector conjs = ASTTerm.mathOCLConjuncts(succ);

      /* JOptionPane.showMessageDialog(null, 
         ">>> Conjunction " + succ + " are: " + conjs, 
         "", 
         JOptionPane.INFORMATION_MESSAGE); */ 

      if (conjs.size() > 1)
      { 
        String res = ""; 
        for (int i = 0; i < conjs.size(); i++) 
        { ASTTerm conj = (ASTTerm) conjs.get(i); 
          res = res + 
            "  Prove " + conj.literalFormSpaces() + " if " + alist + "\n"; 
        } 
        return res; 
      } 
    }  
 
    if (ASTTerm.isMathOCLDisjunction(ante))
    { Vector disjs = ASTTerm.mathOCLDisjuncts(ante);

      if (disjs.size() > 1)
      { String res = ""; 
        for (int i = 0; i < disjs.size(); i++) 
        { ASTTerm disj = (ASTTerm) disjs.get(i); 
          res = res + 
            "  Prove " + slit + " if " + disj.literalFormSpaces() + "\n"; 
        }
        return res;
      } 
    } // also case of (A or B), C  disjuncts are A, C and B, C

    // If succ is a disjunction, it holds if any disjunct is 
    // also equal to or a conjunct of the ante.

    if (ASTTerm.isMathOCLImplication(succ))
    { // A => B, so add A to ante

      ASTTerm pre = ASTTerm.mathOCLAntecedent(succ); 
      ASTTerm post = ASTTerm.mathOCLSuccedent(succ); 

      return "  Prove " + post.literalFormSpaces() + 
             " if " + alist + ", " + pre.literalFormSpaces() + "\n";
    } 

    if (ASTTerm.isMathOCLDisjunction(succ))
    { Vector disjs = ASTTerm.mathOCLDisjuncts(succ);
      Vector subdis = new Vector(); 

      for (int i = 0; i < disjs.size(); i++) 
      { ASTTerm disj = (ASTTerm) disjs.get(i);
        String dlit = disj.literalFormSpaces();  
        if (dlit.equals(alit) || 
            assumptionlits.contains(dlit)) 
        { return "  Simplify true\n"; } 
        else 
        { subdis.add(dlit); } 
      } 

      String res = ""; 
      for (int j = 0; j < subdis.size(); j++) 
      { String subdisj = (String) subdis.get(j); 

        Vector remainingDisjuncts = new Vector(); 
        remainingDisjuncts.addAll(subdis); 
        remainingDisjuncts.remove(subdisj); 

        res = res + "  Prove " + subdisj + " if "; 
        for (int k = 0; k < remainingDisjuncts.size(); k++) 
        { String rdis = (String) remainingDisjuncts.get(k); 
          res = res + "not (" + rdis + "), "; 
        } 
        res = res + alist + "\n"; 
      } 
      return res; 
    }  
 
  /* 
    Vector avars = ante.mathOCLVariables(); 
    Vector svars = succ.mathOCLVariables(); 

    System.out.println("###### Antecedent variables: " + avars); 
    System.out.println("###### Succedent variables: " + svars); */ 

    for (int y = 0; y < rewrites.size(); y++) 
    { Vector rewrite = (Vector) rewrites.get(y); 
      ASTTerm lhs = (ASTTerm) rewrite.get(0); 
      boolean isIn = ASTTerm.isSubterm(lhs,succ);

      System.out.println("###### is subterm: " + lhs + " of " + succ + " " + isIn); 

      if (isIn) // replace lhs by its definition
      { String vv = lhs.literalForm(); 
        ASTTerm rhs = (ASTTerm) rewrite.get(1); 
        ASTTerm newsucc = succ.mathOCLSubstitute(vv,rhs); 
        String alitspace = ante.literalFormSpaces(); 
        String slitspace = newsucc.literalFormSpaces(); 

        return "  Prove " + slitspace + " if " + alitspace; 
      } 
      else // for schematic lhs expr[_V,...]
      { ASTTerm rhs = (ASTTerm) rewrite.get(1); 

        java.util.HashMap env = new java.util.HashMap(); 
        java.util.HashMap binds = 
           succ.hasMatch(lhs,env); 
        // _V |-> expr1, ...

    /*    JOptionPane.showMessageDialog(null, 
          "### Binding for rewrite: " + binds + " for " + 
          lhs + " in " + succ,   "",
          JOptionPane.INFORMATION_MESSAGE); */ 

        if (binds != null) // substitute rhs[binds] for 
        {                  // lhs[binds] in succ
          ASTTerm actualrhs = 
            rhs.instantiate(binds); 
          ASTTerm actuallhs = 
            lhs.instantiate(binds); 
        /*  JOptionPane.showMessageDialog(null, 
            "### Bound versions: " + actuallhs + " |--> " + 
                 actualrhs,   "",
            JOptionPane.INFORMATION_MESSAGE);  */ 

          String vv = actuallhs.literalForm(); 
           
          ASTTerm newsucc = 
             succ.mathOCLSubstitute(vv,actualrhs); 
          String alitspace = ante.literalFormSpaces(); 
          String slitspace = newsucc.literalFormSpaces(); 

          return "  Prove " + slitspace + " if " + alitspace;   
        } 
      } 

    }  


    // String alitspaces = ante.literalFormSpaces(); 
    String slitspaces = succ.literalFormSpaces(); 

    return "  Simplify (" + alit + ") => " + slitspaces; 
  } 

  public static String solveEquations(ASTTerm exprs, ASTTerm vars)
  { Vector exprTerms = exprs.getTerms(); 
    Vector varTerms = vars.getTerms();

    Vector variables = new Vector();  
    java.util.Map varCoefficients = new java.util.HashMap(); 

    String res = ""; 

      // find coefficients of each var in each expr

    if (varTerms.size() == 1 && exprTerms.size() == 1) 
    { // test what powers of var are present
      // It could be a quadratic, or differential eqn

      ASTTerm var0 = (ASTTerm) varTerms.get(0); 
      ASTTerm expr0 = (ASTTerm) exprTerms.get(0); 
      String vx0 = var0.literalForm();

      ASTTerm xterm = new ASTBasicTerm("identifier", "x"); 
      boolean hasX = 
        ASTTerm.isSubterm(xterm, expr0);  
          
      Vector powers = ASTTerm.powersOf(var0,expr0);
      Vector powersR = VectorUtil.removeDuplicates(powers);

      double minp = VectorUtil.vectorMinimum(powers); 
      double maxp = VectorUtil.vectorMaximum(powers); 
 
      Vector diffs = ASTTerm.differentialsOf(var0,expr0);
      Vector diffsR = VectorUtil.removeDuplicates(diffs);
 
      double maxdp = VectorUtil.vectorMaximum(diffsR); 
      double mindp = VectorUtil.vectorMinimum(diffsR); 
          
      JOptionPane.showMessageDialog(null, 
         ">>> Var powers of " + vx0 + " are: " +  
         powersR + " from: " + minp + " to: " + maxp + 
         " Differentials: " + diffsR + " highest: " + maxdp, 
         "", 
         JOptionPane.INFORMATION_MESSAGE); 

      if (maxp == 2 && minp >= 0 && maxdp == 0) 
      { String coefsq = ASTTerm.coefficientOfSquare(var0, expr0); 
        String coefvar = ASTTerm.coefficientOf(var0, expr0); 
        String cnsts = 
              ASTTerm.constantTerms(varTerms,expr0);
        JOptionPane.showMessageDialog(null, 
            ">>> This is a quadratic formula, solving using quadratic solver. Coefficients: " + coefsq + " ; " + coefvar + " ; " + cnsts, 
            "", 
            JOptionPane.INFORMATION_MESSAGE);

        String quadformula1 = 
            AuxMath.quadraticFormula1(coefsq, coefvar, cnsts); 
        String quadformula2 = 
            AuxMath.quadraticFormula2(coefsq, coefvar, cnsts); 
          
        return "  Define " + vx0 + "$1 = " + quadformula1 + "\n" + 
               "  Define " + vx0 + "$2 = " + quadformula2 + "\n" + 
               "  Define " + vx0 + "\n" + 
               "  Constraint on " + vx0 + " | (" + vx0 + " = " + vx0 + "$1) or (" + vx0 + " = " + vx0 + "$2)"; 
      } 
      else if (maxp == 1 && minp == -1 && maxdp == 0) 
      { // multiply by var
        String newformula = 
            ASTTerm.symbolicMultiplication(vx0, expr0); 
        return "  Solve " + newformula + " for " + vx0 + "\n"; 
      } 
      else if (maxp == 1 && minp >= 0 && maxdp == 0) 
      // linear
      { } 
      else if (maxdp == 0 && 
               maxp > 0 && 
               minp == 0 && powersR.size() == 2)
      { // direct solution vx0 = (coef0/coefmaxp)^{1/maxp}
 
        ASTTerm vpow; 
        if (ASTTerm.isIntegerValued(maxp))
        { vpow = ASTTerm.constructNPower(((int) maxp) + "", var0); }
        else 
        { vpow = ASTTerm.constructNPower(maxp + "", var0); }

        String ncoef = ASTTerm.coefficientOf(vpow,expr0); 

        Vector dvars = new Vector(); 
        dvars.add(vpow); 
        String dcnst = ASTTerm.constantTerms(dvars,expr0);

        JOptionPane.showMessageDialog(null, 
              ">>> This is an explicit n-power equation: " + 
              vpow + 
              " " + ncoef + " " + dcnst, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);
         return "  Simplify " + vx0 + " = (-(" + dcnst + ")/" + 
                                   ncoef + ")^{1/" + maxp + "}\n"; 
      } 
      else if (maxdp == 0 && 
               maxp == 0 && 
               minp < 0 && powersR.size() == 2)
      { // direct solution vx0 = (coef0/coefminp)^{1/minp} 
        ASTTerm vpow; 

        if (ASTTerm.isIntegerValued(minp))
        { vpow = ASTTerm.constructNPower(((int) minp) + "", var0); }
        else 
        { vpow = ASTTerm.constructNPower(minp + "", var0); }
 
        String ncoef = ASTTerm.coefficientOf(vpow,expr0); 

        Vector dvars = new Vector(); 
        dvars.add(vpow); 
        String dcnst = ASTTerm.constantTerms(dvars,expr0);

        JOptionPane.showMessageDialog(null, 
              ">>> This is an explicit n-power equation in " + 
              vpow + 
              " Coefficient: " + ncoef + " Constant: " + dcnst, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);
         return "  Simplify " + vx0 + " = (-(" + dcnst + ")/" + 
                                   ncoef + ")^{1/" + minp + "}\n"; 
      } 
      else if (maxdp > 0 && diffsR.size() <= 2 && 
               (mindp == 0 || mindp == maxdp) && 
               maxp == 0)
      { // 1st-order differential eqn with only one diff term
        // a(x)*vdiff + b(x) = 0 or other polynomial in vdiff

        ASTTerm vdiff =
          ASTTerm.constructNDifferential((int) maxdp, var0); 
 
        Vector dpowers = ASTTerm.powersOf(vdiff,expr0);
        String dcoef = ASTTerm.coefficientOf(vdiff,expr0); 

        Vector dvars = new Vector(); 
        dvars.add(vdiff); 
        String dcnst = ASTTerm.constantTerms(dvars,expr0);

        double mindpp = VectorUtil.vectorMinimum(dpowers); 
        double maxdpp = VectorUtil.vectorMaximum(dpowers); 

        ASTTerm maxDpow = ASTTerm.constructNPower(((int) maxdpp) + "", var0);
 
        String dMcoef = ASTTerm.coefficientOf(maxDpow,expr0); 

        JOptionPane.showMessageDialog(null, 
           ">>> This is differential equation with one differential term: Differentials: " + 
           diffsR + 
           " Powers: " + dpowers + " of: " + vdiff + 
           " Contains x: " + hasX + " Constant: " + dcnst + 
           " Min power: " + mindpp + " Max power: " + maxdpp, 
           "", 
           JOptionPane.INFORMATION_MESSAGE);

        
        if (maxdpp == 1 && mindpp >= 0) // linear
        { if (maxdp == 1) // dcoef*(f') + dcnst = 0
          { return "  Define " + vx0 + 
                     " = - ‡ " + 
                     "(" + dcnst + ")/(" + dcoef + ") dx\n";
          } 
          else // f'' or higher occurs
          { ASTTerm vdiffpar1 = vdiff.getTerm(0); 
            return "  Solve " + vdiffpar1.literalForm() + 
                     " = - ‡ " + 
                     "(" + dcnst + ")/(" + dcoef + 
                     ") dx for " + vx0 + "\n"; 
          }  
        }
        else if (maxdpp == 2 && mindpp >= 0) // quadratic
        { String d2coef = 
            ASTTerm.coefficientOfSquare(vdiff,expr0); 

         /* JOptionPane.showMessageDialog(null, 
           ">>> Coeffiecient of square of : " + vdiff + " in " + expr0 +  
           " is " + d2coef, 
           "", 
           JOptionPane.INFORMATION_MESSAGE); */ 

          String d1coef = 
            ASTTerm.coefficientOf(vdiff,expr0); 

          String quadf1 = 
              AuxMath.quadraticFormula1(d2coef, d1coef, 
                                        dcnst); 
          String quadf2 = 
              AuxMath.quadraticFormula2(d2coef, d1coef, 
                                        dcnst);
 
          return "  Solve " + vx0 + "1´ = " + quadf1 + " for " + vx0 + "1\n" + 
               "  Solve " + vx0 + "2´ = " + quadf2 + " for " + vx0 + "2\n" + 
               "  Define " + vx0 + "\n" + 
               "  Constraint on " + vx0 + " | (" + vx0 + " = " + vx0 + "1) or (" + vx0 + " = " + vx0 + "2)\n"; 

        }

        return "  Solve " + exprs.literalForm() + " for " + vars.literalForm() + "\n";
      }
      else if (maxdp > 0)
      { // Differential eqn with several diff terms

        Vector alldcoefs = new Vector();
        Vector alldpowers = new Vector();  
        Vector vdiffs =
          ASTTerm.constructNDifferentialsPowers(
                              (int) maxdp, var0,
                              expr0, alldcoefs, alldpowers); 
          

        ASTTerm vdiff =
          ASTTerm.constructNDifferential((int) maxdp, var0); 
 
        Vector vdiffpowers = ASTTerm.powersOf(vdiff,expr0);
        String dcoef = ASTTerm.coefficientOf(vdiff,expr0); 

        Vector ddvars = new Vector(); 
        ddvars.add(var0); 
        ddvars.add(vdiff); 
        String dcnst = ASTTerm.constantTerms(ddvars,expr0);

        JOptionPane.showMessageDialog(null, 
           ">>> General differential equation " + 
           "Differentials: " + vdiffs + " All coefficients: " + alldcoefs + " All powers: " + alldpowers + 
           " Max diff: " + vdiff + " Coef: " + dcoef + 
           " Powers: " + vdiffpowers + " Has x: " + hasX + " Constant: " + dcnst, 
           "", 
           JOptionPane.INFORMATION_MESSAGE);

        double maxvdiffp = 
           VectorUtil.vectorMaximum(vdiffpowers); 


        if (maxdp == 1 && vdiffs.size() == 2 && 
            hasX == false && maxvdiffp <= 1) 
        { // 1st order linear homogenous equation 
          // coefd1*f' + coeff*f = 0

          String coeff = "" + alldcoefs.get(0); 
          String coefd1 = "" + alldcoefs.get(1); 
          
          ASTTerm be1 = 
            new ASTBasicTerm("basicExpression", coeff); 
          ASTTerm be2 = 
            new ASTBasicTerm("basicExpression", coefd1); 

          String frac = 
               "-(" + ASTTerm.symbolicDivision(be1,be2) + ")"; 

          if ("0".equals(dcnst) || "0.0".equals(dcnst))         
          { // Solution is 
            // "A*e^{-(" + coeff + ")/(" + coefd1 + ")}"; 

            mathoclfunctionIndex++; 
            String A = "A" + mathoclfunctionIndex; 

            return 
              "  Define " + A + "\n" + 
              "  Define " + vx0 + 
                   " = " + A + "*e^{(" + frac + ")*x}\n";
          } 
          else 
          { // Solution is above + g
            // for g = -dcnst/coeff or g = -dcnst*x/coefd1
            mathoclfunctionIndex++; 
            String A = "A" + mathoclfunctionIndex; 

            if ("0".equals(coeff) || "0.0".equals(coeff))
            { return 
                "  Define " + A + "\n" + 
                "  Define " + vx0 + 
                   " = " + A + "*e^{(" + frac + ")*x} - " + 
                        dcnst + "*x/(" + coefd1 + ")\n";
            }  
            else 
            { return 
                "  Define " + A + "\n" + 
                "  Define " + vx0 + 
                   " = " + A + "*e^{(" + frac + ")*x} - " + 
                        dcnst + "/(" + coeff + ")\n";
            }  
          } 
        } 
        else if (maxdp == 1 && vdiffs.size() == 2 && 
                 hasX == false && maxvdiffp == 2) 
        { // 1st order quadratic homogenous equation 

          String dd2coef2 = 
            ASTTerm.coefficientOfSquare(vdiff,expr0); 

          String dd2coef1 = 
            ASTTerm.coefficientOf(vdiff,expr0); 

          Vector dd2vars = new Vector(); 
          dd2vars.add(vdiff); 
          String dd2cnst = 
             ASTTerm.constantTerms(dd2vars,expr0);

          /* JOptionPane.showMessageDialog(null, 
           ">>> Coeffiecient of square of : " + vdiff + " in " + expr0 +  
           " = " + dd2coef2 + " Of " + vdiff + " = " + dd2coef1 + 
           " Constant: " + dd2cnst, 
           "", 
           JOptionPane.INFORMATION_MESSAGE); */ 

          String quadf1 = 
              AuxMath.quadraticFormula1(dd2coef2, dd2coef1, 
                                        dd2cnst); 
          String quadf2 = 
              AuxMath.quadraticFormula2(dd2coef2, dd2coef1, 
                                        dd2cnst);
 
          return "  Solve " + vx0 + "´ - " + quadf1 + " = 0 for " + vx0 + "\n" + 
               "  Solve " + vx0 + "´ - " + quadf2 + " = 0 for " + vx0 + "\n\n"; 
        } 
        else if (maxdp == 2 && vdiffs.size() == 3 && 
                 hasX == false && maxvdiffp <= 1) 
        { // 2nd order linear homogenous equation 

          String coeff = "" + alldcoefs.get(0); 
          String coefd1 = "" + alldcoefs.get(1); 
          String coefd2 = "" + alldcoefs.get(2); 

          ASTTerm vdiff1 =
            ASTTerm.constructNDifferential(1, var0); 

          Vector dd3vars = new Vector(); 
          dd3vars.add(var0); 
          dd3vars.add(vdiff1); 
          dd3vars.add(vdiff); 
          String dd3cnst = 
            ASTTerm.constantTerms(dd3vars,expr0);

          String quadf1 = 
              AuxMath.quadraticFormula1(coefd2, coefd1, 
                                        coeff); 
          String quadf2 = 
              AuxMath.quadraticFormula2(coefd2, coefd1, 
                                        coeff); 

          String specialSolution = ""; 
          if ("0".equals(dd3cnst) || "0.0".equals(dd3cnst))         
          { } 
          else 
          { // Solution is above + g
            // for g = -dd3cnst/coeff or g = -dd3cnst*x/coefd1
 
            if ("0".equals(coeff) || "0.0".equals(coeff))
            { specialSolution = " - " + 
                        dd3cnst + "*x/(" + coefd1 + ")\n";
            }  
            else 
            { specialSolution = " - " + 
                        dd3cnst + "/(" + coeff + ")\n";
            }  
          } 
          
          mathoclfunctionIndex++; 
          String A = "A" + mathoclfunctionIndex; 
          String B = "B" + mathoclfunctionIndex; 

          if (quadf1.equals(quadf2))
          { return 
              "  Define " + A + "\n" + 
              "  Define " + B + "\n" + 
              "  Define " + vx0 + " = (" + A + " + " + B + "*x)*e^{" + quadf1 + "*x}" + specialSolution;
          } 
          return 
            "  Define " + A + "\n" + 
            "  Define " + B + "\n" + 
            "  Define " + vx0 + " = " + A + "*e^{(" + quadf1 + ")*x} + " + B + "*e^{(" + quadf2 + ")*x}" + specialSolution; 
        } 
        else if (maxdp == 1 && vdiffs.size() == 2 && 
                 maxvdiffp <= 1) 
        { // 1st order, 1st degree inhomogenous:
          // a*f' + b*f - c = 0 where a, b, c can involve x
           
          String coeff = "" + alldcoefs.get(0); // b 
          String coefd1 = "" + alldcoefs.get(1); // a
          String cc = dcnst; // c

          // Solution is 
          // f = (1/J)*(‡ (c/a)*J dx) + A/J   where
          // J = exp(‡ b/a dx)

          if ("1".equals(coefd1) || "1.0".equals(coefd1)) { } 
          else 
          { cc = "(" + cc + ")/" + coefd1; 
            coeff = "(" + coeff + ")/" + coefd1; 
          } 

          String Jvalue = "e^{‡ " + coeff + " dx}";
          ASTTerm.mathoclfunctionIndex++; 
          String J = "J" + ASTTerm.mathoclfunctionIndex + "(x)"; 
          String A = "A" + ASTTerm.mathoclfunctionIndex; 

          String integralTerm = 
            "(1/" + J + ")*(‡ (" + cc + ")*" + J + " dx) +"; 
          if ("0".equals(cc) || "0.0".equals(cc))
          { integralTerm = ""; } 

          return "  Define " + J + " = " + Jvalue + "\n" +
            "  Define " + A + "\n" + 
            "  Define " + vx0 + "(x) = " + integralTerm + " " + A + "/" + J + "\n";   
        } 
 
        return "  Solve " + exprs.literalForm() + " for " + vars.literalForm() + "\n";
      } 
      else 
      { JOptionPane.showMessageDialog(null, 
              ">>> Unrecognised formula", 
              "", 
              JOptionPane.INFORMATION_MESSAGE);
          return "  Solve " + exprs.literalForm() + " for " + vars.literalForm() + "\n";
      } 
    }  

    /* Simultaneous and quadratic equations */ 

    for (int i = 0; i < varTerms.size(); i++) 
    { ASTTerm var = (ASTTerm) varTerms.get(i); 

      if (",".equals(var + "")) 
      { continue; } 

      variables.add(var + ""); 

      for (int j = 0; j < exprTerms.size(); j++)
      { ASTTerm expr = (ASTTerm) exprTerms.get(j); 

        if (",".equals(expr + "")) 
        { continue; } 
          
        String coef = ASTTerm.coefficientOf(var,expr); 
        System.out.println(">>> Coefficient of " + var + " in " + expr + " is " + coef); 

        Vector vcoeffs = 
               (Vector) varCoefficients.get(var + ""); 
        if (vcoeffs == null) 
        { vcoeffs = new Vector(); }
        vcoeffs.add(coef); 
        varCoefficients.put(var + "", vcoeffs); 

        System.out.println(">>> Var coefficients: " + 
                           varCoefficients); 
          /* JOptionPane.showMessageDialog(null, 
              ">>> Var coefficients: " + 
                             varCoefficients, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);
            */ 
      } 
    }

      Vector constantTerms = new Vector(); 

      for (int j = 0; j < exprTerms.size(); j++)
      { ASTTerm expr = (ASTTerm) exprTerms.get(j); 

        if (",".equals(expr + "")) 
        { continue; } 

        String cnst = ASTTerm.constantTerms(varTerms,expr);
        constantTerms.add(cnst); 
      } 
  /*
      JOptionPane.showMessageDialog(null, 
              ">>> Constant terms: " + 
                             constantTerms, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);  */ 

      // The determinant of the varCoefficients
      // is the divisor. One row for each equation
      // One column for each variable.

      Vector divisorMatrix = new Vector(); 
      for (int j = 0; j < constantTerms.size(); j++)
      { Vector vvrow = new Vector(); 
        for (int i = 0; i < variables.size(); i++) 
        { String var = (String) variables.get(i);
          Vector vcoefs = (Vector) varCoefficients.get(var); 
          vvrow.add(vcoefs.get(j)); 
        } 
        divisorMatrix.add(vvrow); 
      }

      int msize = divisorMatrix.size(); 

      String divisorString = "1"; 

      if (AuxMath.isNumericMatrix(divisorMatrix))
      { double commonDivisor = 
          Math.pow(-1,msize) *
          AuxMath.determinant(msize, divisorMatrix); 
        divisorString = "(" + commonDivisor + ")"; 
      } 
      else if (msize % 2 == 0) 
      { divisorString = "(" + 
          AuxMath.symbolicDeterminant(msize, divisorMatrix) + ")"; 
      } 
      else  
      { divisorString = "(-1*(" + 
          AuxMath.symbolicDeterminant(msize, divisorMatrix) + "))"; 
      } 

  /* 
      JOptionPane.showMessageDialog(null, 
              ">>> Divisor matrix: " + 
              divisorMatrix + " " + 
              divisorString, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);   */ 

      // The determinant of the varCoefficients
      // is the numerator for var. One row for each equation
      // One column for each variable except the var. 1st var
      // +ve, 2nd -ve, etc.

      int factor = 1; 

      for (int vind = 0; vind < variables.size(); vind++)
      { String vx = (String) variables.get(vind);

        Vector varMatrix = new Vector(); 
        for (int j = 0; j < constantTerms.size(); j++)
        { Vector vv1row = new Vector(); 
          for (int i = 0; i < variables.size(); i++) 
          { if (i != vind) 
            { String var = (String) variables.get(i);
              Vector vcoefs = 
                  (Vector) varCoefficients.get(var); 
              vv1row.add(vcoefs.get(j)); 
            } 
          } 
          vv1row.add(constantTerms.get(j)); 
          varMatrix.add(vv1row); 
        }

        String varNumeratorString = "1"; 

        if (AuxMath.isNumericMatrix(varMatrix))
        { 
          double varNumerator = 
            factor*AuxMath.determinant(msize, varMatrix); 
          varNumeratorString = "" + varNumerator; 
        } 
        else 
        { varNumeratorString = 
            factor + "*" + 
            AuxMath.symbolicDeterminant(msize, varMatrix);
        } 

      /* 
        JOptionPane.showMessageDialog(null, 
              "  Define " + vx + " = " + 
              varNumeratorString + "/" + divisorString, 
              "", 
              JOptionPane.INFORMATION_MESSAGE);  */ 
  
        factor = factor*-1;

        if (!("0".equals(divisorString)) && 
            AuxMath.isNumeric(varNumeratorString) && 
            AuxMath.isNumeric(divisorString))
        { Double numer = 
             Double.parseDouble(varNumeratorString); 
          Double denom = Double.parseDouble(divisorString); 
          res = res + "  Define " + vx + " = " +
                                               numer/denom; 
        } 
        else 
        { res = res + "  Define " + vx + " = " + 
              varNumeratorString + "/" + divisorString + "\n"; 
        } 
    }  
    return res; 
  } 

  public static ASTTerm constructNPower(
                                String nx, ASTTerm v)
  { ASTBasicTerm nexpr = 
      new ASTBasicTerm("basicExpression", nx); 
    Vector dpars = new Vector();
    dpars.add(v);
    dpars.add(new ASTSymbolTerm("^{")); 
    dpars.add(nexpr);  
    dpars.add(new ASTSymbolTerm("}")); 
    ASTTerm res = 
      new ASTCompositeTerm("factor2Expression", dpars);
    return res;  
  }  

  public static ASTTerm constructNPowerProd(
                                int n, ASTTerm v)
  { if (n == 1) 
    { return v; } 

    ASTTerm res = v; 
    for (int i = 2; i <= n; i++) 
    { Vector dpars = new Vector();
      dpars.add(v);
      dpars.add(new ASTSymbolTerm("*")); 
      dpars.add(res);  
      res = 
        new ASTCompositeTerm("factorExpression", dpars);
    } 
    return res;  
  }  

  public static Vector constructNPowers(
                   int n, ASTTerm v, ASTTerm expr0, 
                   Vector coefs)
  { // for i = 1 upto n, find if i-th power of v
    // occurs in expr0 and what its coefficient is
    
    Vector res = new Vector(); 

    String vcoef = ASTTerm.coefficientOf(v,expr0);
    res.add(v); 
    coefs.add(vcoef); 

    for (int i = 2; i <= n; i++) 
    {  
      ASTTerm vpow =
          ASTTerm.constructNPower("" + i, v); 
 
      String dcoef = ASTTerm.coefficientOfPower(v,i,expr0); 

      // ASTTerm vpow2 =
      //     ASTTerm.constructNPowerProd(i, v); 
 
      // String dcoef2 = ASTTerm.coefficientOf(vpow2,expr0); 

      res.add(vpow); 
      coefs.add(dcoef); 
    } 
    return res; 
  }  

  public static Vector constructNPowers(
                   Vector powers, ASTTerm v, ASTTerm expr0, 
                   Vector coefs)
  { // for i : powers, find if i-th power of v
    // occurs in expr0 and what its coefficient is
    
    Vector processed = new Vector(); 
    Vector res = new Vector(); 

    for (int i = 0; i < powers.size(); i++) 
    { String pow = "" + powers.get(i); 
      double dd = Double.parseDouble(pow); 
      int j = (int) dd; 

      if (processed.contains("" + j)) 
      { continue; } 

      processed.add("" + j); 

      if (j > 2)
      { ASTTerm vpow =
          ASTTerm.constructNPower("" + j, v); 
        // String vcoef = ASTTerm.coefficientOf(vpow,expr0);
        res.add(vpow); 
      
        // ASTTerm vpow2 =
        //   ASTTerm.constructNPowerProd(j, v); 
 
        String vcoef2 = 
          ASTTerm.coefficientOfPower(v,j,expr0); 

        coefs.add(vcoef2);
      } 
      else if (j == 1)  
      { String vcoef = ASTTerm.coefficientOf(v,expr0);
        res.add(v); 
        coefs.add(vcoef); 
      }
      else if (j == 2)  
      { String vcoef2 = ASTTerm.coefficientOfSquare(v,expr0);
        ASTTerm vpower2 =
          ASTTerm.constructNPower("2", v); 
        res.add(vpower2); 
        coefs.add(vcoef2); 
      }
    } 
    return res; 
  }  

  public static Vector constructAllPowers(int n, 
                   Vector powers, ASTTerm v, ASTTerm expr0, 
                   Vector coefs)
  { // for i = n down to 1, if  
    // i : powers, find coefficient of i-th power of v
    // otherwise give coefficient 0
    
    Vector processed = new Vector(); 
    Vector res = new Vector(); 

    for (int i = n; i > 1; i--) 
    { if (processed.contains("" + i)) 
      { continue; } 

      processed.add("" + i); 

      if (powers.contains(i))
      { if (i > 2)
        { ASTTerm vpow =
            ASTTerm.constructNPower("" + i, v); 
          String vcoef = ASTTerm.coefficientOf(vpow,expr0);
          res.add(vpow); 
      
          ASTTerm vpow2 =
            ASTTerm.constructNPowerProd(i, v); 
 
          String vcoef2 = ASTTerm.coefficientOf(vpow2,expr0); 

          coefs.add("(" + vcoef + " + " + vcoef2 + ")");
        } 
        else if (i == 2)  
        { String vcoef2 = 
            ASTTerm.coefficientOfSquare(v,expr0);
          ASTTerm vpower2 =
            ASTTerm.constructNPower("2", v); 
          res.add(vpower2); 
          coefs.add(vcoef2); 
        }
      } 
      else // i not in powers
      { ASTTerm vpoweri =
            ASTTerm.constructNPower(i + "", v); 
        res.add(vpoweri); 
        coefs.add("0"); 
      } 
    } 

    String vcoef = ASTTerm.coefficientOf(v,expr0);
    res.add(v); 
    coefs.add(vcoef); 
   
    Vector vs = new Vector(); 
    vs.add(v); 

    String cnst = constantTerms(vs, expr0); 

    res.add(new ASTBasicTerm("basicExpression", "1")); 
    coefs.add(cnst); 

    return res; 
  }  

  public static ASTTerm constructNDifferential(
                                int n, ASTTerm v)
  { ASTTerm res = v; 
    for (int i = 0; i < n; i++) 
    { Vector dpars = new Vector();
      dpars.add(res); 
      dpars.add(new ASTSymbolTerm("´")); 
      res = new ASTCompositeTerm("factorExpression", dpars);
    } 
    return res;  
  }  
        
  public static Vector constructNDifferentials(
            int n, ASTTerm v, ASTTerm expr0, 
            Vector coefs)
  { // for i = 1 upto n, find if i-th diff of v
    // occurs in expr0 and what its coefficient is
    
    Vector res = new Vector(); 

    String vcoef = ASTTerm.coefficientOf(v,expr0);
    res.add(v); 
    coefs.add(vcoef); 

    for (int i = 1; i <= n; i++) 
    {  
      ASTTerm vdiff =
          ASTTerm.constructNDifferential(i, v); 
 
      // Vector dpowers = ASTTerm.powersOf(vdiff,expr0);
      String dcoef = ASTTerm.coefficientOf(vdiff,expr0); 

      res.add(vdiff); 
      coefs.add(dcoef); 
    } 
    return res; 
  } 

  public static Vector constructNDifferentialsPowers(
            int n, ASTTerm v, ASTTerm expr0, 
            Vector coefs, Vector powers)
  { // for i = 1 upto n, find if i-th diff of v
    // occurs in expr0 and what its coefficient and powers are
    
    Vector res = new Vector(); 

    String vcoef = ASTTerm.coefficientOf(v,expr0);
    res.add(v); 
    coefs.add(vcoef); 

    Vector pwrs = ASTTerm.powersOf(v,expr0); 
    powers.add(pwrs); 

    for (int i = 1; i <= n; i++) 
    {  
      ASTTerm vdiff =
          ASTTerm.constructNDifferential(i, v); 
 
      // Vector dpowers = ASTTerm.powersOf(vdiff,expr0);
      String dcoef = ASTTerm.coefficientOf(vdiff,expr0); 

      res.add(vdiff); 
      coefs.add(dcoef);

      Vector dpwrs = ASTTerm.powersOf(vdiff, expr0); 
      powers.add(dpwrs);  
    } 

    return res; 
  } 

  public static Vector mathOCLidentifiers(
                           ASTTerm expr)
  { // All (identifier x) terms in expr. 
    Vector res = new Vector(); 
    if (expr == null || expr instanceof ASTSymbolTerm) 
    { return res; } 
    return expr.allTagSubterms("identifier"); 
  }  
    
  public static String mathOCLDefinition2Operation(
                            ASTTerm defn)
  { // Define v = expr
    // becomes operation vDefinition(pars) : double
    //         pre: true post: result = expr; 

    String res = ""; 

    if (defn instanceof ASTCompositeTerm)
    { ASTCompositeTerm trm = (ASTCompositeTerm) defn; 
      if ("formula".equals(trm.getTag()) && 
          trm.getTerms().size() > 3 &&        
          "Define".equals(trm.getTerm(0) + "") && 
          "=".equals(trm.getTerm(2) + ""))
      { String var = trm.getTerm(1) + ""; 
        ASTTerm expr = trm.getTerm(3); 
        Vector invars = ASTTerm.mathOCLidentifiers(expr); 

        res = "  operation " + var + "Definition("; 
        for (int i = 0; i < invars.size(); i++) 
        { ASTTerm vused = (ASTTerm) invars.get(i); 
          String pvar = vused.literalForm();
          res = res + pvar + " : double"; 
          if (i < invars.size() - 1)
          { res = res + ", "; } 
        }
        res = res + ") : double\n" + 
              "  pre: true\n" + 
              "  post: result = " + expr.literalForm() + ";\n\n";  
      }
    } 
    return res; 
  } 

  public abstract void checkMathOCL(); 

  public abstract Vector mathOCLVariables(); 

  public abstract ASTTerm mathOCLSubstitute(String var, ASTTerm repl); 

  public static void main(String[] args) 
  { // ASTBasicTerm t = new ASTBasicTerm("OclBasicExpression", "true"); 
    // System.out.println(t.isInteger()); 
    // System.out.println(t.isBoolean());

   /* ASTBasicTerm tt1 = new ASTBasicTerm("t1", "aa"); 
    ASTBasicTerm tt2 = new ASTBasicTerm("t2", "bb");
    ASTSymbolTerm tts = new ASTSymbolTerm("&"); 
 

    Vector vect = new Vector(); 
    vect.add(tt1); 
    vect.add(tts); 
    vect.add(tt2); 
    
    ASTCompositeTerm ttc = 
       new ASTCompositeTerm("ct", vect); 

    System.out.println(ttc.tagFunction());

    System.out.println(ASTTerm.isSubterm(tt1, tt2)); 
    System.out.println(ASTTerm.isSubterm(tt1, ttc)); 
    System.out.println(ASTTerm.isSubterm(tt2, ttc));

    System.out.println(ttc);  
    ASTTerm subst = ttc.substituteEq("bb", tt1); 
    System.out.println(subst);  

    ASTTerm.setTaggedValue("x", "defined", "true");
    ASTTerm.addStereo("x", "int");  
    ASTTerm.setTaggedValue("x", "defined", "false");
    ASTTerm.addStereo("x", "String");  
    System.out.println(ASTTerm.metafeatures.get("x"));

    System.out.println(ASTTerm.isIntegerValued(0.5)); 
    System.out.println(ASTTerm.isIntegerValued(0.0)); 
    System.out.println(ASTTerm.isIntegerValued(3.0)); 
    System.out.println(ASTTerm.isIntegerValued(-1.5)); 
    System.out.println(ASTTerm.isIntegerValued(-5.0)); 

    Vector trms1 = new Vector(); 
    trms1.add(new ASTSymbolTerm("exp")); 
    trms1.add(new ASTSymbolTerm("(")); 
    trms1.add(new ASTBasicTerm("basicExpression", "4")); 
    trms1.add(new ASTSymbolTerm(")")); 

    ASTCompositeTerm expx = 
        new ASTCompositeTerm("basicExpression", trms1); 

    Vector terms3 = new Vector(); 
    terms3.add(new ASTBasicTerm("basicExpression", "0")); 
    terms3.add(new ASTSymbolTerm("<")); 
    terms3.add(expx); 
    ASTCompositeTerm leq = 
      new ASTCompositeTerm("equalityExpression", terms3); 

    Vector trms2 = new Vector(); 
    trms2.add(new ASTSymbolTerm("exp")); 
    trms2.add(new ASTSymbolTerm("(")); 
    trms2.add(new ASTBasicTerm("identifier", "_V")); 
    trms2.add(new ASTSymbolTerm(")")); 

    ASTCompositeTerm scheme = 
        new ASTCompositeTerm("basicExpression", trms2);

    java.util.HashMap mm = new java.util.HashMap(); 
    leq.hasMatch(scheme, mm); 
    System.out.println(mm);  

    System.out.println(scheme.instantiate(mm)); */ 

    /* (3*x - 2)/(2*x - 1) */ 

    ASTBasicTerm ttx = new ASTBasicTerm("basicExpression", "x"); 
    ASTBasicTerm tt3 = new ASTBasicTerm("basicExpression", "3");
    ASTSymbolTerm ttmult = new ASTSymbolTerm("*"); 

    Vector vect0 = new Vector(); 
    vect0.add(ttx); 
    vect0.add(ttmult); 
    vect0.add(ttx); 
    
    ASTCompositeTerm ttxx = 
       new ASTCompositeTerm("factorExpression", vect0); 
    // x*x
 
    Vector vect = new Vector(); 
    vect.add(tt3); 
    vect.add(ttmult); 
    vect.add(ttxx); 
    
    ASTCompositeTerm ttc = 
       new ASTCompositeTerm("factorExpression", vect); 
    // 3*x*x

    ASTBasicTerm tt2 = new ASTBasicTerm("basicExpression", "2");
    ASTSymbolTerm ttsub = new ASTSymbolTerm("-"); 

    Vector vect1 = new Vector(); 
    vect1.add(ttc); 
    vect1.add(ttsub); 
    vect1.add(tt2); 
    ASTCompositeTerm ttc1 = 
       new ASTCompositeTerm("additiveExpression", vect1); 
    // 3*x*x - 2

    System.out.println(ttc1.literalFormSpaces()); 
    System.out.println(ASTTerm.isMathOCLPolynomial(ttc1,ttx)); 
    System.out.println(ASTTerm.powersOf(ttx,ttc1)); 

    
    Vector vect2 = new Vector(); 
    vect2.add(tt2); 
    vect2.add(ttmult); 
    vect2.add(ttx); 
    
    ASTCompositeTerm ttc2 = 
       new ASTCompositeTerm("factorExpression", vect2); 
    // 2*x

    ASTBasicTerm tt1 = new ASTBasicTerm("basicExpression", "1");

    Vector vect3 = new Vector(); 
    vect3.add(ttc2); 
    vect3.add(ttsub); 
    vect3.add(tt1); 
    ASTCompositeTerm ttc3 = 
       new ASTCompositeTerm("additiveExpression", vect3); 
    // 2*x - 1

    System.out.println(ttc3.literalFormSpaces());
    System.out.println(ASTTerm.isMathOCLPolynomial(ttc3,ttx));
    // System.out.println(ASTTerm.powersOf(ttx,ttc3)); 

    String divs = 
      ASTTerm.polynomialDivision(ttc1,ttc3,ttx); 
    System.out.println(divs);

    String mults = 
      ASTTerm.polynomialMultiplication(ttc1,ttc3,ttx); 
    System.out.println(mults);

    Vector mv = new Vector(); 
    mv.add(ttc1); mv.add(ttmult); mv.add(ttc3); 
    ASTTerm mult = 
      new ASTCompositeTerm("factorExpression", mv); 
    Vector pwrs = ASTTerm.powersOf(ttx,mult); 
    System.out.println(pwrs); 
    Vector coefs = new Vector(); 
    // Vector pows = ASTTerm.constructNPowers(
    //                             pwrs, ttx, mult, 
    //                             coefs);
    // System.out.println(coefs); 

    Vector vvx = new Vector(); 
    vvx.add(ttx); 

    String p3 = coefficientOfPower(ttx, 3, mult); 
    String p2 = coefficientOfPower(ttx, 2, mult); 
    String p1 = coefficientOfPower(ttx, 1, mult); 
    String p0 = constantTerms(vvx, mult); 

    System.out.println(p3 + " " + p2 + " " + p1 + " " + p0); 
  }
} 

  /* 
    Vector consts = 
      randomBasicASTTermsForTag("Const", 1, 50);
    Vector ops = new Vector(); 
    ops.add(new ASTSymbolTerm("+"));  
    ops.add(new ASTSymbolTerm("-"));  
    Vector vars = 
      randomBasicASTTermsForTag("Var", 1, 50);
    Vector subs = new Vector(); 
    subs.add(consts); 
    subs.add(ops); 
    subs.add(vars); 


    Vector exprs = 
      randomCompositeASTTermsForTag("Expr", subs, 3, 30); 
      // size 3

    Vector subsx = new Vector(); 
      subsx.add(exprs); 
      subsx.add(ops); 
      subsx.add(exprs); 

    Vector nestedexprs = 
      randomCompositeASTTermsForTag("Expr", subsx, 3, 30);
      // size 7

    Vector compars = new Vector(); 
    compars.add(new ASTSymbolTerm("<")); 
    compars.add(new ASTSymbolTerm(">")); 
    compars.add(new ASTSymbolTerm("==")); 


    Vector subs1 = new Vector(); 
    subs1.add(exprs); 
    subs1.add(compars); 
    subs1.add(exprs); 

    Vector cmps = 
      randomCompositeASTTermsForTag("Cmp", subs1, 3, 30);
  
    System.out.println(cmps); // each has size 7

    Vector assgn = new Vector(); 
    assgn.add(new ASTSymbolTerm("=")); 

    Vector subs2 = new Vector(); 
    subs2.add(vars); 
    subs2.add(assgn); 
    subs2.add(exprs); 

    Vector assgns = 
      randomCompositeASTTermsForTag("Assign", subs2, 3, 30);

    // System.out.println(assgns); 
    // (Assign v = e) with size 5

    Vector subs2x = new Vector(); 
    subs2x.add(vars); 
    subs2x.add(assgn); 
    subs2x.add(nestedexprs); 

    Vector assgns2 = 
      randomCompositeASTTermsForTag("Assign", subs2x, 3, 30);

    Vector sqsym = new Vector(); 
    sqsym.add(new ASTSymbolTerm(";")); 

    Vector subsseq = new Vector(); 
    subsseq.add(assgns2); 
    subsseq.add(sqsym); 
    subsseq.add(assgns2); 

    Vector sqs = 
      randomCompositeASTTermsForTag("Seq", subsseq, 3, 100);

    Vector ifsym = new Vector(); 
    ifsym.add(new ASTSymbolTerm("if"));

    Vector thensym = new Vector(); 
    thensym.add(new ASTSymbolTerm("then"));

    Vector elsesym = new Vector(); 
    elsesym.add(new ASTSymbolTerm("else"));

    Vector endifsym = new Vector(); 
    endifsym.add(new ASTSymbolTerm("endif"));

    Vector subifs = new Vector(); 
    subifs.add(ifsym); 
    subifs.add(cmps); 
    subifs.add(thensym); 
    subifs.add(assgns2); 
    subifs.add(elsesym); 
    subifs.add(assgns); 
    subifs.add(endifsym); 

    Vector simpleifs = 
      randomCompositeASTTermsForTag("If", subifs, 7, 40);
    // size 25

    Vector subs3 = new Vector(); 
    subs3.add(assgns); 
    subs3.add(sqsym); 
    subs3.add(assgns); 

    Vector sqs0 = 
      randomCompositeASTTermsForTag("Seq", subs3, 3, 30);
    // size 11

    Vector subs3x = new Vector(); 
    subs3x.add(assgns); 
    subs3x.add(sqsym); 
    subs3x.add(sqs0); 

    Vector sqs1x = 
      randomCompositeASTTermsForTag("Seq", subs3x, 3, 100);
    // size 17

  
    Vector subs4 = new Vector(); 
    subs4.add(ifsym); 
    subs4.add(cmps); 
    subs4.add(thensym); 
    // subs4.add(sqs1x);
    subs4.add(simpleifs);  
    subs4.add(elsesym); 
    // subs4.add(sqs1x); 
    subs4.add(simpleifs); 
    subs4.add(endifsym); 

    Vector sqs1 = 
      randomCompositeASTTermsForTag("If", subs4, 7, 100);
  
   
    // System.out.println(sqs1); 
    // Size 23 for assgns; Size 33 for sqs0; 45 with sqs1
    // Size 60 with simpleifs

    Vector subs4x = new Vector(); 
    subs4x.add(ifsym); 
    subs4x.add(cmps); 
    subs4x.add(thensym); 
    subs4x.add(simpleifs); 
    subs4x.add(elsesym); 
    subs4x.add(assgns); 
    subs4x.add(endifsym); 

    Vector sqs4x = 
      randomCompositeASTTermsForTag("If", subs4x, 7, 100);
    // size 61 or 41 with simpleifs
    
   
    Vector forsym = new Vector(); 
    forsym.add(new ASTSymbolTerm("for"));
    Vector eqsym = new Vector(); 
    eqsym.add(new ASTSymbolTerm("="));
    Vector semisym = new Vector(); 
    semisym.add(new ASTSymbolTerm(";"));
    Vector dosym = new Vector(); 
    dosym.add(new ASTSymbolTerm("do"));
    Vector endforsym = new Vector(); 
    endforsym.add(new ASTSymbolTerm("endfor"));

    Vector subs5 = new Vector(); 
    subs5.add(forsym); 
    subs5.add(vars); 
    subs5.add(eqsym); 
    subs5.add(consts); 
    subs5.add(semisym); 
    subs5.add(cmps); 
    subs5.add(semisym); 
    subs5.add(exprs); 
    subs5.add(dosym); 
    // subs5.add(assgns);
    subs5.add(simpleifs);  
    subs5.add(endforsym); 

    Vector fors = 
      randomCompositeASTTermsForTag("For", subs5, 11, 100);
    // Each of size 22 with assgns in do body; 
    // 42 with simpleifs
         


    File chtml = new File("output/asts.txt"); 
    try
    { PrintWriter chout = new PrintWriter(
                              new BufferedWriter(
                                new FileWriter(chtml)));
      for (int i = 0; i < sqs1.size(); i++)
      { ASTTerm oclexample = (ASTTerm) sqs1.get(i); 
        chout.println(oclexample); 
      }
      chout.close(); 
    
    } 
    catch (Exception _fex) 
    { System.err.println("! No file: output/asts.txt"); }  

  } */ 



/* tree2tree dataset format: */ 

/* {"target_ast": 
      {"root": "<LET>", 
       "children": 
          [{"root": "blank", 
              "children": []}, 
           {"root": "<IF>", 
              "children": 
                [{"root": "<CMP>", 
                  "children": 
                     [{"root": "<Expr>", 
                       "children": 
                          [{"root": "0", 
                               "children": []}]
                      }, 
                      {"root": ">", 
                          "children": []}, 
                      {"root": "<Expr>", 
                          "children": 
                            [{"root": "x", 
                              "children": []}]
                      }
                     ]
                 }, 

                 {"root": "<LET>", 
                     "children": 
                        [{"root": "y", 
                             "children": []}, 
                         {"root": "<Expr>", 
                          "children": 
                             [{"root": "y", "children": []}]
                         }, 
                         {"root": "<UNIT>", "children": []}
                        ]
                 }, 
                 {"root": "<LET>", 
                     "children": 
                        [{"root": "y", "children": []}, 
                         {"root": "<Expr>", 
                             "children": 
                                [{"root": "y", 
                                  "children": []}]
                         }, 
                         {"root": "<UNIT>", 
                             "children": []}
                        ]
                 }
               ]}, 
             {"root": "<LET>", "children": [{"root": "x", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}, {"root": "<LET>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}, {"root": "<UNIT>", "children": []}]}]}]}, 

"source_prog": ["if", "0", ">", "x", "then", "y", "=", "y", "else", "y", "=", "y", "endif", ";", "x", "=", "1", ";", "y", "=", "1"], 

"source_ast": {"root": "<SEQ>", "children": [{"root": "<IF>", "children": [{"root": "<CMP>", "children": [{"root": "<Expr>", "children": [{"root": "0", "children": []}]}, {"root": ">", "children": []}, {"root": "<Expr>", "children": [{"root": "x", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "y", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "y", "children": []}]}]}]}, {"root": "<SEQ>", "children": [{"root": "<ASSIGN>", "children": [{"root": "x", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}]}, {"root": "<ASSIGN>", "children": [{"root": "y", "children": []}, {"root": "<Expr>", "children": [{"root": "1", "children": []}]}]}]}]}, 

"target_prog": ["let", "blank", "=", "if", "0", ">", "x", "then", "let", "y", "=", "y", "in", "()", "else", "let", "y", "=", "y", "in", "()", "in", "let", "x", "=", "1", "in", "let", "y", "=", "1", "in", "()"]} */ 

/* Raw format: 
{"for_tree": 
   ["<SEQ>", ["<ASSIGN>", "y", ["<Expr>", "y"]], 
             ["<SEQ>", 
                ["<IF>", 
                   ["<CMP>", ["<Expr>", 1], "<", ["<Expr>", 0]], 
                   ["<ASSIGN>", "x", ["<Expr>", 0]], 
                   ["<ASSIGN>", "x", ["<Expr>", 0]]
                ], 
                ["<ASSIGN>", "y", ["<Expr>", 1]]
              ]
    ], 

  "raw_for": "y = y ; if 1 < 0 then x = 0 else x = 0 endif ; y = 1", "raw_lam": "let y = y in let blank = if 1 < 0 then let x = 0 in () else let x = 0 in () in let y = 1 in ()", 

  "lam_tree": 
     ["<LET>", "y", 
        ["<Expr>", "y"], 
        ["<LET>", "blank", 
            ["<IF>", 
                ["<CMP>", ["<Expr>", 1], "<", ["<Expr>", 0]], 
                ["<LET>", "x", 
                        ["<Expr>", 0], "<UNIT>"], 
                        ["<LET>", "x", ["<Expr>", 0], "<UNIT>"]
                 ], 
                 ["<LET>", "y", ["<Expr>", 1], "<UNIT>"]
             ]
      ]
   } */ 

